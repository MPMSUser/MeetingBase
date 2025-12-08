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
package de.meetingapps.meetingclient.meetingInfo;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import de.meetingapps.meetingclient.meetingBestand.CtrlStatus;
import de.meetingapps.meetingclient.meetingClientAllg.CALeseParameterNeu;
import de.meetingapps.meetingclient.meetingClientDialoge.CaController;
import de.meetingapps.meetingclient.meetingClientDialoge.CaIcon;
import de.meetingapps.meetingclient.meetingClientDialoge.CaZeigeHinweis;
import de.meetingapps.meetingclient.meetingClientDialoge.CtrlLoginNeu;
import de.meetingapps.meetingclient.meetingClientDialoge.CtrlRoot;
import de.meetingapps.meetingclient.meetingClientOberflaechen.CaOpenWindow;
import de.meetingapps.meetingclient.meetingClientOberflaechen.ObjectActions;
import de.meetingapps.meetingportal.meetComAllg.CaBug;
import de.meetingapps.meetingportal.meetComAllg.CaFehler;
import de.meetingapps.meetingportal.meetComHVParam.ParamInterneKommunikation;
import de.meetingapps.meetingportal.meetComHVParam.ParamS;
import de.meetingapps.meetingportal.meetComStub.CInjects;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * The Class CtrlHauptStage.
 */
public class CtrlHauptStage extends CtrlRoot {
    
    @FXML
    private AnchorPane rootPane;

    /** The resources. */
    @FXML
    private ResourceBundle resources;

    /** The location. */
    @FXML
    private URL location;

    /** The mn mandant wechseln. */
    @FXML
    private MenuItem mnMandantWechseln;

    /** The mn beenden. */
    @FXML
    private MenuItem mnBeenden;

    /** The mn einzelfall. */
    @FXML
    private MenuItem mnEinzelfall;

    /** The mn 1 eintrittskarte selbst. */
    @FXML
    private MenuItem mn1EintrittskarteSelbst;

    /** The mn 1 eintrittskarte vertreter. */
    @FXML
    private MenuItem mn1EintrittskarteVertreter;

    /** The mn 2 eintrittskarte selbst. */
    @FXML
    private MenuItem mn2EintrittskarteSelbst;

    /** The mn 2 eintrittskarte selbst oder vertreter. */
    @FXML
    private MenuItem mn2EintrittskarteSelbstOderVertreter;

    /** The mn SRV. */
    @FXML
    private MenuItem mnSRV;

    /** The mn briefwahl. */
    @FXML
    private MenuItem mnBriefwahl;

    /** The mn KIAV. */
    @FXML
    private MenuItem mnKIAV;

    /** The mn dauervollmacht. */
    @FXML
    private MenuItem mnDauervollmacht;

    /** The mn orga. */
    @FXML
    private MenuItem mnOrga;

    /** The mn wek SRV. */
    @FXML
    private MenuItem mnWekSRV;

    /** The mn wek briefwahl. */
    @FXML
    private MenuItem mnWekBriefwahl;

    /** The mn versandadresse pruefen. */
    @FXML
    private MenuItem mnVersandadressePruefen;

    /** The mn eintrittskkarten drucken. */
    @FXML
    private MenuItem mnEintrittskkartenDrucken;

    /** The lbl user. */
    @FXML
    private Label lblUser;

    /** The lbl server. */
    @FXML
    private Label lblServer;

    /**
     * Gew wek briefwahl.
     *
     * @param event the event
     */
    @FXML
    void gewWekBriefwahl(ActionEvent event) {

    }

    /**
     * Gew wek SRV.
     *
     * @param event the event
     */
    @FXML
    void gewWekSRV(ActionEvent event) {

    }

    /** The mn massentest. */
    @FXML
    private MenuItem mnMassentest;

