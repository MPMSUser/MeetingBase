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

public class KonstKartenklasse {

    public final static int unbekannt = 0;
    public final static int eintrittskartennummer = 1;
    public final static int gastkartennummer = 2;
    public final static int stimmkartennummer = 3;
    public final static int stimmkartennummerSecond = 4;
    public final static int appIdent = 5;
    public final static int personenIdent = 6;
    /**Hinweis: wird beim "Dekodieren" nicht nach außen gegeben, sonder immern nur eintrittskartennummer.
     * Beim Kodieren kann das mit übergeben werden.
     */
    public final static int eintrittskartennummerNeben = 7;

    /**Aktionärsnummer - kommt in App-Ident vor, wenn für einen Bestand keine EK ausgestellt wurde (oder 0-Bestand o.ä.)*/
    public final static int aktionaersnummer = 9;

    static public String getText(int nr) {
        switch (nr) {
        case 0: {
            return "";
        }
        case 1: {
            return "Eintrittskartennummer";
        }
        case 2: {
            return "Gastkartennummer";
        }
        case 3: {
            return "Stimmblocknummer";
        }
        case 4: {
            return "Voternummer";
        }
        case 5: {
            return "AppIdent";
        }
        case 6: {
            return "PersonenIdent";
        }
        case 9: {
            return "Aktionärsnummer";
        }
        }

        return "";

    }
}
