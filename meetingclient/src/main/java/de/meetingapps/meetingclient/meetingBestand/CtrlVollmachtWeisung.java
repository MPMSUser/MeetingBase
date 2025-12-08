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
package de.meetingapps.meetingclient.meetingBestand;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import de.meetingapps.meetingclient.meetingClientDialoge.CtrlRootWeisungenNeu;
import de.meetingapps.meetingclient.meetingClientOberflaechen.ComboBoxZusatz;
import de.meetingapps.meetingclient.meetingClientOberflaechen.MeetingGridPane;
import de.meetingapps.meetingportal.meetComAllg.CaBug;
import de.meetingapps.meetingportal.meetComAllg.CaDateiWrite;
import de.meetingapps.meetingportal.meetComAllg.CaDatumZeit;
import de.meetingapps.meetingportal.meetComAllg.CaFehler;
import de.meetingapps.meetingportal.meetComBl.BlAbstimmungenWeisungen;
import de.meetingapps.meetingportal.meetComBl.BlNummernformBasis;
import de.meetingapps.meetingportal.meetComBl.BlSammelkarten;
import de.meetingapps.meetingportal.meetComDb.DbBundle;
import de.meetingapps.meetingportal.meetComEntities.EclBesitzAREintrag;
import de.meetingapps.meetingportal.meetComEntities.EclBesitzJeKennung;
import de.meetingapps.meetingportal.meetComEntities.EclScan;
import de.meetingapps.meetingportal.meetComEntities.EclTLoginDatenM;
import de.meetingapps.meetingportal.meetComEntities.EclWillenserklaerungStatusNeu;
import de.meetingapps.meetingportal.meetComEntities.EclZugeordneteMeldungNeu;
import de.meetingapps.meetingportal.meetComHVParam.ParamS;
import de.meetingapps.meetingportal.meetComKonst.KonstKartenklasse;
import de.meetingapps.meetingportal.meetComKonst.KonstPortalAktion;
import de.meetingapps.meetingportal.meetComKonst.KonstSkIst;
import de.meetingapps.meetingportal.meetComKonst.KonstWillenserklaerung;
import de.meetingapps.meetingportal.meetComStub.WSClient;
import de.meetingapps.meetingportal.meetComWE.WELogin;
import de.meetingapps.meetingportal.meetComWE.WELoginCheck;
import de.meetingapps.meetingportal.meetComWE.WELoginCheckRC;
import de.meetingapps.meetingportal.meetComWE.WELoginVerify;
import de.meetingapps.meetingportal.meetComWE.WETeilnehmerStatusGetRC;
import de.meetingapps.meetingportal.meetComWE.WEWeisungAendernGet;
import de.meetingapps.meetingportal.meetComWE.WEWeisungAendernGetRC;
import de.meetingapps.meetingportal.meetComWE.WEWeisungAendernVorbereitenGet;
import de.meetingapps.meetingportal.meetComWE.WEWeisungAendernVorbereitenGetRC;
import de.meetingapps.meetingportal.meetComWE.WEWeisungErteilen;
import de.meetingapps.meetingportal.meetComWE.WEWeisungErteilenRC;
import javafx.application.Platform;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

/*TODO: Bestandsverwaltung, KIAV: Funktion "gemäß Weisungsvorschlag" aktuell nicht implementiert!*/

/**
 * The Class CtrlVollmachtWeisung.
 */
public class CtrlVollmachtWeisung extends CtrlRootWeisungenNeu {

    /** The resources. */
    @FXML
    private ResourceBundle resources;

    /** The location. */
    @FXML
    private URL location;

    /** The lbl hinweise. */
    @FXML
    private Label lblHinweise;

    /** The tf aktionaersnummer. */
    @FXML
    private TextField tfAktionaersnummer;

    /** The tf erklaerung datum. */
    @FXML
    private TextField tfErklaerungDatum;

    /** The tf erklaerung zeit. */
    @FXML
    private TextField tfErklaerungZeit;

    /** The cb eingangs weg. */
    @FXML
    private ComboBox<String> cbEingangsWeg;

    /** The btn speichern. */
    @FXML
    private Button btnSpeichern;

    /** The btn abbruch. */
    @FXML
    private Button btnAbbruch;

    /** The tf name vorname. */
    @FXML
    private TextField tfNameVorname;

    /** The tf ort. */
    @FXML
    private TextField tfOrt;

    /** The tf aktien. */
    @FXML
    private TextField tfAktien;

    /** The btn einlesen. */
    @FXML
    private Button btnEinlesen;

    /** The tf zuletzt bearbeitet. */
    @FXML
    private TextField tfZuletztBearbeitet;

    /** The btn alle ja. */
    @FXML
    private Button btnAlleJa;

    /** The btn alle nein. */
    @FXML
    private Button btnAlleNein;

    /** The btn alle enthaltung. */
    @FXML
    private Button btnAlleEnthaltung;

    /** The btn alle nicht teilnahme. */
    @FXML
    private Button btnAlleNichtTeilnahme;

    /** The btn alle ungueltig. */
    @FXML
    private Button btnAlleUngueltig;

    /** The btn alle zuruecksetzen. */
    @FXML
    private Button btnAlleZuruecksetzen;

    /** The btn text datei lesen. */
    @FXML
    private Button btnTextDateiLesen;

    /** The btn scan lesen. */
    @FXML
    private Button btnScanLesen;

    /** The ws client. */
    private WSClient wsClient = null;

    /** ****************Individuelle Anfang****************. */

    /** Bei Scannen: Name der Quell-Datei */
    String quelle = "";

    /** The ecl T login daten M. */
    EclTLoginDatenM eclTLoginDatenM = null;

