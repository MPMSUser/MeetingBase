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

import de.meetingapps.meetingportal.meetComAllg.CaFehler;
import de.meetingapps.meetingportal.meetComBl.BlNummernformen;
import de.meetingapps.meetingportal.meetComDb.DbBundle;
import de.meetingapps.meetingportal.meetComEntities.EclNummernForm;
import de.meetingapps.meetingportal.meetComKonst.KonstCodeart;
import de.meetingapps.meetingportal.meetComKonst.KonstKartenart;
import de.meetingapps.meetingportal.meetComKonst.KonstKartenklasse;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

/**
 * The Class CtrlParameterNummernformenTesten.
 */
public class CtrlParameterNummernformenTesten {

    /** The resources. */
    @FXML
    private ResourceBundle resources;

    /** The location. */
    @FXML
    private URL location;

    /** The tf testeingabe. */
    @FXML
    private TextField tfTesteingabe;

    /** The btn beenden. */
    @FXML
    private Button btnBeenden;

    /** The tf gelesener string. */
    @FXML
    private TextArea tfGelesenerString;

    /** The tf fehlernr. */
    @FXML
    private TextField tfFehlernr;

    /** The tf fehlertext. */
    @FXML
    private TextArea tfFehlertext;

    /** The tfrc ist app ident. */
    @FXML
    private TextField tfrcIstAppIdent;

    /** The tfrc codeart. */
    @FXML
    private TextField tfrcCodeart;

    /** The tfrc kartenklasse. */
    @FXML
    private TextField tfrcKartenklasse;

    /** The tfrc kartenart. */
    @FXML
    private TextField tfrcKartenart;

    /** The tfrc stimmkarte sub nummernkreis. */
    @FXML
    private TextField tfrcStimmkarteSubNummernkreis;

    /** The tfrc eintrittskarte mit neben. */
    @FXML
    private TextField tfrcEintrittskarteMitNeben;

    /** The tfrc stimmkartennummer. */
    @FXML
    private TextField tfrcStimmkartennummer;

    /** The tfrc stimmart. */
    @FXML
    private TextField tfrcStimmart;

    /** The tfrc gattung. */
    @FXML
    private TextField tfrcGattung;

    /** The tfrc personen ident. */
    @FXML
    private TextField tfrcPersonenIdent;

    /** The tfrc bevollmaechtigter. */
    @FXML
    private TextField tfrcBevollmaechtigter;

    /** The tfrc agenda version. */
    @FXML
    private TextField tfrcAgendaVersion;

    /** The tfrc agenda zeichenzahl. */
    @FXML
    private TextField tfrcAgendaZeichenzahl;

    /** The tfrc nummernform ident. */
    @FXML
    private TextField tfrcNummernformIdent;

    /** The gp nummern. */
    @FXML
    private GridPane gpNummern;

    /** **Individuell***. */
    DbBundle dbBundle = null;

