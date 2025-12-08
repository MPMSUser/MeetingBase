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
package de.meetingapps.meetingportal.meetComHVParam;

import java.io.Serializable;

public class ParamPruefzahlen implements Serializable {
    private static final long serialVersionUID = -2674376153309522410L;

    public int laengeIdentifikationsNummer = 0;
    public String identifikationsNummer = ""; //81

    public String dreistelligeKontrollzahl = ""; //82
    public String zweistelligeKontrollzahl = ""; //83
    public String einstelligeKontrollzahl = ""; //84

    /**App-Version: 5 Ziffern Version der App selbst. 5 Ziffern Version der Parameter*/
    public String appVersion = "0000100001";

    /**Prüfziffern-Verfahren:
     * 0 = keine Prüfkennziffer errechnen
     * 
     * 1 bis 4: nur auf Eintrittskartennummer bezogen
     * 
     * 1 = Modulo 10, Wichtung 39713, Differenz auf 10
     * 2 = Modulo 10, Wichtung 31313, Differenz auf 10
     * 3 = Modulo 10, Wichtung 39713
     * 4 = Modulo 10, Wichtung 31313
     * 
     * 11 bis 14: analog 1 bis 4, aber auf komplette Nummer bezogen
     *
     * Wird aus EclNummernFormSet gefüllt. Nicht Mandantenabhängig!
     */
    public int berechnungsVerfahrenPruefziffer = 0;
    

}
