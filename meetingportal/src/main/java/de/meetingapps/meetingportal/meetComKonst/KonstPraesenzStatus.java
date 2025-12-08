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

/**statusPraesenz - siehe EclMeldung*/
public class KonstPraesenzStatus {

    public final static int nichtAnwesend = 0;
    public final static int anwesend = 1;
    public final static int warAnwesend = 2;
    public final static int anwesendInSammelkarte = 4;

    static public String getText(int nr) {
        switch (nr) {
        case 0: {
            return "nicht Anwesend";
        }
        case 1: {
            return "anwesend";
        }
        case 2: {
            return "war anwesend";
        }
        case 4: {
            return "anwesend über Sammelkarte";
        }
        }

        return "";

    }
}
