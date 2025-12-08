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
package de.meetingapps.meetingclient.meetingServiceDesk;

import java.net.URL;
import java.util.ResourceBundle;

import de.meetingapps.meetingclient.meetingClientDialoge.CaController;
import de.meetingapps.meetingclient.meetingClientDialoge.CaIcon;
import de.meetingapps.meetingclient.meetingClientDialoge.CtrlRoot;
import de.meetingapps.meetingclient.meetingClientDialoge.CtrlSuchen;
import de.meetingapps.meetingclient.meetingClientOberflaechen.CaOpenWindow;
import de.meetingapps.meetingclient.meetingClientOberflaechen.ObjectActions;
import de.meetingapps.meetingportal.meetComDb.DbBundle;
import de.meetingapps.meetingportal.meetComEntities.EclAktienregister;
import de.meetingapps.meetingportal.meetComEntities.EclMeldung;
import de.meetingapps.meetingportal.meetComEntities.EclSuchergebnis;
import de.meetingapps.meetingportal.meetComKonst.KonstServicedesk;
import de.meetingapps.meetingportal.meetComStub.WSClient;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

/**
 * The Class CtrlHauptStage.
 */
public class CtrlHauptStage extends CtrlRoot {
    
    @FXML
    private AnchorPane rootPane;

    /** The resources. */
    @FXML
    private ResourceBundle resources;

    /** The location. */
    @FXML
    private URL location;

    /** The btn suchen teilnehmer. */
    @FXML
    private Button btnSuchenTeilnehmer;

    /** The btn neutrale dokumente. */
    @FXML
    private Button btnNeutraleDokumente;

    /** The btn neue gastkarte. */
    @FXML
    private Button btnNeueGastkarte;

    /** The tp ablauf. */
    @FXML
    private TabPane tpAblauf;

    /** The t personenart. */
    @FXML
    private Tab tPersonenart;

    /** The rb aktionaer. */
    @FXML
    private RadioButton rbAktionaer;

    /** The tg personenart. */
    @FXML
    private ToggleGroup tgPersonenart;

    /** The rb vertreter. */
    @FXML
    private RadioButton rbVertreter;

    /** The rb gastkarte vergessen. */
    @FXML
    private RadioButton rbGastkarteVergessen;

    /** The tg personenart 1. */
    @FXML
    private ToggleGroup tgPersonenart1;

    /** The rb begleitperson. */
    @FXML
    private RadioButton rbBegleitperson;

    /** The rb gastkarte. */
    @FXML
    private RadioButton rbGastkarte;

    /** The rb unbekannt. */
    @FXML
    private RadioButton rbUnbekannt;

    /** The btn weiter. */
    @FXML
    private Button btnWeiter;

    /** The btn direkt suche. */
    @FXML
    private Button btnDirektSuche;

    /** The btn direkt gastkarte. */
    @FXML
    private Button btnDirektGastkarte;

    /** The t id aktionaer. */
    @FXML
    private Tab tIdAktionaer;

    /** The lbl id aktionaer EK. */
    @FXML
    private Label lblIdAktionaerEK;

    /** The lbl id aktionaer SK. */
    @FXML
    private Label lblIdAktionaerSK;

    /** The lbl id aktionaer einladung. */
    @FXML
    private Label lblIdAktionaerEinladung;

    /** The btn weiter id aktionaer. */
    @FXML
    private Button btnWeiterIdAktionaer;

    /** The btn zurueck id aktionaer. */
    @FXML
    private Button btnZurueckIdAktionaer;

    /** The t id vertreter. */
    @FXML
    private Tab tIdVertreter;

    /** The lbl id vertreter EK. */
    @FXML
    private Label lblIdVertreterEK;

    /** The lbl id vertreter SK. */
    @FXML
    private Label lblIdVertreterSK;

    /** The lbl id vertreter einladung. */
    @FXML
    private Label lblIdVertreterEinladung;

