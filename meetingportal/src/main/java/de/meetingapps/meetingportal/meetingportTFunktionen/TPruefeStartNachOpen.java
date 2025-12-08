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

import de.meetingapps.meetingportal.meetComAllg.CaBug;
import de.meetingapps.meetingportal.meetComAllg.CaFehler;
import de.meetingapps.meetingportal.meetComBl.BlTeilnehmerLoginNeu;
import de.meetingapps.meetingportal.meetComEclM.EclAbstimmungSetM;
import de.meetingapps.meetingportal.meetComEclM.EclBesitzGesamtAuswahl1M;
import de.meetingapps.meetingportal.meetComEclM.EclDbM;
import de.meetingapps.meetingportal.meetComEclM.EclLoginDatenM;
import de.meetingapps.meetingportal.meetComEclM.EclParamM;
import de.meetingapps.meetingportal.meetComKonst.KonstPortalAktion;
import de.meetingapps.meetingportal.meetComKonst.KonstPortalFehlerView;
import de.meetingapps.meetingportal.meetComKonst.KonstPortalFunktionen;
import de.meetingapps.meetingportal.meetComKonst.KonstPortalView;
import de.meetingapps.meetingportal.meetComKonst.KonstWillenserklaerung;
import de.meetingapps.meetingportal.meetingportTController.TFehlerViewSession;
import de.meetingapps.meetingportal.meetingportTController.TLanguage;
import de.meetingapps.meetingportal.meetingportTController.TLoginLogoutSession;
import de.meetingapps.meetingportal.meetingportTController.TPraesenzZugangAbgang;
import de.meetingapps.meetingportal.meetingportTController.TSession;
import de.meetingapps.meetingportal.meetingportTController.TWillenserklaerungSession;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@RequestScoped
@Named
public class TPruefeStartNachOpen {

    private int logDrucken = 3;

    @Inject
    private EclParamM eclParamM;
    @Inject
    private EclDbM eclDbM;

    private @Inject TSession tSession;
    @Inject
    private TWillenserklaerungSession tWillenserklaerungSession;
    private @Inject TLoginLogoutSession tLoginLogoutSession;
    @Inject
    private TLanguage tLanguage;

    
    private @Inject EclLoginDatenM eclLoginDatenM;
    @Inject
    private TFunktionen tFunktionen;
    @Inject
    private TRender tRender;
    @Inject
    private TPortalFunktionen tPortalFunktionen;
    
    private @Inject TSessionVerwaltung tSessionVerwaltung;
    @Inject
    private TUserSessionManagement tUserSessionManagement;
    @Inject
    private TFehlerViewSession tFehlerViewSession;
    @Inject
    private TPraesenzZugangAbgang tPraesenzZugangAbgang;
    @Inject
    private EclBesitzGesamtAuswahl1M eclBesitzGesamtAuswahl1M;
    @Inject
    private EclAbstimmungSetM eclAbstimmungSetM;

