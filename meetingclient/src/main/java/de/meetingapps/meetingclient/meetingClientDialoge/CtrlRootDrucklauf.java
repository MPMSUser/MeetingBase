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
package de.meetingapps.meetingclient.meetingClientDialoge;

import java.net.URL;
import java.util.ResourceBundle;

import de.meetingapps.meetingportal.meetComAllg.CaString;
import de.meetingapps.meetingportal.meetComDb.DbBundle;
import de.meetingapps.meetingportal.meetComEntities.EclDrucklauf;
import de.meetingapps.meetingportal.meetComKonst.KonstVerarbeitungslaufArt;
import de.meetingapps.meetingportal.meetComStub.WSClient;
import de.meetingapps.meetingportal.meetComWE.WEDrucklaeufeGet;
import de.meetingapps.meetingportal.meetComWE.WEDrucklaeufeGetRC;
import de.meetingapps.meetingportal.meetComWE.WELoginVerify;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

/**
 * Basisklasse für Dialoge mit Drucklauf-Verwaltung. Zugehöriges Muster-fxml
 * siehe RootDrucklauf.fxml.
 *
 * Die verwendeten fxmls können weitere Felder enthalten, müssen jedoch
 * mindestens die in RootDrucklauf.fxml definierten (notfalls im Quellcode
 * ausblenden).
 */
public class CtrlRootDrucklauf extends CtrlRoot {

    /** The resources. */
    @FXML
    private ResourceBundle resources;

    /** The location. */
    @FXML
    private URL location;

    /** The tg nicht gedruckt. */
    @FXML
    private RadioButton tgNichtGedruckt;

    /** The tgg gedruckt. */
    @FXML
    private ToggleGroup tggGedruckt;

    /** The tg wiederholung. */
    @FXML
    private RadioButton tgWiederholung;

    /** The tf verarbeitungslauf gewaehlt. */
    @FXML
    private TextField tfVerarbeitungslaufGewaehlt;

    /** The btn verarbeitungslauf. */
    @FXML
    private Button btnVerarbeitungslauf;

    /** The btn ausfuehren. */
    @FXML
    private Button btnAusfuehren;

    /** The btn abbrechen. */
    @FXML
    private Button btnAbbrechen;

    /** The scrpn ergebnis. */
    @FXML
    private ScrollPane scrpnErgebnis;

    /** The cb ausgabe report. */
    @FXML
    private CheckBox cbAusgabeReport;

    /** The cb export. */
    @FXML
    private CheckBox cbExport;

    /** The tf druck variante. */
    @FXML
    private TextField tfDruckVariante;

    /** The tg alle ohne neuen drucklauf. */
    @FXML
    private RadioButton tgAlleOhneNeuenDrucklauf;

    /** The rb drucker. */
    @FXML
    private RadioButton rbDrucker;

    /** The tg ausgabe report. */
    @FXML
    private ToggleGroup tgAusgabeReport;

    /** The rb PDF kundenordner. */
    @FXML
    private RadioButton rbPDFKundenordner;

    /** The rb PD fbetter root. */
    @FXML
    private RadioButton rbPDFmeetingRoot;

    /** The rb export better root. */
    @FXML
    private RadioButton rbExportMeetingRoot;

    /** The tg CSV. */
    @FXML
    private ToggleGroup tgCSV;

    /** The rb export kundenordner. */
    @FXML
    private RadioButton rbExportKundenordner;

    /** **************Ab hier individuell***************************. */

    protected DbBundle lDbBundle = null;

    /** +++++++"Input"-Werte++++++++++++. */
    /** Reports (Drucker / PDF) können erzeugt werden */
    protected boolean auswahlReportausgabe = true;

    /**
     * Es können ggf. Varianten bei der Report-Ausgabe ausgewählt werden - Drucker,
     * PDF Kundenordner, PDF betterRoot
     */
    protected boolean auswahlReportausgabeVarianten = true;

    /** CSV-Ausgabe kann erzeigt werden. */
    protected boolean auswahlCSV = true;

