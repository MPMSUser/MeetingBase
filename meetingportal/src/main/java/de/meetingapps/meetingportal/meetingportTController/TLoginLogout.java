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
package de.meetingapps.meetingportal.meetingportTController;

import de.meetingapps.meetingportal.meetComAllg.CaBug;
import de.meetingapps.meetingportal.meetComAllg.CaDatumZeit;
import de.meetingapps.meetingportal.meetComAllg.CaFehler;
import de.meetingapps.meetingportal.meetComAllg.CaTokenUtil;
import de.meetingapps.meetingportal.meetComBl.BlEinsprungLinkPortal;
import de.meetingapps.meetingportal.meetComBl.BlNummernformBasis;
import de.meetingapps.meetingportal.meetComBl.BlTeilnehmerLoginNeu;
import de.meetingapps.meetingportal.meetComBrM.BrMAuftraege;
import de.meetingapps.meetingportal.meetComBrM.BrMInit;
import de.meetingapps.meetingportal.meetComBrM.BrMLogin;
import de.meetingapps.meetingportal.meetComEclM.EclBesitzGesamtM;
import de.meetingapps.meetingportal.meetComEclM.EclDbM;
import de.meetingapps.meetingportal.meetComEclM.EclLoginDatenM;
import de.meetingapps.meetingportal.meetComEclM.EclParamM;
import de.meetingapps.meetingportal.meetComEclM.EclPortalTexteM;
import de.meetingapps.meetingportal.meetComEntities.EclAktienregister;
import de.meetingapps.meetingportal.meetComKonst.KonstGruppen;
import de.meetingapps.meetingportal.meetComKonst.KonstLoginKennungArt;
import de.meetingapps.meetingportal.meetComKonst.KonstPortalFunktionen;
import de.meetingapps.meetingportal.meetComKonst.KonstPortalView;
import de.meetingapps.meetingportal.meetingportTFunktionen.TFunktionen;
import de.meetingapps.meetingportal.meetingportTFunktionen.TJsfSession;
import de.meetingapps.meetingportal.meetingportTFunktionen.TPortalFunktionen;
import de.meetingapps.meetingportal.meetingportTFunktionen.TPruefeStartNachOpen;
import de.meetingapps.meetingportal.meetingportTFunktionen.TRemoteAR;
import de.meetingapps.meetingportal.meetingportTFunktionen.TSessionVerwaltung;
import de.meetingapps.meetingportal.meetingportTFunktionen.TUserSessionManagement;
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

@RequestScoped
@Named
public class TLoginLogout {

    /** Our Logger. */
    //    private static final Logger LOGGER = LogManager.getFormatterLogger(TLoginLogout.class.getName());

    private int logDrucken = 3;

    /*Seitenspezifische Fehlermeldung. Seitenspezifisch deshalb, um zu verhindern, dass beim "Neueinsprung" in die Login-Maske
     * (z.B. über direkte Eingabe des Browserlinks) "alte Fehlermeldungen" übernommen werden.*/

    //    private @Inject AControllerLoginSession aControllerLoginSession;
    private @Inject EclDbM eclDbM;

    private @Inject EclParamM eclParamM;

    private @Inject TSessionVerwaltung tSessionVerwaltung;
    private @Inject TSession tSession;
    private @Inject TPermanentSession tPermanentSession;
    private @Inject TJsfSession tJsfSession;
    private @Inject TLanguage tLanguage;
    private @Inject EclLoginDatenM eclLoginDatenM;
    private @Inject EclBesitzGesamtM eclBesitzGesamtM;
    private @Inject TFunktionen tFunktionen;
    private @Inject TPortalFunktionen tPortalFunktionen;
    private @Inject TLoginLogoutSession tLoginLogoutSession;
    private @Inject TEinstellungen tEinstellungen;
    private @Inject TAuswahl tAuswahl;
    private @Inject TPasswortVergessen tPasswortVergessen;
    private @Inject TPraesenzZugangAbgang tPraesenzZugangAbgang;
    private @Inject TMitteilungSession tMitteilungSession;
    private @Inject EclPortalTexteM eclPortalTexteM;
    private @Inject TUserSessionManagement tUserSessionManagement;
    private @Inject TPruefeStartNachOpen tPruefeStartNachOpen;
    private @Inject TMenue tMenue;
    private @Inject PAktionaersdatenSession pAktionaersdatenSession;
    private @Inject TRemoteAR tRemoteAR;
    private @Inject TAuswahlSession tAuswahlSession;

