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
import de.meetingapps.meetingportal.meetComBVerwaltung.BvReload;
import de.meetingapps.meetingportal.meetComEntities.EclTermine;

public class DbTermine  extends DbRootExecute {

    private Connection verbindung = null;
    private DbBundle dbBundle = null;

    public EclTermine ergebnisArray[] = null;

    /*************************Initialisierung***************************/
    public DbTermine(DbBundle pDbBundle) {
        /* Verbindung in lokale Daten eintragen*/
        if (pDbBundle == null) {
            CaBug.drucke("DbTermine.init 001 - dbBundle nicht initialisiert");
            return;
        }
        if (pDbBundle.dbBasis == null) {
            CaBug.drucke("DbTermine.init 002 - dbBasis nicht initialisiert");
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
    public EclTermine ergebnisPosition(int pN) {
        if (ergebnisArray == null) {
            CaBug.drucke("DbTermine.ergebnisPosition 001");
            return null;
        }
        if (pN < 0) {
            CaBug.drucke("DbTermine.ergebnisPosition 002");
            return null;
        }
        if (pN >= ergebnisArray.length) {
            CaBug.drucke("DbTermine.ergebnisPosition 003");
            return null;
        }
        return ergebnisArray[pN];
    }

    /**************************deleteAll Löschen aller Datensätze  eines Mandanten****************/
    public int deleteAll() {
        //		return dbBundle.dbLowLevel.deleteMandant("DELETE FROM "+dbBundle.getSchemaMandant()+
        //				"tbl_termine where mandant=?;"); /* derzeit raus, da teilweise falsch angelegt!*/
        return dbBundle.dbLowLevel.deleteAlle("DELETE FROM " + dbBundle.getSchemaMandant() + "tbl_termine;");
    }

    /**************************setzt aktuelle Mandantennummer bei allen Datensätzen****************/
    public int updateMandant() {
        return dbBundle.dbLowLevel.rawUpdateMandant(dbBundle.getSchemaMandant() + "tbl_termine");
    }

    /*******Checken, ob table überhaupt vorhanden ist***************************/
    public boolean checkTableVorhanden() {
        DbLowLevel lDbLowLevel = new DbLowLevel(dbBundle);
        return lDbLowLevel.checkTableVorhanden("SELECT * from tbl_termine WHERE mandant=0; ");
    }

    /************Neuanlegen Table******************************/
    public int createTable() {
        int rc = 0;
        DbLowLevel lDbLowLevel = new DbLowLevel(dbBundle);
        rc = lDbLowLevel.createTable("CREATE TABLE " + dbBundle.getSchemaMandant() + "tbl_termine ( "
                + "`mandant` int(11) NOT NULL, " + "`identTermin` int(11) NOT NULL, "
                + "`db_version` bigint(20) DEFAULT NULL, " + "`oeffentlicheFixIdent` int(11) NOT NULL, "
                + "`oeffentlicheFixSubIdent` int(11) NOT NULL, " + "`technischErforderlicherTermin` int(11) NOT NULL, "
                + "`terminDatum` char(8) DEFAULT NULL, " + "`terminZeit` char(8) DEFAULT NULL, "
                + "`textDatumZeitFuerPortalDE` varchar(100) DEFAULT NULL, "
                + "`textDatumZeitFuerPortalEN` varchar(100) DEFAULT NULL, "
                + "`beschreibung` varchar(2000) DEFAULT NULL, " + "PRIMARY KEY (`mandant`,`identTermin`) " + ")  ");
        return rc;
    }

    /********** dekodiert die aktuelle Position aus ergebnis in EclAktienregisterEintrag und gibt dieses zurück******/
    EclTermine decodeErgebnis(ResultSet pErgebnis) {

        EclTermine lTermin = new EclTermine();

        try {
            lTermin.mandant = pErgebnis.getInt("tm.mandant");
            lTermin.identTermin = pErgebnis.getInt("tm.identTermin");
            lTermin.db_version = pErgebnis.getLong("tm.db_version");

            lTermin.oeffentlicheFixIdent = pErgebnis.getInt("tm.oeffentlicheFixIdent");
            lTermin.oeffentlicheFixSubIdent = pErgebnis.getInt("tm.oeffentlicheFixSubIdent");
            lTermin.technischErforderlicherTermin = pErgebnis.getInt("tm.technischErforderlicherTermin");

            lTermin.terminDatum = pErgebnis.getString("tm.terminDatum");
            lTermin.terminZeit = pErgebnis.getString("tm.terminZeit");
            lTermin.textDatumZeitFuerPortalDE = pErgebnis.getString("tm.textDatumZeitFuerPortalDE");
            lTermin.textDatumZeitFuerPortalEN = pErgebnis.getString("tm.textDatumZeitFuerPortalEN");

            lTermin.beschreibung = pErgebnis.getString("tm.beschreibung");

        } catch (Exception e) {
            CaBug.drucke("DbTermine.decodeErgebnis 001");
            System.err.println(" " + e.getMessage());
        }

        return lTermin;
    }

    /********************* Fuellen Prepared Statement mit allen Feldern, beginnend bei offset.*****************
     * Kann sowohl für Insert, als auch für update verwendet werden.
     * 
     * offset= Startposition des ersten Feldes (also z.B. 1)
     */
    private int anzfelder = 11; /*Anpassen auf Anzahl der Felder pro Datensatz*/

    void fuellePreparedStatementKomplett(PreparedStatement pPStm, int pOffset, EclTermine pTermin) {

        int startOffset = pOffset; /*Nur erforderlich zum Überprüfen von Programmierfehlern - Feldanzahl*/

        try {
            pPStm.setInt(pOffset, pTermin.mandant);
            pOffset++;
            pPStm.setInt(pOffset, pTermin.identTermin);
            pOffset++;
            pPStm.setLong(pOffset, pTermin.db_version);
            pOffset++;

            pPStm.setInt(pOffset, pTermin.oeffentlicheFixIdent);
            pOffset++;
            pPStm.setInt(pOffset, pTermin.oeffentlicheFixSubIdent);
            pOffset++;
            pPStm.setInt(pOffset, pTermin.technischErforderlicherTermin);
            pOffset++;

            pPStm.setString(pOffset, pTermin.terminDatum);
            pOffset++;
            pPStm.setString(pOffset, pTermin.terminZeit);
            pOffset++;
            pPStm.setString(pOffset, pTermin.textDatumZeitFuerPortalDE);
            pOffset++;
            pPStm.setString(pOffset, pTermin.textDatumZeitFuerPortalEN);
            pOffset++;

            pPStm.setString(pOffset, pTermin.beschreibung);
            pOffset++;

            if (pOffset - startOffset != anzfelder) {
                /*Nur wg. Überprüfung auf Programmierfehler - alle Felder berücksichtigt?*/
                CaBug.drucke("DbTermine.fuellePreparedStatementKomplett 002");
            }

        } catch (SQLException e) {
            CaBug.drucke("DbTermine.fuellePreparedStatementKomplett 001");
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
    public int insert(EclTermine pEmittenten) {

        int erg = 0;
        try {
            /*Felder Neuanlage füllen*/
            String lSql = "INSERT INTO " + dbBundle.getSchemaMandant() + "tbl_termine " + "("
                    + "mandant, identTermin, db_version, oeffentlicheFixIdent, oeffentlicheFixSubIdent, "
                    + "technischErforderlicherTermin, terminDatum, terminZeit, textDatumZeitFuerPortalDE, textDatumZeitFuerPortalEN, beschreibung "
                    + ")" + "VALUES (" + "?, ?, ?, ?, ?, " + "?, ?, ?, ?, ?, ?" + ")";
            PreparedStatement lPStm = verbindung.prepareStatement(lSql, ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
            fuellePreparedStatementKomplett(lPStm, 1, pEmittenten);
            erg = executeUpdate(lPStm);
            lPStm.close();
        } catch (Exception e2) {
            CaBug.drucke("DbTermine.insert 001");
            System.err.println(" " + e2.getMessage());
        }

        /*Spezial-Funktion: im Reload-Bereich markieren, dass sich Emittentenstamm geändert hat, d.h. Alle Systeme
         * müssen beim nächsten "Open" den Parameterstamm neu einlesen*/
        BvReload bvReload = new BvReload(dbBundle);
        bvReload.setReloadParameter(dbBundle.clGlobalVar.mandant);

        if (erg == 0) {/*Fehler beim Einfügen - d.h. z.B. primaryKey bereits vorhanden*/
            return (-1);
        }

        return (1);
    }

    /**Einlesen aller technischen Texte
     * pSortiert=0 nach identTermin
     * */
    public int readAll_technisch(int pSortiert) {
        int anzInArray = 0;
        PreparedStatement lPStm = null;
        String lSql = "";
        try {
            lSql = "SELECT * from " + dbBundle.getSchemaMandant() + "tbl_termine tm ORDER BY identTermin";
            lPStm = verbindung.prepareStatement(lSql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);

            ResultSet lErgebnis = executeQuery(lPStm);
            lErgebnis.last();
            anzInArray = lErgebnis.getRow();
            lErgebnis.beforeFirst();

            ergebnisArray = new EclTermine[anzInArray];

            int i = 0;
            while (lErgebnis.next() == true) {
                ergebnisArray[i] = this.decodeErgebnis(lErgebnis);
                i++;
            }
            lErgebnis.close();
            lPStm.close();

        } catch (Exception e) {
            CaBug.drucke("DbTermine.readAll_technisch 003 " + lSql);
            System.err.println(" " + e.getMessage());
            return (-1);
        }
        return (anzInArray);
    }

    /**Einlesen aller Texte
     * pSortiert=0 nach identTermin
     * */
    public int readAll(int pSortiert) {
        int anzInArray = 0;
        PreparedStatement lPStm = null;
        String lSql = "";
        try {
            lSql = "SELECT * from " + dbBundle.getSchemaMandant() + "tbl_termine tm ORDER BY identTermin";
            lPStm = verbindung.prepareStatement(lSql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);

            ResultSet lErgebnis = executeQuery(lPStm);
            lErgebnis.last();
            anzInArray = lErgebnis.getRow();
            lErgebnis.beforeFirst();

            ergebnisArray = new EclTermine[anzInArray];

            int i = 0;
            while (lErgebnis.next() == true) {
                ergebnisArray[i] = this.decodeErgebnis(lErgebnis);
                i++;
            }
            lErgebnis.close();
            lPStm.close();

        } catch (Exception e) {
            CaBug.drucke("DbTermine.readAll 003 " + lSql);
            System.err.println(" " + e.getMessage());
            return (-1);
        }
        return (anzInArray);
    }

    /**Einlesen eines berstimmten Tgermins mit identTermin*/
    public int read(int identTermin) {
        int anzInArray = 0;
        PreparedStatement lPStm = null;

        try {
            String lSql = "SELECT * from " + dbBundle.getSchemaMandant()
                    + "tbl_termine tm WHERE mandant=? AND identTermin=? ORDER BY identTermin ;";
            lPStm = verbindung.prepareStatement(lSql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            lPStm.setInt(1, dbBundle.clGlobalVar.mandant);
            lPStm.setInt(2, identTermin);

            ResultSet lErgebnis = executeQuery(lPStm);
            lErgebnis.last();
            anzInArray = lErgebnis.getRow();
            lErgebnis.beforeFirst();

            ergebnisArray = new EclTermine[anzInArray];

            int i = 0;
            while (lErgebnis.next() == true) {
                ergebnisArray[i] = this.decodeErgebnis(lErgebnis);
                i++;
            }

            lErgebnis.close();
            lPStm.close();

        } catch (Exception e) {
            CaBug.drucke("DbTermine.read 003");
            System.err.println(" " + e.getMessage());
            return (-1);
        }
        return (anzInArray);
    }

    /**Update. Versionsnummer wird um 1 hochgezählt
     * 
     * Feld mandant wird von dieser Funktion selbstständig belegt.
     * 
     * Zum Sicherstellen der Multiuserfähigkeit muß in jedem Fall der rc CaFehler.pfXyWurdeVonAnderemBenutzerVeraendert
     * nach Aufruf dieser Funktion abgefangen werden.
     * Ansonsten: rc=1 => ok, ansonsten Fehler
     */
    public int update(EclTermine pTermin) {

        pTermin.mandant = dbBundle.clGlobalVar.mandant;
        pTermin.db_version++;

        try {
            String lSql = "UPDATE " + dbBundle.getSchemaMandant() + "tbl_termine SET "
                    + "mandant=?, identTermin=?, db_version=?, oeffentlicheFixIdent=?, oeffentlicheFixSubIdent=?, "
                    + "technischErforderlicherTermin=?, terminDatum=?, terminZeit=?, textDatumZeitFuerPortalDE=?, textDatumZeitFuerPortalEN=?, beschreibung=? "
                    + "WHERE " + "db_version=? AND mandant=? AND identTermin=? ";

            PreparedStatement lPStm = verbindung.prepareStatement(lSql);
            fuellePreparedStatementKomplett(lPStm, 1, pTermin);
            lPStm.setLong(anzfelder + 1, pTermin.db_version - 1);
            lPStm.setInt(anzfelder + 2, pTermin.mandant);
            lPStm.setInt(anzfelder + 3, pTermin.identTermin);

            int ergebnis = executeUpdate(lPStm);
            lPStm.close();
            if (ergebnis == 0) {
                return (CaFehler.pfXyWurdeVonAnderemBenutzerVeraendert);
            }
        } catch (Exception e1) {
            CaBug.drucke("DbTermine.update 001");
            System.err.println(" " + e1.getMessage());
            return (CaFehler.pfdXyBereitsVorhanden);
        }

        /*Spezial-Funktion: im Reload-Bereich markieren, dass sich Emittentenstamm geändert hat, d.h. Alle Systeme
         * müssen beim nächsten "Open" den Parameterstamm neu einlesen*/
        BvReload bvReload = new BvReload(dbBundle);
        bvReload.setReloadParameter(dbBundle.clGlobalVar.mandant);

        return (1);
    }

    public int delete(int pIdentTermin) {

        try {

            String sql = "DELETE FROM " + dbBundle.getSchemaMandant()
                    + "tbl_termine WHERE mandant=? and identTermin=? ";

            PreparedStatement pstm1 = verbindung.prepareStatement(sql);
            pstm1.setInt(1, dbBundle.clGlobalVar.mandant);
            pstm1.setInt(2, pIdentTermin);

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
