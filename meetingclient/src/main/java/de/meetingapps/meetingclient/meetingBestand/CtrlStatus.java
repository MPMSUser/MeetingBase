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

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import de.meetingapps.meetingclient.meetingClientDialoge.CaController;
import de.meetingapps.meetingclient.meetingClientDialoge.CaIcon;
import de.meetingapps.meetingclient.meetingClientDialoge.CaZeigeHinweis;
import de.meetingapps.meetingclient.meetingClientDialoge.CtrlRoot;
import de.meetingapps.meetingclient.meetingClientDialoge.CtrlSuchen;
import de.meetingapps.meetingportal.meetComAllg.CaBug;
import de.meetingapps.meetingportal.meetComAllg.CaFehler;
import de.meetingapps.meetingportal.meetComAllg.CaPDFAnzeigen;
import de.meetingapps.meetingportal.meetComAllg.CaString;
import de.meetingapps.meetingportal.meetComBl.BlAktienregister;
import de.meetingapps.meetingportal.meetComBl.BlMeldungen;
import de.meetingapps.meetingportal.meetComBl.BlNummernformBasis;
import de.meetingapps.meetingportal.meetComBl.BlWillenserklaerungStatus;
import de.meetingapps.meetingportal.meetComDb.DbBundle;
import de.meetingapps.meetingportal.meetComEntities.EclBesitzAREintrag;
import de.meetingapps.meetingportal.meetComEntities.EclBesitzJeKennung;
import de.meetingapps.meetingportal.meetComEntities.EclTLoginDatenM;
import de.meetingapps.meetingportal.meetComEntities.EclWillenserklaerung;
import de.meetingapps.meetingportal.meetComEntities.EclWillenserklaerungStatusNeu;
import de.meetingapps.meetingportal.meetComEntities.EclZugeordneteMeldungNeu;
import de.meetingapps.meetingportal.meetComHVParam.ParamS;
import de.meetingapps.meetingportal.meetComHVParam.ParamSpezial;
import de.meetingapps.meetingportal.meetComKonst.KonstPortalAktion;
import de.meetingapps.meetingportal.meetComKonst.KonstWillenserklaerung;
import de.meetingapps.meetingportal.meetComReports.RepWillenserklaerungenAktionaer;
import de.meetingapps.meetingportal.meetComStub.CInjects;
import de.meetingapps.meetingportal.meetComStub.WSClient;
import de.meetingapps.meetingportal.meetComWE.WEAktionaersStatusDetailGet;
import de.meetingapps.meetingportal.meetComWE.WEAktionaersStatusDetailGetRC;
import de.meetingapps.meetingportal.meetComWE.WEAnmeldungStornieren;
import de.meetingapps.meetingportal.meetComWE.WEAnmeldungStornierenRC;
import de.meetingapps.meetingportal.meetComWE.WEBestandAnmelden;
import de.meetingapps.meetingportal.meetComWE.WEBestandAnmeldenRC;
import de.meetingapps.meetingportal.meetComWE.WEBestandFixAendern;
import de.meetingapps.meetingportal.meetComWE.WEBestandFixAendernRC;
import de.meetingapps.meetingportal.meetComWE.WEEintrittskarteKorrigieren;
import de.meetingapps.meetingportal.meetComWE.WEEintrittskarteStornierenGet;
import de.meetingapps.meetingportal.meetComWE.WEEintrittskarteStornierenGetRC;
import de.meetingapps.meetingportal.meetComWE.WELogin;
import de.meetingapps.meetingportal.meetComWE.WELoginCheck;
import de.meetingapps.meetingportal.meetComWE.WELoginCheckRC;
import de.meetingapps.meetingportal.meetComWE.WELoginVerify;
import de.meetingapps.meetingportal.meetComWE.WEStornierenVorbereitenGet;
import de.meetingapps.meetingportal.meetComWE.WEStornierenVorbereitenGetRC;
import de.meetingapps.meetingportal.meetComWE.WETeilnehmerStatusGetRC;
import de.meetingapps.meetingportal.meetComWE.WEVollmachtDritteStornierenGet;
import de.meetingapps.meetingportal.meetComWE.WEVollmachtDritteStornierenGetRC;
import de.meetingapps.meetingportal.meetComWE.WEWeisungStornierenGet;
import de.meetingapps.meetingportal.meetComWE.WEWeisungStornierenGetRC;
import de.meetingapps.meetingportal.meetingCoreReport.RpDrucken;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SingleSelectionModel;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * The Class CtrlStatus.
 */
public class CtrlStatus extends CtrlRoot {

    /** The log drucken. */
    private int logDrucken = 10;

    /** The tf aktionaersnummer. */
    @FXML
    private TextField tfAktionaersnummer;

    /** The btn einlesen. */
    @FXML
    private Button btnEinlesen;

    /** The btn suchen. */
    @FXML
    private Button btnSuchen;

    /** The btn bearbeiten. */
    @FXML
    private Button btnBearbeiten;

    /** The cb eingangs weg. */
    @FXML
    private ComboBox<String> cbEingangsWeg;

    /** The tf zuletzt bearbeitet. */
    @FXML
    private TextField tfZuletztBearbeitet;

    /** The lbl hinweise. */
    @FXML
    private Label lblHinweise;

    /** The tf name vorname. */
    @FXML
    private TextField tfNameVorname;

    /** The tf ort. */
    @FXML
    private TextField tfOrt;

    /** The tf aktien. */
    @FXML
    private TextField tfAktien;

    /** The tf aktien angemeldet. */
    @FXML
    private TextField tfAktienAngemeldet;

    /** The tf fehlermeldung. */
    @FXML
    private TextArea tfFehlermeldung;

    /** The tp tab pane. */
    @FXML
    private TabPane tpTabPane;

    /** The tab erstanmeldung. */
    @FXML
    private Tab tabErstanmeldung;

    /** The scrpn erstanmeldung. */
    @FXML
    private ScrollPane scrpnErstanmeldung;

    /** The tab status. */
    @FXML
    private Tab tabStatus;

    /** The scrpn status. */
    @FXML
    private ScrollPane scrpnStatus;

    /** The tab weitere WK. */
    @FXML
    private Tab tabWeitereWK;

    /** The scrpn weitere willenserklaerung. */
    @FXML
    private ScrollPane scrpnWeitereWillenserklaerung;

    /** The tf erklaerung datum. */
    @FXML
    private TextField tfErklaerungDatum;

    /** The tf erklaerung zeit. */
    @FXML
    private TextField tfErklaerungZeit;

    /** The btn detail PDF. */
    @FXML
    private Button btnDetailPDF;

    /** The btn abbruch. */
    @FXML
    private Button btnAbbruch;

    /** The btn zurueck. */
    @FXML
    private Button btnZurueck;

    /** ****************Individuelle Anfang****************. */
    GridPane grpnWeisungen = null;

    /** The lbl agenda TOP. */
    Label[] lblAgendaTOP = null;

    /** The lbl gegenantraege TOP. */
    Label[] lblGegenantraegeTOP = null;

    /** The lbl agenda text. */
    Label[] lblAgendaText = null;

    /** The lbl gegenantraege text. */
    Label[] lblGegenantraegeText = null;

    /** The btn agenda zuruecksetzen. */
    Button[] btnAgendaZuruecksetzen = null;

    /** The btn gegenantraege zuruecksetzen. */
    Button[] btnGegenantraegeZuruecksetzen = null;

    /** The tg agenda TOP. */
    ToggleGroup[] tgAgendaTOP = null;

    /** The tg gegenantraege TOP. */
    ToggleGroup[] tgGegenantraegeTOP = null;

    /** The rb agenda ja. */
    RadioButton[] rbAgendaJa = null;

    /** The rb gegenantraege ja. */
    RadioButton[] rbGegenantraegeJa = null;

    /** The rb agenda nein. */
    RadioButton[] rbAgendaNein = null;

    /** The rb gegenantraege nein. */
    RadioButton[] rbGegenantraegeNein = null;

    /** The rb agenda enthaltung. */
    RadioButton[] rbAgendaEnthaltung = null;

    /** The rb gegenantraege enthaltung. */
    RadioButton[] rbGegenantraegeEnthaltung = null;

    /** The rb agenda ungueltig. */
    RadioButton[] rbAgendaUngueltig = null;

    /** The rb gegenantraege ungueltig. */
    RadioButton[] rbGegenantraegeUngueltig = null;

    /** Anzahl der Abstimmungen in Agenda. */
    int anzAgenda = 0;

    /** Anzahl der Abstimmungen in Gegenanträge. */
    int anzGegenantraege = 0;

    /** 4 = Vollmacht/Weisung an SRV 5 = Briefwahl. */
    int ausgewaehlteFunktion = 0;

    /** **Ab hier neu**. */
    /*Erstanmeldung*/
    GridPane grpnErstanmeldung = null;

    /** The btn anmelden alles. */
    Button btnAnmeldenAlles = null;

    /** The btn anmelden aufgeteilt auf 2. */
    Button btnAnmeldenAufgeteiltAuf2 = null;

    /** The btn anmelden fix. */
    Button btnAnmeldenFix = null;

    /** The tf anmelden fix. */
    TextField tfAnmeldenFix = null;

    /** The btn sperren. */
    Button btnSperren = null;

    /** The grpn status. */
    /*Status*/
    GridPane grpnStatus = null;

    /** The btn meldung weitere WK. */
    Button btnMeldungWeitereWK[] = null;

    /** The btn meldung stornieren. */
    Button btnMeldungStornieren[] = null;

    /** The btn meldung fix aendern. */
    Button btnMeldungFixAendern[] = null;

    /** The btn fix neu. */
    Button btnFixNeu = null;

    /** The tf fix neu. */
    TextField tfFixNeu = null;

    /** The btn WK stornieren. */
    List<Button> btnWKStornieren = null;

    /** The meldung stornieren. */
    List<Integer> meldungStornieren = null;

    /** The meldung WK stornieren. */
    List<Integer> meldungWKStornieren = null;

    /** The btn WK aendern. */
    List<Button> btnWKAendern = null;

    /** The meldung aendern. */
    List<Integer> meldungAendern = null;

    /** The meldung WK aendern. */
    List<Integer> meldungWKAendern = null;

    /** The btn WK nachdruck. */
    List<Button> btnWKNachdruck = null;

    /** The meldung nachdruck. */
    List<Integer> meldungNachdruck = null;

    /** The meldung WK nachdruck. */
    List<Integer> meldungWKNachdruck = null;

    /** The grpn weitere WK. */
    /*Weitere Willenserklärung*/
    GridPane grpnWeitereWK = null;

    /** The btn eine EK selbst. */
    Button btnEineEKSelbst = null;

    /** The btn eine EK vertreter. */
    Button btnEineEKVertreter = null;

    /** The btn SRV. */
    Button btnSRV = null;

    /** The btn briefwahl. */
    Button btnBriefwahl = null;

    /** The btn KIAV. */
    Button btnKIAV = null;

    /** The btn dauervollmacht. */
    Button btnDauervollmacht = null;

    /** The btn orga. */
    Button btnOrga = null;

    /** The btn vollmacht dritte. */
    Button btnVollmachtDritte = null;

    /** The ecl T login daten M. */
    EclTLoginDatenM eclTLoginDatenM = null;

    /** The we teilnehmer status get RC. */
    private WETeilnehmerStatusGetRC weTeilnehmerStatusGetRC;

    /** The ecl besitz AR eintrag. */
    EclBesitzAREintrag eclBesitzAREintrag = null;

    /** The aktionaers nummer in arbeit. */
    private String aktionaersNummerInArbeit = "";

    /** The lfd nr zugeordnete meldung. */
    private int lfdNrZugeordneteMeldung = 0;

    /** The lfd nr willenserklaerung. */
    private int lfdNrWillenserklaerung = 0;

    /** The anz registereintraege. */
    private int anzRegistereintraege = 0;

    /**
     * ***************Individuell Ende***********************.
     */

