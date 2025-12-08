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
import java.util.ArrayList;
import java.util.List;

import de.meetingapps.meetingportal.meetComAllg.CaBug;
import de.meetingapps.meetingportal.meetComAllg.CaFehler;
import de.meetingapps.meetingportal.meetComAllg.CaString;
import de.meetingapps.meetingportal.meetComBl.BlAktienregister;
import de.meetingapps.meetingportal.meetComBl.BlNummernformBasis;
import de.meetingapps.meetingportal.meetComBl.BlPersonenNatJur;
import de.meetingapps.meetingportal.meetComBl.BlWillenserklaerung;
import de.meetingapps.meetingportal.meetComEntities.EclAktienregister;
import de.meetingapps.meetingportal.meetComEntities.EclIsin;
import de.meetingapps.meetingportal.meetComEntities.EclMeldung;
import de.meetingapps.meetingportal.meetComEntities.EclPersonenNatJur;
import de.meetingapps.meetingportal.meetComKonst.KonstMeldung;

/*TODO _aktienregisterhistorie - komplett konsolidieren - wann wird wie gelöscht?*/

public class DbAktienregister extends DbRootExecute {

    private int logDrucken = 3;

    private Connection verbindung = null;
    private DbBundle dbBundle = null;

    public EclAktienregister ergebnisArray[] = null;
    public int count = 0;

    private final int Konst_LiefereCreateString_CreateTable = 1;
    private final int Konst_LiefereCreateString_CreateTableAktienregisterHistorie = 2;

    private final int Konst_FuellePreparedStatementIntern_fuellePreparedStatementKomplett = 1;
    //    private final int Konst_FuellePreparedStatementIntern_fuellePreparedStatementInsertAktienregisterNew=2;
    private final int Konst_FuellePreparedStatementIntern_fuellePreparedStatementUpdateAktienregisterNew = 3;
    private final int Konst_FuellePreparedStatementIntern_fuellePreparedStatementInsertHistorieNew = 4;

    private final int Konst_LiefereInsertSql_insert = 1;
    private final int Konst_LiefereInsertSql_insertAktienregisterListe = 2;
    private final int Konst_LiefereInsertSql_insertHistorieListe = 3;

    private final int Konst_LiefereUpdateSql_update = 1;
    private final int Konst_LiefereUpdateSql_updateAktienregisterListe = 2;

