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
import de.meetingapps.meetingportal.meetComEclM.EclDbM;
import de.meetingapps.meetingportal.meetComEclM.EclParamM;
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
public class AControllerVollmachtDritteStornieren {

    @Inject
    ADlgVariablen aDlgVariablen;
    @Inject
    AFunktionen aFunktionen;
    @Inject
    EclPortalTexteM eclTextePortalM;
    @Inject
    EclParamM eclParamM;
    @Inject
    private EclDbM eclDbM;
    @Inject
    EclWillenserklaerungStatusM eclWillenserklaerungStatusM;
    @Inject
    EclZugeordneteMeldungM eclZugeordneteMeldungM;

    /**Tatsächliches Durchführen des Storno einer Vollmacht an Dritte. 
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
        if (!aFunktionen.pruefeStart("aVollmachtDritteStornieren")) {
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
                KonstWillenserklaerung.vollmachtAnDritte, aDlgVariablen.getAusgewaehlteHauptAktion())) {
            eclDbM.closeAllAbbruch();
            return aFunktionen.setzeEnde("aTransaktionAbbruch", true, true);
        }

        BlWillenserklaerung vmWillenserklaerung = new BlWillenserklaerung();
        vmWillenserklaerung.pErteiltAufWeg = aDlgVariablen.getEingabeQuelle();
        vmWillenserklaerung.pErteiltZeitpunkt = aDlgVariablen.getErteiltZeitpunkt();
        vmWillenserklaerung.piMeldungsIdentAktionaer = eclZugeordneteMeldungM.getMeldungsIdent();
        EclPersonenNatJur lPersonenNatJur = new EclPersonenNatJur();
        lPersonenNatJur.ident = eclWillenserklaerungStatusM.getBevollmaechtigterDritterIdent();
        vmWillenserklaerung.pEclPersonenNatJur = lPersonenNatJur;
        vmWillenserklaerung.pWillenserklaerungGeberIdent = -1; /*Aktionär*/
        System.out.println("Storno 3.: pEclPersonenNatJur=" + vmWillenserklaerung.pEclPersonenNatJur.ident
                + " geberIdent=" + vmWillenserklaerung.pWillenserklaerungGeberIdent);
        vmWillenserklaerung.widerrufVollmachtAnDritte(eclDbM.getDbBundle());
        if (vmWillenserklaerung.rcIstZulaessig == false) {
            aDlgVariablen.setFehlerMeldung(eclTextePortalM.getFehlertext(vmWillenserklaerung.rcGrundFuerUnzulaessig));
            aDlgVariablen.setFehlerNr(vmWillenserklaerung.rcGrundFuerUnzulaessig);
            eclDbM.closeAllAbbruch();
            aFunktionen.setzeEnde();
            return "";
        }

        if (eclParamM.getParam().paramPortal.quittungDialog == 1) {
            eclDbM.closeAll();
            return aFunktionen.setzeEnde("aVollmachtDritteStornierenQuittung", true, false);
        } else {

            String returnString = aFunktionen.waehleAusgangsmaske(eclDbM.getDbBundle());
            eclDbM.closeAll();
            return aFunktionen.setzeEnde(returnString, true, true);
        }

    }

    public String doZurueck() {
        if (!aFunktionen.pruefeStart("aVollmachtDritteStornieren")) {
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
