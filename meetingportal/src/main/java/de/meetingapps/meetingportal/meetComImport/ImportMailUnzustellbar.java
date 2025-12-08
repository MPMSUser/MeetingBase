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
package de.meetingapps.meetingportal.meetComImport;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

import de.meetingapps.meetingportal.meetComAllg.CaDateiWrite;
import de.meetingapps.meetingportal.meetComAllg.CaDatumZeit;
import de.meetingapps.meetingportal.meetComBl.BlNummernformen;
import de.meetingapps.meetingportal.meetComDb.DbBundle;
import de.meetingapps.meetingportal.meetComEntities.EclAktienregister;
import de.meetingapps.meetingportal.meetComEntities.EclAktienregisterLoginDaten;
import de.meetingapps.meetingportal.meetComEntities.EclAktienregisterMailRuecklauf;
import de.meetingapps.meetingportal.meetComEntities.EclAktienregisterZusatz;

public class ImportMailUnzustellbar {

    /**
     * 0 = Erstimport, 1 = Nachimport
     */
    public int importArt = 0;

    public String importTxtName = "";

    public String importDateiName = "";
    /** D:\\XY.TXT */

    private DbBundle dbBundle = null;
    CaDateiWrite protokollDatei = null;

    private String mailadresse = "";
    private boolean mailadresseFolgt = false;
    private String passwort = "";
    private String mitgliedsnummer = "";
    private String zeile = null;

    public void importiere(DbBundle aDbBundle) throws FileNotFoundException, IOException {

        int iii = 0;

        dbBundle = aDbBundle;
        dbBundle.dbAktienregisterMailRuecklauf.deleteAll();

        protokollDatei = new CaDateiWrite();
        protokollDatei.oeffne(aDbBundle, "mailUnzustellbar");
        protokollDatei.ausgabe(CaDatumZeit.DatumZeitStringFuerDatenbank());
        protokollDatei.newline();

        try {

            //		      BufferedReader br = new BufferedReader(
            //		              new InputStreamReader(new FileInputStream(importDateiName), "UTF-8"));
            BufferedReader br = new BufferedReader(
                    new InputStreamReader(new FileInputStream(importDateiName), "UTF-8"));

            try {

                zeile = br.readLine(); /*Erste Zeile= Überschrift*/
                while ((zeile = br.readLine()) != null /*&& iii<45000*/) {
                    iii++;
                    if ((iii) % 5000 == 0) {
                        System.out.println(iii + "Import " + CaDatumZeit.DatumZeitStringFuerDatenbank());
                    }

                    zeile = zeile.trim();
                    verarbeiteZeile();
                }

                br.close();
                //	fr.close();
            } catch (IOException e) {
                System.out.print(e.getMessage());
            }

        } catch (FileNotFoundException ex) {
            System.out.print(ex.getMessage());
        }

        protokollDatei.ausgabe(CaDatumZeit.DatumZeitStringFuerDatenbank());
        protokollDatei.newline();

        protokollDatei.schliessen();
        System.out.println("Finito!");

    }

    private void verarbeiteZeile() {
        if (zeile.contains("mail.better-orange.de Mail delivery failed")) {
            /**Dann nächstes E-Mail erreicht*/
            if (mailadresse.isEmpty() && passwort.isEmpty() && mitgliedsnummer.isEmpty()) {
                return; // Dann noch nichts zum Verarbeiten vorhanden
            }
            speichereGeleseneDaten();
            return;
        }
        if (zeile.contains("The following address(es) failed:")) {
            mailadresseFolgt = true;
            return;
        }
        if (mailadresseFolgt) {
            if (!zeile.isEmpty()) {
                mailadresse = zeile;
                mailadresseFolgt = false;
            }
            return;
        }
        if (zeile.contains("Mitgliedsnummer:")) {
            int pos = zeile.indexOf("Mitgliedsnummer:");
            int startpos = pos + 17;
            String hString = zeile.substring(startpos).trim();
            mitgliedsnummer = hString;
            return;
        }
        if (zeile.contains("Passwort:") && !zeile.contains("Passwort:&nbs=")) {
            int pos = zeile.indexOf("Passwort:");
            int startpos = pos + 9;
            String hString = zeile.substring(startpos).trim();
            passwort = hString;
            return;
        }

    }

