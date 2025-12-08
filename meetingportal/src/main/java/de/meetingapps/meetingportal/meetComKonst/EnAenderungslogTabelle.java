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

public enum EnAenderungslogTabelle {

    undefiniert, meldungen;

    /** Wandelt Aufzählungswert in Integer zum Speichern in Entity um*/
    public static int toEntity(EnAenderungslogTabelle StatusP) {
        switch (StatusP) {
        case undefiniert:
            return 0;
        case meldungen:
            return 1;
        }

        return 0;

    }

    /** Wandelt Integer aus Entity in Aufzählungswert um*/
    public static EnAenderungslogTabelle fromEntity(int feld) {
        switch (feld) {
        case 0:
            return (undefiniert);
        case 1:
            return (meldungen);
        }
        return (undefiniert);
    }

}
