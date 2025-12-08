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

/**Weg, über den Willenserklärung abgegeben wurde. Siehe EnWillenserklaerungWeg
 * =1	Papier (Post), außerhalb der HV (kann aber durchaus noch während der HV sein)
 * =2	Fax , außerhalb der HV (kann aber durchaus während der HV noch sein)
 * 
 * =11	Email , außerhalb der HV (kann aber durchaus während der HV noch sein)
 * 
 * 21 bis 29 wird als "Weg Internet" betrachtet!
 * =21	Portal – außerhalb der HV (kann aber durchaus während der HV noch sein)
 * =22	App – außerhalb der HV (kann aber durchaus während der HV noch sein)
 * 
 * =51  Dienstleister manuell erfaßt
 * 
 * =101	Schnittstelle von Extern
 * 
 * =201	Auf konventionellem Weg - während der HV (Counter, Stimmsammlung Papier ...)
 * =202	Selbstbedienung - während der HV (Terminals, App als präsent aktiviert)
 * =203	Online-Teilnahme
 */
public class KonstEingabeQuelle {

    public final static int unbekannt = 0;

    public final static int papierPost_ausserhalbHV = 1;
    public final static int fax_ausserhalbHV = 2;
    public final static int eMail_ausserhalbHV = 11;

    public final static int portal = 21;
    public final static int app = 22;

    public final static int sonstigesAnmeldestelle = 51;
    public final static int importBankenDatei = 52;
    public final static int sonstigesVorOrt = 53;

    public final static int schnittstelleVonExtern = 101;

    public final static int konventionell_aufHV = 201;
    public final static int selbstbedienung_aufHV = 202;
    public final static int onlineTeilnahme = 203;

    static public String getText(int nr) {
        switch (nr) {
        case 0: {
            return "unbekannt";
        }
        case 1: {
            return "Papier / Post außerhalb HV";
        }
        case 2: {
            return "Fax außerhalb HV";
        }
        case 11: {
            return "EMail außerhalb HV";
        }
        case 21: {
            return "Portal";
        }
        case 22: {
            return "App";
        }
        case 51: {
            return "Sonstiges durch Anmeldestelle";
        }
        case 52: {
            return "Import Banken-Datei";
        }
        case 53: {
            return "Sonstiges vor Ort";
        }
        case 101: {
            return "Schnittstelle Extern";
        }
        case 201: {
            return "Konventionell auf HV";
        }
        case 202: {
            return "Selbstbedienung auf HV";
        }
        case 203: {
            return "Online-Teilnahme";
        }
        }

        return "";

    }

    static public int getNummerZuText(String pText) {
        if (pText.compareTo("unbekannt") == 0) {
            return 0;
        }
        if (pText.compareTo("Papier / Post außerhalb HV") == 0) {
            return 1;
        }
        if (pText.compareTo("Fax außerhalb HV") == 0) {
            return 2;
        }
        if (pText.compareTo("EMail außerhalb HV") == 0) {
            return 11;
        }
        if (pText.compareTo("Portal") == 0) {
            return 21;
        }
        if (pText.compareTo("App") == 0) {
            return 22;
        }
        if (pText.compareTo("Sonstiges durch Anmeldestelle") == 0) {
            return 51;
        }
        if (pText.compareTo("Import Banken-Datei") == 0) {
            return 52;
        }
        if (pText.compareTo("Sonstiges vor Ort") == 0) {
            return 53;
        }
        if (pText.compareTo("Schnittstelle Extern") == 0) {
            return 101;
        }
        if (pText.compareTo("Konventionell auf HV") == 0) {
            return 201;
        }
        if (pText.compareTo("Selbstbedienung auf HV") == 0) {
            return 202;
        }
        if (pText.compareTo("Online-Teilnahme") == 0) {
            return 203;
        }
        return 0;
    }

}