    /** The lbl id vertreter vollmacht. */
    @FXML
    private Label lblIdVertreterVollmacht;

    /** The btn weiter id vertreter. */
    @FXML
    private Button btnWeiterIdVertreter;

    /** The btn zurueck id vertreter. */
    @FXML
    private Button btnZurueckIdVertreter;

    /** The t id gast. */
    @FXML
    private Tab tIdGast;

    /** The lbl id gast gastkarte. */
    @FXML
    private Label lblIdGastGastkarte;

    /** The btn weiter id gast. */
    @FXML
    private Button btnWeiterIdGast;

    /** The btn zurueck id gast. */
    @FXML
    private Button btnZurueckIdGast;

    /** The t id unbekannt. */
    @FXML
    private Tab tIdUnbekannt;

    /** The lbl id unbekannt EK. */
    @FXML
    private Label lblIdUnbekanntEK;

    /** The lbl id unbekannt SK. */
    @FXML
    private Label lblIdUnbekanntSK;

    /** The lbl id unbekannt einladung. */
    @FXML
    private Label lblIdUnbekanntEinladung;

    /** The lbl id unbekannt vollmacht. */
    @FXML
    private Label lblIdUnbekanntVollmacht;

    /** The lbl id unbekannt gastkarte. */
    @FXML
    private Label lblIdUnbekanntGastkarte;

    /** The btn weiter id unbekannt. */
    @FXML
    private Button btnWeiterIdUnbekannt;

    /** The btn zurueck id unbekannt. */
    @FXML
    private Button btnZurueckIdUnbekannt;

    /** Siehe KonstServicedesk.PERSONENART_ */
    private int ausgewaehltePersonenart = 0;

