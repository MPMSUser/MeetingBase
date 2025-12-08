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
import de.meetingapps.meetingportal.meetComEntities.EclAbstimmungMeldungRaw;

public class DbAbstimmungMeldungRaw  extends DbRootExecute {

    private Connection verbindung = null;
    private DbBasis dbBasis = null;
    private DbBundle dbBundle = null;

    /*************************Initialisierung***************************/
    /* Verbindung in lokale Daten eintragen*/
    public DbAbstimmungMeldungRaw(DbBundle datenbankbundle) {
        if (datenbankbundle.dbBasis == null) {
            System.err.println("vmcdbBasis nicht initialisiert");
            return;
        }
        dbBasis = datenbankbundle.dbBasis;
        verbindung = datenbankbundle.dbBasis.verbindung;
        dbBundle = datenbankbundle;
    }

    /*****************Ergebnis-Array für Standard-Abfragen*******************************/
    /**Soll nicht direkt verwendet werden, sondern nur über die Zugriffsfunktionen!
     * Aktuell noch public, da direkter Zugriff über DbMeldungen
     */
    public EclAbstimmungMeldungRaw ergebnisArray[] = null;

    public int anzErgebnisGefunden() {
        if (ergebnisArray == null) {
            return 0;
        }
        return ergebnisArray.length;
    }

    public EclAbstimmungMeldungRaw ergebnisGefunden(int lfd) {
        return ergebnisArray[lfd];
    }

    public int createTable() {
        int rc = 0;
        DbLowLevel lDbLowLevel = new DbLowLevel(dbBundle);
        String hString = "CREATE TABLE " + dbBundle.getSchemaMandant() + "tbl_abstimmungmeldungraw ( "
                + "`mandant` int(11) NOT NULL, " + "`ident` int(11) NOT NULL, "
                + "`meldungsIdent` int(11) DEFAULT NULL, " + "`wurdeStorniert` int(11) DEFAULT NULL, "
                + "`erteiltAufWeg` int(11) DEFAULT NULL, " + "`verwendeteKartenklasse` int(11) DEFAULT NULL, "
                + "`verwendeteNummer` char(20) DEFAULT NULL, " + "`wurdeVerarbeitet` int(11) DEFAULT NULL, "
                + "`fehlercode` int(11) DEFAULT NULL, " + "`abstimmungsblock` int(11) DEFAULT NULL, "
                + "`arbeitsplatzNr` int(11) DEFAULT NULL, " + "`zeitstempelraw` bigint(20) DEFAULT NULL, "
                + "`zeitstempel` char(20) DEFAULT NULL, ";

        for (int i = 0; i < 200; i++) {
            hString = hString + "`abgabe" + Integer.toString(i) + "` int(11) DEFAULT NULL, ";
        }
        hString = hString + "PRIMARY KEY (`ident`,`mandant`) " + ") ";

        rc = lDbLowLevel.createTable(hString);
        return rc;
    }

    /**************************deleteAll Löschen aller Datensätze  eines Mandanten****************/
    public int deleteAll() {
        int erg = 0;

        dbBasis.resetInterneIdentAbstimmungMeldungRaw();

        /*tbl_abstimmungMeldungRaw*/
        try {

            String sql1 = "DELETE FROM " + dbBundle.getSchemaMandant() + "tbl_abstimmungMeldungRaw where mandant=?;";
            PreparedStatement pstm1 = verbindung.prepareStatement(sql1);
            pstm1.setInt(1, dbBundle.clGlobalVar.mandant);

            erg = 0; /*Falls Duplicate Key, dann wird exception geschmissen und erg wohl nicht gefüllt!*/
            erg = executeUpdate(pstm1);
            pstm1.close();

        } catch (Exception e2) {
            CaBug.drucke("DbAbstimmungMeldungRaw.deleteAll 001");
            System.err.println(" " + e2.getMessage());
            return (erg);
        }

        return 1;
    }

