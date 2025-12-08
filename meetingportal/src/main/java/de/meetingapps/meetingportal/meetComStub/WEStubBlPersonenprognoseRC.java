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

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

import de.meetingapps.meetingportal.meetComEntities.EclKoordinaten;
import de.meetingapps.meetingportal.meetComEntities.EclMeldung;
import de.meetingapps.meetingportal.meetComEntities.EclPersonenprognose;
import de.meetingapps.meetingportal.meetComWE.WERootRC;

public class WEStubBlPersonenprognoseRC extends WERootRC implements Serializable {
    private static final long serialVersionUID = 1L;

    public EclKoordinaten hvKoordinaten = null;
    public int prediction = 0;
    public int presentPersons = 0;
    public int regShareholder = 0;
    public int shares = 0;
    public int anzRegMeldung = 0;

    //	Timestamp in UTC Time convert mit 
    //	EclPersonenprognose.getLocalDateTime(Timestamp);
    public Timestamp update = null;

    public EclMeldung[] rcRegMeldung = null;
    public EclMeldung[] rcPraesentMeldung = null;

    public List<EclMeldung> listRegMeldung = null;
    public List<EclMeldung> listPraesentMeldung = null;
    public List<EclPersonenprognose> listPp = null;
    public List<Double> distances = null;
}
