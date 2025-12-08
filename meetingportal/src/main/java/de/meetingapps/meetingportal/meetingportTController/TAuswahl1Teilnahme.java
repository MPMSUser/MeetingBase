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
import de.meetingapps.meetingportal.meetComAllg.CaFehler;
import de.meetingapps.meetingportal.meetComBl.BlWillenserklaerungStatusNeuVerarbeiten;
import de.meetingapps.meetingportal.meetComEclM.EclBesitzGesamtAuswahl1M;
import de.meetingapps.meetingportal.meetComEclM.EclBesitzGesamtM;
import de.meetingapps.meetingportal.meetComEclM.EclDbM;
import de.meetingapps.meetingportal.meetComEclM.EclLoginDatenM;
import de.meetingapps.meetingportal.meetComEclM.EclParamM;
import de.meetingapps.meetingportal.meetComKonst.KonstPortalFunktionen;
import de.meetingapps.meetingportal.meetComKonst.KonstPortalUnterlagen;
import de.meetingapps.meetingportal.meetComKonst.KonstPortalView;
import de.meetingapps.meetingportal.meetingportTFunktionen.TFunktionen;
import de.meetingapps.meetingportal.meetingportTFunktionen.TPortalFunktionen;
import de.meetingapps.meetingportal.meetingportTFunktionen.TPruefeStartNachOpen;
import de.meetingapps.meetingportal.meetingportTFunktionen.TSessionVerwaltung;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@RequestScoped
@Named
public class TAuswahl1Teilnahme {

    private int logDrucken = 3;

    private @Inject EclDbM eclDbM;

    private @Inject TSession tSession;
    private @Inject TAuswahlSession tAuswahlSession;
    private @Inject TPortalFunktionen tPortalFunktionen;
    private @Inject TFunktionen tFunktionen;
    private @Inject TSessionVerwaltung tSessionVerwaltung;
    private @Inject TPraesenzZugangAbgang tPraesenzZugangAbgang;
    private @Inject TPraesenzZugangAbgangSession tPraesenzZugangAbgangSession;
    private @Inject EclBesitzGesamtM eclBesitzGesamtM;
    private @Inject TAuswahl1Teilnahme tAuswahl1Teilnahme;
    private @Inject EclLoginDatenM eclLoginDatenM;
    private @Inject TUnterlagen tUnterlagen;
    private @Inject TTeilnehmerverz tTeilnehmerverz;
    private @Inject EclParamM eclParamM;
    private @Inject TStimmabgabe tStimmabgabe;
    private @Inject TMitteilung tMitteilung;
    private @Inject EclBesitzGesamtAuswahl1M eclBesitzGesamtAuswahl1M;

    private @Inject TPruefeStartNachOpen tPruefeStartNachOpen;

    /**Wird nur beim Aufruf des Menüpunktes aus auswahl1 heraus aufgerufen.
     * Initialisiert die nutzungsbedingungen-Zustimmung 
     */
    public void initAusMenue() {
        tPortalFunktionen.belegePortalFunktionenWillenserklaerungenStatusAktiv();

        tPraesenzZugangAbgangSession.setNutzungsbedingungenAkzeptiert(false);
        tPraesenzZugangAbgangSession.setPersonBestaetigt(false);

        eclBesitzGesamtAuswahl1M.setGastPraesent(false);

    }

    /**eclDbM wird in aufrufender Funktion gehandelt*/
    public void init() {
        CaBug.druckeLog("001", logDrucken, 10);
        tFunktionen.leseStatusPortal(eclDbM.getDbBundle());
        CaBug.druckeLog("002", logDrucken, 10);
        tPortalFunktionen.belegePortalFunktionenWillenserklaerungenStatusAktiv();
        CaBug.druckeLog("003", logDrucken, 10);
    }

    public void doZugangAusgewaehlte() {
        if (!tSessionVerwaltung.pruefeStart(KonstPortalView.AUSWAHL1_TEILNAHME)) {
            return;
        }

        eclDbM.openAll();
        boolean brc = tPraesenzZugangAbgang.initZugang(false);
        if (brc == false) {
            eclDbM.closeAll();
            tSessionVerwaltung.setzeEnde();
            return;
        }

        if (eclParamM.getParam().paramPortal.onlineTeilnehmerAbfragen == 1) {
            eclDbM.closeAll();
            /*Name abfragen - beinhaltet auch Briefwahl-Stornierungsabfrage*/
            tSessionVerwaltung.setzeEnde(KonstPortalView.PRAESENZ_ZUGANG_ABFRAGE_PERSON);
            return;
        }
        if (tPraesenzZugangAbgangSession.isHinweisWeisungsStornierungAnzeigen()) {
            eclDbM.closeAll();
            /*Briefwahl muß storniert werden => Abfrage durchführen*/
            tSessionVerwaltung.setzeEnde(KonstPortalView.PRAESENZ_ZUGANG_ABFRAGE_STORNIERUNG);
            return;
        }

        /*Keine weitere Abfrage - Zugang durchführen*/
        tPraesenzZugangAbgang.zugangBuchen();
        eclDbM.closeAll();

        tSessionVerwaltung.setzeEnde(tFunktionen.waehleAuswahlNachPraesenzfunktion());
        return;
    }

