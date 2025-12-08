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

/**Aufrufen des Suchvorgangs*/

public class WESuchen {

    public WELoginVerify weLoginVerify = null;

    public String suchBegriff = "";
    public String[] suchBegriffArray = null;

    /**1=Name Aktionär, 2=Name Vertreter, 3=Eintrittskartennummer, 4=Stimmkartennummer, 5=Meldebestand, 6=Aktionärsnummer; 7=Meldung.Zusatzfeld3 (für Vereine);
     * 8=Folge von Eintrittskartennummern in suchBegriffArray*/
    public int suchart = 0;

    /************************Standard Getter und Setter**********************************************/

    public WELoginVerify getWeLoginVerify() {
        return weLoginVerify;
    }

    public void setWeLoginVerify(WELoginVerify weLoginVerify) {
        this.weLoginVerify = weLoginVerify;
    }

    public String getSuchBegriff() {
        return suchBegriff;
    }

    public void setSuchBegriff(String suchBegriff) {
        this.suchBegriff = suchBegriff;
    }

    public int getSuchart() {
        return suchart;
    }

    public void setSuchart(int suchart) {
        this.suchart = suchart;
    }

    public String[] getSuchBegriffArray() {
        return suchBegriffArray;
    }

    public void setSuchBegriffArray(String[] suchBegriffArray) {
        this.suchBegriffArray = suchBegriffArray;
    }

}
