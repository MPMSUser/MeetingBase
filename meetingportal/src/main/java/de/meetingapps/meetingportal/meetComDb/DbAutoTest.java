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
import de.meetingapps.meetingportal.meetComEntities.EclAutoTest;

public class DbAutoTest {

    private int logDrucken=10;
    
    private Connection verbindung = null;
    private DbBundle dbBundle = null;

    public EclAutoTest ergebnisArray[] = null;

    /**Hinweise zur Nutzung:
     * In Mandanten-Datenbank: tbl_autotest: Bonus-Kartennummern übertragen (ident ist Autoincrement):
     * 
     * use db_mc254j2022ap;
     * insert into tbl_autotest (mandant, scanstring)
     * SELECT mandant, zusatzfeld3 FROM tbl_meldungen;
     * 
     * 
     * 
     * In readNext: maximale Anzahl Mitglieder, die getestet werden sollen (maximalst Anzahl der Mitglieder) (danach Neustart)
     * In Mandanten-Datenbank: tbl_nummernkreis: Satz mit Ident 629 auf 0 zurücksetzen, damit das von vorne losgeht
     * 
     * Wenn von einem Client ein Mitglied gebucht wird, das bereits als Zugang gebucht wurde, dann wird das automatisch als Abgang gebucht (aber nicht umgekehrt)
     * D.h: maximal alle Zugang und wieder Abgang - dann ist schluß ...
     * 
     * Beenden des Testlaufs nur durch "Crashen des Servers"
     */
    
    
    
    /*************************Initialisierung***************************/
    public DbAutoTest(DbBundle pDbBundle) {
        /* Verbindung in lokale Daten eintragen*/
        if (pDbBundle == null) {
            CaBug.drucke("AutoTest.init 001 - dbBundle nicht initialisiert");
            return;
        }
        if (pDbBundle.dbBasis == null) {
            CaBug.drucke("AutoTest.init 002 - dbBasis nicht initialisiert");
            return;
        }

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
    public EclAutoTest ergebnisPosition(int pN) {
        if (ergebnisArray == null) {
            CaBug.drucke("AutoTest.ergebnisPosition 001");
            return null;
        }
        if (pN < 0) {
            CaBug.drucke("AutoTest.ergebnisPosition 002");
            return null;
        }
        if (pN >= ergebnisArray.length) {
            CaBug.drucke("AutoTest.ergebnisPosition 003");
            return null;
        }
        return ergebnisArray[pN];
    }

    /**************************deleteAll Löschen aller Datensätze  eines Mandanten****************/
    public int deleteAll() {
        return dbBundle.dbLowLevel
                .deleteMandant("DELETE FROM " + dbBundle.getSchemaMandant() + "tbl_autotest where mandant=?;");
    }

    /*******Checken, ob table überhaupt vorhanden ist***************************/
    public boolean checkTableVorhanden() {
        DbLowLevel lDbLowLevel = new DbLowLevel(dbBundle);
        return lDbLowLevel.checkTableVorhanden("SELECT * from tbl_autotest WHERE mandant=0; ");
    }

    /************Neuanlegen Table******************************/
    public int createTable() {
        int rc = 0;
        DbLowLevel lDbLowLevel = new DbLowLevel(dbBundle);
        rc = lDbLowLevel.createTable("CREATE TABLE " + dbBundle.getSchemaMandant() + "tbl_autotest ( "
                + "`mandant` int(11) NOT NULL, " + "`ident`  bigint(20)  NOT NULL AUTO_INCREMENT, "
                + "`scanString` varchar(400) DEFAULT NULL, " + "PRIMARY KEY (`ident`) " + ")  ");
        CaBug.druckeLog("AutoTest create table rc=" + rc, logDrucken, 10);
        return rc;
    }

    public int updateMandant() {
        return dbBundle.dbLowLevel.rawUpdateMandant(dbBundle.getSchemaMandant() + "tbl_autotest");
    }


    /********** dekodiert die aktuelle Position aus ergebnis in EclAktienregisterEintrag und gibt dieses zurück******/
    EclAutoTest decodeErgebnis(ResultSet pErgebnis) {

        EclAutoTest lAutoTest = new EclAutoTest();

        try {
            lAutoTest.mandant = pErgebnis.getInt("aut.mandant");
            lAutoTest.ident = pErgebnis.getInt("aut.ident");
            lAutoTest.scanString = pErgebnis.getString("aut.scanString");

        } catch (Exception e) {
            CaBug.drucke("AutoTest.decodeErgebnis 001");
            System.err.println(" " + e.getMessage());
        }

        return lAutoTest;
    }

    /********************* Fuellen Prepared Statement mit allen Feldern, beginnend bei offset.*****************
     * Kann sowohl für Insert, als auch für update verwendet werden.
     * 
     * offset= Startposition des ersten Feldes (also z.B. 1)
     */
//    private int anzfelder = 2; /*Anpassen auf Anzahl der Felder pro Datensatz  ident nicht mit enthalten!*/

    void fuellePreparedStatementKomplett(PreparedStatement pPStm, int pOffset, EclAutoTest pAutoTest) {

        //		int startOffset=pOffset; /*Nur erforderlich zum Überprüfen von Programmierfehlern - Feldanzahl*/

        try {
            pPStm.setInt(pOffset, pAutoTest.mandant);
            pOffset++;
            //			pOffset++; //Ident nicht setzen - Autoincrement

            pPStm.setString(pOffset, pAutoTest.scanString);
            pOffset++;

            //			if (pOffset-startOffset!=anzfelder){ 
            //				/*Nur wg. Überprüfung auf Programmierfehler - alle Felder berücksichtigt?*/
            //				CaBug.drucke("AutoTest.fuellePreparedStatementKomplett 002");
            //			}

        } catch (SQLException e) {
            CaBug.drucke("AutoTest.fuellePreparedStatementKomplett 001");
            e.printStackTrace();
        }

    }

    /**Einlesen eines bestimmten Laufs mit  ident*/
    public int readNext() {
        int anzInArray = 0;

        int pIdent = dbBundle.dbBasis.getInterneIdentAutoTest();
        if (pIdent > 20000) {
            dbBundle.dbBasis.resetInterneIdentAutoTest();
            pIdent = dbBundle.dbBasis.getInterneIdentAutoTest();
        }

        PreparedStatement lPStm = null;

        try {
            String lSql = "SELECT * from " + dbBundle.getSchemaMandant() + "tbl_autotest aut WHERE ident=?;";
            lPStm = verbindung.prepareStatement(lSql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            lPStm.setLong(1, pIdent);

            ResultSet lErgebnis = lPStm.executeQuery();
            lErgebnis.last();
            anzInArray = lErgebnis.getRow();
            lErgebnis.beforeFirst();

            ergebnisArray = new EclAutoTest[anzInArray];

            int i = 0;
            while (lErgebnis.next() == true) {
                ergebnisArray[i] = this.decodeErgebnis(lErgebnis);
                i++;
            }

            lErgebnis.close();
            lPStm.close();

        } catch (Exception e) {
            CaBug.drucke("AutoTest.readZuident 003");
            System.err.println(" " + e.getMessage());
            return (-1);
        }
        return (anzInArray);
    }

}
