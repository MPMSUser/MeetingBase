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
import de.meetingapps.meetingportal.meetComEntities.EclEindeutigerKey;

public class DbEindeutigerKey  extends DbRootExecute {

    private Connection verbindung = null;
    private DbBasis dbBasis = null;
    private DbBundle dbBundle = null;

    private EclEindeutigerKey ergebnisArray[] = null;

    /*************************Initialisierung***************************/
    public DbEindeutigerKey(DbBundle pDbBundle) {
        /* Verbindung in lokale Daten eintragen*/
        if (pDbBundle == null) {
            CaBug.drucke("DbEindeutigerKey.init 001 - dbBundle nicht initialisiert");
            return;
        }
        if (pDbBundle.dbBasis == null) {
            CaBug.drucke("DbEindeutigerKey.init 002 - dbBasis nicht initialisiert");
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

    /**********Liefert pN-tes Element des Ergebnisses der read*Methoden**************
     * pN geht von 0 bis anzErgebnis-1*/
    public EclEindeutigerKey ergebnisPosition(int pN) {
        if (ergebnisArray == null) {
            CaBug.drucke("DbEindeutigerKey.ergebnisPosition 001");
            return null;
        }
        if (pN < 0) {
            CaBug.drucke("DbEindeutigerKey.ergebnisPosition 002");
            return null;
        }
        if (pN >= ergebnisArray.length) {
            CaBug.drucke("DbEindeutigerKey.ergebnisPosition 003");
            return null;
        }
        return ergebnisArray[pN];
    }

    public int createTable() {
        int rc = 0;
        DbLowLevel lDbLowLevel = new DbLowLevel(dbBundle);
//      @formatter:off
        rc = lDbLowLevel.createTable("CREATE TABLE " 
                + dbBundle.getSchemaAllgemein() + "tbl_eindeutigerkey ( "
                + "`ident` int(11) DEFAULT NULL, " 
                + "`eindeutigerKey` varchar(20) NOT NULL, "
                + "`lfd` int(11) DEFAULT NULL, " 
                + "PRIMARY KEY (`eindeutigerKey`), " 
                + "KEY `x_ident` (`ident`) "
                + ") ");
//      @formatter:on
        return rc;
    }

    /**************************deleteAll Löschen aller Datensätze ****************/
    public int deleteAll() {
        int erg = 0;

        try {

            String sql1 = "DELETE FROM " + dbBundle.getSchemaAllgemein() + "tbl_eindeutigerKey;";
            PreparedStatement pstm1 = verbindung.prepareStatement(sql1);
            pstm1.setInt(1, dbBundle.clGlobalVar.mandant);

            erg = 0; /*Falls Duplicate Key, dann wird exception geschmissen und erg wohl nicht gefüllt!*/
            erg = executeUpdate(pstm1);
            pstm1.close();

        } catch (Exception e2) {
            CaBug.drucke("DbEindeutigerKey.deleteAll 001");
            System.err.println(" " + e2.getMessage());
            return (erg);
        }

        return 1;
    }

    /********** dekodiert die aktuelle Position aus ergebnis in EclAktienregisterEintrag und gibt dieses zurück******/
    private EclEindeutigerKey decodeErgebnis(ResultSet pErgebnis) {

        EclEindeutigerKey lEindeutigerKey = new EclEindeutigerKey();

        try {

            lEindeutigerKey.ident = pErgebnis.getInt("ident");
            lEindeutigerKey.eindeutigerKey = pErgebnis.getString("eindeutigerKey");
            lEindeutigerKey.lfd = pErgebnis.getInt("lfd");
        } catch (Exception e) {
            CaBug.drucke("DbEindeutigerKey.decodeErgebnis 001");
            System.err.println(" " + e.getMessage());
        }

        return lEindeutigerKey;
    }

    /********************* Fuellen Prepared Statement mit allen Feldern, beginnend bei offset.*****************
     * Kann sowohl für Insert, als auch für update verwendet werden.
     * 
     * offset= Startposition des ersten Feldes (also z.B. 1)
     */
    private int anzfelder = 3; /*Anpassen auf Anzahl der Felder pro Datensatz*/

    private void fuellePreparedStatementKomplett(PreparedStatement pPStm, int pOffset,
            EclEindeutigerKey pEindeutigerKey) {

        int startOffset = pOffset; /*Nur erforderlich zum Überprüfen von Programmierfehlern - Feldanzahl*/

        try {
            pPStm.setInt(pOffset, pEindeutigerKey.ident);
            pOffset++;
            pPStm.setString(pOffset, pEindeutigerKey.eindeutigerKey);
            pOffset++;
            pPStm.setInt(pOffset, pEindeutigerKey.lfd);
            pOffset++;

            if (pOffset - startOffset != anzfelder) {
                /*Nur wg. Überprüfung auf Programmierfehler - alle Felder berücksichtigt?*/
                CaBug.drucke("DbEindeutigerKey.fuellePreparedStatementKomplett 002");
            }

        } catch (SQLException e) {
            CaBug.drucke("DbEindeutigerKey.fuellePreparedStatementKomplett 001");
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
    public int insert(EclEindeutigerKey pEindeutigerKey) {

        int erg = 0;
        int anzbisher = 0;
        dbBasis.beginTransaction();

        this.read(-1);
        if (this.anzErgebnis() == 0) {
            try {
                String lSql = "INSERT INTO " + dbBundle.getSchemaAllgemein() + "tbl_eindeutigerKey "
                        + "(ident, eindeutigerKey, lfd)" + "VALUES (" + "-1, '-1', 0 )";
                PreparedStatement lPStm;
                lPStm = verbindung.prepareStatement(lSql, ResultSet.TYPE_SCROLL_INSENSITIVE,
                        ResultSet.CONCUR_READ_ONLY);
                erg = executeUpdate(lPStm);

                lSql = "INSERT INTO " + dbBundle.getSchemaAllgemein() + "tbl_eindeutigerKey "
                        + "(ident, eindeutigerKey, lfd)" + "VALUES (" + "0, '0', 0 )";
                lPStm = verbindung.prepareStatement(lSql, ResultSet.TYPE_SCROLL_INSENSITIVE,
                        ResultSet.CONCUR_READ_ONLY);
                erg = executeUpdate(lPStm);

                lPStm.close();

            } catch (SQLException e) {
                e.printStackTrace();
            }
            anzbisher = 0;
        } else {
            anzbisher = ergebnisArray[0].lfd;
        }

        anzbisher++;
        pEindeutigerKey.ident = anzbisher;
        try {

            /*Felder Neuanlage füllen*/
            String lSql = "INSERT INTO " + dbBundle.getSchemaAllgemein() + "tbl_eindeutigerKey " + "("
                    + "ident, eindeutigerKey, lfd" + ")" + "VALUES (" + "?, ?, ? " + ")";
            PreparedStatement lPStm = verbindung.prepareStatement(lSql, ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);

            fuellePreparedStatementKomplett(lPStm, 1, pEindeutigerKey);

            erg = executeUpdate(lPStm);
            lPStm.close();

        } catch (Exception e2) {
            CaBug.drucke("DbEindeutigerKey.insert 001");
            System.err.println(" " + e2.getMessage());
        }

        if (erg == 0) {/*Fehler beim Einfügen - d.h. primaryKey bereits vorhanden*/
            dbBasis.rollbackTransaction();
            dbBasis.endTransaction();
            return (-1);
        }

        /* Ende Transaktion */
        EclEindeutigerKey lEindeutigerKey = new EclEindeutigerKey();
        lEindeutigerKey.ident = -1;
        lEindeutigerKey.lfd = anzbisher;
        lEindeutigerKey.eindeutigerKey = "-1";
        this.update(lEindeutigerKey);
        dbBasis.endTransaction();
        return (1);
    }

    /**Überprüfen, ob insert möglich ist (intern: es wird ein read-Befehl abgesetzt), oder mittlerweile bereits ein 
     * Satz vorhanden ist (dient dem Multiuser-Betrieb ...)
     * Gefüllt sein muss: der Primary Key (in diesem Fall: aktienregisterIdent; mandant wird automatisch gefüllt)
     * 
     * true=insert ist noch möglich
     * false=mittlerweile existiert ein Satz mit diesem Primary Key, oder es sit ein Fehler aufgetreten
     * */
    public boolean insertCheck(EclEindeutigerKey pEindeutigerKey) {
        int anzInArray = 0;
        PreparedStatement lPStm = null;

        if (pEindeutigerKey == null) {
            CaBug.drucke("DbEindeutigerKey.insertCheck 001");
            return false;
        }

        try {
            String lSql = "SELECT * from " + dbBundle.getSchemaAllgemein() + "tbl_eindeutigerKey where "
                    + "eindeutigerKey=? ORDER BY eindeutigerKey;";
            lPStm = verbindung.prepareStatement(lSql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            lPStm.setString(1, pEindeutigerKey.eindeutigerKey);

            ResultSet lErgebnis = executeQuery(lPStm);
            lErgebnis.last();
            anzInArray = lErgebnis.getRow();
            lErgebnis.beforeFirst();
            lErgebnis.close();
            lPStm.close();

            if (anzInArray > 0) {
                return false;
            } else {
                return true;
            }

        } catch (Exception e) {
            CaBug.drucke("DbEindeutigerKey.insertCheck 003");
            System.err.println(" " + e.getMessage());
            return (false);
        }

    }

    public String getNextFree() {
        this.read(0);
        EclEindeutigerKey lEindeutigerKey = this.ergebnisPosition(0);
        int neueID = lEindeutigerKey.lfd + 1;
        lEindeutigerKey.lfd = neueID;
        this.update(lEindeutigerKey);

        this.read(neueID);
        return (this.ergebnisPosition(0).eindeutigerKey.toUpperCase());
    }

    public int read(int ident) {
        int anzInArray = 0;
        PreparedStatement lPStm = null;

        try {
            String lSql = "SELECT * from " + dbBundle.getSchemaAllgemein() + "tbl_eindeutigerKey where "
                    + "ident=? ORDER BY ident;";
            lPStm = verbindung.prepareStatement(lSql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            lPStm.setInt(1, ident);

            ResultSet lErgebnis = executeQuery(lPStm);
            lErgebnis.last();
            anzInArray = lErgebnis.getRow();
            lErgebnis.beforeFirst();

            ergebnisArray = new EclEindeutigerKey[anzInArray];

            int i = 0;
            while (lErgebnis.next() == true) {

                ergebnisArray[i] = this.decodeErgebnis(lErgebnis);
                i++;
            }

            lErgebnis.close();
            lPStm.close();

        } catch (Exception e) {
            CaBug.drucke("DbEindeutigerKey.read 003");
            System.err.println(" " + e.getMessage());
            return (-1);
        }
        return (anzInArray);
    }

    public int update(EclEindeutigerKey pEindeutigerKey) {

        try {

            String lSql = "UPDATE " + dbBundle.getSchemaAllgemein() + "tbl_eindeutigerKey SET "
                    + "ident=?, eindeutigerKey=?, lfd=? " + "WHERE " + "ident=?";

            PreparedStatement lPStm = verbindung.prepareStatement(lSql);
            fuellePreparedStatementKomplett(lPStm, 1, pEindeutigerKey);
            lPStm.setInt(anzfelder + 1, pEindeutigerKey.ident);

            int ergebnis = executeUpdate(lPStm);

            lPStm.close();
            if (ergebnis == 0) {
                return (CaFehler.pfXyWurdeVonAnderemBenutzerVeraendert);
            }
        } catch (Exception e1) {
            CaBug.drucke("DbEindeutigerKey.update 001");
            System.err.println(" " + e1.getMessage());
            return (CaFehler.pfdXyBereitsVorhanden);
        }

        return (1);
    }

}
