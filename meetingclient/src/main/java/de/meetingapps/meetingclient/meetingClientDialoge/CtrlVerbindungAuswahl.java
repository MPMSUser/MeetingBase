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

import de.meetingapps.meetingclient.meetingClientOberflaechen.ObjectActions;
import de.meetingapps.meetingportal.meetComHVParam.ParamInterneKommunikation;
import de.meetingapps.meetingportal.meetComHVParam.ParamS;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;

/**
 * The Class CtrlVerbindungAuswahl.
 */
public class CtrlVerbindungAuswahl extends CtrlRoot {

    /** The resources. */
    @FXML
    private ResourceBundle resources;

    /** The location. */
    @FXML
    private URL location;

    /** The cb server. */
    @FXML
    private ComboBox<String> cbServer;

    /** The ck datenbankverbindung. */
    @FXML
    private CheckBox ckDatenbankverbindung;

    /** The ck web service. */
    @FXML
    private CheckBox ckWebService;

    /** The cb server online. */
    @FXML
    private ComboBox<String> cbServerOnline;

    /** The ck datenbankverbindung online. */
    @FXML
    private CheckBox ckDatenbankverbindungOnline;

    /** The ck web service online. */
    @FXML
    private CheckBox ckWebServiceOnline;

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
        assert cbServer != null : "fx:id=\"cbServer\" was not injected: check your FXML file 'VerbindungAuswahl.fxml'.";
        assert btnStarten != null
                : "fx:id=\"btnStarten\" was not injected: check your FXML file 'VerbindungAuswahl.fxml'.";
        assert btnBeenden != null
                : "fx:id=\"btnBeenden\" was not injected: check your FXML file 'VerbindungAuswahl.fxml'.";
        assert ckDatenbankverbindung != null
                : "fx:id=\"ckDatenbankverbindung\" was not injected: check your FXML file 'VerbindungAuswahl.fxml'.";
        assert ckWebService != null
                : "fx:id=\"ckWebService\" was not injected: check your FXML file 'VerbindungAuswahl.fxml'.";

        /************* Ab hier individuell ********************************************/

        cbServer.getItems().removeAll(cbServer.getItems());
        for (int i = 0; i < ParamInterneKommunikation.webServicePfadZurAuswahlTest.length; i++) {
            cbServer.getItems().add(ParamInterneKommunikation.webServicePfadZurAuswahlTest[i]);
        }
        if (ParamS.clGlobalVar.webServicePfadNr == -1) {
            cbServer.getSelectionModel().select(ParamInterneKommunikation.webServicePfadZurAuswahlTest[ParamInterneKommunikation.defaultOffsetFuerAuswahl]);
            ParamS.clGlobalVar.webServicePfadNr = ParamInterneKommunikation.defaultOffsetFuerAuswahl;
            ParamS.clGlobalVar.datenbankPfadNr = ParamInterneKommunikation.defaultOffsetFuerAuswahl;
        }

        ObjectActions.filterComboBox(cbServer, btnStarten);

        ckDatenbankverbindung.setSelected(true);
        ckWebService.setSelected(true);

        cbServerOnline.getItems().removeAll(cbServerOnline.getItems());
        for (int i = 0; i < ParamInterneKommunikation.webServicePfadZurAuswahlTest.length; i++) {
            cbServerOnline.getItems().add(ParamInterneKommunikation.webServicePfadZurAuswahlTest[i]);
        }
        if (ParamS.clGlobalVar.onlineWebServicePfadNr == -1) {
            cbServerOnline.getSelectionModel().select(ParamInterneKommunikation.webServicePfadZurAuswahlTest[ParamInterneKommunikation.defaultOffsetFuerAuswahl]);
            ParamS.clGlobalVar.onlineWebServicePfadNr = ParamInterneKommunikation.defaultOffsetFuerAuswahl;
            ParamS.clGlobalVar.onlineDatenbankPfadNr = ParamInterneKommunikation.defaultOffsetFuerAuswahl;
        }

        ObjectActions.filterComboBox(cbServerOnline, btnStarten);

