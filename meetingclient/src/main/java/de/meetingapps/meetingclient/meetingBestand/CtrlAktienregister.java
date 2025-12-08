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

import java.lang.reflect.Field;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.kordamp.ikonli.fontawesome5.FontAwesomeSolid;
import org.kordamp.ikonli.javafx.FontIcon;

import de.meetingapps.meetingclient.meetingClientDialoge.CaController;
import de.meetingapps.meetingclient.meetingClientDialoge.CaIcon;
import de.meetingapps.meetingclient.meetingClientDialoge.CtrlRoot;
import de.meetingapps.meetingclient.meetingClientOberflaechen.LoadingScreen;
import de.meetingapps.meetingclient.meetingClientOberflaechen.ObjectActions;
import de.meetingapps.meetingclient.meetingClientOberflaechen.TableCellLongMitPunkt;
import de.meetingapps.meetingportal.meetComBl.BlAktienregister;
import de.meetingapps.meetingportal.meetComDb.DbBundle;
import de.meetingapps.meetingportal.meetComEntities.EclAktienregister;
import de.meetingapps.meetingportal.meetComHVParam.ParamS;
import javafx.application.Platform;
import javafx.beans.Observable;
import javafx.beans.property.SimpleObjectProperty;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/**
 * The Class CtrlAktienregister.
 */
public class CtrlAktienregister extends CtrlRoot {

    public final int width = 1400;
    public final int height = 750;

    @FXML
    private AnchorPane rootPane;

    /** The tf search. */
    @FXML
    private TextField tfSearch;

    /** The btn search. */
    @FXML
    private Button btnSearch;

    /** The cb angemeldet. */
    @FXML
    private CheckBox cbAngemeldet;

    /** The table view. */
    @FXML
    private TableView<EclAktienregister> tableView;

    /** The btn close. */
    @FXML
    private Button btnClose;

    /** The btn table. */
    @FXML
    private Button btnTable;

    /** The btn back. */
    @FXML
    private Button btnBack;

    /** The lbl page. */
    @FXML
    private Label lblPage;

    /** The btn next. */
    @FXML
    private Button btnNext;

    /** The lbl aktien. */
    @FXML
    private Label lblAktien;

    private StackPane loading;

    /** The bl reg. */
    private BlAktienregister blReg;

    /** The columns. */
    private Set<String> columns = new LinkedHashSet<>(List.of("aktionaersnummer", "vorname", "nachname", "strasse",
            "postleitzahl", "ort", "stueckAktien", "meldungAktiv", "inSammelkarte"));

    /** The from meldung. */
    private Set<String> fromMeldung = Set.of("meldungaktiv", "zutrittsident", "insammelkarte", "sammelkarte",
            "statuspraesenz", "meldungsident");

    /** The disabled columns. */
    private final Set<String> disabledColumns = Set.of("serialVersionUID", "englischerVersand", "datenUpdate",
            "kennzeichen", "AnrComparator", "AnrStringComparator", "email", "emailVersand");

    /** The eintrag. */
    public EclAktienregister eintrag;

    /** The limit. */
    private final int limit = 1000;

    /** The page size. */
    private final int pageSize = 500;

    /** The offset max. */
    private int offsetMax = limit;

    /** The offset. */
    private int offset;

    /** The count. */
    private int count;

    /** The page. */
    private int page = 1;

    /** The last page. */
    private int lastPage;

    /** The list. */
    private List<EclAktienregister> list;

    /** The zusatz. */
    private String zusatz = "";

    /** The sort typ. */
    private String sortTyp = "are.aktionaersnummer * 1";

    /** The sort order. */
    private String sortOrder = " ASC";

    /** The funktion. */
    private int funktion;

    /** The t. */
    private Thread t = new Thread();

