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
package de.meetingapps.meetingportal.meetingportTController;

import java.io.Serializable;

import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Named;

@SessionScoped
@Named
public class PEinstellungenAendernSession implements Serializable {
    private static final long serialVersionUID = -3512812550099523387L;

    private boolean passwortNeuVergeben = false;

    private String neuesPasswort = "";
    private String neuesPasswortBestaetigung = "";
    
    
    /*************Standard getter/setter******************/

    public boolean isPasswortNeuVergeben() {
        return passwortNeuVergeben;
    }
    public void setPasswortNeuVergeben(boolean passwortNeuVergeben) {
        this.passwortNeuVergeben = passwortNeuVergeben;
    }
    public String getNeuesPasswort() {
        return neuesPasswort;
    }
    public void setNeuesPasswort(String neuesPasswort) {
        this.neuesPasswort = neuesPasswort;
    }
    public String getNeuesPasswortBestaetigung() {
        return neuesPasswortBestaetigung;
    }
    public void setNeuesPasswortBestaetigung(String neuesPasswortBestaetigung) {
        this.neuesPasswortBestaetigung = neuesPasswortBestaetigung;
    }

    
}
