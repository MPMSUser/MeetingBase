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

import java.util.LinkedList;
import java.util.List;

import de.meetingapps.meetingclient.meetingClientOberflaechen.CbAllgemein;
import de.meetingapps.meetingclient.meetingClientOberflaechen.CbElement;
import de.meetingapps.meetingportal.meetComAllg.CaBug;
import de.meetingapps.meetingportal.meetComAllg.CaFehler;
import de.meetingapps.meetingportal.meetComAllg.CaString;
import de.meetingapps.meetingportal.meetComBl.BlParamStrukt;
import de.meetingapps.meetingportal.meetComDb.DbBundle;
import de.meetingapps.meetingportal.meetComEntities.EclEmittenten;
import de.meetingapps.meetingportal.meetComHVParam.KonstParameter;
import de.meetingapps.meetingportal.meetComHVParam.ParamS;
import de.meetingapps.meetingportal.meetComHVParam.ParamStrukt;
import de.meetingapps.meetingportal.meetComHVParam.ParamStruktGruppen;
import de.meetingapps.meetingportal.meetComHVParam.ParamStruktGruppenHeader;
import de.meetingapps.meetingportal.meetComHVParam.ParamStruktWerte;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

public class CtrlRootParameter extends CtrlRoot {

    int logDrucken=10;
    
    /**1 => wird nicht in Tabs angezeigt => Überschrift und Selektionsfeld in Zeile 1 des Grids*/
    protected int anzahlTabs=1;
    
    protected GridPane[] gpGridPane=null;;
    protected ParamStruktGruppenHeader paramStruktGruppe[]=null;
    protected EclEmittenten emittent=null;
    
    
    /**true => cbSelektion wird hier auatomatisch und neu initialisiert
     * false => cbSelektion wurde mit einem Feld vorbelegt
     */
    private boolean cbSelektionLokalBelegt=true;
    protected ComboBox<CbElement> cbSelektion=null;
    protected CbAllgemein cbAllgemeinSelektion=null;

    /**0 => nichts spezifiziert;
     *      bei Anzeige von Gruppen in Tabs (mehrere Gruppen) wird 
     *      ParamStruktGruppenHeader.tabBeschriftung in den Tabs angezeigt,
     *      
     *      bei Anzeige nicht in Tabs (nur eine Gruppe) wird paramStruktGruppenHeader.ueberschrift in der
     *      ersten Zeile des Grids links angezeigt
     */
    private int anzeigeDerGruppenueberschrift=0;
    
    /**0 = Greenhorn
     * 1 = Consultant
     * 2 = Standard-Abweichungen
     * 3 = Experte
     * 4 = Analyse
     */
    protected int anzeigeModus=1;
    
    /**1 => erste Zeile wird durch Überschrift und/oder Selektion belegt*/
    private int gridZeilenOffset=0;
    
    /**pAnzahlTabs==1 => keine Anzeige in Tabs*/
    @SuppressWarnings("unchecked")
    protected void init(int pAnzahlTabs) {
        /*"Standard"*/
        anzeigeModus=1;
        anzeigeDerGruppenueberschrift=0;
        gridZeilenOffset=0;
        
        anzahlTabs=pAnzahlTabs;

        paramStruktGruppe=new ParamStruktGruppenHeader[pAnzahlTabs];
        gpGridPane=new GridPane[pAnzahlTabs];
        emittent=null;
    }
    
    /**Offset beginnt bei 0*/
    protected void initParamStruktGruppen(int pOffset, int pIdentGruppe) {
        
        DbBundle lDbBundle=new DbBundle();
        BlParamStrukt blParamStrukt=new BlParamStrukt(false, lDbBundle);
        blParamStrukt.leseParameterGruppeZumBearbeiten(pIdentGruppe);
        
        paramStruktGruppe[pOffset]=blParamStrukt.paramStruktGruppeHeader;
        if (emittent==null) {
            CaBug.druckeLog("emittent==null", logDrucken, 10);
            emittent=blParamStrukt.emittent;
            if (emittent==null) {
                CaBug.druckeLog("Emittent ist immer noch null", logDrucken, 10);
            }
            
        }
    }
    
    
    
    protected void initGridPane(int pOffset, GridPane pGgpParameterGridPane) {
        gpGridPane[pOffset]=pGgpParameterGridPane;
        
    }
    
    /**Wenn mit Combo-Box initialisiert wird, dann wird dieses für das Selektionsfeld verwendet.
     * Wenn keine Initialisierung erfolgt, wird ein neues Feld angelegt und in der ersten Zeile des
     * Grids angezeigt (nur zulässig, anzahlTab==1)
     */
    protected void initCbSelektion(ComboBox<CbElement> pCbSelektion) {
        cbSelektion=pCbSelektion;
        cbSelektionLokalBelegt=false;
    }
    
    protected void anzeigeKomplettesGrid() {
        fuelleCbSelektion();

        clearGrids();
        
        erzeugeEingabefelder();
        
        refreshGrids();

    }
    
