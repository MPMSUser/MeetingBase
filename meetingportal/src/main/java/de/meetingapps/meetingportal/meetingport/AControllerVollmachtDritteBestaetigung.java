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
import de.meetingapps.meetingportal.meetComEclM.EclTeilnehmerLoginM;
import de.meetingapps.meetingportal.meetComEclM.EclZugeordneteMeldungM;
import de.meetingapps.meetingportal.meetComEntities.EclPersonenNatJur;
import de.meetingapps.meetingportal.meetComKonst.KonstWillenserklaerung;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@RequestScoped
@Named
@Deprecated
public class AControllerVollmachtDritteBestaetigung {

    @Inject
    ADlgVariablen aDlgVariablen;
    @Inject
    AFunktionen aFunktionen;
    @Inject
    EclParamM eclParamM;
    @Inject
    private EclDbM eclDbM;
    @Inject
    EclPortalTexteM eclTextePortalM;
    @Inject
    EclTeilnehmerLoginM eclTeilnehmerLoginM;
    @Inject
    EclZugeordneteMeldungM eclZugeordneteMeldungM;

    /**Erteilen einer Vollmacht an Dritte
     * 
     * Eingabewerte:
     * 		EclTeilnehmerLoginM.anmeldeIdentPersonenNatJur
     * 		EclZugeordneteMeldungM
     * 		aDlgVariablen.vollmachtVorname/.vollmachtName/.vollmachtOrt
     * 
     * Ausgabewerte:
     * 		aDlgVariablen.fehlerMeldung/.fehlerNr
     * 
     * DbBundle wird innerhalb der Funktion gehandelt
     * @return
     */
    public String doAusstellen() {
        if (!aFunktionen.pruefeStart("aVollmachtDritteBestaetigung")) {
            return "aDlgFehler";
        }

        aDlgVariablen.clearFehlerMeldung();

        /*Prüfen, ob Vollmacht vollständig*/
        if (aDlgVariablen.getVollmachtName().isEmpty()) {
            aDlgVariablen.setFehlerMeldung(eclTextePortalM.getFehlertext(CaFehler.afVollmachtNameEKFehlt));
            aDlgVariablen.setFehlerNr(CaFehler.afVollmachtNameEKFehlt);
            aFunktionen.setzeEnde();
            return "";
        }
        if (aDlgVariablen.getVollmachtOrt().isEmpty()) {
            aDlgVariablen.setFehlerMeldung(eclTextePortalM.getFehlertext(CaFehler.afVollmachtOrtEKFehlt));
            aDlgVariablen.setFehlerNr(CaFehler.afVollmachtOrtEKFehlt);
            aFunktionen.setzeEnde();
            return "";
        }

        /*Initialisieren*/
        eclDbM.openAll();

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

        /*Hinweis: die Ausstellung von Vollmachten an Dritte ist immer nur als zusätzliche Aktion - wenn bereits vorher eine
         * Anmeldung erfolgte - möglich. D.h. immer ausgewaehlteHauptaktion=2
         */
        if (eclZugeordneteMeldungM.getArtBeziehung() == 1) {
            vmWillenserklaerung.pWillenserklaerungGeberIdent = -1; /*Aktionär*/
        } else { /*Hier: es handelt sich um eine Untervollmacht. D.h. Geber = angemeldete personNatJur.*/
            vmWillenserklaerung.pWillenserklaerungGeberIdent = eclTeilnehmerLoginM.getAnmeldeIdentPersonenNatJur();
        }

        EclPersonenNatJur personNatJur = new EclPersonenNatJur();

        personNatJur.ident = 0; /*Neue Person*/
        personNatJur.vorname = aDlgVariablen.getVollmachtVorname();
        personNatJur.name = aDlgVariablen.getVollmachtName();
        personNatJur.ort = aDlgVariablen.getVollmachtOrt();
        vmWillenserklaerung.pEclPersonenNatJur = personNatJur;

        vmWillenserklaerung.vollmachtAnDritte(eclDbM.getDbBundle());
        if (vmWillenserklaerung.rcIstZulaessig == false) {
            aDlgVariablen.setFehlerMeldung(eclTextePortalM.getFehlertext(vmWillenserklaerung.rcGrundFuerUnzulaessig));
            aDlgVariablen.setFehlerNr(vmWillenserklaerung.rcGrundFuerUnzulaessig);
            eclDbM.closeAllAbbruch();
            aFunktionen.setzeEnde();
            return "";
        }

        if (eclParamM.getParam().paramPortal.quittungDialog == 1) {
            eclDbM.closeAll();
            return aFunktionen.setzeEnde("aVollmachtDritteQuittung", true, false);
        } else {
            String naechsteMaske = aFunktionen.waehleAusgangsmaske(eclDbM.getDbBundle());
            eclDbM.closeAll();
            return aFunktionen.setzeEnde(naechsteMaske, true, true);
        }

    }

    public String doZurueck() {
        if (!aFunktionen.pruefeStart("aVollmachtDritteBestaetigung")) {
            return "aDlgFehler";
        }
        return aFunktionen.setzeEnde("aVollmachtDritte", true, false);
    }

    /**Abmelden*/
    public String doAbmelden() {
        aDlgVariablen.clearLogin();
        return aFunktionen.waehleLogout();
    }

}
