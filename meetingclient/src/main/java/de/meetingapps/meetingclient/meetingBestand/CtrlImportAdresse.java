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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import de.meetingapps.meetingclient.meetingClientDialoge.CtrlRoot;
import de.meetingapps.meetingclient.meetingClientOberflaechen.ComboBoxZusatz;
import de.meetingapps.meetingportal.meetComDb.DbBundle;
import de.meetingapps.meetingportal.meetComEntities.EclInhaberImportAnmeldedaten;
import de.meetingapps.meetingportal.meetComEntities.EclImportAdresse;
import de.meetingapps.meetingportal.meetComEntities.EclStaaten;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * The Class CtrlImportAdresse.
 */
public class CtrlImportAdresse extends CtrlRoot {

    /** The tf adresse. */
    @FXML
    private TextField tfAdresse;

    /** The tf strasse. */
    @FXML
    private TextField tfStrasse;

    /** The tf zusatz. */
    @FXML
    private TextField tfZusatz;

    /** The tf plz. */
    @FXML
    private TextField tfPlz;

    /** The tf ort. */
    @FXML
    private TextField tfOrt;

    /** The cb land. */
    @FXML
    private ComboBox<EclStaaten> cbLand;

    /** The anm list. */
    private List<EclInhaberImportAnmeldedaten> newAddress;

    private List<String> newAddressString;

    /** The staaten. */
    private List<EclStaaten> staaten;

    /** The idx. */
    int idx = 0;

    /**
     * Initialize.
     */
    @FXML
    void initialize() {

        DbBundle lDbBundle = new DbBundle();
        lDbBundle.openAll();
        lDbBundle.openWeitere();
        lDbBundle.dbStaaten.readAll(1);

        staaten = lDbBundle.dbStaaten.ergebnis();

        lDbBundle.closeAll();

        ComboBoxZusatz.configureStaatenBox(cbLand, staaten);
        cbLand.setValue(cbLand.getItems().get(1));

        clearDoubleValues();

        next();

    }

    private void clearDoubleValues() {
        Set<String> address = new HashSet<>();

        for (EclInhaberImportAnmeldedaten value : newAddress) {
            address.add(value.getAdresse().replaceAll("\\s+", " ").trim());
        }

        newAddressString = new ArrayList<>(address);

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
     * On save.
     *
     * @param event the event
     */
    @FXML
    private void onSave(ActionEvent event) {

        if (!tfOrt.getText().isBlank()) {

            DbBundle lDbBundle = new DbBundle();
            lDbBundle.openAll();

            lDbBundle.dbImportAdresse.insert(getValues());

            lDbBundle.closeAll();

            next();

        }
    }

    /**
     * Next.
     *
     * @param index the index
     */
    private void next() {

        tfStrasse.clear();
        tfZusatz.clear();
        tfPlz.clear();
        tfOrt.clear();

        if (idx < newAddressString.size()) {
            tfAdresse.setText(newAddressString.get(idx));
            Platform.runLater(() -> tfStrasse.requestFocus());
        } else {
            onBeenden(new ActionEvent());
        }
        idx++;
    }

    /**
     * Gets the values.
     *
     * @return the values
     */
    private EclImportAdresse getValues() {
        return new EclImportAdresse(0, tfAdresse.getText(), tfStrasse.getText().trim(), tfZusatz.getText().trim(),
                tfPlz.getText().trim(), tfOrt.getText().trim(), cbLand.getValue().getCode());
    }

    /**
     * Inits the.
     *
     * @param pEigeneStage the eigene stage
     * @param list         the list
     */
    public void init(Stage pEigeneStage, List<EclInhaberImportAnmeldedaten> list) {
        eigeneStage = pEigeneStage;
        newAddress = list;
    }
}
