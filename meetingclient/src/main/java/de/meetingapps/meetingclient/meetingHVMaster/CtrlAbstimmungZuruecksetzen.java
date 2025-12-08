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

import de.meetingapps.meetingportal.meetComBl.BlAbstimmung;
import de.meetingapps.meetingportal.meetComDb.DbBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

/**
 * The Class CtrlAbstimmungZuruecksetzen.
 */
public class CtrlAbstimmungZuruecksetzen {

    /** The resources. */
    @FXML
    private ResourceBundle resources;

    /** The location. */
    @FXML
    private URL location;

    /** The btn zuruecksetzen. */
    @FXML
    private Button btnZuruecksetzen;

    /** The btn schliessen. */
    @FXML
    private Button btnSchliessen;

    /** The lbl meldung. */
    @FXML
    private Label lblMeldung;

    /**
     * Initialize.
     */
    @FXML
    void initialize() {
        assert btnZuruecksetzen != null : "fx:id=\"btnZuruecksetzen\" was not injected: check your FXML file 'AbstimmungZuruecksetzen.fxml'.";
        assert btnSchliessen != null : "fx:id=\"btnSchliessen\" was not injected: check your FXML file 'AbstimmungZuruecksetzen.fxml'.";
        assert lblMeldung != null : "fx:id=\"lblMeldung\" was not injected: check your FXML file 'AbstimmungZuruecksetzen.fxml'.";

        /********************************
         * Ab hier individuell
         *********************************************/

        lblMeldung.setText("");

    }

    /**
     * Clicked btn schliessen.
     *
     * @param event the event
     */
    @FXML
    void clickedBtnSchliessen(ActionEvent event) {
        eigeneStage.close();

    }

    /**
     * Clicked btn zuruecksetzen.
     *
     * @param event the event
     */
    @FXML
    void clickedBtnZuruecksetzen(ActionEvent event) {
        DbBundle lDbBundle = new DbBundle();
        lDbBundle.openAll();

        BlAbstimmung blAbstimmung = new BlAbstimmung(lDbBundle);
        boolean brc = blAbstimmung.leseAktivenAbstimmungsblock();
        if (brc == false) {
            lblMeldung.setText("Fehler beim Lesen Stimmblock");
            return;
        }
        blAbstimmung.zuruecksetzen();

        lDbBundle.closeAll();
        lblMeldung.setText("Zurückgesetzt");

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
