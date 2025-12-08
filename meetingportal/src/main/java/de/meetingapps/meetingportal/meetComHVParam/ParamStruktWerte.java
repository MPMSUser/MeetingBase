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


public class ParamStruktWerte {

    public int identParamStrukt=0;

    public int lfdNr=0;
    
    /**String wird verwendet, wenn nur einer der Werte zulässig ist.
     * int wird verwendet, wenn die Werte addiert werden sollen (nur Bitweise zulässig!) 
     * LEN=500*/
    public String zulaessigeWerteString=null;
    public int zulaessigeWerteInt=0;
    
    /**Wird direkt in der Maske für den jeweiligen Wert angezeigt; bei boolean, oder bei "zulaessigeWerte";
     * externes Table
     * LEN=80
     */
    public String kurztext=null;

    /**Verwendung in Hilfetext.
     * LEN=800*/
    public String langtext=null;
   
    
    
}