    private @Inject BrMInit brMInit;
    private @Inject BrMLogin brMLogin;
    private @Inject BrMAuftraege brMAuftraege;


    public void doSchliessen() {
        System.out.println("Do Schliessen");
    }

    /*****************************************Ansprung aus JSF heraus*******************************/
    /**Für Button aus Login-Seite*/
    public void doLogin() {
        if (!tSessionVerwaltung.pruefeStartOhneUserLoginPruefung(liefereSeiteLogin())) {
            tSessionVerwaltung.setzeEnde();
            return;
        }
        int verzoegerung=pruefeObZeitverzoegerungFuerLoginVersuch();
        if (verzoegerung>0) {
            tSessionVerwaltung.setzeEnde();
            tLoginLogoutSession.trageFehlerEinMitArt(CaFehler.afLoginVerzoegertAnfang, Integer.toString(verzoegerung), CaFehler.afLoginVerzoegertEnde, 1);
            return;
        }
        doLoginAusfuehren();

    }

    /**Aufruf auch aus Register-Portal*/
    public void doLoginAusfuehren() {

        try {

            eclLoginDatenM.clear();
            eclBesitzGesamtM.clearBeiLogin();
            tMitteilungSession.initNachLogin();
            CaBug.druckeLog("isPeramentPortal=" + tSession.isPermanentPortal(), logDrucken, logDrucken);

            eclPortalTexteM.setNeueQuelle(true);

            CaBug.druckeLog("A", logDrucken, 10);
            /*+++++++++++++++Zurücksetzen++++++++++++*/
            tSession.clearNachLogin();
            tLoginLogoutSession.clearFehler();

            CaBug.druckeLog("B", logDrucken, 10);

            /**Eingegebene Kennung*/
            String lKennung = tLoginLogoutSession.getLoginKennung();
            String orString="ToDo";
            if (lKennung.length()>orString.length()) {
                if (lKennung.startsWith(orString)) {
                    tSession.setOrPortalsperre(true);
                    lKennung=lKennung.substring(orString.length());
                }
            }
            CaBug.druckeLog("lKennung=" + lKennung, logDrucken, 10);

            
            eclDbM.openAll();
            if (!tPruefeStartNachOpen.pruefeStartNachOpenOhneUser()) {
                tSessionVerwaltung.setzeEnde();
                eclDbM.closeAll();
                return;
            }
            CaBug.druckeLog("C", logDrucken, 10);

            tSessionVerwaltung.setzeTimeOut();
            CaBug.druckeLog("F", logDrucken, 10);

            /*+++++Verarbeitung+++++++++*/


            /**Eingegebenes Passwort*/

            String lPasswort = tLoginLogoutSession.getLoginPasswort();
            tLoginLogoutSession.setLoginPasswort("");

            if (eclParamM.getParam().paramPortal.loginIPTrackingAktiv == 1) {
                tFunktionen.speichereIpTracking(lKennung, eclDbM.getDbBundle());
            }
            CaBug.druckeLog("G", logDrucken, 10);

            /*Bei Permanent-Portal: Aktuelle Daten holen, bzw. überprüfen gegen das Aktienregister*/
            String eMailInRemoteRegister = "";
            /*Es wurde bereits mal eine E-Mail-Änderung an GeDix übertragen*/
            boolean eMailNachRemoteRegisterUebertragen = false;

            if (tSession.isPermanentPortal()) {
                CaBug.druckeLog("G1", logDrucken, 10);
                brMInit.init();
                CaBug.druckeLog("G2", logDrucken, 10);

                int rc = brMLogin.pruefeNachLogin(lKennung);
                if (tRemoteAR.pruefeVerfuegbar(rc) == false) {
                    tSessionVerwaltung.setzeEnde();
                    eclDbM.closeAllAbbruch();
                    return;
                }
                if (brMLogin.getRcAktionaerInRegisterVorhanden() == -1) {
                    tSessionVerwaltung.setzeEnde();
                    eclDbM.closeAllAbbruch();
                    tLoginLogoutSession.trageFehlerEin(CaFehler.perAktionaersnummerNichtInAktienregisterEnthalten);
                    tSession.setViewnummer(liefereSeiteLogin());
                    nichtErfolgreicherLogin();
                    return;
                }
                CaBug.druckeLog("G3", logDrucken, 10);

                if (brMLogin.getRcAktionaerBereitsInMeetingVorhanden() == -1) {
                    tSessionVerwaltung.setzeEnde();
                    eclDbM.closeAllAbbruch();
                    tLoginLogoutSession.trageFehlerEin(CaFehler.perAktionaersnummerNichtInPortalRegistriert);
                    tSession.setViewnummer(liefereSeiteLogin());
                    return;
                }
                CaBug.druckeLog("G4", logDrucken, 10);

                /*Für weitere Aktionen ist login-Kennung erforderlich,
                 * deshalb nach blTeilnehmerLogin
                 */
            }

            CaBug.druckeLog("G_A", logDrucken, 10);

            /*Bl-Logik aufrufen*/
            BlTeilnehmerLoginNeu blTeilnehmerLogin = new BlTeilnehmerLoginNeu(tSession.isPermanentPortal());
            blTeilnehmerLogin.initDB(eclDbM.getDbBundle());

            CaBug.druckeLog("G_B", logDrucken, 10);

            int erg = 0;
            if (eclParamM.getLoginVerfahren() == 0) {
                /*Standard-Login*/
                erg = blTeilnehmerLogin.findeUndPruefeKennung(lKennung, lPasswort, true);
                if (erg < 0) {
                    CaBug.druckeLog("G1 fehler=" + CaFehler.getFehlertext(erg, 0), logDrucken, 10);
                    /*Fehler aufgetreten*/
                    tSessionVerwaltung.setzeEnde();
                    eclDbM.closeAllAbbruch();
                    tLoginLogoutSession.trageFehlerEin(erg);
                    tSession.setViewnummer(liefereSeiteLogin());
                    nichtErfolgreicherLogin();
                    return;
                }
            } else {
                /*ku216-Login*/
                boolean passwortEingegeben = false;
                if (!lPasswort.isEmpty()) {
                    passwortEingegeben = true;
                }

                if (passwortEingegeben == true) {
                    erg = blTeilnehmerLogin.findeUndPruefeKennung(lKennung, lPasswort, true);
                    if (erg < 0) {
                        /*Fehler aufgetreten*/
                        tSessionVerwaltung.setzeEnde();
                        eclDbM.closeAllAbbruch();
                        tLoginLogoutSession.trageFehlerEin(erg);
                        tSession.setViewnummer(liefereSeiteLogin());
                        nichtErfolgreicherLogin();
                        return;
                    }
                } else {
                    erg = blTeilnehmerLogin.findeUndPruefeKennung(lKennung, "", false);
                    if (erg < 0) {
                        /*Fehler aufgetreten*/
                        tSessionVerwaltung.setzeEnde();
                        eclDbM.closeAllAbbruch();
                        tLoginLogoutSession.trageFehlerEin(erg);
                        tSession.setViewnummer(liefereSeiteLogin());
                        nichtErfolgreicherLogin();
                        return;
                    }

                    /*Name / Datum prüfen*/
                    if (blTeilnehmerLogin.eclLoginDaten.kennungArt == KonstLoginKennungArt.personenNatJur) {
                        tSessionVerwaltung.setzeEnde();
                        eclDbM.closeAllAbbruch();
                        tLoginLogoutSession.trageFehlerEin(CaFehler.afPasswortFalsch);
                        tSession.setViewnummer(liefereSeiteLogin());
                        nichtErfolgreicherLogin();
                        return;
                    }

                    EclAktienregister lAktienregister = blTeilnehmerLogin.eclAktienregister;

                    /*Alle Daten als juristische Person eintragen. Und Name 1+Name2+Name3 = Login
                     * und zwar ohne Blanks trennen
                     * 
                     * Datum wird in email eingetragen*/

                    String nameVergleich = lAktienregister.name1 + lAktienregister.name2 + lAktienregister.name3;
                    String datumVergleich = lAktienregister.email;
                    if (!tLoginLogoutSession.getLoginName().trim().equals(nameVergleich)) {
                        CaBug.druckeLog("nameVergleich=" + nameVergleich, logDrucken, 10);
                        CaBug.druckeLog("aDlgVariablen.getLoginName()=" + tLoginLogoutSession.getLoginName(), logDrucken, 10);
                        tSessionVerwaltung.setzeEnde();
                        eclDbM.closeAllAbbruch();
                        tLoginLogoutSession.trageFehlerEin(CaFehler.afLoginNameFalsch);
                        tSession.setViewnummer(liefereSeiteLogin());
                        nichtErfolgreicherLogin();
                        return;
                    }
                    if (!tLoginLogoutSession.getLoginDatum().trim().equals(datumVergleich)) {
                        CaBug.druckeLog("datumVergleich=" + datumVergleich, logDrucken, 10);
                        tSessionVerwaltung.setzeEnde();
                        eclDbM.closeAllAbbruch();
                        tLoginLogoutSession.trageFehlerEin(CaFehler.afLoginDatumFalsch);
                        tSession.setViewnummer(liefereSeiteLogin());
                        nichtErfolgreicherLogin();
                       return;
                    }

                }

            }

            CaBug.druckeLog("H", logDrucken, 10);
            if (tSession.isPermanentPortal()) {
                /*Hier sicherstellen, dass Ticket-Status lokal aktualisiert ist!*/
                int rc = brMAuftraege.aktualisiereLokaleAuftraege(blTeilnehmerLogin.eclLoginDaten.loginKennung, blTeilnehmerLogin.eclLoginDaten.ident * (-1));
                if (tRemoteAR.pruefeVerfuegbar(rc) == false) {
                    tSessionVerwaltung.setzeEnde();
                    eclDbM.closeAllAbbruch();
                    return;
                }
                //                if (brMLogin.isRcEmailAuftrageVorhanden()) {
                //                    eMailInRemoteRegister=blTeilnehmerLogin.eclLoginDaten.eMailFuerVersand;
                //                    CaBug.druckeLog("H.1A eMailInRemoteRegister="+eMailInRemoteRegister,logDrucken, 10);
                //                }
                //                else {
                eMailInRemoteRegister = brMLogin.getRcEMailInRemoteRegister();
                CaBug.druckeLog("H.1B eMailInRemoteRegister=" + eMailInRemoteRegister, logDrucken, 10);
                //                }
                brMAuftraege.setzeOberflaechenStatusLtAuftraege(blTeilnehmerLogin.eclLoginDaten.ident * (-1));
                if (pAktionaersdatenSession.isEmailAenderungVorhanden() && brMLogin.isRcEmailAuftrageVorhanden() == false) {
                    /*Es sind Mail-Tickets vorhanden. Deshalb ggf. noch
                     * vorhandene abweichende Mail in GeDix ignorieren
                     */
                    eMailNachRemoteRegisterUebertragen = true;
                }

            }
            blTeilnehmerLogin.eMailInRemoteRegister = eMailInRemoteRegister;
            CaBug.druckeLog("blTeilnehmerLogin.eMailInRemoteRegister=" + blTeilnehmerLogin.eMailInRemoteRegister, logDrucken, 10);

            blTeilnehmerLogin.eMailInRemoteRegisterinArbeit = eMailNachRemoteRegisterUebertragen;
            CaBug.druckeLog("eMailInRemoteRegisterinArbeit=" + eMailNachRemoteRegisterUebertragen, logDrucken, 10);

            blTeilnehmerLogin.pruefeErstregistrierung();

            /*Login für andere Sperren*/
            erg = blTeilnehmerLogin.sperreTempLogin();
            if (erg < 0) {
                /*Dann konnte nicht gesperrt werden, weil gerade anderer gesperrt hat*/
                tSessionVerwaltung.setzeEnde();
                eclDbM.closeAllAbbruch();
                tLoginLogoutSession.trageFehlerEin(erg);
                tSession.setViewnummer(liefereSeiteLogin());
                return;
            }
            //            EclAktienregister lAktienregister=null;
            //            lAktienregister.adresszeile1="";

            //		EclMeldung lMeldung=null;
            //		lMeldung.meldungAktiv=1;

            tLoginLogoutSession.clearFehler();
            eclLoginDatenM.copyFrom(blTeilnehmerLogin);

            if (tSession.isPermanentPortal()) {
                /**Nun aus Aktienregister geholte Anzeigedaten besetzen*/
                eclLoginDatenM.setAnmeldeKennungFuerAnzeige(tPermanentSession.getAnmeldeKennungFuerAnzeige());
                eclLoginDatenM.setTitelVornameName(tPermanentSession.getTitelVornameName());
                eclLoginDatenM.setOrt(tPermanentSession.getOrt());
            }

            /*Bestehende Logins bereinigen*/
            if (blTeilnehmerLogin.eclLoginDaten.letzterLoginAufServer != 0) {
                /*Bereits eingeloggt - nun anderen Login bereinigen*/

                /*Hinweis: Es braucht nix bereinigt werden, außer Präsenzbuchungen.
                 * Denn:
                 * > Auf selbem Server-Login wird durch Eintragen des Users als Session automatisch bereinigt.
                 * > Auf anderem Server-Login wird durch Setzen der Servernummer ebenfalls "deaktiviert".
                 */

                /*Hier ggf. Präsenzbuchungen abgehen lassen*/
                CaBug.druckeLog("User schon eingeloggt - Meldungen abgehen lassen", logDrucken, 1);
                if (tSession.isPermanentPortal() == false) {
                    tFunktionen.leseStatusPortal(eclDbM.getDbBundle());
                    tPraesenzZugangAbgang.praesenzAbgang();
                }
            }

            tUserSessionManagement.eintragenUser();

            /*Ggf. Stati für WOrtmeldung bereinigen*/
            if ((eclLoginDatenM.getEclLoginDaten().konferenzTestAblauf & 2048)==2048 ) {
                eclDbM.getDbBundle().dbLoginDaten.updateKonferenzTestAblauf(eclLoginDatenM.getEclLoginDaten().ident, 0);
            }
            if ((eclLoginDatenM.getEclLoginDaten().konferenzSprechen & 2048)==2048 ) {
                eclDbM.getDbBundle().dbLoginDaten.updateKonferenzSprechen(eclLoginDatenM.getEclLoginDaten().ident, 0);
            }
            
            /*Ggf Wortmeldung-Erteilt setzen*/
            eclDbM.getDbBundle().dbMitteilung.setzeFunktion(KonstPortalFunktionen.wortmeldungen);
            eclDbM.getDbBundle().dbMitteilung.readAll_loginIdent(eclLoginDatenM.getEclLoginDaten().ident);
            if (eclDbM.getDbBundle().dbMitteilung.anzErgebnis()>0) {
                eclBesitzGesamtM.setKennungHatWortmeldungErteilt(true);
            }
            
            /*Aktuellen Login updaten*/
            long lZeitstempel = blTeilnehmerLogin.updateLetzterLoginAufServer(eclParamM.getBmServernummer());
            eclLoginDatenM.getEclLoginDaten().letzterLoginAufServer = eclParamM.getBmServernummer();
            eclLoginDatenM.getEclLoginDaten().zeitstempel = lZeitstempel;

            if (eclLoginDatenM.getEclLoginDaten().kennungArt == KonstLoginKennungArt.aktienregister) {
                eclLoginDatenM.setTestanzeige(KonstGruppen.getText(eclLoginDatenM.getEclAktienregister().gruppe));
            } else {
                eclLoginDatenM.setTestanzeige("");
            }

            CaBug.druckeLog("blTeilnehmerLogin.eclLoginDaten.kennungArt=" + blTeilnehmerLogin.eclLoginDaten.kennungArt, logDrucken, 1);
            CaBug.druckeLog("eclLoginDatenM.getEclLoginDaten().kennungArt=" + eclLoginDatenM.getEclLoginDaten().kennungArt, logDrucken, 1);

            tPortalFunktionen.belegePortalFunktionenWillenserklaerungenStatusAktiv();

            tSession.setUserEingeloggt("1");

            erfolgreicherLogin();
            
            /**********************Verzweigungslogik:*******************************
             * Falls Erstregistrierung erforderlich => tEinstellungen
             *
             * Falls direkt zur Online-Teilnahme (nurRawLiveAbstimmung) => Online-Teilnahme-Start
             * 		(Für Veranstaltungen nur mit Online-Teilnahme ohne sonstige Möglichkeiten, z.B. Vereine)
             *
             * Ab hier noch weiter durchdenken ....
             *
             * Parameter:
             * > Bestände anzeigen oder nicht
             * > Erforderlich zu wissen:
             * >> Gibt es überhaupt Aktienbestände dazu?
             * >> Gibt es angemeldete Aktienbestände dazu?
             *
             * Falls Menü vorhanden (optional für normal-Aktionäre, zwingend für Instis und Gäste):
             * > Auswahl-Menü für normal-Aktionäre
             * > Auswahl-Menü für Insti-Kennung
             * > Auswahl-Menü für Personen
             *
             * Falls Menü nicht vorhanden:
             * > Falls
             */
            /**Nun verzweigen auf die nächste Maske*/
            if (blTeilnehmerLogin.erstregistrierungAufrufen) {
                CaBug.druckeLog("Erstregistrierung aufrufen", logDrucken, 10);
                eclDbM.closeAll();
                /*Erstregistrierung muß aufgerufen werden*/
                tEinstellungen.startRegistrierungNachLogin(blTeilnehmerLogin);
                tSessionVerwaltung.setzeEnde(tEinstellungen.liefereSeiteEinstellungen());
                return;
            }

            int auswahlView = tAuswahl.startAuswahl(true);
            if (eclParamM.liefereZuschaltungHVAutomatischNachLogin() && tAuswahlSession.isOnlineteilnahmeAktiv() && eclParamM.getParam().paramPortal.varianteDialogablauf==0) {
                tPraesenzZugangAbgang.initVirtuelleHV();
                tPraesenzZugangAbgang.zugangBuchenVirtuelleHV();
                tAuswahl.startAuswahl(true);
           }
            
            /**checke ob websocket notwenig und erstelle token*/
            if(eclBesitzGesamtM.isKennungHatWortmeldungErteilt()) {
                BlNummernformBasis blNummernformBasis = new BlNummernformBasis(eclDbM.getDbBundle());
                String lAnmeldeKennung = blNummernformBasis.loginKennungAufbereitenFuerIntern(lKennung);
                lAnmeldeKennung = BlNummernformBasis.aufbereitenFuerDatenbankZugriff(lAnmeldeKennung, eclDbM.getDbBundle());
                CaTokenUtil caTokenUtil = new CaTokenUtil();
                String jwt = caTokenUtil.generateToken(lAnmeldeKennung, eclParamM.getParam().mandant);
                tSession.setJwtWsToken(jwt);
            }
            
            eclDbM.closeAll();
            if (auswahlView < 0) {
                tSession.trageFehlerEin(auswahlView);
                tSessionVerwaltung.setzeEnde(KonstPortalView.fehlerSysLogout);
                return;
            }

            if (auswahlView == KonstPortalView.P_AUSWAHL) {
                CaBug.druckeLog("auswahlView == KonstPortalView.P_AUSWAHL A", logDrucken, 10);
                auswahlView=tMenue.init();
                CaBug.druckeLog("auswahlView="+auswahlView, logDrucken, 10);
            }

            tSessionVerwaltung.setzeEnde(auswahlView);
            
            
        } catch (Exception e) {
            CaBug.drucke("Exception");
            System.out.println(e.getMessage());
            e.printStackTrace();
            eclDbM.closeAll();
        }

        return;

        //		/*XXX*/
        //		/*Hier ist Login-Überprüfung grundsätzlich abgeschlossen - d.h. es ist ein Berechtigter
        //		 * angemeldet.
        //		 * Im folgenden nun Checken, welche Auswahlmaske aufgerufen werden soll
        //		 */
        //		aDlgVariablen.clearDlg();
        //		if (aControllerRegistrierung.pruefeErstregistrierung()==true){
        //			aControllerRegistrierung.initPublikationListe();
        //			aControllerRegistrierung.startRegistrierung();
        //			eclDbM.closeAll();
        //			aDlgVariablen.setErstRegistrierungOderEinstellungenAktiv(1);
        //			return aFunktionen.setzeEnde("aRegistrierung", true, false);
        //		}
        //
        //		if (eclTeilnehmerLoginM.getAnmeldeKennungArt()==1 /*Anmeldung mit Aktienregister-Kennung*/
        //				|| eclTeilnehmerLoginM.getAnmeldeKennungArt()==3 /*Anmeldung über Personen NatJur*/
        //				){
        //			String naechsteMaske=aFunktionen.waehleAusgangsmaske(eclDbM.getDbBundle());
        //			eclDbM.closeAll();
        //
        //			return aFunktionen.setzeEnde(naechsteMaske, true, true);
        //		}
        //
        //
        //		eclDbM.closeAll();
        //		if (eclParamM.getParam().paramPortal.varianteDialogablauf==0) {
        //			return aFunktionen.setzeEnde("aAnmelden", true, true);
        //		}
        //		else {
        //			return aFunktionen.setzeEnde("aAuswahl1", true, true);
        //		}
    }

