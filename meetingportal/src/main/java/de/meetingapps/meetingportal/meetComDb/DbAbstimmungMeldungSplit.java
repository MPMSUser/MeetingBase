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
import de.meetingapps.meetingportal.meetComEntities.EclAbstimmungMeldungSplit;

public class DbAbstimmungMeldungSplit extends DbRootExecute  {

    private Connection verbindung = null;
    private DbBasis dbBasis = null;
    private DbBundle dbBundle = null;

    /*************************Initialisierung***************************/
    /* Verbindung in lokale Daten eintragen*/
    public DbAbstimmungMeldungSplit(DbBundle datenbankbundle) {
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
    private EclAbstimmungMeldungSplit ergebnisArray[] = null;

    public int anzErgebnisGefunden() {
        if (ergebnisArray == null) {
            return 0;
        }
        return ergebnisArray.length;
    }

    public EclAbstimmungMeldungSplit ergebnisPositionGefunden(int lfd) {
        return ergebnisArray[lfd];
    }

    private String liefereLfdString(int pLfd) {
        String lfd = Integer.toString(pLfd);
        if (pLfd == 0) {
            lfd = "";
        }
        return lfd;
    }

    private int createTableNr(int pLfd) {
        String lfd = liefereLfdString(pLfd);
        int rc = 0;
        DbLowLevel lDbLowLevel = new DbLowLevel(dbBundle);
        String sql1 = "CREATE TABLE " + dbBundle.getSchemaMandant() + "tbl_abstimmungmeldungsplit" + lfd + " ( "
                + "`mandant` int(11) NOT NULL, " + "`meldungsIdent` int(11) NOT NULL, ";
        for (int i = 0 + 50 * pLfd; i < 50 + pLfd * 50; i++) {
            for (int i1 = 0; i1 < 10; i1++) {
                sql1 = sql1 + "abgabe" + Integer.toString(i) + "_" + Integer.toString(i1)
                        + " bigint(20) DEFAULT NULL, ";
            }
        }
        sql1 = sql1 + "PRIMARY KEY (`meldungsIdent`,`mandant`) " + ") ";

        rc = lDbLowLevel.createTable(sql1);
        return rc;

    }

    public int createTable() {
        for (int i = 0; i < 4; i++) {
            int rc = createTableNr(i);
            if (rc < 1) {
                return rc;
            }
        }
        return 1;
    }

    /**************************deleteAll Löschen aller Datensätze  eines Mandanten****************/
    private int deleteAllNr(int pLfd) {
        String lfd = liefereLfdString(pLfd);
        int erg = 0;
        try {
            String sql1 = "DELETE FROM " + dbBundle.getSchemaMandant() + "tbl_abstimmungMeldungSplit" + lfd
                    + " where mandant=?;";
            PreparedStatement pstm1 = verbindung.prepareStatement(sql1);
            pstm1.setInt(1, dbBundle.clGlobalVar.mandant);

            erg = 0; /*Falls Duplicate Key, dann wird exception geschmissen und erg wohl nicht gefüllt!*/
            erg = executeUpdate(pstm1);
            pstm1.close();

        } catch (Exception e2) {
            CaBug.drucke("DbAbstimmungMeldungSplit.deleteAllNr " + pLfd);
            System.err.println(" " + e2.getMessage());
            return (erg);
        }
        return 1;
    }

    public int deleteAll() {
        int erg = 0;
        for (int i = 0; i < 4; i++) {
            erg = deleteAllNr(i);
            if (erg < 1) {
                return erg;
            }
        }
        return 1;
    }

    /**************************setzt aktuelle Mandantennummer bei allen Datensätzen****************/
    public int updateMandant() {
        int erg = 0;
        for (int i = 0; i < 4; i++) {
            erg = dbBundle.dbLowLevel.rawUpdateMandant(dbBundle.getSchemaMandant() + "tbl_abstimmungen");
            if (erg < 1) {
                return erg;
            }
        }
        return 1;
    }

    /** dekodiert die aktuelle Position aus ergebnis in lWeisungMeldungSplit**/
    private void decodeErgebnisSplitNr(int pLfd, ResultSet ergebnis,
            EclAbstimmungMeldungSplit lAbstimmungMeldungSplit) {
        String lfd = liefereLfdString(pLfd);

        try {
            int i, i1;
            for (i = 0 + 50 * pLfd; i < 50 + 50 * pLfd; i++) {
                for (i1 = 0; i1 < 10; i1++) {
                    lAbstimmungMeldungSplit.abgabe[i][i1] = ergebnis
                            .getLong("ams" + lfd + ".abgabe" + Integer.toString(i) + "_" + Integer.toString(i1));
                }
            }
        } catch (Exception e) {
            CaBug.drucke("DbAbstimmungMeldungSplit.decodeErgebnisSplitNr " + lfd + " 001");
            System.err.println(" " + e.getMessage());
        }

        return;
    }

    /** dekodiert die aktuelle Position aus ergebnis in EclWeisungMeldungSplit und gibt dieses zurück*/
    public EclAbstimmungMeldungSplit decodeErgebnisSplit(ResultSet ergebnis) {
        EclAbstimmungMeldungSplit lAbstimmungMeldungSplit = new EclAbstimmungMeldungSplit();

        try {
            lAbstimmungMeldungSplit.mandant = ergebnis.getInt("ams.mandant");
            lAbstimmungMeldungSplit.meldungsIdent = ergebnis.getInt("ams.meldungsIdent");
        } catch (Exception e) {
            CaBug.drucke("DbAbstimmungMeldungSplit.decodeErgebnisSplit 001");
            System.err.println(" " + e.getMessage());
        }
        decodeErgebnisSplitNr(0, ergebnis, lAbstimmungMeldungSplit);
        return lAbstimmungMeldungSplit;
    }

    /** dekodiert die aktuelle Position aus ergebnis in lWeisungMeldungSplit**/
    public void decodeErgebnisSplit1(ResultSet ergebnis, EclAbstimmungMeldungSplit lAbstimmungMeldungSplit) {
        decodeErgebnisSplitNr(1, ergebnis, lAbstimmungMeldungSplit);
        return;
    }

    /** dekodiert die aktuelle Position aus ergebnis in lWeisungMeldungSplit**/
    public void decodeErgebnisSplit2(ResultSet ergebnis, EclAbstimmungMeldungSplit lAbstimmungMeldungSplit) {
        decodeErgebnisSplitNr(2, ergebnis, lAbstimmungMeldungSplit);
        return;
    }

    /** dekodiert die aktuelle Position aus ergebnis in lWeisungMeldungSplit**/
    public void decodeErgebnisSplit3(ResultSet ergebnis, EclAbstimmungMeldungSplit lAbstimmungMeldungSplit) {
        decodeErgebnisSplitNr(3, ergebnis, lAbstimmungMeldungSplit);
        return;
    }

    private void fuellePreparedStatementKomplettSplitNr(int pLfd, PreparedStatement pstm, int offset,
            EclAbstimmungMeldungSplit lAbstimmungMeldungSplit) {

        try {
            pstm.setInt(offset, lAbstimmungMeldungSplit.mandant);
            offset++;
            pstm.setInt(offset, lAbstimmungMeldungSplit.meldungsIdent);
            offset++;

            int i, i1;
            for (i = 0 + 50 * pLfd; i < 50 + 50 * pLfd; i++) {
                for (i1 = 0; i1 < 10; i1++) {
                    pstm.setLong(offset, lAbstimmungMeldungSplit.abgabe[i][i1]);
                    offset++;
                }
            }

        } catch (SQLException e) {
            CaBug.drucke("DbAbstimmungMeldungSplit.fuellePreparedStatementKomplettSplitNr " + pLfd + "001");
            e.printStackTrace();
        }

    }

    private int anzfelderSplit = 502;

    private void fuellePreparedStatementKomplettSplit(PreparedStatement pstm, int offset,
            EclAbstimmungMeldungSplit lAbstimmungMeldungSplit) {
        fuellePreparedStatementKomplettSplitNr(0, pstm, offset, lAbstimmungMeldungSplit);
    }

    private int anzfelderSplit1 = 502;

    private void fuellePreparedStatementKomplettSplit1(PreparedStatement pstm, int offset,
            EclAbstimmungMeldungSplit lAbstimmungMeldungSplit) {
        fuellePreparedStatementKomplettSplitNr(1, pstm, offset, lAbstimmungMeldungSplit);
    }

    private int anzfelderSplit2 = 502;

    private void fuellePreparedStatementKomplettSplit2(PreparedStatement pstm, int offset,
            EclAbstimmungMeldungSplit lAbstimmungMeldungSplit) {
        fuellePreparedStatementKomplettSplitNr(2, pstm, offset, lAbstimmungMeldungSplit);
    }

    private int anzfelderSplit3 = 502;

    private void fuellePreparedStatementKomplettSplit3(PreparedStatement pstm, int offset,
            EclAbstimmungMeldungSplit lAbstimmungMeldungSplit) {
        fuellePreparedStatementKomplettSplitNr(3, pstm, offset, lAbstimmungMeldungSplit);
    }

    private int insertNr(int pLfd, EclAbstimmungMeldungSplit lAbstimmungMeldungSplit) {
        String lfd = liefereLfdString(pLfd);

        int erg = 0;
        String sql1 = "";
        try {
            /*Felder Neuanlage füllen*/
            sql1 = "INSERT INTO " + dbBundle.getSchemaMandant() + "tbl_abstimmungMeldungSplit" + lfd + " " + "("
                    + "mandant, " + "meldungsIdent ";
            for (int i = 0 + 50 * pLfd; i < 50 + pLfd * 50; i++) {
                for (int i1 = 0; i1 < 10; i1++) {
                    sql1 = sql1 + ", abgabe" + Integer.toString(i) + "_" + Integer.toString(i1);
                }
            }

            sql1 = sql1 + ")" + "VALUES (" + "?, ? ";
            for (int i = 0; i < 50; i++) {
                for (int i1 = 0; i1 < 10; i1++) {
                    sql1 = sql1 + ", ?";
                }
            }

            sql1 = sql1 + " )";
            PreparedStatement pstm1 = verbindung.prepareStatement(sql1, ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);

            fuellePreparedStatementKomplettSplitNr(pLfd, pstm1, 1, lAbstimmungMeldungSplit);

            erg = 0; /*Falls Duplicate Key, dann wird exception geschmissen und erg wohl nicht gefüllt!*/
            //				System.out.println("vor Update");
            erg = executeUpdate(pstm1);
            //				System.out.println("nach Update");
            pstm1.close();
        } catch (Exception e2) {
            CaBug.drucke("DbAbstimmungMeldungSplit.insertNr " + lfd + " 004");
            System.out.println(sql1);
            System.err.println(" " + e2.getMessage());
        }

        if (erg == 0) {/*Fehler beim Einfügen - d.h. bereits vorhanden*/
            return (-1);
        }
        return 1;
    }

    public int insert(EclAbstimmungMeldungSplit lAbstimmungMeldungSplit) {
        int erg = 0;
        dbBasis.beginTransaction();

        lAbstimmungMeldungSplit.mandant = dbBundle.clGlobalVar.mandant;

        for (int i = 0; i < 4; i++) {
            erg = insertNr(i, lAbstimmungMeldungSplit);
            if (erg < 1) {
                dbBasis.rollbackTransaction();
                dbBasis.endTransaction();
                return (-1);
            }
        }

        dbBasis.endTransaction();
        return (1);
    }

    private int anzInArraySplit = 0;

    public void leseIdentNr(int pLfd, int lIdent) {
        String lfd = liefereLfdString(pLfd);

        try {

            String sql = "";
            sql = "SELECT ams" + lfd + ".* from " + dbBundle.getSchemaMandant() + "tbl_abstimmungMeldungSplit" + lfd
                    + " ams" + lfd + " where " + "ams" + lfd + ".mandant=? ";
            if (lIdent != -99) {
                sql = sql + "AND ams" + lfd + ".meldungsIdent=? ";
            }
            sql = sql + " ORDER BY ams" + lfd + ".meldungsIdent;";

            PreparedStatement pstm1 = verbindung.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
            pstm1.setInt(1, dbBundle.clGlobalVar.mandant);
            if (lIdent != -99) {
                pstm1.setInt(2, lIdent);
            }

            ResultSet ergebnis = executeQuery(pstm1);
            ergebnis.last();
            anzInArraySplit = ergebnis.getRow();
            ergebnis.beforeFirst();

            if (pLfd == 0) {
                ergebnisArray = new EclAbstimmungMeldungSplit[anzInArraySplit];
            }

            int i = 0;
            while (ergebnis.next() == true) {
                switch (pLfd) {
                case 0: {
                    ergebnisArray[i] = this.decodeErgebnisSplit(ergebnis);
                    break;
                }
                case 1: {
                    this.decodeErgebnisSplit1(ergebnis, ergebnisArray[i]);
                    break;
                }
                case 2: {
                    this.decodeErgebnisSplit2(ergebnis, ergebnisArray[i]);
                    break;
                }
                case 3: {
                    this.decodeErgebnisSplit3(ergebnis, ergebnisArray[i]);
                    break;
                }
                }

                i++;
            }
            ergebnis.close();
            pstm1.close();

        } catch (Exception e) {
            CaBug.drucke("DbAbstimmungMeldungSplit.leseIdentNr " + pLfd + " 004");
            System.err.println(" " + e.getMessage());
        }
    }

    /**lIdent=-99 => es werden alle gelesen*/
    public int leseIdent(int lIdent) {
        anzInArraySplit = 0;

        leseIdentNr(0, lIdent);
        leseIdentNr(1, lIdent);
        leseIdentNr(2, lIdent);
        leseIdentNr(3, lIdent);

        return (anzInArraySplit);
    }

    public int updateNr(int pLfd, EclAbstimmungMeldungSplit lAbstimmungMeldungSplit) {
        String lfd = liefereLfdString(pLfd);

        try {

            /*Split*/
            String sql = "UPDATE " + dbBundle.getSchemaMandant() + "tbl_abstimmungMeldungSplit" + lfd + " SET "
                    + "mandant=?,  " + "meldungsIdent=? ";

            int i, i1;
            for (i = 0 + 50 * pLfd; i < 50 + 50 * pLfd; i++) {
                for (i1 = 0; i1 < 10; i1++) {
                    sql = sql + ", abgabe" + Integer.toString(i) + "_" + Integer.toString(i1) + "=?";
                }
            }

            sql = sql + " WHERE " + "meldungsIdent=? AND " + "mandant=?";

            PreparedStatement pstm1 = verbindung.prepareStatement(sql);
            switch (pLfd) {
            case 0: {
                fuellePreparedStatementKomplettSplit(pstm1, 1, lAbstimmungMeldungSplit);
                pstm1.setInt(anzfelderSplit + 1, lAbstimmungMeldungSplit.meldungsIdent);
                pstm1.setInt(anzfelderSplit + 2, dbBundle.clGlobalVar.mandant);
                break;
            }
            case 1: {
                fuellePreparedStatementKomplettSplit1(pstm1, 1, lAbstimmungMeldungSplit);
                pstm1.setInt(anzfelderSplit1 + 1, lAbstimmungMeldungSplit.meldungsIdent);
                pstm1.setInt(anzfelderSplit1 + 2, dbBundle.clGlobalVar.mandant);
                break;
            }
            case 2: {
                fuellePreparedStatementKomplettSplit2(pstm1, 1, lAbstimmungMeldungSplit);
                pstm1.setInt(anzfelderSplit2 + 1, lAbstimmungMeldungSplit.meldungsIdent);
                pstm1.setInt(anzfelderSplit2 + 2, dbBundle.clGlobalVar.mandant);
                break;
            }
            case 3: {
                fuellePreparedStatementKomplettSplit3(pstm1, 1, lAbstimmungMeldungSplit);
                pstm1.setInt(anzfelderSplit3 + 1, lAbstimmungMeldungSplit.meldungsIdent);
                pstm1.setInt(anzfelderSplit3 + 2, dbBundle.clGlobalVar.mandant);
                break;
            }
            }

            int ergebnis1 = executeUpdate(pstm1);
            pstm1.close();
            if (ergebnis1 == 0) {
                return (CaFehler.pfdUnbekannterFehler);
            }
        } catch (Exception e1) {
            CaBug.drucke("DbAbstimmungMeldungSplit.update 004");
            System.err.println(" " + e1.getMessage());
            return (CaFehler.pfdUnbekannterFehler);

        }
        return 1;
    }

    /**Update eines Abstimmvorschlags. Achtung:  Update muß innerhalb einer Transaktion erfolgen.
     */
    public int update(EclAbstimmungMeldungSplit lAbstimmungMeldungSplit) {

        int erg;
        for (int i = 0; i < 4; i++) {
            erg = updateNr(i, lAbstimmungMeldungSplit);
            if (erg < 1) {
                return erg;
            }
        }
        return (1);
    }

}