    /**
     * Initialize.
     */
    @FXML
    void initialize() {

        assert tfTesteingabe != null : "fx:id=\"tfTesteingabe\" was not injected: check your FXML file 'ParameterNummernformTesten.fxml'.";
        assert btnBeenden != null : "fx:id=\"btnBeenden\" was not injected: check your FXML file 'ParameterNummernformTesten.fxml'.";
        assert tfGelesenerString != null : "fx:id=\"tfGelesenerString\" was not injected: check your FXML file 'ParameterNummernformTesten.fxml'.";
        assert tfFehlernr != null : "fx:id=\"tfFehlernr\" was not injected: check your FXML file 'ParameterNummernformTesten.fxml'.";
        assert tfFehlertext != null : "fx:id=\"tfFehlertext\" was not injected: check your FXML file 'ParameterNummernformTesten.fxml'.";
        assert tfrcIstAppIdent != null : "fx:id=\"tfrcIstAppIdent\" was not injected: check your FXML file 'ParameterNummernformTesten.fxml'.";
        assert tfrcCodeart != null : "fx:id=\"tfrcCodeart\" was not injected: check your FXML file 'ParameterNummernformTesten.fxml'.";
        assert tfrcKartenklasse != null : "fx:id=\"tfrcKartenklasse\" was not injected: check your FXML file 'ParameterNummernformTesten.fxml'.";
        assert tfrcKartenart != null : "fx:id=\"tfrcKartenart\" was not injected: check your FXML file 'ParameterNummernformTesten.fxml'.";
        assert tfrcStimmkarteSubNummernkreis != null : "fx:id=\"tfrcStimmkarteSubNummernkreis\" was not injected: check your FXML file 'ParameterNummernformTesten.fxml'.";
        assert tfrcEintrittskarteMitNeben != null : "fx:id=\"tfrcEintrittskarteMitNeben\" was not injected: check your FXML file 'ParameterNummernformTesten.fxml'.";
        assert tfrcStimmkartennummer != null : "fx:id=\"tfrcStimmkartennummer\" was not injected: check your FXML file 'ParameterNummernformTesten.fxml'.";
        assert tfrcStimmart != null : "fx:id=\"tfrcStimmart\" was not injected: check your FXML file 'ParameterNummernformTesten.fxml'.";
        assert tfrcGattung != null : "fx:id=\"tfrcGattung\" was not injected: check your FXML file 'ParameterNummernformTesten.fxml'.";
        assert tfrcPersonenIdent != null : "fx:id=\"tfrcPersonenIdent\" was not injected: check your FXML file 'ParameterNummernformTesten.fxml'.";
        assert tfrcBevollmaechtigter != null : "fx:id=\"tfrcBevollmaechtigter\" was not injected: check your FXML file 'ParameterNummernformTesten.fxml'.";
        assert tfrcAgendaVersion != null : "fx:id=\"tfrcAgendaVersion\" was not injected: check your FXML file 'ParameterNummernformTesten.fxml'.";
        assert tfrcAgendaZeichenzahl != null : "fx:id=\"tfrcAgendaZeichenzahl\" was not injected: check your FXML file 'ParameterNummernformTesten.fxml'.";
        assert tfrcNummernformIdent != null : "fx:id=\"tfrcNummernformIdent\" was not injected: check your FXML file 'ParameterNummernformTesten.fxml'.";
        assert gpNummern != null : "fx:id=\"gpNummern\" was not injected: check your FXML file 'ParameterNummernformTesten.fxml'.";
        
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                tfTesteingabe.requestFocus();
            }
        });

        /*Damit aktuelle Parameter eingelesen sind!*/
        dbBundle = new DbBundle();
        dbBundle.openAll();
        dbBundle.closeAll();

    }

    /*****************Verarbeiten**************************/
    
    private void scanFeldVerarbeiten() {
        String gelesenerString = tfTesteingabe.getText();

        tfTesteingabe.setText("");
        tfGelesenerString.setText(gelesenerString);

        BlNummernformen blNummernformen = new BlNummernformen(dbBundle);
        int rc = blNummernformen.dekodiere(gelesenerString, KonstKartenklasse.unbekannt);

        tfFehlernr.setText(Integer.toString(rc));
        if (rc < 0) {
            tfFehlertext.setText(CaFehler.getFehlertext(rc, 0));
        } else {
            tfFehlertext.setText("");
        }

        if (blNummernformen.rcIstAppIdent) {
            tfrcIstAppIdent.setText("Ja");
        } else {
            tfrcIstAppIdent.setText("Nein");
        }

        tfrcCodeart.setText(
                Integer.toString(blNummernformen.rcCodeart) + " " + KonstCodeart.getText(blNummernformen.rcCodeart));
        tfrcNummernformIdent.setText(Integer.toString(blNummernformen.rcNummernformIdent));
        tfrcKartenklasse.setText(Integer.toString(blNummernformen.rcKartenklasse) + " "
                + KonstKartenklasse.getText(blNummernformen.rcKartenklasse));
        tfrcKartenart.setText(Integer.toString(blNummernformen.rcKartenart) + " "
                + KonstKartenart.getText(blNummernformen.rcKartenart));
        tfrcStimmkarteSubNummernkreis.setText(Integer.toString(blNummernformen.rcStimmkarteSubNummernkreis));
        if (blNummernformen.rcEintrittskarteMitNeben == 1) {
            tfrcEintrittskarteMitNeben.setText("Ja");
        } else {
            tfrcEintrittskarteMitNeben.setText("Nein");
        }
        tfrcStimmkartennummer.setText(Integer.toString(blNummernformen.rcStimmkartennummer));
        tfrcStimmart.setText(Integer.toString(blNummernformen.rcStimmart));
        tfrcGattung.setText(Integer.toString(blNummernformen.rcGattung));
        tfrcPersonenIdent.setText(Integer.toString(blNummernformen.rcPersonenIdent));
        tfrcBevollmaechtigter.setText(Integer.toString(blNummernformen.rcBevollmaechtigter));
        tfrcAgendaVersion.setText(Integer.toString(blNummernformen.rcAgendaVersion));
        tfrcAgendaZeichenzahl.setText(Integer.toString(blNummernformen.rcAgendaZeichenzahl));

        gpNummern.getChildren().clear();
        gpNummern.setGridLinesVisible(true);

        Label lblNummer = new Label();
        lblNummer.setText("Nummer");
        gpNummern.add(lblNummer, 0, 0);

        Label lblNummerIndex = new Label();
        lblNummerIndex.setText("Nummer- Index");
        lblNummerIndex.setWrapText(true);
        gpNummern.add(lblNummerIndex, 1, 0);

        Label lblRC = new Label();
        lblRC.setText("RC");
        gpNummern.add(lblRC, 2, 0);

        Label lblklasse = new Label();
        lblklasse.setText("Klasse");
        gpNummern.add(lblklasse, 3, 0);

        Label lblVertrPerson = new Label();
        lblVertrPerson.setText("Vertr.Person");
        lblVertrPerson.setWrapText(true);
        gpNummern.add(lblVertrPerson, 4, 0);

        Label lblAbstimmung = new Label();
        lblAbstimmung.setText("Abstimmung");
        gpNummern.add(lblAbstimmung, 5, 0);

        if (blNummernformen.rcIdentifikationsnummer != null) {
            for (int i = 0; i < blNummernformen.rcIdentifikationsnummer.size(); i++) {
                Label lblNummerI = new Label();
                lblNummerI.setText(blNummernformen.rcIdentifikationsnummer.get(i));
                gpNummern.add(lblNummerI, 0, i + 1);

                Label lblNummerIndexI = new Label();
                lblNummerIndexI.setText(blNummernformen.rcIdentifikationsnummerNeben.get(i));
                gpNummern.add(lblNummerIndexI, 1, i + 1);

                Label lblRCI = new Label();
                lblRCI.setText(Integer.toString(blNummernformen.rcRcZuIdentifikationsnummer.get(i)));
                gpNummern.add(lblRCI, 2, i + 1);

                Label lblklasseI = new Label();
                lblklasseI.setText(Integer.toString(blNummernformen.rcKartenklasseZuIdentifikationsnummer.get(i)));
                gpNummern.add(lblklasseI, 3, i + 1);

                if (blNummernformen.rcAppVertretendePersonIdent != null
                        && blNummernformen.rcAppVertretendePersonIdent.size() > 0) {
                    Label lblVertrPersonI = new Label();
                    lblVertrPersonI.setText(Integer.toString(blNummernformen.rcAppVertretendePersonIdent.get(i)));
                    gpNummern.add(lblVertrPersonI, 4, i + 1);
                }

                if (blNummernformen.rcAbstimmung != null && blNummernformen.rcAbstimmung.size() > 0) {
                    Label lblAbstimmungI = new Label();
                    lblAbstimmungI.setText(blNummernformen.rcAbstimmung.get(i));
                    gpNummern.add(lblAbstimmungI, 5, i + 1);
                }

            }
        }

    }

    /*************************Bildschirmaktionen*************************************/
    
    /*ENTER abfangen im ScanFeld*/
    @FXML
    void onKeyPressedTesteingabe(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            scanFeldVerarbeiten();
        }
    }

    /**
     * On btn beenden.
     *
     * @param event the event
     */
    @FXML
    void onBtnBeenden(ActionEvent event) {
        eigeneStage.hide();
    }

    /** ************Anzeigefunktionen**************************. */

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
