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
import de.meetingapps.meetingportal.meetComEntities.EclBerechtigungenTexte;

public class DbBerechtigungenTexte {

    private Connection verbindung = null;
    //	private DbBasis dbBasis=null;
    private DbBundle dbBundle = null;

    public EclBerechtigungenTexte ergebnisArray[] = null;

    /*************************Initialisierung**************************
     * Achtung, Abweichend vom Standard: hier wird nur DbBasis mit übergeben,
     * aus Geschwindigkeitsgründen!*/
    public DbBerechtigungenTexte(/*DbBasis pDbBasis*/DbBundle pDbBundle) {
        /* Verbindung in lokale Daten eintragen*/
        if (pDbBundle == null) {
            CaBug.drucke("DbBerechtigungenTexte.init 001 - dbBundle nicht initialisiert");
            return;
        }
        if (pDbBundle.dbBasis == null) {
            CaBug.drucke("DbBerechtigungenTexte.init 002 - dbBasis nicht initialisiert");
            return;
        }

        //		dbBasis=pDbBundle.dbBasis;
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
    public EclBerechtigungenTexte ergebnisPosition(int pN) {
        if (ergebnisArray == null) {
            CaBug.drucke("DbBerechtigungenTexte.ergebnisPosition 001");
            return null;
        }
        if (pN < 0) {
            CaBug.drucke("DbBerechtigungenTexte.ergebnisPosition 002");
            return null;
        }
        if (pN >= ergebnisArray.length) {
            CaBug.drucke("DbBerechtigungenTexte.ergebnisPosition 003");
            return null;
        }
        return ergebnisArray[pN];
    }

    /**************************deleteAll Löschen aller Datensätze**************
     * Verwendung nur für Initialisierung des Servers - nicht für Löschen eines Mandanten!*/
    public int deleteAll() {
        int erg = 0;

        try {

            String sql1 = "DELETE FROM " + dbBundle.getSchemaAllgemein() + "tbl_berechtigungentexte;";
            PreparedStatement pstm1 = verbindung.prepareStatement(sql1);

            erg = 0; /*Falls Duplicate Key, dann wird exception geschmissen und erg wohl nicht gefüllt!*/
            erg = pstm1.executeUpdate();

            pstm1.close();
        } catch (Exception e2) {
            CaBug.drucke("DbBerechtigungenTexte.deleteAll 001");
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
        rc = lDbLowLevel.createTable("CREATE TABLE " + dbBundle.getSchemaAllgemein() + "tbl_berechtigungentexte ( "
                + "`ident` int(11) NOT NULL, " + "`beschreibung` varchar(200) DEFAULT NULL, "
                + "`hauptoffset` int(11) DEFAULT NULL, " + "`nebenoffset` int(11) DEFAULT NULL, "
                + "`mandantenabhaengig` int(11) DEFAULT NULL, " + "PRIMARY KEY (`ident`) " + ") ");
        return rc;
    }

    /********** dekodiert die aktuelle Position aus ergebnis und gibt dieses zurück******/
    private EclBerechtigungenTexte decodeErgebnis(ResultSet pErgebnis) {

        EclBerechtigungenTexte lBerechtigungenTexte = new EclBerechtigungenTexte();

        try {

            lBerechtigungenTexte.ident = pErgebnis.getInt("ident");
            lBerechtigungenTexte.beschreibung = pErgebnis.getString("beschreibung");
            lBerechtigungenTexte.hauptOffset = pErgebnis.getInt("hauptOffset");
            lBerechtigungenTexte.nebenOffset = pErgebnis.getInt("nebenOffset");
            lBerechtigungenTexte.mandantenAbhaengig = pErgebnis.getInt("mandantenAbhaengig");

        } catch (Exception e) {
            CaBug.drucke("DbBerechtigungenTexte.decodeErgebnis 001");
            System.err.println(" " + e.getMessage());
        }

        return lBerechtigungenTexte;
    }

    /********************* Fuellen Prepared Statement mit allen Feldern, beginnend bei offset.*****************
     * Kann sowohl für Insert, als auch für update verwendet werden.
     * 
     * offset= Startposition des ersten Feldes (also z.B. 1)
     */
    private int anzfelder = 5; /*Anpassen auf Anzahl der Felder pro Datensatz*/

    private void fuellePreparedStatementKomplett(PreparedStatement pPStm, int pOffset,
            EclBerechtigungenTexte pBerechtigungenTexte) {

        int startOffset = pOffset; /*Nur erforderlich zum Überprüfen von Programmierfehlern - Feldanzahl*/

        try {
            pPStm.setInt(pOffset, pBerechtigungenTexte.ident);
            pOffset++;
            pPStm.setString(pOffset, pBerechtigungenTexte.beschreibung);
            pOffset++;
            pPStm.setInt(pOffset, pBerechtigungenTexte.hauptOffset);
            pOffset++;
            pPStm.setInt(pOffset, pBerechtigungenTexte.nebenOffset);
            pOffset++;
            pPStm.setInt(pOffset, pBerechtigungenTexte.mandantenAbhaengig);
            pOffset++;

            if (pOffset - startOffset != anzfelder) {
                /*Nur wg. Überprüfung auf Programmierfehler - alle Felder berücksichtigt?*/
                CaBug.drucke("DbBerechtigungenTexte.fuellePreparedStatementKomplett 002");
                System.out.println("pOffset=" + pOffset + " startOffset=" + startOffset + " anzfelder=" + anzfelder);
            }

        } catch (SQLException e) {
            CaBug.drucke("DbBerechtigungenTexte.fuellePreparedStatementKomplett 001");
            e.printStackTrace();
        }

    }

    /**Insert
     * Returnwert:
     * =1 => Insert erfolgreich
     * ansonsten: Fehler
     */
    public int insert(EclBerechtigungenTexte pBerechtigungenTexte) {
        int erg = 0;

        try {

            /*Felder Neuanlage füllen*/
            String lSql = "INSERT INTO " + dbBundle.getSchemaAllgemein() + "tbl_berechtigungentexte " + "("
                    + "ident, beschreibung, hauptOffset, nebenOffset, mandantenAbhaengig " + ")" + "VALUES ("
                    + "?, ?, ?, ?, ? " + ")";
            PreparedStatement lPStm = verbindung.prepareStatement(lSql, ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);

            fuellePreparedStatementKomplett(lPStm, 1, pBerechtigungenTexte);

            erg = lPStm.executeUpdate();
            lPStm.close();
        } catch (Exception e2) {
            CaBug.drucke("DbBerechtigungenTexte.insert 001");
            System.err.println(" " + e2.getMessage());
        }

        if (erg == 0) {/*Fehler beim Einfügen - d.h. primaryKey bereits vorhanden*/
            return (-1);
        }

        return (1);
    }

    public int read(EclBerechtigungenTexte lBerechtigungenTexte) {
        int anzInArray = 0;
        PreparedStatement lPStm = null;

        try {
            String lSql = "SELECT * from " + dbBundle.getSchemaAllgemein() + "tbl_berechtigungentexte where "
                    + "ident=?;";
            lPStm = verbindung.prepareStatement(lSql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            lPStm.setInt(1, lBerechtigungenTexte.ident);

            ResultSet lErgebnis = lPStm.executeQuery();
            lErgebnis.last();
            anzInArray = lErgebnis.getRow();
            lErgebnis.beforeFirst();

            ergebnisArray = new EclBerechtigungenTexte[anzInArray];

            int i = 0;
            while (lErgebnis.next() == true) {
                ergebnisArray[i] = this.decodeErgebnis(lErgebnis);
                i++;
            }
            lErgebnis.close();
            lPStm.close();

        } catch (Exception e) {
            CaBug.drucke("DbBerechtigungenTexte.read 003");
            System.err.println(" " + e.getMessage());
            return (-1);
        }
        return (anzInArray);
    }

    public int read_all() {
        int anzInArray = 0;
        PreparedStatement lPStm = null;

        try {
            String lSql = "SELECT * from " + dbBundle.getSchemaAllgemein() + "tbl_berechtigungentexte "
                    + "ORDER BY  ident;";
            lPStm = verbindung.prepareStatement(lSql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);

            ResultSet lErgebnis = lPStm.executeQuery();
            lErgebnis.last();
            anzInArray = lErgebnis.getRow();
            lErgebnis.beforeFirst();

            ergebnisArray = new EclBerechtigungenTexte[anzInArray];

            int i = 0;
            while (lErgebnis.next() == true) {
                ergebnisArray[i] = this.decodeErgebnis(lErgebnis);
                i++;
            }
            lErgebnis.close();
            lPStm.close();

        } catch (Exception e) {
            CaBug.drucke("DbBerechtigungenTexte.read_all 003");
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
    public int update(EclBerechtigungenTexte pBerechtigungenTexte) {

        try {

            String lSql = "UPDATE " + dbBundle.getSchemaAllgemein() + "tbl_berechtigungentexte SET "
                    + "ident=?, beschreibung=?, hauptOffset=?, nebenOffset=?, mandantenAbhaengig=? " + "WHERE "
                    + "ident=?";

            PreparedStatement lPStm = verbindung.prepareStatement(lSql);
            fuellePreparedStatementKomplett(lPStm, 1, pBerechtigungenTexte);
            lPStm.setInt(anzfelder + 1, pBerechtigungenTexte.ident);

            int ergebnis = lPStm.executeUpdate();
            lPStm.close();
            if (ergebnis == 0) {
                return (CaFehler.pfXyWurdeVonAnderemBenutzerVeraendert);
            }
        } catch (Exception e1) {
            CaBug.drucke("DbBerechtigungenTexte.update 001");
            System.err.println(" " + e1.getMessage());
            return (CaFehler.pfdXyBereitsVorhanden);
        }

        return (1);
    }

    public int delete(EclBerechtigungenTexte pBerechtigungenTexte) {

        try {

            String sql = "DELETE FROM " + dbBundle.getSchemaAllgemein() + "tbl_berechtigungentexte WHERE ident=? ";

            PreparedStatement pstm1 = verbindung.prepareStatement(sql);
            pstm1.setInt(1, pBerechtigungenTexte.ident);

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