    /**
     * Initialize.
     */
    @FXML
    private void initialize() {

        tableView.getSortOrder().addListener((Observable o) -> {

            String tmpSortTyp = tableView.getSortOrder().stream().map(TableColumn::getText)
                    .collect(Collectors.joining(", ", "", "")).toLowerCase();

            tmpSortTyp = tmpSortTyp.equals("ek-nummer") ? "zutrittsident" : tmpSortTyp;

            if (!tmpSortTyp.isBlank()) {
                sortTyp = (fromMeldung.contains(tmpSortTyp) ? "m." : "are.") + tmpSortTyp;
                sortTyp += sortTyp.contains("aktionaersnummer") ? "*1" : "";
            }
        });

        tableView.setOnSort(e -> {
            new Thread(searchTask()).start();
        });

        btnBack.setGraphic(new FontIcon(FontAwesomeSolid.ANGLE_DOUBLE_LEFT));
        btnNext.setGraphic(new FontIcon(FontAwesomeSolid.ANGLE_DOUBLE_RIGHT));
        btnTable.setGraphic(new FontIcon(FontAwesomeSolid.TABLE));

        if (ParamS.param.paramBasis.namensaktienAktiv) {
            columns.add("zutrittsIdent");
        }
        configureTable();

        btnTable.setOnAction(e -> showAlertConfig());

        blReg = new BlAktienregister(false, new DbBundle());

        onSearch(new ActionEvent());

        btnClose.setOnAction(e -> eigeneStage.hide());

        tableView.setRowFactory(tv -> {
            final TableRow<EclAktienregister> row = new TableRow<>();
            row.setOnMouseClicked(x -> {
                if (x.getClickCount() == 2 && !row.isEmpty()) {
                    if (funktion == 1) {
                        eintrag = row.getItem();
                        eigeneStage.hide();
                    } else if (funktion == 2) {
                        Stage newStage = new Stage();
                        CaIcon.bestandsverwaltung(newStage);

                        CtrlStatus controllerStatus = new CtrlStatus();
                        controllerStatus.init(newStage, 0);
                        Platform.runLater(() -> {
                            controllerStatus.getTfAktionaersnummer().setText(row.getItem().aktionaersnummer);
                            controllerStatus.doEinlesen();
                        });
                        CaController caController = new CaController();
                        caController.open(newStage, controllerStatus,
                                "/de/meetingapps/meetingclient/meetingBestand/Status.fxml", 1150, 760,
                                "Einzelfallbearbeitung", true);
                    }
                }
            });
            return row;
        });

        ObjectActions.tableResizePolicy(eigeneStage, tableView, 1100);
        loading = LoadingScreen.createLoadingScreen(rootPane);
        buildContextMenu();
    }

    /**
     * On enter.
     *
     * @param event the event
     */
    @FXML
    private void onEnter(KeyEvent event) {
        if (!t.isAlive()) {
            if (event.getCode() == KeyCode.ENTER)
                btnSearch.fire();
        }
    }

    /**
     * On search.
     *
     * @param event the event
     */
    @FXML
    private void onSearch(ActionEvent event) {
        new Thread(searchTask()).start();
    }

    /**
     * Search task.
     *
     * @return the task
     */
    private Task<Void> searchTask() {

        Task<Void> task = new Task<Void>() {

            @Override
            protected Void call() throws Exception {

                offset = 0;
                offsetMax = limit;
                page = 1;
                btnBack.setDisable(true);

                zusatz = createZusatz(tfSearch.getText());
                if (blReg.searchRegister(zusatz, (sortTyp + sortOrder), offset, limit) == 1) {
                    count = blReg.count;
                    lastPage = (count + pageSize - 1) / pageSize;
                    list = blReg.registerListe;
                }

                Platform.runLater(() -> {
                    lblAktien.setText("Einträge: " + String.valueOf(NumberFormat.getInstance().format(count)));
                    setPage(count == 0 ? 0 : page);
                    System.out.println(offset + pageSize + " > " + count);
                    btnNext.setDisable(pageSize > count);
                });
                return null;
            }
        };

        task.setOnScheduled(e -> {
            t = ObjectActions.disableButton(btnSearch, 1000);
            t.start();
            loading.setVisible(true);
        });
        task.setOnSucceeded(e -> {
            loading.setVisible(false);
            if (list != null) {
                tableView.getItems().setAll(list.subList(0, pageSize > count ? count : pageSize));
                tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
            }
        });
        task.setOnFailed(e -> loading.setVisible(false));

        return task;
    }