    /**
     * Initialize.
     */
    @FXML
    void initialize() {
        assert btnSuchenTeilnehmer != null : "fx:id=\"btnSuchenTeilnehmer\" was not injected: check your FXML file 'HauptStage.fxml'.";
        assert btnNeutraleDokumente != null : "fx:id=\"btnNeutraleDokumente\" was not injected: check your FXML file 'HauptStage.fxml'.";
        assert btnNeueGastkarte != null : "fx:id=\"btnNeueGastkarte\" was not injected: check your FXML file 'HauptStage.fxml'.";
        assert tpAblauf != null : "fx:id=\"tpAblauf\" was not injected: check your FXML file 'HauptStage.fxml'.";
        assert tPersonenart != null : "fx:id=\"tPersonenart\" was not injected: check your FXML file 'HauptStage.fxml'.";
        assert rbAktionaer != null : "fx:id=\"rbAktionaer\" was not injected: check your FXML file 'HauptStage.fxml'.";
        assert tgPersonenart != null : "fx:id=\"tgPersonenart\" was not injected: check your FXML file 'HauptStage.fxml'.";
        assert rbVertreter != null : "fx:id=\"rbVertreter\" was not injected: check your FXML file 'HauptStage.fxml'.";
        assert rbGastkarteVergessen != null : "fx:id=\"rbGastkarteVergessen\" was not injected: check your FXML file 'HauptStage.fxml'.";
        assert tgPersonenart1 != null : "fx:id=\"tgPersonenart1\" was not injected: check your FXML file 'HauptStage.fxml'.";
        assert rbBegleitperson != null : "fx:id=\"rbBegleitperson\" was not injected: check your FXML file 'HauptStage.fxml'.";
        assert rbGastkarte != null : "fx:id=\"rbGastkarte\" was not injected: check your FXML file 'HauptStage.fxml'.";
        assert rbUnbekannt != null : "fx:id=\"rbUnbekannt\" was not injected: check your FXML file 'HauptStage.fxml'.";
        assert btnWeiter != null : "fx:id=\"btnWeiter\" was not injected: check your FXML file 'HauptStage.fxml'.";
        assert btnDirektSuche != null : "fx:id=\"btnDirektSuche\" was not injected: check your FXML file 'HauptStage.fxml'.";
        assert btnDirektGastkarte != null : "fx:id=\"btnDirektGastkarte\" was not injected: check your FXML file 'HauptStage.fxml'.";
        assert tIdAktionaer != null : "fx:id=\"tIdAktionaer\" was not injected: check your FXML file 'HauptStage.fxml'.";
        assert lblIdAktionaerEK != null : "fx:id=\"lblIdAktionaerEK\" was not injected: check your FXML file 'HauptStage.fxml'.";
        assert lblIdAktionaerSK != null : "fx:id=\"lblIdAktionaerSK\" was not injected: check your FXML file 'HauptStage.fxml'.";
        assert lblIdAktionaerEinladung != null : "fx:id=\"lblIdAktionaerEinladung\" was not injected: check your FXML file 'HauptStage.fxml'.";
        assert btnWeiterIdAktionaer != null : "fx:id=\"btnWeiterIdAktionaer\" was not injected: check your FXML file 'HauptStage.fxml'.";
        assert btnZurueckIdAktionaer != null : "fx:id=\"btnZurueckIdAktionaer\" was not injected: check your FXML file 'HauptStage.fxml'.";
        assert tIdVertreter != null : "fx:id=\"tIdVertreter\" was not injected: check your FXML file 'HauptStage.fxml'.";
        assert lblIdVertreterEK != null : "fx:id=\"lblIdVertreterEK\" was not injected: check your FXML file 'HauptStage.fxml'.";
        assert lblIdVertreterSK != null : "fx:id=\"lblIdVertreterSK\" was not injected: check your FXML file 'HauptStage.fxml'.";
        assert lblIdVertreterEinladung != null : "fx:id=\"lblIdVertreterEinladung\" was not injected: check your FXML file 'HauptStage.fxml'.";
        assert lblIdVertreterVollmacht != null : "fx:id=\"lblIdVertreterVollmacht\" was not injected: check your FXML file 'HauptStage.fxml'.";
        assert btnWeiterIdVertreter != null : "fx:id=\"btnWeiterIdVertreter\" was not injected: check your FXML file 'HauptStage.fxml'.";
        assert btnZurueckIdVertreter != null : "fx:id=\"btnZurueckIdVertreter\" was not injected: check your FXML file 'HauptStage.fxml'.";
        assert tIdGast != null : "fx:id=\"tIdGast\" was not injected: check your FXML file 'HauptStage.fxml'.";
        assert lblIdGastGastkarte != null : "fx:id=\"lblIdGastGastkarte\" was not injected: check your FXML file 'HauptStage.fxml'.";
        assert btnWeiterIdGast != null : "fx:id=\"btnWeiterIdGast\" was not injected: check your FXML file 'HauptStage.fxml'.";
        assert btnZurueckIdGast != null : "fx:id=\"btnZurueckIdGast\" was not injected: check your FXML file 'HauptStage.fxml'.";
        assert tIdUnbekannt != null : "fx:id=\"tIdUnbekannt\" was not injected: check your FXML file 'HauptStage.fxml'.";
        assert lblIdUnbekanntEK != null : "fx:id=\"lblIdUnbekanntEK\" was not injected: check your FXML file 'HauptStage.fxml'.";
        assert lblIdUnbekanntSK != null : "fx:id=\"lblIdUnbekanntSK\" was not injected: check your FXML file 'HauptStage.fxml'.";
        assert lblIdUnbekanntEinladung != null : "fx:id=\"lblIdUnbekanntEinladung\" was not injected: check your FXML file 'HauptStage.fxml'.";
        assert lblIdUnbekanntVollmacht != null : "fx:id=\"lblIdUnbekanntVollmacht\" was not injected: check your FXML file 'HauptStage.fxml'.";
        assert lblIdUnbekanntGastkarte != null : "fx:id=\"lblIdUnbekanntGastkarte\" was not injected: check your FXML file 'HauptStage.fxml'.";
        assert btnWeiterIdUnbekannt != null : "fx:id=\"btnWeiterIdUnbekannt\" was not injected: check your FXML file 'HauptStage.fxml'.";
        assert btnZurueckIdUnbekannt != null : "fx:id=\"btnZurueckIdUnbekannt\" was not injected: check your FXML file 'HauptStage.fxml'.";

        tPersonenart.setDisable(false);
        tIdAktionaer.setDisable(true);
        tIdVertreter.setDisable(true);
        tIdGast.setDisable(true);
        tIdUnbekannt.setDisable(true);
        
        ObjectActions.createInfoButton(rootPane, new String[] { "M - Modulauswahl" }, 15.0, 15.0);

        
        eigeneStage.addEventHandler(KeyEvent.KEY_PRESSED, e -> {
            if (e.getCode() == KeyCode.M) {
                CaOpenWindow.openModulauswahl(eigeneStage);
            }
        });
    }

