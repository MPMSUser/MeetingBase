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
import java.text.NumberFormat;
import java.util.LinkedList;
import java.util.List;

import de.meetingapps.meetingportal.meetComAllg.CaBug;
import de.meetingapps.meetingportal.meetComAllg.CaFehler;
import de.meetingapps.meetingportal.meetComEntities.EclAbstimmungenEinzelAusschluss;
import de.meetingapps.meetingportal.meetComEntities.EclMeldung;
import de.meetingapps.meetingportal.meetComHVParam.ParamS;

public class DbAbstimmungenEinzelAusschluss extends DbRootExecute {

    private Connection verbindung = null;
    private DbBasis dbBasis = null;
    private DbBundle dbBundle = null;

    EclAbstimmungenEinzelAusschluss ergebnisArray[] = null;

    /************************* Initialisierung ***************************/
    public DbAbstimmungenEinzelAusschluss(DbBundle pDbBundle) {
        /* Verbindung in lokale Daten eintragen */
        if (pDbBundle == null) {
            CaBug.drucke("DbAbstimmungenEinzelAusschluss.init 001 - dbBundle nicht initialisiert");
            return;
        }
        if (pDbBundle.dbBasis == null) {
            CaBug.drucke("DbAbstimmungenEinzelAusschluss.init 002 - dbBasis nicht initialisiert");
            return;
        }

        dbBasis = pDbBundle.dbBasis;
        verbindung = pDbBundle.dbBasis.verbindung;
        dbBundle = pDbBundle;
    }

    /**************
     * Anzahl der Ergebnisse der read*-Methoden
     **************************/
    public int anzErgebnis() {
        if (ergebnisArray == null) {
            return 0;
        }
        return ergebnisArray.length;
    }

    /**************
     * Anzahl der Ergebnisse der read*-Methoden
     **************************/
    public EclAbstimmungenEinzelAusschluss[] ergebnis() {
        return ergebnisArray;
    }

    /**********
     * Liefert pN-tes Element des Ergebnisses der read*Methoden************** pN
     * geht von 0 bis anzErgebnis-1
     */
    public EclAbstimmungenEinzelAusschluss ergebnisPosition(int pN) {
        if (ergebnisArray == null) {
            CaBug.drucke("DbAbstimmungenEinzelAusschluss.ergebnisPosition 001");
            return null;
        }
        if (pN < 0) {
            CaBug.drucke("DbAbstimmungenEinzelAusschluss.ergebnisPosition 002");
            return null;
        }
        if (pN >= ergebnisArray.length) {
            CaBug.drucke("DbAbstimmungenEinzelAusschluss.ergebnisPosition 003");
            return null;
        }
        return ergebnisArray[pN];
    }

    public int createTable() {
        int rc = 0;
        DbLowLevel lDbLowLevel = new DbLowLevel(dbBundle);
        rc = lDbLowLevel
                .createTable("CREATE TABLE " + dbBundle.getSchemaMandant() + "tbl_abstimmungeneinzelausschluss ( "
                        + "`mandant` int(11) NOT NULL, " + "`abstimmungsIdent` int(11) NOT NULL, "
                        + "`db_version` bigint(20) DEFAULT NULL, " + "`identMeldung` int(11) NOT NULL, "
                        + "PRIMARY KEY (`abstimmungsIdent`,`identMeldung`,`mandant`) " + ")  "

                );
        return rc;
    }

    /**************************
     * deleteAll Löschen aller Datensätze eines Mandanten
     ****************/
    public int deleteAll() {
        int erg = 0;

        try {

            String sql1 = "DELETE FROM " + dbBundle.getSchemaMandant()
                    + "tbl_abstimmungenEinzelAusschluss where mandant=?;";
            PreparedStatement pstm1 = verbindung.prepareStatement(sql1);
            pstm1.setInt(1, dbBundle.clGlobalVar.mandant);

            erg = 0; /*
                      * Falls Duplicate Key, dann wird exception geschmissen und erg wohl nicht
                      * gefüllt!
                      */
            erg = executeUpdate(pstm1);

            pstm1.close();
        } catch (Exception e2) {
            CaBug.drucke("DbAbstimmungenEinzelAusschluss.deleteAll 001");
            System.err.println(" " + e2.getMessage());
            return (erg);
        }

        return 1;
    }

