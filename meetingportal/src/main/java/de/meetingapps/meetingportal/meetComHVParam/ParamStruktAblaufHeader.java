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
package de.meetingapps.meetingportal.meetComHVParam;


public class ParamStruktAblaufHeader {

    static public final int  KONST_IDENT_MODULE_KONFIGURIERBAR=1;
    /**Fest definierte Gruppen 1 bis 1000:
     * 1 = Module konfigurierbar.
     */
    public int identAblauf=0;
    
    /**
     * 1 = Abstimmungsablauf
     */
    public int ablaufArt=0; 
    
    /**Sortierkriterium für Auswahlmasken*/
    public int lfdNr=0;
    
    /**LEN=50*/
    public String kurzBeschreibung="";
    
    /**LEN=2000*/
    public String beschreibung="";

    /**bitweise Selektion von konfigurierbaren Gruppenauswahlen*/
    public long inAblaufauswahlEnthalten=0;
    
    /**=1 => diese Gruppen wird in den automatisch generierten Gruppenauswahlen nicht angezeigt*/
    public int fuerAblaufauswahlGesperrt=0;
    
    public long anzeigenWennBerechtigungVorhanden=0;
    
    
    
}
