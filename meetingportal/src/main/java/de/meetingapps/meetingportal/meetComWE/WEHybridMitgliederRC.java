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

import java.util.List;

import de.meetingapps.meetingportal.meetComEntities.EclAbstimmungMeldung;
import de.meetingapps.meetingportal.meetComEntities.EclMeldung;

/**Für Kommunikation mit dem Online-Server für Hybrid-Veranstaltungen. 
 * NICHT für Stub BlHybridMitglieder!
 */
public class WEHybridMitgliederRC extends WERootRC {

    /**Alle Meldungen, die auf dem Server präsent sind*/
    public List<EclMeldung> rcPraesenteMeldungenAufServer=null;

    public List<EclAbstimmungMeldung> rcAbstimmungMeldungAufServer=null;

}
