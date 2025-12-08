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

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

/**
 * The Class CtrlStornoMeldungBestaetigung.
 */
public class CtrlStornoMeldungBestaetigung {

    /** The resources. */
    @FXML
    private ResourceBundle resources;

    /** The location. */
    @FXML
    private URL location;

    /** The lbl 1. */
    @FXML
    private Label lbl1;

    /** The btn stornieren. */
    @FXML
    private Button btnStornieren;

    /** The btn abbruch. */
    @FXML
    private Button btnAbbruch;

    /**
     * Initialize.
     */
    @FXML
    void initialize() {
        assert lbl1 != null : "fx:id=\"lbl1\" was not injected: check your FXML file 'StornoMeldungBestaetigung.fxml'.";
        assert btnStornieren != null
                : "fx:id=\"btnStornieren\" was not injected: check your FXML file 'StornoMeldungBestaetigung.fxml'.";
        assert btnAbbruch != null
                : "fx:id=\"btnAbbruch\" was not injected: check your FXML file 'StornoMeldungBestaetigung.fxml'.";

    }

    /**
     * Clicked abbruch.
     *
     * @param event the event
     */
    @FXML
    void clickedAbbruch(ActionEvent event) {
        ergebnis = false;
        eigeneStage.hide();
    }

    /**
     * Clicked btn stornieren.
     *
     * @param event the event
     */
    @FXML
    void clickedBtnStornieren(ActionEvent event) {
        ergebnis = true;
        eigeneStage.hide();

    }

    /** The eigene stage. */
    private Stage eigeneStage;

    /** The ergebnis. */
    private boolean ergebnis = false;

    /**
     * Inits the.
     *
     * @param pEigeneStage the eigene stage
     */
    public void init(Stage pEigeneStage) {
        eigeneStage = pEigeneStage;
    }

    /**
     * *********Standard setter und getter**********************.
     *
     * @return true, if is ergebnis
     */

    public boolean isErgebnis() {
        return ergebnis;
    }

    /**
     * Sets the ergebnis.
     *
     * @param ergebnis the new ergebnis
     */
    public void setErgebnis(boolean ergebnis) {
        this.ergebnis = ergebnis;
    }

}