    @FXML
    void initialize() {
        assert tfAktionaersnummer != null
                : "fx:id=\"tfAktionaersnummer\" was not injected: check your FXML file 'Status.fxml'.";
        assert tfFehlermeldung != null
                : "fx:id=\"tfFehlermeldung\" was not injected: check your FXML file 'Status.fxml'.";
        assert tfErklaerungDatum != null
                : "fx:id=\"tfErklaerungDatum\" was not injected: check your FXML file 'Status.fxml'.";
        assert tfErklaerungZeit != null
                : "fx:id=\"tfErklaerungZeit\" was not injected: check your FXML file 'Status.fxml'.";
        assert cbEingangsWeg != null : "fx:id=\"cbEingangsWeg\" was not injected: check your FXML file 'Status.fxml'.";
        assert btnAbbruch != null : "fx:id=\"btnAbbruch\" was not injected: check your FXML file 'Status.fxml'.";
        assert btnZurueck != null : "fx:id=\"btnZurueck\" was not injected: check your FXML file 'Status.fxml'.";
        assert tfZuletztBearbeitet != null
                : "fx:id=\"tfZuletztBearbeitet\" was not injected: check your FXML file 'Status.fxml'.";
        assert tfNameVorname != null : "fx:id=\"tfNameVorname\" was not injected: check your FXML file 'Status.fxml'.";
        assert tfOrt != null : "fx:id=\"tfOrt\" was not injected: check your FXML file 'Status.fxml'.";
        assert tfAktien != null : "fx:id=\"tfAktien\" was not injected: check your FXML file 'Status.fxml'.";
        assert btnEinlesen != null : "fx:id=\"btnEinlesen\" was not injected: check your FXML file 'Status.fxml'.";
        assert tpTabPane != null : "fx:id=\"tpTabPane\" was not injected: check your FXML file 'Status.fxml'.";
        assert tabErstanmeldung != null
                : "fx:id=\"tabErstanmeldung\" was not injected: check your FXML file 'Status.fxml'.";
        assert scrpnErstanmeldung != null
                : "fx:id=\"scrpnErstanmeldung\" was not injected: check your FXML file 'Status.fxml'.";
        assert tabStatus != null : "fx:id=\"tabStatus\" was not injected: check your FXML file 'Status.fxml'.";
        assert scrpnStatus != null : "fx:id=\"scrpnStatus\" was not injected: check your FXML file 'Status.fxml'.";
        assert tabWeitereWK != null : "fx:id=\"tabWeitereWK\" was not injected: check your FXML file 'Status.fxml'.";
        assert scrpnWeitereWillenserklaerung != null
                : "fx:id=\"scrpnWeitereWillenserklaerung\" was not injected: check your FXML file 'Status.fxml'.";
        assert tfAktienAngemeldet != null
                : "fx:id=\"tfAktienAngemeldet\" was not injected: check your FXML file 'Status.fxml'.";
        assert btnBearbeiten != null : "fx:id=\"btnBearbeiten\" was not injected: check your FXML file 'Status.fxml'.";
        assert btnDetailPDF != null : "fx:id=\"btnDetailPDF\" was not injected: check your FXML file 'Status.fxml'.";
        assert btnSuchen != null : "fx:id=\"btnSuchen\" was not injected: check your FXML file 'Status.fxml'.";

        /*************** Ab hier individuell **********************************/
        setzeStatusAktionaersnummerEingeben();
        tfZuletztBearbeitet.setText("");

        btnZurueck.setDisable(true);

        setzeEingangsweg();

    }

    /**
     * Setze eingangsweg.
     */
    private void setzeEingangsweg() {
        /*Eingangsweg setzen*/
        SClErfassungsDaten.initErfassungsfelder(tfErklaerungDatum, tfErklaerungZeit, cbEingangsWeg);
    }

    /**
     * Clicked anmelden alles.
     *
     * @param event the event
     */
    @FXML
    void clickedAnmeldenAlles(ActionEvent event) {
        CaBug.druckeLog("CtrlStatus.clickedAnmeldenAlles", logDrucken, 10);
        CaBug.druckeLog("aktionaersNummerInArbeit=" + aktionaersNummerInArbeit, logDrucken, 10);

        String fehlerString = SClErfassungsDaten.pruefeEingaben(tfErklaerungDatum, tfErklaerungZeit);
        if (fehlerString != null) {
            tfFehlermeldung.setText(fehlerString);
            return;
        }

        WSClient wsClient = new WSClient();

        WELoginVerify weLoginVerify = wsClient.fuelleLoginVerify(eclTLoginDatenM);

        SClErfassungsDaten.fuelleLoginVerify(weLoginVerify, tfErklaerungDatum, tfErklaerungZeit, cbEingangsWeg);

        WEBestandAnmelden weBestandAnmelden = new WEBestandAnmelden();
        weBestandAnmelden.setWeLoginVerify(weLoginVerify);
        weBestandAnmelden.setAktienregisterIdent(eclBesitzAREintrag.aktienregisterEintrag.aktienregisterIdent);
        weBestandAnmelden.setAnmeldeart(1);

        WEBestandAnmeldenRC weBestandAnmeldenRC = wsClient.bestandAnmelden(weBestandAnmelden);

        int rc = weBestandAnmeldenRC.getRc();
        if (rc < 1) { /*Fehlerbehandlung*/
            switch (rc) {
            case CaFehler.afAndererUserAktiv: {
                tfFehlermeldung.setText(
                        "Datensatz mittlerweile von anderem User verändert! Bitte abbrechen und erneut einlesen.");
                break;
            }
            default: {
                tfFehlermeldung
                        .setText("Programmierer verständigen! Fehler " + rc + " " + CaFehler.getFehlertext(rc, 0));
                break;
            }
            }
            setzeStatusAbbruch();
            return;
        }

        neuEinlesenUndAnzeigen(aktionaersNummerInArbeit);
    }

    /**
     * Clicked anmelden aufgeteilt auf 2.
     *
     * @param event the event
     */
    @FXML
    void clickedAnmeldenAufgeteiltAuf2(ActionEvent event) {
        String fehlerString = SClErfassungsDaten.pruefeEingaben(tfErklaerungDatum, tfErklaerungZeit);
        if (fehlerString != null) {
            tfFehlermeldung.setText(fehlerString);
            return;
        }

        WSClient wsClient = new WSClient();

        WELoginVerify weLoginVerify = wsClient.fuelleLoginVerify(eclTLoginDatenM);

        SClErfassungsDaten.fuelleLoginVerify(weLoginVerify, tfErklaerungDatum, tfErklaerungZeit, cbEingangsWeg);

        WEBestandAnmelden weBestandAnmelden = new WEBestandAnmelden();
        weBestandAnmelden.setWeLoginVerify(weLoginVerify);
        weBestandAnmelden.setAktienregisterIdent(eclBesitzAREintrag.aktienregisterEintrag.aktienregisterIdent);
        weBestandAnmelden.setAnmeldeart(2);

        WEBestandAnmeldenRC weBestandAnmeldenRC = wsClient.bestandAnmelden(weBestandAnmelden);

        int rc = weBestandAnmeldenRC.getRc();
        if (rc < 1) { /*Fehlerbehandlung*/
            switch (rc) {
            case CaFehler.pmNichtGenuegendAktienFuerSplit: {
                tfFehlermeldung.setText("Nur mit mindestens 2 Aktien zulässig!");
                return;
            }
            case CaFehler.afAndererUserAktiv: {
                tfFehlermeldung.setText(
                        "Datensatz mittlerweile von anderem User verändert! Bitte abbrechen und erneut einlesen.");
                break;
            }
            default: {
                tfFehlermeldung
                        .setText("Programmierer verständigen! Fehler " + rc + " " + CaFehler.getFehlertext(rc, 0));
                break;
            }
            }
            setzeStatusAbbruch();
            return;
        }

        neuEinlesenUndAnzeigen(aktionaersNummerInArbeit);
    }

    /**
     * Clicked anmelden fix.
     *
     * @param event the event
     */
    @FXML
    void clickedAnmeldenFix(ActionEvent event) {
        String fehlerString = SClErfassungsDaten.pruefeEingaben(tfErklaerungDatum, tfErklaerungZeit);
        if (fehlerString != null) {
            tfFehlermeldung.setText(fehlerString);
            return;
        }

        if (!CaString.isNummern(tfAnmeldenFix.getText()) || tfAnmeldenFix.getText().isEmpty()) {
            tfFehlermeldung.setText("Bitte anzumeldende Aktienzahl eingeben!");
            return;
        }

        long aktienZahlAnmelden = Long.parseLong(tfAnmeldenFix.getText());
        if (aktienZahlAnmelden > eclBesitzAREintrag.aktienregisterEintrag.stimmen || aktienZahlAnmelden < 1) {
            tfFehlermeldung.setText("Bitte gültige Aktienzahl eingeben!");
            return;
        }

        WSClient wsClient = new WSClient();

        WELoginVerify weLoginVerify = wsClient.fuelleLoginVerify(eclTLoginDatenM);

        SClErfassungsDaten.fuelleLoginVerify(weLoginVerify, tfErklaerungDatum, tfErklaerungZeit, cbEingangsWeg);

        WEBestandAnmelden weBestandAnmelden = new WEBestandAnmelden();
        weBestandAnmelden.setWeLoginVerify(weLoginVerify);
        weBestandAnmelden.setAktienregisterIdent(eclBesitzAREintrag.aktienregisterEintrag.aktienregisterIdent);
        weBestandAnmelden.setAnmeldeart(11);
        weBestandAnmelden.setAnzahlAktienFix(aktienZahlAnmelden);

        WEBestandAnmeldenRC weBestandAnmeldenRC = wsClient.bestandAnmelden(weBestandAnmelden);

        int rc = weBestandAnmeldenRC.getRc();
        if (rc < 1) { /*Fehlerbehandlung*/
            switch (rc) {
            case CaFehler.afAndererUserAktiv: {
                tfFehlermeldung.setText(
                        "Datensatz mittlerweile von anderem User verändert! Bitte abbrechen und erneut einlesen.");
                break;
            }
            default: {
                tfFehlermeldung
                        .setText("Programmierer verständigen! Fehler " + rc + " " + CaFehler.getFehlertext(rc, 0));
                break;
            }
            }
            setzeStatusAbbruch();
            return;
        }

        neuEinlesenUndAnzeigen(aktionaersNummerInArbeit);
    }

    /**
     * Clicked weitere anmelden fix.
     *
     * @param event the event
     */
    @FXML
    void clickedWeitereAnmeldenFix(ActionEvent event) {
        String fehlerString = SClErfassungsDaten.pruefeEingaben(tfErklaerungDatum, tfErklaerungZeit);
        if (fehlerString != null) {
            tfFehlermeldung.setText(fehlerString);
            return;
        }

        if (!CaString.isNummern(tfFixNeu.getText()) || tfFixNeu.getText().isEmpty()) {
            tfFehlermeldung.setText("Bitte anzumeldende Aktienzahl eingeben!");
            return;
        }

        long aktienZahlAnmelden = Long.parseLong(tfFixNeu.getText());
        if (aktienZahlAnmelden > eclBesitzAREintrag.aktienregisterEintrag.stimmen || aktienZahlAnmelden < 1) {
            tfFehlermeldung.setText("Bitte gültige Aktienzahl eingeben!");
            return;
        }

        WSClient wsClient = new WSClient();

        WELoginVerify weLoginVerify = wsClient.fuelleLoginVerify(eclTLoginDatenM);

        SClErfassungsDaten.fuelleLoginVerify(weLoginVerify, tfErklaerungDatum, tfErklaerungZeit, cbEingangsWeg);

        WEBestandAnmelden weBestandAnmelden = new WEBestandAnmelden();
        weBestandAnmelden.setWeLoginVerify(weLoginVerify);
        weBestandAnmelden.setAktienregisterIdent(eclBesitzAREintrag.aktienregisterEintrag.aktienregisterIdent);
        weBestandAnmelden.setAnmeldeart(11);
        weBestandAnmelden.setAnzahlAktienFix(aktienZahlAnmelden);

        WEBestandAnmeldenRC weBestandAnmeldenRC = wsClient.bestandFixAnmelden(weBestandAnmelden);

        int rc = weBestandAnmeldenRC.getRc();
        if (rc < 1) { /*Fehlerbehandlung*/
            switch (rc) {
            case CaFehler.pmNichtAngemeldeterBestandZuKlein: {
                tfFehlermeldung.setText("Zu wenig Aktien noch nicht angemeldet!");
                return;
            }
            case CaFehler.afAndererUserAktiv: {
                tfFehlermeldung.setText(
                        "Datensatz mittlerweile von anderem User verändert! Bitte abbrechen und erneut einlesen.");
                break;
            }
            default: {
                tfFehlermeldung
                        .setText("Programmierer verständigen! Fehler " + rc + " " + CaFehler.getFehlertext(rc, 0));
                break;
            }
            }
            setzeStatusAbbruch();
            return;
        }

        neuEinlesenUndAnzeigen(aktionaersNummerInArbeit);
    }

    /**
     * Clicked sperren.
     */
    void clickedSperren() {

        BlAktienregister blReg = new BlAktienregister(false, new DbBundle());
        blReg.sperreAktionaer(eclTLoginDatenM.eclAktienregister);

        setzeStatusAbbruch();
        neuEinlesenUndAnzeigen(aktionaersNummerInArbeit);
    }

