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

import java.util.List;

import de.meetingapps.meetingportal.meetComAllg.CaBug;
import de.meetingapps.meetingportal.meetComAllg.CaDatumZeit;
import de.meetingapps.meetingportal.meetComAllg.CaFehler;
import de.meetingapps.meetingportal.meetComBl.BlPraesenzlistenNummer;
import de.meetingapps.meetingportal.meetComBl.BlWillenserklaerungStatusNeuVerarbeiten;
import de.meetingapps.meetingportal.meetComBlManaged.BlMPersonenAbfrage;
import de.meetingapps.meetingportal.meetComEclM.EclBesitzGesamtAuswahl1M;
import de.meetingapps.meetingportal.meetComEclM.EclBesitzGesamtM;
import de.meetingapps.meetingportal.meetComEclM.EclDbM;
import de.meetingapps.meetingportal.meetComEclM.EclLoginDatenM;
import de.meetingapps.meetingportal.meetComEclM.EclParamM;
import de.meetingapps.meetingportal.meetComEntities.EclAktienregisterErgaenzung;
import de.meetingapps.meetingportal.meetComEntities.EclBesitzAREintrag;
import de.meetingapps.meetingportal.meetComEntities.EclBesitzJeKennung;
import de.meetingapps.meetingportal.meetComEntities.EclLoginDaten;
import de.meetingapps.meetingportal.meetComEntities.EclMeldungVirtuellePraesenz;
import de.meetingapps.meetingportal.meetComEntities.EclWillenserklaerung;
import de.meetingapps.meetingportal.meetComEntities.EclWillenserklaerungZusatz;
import de.meetingapps.meetingportal.meetComEntities.EclZugeordneteMeldungNeu;
import de.meetingapps.meetingportal.meetComHVParam.ParamSpezial;
import de.meetingapps.meetingportal.meetComKonst.KonstPortalView;
import de.meetingapps.meetingportal.meetComKonst.KonstWillenserklaerung;
import de.meetingapps.meetingportal.meetComKonst.KonstWillenserklaerungWeg;
import de.meetingapps.meetingportal.meetingportTFunktionen.TFunktionen;
import de.meetingapps.meetingportal.meetingportTFunktionen.TPortalFunktionen;
import de.meetingapps.meetingportal.meetingportTFunktionen.TPruefeStartNachOpen;
import de.meetingapps.meetingportal.meetingportTFunktionen.TRender;
import de.meetingapps.meetingportal.meetingportTFunktionen.TSessionVerwaltung;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@RequestScoped
@Named
public class TPraesenzZugangAbgang {

    private int logDrucken = 3;

    private @Inject TSessionVerwaltung tSessionVerwaltung;
    private @Inject EclDbM eclDbM;

    private @Inject TRender tRender;
    private @Inject BlMPersonenAbfrage blMPersonenAbfrage;

    private @Inject TSession tSession;
    private @Inject TPraesenzZugangAbgangSession tPraesenzZugangAbgangSession;
    private @Inject EclBesitzGesamtM eclBesitzGesamtM;
    private @Inject TFunktionen tFunktionen;
    private @Inject TAuswahl1Teilnahme tAuswahl1Teilnahme;
    private @Inject EclLoginDatenM eclLoginDatenM;
    private @Inject EclParamM eclParamM;
    private @Inject EclBesitzGesamtAuswahl1M eclBesitzGesamtAuswahl1M;
    private @Inject TPruefeStartNachOpen tPruefeStartNachOpen;
    private @Inject TPortalFunktionen tPortalFunktionen;
    private @Inject TAuswahlSession tAuswahlSession;
    private @Inject TFehlerViewSession tFehlerViewSession;

    @Deprecated
    public int initZugang() {

        /*Funktion vorhanden?*/
        if (tRender.auswahlTeilnahmeZugang() == false) {
            return CaFehler.afFunktionNichtAuswaehlbar;
        }

        blMPersonenAbfrage.belegeListe();
        return 1;
    }

