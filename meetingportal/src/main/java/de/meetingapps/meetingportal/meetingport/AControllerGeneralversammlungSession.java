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

import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Named;

@SessionScoped
@Named
@Deprecated
public class AControllerGeneralversammlungSession implements Serializable {
    private static final long serialVersionUID = -1072228811792773733L;

    private boolean anOderAbgemeldet = false;

    private boolean angemeldet = false;
    private boolean abgemeldet = false;

    /**Für Gruppen (siehe KonstAktienregisterErgaenzung) auf true:
     * 2 - Eheleute
     * 3 - Minderjährige Einzelmitglieder
     * 4 - Eheleute Gesamthandsgemeinschaft
     */
    private boolean zweiPersonenZulaessig = false;

    /**Für SelectBox:
     * 1=1 Person angemeldet
     * 2=abgemeldet
     * 3=2 Personen angemeldet
     */
    private String anmeldung = "";

    /*******************Standard getter und setter***************************/

    public boolean isAnOderAbgemeldet() {
        return anOderAbgemeldet;
    }

    public void setAnOderAbgemeldet(boolean anOderAbgemeldet) {
        this.anOderAbgemeldet = anOderAbgemeldet;
    }

    public boolean isAngemeldet() {
        return angemeldet;
    }

    public void setAngemeldet(boolean angemeldet) {
        this.angemeldet = angemeldet;
    }

    public boolean isZweiPersonenZulaessig() {
        return zweiPersonenZulaessig;
    }

    public void setZweiPersonenZulaessig(boolean zweiPersonenZulaessig) {
        this.zweiPersonenZulaessig = zweiPersonenZulaessig;
    }

    public String getAnmeldung() {
        return anmeldung;
    }

    public void setAnmeldung(String anmeldung) {
        this.anmeldung = anmeldung;
    }

    public boolean isAbgemeldet() {
        return abgemeldet;
    }

    public void setAbgemeldet(boolean abgemeldet) {
        this.abgemeldet = abgemeldet;
    }

}
