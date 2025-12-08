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
import de.meetingapps.meetingportal.meetComEntities.EclAbstimmungMeldung;
import de.meetingapps.meetingportal.meetComKonst.KonstWillenserklaerungWeg;

public class DbAbstimmungMeldung extends DbRootExecute {

    private Connection verbindung = null;
    //	private DbBasis VMcdbBasis=null;
    private DbBundle dbBundle = null;

    /*************************Initialisierung***************************/
    /* Verbindung in lokale Daten eintragen*/
    public DbAbstimmungMeldung(DbBundle datenbankbundle) {
        if (datenbankbundle.dbBasis == null) {
            System.err.println("vmcdbBasis nicht initialisiert");
            return;
        }
        // 		VMcdbBasis=datenbankbundle.dbBasis;
        verbindung = datenbankbundle.dbBasis.verbindung;
        dbBundle = datenbankbundle;
    }

    /*****************Ergebnis-Array für Standard-Abfragen*******************************/
    /**Soll nicht direkt verwendet werden, sondern nur über die Zugriffsfunktionen!
     * Aktuell noch public, da direkter Zugriff über DbMeldungen
     */
    private EclAbstimmungMeldung ergebnisArray[] = null;

    public int anzErgebnisGefunden() {
        if (ergebnisArray == null) {
            return 0;
        }
        return ergebnisArray.length;
    }

    public EclAbstimmungMeldung ergebnisGefunden(int lfd) {
        return ergebnisArray[lfd];
    }

    public int createTable() {
        int rc = 0;
        DbLowLevel lDbLowLevel = new DbLowLevel(dbBundle);
        String hString = "CREATE TABLE " + dbBundle.getSchemaMandant() + "tbl_abstimmungmeldung ( "
                + "`mandant` int(11) NOT NULL, " + "`meldungsIdent` int(11) NOT NULL, "
                + "`stimmen` bigint(20) DEFAULT NULL, " + "`gattung` int(11) DEFAULT NULL, "
                + "`aktiv` int(11) DEFAULT NULL, " + "`erteiltAufWeg` int(11) DEFAULT NULL, "
                + "`zeitstempelraw` bigint(20) DEFAULT NULL, ";
        for (int i = 0; i < 200; i++) {
            hString = hString + "`abgabe" + Integer.toString(i) + "` int(11) DEFAULT NULL, ";
        }
        for (int i = 0; i < 200; i++) {
            hString = hString + "`abstimmungsweg" + Integer.toString(i) + "` int(11) NULL DEFAULT '0', ";
        }
        for (int i = 0; i < 200; i++) {
            hString = hString + "`abstimmungDurchSammelkarte" + Integer.toString(i) + "` int(11) NULL DEFAULT '0', ";
        }
        hString = hString + "`stimmabgabeDirektErfolgt` int(11) NULL DEFAULT '0', PRIMARY KEY (`meldungsIdent`,`mandant`) " + ") ";
        rc = lDbLowLevel.createTable(hString);
        return rc;
    }

    /**************************deleteAll Löschen aller Datensätze  eines Mandanten****************/
    public int deleteAll() {
        int erg = 0;

        /*tbl_abstimmungMeldung*/
        try {

            String sql1 = "DELETE FROM " + dbBundle.getSchemaMandant() + "tbl_abstimmungMeldung where mandant=?;";
            PreparedStatement pstm1 = verbindung.prepareStatement(sql1);
            pstm1.setInt(1, dbBundle.clGlobalVar.mandant);

            erg = 0; /*Falls Duplicate Key, dann wird exception geschmissen und erg wohl nicht gefüllt!*/
            erg = executeUpdate(pstm1);
            pstm1.close();

        } catch (Exception e2) {
            CaBug.drucke("DbAbstimmungMeldung.deleteAll 001");
            System.err.println(" " + e2.getMessage());
            return (erg);
        }

        return 1;
    }

    /**************************setzt aktuelle Mandantennummer bei allen Datensätzen****************/
    public int updateMandant() {
        return dbBundle.dbLowLevel.rawUpdateMandant(dbBundle.getSchemaMandant() + "tbl_abstimmungmeldung");
    }

