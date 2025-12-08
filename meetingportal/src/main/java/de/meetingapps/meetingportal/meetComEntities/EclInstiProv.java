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

/**Provisorium für institutionelle Imports*/
public class EclInstiProv {

    /****************Technische Felder*****************************/
    /**Mandant*/
    public int mandant = 0;

    /** eindeutiger Key*/
    public int ident = 0;

    public int verarbeitungslauf = 0;

    /**Quell-Dateiname LEN=100*/
    public String quellDateiname = "";

    /****************Input von Import******************************/

    /**LEN=20*/
    public String aktionaersnummer = "";
    /**LEN=200*/
    public String aktionaersname = "";

    public long aktienzahl = 0;

    /**1 => Vorgabe: Fix-Anmeldung*/
    public int sollFixAnmeldung = 0;

    public int gesamtMarkierung = 0;

    public int[] einzelMarkierungen = null;

    public int sollGewaehlteSammelkarteIdent = 0;

    /**Vorgabe: auf Fremdbesitz buchen*/
    public int sollVorrangFremdbesitz = 0;

    /***********Manuelle Korrekturen - überschreiben die "Soll"-Werte****/
    /**1 => Vorgabe: Fix-Anmeldung*/
    public int korrFixAnmeldung = -1;

    public int korrGewaehlteSammelkarteIdent = -1;

    /**Vorgabe: auf Fremdbesitz buchen*/
    public int korrVorrangFremdbesitz = -1;

    /*************Tatsächliche Werte - aus Soll und manuelle Korrekturen - nicht in Datenbank!!!************/
    /**1 => Vorgabe: Fix-Anmeldung*/
    public int istFixAnmeldung = -1;

    public int istGewaehlteSammelkarteIdent = -1;

    /**Vorgabe: auf Fremdbesitz buchen*/
    public int istVorrangFremdbesitz = -1;

    /***********Ergebnis-Werte - werden nach der Verarbeitung ergänzt / überschrieben*******************/

    /**LEN=200*/
    public String aktionaersnameAR = "";

    public long aktienzahlAR = 0;

    public int codeVerarbeitetTest = 0;
    public String textVerarbeitetTest = "";

    public int codeVerarbeitetVerarbeitung = 0;
    public String textVerarbeitetVerarbeitung = "";

    public int sammelkartenart = 0;

    public EclInstiProv() {
        einzelMarkierungen = new int[201];
        for (int i = 0; i <= 200; i++) {
            einzelMarkierungen[i] = 0;
        }
    }

}