    /************************* Initialisierung ***************************/
    /* Verbindung in lokale Daten eintragen */
    public DbAktienregister(DbBundle pDbBundle) {
        /* Verbindung in lokale Daten eintragen */
        if (pDbBundle == null) {
            CaBug.drucke("DbAktienregister.init 001 - dbBundle nicht initialisiert");
            return;
        }
        if (pDbBundle.dbBasis == null) {
            CaBug.drucke("DbAktienregister.init 002 - dbBasis nicht initialisiert");
            return;
        }

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

    /**********
     * Liefert pN-tes Element des Ergebnisses der read*Methoden************** pN
     * geht von 0 bis anzErgebnis-1
     */
    public EclAktienregister ergebnisPosition(int pN) {
        if (ergebnisArray == null) {
            CaBug.drucke("DbAktienregister.ergebnisPosition 001");
            return null;
        }
        if (pN < 0) {
            CaBug.drucke("DbAktienregister.ergebnisPosition 002");
            return null;
        }
        if (pN >= ergebnisArray.length) {
            CaBug.drucke("DbAktienregister.ergebnisPosition 003");
            return null;
        }
        return ergebnisArray[pN];
    }

    private String liefereCreateString(int pKonst_LiefereCreateString_) {
        String ergebnis = "CREATE TABLE " + dbBundle.getSchemaMandant();

        switch (pKonst_LiefereCreateString_) {
        case Konst_LiefereCreateString_CreateTable:
            ergebnis += "tbl_aktienregister ( ";
            break;
        case Konst_LiefereCreateString_CreateTableAktienregisterHistorie:
            ergebnis += "tbl_aktienregisterhistorie ( ";
            break;
        }

        switch (pKonst_LiefereCreateString_) {
        case Konst_LiefereCreateString_CreateTable:
            break;
        case Konst_LiefereCreateString_CreateTableAktienregisterHistorie:
            ergebnis += "`aktienregisterhistorieIdent` int(11) NOT NULL AUTO_INCREMENT, ";
            break;
        }

        ergebnis += "`mandant` int(11) NOT NULL, " + "`aktienregisterIdent` int(11) NOT NULL, "
                + "`db_version` bigint(20) DEFAULT NULL, " + "`aktionaersnummer` varchar(20) DEFAULT NULL, "
                + "`personNatJur` int(11) DEFAULT '0', " + "`anredeId` int(11) DEFAULT NULL, "
                + "`titel` char(80) DEFAULT NULL, " + "`name3` char(200) DEFAULT NULL, "
                + "`nachname` char(80) DEFAULT NULL, " + "`name1` char(200) DEFAULT NULL, "
                + "`vorname` char(80) DEFAULT NULL, " + "`name2` char(200) DEFAULT NULL, "
                + "`zusatz` char(80) DEFAULT NULL, " + "`strasse` char(80) DEFAULT NULL, "
                + "`postfach` char(20) DEFAULT NULL, " + "`postleitzahl` char(20) DEFAULT NULL, "
                + "`postleitzahlPostfach` char(20) DEFAULT NULL, " + "`ort` char(80) DEFAULT NULL, "
                + "`staatId` int(11) DEFAULT NULL, " + "`email` char(50) DEFAULT NULL, "
                + "`anredeIdVersand` int(11) DEFAULT NULL, " + "`titelVersand` char(80) DEFAULT NULL, "
                + "`name3Versand` char(200) DEFAULT NULL, " + "`nachnameVersand` char(80) DEFAULT NULL, "
                + "`name1Versand` char(200) DEFAULT NULL, " + "`vornameVersand` char(80) DEFAULT NULL, "
                + "`name2Versand` char(200) DEFAULT NULL, " + "`zusatzVersand` char(80) DEFAULT NULL, "
                + "`strasseVersand` char(80) DEFAULT NULL, " + "`postfachVersand` char(20) DEFAULT NULL, "
                + "`postleitzahlVersand` char(20) DEFAULT NULL, "
                + "`postleitzahlPostfachVersand` char(20) DEFAULT NULL, " + "`ortVersand` char(80) DEFAULT NULL, "
                + "`staatIdVersand` int(11) DEFAULT NULL, " + "`emailVersand` char(50) DEFAULT NULL, "
                + "`nameKomplett` varchar(200) DEFAULT NULL, " + "`istPersonengemeinschaft` int(11) DEFAULT NULL, "
                + "`istJuristischePerson` int(11) DEFAULT NULL, " + "`versandAbweichend` int(11) DEFAULT NULL, "
                + "`gattungId` int(11) DEFAULT NULL, " + "`isin` varchar(12) DEFAULT NULL, "
                + "`stueckAktien` bigint(20) DEFAULT NULL, " + "`stimmen` bigint(20) DEFAULT NULL, "
                + "`besitzart` char(10) DEFAULT NULL, " + "`stimmausschluss` char(13) DEFAULT NULL, "
                + "`gruppe` int(11) DEFAULT '0', ";

        switch (pKonst_LiefereCreateString_) {
        case Konst_LiefereCreateString_CreateTable:
            ergebnis += "`adresszeile1` char(200) DEFAULT NULL, " + "`adresszeile2` char(200) DEFAULT NULL, "
                    + "`adresszeile3` char(200) DEFAULT NULL, " + "`adresszeile4` char(200) DEFAULT NULL, "
                    + "`adresszeile5` char(200) DEFAULT NULL, " + "`adresszeile6` char(200) DEFAULT NULL, "
                    + "`adresszeile7` char(200) DEFAULT NULL, " + "`adresszeile8` char(200) DEFAULT NULL, "
                    + "`adresszeile9` char(200) DEFAULT NULL, " + "`adresszeile10` char(200) DEFAULT NULL, ";
            break;
        case Konst_LiefereCreateString_CreateTableAktienregisterHistorie:
            break;
        }

        ergebnis += "`englischerVersand` int(11) DEFAULT NULL, " + "`herkunftQuelle` int(11) DEFAULT NULL, "
                + "`herkunftQuelleLfd` varchar(30) DEFAULT NULL, " + "`herkunftIdent` int(11) DEFAULT NULL, ";

        switch (pKonst_LiefereCreateString_) {
        case Konst_LiefereCreateString_CreateTable:
            ergebnis += "`versandNummer` int(11) DEFAULT NULL, " + "`datenUpdate` int(11) DEFAULT NULL, ";
            break;
        case Konst_LiefereCreateString_CreateTableAktienregisterHistorie:
            ergebnis += "`kennzeichen` int(11) DEFAULT NULL, ";
            break;
        }

        ergebnis += "`zuletztGeandert` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP, ";

        switch (pKonst_LiefereCreateString_) {
        case Konst_LiefereCreateString_CreateTable:
            ergebnis += "PRIMARY KEY (`aktienregisterIdent`,`mandant`), "
                    + "KEY `IDX_Nummer` (`mandant`,`aktionaersnummer`) ";
            break;
        case 2:
            ergebnis += "PRIMARY KEY (`aktienregisterhistorieIdent`) ";
            break;
        }

        ergebnis += ") ";

        return ergebnis;
    }

    public int createTable() {
        int rc = 0;
        DbLowLevel lDbLowLevel = new DbLowLevel(dbBundle);
        rc = lDbLowLevel.createTable(liefereCreateString(Konst_LiefereCreateString_CreateTable));
        return rc;
    }

    public int createTable_aktienregisterHistorie() {
        int rc = 0;
        DbLowLevel lDbLowLevel = new DbLowLevel(dbBundle);
        rc = lDbLowLevel.createTable(liefereCreateString(Konst_LiefereCreateString_CreateTableAktienregisterHistorie));

        return rc;
    }

    /**************************
     * deleteAll Löschen aller Datensätze eines Mandanten
     ****************/
    public int deleteAll() {
        int erg = 0;

        dbBundle.dbBasis.resetInterneIdentAktienregisterEintrag();

        try {

            //            String sql1 = "DELETE FROM " + dbBundle.getSchemaMandant() + "tbl_aktienregister where mandant=?;";
            String sql1 = "TRUNCATE " + dbBundle.getSchemaMandant() + "tbl_aktienregister;";
            PreparedStatement pstm1 = verbindung.prepareStatement(sql1);
            //            pstm1.setInt(1, dbBundle.clGlobalVar.mandant);

            erg = 0; /*
                      * Falls Duplicate Key, dann wird exception geschmissen und erg wohl nicht
                      * gefüllt!
                      */
            erg = executeUpdate(pstm1);

            pstm1.close();
        } catch (Exception e2) {
            CaBug.drucke("DbAktienregister.deleteAll 001");
            System.err.println(" " + e2.getMessage());
            return (erg);
        }

        return 1;
    }

    public int deleteMeldeEintraege() {
        try {

            String sql = "UPDATE " + dbBundle.getSchemaMandant() + "tbl_aktienregister SET " + "personNatJur=0 ";
            PreparedStatement lPStm = verbindung.prepareStatement(sql);

            int ergebnis = executeUpdate(lPStm);
            lPStm.close();
            if (ergebnis == 0) {
                return (CaFehler.pfXyWurdeVonAnderemBenutzerVeraendert);
            }
        } catch (Exception e1) {
            CaBug.drucke("DbAktienregister.update 001");
            System.err.println(" " + e1.getMessage());
            return (CaFehler.pfdXyBereitsVorhanden);
        }
        return (1);
    }

    public int deleteAll_aktienregisterHistorie() {
        DbLowLevel lDbLowLevel = new DbLowLevel(dbBundle);
        return lDbLowLevel.truncMandant("TRUNCATE " + dbBundle.getSchemaMandant() + "tbl_aktienregisterhistorie");
    }

    /**************************
     * setzt aktuelle Mandantennummer bei allen Datensätzen
     ****************/
    public int updateMandant() {
        dbBundle.dbLowLevel.rawUpdateMandant(dbBundle.getSchemaMandant() + "tbl_aktienregister");
        return dbBundle.dbLowLevel.rawUpdateMandant(dbBundle.getSchemaMandant() + "tbl_aktienregisterhistorie");
    }

    public void reorgInterneIdent() {
        int lMax;
        lMax = 0;

        PreparedStatement pstm1 = null;
        try {
            int anzInArray;
            String sql = "SELECT MAX(aktienregisterIdent) FROM " + dbBundle.getSchemaMandant()
                    + "tbl_aktienregister ab where " + "ab.mandant=? ";
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

        dbBundle.dbBasis.resetInterneIdentAktienregisterEintrag(lMax);

    }

    /**
     * dekodiert die aktuelle Position aus ergebnis in EclAktienregisterEintrag und
     * gibt dieses zurück
     */
    EclAktienregister decodeErgebnis(ResultSet pErgebnis) {
        EclAktienregister lAktienregister = new EclAktienregister();

        try {

            lAktienregister.mandant = pErgebnis.getInt("are.mandant");
            lAktienregister.aktienregisterIdent = pErgebnis.getInt("are.aktienregisterIdent");
            lAktienregister.db_version = pErgebnis.getLong("are.db_version");

            lAktienregister.aktionaersnummer = pErgebnis.getString("are.aktionaersnummer");

            lAktienregister.personNatJur = pErgebnis.getInt("are.personNatJur");
            lAktienregister.anredeId = pErgebnis.getInt("are.anredeId");
            lAktienregister.titel = pErgebnis.getString("are.titel");
            lAktienregister.name3 = pErgebnis.getString("are.name3");
            lAktienregister.nachname = pErgebnis.getString("are.nachname");
            lAktienregister.name1 = pErgebnis.getString("are.name1");
            lAktienregister.vorname = pErgebnis.getString("are.vorname");
            lAktienregister.name2 = pErgebnis.getString("are.name2");
            lAktienregister.zusatz = pErgebnis.getString("are.zusatz");
            lAktienregister.strasse = pErgebnis.getString("are.strasse");
            lAktienregister.postfach = pErgebnis.getString("are.postfach");
            lAktienregister.postleitzahl = pErgebnis.getString("are.postleitzahl");
            lAktienregister.postleitzahlPostfach = pErgebnis.getString("are.postleitzahlPostfach");
            lAktienregister.ort = pErgebnis.getString("are.ort");
            lAktienregister.staatId = pErgebnis.getInt("are.staatId");
            lAktienregister.email = pErgebnis.getString("are.email");

            lAktienregister.anredeIdVersand = pErgebnis.getInt("are.anredeIdVersand");
            lAktienregister.titelVersand = pErgebnis.getString("are.titelVersand");
            lAktienregister.name3Versand = pErgebnis.getString("are.name3Versand");
            lAktienregister.nachnameVersand = pErgebnis.getString("are.nachnameVersand");
            lAktienregister.name1Versand = pErgebnis.getString("are.name1Versand");
            lAktienregister.vornameVersand = pErgebnis.getString("are.vornameVersand");
            lAktienregister.name2Versand = pErgebnis.getString("are.name2Versand");
            lAktienregister.zusatzVersand = pErgebnis.getString("are.zusatzVersand");
            lAktienregister.strasseVersand = pErgebnis.getString("are.strasseVersand");
            lAktienregister.postfachVersand = pErgebnis.getString("are.postfachVersand");
            lAktienregister.postleitzahlVersand = pErgebnis.getString("are.postleitzahlVersand");
            lAktienregister.postleitzahlPostfachVersand = pErgebnis.getString("are.postleitzahlPostfachVersand");
            lAktienregister.ortVersand = pErgebnis.getString("are.ortVersand");
            lAktienregister.staatIdVersand = pErgebnis.getInt("are.staatIdVersand");
            lAktienregister.emailVersand = pErgebnis.getString("are.emailVersand");

            lAktienregister.nameKomplett = pErgebnis.getString("are.nameKomplett");

            lAktienregister.istPersonengemeinschaft = pErgebnis.getInt("are.istPersonengemeinschaft");
            lAktienregister.istJuristischePerson = pErgebnis.getInt("are.istJuristischePerson");
            lAktienregister.versandAbweichend = pErgebnis.getInt("are.versandAbweichend");

            lAktienregister.gattungId = pErgebnis.getInt("are.gattungId");
            lAktienregister.isin = pErgebnis.getString("are.isin");
            lAktienregister.stueckAktien = pErgebnis.getLong("are.stueckAktien");
            lAktienregister.stimmen = pErgebnis.getLong("are.stimmen");

            lAktienregister.besitzart = pErgebnis.getString("are.besitzart");
            lAktienregister.stimmausschluss = pErgebnis.getString("are.stimmausschluss");

            lAktienregister.gruppe = pErgebnis.getInt("are.gruppe");

            lAktienregister.adresszeile1 = pErgebnis.getString("are.adresszeile1");
            lAktienregister.adresszeile2 = pErgebnis.getString("are.adresszeile2");
            lAktienregister.adresszeile3 = pErgebnis.getString("are.adresszeile3");
            lAktienregister.adresszeile4 = pErgebnis.getString("are.adresszeile4");
            lAktienregister.adresszeile5 = pErgebnis.getString("are.adresszeile5");
            lAktienregister.adresszeile6 = pErgebnis.getString("are.adresszeile6");
            lAktienregister.adresszeile7 = pErgebnis.getString("are.adresszeile7");
            lAktienregister.adresszeile8 = pErgebnis.getString("are.adresszeile8");
            lAktienregister.adresszeile9 = pErgebnis.getString("are.adresszeile9");
            lAktienregister.adresszeile10 = pErgebnis.getString("are.adresszeile10");

            lAktienregister.englischerVersand = pErgebnis.getInt("are.englischerVersand");
            lAktienregister.herkunftQuelle = pErgebnis.getInt("are.herkunftQuelle");
            lAktienregister.herkunftQuelleLfd = pErgebnis.getString("are.herkunftQuelleLfd");
            lAktienregister.herkunftIdent = pErgebnis.getInt("are.herkunftIdent");
            lAktienregister.versandNummer = pErgebnis.getInt("are.versandNummer");
            lAktienregister.datenUpdate = pErgebnis.getInt("are.datenUpdate");

        } catch (Exception e) {
            CaBug.drucke("DbAktienregister.decodeErgebnis 001");
            System.err.println(" " + e.getMessage());
        }

        return lAktienregister;
    }

    /**pNormalOderHistorie
     * =1 => tbl_aktienregister
     * =2 => tbl_aktienregisterhistorie
     * =3 => tbl_aktienregisterUpdateNew
     */

    private void fuellePreparedStatementIntern(PreparedStatement pPStm, int pOffset, EclAktienregister pAktienregister,
            int pKonst_FuellePreparedStatementIntern) {

        switch (pKonst_FuellePreparedStatementIntern) {
        case Konst_FuellePreparedStatementIntern_fuellePreparedStatementKomplett:
            anzfelder = 62;
            break;
        case Konst_FuellePreparedStatementIntern_fuellePreparedStatementUpdateAktienregisterNew:
            anzfelder = 63;
            break;
        case Konst_FuellePreparedStatementIntern_fuellePreparedStatementInsertHistorieNew:
            anzfelder = 51;
            break;
        }

        int startOffset = pOffset; /* Nur erforderlich zum Überprüfen von Programmierfehlern - Feldanzahl */

        try {

            switch (pKonst_FuellePreparedStatementIntern) {
            case Konst_FuellePreparedStatementIntern_fuellePreparedStatementKomplett:
                pPStm.setInt(pOffset, pAktienregister.mandant);
                pOffset++;
                pPStm.setInt(pOffset, pAktienregister.aktienregisterIdent);
                pOffset++;
                pPStm.setLong(pOffset, pAktienregister.db_version);
                pOffset++;
                pPStm.setString(pOffset, pAktienregister.aktionaersnummer);
                pOffset++;
                break;
            case Konst_FuellePreparedStatementIntern_fuellePreparedStatementUpdateAktienregisterNew:
                pPStm.setLong(pOffset, pAktienregister.db_version + 1);
                pOffset++;
                pPStm.setString(pOffset, pAktienregister.aktionaersnummer);
                pOffset++;
                break;
            case Konst_FuellePreparedStatementIntern_fuellePreparedStatementInsertHistorieNew:
                pPStm.setInt(pOffset, pAktienregister.mandant);
                pOffset++;
                pPStm.setInt(pOffset, pAktienregister.aktienregisterIdent);
                pOffset++;
                pPStm.setLong(pOffset, pAktienregister.db_version);
                pOffset++;
                pPStm.setString(pOffset, pAktienregister.aktionaersnummer);
                pOffset++;
                break;
            }

            pPStm.setInt(pOffset, pAktienregister.personNatJur);
            pOffset++;
            pPStm.setInt(pOffset, pAktienregister.anredeId);
            pOffset++;
            pPStm.setString(pOffset, pAktienregister.titel);
            pOffset++;
            pPStm.setString(pOffset, pAktienregister.name3);
            pOffset++;
            pPStm.setString(pOffset, pAktienregister.nachname);
            pOffset++;
            pPStm.setString(pOffset, pAktienregister.name1);
            pOffset++;
            pPStm.setString(pOffset, pAktienregister.vorname);
            pOffset++;
            pPStm.setString(pOffset, pAktienregister.name2);
            pOffset++;
            pPStm.setString(pOffset, pAktienregister.zusatz);
            pOffset++;
            pPStm.setString(pOffset, pAktienregister.strasse);
            pOffset++;
            pPStm.setString(pOffset, pAktienregister.postfach);
            pOffset++;
            pPStm.setString(pOffset, pAktienregister.postleitzahl);
            pOffset++;
            pPStm.setString(pOffset, pAktienregister.postleitzahlPostfach);
            pOffset++;
            pPStm.setString(pOffset, pAktienregister.ort);
            pOffset++;
            pPStm.setInt(pOffset, pAktienregister.staatId);
            pOffset++;
            pPStm.setString(pOffset, pAktienregister.email);
            pOffset++;
            pPStm.setInt(pOffset, pAktienregister.anredeIdVersand);
            pOffset++;
            pPStm.setString(pOffset, pAktienregister.titelVersand);
            pOffset++;
            pPStm.setString(pOffset, pAktienregister.name3Versand);
            pOffset++;
            pPStm.setString(pOffset, pAktienregister.nachnameVersand);
            pOffset++;
            pPStm.setString(pOffset, pAktienregister.name1Versand);
            pOffset++;
            pPStm.setString(pOffset, pAktienregister.vornameVersand);
            pOffset++;
            pPStm.setString(pOffset, pAktienregister.name2Versand);
            pOffset++;
            pPStm.setString(pOffset, pAktienregister.zusatzVersand);
            pOffset++;
            pPStm.setString(pOffset, pAktienregister.strasseVersand);
            pOffset++;
            pPStm.setString(pOffset, pAktienregister.postfachVersand);
            pOffset++;
            pPStm.setString(pOffset, pAktienregister.postleitzahlVersand);
            pOffset++;
            pPStm.setString(pOffset, pAktienregister.postleitzahlPostfachVersand);
            pOffset++;
            pPStm.setString(pOffset, pAktienregister.ortVersand);
            pOffset++;
            pPStm.setInt(pOffset, pAktienregister.staatIdVersand);
            pOffset++;
            pPStm.setString(pOffset, pAktienregister.emailVersand);
            pOffset++;
            if (pAktienregister.nameKomplett.length() > 199) {
                pAktienregister.nameKomplett = CaString.trunc(pAktienregister.nameKomplett, 199);
            }
            pPStm.setString(pOffset, pAktienregister.nameKomplett);
            pOffset++;
            pPStm.setInt(pOffset, pAktienregister.istPersonengemeinschaft);
            pOffset++;
            pPStm.setInt(pOffset, pAktienregister.istJuristischePerson);
            pOffset++;
            pPStm.setInt(pOffset, pAktienregister.versandAbweichend);
            pOffset++;
            pPStm.setInt(pOffset, pAktienregister.gattungId);
            pOffset++;
            pPStm.setString(pOffset, pAktienregister.isin);
            pOffset++;
            pPStm.setLong(pOffset, pAktienregister.stueckAktien);
            pOffset++;
            pPStm.setLong(pOffset, pAktienregister.stimmen);
            pOffset++;
            pPStm.setString(pOffset, pAktienregister.besitzart);
            pOffset++;
            pPStm.setString(pOffset, pAktienregister.stimmausschluss);
            pOffset++;
            pPStm.setInt(pOffset, pAktienregister.gruppe);
            pOffset++;

            switch (pKonst_FuellePreparedStatementIntern) {
            case Konst_FuellePreparedStatementIntern_fuellePreparedStatementKomplett:
            case Konst_FuellePreparedStatementIntern_fuellePreparedStatementUpdateAktienregisterNew:
                pPStm.setString(pOffset, pAktienregister.adresszeile1);
                pOffset++;
                pPStm.setString(pOffset, pAktienregister.adresszeile2);
                pOffset++;
                pPStm.setString(pOffset, pAktienregister.adresszeile3);
                pOffset++;
                pPStm.setString(pOffset, pAktienregister.adresszeile4);
                pOffset++;
                pPStm.setString(pOffset, pAktienregister.adresszeile5);
                pOffset++;
                pPStm.setString(pOffset, pAktienregister.adresszeile6);
                pOffset++;
                pPStm.setString(pOffset, pAktienregister.adresszeile7);
                pOffset++;
                pPStm.setString(pOffset, pAktienregister.adresszeile8);
                pOffset++;
                pPStm.setString(pOffset, pAktienregister.adresszeile9);
                pOffset++;
                pPStm.setString(pOffset, pAktienregister.adresszeile10);
                pOffset++;
                break;
            case Konst_FuellePreparedStatementIntern_fuellePreparedStatementInsertHistorieNew:
                break;
            }

            pPStm.setInt(pOffset, pAktienregister.englischerVersand);
            pOffset++;
            pPStm.setInt(pOffset, pAktienregister.herkunftQuelle);
            pOffset++;
            pPStm.setString(pOffset, pAktienregister.herkunftQuelleLfd);
            pOffset++;
            pPStm.setInt(pOffset, pAktienregister.herkunftIdent);
            pOffset++;

            switch (pKonst_FuellePreparedStatementIntern) {
            case Konst_FuellePreparedStatementIntern_fuellePreparedStatementKomplett:
            case Konst_FuellePreparedStatementIntern_fuellePreparedStatementUpdateAktienregisterNew:
                pPStm.setInt(pOffset, pAktienregister.versandNummer);
                pOffset++;
                pPStm.setInt(pOffset, pAktienregister.datenUpdate);
                pOffset++;
                break;
            case Konst_FuellePreparedStatementIntern_fuellePreparedStatementInsertHistorieNew:
                pPStm.setInt(pOffset, 0);
                pOffset++;
                break;
            }

            switch (pKonst_FuellePreparedStatementIntern) {
            case Konst_FuellePreparedStatementIntern_fuellePreparedStatementKomplett:
                break;
            case Konst_FuellePreparedStatementIntern_fuellePreparedStatementUpdateAktienregisterNew:
                pPStm.setInt(pOffset, pAktienregister.aktienregisterIdent);
                pOffset++;
                pPStm.setLong(pOffset, pAktienregister.db_version);
                pOffset++;
                pPStm.setInt(pOffset, pAktienregister.mandant);
                pOffset++;
                break;
            case Konst_FuellePreparedStatementIntern_fuellePreparedStatementInsertHistorieNew:
                break;
            }

            if (pOffset - startOffset != anzfelder) {
                /* Nur wg. Überprüfung auf Programmierfehler - alle Felder berücksichtigt? */
                CaBug.drucke("002");
            }

        } catch (SQLException e) {
            CaBug.drucke("001");
            e.printStackTrace();
        }

    }

    /**
     * Fuellen Prepared Statement mit allen Feldern, beginnend bei offset. Kann
     * sowohl für Insert, als auch für update verwendet werden.
     * 
     * offset= Startposition des ersten Feldes (also z.B. 1)
     * 
     * anzfelder wird jeweils individuell gesetzt.
     */
    private int anzfelder = 64;

    private void fuellePreparedStatementKomplett(PreparedStatement pPStm, int pOffset,
            EclAktienregister pAktienregister) {

        fuellePreparedStatementIntern(pPStm, pOffset, pAktienregister,
                Konst_FuellePreparedStatementIntern_fuellePreparedStatementKomplett);
    }

    private String liefereInsertSql(int pKonst_LiefereInsertSql) {
        String lSql = "";

        lSql += "INSERT INTO " + dbBundle.getSchemaMandant();

        switch (pKonst_LiefereInsertSql) {
        case Konst_LiefereInsertSql_insert:
        case Konst_LiefereInsertSql_insertAktienregisterListe:
            lSql += "tbl_aktienregister ";
            break;
        case Konst_LiefereInsertSql_insertHistorieListe:
            lSql += "tbl_aktienregisterhistorie ";
            break;
        }

        /*46 Elemente*/
        lSql += "(" + "mandant, aktienregisterIdent, db_version, aktionaersnummer, " /*4*/
                + "personNatJur, anredeId, titel, name3, nachname, name1, vorname, name2, zusatz, " /*9*/
                + "strasse, postfach, postleitzahl, postleitzahlPostfach, ort, staatId, email, " /*7*/
                + "anredeIdVersand, titelVersand, name3Versand, nachnameVersand, name1Versand, vornameVersand, name2Versand, zusatzVersand, " /*8*/
                + "strasseVersand, postfachVersand, postleitzahlVersand, postleitzahlPostfachVersand, ortVersand, staatIdVersand, emailVersand, " /*7*/
                + "nameKomplett, istPersonengemeinschaft, istJuristischePerson, versandAbweichend, gattungId, isin, " /*6*/
                + "stueckAktien, stimmen, besitzart, stimmausschluss, gruppe, "; /*5*/

        switch (pKonst_LiefereInsertSql) {
        case Konst_LiefereInsertSql_insert:
        case Konst_LiefereInsertSql_insertAktienregisterListe:
            /*10 Elemente*/
            lSql += "adresszeile1, adresszeile2, adresszeile3, adresszeile4, adresszeile5, "
                    + "adresszeile6, adresszeile7, adresszeile8, adresszeile9, adresszeile10, ";
            break;
        case Konst_LiefereInsertSql_insertHistorieListe:
            break;
        }

        /*4 Elemente*/
        lSql += "englischerVersand, herkunftQuelle, herkunftQuelleLfd, herkunftIdent, ";

        switch (pKonst_LiefereInsertSql) {
        case Konst_LiefereInsertSql_insert:
        case Konst_LiefereInsertSql_insertAktienregisterListe:
            /*2 Elemente*/
            lSql += "versandNummer, datenUpdate ";
            break;
        case Konst_LiefereInsertSql_insertHistorieListe:
            /*1 Elemente*/
            lSql += "kennzeichen ";
            break;
        }

        lSql += ")" + "VALUES (";

        switch (pKonst_LiefereInsertSql) {
        case Konst_LiefereInsertSql_insert:
        case Konst_LiefereInsertSql_insertAktienregisterListe:
            /*62-51=11 Elemente*/
            lSql += "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ";
            break;
        case Konst_LiefereInsertSql_insertHistorieListe:
            /*51 Elemente*/
            break;
        }

        //      @formatter:off
        lSql+=
                  "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, " //10
                + "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, " //10
                + "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, " //10
                + "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, " //10
                + "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, " //10
                + "? " //1
                + ")";
//      @formatter:on

        return lSql;
    }

    public int insert(EclAktienregister lAktienregister) {

        int erg = 0;

        /* neue InterneIdent vergeben */
        erg = dbBundle.dbBasis.getInterneIdentAktienregisterEintrag();
        if (erg < 1) {
            CaBug.drucke("002");
            return (erg);
        }

        lAktienregister.aktienregisterIdent = erg;
        lAktienregister.mandant = dbBundle.clGlobalVar.mandant;

        /*
         * Satz einfügen: Verarbeitungshinweis: > nachdem InterneIdent immer eindeutig
         * vergeben werden, ist prinzipiell eine "Doppeleinfügung" von InterneIdent
         * nicht möglich. Sollte es dazu kommen, ist das immer ein Programmfehler!
         */

        try {

            /* Felder Neuanlage füllen */
            String lSql = liefereInsertSql(Konst_LiefereInsertSql_insert);

            PreparedStatement lPstm = verbindung.prepareStatement(lSql, ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);

            fuellePreparedStatementKomplett(lPstm, 1, lAktienregister);

            erg = 0; /*
                      * Falls Duplicate Key, dann wird exception geschmissen und erg wohl nicht
                      * gefüllt!
                      */
            erg = executeUpdate(lPstm);
            lPstm.close();
        } catch (Exception e2) {
            CaBug.drucke("001");
            System.err.println(" " + e2.getMessage());
        }

        if (erg == 0) {/* Fehler beim Einfügen - d.h. ZutrittsIdent bereits vorhanden */
            return (-1);
        }

        return (1);
    }

    /**
     * Überprüfen, ob insert möglich ist (intern: es wird ein read-Befehl
     * abgesetzt), oder mittlerweile bereits ein Satz vorhanden ist Gefüllt sein
     * muss: der Primary Key (in diesem Fall: aktionaersnummer; mandant wird
     * automatisch gefüllt)
     * 
     * true=insert ist noch möglich false=mittlerweile existiert ein Satz mit diese
     * Aktionärsnummer, oder es sit ein Fehler aufgetreten
     */
    public boolean insertCheck(EclAktienregister pAktienregisterEintrag) {
        int anzInArray = 0;
        PreparedStatement lPStm = null;

        if (pAktienregisterEintrag == null) {
            CaBug.drucke("DbAktienregister.pAktienregisterEintrag 001");
            return false;
        }

        try {
            String lSql = "SELECT * from " + dbBundle.getSchemaMandant() + "tbl_aktienregister are where "
                    + "are.mandant=? AND " + "are.aktionaersnummer=? ORDER BY are.aktionaersnummer;";
            lPStm = verbindung.prepareStatement(lSql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            lPStm.setInt(1, dbBundle.clGlobalVar.mandant);
            lPStm.setString(2, pAktienregisterEintrag.aktionaersnummer);

            ResultSet lErgebnis = executeQuery(lPStm);
            lErgebnis.last();
            anzInArray = lErgebnis.getRow();
            lErgebnis.beforeFirst();

            lErgebnis.close();
            lPStm.close();

            if (anzInArray > 0) {
                return false;
            } else {
                return true;
            }

        } catch (Exception e) {
            CaBug.drucke("DbAktienregister.pAktienregisterEintrag 003");
            System.err.println(" " + e.getMessage());
            return (false);
        }

    }

    /** Lesen nur aktienregisterIdent */
    public int leseZuAktienregisterIdent(int pIdent) {
        EclAktienregister lAktienregister = new EclAktienregister();
        lAktienregister.aktienregisterIdent = pIdent;
        return leseZuAktienregisterEintrag(lAktienregister);
    }

    /**
     * 
     */
    public int leseZuAktienregisterEintrag(EclAktienregister lAktienregisterEintrag) {
        return leseZuAktienregisterEintrag(lAktienregisterEintrag, 1);
    }

    /**
     * Realisiert derzeit, jeweils: aktienregisterIdent, aktionaersnummer,
     * besitzart, name1, gattungId
     * 
     * lAktienregisterEintrag==null => es werden alle gesucht!
     * 
     * pBehandlungNullBestand (aktuell nur für Suche nach Name!!!): 1 = alle
     * anzeigen (auch 0-Bestand) 2 = alle ohne 0-Bestand 3 = nur 0-Bestand
     */
    public int leseZuAktienregisterEintrag(EclAktienregister lAktienregisterEintrag, int pBehandlungNullBestand) {
        int anzInArray = 0;
        PreparedStatement pstm1 = null;
        try {

            if (lAktienregisterEintrag == null) {
                String sql = "SELECT * from " + dbBundle.getSchemaMandant() + "tbl_aktienregister are where "
                        + "are.mandant=?  ORDER BY are.aktienregisterIdent;";
                pstm1 = verbindung.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                pstm1.setInt(1, dbBundle.clGlobalVar.mandant);
            } else {
                if (lAktienregisterEintrag.aktienregisterIdent != 0) {
                    //                    System.out.println("aktienregisterIdent");
                    String sql = "SELECT * from " + dbBundle.getSchemaMandant() + "tbl_aktienregister are where "
                            + "are.mandant=? AND " + "are.aktienregisterIdent=? ORDER BY are.aktienregisterIdent;";
                    pstm1 = verbindung.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE,
                            ResultSet.CONCUR_READ_ONLY);
                    pstm1.setInt(1, dbBundle.clGlobalVar.mandant);
                    pstm1.setInt(2, lAktienregisterEintrag.aktienregisterIdent);
                }
                if (!lAktienregisterEintrag.aktionaersnummer.isEmpty()) {
                    CaBug.druckeLog("aktionaersnummer", logDrucken, 10);
                    /** Verändert! */
                    CaBug.druckeLog("lAktienregisterEintrag.aktionaersnummer vor Aufbereitung="
                            + lAktienregisterEintrag.aktionaersnummer, logDrucken, 10);
                    //                    lAktienregisterEintrag.aktionaersnummer = BlAktienregisterNummerAufbereiten
                    //                            .aufbereitenFuerDatenbankZugriff(lAktienregisterEintrag.aktionaersnummer,
                    //                                    dbBundle.param.paramBasis.laengeAktionaersnummer, dbBundle.clGlobalVar.mandant);
                    lAktienregisterEintrag.aktionaersnummer = BlNummernformBasis
                            .aufbereitenFuerDatenbankZugriff(lAktienregisterEintrag.aktionaersnummer, dbBundle);

                    String sql = "SELECT * from " + dbBundle.getSchemaMandant() + "tbl_aktienregister are where "
                            + "are.mandant=? AND " + "are.aktionaersnummer=? ORDER BY are.aktienregisterIdent;";
                    pstm1 = verbindung.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE,
                            ResultSet.CONCUR_READ_ONLY);
                    pstm1.setInt(1, dbBundle.clGlobalVar.mandant);
                    CaBug.druckeLog(
                            "lAktienregisterEintrag.aktionaersnummer=" + lAktienregisterEintrag.aktionaersnummer,
                            logDrucken, 10);
                    pstm1.setString(2, lAktienregisterEintrag.aktionaersnummer);
                }
                if (!lAktienregisterEintrag.besitzart.isEmpty()) {
                    CaBug.druckeLog("besitzart", logDrucken, 10);
                    String sql = "SELECT * from " + dbBundle.getSchemaMandant() + "tbl_aktienregister are where "
                            + "are.mandant=? AND " + "are.besitzart=? ORDER BY are.aktienregisterIdent;";
                    pstm1 = verbindung.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE,
                            ResultSet.CONCUR_READ_ONLY);
                    pstm1.setInt(1, dbBundle.clGlobalVar.mandant);
                    pstm1.setString(2, lAktienregisterEintrag.besitzart);
                }
                if (lAktienregisterEintrag.gattungId != 0) {
                    CaBug.druckeLog("gattungId", logDrucken, 10);
                    String sql = "SELECT * from " + dbBundle.getSchemaMandant() + "tbl_aktienregister are where "
                            + "are.mandant=? AND " + "are.gattungId=? ORDER BY are.aktienregisterIdent;";
                    pstm1 = verbindung.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE,
                            ResultSet.CONCUR_READ_ONLY);
                    pstm1.setInt(1, dbBundle.clGlobalVar.mandant);
                    pstm1.setInt(2, lAktienregisterEintrag.gattungId);
                }
                if (!lAktienregisterEintrag.name1.isEmpty()) {
                    CaBug.druckeLog("name1", logDrucken, 10);
                    String sql = "SELECT * from " + dbBundle.getSchemaMandant() + "tbl_aktienregister are where "
                            + "are.mandant=? AND ";
                    if (pBehandlungNullBestand == 2) {
                        sql = sql + "(are.stueckAktien!=0 OR are.stimmen!=0) AND ";
                    }
                    if (pBehandlungNullBestand == 3) {
                        sql = sql + "(are.stueckAktien=0 AND are.stimmen=0) AND ";
                    }

                    sql = sql
                            + "(are.name1 like ? OR are.name2 like ? OR are.name3 like ? OR are.nachname like ? OR are.vorname like ?) ORDER BY are.aktionaersnummer;";
                    pstm1 = verbindung.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE,
                            ResultSet.CONCUR_READ_ONLY);
                    pstm1.setInt(1, dbBundle.clGlobalVar.mandant);
                    pstm1.setString(2, "%" + lAktienregisterEintrag.name1 + "%");
                    pstm1.setString(3, "%" + lAktienregisterEintrag.name1 + "%");
                    pstm1.setString(4, "%" + lAktienregisterEintrag.name1 + "%");
                    pstm1.setString(5, "%" + lAktienregisterEintrag.name1 + "%");
                    pstm1.setString(6, "%" + lAktienregisterEintrag.name1 + "%");
                    CaBug.druckeLog("sql=" + sql, logDrucken, 10);
                }
            }

