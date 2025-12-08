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

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.kordamp.ikonli.fontawesome5.FontAwesomeSolid;
import org.kordamp.ikonli.javafx.FontIcon;

import de.meetingapps.meetingportal.meetComDb.DbBundle;
import de.meetingapps.meetingportal.meetComEntities.EclEmittenten;
import de.meetingapps.meetingportal.meetComStub.StubCtrlLogin;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

/**
 * The Class CtrlEmittentenUebersicht.
 */
public class CtrlEmittentenUebersicht extends CtrlRoot {

    /** The tf suche. */
    @FXML
    private TextField tfSuche;

    /** The table emittenten. */
    @FXML
    private TableView<EclEmittenten> tableEmittenten;

    /** The check aktuell. */
    @FXML
    private CheckBox checkAktuell;

    /** The list. */
    private List<EclEmittenten> list;

    /** The l. */
    private StubCtrlLogin l;

    /** The today. */
    private int today = 0;

    /**
     * Initialize.
     */
    @FXML
    private void initialize() {

        for (var entry : tableColumns().entrySet()) {
            if (entry.getValue().equals("Dauerhaft"))
                iconColumn(entry);
            else
                stringColumn(entry);
        }

        today = Integer.parseInt(LocalDate.now().format(DateTimeFormatter.BASIC_ISO_DATE));

        l = new StubCtrlLogin(false, new DbBundle());
        l.liefereMandantenArray();

        list = Arrays.asList(l.rcMandantenArray);

        list.sort((o1, o2) -> sortableHvDatum(o1.hvDatum).compareTo(sortableHvDatum(o2.hvDatum)));

        tableEmittenten.getItems().addAll(list);

        checkAktuell.setSelected(true);
        onAktuell(new ActionEvent());

    }

    /**
     * String column.
     *
     * @param entry the entry
     */
    private void stringColumn(Entry<String, String> entry) {

        TableColumn<EclEmittenten, String> col = new TableColumn<>(entry.getValue());
        col.setCellValueFactory(new PropertyValueFactory<>(entry.getKey()));

        if (entry.getKey().equals("hvDatum"))
            col.setComparator((o1, o2) -> sortableHvDatum(o1).compareTo(sortableHvDatum(o2)));

        col.setMinWidth(100);
        tableEmittenten.getColumns().add(col);

    }

    /**
     * Icon column.
     *
     * @param entry the entry
     */
    private void iconColumn(Entry<String, String> entry) {

        TableColumn<EclEmittenten, FontIcon> col = new TableColumn<>(entry.getValue());

        col.setCellValueFactory(e -> {
            EclEmittenten emittent = e.getValue();
            return emittent.getPortalIstDauerhaft() == 1
                    ? new SimpleObjectProperty<>(new FontIcon(FontAwesomeSolid.CHECK))
                    : null;
        });
        col.setStyle("-fx-alignment: CENTER");
        col.setMinWidth(100);
        tableEmittenten.getColumns().add(col);
    }

    /**
     * On enter.
     *
     * @param event the event
     */
    @FXML
    private void onEnter(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER)
            onSuche(new ActionEvent());
    }

    /**
     * On suche.
     *
     * @param event the event
     */
    @FXML
    private void onSuche(ActionEvent event) {

        if (tfSuche.getText() != null) {
            List<EclEmittenten> tmpList = list.stream()
                    .filter(d -> d.searchString().toLowerCase().contains(tfSuche.getText().toLowerCase()))
                    .collect(Collectors.toList());
            tableEmittenten.getItems().setAll(tmpList);
        }
    }

    /**
     * On beenden.
     *
     * @param event the event
     */
    @FXML
    private void onBeenden(ActionEvent event) {
        eigeneStage.hide();
    }

    /**
     * On aktuell.
     *
     * @param event the event
     */
    @FXML
    private void onAktuell(ActionEvent event) {
        tfSuche.clear();

        if (checkAktuell.isSelected())
            list = tableEmittenten.getItems().stream().filter(isAll()).collect(Collectors.toList());
        else
            list = Arrays.asList(l.rcMandantenArray);

        tableEmittenten.getItems().setAll(list);
        tfSuche.requestFocus();
    }

    /**
     * Checks if is all.
     *
     * @return the predicate
     */
    /*
     * Filtern der Uebersicht
     * Mandanten > 900 Interne Mandanten
     * TODO Sobald verfügbar ändern auf Parameter "Ist Intern/Test-Mandant"
     * Vergangene HVen
     */
    private Predicate<EclEmittenten> isAll() {
        return p -> !(p.getMandant() > 899 || Integer.parseInt(sortableHvDatum(p.hvDatum)) < today);
    }

    /**
     * Sortable hv datum.
     *
     * @param hvDatum the hv datum
     * @return the string
     */
    public String sortableHvDatum(String hvDatum) {
        return hvDatum.length() < 10 ? String.valueOf(today)
                : hvDatum.substring(6, 10) + hvDatum.substring(3, 5) + hvDatum.substring(0, 2);
    }

    /**
     * Table columns.
     *
     * @return the map
     */
    public Map<String, String> tableColumns() {

        Map<String, String> map = new LinkedHashMap<>();

        map.put("mandant", "Mandant");
        map.put("bezeichnungKurz", "Kunde");
        map.put("hvDatum", "HV-Datum");
        map.put("hvOrt", "HV-Ort");
        map.put("portalIstDauerhaft", "Dauerhaft");
        map.put("datenbestandIstProduktiv", "Datenbestand");

        return map;
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
