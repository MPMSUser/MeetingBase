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

import java.util.List;

public class ParamStruktGruppenHeader {

    static public final int  KONST_IDENT_MODULE_KONFIGURIERBAR=1;
    /**Fest definierte Gruppen 1 bis 1000:
     * 1 = Module konfigurierbar.
     */
    public int identGruppe=0;
    
    /**Sortierkriterium für Auswahlmasken*/
    public int lfdNr=0;
    
    /**LEN=50*/
    public String tabBeschriftung="";
    
    /**LEN=100*/
    public String ueberschrift="";
    
    /**LEN=200*/
    public String buttonBeschriftung="";
    
    /**LEN=2000*/
    public String beschreibung="";

    /**bitweise Selektion von konfigurierbaren Gruppenauswahlen*/
    public long inGruppenauswahlEnthalten=0;
    
    /**=1 => diese Gruppen wird in den automatisch generierten Gruppenauswahlen nicht angezeigt*/
    public int fuerGruppenauswahlGesperrt=0;
    
    public long anzeigenWennBerechtigungVorhanden=0;
    
    /*+++++++++++++++++++Nicht in Datenbank++++++++++++++++++++*/
    
    /**Zugehörige Gruppenelemente*/
    public List<ParamStruktGruppen> paramStruktGruppenelemente=null;
    
    
}
