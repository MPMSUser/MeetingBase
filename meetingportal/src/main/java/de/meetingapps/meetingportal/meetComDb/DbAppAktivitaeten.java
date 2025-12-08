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
import de.meetingapps.meetingportal.meetComAllg.CaDatumZeit;
import de.meetingapps.meetingportal.meetComAllg.CaFehler;
import de.meetingapps.meetingportal.meetComEntities.EclAppAktivitaeten;

public class DbAppAktivitaeten {

    private Connection verbindung = null;
    private DbBasis dbBasis = null;
    private DbBundle dbBundle = null;

    private EclAppAktivitaeten ergebnisArray[] = null;

    /*************************Initialisierung***************************/
    public DbAppAktivitaeten(DbBundle pDbBundle) {
        /* Verbindung in lokale Daten eintragen*/
        if (pDbBundle == null) {
            CaBug.drucke("DbAppAktivitaeten.init 001 - dbBundle nicht initialisiert");
            return;
        }
        if (pDbBundle.dbBasis == null) {
            CaBug.drucke("DbAppAktivitaeten.init 002 - dbBasis nicht initialisiert");
            return;
        }

        dbBasis = pDbBundle.dbBasis;
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

    /**************Anzahl der Ergebnisse der read*-Methoden**************************/
    public EclAppAktivitaeten[] ergebnis() {
        return ergebnisArray;
    }

    /**********Liefert pN-tes Element des Ergebnisses der read*Methoden**************
     * pN geht von 0 bis anzErgebnis-1*/
    public EclAppAktivitaeten ergebnisPosition(int pN) {
        if (ergebnisArray == null) {
            CaBug.drucke("DbAppAktivitaeten.ergebnisPosition 001");
            return null;
        }
        if (pN < 0) {
            CaBug.drucke("DbAppAktivitaeten.ergebnisPosition 002");
            return null;
        }
        if (pN >= ergebnisArray.length) {
            CaBug.drucke("DbAppAktivitaeten.ergebnisPosition 003");
            return null;
        }
        return ergebnisArray[pN];
    }

    public int createTable() {
        int rc = 0;
        DbLowLevel lDbLowLevel = new DbLowLevel(dbBundle);
        rc = lDbLowLevel.createTable("CREATE TABLE " + dbBundle.getSchemaAllgemein() + "tbl_appaktivitaeten ( "
                + "`eindeutigerKey` varchar(20) NOT NULL, " + "`funktion` int(11) NOT NULL, "
                + "`mandant` int(11) NOT NULL, " + "`hvJahr` int(11) NOT NULL, " + "`hvNummer` varchar(20) NOT NULL, "
                + "`datenbereich` varchar(20) NOT NULL, " + "`paramter1` varchar(20) NOT NULL, "
                + "`paramter2` varchar(20) NOT NULL, " + "`paramter3` varchar(20) NOT NULL, "
                + "`paramter4` varchar(20) NOT NULL, " + "`paramter5` varchar(20) NOT NULL, "
                + "`datumUhrzeit` varchar(19) NOT NULL, " + "PRIMARY KEY (`eindeutigerKey`) " + ") ");
        return rc;
    }

    /**************************deleteAll Löschen aller Datensätze  eines Mandanten****************/
    public int deleteAll() {
        return dbBundle.dbLowLevel.deleteAlle(
                "DELETE FROM " + dbBundle.getSchemaAllgemein() + "tbl_appaktivitaeten;" /* where mandant=?*/);
    }

    //	/**************************setzt aktuelle Mandantennummer bei allen Datensätzen****************/
    //	public int updateMandant(){
    //		return dbBundle.dbLowLevel.rawUpdateMandant(dbBundle.getSchemaAllgemein()+"tbl_appaktivitaeten");
    //	}

    /********** dekodiert die aktuelle Position aus ergebnis  und gibt dieses zurück******/
    EclAppAktivitaeten decodeErgebnis(ResultSet pErgebnis) {

        EclAppAktivitaeten lEclReturn = new EclAppAktivitaeten();

        try {
            lEclReturn.eindeutigerKey = pErgebnis.getString("eindeutigerKey");

            lEclReturn.funktion = pErgebnis.getInt("funktion");

            lEclReturn.mandant = pErgebnis.getInt("mandant");
            lEclReturn.hvJahr = pErgebnis.getInt("hvJahr");
            lEclReturn.hvNummer = pErgebnis.getString("hvNummer");
            lEclReturn.datenbereich = pErgebnis.getString("datenbereich");

            lEclReturn.paramter1 = pErgebnis.getString("paramter1");
            lEclReturn.paramter2 = pErgebnis.getString("paramter2");
            lEclReturn.paramter3 = pErgebnis.getString("paramter3");
            lEclReturn.paramter4 = pErgebnis.getString("paramter4");
            lEclReturn.paramter5 = pErgebnis.getString("paramter5");

            lEclReturn.datumUhrzeit = pErgebnis.getString("datumUhrzeit");

        } catch (Exception e) {
            CaBug.drucke("DbAppAktivitaeten.decodeErgebnis 001");
            System.err.println(" " + e.getMessage());
        }

        return lEclReturn;
    }

    /********************* Fuellen Prepared Statement mit allen Feldern, beginnend bei offset.*****************
     * Kann sowohl für Insert, als auch für update verwendet werden.
     * 
     * offset= Startposition des ersten Feldes (also z.B. 1)
     */
    private int anzfelder = 12; /*Anpassen auf Anzahl der Felder pro Datensatz*/

    private void fuellePreparedStatementKomplett(PreparedStatement pPStm, int pOffset, EclAppAktivitaeten pEcl) {

        int startOffset = pOffset; /*Nur erforderlich zum Überprüfen von Programmierfehlern - Feldanzahl*/

        try {

            pPStm.setString(pOffset, pEcl.eindeutigerKey);
            pOffset++;
            pPStm.setInt(pOffset, pEcl.funktion);
            pOffset++;

            pPStm.setInt(pOffset, pEcl.mandant);
            pOffset++;
            pPStm.setInt(pOffset, pEcl.hvJahr);
            pOffset++;
            pPStm.setString(pOffset, pEcl.hvNummer);
            pOffset++;
            pPStm.setString(pOffset, pEcl.datenbereich);
            pOffset++;

            pPStm.setString(pOffset, pEcl.paramter1);
            pOffset++;
            pPStm.setString(pOffset, pEcl.paramter2);
            pOffset++;
            pPStm.setString(pOffset, pEcl.paramter3);
            pOffset++;
            pPStm.setString(pOffset, pEcl.paramter4);
            pOffset++;
            pPStm.setString(pOffset, pEcl.paramter5);
            pOffset++;

            pPStm.setString(pOffset, pEcl.datumUhrzeit);
            pOffset++;

            if (pOffset - startOffset != anzfelder) {
                /*Nur wg. Überprüfung auf Programmierfehler - alle Felder berücksichtigt?*/
                CaBug.drucke("DbAppAktivitaeten.fuellePreparedStatementKomplett 002");
            }

        } catch (SQLException e) {
            CaBug.drucke("DbAppAktivitaeten.fuellePreparedStatementKomplett 001");
            e.printStackTrace();
        }

    }

    /**Insert
     * 
     * Returnwert:
     * =1 => Insert erfolgreich
     * ansonsten: Fehler
     */
    public int insert(EclAppAktivitaeten pEcl) {

        int erg = 0;
        dbBasis.beginTransaction();

        pEcl.eindeutigerKey = dbBundle.dbEindeutigerKey.getNextFree();
        pEcl.mandant = dbBundle.clGlobalVar.mandant;
        pEcl.hvJahr = dbBundle.clGlobalVar.hvJahr;
        pEcl.hvNummer = dbBundle.clGlobalVar.hvNummer;
        pEcl.datenbereich = dbBundle.clGlobalVar.datenbereich;
        pEcl.datumUhrzeit = CaDatumZeit.DatumZeitStringFuerDatenbank();

        try {

            /*Felder Neuanlage füllen*/
            String lSql = "INSERT INTO " + dbBundle.getSchemaAllgemein() + "tbl_appaktivitaeten " + "("
                    + "eindeutigerKey, funktion, " + "mandant, hvJahr, hvNummer, datenbereich, "
                    + "paramter1, paramter2, paramter3, paramter4, paramter5, datumUhrzeit " + ")" + "VALUES ("
                    + "?, ?,  " + "?, ?, ?, ?, " + "?, ?, ?, ?, ?, ? " + ")";
            PreparedStatement lPStm = verbindung.prepareStatement(lSql, ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);

            fuellePreparedStatementKomplett(lPStm, 1, pEcl);

            erg = lPStm.executeUpdate();
            lPStm.close();
        } catch (Exception e2) {
            CaBug.drucke("DbAppAktivitaeten.insert 001");
            System.err.println(" " + e2.getMessage());
        }

        if (erg == 0) {/*Fehler beim Einfügen - d.h. primaryKey bereits vorhanden*/
            dbBasis.rollbackTransaction();
            dbBasis.endTransaction();
            return (-1);
        }

        /* Ende Transaktion */
        dbBasis.endTransaction();
        return (1);
    }

    public int read(String pEeindeutigerKey) {
        int anzInArray = 0;
        PreparedStatement lPStm = null;

        try {
            String lSql = "SELECT * from " + dbBundle.getSchemaAllgemein() + "tbl_appaktivitaeten where "
                    + "eindeutigerKey=?; ";
            lPStm = verbindung.prepareStatement(lSql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            lPStm.setString(1, pEeindeutigerKey);

            ResultSet lErgebnis = lPStm.executeQuery();
            lErgebnis.last();
            anzInArray = lErgebnis.getRow();
            lErgebnis.beforeFirst();

            ergebnisArray = new EclAppAktivitaeten[anzInArray];

            int i = 0;
            while (lErgebnis.next() == true) {
                ergebnisArray[i] = this.decodeErgebnis(lErgebnis);
                i++;
            }
            lErgebnis.close();
            lPStm.close();

        } catch (Exception e) {
            CaBug.drucke("DbAppAktivitaeten.read 003");
            System.err.println(" " + e.getMessage());
            return (-1);
        }
        return (anzInArray);
    }

    public int delete(String pEeindeutigerKey) {
        try {

            String sql = "DELETE FROM " + dbBundle.getSchemaAllgemein()
                    + "tbl_appaktivitaeten WHERE eindeutigerKey=?; ";

            PreparedStatement pstm1 = verbindung.prepareStatement(sql);
            pstm1.setString(1, pEeindeutigerKey);

            int ergebnis1 = pstm1.executeUpdate();
            pstm1.close();
            if (ergebnis1 == 0) {
                return (CaFehler.pfXyWurdeVonAnderemBenutzerVeraendert);
            }
        } catch (Exception e1) {
            CaBug.drucke("DbAppAktivitaeten.delete 001");
            System.err.println(" " + e1.getMessage());
            return (-1);
        }

        return (1);
    }

}
