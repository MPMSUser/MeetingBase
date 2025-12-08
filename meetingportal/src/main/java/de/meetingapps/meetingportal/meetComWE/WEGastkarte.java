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

public class WEGastkarte {

    private WELoginVerify weLoginVerify = null;

    /*Beschreibungen siehe ADlgVariablen*/
    private String ausgewaehlteHauptAktion = "";
    private String ausgewaehlteAktion = "";

    private String gastkarteName = "";
    private String gastkarteVorname = "";
    private String gastkarteOrt = "";

    private String gastkarteVersandart = "";
    private int gastkarteNrPersNatJur = 0;

    private String gastkarteEmail = "";
    private String gastkarteAbweichendeAdresse1 = "";
    private String gastkarteAbweichendeAdresse2 = "";
    private String gastkarteAbweichendeAdresse3 = "";
    private String gastkarteAbweichendeAdresse4 = "";
    private String gastkarteAbweichendeAdresse5 = "";

    /**********************Ab hier Standard-Setter/Getter**************************************************/

    public WELoginVerify getWeLoginVerify() {
        return weLoginVerify;
    }

    public void setWeLoginVerify(WELoginVerify weLoginVerify) {
        this.weLoginVerify = weLoginVerify;
    }

    public String getAusgewaehlteHauptAktion() {
        return ausgewaehlteHauptAktion;
    }

    public void setAusgewaehlteHauptAktion(String ausgewaehlteHauptAktion) {
        this.ausgewaehlteHauptAktion = ausgewaehlteHauptAktion;
    }

    public String getAusgewaehlteAktion() {
        return ausgewaehlteAktion;
    }

    public void setAusgewaehlteAktion(String ausgewaehlteAktion) {
        this.ausgewaehlteAktion = ausgewaehlteAktion;
    }

    public String getGastkarteName() {
        return gastkarteName;
    }

    public void setGastkarteName(String gastkarteName) {
        this.gastkarteName = gastkarteName;
    }

    public String getGastkarteVorname() {
        return gastkarteVorname;
    }

    public void setGastkarteVorname(String gastkarteVorname) {
        this.gastkarteVorname = gastkarteVorname;
    }

    public String getGastkarteOrt() {
        return gastkarteOrt;
    }

    public void setGastkarteOrt(String gastkarteOrt) {
        this.gastkarteOrt = gastkarteOrt;
    }

    public String getGastkarteVersandart() {
        return gastkarteVersandart;
    }

    public void setGastkarteVersandart(String gastkarteVersandart) {
        this.gastkarteVersandart = gastkarteVersandart;
    }

    public String getGastkarteEmail() {
        return gastkarteEmail;
    }

    public void setGastkarteEmail(String gastkarteEmail) {
        this.gastkarteEmail = gastkarteEmail;
    }

    public String getGastkarteAbweichendeAdresse1() {
        return gastkarteAbweichendeAdresse1;
    }

    public void setGastkarteAbweichendeAdresse1(String gastkarteAbweichendeAdresse1) {
        this.gastkarteAbweichendeAdresse1 = gastkarteAbweichendeAdresse1;
    }

    public String getGastkarteAbweichendeAdresse2() {
        return gastkarteAbweichendeAdresse2;
    }

    public void setGastkarteAbweichendeAdresse2(String gastkarteAbweichendeAdresse2) {
        this.gastkarteAbweichendeAdresse2 = gastkarteAbweichendeAdresse2;
    }

    public String getGastkarteAbweichendeAdresse3() {
        return gastkarteAbweichendeAdresse3;
    }

    public void setGastkarteAbweichendeAdresse3(String gastkarteAbweichendeAdresse3) {
        this.gastkarteAbweichendeAdresse3 = gastkarteAbweichendeAdresse3;
    }

    public String getGastkarteAbweichendeAdresse4() {
        return gastkarteAbweichendeAdresse4;
    }

    public void setGastkarteAbweichendeAdresse4(String gastkarteAbweichendeAdresse4) {
        this.gastkarteAbweichendeAdresse4 = gastkarteAbweichendeAdresse4;
    }

    public String getGastkarteAbweichendeAdresse5() {
        return gastkarteAbweichendeAdresse5;
    }

    public void setGastkarteAbweichendeAdresse5(String gastkarteAbweichendeAdresse5) {
        this.gastkarteAbweichendeAdresse5 = gastkarteAbweichendeAdresse5;
    }

    public int getGastkarteNrPersNatJur() {
        return gastkarteNrPersNatJur;
    }

    public void setGastkarteNrPersNatJur(int gastkarteNrPersNatJur) {
        this.gastkarteNrPersNatJur = gastkarteNrPersNatJur;
    }

}