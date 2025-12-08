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

import java.io.File;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.kordamp.ikonli.fontawesome5.FontAwesomeRegular;
import org.kordamp.ikonli.fontawesome5.FontAwesomeSolid;
import org.kordamp.ikonli.javafx.FontIcon;

import de.meetingapps.meetingclient.meetingClientDialoge.CtrlRoot;
import de.meetingapps.meetingportal.meetComAllg.CaDateiVerwaltung;
import de.meetingapps.meetingportal.meetComDb.DbBundle;
import de.meetingapps.meetingportal.meetComEntities.EclImportProfil;
import de.meetingapps.meetingportal.meetComEntities.EclWortmeldetischAktion;
import de.meetingapps.meetingportal.meetComEntities.EclWortmeldetischFolgeStatus;
import de.meetingapps.meetingportal.meetComEntities.EclWortmeldetischStatus;
import de.meetingapps.meetingportal.meetComEntities.EclWortmeldetischStatusWeiterleitung;
import de.meetingapps.meetingportal.meetComEntities.EclWortmeldetischView;
import de.meetingapps.meetingportal.meetComEntities.EclWortmeldetischViewStatus;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 * The Class CtrlImportWortmeldetisch.
 */
public class CtrlImportWortmeldetisch extends CtrlRoot {

    /** The tf file. */
    @FXML
    private TextField tfFile;

    /** The btn file. */
    @FXML
    private Button btnFile;

    /** The btn aktion. */
    @FXML
    private Button btnAktion;

    /** The btn folge status. */
    @FXML
    private Button btnFolgeStatus;

    /** The btn status. */
    @FXML
    private Button btnStatus;

    /** The btn status weiterleitung. */
    @FXML
    private Button btnStatusWeiterleitung;

    /** The btn view. */
    @FXML
    private Button btnView;

    /** The btn view status. */
    @FXML
    private Button btnViewStatus;

    /** The btn info. */
    @FXML
    private Button btnInfo;

    /** The table view. */
    @FXML
    private TableView<Object> tableView;

    /** The btn close. */
    @FXML
    private Button btnClose;

    /** The lbl code. */
    @FXML
    private Label lblCode;

    /** The btn import. */
    @FXML
    private Button btnImport;

    /** The info box. */
    @FXML
    private VBox infoBox;

    /** The box 1. */
    @FXML
    private HBox box1;

    /** The box 2. */
    @FXML
    private HBox box2;

    /** The box 3. */
    @FXML
    private HBox box3;

    /** The file. */
    private File file = null;

    /** The db bundle. */
    private DbBundle dbBundle = null;

    /** The profiles. */
    private List<EclImportProfil> profiles = null;

    /** The insert list. */
    private LinkedList<Object> insertList = null;

    /** The violation list. */
    private LinkedList<String> violationList = null;

    /** The import class. */
    private String importClass = "";

    /** The set nr. */
    private int setNr = 0;

    /** The return code. */
    private int returnCode = 0;

    /** The list aktion. */
    private List<EclWortmeldetischAktion> listAktion = null;

    /** The list folge status. */
    private List<EclWortmeldetischFolgeStatus> listFolgeStatus = null;

    /** The list status. */
    private List<EclWortmeldetischStatus> listStatus = null;

    /** The list status weiterleitung. */
    private List<EclWortmeldetischStatusWeiterleitung> listStatusWeiterleitung = null;

    /** The list view. */
    private List<EclWortmeldetischView> listView = null;

    /** The list view status. */
    private List<EclWortmeldetischViewStatus> listViewStatus = null;

    /** The aktion. */
    final String aktion = "EclWortmeldetischAktion";

    /** The folge status. */
    final String folgeStatus = "EclWortmeldetischFolgeStatus";

    /** The status. */
    final String status = "EclWortmeldetischStatus";

    /** The status weiterleitung. */
    final String statusWeiterleitung = "EclWortmeldetischStatusWeiterleitung";

    /** The view. */
    final String view = "EclWortmeldetischView";