    /**
     * On back.
     *
     * @param event the event
     */
    @FXML
    private void onBack(ActionEvent event) {

        offset -= pageSize;

        System.out.println("Anzeige " + offset + " bis " + (offset + pageSize));

        setPage(--page);
        tableView.getItems().setAll(list.subList(offset, offset + pageSize));
        Platform.runLater(() -> tableView.scrollTo(1));

        btnBack.setDisable(offset < pageSize);
        btnNext.setDisable(offset + pageSize > count);

        System.out.println();
        System.out.println();
    }

    /**
     * On next.
     *
     * @param event the event
     */
    @FXML
    private void onNext(ActionEvent event) {
        new Thread(nextTask()).start();
    }

    private Task<Void> nextTask() {

        Task<Void> task = new Task<Void>() {

            @Override
            protected Void call() throws Exception {

                final long timeStart = System.currentTimeMillis();
                offset += pageSize;

                System.out.println("offsetMax " + offsetMax + " / offset " + offset);

                if (offsetMax == offset) {
                    if (blReg.searchRegister(zusatz, (sortTyp + sortOrder), offset, limit) == 1) {
                        list.addAll(blReg.registerListe);
                    }
                    offsetMax += limit;
                }

                System.out.println("liste size " + list.size());

                System.out.println(
                        "Anzeige " + offset + " bis " + ((offset + pageSize) > count ? count : (offset + pageSize)));

                btnBack.setDisable(offset < pageSize);
                btnNext.setDisable(offset + pageSize > count);

                tableView.getItems()
                        .setAll(list.subList(offset, (offset + pageSize) > count ? count : (offset + pageSize)));
                Platform.runLater(() -> {
                    tableView.scrollTo(0);
                    setPage(++page);
                });

                final long timeEnd = System.currentTimeMillis();
                System.out.println("Duration: " + (timeEnd - timeStart));
                System.out.println();
                System.out.println();
                return null;
            }
        };

        task.setOnScheduled(e -> {
            loading.setVisible(true);
        });
        task.setOnSucceeded(e -> {
            loading.setVisible(false);
        });
        task.setOnFailed(e -> loading.setVisible(false));

        return task;
    }

    /**
     * Sets the page.
     *
     * @param page the new page
     */
    private void setPage(int page) {
        lblPage.setText(page + " von " + lastPage);
    }

    private void buildContextMenu() {

        final MenuItem item1 = new MenuItem("Stimmkarte Drucken");
        item1.setOnAction(e -> {

            final EclAktienregister obj = tableView.getSelectionModel().getSelectedItem();

            String ek = obj.getZutrittsIdent();

            if (ek.contains("-")) {

                String[] eks = ek.split(" - ");

                if (eks.length > 1) {

                    Alert alert = new Alert(AlertType.CONFIRMATION);
                    alert.setTitle("Stimmkarte drucken");
                    alert.setHeaderText("Welche Stimmkarte möchten Sie drucken?");
                    alert.setContentText("Bitte auswählen.");
                    alert.getButtonTypes().clear();

                    Map<ButtonType, String> map = new LinkedHashMap<>();

                    for (String str : eks) {
                        if (!str.isBlank()) {
                            ButtonType type = new ButtonType(str);
                            alert.getButtonTypes().add(type);
                            map.put(type, str);
                        }
                    }

                    alert.getButtonTypes().add(new ButtonType("Cancel", ButtonData.CANCEL_CLOSE));
                    Optional<ButtonType> result = alert.showAndWait();
                    if (result.isPresent()) {
                        ek = map.get(result.get());
                    }
                }
            }

            if (ek != null && !ek.isBlank()) {
                CtrlStimmkartenDrucken print = new CtrlStimmkartenDrucken();
                print.gewaehltGattung = obj.getGattungId();
                print.gewaehltStimmkarte = 1;
                print.drucken(ek);
            }
        });

        tableView.setContextMenu(new ContextMenu(item1));

        tableView.setOnContextMenuRequested(e -> {
            final EclAktienregister eintrag = tableView.getSelectionModel().getSelectedItem();
            final Boolean disabled = eintrag.getZutrittsIdent().isBlank() && !eintrag.inSammelkarte.equals("1");
            item1.setDisable(disabled);
        });
    }

