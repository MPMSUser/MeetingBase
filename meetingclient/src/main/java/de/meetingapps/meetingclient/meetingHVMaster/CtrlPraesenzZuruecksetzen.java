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

import de.meetingapps.meetingportal.meetComBl.BlPraesenzlistenNummer;
import de.meetingapps.meetingportal.meetComDb.DbBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

/**
 * The Class CtrlPraesenzZuruecksetzen.
 */
public class CtrlPraesenzZuruecksetzen {

    /** The resources. */
    @FXML
    private ResourceBundle resources;

    /** The location. */
    @FXML
    private URL location;

    /** The btn praesenz zuruecksetzen. */
    @FXML
    private Button btnPraesenzZuruecksetzen;

    /** The btn abbrechen. */
    @FXML
    private Button btnAbbrechen;

    /** The lbl aktuelle praesenz nr. */
    @FXML
    private Label lblAktuellePraesenzNr;

    /** The lbl meldung. */
    @FXML
    private Label lblMeldung;

    /**
     * Initialize.
     */
    @FXML
    void initialize() {
        assert btnPraesenzZuruecksetzen != null : "fx:id=\"btnPraesenzFeststellen\" was not injected: check your FXML file 'PraesenzFeststellen.fxml'.";
        assert btnAbbrechen != null : "fx:id=\"btnAbbrechen\" was not injected: check your FXML file 'PraesenzFeststellen.fxml'.";
        assert lblAktuellePraesenzNr != null : "fx:id=\"lblAktuellePraesenzNr\" was not injected: check your FXML file 'PraesenzFeststellen.fxml'.";
        assert lblMeldung != null : "fx:id=\"lblMeldung\" was not injected: check your FXML file 'PraesenzFeststellen.fxml'.";

        /********************************Ab hier individuell*********************************************/

        refreshInhalt();

        lblMeldung.setText("");

    }

    /**
     * Refresh inhalt.
     */
    private void refreshInhalt() {
        DbBundle lDbBundle = new DbBundle();
        lDbBundle.openAll();
        BlPraesenzlistenNummer lBlPraesenzlistenNummer = new BlPraesenzlistenNummer(lDbBundle);
        lBlPraesenzlistenNummer.leseAktuelleNummernFuerBuchung();
        lDbBundle.closeAll();
        lblAktuellePraesenzNr.setText(Integer.toString(lDbBundle.clGlobalVar.zuVerzeichnisNr1));
    }

    /**
     * Clicked btn praesenz zuruecksetzen.
     *
     * @param event the event
     */
    @FXML
    void clickedBtnPraesenzZuruecksetzen(ActionEvent event) {

        DbBundle lDbBundle = new DbBundle();
        lDbBundle.openAll();
        lDbBundle.dbHVDatenLfd.deleteAll();

        lDbBundle.closeAll();
        refreshInhalt();
        lblMeldung.setText("Präsenz Zurückgsetzt");

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
