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
import de.meetingapps.meetingportal.meetComEntities.EclGeraetKlasseSetZuordnung;

public class DbGeraeteKlasseSetZuordnung {

    private Connection verbindung = null;
    //	private DbBasis dbBasis=null;
    private DbBundle dbBundle = null;

    public EclGeraetKlasseSetZuordnung ergebnisArray[] = null;

    /*************************Initialisierung***************************/
    public DbGeraeteKlasseSetZuordnung(DbBundle pDbBundle) {
        /* Verbindung in lokale Daten eintragen*/
        if (pDbBundle == null) {
            CaBug.drucke("DbGeraeteKlasseSetZuordnung.init 001 - dbBundle nicht initialisiert");
            return;
        }
        if (pDbBundle.dbBasis == null) {
            CaBug.drucke("DbGeraeteKlasseSetZuordnung.init 002 - dbBasis nicht initialisiert");
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
    public EclGeraetKlasseSetZuordnung ergebnisPosition(int pN) {
        if (ergebnisArray == null) {
            CaBug.drucke("DbGeraeteKlasseSetZuordnung.ergebnisPosition 001");
            return null;
        }
        if (pN < 0) {
            CaBug.drucke("DbGeraeteKlasseSetZuordnung.ergebnisPosition 002");
            return null;
        }
        if (pN >= ergebnisArray.length) {
            CaBug.drucke("DbGeraeteKlasseSetZuordnung.ergebnisPosition 003");
            return null;
        }
        return ergebnisArray[pN];
    }

    public int createTable() {
        int rc = 0;
        DbLowLevel lDbLowLevel = new DbLowLevel(dbBundle);
        rc = lDbLowLevel.createTable("CREATE TABLE " + dbBundle.getSchemaAllgemein() + "tbl_geraetklassesetzuordnung ( "
                + "`geraeteSetIdent` int(11) NOT NULL, " + "`geraeteNummer` int(11) NOT NULL, "
                + "`geraeteKlasseIdent` int(11) DEFAULT NULL, " + "PRIMARY KEY (`geraeteSetIdent`,`geraeteNummer`) "
                + ") ");
        return rc;
    }

    /**************************deleteAll Löschen aller Datensätze***************
     * Wurde entfernt, da nicht mandantenabhängig*/

    /********** dekodiert die aktuelle Position aus ergebnis in EclAktienregisterEintrag und gibt dieses zurück******/
    private EclGeraetKlasseSetZuordnung decodeErgebnis(ResultSet pErgebnis) {

        EclGeraetKlasseSetZuordnung lGeraetKlasseSetZuordnung = new EclGeraetKlasseSetZuordnung();

        try {

            lGeraetKlasseSetZuordnung.geraeteSetIdent = pErgebnis.getInt("geraeteSetIdent");
            lGeraetKlasseSetZuordnung.geraeteNummer = pErgebnis.getInt("geraeteNummer");
            lGeraetKlasseSetZuordnung.geraeteKlasseIdent = pErgebnis.getInt("geraeteKlasseIdent");
            lGeraetKlasseSetZuordnung.standortIdent = pErgebnis.getInt("standortIdent");
            lGeraetKlasseSetZuordnung.lokaleDatenZuruecksetzenBeimNaechstenStart = pErgebnis
                    .getInt("lokaleDatenZuruecksetzenBeimNaechstenStart");

        } catch (Exception e) {
            CaBug.drucke("DbGeraeteKlasseSetZuordnung.decodeErgebnis 001");
            System.err.println(" " + e.getMessage());
        }

        return lGeraetKlasseSetZuordnung;
    }

    /********************* Fuellen Prepared Statement mit allen Feldern, beginnend bei offset.*****************
     * Kann sowohl für Insert, als auch für update verwendet werden.
     * 
     * offset= Startposition des ersten Feldes (also z.B. 1)
     */
    private int anzfelder = 5; /*Anpassen auf Anzahl der Felder pro Datensatz*/

    private void fuellePreparedStatementKomplett(PreparedStatement pPStm, int pOffset,
            EclGeraetKlasseSetZuordnung pGeraetKlasseSetZuordnung) {

        int startOffset = pOffset; /*Nur erforderlich zum Überprüfen von Programmierfehlern - Feldanzahl*/

        try {
            pPStm.setInt(pOffset, pGeraetKlasseSetZuordnung.geraeteSetIdent);
            pOffset++;
            pPStm.setInt(pOffset, pGeraetKlasseSetZuordnung.geraeteNummer);
            pOffset++;
            pPStm.setInt(pOffset, pGeraetKlasseSetZuordnung.geraeteKlasseIdent);
            pOffset++;
            pPStm.setInt(pOffset, pGeraetKlasseSetZuordnung.standortIdent);
            pOffset++;
            pPStm.setInt(pOffset, pGeraetKlasseSetZuordnung.lokaleDatenZuruecksetzenBeimNaechstenStart);
            pOffset++;

            if (pOffset - startOffset != anzfelder) {
                /*Nur wg. Überprüfung auf Programmierfehler - alle Felder berücksichtigt?*/
                CaBug.drucke("DbGeraeteKlasseSetZuordnung.fuellePreparedStatementKomplett 002");
                System.out.println("pOffset=" + pOffset + " startOffset=" + startOffset + " anzfelder=" + anzfelder);
            }

        } catch (SQLException e) {
            CaBug.drucke("DbGeraeteKlasseSetZuordnung.fuellePreparedStatementKomplett 001");
            e.printStackTrace();
        }

    }

    /**Insert
     * Returnwert:
     * =1 => Insert erfolgreich
     * ansonsten: Fehler
     */
    public int insert(EclGeraetKlasseSetZuordnung pGeraetKlasseSetZuordnung) {
        int erg = 0;

        try {

            /*Felder Neuanlage füllen*/
            String lSql = "INSERT INTO " + dbBundle.getSchemaAllgemein() + "tbl_geraetKlasseSetZuordnung " + "("
                    + "geraeteSetIdent, geraeteNummer, geraeteKlasseIdent, standortIdent, lokaleDatenZuruecksetzenBeimNaechstenStart "
                    + ")" + "VALUES (" + "?, ?, ?, ?, ? " + ")";
            PreparedStatement lPStm = verbindung.prepareStatement(lSql, ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);

            fuellePreparedStatementKomplett(lPStm, 1, pGeraetKlasseSetZuordnung);

            erg = lPStm.executeUpdate();
            lPStm.close();
        } catch (Exception e2) {
            CaBug.drucke("DbGeraeteKlasseSetZuordnung.insert 001");
            System.err.println(" " + e2.getMessage());
        }

        if (erg == 0) {/*Fehler beim Einfügen - d.h. primaryKey bereits vorhanden*/
            return (-1);
        }

        return (1);
    }

    public int read(int pGeraeteSetident, int pGeraeteNummer) {
        int anzInArray = 0;
        PreparedStatement lPStm = null;

        try {
            String lSql = "SELECT * from " + dbBundle.getSchemaAllgemein() + "tbl_geraetKlasseSetZuordnung where "
                    + "geraeteSetIdent=? AND geraeteNummer=?;";
            lPStm = verbindung.prepareStatement(lSql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            lPStm.setInt(1, pGeraeteSetident);
            lPStm.setInt(2, pGeraeteNummer);

            ResultSet lErgebnis = lPStm.executeQuery();
            lErgebnis.last();
            anzInArray = lErgebnis.getRow();
            lErgebnis.beforeFirst();

            ergebnisArray = new EclGeraetKlasseSetZuordnung[anzInArray];

            int i = 0;
            while (lErgebnis.next() == true) {
                ergebnisArray[i] = this.decodeErgebnis(lErgebnis);
                i++;
            }
            lErgebnis.close();
            lPStm.close();

        } catch (Exception e) {
            CaBug.drucke("DbGeraeteKlasseSetZuordnung.read 003");
            System.err.println(" " + e.getMessage());
            return (-1);
        }
        return (anzInArray);
    }

    public int readZuGeraeteSet_all(int pGeraeteSetident) {
        int anzInArray = 0;
        PreparedStatement lPStm = null;

        try {
            String lSql = "SELECT * from " + dbBundle.getSchemaAllgemein() + "tbl_geraetKlasseSetZuordnung where "
                    + "geraeteSetIdent=?;";
            lPStm = verbindung.prepareStatement(lSql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            lPStm.setInt(1, pGeraeteSetident);

            ResultSet lErgebnis = lPStm.executeQuery();
            lErgebnis.last();
            anzInArray = lErgebnis.getRow();
            lErgebnis.beforeFirst();

            ergebnisArray = new EclGeraetKlasseSetZuordnung[anzInArray];

            int i = 0;
            while (lErgebnis.next() == true) {
                ergebnisArray[i] = this.decodeErgebnis(lErgebnis);
                i++;
            }
            lErgebnis.close();
            lPStm.close();

        } catch (Exception e) {
            CaBug.drucke("DbGeraeteKlasseSetZuordnung.readZuGeraeteSet_all 003");
            System.err.println(" " + e.getMessage());
            return (-1);
        }
        return (anzInArray);
    }

    public int readZuGeraeteSet_all_nurKlasseIdentUnique(int pGeraeteSetident) {
        int anzInArray = 0;
        PreparedStatement lPStm = null;

        try {
            String lSql = "SELECT DISTINCT geraeteKlasseIdent from " + dbBundle.getSchemaAllgemein()
                    + "tbl_geraetKlasseSetZuordnung where " + "geraeteSetIdent=?;";
            lPStm = verbindung.prepareStatement(lSql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            lPStm.setInt(1, pGeraeteSetident);

            ResultSet lErgebnis = lPStm.executeQuery();
            lErgebnis.last();
            anzInArray = lErgebnis.getRow();
            lErgebnis.beforeFirst();

            ergebnisArray = new EclGeraetKlasseSetZuordnung[anzInArray];

            int i = 0;
            while (lErgebnis.next() == true) {
                ergebnisArray[i] = new EclGeraetKlasseSetZuordnung();
                ;
                ergebnisArray[i].geraeteKlasseIdent = lErgebnis.getInt("geraeteKlasseIdent");
                i++;
            }
            lErgebnis.close();
            lPStm.close();

        } catch (Exception e) {
            CaBug.drucke("DbGeraeteKlasseSetZuordnung.readZuGeraeteSet_all_nurKlasseIdentUnique 003");
            System.err.println(" " + e.getMessage());
            return (-1);
        }
        return (anzInArray);
    }

    public int readZuGeraeteKlasse_all(int pGeraeteKlasseIdent) {
        int anzInArray = 0;
        PreparedStatement lPStm = null;

        try {
            String lSql = "SELECT * from " + dbBundle.getSchemaAllgemein() + "tbl_geraetKlasseSetZuordnung where "
                    + "geraeteKlasseIdent=?;";
            lPStm = verbindung.prepareStatement(lSql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            lPStm.setInt(1, pGeraeteKlasseIdent);

            ResultSet lErgebnis = lPStm.executeQuery();
            lErgebnis.last();
            anzInArray = lErgebnis.getRow();
            lErgebnis.beforeFirst();

            ergebnisArray = new EclGeraetKlasseSetZuordnung[anzInArray];

            int i = 0;
            while (lErgebnis.next() == true) {
                ergebnisArray[i] = this.decodeErgebnis(lErgebnis);
                i++;
            }
            lErgebnis.close();
            lPStm.close();

        } catch (Exception e) {
            CaBug.drucke("DbGeraeteKlasseSetZuordnung.readZuGeraeteKlasse_all 003");
            System.err.println(" " + e.getMessage());
            return (-1);
        }
        return (anzInArray);
    }

    /*TODO _Parameter Konsolidieren: Noch nicht angepaßt - abhängig von Pflegefunktionen. Überprüfen, ob noch erforderlich*/
    public int read_all() {
        int anzInArray = 0;
        PreparedStatement lPStm = null;

        try {
            String lSql = "SELECT * from " + dbBundle.getSchemaAllgemein() + "tbl_geraetKlasseSetZuordnung "
                    + "ORDER BY ident;";
            lPStm = verbindung.prepareStatement(lSql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);

            ResultSet lErgebnis = lPStm.executeQuery();
            lErgebnis.last();
            anzInArray = lErgebnis.getRow();
            lErgebnis.beforeFirst();

            ergebnisArray = new EclGeraetKlasseSetZuordnung[anzInArray];

            int i = 0;
            while (lErgebnis.next() == true) {
                ergebnisArray[i] = this.decodeErgebnis(lErgebnis);
                i++;
            }
            lErgebnis.close();
            lPStm.close();

        } catch (Exception e) {
            CaBug.drucke("DbGeraeteKlasseSetZuordnung.read_all 003");
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
    public int update(EclGeraetKlasseSetZuordnung pGeraetKlasseSetZuordnung) {

        try {

            String lSql = "UPDATE " + dbBundle.getSchemaAllgemein() + "tbl_geraetKlasseSetZuordnung SET "
                    + "geraeteSetIdent=?, geraeteNummer=?, geraeteKlasseIdent=?, standortIdent=?, lokaleDatenZuruecksetzenBeimNaechstenStart=? "
                    + "WHERE " + "geraeteSetIdent=? AND geraeteNummer=?";

            PreparedStatement lPStm = verbindung.prepareStatement(lSql);
            fuellePreparedStatementKomplett(lPStm, 1, pGeraetKlasseSetZuordnung);
            lPStm.setInt(anzfelder + 1, pGeraetKlasseSetZuordnung.geraeteSetIdent);
            lPStm.setInt(anzfelder + 2, pGeraetKlasseSetZuordnung.geraeteNummer);

            int ergebnis = lPStm.executeUpdate();
            lPStm.close();
            if (ergebnis == 0) {
                return (CaFehler.pfXyWurdeVonAnderemBenutzerVeraendert);
            }
        } catch (Exception e1) {
            CaBug.drucke("DbGeraeteKlasseSetZuordnung.update 001");
            System.err.println(" " + e1.getMessage());
            return (CaFehler.pfdXyBereitsVorhanden);
        }

        BvReload bvReload = new BvReload(dbBundle);
        bvReload.setReloadParameterGeraete();

        return (1);
    }

    /*So wahrscheinlich nicht brauchbar!*/
    public int delete(int pGeraeteSetIdent, int geraeteNummer) {

        try {

            String sql = "DELETE FROM " + dbBundle.getSchemaAllgemein()
                    + "tbl_geraetKlasseSetZuordnung WHERE geraeteSetIdent=? AND geraeteNummer=? ";

            PreparedStatement pstm1 = verbindung.prepareStatement(sql);
            pstm1.setInt(1, pGeraeteSetIdent);
            pstm1.setInt(2, geraeteNummer);

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