    /**************************
     * setzt aktuelle Mandantennummer bei allen Datensätzen
     ****************/
    public int updateMandant() {
        return dbBundle.dbLowLevel.rawUpdateMandant(dbBundle.getSchemaMandant() + "tbl_abstimmungenEinzelAusschluss");
    }

    /**********
     * dekodiert die aktuelle Position aus ergebnis und gibt dieses zurück
     ******/
    EclAbstimmungenEinzelAusschluss decodeErgebnis(ResultSet pErgebnis) {

        EclAbstimmungenEinzelAusschluss lAbstimmungenEinzelAusschluss = new EclAbstimmungenEinzelAusschluss();

        try {
            lAbstimmungenEinzelAusschluss.mandant = pErgebnis.getInt("abEA.mandant");
            lAbstimmungenEinzelAusschluss.abstimmungsIdent = pErgebnis.getInt("abEA.abstimmungsIdent");
            lAbstimmungenEinzelAusschluss.db_version = pErgebnis.getLong("abEA.db_version");
            lAbstimmungenEinzelAusschluss.identMeldung = pErgebnis.getInt("abEA.identMeldung");
        } catch (Exception e) {
            CaBug.drucke("DbAbstimmungenEinzelAusschluss.decodeErgebnis 001");
            System.err.println(" " + e.getMessage());
        }

        return lAbstimmungenEinzelAusschluss;
    }

    /*********************
     * Fuellen Prepared Statement mit allen Feldern, beginnend bei
     * offset.***************** Kann sowohl für Insert, als auch für update
     * verwendet werden.
     * 
     * offset= Startposition des ersten Feldes (also z.B. 1)
     */
    private int anzfelder = 4; /* Anpassen auf Anzahl der Felder pro Datensatz */

    private void fuellePreparedStatementKomplett(PreparedStatement pPStm, int pOffset,
            EclAbstimmungenEinzelAusschluss lAbstimmungenEinzelAusschluss) {

        int startOffset = pOffset; /* Nur erforderlich zum Überprüfen von Programmierfehlern - Feldanzahl */

        try {
            pPStm.setInt(pOffset, lAbstimmungenEinzelAusschluss.mandant);
            pOffset++;
            pPStm.setInt(pOffset, lAbstimmungenEinzelAusschluss.abstimmungsIdent);
            pOffset++;
            pPStm.setLong(pOffset, lAbstimmungenEinzelAusschluss.db_version);
            pOffset++;
            pPStm.setInt(pOffset, lAbstimmungenEinzelAusschluss.identMeldung);
            pOffset++;

            if (pOffset - startOffset != anzfelder) {
                /* Nur wg. Überprüfung auf Programmierfehler - alle Felder berücksichtigt? */
                CaBug.drucke("DbAbstimmungenEinzelAusschluss.fuellePreparedStatementKomplett 002");
            }

        } catch (SQLException e) {
            CaBug.drucke("DbAbstimmungenEinzelAusschluss.fuellePreparedStatementKomplett 001");
            e.printStackTrace();
        }

    }

    /**
     * Insert
     * 
     * Feld mandant wird von dieser Funktion immer selbstständig belegt.
     * 
     * Returnwert: =1 => Insert erfolgreich ansonsten: Fehler
     */
    public int insert(EclAbstimmungenEinzelAusschluss pAbstimmungenEinzelAusschluss) {

        int erg = 0;

        /* Start Transaktion */
        dbBasis.beginTransaction();

        pAbstimmungenEinzelAusschluss.mandant = dbBundle.clGlobalVar.mandant;

        try {

            /* Felder Neuanlage füllen */
            String lSql = "INSERT INTO " + dbBundle.getSchemaMandant() + "tbl_abstimmungenEinzelAusschluss " + "("
                    + "mandant, abstimmungsIdent, db_version, identMeldung " + ")" + "VALUES (" + "?, ?, ?, ? " + ")";
            PreparedStatement lPStm = verbindung.prepareStatement(lSql, ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);

            fuellePreparedStatementKomplett(lPStm, 1, pAbstimmungenEinzelAusschluss);

            erg = executeUpdate(lPStm);
            lPStm.close();
        } catch (Exception e2) {
            CaBug.drucke("DbAbstimmungenEinzelAusschluss.insert 001");
            System.err.println(" " + e2.getMessage());
        }

        if (erg == 0) {/* Fehler beim Einfügen - d.h. primaryKey bereits vorhanden */
            dbBasis.rollbackTransaction();
            dbBasis.endTransaction();
            return (-1);
        }

        /* Ende Transaktion */
        dbBasis.endTransaction();
        return (1);
    }

