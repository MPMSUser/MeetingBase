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

import de.meetingapps.meetingportal.meetComAllg.CaString;
import de.meetingapps.meetingportal.meetComDb.DbBundle;
import de.meetingapps.meetingportal.meetComEntities.EclNummernForm;
import de.meetingapps.meetingportal.meetComEntities.EclNummernFormSet;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * The Class CtrlParameterNummernformenNeuesSet.
 */
public class CtrlParameterNummernformenNeuesSet {

    /** The resources. */
    @FXML
    private ResourceBundle resources;

    /** The location. */
    @FXML
    private URL location;

    /** The tf set name. */
    @FXML
    private TextField tfSetName;

    /** The btn neu. */
    @FXML
    private Button btnNeu;

    /** The btn abbruch. */
    @FXML
    private Button btnAbbruch;

    /**
     * Initialize.
     */
    @FXML
    void initialize() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                tfSetName.requestFocus();
            }
        });
        assert tfSetName != null : "fx:id=\"tfSetName\" was not injected: check your FXML file 'ParameterNummernformenNeuesSet.fxml'.";
        assert btnNeu != null : "fx:id=\"btnNeu\" was not injected: check your FXML file 'ParameterNummernformenNeuesSet.fxml'.";
        assert btnAbbruch != null : "fx:id=\"btnAbbruch\" was not injected: check your FXML file 'ParameterNummernformenNeuesSet.fxml'.";

    }

    /*************************Bildschirmaktionen*************************************/
    
    @FXML
    void onBtnAbbruch(ActionEvent event) {
        eigeneStage.close();
    }

    /**
     * On btn neu.
     *
     * @param event the event
     */
    @FXML
    void onBtnNeu(ActionEvent event) {
        if (tfSetName.getText().isEmpty()) {
            return;
        }

        DbBundle lDbBundle = new DbBundle();

        EclNummernFormSet lNummernFormSet = new EclNummernFormSet();
        lNummernFormSet.name = CaString.trunc(tfSetName.getText(), 20);

        lDbBundle.openAll();
        lDbBundle.dbNummernFormSet.insert(lNummernFormSet);
        lDbBundle.closeAll();

        eigeneStage.close();

    }

    /**************Anzeigefunktionen***************************/
    
    private Stage eigeneStage;

    /** The zu bearbeitende nummern form. */
    public EclNummernForm zuBearbeitendeNummernForm = null;

    /**
     * Funktion 1 Neuanlege, 2 Ändern.
     *
     * @param pEigeneStage the eigene stage
     */
    public void init(Stage pEigeneStage) {
        eigeneStage = pEigeneStage;

    }

}
