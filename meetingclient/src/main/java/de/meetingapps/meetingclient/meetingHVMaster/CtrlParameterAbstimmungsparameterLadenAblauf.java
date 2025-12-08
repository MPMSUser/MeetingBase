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

import de.meetingapps.meetingclient.meetingClientDialoge.CtrlRootParameter;
import de.meetingapps.meetingportal.meetComDb.DbBundle;
import de.meetingapps.meetingportal.meetComHVParam.ParamStruktAblaufHeader;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class CtrlParameterAbstimmungsparameterLadenAblauf extends CtrlRootParameter {

    /* Anzeige-Selektion (selektion)
     * > 0=Alle Parameter anzeigen
     * > 1=nur zu verändernde Parameter anzeigen
     * > 2=nur zu verändernde Parameter anzeigen, bei denen ursprünglicher Wert vom Standard abweicht
     * 
     * 
     * Anzeige im Grid:
     * > Selektions-Checkbox (nur anzeigen, wenn Veränderung des Parameter-Wertes vorgesehen)
     * > Parameter-Bezeichnung
     * > Alter Standard-Wert (nur falls vom Wert abweichend)
     * > Alter Wert
     * > neuer Standard-Wert
     * */
    

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    private Button[] btnAblauf = null;
 
    @FXML
    private GridPane gpParameterGridPane;


    private List<ParamStruktAblaufHeader> listeParamStruktAblaufHeader=null;

    @FXML
    void initialize() {
        assert gpParameterGridPane != null : "fx:id=\"gpParameterGridPane\" was not injected: check your FXML file 'ParameterGrundeinstellungenUmstellung.fxml'.";

        DbBundle lDbBundle=new DbBundle();
        lDbBundle.openAll();
        lDbBundle.openParamStrukt();
        
        lDbBundle.dbParamStruktAblaufHeader.readAlleFuerAuswahlAbstimmungsablauf();
        int anz=lDbBundle.dbParamStruktAblaufHeader.anzErgebnis();
        btnAblauf=new Button[anz];
        listeParamStruktAblaufHeader=lDbBundle.dbParamStruktAblaufHeader.ergebnis();
        if (anz>0) {
            for (int i=0;i<anz;i++) {
                ParamStruktAblaufHeader lParamStruktAblaufHeader=listeParamStruktAblaufHeader.get(i);
                btnAblauf[i]=new Button();
                btnAblauf[i].setText(lParamStruktAblaufHeader.kurzBeschreibung);
                btnAblauf[i].setOnAction(e -> {
                    clickedLaden(e);
                });
                gpParameterGridPane.add(btnAblauf[i], 0, i, 1,1);
                
                Label lLabel=new Label(lParamStruktAblaufHeader.beschreibung);
                gpParameterGridPane.add(lLabel, 1, i, 1,1);
                
            }
        }
        
        lDbBundle.closeAll();

 
//        /*Parameter-Struktur einlesen*/
//        DbBundle lDbBundle=new DbBundle();
//        BlParamStrukt blParamStrukt=new BlParamStrukt(false, lDbBundle);
//        blParamStrukt.leseParameterFuerWechselVersammlungstyp(veranstaltungstypNeu);
//
//        paramStrukt=blParamStrukt.paramStruktListe;
//        emittent=blParamStrukt.emittent;
//
//        /*Parameter-Struktur mit Checkboxen füllen*/
//        for (ParamStrukt iParamStrukt: paramStrukt) {
//            CheckBox auswahlbox=new CheckBox();
//            auswahlbox.setSelected(false);
//            iParamStrukt.eingabeFeld=new LinkedList<Object>();
//            iParamStrukt.eingabeFeld.add(auswahlbox);
//        }
//
//        /*Parameter-Struktur anzeigen*/
//        refreshAnzeigeParameterStruktur();

    }
    
    @FXML
    void clickedLaden(ActionEvent event) {

        for (int i = 0; i < btnAblauf.length; i++) {
            if (event.getSource() == btnAblauf[i]) {
                gewaehlterParamStruktAblaufHeader=listeParamStruktAblaufHeader.get(i);
                umstellungWurdeDurchgefuehrt=true;
                
            }
        }
        if (umstellungWurdeDurchgefuehrt) {
            gewaehlterAblaufListe=new LinkedList<Integer>();
            
            DbBundle lDbBundle=new DbBundle();
            lDbBundle.openAll();
            lDbBundle.openParamStrukt();

            int anz=lDbBundle.dbParamStruktAblaufElement.readAlleFuerAblaufHeaderIdent(gewaehlterParamStruktAblaufHeader.identAblauf);
            for (int i1=0;i1<anz;i1++) {
                Integer ablaufNr=lDbBundle.dbParamStruktAblaufElement.ergebnisPosition(i1).inhalt;
                gewaehlterAblaufListe.add(ablaufNr);
            }
            
            lDbBundle.closeAll();
            eigeneStage.hide();
            
        }
    }

    
    private Stage eigeneStage;
    

    /**Ergebnis-Parameter*/
    public boolean umstellungWurdeDurchgefuehrt=false;
    public ParamStruktAblaufHeader gewaehlterParamStruktAblaufHeader=null;
    public List<Integer> gewaehlterAblaufListe=null;
    /**
     * Inits the.
     *
     * @param pEigeneStage the eigene stage
     */
    public void init(Stage pEigeneStage) {
        
        eigeneStage = pEigeneStage;
        umstellungWurdeDurchgefuehrt=false;
    }

}
