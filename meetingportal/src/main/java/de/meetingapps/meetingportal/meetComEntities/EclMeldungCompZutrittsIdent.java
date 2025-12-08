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
package de.meetingapps.meetingportal.meetComEntities;

import java.util.Comparator;

public class EclMeldungCompZutrittsIdent implements Comparator<EclMeldung> {

    @Override
    public int compare(EclMeldung o1, EclMeldung o2) {

        String zutrittsIdent1 = o1.zutrittsIdent;
        while (zutrittsIdent1.length() < 6) {
            zutrittsIdent1 = " " + zutrittsIdent1;
        }

        String zutrittsIdent2 = o2.zutrittsIdent;
        while (zutrittsIdent2.length() < 6) {
            zutrittsIdent2 = " " + zutrittsIdent2;
        }

        return zutrittsIdent1.compareTo(zutrittsIdent2);

    }
}
