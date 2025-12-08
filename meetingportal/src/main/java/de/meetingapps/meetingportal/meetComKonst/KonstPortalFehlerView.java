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

public class KonstPortalFehlerView {

    /*Button immer 1214*/
    
    /**Überschrift: 1212
     * Text: 1213*/
    public static final int VERAENDERTER_BESITZ__NEUSTART_DER_FUNKTION=1;
    
    /**Portalfunktion
     * Überschrift: 1215
     * Text: 1216*/
    public static final int FUNKTION_NICHT_MEHR_VERFUEGBAR__ABBRUCH=2;
    
    /**Überschrift: 1217
     * Text: 1218*/
    public static final int STIMMZAEHLUNG_GESCHLOSSEN__ABBRUCH=3;
    
    /**Überschrift: 1219
     * Text: 1220*/
    public static final int STIMMZAEHLUNG_VERAENDERT__NEUSTART=4;
    
    /**Überschrift: 1465
     * Text: 1466*/
    public static final int HVPORTAL_GESCHLOSSEN__ABBRUCH=5;
    
    /**Überschrift: 1467
     * Text: 1468*/
    public static final int KEIN_BESTAND_MEHR_FUER_FUNKTION__ABBRUCH=6;
    
    /**Überschrift: 1469
     * Text: 1470*/
    public static final int ANMELDEFRIST_ABGELAUFEN=7;
    
    /**Willenserklärung*/
    /**Überschrift: 1471
     * Text: 1472*/
    public static final int WILLENSERKLARUNG_NICHT_MEHR_MOEGLICH__ABBRUCH=8;
   
    /**Briefwahl nicht möglich - keine Abstimmungen*/
    /**Überschrift: 1475
     * Text: 1476*/
    public static final int BRIEFWAHL_NICHT_MOEGLICH_KEINE_ABSTIMMUNGEN__ABBRUCH=9;

    /**SRV nicht möglich - keine Abstimmungen*/
    /**Überschrift: 1473
     * Text: 1474*/
    public static final int SRV_NICHT_MOEGLICH_KEINE_ABSTIMMUNGEN__ABBRUCH=10;

    /**Wir werten gerade aus - bitte versuchen Sie es in einigen Minuten wieder
     * (Abstimmungsauswertung läuft - derzeit keine Stimmabgabe möglich)
     * Überschrift: 1491
     * Text: 1492
     * */
    public static final int AUSWERTUNG_LAEUFT_KEINE_WEISUNGSAENDERUNG=11;
    
    /**Anderer User war aktiv (anderer Bevollmächtigter, oder auch Anmeldestelle,
     * Transaktion konnte nicht ausgeführt werden, => Login
     * Überschrift: 1496
     * Text: 1497*/
    public static final int TRANSAKTIONSABBRUCH_ANDERER_USER_AKTIV=12;

    /**Wie AUSWERTUNG_LAEUFT_KEINE_WEISUNGSAENDERUNG,
     * aber bei Erstanmeldung wenn Weisung von anderer Person erteilt => zurück zu 
     * BESITZ_VERTRETUNG_ABFRAGEN
     * Überschrift: 1498
     * Text: 1499*/
    public static final int AUSWERTUNG_LAEUFT_KEINE_WEISUNGSAENDERUNG_WEISUNG_DURCH_ANDERE_ERTEILT=13;

    /**Für Online-Abstimmung / Hybridveranstaltung: keine Abstimmung möglich, da eine der vertretenen
     * Stimmen vor Ort präsent ist.
     * Überschrift: 2023
     * Text: 2024*/
    public static final int STIMMABGABE_NICHT_MOEGLICH_DA_VOR_ORT_PRAESENT=14;

}
