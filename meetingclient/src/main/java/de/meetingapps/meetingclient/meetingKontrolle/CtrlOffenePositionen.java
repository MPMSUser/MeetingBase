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
package de.meetingapps.meetingclient.meetingKontrolle;

import java.net.URL;
import java.util.ResourceBundle;

import de.meetingapps.meetingportal.meetComDb.DbBundle;
import de.meetingapps.meetingportal.meetComEntities.EclWillenserklaerung;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

/**
 * The Class CtrlOffenePositionen.
 */
public class CtrlOffenePositionen {

    /** The resources. */
    @FXML
    private ResourceBundle resources;

    /** The location. */
    @FXML
    private URL location;

    /** The btn schliessen. */
    @FXML
    private Button btnSchliessen;

    /** The grpn inhalt. */
    @FXML
    private GridPane grpnInhalt;

    /**
     * On btn schliessen.
     *
     * @param event the event
     */
    @FXML
    void onBtnSchliessen(ActionEvent event) {
        eigeneStage.hide();
    }

    /**
     * Initialize.
     */
    @FXML
    void initialize() {
        assert btnSchliessen != null : "fx:id=\"btnSchliessen\" was not injected: check your FXML file 'OffenePositionen.fxml'.";
        assert grpnInhalt != null : "fx:id=\"grpnInhalt\" was not injected: check your FXML file 'OffenePositionen.fxml'.";

        DbBundle lDbBundle = new DbBundle();
        lDbBundle.openAll();
        grpnInhalt.getChildren().clear();

        int anz = lDbBundle.dbWillenserklaerung.leseNichtGepruefte();
        System.out.println("anz=" + anz);
        if (anz > 0) {
            for (int i = 0; i < anz; i++) {
                EclWillenserklaerung lWillenserklaerung = lDbBundle.dbWillenserklaerung.willenserklaerungArray[i];

                Label label1 = new Label();
                label1.setText(lWillenserklaerung.zutrittsIdent);
                grpnInhalt.add(label1, 0, i);

                Label label2 = new Label();
                label2.setText(lWillenserklaerung.stimmkarte1);
                grpnInhalt.add(label2, 1, i);

                Label label3 = new Label();
                label3.setText(Integer.toString(lWillenserklaerung.protokollnr));
                grpnInhalt.add(label3, 2, i);

                Label label4 = new Label();
                label4.setText(Integer.toString(lWillenserklaerung.benutzernr));
                grpnInhalt.add(label4, 3, i);

            }

        }

        lDbBundle.closeAll();

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
