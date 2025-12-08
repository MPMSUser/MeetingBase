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
package de.meetingapps.meetingportal.meetComReports;

import de.meetingapps.meetingportal.meetComAllg.CaDateiWrite;
import de.meetingapps.meetingportal.meetComDb.DbBundle;
import de.meetingapps.meetingportal.meetComEntities.EclAktienregister;

public class RepAktienregister {

    /**Rückgabewert. Falls mitExport, ist hier anschließend der volle Pfad-/Dateiname der erzeugten
     * Export-Datei vorhanden.
     */
    public String exportDatei = "";

    private DbBundle dbBundle = null;

    /** pBehandlungNullBestand (aktuell nur für Suche nach Name!!!):
     * 1 = alle anzeigen (auch 0-Bestand)
     * 2 = alle ohne 0-Bestand
     * 3 = nur 0-Bestand
     * 
     * Wurde verwendet für den Export von Sammelanmeldebögen. 
     * TODO Aktueller Bedarf / weitere Verwendung eher unklar.
     */
    public void sucheAktionaersnameFuerExport(DbBundle pDbBundle, String pSuchstring, int pBehandlungNullBestand) {
        dbBundle = pDbBundle;

        CaDateiWrite dateiExport = null;
        dateiExport = new CaDateiWrite();
        dateiExport.trennzeichen = ';';
        dateiExport.dateiart = ".csv";

        dateiExport.oeffne(dbBundle, "sammelanmeldung");

        dateiExport.ausgabe("Aktionärsnummer");
        dateiExport.ausgabe("Aktienzahl");
        dateiExport.ausgabe("Name");
        dateiExport.ausgabe("Versandadresse");
        dateiExport.newline();

        EclAktienregister lAktienregister = new EclAktienregister();
        lAktienregister.name1 = pSuchstring;

        int rc = dbBundle.dbAktienregister.leseZuAktienregisterEintrag(lAktienregister, pBehandlungNullBestand);
        if (rc > 0) {
            for (int i = 0; i < dbBundle.dbAktienregister.anzErgebnis(); i++) {
                lAktienregister = dbBundle.dbAktienregister.ergebnisPosition(i);

                dateiExport.ausgabe(lAktienregister.aktionaersnummer);
                dateiExport.ausgabe(Long.toString(lAktienregister.stueckAktien));

                String name = lAktienregister.name1;
                if (!lAktienregister.name1.isEmpty()) {
                    name += " ";
                }

                name += lAktienregister.name2;
                if (!lAktienregister.name2.isEmpty()) {
                    name += " ";
                }

                name += lAktienregister.name3;
                if (!lAktienregister.name3.isEmpty()) {
                    name += " ";
                }

                name += lAktienregister.nachname;
                if (!lAktienregister.nachname.isEmpty()) {
                    name += " ";
                }

                name += lAktienregister.vorname;
                if (!lAktienregister.vorname.isEmpty()) {
                    name += " ";
                }

                dateiExport.ausgabe(name);

                dateiExport.ausgabe(lAktienregister.adresszeile1);
                dateiExport.ausgabe(lAktienregister.adresszeile2);
                dateiExport.ausgabe(lAktienregister.adresszeile3);
                dateiExport.ausgabe(lAktienregister.adresszeile4);
                dateiExport.ausgabe(lAktienregister.adresszeile5);
                dateiExport.ausgabe(lAktienregister.adresszeile6);
                dateiExport.ausgabe(lAktienregister.adresszeile7);
                dateiExport.ausgabe(lAktienregister.adresszeile8);
                dateiExport.ausgabe(lAktienregister.adresszeile9);
                dateiExport.ausgabe(lAktienregister.adresszeile10);

                dateiExport.newline();
            }
        }

        dateiExport.schliessen();
        exportDatei = dateiExport.dateiname;

    }
}
