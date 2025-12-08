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

/**Zuordnung der Veranstaltungen zu den einzelnen Menüpunkten*/
public class EclVeranstaltungenMenue implements Serializable {
    private static final long serialVersionUID = 1L;

    /**identVeranstaltung, menueNummer = primaryKey*/

    public int identVeranstaltung = 0;

    /**Verweis auf Menü-Eintrag (>0).
     * 
     * <0 - Spezial-Verlinkung:
     * -1 => Anmeldung/Abmeldung Vereins-Portal
     */
    public int menueNummer = 0;

    public int positionInMenue = 0;

    /**Text, der in diesem Menüeintrag für diese Veranstaltung angezeigt wird.
    * LEN=2000
    */
    public String textInDiesemMenue = "";

    /**=1 => Der Anmelde-Button wird für diese Veranstaltung angezeigt in diesem Menü.
    * Achtung - "zieht vorrangig". D.h. auch wenn laut aktivierungsStatus in der Veranstaltung keine Anmeldung
    * möglich ist, und buttonAnzeigen auf 1 steht, wird der Button angezeigt.
    */
    public int buttonAnzeigen = 0;

    
    /*************************Standard getter und setter****************************************/
    
    public int getIdentVeranstaltung() {
        return identVeranstaltung;
    }

    public void setIdentVeranstaltung(int identVeranstaltung) {
        this.identVeranstaltung = identVeranstaltung;
    }

    public int getMenueNummer() {
        return menueNummer;
    }

    public void setMenueNummer(int menueNummer) {
        this.menueNummer = menueNummer;
    }

    public int getPositionInMenue() {
        return positionInMenue;
    }

    public void setPositionInMenue(int positionInMenue) {
        this.positionInMenue = positionInMenue;
    }

    public String getTextInDiesemMenue() {
        return textInDiesemMenue;
    }

    public void setTextInDiesemMenue(String textInDiesemMenue) {
        this.textInDiesemMenue = textInDiesemMenue;
    }

    public int getButtonAnzeigen() {
        return buttonAnzeigen;
    }

    public void setButtonAnzeigen(int buttonAnzeigen) {
        this.buttonAnzeigen = buttonAnzeigen;
    }

}