    /**************************setzt aktuelle Mandantennummer bei allen Datensätzen****************/
    public int updateMandant() {
        return dbBundle.dbLowLevel.rawUpdateMandant(dbBundle.getSchemaMandant() + "tbl_abstimmungMeldungRaw");
    }

    /**************************deleteAll Löschen aller Datensätze  eines Mandanten für einen bestimmten Abstimmungsblock****************/
    public int deleteFuerAbstimmungsblock(int abstimmungsblock) {
        int erg = 0;

        /*tbl_weisungMeldung*/
        try {

            String sql1 = "DELETE FROM " + dbBundle.getSchemaMandant()
                    + "tbl_abstimmungMeldungRaw where mandant=? AND abstimmungsblock=?;";
            PreparedStatement pstm1 = verbindung.prepareStatement(sql1);
            pstm1.setInt(1, dbBundle.clGlobalVar.mandant);
            pstm1.setInt(2, abstimmungsblock);

            erg = 0; /*Falls Duplicate Key, dann wird exception geschmissen und erg wohl nicht gefüllt!*/
            erg = executeUpdate(pstm1);
            pstm1.close();

        } catch (Exception e2) {
            CaBug.drucke("DbAbstimmungMeldungRaw.deleteAll 001");
            System.err.println(" " + e2.getMessage());
            return (erg);
        }

        return 1;
    }

    public void reorgInterneIdent() {
        int lMax;
        lMax = 0;

        PreparedStatement pstm1 = null;
        try {
            int anzInArray;
            String sql = "SELECT MAX(ident) FROM " + dbBundle.getSchemaMandant() + "tbl_abstimmungMeldungRaw where "
                    + "mandant=? ";
            pstm1 = verbindung.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            pstm1.setInt(1, dbBundle.clGlobalVar.mandant);

            ResultSet ergebnis = executeQuery(pstm1);
            ergebnis.last();
            anzInArray = ergebnis.getRow();
            ergebnis.beforeFirst();

            if (anzInArray > 0) {
                ergebnis.next();
                lMax = ergebnis.getInt(1);
            }
            ergebnis.close();
            pstm1.close();

        } catch (Exception e) {
            CaBug.drucke("DbAbstimmungMeldungRaw.reorgInterneIdent 001");
            System.err.println(" " + e.getMessage());
        }

        dbBasis.resetInterneIdentAbstimmungMeldungRaw(lMax);

    }

    /** dekodiert die aktuelle Position aus ergebnis in EclWeisungMeldung und gibt dieses zurück*/
    public EclAbstimmungMeldungRaw decodeErgebnis(ResultSet ergebnis) {
        EclAbstimmungMeldungRaw lAbstimmungMeldungRaw = new EclAbstimmungMeldungRaw();

        try {
            lAbstimmungMeldungRaw.mandant = ergebnis.getInt("amr.mandant");
            lAbstimmungMeldungRaw.ident = ergebnis.getInt("amr.ident");
            lAbstimmungMeldungRaw.meldungsIdent = ergebnis.getInt("amr.meldungsIdent");
            lAbstimmungMeldungRaw.wurdeStorniert = ergebnis.getInt("amr.wurdeStorniert");
            lAbstimmungMeldungRaw.erteiltAufWeg = ergebnis.getInt("amr.erteiltAufWeg");
            lAbstimmungMeldungRaw.verwendeteKartenklasse = ergebnis.getInt("amr.verwendeteKartenklasse");
            lAbstimmungMeldungRaw.verwendeteNummer = ergebnis.getString("amr.verwendeteNummer");
            lAbstimmungMeldungRaw.wurdeVerarbeitet = ergebnis.getInt("amr.wurdeVerarbeitet");
            lAbstimmungMeldungRaw.fehlercode = ergebnis.getInt("amr.fehlercode");
            lAbstimmungMeldungRaw.abstimmungsblock = ergebnis.getInt("amr.abstimmungsblock");
            lAbstimmungMeldungRaw.arbeitsplatzNr = ergebnis.getInt("amr.arbeitsplatzNr");
            lAbstimmungMeldungRaw.zeitstempelraw = ergebnis.getLong("amr.zeitstempelraw");
            lAbstimmungMeldungRaw.zeitstempel = ergebnis.getString("amr.zeitstempel");

            int i;
            for (i = 0; i < 200; i++) {
                lAbstimmungMeldungRaw.abgabe[i] = ergebnis.getInt("amr.abgabe" + Integer.toString(i));
            }

        } catch (Exception e) {
            CaBug.drucke("DbAbstimmungMeldungRaw.decodeErgebnis 001");
            System.err.println(" " + e.getMessage());
        }

        return lAbstimmungMeldungRaw;
    }

