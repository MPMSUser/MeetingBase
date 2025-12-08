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
package de.meetingapps.meetingportal.meetComDb;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import de.meetingapps.meetingportal.meetComAllg.CaBug;
import de.meetingapps.meetingportal.meetComAllg.CaFehler;
import de.meetingapps.meetingportal.meetComEntities.EclKonfigAuswertung;
import de.meetingapps.meetingportal.meetComKonst.KonstKonfigAuswertungArt;

public class DbKonfigAuswertung {

    private int logDrucken=10;
    
    private Connection verbindung = null;
    private DbBundle dbBundle = null;

    public EclKonfigAuswertung ergebnisArray[] = null;

    /*************************Initialisierung***************************/
    public DbKonfigAuswertung(DbBundle pDbBundle) {
        /* Verbindung in lokale Daten eintragen*/
        if (pDbBundle == null) {
            CaBug.drucke("DbKonfigAuswertung.init 001 - dbBundle nicht initialisiert");
            return;
        }
        if (pDbBundle.dbBasis == null) {
            CaBug.drucke("DbKonfigAuswertung.init 002 - dbBasis nicht initialisiert");
            return;
        }

        verbindung = pDbBundle.dbBasis.verbindung;
        dbBundle = pDbBundle;
    }

    /**************Anzahl der Ergebnisse der read*-Methoden**************************/
    public int anzErgebnis() {
        if (ergebnisArray == null) {
            return 0;
        }
        return ergebnisArray.length;
    }

    /**********Liefert pN-tes Element des Ergebnisses der read*Methoden**************
     * pN geht von 0 bis anzErgebnis-1*/
    public EclKonfigAuswertung ergebnisPosition(int pN) {
        if (ergebnisArray == null) {
            CaBug.drucke("DbKonfigAuswertung.ergebnisPosition 001");
            return null;
        }
        if (pN < 0) {
            CaBug.drucke("DbKonfigAuswertung.ergebnisPosition 002");
            return null;
        }
        if (pN >= ergebnisArray.length) {
            CaBug.drucke("DbKonfigAuswertung.ergebnisPosition 003");
            return null;
        }
        return ergebnisArray[pN];
    }

    /**************************deleteAll Löschen aller Datensätze  eines Mandanten****************/
    public int deleteAll() {
        return dbBundle.dbLowLevel
                .deleteMandant("DELETE FROM " + dbBundle.getSchemaMandant() + "tbl_konfigauswertung where mandant=?;");
    }

    /*******Checken, ob table überhaupt vorhanden ist***************************/
    public boolean checkTableVorhanden() {
        DbLowLevel lDbLowLevel = new DbLowLevel(dbBundle);
        return lDbLowLevel.checkTableVorhanden("SELECT * from tbl_konfigauswertung WHERE mandant=0; ");
    }

    /************Neuanlegen Table******************************/
    public int createTable() {
        int rc = 0;
        DbLowLevel lDbLowLevel = new DbLowLevel(dbBundle);
        rc = lDbLowLevel.createTable("CREATE TABLE " + dbBundle.getSchemaMandant() + "tbl_konfigauswertung ( "
                + "`mandant` int(11) NOT NULL, " + "`ident` int(11) NOT NULL, " + "`fuerFunktion` int(11) NOT NULL, "
                + "`nr` int(11) NOT NULL, " + "`positionInLauf` int(11) NOT NULL, "
                + "`ausgeloesteFunktion` int(11) NOT NULL, " + "`ausgeloesteFormNr` int(11) NOT NULL, "
                + "`textFuerFormular1` char(200) DEFAULT NULL, " + "`textFuerFormular2` char(200) DEFAULT NULL, "
                + "`sortierung` int(11) NOT NULL, " + "`gattung` int(11) NOT NULL, " + "`ausgabeWeg` int(11) NOT NULL, "
                + "`dateinamePdf` char(200) DEFAULT NULL, " + "PRIMARY KEY (`ident`, `mandant`) " + ")  ");
        CaBug.druckeLog("DbKonfigAuswertung create table rc=" + rc, logDrucken, 10);
        return rc;
    }

