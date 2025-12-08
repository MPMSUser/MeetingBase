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

import de.meetingapps.meetingclient.meetingClientDialoge.CtrlRoot;
import de.meetingapps.meetingportal.meetComBl.BlTeilnehmerLoginSperre;
import de.meetingapps.meetingportal.meetComDb.DbBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

/**
 * The Class CtrlSperreNullBestand.
 */
public class CtrlSperreNullBestand extends CtrlRoot {

    /** The resources. */
    @FXML
    private ResourceBundle resources;

    /** The location. */
    @FXML
    private URL location;

    /** The btn sperren ausfuehren. */
    @FXML
    private Button btnSperrenAusfuehren;

    /** The btn abbrechen. */
    @FXML
    private Button btnAbbrechen;

    /** ********Ab hier individuell***********. */

    private DbBundle lDbBundle = null;

    /**
     * Initialize.
     */
    @FXML
    void initialize() {

        /********** Ab hier individuell *******************/

        lDbBundle = new DbBundle();

    }

    /**
     * **************Eingabe**************************************.
     *
     * @param event the event
     */
    @FXML
    void onBtnAbbrechen(ActionEvent event) {
        eigeneStage.hide();
    }

    /**
     * On btn sperren ausfuehren.
     *
     * @param event the event
     */
    @FXML
    void onBtnSperrenAusfuehren(ActionEvent event) {
        BlTeilnehmerLoginSperre blTeilnehmerLoginSperre = new BlTeilnehmerLoginSperre(false, lDbBundle);
        blTeilnehmerLoginSperre.alle0BestaendeSperren();
        eigeneStage.hide();
    }

    /**
     * Inits the.
     *
     * @param pEigeneStage the eigene stage
     */
    public void init(Stage pEigeneStage) {
        eigeneStage = pEigeneStage;
    }

}
