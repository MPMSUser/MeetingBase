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
import de.meetingapps.meetingportal.meetComEntities.EclInstiBestandsZuordnung;

public class DbInstiBestandsZuordnung {

    private Connection verbindung = null;
    private DbBasis dbBasis = null;
    private DbBundle dbBundle = null;

    private EclInstiBestandsZuordnung ergebnisArray[] = null;

    /*************************Initialisierung***************************/
    public DbInstiBestandsZuordnung(DbBundle pDbBundle) {
        /* Verbindung in lokale Daten eintragen*/
        if (pDbBundle == null) {
            CaBug.drucke("DbInstiBestandsZuordnung.init 001 - dbBundle nicht initialisiert");
            return;
        }
        if (pDbBundle.dbBasis == null) {
            CaBug.drucke("DbInstiBestandsZuordnung.init 002 - dbBasis nicht initialisiert");
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
    public EclInstiBestandsZuordnung[] ergebnis() {
        return ergebnisArray;
    }

    /**********Liefert pN-tes Element des Ergebnisses der read*Methoden**************
     * pN geht von 0 bis anzErgebnis-1*/
    public EclInstiBestandsZuordnung ergebnisPosition(int pN) {
        if (ergebnisArray == null) {
            CaBug.drucke("DbInstiBestandsZuordnung.ergebnisPosition 001");
            return null;
        }
        if (pN < 0) {
            CaBug.drucke("DbInstiBestandsZuordnung.ergebnisPosition 002");
            return null;
        }
        if (pN >= ergebnisArray.length) {
            CaBug.drucke("DbInstiBestandsZuordnung.ergebnisPosition 003");
            return null;
        }
        return ergebnisArray[pN];
    }

    public int createTable() {
        int rc = 0;
        DbLowLevel lDbLowLevel = new DbLowLevel(dbBundle);
        rc = lDbLowLevel.createTable("CREATE TABLE " + dbBundle.getSchemaMandant() + "tbl_instiBestandsZuordnung ( "
                + "`mandant` int(11) NOT NULL, " + "`ident` int(11) NOT NULL, "
                + "`beschreibung` varchar(100) DEFAULT NULL, " +

                "`zugeordnetRegisterOderMeldungen` int(11) DEFAULT NULL, "
                + "`identAktienregister` int(11) DEFAULT NULL, " + "`zugeordneteStimmen` int(11) DEFAULT NULL, "
                + "`identMeldung` int(11) DEFAULT NULL, " + "`identInsti` int(11) DEFAULT NULL, "
                + "`identUserLogin` int(11) NOT NULL, " + "`verarbeitetSammelAnmeldungGedruckt` int(11) NOT NULL, "
                + "`verarbeitet2` int(11) NOT NULL, " + "`verarbeitet3` int(11) NOT NULL, "
                + "`verarbeitet4` int(11) NOT NULL, " + "`verarbeitet5` int(11) NOT NULL, "
                + "PRIMARY KEY (`mandant`,`ident`, `identUserLogin`) " + ") ");
        return rc;
    }

    /**************************deleteAll Löschen aller Datensätze  eines Mandanten****************/
    public int deleteAll() {
        dbBundle.dbBasis.getInterneIdentInstiBestandsZuordnung();
        return dbBundle.dbLowLevel.deleteMandant(
                "DELETE FROM " + dbBundle.getSchemaMandant() + "tbl_instiBestandsZuordnung where mandant=?");
    }

    /**************************setzt aktuelle Mandantennummer bei allen Datensätzen****************/
    public int updateMandant() {
        return dbBundle.dbLowLevel.rawUpdateMandant(dbBundle.getSchemaMandant() + "tbl_instiBestandsZuordnung");
    }

    public void reorgInterneIdent() {
        int lMax = dbBundle.dbLowLevel.liefereHoechsteIdent(
                "SELECT MAX(ident) FROM " + dbBundle.getSchemaMandant() + "tbl_instiBestandsZuordnung where mandant=?");
        if (lMax != -1) {
            dbBundle.dbBasis.resetInterneIdentInstiBestandsZuordnung(lMax);
        }
    }

    /********** dekodiert die aktuelle Position aus ergebnis  und gibt dieses zurück******/
    EclInstiBestandsZuordnung decodeErgebnis(ResultSet pErgebnis) {

        EclInstiBestandsZuordnung lEclReturn = new EclInstiBestandsZuordnung();

        try {
            lEclReturn.mandant = pErgebnis.getInt("inbz.mandant");
            lEclReturn.ident = pErgebnis.getInt("inbz.ident");
            lEclReturn.beschreibung = pErgebnis.getString("inbz.beschreibung");

            lEclReturn.zugeordnetRegisterOderMeldungen = pErgebnis.getInt("inbz.zugeordnetRegisterOderMeldungen");
            lEclReturn.identAktienregister = pErgebnis.getInt("inbz.identAktienregister");
            lEclReturn.zugeordneteStimmen = pErgebnis.getLong("inbz.zugeordneteStimmen");
            lEclReturn.identMeldung = pErgebnis.getInt("inbz.identMeldung");
            lEclReturn.identInsti = pErgebnis.getInt("inbz.identInsti");
            lEclReturn.identUserLogin = pErgebnis.getInt("inbz.identUserLogin");
            lEclReturn.verarbeitetSammelAnmeldungGedruckt = pErgebnis.getInt("inbz.verarbeitetSammelAnmeldungGedruckt");
            lEclReturn.verarbeitet2 = pErgebnis.getInt("inbz.verarbeitet2");
            lEclReturn.verarbeitet3 = pErgebnis.getInt("inbz.verarbeitet3");
            lEclReturn.verarbeitet4 = pErgebnis.getInt("inbz.verarbeitet4");
            lEclReturn.verarbeitet5 = pErgebnis.getInt("inbz.verarbeitet5");

        } catch (Exception e) {
            CaBug.drucke("DbInstiBestandsZuordnung.decodeErgebnis 001");
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

    private void fuellePreparedStatementKomplett(PreparedStatement pPStm, int pOffset, EclInstiBestandsZuordnung pEcl) {

        int startOffset = pOffset; /*Nur erforderlich zum Überprüfen von Programmierfehlern - Feldanzahl*/

        try {
            pPStm.setInt(pOffset, pEcl.mandant);
            pOffset++;
            pPStm.setInt(pOffset, pEcl.ident);
            pOffset++;
            pPStm.setString(pOffset, pEcl.beschreibung);
            pOffset++;

            pPStm.setInt(pOffset, pEcl.zugeordnetRegisterOderMeldungen);
            pOffset++;
            pPStm.setInt(pOffset, pEcl.identAktienregister);
            pOffset++;
            pPStm.setLong(pOffset, pEcl.zugeordneteStimmen);
            pOffset++;
            pPStm.setInt(pOffset, pEcl.identMeldung);
            pOffset++;
            pPStm.setInt(pOffset, pEcl.identInsti);
            pOffset++;
            pPStm.setInt(pOffset, pEcl.identUserLogin);
            pOffset++;

            pPStm.setInt(pOffset, pEcl.verarbeitetSammelAnmeldungGedruckt);
            pOffset++;
            pPStm.setInt(pOffset, pEcl.verarbeitet2);
            pOffset++;
            pPStm.setInt(pOffset, pEcl.verarbeitet3);
            pOffset++;
            pPStm.setInt(pOffset, pEcl.verarbeitet4);
            pOffset++;
            pPStm.setInt(pOffset, pEcl.verarbeitet5);
            pOffset++;

            if (pOffset - startOffset != anzfelder) {
                /*Nur wg. Überprüfung auf Programmierfehler - alle Felder berücksichtigt?*/
                CaBug.drucke("DbInstiBestandsZuordnung.fuellePreparedStatementKomplett 002");
            }

        } catch (SQLException e) {
            CaBug.drucke("DbInstiBestandsZuordnung.fuellePreparedStatementKomplett 001");
            e.printStackTrace();
        }

    }

    /**Insert
     * 
     * Returnwert:
     * =1 => Insert erfolgreich
     * ansonsten: Fehler
     */
    public int insert(EclInstiBestandsZuordnung pEcl, boolean pMitIdentVergabe) {

        int erg = 0;
        dbBasis.beginTransaction();

        if (pMitIdentVergabe) {
            /* neue InterneIdent vergeben */
            erg = dbBasis.getInterneIdentInstiBestandsZuordnung();
            if (erg < 1) {
                CaBug.drucke("DbInstiBestandsZuordnung.insert 002");
                dbBasis.endTransaction();
                return (erg);
            }

            pEcl.ident = erg;
        }

        pEcl.mandant = dbBundle.clGlobalVar.mandant;

        try {

            /*Felder Neuanlage füllen*/
            String lSql = "INSERT INTO " + dbBundle.getSchemaMandant() + "tbl_instiBestandsZuordnung " + "("
                    + "mandant, ident, beschreibung, "
                    + "zugeordnetRegisterOderMeldungen, identAktienregister, zugeordneteStimmen, identMeldung, identInsti, identUserLogin, "
                    + "verarbeitetSammelAnmeldungGedruckt, verarbeitet2, verarbeitet3, verarbeitet4, verarbeitet5 "
                    + ")" + "VALUES (" + "?, ?, ?, " + "?, ?, ?, ?, ?, ?, " + "?, ?, ?, ?, ? " + ")";
            PreparedStatement lPStm = verbindung.prepareStatement(lSql, ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);

            fuellePreparedStatementKomplett(lPStm, 1, pEcl);

            erg = lPStm.executeUpdate();
            lPStm.close();
        } catch (Exception e2) {
            CaBug.drucke("DbInstiBestandsZuordnung.insert 001");
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
    public int read(EclInstiBestandsZuordnung pEcl) {
        int anzInArray = 0;
        PreparedStatement lPStm = null;

        try {
            String lSql = "SELECT * from " + dbBundle.getSchemaMandant() + "tbl_instiBestandsZuordnung inbz where "
                    + "inbz.mandant=? AND " + "inbz.ident=? " + "ORDER BY inbz.ident;";
            lPStm = verbindung.prepareStatement(lSql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            lPStm.setInt(1, pEcl.mandant);
            lPStm.setInt(2, pEcl.ident);

            ResultSet lErgebnis = lPStm.executeQuery();
            lErgebnis.last();
            anzInArray = lErgebnis.getRow();
            lErgebnis.beforeFirst();

            ergebnisArray = new EclInstiBestandsZuordnung[anzInArray];

            int i = 0;
            while (lErgebnis.next() == true) {
                ergebnisArray[i] = this.decodeErgebnis(lErgebnis);
                i++;
            }
            lErgebnis.close();
            lPStm.close();

        } catch (Exception e) {
            CaBug.drucke("DbInstiBestandsZuordnung.read 003");
            System.err.println(" " + e.getMessage());
            return (-1);
        }
        return (anzInArray);
    }

    /**
     * 
     * Return-Wert = Anzahl der gefundenen Sätze (oder Fehlermeldung <0)*/
    public int readAktienregisterIdent(int pAktionaersIdent) {
        int anzInArray = 0;
        PreparedStatement lPStm = null;

        try {
            String lSql = "SELECT * from " + dbBundle.getSchemaMandant() + "tbl_instiBestandsZuordnung inbz where "
                    + "inbz.mandant=? AND " + "inbz.identAktienregister=? AND " + "inbz.identUserLogin=0 "
                    + "ORDER BY inbz.ident;";
            lPStm = verbindung.prepareStatement(lSql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            lPStm.setInt(1, dbBundle.clGlobalVar.mandant);
            lPStm.setInt(2, pAktionaersIdent);

            ResultSet lErgebnis = lPStm.executeQuery();
            lErgebnis.last();
            anzInArray = lErgebnis.getRow();
            lErgebnis.beforeFirst();

            ergebnisArray = new EclInstiBestandsZuordnung[anzInArray];

            int i = 0;
            while (lErgebnis.next() == true) {
                ergebnisArray[i] = this.decodeErgebnis(lErgebnis);
                i++;
            }
            lErgebnis.close();
            lPStm.close();

        } catch (Exception e) {
            CaBug.drucke("DbInstiBestandsZuordnung.read 003");
            System.err.println(" " + e.getMessage());
            return (-1);
        }
        return (anzInArray);
    }

    /**
     * 
     * Return-Wert = Anzahl der gefundenen Sätze (oder Fehlermeldung <0)*/
    public int readAlle() {
        int anzInArray = 0;
        PreparedStatement lPStm = null;

        try {
            String lSql = "SELECT * from " + dbBundle.getSchemaMandant() + "tbl_instiBestandsZuordnung inbz where "
                    + "inbz.mandant=? " + "ORDER BY inbz.ident;";
            lPStm = verbindung.prepareStatement(lSql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            lPStm.setInt(1, dbBundle.clGlobalVar.mandant);

            ResultSet lErgebnis = lPStm.executeQuery();
            lErgebnis.last();
            anzInArray = lErgebnis.getRow();
            lErgebnis.beforeFirst();

            ergebnisArray = new EclInstiBestandsZuordnung[anzInArray];

            int i = 0;
            while (lErgebnis.next() == true) {
                ergebnisArray[i] = this.decodeErgebnis(lErgebnis);
                i++;
            }
            lErgebnis.close();
            lPStm.close();

        } catch (Exception e) {
            CaBug.drucke("DbInstiBestandsZuordnung.read 003");
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
    public int update(EclInstiBestandsZuordnung pEcl) {

        try {

            String lSql = "UPDATE " + dbBundle.getSchemaMandant() + "tbl_instiBestandsZuordnung SET "
                    + "mandant=?, ident=?, beschreibung=?, "
                    + "zugeordnetRegisterOderMeldungen=?, identAktienregister=?, zugeordneteStimmen=?, identMeldung=?, identInsti=?, identUserLogin=?, "
                    + "verarbeitetSammelAnmeldungGedruckt=?, verarbeitet2=?, verarbeitet3=?, verarbeitet4=?, verarbeitet5=? "
                    + "WHERE " + "mandant=? AND ident=? AND identUserLogin=? ";

            PreparedStatement lPStm = verbindung.prepareStatement(lSql);
            fuellePreparedStatementKomplett(lPStm, 1, pEcl);
            lPStm.setInt(anzfelder + 1, pEcl.mandant);
            lPStm.setInt(anzfelder + 2, pEcl.ident);
            lPStm.setInt(anzfelder + 3, pEcl.identUserLogin);

            int ergebnis = lPStm.executeUpdate();
            lPStm.close();
            if (ergebnis == 0) {
                return (CaFehler.pfXyWurdeVonAnderemBenutzerVeraendert);
            }
        } catch (Exception e1) {
            CaBug.drucke("DbInstiBestandsZuordnung.update 001");
            System.err.println(" " + e1.getMessage());
            return (-1);
        }

        return (1);
    }

    /**Update aller Aktienregister-Zuordnungen mit "Sammelanmeldebogen erstellt"*/
    public int updateSammelAnmeldebogenErstellt(int pInstiIdent, int drucklaufNr) {
        int anzUpdate = 0;

        try {

            String lSql = "UPDATE " + dbBundle.getSchemaMandant() + "tbl_instiBestandsZuordnung SET "
                    + "verarbeitetSammelAnmeldungGedruckt=? " + "WHERE "
                    + "mandant=? AND identInsti=? AND verarbeitetSammelAnmeldungGedruckt=0 AND zugeordnetRegisterOderMeldungen=1; ";

            PreparedStatement lPStm = verbindung.prepareStatement(lSql);
            lPStm.setInt(1, drucklaufNr);
            lPStm.setInt(2, dbBundle.clGlobalVar.mandant);
            lPStm.setLong(3, pInstiIdent);

            anzUpdate = lPStm.executeUpdate();
            lPStm.close();
        } catch (Exception e1) {
            CaBug.drucke("DbInstiBestandsZuordnung.updateSammelAnmeldebogenErstellt 001");
            System.err.println(" " + e1.getMessage());
            return (-1);
        }

        return (anzUpdate);
    }

    /**Return-Werte:
     * pfXyWurdeVonAnderemBenutzerVeraendert
     * -1 => undefinierter Fehler
     * 1 => Löschen erfolgreich
     */
    public int delete(int pIdent, int pIdentUserLogin) {
        try {

            String sql = "DELETE FROM " + dbBundle.getSchemaMandant()
                    + "tbl_instiBestandsZuordnung WHERE ident=? AND identUserLogin=? AND mandant=? ";

            PreparedStatement pstm1 = verbindung.prepareStatement(sql);
            pstm1.setInt(1, pIdent);
            pstm1.setInt(2, pIdentUserLogin);
            pstm1.setInt(3, dbBundle.clGlobalVar.mandant);

            int ergebnis1 = pstm1.executeUpdate();
            pstm1.close();
            if (ergebnis1 == 0) {
                return (CaFehler.pfXyWurdeVonAnderemBenutzerVeraendert);
            }
        } catch (Exception e1) {
            CaBug.drucke("DbInstiBestandsZuordnung.delete 001");
            System.err.println(" " + e1.getMessage());
            return (-1);
        }

        return (1);
    }

}