    /**
     * Voraussetzung:
     * > eclDbM.openall ist erfolgt
     * 
     * Parameter:
     * > pPortalFunktion = KonstPortalFunktionen.
     * > fortsetzenBeiNichtAktiv:
     *      Betrifft nur Situation, wenn Funktion aktuell nicht aktiv ist. Bei "nicht mehr berechtigt",
     *      oder HV-Portal nicht mehr aktiv, erfolgt nie eine Fortsetzung. 
     *      true -> normale Fortsetzung, entsprechende Anzeigen erfolgen sowieso auf der Folgeseite
     *      false -> Fehlerseite je nach Situation (siehe Schritt 2)
     * 
     * Ablauf:
     * =======
     * Schritt 1:
     * ----------
     * Prüfen, ob Portal-Login aktuell grundsätzlich gesperrt
     *      Wenn ja: 
     *      > zu Seite LOGIN
     *      > Fehlertext fest vorbelegen mit "Wartungstext" aus Parametern
     *      > Returnwert = -1
     *      
     * Prüfen aktuelle Kennung
     *      Gesperrt wg. Doppellogin? Dann LOGIN, mit Fehlertext afUserAusgeloggtWgParallelLogin
     *      Gesperrt wg. fehlender Portalberechtigung? Dann LOGIN, mit Fehlertext afBerechtigungFuerAktionaersportalFehlt
     *      Bestand verändert? Dann 
     *          > ggf. Abgang buchen, 
     *          > leseStatusPortal
     *          > Prüfung normal fortsetzen (bei Portalfunktionen wird ja Bestand nicht verändert, sondern nur
     *              zur Überprüfung herangezogen)
 *          Returnwert=-1;
     *
     * Schritt 2:
     * ----------
     * Vorbereiten "Verfügbarkeitsprüfung" Portalfunktionen und Willenserklärung.
     * 
     * Überprüfen, ob HV-Portal überhaupt noch in Betrieb (nicht, wenn pPortalFunktion auch außerhalb
     * HV-Portal verfügbar)
     *      Wenn nein: fehlerView mit HVPORTAL_GESCHLOSSEN__ABBRUCH
     *      
     * Überprüfen, ob Funktion aktiv und berechtigt (nicht, wenn pPortalFunktion auch außerhalb
     * HV-Portal verfügbar). tAuswahlSession ist anschließend in jedem Fall für pPortalFunktion
     * richtig gefüllt.
     *      wenn aktiv, aber nicht berechtigt: fehlerView mit KEIN_BESTAND_MEHR_FUER_FUNKTION__ABBRUCH
    *       Wenn nein: 
     *      wenn fortsetzenBeiNichtVerfuegbarkeit==true: 
     *          wenn nicht aktiv: einfach fortsetzen (denn dann kommt innerhalb
     *          der Funktion eine entsprechende Abfrage),
     *          außer es ist eine Funktion bei der dann eine Fehlermeldung kommt (
     *          in diesem Fall Fehlermeldung gesetzt, und return false)
     *      wenn ==false: fehlerView mit FUNKTION_NICHT_MEHR_VERFUEGBAR__ABBRUCH
     */
    public boolean pruefeNachOpenPortalFunktion(int pPortalFunktion, boolean fortsetzenBeiNichtVerfuegbarkeit) {
        boolean brc=false;

        /*Schritt 1*/
        brc=pruefeStartNachOpen(eclLoginDatenM.getAnmeldeKennungFuerAnzeige(), true, 0);
        if (brc==false) {return false;}

        /*Schritt 2*/
        tPortalFunktionen.bereiteVorPruefungEinzelnePortalFunktion(pPortalFunktion);
        if (KonstPortalFunktionen.benoetigtHVPortal(pPortalFunktion)) {
            if (tPortalFunktionen.getAktuellePortalPhase().lfdHVPortalInBetrieb==false) {
                tFehlerViewSession.setFehlerArt(KonstPortalFehlerView.HVPORTAL_GESCHLOSSEN__ABBRUCH);
                tFehlerViewSession.setNextView(liefereAuswahlView());
                tSessionVerwaltung.setzeEnde(KonstPortalView.FEHLER_VIEW);
                return false;
            }
        }
        if (KonstPortalFunktionen.aktivPruefungMoeglich(pPortalFunktion)) {
            if (tPortalFunktionen.aktivEinzelnePortalFunktion(pPortalFunktion)==false) {
                /*Portalfunktion steht nicht mehr zur Verfügung*/
                if (tPortalFunktionen.aktivUBEinzelnePortalFunktion(pPortalFunktion)) {
                    /*Aktiv, aber offensichtlich nicht mehr berechtigt => immer abbruch*/
                    tFehlerViewSession.setFehlerArt(KonstPortalFehlerView.KEIN_BESTAND_MEHR_FUER_FUNKTION__ABBRUCH);
                    tFehlerViewSession.setNextView(liefereAuswahlView());
                    tSessionVerwaltung.setzeEnde(KonstPortalView.FEHLER_VIEW);
                    return false;
                }
                /*Nun nicht mehr aktiv*/
                if (fortsetzenBeiNichtVerfuegbarkeit==false) {
                    tFehlerViewSession.setFehlerArt(KonstPortalFehlerView.FUNKTION_NICHT_MEHR_VERFUEGBAR__ABBRUCH);
                    tFehlerViewSession.setNextView(liefereAuswahlView());
                    tSessionVerwaltung.setzeEnde(KonstPortalView.FEHLER_VIEW);
                    return false;
                }
                else {
                    /*Bei diesen Funktionen eine Fehlermeldung bringen, 
                     * wenn ausgewählt und nicht aktiv
                     */
                    if (pPortalFunktion==KonstPortalFunktionen.monitoring ||
                            pPortalFunktion==KonstPortalFunktionen.unterlagen   
                            ) {
                        tSession.trageFehlerEin(CaFehler.afFunktionNichtAuswaehlbar);
                        tSessionVerwaltung.setzeEnde();
                        return false;
                    }
                }
            }
        }
        else {
            if (pPortalFunktion==KonstPortalFunktionen.einstellungen) {
                if (tRender.auswahlEinstellungen() == false) {
                    tSession.trageFehlerEin(CaFehler.afFunktionNichtAuswaehlbar);
                    tSessionVerwaltung.setzeEnde();
                    return false;
                }
               
            }
            if (pPortalFunktion==KonstPortalFunktionen.gaeste) {
                if (eclParamM.liefereGastkartenButtonAnzeigen()==false) {
                    tSession.trageFehlerEin(CaFehler.afFunktionNichtAuswaehlbar);
                    tSessionVerwaltung.setzeEnde();
                    return false;
                }
               
            }
        }
        
        return true;
    }

    
    /**
     * Voraussetzung:
     * > eclDbM.openall ist erfolgt
     * 
     * 
     * Ablauf:
     * =======
     * Schritt 1:
     * ----------
     * Prüfen, ob Portal-Login aktuell grundsätzlich gesperrt
     *      Wenn ja: 
     *      > zu Seite LOGIN
     *      > Fehlertext fest vorbelegen mit "Wartungstext" aus Parametern
     *      > Returnwert = -1
     *      
     * Prüfen aktuelle Kennung
     *      Gesperrt wg. Doppellogin? Dann LOGIN, mit Fehlertext afUserAusgeloggtWgParallelLogin, Return false;
     *      Gesperrt wg. fehlender Portalberechtigung? Dann LOGIN, mit Fehlertext afBerechtigungFuerAktionaersportalFehlt; return false;
     *      Bestand verändert? Dann 
     *          > ggf. Abgang buchen, 
     *          > leseStatusPortal
     *          > Prüfung normal fortsetzen (bei Portalfunktionen wird ja Bestand nicht verändert, sondern nur
     *              zur Überprüfung herangezogen)
     *
     * Schritt 2:
     * ----------
     * Vorbereiten "Verfügbarkeitsprüfung" Willenserklärung.
     * 
     * Überprüfen, ob HV-Portal überhaupt noch in Betrieb
     *      Wenn nein: fehlerView mit HVPORTAL_GESCHLOSSEN__ABBRUCH
     * 
     * Falls Hauptaktion Neuanmelden, dann überprüfen ob Anmeldung noch zulässig.
     *      wenn nein: fehlerview mit 
     * Überprüfen, ob Willenserklärung noch möglich (aktiv) und angeboten. 
     * tAuswahlSession ist anschließend in jedem Fall für pPortalFunktion
     * richtig gefüllt.
     *      wenn ==false: fehlerView mit WILLENSERKLARUNG_NICHT_MEHR_MOEGLICH__ABBRUCH
     */

