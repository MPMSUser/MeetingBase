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

import de.meetingapps.meetingportal.meetComAllg.CaString;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Named;

@SessionScoped
@Named
public class UPflegeNachrichtVerwendungsCodeSession implements Serializable {
    private static final long serialVersionUID = 9099396139571462572L;

    private String ident;
    private String beschreibung;
    private String identNachrichtBasisText;
    private boolean reserviertFuerSystem = false;

    /**0=Eingabe ident;
     * 1=Ändern aktiv
     * 2=Einfügen aktiv
     */
    private int modus = 0;

    public void clear() {
        ident = "";
        beschreibung = "";
        identNachrichtBasisText = "";
        reserviertFuerSystem = false;
        modus = 0;
    }

    public boolean isReserviertFuerSystem() {
        int hIdent = 0;
        if (CaString.isNummern(ident)) {
            hIdent = Integer.parseInt(ident);
        }
        return reserviertFuerSystem | (hIdent > 100000);
    }

    /*************Standard-Getter und Setter**************************/
    public String getIdent() {
        return ident;
    }

    public void setIdent(String ident) {
        this.ident = ident;
    }

    public String getBeschreibung() {
        return beschreibung;
    }

    public void setBeschreibung(String beschreibung) {
        this.beschreibung = beschreibung;
    }

    public String getIdentNachrichtBasisText() {
        return identNachrichtBasisText;
    }

    public void setIdentNachrichtBasisText(String identNachrichtBasisText) {
        this.identNachrichtBasisText = identNachrichtBasisText;
    }

    public int getModus() {
        return modus;
    }

    public void setModus(int modus) {
        this.modus = modus;
    }

    public void setReserviertFuerSystem(boolean reserviertFuerSystem) {
        this.reserviertFuerSystem = reserviertFuerSystem;
    }

}
