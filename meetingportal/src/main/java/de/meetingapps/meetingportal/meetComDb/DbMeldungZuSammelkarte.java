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
import de.meetingapps.meetingportal.meetComEntities.EclMeldung;
import de.meetingapps.meetingportal.meetComEntities.EclMeldungZuSammelkarte;

public class DbMeldungZuSammelkarte {

    private Connection verbindung = null;
    //	private DbBasis dbBasis=null;
    private DbBundle dbBundle = null;

    /**Soll nicht direkt verwendet werden, sondern nur über die Zugriffsfunktionen!
     */
    public EclMeldungZuSammelkarte meldungZuSammelkarteArray[] = null;

    /*************************Initialisierung***************************/
    /* Verbindung in lokale Daten eintragen*/
    public DbMeldungZuSammelkarte(DbBundle datenbankbundle) {
        if (datenbankbundle.dbBasis == null) {
            System.err.println("vmcdbBasis nicht initialisiert");
            return;
        }
        // 		dbBasis=datenbankbundle.dbBasis;
        verbindung = datenbankbundle.dbBasis.verbindung;
        dbBundle = datenbankbundle;
    }

    public int anzMeldungZuSammelkarteGefunden() {
        if (meldungZuSammelkarteArray == null) {
            return 0;
        }
        return meldungZuSammelkarteArray.length;
    }

    public EclMeldungZuSammelkarte meldungZuSammelkarteGefunden(int lfd) {
        return meldungZuSammelkarteArray[lfd];
    }

    public int createTable() {
        int rc = 0;
        DbLowLevel lDbLowLevel = new DbLowLevel(dbBundle);
        rc = lDbLowLevel.createTable("CREATE TABLE " + dbBundle.getSchemaMandant() + "tbl_meldungzusammelkarte ( "
                + "`mandant` int(11) NOT NULL, " + "`meldungsIdent` int(11) DEFAULT NULL, "
                + "`sammelIdent` int(11) DEFAULT NULL, " + "`willenserklaerungIdent` int(11) NOT NULL, "
                + "`weisungIdent` int(11) DEFAULT NULL, " + "`aktiv` int(11) DEFAULT NULL, "
                + "PRIMARY KEY (`willenserklaerungIdent`,`mandant`), " + "KEY `IDX_meldungsIdent` (`meldungsIdent`), "
                + "KEY `IDX_sammelIdent` (`sammelIdent`) " + ") ");
        return rc;
    }

    /**************************deleteAll Löschen aller Datensätze  eines Mandanten****************/
    public int deleteAll() {
        int erg = 0;

        try {

            String sql1 = "DELETE FROM " + dbBundle.getSchemaMandant() + "tbl_meldungZuSammelkarte where mandant=?;";
            PreparedStatement pstm1 = verbindung.prepareStatement(sql1);
            pstm1.setInt(1, dbBundle.clGlobalVar.mandant);

            erg = 0; /*Falls Duplicate Key, dann wird exception geschmissen und erg wohl nicht gefüllt!*/
            erg = pstm1.executeUpdate();

            pstm1.close();

        } catch (Exception e2) {
            CaBug.drucke("DbMeldungZuSammelkarte.deleteAll 001");
            System.err.println(" " + e2.getMessage());
            return (erg);
        }

        return 1;
    }

    /**************************setzt aktuelle Mandantennummer bei allen Datensätzen****************/
    public int updateMandant() {
        return dbBundle.dbLowLevel.rawUpdateMandant(dbBundle.getSchemaMandant() + "tbl_meldungZuSammelkarte");
    }

