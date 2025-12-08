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
import javafx.scene.control.CheckBox;
import javafx.stage.Stage;

/**
 * The Class CtrlParameterAbstimmungReload.
 */
public class CtrlParameterAbstimmungReload extends CtrlRoot {

    /** The resources. */
    @FXML
    private ResourceBundle resources;

    /** The location. */
    @FXML
    private URL location;

    /** The btn weiter. */
    @FXML
    private Button btnWeiter;

    /** The btn abbruch. */
    @FXML
    private Button btnAbbruch;

    /** The cb weisungen. */
    @FXML
    private CheckBox cbWeisungen;

    /** The cb abstimmungen. */
    @FXML
    private CheckBox cbAbstimmungen;

    /** The text weiter. */
    public String textWeiter = "Weiter";

    /** The text abbruch. */
    public String textAbbruch = "Abbruch";

    /** true => Weiter geklickt; false=Abbruch geklickt". */
    public boolean ergebnis = true;

    /** The weisungen abbruch. */
    public boolean weisungenAbbruch = false;

    /** The abstimmungen abbruch. */
    public boolean abstimmungenAbbruch = false;

    /**
     * Initialize.
     */
    @FXML
    void initialize() {
        assert btnWeiter != null : "fx:id=\"btnWeiter\" was not injected: check your FXML file 'ParameterAbstimmungReload.fxml'.";
        assert btnAbbruch != null : "fx:id=\"btnAbbruch\" was not injected: check your FXML file 'ParameterAbstimmungReload.fxml'.";
        assert cbWeisungen != null : "fx:id=\"cbWeisungen\" was not injected: check your FXML file 'ParameterAbstimmungReload.fxml'.";
        assert cbAbstimmungen != null : "fx:id=\"cbAbstimmungen\" was not injected: check your FXML file 'ParameterAbstimmungReload.fxml'.";

        /************* Ab hier individuell ********************************************/

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
        weisungenAbbruch = cbWeisungen.isSelected();
        abstimmungenAbbruch = cbAbstimmungen.isSelected();
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

    /**
     * Inits the.
     *
     * @param pEigeneStage the eigene stage
     */
    public void init(Stage pEigeneStage) {
        eigeneStage = pEigeneStage;

    }

}
