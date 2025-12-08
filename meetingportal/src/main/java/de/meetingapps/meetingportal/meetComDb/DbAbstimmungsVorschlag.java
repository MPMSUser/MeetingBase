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
import de.meetingapps.meetingportal.meetComEntities.EclAbstimmungsVorschlag;

public class DbAbstimmungsVorschlag  extends DbRootExecute {

    private Connection verbindung = null;
    private DbBasis dbBasis = null;
    private DbBundle dbBundle = null;

    /**Soll nicht direkt verwendet werden, sondern nur über die Zugriffsfunktionen!
     * Aktuell noch public, da direkter Zugriff über DbMeldungen
     */
    public EclAbstimmungsVorschlag abstimmungsVorschlagArray[] = null;

    /*************************Initialisierung***************************/
    /* Verbindung in lokale Daten eintragen*/
    public DbAbstimmungsVorschlag(DbBundle datenbankbundle) {
        if (datenbankbundle.dbBasis == null) {
            System.err.println("vmcdbBasis nicht initialisiert");
            return;
        }
        dbBasis = datenbankbundle.dbBasis;
        verbindung = datenbankbundle.dbBasis.verbindung;
        dbBundle = datenbankbundle;
    }

    public int anzAbstimmungsVorschlagGefunden() {
        if (abstimmungsVorschlagArray == null) {
            return 0;
        }
        return abstimmungsVorschlagArray.length;
    }

    public EclAbstimmungsVorschlag abstimmungsVorschlagGefunden(int lfd) {
        return abstimmungsVorschlagArray[lfd];
    }

    public int createTable() {
        int rc = 0;
        DbLowLevel lDbLowLevel = new DbLowLevel(dbBundle);
        String hString = "CREATE TABLE " + dbBundle.getSchemaMandant() + "tbl_abstimmungsvorschlag ( "
                + "`mandant` int(11) NOT NULL, " + "`abstimmungsVorschlagIdent` int(11) NOT NULL, "
                + "`beschreibungKurz` varchar(100) DEFAULT NULL, " + "`beschreibungLang` varchar(1000) DEFAULT NULL, "
                + "`linkExtern` varchar(200) DEFAULT NULL, " + "`sammelIdent` int(11) DEFAULT NULL, "
                + "`gueltigAb` varchar(8) DEFAULT NULL, " + "`gueltigBis` varchar(8) DEFAULT NULL, "
                + "`verweisAufAbstimmungsvorschlag` int(11) DEFAULT NULL, ";
        for (int i = 0; i < 200; i++) {
            hString = hString + "`abgabe" + Integer.toString(i) + "` int(11) DEFAULT NULL, ";
        }
        hString = hString + "PRIMARY KEY (`abstimmungsVorschlagIdent`,`mandant`) " + ") ";
        rc = lDbLowLevel.createTable(hString);

        return rc;
    }

    /**************************deleteAll Löschen aller Datensätze  eines Mandanten****************/
    public int deleteAll() {
        int erg = 0;

        dbBasis.resetInterneIdentAbstimmungsVorschlag();

        try {

            String sql1 = "DELETE FROM " + dbBundle.getSchemaMandant() + "tbl_abstimmungsVorschlag where mandant=?;";
            PreparedStatement pstm1 = verbindung.prepareStatement(sql1);
            pstm1.setInt(1, dbBundle.clGlobalVar.mandant);

            erg = 0; /*Falls Duplicate Key, dann wird exception geschmissen und erg wohl nicht gefüllt!*/
            erg = executeUpdate(pstm1);
            pstm1.close();

        } catch (Exception e2) {
            CaBug.drucke("DbAbstimmungsVorschlag.deleteAll 001");
            System.err.println(" " + e2.getMessage());
            return (erg);
        }

        return 1;
    }

    /**************************setzt aktuelle Mandantennummer bei allen Datensätzen****************/
    public int updateMandant() {
        return dbBundle.dbLowLevel.rawUpdateMandant(dbBundle.getSchemaMandant() + "tbl_abstimmungsVorschlag");
    }

    public void reorgInterneIdent() {
        int lMax;
        lMax = 0;

        PreparedStatement pstm1 = null;
        try {
            int anzInArray;
            String sql = "SELECT MAX(abstimmungsVorschlagIdent) FROM " + dbBundle.getSchemaMandant()
                    + "tbl_abstimmungsvorschlag ab where " + "ab.mandant=? ";
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
            CaBug.drucke("DbAbstimmungen.reorgInterneIdent 001");
            System.err.println(" " + e.getMessage());
        }

        dbBundle.dbBasis.resetInterneIdentAbstimmungsVorschlag(lMax);
    }

