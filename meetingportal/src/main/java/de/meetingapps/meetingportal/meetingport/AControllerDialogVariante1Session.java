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

import de.meetingapps.meetingportal.meetComEclM.EclZugeordneteMeldungListeM;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@SessionScoped
@Named
@Deprecated
public class AControllerDialogVariante1Session implements Serializable {
    private static final long serialVersionUID = -1072228811792773733L;

    @Inject
    EclZugeordneteMeldungListeM eclZugeordneteMeldungListeM;

    private int briefwahlBereitsErteilt = 0;
    private int vollmachtBereitsErteilt = 0;
    //	private int bereitsAngemeldet=0;
    //	private int bereitsAbgemeldet=0;
    private int nochNichtAngemeldet = 1;
    private int vollmachtenErhalten = 0;

    /**Für Checkboxes in aAnmeldenAbmelden
     * 1=Abmelden
     * 2=Anmelden
     * Wird automatisch gesetzt über bereitsAngemeldet / bereitsAbgemeldet*/
    private String anabmelden = "0";

    /**************Sonder Getter/Setter*************************/

    public void setBriefwahlBereitsErteilt(int briefwahlBereitsErteilt) {
        this.briefwahlBereitsErteilt = briefwahlBereitsErteilt;
    }

    public int getBriefwahlBereitsErteilt() {
        if (eclZugeordneteMeldungListeM.isBriefwahlVorhanden()) {
            briefwahlBereitsErteilt = 1;
        } else {
            briefwahlBereitsErteilt = 0;
        }
        return briefwahlBereitsErteilt;
    }

    public int getBereitsAngemeldet() {
        String lZusatzfeld3 = eclZugeordneteMeldungListeM.getZugeordneteMeldungenEigeneAktienListeM().get(0)
                .getZusatzfeld3();
        if (lZusatzfeld3.length() < 1) {
            return 0;
        }
        if (lZusatzfeld3.substring(0, 1).equals("1")) {
            return 1;
        } else {
            return 0;
        }
    }

    public void setBereitsAngemeldet(int bereitsAngemeldet) {
        //		this.bereitsAngemeldet = bereitsAngemeldet;
    }

    public int getBereitsAbgemeldet() {
        String lZusatzfeld3 = eclZugeordneteMeldungListeM.getZugeordneteMeldungenEigeneAktienListeM().get(0)
                .getZusatzfeld3();
        if (lZusatzfeld3.length() < 1) {
            return 0;
        }
        if (lZusatzfeld3.substring(0, 1).equals("2")) {
            return 1;
        } else {
            return 0;
        }
    }

    public void setBereitsAbgemeldet(int bereitsAbgemeldet) {
        //		this.bereitsAbgemeldet = bereitsAbgemeldet;
    }

    public String getAnabmelden() {
        if (getBereitsAngemeldet() == 1) {
            return "1";
        }
        if (getBereitsAbgemeldet() == 1) {
            return "2";
        }
        return "0";
    }

    public String getAnabmeldenRaw() {
        return anabmelden;
    }

    public void setAnabmelden(String anabmelden) {
        this.anabmelden = anabmelden;
    }

    /**************Standard Getters/Setters********************************/

    public int getVollmachtBereitsErteilt() {
        return vollmachtBereitsErteilt;
    }

    public void setVollmachtBereitsErteilt(int vollmachtBereitsErteilt) {
        this.vollmachtBereitsErteilt = vollmachtBereitsErteilt;
    }

    public int getVollmachtenErhalten() {
        return vollmachtenErhalten;
    }

    public void setVollmachtenErhalten(int vollmachtenErhalten) {
        this.vollmachtenErhalten = vollmachtenErhalten;
    }

    public int getNochNichtAngemeldet() {
        return nochNichtAngemeldet;
    }

    public void setNochNichtAngemeldet(int nochNichtAngemeldet) {
        this.nochNichtAngemeldet = nochNichtAngemeldet;
    }

}
