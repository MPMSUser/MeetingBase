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

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import de.meetingapps.meetingclient.meetingClientDialoge.CtrlRoot;
import de.meetingapps.meetingportal.meetComBl.BlPersonenprognose;
import de.meetingapps.meetingportal.meetComDb.DbBundle;
import de.meetingapps.meetingportal.meetComEntities.EclPersonenprognose;
import de.meetingapps.meetingportal.meetComHVParam.ParamS;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.util.converter.DoubleStringConverter;
import javafx.util.converter.IntegerStringConverter;

/**
 * The Class CtrlPersonenprognose.
 */
public class CtrlPersonenprognose extends CtrlRoot {

    /** The lbl timestamp. */
    @FXML
    private Label lblTimestamp;

    /** The table. */
    @FXML
    private TableView<EclPersonenprognose> table;

    /** The col description. */
    @FXML
    private TableColumn<EclPersonenprognose, String> colDescription;

    /** The col distance. */
    @FXML
    private TableColumn<EclPersonenprognose, Integer> colDistance;

    /** The col percent. */
    @FXML
    private TableColumn<EclPersonenprognose, Double> colPercent;

    /** The col max. */
    @FXML
    private TableColumn<EclPersonenprognose, Integer> colMax;

    /** The col forecast. */
    @FXML
    private TableColumn<EclPersonenprognose, Integer> colForecast;

    /** The col real percent. */
    @FXML
    private TableColumn<EclPersonenprognose, Double> colRealPercent;

    /** The col real count. */
    @FXML
    private TableColumn<EclPersonenprognose, Integer> colRealCount;

    /** The tf capital. */
    @FXML
    private TextField tfCapital;

    /** The tf shares. */
    @FXML
    private TextField tfShares;

    /** The lbl prediction. */
    @FXML
    private Label lblPrediction;

    /** The tf prediction. */
    @FXML
    private TextField tfPrediction;

    /** The lbl present. */
    @FXML
    private Label lblPresent;

    /** The tf present. */
    @FXML
    private TextField tfPresent;

    /** The tf erg cards. */
    @FXML
    private TextField tfErgCards;

    /** The tf reg shareholder. */
    @FXML
    private TextField tfRegShareholder;

    /** The tf reg shares. */
    @FXML
    private TextField tfRegShares;

    /** The tf reg capital. */
    @FXML
    private TextField tfRegCapital;

    /** The tf reg capital percent. */
    @FXML
    private TextField tfRegCapitalPercent;

    /** The btn close. */
    @FXML
    private Button btnClose;

    /** The btn refresh. */
    @FXML
    private Button btnRefresh;

    /** The list pp. */
    private List<EclPersonenprognose> listPp = null;

    /** The distances. */
    private List<Double> distances = null;

    /** The list. */
    private ObservableList<EclPersonenprognose> list = null;

    /** The hv ort. */
    final private String hvOrt = ParamS.eclEmittent.hvOrt;

    /** The hv plz. */
    final private String hvPlz = ParamS.eclEmittent.veranstaltungPostleitzahl;

    /** The formatter. */
    final private NumberFormat formatter = NumberFormat.getIntegerInstance();

    /** The prediction. */
    private int prediction = 0;

    /** The eks. */
    private int eks = 0;

    /** The bl personenprognose. */
    private BlPersonenprognose blPersonenprognose;

