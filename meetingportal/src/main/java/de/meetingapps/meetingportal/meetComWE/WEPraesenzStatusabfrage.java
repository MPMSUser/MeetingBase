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

import de.meetingapps.meetingportal.meetComKonst.KonstProgrammFunktionHV;

/**Übergabeparameter für "normale" Erfassung, d.h. Delayed-Funktion und zeitliche Eingabeeinschränkungen, z.B. bei SRV, sind aktiv*/
public class WEPraesenzStatusabfrage {

    public WELoginVerify weLoginVerify = null;

    /**Eingelese Nummer in String-Form. Sollte sie nicht eindeutig sein, muß sie
     * zusammen mit kartenart eindeutig sein
     */
    public String identifikationString = "";

    /**gibt an, welche kartenart mit identifikationString / identifikationNummer verbunden ist. Falls in String 
     * vollständiger String enthalten (mit Aktionsnummer), dann überflüssig - muß in diesem 
     * Fall auf 0 stehen.
     * 0 = kartenart ist nicht separat spezifiziert
     * 1 = Eintrittskartennummer
     * 2 = Gastkartennummer
     * 3 = Stimmblocknummer
     * 21 = 
     */
    public int kartenklasse = 0;

    /**Funktion, aus der heraus der Aufruf abgesetzt wurde. In Abhängigkeit dieses Wertes erfolgen ggf. Fehlermeldungen,
     * falls die Bearbeitung dieser Karte in dieser Funktion (noch) nicht möglich ist
     * 1 = normale Akkreditierung (Schalter)
     * 2 = Vorab-Akkreditierung (in Vorbereitung!!)
     * 3 = Vertreternacherfassung
     * 4 = ServiceCounter / Supervisor
     */
    public int programmFunktionHV = KonstProgrammFunktionHV.unbekannt;

    /**************Standard Getter und Setter************************************/

    public WELoginVerify getWeLoginVerify() {
        return weLoginVerify;
    }

    public void setWeLoginVerify(WELoginVerify weLoginVerify) {
        this.weLoginVerify = weLoginVerify;
    }

    public String getIdentifikationString() {
        return identifikationString;
    }

    public void setIdentifikationString(String identifikationString) {
        this.identifikationString = identifikationString;
    }

    public int getKartenklasse() {
        return kartenklasse;
    }

    public void setKartenklasse(int kartenklasse) {
        this.kartenklasse = kartenklasse;
    }

    public int getFunktion() {
        return programmFunktionHV;
    }

    public void setFunktion(int funktion) {
        this.programmFunktionHV = funktion;
    }

}
