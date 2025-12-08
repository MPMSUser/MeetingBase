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

/**Siehe EclKonfigAuswertung*/
public class KonstKonfigAuswertungArt {

    public static int anzahl = 5;

    public final static int unbekannt = 0;
    public final static int erstpraesenz = 1;
    public final static int nachtrag = 2;
    public final static int gesamtpraesenz = 3;
    public final static int abstimmung = 4;
    public final static int inaktiv = 5;

    static public String getText(int nr) {
        switch (nr) {
        case 0: {
            return "undefiniert";
        }
        case 1: {
            return "Erstpräsenz";
        }
        case 2: {
            return "Nachtrag";
        }
        case 3: {
            return "Gesamtpräsenz";
        }
        case 4: {
            return "Abstimmung";
        }
        case 5: {
            return "Inaktiv";
        }
        }
        return "";
    }

}
