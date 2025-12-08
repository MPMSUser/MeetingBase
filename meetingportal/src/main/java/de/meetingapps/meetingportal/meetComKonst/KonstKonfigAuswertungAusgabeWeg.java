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
public class KonstKonfigAuswertungAusgabeWeg {

    public static int anzahl = 25;

    public final static int unbekannt = 0;
    public final static int druckerAbfragen = 1;
    public final static int wieVorherigerDrucker = 2;
    public final static int nn3 = 3;
    public final static int nn4 = 4;
    public final static int nn5 = 5;
    public final static int nn6 = 6;
    public final static int nn7 = 7;
    public final static int nn8 = 8;
    public final static int nn9 = 9;
    public final static int nn10 = 10;
    public final static int drucker1 = 11;
    public final static int drucker2 = 12;
    public final static int drucker3 = 13;
    public final static int drucker4 = 14;
    public final static int drucker5 = 15;
    public final static int drucker6 = 16;
    public final static int drucker7 = 17;
    public final static int drucker8 = 18;
    public final static int drucker9 = 19;
    public final static int drucker10 = 20;
    public final static int pfadPraesentation = 21;
    public final static int pfadBuehne = 22;
    public final static int pfadPPExcel = 23;
    public final static int pfadKundenordner = 24;
    public final static int pfadMeetingOutput = 25;

    static public String getText(int nr) {
        switch (nr) {
        case 0: {
            return "undefiniert";
        }
        case 1: {
            return "Drucker abfragen";
        }
        case 2: {
            return "vorheriger Drucker";
        }
        case 3: {
            return "";
        }
        case 4: {
            return "";
        }
        case 5: {
            return "";
        }
        case 6: {
            return "";
        }
        case 7: {
            return "";
        }
        case 8: {
            return "";
        }
        case 9: {
            return "";
        }
        case 10: {
            return "";
        }
        case 11: {
            return "Drucker 1";
        }
        case 12: {
            return "Drucker 2";
        }
        case 13: {
            return "Drucker 3";
        }
        case 14: {
            return "Drucker 4";
        }
        case 15: {
            return "Drucker 5";
        }
        case 16: {
            return "Drucker 6";
        }
        case 17: {
            return "Drucker 7";
        }
        case 18: {
            return "Drucker 8";
        }
        case 19: {
            return "Drucker 9";
        }
        case 20: {
            return "Drucker 10";
        }
        case 21: {
            return "Ex Pfad Präsentation";
        }
        case 22: {
            return "Ex Pfad Bühnensystem";
        }
        case 23: {
            return "Ex Pfad PP-Excel";
        }
        case 24: {
            return "Pfad Kundenordner";
        }
        case 25: {
            return "Pfad MeetingOutput";
        }
        }
        return "";
    }

}
