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
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import de.meetingapps.meetingportal.meetComAllg.CaBug;
import de.meetingapps.meetingportal.meetComAllg.CaFehler;
import de.meetingapps.meetingportal.meetComEntities.EclNavToBM;

public class DbNavToBM {

    private Connection verbindung = null;
    private DbBundle dbBundle = null;

    public List<EclNavToBM> ergebnisList = null;

    /************************* Initialisierung ***************************/
    public DbNavToBM(DbBundle pDbBundle) {
        /* Verbindung in lokale Daten eintragen */
        if (pDbBundle == null) {
            CaBug.drucke("DbNavToBM.init 001 - dbBundle nicht initialisiert");
            return;
        }
        if (pDbBundle.dbBasis == null) {
            CaBug.drucke("DbNavToBM.init 002 - dbBasis nicht initialisiert");
            return;
        }

        verbindung = pDbBundle.dbBasis.verbindung;
        dbBundle = pDbBundle;
    }

    /**************
     * Anzahl der Ergebnisse der read*-Methoden
     **************************/
    public int anzErgebnis() {
        if (ergebnisList == null) {
            return 0;
        }
        return ergebnisList.size();
    }

    /**********
     * Liefert pN-tes Element des Ergebnisses der read*Methoden************** pN
     * geht von 0 bis anzErgebnis-1
     */
    public EclNavToBM ergebnisPosition(int pN) {
        if (ergebnisList == null) {
            CaBug.drucke("DbNavToBM.ergebnisPosition 001");
            return null;
        }
        if (pN < 0) {
            CaBug.drucke("DbNavToBM.ergebnisPosition 002");
            return null;
        }
        if (pN >= ergebnisList.size()) {
            CaBug.drucke("DbNavToBM.ergebnisPosition 003");
            return null;
        }
        return ergebnisList.get(pN);
    }

    /**************************
     * deleteAll Löschen aller Datensätze eines Mandanten
     ****************/
    public int deleteAll() {
        return dbBundle.dbLowLevel
                .deleteMandant("DELETE FROM " + dbBundle.getSchemaAllgemein() + "tbl_navtobm where ident !=0;");
    }

    /******* Checken, ob table überhaupt vorhanden ist ***************************/
    public boolean checkTableVorhanden() {
        DbLowLevel lDbLowLevel = new DbLowLevel(dbBundle);

        return lDbLowLevel.checkTableVorhandenNew(dbBundle.getSchemaAllgemein().replace(".", ""), "tbl_koordinaten");
    }

    public boolean checkTableVorhandenNew(String schema) {
        DbLowLevel lDbLowLevel = new DbLowLevel(dbBundle);
        return lDbLowLevel.checkTableVorhandenNew(schema, "tbl_navtobm");
    }

    /************ Neuanlegen Table ******************************/
    public int createTable() {
        int rc = 0;
        DbLowLevel lDbLowLevel = new DbLowLevel(dbBundle);
//		@formatter:off
		rc = lDbLowLevel.createTable(
				"CREATE TABLE IF NOT EXISTS " + dbBundle.getSchemaAllgemein() + "tbl_navtobm ( "
				+ "`ident` int(11) NOT NULL, " 
				+ "`description` varchar(60) NOT NULL, " 
				+ "`db_nav` varchar(45) NOT NULL, "
				+ "`db_bm` varchar(20) NOT NULL, "
				+ "`import` tinyint(1) NOT NULL, "
				+ "`updated` TIMESTAMP NOT NULL, "
				+ "PRIMARY KEY (`ident`) " 
				+ ")");
//		@formatter:on
        System.out.println("DbNavToBM create table rc=" + rc);
        return rc;
    }

    //	

    /**********
     * dekodiert die aktuelle Position aus ergebnis in EclAktienregisterEintrag und
     * gibt dieses zurück
     ******/
    EclNavToBM decodeErgebnis(ResultSet pErgebnis) {

        EclNavToBM pData = new EclNavToBM();

        try {
            pData.ident = pErgebnis.getInt("ntb.ident");
            pData.description = pErgebnis.getString("ntb.description");
            pData.db_nav = pErgebnis.getString("ntb.db_nav");
            pData.db_bm = pErgebnis.getString("ntb.db_bm");
            pData.updated = pErgebnis.getTimestamp("ntb.updated");
            pData.imp = pErgebnis.getBoolean("ntb.import");
        } catch (Exception e) {
            CaBug.drucke("DbNavToBM.decodeErgebnis 001");
            System.err.println(" " + e.getMessage());
        }

        return pData;
    }

    /*********************
     * Fuellen Prepared Statement mit allen Feldern, beginnend bei
     * offset.***************** Kann sowohl für Insert, als auch für update
     * verwendet werden.
     * 
     * offset= Startposition des ersten Feldes (also z.B. 1)
     */
    private int anzfelder = 5; /* Anpassen auf Anzahl der Felder pro Datensatz */

