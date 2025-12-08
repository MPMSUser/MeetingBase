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

public class WEMailEK {

    private WELoginVerify weLoginVerify = null;

    /** 1 = erste EK, 2 = zweite EK*/
    private int ek = 0;
    private int eintrittskartePdfNr = 0;
    private String eintrittskarteEmail = "";

    /***************Standard Getter und Setter******************************/
    public WELoginVerify getWeLoginVerify() {
        return weLoginVerify;
    }

    public void setWeLoginVerify(WELoginVerify weLoginVerify) {
        this.weLoginVerify = weLoginVerify;
    }

    public int getEk() {
        return ek;
    }

    public void setEk(int ek) {
        this.ek = ek;
    }

    public int getEintrittskartePdfNr() {
        return eintrittskartePdfNr;
    }

    public void setEintrittskartePdfNr(int eintrittskartePdfNr) {
        this.eintrittskartePdfNr = eintrittskartePdfNr;
    }

    public String getEintrittskarteEmail() {
        return eintrittskarteEmail;
    }

    public void setEintrittskarteEmail(String eintrittskarteEmail) {
        this.eintrittskarteEmail = eintrittskarteEmail;
    }

}
