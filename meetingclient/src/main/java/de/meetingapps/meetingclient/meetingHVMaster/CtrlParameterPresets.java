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

import de.meetingapps.meetingclient.meetingClientDialoge.CaController;
import de.meetingapps.meetingclient.meetingClientDialoge.CaIcon;
import de.meetingapps.meetingclient.meetingClientDialoge.CaZeigeHinweis;
import de.meetingapps.meetingclient.meetingClientDialoge.CtrlRoot;
import de.meetingapps.meetingclient.meetingClientOberflaechen.CbAllgemein;
import de.meetingapps.meetingclient.meetingClientOberflaechen.CbElement;
import de.meetingapps.meetingportal.meetComAllg.CaBug;
import de.meetingapps.meetingportal.meetComBl.BlParamStrukt;
import de.meetingapps.meetingportal.meetComDb.DbBundle;
import de.meetingapps.meetingportal.meetComHVParam.ParamS;
import de.meetingapps.meetingportal.meetComHVParam.ParamStruktPresetArt;
import de.meetingapps.meetingportal.meetComHVParam.ParamStruktVersammlungstyp;
import de.meetingapps.meetingportal.meetComStub.StubParameter;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class CtrlParameterPresets extends CtrlRoot {

    private int logDrucken=10;
    
    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button btnSpeichern;

    @FXML
    private Button[] btnVeranstaltungstypAktivieren;

    @FXML
    private Button[] btnVeranstaltungstypVergleichen;

    @FXML
    private Button[] btnVeranstaltungstypSpeichern;

    @FXML
    private ComboBox<CbElement>[] cbVeranstaltungstyp;
    private CbAllgemein[] cbAllgemeinVeranstaltungstyp;

    @FXML
    private GridPane gpParameterGridPane;

    @FXML
    private Label[] lbVeranstaltungsbeschreibung;

    
    private int[] veranstaltungstypNeu=new int[10];
    private int[] veranstaltungstypAlt=new int[10];
    
    private List<List<ParamStruktVersammlungstyp>> paramStruktVersammlungstypListe=null;
    
    private List<ParamStruktPresetArt> paramStruktPresetArtListe=null;
    private 
    

 
    @FXML
    void clickedSpeichern(ActionEvent event) {
        boolean brc=pruefeObVeraenderungenVorhanden();
        if (brc) {
            CaZeigeHinweis caZeigeHinweis = new CaZeigeHinweis();
            caZeigeHinweis.zeige(eigeneStage, "Achtung, Änderungen durchgeführt. Diese Änderungen werden nicht gespeichert!");
        }
        eigeneStage.hide();
    }

    
    @FXML
    void clickedButtonAusfuehren(ActionEvent event) {

        boolean veranstaltungstypAktivieren=false;
        boolean veranstaltungstypVergleichen=false;
        boolean veranstaltungstypSpeichern=false;
        
        int offsetPresetArt=-1;
        
        for (int i = 0; i < btnVeranstaltungstypAktivieren.length; i++) {
            if (event.getSource() == btnVeranstaltungstypAktivieren[i]) {
                offsetPresetArt = i;
                veranstaltungstypAktivieren=true;
            }
        }
        for (int i = 0; i < btnVeranstaltungstypVergleichen.length; i++) {
            if (event.getSource() == btnVeranstaltungstypVergleichen[i]) {
                offsetPresetArt = i;
                veranstaltungstypVergleichen=true;
            }
        }
        for (int i = 0; i < btnVeranstaltungstypSpeichern.length; i++) {
            if (event.getSource() == btnVeranstaltungstypSpeichern[i]) {
                offsetPresetArt = i;
                veranstaltungstypSpeichern=true;
            }
        }
        
        int presetArt=-1;
        if (offsetPresetArt!=-1) {
            presetArt=paramStruktPresetArtListe.get(offsetPresetArt).ident;
        }
        if (veranstaltungstypAktivieren) {
            doVeranstaltungstypAktivieren(presetArt);
            return;
        }
        if (veranstaltungstypVergleichen) {
            doVeranstaltungstypVergleichen(presetArt);
            return;
        }
        if (veranstaltungstypSpeichern) {
            doVeranstaltungstypSpeichern(presetArt);
            return;
        }
   }

    
    void doVeranstaltungstypAktivieren(int pPresetArt) {
        /*Sicherheitsabfrage stellen*/
        CaZeigeHinweis caZeigeHinweis=new CaZeigeHinweis();
        String veranstaltungsbezeichnungNeu="";
        for (int i=0;i<paramStruktVersammlungstypListe.get(pPresetArt).size();i++) {
            if (paramStruktVersammlungstypListe.get(pPresetArt).get(i).identVersammlungstyp==veranstaltungstypNeu[pPresetArt]) {
                veranstaltungsbezeichnungNeu=paramStruktVersammlungstypListe.get(pPresetArt).get(i).kurzText;
            }
        }
        String anzeigeText="Versammlungstyp auf "+veranstaltungstypNeu[pPresetArt]+" "+veranstaltungsbezeichnungNeu
                +" umstellen?";
        boolean brc=caZeigeHinweis.zeige2Buttons(eigeneStage, anzeigeText, "Weiter", "Abbruch");
        if (brc==false) {
            return;
        }
        
        /*Anzeige-Maske aufrufen*/
        Stage neuerDialog = new Stage();
        CaIcon.master(neuerDialog);
        CtrlParameterPresetsUmstellung controllerFenster = new CtrlParameterPresetsUmstellung();
        controllerFenster.init(neuerDialog, 1, pPresetArt, veranstaltungstypNeu[pPresetArt], veranstaltungstypAlt[pPresetArt], paramStruktVersammlungstypListe);

        CaController caController = new CaController();
        caController.open(neuerDialog, controllerFenster,
                "/de/meetingapps/meetingclient/meetingHVMaster/ParameterPresetsUmstellung.fxml", 1500, 760,
                "Umstellung auf anderen Versammlungstyp", true);
        
        /*Nun noch abfragen, ob Umstellung wirklich ausgeführt wurde - dann ggf. alles neu laden und neu anzeigen*/
        if (controllerFenster.umstellungWurdeDurchgefuehrt==false) {
            caZeigeHinweis=new CaZeigeHinweis();
            caZeigeHinweis.zeige(eigeneStage, "Veranstaltungstyp wurde NICHT umgestellt");
        }
        else {
            caZeigeHinweis=new CaZeigeHinweis();
            caZeigeHinweis.zeige(eigeneStage, "Veranstaltungstyp wurde umgestellt");
       }
        neuInit();
    }

    void doVeranstaltungstypVergleichen(int pPresetArt) {
        
        /*Anzeige-Maske aufrufen*/
        Stage neuerDialog = new Stage();
        CaIcon.master(neuerDialog);
        CtrlParameterPresetsUmstellung controllerFenster = new CtrlParameterPresetsUmstellung();
        controllerFenster.init(neuerDialog, 2, pPresetArt, veranstaltungstypNeu[pPresetArt], veranstaltungstypAlt[pPresetArt], paramStruktVersammlungstypListe);

        CaController caController = new CaController();
        caController.open(neuerDialog, controllerFenster,
                "/de/meetingapps/meetingclient/meetingHVMaster/ParameterPresetsUmstellung.fxml", 1500, 760,
                "Vergleich mit Standardtyp", true);
        
        neuInit();
    }

    
    
     void doVeranstaltungstypSpeichern(int pPresetArt) {
        
        /*Anzeige-Maske aufrufen*/
        Stage neuerDialog = new Stage();
        CaIcon.master(neuerDialog);
        CtrlParameterPresetsNeuerTyp controllerFenster = new CtrlParameterPresetsNeuerTyp();
        controllerFenster.init(neuerDialog, pPresetArt, paramStruktVersammlungstypListe);

        CaController caController = new CaController();
        caController.open(neuerDialog, controllerFenster,
                "/de/meetingapps/meetingclient/meetingHVMaster/ParameterPresetsNeuerTyp.fxml", 1500, 760,
                "Speichern neuer Versammlungstyp", true);
        
        /*Nun noch abfragen, ob Umstellung wirklich ausgeführt wurde - dann ggf. alles neu laden und neu anzeigen*/
        if (controllerFenster.umstellungWurdeDurchgefuehrt==false) {
            CaZeigeHinweis caZeigeHinweis=new CaZeigeHinweis();
            caZeigeHinweis.zeige(eigeneStage, "Veranstaltungstyp wurde NICHT gespeichert");
        }
        else {
            CaZeigeHinweis caZeigeHinweis=new CaZeigeHinweis();
            caZeigeHinweis.zeige(eigeneStage, "Veranstaltungstyp wurde gespeichert");
       }
        neuInit();

    }

    
    boolean pruefeObVeraenderungenVorhanden() {
        for (int i=0;i<10;i++) {
            if (veranstaltungstypNeu[i]!=-1 && veranstaltungstypNeu[i]!=veranstaltungstypAlt[i]) {
                return true;
            }
        }
        return false;
    }
    
    
    @FXML
    void initialize() {
        assert btnSpeichern != null : "fx:id=\"btnSpeichern\" was not injected: check your FXML file 'ParameterGrundeinstellungen.fxml'.";
        assert cbVeranstaltungstyp != null : "fx:id=\"cbVeranstaltungstyp\" was not injected: check your FXML file 'ParameterGrundeinstellungen.fxml'.";
        assert gpParameterGridPane != null : "fx:id=\"gpParameterGridPane\" was not injected: check your FXML file 'ParameterGrundeinstellungen.fxml'.";
        assert lbVeranstaltungsbeschreibung != null : "fx:id=\"lbVeranstaltungsbeschreibung\" was not injected: check your FXML file 'ParameterGrundeinstellungen.fxml'.";
        
        neuInit();
        
        eigeneStage.setOnCloseRequest(new EventHandler<WindowEvent>() {

            @Override
            public void handle(WindowEvent event) {
                boolean brc=pruefeObVeraenderungenVorhanden();
                if (brc) {
                    CaZeigeHinweis caZeigeHinweis = new CaZeigeHinweis();
                    caZeigeHinweis.zeige(eigeneStage, "Achtung, Änderungen durchgeführt. Diese Änderungen werden nicht gespeichert!");
                }
            }
        });
    }

    
    private void neuInit() {
        
        
        /** HV-Parameter sicherheitshalber neu holen */
        DbBundle lDbBundle = new DbBundle();
        StubParameter stubParameter = new StubParameter(false, lDbBundle);
        stubParameter.leseHVParam_all(lDbBundle.clGlobalVar.mandant, lDbBundle.clGlobalVar.hvJahr,
                lDbBundle.clGlobalVar.hvNummer, lDbBundle.clGlobalVar.datenbereich);
        ParamS.param = stubParameter.rcHVParam;

        
        for (int i=0;i<10;i++) {
            veranstaltungstypAlt[i]=ParamS.param.paramBasis.veranstaltungstyp[i];
            CaBug.druckeLog("veranstaltungstyp["+i+"]="+veranstaltungstypAlt[i], logDrucken, 10);
            veranstaltungstypNeu[i]=-1;
        }

        lesePresetArten();
        
        leseVeranstaltungstyp();
        
        zeigeGridMitPresetArtenAn();

    }
    
    @SuppressWarnings("unchecked")
    private void zeigeGridMitPresetArtenAn() {
        gpParameterGridPane.getChildren().clear();

       int zeilenProPresetArt=4;

        cbVeranstaltungstyp=new ComboBox[10];
        cbAllgemeinVeranstaltungstyp=new CbAllgemein[10];
        lbVeranstaltungsbeschreibung=new Label[10];
        btnVeranstaltungstypAktivieren=new Button[10];
        btnVeranstaltungstypVergleichen=new Button[10];
        btnVeranstaltungstypSpeichern=new Button[10];
        
        for (int i=0;i<paramStruktPresetArtListe.size();i++) {
            ParamStruktPresetArt lParamStruktPresetArt=paramStruktPresetArtListe.get(i);
            int presetArt=lParamStruktPresetArt.ident;
            
            int ausgewaehltVeranstaltungstypAlt=veranstaltungstypAlt[presetArt];
            
            Label presetArtKurz=new Label(lParamStruktPresetArt.kurzBezeichnung);
            presetArtKurz.setWrapText(true);
            presetArtKurz.setMinHeight(Region.USE_PREF_SIZE);
            /*columnIndex, rowIndex, colSpan, rowSpan)*/
            gpParameterGridPane.add(presetArtKurz, 0, 0+i*zeilenProPresetArt, 1, 1);

            Label presetArtBezeichnung=new Label(lParamStruktPresetArt.bezeichnung);
            presetArtBezeichnung.setWrapText(true);
            presetArtBezeichnung.setMinHeight(Region.USE_PREF_SIZE);
            /*columnIndex, rowIndex, colSpan, rowSpan)*/
            gpParameterGridPane.add(presetArtBezeichnung, 1, 0+i*zeilenProPresetArt, 2, 1);

            Label labelVerhaltungstyp=new Label("Veranstaltungstyp");
            labelVerhaltungstyp.setWrapText(true);
            labelVerhaltungstyp.setMinHeight(Region.USE_PREF_SIZE);
            /*columnIndex, rowIndex, colSpan, rowSpan)*/
            gpParameterGridPane.add(labelVerhaltungstyp, 0, 1+i*zeilenProPresetArt, 1, 1);

            
            
            lbVeranstaltungsbeschreibung[i]=new Label();
            lbVeranstaltungsbeschreibung[i].setWrapText(true);
            lbVeranstaltungsbeschreibung[i].setMinHeight(Region.USE_PREF_SIZE);
             /*columnIndex, rowIndex, colSpan, rowSpan)*/
            gpParameterGridPane.add(lbVeranstaltungsbeschreibung[i], 1, 2+i*zeilenProPresetArt, 2, 1);

            btnVeranstaltungstypAktivieren[i]=new Button();
            btnVeranstaltungstypAktivieren[i].setWrapText(true);
            btnVeranstaltungstypAktivieren[i].setMinHeight(Region.USE_PREF_SIZE);
            btnVeranstaltungstypAktivieren[i].setStyle("-fx-background-color: #ff0000;");

            btnVeranstaltungstypAktivieren[i].setText("Veranstaltungstyp aktivieren und ggf. Parameter auf Standardwerte stellen");
            btnVeranstaltungstypAktivieren[i].setOnAction(e -> {
                clickedButtonAusfuehren(e);
            });
            if (ausgewaehltVeranstaltungstypAlt==-1) {
                btnVeranstaltungstypAktivieren[i].setVisible(false);
            }
           
            /*columnIndex, rowIndex, colSpan, rowSpan)*/
           gpParameterGridPane.add(btnVeranstaltungstypAktivieren[i], 3, 0+i*zeilenProPresetArt, 1, 1);

           btnVeranstaltungstypVergleichen[i]=new Button();
           btnVeranstaltungstypVergleichen[i].setWrapText(true);
           btnVeranstaltungstypVergleichen[i].setMinHeight(Region.USE_PREF_SIZE);
           btnVeranstaltungstypVergleichen[i].setText("Parameter mit den Standardwerten des ausgewählten Versammlungstyps vergleichen");
           btnVeranstaltungstypVergleichen[i].setOnAction(e -> {
               clickedButtonAusfuehren(e);
           });
           if (ausgewaehltVeranstaltungstypAlt==-1) {
               btnVeranstaltungstypVergleichen[i].setVisible(false);
           }
           /*columnIndex, rowIndex, colSpan, rowSpan)*/
          gpParameterGridPane.add(btnVeranstaltungstypVergleichen[i], 3, 1+i*zeilenProPresetArt, 1, 1);

          btnVeranstaltungstypSpeichern[i]=new Button();
          btnVeranstaltungstypSpeichern[i].setWrapText(true);
          btnVeranstaltungstypSpeichern[i].setMinHeight(Region.USE_PREF_SIZE);
          btnVeranstaltungstypSpeichern[i].setText("Parameter als Standards eines neuen Veranstaltungstyp speichern");
          btnVeranstaltungstypSpeichern[i].setOnAction(e -> {
              clickedButtonAusfuehren(e);
          });
         /*columnIndex, rowIndex, colSpan, rowSpan)*/
         gpParameterGridPane.add(btnVeranstaltungstypSpeichern[i], 3, 2+i*zeilenProPresetArt, 1, 1);

            
            cbVeranstaltungstyp[i]=new ComboBox<CbElement>();
            cbAllgemeinVeranstaltungstyp[i]=new CbAllgemein(cbVeranstaltungstyp[i]);
            
            cbVeranstaltungstyp[i].valueProperty().addListener(new ChangeListener<CbElement>() {
                @Override
                public void changed(@SuppressWarnings("rawtypes") ObservableValue ov, CbElement alterWert,
                        CbElement neuerWert) {
                    if (neuerWert == null) {
                        return;
                    }
                    veranstaltungstypNeu[neuerWert.ident3]=neuerWert.ident1;
                    for (int i=0;i<paramStruktVersammlungstypListe.get(neuerWert.ident3).size();i++) {
                        ParamStruktVersammlungstyp iVersammlungstyp= paramStruktVersammlungstypListe.get(neuerWert.ident3).get(i);
                        if (iVersammlungstyp.identVersammlungstyp==neuerWert.ident1) {
                            lbVeranstaltungsbeschreibung[neuerWert.ident2].setText(iVersammlungstyp.beschreibung);
                        }
                    }
                    if (veranstaltungstypNeu[neuerWert.ident3]==veranstaltungstypAlt[neuerWert.ident3]) {
                        String tempStyle=btnVeranstaltungstypVergleichen[neuerWert.ident2].getStyle();;
                      btnVeranstaltungstypAktivieren[neuerWert.ident2].setText("Auf Standardwerte zurücksetzen");
                       btnVeranstaltungstypAktivieren[neuerWert.ident2].setStyle(tempStyle);
                    }
                    else
                    {
                        btnVeranstaltungstypAktivieren[neuerWert.ident2].setText("Veranstaltungstyp aktivieren und ggf. Parameter auf Standardwerte stellen");
                        btnVeranstaltungstypAktivieren[neuerWert.ident2].setStyle("-fx-background-color: #ff0000;");
                        btnVeranstaltungstypVergleichen[neuerWert.ident2].setVisible(true);
                    }
                }
            });
           
            fuelleFilterVeranstaltungstypFuerAnzeige(i, lParamStruktPresetArt.ident);
            /*columnIndex, rowIndex, colSpan, rowSpan)*/
            gpParameterGridPane.add(cbVeranstaltungstyp[i], 1, 1+i*zeilenProPresetArt, 2, 1);
           
        }

    }
    
    private void leseVeranstaltungstyp() {
        DbBundle lDbBundle=new DbBundle();
        paramStruktVersammlungstypListe=new LinkedList<List<ParamStruktVersammlungstyp>>();
        BlParamStrukt blParamStrukt=new BlParamStrukt(false, lDbBundle);
        for (int i=0;i<10;i++) {
            blParamStrukt.leseVersammlungstypen(i); 
            paramStruktVersammlungstypListe.add(blParamStrukt.paramStruktVersammlungstypListe);
       }
    }

    private void lesePresetArten() {
        DbBundle lDbBundle=new DbBundle();
        paramStruktPresetArtListe=new LinkedList<ParamStruktPresetArt>();
        BlParamStrukt blParamStrukt=new BlParamStrukt(false, lDbBundle);
        blParamStrukt.lesePresetArten();
        paramStruktPresetArtListe=blParamStrukt.paramStruktPresetArtListe;
    }

    private void fuelleFilterVeranstaltungstypFuerAnzeige(int pI, int pPresetArt) {

        int ausgewaehlt=-1;
        cbVeranstaltungstyp[pI].getItems().removeAll(cbVeranstaltungstyp[pI].getItems());

        if (paramStruktVersammlungstypListe.get(pPresetArt)!=null && paramStruktVersammlungstypListe.get(pPresetArt).size()>0) {
            for (int i=0;i<paramStruktVersammlungstypListe.get(pPresetArt).size();i++) {
                ParamStruktVersammlungstyp iVersammlungstyp= paramStruktVersammlungstypListe.get(pPresetArt).get(i);
                CbElement lElement = new CbElement();
                lElement.anzeige = iVersammlungstyp.kurzText;
                
                lElement.ident1 = iVersammlungstyp.identVersammlungstyp;//Ident des Versammlungstyps
                lElement.ident2=pI;//Laufende Nummer in der Anzeige
                lElement.ident3=pPresetArt;//Preset-Art
                
                cbAllgemeinVeranstaltungstyp[pI].addElement(lElement);
                
                if (lElement.ident1==veranstaltungstypAlt[pPresetArt]) {
                    ausgewaehlt=iVersammlungstyp.identVersammlungstyp;
//                    lbVeranstaltungsbeschreibung.setText(iVersammlungstyp.beschreibung);
//                    veranstaltungstypNeu[pI]=lElement.ident1;
                }
            }
        }
        CaBug.druckeLog("pI="+pI+" pPresetArt="+pPresetArt+" ausgewaehlt="+ausgewaehlt, logDrucken, 10);
        if (ausgewaehlt!=-1) {
            cbAllgemeinVeranstaltungstyp[pI].setAusgewaehlt1(ausgewaehlt);
        }
    }

    
    
    
    
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