    public void doZugangAlle() {
        if (!tSessionVerwaltung.pruefeStart(KonstPortalView.AUSWAHL1_TEILNAHME)) {
            return;
        }

        eclDbM.openAll();
        boolean brc = tPraesenzZugangAbgang.initZugang(true);
        if (brc == false) {
            eclDbM.closeAll();
            tSessionVerwaltung.setzeEnde();
            return;
        }

        if (eclParamM.getParam().paramPortal.onlineTeilnehmerAbfragen == 1) {
            /*Name abfragen - beinhaltet auch Briefwahl-Stornierungsabfrage*/
            eclDbM.closeAll();
            tSessionVerwaltung.setzeEnde(KonstPortalView.PRAESENZ_ZUGANG_ABFRAGE_PERSON);
            return;
        }
        if (tPraesenzZugangAbgangSession.isHinweisWeisungsStornierungAnzeigen()) {
            /*Briefwahl muß storniert werden => Abfrage durchführen*/
            eclDbM.closeAll();
            tSessionVerwaltung.setzeEnde(KonstPortalView.PRAESENZ_ZUGANG_ABFRAGE_STORNIERUNG);
            return;
        }
        /*Keine weitere Abfrage - Zugang durchführen*/
        tPraesenzZugangAbgang.zugangBuchen();
        eclDbM.closeAll();

        tSessionVerwaltung.setzeEnde(tFunktionen.waehleAuswahlNachPraesenzfunktion());
        return;

    }

    public void doAbgangAusgewaehlte() {
        if (!tSessionVerwaltung.pruefeStart(KonstPortalView.AUSWAHL1_TEILNAHME)) {
            return;
        }

        ausfuehrenAbgang(false);

        tSessionVerwaltung.setzeEnde(tFunktionen.waehleAuswahlNachPraesenzfunktion());
        return;

    }

    public void doAbgangAlle() {
        if (!tSessionVerwaltung.pruefeStart(KonstPortalView.AUSWAHL1_TEILNAHME)) {
            return;
        }

        ausfuehrenAbgang(true);
        tSession.setStreamshow(false);

        tSessionVerwaltung.setzeEnde(tFunktionen.waehleAuswahlNachPraesenzfunktion());
        return;
    }

    private void ausfuehrenAbgang(boolean pAlle) {
        tPraesenzZugangAbgangSession.setDurchfuehrenFuerAlle(pAlle);

        eclDbM.openAll();
        BlWillenserklaerungStatusNeuVerarbeiten blWillenserklaerungStatusNeuVerarbeiten = new BlWillenserklaerungStatusNeuVerarbeiten(
                eclDbM.getDbBundle());
        blWillenserklaerungStatusNeuVerarbeiten.besitzJeKennungListe = eclBesitzGesamtM
                .getBesitzJeKennungListePraesent();
        blWillenserklaerungStatusNeuVerarbeiten.bucheAbgang(tPraesenzZugangAbgangSession.isDurchfuehrenFuerAlle(),
                eclLoginDatenM.lieferePersonNatJurIdent());
        tAuswahl1Teilnahme.init();
        eclDbM.closeAll();

    }

    /*********************Div. Funktionen*******************************/

    /*++++++++++++++++++++++Unterlagen++++++++++++++++++++++++++*/
    public void doUnterlagenGast() {
        if (!tSessionVerwaltung.pruefeStart(KonstPortalView.AUSWAHL1_TEILNAHMEGAST)) {
            return;
        }
        tSession.setRueckkehrZuMenue(KonstPortalView.AUSWAHL1_TEILNAHMEGAST);
        eclDbM.openAll();

        tPortalFunktionen.belegePortalFunktionenWillenserklaerungenStatusAktiv();
        if (!tPruefeStartNachOpen.pruefeStartNachOpen(KonstPortalView.AUSWAHL1)) {
            eclDbM.closeAll();
            return;
        }
        if (tAuswahlSession.unterlagenAngeboten() == false) {
            tSession.trageFehlerEin(CaFehler.afFunktionDerzeitNichtAktiv);
            eclDbM.closeAll();
            return;
        }

        unterlagenAusfuehren();
        eclDbM.closeAll();
        return;
    }

