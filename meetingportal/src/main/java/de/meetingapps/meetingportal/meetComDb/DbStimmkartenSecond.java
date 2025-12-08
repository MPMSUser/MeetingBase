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
import de.meetingapps.meetingportal.meetComEntities.EclStimmkartenSecond;
import de.meetingapps.meetingportal.meetComEntities.EclZutrittsIdent;

/*TODO _DBKlassenKonsolidierung: Close noch nicht eingebaut*/

public class DbStimmkartenSecond {
    private DbBundle dbBundle = null;
    private Connection verbindung = null;
    //	private DbBasis dbBasis=null;

    private EclStimmkartenSecond ergebnisArray[] = null;

    /*************************Initialisierung***************************/
    public DbStimmkartenSecond(DbBundle pDbBundle) {
        /* Verbindung in lokale Daten eintragen*/
        if (pDbBundle == null) {
            CaBug.drucke("DbStimmkartenSecond.init 001 - dbBundle nicht initialisiert");
            return;
        }
        if (pDbBundle.dbBasis == null) {
            CaBug.drucke("DbStimmkartenSecond.init 002 - dbBasis nicht initialisiert");
            return;
        }

        //		dbBasis=pDbBundle.dbBasis;
        verbindung = pDbBundle.dbBasis.verbindung;
        dbBundle = pDbBundle;
    }

    public int anzErgebnis() {
        if (ergebnisArray == null) {
            return 0;
        }
        return ergebnisArray.length;
    }

    public EclStimmkartenSecond ergebnisPosition(int lfd) {
        if (ergebnisArray == null) {
            CaBug.drucke("DbStimmkartenSecond.ergebnisPosition 001");
            return null;
        }
        if (lfd < 0) {
            CaBug.drucke("DbStimmkartenSecond.ergebnisPosition 002");
            return null;
        }
        if (lfd >= ergebnisArray.length) {
            CaBug.drucke("DbStimmkartenSecond.ergebnisPosition 003");
            return null;
        }
        return ergebnisArray[lfd];
    }

    public int createTable() {
        int rc = 0;
        DbLowLevel lDbLowLevel = new DbLowLevel(dbBundle);
        rc = lDbLowLevel.createTable("CREATE TABLE " + dbBundle.getSchemaMandant() + "tbl_stimmkartensecond ( "
                + "`mandant` int(11) DEFAULT NULL, " + "`stimmkarteSecond` char(20) DEFAULT NULL, "
                + "`db_version` bigint(20) DEFAULT NULL, " + "`delayedVorhanden` int(11) DEFAULT NULL, "
                + "`stimmkarteSecondIstGesperrt` int(11) DEFAULT NULL, "
                + "`stimmkarteSecondIstGesperrt_Delayed` int(11) DEFAULT NULL, "
                + "`stimmkarteSecondVers` int(11) DEFAULT NULL, "
                + "`stimmkarteSecondVers_Delayed` int(11) DEFAULT NULL, " + "`meldungsIdentGast` int(11) DEFAULT NULL, "
                + "`meldungsIdentAktionaer` int(11) DEFAULT NULL, " + "`gueltigeKlasse` int(11) DEFAULT NULL, "
                + "`gueltigeKlasse_Delayed` int(11) DEFAULT NULL, " + "`personenNatJurIdent` int(11) DEFAULT NULL, "
                + "`personenNatJurIdent_Delayed` int(11) DEFAULT NULL, " + "`zutrittsIdent` char(20) DEFAULT NULL, "
                + "`zutrittsIdentNeben` char(2) DEFAULT NULL, "
                + "UNIQUE KEY `cons_stimmkarteSecond` (`stimmkarteSecond`,`stimmkarteSecondVers`,`mandant`), "
                + "KEY `IDX_stimmkarteSecond` (`stimmkarteSecond`), "
                + "KEY `IDX_meldungsIdentGast` (`meldungsIdentGast`), "
                + "KEY `IDX_meldungsIdentAktionaer` (`meldungsIdentAktionaer`) " + ") ");
        return rc;
    }

