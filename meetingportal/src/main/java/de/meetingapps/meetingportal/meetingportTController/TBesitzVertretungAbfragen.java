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

import java.util.LinkedList;
import java.util.List;

import de.meetingapps.meetingportal.meetComAllg.CaBug;
import de.meetingapps.meetingportal.meetComAllg.CaFehler;
import de.meetingapps.meetingportal.meetComBl.BlWillenserklaerung;
import de.meetingapps.meetingportal.meetComEclM.EclBesitzGesamtM;
import de.meetingapps.meetingportal.meetComEclM.EclDbM;
import de.meetingapps.meetingportal.meetComEclM.EclParamM;
import de.meetingapps.meetingportal.meetComEntities.EclZugeordneteMeldungNeu;
import de.meetingapps.meetingportal.meetComKonst.KonstPortalFehlerView;
import de.meetingapps.meetingportal.meetComKonst.KonstPortalView;
import de.meetingapps.meetingportal.meetingportTFunktionen.TFunktionen;
import de.meetingapps.meetingportal.meetingportTFunktionen.TPruefeStartNachOpen;
import de.meetingapps.meetingportal.meetingportTFunktionen.TSessionVerwaltung;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@RequestScoped
@Named
public class TBesitzVertretungAbfragen {

    private int logDrucken = 3;

    private @Inject EclDbM eclDbM;

    private @Inject TSession tSession;
    private @Inject TSessionVerwaltung tSessionVerwaltung;
    private @Inject TWillenserklaerungSession tWillenserklaerungSession;
    private @Inject TAuswahl tAuswahl;
    private @Inject TFunktionen tFunktionen;
    private @Inject EclBesitzGesamtM eclBesitzGesamtM;
    private @Inject TFehlerViewSession tFehlerViewSession;
    private @Inject EclParamM eclParamM;
    private @Inject TPruefeStartNachOpen tPruefeStartNachOpen;

    /**Kein eclDbM erforderlich*/
    public void doInit() {
        /*Ausgewählt für alle Elemente auf true setzen - Standardmäßig sind alle ausgewählt*/
        List<EclZugeordneteMeldungNeu> meldungenBereitsErteiltWeisungAllgemeinDurchAndereListe = eclBesitzGesamtM.getMeldungenBereitsErteiltWeisungAllgemeinDurchAndereListe();
        if (meldungenBereitsErteiltWeisungAllgemeinDurchAndereListe == null) {
            CaBug.drucke("001");
            return;
        }
        for (EclZugeordneteMeldungNeu iZugeordneteMeldung : meldungenBereitsErteiltWeisungAllgemeinDurchAndereListe) {
            iZugeordneteMeldung.ausgewaehlt = true;
        }
    }

