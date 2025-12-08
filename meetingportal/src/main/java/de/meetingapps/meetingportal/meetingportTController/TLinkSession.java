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

/**Wird verwendet, um bei der Erzeugung eines Passwort-Vergessen- oder Email-Bestätigungs-Links den Link zwischenzuspeichern, der dann in 
 * den Text der E-Mail eingefügt wird.
 */
@SessionScoped
@Named
public class TLinkSession implements Serializable {
    private static final long serialVersionUID = 7977707744680595763L;

    /**Link, der in die Email-Bestätigung oder in das Email-Passwort-Vergessen mit einkopiert wird - kompletter Link, falls über Portal verschickt*/
    private String einsprungsLinkFuerEmail = "";

    /**Code, der der in die Email-Bestätigung oder in das Email-Passwort-Vergessen mit einkopiert wird - nur Code, falls über App angefordert*/
    private String einsprungsLinkNurCode = "";

    public String getEinsprungsLinkFuerEmail() {
        return einsprungsLinkFuerEmail;
    }

    public void setEinsprungsLinkFuerEmail(String einsprungsLinkFuerEmail) {
        this.einsprungsLinkFuerEmail = einsprungsLinkFuerEmail;
    }

    public String getEinsprungsLinkNurCode() {
        return einsprungsLinkNurCode;
    }

    public void setEinsprungsLinkNurCode(String einsprungsLinkNurCode) {
        this.einsprungsLinkNurCode = einsprungsLinkNurCode;
    }

}
