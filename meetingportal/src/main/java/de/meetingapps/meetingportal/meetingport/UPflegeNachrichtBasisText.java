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
import de.meetingapps.meetingportal.meetComEntities.EclNachrichtBasisText;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@RequestScoped
@Named
public class UPflegeNachrichtBasisText {

    @Inject
    EclDbM eclDbM;
    @Inject
    USession uSession;
    @Inject
    XSessionVerwaltung xSessionVerwaltung;
    @Inject
    EclUserLoginM eclUserLoginM;

    @Inject
    UPflegeNachrichtBasisTextSession uPflegeNachrichtBasisTextSession;

    public String doWeiter() {
        if (eclUserLoginM.pruefe_uportal_dLoginHighAdmin() == false) {
            return "";
        }
        if (uPflegeNachrichtBasisTextSession.getModus() != 0) {
            return "";
        }
        if (!xSessionVerwaltung.pruefeUStart("uPflegeNachrichtBasisText", "")) {
            xSessionVerwaltung.setzeUEnde();
            return "uDlgFehler";
        }
        String sIdent = uPflegeNachrichtBasisTextSession.getIdent();
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
        eclDbM.getDbBundle().dbNachrichtBasisText.read(ident);
        int anz = eclDbM.getDbBundle().dbNachrichtBasisText.anzErgebnis();
        EclNachrichtBasisText lNachrichtBasisText = null;
        if (anz > 0) {
            lNachrichtBasisText = eclDbM.getDbBundle().dbNachrichtBasisText.ergebnisPosition(0);
        }
        eclDbM.closeAll();
        if (anz > 0) {
            /*Ändern*/
            uPflegeNachrichtBasisTextSession.setBeschreibung(lNachrichtBasisText.beschreibung);
            uPflegeNachrichtBasisTextSession.setBetreff(lNachrichtBasisText.betreff);
            uPflegeNachrichtBasisTextSession.setMailText(lNachrichtBasisText.mailText);
            uPflegeNachrichtBasisTextSession.setModus(1);
        } else {
            /*Neu*/
            uPflegeNachrichtBasisTextSession.setBeschreibung("");
            uPflegeNachrichtBasisTextSession.setBetreff("");
            uPflegeNachrichtBasisTextSession.setMailText("");
            uPflegeNachrichtBasisTextSession.setModus(2);
        }
        return xSessionVerwaltung.setzeUEnde("uPflegeNachrichtBasisText", true, false, eclUserLoginM.getKennung());
    }

    public String doSpeichern() {
        if (eclUserLoginM.pruefe_uportal_dLoginHighAdmin() == false) {
            return "";
        }
        if (uPflegeNachrichtBasisTextSession.getModus() != 1) {
            return "";
        }
        if (speichernPruefen(2) == false) {
            return "";
        }
        if (!xSessionVerwaltung.pruefeUStart("uPflegeNachrichtBasisText", "")) {
            xSessionVerwaltung.setzeUEnde();
            return "uDlgFehler";
        }
        speichern(2);
        return xSessionVerwaltung.setzeUEnde("uPflegeNachrichtBasisText", true, false, eclUserLoginM.getKennung());
    }

    public String doNeu() {
        if (eclUserLoginM.pruefe_uportal_dLoginHighAdmin() == false) {
            return "";
        }
        if (uPflegeNachrichtBasisTextSession.getModus() != 2) {
            return "";
        }
        if (speichernPruefen(1) == false) {
            return "";
        }
        if (!xSessionVerwaltung.pruefeUStart("uPflegeNachrichtBasisText", "")) {
            xSessionVerwaltung.setzeUEnde();
            return "uDlgFehler";
        }
        speichern(1);
        return xSessionVerwaltung.setzeUEnde("uPflegeNachrichtBasisText", true, false, eclUserLoginM.getKennung());
    }

    private boolean speichernPruefen(int neuOderAendern) {
        if (uPflegeNachrichtBasisTextSession.getBeschreibung().length() > 80) {
            uSession.setFehlermeldung("Beschreibung darf maximal 80 Zeichen lang sein!");
            return false;
        }
        if (uPflegeNachrichtBasisTextSession.getBetreff().length() > 80) {
            uSession.setFehlermeldung("Betreff darf maximal 80 Zeichen lang sein!");
            return false;
        }
        if (uPflegeNachrichtBasisTextSession.getMailText().length() > 2000) {
            uSession.setFehlermeldung("Beschreibung darf maximal 2000 Zeichen lang sein!");
            return false;
        }

        return true;
    }

    private void speichern(int neuOderAendern) {
        eclDbM.openAll();
        eclDbM.openWeitere();
        EclNachrichtBasisText lNachrichtBasisText = new EclNachrichtBasisText();
        lNachrichtBasisText.ident = Integer.parseInt(uPflegeNachrichtBasisTextSession.getIdent());
        lNachrichtBasisText.beschreibung = uPflegeNachrichtBasisTextSession.getBeschreibung();
        lNachrichtBasisText.betreff = uPflegeNachrichtBasisTextSession.getBetreff();
        lNachrichtBasisText.mailText = uPflegeNachrichtBasisTextSession.getMailText();
        if (neuOderAendern == 2) {
            eclDbM.getDbBundle().dbNachrichtBasisText.update(lNachrichtBasisText);
        } else {
            eclDbM.getDbBundle().dbNachrichtBasisText.insert(lNachrichtBasisText);
        }
        eclDbM.closeAll();
        uPflegeNachrichtBasisTextSession.clear();
    }

    public String doAbbrechen() {
        if (eclUserLoginM.pruefe_uportal_dLoginHighAdmin() == false) {
            return "";
        }
        if (uPflegeNachrichtBasisTextSession.getModus() != 1 && uPflegeNachrichtBasisTextSession.getModus() != 2) {
            return "";
        }
        if (!xSessionVerwaltung.pruefeUStart("uPflegeNachrichtBasisText", "")) {
            xSessionVerwaltung.setzeUEnde();
            return "uDlgFehler";
        }
        uPflegeNachrichtBasisTextSession.clear();
        return xSessionVerwaltung.setzeUEnde("uPflegeNachrichtBasisText", true, false, eclUserLoginM.getKennung());
    }

}
