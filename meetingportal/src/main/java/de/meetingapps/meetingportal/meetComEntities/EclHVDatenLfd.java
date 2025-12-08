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

public class EclHVDatenLfd {

    public int mandant = 0;
    public int ident = 0;
    public long db_version = 0;
    public int benutzer = 0;
    /**Len=40*/
    public String wert = "";
    /**Len=80*/
    public String beschreibung = "";

    /*Beschreibung des Dateiinhalts:
     * 
     * neueIdent[alteIdent]
    (0,1[1],0,0,'0','Präsenzsumme normal Gattung 1'), benutzernr=0
    (0,2[2],0,0,'0','Präsenzsumme normal Gattung 2'),
    (0,3[-],0,0,'0','Präsenzsumme normal Gattung 3'),
    (0,4[-],0,0,'0','Präsenzsumme normal Gattung 4'),
    (0,5[-],0,0,'0','Präsenzsumme normal Gattung 5'),
    
    (0,11[11],0,0,'0','Präsenzsumme Delayed Gattung 1'),
    (0,12[12],0,0,'0','Präsenzsumme Delayed Gattung 2'),
    (0,13[-],0,0,'0','Präsenzsumme Delayed Gattung 3'),
    (0,14[-],0,0,'0','Präsenzsumme Delayed Gattung 4'),
    (0,15[-],0,0,'0','Präsenzsumme Delayed Gattung 5'),
    
    für 501 bis 620 gilt: "Benutzernr" wird als "Nummer der Präsenzliste" gesetzt.
    
    (0,501[31],0,0,'0','G1 aktuelle Liste zuVerzeichnisNr1 berechnet'), benutzernr=Nummer der aktuellen Liste
    (0,502[32],0,0,'0','G2 aktuelle Liste zuVerzeichnisNr1 berechnet'), benutzernr=Nummer der aktuellen Liste
    (0,503[-],0,0,'0','G3 aktuelle Liste zuVerzeichnisNr1 berechnet'), benutzernr=Nummer der aktuellen Liste
    (0,504[-],0,0,'0','G4 aktuelle Liste zuVerzeichnisNr1 berechnet'), benutzernr=Nummer der aktuellen Liste
    (0,505[-],0,0,'0','G5 aktuelle Liste zuVerzeichnisNr1 berechnet'), benutzernr=Nummer der aktuellen Liste
    analog 506-510:
    (0,506[33],0,0,'0','G1 aktuelle Liste zuVerzeichnisNr2 berechnet'),
    (0,507[34],0,0,'0','G2 aktuelle Liste zuVerzeichnisNr2 berechnet'),
    analog 511-515:
    (0,511[35],0,0,'0','G1 aktuelle Liste zuVerzeichnisNr3 berechnet'),
    (0,512[36],0,0,'0','G2 aktuelle Liste zuVerzeichnisNr3 berechnet'),
    analog 516-520:
    (0,516[37],0,0,'0','G1 aktuelle Liste zuVerzeichnisNr4 berechnet'),
    (0,517[38],0,0,'0','G2 aktuelle Liste zuVerzeichnisNr4 berechnet'),
    
    wie 501 bis 520, aber für Briefwahl:
    (0,601[131],0,0,'0','G1 - Briefwahl - aktuelle Liste zuVerzeichnisNr1 berechnet'), benutzernr=Nummer der aktuellen Liste
    (0,602[132],0,0,'0','G2 - Briefwahl - aktuelle Liste zuVerzeichnisNr1 berechnet'), benutzernr=Nummer der aktuellen Liste
    ...
    (0,606[133],0,0,'0','G1 - Briefwahl - aktuelle Liste zuVerzeichnisNr2 berechnet'),
    (0,607[134],0,0,'0','G2 - Briefwahl - aktuelle Liste zuVerzeichnisNr2 berechnet'),
    ...
    (0,611[135],0,0,'0','G1 - Briefwahl - aktuelle Liste zuVerzeichnisNr3 berechnet'),
    (0,612[136],0,0,'0','G2 - Briefwahl - aktuelle Liste zuVerzeichnisNr3 berechnet'),
    ...
    (0,616[137],0,0,'0','G1 - Briefwahl - aktuelle Liste zuVerzeichnisNr4 berechnet'),
    (0,617[138],0,0,'0','G2 - Briefwahl - aktuelle Liste zuVerzeichnisNr4 berechnet'),
    ...
    
    551 bis 570 - wie 501 bis 520, nur "aus Liste": 
    (0,551[41],0,0,'0','G1 aktuelle Liste zuVerzeichnisNr1 ausListe'), benutzernr=Nummer der aktuellen Liste
    (0,552[42],0,0,'0','G2 aktuelle Liste zuVerzeichnisNr1 ausListe'), benutzernr=Nummer der aktuellen Liste
    ...
    (0,556[43],0,0,'0','G1 aktuelle Liste zuVerzeichnisNr2 ausListe'),
    (0,557[44],0,0,'0','G2 aktuelle Liste zuVerzeichnisNr2 ausListe'),
    ---
    (0,561[45],0,0,'0','G1 aktuelle Liste zuVerzeichnisNr3 ausListe'),
    (0,562[46],0,0,'0','G2 aktuelle Liste zuVerzeichnisNr3 ausListe'),
    ...
    (0,566[47],0,0,'0','G1 aktuelle Liste zuVerzeichnisNr4 ausListe'),
    (0,567[48],0,0,'0','G2 aktuelle Liste zuVerzeichnisNr4 ausListe'),
    ...
    
    51 bis 54: aktuelle Nummer des laufenden Verzeichnisses (-1 = Erstpräsenz)
    
    61 bis 64[61 bis 64]: Datum Uhrzeit der Feststellung des jeweiligen Verzeichnisses, benutzernr=Nummer der aktuellen Liste 
    
    71: höchste Willenserklärung, die bereits in elektronisches Teilnehmerverzeichnis übernommen wurde
    72: Präsenznummer, die der Abstimmung zugrunde gelegt wurde. benutzernr=Abstimmungsblock
    
    (0,101,0,0,'0','aktuelle Protokollnummer je Arbeitsplatz'), benutzernr=arbeitsplatzNr
    (0,102,0,0,'0','Anzahl Erstzugänge im Protokoll je Arbeitsplatz'),
    (0,103,0,0,'0','Anzahl Einträge insgesamt im Protokoll je Arbeitsplatz');
    (0,104,0,0,'0','Letztes abgeschlossenes Protokoll je Arbeitsplatz');
    
    1000: =1 => Archiv ist erstellt, kann normal ausgewertet werden; =2 => Archiv ist erstellt, Achtung Sammelkarten mit Kopfweisung enthalten
    1001: benutzernr=Abstimmungsident: Abstimmungspunkt-Ident ist in Archiv enthalten bzw. wurde abgestimmt
    
    
     */

}