    /**
     * Bereite tab status.
     */
    private void bereiteTabStatus() {
        int i2;
        int zeilenSpalte1 = 0;
        int zeilenSpalte2 = 0;
        int anzahlMeldungen = 0;
        long anzahlStimmen = 0;

        grpnStatus = new GridPane();
        grpnStatus.setVgap(5);
        grpnStatus.setHgap(15);

        anzahlMeldungen = eclBesitzAREintrag.liefereAnzZugeordneteMeldungenEigeneAktien();

        btnMeldungWeitereWK = new Button[anzahlMeldungen];
        btnMeldungStornieren = new Button[anzahlMeldungen];
        btnMeldungFixAendern = new Button[anzahlMeldungen];

        btnWKStornieren = new LinkedList<Button>();
        meldungStornieren = new LinkedList<Integer>();
        meldungWKStornieren = new LinkedList<Integer>();

        btnWKAendern = new LinkedList<Button>();
        meldungAendern = new LinkedList<Integer>();
        meldungWKAendern = new LinkedList<Integer>();

        btnWKNachdruck = new LinkedList<Button>();
        meldungNachdruck = new LinkedList<Integer>();
        meldungWKNachdruck = new LinkedList<Integer>();

        for (int i = 0; i < anzahlMeldungen; i++) {
            if (zeilenSpalte1 < zeilenSpalte2) {
                zeilenSpalte1 = zeilenSpalte2;
            }
            zeilenSpalte2 = zeilenSpalte1;

            EclZugeordneteMeldungNeu eclZugeordneteMeldungNeu = eclBesitzAREintrag.zugeordneteMeldungenListe.get(i);

            Label hLabel0 = new Label();
            hLabel0.setText("MeldeIdent=" + Integer.toString(eclZugeordneteMeldungNeu.meldungsIdent));
            grpnStatus.add(hLabel0, 0, zeilenSpalte1);
            zeilenSpalte1++;

            Label hLabel1 = new Label();
            hLabel1.setText(eclZugeordneteMeldungNeu.getAktionaerTitelVornameName());
            grpnStatus.add(hLabel1, 0, zeilenSpalte1);
            zeilenSpalte1++;

            Label hLabel2 = new Label();
            hLabel2.setText(eclZugeordneteMeldungNeu.getAktionaerOrt());
            grpnStatus.add(hLabel2, 0, zeilenSpalte1);
            zeilenSpalte1++;

            if (eclZugeordneteMeldungNeu.statusPraesenz > 0) {
                Label hLabelP = new Label();
                hLabelP.setText("Ist oder war präsent");
                grpnStatus.add(hLabelP, 0, zeilenSpalte1);
                zeilenSpalte1++;
            }
            if (eclZugeordneteMeldungNeu.getKlasse() == 1) {
                Label hLabel3 = new Label();
                hLabel3.setText("Aktionär mit " + eclZugeordneteMeldungNeu.getStueckAktien() + " Aktien");
                anzahlStimmen += eclZugeordneteMeldungNeu.getStueckAktien();
                grpnStatus.add(hLabel3, 0, zeilenSpalte1);
                zeilenSpalte1++;

                btnMeldungWeitereWK[i] = new Button();
                btnMeldungWeitereWK[i].setText("Weitere Willenserklärung");
                btnMeldungWeitereWK[i].setOnAction(e -> {
                    clickedweitereWK(e);
                });
                if (CInjects.aendern) {
                    grpnStatus.add(btnMeldungWeitereWK[i], 0, zeilenSpalte1);
                }
                zeilenSpalte1++;

                btnMeldungStornieren[i] = new Button();
                btnMeldungStornieren[i].setText("Anmeldung Stornieren");
                btnMeldungStornieren[i].setOnAction(e -> {
                    clickedMeldungStornieren(e);
                });
                if (CInjects.aendern) {
                    grpnStatus.add(btnMeldungStornieren[i], 0, zeilenSpalte1);
                }
                zeilenSpalte1++;

                btnMeldungFixAendern[i] = new Button();
                btnMeldungFixAendern[i].setText("Fix-Aktien ändern");
                btnMeldungFixAendern[i].setOnAction(e -> {
                    clickedMeldungFixAendern(e);
                });
                if (CInjects.aendern) {
                    grpnStatus.add(btnMeldungFixAendern[i], 0, zeilenSpalte1);
                }
                zeilenSpalte1++;

                Button btnStimmausschluss = new Button(
                        "Stimmausschluss - " + eclZugeordneteMeldungNeu.eclMeldung.stimmausschluss.trim());
                btnStimmausschluss.setOnAction(e -> setzeStimmausschluss(eclZugeordneteMeldungNeu));
                if (CInjects.aendern) {
                    grpnStatus.add(btnStimmausschluss, 0, zeilenSpalte1);
                }
                zeilenSpalte1++;

                if (eclZugeordneteMeldungNeu.zugeordneteWillenserklaerungenList != null) {
                    for (int i1 = 0; i1 < eclZugeordneteMeldungNeu.zugeordneteWillenserklaerungenList.size(); i1++) {
                        EclWillenserklaerungStatusNeu willenserklaerung = eclZugeordneteMeldungNeu.zugeordneteWillenserklaerungenList
                                .get(i1);
                        if (willenserklaerung.isIstLeerDummy()) {
                            Label hwLabel1 = new Label();
                            hwLabel1.setText("Keine Willenserklärungen vorhanden");
                            grpnStatus.add(hwLabel1, 1, zeilenSpalte2);
                            zeilenSpalte2++;

                        } else {
                            Button hBtn1 = new Button();
                            hBtn1.setText("Stornieren");
                            hBtn1.setOnAction(e -> {
                                clickedStornieren(e);
                            });
                            btnWKStornieren.add(hBtn1);
                            meldungStornieren.add(i);
                            meldungWKStornieren.add(i1);
                            /*TODO Konsolidieren - isStornierenIstZulaessig bei Anmeldestelle*/
                            if (!willenserklaerung.isStorniert()
                                    && CInjects.aendern /*&& willenserklaerung.isStornierenIstZulaessig()*/
                                    && willenserklaerung
                                            .getWillenserklaerung() != KonstWillenserklaerung.anmeldungAusAktienregister) {
                                grpnStatus.add(hBtn1, 2, zeilenSpalte2);
                            }

                            if (!willenserklaerung.isStorniert()
                                    && CInjects.aendern/*willenserklaerung.isAendernIstZulaessig()*/
                                    && willenserklaerung
                                            .getWillenserklaerung() != KonstWillenserklaerung.anmeldungAusAktienregister
                                    && willenserklaerung
                                            .getWillenserklaerung() != KonstWillenserklaerung.neueZutrittsIdentZuMeldung
                                    && willenserklaerung
                                            .getWillenserklaerung() != KonstWillenserklaerung.neueZutrittsIdentZuMeldung_VollmachtAnDritte
                                    && willenserklaerung
                                            .getWillenserklaerung() != KonstWillenserklaerung.vollmachtAnDritte) {
                                Button hBtn2 = new Button();
                                hBtn2.setText("Ändern");
                                hBtn2.setOnAction(e -> {
                                    clickedAendern(e);
                                });
                                btnWKAendern.add(hBtn2);
                                meldungAendern.add(i);
                                meldungWKAendern.add(i1);
                                if (!willenserklaerung.isStorniert() && CInjects.aendern && willenserklaerung
                                        .getWillenserklaerung() != KonstWillenserklaerung.anmeldungAusAktienregister) {
                                    grpnStatus.add(hBtn2, 3, zeilenSpalte2);
                                }

                                /*War ein Versuch, geht aber nicht auf die schnelle, weil eclWeisungMeldung an dieser Stimme immer ==null ist.*/
                                //                                if (willenserklaerung.eclWeisungMeldung==null) {
                                //                                    CaBug.druckeLog("eclWeisungMeldung==null", logDrucken, 10);
                                //                                }
                                //                                else {
                                //                                    CaBug.druckeLog("eclWeisungMeldung!=null", logDrucken, 10);
                                //                                }
                                //                                
                                //                                if (willenserklaerung.eclWeisungMeldung!=null && willenserklaerung.eclWeisungMeldung.aktiv==0) {/*Nicht aktiv*/
                                //                                    CaBug.druckeLog("nicht aktiv", logDrucken, 10);
                                //                                    Label nichtAktiv=new Label("Nicht Aktiv!");
                                //                                    zeilenSpalte2++;
                                //                                    grpnStatus.add(nichtAktiv,  3, zeilenSpalte2);
                                //                                    
                                //                                }

                            } else {
                                if (willenserklaerung
                                        .getWillenserklaerung() == KonstWillenserklaerung.neueZutrittsIdentZuMeldung
                                        || willenserklaerung
                                                .getWillenserklaerung() == KonstWillenserklaerung.neueZutrittsIdentZuMeldung_VollmachtAnDritte
                                        || willenserklaerung
                                                .getWillenserklaerung() == KonstWillenserklaerung.vollmachtAnDritte) {
                                    Button hBtn2 = new Button();
                                    hBtn2.setText("Korrigieren");
                                    hBtn2.setOnAction(e -> {
                                        clickedAendern(e);
                                    });
                                    btnWKAendern.add(hBtn2);
                                    meldungAendern.add(i);
                                    meldungWKAendern.add(i1);
                                    if (!willenserklaerung.isStorniert() && CInjects.aendern) {
                                        grpnStatus.add(hBtn2, 3, zeilenSpalte2);
                                    }

                                }
                                if (willenserklaerung
                                        .getWillenserklaerung() == KonstWillenserklaerung.neueZutrittsIdentZuMeldung
                                        || willenserklaerung
                                                .getWillenserklaerung() == KonstWillenserklaerung.neueZutrittsIdentZuMeldung_VollmachtAnDritte) {
                                    Button hBtn3 = new Button();
                                    hBtn3.setText("Nachdruck");
                                    hBtn3.setOnAction(e -> {
                                        clickedNachdruck(e);
                                    });
                                    btnWKNachdruck.add(hBtn3);
                                    meldungNachdruck.add(i);
                                    meldungWKNachdruck.add(i1);
                                    if (!willenserklaerung.isStorniert() && CInjects.aendern) {
                                        grpnStatus.add(hBtn3, 4, zeilenSpalte2);
                                    }

                                }

                            }

                            if (willenserklaerung.isStorniert()) {
                                Label hLbl = new Label();
                                hLbl.setText("Storniert");
                                grpnStatus.add(hLbl, 2, zeilenSpalte2);
                            }

                            for (i2 = 0; i2 < willenserklaerung.textListeIntern.size(); i2++) {
                                Label hwLabel2 = new Label();
                                hwLabel2.setText(willenserklaerung.textListeIntern.get(i2));
                                grpnStatus.add(hwLabel2, 1, zeilenSpalte2);
                                zeilenSpalte2++;
                            }
                            zeilenSpalte2++;
                        }
                    }
                }
            }

            Label hLabel4 = new Label();
            hLabel4.setText("");
            grpnStatus.add(hLabel4, 0, zeilenSpalte1);
            zeilenSpalte1++;

        }

        Button btnFixNeu = new Button();
        btnFixNeu.setText("neue Fix-Anmeldung");
        btnFixNeu.setOnAction(e -> {
            clickedWeitereAnmeldenFix(e);
        });
        tfFixNeu = new TextField();
        tfFixNeu.setText("");
        HBox hHBox = new HBox();
        hHBox.getChildren().addAll(btnFixNeu, tfFixNeu);
        if (anzahlStimmen < eclTLoginDatenM.stimmen && CInjects.aendern) {
            grpnStatus.add(hHBox, 0, zeilenSpalte1);

        }

        scrpnStatus.setContent(grpnStatus);

        tfAktienAngemeldet.setText((Long.toString(anzahlStimmen)));
    }