    /**************************deleteAll Löschen aller Datensätze  eines Mandanten****************/
    public int deleteAll() {
        return dbBundle.dbLowLevel
                .deleteMandant("DELETE FROM " + dbBundle.getSchemaMandant() + "tbl_stimmkartenSecond where mandant=?;");
    }

    /**************************setzt aktuelle Mandantennummer bei allen Datensätzen****************/
    public int updateMandant() {
        return dbBundle.dbLowLevel.rawUpdateMandant(dbBundle.getSchemaMandant() + "tbl_stimmkartenSecond");
    }

    public void reorgInterneIdent() {
    }

    /** dekodiert die aktuelle Position aus ergebnis in EvlVIpKZ und gibt dieses zurück*/
    private EclStimmkartenSecond decodeErgebnis(ResultSet ergebnis) {
        EclStimmkartenSecond lStimmKartenSecond = new EclStimmkartenSecond();

        try {

            lStimmKartenSecond.mandant = ergebnis.getInt("mandant");

            lStimmKartenSecond.stimmkarteSecond = ergebnis.getString("stimmkarteSecond");
            lStimmKartenSecond.db_version = ergebnis.getLong("db_version");

            lStimmKartenSecond.delayedVorhanden = ergebnis.getInt("delayedVorhanden");
            lStimmKartenSecond.stimmkarteSecondIstGesperrt = ergebnis.getInt("stimmkarteSecondIstGesperrt");
            lStimmKartenSecond.stimmkarteSecondIstGesperrt_Delayed = ergebnis
                    .getInt("stimmkarteSecondIstGesperrt_Delayed");
            lStimmKartenSecond.stimmkarteSecondVers = ergebnis.getInt("stimmkarteSecondVers");
            lStimmKartenSecond.stimmkarteSecondVers_Delayed = ergebnis.getInt("stimmkarteSecondVers_Delayed");

            lStimmKartenSecond.meldungsIdentGast = ergebnis.getInt("meldungsIdentGast");
            lStimmKartenSecond.meldungsIdentAktionaer = ergebnis.getInt("meldungsIdentAktionaer");
            lStimmKartenSecond.gueltigeKlasse = ergebnis.getInt("gueltigeKlasse");
            lStimmKartenSecond.gueltigeKlasse_Delayed = ergebnis.getInt("gueltigeKlasse_Delayed");

            lStimmKartenSecond.personenNatJurIdent = ergebnis.getInt("personenNatJurIdent");
            lStimmKartenSecond.personenNatJurIdent_Delayed = ergebnis.getInt("personenNatJurIdent_Delayed");

            lStimmKartenSecond.zutrittsIdent = ergebnis.getString("zutrittsIdent");
            lStimmKartenSecond.zutrittsIdentNeben = ergebnis.getString("zutrittsIdentNeben");

        } catch (Exception e) {
            CaBug.drucke("DbStimmkartenSecond.decodeErgebnis 001");
            System.err.println(" " + e.getMessage());
        }

        return lStimmKartenSecond;
    }

    /** Fuellen Prepared Statement mit allen Feldern, beginnend bei offset.
     * Kann sowohl für Insert, als auch für update verwendet werden.
     * 
     * offset= Startposition des ersten Feldes (also z.B. 1)
     */
    private int anzfelder = 16; /*Anpassen auf Anzahl der Felder pro Datensatz*/