    public boolean pruefeNachOpenPortalAktuelleHauptAktionAktion() {
        return pruefeNachOpenPortalHauptAktionAktion(tWillenserklaerungSession.getIntAusgewaehlteHauptAktion(), 
                tWillenserklaerungSession.getIntAusgewaehlteAktion());
        
    }
    
    public boolean pruefeNachOpenPortalWillenserklaerung(int pWillenserklaerung, int pStornoOderAendern) {
        int lAktion=0;
        //@formatter:off
        switch (pWillenserklaerung) {
        /*Bei diesen WK keine Änderung möglich*/
        case KonstWillenserklaerung.neueZutrittsIdentZuMeldung:
            lAktion=KonstPortalAktion.EK_STORNIEREN;
            break;
        case KonstWillenserklaerung.neueZutrittsIdentZuMeldung_VollmachtAnDritte:
            lAktion=KonstPortalAktion.EK_MIT_VOLLMACHT_STORNIEREN;
            break;
        case KonstWillenserklaerung.vollmachtAnDritte:
            lAktion=KonstPortalAktion.VOLLMACHT_DRITTE_STORNIEREN;
            break;
            
        case KonstWillenserklaerung.aendernWeisungAnSRV:
        case KonstWillenserklaerung.vollmachtUndWeisungAnSRV:
            if (pStornoOderAendern==1) {lAktion=KonstPortalAktion.SRV_STORNIEREN;}
            else {lAktion=KonstPortalAktion.SRV_AENDERN;}
            break;
        case KonstWillenserklaerung.briefwahl:
        case KonstWillenserklaerung.aendernBriefwahl:
            if (pStornoOderAendern==1) {lAktion=KonstPortalAktion.BRIEFWAHL_STORNIEREN;}
            else {lAktion=KonstPortalAktion.BRIEFWAHL_AENDERN;}
            break;
        case KonstWillenserklaerung.vollmachtUndWeisungAnKIAV:
        case KonstWillenserklaerung.aendernWeisungAnKIAV:
            if (pStornoOderAendern==1) {lAktion=KonstPortalAktion.KIAV_STORNIEREN;}
            else {lAktion=KonstPortalAktion.KIAV_WEISUNG_AENDERN;}
            break;
        case KonstWillenserklaerung.dauervollmachtAnKIAV:
            if (pStornoOderAendern==1) {lAktion=KonstPortalAktion.DAUERVOLLMACHT_STORNIEREN;}
            else {lAktion=KonstPortalAktion.DAUERVOLLMACHT_AENDERN;}
            break;
        case KonstWillenserklaerung.organisatorischMitWeisungInSammelkarte:
        case KonstWillenserklaerung.aendernWeisungOrganisatorischInSammelkarte:
            if (pStornoOderAendern==1) {lAktion=KonstPortalAktion.ORGANISATORISCH_STORNIEREN;}
            else {lAktion=KonstPortalAktion.ORGANISATORISCH_AENDERN;}
            break;
       }
        //@formatter:on
        if (lAktion==0) {
            CaBug.drucke("001");
            return false;
        }
        else {
            return pruefeNachOpenPortalHauptAktionAktion(0, lAktion);
        }
    }
    
