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

public class WEOeffentlicheIDPruefenRC extends WERootRC {

    private int fehlerNr = 0;
    private String fehlerMeldung = "";

    private int personNatJurOeffentlicheID = 0;
    private String vollmachtName = "";
    private String vollmachtVorname = "";
    private String vollmachtOrt = "";

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

    public int getPersonNatJurOeffentlicheID() {
        return personNatJurOeffentlicheID;
    }

    public void setPersonNatJurOeffentlicheID(int personNatJurOeffentlicheID) {
        this.personNatJurOeffentlicheID = personNatJurOeffentlicheID;
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

}
