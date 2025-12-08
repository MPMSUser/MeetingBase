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
import de.meetingapps.meetingportal.meetComEntities.EclSuchlaufDefinition;

public class DbSuchlaufDefinition {

    private Connection verbindung = null;
    private DbBasis dbBasis = null;
    private DbBundle dbBundle = null;

    private EclSuchlaufDefinition ergebnisArray[] = null;

    /*************************Initialisierung***************************/
    public DbSuchlaufDefinition(DbBundle pDbBundle) {
        /* Verbindung in lokale Daten eintragen*/
        if (pDbBundle == null) {
            CaBug.drucke("DbSuchlaufDefinition.init 001 - dbBundle nicht initialisiert");
            return;
        }
        if (pDbBundle.dbBasis == null) {
            CaBug.drucke("DbSuchlaufDefinition.init 002 - dbBasis nicht initialisiert");
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
    public EclSuchlaufDefinition[] ergebnis() {
        return ergebnisArray;
    }

    /**********Liefert pN-tes Element des Ergebnisses der read*Methoden**************
     * pN geht von 0 bis anzErgebnis-1*/
    public EclSuchlaufDefinition ergebnisPosition(int pN) {
        if (ergebnisArray == null) {
            CaBug.drucke("DbSuchlaufDefinition.ergebnisPosition 001");
            return null;
        }
        if (pN < 0) {
            CaBug.drucke("DbSuchlaufDefinition.ergebnisPosition 002");
            return null;
        }
        if (pN >= ergebnisArray.length) {
            CaBug.drucke("DbSuchlaufDefinition.ergebnisPosition 003");
            return null;
        }
        return ergebnisArray[pN];
    }

    public int createTable() {
        int rc = 0;
        DbLowLevel lDbLowLevel = new DbLowLevel(dbBundle);
        rc = lDbLowLevel.createTable("CREATE TABLE " + dbBundle.getSchemaMandant() + "tbl_suchlaufDefinition ( "
                + "`mandant` int(11) NOT NULL, " + "`ident` int(11) NOT NULL, "
                + "`bezeichnung` varchar(200) DEFAULT NULL, " + "`identSuchlaufBegriffe` int(11) DEFAULT NULL, "
                + "`identSuchlaufBegriffIstGlobal` int(11) DEFAULT NULL, " + "`verwendung` int(11) DEFAULT NULL, "
                + "`sucheNachAktionaer` int(11) DEFAULT NULL, " + "`sucheNachVertreter` int(11) DEFAULT NULL, "
                + "`parameter1` varchar(100) DEFAULT NULL, " + "`parameter2` varchar(100) DEFAULT NULL, "
                + "`parameter3` varchar(100) DEFAULT NULL, " + "`parameter4` varchar(100) DEFAULT NULL, "
                + "`parameter5` varchar(100) DEFAULT NULL, " + "PRIMARY KEY (`mandant`,`ident`) " + ") ");
        return rc;
    }

    /**************************deleteAll Löschen aller Datensätze  eines Mandanten****************/
    public int deleteAll() {
        dbBundle.dbBasis.resetInterneIdentInsti();
        return dbBundle.dbLowLevel
                .deleteMandant("DELETE FROM " + dbBundle.getSchemaMandant() + "tbl_suchlaufDefinition where mandant=?");
    }

    /**************************setzt aktuelle Mandantennummer bei allen Datensätzen****************/
    public int updateMandant() {
        return dbBundle.dbLowLevel.rawUpdateMandant(dbBundle.getSchemaMandant() + "tbl_suchlaufDefinition");
    }

    public void reorgInterneIdent() {
        int lMax = dbBundle.dbLowLevel.liefereHoechsteIdent(
                "SELECT MAX(ident) FROM " + dbBundle.getSchemaMandant() + "tbl_suchlaufDefinition where mandant=?");
        if (lMax != -1) {
            dbBundle.dbBasis.resetInterneIdentSuchlaufDefinition(lMax);
        }
    }

    /********** dekodiert die aktuelle Position aus ergebnis  und gibt dieses zurück******/
    EclSuchlaufDefinition decodeErgebnis(ResultSet pErgebnis) {

        EclSuchlaufDefinition lEclReturn = new EclSuchlaufDefinition();

        try {
            lEclReturn.mandant = pErgebnis.getInt("mandant");
            lEclReturn.ident = pErgebnis.getInt("ident");

            lEclReturn.bezeichnung = pErgebnis.getString("bezeichnung");
            lEclReturn.identSuchlaufBegriffe = pErgebnis.getInt("identSuchlaufBegriffe");
            lEclReturn.identSuchlaufBegriffIstGlobal = pErgebnis.getInt("identSuchlaufBegriffIstGlobal");
            lEclReturn.verwendung = pErgebnis.getInt("verwendung");
            lEclReturn.sucheNachAktionaer = pErgebnis.getInt("sucheNachAktionaer");
            lEclReturn.sucheNachVertreter = pErgebnis.getInt("sucheNachVertreter");

            lEclReturn.parameter1 = pErgebnis.getString("parameter1");
            lEclReturn.parameter2 = pErgebnis.getString("parameter2");
            lEclReturn.parameter3 = pErgebnis.getString("parameter3");
            lEclReturn.parameter4 = pErgebnis.getString("parameter4");
            lEclReturn.parameter5 = pErgebnis.getString("parameter5");

        } catch (Exception e) {
            CaBug.drucke("DbSuchlaufDefinition.decodeErgebnis 001");
            System.err.println(" " + e.getMessage());
        }

        return lEclReturn;
    }

    /********************* Fuellen Prepared Statement mit allen Feldern, beginnend bei offset.*****************
     * Kann sowohl für Insert, als auch für update verwendet werden.
     * 
     * offset= Startposition des ersten Feldes (also z.B. 1)
     */
    private int anzfelder = 13; /*Anpassen auf Anzahl der Felder pro Datensatz*/

    private void fuellePreparedStatementKomplett(PreparedStatement pPStm, int pOffset, EclSuchlaufDefinition pEcl) {

        int startOffset = pOffset; /*Nur erforderlich zum Überprüfen von Programmierfehlern - Feldanzahl*/

        try {
            pPStm.setInt(pOffset, pEcl.mandant);
            pOffset++;
            pPStm.setInt(pOffset, pEcl.ident);
            pOffset++;

            pPStm.setString(pOffset, pEcl.bezeichnung);
            pOffset++;
            pPStm.setInt(pOffset, pEcl.identSuchlaufBegriffe);
            pOffset++;
            pPStm.setInt(pOffset, pEcl.identSuchlaufBegriffIstGlobal);
            pOffset++;
            pPStm.setInt(pOffset, pEcl.verwendung);
            pOffset++;
            pPStm.setInt(pOffset, pEcl.sucheNachAktionaer);
            pOffset++;
            pPStm.setInt(pOffset, pEcl.sucheNachVertreter);
            pOffset++;

            pPStm.setString(pOffset, pEcl.parameter1);
            pOffset++;
            pPStm.setString(pOffset, pEcl.parameter2);
            pOffset++;
            pPStm.setString(pOffset, pEcl.parameter3);
            pOffset++;
            pPStm.setString(pOffset, pEcl.parameter4);
            pOffset++;
            pPStm.setString(pOffset, pEcl.parameter5);
            pOffset++;

            if (pOffset - startOffset != anzfelder) {
                /*Nur wg. Überprüfung auf Programmierfehler - alle Felder berücksichtigt?*/
                CaBug.drucke("DbSuchlaufDefinition.fuellePreparedStatementKomplett 002");
            }

        } catch (SQLException e) {
            CaBug.drucke("DbSuchlaufDefinition.fuellePreparedStatementKomplett 001");
            e.printStackTrace();
        }

    }

    /**Insert
     * 
     * Returnwert:
     * =1 => Insert erfolgreich
     * ansonsten: Fehler
     */
    public int insert(EclSuchlaufDefinition pEcl) {

        int erg = 0;
        dbBasis.beginTransaction();

        /* neue InterneIdent vergeben */
        erg = dbBasis.getInterneIdentSuchlaufDefinition();
        if (erg < 1) {
            CaBug.drucke("DbSuchlaufDefinition.insert 002");
            dbBasis.endTransaction();
            return (erg);
        }

        pEcl.ident = erg;

        pEcl.mandant = dbBundle.clGlobalVar.mandant;

        try {

            /*Felder Neuanlage füllen*/
            String lSql = "INSERT INTO " + dbBundle.getSchemaMandant() + "tbl_suchlaufDefinition " + "("
                    + "mandant, ident, "
                    + "bezeichnung, identSuchlaufBegriffe, identSuchlaufBegriffIstGlobal, verwendung, sucheNachAktionaer, sucheNachVertreter, "
                    + "parameter1, parameter2, parameter3, parameter4, parameter5  " + ")" + "VALUES (" + "?, ?, "
                    + "?, ?, ?, ?, ?, ?, " + "?, ?, ?, ?, ? " + ")";
            PreparedStatement lPStm = verbindung.prepareStatement(lSql, ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);

            fuellePreparedStatementKomplett(lPStm, 1, pEcl);

            erg = lPStm.executeUpdate();
            lPStm.close();
        } catch (Exception e2) {
            CaBug.drucke("DbSuchlaufDefinition.insert 001");
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

    /**Mandant und Ident müssen übergeben werden.
     * 
     * Return-Wert = Anzahl der gefundenen Sätze (oder Fehlermeldung <0)*/
    public int read(int pIdent) {
        int anzInArray = 0;
        PreparedStatement lPStm = null;

        try {
            String lSql = "SELECT * from " + dbBundle.getSchemaMandant() + "tbl_suchlaufDefinition where "
                    + "mandant=? AND " + "ident=? " + "ORDER BY ident;";
            lPStm = verbindung.prepareStatement(lSql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            lPStm.setInt(1, dbBundle.clGlobalVar.mandant);
            lPStm.setInt(2, pIdent);

            ResultSet lErgebnis = lPStm.executeQuery();
            lErgebnis.last();
            anzInArray = lErgebnis.getRow();
            lErgebnis.beforeFirst();

            ergebnisArray = new EclSuchlaufDefinition[anzInArray];

            int i = 0;
            while (lErgebnis.next() == true) {
                ergebnisArray[i] = this.decodeErgebnis(lErgebnis);
                i++;
            }
            lErgebnis.close();
            lPStm.close();

        } catch (Exception e) {
            CaBug.drucke("DbSuchlaufDefinition.read 003");
            System.err.println(" " + e.getMessage());
            return (-1);
        }
        return (anzInArray);
    }

    /**
     * 
     * Return-Wert = Anzahl der gefundenen Sätze (oder Fehlermeldung <0)*/
    public int readVerwendung(int pVerwendung, String pParameter1, String pParameter2, String pParameter3,
            String pParameter4, String pParameter5) {
        int anzInArray = 0;
        PreparedStatement lPStm = null;

        try {
            String lSql = "SELECT * from " + dbBundle.getSchemaMandant() + "tbl_suchlaufDefinition where "
                    + "mandant=? AND " + "verwendung=? ";
            if (!pParameter1.isEmpty()) {
                lSql = lSql + "AND parameter1=? ";
            }
            if (!pParameter2.isEmpty()) {
                lSql = lSql + "AND parameter2=? ";
            }
            if (!pParameter3.isEmpty()) {
                lSql = lSql + "AND parameter3=? ";
            }
            if (!pParameter4.isEmpty()) {
                lSql = lSql + "AND parameter4=? ";
            }
            if (!pParameter5.isEmpty()) {
                lSql = lSql + "AND parameter5=? ";
            }
            lSql = lSql + "ORDER BY ident;";
            lPStm = verbindung.prepareStatement(lSql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            lPStm.setInt(1, dbBundle.clGlobalVar.mandant);
            lPStm.setInt(2, pVerwendung);
            int offset = 2;
            if (!pParameter1.isEmpty()) {
                offset++;
                lPStm.setString(offset, pParameter1);
            }
            if (!pParameter2.isEmpty()) {
                offset++;
                lPStm.setString(offset, pParameter2);
            }
            if (!pParameter3.isEmpty()) {
                offset++;
                lPStm.setString(offset, pParameter3);
            }
            if (!pParameter4.isEmpty()) {
                offset++;
                lPStm.setString(offset, pParameter4);
            }
            if (!pParameter5.isEmpty()) {
                offset++;
                lPStm.setString(offset, pParameter5);
            }

            ResultSet lErgebnis = lPStm.executeQuery();
            lErgebnis.last();
            anzInArray = lErgebnis.getRow();
            lErgebnis.beforeFirst();

            ergebnisArray = new EclSuchlaufDefinition[anzInArray];

            int i = 0;
            while (lErgebnis.next() == true) {
                ergebnisArray[i] = this.decodeErgebnis(lErgebnis);
                i++;
            }
            lErgebnis.close();
            lPStm.close();

        } catch (Exception e) {
            CaBug.drucke("DbSuchlaufDefinition.read 003");
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
    public int update(EclSuchlaufDefinition pEcl) {

        try {

            String lSql = "UPDATE " + dbBundle.getSchemaMandant() + "tbl_suchlaufDefinition SET "
                    + "mandant=?, ident=?, "
                    + "bezeichnung=?, identSuchlaufBegriffe=?, identSuchlaufBegriffIstGlobal=?, verwendung=?, sucheNachAktionaer=?, sucheNachVertreter=?, "
                    + "parameter1=?, parameter2=?, parameter3=?, parameter4=?, parameter5=?  " + "WHERE "
                    + "mandant=? AND ident=? ";

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
            CaBug.drucke("DbSuchlaufDefinition.update 001");
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
    public int delete(int pIdent) {
        try {

            String sql = "DELETE FROM " + dbBundle.getSchemaMandant()
                    + "tbl_suchlaufDefinition WHERE ident=? AND mandant=? ";

            PreparedStatement pstm1 = verbindung.prepareStatement(sql);
            pstm1.setInt(1, pIdent);
            pstm1.setInt(2, dbBundle.clGlobalVar.mandant);

            int ergebnis1 = pstm1.executeUpdate();
            pstm1.close();
            if (ergebnis1 == 0) {
                return (CaFehler.pfXyWurdeVonAnderemBenutzerVeraendert);
            }
        } catch (Exception e1) {
            CaBug.drucke("DbSuchlaufDefinition.delete 001");
            System.err.println(" " + e1.getMessage());
            return (-1);
        }

        return (1);
    }

}
