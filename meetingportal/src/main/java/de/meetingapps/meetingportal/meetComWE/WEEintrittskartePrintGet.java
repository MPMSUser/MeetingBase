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

public class WEEintrittskartePrintGet {

    private WELoginVerify weLoginVerify = null;

    /*Nummer der Druckdatei. Muß vorher ermittelt werden (wird z.B. bei der Eintrittskartenausstellung im Return
     * mitgegeben)
     */
    private int dateinr = 0;

    /*****************Standard Getter und Setter********************************/

    public WELoginVerify getWeLoginVerify() {
        return weLoginVerify;
    }

    public void setWeLoginVerify(WELoginVerify weLoginVerify) {
        this.weLoginVerify = weLoginVerify;
    }

    public int getDateinr() {
        return dateinr;
    }

    public void setDateinr(int dateinr) {
        this.dateinr = dateinr;
    }

}