    /** The ecl zugeordnete meldung. */
    EclZugeordneteMeldungNeu eclZugeordneteMeldung = null;

    /** The ecl willenserklaerung status. */
    EclWillenserklaerungStatusNeu eclWillenserklaerungStatus = null;

    /** The list login daten. */
    public List<EclTLoginDatenM> listLoginDaten = null;

    /** The list meldung. */
    public List<EclZugeordneteMeldungNeu> listMeldung = null;

    /**
     * ***************Individuell Ende***********************.
     */

    @FXML
    void initialize() {
        assert tfAktionaersnummer != null
                : "fx:id=\"tfAktionaersnummer\" was not injected: check your FXML file 'VollmachtWeisungSRV.fxml'.";
        assert tfErklaerungDatum != null
                : "fx:id=\"tfErklaerungDatum\" was not injected: check your FXML file 'VollmachtWeisungSRV.fxml'.";
        assert tfErklaerungZeit != null
                : "fx:id=\"tfErklaerungZeit\" was not injected: check your FXML file 'VollmachtWeisungSRV.fxml'.";
        assert cbEingangsWeg != null
                : "fx:id=\"cbEingangsWeg\" was not injected: check your FXML file 'VollmachtWeisungSRV.fxml'.";
        assert btnSpeichern != null
                : "fx:id=\"btnSpeichern\" was not injected: check your FXML file 'VollmachtWeisungSRV.fxml'.";
        assert btnAbbruch != null
                : "fx:id=\"btnAbbruch\" was not injected: check your FXML file 'VollmachtWeisungSRV.fxml'.";
        assert tfNameVorname != null
                : "fx:id=\"tfNameVorname\" was not injected: check your FXML file 'VollmachtWeisungSRV.fxml'.";
        assert tfOrt != null : "fx:id=\"tfOrt\" was not injected: check your FXML file 'VollmachtWeisungSRV.fxml'.";
        assert tfAktien != null
                : "fx:id=\"tfAktien\" was not injected: check your FXML file 'VollmachtWeisungSRV.fxml'.";
        assert btnEinlesen != null
                : "fx:id=\"btnEinlesen\" was not injected: check your FXML file 'VollmachtWeisungSRV.fxml'.";
        assert tfZuletztBearbeitet != null
                : "fx:id=\"tfZuletztBearbeitet\" was not injected: check your FXML file 'VollmachtWeisungSRV.fxml'.";
        assert scrpnWeisungen != null
                : "fx:id=\"scrpnWeisungen\" was not injected: check your FXML file 'VollmachtWeisungSRV.fxml'.";
        assert cbKIAV != null : "fx:id=\"cbKIAV\" was not injected: check your FXML file 'VollmachtWeisungKIAV.fxml'.";
        assert btnAlleJa != null
                : "fx:id=\"btnAlleJa\" was not injected: check your FXML file 'VollmachtWeisungSRV.fxml'.";
        assert btnAlleNein != null
                : "fx:id=\"btnAlleNein\" was not injected: check your FXML file 'VollmachtWeisungSRV.fxml'.";
        assert btnAlleEnthaltung != null
                : "fx:id=\"btnAlleEnthaltung\" was not injected: check your FXML file 'VollmachtWeisungSRV.fxml'.";
        assert btnAlleUngueltig != null
                : "fx:id=\"btnAlleUngueltig\" was not injected: check your FXML file 'VollmachtWeisungSRV.fxml'.";
        assert btnAlleZuruecksetzen != null
                : "fx:id=\"btnAlleZuruecksetzen\" was not injected: check your FXML file 'VollmachtWeisungSRV.fxml'.";
        assert btnTextDateiLesen != null
                : "fx:id=\"btnTextDateiLesen\" was not injected: check your FXML file 'VollmachtWeisungKIAV.fxml'.";
        assert btnScanLesen != null
                : "fx:id=\"btnScanLesen\" was not injected: check your FXML file 'VollmachtWeisungKIAV.fxml'.";

        /*************** Ab hier individuell **********************************/

        switch (ausgewaehlteFunktion) {
        case KonstPortalAktion.SRV_NEU:
        case KonstPortalAktion.SRV_AENDERN:
            skIst = KonstSkIst.srv;
            break;
        case KonstPortalAktion.BRIEFWAHL_NEU:
        case KonstPortalAktion.BRIEFWAHL_AENDERN:
            skIst = KonstSkIst.briefwahl;
            break;
        case KonstPortalAktion.KIAV_MIT_WEISUNG_NEU:
        case KonstPortalAktion.KIAV_WEISUNG_AENDERN:
            skIst = KonstSkIst.kiav;
            btnScanLesen.setVisible(false);
            break;
        case KonstPortalAktion.DAUERVOLLMACHT_MIT_WEISUNG_NEU:
        case KonstPortalAktion.DAUERVOLLMACHT_AENDERN:
            skIst = KonstSkIst.dauervollmacht;
            btnScanLesen.setVisible(false);
            break;
        case KonstPortalAktion.ORGANISATORISCH_MIT_WEISUNG_NEU:
        case KonstPortalAktion.ORGANISATORISCH_AENDERN:
            skIst = KonstSkIst.organisatorisch;
            btnScanLesen.setVisible(false);
            break;
        }

        setzeStatusAktionaersnummerEingeben();
        tfZuletztBearbeitet.setText("");

        lDbBundle = new DbBundle();

        blAbstimmungenWeisungen = new BlAbstimmungenWeisungen(false, lDbBundle);
        blAbstimmungenWeisungen.leseAgendaFuerInterneWeisungenErfassung();

        blSammelkarten = new BlSammelkarten(false, lDbBundle);
        blSammelkarten.leseSammelkartenlisteFuerWeisungen(skIst, 2);

        if (ausgewaehlteFunktion == KonstPortalAktion.SRV_AENDERN
                || ausgewaehlteFunktion == KonstPortalAktion.BRIEFWAHL_AENDERN
                || ausgewaehlteFunktion == KonstPortalAktion.KIAV_WEISUNG_AENDERN
                || ausgewaehlteFunktion == KonstPortalAktion.DAUERVOLLMACHT_AENDERN
                || ausgewaehlteFunktion == KonstPortalAktion.ORGANISATORISCH_AENDERN) {
            /*Dann: Aufgerufen im Änderungsmodus. Bisherige Weisung aufrufen und anzeigen*/
            /*Stornieren vorbereiten holen*/
            WSClient wsClient = new WSClient();

            WELoginVerify weLoginVerify = wsClient.fuelleLoginVerify(eclTLoginDatenM);

            weLoginVerify.setEingabeQuelle(1); /*Siehe EclWillenserklaerung - erteiltAufWeg*/
            weLoginVerify.setErteiltZeitpunkt("");
            WEWeisungAendernVorbereitenGet weWeisungAendernVorbereitenGet = new WEWeisungAendernVorbereitenGet();
            weWeisungAendernVorbereitenGet.setWeLoginVerify(weLoginVerify);
            weWeisungAendernVorbereitenGet.setAusgewaehlteHauptAktion(KonstPortalAktion.HAUPT_BEREITSANGEMELDET);
            weWeisungAendernVorbereitenGet.setAusgewaehlteAktion(ausgewaehlteFunktion);

            weWeisungAendernVorbereitenGet.setZugeordneteMeldung(eclZugeordneteMeldung);
            weWeisungAendernVorbereitenGet.setEclWillenserklaerungStatus(eclWillenserklaerungStatus);
            WEWeisungAendernVorbereitenGetRC weWeisungAendernVorbereitenGetRC = wsClient
                    .weisungAendernVorbereitenGet(weWeisungAendernVorbereitenGet);

            meldungsIdent = weWeisungAendernVorbereitenGetRC.getMeldungsIdent();
            willenserklaerungIdent = weWeisungAendernVorbereitenGetRC.getWillenserklaerungIdent();
            weisungsIdent = weWeisungAendernVorbereitenGetRC.getWeisungsIdent();

            blAbstimmungenWeisungen.initWeisungMeldung(1);
            blAbstimmungenWeisungen.rcWeisungMeldung[0] = weWeisungAendernVorbereitenGetRC.getWeisungMeldung();

            aktuelleGattung = eclTLoginDatenM.eclAktienregister.getGattungId();
            blSammelkarten.rcSammelkartenFuerWeisungserfassungAusgewaehlteMeldeIdent[aktuelleGattung] = weWeisungAendernVorbereitenGetRC
                    .getSammelIdent();
            System.out.println("blSammelkarten.rcSammelkartenFuerWeisungserfassungAusgewaehlteMeldeIdent="
                    + blSammelkarten.rcSammelkartenFuerWeisungserfassungAusgewaehlteMeldeIdent[aktuelleGattung]);

            hauptFunktion = KonstPortalAktion.HAUPT_AENDERN;
        }

        /*Eingangsweg setzen*/
        SClErfassungsDaten.initErfassungsfelder(tfErklaerungDatum, tfErklaerungZeit, cbEingangsWeg);

        if (this.hauptFunktion == KonstPortalAktion.HAUPT_BEREITSANGEMELDET
                || this.hauptFunktion == KonstPortalAktion.HAUPT_AENDERN) {
            /*zu bestehender Anmeldung zusätzliche Willenserklärung - Ändern oder Neu*/
            tfAktionaersnummer.setText(eclTLoginDatenM.eclAktienregister.aktionaersnummer);
            aktuelleGattung = eclTLoginDatenM.eclAktienregister.getGattungId();
            boolean brc = bereiteEingabefelderVor();
            if (brc == false) {
                eigeneStage.hide();
                return;
            }
            if (hauptFunktion == 3) { /*Im Ändern-Modus Sammelkartenauswahl sperren*/
                ComboBoxZusatz.sperreCbElement(cbKIAV);
            }
        } else {
            setzeStatusAktionaersnummerEingeben();
        }
    }

