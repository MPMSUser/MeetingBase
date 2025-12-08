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
import de.meetingapps.meetingportal.meetComEclM.EclDbM;
import de.meetingapps.meetingportal.meetComEclM.EclPortalTexteM;
import de.meetingapps.meetingportal.meetComEclM.EclTeilnehmerLoginM;
import de.meetingapps.meetingportal.meetComEclM.EclWillenserklaerungStatusM;
import de.meetingapps.meetingportal.meetComEclM.EclZugeordneteMeldungM;
import de.meetingapps.meetingportal.meetComEntities.EclAktienregister;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@RequestScoped
@Named
@Deprecated
public class AControllerGastkarte {

    @Inject
    ADlgVariablen aDlgVariablen;
    @Inject
    AFunktionen aFunktionen;
    @Inject
    EclPortalTexteM eclTextePortalM;
    @Inject
    private EclDbM eclDbM;
    @Inject
    EclWillenserklaerungStatusM eckWillenserklaerungStatusM;
    @Inject
    EclZugeordneteMeldungM eclZugeordneteMeldungM;
    @Inject
    private EclTeilnehmerLoginM eclTeilnehmerLoginM;
    @Inject
    AControllerEintrittskarte aControllerEintrittskarte;

    public String doAusstellen() {
        if (!aFunktionen.pruefeStart("aGastkarte")) {
            return "aDlgFehler";
        }
        /****Überprüfen, ob Eingabe vollständig erfolgt ist****/
        boolean ergBool = false;
        int erg;
        EclAktienregister aktienregisterEintrag = null;

        aDlgVariablen.clearFehlerMeldung();
        /*Initialisieren*/
        eclDbM.openAll();

        ergBool = aControllerEintrittskarte.pruefeEingabenFuerGastkarte();
        if (ergBool == false) {
            eclDbM.closeAllAbbruch();
            aFunktionen.setzeEnde();
            return "";
        }

        /*Aktienregister füllen*/
        aktienregisterEintrag = new EclAktienregister();
        aktienregisterEintrag.aktionaersnummer = eclTeilnehmerLoginM.getAnmeldeAktionaersnummer();
        erg = eclDbM.getDbBundle().dbAktienregister.leseZuAktienregisterEintrag(aktienregisterEintrag);
        if (erg <= 0) {/*Aktienregistereintrag nicht mehr vorhanden*/

            aDlgVariablen.setFehlerMeldung(
                    eclTextePortalM.getFehlertext(CaFehler.afAktienregisterEintragNichtMehrVorhanden));
            aDlgVariablen.setFehlerNr(CaFehler.afAktienregisterEintragNichtMehrVorhanden);
            eclDbM.closeAllAbbruch();
        }

        aktienregisterEintrag = eclDbM.getDbBundle().dbAktienregister.ergebnisPosition(0);

        ergBool = aControllerEintrittskarte.anlegenGastkarte(aktienregisterEintrag, eclDbM.getDbBundle());
        if (ergBool == false) {
            if (aDlgVariablen.getFehlerNr() == CaFehler.afAndererUserAktiv) {
                eclDbM.closeAllAbbruch();
                return aFunktionen.setzeEnde("aDlgAbbruch", true, true);

            }
            eclDbM.closeAllAbbruch();
            aFunktionen.setzeEnde();
            return "";
        }

        eclDbM.closeAll();
        return aFunktionen.setzeEnde("aGastkarteQuittung", true, false);
    }

    public String doZurueck() {
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