    public void doUnterlagen() {
        if (!tSessionVerwaltung.pruefeStart(KonstPortalView.AUSWAHL1_TEILNAHME)) {
            return;
        }
        tSession.setRueckkehrZuMenue(KonstPortalView.AUSWAHL1_TEILNAHME);

        eclDbM.openAll();

        tPortalFunktionen.belegePortalFunktionenWillenserklaerungenStatusAktiv();
        if (!tPruefeStartNachOpen.pruefeStartNachOpen(KonstPortalView.AUSWAHL1)) {
            eclDbM.closeAll();
            return;
        }
        if (tAuswahlSession.unterlagenAngeboten() == false) {
            tSession.trageFehlerEin(CaFehler.afFunktionDerzeitNichtAktiv);
            eclDbM.closeAll();
            return;
        }

        unterlagenAusfuehren();
        eclDbM.closeAll();
        return;
    }

    private void unterlagenAusfuehren() {

        tUnterlagen.init(KonstPortalUnterlagen.ANZEIGEN_UNTERLAGEN, 0);

        tSessionVerwaltung.setzeEnde(KonstPortalView.UNTERLAGEN);
        return;

    }

    public void doStream() {
        if (!tSessionVerwaltung.pruefeStart(KonstPortalView.AUSWAHL1_TEILNAHME)) {
            return;
        }

        eclDbM.openAll();

        tPortalFunktionen.belegePortalFunktionenWillenserklaerungenStatusAktiv();
        if (!tPruefeStartNachOpen.pruefeStartNachOpen(KonstPortalView.AUSWAHL1)) {
            eclDbM.closeAll();
            return;
        }
        if (tAuswahlSession.isStreamAktiv() == false) {
            tSession.trageFehlerEin(CaFehler.afFunktionDerzeitNichtAktiv);
            eclDbM.closeAll();
            return;
        }

        // tStream.init();

        eclDbM.closeAll();
        tSession.setRueckkehrZuMenue(KonstPortalView.AUSWAHL1_TEILNAHME);
        tSessionVerwaltung.setzeEnde(KonstPortalView.STREAM_START);
        return;

    }

    public void doStreamAusGastAuswahl() {
        if (!tSessionVerwaltung.pruefeStart(KonstPortalView.AUSWAHL1_TEILNAHMEGAST)) {
            return;
        }

        eclDbM.openAll();

        tPortalFunktionen.belegePortalFunktionenWillenserklaerungenStatusAktiv();
        if (!tPruefeStartNachOpen.pruefeStartNachOpen(KonstPortalView.AUSWAHL1)) {
            eclDbM.closeAll();
            return;
        }
        if (tAuswahlSession.isStreamAktiv() == false) {
            tSession.trageFehlerEin(CaFehler.afFunktionDerzeitNichtAktiv);
            eclDbM.closeAll();
            return;
        }

        // tStream.init();

        eclDbM.closeAll();
        tSession.setRueckkehrZuMenue(KonstPortalView.AUSWAHL1_TEILNAHMEGAST);

        tSessionVerwaltung.setzeEnde(KonstPortalView.STREAM_START);
        return;

    }

    public void doFragen() {
        if (!tSessionVerwaltung.pruefeStart(KonstPortalView.AUSWAHL1_TEILNAHME)) {
            return;
        }

        eclDbM.openAll();
        eclDbM.openWeitere();

        tPortalFunktionen.belegePortalFunktionenWillenserklaerungenStatusAktiv();
        CaBug.druckeLog("vor tPruefeStartNachOpen", logDrucken, 10);
        if (!tPruefeStartNachOpen.pruefeStartNachOpen(KonstPortalView.AUSWAHL1)) {
            eclDbM.closeAll();
            return;
        }
        if (tAuswahlSession.isFragenAngeboten() == false) {
            tSession.trageFehlerEin(CaFehler.afFunktionDerzeitNichtAktiv);
            eclDbM.closeAll();
            return;
        }

        /**Abbilden auf TMitteilung*/
        /*int rc = */tMitteilung.initAusAuswahl(KonstPortalFunktionen.fragen);
        eclDbM.closeAll();
        tSession.setRueckkehrZuMenue(KonstPortalView.AUSWAHL1_TEILNAHME);
        tSessionVerwaltung.setzeEnde(KonstPortalView.FRAGEN);
        return;
    }

