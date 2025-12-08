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
import de.meetingapps.meetingportal.meetComEntities.EclVerarbeitungsLauf;

public class DbVerarbeitungsLauf {

    private Connection verbindung = null;
    private DbBundle dbBundle = null;

    public EclVerarbeitungsLauf ergebnisArray[] = null;

    /*************************Initialisierung***************************/
    public DbVerarbeitungsLauf(DbBundle pDbBundle) {
        /* Verbindung in lokale Daten eintragen*/
        if (pDbBundle == null) {
            CaBug.drucke("DbVerarbeitungsLauf.init 001 - dbBundle nicht initialisiert");
            return;
        }
        if (pDbBundle.dbBasis == null) {
            CaBug.drucke("DbVerarbeitungsLauf.init 002 - dbBasis nicht initialisiert");
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
    public EclVerarbeitungsLauf ergebnisPosition(int pN) {
        if (ergebnisArray == null) {
            CaBug.drucke("DbVerarbeitungsLauf.ergebnisPosition 001");
            return null;
        }
        if (pN < 0) {
            CaBug.drucke("DbVerarbeitungsLauf.ergebnisPosition 002");
            return null;
        }
        if (pN >= ergebnisArray.length) {
            CaBug.drucke("DbVerarbeitungsLauf.ergebnisPosition 003");
            return null;
        }
        return ergebnisArray[pN];
    }

    /**************************deleteAll Löschen aller Datensätze  eines Mandanten****************/
    public int deleteAll() {
        return dbBundle.dbLowLevel.deleteMandant(
                "DELETE FROM " + dbBundle.getSchemaAllgemein() + "tbl_verarbeitungsLauf where mandant=?;");
    }

    /*******Checken, ob table überhaupt vorhanden ist***************************/
    public boolean checkTableVorhanden() {
        DbLowLevel lDbLowLevel = new DbLowLevel(dbBundle);
        return lDbLowLevel.checkTableVorhanden("SELECT * from tbl_verarbeitungsLauf WHERE mandant=0; ");
    }

    /************Neuanlegen Table******************************/
    public int createTable() {
        int rc = 0;
        DbLowLevel lDbLowLevel = new DbLowLevel(dbBundle);
        rc = lDbLowLevel.createTable("CREATE TABLE " + dbBundle.getSchemaAllgemein() + "tbl_verarbeitungsLauf ( "
                + "`mandant` int(11) NOT NULL, " + "`ident` int(11) NOT NULL, "
                + "`verarbeitungsArt` int(11) NOT NULL, " + "`statusDesLaufs` int(11) NOT NULL, "
                + "`verarbeitungsZeit` char(19) DEFAULT NULL, " + "`argument0` varchar(200) DEFAULT NULL, "
                + "`argument1` varchar(200) DEFAULT NULL, " + "`argument2` varchar(200) DEFAULT NULL, "
                + "`argument3` varchar(200) DEFAULT NULL, " + "`argument4` varchar(200) DEFAULT NULL, "
                + "`argument5` varchar(200) DEFAULT NULL, " + "`argument6` varchar(200) DEFAULT NULL, "
                + "`argument7` varchar(200) DEFAULT NULL, " + "`argument8` varchar(200) DEFAULT NULL, "
                + "`argument9` varchar(200) DEFAULT NULL, " + "PRIMARY KEY (`ident`) " + ")  ");
        System.out.println("DbVerarbeitungsLauf create table rc=" + rc);
        return rc;
    }

    /********** dekodiert die aktuelle Position aus ergebnis in EclAktienregisterEintrag und gibt dieses zurück******/
    EclVerarbeitungsLauf decodeErgebnis(ResultSet pErgebnis) {

        EclVerarbeitungsLauf lVerarbeitungsLauf = new EclVerarbeitungsLauf();

        try {
            lVerarbeitungsLauf.mandant = pErgebnis.getInt("vl.mandant");
            lVerarbeitungsLauf.ident = pErgebnis.getInt("vl.ident");
            lVerarbeitungsLauf.verarbeitungsArt = pErgebnis.getInt("vl.verarbeitungsArt");
            lVerarbeitungsLauf.statusDesLaufs = pErgebnis.getInt("vl.statusDesLaufs");
            lVerarbeitungsLauf.verarbeitungsZeit = pErgebnis.getString("vl.verarbeitungsZeit");

            for (int i = 0; i < 10; i++) {
                lVerarbeitungsLauf.argument[i] = pErgebnis.getString("vl.argument" + Integer.toString(i));
            }

        } catch (Exception e) {
            CaBug.drucke("DbVerarbeitungsLauf.decodeErgebnis 001");
            System.err.println(" " + e.getMessage());
        }

        return lVerarbeitungsLauf;
    }

    /********************* Fuellen Prepared Statement mit allen Feldern, beginnend bei offset.*****************
     * Kann sowohl für Insert, als auch für update verwendet werden.
     * 
     * offset= Startposition des ersten Feldes (also z.B. 1)
     */
    private int anzfelder = 15; /*Anpassen auf Anzahl der Felder pro Datensatz*/

    void fuellePreparedStatementKomplett(PreparedStatement pPStm, int pOffset,
            EclVerarbeitungsLauf pVerarbeitungsLauf) {

        int startOffset = pOffset; /*Nur erforderlich zum Überprüfen von Programmierfehlern - Feldanzahl*/

        try {
            pPStm.setInt(pOffset, pVerarbeitungsLauf.mandant);
            pOffset++;
            pPStm.setInt(pOffset, pVerarbeitungsLauf.ident);
            pOffset++;

            pPStm.setInt(pOffset, pVerarbeitungsLauf.verarbeitungsArt);
            pOffset++;
            pPStm.setInt(pOffset, pVerarbeitungsLauf.statusDesLaufs);
            pOffset++;
            pPStm.setString(pOffset, pVerarbeitungsLauf.verarbeitungsZeit);
            pOffset++;

            for (int i = 0; i < 10; i++) {
                pPStm.setString(pOffset, pVerarbeitungsLauf.argument[i]);
                pOffset++;
            }

            if (pOffset - startOffset != anzfelder) {
                /*Nur wg. Überprüfung auf Programmierfehler - alle Felder berücksichtigt?*/
                CaBug.drucke("DbVerarbeitungsLauf.fuellePreparedStatementKomplett 002");
            }

        } catch (SQLException e) {
            CaBug.drucke("DbVerarbeitungsLauf.fuellePreparedStatementKomplett 001");
            e.printStackTrace();
        }

    }

    /**Insert
     * 
     * Feld mandant wird von dieser Funktion nicht selbstständig belegt.
     * 
     * Returnwert:
     * =1 => Insert erfolgreich
     * ansonsten: Fehler
     */
    public int insert(EclVerarbeitungsLauf pVerarbeitungsLauf) {

        int erg = 0;

        /* neue InterneIdent vergeben */
        erg = dbBundle.dbBasis.getInterneIdentVerarbeitungsLaufOhneMandant();
        if (erg < 1) {
            CaBug.drucke("DbVerarbeitungsLauf.insert 002");
            return (erg);
        }

        System.out.println("DbVerarbeitungsLauf erg=" + erg);
        pVerarbeitungsLauf.ident = erg;

        try {
            /*Felder Neuanlage füllen*/
            String lSql = "INSERT INTO " + dbBundle.getSchemaAllgemein() + "tbl_verarbeitungsLauf " + "("
                    + "mandant, ident, verarbeitungsArt, statusDesLaufs, verarbeitungsZeit,"
                    + "argument0, argument1, argument2, argument3, argument4, "
                    + "argument5, argument6, argument7, argument8, argument9 " + ")" + "VALUES (" + "?, ?, ?, ?, ?, "
                    + "?, ?, ?, ?, ?, " + "?, ?, ?, ?, ? " + ")";
            PreparedStatement lPStm = verbindung.prepareStatement(lSql, ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
            fuellePreparedStatementKomplett(lPStm, 1, pVerarbeitungsLauf);
            erg = lPStm.executeUpdate();
            lPStm.close();
        } catch (Exception e2) {
            CaBug.drucke("DbVerarbeitungsLauf.insert 001");
            System.err.println(" " + e2.getMessage());
        }

        if (erg == 0) {/*Fehler beim Einfügen - d.h. z.B. primaryKey bereits vorhanden*/
            return (-1);
        }

        return (1);
    }

    /**Lese alle Verarbeitungsläufe
     * Falls pMandant>0 dann nur die für diesen Mandanten
     * */
    public int readAll(int pMandant) {
        int anzInArray = 0;
        PreparedStatement lPStm = null;

        try {
            String lSql = "SELECT * from " + dbBundle.getSchemaAllgemein() + "tbl_verarbeitungsLauf vl ";
            if (pMandant != 0) {
                lSql = lSql + " WHERE mandant=? ";
            }
            lSql = lSql + " ORDER BY ident";
            lPStm = verbindung.prepareStatement(lSql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            if (pMandant != 0) {
                lPStm.setInt(1, pMandant);
            }

            ResultSet lErgebnis = lPStm.executeQuery();
            lErgebnis.last();
            anzInArray = lErgebnis.getRow();
            lErgebnis.beforeFirst();

            ergebnisArray = new EclVerarbeitungsLauf[anzInArray];

            int i = 0;
            while (lErgebnis.next() == true) {
                ergebnisArray[i] = this.decodeErgebnis(lErgebnis);
                i++;
            }
            lErgebnis.close();
            lPStm.close();

        } catch (Exception e) {
            CaBug.drucke("DbVerarbeitungsLauf.readAll_technisch 003");
            System.err.println(" " + e.getMessage());
            return (-1);
        }
        return (anzInArray);
    }

    /**Einlesen eines bestimmten Laufs mit  ident*/
    public int read(int ident) {
        int anzInArray = 0;
        PreparedStatement lPStm = null;

        try {
            String lSql = "SELECT * from " + dbBundle.getSchemaAllgemein() + "tbl_verarbeitungsLauf vl WHERE ident=?;";
            lPStm = verbindung.prepareStatement(lSql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            lPStm.setInt(1, ident);

            ResultSet lErgebnis = lPStm.executeQuery();
            lErgebnis.last();
            anzInArray = lErgebnis.getRow();
            lErgebnis.beforeFirst();

            ergebnisArray = new EclVerarbeitungsLauf[anzInArray];

            int i = 0;
            while (lErgebnis.next() == true) {
                ergebnisArray[i] = this.decodeErgebnis(lErgebnis);
                i++;
            }

            lErgebnis.close();
            lPStm.close();

        } catch (Exception e) {
            CaBug.drucke("DbVerarbeitungsLauf.read 003");
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
    public int update(EclVerarbeitungsLauf pVerarbeitungsLauf) {

        try {
            String lSql = "UPDATE " + dbBundle.getSchemaAllgemein() + "tbl_verarbeitungsLauf SET "
                    + "mandant=?, ident=?, verarbeitungsArt=?, statusDesLaufs=?, verarbeitungsZeit=?, "
                    + "argument0=?, argument1=?, argument2=?, argument3=?, argument4=?, "
                    + "argument5=?, argument6=?, argument7=?, argument8=?, argument9=? " + "WHERE " + "ident=? ";

            PreparedStatement lPStm = verbindung.prepareStatement(lSql);
            fuellePreparedStatementKomplett(lPStm, 1, pVerarbeitungsLauf);
            lPStm.setInt(anzfelder + 1, pVerarbeitungsLauf.ident);

            int ergebnis = lPStm.executeUpdate();
            lPStm.close();
            if (ergebnis == 0) {
                return (CaFehler.pfXyWurdeVonAnderemBenutzerVeraendert);
            }
        } catch (Exception e1) {
            CaBug.drucke("DbVerarbeitungsLauf.update 001");
            System.err.println(" " + e1.getMessage());
            return (CaFehler.pfdXyBereitsVorhanden);
        }

        return (1);
    }

    public int delete(int pIdent) {

        try {

            String sql = "DELETE FROM " + dbBundle.getSchemaAllgemein() + "tbl_verarbeitungsLauf WHERE ident=? ";

            PreparedStatement pstm1 = verbindung.prepareStatement(sql);
            pstm1.setInt(1, pIdent);

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