    /** Fuellen Prepared Statement mit allen Feldern, beginnend bei offset.
     * Kann sowohl für Insert, als auch für update verwendet werden.
     * 
     * offset= Startposition des ersten Feldes (also z.B. 1)
     */
    //	private int anzfelder=213;
    private void fuellePreparedStatementKomplett(PreparedStatement pstm, int offset,
            EclAbstimmungMeldungRaw lAbstimmungMeldungRaw) {

        try {
            pstm.setInt(offset, lAbstimmungMeldungRaw.mandant);
            offset++;
            pstm.setInt(offset, lAbstimmungMeldungRaw.ident);
            offset++;
            pstm.setInt(offset, lAbstimmungMeldungRaw.meldungsIdent);
            offset++;
            pstm.setInt(offset, lAbstimmungMeldungRaw.wurdeStorniert);
            offset++;
            pstm.setInt(offset, lAbstimmungMeldungRaw.erteiltAufWeg);
            offset++;
            pstm.setInt(offset, lAbstimmungMeldungRaw.verwendeteKartenklasse);
            offset++;
            pstm.setString(offset, lAbstimmungMeldungRaw.verwendeteNummer);
            offset++;
            pstm.setInt(offset, lAbstimmungMeldungRaw.wurdeVerarbeitet);
            offset++;
            pstm.setInt(offset, lAbstimmungMeldungRaw.fehlercode);
            offset++;
            pstm.setInt(offset, lAbstimmungMeldungRaw.abstimmungsblock);
            offset++;
            pstm.setInt(offset, lAbstimmungMeldungRaw.arbeitsplatzNr);
            offset++;
            pstm.setLong(offset, lAbstimmungMeldungRaw.zeitstempelraw);
            offset++;
            pstm.setString(offset, lAbstimmungMeldungRaw.zeitstempel);
            offset++;

            int i;
            for (i = 0; i < 200; i++) {
                pstm.setInt(offset, lAbstimmungMeldungRaw.abgabe[i]);
                offset++;
            }

        } catch (SQLException e) {
            CaBug.drucke("DbAbstimmungMeldungRaw.fuellePreparedStatementKomplett 001");
            e.printStackTrace();
        }
    }

    public int insert(EclAbstimmungMeldungRaw lAbstimmungMeldungRaw) {
        int erg = 0;
        int satznr = 0;

        lAbstimmungMeldungRaw.mandant = dbBundle.clGlobalVar.mandant;

        /* neue InterneIdent vergeben */
        satznr = dbBasis.getInterneIdentAbstimmungMeldungRaw();
        if (satznr < 1) {
            CaBug.drucke("DbAbstimmungMeldungRaw.insert 002");
            dbBasis.endTransaction();
            return (erg);
        }

        lAbstimmungMeldungRaw.ident = satznr;

        try {

            /*Felder Neuanlage füllen*/
            String sql1 = "INSERT INTO " + dbBundle.getSchemaMandant() + "tbl_abstimmungMeldungRaw " + "("
                    + "mandant,  ident, "
                    + "meldungsIdent, wurdeStorniert, erteiltAufWeg, verwendeteKartenklasse, verwendeteNummer, wurdeVerarbeitet, fehlercode, abstimmungsblock, arbeitsplatzNr, "
                    + "zeitstempelraw, zeitstempel ";

            int i;
            for (i = 0; i < 200; i++) {
                sql1 = sql1 + ", abgabe" + Integer.toString(i);
            }
            sql1 = sql1 + ")" + "VALUES (" + "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? ";
            for (i = 0; i < 200; i++) {
                sql1 = sql1 + ", ?";
            }

            sql1 = sql1 + " )";
            PreparedStatement pstm1 = verbindung.prepareStatement(sql1, ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);

            fuellePreparedStatementKomplett(pstm1, 1, lAbstimmungMeldungRaw);

            erg = 0; /*Falls Duplicate Key, dann wird exception geschmissen und erg wohl nicht gefüllt!*/
            //			System.out.println("vor Update");
            erg = executeUpdate(pstm1);
            //			System.out.println("nach Update");
            pstm1.close();
        } catch (Exception e2) {
            CaBug.drucke("DbAbstimmungMeldungRaw.insert 001");
            System.err.println(" " + e2.getMessage());
        }

        if (erg == 0) {/*Fehler beim Einfügen - d.h. bereits vorhanden*/
            return (-1);
        }

        return (1);
    }

