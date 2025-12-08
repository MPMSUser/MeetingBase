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
package de.meetingapps.meetingportal.meetComStub;

import de.meetingapps.meetingportal.meetComEntities.EclAktienregister;
import de.meetingapps.meetingportal.meetComEntities.EclInsti;
import de.meetingapps.meetingportal.meetComEntities.EclInstiBestandsZuordnung;
import de.meetingapps.meetingportal.meetComEntities.EclInstiSubZuordnung;
import de.meetingapps.meetingportal.meetComEntities.EclMeldung;
import de.meetingapps.meetingportal.meetComEntities.EclSuchlaufBegriffe;
import de.meetingapps.meetingportal.meetComEntities.EclUserLogin;
import de.meetingapps.meetingportal.meetComWE.WERootRC;

public class WEStubBlInstiRC extends WERootRC {

    public EclInsti[] rcInsti = null;

    public EclSuchlaufBegriffe rcSuchlaufBegriffe = null;
    public EclInstiSubZuordnung[] rcEclInstiSubZuordnungUeblichArray = null;
    public EclInstiSubZuordnung[] rcEclInstiSubZuordnungTatsaechlichArray = null;

    public EclInsti insti = null;

    public EclUserLogin[] rcUserLoginZuInsti = null;

    public long rcAktienzahl = 0;

    /**Rückgabe für fuelleInstiBestandsZuordnung - 
     * einmal für Aktienregister, und einmal für Meldungen
     */
    public EclInstiBestandsZuordnung[] rcRegInstiBestandsZuordnung = null;
    public EclAktienregister[] rcRegAktienregister = null;
    public EclMeldung[] rcRegMeldung = null;
    public EclUserLogin[] rcRegUserLogin = null;

    public EclInstiBestandsZuordnung[] rcMeldInstiBestandsZuordnung = null;
    public EclAktienregister[] rcMeldAktienregister = null;
    public EclMeldung[] rcMeldMeldung = null;
    public EclUserLogin[] rcMeldUserLogin = null;

}
