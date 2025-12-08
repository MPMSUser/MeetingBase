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
package de.meetingapps.meetingportal.meetComEh;

import java.io.Serializable;

/**Verwendung: uLogin, Pflege Phasen / Portal-Funktionen*/
public class EhPortalFunktionen  implements Serializable {
    private static final long serialVersionUID = 8158762798521453928L;
    
    public int portalFunktionNr=0;
    public String portalFunktionText="";
    public boolean angeboten=false;
    public String aktiv="";
    
    /************************Standard getter-Setter*******************************/
    
    public int getPortalFunktionNr() {
        return portalFunktionNr;
    }
    public void setPortalFunktionNr(int portalFunktionNr) {
        this.portalFunktionNr = portalFunktionNr;
    }
    public String getPortalFunktionText() {
        return portalFunktionText;
    }
    public void setPortalFunktionText(String portalFunktionText) {
        this.portalFunktionText = portalFunktionText;
    }
    public boolean isAngeboten() {
        return angeboten;
    }
    public void setAngeboten(boolean angeboten) {
        this.angeboten = angeboten;
    }
    public String getAktiv() {
        return aktiv;
    }
    public void setAktiv(String aktiv) {
        this.aktiv = aktiv;
    }
    
    
    
    

}
