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

import java.util.List;

import de.meetingapps.meetingportal.meetComEntities.EclInsti;
import de.meetingapps.meetingportal.meetComEntities.EclInstiBestandsZuordnung;
import de.meetingapps.meetingportal.meetComEntities.EclInstiSubZuordnung;
import de.meetingapps.meetingportal.meetComEntities.EclSuchlaufBegriffe;
import de.meetingapps.meetingportal.meetComWE.WERoot;

public class WEStubBlInsti extends WERoot {

    /**Bisher vergebener Max-Wert = 9*/
    public int stubFunktion = -1;

    public int pInstiIdent = 0;
    public int pAktionaersIdent = 0;

    /**Auch pAktuelleInsti*/
    public EclInsti insti = null;

    public EclSuchlaufBegriffe suchlaufBegriffe = null;

    public boolean pGlobal = false;;
    public List<EclInstiSubZuordnung> pInstiSubZuordnung = null;;

    public EclInstiBestandsZuordnung[] pZuordnungenLoeschen = null;

    /***********Standard Getter und Setter********************/

    public int getStubFunktion() {
        return stubFunktion;
    }

    public void setStubFunktion(int stubFunktion) {
        this.stubFunktion = stubFunktion;
    }

    public int getpInstiIdent() {
        return pInstiIdent;
    }

    public void setpInstiIdent(int pInstiIdent) {
        this.pInstiIdent = pInstiIdent;
    }

    public EclInsti getInsti() {
        return insti;
    }

    public void setInsti(EclInsti insti) {
        this.insti = insti;
    }

}
