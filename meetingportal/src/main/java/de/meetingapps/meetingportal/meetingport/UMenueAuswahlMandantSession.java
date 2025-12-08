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
import java.util.List;

import de.meetingapps.meetingportal.meetComEclM.EclMandantAuswahlElementM;
import de.meetingapps.meetingportal.meetComEclM.EclUserLoginM;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@SessionScoped
@Named
public class UMenueAuswahlMandantSession implements Serializable {

    @Inject
    EclUserLoginM eclUserLoginM;

    private static final long serialVersionUID = 9099396139571462572L;

    /**Enthält EclMandantAuswahlElementM.lfdIdent*/
    private String ausgewaehlterMandant = "";

    private List<EclMandantAuswahlElementM> mandantenListeZurAuswahl = null;
    private boolean mandantenZurAuswahlVorhanden = false;

    /**Funktionen für Menü*/

    public boolean menue() {
        return menueNachrichten() | menueStammdaten() | menueSonstiges();
    }

    public boolean menueNachrichten() {
        return eclUserLoginM.pruefe_uportal_mailSenden();
    }

    public boolean menueStammdaten() {
        return eclUserLoginM.pruefe_uportal_dLoginHighAdmin();
    }

    public boolean menueSonstiges() {
        return true;
    }

    /*************Standard-Getter und Setter**************************/

    public String getAusgewaehlterMandant() {
        return ausgewaehlterMandant;
    }

    public void setAusgewaehlterMandant(String ausgewaehlterMandant) {
        this.ausgewaehlterMandant = ausgewaehlterMandant;
    }

    public List<EclMandantAuswahlElementM> getMandantenListeZurAuswahl() {
        return mandantenListeZurAuswahl;
    }

    public void setMandantenListeZurAuswahl(List<EclMandantAuswahlElementM> mandantenListeZurAuswahl) {
        this.mandantenListeZurAuswahl = mandantenListeZurAuswahl;
    }

    public boolean isMandantenZurAuswahlVorhanden() {
        return mandantenZurAuswahlVorhanden;
    }

    public void setMandantenZurAuswahlVorhanden(boolean mandantenZurAuswahlVorhanden) {
        this.mandantenZurAuswahlVorhanden = mandantenZurAuswahlVorhanden;
    }

}
