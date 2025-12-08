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

import de.meetingapps.meetingportal.meetComAllg.CaBug;
import de.meetingapps.meetingportal.meetComAllg.CaFehler;
import de.meetingapps.meetingportal.meetComBl.BlNummernformen;
import de.meetingapps.meetingportal.meetComBl.BlTeilnehmerLogin;
import de.meetingapps.meetingportal.meetComEclM.EclDbM;
import de.meetingapps.meetingportal.meetComEclM.EclParamM;
import de.meetingapps.meetingportal.meetComEclM.EclPortalTexteM;
import de.meetingapps.meetingportal.meetComEclM.EclTeilnehmerLoginM;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

/*TODO # Reengineering ku1010: vor/nach der HV "ist nicht mehr möglich" "ist noch nicht möglich" einbauen - aAnmelden, aStatus*/
/*TODO # Reengineering ku164: "Sie sind mit Stand im Portal eingetragen"*/

/**Verwendung der "Text-Nummern" in Portal und App
 *(Gesamt-Ident Portal - AppSeite/AppIdentAufSeite)
 *
 * Portal:
 * ALogin wird angezeigt auf der Login-Seite, zusammen mit Eingabefeldern für Aktionärsnummer und Passwort
 * 
 * App:
 * AppBesitzHinzufuegen
 * Variante 1: "manueller Login": Wird nach Auswahl des Mandanten angezeigt, zusammen mit Eingabefeldern für Aktionärsnummer und Passwort (also analog Portal)
 * 
 * Variante 2: "QR-Code Login": nach dem Scannen wird eine Seite mit den Login-Daten aus Scan zum Bestätigen angezeigt - mit den entsprechenden Texten des Mandanten
 * 
 * Nur App:
 * (224-15/999) Fenstertitel
 * 
 * (1- / ) "Große Überschrift" zB. Willkommen im Aktionärsportal (nur Portal)
 * 
 * (3-15/3) Überschrift - z.B. Internetservice für Aktionäre
 *  
 * (4-15/8) Erster Text-Block  Beschreibung, was im Internetservice alles geht (App und Portal)
 * 
 * Nur Portal, Falls App vorhanden:
 * (199- / ) Textblock, dass auch App möglich ist
 * (198- / ) Button - Verweis auf IOS
 * (197- / ) Button - Verweis auf Android
 * 
 * (2-15/9) - Beschreibung, wie Login durchzuführen ist. in Portal auf ALogin, in App nur bei manuellem Login Variante 1
 * 
 * (5-15/4) Bezeichnung Eingabefeld Aktionärsnummer
 * 
 * (6-15/5) Bezeichnung Eingabefeld Passwort
 * 
 * (9-15/10) Text für Passwort vergessen-Verfahren. In Portal auf ALogin, in App nur bei manuellem Login Variante 1
 * (10-15/7) Button / Link-Bezeichung für Start Passwortvergessen-Verfahren. Wie (9)
 * 
 * (7-15/11) Textblock der immer mit angezeigt wird, z.B. für "hier werden Sie geholfen"
 * 
 * (8-15/6) Button Login
 * 
 *
 *
 *
 */

/*TODO _APP: Passeort-Vergessen-Prozedur. - Auch für Portal*/

@RequestScoped
@Named
@Deprecated
public class AControllerLogin {

    /*Seitenspezifische Fehlermeldung. Seitenspezifisch deshalb, um zu verhindern, dass beim "Neueinsprung" in die Login-Maske 
     * (z.B. über direkte Eingabe des Browserlinks) "alte Fehlermeldungen" übernommen werden.*/

    @Inject
    AControllerLoginSession aControllerLoginSession;
    @Inject
    EclDbM eclDbM;
    @Inject
    ADlgVariablen aDlgVariablen;
    @Inject
    AFunktionen aFunktionen;
    @Inject
    EclPortalTexteM eclTextePortalM;
    //	@Inject EclZugeordneteMeldungListeM eclZugeordneteMeldungListeM;
    @Inject
    private EclTeilnehmerLoginM eclTeilnehmerLoginM;
    //	@Inject EclAnredeListeM lAnredeListeM;
    //	@Inject EclGastM lGastM;
    //	@Inject EclMeldungenMeldungenListeM lMeldungenMeldungenListeM;

    @Inject
    AControllerRegistrierung aControllerRegistrierung;
    @Inject
    AControllerPasswortVergessen aControllerPasswortVergessen;

    @Inject
    EclParamM eclParamM;

    //	@Inject XControllerAllgemein xControllerAllgemein;
    @Inject
    XSessionVerwaltung xSessionVerwaltung;

    @Inject
    ALanguage aLanguage;

    public void clearFehlermeldung() {
        aControllerLoginSession.clearFehlermeldung();
    }

