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

/**Für Einzelaktionäre.
 * 
 * Bei Sammelkarten immer Splitting! (nur so kann dynamische Stimmenänderung der Sammelkarten sauber protokolliert werden auch für Abstimmung!)
 */

public class EclAbstimmungMeldungRaw {

    /**Mandant**/
    public int mandant = 0;

    public int ident = 0;

    /**Ident der Meldung, zu der die Abstimmung gehört
     * Achtung - kann 0 sein! Nämlich dann, wenn eine Stimmkarte abgegeben und gelesen wurde, die noch nicht zugeordnet ist!**/
    public int meldungsIdent = 0;

    /**1 => dieser Raw-Satz wurde storniert und wurde dementsprechend bei der Abstimmungsermittlung nicht berücksichtigt*/
    public int wurdeStorniert = 0;

    /**Gemäß KonstWillenserklaerungWeg*/
    public int erteiltAufWeg = 0;

    /**Kartenklasse, die zur Erfassung verwendet wurde
     * Zulässige Klassen: eintrittskartennummer, stimmkartennummer, stimmkartennummerSecond, personenIdent (für App)
     * */
    public int verwendeteKartenklasse = 0;

    /**Fürs Scannen verwendete nummer (also nur z.B. die Stimmkartennummer - nicht der komplette QR-Code)
     * Length=20*/
    public String verwendeteNummer = "";

    /**=0 => konnte nicht verarbeitet werden, z.B. weil nicht präsent. Achtung: Steht auf 1, wenn wg. "Gattung nicht stimmberechtigt"
     *  nicht verarbeitet wurde!*/
    public int wurdeVerarbeitet = 0;

    /**Fehlercode, falls wurdeVerarbeitet==0; oder wurdeVerarbeitet=1, und Fehlercode="wg. Gattung nicht stimmberechtigt"*/
    public int fehlercode = 1;

    public int abstimmungsblock = 0;

    public int arbeitsplatzNr = 0;

    /**Zeitpunkt der Stimmabgabe (soweit bekannt)*/
    public long zeitstempelraw = 0;

    /**Zeitpunkt des Abspeicherns in der Datenbank - z.B. zum Ermitteln, in welchem Import-Schritt das Speichern erfolgte*/
    public String zeitstempel = "";

    /**Einzele Weisungsabgaben - bereits interpretiert
     * Integer-Wert = Wert der Stimmart wie in EnStimmart definiert**/
    public int[] abgabe = new int[200];

    public EclAbstimmungMeldungRaw() {
        int i;
        for (i = 0; i < 200; i++) {
            abgabe[i] = -1;
        }
    }

}
