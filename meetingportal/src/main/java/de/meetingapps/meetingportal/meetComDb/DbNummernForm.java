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

/**Wichtig: Nicht Mandantenfähig! Mandanten-Nummer ist zwar vorhanden, aber 
 * nicht key - key ist nur ident.*/

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import de.meetingapps.meetingportal.meetComAllg.CaBug;
import de.meetingapps.meetingportal.meetComAllg.CaFehler;
import de.meetingapps.meetingportal.meetComBVerwaltung.BvReload;
import de.meetingapps.meetingportal.meetComEntities.EclNummernForm;

public class DbNummernForm  extends DbRootExecute {

    private Connection verbindung = null;
    private DbBasis dbBasis = null;
    private DbBundle dbBundle = null;

    public EclNummernForm ergebnisArray[] = null;

    /*************************Initialisierung***************************/
    public DbNummernForm(DbBundle pDbBundle) {
        /* Verbindung in lokale Daten eintragen*/
        if (pDbBundle == null) {
            CaBug.drucke("DbNummernForm.init 001 - dbBundle nicht initialisiert");
            return;
        }
        if (pDbBundle.dbBasis == null) {
            CaBug.drucke("DbNummernForm.init 002 - dbBasis nicht initialisiert");
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
    public EclNummernForm ergebnisPosition(int pN) {
        if (ergebnisArray == null) {
            CaBug.drucke("DbNummernForm.ergebnisPosition 001");
            return null;
        }
        if (pN < 0) {
            CaBug.drucke("DbNummernForm.ergebnisPosition 002");
            return null;
        }
        if (pN >= ergebnisArray.length) {
            CaBug.drucke("DbNummernForm.ergebnisPosition 003");
            return null;
        }
        return ergebnisArray[pN];
    }

    public int createTable() {
        int rc = 0;
        DbLowLevel lDbLowLevel = new DbLowLevel(dbBundle);
        rc = lDbLowLevel.createTable("CREATE TABLE " + dbBundle.getSchemaAllgemein() + "tbl_nummernform ( "
                + "`mandant` int(11) DEFAULT NULL, " + "`ident` int(11) NOT NULL, "
                + "`geloescht` int(11) DEFAULT NULL, " + "`ersetztDurch` int(11) DEFAULT NULL, "
                + "`kodierung` varchar(20) DEFAULT NULL, " + "`beschreibung` varchar(120) DEFAULT NULL, "
                + "PRIMARY KEY (`ident`) " + ") ");
        return rc;
    }

    /**************************deleteAll Löschen aller Datensätze  eines Mandanten****************/
    public int deleteAll() {
        int erg = 0;

        dbBasis.resetInterneIdentNummernForm();

        try {

            String sql1 = "DELETE FROM tbl_nummernform;";
            PreparedStatement pstm1 = verbindung.prepareStatement(sql1);

            erg = 0; /*Falls Duplicate Key, dann wird exception geschmissen und erg wohl nicht gefüllt!*/
            erg = executeUpdate(pstm1);

            pstm1.close();
        } catch (Exception e2) {
            CaBug.drucke("DbNummernForm.deleteAll 001");
            System.err.println(" " + e2.getMessage());
            return (erg);
        }

        return 1;
    }

    public void reorgInterneIdent() {
        int lMax;
        lMax = 0;

        PreparedStatement pstm1 = null;
        try {
            int anzInArray;
            String sql = "SELECT MAX(ident) FROM " + dbBundle.getSchemaAllgemein() + "tbl_nummernform ";
            pstm1 = verbindung.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);

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
            CaBug.drucke("DbNummernForm.reorgInterneIdent 001");
            System.err.println(" " + e.getMessage());
        }

        dbBasis.resetInterneIdentNummernForm(lMax);

    }

    /********** dekodiert die aktuelle Position aus ergebnis in EclAktienregisterEintrag und gibt dieses zurück******/
    private EclNummernForm decodeErgebnis(ResultSet pErgebnis) {

        EclNummernForm lNummernForm = new EclNummernForm();

        try {

            lNummernForm.mandant = pErgebnis.getInt("mandant");
            lNummernForm.ident = pErgebnis.getInt("ident");
            lNummernForm.geloescht = pErgebnis.getInt("geloescht");
            lNummernForm.ersetztDurch = pErgebnis.getInt("ersetztDurch");

            lNummernForm.kodierung = pErgebnis.getString("kodierung");
            lNummernForm.beschreibung = pErgebnis.getString("beschreibung");

        } catch (Exception e) {
            CaBug.drucke("DbNummernForm.decodeErgebnis 001");
            System.err.println(" " + e.getMessage());
        }

        return lNummernForm;
    }

    /********************* Fuellen Prepared Statement mit allen Feldern, beginnend bei offset.*****************
     * Kann sowohl für Insert, als auch für update verwendet werden.
     * 
     * offset= Startposition des ersten Feldes (also z.B. 1)
     */
    private int anzfelder = 6; /*Anpassen auf Anzahl der Felder pro Datensatz*/

    private void fuellePreparedStatementKomplett(PreparedStatement pPStm, int pOffset, EclNummernForm pNummernForm) {

        int startOffset = pOffset; /*Nur erforderlich zum Überprüfen von Programmierfehlern - Feldanzahl*/

        try {
            pPStm.setInt(pOffset, pNummernForm.mandant);
            pOffset++;
            pPStm.setInt(pOffset, pNummernForm.ident);
            pOffset++;
            pPStm.setInt(pOffset, pNummernForm.geloescht);
            pOffset++;

            pPStm.setInt(pOffset, pNummernForm.ersetztDurch);
            pOffset++;

            pPStm.setString(pOffset, pNummernForm.kodierung);
            pOffset++;
            pPStm.setString(pOffset, pNummernForm.beschreibung);
            pOffset++;

            if (pOffset - startOffset != anzfelder) {
                /*Nur wg. Überprüfung auf Programmierfehler - alle Felder berücksichtigt?*/
                CaBug.drucke("DbNummernForm.fuellePreparedStatementKomplett 002");
            }

        } catch (SQLException e) {
            CaBug.drucke("DbNummernForm.fuellePreparedStatementKomplett 001");
            e.printStackTrace();
        }

    }

    /**Insert
     * 
     * Feld mandant wird von dieser Funktion NICHT belegt.
     * 
     * Returnwert:
     * =1 => Insert erfolgreich
     * ansonsten: Fehler
     */
    public int insert(EclNummernForm pNummernForm) {

        int erg = 0;

        /* Start Transaktion */
        dbBasis.beginTransaction();

        /* neue InterneIdent vergeben */
        erg = dbBasis.getInterneIdentNummernForm();
        if (erg < 1) {
            CaBug.drucke("DbNummernForm.insert 002");
            dbBasis.rollbackTransaction();
            dbBasis.endTransaction();
            return (erg);
        }
        pNummernForm.ident = erg;

        //		pNummernForm.mandant=dbBundle.globalVar.mandant;

        /* Satz einfügen: 
         * Verarbeitungshinweis: 
         * 	>	nachdem InterneIdent immer eindeutig vergeben werden, ist prinzipiell eine "Doppeleinfügung"
         * 		von InterneIdent nicht möglich. Sollte es dazu kommen, ist das immer ein Programmfehler!
         */

        try {

            /*Felder Neuanlage füllen*/
            String lSql = "INSERT INTO " + dbBundle.getSchemaAllgemein() + "tbl_nummernform " + "("
                    + "mandant, ident, geloescht, " + "ersetztDurch, kodierung, beschreibung" + ")" + "VALUES ("
                    + "?, ?, ?, " + "?, ?, ? " + ")";
            PreparedStatement lPStm = verbindung.prepareStatement(lSql, ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
            System.out.println("A");
            fuellePreparedStatementKomplett(lPStm, 1, pNummernForm);
            System.out.println("B");

            erg = executeUpdate(lPStm);
            System.out.println("C");
            lPStm.close();
        } catch (Exception e2) {
            CaBug.drucke("DbNummernForm.insert 001");
            System.err.println(" " + e2.getMessage());
            e2.printStackTrace();
        }

        if (erg == 0) {/*Fehler beim Einfügen - d.h. primaryKey bereits vorhanden*/
            dbBasis.rollbackTransaction();
            dbBasis.endTransaction();
            return (-1);
        }

        BvReload bvReload = new BvReload(dbBundle);
        bvReload.setReloadParameter(dbBundle.clGlobalVar.mandant);

        /* Ende Transaktion */
        dbBasis.endTransaction();
        return (1);
    }

    public int read(EclNummernForm pNummernForm) {
        int anzInArray = 0;
        PreparedStatement lPStm = null;

        try {
            String lSql = "SELECT * from " + dbBundle.getSchemaAllgemein() + "tbl_nummernform where "
                    + "ident=? ORDER BY ident;";
            lPStm = verbindung.prepareStatement(lSql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            lPStm.setInt(1, pNummernForm.ident);

            ResultSet lErgebnis = executeQuery(lPStm);
            lErgebnis.last();
            anzInArray = lErgebnis.getRow();
            lErgebnis.beforeFirst();

            ergebnisArray = new EclNummernForm[anzInArray];

            int i = 0;
            while (lErgebnis.next() == true) {
                ergebnisArray[i] = this.decodeErgebnis(lErgebnis);
                i++;
            }
            lErgebnis.close();
            lPStm.close();

        } catch (Exception e) {
            CaBug.drucke("DbNummernForm.read 003");
            System.err.println(" " + e.getMessage());
            return (-1);
        }
        return (anzInArray);
    }

    public int read_all() {
        int anzInArray = 0;
        PreparedStatement lPStm = null;

        try {
            String lSql = "SELECT * from " + dbBundle.getSchemaAllgemein() + "tbl_nummernform " + "ORDER BY ident;";
            lPStm = verbindung.prepareStatement(lSql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);

            ResultSet lErgebnis = executeQuery(lPStm);
            lErgebnis.last();
            anzInArray = lErgebnis.getRow();
            lErgebnis.beforeFirst();

            ergebnisArray = new EclNummernForm[anzInArray];

            int i = 0;
            while (lErgebnis.next() == true) {
                ergebnisArray[i] = this.decodeErgebnis(lErgebnis);
                i++;
            }
            lErgebnis.close();
            lPStm.close();

        } catch (Exception e) {
            CaBug.drucke("DbNummernForm.read_all 003");
            System.err.println(" " + e.getMessage());
            return (-1);
        }
        return (anzInArray);
    }

    /**Update.
     *
     * Nicht Multiuserfähig ...
     * 
     * Ansonsten: rc=1 => ok, ansonsten Fehler
     */
    public int update(EclNummernForm pNummernForm) {

        try {

            String lSql = "UPDATE " + dbBundle.getSchemaAllgemein() + "tbl_nummernform SET "
                    + "mandant=?, ident=?, geloescht=?, " + "ersetztDurch=?, kodierung=?, beschreibung=? " + "WHERE "
                    + "ident=?";

            PreparedStatement lPStm = verbindung.prepareStatement(lSql);
            fuellePreparedStatementKomplett(lPStm, 1, pNummernForm);
            lPStm.setInt(anzfelder + 1, pNummernForm.ident);

            int ergebnis = executeUpdate(lPStm);
            lPStm.close();
            if (ergebnis == 0) {
                return (CaFehler.pfXyWurdeVonAnderemBenutzerVeraendert);
            }
        } catch (Exception e1) {
            CaBug.drucke("DbNummernForm.update 001");
            System.err.println(" " + e1.getMessage());
            return (CaFehler.pfdXyBereitsVorhanden);
        }

        BvReload bvReload = new BvReload(dbBundle);
        bvReload.setReloadParameter(dbBundle.clGlobalVar.mandant);

        return (1);
    }

    public int delete(int ident) {

        try {

            String sql = "DELETE FROM " + dbBundle.getSchemaAllgemein() + "tbl_nummernform WHERE ident=? ";

            PreparedStatement pstm1 = verbindung.prepareStatement(sql);
            pstm1.setInt(1, ident);

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
