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
package de.meetingapps.meetingportal.meetComHVParam;

import java.io.Serializable;
import java.util.List;

public class ParamStrukt implements Serializable {
    private static final long serialVersionUID = -5227818384418383144L;

    /*++++++++++++++++++++++++Identifikation++++++++++++++++++++++*/
    public int ident=0;
    
    /**1 = tbl_parameter
     * 2 = tbl_parameterServer
     * 3 = tbl_parameterLocal
     * 4 = tbl_parameterLang
     * 5 = tbl_emittent (in diesem Fall: parameterOffsetInTable gibt das Feld wieder
     */
    public int parameterTable=0;
    
    /**Für parameterTable==5 (emittent)
     * 1:   portalVorhanden
     * 2:   registerAnbindungVorhanden
     * 3:   appVorhanden
     * 4:   emittentenPortalVorhanden
     * 5:   datenbestandIstProduktiv
     * 
     */
    public int parameterOffsetInTable=0;
    
    /**Für den Fall, dass mehrere Parameter in einem Satz im Table
     * zusammengefaßt sind 
     */
    public int parameterSubOffsetInTable=0;
    public int parameterLaengeInTable=0;

    public int parameterGehoertZuPresetArt=0;
    
    /**++++++++++++++++++tatsächlicher Wert++++++++++++++++++++++++++++++*/
    public String wert="";
    
    public boolean wertWurdeAusTableBelegt=false;
    
    
    /*+++++++++++++Standardwerte++++++++++++++++++++*/
    /**dient zum Abgleich bei der Pflege, ob 
     * ein vom Standard abweichender Wert eingegeben wurde;
     * sowie zum Selektieren, ob der Parameter nur für Experten angezeigt werden soll
     */
    public List<ParamStruktStandard> struktStandard=null;
    
    /*+++++++++++++++++++++Beschreibung des Wertes++++++++++++++++++++++++*/
    
    
    static public final int IST_TYP_INTEGER_ALS_EINGABEFELD=0;
    static public final int IST_TYP_STRING_ALS_EINGABEFELD=1;
    static public final int IST_TYP_BOOLEAN=2;
    static public final int IST_TYP_STRING_ALS_DROP_DOWN=3;
    static public final int IST_TYP_STRING_ALS_RADIO_BUTTONS=4;
    static public final int IST_TYP_INTEGER_ALS_VERKNUEPFUNGSFELD=5;
    static public final int IST_TYP_INTEGER_ALS_DROP_DOWN=6;
   
    public int wertIstTyp=0; 

    
    /*+++++++++++++Gültigkeitsprüfung des Wertes++++++++++++++++++++++*/
    /**Verweist auf eine festprogrammierte Prüfroutine*/
    public int pruefroutineAusfuehren=0;
    
    /*+++++++++++++++++Gültigkeitsprüfung des Wertes - String+++++++++++++++*/
    
    /**0 = keine Prüfung, außer Laenge
     * 1 = zulaessigeZeichen pruefen - nur die in zulaessige Zeichen enthaltenen Zeichen dürfen im Eingabefeld vorkommen
     * 2 = zulaessigeWerte prüfen
     */
    public int pruefenString=0;
    
    public int minimaleLaengeString=0;
    public int maximaleLaengeString=0;
    
    /**LEN=1000;*/
    public String zulaessigeZeichenString="";
    
    /*+++++++++++++++++Gültigkeitsprüfung des Wertes - Int+++++++++++++++*/
    
    /**0 = keine Prüfung, außer Laenge
     * 1 = zulaessigeZiffern pruefen - nur die in zulaessige Zeichen enthaltenen Zeichen dürfen im Eingabefeld vorkommen
     * 2 = zulässige Werte prüfen
     */
    public int pruefenInt=0;
    
    public int minimalWertInt=0;
    public int maximalWertInt=0;
    
    /**LEN=100*/
    public String zulaessigeZiffernInt="";
    
   
    /*+++++++++++++++++++++++++++Übergreifende++++++++++++++++++++*/
    /**0 => aus Parameter-Konfiguration die zulässigen struktWerte lesen.
     * >0 => struktWerte werden gemäß "Programmierung" gefüllt
     * 1 = Nummernform-Sets
     * 2 = Abstimmungsblöcke
     */
    public int werteLesenAus=0;
    
    public List<ParamStruktWerte> struktWerte=null;
    
    
    /*+++++++++++++++++++++++Anzeige+++++++++++++++++++++++++++++++++*/
    
    /**0=Nix besonderes. 
     *      Für Int und String einfaches Eingabefeld mit eingabeLaenge (=max Zeichen einzugeben), feldLaenge (Länge des Feldes am Bildschirm)
     *      Für boolean-Wert. Wird als Checkbox angezeigt, false=[0], true=[1]
     * 1=Bei Radiobuttons / mehreren Checkboxen: Radiobuttons für zulässige Werte
     * 2=DropDown für zulässige Werte 
     * 3=Checkboxes für zulässige Werte 
     * ????Wahrscheinlich überflüssig?????????????????????????????????
     */
    public int anzeigeForm=0;
    
