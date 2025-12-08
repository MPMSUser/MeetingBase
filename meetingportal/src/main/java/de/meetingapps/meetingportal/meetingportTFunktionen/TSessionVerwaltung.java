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
package de.meetingapps.meetingportal.meetingportTFunktionen;

import java.util.List;

import de.meetingapps.meetingportal.meetComAllg.CaBug;
import de.meetingapps.meetingportal.meetComAllg.CaDatumZeit;
import de.meetingapps.meetingportal.meetComAllg.CaFehler;
import de.meetingapps.meetingportal.meetComAllg.CaString;
import de.meetingapps.meetingportal.meetComBlManaged.BlMFuelleEclMAusPufferOderDBEE;
import de.meetingapps.meetingportal.meetComBlManaged.BlMPuffer;
import de.meetingapps.meetingportal.meetComEclM.EclDbM;
import de.meetingapps.meetingportal.meetComEclM.EclLoginDatenM;
import de.meetingapps.meetingportal.meetComEclM.EclParamM;
import de.meetingapps.meetingportal.meetComEclM.EclPortalTexteM;
import de.meetingapps.meetingportal.meetComEclM.EclPortalUnterlagenM;
import de.meetingapps.meetingportal.meetComEntities.EclEmittenten;
import de.meetingapps.meetingportal.meetComHVParam.ClGlobalVar;
import de.meetingapps.meetingportal.meetComKonst.KonstPortalUnterlagen;
import de.meetingapps.meetingportal.meetComKonst.KonstPortalView;
import de.meetingapps.meetingportal.meetingportTController.TBestaetigenSession;
import de.meetingapps.meetingportal.meetingportTController.TLanguage;
import de.meetingapps.meetingportal.meetingportTController.TLoginLogoutSession;
import de.meetingapps.meetingportal.meetingportTController.TMenueSession;
import de.meetingapps.meetingportal.meetingportTController.TPasswortVergessenSession;
import de.meetingapps.meetingportal.meetingportTController.TSession;
import de.meetingapps.meetingportal.meetingportTController.TUnterlagenSession;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@RequestScoped
@Named
public class TSessionVerwaltung {

    private int logDrucken = 3;

    @Inject
    private EclParamM eclParamM;
    @Inject
    private EclDbM eclDbM;
    private @Inject BlMFuelleEclMAusPufferOderDBEE blMFuelleEclMAusPufferOderDBEE;
    @Inject
    private BlMPuffer blMPuffer;
    private @Inject EclPortalTexteM eclPortalTexteM;
    @Inject
    private TSeitensteuerung tSeitensteuerung;
    @Inject
    private TLanguage tLanguage;

    @Inject
    private TSession tSession;
    @Inject
    private TLoginLogoutSession tLoginLogoutSession;
    @Inject
    private TUnterlagenSession tUnterlagenSession;

    @Inject
    private TMenueSession tMenueSession;
    @Inject
    private TJsfSession tJsfSession;
    @Inject
    private EclLoginDatenM eclLoginDatenM;
    @Inject
    private TPortalFunktionen tPortalFunktionen;
    @Inject
    private TUserSessionManagement tUserSessionManagement;
    @Inject
    private TPasswortVergessenSession tPasswortVergessenSession;
    @Inject
    private TBestaetigenSession tBestaetigenSession;

    /*****************************************
     * Initialisieren und Reload Mandant (einschließlich parameter)******************** Setzt Mandantennummer, liest
     * Parameter (HVParam und ClParameterNS, Fehlertexte) ein und belegt diese in der globalen Verwaltungstabelle
     * (EclParamListeM) und belegt die Beans für die Parameter (EclParamM, EclParameterM, EclFehlerM)
     */

    /**
     * Link-Aufbau: zeit=TTMMJJJJHHMMSS (optional) Hinweis: wenn zeit einmal angegeben, dann bleibt diese erhalten, bis
     * der Browser geschlossen wird.
     *
     * hvjahr (optional, aber wenn angegeben dann muß auch hvnummer und datenbereich gefüllt sein)
     */
    private String mandant = "";
    private String hvjahr = "";
    private String hvnummer = "";
    private String datenbereich = "";

