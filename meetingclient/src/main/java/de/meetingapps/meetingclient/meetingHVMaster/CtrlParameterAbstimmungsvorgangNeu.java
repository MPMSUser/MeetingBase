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

import de.meetingapps.meetingclient.meetingClientDialoge.CtrlRoot;
import de.meetingapps.meetingportal.meetComEntities.EclAbstimmungsblock;
import de.meetingapps.meetingportal.meetComHVParam.ParamS;
import de.meetingapps.meetingportal.meetComStub.StubAbstimmungen;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * The Class CtrlParameterAbstimmungsvorgangNeu.
 */
public class CtrlParameterAbstimmungsvorgangNeu extends CtrlRoot {

    /** The resources. */
    @FXML
    private ResourceBundle resources;

    /** The location. */
    @FXML
    private URL location;

    /** The btn neu. */
    @FXML
    private Button btnNeu;

    /** The btn abbrechen. */
    @FXML
    private Button btnAbbrechen;

    /** The lbl englisch. */
    @FXML
    private Label lblEnglisch;

    /** The tf kurzbezeichnung DE. */
    @FXML
    private TextField tfKurzbezeichnungDE;

    /** The tf bezeichnung DE. */
    @FXML
    private TextField tfBezeichnungDE;

    /** The tf bezeichnung EN. */
    @FXML
    private TextField tfBezeichnungEN;

    /**
     * Initialize.
     */
    @FXML
    void initialize() {
        assert btnNeu != null : "fx:id=\"btnNeu\" was not injected: check your FXML file 'ParameterAbstimmungsvorgangNeu.fxml'.";
        assert btnAbbrechen != null : "fx:id=\"btnAbbrechen\" was not injected: check your FXML file 'ParameterAbstimmungsvorgangNeu.fxml'.";
        assert lblEnglisch != null : "fx:id=\"lblEnglisch\" was not injected: check your FXML file 'ParameterAbstimmungsvorgangNeu.fxml'.";
        assert tfKurzbezeichnungDE != null : "fx:id=\"tfKurzbezeichnungDE\" was not injected: check your FXML file 'ParameterAbstimmungsvorgangNeu.fxml'.";
        assert tfBezeichnungDE != null : "fx:id=\"tfBezeichnungDE\" was not injected: check your FXML file 'ParameterAbstimmungsvorgangNeu.fxml'.";
        assert tfBezeichnungEN != null : "fx:id=\"tfBezeichnungEN\" was not injected: check your FXML file 'ParameterAbstimmungsvorgangNeu.fxml'.";
        
        /****** Div. Felder aktivieren/deaktivieren/belegen ******/

        deaktiviereFelderBasis();
    }

    /************Oberflächen****************************************/

    /*++++Formulare TOP+++++++*/

    private void deaktiviereFelderBasis() {
        if (!ParamS.param.paramModuleKonfigurierbar.englischeAgenda) {
            lblEnglisch.setVisible(false);
            tfBezeichnungEN.setVisible(false);
        }
    }

    
    /*************************Reagieren auf Eingabe***************************************************/
    
    @FXML
    void clickedAbbrechen(ActionEvent event) {
        rc = false;
        eigeneStage.hide();
        return;
    }

    /**
     * Clicked neu.
     *
     * @param event the event
     */
    @FXML
    void clickedNeu(ActionEvent event) {
        boolean brc = speichern();
        if (brc == false) {
            return;
        }

        rc = true;
        eigeneStage.hide();
    }

    /***********************************Logiken*********************************/
    
    private boolean speichern() {

        EclAbstimmungsblock abstimmungsvorgang = new EclAbstimmungsblock();

        pruefeLaenge(tfKurzbezeichnungDE, "Kurzbezeichnung", 40);
        abstimmungsvorgang.kurzBeschreibung = tfKurzbezeichnungDE.getText();

        pruefeLaenge(tfBezeichnungDE, "Bezeichnung Deutsch", 80);
        abstimmungsvorgang.beschreibung = tfBezeichnungDE.getText();

        pruefeLaenge(tfBezeichnungEN, "Bezeichnung Englisch", 80);
        abstimmungsvorgang.beschreibungEN = tfBezeichnungEN.getText();

        if (!lFehlertext.isEmpty()) {
            fehlerMeldung(lFehlertext);
            return false;
        }

        abstimmungsvorgang.position = 0;
        abstimmungsvorgang.aktiv = 0;
        stubAbstimmungen.insertAbstimmVorgang(abstimmungsvorgang);

        return true;
    }

    /********Übergabewerte*/
    /**Return-Wert. true=ausführen, false=beenden*/
    public boolean rc = false;

    /** The stub abstimmungen. */
    private StubAbstimmungen stubAbstimmungen = null;

    /**
     * Inits the.
     *
     * @param pEigeneStage      the eigene stage
     * @param pStubAbstimmungen the stub abstimmungen
     */
    public void init(Stage pEigeneStage, StubAbstimmungen pStubAbstimmungen) {
        eigeneStage = pEigeneStage;
        stubAbstimmungen = pStubAbstimmungen;
    }

}