    /**
     * Bereite tab erstanmeldung.
     */
    private void bereiteTabErstanmeldung() {

        grpnErstanmeldung = new GridPane();
        grpnErstanmeldung.setVgap(5);
        grpnErstanmeldung.setHgap(15);

        if (CInjects.aendern) {

            btnAnmeldenAlles = new Button("Anmelden Gesamtbestand");
            btnAnmeldenAlles.setOnAction(e -> {
                clickedAnmeldenAlles(e);
            });
            grpnErstanmeldung.add(btnAnmeldenAlles, 0, 0);
            Label lblAnmeldenAlles = new Label("Der Gesamtbestand des Aktionärs wird angemeldet.");
            grpnErstanmeldung.add(lblAnmeldenAlles, 1, 0);

            btnAnmeldenAufgeteiltAuf2 = new Button("Anmelden 2 Bestände gleichverteilt");
            btnAnmeldenAufgeteiltAuf2.setOnAction(e -> {
                clickedAnmeldenAufgeteiltAuf2(e);
            });
            grpnErstanmeldung.add(btnAnmeldenAufgeteiltAuf2, 0, 1);
            Label lblAnmeldenAufgeteiltAuf2 = new Label(
                    "Der Gesamtbestand des Aktionärs wird angemeldet, gleichmäßig aufgeteilt auf 2 Meldungen.");
            grpnErstanmeldung.add(lblAnmeldenAufgeteiltAuf2, 1, 1);

            btnAnmeldenFix = new Button("Anmelden mit Fix-Bestand");
            btnAnmeldenFix.setOnAction(e -> {
                clickedAnmeldenFix(e);
            });
            grpnErstanmeldung.add(btnAnmeldenFix, 0, 2);
            Label lblAnmeldenFix = new Label("Der eingegebene Fix-Aktienbestand wird angemeldet.");
            grpnErstanmeldung.add(lblAnmeldenFix, 1, 2);
            Label lblAnmeldenFixAktien = new Label("Anz Aktien Fix:");
            grpnErstanmeldung.add(lblAnmeldenFixAktien, 2, 2);
            tfAnmeldenFix = new TextField("");
            tfAnmeldenFix.setText("");
            grpnErstanmeldung.add(tfAnmeldenFix, 3, 2);

            //          Button Aktionaer sperren
            if (ParamS.param.paramBasis.namensaktienAktiv) {
                btnSperren = new Button("Bestand 0 setzen");
                btnSperren.setOnAction(e -> {
                    Alert alert = new Alert(AlertType.CONFIRMATION);
                    alert.setTitle("Aktionär sperren");
                    alert.setHeaderText("Aktionär wirklich entgültig sperren?");
                    alert.setContentText(null);

                    Optional<ButtonType> result = alert.showAndWait();
                    if (result.get() == ButtonType.OK)
                        clickedSperren();
                });
                grpnErstanmeldung.add(btnSperren, 0, 3);

                Label lblSperren = new Label("Der Bestand des Aktionärs wird 0 gesetzt");
                grpnErstanmeldung.add(lblSperren, 1, 3);
            }
        }

        else {
            Label lblH1 = new Label("Nicht angemeldet!");
            grpnErstanmeldung.add(lblH1, 0, 0);
        }

        scrpnErstanmeldung.setContent(grpnErstanmeldung);

    }

    /**
     * Clicked eine EK selbst.
     *
     * @param event the event
     */
    @FXML
    void clickedEineEKSelbst(ActionEvent event) {

        Stage neuerDialog = new Stage();

        CtrlEineEintrittskarteSelbst controllerEineEintrittskarteSelbst = new CtrlEineEintrittskarteSelbst();

        controllerEineEintrittskarteSelbst.init(neuerDialog, 2, eclTLoginDatenM,
                eclBesitzAREintrag.zugeordneteMeldungenListe.get(lfdNrZugeordneteMeldung));

        FXMLLoader loader = new FXMLLoader(getClass().getResource("EineEintrittskarteSelbst.fxml"));
        loader.setController(controllerEineEintrittskarteSelbst);
        Parent mainPane = null;
        try {
            mainPane = (Parent) loader.load();
        } catch (IOException e) {
            CaBug.drucke("CtrlStatus.clickedEineEKSelbst 001");
            e.printStackTrace();
        }
        Scene scene = new Scene(mainPane, 1000, 800);
        neuerDialog.setTitle("Eine Eintrittskarte Selbst zu bestehender Anmeldung");
        neuerDialog.setScene(scene);
        neuerDialog.initModality(Modality.APPLICATION_MODAL);
        neuerDialog.showAndWait();

        neuEinlesenUndAnzeigen(aktionaersNummerInArbeit);
    }

    /**
     * Clicked eine EK vertreter.
     *
     * @param event the event
     */
    @FXML
    void clickedEineEKVertreter(ActionEvent event) {

        Stage neuerDialog = new Stage();

        CtrlEineEintrittskarteVollmacht controllerEineEintrittskarteVollmacht = new CtrlEineEintrittskarteVollmacht();

        controllerEineEintrittskarteVollmacht.init(neuerDialog, 2, eclTLoginDatenM,
                eclBesitzAREintrag.zugeordneteMeldungenListe.get(lfdNrZugeordneteMeldung));

        FXMLLoader loader = new FXMLLoader(getClass().getResource("EineEintrittskarteVollmacht.fxml"));
        loader.setController(controllerEineEintrittskarteVollmacht);
        Parent mainPane = null;
        try {
            mainPane = (Parent) loader.load();
        } catch (IOException e) {
            CaBug.drucke("CtrlStatus.clickedEineEKVertreter 001");
            e.printStackTrace();
        }
        Scene scene = new Scene(mainPane, 1000, 800);
        neuerDialog.setTitle("Eine Eintrittskarte mit Vertreter zu bestehender Anmeldung");
        neuerDialog.setScene(scene);
        neuerDialog.initModality(Modality.APPLICATION_MODAL);
        neuerDialog.showAndWait();

        neuEinlesenUndAnzeigen(aktionaersNummerInArbeit);
    }

    /**
     * Clicked SRV.
     *
     * @param event the event
     */
    @FXML
    void clickedSRV(ActionEvent event) {
        Stage neuerDialog = new Stage();
        CaIcon.master(neuerDialog);

        CtrlVollmachtWeisung controllerFenster = new CtrlVollmachtWeisung();

        controllerFenster.init(neuerDialog, 4, 2, eclTLoginDatenM,
                eclBesitzAREintrag.zugeordneteMeldungenListe.get(lfdNrZugeordneteMeldung), null);

        CaController caController = new CaController();
        caController.open(neuerDialog, controllerFenster,
                "/de/meetingapps/meetingclient/meetingBestand/VollmachtWeisung.fxml", 1500, 760,
                "Vollmacht/Weisung Stimmrechtsvertreter zu bestehender Anmeldung", true);

        neuEinlesenUndAnzeigen(aktionaersNummerInArbeit);
    }

    /**
     * Clicked briefwahl.
     *
     * @param event the event
     */
    @FXML
    void clickedBriefwahl(ActionEvent event) {
        Stage neuerDialog = new Stage();
        CaIcon.master(neuerDialog);

        CtrlVollmachtWeisung controllerFenster = new CtrlVollmachtWeisung();

        controllerFenster.init(neuerDialog, 5, 2, eclTLoginDatenM,
                eclBesitzAREintrag.zugeordneteMeldungenListe.get(lfdNrZugeordneteMeldung), null);

        CaController caController = new CaController();
        caController.open(neuerDialog, controllerFenster,
                "/de/meetingapps/meetingclient/meetingBestand/VollmachtWeisung.fxml", 1500, 760,
                "Briefwahl zu bestehender Anmeldung", true);

        neuEinlesenUndAnzeigen(aktionaersNummerInArbeit);
    }

    /**
     * Clicked KIAV.
     *
     * @param event the event
     */
    @FXML
    void clickedKIAV(ActionEvent event) {
        Stage neuerDialog = new Stage();
        CaIcon.master(neuerDialog);

        CtrlVollmachtWeisung controllerFenster = new CtrlVollmachtWeisung();

        controllerFenster.init(neuerDialog, 6, 2, eclTLoginDatenM,
                eclBesitzAREintrag.zugeordneteMeldungenListe.get(lfdNrZugeordneteMeldung), null);

        CaController caController = new CaController();
        caController.open(neuerDialog, controllerFenster,
                "/de/meetingapps/meetingclient/meetingBestand/VollmachtWeisung.fxml", 1500, 760,
                "Vollmacht(/Weisung) an KIAV zu bestehender Anmeldung", true);

        neuEinlesenUndAnzeigen(aktionaersNummerInArbeit);
    }

    /**
     * Clicked dauervollmacht.
     *
     * @param event the event
     */
    @FXML
    void clickedDauervollmacht(ActionEvent event) {
        Stage neuerDialog = new Stage();
        CaIcon.master(neuerDialog);

        CtrlVollmachtWeisung controllerFenster = new CtrlVollmachtWeisung();

        controllerFenster.init(neuerDialog, 31, 2, eclTLoginDatenM,
                eclBesitzAREintrag.zugeordneteMeldungenListe.get(lfdNrZugeordneteMeldung), null);

        CaController caController = new CaController();
        caController.open(neuerDialog, controllerFenster,
                "/de/meetingapps/meetingclient/meetingBestand/VollmachtWeisung.fxml", 1500, 760,
                "Dauervollmacht an KIAV zu bestehender Anmeldung", true);

        neuEinlesenUndAnzeigen(aktionaersNummerInArbeit);
    }

    /**
     * Clicked orga.
     *
     * @param event the event
     */
    @FXML
    void clickedOrga(ActionEvent event) {
        Stage neuerDialog = new Stage();
        CaIcon.master(neuerDialog);

        CtrlVollmachtWeisung controllerFenster = new CtrlVollmachtWeisung();

        controllerFenster.init(neuerDialog, 35, 2, eclTLoginDatenM,
                eclBesitzAREintrag.zugeordneteMeldungenListe.get(lfdNrZugeordneteMeldung), null);

        CaController caController = new CaController();
        caController.open(neuerDialog, controllerFenster,
                "/de/meetingapps/meetingclient/meetingBestand/VollmachtWeisung.fxml", 1500, 760,
                "Aufnahme einer bestehenden Anmeldung in eine Sammelkarte (organisatorisch) mit/ohne Weisung", true);

        neuEinlesenUndAnzeigen(aktionaersNummerInArbeit);
    }

    /**
     * Clicked vollmacht an dritte.
     *
     * @param event the event
     */
    @FXML
    void clickedVollmachtAnDritte(ActionEvent event) {

        Stage neuerDialog = new Stage();

        CtrlVollmachtAnDritte controllerVollmachtAnDritte = new CtrlVollmachtAnDritte();

        controllerVollmachtAnDritte.init(neuerDialog, 2, eclTLoginDatenM,
                eclBesitzAREintrag.zugeordneteMeldungenListe.get(lfdNrZugeordneteMeldung));

        FXMLLoader loader = new FXMLLoader(getClass().getResource("VollmachtAnDritte.fxml"));
        loader.setController(controllerVollmachtAnDritte);
        Parent mainPane = null;
        try {
            mainPane = (Parent) loader.load();
        } catch (IOException e) {
            CaBug.drucke("CtrlStatus.clickedVollmachtAnDritte 001");
            e.printStackTrace();
        }
        Scene scene = new Scene(mainPane, 900, 650);
        neuerDialog.setTitle("Vollmacht an Dritte (ohne Ausstellung einer Eintrittskarte)");
        neuerDialog.setScene(scene);
        neuerDialog.initModality(Modality.APPLICATION_MODAL);
        neuerDialog.showAndWait();

        neuEinlesenUndAnzeigen(aktionaersNummerInArbeit);
    }

    /**
     * Bereite tab weitere WK.
     */
    private void bereiteTabWeitereWK() {
        grpnWeitereWK = new GridPane();
        grpnWeitereWK.setVgap(5);
        grpnWeitereWK.setHgap(15);

        btnEineEKSelbst = new Button("Eine Eintrittskarte Selbst");
        btnEineEKSelbst.setOnAction(e -> {
            clickedEineEKSelbst(e);
        });
        grpnWeitereWK.add(btnEineEKSelbst, 0, 0);
        Label lblEineEKSelbst = new Label("Eine Eintrittskarte auf Selbst wird ausgestellt.");
        grpnWeitereWK.add(lblEineEKSelbst, 1, 0);

        btnEineEKVertreter = new Button("Eine Eintrittskarte mit Vertreter");
        btnEineEKVertreter.setOnAction(e -> {
            clickedEineEKVertreter(e);
        });
        grpnWeitereWK.add(btnEineEKVertreter, 0, 1);
        Label lblEineEKVertreter = new Label("Eine Eintrittskarte ausgestellt auf einen Vertreter.");
        grpnWeitereWK.add(lblEineEKVertreter, 1, 1);

        btnSRV = new Button("Stimmrechtsvertreter");
        btnSRV.setOnAction(e -> {
            clickedSRV(e);
        });
        grpnWeitereWK.add(btnSRV, 0, 2);
        Label lblSRV = new Label("Vollmacht und Weisung an Stimmrechtsvertreter");
        grpnWeitereWK.add(lblSRV, 1, 2);

        btnBriefwahl = new Button("Briefwahl");
        btnBriefwahl.setOnAction(e -> {
            clickedBriefwahl(e);
        });
        grpnWeitereWK.add(btnBriefwahl, 0, 3);
        Label lblBriefwahl = new Label("Briefwahl.");
        grpnWeitereWK.add(lblBriefwahl, 1, 3);

        btnKIAV = new Button("KIAV");
        btnKIAV.setOnAction(e -> {
            clickedKIAV(e);
        });
        grpnWeitereWK.add(btnKIAV, 0, 4);
        Label lblKIAV = new Label("Vollmacht/(Weisung) an Kreditinstitut/Aktionärsvereinigung (in Sammelkarte).");
        grpnWeitereWK.add(lblKIAV, 1, 4);

        btnDauervollmacht = new Button("Dauervollmacht");
        btnDauervollmacht.setOnAction(e -> {
            clickedDauervollmacht(e);
        });
        grpnWeitereWK.add(btnDauervollmacht, 0, 5);
        Label lblDauervollmacht = new Label(
                "Dauervollmacht eines Aktionärs an ein Kreditinstitut (ohne Weisung; in Sammelkarte).");
        grpnWeitereWK.add(lblDauervollmacht, 1, 5);

        btnOrga = new Button("Organisatorisch in Sammelkarte");
        btnOrga.setOnAction(e -> {
            clickedOrga(e);
        });
        grpnWeitereWK.add(btnOrga, 0, 6);
        Label lblOrga = new Label("Aufnahme in eine rein organisatorische Sammelkarte (mit/ohne Weisung).");
        grpnWeitereWK.add(lblOrga, 1, 6);

        btnVollmachtDritte = new Button("Vollmacht an Dritte");
        btnVollmachtDritte.setOnAction(e -> {
            clickedVollmachtAnDritte(e);
        });
        grpnWeitereWK.add(btnVollmachtDritte, 0, 7);
        Label lblVollmachtDritte = new Label("Erfassung einer Vollmacht an Dritte (ohne weitere Eintrittskarte).");
        grpnWeitereWK.add(lblVollmachtDritte, 1, 7);

        scrpnWeitereWillenserklaerung.setContent(grpnWeitereWK);

    }