    /** The view status. */
    final String viewStatus = "EclWortmeldetischViewStatus";

    /** The disabled columns. */
    private final Set<String> disabledColumns = Set.of("serialVersionUID", "logDrucken", "statusArray",
            "summeOffsetInAnzeigeView", "summeOffsetInAnzeigeVersammlungsleiter", "wortmeldetischFolgeStatusList",
            "rederaumErgaenzen", "testraumErgaenzen");

    /**
     * Initialize.
     */
    @FXML
    private void initialize() {

        btnInfo.setGraphic(new FontIcon(FontAwesomeSolid.INFO_CIRCLE));
        btnInfo.setOnMouseEntered(e -> infoBox.setVisible(true));
        btnInfo.setOnMouseExited(e -> infoBox.setVisible(false));

        box1.getChildren().add(0, new FontIcon(FontAwesomeSolid.CHECK));
        box2.getChildren().add(0, new FontIcon(FontAwesomeRegular.CIRCLE));
        box3.getChildren().add(0, new FontIcon(FontAwesomeSolid.TIMES));

        btnAktion.setId(aktion);
        btnFolgeStatus.setId(folgeStatus);
        btnStatus.setId(status);
        btnStatusWeiterleitung.setId(statusWeiterleitung);
        btnView.setId(view);
        btnViewStatus.setId(viewStatus);

        btnAktion.setOnAction(e -> show(btnAktion.getId()));
        btnFolgeStatus.setOnAction(e -> show(btnFolgeStatus.getId()));
        btnStatus.setOnAction(e -> show(btnStatus.getId()));
        btnStatusWeiterleitung.setOnAction(e -> show(btnStatusWeiterleitung.getId()));
        btnView.setOnAction(e -> show(btnView.getId()));
        btnViewStatus.setOnAction(e -> show(btnViewStatus.getId()));

    }

    /**
     * Show.
     *
     * @param btnId the btn id
     */
    private void show(String btnId) {

        btnImport.setDisable(true);

        if (tfFile.getText().matches("\\d+")) {

            final int tmpSetNr = Integer.parseInt(tfFile.getText());

            if (tmpSetNr != setNr) {

                setNr = tmpSetNr;

                search();

            }
        }

        if (setNr != 0) {

            importClass = btnId;

            lblCode.setText("Anzeige SetNr: " + setNr + " Klasse: " + importClass);

            configureTable();
            tableView.getItems().setAll(setItems());

        }
    }

    /**
     * Search.
     */
    private void search() {

        dbBundle = new DbBundle();
        dbBundle.openAll();

        dbBundle.dbWortmeldetischAktion.readAll(setNr);
        listAktion = dbBundle.dbWortmeldetischAktion.ergebnis();
        setButtonIcon(btnAktion, listAktion.isEmpty() ? 3 : 2);

        dbBundle.dbWortmeldetischFolgeStatus.readAll(setNr);
        listFolgeStatus = dbBundle.dbWortmeldetischFolgeStatus.ergebnis();
        setButtonIcon(btnFolgeStatus, listFolgeStatus.isEmpty() ? 3 : 2);

        dbBundle.dbWortmeldetischStatus.readAll(setNr);
        listStatus = dbBundle.dbWortmeldetischStatus.ergebnis();
        setButtonIcon(btnStatus, listStatus.isEmpty() ? 3 : 2);

        dbBundle.dbWortmeldetischStatusWeiterleitung.readAll(setNr);
        listStatusWeiterleitung = dbBundle.dbWortmeldetischStatusWeiterleitung.ergebnis();
        setButtonIcon(btnStatusWeiterleitung, listStatusWeiterleitung.isEmpty() ? 3 : 2);

        dbBundle.dbWortmeldetischView.readAll(setNr);
        listView = dbBundle.dbWortmeldetischView.ergebnis();
        setButtonIcon(btnView, listView.isEmpty() ? 3 : 2);

        dbBundle.dbWortmeldetischViewStatus.readAll(setNr);
        listViewStatus = dbBundle.dbWortmeldetischViewStatus.ergebnis();
        setButtonIcon(btnViewStatus, listViewStatus.isEmpty() ? 3 : 2);

        dbBundle.closeAll();
    }

