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

/**Für Portal, Mitteilungen - Anzeige / Auswahl der InhaltsHinweise*/
public class EhInhaltsHinweise  implements Serializable {
    private static final long serialVersionUID = -4115792225368547582L;
    
    public int lfdNummer=0;
    public String textDE="";
    public String textEN="";
    public boolean selektiert=false;
    
    
    
    /********************************Standard setter und getter*************************/
    public int getLfdNummer() {
        return lfdNummer;
    }
    public void setLfdNummer(int lfdNummer) {
        this.lfdNummer = lfdNummer;
    }
    public boolean isSelektiert() {
        return selektiert;
    }
    public void setSelektiert(boolean selektiert) {
        this.selektiert = selektiert;
    }
    public String getTextDE() {
        return textDE;
    }
    public void setTextDE(String textDE) {
        this.textDE = textDE;
    }
    public String getTextEN() {
        return textEN;
    }
    public void setTextEN(String textEN) {
        this.textEN = textEN;
    }

}
