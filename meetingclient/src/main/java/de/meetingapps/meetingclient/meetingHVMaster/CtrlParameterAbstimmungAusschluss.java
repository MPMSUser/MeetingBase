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
package de.meetingapps.meetingclient.meetingHVMaster;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import org.kordamp.ikonli.fontawesome5.FontAwesomeSolid;
import org.kordamp.ikonli.javafx.FontIcon;

import de.meetingapps.meetingclient.meetingClientDialoge.CaController;
import de.meetingapps.meetingclient.meetingClientDialoge.CaIcon;
import de.meetingapps.meetingclient.meetingClientDialoge.CtrlRoot;
import de.meetingapps.meetingclient.meetingClientDialoge.CtrlSuchen;
import de.meetingapps.meetingclient.meetingClientOberflaechen.MaxLengthNumericTextField;
import de.meetingapps.meetingclient.meetingClientOberflaechen.TableCellLongMitPunkt;
import de.meetingapps.meetingportal.meetComAllg.CaString;
import de.meetingapps.meetingportal.meetComBl.BlNummernformen;
import de.meetingapps.meetingportal.meetComBl.BlSuchen;
import de.meetingapps.meetingportal.meetComDb.DbBundle;
import de.meetingapps.meetingportal.meetComEntities.EclAbstimmung;
import de.meetingapps.meetingportal.meetComEntities.EclAbstimmungenEinzelAusschluss;
import de.meetingapps.meetingportal.meetComEntities.EclMeldung;
import de.meetingapps.meetingportal.meetComHVParam.ParamS;
import de.meetingapps.meetingportal.meetComKonst.KonstKartenklasse;
import de.meetingapps.meetingportal.meetComKonst.KonstSuchlaufSuchbegriffArt;
import javafx.application.Platform;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

/**
 * The Class CtrlParameterAbstimmungAusschluss.
 */
public class CtrlParameterAbstimmungAusschluss extends CtrlRoot {

    /** The resources. */
    @FXML
    private ResourceBundle resources;

    /** The location. */
    @FXML
    private URL location;

    /** The scr pane. */
    @FXML
    private ScrollPane scrPane;

    //** The table Einzel. */
    @FXML
    private TableView<EclMeldung> tableEinzel;

    /** The btn neu. */
    @FXML
    private Button btnNeu;

    /** The btn suche. */
    @FXML
    private Button btnSuche;

    /** The lbl melde ident. */
    @FXML
    private Label lblMeldeIdent;

    /** The tf melde ident. */
    @FXML
    private TextField tfMeldeIdent;

    @FXML
    private Label lblEkNummer;

    @FXML
    private TextField tfEkNummer;

    /** The lbl aktionaersnummer. */
    @FXML
    private Label lblAktionaersnummer;

    /** The tf aktionaersnummer. */
    @FXML
    private TextField tfAktionaersnummer;

    /** The btn speichern. */
    @FXML
    private Button btnSpeichern;

    /** The lbll abstimmung. */
    @FXML
    private Label lbllAbstimmung;

    /** The lbl fehler. */
    @FXML
    private Label lblFehler;

    /** The l db bundle. */
    private DbBundle lDbBundle = null;

    /** The grpn abstimmungen. */
    private GridPane grpnAbstimmungen = null;

    /** The tf stimmen pauschal. */
    private MaxLengthNumericTextField[] tfStimmenPauschal = null;

    /** The cb abziehen bei. */
    private ComboBox<String>[] cbAbziehenBei = null; /* J, N möglich */

    /** The anzeige einzel ausschluss. */
    private List<EclMeldung> einzelausschluss;

    /**
     * Initialize.
     */
    @FXML
    void initialize() {
        // @formatter:off
        assert scrPane != null : "fx:id=\"scrPane\" was not injected: check your FXML file 'ParameterAbstimmungDetail.fxml'.";
        assert btnNeu != null : "fx:id=\"btnNeu\" was not injected: check your FXML file 'ParameterAbstimmungDetail.fxml'.";
        assert btnSuche != null : "fx:id=\"btnAbbruch\" was not injected: check your FXML file 'ParameterAbstimmungDetail.fxml'.";
        assert lblMeldeIdent != null : "fx:id=\"lblMeldeIdent\" was not injected: check your FXML file 'ParameterAbstimmungDetail.fxml'.";
        assert tfMeldeIdent != null : "fx:id=\"tfMeldeIdent\" was not injected: check your FXML file 'ParameterAbstimmungDetail.fxml'.";
        assert btnSpeichern != null : "fx:id=\"btnSpeichern\" was not injected: check your FXML file 'ParameterAbstimmungDetail.fxml'.";
        assert lbllAbstimmung != null : "fx:id=\"lbllAbstimmung\" was not injected: check your FXML file 'ParameterAbstimmungDetail.fxml'.";
        assert lblFehler != null : "fx:id=\"lblFehler\" was not injected: check your FXML file 'ParameterAbstimmungDetail.fxml'.";
        // @formatter:on

        lDbBundle = new DbBundle();

        switch (funktion) {
        case 1:
            zeigePauschalausschluss();
            break;
        case 2:
            zeigeEinzelausschluss();
            ladeEinzelausschluss();
            Platform.runLater(() -> tfMeldeIdent.requestFocus());
            break;
        }
    }