    public void doWiderrufenUndUebernehmen() {
        if (!tSessionVerwaltung.pruefeStart(KonstPortalView.BESITZ_VERTRETUNG_ABFRAGEN)) {
            return;
        }
        eclDbM.openAll();

        if (!tPruefeStartNachOpen.pruefeStartNachOpen(KonstPortalView.LOGIN)) {
            eclDbM.closeAll();
            return;
        }

        
        
        if (eclParamM.getParam().paramPortal.weisungenAktuellNichtMoeglich==1
                || eclParamM.getParam().paramPortal.weisungenAktuellNichtMoeglichAberBriefwahlSchon==1) {
            eclDbM.closeAll();
            tFehlerViewSession.setFehlerArt(KonstPortalFehlerView.AUSWERTUNG_LAEUFT_KEINE_WEISUNGSAENDERUNG_WEISUNG_DURCH_ANDERE_ERTEILT);
            tFehlerViewSession.setNextView(KonstPortalView.BESITZ_VERTRETUNG_ABFRAGEN);
            tSessionVerwaltung.setzeEnde(KonstPortalView.FEHLER_VIEW);
            return;
        }
                
        List<Integer> ausblendenMeldungen = eclBesitzGesamtM.getAusblendenMeldungen();
        if (ausblendenMeldungen == null) {
            ausblendenMeldungen = new LinkedList<Integer>();
        }

        int gef=0;
        for (EclZugeordneteMeldungNeu iZugeordneteMeldung : eclBesitzGesamtM.getMeldungenBereitsErteiltWeisungAllgemeinDurchAndereListe()) {
           /*Alle ausgewählten Stornieren*/
            if (iZugeordneteMeldung.ausgewaehlt == true) {
                /*Prüfen ob noch unverändert*/
                if (tFunktionen.reCheckKeineNeueWillenserklaerungen(eclDbM.getDbBundle(),
                        iZugeordneteMeldung.getMeldungsIdent(),
                        iZugeordneteMeldung.getIdentHoechsteWillenserklaerung()) == false) {
                    tFehlerViewSession.setFehlerArt(KonstPortalFehlerView.TRANSAKTIONSABBRUCH_ANDERER_USER_AKTIV);
                    tFehlerViewSession.setNextView(KonstPortalView.LOGIN);
                    tSessionVerwaltung.setzeEnde(KonstPortalView.FEHLER_VIEW);
                    eclDbM.closeAll();
                    return;
                }
                
                gef++;
                if (iZugeordneteMeldung.anzAlleBriefwahl > 0) {
                    CaBug.druckeLog("iZugeordneteMeldung.anzAlleBriefwahl=" + iZugeordneteMeldung.anzAlleBriefwahl, logDrucken, 10);
                    /*Willenserklärung stornieren*/
                    BlWillenserklaerung vmWillenserklaerung = new BlWillenserklaerung();
                    vmWillenserklaerung.pQuelle = tWillenserklaerungSession.getQuelle();
                    vmWillenserklaerung.pErteiltAufWeg = tWillenserklaerungSession.getEingabeQuelle();
                    vmWillenserklaerung.pErteiltZeitpunkt = tWillenserklaerungSession.getErteiltZeitpunkt();
                    vmWillenserklaerung.piMeldungsIdentAktionaer = iZugeordneteMeldung.meldungsIdent;
                    vmWillenserklaerung.pAufnehmendeSammelkarteIdent = iZugeordneteMeldung.eclMeldung.meldungEnthaltenInSammelkarte;
                    CaBug.druckeLog("vmWillenserklaerung.piMeldungsIdentAktionaer=" + vmWillenserklaerung.piMeldungsIdentAktionaer, logDrucken, 10);
                    vmWillenserklaerung.pWillenserklaerungGeberIdent = 0; /*Egal wer*/
                    vmWillenserklaerung.widerrufBriefwahl(eclDbM.getDbBundle());
                    if (vmWillenserklaerung.rcIstZulaessig == false) {
                        CaBug.drucke("001 vmWillenserklaerung.rcGrundFuerUnzulaessig=" + vmWillenserklaerung.rcGrundFuerUnzulaessig);
                    }
                }

                if (iZugeordneteMeldung.anzAlleSRV > 0) {
                    CaBug.druckeLog("iZugeordneteMeldung.anzAlleSRV=" + iZugeordneteMeldung.anzAlleSRV, logDrucken, 10);
                    /*Willenserklärung stornieren*/
                    BlWillenserklaerung vmWillenserklaerung = new BlWillenserklaerung();
                    vmWillenserklaerung.pQuelle = tWillenserklaerungSession.getQuelle();
                    vmWillenserklaerung.pErteiltAufWeg = tWillenserklaerungSession.getEingabeQuelle();
                    vmWillenserklaerung.pErteiltZeitpunkt = tWillenserklaerungSession.getErteiltZeitpunkt();
                    vmWillenserklaerung.piMeldungsIdentAktionaer = iZugeordneteMeldung.meldungsIdent;
                    vmWillenserklaerung.pAufnehmendeSammelkarteIdent = iZugeordneteMeldung.eclMeldung.meldungEnthaltenInSammelkarte;
                    CaBug.druckeLog("vmWillenserklaerung.piMeldungsIdentAktionaer=" + vmWillenserklaerung.piMeldungsIdentAktionaer, logDrucken, 10);
                    vmWillenserklaerung.pWillenserklaerungGeberIdent = 0; /*Egal wer*/
                    vmWillenserklaerung.widerrufVollmachtUndWeisungAnSRV(eclDbM.getDbBundle());
                    if (vmWillenserklaerung.rcIstZulaessig == false) {
                        CaBug.drucke("001 vmWillenserklaerung.rcGrundFuerUnzulaessig=" + vmWillenserklaerung.rcGrundFuerUnzulaessig);
                    }
                }

            }

            /*Alle nicht ausgewählten ausblenden*/
            if (iZugeordneteMeldung.ausgewaehlt == false && gef>0) {
                ausblendenMeldungen.add(iZugeordneteMeldung.meldungsIdent);
            }
        }
        if (gef==0) {
            tSession.trageFehlerEin(CaFehler.afBeiUebernahmeBestandZumWiderrufenAuswaehlen);
            eclDbM.closeAll();
            tSessionVerwaltung.setzeEnde();
            return;
        }
        
        eclBesitzGesamtM.setAusblendenMeldungen(ausblendenMeldungen);

        int rcAuswahl = tAuswahl.startAuswahl(true);
        eclDbM.closeAll();
        tSessionVerwaltung.setzeEnde(rcAuswahl);
        return;

    }

    public void doWeiterOhneUebernehmen() {
        if (!tSessionVerwaltung.pruefeStart(KonstPortalView.BESITZ_VERTRETUNG_ABFRAGEN)) {
            return;
        }
        eclDbM.openAll();
        /**Alle Elemente der Liste an die ausgeblendete Liste anhängen*/
        List<Integer> ausblendenMeldungen = eclBesitzGesamtM.getAusblendenMeldungen();
        if (ausblendenMeldungen == null) {
            ausblendenMeldungen = new LinkedList<Integer>();
        }
        for (EclZugeordneteMeldungNeu iZugeordneteMeldung : eclBesitzGesamtM.getMeldungenBereitsErteiltWeisungAllgemeinDurchAndereListe()) {
            ausblendenMeldungen.add(iZugeordneteMeldung.meldungsIdent);
        }
        eclBesitzGesamtM.setAusblendenMeldungen(ausblendenMeldungen);

        int rcAuswahl = tAuswahl.startAuswahl(true);
        eclDbM.closeAll();
        tSessionVerwaltung.setzeEnde(rcAuswahl);
        return;

    }