    /**eclDbM wird in aufrufender Funktion gehandelt*/
    public boolean initZugang(boolean pAlle) {
        tPortalFunktionen.belegePortalFunktionenWillenserklaerungenStatusAktiv();
        if (!tPruefeStartNachOpen.pruefeStartNachOpen(KonstPortalView.AUSWAHL1)) {
            return false;
        }
        if (tAuswahlSession.isOnlineteilnahmeAktivUB() == false) {
            tSession.trageFehlerEin(CaFehler.afFunktionDerzeitNichtAktiv);
            return false;
        }

        if (eclParamM.getParam().paramPortal.onlineTeilnahmeSeparateNutzungsbedingungen == 1) {
            if (tPraesenzZugangAbgangSession.isNutzungsbedingungenAkzeptiert() == false) {
                tSession.trageFehlerEin(CaFehler.afBestaetigungNutzungsbedingungenOnlineTeilnahmeFehlt);
                return false;
            }
            if (tPraesenzZugangAbgangSession.isPersonBestaetigt() == false) {
                tSession.trageFehlerEin(CaFehler.afBestaetigungPersonOnlineTeilnahmeFehlt);
                return false;
            }
        }

        tPraesenzZugangAbgangSession.setDurchfuehrenFuerAlle(pAlle);

        /*+++++++++++++++++Prüfen, ob:++++++++++++++++++++++++
         * 1.) überhaupt welche ausgewählt wurden
         * 2.) Briefwahl etc. für ausgewählte gegeben wurde
         */
        boolean zugangAuchFuerEigeneMoeglich = eclBesitzGesamtAuswahl1M.isEigenerBestandOTVertretbar();
        CaBug.druckeLog("zugangAuchFuerEigeneMoeglich=" + zugangAuchFuerEigeneMoeglich, logDrucken, 5);
        BlWillenserklaerungStatusNeuVerarbeiten blWillenserklaerungStatusNeuVerarbeiten = new BlWillenserklaerungStatusNeuVerarbeiten(
                eclDbM.getDbBundle());
        blWillenserklaerungStatusNeuVerarbeiten.besitzJeKennungListe = eclBesitzGesamtM
                .getBesitzJeKennungListeNichtPraesent();
        blWillenserklaerungStatusNeuVerarbeiten.pruefeAufAusgewaehltUndWeisung(pAlle, zugangAuchFuerEigeneMoeglich);

        CaBug.druckeLog("blWillenserklaerungStatusNeuVerarbeiten.rcInSammelkarteVorhanden="
                + blWillenserklaerungStatusNeuVerarbeiten.rcInSammelkarteVorhanden, logDrucken, 5);
        CaBug.druckeLog("blWillenserklaerungStatusNeuVerarbeiten.rcAusgewaehlteVorhanden="
                + blWillenserklaerungStatusNeuVerarbeiten.rcAusgewaehlteVorhanden, logDrucken, 5);

        if (blWillenserklaerungStatusNeuVerarbeiten.rcAusgewaehlteVorhanden == false) {
            /**Keine Bestände ausgewählt -> Fehlermeldung*/
            tSession.trageFehlerEin(CaFehler.afBestandFuerZugangAuswaehlen);
            return false;
        }

        if (blWillenserklaerungStatusNeuVerarbeiten.rcInSammelkarteVorhanden == false) {
            tPraesenzZugangAbgangSession.setHinweisWeisungsStornierungAnzeigen(false);
        } else {
            tPraesenzZugangAbgangSession.setHinweisWeisungsStornierungAnzeigen(true);
        }

        /*++++++++++++++++Personen-Abfrage vorbereiten+++++++++++++++++*/
        blMPersonenAbfrage.belegeListe();

        return true;
    }

    /**Kein eclDbM erforderlich*/
    public int initAbgang() {
        /*Funktion vorhanden?*/
        if (tRender.auswahlTeilnahmeAbgang() == false) {
            return CaFehler.afFunktionNichtAuswaehlbar;
        }

        return 1;
    }

    /****************Buttons iPraesenzZugangAbfragePerson*********************************/

    public void doPersonAuswaehlen() {
        if (!tSessionVerwaltung.pruefeStart(KonstPortalView.PRAESENZ_ZUGANG_ABFRAGE_PERSON)) {
            return;
        }

        int art = blMPersonenAbfrage.belegeAusgewaehltenTeilnehmer();
        if (art == 4) { //"Sonstige Person"
            tSessionVerwaltung.setzeEnde(KonstPortalView.PRAESENZ_ZUGANG_FALSCHE_PERSON);
        } else {
            tSessionVerwaltung.setzeEnde(KonstPortalView.PRASESENZ_ZUGANG_PERSON_BESTAETIGEN);
        }
        return;
    }

    public void doPersonAuswaehlenZurueck() {
        if (!tSessionVerwaltung.pruefeStart(KonstPortalView.PRAESENZ_ZUGANG_ABFRAGE_PERSON)) {
            return;
        }
        tSessionVerwaltung.setzeEnde(tFunktionen.waehleAuswahlNachPraesenzfunktion());
        return;
    }

