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

public class EclPraesenzlisteCompName implements Comparator<EclPraesenzliste> {

    @Override
    public int compare(EclPraesenzliste o1, EclPraesenzliste o2) {

        if (o1.sortierName.compareToIgnoreCase(o2.sortierName) != 0) {
            return o1.sortierName.compareToIgnoreCase(o2.sortierName);
        }

        if (o1.meldeIdentAktionaer < o2.meldeIdentAktionaer) {
            return -1;
        }
        if (o1.meldeIdentAktionaer > o2.meldeIdentAktionaer) {
            return 1;
        }

        if (o1.willenserklaerungIdent < o2.willenserklaerungIdent) {
            return -1;
        }
        if (o1.willenserklaerungIdent > o2.willenserklaerungIdent) {
            return 1;
        }
        return 0;

    }
}
