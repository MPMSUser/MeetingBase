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

import de.meetingapps.meetingclient.meetingClient.ClBonPrinter;
import de.meetingapps.meetingportal.meetComHVParam.ParamS;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

/**
 * The Class CtrlWechseln.
 */
public class CtrlWechseln {
    
    public final int width = 500;
    public final int height = 300;

    /** The resources. */
    @FXML
    private ResourceBundle resources;

    /** The location. */
    @FXML
    private URL location;

    /** The btn ja. */
    @FXML
    private Button btnJa;

    /** The tf wechseln. */
    @FXML
    private Label tfWechseln;

    /** The btn nein. */
    @FXML
    private Button btnNein;

    /** The btn probedruck. */
    @FXML
    private Button btnProbedruck;

    /**
     * On btn probedruck.
     *
     * @param event the event
     */
    @FXML
    void onBtnProbedruck(ActionEvent event) {

        if (ParamS.paramGeraet.bonDruckerIstZugeordnet) {

            ClBonPrinter bonPrinter = new ClBonPrinter();
            bonPrinter.votes = "Testdruck Zeile 1\nTestdruck Zeile 2\n";
            bonPrinter.drucken();
        }
    }

    /**
     * Initialize.
     */
    @FXML
    void initialize() {
        assert btnJa != null : "fx:id=\"btnJa\" was not injected: check your FXML file 'Wechseln.fxml'.";
        assert tfWechseln != null : "fx:id=\"tfWechseln\" was not injected: check your FXML file 'Wechseln.fxml'.";
        assert btnNein != null : "fx:id=\"btnNein\" was not injected: check your FXML file 'Wechseln.fxml'.";
        assert btnProbedruck != null : "fx:id=\"btnProbedruck\" was not injected: check your FXML file 'Wechseln.fxml'.";

        /************* Ab hier individuell ********************************************/

        tfWechseln.setText(wechselText);

    }

    /**
     * On btn ja.
     *
     * @param event the event
     */
    @FXML
    void onBtnJa(ActionEvent event) {
        ausgewaehlt = true;
        eigeneStage.hide();

    }

    /**
     * On btn nein.
     *
     * @param event the event
     */
    @FXML
    void onBtnNein(ActionEvent event) {
        ausgewaehlt = false;
        eigeneStage.hide();

    }

    /** The eigene stage. */
    private Stage eigeneStage;

    /** The wechsel text. */
    private String wechselText = "";

    /** The ausgewaehlt. */
    public boolean ausgewaehlt = false;

    /**
     * Inits the.
     *
     * @param pEigeneStage the eigene stage
     * @param pWechselText the wechsel text
     */
    public void init(Stage pEigeneStage, String pWechselText) {
        eigeneStage = pEigeneStage;
        wechselText = pWechselText;

    }

}