    /** dekodiert die aktuelle Position aus ergebnis in EclWeisungMeldung und gibt dieses zurück*/
    public EclAbstimmungMeldung decodeErgebnis(ResultSet ergebnis) {
        EclAbstimmungMeldung lAbstimmungMeldung = new EclAbstimmungMeldung();

        try {
            lAbstimmungMeldung.mandant = ergebnis.getInt("am.mandant");
            lAbstimmungMeldung.meldungsIdent = ergebnis.getInt("am.meldungsIdent");
            lAbstimmungMeldung.stimmen = ergebnis.getLong("am.stimmen");
            lAbstimmungMeldung.gattung = ergebnis.getInt("am.gattung");
            lAbstimmungMeldung.aktiv = ergebnis.getInt("am.aktiv");
            lAbstimmungMeldung.erteiltAufWeg = ergebnis.getInt("am.erteiltAufWeg");
            lAbstimmungMeldung.zeitstempelraw = ergebnis.getLong("am.zeitstempelraw");

            for (int i = 0; i < 200; i++) {
                lAbstimmungMeldung.abgabe[i] = ergebnis.getInt("am.abgabe" + Integer.toString(i));
            }
            for (int i = 0; i < 200; i++) {
                lAbstimmungMeldung.abstimmungsweg[i] = ergebnis.getInt("am.abstimmungsweg" + Integer.toString(i));
            }
            for (int i = 0; i < 200; i++) {
                lAbstimmungMeldung.abstimmungDurchSammelkarte[i] = ergebnis.getInt("am.abstimmungDurchSammelkarte" + Integer.toString(i));
            }
            lAbstimmungMeldung.stimmabgabeDirektErfolgt = ergebnis.getInt("am.stimmabgabeDirektErfolgt");

        } catch (Exception e) {
            CaBug.drucke("DbAbstimmungMeldung.decodeErgebnis 001");
            System.err.println(" " + e.getMessage());
        }

        return lAbstimmungMeldung;
    }

    /** Fuellen Prepared Statement mit allen Feldern, beginnend bei offset.
     * Kann sowohl für Insert, als auch für update verwendet werden.
     * 
     * offset= Startposition des ersten Feldes (also z.B. 1)
     */
    private int anzfelder = 608;

    private void fuellePreparedStatementKomplett(PreparedStatement pstm, int offset,
            EclAbstimmungMeldung lAbstimmungMeldung) {

        try {
            pstm.setInt(offset, lAbstimmungMeldung.mandant);
            offset++;
            pstm.setInt(offset, lAbstimmungMeldung.meldungsIdent);
            offset++;
            pstm.setLong(offset, lAbstimmungMeldung.stimmen);
            offset++;
            pstm.setInt(offset, lAbstimmungMeldung.gattung);
            offset++;
            pstm.setInt(offset, lAbstimmungMeldung.aktiv);
            offset++;
            pstm.setInt(offset, lAbstimmungMeldung.erteiltAufWeg);
            offset++;
            pstm.setLong(offset, lAbstimmungMeldung.zeitstempelraw);
            offset++;

            for (int i = 0; i < 200; i++) {
                pstm.setInt(offset, lAbstimmungMeldung.abgabe[i]);
                offset++;
            }
            for (int i = 0; i < 200; i++) {
                pstm.setInt(offset, lAbstimmungMeldung.abstimmungsweg[i]);
                offset++;
            }
            for (int i = 0; i < 200; i++) {
                pstm.setInt(offset, lAbstimmungMeldung.abstimmungDurchSammelkarte[i]);
                offset++;
            }
            pstm.setInt(offset, lAbstimmungMeldung.stimmabgabeDirektErfolgt);
            offset++;

        } catch (SQLException e) {
            CaBug.drucke("DbAbstimmungMeldung.fuellePreparedStatementKomplett 001");
            e.printStackTrace();
        }
    }

