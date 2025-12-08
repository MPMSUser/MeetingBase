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

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import de.meetingapps.meetingportal.meetComAllg.CaBug;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;

/**
 * The Class CtrlVersandAdressePruefenAuswahl.
 */
public class CtrlVersandAdressePruefenAuswahl {

    /** The resources. */
    @FXML
    private ResourceBundle resources;

    /** The location. */
    @FXML
    private URL location;

    /** The tg weg portal. */
    @FXML
    private RadioButton tgWegPortal;

    /** The tgg weg. */
    @FXML
    private ToggleGroup tggWeg;

    /** The tg weg alle. */
    @FXML
    private RadioButton tgWegAlle;

    /** The tg geprueft nur ungeprueft. */
    @FXML
    private RadioButton tgGeprueftNurUngeprueft;

    /** The tgg geprueft. */
    @FXML
    private ToggleGroup tggGeprueft;

    /** The tg geprueft alle. */
    @FXML
    private RadioButton tgGeprueftAlle;

    /** The tg manuell nur manuell. */
    @FXML
    private RadioButton tgManuellNurManuell;

    /** The tgg manuell. */
    @FXML
    private ToggleGroup tggManuell;

    /** The tg manuell alle. */
    @FXML
    private RadioButton tgManuellAlle;

    /** The btn starten. */
    @FXML
    private Button btnStarten;

    /**
     * Initialize.
     */
    @FXML
    void initialize() {
        assert tgWegPortal != null
                : "fx:id=\"tgWegPortal\" was not injected: check your FXML file 'VersandAdressePruefenAuswahl.fxml'.";
        assert tggWeg != null
                : "fx:id=\"tggWeg\" was not injected: check your FXML file 'VersandAdressePruefenAuswahl.fxml'.";
        assert tgWegAlle != null
                : "fx:id=\"tgWegAlle\" was not injected: check your FXML file 'VersandAdressePruefenAuswahl.fxml'.";
        assert tgGeprueftNurUngeprueft != null
                : "fx:id=\"tgGeprueftNurUngeprueft\" was not injected: check your FXML file 'VersandAdressePruefenAuswahl.fxml'.";
        assert tggGeprueft != null
                : "fx:id=\"tggGeprueft\" was not injected: check your FXML file 'VersandAdressePruefenAuswahl.fxml'.";
        assert tgGeprueftAlle != null
                : "fx:id=\"tgGeprueftAlle\" was not injected: check your FXML file 'VersandAdressePruefenAuswahl.fxml'.";
        assert tgManuellNurManuell != null
                : "fx:id=\"tgManuellNurManuell\" was not injected: check your FXML file 'VersandAdressePruefenAuswahl.fxml'.";
        assert tggManuell != null
                : "fx:id=\"tggManuell\" was not injected: check your FXML file 'VersandAdressePruefenAuswahl.fxml'.";
        assert tgManuellAlle != null
                : "fx:id=\"tgManuellAlle\" was not injected: check your FXML file 'VersandAdressePruefenAuswahl.fxml'.";
        assert btnStarten != null
                : "fx:id=\"btnStarten\" was not injected: check your FXML file 'VersandAdressePruefenAuswahl.fxml'.";

        /*************************
         * Ab hier individuell
         *********************************************/

    }

    /**
     * Clicked starten.
     *
     * @param event the event
     */
    @FXML
    void clickedStarten(ActionEvent event) {

        boolean nurInternet = false;
        boolean nurNochNichtGeprueft = false;
        boolean nurAbweichendeEingegeben = false;

        if (tgWegPortal.isSelected()) {
            nurInternet = true;
        }

        if (tgGeprueftNurUngeprueft.isSelected()) {
            nurNochNichtGeprueft = true;
        }

        if (tgManuellNurManuell.isSelected()) {
            nurAbweichendeEingegeben = true;
        }

        Stage neuerDialog = new Stage();

        CtrlVersandAdressePruefenBearbeiten controllerVersandAdressePruefenBearbeiten = new CtrlVersandAdressePruefenBearbeiten();

        controllerVersandAdressePruefenBearbeiten.init(neuerDialog, nurInternet, nurNochNichtGeprueft,
                nurAbweichendeEingegeben);

        FXMLLoader loader = new FXMLLoader(getClass().getResource("VersandAdressePruefenBearbeiten.fxml"));
        loader.setController(controllerVersandAdressePruefenBearbeiten);
        Parent mainPane = null;
        try {
            mainPane = (Parent) loader.load();
        } catch (IOException e) {
            CaBug.drucke("CtrlVersandAdressePruefenAuswahl.clickedStarten 001");
            e.printStackTrace();
        }
        Scene scene = new Scene(mainPane, 1280, 800);
        neuerDialog.setTitle("Prüfen Versandadresse");
        neuerDialog.setScene(scene);
        //   	neuerDialog.initModality(Modality.APPLICATION_MODAL);
        neuerDialog.show();
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
