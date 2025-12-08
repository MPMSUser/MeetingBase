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
package de.meetingapps.meetingportal.meetComBl;

/**Überführen in BlNummernformBasis*/
@Deprecated
public class BlAktienregisterNummerAufbereiten {

    /**Bereitet die Aktionärsnummer für die Bildschirmausgabe und den Klartextdruck auf:
     * > Letzte 0 wird abgeschnitten
     * > letzte 1 wird durch -1 ersetzt;
     */
    public static String aufbereitenFuerKlarschrift(String pNummer) {

        String ergebnis = "";
        if (pNummer.isEmpty()) {
            return "";
        }

        int laenge = pNummer.length();
        if (laenge < 2) {
            return pNummer;
        }

        String zusatz = pNummer.substring(laenge - 1);
        ergebnis = pNummer.substring(0, laenge - 1);

        if (zusatz.compareTo("1") == 0) {
            ergebnis = ergebnis + "-1";
        }

        return ergebnis;
    }

    /* "-" entfernen; "0" anhängen, wenn Länge=Param-Länge-Aktionärsnummer*/
    public static String aufbereitenFuerDatenbankZugriff(String pNummer, int pLaenge, int pMandant) {
        if (pLaenge == 0) {
            return pNummer;
        }

        /*12.04.2021*/
//        if (pMandant == 97 && pNummer.length() > 2
//                && (pNummer.substring(0, 2).compareTo("80") == 0 || pNummer.substring(0, 2).compareTo("90") == 0)) {
//            /*TODO #konsolidieren Special für ku097*/
//            pLaenge = 9;
//        }

        String ergebnis = "";
        if (pNummer.isEmpty()) {
            return "";
        }

        int positionStrich = pNummer.indexOf("-");
        if (positionStrich == -1) {
            ergebnis = pNummer;
        } else {
            ergebnis = pNummer.substring(0, positionStrich) + pNummer.substring(positionStrich + 1);
        }

        int laenge = ergebnis.length();
        if (laenge == pLaenge) {
            ergebnis = ergebnis + "0";
        }

        return ergebnis;
    }

    public static String entferneStrichTestweise(String pNummer, int pLaenge, int pMandant) {
        if (pLaenge == 0) {
            return pNummer;
        }
        
        /*12.04.2021*/
//
//        if (pMandant == 97 && pNummer.length() > 2
//                && (pNummer.substring(0, 2).compareTo("80") == 0 || pNummer.substring(0, 2).compareTo("90") == 0)) {
//            /*TODO #konsolidieren Special für ku097*/
//            pLaenge = 9;
//        }

        String ergebnis = "";
        if (pNummer.isEmpty()) {
            return "";
        }
        int positionStrich = pNummer.indexOf("-");
        if (positionStrich == -1) {
            ergebnis = pNummer;
        } else {
            ergebnis = pNummer.substring(0, positionStrich) + pNummer.substring(positionStrich + 1);
        }
        return ergebnis;
    }

}