    private void clearGrids() {
        for (int i=0;i<anzahlTabs;i++) {
            gpGridPane[i].getChildren().clear();
        }
    }

    
    private void erzeugeEingabefelder(){
        for (int i=0;i<anzahlTabs;i++) {
            ParamStruktGruppenHeader lParamStruktGruppenHeader=paramStruktGruppe[i];
            List<ParamStruktGruppen> lParamStruktGruppenelemente=lParamStruktGruppenHeader.paramStruktGruppenelemente;
            for (ParamStruktGruppen iParamStruktGruppen: lParamStruktGruppenelemente) {
                if (iParamStruktGruppen.elementTyp==0) {
                    ParamStrukt lParamStrukt=iParamStruktGruppen.paramStrukt;
                    lParamStrukt.eingabeFeld=new LinkedList<Object>();
                    switch (lParamStrukt.wertIstTyp) {
                    
                    case ParamStrukt.IST_TYP_INTEGER_ALS_EINGABEFELD:
                    case ParamStrukt.IST_TYP_STRING_ALS_EINGABEFELD:
                        TextField tfTextField=new TextField();
                        tfTextField.setText(lParamStrukt.wert);
                        lParamStrukt.eingabeFeld.add(tfTextField);
                        break;
                        
                    case ParamStrukt.IST_TYP_BOOLEAN:
                        CheckBox cbEingabe=new CheckBox();
                        if (lParamStrukt.wert.equals("1")) {
                            cbEingabe.setSelected(true);
                        }
                        else {
                            cbEingabe.setSelected(false);
                        }
                        lParamStrukt.eingabeFeld.add(cbEingabe);
                        break;
                        
                    case ParamStrukt.IST_TYP_STRING_ALS_RADIO_BUTTONS:
                        if (lParamStrukt.struktWerte!=null && lParamStrukt.struktWerte.size()>0) {
                            ToggleGroup toggleGroup=new ToggleGroup();
                            for (int i1=0;i1<lParamStrukt.struktWerte.size();i1++) {
                                RadioButton radioButton=new RadioButton(lParamStrukt.struktWerte.get(i1).kurztext);
                                radioButton.setToggleGroup(toggleGroup);
                                if (lParamStrukt.struktWerte.get(i1).zulaessigeWerteString.equals(lParamStrukt.wert)) {
                                    radioButton.setSelected(true);
                                }
                                lParamStrukt.eingabeFeld.add(radioButton);
                            }
                        }
                        break;
                    case ParamStrukt.IST_TYP_INTEGER_ALS_VERKNUEPFUNGSFELD:
                        if (lParamStrukt.struktWerte!=null && lParamStrukt.struktWerte.size()>0) {
                            for (int i1=0;i1<lParamStrukt.struktWerte.size();i1++) {
                                CheckBox checkBox=new CheckBox(lParamStrukt.struktWerte.get(i1).kurztext);
                                int zulaessigerWert=lParamStrukt.struktWerte.get(i1).zulaessigeWerteInt;
                                CaBug.druckeLog("i1="+i1+" zulaessigerWert="+zulaessigerWert+" lParamStrukt.wert="+lParamStrukt.wert, logDrucken, 10);
                                if (lParamStrukt.wert.isEmpty()==false) {
                                    if ((zulaessigerWert & Integer.parseInt(lParamStrukt.wert))==zulaessigerWert) {
                                        checkBox.setSelected(true);
                                    }
                                }
                                lParamStrukt.eingabeFeld.add(checkBox);
                            }
                        }
                        break;
                    case ParamStrukt.IST_TYP_INTEGER_ALS_DROP_DOWN:
                        if (lParamStrukt.struktWerte!=null && lParamStrukt.struktWerte.size()>0) {
                            int lWert=-1;
                            if (lParamStrukt.wert.isEmpty()==false) {
                                lWert=Integer.parseInt(lParamStrukt.wert);
                            }
                            ComboBox<CbElement> comboBox=new ComboBox<CbElement>();
                            CbAllgemein comboAllgemeinBox=new CbAllgemein(comboBox);
                            if (lParamStrukt.struktWerte!=null) {
                                for (int i1=0;i1<lParamStrukt.struktWerte.size();i1++) {
                                    ParamStruktWerte lParamStruktWert=lParamStrukt.struktWerte.get(i1);
                                    CbElement cbElement=new CbElement();
                                    cbElement.ident1=lParamStruktWert.zulaessigeWerteInt;
                                    cbElement.anzeige=lParamStruktWert.kurztext+" ("+Integer.toString(cbElement.ident1)+")";
                                    comboAllgemeinBox.addElement(cbElement);
                                }
                                comboAllgemeinBox.setAusgewaehlt1(lWert);
                            }
                            lParamStrukt.eingabeFeld.add(comboBox);
                        }

                        break;
                    }
                }
            }
        }
    }
    