    /****************Buttons iPraesenzZugangFalschePerson*********************************/
    public void doFalschePersonWeiter() {
        if (!tSessionVerwaltung.pruefeStart(KonstPortalView.PRAESENZ_ZUGANG_FALSCHE_PERSON)) {
            return;
        }

        tSessionVerwaltung.setzeEnde(tFunktionen.waehleAuswahlNachPraesenzfunktion());
        return;
    }

    public void doFalschePersonZurueck() {
        if (!tSessionVerwaltung.pruefeStart(KonstPortalView.PRAESENZ_ZUGANG_FALSCHE_PERSON)) {
            return;
        }
        tSessionVerwaltung.setzeEnde(tFunktionen.waehleAuswahlNachPraesenzfunktion());
        return;
    }

    /****************Buttons iPraesenzZugangPersonBestaetigen*********************************/

    /**Bestätigen und gleichzeitig Zugang buchen*/
    public void doPersonBestaetigenZugangUndBuchen() {
        if (!tSessionVerwaltung.pruefeStart(KonstPortalView.PRASESENZ_ZUGANG_PERSON_BESTAETIGEN)) {
            return;
        }

        eclDbM.openAll();
        tPortalFunktionen.belegePortalFunktionenWillenserklaerungenStatusAktiv();
        if (!tPruefeStartNachOpen.pruefeStartNachOpen(KonstPortalView.AUSWAHL1)) {
            tSessionVerwaltung.setzeEnde();
            eclDbM.closeAll();
            return;
        }
        if (tAuswahlSession.isOnlineteilnahmeAktivUB() == false) {
            eclDbM.closeAll();
            tFehlerViewSession.setFehlerArt(2);
            tFehlerViewSession.setNextView(tFunktionen.waehleAuswahlNachPraesenzfunktion());
            tSessionVerwaltung.setzeEnde(KonstPortalView.FEHLER_VIEW);
            return;
        }

        zugangBuchen();
        eclDbM.closeAll();

        tSessionVerwaltung.setzeEnde(tFunktionen.waehleAuswahlNachPraesenzfunktion());
        return;
    }

    public void zugangBuchen() {
        int bevollmaechtigter = eclLoginDatenM.lieferePersonNatJurIdent();
        String bevollmaechtigterName = eclLoginDatenM.getName();
        String bevollmaechtigterVorname = eclLoginDatenM.getVorname();
        String bevollmaechtigterOrt = eclLoginDatenM.getOrt();

        if (eclLoginDatenM.liefereKennungArt() == 1) {
            EclAktienregisterErgaenzung lAktienregisterErgaenzung = eclLoginDatenM.getEclAktienregisterErgaenzung();
            if (lAktienregisterErgaenzung != null) {
                if (lAktienregisterErgaenzung.ergaenzungKennzeichen[24] != 0) {
                    bevollmaechtigter = lAktienregisterErgaenzung.ergaenzungKennzeichen[24];

                    bevollmaechtigterName = lAktienregisterErgaenzung.ergaenzungLangString[24];
                    bevollmaechtigterVorname = lAktienregisterErgaenzung.ergaenzungLangString[25];
                    bevollmaechtigterOrt = lAktienregisterErgaenzung.ergaenzungLangString[26];

                }
            } else {
                CaBug.drucke("001");
            }
        }

        boolean zugangAuchFuerEigeneMoeglich = eclBesitzGesamtAuswahl1M.isEigenerBestandOTVertretbar();
        CaBug.druckeLog("zugangAuchFuerEigeneMoeglich=" + zugangAuchFuerEigeneMoeglich, logDrucken, 5);

        BlWillenserklaerungStatusNeuVerarbeiten blWillenserklaerungStatusNeuVerarbeiten = new BlWillenserklaerungStatusNeuVerarbeiten(
                eclDbM.getDbBundle());
        blWillenserklaerungStatusNeuVerarbeiten.besitzJeKennungListe = eclBesitzGesamtM
                .getBesitzJeKennungListeNichtPraesent();
        blWillenserklaerungStatusNeuVerarbeiten.storniereSammelkartenEintraegeUndBucheZugang(
                tPraesenzZugangAbgangSession.isDurchfuehrenFuerAlle(), eclLoginDatenM.lieferePersonNatJurIdent(),
                bevollmaechtigter, bevollmaechtigterName, bevollmaechtigterVorname, bevollmaechtigterOrt,
                zugangAuchFuerEigeneMoeglich);

        tAuswahl1Teilnahme.init();
    }

