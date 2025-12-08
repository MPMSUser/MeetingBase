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

import de.meetingapps.meetingportal.meetComAllg.CaString;
import de.meetingapps.meetingportal.meetComEclM.EclDbM;
import de.meetingapps.meetingportal.meetComEclM.EclUserLoginM;
import de.meetingapps.meetingportal.meetComEntities.EclNachrichtVerwendungsCode;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@RequestScoped
@Named
public class UPflegeNachrichtVerwendungsCode {

    @Inject
    EclDbM eclDbM;
    @Inject
    USession uSession;
    @Inject
    XSessionVerwaltung xSessionVerwaltung;
    @Inject
    EclUserLoginM eclUserLoginM;

    @Inject
    UPflegeNachrichtVerwendungsCodeSession uPflegeNachrichtVerwendungsCodeSession;

    public String doWeiter() {
        if (eclUserLoginM.pruefe_uportal_dLoginHighAdmin() == false) {
            return "";
        }
        if (uPflegeNachrichtVerwendungsCodeSession.getModus() != 0) {
            return "";
        }
        if (!xSessionVerwaltung.pruefeUStart("uPflegeNachrichtVerwendungsCode", "")) {
            xSessionVerwaltung.setzeUEnde();
            return "uDlgFehler";
        }
        String sIdent = uPflegeNachrichtVerwendungsCodeSession.getIdent();
        if (!CaString.isNummern(sIdent)) {
            uSession.setFehlermeldung("Nur Zahlen zulässig!");
            return "";
        }
        int ident = Integer.parseInt(sIdent);
        if (ident < 1 || ident > 999999) {
            uSession.setFehlermeldung("Nur Zahlen zwischen 1 und 999999 zulässig!");
            return "";
        }
        eclDbM.openAll();
        eclDbM.openWeitere();
        eclDbM.getDbBundle().dbNachrichtVerwendungsCode.read(ident);
        int anz = eclDbM.getDbBundle().dbNachrichtVerwendungsCode.anzErgebnis();
        EclNachrichtVerwendungsCode lNachrichtVerwendungsCode = null;
        if (anz > 0) {
            lNachrichtVerwendungsCode = eclDbM.getDbBundle().dbNachrichtVerwendungsCode.ergebnisPosition(0);
        }
        eclDbM.closeAll();
        if (anz > 0) {
            /*Ändern*/
            uPflegeNachrichtVerwendungsCodeSession.setBeschreibung(lNachrichtVerwendungsCode.beschreibung);
            uPflegeNachrichtVerwendungsCodeSession
                    .setIdentNachrichtBasisText(Integer.toString(lNachrichtVerwendungsCode.identNachrichtBasisText));
            if (lNachrichtVerwendungsCode.reserviertFuerSystem == 1) {
                uPflegeNachrichtVerwendungsCodeSession.setReserviertFuerSystem(true);
            } else {
                uPflegeNachrichtVerwendungsCodeSession.setReserviertFuerSystem(false);
            }
            uPflegeNachrichtVerwendungsCodeSession.setModus(1);
        } else {
            /*Neu*/
            uPflegeNachrichtVerwendungsCodeSession.setBeschreibung("");
            uPflegeNachrichtVerwendungsCodeSession.setIdentNachrichtBasisText("");
            uPflegeNachrichtVerwendungsCodeSession.setReserviertFuerSystem(false);
            uPflegeNachrichtVerwendungsCodeSession.setModus(2);
        }
        return xSessionVerwaltung.setzeUEnde("uPflegeNachrichtVerwendungsCode", true, false,
                eclUserLoginM.getKennung());
    }

