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
package de.meetingapps.meetingportal.meetComWE;

import de.meetingapps.meetingportal.meetComEntities.EclPersonenNatJur;
import de.meetingapps.meetingportal.meetComEntities.EclWillenserklaerung;
import de.meetingapps.meetingportal.meetComEntities.EclWillenserklaerungZusatz;

/**Wird mehrfach verwendet:
 * > Als Aufruf beim Holen der Daten zum Korrigieren
 * > Als return-Wert mit gefüllten Daten beim Aufruf zum Korrigieren
 * 
 * > Als Aufruf- und Returnwert beim Speichern der korrigierten Daten
 * 
 * @author N.N
 *
 */
public class WEEintrittskarteKorrigieren extends WERootRC {

    public WELoginVerify weLoginVerify = null;

    /**Erst-Willenserklärung (für Eintrittskartenausstellung)*/
    public EclWillenserklaerung willenserklaerung = null;
    public EclWillenserklaerungZusatz willenserklaerungZusatz = null;

    /**Zweit-Willenserklärung (für erteilte Vollmacht)*/
    public EclWillenserklaerung willenserklaerung2 = null;
    public EclWillenserklaerungZusatz willenserklaerungZusatz2 = null;

    /**Vertreterdaten - aus Willenserklaerung2*/
    public EclPersonenNatJur personNatJur = null;

    /***********************Standard Setter und Getter****************************/

    public WELoginVerify getWeLoginVerify() {
        return weLoginVerify;
    }

    public void setWeLoginVerify(WELoginVerify weLoginVerify) {
        this.weLoginVerify = weLoginVerify;
    }

    public EclWillenserklaerung getWillenserklaerung() {
        return willenserklaerung;
    }

    public void setWillenserklaerung(EclWillenserklaerung willenserklaerung) {
        this.willenserklaerung = willenserklaerung;
    }

    public EclWillenserklaerungZusatz getWillenserklaerungZusatz() {
        return willenserklaerungZusatz;
    }

    public void setWillenserklaerungZusatz(EclWillenserklaerungZusatz willenserklaerungZusatz) {
        this.willenserklaerungZusatz = willenserklaerungZusatz;
    }

    public EclWillenserklaerung getWillenserklaerung2() {
        return willenserklaerung2;
    }

    public void setWillenserklaerung2(EclWillenserklaerung willenserklaerung2) {
        this.willenserklaerung2 = willenserklaerung2;
    }

    public EclWillenserklaerungZusatz getWillenserklaerungZusatz2() {
        return willenserklaerungZusatz2;
    }

    public void setWillenserklaerungZusatz2(EclWillenserklaerungZusatz willenserklaerungZusatz2) {
        this.willenserklaerungZusatz2 = willenserklaerungZusatz2;
    }

    public EclPersonenNatJur getPersonNatJur() {
        return personNatJur;
    }

    public void setPersonNatJur(EclPersonenNatJur personNatJur) {
        this.personNatJur = personNatJur;
    }

}