    /**
     * Maske, die direkt aufgerufen wird - für Passwort vergessen und E-Mail-Bestätigung 1=bestaetigen 2=bestaetigen2
     * 3=pwzurueck 4=pwzurueckapp (für App)
     */

    private String linkArt = "";
    private String bestaetigungsCode = "";

    private String test = "";
    private String zeit = "";
    
    /**1 => Permanent-Portal ist aufgerufen*/
    private String permanentPortal="0";

    /** Wenn true, dann konnte die Bearbeitung nicht durchgeführt werden => Fehleranzeige */

    private boolean schwererFehler = false;

    /** wird eigentlich nicht benötigt .... nur zur Warnungsunterdrückung */
    public synchronized String getMandant() {
        return mandant;
    }

    private void fehlerhafterLinkGefunden() {
        eclParamM.getClGlobalVar().mandant = 0;
        eclParamM.getClGlobalVar().hvJahr = 0;
        eclParamM.getClGlobalVar().hvNummer = "";
        eclParamM.getClGlobalVar().datenbereich = "";
        tSession.setTechnischerFehlertext("Fehlerhafte URL - bitte überprüfen Sie den Aufruf-Link");
        //KonstPortalView.fehlerLinkAufruf);
    }

    private void zweiterMandantAufgerufen() {
        tSession.setViewnummer(KonstPortalView.fehlerZweiterMandant);
    }