    //	09.01.2018: auskommentiert - offensichtlich nicht gebraucht?
    //	private String pwVergessenCode="";

    /*****************************************Ansprung aus JSF heraus*******************************/
    public String doLogin() {

        /**Zurücksetzen insbesondere von auswahlMaske*/
        aDlgVariablen.clearNachLogin();

        int rc = 0;

        if (eclParamM.getClGlobalVar().mandant == 0) {
            CaBug.drucke("AControllerLogin.doLogin 001: Mandant ist 0");
            aFunktionen.setzeEnde();
            return "aDlgFehlerAllgemein";
        }

        if (eclParamM.getParam().paramPortal.timeoutAufLang == 1) {
            xSessionVerwaltung.setTimeoutLang();
        } else {
            xSessionVerwaltung.setTimeoutNormal();
        }

        if (!aFunktionen.pruefeStart("aLogin")) {
            aFunktionen.setzeEnde();
            return "aDlgFehler";
        }

        /*Eingabefelder und Fehlermeldung von Login-Maske "Resetten"*/
        this.clearFehlermeldung();

        if (eclParamM.getParam().paramPortal.loginGesperrt == 1) {
            if (aLanguage.getLang() == 1) {
                this.setFehlerMeldung(eclParamM.getParam().paramPortal.loginGesperrtTextDeutsch);
            } else {
                this.setFehlerMeldung(eclParamM.getParam().paramPortal.loginGesperrtTextEnglisch);
            }
            aFunktionen.setzeEnde();
            return "";
        }

        eclDbM.openAll();

        BlNummernformen blNummernformen = new BlNummernformen(eclDbM.getDbBundle());
        aControllerLoginSession.setLoginKennung(
                blNummernformen.aktienregisterNraufbereitenFuerIntern(aControllerLoginSession.getLoginKennung()));

        aDlgVariablen.setLoginKennung(aControllerLoginSession.getLoginKennung());
        aDlgVariablen.setLoginPasswort(aControllerLoginSession.getLoginPasswort());
        aControllerLoginSession.setLoginKennung("");
        aControllerLoginSession.setLoginPasswort("");

        if (eclParamM.getParam().paramPortal.loginIPTrackingAktiv == 1) {
            xSessionVerwaltung.liefereFacesContext();
            xSessionVerwaltung.liefereClientIpAddr(aDlgVariablen.getLoginKennung(), eclDbM.getDbBundle());
        }

        /*Prüfen ob Login mit diesen Daten überhaupt zulässig*/
        rc = login(true);
        if (rc <= 0) {
            /*Fehler aufgetreten*/
            eclDbM.closeAllAbbruch();

            switch (rc) {
            case CaFehler.afKennungUnbekannt:
                this.setFehlerNr(CaFehler.afKennungUnbekannt);
                this.setFehlerMeldung(eclTextePortalM.getFehlertext(CaFehler.afKennungUnbekannt));
                aFunktionen.setzeEnde();
                return "aLogin";
            case CaFehler.afPasswortFalsch:
                this.setFehlerNr(CaFehler.afPasswortFalsch);
                this.setFehlerMeldung(eclTextePortalM.getFehlertext(CaFehler.afPasswortFalsch));
                aFunktionen.setzeEnde();
                return "aLogin";
            }
        }

        if (eclTeilnehmerLoginM.isAnmeldenUnzulaessig()) {
            this.setFehlerNr(CaFehler.afBerechtigungFuerAktionaersportalFehlt);
            this.setFehlerMeldung(eclTextePortalM.getFehlertext(CaFehler.afBerechtigungFuerAktionaersportalFehlt));
            aFunktionen.setzeEnde();
            return "aLogin";
        }

        /*Hier ist Login-Überprüfung grundsätzlich abgeschlossen - d.h. es ist ein Berechtigter
         * angemeldet.
         * Im folgenden nun Checken, welche Auswahlmaske aufgerufen werden soll
         */
        aDlgVariablen.clearDlg();
        if (aControllerRegistrierung.pruefeErstregistrierung() == true) {
            aControllerRegistrierung.initPublikationListe();
            aControllerRegistrierung.startRegistrierung();
            eclDbM.closeAll();
            aDlgVariablen.setErstRegistrierungOderEinstellungenAktiv(1);
            return aFunktionen.setzeEnde("aRegistrierung", true, false);
        }

        if (eclTeilnehmerLoginM.getAnmeldeKennungArt() == 1 /*Anmeldung mit Aktienregister-Kennung*/
                || eclTeilnehmerLoginM.getAnmeldeKennungArt() == 3 /*Anmeldung über Personen NatJur*/
        ) {
            String naechsteMaske = aFunktionen.waehleAusgangsmaske(eclDbM.getDbBundle());
            eclDbM.closeAll();

            return aFunktionen.setzeEnde(naechsteMaske, true, true);
        }

        eclDbM.closeAll();
        if (eclParamM.getParam().paramPortal.varianteDialogablauf == 0) {
            return aFunktionen.setzeEnde("aAnmelden", true, true);
        } else {
            return aFunktionen.setzeEnde("aAuswahl1", true, true);
        }
    }