    /** dekodiert die aktuelle Position aus ergebnis in EclMeldungZuSammelkarte und gibt dieses zurück*/
    private EclMeldungZuSammelkarte decodeErgebnis(ResultSet ergebnis) {
        EclMeldungZuSammelkarte lMeldungZuSammelkarte = new EclMeldungZuSammelkarte();

        try {

            lMeldungZuSammelkarte.mandant = ergebnis.getInt("mandant");
            lMeldungZuSammelkarte.meldungsIdent = ergebnis.getInt("meldungsIdent");
            lMeldungZuSammelkarte.sammelIdent = ergebnis.getInt("sammelIdent");

            lMeldungZuSammelkarte.willenserklaerungIdent = ergebnis.getInt("willenserklaerungIdent");
            lMeldungZuSammelkarte.weisungIdent = ergebnis.getInt("weisungIdent");
            lMeldungZuSammelkarte.aktiv = ergebnis.getInt("aktiv");

        } catch (Exception e) {
            CaBug.drucke("DbMeldungZuSammelkarte.decodeErgebnis 001");
            System.err.println(" " + e.getMessage());
        }

        return lMeldungZuSammelkarte;
    }

    /** Fuellen Prepared Statement mit allen Feldern, beginnend bei offset.
     * Kann sowohl für Insert, als auch für update verwendet werden.
     * 
     * offset= Startposition des ersten Feldes (also z.B. 1)
     */
    private int anzfelder = 6;

    private void fuellePreparedStatementKomplett(PreparedStatement pstm, int offset,
            EclMeldungZuSammelkarte lMeldungZuSammelkarte) {

        try {
            pstm.setInt(offset, lMeldungZuSammelkarte.mandant);
            offset++;
            pstm.setInt(offset, lMeldungZuSammelkarte.meldungsIdent);
            offset++;
            pstm.setInt(offset, lMeldungZuSammelkarte.sammelIdent);
            offset++;
            pstm.setInt(offset, lMeldungZuSammelkarte.willenserklaerungIdent);
            offset++;
            pstm.setInt(offset, lMeldungZuSammelkarte.weisungIdent);
            offset++;
            pstm.setInt(offset, lMeldungZuSammelkarte.aktiv);
            offset++;

        } catch (SQLException e) {
            CaBug.drucke("DbMeldungZuSammelkarte.fuellePreparedStatementKomplett 001");
            e.printStackTrace();
        }

    }

