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

public class WECheckPersonengemeinschaftRC extends WERootRC {

    private int fehlerNr = 0;
    private String fehlerMeldung = "";

    private String vollmachtName = "";
    private String vollmachtVorname = "";
    private String vollmachtOrt = "";

    private String vollmachtName2 = "";
    private String vollmachtVorname2 = "";
    private String vollmachtOrt2 = "";

    /*********************Standard-Getter und Setter******************************************/

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

    public String getVollmachtName() {
        return vollmachtName;
    }

    public void setVollmachtName(String vollmachtName) {
        this.vollmachtName = vollmachtName;
    }

    public String getVollmachtVorname() {
        return vollmachtVorname;
    }

    public void setVollmachtVorname(String vollmachtVorname) {
        this.vollmachtVorname = vollmachtVorname;
    }

    public String getVollmachtOrt() {
        return vollmachtOrt;
    }

    public void setVollmachtOrt(String vollmachtOrt) {
        this.vollmachtOrt = vollmachtOrt;
    }

    public String getVollmachtName2() {
        return vollmachtName2;
    }

    public void setVollmachtName2(String vollmachtName2) {
        this.vollmachtName2 = vollmachtName2;
    }

    public String getVollmachtVorname2() {
        return vollmachtVorname2;
    }

    public void setVollmachtVorname2(String vollmachtVorname2) {
        this.vollmachtVorname2 = vollmachtVorname2;
    }

    public String getVollmachtOrt2() {
        return vollmachtOrt2;
    }

    public void setVollmachtOrt2(String vollmachtOrt2) {
        this.vollmachtOrt2 = vollmachtOrt2;
    }

}
