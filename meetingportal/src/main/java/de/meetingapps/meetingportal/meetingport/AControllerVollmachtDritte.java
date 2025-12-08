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
import de.meetingapps.meetingportal.meetComEclM.EclParamM;
import de.meetingapps.meetingportal.meetComEclM.EclPortalTexteM;
import de.meetingapps.meetingportal.meetComEclM.EclTeilnehmerLoginM;
import de.meetingapps.meetingportal.meetComEclM.EclZugeordneteMeldungM;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@RequestScoped
@Named
@Deprecated
public class AControllerVollmachtDritte {

    @Inject
    ADlgVariablen aDlgVariablen;
    @Inject
    AFunktionen aFunktionen;
    @Inject
    EclParamM eclParamM;
    @Inject
    EclPortalTexteM eclTextePortalM;
    @Inject
    EclTeilnehmerLoginM eclTeilnehmerLoginM;
    @Inject
    EclZugeordneteMeldungM eclZugeordneteMeldungM;
    @Inject
    AControllerVollmachtDritteBestaetigung aControllerVollmachtDritteBestaetigung;

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
        if (!aFunktionen.pruefeStart("aVollmachtDritte")) {
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

        if (eclParamM.getParam().paramPortal.bestaetigenDialog == 1) {
            return aFunktionen.setzeEnde("aVollmachtDritteBestaetigung", true, false);
        } else {
            aFunktionen.setzeEnde("aVollmachtDritteBestaetigung", true, false);
            String naechsteMaske = aControllerVollmachtDritteBestaetigung.doAusstellen();
            if (naechsteMaske.isEmpty()) {
                aFunktionen.setzeEnde("aVollmachtDritte", true, false);
                return "";
            }
            return aFunktionen.setzeEnde(naechsteMaske, true, false);
        }

    }

    public String doZurueck() {
        if (!aFunktionen.pruefeStart("aVollmachtDritte")) {
            return "aDlgFehler";
        }

        if (aDlgVariablen.getAusgewaehlteAktion().compareTo("21") == 0) {
            return aFunktionen.setzeEnde("aNeueWillenserklaerung", true, true);
        } else { /*"29"*/
            aFunktionen.setzeEnde("aNeueWillenserklaerung", true, true);
            return aFunktionen.setzeEnde("aStatus", true, true);
        }
    }

    /**Abmelden*/
    public String doAbmelden() {
        aDlgVariablen.clearLogin();
        return aFunktionen.waehleLogout();
    }

}