    public void doPasswortVergessen() {
        if (!tSessionVerwaltung.pruefeStartOhneUserLoginPruefung(liefereSeiteLogin())) {
            tSessionVerwaltung.setzeEnde();
            return;
        }
        tPasswortVergessen.passwortZuruecksetzenVorbereiten();
        tSessionVerwaltung.setzeEnde(KonstPortalView.PASSWORT_VERGESSEN);
    }

    /**Für Permanent-Portal*/
    public void doPPasswortVergessen() {
        if (!tSessionVerwaltung.pruefeStartOhneUserLoginPruefung(liefereSeiteLogin())) {
            tSessionVerwaltung.setzeEnde();
            return;
        }
        tPasswortVergessen.passwortZuruecksetzenVorbereiten();
        tSessionVerwaltung.setzeEnde(KonstPortalView.P_PASSWORT_VERGESSEN);
    }

    
    /**Permanent-Portal*/
    public void doRegistrieren() {
        tPasswortVergessen.passwortZuruecksetzenVorbereiten();
        tSessionVerwaltung.setzeEnde(KonstPortalView.P_REGISTRIEREN);

    }

    public void doLogout() {
        
        try {
            CaBug.druckeLog("001", logDrucken, 10);
            tSessionVerwaltung.pruefeStartBeiLogout(KonstPortalView.logout);
            //        CaBug.druckeLog("setUserEingeloggt", logDrucken, 10);
            //        tSession.setUserEingeloggt("0");
            eclDbM.openAll();
            CaBug.druckeLog("002", logDrucken, 10);
            boolean bRc = tPruefeStartNachOpen.pruefeStartNachOpen();
            CaBug.druckeLog("003", logDrucken, 10);
            if (bRc) {
                /*Wenn False, dann bereits alles deaktiviert, oder Portal gesperrt*/
                logoutDurchfuehren();
            }
            CaBug.druckeLog("004", logDrucken, 10);

            BlEinsprungLinkPortal blEinsprungLinkPortal = new BlEinsprungLinkPortal(eclDbM.getDbBundle());
            String sprache = "DE";
            if (tLanguage.getLang() == 2) {
                sprache = "EN";
            }
            String zielLink = blEinsprungLinkPortal.linkFuerPortalStart(tSession.getDifZeit(), tSession.getHvJahr(), tSession.getHvNummer(), tSession.getDatenbereich(), sprache,
                    tSession.getTestModus(), tSession.isPermanentPortal());
            CaBug.druckeLog("005", logDrucken, 10);

            eclDbM.closeAll();
            tLoginLogoutSession.clearAll();
            CaBug.druckeLog("006", logDrucken, 10);
            tSession.clearSession();
            tSession.setViewnummer(liefereSeiteLogin());
            tSession.setUserEingeloggt("0");

            CaBug.druckeLog("zielLink=" + zielLink, logDrucken, 5);
            tJsfSession.loescheAktuelleSession(zielLink);

            CaBug.druckeLog("kurzVorReturn", logDrucken, 10);
            
            return;
        } catch (Exception e) {
            CaBug.drucke("Exception");
            //            System.out.println(e.getMessage());
            //            e.printStackTrace();
            eclDbM.closeAll();
        }
    }

