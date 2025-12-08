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

public class KonstStimmkarteIstGesperrt {

    public final static int aktiv = 0;
    /**inaktiv => Nutzung für Zu-/Abgänge etc. als Identifikation (für Aktionär und/oder Meldung), nicht jedoch für Abstimmung*/
    public final static int inaktiv = 1;
    /**gesperrt => "verloren" - überhaupt nicht mehr nutzbar*/
    public final static int gesperrt = 2;

    static public String getText(int nr) {
        switch (nr) {
        case 0: {
            return "aktiv";
        }
        case 1: {
            return "inaktiv für Abstimmung";
        }
        case 2: {
            return "gesperrt";
        }
        }

        return "";

    }
}
