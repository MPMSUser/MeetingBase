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

public class KonstStimmart {

    
    /**Nur in Raw-Sätzen: auf dem Stimmabgabemedium, durch das der entsprechende Raw-Satz
     * erzeugt wurde, ist dieser Punkt nicht enthalten
     */
    public final static int nichtAufMedium=-1;
    
    /**Es wurde keine Weisung erfaßt (auch keine "Freie")*/
    public final static int nichtMarkiert = 0;
    /*Standard-Stimmarten (1-5)*/
    public final static int ja = 1;
    public final static int nein = 2;
    public final static int enthaltung = 3;
    public final static int ungueltig = 4;
    public final static int nichtTeilnahme = 5;
    /*Verweis auf Split (6)*/
    public final static int splitLiegtVor = 6;
    /*Sonstiges (7-12)*/
    /**Abstimmung im freien Ermessen des Gebers**/
    public final static int frei = 7;
    /**Nicht-Markiert darf nur im Raw-Format vorliegen. Die Interpretation muss ja bereits zum Zeitpunkt der
     * Weisungserteilung bekannt und fest sein - und darf sich z.B. nicht ändern wenn Subtraktionsverfahren
     * umgekehrt wird**/
    public final static int stimmartDepcrecated = 8;
    /**z.B. wg. Vorstand etc.**/
    public final static int stimmausschluss = 9;
    /**Ab hier nicht mehr im Split, und auch nicht in Summe von Sammelkarten berücksichtigt*/
    /**Gattung stimmt für diesen Beschluß nicht ab**/
    public final static int nichtStimmberechtigt = 10;
    /**Abstimmung zum Zeitpunkt der Weisungsabgabe noch nicht bekannt**/
    public final static int abstimmungNichtBekannt = 11;
    /**Weisung "Gegenantrag wird unterstützt" (Markierung in Formular bzw. im Portal*/
    public final static int gegenantragWirdUnterstuetzt = 12;

    static public String getText(int nr) {
        switch (nr) {
        case -1: {
            return "Nicht auf Medium";
        }
        case 0: {
            return "NichtMarkiert";
        }
        case 1: {
            return "Ja";
        }
        case 2: {
            return "Nein";
        }
        case 3: {
            return "Enthaltung";
        }
        case 4: {
            return "Ungueltig";
        }
        case 5: {
            return "NichtTeilnahme";
        }
        case 6: {
            return "SplitLiegtVor";
        }
        case 7: {
            return "Frei";
        }
        case 8: {
            return "Deprecated";
        }
        case 9: {
            return "Stimmausschluss";
        }
        case 10: {
            return "NichtStimmberechtigt";
        }
        case 11: {
            return "AbstimmungNichtBekannt";
        }
        case 12: {
            return "GegenantragWirdUnterstuetzt";
        }
        }

        return "";

    }

    static public String getTextKurz(int nr) {
        switch (nr) {
        case -1: {
            return "x";
        }

        case 0: {
            return ".";
        }
        case 1: {
            return "J";
        }
        case 2: {
            return "N";
        }
        case 3: {
            return "E";
        }
        case 4: {
            return "U";
        }
        case 5: {
            return "NT";
        }
        case 6: {
            return "?";
        }
        case 7: {
            return "F";
        }
        case 8: {
            return "-";
        }
        case 9: {
            return "S";
        }
        case 10: {
            return "R";
        } /*Früher: auch S. Geändert wg. Doppelbelegung*/
        case 11: {
            return "3";
        }
        case 12: {
            return "G";
        }
        }

        return "";

    }

    static public int getIntVonTextKurz(String pText) {
        String lText=pText.toUpperCase();
        switch (lText) {
        case "x": {
            return -1;
        }
        case ".": {
            return 0;
        }
        case "J": {
            return 1;
        }
        case "N": {
            return 2;
        }
        case "E": {
            return 3;
        }
        case "U": {
            return 4;
        }
        case "NT": {
            return 5;
        }
        case "?": {
            return 6;
        }
        case "F": {
            return 7;
        }
        case "-": {
            return 8;
        }
        case "S": {
            return 9;
        }
        case "R": {
            return 10;
        } /*Früher: auch S. Geändert wg. Doppelbelegung*/
        case "3": {
            return 11;
        }
        case "G": {
            return 12;
        }
        }
        return 0;

    }

}