    /**
     * Initialize.
     */
    @FXML
    void initialize() {

        blPersonenprognose = new BlPersonenprognose(false, new DbBundle());

        if (blPersonenprognose.intialisierePrognose() == 1)
            System.out.println("initialisiere Personenprognose");

        //		Vorhandene Datensätze aus BlPersonenprognose - DbPersonenprognose einlesen
        listPp = blPersonenprognose.listPp;
        if (listPp == null)
            listPp = new ArrayList<>();
        list = FXCollections.observableArrayList(listPp);

        //		Einrichtung TableView Zeile 164 - 215
        colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        colDistance.setCellValueFactory(new PropertyValueFactory<>("distance"));
        colPercent.setCellValueFactory(new PropertyValueFactory<>("percent"));
        colMax.setCellValueFactory(new PropertyValueFactory<>("max"));
        colForecast.setCellValueFactory(new PropertyValueFactory<>("forecast"));
        colRealPercent.setCellValueFactory(new PropertyValueFactory<>("realPercent"));
        colRealCount.setCellValueFactory(new PropertyValueFactory<>("realCount"));

        colDescription.setCellFactory(TextFieldTableCell.<EclPersonenprognose>forTableColumn());
        colDescription.setOnEditCommit((CellEditEvent<EclPersonenprognose, String> event) -> {
            TablePosition<EclPersonenprognose, String> pos = event.getTablePosition();
            EclPersonenprognose data = event.getTableView().getItems().get(pos.getRow());
            if (!event.getNewValue().equals(event.getOldValue()))
                data.setDescription(event.getNewValue());
        });

        colDistance.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter() {
            public Integer fromString(String value) {

                value = value.trim();

                if (value == null || value.length() < 1)
                    return null;
                else if (value.matches("\\d*"))
                    return Integer.valueOf(value);
                else
                    return null;
            }
        }));
        colDistance.setOnEditCommit((CellEditEvent<EclPersonenprognose, Integer> event) -> {
            TablePosition<EclPersonenprognose, Integer> pos = event.getTablePosition();
            EclPersonenprognose data = event.getTableView().getItems().get(pos.getRow());
            if (event.getNewValue() != null) {
                if (!event.getNewValue().equals(event.getOldValue())) {
                    data.setDistance(event.getNewValue());
                    if (event.getNewValue() < 1000)
                        data.setDescription("bis " + event.getNewValue() + " km");
                    columnChange();
                }
            } else {
                data.setDistance(event.getOldValue());
            }
        });

        colPercent.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter() {
            public Double fromString(String value) {

                value = value.trim();

                if (value == null || value.length() < 1)
                    return null;
                else if (value.matches("(\\d{0,2}[.,]?\\d{0,2})"))
                    return Double.valueOf(value.replace(",", "."));
                else
                    return null;
            }
        }));
        colPercent.setOnEditCommit((CellEditEvent<EclPersonenprognose, Double> event) -> {

            TablePosition<EclPersonenprognose, Double> pos = event.getTablePosition();
            EclPersonenprognose data = event.getTableView().getItems().get(pos.getRow());

            if (event.getNewValue() != null) {
                if (!event.getNewValue().equals(event.getOldValue())) {
                    data.setPercent(event.getNewValue());
                    if (data.getMax() == 0 && data.getDistance() != 0) {
                        columnChange();
                    } else if (data.getMax() != 0 && data.getDistance() != 0) {
                        changePercent(data);
                    }
                    tfPrediction.setText(formatter.format(prediction));
                    table.refresh();
                }
            } else {
                data.setPercent(event.getOldValue());
            }
        });

        /*
         * Initialisierung ContextMenu - Add - Delete
         */
        final ContextMenu tableContextMenu = new ContextMenu();
        final MenuItem addMenuItem = new MenuItem("Hinzufügen");

        addMenuItem.setOnAction(e -> table.getItems().add(new EclPersonenprognose()));

        final MenuItem deleteSelectedMenuItem = new MenuItem("Löschen");
        deleteSelectedMenuItem.disableProperty().bind(Bindings.isEmpty(table.getSelectionModel().getSelectedItems()));
        deleteSelectedMenuItem.setOnAction(e -> {
            EclPersonenprognose data = table.getSelectionModel().getSelectedItem();
            list.remove(data);
            if (data.getIdent() != 0)
                blPersonenprognose.deletePrognose(data);
            if (data.getMax() != 0)
                columnChange();
        });

        tableContextMenu.getItems().addAll(addMenuItem, deleteSelectedMenuItem);

        table.setContextMenu(tableContextMenu);

        table.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

        table.getColumns().forEach(e -> e.setStyle("-fx-alignment: CENTER;"));

        /*
         * Fülle TextFields und Labels mit werten aus der Datenbank
         */
        lblTimestamp.setText("Berechnet am: " + EclPersonenprognose.getLocalDateTime(blPersonenprognose.update));

        eks = blPersonenprognose.anzRegMeldung;
        tfErgCards.setText(formatter.format(eks));
        prediction = blPersonenprognose.prediction;
        tfPrediction.setText(formatter.format(prediction));
        tfPresent.setText(formatter.format(blPersonenprognose.presentPersons));

        tfRegShareholder.setText(formatter.format(blPersonenprognose.regShareholder));

        final int shares = blPersonenprognose.shares;
        tfRegShares.setText(formatter.format(shares));

        tfCapital.setText(NumberFormat.getCurrencyInstance().format(ParamS.param.paramBasis.getGrundkapitalEuro(1)));
        tfShares.setText(formatter.format(ParamS.param.paramBasis.getGrundkapitalStueck(1)));
        tfRegCapital.setText(NumberFormat.getCurrencyInstance(Locale.GERMANY)
                .format(shares * ParamS.param.paramBasis.getWertEinerAktie(1)));
        Double num = (double) shares / ParamS.param.paramBasis.getGrundkapitalStueck(1);
        tfRegCapitalPercent.setText(new DecimalFormat("##.## %").format(num));

        table.setItems(list);

        checkPresent();
        eigeneStage.setOnCloseRequest(e -> clickedClose(new ActionEvent()));

    }

    /**
     * Change percent.
     *
     * @param pData the data
     */
    private void changePercent(EclPersonenprognose pData) {

        final int oldForecast = pData.getForecast();
        final int newForecast = (int) (pData.getMax() * (pData.getPercent() / 100));

        prediction = prediction - oldForecast + newForecast;

        pData.setForecast(newForecast);

    }

    /**
     * Column change.
     */
    private void columnChange() {
        if (distances == null) {
            refresh(new ActionEvent());
        } else {
            blPersonenprognose.calculateChange(list, distances);
            list = FXCollections.observableArrayList(blPersonenprognose.listPp);
            prediction = blPersonenprognose.prediction;
            tfPrediction.setText(formatter.format(prediction));
        }

        table.setItems(list);
        table.refresh();
    }

    /**
     * Check present.
     */
    private void checkPresent() {

        if (blPersonenprognose.presentPersons > 0) {
            GridPane.setRowSpan(lblPrediction, 1);
            GridPane.setRowSpan(tfPrediction, 1);

            lblPresent.setVisible(true);
            tfPresent.setVisible(true);
        }
    }

    /**
     * Clicked close.
     *
     * @param event the event
     */
    @FXML
    private void clickedClose(ActionEvent event) {
        blPersonenprognose.save(list);
        eigeneStage.hide();
    }

    /**
     * Refresh.
     *
     * @param event the event
     */
    @FXML
    private void refresh(ActionEvent event) {

        blPersonenprognose.calculation(list, hvOrt, hvPlz);

        list = FXCollections.observableArrayList(blPersonenprognose.listPp);
        distances = blPersonenprognose.distances;
        lblTimestamp.setText(EclPersonenprognose.getLocalDateTime(blPersonenprognose.update));
        prediction = blPersonenprognose.prediction;
        tfPrediction.setText(formatter.format(prediction));
        table.setItems(list);
        table.refresh();

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