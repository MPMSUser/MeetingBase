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

/**Wichtig: Mandantenübergreifend*/

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import de.meetingapps.meetingportal.meetComAllg.CaBug;
import de.meetingapps.meetingportal.meetComAllg.CaFehler;
import de.meetingapps.meetingportal.meetComBVerwaltung.BvReload;
import de.meetingapps.meetingportal.meetComEntities.EclGeraeteKlasse;

public class DbGeraeteKlasse {

    private Connection verbindung = null;
    //	private DbBasis dbBasis=null;
    private DbBundle dbBundle = null;

    public EclGeraeteKlasse ergebnisArray[] = null;

    /*************************Initialisierung***************************/
    public DbGeraeteKlasse(DbBundle pDbBundle) {
        /* Verbindung in lokale Daten eintragen*/
        if (pDbBundle == null) {
            CaBug.drucke("DbGeraeteKlasse.init 001 - dbBundle nicht initialisiert");
            return;
        }
        if (pDbBundle.dbBasis == null) {
            CaBug.drucke("DbGeraeteKlasse.init 002 - dbBasis nicht initialisiert");
            return;
        }

        //		dbBasis=pDbBundle.dbBasis;
        dbBundle = pDbBundle;
        verbindung = pDbBundle.dbBasis.verbindung;
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
    public EclGeraeteKlasse ergebnisPosition(int pN) {
        if (ergebnisArray == null) {
            CaBug.drucke("DbGeraeteKlasse.ergebnisPosition 001");
            return null;
        }
        if (pN < 0) {
            CaBug.drucke("DbGeraeteKlasse.ergebnisPosition 002");
            return null;
        }
        if (pN >= ergebnisArray.length) {
            CaBug.drucke("DbGeraeteKlasse.ergebnisPosition 003");
            return null;
        }
        return ergebnisArray[pN];
    }

    public int createTable() {
        int rc = 0;
        DbLowLevel lDbLowLevel = new DbLowLevel(dbBundle);
        rc = lDbLowLevel.createTable(
                "CREATE TABLE " + dbBundle.getSchemaAllgemein() + "tbl_geraeteklasse ( " + "`ident` int(11) NOT NULL, "
                        + "`beschreibung` varchar(500) DEFAULT NULL, " + "PRIMARY KEY (`ident`) " + ") ");
        return rc;
    }

    /**************************deleteAll Löschen aller Datensätze***************
     * Wurde entfernt, da nicht mandantenabhängig*/

    /********** dekodiert die aktuelle Position aus ergebnis in EclAktienregisterEintrag und gibt dieses zurück******/
    private EclGeraeteKlasse decodeErgebnis(ResultSet pErgebnis) {

        EclGeraeteKlasse lGeraeteKlasse = new EclGeraeteKlasse();

        try {

            lGeraeteKlasse.ident = pErgebnis.getInt("ident");
            lGeraeteKlasse.kurzBeschreibung = pErgebnis.getString("kurzBeschreibung");
            lGeraeteKlasse.beschreibung = pErgebnis.getString("beschreibung");

        } catch (Exception e) {
            CaBug.drucke("DbGeraeteKlasse.decodeErgebnis 001");
            System.err.println(" " + e.getMessage());
        }

        return lGeraeteKlasse;
    }

    /********************* Fuellen Prepared Statement mit allen Feldern, beginnend bei offset.*****************
     * Kann sowohl für Insert, als auch für update verwendet werden.
     * 
     * offset= Startposition des ersten Feldes (also z.B. 1)
     */
    private int anzfelder = 3; /*Anpassen auf Anzahl der Felder pro Datensatz*/

    private void fuellePreparedStatementKomplett(PreparedStatement pPStm, int pOffset,
            EclGeraeteKlasse pGeraeteKlasse) {

        int startOffset = pOffset; /*Nur erforderlich zum Überprüfen von Programmierfehlern - Feldanzahl*/

        try {
            pPStm.setInt(pOffset, pGeraeteKlasse.ident);
            pOffset++;
            pPStm.setString(pOffset, pGeraeteKlasse.kurzBeschreibung);
            pOffset++;
            pPStm.setString(pOffset, pGeraeteKlasse.beschreibung);
            pOffset++;

            if (pOffset - startOffset != anzfelder) {
                /*Nur wg. Überprüfung auf Programmierfehler - alle Felder berücksichtigt?*/
                CaBug.drucke("DbGeraeteKlasse.fuellePreparedStatementKomplett 002");
                System.out.println("pOffset=" + pOffset + " startOffset=" + startOffset + " anzfelder=" + anzfelder);
            }

        } catch (SQLException e) {
            CaBug.drucke("DbGeraeteKlasse.fuellePreparedStatementKomplett 001");
            e.printStackTrace();
        }

    }

    /**Insert
     * 
     * Ident muß vom Aufrufer vergeben werden - keine automatische Vergabe, damit Gruppenbildung möglich ist.
     * Returnwert:
     * =1 => Insert erfolgreich
     * ansonsten: Fehler
     */
    public int insert(EclGeraeteKlasse pGeraeteKlasse) {
        int erg = 0;

        try {

            /*Felder Neuanlage füllen*/
            String lSql = "INSERT INTO " + dbBundle.getSchemaAllgemein() + "tbl_geraeteklasse " + "("
                    + "ident, kurzBeschreibung, beschreibung " + ")" + "VALUES (" + "?, ?, ? " + ")";
            PreparedStatement lPStm = verbindung.prepareStatement(lSql, ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);

            fuellePreparedStatementKomplett(lPStm, 1, pGeraeteKlasse);

            erg = lPStm.executeUpdate();
            lPStm.close();
        } catch (Exception e2) {
            CaBug.drucke("DbGeraeteKlasse.insert 001");
            System.err.println(" " + e2.getMessage());
        }

        if (erg == 0) {/*Fehler beim Einfügen - d.h. primaryKey bereits vorhanden*/
            return (-1);
        }

        BvReload bvReload = new BvReload(dbBundle);
        bvReload.setReloadParameterGeraete();

        return (1);
    }

    public int read(int pGeraeteKlasse) {
        int anzInArray = 0;
        PreparedStatement lPStm = null;

        try {
            String lSql = "SELECT * from " + dbBundle.getSchemaAllgemein() + "tbl_geraeteklasse where " + "ident=?;";
            lPStm = verbindung.prepareStatement(lSql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            lPStm.setInt(1, pGeraeteKlasse);

            ResultSet lErgebnis = lPStm.executeQuery();
            lErgebnis.last();
            anzInArray = lErgebnis.getRow();
            lErgebnis.beforeFirst();

            ergebnisArray = new EclGeraeteKlasse[anzInArray];

            int i = 0;
            while (lErgebnis.next() == true) {
                ergebnisArray[i] = this.decodeErgebnis(lErgebnis);
                i++;
            }
            lErgebnis.close();
            lPStm.close();

        } catch (Exception e) {
            CaBug.drucke("DbGeraeteKlasse.read 003");
            System.err.println(" " + e.getMessage());
            return (-1);
        }
        return (anzInArray);
    }

    public int read_all() {
        int anzInArray = 0;
        PreparedStatement lPStm = null;

        try {
            String lSql = "SELECT * from " + dbBundle.getSchemaAllgemein() + "tbl_geraeteklasse " + "ORDER BY ident;";
            lPStm = verbindung.prepareStatement(lSql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);

            ResultSet lErgebnis = lPStm.executeQuery();
            lErgebnis.last();
            anzInArray = lErgebnis.getRow();
            lErgebnis.beforeFirst();

            ergebnisArray = new EclGeraeteKlasse[anzInArray];

            int i = 0;
            while (lErgebnis.next() == true) {
                ergebnisArray[i] = this.decodeErgebnis(lErgebnis);
                i++;
            }
            lErgebnis.close();
            lPStm.close();

        } catch (Exception e) {
            CaBug.drucke("DbGeraeteKlasse.read_all 003");
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
    public int update(EclGeraeteKlasse pGeraeteKlasse) {

        try {

            String lSql = "UPDATE " + dbBundle.getSchemaAllgemein() + "tbl_geraeteklasse SET "
                    + "ident=?, kurzBeschreibung=?, beschreibung=? " + "WHERE " + "ident=?";

            PreparedStatement lPStm = verbindung.prepareStatement(lSql);
            fuellePreparedStatementKomplett(lPStm, 1, pGeraeteKlasse);
            lPStm.setInt(anzfelder + 1, pGeraeteKlasse.ident);

            int ergebnis = lPStm.executeUpdate();
            lPStm.close();
            if (ergebnis == 0) {
                return (CaFehler.pfXyWurdeVonAnderemBenutzerVeraendert);
            }
        } catch (Exception e1) {
            CaBug.drucke("DbGeraeteKlasse.update 001");
            System.err.println(" " + e1.getMessage());
            return (CaFehler.pfdXyBereitsVorhanden);
        }

        BvReload bvReload = new BvReload(dbBundle);
        bvReload.setReloadParameterGeraete();

        return (1);
    }

    public int delete(int ident) {

        try {

            String sql = "DELETE FROM " + dbBundle.getSchemaAllgemein() + "tbl_geraeteklasse WHERE ident=? ";

            PreparedStatement pstm1 = verbindung.prepareStatement(sql);
            pstm1.setInt(1, ident);

            int ergebnis1 = pstm1.executeUpdate();
            pstm1.close();
            if (ergebnis1 == 0) {
                return (CaFehler.pfXyWurdeVonAnderemBenutzerVeraendert);
            }
        } catch (Exception e1) {
            CaBug.drucke("DbGeraeteKlasse.delete 001");
            System.err.println(" " + e1.getMessage());
            return (CaFehler.pfdXyBereitsVorhanden);
        }

        BvReload bvReload = new BvReload(dbBundle);
        bvReload.setReloadParameterGeraete();

        return (1);
    }

}
