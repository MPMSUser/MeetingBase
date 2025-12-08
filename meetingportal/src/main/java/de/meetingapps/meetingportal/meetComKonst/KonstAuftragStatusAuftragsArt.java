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
package de.meetingapps.meetingportal.meetComKonst;

import de.meetingapps.meetingportal.meetComAllg.CaBug;

public class KonstAuftragStatusAuftragsArt {

  
    
    public static final int ANBINDUNG_AKTIENREGISTER_AENDERN_FEHLER=-1; 
    public static final int ANBINDUNG_AKTIENREGISTER_AENDERN_OFFEN=1; 
    public static final int ANBINDUNG_AKTIENREGISTER_AENDERN_RUECKFRAGE=2; 
    public static final int ANBINDUNG_AKTIENREGISTER_AENDERN_ERLEDIGT=3; 
    public static final int ANBINDUNG_AKTIENREGISTER_AENDERN_ABGELEHNT=4; 
    
    /**Status wird gesetzt, wenn nicht auf Response von Remote-Register gewartet werden
     * muß, weil die Änderung sowieso durchgeführt wird
     */
    public static final int ANBINDUNG_AKTIENREGISTER_AENDERN_GLEICHERLEDIGT=5; 
    
    public static boolean aktienregisterInArbeit(int pStatus) {
        switch (pStatus) {
        case ANBINDUNG_AKTIENREGISTER_AENDERN_OFFEN:return true;
        case ANBINDUNG_AKTIENREGISTER_AENDERN_RUECKFRAGE:return true;
       }
        
        return false;
    }
    
    public static boolean aktienregisterFehlerhaft(int pStatus) {
        switch (pStatus) {
        case ANBINDUNG_AKTIENREGISTER_AENDERN_RUECKFRAGE:return true;
        case ANBINDUNG_AKTIENREGISTER_AENDERN_ABGELEHNT:return true;
               }
        
        return false;
    }

    public static boolean aktienregisterPositivErledigt(int pStatus) {
        switch (pStatus) {
        case ANBINDUNG_AKTIENREGISTER_AENDERN_ERLEDIGT:return true;
        case ANBINDUNG_AKTIENREGISTER_AENDERN_GLEICHERLEDIGT:return true;
               }
        
        return false;
        
    }
    
    public static int liefereCodeZuStatusText_Aktienregister(String pText) {
        switch (pText) {
        case "Offen": return ANBINDUNG_AKTIENREGISTER_AENDERN_OFFEN;
        case "Rückfrage": return ANBINDUNG_AKTIENREGISTER_AENDERN_RUECKFRAGE;
        case "Erledigt": return ANBINDUNG_AKTIENREGISTER_AENDERN_ERLEDIGT;
        case "Abgelehnt": return ANBINDUNG_AKTIENREGISTER_AENDERN_ABGELEHNT;
        case "Offen / in Klärung": return ANBINDUNG_AKTIENREGISTER_AENDERN_OFFEN;
        case "in Bearbeitung": return ANBINDUNG_AKTIENREGISTER_AENDERN_OFFEN;
        case "Widerruf": return ANBINDUNG_AKTIENREGISTER_AENDERN_ABGELEHNT;
        }
        CaBug.drucke("001 pText="+pText);
        return ANBINDUNG_AKTIENREGISTER_AENDERN_FEHLER;
    }
    
    
    
}
//Offen (gelbes Icon)
//Rückfrage (gelbes Icon)
//Erledigt (die Änderungen werden übernommen und können im Portal eingesehen werden)
//Abgelehnt (Nachricht an das Mitglied, dass die Änderungen nicht übernommen werden konnten und das Mitglied sich bitte an die Mitgliederbetreuung wenden soll, angezeigt werden die alten Daten)
//Offen / in Klärung (gelbes Icon)
//Widerruf (nicht relevant für uns)
//Drucken (nicht relevant für uns)
