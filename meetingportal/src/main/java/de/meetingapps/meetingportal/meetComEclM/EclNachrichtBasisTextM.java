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

import de.meetingapps.meetingportal.meetComEntities.EclNachrichtBasisText;

public class EclNachrichtBasisTextM implements Serializable {
    private static final long serialVersionUID = 2073417318634681163L;

    private int ident = 0;
    private String beschreibung = "";
    private String betreff = "";
    private String mailText = "";

    public EclNachrichtBasisTextM() {
        return;
    }

    public EclNachrichtBasisTextM(EclNachrichtBasisText pNachrichtBasisText) {
        ident = pNachrichtBasisText.ident;
        beschreibung = pNachrichtBasisText.beschreibung;
        betreff = pNachrichtBasisText.betreff;
        mailText = pNachrichtBasisText.mailText;
    }

    /***********Standard setter und getter********************/
    public int getIdent() {
        return ident;
    }

    public void setIdent(int ident) {
        this.ident = ident;
    }

    public String getBeschreibung() {
        return beschreibung;
    }

    public void setBeschreibung(String beschreibung) {
        this.beschreibung = beschreibung;
    }

    public String getBetreff() {
        return betreff;
    }

    public void setBetreff(String betreff) {
        this.betreff = betreff;
    }

    public String getMailText() {
        return mailText;
    }

    public void setMailText(String mailText) {
        this.mailText = mailText;
    }

}
