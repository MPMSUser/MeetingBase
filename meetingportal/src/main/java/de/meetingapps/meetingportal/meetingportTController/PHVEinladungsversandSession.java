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
public class PHVEinladungsversandSession implements Serializable {
    private static final long serialVersionUID = 7042331631855447813L;

    /**setzen, wenn Registrierung für den elektronischen Einladungsversand zulässig ist*/
    private boolean registrierungZulaessig=true;
    
    private String mailAdresse="";
    
    /**1=Post, 2=Mail*/
    private String auswahl="";
    private String auswahlAlt="";

    
    /**aenderungsverfolgung*/
    private String vorAendungAuswahl="";
    
    public boolean isRegistrierungZulaessig() {
        return registrierungZulaessig;
    }

    public void setRegistrierungZulaessig(boolean registrierungZulaessig) {
        this.registrierungZulaessig = registrierungZulaessig;
    }

    public String getMailAdresse() {
        return mailAdresse;
    }

    public void setMailAdresse(String mailAdresse) {
        this.mailAdresse = mailAdresse;
    }

    public String getAuswahl() {
        return auswahl;
    }

    public void setAuswahl(String auswahl) {
        this.auswahl = auswahl;
    }

    public String getAuswahlAlt() {
        return auswahlAlt;
    }

    public void setAuswahlAlt(String auswahlAlt) {
        this.auswahlAlt = auswahlAlt;
    }

    public String getVorAendungAuswahl() {
        return vorAendungAuswahl;
    }

    public void setVorAendungAuswahl(String vorAendungAuswahl) {
        this.vorAendungAuswahl = vorAendungAuswahl;
    }
}
