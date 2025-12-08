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
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/**
 * The Class CtrlZeigeHinweis.
 */
public class CtrlZeigeHinweis {

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

    /** The pn basis. */
    @FXML
    private Pane pnBasis;

    /**
     * Initialize.
     */
    @FXML
    void initialize() {
        assert lblHinweis != null : "fx:id=\"lblHinweis\" was not injected: check your FXML file 'ZeigeHinweis.fxml'.";
        assert btnWeiter != null : "fx:id=\"btnWeiter\" was not injected: check your FXML file 'ZeigeHinweis.fxml'.";

        /************* Ab hier individuell ********************************************/

        lblHinweis.setText(hinweisText);
        btnWeiter.requestFocus();

        switch (farbe) {
        case 0:/*normal*/
            break;
        case 1:/*grün*/
            pnBasis.setStyle("-fx-background-color: #00ff00; ");
            break;
        case 2:/*gelb*/
            pnBasis.setStyle("-fx-background-color: #fcf800; ");
            break;
        case 3: /*rot*/
            pnBasis.setStyle("-fx-background-color: #ff0000; ");
            break;
        }

    }

    /**
     * On btn weiter.
     *
     * @param event the event
     */
    @FXML
    void onBtnWeiter(ActionEvent event) {
        eigeneStage.hide();
    }

    /** The eigene stage. */
    private Stage eigeneStage;

    /** The hinweis text. */
    private String hinweisText = "";

    /** The farbe. */
    public int farbe = 0;

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
