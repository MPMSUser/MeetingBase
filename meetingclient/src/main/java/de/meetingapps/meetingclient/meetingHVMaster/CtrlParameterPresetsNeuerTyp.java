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
import java.util.List;
import java.util.ResourceBundle;

import de.meetingapps.meetingclient.meetingClientDialoge.CaZeigeHinweis;
import de.meetingapps.meetingclient.meetingClientDialoge.CtrlRoot;
import de.meetingapps.meetingclient.meetingClientOberflaechen.CbAllgemein;
import de.meetingapps.meetingclient.meetingClientOberflaechen.CbElement;
import de.meetingapps.meetingportal.meetComBl.BlParamStrukt;
import de.meetingapps.meetingportal.meetComDb.DbBundle;
import de.meetingapps.meetingportal.meetComEntities.EclEmittenten;
import de.meetingapps.meetingportal.meetComHVParam.ParamStrukt;
import de.meetingapps.meetingportal.meetComHVParam.ParamStruktVersammlungstyp;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class CtrlParameterPresetsNeuerTyp extends CtrlRoot {

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

    @FXML
    private Button btnVeranstaltungstypSpeichern;

    @FXML
    private ComboBox<CbElement> cbVererbenVon;
    private CbAllgemein cbAllgemeinVererbenVon;

    @FXML
    private Label lblSelektionAnzeige;

    @FXML
    private Label lblVeranstaltungstypAlt;

    @FXML
    private Label lblVeranstaltungstypNeu;

    @FXML
    private TextField tfBeschreibungKurz;

    @FXML
    private TextArea tfBeschreibungLang;



    private List<ParamStrukt> paramStrukt=null;

    private EclEmittenten emittent=null;
    
    private int vererbenVon=-1;
    
    
    
    @FXML
    void clickedVeranstaltungstypSpeichern(ActionEvent event) {
        String beschreibungKurz=tfBeschreibungKurz.getText();
        if (beschreibungKurz.isEmpty()) {
            CaZeigeHinweis caZeigeHinweis=new CaZeigeHinweis();
            caZeigeHinweis.zeige(eigeneStage, "Bitte Kurz-Beschreibung eingeben!");
            return;
        }
        String beschreibungLang=tfBeschreibungLang.getText();
        if (beschreibungLang.isEmpty()) {
            CaZeigeHinweis caZeigeHinweis=new CaZeigeHinweis();
            caZeigeHinweis.zeige(eigeneStage, "Bitte Lang-Beschreibung eingeben!");
            return;
        }
        
        DbBundle lDbBundle=new DbBundle();
        BlParamStrukt blParamStrukt=new BlParamStrukt(false, lDbBundle);
        int rc=blParamStrukt.speichereNeuenVersammlungstyp(presetArt, beschreibungKurz, beschreibungLang, vererbenVon);
        
        umstellungWurdeDurchgefuehrt=true;
        eigeneStage.hide();
       
    }

    

    @FXML
    void initialize() {
        assert btnVeranstaltungstypSpeichern != null : "fx:id=\"btnVeranstaltungstypSpeichern\" was not injected: check your FXML file 'ParameterGrundeinstellungenNeuerTyp.fxml'.";
        assert cbVererbenVon != null : "fx:id=\"cbVererbenVon\" was not injected: check your FXML file 'ParameterGrundeinstellungenNeuerTyp.fxml'.";
        assert lblSelektionAnzeige != null : "fx:id=\"lblSelektionAnzeige\" was not injected: check your FXML file 'ParameterGrundeinstellungenNeuerTyp.fxml'.";
        assert lblVeranstaltungstypAlt != null : "fx:id=\"lblVeranstaltungstypAlt\" was not injected: check your FXML file 'ParameterGrundeinstellungenNeuerTyp.fxml'.";
        assert lblVeranstaltungstypNeu != null : "fx:id=\"lblVeranstaltungstypNeu\" was not injected: check your FXML file 'ParameterGrundeinstellungenNeuerTyp.fxml'.";
        assert tfBeschreibungKurz != null : "fx:id=\"tfBeschreibungKurz\" was not injected: check your FXML file 'ParameterGrundeinstellungenNeuerTyp.fxml'.";
        assert tfBeschreibungLang != null : "fx:id=\"tfBeschreibungLang\" was not injected: check your FXML file 'ParameterGrundeinstellungenNeuerTyp.fxml'.";



        cbAllgemeinVererbenVon=new CbAllgemein(cbVererbenVon);
        fuelleFilterVererbenVon();

        cbVererbenVon.valueProperty().addListener(new ChangeListener<CbElement>() {
            @Override
            public void changed(@SuppressWarnings("rawtypes") ObservableValue ov, CbElement alterWert,
                    CbElement neuerWert) {
                if (neuerWert == null) {
                    return;
                }
                vererbenVon=neuerWert.ident1;
            }
        });



    }
    
    
    
    private void fuelleFilterVererbenVon() {

        cbVererbenVon.getItems().removeAll(cbVererbenVon.getItems());

        CbElement lElementNichts = new CbElement();
        lElementNichts.anzeige = "keine Vererbung";
        lElementNichts.ident1 = -1;
        cbAllgemeinVererbenVon.addElement(lElementNichts);

        
        
        if (paramStruktVersammlungstypListe!=null && paramStruktVersammlungstypListe.size()>0) {
            for (int i=0;i<paramStruktVersammlungstypListe.get(presetArt).size();i++) {
                ParamStruktVersammlungstyp iVersammlungstyp= paramStruktVersammlungstypListe.get(presetArt).get(i);
                CbElement lElement = new CbElement();
                lElement.anzeige = iVersammlungstyp.kurzText;
                lElement.ident1 = iVersammlungstyp.identVersammlungstyp;
                cbAllgemeinVererbenVon.addElement(lElement);
            }
        }
        cbAllgemeinVererbenVon.setAusgewaehlt1(-1);
    }


    
    private Stage eigeneStage;
    private int presetArt;
    private List<List<ParamStruktVersammlungstyp>> paramStruktVersammlungstypListe=null;

    /**Ergebnis-Parameter*/
    public boolean umstellungWurdeDurchgefuehrt=false;
    
    /**
     * Inits the.
     *
     * @param pEigeneStage the eigene stage
     */
    public void init(Stage pEigeneStage, int pPresetArt, List<List<ParamStruktVersammlungstyp>> pParamStruktVersammlungstypListe) {
        
        umstellungWurdeDurchgefuehrt=false;
        presetArt=pPresetArt;
        
        paramStruktVersammlungstypListe=pParamStruktVersammlungstypListe;

        eigeneStage = pEigeneStage;
    }

}