    public int insert(EclAbstimmungMeldung lAbstimmungMeldung) {
        int erg = 0;

        lAbstimmungMeldung.mandant = dbBundle.clGlobalVar.mandant;

        try {

            /*Felder Neuanlage füllen*/
            String sql1 = "INSERT INTO " + dbBundle.getSchemaMandant() + "tbl_abstimmungMeldung " + "(" + "mandant,  "
                    + "meldungsIdent, " + "stimmen, gattung, aktiv, erteiltAufWeg, zeitstempelraw ";

            for (int i = 0; i < 200; i++) {
                sql1 = sql1 + ", abgabe" + Integer.toString(i);
            }
            for (int i = 0; i < 200; i++) {
                sql1 = sql1 + ", abstimmungsweg" + Integer.toString(i);
            }
            for (int i = 0; i < 200; i++) {
                sql1 = sql1 + ", abstimmungDurchSammelkarte" + Integer.toString(i);
            }
            sql1 = sql1 + ", stimmabgabeDirektErfolgt";
            
            sql1 = sql1 + ")" + "VALUES (" + "?, ?, ?, ?, ?, ?, ?, ? ";
            for (int i = 0; i < 600; i++) {
                sql1 = sql1 + ", ?";
            }

            sql1 = sql1 + " )";
            PreparedStatement pstm1 = verbindung.prepareStatement(sql1, ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);

            fuellePreparedStatementKomplett(pstm1, 1, lAbstimmungMeldung);

            erg = 0; /*Falls Duplicate Key, dann wird exception geschmissen und erg wohl nicht gefüllt!*/
            //			System.out.println("vor Update");
            erg = executeUpdate(pstm1);
            //			System.out.println("nach Update");
            pstm1.close();
        } catch (Exception e2) {
            CaBug.drucke("DbAbstimmungMeldung.insert 001");
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
            sql = "SELECT am.* from " + dbBundle.getSchemaMandant() + "tbl_abstimmungMeldung am where "
                    + "am.mandant=? AND " + "am.meldungsIdent=? ORDER BY am.meldungsIdent;";

            PreparedStatement pstm1 = verbindung.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
            pstm1.setInt(1, dbBundle.clGlobalVar.mandant);
            pstm1.setInt(2, lIdent);

            ResultSet ergebnis = executeQuery(pstm1);
            ergebnis.last();
            anzInArray = ergebnis.getRow();
            ergebnis.beforeFirst();

            ergebnisArray = new EclAbstimmungMeldung[anzInArray];

            int i = 0;
            while (ergebnis.next() == true) {
                ergebnisArray[i] = this.decodeErgebnis(ergebnis);
                i++;
            }
            ergebnis.close();
            pstm1.close();

        } catch (Exception e) {
            CaBug.drucke("DbAbstimmungMeldung.leseIdent 001");
            System.err.println(" " + e.getMessage());
        }

        return (anzInArray);

    }

    private ResultSet lErgebnis_meldeliste = null;
    private PreparedStatement lPStm_meldeliste = null;

    public int readinit_all(boolean pNurAbgegebene) {

        int anzInArray = 0;

        try {

            String sql = "";
            sql = "SELECT am.* from " + dbBundle.getSchemaMandant() + "tbl_abstimmungMeldung am where "
                    + "am.mandant=? " + "ORDER BY am.meldungsIdent;";

            //			System.out.println(lSql);

            lPStm_meldeliste = verbindung.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
            lPStm_meldeliste.setInt(1, dbBundle.clGlobalVar.mandant);

            lErgebnis_meldeliste = executeQuery(lPStm_meldeliste);
            lErgebnis_meldeliste.last();
            anzInArray = lErgebnis_meldeliste.getRow();
            lErgebnis_meldeliste.beforeFirst();

            ergebnisArray = new EclAbstimmungMeldung[1];

        } catch (Exception e) {
            CaBug.drucke("DbAbstimmungMeldung.readinit_all 003");
            System.err.println(" " + e.getMessage());
            return (-1);
        }
        return (anzInArray);
    }

    /**
     * 
     * @return
     * true=Satz gelesen
     * false=ende erreicht, kein Satz mehr gelesen
     */
    public boolean readnext_all() {
        try {
            if (lErgebnis_meldeliste.next() == true) {

                ergebnisArray[0] = this.decodeErgebnis(lErgebnis_meldeliste);
                return true;
            } else {
                lErgebnis_meldeliste.close();
                lPStm_meldeliste.close();
                return false;
            }

        } catch (SQLException e) {
            CaBug.drucke("DbAbstimmungMeldung.readnext_all 003");
            System.err.println(" " + e.getMessage());
            return false;
        }
    }

