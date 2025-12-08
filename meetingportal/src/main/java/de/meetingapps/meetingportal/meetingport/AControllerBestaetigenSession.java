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
public class AControllerBestaetigenSession implements Serializable {
    private static final long serialVersionUID = -1072228811792773733L;

    /*Seitenspezifische Fehlermeldung. Seitenspezifisch deshalb, um zu verhindern, dass beim "Neueinsprung" in die Login-Maske 
     * (z.B. über direkte Eingabe des Browserlinks) "alte Fehlermeldungen" übernommen werden.
     * War ursprünglich mal direkt in AControllerBestaetigen*/
    private String fehlerMeldung = "";
    private int fehlerNr = 0;

    private String bestaetigungsCode;
    private String bestaetigungsCode2;

    public void clearFehlermeldung() {
        fehlerMeldung = "";
        fehlerNr = 0;
    }

    private String fehlerMeldung2 = "";
    private int fehlerNr2 = 0;

    public void clearFehlermeldung2() {
        fehlerMeldung2 = "";
        fehlerNr2 = 0;
    }

    public String getFehlerMeldung() {
        return fehlerMeldung;
    }

    public void setFehlerMeldung(String fehlerMeldung) {
        this.fehlerMeldung = fehlerMeldung;
    }

    public int getFehlerNr() {
        return fehlerNr;
    }

    public void setFehlerNr(int fehlerNr) {
        this.fehlerNr = fehlerNr;
    }

    public String getFehlerMeldung2() {
        return fehlerMeldung2;
    }

    public void setFehlerMeldung2(String fehlerMeldung2) {
        this.fehlerMeldung2 = fehlerMeldung2;
    }

    public int getFehlerNr2() {
        return fehlerNr2;
    }

    public void setFehlerNr2(int fehlerNr2) {
        this.fehlerNr2 = fehlerNr2;
    }

    public String getBestaetigungsCode() {
        return bestaetigungsCode;
    }

    public void setBestaetigungsCode(String bestaetigungsCode) {
        this.bestaetigungsCode = bestaetigungsCode;
    }

    public String getBestaetigungsCode2() {
        return bestaetigungsCode2;
    }

    public void setBestaetigungsCode2(String bestaetigungsCode2) {
        this.bestaetigungsCode2 = bestaetigungsCode2;
    }

}