    /**
     * ***************Oberflächensteuerung*************************************.
     *
     * Reiter "Ermittlung Personenart"
     * 
     * @param event the event
     */
    @FXML
    void onBtnWeiter(ActionEvent event) {

        if (pruefenUndBelegenPersonenart() == false) {
            return;
        }

        tPersonenart.setDisable(true);
        tIdAktionaer.setDisable(true);
        tIdVertreter.setDisable(true);
        tIdGast.setDisable(true);
        tIdUnbekannt.setDisable(true);

        tpAblauf.getSelectionModel().select(0);

        switch (ausgewaehltePersonenart) {
        case KonstServicedesk.PERSONENART_AKTIONAER:
            tIdAktionaer.setDisable(false);
            tpAblauf.getSelectionModel().select(1);
            break;
        case KonstServicedesk.PERSONENART_VERTRETER:
            tIdVertreter.setDisable(false);
            tpAblauf.getSelectionModel().select(2);
            break;
        case KonstServicedesk.PERSONENART_UNBEKANNT:
            tIdUnbekannt.setDisable(false);
            tpAblauf.getSelectionModel().select(4);
            break;
        case KonstServicedesk.PERSONENART_GAST_OHNE_ANMELDUNG:
            neueGastkarteAusfuehren();
            break;
        case KonstServicedesk.PERSONENART_GAST_MIT_ANMELDUNG:
            tIdGast.setDisable(false);
            tpAblauf.getSelectionModel().select(3);
            break;
        case KonstServicedesk.PERSONENART_BEGLEITPERSON:
            neueGastkarteAusfuehren();
            break;
        }

    }

    /**
     * Direkt zur Suche-Button.
     *
     * @param event the event
     */
    @FXML
    void onBtnDirektSuche(ActionEvent event) {
        if (pruefenUndBelegenPersonenart() == false) {
            return;
        }
        if (ausgewaehltePersonenart == KonstServicedesk.PERSONENART_GAST_OHNE_ANMELDUNG
                || ausgewaehltePersonenart == KonstServicedesk.PERSONENART_BEGLEITPERSON) {
            fehlerMeldung("Suchfunktion für diese Personenart nicht zulässig");
            return;
        }
        suchenAufrufen();

        showStartTab();

    }

    /**
     * Direkt zur Ausstellung Gastkarte.
     *
     * @param event the event
     */
    @FXML
    void onBtnDirektGastkarte(ActionEvent event) {
        neueGastkarteAusfuehren();
    }

    /**
     * +++++++++++++++++++++Reiter "Identifikation Aktionär"++++++++++++.
     * 
     * Button Weiter
     * 
     * @param event the event
     */
    @FXML
    void onBtnWeiterIdAktionaer(ActionEvent event) {
        suchenAufrufen();
        showStartTab();
    }

