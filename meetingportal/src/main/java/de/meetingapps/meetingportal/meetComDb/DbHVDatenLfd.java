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
import de.meetingapps.meetingportal.meetComEntities.EclHVDatenLfd;

/**Hinweis: da Zugriff auch über Puffer möglich, läuft in dieser Db-Klasse einiges
 * anders als in den Standard-Db-Klassen! Bitte Hinweise zu den einzelnen Funktionen beachten!
 */
public class DbHVDatenLfd {

    private Connection verbindung = null;
    private DbBasis dbBasis = null;
    private DbBundle dbBundle = null;

    private EclHVDatenLfd ergebnisArray[] = null;

    /*************************Initialisierung***************************/
    public DbHVDatenLfd(DbBundle pDbBundle) {
        /* Verbindung in lokale Daten eintragen*/
        if (pDbBundle == null) {
            CaBug.drucke("DbHVDatenLfd.init 001 - dbBundle nicht initialisiert");
            return;
        }
        if (pDbBundle.dbBasis == null) {
            CaBug.drucke("DbHVDatenLfd.init 002 - dbBasis nicht initialisiert");
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
    public EclHVDatenLfd ergebnisPosition(int pN) {
        if (ergebnisArray == null) {
            CaBug.drucke("DbHVDatenLfd.ergebnisPosition 001");
            return null;
        }
        if (pN < 0) {
            CaBug.drucke("DbHVDatenLfd.ergebnisPosition 002");
            return null;
        }
        if (pN >= ergebnisArray.length) {
            CaBug.drucke("DbHVDatenLfd.ergebnisPosition 003");
            return null;
        }
        return ergebnisArray[pN];
    }

    public int createTable() {
        int rc = 0;
        DbLowLevel lDbLowLevel = new DbLowLevel(dbBundle);
        rc = lDbLowLevel.createTable("CREATE TABLE " + dbBundle.getSchemaMandant() + "tbl_hvdatenlfd ( "
                + "`mandant` int(11) NOT NULL, " + "`ident` int(11) NOT NULL, "
                + "`db_version` bigint(20) DEFAULT NULL, " + "`benutzer` int(11) NOT NULL, "
                + "`wert` char(40) DEFAULT NULL, " + "`beschreibung` char(80) DEFAULT NULL, "
                + "PRIMARY KEY (`ident`,`mandant`,`benutzer`) " + ") "

        );
        return rc;
    }

    /**************************setzt aktuelle Mandantennummer bei allen Datensätzen****************/
    public int updateMandant() {
        return dbBundle.dbLowLevel.rawUpdateMandant(dbBundle.getSchemaMandant() + "tbl_hvdatenlfd");
    }

    /**************************deleteAll Löschen aller Datensätze  eines Mandanten****************/
    public int deleteAll() {
        int erg = 0;

        try {

            String sql1 = "DELETE FROM " + dbBundle.getSchemaMandant() + "tbl_hvDatenLfd where mandant=?;";
            PreparedStatement pstm1 = verbindung.prepareStatement(sql1);
            pstm1.setInt(1, dbBundle.clGlobalVar.mandant);

            erg = 0; /*Falls Duplicate Key, dann wird exception geschmissen und erg wohl nicht gefüllt!*/
            erg = pstm1.executeUpdate();
            pstm1.close();

        } catch (Exception e2) {
            CaBug.drucke("DbHVDatenLfd.deleteAll 001");
            System.err.println(" " + e2.getMessage());
            return (erg);
        }

        return 1;
    }

    /**************************deleteAll Löschen aller Datensätze  eines Mandanten****************/
    public int deleteArchivierteAbstimmungen() {
        int erg = 0;

        try {

            String sql1 = "DELETE FROM " + dbBundle.getSchemaMandant() + "tbl_hvDatenLfd where mandant=? AND (ident=1000 OR ident=1001);";
            PreparedStatement pstm1 = verbindung.prepareStatement(sql1);
            pstm1.setInt(1, dbBundle.clGlobalVar.mandant);

            erg = 0; /*Falls Duplicate Key, dann wird exception geschmissen und erg wohl nicht gefüllt!*/
            erg = pstm1.executeUpdate();
            pstm1.close();

        } catch (Exception e2) {
            CaBug.drucke("DbHVDatenLfd.deleteAll 001");
            System.err.println(" " + e2.getMessage());
            return (erg);
        }

        return 1;
    }

    /********** dekodiert die aktuelle Position aus ergebnis und gibt dieses zurück******/
    private EclHVDatenLfd decodeErgebnis(ResultSet pErgebnis) {

        EclHVDatenLfd lHVDatenLfd = new EclHVDatenLfd();

        try {

            lHVDatenLfd.mandant = pErgebnis.getInt("mandant");
            lHVDatenLfd.ident = pErgebnis.getInt("ident");
            lHVDatenLfd.db_version = pErgebnis.getLong("db_version");
            lHVDatenLfd.benutzer = pErgebnis.getInt("benutzer");

            lHVDatenLfd.wert = pErgebnis.getString("wert");
            lHVDatenLfd.beschreibung = pErgebnis.getString("beschreibung");

        } catch (Exception e) {
            CaBug.drucke("DbHVDatenLfd.decodeErgebnis 001");
            System.err.println(" " + e.getMessage());
        }

        return lHVDatenLfd;
    }

    /********************* Fuellen Prepared Statement mit allen Feldern, beginnend bei offset.*****************
     * Kann sowohl für Insert, als auch für update verwendet werden.
     * 
     * offset= Startposition des ersten Feldes (also z.B. 1)
     */
    private int anzfelder = 6; /*Anpassen auf Anzahl der Felder pro Datensatz*/

    private void fuellePreparedStatementKomplett(PreparedStatement pPStm, int pOffset, EclHVDatenLfd pHVDatenLfd) {

        int startOffset = pOffset; /*Nur erforderlich zum Überprüfen von Programmierfehlern - Feldanzahl*/

        try {
            pPStm.setInt(pOffset, pHVDatenLfd.mandant);
            pOffset++;
            pPStm.setInt(pOffset, pHVDatenLfd.ident);
            pOffset++;
            pPStm.setLong(pOffset, pHVDatenLfd.db_version);
            pOffset++;
            pPStm.setInt(pOffset, pHVDatenLfd.benutzer);
            pOffset++;

            pPStm.setString(pOffset, pHVDatenLfd.wert);
            pOffset++;
            pPStm.setString(pOffset, pHVDatenLfd.beschreibung);
            pOffset++;

            if (pOffset - startOffset != anzfelder) {
                /*Nur wg. Überprüfung auf Programmierfehler - alle Felder berücksichtigt?*/
                CaBug.drucke("DbHVDatenLfd.fuellePreparedStatementKomplett 002");
            }

        } catch (SQLException e) {
            CaBug.drucke("DbHVDatenLfd.fuellePreparedStatementKomplett 001");
            e.printStackTrace();
        }

    }

    /**Insert
     * 
     * Feld mandant wird von dieser Funktion immer selbstständig belegt.
     * 
     * Achtung: "ident" muß vorbelegt sein!
     * Returnwert:
     * =1 => Insert erfolgreich
     * ansonsten: Fehler
     */
    public int insert(EclHVDatenLfd pHVDatenLfd) {

        int erg = 0;

        /* Start Transaktion */
        dbBasis.beginTransaction();

        pHVDatenLfd.mandant = dbBundle.clGlobalVar.mandant;

        /* Satz einfügen: 
         * Verarbeitungshinweis: 
         * 	>	nachdem InterneIdent immer eindeutig vergeben werden, ist prinzipiell eine "Doppeleinfügung"
         * 		von InterneIdent nicht möglich. Sollte es dazu kommen, ist das immer ein Programmfehler!
         */

        try {

            /*Felder Neuanlage füllen*/
            String lSql = "INSERT INTO " + dbBundle.getSchemaMandant() + "tbl_hvDatenLfd " + "("
                    + "mandant, ident, db_version, benutzer, " + "wert, beschreibung " + ")" + "VALUES ("
                    + "?, ?, ?, ?, " + "?, ? " + ")";
            PreparedStatement lPStm = verbindung.prepareStatement(lSql, ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);

            fuellePreparedStatementKomplett(lPStm, 1, pHVDatenLfd);

            erg = lPStm.executeUpdate();
            lPStm.close();
        } catch (Exception e2) {
            CaBug.drucke("DbHVDatenLfd.insert 001");
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

    /**Realisierte Selektion derzeit, jeweils gemeinsam: 
     * ident + mandant + benutzer (!!!)
     * 
     * Return-Wert:
     * >=1 => Anzahl der gefundenen Datensätze
     * =0 => nix gefunden
     * <0 => Fehler
     * */
    public int read(EclHVDatenLfd pHVDatenLfd) {
        int anzInArray = 0;
        PreparedStatement lPStm = null;
        int gefSelect = 0; /*Nur zum Aufdecken von Programmierfehlern - in diesem Fall: unvollständige Parameterübergabe*/

        if (pHVDatenLfd == null) {
            CaBug.drucke("DbHVDatenLfd.read 001");
            return -1;
        }

        try {
            if (pHVDatenLfd.ident != 0) {
                gefSelect = 1;
                String lSql = "SELECT * from " + dbBundle.getSchemaMandant() + "tbl_hvDatenLfd where "
                        + "mandant=? AND " + "ident=? and benutzer=? ORDER BY ident;";
                lPStm = verbindung.prepareStatement(lSql, ResultSet.TYPE_SCROLL_INSENSITIVE,
                        ResultSet.CONCUR_READ_ONLY);
                lPStm.setInt(1, dbBundle.clGlobalVar.mandant);
                lPStm.setInt(2, pHVDatenLfd.ident);
                lPStm.setInt(3, pHVDatenLfd.benutzer);
            }

            if (gefSelect == 0) {
                CaBug.drucke("DbHVDatenLfd.read 002");
                return -1;
            }

            ResultSet lErgebnis = lPStm.executeQuery();
            lErgebnis.last();
            anzInArray = lErgebnis.getRow();
            lErgebnis.beforeFirst();

            ergebnisArray = new EclHVDatenLfd[anzInArray];

            int i = 0;
            while (lErgebnis.next() == true) {

                ergebnisArray[i] = this.decodeErgebnis(lErgebnis);
                i++;

            }
            lErgebnis.close();
            lPStm.close();

        } catch (Exception e) {
            CaBug.drucke("DbHVDatenLfd.read 003");
            System.err.println(" " + e.getMessage());
            return (-1);
        }

        return (anzInArray);
    }

    public int readLetzteProtokolle() {
        int anzInArray = 0;
        PreparedStatement lPStm = null;

        try {
            String lSql = "SELECT * from " + dbBundle.getSchemaMandant() + "tbl_hvDatenLfd where " + "mandant=? AND "
                    + "ident=104 ORDER BY benutzer;";
            lPStm = verbindung.prepareStatement(lSql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            lPStm.setInt(1, dbBundle.clGlobalVar.mandant);

            ResultSet lErgebnis = lPStm.executeQuery();
            lErgebnis.last();
            anzInArray = lErgebnis.getRow();
            lErgebnis.beforeFirst();

            ergebnisArray = new EclHVDatenLfd[anzInArray];

            int i = 0;
            while (lErgebnis.next() == true) {

                ergebnisArray[i] = this.decodeErgebnis(lErgebnis);
                i++;

            }
            lErgebnis.close();
            lPStm.close();

        } catch (Exception e) {
            CaBug.drucke("DbHVDatenLfd.readLetzteProtokolle 003");
            System.err.println(" " + e.getMessage());
            return (-1);
        }

        return (anzInArray);
    }

    public int readArbeitsplaetzeMitProtokollen() {
        int anzInArray = 0;
        PreparedStatement lPStm = null;

        try {
            String lSql = "SELECT * from " + dbBundle.getSchemaMandant() + "tbl_hvDatenLfd where " + "mandant=? AND "
                    + "ident=101 ORDER BY benutzer;";
            lPStm = verbindung.prepareStatement(lSql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            lPStm.setInt(1, dbBundle.clGlobalVar.mandant);

            ResultSet lErgebnis = lPStm.executeQuery();
            lErgebnis.last();
            anzInArray = lErgebnis.getRow();
            lErgebnis.beforeFirst();

            ergebnisArray = new EclHVDatenLfd[anzInArray];

            int i = 0;
            while (lErgebnis.next() == true) {

                ergebnisArray[i] = this.decodeErgebnis(lErgebnis);
                i++;

            }
            lErgebnis.close();
            lPStm.close();

        } catch (Exception e) {
            CaBug.drucke("DbHVDatenLfd.readLetzteProtokolle 003");
            System.err.println(" " + e.getMessage());
            return (-1);
        }

        return (anzInArray);
    }

    /**Realisierte Selektion derzeit, jeweils gemeinsam: 
     * ident + mandant + benutzer (!!!)
     * 
     * Return-Wert:
     * >=1 => Anzahl der gefundenen Datensätze
     * =0 => nix gefunden
     * <0 => Fehler
     * */
    public int readForUpdate(EclHVDatenLfd pHVDatenLfd) {
        int anzInArray = 0;
        PreparedStatement lPStm = null;
        int gefSelect = 0; /*Nur zum Aufdecken von Programmierfehlern - in diesem Fall: unvollständige Parameterübergabe*/

        if (pHVDatenLfd == null) {
            CaBug.drucke("DbHVDatenLfd.readForUpdate 001");
            return -1;
        }

        try {
            if (pHVDatenLfd.ident != 0) {
                gefSelect = 1;
                String lSql = "SELECT * from " + dbBundle.getSchemaMandant() + "tbl_hvDatenLfd where "
                        + "mandant=? AND " + "ident=? and benutzer=? ORDER BY ident FOR UPDATE;";
                lPStm = verbindung.prepareStatement(lSql, ResultSet.TYPE_SCROLL_INSENSITIVE,
                        ResultSet.CONCUR_READ_ONLY);
                lPStm.setInt(1, dbBundle.clGlobalVar.mandant);
                lPStm.setInt(2, pHVDatenLfd.ident);
                lPStm.setInt(3, pHVDatenLfd.benutzer);
            }

            if (gefSelect == 0) {
                CaBug.drucke("DbHVDatenLfd.readForUpdate 002");
                return -1;
            }

            ResultSet lErgebnis = lPStm.executeQuery();
            lErgebnis.last();
            anzInArray = lErgebnis.getRow();
            lErgebnis.beforeFirst();

            ergebnisArray = new EclHVDatenLfd[anzInArray];

            int i = 0;
            while (lErgebnis.next() == true) {

                ergebnisArray[i] = this.decodeErgebnis(lErgebnis);
                i++;

            }
            lErgebnis.close();
            lPStm.close();

        } catch (Exception e) {
            CaBug.drucke("DbHVDatenLfd.readForUpdate 003");
            System.err.println(" " + e.getMessage());
            return (-1);
        }

        return (anzInArray);
    }

    /**Update. Versionsnummer wird um 1 hochgezählt
     * 
     * Feld mandant muß vor dieser Funktion belegt werden!!
     * 
     * ident und benutzernummer geben den Satz an, der upgedated wird.
     * 
     * Zum Sicherstellen der Multiuserfähigkeit muß in jedem Fall der rc CaFehler.pfXyWurdeVonAnderemBenutzerVeraendert
     * nach Aufruf dieser Funktion abgefangen werden.
     * Ansonsten: rc=1 => ok, ansonsten Fehler
     */
    public int update(EclHVDatenLfd pHVDatenLfd) {

        pHVDatenLfd.db_version++;

        try {

            String lSql = "UPDATE " + dbBundle.getSchemaMandant() + "tbl_hvDatenLfd SET "
                    + "mandant=?, ident=?, db_version=?, benutzer=?, " + "wert=?, beschreibung=? " + "WHERE "
                    + "ident=? AND db_version=? AND mandant=? and benutzer=?";

            PreparedStatement lPStm = verbindung.prepareStatement(lSql);
            fuellePreparedStatementKomplett(lPStm, 1, pHVDatenLfd);
            lPStm.setInt(anzfelder + 1, pHVDatenLfd.ident);
            lPStm.setLong(anzfelder + 2, pHVDatenLfd.db_version - 1);
            lPStm.setInt(anzfelder + 3, dbBundle.clGlobalVar.mandant);
            lPStm.setInt(anzfelder + 4, pHVDatenLfd.benutzer);

            int ergebnis = lPStm.executeUpdate();
            lPStm.close();
            if (ergebnis == 0) {
                return (CaFehler.pfXyWurdeVonAnderemBenutzerVeraendert);
            }
        } catch (Exception e1) {
            CaBug.drucke("DbHVDatenLfd.update 001");
            System.err.println(" " + e1.getMessage());
            return (CaFehler.pfdXyBereitsVorhanden);
        }

        return (1);
    }
    
    public void insertOrUpdate(EclHVDatenLfd lHVDatenLfd, String wert) {

        readForUpdate(lHVDatenLfd);
        if (anzErgebnis() != 0) {/*Bereits ein Eintrag vorhanden*/
            lHVDatenLfd = ergebnisPosition(0);
            lHVDatenLfd.wert = wert;
            update(lHVDatenLfd);
        } else {
            lHVDatenLfd.wert = wert;
            insert(lHVDatenLfd);
        }

    }

}
