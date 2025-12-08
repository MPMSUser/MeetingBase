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

import de.meetingapps.meetingportal.meetComEntities.EclWillenserklaerungStatusNeu;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

/**
 * The Class CtrlStornoBestaetigung.
 */
public class CtrlStornoBestaetigung {

    /** The resources. */
    @FXML
    private ResourceBundle resources;

    /** The location. */
    @FXML
    private URL location;

    /** The lbl 1. */
    @FXML
    private Label lbl1;

    /** The lbl 2. */
    @FXML
    private Label lbl2;

    /** The lbl 3. */
    @FXML
    private Label lbl3;

    /** The lbl 4. */
    @FXML
    private Label lbl4;

    /** The lbl 5. */
    @FXML
    private Label lbl5;

    /** The lbl 6. */
    @FXML
    private Label lbl6;

    /** The lbl 7. */
    @FXML
    private Label lbl7;

    /** The lbl 8. */
    @FXML
    private Label lbl8;

    /** The lbl 9. */
    @FXML
    private Label lbl9;

    /** The lbl 10. */
    @FXML
    private Label lbl10;

    /** The btn stornieren. */
    @FXML
    private Button btnStornieren;

    /** The btn abbruch. */
    @FXML
    private Button btnAbbruch;

    /**
     * Initialize.
     */
    @FXML
    void initialize() {
        assert lbl1 != null : "fx:id=\"lbl1\" was not injected: check your FXML file 'StornoBestaetigung.fxml'.";
        assert lbl2 != null : "fx:id=\"lbl2\" was not injected: check your FXML file 'StornoBestaetigung.fxml'.";
        assert lbl3 != null : "fx:id=\"lbl3\" was not injected: check your FXML file 'StornoBestaetigung.fxml'.";
        assert lbl4 != null : "fx:id=\"lbl4\" was not injected: check your FXML file 'StornoBestaetigung.fxml'.";
        assert lbl5 != null : "fx:id=\"lbl5\" was not injected: check your FXML file 'StornoBestaetigung.fxml'.";
        assert lbl6 != null : "fx:id=\"lbl6\" was not injected: check your FXML file 'StornoBestaetigung.fxml'.";
        assert lbl7 != null : "fx:id=\"lbl7\" was not injected: check your FXML file 'StornoBestaetigung.fxml'.";
        assert lbl8 != null : "fx:id=\"lbl8\" was not injected: check your FXML file 'StornoBestaetigung.fxml'.";
        assert lbl9 != null : "fx:id=\"lbl9\" was not injected: check your FXML file 'StornoBestaetigung.fxml'.";
        assert lbl10 != null : "fx:id=\"lbl10\" was not injected: check your FXML file 'StornoBestaetigung.fxml'.";
        assert btnStornieren != null
                : "fx:id=\"btnStornieren\" was not injected: check your FXML file 'StornoBestaetigung.fxml'.";
        assert btnAbbruch != null
                : "fx:id=\"btnAbbruch\" was not injected: check your FXML file 'StornoBestaetigung.fxml'.";

        /*************** Ab hier individuell **********************************/

        lbl1.setText("");
        lbl2.setText("");
        lbl3.setText("");
        lbl4.setText("");
        lbl5.setText("");
        lbl6.setText("");
        lbl7.setText("");
        lbl8.setText("");
        lbl9.setText("");
        lbl10.setText("");

        int i;
        for (i = 0; i < eclWillenserklaerungStatus.textListeIntern.size(); i++) {
            String hString = eclWillenserklaerungStatus.textListeIntern.get(i);
            switch (i) {
            case 0:
                lbl1.setText(hString);
                break;
            case 1:
                lbl2.setText(hString);
                break;
            case 2:
                lbl3.setText(hString);
                break;
            case 3:
                lbl4.setText(hString);
                break;
            case 4:
                lbl5.setText(hString);
                break;
            case 5:
                lbl6.setText(hString);
                break;
            case 6:
                lbl7.setText(hString);
                break;
            case 7:
                lbl8.setText(hString);
                break;
            case 8:
                lbl9.setText(hString);
                break;
            case 9:
                lbl10.setText(hString);
                break;
            default:
                break;
            }
        }

    }

    /**
     * Clicked abbruch.
     *
     * @param event the event
     */
    @FXML
    void clickedAbbruch(ActionEvent event) {
        ergebnis = false;
        eigeneStage.hide();
    }

    /**
     * Clicked btn stornieren.
     *
     * @param event the event
     */
    @FXML
    void clickedBtnStornieren(ActionEvent event) {
        ergebnis = true;
        eigeneStage.hide();

    }

    /** The eigene stage. */
    private Stage eigeneStage;

    /** The ecl willenserklaerung status. */
    private EclWillenserklaerungStatusNeu eclWillenserklaerungStatus = null;

    /** The ergebnis. */
    private boolean ergebnis = false;

    /**
     * Inits the.
     *
     * @param pEigeneStage             the eigene stage
     * @param funktion                 the funktion
     * @param pWillenserklaerungStatus the willenserklaerung status
     */
    public void init(Stage pEigeneStage, int funktion,
            EclWillenserklaerungStatusNeu pWillenserklaerungStatus) {
        eigeneStage = pEigeneStage;
        eclWillenserklaerungStatus = pWillenserklaerungStatus;
    }

    /**
     * *********Standard setter und getter**********************.
     *
     * @return true, if is ergebnis
     */

    public boolean isErgebnis() {
        return ergebnis;
    }

    /**
     * Sets the ergebnis.
     *
     * @param ergebnis the new ergebnis
     */
    public void setErgebnis(boolean ergebnis) {
        this.ergebnis = ergebnis;
    }

}
