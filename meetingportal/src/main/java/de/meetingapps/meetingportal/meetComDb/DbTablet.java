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
import de.meetingapps.meetingportal.meetComEntities.EclTablet;

public class DbTablet {

    private Connection verbindung = null;
    //	private DbBasis dbBasis=null;
    private DbBundle dbBundle = null;

    /*************************Initialisierung***************************/
    /* Verbindung in lokale Daten eintragen*/
    public DbTablet(DbBundle datenbankbundle) {
        if (datenbankbundle.dbBasis == null) {
            System.err.println("vmcdbBasis nicht initialisiert");
            return;
        }
        // 		dbBasis=datenbankbundle.dbBasis;
        verbindung = datenbankbundle.dbBasis.verbindung;
        dbBundle = datenbankbundle;
    }

    /*****************Ergebnis-Array für Standard-Abfragen*******************************/
    /**Soll nicht direkt verwendet werden, sondern nur über die Zugriffsfunktionen!
     * Aktuell noch public, da direkter Zugriff über DbMeldungen
     */
    public EclTablet ergebnisArray[] = null;

    public int anzErgebnisGefunden() {
        if (ergebnisArray == null) {
            return 0;
        }
        return ergebnisArray.length;
    }

    public EclTablet ergebnisGefunden(int lfd) {
        return ergebnisArray[lfd];
    }

    public int createTable() {
        int rc = 0;
        DbLowLevel lDbLowLevel = new DbLowLevel(dbBundle);
        rc = lDbLowLevel.createTable("CREATE TABLE " + dbBundle.getSchemaAllgemein() + "tbl_tablet ( "
                + "`arbeitsplatzNr` int(11) NOT NULL, " + "`status` int(11) DEFAULT NULL, "
                + "PRIMARY KEY (`arbeitsplatzNr`) " + ") ");
        return rc;
    }

    /**************************deleteAll Löschen aller Datensätze****************/
    public int deleteAll() {
        int erg = 0;

        /*tbl_weisungMeldung*/
        try {

            String sql1 = "DELETE FROM " + dbBundle.getSchemaAllgemein() + "tbl_tablet;";
            PreparedStatement pstm1 = verbindung.prepareStatement(sql1);

            erg = 0; /*Falls Duplicate Key, dann wird exception geschmissen und erg wohl nicht gefüllt!*/
            erg = pstm1.executeUpdate();
            pstm1.close();

        } catch (Exception e2) {
            CaBug.drucke("DbTablet.deleteAll 001");
            System.err.println(" " + e2.getMessage());
            return (erg);
        }

        return 1;
    }

    /** dekodiert die aktuelle Position aus ergebnis in EclWeisungMeldung und gibt dieses zurück*/
    public EclTablet decodeErgebnis(ResultSet ergebnis) {
        EclTablet lTablet = new EclTablet();

        try {
            lTablet.arbeitsplatzNr = ergebnis.getInt("tb.arbeitsplatzNr");
            lTablet.status = ergebnis.getInt("tb.status");

        } catch (Exception e) {
            CaBug.drucke("DbTablet.decodeErgebnis 001");
            System.err.println(" " + e.getMessage());
        }

        return lTablet;
    }

    /** Fuellen Prepared Statement mit allen Feldern, beginnend bei offset.
     * Kann sowohl für Insert, als auch für update verwendet werden.
     * 
     * offset= Startposition des ersten Feldes (also z.B. 1)
     */
    private int anzfelder = 2;

    private void fuellePreparedStatementKomplett(PreparedStatement pstm, int offset, EclTablet lTablet) {

        try {
            pstm.setInt(offset, lTablet.arbeitsplatzNr);
            offset++;
            pstm.setInt(offset, lTablet.status);
            offset++;

        } catch (SQLException e) {
            CaBug.drucke("DbTablet.fuellePreparedStatementKomplett 001");
            e.printStackTrace();
        }
    }

