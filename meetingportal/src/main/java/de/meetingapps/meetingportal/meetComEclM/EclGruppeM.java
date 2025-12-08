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

import de.meetingapps.meetingportal.meetComEntities.EclGruppe;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Named;

@SessionScoped
@Named
public class EclGruppeM implements Serializable {
    private static final long serialVersionUID = -5085633043740460761L;

    private String gruppenNr = "";
    private String gruppenklassenNr = "";
    private String gruppenText = "";
    private int vertreterZwingend = 0;
    private int fuerAktionaere = 0;
    private int fuerGaeste = 0;

    /*********Ab hier nicht in Datenbank!*******************/
    /**Formular-Nummer Gast-EK*/
    private int gastEKFormular = 0;

    public void copyFrom(EclGruppe pGruppe) {

        gruppenNr = Integer.toString(pGruppe.gruppenNr);
        gruppenklassenNr = Integer.toString(pGruppe.gruppenklassenNr);
        gruppenText = pGruppe.gruppenText;
        vertreterZwingend = pGruppe.vertreterZwingend;
        fuerAktionaere = pGruppe.fuerAktionaere;
        fuerGaeste = pGruppe.fuerGaeste;
        gastEKFormular = pGruppe.gastEKFormular;
    }

    /******Ab hier Standard getter/setter********************/
    public String getGruppenNr() {
        return gruppenNr;
    }

    public void setGruppenNr(String gruppenNr) {
        this.gruppenNr = gruppenNr;
    }

    public String getGruppenklassenNr() {
        return gruppenklassenNr;
    }

    public void setGruppenklassenNr(String gruppenklassenNr) {
        this.gruppenklassenNr = gruppenklassenNr;
    }

    public String getGruppenText() {
        return gruppenText;
    }

    public void setGruppenText(String gruppenText) {
        this.gruppenText = gruppenText;
    }

    public int getVertreterZwingend() {
        return vertreterZwingend;
    }

    public void setVertreterZwingend(int vertreterZwingend) {
        this.vertreterZwingend = vertreterZwingend;
    }

    public int getFuerAktionaere() {
        return fuerAktionaere;
    }

    public void setFuerAktionaere(int fuerAktionaere) {
        this.fuerAktionaere = fuerAktionaere;
    }

    public int getFuerGaeste() {
        return fuerGaeste;
    }

    public void setFuerGaeste(int fuerGaeste) {
        this.fuerGaeste = fuerGaeste;
    }

    public int getGastEKFormular() {
        return gastEKFormular;
    }

    public void setGastEKFormular(int gastEKFormular) {
        this.gastEKFormular = gastEKFormular;
    }

}
