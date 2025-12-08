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

import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.kordamp.ikonli.fontawesome5.FontAwesomeSolid;
import org.kordamp.ikonli.javafx.FontIcon;

import de.meetingapps.meetingclient.meetingClientDialoge.CtrlRoot;
import de.meetingapps.meetingclient.meetingClientOberflaechen.LoadingScreen;
import de.meetingapps.meetingclient.meetingClientOberflaechen.ObjectActions;
import de.meetingapps.meetingclient.meetingClientOberflaechen.TableCellLongMitPunkt;
import de.meetingapps.meetingportal.meetComAllg.CaString;
import de.meetingapps.meetingportal.meetComBl.BlMeldungen;
import de.meetingapps.meetingportal.meetComBl.BlSammelkarten;
import de.meetingapps.meetingportal.meetComDb.DbBundle;
import de.meetingapps.meetingportal.meetComEntities.EclMeldung;
import de.meetingapps.meetingportal.meetComHVParam.ParamS;
import de.meetingapps.meetingportal.meetComKonst.KonstSkIst;
import javafx.application.Platform;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.PieChart.Data;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

/**
 * The Class CtrlPraesenzliste.
 */
public class CtrlPraesenzliste extends CtrlRoot {

    public final int width = 1400;
    public final int height = 700;

    @FXML
    private AnchorPane rootPane;

    /** The tf search. */
    @FXML
    private TextField tfSearch;

    /** The btn search. */
    @FXML
    private Button btnRead;

    /** The table view. */
    @FXML
    private TableView<EclMeldung> tableView;

    /** The anr. */
    @FXML
    private TableColumn<EclMeldung, String> anr;

    /** The ek. */
    @FXML
    private TableColumn<EclMeldung, String> ek;

    /** The stimmen. */
    @FXML
    private TableColumn<EclMeldung, String> stimmen;

    /** The aktionaer. */
    @FXML
    private TableColumn<EclMeldung, String> aktionaer;

    /** The a vorname. */
    @FXML
    private TableColumn<EclMeldung, String> a_vorname;

    /** The a name. */
    @FXML
    private TableColumn<EclMeldung, String> a_name;

    /** The vertreter. */
    @FXML
    private TableColumn<EclMeldung, String> vertreter;

    /** The v vorname. */
    @FXML
    private TableColumn<EclMeldung, String> v_vorname;

    /** The v name. */
    @FXML
    private TableColumn<EclMeldung, String> v_name;

    /** The sammelkarte. */
    @FXML
    private TableColumn<EclMeldung, String> sammelkarte;

    /** The status. */
    @FXML
    private TableColumn<EclMeldung, FontIcon> status;

    /** The chart. */
    @FXML
    private PieChart chart;

    /** The stack pane. */
    @FXML
    private StackPane stackPane;

    /** The lbl percent. */
    @FXML
    private Label lblPercent;

    /** The lbl registered. */
    @FXML
    private Label lblRegistered;

    /** The lbl present. */
    @FXML
    private Label lblPresent;

    /** The lbl max present. */
    @FXML
    private Label lblMaxPresent;

    /** The lbl entries. */
    @FXML
    private Label lblEntries;

    /** The btn close. */
    @FXML
    private Button btnClose;

    @FXML
    private Label lblUpdateTime;

    @FXML
    private ToggleButton tbReload;

    /** The bl meldung. */
    private BlMeldungen blMeldung;

    /** The t. */
    private Thread t = new Thread();

    /** The list. */
    private List<EclMeldung> list;

    /** The angemeldete aktien. */
    private int angemeldeteAktien;

    /** The max size. */
    private int maxSize;
    /*MaxSize von was? Nicht sprechend, kein Kommentar*/

    /** The max shares. */
    private long maxShares;

    private StackPane loading;

    private long[] stimmenSammelkarten = { 0, 0, 0, 0, 0 };
    private Map<Integer, String[]> idToVertreter;