    public String doSpeichern() {
        if (eclUserLoginM.pruefe_uportal_dLoginHighAdmin() == false) {
            return "";
        }
        String sIdentNachrichtBasisText = uPflegeNachrichtVerwendungsCodeSession.getIdentNachrichtBasisText();
        int identNachrichtBasisText = 0;
        if (!sIdentNachrichtBasisText.isEmpty()) {
            if (!CaString.isNummern(sIdentNachrichtBasisText)) {
                uSession.setFehlermeldung("Nur Zahlen zulässig!");
                return "";
            }
            identNachrichtBasisText = Integer.parseInt(sIdentNachrichtBasisText);
            if (identNachrichtBasisText < 1 || identNachrichtBasisText > 999999) {
                uSession.setFehlermeldung("Nur Zahlen zwischen 1 und 999999 zulässig!");
                return "";
            }
        }
        if (uPflegeNachrichtVerwendungsCodeSession.getBeschreibung().length() > 80) {
            uSession.setFehlermeldung("Beschreibung darf maximal 80 Zeichen lang sein!");
            return "";
        }
        if (uPflegeNachrichtVerwendungsCodeSession.getModus() != 1) {
            return "";
        }
        if (!xSessionVerwaltung.pruefeUStart("uPflegeNachrichtVerwendungsCode", "")) {
            xSessionVerwaltung.setzeUEnde();
            return "uDlgFehler";
        }
        eclDbM.openAll();
        eclDbM.openWeitere();
        EclNachrichtVerwendungsCode lNachrichtVerwendungsCode = new EclNachrichtVerwendungsCode();
        lNachrichtVerwendungsCode.ident = Integer.parseInt(uPflegeNachrichtVerwendungsCodeSession.getIdent());
        lNachrichtVerwendungsCode.beschreibung = uPflegeNachrichtVerwendungsCodeSession.getBeschreibung();
        lNachrichtVerwendungsCode.identNachrichtBasisText = identNachrichtBasisText;
        if (uPflegeNachrichtVerwendungsCodeSession.isReserviertFuerSystem()) {
            lNachrichtVerwendungsCode.reserviertFuerSystem = 1;
        } else {
            lNachrichtVerwendungsCode.reserviertFuerSystem = 0;
        }
        eclDbM.getDbBundle().dbNachrichtVerwendungsCode.update(lNachrichtVerwendungsCode);
        eclDbM.closeAll();
        uPflegeNachrichtVerwendungsCodeSession.clear();
        return xSessionVerwaltung.setzeUEnde("uPflegeNachrichtVerwendungsCode", true, false,
                eclUserLoginM.getKennung());
    }

    public String doNeu() {
        if (eclUserLoginM.pruefe_uportal_dLoginHighAdmin() == false) {
            return "";
        }
        String sIdentNachrichtBasisText = uPflegeNachrichtVerwendungsCodeSession.getIdentNachrichtBasisText();
        int identNachrichtBasisText = 0;
        if (!sIdentNachrichtBasisText.isEmpty()) {
            if (!CaString.isNummern(sIdentNachrichtBasisText)) {
                uSession.setFehlermeldung("Nur Zahlen zulässig!");
                return "";
            }
            identNachrichtBasisText = Integer.parseInt(sIdentNachrichtBasisText);
            if (identNachrichtBasisText < 1 || identNachrichtBasisText > 999999) {
                uSession.setFehlermeldung("Nur Zahlen zwischen 1 und 999999 zulässig!");
                return "";
            }
        }
        if (uPflegeNachrichtVerwendungsCodeSession.getBeschreibung().length() > 80) {
            uSession.setFehlermeldung("Beschreibung darf maximal 80 Zeichen lang sein!");
            return "";
        }
        if (uPflegeNachrichtVerwendungsCodeSession.getModus() != 2) {
            return "";
        }
        if (!xSessionVerwaltung.pruefeUStart("uPflegeNachrichtVerwendungsCode", "")) {
            xSessionVerwaltung.setzeUEnde();
            return "uDlgFehler";
        }
        eclDbM.openAll();
        eclDbM.openWeitere();
        EclNachrichtVerwendungsCode lNachrichtVerwendungsCode = new EclNachrichtVerwendungsCode();
        lNachrichtVerwendungsCode.ident = Integer.parseInt(uPflegeNachrichtVerwendungsCodeSession.getIdent());
        lNachrichtVerwendungsCode.beschreibung = uPflegeNachrichtVerwendungsCodeSession.getBeschreibung();
        lNachrichtVerwendungsCode.identNachrichtBasisText = identNachrichtBasisText;
        if (uPflegeNachrichtVerwendungsCodeSession.isReserviertFuerSystem()) {
            lNachrichtVerwendungsCode.reserviertFuerSystem = 1;
        } else {
            lNachrichtVerwendungsCode.reserviertFuerSystem = 0;
        }
        eclDbM.getDbBundle().dbNachrichtVerwendungsCode.insert(lNachrichtVerwendungsCode);
        eclDbM.closeAll();
        uPflegeNachrichtVerwendungsCodeSession.clear();
        return xSessionVerwaltung.setzeUEnde("uPflegeNachrichtVerwendungsCode", true, false,
                eclUserLoginM.getKennung());
    }

    public String doAbbrechen() {
        if (eclUserLoginM.pruefe_uportal_dLoginHighAdmin() == false) {
            return "";
        }
        if (uPflegeNachrichtVerwendungsCodeSession.getModus() != 1
                && uPflegeNachrichtVerwendungsCodeSession.getModus() != 2) {
            return "";
        }
        if (!xSessionVerwaltung.pruefeUStart("uPflegeNachrichtVerwendungsCode", "")) {
            xSessionVerwaltung.setzeUEnde();
            return "uDlgFehler";
        }
        uPflegeNachrichtVerwendungsCodeSession.clear();
        return xSessionVerwaltung.setzeUEnde("uPflegeNachrichtVerwendungsCode", true, false,
                eclUserLoginM.getKennung());
    }

}
