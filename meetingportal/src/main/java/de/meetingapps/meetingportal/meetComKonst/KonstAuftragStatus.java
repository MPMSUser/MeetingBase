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

public class KonstAuftragStatus {
    
    public final static int UNBEKANNT = 0;
    
    /**Der Auftrag wird noch bearbeitet*/
    public final static int IN_ARBEIT=1;
    
    /**Der Auftrag ist fertig, das Ergebnis kann abgerufen werden*/
    public final static int FERTIG=2;
    
    /**Der Auftrag ist als endgültig erledigt gekennzeichnet, d.h. 
     * er wird in Übersichten nicht mehr angeboten.
     */
    public final static int ERLEDIGT=3;

}
