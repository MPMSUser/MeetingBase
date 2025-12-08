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

import de.meetingapps.meetingportal.meetComAllg.CaPasswortVerschluesseln;
import de.meetingapps.meetingportal.meetComDb.DbUserLogin;
import de.meetingapps.meetingportal.meetComEclM.EclDbM;
import de.meetingapps.meetingportal.meetComEclM.EclUserLoginM;
import de.meetingapps.meetingportal.meetComEntities.EclUserLogin;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@RequestScoped
@Named
public class UPasswortAendern {

    @Inject
    private USession uSession;
    @Inject
    private UPasswortAendernSession uPasswortAendernSession;

    @Inject
    private EclDbM eclDbM;
    @Inject
    private EclUserLoginM eclUserLoginM;

    @Inject
    private XSessionVerwaltung xSessionVerwaltung;

    public void init(String pAufrufSeite) {
        uPasswortAendernSession.clear();
        uPasswortAendernSession.setAufrufSeite(pAufrufSeite);
    }

    public String doPasswortAendern() {

        if (!xSessionVerwaltung.pruefeStart("uPasswortAendern", "")) {
            xSessionVerwaltung.setzeEnde();
            return "uDlgFehler";
        }

        if (uPasswortAendernSession.getAltesPasswort().isEmpty()) {
            uSession.setFehlermeldung("Bitte altes Passwort eingeben");
            xSessionVerwaltung.setzeEnde();
            return "";
        }
        if (uPasswortAendernSession.getNeuesPasswort().isEmpty()) {
            uSession.setFehlermeldung("Bitte neues Passwort eingeben");
            xSessionVerwaltung.setzeEnde();
            return "";
        }
        if (uPasswortAendernSession.getNeuesPasswortBestaetigen().isEmpty()) {
            uSession.setFehlermeldung("Bitte neues Passwort bestätigen");
            xSessionVerwaltung.setzeEnde();
            return "";
        }
        if (uPasswortAendernSession.getNeuesPasswortBestaetigen()
                .compareTo(uPasswortAendernSession.getNeuesPasswort()) != 0) {
            uSession.setFehlermeldung("Neues Passwort und neues Passwort bestätigen stimmt nicht überein");
            xSessionVerwaltung.setzeEnde();
            return "";
        }

        String hNeuesPasswort=uPasswortAendernSession.getNeuesPasswort();
        if (hNeuesPasswort.length()<8) {
            uSession.setFehlermeldung("Neues Passwort zu kurz - mindestens 8 Stellen!");
            xSessionVerwaltung.setzeEnde();
            return "";
         }
        int rc=CaPasswortVerschluesseln.pruefePasswortZulaessig(hNeuesPasswort);
        if (rc<0) {
            uSession.setFehlermeldung("Neues Passwort zu unsicher - mindestens 1 Großbuchstabe, 1 Kleinbuchstabe, 1 Ziffer!");
            xSessionVerwaltung.setzeEnde();
            return "";
        }
        
        eclDbM.openAll();
        DbUserLogin dbUserLogin = eclDbM.getDbBundle().dbUserLogin;
        boolean mitMandant = true;
        if (eclUserLoginM.getMandant() == 0) {
            mitMandant = false;
        }
        dbUserLogin.leseZuKennung(eclUserLoginM.getKennung(), mitMandant);
        if (dbUserLogin.anzUserLoginGefunden() == 0) {
            eclDbM.closeAll();
            uSession.setFehlermeldung("Interner Fehler: Kennung nicht gefunden");
            xSessionVerwaltung.setzeEnde();
            return "";
        }

        EclUserLogin lUserLogin = dbUserLogin.userLoginArray[0];
        if (lUserLogin.passwort
                .compareTo(CaPasswortVerschluesseln.verschluesseln(uPasswortAendernSession.getAltesPasswort())) != 0) {
            eclDbM.closeAll();
            uSession.setFehlermeldung("Altes Passwort falsch");
            xSessionVerwaltung.setzeEnde();
            return "";

        }

        lUserLogin.passwort = CaPasswortVerschluesseln.verschluesseln(uPasswortAendernSession.getNeuesPasswort());
        dbUserLogin.update(lUserLogin);

        eclDbM.closeAll();
        uPasswortAendernSession.clear();
        return xSessionVerwaltung.setzeUEnde(uPasswortAendernSession.getAufrufSeite(), true, false,
                eclUserLoginM.getKennung());
    }

}