    private void speichereGeleseneDaten() {

        EclAktienregisterMailRuecklauf lAktienregisterMailRuecklauf = new EclAktienregisterMailRuecklauf();
        lAktienregisterMailRuecklauf.mandant = dbBundle.clGlobalVar.mandant;
        lAktienregisterMailRuecklauf.aktionaersnummer = mitgliedsnummer;
        lAktienregisterMailRuecklauf.mailadresse = mailadresse;
        lAktienregisterMailRuecklauf.passwort = passwort;
        dbBundle.dbAktienregisterMailRuecklauf.insert(lAktienregisterMailRuecklauf);

        boolean verarbeitet = false;
        boolean fehlerAufgetreten = false;
        if (!mailadresse.isEmpty() && !passwort.isEmpty()) {
            /*Mailadresse und Passwort vorhanden; Einträge suchen*/
            fehlerAufgetreten = satzVerarbeiten(1);
            verarbeitet = true;
        }
        if (!mailadresse.isEmpty() && !mitgliedsnummer.isEmpty()) {
            /*Mailadresse und Mitgliedsnummer suchen*/
            fehlerAufgetreten = satzVerarbeiten(2);
            verarbeitet = true;
        }

        if (verarbeitet == true && fehlerAufgetreten == false) {
        } else {
            protokollDatei.ausgabe("Fehler:");
            protokollDatei.ausgabe(mitgliedsnummer);
            protokollDatei.ausgabe(mailadresse);
            protokollDatei.ausgabe(passwort);
            protokollDatei.newline();
        }

        mitgliedsnummer = "";
        mailadresse = "";
        mailadresseFolgt = false;
        passwort = "";
    }

    /**true=>FehlerAufgetreten ja*/
    private boolean satzVerarbeiten(int passwortOderMitgliedsnummer) {
        int anzahlgefunden = 0;
        String mitgliedsnummerIntern = "";
        if (!mitgliedsnummer.isEmpty()) {
            BlNummernformen blNummernformen = new BlNummernformen(dbBundle);
            mitgliedsnummerIntern = blNummernformen.aktienregisterNraufbereitenFuerIntern(mitgliedsnummer);
            mitgliedsnummerIntern = mitgliedsnummerIntern.substring(0, 10);
        }
        /**Suchen aller Sätze mit Mailadresse*/
        EclAktienregisterZusatz lAktienregisterZusatz = new EclAktienregisterZusatz();
        lAktienregisterZusatz.eMailFuerVersand = mailadresse;
        anzahlgefunden = dbBundle.dbAktienregisterZusatz.read(lAktienregisterZusatz);
        if (anzahlgefunden < 0) {
            protokollDatei.ausgabe("Mailadresse nicht gefunden");
            return true;
        }
        if (anzahlgefunden == 1) {
            int ident = dbBundle.dbAktienregisterZusatz.ergebnisPosition(0).aktienregisterIdent;
            dbBundle.dbAktienregister.leseZuAktienregisterIdent(ident);
            EclAktienregister lAktienregister = dbBundle.dbAktienregister.ergebnisPosition(0);
            lAktienregister.versandNummer = 2;
            dbBundle.dbAktienregister.update(lAktienregister);
        }
        if (anzahlgefunden > 1) {
            protokollDatei.ausgabe("Mailadresse doppelt verwendet " + mailadresse);
            protokollDatei.newline();
            int gef = 0;

            for (int i = 0; i < anzahlgefunden; i++) {
                int ident = dbBundle.dbAktienregisterZusatz.ergebnisPosition(i).aktienregisterIdent;
                dbBundle.dbAktienregisterLoginDaten.readIdent(ident);
                EclAktienregisterLoginDaten lAktienregisterLoginDaten = dbBundle.dbAktienregisterLoginDaten.ergebnisArray[0];
                //				if (passwortOderMitgliedsnummer==2){
                //					protokollDatei.ausgabe(lAktienregisterLoginDaten.loginKennung.substring(0, 10));
                //					protokollDatei.ausgabe(mitgliedsnummerIntern);
                //					protokollDatei.newline();
                //				}
                if ((lAktienregisterLoginDaten.passwortInitial.substring(8, 16).compareTo(passwort) == 0
                        && passwortOderMitgliedsnummer == 1)
                        || (lAktienregisterLoginDaten.loginKennung.substring(0, 10)
                                .compareTo(mitgliedsnummerIntern) == 0 && passwortOderMitgliedsnummer == 2)) {
                    gef++;
                    dbBundle.dbAktienregister.leseZuAktienregisterIdent(ident);
                    EclAktienregister lAktienregister = dbBundle.dbAktienregister.ergebnisPosition(0);
                    lAktienregister.versandNummer = 2;
                    dbBundle.dbAktienregister.update(lAktienregister);
                }
            }
            if (gef > 1) {
                protokollDatei.ausgabe("Zuordnung nicht eindeutig");
                protokollDatei.newline();
            }
            if (gef == 0) {
                protokollDatei.ausgabe("Zuordnung nicht möglich");
                protokollDatei.newline();
            }

            /*XXX*/
        }
        /*XXX*/

        return false;
    }
}