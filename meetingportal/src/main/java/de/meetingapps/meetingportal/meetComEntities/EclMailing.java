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

public class EclMailing {
    
    /**Eindeutige, interne Nummer*/
    public int mailingIdent = 0;
    public long db_version = 0;
    
    /**
     * Name der Mailvorlage
     * LEN=200
     */
    public String name = "";
    /**
     * Mail-Betreff
     * LEN=600
     */
    public String betreff = "";
    
    /**
     * Bitte als Text oder Longtext in die Datenbank
     * HTML-Code für Mailversand als HTML
     */
    public String htmlMail = "";
    /**
     * Bitte als Text oder Longtext in die Datenbank
     * Fallback-Text, wenn Client kein HTML-Mail unterstützt 
     */
    public String alternativMailText = "";
}
