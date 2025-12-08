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
import java.sql.ResultSet;
import java.sql.Statement;

import de.meetingapps.meetingportal.meetComAllg.CaBug;
import de.meetingapps.meetingportal.meetComEntities.EclVertretungsArt;

public class DbVertretungsArten {

    private Connection verbindung = null;
    //	private DbBasis dbBasis=null;
    private DbBundle dbBundle = null;

    /*Array für alle Sprachen*/
    public EclVertretungsArt vertretungsartenarray[];
    public int AnzVertretungsArtenInArray;

    /*Array für read-Kommando*/
    public EclVertretungsArt vertretungsartenreadarray[];
    public int AnzVertretungsArtenInReadArray;

    /*************************Initialisierung***************************/
    /* Verbindung in lokale Daten eintragen*/
    public DbVertretungsArten(DbBundle pDbBundle) {
        /* Verbindung in lokale Daten eintragen*/
        if (pDbBundle == null) {
            CaBug.drucke("DbVertretungsArten.init 001 - dbBundle nicht initialisiert");
            return;
        }
        if (pDbBundle.dbBasis == null) {
            CaBug.drucke("DbVertretungsArten.init 002 - dbBasis nicht initialisiert");
            return;
        }

        //		dbBasis=pDbBundle.dbBasis;
        verbindung = pDbBundle.dbBasis.verbindung;
        dbBundle = pDbBundle;
    }

    public int createTable() {
        int rc = 0;
        DbLowLevel lDbLowLevel = new DbLowLevel(dbBundle);
        rc = lDbLowLevel.createTable("CREATE TABLE " + dbBundle.getSchemaAllgemein() + "tbl_vertretungsarten ( "
                + "`vertretartNr` int(11) NOT NULL, " + "`vertretartKurz` varchar(7) DEFAULT NULL, "
                + "`vertretartLang` char(40) DEFAULT NULL, " + "`vertretartImTeilnVerz` int(11) DEFAULT NULL, "
                + "PRIMARY KEY (`vertretartNr`), " + "UNIQUE KEY `vertretartNr` (`vertretartNr`) " + ") ");
        return rc;
    }

    /*****************Vertretungsarten in Array einlesen*******************************/
    /*Rückgabewert: AnzVertretungsArtenInArray*/
    /*In Public vertretungsartenarray[] stehen die Vertretungsart-Elemente drin*/
    public int FuelleVertretungsArtenArray() {

        System.out.println("TestVertretungsarten");

        try {

            Statement stm = verbindung.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            String sql = "SELECT * from " + dbBundle.getSchemaAllgemein() + "tbl_vertretungsarten";
            ResultSet ergebnis = stm.executeQuery(sql);
            ergebnis.last();
            AnzVertretungsArtenInArray = ergebnis.getRow();
            System.out.println("Vertretungsartenanzahl " + AnzVertretungsArtenInArray);
            ergebnis.beforeFirst();

            vertretungsartenarray = new EclVertretungsArt[AnzVertretungsArtenInArray];
            System.out.println("Inhalt von Tabelle VertretungsArten interpretieren");

            int i = 0;
            while (ergebnis.next() == true) {
                EclVertretungsArt vertretungsart = new EclVertretungsArt();
                vertretungsart.vertretartNr = ergebnis.getInt("vertretartNr");
                vertretungsart.vertretartKurz = ergebnis.getString("vertretartKurz");
                vertretungsart.vertretartLang = ergebnis.getString("vertretartLang");
                vertretungsart.vertretartImTeilnVerz = ergebnis.getInt("vertretartImTeilnVerz");
                vertretungsartenarray[i] = vertretungsart;
                i++;

            }
            ergebnis.close();
            stm.close();

        } catch (Exception e) {
            System.err.println(" " + e.getMessage());
        }
        return (AnzVertretungsArtenInArray);

    }

}
