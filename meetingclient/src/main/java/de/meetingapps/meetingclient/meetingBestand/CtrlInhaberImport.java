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

import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.kordamp.ikonli.fontawesome5.FontAwesomeSolid;
import org.kordamp.ikonli.javafx.FontIcon;

import de.meetingapps.meetingclient.meetingClientDialoge.CaController;
import de.meetingapps.meetingclient.meetingClientDialoge.CaIcon;
import de.meetingapps.meetingclient.meetingClientDialoge.CtrlRoot;
import de.meetingapps.meetingclient.meetingClientOberflaechen.CustomAlert;
import de.meetingapps.meetingclient.meetingClientOberflaechen.LoadingScreen;
import de.meetingapps.meetingportal.meetComBl.BlInhaberImport;
import de.meetingapps.meetingportal.meetComBl.BlSammelkarten;
import de.meetingapps.meetingportal.meetComDb.DbBundle;
import de.meetingapps.meetingportal.meetComEh.EhGattung;
import de.meetingapps.meetingportal.meetComEntities.EclInhaberImportAnmeldedaten;
import de.meetingapps.meetingportal.meetComEntities.EclImportProfil;
import de.meetingapps.meetingportal.meetComEntities.EclMeldung;
import de.meetingapps.meetingportal.meetComHVParam.ParamS;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 * The Class CtrlInhaberImport.
 */
public class CtrlInhaberImport extends CtrlRoot {

    /** The root pane. */
    @FXML
    private AnchorPane rootPane;

    /** The tab pane. */
    @FXML
    private TabPane tabPane;

    /** The tf file. */
    @FXML
    private TextField tfFile;

    /** The cb profile. */
    @FXML
    private ComboBox<EclImportProfil> cbProfile;

    /** The btn clear file. */
    @FXML
    private Button btnClearFile;

    /** The btn file. */
    @FXML
    private Button btnFile;

    /** The btn read. */
    @FXML
    private Button btnRead;

    /** The table view. */
    @FXML
    private TableView<EclInhaberImportAnmeldedaten> tableView;

    /** The btn close. */
    @FXML
    private Button btnClose;

    /** The btn table columns. */
    @FXML
    private Button btnTableColumns;

    /** The btn create. */
    @FXML
    private Button btnCreate;

    /** The lbl code. */
    @FXML
    private Label lblCode;

    /** The btn copy. */
    @FXML
    private Button btnCopy;

    @FXML
    private Button btnCopyDetailed;

    /** The btn import. */
    @FXML
    private Button btnImport;

    private StackPane loading;

    /** The code. */
    private int code = 0;

    /** The list. */
    private List<EclInhaberImportAnmeldedaten> list;

    /** The file. */
    private File file;

    /** The columns. */
    private Set<String> columns = new LinkedHashSet<>(List.of("ekNr", "vorname", "nachname", "anmeldung", "strasse",
            "plz", "ort", "stueck", "wkn", "referenzEKNr", "isin", "gattungId", "datei", "dateikuerzel"));

    /** The tab id. */
    private String tabId = "1";

    /** The file type. */
    private String fileType = "";

    /** The gattung strings. */
    private List<String> gattungStrings = new ArrayList<>();

    /** The gattung map. */
    private Map<String, EhGattung> gattungMap = new HashMap<>();

    /** The directory. */
    private String directory = "C:\\";

    /** The bl InhaberImport import. */
    private BlInhaberImport blInhaberImportImport;

    /** The pw. */
    private final String pw = "geheim!";

    /** The disabled columns. */
    //  Spalten welche nicht in der Spaltenauswahl vorkommen sollen
    private final Set<String> disabledColumns = Set.of("ident", "EkComparator");

    /** The import klassen. */
    private final Set<String> importKlassen = Set.of("EclInhaberImportAnmeldedaten");

