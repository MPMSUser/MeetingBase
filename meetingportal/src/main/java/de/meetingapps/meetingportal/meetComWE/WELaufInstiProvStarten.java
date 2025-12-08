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

import java.util.List;

import de.meetingapps.meetingportal.meetComEntities.EclInstiProv;

public class WELaufInstiProvStarten extends WERoot {

    /**1=Übertragen und Einspielen Import-Liste listInstiProv
     * 2=Testlauf laufNr starten
     * 3=Verarbeitungslauf laufNr starten
     * 4=wie 3, aber nur Weisungsbuchung (für Inhaberaktien - Anmeldung ist vorher über InhaberImport erfolgt, Aktionärsnummer=EK-Nr)
     */
    
    public int funktion = 0;

    public int laufNr = 0;

    public List<EclInstiProv> listInstiProv = null;

    /*********************Standard Getter und Setter****************************************/

    public int getFunktion() {
        return funktion;
    }

    public void setFunktion(int funktion) {
        this.funktion = funktion;
    }

    public int getLaufNr() {
        return laufNr;
    }

    public void setLaufNr(int laufNr) {
        this.laufNr = laufNr;
    }

    public List<EclInstiProv> getListInstiProv() {
        return listInstiProv;
    }

    public void setListInstiProv(List<EclInstiProv> listInstiProv) {
        this.listInstiProv = listInstiProv;
    }

}