    public void doPersonBestaetigenZurueck() {
        if (!tSessionVerwaltung.pruefeStart(KonstPortalView.PRASESENZ_ZUGANG_PERSON_BESTAETIGEN)) {
            return;
        }
        tSessionVerwaltung.setzeEnde(KonstPortalView.PRAESENZ_ZUGANG_ABFRAGE_PERSON);
        return;
    }

    /**********************************Buttons iPraesenzZugangAbfrageStornierung*********************************/
    /**Bestätigen und gleichzeitig Zugang buchen*/
    public void doAbfrageStornierungWeiter() {
        if (!tSessionVerwaltung.pruefeStart(KonstPortalView.PRAESENZ_ZUGANG_ABFRAGE_STORNIERUNG)) {
            return;
        }

        eclDbM.openAll();

        tPortalFunktionen.belegePortalFunktionenWillenserklaerungenStatusAktiv();
        if (!tPruefeStartNachOpen.pruefeStartNachOpen(KonstPortalView.AUSWAHL1)) {
            tSessionVerwaltung.setzeEnde();
            eclDbM.closeAll();
            return;
        }
        if (tAuswahlSession.isOnlineteilnahmeAktivUB() == false) {
            eclDbM.closeAll();
            tFehlerViewSession.setFehlerArt(2);
            tFehlerViewSession.setNextView(tFunktionen.waehleAuswahlNachPraesenzfunktion());
            tSessionVerwaltung.setzeEnde(KonstPortalView.FEHLER_VIEW);
            return;
        }

        zugangBuchen();
        eclDbM.closeAll();

        tSessionVerwaltung.setzeEnde(tFunktionen.waehleAuswahlNachPraesenzfunktion());
        return;
    }

    public void doAbfrageStornierungZurueck() {
        if (!tSessionVerwaltung.pruefeStart(KonstPortalView.PRAESENZ_ZUGANG_ABFRAGE_STORNIERUNG)) {
            return;
        }
        tSessionVerwaltung.setzeEnde(tFunktionen.waehleAuswahlNachPraesenzfunktion());
        return;
    }

    /****************Buttons iPraesenzAbgangBestaetigung*********************************/
    /***************************Derzeit nicht aktiv**********************************/
    /**Bestätigen und gleichzeitig Zugang buchen*/
    public void doAbgangWeiter() {
        if (!tSessionVerwaltung.pruefeStart(KonstPortalView.PRAESENZ_ABGANG_BESTAETIGUNG)) {
            return;
        }

        eclDbM.openAll();

        tPortalFunktionen.belegePortalFunktionenWillenserklaerungenStatusAktiv();
        if (!tPruefeStartNachOpen.pruefeStartNachOpen(KonstPortalView.AUSWAHL1)) {
            tSessionVerwaltung.setzeEnde();
            eclDbM.closeAll();
            return;
        }

        BlWillenserklaerungStatusNeuVerarbeiten blWillenserklaerungStatusNeuVerarbeiten = new BlWillenserklaerungStatusNeuVerarbeiten(
                eclDbM.getDbBundle());
        blWillenserklaerungStatusNeuVerarbeiten.besitzJeKennungListe = eclBesitzGesamtM
                .getBesitzJeKennungListePraesent();
        blWillenserklaerungStatusNeuVerarbeiten.bucheAbgang(true, eclLoginDatenM.lieferePersonNatJurIdent());

        tAuswahl1Teilnahme.init();

        eclDbM.closeAll();

        tSessionVerwaltung.setzeEnde(tFunktionen.waehleAuswahlNachPraesenzfunktion());
        return;
    }

    public void doAbgangZurueck() {
        if (!tSessionVerwaltung.pruefeStart(KonstPortalView.PRAESENZ_ABGANG_BESTAETIGUNG)) {
            return;
        }
        tSessionVerwaltung.setzeEnde(tFunktionen.waehleAuswahlNachPraesenzfunktion());
        return;
    }