    private void fuellePreparedStatementKomplett(PreparedStatement pstm, int pOffset,
            EclStimmkartenSecond lStimmKartenSecond) {

        int startOffset = pOffset; /*Nur erforderlich zum Überprüfen von Programmierfehlern - Feldanzahl*/

        lStimmKartenSecond.mandant = dbBundle.clGlobalVar.mandant;

        try {
            pstm.setInt(pOffset, lStimmKartenSecond.mandant);
            pOffset++;
            pstm.setString(pOffset, lStimmKartenSecond.stimmkarteSecond);
            pOffset++;
            pstm.setLong(pOffset, lStimmKartenSecond.db_version);
            pOffset++;
            pstm.setInt(pOffset, lStimmKartenSecond.delayedVorhanden);
            pOffset++;
            pstm.setInt(pOffset, lStimmKartenSecond.stimmkarteSecondIstGesperrt);
            pOffset++;
            pstm.setInt(pOffset, lStimmKartenSecond.stimmkarteSecondIstGesperrt_Delayed);
            pOffset++;
            pstm.setInt(pOffset, lStimmKartenSecond.stimmkarteSecondVers);
            pOffset++;
            pstm.setInt(pOffset, lStimmKartenSecond.stimmkarteSecondVers_Delayed);
            pOffset++;
            pstm.setInt(pOffset, lStimmKartenSecond.meldungsIdentGast);
            pOffset++;
            pstm.setInt(pOffset, lStimmKartenSecond.meldungsIdentAktionaer);
            pOffset++;
            pstm.setInt(pOffset, lStimmKartenSecond.gueltigeKlasse);
            pOffset++;
            pstm.setInt(pOffset, lStimmKartenSecond.gueltigeKlasse_Delayed);
            pOffset++;
            pstm.setInt(pOffset, lStimmKartenSecond.personenNatJurIdent);
            pOffset++;
            pstm.setInt(pOffset, lStimmKartenSecond.personenNatJurIdent_Delayed);
            pOffset++;
            pstm.setString(pOffset, lStimmKartenSecond.zutrittsIdent);
            pOffset++;
            pstm.setString(pOffset, lStimmKartenSecond.zutrittsIdentNeben);
            pOffset++;

            if (pOffset - startOffset != anzfelder) {
                /*Nur wg. Überprüfung auf Programmierfehler - alle Felder berücksichtigt?*/
                CaBug.drucke("DbStimmkartenSecond.fuellePreparedStatementKomplett 002");
            }

        } catch (SQLException e) {
            CaBug.drucke("DbStimmkartenSecond.fuellePreparedStatementKomplett 001");
            e.printStackTrace();
        }

    }

    /**Liefert alle Stimmkarte, auch die mit Version !=0*/
    public int read_alleVersionen(String pStimmkarteSecond) {
        int anz = 0;
        ergebnisArray = null;

        String sql = "SELECT * from " + dbBundle.getSchemaMandant()
                + "tbl_stimmkartenSecond where stimmkarteSecond=? AND mandant=?";
        try {

            PreparedStatement pstm1 = verbindung.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
            pstm1.setString(1, pStimmkarteSecond);
            pstm1.setInt(2, dbBundle.clGlobalVar.mandant);
            ResultSet ergebnis = pstm1.executeQuery();

            ergebnis.last();
            anz = ergebnis.getRow();
            ergebnis.beforeFirst();
            ergebnisArray = new EclStimmkartenSecond[anz];

            int i = 0;
            while (ergebnis.next() == true) {
                ergebnisArray[i] = decodeErgebnis(ergebnis);
                i++;
            }
            ergebnis.close();
            pstm1.close();

        } catch (Exception e) {
            CaBug.drucke("DbStimmkartenSecond.readStimmkarteSecond 001");
            System.err.println(" " + e.getMessage());
        }
        return (anz);
    }

    /**Liefert aktive und inaktive/gesperrte, aber nicht versionierte.
     * Es wird immer nur die aktuelle Version geliefert (nicht delayed)*/
    public int read(String stimmkarteSecond) {
        return read(stimmkarteSecond, 1);
    }

