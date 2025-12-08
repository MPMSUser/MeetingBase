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
import de.meetingapps.meetingportal.meetComEntities.EclStimmkarteInhalt;

public class DbStimmkarteInhalt {

    private Connection verbindung = null;
    private DbBasis dbBasis = null;
    private DbBundle dbBundle = null;

    private EclStimmkarteInhalt ergebnisArray[] = null;

    /*************************Initialisierung***************************/
    public DbStimmkarteInhalt(DbBundle pDbBundle) {
        /* Verbindung in lokale Daten eintragen*/
        if (pDbBundle == null) {
            CaBug.drucke("DbStimmkarteInhalt.init 001 - dbBundle nicht initialisiert");
            return;
        }
        if (pDbBundle.dbBasis == null) {
            CaBug.drucke("DbStimmkarteInhalt.init 002 - dbBasis nicht initialisiert");
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
    public EclStimmkarteInhalt[] ergebnis() {
        return ergebnisArray;
    }

    public int createTable() {
        int rc = 0;
        DbLowLevel lDbLowLevel = new DbLowLevel(dbBundle);
        rc = lDbLowLevel.createTable("CREATE TABLE " + dbBundle.getSchemaMandant() + "tbl_stimmkarteinhalt ( "
                + "`mandant` int(11) NOT NULL, " + "`stimmkartenNr` int(11) NOT NULL, "
                + "`posInBlock` int(11) DEFAULT NULL, " + "`db_version` bigint(20) DEFAULT NULL, "
                + "`kurzBezeichnung` varchar(40) DEFAULT NULL, " + "`stimmkartenBezeichnung` varchar(80) DEFAULT NULL, "
                + "`stimmkartenBezeichnungEN` varchar(80) DEFAULT NULL, "
                + "`stimmkarteIstAktiv` int(11) DEFAULT NULL, " + "PRIMARY KEY (`stimmkartenNr`,`mandant`) " + ") ");
        return rc;
    }

    /**********Liefert pN-tes Element des Ergebnisses der read*Methoden**************
     * pN geht von 0 bis anzErgebnis-1*/
    public EclStimmkarteInhalt ergebnisPosition(int pN) {
        if (ergebnisArray == null) {
            CaBug.drucke("DbStimmkarteInhalt.ergebnisPosition 001");
            return null;
        }
        if (pN < 0) {
            CaBug.drucke("DbStimmkarteInhalt.ergebnisPosition 002");
            return null;
        }
        if (pN >= ergebnisArray.length) {
            CaBug.drucke("DbStimmkarteInhalt.ergebnisPosition 003");
            return null;
        }
        return ergebnisArray[pN];
    }

    /**************************deleteAll Löschen aller Datensätze  eines Mandanten****************/
    public int deleteAll() {
        return dbBundle.dbLowLevel
                .deleteMandant("DELETE FROM " + dbBundle.getSchemaMandant() + "tbl_stimmkarteInhalt where mandant=?;");
    }

    /**************************setzt aktuelle Mandantennummer bei allen Datensätzen****************/
    public int updateMandant() {
        return dbBundle.dbLowLevel.rawUpdateMandant(dbBundle.getSchemaMandant() + "tbl_stimmkarteInhalt");
    }

    /********** dekodiert die aktuelle Position aus ergebnis  und gibt dieses zurück******/
    private EclStimmkarteInhalt decodeErgebnis(ResultSet pErgebnis) {

        EclStimmkarteInhalt lStimmkarteInhalt = new EclStimmkarteInhalt();

        try {
            lStimmkarteInhalt.mandant = pErgebnis.getInt("mandant");
            lStimmkarteInhalt.stimmkartenNr = pErgebnis.getInt("stimmkartenNr");
            lStimmkarteInhalt.posInBlock = pErgebnis.getInt("posInBlock");
            lStimmkarteInhalt.db_version = pErgebnis.getLong("db_version");

            lStimmkarteInhalt.kurzBezeichnung = pErgebnis.getString("kurzBezeichnung");
            lStimmkarteInhalt.stimmkartenBezeichnung = pErgebnis.getString("stimmkartenBezeichnung");
            lStimmkarteInhalt.stimmkartenBezeichnungEN = pErgebnis.getString("stimmkartenBezeichnungEN");
            lStimmkarteInhalt.stimmkarteIstAktiv = pErgebnis.getInt("stimmkarteIstAktiv");

        } catch (Exception e) {
            CaBug.drucke("DbStimmkarteInhalt.decodeErgebnis 001");
            System.err.println(" " + e.getMessage());
        }

        return lStimmkarteInhalt;
    }

    /********************* Fuellen Prepared Statement mit allen Feldern, beginnend bei offset.*****************
     * Kann sowohl für Insert, als auch für update verwendet werden.
     * 
     * offset= Startposition des ersten Feldes (also z.B. 1)
     */
    private int anzfelder = 8; /*Anpassen auf Anzahl der Felder pro Datensatz*/

    private void fuellePreparedStatementKomplett(PreparedStatement pPStm, int pOffset,
            EclStimmkarteInhalt pStimmkarteInhalt) {

        int startOffset = pOffset; /*Nur erforderlich zum Überprüfen von Programmierfehlern - Feldanzahl*/

        try {
            pPStm.setInt(pOffset, pStimmkarteInhalt.mandant);
            pOffset++;
            pPStm.setInt(pOffset, pStimmkarteInhalt.stimmkartenNr);
            pOffset++;
            pPStm.setInt(pOffset, pStimmkarteInhalt.posInBlock);
            pOffset++;
            pPStm.setLong(pOffset, pStimmkarteInhalt.db_version);
            pOffset++;

            pPStm.setString(pOffset, pStimmkarteInhalt.kurzBezeichnung);
            pOffset++;
            pPStm.setString(pOffset, pStimmkarteInhalt.stimmkartenBezeichnung);
            pOffset++;
            pPStm.setString(pOffset, pStimmkarteInhalt.stimmkartenBezeichnungEN);
            pOffset++;
            pPStm.setInt(pOffset, pStimmkarteInhalt.stimmkarteIstAktiv);
            pOffset++;

            if (pOffset - startOffset != anzfelder) {
                /*Nur wg. Überprüfung auf Programmierfehler - alle Felder berücksichtigt?*/
                CaBug.drucke("DbStimmkarteInhalt.fuellePreparedStatementKomplett 002");
            }

        } catch (SQLException e) {
            CaBug.drucke("DbStimmkarteInhalt.fuellePreparedStatementKomplett 001");
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
    public int insert(EclStimmkarteInhalt pStimmkarteInhalt) {

        int erg = 0;

        /* Start Transaktion */
        dbBasis.beginTransaction();

        pStimmkarteInhalt.mandant = dbBundle.clGlobalVar.mandant;

        try {

            /*Felder Neuanlage füllen*/
            String lSql = "INSERT INTO " + dbBundle.getSchemaMandant() + "tbl_stimmkarteInhalt " + "("
                    + "mandant, stimmkartenNr, posInBlock, db_version, "
                    + "kurzBezeichnung, stimmkartenBezeichnung, stimmkartenBezeichnungEN, stimmkarteIstAktiv " + ") "
                    + "VALUES (" + "?, ?, ?, ?, " + "?, ?, ?, ? " + ")";
            PreparedStatement lPStm = verbindung.prepareStatement(lSql, ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);

            fuellePreparedStatementKomplett(lPStm, 1, pStimmkarteInhalt);

            erg = lPStm.executeUpdate();
            lPStm.close();
        } catch (Exception e2) {
            CaBug.drucke("DbStimmkarteInhalt.insert 001");
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

    public int read(EclStimmkarteInhalt pStimmkarteInhalt) {
        int anzInArray = 0;
        PreparedStatement lPStm = null;

        try {
            String lSql = "SELECT * from " + dbBundle.getSchemaMandant() + "tbl_stimmkarteInhalt where "
                    + "mandant=? AND " + "stimmkartenNr=? ORDER BY posInBlock;";
            lPStm = verbindung.prepareStatement(lSql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            lPStm.setInt(1, dbBundle.clGlobalVar.mandant);
            lPStm.setInt(2, pStimmkarteInhalt.stimmkartenNr);

            ResultSet lErgebnis = lPStm.executeQuery();
            lErgebnis.last();
            anzInArray = lErgebnis.getRow();
            lErgebnis.beforeFirst();

            ergebnisArray = new EclStimmkarteInhalt[anzInArray];

            int i = 0;
            while (lErgebnis.next() == true) {
                ergebnisArray[i] = this.decodeErgebnis(lErgebnis);
                i++;
            }
            lErgebnis.close();
            lPStm.close();

        } catch (Exception e) {
            CaBug.drucke("DbStimmkarteInhalt.read 003");
            System.err.println(" " + e.getMessage());
            return (-1);
        }
        return (anzInArray);
    }

    public int readAll() {
        int anzInArray = 0;
        PreparedStatement lPStm = null;

        try {
            String lSql = "SELECT * from " + dbBundle.getSchemaMandant() + "tbl_stimmkarteInhalt where "
                    + "mandant=? ORDER BY posInBlock;";
            lPStm = verbindung.prepareStatement(lSql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            lPStm.setInt(1, dbBundle.clGlobalVar.mandant);

            ResultSet lErgebnis = lPStm.executeQuery();
            lErgebnis.last();
            anzInArray = lErgebnis.getRow();
            lErgebnis.beforeFirst();

            ergebnisArray = new EclStimmkarteInhalt[anzInArray];

            int i = 0;
            while (lErgebnis.next() == true) {
                ergebnisArray[i] = this.decodeErgebnis(lErgebnis);
                i++;
            }
            lErgebnis.close();
            lPStm.close();

        } catch (Exception e) {
            CaBug.drucke("DbStimmkarteInhalt.readAll 003");
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
    public int update(EclStimmkarteInhalt pStimmkarteInhalt) {

        pStimmkarteInhalt.db_version++;

        try {

            String lSql = "UPDATE " + dbBundle.getSchemaMandant() + "tbl_stimmkarteInhalt SET "
                    + "mandant=?, stimmkartenNr=?, posInBlock=?, db_version=?, "
                    + "kurzBezeichnung=?, stimmkartenBezeichnung=?, stimmkartenBezeichnungEN=?, stimmkarteIstAktiv=? "
                    + "WHERE " + "stimmkartenNr=? AND " + "db_version=? AND mandant=?";

            PreparedStatement lPStm = verbindung.prepareStatement(lSql);
            fuellePreparedStatementKomplett(lPStm, 1, pStimmkarteInhalt);
            lPStm.setInt(anzfelder + 1, pStimmkarteInhalt.stimmkartenNr);
            lPStm.setLong(anzfelder + 2, pStimmkarteInhalt.db_version - 1);
            lPStm.setLong(anzfelder + 3, dbBundle.clGlobalVar.mandant);

            int ergebnis = lPStm.executeUpdate();
            lPStm.close();
            if (ergebnis == 0) {
                return (CaFehler.pfXyWurdeVonAnderemBenutzerVeraendert);
            }
        } catch (Exception e1) {
            CaBug.drucke("DbStimmkarteInhalt.update 001");
            System.err.println(" " + e1.getMessage());
            return (CaFehler.pfdXyBereitsVorhanden);
        }

        return (1);
    }

    public int delete(int ident) {

        try {

            String sql = "DELETE FROM " + dbBundle.getSchemaMandant()
                    + "tbl_stimmkarteInhalt WHERE stimmkartenNr=? AND mandant=? ";

            PreparedStatement pstm1 = verbindung.prepareStatement(sql);
            pstm1.setInt(1, ident);
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