    /**
     * Initialize.
     */
    @FXML
    void initialize() {

        assert mnMandantWechseln != null : "fx:id=\"mnMandantWechseln\" was not injected: check your FXML file 'HauptStage.fxml'.";
        assert mnBeenden != null : "fx:id=\"mnBeenden\" was not injected: check your FXML file 'HauptStage.fxml'.";
        assert mnEinzelfall != null : "fx:id=\"mnEinzelfall\" was not injected: check your FXML file 'HauptStage.fxml'.";
        assert lblUser != null : "fx:id=\"lblUser\" was not injected: check your FXML file 'HauptStage.fxml'.";
        assert lblServer != null : "fx:id=\"lblServer\" was not injected: check your FXML file 'HauptStage.fxml'.";

        /**********************
         * Ab hier individuell
         **************************************************/
        zeigeUser();
        
        ObjectActions.createInfoButton(rootPane, new String[] { "M - Modulauswahl" }, 30.0, 5.0);
        
        eigeneStage.addEventHandler(KeyEvent.KEY_PRESSED, e -> {
            if (e.getCode() == KeyCode.M) {
                CaOpenWindow.openModulauswahl(eigeneStage);
            }
        });
    }

    /**
     * Zeige user.
     */
    public void zeigeUser() {
        lblUser.setText("Mandant=" + ParamS.clGlobalVar.mandant + " Laufwerk=" + ParamS.clGlobalVar.lwPfadAllgemein
                + " User=" + ParamS.clGlobalVar.benutzernr + " WebService="
                + ParamInterneKommunikation.webServicePfadZurAuswahlTest[ParamS.clGlobalVar.webServicePfadNr]);
        lblServer.setText(ParamInterneKommunikation.webServiceInfo[ParamS.clGlobalVar.webServicePfadNr]);
    }

    /**
     * Gew modulauswahl.
     *
     * @param event the event
     */
    @FXML
    void gewModulauswahl(ActionEvent event) {
        CaOpenWindow.openModulauswahl(eigeneStage);
    }
    

    /**
     * Gew mandant wechseln.
     *
     * @param event the event
     */
    @FXML
    void gewMandantWechseln(ActionEvent event) {

        Stage zwischenDialog = new Stage();
        CaIcon.bestandsverwaltung(zwischenDialog);

        CtrlLoginNeu controllerDialog = new CtrlLoginNeu();
        controllerDialog.nurMandantenwechsel = true;

        controllerDialog.init(zwischenDialog);

        CaController caController = new CaController();
        caController.open(zwischenDialog, controllerDialog,
                "/de/meetingapps/meetingclient/meetingClientDialoge/LoginNeu.fxml", 700, 400,
                "Mandanten-Auswahl", true);

        boolean bRC = controllerDialog.fortsetzen;

        if (bRC == false) {

        } else {
            /*ParamS.clGlobalVar.mandant etc. sind schon vom Controller belegt*/
            CALeseParameterNeu caLeseParameterNeu = new CALeseParameterNeu();
            int rc = caLeseParameterNeu.leseHVParameter();
            //			return rc;
            //
            //			ParamS.eclEmittent=controllerDialog.eclEmittent;
            //
            //			BlParameterUeberWebService lBlInitHVWebServices=new BlParameterUeberWebService();
            //			int rc=lBlInitHVWebServices.holeHVParameter();
            //
            if (rc < 1) { //Fehlerbehandlung
                CaZeigeHinweis caZeigeHinweis = new CaZeigeHinweis();
                caZeigeHinweis.zeige(eigeneStage, "Systemfehler " + "Fehler " + CaFehler.getFehlertext(rc, 0));
            }

            zeigeUser();

        }
    }

    /**
     * Gew einzelfall.
     *
     * @param event the event
     */
    @FXML
    void gewEinzelfall(ActionEvent event) {

        CInjects.aendern = false;
        Stage neuerDialog = new Stage();

        CtrlStatus controllerStatus = new CtrlStatus();

        controllerStatus.init(neuerDialog, 0);

        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/de/meetingapps/meetingclient/meetingPortAnmelde/Status.fxml"));
        loader.setController(controllerStatus);
        Parent mainPane = null;
        try {
            mainPane = (Parent) loader.load();
        } catch (IOException e) {
            CaBug.drucke("CtrlHauptStage.gewEinzelfall 001");
            e.printStackTrace();
        }
        Scene scene = new Scene(mainPane, 1200, 850);
        neuerDialog.setTitle("Einzelfallbearbeitung");
        neuerDialog.setScene(scene);
        neuerDialog.initModality(Modality.APPLICATION_MODAL);
        neuerDialog.showAndWait();

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
