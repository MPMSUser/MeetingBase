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
package de.meetingapps.meetingportal.meetComEntities;

import java.io.Serializable;

/**Verarbeitungsregeln für den Service-Desk-Workflow.
 * 
 * Gleich so gestaltet, dass es auch für andere "Desks" verwendbar ist (ggf.
 * mit unterschiedlichen Berechtigungsstufen, und auch für Bankenschalter)
 * 
 * Dementsprechend sind mandantenabhängig mehrere "Sets" speicherbar, die je nach Mandant und Funktion
 * aktiviert werden können 
 * 
 * Berücksichtigung verschiedener Berechtigungsstufen ist vorgesehen.
 *
 */
public class EclServiceDeskWorkflow  implements Serializable {
    private static final long serialVersionUID = -8143543386863019266L;

    /**ident wird beim Einfügen/Updaten jeweils neu vergeben, und darf nicht
     * als Referenz von "Extern" verwendet werden.
     * ident gibt auch die Reihenfolge vor, in der die einzelnen Bedingungssätze
     * abgearbeitet werden.
     */
    public int ident=0;
    
    /**Verarbeitungsstep gehört zu diesem Set; Set-Beschreibung in
     * EclServiceDeskSet*/
    public int setNr=0;
    
    /**-1 => wird aktuell nicht berücksichtigt. Speziell für Testcases gedacht*/
    public int istAktiv=0;
    
    /**++++++++++++++++++++Bedingungen+++++++++++++++++++++++++++*/
    
    /**Bedingungen aus Datenbank für den zu bearbeitenden Teilnehmer.
     * 
     * Alle Bedingungen werden mit "Und" verknüpft.
     * 
     * -1 = Bedingung darf nicht erfüllt sein
     * 1 = Bedingung muß erfüllt sein
     * 0 = ist egal für diesen Fall
     * */
    public int istGast=0;
    public int istAktionaer=0;
    public int istSammelkarte=0;
    public int istAngemeldeterAktionaer=0;
    public int istNullbestand=0;
    
    public int istGeradePraesent=0;
    public int istGeradeSelbstPraesent=0;
    public int istGeradeVertretenPraesent=0;
    public int warPraesent=0;

    public int istGeradeVirtuellPraesent=0;
    public int istGeradeVirtuellSelbstPraesent=0;
    public int istGeradeVirtuellVertretenPraesent=0;
    public int warVirtuellPraesent=0;
    
    public int istAktionaerInSammelkarte=0;
    public int istInBriefwahlsammelkarte=0;
    public int istInSRVsammelkarte=0;
    public int istInKIAVsammelkarte=0;
    public int istInDauervollmachtsammelkarte=0;
    public int istInOrgasammelkarte=0;
    
    public int hatEKzugeordnet=0;
    public int hatSKzugeordnet=0;
    
    public int hatVollmachtErteilt=0;
    
    /**Mischung aus Stimmkarten-Parametern und Gattung des Teilnehmers*/
    public int esGibtStimmkartenZumZuordnen=0;
    public int esGibtStimmkartenZumDrucken=0;
    
    /**Zusätzliche Fragen erfüllt
     * Zur Klarstellung: hier gilt:
     * -1 => Antwort muß noch unbelegt sein
     * 0 => Antwortwert ist egal
     * >0 => Antwortwert muß den entsprechenden Wert haben*/ 
    public int[] wertAntwort=new int[10];
    
    /**++++++++++++++++++++++++Aktionen, wenn diese Bedingungen erfüllt sind*********/
    /**----Frage - für Folgestatus----*/
    /**=-1 => Frage wird nicht gestellt; >=0 => Ergebnis wird in wertAntwort<x> geschrieben*/
    public int frageInAntwortSchreiben=-1;
    public String frageEinleitungsText="";
    public String[] frage=new String[5];
    
    /**---Ausführen-Aktion - als Abfrage---*/
    /**=1 => Ausführen-Abfrage wird angezeigt; nur zulässig, wenn frageInAntwortSchreiben=-1 (und umgekehrt!)*/
    public int ausfuehrenAbfrage=-1;
    public String[] ausfuehrenAbfrageText=new String[10];
    public String[] ausfuehrenAbfrageHinweis=new String[10];
    public int[] ausfuehrenAbfrageFunktion=new int[10];
    public int[] ausfuehrenAbfrageErforderlichesRecht=new int[10];
    
    public String[] ausfuehrenButtonText=new String[10];
    public String[] ausfuehrenButtonHinweis=new String[10];
    public int[] ausfuehrenButtonFunktion=new int[10];
    public int[] ausfuehrenButtonErforderlichesRecht=new int[10];
    
    /**++++++++++++++Erläuterungen für diesen Teilnehmer+++++++++++++++++++++++++++*/
    /**LEN=1000*/
    public String anzeigeStatus="";
    
    
}