    /**
     * Clicked suchen.
     *
     * @param event the event
     */
    @FXML
    void clickedSuchen(ActionEvent event) {

        Stage newStage = new Stage();
        CaIcon.bestandsverwaltung(newStage);
        CtrlSuchen controllerFenster = new CtrlSuchen();
        controllerFenster.init(newStage);

        controllerFenster.funktionsButton1 = "Verarbeiten";
        controllerFenster.mehrfachAuswahlZulaessig = false;

        controllerFenster.durchsuchenSammelkarten = false;
        controllerFenster.durchsuchenInSammelkarten = true;
        controllerFenster.durchsuchenGaeste = false;

        CaController caController = new CaController();
        caController.open(newStage, controllerFenster,
                "/de/meetingapps/meetingclient/meetingClientDialoge/Suchen.fxml", 1500, 760, "Suchen", true);

        if (controllerFenster.rcFortsetzen) {
            int anz = controllerFenster.rcAktienregister.length;
            if (anz == 1) {
                tfAktionaersnummer.setText(controllerFenster.rcAktienregister[0].aktionaersnummer);
                doEinlesen();
                return;
            }
        }

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
    public void doEinlesen() {

        if (tfAktionaersnummer.getText() == null || tfAktionaersnummer.getText().isEmpty()) {
            tfFehlermeldung.setText("Bitte Aktionärsnummer eingeben!");
            return;
        }
        if (tfAktionaersnummer.getText().startsWith("S")) {
            tfFehlermeldung.setText("Bitte Aktionärsnummer eingeben!");
            return;
        }

        DbBundle lDbBundle = new DbBundle();
        BlNummernformBasis blNummernformBasis = new BlNummernformBasis(lDbBundle);
        aktionaersNummerInArbeit = blNummernformBasis.loginKennungAufbereitenFuerIntern(tfAktionaersnummer.getText());

        tfAktionaersnummer.setText(aktionaersNummerInArbeit);

        if (ParamSpezial.ku168(ParamS.clGlobalVar.mandant)) {
            if (aktionaersNummerInArbeit.length() <= 4) {
                while (aktionaersNummerInArbeit.length() < 5) {
                    aktionaersNummerInArbeit = "0" + aktionaersNummerInArbeit;
                }
                aktionaersNummerInArbeit = "55555" + aktionaersNummerInArbeit + "0";
            }
        }

        neuEinlesenUndAnzeigen(aktionaersNummerInArbeit);

    }

    /**
     * Clicked abbruch.
     *
     * @param event the event
     */
    @FXML
    void clickedAbbruch(ActionEvent event) {
        setzeStatusAktionaersnummerEingeben();
    }

    /**
     * Clicked zurueck.
     *
     * @param event the event
     */
    @FXML
    void clickedZurueck(ActionEvent event) {
        neuEinlesenUndAnzeigen(aktionaersNummerInArbeit);
    }

    /**
     * Clicked speichern.
     *
     * @param event the event
     */
    @FXML
    void clickedSpeichern(ActionEvent event) {

    }

    /**
     * Clicked stornieren.
     *
     * @param event the event
     */
    @FXML
    void clickedStornieren(ActionEvent event) {
        int i;
        int rc = 0;
        int sammelIdent = 0;
        int gef = -1;

        for (i = 0; i < btnWKStornieren.size(); i++) {
            if (event.getSource() == btnWKStornieren.get(i)) {
                gef = i;
            }
        }
        lfdNrZugeordneteMeldung = meldungStornieren.get(gef);
        lfdNrWillenserklaerung = meldungWKStornieren.get(gef);

        Stage neuerDialog = new Stage();

        CtrlStornoBestaetigung controllerBestaetigung = new CtrlStornoBestaetigung();

        controllerBestaetigung
                .init(neuerDialog, 2,
                        eclBesitzAREintrag.zugeordneteMeldungenListe
                                .get(lfdNrZugeordneteMeldung).zugeordneteWillenserklaerungenList
                                        .get(lfdNrWillenserklaerung));

        FXMLLoader loader = new FXMLLoader(getClass().getResource("StornoBestaetigung.fxml"));
        loader.setController(controllerBestaetigung);
        Parent mainPane = null;
        try {
            mainPane = (Parent) loader.load();
        } catch (IOException e) {
            CaBug.drucke("CtrlStatus.clickedStornieren 001");
            e.printStackTrace();
        }
        Scene scene = new Scene(mainPane, 400, 400);
        neuerDialog.setTitle("Stornieren bestätigen");
        neuerDialog.setScene(scene);
        neuerDialog.initModality(Modality.APPLICATION_MODAL);
        neuerDialog.showAndWait();

        if (controllerBestaetigung.isErgebnis()) {
            /*Stornieren*/

            String fehlerString = SClErfassungsDaten.pruefeEingaben(tfErklaerungDatum, tfErklaerungZeit);
            if (fehlerString != null) {
                CaZeigeHinweis caZeigeHinweisFehler = new CaZeigeHinweis();
                caZeigeHinweisFehler.zeige(eigeneStage, "Fehler: " + fehlerString);
                tfFehlermeldung.setText(fehlerString);
                return;
            }

            WSClient wsClient = new WSClient();

            WELoginVerify weLoginVerify = wsClient.fuelleLoginVerify(eclTLoginDatenM);
            EclWillenserklaerungStatusNeu lWillenserklaerungStatus = eclBesitzAREintrag.zugeordneteMeldungenListe
                    .get(lfdNrZugeordneteMeldung).zugeordneteWillenserklaerungenList.get(lfdNrWillenserklaerung);

            if (lWillenserklaerungStatus.getWillenserklaerung() != KonstWillenserklaerung.widerrufVollmachtAnDritte) {
                /*Stornieren vorbereiten holen*/
                SClErfassungsDaten.fuelleLoginVerify(weLoginVerify, tfErklaerungDatum, tfErklaerungZeit, cbEingangsWeg);

                WEStornierenVorbereitenGet weStornierenVorbereitenGet = new WEStornierenVorbereitenGet();
                weStornierenVorbereitenGet.setWeLoginVerify(weLoginVerify);
                weStornierenVorbereitenGet.setAusgewaehlteHauptAktion(KonstPortalAktion.HAUPT_BEREITSANGEMELDET);

                // 				switch (EnWillenserklaerung.fromInt(lWillenserklaerungStatusM.getWillenserklaerung())){
                switch (lWillenserklaerungStatus.getWillenserklaerung()) {
                case KonstWillenserklaerung.neueZutrittsIdentZuMeldung: {
                    weStornierenVorbereitenGet.setAusgewaehlteAktion(KonstPortalAktion.EK_STORNIEREN);
                    break;
                }
                case KonstWillenserklaerung.neueZutrittsIdentZuMeldung_VollmachtAnDritte: {
                    weStornierenVorbereitenGet.setAusgewaehlteAktion(KonstPortalAktion.EK_MIT_VOLLMACHT_STORNIEREN);
                    break;
                }
                case KonstWillenserklaerung.vollmachtUndWeisungAnSRV: {
                    weStornierenVorbereitenGet.setAusgewaehlteAktion(KonstPortalAktion.SRV_STORNIEREN);
                    break;
                }
                case KonstWillenserklaerung.briefwahl: {
                    weStornierenVorbereitenGet.setAusgewaehlteAktion(KonstPortalAktion.BRIEFWAHL_STORNIEREN);
                    break;
                }
                case KonstWillenserklaerung.vollmachtUndWeisungAnKIAV: {
                    weStornierenVorbereitenGet.setAusgewaehlteAktion(KonstPortalAktion.KIAV_STORNIEREN);
                    break;
                }
                case KonstWillenserklaerung.dauervollmachtAnKIAV: {
                    weStornierenVorbereitenGet.setAusgewaehlteAktion(KonstPortalAktion.DAUERVOLLMACHT_STORNIEREN);
                    break;
                }
                case KonstWillenserklaerung.organisatorischMitWeisungInSammelkarte: {
                    weStornierenVorbereitenGet.setAusgewaehlteAktion(KonstPortalAktion.ORGANISATORISCH_STORNIEREN);
                    break;
                }
                default:
                    break;
                }

                weStornierenVorbereitenGet.setZugeordneteMeldung(
                        eclBesitzAREintrag.zugeordneteMeldungenListe.get(lfdNrZugeordneteMeldung));
                weStornierenVorbereitenGet.setEclWillenserklaerungStatus(lWillenserklaerungStatus);
                WEStornierenVorbereitenGetRC weStornierenVorbereitenGetRC = wsClient
                        .stornierenVorbereitenGet(weStornierenVorbereitenGet);
                sammelIdent = weStornierenVorbereitenGetRC.getSammelIdent();
            }

            SClErfassungsDaten.fuelleLoginVerify(weLoginVerify, tfErklaerungDatum, tfErklaerungZeit, cbEingangsWeg);

            switch (lWillenserklaerungStatus.getWillenserklaerung()) {
            case KonstWillenserklaerung.neueZutrittsIdentZuMeldung: {
                WEEintrittskarteStornierenGet weEintrittskarteStornierenGet = new WEEintrittskarteStornierenGet();
                weEintrittskarteStornierenGet.setWeLoginVerify(weLoginVerify);
                weEintrittskarteStornierenGet.setAusgewaehlteHauptAktion(KonstPortalAktion.HAUPT_BEREITSANGEMELDET);
                weEintrittskarteStornierenGet.setAusgewaehlteAktion(KonstPortalAktion.EK_STORNIEREN);
                weEintrittskarteStornierenGet.setZugeordneteMeldung(
                        eclBesitzAREintrag.zugeordneteMeldungenListe.get(lfdNrZugeordneteMeldung));
                weEintrittskarteStornierenGet.setEclWillenserklaerungStatus(lWillenserklaerungStatus);
                WEEintrittskarteStornierenGetRC weEintrittskarteStornierenGetRC = wsClient
                        .eintrittskarteStornierenGet(weEintrittskarteStornierenGet);
                rc = weEintrittskarteStornierenGetRC.getRc();
                break;
            }
            case KonstWillenserklaerung.neueZutrittsIdentZuMeldung_VollmachtAnDritte: {
                WEEintrittskarteStornierenGet weEintrittskarteStornierenGet = new WEEintrittskarteStornierenGet();
                weEintrittskarteStornierenGet.setWeLoginVerify(weLoginVerify);
                weEintrittskarteStornierenGet.setAusgewaehlteHauptAktion(KonstPortalAktion.HAUPT_BEREITSANGEMELDET);
                weEintrittskarteStornierenGet.setAusgewaehlteAktion(KonstPortalAktion.EK_MIT_VOLLMACHT_STORNIEREN);
                weEintrittskarteStornierenGet.setZugeordneteMeldung(
                        eclBesitzAREintrag.zugeordneteMeldungenListe.get(lfdNrZugeordneteMeldung));
                weEintrittskarteStornierenGet.setEclWillenserklaerungStatus(lWillenserklaerungStatus);
                WEEintrittskarteStornierenGetRC weEintrittskarteStornierenGetRC = wsClient
                        .eintrittskarteStornierenGet(weEintrittskarteStornierenGet);
                rc = weEintrittskarteStornierenGetRC.getRc();
                break;
            }
            case KonstWillenserklaerung.vollmachtUndWeisungAnSRV:
            case KonstWillenserklaerung.briefwahl:
            case KonstWillenserklaerung.vollmachtUndWeisungAnKIAV:
            case KonstWillenserklaerung.dauervollmachtAnKIAV:
            case KonstWillenserklaerung.organisatorischMitWeisungInSammelkarte: {
                WEWeisungStornierenGet weWeisungStornierenGet = new WEWeisungStornierenGet();
                weWeisungStornierenGet.setWeLoginVerify(weLoginVerify);
                weWeisungStornierenGet.setAusgewaehlteHauptAktion(KonstPortalAktion.HAUPT_BEREITSANGEMELDET);
                switch (lWillenserklaerungStatus.getWillenserklaerung()) {
                case KonstWillenserklaerung.vollmachtUndWeisungAnSRV: {
                    weWeisungStornierenGet.setAusgewaehlteAktion(KonstPortalAktion.SRV_STORNIEREN);
                    break;
                }
                case KonstWillenserklaerung.briefwahl: {
                    weWeisungStornierenGet.setAusgewaehlteAktion(KonstPortalAktion.BRIEFWAHL_STORNIEREN);
                    break;
                }
                case KonstWillenserklaerung.vollmachtUndWeisungAnKIAV: {
                    weWeisungStornierenGet.setAusgewaehlteAktion(KonstPortalAktion.KIAV_STORNIEREN);
                    break;
                }
                case KonstWillenserklaerung.dauervollmachtAnKIAV: {
                    weWeisungStornierenGet.setAusgewaehlteAktion(KonstPortalAktion.DAUERVOLLMACHT_STORNIEREN);
                    break;
                }
                case KonstWillenserklaerung.organisatorischMitWeisungInSammelkarte: {
                    weWeisungStornierenGet.setAusgewaehlteAktion(KonstPortalAktion.ORGANISATORISCH_STORNIEREN);
                    break;
                }
                default:
                    break;

                }
                weWeisungStornierenGet.setMeldungsIdent(
                        eclBesitzAREintrag.zugeordneteMeldungenListe.get(lfdNrZugeordneteMeldung).getMeldungsIdent());

                weWeisungStornierenGet.setWillenserklaerungIdent(lWillenserklaerungStatus.getWillenserklaerungIdent());
                weWeisungStornierenGet.setSammelIdent(sammelIdent);
                weWeisungStornierenGet.setZugeordneteMeldung(
                        eclBesitzAREintrag.zugeordneteMeldungenListe.get(lfdNrZugeordneteMeldung));
                weWeisungStornierenGet.setEclWillenserklaerungStatus(lWillenserklaerungStatus);

                WEWeisungStornierenGetRC weWeisungStornierenGetRC = wsClient
                        .weisungStornierenGet(weWeisungStornierenGet);
                rc = weWeisungStornierenGetRC.getRc();
                break;
            }
            case KonstWillenserklaerung.vollmachtAnDritte: {
                WEVollmachtDritteStornierenGet weVollmachtDritteStornierenGet = new WEVollmachtDritteStornierenGet();
                weVollmachtDritteStornierenGet.setWeLoginVerify(weLoginVerify);
                weVollmachtDritteStornierenGet.setAusgewaehlteHauptAktion(KonstPortalAktion.HAUPT_BEREITSANGEMELDET);
                weVollmachtDritteStornierenGet.setAusgewaehlteAktion(KonstPortalAktion.VOLLMACHT_DRITTE_STORNIEREN);
                weVollmachtDritteStornierenGet.setZugeordneteMeldung(
                        eclBesitzAREintrag.zugeordneteMeldungenListe.get(lfdNrZugeordneteMeldung));
                weVollmachtDritteStornierenGet.setEclWillenserklaerungStatus(lWillenserklaerungStatus);
                WEVollmachtDritteStornierenGetRC weVollmachtDritteStornierenGetRC = wsClient
                        .vollmachtDritteStornierenGet(weVollmachtDritteStornierenGet);
                rc = weVollmachtDritteStornierenGetRC.getRc();
                break;
            }
            default:
                break;
            }

            /*
            WEVollmachtDritteGet weVollmachtDritteGet=new WEVollmachtDritteGet();
            weVollmachtDritteGet.setVollmachtName(tfVertreterName.getText().trim());
            weVollmachtDritteGet.setVollmachtVorname(tfVertreterVorname.getText().trim());
            weVollmachtDritteGet.setVollmachtOrt(tfVertreterOrt.getText().trim());
            
            
            
            weVollmachtDritteGet.setWeLoginVerify(weLoginVerify);
            if (hauptFunktion==1){
            	weVollmachtDritteGet.setAusgewaehlteHauptAktion("1");}
            else{weVollmachtDritteGet.setAusgewaehlteHauptAktion("2");}
            weVollmachtDritteGet.setAusgewaehlteAktion("29");
            
            weVollmachtDritteGet.setEclZugeordneteMeldungM(eclZugeordneteMeldungM);
            
            WEVollmachtDritteGetRC weVollmachtDritteGetRC=wsClient.vollmachtDritteGet(weVollmachtDritteGet);
            rc=weVollmachtDritteGetRC.getRc();
            */

            if (rc < 0) { /*Fehlerbehandlung*/
                switch (rc) {
                case CaFehler.afAndererUserAktiv: {
                    tfFehlermeldung.setText(
                            "Datensatz mittlerweile von anderem User verändert! Bitte abbrechen und erneut einlesen.");
                    break;
                }
                default: {
                    tfFehlermeldung
                            .setText("Programmierer verständigen! Fehler " + rc + " " + CaFehler.getFehlertext(rc, 0));
                    break;
                }
                }
                setzeStatusAbbruch();
                return;
            }
        } else {
            /*Nicht stornieren*/
        }

        neuEinlesenUndAnzeigen(aktionaersNummerInArbeit);

    }

    /**
     * Clicked nachdruck.
     *
     * @param event the event
     */
    @FXML
    void clickedNachdruck(ActionEvent event) {

        int i;
        int gef = -1;
        for (i = 0; i < btnWKNachdruck.size(); i++) {
            if (event.getSource() == btnWKNachdruck.get(i)) {
                gef = i;
            }
        }
        lfdNrZugeordneteMeldung = meldungNachdruck.get(gef);
        lfdNrWillenserklaerung = meldungWKNachdruck.get(gef);
        EclWillenserklaerungStatusNeu lWillenserklaerungStatus = eclBesitzAREintrag.zugeordneteMeldungenListe
                .get(lfdNrZugeordneteMeldung).zugeordneteWillenserklaerungenList.get(lfdNrWillenserklaerung);

        String ekNummer = lWillenserklaerungStatus.getZutrittsIdent();
        String ekNummerNeben = lWillenserklaerungStatus.getZutrittsIdentNeben();

        CtrlEintrittskarteDrucken eintrittskarteDrucken = new CtrlEintrittskarteDrucken();
        eintrittskarteDrucken.einzelDruck(ekNummer, ekNummerNeben);
    }

    /**
     * Clicked aendern.
     *
     * @param event the event
     */
    @FXML
    void clickedAendern(ActionEvent event) {
        int i;
        int gef = -1;
        for (i = 0; i < btnWKAendern.size(); i++) {
            if (event.getSource() == btnWKAendern.get(i)) {
                gef = i;
            }
        }
        lfdNrZugeordneteMeldung = meldungAendern.get(gef);
        lfdNrWillenserklaerung = meldungWKAendern.get(gef);

        EclWillenserklaerungStatusNeu lWillenserklaerungStatus = eclBesitzAREintrag.zugeordneteMeldungenListe
                .get(lfdNrZugeordneteMeldung).zugeordneteWillenserklaerungenList.get(lfdNrWillenserklaerung);

        if (lWillenserklaerungStatus.getWillenserklaerung() == KonstWillenserklaerung.neueZutrittsIdentZuMeldung
                || lWillenserklaerungStatus
                        .getWillenserklaerung() == KonstWillenserklaerung.neueZutrittsIdentZuMeldung_VollmachtAnDritte
                || lWillenserklaerungStatus
                        .getWillenserklaerung() == KonstWillenserklaerung.vollmachtAnDritte) {/******************************************
                                                                                               * Korrigieren
                                                                                               ****************************/

            EclWillenserklaerung eclWillenserklaerung = new EclWillenserklaerung();
            eclWillenserklaerung.willenserklaerungIdent = lWillenserklaerungStatus.getWillenserklaerungIdent();

            EclWillenserklaerung eclWillenserklaerung2 = new EclWillenserklaerung();
            eclWillenserklaerung2.willenserklaerungIdent = lWillenserklaerungStatus.getWillenserklaerungIdent2();

            WSClient wsClient = new WSClient();

            WELoginVerify weLoginVerify = wsClient.fuelleLoginVerify(eclTLoginDatenM);

            WEEintrittskarteKorrigieren weEintrittskarteKorrigieren = new WEEintrittskarteKorrigieren();
            weEintrittskarteKorrigieren.setWeLoginVerify(weLoginVerify);
            weEintrittskarteKorrigieren.setWillenserklaerung(eclWillenserklaerung);
            weEintrittskarteKorrigieren.setWillenserklaerung2(eclWillenserklaerung2);

            WEEintrittskarteKorrigieren weEintrittskarteKorrigierenRC = wsClient
                    .eintrittskarteKorrigierenGET(weEintrittskarteKorrigieren);

            int rc = weEintrittskarteKorrigierenRC.getRc();
            if (rc < 1) { /*Fehlerbehandlung*/
                switch (rc) {
                case CaFehler.afAndererUserAktiv: {
                    tfFehlermeldung.setText(
                            "Datensatz mittlerweile von anderem User verändert! Bitte abbrechen und erneut einlesen.");
                    break;
                }
                default: {
                    tfFehlermeldung
                            .setText("Programmierer verständigen! Fehler " + rc + " " + CaFehler.getFehlertext(rc, 0));
                    break;
                }
                }
                setzeStatusAbbruch();
                return;
            }

            Stage neuerDialog = new Stage();

            CtrlEineEintrittskarteKorrigieren controllerEineEintrittskarteKorrigieren = new CtrlEineEintrittskarteKorrigieren();

            controllerEineEintrittskarteKorrigieren.init(neuerDialog, eclTLoginDatenM,
                    eclBesitzAREintrag.zugeordneteMeldungenListe.get(lfdNrZugeordneteMeldung),
                    weEintrittskarteKorrigierenRC.willenserklaerung,
                    weEintrittskarteKorrigierenRC.willenserklaerungZusatz,
                    weEintrittskarteKorrigierenRC.willenserklaerung2,
                    weEintrittskarteKorrigierenRC.willenserklaerungZusatz2, weEintrittskarteKorrigierenRC.personNatJur);

            FXMLLoader loader = new FXMLLoader(getClass().getResource("EineEintrittskarteKorrigieren.fxml"));
            loader.setController(controllerEineEintrittskarteKorrigieren);
            Parent mainPane = null;
            try {
                mainPane = (Parent) loader.load();
            } catch (IOException e) {
                CaBug.drucke("CtrlStatus.clickedAendern 002");
                e.printStackTrace();
            }
            Scene scene = new Scene(mainPane, 1100, 800);
            neuerDialog.setTitle("Korrigieren Eintrittskarte");
            neuerDialog.setScene(scene);
            neuerDialog.initModality(Modality.APPLICATION_MODAL);
            neuerDialog.showAndWait();

            neuEinlesenUndAnzeigen(aktionaersNummerInArbeit);

            return;
        }

        /************ Normales Ändern ********************/

        int lFunktion = 0;
        switch (lWillenserklaerungStatus.getWillenserklaerung()) {
        case KonstWillenserklaerung.vollmachtUndWeisungAnSRV: {
            lFunktion = KonstPortalAktion.SRV_AENDERN;
            break;
        }
        case KonstWillenserklaerung.briefwahl: {
            lFunktion = KonstPortalAktion.BRIEFWAHL_AENDERN;
            break;
        }
        case KonstWillenserklaerung.vollmachtUndWeisungAnKIAV: {
            lFunktion = KonstPortalAktion.KIAV_WEISUNG_AENDERN;
            break;
        }
        case KonstWillenserklaerung.dauervollmachtAnKIAV: {
            lFunktion = KonstPortalAktion.DAUERVOLLMACHT_AENDERN;
            break;
        }
        case KonstWillenserklaerung.organisatorischMitWeisungInSammelkarte: {
            lFunktion = KonstPortalAktion.ORGANISATORISCH_AENDERN;
            break;
        }
        default:
            break;
        }

        /*Vollmacht/Weisung-SRV-Fenster aufrufen*/
        Stage neuerDialog = new Stage();
        CaIcon.master(neuerDialog);

        CtrlVollmachtWeisung controllerFenster = new CtrlVollmachtWeisung();

        controllerFenster.init(neuerDialog, lFunktion, 2, eclTLoginDatenM,
                eclBesitzAREintrag.zugeordneteMeldungenListe.get(lfdNrZugeordneteMeldung), lWillenserklaerungStatus);

        CaController caController = new CaController();
        caController.open(neuerDialog, controllerFenster,
                "/de/meetingapps/meetingclient/meetingBestand/VollmachtWeisung.fxml", 1500, 760,
                "Ändern Weisung", true);

        neuEinlesenUndAnzeigen(aktionaersNummerInArbeit);

    }

    /**
     * Clickedweitere WK.
     *
     * @param event the event
     */
    @FXML
    void clickedweitereWK(ActionEvent event) {
        int i;

        int anzahlMeldungen = eclBesitzAREintrag.liefereAnzZugeordneteMeldungenEigeneAktien();
        int gef = -1;
        for (i = 0; i < anzahlMeldungen; i++) {
            if (event.getSource() == btnMeldungWeitereWK[i]) {
                gef = i;
            }
        }
        lfdNrZugeordneteMeldung = gef;

        bereiteTabWeitereWK();

        tabWeitereWK.setDisable(false);
        tabStatus.setDisable(true);
        tabErstanmeldung.setDisable(true);

        SingleSelectionModel<Tab> selectionModel = tpTabPane.getSelectionModel();
        selectionModel.select(tabWeitereWK);

        btnZurueck.setDisable(false);

        tfFehlermeldung.setText("");

    }

    /**
     * Clicked meldung stornieren.
     *
     * @param event the event
     */
    @FXML
    void clickedMeldungStornieren(ActionEvent event) {
        int i;

        String fehlerString = SClErfassungsDaten.pruefeEingaben(tfErklaerungDatum, tfErklaerungZeit);
        if (fehlerString != null) {
            tfFehlermeldung.setText(fehlerString);
            return;
        }

        int anzahlMeldungen = eclBesitzAREintrag.liefereAnzZugeordneteMeldungenEigeneAktien();
        int gef = -1;
        for (i = 0; i < anzahlMeldungen; i++) {
            if (event.getSource() == btnMeldungStornieren[i]) {
                gef = i;
            }
        }
        lfdNrZugeordneteMeldung = gef;

        Stage neuerDialog = new Stage();

        CtrlStornoMeldungBestaetigung controllerMeldungBestaetigung = new CtrlStornoMeldungBestaetigung();

        controllerMeldungBestaetigung.init(neuerDialog);

        FXMLLoader loader = new FXMLLoader(getClass().getResource("StornoMeldungBestaetigung.fxml"));
        loader.setController(controllerMeldungBestaetigung);
        Parent mainPane = null;
        try {
            mainPane = (Parent) loader.load();
        } catch (IOException e) {
            CaBug.drucke("CtrlStatus.clickedMeldungStornieren 001");
            e.printStackTrace();
        }
        Scene scene = new Scene(mainPane, 400, 400);
        neuerDialog.setTitle("Stornieren bestätigen");
        neuerDialog.setScene(scene);
        neuerDialog.initModality(Modality.APPLICATION_MODAL);
        neuerDialog.showAndWait();

        if (controllerMeldungBestaetigung.isErgebnis()) {
            WSClient wsClient = new WSClient();

            WELoginVerify weLoginVerify = wsClient.fuelleLoginVerify(eclTLoginDatenM);

            SClErfassungsDaten.fuelleLoginVerify(weLoginVerify, tfErklaerungDatum, tfErklaerungZeit, cbEingangsWeg);

            WEAnmeldungStornieren weAnmeldungStornieren = new WEAnmeldungStornieren();
            weAnmeldungStornieren.setWeLoginVerify(weLoginVerify);
            weAnmeldungStornieren.setMeldungsIdent(
                    eclBesitzAREintrag.zugeordneteMeldungenListe.get(lfdNrZugeordneteMeldung).getMeldungsIdent());

            WEAnmeldungStornierenRC weAnmeldungStornierenRC = wsClient.anmeldungStornieren(weAnmeldungStornieren);

            int rc = weAnmeldungStornierenRC.getRc();
            if (rc < 1) { /*Fehlerbehandlung*/
                switch (rc) {
                case CaFehler.pmWillenserklaerungenVorhanden: {
                    tfFehlermeldung.setText("Stornieren nicht möglich - noch Willenserklärungen vorhanden!");
                    return;
                }
                case CaFehler.afAndererUserAktiv: {
                    tfFehlermeldung.setText(
                            "Datensatz mittlerweile von anderem User verändert! Bitte abbrechen und erneut einlesen.");
                    break;
                }
                default: {
                    tfFehlermeldung
                            .setText("Programmierer verständigen! Fehler " + rc + " " + CaFehler.getFehlertext(rc, 0));
                    break;
                }
                }
                setzeStatusAbbruch();
                return;
            } else if (rc == 1 && ParamS.param.paramBasis.inhaberaktienAktiv
                    && !ParamS.param.paramBasis.namensaktienAktiv) {
                clickedSperren();
            }

        }
        neuEinlesenUndAnzeigen(aktionaersNummerInArbeit);
    }

    /**
     * Clicked meldung fix aendern.
     *
     * @param event the event
     */
    @FXML
    void clickedMeldungFixAendern(ActionEvent event) {
        int i;

        String fehlerString = SClErfassungsDaten.pruefeEingaben(tfErklaerungDatum, tfErklaerungZeit);
        if (fehlerString != null) {
            tfFehlermeldung.setText(fehlerString);
            return;
        }

        int anzahlMeldungen = eclBesitzAREintrag.liefereAnzZugeordneteMeldungenEigeneAktien();
        int gef = -1;
        for (i = 0; i < anzahlMeldungen; i++) {
            if (event.getSource() == btnMeldungFixAendern[i]) {
                gef = i;
            }
        }
        lfdNrZugeordneteMeldung = gef;

        Stage neuerDialog = new Stage();

        CtrlAnmeldungFixAendern controllerAnmeldungFixAendern = new CtrlAnmeldungFixAendern();

        controllerAnmeldungFixAendern.init(neuerDialog);

        FXMLLoader loader = new FXMLLoader(getClass().getResource("AnmeldungFixAendern.fxml"));
        loader.setController(controllerAnmeldungFixAendern);
        Parent mainPane = null;
        try {
            mainPane = (Parent) loader.load();
        } catch (IOException e) {
            CaBug.drucke("CtrlStatus.clickedMeldungFixAendern 001");
            e.printStackTrace();
        }
        Scene scene = new Scene(mainPane, 400, 400);
        neuerDialog.setTitle("In Fix ändern mit neuer Aktienzahl");
        neuerDialog.setScene(scene);
        neuerDialog.initModality(Modality.APPLICATION_MODAL);
        neuerDialog.showAndWait();

        if (controllerAnmeldungFixAendern.isErgebnis()) {
            WSClient wsClient = new WSClient();

            WELoginVerify weLoginVerify = wsClient.fuelleLoginVerify(eclTLoginDatenM);

            SClErfassungsDaten.fuelleLoginVerify(weLoginVerify, tfErklaerungDatum, tfErklaerungZeit, cbEingangsWeg);

            WEBestandFixAendern weBestandFixAendern = new WEBestandFixAendern();
            weBestandFixAendern.setWeLoginVerify(weLoginVerify);
            weBestandFixAendern.setMeldeint(
                    eclBesitzAREintrag.zugeordneteMeldungenListe.get(lfdNrZugeordneteMeldung).getMeldungsIdent());
            weBestandFixAendern.setAnzahlAktienFix(controllerAnmeldungFixAendern.getNeuAktien());
            WEBestandFixAendernRC weBestandFixAendernRC = wsClient.bestandFixAendern(weBestandFixAendern);

            int rc = weBestandFixAendernRC.getRc();
            if (rc < 1) { /*Fehlerbehandlung*/
                switch (rc) {
                case CaFehler.pmNichtAngemeldeterBestandZuKlein: {
                    tfFehlermeldung.setText("Bestand zu klein!");
                    return;
                }
                case CaFehler.afAndererUserAktiv: {
                    tfFehlermeldung.setText(
                            "Datensatz mittlerweile von anderem User verändert! Bitte abbrechen und erneut einlesen.");
                    break;
                }
                default: {
                    tfFehlermeldung
                            .setText("Programmierer verständigen! Fehler " + rc + " " + CaFehler.getFehlertext(rc, 0));
                    break;
                }
                }
                setzeStatusAbbruch();
                return;
            }

        } else {

        }

        neuEinlesenUndAnzeigen(aktionaersNummerInArbeit);

    }

    /**
     * Clicked bearbeiten.
     *
     * @param event the event
     */
    @FXML
    void clickedBearbeiten(ActionEvent event) {

        Stage newStage = new Stage();
        CaIcon.bestandsverwaltung(newStage);

        CtrlMeldedaten controllerFenster = new CtrlMeldedaten();
        controllerFenster.init(newStage, eclTLoginDatenM);

        CaController caController = new CaController();
        caController.open(newStage, controllerFenster,
                "/de/meetingapps/meetingclient/meetingBestand/Meldedaten.fxml", 1300, 670, "Meldedaten", true);

        if (controllerFenster.geandert) {
            clickedAbbruch(new ActionEvent());
            tfAktionaersnummer.setText(controllerFenster.eintrag.aktionaersnummer);
            doEinlesen();
        }
        tfAktionaersnummer.requestFocus();
    }

    /**
     * Setze status aktionaersnummer eingeben.
     */
    private void setzeStatusAktionaersnummerEingeben() {
        btnEinlesen.setDisable(false);
        btnSuchen.setDisable(false);
        btnDetailPDF.setDisable(true);
        btnAbbruch.setDisable(true);
        btnZurueck.setDisable(true);
        btnBearbeiten.setDisable(false);
        btnBearbeiten.setText("Neu Anlegen");
        eclTLoginDatenM = null;

        tfAktionaersnummer.setText("");
        tfNameVorname.setText("");
        tfOrt.setText("");
        tfAktien.setText("");
        tfAktien.setText("");
        tfFehlermeldung.setText("");
        tfAktionaersnummer.setEditable(true);
        tfAktionaersnummer.requestFocus();

        tfNameVorname.setEditable(false);
        tfOrt.setEditable(false);
        tfAktien.setEditable(false);

        lblHinweise.setText("");

        int i;
        for (i = 0; i < anzAgenda; i++) {
            rbAgendaJa[i].setSelected(false);
            rbAgendaNein[i].setSelected(false);
            rbAgendaEnthaltung[i].setSelected(false);
            rbAgendaUngueltig[i].setSelected(false);
        }
        for (i = 0; i < anzGegenantraege; i++) {
            rbGegenantraegeJa[i].setSelected(false);
            rbGegenantraegeNein[i].setSelected(false);
            rbGegenantraegeEnthaltung[i].setSelected(false);
            rbGegenantraegeUngueltig[i].setSelected(false);
        }

        grpnStatus = new GridPane();
        scrpnStatus.setContent(grpnStatus);

        grpnWeitereWK = new GridPane();
        scrpnWeitereWillenserklaerung.setContent(grpnWeitereWK);

        grpnErstanmeldung = new GridPane();
        scrpnErstanmeldung.setContent(grpnErstanmeldung);

    }

    /**
     * Setze status verarbeitung.
     */
    private void setzeStatusVerarbeitung() {
        btnEinlesen.setDisable(true);
        btnSuchen.setDisable(true);
        btnDetailPDF.setDisable(false);
        btnAbbruch.setDisable(false);
        btnZurueck.setDisable(true);
        btnBearbeiten.setDisable(false);

        btnBearbeiten.setText("Bearbeiten");

        tfAktionaersnummer.setEditable(false);
    }

    /**
     * Setze status abbruch.
     */
    private void setzeStatusAbbruch() {
        btnEinlesen.setDisable(true);
        btnSuchen.setDisable(true);
        btnDetailPDF.setDisable(false);
        btnAbbruch.setDisable(false);
        btnZurueck.setDisable(true);
        btnBearbeiten.setDisable(true);

        tfAktionaersnummer.setEditable(false);
        btnAbbruch.requestFocus();

        grpnStatus = new GridPane();
        scrpnStatus.setContent(grpnStatus);

        grpnWeitereWK = new GridPane();
        scrpnWeitereWillenserklaerung.setContent(grpnWeitereWK);

        grpnErstanmeldung = new GridPane();
        scrpnErstanmeldung.setContent(grpnErstanmeldung);

    }

    /**
     * Sets the ze stimmausschluss.
     *
     * @param meldung the new ze stimmausschluss
     */
    private void setzeStimmausschluss(EclZugeordneteMeldungNeu meldung) {

        // Kennzeichen definieren
        final String[] kennzeichen = { "V", "A", "S", "1", "2", "3", "4", "5", "6", "7", "8", "9", "E" };

        // Verbindung von Kennzeichen und CheckBox
        // CheckBox hat keine moeglichkeit das dass Label ueber der Box steht
        Map<CheckBox, String> map = new LinkedHashMap<>();

        // Maske Stimmausschluss
        Dialog<String> dialog = new Dialog<String>();
        dialog.setTitle("Stimmausschluss");
        dialog.setHeaderText("Wähle Stimmausschluss Kennzeichen");
        ButtonType saveButtonType = new ButtonType("Speichern", ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);

        // Content Kennzeichen und CheckBox
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(5);
        grid.setPadding(new Insets(20, 10, 10, 10));
        int x = 0;
        
        /**
         * Aufbau 
         * Reihe 1 - Label
         * Reihe 2 - CheckBox
         */
        for (String s : kennzeichen) {
            CheckBox c = new CheckBox();
            c.setSelected(meldung.eclMeldung.stimmausschluss.contains(s));
            map.put(c, s);
            Label lbl = new Label(s);
            grid.add(lbl, x, 0);
            GridPane.setHalignment(lbl, HPos.CENTER);
            grid.add(c, x++, 1);
        }

        dialog.getDialogPane().setContent(grid);

        /**
         * Ergebnis - Prueft alle CheckBoxen ob selektiert und gibt Kennzeichen
         * Alle Kennzeichen ergeben einen String
         */
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == saveButtonType) {
                StringBuilder sb = new StringBuilder();
                for (var entry : map.entrySet()) {
                    if (entry.getKey().isSelected()) {
                        sb.append(entry.getValue());
                    }
                }
                return sb.toString();
            }
            return null;
        });

        Optional<String> result = dialog.showAndWait();

        
        /**
         * Bei speichern pruefen ob Veraenderung
         * true -> Meldung aktualisieren und neue Einlesen
         */
        result.ifPresent(stimmausschluss -> {
            if (!meldung.eclMeldung.stimmausschluss.trim().equals(stimmausschluss)) {
                BlMeldungen blMeldungen = new BlMeldungen(false, new DbBundle(), ParamS.clGlobalVar.mandant);
                blMeldungen.eclMeldung = meldung.eclMeldung;
                blMeldungen.eclMeldung.stimmausschluss = stimmausschluss;
                blMeldungen.updateText = "Stimmausschluss";
                int erg = blMeldungen.updateMeldung(false);
                if (erg == 1) {
                    neuEinlesenUndAnzeigen(aktionaersNummerInArbeit);
                    tfFehlermeldung.setText("Stimmausschluss - " + stimmausschluss + " - gespeichert");
                } else {
                    btnAbbruch.fire();
                    tfFehlermeldung.setText(CaFehler.getFehlertext(erg, 0).trim() + "\n Bitte Aktionär neu einlesen.");
                    tfAktionaersnummer.setText(aktionaersNummerInArbeit);
                }
            }
        });
    }

    /**
     * Neu einlesen und anzeigen.
     *
     * @param aktionaersnummer the aktionaersnummer
     */
    private void neuEinlesenUndAnzeigen(String aktionaersnummer) {

        CaBug.druckeLog("CtrlStatus.neuEinlesenUndAnzeigen", logDrucken, 10);
        CaBug.druckeLog(aktionaersnummer, logDrucken, 10);

        WSClient wsClient = new WSClient();

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

        weLogin.setKennung(aktionaersnummer);
        WELoginCheck weLoginCheck = new WELoginCheck();
        weLoginCheck.weLogin = weLogin;

        /*Die Rückgegebenen Aktionärsdaten speichern, da sie für weitere Aktionen
         * benötigt werden
         */
        WELoginCheckRC weLoginCheckRC = null;
        weLoginCheckRC = wsClient.loginCheck(weLoginCheck);
        eclTLoginDatenM = weLoginCheckRC.eclTLoginDatenM;

        int rc = weLoginCheckRC.getRc();
        if (rc < 1) { /*Fehlerbehandlung*/
            switch (rc) {
            case CaFehler.afKennungUnbekannt: {
                tfFehlermeldung.setText("Aktionärsnummer gibt es nicht!");
                break;
            }
            default: {
                tfFehlermeldung
                        .setText("Programmierer verständigen! Fehler " + rc + " " + CaFehler.getFehlertext(rc, 0));
                break;
            }
            }
            setzeStatusAbbruch();
            return;

        }
        anzRegistereintraege = weLoginCheckRC.anzahlAktionaersnummernVorhanden;

        tfNameVorname.setText(eclTLoginDatenM.liefereVornameNameTitel());
        tfOrt.setText(eclTLoginDatenM.ort);
        tfAktien.setText(eclTLoginDatenM.stimmenDE);
        if (anzRegistereintraege > 1) {
            lblHinweise.setText("Achtung - mehrere Registereinträge zu dieser Aktionärsnummer");
        }

        /**************************
         * Aktionärs-Status holen
         *****************************************/

        WELoginVerify weLoginVerify = wsClient.fuelleLoginVerify(eclTLoginDatenM);

        weTeilnehmerStatusGetRC = wsClient.teilnehmerStatusGet(weLoginVerify);
        rc = weTeilnehmerStatusGetRC.getRc();

        if (rc < 1) { /*Fehlerbehandlung*/
            switch (rc) {
            default: {
                tfFehlermeldung
                        .setText("Programmierer verständigen! Fehler " + rc + " " + CaFehler.getFehlertext(rc, 0));
                break;
            }
            }
            setzeStatusAbbruch();
            return;
        }

        if (weTeilnehmerStatusGetRC.besitzJeKennungListe == null) {
            tfFehlermeldung.setText("Programmierer verständigen! Fehler 001");
            setzeStatusAbbruch();
            return;
        }
        if (weTeilnehmerStatusGetRC.besitzJeKennungListe.size() < 1) {
            tfFehlermeldung.setText("Programmierer verständigen! Fehler 002");
            setzeStatusAbbruch();
            return;
        }
        EclBesitzJeKennung eclBesitzJeKennung = weTeilnehmerStatusGetRC.besitzJeKennungListe.get(0);

        if (eclBesitzJeKennung.eigenerAREintragVorhanden == false) {
            tfFehlermeldung.setText("Programmierer verständigen! Fehler 003");
            setzeStatusAbbruch();
            return;
        }
        if (eclBesitzJeKennung.eigenerAREintragListe.size() < 1) {
            tfFehlermeldung.setText("Programmierer verständigen! Fehler 004");
            setzeStatusAbbruch();
            return;
        }

        eclBesitzAREintrag = eclBesitzJeKennung.eigenerAREintragListe.get(0);

        if (eclBesitzAREintrag.angemeldet) { /*Status anzeigen*/
            bereiteTabStatus();

            tabStatus.setDisable(false);
            tabErstanmeldung.setDisable(true);
            tabWeitereWK.setDisable(true);

            SingleSelectionModel<Tab> selectionModel = tpTabPane.getSelectionModel();
            selectionModel.select(tabStatus);
            tfFehlermeldung.setText("");
            setzeStatusVerarbeitung();
            setzeEingangsweg();
            return;
        } else {/*Erstanmeldung*/

            bereiteTabErstanmeldung();

            tabErstanmeldung.setDisable(false);
            tabStatus.setDisable(true);
            tabWeitereWK.setDisable(true);

            SingleSelectionModel<Tab> selectionModel = tpTabPane.getSelectionModel();
            selectionModel.select(tabErstanmeldung);
            tfAktienAngemeldet.setText("0");
            tfFehlermeldung.setText("Anmeldung kann durchgeführt werden.");
        }

        setzeStatusVerarbeitung();

    }

    /**
     * Clicked detail PDF.
     *
     * @param event the event
     */
    @FXML
    void clickedDetailPDF(ActionEvent event) {
        WSClient wsClient = new WSClient();

        WELoginVerify weLoginVerify = wsClient.fuelleLoginVerify(eclTLoginDatenM);
        WEAktionaersStatusDetailGet weAktionaersStatusDetailGet = new WEAktionaersStatusDetailGet();
        weAktionaersStatusDetailGet.weLoginVerify = weLoginVerify;
        weAktionaersStatusDetailGet.aktionaersnummer = aktionaersNummerInArbeit;

        WEAktionaersStatusDetailGetRC weAktionaersStatusDetailGetRC = wsClient
                .aktionaersStatusDetailGet(weAktionaersStatusDetailGet);

        int rc = weAktionaersStatusDetailGetRC.getRc();
        if (rc < 0) { /*Fehlerbehandlung*/
            switch (rc) {
            case CaFehler.afKennungUnbekannt: {
                tfFehlermeldung.setText("Aktionärsnummer gibt es nicht!");
                break;
            }
            default: {
                tfFehlermeldung
                        .setText("Programmierer verständigen! Fehler " + rc + " " + CaFehler.getFehlertext(rc, 0));
                break;
            }
            }
            setzeStatusAbbruch();
            return;
        }

        DbBundle lDbBundle = new DbBundle(); /*Nur wegen mandant*/

        RpDrucken rpDrucken = new RpDrucken();
        rpDrucken.initClientStandardPDF();

        /** Hier noch einbauen, dass in PDF und automatisch angezeigt wird */

        RepWillenserklaerungenAktionaer lWillenserklaerungenAktionaer = new RepWillenserklaerungenAktionaer(false,
                lDbBundle);
        lWillenserklaerungenAktionaer.init(rpDrucken);

        BlWillenserklaerungStatus blWillenserklaerungStatus = new BlWillenserklaerungStatus(lDbBundle);

        blWillenserklaerungStatus.piSelektionGeberOderAlle = weAktionaersStatusDetailGetRC.piSelektionGeberOderAlle;
        blWillenserklaerungStatus.piRueckgabeKurzOderLang = weAktionaersStatusDetailGetRC.piRueckgabeKurzOderLang;
        blWillenserklaerungStatus.piAnsichtVerarbeitungOderAnalyse = weAktionaersStatusDetailGetRC.piAnsichtVerarbeitungOderAnalyse;
        blWillenserklaerungStatus.piAlleWillenserklaerungen = weAktionaersStatusDetailGetRC.piAlleWillenserklaerungen;
        blWillenserklaerungStatus.zugeordneteMeldungenEigeneAktienArray = weAktionaersStatusDetailGetRC.zugeordneteMeldungenEigeneAktienArray;
        blWillenserklaerungStatus.gastKartenGemeldetEigeneAktien = weAktionaersStatusDetailGetRC.gastKartenGemeldetEigeneAktien;
        blWillenserklaerungStatus.aktienregisterPersonNatJurIdent = weAktionaersStatusDetailGetRC.aktienregisterPersonNatJurIdent;
        blWillenserklaerungStatus.zugeordneteMeldungenEigeneGastkartenArray = weAktionaersStatusDetailGetRC.zugeordneteMeldungenEigeneGastkartenArray;
        blWillenserklaerungStatus.zugeordneteMeldungenBevollmaechtigtArray = weAktionaersStatusDetailGetRC.zugeordneteMeldungenBevollmaechtigtArray;
        blWillenserklaerungStatus.briefwahlVorhanden = weAktionaersStatusDetailGetRC.briefwahlVorhanden;
        blWillenserklaerungStatus.srvVorhanden = weAktionaersStatusDetailGetRC.srvVorhanden;
        blWillenserklaerungStatus.rcHatNichtNurPortalWillenserklaerungen = weAktionaersStatusDetailGetRC.rcHatNichtNurPortalWillenserklaerungen;
        blWillenserklaerungStatus.rcDatumLetzteWillenserklaerung = weAktionaersStatusDetailGetRC.rcDatumLetzteWillenserklaerung;
        blWillenserklaerungStatus.rcListeVollmachten = weAktionaersStatusDetailGetRC.rcListeVollmachten;

        lWillenserklaerungenAktionaer.druckeEinzelnenAktionaer(lDbBundle, aktionaersNummerInArbeit,
                weAktionaersStatusDetailGetRC.eclAktienregisterEintrag, blWillenserklaerungStatus);

        rpDrucken.endeListe();
        tfFehlermeldung.setText(rpDrucken.drucklaufDatei);
        CaPDFAnzeigen.anzeigenDatei(rpDrucken.drucklaufDatei);

    }

    /**
     * Gets the tf aktionaersnummer.
     *
     * @return the tf aktionaersnummer
     */
    public TextField getTfAktionaersnummer() {
        return tfAktionaersnummer;
    }

    /**
     * Sets the tf aktionaersnummer.
     *
     * @param tfAktionaersnummer the new tf aktionaersnummer
     */
    public void setTfAktionaersnummer(TextField tfAktionaersnummer) {
        this.tfAktionaersnummer = tfAktionaersnummer;
    }

    /** The eigene stage. */
    private Stage eigeneStage;

    /**
     * Funktion: 4 = Vollmacht/Weisung an SRV 5 = Briefwahl.
     *
     * @param pEigeneStage the eigene stage
     * @param pFunktion    the funktion
     */
    public void init(Stage pEigeneStage, int pFunktion) {
        eigeneStage = pEigeneStage;
        ausgewaehlteFunktion = pFunktion;

    }

}