    /**
     * Initialize.
     */
    @FXML
    void initialize() {
        blInhaberImportImport = new BlInhaberImport(false, new DbBundle());

        blInhaberImportImport.loadLists();

        list = blInhaberImportImport.InhaberImportList;

        List<EclImportProfil> profiles = blInhaberImportImport.InhaberImportProfiles;
        profiles = profiles.stream().filter(e -> importKlassen.contains(e.getKlasse())).collect(Collectors.toList());

        if (!profiles.isEmpty()) {
            cbProfile.getItems().addAll(profiles);
            cbProfile.setValue(profiles.get(0));
        }

        cbProfile.getSelectionModel().selectedItemProperty().addListener((o, oV, nV) -> {
            btnRead.setDisable((nV == null && file == null) || file.getName().length() > 80);
        });

        btnClearFile.setOnAction(e -> clearLog());

        configureTable();

        tabPane.getSelectionModel().selectedItemProperty()
                .addListener(((observable, oldValue, newValue) -> configureLayout(newValue.getId())));

        btnTableColumns.setOnAction(e -> showAlertConfig());

        btnClearFile.setGraphic(new FontIcon(FontAwesomeSolid.TRASH));
        btnFile.setGraphic(new FontIcon(FontAwesomeSolid.FOLDER_OPEN));

        btnTableColumns.setGraphic(new FontIcon(FontAwesomeSolid.TABLE));
        btnCreate.setGraphic(new FontIcon(FontAwesomeSolid.PLUS));

        btnCopy.setGraphic(new FontIcon(FontAwesomeSolid.CLIPBOARD));
        btnCopyDetailed.setGraphic(new FontIcon(FontAwesomeSolid.CLIPBOARD_LIST));

        loading = LoadingScreen.createLoadingScreen(rootPane);

    }

    /**
     * Choose file.
     */
    @FXML
    private void chooseFile() {

        lblCode.setText("");
        final String verzBasis = ParamS.paramGeraet.lwPfadKundenordnerBasis + ParamS.eclEmittent.pfadErgKundenOrdner;
        final Boolean directoryCheck = new File(verzBasis).exists();

        final FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(
                new File(directory.equals("C:\\") && directoryCheck ? verzBasis.replace("\\PDF", "") : directory));
        file = fileChooser.showOpenDialog(eigeneStage);
        if (file != null) {
            directory = file.getParent();

            tfFile.setText(file.getAbsolutePath());
            fileType = FilenameUtils.getExtension(file.getAbsolutePath()).toLowerCase();

            if (blInhaberImportImport.typeCSV.contains(fileType) || blInhaberImportImport.typeExcel.contains(fileType)) {
                cbProfile.setVisible(true);
                AnchorPane.setRightAnchor(tfFile, 500.0);
            } else {
                cbProfile.setVisible(false);
                AnchorPane.setRightAnchor(tfFile, 285.0);
            }

            if (file.getName().length() <= 80 && cbProfile.getValue() != null)
                btnRead.setDisable(false);
            else if (file.getName().length() > 80)
                lblCode.setText("Dateiname zu lang (" + file.getName().length() + ") Zeichen - maximal 80 Zeichen!");
        }
        btnRead.setText("Einlesen");
        btnImport.setText("Importieren");
        btnImport.setDisable(true);
        btnCopy.setVisible(false);
        btnCopyDetailed.setVisible(false);
        tableView.getItems().clear();

    }

    /**
     * Read task.
     *
     * @return the task
     */
    private Task<Void> readTask() {
        Task<Void> task = new Task<Void>() {
            protected Void call() throws Exception {
                doRead();
                return null;
            }
        };
        task.setOnScheduled(e -> loading.setVisible(true));
        task.setOnSucceeded(e -> doError());
        task.setOnFailed(e -> loading.setVisible(false));

        return task;
    }

    /**
     * Do read.
     */
    private void doRead() {

        tableView.getItems().clear();
        blInhaberImportImport.importList = null;
        blInhaberImportImport.pSammelIdent = 0;

        try {
            InputStream in = new FileInputStream(file);
            byte[] fileByte = IOUtils.toByteArray(in);
            in.close();
            blInhaberImportImport.setFile(file, fileByte, cbProfile.getValue());

        } catch (IOException e) {
            e.printStackTrace();
        }
        code = blInhaberImportImport.prepareFile();
        Platform.runLater(() -> lblCode.setText(returnText(code)));
        System.out.println(returnText(code));

    }

