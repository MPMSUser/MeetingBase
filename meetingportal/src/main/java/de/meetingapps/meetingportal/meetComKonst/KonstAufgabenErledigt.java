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

public class KonstAufgabenErledigt {

    public static final int erledigt = 1;
    public static final int nichtMoeglichWgNullBestand = 2;
    public static final int nichtMoeglichFalscheAngaben = 3;
    public static final int nichtDurchgefuehrtDoppelteAnforderung = 4;

    public static String getText(int status) {
        switch (status) {
        case 1:
            return "Erledigt";
        case 2:
            return "Nicht Möglich - NullBestand";
        case 3:
            return "Nicht Möglich- Falsche Angaben";
        case 4:
            return "Nicht Durchgeführt- Doppelte Anforderung";
        }
        return "";
    }

}
