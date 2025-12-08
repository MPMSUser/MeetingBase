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
import de.meetingapps.meetingportal.meetComEntities.EclAbstimmungsblock;

public class DbAbstimmungsblock  extends DbRootExecute {

    private Connection verbindung = null;
    private DbBasis dbBasis = null;
    private DbBundle dbBundle = null;

    public EclAbstimmungsblock ergebnisArray[] = null;

    /*************************Initialisierung***************************/
    public DbAbstimmungsblock(DbBundle pDbBundle) {
        /* Verbindung in lokale Daten eintragen*/
        if (pDbBundle == null) {
            CaBug.drucke("DbAbstimmungsblock.init 001 - dbBundle nicht initialisiert");
            return;
        }
        if (pDbBundle.dbBasis == null) {
            CaBug.drucke("DbAbstimmungsblock.init 002 - dbBasis nicht initialisiert");
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
    public EclAbstimmungsblock ergebnisPosition(int pN) {
        if (ergebnisArray == null) {
            CaBug.drucke("DbAbstimmungsblock.ergebnisPosition 001");
            return null;
        }
        if (pN < 0) {
            CaBug.drucke("DbAbstimmungsblock.ergebnisPosition 002");
            return null;
        }
        if (pN >= ergebnisArray.length) {
            CaBug.drucke("DbAbstimmungsblock.ergebnisPosition 003");
            return null;
        }
        return ergebnisArray[pN];
    }

    public int createTable() {
        int rc = 0;
        DbLowLevel lDbLowLevel = new DbLowLevel(dbBundle);
        rc = lDbLowLevel.createTable("CREATE TABLE " + dbBundle.getSchemaMandant() + "tbl_abstimmungsblock ( "
                + "`mandant` int(11) NOT NULL, " + "`ident` int(11) NOT NULL, "
                + "`db_version` bigint(20) DEFAULT NULL, " + "`position` int(11) DEFAULT NULL, "
                + "`kurzBeschreibung` varchar(40) DEFAULT NULL, " + "`beschreibung` varchar(80) DEFAULT NULL, "
                + "`beschreibungEN` varchar(80) DEFAULT NULL, " + "`aktiv` int(11) DEFAULT NULL, "
                + "PRIMARY KEY (`ident`,`mandant`) " + ") ");
        return rc;
    }

    /**************************deleteAll Löschen aller Datensätze  eines Mandanten****************/
    public int deleteAll() {
        int erg = 0;

        dbBasis.resetInterneIdentAbstimmungsblock();

        try {

            String sql1 = "DELETE FROM " + dbBundle.getSchemaMandant() + "tbl_abstimmungsblock where mandant=?;";
            PreparedStatement pstm1 = verbindung.prepareStatement(sql1);
            pstm1.setInt(1, dbBundle.clGlobalVar.mandant);

            erg = 0; /*Falls Duplicate Key, dann wird exception geschmissen und erg wohl nicht gefüllt!*/
            erg = executeUpdate(pstm1);

            pstm1.close();
        } catch (Exception e2) {
            CaBug.drucke("DbAbstimmungsblock.deleteAll 001");
            System.err.println(" " + e2.getMessage());
            return (erg);
        }

        return 1;
    }

    /**************************setzt aktuelle Mandantennummer bei allen Datensätzen****************/
    public int updateMandant() {
        return dbBundle.dbLowLevel.rawUpdateMandant(dbBundle.getSchemaMandant() + "tbl_abstimmungsblock");
    }

    public void reorgInterneIdent() {
        int lMax;
        lMax = 0;

        PreparedStatement pstm1 = null;
        try {
            int anzInArray;
            String sql = "SELECT MAX(ident) FROM " + dbBundle.getSchemaMandant() + "tbl_abstimmungsblock ab where "
                    + "ab.mandant=? ";
            pstm1 = verbindung.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            pstm1.setInt(1, dbBundle.clGlobalVar.mandant);

            ResultSet ergebnis = executeQuery(pstm1);
            ergebnis.last();
            anzInArray = ergebnis.getRow();
            ergebnis.beforeFirst();

            if (anzInArray > 0) {
                ergebnis.next();
                lMax = ergebnis.getInt(1);
            }
            ergebnis.close();
            pstm1.close();

        } catch (Exception e) {
            CaBug.drucke("DbAbstimmungsblock.reorgInterneIdent 001");
            System.err.println(" " + e.getMessage());
        }

        dbBasis.resetInterneIdentAbstimmungsblock(lMax);

    }

    /********** dekodiert die aktuelle Position aus ergebnis in EclAktienregisterEintrag und gibt dieses zurück******/
    private EclAbstimmungsblock decodeErgebnis(ResultSet pErgebnis) {

        EclAbstimmungsblock lAbstimmungsblock = new EclAbstimmungsblock();

        try {

            lAbstimmungsblock.mandant = pErgebnis.getInt("mandant");
            lAbstimmungsblock.ident = pErgebnis.getInt("ident");
            lAbstimmungsblock.db_version = pErgebnis.getLong("db_version");

            lAbstimmungsblock.position = pErgebnis.getInt("position");

            lAbstimmungsblock.kurzBeschreibung = pErgebnis.getString("kurzBeschreibung");
            lAbstimmungsblock.beschreibung = pErgebnis.getString("beschreibung");
            lAbstimmungsblock.beschreibungEN = pErgebnis.getString("beschreibungEN");

            lAbstimmungsblock.aktiv = pErgebnis.getInt("aktiv");

        } catch (Exception e) {
            CaBug.drucke("DbAbstimmungsblock.decodeErgebnis 001");
            System.err.println(" " + e.getMessage());
        }

        return lAbstimmungsblock;
    }

    /********************* Fuellen Prepared Statement mit allen Feldern, beginnend bei offset.*****************
     * Kann sowohl für Insert, als auch für update verwendet werden.
     * 
     * offset= Startposition des ersten Feldes (also z.B. 1)
     */
    private int anzfelder = 8; /*Anpassen auf Anzahl der Felder pro Datensatz*/

    private void fuellePreparedStatementKomplett(PreparedStatement pPStm, int pOffset,
            EclAbstimmungsblock pAbstimmungsblock) {

        int startOffset = pOffset; /*Nur erforderlich zum Überprüfen von Programmierfehlern - Feldanzahl*/

        try {
            pPStm.setInt(pOffset, pAbstimmungsblock.mandant);
            pOffset++;
            pPStm.setInt(pOffset, pAbstimmungsblock.ident);
            pOffset++;
            pPStm.setLong(pOffset, pAbstimmungsblock.db_version);
            pOffset++;

            pPStm.setInt(pOffset, pAbstimmungsblock.position);
            pOffset++;

            pPStm.setString(pOffset, pAbstimmungsblock.kurzBeschreibung);
            pOffset++;
            pPStm.setString(pOffset, pAbstimmungsblock.beschreibung);
            pOffset++;
            pPStm.setString(pOffset, pAbstimmungsblock.beschreibungEN);
            pOffset++;

            pPStm.setInt(pOffset, pAbstimmungsblock.aktiv);
            pOffset++;

            if (pOffset - startOffset != anzfelder) {
                /*Nur wg. Überprüfung auf Programmierfehler - alle Felder berücksichtigt?*/
                CaBug.drucke("DbAbstimmungsblock.fuellePreparedStatementKomplett 002");
            }

        } catch (SQLException e) {
            CaBug.drucke("DbAbstimmungsblock.fuellePreparedStatementKomplett 001");
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
    public int insert(EclAbstimmungsblock pAbstimmungsblock) {

        int erg = 0;

        /* Start Transaktion */
        dbBasis.beginTransaction();

        /* neue InterneIdent vergeben */
        erg = dbBasis.getInterneIdentAbstimmungsblock();
        if (erg < 1) {
            CaBug.drucke("DbAbstimmungsblock.insert 002");
            dbBasis.rollbackTransaction();
            dbBasis.endTransaction();
            return (erg);
        }
        pAbstimmungsblock.ident = erg;

        pAbstimmungsblock.mandant = dbBundle.clGlobalVar.mandant;

        /* Satz einfügen: 
         * Verarbeitungshinweis: 
         * 	>	nachdem InterneIdent immer eindeutig vergeben werden, ist prinzipiell eine "Doppeleinfügung"
         * 		von InterneIdent nicht möglich. Sollte es dazu kommen, ist das immer ein Programmfehler!
         */

        try {

            /*Felder Neuanlage füllen*/
            String lSql = "INSERT INTO " + dbBundle.getSchemaMandant() + "tbl_abstimmungsblock " + "("
                    + "mandant, ident, db_version, " + "position, kurzBeschreibung, beschreibung, beschreibungEN, aktiv"
                    + ")" + "VALUES (" + "?, ?, ?, " + "?, ?, ?, ?, ? " + ")";
            PreparedStatement lPStm = verbindung.prepareStatement(lSql, ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);

            fuellePreparedStatementKomplett(lPStm, 1, pAbstimmungsblock);

            erg = executeUpdate(lPStm);
            lPStm.close();
        } catch (Exception e2) {
            CaBug.drucke("DbAbstimmungsblock.insert 001");
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

    public int read(int pAbstimmungsblockIdent) {
        EclAbstimmungsblock lAbstimmungsblock=new EclAbstimmungsblock();
        lAbstimmungsblock.ident=pAbstimmungsblockIdent;
        return read(lAbstimmungsblock);
    }
    
    public int read(EclAbstimmungsblock pAbstimmungsblock) {
        int anzInArray = 0;
        PreparedStatement lPStm = null;

        try {
            String lSql = "SELECT * from " + dbBundle.getSchemaMandant() + "tbl_abstimmungsblock where "
                    + "mandant=? AND " + "ident=? ORDER BY ident;";
            lPStm = verbindung.prepareStatement(lSql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            lPStm.setInt(1, dbBundle.clGlobalVar.mandant);
            lPStm.setInt(2, pAbstimmungsblock.ident);

            ResultSet lErgebnis = executeQuery(lPStm);
            lErgebnis.last();
            anzInArray = lErgebnis.getRow();
            lErgebnis.beforeFirst();

            ergebnisArray = new EclAbstimmungsblock[anzInArray];

            int i = 0;
            while (lErgebnis.next() == true) {
                ergebnisArray[i] = this.decodeErgebnis(lErgebnis);
                i++;
            }
            lErgebnis.close();
            lPStm.close();

        } catch (Exception e) {
            CaBug.drucke("DbAbstimmungsblock.read 003");
            System.err.println(" " + e.getMessage());
            return (-1);
        }
        return (anzInArray);
    }

    @Deprecated
    public int read_aktivenAbstimmungsblock() {
        return read_abstimmungsbloeckeNachAktiv(15);
    }

    /**Aktivstatus 1, 2, 3*/
    public int read_anzuzeigendeAbstimmungsbloecke() {
        return read_abstimmungsbloeckeNachAktiv(1);
    }

    /**Aktivstatus 2*/
    public int read_einzusammelndenAbstimmungsblock() {
        return read_abstimmungsbloeckeNachAktiv(2);
    }

    /**Aktivstatus 2, 3*/
    public int read_zuVerarbeitendenAbstimmungsblock() {
        return read_abstimmungsbloeckeNachAktiv(15);
    }

    /**0 = wirklich alle
     * 1 = alle anzuzeigende (also Aktivstatus 1, 2, 3)
     * 2 = nur den einzusammelnden
     * 3 = nur den zu verarbeitenden (nicht einzusammelnden!)
     * 
     * 15 = nur den einzusammelnden oder zu verarbeitenden (d.h. 2 oder 3)
     */
    private int read_abstimmungsbloeckeNachAktiv(int pAktiv) {
        int anzInArray = 0;
        PreparedStatement lPStm = null;

        try {
            String lSql = "SELECT * from " + dbBundle.getSchemaMandant() + "tbl_abstimmungsblock where " + "mandant=? ";
            switch (pAktiv) {
            case 0:
                break;
            case 1:
                lSql = lSql + "AND (aktiv=1 OR aktiv=2 OR aktiv=3)";
                break;
            case 2:
                lSql = lSql + "AND (aktiv=2)";
                break;
            case 3:
                lSql = lSql + "AND (aktiv=3)";
                break;
            case 15:
                lSql = lSql + "AND (aktiv=2 OR aktiv=3)";
                break;
            }
            lSql = lSql + " ORDER BY position;";
            lPStm = verbindung.prepareStatement(lSql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            lPStm.setInt(1, dbBundle.clGlobalVar.mandant);

            ResultSet lErgebnis = executeQuery(lPStm);
            lErgebnis.last();
            anzInArray = lErgebnis.getRow();
            lErgebnis.beforeFirst();

            ergebnisArray = new EclAbstimmungsblock[anzInArray];

            int i = 0;
            while (lErgebnis.next() == true) {
                ergebnisArray[i] = this.decodeErgebnis(lErgebnis);
                i++;
            }
            lErgebnis.close();
            lPStm.close();

        } catch (Exception e) {
            CaBug.drucke("DbAbstimmungsblock.read_aktivenAbstimmungsblock 003");
            System.err.println(" " + e.getMessage());
            return (-1);
        }
        return (anzInArray);

    }

    public int read_all() {
        int anzInArray = 0;
        PreparedStatement lPStm = null;

        try {
            String lSql = "SELECT * from " + dbBundle.getSchemaMandant() + "tbl_abstimmungsblock where " + "mandant=? "
                    + "ORDER BY ident;";
            lPStm = verbindung.prepareStatement(lSql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            lPStm.setInt(1, dbBundle.clGlobalVar.mandant);

            ResultSet lErgebnis = executeQuery(lPStm);
            lErgebnis.last();
            anzInArray = lErgebnis.getRow();
            lErgebnis.beforeFirst();

            ergebnisArray = new EclAbstimmungsblock[anzInArray];

            int i = 0;
            while (lErgebnis.next() == true) {
                ergebnisArray[i] = this.decodeErgebnis(lErgebnis);
                i++;
            }
            lErgebnis.close();
            lPStm.close();

        } catch (Exception e) {
            CaBug.drucke("DbAbstimmungsblock.read_all 003");
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
    public int update(EclAbstimmungsblock pAbstimmungsblock) {

        pAbstimmungsblock.db_version++;

        try {

            String lSql = "UPDATE " + dbBundle.getSchemaMandant() + "tbl_abstimmungsblock SET "
                    + "mandant=?, ident=?, db_version=?, "
                    + "position=?, kurzBeschreibung=?, beschreibung=?, beschreibungEN=?, aktiv=? " + "WHERE "
                    + "ident=? AND " + "db_version=? AND mandant=?";

            PreparedStatement lPStm = verbindung.prepareStatement(lSql);
            fuellePreparedStatementKomplett(lPStm, 1, pAbstimmungsblock);
            lPStm.setInt(anzfelder + 1, pAbstimmungsblock.ident);
            lPStm.setLong(anzfelder + 2, pAbstimmungsblock.db_version - 1);
            lPStm.setLong(anzfelder + 3, dbBundle.clGlobalVar.mandant);

            int ergebnis = executeUpdate(lPStm);
            lPStm.close();
            if (ergebnis == 0) {
                return (CaFehler.pfXyWurdeVonAnderemBenutzerVeraendert);
            }
        } catch (Exception e1) {
            CaBug.drucke("DbAbstimmungsblock.update 001");
            System.err.println(" " + e1.getMessage());
            return (CaFehler.pfdXyBereitsVorhanden);
        }

        return (1);
    }

    /**Update - alle von Status 2/3 auf Status 1 zurücksetzen. Versionsnummer wird nicht berücksichtigt!!!
     * 
     * Feld mandant wird von dieser Funktion immer selbstständig belegt.
     * 
     * Ansonsten: rc=1 => ok, ansonsten Fehler
     */
    public int update_RuecksetzenStatusAufAnzeigen() {

        try {

            String lSql = "UPDATE " + dbBundle.getSchemaMandant() + "tbl_abstimmungsblock SET " + "aktiv=1 " + "WHERE "
                    + "aktiv!=0 AND " + "mandant=?";

            PreparedStatement lPStm = verbindung.prepareStatement(lSql);
            lPStm.setLong(1, dbBundle.clGlobalVar.mandant);

            int ergebnis = executeUpdate(lPStm);
            lPStm.close();
            if (ergebnis == 0) {
                return (CaFehler.pfXyWurdeVonAnderemBenutzerVeraendert);
            }
        } catch (Exception e1) {
            CaBug.drucke("DbAbstimmungsblock.update_RuecksetzenStatusAufAnzeigen 001");
            System.err.println(" " + e1.getMessage());
            return (CaFehler.pfdXyBereitsVorhanden);
        }

        return (1);
    }

    public int delete(int ident) {

        try {

            String sql = "DELETE FROM " + dbBundle.getSchemaMandant()
                    + "tbl_abstimmungsblock WHERE ident=? AND mandant=? ";

            PreparedStatement pstm1 = verbindung.prepareStatement(sql);
            pstm1.setInt(1, ident);
            pstm1.setInt(2, dbBundle.clGlobalVar.mandant);

            int ergebnis1 = executeUpdate(pstm1);
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
