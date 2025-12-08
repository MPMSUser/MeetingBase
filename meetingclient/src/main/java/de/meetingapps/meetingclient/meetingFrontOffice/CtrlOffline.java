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
package de.meetingapps.meetingclient.meetingFrontOffice;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

/**
 * The Class CtrlOffline.
 */
public class CtrlOffline {

    /** The resources. */
    @FXML
    private ResourceBundle resources;

    /** The location. */
    @FXML
    private URL location;

    /** The btn retry. */
    @FXML
    private Button btnRetry;

    /** The btn fortsetzen. */
    @FXML
    private Button btnFortsetzen;

    /**
     * On btn retry.
     *
     * @param event the event
     */
    @FXML
    void onBtnRetry(ActionEvent event) {
        retrygedrueckt = true;
        eigeneStage.hide();
    }

    /**
     * On btn fortsetzen.
     *
     * @param event the event
     */
    @FXML
    void onBtnFortsetzen(ActionEvent event) {
        fortsetzengedrueckt = true;
        eigeneStage.hide();

    }

    /**
     * Initialize.
     */
    @FXML
    void initialize() {
        assert btnRetry != null : "fx:id=\"btnRetry\" was not injected: check your FXML file 'OfflineStage.fxml'.";
        assert btnFortsetzen != null : "fx:id=\"btnFortsetzen\" was not injected: check your FXML file 'OfflineStage.fxml'.";

        retrygedrueckt = false;
    }

    /** The eigene stage. */
    private Stage eigeneStage;

    /** The retrygedrueckt. */
    public boolean retrygedrueckt = false;

    /** The fortsetzengedrueckt. */
    public boolean fortsetzengedrueckt = false;

    /**
     * Inits the.
     *
     * @param pEigeneStage the eigene stage
     */
    public void init(Stage pEigeneStage) {
        eigeneStage = pEigeneStage;

    }

}
