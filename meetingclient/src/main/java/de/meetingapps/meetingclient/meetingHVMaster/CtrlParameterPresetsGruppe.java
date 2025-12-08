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

import de.meetingapps.meetingclient.meetingClientDialoge.CaZeigeHinweis;
import de.meetingapps.meetingclient.meetingClientDialoge.CtrlRootParameter;
import de.meetingapps.meetingportal.meetComDb.DbBundle;
import de.meetingapps.meetingportal.meetComHVParam.ParamS;
import de.meetingapps.meetingportal.meetComStub.StubParameter;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class CtrlParameterPresetsGruppe extends CtrlRootParameter {

    
    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private GridPane gpParameterGridPane;
    
    @FXML
    private Button btnBeendenOhneSpeichern;

    @FXML
    private Button btnSpeichern;

    
    
    @FXML
    void clickedBeendenOhneSpeichern(ActionEvent event) {
        boolean brc=pruefeObVeraenderungenVorhanden();
        if (brc) {
            CaZeigeHinweis caZeigeHinweis = new CaZeigeHinweis();
            caZeigeHinweis.zeige(eigeneStage, "Achtung, Änderungen durchgeführt. Diese Änderungen werden nicht gespeichert!");
        }
        eigeneStage.hide();
    }

    @FXML
    void clickedSpeichern(ActionEvent event) {
        int rc=speichereParamStruktGruppenListe();
        if (rc==1) {
            eigeneStage.hide();
        }

    }

    
    
    
    @FXML
    void initialize() {
        assert gpParameterGridPane != null : "fx:id=\"gpParameterGridPane\" was not injected: check your FXML file 'ParameterMitMenue.fxml'.";
        
        init(1);
        initParamStruktGruppen(0, gruppenNummer);
        initGridPane(0, gpParameterGridPane);
        anzeigeKomplettesGrid();
        btnSpeichern.setVisible(true);
        btnBeendenOhneSpeichern.setVisible(true);

    }

   

    
    
    private Stage eigeneStage;
    private int gruppenNummer;

    /**
     * Inits the.
     *
     * @param pEigeneStage the eigene stage
     */
    public void init(Stage pEigeneStage, int pGruppenNummer) {
        /** HV-Parameter sicherheitshalber neu holen */
        DbBundle lDbBundle = new DbBundle();
        StubParameter stubParameter = new StubParameter(false, lDbBundle);
        stubParameter.leseHVParam_all(lDbBundle.clGlobalVar.mandant, lDbBundle.clGlobalVar.hvJahr,
                lDbBundle.clGlobalVar.hvNummer, lDbBundle.clGlobalVar.datenbereich);
        ParamS.param = stubParameter.rcHVParam;

        eigeneStage = pEigeneStage;
        gruppenNummer=pGruppenNummer;
    }

}
