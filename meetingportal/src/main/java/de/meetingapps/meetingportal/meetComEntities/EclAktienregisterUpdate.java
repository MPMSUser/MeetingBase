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

public class EclAktienregisterUpdate {

    /**Mandantennummer*/
    public int mandant = 0;

    /** eindeutiger Key für Aktienregistersatz (zusammen mit mandant), der unveränderlich ist. 
     * Wird in diesem Fall bei DBAktienregisterZusatz.insert nicht neu vergeben, sondern muß bereits gefüllt sein,
     * da dieses Feld der "Foreign-Key" zu EclAktienregisterEintrag ist.
     */

    public int aktienregisterIdent = 0;

    /*Zur freien Verwendung*/
    public int kennzeichen = 0;

}
