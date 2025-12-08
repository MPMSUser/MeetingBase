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
import de.meetingapps.meetingportal.meetComEntities.EclVerarbeitungsProtokoll;

public class DbVerarbeitungsProtokoll {

    private Connection verbindung = null;
    private DbBundle dbBundle = null;

    public EclVerarbeitungsProtokoll ergebnisArray[] = null;

    /*************************Initialisierung***************************/
    public DbVerarbeitungsProtokoll(DbBundle pDbBundle) {
        /* Verbindung in lokale Daten eintragen*/
        if (pDbBundle == null) {
            CaBug.drucke("DbVerarbeitungsProtokoll.init 001 - dbBundle nicht initialisiert");
            return;
        }
        if (pDbBundle.dbBasis == null) {
            CaBug.drucke("DbVerarbeitungsProtokoll.init 002 - dbBasis nicht initialisiert");
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
    public EclVerarbeitungsProtokoll ergebnisPosition(int pN) {
        if (ergebnisArray == null) {
            CaBug.drucke("DbVerarbeitungsProtokoll.ergebnisPosition 001");
            return null;
        }
        if (pN < 0) {
            CaBug.drucke("DbVerarbeitungsProtokoll.ergebnisPosition 002");
            return null;
        }
        if (pN >= ergebnisArray.length) {
            CaBug.drucke("DbVerarbeitungsProtokoll.ergebnisPosition 003");
            return null;
        }
        return ergebnisArray[pN];
    }

    /**************************deleteAll Löschen aller Datensätze  eines Mandanten****************/
    public int deleteAll() {
        return dbBundle.dbLowLevel.deleteMandant(
                "DELETE FROM " + dbBundle.getSchemaAllgemein() + "tbl_verarbeitungsProtokoll where mandant=?;");
    }

    /*******Checken, ob table überhaupt vorhanden ist***************************/
    public boolean checkTableVorhanden() {
        DbLowLevel lDbLowLevel = new DbLowLevel(dbBundle);
        return lDbLowLevel.checkTableVorhanden("SELECT * from tbl_verarbeitungsProtokoll WHERE mandant=0; ");
    }

    /************Neuanlegen Table******************************/
    public int createTable() {
        int rc = 0;
        DbLowLevel lDbLowLevel = new DbLowLevel(dbBundle);
        rc = lDbLowLevel.createTable("CREATE TABLE " + dbBundle.getSchemaAllgemein() + "tbl_verarbeitungsProtokoll ( "
                + "`mandant` int(11) NOT NULL, " + "`ident` int(11) NOT NULL, "
                + "`verarbeitungslauf` int(11) NOT NULL, " + "`ergebnis` int(11) NOT NULL, "
                + "`codeVerarbeitet` int(11) NOT NULL, " + "`textVerarbeitet` varchar(400) DEFAULT NULL, "
                + "`codeFehler` int(11) NOT NULL, " + "`textFehler` varchar(400) DEFAULT NULL, "
                + "PRIMARY KEY (`ident`, `verarbeitungslauf`) " + ")  ");
        System.out.println("DbVerarbeitungsProtokoll create table rc=" + rc);
        return rc;
    }

    /********** dekodiert die aktuelle Position aus ergebnis in EclAktienregisterEintrag und gibt dieses zurück******/
    EclVerarbeitungsProtokoll decodeErgebnis(ResultSet pErgebnis) {

        EclVerarbeitungsProtokoll lVerarbeitungsProtokoll = new EclVerarbeitungsProtokoll();

        try {
            lVerarbeitungsProtokoll.mandant = pErgebnis.getInt("vp.mandant");
            lVerarbeitungsProtokoll.ident = pErgebnis.getInt("vp.ident");
            lVerarbeitungsProtokoll.verarbeitungslauf = pErgebnis.getInt("vp.verarbeitungslauf");
            lVerarbeitungsProtokoll.ergebnis = pErgebnis.getInt("vp.ergebnis");

            lVerarbeitungsProtokoll.codeVerarbeitet = pErgebnis.getInt("vp.codeVerarbeitet");
            lVerarbeitungsProtokoll.textVerarbeitet = pErgebnis.getString("vp.textVerarbeitet");
            lVerarbeitungsProtokoll.codeFehler = pErgebnis.getInt("vp.codeFehler");
            lVerarbeitungsProtokoll.textFehler = pErgebnis.getString("vp.textFehler");

        } catch (Exception e) {
            CaBug.drucke("DbVerarbeitungsProtokoll.decodeErgebnis 001");
            System.err.println(" " + e.getMessage());
        }

        return lVerarbeitungsProtokoll;
    }

    /********************* Fuellen Prepared Statement mit allen Feldern, beginnend bei offset.*****************
     * Kann sowohl für Insert, als auch für update verwendet werden.
     * 
     * offset= Startposition des ersten Feldes (also z.B. 1)
     */
    private int anzfelder = 8; /*Anpassen auf Anzahl der Felder pro Datensatz*/

    void fuellePreparedStatementKomplett(PreparedStatement pPStm, int pOffset,
            EclVerarbeitungsProtokoll pVerarbeitungsProtokoll) {

        int startOffset = pOffset; /*Nur erforderlich zum Überprüfen von Programmierfehlern - Feldanzahl*/

        try {
            pPStm.setInt(pOffset, pVerarbeitungsProtokoll.mandant);
            pOffset++;
            pPStm.setInt(pOffset, pVerarbeitungsProtokoll.ident);
            pOffset++;
            pPStm.setInt(pOffset, pVerarbeitungsProtokoll.verarbeitungslauf);
            pOffset++;
            pPStm.setInt(pOffset, pVerarbeitungsProtokoll.ergebnis);
            pOffset++;

            pPStm.setInt(pOffset, pVerarbeitungsProtokoll.codeVerarbeitet);
            pOffset++;
            pPStm.setString(pOffset, pVerarbeitungsProtokoll.textVerarbeitet);
            pOffset++;
            pPStm.setInt(pOffset, pVerarbeitungsProtokoll.codeFehler);
            pOffset++;
            pPStm.setString(pOffset, pVerarbeitungsProtokoll.textFehler);
            pOffset++;

            if (pOffset - startOffset != anzfelder) {
                /*Nur wg. Überprüfung auf Programmierfehler - alle Felder berücksichtigt?*/
                CaBug.drucke("DbVerarbeitungsProtokoll.fuellePreparedStatementKomplett 002");
            }

        } catch (SQLException e) {
            CaBug.drucke("DbVerarbeitungsProtokoll.fuellePreparedStatementKomplett 001");
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
    public int insert(EclVerarbeitungsProtokoll pVerarbeitungsProtokoll) {

        int erg = 0;

        try {
            /*Felder Neuanlage füllen*/
            String lSql = "INSERT INTO " + dbBundle.getSchemaAllgemein() + "tbl_verarbeitungsProtokoll " + "("
                    + "mandant, ident, verarbeitungslauf, ergebnis, "
                    + "codeVerarbeitet, textVerarbeitet, codeFehler, textFehler " + ")" + "VALUES (" + "?, ?, ?, ?, "
                    + "?, ?, ?, ? " + ")";
            PreparedStatement lPStm = verbindung.prepareStatement(lSql, ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
            fuellePreparedStatementKomplett(lPStm, 1, pVerarbeitungsProtokoll);
            erg = lPStm.executeUpdate();
            lPStm.close();
        } catch (Exception e2) {
            CaBug.drucke("DbVerarbeitungsProtokoll.insert 001");
            System.err.println(" " + e2.getMessage());
        }

        if (erg == 0) {/*Fehler beim Einfügen - d.h. z.B. primaryKey bereits vorhanden*/
            return (-1);
        }

        return (1);
    }

    /**Lese alle Verarbeitungsläufe
     * */
    public int readAll(int pVerarbeitungslauf) {
        int anzInArray = 0;
        PreparedStatement lPStm = null;

        try {
            String lSql = "SELECT * from " + dbBundle.getSchemaAllgemein()
                    + "tbl_verarbeitungsProtokoll vp WHERE verarbeitungslauf=? ORDER BY ident";
            lPStm = verbindung.prepareStatement(lSql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            lPStm.setInt(1, pVerarbeitungslauf);

            ResultSet lErgebnis = lPStm.executeQuery();
            lErgebnis.last();
            anzInArray = lErgebnis.getRow();
            lErgebnis.beforeFirst();

            ergebnisArray = new EclVerarbeitungsProtokoll[anzInArray];

            int i = 0;
            while (lErgebnis.next() == true) {
                ergebnisArray[i] = this.decodeErgebnis(lErgebnis);
                i++;
            }
            lErgebnis.close();
            lPStm.close();

        } catch (Exception e) {
            CaBug.drucke("DbVerarbeitungsProtokoll.readAll_technisch 003");
            System.err.println(" " + e.getMessage());
            return (-1);
        }
        return (anzInArray);
    }

    /**Einlesen eines bestimmten Laufs mit  ident, verarbeitungslauf*/
    public int read(int ident, int verarbeitungslauf) {
        int anzInArray = 0;
        PreparedStatement lPStm = null;

        try {
            String lSql = "SELECT * from " + dbBundle.getSchemaAllgemein() + "tbl_verarbeitungsProtokoll vp "
                    + "WHERE ident=? AND verarbeitungslauf=?;";
            lPStm = verbindung.prepareStatement(lSql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            lPStm.setInt(1, ident);
            lPStm.setInt(2, verarbeitungslauf);

            ResultSet lErgebnis = lPStm.executeQuery();
            lErgebnis.last();
            anzInArray = lErgebnis.getRow();
            lErgebnis.beforeFirst();

            ergebnisArray = new EclVerarbeitungsProtokoll[anzInArray];

            int i = 0;
            while (lErgebnis.next() == true) {
                ergebnisArray[i] = this.decodeErgebnis(lErgebnis);
                i++;
            }

            lErgebnis.close();
            lPStm.close();

        } catch (Exception e) {
            CaBug.drucke("DbVerarbeitungsProtokoll.read 003");
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
    public int update(EclVerarbeitungsProtokoll pVerarbeitungsProtokoll) {

        try {
            String lSql = "UPDATE " + dbBundle.getSchemaAllgemein() + "tbl_verarbeitungsProtokoll SET "
                    + "mandant=?, ident=?, verarbeitungslauf=?, ergebnis=?,  "
                    + "codeVerarbeitet=?, textVerarbeitet=?, codeFehler=?, textFehler=? " + "WHERE "
                    + "ident=? AND verarbeitungslauf=?";

            PreparedStatement lPStm = verbindung.prepareStatement(lSql);
            fuellePreparedStatementKomplett(lPStm, 1, pVerarbeitungsProtokoll);
            lPStm.setInt(anzfelder + 1, pVerarbeitungsProtokoll.ident);
            lPStm.setInt(anzfelder + 2, pVerarbeitungsProtokoll.verarbeitungslauf);

            int ergebnis = lPStm.executeUpdate();
            lPStm.close();
            if (ergebnis == 0) {
                return (CaFehler.pfXyWurdeVonAnderemBenutzerVeraendert);
            }
        } catch (Exception e1) {
            CaBug.drucke("DbVerarbeitungsProtokoll.update 001");
            System.err.println(" " + e1.getMessage());
            return (CaFehler.pfdXyBereitsVorhanden);
        }

        return (1);
    }

    public int delete(int pIdent, int pVerarbeitungslauf) {

        try {

            String sql = "DELETE FROM " + dbBundle.getSchemaAllgemein()
                    + "tbl_verarbeitungsProtokoll WHERE ident=? AND verarbeitungslauf=?";

            PreparedStatement pstm1 = verbindung.prepareStatement(sql);
            pstm1.setInt(1, pIdent);
            pstm1.setInt(2, pVerarbeitungslauf);

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