    /**
     * On btn zurueck id aktionaer.
     *
     * @param event the event
     */
    @FXML
    void onBtnZurueckIdAktionaer(ActionEvent event) {
        showStartTab();
    }

    /**
     * +++++++++++++++++++++Reiter "Identifikation Vertreter"++++++++++++.
     *
     * Button Weiter
     * 
     * @param event the event
     */
    @FXML
    void onBtnWeiterIdVertreter(ActionEvent event) {
        suchenAufrufen();
        showStartTab();
    }

    /**
     * On btn zurueck id vertreter.
     *
     * @param event the event
     */
    @FXML
    void onBtnZurueckIdVertreter(ActionEvent event) {
        showStartTab();
    }

    /**
     * +++++++++++++++++++++Reiter "Identifikation Gast"++++++++++++.
     * 
     * Button Weiter
     *
     * @param event the event
     */
    @FXML
    void onBtnWeiterIdGast(ActionEvent event) {
        suchenAufrufen();
        showStartTab();
    }

    /**
     * On btn zurueck id gast.
     *
     * @param event the event
     */
    @FXML
    void onBtnZurueckIdGast(ActionEvent event) {
        showStartTab();
    }

    /**
     * +++++++++++++++++++++Reiter "Identifikation Unbekannte
     * Personenart"++++++++++++.
     *
     * Button Weiter
     * 
     * @param event the event
     */
    @FXML
    void onBtnWeiterIdUnbekannt(ActionEvent event) {
        suchenAufrufen();
        showStartTab();
    }

    /**
     * On btn zurueck id unbekannt.
     *
     * @param event the event
     */
    @FXML
    void onBtnZurueckIdUnbekannt(ActionEvent event) {
        showStartTab();
    }

    /**
     * +++++++++++++++++++++++++++++++++++Weitere
     * Bearbeitungsmöglichkeiten++++++++++++++++++.
     *
     * @param event the event
     */
    @FXML
    void onBtnSuchenTeilnehmer(ActionEvent event) {
        ausgewaehltePersonenart = KonstServicedesk.PERSONENART_ALLE;
        suchenAufrufen();
        showStartTab();
    }

    /**
     * On btn neue gastkarte.
     *
     * @param event the event
     */
    @FXML
    void onBtnNeueGastkarte(ActionEvent event) {
        neueGastkarteAusfuehren();
    }

    /**
     * On btn neutrale dokumente.
     *
     * @param event the event
     */
    @FXML
    void onBtnNeutraleDokumente(ActionEvent event) {
        /*AAAAA ServiceDesk*/
    }

    /**
     * +++++++Subfunktionen zum Dialogablauf++++++++++++++++++.
     */
    /** Anzeige des Start-Tabs / Ausgangsbasis des Serivce-Desk-Tools */
    private void showStartTab() {
        tPersonenart.setDisable(false);
        tpAblauf.getSelectionModel().select(0);

        rbAktionaer.setSelected(false);
        rbVertreter.setSelected(false);
        rbUnbekannt.setSelected(false);
        rbBegleitperson.setSelected(false);
        rbGastkarte.setSelected(false);
        rbGastkarteVergessen.setSelected(false);

        tIdAktionaer.setDisable(true);
        tIdVertreter.setDisable(true);
        tIdGast.setDisable(true);
        tIdUnbekannt.setDisable(true);
    }