    /********** dekodiert die aktuelle Position aus ergebnis in EclAktienregisterEintrag und gibt dieses zurück******/
    EclKonfigAuswertung decodeErgebnis(ResultSet pErgebnis) {

        EclKonfigAuswertung lKonfigAuswertung = new EclKonfigAuswertung();

        try {
            lKonfigAuswertung.mandant = pErgebnis.getInt("ka.mandant");
            lKonfigAuswertung.ident = pErgebnis.getInt("ka.ident");
            lKonfigAuswertung.fuerFunktion = pErgebnis.getInt("ka.fuerFunktion");
            lKonfigAuswertung.nr = pErgebnis.getInt("ka.nr");
            lKonfigAuswertung.positionInLauf = pErgebnis.getInt("ka.positionInLauf");
            lKonfigAuswertung.ausgeloesteFunktion = pErgebnis.getInt("ka.ausgeloesteFunktion");
            lKonfigAuswertung.ausgeloesteFormNr = pErgebnis.getInt("ka.ausgeloesteFormNr");
            lKonfigAuswertung.textFuerFormular1 = pErgebnis.getString("ka.textFuerFormular1");
            lKonfigAuswertung.textFuerFormular2 = pErgebnis.getString("ka.textFuerFormular2");
            lKonfigAuswertung.sortierung = pErgebnis.getInt("ka.sortierung");
            lKonfigAuswertung.gattung = pErgebnis.getInt("ka.gattung");
            lKonfigAuswertung.ausgabeWeg = pErgebnis.getInt("ka.ausgabeWeg");
            lKonfigAuswertung.dateinamePdf = pErgebnis.getString("ka.dateinamePdf");
        } catch (Exception e) {
            CaBug.drucke("DbKonfigAuswertung.decodeErgebnis 001");
            System.err.println(" " + e.getMessage());
        }

        return lKonfigAuswertung;
    }

    /********************* Fuellen Prepared Statement mit allen Feldern, beginnend bei offset.*****************
     * Kann sowohl für Insert, als auch für update verwendet werden.
     * 
     * offset= Startposition des ersten Feldes (also z.B. 1)
     */
    private int anzfelder = 13; /*Anpassen auf Anzahl der Felder pro Datensatz*/

    void fuellePreparedStatementKomplett(PreparedStatement pPStm, int pOffset, EclKonfigAuswertung pKonfigAuswertung) {

        int startOffset = pOffset; /*Nur erforderlich zum Überprüfen von Programmierfehlern - Feldanzahl*/

        try {
            pPStm.setInt(pOffset, pKonfigAuswertung.mandant);
            pOffset++;
            pPStm.setInt(pOffset, pKonfigAuswertung.ident);
            pOffset++;
            pPStm.setInt(pOffset, pKonfigAuswertung.fuerFunktion);
            pOffset++;
            pPStm.setInt(pOffset, pKonfigAuswertung.nr);
            pOffset++;
            pPStm.setInt(pOffset, pKonfigAuswertung.positionInLauf);
            pOffset++;
            pPStm.setInt(pOffset, pKonfigAuswertung.ausgeloesteFunktion);
            pOffset++;
            pPStm.setInt(pOffset, pKonfigAuswertung.ausgeloesteFormNr);
            pOffset++;
            pPStm.setString(pOffset, pKonfigAuswertung.textFuerFormular1);
            pOffset++;
            pPStm.setString(pOffset, pKonfigAuswertung.textFuerFormular2);
            pOffset++;
            pPStm.setInt(pOffset, pKonfigAuswertung.sortierung);
            pOffset++;
            pPStm.setInt(pOffset, pKonfigAuswertung.gattung);
            pOffset++;
            pPStm.setInt(pOffset, pKonfigAuswertung.ausgabeWeg);
            pOffset++;
            pPStm.setString(pOffset, pKonfigAuswertung.dateinamePdf);
            pOffset++;

            if (pOffset - startOffset != anzfelder) {
                /*Nur wg. Überprüfung auf Programmierfehler - alle Felder berücksichtigt?*/
                CaBug.drucke("DbKonfigAuswertung.fuellePreparedStatementKomplett 002");
            }

        } catch (SQLException e) {
            CaBug.drucke("DbKonfigAuswertung.fuellePreparedStatementKomplett 001");
            e.printStackTrace();
        }

    }

