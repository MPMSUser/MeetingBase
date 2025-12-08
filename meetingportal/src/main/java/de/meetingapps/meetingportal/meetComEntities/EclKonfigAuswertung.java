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

public class EclKonfigAuswertung {

    /**Mandantennummer*/
    public int mandant = 0;

    /**Eindeutiger Key*/
    public int ident = 0;

    /**Zuordnung dieser Konfig zu einer Funktion (Erstpräsenz, etc.).
     * Siehe KonstKonfigAuswertungArt*/
    public int fuerFunktion = 0;

    /**"Nummer", unter der die Konfigs zu einem Lauf zusammengefaßt werden. Irgendwann mal Verweis auf 
     * extra Klasse mit entsprechender Beschreibung. Derzeit einfach laufende Nummer, frei vergebbar
     */
    public int nr = 0;

    /**Gibt Reihenfolge vor, in der die Auswertungen innerhalb des Laufs ausgeführt werden sollen*/
    public int positionInLauf = 0;

    /**Gibt an, was konkret passieren soll. Ist abhängig von "fuerFunktion".
     * Siehe KonstKonfigAuswertungFunktion
     */
    public int ausgeloesteFunktion = 0;

    /**Je nach ausgeloesteFunktion: verwendete Formular- oder Listen-Nr.*/
    public int ausgeloesteFormNr = 0;

    /**Text wird an Formular übergeben, soweit dort unterstützt. Verwendbar z.B. für
     * Verwendung (Notar, Wortmeldetisch ...) und
     * Sortierung (Alpha ...)
     * für die Zwischenblätter
     * LEN=200
     */
    public String textFuerFormular1 = "";
    public String textFuerFormular2 = "";

    /**Für Teilnehmerverzeichnisse:
     * KonstKonfigAuswertungSortierung
     * 1 = Name, 2 = EK, 3 = SK, 4=Aktien aufsteigend
     */
    public int sortierung = 0;

    /**Für Präsenzausgabe: Gattung
     * 0=alle, 1 bis 5 = entsprechende Gattung
     */
    public int gattung = 0;

    /**1 = Drucker abfragen
     * 2 = wie vorherig abgefragter Drucker
     * 11 bis 20 = vorkonfigurierter Drucker
     * 21 bis 30 = PDF-Ausgabe
     * 		Details zu spezifieren (Verzeichnisse)	
     * 		21 = Ex Pfad Präsentation
     * 		22 = Ex Pfad Bühnensystem
     * 		23 = Ex Pfad PP-Excep
     * 		24 = Kundeno.Basis-LW/Pfad
     * Zu ergänzen zum Excel, PowerPoint etc.
     * KonstKonfigAuswertungAusgabeWeg
     * TODO Anpassen wie ParamServer - autoDrucker
     */
    public int ausgabeWeg = 0;

    /**Falls ausgabeWeg = 21 bis 30*/
    public String dateinamePdf = "";
}
