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
package de.meetingapps.meetingclient.meetingClientDialoge;

import java.net.URL;
import java.util.ResourceBundle;

import de.meetingapps.meetingclient.meetingClientAllg.CaPruefeLogin;
import de.meetingapps.meetingportal.meetComAllg.CaFehler;
import de.meetingapps.meetingportal.meetComHVParam.ParamS;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * The Class CtrlLoginPasswortAendern.
 */
public class CtrlLoginPasswortAendern extends CtrlRoot {

    /** The resources. */
    @FXML
    private ResourceBundle resources;

    /** The location. */
    @FXML
    private URL location;

    /** The tf kennung. */
    @FXML
    private TextField tfKennung;

    /** The tf altes passwort. */
    @FXML
    private PasswordField tfAltesPasswort;

    /** The tf passwort. */
    @FXML
    private PasswordField tfPasswort;

    /** The tf passwort wiederholen. */
    @FXML
    private PasswordField tfPasswortWiederholen;

    /** The btn starten. */
    @FXML
    private Button btnStarten;

    /** The btn beenden. */
    @FXML
    private Button btnBeenden;

    /**
     * Initialize.
     */
    @FXML
    void initialize() {
        assert tfKennung != null
                : "fx:id=\"tfKennung\" was not injected: check your FXML file 'LoginPasswortAendern.fxml'.";
        assert tfAltesPasswort != null
                : "fx:id=\"tfAltesPasswort\" was not injected: check your FXML file 'LoginPasswortAendern.fxml'.";
        assert tfPasswort != null
                : "fx:id=\"tfPasswort\" was not injected: check your FXML file 'LoginPasswortAendern.fxml'.";
        assert tfPasswortWiederholen != null
                : "fx:id=\"tfPasswortWiederholen\" was not injected: check your FXML file 'LoginPasswortAendern.fxml'.";
        assert btnStarten != null
                : "fx:id=\"btnStarten\" was not injected: check your FXML file 'LoginPasswortAendern.fxml'.";
        assert btnBeenden != null
                : "fx:id=\"btnBeenden\" was not injected: check your FXML file 'LoginPasswortAendern.fxml'.";

        /************* Ab hier individuell ********************************************/

        tfKennung.setText(ParamS.eclUserLogin.kennung);

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                tfAltesPasswort.requestFocus();
            }
        });

    }

    /**
     * afPasswortZuKurz 
     * afPasswortNichtSicher
     * 
     * 1 = erfolgreich.
     *
     * @return the int
     */
    private int setzeNeuesPasswort() {
        int rc = 1;
        CaPruefeLogin caPruefeLogin = new CaPruefeLogin();
        rc = caPruefeLogin.setzeNeuesPasswort(ParamS.eclUserLogin, tfAltesPasswort.getText(), tfPasswort.getText());
        System.out.println("CtrLogin rc=" + rc);
        return rc;
    }

    /**
     * Btn beenden clicked.
     *
     * @param event the event
     */
    @FXML
    void btnBeendenClicked(ActionEvent event) {
        fortsetzen = false;
        eigeneStage.hide();
    }

    /**
     * Btn starten clicked.
     *
     * @param event the event
     */
    @FXML
    void btnStartenClicked(ActionEvent event) {

        /*Eingaben überprüfen*/

        if (tfPasswort.getText().compareTo(tfPasswortWiederholen.getText()) != 0) {
            CaZeigeHinweis zeigeHinweis = new CaZeigeHinweis();
            zeigeHinweis.zeige(eigeneStage, "Wiederholung des Passwortes stimmt nicht mit dem neuen Passwort überein!");
            tfPasswortWiederholen.requestFocus();
            return;
        }

        int rc = setzeNeuesPasswort();
        if (rc == 1) {
            fortsetzen = true;
            eigeneStage.hide();
        } else {
            String fehlerText = "";
            switch (rc) {
            case CaFehler.afPasswortZuKurz:
                fehlerText = "Passwort muß mindestens 8 Zeichen enthalten!";
                break;
            case CaFehler.afPasswortNichtSicher:
                fehlerText = "Passwort muß mindestens ein Sonderzeichen, einen Großbuchsten, einen Kleinbuchstaben und eine Zahl enthalten!";
                break;
            case CaFehler.afPasswortBereitsVerwendet:
                fehlerText = "Passwort bereits kürzlich verwendet! Bitte wählen Sie ein anderes Passwort!";
                break;
            }

            CaZeigeHinweis zeigeHinweis = new CaZeigeHinweis();
            zeigeHinweis.zeige(eigeneStage, fehlerText);
            return;
        }
    }

    /** The eigene stage. */
    private Stage eigeneStage;

    /** Return-Werte für aufrufende Routine. */
    public boolean fortsetzen = false;

    /**
     * Inits the.
     *
     * @param pEigeneStage the eigene stage
     */
    public void init(Stage pEigeneStage) {
        eigeneStage = pEigeneStage;

    }

}
