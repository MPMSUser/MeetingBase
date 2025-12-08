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
public class TEintrittskarteQuittungUDetailSession implements Serializable {
    private static final long serialVersionUID = -7574137519136470588L;

    /**Siehe KonstEintrittskarteDetailArt*/
    private int artEintrittskarte = 0;

    /*******Zusatzfelder für EintrittskarteDetails*******/
    /**=1 => Eintrittskarte wurde bereits gedruckt*/
    private boolean eintrittskarteWurdeGedruckt = true;
    /**Datum, zu dem Eintrittskarte gedruckt wurde*/
    private String eintrittskarteDruckDatum = "";

    /************************Gastkartenausstellung**********************/
    /*TODO konsolidieren*/
    private boolean gruppenausstellung = false;
    private int identMasterGast = 0;

    /****************************************Standard getter und setter**************************************/
    public int getArtEintrittskarte() {
        return artEintrittskarte;
    }

    public void setArtEintrittskarte(int artEintrittskarte) {
        this.artEintrittskarte = artEintrittskarte;
    }

    public boolean isEintrittskarteWurdeGedruckt() {
        return eintrittskarteWurdeGedruckt;
    }

    public void setEintrittskarteWurdeGedruckt(boolean eintrittskarteWurdeGedruckt) {
        this.eintrittskarteWurdeGedruckt = eintrittskarteWurdeGedruckt;
    }

    public String getEintrittskarteDruckDatum() {
        return eintrittskarteDruckDatum;
    }

    public void setEintrittskarteDruckDatum(String eintrittskarteDruckDatum) {
        this.eintrittskarteDruckDatum = eintrittskarteDruckDatum;
    }

    public boolean isGruppenausstellung() {
        return gruppenausstellung;
    }

    public void setGruppenausstellung(boolean gruppenausstellung) {
        this.gruppenausstellung = gruppenausstellung;
    }

    public int getIdentMasterGast() {
        return identMasterGast;
    }

    public void setIdentMasterGast(int identMasterGast) {
        this.identMasterGast = identMasterGast;
    }

}
