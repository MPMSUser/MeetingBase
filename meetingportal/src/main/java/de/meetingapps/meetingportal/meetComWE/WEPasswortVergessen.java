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

public class WEPasswortVergessen extends WERoot {

    /**Alle Felder siehe AControllerPasswortVergessenSession*/

    public String loginKennung = ""; //loginKennung

    public String emailAdresse = ""; //eMailAdresse

    public String strasse = "";
    public String ort = "";

    public String geburtsdatum = "";

    /**Falls sowohl bei E-Mail-Registrierung als auch bei Nicht-Email-Registrierung der (halb)automatische Weg
     * angeboten wird (also Eingabe von Email, oder Eingabe von Straße / Ort), dann muß eine Auswahl für den
     * Aktionär angeboten werden => auswahlAnbieten=true;
     */
    public boolean auswahlAnbieten = false;

    /**Falls rein manuelles Verfahren, dann keinerlei Eingabe ermöglichen.*/
    public boolean eingabeAnbieten = false;

    /**Falls auswahlAnbieten=true, dann wird über dieses Feld gesteuert welche der beiden Varianten (E-Mail oder Straße/Ort)
     * vom Aktionär ausgewählt wurde.
     * 1=Email-Adresse
     * 2=Strasse / Ort
     */
    public String eingabeSelektiert = "0";

    /**1 = Text 231, "nichts einzugeben"
     * 2 = Text 208. "nur Email eingeben"
     * 3 = Text 233, "nur Anschrift einzugeben"
     * 4 = Text 232, ""Auswahl möglich"
     */
    public int textVariante = 0;

    /**Standardwert False. Wird bei Anforderung über App auf true gesetzt - dann wird nur Code verschickt, nicht Link*/
    public boolean wurdeUeberAppAngefordert = false;

    /**sprache - hier wird die Sprache der App übermittelt, die gerade aktiv ist, um die Sprache
     * des Emails zu setzen*/
    public int sprache = 1;
}
