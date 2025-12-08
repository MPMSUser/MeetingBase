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
package de.meetingapps.meetingclient.meetingku178Akkreditierung;

import java.net.URL;
import java.util.ResourceBundle;

import de.meetingapps.meetingclient.meetingClientDialoge.CtrlRoot;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * The Class CtrlVertreterEingabe.
 */
public class CtrlVertreterEingabe extends CtrlRoot {

    /** The resources. */
    @FXML
    private ResourceBundle resources;

    /** The location. */
    @FXML
    private URL location;

    /** The tf name. */
    @FXML
    private TextField tfName;

    /** The tf vorname. */
    @FXML
    private TextField tfVorname;

    /** The tf ort. */
    @FXML
    private TextField tfOrt;

    /** The btn abbrechen. */
    @FXML
    private Button btnAbbrechen;

    /** The btn uebernehmen. */
    @FXML
    private Button btnUebernehmen;

    /** -1 => Abbruch 1 = ok, Vertreterdaten gefüllt. */
    public int rc = 0;

    /** The name. */
    public String name = "";

    /** The vorname. */
    public String vorname = "";

    /** The ort. */
    public String ort = "";

    /**
     * Initialize.
     */
    @FXML
    void initialize() {
        assert tfName != null : "fx:id=\"tfName\" was not injected: check your FXML file 'VertreterEingabe.fxml'.";
        assert tfVorname != null : "fx:id=\"tfVorname\" was not injected: check your FXML file 'VertreterEingabe.fxml'.";
        assert tfOrt != null : "fx:id=\"tfOrt\" was not injected: check your FXML file 'VertreterEingabe.fxml'.";
        assert btnAbbrechen != null : "fx:id=\"btnAbbrechen\" was not injected: check your FXML file 'VertreterEingabe.fxml'.";
        assert btnUebernehmen != null : "fx:id=\"btnUebernehmen\" was not injected: check your FXML file 'VertreterEingabe.fxml'.";

    }

    /**
     * On btn abbrechen.
     *
     * @param event the event
     */
    @FXML
    void onBtnAbbrechen(ActionEvent event) {
        rc = -1;
        eigeneStage.hide();
        return;
    }

    /**
     * On btn uebernehmen.
     *
     * @param event the event
     */
    @FXML
    void onBtnUebernehmen(ActionEvent event) {
        name = tfName.getText();
        vorname = tfVorname.getText();
        ort = tfOrt.getText();

        if (name.isEmpty() || name.length() > 80) {
            this.fehlerMeldung("Bitte Name des Verteters (maximal 80 Stellen) eingeben");
            return;
        }
        if (vorname.isEmpty() || vorname.length() > 80) {
            this.fehlerMeldung("Bitte Vorname des Verteters (maximal 80 Stellen) eingeben");
            return;
        }
        if (ort.isEmpty() || ort.length() > 80) {
            this.fehlerMeldung("Bitte Ort des Verteters (maximal 80 Stellen) eingeben");
            return;
        }

        rc = 1;
        eigeneStage.hide();
        return;

    }

    /** The eigene stage. */
    private Stage eigeneStage;

    /**
     * Inits the.
     *
     * @param pEigeneStage the eigene stage
     */
    public void init(Stage pEigeneStage) {
        eigeneStage = pEigeneStage;

    }

}
