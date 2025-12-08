/*
 *  Copyright 2025 Better Orange IR & HV AG
 *
 *  Licensed under the Meetingbase License (the "License");
 *  Vou may not use this file except in compliance with the License.
 *  You may obtain a copy of the License in the root directory (MEETINGBASE_LICENSE).
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package de.meetingapps.meetingportal.meetingport;

import de.meetingapps.meetingportal.meetComAllg.CaFehler;
import de.meetingapps.meetingportal.meetComBl.BlWillenserklaerung;
import de.meetingapps.meetingportal.meetComEclM.EclAbstimmungenListeM;
import de.meetingapps.meetingportal.meetComEclM.EclDbM;
import de.meetingapps.meetingportal.meetComEclM.EclPortalTexteM;
import de.meetingapps.meetingportal.meetComEclM.EclWillenserklaerungStatusM;
import de.meetingapps.meetingportal.meetComEclM.EclZugeordneteMeldungM;
import de.meetingapps.meetingportal.meetComEntities.EclPersonenNatJur;
import de.meetingapps.meetingportal.meetComKonst.KonstWillenserklaerung;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@RequestScoped
@Named
@Deprecated
public class AControllerEintrittskarteStornieren {

    @Inject
    EclAbstimmungenListeM eclAbstimmungenListeM;

    @Inject
    ADlgVariablen aDlgVariablen;
    @Inject
    AFunktionen aFunktionen;
    @Inject
    EclPortalTexteM eclTextePortalM;
    @Inject
    private EclDbM eclDbM;
    @Inject
    EclWillenserklaerungStatusM eclWillenserklaerungStatusM;
    @Inject
    EclZugeordneteMeldungM eclZugeordneteMeldungM;

    /**Tatsächliches Durchführen des Storno einer Eintrittskarte / Eintrittskarte mit Vollmacht / Gastkarte. Bei Eintrittskarte
     * mit Vollmacht wird Vollmacht mit storniert.
     * 
     * Eingabeparameter:
     * 		aDlgVariablen.ausgewaehlteAktion
     * 		EclWillenserklaerungStatusM
     * 		EclZugeordneteMeldungM
     * 	
     * Ausgabeparameter:
     * 		aDlgVariablen.fehlerMeldung/.fehlerNr
     * Zum Ende hin wird aFunktione.waehleAusgangsmaske aufgerufen, d.h. EclZugeordneteMeldungListeM wird
     * neu ermittelt.
     * 
     * DbBundle wird innerhalb der Funktion gehandled.
     * @return
     */
    public String doStornieren() {
        if (!aFunktionen.pruefeStart("aEintrittskarteStornieren")) {
            return "aDlgFehler";
        }

        eclDbM.openAll();
        aDlgVariablen.clearFehlerMeldung();

        if (aFunktionen.reCheckKeineNeueWillenserklaerungen(eclDbM.getDbBundle(),
                eclZugeordneteMeldungM.getMeldungsIdent(),
                eclZugeordneteMeldungM.getIdentHoechsteWillenserklaerung()) == false) {
            aDlgVariablen.setFehlerMeldung(eclTextePortalM.getFehlertext(CaFehler.afAndererUserAktiv));
            aDlgVariablen.setFehlerNr(CaFehler.afAndererUserAktiv);
            eclDbM.closeAllAbbruch();
            return aFunktionen.setzeEnde("aDlgAbbruch", true, true);
        }

        if (!aFunktionen.pruefeOBWillenserklaerungZulaessig(eclDbM.getDbBundle(),
                KonstWillenserklaerung.neueZutrittsIdentZuMeldung, aDlgVariablen.getAusgewaehlteHauptAktion())) {
            eclDbM.closeAllAbbruch();
            return aFunktionen.setzeEnde("aTransaktionAbbruch", true, true);
        }

        BlWillenserklaerung willenserklaerung = new BlWillenserklaerung();
        willenserklaerung.pErteiltAufWeg = aDlgVariablen.getEingabeQuelle();
        willenserklaerung.pErteiltZeitpunkt = aDlgVariablen.getErteiltZeitpunkt();
        willenserklaerung.piZutrittsIdent.zutrittsIdent = eclWillenserklaerungStatusM.getZutrittsIdent();
        willenserklaerung.piZutrittsIdent.zutrittsIdentNeben = eclWillenserklaerungStatusM.getZutrittsIdentNeben();

        if (aDlgVariablen.getAusgewaehlteAktion().compareTo("16") == 0) {
            willenserklaerung.piKlasse = 0;
        } else {
            willenserklaerung.piKlasse = 1;
        }
        willenserklaerung.pWillenserklaerungGeberIdent = -1; /*Aktionär*/

        willenserklaerung.sperrenZutrittsIdent(eclDbM.getDbBundle());

        /*Falls nicht möglich: Fehlermeldung, zum Anmelden*/
        if (willenserklaerung.rcIstZulaessig == false) {
            aDlgVariablen.setFehlerMeldung(eclTextePortalM.getFehlertext(willenserklaerung.rcGrundFuerUnzulaessig));
            aDlgVariablen.setFehlerNr(willenserklaerung.rcGrundFuerUnzulaessig);
            eclDbM.closeAllAbbruch();
        }

        if (aDlgVariablen.getAusgewaehlteAktion()
                .compareTo("18") == 0) {/*Eintrittskarte mit Vollmacht - nun noch die Vollmacht stornieren*/
            BlWillenserklaerung vmWillenserklaerung = new BlWillenserklaerung();
            vmWillenserklaerung.pErteiltAufWeg = aDlgVariablen.getEingabeQuelle();
            vmWillenserklaerung.pErteiltZeitpunkt = aDlgVariablen.getErteiltZeitpunkt();
            vmWillenserklaerung.piMeldungsIdentAktionaer = eclZugeordneteMeldungM.getMeldungsIdent();
            EclPersonenNatJur lPersonenNatJur = new EclPersonenNatJur();
            lPersonenNatJur.ident = eclWillenserklaerungStatusM.getBevollmaechtigterDritterIdent();
            vmWillenserklaerung.pEclPersonenNatJur = lPersonenNatJur;
            vmWillenserklaerung.pWillenserklaerungGeberIdent = -1; /*Aktionär*/
            vmWillenserklaerung.widerrufVollmachtAnDritte(eclDbM.getDbBundle());
            if (vmWillenserklaerung.rcIstZulaessig == false) {
                aDlgVariablen.setFehlerMeldung(eclTextePortalM.getFehlertext(willenserklaerung.rcGrundFuerUnzulaessig));
                aDlgVariablen.setFehlerNr(willenserklaerung.rcGrundFuerUnzulaessig);
                eclDbM.closeAllAbbruch();
            }
        }

        eclDbM.closeAll();
        return aFunktionen.setzeEnde("aEintrittskarteStornierenQuittung", true, false);
    }

    public String doZurueck() {
        if (!aFunktionen.pruefeStart("aEintrittskarteStornieren")) {
            return "aDlgFehler";
        }
        eclDbM.openAll();
        String naechsteMaske = aFunktionen.waehleAusgangsmaske(eclDbM.getDbBundle());
        eclDbM.closeAll();
        return aFunktionen.setzeEnde(naechsteMaske, true, true);

    }

    /**Abmelden*/
    public String doAbmelden() {
        aDlgVariablen.clearLogin();
        return aFunktionen.waehleLogout();
    }

}
