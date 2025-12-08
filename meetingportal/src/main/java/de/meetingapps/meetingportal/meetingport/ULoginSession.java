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

import de.meetingapps.meetingportal.meetComHVParam.SParamProgramm;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Named;

@SessionScoped
@Named
public class ULoginSession implements Serializable {
    private static final long serialVersionUID = 9099396139571462572L;

    private String kennung="";
    private String passwort="";

    private String baseLink = "";
    private String mandant = "";
    private String hvjahr = "";
    private String hvnummer = "";
    private String datenbankbereich = "";

    private String vergessenKennung="";
    private String vergessenMail="";
    
    public void clear() {
        kennung = "";
        passwort = "";
        
        vergessenKennung="";
        vergessenMail="";
    }

    public String version() {
        return SParamProgramm.programmVersion;
    }
    /*************Standard-Getter und Setter**************************/

    public String getKennung() {
        return kennung;
    }

    public void setKennung(String kennung) {
        this.kennung = kennung;
    }

    public String getPasswort() {
        return passwort;
    }

    public void setPasswort(String passwort) {
        this.passwort = passwort;
    }

    public String getBaseLink() {
        return baseLink;
    }

    public void setBaseLink(String baseLink) {
        this.baseLink = baseLink;
    }

    public String getMandant() {
        return mandant;
    }

    public void setMandant(String mandant) {
        this.mandant = mandant;
    }

    public String getHvjahr() {
        return hvjahr;
    }

    public void setHvjahr(String hvjahr) {
        this.hvjahr = hvjahr;
    }

    public String getHvnummer() {
        return hvnummer;
    }

    public void setHvnummer(String hvnummer) {
        this.hvnummer = hvnummer;
    }

    public String getDatenbankbereich() {
        return datenbankbereich;
    }

    public void setDatenbankbereich(String datenbankbereich) {
        this.datenbankbereich = datenbankbereich;
    }

    public String getVergessenKennung() {
        return vergessenKennung;
    }

    public void setVergessenKennung(String vergessenKennung) {
        this.vergessenKennung = vergessenKennung;
    }

    public String getVergessenMail() {
        return vergessenMail;
    }

    public void setVergessenMail(String vergessenMail) {
        this.vergessenMail = vergessenMail;
    }

}
