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
import de.meetingapps.meetingportal.meetComAllg.CaString;
import de.meetingapps.meetingportal.meetComEntities.EclZutrittsIdent;
import de.meetingapps.meetingportal.meetComEntities.EclZutrittskarten;
import de.meetingapps.meetingportal.meetComHVParam.ParamSpezial;
import de.meetingapps.meetingportal.meetComKonst.KonstKartenklasse;

public class DbZutrittskarten {
    private DbBundle dbBundle = null;
    private Connection verbindung = null;
    //	private DbBasis dbBasis=null;

    private EclZutrittskarten ergebnisArray[] = null;
    //	private EclZutrittskarten zutrittskartenOldVersion=null;

    /*************************Initialisierung***************************/
    public DbZutrittskarten(DbBundle pDbBundle) {
        /* Verbindung in lokale Daten eintragen*/
        if (pDbBundle == null) {
            CaBug.drucke("DbZutrittskarten.init 001 - dbBundle nicht initialisiert");
            return;
        }
        if (pDbBundle.dbBasis == null) {
            CaBug.drucke("DbZutrittskarten.init 002 - dbBasis nicht initialisiert");
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

    public EclZutrittskarten ergebnisPosition(int lfd) {
        if (ergebnisArray == null) {
            CaBug.drucke("DbZutrittskarten.ergebnisPosition 001");
            return null;
        }
        if (lfd < 0) {
            CaBug.drucke("DbZutrittskarten.ergebnisPosition 002");
            return null;
        }
        if (lfd >= ergebnisArray.length) {
            CaBug.drucke("DbZutrittskarten.ergebnisPosition 003");
            return null;
        }
        return ergebnisArray[lfd];
    }

    public EclZutrittskarten[] ergebnis() {
        if (ergebnisArray == null) {
            return new EclZutrittskarten[0];
        }
        return ergebnisArray;
    }


    public int createTable() {
        int rc = 0;
        DbLowLevel lDbLowLevel = new DbLowLevel(dbBundle);
        rc = lDbLowLevel.createTable("CREATE TABLE " + dbBundle.getSchemaMandant() + "tbl_zutrittskarten ( "
                + "`mandant` int(11) NOT NULL, " + "`zutrittsIdent` char(20) NOT NULL, "
                + "`zutrittsIdentNeben` char(2) NOT NULL, " + "`db_version` bigint(20) DEFAULT NULL, "
                + "`zutrittsIdentKlasse` int(11) NOT NULL, " + "`delayedVorhanden` int(11) DEFAULT NULL, "
                + "`zutrittsIdentIstGesperrt` int(11) DEFAULT NULL, "
                + "`zutrittsIdentIstGesperrt_Delayed` int(11) DEFAULT NULL, "
                + "`zutrittsIdentVers` int(11) NOT NULL, " + "`zutrittsIdentVers_Delayed` int(11) NOT NULL, "
                + "`meldungsIdentGast` int(11) DEFAULT NULL, " + "`meldungsIdentAktionaer` int(11) DEFAULT NULL, "
                + "`gueltigeKlasse` int(11) DEFAULT NULL, " + "`gueltigeKlasse_Delayed` int(11) DEFAULT NULL, "
                + "`personenNatJurIdent` int(11) DEFAULT NULL, "
                + "`personenNatJurIdent_Delayed` int(11) DEFAULT NULL, "
                + "`ausgestelltAufPersonenNatJurIdent` int(11) DEFAULT NULL, "
                + "PRIMARY KEY (`mandant`,`zutrittsIdent`,`zutrittsIdentNeben`,`zutrittsIdentKlasse`,`zutrittsIdentVers`,`zutrittsIdentVers_Delayed`), "
                + "KEY `IDX_zutrittsIdent` (`zutrittsIdent`), " + "KEY `IDX_meldungsIdentGast` (`meldungsIdentGast`), "
                + "KEY `IDX_meldungsIdentAktionaer` (`meldungsIdentAktionaer`) " + ") ");
        return rc;
    }

    /**************************deleteAll Löschen aller Datensätze  eines Mandanten****************/
    public int deleteAll() {
        dbBundle.dbBasis.resetGastkartennummer();
        for (int i = 1; i <= 5; i++) {
            dbBundle.dbBasis.resetEintrittskartennummer(i);
        }
        return dbBundle.dbLowLevel
                .deleteMandant("DELETE FROM " + dbBundle.getSchemaMandant() + "tbl_zutrittskarten where mandant=?;");
    }

    /**************************setzt aktuelle Mandantennummer bei allen Datensätzen****************/
    public int updateMandant() {
        return dbBundle.dbLowLevel.rawUpdateMandant(dbBundle.getSchemaMandant() + "tbl_zutrittskarten");
    }

    public void reorgInterneIdent() {
        String von, bis;
        int lMax = 0;
        /*Aktionärs-ZutrittsIdents Auto für alle Gattungen*/
        for (int i = 1; i <= 5; i++) {
            von = CaString.fuelleLinksNull(
                    Integer.toString(dbBundle.param.paramNummernkreise.vonSubEintrittskartennummer[i][2]),
                    dbBundle.param.paramNummernkreise.laengeKartennummer[KonstKartenklasse.eintrittskartennummer]);
            bis = CaString.fuelleLinksNull(
                    Integer.toString(dbBundle.param.paramNummernkreise.bisSubEintrittskartennummer[i][2]),
                    dbBundle.param.paramNummernkreise.laengeKartennummer[KonstKartenklasse.eintrittskartennummer]);
            lMax = dbBundle.dbLowLevel.liefereHoechsteIdentString("SELECT MAX(zutrittsIdent) FROM "
                    + dbBundle.getSchemaMandant()
                    + "tbl_zutrittskarten ab where ab.mandant=? AND ab.zutrittsIdentKlasse=1 AND ab.zutrittsIdent>="
                    + von + " AND ab.zutrittsIdent<=" + bis);
            if (lMax == -1) {
                lMax = 0;
            }
            dbBundle.dbBasis.resetEintrittskartennummer(i, lMax);
        }

        /*Gastkarten - Auto*/
        von = CaString.fuelleLinksNull(
                Integer.toString(
                        dbBundle.param.paramNummernkreise.vonKartennummerAuto[KonstKartenklasse.gastkartennummer]),
                dbBundle.param.paramNummernkreise.laengeKartennummer[KonstKartenklasse.gastkartennummer]);
        bis = CaString.fuelleLinksNull(
                Integer.toString(
                        dbBundle.param.paramNummernkreise.bisKartennummerAuto[KonstKartenklasse.gastkartennummer]),
                dbBundle.param.paramNummernkreise.laengeKartennummer[KonstKartenklasse.gastkartennummer]);
        lMax = dbBundle.dbLowLevel
                .liefereHoechsteIdentString("SELECT MAX(zutrittsIdent) FROM " + dbBundle.getSchemaMandant()
                        + "tbl_zutrittskarten ab where ab.mandant=? AND ab.zutrittsIdentKlasse=0 AND ab.zutrittsIdent>="
                        + von + " AND ab.zutrittsIdent<=" + bis);
        if (lMax == -1) {
            lMax = 0;
        }
        dbBundle.dbBasis.resetGastkartennummer(lMax);
    }

    /** dekodiert die aktuelle Position aus ergebnis in EclZutrittskarten und gibt dieses zurück*/
    EclZutrittskarten decodeErgebnis(ResultSet ergebnis) {
        EclZutrittskarten lZutrittsKarten = new EclZutrittskarten();

        try {

            lZutrittsKarten.mandant = ergebnis.getInt("mandant");

            lZutrittsKarten.zutrittsIdent = ergebnis.getString("zutrittsIdent");
            lZutrittsKarten.zutrittsIdentNeben = ergebnis.getString("zutrittsIdentNeben");
            lZutrittsKarten.db_version = ergebnis.getLong("db_version");

            lZutrittsKarten.zutrittsIdentKlasse = ergebnis.getInt("zutrittsIdentKlasse");
            lZutrittsKarten.delayedVorhanden = ergebnis.getInt("delayedVorhanden");
            lZutrittsKarten.zutrittsIdentIstGesperrt = ergebnis.getInt("zutrittsIdentIstGesperrt");
            lZutrittsKarten.zutrittsIdentIstGesperrt_Delayed = ergebnis.getInt("zutrittsIdentIstGesperrt_Delayed");
            lZutrittsKarten.zutrittsIdentVers = ergebnis.getInt("zutrittsIdentVers");
            lZutrittsKarten.zutrittsIdentVers_Delayed = ergebnis.getInt("zutrittsIdentVers_Delayed");

            lZutrittsKarten.meldungsIdentGast = ergebnis.getInt("meldungsIdentGast");
            lZutrittsKarten.meldungsIdentAktionaer = ergebnis.getInt("meldungsIdentAktionaer");
            lZutrittsKarten.gueltigeKlasse = ergebnis.getInt("gueltigeKlasse");
            lZutrittsKarten.gueltigeKlasse_Delayed = ergebnis.getInt("gueltigeKlasse_Delayed");

            lZutrittsKarten.personenNatJurIdent = ergebnis.getInt("personenNatJurIdent");
            lZutrittsKarten.personenNatJurIdent_Delayed = ergebnis.getInt("personenNatJurIdent_Delayed");

            lZutrittsKarten.ausgestelltAufPersonenNatJurIdent = ergebnis.getInt("ausgestelltAufPersonenNatJurIdent");

        } catch (Exception e) {
            CaBug.drucke("DbZutrittskarten.decodeErgebnis 001");
            System.err.println(" " + e.getMessage());
        }

        return lZutrittsKarten;
    }

    /********************* Fuellen Prepared Statement mit allen Feldern, beginnend bei offset.*****************
     * Kann sowohl für Insert, als auch für update verwendet werden.
     * 
     * offset= Startposition des ersten Feldes (also z.B. 1)
     */
    private int anzfelder = 17; /*Anpassen auf Anzahl der Felder pro Datensatz*/

    private void fuellePreparedStatementKomplett(PreparedStatement pstm, int offset,
            EclZutrittskarten lZutrittsKarten) {

        int startOffset = offset; /*Nur erforderlich zum Überprüfen von Programmierfehlern - Feldanzahl*/

        lZutrittsKarten.mandant = dbBundle.clGlobalVar.mandant;

        try {
            pstm.setInt(offset, lZutrittsKarten.mandant);
            offset++;
            pstm.setString(offset, lZutrittsKarten.zutrittsIdent);
            offset++;
            pstm.setString(offset, lZutrittsKarten.zutrittsIdentNeben);
            offset++;
            pstm.setLong(offset, lZutrittsKarten.db_version);
            offset++;
            pstm.setInt(offset, lZutrittsKarten.zutrittsIdentKlasse);
            offset++;
            pstm.setInt(offset, lZutrittsKarten.delayedVorhanden);
            offset++;
            pstm.setInt(offset, lZutrittsKarten.zutrittsIdentIstGesperrt);
            offset++;
            pstm.setInt(offset, lZutrittsKarten.zutrittsIdentIstGesperrt_Delayed);
            offset++;
            pstm.setInt(offset, lZutrittsKarten.zutrittsIdentVers);
            offset++;
            pstm.setInt(offset, lZutrittsKarten.zutrittsIdentVers_Delayed);
            offset++;
            pstm.setInt(offset, lZutrittsKarten.meldungsIdentGast);
            offset++;
            pstm.setInt(offset, lZutrittsKarten.meldungsIdentAktionaer);
            offset++;
            pstm.setInt(offset, lZutrittsKarten.gueltigeKlasse);
            offset++;
            pstm.setInt(offset, lZutrittsKarten.gueltigeKlasse_Delayed);
            offset++;
            pstm.setInt(offset, lZutrittsKarten.personenNatJurIdent);
            offset++;
            pstm.setInt(offset, lZutrittsKarten.personenNatJurIdent_Delayed);
            offset++;
            pstm.setInt(offset, lZutrittsKarten.ausgestelltAufPersonenNatJurIdent);
            offset++;

            if (offset - startOffset != anzfelder) {
                /*Nur wg. Überprüfung auf Programmierfehler - alle Felder berücksichtigt?*/
                CaBug.drucke("DbZutrittskarten.fuellePreparedStatementKomplett 002");
            }

        } catch (SQLException e) {
            CaBug.drucke("DbZutrittskarten.fuellePreparedStatementKomplett 001");
            e.printStackTrace();
        }

    }

    /**Liefert alle ZutrittsIdents, auch die mit Version !=0*/
    public int read_alleVersionen(EclZutrittsIdent pZutrittsIdent) {
        int anz = 0;

        ergebnisArray = null;

        String sql = "SELECT * from " + dbBundle.getSchemaMandant()
                + "tbl_zutrittskarten zk where zk.zutrittsIdent=? AND zk.zutrittsIdentNeben=? AND zk.mandant=?";
        try {

            PreparedStatement pstm1 = verbindung.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
            pstm1.setString(1, pZutrittsIdent.zutrittsIdent);
            pstm1.setString(2, pZutrittsIdent.zutrittsIdentNeben);
            pstm1.setInt(3, dbBundle.clGlobalVar.mandant);
            ResultSet ergebnis = pstm1.executeQuery();

            ergebnis.last();
            anz = ergebnis.getRow();
            ergebnis.beforeFirst();

            ergebnisArray = new EclZutrittskarten[anz];

            int i = 0;
            while (ergebnis.next() == true) {
                ergebnisArray[i] = decodeErgebnis(ergebnis);
                i++;
            }

            ergebnis.close();
            pstm1.close();

        } catch (Exception e) {
            System.err.println(" " + e.getMessage());
            CaBug.drucke("DbZutrittskarten.readZutrittsIdent 001");
        }

        if (anz == 0) {
            return (CaFehler.pfXyNichtVorhanden);
        }

        return (anz);

    }

    /**Liefert aktive und inaktive/gesperrte, aber nicht versionierte Sätze zu pZutrittsIdent, sowohl Gastkarten 
     * als auch Aktionärseintrittskarten.
     * Delayed-Versionierung wird je nach Parameter aktuellOderDelayed berücksichtigt*/
    public int read(EclZutrittsIdent pZutrittsIdent, int aktuellOderDelayed) {
        int anz = 0;

        ergebnisArray = null;

        String sql = "SELECT * from " + dbBundle.getSchemaMandant()
                + "tbl_zutrittskarten zk where zk.zutrittsIdent=? AND zk.zutrittsIdentNeben=? AND zk.mandant=? ";
        if (aktuellOderDelayed == 1) {
            sql = sql + "and zk.zutrittsIdentVers=0 ";
        } else {
            sql = sql + "and zk.zutrittsIdentVers_Delayed=0 ";
        }
        try {

            PreparedStatement pstm1 = verbindung.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
            pstm1.setString(1, pZutrittsIdent.zutrittsIdent);
            pstm1.setString(2, pZutrittsIdent.zutrittsIdentNeben);
            pstm1.setInt(3, dbBundle.clGlobalVar.mandant);
            ResultSet ergebnis = pstm1.executeQuery();

            ergebnis.last();
            anz = ergebnis.getRow();
            ergebnis.beforeFirst();
            ergebnisArray = new EclZutrittskarten[anz];

            int i = 0;
            while (ergebnis.next() == true) {
                ergebnisArray[i] = decodeErgebnis(ergebnis);
                i++;
            }
            ergebnis.close();
            pstm1.close();

        } catch (Exception e) {
            CaBug.drucke("DbZutrittskarten.readZutrittsIdent 001");
            System.err.println(" " + e.getMessage());
        }
        if (anz == 0) {
            return (CaFehler.pfXyNichtVorhanden);
        }
        return (anz);
    }

    /**Liefert aktive und inaktive/gesperrte, aber nicht versionierte Sätze zu pZutrittsIdent, Gastkarten.
     * Delayed-Versionierung wird je nach Parameter aktuellOderDelayed berücksichtigt*/
    public int readGast(EclZutrittsIdent pZutrittsIdent, int aktuellOderDelayed) {
        int anz = 0;

        ergebnisArray = null;

        String sql = "SELECT * from " + dbBundle.getSchemaMandant()
                + "tbl_zutrittskarten zk where zk.zutrittsIdent=? AND zk.zutrittsIdentNeben=? AND zk.zutrittsIdentKlasse=0 AND zk.mandant=? ";
        if (aktuellOderDelayed == 1) {
            sql = sql + "and zk.zutrittsIdentVers=0 ";
        } else {
            sql = sql + "and zk.zutrittsIdentVers_Delayed=0 ";
        }
        try {

            PreparedStatement pstm1 = verbindung.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
            pstm1.setString(1, pZutrittsIdent.zutrittsIdent);
            pstm1.setString(2, pZutrittsIdent.zutrittsIdentNeben);
            pstm1.setInt(3, dbBundle.clGlobalVar.mandant);
            ResultSet ergebnis = pstm1.executeQuery();

            ergebnis.last();
            anz = ergebnis.getRow();
            ergebnis.beforeFirst();
            ergebnisArray = new EclZutrittskarten[anz];

            int i = 0;
            while (ergebnis.next() == true) {
                ergebnisArray[i] = decodeErgebnis(ergebnis);
                i++;
            }
            ergebnis.close();
            pstm1.close();
        } catch (Exception e) {
            CaBug.drucke("DbZutrittskarten.readGast 001");
            System.err.println(" " + e.getMessage());
        }
        if (anz == 0) {
            return (CaFehler.pfXyNichtVorhanden);
        }

        return (anz);
    }

    /**Liefert aktive und inaktive/gesperrte, aber nicht versionierte Sätze zu pZutrittsIdent,  
     * Aktionärseintrittskarten.
     * Delayed-Versionierung wird je nach Parameter aktuell(1)OderDelayed(2) berücksichtigt*/
    public int readAktionaer(EclZutrittsIdent pZutrittsIdent, int aktuellOderDelayed) {
        int anz = 0;

        ergebnisArray = null;

        String sql = "SELECT * from " + dbBundle.getSchemaMandant()
                + "tbl_zutrittskarten zk where zk.zutrittsIdent=? AND zk.zutrittsIdentNeben=? AND zk.zutrittsIdentKlasse=1 AND zk.mandant=? ";
        if (aktuellOderDelayed == 1) {
            sql = sql + "and zk.zutrittsIdentVers=0 ";
        } else {
            sql = sql + "and zk.zutrittsIdentVers_Delayed=0 ";
        }
        try {

            PreparedStatement pstm1 = verbindung.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
            pstm1.setString(1, pZutrittsIdent.zutrittsIdent);
            pstm1.setString(2, pZutrittsIdent.zutrittsIdentNeben);
            pstm1.setInt(3, dbBundle.clGlobalVar.mandant);
            ResultSet ergebnis = pstm1.executeQuery();
            ergebnis.last();
            anz = ergebnis.getRow();
            ergebnis.beforeFirst();
            ergebnisArray = new EclZutrittskarten[anz];

            int i = 0;
            while (ergebnis.next() == true) {
                ergebnisArray[i] = decodeErgebnis(ergebnis);
                i++;
            }
            ergebnis.close();
            pstm1.close();
        } catch (Exception e) {
            CaBug.drucke("DbZutrittskarten.readAktionaer 001");
            System.err.println(" " + e.getMessage());
        }
        if (anz == 0) {
            return (CaFehler.pfXyNichtVorhanden);
        }
        return (anz);
    }

    @Deprecated
    /**Liefert aktive und stornierte, aber nicht versionierte.
     * Delayed-versionierung wird dabei nicht berücksichtigt!
     * Wird abgelöst durch readGast*/
    public int readZutrittsIdentGast(EclZutrittsIdent pZutrittsIdent) {
        int anz = 0;

        ergebnisArray = null;

        String sql = "SELECT * from " + dbBundle.getSchemaMandant()
                + "tbl_zutrittskarten zk where zk.zutrittsIdent=? AND zk.zutrittsIdentNeben=? and zk.zutrittsIdentKlasse=0  and zk.zutrittsIdentVers=0 and zk.mandant=?";
        try {

            PreparedStatement pstm1 = verbindung.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
            pstm1.setString(1, pZutrittsIdent.zutrittsIdent);
            pstm1.setString(2, pZutrittsIdent.zutrittsIdentNeben);
            pstm1.setInt(3, dbBundle.clGlobalVar.mandant);
            ResultSet ergebnis = pstm1.executeQuery();

            ergebnis.last();
            anz = ergebnis.getRow();
            ergebnis.beforeFirst();
            ergebnisArray = new EclZutrittskarten[anz];

            int i = 0;
            while (ergebnis.next() == true) {
                ergebnisArray[i] = decodeErgebnis(ergebnis);
                i++;
            }
            ergebnis.close();
            pstm1.close();

        } catch (Exception e) {
            CaBug.drucke("DbZutrittskarten.readZutrittsIdentGast 001");
            System.err.println(" " + e.getMessage());
        }

        if (anz == 0) {
            return (CaFehler.pfXyNichtVorhanden);
        }

        return (1);
    }

    @Deprecated
    /**Liefert aktive und stornierte, aber nicht versionierte.
     * Delay-Versionierung wird dabei nicht berücksichtigt!
     * Wird abgelöst durch readAktionaer*/
    public int readZutrittsIdentAktionaer(EclZutrittsIdent pZutrittsIdent) {
        int anz = 0;

        ergebnisArray = null;

        String sql = "SELECT * from " + dbBundle.getSchemaMandant()
                + "tbl_zutrittskarten zk where zk.zutrittsIdent=? AND zk.zutrittsIdentNeben=? and zk.zutrittsIdentKlasse=1 and zk.zutrittsIdentVers=0 and zk.mandant=?";
        try {

            PreparedStatement pstm1 = verbindung.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
            pstm1.setString(1, pZutrittsIdent.zutrittsIdent);
            pstm1.setString(2, pZutrittsIdent.zutrittsIdentNeben);
            pstm1.setInt(3, dbBundle.clGlobalVar.mandant);
            ResultSet ergebnis = pstm1.executeQuery();

            ergebnis.last();
            anz = ergebnis.getRow();
            ergebnis.beforeFirst();
            ergebnisArray = new EclZutrittskarten[anz];

            int i = 0;
            while (ergebnis.next() == true) {
                ergebnisArray[i] = decodeErgebnis(ergebnis);
                i++;
            }
            ergebnis.close();
            pstm1.close();

        } catch (Exception e) {
            CaBug.drucke("DbZutrittskarten.readZutrittsIdentAktionaer 001");
            System.err.println(" " + e.getMessage());
        }
        if (anz == 0) {
            return (CaFehler.pfXyNichtVorhanden);
        }

        return (1);

    }

    /**Liefert alle zugeordneten ZutrittsIdents, auch gesperrte. Dies ist ab sofort so gewollt :-), aber es ist noch nicht untersucht,
     * ob nicht aufrufende Funktionen davon ausgehen, dass nur gültige ZutrittsIdents gemeldet werden
     */
    public int readZuMeldungsIdent(int meldungsIdent) {
        /*TODO $Codeoptimierung: readZuMeldungsIdent mit readZuMeldungsIdentGast und readZuMeldungsIdentAktionaer verschmelzen*/
        /*TODO $Codeoptimierung: readZuMeldungsIdent* - liefert derzeit alle, auch gesperrte! Ist das so gewollt?*/
        int anz = 0;
        ergebnisArray = null;
        String sql = "SELECT * from " + dbBundle.getSchemaMandant()
                + "tbl_zutrittskarten zk where (zk.meldungsIdentGast=? or zk.meldungsIdentAktionaer=?) and zk.mandant=?";
        try {

            PreparedStatement pstm1 = verbindung.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
            pstm1.setInt(1, meldungsIdent); /*TODO $$Prüfen: war ursprünglich String-Umwandlung*/
            pstm1.setInt(2, meldungsIdent); /*TODO $$Prüfen: war ursprünglich String-Umwandlung*/
            pstm1.setInt(3, dbBundle.clGlobalVar.mandant);
            ResultSet ergebnis = pstm1.executeQuery();

            ergebnis.last();
            anz = ergebnis.getRow();
            ergebnis.beforeFirst();
            ergebnisArray = new EclZutrittskarten[anz];

            int i = 0;
            while (ergebnis.next() == true) {
                ergebnisArray[i] = decodeErgebnis(ergebnis);
                i++;
            }
            ergebnis.close();
            pstm1.close();

        } catch (Exception e) {
            CaBug.drucke("DbZutrittskarten.readZuMeldungsIdent 001");
            System.err.println(" " + e.getMessage());
        }
        if (anz == 0) {
            return (CaFehler.pfXyNichtVorhanden);
        }

        return (1);

    }

    /**gueltigeKlasse wird berücksichtigt!*/
    public int readGueltigeZuMeldungPerson(int meldungsIdent, int personIdent) {
        int anz = 0;
        ergebnisArray = null;
        String sql = "SELECT * from " + dbBundle.getSchemaMandant() + "tbl_zutrittskarten zk where "
                + "((zk.meldungsIdentGast=? AND zk.gueltigeKlasse=0) OR (zk.meldungsIdentAktionaer=? AND zk.gueltigeKlasse=1)) "
                + "and zk.mandant=? and zk.zutrittsIdentIstGesperrt=0 and zk.personenNatJurIdent=?";
        try {

            PreparedStatement pstm1 = verbindung.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
            pstm1.setInt(1, meldungsIdent); /*TODO $$Prüfen: war ursprünglich String-Umwandlung*/
            pstm1.setInt(2, meldungsIdent); /*TODO $$Prüfen: war ursprünglich String-Umwandlung*/
            pstm1.setInt(3, dbBundle.clGlobalVar.mandant);
            pstm1.setInt(4, personIdent);
            ResultSet ergebnis = pstm1.executeQuery();

            ergebnis.last();
            anz = ergebnis.getRow();
            ergebnis.beforeFirst();
            ergebnisArray = new EclZutrittskarten[anz];

            int i = 0;
            while (ergebnis.next() == true) {
                ergebnisArray[i] = decodeErgebnis(ergebnis);
                i++;
            }
            ergebnis.close();
            pstm1.close();

        } catch (Exception e) {
            CaBug.drucke("DbZutrittskarten.readGueltigeZuMeldungPerson 001");
            System.err.println(" " + e.getMessage());
        }
        if (anz == 0) {
            return (CaFehler.pfXyNichtVorhanden);
        }

        return (1);

    }

    public int readZuMeldungsIdentGast(int meldungsIdentGast) {
        int anz = 0;
        ergebnisArray = null;
        String sql = "SELECT * from " + dbBundle.getSchemaMandant()
                + "tbl_zutrittskarten zk where zk.meldungsIdentGast=? and zk.mandant=?";
        try {

            PreparedStatement pstm1 = verbindung.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
            pstm1.setInt(1, meldungsIdentGast); /*TODO $$Prüfen: war ursprünglich String-Umwandlung*/
            pstm1.setInt(2, dbBundle.clGlobalVar.mandant);
            ResultSet ergebnis = pstm1.executeQuery();

            ergebnis.last();
            anz = ergebnis.getRow();
            ergebnis.beforeFirst();
            ergebnisArray = new EclZutrittskarten[anz];

            int i = 0;
            while (ergebnis.next() == true) {
                ergebnisArray[i] = decodeErgebnis(ergebnis);
                i++;
            }
            ergebnis.close();
            pstm1.close();

        } catch (Exception e) {
            CaBug.drucke("DbZutrittskarten.readZuMeldungsIdentGast 001");
            System.err.println(" " + e.getMessage());
        }
        if (anz == 0) {
            return (CaFehler.pfXyNichtVorhanden);
        }

        return (1);

    }

    public int readZuMeldungsIdentAktionaer(int meldungsIdentAktionaer) {
        int anz = 0;
        ergebnisArray = null;

        String sql = "SELECT * from " + dbBundle.getSchemaMandant()
                + "tbl_zutrittskarten zk where zk.meldungsIdentAktionaer=? and zk.mandant=?";
        try {

            PreparedStatement pstm1 = verbindung.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
            pstm1.setInt(1, meldungsIdentAktionaer); /*TODO $$Prüfen: war ursprünglich String-Umwandlung*/
            pstm1.setInt(2, dbBundle.clGlobalVar.mandant);
            ResultSet ergebnis = pstm1.executeQuery();

            ergebnis.last();
            anz = ergebnis.getRow();
            ergebnis.beforeFirst();
            ergebnisArray = new EclZutrittskarten[anz];

            int i = 0;
            while (ergebnis.next() == true) {
                ergebnisArray[i] = decodeErgebnis(ergebnis);
                i++;
            }
            ergebnis.close();
            pstm1.close();

        } catch (Exception e) {
            CaBug.drucke("DbZutrittskarten.readZuMeldungsIdentAktionaer 001");
            System.err.println(" " + e.getMessage());
        }
        if (anz == 0) {
            return (CaFehler.pfXyNichtVorhanden);
        }

        return (1);

    }

    /**Lesen aller ZutrittsIdent (auch gesperrte) >=pVon und <=pBis.
     * pVon / pBis müssen über BlNummernformat aufbereitet sein.
     */
    public int readVonBis(String pVon, String pBis) {
        int anz = 0;
        ergebnisArray = null;

        String sql = "SELECT * from " + dbBundle.getSchemaMandant()
                + "tbl_zutrittskarten zk where zk.zutrittsIdent>=? and zk.zutrittsIdent<=? AND zk.mandant=?";
        try {

            PreparedStatement pstm1 = verbindung.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
            pstm1.setString(1, pVon);
            pstm1.setString(2, pBis);
            pstm1.setInt(3, dbBundle.clGlobalVar.mandant);
            ResultSet ergebnis = pstm1.executeQuery();

            ergebnis.last();
            anz = ergebnis.getRow();
            ergebnis.beforeFirst();
            ergebnisArray = new EclZutrittskarten[anz];

            int i = 0;
            while (ergebnis.next() == true) {
                ergebnisArray[i] = decodeErgebnis(ergebnis);
                i++;
            }
            ergebnis.close();
            pstm1.close();

        } catch (Exception e) {
            CaBug.drucke("DbZutrittskarten.readZuMeldungsIdentAktionaer 001");
            System.err.println(" " + e.getMessage());
        }
        if (anz == 0) {
            return (CaFehler.pfXyNichtVorhanden);
        }

        return (1);

    }

    public int insert(EclZutrittskarten lZutrittsKarten) {
        int erg = 0;

        if (ParamSpezial.ku178(dbBundle.clGlobalVar.mandant)) {
            lZutrittsKarten.personenNatJurIdent = -1;
        }

        try {

            /*Felder Neuanlage füllen*/
            String sql1 = "INSERT INTO " + dbBundle.getSchemaMandant() + "tbl_zutrittskarten " + "("
                    + "mandant, zutrittsIdent, zutrittsIdentNeben, "
                    + "db_version, zutrittsIdentKlasse, delayedVorhanden, zutrittsIdentIstGesperrt, zutrittsIdentIstGesperrt_Delayed, zutrittsIdentVers, zutrittsIdentVers_Delayed,"
                    + "meldungsIdentGast, meldungsIdentAktionaer, gueltigeKlasse, gueltigeKlasse_Delayed, "
                    + "personenNatJurIdent, personenNatJurIdent_Delayed, ausgestelltAufPersonenNatJurIdent)" +

                    "VALUES (" + "?, ?, ?, " + "?, ?, ?, ?, ?, ?, ?, " + "?, ?, ?, ?, " + "?, ?, ?)";
            PreparedStatement pstm1 = verbindung.prepareStatement(sql1, ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);

            fuellePreparedStatementKomplett(pstm1, 1, lZutrittsKarten);

            erg = 0; /*Falls Duplicate Key, dann wird exception geschmissen und erg wohl nicht gefüllt!*/
            erg = pstm1.executeUpdate();
            pstm1.close();
        } catch (Exception e2) {
            CaBug.drucke("DbZutrittskarten.insert 001");
            System.err.println(" " + e2.getMessage());
        }

        if (erg == 0) {/*Fehler beim Einfügen - d.h. ZutrittsIdent / zutrittsIdentKlasse bereits vorhanden*/
            return (CaFehler.pfdXyBereitsVorhanden);
        }

        return (1);
    }

    public int update(EclZutrittskarten lZutrittsKartenNeu) {

        lZutrittsKartenNeu.db_version++;

        try {

            String sql = "UPDATE " + dbBundle.getSchemaMandant() + "tbl_zutrittskarten SET " + "mandant=?, "
                    + "zutrittsIdent=?, zutrittsIdentNeben=?, db_version=?, zutrittsIdentKlasse=?, delayedVorhanden=?, zutrittsIdentIstGesperrt=?, zutrittsIdentIstGesperrt_Delayed=?, zutrittsIdentVers=?, zutrittsIdentVers_Delayed=?, "
                    + "meldungsIdentGast=?, meldungsIdentAktionaer=?, gueltigeKlasse=?, gueltigeKlasse_Delayed=?, "
                    + "personenNatJurIdent=?, personenNatJurIdent_Delayed=?, ausgestelltAufPersonenNatJurIdent=? "
                    + "WHERE " + "mandant=? AND " + "zutrittsIdent=? AND zutrittsIdentNeben=? AND db_version=?";

            PreparedStatement pstm1 = verbindung.prepareStatement(sql);
            fuellePreparedStatementKomplett(pstm1, 1, lZutrittsKartenNeu);
            pstm1.setInt(anzfelder + 1, dbBundle.clGlobalVar.mandant);
            pstm1.setString(anzfelder + 2, lZutrittsKartenNeu.zutrittsIdent);
            pstm1.setString(anzfelder + 3, lZutrittsKartenNeu.zutrittsIdentNeben);
            pstm1.setLong(anzfelder + 4, lZutrittsKartenNeu.db_version - 1);

            int ergebnis1 = pstm1.executeUpdate();
            pstm1.close();
            if (ergebnis1 == 0) {
                return (CaFehler.pfXyWurdeVonAnderemBenutzerVeraendert);
            }
        } catch (Exception e1) {
            CaBug.drucke("DbZutrittskarten.update 001");
            System.err.println(" " + e1.getMessage());
            return (CaFehler.pfdXyBereitsVorhanden);

        }

        return (1);
    }

    /****************************************************************************************************************************************/
    /**************************************Methoden zum Auflösen der Delayed-ZutrittsIdent***************************************************/
    /****************************************************************************************************************************************/
    /**Hier wird die jeweils nächste eingelesen und zu verarbeitende delayed Willenserklärung von diesen Methoden abgelegt*/
    public EclZutrittskarten rcDelayedZutrittskarten = null;

    /**Anzahl der zu verarbeitenden Delayed-Meldungen - ist nach aufloesenDelayed_init gefüllt*/
    public int rcDelayedAnzahl = 0;

    private ResultSet delayedErgebnis = null;
    private PreparedStatement delayedPstm = null;

    /**Initialisierung - setzt Select ab*/
    public int initAufloesenDelayed() {
        try {

            String sql = "SELECT * from " + dbBundle.getSchemaMandant() + "tbl_zutrittskarten zk where "
                    + "zk.mandant=? AND zk.delayedVorhanden=1;";

            delayedPstm = verbindung.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
            delayedPstm.setInt(1, dbBundle.clGlobalVar.mandant);

            delayedErgebnis = delayedPstm.executeQuery();
            delayedErgebnis.last();
            rcDelayedAnzahl = delayedErgebnis.getRow();
            delayedErgebnis.beforeFirst();

        } catch (Exception e) {
            CaBug.drucke("DbZutrittskarten.initAufloesenDelayed 001");
            System.err.println(" " + e.getMessage());
        }

        return (rcDelayedAnzahl);
    }

    /**Liefere nächste Willenserklärung in rcDelayedZutrittskarten ab
     * liefert 0, wenn Dateiende erreicht*/
    public int readNextDelayed() {
        try {
            if (delayedErgebnis.next() == false) {
                delayedErgebnis.close();
                delayedPstm.close();
                return (0);
            }
            rcDelayedZutrittskarten = this.decodeErgebnis(delayedErgebnis);

        } catch (Exception e) {
            CaBug.drucke("DbZutrittskarten.readNextDelayed 001");
            System.err.println(" " + e.getMessage());
        }
        return 1;
    }

}
