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

public class KonstAbstimmungsAblauf {

    public final static int maxNummer = 600;

    /*Abstimmung auswählen / aktivieren etc.*/
    public final static int abstimmungsblockAktivierenDeaktivieren = 1;
    public final static int abstimmungsblockAuswaehlen = 2; /*Alle aktiven auswählen, oder einen einzelnen Block aus aktiven auswählen*/
    public final static int abstimmungRefresh = 3; /*Holen der aktiven Stimmblöcke*/

    /************Präsenz fertigstellen*************************/
    public final static int srvErfassenManuell = 101;
    public final static int srvErfassenBelegleser = 102;

    /*Letzte Abgänge, Wiederzugänge, Erstzugänge erfassen*/
    public final static int praesenzErfassenManuell = 111; 
    /*Alle die, die abgestimmt haben, und noch nicht präsent sind, präsent setzen*/
    public final static int nichtPraesenteAbstimmendePraesentSetzen=112; 

    public final static int srvBeendenAufHV = 121;
    public final static int srvAktivierenAufHV = 122;
    public final static int srvBriefwahlBeendenAusserhalbHV = 123;
    public final static int srvBriefwahlAktivierenAusserhalbHV = 124;
    public final static int srvBriefwahlBeendenKomplett = 125;
    public final static int srvBriefwahlAktivierenKomplett = 126;
    public final static int srvFreigabeDrucken = 127;

    public final static int portalImportieren = 141;

    /*Delay*/
    public final static int delaySammelBeginn = 151;
    public final static int delaySammelEnde = 152;
    public final static int delayEnde = 153;
    public final static int delayAufloesen = 154;

    /*Präsenzfeststellung*/
    public final static int praesenzVorAbstimmungFeststellen = 161;
    public final static int praesenzVorAbstimmungDrucken = 162;
    public final static int prasenzVorAbstimmungFreigabeBuehnensystem = 163;
    public final static int prasenzVorAbstimmungDruckenBuehne = 164;

    public final static int praesenzFuerAbstimmungFeststellen = 171;
    public final static int praesenzFuerAbstimmungDrucken = 172;
    public final static int prasenzFuerAbstimmungFreigabeBuehnensystem = 173;
    public final static int prasenzFuerAbstimmungDruckenBuehne = 174;

    /*Stimmsammlung*/
    public final static int stimmsammlungBeginn = 201;
    public final static int stimmsammlungEnde = 202;
    public final static int stimmsammlungMonitor = 203;
    public final static int stimmsammlungCheck = 204;
    public final static int stimmsammlungFrageSchliessen = 205;
    public final static int stimmsammlungMonitorEK = 206;

    /*Stimmen einlesen*/
    public final static int stimmenOhneMarkierungenEinlesenManuell = 221;
    public final static int stimmenEinlesenScannerDatei = 222;
    public final static int stimmenEinlesenScannerDatenbank = 223;
    public final static int stimmenEinlesenTabletKarte = 224;
    public final static int stimmenMarkierungsBelegeEinlesenManuell = 225;
    public final static int stimmenMarkierungenEinlesenManuell = 226;

    public final static int stimmenDetailPruefenStorno = 229;

    /*Ergebnis ermitteln*/
    public final static int ergebnisAuswerten = 241;
    public final static int ergebnisPlausibiliseren = 242;
    public final static int ergebnisDruckKurzfassung = 243;
    public final static int ergebnisDruckLangsassung = 244;
    public final static int ergebnisPreviewBuehne = 245;
    public final static int ergebnisExportPraesentation = 246;
    public final static int ergebnisPraesentationPDF = 247;
    public final static int ergebnisDruckExport = 248;

    /*Verkünden*/
    public final static int verkuendenFreigabeFuerBuehne = 271;
    public final static int verkuendenDruckKurzfassungBuehne = 272;
    public final static int verkuendenDruckLangfasshungBuehne = 273;

    public final static int verkuendenBeginn = 281;
    public final static int verkuendenEnde = 282;

    /*Nächste Abstimmung*/
    public final static int naechsteAbstimmung = 501;