    /**
     * On read.
     *
     * @param event the event
     */
    @FXML
    private void onRead(ActionEvent event) {
        if (tabId.equals("1")) {
            tableView.getItems().clear();
            new Thread(readTask()).start();
        } else {
            search();
        }
    }

    /**
     * Do error.
     */
    private void doError() {

        loading.setVisible(false);

        if (code == 14) {

            Stage newStage = new Stage();
            CaIcon.bestandsverwaltung(newStage);

            CtrlImportAdresse controllerFenster = new CtrlImportAdresse();
            controllerFenster.init(newStage, blInhaberImportImport.emptyAdressList);

            CaController caController = new CaController();
            caController.open(newStage, controllerFenster,
                    "/de/meetingapps/meetingclient/meetingBestand/ImportAdresse.fxml", 670, 370, "Adressen", true);

            lblCode.setText(returnText(15));

        } else if (code == 4) {

            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Nicht unterstütze Felder");
            alert.setHeaderText("Folgende Felder werden nicht unterstützt");
            Label label = new Label("Es wurden folgende Dateifehler gefunden:");

            ListView<String> listView = new ListView<>(FXCollections.observableList(blInhaberImportImport.violationList));

            listView.setPrefWidth(600.0);
            GridPane.setVgrow(listView, Priority.ALWAYS);
            GridPane.setHgrow(listView, Priority.ALWAYS);

            GridPane expContent = new GridPane();
            expContent.setMaxWidth(Double.MAX_VALUE);
            expContent.add(label, 0, 0);
            expContent.add(listView, 0, 1);

            alert.getDialogPane().setContent(expContent);
            alert.showAndWait();

        } else if (code == 6) {

            choiceList();

            int gattung = showDialog();
            if (gattung == 0) {
                lblCode.setText(returnText(7));
                return;
            } else {
                for (EclInhaberImportAnmeldedaten error : blInhaberImportImport.errorList)
                    error.setGattungId(gattung);
            }

            blInhaberImportImport.importList.addAll(blInhaberImportImport.errorList);
            blInhaberImportImport.importList.sort(Comparator.comparing(EclInhaberImportAnmeldedaten::getEkNr));
            tableView.getItems().addAll(blInhaberImportImport.importList);
            blInhaberImportImport.errorList = null;
            lblCode.setText(returnText(1));
            btnImport.setDisable(false);
            btnImport.setDefaultButton(true);
            btnCopy.setVisible(false);
            btnCopyDetailed.setVisible(false);
        } else if (code == 3) {

            final String title = "Datensätze vorhanden";
            final String header = "Alle Datensätze sind bereits vorhanden!";
            final String text = "Soll die Datei trotzdem importiert werden?";

            if (CustomAlert.passwortConfirmation(pw, title, header, text))
                code = 1;

        } else if (code == 12) {

            // TODO CustomAlert          Sammelkarte auswählen
            BlSammelkarten sam = new BlSammelkarten(false, new DbBundle());
            sam.holeSammelkartenMeldeDaten(true, 0);
            List<EclMeldung> tmpList = Arrays.asList(sam.rcSammelMeldung).stream().filter(
                    e -> e.akzeptiertDedizierteWeisung() && e.gattung == blInhaberImportImport.importList.get(0).getGattungId())
                    .collect(Collectors.toList());

            List<String> sammelkarten = new ArrayList<>();
            tmpList.forEach(e -> sammelkarten.add(e.meldungsIdent + " - " + e.name + " - " + e.zusatzfeld2));

            ChoiceDialog<String> dialog = new ChoiceDialog<>(sammelkarten.get(0), sammelkarten);

            dialog.setTitle("Sammelkarte auswählen");
            dialog.setHeaderText("Es wurden Weisungen erkannt \n Bitte Sammelkarte auswählen");

            dialog.setContentText("Bitte auswählen:");

            Optional<String> result = dialog.showAndWait();

            if (result.isPresent()) {
                code = 1;
                final String[] txt = result.get().split(" - ");
                blInhaberImportImport.pSammelIdent = Integer.valueOf(txt[0]);
                lblCode.setText("Weisung > " + result.get().toString());
            }
        }

        if (code == 1 || code == 13) {
            btnRead.setText("Einlesen - " + blInhaberImportImport.importList.size());
            btnImport.setText("Importieren");
            tableView.getItems().addAll(blInhaberImportImport.importList);
            btnImport.setDisable(false);
            btnImport.setDefaultButton(true);
        }
    }

