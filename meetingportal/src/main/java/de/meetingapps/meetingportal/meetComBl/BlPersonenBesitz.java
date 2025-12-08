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
package de.meetingapps.meetingportal.meetComBl;

import de.meetingapps.meetingportal.meetComDb.DbBundle;

/**************************Über diese Klasse wird die Zuordnung von Personen zu ihrem Besitz abgefragt (bzw. "High-Level" Verwaltet).
 * 
 * 2 Arten von Zuordnungen:
 * > Meldungen (d.h. die Meldung ist direkt der Person zugeordnet im Sinne von "Aktien gehören dieser Person", also = Aktionär 
 * 		(Bei Fremdbesitzt stimmt das natürlich nicht ganz ...)
 * > Gastmeldung (d.h. die Person hat eine Meldung als Gast - Achtung, das muß nicht zwingend heißen dass sie auch eine Zutrittsberechtigung
 * 		als solche hat - Gastkarten können ja auch wieder storniert werden, die Meldung lebt aber weiter)
 * > Vollmachten (d.h. die Person hat von einer anderen Person eine Vollmacht bekommen, Aktienbesitz zu vertreten)
 * 
 *
 */

/*TODO _Klasse BlPersonenBesitz: wozu wird die überhaupt benötigt? Derzeit quasi keine Funktion*/
public class BlPersonenBesitz {

//    private DbBundle lDbBundle = null;

    public BlPersonenBesitz(DbBundle pDbBundle) {
//        lDbBundle = pDbBundle;
    }

    void lese(int pPersonIdent) {

        /************************Einlesen aller direkten Meldungen zu pPersonIdent************************************
         * Kriterium: alle Meldungen, in denen .personenNatJurIdent=pPersonIdent ist*/

        /**************************Einlesen aller Vollmachten zu pPersonIdent******************************************
         * Ablauf: 
         * > Erst mal alle Willenserklärungen lesen, bei denen pPersonIdent als Vollmachtsempfänger eingetragen ist
         * > Dann für alle diese Willenserklärungen:
         * 		>> Lesen der Willenserklärungskette
         * */
    }
}