            ResultSet ergebnis = executeQuery(pstm1);
            ergebnis.last();
            anzInArray = ergebnis.getRow();
            ergebnis.beforeFirst();

            ergebnisArray = new EclAktienregister[anzInArray];

            int i = 0;
            while (ergebnis.next() == true) {

                ergebnisArray[i] = this.decodeErgebnis(ergebnis);
                i++;

            }
            ergebnis.close();
            pstm1.close();

        } catch (Exception e) {
            CaBug.drucke("DbAktienregister.leseZuAktienregisterEintrag 001");
            System.err.println(" " + e.getMessage());
        }

        return (anzInArray);
    }

    /**
     * Liest alle Einträge ein, die zu dieser Aktionärsnummer gehören. Unabhängig
     * von der "nachgestellten" Ziffer (1 oder 0). Darf nur für Namensaktien
     * aufgerufen werden!
     */
    public int leseZuAktienregisternummer(String pAktionaersnummer) {
        int anzInArray = 0;
        PreparedStatement pstm1 = null;
        try {
            String sql = "SELECT * from " + dbBundle.getSchemaMandant() + "tbl_aktienregister are where "
                    + "are.mandant=?  and are.aktionaersnummer like ? ORDER BY are.aktionaersnummer;";
            pstm1 = verbindung.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            pstm1.setInt(1, dbBundle.clGlobalVar.mandant);
            pstm1.setString(2, pAktionaersnummer + "%");

            ResultSet ergebnis = executeQuery(pstm1);
            ergebnis.last();
            anzInArray = ergebnis.getRow();
            ergebnis.beforeFirst();

            ergebnisArray = new EclAktienregister[anzInArray];

            int i = 0;
            while (ergebnis.next() == true) {

                ergebnisArray[i] = this.decodeErgebnis(ergebnis);
                i++;

            }
            ergebnis.close();
            pstm1.close();

        } catch (Exception e) {
            CaBug.drucke("DbAktienregister.leseZuAktienregisternummer 001");
            System.err.println(" " + e.getMessage());
        }

        return (anzInArray);
    }

    /**
     * Liest alle Aktienregistereinträge
     */
    public int leseAlle() {
        int anzInArray = 0;
        PreparedStatement pstm1 = null;
        try {
            String sql = "SELECT * from " + dbBundle.getSchemaMandant() + "tbl_aktienregister are where "
                    + "are.mandant=? ORDER BY are.aktionaersnummer;";
            pstm1 = verbindung.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            pstm1.setInt(1, dbBundle.clGlobalVar.mandant);

            ResultSet ergebnis = executeQuery(pstm1);
            ergebnis.last();
            anzInArray = ergebnis.getRow();
            ergebnis.beforeFirst();

            ergebnisArray = new EclAktienregister[anzInArray];

            int i = 0;
            while (ergebnis.next() == true) {

                ergebnisArray[i] = this.decodeErgebnis(ergebnis);
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

    public int lese0Bestand() {
        int anzInArray = 0;
        PreparedStatement pstm1 = null;
        try {
            String sql = "SELECT * from " + dbBundle.getSchemaMandant() + "tbl_aktienregister are where "
                    + "are.mandant=? AND are.stimmen=0 ORDER BY are.aktionaersnummer;";
            pstm1 = verbindung.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            pstm1.setInt(1, dbBundle.clGlobalVar.mandant);

            ResultSet ergebnis = executeQuery(pstm1);
            ergebnis.last();
            anzInArray = ergebnis.getRow();
            ergebnis.beforeFirst();

            ergebnisArray = new EclAktienregister[anzInArray];

            int i = 0;
            while (ergebnis.next() == true) {

                ergebnisArray[i] = this.decodeErgebnis(ergebnis);
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

    public int leseNamensfelder(String pName) {
        int anzInArray = 0;
        PreparedStatement pstm1 = null;
        try {
            // @formatter:off
            String sql = "SELECT * from " + dbBundle.getSchemaMandant() + "tbl_aktienregister are where "
                    + "are.nachname LIKE ? OR " 
                    + "are.name1 LIKE ? OR " 
                    + "are.vorname LIKE ? OR "
                    + "are.name2 LIKE ? OR " 
                    + "are.zusatz LIKE ? " 
                    + "ORDER BY are.aktionaersnummer;";
            // @formatter:on
            pstm1 = verbindung.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            pstm1.setString(1, "%" + pName + "%");
            pstm1.setString(2, "%" + pName + "%");
            pstm1.setString(3, "%" + pName + "%");
            pstm1.setString(4, "%" + pName + "%");
            pstm1.setString(5, "%" + pName + "%");

            ResultSet ergebnis = executeQuery(pstm1);
            ergebnis.last();
            anzInArray = ergebnis.getRow();
            ergebnis.beforeFirst();

            ergebnisArray = new EclAktienregister[anzInArray];

            int i = 0;
            while (ergebnis.next() == true) {

                ergebnisArray[i] = this.decodeErgebnis(ergebnis);
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

    public int leseAktienregisterOhneMeldung() {
        int anzInArray = 0;
        PreparedStatement pstm1 = null;
        try {

            String sql = "SELECT * FROM " + dbBundle.getSchemaMandant() + "tbl_meldungen m RIGHT OUTER JOIN "
                    + dbBundle.getSchemaMandant()
                    + "tbl_aktienregister are on m.aktionaersnummer=are.aktionaersnummer where m.aktionaersnummer is null";

            pstm1 = verbindung.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);

            ResultSet ergebnis = executeQuery(pstm1);
            ergebnis.last();
            anzInArray = ergebnis.getRow();
            ergebnis.beforeFirst();

            ergebnisArray = new EclAktienregister[anzInArray];

            int i = 0;
            while (ergebnis.next() == true) {

                ergebnisArray[i] = this.decodeErgebnis(ergebnis);
                i++;

            }
            ergebnis.close();
            pstm1.close();

        } catch (Exception e) {
            CaBug.drucke("DbAktienregister.leseZuAktienregisterEintrag 001");
            System.err.println(" " + e.getMessage());
        }

        return (anzInArray);
    }

    public List<EclAktienregister> readAll() {

        List<EclAktienregister> list = new ArrayList<>();

        String sql = "SELECT * from " + dbBundle.getSchemaMandant() + "tbl_aktienregister are where "
                + "are.mandant=? ORDER BY are.aktionaersnummer;";

        try (PreparedStatement pstmt = verbindung.prepareStatement(sql)) {
            pstmt.setInt(1, dbBundle.clGlobalVar.mandant);

            try (ResultSet rs = executeQuery(pstmt)) {

                while (rs.next())
                    list.add(this.decodeErgebnis(rs));

            }

        } catch (Exception e) {
            CaBug.drucke("DbAktienregister.readAll 001");
            System.err.println(" " + e.getMessage());
        }

        return list;
    }

    public List<EclAktienregister> readSearchOffset(String string, String sort, int off, int limit) {

        if (off == 0)
            count = readSearchCount(string);

        // @formatter:off
        String sql = "SELECT are.*, IF(group_concat(m.meldungAktiv) LIKE '%1%', '1', '') AS meldungaktiv,"
                + " IFNULL(group_concat(m.zutrittsIdent), '') AS zutrittsident,"
                + " IF(group_concat(m.meldungsTyp) LIKE '%" + KonstMeldung.KARTENART_IN_SAMMELKARTE + "%', '1', '') AS insammelkarte,"
                + " IFNULL(IF(group_concat(m.meldungEnthaltenInSammelkarte) LIKE '0', '', group_concat(m.meldungEnthaltenInSammelkarte)), '') AS sammelkarte,"
                + " IF(group_concat(m.statusPraesenz) LIKE '%1%' OR group_concat(m.statusPraesenz) LIKE '%4%', '1', '') AS statuspraesenz, group_concat(m.meldungsIdent)"
                + " from " + dbBundle.getSchemaMandant() + "tbl_aktienregister are"
                + " LEFT JOIN " + dbBundle.getSchemaMandant() + "tbl_meldungen m ON are.aktienregisterIdent = m.aktienregisterIdent"
                + " WHERE are.mandant=?" + string
                + " GROUP BY are.aktienregisterIdent"
                + " ORDER BY " + sort 
                + " LIMIT " + off + ", " + limit;
        // @formatter:on

        // System.out.println(sql);

        List<EclAktienregister> list = new ArrayList<>();

        try (PreparedStatement pstmt = verbindung.prepareStatement(sql)) {
            pstmt.setInt(1, dbBundle.clGlobalVar.mandant);

            try (ResultSet rs = executeQuery(pstmt)) {
                while (rs.next()) {
                    EclAktienregister tmpAktienregister = this.decodeErgebnis(rs);
                    tmpAktienregister.meldungAktiv = rs.getString("meldungaktiv");
                    tmpAktienregister.zutrittsIdent = rs.getString("zutrittsident");
                    tmpAktienregister.zutrittsIdent = tmpAktienregister.zutrittsIdent.startsWith(",")
                            ? tmpAktienregister.zutrittsIdent.substring(1).replaceAll(",", " - ")
                            : tmpAktienregister.zutrittsIdent.replaceAll(",", " - ");
                    tmpAktienregister.inSammelkarte = rs.getString("insammelkarte");
                    tmpAktienregister.sammelkarte = rs.getString("sammelkarte");
                    tmpAktienregister.statusPraesenz = rs.getString("statuspraesenz");
                    tmpAktienregister.meldungsIdent = rs.getString("group_concat(m.meldungsIdent)");

                    if (tmpAktienregister.nachname.isBlank())
                        tmpAktienregister.nachname = tmpAktienregister.nameKomplett;

                    list.add(tmpAktienregister);
                }
            }

        } catch (Exception e) {
            CaBug.drucke("DbAktienregister.readAll 001");
            System.err.println(" " + e.getMessage());
        }
        return list;
    }

    private int readSearchCount(String string) {

        String sql = "SELECT COUNT(*) from " + dbBundle.getSchemaMandant() + "tbl_aktienregister are where "
                + "are.mandant=?" + string;

        int c = 0;

        try (PreparedStatement pstmt = verbindung.prepareStatement(sql)) {
            pstmt.setInt(1, dbBundle.clGlobalVar.mandant);

            try (ResultSet rs = executeQuery(pstmt)) {
                if (rs.next()) {
                    c = rs.getInt(1);
                }
            }

        } catch (Exception e) {
            CaBug.drucke("DbAktienregister.readAll 001");
            System.err.println(" " + e.getMessage());
        }
        return c;
    }

    public List<EclIsin> readIsinDistinct() {

        List<EclIsin> list = new ArrayList<>();

        String sql = "SELECT distinct(isin), gattungId FROM " + dbBundle.getSchemaMandant()
                + "tbl_aktienregister WHERE ISIN != ''";

        try (PreparedStatement pstmt = verbindung.prepareStatement(sql)) {

            try (ResultSet rs = executeQuery(pstmt)) {

                while (rs.next()) {
                    if (!rs.getString(1).isBlank()) {
                        list.add(new EclIsin(rs.getInt(2), rs.getString(1)));
                    }
                }
            }
        } catch (Exception e) {
            CaBug.drucke("DbAktienregister.read ISIN 001");
            System.err.println(" " + e.getMessage());
        }
        return list;
    }

    public int readHighestIdent() {

        int id = 0;

        String sql = "SELECT max(aktienregisterIdent) FROM " + dbBundle.getSchemaMandant() + "tbl_aktienregister";

        try (PreparedStatement pstmt = verbindung.prepareStatement(sql)) {
            try (ResultSet rs = executeQuery(pstmt)) {

                if (rs.next())
                    id = rs.getInt(1);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return id;
    }

    public int readHighestVersandnummer() {

        int id = 0;

        String sql = "SELECT max(versandnummer) FROM " + dbBundle.getSchemaMandant() + "tbl_aktienregister";

        try (PreparedStatement pstmt = verbindung.prepareStatement(sql)) {
            try (ResultSet rs = executeQuery(pstmt)) {

                if (rs.next())
                    id = rs.getInt(1);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return id;
    }

    public long[] leseGesamtbestand() {

        long[] bestand = { 0, 0 };

        String sql = "SELECT sum(stimmen), sum(stueckAktien) FROM " + dbBundle.getSchemaMandant()
                + "tbl_aktienregister";

        try (PreparedStatement pstmt = verbindung.prepareStatement(sql)) {
            try (ResultSet rs = executeQuery(pstmt)) {

                if (rs.next())
                    bestand = new long[] { rs.getLong(1), rs.getLong(2) };
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return bestand;
    }

    private String liefereUpdateSql(int pKonst_LiefereUpdateSql) {
        String lSql = "";

        lSql = "UPDATE " + dbBundle.getSchemaMandant() + "tbl_aktienregister SET ";

        switch (pKonst_LiefereUpdateSql) {
        case Konst_LiefereUpdateSql_update:
            lSql += "mandant=?, aktienregisterIdent=?, ";
            break;
        case Konst_LiefereUpdateSql_updateAktienregisterListe:
            break;
        }

        lSql += "db_version=?, aktionaersnummer=?, "
                + "personNatJur=?, anredeId=?, titel=?, name3=?, nachname=?, name1=?, vorname=?, name2=?, zusatz=?, "
                + "strasse=?, postfach=?, postleitzahl=?, postleitzahlPostfach=?, ort=?, staatId=?, email=?, "
                + "anredeIdVersand=?, titelVersand=?, name3Versand=?, nachnameVersand=?, name1Versand=?, vornameVersand=?, name2Versand=?, zusatzVersand=?, "
                + "strasseVersand=?, postfachVersand=?, postleitzahlVersand=?, postleitzahlPostfachVersand=?, ortVersand=?, staatIdVersand=?, emailVersand=?, "
                + "nameKomplett=?, istPersonengemeinschaft=?, istJuristischePerson=?, versandAbweichend=?, gattungId=?, isin=?, "
                + "stueckAktien=?, stimmen=?, besitzart=?, stimmausschluss=?, gruppe=?, "
                + "adresszeile1=?, adresszeile2=?, adresszeile3=?, adresszeile4=?, adresszeile5=?, "
                + "adresszeile6=?, adresszeile7=?, adresszeile8=?, adresszeile9=?, adresszeile10=?, "
                + "englischerVersand=?, herkunftQuelle=?, herkunftQuelleLfd=?, herkunftIdent=?, versandNummer=?, datenUpdate=? "
                + "WHERE ";

        switch (pKonst_LiefereUpdateSql) {
        case Konst_LiefereUpdateSql_update:
        case Konst_LiefereUpdateSql_updateAktienregisterListe:
            lSql += "aktienregisterIdent=? AND db_version=? AND mandant=?";
            break;
        }

        return lSql;
    }

    /**
     * Update einer Willenserklärung. Versionsnummer wird um 1 hochgezählt
     */
    public int update(EclAktienregister lAktienregister) {

        lAktienregister.db_version++;
        try {

            String sql = liefereUpdateSql(Konst_LiefereUpdateSql_update);

            PreparedStatement lPStm = verbindung.prepareStatement(sql);
            fuellePreparedStatementKomplett(lPStm, 1, lAktienregister);
            lPStm.setInt(anzfelder + 1, lAktienregister.aktienregisterIdent);
            lPStm.setLong(anzfelder + 2, lAktienregister.db_version - 1);
            lPStm.setLong(anzfelder + 3, dbBundle.clGlobalVar.mandant);

            int ergebnis = executeUpdate(lPStm);
            lPStm.close();
            if (ergebnis == 0) {
                return (CaFehler.pfXyWurdeVonAnderemBenutzerVeraendert);
            }
        } catch (Exception e1) {
            CaBug.drucke("DbAktienregister.update 001");
            System.err.println(" " + e1.getMessage());
            return (CaFehler.pfdXyBereitsVorhanden);
        }
        return (1);
    }

    /**
     * Update Aktienregister, mit Prüfung ob ein Update in 
     * Willenserklärung oder PersonNatJur notwendig ist
     * 
     * Neue Funktion erstellt da die normale update noch in meetComImport
     * verwendet wird.
     */
    public int updateNew(EclAktienregister eintrag) {

        eintrag.db_version++;

        String sql = liefereUpdateSql(Konst_LiefereUpdateSql_update);

        try (PreparedStatement pstmt = verbindung.prepareStatement(sql)) {
            fuellePreparedStatementKomplett(pstmt, 1, eintrag);
            pstmt.setInt(anzfelder + 1, eintrag.aktienregisterIdent);
            pstmt.setLong(anzfelder + 2, eintrag.db_version - 1);
            pstmt.setLong(anzfelder + 3, dbBundle.clGlobalVar.mandant);

            int ergebnis = executeUpdate(pstmt);
            if (ergebnis == 0)
                return (CaFehler.pfXyWurdeVonAnderemBenutzerVeraendert);

            updateWillenserklaerung(eintrag);

            updatePersonNatJur(eintrag);

        } catch (SQLException e1) {
            CaBug.drucke("DbAktienregister.update 001");
            System.err.println(" " + e1.getMessage());
            return (CaFehler.pfdXyBereitsVorhanden);
        }
        return (1);
    }

    /**
     * Update PersonNatJur im Aktienregister
     */
    public int updatePersonNatJur(EclAktienregister eintrag, int pPersonNatJurIdent) {

        try {

            String sql = "UPDATE " + dbBundle.getSchemaMandant() + "tbl_aktienregister SET "
                    + "db_version=?, personNatJur=? " + "WHERE " + "aktienregisterIdent=? AND " + "mandant=?";

            PreparedStatement lPStm = verbindung.prepareStatement(sql);
            lPStm.setLong(1, ++eintrag.db_version);
            lPStm.setInt(2, pPersonNatJurIdent);
            lPStm.setInt(3, eintrag.aktienregisterIdent);
            lPStm.setInt(4, dbBundle.clGlobalVar.mandant);

            int ergebnis = executeUpdate(lPStm);
            lPStm.close();
            if (ergebnis == 0) {
                return (CaFehler.pfXyWurdeVonAnderemBenutzerVeraendert);
            }
        } catch (Exception e1) {
            CaBug.drucke("001");
            System.err.println(" " + e1.getMessage());
            return (CaFehler.pfdXyBereitsVorhanden);
        }

        return (1);
    }

    public int delete0Bestand() {
        int erg = 0;

        try {

            String sql1 = "DELETE FROM " + dbBundle.getSchemaMandant() + "tbl_aktienregister where stueckAktien=0;";
            PreparedStatement pstm1 = verbindung.prepareStatement(sql1);

            erg = 0; /*
                      * Falls Duplicate Key, dann wird exception geschmissen und erg wohl nicht
                      * gefüllt!
                      */
            erg = executeUpdate(pstm1);

            pstm1.close();
        } catch (Exception e2) {
            CaBug.drucke("001");
            System.err.println(" " + e2.getMessage());
            return (erg);
        }

        return 1;
    }

    /**
     * Versandnummer 1 setzen Fuer Dauerportale nach dem ersten Import um wieder die
     * ganze Versandliste auszugeben
     */
    public int updateVersandnummer() {

        int erg = 0;

        String sql = "UPDATE " + dbBundle.getSchemaMandant() + "tbl_aktienregister SET versandnummer=1 WHERE mandant=?";

        try (PreparedStatement pstmt = verbindung.prepareStatement(sql)) {

            pstmt.setInt(1, dbBundle.clGlobalVar.mandant);

            erg = executeUpdate(pstmt);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return erg;
    }

    /**
     * Entfernt alle 'PV' Kennzeichen in der jeweiligen Versandnummer
     * Wird aktuell nur bei ku164 verwendet
     */
    public int updateEmail(int versandnummer) {

        int erg = 0;

        String sql = "UPDATE " + dbBundle.getSchemaMandant() + "tbl_aktienregister "
                + "SET email='' WHERE email='PV' and mandant=? and versandnummer=?";

        try (PreparedStatement pstmt = verbindung.prepareStatement(sql)) {

            pstmt.setInt(1, dbBundle.clGlobalVar.mandant);
            pstmt.setInt(2, versandnummer);

            erg = executeUpdate(pstmt);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return erg;
    }

    /*
     * Ab hier Funktionen fuer den WebService vom Aktienregister import
     */

    public int insertAktienregisterListe(List<EclAktienregister> list) {

        int erg = 0;

        String sql = liefereInsertSql(Konst_LiefereInsertSql_insertAktienregisterListe);

        try (PreparedStatement pstmt = verbindung.prepareStatement(sql)) {

            for (EclAktienregister pData : list) {

                fuellePreparedStatementKomplett(pstmt, 1, pData);
                executeUpdate(pstmt);
                erg++;

                if (erg % 1000 == 0)
                    CaBug.out(erg);
            }

            return erg;

        } catch (SQLException e) {
            e.printStackTrace();
            CaBug.out(e.toString());
            return -1;
        }
    }

    private void fuellePreparedStatementUpdateAktienregisterNew(PreparedStatement pstmt, EclAktienregister pData,
            int offset) {

        fuellePreparedStatementIntern(pstmt, offset, pData,
                Konst_FuellePreparedStatementIntern_fuellePreparedStatementUpdateAktienregisterNew);
    }

    public int updateAktienregisterListe(List<EclAktienregister> list) {

        int erg = 0;

        String sql = liefereUpdateSql(Konst_LiefereUpdateSql_updateAktienregisterListe);

        try (PreparedStatement pstmt = verbindung.prepareStatement(sql)) {

            for (EclAktienregister lAktienregisterEintrag : list) {

                updateWillenserklaerung(lAktienregisterEintrag);

                updatePersonNatJur(lAktienregisterEintrag);

                fuellePreparedStatementUpdateAktienregisterNew(pstmt, lAktienregisterEintrag, 1);

                int c = executeUpdate(pstmt);

                // Datensatz wurde in der Zwischenzeit veraendert
                if (c == 0) {

                    // Datensatz neu einlesen
                    leseZuAktienregisterIdent(lAktienregisterEintrag.aktienregisterIdent);

                    // Prufen ob vorhanden
                    if (anzErgebnis() > 0) {
                        final EclAktienregister oldEintrag = ergebnisArray[0];

                        // neue db_version zuordnen
                        lAktienregisterEintrag.db_version = oldEintrag.db_version;

                        // Pruefen ob sich die Anschrift veraendert hat
                        // true -> Update PersonNatJur
                        if (oldEintrag.personNatJur > 0) {
                            lAktienregisterEintrag.personNatJur = oldEintrag.personNatJur;
                            lAktienregisterEintrag.personNatJurGeaendert = BlAktienregister
                                    .checkPersonUpdate(lAktienregisterEintrag, oldEintrag);
                            updatePersonNatJur(lAktienregisterEintrag);
                        }
                        // Udpate Aktienregister
                        c = update(lAktienregisterEintrag);
                    }
                }
                // return erg -> erg == list.size();
                if (c == 1) {
                    erg++;
                }
            }
            return erg;

        } catch (SQLException e) {
            System.out.println(e.toString());
            return -1;
        }
    }

    private void updateWillenserklaerung(EclAktienregister eintrag) {

        if (eintrag.getBesitzGeaendert()) {

            dbBundle.dbMeldungen.leseZuAktienregisterIdent(eintrag.getAktienregisterIdent(), false);

            for (EclMeldung meldung : dbBundle.dbMeldungen.meldungenArray) {
                meldung.setBesitzart(eintrag.getBesitzart());
                dbBundle.dbMeldungen.update(meldung, false);
            }
        }

        if (eintrag.getBestandGeaendert()) {

            BlWillenserklaerung blWillenserklaerung = new BlWillenserklaerung();
            blWillenserklaerung.pEclAktienregisterEintrag = eintrag;
            blWillenserklaerung.pAktienAnmelden = eintrag.stimmen;
            blWillenserklaerung.anmeldungAendernAktienbestandRegister(dbBundle);
            if (blWillenserklaerung.rcGrundFuerUnzulaessig < 0
                    && blWillenserklaerung.rcGrundFuerUnzulaessig != CaFehler.pmKeineAnmeldungenVorhanden) {
                System.out.println("Bestand aendern für Aktionaersnummer=" + eintrag.aktionaersnummer
                        + " Fehler/Warnung=" + Integer.toString(blWillenserklaerung.rcGrundFuerUnzulaessig));
            }
        }
    }

    private void updatePersonNatJur(EclAktienregister eintrag) {

        if (eintrag.personNatJur != 0 && eintrag.personNatJurGeaendert) {

            BlPersonenNatJur blPersonenNatJur = new BlPersonenNatJur();
            dbBundle.dbPersonenNatJur.read(eintrag.personNatJur);
            dbBundle.dbStaaten.readId(eintrag.staatId);
            final String laendercode = (dbBundle.dbStaaten.anzErgebnis() > 0)
                    ? dbBundle.dbStaaten.ergebnisPosition(0).code
                    : "";

            if (dbBundle.dbPersonenNatJur.anzPersonenNatJurGefunden() > 0) {
                final EclPersonenNatJur personUpdate = blPersonenNatJur.updateAusAktienregister(
                        dbBundle.dbPersonenNatJur.personenNatJurArray[0], eintrag, laendercode);

                dbBundle.dbPersonenNatJur.update(personUpdate);
            }
        }
    }

    private void fuellePreparedStatementInsertHistorieNew(PreparedStatement pstmt, EclAktienregister registerEintrag,
            int offset) throws SQLException {

        fuellePreparedStatementIntern(pstmt, offset, registerEintrag,
                Konst_FuellePreparedStatementIntern_fuellePreparedStatementInsertHistorieNew);
    }

    public int insertHistorieListe(List<EclAktienregister> list) {

        int erg = 0;

        String sql = liefereInsertSql(Konst_LiefereInsertSql_insertHistorieListe);

        try (PreparedStatement pstmt = verbindung.prepareStatement(sql)) {

            for (EclAktienregister pData : list) {

                fuellePreparedStatementInsertHistorieNew(pstmt, pData, 1);
                executeUpdate(pstmt);

                erg++;
                if (erg % 1000 == 0) {
                    System.out.println(erg);
                }
            }

            return erg;

        } catch (SQLException e) {
            System.out.println(e.toString());
            return -1;
        }
    }

    public List<EclAktienregister> readChangeNames() {

        List<EclAktienregister> list = new ArrayList<>();

        String sql = "SELECT * from " + dbBundle.getSchemaMandant() + "tbl_aktienregister are "
                + "WHERE are.personNatJur != 0";

        try (PreparedStatement pstmt = verbindung.prepareStatement(sql)) {

            try (ResultSet rs = executeQuery(pstmt)) {

                while (rs.next())
                    list.add(this.decodeErgebnis(rs));

            }

        } catch (Exception e) {
            CaBug.drucke("DbAktienregister.readAll 001");
            System.err.println(" " + e.getMessage());
        }

        return list;
    }

}