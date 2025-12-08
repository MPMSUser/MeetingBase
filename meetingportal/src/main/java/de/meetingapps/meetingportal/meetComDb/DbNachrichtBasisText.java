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
import de.meetingapps.meetingportal.meetComEntities.EclNachrichtBasisText;

public class DbNachrichtBasisText {

    private Connection verbindung = null;
    private DbBasis dbBasis = null;
    private DbBundle dbBundle = null;

    private EclNachrichtBasisText ergebnisArray[] = null;

    /*************************Initialisierung***************************/
    public DbNachrichtBasisText(DbBundle pDbBundle) {
        /* Verbindung in lokale Daten eintragen*/
        if (pDbBundle == null) {
            CaBug.drucke("DbNachrichtBasisText.init 001 - dbBundle nicht initialisiert");
            return;
        }
        if (pDbBundle.dbBasis == null) {
            CaBug.drucke("DbNachrichtBasisText.init 002 - dbBasis nicht initialisiert");
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
    public EclNachrichtBasisText[] ergebnis() {
        return ergebnisArray;
    }

    /**********Liefert pN-tes Element des Ergebnisses der read*Methoden**************
     * pN geht von 0 bis anzErgebnis-1*/
    public EclNachrichtBasisText ergebnisPosition(int pN) {
        if (ergebnisArray == null) {
            CaBug.drucke("DbNachrichtBasisText.ergebnisPosition 001");
            return null;
        }
        if (pN < 0) {
            CaBug.drucke("DbNachrichtBasisText.ergebnisPosition 002");
            return null;
        }
        if (pN >= ergebnisArray.length) {
            CaBug.drucke("DbNachrichtBasisText.ergebnisPosition 003");
            return null;
        }
        return ergebnisArray[pN];
    }

    public int createTable() {
        int rc = 0;
        DbLowLevel lDbLowLevel = new DbLowLevel(dbBundle);
        rc = lDbLowLevel.createTable("CREATE TABLE " + dbBundle.getSchemaAllgemein() + "tbl_nachrichtBasisText ( "
                + "`ident` int(11) NOT NULL, " + "`beschreibung` varchar(80) DEFAULT NULL, "
                + "`betreff` varchar(80) DEFAULT NULL, " + "`mailText` varchar(2000) DEFAULT NULL, "
                + "PRIMARY KEY (`ident`) " + ") ");
        return rc;
    }

    /**************************deleteAll Löschen aller Datensätze  eines Mandanten****************/
    public int deleteAll() {
        return dbBundle.dbLowLevel.deleteAlle /*deleteMandant*/(
                "DELETE FROM " + dbBundle.getSchemaAllgemein() + "tbl_nachrichtBasisText;" /* where mandant=?*/);
    }

    /********** dekodiert die aktuelle Position aus ergebnis  und gibt dieses zurück******/
    EclNachrichtBasisText decodeErgebnis(ResultSet pErgebnis) {

        EclNachrichtBasisText lEclReturn = new EclNachrichtBasisText();

        try {
            lEclReturn.ident = pErgebnis.getInt("nacba.ident");

            lEclReturn.beschreibung = pErgebnis.getString("nacba.beschreibung");
            lEclReturn.betreff = pErgebnis.getString("nacba.betreff");
            lEclReturn.mailText = pErgebnis.getString("nacba.mailText");
        } catch (Exception e) {
            CaBug.drucke("DbNachrichtBasisText.decodeErgebnis 001");
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

    private void fuellePreparedStatementKomplett(PreparedStatement pPStm, int pOffset, EclNachrichtBasisText pEcl) {

        int startOffset = pOffset; /*Nur erforderlich zum Überprüfen von Programmierfehlern - Feldanzahl*/

        try {
            pPStm.setInt(pOffset, pEcl.ident);
            pOffset++;

            pPStm.setString(pOffset, pEcl.beschreibung);
            pOffset++;

            pPStm.setString(pOffset, pEcl.betreff);
            pOffset++;
            pPStm.setString(pOffset, pEcl.mailText);
            pOffset++;

            if (pOffset - startOffset != anzfelder) {
                /*Nur wg. Überprüfung auf Programmierfehler - alle Felder berücksichtigt?*/
                CaBug.drucke("DbNachrichtBasisText.fuellePreparedStatementKomplett 002");
            }

        } catch (SQLException e) {
            CaBug.drucke("DbNachrichtBasisText.fuellePreparedStatementKomplett 001");
            e.printStackTrace();
        }

    }

    /**Insert
     * 
     * Returnwert:
     * =1 => Insert erfolgreich
     * ansonsten: Fehler
     */
    public int insert(EclNachrichtBasisText pEcl) {

        int erg = 0;

        try {

            /*Felder Neuanlage füllen*/
            String lSql = "INSERT INTO " + dbBundle.getSchemaAllgemein() + "tbl_nachrichtBasisText " + "("
                    + "ident, beschreibung, " + "betreff, mailText " + ")" + "VALUES (" + "?, ?, " + "?, ? " + ")";
            PreparedStatement lPStm = verbindung.prepareStatement(lSql, ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);

            fuellePreparedStatementKomplett(lPStm, 1, pEcl);

            erg = lPStm.executeUpdate();
            lPStm.close();
        } catch (Exception e2) {
            CaBug.drucke("DbNachrichtBasisText.insert 001");
            System.err.println(" " + e2.getMessage());
        }

        if (erg == 0) {/*Fehler beim Einfügen - d.h. primaryKey bereits vorhanden*/
            return (-1);
        }

        /* Ende Transaktion */
        return (1);
    }

    /**pIdent==0 => alle
     * 
     * Return-Wert = Anzahl der gefundenen Sätze (oder Fehlermeldung <0)*/
    public int read(int pIdent) {
        int anzInArray = 0;
        PreparedStatement lPStm = null;

        try {
            String lSql = "SELECT * from " + dbBundle.getSchemaAllgemein() + "tbl_nachrichtBasisText nacba ";
            if (pIdent > 0) {
                lSql = lSql + "where nacba.ident=? ";
            }
            lSql = lSql + "ORDER BY nacba.ident;";
            lPStm = verbindung.prepareStatement(lSql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            if (pIdent > 0) {
                lPStm.setInt(1, pIdent);
            }

            ResultSet lErgebnis = lPStm.executeQuery();
            lErgebnis.last();
            anzInArray = lErgebnis.getRow();
            lErgebnis.beforeFirst();

            ergebnisArray = new EclNachrichtBasisText[anzInArray];

            int i = 0;
            while (lErgebnis.next() == true) {
                ergebnisArray[i] = this.decodeErgebnis(lErgebnis);
                i++;
            }
            lErgebnis.close();
            lPStm.close();

        } catch (Exception e) {
            CaBug.drucke("DbNachrichtBasisText.read 003");
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
    public int update(EclNachrichtBasisText pEcl) {

        try {

            String lSql = "UPDATE " + dbBundle.getSchemaAllgemein() + "tbl_nachrichtBasisText SET "
                    + "ident=?, beschreibung=?, " + "betreff=?, mailText=? " + "WHERE " + "ident=? ";

            PreparedStatement lPStm = verbindung.prepareStatement(lSql);
            fuellePreparedStatementKomplett(lPStm, 1, pEcl);
            lPStm.setInt(anzfelder + 1, pEcl.ident);

            int ergebnis = lPStm.executeUpdate();
            lPStm.close();
            if (ergebnis == 0) {
                return (CaFehler.pfXyWurdeVonAnderemBenutzerVeraendert);
            }
        } catch (Exception e1) {
            CaBug.drucke("DbNachrichtBasisText.update 001");
            System.err.println(" " + e1.getMessage());
            return (-1);
        }

        return (1);
    }

    /**Return-Werte:
     * pfXyWurdeVonAnderemBenutzerVeraendert
     * -1 => undefinierter Fehler
     * 1 => Löschen erfolgreich
     */
    public int delete(int pIdent) {
        try {

            String sql = "DELETE FROM " + dbBundle.getSchemaAllgemein() + "tbl_nachrichtBasisText WHERE ident=? ";

            PreparedStatement pstm1 = verbindung.prepareStatement(sql);
            pstm1.setInt(1, pIdent);

            int ergebnis1 = pstm1.executeUpdate();
            pstm1.close();
            if (ergebnis1 == 0) {
                return (CaFehler.pfXyWurdeVonAnderemBenutzerVeraendert);
            }
        } catch (Exception e1) {
            CaBug.drucke("DbNachrichtBasisText.delete 001");
            System.err.println(" " + e1.getMessage());
            return (-1);
        }

        return (1);
    }

}
