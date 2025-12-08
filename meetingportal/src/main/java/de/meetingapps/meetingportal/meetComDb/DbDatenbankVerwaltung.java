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

public class DbDatenbankVerwaltung {

    private Connection verbindung = null;
    //	private DbBasis dbBasis=null;
    private DbBundle dbBundle = null;

    /*************************Initialisierung***************************/
    public DbDatenbankVerwaltung(DbBundle pDbBundle) {
        /* Verbindung in lokale Daten eintragen*/
        if (pDbBundle == null) {
            CaBug.drucke("DbDatenbankVerwaltung.init 001 - dbBundle nicht initialisiert");
            return;
        }
        if (pDbBundle.dbBasis == null) {
            CaBug.drucke("DbDatenbankVerwaltung.init 002 - dbBasis nicht initialisiert");
            return;
        }

        //		dbBasis=pDbBundle.dbBasis;
        verbindung = pDbBundle.dbBasis.verbindung;
        dbBundle = pDbBundle;
    }

    /**********************Verwaltung von tableVersion**********************************/
    /**Returnwert:
     * -1 => Wert konnte nicht geholt werden
     * @return
     */

    public int createTableVersion() {
        int rc = 0;
        DbLowLevel lDbLowLevel = new DbLowLevel(dbBundle);
        rc = lDbLowLevel.createTable(
                "CREATE TABLE " + dbBundle.getSchemaAllgemein() + "tbl_tableversion ( " + "`mandant` int(11) NOT NULL, "
                        + "`ident` int(11) NOT NULL, " + "`db_version` bigint(20) DEFAULT NULL, "
                        + "`wert` char(20) DEFAULT NULL, " + "PRIMARY KEY (`mandant`,`ident`) " + ")");
        if (rc < 0) {
            return rc;
        }
        rc = lDbLowLevel.rawOperation("INSERT INTO " + dbBundle.getSchemaAllgemein() + "tbl_tableversion "
                + "SET mandant=0, ident=1, db_version=0, wert='5'");

        return rc;
    }

    public int holeTableVersion() {
        int anzInArray = 0;
        PreparedStatement lPStm = null;
        int lErgebnisWert = -1;

        try {
            String lSql = "SELECT * from " + dbBundle.getSchemaAllgemein() + "tbl_tableVersion WHERE ident=1 "
                    + "ORDER BY ident;";
            lPStm = verbindung.prepareStatement(lSql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);

            ResultSet lErgebnis = lPStm.executeQuery();
            lErgebnis.last();
            anzInArray = lErgebnis.getRow();
            lErgebnis.beforeFirst();

            if (anzInArray == 0) {
                return -1;
            }

            while (lErgebnis.next() == true) {
                lErgebnisWert = Integer.parseInt(lErgebnis.getString("wert"));
            }
            lErgebnis.close();
            lPStm.close();

        } catch (SQLException e) {
            /*Wird in diesem Fall erwartet - nämlich wenn Datenbank so alt ist, dass noch kein Table vorhanden ist.*/
            CaBug.drucke("DbDatenbankVerwaltung.holeTableVersion 001");
            System.err.println(" " + e.getMessage());
            return (-1);
        } catch (Exception e) {
            CaBug.drucke("DbDatenbankVerwaltung.holeTableVersion 003");
            System.err.println(" " + e.getMessage());
            return (-1);
        }
        return (lErgebnisWert);
    }

    public int updateTableVersion(int pTableVersionNeu) {

        try {
            String lSql = "UPDATE " + dbBundle.getSchemaAllgemein() + "tbl_tableVersion SET " + "wert=? " + "WHERE "
                    + "ident=1";

            PreparedStatement lPStm = verbindung.prepareStatement(lSql);
            lPStm.setString(1, Integer.toString(pTableVersionNeu));

            int ergebnis = lPStm.executeUpdate();
            lPStm.close();
            if (ergebnis == 0) {
                return (CaFehler.pfXyWurdeVonAnderemBenutzerVeraendert);
            }
        } catch (Exception e1) {
            CaBug.drucke("DbDatenbankVerwaltung.updateTableVersion 001");
            System.err.println(" " + e1.getMessage());
            return (-1);
        }

        return (1);
    }

    /*************************Schema***********************************************/
    public int createMandantenSchema(String pSchema) {
        String schemaName = "CREATE DATABASE " + pSchema;
        DbLowLevel dbLowLevel = new DbLowLevel(dbBundle);
        return dbLowLevel.createSchema(schemaName);
    }

}
