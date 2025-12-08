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

import java.io.Serializable;

public class ParamModuleKonfigurierbar implements Serializable {
    private static final long serialVersionUID = -7763647215050916128L;

    /**Kommt aus EclEmittenten.portalVorhanden*/
    public boolean aktionaersportal = true;

    public boolean briefwahl = true; //50

    /**Muß aktiviert werden, wenn Weisungen aus einem externen System (also z.B.
     * von arfuehrer001) per Schnittstelle übernommen werden sollen
     */
    public boolean weisungenSchnittstelleExternesSystem = false; //52

    /**Kommt aus EclEmittenten.emittentenPortalVorhanden*/
    public boolean emittentenportal = true;

    /**Kommt aus EclEmittenten.registerAnbindungVorhanden*/
    public boolean registeranbindung = true;

    public boolean elektronischesTeilnehmerverzeichnis = true; //41

    /**Muß aktiviert werden, wenn die elektronische Weisungserfassung entweder in der App
     * oder in einem elektronischen Weisungsterminal zur Verfügung gestellt wird
     */
    public boolean elektronischeWeisungserfassungHV = true; //47

    public boolean tabletAbstimmung = true; //42
    public boolean scannerAbstimmung = true; //51

    /**Kommt aus EclEmittenten.appVorhanden*/
    public boolean hvApp = true;

    public boolean hvAppAbstimmung = true; //44

    /**TODO _App: nicht verwendbar! Grund: zum Zeitpunkt wo benötigt, stehen Parameter nicht zur Verfügung :-(. Da auch derzeit nur für Test gedacht,
     * erübrigt sich erst mal die Frage :-( ...
     */
    public boolean hvAppHVFunktionen = false; //49

    public boolean elektronischerStimmblock = true; //45
    public boolean onlineTeilnahme = true; //46
    
    /**Bezogen auf die Versammlung selbst (Portal im Vorfeld nicht betroffen)
     * 0=uneingeschränkt
     * 1=rein virtuelle HV
     * 2=rein Präsenz-HV
     */
    public int hvForm=0; //rbHvForm0 bis 2; 53
    
    //cbOnlineTeilnahme
   
    public boolean englischeAgenda = true; //48
    
    public boolean mehrereGattungen = true; //54

}

/*
    	if (SHVParam.param.paramModuleKonfigurierbar.aktionaersportal){

*/