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

/**In dieser Klasse / Table werden die Kennungen (derzeit manuell) eingetragen, 
 * für die immer wieder spezielle Passwörter eingestellt werden sollen.
 * Nur für Demo-/Vorführzwecke!
 * 
 * Konkret:
 * > Manueller Eintrag in dieser Datei
 * > Die hier eingetragenen Benutzer können mit spezieller Funktion in uLogin immer wieder auf dieses Passwort zurückgesetzt werden
 * > Beim Passwort zurücksetzen werden anschließend diese Benutzer auch immer wieder auf das hier eingetragene Passwort zurückgesetzt
 * 
 * Achtung: diese Daten werden beim Zurücksetzen des AKtienregisters NICHT gelöscht!
 * Hintergrund:
 * Beim "normalen" Produktionsbestand wird diese Table sowieso nicht verwendet.
 * Beim Testdatenbestand können so voreingestellte Kennungen mit Passwörter für Demos erhalten bleiben.
 * 
 * @author N.N
 *
 */
public class EclLoginDatenDemo {

    /**
     * Identisch mit der Kennung loginKennung in EclLoginDaten
     * LEN=20, UNIQUE
     */
    public String loginKennung = "";

    /**LEN=30*/
    public String passwortDemo="";
    
    /**
     * Beschreibung, wofür diese Kennung verwendet wird
     * LEN=200*/
    public String verwendungszweck="";
}