    private void refreshGrids() {
        CaBug.druckeLog(lFehlertext, logDrucken, 10);
//        gpGridPane[0].setGridLinesVisible(true);
        
        RowConstraints rowConstraints = new RowConstraints(
                Region.USE_COMPUTED_SIZE,             // minimale Höhe
                Region.USE_COMPUTED_SIZE,             // bevorzugte Höhe
                Region.USE_COMPUTED_SIZE,             // maximale Höhe
                Priority.ALWAYS, // vertikale Ausdehnung
                VPos.CENTER,     // vertikale Anordnung
                true);           // vertikale Füllung
 
        gpGridPane[0].setHgap(15); 
        gpGridPane[0].setVgap(15);
        
        /*Oben, Rechts, unten, Links*/
//        gpGridPane[0].setPadding(new Insets(10, 10, 10, 10)); 
           
        
        /**Selektionsfeld anzeigen*/
        if (cbSelektionLokalBelegt==true) {
            gridZeilenOffset=1;
            gpGridPane[0].add(cbSelektion, 2, 0);
        }
        
        /**Überschrift anzeigen*/
        if (anzahlTabs==1 && anzeigeDerGruppenueberschrift==0) {
            /*Gruppenüberschrift in erster Zeile des Grids anzeigen*/
            gridZeilenOffset=1;
            Label lUeberschrift=new Label();
            lUeberschrift.setText(ersetzeVariablenInString(paramStruktGruppe[0].ueberschrift));
            lUeberschrift.setWrapText(true);
//            lUeberschrift.setPrefWidth(100);
            lUeberschrift.setMinHeight(Region.USE_PREF_SIZE);
//            lUeberschrift.prefWidthProperty().bind(gpGridPane[0].widthProperty());
            /*columnIndex, rowIndex, colSpan, rowSpan)*/
            gpGridPane[0].add(lUeberschrift, 0, 0, 2, 1);

        }
        
        int lfdZeile=gridZeilenOffset;
        int lfdZeileVorherigesElement=0;
        boolean zeilenStartWurdeAusgeblendet=false;
        
        for (int i=0;i<anzahlTabs;i++) {
            ParamStruktGruppenHeader lParamStruktGruppenHeader=paramStruktGruppe[i];
            List<ParamStruktGruppen> lParamStruktGruppenelemente=lParamStruktGruppenHeader.paramStruktGruppenelemente;
            for (ParamStruktGruppen iParamStruktGruppen: lParamStruktGruppenelemente) {
                
                boolean anzeigeInSelberZeileWieVorgaenger=false;
                int anzeigeInZeile=lfdZeile;
                int anzeigeSpalteFuerEingabefeld=1;
                if (iParamStruktGruppen.elementHorizontalAnordnen==1) {
                    anzeigeInSelberZeileWieVorgaenger=true;
                    anzeigeInZeile=lfdZeileVorherigesElement;
                    anzeigeSpalteFuerEingabefeld=iParamStruktGruppen.anzeigeInSpalte;
                }
                else {
                    zeilenStartWurdeAusgeblendet=false;
                }
                boolean elementWurdeAngezeigt=false;
                
               switch (iParamStruktGruppen.elementTyp) {
               case 0:
                   if (pruefeAnzeigenEingabezeile(iParamStruktGruppen)==true) {
                       
                       if (zeilenStartWurdeAusgeblendet==true && anzeigeInSelberZeileWieVorgaenger==true) {
                           /**Dann Zeilenvorschub durchführen*/
                           anzeigeInZeile=lfdZeile;
                           lfdZeile++;
                           lfdZeileVorherigesElement++;
                           
                           zeilenStartWurdeAusgeblendet=false;
                       }
                       
                       int spanFuerEingabefeld=1;
                       boolean bezeichnungNachEingabefeldInNaechsteZeile=false;
                       if ((iParamStruktGruppen.darstellungsAttribut & 1)==1) {
                           spanFuerEingabefeld=3;
                           bezeichnungNachEingabefeldInNaechsteZeile=true;
                       }
                       /*Eingabeparameter*/
                       ParamStrukt lParamStrukt=iParamStruktGruppen.paramStrukt;
                       switch (lParamStrukt.wertIstTyp) {

                       case ParamStrukt.IST_TYP_INTEGER_ALS_EINGABEFELD:
                       case ParamStrukt.IST_TYP_STRING_ALS_EINGABEFELD:
                           /*columnIndex, rowIndex, colSpan, rowSpan)*/
                           gpGridPane[0].add((TextField)lParamStrukt.eingabeFeld.get(0), anzeigeSpalteFuerEingabefeld, anzeigeInZeile, spanFuerEingabefeld, 1);
                           break;

                       case ParamStrukt.IST_TYP_BOOLEAN:
                           /*columnIndex, rowIndex, colSpan, rowSpan)*/
                           gpGridPane[0].add((CheckBox)lParamStrukt.eingabeFeld.get(0), anzeigeSpalteFuerEingabefeld, anzeigeInZeile, spanFuerEingabefeld, 1);
                           break;
                       case ParamStrukt.IST_TYP_STRING_ALS_RADIO_BUTTONS:
                           if ((iParamStruktGruppen.darstellungsAttribut & 4) ==4) {
                               /*Radiobuttons nebeneinander*/
                               HBox hBox=new HBox();
                               for (int i1=0;i1<lParamStrukt.eingabeFeld.size();i1++) {
                                   RadioButton radioButton=(RadioButton)lParamStrukt.eingabeFeld.get(i1);
                                   radioButton.setPadding(new Insets(0, 20, 0, 0));
                                   hBox.getChildren().add(radioButton);
                               }
                               spanFuerEingabefeld=3;
                               bezeichnungNachEingabefeldInNaechsteZeile=true;
                               gpGridPane[0].add(hBox, anzeigeSpalteFuerEingabefeld, anzeigeInZeile, spanFuerEingabefeld, 1);
                           }
                           else {
                               /*Radiobuttons untereinander*/
                               VBox vBox=new VBox();
                               for (int i1=0;i1<lParamStrukt.eingabeFeld.size();i1++) {
                                   RadioButton radioButton=(RadioButton)lParamStrukt.eingabeFeld.get(i1);
                                   radioButton.setPadding(new Insets(2, 0, 2, 0));
                                   vBox.getChildren().add(radioButton);
                               }
                               gpGridPane[0].add(vBox, anzeigeSpalteFuerEingabefeld, anzeigeInZeile, spanFuerEingabefeld, 1);
                           }
                           break;
                       case ParamStrukt.IST_TYP_INTEGER_ALS_VERKNUEPFUNGSFELD:
                           if ((iParamStruktGruppen.darstellungsAttribut & 4) ==4) {
                               /*Checkbox nebeneinander*/
                               HBox hBox=new HBox();
                               for (int i1=0;i1<lParamStrukt.eingabeFeld.size();i1++) {
                                   CheckBox checkBox=(CheckBox)lParamStrukt.eingabeFeld.get(i1);
                                   checkBox.setPadding(new Insets(0, 20, 0, 0));
                                   hBox.getChildren().add(checkBox);
                               }
                               spanFuerEingabefeld=3;
                               bezeichnungNachEingabefeldInNaechsteZeile=true;
                               gpGridPane[0].add(hBox, anzeigeSpalteFuerEingabefeld, anzeigeInZeile, spanFuerEingabefeld, 1);
                           }
                           else {
                               /*Checkbox untereinander*/
                               VBox vBox=new VBox();
//                               vBox.setMinHeight(Region.USE_COMPUTED_SIZE);
                               for (int i1=0;i1<lParamStrukt.eingabeFeld.size();i1++) {
                                   CheckBox checkBox=(CheckBox)lParamStrukt.eingabeFeld.get(i1);
                                   checkBox.setPadding(new Insets(2, 0, 2, 0));
                                   vBox.getChildren().add(checkBox);
                               }
                               gpGridPane[0].add(vBox, anzeigeSpalteFuerEingabefeld, anzeigeInZeile, spanFuerEingabefeld, 1);
                           }
                           break;
                       case ParamStrukt.IST_TYP_INTEGER_ALS_DROP_DOWN:
                           /*columnIndex, rowIndex, colSpan, rowSpan)*/
                           gpGridPane[0].add((ComboBox<CbElement>)lParamStrukt.eingabeFeld.get(0), anzeigeSpalteFuerEingabefeld, anzeigeInZeile, spanFuerEingabefeld, 1);
                           break;
                       }
                       
                       /*Beschreibung anzeigen*/
                       if (lParamStrukt.bezeichnungVorEingabefeld.isEmpty()==false 
                               && lParamStrukt.bezeichnungVorEingabefeld.equals("null")==false 
                               && anzeigeInSelberZeileWieVorgaenger==false) {
                           Label lBezeichnungVorEingabefeld=new Label();
                           lBezeichnungVorEingabefeld.setText(ersetzeVariablenInString(lParamStrukt.bezeichnungVorEingabefeld));
                           lBezeichnungVorEingabefeld.setWrapText(true);
                           lBezeichnungVorEingabefeld.setMinHeight(Region.USE_PREF_SIZE);
                           if ((iParamStruktGruppen.darstellungsAttribut & 2) ==2) {
                               lBezeichnungVorEingabefeld.setStyle("-fx-font-weight: bold");
                           }
                           if (standardWeichtAb) {
                               lBezeichnungVorEingabefeld.setTextFill(Color.DARKRED);
                           }
                           /*columnIndex, rowIndex, colSpan, rowSpan)*/
                           gpGridPane[0].add(lBezeichnungVorEingabefeld, 0, anzeigeInZeile, 1, 1);
                           if (lParamStrukt.hilfetext.isEmpty()==false) {
                               lBezeichnungVorEingabefeld.setOnMouseClicked(e -> {
                                   clickedHilfetextAnzeigen(e);
                               });
                               lParamStrukt.labelFeld=lBezeichnungVorEingabefeld;

                           }
                       }

                       if (anzeigeInSelberZeileWieVorgaenger==false) {
                           /*bezeichnungNachEingabefeld*/
                           if (lParamStrukt.bezeichnungNachEingabefeld.trim().isEmpty()==false && lParamStrukt.bezeichnungNachEingabefeld.equals("null")==false) {
                               int spalteFuerBezeichnungNachEingabefeld=2;
                               int spanFuerBezeichnungNachEingabefeld=2;
                               if (bezeichnungNachEingabefeldInNaechsteZeile) {
                                   lfdZeile++;
                                   gpGridPane[0].getRowConstraints().add(rowConstraints);
                                   spalteFuerBezeichnungNachEingabefeld=2;
                                   spanFuerBezeichnungNachEingabefeld=1;
                               }
                               Label lBezeichnungNachEingabefeld=new Label();
                               lBezeichnungNachEingabefeld.setText(ersetzeVariablenInString(lParamStrukt.bezeichnungNachEingabefeld));
                               lBezeichnungNachEingabefeld.setWrapText(true);
                               lBezeichnungNachEingabefeld.setMinHeight(Region.USE_PREF_SIZE);
                               /*columnIndex, rowIndex, colSpan, rowSpan)*/
                               gpGridPane[0].add(lBezeichnungNachEingabefeld, spalteFuerBezeichnungNachEingabefeld, lfdZeile, spanFuerBezeichnungNachEingabefeld, 1);
                           }
                       }
                       
                       elementWurdeAngezeigt=true;
                       /*??????*/

                       
                   }
                   else {
                       if (iParamStruktGruppen.elementHorizontalAnordnen==0) {
                           zeilenStartWurdeAusgeblendet=true;
                       }
                   }
                   break;
               case 1:
                   /*Zwischenüberschrift*/
                   if (pruefeAnzeigenZwischenueberschrift(iParamStruktGruppen)==true) {
                       
                       if (zeilenStartWurdeAusgeblendet==true && anzeigeInSelberZeileWieVorgaenger==true) {
                           /**Dann Zeilenvorschub durchführen*/
                           anzeigeInZeile=lfdZeile;
                           lfdZeile++;
                           lfdZeileVorherigesElement++;
                           
                           zeilenStartWurdeAusgeblendet=false;
                       }

                       
                       int anzeigeSpalteFuerUeberschrift=0;
                       if (iParamStruktGruppen.elementHorizontalAnordnen==1) {
                           anzeigeSpalteFuerUeberschrift=iParamStruktGruppen.anzeigeInSpalte;
                       }                       
                       Label lZwischenueberschrift=new Label();
                       lZwischenueberschrift.setText(ersetzeVariablenInString(iParamStruktGruppen.textFeld));
                       lZwischenueberschrift.setWrapText(true);
                       lZwischenueberschrift.setMinHeight(Region.USE_PREF_SIZE);
                       if ((iParamStruktGruppen.darstellungsAttribut & 2) ==2) {
                           lZwischenueberschrift.setStyle("-fx-font-weight: bold");
                       }
                       /*columnIndex, rowIndex, colSpan, rowSpan)*/
                       int colSpan=1;
                       if (anzeigeInSelberZeileWieVorgaenger) {
                           colSpan=1;
                       }
                       gpGridPane[0].add(lZwischenueberschrift, anzeigeSpalteFuerUeberschrift, anzeigeInZeile, colSpan, 1);
                       
                       elementWurdeAngezeigt=true;
                   }
                   else {
                       if (iParamStruktGruppen.elementHorizontalAnordnen==0) {
                           zeilenStartWurdeAusgeblendet=true;
                       }
                   }
                   break;
                  
               
               
               }
               if (anzeigeInSelberZeileWieVorgaenger==false && elementWurdeAngezeigt==true) {
                   lfdZeileVorherigesElement=lfdZeile;
                   lfdZeile++;
                   gpGridPane[0].getRowConstraints().add(rowConstraints);
               }

            }
        }
        
    }
    
    
    private boolean standardWeichtAb=false;
    private boolean pruefeAnzeigenEingabezeile(ParamStruktGruppen iParamStruktGruppen) {
        standardWeichtAb=false;
        if (iParamStruktGruppen.paramStrukt.wertStandardGefuellt==-1 ||
                iParamStruktGruppen.paramStrukt.wert.equals(iParamStruktGruppen.paramStrukt.wertStandard)==false) {
            standardWeichtAb=true;
        }

        if (anzeigeModus==0) {
            /*Greenhorn*/
            CaBug.druckeLog("LfdNr"+iParamStruktGruppen.lfdNr+" "+iParamStruktGruppen.paramStrukt.ident+" anzeigeModus==Greenhorn false", logDrucken, 10);
            return false;
        }
        if (anzeigeModus==4) {
            /*Analyse*/
            CaBug.druckeLog("LfdNr"+iParamStruktGruppen.lfdNr+" "+iParamStruktGruppen.paramStrukt.ident+" anzeigeModus==Analyse true", logDrucken, 10);
            return true;
        }
        
        if (iParamStruktGruppen.paramStrukt.parameterTutNichts==1 || iParamStruktGruppen.paramStrukt.parameterIstFuerAllgemeineVerwendungFreigegeben==0
                || (iParamStruktGruppen.nurImExpertenmodusAnzeigen & 2) ==2) {
            CaBug.druckeLog("LfdNr"+iParamStruktGruppen.lfdNr+" "+iParamStruktGruppen.paramStrukt.ident+" Tut nichts oder ist nicht freigegeben false", logDrucken, 10);
            return false;
        }
        
        /*Module konfigurierbar ausblenden*/
        boolean brc=pruefeModuleAktiv(iParamStruktGruppen);
        if (brc==false) {
            CaBug.druckeLog("LfdNr"+iParamStruktGruppen.lfdNr+" "+iParamStruktGruppen.paramStrukt.ident+" notwendiges Modul ist nicht aktiv false", logDrucken, 10);
            return false;
        }
        
        if (anzeigeModus==3) {
            /*Experte*/
            CaBug.druckeLog("LfdNr"+iParamStruktGruppen.lfdNr+" "+iParamStruktGruppen.paramStrukt.ident+" anzeigeModus==Experte true", logDrucken, 10);
            return true;
        }
        
        if ((iParamStruktGruppen.nurImExpertenmodusAnzeigen & 1)==0 && anzeigeModus==1) {
            CaBug.druckeLog("LfdNr"+iParamStruktGruppen.lfdNr+" "+iParamStruktGruppen.paramStrukt.ident+" normaler Modus true", logDrucken, 10);
            return true;
        }
        
        /**Hier nun anzeigeModus==1 or ==2 => wenn vom Standard-Abweichend, dann anzeigen*/
        if (standardWeichtAb==true) {
            CaBug.druckeLog("LfdNr"+iParamStruktGruppen.lfdNr+" "+iParamStruktGruppen.paramStrukt.ident+" Standard weicht ab true", logDrucken, 10);
            return true;
        }
        
        CaBug.druckeLog("LfdNr"+iParamStruktGruppen.lfdNr+" "+iParamStruktGruppen.paramStrukt.ident+" am Ende false", logDrucken, 10);
        return false;
    }
    