    /***************************Abgang bei Logout / Browser-Schließen***************************************************/
    /**alle ggf. noch vorhandenen Präsenz-Buchungen werden
     * als Abgang gebucht
     */
    public void praesenzAbgang() {
        if (ParamSpezial.ku310(eclParamM.getClGlobalVar().mandant)==true) {
            /**Bei ku310 nicht buchen!*/
            return;
        }

        CaBug.druckeLog("001", logDrucken, 10);
        BlWillenserklaerungStatusNeuVerarbeiten blWillenserklaerungStatusNeuVerarbeiten = new BlWillenserklaerungStatusNeuVerarbeiten(
                eclDbM.getDbBundle());
        CaBug.druckeLog("002", logDrucken, 10);
        blWillenserklaerungStatusNeuVerarbeiten.besitzJeKennungListe = eclBesitzGesamtM
                .getBesitzJeKennungListePraesent();
        CaBug.druckeLog("003", logDrucken, 10);
        if (blWillenserklaerungStatusNeuVerarbeiten.besitzJeKennungListe != null) {
            CaBug.druckeLog("004", logDrucken, 10);
            blWillenserklaerungStatusNeuVerarbeiten.bucheAbgang(true, eclLoginDatenM.lieferePersonNatJurIdent());
            CaBug.druckeLog("005", logDrucken, 10);
       }
        CaBug.druckeLog("006", logDrucken, 10);
        tAuswahl1Teilnahme.init();
        CaBug.druckeLog("007", logDrucken, 10);
    }

    /*************************Gast*************************************************/

    public void doZugangGastSeparatesMenue() {
        if (!tSessionVerwaltung.pruefeStart(KonstPortalView.AUSWAHL1_TEILNAHMEGAST)) {
            return;
        }
        tSession.setRueckkehrZuMenue(KonstPortalView.AUSWAHL1_TEILNAHMEGAST);
        zugangGast();
    }

    public void doZugangGast() {
        if (!tSessionVerwaltung.pruefeStart(KonstPortalView.AUSWAHL1_TEILNAHME)) {
            return;
        }
        tSession.setRueckkehrZuMenue(KonstPortalView.AUSWAHL1_TEILNAHME);
        zugangGast();
    }

    public void doZugangGastAusAbfrageStornierung() {
        if (!tSessionVerwaltung.pruefeStart(KonstPortalView.PRAESENZ_ZUGANG_ABFRAGE_STORNIERUNG)) {
            return;
        }
        tSession.setRueckkehrZuMenue(KonstPortalView.AUSWAHL1_TEILNAHME);
        zugangGast();
    }

    private void zugangGast() {
        eclDbM.openAll();

        tPortalFunktionen.belegePortalFunktionenWillenserklaerungenStatusAktiv();
        if (!tPruefeStartNachOpen.pruefeStartNachOpen(KonstPortalView.AUSWAHL1)) {
            tSessionVerwaltung.setzeEnde();
            eclDbM.closeAll();
            return;
        }
        if (tAuswahlSession.isOnlineteilnahmeAktivUB() == false) {
            eclDbM.closeAll();
            tSession.trageFehlerEin(CaFehler.afFunktionDerzeitNichtAktiv);
            tSessionVerwaltung.setzeEnde();
            return;
        }

        zugangGastBuchen();
        eclDbM.closeAll();

        tSessionVerwaltung.setzeEnde(tSession.getRueckkehrZuMenue());
        return;
    }

    public void doAbgangGastSeparatesMenue() {
        if (!tSessionVerwaltung.pruefeStart(KonstPortalView.AUSWAHL1_TEILNAHMEGAST)) {
            return;
        }
        tSession.setRueckkehrZuMenue(KonstPortalView.AUSWAHL1_TEILNAHMEGAST);
        abgangGast();
    }

    public void doAbgangGast() {
        if (!tSessionVerwaltung.pruefeStart(KonstPortalView.AUSWAHL1_TEILNAHME)) {
            return;
        }
        tSession.setRueckkehrZuMenue(KonstPortalView.AUSWAHL1_TEILNAHME);
        abgangGast();
    }

    private void abgangGast() {

        eclDbM.openAll();

        tPortalFunktionen.belegePortalFunktionenWillenserklaerungenStatusAktiv();
        if (!tPruefeStartNachOpen.pruefeStartNachOpen(KonstPortalView.AUSWAHL1)) {
            tSessionVerwaltung.setzeEnde();
            eclDbM.closeAll();
            return;
        }

        abgangGastBuchen();
        eclDbM.closeAll();

        tSessionVerwaltung.setzeEnde(tSession.getRueckkehrZuMenue());
        return;
    }