    /**
     * Cb KIAV changed.
     *
     * @param event the event
     */
    @FXML
    void cbKIAVChanged(ActionEvent event) {
        kiavChanged();
    }

    /**
     * On key aktionaersnummer.
     *
     * @param event the event
     */
    @FXML
    void onKeyAktionaersnummer(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            doEinlesen();
        }
    }

    /**
     * Clicked einlesen.
     *
     * @param event the event
     */
    @FXML
    void clickedEinlesen(ActionEvent event) {
        doEinlesen();
    }

    /**
     * Do einlesen.
     */
    private void doEinlesen() {

        wsClient = null;

        BlNummernformBasis blNummernformAktionaersnummer = new BlNummernformBasis(lDbBundle);
        boolean rc = einlesen(
                blNummernformAktionaersnummer.loginKennungAufbereitenFuerIntern(tfAktionaersnummer.getText()));

        if (rc == false) {
            fehlerMeldung(fehlerMeldungSpeichern);
            return;
        } else {
            boolean brc = bereiteEingabefelderVor();
            if (brc == false) {
                setzeStatusAktionaersnummerEingeben();
                return;
            }

            setzeStatusVerarbeitung();
        }
    }

    /**
     * Bereite eingabefelder vor.
     *
     * @return true, if successful
     */
    private boolean bereiteEingabefelderVor() {
        tfNameVorname.setText(eclTLoginDatenM.liefereVornameNameTitel());
        tfOrt.setText(eclTLoginDatenM.ort);

        if (this.hauptFunktion == KonstPortalAktion.HAUPT_BEREITSANGEMELDET
                || this.hauptFunktion == KonstPortalAktion.HAUPT_AENDERN) {
            tfAktien.setText(eclZugeordneteMeldung.aktionaerStimmenDE);
        } else {
            tfAktien.setText(eclTLoginDatenM.stimmenDE);
        }

        int vorbelegenStimmart = 0;
        switch (skIst) {
        case KonstSkIst.srv:
            vorbelegenStimmart = ParamS.param.paramAbstimmungParameter.weisungVorbelegungMitSRV;
            break;
        case KonstSkIst.briefwahl:
            vorbelegenStimmart = ParamS.param.paramAbstimmungParameter.weisungVorbelegungMitBriefwahl;
            break;
        case KonstSkIst.kiav:
            vorbelegenStimmart = ParamS.param.paramAbstimmungParameter.weisungVorbelegungMitKIAV;
            break;
        case KonstSkIst.dauervollmacht:
            vorbelegenStimmart = ParamS.param.paramAbstimmungParameter.weisungVorbelegungMitDauer;
            break;
        case KonstSkIst.organisatorisch:
            vorbelegenStimmart = ParamS.param.paramAbstimmungParameter.weisungVorbelegungMitOrg;
            break;
        }
        zeigeAbstimmungFuerGattungAnNeu(vorbelegenStimmart, vorbelegenStimmart, false);

        boolean brc = zeigeKIAVFuerGattungAnNeu(false);
        if (brc == false) {
            setzeStatusAktionaersnummerEingeben();
            return false;
        }

        setzeStatusVerarbeitung();
        return true;

    }

    /**
     * Einlesen.
     *
     * @param pAktionaersnummer the aktionaersnummer
     * @return true, if successful
     */
    private boolean einlesen(String pAktionaersnummer) {

        WETeilnehmerStatusGetRC weTeilnehmerStatusGetRC;

        if (wsClient == null) {
            wsClient = new WSClient();
        }

        WELogin weLogin = new WELogin();

        /***********************
         * Aktionärsdaten grundsätzlich holen / überprüfen auf Existenz
         ************/
        /*Hinweise: 
         * > mandant wird automatisch gesetzt aus ClGlobalVar.mandant - diese vorher belegen!
         * > user, uKennung, uPasswort werden automatisch gesetzt
         * 
         */
        weLogin.setKennungArt(1); /* kennung enthält Aktionärsnummer*/
        if (pAktionaersnummer.isEmpty()) {
            fehlerMeldungSpeichern = "Bitte Aktionärsnummer eingeben!";
            return false;
        }

        if (tfAktionaersnummer.getText().startsWith("S")) {
            fehlerMeldungSpeichern = "Bitte Aktionärsnummer eingeben!";
            return false;
        }

        DbBundle lDbBundle = new DbBundle();
        BlNummernformBasis blNummernformBasis = new BlNummernformBasis(lDbBundle);
        pAktionaersnummer = blNummernformBasis.loginKennungAufbereitenFuerIntern(tfAktionaersnummer.getText());

        weLogin.setKennung(pAktionaersnummer);

        /*Die Rückgegebenen Aktionärsdaten speichern, da sie für weitere Aktionen
         * benötigt werden
         */
        WELoginCheck weLoginCheck = new WELoginCheck();
        weLoginCheck.weLogin = weLogin;

        WELoginCheckRC weLoginCheckRC = null;

        weLoginCheckRC = wsClient.loginCheck(weLoginCheck);
        eclTLoginDatenM = weLoginCheckRC.eclTLoginDatenM;

        int rc = weLoginCheckRC.rc;
        if (rc < 1) { /*Fehlerbehandlung*/
            switch (rc) {
            case CaFehler.afKennungUnbekannt: {
                fehlerMeldungSpeichern = "Aktionärsnummer gibt es nicht!";
                break;
            }
            default: {
                fehlerMeldungSpeichern = "Programmierer verständigen! Fehler " + rc + " "
                        + CaFehler.getFehlertext(rc, 0);
                break;
            }
            }
            return false;
        }

        if (weLoginCheckRC.anzahlAktionaersnummernVorhanden > 1) {
            lblHinweise.setText("Achtung - mehrere Registereinträge zu dieser Aktionärsnummer");
        }

        aktuelleGattung = eclTLoginDatenM.eclAktienregister.getGattungId();

        /**************************
         * Aktionärs-Status holen
         *****************************************/

        WELoginVerify weLoginVerify = wsClient.fuelleLoginVerify(eclTLoginDatenM);

        weTeilnehmerStatusGetRC = wsClient.teilnehmerStatusGet(weLoginVerify);
        rc = weTeilnehmerStatusGetRC.getRc();
        if (rc < 1) { /*Fehlerbehandlung*/
            switch (rc) {
            default: {
                fehlerMeldungSpeichern = "Programmierer verständigen! Fehler " + rc + " "
                        + CaFehler.getFehlertext(rc, 0);
                break;
            }
            }
            return false;
        }

        if (pruefeObSchonAngemeldet(weTeilnehmerStatusGetRC)) {
            fehlerMeldungSpeichern = "Aktionär ist bereits zur HV angemeldet - Weisungserteilung mit dieser Funktion nicht möglich!";
            return false;
        }

        return true;
    }

    /**
     * Pruefe ob schon angemeldet.
     *
     * @param weTeilnehmerStatusGetRC the we teilnehmer status get RC
     * @return true, if successful
     */
    private boolean pruefeObSchonAngemeldet(WETeilnehmerStatusGetRC weTeilnehmerStatusGetRC) {
        if (weTeilnehmerStatusGetRC.besitzJeKennungListe == null) {
            CaBug.drucke("001");
            return false;
        }
        if (weTeilnehmerStatusGetRC.besitzJeKennungListe.size() < 1) {
            CaBug.drucke("002");
            return false;
        }
        EclBesitzJeKennung eclBesitzJeKennung = weTeilnehmerStatusGetRC.besitzJeKennungListe.get(0);

        if (eclBesitzJeKennung.eigenerAREintragVorhanden == false) {
            CaBug.drucke("003");
            return false;
        }
        if (eclBesitzJeKennung.eigenerAREintragListe.size() < 1) {
            CaBug.drucke("004");
            return false;
        }

        EclBesitzAREintrag eclBesitzAREintrag = eclBesitzJeKennung.eigenerAREintragListe.get(0);
        return eclBesitzAREintrag.angemeldet;
    }

    /**
     * Clicked abbruch.
     *
     * @param event the event
     */
    @FXML
    void clickedAbbruch(ActionEvent event) {
        setzeStatusAktionaersnummerEingeben();
        if (hauptFunktion == KonstPortalAktion.HAUPT_BEREITSANGEMELDET
                || hauptFunktion == KonstPortalAktion.HAUPT_AENDERN) {
            eigeneStage.hide();
        }
    }

    /**
     * Pruefe allgemeine eingaben.
     *
     * @return true, if successful
     */
    private boolean pruefeAllgemeineEingaben() {
        String fehlerString = SClErfassungsDaten.pruefeEingaben(tfErklaerungDatum, tfErklaerungZeit);
        if (fehlerString != null) {
            fehlerMeldung(fehlerString);
            return false;
        }

        if (blSammelkarten.rcSammelkartenFuerWeisungserfassungAusgewaehlteMeldeIdent[aktuelleGattung] == -1) {
            fehlerMeldung("Bitte Sammelkarte auswählen!");
            return false;
        }
        return true;
    }

    /**
     * Clicked speichern.
     *
     * @param event the event
     */
    @FXML
    void clickedSpeichern(ActionEvent event) {

        wsClient = null;

        if (listLoginDaten != null) {

            for (int i = 0; i < listLoginDaten.size(); i++) {
                eclTLoginDatenM = listLoginDaten.get(i);
                eclZugeordneteMeldung = listMeldung.get(i);

                if (pruefeAllgemeineEingaben() == false) {
                    return;
                }
                if (pruefeWeisungenVollstaendig() == false) {
                    return;
                }

                boolean rc = speichern(false);
                if (rc == false) {
                    fehlerMeldung(fehlerMeldungSpeichern);
                    return;
                }

            }

            if (hauptFunktion == 2 || hauptFunktion == 3) {
                eigeneStage.hide();
            }

            setzeStatusAktionaersnummerEingeben();
            tfZuletztBearbeitet.setText(
                    eclTLoginDatenM.anmeldeKennungFuerAnzeige + " " + eclTLoginDatenM.liefereVornameNameTitel());

            return;

        } else {

            if (pruefeAllgemeineEingaben() == false) {
                return;
            }
            if (pruefeWeisungenVollstaendig() == false) {
                return;
            }

            boolean rc = speichern(false);
            if (rc == false) {
                fehlerMeldung(fehlerMeldungSpeichern);
                return;
            }

            if (hauptFunktion == 2 || hauptFunktion == 3) {
                eigeneStage.hide();
            }

            setzeStatusAktionaersnummerEingeben();
            tfZuletztBearbeitet.setText(
                    eclTLoginDatenM.anmeldeKennungFuerAnzeige + " " + eclTLoginDatenM.liefereVornameNameTitel());

            return;
        }

    }

    /**
     * Enter in weisung gedrueckt.
     */
    protected void enterInWeisungGedrueckt() {
        System.out.println("Enter gedrückt");
    }

    /** The fehler meldung speichern. */
    private String fehlerMeldungSpeichern = "";

    /**
     * pScanning=true => Verarbeitung eines Scans, d.h. Weisungen sind bereits
     * vorbereitet
     *
     * @param pScanning the scanning
     * @return true, if successful
     */
    private boolean speichern(boolean pScanning) {

        if (pScanning == false) {
            blAbstimmungenWeisungen.initWeisungMeldung(1);

            weisungseingabenSpeichernDirekt();

            if (pruefeGruppen(0, aktuelleGattung, false) == false) {
                fehlerMeldungSpeichern = "Abstimmung bei Gruppe so nicht zulässig- bitte manuell korrigieren";
                return false;
            }
        }

        int rc = 0;
        if (wsClient == null) {
            wsClient = new WSClient();
        }

        WELoginVerify weLoginVerify = wsClient.fuelleLoginVerify(eclTLoginDatenM);

        SClErfassungsDaten.fuelleLoginVerify(weLoginVerify, tfErklaerungDatum, tfErklaerungZeit, cbEingangsWeg);

        if (hauptFunktion == KonstPortalAktion.HAUPT_NEUANMELDUNG
                || hauptFunktion == KonstPortalAktion.HAUPT_BEREITSANGEMELDET) { /*Erteilen*/

            WEWeisungErteilen weWeisungErteilen = new WEWeisungErteilen();
            weWeisungErteilen.setWeLoginVerify(weLoginVerify);

            weWeisungErteilen.setAusgewaehlteHauptAktion(hauptFunktion);

            switch (ausgewaehlteFunktion) {
            case KonstPortalAktion.SRV_NEU: { /*Vollmacht/Weisung SRV*/
                weWeisungErteilen.setAusgewaehlteAktion(KonstPortalAktion.SRV_NEU);
                break;
            }
            case KonstPortalAktion.BRIEFWAHL_NEU: { /*Briefwahl*/
                weWeisungErteilen.setAusgewaehlteAktion(KonstPortalAktion.BRIEFWAHL_NEU);
                break;
            }
            case KonstPortalAktion.KIAV_MIT_WEISUNG_NEU: {/*KIAV*/
                switch (ausgewaehlteWeisungsart) {
                case 1: {
                    weWeisungErteilen.setAusgewaehlteAktion(KonstPortalAktion.KIAV_NUR_VOLLMACHT_NEU);
                    break;
                }
                case 2: {
                    weWeisungErteilen.setAusgewaehlteAktion(KonstPortalAktion.KIAV_VOLLMACHT_UND_WEISUNG_DEDIZIERT_NEU);
                    break;
                }
                case 3: {
                    weWeisungErteilen
                            .setAusgewaehlteAktion(KonstPortalAktion.KIAV_VOLLMACHT_UND_WEISUNG_GEMAESS_VORSCHLAG_NEU);
                    break;
                }
                }
                break;
            }
            case KonstPortalAktion.DAUERVOLLMACHT_MIT_WEISUNG_NEU: {/*Dauervollmacht*/
                switch (ausgewaehlteWeisungsart) {
                case 1: {
                    weWeisungErteilen.setAusgewaehlteAktion(KonstPortalAktion.DAUERVOLLMACHT_NUR_VOLLMACHT_NEU);
                    break;
                }
                case 2: {
                    weWeisungErteilen.setAusgewaehlteAktion(
                            KonstPortalAktion.DAUERVOLLMACHT_VOLLMACHT_UND_WEISUNG_DEDIZIERT_NEU);
                    break;
                }
                case 3: {
                    weWeisungErteilen.setAusgewaehlteAktion(
                            KonstPortalAktion.DAUERVOLLMACHT_VOLLMACHT_UND_WEISUNG_GEMAESS_VORSCHLAG_NEU);
                    break;
                }
                }
                break;
            }
            case KonstPortalAktion.ORGANISATORISCH_MIT_WEISUNG_NEU: {/*Orga*/
                switch (ausgewaehlteWeisungsart) {
                case 1: {
                    weWeisungErteilen.setAusgewaehlteAktion(KonstPortalAktion.ORGANISATORISCH_NUR_VOLLMACHT_NEU);
                    break;
                }
                case 2: {
                    weWeisungErteilen.setAusgewaehlteAktion(
                            KonstPortalAktion.ORGANISATORISCH_VOLLMACHT_UND_WEISUNG_DEDIZIERT_NEU);
                    break;
                }
                case 3: {
                    weWeisungErteilen.setAusgewaehlteAktion(
                            KonstPortalAktion.ORGANISATORISCH_VOLLMACHT_UND_WEISUNG_GEMAESS_VORSCHLAG_NEU);
                    break;
                }
                }
                break;
            }
            }

            weWeisungErteilen.setAnmeldeAktionaersnummer(eclTLoginDatenM.eclAktienregister.aktionaersnummer);
            weWeisungErteilen.setEclZugeordneteMeldung(eclZugeordneteMeldung);

            weWeisungErteilen.setWeisungMeldung(blAbstimmungenWeisungen.rcWeisungMeldung[0]);
            weWeisungErteilen.setWeisungMeldungRaw(blAbstimmungenWeisungen.rcWeisungMeldungRaw[0]);

            weWeisungErteilen.setQuelle(quelle);

            weWeisungErteilen.setSammelIdent(
                    blSammelkarten.rcSammelkartenFuerWeisungserfassungAusgewaehlteMeldeIdent[aktuelleGattung]);

            WEWeisungErteilenRC weWeisungErteilenRC = wsClient.weisungErteilen(weWeisungErteilen);

            rc = weWeisungErteilenRC.getRc();
        } else {/*Ändern*/
            WEWeisungAendernGet weWeisungAendernGet = new WEWeisungAendernGet();
            weWeisungAendernGet.setWeLoginVerify(weLoginVerify);
            weWeisungAendernGet.ausgewaehlteHauptAktion = KonstPortalAktion.HAUPT_BEREITSANGEMELDET;
            weWeisungAendernGet.ausgewaehlteAktion = ausgewaehlteFunktion;
            weWeisungAendernGet.setMeldungsIdent(meldungsIdent);
            weWeisungAendernGet.setWillenserklaerungIdent(willenserklaerungIdent);
            weWeisungAendernGet.setSammelIdent(
                    blSammelkarten.rcSammelkartenFuerWeisungserfassungAusgewaehlteMeldeIdent[aktuelleGattung]);
            weWeisungAendernGet.setWeisungsIdent(weisungsIdent);
            weWeisungAendernGet.eclZugeordneteMeldung = eclZugeordneteMeldung;
            weWeisungAendernGet.eclWillenserklaerungStatus = eclWillenserklaerungStatus;
            weWeisungAendernGet.setWeisungMeldung(blAbstimmungenWeisungen.rcWeisungMeldung[0]);
            weWeisungAendernGet.setWeisungMeldungRaw(blAbstimmungenWeisungen.rcWeisungMeldungRaw[0]);

            WEWeisungAendernGetRC weWeisungAendernGetRC = wsClient.weisungAendernGet(weWeisungAendernGet);

            rc = weWeisungAendernGetRC.getRc();

        }

        if (rc < 1) { /*Fehlerbehandlung*/
            switch (rc) {
            case CaFehler.afAndererUserAktiv: {
                fehlerMeldungSpeichern = "Datensatz mittlerweile von anderem User verändert! Bitte abbrechen und erneut einlesen.";
                break;
            }
            default: {
                fehlerMeldungSpeichern = "Programmierer verständigen! Fehler " + rc + " "
                        + CaFehler.getFehlertext(rc, 0);
                break;
            }
            }
            return false;
        }

        return true;
    }

    /**
     * Ausgewaehlte kartenklasse.
     *
     * @return the int
     */
    private int ausgewaehlteKartenklasse() {
        int vorauswahl = KonstKartenklasse.aktionaersnummer;
        return vorauswahl;
    }

    /**
     * Clicked scan lesen.
     *
     * @param event the event
     */
    @FXML
    void clickedScanLesen(ActionEvent event) {

        Service<Void> service = new Service<Void>() {
            @Override
            protected Task<Void> createTask() {
                return new Task<Void>() {

                    @Override
                    protected Void call() {
                        /*************** Start-Sequenz ********************************/
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                btnScanLesen.setDisable(true);
                                btnEinlesen.setDisable(true);
                                initFehlerGrid();
                            }
                        });

                        int gesamtanzahl = 0;
                        int fehleranzahl = 0;

                        int anzahlInDatenbank = 0;

                        /*Ergebnis= aktuellerScanLauf*/
                        int[] zulaessigeKartenarten = { 0 };
                        int willenserklaerung = 0;
                        String ausgabeText = "";

                        if (ausgewaehlteFunktion == 4) {
                            willenserklaerung = KonstWillenserklaerung.vollmachtUndWeisungAnSRV;
                            ausgabeText = "Anmelden und Weisung SRV";
                        } else {
                            willenserklaerung = KonstWillenserklaerung.briefwahl;
                            ausgabeText = "Anmelden und Briefwahl";
                        }
                        EclScan[] scanTable = blAbstimmungenWeisungen.scanDateiInit(willenserklaerung,
                                ausgewaehlteKartenklasse(), zulaessigeKartenarten,
                                blSammelkarten.rcSammelkartenFuerWeisungserfassungMeldeIdentFuerStandard);
                        anzahlInDatenbank = scanTable.length;

                        CaDateiWrite protokollDatei = null;
                        protokollDatei = new CaDateiWrite();
                        protokollDatei.oeffne(lDbBundle, "weisungenScannen");
                        protokollDatei.ausgabe(CaDatumZeit.DatumZeitStringFuerDatenbank());
                        protokollDatei.newline();

                        protokollDatei.ausgabe("Scan-Datei : Datenbank");
                        protokollDatei.newline();

                        protokollDatei.ausgabe("Funktion :" + ausgabeText);
                        protokollDatei.newline();

                        System.out.println("Vor Verarbeitung");
                        /*********************** Verarbeitung **************************/
                        for (int i = 0; i < anzahlInDatenbank; i++) {
                            fehlerMeldungSpeichern = "";
                            gesamtanzahl++;
                            EclScan[] aktuellerScan = new EclScan[1];
                            aktuellerScan[0] = scanTable[i];

                            boolean bRc = einlesen(aktuellerScan[0].barcode);
                            if (bRc == false) { /*Aktionärsnummer nicht verarbeitbar*/
                                protokollDatei.ausgabe(
                                        "Fehler bei " + aktuellerScan[0].barcode + " :" + fehlerMeldungSpeichern);
                                protokollDatei.newline();
                                fehleranzahl++;

                            } else {/*Aktionärsnummer verarbeitbar*/
                                blAbstimmungenWeisungen.initWeisungMeldung(1);
                                blAbstimmungenWeisungen.uebertrageScanSatzInWeisungMeldung(aktuelleGattung,
                                        aktuellerScan[0], 0, -9999);
                                boolean rc = speichern(false);
                                if (rc == false) {
                                    protokollDatei.ausgabe(
                                            "Fehler bei " + aktuellerScan[0].barcode + " :" + fehlerMeldungSpeichern);
                                    protokollDatei.newline();
                                    fehleranzahl++;
                                }

                            }
                            Platform.runLater(new Runnable() {
                                @Override
                                public void run() {
                                    eintragInFehlerGrid(aktuellerScan[0].barcode, fehlerMeldungSpeichern);
                                }
                            });

                        }

                        //   	               	   		System.out.println("Ende-Verarbeitung");

                        /************** EndeSequenz *****************************************/

                        protokollDatei.ausgabe(CaDatumZeit.DatumZeitStringFuerDatenbank());
                        protokollDatei.newline();
                        protokollDatei
                                .ausgabe("Anzahl verarbeiteter Sätze insgesamt=" + Integer.toString(gesamtanzahl));
                        protokollDatei.newline();

                        protokollDatei.ausgabe("Anzahl Fehler=" + Integer.toString(fehleranzahl));
                        protokollDatei.newline();
                        protokollDatei.schliessen();

                        final int gesamtanzahlFuerRunLater = gesamtanzahl;
                        final int fehleranzahlFuerRunLater = fehleranzahl;
                        final String protokollDateinameFuerRunLater = protokollDatei.dateiname;

                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                eintragInFehlerGrid("Fertig",
                                        "Anzahl gelesen: " + Integer.toString(gesamtanzahlFuerRunLater)
                                                + " Anzahl Fehler: " + Integer.toString(fehleranzahlFuerRunLater));
                                eintragInFehlerGrid("", " Protokoll in " + protokollDateinameFuerRunLater);
                                btnScanLesen.setDisable(false);
                                btnEinlesen.setDisable(false);
                            }
                        });

                        return null;
                    }
                };
            }

        };
        service.start();

        return;
    }

    /**
     * Setze status aktionaersnummer eingeben.
     */
    private void setzeStatusAktionaersnummerEingeben() {
        btnEinlesen.setDisable(false);
        btnSpeichern.setDisable(true);
        btnAbbruch.setDisable(true);
        btnScanLesen.setDisable(false);

        btnAlleJa.setVisible(false);
        btnAlleNein.setVisible(false);
        btnAlleEnthaltung.setVisible(false);
        btnAlleNichtTeilnahme.setVisible(false);
        btnAlleUngueltig.setVisible(false);
        btnAlleZuruecksetzen.setVisible(false);

        tfAktionaersnummer.setText("");
        tfAktionaersnummer.setEditable(true);

        tfNameVorname.setText("");
        tfOrt.setText("");
        tfAktien.setText("");
        lblHinweise.setText("");

        tfAktionaersnummer.requestFocus();

        grpnWeisungen = new MeetingGridPane();
        scrpnWeisungen.setContent(grpnWeisungen);
    }

    /**
     * Zeige gesamt buttons.
     */
    @Override
    protected void zeigeGesamtButtons() {
        if (!scrpnWeisungen.isVisible()) {
            verbergeGesamtButtons();
            return;
        }
        if (buttonAlleJaAktivieren) {
            btnAlleJa.setVisible(true);
        }
        if (buttonAlleNeinAktivieren) {
            btnAlleNein.setVisible(true);
        }
        if (buttonAlleEnthaltungAktivieren) {
            btnAlleEnthaltung.setVisible(true);
        }
        if (buttonAlleNichtTeilnahmeAktivieren) {
            btnAlleNichtTeilnahme.setVisible(true);
        }
        if (buttonAlleUngueltigAktivieren) {
            btnAlleUngueltig.setVisible(true);
        }
        btnAlleZuruecksetzen.setVisible(true);
    }

    /**
     * Verberge gesamt buttons.
     */
    @Override
    protected void verbergeGesamtButtons() {
        btnAlleJa.setVisible(false);
        btnAlleNein.setVisible(false);
        btnAlleEnthaltung.setVisible(false);
        btnAlleNichtTeilnahme.setVisible(false);
        btnAlleUngueltig.setVisible(false);
        btnAlleZuruecksetzen.setVisible(false);
    }

    /**
     * Setze status verarbeitung.
     */
    private void setzeStatusVerarbeitung() {
        btnEinlesen.setDisable(true);
        btnSpeichern.setDisable(false);
        btnAbbruch.setDisable(false);
        btnScanLesen.setDisable(true);

        zeigeGesamtButtons();

        tfAktionaersnummer.setEditable(false);
    }

    /**
     * Clicked alle ja.
     *
     * @param event the event
     */
    @FXML
    void clickedAlleJa(ActionEvent event) {
        markierenAlleJa();
    }

    /**
     * Clicked alle nein.
     *
     * @param event the event
     */
    @FXML
    void clickedAlleNein(ActionEvent event) {
        markierenAlleNein();
    }

    /**
     * Clicked alle enthaltung.
     *
     * @param event the event
     */
    @FXML
    void clickedAlleEnthaltung(ActionEvent event) {
        markierenAlleEnthaltung();
    }

    /**
     * Clicked alle nicht teilnahme.
     *
     * @param event the event
     */
    @FXML
    void clickedAlleNichtTeilnahme(ActionEvent event) {
        markierenAlleNichtTeilnahme();
    }

    /**
     * Clicked alle ungueltig.
     *
     * @param event the event
     */
    @FXML
    void clickedAlleUngueltig(ActionEvent event) {
        markierenAlleUngueltig();
    }

    /**
     * Clicked alle zuruecksetzen.
     *
     * @param event the event
     */
    @FXML
    void clickedAlleZuruecksetzen(ActionEvent event) {
        markierenAlleZuruecksetzen();
    }

    /** The meldungs ident. */
    /*Für Änderungsmodus*/
    private int meldungsIdent = 0;

    /** The willenserklaerung ident. */
    private int willenserklaerungIdent = 0;

    /** The weisungs ident. */
    private int weisungsIdent = 0;

    /**
     * pFunktion: siehe CtrlRootWeisungen -> ausgewaehlteFunktion
     * 
     * pHauptFunktion: siehe CtrlRootWeisungen -> hauptFunktion.
     *
     * @param pEigeneStage             the eigene stage
     * @param pFunktion                the funktion
     * @param pHauptFunktion           the haupt funktion
     * @param pEclTLoginDaten          the ecl T login daten
     * @param pEclZugeordneteMeldung   the ecl zugeordnete meldung
     * @param lWillenserklaerungStatus the l willenserklaerung status
     */
    public void init(Stage pEigeneStage, int pFunktion, int pHauptFunktion, EclTLoginDatenM pEclTLoginDaten,
            EclZugeordneteMeldungNeu pEclZugeordneteMeldung,
            EclWillenserklaerungStatusNeu lWillenserklaerungStatus /*Nur bei Änderungsmodus erforderlich, sonst null übergeben*/
    ) {
        eigeneStage = pEigeneStage;
        ausgewaehlteFunktion = pFunktion;
        hauptFunktion = pHauptFunktion;

        eclTLoginDatenM = pEclTLoginDaten;
        //    	eclZugeordneteMeldungListeM=zugeordneteMeldungListe;
        eclZugeordneteMeldung = pEclZugeordneteMeldung;
        eclWillenserklaerungStatus = lWillenserklaerungStatus;
    }
}