    private boolean pruefeAnzeigenZwischenueberschrift(ParamStruktGruppen iParamStruktGruppen) {
        if (anzeigeModus==0) {
            /*Greenhorn*/
            return false;
        }
        if (anzeigeModus==4) {
            /*Analyse*/
            return true;
        }
        
        if ((iParamStruktGruppen.nurImExpertenmodusAnzeigen & 2)==2) {
            return false;
        }

        
        /*Module konfigurierbar ausblenden*/
        boolean brc=pruefeModuleAktiv(iParamStruktGruppen);
        if (brc==false) {
            return false;
        }

        if (anzeigeModus==3) {
            /*Experte*/
            return true;
        }
        
        if ((iParamStruktGruppen.nurImExpertenmodusAnzeigen & 1)==0 && anzeigeModus==1) {
            return true;
        }
        
        return false;
    }

    
    /**Module konfigurierbar ausblenden**/
    private boolean pruefeModuleAktiv(ParamStruktGruppen iParamStruktGruppen) {
        long anzeigenWennModulAktiv=iParamStruktGruppen.anzeigenWennModulAktiv;
        if (iParamStruktGruppen.anzeigenWennModulAktiv!=0) {
            //      @formatter:off
            if ((anzeigenWennModulAktiv & KonstParameter.PRUEFBIT_MODULE_KONFIGURIERBAR__AKTIONAERSPORTAL) == KonstParameter.PRUEFBIT_MODULE_KONFIGURIERBAR__AKTIONAERSPORTAL && ParamS.param.paramModuleKonfigurierbar.aktionaersportal==false) {return false;}
            if ((anzeigenWennModulAktiv & KonstParameter.PRUEFBIT_MODULE_KONFIGURIERBAR__BRIEFWAHL) == KonstParameter.PRUEFBIT_MODULE_KONFIGURIERBAR__BRIEFWAHL && ParamS.param.paramModuleKonfigurierbar.briefwahl==false) {return false;}
            if ((anzeigenWennModulAktiv & KonstParameter.PRUEFBIT_MODULE_KONFIGURIERBAR__WEISUNGEN_SCHNITTSTELLE_EXTERNES_SYSTEM) == KonstParameter.PRUEFBIT_MODULE_KONFIGURIERBAR__WEISUNGEN_SCHNITTSTELLE_EXTERNES_SYSTEM && ParamS.param.paramModuleKonfigurierbar.weisungenSchnittstelleExternesSystem==false) {return false;}
            if ((anzeigenWennModulAktiv & KonstParameter.PRUEFBIT_MODULE_KONFIGURIERBAR__EMITTENTENPORTAL) == KonstParameter.PRUEFBIT_MODULE_KONFIGURIERBAR__EMITTENTENPORTAL && ParamS.param.paramModuleKonfigurierbar.emittentenportal==false) {return false;}
            if ((anzeigenWennModulAktiv & KonstParameter.PRUEFBIT_MODULE_KONFIGURIERBAR__REGISTERANBINDUNG) == KonstParameter.PRUEFBIT_MODULE_KONFIGURIERBAR__REGISTERANBINDUNG && ParamS.param.paramModuleKonfigurierbar.registeranbindung==false) {return false;}
            if ((anzeigenWennModulAktiv & KonstParameter.PRUEFBIT_MODULE_KONFIGURIERBAR__ELEKTRONISCHES_TEILNEHMERVERZEICHNIS) == KonstParameter.PRUEFBIT_MODULE_KONFIGURIERBAR__ELEKTRONISCHES_TEILNEHMERVERZEICHNIS && ParamS.param.paramModuleKonfigurierbar.elektronischesTeilnehmerverzeichnis==false) {return false;}
            if ((anzeigenWennModulAktiv & KonstParameter.PRUEFBIT_MODULE_KONFIGURIERBAR__ELEKTRONISCHE_WEISUNGSERFASSUNG_HV) == KonstParameter.PRUEFBIT_MODULE_KONFIGURIERBAR__ELEKTRONISCHE_WEISUNGSERFASSUNG_HV && ParamS.param.paramModuleKonfigurierbar.elektronischeWeisungserfassungHV==false) {return false;}
            if ((anzeigenWennModulAktiv & KonstParameter.PRUEFBIT_MODULE_KONFIGURIERBAR__TABLET_ABSTIMMUNG) == KonstParameter.PRUEFBIT_MODULE_KONFIGURIERBAR__TABLET_ABSTIMMUNG && ParamS.param.paramModuleKonfigurierbar.tabletAbstimmung==false) {return false;}
            if ((anzeigenWennModulAktiv & KonstParameter.PRUEFBIT_MODULE_KONFIGURIERBAR__SCANNER_ABSTIMMUNG) == KonstParameter.PRUEFBIT_MODULE_KONFIGURIERBAR__SCANNER_ABSTIMMUNG && ParamS.param.paramModuleKonfigurierbar.scannerAbstimmung==false) {return false;}
            if ((anzeigenWennModulAktiv & KonstParameter.PRUEFBIT_MODULE_KONFIGURIERBAR__HV_APP) == KonstParameter.PRUEFBIT_MODULE_KONFIGURIERBAR__HV_APP && ParamS.param.paramModuleKonfigurierbar.hvApp==false) {return false;}
            if ((anzeigenWennModulAktiv & KonstParameter.PRUEFBIT_MODULE_KONFIGURIERBAR__HV_APP_ABSTIMMUNG) == KonstParameter.PRUEFBIT_MODULE_KONFIGURIERBAR__HV_APP_ABSTIMMUNG && ParamS.param.paramModuleKonfigurierbar.hvAppAbstimmung==false) {return false;}
            if ((anzeigenWennModulAktiv & KonstParameter.PRUEFBIT_MODULE_KONFIGURIERBAR__HV_APP_HV_FUNKTIONEN) == KonstParameter.PRUEFBIT_MODULE_KONFIGURIERBAR__HV_APP_HV_FUNKTIONEN && ParamS.param.paramModuleKonfigurierbar.hvAppHVFunktionen==false) {return false;}
            if ((anzeigenWennModulAktiv & KonstParameter.PRUEFBIT_MODULE_KONFIGURIERBAR__ELEKTRONISCHER_STIMMBLOCK) == KonstParameter.PRUEFBIT_MODULE_KONFIGURIERBAR__ELEKTRONISCHER_STIMMBLOCK && ParamS.param.paramModuleKonfigurierbar.elektronischerStimmblock==false) {return false;}
            if ((anzeigenWennModulAktiv & KonstParameter.PRUEFBIT_MODULE_KONFIGURIERBAR__ONLINE_TEILNAHME) == KonstParameter.PRUEFBIT_MODULE_KONFIGURIERBAR__ONLINE_TEILNAHME && ParamS.param.paramModuleKonfigurierbar.onlineTeilnahme==false) {return false;}
            if ((anzeigenWennModulAktiv & KonstParameter.PRUEFBIT_MODULE_KONFIGURIERBAR__ENGLISCHE_AGENDA) == KonstParameter.PRUEFBIT_MODULE_KONFIGURIERBAR__ENGLISCHE_AGENDA && ParamS.param.paramModuleKonfigurierbar.englischeAgenda==false) {return false;}
            if ((anzeigenWennModulAktiv & KonstParameter.PRUEFBIT_MODULE_KONFIGURIERBAR__MEHRERE_GATTUNGEN) == KonstParameter.PRUEFBIT_MODULE_KONFIGURIERBAR__MEHRERE_GATTUNGEN && ParamS.param.paramModuleKonfigurierbar.mehrereGattungen==false) {return false;}


            //      @formatter:on
        }
        return true;
    }
    
    
    private void fuelleCbSelektion() {
        if (cbSelektionLokalBelegt==true) {
            cbSelektion=new ComboBox<CbElement>();
        }
        
        cbAllgemeinSelektion=new CbAllgemein(cbSelektion);
        
        CbElement lElement0 = new CbElement();
        lElement0.anzeige = "Greenhorn";
        lElement0.ident1 = 0;
        cbAllgemeinSelektion.addElement(lElement0);

        CbElement lElement1 = new CbElement();
        lElement1.anzeige = "Consultant";
        lElement1.ident1 = 1;
        cbAllgemeinSelektion.addElement(lElement1);

        CbElement lElement2 = new CbElement();
        lElement2.anzeige = "Standard-Abweichungen";
        lElement2.ident1 = 2;
        cbAllgemeinSelektion.addElement(lElement2);

        CbElement lElement3 = new CbElement();
        lElement3.anzeige = "Experte";
        lElement3.ident1 = 3;
        cbAllgemeinSelektion.addElement(lElement3);

        CbElement lElement4 = new CbElement();
        lElement4.anzeige = "Analyse";
        lElement4.ident1 = 4;
        cbAllgemeinSelektion.addElement(lElement4);

        cbAllgemeinSelektion.setAusgewaehlt1(anzeigeModus);

        cbSelektion.valueProperty().addListener(new ChangeListener<CbElement>() {
            @Override
            public void changed(@SuppressWarnings("rawtypes") ObservableValue ov, CbElement alterWert,
                    CbElement neuerWert) {
                if (neuerWert == null) {
                    return;
                }
                int neuerAnzeigeModus=neuerWert.ident1;
                if (neuerAnzeigeModus!=anzeigeModus) {
                    anzeigeModus=neuerAnzeigeModus;
                    clearGrids();
                    refreshGrids();
                }
            }
        });

    }
    