    /**
     * Pruefen und belegen personenart.
     *
     * @return true, if successful
     */
    private boolean pruefenUndBelegenPersonenart() {
        if (rbAktionaer.isSelected() == false &&
                rbVertreter.isSelected() == false &&
                rbUnbekannt.isSelected() == false &&
                rbBegleitperson.isSelected() == false &&
                rbGastkarte.isSelected() == false &&
                rbGastkarteVergessen.isSelected() == false) {
            fehlerMeldung("Bitte eine Personenart auswählen");
            return false;
        }

        if (rbAktionaer.isSelected()) {
            ausgewaehltePersonenart = KonstServicedesk.PERSONENART_AKTIONAER;
        }
        if (rbVertreter.isSelected()) {
            ausgewaehltePersonenart = KonstServicedesk.PERSONENART_VERTRETER;
        }
        if (rbUnbekannt.isSelected()) {
            ausgewaehltePersonenart = KonstServicedesk.PERSONENART_UNBEKANNT;
        }
        if (rbBegleitperson.isSelected()) {
            ausgewaehltePersonenart = KonstServicedesk.PERSONENART_BEGLEITPERSON;
        }
        if (rbGastkarte.isSelected()) {
            ausgewaehltePersonenart = KonstServicedesk.PERSONENART_GAST_OHNE_ANMELDUNG;
        }
        if (rbGastkarteVergessen.isSelected()) {
            ausgewaehltePersonenart = KonstServicedesk.PERSONENART_GAST_MIT_ANMELDUNG;
        }

        return true;
    }

    /**
     * Suchen aufrufen.
     */
    private void suchenAufrufen() {
        Stage newStage = new Stage();
        CaIcon.serviceDesk(newStage);
        CtrlSuchen controllerFenster = new CtrlSuchen();
        controllerFenster.init(newStage);

        controllerFenster.funktionsButton1 = "Verarbeiten";

        controllerFenster.funktionsButton3 = "Neue Gastkarte";
        controllerFenster.funktionsButton3OhneAuswahlZulaessig = true;

        controllerFenster.mehrfachAuswahlZulaessig = false;

        //        controllerFenster.funktionsButton3 = "Neue Gastkarte";

        controllerFenster.durchsuchenSammelkarten = true;
        controllerFenster.durchsuchenInSammelkarten = true;
        controllerFenster.durchsuchenGaeste = true;
        controllerFenster.durchsuchenNurGaeste = false;

        if (ausgewaehltePersonenart != KonstServicedesk.PERSONENART_ALLE) {
            controllerFenster.suchenNachInterneIdent = false;
        }
        if (ausgewaehltePersonenart == KonstServicedesk.PERSONENART_GAST_MIT_ANMELDUNG) {
            controllerFenster.suchenNurNachGastMeldungen = true;
            controllerFenster.durchsuchenNurGaeste = true;
        }

        CaController caController = new CaController();
        caController.open(newStage, controllerFenster, "/de/meetingapps/meetingclient/meetingClientDialoge/Suchen.fxml",
                1500, 760, "Suchen", true);

        if (controllerFenster.rcFortsetzen) {
            int rcFunktion = controllerFenster.rcFunktion;

            if (rcFunktion == 1) {
                detailAusfuehren(controllerFenster.rcAktienregister, controllerFenster.rcMeldungen,
                        controllerFenster.rcSonstigeVollmacht);
                return;
            }
            if (rcFunktion == 3) {
                neueGastkarteAusfuehren();
                return;
            }

            return;

        }

    }

    /**
     * Detail ausfuehren.
     *
     * @param rcAktienregister    the rc aktienregister
     * @param rcMeldungen         the rc meldungen
     * @param rcSonstigeVollmacht the rc sonstige vollmacht
     */
    private void detailAusfuehren(EclAktienregister[] rcAktienregister, EclMeldung[] rcMeldungen,
            String[] rcSonstigeVollmacht) {
        Stage newStageDetail = new Stage();
        CaIcon.serviceDesk(newStageDetail);

        CtrlDetailStage controllerFensterDetail = new CtrlDetailStage();
        controllerFensterDetail.init(newStageDetail);

        controllerFensterDetail.gewaehltAktienregister = rcAktienregister;
        controllerFensterDetail.gewaehltMeldungen = rcMeldungen;
        controllerFensterDetail.gewaehltSonstigeVollmacht = rcSonstigeVollmacht;

        controllerFensterDetail.ausgewaehltePersonenart = ausgewaehltePersonenart;

        CaController caControllerDetail = new CaController();
        caControllerDetail.open(newStageDetail, controllerFensterDetail,
                "/de/meetingapps/meetingclient/meetingServiceDesk/DetailStage.fxml", 1500, 760, "Verarbeiten", true);

        /*XXX*/

        //        int anz = controllerFenster.rcAktienregister.length;
        //        if (anz == 1) {
        //            tfAktionaersnummer.setText(controllerFenster.rcAktienregister[0].aktionaersnummer);
        //            doEinlesen();
        //            return;
        //        }

    }

