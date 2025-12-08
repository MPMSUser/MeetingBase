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
package de.meetingapps.meetingportal.meetComKonst;

/**Funktion, aus der heraus der Aufruf einer Präsenzfunktion abgesetzt wurde. In Abhängigkeit dieses Wertes erfolgen ggf. Fehlermeldungen,
 * falls die Bearbeitung dieser Karte in dieser Funktion (noch) nicht möglich ist. Siehe WEPraesenz*.
 * 1 = normale Akkreditierung (Schalter)
 * 2 = Vorab-Akkreditierung (in Vorbereitung!!)
 * 3 = Vertreternacherfassung
 * 4 = ServiceCounter / Supervisor
 */
public class KonstProgrammFunktionHV {

    public final static int unbekannt = 0;
    public final static int akkreditierungsSchalterStandard = 1;
    public final static int akkreditierungsSchalterVorabAkkreditierung = 2;
    public final static int vertreterNacherfassung = 3;
    public final static int serviceCounter = 4;
    public final static int appMeetingSmart = 5;

}