    /**Return-Werte:
     * CaFehler.pfFormatXyUnzulaessig: es wurde ein Eingabefehler festgestellt und bereits
     * ausgegeben.
     *
     * CaFehler.pfXyWurdeVonAnderemBenutzerVeraendert: Parameter konnten nicht gespeichert werden.
     * Entsprechende Fehlermeldung wurde ausgegeben.
     * 
     * Achtung: wenn das Speichern ausgelöst wird, werden alle Eingabefelder gelöscht! Sonst
     * funktioniert nämlich der Web-Service nicht ...
     * @return
     */
    protected int speichereParamStruktGruppenListe() {
        
        boolean brc=pruefeEingabefelder();
        if (brc==false) {
            return CaFehler.pfFormatXyUnzulaessig;
        }
        
        uebertrageEingabefelderInParamStruktWert();
        
        DbBundle lDbBundle=new DbBundle();
        BlParamStrukt blParamStrukt=new BlParamStrukt(false, lDbBundle);
        int rc=blParamStrukt.speichereParameterGruppe(paramStruktGruppe, emittent);
        
        if (rc==CaFehler.pfXyWurdeVonAnderemBenutzerVeraendert) {
            CaZeigeHinweis caZeigeHinweise=new CaZeigeHinweis();
            caZeigeHinweise.zeige(eigeneStage, "Achtung, Parameter konnten möglicherweise nur unvollständig gespeichert werden, ggf. von anderem Benutzer verändert!");
            return CaFehler.pfXyWurdeVonAnderemBenutzerVeraendert;
        }
       
        return 1;
    }
    
