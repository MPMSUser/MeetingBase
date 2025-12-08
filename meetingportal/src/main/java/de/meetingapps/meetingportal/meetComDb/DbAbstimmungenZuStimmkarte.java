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

/**Hinweis: diese Klasse kann als "Musterklasse" / Blaupause für andere Db-Zugriffsklassen verwendet werden*/

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import de.meetingapps.meetingportal.meetComAllg.CaBug;
import de.meetingapps.meetingportal.meetComAllg.CaFehler;
import de.meetingapps.meetingportal.meetComEntities.EclAbstimmungenZuStimmkarte;

public class DbAbstimmungenZuStimmkarte extends DbRootExecute {

    private Connection verbindung = null;
    private DbBasis dbBasis = null;
    private DbBundle dbBundle = null;

    EclAbstimmungenZuStimmkarte ergebnisArray[] = null;

    /*************************Initialisierung***************************/
    public DbAbstimmungenZuStimmkarte(DbBundle pDbBundle) {
        /* Verbindung in lokale Daten eintragen*/
        if (pDbBundle == null) {
            CaBug.drucke("DbAbstimmungenZuStimmkarte.init 001 - dbBundle nicht initialisiert");
            return;
        }
        if (pDbBundle.dbBasis == null) {
            CaBug.drucke("DbAbstimmungenZuStimmkarte.init 002 - dbBasis nicht initialisiert");
            return;
        }

        dbBasis = pDbBundle.dbBasis;
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

    /**************Anzahl der Ergebnisse der read*-Methoden**************************/
    public EclAbstimmungenZuStimmkarte[] ergebnis() {
        return ergebnisArray;
    }

    /**********Liefert pN-tes Element des Ergebnisses der read*Methoden**************
     * pN geht von 0 bis anzErgebnis-1*/
    public EclAbstimmungenZuStimmkarte ergebnisPosition(int pN) {
        if (ergebnisArray == null) {
            CaBug.drucke("DbAbstimmungenZuStimmkarte.ergebnisPosition 001");
            return null;
        }
        if (pN < 0) {
            CaBug.drucke("DbAbstimmungenZuStimmkarte.ergebnisPosition 002");
            return null;
        }
        if (pN >= ergebnisArray.length) {
            CaBug.drucke("DbAbstimmungenZuStimmkarte.ergebnisPosition 003");
            return null;
        }
        return ergebnisArray[pN];
    }

    public int createTable() {
        int rc = 0;
        DbLowLevel lDbLowLevel = new DbLowLevel(dbBundle);
        rc = lDbLowLevel.createTable("CREATE TABLE " + dbBundle.getSchemaMandant() + "tbl_abstimmungenzustimmkarte ( "
                + "`mandant` int(11) NOT NULL, " + "`stimmkartenNr` int(11) NOT NULL, "
                + "`db_version` bigint(20) DEFAULT NULL, " + "`identAbstimmung` int(11) DEFAULT NULL, "
                + "`identAbstimmungAufKarte` int(11) NOT NULL, " + "`positionInStimmkarte` int(11) DEFAULT NULL, "
                + "PRIMARY KEY (`stimmkartenNr`,`identAbstimmungAufKarte`,`mandant`) " + ") ");
        return rc;
    }

    /**************************deleteAll Löschen aller Datensätze  eines Mandanten****************/
    public int deleteAll() {
        int erg = 0;

        try {

            String sql1 = "DELETE FROM " + dbBundle.getSchemaMandant()
                    + "tbl_abstimmungenZuStimmkarte where mandant=?;";
            PreparedStatement pstm1 = verbindung.prepareStatement(sql1);
            pstm1.setInt(1, dbBundle.clGlobalVar.mandant);

            erg = 0; /*Falls Duplicate Key, dann wird exception geschmissen und erg wohl nicht gefüllt!*/
            erg = executeUpdate(pstm1);

            pstm1.close();
        } catch (Exception e2) {
            CaBug.drucke("DbAbstimmungenZuStimmkarte.deleteAll 001");
            System.err.println(" " + e2.getMessage());
            return (erg);
        }

        return 1;
    }

    /**************************setzt aktuelle Mandantennummer bei allen Datensätzen****************/
    public int updateMandant() {
        return dbBundle.dbLowLevel.rawUpdateMandant(dbBundle.getSchemaMandant() + "tbl_abstimmungenZuStimmkarte");
    }

    /********** dekodiert die aktuelle Position aus ergebnis  und gibt dieses zurück******/
    EclAbstimmungenZuStimmkarte decodeErgebnis(ResultSet pErgebnis) {

        EclAbstimmungenZuStimmkarte lAbstimmungenZuStimmkarte = new EclAbstimmungenZuStimmkarte();

        try {
            lAbstimmungenZuStimmkarte.mandant = pErgebnis.getInt("abZski.mandant");
            lAbstimmungenZuStimmkarte.stimmkartenNr = pErgebnis.getInt("abZski.stimmkartenNr");
            lAbstimmungenZuStimmkarte.db_version = pErgebnis.getLong("abZski.db_version");

            lAbstimmungenZuStimmkarte.identAbstimmung = pErgebnis.getInt("abZski.identAbstimmung");
            lAbstimmungenZuStimmkarte.identAbstimmungAufKarte = pErgebnis.getInt("abZski.identAbstimmungAufKarte");
            lAbstimmungenZuStimmkarte.positionInStimmkarte = pErgebnis.getInt("abZski.positionInStimmkarte");

        } catch (Exception e) {
            CaBug.drucke("DbAbstimmungenZuStimmkarte.decodeErgebnis 001");
            System.err.println(" " + e.getMessage());
        }

        return lAbstimmungenZuStimmkarte;
    }

    /********************* Fuellen Prepared Statement mit allen Feldern, beginnend bei offset.*****************
     * Kann sowohl für Insert, als auch für update verwendet werden.
     * 
     * offset= Startposition des ersten Feldes (also z.B. 1)
     */
    private int anzfelder = 6; /*Anpassen auf Anzahl der Felder pro Datensatz*/

    private void fuellePreparedStatementKomplett(PreparedStatement pPStm, int pOffset,
            EclAbstimmungenZuStimmkarte lAbstimmungenZuStimmkarte) {

        int startOffset = pOffset; /*Nur erforderlich zum Überprüfen von Programmierfehlern - Feldanzahl*/

        try {
            pPStm.setInt(pOffset, lAbstimmungenZuStimmkarte.mandant);
            pOffset++;
            pPStm.setInt(pOffset, lAbstimmungenZuStimmkarte.stimmkartenNr);
            pOffset++;
            pPStm.setLong(pOffset, lAbstimmungenZuStimmkarte.db_version);
            pOffset++;

            pPStm.setInt(pOffset, lAbstimmungenZuStimmkarte.identAbstimmung);
            pOffset++;
            pPStm.setInt(pOffset, lAbstimmungenZuStimmkarte.identAbstimmungAufKarte);
            pOffset++;
            pPStm.setInt(pOffset, lAbstimmungenZuStimmkarte.positionInStimmkarte);
            pOffset++;

            if (pOffset - startOffset != anzfelder) {
                /*Nur wg. Überprüfung auf Programmierfehler - alle Felder berücksichtigt?*/
                CaBug.drucke("DbAbstimmungenZuStimmkarte.fuellePreparedStatementKomplett 002");
            }

        } catch (SQLException e) {
            CaBug.drucke("DbAbstimmungenZuStimmkarte.fuellePreparedStatementKomplett 001");
            e.printStackTrace();
        }

    }

    /**Insert
     * 
     * Feld mandant wird von dieser Funktion immer selbstständig belegt.
     * 
     * Returnwert:
     * =1 => Insert erfolgreich
     * ansonsten: Fehler
     */
    public int insert(EclAbstimmungenZuStimmkarte pAbstimmungenZuStimmkarte) {

        int erg = 0;

        /* Start Transaktion */
        dbBasis.beginTransaction();

        pAbstimmungenZuStimmkarte.mandant = dbBundle.clGlobalVar.mandant;

        try {

            /*Felder Neuanlage füllen*/
            String lSql = "INSERT INTO " + dbBundle.getSchemaMandant() + "tbl_abstimmungenZuStimmkarte " + "("
                    + "mandant, stimmkartenNr, db_version, "
                    + "identAbstimmung, identAbstimmungAufKarte, positionInStimmkarte " + ")" + "VALUES (" + "?, ?, ?, "
                    + "?, ?, ? " + ")";
            PreparedStatement lPStm = verbindung.prepareStatement(lSql, ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);

            fuellePreparedStatementKomplett(lPStm, 1, pAbstimmungenZuStimmkarte);

            erg = executeUpdate(lPStm);
            lPStm.close();
        } catch (Exception e2) {
            CaBug.drucke("DbAbstimmungenZuStimmkarte.insert 001");
            System.err.println(" " + e2.getMessage());
        }

        if (erg == 0) {/*Fehler beim Einfügen - d.h. primaryKey bereits vorhanden*/
            dbBasis.rollbackTransaction();
            dbBasis.endTransaction();
            return (-1);
        }

        /* Ende Transaktion */
        dbBasis.endTransaction();
        return (1);
    }

    public int readAll() {
        int anzInArray = 0;
        PreparedStatement lPStm = null;

        try {
            String lSql = "SELECT abZski.* from " + dbBundle.getSchemaMandant()
                    + "tbl_abstimmungenZuStimmkarte abZski where "
                    + "abZski.mandant=? ORDER BY abZski.positionInStimmkarte;";
            lPStm = verbindung.prepareStatement(lSql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            lPStm.setInt(1, dbBundle.clGlobalVar.mandant);

            ResultSet lErgebnis = executeQuery(lPStm);
            lErgebnis.last();
            anzInArray = lErgebnis.getRow();
            lErgebnis.beforeFirst();

            ergebnisArray = new EclAbstimmungenZuStimmkarte[anzInArray];

            int i = 0;
            while (lErgebnis.next() == true) {
                ergebnisArray[i] = this.decodeErgebnis(lErgebnis);
                i++;
            }
            lErgebnis.close();
            lPStm.close();

        } catch (Exception e) {
            CaBug.drucke("DbAbstimmungenZuStimmkarte.readZuStimmkartenNr 003");
            System.err.println(" " + e.getMessage());
            return (-1);
        }
        return (anzInArray);
    }

    public int readZuStimmkartenNr(int pStimmkartenNr) {
        int anzInArray = 0;
        PreparedStatement lPStm = null;

        try {
            String lSql = "SELECT abZski.* from " + dbBundle.getSchemaMandant()
                    + "tbl_abstimmungenZuStimmkarte abZski where " + "abZski.mandant=? AND "
                    + "abZski.stimmkartenNr=? ORDER BY abZski.positionInStimmkarte;";
            lPStm = verbindung.prepareStatement(lSql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            lPStm.setInt(1, dbBundle.clGlobalVar.mandant);
            lPStm.setInt(2, pStimmkartenNr);

            ResultSet lErgebnis = executeQuery(lPStm);
            lErgebnis.last();
            anzInArray = lErgebnis.getRow();
            lErgebnis.beforeFirst();

            ergebnisArray = new EclAbstimmungenZuStimmkarte[anzInArray];

            int i = 0;
            while (lErgebnis.next() == true) {
                ergebnisArray[i] = this.decodeErgebnis(lErgebnis);
                i++;
            }
            lErgebnis.close();
            lPStm.close();

        } catch (Exception e) {
            CaBug.drucke("DbAbstimmungenZuStimmkarte.readZuStimmkartenNr 003");
            System.err.println(" " + e.getMessage());
            return (-1);
        }
        return (anzInArray);
    }

    public int readZuIdentAbstimmung(int pIdentAbstimmung) {
        int anzInArray = 0;
        PreparedStatement lPStm = null;

        try {
            String lSql = "SELECT abZski.* from " + dbBundle.getSchemaMandant()
                    + "tbl_abstimmungenZuStimmkarte abZski where " + "abZski.mandant=? AND "
                    + "abZski.identAbstimmung=? ORDER BY abZski.stimmkartenNr;";
            lPStm = verbindung.prepareStatement(lSql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            lPStm.setInt(1, dbBundle.clGlobalVar.mandant);
            lPStm.setInt(2, pIdentAbstimmung);

            ResultSet lErgebnis = executeQuery(lPStm);
            lErgebnis.last();
            anzInArray = lErgebnis.getRow();
            lErgebnis.beforeFirst();

            ergebnisArray = new EclAbstimmungenZuStimmkarte[anzInArray];

            int i = 0;
            while (lErgebnis.next() == true) {
                ergebnisArray[i] = this.decodeErgebnis(lErgebnis);
                i++;
            }
            lErgebnis.close();
            lPStm.close();

        } catch (Exception e) {
            CaBug.drucke("DbAbstimmungenZuStimmkarte.readZuIdentAbstimmung 003");
            System.err.println(" " + e.getMessage());
            return (-1);
        }
        return (anzInArray);
    }

    /**Update. Versionsnummer wird um 1 hochgezählt
     * 
     * Feld mandant wird von dieser Funktion immer selbstständig belegt.
     * 
     * Zum Sicherstellen der Multiuserfähigkeit muß in jedem Fall der rc CaFehler.pfXyWurdeVonAnderemBenutzerVeraendert
     * nach Aufruf dieser Funktion abgefangen werden.
     * Ansonsten: rc=1 => ok, ansonsten Fehler
     */
    public int update(EclAbstimmungenZuStimmkarte pAbstimmungenZuStimmkarte) {

        pAbstimmungenZuStimmkarte.db_version++;

        try {

            String lSql = "UPDATE " + dbBundle.getSchemaMandant() + "tbl_abstimmungenZuStimmkarte SET "
                    + "mandant=?, stimmkartenNr=?, db_version=?, "
                    + "identAbstimmung=?, identAbstimmungAufKarte=?, positionInStimmkarte=? " + "WHERE "
                    + "stimmkartenNr=? AND " + "db_version=? AND identAbstimmungAufKarte=? AND mandant=?";

            PreparedStatement lPStm = verbindung.prepareStatement(lSql);
            fuellePreparedStatementKomplett(lPStm, 1, pAbstimmungenZuStimmkarte);
            lPStm.setInt(anzfelder + 1, pAbstimmungenZuStimmkarte.stimmkartenNr);
            lPStm.setLong(anzfelder + 2, pAbstimmungenZuStimmkarte.db_version - 1);
            lPStm.setLong(anzfelder + 3, pAbstimmungenZuStimmkarte.identAbstimmungAufKarte);
            lPStm.setLong(anzfelder + 4, dbBundle.clGlobalVar.mandant);

            int ergebnis = executeUpdate(lPStm);
            lPStm.close();
            if (ergebnis == 0) {
                return (CaFehler.pfXyWurdeVonAnderemBenutzerVeraendert);
            }
        } catch (Exception e1) {
            CaBug.drucke("DbAbstimmungenZuStimmkarte.update 001");
            System.err.println(" " + e1.getMessage());
            return (CaFehler.pfdXyBereitsVorhanden);
        }

        return (1);
    }

    public int delete(int pStimmkarte, int pIdentAbstimmungAufKarte) {

        try {

            String sql = "DELETE FROM " + dbBundle.getSchemaMandant()
                    + "tbl_abstimmungenZuStimmkarte WHERE stimmkartenNr=? AND identAbstimmungAufKarte=? AND mandant=? ";

            PreparedStatement pstm1 = verbindung.prepareStatement(sql);
            pstm1.setInt(1, pStimmkarte);
            pstm1.setInt(2, pIdentAbstimmungAufKarte);
            pstm1.setInt(3, dbBundle.clGlobalVar.mandant);

            int ergebnis1 = executeUpdate(pstm1);
            pstm1.close();
            if (ergebnis1 == 0) {
                return (CaFehler.pfXyWurdeVonAnderemBenutzerVeraendert);
            }
        } catch (Exception e1) {
            CaBug.drucke("DbAbstimmungZuAbstimmungsblock.delete 001");
            System.err.println(" " + e1.getMessage());
            return (CaFehler.pfdXyBereitsVorhanden);
        }

        return (1);
    }

}