    /**
     * Speichere pauschalausschluss.
     *
     * @return true, if successful
     */
    private boolean speicherePauschalausschluss() {
        for (int i = 0; i < 5; i++) {
            if (tfStimmenPauschal[i].getText() == null || tfStimmenPauschal[i].getText().isEmpty()) {
                return false;
            }
            String hText = tfStimmenPauschal[i].getText();
            if (!CaString.isNummern(hText)) {
                return false;
            }
            aktuelleAbstimmung.pauschalAusschluss[i] = Long.parseLong(hText);

            hText = cbAbziehenBei[i].getValue();
            if (hText.compareTo("J") == 0) {
                aktuelleAbstimmung.pauschalAusschlussJN[i] = 1;
            } else {
                aktuelleAbstimmung.pauschalAusschlussJN[i] = 2;
            }
        }

        return true;
    }

    /**
     * Speichere einzelausschluss.
     *
     * @return true, if successful
     */
    private boolean speichereEinzelausschluss() {
        return true;
    }

    /** The eigene stage. */
    private Stage eigeneStage;

    /** The funktion. */
    /* 1 Pauschalauschluus, 2 Einzelausschluss */
    private int funktion = 0;

    /** The aktuelle abstimmung. */
    private EclAbstimmung aktuelleAbstimmung;

    /**
     * Inits the.
     *
     * @param pEigeneStage the eigene stage
     * @param pFunktion    the funktion
     * @param pAbstimmung  the abstimmung
     */
    public void init(Stage pEigeneStage, int pFunktion, EclAbstimmung pAbstimmung) {
        eigeneStage = pEigeneStage;
        funktion = pFunktion;
        aktuelleAbstimmung = pAbstimmung;
    }

    /**
     * Zeige allgemein.
     *
     * @param pauschal the pauschal
     */
    private void zeigeAllgemein(Boolean pauschal) {
        lbllAbstimmung.setText("(Ident " + Integer.toString(aktuelleAbstimmung.ident) + ") " + aktuelleAbstimmung.nummer
                + " " + aktuelleAbstimmung.nummerindex + " " + aktuelleAbstimmung.kurzBezeichnung);
        if (pauschal) {
            btnSuche.setVisible(false);
            btnNeu.setVisible(false);
            lblMeldeIdent.setVisible(false);
            tfMeldeIdent.setVisible(false);
            lblEkNummer.setVisible(false);
            tfEkNummer.setVisible(false);
            lblAktionaersnummer.setVisible(false);
            tfAktionaersnummer.setVisible(false);
        }
    }

    /**
     * Zeige pauschalausschluss.
     */
    @SuppressWarnings("unchecked")
    private void zeigePauschalausschluss() {
        zeigeAllgemein(true);

        grpnAbstimmungen = new GridPane();
        grpnAbstimmungen.setVgap(5);
        grpnAbstimmungen.setHgap(15);

        tfStimmenPauschal = new MaxLengthNumericTextField[5];
        cbAbziehenBei = new ComboBox[5];
        Label uLabel = new Label("Pauschalausschluss ");
        grpnAbstimmungen.add(uLabel, 0, 0);

        for (int i = 0; i < 5; i++) {
            int spalte = 0;

            Label hLabel = new Label("Stimmen Gattung " + ParamS.param.paramBasis.gattungBezeichnungKurz[i]);
            if (ParamS.param.paramBasis.gattungAktiv[i]) {
                grpnAbstimmungen.add(hLabel, spalte, i + 1);
                spalte++;
            }

            tfStimmenPauschal[i] = new MaxLengthNumericTextField(14);
            tfStimmenPauschal[i].setText(Long.toString(aktuelleAbstimmung.pauschalAusschluss[i]));
            if (ParamS.param.paramBasis.gattungAktiv[i]) {
                grpnAbstimmungen.add(tfStimmenPauschal[i], spalte, i + 1);
                spalte++;
            }

            Label hLabel1 = new Label("abziehen bei:");
            if (ParamS.param.paramBasis.gattungAktiv[i]) {
                grpnAbstimmungen.add(hLabel1, spalte, i + 1);
                spalte++;
            }

            cbAbziehenBei[i] = new ComboBox<String>();
            cbAbziehenBei[i].getItems().addAll("J");
            cbAbziehenBei[i].getItems().addAll("N");
            if (aktuelleAbstimmung.pauschalAusschlussJN[i] == 2) {
                cbAbziehenBei[i].setValue("N");
            } else {
                cbAbziehenBei[i].setValue("J");
            }
            if (ParamS.param.paramBasis.gattungAktiv[i]) {
                grpnAbstimmungen.add(cbAbziehenBei[i], spalte, i + 1);
                spalte++;
            }

        }
        scrPane.setContent(grpnAbstimmungen);

    }

