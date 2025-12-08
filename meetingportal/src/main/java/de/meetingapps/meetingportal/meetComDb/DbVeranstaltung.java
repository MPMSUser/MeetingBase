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
import de.meetingapps.meetingportal.meetComEntities.EclVeranstaltung;

public class DbVeranstaltung {

    private Connection verbindung = null;
    private DbBasis dbBasis = null;
    private DbBundle dbBundle = null;

    private EclVeranstaltung ergebnisArray[] = null;

    /*************************Initialisierung***************************/
    public DbVeranstaltung(DbBundle pDbBundle) {
        /* Verbindung in lokale Daten eintragen*/
        if (pDbBundle == null) {
            CaBug.drucke("DbVeranstaltung.init 001 - dbBundle nicht initialisiert");
            return;
        }
        if (pDbBundle.dbBasis == null) {
            CaBug.drucke("DbVeranstaltung.init 002 - dbBasis nicht initialisiert");
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
    public EclVeranstaltung[] ergebnis() {
        return ergebnisArray;
    }

    /**********Liefert pN-tes Element des Ergebnisses der read*Methoden**************
     * pN geht von 0 bis anzErgebnis-1*/
    public EclVeranstaltung ergebnisPosition(int pN) {
        if (ergebnisArray == null) {
            CaBug.drucke("DbVeranstaltung.ergebnisPosition 001");
            return null;
        }
        if (pN < 0) {
            CaBug.drucke("DbVeranstaltung.ergebnisPosition 002");
            return null;
        }
        if (pN >= ergebnisArray.length) {
            CaBug.drucke("DbVeranstaltung.ergebnisPosition 003");
            return null;
        }
        return ergebnisArray[pN];
    }

    public int createTable() {
        int rc = 0;
        DbLowLevel lDbLowLevel = new DbLowLevel(dbBundle);
        rc = lDbLowLevel.createTable("CREATE TABLE " + dbBundle.getSchemaMandant() + "tbl_veranstaltung ( "
                + "`mandant` int(11) NOT NULL, " + "`ident` int(11) NOT NULL, "
                + "`veranstaltungsnummer` int(11) NOT NULL, " + "`aktiv` int(11) NOT NULL, "
                + "`kurzText` varchar(800) DEFAULT NULL, " + "`text1` varchar(200) DEFAULT NULL, "
                + "`text2` varchar(200) DEFAULT NULL, " + "`text3` varchar(200) DEFAULT NULL, "
                + "`text4` varchar(200) DEFAULT NULL, " + "`text5` varchar(200) DEFAULT NULL, "
                + "`text6` varchar(200) DEFAULT NULL, " + "`maximaleAnzahlAnmeldungen` int(11) DEFAULT NULL, "
                + "`istAnzahlAnmeldungen` int(11) DEFAULT NULL, " + "`istStandardFuerBundesland` int(11) DEFAULT NULL, "
                + "PRIMARY KEY (`mandant`,`ident`) " + ") ");
        return rc;
    }

    /**************************deleteAll Löschen aller Datensätze  eines Mandanten****************/
    public int deleteAll() {
        dbBundle.dbBasis.resetInterneIdentVeranstaltung();
        return dbBundle.dbLowLevel
                .deleteMandant("DELETE FROM " + dbBundle.getSchemaMandant() + "tbl_veranstaltung where mandant=?");
    }

    /**************************setzt aktuelle Mandantennummer bei allen Datensätzen****************/
    public int updateMandant() {
        return dbBundle.dbLowLevel.rawUpdateMandant(dbBundle.getSchemaMandant() + "tbl_veranstaltung");
    }

    public void reorgInterneIdent() {
        int lMax = dbBundle.dbLowLevel.liefereHoechsteIdent(
                "SELECT MAX(ident) FROM " + dbBundle.getSchemaMandant() + "tbl_veranstaltung where mandant=?");
        if (lMax != -1) {
            dbBundle.dbBasis.resetInterneIdentVeranstaltung(lMax);
        }
    }

    /********** dekodiert die aktuelle Position aus ergebnis  und gibt dieses zurück******/
    EclVeranstaltung decodeErgebnis(ResultSet pErgebnis) {

        EclVeranstaltung lEclReturn = new EclVeranstaltung();

        try {
            lEclReturn.mandant = pErgebnis.getInt("mandant");
            lEclReturn.ident = pErgebnis.getInt("ident");
            lEclReturn.veranstaltungsnummer = pErgebnis.getInt("veranstaltungsnummer");
            lEclReturn.aktiv = pErgebnis.getInt("aktiv");

            lEclReturn.kurzText = pErgebnis.getString("kurzText");
            lEclReturn.text1 = pErgebnis.getString("text1");
            lEclReturn.text2 = pErgebnis.getString("text2");
            lEclReturn.text3 = pErgebnis.getString("text3");
            lEclReturn.text4 = pErgebnis.getString("text4");
            lEclReturn.text5 = pErgebnis.getString("text5");
            lEclReturn.text6 = pErgebnis.getString("text6");

            lEclReturn.maximaleAnzahlAnmeldungen = pErgebnis.getInt("maximaleAnzahlAnmeldungen");
            lEclReturn.istAnzahlAnmeldungen = pErgebnis.getInt("istAnzahlAnmeldungen");
            lEclReturn.istStandardFuerBundesland = pErgebnis.getInt("istStandardFuerBundesland");

        } catch (Exception e) {
            CaBug.drucke("DbVeranstaltung.decodeErgebnis 001");
            System.err.println(" " + e.getMessage());
        }

        return lEclReturn;
    }

    /********************* Fuellen Prepared Statement mit allen Feldern, beginnend bei offset.*****************
     * Kann sowohl für Insert, als auch für update verwendet werden.
     * 
     * offset= Startposition des ersten Feldes (also z.B. 1)
     */
    private int anzfelder = 14; /*Anpassen auf Anzahl der Felder pro Datensatz*/

    private void fuellePreparedStatementKomplett(PreparedStatement pPStm, int pOffset, EclVeranstaltung pEcl) {

        int startOffset = pOffset; /*Nur erforderlich zum Überprüfen von Programmierfehlern - Feldanzahl*/

        try {
            pPStm.setInt(pOffset, pEcl.mandant);
            pOffset++;
            pPStm.setInt(pOffset, pEcl.ident);
            pOffset++;
            pPStm.setInt(pOffset, pEcl.veranstaltungsnummer);
            pOffset++;
            pPStm.setInt(pOffset, pEcl.aktiv);
            pOffset++;

            pPStm.setString(pOffset, pEcl.kurzText);
            pOffset++;
            pPStm.setString(pOffset, pEcl.text1);
            pOffset++;
            pPStm.setString(pOffset, pEcl.text2);
            pOffset++;
            pPStm.setString(pOffset, pEcl.text3);
            pOffset++;
            pPStm.setString(pOffset, pEcl.text4);
            pOffset++;
            pPStm.setString(pOffset, pEcl.text5);
            pOffset++;
            pPStm.setString(pOffset, pEcl.text6);
            pOffset++;

            pPStm.setInt(pOffset, pEcl.maximaleAnzahlAnmeldungen);
            pOffset++;
            pPStm.setInt(pOffset, pEcl.istAnzahlAnmeldungen);
            pOffset++;
            pPStm.setInt(pOffset, pEcl.istStandardFuerBundesland);
            pOffset++;

            if (pOffset - startOffset != anzfelder) {
                /*Nur wg. Überprüfung auf Programmierfehler - alle Felder berücksichtigt?*/
                CaBug.drucke("DbVeranstaltung.fuellePreparedStatementKomplett 002");
            }

        } catch (SQLException e) {
            CaBug.drucke("DbVeranstaltung.fuellePreparedStatementKomplett 001");
            e.printStackTrace();
        }

    }

    /**Insert
     * 
     * Returnwert:
     * =1 => Insert erfolgreich
     * ansonsten: Fehler
     */
    public int insert(EclVeranstaltung pEcl) {

        int erg = 0;
        dbBasis.beginTransaction();

        /* neue InterneIdent vergeben */
        erg = dbBasis.getInterneIdentVeranstaltung();
        if (erg < 1) {
            CaBug.drucke("DbVeranstaltung.insert 002");
            dbBasis.endTransaction();
            return (erg);
        }

        pEcl.ident = erg;

        pEcl.mandant = dbBundle.clGlobalVar.mandant;

        try {

            /*Felder Neuanlage füllen*/
            String lSql = "INSERT INTO " + dbBundle.getSchemaMandant() + "tbl_veranstaltung " + "("
                    + "mandant, ident, veranstaltungsnummer, aktiv, kurzText, "
                    + "text1, text2, text3, text4, text5, text6, "
                    + "maximaleAnzahlAnmeldungen, istAnzahlAnmeldungen, istStandardFuerBundesland " + ")" + "VALUES ("
                    + "?, ?, ?, ?, ?, " + "?, ?, ?, ?, ?, ?," + "?, ?, ? " + ")";
            PreparedStatement lPStm = verbindung.prepareStatement(lSql, ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);

            fuellePreparedStatementKomplett(lPStm, 1, pEcl);

            erg = lPStm.executeUpdate();
            lPStm.close();
        } catch (Exception e2) {
            CaBug.drucke("DbVeranstaltung.insert 001");
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

    /**Mandant und Ident müssen übergeben werden.
     * 
     * Return-Wert = Anzahl der gefundenen Sätze (oder Fehlermeldung <0)*/
    public int read(int pIdent) {
        int anzInArray = 0;
        PreparedStatement lPStm = null;

        try {
            String lSql = "SELECT * from " + dbBundle.getSchemaMandant() + "tbl_veranstaltung where " + "mandant=? AND "
                    + "ident=? " + "ORDER BY ident;";
            lPStm = verbindung.prepareStatement(lSql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            lPStm.setInt(1, dbBundle.clGlobalVar.mandant);
            lPStm.setInt(2, pIdent);

            ResultSet lErgebnis = lPStm.executeQuery();
            lErgebnis.last();
            anzInArray = lErgebnis.getRow();
            lErgebnis.beforeFirst();

            ergebnisArray = new EclVeranstaltung[anzInArray];

            int i = 0;
            while (lErgebnis.next() == true) {
                ergebnisArray[i] = this.decodeErgebnis(lErgebnis);
                i++;
            }
            lErgebnis.close();
            lPStm.close();

        } catch (Exception e) {
            CaBug.drucke("DbVeranstaltung.read 003");
            System.err.println(" " + e.getMessage());
            return (-1);
        }
        return (anzInArray);
    }

    public int readAllAktive() {
        int anzInArray = 0;
        PreparedStatement lPStm = null;

        try {
            String lSql = "SELECT * from " + dbBundle.getSchemaMandant() + "tbl_veranstaltung where " + "mandant=? AND "
                    + "aktiv!=0 " + "ORDER BY veranstaltungsnummer;";
            lPStm = verbindung.prepareStatement(lSql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            lPStm.setInt(1, dbBundle.clGlobalVar.mandant);

            ResultSet lErgebnis = lPStm.executeQuery();
            lErgebnis.last();
            anzInArray = lErgebnis.getRow();
            lErgebnis.beforeFirst();

            ergebnisArray = new EclVeranstaltung[anzInArray];

            int i = 0;
            while (lErgebnis.next() == true) {
                ergebnisArray[i] = this.decodeErgebnis(lErgebnis);
                i++;
            }
            lErgebnis.close();
            lPStm.close();

        } catch (Exception e) {
            CaBug.drucke("DbVeranstaltung.read 003");
            System.err.println(" " + e.getMessage());
            return (-1);
        }
        return (anzInArray);
    }

    public int readZuBundesland(int pIdent) {
        int anzInArray = 0;
        PreparedStatement lPStm = null;

        try {
            String lSql = "SELECT * from " + dbBundle.getSchemaMandant() + "tbl_veranstaltung where " + "mandant=? AND "
                    + "istStandardFuerBundesland=? " + "ORDER BY ident;";
            lPStm = verbindung.prepareStatement(lSql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            lPStm.setInt(1, dbBundle.clGlobalVar.mandant);
            lPStm.setInt(2, pIdent);

            ResultSet lErgebnis = lPStm.executeQuery();
            lErgebnis.last();
            anzInArray = lErgebnis.getRow();
            lErgebnis.beforeFirst();

            ergebnisArray = new EclVeranstaltung[anzInArray];

            int i = 0;
            while (lErgebnis.next() == true) {
                ergebnisArray[i] = this.decodeErgebnis(lErgebnis);
                i++;
            }
            lErgebnis.close();
            lPStm.close();

        } catch (Exception e) {
            CaBug.drucke("DbVeranstaltung.read 003");
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
    public int update(EclVeranstaltung pEcl) {

        try {

            String lSql = "UPDATE " + dbBundle.getSchemaMandant() + "tbl_veranstaltung SET "
                    + "mandant=?, ident=?, veranstaltungsnummer=?, aktiv=?, kurzText=?, "
                    + "text1=?, text2=?, text3=?, text4=?, text5=?, text6=?, "
                    + "maximaleAnzahlAnmeldungen=?, istAnzahlAnmeldungen=?, istStandardFuerBundesland=? " + "WHERE "
                    + "mandant=? AND ident=? ";

            PreparedStatement lPStm = verbindung.prepareStatement(lSql);
            fuellePreparedStatementKomplett(lPStm, 1, pEcl);
            lPStm.setInt(anzfelder + 1, pEcl.mandant);
            lPStm.setInt(anzfelder + 2, pEcl.ident);

            int ergebnis = lPStm.executeUpdate();
            lPStm.close();
            if (ergebnis == 0) {
                return (CaFehler.pfXyWurdeVonAnderemBenutzerVeraendert);
            }
        } catch (Exception e1) {
            CaBug.drucke("DbVeranstaltung.update 001");
            System.err.println(" " + e1.getMessage());
            return (-1);
        }

        return (1);
    }

    public int updateReduziere(int pVeranstaltung, int pPersonenzahl) {

        int ergebnis = 0;
        try {

            String lSql = "UPDATE " + dbBundle.getSchemaMandant() + "tbl_veranstaltung SET "
                    + "istAnzahlAnmeldungen=istAnzahlAnmeldungen-? " + "WHERE "
                    + "mandant=? AND ident=? ";

            PreparedStatement lPStm = verbindung.prepareStatement(lSql);
            lPStm.setInt(1, pPersonenzahl);
            lPStm.setInt(2, dbBundle.clGlobalVar.mandant);
            lPStm.setInt(3, pVeranstaltung);

            ergebnis = lPStm.executeUpdate();
            lPStm.close();
            if (ergebnis == 0) {
                return (CaFehler.pfXyWurdeVonAnderemBenutzerVeraendert);
            }
        } catch (Exception e1) {
            CaBug.drucke("DbVeranstaltung.update 001");
            System.err.println(" " + e1.getMessage());
            return (-1);
        }

        return (ergebnis);
    }

    public int updateErhoehe(int pVeranstaltung, int pPersonenzahl) {

        int ergebnis = 0;
        try {

            String lSql = "UPDATE " + dbBundle.getSchemaMandant() + "tbl_veranstaltung SET "
                    + "istAnzahlAnmeldungen=istAnzahlAnmeldungen+? " + "WHERE " + "mandant=? AND ident=? "
                    + "AND istAnzahlAnmeldungen+?<=maximaleAnzahlAnmeldungen";

            PreparedStatement lPStm = verbindung.prepareStatement(lSql);
            lPStm.setInt(1, pPersonenzahl);
            lPStm.setInt(2, dbBundle.clGlobalVar.mandant);
            lPStm.setInt(3, pVeranstaltung);
            lPStm.setInt(4, pPersonenzahl);

            ergebnis = lPStm.executeUpdate();
            lPStm.close();
            if (ergebnis == 0) {
                return (CaFehler.pfXyWurdeVonAnderemBenutzerVeraendert);
            }
        } catch (Exception e1) {
            CaBug.drucke("DbVeranstaltung.update 001");
            System.err.println(" " + e1.getMessage());
            return (-1);
        }

        return (ergebnis);
    }

    /**Return-Werte:
     * pfXyWurdeVonAnderemBenutzerVeraendert
     * -1 => undefinierter Fehler
     * 1 => Löschen erfolgreich
     */
    public int delete(int pIdent) {
        try {

            String sql = "DELETE FROM " + dbBundle.getSchemaMandant()
                    + "tbl_veranstaltung WHERE ident=? AND mandant=? ";

            PreparedStatement pstm1 = verbindung.prepareStatement(sql);
            pstm1.setInt(1, pIdent);
            pstm1.setInt(2, dbBundle.clGlobalVar.mandant);

            int ergebnis1 = pstm1.executeUpdate();
            pstm1.close();
            if (ergebnis1 == 0) {
                return (CaFehler.pfXyWurdeVonAnderemBenutzerVeraendert);
            }
        } catch (Exception e1) {
            CaBug.drucke("DbVeranstaltung.delete 001");
            System.err.println(" " + e1.getMessage());
            return (-1);
        }

        return (1);
    }

}