    public int lese_RawIdent(int lIdent) {
        int anzInArray = 0;
        try {

            String sql = "";
            sql = "SELECT amr.* from " + dbBundle.getSchemaMandant() + "tbl_abstimmungMeldungRaw amr where "
                    + "amr.mandant=? AND amr.ident=? ";
            sql = sql + "ORDER BY amr.ident;";

            PreparedStatement pstm1 = verbindung.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
            pstm1.setInt(1, dbBundle.clGlobalVar.mandant);
            pstm1.setInt(2, lIdent);

            ResultSet ergebnis = executeQuery(pstm1);
            ergebnis.last();
            anzInArray = ergebnis.getRow();
            ergebnis.beforeFirst();

            ergebnisArray = new EclAbstimmungMeldungRaw[anzInArray];

            int i = 0;
            while (ergebnis.next() == true) {
                ergebnisArray[i] = this.decodeErgebnis(ergebnis);
                i++;
            }
            ergebnis.close();
            pstm1.close();

        } catch (Exception e) {
            CaBug.drucke("DbAbstimmungMeldungRaw.leseIdent 001");
            System.err.println(" " + e.getMessage());
        }
        return (anzInArray);
    }

    public int lese_MeldungsIdent(int lIdent) {
        return lese_MeldungsIdent(lIdent, -1);
    }

    public int lese_MeldungsIdent(int lIdent, int lAbstimmungsblock) {
        int anzInArray = 0;
        try {

            String sql = "";
            sql = "SELECT amr.* from " + dbBundle.getSchemaMandant() + "tbl_abstimmungMeldungRaw amr where "
                    + "amr.mandant=? AND amr.meldungsIdent=? ";
            if (lAbstimmungsblock != -1) {
                sql = sql + "AND amr.abstimmungsblock=? ";
            }
            sql = sql + "ORDER BY amr.ident;";

            PreparedStatement pstm1 = verbindung.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
            pstm1.setInt(1, dbBundle.clGlobalVar.mandant);
            pstm1.setInt(2, lIdent);
            if (lAbstimmungsblock != -1) {
                pstm1.setInt(3, lAbstimmungsblock);
            }

            ResultSet ergebnis = executeQuery(pstm1);
            ergebnis.last();
            anzInArray = ergebnis.getRow();
            ergebnis.beforeFirst();

            ergebnisArray = new EclAbstimmungMeldungRaw[anzInArray];

            int i = 0;
            while (ergebnis.next() == true) {
                ergebnisArray[i] = this.decodeErgebnis(ergebnis);
                i++;
            }
            ergebnis.close();
            pstm1.close();

        } catch (Exception e) {
            CaBug.drucke("DbAbstimmungMeldungRaw.leseIdent 001");
            System.err.println(" " + e.getMessage());
        }
        return (anzInArray);
    }

