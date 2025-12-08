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

import de.meetingapps.meetingportal.meetComEntities.EclEmittenten;
import de.meetingapps.meetingportal.meetComEntities.EclIsin;
import de.meetingapps.meetingportal.meetComEntities.EclParameterSet;
import de.meetingapps.meetingportal.meetComEntities.EclPortalText;
import de.meetingapps.meetingportal.meetComHVParam.HVParam;
import de.meetingapps.meetingportal.meetComWE.WERoot;

public class WEStubMandantAnlegen extends WERoot {

    /**Bisher vergebener Max-Wert = 9*/
    public int stubFunktion = -1;

    public EclEmittenten pEmittenten = null;
    public EclParameterSet pParameterSet = null;
    public HVParam ergHVParam=null;
    public List<EclIsin> isinListe=null;
    public EclPortalText portalTexteArray[] = null;

    /***********Standard Getter und Setter********************/

    public int getStubFunktion() {
        return stubFunktion;
    }

    public void setStubFunktion(int stubFunktion) {
        this.stubFunktion = stubFunktion;
    }

    public EclEmittenten getpEmittenten() {
        return pEmittenten;
    }

    public void setpEmittenten(EclEmittenten pEmittenten) {
        this.pEmittenten = pEmittenten;
    }

    public EclParameterSet getpParameterSet() {
        return pParameterSet;
    }

    public void setpParameterSet(EclParameterSet pParameterSet) {
        this.pParameterSet = pParameterSet;
    }

    public HVParam getErgHVParam() {
        return ergHVParam;
    }

    public void setErgHVParam(HVParam ergHVParam) {
        this.ergHVParam = ergHVParam;
    }

    public List<EclIsin> getIsinListe() {
        return isinListe;
    }

    public void setIsinListe(List<EclIsin> isinListe) {
        this.isinListe = isinListe;
    }

    public EclPortalText[] getPortalTexteArray() {
        return portalTexteArray;
    }

    public void setPortalTexteArray(EclPortalText[] portalTexteArray) {
        this.portalTexteArray = portalTexteArray;
    }


}