    /*******************Verarbeiten Login-Logik*****************************************************
     * Eingabeparameter:
     * 		ADlgVariablen.loginKennung = Kennung, mit der das Einloggen versucht wird
     * 		ADlgVariablen.loginPasswort = Passwort, das beim Einloggen verwendet wird
     * 		
     * 
     * Gesetzt nach Abarbeitung:
     * 		DlgVariablen.fehlerNr	<0 => Fehler aufgetreten, entsprechende Fehlernummer gespeichert (und als rc
     * 					übergeben)
     * 		EclTeilnehmerLoginM		komplett gefüllt
     * 
     * 		passwortVerschluesselt
     * @return
     */
    private String passwortVerschluesselt = "";

    public int login(boolean passwortPruefen) {

        boolean erg;

        /*aktuellerBenutzer=lCbenutzer;*/

        BlTeilnehmerLogin blTeilnehmerLogin = new BlTeilnehmerLogin();
        blTeilnehmerLogin.initDB(eclDbM.getDbBundle());
        erg = blTeilnehmerLogin.findeKennung(aDlgVariablen.getLoginKennung());
        if (erg == false) {
            aDlgVariablen.setFehlerNr(CaFehler.afKennungUnbekannt);
            return CaFehler.afKennungUnbekannt;
        }

        blTeilnehmerLogin.anmeldeKennung = aDlgVariablen.getLoginKennung();

        if (passwortPruefen == true) {
            erg = blTeilnehmerLogin.pruefePasswort(aDlgVariablen.getLoginPasswort());
            passwortVerschluesselt = blTeilnehmerLogin.anmeldePasswortVerschluesselt;
            if (erg == false) {
                aDlgVariablen.setFehlerNr(CaFehler.afPasswortFalsch);
                return CaFehler.afPasswortFalsch;
            }
        }

        //		System.out.println("Letzter Aktienregisterinport="+eclDbM.getDbBundle().param.parameterNS.pLetzterAktienregisterImport);

        aDlgVariablen.clearFehlerMeldung();
        eclTeilnehmerLoginM.copyFrom(blTeilnehmerLogin);

        aDlgVariablen.setLoginArt(1);
        if (eclTeilnehmerLoginM.getAnmeldeKennungArt() != 1) {
            aDlgVariablen.setLoginArt(3);
        }
        return 1;
    }

    public String doPasswortVergessen() {
        if (!aFunktionen.pruefeStart("aLogin")) {
            return "aDlgFehler";
        }
        aControllerPasswortVergessen.passwortZuruecksetzenVorbereiten();
        return aFunktionen.setzeEnde("aPasswortVergessen", true, true);
    }

    public String doLogout() {
        return aFunktionen.waehleLogout();
    }

    public boolean pruefeFehlerMeldungVorhanden() {
        if (aControllerLoginSession.getFehlerMeldung().isEmpty()) {
            return false;
        }
        return true;
    }

    /*******************Ab hier: Standard Getter/Setter********************************************/

    public String getLoginKennung() {
        return aControllerLoginSession.getLoginKennung();
    }

    public void setLoginKennung(String loginKennung) {
        aControllerLoginSession.setLoginKennung(loginKennung);
    }

    public String getLoginPasswort() {
        return aControllerLoginSession.getLoginPasswort();
    }

    public void setLoginPasswort(String loginPasswort) {
        aControllerLoginSession.setLoginPasswort(loginPasswort);
    }

    //	09.01.2018: auskommentiert - offensichtlich nicht gebraucht?
    //	public String getPwVergessenCode() {
    //		return pwVergessenCode;
    //	}
    //
    //
    //	public void setPwVergessenCode(String pwVergessenCode) {
    //		this.pwVergessenCode = pwVergessenCode;
    //	}

    public String getFehlerMeldung() {
        return aControllerLoginSession.getFehlerMeldung();
    }

    public void setFehlerMeldung(String fehlerMeldung) {
        aControllerLoginSession.setFehlerMeldung(fehlerMeldung);
    }

    public int getFehlerNr() {
        return aControllerLoginSession.getFehlerNr();
    }

    public void setFehlerNr(int fehlerNr) {
        aControllerLoginSession.setFehlerNr(fehlerNr);
    }

    public String getPasswortVerschluesselt() {
        return passwortVerschluesselt;
    }

    public void setPasswortVerschluesselt(String passwortVerschluesselt) {
        this.passwortVerschluesselt = passwortVerschluesselt;
    }

}