    /**
     * Sets the button icon.
     *
     * @param btn the btn
     * @param num the num
     */
    private void setButtonIcon(Button btn, int num) {

        FontIcon icon = null;

        btn.setContentDisplay(ContentDisplay.RIGHT);

        switch (num) {
        case 1:
            icon = new FontIcon(FontAwesomeSolid.CHECK);
            btn.setGraphic(icon);
            break;
        case 2:
            icon = new FontIcon(FontAwesomeRegular.CIRCLE);
            btn.setGraphic(icon);
            break;
        case 3:
            icon = new FontIcon(FontAwesomeSolid.TIMES);
            btn.setGraphic(icon);
            break;
        }
    }

    /**
     * On file.
     *
     * @param event the event
     */
    @FXML
    void onFile(ActionEvent event) {

        final FileChooser fileChooser = new FileChooser();
        file = fileChooser.showOpenDialog(eigeneStage);

        if (file != null) {

            tfFile.setText(file.getAbsolutePath());

            returnCode = 0;

            dbBundle = new DbBundle();
            dbBundle.openAll();

            profiles = dbBundle.dbImportProfil.readAll();

            dbBundle.closeAll();

            CaDateiVerwaltung dateiVerwaltung = new CaDateiVerwaltung();

            returnCode = tableFile(dateiVerwaltung.convertExceltoList(file));
            
            

            if (violationList != null && !violationList.isEmpty()) {
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Nicht unterstütze Felder");
                alert.setHeaderText("Folgende Felder werden nicht unterstützt");
                Label label = new Label("Es wurden folgende Dateifehler gefunden:");

                ListView<String> listView = new ListView<>(FXCollections.observableList(violationList));

                listView.setPrefWidth(600.0);
                GridPane.setVgrow(listView, Priority.ALWAYS);
                GridPane.setHgrow(listView, Priority.ALWAYS);

                GridPane expContent = new GridPane();
                expContent.setMaxWidth(Double.MAX_VALUE);
                expContent.add(label, 0, 0);
                expContent.add(listView, 0, 1);

                alert.getDialogPane().setContent(expContent);
                alert.showAndWait();
            } else {
                lblCode.setText(returnText());
            }
        }
    }

    /**
     * Table file.
     *
     * @param list the list
     * @return the int
     */
    private int tableFile(LinkedList<String[]> list) {

        if (!list.isEmpty()) {

            List<String> columnsFile = new LinkedList<>(Arrays.asList(list.get(0)));

            EclImportProfil importProfile = findProfile(columnsFile);

            if (importProfile != null) {

                returnCode = 1;

                setNr = 0;
                importClass = importProfile.getKlasse();
                insertList = new LinkedList<>();
                violationList = new LinkedList<>();

                list.remove(0);
                Map<String, String> profilMap = importProfile.createMap();

                //                ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
                //                Validator validator = factory.getValidator();

                for (var line : list) {
                    Map<String, String> dataMap = new HashMap<>();
                    for (int i = 0; i < columnsFile.size(); i++) {

                        final String db = profilMap.get(columnsFile.get(i));
                        final String lineTrim = line[i] == null ? "" : line[i];

                        if (db != null) {
                            checkSetNr(db, lineTrim);
                            dataMap.put(db, lineTrim);
                        }
                    }

                    Object o = create(dataMap);
                    //                    Set<ConstraintViolation<Object>> violations = validator.validate(o);
                    //                    if (violations.size() > 0) {
                    //                        for (ConstraintViolation<Object> violation : violations) {
                    //                            violationList.add(violation.getMessage() + " - " + violation.getInvalidValue() + " - "
                    //                                    + violation.getInvalidValue().toString().length() + " Zeichen");
                    //                        }
                    //                    } else
                    insertList.add(o);

                }

                tableView.getItems().setAll(insertList);

                configureTable();

                search();
                
                final Boolean success = returnCode == 1;
                btnImport.setDisable(!success);
                return returnCode;

            } else {
                return 5;
            }
        } else {
            return 4;
        }
    }

