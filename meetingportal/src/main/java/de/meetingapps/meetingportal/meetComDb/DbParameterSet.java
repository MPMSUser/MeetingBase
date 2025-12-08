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
import de.meetingapps.meetingportal.meetComEntities.EclParameterSet;

public class DbParameterSet {

    private Connection verbindung = null;
    private DbBasis dbBasis = null;
    private DbBundle dbBundle = null;

    EclParameterSet ergebnisArray[] = null;

    /*************************Initialisierung***************************/
    public DbParameterSet(DbBundle pDbBundle) {
        /* Verbindung in lokale Daten eintragen*/
        if (pDbBundle == null) {
            CaBug.drucke("DbParameterSet.init 001 - dbBundle nicht initialisiert");
            return;
        }
        if (pDbBundle.dbBasis == null) {
            CaBug.drucke("DbParameterSet.init 002 - dbBasis nicht initialisiert");
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
    public EclParameterSet[] ergebnis() {
        return ergebnisArray;
    }

    /**********Liefert pN-tes Element des Ergebnisses der read*Methoden**************
     * pN geht von 0 bis anzErgebnis-1*/
    public EclParameterSet ergebnisPosition(int pN) {
        if (ergebnisArray == null) {
            CaBug.drucke("DbParameterSet.ergebnisPosition 001");
            return null;
        }
        if (pN < 0) {
            CaBug.drucke("DbParameterSet.ergebnisPosition 002");
            return null;
        }
        if (pN >= ergebnisArray.length) {
            CaBug.drucke("DbParameterSet.ergebnisPosition 003");
            return null;
        }
        return ergebnisArray[pN];
    }

    public int createTable() {
        int rc = 0;
        DbLowLevel lDbLowLevel = new DbLowLevel(dbBundle);
        rc = lDbLowLevel.createTable(
                "CREATE TABLE " + dbBundle.getSchemaAllgemein() + "tbl_parameterset ( " + "`ident` int(11) NOT NULL, "
                        + "`beschreibung` varchar(400) DEFAULT NULL, " + "`angelegtVonUserID` int(11) NOT NULL, "
                        + "`angelegtAm` char(19) DEFAULT NULL, " + "`letzteAenderungVonUserID` int(11) NOT NULL, "
                        + "`letzteAenderungAm` char(19) DEFAULT NULL, " + "PRIMARY KEY (`ident`) " + ") "

        );
        return rc;
    }

    /**************************deleteAll Löschen aller Datensätze ****************/
    public int deleteAll() {
        int erg = 0;

        try {

            String sql1 = "DELETE FROM " + dbBundle.getSchemaAllgemein() + "tbl_parameterset;";
            PreparedStatement pstm1 = verbindung.prepareStatement(sql1);

            erg = 0; /*Falls Duplicate Key, dann wird exception geschmissen und erg wohl nicht gefüllt!*/
            erg = pstm1.executeUpdate();

            pstm1.close();
        } catch (Exception e2) {
            CaBug.drucke("DbParameterSet.deleteAll 001");
            System.err.println(" " + e2.getMessage());
            return (erg);
        }

        return 1;
    }

    /********** dekodiert die aktuelle Position aus ergebnis  und gibt dieses zurück******/
    EclParameterSet decodeErgebnis(ResultSet pErgebnis) {

        EclParameterSet lAppText = new EclParameterSet();

        try {
            lAppText.ident = pErgebnis.getInt("ps.ident");
            lAppText.beschreibung = pErgebnis.getString("ps.beschreibung");
            lAppText.angelegtVonUserID = pErgebnis.getInt("ps.angelegtVonUserID");
            lAppText.angelegtAm = pErgebnis.getString("ps.angelegtAm");
            lAppText.letzteAenderungVonUserID = pErgebnis.getInt("ps.letzteAenderungVonUserID");
            lAppText.letzteAenderungAm = pErgebnis.getString("ps.letzteAenderungAm");
        } catch (Exception e) {
            CaBug.drucke("DbParameterSet.decodeErgebnis 001");
            System.err.println(" " + e.getMessage());
        }

        return lAppText;
    }

    /********************* Fuellen Prepared Statement mit allen Feldern, beginnend bei offset.*****************
     * Kann sowohl für Insert, als auch für update verwendet werden.
     * 
     * offset= Startposition des ersten Feldes (also z.B. 1)
     */
    private int anzfelder = 6; /*Anpassen auf Anzahl der Felder pro Datensatz*/

    private void fuellePreparedStatementKomplett(PreparedStatement pPStm, int pOffset, EclParameterSet pParameterSet) {

        int startOffset = pOffset; /*Nur erforderlich zum Überprüfen von Programmierfehlern - Feldanzahl*/

        try {
            pPStm.setInt(pOffset, pParameterSet.ident);
            pOffset++;
            pPStm.setString(pOffset, pParameterSet.beschreibung);
            pOffset++;
            pPStm.setInt(pOffset, pParameterSet.angelegtVonUserID);
            pOffset++;
            pPStm.setString(pOffset, pParameterSet.angelegtAm);
            pOffset++;
            pPStm.setInt(pOffset, pParameterSet.letzteAenderungVonUserID);
            pOffset++;
            pPStm.setString(pOffset, pParameterSet.letzteAenderungAm);
            pOffset++;

            if (pOffset - startOffset != anzfelder) {
                /*Nur wg. Überprüfung auf Programmierfehler - alle Felder berücksichtigt?*/
                CaBug.drucke("DbParameterSet.fuellePreparedStatementKomplett 002");
            }

        } catch (SQLException e) {
            CaBug.drucke("DbParameterSet.fuellePreparedStatementKomplett 001");
            e.printStackTrace();
        }

    }

    /**Insert
     * Returnwert:
     * =1 => Insert erfolgreich
     * ansonsten: Fehler
     */
    public int insert(EclParameterSet pParameterSet) {

        int erg = 0;
        //		System.out.println("Insert: pAppText mandant="+pAppText.mandant+" sprache="+pAppText.sprache+" seitennummer="+pAppText.seitennummer+" ident="+pAppText.ident
        //				+" lfdnummer="+pAppText.lfdNummer);

        /* Start Transaktion */
        dbBasis.beginTransaction();

        int ident = dbBasis.getInterneIdentParameterSet();
        pParameterSet.ident = ident;
        try {

            /*Felder Neuanlage füllen*/
            String lSql = "INSERT INTO " + dbBundle.getSchemaAllgemein() + "tbl_parameterset " + "("
                    + "ident, beschreibung, angelegtVonUserID, angelegtAm, letzteAenderungVonUserID, letzteAenderungAm "
                    + ")" + "VALUES (" + "?, ?, ?, ?, ?, ? " + ")";
            PreparedStatement lPStm = verbindung.prepareStatement(lSql, ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);

            fuellePreparedStatementKomplett(lPStm, 1, pParameterSet);

            erg = lPStm.executeUpdate();
            lPStm.close();
        } catch (Exception e2) {
            CaBug.drucke("DbParameterSet.insert 001");
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

    public int read(int pIdent) {
        int anzInArray = 0;
        PreparedStatement lPStm = null;

        try {
            String lSql = "SELECT ps.* from " + dbBundle.getSchemaAllgemein() + "tbl_parameterset ps where "
                    + "ident=?;";
            lPStm = verbindung.prepareStatement(lSql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            lPStm.setInt(1, pIdent);

            ResultSet lErgebnis = lPStm.executeQuery();
            lErgebnis.last();
            anzInArray = lErgebnis.getRow();
            lErgebnis.beforeFirst();

            ergebnisArray = new EclParameterSet[anzInArray];

            int i = 0;
            while (lErgebnis.next() == true) {
                ergebnisArray[i] = this.decodeErgebnis(lErgebnis);
                i++;
            }
            lErgebnis.close();
            lPStm.close();

        } catch (Exception e) {
            CaBug.drucke("DbParameterSet.read 003");
            System.err.println(" " + e.getMessage());
            return (-1);
        }
        return (anzInArray);
    }

    /**Füllt auch angelegtVonUserLoginText und letzteAenderungVonUserLoginText über Join*/
    public int readAll() {
        int anzInArray = 0;
        PreparedStatement lPStm = null;

        try {
            String lSql = "SELECT ps.*, ul1.kennung, ul2.kennung from " + dbBundle.getSchemaAllgemein()
                    + "tbl_parameterset ps " + "INNER JOIN " + dbBundle.getSchemaAllgemein()
                    + "tbl_userlogin ul1 ON ps.angelegtVonUserID=ul1.userLoginIdent " + "INNER JOIN "
                    + dbBundle.getSchemaAllgemein()
                    + "tbl_userlogin ul2 ON ps.letzteAenderungVonUserID=ul2.userLoginIdent; ";
            lPStm = verbindung.prepareStatement(lSql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);

            ResultSet lErgebnis = lPStm.executeQuery();
            lErgebnis.last();
            anzInArray = lErgebnis.getRow();
            lErgebnis.beforeFirst();

            ergebnisArray = new EclParameterSet[anzInArray];

            int i = 0;
            while (lErgebnis.next() == true) {
                ergebnisArray[i] = this.decodeErgebnis(lErgebnis);
                ergebnisArray[i].angelegtVonUserLoginText = lErgebnis.getString("ul1.kennung");
                ergebnisArray[i].letzteAenderungVonUserLoginText = lErgebnis.getString("ul2.kennung");
                i++;
            }
            lErgebnis.close();
            lPStm.close();

        } catch (Exception e) {
            CaBug.drucke("DbParameterSet.readAll 003");
            System.err.println(" " + e.getMessage());
            return (-1);
        }
        return (anzInArray);
    }

    /**Update. 
     * 
     */

    public int update(EclParameterSet pParameterSet) {
        try {

            String lSql = "UPDATE " + dbBundle.getSchemaAllgemein() + "tbl_parameterset SET "
                    + "ident=?, beschreibung=?, angelegtVonUserID=?, angelegtAm=?, letzteAenderungVonUserID=?, letzteAenderungAm=? "
                    + "WHERE " + "ident=?;";

            PreparedStatement lPStm = verbindung.prepareStatement(lSql);
            fuellePreparedStatementKomplett(lPStm, 1, pParameterSet);
            lPStm.setInt(anzfelder + 1, pParameterSet.ident);

            int ergebnis = lPStm.executeUpdate();
            lPStm.close();
            if (ergebnis == 0) {
                return (CaFehler.pfXyWurdeVonAnderemBenutzerVeraendert);
            }
        } catch (Exception e1) {
            CaBug.drucke("DbParameterSet.update 001");
            System.err.println(" " + e1.getMessage());
            return (CaFehler.pfdXyBereitsVorhanden);
        }

        return (1);
    }

    public int delete(int pIdent) {
        try {

            String sql = "DELETE FROM " + dbBundle.getSchemaAllgemein() + "tbl_parameterset WHERE ident=? ";

            PreparedStatement pstm1 = verbindung.prepareStatement(sql);
            pstm1.setInt(1, pIdent);

            int ergebnis1 = pstm1.executeUpdate();
            pstm1.close();
            if (ergebnis1 == 0) {
                return (CaFehler.pfXyWurdeVonAnderemBenutzerVeraendert);
            }
        } catch (Exception e1) {
            CaBug.drucke("DbParameterSet.delete 001");
            System.err.println(" " + e1.getMessage());
            return (CaFehler.pfdXyBereitsVorhanden);
        }

        return (1);
    }

}
