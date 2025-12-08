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

public class KonstSuchlaufVerwendung {

    public final static int unbekannt = 0;

    /**Zuordnung von Beständen zu Investoren für GovVal - aus Aktienregister*/
    public final static int bestandsZuordnungGovValAktienregister = 1;

    /**Zuordnung von Beständen zu Investoren für GovVal - aus Meldungen*/
    public final static int bestandsZuordnungGovValMeldungen = 2;

    /**Zuordnung von Beständen zu Investoren - generell - aus Aktienregister
     * parameter1=EclInsti.ident*/
    public final static int bestandsZuordnungInstiAktienregister = 11;

    /**Zuordnung von Beständen zu Investoren - generell - aus Meldungen*/
    public final static int bestandsZuordnungInstiMeldungen = 12;

    /**Einzelner Kandidat für Stimmausschluß.
     * parameter1 = Stimmausschlußkennzeichen
     * parameter2 = abstimmungsIdent
     * 
     */
    public final static int stimmausschlussEinzel = 101;

    /**Nimmt alle Ergebnisse aus den einzelnen Kandidaten mit dem entsprechenden
     * Stimmausschlußkennzeichen.
     * 
     * Sowie eigene Suchlaufbegriffe.
     * 
     * parameter1 = Stimmausschlußkennzeichen
     */
    public final static int stimmausschlussGesamt = 102;

    public static boolean isRegisterSuche(int pWert) {
        switch (pWert) {
        case KonstSuchlaufVerwendung.bestandsZuordnungInstiAktienregister:
            return true;
        }

        return false;

    }

    public static boolean isMeldebestandSuche(int pWert) {
        switch (pWert) {
        case KonstSuchlaufVerwendung.bestandsZuordnungInstiMeldungen:
            return true;
        }

        return false;

    }

}
