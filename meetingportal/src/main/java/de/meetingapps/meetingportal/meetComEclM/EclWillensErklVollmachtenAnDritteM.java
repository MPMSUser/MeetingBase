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

import de.meetingapps.meetingportal.meetComEntities.EclWillensErklVollmachtenAnDritte;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Named;

@SessionScoped
@Named
public class EclWillensErklVollmachtenAnDritteM implements Serializable {
    private static final long serialVersionUID = 7438761678241678662L;

    /**Willenserklärung, mit der diese Vollmacht erteilt wurde*/
    private EclWillenserklaerungM willenserklaerungErteilt = null;

    /**Willenserklärung, mit der diese Vollmacht widerrufen wurde.
     * ==null, => nicht widerrufen sondern gültig
     */
    private EclWillenserklaerungM willenserklaerungStorniert = null;

    /**Person, die Bevollmächtigt wurde*/
    private EclPersonenNatJurM bevollmaechtigtePerson = null;

    /**Für Schnellzugriff: true => Vollmacht wurde storniert*/
    private boolean wurdeStorniert = false;

    /**"Merker" fürs Durcharbeiten von Vollmachtsketten*/
    private int merker = 0;

    public void copyFrom(EclWillensErklVollmachtenAnDritte pWillensErkl) {
        if (pWillensErkl.willenserklaerungErteilt == null) {
            this.willenserklaerungErteilt = null;
        } else {
            this.willenserklaerungErteilt = new EclWillenserklaerungM();
            this.willenserklaerungErteilt.copyFrom(pWillensErkl.willenserklaerungErteilt);
        }

        if (pWillensErkl.willenserklaerungStorniert == null) {
            this.willenserklaerungStorniert = null;
        } else {
            this.willenserklaerungStorniert = new EclWillenserklaerungM();
            this.willenserklaerungStorniert.copyFrom(pWillensErkl.willenserklaerungStorniert);
        }

        if (pWillensErkl.bevollmaechtigtePerson == null) {
            this.bevollmaechtigtePerson = null;
        } else {
            this.bevollmaechtigtePerson = new EclPersonenNatJurM();
            this.bevollmaechtigtePerson.copyFrom(pWillensErkl.bevollmaechtigtePerson);
        }
        this.wurdeStorniert = pWillensErkl.wurdeStorniert;
        this.merker = pWillensErkl.merker;

    }

    /*************************Standard Getter/Setter*********************************/

    public EclWillenserklaerungM getWillenserklaerungErteilt() {
        return willenserklaerungErteilt;
    }

    public void setWillenserklaerungErteilt(EclWillenserklaerungM willenserklaerungErteilt) {
        this.willenserklaerungErteilt = willenserklaerungErteilt;
    }

    public EclWillenserklaerungM getWillenserklaerungStorniert() {
        return willenserklaerungStorniert;
    }

    public void setWillenserklaerungStorniert(EclWillenserklaerungM willenserklaerungStorniert) {
        this.willenserklaerungStorniert = willenserklaerungStorniert;
    }

    public EclPersonenNatJurM getBevollmaechtigtePerson() {
        return bevollmaechtigtePerson;
    }

    public void setBevollmaechtigtePerson(EclPersonenNatJurM bevollmaechtigtePerson) {
        this.bevollmaechtigtePerson = bevollmaechtigtePerson;
    }

    public boolean isWurdeStorniert() {
        return wurdeStorniert;
    }

    public void setWurdeStorniert(boolean wurdeStorniert) {
        this.wurdeStorniert = wurdeStorniert;
    }

    public int getMerker() {
        return merker;
    }

    public void setMerker(int merker) {
        this.merker = merker;
    }

}