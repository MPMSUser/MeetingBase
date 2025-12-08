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
package de.meetingapps.meetingportal.meetComWE;

import de.meetingapps.meetingportal.meetComEntities.EclScan;
import de.meetingapps.meetingportal.meetComEntities.EclVerarbeitungsLauf;
import de.meetingapps.meetingportal.meetComEntities.EclVerarbeitungsProtokoll;

public class WELaufInfoGetRC extends WERootRC {

    /**Rückgabe-Art
     * 0 = Liste der Verarbeitungsläufe in verarbeitungsLaeufe
     * Ansonsten:
     * KonstVerarbeitungsArt.scanLaufAnmeldestelle in verarbeitungsProtokoll, scanlauf
     * 	scanLaufSRVHV
     * 	scanLaufAbstimmung*/
    public int rueckgabeArt = 0;

    public EclVerarbeitungsLauf[] verarbeitungsLaeufe = null;
    public EclVerarbeitungsProtokoll[] verarbeitungsProtokoll = null;
    public EclScan[] scanlauf = null;

}
