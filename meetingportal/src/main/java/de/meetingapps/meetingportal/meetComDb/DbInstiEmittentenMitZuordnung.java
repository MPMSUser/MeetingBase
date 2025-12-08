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

/**Hinweis: diese Klasse kann als "Musterklasse" / Blaupause für andere Db-Zugriffsklassen verwendet werden*/

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import de.meetingapps.meetingportal.meetComAllg.CaBug;
import de.meetingapps.meetingportal.meetComAllg.CaFehler;
import de.meetingapps.meetingportal.meetComEntities.EclInstiEmittentenMitZuordnung;

public class DbInstiEmittentenMitZuordnung {

    private Connection verbindung = null;
//    private DbBasis dbBasis = null;
    private DbBundle dbBundle = null;

    private EclInstiEmittentenMitZuordnung ergebnisArray[] = null;

    /*************************Initialisierung***************************/
    public DbInstiEmittentenMitZuordnung(DbBundle pDbBundle) {
        /* Verbindung in lokale Daten eintragen*/
        if (pDbBundle == null) {
            CaBug.drucke("DbInstiEmittentenMitZuordnung.init 001 - dbBundle nicht initialisiert");
            return;
        }
        if (pDbBundle.dbBasis == null) {
            CaBug.drucke("DbInstiEmittentenMitZuordnung.init 002 - dbBasis nicht initialisiert");
            return;
        }

//        dbBasis = pDbBundle.dbBasis;
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
    public EclInstiEmittentenMitZuordnung[] ergebnis() {
        return ergebnisArray;
    }

    /**********Liefert pN-tes Element des Ergebnisses der read*Methoden**************
     * pN geht von 0 bis anzErgebnis-1*/
    public EclInstiEmittentenMitZuordnung ergebnisPosition(int pN) {
        if (ergebnisArray == null) {
            CaBug.drucke("DbInstiEmittentenMitZuordnung.ergebnisPosition 001");
            return null;
        }
        if (pN < 0) {
            CaBug.drucke("DbInstiEmittentenMitZuordnung.ergebnisPosition 002");
            return null;
        }
        if (pN >= ergebnisArray.length) {
            CaBug.drucke("DbInstiEmittentenMitZuordnung.ergebnisPosition 003");
            return null;
        }
        return ergebnisArray[pN];
    }

    public int createTable() {
        int rc = 0;
        DbLowLevel lDbLowLevel = new DbLowLevel(dbBundle);
        rc = lDbLowLevel
                .createTable("CREATE TABLE " + dbBundle.getSchemaAllgemein() + "tbl_instiEmittentenMitZuordnung ( "
                        + "`identInsti` int(11) NOT NULL, " + "`mandant` int(11) NOT NULL, "
                        + "`hvJahr` int(11) NOT NULL, " + "`hvNummer` char(1) NOT NULL, " + "`dbArt` char(1) NOT NULL, "
                        + "PRIMARY KEY (`identInsti`,`mandant`,`hvJahr`,`hvNummer`,`dbArt`) " + ") ");
        return rc;
    }

    /**************************deleteAll Löschen aller Datensätze  eines Mandanten****************/
    public int deleteAll() {
        return dbBundle.dbLowLevel.deleteAlle /*deleteMandant*/("DELETE FROM " + dbBundle.getSchemaAllgemein()
                + "tbl_instiEmittentenMitZuordnung;" /* where mandant=?*/);
    }

    //	/**************************setzt aktuelle Mandantennummer bei allen Datensätzen****************/
    //	public int updateMandant(){
    //		return dbBundle.dbLowLevel.rawUpdateMandant(dbBundle.getSchemaAllgemein()+"tbl_instiEmittentenMitZuordnung");
    //	}

    /********** dekodiert die aktuelle Position aus ergebnis  und gibt dieses zurück******/
    EclInstiEmittentenMitZuordnung decodeErgebnis(ResultSet pErgebnis) {

        EclInstiEmittentenMitZuordnung lEclReturn = new EclInstiEmittentenMitZuordnung();

        try {
            lEclReturn.identInsti = pErgebnis.getInt("identInsti");
            lEclReturn.mandant = pErgebnis.getInt("mandant");
            lEclReturn.hvJahr = pErgebnis.getInt("hvJahr");
            lEclReturn.hvNummer = pErgebnis.getString("hvNummer");
            lEclReturn.dbArt = pErgebnis.getString("dbArt");
        } catch (Exception e) {
            CaBug.drucke("DbInstiEmittentenMitZuordnung.decodeErgebnis 001");
            System.err.println(" " + e.getMessage());
        }

        return lEclReturn;
    }

    /********************* Fuellen Prepared Statement mit allen Feldern, beginnend bei offset.*****************
     * Kann sowohl für Insert, als auch für update verwendet werden.
     * 
     * offset= Startposition des ersten Feldes (also z.B. 1)
     */
    private int anzfelder = 5; /*Anpassen auf Anzahl der Felder pro Datensatz*/

    private void fuellePreparedStatementKomplett(PreparedStatement pPStm, int pOffset,
            EclInstiEmittentenMitZuordnung pEcl) {

        int startOffset = pOffset; /*Nur erforderlich zum Überprüfen von Programmierfehlern - Feldanzahl*/

        try {
            pPStm.setInt(pOffset, pEcl.identInsti);
            pOffset++;
            pPStm.setInt(pOffset, pEcl.mandant);
            pOffset++;
            pPStm.setInt(pOffset, pEcl.hvJahr);
            pOffset++;
            pPStm.setString(pOffset, pEcl.hvNummer);
            pOffset++;
            pPStm.setString(pOffset, pEcl.dbArt);
            pOffset++;

            if (pOffset - startOffset != anzfelder) {
                /*Nur wg. Überprüfung auf Programmierfehler - alle Felder berücksichtigt?*/
                CaBug.drucke("DbInstiEmittentenMitZuordnung.fuellePreparedStatementKomplett 002");
            }

        } catch (SQLException e) {
            CaBug.drucke("DbInstiEmittentenMitZuordnung.fuellePreparedStatementKomplett 001");
            e.printStackTrace();
        }

    }

    /**Insert
     * Alle Felder in pEcl müssen gefüllt sein!
     * Returnwert:
     * =1 => Insert erfolgreich
     * ansonsten: Fehler
     */
    public int insert(EclInstiEmittentenMitZuordnung pEcl) {

        int erg = 0;

        try {

            /*Felder Neuanlage füllen*/
            String lSql = "INSERT INTO " + dbBundle.getSchemaAllgemein() + "tbl_instiEmittentenMitZuordnung " + "("
                    + "identInsti, mandant, hvJahr, " + "hvNummer, dbArt " + ")" + "VALUES (" + "?, ?, ?, " + "?, ? "
                    + ")";
            PreparedStatement lPStm = verbindung.prepareStatement(lSql, ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);

            fuellePreparedStatementKomplett(lPStm, 1, pEcl);

            erg = lPStm.executeUpdate();
            lPStm.close();
        } catch (Exception e2) {
            CaBug.drucke("DbInstiEmittentenMitZuordnung.insert 001");
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
    public int read(EclInstiEmittentenMitZuordnung pEcl) {
        int anzInArray = 0;
        PreparedStatement lPStm = null;

        try {
            String lSql = "SELECT * from " + dbBundle.getSchemaAllgemein() + "tbl_instiEmittentenMitZuordnung where "
                    + "identInsti=? AND " + "mandant=? AND " + "hvJahr=? AND " + "hvNummer=? AND " + "dbArt=? ;";
            lPStm = verbindung.prepareStatement(lSql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            lPStm.setInt(1, pEcl.identInsti);
            lPStm.setInt(2, pEcl.mandant);
            lPStm.setInt(3, pEcl.hvJahr);
            lPStm.setString(4, pEcl.hvNummer);
            lPStm.setString(5, pEcl.dbArt);

            ResultSet lErgebnis = lPStm.executeQuery();
            lErgebnis.last();
            anzInArray = lErgebnis.getRow();
            lErgebnis.beforeFirst();

            ergebnisArray = new EclInstiEmittentenMitZuordnung[anzInArray];

            int i = 0;
            while (lErgebnis.next() == true) {
                ergebnisArray[i] = this.decodeErgebnis(lErgebnis);
                i++;
            }
            lErgebnis.close();
            lPStm.close();

        } catch (Exception e) {
            CaBug.drucke("DbInstiEmittentenMitZuordnung.read 003");
            System.err.println(" " + e.getMessage());
            return (-1);
        }
        return (anzInArray);
    }

    public int readAllZuMandant(int pMandant) {
        int anzInArray = 0;
        PreparedStatement lPStm = null;

        try {
            String lSql = "SELECT * from " + dbBundle.getSchemaAllgemein() + "tbl_instiEmittentenMitZuordnung where "
                    + "mandant=? ;";
            lPStm = verbindung.prepareStatement(lSql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            lPStm.setInt(1, pMandant);

            ResultSet lErgebnis = lPStm.executeQuery();
            lErgebnis.last();
            anzInArray = lErgebnis.getRow();
            lErgebnis.beforeFirst();

            ergebnisArray = new EclInstiEmittentenMitZuordnung[anzInArray];

            int i = 0;
            while (lErgebnis.next() == true) {
                ergebnisArray[i] = this.decodeErgebnis(lErgebnis);
                i++;
            }
            lErgebnis.close();
            lPStm.close();

        } catch (Exception e) {
            CaBug.drucke("readAllZuMandant.read 003");
            System.err.println(" " + e.getMessage());
            return (-1);
        }
        return (anzInArray);
    }

    /**Return-Werte:
     * pfXyWurdeVonAnderemBenutzerVeraendert
     * -1 => undefinierter Fehler
     * 1 => Löschen erfolgreich
     */
    public int delete(EclInstiEmittentenMitZuordnung pEcl) {
        try {

            String sql = "DELETE FROM " + dbBundle.getSchemaAllgemein() + "tbl_instiEmittentenMitZuordnung WHERE "
                    + "identInsti=? AND " + "mandant=? AND " + "hvJahr=? AND " + "hvNummer=? AND " + "dbArt=? ;";

            PreparedStatement pstm1 = verbindung.prepareStatement(sql);
            pstm1.setInt(1, pEcl.identInsti);
            pstm1.setInt(2, pEcl.mandant);
            pstm1.setInt(3, pEcl.hvJahr);
            pstm1.setString(4, pEcl.hvNummer);
            pstm1.setString(5, pEcl.dbArt);

            int ergebnis1 = pstm1.executeUpdate();
            pstm1.close();
            if (ergebnis1 == 0) {
                return (CaFehler.pfXyWurdeVonAnderemBenutzerVeraendert);
            }
        } catch (Exception e1) {
            CaBug.drucke("DbInstiEmittentenMitZuordnung.delete 001");
            System.err.println(" " + e1.getMessage());
            return (-1);
        }

        return (1);
    }

}
