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

public class WEEintrittskartenDrucklaufGet {

    private WELoginVerify weLoginVerify = null;

    private boolean erstDruck = false;
    private int drucklauf = 0;
    private boolean nurGepruefte = false;

    /**1 = Gast, 2=Aktionär*/
    private int gastOderAktionaer = 2;

    /**0=alle, 1=nur Portal, 2=nur Anmeldestelle*/
    private int wegSelektion = 0;

    /**0=alle, 1=nur Inland, 2=nur Ausland*/
    private int landSelektion = 0;

    /**0=Standard, Papierversand erfolgt nur wenn angefordert;
     * 1=Papierversand erfolgt auch für die "Selbstgedruckten" oder "Selbstgemailten"
     */
    private int ekVersandFuerAlleImPortalAngefordertenSelektion=0; 

    private String vonEkNummer = "";
    private String bisEkNummer = "";

    /*******************Standard Getter und Setter*************************************/

    public WELoginVerify getWeLoginVerify() {
        return weLoginVerify;
    }

    public void setWeLoginVerify(WELoginVerify weLoginVerify) {
        this.weLoginVerify = weLoginVerify;
    }

    public boolean isErstDruck() {
        return erstDruck;
    }

    public void setErstDruck(boolean erstDruck) {
        this.erstDruck = erstDruck;
    }

    public int getDrucklauf() {
        return drucklauf;
    }

    public void setDrucklauf(int drucklauf) {
        this.drucklauf = drucklauf;
    }

    public boolean isNurGepruefte() {
        return nurGepruefte;
    }

    public void setNurGepruefte(boolean nurGepruefte) {
        this.nurGepruefte = nurGepruefte;
    }

    public int getGastOderAktionaer() {
        return gastOderAktionaer;
    }

    public void setGastOderAktionaer(int gastOderAktionaer) {
        this.gastOderAktionaer = gastOderAktionaer;
    }

    public int getWegSelektion() {
        return wegSelektion;
    }

    public void setWegSelektion(int wegSelektion) {
        this.wegSelektion = wegSelektion;
    }

    public int getLandSelektion() {
        return landSelektion;
    }

    public void setLandSelektion(int landSelektion) {
        this.landSelektion = landSelektion;
    }

    public String getVonEkNummer() {
        return vonEkNummer;
    }

    public void setVonEkNummer(String vonEkNummer) {
        this.vonEkNummer = vonEkNummer;
    }

    public String getBisEkNummer() {
        return bisEkNummer;
    }

    public void setBisEkNummer(String bisEkNummer) {
        this.bisEkNummer = bisEkNummer;
    }

    public int getEkVersandFuerAlleImPortalAngefordertenSelektion() {
        return ekVersandFuerAlleImPortalAngefordertenSelektion;
    }

    public void setEkVersandFuerAlleImPortalAngefordertenSelektion(int ekVersandFuerAlleImPortalAngefordertenSelektion) {
        this.ekVersandFuerAlleImPortalAngefordertenSelektion = ekVersandFuerAlleImPortalAngefordertenSelektion;
    }

 
}