    /**
     * Es könnne ggf. Varianten beim CSV-Export ausgewählt werden - Kundenordner,
     * betterRoot
     */
    protected boolean auswahlCSVVarianten = true;

    /**
     * Bei Drucklaufauswahl ist die Variante "alle, ohne neuen Drucklauf zu
     * erzeugen" zulässig.
     */
    protected boolean auswahlDrucklaufAlleZulaessig = true;

    /** The verarbeitungslauf art. */
    protected int verarbeitungslaufArt = 1;

    /** The verarbeitungslauf sub art. */
    protected int verarbeitungslaufSubArt = 0;

    /** +++++"Ergebnis"-Werte++++++++++++++++. */
    protected boolean rcAusgabeReportGewaehlt = false;

    /** 1=Drucker, 2=PDF (Kundenordner), 3=PDF (in betterAusdruckeIntern). */
    protected int rcAusgabeReportVarianteGewaehlt = -1;

    /** The rc ausgabe CSV gewaehlt. */
    protected boolean rcAusgabeCSVGewaehlt = false;

    /** 1=Export in Kundenordner, 2=Export in betterOutput. */
    protected int rcAusgabeCSVVarianteGewaehlt = -1;

    /** The rc druckvariante. */
    protected String rcDruckvariante = "";

    /**
     * -1 => alle Drucken, ohne Drucklauferzeugung 0 => neuen Drucklauf >0 =>
     * bestehenden Drucklauf drucken.
     */
    protected int rcDrucklaufNr = 0;

    /** ++++nur lokale Arbeitsvariablen++++. */
    private EclDrucklauf[] drucklaufArray = null;

