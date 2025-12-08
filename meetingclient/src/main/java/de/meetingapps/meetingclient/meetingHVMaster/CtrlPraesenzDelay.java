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

import de.meetingapps.meetingportal.meetComDb.DbBundle;
import de.meetingapps.meetingportal.meetComEntities.EclParameter;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * The Class CtrlPraesenzDelay.
 */
public class CtrlPraesenzDelay {

    /** The btn delay speichern. */
    @FXML
    private Button btnDelaySpeichern;

    /** The btn abbrechen. */
    @FXML
    private Button btnAbbrechen;

    /** The lbl meldung. */
    @FXML
    private Label lblMeldung;

    /** The tf delay stufe. */
    @FXML
    private TextField tfDelayStufe;

    /**
     * Initialize.
     */
    @FXML
    void initialize() {
        assert btnDelaySpeichern != null : "fx:id=\"btnDelaySpeichern\" was not injected: check your FXML file 'PraesenzDelay.fxml'.";
        assert btnAbbrechen != null : "fx:id=\"btnAbbrechen\" was not injected: check your FXML file 'PraesenzDelay.fxml'.";
        assert lblMeldung != null : "fx:id=\"lblMeldung\" was not injected: check your FXML file 'PraesenzDelay.fxml'.";
        assert tfDelayStufe != null : "fx:id=\"tfDelayStufe\" was not injected: check your FXML file 'PraesenzDelay.fxml'.";

        /********************************Ab hier individuell*********************************************/
        
        refreshInhalt();

        lblMeldung.setText("");

    }

    /**
     * Refresh inhalt.
     */
    private void refreshInhalt() {
        DbBundle lDbBundle = new DbBundle();
        lDbBundle.openAll(); //Zum Einlesen der aktuellsten Parameter!
        lDbBundle.closeAll();
        tfDelayStufe.setText(Integer.toString(lDbBundle.param.paramAkkreditierung.plfdHVDelayed));
    }

    /**
     * Clicked btn delay speichern.
     *
     * @param event the event
     */
    @FXML
    void clickedBtnDelaySpeichern(ActionEvent event) {

        DbBundle lDbBundle = new DbBundle();
        lDbBundle.openAll();

        EclParameter lEclParameter = new EclParameter();
        lEclParameter.ident = 513;
        lDbBundle.dbParameter.read(lEclParameter);
        lEclParameter = lDbBundle.dbParameter.ergebnisPosition(0);
        lEclParameter.wert = tfDelayStufe.getText();
        lDbBundle.dbParameter.update(lEclParameter);

        lDbBundle.closeAll();
        refreshInhalt();
        lblMeldung.setText("Delay gespeichert");

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

}