    /**
     * Find profile.
     *
     * @param columnsFile the columns file
     * @return the ecl import profil
     */
    private EclImportProfil findProfile(List<String> columnsFile) {

        EclImportProfil importProfile = null;

        for (EclImportProfil profile : profiles) {

            List<String> columnsProfil = Arrays.asList(profile.getDateiFeld());

            for (String column : columnsFile) {

                if (!columnsProfil.contains(column)) {
                    // Checken welche Spalten im Profil noch fehlen
                    // System.out.println(column);
                    importProfile = null;
                    break;
                } else {
                    importProfile = profile;
                }
            }

            if (importProfile != null)
                return importProfile;
        }
        return null;
    }

    /**
     * Check set nr.
     *
     * @param db       the db
     * @param lineTrim the line trim
     */
    private void checkSetNr(String db, String lineTrim) {

        if (db.equals("setNr")) {

            if (setNr == 0) {
                setNr = Integer.parseInt(lineTrim);
            } else if (setNr != Integer.parseInt(lineTrim)) {
                setNr = 0;
                returnCode = 3;
            }
        }
    }

    /**
     * Configure table.
     */
    private void configureTable() {

        tableView.getColumns().clear();

        Field[] fields = getFields();

        for (Field f : fields) {
            if (!disabledColumns.contains(f.getName())) {
                TableColumn<Object, ?> col = new TableColumn<>(f.getName().toUpperCase(Locale.GERMANY));
                col.setCellValueFactory(new PropertyValueFactory<>(f.getName()));

                tableView.getColumns().add(col);
            }
        }
    }

    /**
     * Sets the items.
     *
     * @return the {@code list<? extends object>}
     */
    private List<? extends Object> setItems() {

        switch (importClass) {
        case aktion:
            return listAktion;
        case folgeStatus:
            return listFolgeStatus;
        case status:
            return listStatus;
        case statusWeiterleitung:
            return listStatusWeiterleitung;
        case view:
            return listView;
        case viewStatus:
            return listViewStatus;
        default:
            return null;
        }
    }

    /**
     * Gets the fields.
     *
     * @return the fields
     */
    private Field[] getFields() {

        switch (importClass) {
        case aktion:
            return EclWortmeldetischAktion.class.getDeclaredFields();
        case folgeStatus:
            return EclWortmeldetischFolgeStatus.class.getDeclaredFields();
        case status:
            return EclWortmeldetischStatus.class.getDeclaredFields();
        case statusWeiterleitung:
            return EclWortmeldetischStatusWeiterleitung.class.getDeclaredFields();
        case view:
            return EclWortmeldetischView.class.getDeclaredFields();
        case viewStatus:
            return EclWortmeldetischViewStatus.class.getDeclaredFields();
        default:
            return null;
        }
    }

    /**
     * Creates the.
     *
     * @param dataMap the data map
     * @return the object
     */
    private Object create(Map<String, String> dataMap) {

        switch (importClass) {
        case aktion:
            return EclWortmeldetischAktion.create(dataMap);
        case folgeStatus:
            return EclWortmeldetischFolgeStatus.create(dataMap);
        case status:
            return EclWortmeldetischStatus.create(dataMap);
        case statusWeiterleitung:
            return EclWortmeldetischStatusWeiterleitung.create(dataMap);
        case view:
            return EclWortmeldetischView.create(dataMap);
        case viewStatus:
            return EclWortmeldetischViewStatus.create(dataMap);
        default:
            return null;
        }
    }

