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

public class WEVersandAdressePruefenGet {

    private WELoginVerify weLoginVerify = null;

    private boolean nurInternet = false;
    private boolean nurNochNichtGeprueft = false;
    private boolean nurAbweichendeEingegeben = false;

    /*******************Standard Getter und Setter*************************************/

    public WELoginVerify getWeLoginVerify() {
        return weLoginVerify;
    }

    public void setWeLoginVerify(WELoginVerify weLoginVerify) {
        this.weLoginVerify = weLoginVerify;
    }

    public boolean isNurInternet() {
        return nurInternet;
    }

    public void setNurInternet(boolean nurInternet) {
        this.nurInternet = nurInternet;
    }

    public boolean isNurNochNichtGeprueft() {
        return nurNochNichtGeprueft;
    }

    public void setNurNochNichtGeprueft(boolean nurNochNichtGeprueft) {
        this.nurNochNichtGeprueft = nurNochNichtGeprueft;
    }

    public boolean isNurAbweichendeEingegeben() {
        return nurAbweichendeEingegeben;
    }

    public void setNurAbweichendeEingegeben(boolean nurAbweichendeEingegeben) {
        this.nurAbweichendeEingegeben = nurAbweichendeEingegeben;
    }

}