    public void doWeiter() {
        if (!tSessionVerwaltung.pruefeStart(KonstPortalView.WEISUNG_QUITTUNG)) {
            return;
        }
        eclDbM.openAll();
        tAuswahl.startAuswahl(true);
        eclDbM.closeAll();
        tSessionVerwaltung.setzeEnde(tFunktionen.waehleAuswahl());
        return;
    }

//    public void doEmail() {
//        if (!tSessionVerwaltung.pruefeStart(KonstPortalView.WEISUNG_QUITTUNG)) {
//            return;
//        }
//
//        String hMail = tWillenserklaerungSession.getEmailFuerBestaetigung();
//        if (hMail == null || hMail.isEmpty() || !CaString.isMailadresse(hMail)) {
//            tSession.trageFehlerEin(CaFehler.afEMailBestaetigungFalsch);
//            tSessionVerwaltung.setzeEnde();
//            return;
//        }
//
//        eclDbM.openAll();
//
//        erzeugePDF();
//
//        String hMailText = "", hBetreff = "";
//        if (tWillenserklaerungSession.getArt().contentEquals("1")) {
//            /*Briefwahl*/
//            hBetreff = eclPortalTexteM.holeText("927");
//            hMailText = eclPortalTexteM.holeText("929");
//        } else {
//            /*SRV*/
//            hBetreff = eclPortalTexteM.holeText("926");
//            hMailText = eclPortalTexteM.holeText("928");
//        }
//        baMailM.sendenMitAnhang(hMail, hBetreff, hMailText,
//                eclParamM.getClGlobalVar().lwPfadAllgemein + "\\meetingausdrucke\\" + eclParamM.getMandantPfad() + "\\bes" + tWillenserklaerungSession.getNummerBestaetigung() + ".pdf");
//
//        eclDbM.closeAll();
//        tSessionVerwaltung.setzeEnde();
//    }
//
//    public void doPdf() {
//        if (!tSessionVerwaltung.pruefeStart(KonstPortalView.WEISUNG_QUITTUNG)) {
//            return;
//        }
//        eclDbM.openAll();
//        erzeugePDF();
//        eclDbM.closeAll();
//
//        String dateiName = eclParamM.getClGlobalVar().lwPfadAllgemein + "\\meetingausdrucke\\" + eclParamM.getMandantPfad() + "\\bes" + tWillenserklaerungSession.getNummerBestaetigung() + ".pdf";
//
//        RpBrowserAnzeigen rpBrowserAnzeigen = new RpBrowserAnzeigen();
//        rpBrowserAnzeigen.zeigen(dateiName);
//
//        tSessionVerwaltung.setzeEnde();
//    }
//
//    private void erzeugePDF() {
//        RpDrucken rpDrucken = new RpDrucken();
//        rpDrucken.initServer();
//        rpDrucken.exportFormat = 8;
//        rpDrucken.exportDatei = "bes" + tWillenserklaerungSession.getNummerBestaetigung();
//        rpDrucken.initFormular(eclDbM.getDbBundle());
//
//        /*Variablen füllen - sowie Dokumentvorlage*/
//        RpVariablen rpVariablen = new RpVariablen(eclDbM.getDbBundle());
//        rpVariablen.weisungBestaetigung("01", rpDrucken);
//        rpDrucken.startFormular();
//
//        rpVariablen.fuelleVariable(rpDrucken, "Art", tWillenserklaerungSession.getArt());
//        rpVariablen.fuelleVariable(rpDrucken, "AusstellungsZeit", tWillenserklaerungSession.getAusstellungsZeit());
//        rpVariablen.fuelleVariable(rpDrucken, "NummerBestaetigung", tWillenserklaerungSession.getNummerBestaetigung());
//        rpVariablen.fuelleVariable(rpDrucken, "KennungVeranstaltung", tWillenserklaerungSession.getKennungVeranstaltung());
//        rpVariablen.fuelleVariable(rpDrucken, "ISIN", tWillenserklaerungSession.getIsin());
//        rpVariablen.fuelleVariable(rpDrucken, "Datum", tWillenserklaerungSession.getDatum());
//        rpVariablen.fuelleVariable(rpDrucken, "NameEmittent", tWillenserklaerungSession.getNameEmittent());
//        rpVariablen.fuelleVariable(rpDrucken, "NameBestaetigender", tWillenserklaerungSession.getNameBestaetigender());
//        rpVariablen.fuelleVariable(rpDrucken, "NameAbstimmender", tWillenserklaerungSession.getNameAbstimmender());
//
//        //Start printing
//        rpDrucken.druckenFormular();
//        rpDrucken.endeFormular();
//    }

}
