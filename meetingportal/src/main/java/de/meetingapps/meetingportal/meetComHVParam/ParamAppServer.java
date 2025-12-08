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

/**div. Parameter zum Steuern der App - Serverseitig*/
public class ParamAppServer implements Serializable {
    private static final long serialVersionUID = -2457964750836076443L;

    /**Die folgenden 3 Parameter beziehen sich auf die Disclaimer der App - nicht zu verwechseln mit den
     * Mandanten-spezifischen Disclaimern für die jeweilige HV / den jeweiligen Mandanten.
     * Die folgenden 3 Parameter sind damit auch mandantenunabhängig!
     */
    public int disclaimerAppAktivNutzungsbedingungen = 1;
    public int disclaimerAppAktivDatenschutz = 1;
    public int disclaimerAppAktivVerwendungPersoenlicherDaten = 1;

    /**Die folgenden Parameter werden vom System selbstständig ermittelt*/
    public int hoechsteVersionTexteDEUebergreifend = 0;
    public int hoechsteVersionTexteENUebergreifend = 0;

    public int hoechsteVersionTexteDEMandant = 0;
    public int hoechsteVersionTexteENMandant = 0;

    /**Derzeit Knstanten */
    public int minZulaessigeAppVersion = 1;
    public int aktuelleAppVersion = 1;

}