    private boolean pruefeEingabefelder() {
        CaBug.druckeLog("", logDrucken, 10);
        for (int i=0;i<paramStruktGruppe.length;i++) {
            List<ParamStruktGruppen> lParamStruktGruppe=paramStruktGruppe[i].paramStruktGruppenelemente;
            if (lParamStruktGruppe!=null && lParamStruktGruppe.size()>0) {
                for (ParamStruktGruppen iParamStruktGruppe : lParamStruktGruppe) {
                    if (iParamStruktGruppe.elementTyp==0) {
                        ParamStrukt lParamStrukt=iParamStruktGruppe.paramStrukt;
                        switch (lParamStrukt.wertIstTyp) {
                        case ParamStrukt.IST_TYP_INTEGER_ALS_EINGABEFELD:
                            TextField lTextFieldInt=(TextField)lParamStrukt.eingabeFeld.get(0);
                            String wertTextFieldInt=lTextFieldInt.getText();
                            if (CaString.isNummernNegativ(wertTextFieldInt)==false) {
                                zeigeFehler(lParamStrukt.bezeichnungVorEingabefeld
                                        +": Nur Zahl zulässig"
                                        );
                                lTextFieldInt.requestFocus();
                                return false;
                            }
                            int wertInteger=Integer.parseInt(wertTextFieldInt);
                            if (wertInteger>lParamStrukt.maximalWertInt) {
                                zeigeFehler(lParamStrukt.bezeichnungVorEingabefeld
                                        +": Maximaler Wert = "
                                        +lParamStrukt.maximalWertInt
                                        +" tatsächlicher Wert = "+wertInteger
                                        );
                                lTextFieldInt.requestFocus();
                                return false;
                            }
                            if (wertInteger<lParamStrukt.minimalWertInt) {
                                zeigeFehler(lParamStrukt.bezeichnungVorEingabefeld
                                        +": Minimaler Wert = "
                                        +lParamStrukt.minimalWertInt
                                        +" tatsächlicher Wert = "+wertInteger
                                        );
                                lTextFieldInt.requestFocus();
                                return false;
                            }
                            break;


                        case ParamStrukt.IST_TYP_STRING_ALS_EINGABEFELD:
                            TextField lTextFieldString=(TextField)lParamStrukt.eingabeFeld.get(0);
                            String wertTextFieldString=lTextFieldString.getText();
                            int laenge=wertTextFieldString.length();
                            CaBug.druckeLog("IST_TYP_STRING_ALS_EINGABEFELD laenge="+laenge, logDrucken, 10);
                            if (laenge>lParamStrukt.maximaleLaengeString) {
                                zeigeFehler(lParamStrukt.bezeichnungVorEingabefeld
                                        +": Maximale Länge = "
                                        +lParamStrukt.maximaleLaengeString
                                        +" tatsächliche Länge = "+laenge
                                        );
                                lTextFieldString.requestFocus();
                                return false;
                            }
                            if (laenge<lParamStrukt.minimaleLaengeString) {
                                zeigeFehler(lParamStrukt.bezeichnungVorEingabefeld
                                        +": Minimale Länge = "
                                        +lParamStrukt.minimaleLaengeString
                                        +" tatsächliche Länge = "+laenge
                                        );
                                lTextFieldString.requestFocus();
                                return false;
                            }
                            if (lParamStrukt.pruefenString==1) {
                                for (int i1=0;i1<wertTextFieldString.length();i1++) {
                                    String hZeichen=wertTextFieldString.substring(i1, i1+1);
                                    if (lParamStrukt.zulaessigeZeichenString.contains(hZeichen)==false) {
                                        zeigeFehler(lParamStrukt.bezeichnungVorEingabefeld
                                                +": Zeichen = '"
                                                +hZeichen
                                                +"' nicht zulässig."
                                                );
                                        lTextFieldString.requestFocus();
                                        return false;
                                    }
                                }
                            }
                            break;
                       
                        case ParamStrukt.IST_TYP_BOOLEAN:
                            break;
                        }
                    
                    }
                }
            }
        }
        return true;
    }
    