    /**
     * Initialize.
     */
    @FXML
    private void initialize() {

        blMeldung = new BlMeldungen(false, new DbBundle());

        anr.setCellValueFactory(new PropertyValueFactory<>("aktionaersnummer"));
        ek.setCellValueFactory(new PropertyValueFactory<>("zutrittsIdent"));
        stimmen.setCellValueFactory(new PropertyValueFactory<>("stimmen"));
        stimmen.setCellFactory(e -> new TableCellLongMitPunkt<>());
        a_vorname.setCellValueFactory(new PropertyValueFactory<>("vorname"));
        a_name.setCellValueFactory(new PropertyValueFactory<>("name"));
        v_vorname.setCellValueFactory(new PropertyValueFactory<>("vertreterVorname"));
        v_name.setCellValueFactory(new PropertyValueFactory<>("vertreterName"));
        sammelkarte.setCellValueFactory(new PropertyValueFactory<>("zusatz1"));
        status.setStyle("-fx-alignment: CENTER");
        status.setCellValueFactory(e -> {
            final Boolean present = e.getValue().statusPraesenz == 1 || e.getValue().statusPraesenz == 4;

            FontIcon icon = present ? new FontIcon(FontAwesomeSolid.SIGN_IN_ALT)
                    : new FontIcon(FontAwesomeSolid.SIGN_OUT_ALT);

            icon.setIconColor(present ? Color.rgb(35, 122, 49) : Color.rgb(135, 43, 32));
            return new SimpleObjectProperty<>(icon);
        });

        chart.setStartAngle(270);
        chart.setClockwise(true);
        chart.setLabelsVisible(false);

        btnClose.setOnAction(e -> eigeneStage.hide());
        tbReload.setGraphic(new FontIcon(FontAwesomeSolid.SYNC_ALT));

        tfSearch.textProperty().addListener((o, oV, nV) -> doSearch());

        ObjectActions.tableResizePolicy(eigeneStage, tableView, 1100);

        loading = LoadingScreen.createLoadingScreen(rootPane);

        new Thread(readTask()).start();

        eigeneStage.setOnHiding((event) -> {
            t.interrupt();
        });
    }

    /**
     * Inits the data.
     */
    private void initData() {

        int kapital = 0;
        long curr = 0;

        for (int i = 1; i <= 5; i++) {
            if (ParamS.param.paramBasis.getGattungAktiv(i)) {
                kapital += ParamS.param.paramBasis.grundkapitalStueck[i - 1];
            }
        }

        Set<String> current = new HashSet<>();
        Set<String> max = new HashSet<>();
        List<Data> chartList = new ArrayList<>();
        long presentVotes = 0;

        if (ParamS.param.paramAkkreditierung.skOffenlegungSRV == 0) {
            chartList.add(
                    new PieChart.Data("SRV", calcPercent(stimmenSammelkarten[KonstSkIst.srv - 1], angemeldeteAktien)));
            presentVotes += stimmenSammelkarten[KonstSkIst.srv - 1];
        }
        if (stimmenSammelkarten[KonstSkIst.briefwahl - 1] > 0) {
            chartList.add(new PieChart.Data("Briefwahl",
                    calcPercent(stimmenSammelkarten[KonstSkIst.briefwahl - 1], angemeldeteAktien)));
            presentVotes += stimmenSammelkarten[KonstSkIst.briefwahl - 1];
        }
        if (ParamS.param.paramAkkreditierung.skOffenlegungKIAV == 0) {
            chartList.add(new PieChart.Data("KIAV",
                    calcPercent(stimmenSammelkarten[KonstSkIst.kiav - 1], angemeldeteAktien)));
            presentVotes += stimmenSammelkarten[KonstSkIst.kiav - 1];
        }
        if (ParamS.param.paramAkkreditierung.skOffenlegungDauer == 0) {
            chartList.add(new PieChart.Data("Dauervollmacht",
                    calcPercent(stimmenSammelkarten[KonstSkIst.dauervollmacht - 1], angemeldeteAktien)));
            presentVotes += stimmenSammelkarten[KonstSkIst.dauervollmacht - 1];
        }
        if (ParamS.param.paramAkkreditierung.skOffenlegungOrga == 0) {
            chartList.add(new PieChart.Data("Organisatorisch",
                    calcPercent(stimmenSammelkarten[KonstSkIst.organisatorisch - 1], angemeldeteAktien)));
            presentVotes += stimmenSammelkarten[KonstSkIst.organisatorisch - 1];
        }

        for (EclMeldung mel : list) {

            String person = "";

            if (mel.meldungEnthaltenInSammelkarte != 0) {
                if (mel.vertreterVorname != null && mel.vertreterName != null && mel.zusatz1.contains("SRV - HV")) {
                    /*Hier wird zusatz1 - Feld zweckentfremdet, ohne dass eine Kommentierung stattfindet*/
                    max.add((mel.vertreterVorname + " " + mel.vertreterName).trim());
                }

                String[] vertreter = idToVertreter.get(mel.meldungEnthaltenInSammelkarte);
                person = vertreter[0] + " " + vertreter[1];

                mel.setVertreterVorname(vertreter[0]);
                mel.setVertreterName(vertreter[1]);
            } else if (mel.vertreterVorname == null && mel.vertreterName == null) {
                person = (mel.vorname + " " + mel.name).trim();
            } else {
                person = (mel.vertreterVorname + " " + mel.vertreterName).trim();
            }

            if ((mel.statusPraesenz == 1 || mel.statusPraesenz == 4)) {

                curr += mel.stimmen;

                max.add(person);
                current.add(person);

                if (mel.zusatz1 != null && mel.zusatz1.contains("SRV - HV")) {
                    current.remove(person);
                }
            } else {
                max.add(person);
            }
        }

        max.addAll(current);

        final DecimalFormat df = new DecimalFormat("0.00");

        double percent = calcPercent(curr, angemeldeteAktien);
        chartList.add(new PieChart.Data("präsent", percent));

        double percentMax = calcPercent(angemeldeteAktien, kapital);
        lblRegistered.setText(df.format(percentMax) + "%");

        double present = 0.0;

        for (Data data : chartList)
            present += data.getPieValue();

        lblPercent.setText(df.format(calcPercent(curr + presentVotes, kapital)) + "%");

        chartList.add(new PieChart.Data("Nicht präsent", 100.0 - present));

        chart.setData(FXCollections.observableArrayList(chartList));

        Map<String, String> map = new LinkedHashMap<>();

        for (Data data : chartList) {
            if (data.getPieValue() > 0.0) {
                final String color = getColor(data.getName());
                data.getNode().setStyle("-fx-pie-color: " + color);
                map.put(color, data.getName());
            }
        }

        ObjectActions.createLegendeButton(rootPane, map, 70.0, 20.0);

        chart.getData().forEach(data -> {
            Tooltip tooltip = new Tooltip();
            tooltip.setText(data.getName());
            Tooltip.install(data.getNode(), tooltip);
        });

        lblPresent.setText("" + current.size());
        lblMaxPresent.setText("" + max.size());

        setEntryLabel(maxSize, maxShares);

    }

