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
import de.meetingapps.meetingportal.meetComEntities.EclZugeordneteKennung;

public class DbZuordnungKennung {

    private Connection verbindung = null;
    //	private DbBasis dbBasis=null;
    private DbBundle dbBundle = null;

    private EclZugeordneteKennung ergebnisArray[] = null;

    /*************************Initialisierung***************************/
    public DbZuordnungKennung(DbBundle pDbBundle) {
        /* Verbindung in lokale Daten eintragen*/
        if (pDbBundle == null) {
            CaBug.drucke("001 - dbBundle nicht initialisiert");
            return;
        }
        if (pDbBundle.dbBasis == null) {
            CaBug.drucke("002 - dbBasis nicht initialisiert");
            return;
        }

        //		dbBasis=pDbBundle.dbBasis;
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
    public EclZugeordneteKennung[] ergebnis() {
        return ergebnisArray;
    }

    /**********Liefert pN-tes Element des Ergebnisses der read*Methoden**************
     * pN geht von 0 bis anzErgebnis-1*/
    public EclZugeordneteKennung ergebnisPosition(int pN) {
        if (ergebnisArray == null) {
            CaBug.drucke("001");
            return null;
        }
        if (pN < 0) {
            CaBug.drucke("002");
            return null;
        }
        if (pN >= ergebnisArray.length) {
            CaBug.drucke("003");
            return null;
        }
        return ergebnisArray[pN];
    }

    public int createTable() {
        int rc = 0;
        DbLowLevel lDbLowLevel = new DbLowLevel(dbBundle);
        rc = lDbLowLevel.createTable("CREATE TABLE " + dbBundle.getSchemaMandant() + "tbl_zugeordneteKennung ( "
                + "`loginKennung` varchar(20) NOT NULL, " + "`zugeordneteKennung` varchar(20) NOT NULL, "
                + "`kennungArt` int(11) DEFAULT NULL, " + "`zuordnungIstFuerPraesenzVerifiziert` int(11) DEFAULT NULL, "
                + "PRIMARY KEY (`loginKennung`,`zugeordneteKennung`), " + "KEY `IDX_LOGINKENNUNG` (`loginKennung`) "
                + ") ");
        return rc;
    }

    /**************************deleteAll Löschen aller Datensätze  eines Mandanten****************/
    public int deleteAll() {
        return dbBundle.dbLowLevel.deleteAlle(
                "DELETE FROM " + dbBundle.getSchemaMandant() + "tbl_zugeordneteKennung");
    }

    /**************************setzt aktuelle Mandantennummer bei allen Datensätzen****************/
    public int updateMandant() {
        return dbBundle.dbLowLevel.rawUpdateMandant(dbBundle.getSchemaMandant() + "tbl_zugeordneteKennung");
    }

    /********** dekodiert die aktuelle Position aus ergebnis  und gibt dieses zurück******/
    EclZugeordneteKennung decodeErgebnis(ResultSet pErgebnis) {

        EclZugeordneteKennung lEclReturn = new EclZugeordneteKennung();

        try {
            lEclReturn.loginKennung = pErgebnis.getString("loginKennung");
            lEclReturn.zugeordneteKennung = pErgebnis.getString("zugeordneteKennung");
            lEclReturn.kennungArt = pErgebnis.getInt("kennungArt");
            lEclReturn.zuordnungIstFuerPraesenzVerifiziert = pErgebnis.getInt("zuordnungIstFuerPraesenzVerifiziert");
        } catch (Exception e) {
            CaBug.drucke("001");
            System.err.println(" " + e.getMessage());
        }

        return lEclReturn;
    }

    /********************* Fuellen Prepared Statement mit allen Feldern, beginnend bei offset.*****************
     * Kann sowohl für Insert, als auch für update verwendet werden.
     * 
     * offset= Startposition des ersten Feldes (also z.B. 1)
     */
    private int anzfelder = 4; /*Anpassen auf Anzahl der Felder pro Datensatz*/

    private void fuellePreparedStatementKomplett(PreparedStatement pPStm, int pOffset, EclZugeordneteKennung pEcl) {

        int startOffset = pOffset; /*Nur erforderlich zum Überprüfen von Programmierfehlern - Feldanzahl*/

        try {
            pPStm.setString(pOffset, pEcl.loginKennung);
            pOffset++;
            pPStm.setString(pOffset, pEcl.zugeordneteKennung);
            pOffset++;
            pPStm.setInt(pOffset, pEcl.kennungArt);
            pOffset++;
            pPStm.setInt(pOffset, pEcl.zuordnungIstFuerPraesenzVerifiziert);
            pOffset++;

            if (pOffset - startOffset != anzfelder) {
                /*Nur wg. Überprüfung auf Programmierfehler - alle Felder berücksichtigt?*/
                CaBug.drucke("002");
            }

        } catch (SQLException e) {
            CaBug.drucke("001");
            e.printStackTrace();
        }

    }

    /**Insert
     * 
     * Returnwert:
     * =1 => Insert erfolgreich
     * ansonsten: Fehler
     */
    public int insert(EclZugeordneteKennung pEcl) {

        int erg = 0;
        try {

            /*Felder Neuanlage füllen*/
            String lSql = "INSERT INTO " + dbBundle.getSchemaMandant() + "tbl_zugeordneteKennung " + "("
                    + "loginKennung, zugeordneteKennung, kennungArt, zuordnungIstFuerPraesenzVerifiziert " + ")"
                    + "VALUES (" + "?, ?, ?, ? " + ")";
            PreparedStatement lPStm = verbindung.prepareStatement(lSql, ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);

            fuellePreparedStatementKomplett(lPStm, 1, pEcl);

            erg = lPStm.executeUpdate();
            lPStm.close();
        } catch (Exception e2) {
            CaBug.drucke("001");
            System.err.println(" " + e2.getMessage());
        }

        if (erg == 0) {/*Fehler beim Einfügen - d.h. primaryKey bereits vorhanden*/
            return (-1);
        }

        return (1);
    }

    /**Mandant und Ident müssen übergeben werden.
     * 
     * Return-Wert = Anzahl der gefundenen Sätze (oder Fehlermeldung <0)*/
    public int read(String pLoginKennung) {
        int anzInArray = 0;
        PreparedStatement lPStm = null;

        try {
            String lSql = "SELECT * from " + dbBundle.getSchemaMandant() + "tbl_zugeordneteKennung where "
                    + "loginKennung=?; ";
            lPStm = verbindung.prepareStatement(lSql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            lPStm.setString(1, pLoginKennung);

            ResultSet lErgebnis = lPStm.executeQuery();
            lErgebnis.last();
            anzInArray = lErgebnis.getRow();
            lErgebnis.beforeFirst();

            ergebnisArray = new EclZugeordneteKennung[anzInArray];

            int i = 0;
            while (lErgebnis.next() == true) {
                ergebnisArray[i] = this.decodeErgebnis(lErgebnis);
                i++;
            }
            lErgebnis.close();
            lPStm.close();

        } catch (Exception e) {
            CaBug.drucke("003");
            System.err.println(" " + e.getMessage());
            return (-1);
        }
        return (anzInArray);
    }

    
    
    public int read_zugeordneteKennung(String pZugeordneteKennung) {
        int anzInArray = 0;
        PreparedStatement lPStm = null;

        try {
            String lSql = "SELECT * from " + dbBundle.getSchemaMandant() + "tbl_zugeordneteKennung where "
                    + "zugeordneteKennung=?; ";
            lPStm = verbindung.prepareStatement(lSql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            lPStm.setString(1, pZugeordneteKennung);

            ResultSet lErgebnis = lPStm.executeQuery();
            lErgebnis.last();
            anzInArray = lErgebnis.getRow();
            lErgebnis.beforeFirst();

            ergebnisArray = new EclZugeordneteKennung[anzInArray];

            int i = 0;
            while (lErgebnis.next() == true) {
                ergebnisArray[i] = this.decodeErgebnis(lErgebnis);
                i++;
            }
            lErgebnis.close();
            lPStm.close();

        } catch (Exception e) {
            CaBug.drucke("003");
            System.err.println(" " + e.getMessage());
            return (-1);
        }
        return (anzInArray);
    }

    
    
    /**Update. 
     * 
     * Returnwert:
     * pfXyWurdeVonAnderemBenutzerVeraendert
     * -1 => unbekannter Fehler
     * 1 = Update wurde durchgeführt.
     * 
     */
    public int update(EclZugeordneteKennung pEcl) {

        try {

            String lSql = "UPDATE " + dbBundle.getSchemaMandant() + "tbl_zugeordneteKennung SET "
                    + "loginKennung=?, zugeordneteKennung=?, " + "kennungArt=?, zuordnungIstFuerPraesenzVerifiziert=? "
                    + "WHERE " + "loginKennung=? AND zugeordneteKennung=? ";

            PreparedStatement lPStm = verbindung.prepareStatement(lSql);
            fuellePreparedStatementKomplett(lPStm, 1, pEcl);
            lPStm.setString(anzfelder + 1, pEcl.loginKennung);
            lPStm.setString(anzfelder + 2, pEcl.zugeordneteKennung);

            int ergebnis = lPStm.executeUpdate();
            lPStm.close();
            if (ergebnis == 0) {
                return (CaFehler.pfXyWurdeVonAnderemBenutzerVeraendert);
            }
        } catch (Exception e1) {
            CaBug.drucke("001");
            System.err.println(" " + e1.getMessage());
            return (-1);
        }

        return (1);
    }

    /**Return-Werte:
     * -1 => undefinierter Fehler
     * 1 => Löschen erfolgreich
     */
    public int delete(EclZugeordneteKennung pEcl) {
        try {

            String sql = "DELETE FROM " + dbBundle.getSchemaMandant() + "tbl_zugeordneteKennung WHERE "
                    + "loginKennung=? AND zugeordneteKennung=? ";

            PreparedStatement pstm1 = verbindung.prepareStatement(sql);
            pstm1.setString(1, pEcl.loginKennung);
            pstm1.setString(2, pEcl.zugeordneteKennung);

            int ergebnis1 = pstm1.executeUpdate();
            pstm1.close();
            if (ergebnis1 == 0) {
                return (CaFehler.pfXyWurdeVonAnderemBenutzerVeraendert);
            }
        } catch (Exception e1) {
            CaBug.drucke("001");
            System.err.println(" " + e1.getMessage());
            return (-1);
        }

        return (1);
    }

}