    /**
     * Initialize root drucklauf fxml.
     */
    protected void initializeRootDrucklaufFxml() {
        // @formatter:off
        assert tgNichtGedruckt != null : "fx:id=\"tgNichtGedruckt\" was not injected: check your FXML file 'RootDrucklauf.fxml'.";
        assert tggGedruckt != null : "fx:id=\"tggGedruckt\" was not injected: check your FXML file 'RootDrucklauf.fxml'.";
        assert tgWiederholung != null : "fx:id=\"tgWiederholung\" was not injected: check your FXML file 'RootDrucklauf.fxml'.";
        assert tfVerarbeitungslaufGewaehlt != null : "fx:id=\"tfVerarbeitungslaufGewaehlt\" was not injected: check your FXML file 'RootDrucklauf.fxml'.";
        assert btnVerarbeitungslauf != null : "fx:id=\"btnVerarbeitungslauf\" was not injected: check your FXML file 'RootDrucklauf.fxml'.";
        assert btnAusfuehren != null : "fx:id=\"btnAusfuehren\" was not injected: check your FXML file 'RootDrucklauf.fxml'.";
        assert btnAbbrechen != null : "fx:id=\"btnAbbrechen\" was not injected: check your FXML file 'RootDrucklauf.fxml'.";
        assert scrpnErgebnis != null : "fx:id=\"scrpnErgebnis\" was not injected: check your FXML file 'RootDrucklauf.fxml'.";
        assert cbAusgabeReport != null : "fx:id=\"cbAusgabeReport\" was not injected: check your FXML file 'RootDrucklauf.fxml'.";
        assert cbExport != null : "fx:id=\"cbExport\" was not injected: check your FXML file 'RootDrucklauf.fxml'.";
        assert tfDruckVariante != null : "fx:id=\"tfDruckVariante\" was not injected: check your FXML file 'RootDrucklauf.fxml'.";
        assert tgAlleOhneNeuenDrucklauf != null : "fx:id=\"tfAlleOhneNeuenDrucklauf\" was not injected: check your FXML file 'RootDrucklauf.fxml'.";
        assert rbDrucker != null : "fx:id=\"rbDrucker\" was not injected: check your FXML file 'RootDrucklauf.fxml'.";
        assert tgAusgabeReport != null : "fx:id=\"tgAusgabeReport\" was not injected: check your FXML file 'RootDrucklauf.fxml'.";
        assert rbPDFKundenordner != null : "fx:id=\"rbPDFKundenordner\" was not injected: check your FXML file 'RootDrucklauf.fxml'.";
        assert rbPDFmeetingRoot != null : "fx:id=\"rbPDFbetterRoot\" was not injected: check your FXML file 'RootDrucklauf.fxml'.";
        assert rbExportMeetingRoot != null : "fx:id=\"rbExportBetterRoot\" was not injected: check your FXML file 'RootDrucklauf.fxml'.";
        assert tgCSV != null : "fx:id=\"tgCSV\" was not injected: check your FXML file 'RootDrucklauf.fxml'.";
        assert rbExportKundenordner != null : "fx:id=\"rbExportKundenordner\" was not injected: check your FXML file 'RootDrucklauf.fxml'.";
        // @formatter:on

        /*********** Ab hier individuell ****************************/

        WSClient wsClient = new WSClient();
        WEDrucklaeufeGet weDrucklaeufeGet = new WEDrucklaeufeGet();
        weDrucklaeufeGet.drucklaufArt = verarbeitungslaufArt;
        weDrucklaeufeGet.drucklaufSubArt = verarbeitungslaufSubArt;
        WELoginVerify weLoginVerify = new WELoginVerify(); /*Mandatennummer wird in WSClient immer gesetzt*/
        weDrucklaeufeGet.setWeLoginVerify(weLoginVerify);

        WEDrucklaeufeGetRC weDrucklaeufeGetRC = wsClient.drucklaeufeGet(weDrucklaeufeGet);

        drucklaufArray = weDrucklaeufeGetRC.drucklaufArray;

        tfDruckVariante.setText("01");

        if (auswahlReportausgabe) {
        } else {
            cbAusgabeReport.setVisible(false);
            auswahlReportausgabeVarianten = false;
            cbExport.setSelected(true);
        }
        if (auswahlReportausgabeVarianten == false) {
            rbDrucker.setSelected(true);
            rbPDFKundenordner.setVisible(false);
            rbPDFmeetingRoot.setVisible(false);
        }

        if (auswahlCSV) {
        } else {
            cbExport.setVisible(false);
            auswahlCSVVarianten = false;
            cbAusgabeReport.setSelected(true);
        }
        if (auswahlCSVVarianten == false) {
            rbExportMeetingRoot.setSelected(true);
            rbExportMeetingRoot.setVisible(false);
            rbExportKundenordner.setVisible(false);
        }

        if (auswahlDrucklaufAlleZulaessig == false) {
            tgAlleOhneNeuenDrucklauf.setVisible(false);
        }

        lDbBundle = new DbBundle();

    }

    /**
     * On btn verarbeitungslauf.
     *
     * @param event the event
     */
    @FXML
    public void onBtnVerarbeitungslauf(ActionEvent event) {
        Stage neuerDialog = new Stage();
        CaIcon.bestandsverwaltung(neuerDialog);

        CtrDrucklaufAuswaehlen controllerFenster = new CtrDrucklaufAuswaehlen();

        controllerFenster.init(neuerDialog);
        controllerFenster.drucklaufListe = drucklaufArray;
        controllerFenster.verarbeitungslaufArt = verarbeitungslaufArt;

        CaController caController = new CaController();
        caController.open(neuerDialog, controllerFenster,
                "/de/meetingapps/meetingclient/meetingClientDialoge/DrucklaufAuswaehlen.fxml", 1200, 850,
                KonstVerarbeitungslaufArt.getText(verarbeitungslaufArt) + "- Verarbeitungslauf auswählen", true);

        if (controllerFenster.verarbeitungslaufNr.isEmpty()) {
            return;
        } else {
            tfVerarbeitungslaufGewaehlt.setText(controllerFenster.verarbeitungslaufNr);
            tgWiederholung.setSelected(true);
            return;
        }

    }

    /**
     * On btn abbrechen.
     *
     * @param event the event
     */
    @FXML
    public void onBtnAbbrechen(ActionEvent event) {
        eigeneStage.hide();
    }

