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
package de.meetingapps.meetingportal.meetComEntities;

import java.io.Serializable;

public class EclAktienregisterBestandshistorieeintrag implements Serializable {
    private static final long serialVersionUID = -3263088247881418959L;
    
    public String datum_der_wirksamkeit;
    public String datum_der_buchung;
    public String buchungstext;
    public int anteile;
    public String betrag;
    
    /***********************************Spezial Getter**********************************************/
    public String getBuchungstextBereinigt() {
        String s = this.buchungstext.replace("\\par", "");
        s = s.replace("-  ", "");
        return s;
    }
    
    /*******************************************standard getter und setter**************************/
    
    public String getDatum_der_wirksamkeit() {
        return datum_der_wirksamkeit;
    }
    public void setDatum_der_wirksamkeit(String datum_der_wirksamkeit) {
        this.datum_der_wirksamkeit = datum_der_wirksamkeit;
    }
    public String getDatum_der_buchung() {
        return datum_der_buchung;
    }
    public void setDatum_der_buchung(String datum_der_buchung) {
        this.datum_der_buchung = datum_der_buchung;
    }
    public String getBuchungstext() {
        return buchungstext;
    }
    public void setBuchungstext(String buchungstext) {
        this.buchungstext = buchungstext;
    }
    public int getAnteile() {
        return anteile;
    }
    public void setAnteile(int anteile) {
        this.anteile = anteile;
    }
    public String getBetrag() {
        return betrag;
    }
    public void setBetrag(String betrag) {
        this.betrag = betrag;
    }

    
}
