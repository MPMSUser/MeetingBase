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

/**HV-Daten zum Übergeben per Web-Service*/
public class WEHVDatenRC extends WERootRC {

    /**Mandant-Nummer**/
    private int mandant = 54;

    /**Laufende Nummer der HV**/
    private int hvNr = 0;

    /**Gesellschaftsname**/
    private String gesellschaft = "";

    /**HV-Datum**/
    private String hvDatum = "";

    /**HV-Ort**/
    private String hvOrt = "";

    public int getMandant() {
        return mandant;
    }

    public void setMandant(int mandant) {
        this.mandant = mandant;
    }

    public int getHvNr() {
        return hvNr;
    }

    public void setHvNr(int hvNr) {
        this.hvNr = hvNr;
    }

    public String getGesellschaft() {
        return gesellschaft;
    }

    public void setGesellschaft(String gesellschaft) {
        this.gesellschaft = gesellschaft;
    }

    public String getHvDatum() {
        return hvDatum;
    }

    public void setHvDatum(String hvDatum) {
        this.hvDatum = hvDatum;
    }

    public String getHvOrt() {
        return hvOrt;
    }

    public void setHvOrt(String hvOrt) {
        this.hvOrt = hvOrt;
    }

}