    /**Liefert aktive und inaktive/gesperrte, aber nicht versionierte.
     * Delayed-Versionierung wird je nach Parameter aktuellOderDelayed berücksichtigt*/
    public int read(String stimmkarteSecond, int aktuellOderDelayed) {

        int anz = 0;
        ergebnisArray = null;

        String sql = "SELECT * from " + dbBundle.getSchemaMandant()
                + "tbl_stimmkartenSecond where stimmkarteSecond=? and mandant=? ";
        if (aktuellOderDelayed == 1) {
            sql = sql + "and stimmkarteSecondVers=0 ";
        } else {
            sql = sql + "and stimmkarteSecondVers_Delayed=0 ";
        }

        try {

            PreparedStatement pstm1 = verbindung.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
            pstm1.setString(1, stimmkarteSecond);
            pstm1.setInt(2, dbBundle.clGlobalVar.mandant);
            ResultSet ergebnis = pstm1.executeQuery();

            ergebnis.last();
            anz = ergebnis.getRow();
            ergebnis.beforeFirst();
            ergebnisArray = new EclStimmkartenSecond[anz];

            int i = 0;
            while (ergebnis.next() == true) {
                ergebnisArray[i] = decodeErgebnis(ergebnis);
                i++;
            }
            ergebnis.close();
            pstm1.close();

        } catch (Exception e) {
            CaBug.drucke("DbStimmkartenSecond.read 001");
            System.err.println(" " + e.getMessage());
        }

        return (anz);
    }

    /**Liefert alle StimmkartenSecond zu einer Meldung. Egal ob als Gast oder als Aktionär zugeordnet. Auch "Versionierte"*/
    public int readZuMeldungsIdent(int meldungsIdent) {
        int anz = 0;
        ergebnisArray = null;

        String sql = "SELECT * from " + dbBundle.getSchemaMandant()
                + "tbl_stimmkartenSecond where (meldungsIdentGast=? OR meldungsIdentAktionaer=?) and mandant=?";
        try {

            PreparedStatement pstm1 = verbindung.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
            pstm1.setString(1, Integer.toString(meldungsIdent));
            pstm1.setString(2, Integer.toString(meldungsIdent));
            pstm1.setInt(3, dbBundle.clGlobalVar.mandant);
            ResultSet ergebnis = pstm1.executeQuery();

            ergebnis.last();
            anz = ergebnis.getRow();
            ergebnis.beforeFirst();
            ergebnisArray = new EclStimmkartenSecond[anz];

            int i = 0;
            while (ergebnis.next() == true) {
                ergebnisArray[i] = decodeErgebnis(ergebnis);
                i++;
            }
            ergebnis.close();
            pstm1.close();
        } catch (Exception e) {
            CaBug.drucke("DbStimmkartenSecond.readZuMeldungsIdent 001");
            System.err.println(" " + e.getMessage());
        }
        return (anz);
    }

    /**Liefert alle StimmkartenSecond zu einer ZutrittsIdent. Egal ob als Gast oder als Aktionär zugeordnet. Nur gültige (also nicht
     * versionierte, und nicht gesperrte). Liefert alle (also auch delayte)*/
    public int readGueltigeZuZutrittsIdent(EclZutrittsIdent zutrittsIdent) {
        int anz = 0;
        ergebnisArray = null;

        String sql = "SELECT * from " + dbBundle.getSchemaMandant()
                + "tbl_stimmkartenSecond where zutrittsIdent=? AND zutrittsIdentNeben=? AND (stimmkarteSecondIstGesperrt=0 OR stimmkarteSecondIstGesperrt=1) AND mandant=?";
        try {

            PreparedStatement pstm1 = verbindung.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
            pstm1.setString(1, zutrittsIdent.zutrittsIdent);
            pstm1.setString(2, zutrittsIdent.zutrittsIdentNeben);
            pstm1.setInt(3, dbBundle.clGlobalVar.mandant);
            ResultSet ergebnis = pstm1.executeQuery();

            ergebnis.last();
            anz = ergebnis.getRow();
            ergebnis.beforeFirst();
            ergebnisArray = new EclStimmkartenSecond[anz];

            int i = 0;
            while (ergebnis.next() == true) {
                ergebnisArray[i] = decodeErgebnis(ergebnis);
                i++;
            }
            ergebnis.close();
            pstm1.close();
        } catch (Exception e) {
            CaBug.drucke("DbStimmkartenSecond.readGueltigeZuZutrittsIdent 001");
            System.err.println(" " + e.getMessage());
        }
        return (anz);
    }