    /**
     * On btn ausfuehren.
     *
     * @param event the event
     */
    @FXML
    public void onBtnAusfuehren(ActionEvent event) {

    }

    /**
     * Do ausfuehren eingabe verarbeiten.
     *
     * @return true, if successful
     */
    public boolean doAusfuehrenEingabeVerarbeiten() {
        lFehlertext = "";

        rcAusgabeReportGewaehlt = cbAusgabeReport.isSelected();
        rcAusgabeCSVGewaehlt = cbExport.isSelected();

        if (!rcAusgabeReportGewaehlt && !rcAusgabeCSVGewaehlt) {
            if (auswahlReportausgabe) {
                lFehlertext = "Ausgabe Report muß ausgewählt sein";
            }
            if (auswahlCSV) {
                lFehlertext = "Erzeugen CSV-Export muß ausgewählt sein";
            }
            if (auswahlReportausgabe && auswahlCSV) {
                lFehlertext = "Ausgabe Report oder Erzeugen CSV-Export muß ausgewählt sein";
            }

            fehlerMeldung(lFehlertext);
            return false;
        }

        rcDruckvariante = "";
        if (rcAusgabeReportGewaehlt) {
            pruefeZahlNichtLeerLaenge(tfDruckVariante, "Druck-Variante", 2);
            if (!lFehlertext.isEmpty()) {
                fehlerMeldung(lFehlertext);
                return false;
            }
            rcDruckvariante = CaString.fuelleLinksNull(Integer.toString(Integer.parseInt(tfDruckVariante.getText())),
                    2);
        }

        if (tgNichtGedruckt.isSelected()) {
            rcDrucklaufNr = 0;
        }
        if (tgWiederholung.isSelected()) {
            pruefeZahlNichtLeerLaenge(tfVerarbeitungslaufGewaehlt, "Verarbeitungslauf-Nr", 5);
            if (!lFehlertext.isEmpty()) {
                fehlerMeldung(lFehlertext);
                return false;
            }

            rcDrucklaufNr = Integer.parseInt(tfVerarbeitungslaufGewaehlt.getText());

            int gef = -1;
            for (int i = 0; i < drucklaufArray.length; i++) {
                if (rcDrucklaufNr == drucklaufArray[i].drucklaufNr) {
                    gef = i;
                }
            }
            if (gef == -1) {
                fehlerhaftesFeld = null;
                fehlerMeldung("Verarbeitungslauf-Nr. nicht vorhanden!");
                return false;
            }
        }
        if (tgAlleOhneNeuenDrucklauf.isSelected()) {
            rcDrucklaufNr = -1;
        }

        /** 1=Drucker, 2=PDF (Kundenordner), 3=PDF (in betterAusdruckeIntern) */
        if (rbDrucker.isSelected()) {
            rcAusgabeReportVarianteGewaehlt = 1;
        }
        if (rbPDFKundenordner.isSelected()) {
            rcAusgabeReportVarianteGewaehlt = 2;
        }
        if (rbPDFmeetingRoot.isSelected()) {
            rcAusgabeReportVarianteGewaehlt = 3;
        }

        /** 1=Export in Kundenordner, 2=Export in betterOutput */
        if (rbExportKundenordner.isSelected()) {
            rcAusgabeCSVVarianteGewaehlt = 1;
        }
        if (rbExportMeetingRoot.isSelected()) {
            rcAusgabeCSVVarianteGewaehlt = 2;
        }

        return true;
    }

    /**
     * Do ausfuehren ergebnis anzeigen.
     *
     * @param grpnErgebnis the grpn ergebnis
     */
    public void doAusfuehrenErgebnisAnzeigen(GridPane grpnErgebnis) {
        scrpnErgebnis.setContent(grpnErgebnis);
    }

    /**
     * Do ausfuehren beendet.
     */
    public void doAusfuehrenBeendet() {
        btnVerarbeitungslauf.setDisable(true);
        btnAusfuehren.setDisable(true);
    }

}