    public void doWidersprueche() {
        if (!tSessionVerwaltung.pruefeStart(KonstPortalView.AUSWAHL1_TEILNAHME)) {
            return;
        }

        eclDbM.openAll();
        eclDbM.openWeitere();

        tPortalFunktionen.belegePortalFunktionenWillenserklaerungenStatusAktiv();
        if (!tPruefeStartNachOpen.pruefeStartNachOpen(KonstPortalView.AUSWAHL1)) {
            eclDbM.closeAll();
            return;
        }
        if (tAuswahlSession.isWiderspruecheAngeboten() == false) {
            tSession.trageFehlerEin(CaFehler.afFunktionDerzeitNichtAktiv);
            eclDbM.closeAll();
            return;
        }

        /*TODO ABbildung auf tMitteilungen*/
//        tWidersprueche.init();
        eclDbM.closeAll();
        tSessionVerwaltung.setzeEnde(KonstPortalView.WIDERSPRUECHE);
        return;

    }

    public void doTeilnehmerverz() {
        if (!tSessionVerwaltung.pruefeStart(KonstPortalView.AUSWAHL1_TEILNAHME)) {
            return;
        }

        eclDbM.openAll();
        eclDbM.openWeitere();

        tPortalFunktionen.belegePortalFunktionenWillenserklaerungenStatusAktiv();
        if (!tPruefeStartNachOpen.pruefeStartNachOpen(KonstPortalView.AUSWAHL1)) {
            eclDbM.closeAll();
            return;
        }
        if (tAuswahlSession.isTeilnehmerverzeichnisAngeboten() == false) {
            tSession.trageFehlerEin(CaFehler.afFunktionDerzeitNichtAktiv);
            eclDbM.closeAll();
            return;
        }

        tTeilnehmerverz.init(false);
        eclDbM.closeAll();
        tSessionVerwaltung.setzeEnde(KonstPortalView.TEILNEHMERVERZEICHNIS);
        return;

    }

    public void doStimmabgabe() {
        if (!tSessionVerwaltung.pruefeStart(KonstPortalView.AUSWAHL1_TEILNAHME)) {
            return;
        }

        eclDbM.openAll();

        if (!tPruefeStartNachOpen.pruefeStartNachOpen(KonstPortalView.AUSWAHL1)) {
            tSessionVerwaltung.setzeEnde();
            eclDbM.closeAll();
            return;
        }

        int rc = tStimmabgabe.startStimmabgabe();

        eclDbM.closeAll();
        if (rc == CaFehler.afDerzeitKeineAbstimmungEroeffnet) {
            tSessionVerwaltung.setzeEnde(KonstPortalView.STIMMABGABE_DERZEIT_NICHT_MOEGLICH);
            return;
        }
        if (rc < 0) {
            switch (rc) {
            case CaFehler.afNichtStimmberechtigt:
            case CaFehler.afFunktionNichtAuswaehlbar:
            default:
                tSession.trageFehlerEin(rc);
                tSessionVerwaltung.setzeEnde();
                return;
            }
        }

        if (eclBesitzGesamtAuswahl1M.getAnzPraesenteVorhanden() == 1) {
            tStimmabgabe.startAlle();
            tSessionVerwaltung.setzeEnde(KonstPortalView.STIMMABGABE);
        } else {
            tSessionVerwaltung.setzeEnde(KonstPortalView.STIMMABGABE_AUSWAHL);
        }
        return;

    }

    public void doWortmeldungen() {
        if (!tSessionVerwaltung.pruefeStart(KonstPortalView.AUSWAHL1_TEILNAHME)) {
            return;
        }

        eclDbM.openAll();
        eclDbM.openWeitere();

        tPortalFunktionen.belegePortalFunktionenWillenserklaerungenStatusAktiv();
        if (!tPruefeStartNachOpen.pruefeStartNachOpen(KonstPortalView.AUSWAHL1)) {
            eclDbM.closeAll();
            return;
        }

        if (tAuswahlSession.isWortmeldungenAngeboten() == false) {
            tSession.trageFehlerEin(CaFehler.afFunktionDerzeitNichtAktiv);
            eclDbM.closeAll();
            return;
        }

        /*TODO Abbilden auf tMitteilung*/
//        tWortmeldungen.init();
        /*rc = */tMitteilung.initAusAuswahl(KonstPortalFunktionen.wortmeldungen);
        eclDbM.closeAll();
        tSessionVerwaltung.setzeEnde(KonstPortalView.WORTMELDUNGEN);
        return;

    }

    /**************************Zurück*******************************/
    public void doZurueck() {
        if (!tSessionVerwaltung.pruefeStart(KonstPortalView.AUSWAHL1_TEILNAHME)) {
            return;
        }
        tSessionVerwaltung.setzeEnde(KonstPortalView.AUSWAHL1);
        return;
    }

    /**********************************************Gast-Teilnahme************************************************/
    public void doZurueckGast() {
        if (!tSessionVerwaltung.pruefeStart(KonstPortalView.AUSWAHL1_TEILNAHMEGAST)) {
            return;
        }
        tSessionVerwaltung.setzeEnde(KonstPortalView.AUSWAHL1);
        return;
    }

}