    /** dekodiert die aktuelle Position aus ergebnis in EclWillenserklaerung und gibt dieses zurück*/
    private EclAbstimmungsVorschlag decodeErgebnis(ResultSet ergebnis) {
        EclAbstimmungsVorschlag lAbstimmungsVorschlag = new EclAbstimmungsVorschlag();

        try {

            lAbstimmungsVorschlag.mandant = ergebnis.getInt("mandant");
            lAbstimmungsVorschlag.abstimmungsVorschlagIdent = ergebnis.getInt("abstimmungsVorschlagIdent");

            lAbstimmungsVorschlag.beschreibungKurz = ergebnis.getString("beschreibungKurz");
            lAbstimmungsVorschlag.beschreibungLang = ergebnis.getString("beschreibungLang");
            lAbstimmungsVorschlag.linkExtern = ergebnis.getString("linkExtern");

            lAbstimmungsVorschlag.sammelIdent = ergebnis.getInt("sammelIdent");
            lAbstimmungsVorschlag.gueltigAb = ergebnis.getString("gueltigAb");
            lAbstimmungsVorschlag.gueltigBis = ergebnis.getString("gueltigBis");

            lAbstimmungsVorschlag.verweisAufAbstimmungsvorschlag = ergebnis.getInt("verweisAufAbstimmungsvorschlag");

            int i;
            for (i = 0; i < 200; i++) {
                lAbstimmungsVorschlag.abgabe[i] = ergebnis.getInt("abgabe" + Integer.toString(i));
            }

        } catch (Exception e) {
            CaBug.drucke("DbAbstimmungsVorschlag.decodeErgebnis 001");
            System.err.println(" " + e.getMessage());
        }

        return lAbstimmungsVorschlag;
    }

    /** Fuellen Prepared Statement mit allen Feldern, beginnend bei offset.
     * Kann sowohl für Insert, als auch für update verwendet werden.
     * 
     * offset= Startposition des ersten Feldes (also z.B. 1)
     */
    private int anzfelder = 209;

    private void fuellePreparedStatementKomplett(PreparedStatement pstm, int offset,
            EclAbstimmungsVorschlag lAbstimmungsVorschlag) {

        try {
            pstm.setInt(offset, lAbstimmungsVorschlag.mandant);
            offset++;
            pstm.setInt(offset, lAbstimmungsVorschlag.abstimmungsVorschlagIdent);
            offset++;

            pstm.setString(offset, lAbstimmungsVorschlag.beschreibungKurz);
            offset++;
            pstm.setString(offset, lAbstimmungsVorschlag.beschreibungLang);
            offset++;
            pstm.setString(offset, lAbstimmungsVorschlag.linkExtern);
            offset++;

            pstm.setInt(offset, lAbstimmungsVorschlag.sammelIdent);
            offset++;
            pstm.setString(offset, lAbstimmungsVorschlag.gueltigAb);
            offset++;
            pstm.setString(offset, lAbstimmungsVorschlag.gueltigBis);
            offset++;

            pstm.setInt(offset, lAbstimmungsVorschlag.verweisAufAbstimmungsvorschlag);
            offset++;

            int i;
            for (i = 0; i < 200; i++) {
                pstm.setInt(offset, lAbstimmungsVorschlag.abgabe[i]);
                offset++;
            }

        } catch (SQLException e) {
            CaBug.drucke("DbAbstimmungsVorschlag.fuellePreparedStatementKomplett 001");
            e.printStackTrace();
        }

    }

    public int insert(EclAbstimmungsVorschlag lAbstimmungsVorschlag) {

        int erg;

        /* Start Transaktion */
        dbBasis.beginTransaction();

        /* neue InterneIdent vergeben */
        erg = dbBasis.getInterneIdentAbstimmungsVorschlag();
        if (erg < 1) {
            CaBug.drucke("DbAbstimmungsVorschlag.insert 002");
            dbBasis.endTransaction();
            return (erg);
        }
        lAbstimmungsVorschlag.abstimmungsVorschlagIdent = erg;

        lAbstimmungsVorschlag.mandant = dbBundle.clGlobalVar.mandant;

        /* VclMeldung einfügen */
        /* Verarbeitungshinweise: 
         * 	>	nachdem InterneIdent immer eindeutig vergeben werden, ist prinzipiell eine "Doppeleinfügung"
         * 		von InterneIdent nicht möglich
         */

        try {

            /*Felder Neuanlage füllen*/
            String sql1 = "INSERT INTO " + dbBundle.getSchemaMandant() + "tbl_abstimmungsVorschlag " + "("
                    + "mandant, abstimmungsVorschlagIdent, " + "beschreibungKurz, beschreibungLang, linkExtern, "
                    + "sammelIdent, " + "gueltigAb, gueltigBis, " + "verweisAufAbstimmungsvorschlag, ";

            int i;
            for (i = 0; i < 200; i++) {
                sql1 = sql1 + " abgabe" + Integer.toString(i);
                if (i != 199) {
                    sql1 = sql1 + ",";
                }
            }
            sql1 = sql1 + ")" + "VALUES (" + "?, ?, " + "?, ?, ?, " + "?, " + "?, ?, " + "?, ";
            for (i = 0; i < 200; i++) {
                sql1 = sql1 + " ?";
                if (i != 199) {
                    sql1 = sql1 + ",";
                }
            }

            sql1 = sql1 + " )";
            PreparedStatement pstm1 = verbindung.prepareStatement(sql1, ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);

            fuellePreparedStatementKomplett(pstm1, 1, lAbstimmungsVorschlag);

            erg = 0; /*Falls Duplicate Key, dann wird exception geschmissen und erg wohl nicht gefüllt!*/
            //			System.out.println("vor Update");
            erg = executeUpdate(pstm1);
            //			System.out.println("nach Update");
            pstm1.close();
        } catch (Exception e2) {
            CaBug.drucke("DbAbstimmungsVorschlag.insert 001");
            System.err.println(" " + e2.getMessage());
        }

        if (erg == 0) {/*Fehler beim Einfügen - d.h. ZutrittsIdent bereits vorhanden*/
            dbBasis.endTransaction();
            return (-1);
        }

        /* Ende Transaktion */
        dbBasis.endTransaction();

        return (1);
    }

