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
import de.meetingapps.meetingportal.meetComEntities.EclSuchlaufBegriffe;

public class DbSuchlaufBegriffe {

    private Connection verbindung = null;
    private DbBasis dbBasis = null;
    private DbBundle dbBundle = null;

    private EclSuchlaufBegriffe ergebnisArray[] = null;

    /*************************Initialisierung***************************/
    public DbSuchlaufBegriffe(DbBundle pDbBundle) {
        /* Verbindung in lokale Daten eintragen*/
        if (pDbBundle == null) {
            CaBug.drucke("DbSuchlaufBegriffe.init 001 - dbBundle nicht initialisiert");
            return;
        }
        if (pDbBundle.dbBasis == null) {
            CaBug.drucke("DbSuchlaufBegriffe.init 002 - dbBasis nicht initialisiert");
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
    public EclSuchlaufBegriffe[] ergebnis() {
        return ergebnisArray;
    }

    /**********Liefert pN-tes Element des Ergebnisses der read*Methoden**************
     * pN geht von 0 bis anzErgebnis-1*/
    public EclSuchlaufBegriffe ergebnisPosition(int pN) {
        if (ergebnisArray == null) {
            CaBug.drucke("DbSuchlaufBegriffe.ergebnisPosition 001");
            return null;
        }
        if (pN < 0) {
            CaBug.drucke("DbSuchlaufBegriffe.ergebnisPosition 002");
            return null;
        }
        if (pN >= ergebnisArray.length) {
            CaBug.drucke("DbSuchlaufBegriffe.ergebnisPosition 003");
            return null;
        }
        return ergebnisArray[pN];
    }

    private String liefereSchema(boolean global) {
        if (global) {
            return dbBundle.getSchemaAllgemein();
        }
        return dbBundle.getSchemaMandant();
    }

    public int createTable(boolean global) {
        int rc = 0;
        DbLowLevel lDbLowLevel = new DbLowLevel(dbBundle);
        rc = lDbLowLevel.createTable(
                "CREATE TABLE " + liefereSchema(global) + "tbl_suchlaufBegriffe ( " + "`mandant` int(11) NOT NULL, "
                        + "`ident` int(11) NOT NULL, " + "`bezeichnung` varchar(200) DEFAULT NULL, "
                        + "`suchbegriffe` varchar(800) DEFAULT NULL, " + "PRIMARY KEY (`mandant`,`ident`) " + ") ");
        return rc;
    }

    /**************************deleteAll Löschen aller Datensätze  eines Mandanten****************/
    public int deleteAll(boolean global) {
        if (global) {
            dbBundle.dbBasis.resetInterneIdentSuchlaufBegriffeOhneMandant();
            return dbBundle.dbLowLevel
                    .deleteAlle("DELETE FROM " + dbBundle.getSchemaAllgemein() + "tbl_suchlaufBegriffe;");
        } else {
            dbBundle.dbBasis.resetInterneIdentSuchlaufBegriffeMitMandant();
            return dbBundle.dbLowLevel.deleteMandant(
                    "DELETE FROM " + dbBundle.getSchemaMandant() + "tbl_suchlaufBegriffe where mandant=?");
        }
    }

    /**************************setzt aktuelle Mandantennummer bei allen Datensätzen****************/
    public int updateMandant() {
        return dbBundle.dbLowLevel.rawUpdateMandant(dbBundle.getSchemaMandant() + "tbl_suchlaufBegriffe");
    }

    public void reorgInterneIdent(boolean global) {
        if (global) {
            int lMax = dbBundle.dbLowLevel.liefereHoechsteIdentOhneMandant(
                    "SELECT MAX(ident) FROM " + dbBundle.getSchemaAllgemein() + "tbl_suchlaufBegriffe;");
            if (lMax != -1) {
                dbBundle.dbBasis.resetInterneIdentSuchlaufBegriffeOhneMandant(lMax);
            }
        } else {
            int lMax = dbBundle.dbLowLevel.liefereHoechsteIdent(
                    "SELECT MAX(ident) FROM " + dbBundle.getSchemaMandant() + "tbl_suchlaufBegriffe where mandant=?;");
            if (lMax != -1) {
                dbBundle.dbBasis.resetInterneIdentSuchlaufBegriffeMitMandant(lMax);
            }
        }
    }

    /********** dekodiert die aktuelle Position aus ergebnis  und gibt dieses zurück******/
    EclSuchlaufBegriffe decodeErgebnis(ResultSet pErgebnis) {

        EclSuchlaufBegriffe lEclReturn = new EclSuchlaufBegriffe();

        try {
            lEclReturn.mandant = pErgebnis.getInt("mandant");
            lEclReturn.ident = pErgebnis.getInt("ident");

            lEclReturn.bezeichnung = pErgebnis.getString("bezeichnung");
            lEclReturn.suchbegriffe = pErgebnis.getString("suchbegriffe");
        } catch (Exception e) {
            CaBug.drucke("DbSuchlaufBegriffe.decodeErgebnis 001");
            System.err.println(" " + e.getMessage());
        }

        return lEclReturn;
    }

    /********************* Fuellen Prepared Statement mit allen Feldern, beginnend bei offset.*****************
     * Kann sowohl für Insert, als auch für update verwendet werden.
     * 
     * offset= Startposition des ersten Feldes (also z.B. 1)
     */
    private int anzfelder = 4; /*Anpassen auf Anzahl der Felder pro Datensatz*/

    private void fuellePreparedStatementKomplett(PreparedStatement pPStm, int pOffset, EclSuchlaufBegriffe pEcl) {

        int startOffset = pOffset; /*Nur erforderlich zum Überprüfen von Programmierfehlern - Feldanzahl*/

        try {
            pPStm.setInt(pOffset, pEcl.mandant);
            pOffset++;
            pPStm.setInt(pOffset, pEcl.ident);
            pOffset++;

            pPStm.setString(pOffset, pEcl.bezeichnung);
            pOffset++;
            pPStm.setString(pOffset, pEcl.suchbegriffe);
            pOffset++;

            if (pOffset - startOffset != anzfelder) {
                /*Nur wg. Überprüfung auf Programmierfehler - alle Felder berücksichtigt?*/
                CaBug.drucke("DbSuchlaufBegriffe.fuellePreparedStatementKomplett 002");
            }

        } catch (SQLException e) {
            CaBug.drucke("DbSuchlaufBegriffe.fuellePreparedStatementKomplett 001");
            e.printStackTrace();
        }

    }

    /**Insert
     * 
     * Returnwert:
     * =1 => Insert erfolgreich
     * ansonsten: Fehler
     */
    public int insert(boolean global, EclSuchlaufBegriffe pEcl) {

        int erg = 0;
        dbBasis.beginTransaction();

        /* neue InterneIdent vergeben */
        if (global) {
            erg = dbBasis.getInterneIdentSuchlaufBegriffeOhneMandant();
        } else {
            erg = dbBasis.getInterneIdentSuchlaufBegriffeMitMandant();
        }
        if (erg < 1) {
            CaBug.drucke("DbSuchlaufBegriffe.insert 002");
            dbBasis.endTransaction();
            return (erg);
        }

        pEcl.ident = erg;

        if (global) {
            pEcl.mandant = 0 /*dbBundle.clGlobalVar.mandant*/;
        } else {
            pEcl.mandant = dbBundle.clGlobalVar.mandant;
        }

        try {

            /*Felder Neuanlage füllen*/
            String lSql = "INSERT INTO " + liefereSchema(global) + "tbl_suchlaufBegriffe " + "(" + "mandant, ident, "
                    + "bezeichnung, suchbegriffe " + ")" + "VALUES (" + "?, ?, " + "?, ? " + ")";
            PreparedStatement lPStm = verbindung.prepareStatement(lSql, ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);

            fuellePreparedStatementKomplett(lPStm, 1, pEcl);

            erg = lPStm.executeUpdate();
            lPStm.close();
        } catch (Exception e2) {
            CaBug.drucke("DbSuchlaufBegriffe.insert 001");
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

    /**Ident muss übergeben werden.
     * 
     * Return-Wert = Anzahl der gefundenen Sätze (oder Fehlermeldung <0)*/
    public int read(boolean global, int pIdent) {
        int anzInArray = 0;
        PreparedStatement lPStm = null;

        try {
            String lSql = "SELECT * from " + liefereSchema(global) + "tbl_suchlaufBegriffe where " + "mandant=? AND "
                    + "ident=? " + "ORDER BY ident;";
            lPStm = verbindung.prepareStatement(lSql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            if (global) {
                lPStm.setInt(1, 0);
            } else {
                lPStm.setInt(1, dbBundle.clGlobalVar.mandant);
            }
            lPStm.setInt(2, pIdent);

            ResultSet lErgebnis = lPStm.executeQuery();
            lErgebnis.last();
            anzInArray = lErgebnis.getRow();
            lErgebnis.beforeFirst();

            ergebnisArray = new EclSuchlaufBegriffe[anzInArray];

            int i = 0;
            while (lErgebnis.next() == true) {
                ergebnisArray[i] = this.decodeErgebnis(lErgebnis);
                i++;
            }
            lErgebnis.close();
            lPStm.close();

        } catch (Exception e) {
            CaBug.drucke("DbSuchlaufBegriffe.read 003");
            System.err.println(" " + e.getMessage());
            return (-1);
        }
        return (anzInArray);
    }

    /**Update. 
     * 
     * Returnwert:
     * pfXyWurdeVonAnderemBenutzerVeraendert
     * -1 => unbekannter Fehler
     * 1 = Update wurde durchgeführt.
     * 
     */
    public int update(boolean global, EclSuchlaufBegriffe pEcl) {

        try {

            String lSql = "UPDATE " + liefereSchema(global) + "tbl_suchlaufBegriffe SET " + "mandant=?, ident=?, "
                    + "bezeichnung=?, suchbegriffe=? " + "WHERE " + "mandant=? AND ident=? ";

            PreparedStatement lPStm = verbindung.prepareStatement(lSql);
            fuellePreparedStatementKomplett(lPStm, 1, pEcl);
            lPStm.setInt(anzfelder + 1, pEcl.mandant);
            lPStm.setInt(anzfelder + 2, pEcl.ident);

            int ergebnis = lPStm.executeUpdate();
            lPStm.close();
            if (ergebnis == 0) {
                return (CaFehler.pfXyWurdeVonAnderemBenutzerVeraendert);
            }
        } catch (Exception e1) {
            CaBug.drucke("DbSuchlaufBegriffe.update 001");
            System.err.println(" " + e1.getMessage());
            return (-1);
        }

        return (1);
    }

    /**Return-Werte:
     * pfXyWurdeVonAnderemBenutzerVeraendert
     * -1 => undefinierter Fehler
     * 1 => Löschen erfolgreich
     */
    public int delete(boolean global, int pIdent) {
        try {

            String sql = "DELETE FROM " + liefereSchema(global) + "tbl_suchlaufBegriffe WHERE ident=? AND mandant=? ";

            PreparedStatement pstm1 = verbindung.prepareStatement(sql);
            pstm1.setInt(1, pIdent);
            if (global) {
                pstm1.setInt(2, 0 /*dbBundle.clGlobalVar.mandant*/);
            } else {
                pstm1.setInt(2, dbBundle.clGlobalVar.mandant);
            }

            int ergebnis1 = pstm1.executeUpdate();
            pstm1.close();
            if (ergebnis1 == 0) {
                return (CaFehler.pfXyWurdeVonAnderemBenutzerVeraendert);
            }
        } catch (Exception e1) {
            CaBug.drucke("DbSuchlaufBegriffe.delete 001");
            System.err.println(" " + e1.getMessage());
            return (-1);
        }

        return (1);
    }

}