    @SuppressWarnings("unchecked")
    private void zeigeEinzelausschluss() {
        zeigeAllgemein(false);
        btnNeu.setText("Ausführen");
        tableEinzel.setVisible(true);

        keyEvent(tfAktionaersnummer);
        keyEvent(tfMeldeIdent);
        keyEvent(tfEkNummer);

        TableColumn<EclMeldung, Integer> meldeIdent = new TableColumn<>("MeldeIdent");
        meldeIdent.setCellValueFactory(new PropertyValueFactory<>("meldungsIdent"));
        meldeIdent.setStyle("-fx-alignment: CENTER");

        TableColumn<EclMeldung, String> anr = new TableColumn<>("Aktionärsnummer");
        anr.setCellValueFactory(new PropertyValueFactory<>("aktionaersnummer"));
        anr.setStyle("-fx-alignment: CENTER");

        TableColumn<EclMeldung, String> zutrittsIdent = new TableColumn<>("EK");
        zutrittsIdent.setCellValueFactory(new PropertyValueFactory<>("zutrittsIdent"));
        zutrittsIdent.setStyle("-fx-alignment: CENTER");

        TableColumn<EclMeldung, String> gattung = new TableColumn<>("Gattung");
        gattung.setCellValueFactory(new PropertyValueFactory<>("zusatz1"));
        gattung.setStyle("-fx-alignment: CENTER");

        TableColumn<EclMeldung, String> stimmen = new TableColumn<>("Aktien");
        stimmen.setCellValueFactory(new PropertyValueFactory<>("stimmen"));
        stimmen.setCellFactory(col -> new TableCellLongMitPunkt<EclMeldung, String>());
        stimmen.setStyle("-fx-alignment: CENTER");

        TableColumn<EclMeldung, String> name = new TableColumn<>("Name");
        name.setCellValueFactory(new PropertyValueFactory<>("name"));
        name.setStyle("-fx-alignment: CENTER");

        TableColumn<EclMeldung, EclMeldung> delete = new TableColumn<>();
        delete.setCellValueFactory(e -> new ReadOnlyObjectWrapper<>(e.getValue()));
        delete.setStyle("-fx-alignment: CENTER");
        delete.setCellFactory(e -> new TableCell<EclMeldung, EclMeldung>() {
            private final Button deleteButton = new Button();

            @Override
            protected void updateItem(EclMeldung meldung, boolean empty) {
                super.updateItem(meldung, empty);

                if (meldung == null) {
                    setGraphic(null);
                    return;
                }
                deleteButton.setGraphic(new FontIcon(FontAwesomeSolid.TRASH));
                setGraphic(deleteButton);
                deleteButton.setOnAction(event -> clickedLoeschen(meldung));
            }
        });

        name.setMinWidth(150);
        name.prefWidthProperty()
                .bind(tableEinzel.widthProperty().subtract(meldeIdent.widthProperty()).subtract(anr.widthProperty())
                        .subtract(zutrittsIdent.widthProperty()).subtract(gattung.widthProperty())
                        .subtract(stimmen.widthProperty()).subtract(delete.widthProperty()).subtract(6));

        tableEinzel.getColumns().setAll(meldeIdent, anr, zutrittsIdent, name, gattung, stimmen, delete);

        scrPane.setVisible(false);

    }