    private void uebertrageEingabefelderInParamStruktWert() {
        for (int i=0;i<paramStruktGruppe.length;i++) {
            List<ParamStruktGruppen> lParamStruktGruppe=paramStruktGruppe[i].paramStruktGruppenelemente;
            if (lParamStruktGruppe!=null && lParamStruktGruppe.size()>0) {
                for (ParamStruktGruppen iParamStruktGruppe : lParamStruktGruppe) {
                    if (iParamStruktGruppe.elementTyp==0) {
                        ParamStrukt lParamStrukt=iParamStruktGruppe.paramStrukt;
                        switch (lParamStrukt.wertIstTyp) {
                        
                        case ParamStrukt.IST_TYP_INTEGER_ALS_EINGABEFELD:
                        case ParamStrukt.IST_TYP_STRING_ALS_EINGABEFELD:
                            TextField tfTextField=(TextField)lParamStrukt.eingabeFeld.get(0);
                            lParamStrukt.wert=tfTextField.getText();
                            break;

                        case ParamStrukt.IST_TYP_BOOLEAN:
                            CheckBox cbEingabe=(CheckBox)lParamStrukt.eingabeFeld.get(0);
                            if (cbEingabe.isSelected()) {
                                lParamStrukt.wert="1";
                            }
                            else {
                                lParamStrukt.wert="0";
                            }
                            break;
                        case ParamStrukt.IST_TYP_STRING_ALS_RADIO_BUTTONS:
                            for (int i1=0;i1<lParamStrukt.struktWerte.size();i1++) {
                                if (((RadioButton)lParamStrukt.eingabeFeld.get(i1)).isSelected()) {
                                    lParamStrukt.wert=lParamStrukt.struktWerte.get(i1).zulaessigeWerteString;
                                }
                            }
                            break;
                        case ParamStrukt.IST_TYP_INTEGER_ALS_VERKNUEPFUNGSFELD:
                            int wert=0;
                            for (int i1=0;i1<lParamStrukt.struktWerte.size();i1++) {
                                if (((CheckBox)lParamStrukt.eingabeFeld.get(i1)).isSelected()) {
                                    wert+=lParamStrukt.struktWerte.get(i1).zulaessigeWerteInt;
                                }
                            }
                            lParamStrukt.wert=Integer.toString(wert);
                            break;
                        case ParamStrukt.IST_TYP_INTEGER_ALS_DROP_DOWN:
                            ComboBox<CbElement> comboBox=(ComboBox<CbElement>)lParamStrukt.eingabeFeld.get(0);
                            lParamStrukt.wert=Integer.toString(comboBox.getValue().ident1);
                            break;
                            
                        }
                        lParamStrukt.labelFeld=null;
                        lParamStrukt.eingabeFeld=null;
                    
                    }
                }
            }
        }
        
    }
    
