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
import de.meetingapps.meetingportal.meetComEntities.EclAbstimmungZuAbstimmungsblock;

public class DbAbstimmungZuAbstimmungsblock  extends DbRootExecute {

    private Connection verbindung = null;
    private DbBasis dbBasis = null;
    private DbBundle dbBundle = null;

    public EclAbstimmungZuAbstimmungsblock ergebnisArray[] = null;

    //	ALTER TABLE `db_mc168j2018ap`.`tbl_abstimmungzuabstimmungsblock` 
    //	ADD COLUMN `seite` INT(11) NULL DEFAULT NULL AFTER `position`,
    //	DROP PRIMARY KEY,
    //	ADD PRIMARY KEY (`mandant`, `ident`);
    //	;

    /*************************Initialisierung***************************/
    public DbAbstimmungZuAbstimmungsblock(DbBundle pDbBundle) {
        /* Verbindung in lokale Daten eintragen*/
        if (pDbBundle == null) {
            CaBug.drucke("DbAbstimmungZuAbstimmungsblock.init 001 - dbBundle nicht initialisiert");
            return;
        }
        if (pDbBundle.dbBasis == null) {
            CaBug.drucke("DbAbstimmungZuAbstimmungsblock.init 002 - dbBasis nicht initialisiert");
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
    public EclAbstimmungZuAbstimmungsblock[] ergebnis() {
        return ergebnisArray;
    }

    /**********Liefert pN-tes Element des Ergebnisses der read*Methoden**************
     * pN geht von 0 bis anzErgebnis-1*/
    public EclAbstimmungZuAbstimmungsblock ergebnisPosition(int pN) {
        if (ergebnisArray == null) {
            CaBug.drucke("DbAbstimmungZuAbstimmungsblock.ergebnisPosition 001");
            return null;
        }
        if (pN < 0) {
            CaBug.drucke("DbAbstimmungZuAbstimmungsblock.ergebnisPosition 002");
            return null;
        }
        if (pN >= ergebnisArray.length) {
            CaBug.drucke("DbAbstimmungZuAbstimmungsblock.ergebnisPosition 003");
            return null;
        }
        return ergebnisArray[pN];
    }

    public int createTable() {
        int rc = 0;
        DbLowLevel lDbLowLevel = new DbLowLevel(dbBundle);
        rc = lDbLowLevel.createTable("CREATE TABLE " + dbBundle.getSchemaMandant()
                + "tbl_abstimmungzuabstimmungsblock ( " + "`mandant` int(11) NOT NULL, " + "`ident` int(11) NOT NULL, "
                + "`db_version` bigint(20) DEFAULT NULL, " + "`position` int(11) DEFAULT NULL, "
                + "`seite` int(11) DEFAULT NULL, " + "`identAbstimmungsblock` int(11) DEFAULT NULL, "
                + "`identAbstimmung` int(11) DEFAULT NULL, " + "`nummerDerStimmkarte` int(11) DEFAULT NULL, "
                + "`positionAufStimmkarte` int(11) DEFAULT NULL, " + "`positionInAusdruck` int(11) DEFAULT NULL, "
                + "PRIMARY KEY (`ident`,`mandant`) " + ") ");
        return rc;
    }

    /**************************deleteAll Löschen aller Datensätze  eines Mandanten****************/
    public int deleteAll() {
        int erg = 0;

        dbBasis.resetInterneIdentAbstimmungZuAbstimmungsblock();

        try {

            String sql1 = "DELETE FROM " + dbBundle.getSchemaMandant()
                    + "tbl_abstimmungZuAbstimmungsblock where mandant=?;";
            PreparedStatement pstm1 = verbindung.prepareStatement(sql1);
            pstm1.setInt(1, dbBundle.clGlobalVar.mandant);

            erg = 0; /*Falls Duplicate Key, dann wird exception geschmissen und erg wohl nicht gefüllt!*/
            erg = executeUpdate(pstm1);

            pstm1.close();
        } catch (Exception e2) {
            CaBug.drucke("DbAbstimmungZuAbstimmungsblock.deleteAll 001");
            System.err.println(" " + e2.getMessage());
            return (erg);
        }

        return 1;
    }

    /**************************setzt aktuelle Mandantennummer bei allen Datensätzen****************/
    public int updateMandant() {
        return dbBundle.dbLowLevel.rawUpdateMandant(dbBundle.getSchemaMandant() + "tbl_abstimmungZuAbstimmungsblock");
    }

    public void reorgInterneIdent() {
        int lMax;
        lMax = 0;

        PreparedStatement pstm1 = null;
        try {
            int anzInArray;
            String sql = "SELECT MAX(ident) FROM " + dbBundle.getSchemaMandant()
                    + "tbl_abstimmungZuAbstimmungsblock ab where " + "ab.mandant=? ";
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
            CaBug.drucke("DbAbstimmungZuAbstimmungsblock.reorgInterneIdent 001");
            System.err.println(" " + e.getMessage());
        }

        dbBasis.resetInterneIdentAbstimmungZuAbstimmungsblock(lMax);
    }

    /********** dekodiert die aktuelle Position aus ergebnis in EclAktienregisterEintrag und gibt dieses zurück******/
    EclAbstimmungZuAbstimmungsblock decodeErgebnis(ResultSet pErgebnis) {

        EclAbstimmungZuAbstimmungsblock lAbstimmungZuAbstimmungsblock = new EclAbstimmungZuAbstimmungsblock();

        try {
            lAbstimmungZuAbstimmungsblock.mandant = pErgebnis.getInt("abZabb.mandant");
            lAbstimmungZuAbstimmungsblock.ident = pErgebnis.getInt("abZabb.ident");
            lAbstimmungZuAbstimmungsblock.db_version = pErgebnis.getLong("abZabb.db_version");

            lAbstimmungZuAbstimmungsblock.position = pErgebnis.getInt("abZabb.position");
            lAbstimmungZuAbstimmungsblock.seite = pErgebnis.getInt("abZabb.seite");
            lAbstimmungZuAbstimmungsblock.identAbstimmungsblock = pErgebnis.getInt("abZabb.identAbstimmungsblock");
            lAbstimmungZuAbstimmungsblock.identAbstimmung = pErgebnis.getInt("abZabb.identAbstimmung");

            lAbstimmungZuAbstimmungsblock.nummerDerStimmkarte = pErgebnis.getInt("abZabb.nummerDerStimmkarte");
            lAbstimmungZuAbstimmungsblock.positionAufStimmkarte = pErgebnis.getInt("abZabb.positionAufStimmkarte");

            lAbstimmungZuAbstimmungsblock.positionInAusdruck = pErgebnis.getInt("abZabb.positionInAusdruck");
        } catch (Exception e) {
            CaBug.drucke("DbAbstimmungZuAbstimmungsblock.decodeErgebnis 001");
            System.err.println(" " + e.getMessage());
        }

        return lAbstimmungZuAbstimmungsblock;
    }

    /********************* Fuellen Prepared Statement mit allen Feldern, beginnend bei offset.*****************
     * Kann sowohl für Insert, als auch für update verwendet werden.
     * 
     * offset= Startposition des ersten Feldes (also z.B. 1)
     */
    private int anzfelder = 10; /*Anpassen auf Anzahl der Felder pro Datensatz*/

    private void fuellePreparedStatementKomplett(PreparedStatement pPStm, int pOffset,
            EclAbstimmungZuAbstimmungsblock pAbstimmungZuAbstimmungsblock) {

        int startOffset = pOffset; /*Nur erforderlich zum Überprüfen von Programmierfehlern - Feldanzahl*/

        try {
            pPStm.setInt(pOffset, pAbstimmungZuAbstimmungsblock.mandant);
            pOffset++;
            pPStm.setInt(pOffset, pAbstimmungZuAbstimmungsblock.ident);
            pOffset++;
            pPStm.setLong(pOffset, pAbstimmungZuAbstimmungsblock.db_version);
            pOffset++;

            pPStm.setInt(pOffset, pAbstimmungZuAbstimmungsblock.position);
            pOffset++;
            pPStm.setInt(pOffset, pAbstimmungZuAbstimmungsblock.seite);
            pOffset++;
            pPStm.setInt(pOffset, pAbstimmungZuAbstimmungsblock.identAbstimmungsblock);
            pOffset++;
            pPStm.setInt(pOffset, pAbstimmungZuAbstimmungsblock.identAbstimmung);
            pOffset++;

            pPStm.setInt(pOffset, pAbstimmungZuAbstimmungsblock.nummerDerStimmkarte);
            pOffset++;
            pPStm.setInt(pOffset, pAbstimmungZuAbstimmungsblock.positionAufStimmkarte);
            pOffset++;

            pPStm.setInt(pOffset, pAbstimmungZuAbstimmungsblock.positionInAusdruck);
            pOffset++;

            if (pOffset - startOffset != anzfelder) {
                /*Nur wg. Überprüfung auf Programmierfehler - alle Felder berücksichtigt?*/
                CaBug.drucke("DbAbstimmungZuAbstimmungsblock.fuellePreparedStatementKomplett 002");
            }

        } catch (SQLException e) {
            CaBug.drucke("DbAbstimmungZuAbstimmungsblock.fuellePreparedStatementKomplett 001");
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
    public int insert(EclAbstimmungZuAbstimmungsblock pAbstimmungZuAbstimmungsblock) {

        int erg = 0;

        /* Start Transaktion */
        dbBasis.beginTransaction();

        /* neue InterneIdent vergeben */
        erg = dbBasis.getInterneIdentAbstimmungZuAbstimmungsblock();
        if (erg < 1) {
            CaBug.drucke("DbAbstimmungZuAbstimmungsblock.insert 002");
            dbBasis.rollbackTransaction();
            dbBasis.endTransaction();
            return (erg);
        }
        pAbstimmungZuAbstimmungsblock.ident = erg;

        pAbstimmungZuAbstimmungsblock.mandant = dbBundle.clGlobalVar.mandant;

        /* Satz einfügen: 
         * Verarbeitungshinweis: 
         * 	>	nachdem InterneIdent immer eindeutig vergeben werden, ist prinzipiell eine "Doppeleinfügung"
         * 		von InterneIdent nicht möglich. Sollte es dazu kommen, ist das immer ein Programmfehler!
         */

        try {

            /*Felder Neuanlage füllen*/
            String lSql = "INSERT INTO " + dbBundle.getSchemaMandant() + "tbl_abstimmungZuAbstimmungsblock " + "("
                    + "mandant, ident, db_version, " + "position, seite, identAbstimmungsblock, identAbstimmung, "
                    + "nummerDerStimmkarte, positionAufStimmkarte, " + "positionInAusdruck " + ")" + "VALUES ("
                    + "?, ?, ?, " + "?, ?, ?, ?, " + "?, ?, " + "? " + ")";
            PreparedStatement lPStm = verbindung.prepareStatement(lSql, ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);

            fuellePreparedStatementKomplett(lPStm, 1, pAbstimmungZuAbstimmungsblock);

            erg = executeUpdate(lPStm);
            lPStm.close();
        } catch (Exception e2) {
            CaBug.drucke("DbAbstimmungZuAbstimmungsblock.insert 001");
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

    public int read(EclAbstimmungZuAbstimmungsblock pAbstimmungZuAbstimmungsblock) {
        int anzInArray = 0;
        PreparedStatement lPStm = null;

        try {
            String lSql = "SELECT abZabb.* from " + dbBundle.getSchemaMandant()
                    + "tbl_abstimmungZuAbstimmungsblock abZabb where " + "abZabb.mandant=? AND "
                    + "abZabb.ident=? ORDER BY abZabb.ident;";
            lPStm = verbindung.prepareStatement(lSql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            lPStm.setInt(1, dbBundle.clGlobalVar.mandant);
            lPStm.setInt(2, pAbstimmungZuAbstimmungsblock.ident);

            ResultSet lErgebnis = executeQuery(lPStm);
            lErgebnis.last();
            anzInArray = lErgebnis.getRow();
            lErgebnis.beforeFirst();

            ergebnisArray = new EclAbstimmungZuAbstimmungsblock[anzInArray];

            int i = 0;
            while (lErgebnis.next() == true) {
                ergebnisArray[i] = this.decodeErgebnis(lErgebnis);
                i++;
            }
            lErgebnis.close();
            lPStm.close();

        } catch (Exception e) {
            CaBug.drucke("DbAbstimmungZuAbstimmungsblock.read 003");
            System.err.println(" " + e.getMessage());
            return (-1);
        }
        return (anzInArray);
    }

    public int read_all() {
        int anzInArray = 0;
        PreparedStatement lPStm = null;

        try {
            String lSql = "SELECT abZabb.* from " + dbBundle.getSchemaMandant()
                    + "tbl_abstimmungZuAbstimmungsblock abZabb where " + "abZabb.mandant=? ORDER BY abZabb.ident;";
            lPStm = verbindung.prepareStatement(lSql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            lPStm.setInt(1, dbBundle.clGlobalVar.mandant);

            ResultSet lErgebnis = executeQuery(lPStm);
            lErgebnis.last();
            anzInArray = lErgebnis.getRow();
            lErgebnis.beforeFirst();

            ergebnisArray = new EclAbstimmungZuAbstimmungsblock[anzInArray];

            int i = 0;
            while (lErgebnis.next() == true) {
                ergebnisArray[i] = this.decodeErgebnis(lErgebnis);
                i++;
            }
            lErgebnis.close();
            lPStm.close();

        } catch (Exception e) {
            CaBug.drucke("003");
            System.err.println(" " + e.getMessage());
            return (-1);
        }
        return (anzInArray);
    }

    /**sortierung
     * Alt (bis 01.03.2025):
     * =1 => .seite, .position
     * =2 => .nummerDerStimmkarte, .positionAufStimmkarte
     * =3 => .positionInAusdruck, .seite, .position
     * 
     * Neu:
     * 1 = Für Tabletabstimmung, Live-Abstimmung im Portal 
     *      .seite, .position, .nummerDerStimmkarte, .positionAufStimmkarte, .positionInAusdruck
     * 2 = Für Stimmzettel, Stimmkarten
     *      .nummerDerStimmkarte, .positionAufStimmkarte, .seite, .position, .positionInAusdruck
     * 3 = Ausdruck gemäß position in Ausdruck (bzw. Tabletposition)
     *      .positionInAusdruck, .seite, .position, .nummerDerStimmkarte, .positionAufStimmkarte
     * 4 = Ausdruck gemäß position in Ausdruck (bzw. Stimmkartenposition)
     *      .positionInAusdruck, .nummerDerStimmkarte, .positionAufStimmkarte, .seite, .position
     * 
     * Wird immer auch nach abZabb.ident sortiert, um "Zufallsreihenfolgen" auszuschließen.
     */
    public int read_zuAbstimmungsblock(int pAbstimmungsBlock, int pSortierung) {
        int anzInArray = 0;
        PreparedStatement lPStm = null;

        try {
            String lSql = "SELECT abZabb.* from " + dbBundle.getSchemaMandant()
                    + "tbl_abstimmungZuAbstimmungsblock abZabb where " + "abZabb.mandant=? AND "
                    + "abZabb.identAbstimmungsblock=? ";
            switch (pSortierung) {
            case 1: 
                lSql = lSql + "ORDER BY abZabb.seite, abZabb.position, abZabb.nummerDerStimmkarte, abZabb.positionAufStimmkarte, abZabb.positionInAusdruck, abZabb.ident;";
                break;
            case 2: 
                lSql = lSql + "ORDER BY abZabb.nummerDerStimmkarte, abZabb.positionAufStimmkarte, abZabb.seite, abZabb.position, abZabb.positionInAusdruck, abZabb.ident;";
                break;
            case 3: 
                lSql = lSql + "ORDER BY abZabb.positionInAusdruck, abZabb.seite, abZabb.position, abZabb.nummerDerStimmkarte, abZabb.positionAufStimmkarte, abZabb.ident;";
                break;
            case 4: 
                lSql = lSql + "ORDER BY abZabb.positionInAusdruck, abZabb.nummerDerStimmkarte, abZabb.positionAufStimmkarte, abZabb.seite, abZabb.position, abZabb.ident;";
                break;
            }
            lPStm = verbindung.prepareStatement(lSql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            lPStm.setInt(1, dbBundle.clGlobalVar.mandant);
            lPStm.setInt(2, pAbstimmungsBlock);

            ResultSet lErgebnis = executeQuery(lPStm);
            lErgebnis.last();
            anzInArray = lErgebnis.getRow();
            lErgebnis.beforeFirst();

            ergebnisArray = new EclAbstimmungZuAbstimmungsblock[anzInArray];

            int i = 0;
            while (lErgebnis.next() == true) {
                ergebnisArray[i] = this.decodeErgebnis(lErgebnis);
                i++;
            }
            lErgebnis.close();
            lPStm.close();

        } catch (Exception e) {
            CaBug.drucke("DbAbstimmungZuAbstimmungsblock.read_zuAbstimmungsblock 003");
            System.err.println(" " + e.getMessage());
            return (-1);
        }
        return (anzInArray);
    }

    public int read_zuZuIdentAbstimmung(int pIdentAbstimmung) {
        int anzInArray = 0;
        PreparedStatement lPStm = null;

        try {
            String lSql = "SELECT abZabb.* from " + dbBundle.getSchemaMandant()
                    + "tbl_abstimmungZuAbstimmungsblock abZabb where " + "abZabb.mandant=? AND "
                    + "abZabb.identAbstimmung=? ORDER BY abZabb.position;";
            lPStm = verbindung.prepareStatement(lSql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            lPStm.setInt(1, dbBundle.clGlobalVar.mandant);
            lPStm.setInt(2, pIdentAbstimmung);

            ResultSet lErgebnis = executeQuery(lPStm);
            lErgebnis.last();
            anzInArray = lErgebnis.getRow();
            lErgebnis.beforeFirst();

            ergebnisArray = new EclAbstimmungZuAbstimmungsblock[anzInArray];

            int i = 0;
            while (lErgebnis.next() == true) {
                ergebnisArray[i] = this.decodeErgebnis(lErgebnis);
                i++;
            }
            lErgebnis.close();
            lPStm.close();

        } catch (Exception e) {
            CaBug.drucke("DbAbstimmungZuAbstimmungsblock.read_zuZuIdentAbstimmung 003");
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
    public int update(EclAbstimmungZuAbstimmungsblock pAbstimmungZuAbstimmungsblock) {

        pAbstimmungZuAbstimmungsblock.db_version++;

        try {

            String lSql = "UPDATE " + dbBundle.getSchemaMandant() + "tbl_abstimmungZuAbstimmungsblock SET "
                    + "mandant=?, ident=?, db_version=?, "
                    + "position=?, seite=?, identAbstimmungsblock=?, identAbstimmung=?, "
                    + "nummerDerStimmkarte=?, positionAufStimmkarte=?, " + "positionInAusdruck=? " + "WHERE "
                    + "ident=? AND " + "db_version=? AND mandant=?";

            PreparedStatement lPStm = verbindung.prepareStatement(lSql);
            fuellePreparedStatementKomplett(lPStm, 1, pAbstimmungZuAbstimmungsblock);
            lPStm.setInt(anzfelder + 1, pAbstimmungZuAbstimmungsblock.ident);
            lPStm.setLong(anzfelder + 2, pAbstimmungZuAbstimmungsblock.db_version - 1);
            lPStm.setLong(anzfelder + 3, dbBundle.clGlobalVar.mandant);

            int ergebnis = executeUpdate(lPStm);
            lPStm.close();
            if (ergebnis == 0) {
                return (CaFehler.pfXyWurdeVonAnderemBenutzerVeraendert);
            }
        } catch (Exception e1) {
            CaBug.drucke("DbAbstimmungZuAbstimmungsblock.update 001");
            System.err.println(" " + e1.getMessage());
            return (CaFehler.pfdXyBereitsVorhanden);
        }

        return (1);
    }

    public int delete(int ident) {

        try {

            String sql = "DELETE FROM " + dbBundle.getSchemaMandant()
                    + "tbl_abstimmungZuAbstimmungsblock WHERE ident=? AND mandant=? ";

            PreparedStatement pstm1 = verbindung.prepareStatement(sql);
            pstm1.setInt(1, ident);
            pstm1.setInt(2, dbBundle.clGlobalVar.mandant);

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
