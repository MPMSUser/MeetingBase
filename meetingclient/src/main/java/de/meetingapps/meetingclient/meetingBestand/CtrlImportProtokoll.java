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
import java.util.List;
import java.util.stream.Collectors;

import de.meetingapps.meetingclient.meetingClientDialoge.CtrlRoot;
import de.meetingapps.meetingportal.meetComBl.BlInhaberImport;
import de.meetingapps.meetingportal.meetComDb.DbBundle;
import de.meetingapps.meetingportal.meetComEntities.EclImportProtokoll;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

/**
 * The Class CtrlInhaberImportProtokoll.
 */
public class CtrlImportProtokoll extends CtrlRoot {
    
    final public int width = 720;
    final public int height = 400;

    /** The tf search. */
    @FXML
    private TextField tfSearch;

    /** The table view. */
    @FXML
    private TableView<EclImportProtokoll> tableView;

    /** The ident. */
    @FXML
    private TableColumn<EclImportProtokoll, Integer> ident;

    /** The ident User. */
    @FXML
    private TableColumn<EclImportProtokoll, Integer> identUser;

    /** The ek Von. */
    @FXML
    private TableColumn<EclImportProtokoll, Integer> ekVon;

    /** The ek Bis. */
    @FXML
    private TableColumn<EclImportProtokoll, Integer> ekBis;

    /** The dateiname. */
    @FXML
    private TableColumn<EclImportProtokoll, String> dateiname;

    /** The md5. */
    @FXML
    private TableColumn<EclImportProtokoll, String> md5;

    /** The erstellt. */
    @FXML
    private TableColumn<EclImportProtokoll, Timestamp> erstellt;

    /** The btn Close. */
    @FXML
    private Button btnClose;

    /** The lbl entries. */
    @FXML
    private Label lblEntries;

    /** The list. */
    private List<EclImportProtokoll> list;

    /**
     * Initialize.
     */
    @FXML
    private void initialize() {

        ident.setCellValueFactory(new PropertyValueFactory<>("ident"));
        identUser.setCellValueFactory(new PropertyValueFactory<>("userLoginIdent"));
        ekVon.setCellValueFactory(new PropertyValueFactory<>("ekVon"));
        ekBis.setCellValueFactory(new PropertyValueFactory<>("ekBis"));
        dateiname.setCellValueFactory(new PropertyValueFactory<>("name"));
        md5.setCellValueFactory(new PropertyValueFactory<>("datei"));
        erstellt.setCellValueFactory(new PropertyValueFactory<>("update"));

        tfSearch.textProperty().addListener((o, oV, nV) -> {
            onSearch();
        });

        btnClose.setOnAction(e -> eigeneStage.hide());

        initData();
    }

    /**
     * On search.
     */
    private void onSearch() {
        if (tfSearch.getText() != null) {
            List<EclImportProtokoll> tmpList = list.stream()
                    .filter(d -> d.searchString().toLowerCase().contains(tfSearch.getText().toLowerCase()))
                    .collect(Collectors.toList());
            tableView.getItems().setAll(tmpList);
            setEntryLabel(tmpList.size(), list.size());
        }
    }

    /**
     * Sets the entry label.
     *
     * @param search the new entry label
     * @param search the new entry label
     */
    private void setEntryLabel(int tmpSize, int maxSize) {
        lblEntries.setText((tmpSize == maxSize) ? "Einträge: " + maxSize : "Einträge: " + tmpSize + " / " + maxSize);
    }

    private void initData() {
        final BlInhaberImport blInhaberImport = new BlInhaberImport(false, new DbBundle());
        blInhaberImport.readProtokoll();
        list = blInhaberImport.importProtokoll;
        tableView.getItems().setAll(list);
        setEntryLabel(list.size(), list.size());
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