    public void zugangGastBuchen() {

        //		if (eclParamM.getParam().paramPortal.onlineTeilnahmeSeparateNutzungsbedingungen==1) {
        //			if (tPraesenzZugangAbgangSession.isNutzungsbedingungenAkzeptiert()==false) {
        //				tSession.trageFehlerEin(CaFehler.afBestaetigungNutzungsbedingungenOnlineTeilnahmeFehlt);
        //				return;
        //			}
        //			if (tPraesenzZugangAbgangSession.isPersonBestaetigt()==false) {
        //				tSession.trageFehlerEin(CaFehler.afBestaetigungPersonOnlineTeilnahmeFehlt);
        //				return;
        //			}
        //		}

        int lLoginIdent = eclLoginDatenM.getEclLoginDaten().ident;
        eclDbM.getDbBundle().dbLoginDaten.read_ident(lLoginIdent);

        EclLoginDaten lLoginDaten = eclDbM.getDbBundle().dbLoginDaten.ergebnisPosition(0);
        lLoginDaten.kommunikationssprache = 101;
        eclDbM.getDbBundle().dbLoginDaten.update(lLoginDaten);
        eclLoginDatenM.setEclLoginDaten(lLoginDaten);
        eclBesitzGesamtAuswahl1M.setGastPraesent(true);
        tAuswahl1Teilnahme.init();
    }

    public void abgangGastBuchen() {
        int lLoginIdent = eclLoginDatenM.getEclLoginDaten().ident;
        eclDbM.getDbBundle().dbLoginDaten.read_ident(lLoginIdent);

        EclLoginDaten lLoginDaten = eclDbM.getDbBundle().dbLoginDaten.ergebnisPosition(0);
        lLoginDaten.kommunikationssprache = 102;
        eclDbM.getDbBundle().dbLoginDaten.update(lLoginDaten);
        eclLoginDatenM.setEclLoginDaten(lLoginDaten);
        eclBesitzGesamtAuswahl1M.setGastPraesent(false);
        tAuswahl1Teilnahme.init();
        tSession.setStreamshow(false);
    }
    
    
    /******************************************Virtuelle HV*********************************************************/
    public int initVirtuelleHV() {
        tPortalFunktionen.belegePortalFunktionenWillenserklaerungenStatusAktiv();
        if (tAuswahlSession.isOnlineteilnahmeAktivUB() == false) {
            return CaFehler.afFunktionDerzeitNichtAktiv;
        }
        return 1;
    }
    
    public int zugangBuchenVirtuelleHV() {
        CaBug.druckeLog("", logDrucken, 10);
        tPortalFunktionen.belegePortalFunktionenWillenserklaerungenStatusAktiv();
        if (tAuswahlSession.isOnlineteilnahmeAktivUB() == false) {
            return CaFehler.afFunktionDerzeitNichtAktiv;
        }
        
        eclBesitzGesamtM.setKennungIstOnlinePraesent(true);
        
        BlPraesenzlistenNummer lBlPraesenzlistenNummer = new BlPraesenzlistenNummer(eclDbM.getDbBundle());
        lBlPraesenzlistenNummer.leseAktuelleNummernOhneUpdate();/*TODO Verändert auf ohne Update*/

        return zugangAbgangBuchen(1);
    }
    
    public int abgangBuchenVirtuelleHV() {
        CaBug.druckeLog("", logDrucken, 10);
        tPortalFunktionen.belegePortalFunktionenWillenserklaerungenStatusAktiv();
        if (tAuswahlSession.isOnlineteilnahmeAktivUB() == false) {
            return CaFehler.afFunktionDerzeitNichtAktiv;
        }

        eclBesitzGesamtM.setKennungIstOnlinePraesent(false);

        BlPraesenzlistenNummer lBlPraesenzlistenNummer = new BlPraesenzlistenNummer(eclDbM.getDbBundle());
        lBlPraesenzlistenNummer.leseAktuelleNummernOhneUpdate();/*TODO Verändert auf ohne Update*/

        return zugangAbgangBuchen(2);
    }
    