    public int insert(EclTablet lTablet) {
        int erg = 0;

        try {

            /*Felder Neuanlage füllen*/
            String sql1 = "INSERT INTO " + dbBundle.getSchemaAllgemein() + "tbl_tablet " + "(" + "arbeitsplatzNr,  "
                    + "status ";
            sql1 = sql1 + ")" + "VALUES (" + "?, ? )";
            PreparedStatement pstm1 = verbindung.prepareStatement(sql1, ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);

            fuellePreparedStatementKomplett(pstm1, 1, lTablet);

            erg = 0; /*Falls Duplicate Key, dann wird exception geschmissen und erg wohl nicht gefüllt!*/
            //			System.out.println("vor Update");
            erg = pstm1.executeUpdate();
            //			System.out.println("nach Update");
            pstm1.close();
        } catch (Exception e2) {
            CaBug.drucke("DbTablet.insert 001");
            System.err.println(" " + e2.getMessage());
        }

        if (erg == 0) {/*Fehler beim Einfügen - d.h. bereits vorhanden*/
            return (-1);
        }

        /* Ende Transaktion */

        return (1);
    }

    public int lese_Ident(int lIdent) {
        int anzInArray = 0;
        try {

            String sql = "";
            sql = "SELECT tb.* from " + dbBundle.getSchemaAllgemein() + "tbl_tablet tb where " + "tb.arbeitsplatzNr=?;";

            PreparedStatement pstm1 = verbindung.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
            pstm1.setInt(1, lIdent);

            ResultSet ergebnis = pstm1.executeQuery();
            ergebnis.last();
            anzInArray = ergebnis.getRow();
            ergebnis.beforeFirst();

            ergebnisArray = new EclTablet[anzInArray];

            int i = 0;
            while (ergebnis.next() == true) {
                ergebnisArray[i] = this.decodeErgebnis(ergebnis);
                i++;
            }
            ergebnis.close();
            pstm1.close();

        } catch (Exception e) {
            CaBug.drucke("DbTablet.leseIdent 001");
            System.err.println(" " + e.getMessage());
        }

        return (anzInArray);

    }

    public int lese_all() {
        int anzInArray = 0;
        try {

            String sql = "";
            sql = "SELECT tb.* from " + dbBundle.getSchemaAllgemein() + "tbl_tablet tb;";

            PreparedStatement pstm1 = verbindung.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);

            ResultSet ergebnis = pstm1.executeQuery();
            ergebnis.last();
            anzInArray = ergebnis.getRow();
            ergebnis.beforeFirst();

            ergebnisArray = new EclTablet[anzInArray];

            int i = 0;
            while (ergebnis.next() == true) {
                ergebnisArray[i] = this.decodeErgebnis(ergebnis);
                i++;
            }
            ergebnis.close();
            pstm1.close();

        } catch (Exception e) {
            CaBug.drucke("DbTablet.lese_all 001");
            System.err.println(" " + e.getMessage());
        }

        return (anzInArray);

    }

    public int update(EclTablet lTablet) {

        try {

            String sql = "UPDATE " + dbBundle.getSchemaAllgemein() + "tbl_tablet SET " + "arbeitsplatzNr=?, status=? ";
            sql = sql + " WHERE " + "arbeitsplatzNr=?";

            PreparedStatement pstm1 = verbindung.prepareStatement(sql);
            fuellePreparedStatementKomplett(pstm1, 1, lTablet);
            pstm1.setInt(anzfelder + 1, lTablet.arbeitsplatzNr);

            int ergebnis1 = pstm1.executeUpdate();
            pstm1.close();
            if (ergebnis1 == 0) {
                return (CaFehler.pfdUnbekannterFehler);
            }
        } catch (Exception e1) {
            CaBug.drucke("DbTablet.update 001");
            System.err.println(" " + e1.getMessage());
            return (CaFehler.pfdUnbekannterFehler);

        }

        return (1);
    }

}