    void fuellePreparedStatementKomplett(PreparedStatement pPStm, int pOffset, EclNavToBM pData) {

        int startOffset = pOffset; /* Nur erforderlich zum Überprüfen von Programmierfehlern - Feldanzahl */

        try {
            pPStm.setInt(pOffset, pData.ident);
            pOffset++;
            pPStm.setString(pOffset, pData.description);
            pOffset++;
            pPStm.setString(pOffset, pData.db_nav);
            pOffset++;
            pPStm.setString(pOffset, pData.db_bm);
            pOffset++;
            pPStm.setBoolean(pOffset, pData.imp);
            pOffset++;

            if (pOffset - startOffset != anzfelder) {
                /* Nur wg. Überprüfung auf Programmierfehler - alle Felder berücksichtigt? */
                CaBug.drucke("DbNavToBM.fuellePreparedStatementKomplett 002");
            }

        } catch (SQLException e) {
            CaBug.drucke("DbNavToBM.fuellePreparedStatementKomplett 001");
            e.printStackTrace();
        }

    }

    /**
     * Insert
     * 
     * Feld drucklaufNr wird hier belegt
     * 
     * Returnwert: =1 => Insert erfolgreich ansonsten: Fehler
     */
    public int insert(EclNavToBM pData) {

        int erg = 0;

        pData.ident = readHighestIdent();

//		@formatter:off
		try {
			/* Felder Neuanlage füllen */
			String lSql = "INSERT INTO " + dbBundle.getSchemaAllgemein() 
					+ "tbl_navtobm ("
					+ "ident, description, db_nav, db_bm, import) " 
					+ "VALUES (" 
					+ "?, ?, ?, ?, ? " 
					+ ")";
//		@formatter:on
            PreparedStatement lPStm = verbindung.prepareStatement(lSql, ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
            fuellePreparedStatementKomplett(lPStm, 1, pData);
            erg = lPStm.executeUpdate();
            lPStm.close();
        } catch (Exception e2) {
            CaBug.drucke("DbNavToBM.insert 001");
            e2.printStackTrace();
            System.err.println(" " + e2.getMessage());
        }

        if (erg == 0) {/* Fehler beim Einfügen - d.h. z.B. primaryKey bereits vorhanden */
            return (-1);
        }
        return (1);
    }

    public int update(EclNavToBM pData) {

        try {
            String lSql = "UPDATE " + dbBundle.getSchemaAllgemein() + "tbl_navtobm SET "
                    + "description=?, db_nav=?, db_bm=?, import=? WHERE ident=?";

            PreparedStatement lPStm = verbindung.prepareStatement(lSql);
            lPStm.setString(1, pData.description);
            lPStm.setString(2, pData.db_nav);
            lPStm.setString(3, pData.db_bm);
            lPStm.setBoolean(4, pData.imp);
            lPStm.setInt(5, pData.ident);

            int ergebnis = lPStm.executeUpdate();
            lPStm.close();
            if (ergebnis == 0) {
                return (CaFehler.pfXyWurdeVonAnderemBenutzerVeraendert);
            }
        } catch (Exception e1) {
            CaBug.drucke("DbNavToBM.update 001");
            System.err.println(" " + e1.getMessage());
            return (CaFehler.pfdXyBereitsVorhanden);
        }
        return (1);
    }

    /**
     * Lese alle Druckläufe-Konfigs
     */
    public List<EclNavToBM> readAll() {
        ergebnisList = new ArrayList<>();
        PreparedStatement lPStm = null;

        try {
            String lSql = "SELECT * from " + dbBundle.getSchemaAllgemein() + "tbl_navtobm ntb ORDER BY ntb.ident";

            lPStm = verbindung.prepareStatement(lSql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);

            ResultSet lErgebnis = lPStm.executeQuery();

            while (lErgebnis.next())
                ergebnisList.add(decodeErgebnis(lErgebnis));

            lErgebnis.close();
            lPStm.close();

        } catch (Exception e) {
            CaBug.drucke("DbNavToBM.readAll_technisch 003");
            System.err.println(" " + e.getMessage());
            e.printStackTrace();
            return null;
        }
        return ergebnisList;
    }

    private int readHighestIdent() {

        int ident = 0;
        PreparedStatement lPStm = null;

        try {

            String sql = "SELECT MAX(ident) FROM " + dbBundle.getSchemaAllgemein() + "tbl_navtobm";
            lPStm = verbindung.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            ResultSet lErgebnis = lPStm.executeQuery();

            if (lErgebnis.next())
                ident = lErgebnis.getInt(1);

            lErgebnis.close();
            lPStm.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ++ident;
    }

    //	null = Fehler oder keine Datensätze
    public Timestamp readUpdateTime() {

        Timestamp time = null;
        PreparedStatement lPStm = null;
        try {
            String lSql = "SELECT DISTINCT(updated) from " + dbBundle.getSchemaAllgemein() + "tbl_navtobm";
            lPStm = verbindung.prepareStatement(lSql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);

            ResultSet lErgebnis = lPStm.executeQuery();
            if (lErgebnis.next()) {
                time = lErgebnis.getTimestamp(1);
            }

            lErgebnis.close();
            lPStm.close();

        } catch (

        Exception e) {
            CaBug.drucke("DbNavToBM.read 003");
            System.err.println(" " + e.getMessage());
            return null;
        }
        return time;
    }

    public int delete(EclNavToBM pData) {

        try {

            String sql = "DELETE FROM " + dbBundle.getSchemaAllgemein() + "tbl_navtobm WHERE ident=?";

            PreparedStatement pstm1 = verbindung.prepareStatement(sql);
            pstm1.setInt(1, pData.ident);

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