    public void doBeenden() {
        String zielLink = eclParamM.getParam().paramPortal.logoutZiel;
        CaBug.druckeLog("ziel-Link=" + zielLink, logDrucken, 10);
        tJsfSession.loescheAktuelleSession(zielLink);
        return;

    }

    private void logoutDurchfuehren() {
        CaBug.druckeLog("001", logDrucken, 10);
        BlTeilnehmerLoginNeu blTeilnehmerLogin = new BlTeilnehmerLoginNeu();
        blTeilnehmerLogin.initDB(eclDbM.getDbBundle());
        eclLoginDatenM.copyToForReload(blTeilnehmerLogin);
        int erg = blTeilnehmerLogin.sperreTempLogin();
        CaBug.druckeLog("002", logDrucken, 10);
        if (erg < 0) {
            /*Dann konnte nicht gesperrt werden, weil gerade anderer gesperrt hat*/
            return;
        }
        CaBug.druckeLog("003", logDrucken, 10);

        if (eclParamM.getParam().paramPortal.varianteDialogablauf==0) {
            /*Standard HV*/
            if (eclBesitzGesamtM.isKennungIstOnlinePraesent()) {
                tPraesenzZugangAbgang.abgangBuchenVirtuelleHV();
            }
        }
        else {
            /*Verein*/
            tPraesenzZugangAbgang.praesenzAbgang();
        }
        CaBug.druckeLog("004", logDrucken, 10);

        tSession.setKonferenzshow(false);

        tUserSessionManagement.austragenUser();
        CaBug.druckeLog("005", logDrucken, 10);

        /*Aktuellen Login updaten auf ausgeloggt*/
        blTeilnehmerLogin.updateLetzterLoginAufServer(0);
        CaBug.druckeLog("006", logDrucken, 10);

    }