    /**
     * Configure table.
     */
    private void configureTable() {

        tableView.getColumns().clear();

        for (String str : columns) {

            if (str.equals("meldungAktiv") || str.equals("inSammelkarte") || str.equals("statusPraesenz")) {

                TableColumn<EclAktienregister, FontIcon> iconCol = new TableColumn<>(str.toUpperCase(Locale.GERMANY));

                switch (str) {
                case "meldungAktiv":
                    iconCol.setText("ANGEMELDET");
                    iconCol.setCellValueFactory(e -> {
                        EclAktienregister tmpAktienregister = e.getValue();
                        return tmpAktienregister.meldungAktiv.equals("1")
                                ? new SimpleObjectProperty<>(new FontIcon(FontAwesomeSolid.CHECK))
                                : null;
                    });
                    break;
                case "inSammelkarte":
                    iconCol.setCellValueFactory(e -> {
                        EclAktienregister tmpAktienregister = e.getValue();
                        return tmpAktienregister.inSammelkarte.equals("1")
                                ? new SimpleObjectProperty<>(new FontIcon(FontAwesomeSolid.CHECK))
                                : null;
                    });
                    break;
                case "statusPraesenz":
                    iconCol.setCellValueFactory(e -> {
                        EclAktienregister tmpAktienregister = e.getValue();
                        return tmpAktienregister.statusPraesenz.equals("1")
                                ? new SimpleObjectProperty<>(new FontIcon(FontAwesomeSolid.CHECK))
                                : null;

                    });
                    break;
                default:
                    break;
                }
                iconCol.setSortable(false);
                iconCol.setMinWidth(100);
                iconCol.setStyle("-fx-alignment: CENTER");
                tableView.getColumns().add(iconCol);
            } else {
                TableColumn<EclAktienregister, ?> col = new TableColumn<>(str.toUpperCase(Locale.GERMANY));

                col.setCellValueFactory(new PropertyValueFactory<>(str));
                if (str.equals("stueckAktien")) {
                    col.setCellFactory(e -> new TableCellLongMitPunkt<>());
                }

                col.sortTypeProperty().addListener(o -> {
                    sortOrder = col.getSortType().name().equals("DESCENDING") ? " DESC" : " ASC";
                });
                col.setMinWidth(100);
                col.setSortable(!str.equals("meldungsIdent"));

                if (str.equals("zutrittsIdent") && tableView.getColumns().size() > 0) {
                    col.setText("EK-Nummer");
                    tableView.getColumns().add(1, col);
                } else {
                    tableView.getColumns().add(col);
                }
            }
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

        Field[] fields = EclAktienregister.class.getDeclaredFields();

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
        alert.getButtonTypes().setAll(new ButtonType("Abbrechen", ButtonData.CANCEL_CLOSE), btnSave);

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
     * Creates the zusatz.
     *
     * @param string the string
     * @return the string
     */
    private String createZusatz(String string) {

        String search = cbAngemeldet.isSelected() ? " AND are.stueckAktien > 0 AND are.personNatJur > 0" : "";
        // Zahl
        if (!string.isBlank()) {
            if (string.matches("\\d+")) {
                search += " AND (are.aktionaersnummer like '%" + string + "%' OR are.stueckAktien like'%" + string
                        + "%')";

                // MODIFIER + Zahl
            } else if (string.matches("([<>=])(\\s*)(\\d+)")) {
                search += " AND are.stueckAktien" + string.split(" ")[0] + string.split(" ")[1];

                // Text
            } else {
                search += " AND concat(';', ';', are.aktionaersnummer, ';', are.adresszeile1, ';', are.adresszeile2, ';', are.adresszeile3, ';', are.adresszeile4, ';', are.adresszeile5, ';', are."
                        + "adresszeile6, ';', are.nachname, ';', are.vorname, ';', are.name1, ';', are.name2, ';', are.name3, ';', are.ort, ';', are.strasse) LIKE '%"
                        + string + "%'";
            }
        }
        return search;
    }

    /**
     * Inits the.
     *
     * @param pEigeneStage the eigene stage
     * @param pFunktion    the funktion
     */
    public void init(Stage pEigeneStage, int pFunktion) {
        eigeneStage = pEigeneStage;
        funktion = pFunktion;
    }

}
