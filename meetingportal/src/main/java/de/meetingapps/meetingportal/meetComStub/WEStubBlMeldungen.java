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

import de.meetingapps.meetingportal.meetComEntities.EclMeldung;
import de.meetingapps.meetingportal.meetComWE.WERoot;

public class WEStubBlMeldungen extends WERoot {

    public int stubFunktion = -1;
    public int mandant = 0;
    public EclMeldung eclMeldung;
    public Boolean mitPersonNatJur;

    public WEStubBlMeldungen() {

    }

    public WEStubBlMeldungen(int stubFunktion, int mandant) {
        this.stubFunktion = stubFunktion;
        this.mandant = mandant;
    }

    public int getStubFunktion() {
        return stubFunktion;
    }

}
