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

public class WEBestandAnmelden {

    private WELoginVerify weLoginVerify = null;

    private int aktienregisterIdent = 0;

    /* 1 = 1 Anmeldung, kompletter Bestand
     * 2 = 2 Anmeldungen, möglichst gleichmäßig aufgeteilt
     * 11= Anmeldung Fix (=> anzahlAktienFix)
     */
    private int anmeldeart = 0;

    /*Anzahl Aktien für Fix-Anmeldung*/
    private long anzahlAktienFix = 0;

    /*******************Standard Getter und Setter*************************************/

    public WELoginVerify getWeLoginVerify() {
        return weLoginVerify;
    }

    public void setWeLoginVerify(WELoginVerify weLoginVerify) {
        this.weLoginVerify = weLoginVerify;
    }

    public int getAktienregisterIdent() {
        return aktienregisterIdent;
    }

    public void setAktienregisterIdent(int aktienregisterIdent) {
        this.aktienregisterIdent = aktienregisterIdent;
    }

    public int getAnmeldeart() {
        return anmeldeart;
    }

    public void setAnmeldeart(int anmeldeart) {
        this.anmeldeart = anmeldeart;
    }

    public long getAnzahlAktienFix() {
        return anzahlAktienFix;
    }

    public void setAnzahlAktienFix(long anzahlAktienFix) {
        this.anzahlAktienFix = anzahlAktienFix;
    }

}