    /**
     * Lädt die über den Link-Aufruf belegten Mandanten-Felder in die globalen Variablen und initialisiert den Mandant
     */
    public synchronized boolean ladenMandant() {
        
        
        /* ++++++mandantenNr prüfen und belegen++++++ */
        if (mandant.isEmpty() || mandant.length() > 5 || !CaString.isNummern(mandant)) {
            CaBug.druckeProt("Fehlerhafter Link: unzulässiges Format Mandant");
            fehlerhafterLinkGefunden();
            return false;
        }
        int mandantenNr = Integer.parseInt(mandant);
        if (mandantenNr == 0) {
            CaBug.druckeProt("Fehlerhafter Link: Mandant=0");
            fehlerhafterLinkGefunden();
            return false;
        }

        if (eclParamM.getClGlobalVar().mandant!=0 && eclParamM.getClGlobalVar().mandant!=mandantenNr) {
            
            CaBug.druckeProt("Zweiter Aufruf");
            zweiterMandantAufgerufen();
            String zielLink=tJsfSession.getKomplettenAufruf();
            CaBug.druckeLog("Ziellink="+zielLink, logDrucken, 3);
            tJsfSession.loescheAktuelleSession(zielLink);

            return false;
        }
        /* ++++++hvjahrNr prüfen und belegen - wenn nicht übergeben dann 0 ++++++++++ */
        int hvjahrNr = 0;
        if (!hvjahr.isEmpty()) {
            if (hvjahr.length() > 5 || !CaString.isNummern(hvjahr)) {
                CaBug.druckeProt("Fehlerhafter Link: unzulässiges Format HVJahr");
                fehlerhafterLinkGefunden();
                return false;
            }
            hvjahrNr = Integer.parseInt(hvjahr);
        }

        /* +++++hvnummer und datenbankbereich prüfen+++++++++++++ */
        if ((!hvnummer.isEmpty() && hvnummer.length() > 3) || (!datenbereich.isEmpty() && datenbereich.length() > 3)) {
            CaBug.druckeProt("Fehlerhafter Link: unzulässiges Format Datenbereich");
            fehlerhafterLinkGefunden();
            return false;
        }

        /* Sicherheitshalber initialisieren */
        if (eclParamM.getClGlobalVar() == null) {
            eclParamM.setClGlobalVar(new ClGlobalVar());
        }

        /* ++++++++++passenden Emittent einlesen++++++++++++ */
        boolean startNurMitMandant = false;
        if (hvjahrNr == 0 || hvnummer.isEmpty() || datenbereich.isEmpty()) {
            startNurMitMandant = true;
        }
        EclEmittenten lEmittenten = null;

        /* Ggf. Puffer füllen, sowie Emittenten-Auswahl vorbereiten */
        eclParamM.getClGlobalVar().mandant = 0; // auf 0 setzen, für den Fall dass noch von vorher gesetzt
        eclDbM.openAllOhneParameterCheck();
        blMFuelleEclMAusPufferOderDBEE.fuelleAlleVariablenBeiStart();
        if (startNurMitMandant) {
            lEmittenten = blMPuffer.getStandardEmittentFuerEmittentenPortal(mandantenNr);
            tSession.setHvJahr(0);
            tSession.setHvNummer("");
            tSession.setDatenbereich("");
        } else {
            lEmittenten = blMPuffer.getEmittent(mandantenNr, hvjahrNr, hvnummer, datenbereich);
            tSession.setHvJahr(hvjahrNr);
            tSession.setHvNummer(hvnummer);
            tSession.setDatenbereich(datenbereich);
        }
        eclDbM.closeAll();

        if (lEmittenten == null) {
            if (startNurMitMandant) {
                CaBug.druckeProt("001 - keinen aktiven Portal-Mandanten gefunden Mandant="+Integer.toString(mandantenNr));
                fehlerhafterLinkGefunden();
                return false;
            } else {
                CaBug.druckeProt("002 - Mandant/HV-Jahr etc. nicht gefunden="+Integer.toString(mandantenNr));
                fehlerhafterLinkGefunden();
                return false;
            }
        }

        eclParamM.getClGlobalVar().mandant = mandantenNr;
        eclParamM.getClGlobalVar().hvJahr = lEmittenten.hvJahr;
        eclParamM.getClGlobalVar().hvNummer = lEmittenten.hvNummer;
        eclParamM.getClGlobalVar().datenbereich = lEmittenten.dbArt;

        CaBug.druckeLog("mandant=" + mandantenNr + " lEmittenten.hvJahr=" + lEmittenten.hvJahr
                + " lEmittenten.hvNummer=" + lEmittenten.hvNummer + " lEmittenten.dbArt="+lEmittenten.dbArt, logDrucken,
                1);

        /* Nochmal aufrufen, damit ECLs zu aktuellem Emittent gefüllt werden */
        eclDbM.openAll();
        blMFuelleEclMAusPufferOderDBEE.fuelleAlleVariablenBeiStart();
        eclDbM.closeAll();
        
        boolean brc=eclPortalTexteM.portalTexteSindVorhanden();
        if (brc==false) {
            CaBug.drucke("Portaltexte sind nicht vorhanden");
            tSession.setTechnischerFehlertext("Portaltexte nicht vorhanden - falsches Basisset?");
            return false;
            
        }

        /** Prüfen, ob das verwendete Design mit dem Mandanten zusammenpaßt */
        String designKuerzel = tJsfSession.getDesignKuerzel();
        if (!designKuerzel.equals(eclParamM.getParam().paramPortal.designKuerzel)) {
            CaBug.druckeProt("Fehlerhafter Link: Design-Kürzel paßt nicht zum Mandant="+Integer.toString(mandantenNr));
            fehlerhafterLinkGefunden();
            return false;
        }

        if (eclParamM.getParamServer().pLocalPraefixLink.startsWith("https")) {
            tSession.setPraefixSocket("wss");
        } else {
            tSession.setPraefixSocket("ws");
        }
        CaBug.druckeLog("praefixSocket=" + tSession.getPraefixSocket(), logDrucken, 10);

        tSession.setJsfSessionId(tJsfSession.getSessionID());
        tLoginLogoutSession.clearAll();
        tSession.clearSession();
        if (tSession.isPermanentPortal()) {
            tSession.setViewnummer(KonstPortalView.P_LOGIN);
        }
        else {
            tSession.setViewnummer(KonstPortalView.LOGIN);
        }
        tSeitensteuerung.belegeFuerSeite();
        
//        /**Auf Standard setzen. Wird ggf. von späterem Parameter per=1 überschrieben.
//         * In diesem Fall dann auch setViewnummer und belegeFuerSeite
//         * nochmal aufrufen
//         */
//        permanentPortal="0";
//        tSession.setPermanentPortal(false);

        return true;
    }

