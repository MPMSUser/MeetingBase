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

import de.meetingapps.meetingportal.meetComEntities.EclMeldung;

public enum EnMeldungAktiv {

    undefiniert, MeldungIstAktiv, MeldungIstStorniert, ZutrittsIdentIstStorniert;

    public static int toClMeldung(EnMeldungAktiv StatusP) {
        switch (StatusP) {
        case undefiniert:
            return 0;
        case MeldungIstAktiv:
            return 1;
        case MeldungIstStorniert:
            return 2;
        case ZutrittsIdentIstStorniert:
            return 3;
        }

        return 0;

    }

    public static EnMeldungAktiv fromClMeldung(EclMeldung meldung) {
        switch (meldung.statusPraesenz) {
        case 0:
            return (undefiniert);
        case 1:
            return (MeldungIstAktiv);
        case 2:
            return (MeldungIstStorniert);
        case 3:
            return (ZutrittsIdentIstStorniert);
        }
        return (undefiniert);
    }

}