    /**
     * Neue gastkarte ausfuehren.
     */
    private void neueGastkarteAusfuehren() {
        /*XXX*/
    }

    //    @FXML
    //    private ScrollPane scpnErgebnis;
    //
    //    @FXML
    //    private TextArea tfEingabe;
    //
    //    @FXML
    //    private Label lbFehler;
    //
    //    @FXML
    //    private Button btnEinladungScanCode;
    //
    //    @FXML
    //    private Button btnEinladungInternetCode;
    //
    //    @FXML
    //    private Button btnAktionaersnummer;
    //
    //    @FXML
    //    private Button btnEintrittskartenNummer;
    //
    //    @FXML
    //    private Button btnStimmkartenNummer;
    //
    //    @FXML
    //    private Button btnNameMeldebestand;
    //
    //    @FXML
    //    private Button btnNameAktienregister;
    //
    //    @FXML
    //    private Button btnAppCode;
    //
    //    @FXML
    //    private Button btnNeueGastkarte;
    //
    //    @FXML
    //    private Button btnKIAV;
    //
    //    @FXML
    //    private Button btnAkkreditierung;
    //
    //    @FXML
    //    private Button btnVereinspraesenz;
    //
    //    @FXML
    //    private Button btnTagungsausweis;
    //
    //    @FXML
    //    private Button btnClear;
    //
    //    @FXML
    //    private Label lblSuchbegriff;
    //
    //    @FXML
    //    private Label lblSuchenNach;

    /** Weitere Bildschirmelemente. */

    GridPane grpnErgebnis = null;

    /** The btn auswaehlen. */
    private Button[] btnAuswaehlen = null;

    /** *********Parameter Sektion - Temporär***********. */
    private boolean appAktiv = false;

    /** The namensaktien aktiv. */
    private boolean namensaktienAktiv = false;

    /** The banken aktiv. */
    private boolean bankenAktiv = false;

    /** The praesenz modul aktiv. */
    private boolean praesenzModulAktiv = false;

    /** The einladungs internetcode. */
    private boolean einladungsInternetcode = false;

    /** The es gibt stimmkartennummer. */
    private boolean esGibtStimmkartennummer = true;

    /** The gaeste aktiv. */
    private boolean gaesteAktiv = false;

    /** **********Globale Werte************. */

    private WSClient wsClient = null;

    /** The l db bundle. */
    private DbBundle lDbBundle = null;

    /**
     * 1=Eintrittskartennummer/Gastkartennummer 2=Stimmkartennummer 3=Name im
     * Meldebestand 4=Name im Aktienregister 5=Aktionärsnummer 8=Teilnehmerausweis,
     * d.h. Folge von Idents
     */
    private int suchfunktion = 0;

    /** The fehlertext. */
    private String fehlertext = "";

    /** *Für nummerVerarbeiten()**. */
    private String scanNummer = "";

    /** The kartenklasse. */
    int kartenklasse = 0;

    /** The suchergebnis fuer anzeige. */
    EclSuchergebnis[] suchergebnisFuerAnzeige = null;


    /** The eigene stage. */
    private Stage eigeneStage;

    /**
     * Inits the.
     *
     * @param pEigeneStage the eigene stage
     */
    public void init(Stage pEigeneStage) {
        eigeneStage = pEigeneStage;

    }

}