    private int zugangAbgangBuchen(int pZugangOderAbgang) {
        /*Bevollmächtigten ermitteln (leer = kein Bevollmächtigter)*/
        
        int kennungIdent=eclLoginDatenM.getEclLoginDaten().ident;
        int bevollmaechtigeterPersonNatJurIdent=0;
        String bevollmaechtigterName="";
        String bevollmaechtigterVorname="";
        String bevollmaechtigterOrt="";

        if (eclLoginDatenM.liefereKennungArt()==2) {
            bevollmaechtigeterPersonNatJurIdent=eclLoginDatenM.getEclLoginDaten().personenNatJurIdent;
            bevollmaechtigterName = eclLoginDatenM.getName();
            bevollmaechtigterVorname = eclLoginDatenM.getVorname();
            bevollmaechtigterOrt = eclLoginDatenM.getOrt();
            
            
        }
        
        /*Bestand durchrennen und verbuchen*/
        List<EclBesitzJeKennung> besitzJeKennungListe=eclBesitzGesamtM.getBesitzJeKennungListe();
        for (EclBesitzJeKennung iEclBesitzJeKennung: besitzJeKennungListe) {
            if (iEclBesitzJeKennung.eigenerAREintragVorhanden) {
                /**Eigener Besitz*/
                for (EclBesitzAREintrag iEclBesitzAREintrag: iEclBesitzJeKennung.eigenerAREintragListe) {
                    zugangAbgangBuchenAREintrag(pZugangOderAbgang, 
                            kennungIdent, bevollmaechtigeterPersonNatJurIdent, bevollmaechtigterName, bevollmaechtigterVorname, bevollmaechtigterOrt, 
                            iEclBesitzAREintrag);            
                }
            }
            if (iEclBesitzJeKennung.erhalteneVollmachtenVorhanden) {
                /**Vollmachten*/
                for (EclZugeordneteMeldungNeu iEclZugeordneteMeldung: iEclBesitzJeKennung.zugeordneteMeldungenBevollmaechtigtListe) {
                    zugangAbgangBuchenMeldung(pZugangOderAbgang, 
                            kennungIdent, bevollmaechtigeterPersonNatJurIdent, bevollmaechtigterName, bevollmaechtigterVorname, bevollmaechtigterOrt, 
                            iEclZugeordneteMeldung);
                }
            }
            if (iEclBesitzJeKennung.instiAREintraegeVorhanden) {
                /**Insti-zugeordnete Aktienregistereinträge*/
                for (EclBesitzAREintrag iEclBesitzAREintrag: iEclBesitzJeKennung.instiAREintraegeListe) {
                    zugangAbgangBuchenAREintrag(pZugangOderAbgang, 
                            kennungIdent, bevollmaechtigeterPersonNatJurIdent, bevollmaechtigterName, bevollmaechtigterVorname, bevollmaechtigterOrt, 
                            iEclBesitzAREintrag);            
                }
            }
            if (iEclBesitzJeKennung.instiMeldungenVorhanden) {
                /**Insti-Zugeordnete Meldungen*/
                for (EclZugeordneteMeldungNeu iEclZugeordneteMeldung: iEclBesitzJeKennung.zugeordneteMeldungenInstiListe) {
                    zugangAbgangBuchenMeldung(pZugangOderAbgang, 
                            kennungIdent, bevollmaechtigeterPersonNatJurIdent, bevollmaechtigterName, bevollmaechtigterVorname, bevollmaechtigterOrt, 
                            iEclZugeordneteMeldung);
                }
            }

        }
        
        
        return 1;
    }
    
    private int zugangAbgangBuchenAREintrag(int pZugangOderAbgang, 
            int kennungIdent, int bevollmaechtigeterPersonNatJurIdent, String bevollmaechtigterName, String bevollmaechtigterVorname, String bevollmaechtigterOrt, 
            EclBesitzAREintrag iEclBesitzAREintrag) {
        
        List<EclZugeordneteMeldungNeu> zugeordneteMeldungenListe=iEclBesitzAREintrag.zugeordneteMeldungenListe;
        if (zugeordneteMeldungenListe!=null) {
            for (EclZugeordneteMeldungNeu iEclZugeordneteMeldung: zugeordneteMeldungenListe) {
                zugangAbgangBuchenMeldung(pZugangOderAbgang, 
                        kennungIdent, bevollmaechtigeterPersonNatJurIdent, bevollmaechtigterName, bevollmaechtigterVorname, bevollmaechtigterOrt, 
                        iEclZugeordneteMeldung);
            }
            
        }
        return 1;
    }