    public boolean pruefeNachOpenPortalHauptAktionAktion(int pHauptaktion, int pAktion) {

        boolean brc=false;

        /*Schritt 1*/
        brc=pruefeStartNachOpen(eclLoginDatenM.getAnmeldeKennungFuerAnzeige(), true, liefereAuswahlView());
        if (brc==false) {return false;}

        /*Schritt 2*/
        tPortalFunktionen.belegeAktivFuerWillenserklaerungen();
        if (tPortalFunktionen.getAktuellePortalPhase().lfdHVPortalInBetrieb==false) {
            tFehlerViewSession.setFehlerArt(KonstPortalFehlerView.HVPORTAL_GESCHLOSSEN__ABBRUCH);
            tFehlerViewSession.setNextView(liefereAuswahlView());
            tSessionVerwaltung.setzeEnde(KonstPortalView.FEHLER_VIEW);
            return false;
        }

        if (pHauptaktion==KonstPortalAktion.HAUPT_NEUANMELDUNG && !tSession.isPortalErstanmeldungIstMoeglich()) {
            /*Anmeldung nicht mehr möglich*/
            tFehlerViewSession.setFehlerArt(KonstPortalFehlerView.ANMELDEFRIST_ABGELAUFEN);
            tFehlerViewSession.setNextView(liefereAuswahlView());
            tSessionVerwaltung.setzeEnde(KonstPortalView.FEHLER_VIEW);
            return false;
        }
        //@formatter:off
        boolean nichtMehrVerfuegbar=false;
        boolean tagesordnungIstVeraendert=false;
        boolean weisungenAktuellNichtMoeglich=false;
        
        switch (pAktion) {
        case KonstPortalAktion.NUR_ANMELDUNG_OHNE_WEITERE_WILLENSERKLAERUNG:
            break;
        case KonstPortalAktion.EINE_EK_SELBST:
            if (!tSession.isPortalEKIstMoeglich()) {nichtMehrVerfuegbar=true;}
            if (eclParamM.getParam().paramPortal.ekSelbstMoeglich==0) {nichtMehrVerfuegbar=true;};
            break;
        case KonstPortalAktion.ZWEI_EK_SELBST_PERSGEMEINSCHAFT:
            if (!tSession.isPortalEKIstMoeglich()) {nichtMehrVerfuegbar=true;}
            if (eclParamM.getParam().paramPortal.ek2PersonengemeinschaftMoeglich==0) {nichtMehrVerfuegbar=true;};
            break;
        case KonstPortalAktion.EINE_EK_MIT_VOLLMACHT:
            if (!tSession.isPortalEKIstMoeglich()) {nichtMehrVerfuegbar=true;}
            if (eclParamM.getParam().paramPortal.ekVollmachtMoeglich==0) {nichtMehrVerfuegbar=true;};
           break;
        case KonstPortalAktion.ZWEI_EK_SELBST_ODER_VOLLMACHT:
            if (!tSession.isPortalEKIstMoeglich()) {nichtMehrVerfuegbar=true;}
            if (eclParamM.getParam().paramPortal.ek2MitOderOhneVollmachtMoeglich==0) {nichtMehrVerfuegbar=true;};
            break;
        case KonstPortalAktion.ZWEI_EK_SELBST_FUER_ALLE:
            if (!tSession.isPortalEKIstMoeglich()) {nichtMehrVerfuegbar=true;}
            if (eclParamM.getParam().paramPortal.ek2SelbstMoeglich==0) {nichtMehrVerfuegbar=true;};
            break;
        case KonstPortalAktion.EK_STORNIEREN:
        case KonstPortalAktion.EK_MIT_VOLLMACHT_STORNIEREN:
            if (!tSession.isPortalEKIstMoeglich()) {nichtMehrVerfuegbar=true;}
            break;
        case KonstPortalAktion.VOLLMACHT_DRITTE_STORNIEREN:
        case KonstPortalAktion.VOLLMACHT_DRITTE:
            if (!tSession.isPortalVollmachtDritteIstMoeglich()) {nichtMehrVerfuegbar=true;}
            break;
        case KonstPortalAktion.SRV_NEU:
        case KonstPortalAktion.SRV_AENDERN:
        case KonstPortalAktion.SRV_STORNIEREN:
            if (!tSession.isPortalSRVIstMoeglich()) {nichtMehrVerfuegbar=true;}
            if (eclParamM.getParam().paramPortal.srvAngeboten==0) {nichtMehrVerfuegbar=true;};
            CaBug.druckeLog("****************************************************************eclAbstimmungSetM.getVersionWeisungenAktuell()="+eclAbstimmungSetM.getVersionWeisungenAktuell()+" eclAbstimmungSetM.getVersionWeisungenStart()="+eclAbstimmungSetM.getVersionWeisungenStart(), logDrucken, 10);
            if (eclAbstimmungSetM.getVersionWeisungenAktuell() != eclAbstimmungSetM.getVersionWeisungenStart()) {tagesordnungIstVeraendert=true;}
            if (eclParamM.getParam().paramPortal.weisungenAktuellNichtMoeglich==1 || eclParamM.getParam().paramPortal.weisungenAktuellNichtMoeglichAberBriefwahlSchon==1) {weisungenAktuellNichtMoeglich=true;}
           break;
        case KonstPortalAktion.BRIEFWAHL_NEU:
        case KonstPortalAktion.BRIEFWAHL_AENDERN:
        case KonstPortalAktion.BRIEFWAHL_STORNIEREN:
            if (!tSession.isPortalBriefwahlIstMoeglich()) {nichtMehrVerfuegbar=true;}
            if (eclParamM.getParam().paramPortal.briefwahlAngeboten==0) {nichtMehrVerfuegbar=true;};
            if (eclAbstimmungSetM.getVersionWeisungenAktuell() != eclAbstimmungSetM.getVersionWeisungenStart()) {tagesordnungIstVeraendert=true;}
            if (eclParamM.getParam().paramPortal.weisungenAktuellNichtMoeglich==1) {weisungenAktuellNichtMoeglich=true;}
            break;
        case KonstPortalAktion.KIAV_MIT_WEISUNG_NEU:
        case KonstPortalAktion.KIAV_NUR_VOLLMACHT_NEU:
        case KonstPortalAktion.KIAV_VOLLMACHT_UND_WEISUNG_DEDIZIERT_NEU:
        case KonstPortalAktion.KIAV_VOLLMACHT_UND_WEISUNG_GEMAESS_VORSCHLAG_NEU:
        case KonstPortalAktion.KIAV_WEISUNG_AENDERN:
        case KonstPortalAktion.KIAV_STORNIEREN:
            if (!tSession.isPortalKIAVIstMoeglich()) {nichtMehrVerfuegbar=true;}
            if (eclParamM.getParam().paramPortal.vollmachtKIAVAngeboten==0) {nichtMehrVerfuegbar=true;};
            if (eclAbstimmungSetM.getVersionWeisungenAktuell() != eclAbstimmungSetM.getVersionWeisungenStart()) {tagesordnungIstVeraendert=true;}
            if (eclParamM.getParam().paramPortal.weisungenAktuellNichtMoeglich==1 || eclParamM.getParam().paramPortal.weisungenAktuellNichtMoeglichAberBriefwahlSchon==1) {weisungenAktuellNichtMoeglich=true;}
           break;
        case KonstPortalAktion.DAUERVOLLMACHT_MIT_WEISUNG_NEU:
        case KonstPortalAktion.DAUERVOLLMACHT_NUR_VOLLMACHT_NEU:
        case KonstPortalAktion.DAUERVOLLMACHT_VOLLMACHT_UND_WEISUNG_DEDIZIERT_NEU:
        case KonstPortalAktion.DAUERVOLLMACHT_VOLLMACHT_UND_WEISUNG_GEMAESS_VORSCHLAG_NEU:
        case KonstPortalAktion.ORGANISATORISCH_MIT_WEISUNG_NEU:
        case KonstPortalAktion.ORGANISATORISCH_NUR_VOLLMACHT_NEU:
        case KonstPortalAktion.ORGANISATORISCH_VOLLMACHT_UND_WEISUNG_DEDIZIERT_NEU:
        case KonstPortalAktion.ORGANISATORISCH_VOLLMACHT_UND_WEISUNG_GEMAESS_VORSCHLAG_NEU:
        case KonstPortalAktion.DAUERVOLLMACHT_AENDERN:
        case KonstPortalAktion.ORGANISATORISCH_AENDERN:
        case KonstPortalAktion.DAUERVOLLMACHT_STORNIEREN:
        case KonstPortalAktion.ORGANISATORISCH_STORNIEREN:
            if (!tSession.isPortalKIAVIstMoeglich()) {nichtMehrVerfuegbar=true;}
            if (eclAbstimmungSetM.getVersionWeisungenAktuell() != eclAbstimmungSetM.getVersionWeisungenStart()) {tagesordnungIstVeraendert=true;}
            if (eclParamM.getParam().paramPortal.weisungenAktuellNichtMoeglich==1 || eclParamM.getParam().paramPortal.weisungenAktuellNichtMoeglichAberBriefwahlSchon==1) {weisungenAktuellNichtMoeglich=true;}
          break;
        default:/*Für alle Funktionen, die hier übersehen wurden ...*/ 
            CaBug.drucke("001 pAktion="+pAktion);
           nichtMehrVerfuegbar=true;
            break;
        }
        //@formatter:on

        if (nichtMehrVerfuegbar) {
            tFehlerViewSession.setFehlerArt(KonstPortalFehlerView.WILLENSERKLARUNG_NICHT_MEHR_MOEGLICH__ABBRUCH);
            tFehlerViewSession.setNextView(liefereAuswahlView());
            tSessionVerwaltung.setzeEnde(KonstPortalView.FEHLER_VIEW);
            return false;
       }
        if (tagesordnungIstVeraendert) {
            tFehlerViewSession.setFehlerArt(KonstPortalFehlerView.STIMMZAEHLUNG_VERAENDERT__NEUSTART);
            tFehlerViewSession.setNextView(liefereAuswahlView());
            tSessionVerwaltung.setzeEnde(KonstPortalView.FEHLER_VIEW);
            return false;
       }
        if (weisungenAktuellNichtMoeglich) {
            tFehlerViewSession.setFehlerArt(KonstPortalFehlerView.AUSWERTUNG_LAEUFT_KEINE_WEISUNGSAENDERUNG);
            tFehlerViewSession.setNextView(liefereAuswahlView());
            tSessionVerwaltung.setzeEnde(KonstPortalView.FEHLER_VIEW);
            return false;
       }
         
       return true;
    }

 
    
    
    public int liefereAuswahlView() {
        if (eclParamM.getParam().paramPortal.varianteDialogablauf==0) {
            return KonstPortalView.AUSWAHL;
        }
        else {
            return KonstPortalView.AUSWAHL1;
        }
    }
    /*++++++++++++++++Prüfen bei Start wenn Open erfolgt+++++++++++++++++++++++++++++++*/
    public boolean pruefeStartNachOpen(int pFortsetzungsViewBeiVeraendertemBesitz) {
        return pruefeStartNachOpen(eclLoginDatenM.getAnmeldeKennungFuerAnzeige(), true, pFortsetzungsViewBeiVeraendertemBesitz);
    }

