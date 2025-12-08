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

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;

import org.kordamp.ikonli.fontawesome5.FontAwesomeSolid;
import org.kordamp.ikonli.javafx.FontIcon;

import de.meetingapps.meetingclient.meetingClientDialoge.CtrlRoot;
import de.meetingapps.meetingportal.meetComEntities.EclAnmeldestelle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

/**
 * The Class CtrlAnmeldestelle.
 */
public class CtrlAnmeldestelle extends CtrlRoot {

    /** The tab pane. */
    @FXML
    private TabPane tabPane;

    /** The lbl ident. */
    @FXML
    private Label lblIdent;

    /** The lbl anmeldestelle. */
    @FXML
    private Label lblAnmeldestelle;

    /** The tf suche. */
    @FXML
    private TextField tfSuche;

    /** The tf anmeldestelle. */
    @FXML
    private TextField tfAnmeldestelle;

    /** The tf strasse. */
    @FXML
    private TextField tfStrasse;

    /** The tf plz. */
    @FXML
    private TextField tfPlz;

    /** The tf ort. */
    @FXML
    private TextField tfOrt;

    /** The tf land. */
    @FXML
    private TextField tfLand;

    /** The ta notiz. */
    @FXML
    private TextArea taNotiz;

    /** The tf telefon. */
    @FXML
    private TextField tfTelefon;

    /** The tf fax. */
    @FXML
    private TextField tfFax;

    /** The tf email. */
    @FXML
    private TextField tfEmail;

    /** The btn zurueck. */
    @FXML
    private Button btnZurueck;

    /** The btn weiter. */
    @FXML
    private Button btnWeiter;

    /** The btn suche. */
    @FXML
    private Button btnSuche;

    /** The btn neu. */
    @FXML
    private Button btnNeu;

    /** The btn beenden. */
    @FXML
    private Button btnBeenden;

    /** The btn speichern. */
    @FXML
    private Button btnSpeichern;

    /** The table view. */
    @FXML
    private TableView<EclAnmeldestelle> tableView;

    /** The col ident. */
    @FXML
    private TableColumn<EclAnmeldestelle, Integer> colIdent;

    /** The col anmeldestelle. */
    @FXML
    private TableColumn<EclAnmeldestelle, String> colAnmeldestelle;

    /** The col strasse. */
    @FXML
    private TableColumn<EclAnmeldestelle, String> colStrasse;

    /** The col plz. */
    @FXML
    private TableColumn<EclAnmeldestelle, String> colPlz;

    /** The col ort. */
    @FXML
    private TableColumn<EclAnmeldestelle, String> colOrt;

    /** The col land. */
    @FXML
    private TableColumn<EclAnmeldestelle, String> colLand;

    /** The col telefon. */
    @FXML
    private TableColumn<EclAnmeldestelle, String> colTelefon;

    /** The col fax. */
    @FXML
    private TableColumn<EclAnmeldestelle, String> colFax;

    /** The col email. */
    @FXML
    private TableColumn<EclAnmeldestelle, String> colEmail;

    /** The anm. */
    private EclAnmeldestelle anm;

    /** The list. */
    private ArrayList<EclAnmeldestelle> list = new ArrayList<>();

//    /** The l db bundle. */
//    private DbBundle lDbBundle;

