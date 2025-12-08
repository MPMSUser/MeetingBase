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

/**Versionsverwaltung für die App
 * In App: siehe EdbVersion*/
public class EclAppVersion {

    public int mandant = 0;
    /** Satz 1, Mandant 0: Version der Datenbankstruktur. 
     *   Wird benötigt, um beim App-Start zu erkennen, ob die App
     *      möglicherweise upgedated wurde und deshalb eine Datenbank-Umarbeitung erforderlich ist.
     *      Wird auf Server nicht benötigt - nur Abgleich zwischen lokaler App-Version und Datenbankverson
     *      durch die App selbst.
     *      
     * Satz 2, Mandant 0: Version der übergreifenden Texte
     * Satz 3, Mandant <mdnr>: Version der mandantenspeifischen Texte
     * Satz 4, Mandant <mdnr>: Version des Logos des Mandanten
     * 
     * Wenn nicht vorhanden, dann bedeutet dies: Datenbank ist noch nicht initialisiert.
     */
    public int ident = 0;

    /**Für Satz 1 derzeit keine Konvention.
     * 
     * Für Satz 2 und 4: Integer-Wert, da beim Textabgleich zwischen App und Server alle Texte
     * übertragen werden, die eine größere Versionsnummer haben als in der App aktuell
     * gespeichert. Vergleich erfolgt auf Integer.
     * Length=10*/
    public String versionsnummer = "";

}