    public int update(EclAbstimmungMeldung lAbstimmungMeldung) {

        try {

            String sql = "UPDATE " + dbBundle.getSchemaMandant() + "tbl_abstimmungMeldung SET " + "mandant=?, "
                    + "meldungsIdent=?,  stimmen=?, " + "gattung=?, aktiv=?, erteiltAufWeg=?, zeitstempelraw=? ";
            for (int i = 0; i < 200; i++) {
                sql = sql + ", abgabe" + Integer.toString(i) + "=?";
            }
            for (int i = 0; i < 200; i++) {
                sql = sql + ", abstimmungsweg" + Integer.toString(i) + "=?";
            }
            for (int i = 0; i < 200; i++) {
                sql = sql + ", abstimmungDurchSammelkarte" + Integer.toString(i) + "=?";
            }
            sql = sql + ", stimmabgabeDirektErfolgt=?";
            sql = sql + " WHERE " + "meldungsIdent=? AND " + "mandant=?";

            PreparedStatement pstm1 = verbindung.prepareStatement(sql);
            fuellePreparedStatementKomplett(pstm1, 1, lAbstimmungMeldung);
            pstm1.setInt(anzfelder + 1, lAbstimmungMeldung.meldungsIdent);
            pstm1.setInt(anzfelder + 2, dbBundle.clGlobalVar.mandant);

            int ergebnis1 = executeUpdate(pstm1);
            pstm1.close();
            if (ergebnis1 == 0) {
                return (CaFehler.pfdUnbekannterFehler);
            }
        } catch (Exception e1) {
            CaBug.drucke("DbAbstimmungMeldung.update 001");
            System.err.println(" " + e1.getMessage());
            return (CaFehler.pfdUnbekannterFehler);

        }

        return (1);
    }

    public int update_allPosition(int position) {
        try {
            String sql = "UPDATE " + dbBundle.getSchemaMandant() + "tbl_abstimmungMeldung SET ";
            sql = sql + " abgabe" + Integer.toString(position) + "=0,";
            sql = sql + " abstimmungsweg" + Integer.toString(position) + "=0,";
            sql = sql + " abstimmungDurchSammelkarte" + Integer.toString(position) + "=0";
            sql = sql + " WHERE mandant=?";

            PreparedStatement pstm1 = verbindung.prepareStatement(sql);
            pstm1.setInt(1, dbBundle.clGlobalVar.mandant);

            int ergebnis1 = executeUpdate(pstm1);
            pstm1.close();
            if (ergebnis1 == 0) {
                return (CaFehler.pfdUnbekannterFehler);
            }
        } catch (Exception e1) {
            CaBug.drucke("DbAbstimmungMeldung.update_allPosition 001");
            System.err.println(" " + e1.getMessage());
            return (CaFehler.pfdUnbekannterFehler);
        }

        return (1);
    }

    /**Löscht an Position alle maschinell eingefügten Stimmabgaben. Das sind die Stimmausschlüsse und die
     * ABSTIMMNUNGSERGAENZUNG_
     */
    public int update_allPositionMaschinelleAbgaben(int position) {
        try {
            String sql = "UPDATE " + dbBundle.getSchemaMandant() + "tbl_abstimmungMeldung SET ";
            sql = sql + " abgabe" + Integer.toString(position) + "=0, ";
            sql = sql + " abstimmungsweg" + Integer.toString(position) + "=0, ";
            sql = sql + " abstimmungDurchSammelkarte" + Integer.toString(position) + "=0";
            sql = sql + " WHERE (abstimmungsweg" + Integer.toString(position)+">=? AND"
                    + " abstimmungsweg" + Integer.toString(position)+"<=?) OR"
                    + " abstimmungsweg" + Integer.toString(position)+"=1006 ";

            PreparedStatement pstm1 = verbindung.prepareStatement(sql);
            pstm1.setInt(1, KonstWillenserklaerungWeg.ABSTIMMUNGSERGAENZUNG_BEGINN);
            pstm1.setInt(2, KonstWillenserklaerungWeg.ABSTIMMUNGSERGAENZUNG_ENDE);

            int ergebnis1 = executeUpdate(pstm1);
            pstm1.close();
            if (ergebnis1 == 0) {
                return (CaFehler.pfdUnbekannterFehler);
            }
        } catch (Exception e1) {
            CaBug.drucke("001");
            System.err.println(" " + e1.getMessage());
            return (CaFehler.pfdUnbekannterFehler);
        }

        return (1);
    }

}