    /**Liest alle mit wurdeVerarbeitet==0 oder fehlercode<0) */
    public int lese_allNichtVerarbeitet(int pAbstimmungsblock) {
        int anzInArray = 0;
        try {

            String sql = "";
            sql = "SELECT amr.* from " + dbBundle.getSchemaMandant() + "tbl_abstimmungMeldungRaw amr where "
                    + "amr.mandant=? AND "
                    + "amr.abstimmungsblock=? AND (amr.wurdeVerarbeitet=0 OR amr.fehlercode<0) ORDER BY amr.ident;";

            PreparedStatement pstm1 = verbindung.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
            pstm1.setInt(1, dbBundle.clGlobalVar.mandant);
            pstm1.setInt(2, pAbstimmungsblock);

            ResultSet ergebnis = executeQuery(pstm1);
            ergebnis.last();
            anzInArray = ergebnis.getRow();
            ergebnis.beforeFirst();

            ergebnisArray = new EclAbstimmungMeldungRaw[anzInArray];

            int i = 0;
            while (ergebnis.next() == true) {
                ergebnisArray[i] = this.decodeErgebnis(ergebnis);
                i++;
            }
            ergebnis.close();
            pstm1.close();

        } catch (Exception e) {
            CaBug.drucke("DbAbstimmungMeldungRaw.lese_allNichtVerarbeitet 001");
            System.err.println(" " + e.getMessage());
        }

        return (anzInArray);

    }

    public int updateStorno(EclAbstimmungMeldungRaw lAbstimmungMeldungRaw) {

        try {

            String sql = "UPDATE " + dbBundle.getSchemaMandant() + "tbl_abstimmungMeldungRaw SET "
                    + "wurdeStorniert=? ";
            sql = sql + " WHERE " + "ident=? AND " + "mandant=?";

            PreparedStatement pstm1 = verbindung.prepareStatement(sql);
            pstm1.setInt(1, lAbstimmungMeldungRaw.wurdeStorniert);
            pstm1.setInt(2, lAbstimmungMeldungRaw.ident);
            pstm1.setInt(3, lAbstimmungMeldungRaw.mandant);

            int ergebnis1 = executeUpdate(pstm1);
            pstm1.close();
            if (ergebnis1 == 0) {
                return (CaFehler.pfdUnbekannterFehler);
            }
        } catch (Exception e1) {
            CaBug.drucke("DbAbstimmungMeldungRaw.updateStorno 001");
            System.err.println(" " + e1.getMessage());
            return (CaFehler.pfdUnbekannterFehler);

        }

        return (1);
    }

    public int updateVerwendetFehlerCodeMeldungsIdent(EclAbstimmungMeldungRaw lAbstimmungMeldungRaw) {
        try {

            String sql = "UPDATE " + dbBundle.getSchemaMandant() + "tbl_abstimmungMeldungRaw SET "
                    + "meldungsIdent=?, wurdeVerarbeitet=?, fehlercode=? ";
            sql = sql + " WHERE " + "ident=? AND " + "mandant=?";

            PreparedStatement pstm1 = verbindung.prepareStatement(sql);
            pstm1.setInt(1, lAbstimmungMeldungRaw.meldungsIdent);
            pstm1.setInt(2, lAbstimmungMeldungRaw.wurdeVerarbeitet);
            pstm1.setInt(3, lAbstimmungMeldungRaw.fehlercode);
            pstm1.setInt(4, lAbstimmungMeldungRaw.ident);
            pstm1.setInt(5, lAbstimmungMeldungRaw.mandant);

            int ergebnis1 = executeUpdate(pstm1);
            pstm1.close();
            if (ergebnis1 == 0) {
                return (CaFehler.pfdUnbekannterFehler);
            }
        } catch (Exception e1) {
            CaBug.drucke("DbAbstimmungMeldungRaw.updateVerwendetFehlerCodeMeldungsIdent 001");
            System.err.println(" " + e1.getMessage());
            return (CaFehler.pfdUnbekannterFehler);

        }

        return (1);
    }

}
