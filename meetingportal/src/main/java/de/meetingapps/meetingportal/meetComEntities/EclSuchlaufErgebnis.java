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

public class EclSuchlaufErgebnis {

    public int mandant = 0;

    public int ident = 0;

    public int identSuchlaufDefinition = 0;

    /**0=aus SuchlaufBegriffen
     * >0 aus Einzelsuche; Suchbegriff dann in einzelSuchBegriff
     * 		KonstSuchlaufSuchbegriffArt
     * 		1=Aktienregisternummer
     * 		2=Eintrittskartennummmer
     * 		3=Stimmkartennummer
     * 		4=Einzelname nur Aktionär
     * 		5=Einzelname nur Vertreter
     * 		6=Einzelname Aktionär und aktueller Vertreter
     * 		7=MeldeIdent
     */
    public int entstandenAus = 0;
    /**LEN=100*/
    public String einzelSuchBegriff = "";

    /**Falls entstandenAus==0, d.h. Satz ist aus Begriffs-Suchlauf entstanden:
     * 	0 = unverändert seit letzter Suche
     * 	1 = seit letzter Suche dazugekommen
     * -1 = in letzter Suche vorhanden, in aktueller nicht mehr
     */
    public int veraenderungGegenueberLetztemSuchlauf = 0;

    public int identAktienregister = 0;

    public int identMelderegister = 0;

    public int wurdeVerarbeitet = 0;

    /**=1 => dieser Ergebnissatz ist nicht mehr im aktuellen Suchlaufergebnis
     * enthalten, wurde aber bereits verarbeitet
     */
    public int verarbeitetNichtMehrInSuchergebnis = 0;

    public int wurdeAusSucheAusgegrenzt = 0;

    /**Len=100*/
    public String ausgegrenztWeil = "";

    /**Name "potentielle" Vollmacht (aus dem Kreis der gespeicherten Vollmachten, muß nicht unbedingt
     * die aktuelle sein!); 
     * Können auch mehrere Namen sein (nämlich beim Suchen nach Suchbegriffen, wenn beim Suchen nach Vollmachten mehrere passende
     * Vollmachten gefunden wurden). Falls dadurch Inhalt zu lang (d.h. >400 Zeichen), wird vorne "Unvollständig; " eingefügt.
     * 
     * Vollmachten aus den vorherigen Suchläufen, die wieder gefunden wurden, stehen normal.
     * Vollmachten die nicht mehr wiedergefunden wurden, und weiter nicht enthalten sind, stehen in (- ....)
     * Vollmachten, die erstmalig nicht mehr wiedergefunden wurden, stehen mit - .....
     * Vollmachten die neu gefunden wurden, stehen mit + .....
     * LEN=400
     */
    public String gefundeneVollmachtName = "";

    /**Parameter - Verwendung je nach Funktion. Z.B. für Insti-Zuordnung: Anzahl der Aktien bei Teil-Zuordnung
     * Jeweils LEN=100*/
    public String parameter1 = "";
    public String parameter2 = "";
    public String parameter3 = "";
    public String parameter4 = "";
    public String parameter5 = "";

    /*****************Ab hier nicht in Datenbank! Nur zur internen Verwaltung!************************************/
    public boolean bereitsInDatenbank = false;
    public boolean seitLetztemSpeichernVeraendert = false;
    /**Wird verwendet, um Verarbeitungshinweise anzuzeigen*/
    public String meldungsText = "";
}
