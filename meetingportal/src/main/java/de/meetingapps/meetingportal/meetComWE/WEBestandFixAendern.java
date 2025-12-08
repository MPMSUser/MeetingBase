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

public class WEBestandFixAendern {

    private WELoginVerify weLoginVerify = null;

    /*Anzahl Aktien für Fix-Anmeldung*/
    private long anzahlAktienFix = 0;
    private int meldeint = 0;

    /*******************Standard Getter und Setter*************************************/

    public WELoginVerify getWeLoginVerify() {
        return weLoginVerify;
    }

    public void setWeLoginVerify(WELoginVerify weLoginVerify) {
        this.weLoginVerify = weLoginVerify;
    }

    public long getAnzahlAktienFix() {
        return anzahlAktienFix;
    }

    public void setAnzahlAktienFix(long anzahlAktienFix) {
        this.anzahlAktienFix = anzahlAktienFix;
    }

    public int getMeldeint() {
        return meldeint;
    }

    public void setMeldeint(int meldeint) {
        this.meldeint = meldeint;
    }

}
