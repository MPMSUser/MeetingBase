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

public enum EnAenderungslogAktion {

    undefiniert, neuaufnahme, aenderung, loeschung, neueZutrittsIdent, meldungStorniert, freigabeZutrittsIdent;

    /** Wandelt Aufzählungswert in Integer zum Speichern in Entity um*/
    public static int toEntity(EnAenderungslogAktion StatusP) {
        switch (StatusP) {
        case undefiniert:
            return 0;
        case neuaufnahme:
            return 1;
        case aenderung:
            return 2;
        case loeschung:
            return 3;
        case neueZutrittsIdent:
            return 4;
        case meldungStorniert:
            return 5;
        case freigabeZutrittsIdent:
            return 6;
        }

        return 0;

    }

    /** Wandelt Integer aus Entity in Aufzählungswert um*/
    public static EnAenderungslogAktion fromEntity(int feld) {
        switch (feld) {
        case 0:
            return (undefiniert);
        case 1:
            return (neuaufnahme);
        case 2:
            return (aenderung);
        case 3:
            return (loeschung);
        case 4:
            return (neueZutrittsIdent);
        case 5:
            return (meldungStorniert);
        case 6:
            return (freigabeZutrittsIdent);
        }
        return (undefiniert);
    }

    @Override
    public String toString() {

        switch (this) {
        case undefiniert:
            return ("Undefiniert");
        case neuaufnahme:
            return ("Neuaufnahme");
        case aenderung:
            return ("Änderung");
        case loeschung:
            return ("Löschung");
        case neueZutrittsIdent:
            return ("Neue ZutrittsIdentifikation");
        case meldungStorniert:
            return ("Anmeldung storniert");
        case freigabeZutrittsIdent:
            return ("Freigabe ZutrittsIdentifikation");

        }
        return ("Undefiniert");
    }

}
