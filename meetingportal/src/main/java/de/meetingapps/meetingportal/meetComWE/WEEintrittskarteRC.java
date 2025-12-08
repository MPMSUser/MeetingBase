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

public class WEEintrittskarteRC extends WERootRC {

    private int fehlerNr = 0;
    private String fehlerMeldung = "";
    
    @Deprecated
    private int aktienregisterEintragIdent = 0;
    private int eintrittskartePdfNr = 0;
    private int eintrittskartePdfNr2 = 0;

    private String zutrittsIdent = "";
    private String zutrittsIdentNeben = "";
    private String zutrittsIdent2 = "";
    private String zutrittsIdentNeben2 = "";

    /**WillenserkärungsIdent für die durchgeführte Willenserklärung (also z.B. Weisung, EK etc.)
     * Wichtig: wenn 2 EKs ausgestellt werden, dann werden diese Variablen nicht korrekt / vollständig gefüllt - 
     * Verwendung dafür dann nicht sinnvoll!*/
    private int rcWillenserklaerungIdentAusgefuehrt = 0;

    /**WillenserkärungsIdent für die durchgeführte Zweit-Willenserklärung (also z.B. VollmachtDritte zu EK)*/
    private int rcWillenserklaerungIdentAusgefuehrtZweit = 0;

    /****************Standard Getter und Setter***********************************************************/

    public int getFehlerNr() {
        return fehlerNr;
    }

    public void setFehlerNr(int fehlerNr) {
        this.fehlerNr = fehlerNr;
    }

    public String getFehlerMeldung() {
        return fehlerMeldung;
    }

    public void setFehlerMeldung(String fehlerMeldung) {
        this.fehlerMeldung = fehlerMeldung;
    }

    public int getEintrittskartePdfNr() {
        return eintrittskartePdfNr;
    }

    public void setEintrittskartePdfNr(int eintrittskartePdfNr) {
        this.eintrittskartePdfNr = eintrittskartePdfNr;
    }

    public int getEintrittskartePdfNr2() {
        return eintrittskartePdfNr2;
    }

    public void setEintrittskartePdfNr2(int eintrittskartePdfNr2) {
        this.eintrittskartePdfNr2 = eintrittskartePdfNr2;
    }

    @Deprecated
    public int getAktienregisterEintragIdent() {
        return aktienregisterEintragIdent;
    }

    @Deprecated
    public void setAktienregisterEintragIdent(int aktienregisterEintragIdent) {
        this.aktienregisterEintragIdent = aktienregisterEintragIdent;
    }

    public int getRcWillenserklaerungIdentAusgefuehrt() {
        return rcWillenserklaerungIdentAusgefuehrt;
    }

    public void setRcWillenserklaerungIdentAusgefuehrt(int rcWillenserklaerungIdentAusgefuehrt) {
        this.rcWillenserklaerungIdentAusgefuehrt = rcWillenserklaerungIdentAusgefuehrt;
    }

    public int getRcWillenserklaerungIdentAusgefuehrtZweit() {
        return rcWillenserklaerungIdentAusgefuehrtZweit;
    }

    public void setRcWillenserklaerungIdentAusgefuehrtZweit(int rcWillenserklaerungIdentAusgefuehrtZweit) {
        this.rcWillenserklaerungIdentAusgefuehrtZweit = rcWillenserklaerungIdentAusgefuehrtZweit;
    }

    public String getZutrittsIdent() {
        return zutrittsIdent;
    }

    public void setZutrittsIdent(String zutrittsIdent) {
        this.zutrittsIdent = zutrittsIdent;
    }

    public String getZutrittsIdent2() {
        return zutrittsIdent2;
    }

    public void setZutrittsIdent2(String zutrittsIdent2) {
        this.zutrittsIdent2 = zutrittsIdent2;
    }

    public String getZutrittsIdentNeben() {
        return zutrittsIdentNeben;
    }

    public void setZutrittsIdentNeben(String zutrittsIdentNeben) {
        this.zutrittsIdentNeben = zutrittsIdentNeben;
    }

    public String getZutrittsIdentNeben2() {
        return zutrittsIdentNeben2;
    }

    public void setZutrittsIdentNeben2(String zutrittsIdentNeben2) {
        this.zutrittsIdentNeben2 = zutrittsIdentNeben2;
    }

}