    static public String getText(int nr) {
        switch (nr) {

        /*Abstimmung auswählen / aktivieren etc.*/
        case 1:
            return "Abstimmungsblock aktivieren/deaktivieren";
        case 2:
            return "---Abstimmungsblock auswählen";
        case 3:
            return "Abstimmungsblock Reload";

        /*Präsenz fertigstellen*/
        case 101:
            return "---SRV Manuell erfassen";
        case 102:
            return "---SRV Belegscanner";

        case 111:
            return "---Präsenz Manuell erfassen";
        case 112:
            return "Nicht-präsente Abstimmende Präset setzen";
        case 121:
            return "---SRV beenden auf HV";
        case 122:
            return "---SRV aktivieren auf HV";
        case 123:
            return "---SRV/Briefwahl beenden außerhalb HV";
        case 124:
            return "---SRV/Briefwahl aktivieren außerhalb HV";
        case 125:
            return "---SRV/Briefwahl beenden komplett";
        case 126:
            return "---SRV/Briefwahl aktivieren komplett";
        case 127:
            return "---SRV-Freigabe drucken";

        case 141:
            return "---Portal importieren";

        /*Delay*/
        case 151:
            return "Delay Aktiv Beginn Einsammeln";
        case 152:
            return "Delay Aktiv Ende Einsammeln";
        case 153:
            return "Delay Beenden";
        case 154:
            return "Delay Aufloesen";

        /*Präsenzfeststellung*/
        case 161:
            return "---Präsenzfeststellung vor Abstimmung";
        case 162:
            return "---Präsenzdruck vor Abstimmung";
        case 163:
            return "---Präsenzfreigabe Bühne vor Abstimmung";
        case 164:
            return "---Präsenzdruck Bühne vor Abstimmung";

        case 171:
            return "---Präsenzfeststellung für Abstimmung";
        case 172:
            return "---Präsenzdruck für Abstimmung";
        case 173:
            return "---Präsenzfreigabe Bühne für Abstimmung";
        case 174:
            return "---Präsenzdruck Bühne für Abstimmung";

        /*Stimmsammlung*/
        case 201:
            return "---Stimmsammlung Beginn";
        case 202:
            return "---Stimmsammlung Ende";
        case 203:
            return "Stimmsammlung Monitor";
        case 204:
            return "---Stimmsammlung Check";
        case 205:
            return "---Stimmsammlung Nachfrage Beenden veranlassen";
        case 206:
            return "Stimmsammlung Monitor Eintrittskarten";

        /*Stimmen einlesen*/
        case 221:
            return "Stimmschnipsel einlesen Manuell";
        case 222:
            return "Stimmbelege einlesen Scanner aus Datei";
        case 223:
            return "Stimmbelege einlesen Scanner aus Datenbank";
        case 224:
            return "Stimmbelege einlesen Tablet-Karte";
        case 225:
            return "Markierungsbelege einlesen Manuell";
        case 226:
            return "Markierungen für Abstimmungsblock einlesen Manuell";

        case 229:
            return "Details prüfen, ggf. Stornieren";

        /*Ergebnis ermitteln*/
        case 241:
            return "Auswerten";
        case 242:
            return "---Plausibilisieren";
        case 243:
            return "---Druck Ergebnis Kurzfassung";
        case 244:
            return "---Druck Ergebnis Langfassung";
        case 245:
            return "---Vorschau Ergebnis Bühne";
        case 246:
            return "---Ergebnis Export Präsentation";
        case 247:
            return "---Ergebnis Präsentation PDF";
        case 248:
            return "Drucken/Export";

        /*Verkünden*/
        case 271:
            return "---Freigabe Ergebnis Bühnensystem";
        case 272:
            return "---Bühnendruck Kurzfassung";
        case 273:
            return "---Bühnendruck Langfassung";

        case 281:
            return "---Verkündung Beginn";
        case 282:
            return "---Verkündung Ende";

        /*Nächste Abstimmung*/
        case 501:
            return "Nächste Abstimmung";

        default:
            return "";
        }
    }

}