    /**
     * Import task.
     *
     * @return the task
     */
    private Task<Void> importTask() {
        Task<Void> task = new Task<Void>() {
            protected Void call() throws Exception {
                doImport();
                return null;
            }
        };
        task.setOnScheduled(e -> loading.setVisible(true));
        task.setOnSucceeded(e -> {
            btnCopy.setVisible(true);
            btnCopyDetailed.setVisible(true);
            loading.setVisible(false);
        });
        task.setOnFailed(e -> loading.setVisible(false));

        return task;
    }

    /**
     * Do import.
     */
    private void doImport() {

        code = blInhaberImportImport.doImport();
        Platform.runLater(() -> {
            lblCode.setText(returnText(code));
            btnImport.setText("Importieren - " + blInhaberImportImport.importList.size());
        });
        btnRead.setDisable(true);
        btnImport.setDisable(true);
        tableView.getItems().clear();
        tableView.getItems().addAll(blInhaberImportImport.importList);
        list = blInhaberImportImport.InhaberImportList;
    }

    /**
     * On import.
     *
     * @param event the event
     */
    @FXML
    private void onImport(ActionEvent event) {
        new Thread(importTask()).start();
    }

    /**
     * On copy.
     *
     * @param event the event
     */
    @FXML
    private void onCopy(ActionEvent event) {
        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(setCopyString(false)), null);
    }

    @FXML
    private void onCopyDetailed(ActionEvent event) {
        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(setCopyString(true)), null);
    }

    /**
     * Sets the copy string.
     */
    private String setCopyString(Boolean detailed) {
        final String wkn = blInhaberImportImport.importList.get(0).getWkn();
        final String isin = blInhaberImportImport.importList.get(0).getIsin();

        // @formatter:off
        return "Sehr geehrte Damen und Herren, \n\n" 
                + "Ihre Eintrittskartennummer"
                + (blInhaberImportImport.importList.size() > 1 
                        ? ("n lauten " + blInhaberImportImport.ekVon + " - " + blInhaberImportImport.ekBis) 
                        : (" lautet " + blInhaberImportImport.ekVon)) 
                + "\n\n"
                + createEmailText(detailed)
                + (isin.isBlank() ? "WKN: " + wkn : "ISIN: " + isin) + " - "
                + ParamS.eclEmittent.bezeichnungKurz;
        // @formatter:on
    }

    private String createEmailText(Boolean detailed) {

        final StringBuilder sb = new StringBuilder();

        if (detailed) {
            for (var value : blInhaberImportImport.importList) {

                final String vorname = anonymize(value.getVorname(), 2);
                final String nachname = anonymize(value.getNachname(), 2);
                final String anmeldung = anonymize(value.getAnmeldung(), 3);

                sb.append("\t" + value.getReferenzEKNr());
                sb.append(vorname.isBlank() ? "" : " - " + vorname);
                sb.append(nachname.isBlank() ? "" : " " + nachname);
                sb.append(anmeldung.isBlank() ? "" : " - " + anmeldung);
                sb.append("\n");

            }
            sb.append("\n");
        }
        return sb.toString();
    }

    private String anonymize(String str, int length) {

        if (!str.isBlank() && str.length() > length - 1) {
            return str.substring(0, length) + "xxx";
        }
        return "";
    }

    /**
     * On enter.
     *
     * @param event the event
     */
    @FXML
    private void onEnter(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER)
            search();
    }

    /**
     * Search.
     */
    private void search() {
        if (tfFile.getText() != null) {
            List<EclInhaberImportAnmeldedaten> tmpList = list.stream()
                    .filter(d -> d.searchString().toLowerCase().contains(tfFile.getText().toLowerCase()))
                    .collect(Collectors.toList());
            tableView.getItems().setAll(tmpList);
        }
    }

    /**
     * Show dialog.
     *
     * @return the int
     */
    private int showDialog() {

        Dialog<String> dialog = new Dialog<>();

        dialog.setTitle("Gattung auswählen");
        dialog.setHeaderText("WKN/ISIN nicht vorhanden\nDatei trotzdem importieren?");

        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        GridPane grid = new GridPane();

        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 10, 10, 10));

        ChoiceBox<String> cbGattung = new ChoiceBox<String>();
        cbGattung.getItems().setAll(gattungStrings);
        cbGattung.setValue(gattungStrings.get(0));

        PasswordField tfPasswort = new PasswordField();

        grid.add(new Label("Gattung:"), 0, 0);
        grid.add(cbGattung, 1, 0);
        grid.add(new Label("Passwort:"), 0, 1);
        grid.add(tfPasswort, 1, 1);

        Node okButton = dialog.getDialogPane().lookupButton(ButtonType.OK);
        okButton.setDisable(true);

        tfPasswort.textProperty().addListener((o, oV, nV) -> okButton.setDisable(!nV.equals(pw)));

        dialog.getDialogPane().setContent(grid);

        Platform.runLater(() -> tfPasswort.requestFocus());

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == ButtonType.OK) {
                return cbGattung.getValue();
            }
            return null;
        });

        Optional<String> result = dialog.showAndWait();
        if (result.isPresent())
            return gattungMap.get(result.get()).getGattungId();
        else
            return 0;
    }

    /**
     * Choice list.
     */
    private void choiceList() {

        gattungMap.clear();
        gattungStrings.clear();

        for (EhGattung gattung : blInhaberImportImport.gattungList) {
            gattungMap.put(gattung.getBezeichnung() + ", ISIN: " + Arrays.toString(gattung.getIsinList().toArray()),
                    gattung);
            gattungStrings
                    .add(gattung.getBezeichnung() + ", ISIN: " + Arrays.toString(gattung.getIsinList().toArray()));
        }
    }

    /**
     * Configure table.
     */
    private void configureTable() {

        tableView.getColumns().clear();

        for (String str : columns) {

            TableColumn<EclInhaberImportAnmeldedaten, ?> col = new TableColumn<>(str.toUpperCase(Locale.GERMANY));
            col.setCellValueFactory(new PropertyValueFactory<>(str));

            tableView.getColumns().add(col);
        }
    }

    /**
     * Show alert config.
     */
    private void showAlertConfig() {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Spalten anpassen");
        alert.setHeaderText(null);
        alert.setGraphic(null);

        GridPane layout = new GridPane();
        layout.setHgap(20);
        layout.setVgap(10);
        layout.setPadding(new Insets(20, 40, 10, 20));

        final int columnLength = 12;

        Field[] fields = EclInhaberImportAnmeldedaten.class.getDeclaredFields();

        int row = 0;
        int col = 0;

        List<CheckBox> cbList = new ArrayList<>();

        for (Field f : fields) {
            if (!disabledColumns.contains(f.getName())) {

                final CheckBox cb = new CheckBox(f.getName());
                cb.setPrefWidth(150);
                cb.setSelected(columns.contains(f.getName()));

                final Button btnField = new Button("", cb);
                btnField.setBackground(null);

                if (row > 0 && row % columnLength == 0) {
                    col++;
                    row = 0;
                }

                cbList.add(cb);
                layout.add(btnField, col, row);
                row++;
            }
        }

        alert.getDialogPane().setContent(layout);
        ButtonType btnSave = new ButtonType("Übernehmen", ButtonData.OK_DONE);
        alert.getButtonTypes().setAll(btnSave, new ButtonType("Abbrechen", ButtonData.CANCEL_CLOSE));

        Optional<ButtonType> result = alert.showAndWait();
        result.ifPresent(e -> e.getButtonData().toString());

        if (result.get() == btnSave) {
            Set<String> columnsNew = new LinkedHashSet<>();

            for (CheckBox cb : cbList) {
                if (cb.isSelected())
                    columnsNew.add(cb.getText());
            }

            if (!columns.equals(columnsNew)) {
                columns = columnsNew;
                configureTable();
                tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
            }
        }
    }

    /**
     * On create.
     *
     * @param event the event
     */
    @FXML
    private void onCreate(ActionEvent event) {

        Stage newStage = new Stage();
        CaIcon.bestandsverwaltung(newStage);

        CtrlMeldedaten controllerFenster = new CtrlMeldedaten();
        controllerFenster.init(newStage, null);

        CaController caController = new CaController();
        caController.open(newStage, controllerFenster, "/de/meetingapps/meetingclient/meetingBestand/Meldedaten.fxml",
                1300, 670, "Meldedaten", true);

    }

    /**
     * Clear log.
     *
     * @param btn the btn
     */
    private void clearLog() {
        file = null;
        tfFile.clear();
        btnRead.setDisable(true);
        btnRead.setText("Einlesen");
        btnImport.setDisable(true);
        tableView.getItems().clear();
        lblCode.setText("");
    }

    /**
     * Configure layout.
     *
     * @param tabId the tab id
     */
    private void configureLayout(String tabId) {

        tableView.getItems().clear();
        file = null;

        this.tabId = tabId;

        tfFile.clear();
        tfFile.setEditable(!tfFile.isEditable());
        btnRead.setDisable(tabId.equals("1") ? true : false);
        btnImport.setDefaultButton(false);
        btnImport.setVisible(btnImport.isVisible());
        btnClearFile.setVisible(!btnClearFile.isVisible());
        btnFile.setVisible(!btnFile.isVisible());
        btnRead.setPrefWidth(tabId.equals("1") ? 135 : 255);
        cbProfile.setVisible(false);
        btnRead.setText(tabId.equals("1") ? "Einlesen" : "Suchen");
        tfFile.setPromptText(tabId.equals("1") ? "Datei..." : "Suche...");
        AnchorPane.setRightAnchor(tfFile, 285.0);

        if (!tabId.equals("1"))
            tableView.getItems().addAll(list);

    }

    /**
     * Return text.
     *
     * @param key the key
     * @return the string
     */
    private String returnText(int key) {
        switch (key) {
        case 0:
            return "Bei der Bearbeitung ist ein Fehler aufgetreten.";
        case 1:
            return "Bearbeitung erfolgreich abgeschlossen.";
        case 2:
            return "Zeilen: " + blInhaberImportImport.errorLines + " nach Sonderzeichen überprüfen.";
        case 3:
            return "Datei bereits vorhanden.";
        case 4:
            return "Die Datei beinhaltet nicht kompatible Felder.";
        case 5:
            return "Die Spalten der Datei: [" + blInhaberImportImport.errorColumns + "] sind mit dem Profil nicht kompatibel.";
        case 6:
            return "WKN / ISIN ist nicht zuordenbar bitte auswählen.";
        case 7:
            return "Auswahl der ISIN / Gattung wurde abgebrochen.";
        case 8:
            return "XML";
        case 9:
            return "Ein Update wurde erkannt bitte prüfen";
        case 10:
            return "Zeilen der Datei sind nicht kompatibel.";
        case 11:
            return "Bestand wurde bereits geändert bitte neu einlesen.";
        case 12:
            return "Weisung";
        case 13:
            return "Einlesen erfolgreich.";
        case 14:
            return "Adresse nicht vorhanden";
        case 15:
            return "Adresseingabe abgeschlossen - bitte neu einlesen";
        case 16:
            return "Leere Felder bei den Weisungen erkannt, bitte Datei prüfen.";
        case 17:
            return "Fehler bei Clearstream Import.";
        default:
            return "Fehlercode nicht abgefangen, Code: " + key;
        }
    }

    /**
     * On close.
     *
     * @param event the event
     */
    @FXML
    private void onClose(ActionEvent event) {
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