    public int insert(EclMeldungZuSammelkarte lMeldungZuSammelkarte) {

        int erg = 0;

        lMeldungZuSammelkarte.mandant = dbBundle.clGlobalVar.mandant;

        try {

            /*Felder Neuanlage füllen*/
            String sql1 = "INSERT INTO " + dbBundle.getSchemaMandant() + "tbl_meldungZuSammelkarte " + "("
                    + "mandant, meldungsIdent, sammelIdent, " + "willenserklaerungIdent, weisungIdent, aktiv" + ")"
                    + "VALUES (" + "?, ?, ?, " + "?, ?, ?)";
            PreparedStatement pstm1 = verbindung.prepareStatement(sql1, ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
            fuellePreparedStatementKomplett(pstm1, 1, lMeldungZuSammelkarte);

            erg = 0; /*Falls Duplicate Key, dann wird exception geschmissen und erg wohl nicht gefüllt!*/
            //			System.out.println("vor Update");
            erg = pstm1.executeUpdate();
            //			System.out.println("nach Update");
            pstm1.close();
        } catch (Exception e2) {
            CaBug.drucke("DbMeldungZuSammelkarte.insert 001");
            System.err.println(" " + e2.getMessage());
        }

        if (erg == 0) {/*Fehler beim Einfügen*/
            return (-1);
        }
        return (1);
    }

    /**Sortierung nach meldungsIdent*/
    public int leseAlleAktiven() {
        int anzInArray = 0;
        try {

            String sql = "SELECT * from " + dbBundle.getSchemaMandant() + "tbl_meldungZuSammelkarte where "
                    + "mandant=? AND " + "aktiv=1 ORDER BY meldungsIdent;";

            PreparedStatement pstm1 = verbindung.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
            pstm1.setInt(1, dbBundle.clGlobalVar.mandant);

            ResultSet ergebnis = pstm1.executeQuery();
            ergebnis.last();
            anzInArray = ergebnis.getRow();
            ergebnis.beforeFirst();

            meldungZuSammelkarteArray = new EclMeldungZuSammelkarte[anzInArray];

            int i = 0;
            while (ergebnis.next() == true) {

                meldungZuSammelkarteArray[i] = this.decodeErgebnis(ergebnis);
                i++;

            }
            ergebnis.close();
            pstm1.close();

        } catch (Exception e) {
            CaBug.drucke("001");
            System.err.println(" " + e.getMessage());
        }

        return (anzInArray);
    }

    public int leseZuMeldungIdent(int pIdent) {
        EclMeldung lMeldung=new EclMeldung();
        lMeldung.meldungsIdent=pIdent;
        return leseZuMeldung(lMeldung);
    }
    
    public int leseZuMeldung(EclMeldung meldung) {
        int anzInArray = 0;
        try {

            String sql = "SELECT * from " + dbBundle.getSchemaMandant() + "tbl_meldungZuSammelkarte where "
                    + "mandant=? AND " + "meldungsIdent=? ORDER BY willenserklaerungIdent;";

            PreparedStatement pstm1 = verbindung.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
            pstm1.setInt(1, dbBundle.clGlobalVar.mandant);
            pstm1.setInt(2, meldung.meldungsIdent);

            ResultSet ergebnis = pstm1.executeQuery();
            ergebnis.last();
            anzInArray = ergebnis.getRow();
            ergebnis.beforeFirst();

            meldungZuSammelkarteArray = new EclMeldungZuSammelkarte[anzInArray];

            int i = 0;
            while (ergebnis.next() == true) {

                meldungZuSammelkarteArray[i] = this.decodeErgebnis(ergebnis);
                i++;

            }
            ergebnis.close();
            pstm1.close();

        } catch (Exception e) {
            CaBug.drucke("DbMeldungZuSammelkarte.leseZuMeldung 001");
            System.err.println(" " + e.getMessage());
        }

        return (anzInArray);
    }

    /**Liefert nur aktiven!*/
    public int leseZuSammelkarteNurAktive(int sammelkarteIdent) {
        int anzInArray = 0;
        try {

            String sql = "SELECT * from " + dbBundle.getSchemaMandant() + "tbl_meldungZuSammelkarte where "
                    + "mandant=? AND aktiv=1 AND sammelIdent=? ORDER BY meldungsIdent;";

            PreparedStatement pstm1 = verbindung.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
            pstm1.setInt(1, dbBundle.clGlobalVar.mandant);
            pstm1.setInt(2, sammelkarteIdent);

            ResultSet ergebnis = pstm1.executeQuery();
            ergebnis.last();
            anzInArray = ergebnis.getRow();
            ergebnis.beforeFirst();

            meldungZuSammelkarteArray = new EclMeldungZuSammelkarte[anzInArray];

            int i = 0;
            while (ergebnis.next() == true) {

                meldungZuSammelkarteArray[i] = this.decodeErgebnis(ergebnis);
                i++;

            }
            ergebnis.close();
            pstm1.close();

        } catch (Exception e) {
            CaBug.drucke("DbMeldungZuSammelkarte.leseZuMeldung 001");
            System.err.println(" " + e.getMessage());
        }

        return (anzInArray);
    }

    /**Liefert alle, auch die nicht aktiven!*/
    public int leseZuSammelkarte(int sammelkarteIdent) {
        int anzInArray = 0;
        try {

            String sql = "SELECT * from " + dbBundle.getSchemaMandant() + "tbl_meldungZuSammelkarte where "
                    + "mandant=? AND " + "sammelIdent=? ORDER BY meldungsIdent;";

            PreparedStatement pstm1 = verbindung.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
            pstm1.setInt(1, dbBundle.clGlobalVar.mandant);
            pstm1.setInt(2, sammelkarteIdent);

            ResultSet ergebnis = pstm1.executeQuery();
            ergebnis.last();
            anzInArray = ergebnis.getRow();
            ergebnis.beforeFirst();

            meldungZuSammelkarteArray = new EclMeldungZuSammelkarte[anzInArray];

            int i = 0;
            while (ergebnis.next() == true) {

                meldungZuSammelkarteArray[i] = this.decodeErgebnis(ergebnis);
                i++;

            }
            ergebnis.close();
            pstm1.close();

        } catch (Exception e) {
            CaBug.drucke("DbMeldungZuSammelkarte.leseZuMeldung 001");
            System.err.println(" " + e.getMessage());
        }

        return (anzInArray);
    }

    public int leseZuWillenserklaerung(int willenserklaerungIdent) {
        int anzInArray = 0;
        try {

            String sql = "SELECT * from " + dbBundle.getSchemaMandant() + "tbl_meldungZuSammelkarte where "
                    + "mandant=? AND " + "willenserklaerungIdent=?;";

            PreparedStatement pstm1 = verbindung.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
            pstm1.setInt(1, dbBundle.clGlobalVar.mandant);
            pstm1.setInt(2, willenserklaerungIdent);

            ResultSet ergebnis = pstm1.executeQuery();
            ergebnis.last();
            anzInArray = ergebnis.getRow();
            ergebnis.beforeFirst();

            meldungZuSammelkarteArray = new EclMeldungZuSammelkarte[anzInArray];

            int i = 0;
            while (ergebnis.next() == true) {

                meldungZuSammelkarteArray[i] = this.decodeErgebnis(ergebnis);
                i++;

            }
            ergebnis.close();
            pstm1.close();

        } catch (Exception e) {
            CaBug.drucke("DbMeldungZuSammelkarte.leseZuWillenserklaerung 001");
            System.err.println(" " + e.getMessage());
        }

        return (anzInArray);
    }

    /**Update einer Willenserklärung. Es wird davon ausgegangen, dass diese Funktion nur
     * innerhalb einer Transaktion aufgerufen wird!
     */
    public int update(EclMeldungZuSammelkarte lMeldungZuSammelkarte) {

        try {

            String sql = "UPDATE " + dbBundle.getSchemaMandant() + "tbl_meldungZuSammelkarte SET "
                    + "mandant=?, meldungsIdent=?, sammelIdent=?, "
                    + "willenserklaerungIdent=?, weisungIdent=?, aktiv=? " + "WHERE " + "willenserklaerungIdent=? AND "
                    + "mandant=?";

            PreparedStatement pstm1 = verbindung.prepareStatement(sql);
            fuellePreparedStatementKomplett(pstm1, 1, lMeldungZuSammelkarte);
            pstm1.setInt(anzfelder + 1, lMeldungZuSammelkarte.willenserklaerungIdent);
            pstm1.setLong(anzfelder + 2, dbBundle.clGlobalVar.mandant);

            int ergebnis1 = pstm1.executeUpdate();
            pstm1.close();
            if (ergebnis1 == 0) {
                return (CaFehler.pfdUnbekannterFehler);
            }
        } catch (Exception e1) {
            CaBug.drucke("DbMeldungZuSammelkarte.update 001");
            System.err.println(" " + e1.getMessage());
            return (CaFehler.pfdUnbekannterFehler);

        }
        return (1);
    }

    /**Update aktiv-Kennzeichen, Selektion über lWillenserklaerungIdent. Es wird davon ausgegangen, dass diese Funktion nur
     * innerhalb einer Transaktion aufgerufen wird!
     */
    public int updateAktivKZ(int lWillenserklaerungIdent, int lAktiv) {
        //	System.out.println("update Aktiv lWeisungIdent "+lWillenserklaerungIdent+" lAktiv "+lAktiv);

        try {
            String sql = "UPDATE " + dbBundle.getSchemaMandant() + "tbl_meldungZuSammelkarte SET " + "aktiv=? "
                    + "WHERE " + "willenserklaerungIdent=? AND " + "mandant=?";

            PreparedStatement pstm1 = verbindung.prepareStatement(sql);
            pstm1.setInt(1, lAktiv);
            pstm1.setInt(2, lWillenserklaerungIdent);
            pstm1.setLong(3, dbBundle.clGlobalVar.mandant);

            int ergebnis1 = pstm1.executeUpdate();
            pstm1.close();
            if (ergebnis1 == 0) {
                return (CaFehler.pfdUnbekannterFehler);
            }
        } catch (Exception e1) {
            CaBug.drucke("DbMeldungZuSammelkarte.updateAktivKZ 001");
            System.err.println(" " + e1.getMessage());
            return (CaFehler.pfdUnbekannterFehler);

        }
        return (1);
    }

}