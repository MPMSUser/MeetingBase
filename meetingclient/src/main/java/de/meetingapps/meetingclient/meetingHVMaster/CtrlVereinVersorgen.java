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

import de.meetingapps.meetingportal.meetComBl.BlTeilnehmerStandVerein;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * The Class CtrlVereinVersorgen.
 */
public class CtrlVereinVersorgen {

    /** The resources. */
    @FXML
    private ResourceBundle resources;

    /** The location. */
    @FXML
    private URL location;

    /** The btn updaten. */
    @FXML
    private Button btnUpdaten;

    /** The btn abbrechen. */
    @FXML
    private Button btnAbbrechen;

    /** The lbl meldung. */
    @FXML
    private Label lblMeldung;

    /**
     * Initialize.
     */
    @FXML
    void initialize() {
        assert btnUpdaten != null : "fx:id=\"btnUpdaten\" was not injected: check your FXML file 'ElekVereinVersorgen.fxml'.";
        assert btnAbbrechen != null : "fx:id=\"btnAbbrechen\" was not injected: check your FXML file 'ElekVereinVersorgen.fxml'.";
        assert lblMeldung != null : "fx:id=\"lblMeldung\" was not injected: check your FXML file 'ElekVereinVersorgen.fxml'.";

        /********************************Ab hier individuell*********************************************/

        lblMeldung.setText("");

    }

    /** The update anzahl. */
    private int updateAnzahl = 1;

    /** The update laeuft. */
    private int updateLaeuft = 0;

    /** The versorgen service. */
    VersorgenService versorgenService = new VersorgenService();

    /**
     * Clicked btn updaten.
     *
     * @param event the event
     */
    @FXML
    void clickedBtnUpdaten(ActionEvent event) {
        if (updateLaeuft == 1) {
            btnUpdaten.setText(Integer.toString(updateAnzahl));
        } else {
            versorgenService.start();
            updateLaeuft = 1;
        }
    }

    /**
     * Aktualisieren.
     */
    void aktualisieren() {

        BlTeilnehmerStandVerein blTeilnehmerStandVerein = new BlTeilnehmerStandVerein();
        blTeilnehmerStandVerein.neuerStand(false);
    }

    /**
     * Clicked btn abbrechen.
     *
     * @param event the event
     */
    @FXML
    void clickedBtnAbbrechen(ActionEvent event) {
        eigeneStage.close();
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

    /**
     * The Class VersorgenService.
     */
    public class VersorgenService extends javafx.concurrent.ScheduledService {

        /**
         * Instantiates a new versorgen service.
         */
        public VersorgenService() {
            super();
            System.out.println("Test1");
            setPeriod(Duration.seconds(150)); //150

        }

        /**
         * Creates the task.
         *
         * @return the task
         */
        @Override
        protected Task<?> createTask() {
            return new VersorgenTask();
        }

    }

    /**
     * The Class VersorgenTask.
     */
    public class VersorgenTask extends Task<Object> {

        /**
         * Instantiates a new versorgen task.
         */
        public VersorgenTask() {
            System.out.println("CounterTask");
        }

        /**
         * Call.
         *
         * @return the object
         * @throws Exception the exception
         */
        @Override
        protected Object call() throws Exception {
            System.out.println("call");
            aktualisieren();
            updateAnzahl++;
            return null;
        }

    }

}
