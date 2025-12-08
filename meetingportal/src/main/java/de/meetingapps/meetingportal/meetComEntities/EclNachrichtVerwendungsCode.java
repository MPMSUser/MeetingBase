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

public class EclNachrichtVerwendungsCode {

    /**Keine automatische Vergabe!*/
    public int ident = 0;

    /**LEN=80*/
    public String beschreibung = "";

    public int identNachrichtBasisText = 0;

    /**=1 => dieser Code kann nur bei automatisch generierten Mails verwendet werden -
     * da abhängig von diesem Code z.B. die Nachrichtenparameter fest vorgegeben sind
     * und beim Empfänger weiterverarbeitet werden können.
     * Bsp.:Weisungsempfehlung, so dass über einen Mail-Parameter direkt auf die Empfehlung
     * verwiesen wird.  
     */
    public int reserviertFuerSystem = 0;

}
