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
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * The Class CtrlParameterNummernformenEinzelneForm.
 */
public class CtrlParameterNummernformenEinzelneForm {

    /** The resources. */
    @FXML
    private ResourceBundle resources;

    /** The location. */
    @FXML
    private URL location;

    /** The lbl ersetzt. */
    @FXML
    private Label lblErsetzt;

    /** The cb geloescht. */
    @FXML
    private CheckBox cbGeloescht;

    /** The tf ident. */
    @FXML
    private TextField tfIdent;

    /** The tf ersetzt. */
    @FXML
    private TextField tfErsetzt;

    /** The tf codierung. */
    @FXML
    private TextField tfCodierung;

    /** The tf beschreibung. */
    @FXML
    private TextArea tfBeschreibung;

    /** The btn speichern. */
    @FXML
    private Button btnSpeichern;

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
                tfCodierung.requestFocus();
            }
        });

        assert lblErsetzt != null : "fx:id=\"lblErsetzt\" was not injected: check your FXML file 'ParameterNummernformEinzelneForm.fxml'.";
        assert cbGeloescht != null : "fx:id=\"cbGeloescht\" was not injected: check your FXML file 'ParameterNummernformEinzelneForm.fxml'.";
        assert tfIdent != null : "fx:id=\"tfIdent\" was not injected: check your FXML file 'ParameterNummernformEinzelneForm.fxml'.";
        assert tfErsetzt != null : "fx:id=\"tfErsetzt\" was not injected: check your FXML file 'ParameterNummernformEinzelneForm.fxml'.";
        assert tfCodierung != null : "fx:id=\"tfCodierung\" was not injected: check your FXML file 'ParameterNummernformEinzelneForm.fxml'.";
        assert tfBeschreibung != null : "fx:id=\"tfBeschreibung\" was not injected: check your FXML file 'ParameterNummernformEinzelneForm.fxml'.";
        assert btnSpeichern != null : "fx:id=\"btnSpeichern\" was not injected: check your FXML file 'ParameterNummernformEinzelneForm.fxml'.";
        assert btnAbbruch != null : "fx:id=\"btnAbbruch\" was not injected: check your FXML file 'ParameterNummernformEinzelneForm.fxml'.";

        if (funktion == 1) {/*Neuaufnahme*/
            tfIdent.setText("(wird vergeben)");
            tfIdent.setEditable(false);
            cbGeloescht.setVisible(false);
            lblErsetzt.setVisible(false);
            tfErsetzt.setVisible(false);
        } else { /*Ändern*/
            tfIdent.setText(Integer.toString(zuBearbeitendeNummernForm.ident));
            tfIdent.setEditable(false);
            cbGeloescht.setVisible(true);
            if (zuBearbeitendeNummernForm.geloescht == 1) {
                cbGeloescht.setSelected(true);
            }
            tfErsetzt.setText(Integer.toString(zuBearbeitendeNummernForm.ersetztDurch));
            tfCodierung.setText(zuBearbeitendeNummernForm.kodierung);
            tfBeschreibung.setText(zuBearbeitendeNummernForm.beschreibung);
        }
    }

    /*************************Bildschirmaktionen*************************************/
    
    @FXML
    void onBtnAbbruch(ActionEvent event) {
        eigeneStage.close();
    }

    /**
     * On btn speichern.
     *
     * @param event the event
     */
    @FXML
    void onBtnSpeichern(ActionEvent event) {
        if (tfCodierung.getText().isEmpty()) {
            return;
        }
        if (cbGeloescht.isSelected()) {
            if (tfErsetzt.getText().isEmpty()) {
                tfErsetzt.setText("0");
            }
            if (tfErsetzt.getText().length() > 8) {
                return;
            }
            if (!CaString.isNummern(tfErsetzt.getText())) {
                return;
            }
        }

        DbBundle lDbBundle = new DbBundle();

        if (funktion == 1) {/*Neuaufnahme*/
            EclNummernForm lNummernForm = new EclNummernForm();
            lNummernForm.kodierung = CaString.trunc(tfCodierung.getText(), 20);
            lNummernForm.beschreibung = CaString.trunc(tfBeschreibung.getText(), 120);

            lDbBundle.openAll();
            lDbBundle.dbNummernForm.insert(lNummernForm);
            lDbBundle.closeAll();
        } else { /*Ändern*/
            if (!cbGeloescht.isSelected()) {
                zuBearbeitendeNummernForm.geloescht = 0;
                zuBearbeitendeNummernForm.ersetztDurch = 0;
            } else {
                zuBearbeitendeNummernForm.geloescht = 1;
                zuBearbeitendeNummernForm.ersetztDurch = Integer.parseInt(tfErsetzt.getText());
            }
            zuBearbeitendeNummernForm.kodierung = CaString.trunc(tfCodierung.getText(), 20);
            zuBearbeitendeNummernForm.beschreibung = CaString.trunc(tfBeschreibung.getText(), 120);

            lDbBundle.openAll();
            lDbBundle.dbNummernForm.update(zuBearbeitendeNummernForm);
            lDbBundle.closeAll();

        }

        eigeneStage.close();

    }

    /**************Anzeigefunktionen***************************/

    private Stage eigeneStage;

    /** The funktion. */
    private int funktion;

    /** The zu bearbeitende nummern form. */
    public EclNummernForm zuBearbeitendeNummernForm = null;

    /**
     * Funktion 1 Neuanlege, 2 Ändern.
     *
     * @param pEigeneStage the eigene stage
     * @param pFunktion    the funktion
     */
    public void init(Stage pEigeneStage, int pFunktion) {
        eigeneStage = pEigeneStage;
        funktion = pFunktion;

    }

}
