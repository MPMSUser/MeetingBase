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

import java.net.URL;
import java.util.ResourceBundle;

import de.meetingapps.meetingclient.meetingClientDialoge.CtrlRoot;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * The Class CtrlAbstimmungArchiv.
 */
public class CtrlAbstimmungku310 extends CtrlRoot {

    /** The resources. */
    @FXML
    private ResourceBundle resources;

    /** The location. */
    @FXML
    private URL location;

    @FXML
    private Button btnWeiter;

    @FXML
    private TextField tfArbeitgeber;

    @FXML
    private TextField tfArbeitnehmer;

    @FXML
    void onBtnWeiter(ActionEvent event) {

        eingabeArbeitgeber=tfArbeitgeber.getText().trim();
        eingabeArbeitnehmer=tfArbeitnehmer.getText().trim();
        eigeneStage.hide();
    }



    /**
     * Initialize.
     */
    @FXML
    void initialize() {

        /********** Ab hier individuell *******************/


    }


    /**
     * Inits the.
     *
     * @param pEigeneStage the eigene stage
     */
    public String eingabeArbeitgeber="";
    public String eingabeArbeitnehmer="";
    
    public void init(Stage pEigeneStage) {
        eigeneStage = pEigeneStage;
    }

}
