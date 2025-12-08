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

import de.meetingapps.meetingportal.meetComEclM.EclAbstimmungMitVorschlagM;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Named;

@SessionScoped
@Named
public class UTOWeisungsEmpfehlungSession implements Serializable {
    private static final long serialVersionUID = 9099396139571462572L;

    private List<EclAbstimmungMitVorschlagM> toMitVorschlagListe = null;

    /**false => bei den TOPs wird keine Selektionsmöglichkeit angeboten*/
    private boolean auswahlMoeglich = false;

    private boolean ausgewaehltNeuerTOP = false;
    private boolean ausgewaehltNeuerTOPNachricht = false;
    private boolean ausgewaehltEmpfehlungAendern = false;
    private boolean ausgewaehltAendernTOP = false;

    /**Setzt alle Auswahl-Werte auf false*/
    public void auswahlInit() {
        auswahlMoeglich = false;

        ausgewaehltNeuerTOP = false;
        ausgewaehltNeuerTOPNachricht = false;
        ausgewaehltEmpfehlungAendern = false;
        ausgewaehltAendernTOP = false;
    }

    /*************Standard-Getter und Setter**************************/

    public List<EclAbstimmungMitVorschlagM> getToMitVorschlagListe() {
        return toMitVorschlagListe;
    }

    public void setToMitVorschlagListe(List<EclAbstimmungMitVorschlagM> toMitVorschlagListe) {
        this.toMitVorschlagListe = toMitVorschlagListe;
    }

    public boolean isAuswahlMoeglich() {
        return auswahlMoeglich;
    }

    public void setAuswahlMoeglich(boolean auswahlMoeglich) {
        this.auswahlMoeglich = auswahlMoeglich;
    }

    public boolean isAusgewaehltNeuerTOP() {
        return ausgewaehltNeuerTOP;
    }

    public void setAusgewaehltNeuerTOP(boolean ausgewaehltNeuerTOP) {
        this.ausgewaehltNeuerTOP = ausgewaehltNeuerTOP;
    }

    public boolean isAusgewaehltEmpfehlungAendern() {
        return ausgewaehltEmpfehlungAendern;
    }

    public void setAusgewaehltEmpfehlungAendern(boolean ausgewaehltEmpfehlungAendern) {
        this.ausgewaehltEmpfehlungAendern = ausgewaehltEmpfehlungAendern;
    }

    public boolean isAusgewaehltAendernTOP() {
        return ausgewaehltAendernTOP;
    }

    public void setAusgewaehltAendernTOP(boolean ausgewaehltAendernTOP) {
        this.ausgewaehltAendernTOP = ausgewaehltAendernTOP;
    }

    public boolean isAusgewaehltNeuerTOPNachricht() {
        return ausgewaehltNeuerTOPNachricht;
    }

    public void setAusgewaehltNeuerTOPNachricht(boolean ausgewaehltNeuerTOPNachricht) {
        this.ausgewaehltNeuerTOPNachricht = ausgewaehltNeuerTOPNachricht;
    }

}