    private String getColor(String str) {
        return switch (str) {
        case "SRV" -> "#DA5828";
        case "Briefwahl" -> "#E99F27";
        case "KIAV" -> "#893BB4";
        case "Dauervollmacht" -> "#EC7063";
        case "Organisatorisch" -> "#76d7C4";
        case "präsent" -> "#3B4FB4";
        case "Nicht präsent" -> "#F5F5F5";
        default -> throw new IllegalArgumentException("Unexpected value: " + str);
        };
    }

    private double calcPercent(long curr, int capital) {
        return Double.valueOf(curr) / Double.valueOf(capital) * 100.0;
    }

    /**
     * On search.
     *
     * @param event the event
     */
    private void doSearch() {
        if (tfSearch.getText() != null) {
            List<EclMeldung> tmpList = list.stream()
                    .filter(d -> d.searchString().toLowerCase().contains(tfSearch.getText().toLowerCase()))
                    .collect(Collectors.toList());
            long tmpShares = tmpList.stream().mapToLong(x -> x.getStimmen()).sum();

            tableView.getItems().setAll(tmpList);
            setEntryLabel(tmpList.size(), tmpShares);
        }
    }

    private void loadList() {
        BlSammelkarten sam = new BlSammelkarten(false, new DbBundle());

        sam.holeSammelkartenDaten(true, 0);

        // Map für MeldungsIdent > MeldungsIdent - Name Sammelkarte
        Map<Integer, String> map = new HashMap<>();
        /*map hat weder einen sprechenden Namen, noch gibts einen Kommentar dazu*/

        Arrays.fill(stimmenSammelkarten, 0);
        idToVertreter = new HashMap<>();

        if (sam.rcSammelMeldung != null) {

            for (EclMeldung mel : sam.rcSammelMeldung) {
                if (mel.stimmen != 0 || mel.stueckAktien != 0) {
                    // Hab kein Kennzeichen für eine SRV Sammelkarte gefunden
                    final Boolean srv = mel.name.toLowerCase().contains("stimmrechtsvertreter");
                    map.put(mel.meldungsIdent,
                            (mel.meldungsIdent + " - " + (srv ? "SRV" : mel.name) + " - " + mel.zusatzfeld2).trim());
                    /*Später wird abgefragt auf SRV - HV. D.h. wenn in der HV-Sammelkarte das verändert wurde, funktioniert Programm nicht mehr
                     * korrektur -> Wenn Stimmrechtsvertreter nicht im Namen steht*/

                    final String[] vertreter = { mel.vertreterVorname, mel.vertreterName };
                    idToVertreter.put(mel.meldungsIdent, vertreter);

                    if (mel.statusPraesenz == 1 || mel.skIst == KonstSkIst.briefwahl)
                        /*Verständnisfrage: Die Meldung ist präsent, und ist gleichzeitig eine Sammelkarte Briefwahl?
                         * Dürfte eigentlich nicht vorkommen? Ist kein und sondern ein oder
                         */
                        stimmenSammelkarten[mel.skIst - 1] += mel.stueckAktien;
                }
            }
        }

        blMeldung.readPraesenz();
        list = blMeldung.praesenzliste;
        list.forEach(e -> e.zusatz1 = map.get(e.meldungEnthaltenInSammelkarte));

        angemeldeteAktien = blMeldung.angemeldeteAktien;

        maxSize = list.size();
        maxShares = list.stream().mapToLong(x -> x.getStimmen()).sum();
    }