    private int zugangAbgangBuchenMeldung(int pZugangOderAbgang, 
            int kennungIdent, int bevollmaechtigeterPersonNatJurIdent, String bevollmaechtigterName, String bevollmaechtigterVorname, String bevollmaechtigterOrt, 
            EclZugeordneteMeldungNeu iEclZugeordneteMeldung) {
        
        /*tbl_meldungVirtuellePraesenz aktualisieren*/
        boolean wiederZugang=false;
        int willensart=0;
        
        if (pZugangOderAbgang==1) {
            /*Zugang*/
            /*Prüfen, ob schon ein Eintrag vorhanden*/
            eclDbM.getDbBundle().dbMeldungVirtuellePraesenz.read(kennungIdent, iEclZugeordneteMeldung.meldungsIdent);
            if (eclDbM.getDbBundle().dbMeldungVirtuellePraesenz.anzErgebnis()>0) {
                wiederZugang=true;
            }
            
            
            EclMeldungVirtuellePraesenz lMeldungVirtuellePraesenz=new EclMeldungVirtuellePraesenz();
            lMeldungVirtuellePraesenz.loginkennungIdent=kennungIdent;
            lMeldungVirtuellePraesenz.meldungsIdent=iEclZugeordneteMeldung.meldungsIdent;
            lMeldungVirtuellePraesenz.vertreterName=bevollmaechtigterName;
            lMeldungVirtuellePraesenz.vertreterVorname=bevollmaechtigterVorname;
            lMeldungVirtuellePraesenz.vertreterOrt=bevollmaechtigterOrt;
            lMeldungVirtuellePraesenz.statusPraesenz=1;
           
            eclDbM.getDbBundle().dbMeldungVirtuellePraesenz.update_Zugang(lMeldungVirtuellePraesenz);
            
            /*Willenserkläerungart festlegen*/
            if (wiederZugang==false) {
                if (bevollmaechtigterName.isEmpty()) {
                    willensart=KonstWillenserklaerung.virtZugangSelbst;
                }
                else {
                    willensart=KonstWillenserklaerung.virtZugangVollmacht;
                }
            }
            else {
                if (bevollmaechtigterName.isEmpty()) {
                    willensart=KonstWillenserklaerung.virtWiederzugangSelbst;
                }
                else {
                    willensart=KonstWillenserklaerung.virtWiederzugangVollmacht;
                }
               
            }
        }
        else {
            /*Abgang*/
            eclDbM.getDbBundle().dbMeldungVirtuellePraesenz.update_Abgang(kennungIdent, iEclZugeordneteMeldung.meldungsIdent);
            
            /*Willenserkläerungart festlegen*/
            if (bevollmaechtigterName.isEmpty()) {
                willensart=KonstWillenserklaerung.virtAbgang;
            }
            else {
                willensart=KonstWillenserklaerung.virtAbgangVollmacht;
            }

        }

        /**Willenserklärung wegschreiben*/
        
        EclWillenserklaerung lPreparedWillenserklaerung = new EclWillenserklaerung();
        EclWillenserklaerungZusatz lPreparedWillenserklaerungZusatz = new EclWillenserklaerungZusatz();
        
        lPreparedWillenserklaerung.willenserklaerung = willensart;
        lPreparedWillenserklaerung.meldungsIdent = iEclZugeordneteMeldung.meldungsIdent;
        lPreparedWillenserklaerung.stimmen = iEclZugeordneteMeldung.stueckAktien; /*TODO VidKonf  mit Stimmen/Aktien nochmal checken ....*/
        lPreparedWillenserklaerung.aktien = iEclZugeordneteMeldung.stueckAktien;

        lPreparedWillenserklaerung.identifikationDurch = 0;
        lPreparedWillenserklaerung.identifikationKlasse = 0;
        lPreparedWillenserklaerung.identifikationZutrittsIdent = "";
        lPreparedWillenserklaerung.identifikationStimmkarte = "";
        lPreparedWillenserklaerung.identifikationStimmkarteSecond = "";

        lPreparedWillenserklaerung.erteiltAufWeg = KonstWillenserklaerungWeg.portal;
        lPreparedWillenserklaerung.veraenderungszeit = CaDatumZeit.DatumZeitStringFuerDatenbank();

        lPreparedWillenserklaerung.willenserklaerungGeberIdent = kennungIdent;

        lPreparedWillenserklaerung.verweisart = 0;
        lPreparedWillenserklaerung.verweisAufWillenserklaerung = 0;

        lPreparedWillenserklaerung.bevollmaechtigterDritterIdent = bevollmaechtigeterPersonNatJurIdent;
        lPreparedWillenserklaerung.folgeBuchungFuerIdent = 0;
        /*Hinweis: wird ggf. von den einzelnen "Nutzern" nach Aufruf von prepareWillenserklaerung
         * nochmal überschrieben - siehe Beschreibung "mehrfach abhängig" bei pFolgeFuerWillenserklaerungIdent
         */

        eclDbM.getDbBundle().dbWillenserklaerung.insert(lPreparedWillenserklaerung, lPreparedWillenserklaerungZusatz);
        
        
        return 1;
    }

}
