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
import de.meetingapps.meetingclient.meetingClientDialoge.CtrlRoot;
import de.meetingapps.meetingclient.meetingClientOberflaechen.CbAllgemein;
import de.meetingapps.meetingclient.meetingClientOberflaechen.CbElement;
import de.meetingapps.meetingportal.meetComAllg.CaBug;
import de.meetingapps.meetingportal.meetComAllg.CaFehler;
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
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class CtrlParameterPresetsUmstellung extends CtrlRoot {

    private int logDrucken=10;
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
    private Button btnVeranstaltungstypAktivieren;

    @FXML
    private ComboBox<CbElement> cbSelektionAnzeige;
    private CbAllgemein cbAllgemeinSelektionAnzeige;

    @FXML
    private GridPane gpParameterGridPane;

    @FXML
    private TextField tfVeranstaltungstypAlt;

    @FXML
    private TextField tfVeranstaltungstypNeu;

    @FXML
    private Label lblSelektionAnzeige;

    @FXML
    private Label lblVeranstaltungstypAlt;

    @FXML
    private Label lblVeranstaltungstypNeu;


    private List<ParamStrukt> paramStrukt=null;

    private EclEmittenten emittent=null;
    
    private int selektion=1;
    
    @FXML
    void clickedVeranstaltungstypAktivieren(ActionEvent event) {
        
        for (ParamStrukt iParamStrukt: paramStrukt) {
            CheckBox lCheckBox=(CheckBox)(iParamStrukt.eingabeFeld.get(0));
            if (lCheckBox.isSelected()) {
                iParamStrukt.aufNeuenStandardWertUpdaten=true;
                iParamStrukt.wert=iParamStrukt.wertStandardNeu;
            }
            iParamStrukt.eingabeFeld=null;
        }
        
        DbBundle lDbBundle=new DbBundle();
        BlParamStrukt blParamStrukt=new BlParamStrukt(false, lDbBundle);
        int rc=blParamStrukt.speichereParameterFuerWechselVersammlungstyp(presetArt, veranstaltungstypNeu, paramStrukt, emittent);

        if (rc==CaFehler.pfXyWurdeVonAnderemBenutzerVeraendert) {
            CaZeigeHinweis caZeigeHinweise=new CaZeigeHinweis();
            caZeigeHinweise.zeige(eigeneStage, "Achtung, Parameter konnten möglicherweise nur unvollständig gespeichert werden, ggf. von anderem Benutzer verändert!");
        }

        umstellungWurdeDurchgefuehrt=true;
        eigeneStage.hide();
    }

    @FXML
    void initialize() {
        assert btnVeranstaltungstypAktivieren != null : "fx:id=\"btnVeranstaltungstypAktivieren\" was not injected: check your FXML file 'ParameterGrundeinstellungenUmstellung.fxml'.";
        assert cbSelektionAnzeige != null : "fx:id=\"cbSelektionAnzeige\" was not injected: check your FXML file 'ParameterGrundeinstellungenUmstellung.fxml'.";
        assert gpParameterGridPane != null : "fx:id=\"gpParameterGridPane\" was not injected: check your FXML file 'ParameterGrundeinstellungenUmstellung.fxml'.";
        assert tfVeranstaltungstypAlt != null : "fx:id=\"tfVeranstaltungstypAlt\" was not injected: check your FXML file 'ParameterGrundeinstellungenUmstellung.fxml'.";
        assert tfVeranstaltungstypNeu != null : "fx:id=\"tfVeranstaltungstypNeu\" was not injected: check your FXML file 'ParameterGrundeinstellungenUmstellung.fxml'.";

        if (aktivierenOderPruefen==2) {
            lblVeranstaltungstypAlt.setVisible(false);
            tfVeranstaltungstypAlt.setVisible(false);
            btnVeranstaltungstypAktivieren.setVisible(false);
        }

        cbAllgemeinSelektionAnzeige=new CbAllgemein(cbSelektionAnzeige);
        fuelleFilterSelektion();

        cbSelektionAnzeige.valueProperty().addListener(new ChangeListener<CbElement>() {
            @Override
            public void changed(@SuppressWarnings("rawtypes") ObservableValue ov, CbElement alterWert,
                    CbElement neuerWert) {
                if (neuerWert == null) {
                    return;
                }
                selektion=neuerWert.ident1;
                refreshAnzeigeParameterStruktur();
            }
        });



        for (int i=0;i<paramStruktVersammlungstypListe.get(presetArt).size();i++) {
            ParamStruktVersammlungstyp lParamStruktVersammlungstyp=paramStruktVersammlungstypListe.get(presetArt).get(i);
            if (lParamStruktVersammlungstyp.identVersammlungstyp==veranstaltungstypNeu) {
                String veranstaltungstext=Integer.toString(lParamStruktVersammlungstyp.identVersammlungstyp)
                        +" "
                        +lParamStruktVersammlungstyp.kurzText;
                tfVeranstaltungstypNeu.setText(veranstaltungstext);
            }
            if (lParamStruktVersammlungstyp.identVersammlungstyp==veranstaltungstypAlt) {
                String veranstaltungstext=Integer.toString(lParamStruktVersammlungstyp.identVersammlungstyp)
                        +" "
                        +lParamStruktVersammlungstyp.kurzText;
                tfVeranstaltungstypAlt.setText(veranstaltungstext);
            }

        }

        /*Parameter-Struktur einlesen*/
        DbBundle lDbBundle=new DbBundle();
        BlParamStrukt blParamStrukt=new BlParamStrukt(false, lDbBundle);
        blParamStrukt.leseParameterFuerWechselVersammlungstyp(presetArt, veranstaltungstypNeu);

        paramStrukt=blParamStrukt.paramStruktListe;
        emittent=blParamStrukt.emittent;

        if (aktivierenOderPruefen==1) {
            /*Parameter-Struktur mit Checkboxen füllen*/
            for (ParamStrukt iParamStrukt: paramStrukt) {
                CheckBox auswahlbox=new CheckBox();
                auswahlbox.setSelected(false);
                iParamStrukt.eingabeFeld=new LinkedList<Object>();
                iParamStrukt.eingabeFeld.add(auswahlbox);
            }
        }

        /*Parameter-Struktur anzeigen*/
        refreshAnzeigeParameterStruktur();

    }
    
    
    private void refreshAnzeigeParameterStruktur() {
        
        gpParameterGridPane.getChildren().clear();
        
        if (aktivierenOderPruefen==1) {
            Label lCheck=new Label("Auswahl");
            /*columnIndex, rowIndex, colSpan, rowSpan)*/
            gpParameterGridPane.add(lCheck, 0, 0, 1,1);
        }
        
        Label lParameterName=new Label("Parametername");
        gpParameterGridPane.add(lParameterName, 1, 0, 1,1);

        if (aktivierenOderPruefen==1) {
            Label lAlterStandardWert=new Label("Alter Standard Wert");
            gpParameterGridPane.add(lAlterStandardWert, 2, 0, 1,1);
            
            Label lAlterWert=new Label("Alter Wert");
            gpParameterGridPane.add(lAlterWert, 3, 0, 1,1);

            Label lNeuerWert=new Label("Neuer Wert");
            gpParameterGridPane.add(lNeuerWert, 4, 0, 1,1);

        }
        else {
            Label lAlterStandardWert=new Label("Standard Wert");
            gpParameterGridPane.add(lAlterStandardWert, 2, 0, 1,1);
            
            Label lAlterWert=new Label("Aktueller Wert");
            gpParameterGridPane.add(lAlterWert, 3, 0, 1,1);
            
            lblVeranstaltungstypNeu.setText("eingestellter Veranstaltungstyp");
        }
        
        

        int zeile=1;
        
        for (ParamStrukt iParamStrukt: paramStrukt) {
            boolean anzeigen=false;
            switch (selektion) {
            case 0:
                /*Alle*/
                anzeigen=true;
                break;
            case 1:
                /*nur zu verändernde Parameter*/
                if ((iParamStrukt.wert.equals(iParamStrukt.wertStandardNeu)==false && iParamStrukt.wertStandardNeuGefuellt!=-1) ) {
                    anzeigen=true;
                }
                break;
            case 2:
                /*nur zu verändernde Parameter, bei denen ursprünglicher Wert vom Standard abweicht*/
                if ((iParamStrukt.wert.equals(iParamStrukt.wertStandardNeu)==false  && iParamStrukt.wertStandardNeuGefuellt!=-1)
                    && iParamStrukt.wert.equals(iParamStrukt.wertStandard)==false) {
                    anzeigen=true;
                }
                break;
            }
            if (anzeigen) {
                /*columnIndex, rowIndex, colSpan, rowSpan)*/
                if (iParamStrukt.wertStandardNeuGefuellt!=-1) {
                    if (aktivierenOderPruefen==1) {
                        gpParameterGridPane.add((CheckBox)(iParamStrukt.eingabeFeld.get(0)), 0, zeile, 1,1);
                    }
                }
                
                Label lIName=new Label(iParamStrukt.bezeichnungVorEingabefeld);
                gpParameterGridPane.add(lIName, 1, zeile, 1,1);
                
                Label lIAlterStandardWert=new Label(iParamStrukt.wertStandard);
                gpParameterGridPane.add(lIAlterStandardWert, 2, zeile, 1,1);

                Label lIAlterWert=new Label(iParamStrukt.wert);
                gpParameterGridPane.add(lIAlterWert, 3, zeile, 1,1);

                Label lINeuerWert=new Label(iParamStrukt.wertStandardNeu);
                gpParameterGridPane.add(lINeuerWert, 4, zeile, 1,1);

                zeile++;
            }
        }
    }
    
    private void fuelleFilterSelektion() {

        cbSelektionAnzeige.getItems().removeAll(cbSelektionAnzeige.getItems());

        CbElement lElement0 = new CbElement();
        lElement0.anzeige = "Alle Parameter";
        lElement0.ident1 = 0;
        cbAllgemeinSelektionAnzeige.addElement(lElement0);

        CbElement lElement1 = new CbElement();
        lElement1.anzeige = "Alle zu verändernde Parameter";
        lElement1.ident1 = 1;
        cbAllgemeinSelektionAnzeige.addElement(lElement1);

        CbElement lElement2 = new CbElement();
        lElement2.anzeige = "Nur zu verändernde Parameter, die aktuell vom Standard abweichen";
        lElement2.ident1 = 2;
        cbAllgemeinSelektionAnzeige.addElement(lElement2);

        cbAllgemeinSelektionAnzeige.setAusgewaehlt1(1);
    }


    
    private Stage eigeneStage;
    
    private int veranstaltungstypNeu, veranstaltungstypAlt;
    
    private List<List<ParamStruktVersammlungstyp>> paramStruktVersammlungstypListe=null;
    private int aktivierenOderPruefen;
    private int presetArt;

    /**Ergebnis-Parameter*/
    public boolean umstellungWurdeDurchgefuehrt=false;
    
    /**
     * Inits the.
     *
     * @param pEigeneStage the eigene stage
     * 
     * pVeranstaltungstypNeu ist immer gefüllt und das "Ziel"-Preset.
     * pVeranstaltungstypAlt kann (beim überprüfen) -1 sein.
     */
    public void init(Stage pEigeneStage, int pAktivierenOderPruefen, int pPresetArt, int pVeranstaltungstypNeu, int pVeranstaltungstypAlt, List<List<ParamStruktVersammlungstyp>> pParamStruktVersammlungstypListe) {
        
        CaBug.druckeLog("pPresetArt="+pPresetArt+" pVeranstaltungstypNeu="+pVeranstaltungstypNeu+" pVeranstaltungstypAlt="+pVeranstaltungstypAlt, logDrucken, 10);
        
        umstellungWurdeDurchgefuehrt=false;
        
        aktivierenOderPruefen=pAktivierenOderPruefen;
        presetArt=pPresetArt;
        
        veranstaltungstypNeu=pVeranstaltungstypNeu;
        veranstaltungstypAlt=pVeranstaltungstypAlt;
        paramStruktVersammlungstypListe=pParamStruktVersammlungstypListe;

        eigeneStage = pEigeneStage;
    }

}