    /**Insert
     * 
     * Feld drucklaufNr wird hier belegt
     * 
     * Returnwert:
     * =1 => Insert erfolgreich
     * ansonsten: Fehler
     */
    public int insert(EclKonfigAuswertung pDrucklauf) {

        int erg = 0;

        /* neue InterneIdent vergeben */
        erg = dbBundle.dbBasis.getInterneIdentKonfigAuswertung();
        if (erg < 1) {
            CaBug.drucke("DbKonfigAuswertung.insert 002");
            return (erg);
        }

        System.out.println("DbKonfigAuswertung erg=" + erg);
        pDrucklauf.ident = erg;
        pDrucklauf.mandant = dbBundle.clGlobalVar.mandant;

        try {
            /*Felder Neuanlage füllen*/
            String lSql = "INSERT INTO " + dbBundle.getSchemaMandant() + "tbl_konfigauswertung " + "("
                    + "mandant, ident, fuerFunktion, nr, positionInLauf," + "ausgeloesteFunktion, "
                    + "ausgeloesteFormNr, textFuerFormular1, textFuerFormular2, sortierung, gattung, "
                    + "ausgabeWeg, dateinamePdf " + ")" + "VALUES (" + "?, ?, ?, ?, ?, " + "?, " + "?, ?, ?, ?, ?, "
                    + "?, ? " + ")";
            PreparedStatement lPStm = verbindung.prepareStatement(lSql, ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
            fuellePreparedStatementKomplett(lPStm, 1, pDrucklauf);
            erg = lPStm.executeUpdate();
            lPStm.close();
        } catch (Exception e2) {
            CaBug.drucke("DbKonfigAuswertung.insert 001");
            System.err.println(" " + e2.getMessage());
        }

        if (erg == 0) {/*Fehler beim Einfügen - d.h. z.B. primaryKey bereits vorhanden*/
            return (-1);
        }

        return (1);
    }

    /**Lese alle Druckläufe-Konfigs
     * */
    public int readAll() {
        int anzInArray = 0;
        PreparedStatement lPStm = null;

        try {
            String lSql = "SELECT * from " + dbBundle.getSchemaMandant()
                    + "tbl_konfigauswertung ka WHERE mandant=? ORDER BY ka.fuerFunktion, ka.nr, ka.positionInLauf";
            lPStm = verbindung.prepareStatement(lSql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);

            lPStm.setInt(1, dbBundle.clGlobalVar.mandant);

            ResultSet lErgebnis = lPStm.executeQuery();
            lErgebnis.last();
            anzInArray = lErgebnis.getRow();
            lErgebnis.beforeFirst();

            ergebnisArray = new EclKonfigAuswertung[anzInArray];

            int i = 0;
            while (lErgebnis.next() == true) {
                ergebnisArray[i] = this.decodeErgebnis(lErgebnis);
                i++;
            }
            lErgebnis.close();
            lPStm.close();

        } catch (Exception e) {
            CaBug.drucke("DbKonfigAuswertung.readAll_technisch 003");
            System.err.println(" " + e.getMessage());
            return (-1);
        }
        return (anzInArray);
    }

    /**Lese alle Druckläufe
     * 0 = alle
     * KonstKonfigAuswertungArt.erstpraesenz, nachtrag, gesamtpraesenz: immer diese 3 zusammen
     * abstimmung
     * */
    public int readAll_Laeufe(int pArt) {
        int anzInArray = 0;
        PreparedStatement lPStm = null;

        try {
            String lSql = "SELECT DISTINCT ka.fuerFunktion, ka.nr from " + dbBundle.getSchemaMandant()
                    + "tbl_konfigauswertung ka WHERE mandant=? ";
            if (pArt == KonstKonfigAuswertungArt.erstpraesenz || pArt == KonstKonfigAuswertungArt.nachtrag
                    || pArt == KonstKonfigAuswertungArt.gesamtpraesenz) {
                lSql = lSql + " AND (ka.fuerFunktion=1 OR ka.fuerFunktion=2 OR ka.fuerFunktion=3) ";
            }
            if (pArt == KonstKonfigAuswertungArt.abstimmung) {
                lSql = lSql + " AND ka.fuerFunktion=4 ";
            }
            lSql = lSql + "ORDER BY ka.fuerFunktion, ka.nr";
            lPStm = verbindung.prepareStatement(lSql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);

            lPStm.setInt(1, dbBundle.clGlobalVar.mandant);

            ResultSet lErgebnis = lPStm.executeQuery();
            lErgebnis.last();
            anzInArray = lErgebnis.getRow();
            lErgebnis.beforeFirst();

            ergebnisArray = new EclKonfigAuswertung[anzInArray];

            int i = 0;
            while (lErgebnis.next() == true) {
                EclKonfigAuswertung lKonfigAuswertung = new EclKonfigAuswertung();
                lKonfigAuswertung.fuerFunktion = lErgebnis.getInt("ka.fuerFunktion");
                lKonfigAuswertung.nr = lErgebnis.getInt("ka.nr");

                ergebnisArray[i] = lKonfigAuswertung;
                i++;
            }
            lErgebnis.close();
            lPStm.close();

        } catch (Exception e) {
            CaBug.drucke("DbKonfigAuswertung.readAll_technisch 003");
            System.err.println(" " + e.getMessage());
            return (-1);
        }
        return (anzInArray);
    }

    /**Einlesen einer bestimmten Aufgabe mit  ident*/
    public int read(int ident) {
        int anzInArray = 0;
        PreparedStatement lPStm = null;

        try {
            String lSql = "SELECT * from " + dbBundle.getSchemaMandant()
                    + "tbl_konfigauswertung ka WHERE ka.ident=? and ka.mandant=?;";
            lPStm = verbindung.prepareStatement(lSql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            lPStm.setInt(1, ident);
            lPStm.setInt(2, dbBundle.clGlobalVar.mandant);

            ResultSet lErgebnis = lPStm.executeQuery();
            lErgebnis.last();
            anzInArray = lErgebnis.getRow();
            lErgebnis.beforeFirst();

            ergebnisArray = new EclKonfigAuswertung[anzInArray];

            int i = 0;
            while (lErgebnis.next() == true) {
                ergebnisArray[i] = this.decodeErgebnis(lErgebnis);
                i++;
            }

            lErgebnis.close();
            lPStm.close();

        } catch (Exception e) {
            CaBug.drucke("DbKonfigAuswertung.read 003");
            System.err.println(" " + e.getMessage());
            return (-1);
        }
        return (anzInArray);
    }

    /**Update. Versionsnummer wird um 1 hochgezählt
     * 
     * Feld mandant wird von dieser Funktion nicht verändert.
     * 
     * Zum Sicherstellen der Multiuserfähigkeit muß in jedem Fall der rc CaFehler.pfXyWurdeVonAnderemBenutzerVeraendert
     * nach Aufruf dieser Funktion abgefangen werden.
     * Ansonsten: rc=1 => ok, ansonsten Fehler
     */
    public int update(EclKonfigAuswertung pKonfigAuswertung) {

        try {
            String lSql = "UPDATE " + dbBundle.getSchemaMandant() + "tbl_konfigauswertung SET "
                    + "mandant=?, ident=?, fuerFunktion=?, nr=?, positionInLauf=?," + "ausgeloesteFunktion=?, "
                    + "ausgeloesteFormNr=?, textFuerFormular1=?, textFuerFormular2=?, sortierung=?, gattung=?, "
                    + "ausgabeWeg=?, dateinamePdf=? " + "WHERE " + "ident=? AND mandant=?";

            PreparedStatement lPStm = verbindung.prepareStatement(lSql);
            fuellePreparedStatementKomplett(lPStm, 1, pKonfigAuswertung);
            lPStm.setInt(anzfelder + 1, pKonfigAuswertung.ident);
            lPStm.setInt(anzfelder + 2, dbBundle.clGlobalVar.mandant);

            int ergebnis = lPStm.executeUpdate();
            lPStm.close();
            if (ergebnis == 0) {
                return (CaFehler.pfXyWurdeVonAnderemBenutzerVeraendert);
            }
        } catch (Exception e1) {
            CaBug.drucke("DbKonfigAuswertung.update 001");
            System.err.println(" " + e1.getMessage());
            return (CaFehler.pfdXyBereitsVorhanden);
        }

        return (1);
    }

    public int updateMandant() {
        return dbBundle.dbLowLevel.rawUpdateMandant(dbBundle.getSchemaMandant() + "tbl_konfigauswertung");
    }

    public int delete(int pKonfigAuswertung) {

        try {

            String sql = "DELETE FROM " + dbBundle.getSchemaMandant()
                    + "tbl_konfigauswertung WHERE ident=? AND mandant=? ";

            PreparedStatement pstm1 = verbindung.prepareStatement(sql);
            pstm1.setInt(1, pKonfigAuswertung);
            pstm1.setInt(2, dbBundle.clGlobalVar.mandant);

            int ergebnis1 = pstm1.executeUpdate();
            pstm1.close();
            if (ergebnis1 == 0) {
                return (CaFehler.pfXyWurdeVonAnderemBenutzerVeraendert);
            }
        } catch (Exception e1) {
            CaBug.drucke("DbAbstimmungen.delete 001");
            System.err.println(" " + e1.getMessage());
            return (CaFehler.pfdXyBereitsVorhanden);
        }

        return (1);
    }

}
