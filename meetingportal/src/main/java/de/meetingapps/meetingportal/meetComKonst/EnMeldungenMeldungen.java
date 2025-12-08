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

public enum EnMeldungenMeldungen {

    undefiniert, ohneInfo, gaestegruppe, begleitperson;

    /** Wandelt Aufzählungswert in Integer zum Speichern in Entity um*/
    public static int toEntity(EnMeldungenMeldungen StatusP) {
        switch (StatusP) {
        case undefiniert:
            return 0;
        case ohneInfo:
            return 1;
        case gaestegruppe:
            return 2;
        case begleitperson:
            return 3;
        }

        return 0;

    }

    /** Wandelt Integer aus Entity in Aufzählungswert um*/
    public static EnMeldungenMeldungen fromEntity(int feld) {
        switch (feld) {
        case 0:
            return (undefiniert);
        case 1:
            return (ohneInfo);
        case 2:
            return (gaestegruppe);
        case 3:
            return (begleitperson);
        }
        return (undefiniert);
    }

    @Override
    public String toString() {

        switch (this) {
        case undefiniert:
            return ("Undefiniert");
        case ohneInfo:
            return ("Ohne Information");
        case gaestegruppe:
            return ("Gästegruppe");
        case begleitperson:
            return ("Begleitperson");

        }
        return ("Undefiniert");
    }

    public static EnMeldungenMeldungen fromString(String grund) {

        if (grund.equals("Undefiniert")) {
            return (undefiniert);
        }
        if (grund.equals("Ohne Information")) {
            return (ohneInfo);
        }
        if (grund.equals("Gästegruppe")) {
            return (gaestegruppe);
        }
        if (grund.equals("Begleitperson")) {
            return (begleitperson);
        }

        return (undefiniert);
    }

}
