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

import de.meetingapps.meetingportal.meetComEclM.EclZugeordneteMeldungM;

public class WEOeffentlicheIDPruefen {

    private WELoginVerify weLoginVerify = null;

    /*Beschreibungen siehe ADlgVariablen*/
    private String ausgewaehlteHauptAktion = "";
    private String ausgewaehlteAktion = "";

    private String zielOeffentlicheID = "";

    private EclZugeordneteMeldungM eclZugeordneteMeldungM = null;

    /***************Ab hier Standard-Getter und Setter*************************************************/

    public WELoginVerify getWeLoginVerify() {
        return weLoginVerify;
    }

    public void setWeLoginVerify(WELoginVerify weLoginVerify) {
        this.weLoginVerify = weLoginVerify;
    }

    public String getAusgewaehlteHauptAktion() {
        return ausgewaehlteHauptAktion;
    }

    public void setAusgewaehlteHauptAktion(String ausgewaehlteHauptAktion) {
        this.ausgewaehlteHauptAktion = ausgewaehlteHauptAktion;
    }

    public String getAusgewaehlteAktion() {
        return ausgewaehlteAktion;
    }

    public void setAusgewaehlteAktion(String ausgewaehlteAktion) {
        this.ausgewaehlteAktion = ausgewaehlteAktion;
    }

    public String getZielOeffentlicheID() {
        return zielOeffentlicheID;
    }

    public void setZielOeffentlicheID(String zielOeffentlicheID) {
        this.zielOeffentlicheID = zielOeffentlicheID;
    }

    public EclZugeordneteMeldungM getEclZugeordneteMeldungM() {
        return eclZugeordneteMeldungM;
    }

    public void setEclZugeordneteMeldungM(EclZugeordneteMeldungM eclZugeordneteMeldungM) {
        this.eclZugeordneteMeldungM = eclZugeordneteMeldungM;
    }

}
