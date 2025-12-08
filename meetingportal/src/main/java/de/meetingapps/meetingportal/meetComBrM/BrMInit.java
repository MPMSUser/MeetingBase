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
package de.meetingapps.meetingportal.meetComBrM;


import de.meetingapps.meetingportal.meetingportTController.TPermanentSession;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

/**Hinweis zu den gesamten BrM-Funktionen:
 * 
 * In diesen Klassen wird die Kommunikation hin zum Aktienregister abgebildet.
 * 
 * Eine Datenhaltung innerhalb dieser Klassen ist nicht vorgesehen, da alle
 * Request-Scope.
 * Speicherung über eine Session hinweg erfolgt ausschließlich in TController.TPermanentSession.
 */

@RequestScoped
@Named
public class BrMInit {

    private @Inject TPermanentSession tPermanentSession;
    
    /**Wird beim Login-Aufgerufen und initialisiert die komplette Kommunikation hin zum Aktienregister*/
    public int init() {
        
        tPermanentSession.setJwt_token(null);
        tPermanentSession.setJwt_exp_date(null);
        return 1;
    }
    
    /**Wird aufgerufen für Initialisierung, ohne dass ein Mitglied eingeloggt ist.
     * Also z.B. zum Abholen der Länderliste
     */
    public int initOhneMitglied() {
        
        return 1;
    }
}