        ckDatenbankverbindungOnline.setSelected(true);
        ckWebServiceOnline.setSelected(true);

    }

    /**
     * Cb server changed.
     *
     * @param event the event
     */
    @FXML
    void cbServerChanged(ActionEvent event) {
        String erg = cbServer.getValue();
        int gef = 0;
        for (int i = 0; i < ParamInterneKommunikation.webServicePfadZurAuswahlTest.length; i++) {
            if (ParamInterneKommunikation.webServicePfadZurAuswahlTest[i].compareTo(erg) == 0) {
                gef = i;
            }
        }
        ParamS.clGlobalVar.webServicePfadNr = gef;
        ParamS.clGlobalVar.datenbankPfadNr = ParamInterneKommunikation.liefereDatenbankPfadNr(gef);
    }

    /**
     * Cb server online changed.
     *
     * @param event the event
     */
    @FXML
    void cbServerOnlineChanged(ActionEvent event) {
        String erg = cbServerOnline.getValue();
        int gef = 0;
        for (int i = 0; i < ParamInterneKommunikation.webServicePfadZurAuswahlTest.length; i++) {
            if (ParamInterneKommunikation.webServicePfadZurAuswahlTest[i].compareTo(erg) == 0) {
                gef = i;
            }
        }
        ParamS.clGlobalVar.onlineWebServicePfadNr = gef;
        ParamS.clGlobalVar.onlineDatenbankPfadNr = ParamInterneKommunikation.liefereDatenbankPfadNr(gef);
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

        /** Zum Aufrufen des Test-Moduls direkt nach dem Start-Aufruf */
        //      if (1==1) {
        //          starteTest();
        //          return;
        //      }

        if (ParamS.clGlobalVar.webServicePfadNr == -1) {
            CaZeigeHinweis caZeigeHinweis = new CaZeigeHinweis();
            caZeigeHinweis.zeige(eigeneStage, "Bitte Server auswählen!");
            return;
        }

        if (!ckDatenbankverbindung.isSelected()) {
            ParamS.clGlobalVar.datenbankPfadNr = -1;
        }
        if (!ckWebService.isSelected()) {
            ParamS.clGlobalVar.webServicePfadNr = -1;
        }

        if (ParamS.clGlobalVar.datenbankPfadNr == -1 && ParamS.clGlobalVar.webServicePfadNr == -1) {
            CaZeigeHinweis caZeigeHinweis = new CaZeigeHinweis();
            caZeigeHinweis.zeige(eigeneStage, "Datenbankverbindung und/oder Web-Verbindung zulassen!");
            return;
        }

        fortsetzen = true;
        eigeneStage.hide();
    }

    /** The eigene stage. */
    private Stage eigeneStage;

    /** The fortsetzen. */
    public boolean fortsetzen = false;

    /**
     * Inits the.
     *
     * @param pEigeneStage the eigene stage
     */
    public void init(Stage pEigeneStage) {
        eigeneStage = pEigeneStage;

    }

    //    void starteTest(){
    //          Stage neuerDialog=new Stage();
    //
    //      CtrlModulTest controllerFenster= 
    //              new CtrlModulTest();
    //
    //      controllerFenster.init(neuerDialog); 
    //
    //      FXMLLoader loader = new FXMLLoader(
    //              getClass().getResource(
    //                      "/betterHVMaster/ModulTest.fxml"
    //                      )
    //              );
    //      loader.setController(controllerFenster);
    //      Parent mainPane=null;
    //      try {
    //          mainPane = (Parent) loader.load();
    //      } catch (IOException e) {
    //          CaBug.drucke("CtrlHauptStage.gewModulTest 001");
    //          e.printStackTrace();
    //      }
    //      Scene scene=new Scene(mainPane,1000,800);
    //      neuerDialog.setTitle("Modul-Test");
    //      neuerDialog.setScene(scene);
    //      neuerDialog.initModality(Modality.APPLICATION_MODAL);
    //      neuerDialog.showAndWait();
    //    }

}
