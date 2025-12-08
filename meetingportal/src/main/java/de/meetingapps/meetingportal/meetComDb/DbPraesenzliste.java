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
import de.meetingapps.meetingportal.meetComEntities.EclPraesenzliste;
import de.meetingapps.meetingportal.meetComEntities.EclWillenserklaerung;

public class DbPraesenzliste {

    private Connection verbindung = null;
    private DbBasis dbBasis = null;
    private DbBundle dbBundle = null;

    private EclPraesenzliste ergebnisArray[] = null;

    /*************************Initialisierung***************************/
    public DbPraesenzliste(DbBundle pDbBundle) {
        /* Verbindung in lokale Daten eintragen*/
        if (pDbBundle == null) {
            CaBug.drucke("DbPraesenzliste.init 001 - dbBundle nicht initialisiert");
            return;
        }
        if (pDbBundle.dbBasis == null) {
            CaBug.drucke("DbPraesenzliste.init 002 - dbBasis nicht initialisiert");
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
    public EclPraesenzliste ergebnisPosition(int pN) {
        if (ergebnisArray == null) {
            CaBug.drucke("DbPraesenzliste.ergebnisPosition 001");
            return null;
        }
        if (pN < 0) {
            CaBug.drucke("DbPraesenzliste.ergebnisPosition 002");
            return null;
        }
        if (pN >= ergebnisArray.length) {
            CaBug.drucke("DbPraesenzliste.ergebnisPosition 003");
            return null;
        }
        return ergebnisArray[pN];
    }

    /**Liefert Array zurücl***********/
    public EclPraesenzliste[] ergebnis() {
        return ergebnisArray;
    }

    public int createTable() {
        int rc = 0;
        DbLowLevel lDbLowLevel = new DbLowLevel(dbBundle);
        rc = lDbLowLevel.createTable("CREATE TABLE " + dbBundle.getSchemaMandant() + "tbl_praesenzliste ( "
                + "`mandant` int(11) DEFAULT NULL, " + "`db_version` bigint(20) DEFAULT NULL, "
                + "`drucken` int(11) DEFAULT NULL, " + "`willenserklaerungIdent` int(11) DEFAULT NULL, "
                + "`meldeIdentAktionaer` int(11) DEFAULT NULL, " + "`meldungstyp` int(11) DEFAULT NULL, "
                + "`meldungSkOffenlegung` int(11) DEFAULT NULL, " + "`zutrittsIdent` varchar(10) DEFAULT NULL, "
                + "`stimmkarte` varchar(10) DEFAULT NULL, " + "`aktionaerName` varchar(80) DEFAULT NULL, "
                + "`aktionaerVorname` varchar(80) DEFAULT NULL, " + "`aktionaerOrt` varchar(80) DEFAULT NULL, "
                + "`aktien` bigint(20) DEFAULT NULL, " + "`stimmen` bigint(20) DEFAULT NULL, "
                + "`besitzartKuerzel` char(10) DEFAULT NULL, " + "`gattung` int(11) DEFAULT NULL, "
                + "`willenserklaerung` int(11) DEFAULT NULL, " + "`vertreterIdent` int(11) DEFAULT NULL, "
                + "`vertreterName` varchar(80) DEFAULT NULL, " + "`vertreterVorname` varchar(80) DEFAULT NULL, "
                + "`vertreterOrt` varchar(80) DEFAULT NULL, " + "`sammelkartenIdent` int(11) DEFAULT NULL, "
                + "`sammelkartenName` varchar(80) DEFAULT NULL, " + "`sammelkartenVorname` varchar(80) DEFAULT NULL, "
                + "`sammelkartenOrt` varchar(80) DEFAULT NULL, " + "`skOffenlegung` int(11) DEFAULT NULL, "
                + "`sortierName` varchar(80) DEFAULT NULL, "
                + "KEY `IDX_willenserklaerung` (`willenserklaerungIdent`,`mandant`) " + ") ");
        return rc;
    }

    /**************************deleteAll Löschen aller Datensätze  eines Mandanten****************/
    public int deleteAll() {
        return dbBundle.dbLowLevel
                .deleteMandant("DELETE FROM " + dbBundle.getSchemaMandant() + "tbl_praesenzliste where mandant=?");
    }

    /**************************setzt aktuelle Mandantennummer bei allen Datensätzen****************/
    public int updateMandant() {
        return dbBundle.dbLowLevel.rawUpdateMandant(dbBundle.getSchemaMandant() + "tbl_praesenzliste");
    }

    /********** dekodiert die aktuelle Position aus ergebnis in EclPraesenzliste und gibt dieses zurück******/
    private EclPraesenzliste decodeErgebnis(ResultSet pErgebnis) {

        EclPraesenzliste lPraesenzliste = new EclPraesenzliste();

        try {

            lPraesenzliste.mandant = pErgebnis.getInt("mandant");
            lPraesenzliste.db_version = pErgebnis.getLong("db_version");

            lPraesenzliste.drucken = pErgebnis.getInt("drucken");
            lPraesenzliste.willenserklaerungIdent = pErgebnis.getInt("willenserklaerungIdent");
            lPraesenzliste.meldeIdentAktionaer = pErgebnis.getInt("meldeIdentAktionaer");
            lPraesenzliste.meldungstyp = pErgebnis.getInt("meldungstyp");
            lPraesenzliste.meldungSkOffenlegung = pErgebnis.getInt("meldungSkOffenlegung");
            lPraesenzliste.zutrittsIdent = pErgebnis.getString("zutrittsIdent");
            lPraesenzliste.stimmkarte = pErgebnis.getString("stimmkarte");
            lPraesenzliste.aktionaerName = pErgebnis.getString("aktionaerName");
            lPraesenzliste.aktionaerVorname = pErgebnis.getString("aktionaerVorname");
            lPraesenzliste.aktionaerOrt = pErgebnis.getString("aktionaerOrt");

            lPraesenzliste.aktien = pErgebnis.getLong("aktien");
            lPraesenzliste.stimmen = pErgebnis.getLong("stimmen");
            lPraesenzliste.besitzartKuerzel = pErgebnis.getString("besitzartKuerzel");
            lPraesenzliste.gattung = pErgebnis.getInt("gattung");

            lPraesenzliste.willenserklaerung = pErgebnis.getInt("willenserklaerung");

            lPraesenzliste.vertreterIdent = pErgebnis.getInt("vertreterIdent");
            lPraesenzliste.vertreterName = pErgebnis.getString("vertreterName");
            lPraesenzliste.vertreterVorname = pErgebnis.getString("vertreterVorname");
            lPraesenzliste.vertreterOrt = pErgebnis.getString("vertreterOrt");

            lPraesenzliste.sammelkartenIdent = pErgebnis.getInt("sammelkartenIdent");
            lPraesenzliste.sammelkartenName = pErgebnis.getString("sammelkartenName");
            lPraesenzliste.sammelkartenVorname = pErgebnis.getString("sammelkartenVorname");
            lPraesenzliste.sammelkartenOrt = pErgebnis.getString("sammelkartenOrt");
            lPraesenzliste.skOffenlegung = pErgebnis.getInt("skOffenlegung");

            lPraesenzliste.sortierName = pErgebnis.getString("sortierName");

        } catch (Exception e) {
            CaBug.drucke("DbPraesenzliste.decodeErgebnis 001");
            System.err.println(" " + e.getMessage());
        }

        return lPraesenzliste;
    }

    /********************* Fuellen Prepared Statement mit allen Feldern, beginnend bei offset.*****************
     * Kann sowohl für Insert, als auch für update verwendet werden.
     * 
     * offset= Startposition des ersten Feldes (also z.B. 1)
     */
    private int anzfelder = 27; /*Anpassen auf Anzahl der Felder pro Datensatz*/

    private void fuellePreparedStatementKomplett(PreparedStatement pPStm, int pOffset,
            EclPraesenzliste pPraesenzliste) {

        int startOffset = pOffset; /*Nur erforderlich zum Überprüfen von Programmierfehlern - Feldanzahl*/

        try {
            pPStm.setInt(pOffset, pPraesenzliste.mandant);
            pOffset++;
            pPStm.setLong(pOffset, pPraesenzliste.db_version);
            pOffset++;

            pPStm.setInt(pOffset, pPraesenzliste.drucken);
            pOffset++;
            pPStm.setInt(pOffset, pPraesenzliste.willenserklaerungIdent);
            pOffset++;
            pPStm.setInt(pOffset, pPraesenzliste.meldeIdentAktionaer);
            pOffset++;
            pPStm.setInt(pOffset, pPraesenzliste.meldungstyp);
            pOffset++;

            pPStm.setInt(pOffset, pPraesenzliste.meldungSkOffenlegung);
            pOffset++;
            pPStm.setString(pOffset, pPraesenzliste.zutrittsIdent);
            pOffset++;
            pPStm.setString(pOffset, pPraesenzliste.stimmkarte);
            pOffset++;
            pPStm.setString(pOffset, pPraesenzliste.aktionaerName);
            pOffset++;
            pPStm.setString(pOffset, pPraesenzliste.aktionaerVorname);
            pOffset++;
            pPStm.setString(pOffset, pPraesenzliste.aktionaerOrt);
            pOffset++;

            pPStm.setLong(pOffset, pPraesenzliste.aktien);
            pOffset++;
            pPStm.setLong(pOffset, pPraesenzliste.stimmen);
            pOffset++;
            pPStm.setString(pOffset, pPraesenzliste.besitzartKuerzel);
            pOffset++;
            pPStm.setInt(pOffset, pPraesenzliste.gattung);
            pOffset++;

            pPStm.setInt(pOffset, pPraesenzliste.willenserklaerung);
            pOffset++;

            pPStm.setInt(pOffset, pPraesenzliste.vertreterIdent);
            pOffset++;
            pPStm.setString(pOffset, pPraesenzliste.vertreterName);
            pOffset++;
            pPStm.setString(pOffset, pPraesenzliste.vertreterVorname);
            pOffset++;
            pPStm.setString(pOffset, pPraesenzliste.vertreterOrt);
            pOffset++;

            pPStm.setInt(pOffset, pPraesenzliste.sammelkartenIdent);
            pOffset++;
            pPStm.setString(pOffset, pPraesenzliste.sammelkartenName);
            pOffset++;
            pPStm.setString(pOffset, pPraesenzliste.sammelkartenVorname);
            pOffset++;
            pPStm.setString(pOffset, pPraesenzliste.sammelkartenOrt);
            pOffset++;
            pPStm.setInt(pOffset, pPraesenzliste.skOffenlegung);
            pOffset++;

            pPStm.setString(pOffset, pPraesenzliste.sortierName);
            pOffset++;

            if (pOffset - startOffset != anzfelder) {
                /*Nur wg. Überprüfung auf Programmierfehler - alle Felder berücksichtigt?*/
                CaBug.drucke("DbPraesenzliste.fuellePreparedStatementKomplett 002");
            }

        } catch (SQLException e) {
            CaBug.drucke("DbPraesenzliste.fuellePreparedStatementKomplett 001");
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
    public int insert(EclPraesenzliste pPraesenzliste) {

        int erg = 0;

        /* Start Transaktion */
        dbBasis.beginTransaction();

        /* neue InterneIdent vergeben */
        /*	In diesem Fall nicht benötigt. Ist nur enthalten wg. "Blaupausenfunktionalität"
        erg=dbBasis.getInterneIdentAktienregisterEintrag();
        if (erg<1){
        	CaBug.drucke("DbPraesenzliste.insert 002");
        	dbBasis.rollbackTransaction();dbBasis.endTransaction();return (erg);
        }
        
        lAktienregisterZusatz.aktienregisterIdent=erg;
        
        */

        pPraesenzliste.mandant = dbBundle.clGlobalVar.mandant;

        /* Satz einfügen: 
         * Verarbeitungshinweis: 
         * 	>	nachdem InterneIdent immer eindeutig vergeben werden, ist prinzipiell eine "Doppeleinfügung"
         * 		von InterneIdent nicht möglich. Sollte es dazu kommen, ist das immer ein Programmfehler!
         */

        try {

            /*Felder Neuanlage füllen*/
            String lSql = "INSERT INTO " + dbBundle.getSchemaMandant() + "tbl_praesenzliste " + "("
                    + "mandant, db_version, " + "drucken, willenserklaerungIdent, meldeIdentAktionaer, meldungstyp, "
                    + "meldungSkOffenlegung, zutrittsIdent, stimmkarte, aktionaerName, aktionaerVorname, aktionaerOrt, "
                    + "aktien, stimmen, besitzartKuerzel, gattung, " + "willenserklaerung, "
                    + "vertreterIdent, vertreterName, vertreterVorname, vertreterOrt, "
                    + "sammelkartenIdent, sammelkartenName, sammelkartenVorname, sammelkartenOrt, skOffenlegung, "
                    + "sortierName" + ")" + "VALUES (" + "?, ?, " + "?, ?, ?, ?, " + "?, ?, ?, ?, ?, ?, "
                    + "?, ?, ?, ?, " + "?, " + "?, ?, ?, ?, " + "?, ?, ?, ?, ?," + "?" + ")";
            PreparedStatement lPStm = verbindung.prepareStatement(lSql, ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);

            fuellePreparedStatementKomplett(lPStm, 1, pPraesenzliste);

            erg = lPStm.executeUpdate();
            lPStm.close();
        } catch (Exception e2) {
            CaBug.drucke("DbPraesenzliste.insert 001");
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

    /**Überprüfen, ob insert möglich ist (intern: es wird ein read-Befehl abgesetzt), oder mittlerweile bereits ein 
     * Satz vorhanden ist (dient dem Multiuser-Betrieb ...)
     * Gefüllt sein muss: der Primary Key (in diesem Fall: aktienregisterIdent; mandant wird automatisch gefüllt)
     * 
     * true=insert ist noch möglich
     * false=mittlerweile existiert ein Satz mit diesem Primary Key, oder es sit ein Fehler aufgetreten
     * */
    public boolean insertCheck(EclPraesenzliste pPraesenzliste) {
        int anzInArray = 0;
        PreparedStatement lPStm = null;

        if (pPraesenzliste == null) {
            CaBug.drucke("DbPraesenzliste.insertCheck 001");
            return false;
        }

        try {
            String lSql = "SELECT * from " + dbBundle.getSchemaMandant() + "tbl_praesenzliste where " + "mandant=? AND "
                    + "willenserklaerungIdent=? ORDER BY willenserklaerungIdent;";
            lPStm = verbindung.prepareStatement(lSql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            lPStm.setInt(1, dbBundle.clGlobalVar.mandant);
            lPStm.setInt(2, pPraesenzliste.willenserklaerungIdent);

            ResultSet lErgebnis = lPStm.executeQuery();
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
            CaBug.drucke("DbPraesenzliste.insertCheck 003");
            System.err.println(" " + e.getMessage());
            return (false);
        }

    }

    /**Realisierte Selektion derzeit, jeweils einzeln: 
     * willenserklaerungIdent
     * meldeIdentAktionaer
     * 
     * Return-Wert:
     * >=1 => Anzahl der gefundenen Datensätze
     * =0 => nix gefunden
     * <0 => Fehler
     * */
    public int read(EclPraesenzliste pPraesenzliste) {
        int anzInArray = 0;
        PreparedStatement lPStm = null;

        if (pPraesenzliste == null) {
            CaBug.drucke("DbPraesenzliste.read 001");
            return -1;
        }

        String lSql = "";
        try {
            if (pPraesenzliste.willenserklaerungIdent != 0) {
                lSql = "SELECT * from " + dbBundle.getSchemaMandant() + "tbl_praesenzliste where " + "mandant=? AND "
                        + "willenserklaerungIdent=? ORDER BY willenserklaerungIdent;";
                lPStm = verbindung.prepareStatement(lSql, ResultSet.TYPE_SCROLL_INSENSITIVE,
                        ResultSet.CONCUR_READ_ONLY);
                lPStm.setInt(1, dbBundle.clGlobalVar.mandant);
                lPStm.setInt(2, pPraesenzliste.willenserklaerungIdent);
            }
            if (pPraesenzliste.meldeIdentAktionaer != 0) {
                lSql = "SELECT * from " + dbBundle.getSchemaMandant() + "tbl_praesenzliste where " + "mandant=? AND "
                        + "meldeIdentAktionaer=? ORDER BY willenserklaerungIdent;";
                lPStm = verbindung.prepareStatement(lSql, ResultSet.TYPE_SCROLL_INSENSITIVE,
                        ResultSet.CONCUR_READ_ONLY);
                lPStm.setInt(1, dbBundle.clGlobalVar.mandant);
                lPStm.setInt(2, pPraesenzliste.meldeIdentAktionaer);
            }

            ResultSet lErgebnis = lPStm.executeQuery();
            lErgebnis.last();
            anzInArray = lErgebnis.getRow();
            lErgebnis.beforeFirst();

            ergebnisArray = new EclPraesenzliste[anzInArray];

            int i = 0;
            while (lErgebnis.next() == true) {
                ergebnisArray[i] = this.decodeErgebnis(lErgebnis);
                i++;
            }
            lErgebnis.close();
            lPStm.close();

        } catch (Exception e) {
            CaBug.drucke("DbPraesenzliste.read 003");
            System.err.println(" " + e.getMessage());
            return (-1);
        }
        return (anzInArray);
    }

    public int read_all() {
        int anzInArray = 0;
        PreparedStatement lPStm = null;

        try {
            String lSql = "SELECT * from " + dbBundle.getSchemaMandant() + "tbl_praesenzliste where " + "mandant=? "
                    + "ORDER BY willenserklaerungIdent;";
            lPStm = verbindung.prepareStatement(lSql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            lPStm.setInt(1, dbBundle.clGlobalVar.mandant);

            ResultSet lErgebnis = lPStm.executeQuery();
            lErgebnis.last();
            anzInArray = lErgebnis.getRow();
            lErgebnis.beforeFirst();

            ergebnisArray = new EclPraesenzliste[anzInArray];

            int i = 0;
            while (lErgebnis.next() == true) {
                ergebnisArray[i] = this.decodeErgebnis(lErgebnis);
                i++;
            }
            lErgebnis.close();
            lPStm.close();

        } catch (Exception e) {
            CaBug.drucke("DbPraesenzliste.read_all 003");
            System.err.println(" " + e.getMessage());
            return (-1);
        }
        return (anzInArray);
    }

    /**Update einer Willenserklärung. Versionsnummer wird um 1 hochgezählt
     */
    public int delete(EclWillenserklaerung lWillenserklaerung) {

        try {

            String sql = "DELETE FROM " + dbBundle.getSchemaMandant()
                    + "tbl_praesenzliste WHERE willenserklaerungIdent=? AND mandant=? ";

            PreparedStatement pstm1 = verbindung.prepareStatement(sql);
            pstm1.setInt(1, lWillenserklaerung.willenserklaerungIdent);
            pstm1.setInt(2, dbBundle.clGlobalVar.mandant);

            int ergebnis1 = pstm1.executeUpdate();
            pstm1.close();
            if (ergebnis1 == 0) {
                return (CaFehler.pfXyWurdeVonAnderemBenutzerVeraendert);
            }
        } catch (Exception e1) {
            CaBug.drucke("DbPraesenzliste.delete 001");
            System.err.println(" " + e1.getMessage());
            return (CaFehler.pfdXyBereitsVorhanden);

        }

        return (1);
    }

    /**Update. Versionsnummer wird um 1 hochgezählt
     * 
     * Feld mandant wird von dieser Funktion immer selbstständig belegt.
     * 
     * Zum Sicherstellen der Multiuserfähigkeit muß in jedem Fall der rc CaFehler.pfXyWurdeVonAnderemBenutzerVeraendert
     * nach Aufruf dieser Funktion abgefangen werden.
     * Ansonsten: rc=1 => ok, ansonsten Fehler
     */
    public int update(EclPraesenzliste pPraesenzliste) {

        pPraesenzliste.db_version++;

        try {

            String lSql = "UPDATE " + dbBundle.getSchemaMandant() + "tbl_praesenzliste SET "
                    + "mandant=?, db_version=?, "
                    + "drucken=?, willenserklaerungIdent=?, meldeIdentAktionaer=?, meldungstyp=?, "
                    + "meldungSkOffenlegung=?, zutrittsIdent=?, stimmkarte=?, aktionaerName=?, aktionaerVorname=?, aktionaerOrt=?, "
                    + "aktien=?, stimmen=?, besitzartKuerzel=?, gattung=?, " + "willenserklaerung=?, "
                    + "vertreterIdent=?, vertreterName=?, vertreterVorname=?, vertreterOrt=?, "
                    + "sammelkartenIdent=?, sammelkartenName=?, sammelkartenVorname=?, sammelkartenOrt=?, skOffenlegung=?, "
                    + "sortierName=?" + "WHERE " + "willenserklaerungIdent=? AND " + "db_version=? AND mandant=?";

            PreparedStatement lPStm = verbindung.prepareStatement(lSql);
            fuellePreparedStatementKomplett(lPStm, 1, pPraesenzliste);
            lPStm.setInt(anzfelder + 1, pPraesenzliste.willenserklaerungIdent);
            lPStm.setLong(anzfelder + 2, pPraesenzliste.db_version - 1);
            lPStm.setLong(anzfelder + 3, dbBundle.clGlobalVar.mandant);

            int ergebnis = lPStm.executeUpdate();
            lPStm.close();
            if (ergebnis == 0) {
                return (CaFehler.pfXyWurdeVonAnderemBenutzerVeraendert);
            }
        } catch (Exception e1) {
            CaBug.drucke("DbPraesenzliste.update 001");
            System.err.println(" " + e1.getMessage());
            return (CaFehler.pfdXyBereitsVorhanden);
        }

        return (1);
    }

    /**Überprüfen, ob update möglich ist (intern: es wird ein read-Befehl abgesetzt), oder mittlerweile bereits der 
     * Satz geändert wurde (dient dem Multiuser-Betrieb ...)
     * Gefüllt sein muss: der Primary Key (in diesem Fall: aktienregisterIdent; mandant wird automatisch gefüllt),
     * sowie db_version
     * 
     * true=update ist noch möglich
     * false=mittlerweile wurde der Satz verändert, oder es ist ein Fehler aufgetreten
     * */
    public boolean updateCheck(EclPraesenzliste pPraesenzliste) {
        int anzInArray = 0;
        PreparedStatement lPStm = null;

        if (pPraesenzliste == null) {
            CaBug.drucke("DbPraesenzliste.updateCheck 001");
            return false;
        }

        try {
            String lSql = "SELECT * from " + dbBundle.getSchemaMandant() + "tbl_praesenzliste where " + "mandant=? AND "
                    + "willenserklaerungIdent=? and db_version=? ORDER BY willenserklaerungIdent;";
            lPStm = verbindung.prepareStatement(lSql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            lPStm.setInt(1, dbBundle.clGlobalVar.mandant);
            lPStm.setInt(2, pPraesenzliste.willenserklaerungIdent);
            lPStm.setLong(3, pPraesenzliste.db_version);

            ResultSet lErgebnis = lPStm.executeQuery();
            lErgebnis.last();
            anzInArray = lErgebnis.getRow();
            lErgebnis.beforeFirst();

            lErgebnis.close();
            lPStm.close();

            if (anzInArray > 0) {
                return true;
            } else {
                return false;
            }

        } catch (Exception e) {
            CaBug.drucke("DbPraesenzliste.updateCheck 003");
            System.err.println(" " + e.getMessage());
            return (false);
        }

    }

}