    /**
     * On import.
     *
     * @param event the event
     */
    @FXML
    void onImport(ActionEvent event) {

        if (insertList != null) {

            if (confirmAlert(setItems().isEmpty())) {

                dbBundle.openAll();

                switch (importClass) {
                case aktion:
                    listAktion.clear();
                    dbBundle.dbWortmeldetischAktion.deleteSet(setNr);
                    for (Object o : insertList) {
                        dbBundle.dbWortmeldetischAktion.insert((EclWortmeldetischAktion) o);
                        listAktion.add((EclWortmeldetischAktion) o);
                    }
                    setButtonIcon(btnAktion, 1);
                    break;
                case folgeStatus:
                    listFolgeStatus.clear();
                    dbBundle.dbWortmeldetischFolgeStatus.deleteSet(setNr);
                    for (Object o : insertList) {
                        dbBundle.dbWortmeldetischFolgeStatus.insert((EclWortmeldetischFolgeStatus) o);
                        listFolgeStatus.add((EclWortmeldetischFolgeStatus) o);
                    }
                    setButtonIcon(btnFolgeStatus, 1);
                    break;
                case status:
                    listStatus.clear();
                    dbBundle.dbWortmeldetischStatus.deleteSet(setNr);
                    for (Object o : insertList) {
                        dbBundle.dbWortmeldetischStatus.insert((EclWortmeldetischStatus) o);
                        listStatus.add((EclWortmeldetischStatus) o);
                    }
                    setButtonIcon(btnStatus, 1);
                    break;
                case statusWeiterleitung:
                    listStatusWeiterleitung.clear();
                    dbBundle.dbWortmeldetischStatusWeiterleitung.deleteSet(setNr);
                    for (Object o : insertList) {
                        dbBundle.dbWortmeldetischStatusWeiterleitung.insert((EclWortmeldetischStatusWeiterleitung) o);
                        listStatusWeiterleitung.add((EclWortmeldetischStatusWeiterleitung) o);
                    }
                    setButtonIcon(btnStatusWeiterleitung, 1);
                    break;
                case view:
                    listView.clear();
                    dbBundle.dbWortmeldetischView.deleteSet(setNr);
                    for (Object o : insertList) {
                        dbBundle.dbWortmeldetischView.insert((EclWortmeldetischView) o);
                        listView.add((EclWortmeldetischView) o);
                    }
                    setButtonIcon(btnView, 1);
                    break;
                case viewStatus:
                    listViewStatus.clear();
                    dbBundle.dbWortmeldetischViewStatus.deleteSet(setNr);
                    for (Object o : insertList) {
                        dbBundle.dbWortmeldetischViewStatus.insert((EclWortmeldetischViewStatus) o);
                        listViewStatus.add((EclWortmeldetischViewStatus) o);
                    }
                    setButtonIcon(btnViewStatus, 1);
                    break;
                default:
                    break;
                }

                dbBundle.closeAll();
                returnCode = 2;
                btnImport.setDisable(true);

            } else {
                returnCode = 3;
            }
            lblCode.setText(returnText());
        }
    }

    /**
     * Return text.
     *
     * @return the string
     */
    private String returnText() {
        return switch (returnCode) {
        case 0 -> "Bei der Bearbeitung ist ein Fehler aufgetreten";
        case 1 -> "Einlesen erfolgreich - Set-Nummer: " + setNr + " kann importiert werden.";
        case 2 -> "Import abgeschlossen.";
        case 3 -> "Mehrere Set-Nummern in Datei erkannt - Import nicht möglich.";
        case 4 -> "Liste ist leer.";
        case 5 -> "Kein passendes Importprofil gefunden.";
        default -> "";
        };
    }

    /**
     * Confirm alert.
     *
     * @param isEmpty the is empty
     * @return the boolean
     */
    private Boolean confirmAlert(Boolean isEmpty) {

        if (isEmpty) {
            return true;
        }
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Aktion bitte bestätigen");
        alert.setHeaderText("Set: " + setNr + " - " + importClass + " -  bereits vorhanden!");
        alert.setContentText("Trotzdem importieren?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * On close.
     *
     * @param event the event
     */
    @FXML
    void onClose(ActionEvent event) {
        eigeneStage.hide();
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
