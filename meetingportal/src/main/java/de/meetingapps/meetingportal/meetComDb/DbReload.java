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

/**Wichtig: diese Table muß - wg. Zugriffsverfahren und Zugriffsgeschwindigkeit - komplett 
 * in das Mandantenübergreifende Schema!*/

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import de.meetingapps.meetingportal.meetComAllg.CaBug;
import de.meetingapps.meetingportal.meetComAllg.CaFehler;
import de.meetingapps.meetingportal.meetComEntities.EclReload;

public class DbReload {

    private Connection verbindung = null;
    private DbBasis dbBasis = null;
    private DbBundle dbBundle = null;

    public EclReload ergebnisArray[] = null;

    /*************************Initialisierung**************************
     * Achtung, Abweichend vom Standard: hier wird nur DbBasis mit übergeben,
     * aus Geschwindigkeitsgründen!*/
    public DbReload(DbBundle pDbBundle) {
        /* Verbindung in lokale Daten eintragen*/
        if (pDbBundle == null) {
            CaBug.drucke("DbReload.init 002 - pDbBundle nicht initialisiert");
            return;
        }

        dbBundle = pDbBundle;
        dbBasis = pDbBundle.dbBasis;
        verbindung = dbBasis.verbindung;
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
    public EclReload ergebnisPosition(int pN) {
        if (ergebnisArray == null) {
            CaBug.drucke("DbReload.ergebnisPosition 001");
            return null;
        }
        if (pN < 0) {
            CaBug.drucke("DbReload.ergebnisPosition 002");
            return null;
        }
        if (pN >= ergebnisArray.length) {
            CaBug.drucke("DbReload.ergebnisPosition 003");
            return null;
        }
        return ergebnisArray[pN];
    }

    /**************************deleteAll Löschen aller Datensätze**************
     * Verwendung nur für Initialisierung des Servers - nicht für Löschen eines Mandanten!*/
    public int deleteAll() {
        int erg = 0;

        try {

            String sql1 = "DELETE FROM " + dbBundle.getSchemaAllgemein() + "tbl_reload;";
            PreparedStatement pstm1 = verbindung.prepareStatement(sql1);

            erg = 0; /*Falls Duplicate Key, dann wird exception geschmissen und erg wohl nicht gefüllt!*/
            erg = pstm1.executeUpdate();

            pstm1.close();
        } catch (Exception e2) {
            CaBug.drucke("DbReload.deleteAll 001");
            System.err.println(" " + e2.getMessage());
            return (erg);
        }

        return 1;
    }

    /************Neuanlegen Table*****************************
     * Sondersituation: pDbBundle muß hier übergeben werden,
     * da dies für die restlichen Funktionen nicht benötigt
     * wird (aus Performancegründen)*/
    public int createTable() {
        int rc = 0;
        DbLowLevel lDbLowLevel = new DbLowLevel(dbBundle);
        rc = lDbLowLevel.createTable("CREATE TABLE " + dbBundle.getSchemaAllgemein() + "tbl_reload ( "
                + "`mandant` INT(11) NOT NULL DEFAULT 0, " + "`ident` INT(11) NOT NULL DEFAULT 0, "
                + "`reload` INT(11) NULL DEFAULT 0, " + "PRIMARY KEY (`mandant`,`ident`)" + ")  ");
        return rc;
    }

    /********** dekodiert die aktuelle Position aus ergebnis und gibt dieses zurück******/
    private EclReload decodeErgebnis(ResultSet pErgebnis) {

        EclReload lReload = new EclReload();

        try {

            lReload.mandant = pErgebnis.getInt("mandant");
            lReload.ident = pErgebnis.getInt("ident");
            lReload.reload = pErgebnis.getInt("reload");

        } catch (Exception e) {
            CaBug.drucke("DbReload.decodeErgebnis 001");
            System.err.println(" " + e.getMessage());
        }

        return lReload;
    }

    /********************* Fuellen Prepared Statement mit allen Feldern, beginnend bei offset.*****************
     * Kann sowohl für Insert, als auch für update verwendet werden.
     * 
     * offset= Startposition des ersten Feldes (also z.B. 1)
     */
    private int anzfelder = 3; /*Anpassen auf Anzahl der Felder pro Datensatz*/

    private void fuellePreparedStatementKomplett(PreparedStatement pPStm, int pOffset, EclReload pReload) {

        int startOffset = pOffset; /*Nur erforderlich zum Überprüfen von Programmierfehlern - Feldanzahl*/

        try {
            pPStm.setInt(pOffset, pReload.mandant);
            pOffset++;
            pPStm.setInt(pOffset, pReload.ident);
            pOffset++;
            pPStm.setInt(pOffset, pReload.reload);
            pOffset++;

            if (pOffset - startOffset != anzfelder) {
                /*Nur wg. Überprüfung auf Programmierfehler - alle Felder berücksichtigt?*/
                CaBug.drucke("DbReload.fuellePreparedStatementKomplett 002");
                System.out.println("pOffset=" + pOffset + " startOffset=" + startOffset + " anzfelder=" + anzfelder);
            }

        } catch (SQLException e) {
            CaBug.drucke("DbReload.fuellePreparedStatementKomplett 001");
            e.printStackTrace();
        }

    }

    /**Insert
     * Returnwert:
     * =1 => Insert erfolgreich
     * ansonsten: Fehler
     */
    public int insert(EclReload pReload) {
        int erg = 0;

        try {

            /*Felder Neuanlage füllen*/
            String lSql = "INSERT INTO " + dbBundle.getSchemaAllgemein() + "tbl_reload " + "("
                    + "mandant, ident, reload " + ")" + "VALUES (" + "?, ?, ? " + ")";
            PreparedStatement lPStm = verbindung.prepareStatement(lSql, ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);

            fuellePreparedStatementKomplett(lPStm, 1, pReload);

            erg = lPStm.executeUpdate();
            lPStm.close();
        } catch (Exception e2) {
            CaBug.drucke("DbReload.insert 001");
            System.err.println(" " + e2.getMessage());
        }

        if (erg == 0) {/*Fehler beim Einfügen - d.h. primaryKey bereits vorhanden*/
            return (-1);
        }

        return (1);
    }

    public int read(EclReload lReload) {
        int anzInArray = 0;
        PreparedStatement lPStm = null;

        try {
            String lSql = "SELECT * from " + dbBundle.getSchemaAllgemein() + "tbl_reload where "
                    + "mandant=? AND ident=?;";
            lPStm = verbindung.prepareStatement(lSql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            lPStm.setInt(1, lReload.mandant);
            lPStm.setInt(2, lReload.ident);

            ResultSet lErgebnis = lPStm.executeQuery();
            lErgebnis.last();
            anzInArray = lErgebnis.getRow();
            lErgebnis.beforeFirst();

            ergebnisArray = new EclReload[anzInArray];

            int i = 0;
            while (lErgebnis.next() == true) {
                ergebnisArray[i] = this.decodeErgebnis(lErgebnis);
                i++;
            }
            lErgebnis.close();
            lPStm.close();

        } catch (Exception e) {
            CaBug.drucke("DbReload.read 003");
            System.err.println(" " + e.getMessage());
            return (-1);
        }
        return (anzInArray);
    }

    public int read_all(int pMandant) {
        int anzInArray = 0;
        PreparedStatement lPStm = null;

        try {
            String lSql = "SELECT * from " + dbBundle.getSchemaAllgemein() + "tbl_reload WHERE mandant=0 OR mandant=? "
                    + "ORDER BY mandant, ident;";
            lPStm = verbindung.prepareStatement(lSql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            lPStm.setInt(1, pMandant);

            ResultSet lErgebnis = lPStm.executeQuery();
            lErgebnis.last();
            anzInArray = lErgebnis.getRow();
            lErgebnis.beforeFirst();

            ergebnisArray = new EclReload[anzInArray];

            int i = 0;
            while (lErgebnis.next() == true) {
                ergebnisArray[i] = this.decodeErgebnis(lErgebnis);
                i++;
            }
            lErgebnis.close();
            lPStm.close();

        } catch (Exception e) {
            CaBug.drucke("DbReload.read_all 003");
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
     * 
     * Möglicherweise noch anpassen - nach dem Motto: falls noch nicht vorhanden, dann Updaten, ansonsten einfügen
     */
    public int update(EclReload pReload) {

        try {

            String lSql = "UPDATE " + dbBundle.getSchemaAllgemein() + "tbl_reload SET "
                    + "mandant=?, ident=?, reload=? " + "WHERE " + "mandant=? AND ident=?";

            PreparedStatement lPStm = verbindung.prepareStatement(lSql);
            fuellePreparedStatementKomplett(lPStm, 1, pReload);
            lPStm.setInt(anzfelder + 1, pReload.mandant);
            lPStm.setInt(anzfelder + 2, pReload.ident);

            int ergebnis = lPStm.executeUpdate();
            lPStm.close();
            if (ergebnis == 0) {
                return (CaFehler.pfXyWurdeVonAnderemBenutzerVeraendert);
            }
        } catch (Exception e1) {
            CaBug.drucke("DbReload.update 001");
            System.err.println(" " + e1.getMessage());
            return (CaFehler.pfdXyBereitsVorhanden);
        }

        return (1);
    }

    
    /**Gefüllt sein muß mandant, ident, reload (alter Wert)
     * Liefert ggf. CaFehler.pfXyWurdeVonAnderemBenutzerVeraendert*/
    public int updateErhoheVersionsnummer(EclReload pReload) {

        pReload.reload++;
        
        try {

            String lSql = "UPDATE " + dbBundle.getSchemaAllgemein() + "tbl_reload SET "
                    + "mandant=?, ident=?, reload=? " + "WHERE " + "mandant=? AND ident=? AND reload=? ";

            PreparedStatement lPStm = verbindung.prepareStatement(lSql);
            fuellePreparedStatementKomplett(lPStm, 1, pReload);
            lPStm.setInt(anzfelder + 1, pReload.mandant);
            lPStm.setInt(anzfelder + 2, pReload.ident);
            lPStm.setInt(anzfelder + 3, pReload.reload-1);

            int ergebnis = lPStm.executeUpdate();
            lPStm.close();
            if (ergebnis == 0) {
                return (CaFehler.pfXyWurdeVonAnderemBenutzerVeraendert);
            }
        } catch (Exception e1) {
            CaBug.drucke("DbReload.update 001");
            System.err.println(" " + e1.getMessage());
            return (CaFehler.pfdXyBereitsVorhanden);
        }

        return (1);
    }

    
    public int delete(EclReload pReload) {

        try {

            String sql = "DELETE FROM " + dbBundle.getSchemaAllgemein() + "tbl_reload WHERE mandant=? AND ident=? ";

            PreparedStatement pstm1 = verbindung.prepareStatement(sql);
            pstm1.setInt(1, pReload.mandant);
            pstm1.setInt(2, pReload.ident);

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