    /**
     * Initialize.
     */
    @FXML
    private void initialize() {

        anm = new EclAnmeldestelle(1, "Bank 1", "Straße 1", "81379", "München", "Land", "", "", "", "",
                Timestamp.valueOf(LocalDateTime.now()));
        list.add(anm);
        anm = new EclAnmeldestelle(2, "Bank 2", "Straße 2", "81379", "München", "Land", "", "", "", "",
                Timestamp.valueOf(LocalDateTime.now()));
        list.add(anm);
        anm = new EclAnmeldestelle(3, "Bank 3", "Straße 3", "81379", "München", "Land", "", "", "", "",
                Timestamp.valueOf(LocalDateTime.now()));
        list.add(anm);
        anm = new EclAnmeldestelle(5, "Bank 5", "Straße 3", "81379", "München", "Land", "", "", "", "",
                Timestamp.valueOf(LocalDateTime.now()));
        list.add(anm);
        anm = new EclAnmeldestelle(6, "Bank 6", "Straße 3", "81379", "München", "Land", "", "", "", "",
                Timestamp.valueOf(LocalDateTime.now()));
        list.add(anm);
        /*
         * Geladene Liste
         */

        tableView.getItems().setAll(list);

        colIdent.setCellValueFactory(new PropertyValueFactory<EclAnmeldestelle, Integer>("ident"));
        colAnmeldestelle.setCellValueFactory(new PropertyValueFactory<EclAnmeldestelle, String>("name"));
        colStrasse.setCellValueFactory(new PropertyValueFactory<EclAnmeldestelle, String>("strasse"));
        colPlz.setCellValueFactory(new PropertyValueFactory<EclAnmeldestelle, String>("plz"));
        colOrt.setCellValueFactory(new PropertyValueFactory<EclAnmeldestelle, String>("ort"));
        colLand.setCellValueFactory(new PropertyValueFactory<EclAnmeldestelle, String>("land"));
        colTelefon.setCellValueFactory(new PropertyValueFactory<EclAnmeldestelle, String>("telefon"));
        colFax.setCellValueFactory(new PropertyValueFactory<EclAnmeldestelle, String>("fax"));
        colEmail.setCellValueFactory(new PropertyValueFactory<EclAnmeldestelle, String>("email"));

        tableView.setRowFactory(tv -> {
            TableRow<EclAnmeldestelle> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() >= 2 && (!row.isEmpty())) {
                    checkChanges();
                    EclAnmeldestelle selectedAnm = row.getItem();
                    setValues(selectedAnm);
                }
            });
            return row;
        });

        tabPane.addEventFilter(KeyEvent.ANY, event -> {

            final KeyCombination focusSearchField = new KeyCodeCombination(KeyCode.F, KeyCombination.CONTROL_DOWN);

            if (focusSearchField.match(event))
                tfSuche.requestFocus();
        });

        btnZurueck.setGraphic(new FontIcon(FontAwesomeSolid.ANGLE_DOUBLE_LEFT));
        btnWeiter.setGraphic(new FontIcon(FontAwesomeSolid.ANGLE_DOUBLE_RIGHT));
        btnSuche.setGraphic(new FontIcon(FontAwesomeSolid.SEARCH));
        btnNeu.setGraphic(new FontIcon(FontAwesomeSolid.PLUS));

        anm = list.get(0);
        setValues(anm);

    }

    /**
     * Search anm.
     *
     * @param event the event
     */
    @FXML
    void searchAnm(ActionEvent event) {

        if (!anm.equals(getValues())) {
            if (showAlert()) {
                btnSpeichern.fire();
            }
        }

        if (!tfSuche.getText().isEmpty()) {

            final String str = tfSuche.getText();

            boolean isInteger = str.matches("\\d+");
            EclAnmeldestelle tmpAnm = null;

            if (isInteger) {
                int ident = Integer.parseInt(str);
                tmpAnm = list.stream().filter(anm -> anm.getIdent() == ident).findFirst().orElse(null);
            } else {
                tmpAnm = list.stream().filter(anm -> anm.getName().contains(str)).findFirst().orElse(null);
            }

            if (tmpAnm == null) {
                System.out.println("Nichts gefunden");
            } else {
                setValues(tmpAnm);
                tfAnmeldestelle.requestFocus();
            }
        }
    }

    /**
     * New anm.
     */
    @FXML
    void newAnm() {

        checkChanges();

        EclAnmeldestelle newAnm = new EclAnmeldestelle();
        setValues(newAnm);

    }

    /**
     * Sets the values.
     *
     * @param anm the new values
     */
    private void setValues(EclAnmeldestelle anm) {

        this.anm = anm;

        lblIdent.setText(String.valueOf(anm.getIdent()));
        lblAnmeldestelle.setText(anm.getName());
        tfSuche.setText(String.valueOf(anm.getIdent()));

        tfAnmeldestelle.setText(anm.getName());
        tfStrasse.setText(anm.getStrasse());
        tfPlz.setText(anm.getPlz());
        tfOrt.setText(anm.getOrt());
        tfLand.setText(anm.getLand());
        tfTelefon.setText(anm.getTelefon());
        tfFax.setText(anm.getFax());
        tfEmail.setText(anm.getEmail());

    }

    /**
     * Gets the values.
     *
     * @return the values
     */
    private EclAnmeldestelle getValues() {

        EclAnmeldestelle anm = new EclAnmeldestelle();

        anm.setIdent(Integer.parseInt(lblIdent.getText()));
        anm.setName(tfAnmeldestelle.getText());
        anm.setStrasse(tfStrasse.getText());
        anm.setPlz(tfPlz.getText());
        anm.setOrt(tfOrt.getText());
        anm.setLand(tfLand.getText());
        anm.setTelefon(tfTelefon.getText());
        anm.setFax(tfFax.getText());
        anm.setEmail(tfEmail.getText());
        anm.setNotiz(taNotiz.getText());
        anm.setZuletztGeandert(this.anm.getZuletztGeandert());

        return anm;
    }

    /**
     * Search on enter.
     *
     * @param event the event
     */
    @FXML
    void searchOnEnter(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER)
            btnSuche.fire();
    }

    /**
     * Save on shortcut.
     *
     * @param event the event
     */
    @FXML
    void saveOnShortcut(KeyEvent event) {

        final KeyCombination keyComb = new KeyCodeCombination(KeyCode.ENTER, KeyCombination.CONTROL_DOWN);

        if (keyComb.match(event))
            btnSpeichern.fire();
    }

    /**
     * Save anm.
     *
     * @param event the event
     */
    @FXML
    void saveAnm(ActionEvent event) {
        /*
         * Insert or Update
         */

        EclAnmeldestelle oldAnm = anm;
        EclAnmeldestelle newAnm = getValues();

        System.out.println(oldAnm.toString());
        System.out.println(newAnm.toString());

        if (!oldAnm.equals(newAnm)) {

            if (newAnm.getIdent() == 0) {

                System.out.println("insert");

            } else {

                System.out.println("update");

            }

        } else {

            System.out.println("keine Aenderung");

        }
    }

    /**
     * Prev anm.
     */
    @FXML
    void prevAnm() {
        checkChanges();
        int idx = list.indexOf(anm);

        if (idx > 0)
            setValues(list.get(--idx));

        tfAnmeldestelle.requestFocus();
    }

    /**
     * Next anm.
     */
    @FXML
    void nextAnm() {
        checkChanges();
        int idx = list.indexOf(anm);

        if (++idx < list.size())
            setValues(list.get(idx));

        tfAnmeldestelle.requestFocus();
    }

    /**
     * Close.
     *
     * @param event the event
     */
    @FXML
    void close(ActionEvent event) {
        checkChanges();
        eigeneStage.close();
    }

    /**
     * Check changes.
     */
    private void checkChanges() {
        if (!anm.equals(getValues())) {
            if (showAlert()) {
                btnSpeichern.fire();
            }
        }
    }

    /**
     * Show alert.
     *
     * @return the boolean
     */
    private Boolean showAlert() {

        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Änderungen speichern?");
        alert.setHeaderText("Änderungen speichern?");
        alert.setContentText("Sollen ihre Änderugen an der aktuellen Anforderungsstelle gespeichert werden?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK)
            return true;
        else
            return false;
    }

    /**
     * Inits the.
     *
     * @param pEigeneStage the eigene stage
     */
    public void init(Stage pEigeneStage) {
        eigeneStage = pEigeneStage;
    }
}
