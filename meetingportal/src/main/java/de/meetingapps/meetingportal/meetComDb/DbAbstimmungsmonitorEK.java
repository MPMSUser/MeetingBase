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
import de.meetingapps.meetingportal.meetComEntities.EclAbstimmungsmonitorEK;
import de.meetingapps.meetingportal.meetComEntities.EclZutrittsIdent;

public class DbAbstimmungsmonitorEK  extends DbRootExecute {

    private Connection verbindung = null;
    private DbBundle dbBundle = null;

    public EclAbstimmungsmonitorEK ergebnisArray[] = null;

    /*************************Initialisierung***************************/
    public DbAbstimmungsmonitorEK(DbBundle pDbBundle) {
        /* Verbindung in lokale Daten eintragen*/
        if (pDbBundle == null) {
            CaBug.drucke("DbAbstimmungsmonitorEK.init 001 - dbBundle nicht initialisiert");
            return;
        }
        if (pDbBundle.dbBasis == null) {
            CaBug.drucke("DbAbstimmungsmonitorEK.init 002 - dbBasis nicht initialisiert");
            return;
        }

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

    /**********Liefert pN-tes Element des Ergebnisses der read*Methoden**************
     * pN geht von 0 bis anzErgebnis-1*/
    public EclAbstimmungsmonitorEK ergebnisPosition(int pN) {
        if (ergebnisArray == null) {
            CaBug.drucke("DbAbstimmungsmonitorEK.ergebnisPosition 001");
            return null;
        }
        if (pN < 0) {
            CaBug.drucke("DbAbstimmungsmonitorEK.ergebnisPosition 002");
            return null;
        }
        if (pN >= ergebnisArray.length) {
            CaBug.drucke("DbAbstimmungsmonitorEK.ergebnisPosition 003");
            return null;
        }
        return ergebnisArray[pN];
    }

    public int createTable() {
        int rc = 0;
        DbLowLevel lDbLowLevel = new DbLowLevel(dbBundle);
        rc = lDbLowLevel.createTable("CREATE TABLE " + dbBundle.getSchemaMandant() + "tbl_abstimmungsmonitorek ( "
                + "`mandant` int(11) NOT NULL, " + "`ident` int(11) DEFAULT NULL, "
                + "`db_version` bigint(20) DEFAULT NULL, " + "`zutrittsIdent` varchar(20) NOT NULL DEFAULT '', "
                + "`zutrittsIdentNeben` varchar(2) DEFAULT '', " + "`stimmkarte` varchar(20) DEFAULT '', "
                + "`meldeIdent` int(11) DEFAULT NULL, " + "`meldeAktionaer` varchar(80) DEFAULT '', "
                + "`meldeVertreter` varchar(80) DEFAULT '', " + "`meldeStimmen` bigint(20) DEFAULT '0', "
                + "`statusPraesenz` int(11) DEFAULT '0', " + "PRIMARY KEY (`zutrittsIdent`,`mandant`) " + ") ");
        return rc;
    }

    /**************************deleteAll Löschen aller Datensätze  eines Mandanten****************/
    public int deleteAll() {
        int erg = 0;

        dbBundle.dbBasis.resetInterneIdentAbstimmungsmonitorEK();

        try {

            String sql1 = "DELETE FROM " + dbBundle.getSchemaMandant() + "tbl_abstimmungsmonitorEK where mandant=?;";
            PreparedStatement pstm1 = verbindung.prepareStatement(sql1);
            pstm1.setInt(1, dbBundle.clGlobalVar.mandant);

            erg = 0; /*Falls Duplicate Key, dann wird exception geschmissen und erg wohl nicht gefüllt!*/
            erg = executeUpdate(pstm1);

            pstm1.close();
        } catch (Exception e2) {
            CaBug.drucke("DbAbstimmungsmonitorEK.deleteAll 001");
            System.err.println(" " + e2.getMessage());
            return (erg);
        }

        return 1;
    }

    /**************************setzt aktuelle Mandantennummer bei allen Datensätzen****************/
    public int updateMandant() {
        return dbBundle.dbLowLevel.rawUpdateMandant(dbBundle.getSchemaMandant() + "tbl_abstimmungsmonitorEK");
    }

    public void reorgInterneIdent() {
        int lMax;
        lMax = 0;

        PreparedStatement pstm1 = null;
        try {
            int anzInArray;
            String sql = "SELECT MAX(ident) FROM " + dbBundle.getSchemaMandant() + "tbl_abstimmungsmonitorEK ab where "
                    + "ab.mandant=? ";
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
            CaBug.drucke("DbAbstimmungsmonitorEK.reorgInterneIdent 001");
            System.err.println(" " + e.getMessage());
        }

        dbBundle.dbBasis.resetInterneIdentAbstimmungsmonitorEK(lMax);

    }

    /********** dekodiert die aktuelle Position aus ergebnis in EclAktienregisterEintrag und gibt dieses zurück******/
    EclAbstimmungsmonitorEK decodeErgebnis(ResultSet pErgebnis) {

        EclAbstimmungsmonitorEK lAbstimmungsmonitorE = new EclAbstimmungsmonitorEK();

        try {

            lAbstimmungsmonitorE.mandant = pErgebnis.getInt("amo.mandant");
            lAbstimmungsmonitorE.ident = pErgebnis.getInt("amo.ident");
            lAbstimmungsmonitorE.db_version = pErgebnis.getLong("amo.db_version");

            lAbstimmungsmonitorE.zutrittsIdent = pErgebnis.getString("amo.zutrittsIdent");
            lAbstimmungsmonitorE.zutrittsIdentNeben = pErgebnis.getString("amo.zutrittsIdentNeben");
            lAbstimmungsmonitorE.stimmkarte = pErgebnis.getString("amo.stimmkarte");

            lAbstimmungsmonitorE.meldeIdent = pErgebnis.getInt("amo.meldeIdent");

            lAbstimmungsmonitorE.meldeAktionaer = pErgebnis.getString("amo.meldeAktionaer");
            lAbstimmungsmonitorE.meldeVertreter = pErgebnis.getString("amo.meldeVertreter");

            lAbstimmungsmonitorE.meldeStimmen = pErgebnis.getLong("amo.meldeStimmen");
            lAbstimmungsmonitorE.statusPraesenz = pErgebnis.getInt("amo.statusPraesenz");

        } catch (Exception e) {
            CaBug.drucke("DbAbstimmungsmonitorEK.decodeErgebnis 001");
            System.err.println(" " + e.getMessage());
        }

        return lAbstimmungsmonitorE;
    }

    /********************* Fuellen Prepared Statement mit allen Feldern, beginnend bei offset.*****************
     * Kann sowohl für Insert, als auch für update verwendet werden.
     * 
     * offset= Startposition des ersten Feldes (also z.B. 1)
     */
    private int anzfelder = 11; /*Anpassen auf Anzahl der Felder pro Datensatz*/

    void fuellePreparedStatementKomplett(PreparedStatement pPStm, int pOffset,
            EclAbstimmungsmonitorEK pAbstimmungsmonitorEK) {

        int startOffset = pOffset; /*Nur erforderlich zum Überprüfen von Programmierfehlern - Feldanzahl*/

        try {
            pPStm.setInt(pOffset, pAbstimmungsmonitorEK.mandant);
            pOffset++;
            pPStm.setInt(pOffset, pAbstimmungsmonitorEK.ident);
            pOffset++;
            pPStm.setLong(pOffset, pAbstimmungsmonitorEK.db_version);
            pOffset++;

            pPStm.setString(pOffset, pAbstimmungsmonitorEK.zutrittsIdent);
            pOffset++;
            pPStm.setString(pOffset, pAbstimmungsmonitorEK.zutrittsIdentNeben);
            pOffset++;
            pPStm.setString(pOffset, pAbstimmungsmonitorEK.stimmkarte);
            pOffset++;

            pPStm.setInt(pOffset, pAbstimmungsmonitorEK.meldeIdent);
            pOffset++;

            pPStm.setString(pOffset, CaString.trunc(pAbstimmungsmonitorEK.meldeAktionaer, 80));
            pOffset++;
            pPStm.setString(pOffset, CaString.trunc(pAbstimmungsmonitorEK.meldeVertreter, 80));
            pOffset++;

            pPStm.setLong(pOffset, pAbstimmungsmonitorEK.meldeStimmen);
            pOffset++;
            pPStm.setInt(pOffset, pAbstimmungsmonitorEK.statusPraesenz);
            pOffset++;

            if (pOffset - startOffset != anzfelder) {
                /*Nur wg. Überprüfung auf Programmierfehler - alle Felder berücksichtigt?*/
                CaBug.drucke("DbAbstimmungsmonitorEK.fuellePreparedStatementKomplett 002");
            }

        } catch (SQLException e) {
            CaBug.drucke("DbAbstimmungsmonitorEK.fuellePreparedStatementKomplett 001");
            e.printStackTrace();
        }

    }

    /**Insert
     * 
     * Feld mandant wird von dieser Funktion immer selbstständig belegt.
     * 
     * Returnwert:
     * =1 => Insert erfolgreich
     * ansonsten: Fehler
     */
    public int insert(EclAbstimmungsmonitorEK pAbstimmungsmonitorEK) {

        int erg = 0;

        pAbstimmungsmonitorEK.mandant = dbBundle.clGlobalVar.mandant;

        try {

            /*Felder Neuanlage füllen*/
            String lSql = "INSERT INTO " + dbBundle.getSchemaMandant() + "tbl_abstimmungsmonitorEK " + "("
                    + "mandant, ident, db_version, "
                    + "zutrittsIdent, zutrittsIdentNeben, stimmkarte, meldeIdent, meldeAktionaer, "
                    + "meldeVertreter, meldeStimmen, statusPraesenz)" + "VALUES (" + "?, ?, ?, " + "?, ?, ?, ?, ?, "
                    + "?, ?, ?" + ")";
            PreparedStatement lPStm = verbindung.prepareStatement(lSql, ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);

            fuellePreparedStatementKomplett(lPStm, 1, pAbstimmungsmonitorEK);

            erg = executeUpdate(lPStm);
            lPStm.close();
        } catch (Exception e2) {
            CaBug.drucke("DbAbstimmungsmonitorEK.insert 001");
            System.err.println(" " + e2.getMessage());
        }

        if (erg == 0) {/*Fehler beim Einfügen - d.h. primaryKey bereits vorhanden*/
            return (-1);
        }

        return (1);
    }

    /**Einlesen des Satzes mit ZutrittsIdent*/
    public int read(EclZutrittsIdent pZutrittsIdent) {
        int anzInArray = 0;
        PreparedStatement lPStm = null;

        try {
            String lSql = "SELECT amo.* from " + dbBundle.getSchemaMandant() + "tbl_abstimmungsmonitorEK amo where "
                    + "amo.mandant=? AND amo.zutrittsIdent=? AND amo.zutrittsIdentNeben=?;";
            lPStm = verbindung.prepareStatement(lSql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            lPStm.setInt(1, dbBundle.clGlobalVar.mandant);
            lPStm.setString(2, pZutrittsIdent.zutrittsIdent);
            lPStm.setString(3, pZutrittsIdent.zutrittsIdentNeben);

            ResultSet lErgebnis = executeQuery(lPStm);
            lErgebnis.last();
            anzInArray = lErgebnis.getRow();
            lErgebnis.beforeFirst();

            ergebnisArray = new EclAbstimmungsmonitorEK[anzInArray];

            int i = 0;
            while (lErgebnis.next() == true) {
                ergebnisArray[i] = this.decodeErgebnis(lErgebnis);
                i++;
            }
            lErgebnis.close();
            lPStm.close();

        } catch (Exception e) {
            CaBug.drucke("DbAbstimmungsmonitorEK.read 003");
            System.err.println(" " + e.getMessage());
            return (-1);
        }
        return (anzInArray);
    }

    /**Einlesen aller Sätze des Mandanten.*/
    public int read_all() {
        int anzInArray = 0;
        PreparedStatement lPStm = null;

        try {
            String lSql = "SELECT amo.* from " + dbBundle.getSchemaMandant() + "tbl_abstimmungsmonitorEK amo where "
                    + "amo.mandant=?;";
            lPStm = verbindung.prepareStatement(lSql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            lPStm.setInt(1, dbBundle.clGlobalVar.mandant);

            ResultSet lErgebnis = executeQuery(lPStm);
            lErgebnis.last();
            anzInArray = lErgebnis.getRow();
            lErgebnis.beforeFirst();

            ergebnisArray = new EclAbstimmungsmonitorEK[anzInArray];

            int i = 0;
            while (lErgebnis.next() == true) {
                ergebnisArray[i] = this.decodeErgebnis(lErgebnis);
                i++;
            }
            lErgebnis.close();
            lPStm.close();

        } catch (Exception e) {
            CaBug.drucke("DbAbstimmungsmonitorEK.read 003");
            System.err.println(" " + e.getMessage());
            return (-1);
        }
        return (anzInArray);
    }

    /**Update. Versionsnummer wird um 1 hochgezählt
     * 
     * Feld mandant wird von dieser Funktion nicht selbstständig belegt.
     * 
     * Zum Sicherstellen der Multiuserfähigkeit muß in jedem Fall der rc CaFehler.pfXyWurdeVonAnderemBenutzerVeraendert
     * nach Aufruf dieser Funktion abgefangen werden.
     * Ansonsten: rc=1 => ok, ansonsten Fehler
     */
    public int update(EclAbstimmungsmonitorEK pAbstimmungsmonitorEK) {

        pAbstimmungsmonitorEK.db_version++;

        try {
            String lSql = "UPDATE " + dbBundle.getSchemaMandant() + "tbl_abstimmungsmonitorEK SET "
                    + "mandant=?, ident=?, db_version=?, "
                    + "zutrittsIdent=?, zutrittsIdentNeben=?, stimmkarte=?, meldeIdent=?, meldeAktionaer=?, "
                    + "meldeVertreter=?, meldeStimmen=?, statusPraesenz=? " + "WHERE "
                    + "db_version=? AND mandant=? AND ident=?";

            PreparedStatement lPStm = verbindung.prepareStatement(lSql);
            fuellePreparedStatementKomplett(lPStm, 1, pAbstimmungsmonitorEK);
            lPStm.setLong(anzfelder + 1, pAbstimmungsmonitorEK.db_version - 1);
            lPStm.setInt(anzfelder + 2, pAbstimmungsmonitorEK.mandant);
            lPStm.setInt(anzfelder + 3, pAbstimmungsmonitorEK.ident);

            int ergebnis = executeUpdate(lPStm);
            lPStm.close();
            if (ergebnis == 0) {
                return (CaFehler.pfXyWurdeVonAnderemBenutzerVeraendert);
            }
        } catch (Exception e1) {
            CaBug.drucke("DbAbstimmungsmonitorEK.update 001");
            System.err.println(" " + e1.getMessage());
            return (CaFehler.pfdXyBereitsVorhanden);
        }

        return (1);
    }

    public int delete(int pIdent) {

        try {

            String sql = "DELETE FROM " + dbBundle.getSchemaMandant()
                    + "tbl_abstimmungsmonitorEK WHERE mandant=? and ident=? ";

            PreparedStatement pstm1 = verbindung.prepareStatement(sql);
            pstm1.setInt(1, dbBundle.clGlobalVar.mandant);
            pstm1.setInt(2, pIdent);

            int ergebnis1 = executeUpdate(pstm1);
            pstm1.close();
            if (ergebnis1 == 0) {
                return (CaFehler.pfXyWurdeVonAnderemBenutzerVeraendert);
            }
        } catch (Exception e1) {
            CaBug.drucke("DbAbstimmungen.delete 001");
            System.err.println(" " + e1.getMessage());
            return (CaFehler.pfdXyBereitsVorhanden);
        }

        return (1);
    }

}
