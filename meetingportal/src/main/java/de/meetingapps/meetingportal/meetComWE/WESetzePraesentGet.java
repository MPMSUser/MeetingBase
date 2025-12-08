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

public class WESetzePraesentGet {

    private WELoginVerify weLoginVerify = null;
    private int meldungsIdent = 0;
    private int kartenart = 0;
    private int kartennr = 0;
    private int identPersonNatJur = 0;

    /*********************Standard Setter/Getter**********************************************/

    public WELoginVerify getWeLoginVerify() {
        return weLoginVerify;
    }

    public void setWeLoginVerify(WELoginVerify weLoginVerify) {
        this.weLoginVerify = weLoginVerify;
    }

    public int getMeldungsIdent() {
        return meldungsIdent;
    }

    public void setMeldungsIdent(int meldungsIdent) {
        this.meldungsIdent = meldungsIdent;
    }

    public int getKartenart() {
        return kartenart;
    }

    public void setKartenart(int kartenart) {
        this.kartenart = kartenart;
    }

    public int getKartennr() {
        return kartennr;
    }

    public void setKartennr(int kartennr) {
        this.kartennr = kartennr;
    }

    public int getIdentPersonNatJur() {
        return identPersonNatJur;
    }

    public void setIdentPersonNatJur(int identPersonNatJur) {
        this.identPersonNatJur = identPersonNatJur;
    }

}