    public String doLogoutVerlassen() {
        tLoginLogoutSession.clearAll();
        return tFunktionen.waehleLogout();
    }

    public void doBrowserSchliessen() {
        try {
            CaBug.druckeLog("Browser Schließen", logDrucken, 2);

            tSessionVerwaltung.pruefeStartBeiLogout(KonstPortalView.browserschliessen);
            CaBug.druckeLog("A", logDrucken, 10);
            eclDbM.openAll();
            boolean bRc = false;
            if (eclLoginDatenM.getEclLoginDaten() != null && eclLoginDatenM.getEclLoginDaten().ident != 0) {
                CaBug.druckeLog("If", logDrucken, 10);
                CaBug.druckeLog("eclLoginDatenM.getEclLoginDaten().ident=" + eclLoginDatenM.getEclLoginDaten().ident, logDrucken, 10);
                bRc = tPruefeStartNachOpen.pruefeStartNachOpen();
            } else {
                CaBug.druckeLog("Else", logDrucken, 10);
                /**Entweder noch gar kein Login erfolgt, oder User bereits gecleard*/
                tPruefeStartNachOpen.pruefeStartNachOpenOhneUser();
            }

            if (bRc) {
                /*Wenn False, dann bereits alles deaktiviert, oder Portal gesperrt*/
                if (tSession.getUserEingeloggt().equals("1")) {
                    logoutDurchfuehren();
                }
                if (tSession.getUserEingeloggt().equals("2")) {
                    tSession.setUserEingeloggt("1");
               }
            }
            tSession.setUserEingeloggt("0");
            
            eclDbM.closeAll();
            CaBug.druckeLog("Ende", logDrucken, 5);
        } catch (Exception e) {
            CaBug.drucke("Exception");
            //            System.out.println(e.getMessage());
            //            e.printStackTrace();
            eclDbM.closeAll();
        }
    }

