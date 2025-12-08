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

public enum EnAzStatusPraesenz {

    NichtAnwesend, Anwesend, /*Setzt unwiderruflich (außer Storno) auch StatusWarPraesenz*/
    WarAnwesend, Briefwahl, AnwesendAlsGast, /*Setzt unwiderruflich (außer Storno) auch StatusWarPraesenzGast*/
    WarAnwesendAlsGast;

    public static int toClMeldung(EnAzStatusPraesenz StatusP) {
        switch (StatusP) {
        case NichtAnwesend:
            return 0;
        case Anwesend:
            return 1;
        case WarAnwesend:
            return 2;
        case Briefwahl:
            return 3;
        case AnwesendAlsGast:
            return 4;
        case WarAnwesendAlsGast:
            return 5;
        }

        return 99;

    }

    public static EnAzStatusPraesenz fromClMeldung(EclMeldung meldung) {
        switch (meldung.statusPraesenz) {
        case 0:
            return (NichtAnwesend);
        case 1:
            return (Anwesend);
        case 2:
            return (WarAnwesend);
        case 3:
            return (Briefwahl);
        case 4:
            return (AnwesendAlsGast);
        case 5:
            return (WarAnwesendAlsGast);
        }
        return (NichtAnwesend);
    }

    @Override
    public String toString() {
        switch (this) {
        case NichtAnwesend:
            return ("Nicht Anwesend");
        case Anwesend:
            return ("Anwesend");
        case WarAnwesend:
            return ("War Anwesend");
        case Briefwahl:
            return ("Briefwahl");
        case AnwesendAlsGast:
            return ("Anwesend als Gast");
        case WarAnwesendAlsGast:
            return ("War Anwesend als Gast");

        }
        return ("Undefiniert");
    }
}
