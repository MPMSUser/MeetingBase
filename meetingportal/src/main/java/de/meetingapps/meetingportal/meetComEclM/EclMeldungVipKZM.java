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

import de.meetingapps.meetingportal.meetComEntities.EclMeldungVipKZ;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Named;

@SessionScoped
@Named
public class EclMeldungVipKZM implements Serializable {
    private static final long serialVersionUID = 13434130248480493L;

    private int mandant = 0;
    private int meldungsIdent = 0;
    private String vipKZKuerzel = "";
    private String parameter = "";

    /*Nicht Bestandteil dieser Table, aber im Hinblick auf meldung enthalten*/
    private String beschreibung; /*Aus EclAusstellungsgrund*/

    public void copyFrom(EclMeldungVipKZ pMeldungVipKZ) {
        mandant = pMeldungVipKZ.mandant;
        meldungsIdent = pMeldungVipKZ.meldungsIdent;
        vipKZKuerzel = pMeldungVipKZ.vipKZKuerzel;
        parameter = pMeldungVipKZ.parameter;
        beschreibung = pMeldungVipKZ.beschreibung;
    }

    public int getMandant() {
        return mandant;
    }

    public void setMandant(int mandant) {
        this.mandant = mandant;
    }

    public int getMeldungsIdent() {
        return meldungsIdent;
    }

    public void setMeldungsIdent(int meldungsIdent) {
        this.meldungsIdent = meldungsIdent;
    }

    public String getVipKZKuerzel() {
        return vipKZKuerzel;
    }

    public void setVipKZKuerzel(String vipKZKuerzel) {
        this.vipKZKuerzel = vipKZKuerzel;
    }

    public String getParameter() {
        return parameter;
    }

    public void setParameter(String parameter) {
        this.parameter = parameter;
    }

    public String getBeschreibung() {
        return beschreibung;
    }

    public void setBeschreibung(String beschreibung) {
        this.beschreibung = beschreibung;
    }

}