    /**
     * Read task.
     *
     * @return the task
     */
    private Task<Void> readTask() {

        Task<Void> task = new Task<Void>() {

            @Override
            protected Void call() throws Exception {
                loadList();
                setTime();
                return null;
            }
        };

        task.setOnScheduled(e -> {
            tfSearch.clear();
            t = ObjectActions.disableButton(btnRead, 1000);
            t.start();
            loading.setVisible(true);
        });

        task.setOnSucceeded(e -> {
            loading.setVisible(false);
            if (list != null) {
                tableView.getItems().setAll(list);
                initData();
            }
        });

        task.setOnFailed(e -> {
            loading.setVisible(false);
            task.getException().printStackTrace(System.err);
        });

        return task;
    }

    @FXML
    private void onRead(ActionEvent event) {
        new Thread(readTask()).start();
    }

    @FXML
    private void onReload(ActionEvent event) {

        if (tbReload.isSelected()) {
            t = new Thread(refreshTask());
            t.start();
        } else {
            tfSearch.setDisable(false);
            btnRead.setDisable(false);
            tableView.getColumns().forEach(x -> x.setSortable(true));
        }
    }

    private Task<Void> refreshTask() {

        Task<Void> task = new Task<Void>() {

            @Override
            protected Void call() throws Exception {
                loading.setVisible(true);
                loadList();
                tableView.getItems().setAll(list);
                Platform.runLater(() -> initData());
                loading.setVisible(false);
                setTime();
                Thread.sleep(15000);
                return null;
            }
        };

        task.setOnScheduled(e -> {
            tfSearch.setDisable(true);
            btnRead.setDisable(true);
            tableView.getColumns().forEach(x -> x.setSortable(false));
        });

        task.setOnSucceeded(e -> {
            tfSearch.setDisable(false);
            btnRead.setDisable(false);
            tableView.getColumns().forEach(x -> x.setSortable(true));
            if (tbReload.isSelected()) {
                t = new Thread(refreshTask());
                t.start();
            }
        });
        return task;
    }

    private void setTime() {
        Platform.runLater(() -> lblUpdateTime
                .setText("Stand: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")) + " Uhr"));
    }

    /**
     * Sets the entry label.
     *
     * @param search the new entry label
     */
    private void setEntryLabel(int search, long aktien) {

        final String entry = (search == maxSize) ? "Einträge: " + maxSize : "Einträge: " + search + " / " + maxSize;
        final String shares = (aktien == maxShares) ? "Aktien: " + CaString.toStringDE(maxShares)
                : "Aktien: " + CaString.toStringDE(aktien) + " / " + CaString.toStringDE(maxShares);

        Platform.runLater(() -> lblEntries.setText(entry + " - " + shares));
    }

    /**
     * Inits the.
     *
     * @param pEigeneStage the eigene stage
     * @param pFunktion    the funktion
     */
    public void init(Stage pEigeneStage, int pFunktion) {
        eigeneStage = pEigeneStage;
    }
}
