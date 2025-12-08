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

/**Scan-Schnittstelle über Datenbank*/
public class EclScan {

    public int mandant = 0;
    /**Wird über Autoincrement vergeben - 
     * deshalb keine Referenzierung darauf (über Tables hinweg) zulässig*/
    public int ident = 0;

    /**Barcode. Volle Kodierung, wie gelesen
     * LEN=45*/
    public String barcode = "";

    /**Dateiname der Vorlage - nicht bei Stimmkartenscan
     * LEN=100*/
    public String dateiname = "";

    /**Derzeit nicht weiter verwendet
     * LEN=12*/
    public String scanNummer = "";

    /**0 bzw. null => Satz wurde noch nicht weiterverarbeitet. Ansonst wurde verarbeitet.
     * Höchste verarbeitete Nummer steht in Satz mit barcode="XXXXX";
     */
    public int verarbeitet = 0;

    public int istAnmelden = 0;
    public int istBriefwahl = 0;
    public int istSRV = 0;
    public int istSRVHV = 0;
    public int istAbstimmung = 0;

    /**Kann sein: null oder "0" => nicht markiert
     * 1, 2, ??? => 1EK bestellt.
     */
    public String ist1EK = "";

    public boolean markiertIst1eK() {
        if (ist1EK == null || ist1EK.isEmpty() || ist1EK.equals("") || ist1EK.equals("0")) {
            return false;
        }
        return true;
    }

    /**Kann sein: null oder "0" => nicht markiert.
     * Ansonsten 0 1 2 3
     * 1 = Überhaupt markiert
     * 2, 3, etc.: Felder sind ausgefüllt
     */
    public String ist1EKVollmacht = "";
    public int ist2EK = 0;

    public String gesamtmarkierung = "";

    /**61 Positionen, von 0 bis 60 (0 nicht belegt)
     * In Datenbank: pos.01 bis pos.60, LEN=3*/
    public String pos[] = null;

    /**Derzeit nicht verwendet
     * LEN=10
     */
    public String geraeteNummer = "";

    /**Derzeit nicht verwendet
     * LEN=20
     */
    public String zeitstempel = "";

    public EclScan() {
        pos = new String[61];
        for (int i = 0; i <= 60; i++) {
            pos[i] = "";
        }
    }
}