    /**
     * Muß aus erster JSF-Startseite aus aufgerufen werden - Mandant wird gesetzt, und Parameter werden in Puffer
     * geladen.
     */
    public synchronized void setMandant(String mandant) {

        tMenueSession.setDeepLinkPMenueFunktionscode(0);
        tMenueSession.setDeepLinkPMenueFunktionscodeSub(0);
        
        /*
         * Ansatzpunkte: > wenn die Session bereits eingetragen ist, dann nichts neues setzen. Geht aber nicht, weil
         * wenn neuer Link aufgerufen ...
         *
         * > zweite Session ganz normal eröffnen. Bei jedem Button checken - wenn zwei socket-Sessions offen, dann
         * Hinweis auf parallele Session => Browser schließen
         *
         *
         */
        CaBug.druckeLog("set Mandant=" + mandant, logDrucken, 2);
        this.mandant = mandant;
        
        boolean brc=ladenMandant();
        if (brc==false) {
            tSession.setViewnummer(KonstPortalView.FEHLER_VIEW_TECHNISCH);
            return;
        }
        
        /*Falls Sprache DE nicht aktiviert, dann Englisch setzen*/
        int sprache=eclParamM.getEclEmittent().portalSprache;
         if ((sprache & 1) ==0 && sprache!=0) {
            tLanguage.setLang(2);
        }
         
        /* Werte auf Standard setzen, damit sie ggf. von vorherigem Aufruf noch passen, falls nicht von Parametern überschrieben*/
        tSession.setTestModus("0");
//        tSession.setPermanentPortal(false);

        tPortalFunktionen.belegeAktivFuerWillenserklaerungen();
        
        /*Nun noch öffentliche Unterlagen belegen*/
        List<EclPortalUnterlagenM> unterlagenListe=null;
        boolean unterlagenListeVorhanden=false;
        
        unterlagenListe=tPortalFunktionen.erzeugeUnterlagenliste(KonstPortalUnterlagen.ANZEIGEN_LOGIN_OBEN, 0);
        if (unterlagenListe==null || unterlagenListe.size()==0) {
            unterlagenListeVorhanden=false;
        }
        else {
            unterlagenListeVorhanden=true;
        }
        tLoginLogoutSession.setUnterlagenListeOben(unterlagenListe);
        tLoginLogoutSession.setUnterlagenListeObenVorhanden(unterlagenListeVorhanden);
        
        unterlagenListe=tPortalFunktionen.erzeugeUnterlagenliste(KonstPortalUnterlagen.ANZEIGEN_LOGIN_UNTEN, 0);
        if (unterlagenListe==null || unterlagenListe.size()==0) {
            unterlagenListeVorhanden=false;
        }
        else {
            unterlagenListeVorhanden=true;
        }
        tLoginLogoutSession.setUnterlagenListeUnten(unterlagenListe);
        tLoginLogoutSession.setUnterlagenListeUntenVorhanden(unterlagenListeVorhanden);
        tUnterlagenSession.setUnterlagenbereich(KonstPortalUnterlagen.ANZEIGEN_LOGIN_UNTENODEROBEN);
    }

    /** Nur Dummy */
    public String getTest() {
        return test;
    }

    public void setTest(String test) {
        if ((eclParamM.getParam().paramPortal.testModus & 1) == 1 || test.equals("3")) {
            tSession.setTestModus(test);
            this.test = test;
        } else {
            this.test = "0";
            tSession.setTestModus("0");
        }
    }

    /** Nur Dummy */
    public String getZeit() {
        return zeit;
    }

    /**ze9t=TTMMJJJJHHMMSS*/
    public void setZeit(String zeit) {
        String hZeit = zeit;
        CaBug.druckeLog("zeit=" + zeit + " zeit.length()=" + zeit.length(), logDrucken, 2);
        if ((eclParamM.getParam().paramPortal.testModus & 2) == 2 && zeit.length() == 14) {
            hZeit = zeit.substring(0, 2) + "." + zeit.substring(2, 4) + "." + zeit.substring(4, 8) + " "
                    + zeit.substring(8, 10) + ":" + zeit.substring(10, 12) + ":" + zeit.substring(12);
            long zeitDif = CaDatumZeit.DatumZeitStringToLong(hZeit) - CaDatumZeit.zeitStempelMS();
            tSession.setDifZeit(zeitDif);
            this.zeit = zeit;
        } else {
            this.zeit = "";
            tSession.setDifZeit(0);
        }
    }

