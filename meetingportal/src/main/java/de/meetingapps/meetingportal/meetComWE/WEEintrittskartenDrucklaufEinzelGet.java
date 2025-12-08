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

public class WEEintrittskartenDrucklaufEinzelGet {

    private WELoginVerify weLoginVerify = null;

    /**1 = Gast, 2=Aktionär*/
    private int gastOderAktionaer = 2;

    private String ekNummer = "";
    private String ekNummerNeben = "";

    /**0=Standard, Papierversand erfolgt nur wenn angefordert;
     * 1=Papierversand erfolgt auch für die "Selbstgedruckten" oder "Selbstgemailten"
     */
    private int ekVersandFuerAlleImPortalAngefordertenSelektion=0; 

    /*******************Standard Getter und Setter*************************************/

    public WELoginVerify getWeLoginVerify() {
        return weLoginVerify;
    }

    public void setWeLoginVerify(WELoginVerify weLoginVerify) {
        this.weLoginVerify = weLoginVerify;
    }

    public String getEkNummer() {
        return ekNummer;
    }

    public void setEkNummer(String ekNummer) {
        this.ekNummer = ekNummer;
    }

    public String getEkNummerNeben() {
        return ekNummerNeben;
    }

    public void setEkNummerNeben(String ekNummerNeben) {
        this.ekNummerNeben = ekNummerNeben;
    }

    public int getGastOderAktionaer() {
        return gastOderAktionaer;
    }

    public void setGastOderAktionaer(int gastOderAktionaer) {
        this.gastOderAktionaer = gastOderAktionaer;
    }

    public int getEkVersandFuerAlleImPortalAngefordertenSelektion() {
        return ekVersandFuerAlleImPortalAngefordertenSelektion;
    }

    public void setEkVersandFuerAlleImPortalAngefordertenSelektion(int ekVersandFuerAlleImPortalAngefordertenSelektion) {
        this.ekVersandFuerAlleImPortalAngefordertenSelektion = ekVersandFuerAlleImPortalAngefordertenSelektion;
    }

}