    public boolean pruefeStartNachOpen() {
        return pruefeStartNachOpen(eclLoginDatenM.getAnmeldeKennungFuerAnzeige(), true, 0);
    }

    public boolean pruefeStartNachOpenOhneUser() {
        return pruefeStartNachOpen(eclLoginDatenM.getAnmeldeKennungFuerAnzeige(), false, 0);
    }

    /**pFortsetzungsViewBeiVeraendertemBesitz=0 => Besitz wird bei Veränderung neu eingelesen und dann normal weitergemacht.
     * Ansonsten: ZwischenView ("Bestand hat sich geändert -> Vorgang neu starten"), und dann pFortsetzungsViewBeiVeraendertemBesitz anzeigen
     */
    public boolean pruefeStartNachOpen(String pLogin, boolean pUserPruefen, int pFortsetzungsViewBeiVeraendertemBesitz) {
        CaBug.druckeLog("Start", logDrucken, 10);
        /*Portal gesperrt?*/
        if (eclParamM.getParam().paramPortal.loginGesperrt == 1 && tSession.isOrPortalsperre()==false) {
            tLoginLogoutSession.clearAll();
            if (tLanguage.getLang() == 1) {
                tLoginLogoutSession.trageFehlerEinMitArt(eclParamM.getParam().paramPortal.loginGesperrtTextDeutsch, 1);
            } else {
                tLoginLogoutSession.trageFehlerEinMitArt(eclParamM.getParam().paramPortal.loginGesperrtTextEnglisch, 1);
            }
            if (tSession.isPermanentPortal()==false) {
                tSession.setViewnummer(KonstPortalView.LOGIN);
            }
            else {
                tSession.setViewnummer(KonstPortalView.P_LOGIN);
            }
            tSessionVerwaltung.protokolliereDialogschritt("Portal gesperrt", pLogin);
            return false;
        }

        /*Alle Portale gesperrt*/
        if (eclParamM.getParamServer().loginGesperrt == 1 && tSession.isOrPortalsperre()==false) {
            tLoginLogoutSession.clearAll();
            if (tLanguage.getLang() == 1) {
                tLoginLogoutSession.trageFehlerEinMitArt(eclParamM.getParamServer().loginGesperrtTextDeutsch, 1);
            } else {
                tLoginLogoutSession.trageFehlerEinMitArt(eclParamM.getParamServer().loginGesperrtTextEnglisch, 1);
            }
            if (tSession.isPermanentPortal()==false) {
                tSession.setViewnummer(KonstPortalView.LOGIN);
            }
            else {
                tSession.setViewnummer(KonstPortalView.P_LOGIN);
            }
            tSessionVerwaltung.protokolliereDialogschritt("Portal gesperrt", pLogin);
            return false;
        }

        if (pUserPruefen) {
            CaBug.druckeLog("User prüfen", logDrucken, 10);

            boolean pruefenDoppeltLogin = (eclParamM.getParam().paramPortal.doppelLoginGesperrt == 1);

            /*Server-Nummer identisch, und Benutzer noch zugelassen für Portal?*/
            BlTeilnehmerLoginNeu blTeilnehmerLogin = new BlTeilnehmerLoginNeu();
            blTeilnehmerLogin.initDB(eclDbM.getDbBundle());
            int erg = blTeilnehmerLogin.pruefeRefresh(eclLoginDatenM.getEclLoginDaten().ident, eclParamM.getBmServernummer(), pruefenDoppeltLogin);
            CaBug.druckeLog("erg=" + erg, logDrucken, 10);
            if (erg < 0) {
                if (erg == CaFehler.afBesitzReloadDurchfuehren) {
                    tFunktionen.leseStatusPortal(eclDbM.getDbBundle());
                    if (pFortsetzungsViewBeiVeraendertemBesitz != 0) {

                        /*Abgang durchführen, falls präsente vorhanden*/
                        tPraesenzZugangAbgang.praesenzAbgang();
                        if (eclBesitzGesamtAuswahl1M.isGastPraesent()) {
                            tPraesenzZugangAbgang.abgangGastBuchen();
                        }

                        /*Nun Fehler-Seite aufrufen*/
                        tFehlerViewSession.setFehlerArt(1);//Veränderter Besitz
                        tFehlerViewSession.setNextView(pFortsetzungsViewBeiVeraendertemBesitz);
                        tSessionVerwaltung.setzeEnde(KonstPortalView.FEHLER_VIEW);
                        return false;
                    }
                } else {
                    /* afBerechtigungFuerAktionaersportalFehlt
                     * afUserAusgeloggtWgParallelLogin
                     */
                   String protokollText="";
                    switch (erg) {
                    case CaFehler.afBerechtigungFuerAktionaersportalFehlt:protokollText="Berechtigung für Portal entzogen";break;
                    case CaFehler.afUserAusgeloggtWgParallelLogin:protokollText="Doppellogin";break;
                    default:protokollText="Unbekannter Fehler";break;
                    }
                    tSession.setViewnummer(KonstPortalView.LOGIN);
                    tSessionVerwaltung.protokolliereDialogschritt(">>>>"+protokollText+"<<<<" + CaFehler.getFehlertext(erg, 0) + " (anderer Server)", pLogin);
                    tSession.setUserEingeloggt("0");
                    tLoginLogoutSession.clearAll();
                    tLoginLogoutSession.trageFehlerEin(erg);
                    return false;
                }
            }

            /*Gespeicherter Server ist hier definitiv gleich - nun schauen ob noch Session gleich*/
            /*Derzeit inaktiv - wenn bei diesem Server rausgeflogen, dann wurde das schon beim ersten Schritt gemerkt*/
            boolean bRc = tUserSessionManagement.pruefenObSessionNochGleich();
            if (bRc == false) {
                tSession.setViewnummer(KonstPortalView.LOGIN);
                tSessionVerwaltung.protokolliereDialogschritt(">>>>Doppellogin<<<<" + CaFehler.getFehlertext(CaFehler.afUserAusgeloggtWgParallelLogin, 0) + " (gleicher Server)", pLogin);
                tSession.setUserEingeloggt("0");
                tLoginLogoutSession.clearAll();
                tLoginLogoutSession.trageFehlerEin(CaFehler.afUserAusgeloggtWgParallelLogin);
                return false;
            }
        }

        return true;
    }

    /******************Standard Setter und Getter**************************/

}