    /**Liefert alle "aktiv gültigen (gueltigeKlasse)" StimmkartenSecond zu einer meldungsIdent und einer bestimmten Person. Egal ob als Gast oder als Aktionär zugeordnet. Nur gültige (also nicht
     * versionierte, und nicht gesperrte). Liefert alle (also auch delayte)*/
    public int readGueltigeZuMeldungPerson(int pMeldeIdent, int pPersonNatJurIdent) {
        int anz = 0;
        ergebnisArray = null;

        String sql = "SELECT * from " + dbBundle.getSchemaMandant() + "tbl_stimmkartenSecond where "
                + "((meldungsIdentGast=? AND gueltigeKlasse=0) OR (meldungsIdentAktionaer=? AND gueltigeKlasse=1)) "
                + "AND personenNatJurIdent=? AND (stimmkarteSecondIstGesperrt=0 OR stimmkarteSecondIstGesperrt=1) AND mandant=?";
        try {

            PreparedStatement pstm1 = verbindung.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
            pstm1.setInt(1, pMeldeIdent);
            pstm1.setInt(2, pMeldeIdent);
            pstm1.setInt(3, pPersonNatJurIdent);
            pstm1.setInt(4, dbBundle.clGlobalVar.mandant);
            ResultSet ergebnis = pstm1.executeQuery();

            ergebnis.last();
            anz = ergebnis.getRow();
            ergebnis.beforeFirst();
            ergebnisArray = new EclStimmkartenSecond[anz];

            int i = 0;
            while (ergebnis.next() == true) {
                ergebnisArray[i] = decodeErgebnis(ergebnis);
                i++;
            }
            ergebnis.close();
            pstm1.close();
        } catch (Exception e) {
            CaBug.drucke("DbStimmkartenSecond.readGueltigeZuMeldungPerson 001");
            System.err.println(" " + e.getMessage());
        }
        return (anz);
    }

    public int insert(EclStimmkartenSecond lStimmKartenSecond) {
        int erg = 0;
        lStimmKartenSecond.mandant = dbBundle.clGlobalVar.mandant;

        try {

            /*Felder Neuanlage füllen*/
            String sql1 = "INSERT INTO " + dbBundle.getSchemaMandant() + "tbl_stimmkartenSecond " + "(" + "mandant, "
                    + "stimmkarteSecond, db_version, delayedVorhanden, "
                    + "stimmkarteSecondIstGesperrt, stimmkarteSecondIstGesperrt_Delayed, stimmkarteSecondVers, stimmkarteSecondVers_Delayed,"
                    + "meldungsIdentGast, meldungsIdentAktionaer, gueltigeKlasse, gueltigeKlasse_Delayed, "
                    + "personenNatJurIdent, personenNatJurIdent_Delayed, zutrittsIdent, zutrittsIdentNeben " + ")" +

                    "VALUES (" + "?, ?, " + "?, ?, ?, " + "?, ?, ?, ?, " + "?, ?, ?, ?, " + "?, ?, ? " + ")";
            PreparedStatement pstm1 = verbindung.prepareStatement(sql1, ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);

            fuellePreparedStatementKomplett(pstm1, 1, lStimmKartenSecond);

            erg = 0; /*Falls Duplicate Key, dann wird exception geschmissen und erg wohl nicht gefüllt!*/
            erg = pstm1.executeUpdate();
            pstm1.close();
        } catch (Exception e2) {
            CaBug.drucke("DbStimmkartenSecond.insert 001");
            System.err.println(" " + e2.getMessage());
        }

        if (erg == 0) {/*Fehler beim Einfügen - d.h. ZutrittsIdent / zutrittsIdentKlasse bereits vorhanden*/
            return (CaFehler.pfdXyBereitsVorhanden);
        }
        return (1);
    }

