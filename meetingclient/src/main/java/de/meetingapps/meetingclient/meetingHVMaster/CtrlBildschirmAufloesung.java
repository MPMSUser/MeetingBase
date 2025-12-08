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
package de.meetingapps.meetingclient.meetingHVMaster;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import de.meetingapps.meetingportal.meetComAllg.CaBug;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * The Class CtrlBildschirmAufloesung.
 */
public class CtrlBildschirmAufloesung {

    /** The resources. */
    @FXML
    private ResourceBundle resources;

    /** The location. */
    @FXML
    private URL location;

    /** The lbl X wert. */
    @FXML
    private TextField lblXWert;

    /** The lbl Y wert. */
    @FXML
    private TextField lblYWert;

    /** The btn anzeigen. */
    @FXML
    private Button btnAnzeigen;

    /**
     * On btn anzeigen.
     *
     * @param event the event
     */
    @FXML
    void onBtnAnzeigen(ActionEvent event) {
        String hString = lblXWert.getText();
        int xWert = Integer.parseInt(hString);

        hString = lblYWert.getText();
        int yWert = Integer.parseInt(hString);

        System.out.println("Test");

        Stage neuerDialog = new Stage();

        CtrlBildschirmAufloesungMaske controllerFenster = new CtrlBildschirmAufloesungMaske();

        controllerFenster.init(neuerDialog);

        FXMLLoader loader = new FXMLLoader(getClass().getResource("BildschirmAufloesungMaske.fxml"));
        loader.setController(controllerFenster);
        Parent mainPane = null;
        try {
            mainPane = (Parent) loader.load();
        } catch (IOException e) {
            CaBug.drucke("CtrlHauptStage.onBtnAnzeigen 001");
            e.printStackTrace();
        }
        Scene scene = new Scene(mainPane, xWert, yWert);
        neuerDialog.setTitle("Bildschirmauflösung " + xWert + " x " + yWert);
        neuerDialog.setScene(scene);
        neuerDialog.initModality(Modality.APPLICATION_MODAL);
        neuerDialog.showAndWait();

    }

    /**
     * Initialize.
     */
    @FXML
    void initialize() {
        assert lblXWert != null : "fx:id=\"lblXWert\" was not injected: check your FXML file 'BildschirmAufloesung.fxml'.";
        assert lblYWert != null : "fx:id=\"lblYWert\" was not injected: check your FXML file 'BildschirmAufloesung.fxml'.";
        assert btnAnzeigen != null : "fx:id=\"btnAnzeigen\" was not injected: check your FXML file 'BildschirmAufloesung.fxml'.";

    }

    //    private Stage eigeneStage;

    /**
     * Inits the.
     *
     * @param pEigeneStage the eigene stage
     */
    public void init(Stage pEigeneStage) {
        //        eigeneStage = pEigeneStage;

    }

}
