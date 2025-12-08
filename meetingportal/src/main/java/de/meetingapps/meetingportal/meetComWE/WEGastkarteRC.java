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

public class WEGastkarteRC extends WERootRC {

    private int fehlerNr = 0;
    private String fehlerMeldung = "";
    private int gastkartePdfNr = 0;
    private String gastkarteAbweichendeAdresse1 = "";
    private String gastkarteAbweichendeAdresse2 = "";
    private String gastkarteAbweichendeAdresse3 = "";
    private String gastkarteAbweichendeAdresse4 = "";
    private String gastkarteAbweichendeAdresse5 = "";
    private int gastkarteMeldeIdent = 0;
    private String gastkarteZutrittsIdent = "";
    private String gastkarteZutrittsIdentNeben = "";

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

    public int getGastkartePdfNr() {
        return gastkartePdfNr;
    }

    public void setGastkartePdfNr(int gastkartePdfNr) {
        this.gastkartePdfNr = gastkartePdfNr;
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

    public int getGastkarteMeldeIdent() {
        return gastkarteMeldeIdent;
    }

    public void setGastkarteMeldeIdent(int gastkarteMeldeIdent) {
        this.gastkarteMeldeIdent = gastkarteMeldeIdent;
    }

    public String getGastkarteZutrittsIdent() {
        return gastkarteZutrittsIdent;
    }

    public void setGastkarteZutrittsIdent(String gastkarteZutrittsIdent) {
        this.gastkarteZutrittsIdent = gastkarteZutrittsIdent;
    }

    public String getGastkarteZutrittsIdentNeben() {
        return gastkarteZutrittsIdentNeben;
    }

    public void setGastkarteZutrittsIdentNeben(String gastkarteZutrittsIdentNeben) {
        this.gastkarteZutrittsIdentNeben = gastkarteZutrittsIdentNeben;
    }

}