    public void setBestaetigungsCode(String bestaetigungsCode) {
        tSession.clearFehler();
        CaBug.druckeLog("bestaetigungsCode="+bestaetigungsCode+" linkArt="+linkArt+" tSession.isPermanentPortal()="+tSession.isPermanentPortal(), logDrucken, 3);
        this.bestaetigungsCode = bestaetigungsCode;
        if (linkArt == null || linkArt.isEmpty() || linkArt.length() > 5) {
            linkArt = "";
            return;
        }
        switch (linkArt) {
            case "1":
                tBestaetigenSession.setBestaetigungsCode(bestaetigungsCode);
                tSession.setViewnummer(KonstPortalView.BESTAETIGEN);
                break;
            case "2":
                tBestaetigenSession.setBestaetigungsCode2(bestaetigungsCode);
                tSession.setViewnummer(KonstPortalView.BESTAETIGEN2);
                break;
            case "3":
                if (tSession.isPermanentPortal()==false) {
                    tSession.setViewnummer(KonstPortalView.PW_ZURUECK);
                }
                else {
                    tSession.setViewnummer(KonstPortalView.P_PW_ZURUECK);
                }
                tPasswortVergessenSession.setBestaetigungsCode(bestaetigungsCode);
                break;
        }
        tSeitensteuerung.belegeFuerSeite();
    }

    
    /**"1" = Permanent-Portal; "2"=HV-Portal*/
    public void setPermanentPortal(String permanentPortal) {
        CaBug.druckeLog("permanentPortal="+permanentPortal, logDrucken, 10);
        CaBug.druckeLogAufrufsequenz("permanentPortal="+permanentPortal, logDrucken, 10);
        if (permanentPortal.equals("1")) {
            tSession.setPermanentPortal(true);
            tSession.setViewnummer(KonstPortalView.P_LOGIN);
            tSeitensteuerung.belegeFuerSeite();
        }
        if (permanentPortal.equals("2")) {
            tSession.setPermanentPortal(false);
            tSession.setViewnummer(KonstPortalView.LOGIN);
            tSeitensteuerung.belegeFuerSeite();
         }
    }

    
    /**************************** Session-Start/Ende-Routinen für Seitenwechsel *************************/
    private int startPruefen = 1;
    private int bereitsAufgerufen = 0;

    /* ++++++++++++++++Prüfen bei jedem Start - ohne Open+++++++++++++++++++++++++++++++ */
    public boolean pruefeStartOhneMaskenpruefung() {
        int[] aufrufMasken = new int[1];
        aufrufMasken[0] = 0;
        return pruefeStart(aufrufMasken, eclLoginDatenM.getAnmeldeKennungFuerAnzeige(), true, true, false);
    }

    public boolean pruefeStart(int pAufrufMaske) {
        return pruefeStart(pAufrufMaske, eclLoginDatenM.getAnmeldeKennungFuerAnzeige());
    }

    public boolean pruefeStart(int[] pAufrufMaske) {
        return pruefeStart(pAufrufMaske, eclLoginDatenM.getAnmeldeKennungFuerAnzeige());
    }

    public boolean pruefeStartOhneUserLoginPruefung(int pAufrufMaske) {
        int[] aufrufMasken = new int[1];
        aufrufMasken[0] = pAufrufMaske;
        return pruefeStart(aufrufMasken, eclLoginDatenM.getAnmeldeKennungFuerAnzeige(), false, false, true);
    }

    public void pruefeStartBeiLogout(int pAufrufMaske) {
        int[] aufrufMasken = new int[1];
        aufrufMasken[0] = pAufrufMaske;
        pruefeStart(aufrufMasken, eclLoginDatenM.getAnmeldeKennungFuerAnzeige(), true, true, true);
        return;
    }

