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

import de.meetingapps.meetingportal.meetComEclM.EclDbM;
import de.meetingapps.meetingportal.meetComEclM.EclZugeordneteMeldungListeM;
import de.meetingapps.meetingportal.meetComEclM.EclZugeordneteMeldungM;
import de.meetingapps.meetingportal.meetComEntities.EclMeldung;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

/**Verwendung der "Text-Nummern" in Portal und App
 *(Gesamt-Ident Portal - AppSeite/AppIdentAufSeite)
 *
 * Nur App:
 * (605-?15/?999) Fenstertitel
 *
 * (595-/) Überschrift
 * (596/) Text am Anfang der Seite
 *
 * (597-/) Text wenn bereits angemeldet
 * (598-/) Text wenn bereits abgemeldet
 * (606-/) Text wenn weder an- noch abgemeldet
 * (599-/) Text vor Auswahl Checkboxes
 * (600-/) Checkbox Anmelden
 * (601-/) Checkbox Abmelden
 * (602-/) Text nach Auswahl Checkboxes
 * (603-/) Button Weiter
 *
 *
 */

@RequestScoped
@Named
@Deprecated
public class AControllerAnmeldenAbmelden {

    @Inject
    ADlgVariablen aDlgVariablen;
    @Inject
    AFunktionen aFunktionen;
    @Inject
    private EclDbM eclDbM;

    @Inject
    private EclZugeordneteMeldungListeM eclZugeordneteMeldungListeM;

    @Inject
    private AControllerDialogVariante1Session aControllerDialogVariante1Session;

    public String doWeiter() {
        if (!aFunktionen.pruefeStart("aAnmeldenAbmelden")) {
            return "aDlgFehler";
        }

        EclZugeordneteMeldungM lZugeordneteMeldungM = eclZugeordneteMeldungListeM
                .getZugeordneteMeldungenEigeneAktienListeM().get(0);
        String lZusatzfeld3 = lZugeordneteMeldungM.getZusatzfeld3();
        String lAnAbMelden = aControllerDialogVariante1Session.getAnabmeldenRaw();
        if (lAnAbMelden == null || lAnAbMelden.length() < 1) {
            lAnAbMelden = "0";
        }
        if (lZusatzfeld3.length() < 2) {
            lZusatzfeld3 = lAnAbMelden;
        } else {
            lZusatzfeld3 = lAnAbMelden + lZusatzfeld3.substring(1);
        }
        lZugeordneteMeldungM.setZusatzfeld3(lZusatzfeld3);

        int lMeldeIdent = lZugeordneteMeldungM.getMeldungsIdent();
        eclDbM.openAll();
        eclDbM.getDbBundle().dbMeldungen.leseZuIdent(lMeldeIdent);
        EclMeldung lMeldung = eclDbM.getDbBundle().dbMeldungen.meldungenArray[0];
        lMeldung.zusatzfeld3 = lZusatzfeld3;
        eclDbM.getDbBundle().dbMeldungen.update(lMeldung);
        eclDbM.closeAll();

        return aFunktionen.setzeEnde("aAuswahl1", true, true);

    }

    /**Abmelden*/
    public String doAbmelden() {
        if (!aFunktionen.pruefeStart("aAnmeldenAbmelden")) {
            return "aDlgFehler";
        }
        aDlgVariablen.clearLogin();
        return aFunktionen.waehleLogout();
    }

}
