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

import de.meetingapps.meetingportal.meetComWE.WERoot;

public class WEStubRepSammelkarten extends WERoot {

    /**Bisher vergebener Max-Wert = 1*/
    public int stubFunktion = -1;

    public int arbeitsplatznr = 0;
    public int benutzernr = 0;

    public int pDrucklaufnr = 0;

    /***********Standard Getter und Setter********************/

    public int getStubFunktion() {
        return stubFunktion;
    }

    public void setStubFunktion(int stubFunktion) {
        this.stubFunktion = stubFunktion;
    }

    public int getArbeitsplatznr() {
        return arbeitsplatznr;
    }

    public void setArbeitsplatznr(int arbeitsplatznr) {
        this.arbeitsplatznr = arbeitsplatznr;
    }

    public int getBenutzernr() {
        return benutzernr;
    }

    public void setBenutzernr(int benutzernr) {
        this.benutzernr = benutzernr;
    }

    public int getpDrucklaufnr() {
        return pDrucklaufnr;
    }

    public void setpDrucklaufnr(int pDrucklaufnr) {
        this.pDrucklaufnr = pDrucklaufnr;
    }

}