    public boolean pruefeStart(int pAufrufMaske, String pLogin) {
        int[] aufrufMasken = new int[1];
        aufrufMasken[0] = pAufrufMaske;
        return pruefeStart(aufrufMasken, pLogin, true, false, true);
    }

    public boolean pruefeStart(int[] pAufrufMaske, String pLogin) {
        return pruefeStart(pAufrufMaske, pLogin, true, false, true);
    }

    public void protokolliereDialogschritt(String pBeschreibung, String pLogin) {
        CaBug.druckeProt(pBeschreibung + " Mandant=" + eclParamM.getClGlobalVar().mandant + " Login=" + pLogin
                + " SessionId=" + tSession.getJsfSessionId());
    }

    /**
     * Wenn startPruefen==0, dann macht diese Funktion nix, gibt dann true zurück. Wenn pBeiLogout==true, dann
     * spezieller Ablauf für Logout-Button (insbesondere wird dann Start-Seite nicht überprüft)
     *
     * Setzt tSession-Fehler zurück.
     *
     * Verwendet: startPruefen == 0 => Funktion macht nix, gibt dann nur true zurück bereitsAufgerufen
     *
     * Setzt: bereitsAufgerufen
     * 
     * 
     * tSession.clearFehler.
     * Protokollausgabe "Seite Start".
     * Doppelclick ignorieren.
     * 
     * Prüfen, ob Session mittlerweile gesperrt (über Session-Verwaltung im Speicher, d.h. ohne
     * Zugriff auf Datenbank)
     *      Falls ja, dann Rückkehr zu LOGIN, und Fehlermeldung afUserAusgeloggtWgParallelLogin
     *      
     * Prüfen, ob Mandant ausgewählt
     *      Wenn nein, dann FEHLER_UNBEKANNT
     *      
     * Prüfen, ob aufrufende Maske die auch intern gespeicherte aktuelle Maske ist
     *      Wenn nein, dann FEHLER_DIALOG
     *
     *
     */
    public boolean pruefeStart(int[] pAufrufMaske, String pLogin, boolean pUserNochEingeloggtPruefen,
            boolean pBeiLogout, boolean pStartMaskePruefen) {
        if (startPruefen == 0) {
            return true;
        }

        tSession.clearFehler();
        /* Protokollausgabe */
        protokolliereDialogschritt(">>>>pruefeStart<<<< paufrufmaske=" + KonstPortalView.getText(pAufrufMaske[0])
                + " aktuelleMaske=" + KonstPortalView.getText(tSession.getViewnummer()), pLogin);

        /* Doppelklick abfangen - Seite bleibt unverändert, aber Verarbeitung wird abgebrochen */
        if (bereitsAufgerufen == 1) {
            protokolliereDialogschritt(">>>>DoppelKlick<<<<", pLogin);
            return false;
        }
        bereitsAufgerufen = 1;

        if (logDrucken > 2) {
            CaBug.druckeLog("tSession.getJsfSessionId()" + tSession.getJsfSessionId(), logDrucken, 2);
            CaBug.druckeLog("Inhalt der Hashmaps", logDrucken, 2);
        }

        if (pUserNochEingeloggtPruefen) {
            /* Prüfen, ob Session mittlerweile gesperrt */
            boolean bRc = tUserSessionManagement.pruefenObSessionMittlerweileGesperrt();
            if (bRc == true) {
                tSession.setViewnummer(KonstPortalView.LOGIN);
                protokolliereDialogschritt(">>>>Doppellogin<<<<"
                        + CaFehler.getFehlertext(CaFehler.afUserAusgeloggtWgParallelLogin, 0) + " (gleicher Server)",
                        pLogin);
                tLoginLogoutSession.clearAll();
                tSession.setUserEingeloggt("0");
                tLoginLogoutSession.trageFehlerEin(CaFehler.afUserAusgeloggtWgParallelLogin);
                return false;
            }
        }
        if (!pBeiLogout) {
            /* ++++++++++++++Prüfungen - technischer Art+++++++++ */
            if (eclParamM.mandantIstAusgewaehlt() == false) {
                CaBug.drucke("001: Mandant ist 0");
                setzeEnde(KonstPortalView.FEHLER_UNBEKANNT);
                return false;
            }

            /* Prüfen, ob die Maske zulässig ist */
            if (pStartMaskePruefen) {
                boolean richtigeMaske = false;
                for (int i = 0; i < pAufrufMaske.length; i++) {
                    if (pAufrufMaske[i] == tSession.getViewnummer()) {
                        richtigeMaske = true;
                    }
                }
                if (richtigeMaske == false) {
                    setzeEnde(KonstPortalView.FEHLER_DIALOG);
                    return false;
                }
            }
        }

        return true;
    }

