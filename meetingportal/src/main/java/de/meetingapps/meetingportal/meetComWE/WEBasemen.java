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
package de.meetingapps.meetingportal.meetComWE;

public class WEBasemen {

    /************************Identifikation des aufrufenden Programms*******************************/
    /**Zugangs-Identifikation basemen*/
    public String uKennung = "";
    public String uPasswort = "";

    /**************************Mandanten-Auswahl*************************************************/
    /**Identifikation des Mandanten auf Basemen-Seite, i.d.R. von Clearstream vergebene
     * eindeutige Nummer
     * LEN =6, (i.d.R. nur 4)*/
    public String mandant_Basemen = "";

    /**ID der HV auf Basemen-Seite; String, der mit Emittenten-Table von betterMeeting
     * gematchet wird und so HV identifiziert wird
     * LEN=50*/
    public String hvId_Basemen = "";

    /**Produktionskennzeichen
     * 1=Produktion
     * 2=Test
     */
    public int produktionsKennzeichen = 2;

    /***************************Funktionsparameter**************************************************/

    /**Angabe der gewünschten Funktion - siehe KonstBasemenFunkt*/
    public int funktionscode = 0;

    /**Parameter - je nach aufgerufender Funktion*/
    public String aktionaersnummer = "";

    /**Liste der Parameter. Bedeutung und Reihenfolge bei jeweiliger Funktion festgelegt.
     * Übergeben werden z.B. gewünschte Selektionen (bei Statistiken), gewünschte Sortierungen (bei Listen von mehreren
     * Aktionären)*/
    public String[] parameterListe = null;

}
