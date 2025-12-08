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
package de.meetingapps.meetingportal.meetComEclM;

import java.io.Serializable;

public class EclZutrittsIdentAnzeigeM implements Serializable {
    private static final long serialVersionUID = 4822066162583814346L;

    private String nummerAnzeige = "";
    private String nummer = "";
    private String nummerNeben = "";

    private boolean storniert = false;

    /**********Standard Getter/Setter**************/

    public String getNummerAnzeige() {
        return nummerAnzeige;
    }

    public void setNummerAnzeige(String nummerAnzeige) {
        this.nummerAnzeige = nummerAnzeige;
    }

    public String getNummer() {
        return nummer;
    }

    public void setNummer(String nummer) {
        this.nummer = nummer;
    }

    public String getNummerNeben() {
        return nummerNeben;
    }

    public void setNummerNeben(String nummerNeben) {
        this.nummerNeben = nummerNeben;
    }

    public boolean isStorniert() {
        return storniert;
    }

    public void setStorniert(boolean storniert) {
        this.storniert = storniert;
    }

}
