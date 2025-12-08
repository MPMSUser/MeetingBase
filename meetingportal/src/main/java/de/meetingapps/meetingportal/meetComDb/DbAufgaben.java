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
import de.meetingapps.meetingportal.meetComAllg.CaDatumZeit;
import de.meetingapps.meetingportal.meetComAllg.CaFehler;
import de.meetingapps.meetingportal.meetComEntities.EclAufgaben;
import de.meetingapps.meetingportal.meetComKonst.KonstAufgabenStatus;

public class DbAufgaben  extends DbRootExecute {

    private Connection verbindung = null;
    private DbBundle dbBundle = null;

    public EclAufgaben ergebnisArray[] = null;

    /*************************Initialisierung***************************/
    public DbAufgaben(DbBundle pDbBundle) {
        /* Verbindung in lokale Daten eintragen*/
        if (pDbBundle == null) {
            CaBug.drucke("DbAufgaben.init 001 - dbBundle nicht initialisiert");
            return;
        }
        if (pDbBundle.dbBasis == null) {
            CaBug.drucke("DbAufgaben.init 002 - dbBasis nicht initialisiert");
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
    public EclAufgaben ergebnisPosition(int pN) {
        if (ergebnisArray == null) {
            CaBug.drucke("DbAufgaben.ergebnisPosition 001");
            return null;
        }
        if (pN < 0) {
            CaBug.drucke("DbAufgaben.ergebnisPosition 002");
            return null;
        }
        if (pN >= ergebnisArray.length) {
            CaBug.drucke("DbAufgaben.ergebnisPosition 003");
            return null;
        }
        return ergebnisArray[pN];
    }

    /**************************deleteAll Löschen aller Datensätze  eines Mandanten****************/
    public int deleteAll() {
        return dbBundle.dbLowLevel
                .deleteMandant("DELETE FROM " + dbBundle.getSchemaAllgemein() + "tbl_aufgaben where mandant=?;");
    }

    /*******Checken, ob table überhaupt vorhanden ist***************************/
    public boolean checkTableVorhanden() {
        DbLowLevel lDbLowLevel = new DbLowLevel(dbBundle);
        return lDbLowLevel.checkTableVorhanden("SELECT * from tbl_aufgaben WHERE mandant=0; ");
    }

    /************Neuanlegen Table******************************/
    public int createTable() {
        int rc = 0;
        DbLowLevel lDbLowLevel = new DbLowLevel(dbBundle);
        rc = lDbLowLevel.createTable("CREATE TABLE " + dbBundle.getSchemaAllgemein() + "tbl_aufgaben ( "
                + "`mandant` int(11) NOT NULL, " + "`identAufgabe` int(11) NOT NULL, "
                + "`db_version` bigint(20) DEFAULT NULL, " + "`aufgabe` int(11) NOT NULL, "
                + "`freitextBeschreibung` varchar(1000) DEFAULT NULL, " + "`zeitpunktErteilt` char(19) DEFAULT NULL, "
                + "`anforderer` int(11) NOT NULL, " + "`angefordertVonBemerkung` varchar(1000) DEFAULT NULL, "
                + "`userNummerErfassung` int(11) NOT NULL, " + "`status` int(11) NOT NULL, "
                + "`argument0` varchar(200) DEFAULT NULL, " + "`argument1` varchar(200) DEFAULT NULL, "
                + "`argument2` varchar(200) DEFAULT NULL, " + "`argument3` varchar(200) DEFAULT NULL, "
                + "`argument4` varchar(200) DEFAULT NULL, " + "`argument5` varchar(200) DEFAULT NULL, "
                + "`argument6` varchar(200) DEFAULT NULL, " + "`argument7` varchar(200) DEFAULT NULL, "
                + "`argument8` varchar(200) DEFAULT NULL, " + "`argument9` varchar(200) DEFAULT NULL, "
                + "`erledigtVermerk` int(11) NOT NULL, " + "`erledigtBemerkung` varchar(1000) DEFAULT NULL, "
                + "`zeitpunktErledigt` char(19) DEFAULT NULL, " + "`UserNummerVerarbeitet` int(11) NOT NULL, "
                + "`drucklaufNr` int(11) DEFAULT '0', " + "PRIMARY KEY (`identAufgabe`) " + ") ");
        System.out.println("dbaufgaben create table rc=" + rc);
        return rc;
    }

    /********** dekodiert die aktuelle Position aus ergebnis in EclAktienregisterEintrag und gibt dieses zurück******/
    EclAufgaben decodeErgebnis(ResultSet pErgebnis) {

        EclAufgaben lAufgabe = new EclAufgaben();

        try {
            lAufgabe.mandant = pErgebnis.getInt("auf.mandant");
            lAufgabe.identAufgabe = pErgebnis.getInt("auf.identAufgabe");
            lAufgabe.db_version = pErgebnis.getLong("auf.db_version");

            lAufgabe.aufgabe = pErgebnis.getInt("auf.aufgabe");
            lAufgabe.freitextBeschreibung = pErgebnis.getString("auf.freitextBeschreibung");

            lAufgabe.zeitpunktErteilt = pErgebnis.getString("auf.zeitpunktErteilt");
            lAufgabe.anforderer = pErgebnis.getInt("auf.anforderer");
            lAufgabe.angefordertVonBemerkung = pErgebnis.getString("auf.angefordertVonBemerkung");

            lAufgabe.userNummerErfassung = pErgebnis.getInt("auf.userNummerErfassung");
            lAufgabe.status = pErgebnis.getInt("auf.status");

            for (int i = 0; i < 10; i++) {
                lAufgabe.argument[i] = pErgebnis.getString("auf.argument" + Integer.toString(i));
            }

            lAufgabe.erledigtVermerk = pErgebnis.getInt("auf.erledigtVermerk");
            lAufgabe.erledigtBemerkung = pErgebnis.getString("auf.erledigtBemerkung");
            lAufgabe.zeitpunktErledigt = pErgebnis.getString("auf.zeitpunktErledigt");

            lAufgabe.userNummerVerarbeitet = pErgebnis.getInt("auf.userNummerVerarbeitet");
            lAufgabe.drucklaufNr = pErgebnis.getInt("auf.drucklaufNr");

        } catch (Exception e) {
            CaBug.drucke("DbAufgaben.decodeErgebnis 001");
            System.err.println(" " + e.getMessage());
        }

        return lAufgabe;
    }

    /********************* Fuellen Prepared Statement mit allen Feldern, beginnend bei offset.*****************
     * Kann sowohl für Insert, als auch für update verwendet werden.
     * 
     * offset= Startposition des ersten Feldes (also z.B. 1)
     */
    private int anzfelder = 25; /*Anpassen auf Anzahl der Felder pro Datensatz*/

    void fuellePreparedStatementKomplett(PreparedStatement pPStm, int pOffset, EclAufgaben pAufgabe) {

        int startOffset = pOffset; /*Nur erforderlich zum Überprüfen von Programmierfehlern - Feldanzahl*/

        try {
            pPStm.setInt(pOffset, pAufgabe.mandant);
            pOffset++;
            pPStm.setInt(pOffset, pAufgabe.identAufgabe);
            pOffset++;
            pPStm.setLong(pOffset, pAufgabe.db_version);
            pOffset++;

            pPStm.setInt(pOffset, pAufgabe.aufgabe);
            pOffset++;
            pPStm.setString(pOffset, pAufgabe.freitextBeschreibung);
            pOffset++;

            pPStm.setString(pOffset, pAufgabe.zeitpunktErteilt);
            pOffset++;

            pPStm.setInt(pOffset, pAufgabe.anforderer);
            pOffset++;
            pPStm.setString(pOffset, pAufgabe.angefordertVonBemerkung);
            pOffset++;

            pPStm.setInt(pOffset, pAufgabe.userNummerErfassung);
            pOffset++;

            pPStm.setInt(pOffset, pAufgabe.status);
            pOffset++;

            for (int i = 0; i < 10; i++) {
                pPStm.setString(pOffset, pAufgabe.argument[i]);
                pOffset++;
            }

            pPStm.setInt(pOffset, pAufgabe.erledigtVermerk);
            pOffset++;
            pPStm.setString(pOffset, pAufgabe.erledigtBemerkung);
            pOffset++;
            pPStm.setString(pOffset, pAufgabe.zeitpunktErledigt);
            pOffset++;
            pPStm.setInt(pOffset, pAufgabe.userNummerVerarbeitet);
            pOffset++;
            pPStm.setInt(pOffset, pAufgabe.drucklaufNr);
            pOffset++;

            if (pOffset - startOffset != anzfelder) {
                /*Nur wg. Überprüfung auf Programmierfehler - alle Felder berücksichtigt?*/
                CaBug.drucke("DbAufgaben.fuellePreparedStatementKomplett 002");
            }

        } catch (SQLException e) {
            CaBug.drucke("DbAufgaben.fuellePreparedStatementKomplett 001");
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
    public int insert(EclAufgaben pAufgabe) {

        int erg = 0;

        /* neue InterneIdent vergeben */
        erg = dbBundle.dbBasis.getInterneIdentAufgabenOhneMandant();
        if (erg < 1) {
            CaBug.drucke("DbAufgaben.insert 002");
            return (erg);
        }

        System.out.println("DbAufgaben erg=" + erg);
        pAufgabe.identAufgabe = erg;

        try {
            /*Felder Neuanlage füllen*/
            String lSql = "INSERT INTO " + dbBundle.getSchemaAllgemein() + "tbl_aufgaben " + "("
                    + "mandant, identAufgabe, db_version, aufgabe, freitextBeschreibung,"
                    + "zeitpunktErteilt, anforderer, angefordertVonBemerkung, userNummerErfassung, status, "
                    + "argument0, argument1, argument2, argument3, argument4, "
                    + "argument5, argument6, argument7, argument8, argument9, "
                    + "erledigtVermerk, erledigtBemerkung, zeitpunktErledigt, userNummerVerarbeitet, drucklaufNr " + ")"
                    + "VALUES (" + "?, ?, ?, ?, ?, " + "?, ?, ?, ?, ?, " + "?, ?, ?, ?, ?, " + "?, ?, ?, ?, ?, "
                    + "?, ?, ?, ?, ? " + ")";
            PreparedStatement lPStm = verbindung.prepareStatement(lSql, ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
            fuellePreparedStatementKomplett(lPStm, 1, pAufgabe);
            erg = executeUpdate(lPStm);
            lPStm.close();
        } catch (Exception e2) {
            CaBug.drucke("DbAufgaben.insert 001");
            System.err.println(" " + e2.getMessage());
        }

        if (erg == 0) {/*Fehler beim Einfügen - d.h. z.B. primaryKey bereits vorhanden*/
            return (-1);
        }

        return (1);
    }

    private int read_allgemein(PreparedStatement pPStm) {
        int anzInArray = 0;

        try {

            ResultSet lErgebnis = executeQuery(pPStm);
            lErgebnis.last();
            anzInArray = lErgebnis.getRow();
            lErgebnis.beforeFirst();

            ergebnisArray = new EclAufgaben[anzInArray];

            int i = 0;
            while (lErgebnis.next() == true) {
                ergebnisArray[i] = this.decodeErgebnis(lErgebnis);
                i++;
            }
            lErgebnis.close();
            pPStm.close();

        } catch (Exception e) {
            CaBug.drucke("DbAufgaben.read_allgemein 003");
            System.err.println(" " + e.getMessage());
            return (-1);
        }
        return (anzInArray);
    }

    /**Lese alle Aufgaben, die noch zu bearbeiten sind
     * */
    public int readAll_Offen() {
        PreparedStatement lPStm = null;

        try {
            String lSql = "SELECT * from " + dbBundle.getSchemaAllgemein()
                    + "tbl_aufgaben auf WHERE status=? OR STATUS=? ORDER BY identAufgabe";
            lPStm = verbindung.prepareStatement(lSql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);

            lPStm.setInt(1, KonstAufgabenStatus.gestellt);
            lPStm.setInt(2, KonstAufgabenStatus.inArbeit);

        } catch (Exception e) {
            CaBug.drucke("DbAufgaben.readAll_technisch 003");
            System.err.println(" " + e.getMessage());
            return (-1);
        }
        return read_allgemein(lPStm);
    }

    /**Einlesen aller Aufgaben zu aktuellem mandant und pArgument 0 und aufgabe=aktionaerNeuesPasswortAdressePruefen || aktionaerNeuesPasswort
     * (üblicherweise, um alle bisherigen Anfragen eines AKtionärs
     * z.B. für Passwortanfragen auszulesen*/
    public int read(String pArgument0) {
        PreparedStatement lPStm = null;

        try {
            String lSql = "SELECT * from " + dbBundle.getSchemaAllgemein() + "tbl_aufgaben auf WHERE auf.mandant=? AND "
                    + "(auf.aufgabe=1 OR auf.aufgabe=2 OR auf.aufgabe=3) AND auf.argument0 like ?;";
            lPStm = verbindung.prepareStatement(lSql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            lPStm.setInt(1, dbBundle.clGlobalVar.mandant);
            lPStm.setString(2, pArgument0 + "%");
        } catch (Exception e) {
            CaBug.drucke("DbAufgaben.read 003");
            System.err.println(" " + e.getMessage());
            return (-1);
        }
        return read_allgemein(lPStm);
    }

    /**Einlesen einer bestimmten Aufgabe mit  ident*/
    public int read(int ident) {
        PreparedStatement lPStm = null;

        try {
            String lSql = "SELECT * from " + dbBundle.getSchemaAllgemein() + "tbl_aufgaben auf WHERE identAufgabe=?;";
            lPStm = verbindung.prepareStatement(lSql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            lPStm.setInt(1, ident);

        } catch (Exception e) {
            CaBug.drucke("DbAufgaben.read 003");
            System.err.println(" " + e.getMessage());
            return (-1);
        }
        return read_allgemein(lPStm);
    }

    /*++++++++Read-und update Funktionen für Aufgaben-Bearbeitung+++++++++++++++++++*/

    /** Alle Aufgaben aktionaerNeuesPasswortAdressePruefen und status ==gestellt einlesen. Sortiert nach Mandanten und argument0*/
    public int read_passwortPruefen() {
        PreparedStatement lPStm = null;

        try {
            String lSql = "SELECT * from " + dbBundle.getSchemaAllgemein()
                    + "tbl_aufgaben auf WHERE auf.aufgabe=1 AND auf.status=1 ORDER BY auf.mandant, auf.argument0, auf.identAufgabe;";
            lPStm = verbindung.prepareStatement(lSql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);

        } catch (Exception e) {
            CaBug.drucke("DbAufgaben.read 003");
            System.err.println(" " + e.getMessage());
            return (-1);
        }
        return read_allgemein(lPStm);
    }

    /** Setzt Drucklauf und status=erledigt für alle auf.aufgabe=aktionaerNeuesPasswortAdressePruefen oder aktionaerNeuesPasswort*/
    public int update_passwortPruefenDrucken(int pDrucklaufNr) {
        String erledigtZeitpunkt = CaDatumZeit.DatumZeitStringFuerDatenbank();
        try {
            String lSql = "UPDATE " + dbBundle.getSchemaAllgemein() + "tbl_aufgaben SET "
                    + "status=4, zeitpunktErledigt=?, drucklaufNr=? " + "WHERE "
                    + "(aufgabe=1 OR aufgabe=2) AND status=6 ";

            PreparedStatement lPStm = verbindung.prepareStatement(lSql);
            lPStm.setString(1, erledigtZeitpunkt);
            lPStm.setInt(2, pDrucklaufNr);
            return update_allgemein(lPStm);

        } catch (Exception e1) {
            CaBug.drucke("DbAufgaben.update 001");
            System.err.println(" " + e1.getMessage());
            return (CaFehler.pfdXyBereitsVorhanden);
        }

    }

    /* Alle Aufgaben mit drucklaufNr=pDrucklauf. Sortiert nach Mandanten und argument0*/
    public int read_passwortVerarbeiten(int pDrucklauf) {
        PreparedStatement lPStm = null;

        try {
            String lSql = "SELECT * from " + dbBundle.getSchemaAllgemein() + "tbl_aufgaben auf "
                    + "WHERE auf.drucklaufNr=? ORDER BY auf.mandant, auf.argument0, auf.identAufgabe;";
            lPStm = verbindung.prepareStatement(lSql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            lPStm.setInt(1, pDrucklauf);

        } catch (Exception e) {
            CaBug.drucke("003");
            System.err.println(" " + e.getMessage());
            return (-1);
        }
        return read_allgemein(lPStm);
    }

    private int update_allgemein(PreparedStatement pPStm) {
        int ergebnis = 0;
        try {
            ergebnis = executeUpdate(pPStm);
            pPStm.close();
            if (ergebnis == 0) {
                return (CaFehler.pfXyWurdeVonAnderemBenutzerVeraendert);
            }
        } catch (Exception e1) {
            CaBug.drucke("DbAufgaben.update_allgemein 001");
            System.err.println(" " + e1.getMessage());
            return (CaFehler.pfdXyBereitsVorhanden);
        }

        return (ergebnis);
    }

    /**Update. Versionsnummer wird um 1 hochgezählt
     * 
     * Feld mandant wird von dieser Funktion nicht verändert.
     * 
     * Zum Sicherstellen der Multiuserfähigkeit muß in jedem Fall der rc CaFehler.pfXyWurdeVonAnderemBenutzerVeraendert
     * nach Aufruf dieser Funktion abgefangen werden.
     * Ansonsten: rc=1 => ok, ansonsten Fehler
     */
    public int update(EclAufgaben pAufgabe) {

        pAufgabe.db_version++;

        try {
            String lSql = "UPDATE " + dbBundle.getSchemaAllgemein() + "tbl_aufgaben SET "
                    + "mandant=?, identAufgabe=?, db_version=?, aufgabe=?, freitextBeschreibung=?,"
                    + "zeitpunktErteilt=?, anforderer=?, angefordertVonBemerkung=?, userNummerErfassung=?, status=?, "
                    + "argument0=?, argument1=?, argument2=?, argument3=?, argument4=?, "
                    + "argument5=?, argument6=?, argument7=?, argument8=?, argument9=?, "
                    + "erledigtVermerk=?, erledigtBemerkung=?, zeitpunktErledigt=?, userNummerVerarbeitet=?, drucklaufNr=? "
                    + "WHERE " + "db_version=? AND identAufgabe=? ";

            PreparedStatement lPStm = verbindung.prepareStatement(lSql);
            fuellePreparedStatementKomplett(lPStm, 1, pAufgabe);
            lPStm.setLong(anzfelder + 1, pAufgabe.db_version - 1);
            lPStm.setInt(anzfelder + 2, pAufgabe.identAufgabe);
            return update_allgemein(lPStm);

        } catch (Exception e1) {
            CaBug.drucke("DbAufgaben.update 001");
            System.err.println(" " + e1.getMessage());
            return (CaFehler.pfdXyBereitsVorhanden);
        }

    }

    public int delete(int pIdentAufgabe) {

        try {

            String sql = "DELETE FROM " + dbBundle.getSchemaAllgemein() + "tbl_aufgaben WHERE identAufgabe=? ";

            PreparedStatement pstm1 = verbindung.prepareStatement(sql);
            pstm1.setInt(1, pIdentAufgabe);

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
