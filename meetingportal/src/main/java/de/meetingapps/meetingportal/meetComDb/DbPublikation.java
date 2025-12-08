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
import de.meetingapps.meetingportal.meetComEntities.EclPublikation;

public class DbPublikation {

    private Connection verbindung = null;
    private DbBasis dbBasis = null;
    private DbBundle dbBundle = null;

    private EclPublikation ergebnisArray[] = null;

    /*************************Initialisierung***************************/
    public DbPublikation(DbBundle pDbBundle) {
        /* Verbindung in lokale Daten eintragen*/
        if (pDbBundle == null) {
            CaBug.drucke("DbPublikation.init 001 - dbBundle nicht initialisiert");
            return;
        }
        if (pDbBundle.dbBasis == null) {
            CaBug.drucke("DbPublikation.init 002 - dbBasis nicht initialisiert");
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
    public EclPublikation ergebnisPosition(int pN) {
        if (ergebnisArray == null) {
            CaBug.drucke("DbPublikation.ergebnisPosition 001");
            return null;
        }
        if (pN < 0) {
            CaBug.drucke("DbPublikation.ergebnisPosition 002");
            return null;
        }
        if (pN >= ergebnisArray.length) {
            CaBug.drucke("DbPublikation.ergebnisPosition 003");
            return null;
        }
        return ergebnisArray[pN];
    }

    public int createTable() {
        int rc = 0;
        DbLowLevel lDbLowLevel = new DbLowLevel(dbBundle);
        rc = lDbLowLevel.createTable("CREATE TABLE " + dbBundle.getSchemaMandant() + "tbl_publikation ( "
                + "`mandant` int(11) NOT NULL, " + "`ident` int(11) NOT NULL, "
                + "`db_version` bigint(20) DEFAULT NULL, " + "`position` int(11) DEFAULT NULL, "
                + "`bezeichnung` char(30) DEFAULT NULL, " + "`publikationenZustellung0` int(11) DEFAULT NULL, "
                + "`publikationenZustellung1` int(11) DEFAULT NULL, "
                + "`publikationenZustellung2` int(11) DEFAULT NULL, "
                + "`publikationenZustellung3` int(11) DEFAULT NULL, "
                + "`publikationenZustellung4` int(11) DEFAULT NULL, "
                + "`publikationenZustellung5` int(11) DEFAULT NULL, "
                + "`publikationenZustellung6` int(11) DEFAULT NULL, "
                + "`publikationenZustellung7` int(11) DEFAULT NULL, "
                + "`publikationenZustellung8` int(11) DEFAULT NULL, "
                + "`publikationenZustellung9` int(11) DEFAULT NULL, " + "PRIMARY KEY (`ident`,`mandant`) " + ") "

        );
        return rc;
    }

    /**************************deleteAll Löschen aller Datensätze  eines Mandanten****************/
    public int deleteAll() {
        dbBasis.resetInterneIdentPublikation();
        return dbBundle.dbLowLevel
                .deleteMandant("DELETE FROM " + dbBundle.getSchemaMandant() + "tbl_publikation where mandant=?;");
    }

    /**************************setzt aktuelle Mandantennummer bei allen Datensätzen****************/
    public int updateMandant() {
        return dbBundle.dbLowLevel.rawUpdateMandant(dbBundle.getSchemaMandant() + "tbl_publikation");
    }

    public void reorgInterneIdent() {
        int lMax = dbBundle.dbLowLevel.liefereHoechsteIdent(
                "SELECT MAX(ident) FROM " + dbBundle.getSchemaMandant() + "tbl_publikation ab where ab.mandant=? ");
        if (lMax != -1) {
            dbBundle.dbBasis.resetInterneIdentPublikation(lMax);
        }
    }

    /********** dekodiert die aktuelle Position aus ergebnis in EclAktienregisterEintrag und gibt dieses zurück******/
    private EclPublikation decodeErgebnis(ResultSet pErgebnis) {

        EclPublikation lPublikation = new EclPublikation();

        try {

            lPublikation.mandant = pErgebnis.getInt("mandant");
            lPublikation.ident = pErgebnis.getInt("ident");
            lPublikation.db_version = pErgebnis.getLong("db_version");

            lPublikation.position = pErgebnis.getInt("position");
            lPublikation.bezeichnung = pErgebnis.getString("bezeichnung");
            lPublikation.publikationenZustellung[0] = pErgebnis.getInt("publikationenZustellung0");
            lPublikation.publikationenZustellung[1] = pErgebnis.getInt("publikationenZustellung1");
            lPublikation.publikationenZustellung[2] = pErgebnis.getInt("publikationenZustellung2");
            lPublikation.publikationenZustellung[3] = pErgebnis.getInt("publikationenZustellung3");
            lPublikation.publikationenZustellung[4] = pErgebnis.getInt("publikationenZustellung4");
            lPublikation.publikationenZustellung[5] = pErgebnis.getInt("publikationenZustellung5");
            lPublikation.publikationenZustellung[6] = pErgebnis.getInt("publikationenZustellung6");
            lPublikation.publikationenZustellung[7] = pErgebnis.getInt("publikationenZustellung7");
            lPublikation.publikationenZustellung[8] = pErgebnis.getInt("publikationenZustellung8");
            lPublikation.publikationenZustellung[9] = pErgebnis.getInt("publikationenZustellung9");

        } catch (Exception e) {
            CaBug.drucke("DbPublikation.decodeErgebnis 001");
            System.err.println(" " + e.getMessage());
        }

        return lPublikation;
    }

    /********************* Fuellen Prepared Statement mit allen Feldern, beginnend bei offset.*****************
     * Kann sowohl für Insert, als auch für update verwendet werden.
     * 
     * offset= Startposition des ersten Feldes (also z.B. 1)
     */
    private int anzfelder = 15; /*Anpassen auf Anzahl der Felder pro Datensatz*/

    private void fuellePreparedStatementKomplett(PreparedStatement pPStm, int pOffset, EclPublikation pPublikation) {

        int startOffset = pOffset; /*Nur erforderlich zum Überprüfen von Programmierfehlern - Feldanzahl*/

        try {
            pPStm.setInt(pOffset, pPublikation.mandant);
            pOffset++;
            pPStm.setInt(pOffset, pPublikation.ident);
            pOffset++;
            pPStm.setLong(pOffset, pPublikation.db_version);
            pOffset++;

            pPStm.setInt(pOffset, pPublikation.position);
            pOffset++;
            pPStm.setString(pOffset, pPublikation.bezeichnung);
            pOffset++;

            pPStm.setInt(pOffset, pPublikation.publikationenZustellung[0]);
            pOffset++;
            pPStm.setInt(pOffset, pPublikation.publikationenZustellung[1]);
            pOffset++;
            pPStm.setInt(pOffset, pPublikation.publikationenZustellung[2]);
            pOffset++;
            pPStm.setInt(pOffset, pPublikation.publikationenZustellung[3]);
            pOffset++;
            pPStm.setInt(pOffset, pPublikation.publikationenZustellung[4]);
            pOffset++;
            pPStm.setInt(pOffset, pPublikation.publikationenZustellung[5]);
            pOffset++;
            pPStm.setInt(pOffset, pPublikation.publikationenZustellung[6]);
            pOffset++;
            pPStm.setInt(pOffset, pPublikation.publikationenZustellung[7]);
            pOffset++;
            pPStm.setInt(pOffset, pPublikation.publikationenZustellung[8]);
            pOffset++;
            pPStm.setInt(pOffset, pPublikation.publikationenZustellung[9]);
            pOffset++;

            if (pOffset - startOffset != anzfelder) {
                /*Nur wg. Überprüfung auf Programmierfehler - alle Felder berücksichtigt?*/
                CaBug.drucke("DbPublikation.fuellePreparedStatementKomplett 002");
            }

        } catch (SQLException e) {
            CaBug.drucke("DbPublikation.fuellePreparedStatementKomplett 001");
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
    public int insert(EclPublikation pPublikation) {

        int erg = 0;

        /* Start Transaktion */
        dbBasis.beginTransaction();

        /* neue InterneIdent vergeben */
        erg = dbBasis.getInterneIdentPublikation();
        if (erg < 1) {
            CaBug.drucke("DbPublikation.insert 002");
            dbBasis.rollbackTransaction();
            dbBasis.endTransaction();
            return (erg);
        }

        pPublikation.ident = erg;

        pPublikation.mandant = dbBundle.clGlobalVar.mandant;

        /* Satz einfügen: 
         * Verarbeitungshinweis: 
         * 	>	nachdem InterneIdent immer eindeutig vergeben werden, ist prinzipiell eine "Doppeleinfügung"
         * 		von InterneIdent nicht möglich. Sollte es dazu kommen, ist das immer ein Programmfehler!
         */

        try {

            /*Felder Neuanlage füllen*/
            String lSql = "INSERT INTO " + dbBundle.getSchemaMandant() + "tbl_publikation " + "("
                    + "mandant, ident, db_version, " + "position, bezeichnung, "
                    + "publikationenZustellung0, publikationenZustellung1, publikationenZustellung2, publikationenZustellung3, publikationenZustellung4, "
                    + "publikationenZustellung5, publikationenZustellung6, publikationenZustellung7, publikationenZustellung8, publikationenZustellung9 "
                    + ")" + "VALUES (" + "?, ?, ?, " + "?, ?,  " + "?, ?, ?, ?, ?, " + "?, ?, ?, ?, ? " + ")";
            PreparedStatement lPStm = verbindung.prepareStatement(lSql, ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);

            fuellePreparedStatementKomplett(lPStm, 1, pPublikation);

            erg = lPStm.executeUpdate();

            lPStm.close();
        } catch (Exception e2) {
            CaBug.drucke("DbPublikation.insert 001");
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

    /**Realisierte Selektion derzeit, jeweils einzeln: 
     * ident
     * 
     * Return-Wert:
     * >=1 => Anzahl der gefundenen Datensätze
     * =0 => nix gefunden
     * <0 => Fehler
     * */
    public int read(EclPublikation lPublikation) {
        int anzInArray = 0;
        PreparedStatement lPStm = null;
        int gefSelect = 0; /*Nur zum Aufdecken von Programmierfehlern - in diesem Fall: unvollständige Parameterübergabe*/

        if (lPublikation == null) {
            CaBug.drucke("DbPublikation.read 001");
            return -1;
        }

        try {
            if (lPublikation.ident != 0) {
                gefSelect = 1;
                String lSql = "SELECT * from " + dbBundle.getSchemaMandant() + "tbl_publikation where "
                        + "mandant=? AND " + "ident=? ORDER BY ident;";
                lPStm = verbindung.prepareStatement(lSql, ResultSet.TYPE_SCROLL_INSENSITIVE,
                        ResultSet.CONCUR_READ_ONLY);
                lPStm.setInt(1, dbBundle.clGlobalVar.mandant);
                lPStm.setInt(2, lPublikation.ident);
            }

            if (gefSelect == 0) {
                CaBug.drucke("DbPublikation.read 002");
                return -1;
            }

            ResultSet lErgebnis = lPStm.executeQuery();
            lErgebnis.last();
            anzInArray = lErgebnis.getRow();
            lErgebnis.beforeFirst();

            ergebnisArray = new EclPublikation[anzInArray];

            int i = 0;
            while (lErgebnis.next() == true) {

                ergebnisArray[i] = this.decodeErgebnis(lErgebnis);
                i++;

            }
            lErgebnis.close();
            lPStm.close();

        } catch (Exception e) {
            CaBug.drucke("DbPublikation.read 003");
            System.err.println(" " + e.getMessage());
            return (-1);
        }

        return (anzInArray);
    }

    /**Liest alle zur Verfügung stehende Publikationen für diesen Mandanten, aufsteigend sortiert nach position.
     * 
     * Return-Wert:
     * >=1 => Anzahl der gefundenen Datensätze
     * =0 => nix gefunden
     * <0 => Fehler
     * */
    public int read_all() {
        int anzInArray = 0;
        PreparedStatement lPStm = null;

        try {
            String lSql = "SELECT * from " + dbBundle.getSchemaMandant() + "tbl_publikation where " + "mandant=? "
                    + "ORDER BY position;";
            lPStm = verbindung.prepareStatement(lSql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            lPStm.setInt(1, dbBundle.clGlobalVar.mandant);

            ResultSet lErgebnis = lPStm.executeQuery();
            lErgebnis.last();
            anzInArray = lErgebnis.getRow();
            lErgebnis.beforeFirst();

            ergebnisArray = new EclPublikation[anzInArray];

            int i = 0;
            while (lErgebnis.next() == true) {

                ergebnisArray[i] = this.decodeErgebnis(lErgebnis);
                i++;

            }
            lErgebnis.close();
            lPStm.close();

        } catch (Exception e) {
            CaBug.drucke("DbPublikation.read 003");
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
    public int update(EclPublikation pPublikation) {

        pPublikation.db_version++;

        try {

            String lSql = "UPDATE " + dbBundle.getSchemaMandant() + "tbl_publikation SET "
                    + "mandant=?, ident=?, db_version=?, " + "position=?, bezeichnung=?, "
                    + "publikationenZustellung0=?, publikationenZustellung1=?, publikationenZustellung2=?, publikationenZustellung3=?, publikationenZustellung4=?, "
                    + "publikationenZustellung5=?, publikationenZustellung6=?, publikationenZustellung7=?, publikationenZustellung8=?, publikationenZustellung9=? "
                    + "WHERE " + "ident=? AND " + "db_version=? AND mandant=?";

            PreparedStatement lPStm = verbindung.prepareStatement(lSql);
            fuellePreparedStatementKomplett(lPStm, 1, pPublikation);
            lPStm.setInt(anzfelder + 1, pPublikation.ident);
            lPStm.setLong(anzfelder + 2, pPublikation.db_version - 1);
            lPStm.setLong(anzfelder + 3, dbBundle.clGlobalVar.mandant);

            int ergebnis = lPStm.executeUpdate();
            lPStm.close();
            if (ergebnis == 0) {
                return (CaFehler.pfXyWurdeVonAnderemBenutzerVeraendert);
            }
        } catch (Exception e1) {
            CaBug.drucke("DbPublikation.update 001");
            System.err.println(" " + e1.getMessage());
            return (CaFehler.pfdXyBereitsVorhanden);
        }

        return (1);
    }
}
