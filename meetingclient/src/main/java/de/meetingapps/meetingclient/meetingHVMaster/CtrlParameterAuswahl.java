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
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;

import de.meetingapps.meetingclient.meetingClientDialoge.CaZeigeHinweis;
import de.meetingapps.meetingclient.meetingClientDialoge.CtrlRootParameter;
import de.meetingapps.meetingportal.meetComBl.BlParamStrukt;
import de.meetingapps.meetingportal.meetComDb.DbBundle;
import de.meetingapps.meetingportal.meetComHVParam.ParamS;
import de.meetingapps.meetingportal.meetComHVParam.ParamStruktGruppenHeader;
import de.meetingapps.meetingportal.meetComStub.StubParameter;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.stage.Stage;

public class CtrlParameterAuswahl extends CtrlRootParameter {

    
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

   
    private List<Button> buttonListe=new LinkedList<Button>();
    
    private List<ParamStruktGruppenHeader> paramStruktGruppenHeaderListe=null;

    
    
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
        btnSpeichern.setVisible(false);
        btnBeendenOhneSpeichern.setVisible(false);
        zeigeMenueAn();
    }

   
    private void zeigeMenueAn() {
        DbBundle lDbBundle=new DbBundle();
        
        BlParamStrukt blParamStrukt=new BlParamStrukt(false, lDbBundle);
        blParamStrukt.leseGruppenheaderFuerGruppenAuswahl();
        
        paramStruktGruppenHeaderListe=blParamStrukt.paramStruktGruppenHeaderListe;
        gpParameterGridPane.getChildren().clear();
        
        if (paramStruktGruppenHeaderListe!=null && paramStruktGruppenHeaderListe.size()>0) {
            int zeile=0;
            for (int i=0;i<paramStruktGruppenHeaderListe.size();i++) {
                ParamStruktGruppenHeader lParamStruktGruppenHeader=paramStruktGruppenHeaderListe.get(i);
                
                Button lButton=new Button();
                lButton.setText(lParamStruktGruppenHeader.buttonBeschriftung);
                
                lButton.setOnAction(e -> {
                    clickedAuswaehlen(e);
                });
                buttonListe.add(lButton);
                
                /*columnIndex, rowIndex, colSpan, rowSpan)*/
                gpParameterGridPane.add(lButton, 0, zeile, 1, 1);
                 
                Label lLabel=new Label(lParamStruktGruppenHeader.beschreibung);
                lLabel.setWrapText(true);
                lLabel.setMinHeight(Region.USE_PREF_SIZE);
                gpParameterGridPane.add(lLabel, 1, zeile, 3, 1);

                zeile++;

            }
        }
        
    }
    
    
    
    @FXML
    void clickedAuswaehlen(ActionEvent event) {

        int ausgewaehltesMenueOffset=-1;
        for (int i = 0; i < buttonListe.size(); i++) {
            if (event.getSource() == buttonListe.get(i)) {
                ausgewaehltesMenueOffset = i;
            }
        }
        
        if (ausgewaehltesMenueOffset!=-1) {
            init(1);
            initParamStruktGruppen(0, paramStruktGruppenHeaderListe.get(ausgewaehltesMenueOffset).identGruppe);
            initGridPane(0, gpParameterGridPane);
            anzeigeKomplettesGrid();
            btnSpeichern.setVisible(true);
            btnBeendenOhneSpeichern.setVisible(true);
        }

    }

    
    
    private Stage eigeneStage;

    /**
     * Inits the.
     *
     * @param pEigeneStage the eigene stage
     */
    public void init(Stage pEigeneStage) {
        /** HV-Parameter sicherheitshalber neu holen */
        DbBundle lDbBundle = new DbBundle();
        StubParameter stubParameter = new StubParameter(false, lDbBundle);
        stubParameter.leseHVParam_all(lDbBundle.clGlobalVar.mandant, lDbBundle.clGlobalVar.hvJahr,
                lDbBundle.clGlobalVar.hvNummer, lDbBundle.clGlobalVar.datenbereich);
        ParamS.param = stubParameter.rcHVParam;

        eigeneStage = pEigeneStage;
    }

}