    private String ersetzeVariablenInString(String pAlterWert) {
        String pNeuerWert="";
        
        switch (pAlterWert) {
        case "##G1":
            pNeuerWert=ParamS.param.paramBasis.getGattungBezeichnung(1);
            break;
        case "##G2":
            pNeuerWert=ParamS.param.paramBasis.getGattungBezeichnung(2);
            break;
        case "##G3":
            pNeuerWert=ParamS.param.paramBasis.getGattungBezeichnung(3);
            break;
        case "##G4":
            pNeuerWert=ParamS.param.paramBasis.getGattungBezeichnung(4);
            break;
        case "##G5":
            pNeuerWert=ParamS.param.paramBasis.getGattungBezeichnung(5);
            break;
            
        case "##G1Kurz":
            pNeuerWert=ParamS.param.paramBasis.getGattungBezeichnungKurz(1);
            break;
        case "##G2Kurz":
            pNeuerWert=ParamS.param.paramBasis.getGattungBezeichnungKurz(2);
            break;
        case "##G3Kurz":
            pNeuerWert=ParamS.param.paramBasis.getGattungBezeichnungKurz(3);
            break;
        case "##G4Kurz":
            pNeuerWert=ParamS.param.paramBasis.getGattungBezeichnungKurz(4);
            break;
        case "##G5Kurz":
            pNeuerWert=ParamS.param.paramBasis.getGattungBezeichnungKurz(5);
            break;
        default:
            pNeuerWert=pAlterWert;
            break;
        }
        
        return pNeuerWert;
    }
    
    protected boolean pruefeObVeraenderungenVorhanden() {
        for (int i=0;i<paramStruktGruppe.length;i++) {
            List<ParamStruktGruppen> lParamStruktGruppe=paramStruktGruppe[i].paramStruktGruppenelemente;
            if (lParamStruktGruppe!=null && lParamStruktGruppe.size()>0) {
                for (ParamStruktGruppen iParamStruktGruppe : lParamStruktGruppe) {
                    if (iParamStruktGruppe.elementTyp==0) {
                        ParamStrukt lParamStrukt=iParamStruktGruppe.paramStrukt;
                        String neuerWert=null;
                        switch (lParamStrukt.wertIstTyp) {
                        case ParamStrukt.IST_TYP_BOOLEAN:
                            CheckBox cbEingabe=(CheckBox)lParamStrukt.eingabeFeld.get(0);
                            if (cbEingabe.isSelected()) {
                                neuerWert="1";
                            }
                            else {
                                neuerWert="0";
                            }
                            break;
                        }
                        if (neuerWert!=null && neuerWert.equals(lParamStrukt.wert)==false) {
                            return true;
                        }
                    
                    }
                }
            }
        }
        return false;
    }

    
    void clickedHilfetextAnzeigen(MouseEvent e) {

        CaBug.druckeLog("", logDrucken, 10);
        
        for (int i=0;i<paramStruktGruppe.length;i++) {
            ParamStruktGruppenHeader lParamStruktGruppenHeader=paramStruktGruppe[i];
            List<ParamStruktGruppen> lParamStruktGruppenelemente=lParamStruktGruppenHeader.paramStruktGruppenelemente;
            for (ParamStruktGruppen iParamStruktGruppen: lParamStruktGruppenelemente) {
                if (iParamStruktGruppen.elementTyp==0) {
                    ParamStrukt lParamStrukt=iParamStruktGruppen.paramStrukt;
                    if (e.getSource()==(Label)lParamStrukt.labelFeld) {
                        CaZeigeHinweis caZeigeHinweis=new CaZeigeHinweis();
                        caZeigeHinweis.zeigeHilfetext(eigeneStage, lParamStrukt.hilfetext);
                        
                    }
                }
            }

        }
    }
    
    private void zeigeFehler(String pFehlertext) {
        CaZeigeHinweis caZeigeHinweis=new CaZeigeHinweis();
        caZeigeHinweis.zeige(eigeneStage, pFehlertext);
    }

    
    
}

