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
import de.meetingapps.meetingportal.meetComEntities.EclPersonenprognose;

public class DbPersonenprognose {

    private int logDrucken=10;
    
    private Connection verbindung = null;
    private DbBundle dbBundle = null;

    public List<EclPersonenprognose> ergebnisList = null;

    /************************* Initialisierung ***************************/
    public DbPersonenprognose(DbBundle pDbBundle) {
        /* Verbindung in lokale Daten eintragen */
        if (pDbBundle == null) {
            CaBug.drucke("DbPersonenprognose.init 001 - dbBundle nicht initialisiert");
            return;
        }
        if (pDbBundle.dbBasis == null) {
            CaBug.drucke("DbPersonenprognose.init 002 - dbBasis nicht initialisiert");
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
    public EclPersonenprognose ergebnisPosition(int pN) {
        if (ergebnisList == null) {
            CaBug.drucke("DbPersonenprognose.ergebnisPosition 001");
            return null;
        }
        if (pN < 0) {
            CaBug.drucke("DbPersonenprognose.ergebnisPosition 002");
            return null;
        }
        if (pN >= ergebnisList.size()) {
            CaBug.drucke("DbPersonenprognose.ergebnisPosition 003");
            return null;
        }
        return ergebnisList.get(pN);
    }

    /**************************
     * deleteAll Löschen aller Datensätze eines Mandanten
     ****************/
    public int deleteAll() {
        return dbBundle.dbLowLevel
                .deleteAlle("DELETE FROM " + dbBundle.getSchemaMandant() + "tbl_personenprognose where ident !=0;");
    }

    /******* Checken, ob table überhaupt vorhanden ist ***************************/
    public boolean checkTableVorhanden() {
        DbLowLevel lDbLowLevel = new DbLowLevel(dbBundle);
        return lDbLowLevel.checkTableVorhanden(
                "SELECT * from " + dbBundle.getSchemaMandant().toLowerCase() + "tbl_personenprognose LIMIT 1");
    }

    public boolean checkTableVorhandenNew(String schema) {
        DbLowLevel lDbLowLevel = new DbLowLevel(dbBundle);
        return lDbLowLevel.checkTableVorhandenNew(schema, "tbl_personenprognose");
    }

    /************ Neuanlegen Table ******************************/
    public int createTable() {
        int rc = 0;
        DbLowLevel lDbLowLevel = new DbLowLevel(dbBundle);
//		@formatter:off
		rc = lDbLowLevel.createTable(
				"CREATE TABLE IF NOT EXISTS " + dbBundle.getSchemaMandant() + "tbl_personenprognose ( "
				+ "`ident` int(11) NOT NULL, " 
				+ "`description` varchar(60) NOT NULL, " 
				+ "`distance` int(11) NOT NULL, "
				+ "`percent` double NOT NULL, "
				+ "`max` int(11) NOT NULL, "
				+ "`forecast` int(11) NOT NULL, "
				+ "`realPercent` double NOT NULL, "
				+ "`realCount` int NOT NULL, "
				+ "`updated` TIMESTAMP NOT NULL, " 
				+ "PRIMARY KEY (`ident`) " 
				+ ")");
//		@formatter:on
        CaBug.druckeLog("DbPersonenprognose create table rc=" + rc, logDrucken, 10);
        return rc;
    }

    /**********
     * dekodiert die aktuelle Position aus ergebnis in EclAktienregisterEintrag und
     * gibt dieses zurück
     ******/
    EclPersonenprognose decodeErgebnis(ResultSet pErgebnis) {

        EclPersonenprognose lPersonenprognose = new EclPersonenprognose();

        try {
            lPersonenprognose.ident = pErgebnis.getInt("pp.ident");
            lPersonenprognose.description = pErgebnis.getString("pp.description");
            lPersonenprognose.distance = pErgebnis.getInt("pp.distance");
            lPersonenprognose.percent = pErgebnis.getDouble("pp.percent");
            lPersonenprognose.max = pErgebnis.getInt("pp.max");
            lPersonenprognose.forecast = pErgebnis.getInt("pp.forecast");
            lPersonenprognose.realPercent = pErgebnis.getInt("pp.realPercent");
            lPersonenprognose.realCount = pErgebnis.getInt("pp.realCount");
            lPersonenprognose.updated = pErgebnis.getTimestamp("pp.updated");
        } catch (Exception e) {
            CaBug.drucke("DbPersonenprognose.decodeErgebnis 001");
            System.err.println(" " + e.getMessage());
        }

        return lPersonenprognose;
    }

    /*********************
     * Fuellen Prepared Statement mit allen Feldern, beginnend bei
     * offset.***************** Kann sowohl für Insert, als auch für update
     * verwendet werden.
     * 
     * offset= Startposition des ersten Feldes (also z.B. 1)
     */
    private int anzfelder = 9; /* Anpassen auf Anzahl der Felder pro Datensatz */

    void fuellePreparedStatementKomplett(PreparedStatement pPStm, int pOffset, EclPersonenprognose pData) {

        int startOffset = pOffset; /* Nur erforderlich zum Überprüfen von Programmierfehlern - Feldanzahl */

        try {
            pPStm.setInt(pOffset, pData.ident);
            pOffset++;
            pPStm.setString(pOffset, pData.description);
            pOffset++;
            pPStm.setInt(pOffset, pData.distance);
            pOffset++;
            pPStm.setDouble(pOffset, pData.percent);
            pOffset++;
            pPStm.setInt(pOffset, pData.max);
            pOffset++;
            pPStm.setInt(pOffset, pData.forecast);
            pOffset++;
            pPStm.setDouble(pOffset, pData.realPercent);
            pOffset++;
            pPStm.setInt(pOffset, pData.realCount);
            pOffset++;
            pPStm.setTimestamp(pOffset, pData.updated);
            pOffset++;

            if (pOffset - startOffset != anzfelder) {
                /* Nur wg. Überprüfung auf Programmierfehler - alle Felder berücksichtigt? */
                CaBug.drucke("DbPersonenprognose.fuellePreparedStatementKomplett 002");
            }

        } catch (SQLException e) {
            CaBug.drucke("DbPersonenprognose.fuellePreparedStatementKomplett 001");
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
    public int insert(EclPersonenprognose pData) {

        int erg = 0;

        pData.ident = readHighestIdent();

//		@formatter:off
		try {
			/* Felder Neuanlage füllen */
			String lSql = "INSERT INTO " + dbBundle.getSchemaMandant() 
					+ "tbl_personenprognose ("
					+ "ident, description, distance, percent, max, forecast, realPercent, realCount, updated)" 
					+ "VALUES (" 
					+ "?, ?, ?, ?, ?, ?, ?, ?, ? " 
					+ ")";
//		@formatter:on
            PreparedStatement lPStm = verbindung.prepareStatement(lSql, ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
            fuellePreparedStatementKomplett(lPStm, 1, pData);
            erg = lPStm.executeUpdate();
            lPStm.close();
        } catch (Exception e2) {
            CaBug.drucke("DbPersonenprognose.insert 001");
            e2.printStackTrace();
            System.err.println(" " + e2.getMessage());
        }

        if (erg == 0) {/* Fehler beim Einfügen - d.h. z.B. primaryKey bereits vorhanden */
            return (-1);
        }
        return (1);
    }

    public int update(EclPersonenprognose pData) {

        try {
            String lSql = "UPDATE " + dbBundle.getSchemaMandant() + "tbl_personenprognose SET "
                    + "description=?, distance=?, percent=?, max=?, forecast=?, realPercent=?, realCount=?, updated=? WHERE ident = ?";

            PreparedStatement lPStm = verbindung.prepareStatement(lSql);
            lPStm.setString(1, pData.description);
            lPStm.setInt(2, pData.distance);
            lPStm.setDouble(3, pData.percent);
            lPStm.setDouble(4, pData.max);
            lPStm.setInt(5, pData.forecast);
            lPStm.setDouble(6, pData.realPercent);
            lPStm.setDouble(7, pData.realCount);
            lPStm.setTimestamp(8, pData.updated);
            lPStm.setInt(9, pData.ident);

            int ergebnis = lPStm.executeUpdate();
            lPStm.close();
            if (ergebnis == 0) {
                return (CaFehler.pfXyWurdeVonAnderemBenutzerVeraendert);
            }
        } catch (Exception e1) {
            CaBug.drucke("DbPersonenprognose.update 001");
            System.err.println(" " + e1.getMessage());
            return (CaFehler.pfdXyBereitsVorhanden);
        }
        return (1);
    }

    /**
     * Lese alle Druckläufe-Konfigs
     */
    public List<EclPersonenprognose> readAll() {
        ergebnisList = new ArrayList<>();
        PreparedStatement lPStm = null;

        try {
            String lSql = "SELECT * from " + dbBundle.getSchemaMandant()
                    + "tbl_personenprognose pp ORDER BY pp.distance";

            lPStm = verbindung.prepareStatement(lSql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);

            ResultSet lErgebnis = lPStm.executeQuery();

            while (lErgebnis.next())
                ergebnisList.add(decodeErgebnis(lErgebnis));

            lErgebnis.close();
            lPStm.close();

        } catch (Exception e) {
            CaBug.drucke("DbPersonenprognose.readAll_technisch 003");
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

            String sql = "SELECT MAX(ident) FROM " + dbBundle.getSchemaMandant() + "tbl_personenprognose";
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

    //	-1 Fehler oder keine Datensätze
    public int readSumPrediction() {

        PreparedStatement lPStm = null;
        int ident = -1;

        try {
            String lSql = "SELECT SUM(forecast) from " + dbBundle.getSchemaMandant() + "tbl_personenprognose";
            lPStm = verbindung.prepareStatement(lSql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);

            ResultSet lErgebnis = lPStm.executeQuery();
            if (lErgebnis.next())
                ident = lErgebnis.getInt(1);

            lErgebnis.close();
            lPStm.close();

        } catch (Exception e) {
            CaBug.drucke("DbPersonenprognose.read 003");
            System.err.println(" " + e.getMessage());
            return (-1);
        }
        return ident;
    }
    
    //  -1 Fehler oder keine Datensätze
    public int readSumPresent() {

        PreparedStatement lPStm = null;
        int ident = -1;

        try {
            String lSql = "SELECT SUM(realCount) from " + dbBundle.getSchemaMandant() + "tbl_personenprognose";
            lPStm = verbindung.prepareStatement(lSql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);

            ResultSet lErgebnis = lPStm.executeQuery();
            if (lErgebnis.next())
                ident = lErgebnis.getInt(1);

            lErgebnis.close();
            lPStm.close();

        } catch (Exception e) {
            CaBug.drucke("DbPersonenprognose.read 003");
            System.err.println(" " + e.getMessage());
            return (-1);
        }
        return ident;
    }

    //	null = Fehler oder keine Datensätze
    public Timestamp readUpdateTime() {

        Timestamp time = null;
        PreparedStatement lPStm = null;
        try {
            String lSql = "SELECT DISTINCT(updated) from " + dbBundle.getSchemaMandant() + "tbl_personenprognose";
            lPStm = verbindung.prepareStatement(lSql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);

            ResultSet lErgebnis = lPStm.executeQuery();
            if (lErgebnis.next()) {
                time = lErgebnis.getTimestamp(1);
            }

            lErgebnis.close();
            lPStm.close();

        } catch (

        Exception e) {
            CaBug.drucke("DbPersonenprognose.read 003");
            System.err.println(" " + e.getMessage());
            return null;
        }
        return time;
    }

    public int delete(EclPersonenprognose pData) {

        try {

            String sql = "DELETE FROM " + dbBundle.getSchemaMandant() + "tbl_personenprognose WHERE ident=?";

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