    /*+++++++++++++++++++++++++++++++++Verarbeitungslogiken++++++++++++++++++++++++++++*/

    public int liefereSeiteLogin() {
        if (tSession.isPermanentPortal()) {
            return KonstPortalView.P_LOGIN;
        } else {
            return KonstPortalView.LOGIN;
        }
    }

    
    /*++++++++++++++++++++++++++++Login-Schutz für Verzögerung +++++++++++++++++*/
    /**Setzt Login-Versuche wieder auf 0 - aufzurufen bei erfolgreichem Login*/
    private void erfolgreicherLogin() {
        tLoginLogoutSession.setAnzahlLoginFehlversuche(0);
        tLoginLogoutSession.setNaechsterZuleassigerLoginTimeStamp(0L);
    }
    
    /**Setzt Login-Versuchszähler hoch etc. - aufzurufen bei nicht-erfolgreichem Login*/
    private void nichtErfolgreicherLogin() {
        int anzahlBisherigeLoginFehlversuche=tLoginLogoutSession.getAnzahlLoginFehlversuche();
        long aktuellerTimeStamp=CaDatumZeit.zeitStempelMS();
        long naechsterZulaessigerLoginTimeStamp=aktuellerTimeStamp;
        
        int loginVerzoegerungAbVersuch=eclParamM.getParam().paramPortal.loginVerzoegerungAbVersuch;
        int loginVerzoegerungSekunden=eclParamM.getParam().paramPortal.loginVerzoegerungSekunden;
        
        if (anzahlBisherigeLoginFehlversuche>0 && loginVerzoegerungAbVersuch>0) {
            /*Zeitstempel-Erhöhung berechnen*/
            int dif=anzahlBisherigeLoginFehlversuche/loginVerzoegerungAbVersuch;
            naechsterZulaessigerLoginTimeStamp=CaDatumZeit.addSekundenZuStempel(naechsterZulaessigerLoginTimeStamp, dif*loginVerzoegerungSekunden);
        }
        CaBug.druckeLog("aktuellerTimeStamp="+aktuellerTimeStamp+" naechsterZulaessigerLoginTimeStamp="+naechsterZulaessigerLoginTimeStamp, logDrucken, 10);
        tLoginLogoutSession.setNaechsterZuleassigerLoginTimeStamp(naechsterZulaessigerLoginTimeStamp);
        tLoginLogoutSession.setAnzahlLoginFehlversuche(anzahlBisherigeLoginFehlversuche+1);
    }
    
    /**>0 => Login wegen Verzögerung erst in x Sekunden möglich*/
    private int pruefeObZeitverzoegerungFuerLoginVersuch() {
        long naechsterZulaessigerLoginTimeStamp=tLoginLogoutSession.getNaechsterZuleassigerLoginTimeStamp();
        int dif=CaDatumZeit.ermittleSekundenBisStempel(naechsterZulaessigerLoginTimeStamp);
        return dif;
    }
    
    
}