    /* ++++++++++++++++Ende-Verarbeitung++++++++++++++++++++++++++++++++++++++++++++++++ */

    public void setzeEnde() {
        bereitsAufgerufen = 0;
        /*Die folgenden beiden Statements lesen sich als "Schwachsinnig". Ist es aber nicht, da beim setViewnummer
         * fehhlerseiteAnzeigen berücksichtigt wird
         */
        int viewnummer=tSession.getViewnummer();
        tSession.setViewnummer(viewnummer);
        return;
    }

    public void setzeEnde(int pNaechsteMaske) {
        setzeEnde(pNaechsteMaske, eclLoginDatenM.getAnmeldeKennungFuerAnzeige());
        return;
    }

    /**
     * Verwendet: startPruefen ==0 => macht nichts
     *
     * Verändert: bereitsAufgerufen
     */
    public void setzeEnde(int pNaechsteMaske, String pLogin) {
        if (startPruefen == 0) {
            return;
        }
        bereitsAufgerufen = 0;
        protokolliereDialogschritt(">>>>>setzeEnde<<<<< naechsteMaske=" + KonstPortalView.getText(pNaechsteMaske),
                pLogin);
        tSession.setViewnummer(pNaechsteMaske);
        tSeitensteuerung.belegeFuerSeite();
       
        return;
    }

    /********************* Time-Out-Verwaltung **************************************************/
    public boolean timeOutAufLang() {
        if (eclParamM.getParam().paramPortal.timeoutAufLang == 1 || (!eclParamM.getEclEmittent().hvDatum.isEmpty()
                && eclParamM.getEclEmittent().hvDatum.equals(CaDatumZeit.DatumStringFuerAnzeige()))) {
            return true;
        }

        return false;
    }

    public void setzeTimeOut() {
        if (timeOutAufLang()) {
            CaBug.druckeLog("TimeOutAufLang", logDrucken, 10);
            tJsfSession.setTimeoutLang();
        } else {
            CaBug.druckeLog("TimeOutAufNormal", logDrucken, 10);
            tJsfSession.setTimeoutNormal();
        }
    }

    /****************** Standard Setter und Getter **************************/

    public int getStartPruefen() {
        return startPruefen;
    }

    public void setStartPruefen(int startPruefen) {
        this.startPruefen = startPruefen;
    }

    synchronized public void setDatenbereich(String datenbereich) {
        CaBug.druckeLog("datenbereich=" + datenbereich, logDrucken, 2);
        this.datenbereich = datenbereich;
    }

    synchronized public String getDatenbereich() {
        return datenbereich;
    }

    synchronized public String getHvjahr() {
        return hvjahr;
    }

    synchronized public void setHvjahr(String hvjahr) {
        this.hvjahr = hvjahr;
    }

    synchronized public String getHvnummer() {
        return hvnummer;
    }

    synchronized public void setHvnummer(String hvnummer) {
        this.hvnummer = hvnummer;
    }

    public boolean isSchwererFehler() {
        return schwererFehler;
    }

    public void setSchwererFehler(boolean schwererFehler) {
        this.schwererFehler = schwererFehler;
    }

    public String getLinkArt() {
        return linkArt;
    }

    public void setLinkArt(String linkArt) {
        this.linkArt = linkArt;
    }

    public String getBestaetigungsCode() {
        return bestaetigungsCode;
    }

    public String getPermanentPortal() {
        return permanentPortal;
    }


}