    public int update(EclStimmkartenSecond lStimmKartenNeuSecond) {

        lStimmKartenNeuSecond.db_version++;

        try {

            String sql = "UPDATE " + dbBundle.getSchemaMandant() + "tbl_stimmkartenSecond SET " + "mandant=?, "
                    + "stimmkarteSecond=?, db_version=?, delayedVorhanden=?, "
                    + "stimmkarteSecondIstGesperrt=?, stimmkarteSecondIstGesperrt_Delayed=?, stimmkarteSecondVers=?, stimmkarteSecondVers_Delayed=?, "
                    + "meldungsIdentGast=?, meldungsIdentAktionaer=?, gueltigeKlasse=?, gueltigeKlasse_Delayed=?, "
                    + "personenNatJurIdent=?, personenNatJurIdent_Delayed=?, zutrittsIdent=?, zutrittsIdentNeben=? "
                    + "WHERE " + "mandant=? AND stimmkarteSecond=? AND db_version=? ";

            PreparedStatement pstm1 = verbindung.prepareStatement(sql);
            fuellePreparedStatementKomplett(pstm1, 1, lStimmKartenNeuSecond); /*SET-Teil*/
            pstm1.setInt(anzfelder + 1, dbBundle.clGlobalVar.mandant);
            pstm1.setString(anzfelder + 2, lStimmKartenNeuSecond.stimmkarteSecond);
            pstm1.setLong(anzfelder + 3, lStimmKartenNeuSecond.db_version - 1);

            int ergebnis1 = pstm1.executeUpdate();
            pstm1.close();

            if (ergebnis1 == 0) {
                return (CaFehler.pfXyWurdeVonAnderemBenutzerVeraendert);
            }
        } catch (Exception e1) {
            CaBug.drucke("DbStimmkartenSecond.update 001");
            System.err.println(" " + e1.getMessage());
            return (CaFehler.pfdXyBereitsVorhanden);

        }

        return (1);
    }

    /****************************************************************************************************************************************/
    /**************************************Methoden zum Auflösen der Delayed-StimmkartenSecond***************************************************/
    /****************************************************************************************************************************************/
    /**Hier wird die jeweils nächste eingelesen und zu verarbeitende delayed Willenserklärung von diesen Methoden abgelegt*/
    public EclStimmkartenSecond rcDelayedStimmkartenSecond = null;

    /**Anzahl der zu verarbeitenden Delayed-Meldungen - ist nach aufloesenDelayed_init gefüllt*/
    public int rcDelayedAnzahl = 0;

    private ResultSet delayedErgebnis = null;

    /**Initialisierung - setzt Select ab*/
    public int initAufloesenDelayed() {
        try {

            String sql = "SELECT * from " + dbBundle.getSchemaMandant() + "tbl_stimmkartenSecond where "
                    + "mandant=? AND delayedVorhanden=1;";

            PreparedStatement pstm1 = verbindung.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
            pstm1.setInt(1, dbBundle.clGlobalVar.mandant);

            delayedErgebnis = pstm1.executeQuery();
            delayedErgebnis.last();
            rcDelayedAnzahl = delayedErgebnis.getRow();
            delayedErgebnis.beforeFirst();

        } catch (Exception e) {
            CaBug.drucke("DbStimmkartenSecond.initAufloesenDelayed 001");
            System.err.println(" " + e.getMessage());
        }

        return (rcDelayedAnzahl);
    }

    /**Liefere nächste Willenserklärung in rcDelayedStimmkartenSecond ab
     * liefert 0, wenn Dateiende erreicht*/
    public int readNextDelayed() {
        try {
            if (delayedErgebnis.next() == false) {
                return (0);
            }
            rcDelayedStimmkartenSecond = this.decodeErgebnis(delayedErgebnis);

        } catch (Exception e) {
            CaBug.drucke("DbStimmkartenSecond.readNextDelayed 001");
            System.err.println(" " + e.getMessage());
        }
        return 1;
    }

}
