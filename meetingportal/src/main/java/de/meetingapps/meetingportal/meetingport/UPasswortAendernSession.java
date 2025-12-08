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
package de.meetingapps.meetingportal.meetingport;

import java.io.Serializable;

import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Named;

@SessionScoped
@Named
public class UPasswortAendernSession implements Serializable {
    private static final long serialVersionUID = -6599895420858637724L;

    private String altesPasswort = "";
    private String neuesPasswort = "";
    private String neuesPasswortBestaetigen = "";

    private String aufrufSeite = "";

    public void clear() {
        altesPasswort = "";
        neuesPasswort = "";
        neuesPasswortBestaetigen = "";
    }

    /***********Standard getter und setter*************/
    public String getAltesPasswort() {
        return altesPasswort;
    }

    public void setAltesPasswort(String altesPasswort) {
        this.altesPasswort = altesPasswort;
    }

    public String getNeuesPasswort() {
        return neuesPasswort;
    }

    public void setNeuesPasswort(String neuesPasswort) {
        this.neuesPasswort = neuesPasswort;
    }

    public String getNeuesPasswortBestaetigen() {
        return neuesPasswortBestaetigen;
    }

    public void setNeuesPasswortBestaetigen(String neuesPasswortBestaetigen) {
        this.neuesPasswortBestaetigen = neuesPasswortBestaetigen;
    }

    public String getAufrufSeite() {
        return aufrufSeite;
    }

    public void setAufrufSeite(String aufrufSeite) {
        this.aufrufSeite = aufrufSeite;
    }

}