    private void ladeEinzelausschluss() {

        lDbBundle.openAll();
        einzelausschluss = lDbBundle.dbAbstimmungenEinzelAusschluss.readMeldungenZuId(aktuelleAbstimmung.ident);
        lDbBundle.closeAll();

        tableEinzel.getItems().setAll(einzelausschluss);

    }

    /**
     * On btn clicked speichern.
     *
     * @param event the event
     */
    @FXML
    private void onBtnClickedSpeichern(ActionEvent event) {
        boolean rc = true;
        switch (funktion) {
        case 1:
            rc = speicherePauschalausschluss();
            break;
        case 2:
            rc = speichereEinzelausschluss();
            break;
        }

        if (rc == false) {
            return;
        }
        eigeneStage.hide();
    }

    private void clickedLoeschen(EclMeldung meldung) {

        lDbBundle.openAll();
        lDbBundle.dbAbstimmungenEinzelAusschluss.delete(aktuelleAbstimmung.ident, meldung.getMeldungsIdent());
        lDbBundle.closeAll();
        ladeEinzelausschluss();
    }

    /**
     * On btn cllicked neu.
     *
     * @param event the event
     */
    @FXML
    private void onBtnCllickedNeu(ActionEvent event) {

        final BlSuchen blSuchen = new BlSuchen(false, new DbBundle());
        lblFehler.setText("");

        if (!tfMeldeIdent.getText().isBlank()) {

            String hText = tfMeldeIdent.getText();
            if (hText.length() > 8) {
                lblFehler.setText("Ident zu lang!");
                return;
            }
            if (!CaString.isNummern(hText)) {
                lblFehler.setText("Gültige Ident eingeben!");
                return;
            }

            blSuchen.sucheAusfuehren(KonstSuchlaufSuchbegriffArt.meldeIdent, tfMeldeIdent.getText(), false, true, false,
                    false);

        } else if (!tfAktionaersnummer.getText().isBlank()) {

            blSuchen.sucheAusfuehren(KonstSuchlaufSuchbegriffArt.registerNummer, tfAktionaersnummer.getText(), false,
                    true, false, false);

        } else if (!tfEkNummer.getText().isEmpty()) {

            BlNummernformen blNummernformen = new BlNummernformen(lDbBundle);
            tfEkNummer.setText(
                    blNummernformen.formatiereNr(tfEkNummer.getText(), KonstKartenklasse.eintrittskartennummer));

            blSuchen.sucheAusfuehren(KonstSuchlaufSuchbegriffArt.ekNummer, tfEkNummer.getText(), false, true, false,
                    false);

        }

        if (blSuchen.rcMeldungen == null) {
            lblFehler.setText("Nummer nicht vorhanden!");
            return;
        }

        final EclMeldung newMeldung = blSuchen.rcMeldungen[0];
        final Boolean stimmberechtigt = aktuelleAbstimmung.stimmberechtigteGattungen[newMeldung.getGattung() - 1] == 1;

        if (!stimmberechtigt) {
            lblFehler.setText("Gattung nicht stimmberechtigt!");
            return;
        }

        final int newMeldeIdent = newMeldung.meldungsIdent;

        for (EclMeldung data : einzelausschluss) {
            if (data.meldungsIdent == newMeldeIdent) {
                lblFehler.setText("Meldung bereits zugeordnet!");
                return;
            }
        }

        /* Speichern */
        lDbBundle.openAll();
        EclAbstimmungenEinzelAusschluss neueAbstimmung = new EclAbstimmungenEinzelAusschluss(aktuelleAbstimmung.ident,
                newMeldeIdent);
        lDbBundle.dbAbstimmungenEinzelAusschluss.insert(neueAbstimmung);
        lDbBundle.closeAll();
        ladeEinzelausschluss();

        tfMeldeIdent.clear();
        tfEkNummer.clear();
        tfAktionaersnummer.clear();

    }

    /**
     * On btn clicked suche.
     *
     * @param event the event
     */
    @FXML
    void onBtnClickedSuche(ActionEvent event) {

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
                tfMeldeIdent.clear();
                tfAktionaersnummer.setText(controllerFenster.rcAktienregister[0].aktionaersnummer);
                tfAktionaersnummer.requestFocus();
                return;
            }
        }
    }

    /**
     * Key event.
     *
     * @param tf the tf
     */
    private void keyEvent(TextField tf) {
        tf.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                btnNeu.fire();
            } else if (event.getCode() == KeyCode.ESCAPE) {
                tfMeldeIdent.clear();
                tfEkNummer.clear();
                tfAktionaersnummer.clear();
            }
        });
    }

}
