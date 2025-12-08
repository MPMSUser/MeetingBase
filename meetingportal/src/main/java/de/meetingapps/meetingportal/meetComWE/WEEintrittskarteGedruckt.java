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

import de.meetingapps.meetingportal.meetComEntities.EclWillenserklaerung;
import de.meetingapps.meetingportal.meetComEntities.EclWillenserklaerungZusatz;

public class WEEintrittskarteGedruckt {

    private WELoginVerify weLoginVerify = null;

    private List<EclWillenserklaerung> listWillenserklaerung = null;
    private List<EclWillenserklaerungZusatz> listWillenserklaerungZusatz = null;
    private int drucklauf = 0;

    /*******************Standard Getter und Setter*************************************/

    public WELoginVerify getWeLoginVerify() {
        return weLoginVerify;
    }

    public void setWeLoginVerify(WELoginVerify weLoginVerify) {
        this.weLoginVerify = weLoginVerify;
    }

    public int getDrucklauf() {
        return drucklauf;
    }

    public void setDrucklauf(int drucklauf) {
        this.drucklauf = drucklauf;
    }

    public List<EclWillenserklaerung> getListWillenserklaerung() {
        return listWillenserklaerung;
    }

    public void setListWillenserklaerung(List<EclWillenserklaerung> listWillenserklaerung) {
        this.listWillenserklaerung = listWillenserklaerung;
    }

    public List<EclWillenserklaerungZusatz> getListWillenserklaerungZusatz() {
        return listWillenserklaerungZusatz;
    }

    public void setListWillenserklaerungZusatz(List<EclWillenserklaerungZusatz> listWillenserklaerungZusatz) {
        this.listWillenserklaerungZusatz = listWillenserklaerungZusatz;
    }

}
