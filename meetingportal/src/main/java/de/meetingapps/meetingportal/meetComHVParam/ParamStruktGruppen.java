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

public class ParamStruktGruppen {

    public int identGruppe=0;
    public int lfdNr=0;
    
    /**0 = Parameter - Standardeingabe
     * 1 = Überschrift in Grid
     * 
     */
    public int elementTyp=0;
    
    /**Bei elementTyp==0 => Verweis auf den zu editierenden Parameter*/
    public int identParamStrukt=0;
    
    /**bei elementTyp==1
     * LEN=500*/
    public String textFeld="";
    
    /**Je nach Typ, Bitweise:
     * 1 = (nur für elementTyp==0 zulässig): "Beschreibung nach Feld" wird unter das Eingabefeld angezeigt, das Eingabefeld selbst
     *      wird über Spalten 2 bis 4 angezeigt
     * 2 = Label wird in Bold angezeigt;
     *      bei Überschrift: Überschrift wird komplett in Bold angezeigt.
     *      bei Eingabefeldern: Bezeichnung vor dem Eingabefeld wird in Bold angezeigt
     * 4 = mehrere Checkboxen/Radiobuttons werden nebeneinander angezeigt. Damit wird automatisch auch "Beschreibung nach Feld" 
     *      unter das Eingabefeld angezeigt (also wie Bit 1). Wenn Bit nicht gesetzt, dann weden die Boxen untereinander angezeigt. 
     */
    public int darstellungsAttribut=0;

    /** & 1 => Nur im Expertenmodus anzeigen
     *  & 2 => Nicht für Allgemeinheit freigegeben
     */
    public int nurImExpertenmodusAnzeigen=0;
    
    public long anzeigenWennModulAktiv=0;

    
    /**+++Für zusätzlichen, individuell zu programmierenden, Funktionsbutton++++
     * Funktionsbuttons werden in Spalte 2 und 3 angezeigt; d.h. ggf. darstellungsAttribut Bit 1 setzen!*/
    public int button1Funktionscode=0;
    /**LEN=100*/
    public String button1Text="";
    
    public int button2Funktionscode=0;
    public String button2Text="";
    
    /**++++Für Anordnung mehrerer Elemente nebeneinander+++++
     * Damit können Elemente des Typs 0 und 1 Spaltengerecht angeordnet werden.
     * Wenn aktiviert, werden die Nachbezeichnungsfelder für elementTyp 0 nicht angezeigt!
     */
    /**wenn 1, dann wird das Element in der Zeile des vorhergehenden Elements angezeigt,
     * in der entsprechenden Spalte
     */
    public int elementHorizontalAnordnen=0;
    /**Wird nur verwendet, wenn elementHorizontalAnordnen = 1
     * Spaltenzählung beginnt bei 0 (linke Spalte = 0)*/
    public int anzeigeInSpalte=0;
   
    /*+++++++++++++++++++Nicht in Datenbank++++++++++++++++++++*/
    
    /**Falls elementTyp=0 => zugehörige Parameterstruktur*/
    public ParamStrukt paramStrukt=null;
    
    
}