    public int readZuIdentAbstimmung(int pAbstimmung) {
        int anzInArray = 0;
        PreparedStatement lPStm = null;

        try {
            String lSql = "SELECT abEA.* from " + dbBundle.getSchemaMandant()
                    + "tbl_abstimmungenEinzelAusschluss abEA where " + "abEA.mandant=? AND "
                    + "abEA.abstimmungsIdent=? ORDER BY abEA.identMeldung;";
            lPStm = verbindung.prepareStatement(lSql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            lPStm.setInt(1, dbBundle.clGlobalVar.mandant);
            lPStm.setInt(2, pAbstimmung);

            ResultSet lErgebnis = executeQuery(lPStm);
            lErgebnis.last();
            anzInArray = lErgebnis.getRow();
            lErgebnis.beforeFirst();

            ergebnisArray = new EclAbstimmungenEinzelAusschluss[anzInArray];

            int i = 0;
            while (lErgebnis.next() == true) {
                ergebnisArray[i] = this.decodeErgebnis(lErgebnis);
                i++;
            }
            lErgebnis.close();
            lPStm.close();

        } catch (Exception e) {
            CaBug.drucke("DbAbstimmungenEinzelAusschluss.readZuStimmkartenNr 003");
            System.err.println(" " + e.getMessage());
            return (-1);
        }
        return (anzInArray);
    }

    public int readAlle() {
        int anzInArray = 0;
        PreparedStatement lPStm = null;

        try {
            String lSql = "SELECT abEA.* from " + dbBundle.getSchemaMandant()
                    + "tbl_abstimmungenEinzelAusschluss abEA where " + "abEA.mandant=? "
                    + "ORDER BY abEA.abstimmungsIdent;";
            lPStm = verbindung.prepareStatement(lSql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            lPStm.setInt(1, dbBundle.clGlobalVar.mandant);

            ResultSet lErgebnis = executeQuery(lPStm);
            lErgebnis.last();
            anzInArray = lErgebnis.getRow();
            lErgebnis.beforeFirst();

            ergebnisArray = new EclAbstimmungenEinzelAusschluss[anzInArray];

            int i = 0;
            while (lErgebnis.next() == true) {
                ergebnisArray[i] = this.decodeErgebnis(lErgebnis);
                i++;
            }
            lErgebnis.close();
            lPStm.close();

        } catch (Exception e) {
            CaBug.drucke("DbAbstimmungenEinzelAusschluss.readZuStimmkartenNr 003");
            System.err.println(" " + e.getMessage());
            return (-1);
        }
        return (anzInArray);
    }

    public int delete(int pAbstimmungsIdent, int pIdentMeldung) {

        try {

            String sql = "DELETE FROM " + dbBundle.getSchemaMandant()
                    + "tbl_abstimmungenEinzelAusschluss WHERE abstimmungsIdent=? AND identMeldung=? AND mandant=? ";

            PreparedStatement pstm1 = verbindung.prepareStatement(sql);
            pstm1.setInt(1, pAbstimmungsIdent);
            pstm1.setInt(2, pIdentMeldung);
            pstm1.setInt(3, dbBundle.clGlobalVar.mandant);

            int ergebnis1 = executeUpdate(pstm1);
            pstm1.close();
            if (ergebnis1 == 0) {
                return (CaFehler.pfXyWurdeVonAnderemBenutzerVeraendert);
            }
        } catch (Exception e1) {
            CaBug.drucke("DbAbstimmungZuAbstimmungsblock.delete 001");
            System.err.println(" " + e1.getMessage());
            return (CaFehler.pfdXyBereitsVorhanden);
        }

        return (1);
    }

    public int deleteAlleZuAbstimmung(int pAbstimmungsIdent) {

        try {

            String sql = "DELETE FROM " + dbBundle.getSchemaMandant()
                    + "tbl_abstimmungenEinzelAusschluss WHERE abstimmungsIdent=? AND mandant=? ";

            PreparedStatement pstm1 = verbindung.prepareStatement(sql);
            pstm1.setInt(1, pAbstimmungsIdent);
            pstm1.setInt(2, dbBundle.clGlobalVar.mandant);

            int ergebnis1 = executeUpdate(pstm1);
            pstm1.close();
            if (ergebnis1 == 0) {
                return (CaFehler.pfXyWurdeVonAnderemBenutzerVeraendert);
            }
        } catch (Exception e1) {
            CaBug.drucke("DbAbstimmungZuAbstimmungsblock.deleteAlleZuAbstimmung 001");
            System.err.println(" " + e1.getMessage());
            return (CaFehler.pfdXyBereitsVorhanden);
        }

        return (1);
    }

    public List<String> readAnzeigeStringZuId(int pAbstimmungsIdent) {

        List<String> list = new LinkedList<>();

        String sql = "SELECT abEA.identMeldung, p.kurzName, me.stimmen FROM " + dbBundle.getSchemaMandant()
                + "tbl_abstimmungeneinzelausschluss abEA " + "LEFT JOIN " + dbBundle.getSchemaMandant()
                + "tbl_meldungen me on me.meldungsIdent = abEA.identMeldung " + "LEFT JOIN "
                + dbBundle.getSchemaMandant() + "tbl_personennatjur p on p.ident = me.personenNatJurIdent "
                + "WHERE abEA.abstimmungsIdent=? and abEA.mandant=?";

        try (PreparedStatement ps = verbindung.prepareStatement(sql)) {

            ps.setInt(1, pAbstimmungsIdent);
            ps.setInt(2, dbBundle.clGlobalVar.mandant);

            try (ResultSet rs = executeQuery(ps)) {
                while (rs.next()) {
                    list.add("Meldung: " + rs.getInt(1) + " - " + rs.getString(2) + " - Aktien: "
                            + String.valueOf(NumberFormat.getInstance().format(rs.getInt(3))));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<EclMeldung> readMeldungenZuId(int pAbstimmungsIdent) {

        List<EclMeldung> list = new LinkedList<>();

        String sql = "SELECT abEA.identMeldung, me.aktionaersnummer, me.zutrittsIdent, me.gattung, me.stimmen, p.kurzName FROM "
                + dbBundle.getSchemaMandant() + "tbl_abstimmungeneinzelausschluss abEA " + "LEFT JOIN "
                + dbBundle.getSchemaMandant() + "tbl_meldungen me on me.meldungsIdent = abEA.identMeldung "
                + "LEFT JOIN " + dbBundle.getSchemaMandant()
                + "tbl_personennatjur p on p.ident = me.personenNatJurIdent "
                + "WHERE abEA.abstimmungsIdent=? and abEA.mandant=?";

        try (PreparedStatement ps = verbindung.prepareStatement(sql)) {

            ps.setInt(1, pAbstimmungsIdent);
            ps.setInt(2, dbBundle.clGlobalVar.mandant);

            try (ResultSet rs = executeQuery(ps)) {
                while (rs.next()) {
                    final EclMeldung meldung = new EclMeldung();
                    meldung.meldungsIdent = rs.getInt(1);
                    meldung.aktionaersnummer = rs.getString(2);
                    meldung.zutrittsIdent = rs.getString(3);
                    meldung.zusatz1 = ParamS.param.paramBasis.getGattungBezeichnungKurz(rs.getInt(4));
                    meldung.stimmen = rs.getInt(5);
                    meldung.name = rs.getString(6);

                    list.add(meldung);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}
