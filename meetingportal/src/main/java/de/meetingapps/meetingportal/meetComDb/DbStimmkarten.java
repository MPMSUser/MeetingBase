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
import de.meetingapps.meetingportal.meetComEntities.EclStimmkarten;
import de.meetingapps.meetingportal.meetComEntities.EclZutrittsIdent;
import de.meetingapps.meetingportal.meetComKonst.KonstKartenklasse;

public class DbStimmkarten {
    private DbBundle dbBundle = null;
    private Connection verbindung = null;
    //	private DbBasis dbBasis=null;

    private EclStimmkarten ergebnisArray[] = null;

    /*************************Initialisierung***************************/
    public DbStimmkarten(DbBundle pDbBundle) {
        /* Verbindung in lokale Daten eintragen*/
        if (pDbBundle == null) {
            CaBug.drucke("DbAktienregisterZusatz.init 001 - dbBundle nicht initialisiert");
            return;
        }
        if (pDbBundle.dbBasis == null) {
            CaBug.drucke("DbAktienregisterZusatz.init 002 - dbBasis nicht initialisiert");
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

    public EclStimmkarten ergebnisPosition(int lfd) {
        if (ergebnisArray == null) {
            CaBug.drucke("DbStimmkarten.ergebnisPosition 001");
            return null;
        }
        if (lfd < 0) {
            CaBug.drucke("DbStimmkarten.ergebnisPosition 002");
            return null;
        }
        if (lfd >= ergebnisArray.length) {
            CaBug.drucke("DbStimmkarten.ergebnisPosition 003");
            return null;
        }
        return ergebnisArray[lfd];
    }

    public EclStimmkarten[] ergebnis() {
        if (ergebnisArray == null) {
            return new EclStimmkarten[0];
        }
        return ergebnisArray;
    }

     
    public int createTable() {
        int rc = 0;
        DbLowLevel lDbLowLevel = new DbLowLevel(dbBundle);
        rc = lDbLowLevel.createTable("CREATE TABLE " + dbBundle.getSchemaMandant() + "tbl_stimmkarten ( "
                + "`mandant` int(11) NOT NULL, " + "`stimmkarte` char(20) NOT NULL, "
                + "`db_version` bigint(20) DEFAULT NULL, " + "`ausSubnummernkreis` int(11) DEFAULT NULL, "
                + "`delayedVorhanden` int(11) DEFAULT NULL, " + "`stimmkarteIstGesperrt` int(11) DEFAULT NULL, "
                + "`stimmkarteIstGesperrt_Delayed` int(11) DEFAULT NULL, " + "`stimmkarteVers` int(11) NOT NULL, "
                + "`stimmkarteVers_Delayed` int(11) NOT NULL, " + "`meldungsIdentGast` int(11) DEFAULT NULL, "
                + "`meldungsIdentAktionaer` int(11) DEFAULT NULL, " + "`gueltigeKlasse` int(11) DEFAULT NULL, "
                + "`gueltigeKlasse_Delayed` int(11) DEFAULT NULL, " + "`personenNatJurIdent` int(11) DEFAULT NULL, "
                + "`personenNatJurIdent_Delayed` int(11) DEFAULT NULL, " + "`zutrittsIdent` char(20) DEFAULT NULL, "
                + "`zutrittsIdentNeben` char(2) DEFAULT NULL, "
                + "PRIMARY KEY (`mandant`,`stimmkarte`,`stimmkarteVers`,`stimmkarteVers_Delayed`), "
                + "KEY `IDX_stimmkarte` (`stimmkarte`), " + "KEY `IDX_meldungsIdentGast` (`meldungsIdentGast`), "
                + "KEY `IDX_meldungsIdentAktionaer` (`meldungsIdentAktionaer`) " + ") ");
        return rc;
    }

    /**************************deleteAll Löschen aller Datensätze  eines Mandanten****************/
    public int deleteAll() {
        dbBundle.dbBasis.resetGastkartennummer();
        for (int i = 1; i <= 5; i++) {
            dbBundle.dbBasis.resetStimmkartennummer(i);
        }
        return dbBundle.dbLowLevel
                .deleteMandant("DELETE FROM " + dbBundle.getSchemaMandant() + "tbl_stimmkarten where mandant=?;");
    }

    /**************************setzt aktuelle Mandantennummer bei allen Datensätzen****************/
    public int updateMandant() {
        return dbBundle.dbLowLevel.rawUpdateMandant(dbBundle.getSchemaMandant() + "tbl_stimmkarten");
    }

    public void reorgInterneIdent() {
        String von, bis;
        int lMax = 0;
        /*Aktionärs-StimmkartenIdents Auto für alle Gattungen*/
        for (int i = 1; i <= 5; i++) {
            von = CaString.fuelleLinksNull(
                    Integer.toString(dbBundle.param.paramNummernkreise.vonSubStimmkartennummer[i][5]),
                    dbBundle.param.paramNummernkreise.laengeKartennummer[KonstKartenklasse.stimmkartennummer]);
            bis = CaString.fuelleLinksNull(
                    Integer.toString(dbBundle.param.paramNummernkreise.bisSubStimmkartennummer[i][5]),
                    dbBundle.param.paramNummernkreise.laengeKartennummer[KonstKartenklasse.stimmkartennummer]);
            lMax = dbBundle.dbLowLevel.liefereHoechsteIdentString("SELECT MAX(stimmkarte) FROM "
                    + dbBundle.getSchemaMandant() + "tbl_stimmkarten ab where ab.mandant=? AND ab.stimmkarte>=" + von
                    + " AND ab.stimmkarte<=" + bis);
            if (lMax == -1) {
                lMax = 0;
            }
            dbBundle.dbBasis.resetStimmkartennummer(i, lMax);
        }
    }

    /** dekodiert die aktuelle Position aus ergebnis in EvlVIpKZ und gibt dieses zurück*/
    EclStimmkarten decodeErgebnis(ResultSet ergebnis) {
        EclStimmkarten lStimmKarten = new EclStimmkarten();

        try {

            lStimmKarten.mandant = ergebnis.getInt("sk.mandant");

            lStimmKarten.stimmkarte = ergebnis.getString("sk.stimmkarte");
            lStimmKarten.db_version = ergebnis.getLong("sk.db_version");

            lStimmKarten.ausSubnummernkreis = ergebnis.getInt("sk.ausSubnummernkreis");

            lStimmKarten.delayedVorhanden = ergebnis.getInt("sk.delayedVorhanden");
            lStimmKarten.stimmkarteIstGesperrt = ergebnis.getInt("sk.stimmkarteIstGesperrt");
            lStimmKarten.stimmkarteIstGesperrt_Delayed = ergebnis.getInt("sk.stimmkarteIstGesperrt_Delayed");
            lStimmKarten.stimmkarteVers = ergebnis.getInt("sk.stimmkarteVers");
            lStimmKarten.stimmkarteVers_Delayed = ergebnis.getInt("sk.stimmkarteVers_Delayed");

            lStimmKarten.meldungsIdentGast = ergebnis.getInt("sk.meldungsIdentGast");
            lStimmKarten.meldungsIdentAktionaer = ergebnis.getInt("sk.meldungsIdentAktionaer");
            lStimmKarten.gueltigeKlasse = ergebnis.getInt("sk.gueltigeKlasse");
            lStimmKarten.gueltigeKlasse_Delayed = ergebnis.getInt("sk.gueltigeKlasse_Delayed");

            lStimmKarten.personenNatJurIdent = ergebnis.getInt("sk.personenNatJurIdent");
            lStimmKarten.personenNatJurIdent_Delayed = ergebnis.getInt("sk.personenNatJurIdent_Delayed");

            lStimmKarten.zutrittsIdent = ergebnis.getString("sk.zutrittsIdent");
            lStimmKarten.zutrittsIdentNeben = ergebnis.getString("sk.zutrittsIdentNeben");

        } catch (Exception e) {
            CaBug.drucke("DbStimmkarten.decodeErgebnis 001");
            System.err.println(" " + e.getMessage());
        }

        return lStimmKarten;
    }

    /********************* Fuellen Prepared Statement mit allen Feldern, beginnend bei offset.*****************
     * Kann sowohl für Insert, als auch für update verwendet werden.
     * 
     * offset= Startposition des ersten Feldes (also z.B. 1)
     */
    private int anzfelder = 17; /*Anpassen auf Anzahl der Felder pro Datensatz*/

    private void fuellePreparedStatementKomplett(PreparedStatement pPStm, int pOffset, EclStimmkarten lStimmKarten) {

        int startOffset = pOffset; /*Nur erforderlich zum Überprüfen von Programmierfehlern - Feldanzahl*/

        lStimmKarten.mandant = dbBundle.clGlobalVar.mandant;

        try {
            pPStm.setInt(pOffset, lStimmKarten.mandant);
            pOffset++;
            pPStm.setString(pOffset, lStimmKarten.stimmkarte);
            pOffset++;
            pPStm.setLong(pOffset, lStimmKarten.db_version);
            pOffset++;
            pPStm.setInt(pOffset, lStimmKarten.ausSubnummernkreis);
            pOffset++;
            pPStm.setInt(pOffset, lStimmKarten.delayedVorhanden);
            pOffset++;
            pPStm.setInt(pOffset, lStimmKarten.stimmkarteIstGesperrt);
            pOffset++;
            pPStm.setInt(pOffset, lStimmKarten.stimmkarteIstGesperrt_Delayed);
            pOffset++;
            pPStm.setInt(pOffset, lStimmKarten.stimmkarteVers);
            pOffset++;
            pPStm.setInt(pOffset, lStimmKarten.stimmkarteVers_Delayed);
            pOffset++;
            pPStm.setInt(pOffset, lStimmKarten.meldungsIdentGast);
            pOffset++;
            pPStm.setInt(pOffset, lStimmKarten.meldungsIdentAktionaer);
            pOffset++;
            pPStm.setInt(pOffset, lStimmKarten.gueltigeKlasse);
            pOffset++;
            pPStm.setInt(pOffset, lStimmKarten.gueltigeKlasse_Delayed);
            pOffset++;
            pPStm.setInt(pOffset, lStimmKarten.personenNatJurIdent);
            pOffset++;
            pPStm.setInt(pOffset, lStimmKarten.personenNatJurIdent_Delayed);
            pOffset++;
            pPStm.setString(pOffset, lStimmKarten.zutrittsIdent);
            pOffset++;
            pPStm.setString(pOffset, lStimmKarten.zutrittsIdentNeben);
            pOffset++;

            if (pOffset - startOffset != anzfelder) {
                /*Nur wg. Überprüfung auf Programmierfehler - alle Felder berücksichtigt?*/
                CaBug.drucke("DbStimmkarten.fuellePreparedStatementKomplett 002");
            }

        } catch (SQLException e) {
            CaBug.drucke("DbStimmkarten.fuellePreparedStatementKomplett 001");
            e.printStackTrace();
        }

    }

    /**Liefert alle Stimmkarten, auch die mit Version !=0*/
    public int read_alleVersionen(String pStimmkarte) {
        int anz = 0;
        ergebnisArray = null;

        String sql = "SELECT * from " + dbBundle.getSchemaMandant()
                + "tbl_stimmkarten sk where sk.stimmkarte=? AND sk.mandant=?";
        try {
            PreparedStatement pstm1 = verbindung.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
            pstm1.setString(1, pStimmkarte);
            pstm1.setInt(2, dbBundle.clGlobalVar.mandant);
            ResultSet ergebnis = pstm1.executeQuery();

            ergebnis.last();
            anz = ergebnis.getRow();
            ergebnis.beforeFirst();
            ergebnisArray = new EclStimmkarten[anz];

            int i = 0;
            while (ergebnis.next() == true) {
                ergebnisArray[i] = decodeErgebnis(ergebnis);
                i++;
            }
            ergebnis.close();
            pstm1.close();
        } catch (Exception e) {
            CaBug.drucke("DbStimmkarten.read_alleVersionen 001");
            System.err.println(" " + e.getMessage());
            return (-1);
        }
        return (anz);
    }

    /**Liefert aktive und inaktive/gesperrte, aber nicht versionierte.
     * Es wird immer nur die aktuelle Version geliefert (nicht delayed)*/
    public int read(String stimmkarte) {
        return read(stimmkarte, 1);
    }

    /**Liefert aktive und inaktive/gesperrte, aber nicht versionierte.
     * Delayed-Versionierung wird je nach Parameter aktuellOderDelayed berücksichtigt*/
    public int read(String stimmkarte, int aktuellOderDelayed) {

        int anz = 0;
        ergebnisArray = null;

        String sql = "SELECT * from " + dbBundle.getSchemaMandant()
                + "tbl_stimmkarten sk where sk.stimmkarte=? and sk.mandant=? ";
        if (aktuellOderDelayed == 1) {
            sql = sql + "and sk.stimmkarteVers=0 ";
        } else {
            sql = sql + "and sk.stimmkarteVers_Delayed=0 ";
        }
        try {

            PreparedStatement pstm1 = verbindung.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
            pstm1.setString(1, stimmkarte);
            pstm1.setInt(2, dbBundle.clGlobalVar.mandant);
            ResultSet ergebnis = pstm1.executeQuery();

            ergebnis.last();
            anz = ergebnis.getRow();
            ergebnis.beforeFirst();
            ergebnisArray = new EclStimmkarten[anz];

            int i = 0;
            while (ergebnis.next() == true) {
                ergebnisArray[i] = decodeErgebnis(ergebnis);
                i++;
            }
            ergebnis.close();
            pstm1.close();

        } catch (Exception e) {
            CaBug.drucke("DbStimmkarten.read 001");
            System.err.println(" " + e.getMessage());
        }

        return (anz);
    }

    /**Liefert alle Stimmkarten zu einer Meldung. Egal ob als Gast oder als Aktionär zugeordnet. Auch "Versionierte" und gesperrte.
     * Dies ist ab sofort so gewollt :-), aber es ist noch nicht untersucht,
     * ob nicht aufrufende Funktionen davon ausgehen, dass nur gültige Idents gemeldet werden
     */
    public int readZuMeldungsIdent(int meldungsIdent) {
        int anz = 0;
        ergebnisArray = null;

        String sql = "SELECT * from " + dbBundle.getSchemaMandant()
                + "tbl_stimmkarten sk where (sk.meldungsIdentGast=? OR sk.meldungsIdentAktionaer=?) and sk.mandant=?";
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
            ergebnisArray = new EclStimmkarten[anz];

            int i = 0;
            while (ergebnis.next() == true) {
                ergebnisArray[i] = decodeErgebnis(ergebnis);
                i++;
            }
            ergebnis.close();
            pstm1.close();
        } catch (Exception e) {
            CaBug.drucke("DbStimmkarten.readZuMeldungsIdent 001");
            System.err.println(" " + e.getMessage());
        }
        return (anz);
    }

    /**Liefert alle Stimmkarten zu einer ZutrittsIdent. Egal ob als Gast oder als Aktionär zugeordnet. Nur gültige (also nicht
     * versionierte, und nicht gesperrte). Liefert alle (also auch delayte)*/
    public int readGueltigeZuZutrittsIdent(EclZutrittsIdent zutrittsIdent) {
        int anz = 0;
        ergebnisArray = null;

        String sql = "SELECT * from " + dbBundle.getSchemaMandant()
                + "tbl_stimmkarten sk where sk.zutrittsIdent=? AND sk.zutrittsIdentNeben=? AND (sk.stimmkarteIstGesperrt=0 OR sk.stimmkarteIstGesperrt=1) AND sk.mandant=?";
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
            ergebnisArray = new EclStimmkarten[anz];

            int i = 0;
            while (ergebnis.next() == true) {
                ergebnisArray[i] = decodeErgebnis(ergebnis);
                i++;
            }
            ergebnis.close();
            pstm1.close();
        } catch (Exception e) {
            CaBug.drucke("DbStimmkarten.readGueltigeZuZutrittsIdent 001");
            System.err.println(" " + e.getMessage());
        }
        return (anz);
    }

