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

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

/**
 * The Class CtrlZeige2Buttons.
 */
public class CtrlZeige2Buttons {

    /** The resources. */
    @FXML
    private ResourceBundle resources;

    /** The location. */
    @FXML
    private URL location;

    /** The lbl hinweis. */
    @FXML
    private Label lblHinweis;

    /** The btn weiter. */
    @FXML
    private Button btnWeiter;

    /** The btn abbruch. */
    @FXML
    private Button btnAbbruch;

    /** The text weiter. */
    public String textWeiter = "Weiter";

    /** The text abbruch. */
    public String textAbbruch = "Abbruch";

    /** true => Weiter geklickt; false=Abbruch geklickt". */
    public boolean ergebnis = true;

    /**
     * Initialize.
     */
    @FXML
    void initialize() {
        assert lblHinweis != null : "fx:id=\"lblHinweis\" was not injected: check your FXML file 'Zeige2Buttons.fxml'.";
        assert btnWeiter != null : "fx:id=\"btnWeiter\" was not injected: check your FXML file 'Zeige2Buttons.fxml'.";
        assert btnAbbruch != null : "fx:id=\"btnAbbruch\" was not injected: check your FXML file 'Zeige2Buttons.fxml'.";

        /************* Ab hier individuell ********************************************/

        lblHinweis.setText(hinweisText);
        btnWeiter.setText(textWeiter);
        btnAbbruch.setText(textAbbruch);
        btnWeiter.requestFocus();
    }

    /**
     * On btn weiter.
     *
     * @param event the event
     */
    @FXML
    void onBtnWeiter(ActionEvent event) {
        ergebnis = true;
        eigeneStage.hide();
    }

    /**
     * On btn abbruch.
     *
     * @param event the event
     */
    @FXML
    void onBtnAbbruch(ActionEvent event) {
        ergebnis = false;
        eigeneStage.hide();
    }

    /** The eigene stage. */
    private Stage eigeneStage;

    /** The hinweis text. */
    private String hinweisText = "";

    /**
     * Inits the.
     *
     * @param pEigeneStage the eigene stage
     * @param pHinweisText the hinweis text
     */
    public void init(Stage pEigeneStage, String pHinweisText) {
        eigeneStage = pEigeneStage;
        hinweisText = pHinweisText;

    }

}