    public int eingabeLaenge=0;
    public int feldLaenge=0;
    
    /**Erläuterungsanzeige vor dem Eingabefeld (Feldbezeichnung)
     * LEN=100*/
    public String bezeichnungVorEingabefeld="";
    /**Erläuterungsanzeige nach dem Eingabefeld
     * LEN=1000*/
    public String bezeichnungNachEingabefeld=""; 
    
    
    /**Wird im Hilfefenster als ersten Text angezeigt. Falls zulässigeWerte-Liste gefüllt, dann anschließend
     * zulässigeWerte und langtext
     */
    public String hilfetext="";
    
    /**LEN=80*/
    public String parameterName="";
    
    /*++++++++++++Verwendbarkeit / Anzeigeselektion++++++++++++++++*/
    
    /*Anzeigelogik:
     * 0 - Greenhorn - nur Dummy, zeigt nix an
     * -------------
     * 
     * 1 - Consultant:
     * ------------ 
     * (> nurImExpertenmodusAnzeigen==0
     * ODER
     * > alle, bei dem Wert vom aktuellen Standard-Wert abweicht
     * )
     * UND
     * > parameterTutNichts==0
     * Und
     * > parameterIstFuerAllgemeineVerwendungFreigegeben==1
     * UND
     * > anzeigenWennModulAktiv wird ausgewertet
     * 
     * 2- Nur Abweichungen vom Standard:
     * ----------------------------------
     * > alle, bei dem Wert vom aktuellen Standard-Wert abweicht
     * UND
     * > parameterTutNichts==0
     * UND
     * > parameterIstFuerAllgemeineVerwendungFreigegeben==1
     * UND
     * > anzeigenWennModulAktiv wird ausgewertet
     * 
     * 3 - Experte
     * -----------
     * > parameterTutNichts==0
     * Und
     * > parameterIstFuerAllgemeineVerwendungFreigegeben==1
     * UND
     * > anzeigenWennModulAktiv wird ausgewertet)
     * 
     * 4 - Analyse
     * -----------
     * > Wirklich alle
     * 
     */
    
    
    public int parameterTutNichts=0;
    public int parameterIstFuerAllgemeineVerwendungFreigegeben=1;
    
    
    /***********************Nicht in Datenbank************************************************/
    
    /**Wird verwendet, um Hilfetext anzuzeigen*/
    public Object labelFeld=null;
    
    /**Ist eine Liste, da z.B. bei Select-Boxen mehrere Checkboxen gespeichert werden müssen.
     * 
     * Beim "Umschießen" des Veranstaltungstyps wird darin die Checkbox angelegt, über die
     * selektiert werden kann, ob der Parameter verändert werden soll oder nicht*/
    public List<Object> eingabeFeld=null;
    
    /**Falls parameterTable==5: Version des Emittentensatzes, aus dem Parameter gelesen wurde*/
    public long db_versionEmittent=0;
    
    /**>=0 => für diesen Parameter gibt es einen Standardwert; und zwar
     * aus dem Standardwert aus Versammlungstyp Offset[wertStandardGefuellt] in vererbungsliste
     * (siehe BlParamStrukt)*/
    public int wertStandardGefuellt=-1;
    public String wertStandard="";
    
    /**Wird verwendet, wenn Veranstaltungstyp verändert wird, beim "umschießen" aller Parameter-Werte*/
    public int wertStandardNeuGefuellt=-1;
    public String wertStandardNeu="";
    
    /**Wird beim Umstellen auf neuenVeranstaltungstyp verwendet. Wenn true, dann erfolgt ein Update des Parameterwertes,
     * sonst nicht
     */
    public boolean aufNeuenStandardWertUpdaten=false;
    
//  @formatter:off
//    ParamStrukt(
//            int pWertInt,
//            
//            int pStandardwertInt,
//            
//            int pParameterTable,
//            int pParameterOffsetInTable){
//        wertInt=pWertInt;
//        
//        
//        wertIstTyp=IST_TYP_INTEGER;
//        parameterTable=pParameterTable;
//        parameterOffsetInTable=pParameterOffsetInTable;
//    }
    
    
//    ParamStrukt(
//            int pWertInt,
//            String pWertString,
//            boolean pWertBoolean,
//            
//            int pStandardwertInt,
//            String pStandardwertString,
//            boolean pStandardwertBoolean,
//            
//            int pWertIstTyp,
//            int pParameterTable,
//            int pParameterOffsetInTable){
//        
//        wertInt=pWertInt;
//        wertString=pWertString;
//        wertBoolean=pWertBoolean;
//        
//        
//        wertIstTyp=pWertIstTyp;
//        parameterTable=pParameterTable;
//        parameterOffsetInTable=pParameterOffsetInTable;
//        
//    }
//  @formatter:on
    
}