    /**Liefert alle "aktiv gültigen (gueltigeKlasse)" Stimmkarten zu einer meldungsIdent und einer bestimmten Person. Egal ob als Gast oder als Aktionär zugeordnet. Nur gültige (also nicht
     * versionierte, und nicht gesperrte). Liefert alle (also auch delayte)*/
    public int readGueltigeZuMeldungPerson(int pMeldeIdent, int pPersonNatJurIdent) {
        int anz = 0;
        ergebnisArray = null;

        String sql = "SELECT * from " + dbBundle.getSchemaMandant() + "tbl_stimmkarten sk where "
                + "((sk.meldungsIdentGast=? AND sk.gueltigeKlasse=0) OR (sk.meldungsIdentAktionaer=? AND sk.gueltigeKlasse=1)) "
                + "AND sk.personenNatJurIdent=? AND (sk.stimmkarteIstGesperrt=0 OR sk.stimmkarteIstGesperrt=1) AND sk.mandant=?";
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
            ergebnisArray = new EclStimmkarten[anz];

            int i = 0;
            while (ergebnis.next() == true) {
                ergebnisArray[i] = decodeErgebnis(ergebnis);
                i++;
            }
            ergebnis.close();
            pstm1.close();
        } catch (Exception e) {
            CaBug.drucke("DbStimmkarten.readGueltigeZuMeldungPerson 001");
            System.err.println(" " + e.getMessage());
        }
        return (anz);
    }

    public int insert(EclStimmkarten lStimmKarten) {
        int erg = 0;
        lStimmKarten.mandant = dbBundle.clGlobalVar.mandant;
        try {

            /*Felder Neuanlage füllen*/
            String sql1 = "INSERT INTO " + dbBundle.getSchemaMandant() + "tbl_stimmkarten " + "(" + "mandant, "
                    + "stimmkarte, db_version, ausSubnummernkreis, delayedVorhanden, "
                    + "stimmkarteIstGesperrt, stimmkarteIstGesperrt_Delayed, stimmkarteVers, stimmkarteVers_Delayed,"
                    + "meldungsIdentGast, meldungsIdentAktionaer, gueltigeKlasse, gueltigeKlasse_Delayed, "
                    + "personenNatJurIdent, personenNatJurIdent_Delayed, zutrittsIdent, zutrittsIdentNeben " + ")" +

                    "VALUES (" + "?, " + "?, ?, ?, ?, ?, " + "?, ?, ?, ?, " + "?, ?, ?, ?, " + "?, ?, ? " + ")";
            PreparedStatement pstm1 = verbindung.prepareStatement(sql1, ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);

            fuellePreparedStatementKomplett(pstm1, 1, lStimmKarten);

            erg = 0; /*Falls Duplicate Key, dann wird exception geschmissen und erg wohl nicht gefüllt!*/
            erg = pstm1.executeUpdate();
            pstm1.close();
        } catch (Exception e2) {
            CaBug.drucke("DbStimmkarten.insert 001");
            System.err.println(" " + e2.getMessage());
        }

        if (erg == 0) {/*Fehler beim Einfügen - d.h. ZutrittsIdent / zutrittsIdentKlasse bereits vorhanden*/
            return (CaFehler.pfdXyBereitsVorhanden);
        }
        return (1);
    }

    public int delete(EclStimmkarten lStimmKartenNeu) {

        try {

            String sql = "DELETE FROM " + dbBundle.getSchemaMandant()
                    + "tbl_stimmkarten WHERE stimmkarte=? AND mandant=? ";

            PreparedStatement pstm1 = verbindung.prepareStatement(sql);
            pstm1.setString(1, lStimmKartenNeu.stimmkarte);
            pstm1.setInt(2, dbBundle.clGlobalVar.mandant);

            int ergebnis1 = pstm1.executeUpdate();
            pstm1.close();
            if (ergebnis1 == 0) {
                return (CaFehler.pfXyWurdeVonAnderemBenutzerVeraendert);
            }
        } catch (Exception e1) {
            CaBug.drucke("DbStimmkarten.delete 001");
            System.err.println(" " + e1.getMessage());
            return (CaFehler.pfdXyBereitsVorhanden);
        }

        return (1);
    }

    public int update(EclStimmkarten lStimmKartenNeu) {

        lStimmKartenNeu.db_version++;

        try {

            String sql = "UPDATE " + dbBundle.getSchemaMandant() + "tbl_stimmkarten SET " + "mandant=?, "
                    + "stimmkarte=?, db_version=?, ausSubnummernkreis=?, delayedVorhanden=?, "
                    + "stimmkarteIstGesperrt=?, stimmkarteIstGesperrt_Delayed=?, stimmkarteVers=?, stimmkarteVers_Delayed=?, "
                    + "meldungsIdentGast=?, meldungsIdentAktionaer=?, gueltigeKlasse=?, gueltigeKlasse_Delayed=?, "
                    + "personenNatJurIdent=?, personenNatJurIdent_Delayed=?, zutrittsIdent=?, zutrittsIdentNeben=? "
                    + "WHERE " + "mandant=? AND stimmkarte=? AND db_version=? ";

            PreparedStatement pstm1 = verbindung.prepareStatement(sql);
            fuellePreparedStatementKomplett(pstm1, 1, lStimmKartenNeu);
            pstm1.setInt(anzfelder + 1, dbBundle.clGlobalVar.mandant);
            pstm1.setString(anzfelder + 2, lStimmKartenNeu.stimmkarte);
            pstm1.setLong(anzfelder + 3, lStimmKartenNeu.db_version - 1);

            int ergebnis1 = pstm1.executeUpdate();
            pstm1.close();
            if (ergebnis1 == 0) {
                return (CaFehler.pfXyWurdeVonAnderemBenutzerVeraendert);
            }
        } catch (Exception e1) {
            CaBug.drucke("DbStimmkarten.update 001");
            System.err.println(" " + e1.getMessage());
            return (CaFehler.pfdXyBereitsVorhanden);

        }

        return (1);
    }

    /****************************************************************************************************************************************/
    /**************************************Methoden zum Auflösen der Delayed-Stimmkarten***************************************************/
    /****************************************************************************************************************************************/
    /**Hier wird die jeweils nächste eingelesen und zu verarbeitende delayed Willenserklärung von diesen Methoden abgelegt*/
    public EclStimmkarten rcDelayedStimmkarten = null;

    /**Anzahl der zu verarbeitenden Delayed-Meldungen - ist nach aufloesenDelayed_init gefüllt*/
    public int rcDelayedAnzahl = 0;

    private ResultSet delayedErgebnis = null;
    private PreparedStatement delayedPstm = null;

    /**Initialisierung - setzt Select ab*/
    public int initAufloesenDelayed() {
        try {

            String sql = "SELECT * from " + dbBundle.getSchemaMandant() + "tbl_stimmkarten sk where "
                    + "sk.mandant=? AND sk.delayedVorhanden=1;";

            delayedPstm = verbindung.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
            delayedPstm.setInt(1, dbBundle.clGlobalVar.mandant);

            delayedErgebnis = delayedPstm.executeQuery();
            delayedErgebnis.last();
            rcDelayedAnzahl = delayedErgebnis.getRow();
            delayedErgebnis.beforeFirst();

        } catch (Exception e) {
            CaBug.drucke("DbStimmkarten.initAufloesenDelayed 001");
            System.err.println(" " + e.getMessage());
        }
        return (rcDelayedAnzahl);
    }

    /**Liefere nächste Willenserklärung in rcDelayedStimmkarten ab
     * liefert 0, wenn Dateiende erreicht*/
    public int readNextDelayed() {
        try {
            if (delayedErgebnis.next() == false) {
                delayedErgebnis.close();
                delayedPstm.close();
                return (0);
            }
            rcDelayedStimmkarten = this.decodeErgebnis(delayedErgebnis);

        } catch (Exception e) {
            CaBug.drucke("DbStimmkarten.readNextDelayed 001");
            System.err.println(" " + e.getMessage());
        }
        return 1;
    }

}