    public int leseZuSammelIdent(int lSammelIdent) {
        int anzInArray = 0;
        try {

            String sql = "SELECT * from " + dbBundle.getSchemaMandant() + "tbl_abstimmungsVorschlag where "
                    + "mandant=? AND " + "sammelIdent=? ORDER BY abstimmungsVorschlagIdent;";

            PreparedStatement pstm1 = verbindung.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
            pstm1.setInt(1, dbBundle.clGlobalVar.mandant);
            pstm1.setInt(2, lSammelIdent);

            ResultSet ergebnis = executeQuery(pstm1);
            ergebnis.last();
            anzInArray = ergebnis.getRow();
            ergebnis.beforeFirst();

            abstimmungsVorschlagArray = new EclAbstimmungsVorschlag[anzInArray];

            int i = 0;
            while (ergebnis.next() == true) {
                abstimmungsVorschlagArray[i] = this.decodeErgebnis(ergebnis);
                i++;
            }
            ergebnis.close();
            pstm1.close();

        } catch (Exception e) {
            CaBug.drucke("leseZuMeldung.leseZuMeldung 001");
            System.err.println(" " + e.getMessage());
        }

        return (anzInArray);

    }

    public int lese(int lIdent) {
        int anzInArray = 0;
        try {

            String sql = "SELECT * from " + dbBundle.getSchemaMandant() + "tbl_abstimmungsVorschlag where "
                    + "mandant=? AND " + "abstimmungsVorschlagIdent=? ORDER BY abstimmungsVorschlagIdent;";

            PreparedStatement pstm1 = verbindung.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
            pstm1.setInt(1, dbBundle.clGlobalVar.mandant);
            pstm1.setInt(2, lIdent);

            ResultSet ergebnis = executeQuery(pstm1);
            ergebnis.last();
            anzInArray = ergebnis.getRow();
            ergebnis.beforeFirst();

            abstimmungsVorschlagArray = new EclAbstimmungsVorschlag[anzInArray];

            int i = 0;
            while (ergebnis.next() == true) {
                abstimmungsVorschlagArray[i] = this.decodeErgebnis(ergebnis);
                i++;
            }
            ergebnis.close();
            pstm1.close();

        } catch (Exception e) {
            CaBug.drucke("DbAbstimmungsVorschlag.lese 001");
            System.err.println(" " + e.getMessage());
        }

        return (anzInArray);

    }

    /**Update eines Abstimmvorschlags. Achtung: Update ist nur zulässig zum Eintrag eines
     * "gueltigBis"-Datums. Update muß innerhalb einer Transaktion erfolgen.
     */
    public int update(EclAbstimmungsVorschlag lAbstimmungsVorschlag) {

        try {

            String sql = "UPDATE " + dbBundle.getSchemaMandant() + "tbl_abstimmungsVorschlag SET "
                    + "mandant=?, abstimmungsVorschlagIdent=?, "
                    + "beschreibungKurz=?, beschreibungLang=?, linkExtern=?, " + "sammelIdent=?, "
                    + "gueltigAb=?, gueltigBis=?, verweisAufAbstimmungsvorschlag=?";
            int i;
            for (i = 0; i < 200; i++) {
                sql = sql + ", abgabe" + Integer.toString(i) + "=?";
            }
            sql = sql + " WHERE " + "abstimmungsVorschlagIdent=? AND " + "mandant=?";

            PreparedStatement pstm1 = verbindung.prepareStatement(sql);
            fuellePreparedStatementKomplett(pstm1, 1, lAbstimmungsVorschlag);
            pstm1.setInt(anzfelder + 1, lAbstimmungsVorschlag.abstimmungsVorschlagIdent);
            pstm1.setLong(anzfelder + 2, dbBundle.clGlobalVar.mandant);

            int ergebnis1 = executeUpdate(pstm1);
            pstm1.close();
            if (ergebnis1 == 0) {
                return (CaFehler.pfdUnbekannterFehler);
            }
        } catch (Exception e1) {
            CaBug.drucke("DbAbstimmungsVorschlag.update 001");
            System.err.println(" " + e1.getMessage());
            return (CaFehler.pfdUnbekannterFehler);

        }

        return (1);
    }

}
