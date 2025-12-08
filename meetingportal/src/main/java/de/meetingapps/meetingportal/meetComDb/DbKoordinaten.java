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
import de.meetingapps.meetingportal.meetComEntities.EclKoordinaten;

public class DbKoordinaten {

    private Connection verbindung = null;
    private DbBundle dbBundle = null;

    public DbKoordinaten(DbBundle pDbBundle) {
        /* Verbindung in lokale Daten eintragen */
        if (pDbBundle == null) {
            CaBug.drucke("DbKoordinaten.init 001 - dbBundle nicht initialisiert");
            return;
        }
        if (pDbBundle.dbBasis == null) {
            CaBug.drucke("DbKoordinaten.init 002 - dbBasis nicht initialisiert");
            return;
        }

        verbindung = pDbBundle.dbBasis.verbindung;
        dbBundle = pDbBundle;
    }

    /******* Checken, ob table überhaupt vorhanden ist ***************************/
    public boolean checkTableVorhanden() {
        DbLowLevel lDbLowLevel = new DbLowLevel(dbBundle);

        return lDbLowLevel.checkTableVorhandenNew(dbBundle.getSchemaAllgemein().replace(".", ""), "tbl_koordinaten");
    }

    /************ Neuanlegen Table ******************************/
    public int createTable() {
        int rc = 0;
        DbLowLevel lDbLowLevel = new DbLowLevel(dbBundle);
//		@formatter:off
		rc = lDbLowLevel.createTable(
				"CREATE TABLE IF NOT EXISTS" + dbBundle.getSchemaAllgemein() + "tbl_koordinaten ( "
				+ "`plz` varchar(5) NOT NULL, " 
				+ "`ort` varchar(60) NOT NULL, "
				+ "`bundesland` varchar(45) NOT NULL, " 
				+ "`breite` double NOT NULL, "
				+ "`laenge` double NOT NULL, " 
				+ "PRIMARY KEY (`plz`, `ort`, `bundesland`) " 
				+ ")");
//		@formatter:on
        System.out.println("DbKoordinaten create table rc=" + rc);
        return rc;
    }

    /**********
     * dekodiert die aktuelle Position aus ergebnis in EclAktienregisterEintrag und
     * gibt dieses zurück
     ******/
    EclKoordinaten decodeErgebnis(ResultSet pErgebnis) {

        EclKoordinaten koord = new EclKoordinaten();

        try {
            koord.plz = pErgebnis.getString("ko.plz");
            koord.ort = pErgebnis.getString("ko.ort");
            koord.bundesland = pErgebnis.getString("ko.bundesland");
            koord.breite = pErgebnis.getDouble("ko.breite");
            koord.laenge = pErgebnis.getDouble("ko.laenge");
        } catch (SQLException e) {
            CaBug.drucke("DbKoordinaten.decodeErgebnis 001");
            System.err.println(" " + e.getMessage());
        }

        return koord;
    }

    /*********************
     * Fuellen Prepared Statement mit allen Feldern, beginnend bei
     * offset.***************** Kann sowohl für Insert, als auch für update
     * verwendet werden.
     * 
     * offset= Startposition des ersten Feldes (also z.B. 1)
     */
    private int anzfelder = 5; /* Anpassen auf Anzahl der Felder pro Datensatz */

    void fuellePreparedStatementKomplett(PreparedStatement pPStm, int pOffset, EclKoordinaten koord) {

        int startOffset = pOffset; /* Nur erforderlich zum Überprüfen von Programmierfehlern - Feldanzahl */

        try {
            pPStm.setString(pOffset, koord.plz);
            pOffset++;
            pPStm.setString(pOffset, koord.ort);
            pOffset++;
            pPStm.setString(pOffset, koord.bundesland);
            pOffset++;
            pPStm.setDouble(pOffset, koord.breite);
            pOffset++;
            pPStm.setDouble(pOffset, koord.laenge);
            pOffset++;

            if (pOffset - startOffset != anzfelder) {
                /* Nur wg. Überprüfung auf Programmierfehler - alle Felder berücksichtigt? */
                CaBug.drucke("DbKoordinaten.fuellePreparedStatementKomplett 002");
            }

        } catch (SQLException e) {
            CaBug.drucke("DbKoordinaten.fuellePreparedStatementKomplett 001");
            e.printStackTrace();
        }

    }

    /** Koordinaten mit Ort ermitteln */
    public EclKoordinaten readByOrt(String ort) {

        if (ort == null)
            return null;

        PreparedStatement lPStm = null;
        EclKoordinaten eclKoordinaten = null;

        try {
            String lSql = "SELECT * from " + dbBundle.getSchemaAllgemein()
                    + "tbl_koordinaten ko WHERE ko.ort LIKE ? LIMIT 1;";
            lPStm = verbindung.prepareStatement(lSql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            lPStm.setString(1, ort);

            ResultSet lErgebnis = lPStm.executeQuery();

            if (lErgebnis.next())
                eclKoordinaten = this.decodeErgebnis(lErgebnis);

            lErgebnis.close();
            lPStm.close();

        } catch (Exception e) {
            CaBug.drucke("DbKoordinaten.read 003");
            System.err.println(" " + e.getMessage());
            return null;
        }
        return eclKoordinaten;
    }

    /** Koordinaten mit PLZ ermitteln */
    public EclKoordinaten readByPlz(String plz) {

        if (plz == null)
            return null;

        PreparedStatement lPStm = null;
        EclKoordinaten eclKoordinaten = null;

        try {
            String lSql = "SELECT * from " + dbBundle.getSchemaAllgemein() + "tbl_koordinaten ko WHERE ko.plz=?;";
            lPStm = verbindung.prepareStatement(lSql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            lPStm.setString(1, plz);

            ResultSet lErgebnis = lPStm.executeQuery();

            if (lErgebnis.next())
                eclKoordinaten = this.decodeErgebnis(lErgebnis);

            lErgebnis.close();
            lPStm.close();

        } catch (Exception e) {
            CaBug.drucke("DbKoordinaten.read 003");
            System.err.println(" " + e.getMessage());
            return null;
        }
        return eclKoordinaten;
    }

}
