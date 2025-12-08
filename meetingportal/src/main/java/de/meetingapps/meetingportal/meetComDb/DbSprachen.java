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
import de.meetingapps.meetingportal.meetComEntities.EclSprache;

public class DbSprachen {

    private Connection verbindung = null;
    //	private DbBasis dbBasis=null;
    private DbBundle dbBundle = null;

    /*Array für alle Sprachen*/
    public EclSprache sprachenarray[];
    public int AnzSprachenInArray;

    /*Array für read-Kommando*/
    public EclSprache sprachenreadarray[];
    public int AnzSprachenInReadArray;

    /*************************Initialisierung***************************/
    /* Verbindung in lokale Daten eintragen*/
    public DbSprachen(DbBundle pDbBundle) {
        /* Verbindung in lokale Daten eintragen*/
        if (pDbBundle == null) {
            CaBug.drucke("DbAppTexte.init 001 - dbBundle nicht initialisiert");
            return;
        }
        if (pDbBundle.dbBasis == null) {
            CaBug.drucke("DbAppTexte.init 002 - dbBasis nicht initialisiert");
            return;
        }

        //		dbBasis=pDbBundle.dbBasis;
        verbindung = pDbBundle.dbBasis.verbindung;
        dbBundle = pDbBundle;
    }

    public int createTable() {
        int rc = 0;
        DbLowLevel lDbLowLevel = new DbLowLevel(dbBundle);
        rc = lDbLowLevel.createTable(
                "CREATE TABLE " + dbBundle.getSchemaAllgemein() + "tbl_sprachen ( " + "`sprachennr` int(11) NOT NULL, "
                        + "`iststandard` int(11) DEFAULT NULL, " + "`sprache` varchar(20) DEFAULT NULL, "
                        + "PRIMARY KEY (`sprachennr`), " + "UNIQUE KEY `sprachennr` (`sprachennr`) " + ") ");
        return rc;
    }

    /*****************Sprachen in Array einlesen*******************************/
    /*Rückgabewert: AnzSprachenInArray*/
    /*In Public sprachenarray[] stehen die Sprachen-Elemente drin*/
    public int FuelleSprachenArray() {

        try {

            Statement stm = verbindung.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            String sql = "SELECT * from " + dbBundle.getSchemaAllgemein() + "tbl_sprachen";
            ResultSet ergebnis = stm.executeQuery(sql);
            ergebnis.last();
            AnzSprachenInArray = ergebnis.getRow();
            System.out.println("Sprachenanzahl " + AnzSprachenInArray);
            ergebnis.beforeFirst();

            sprachenarray = new EclSprache[AnzSprachenInArray];
            System.out.println("Inhalt von Tabelle Sprachen interpretieren");

            int i = 0;
            while (ergebnis.next() == true) {
                EclSprache sprache = new EclSprache();
                sprache.sprachennr = ergebnis.getInt("sprachennr");
                sprache.sprache = ergebnis.getString("sprache");
                sprache.iststandard = ergebnis.getInt("iststandard");
                sprachenarray[i] = sprache;
                i++;

            }
            ergebnis.close();
            stm.close();

        } catch (Exception e) {
            System.err.println(" " + e.getMessage());
        }
        return (AnzSprachenInArray);

    }

    /*****************Read (Sprachentext) Sprache in ReadArray einlesen*******************************/
    /*Rückgabewert: AnzSprachenInReadArray*/
    /*In Public sprachenreadarray[] stehen die Sprachen-Elemente drin*/
    public int ReadSprache_Sprachentext(String sprachentext) {

        System.out.println("TestSprachen Read");

        try {

            Statement stm = verbindung.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            String sql = "SELECT * from " + dbBundle.getSchemaAllgemein() + "tbl_sprachen where sprache='"
                    + sprachentext + "'";
            ResultSet ergebnis = stm.executeQuery(sql);
            ergebnis.last();
            AnzSprachenInReadArray = ergebnis.getRow();
            System.out.println("Sprachenanzahl " + AnzSprachenInReadArray);
            ergebnis.beforeFirst();

            sprachenreadarray = new EclSprache[AnzSprachenInReadArray];
            System.out.println("Inhalt von Tabelle Sprachen interpretieren");

            int i = 0;
            while (ergebnis.next() == true) {
                EclSprache sprache = new EclSprache();
                sprache.sprachennr = ergebnis.getInt("sprachennr");
                sprache.sprache = ergebnis.getString("sprache");
                sprache.iststandard = ergebnis.getInt("iststandard");
                sprachenreadarray[i] = sprache;
                i++;

            }
            ergebnis.close();
            stm.close();

        } catch (Exception e) {
            System.err.println(" " + e.getMessage());
        }
        return (AnzSprachenInReadArray);

    }

}
