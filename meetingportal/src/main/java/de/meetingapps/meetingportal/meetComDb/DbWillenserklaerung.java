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
import de.meetingapps.meetingportal.meetComAllg.CaDatumZeit;
import de.meetingapps.meetingportal.meetComAllg.CaFehler;
import de.meetingapps.meetingportal.meetComEntities.EclMeldung;
import de.meetingapps.meetingportal.meetComEntities.EclWillenserklaerung;
import de.meetingapps.meetingportal.meetComEntities.EclWillenserklaerungZusatz;
import de.meetingapps.meetingportal.meetComEntities.EclZutrittsIdent;
import de.meetingapps.meetingportal.meetComKonst.KonstWillenserklaerung;

public class DbWillenserklaerung {

    private int logDrucken=3;
    
    private Connection verbindung = null;
    private DbBasis dbBasis = null;
    private DbBundle dbBundle = null;

    /**Soll nicht direkt verwendet werden, sondern nur über die Zugriffsfunktionen!
     * Aktuell noch public, da direkter Zugriff über DbMeldungen
     */
    public EclWillenserklaerung willenserklaerungArray[] = null;

    /*************************Initialisierung***************************/
    /* Verbindung in lokale Daten eintragen*/
    public DbWillenserklaerung(DbBundle datenbankbundle) {
        if (datenbankbundle.dbBasis == null) {
            System.err.println("vmcdbBasis nicht initialisiert");
            return;
        }
        dbBasis = datenbankbundle.dbBasis;
        verbindung = datenbankbundle.dbBasis.verbindung;
        dbBundle = datenbankbundle;
    }

    public int anzWillenserklaerungGefunden() {
        if (willenserklaerungArray == null) {
            return 0;
        }
        return willenserklaerungArray.length;
    }

    public EclWillenserklaerung willenserklaerungGefunden(int lfd) {
        return willenserklaerungArray[lfd];
    }

    public int createTable() {
        int rc = 0;
        DbLowLevel lDbLowLevel = new DbLowLevel(dbBundle);
        rc = lDbLowLevel.createTable("CREATE TABLE " + dbBundle.getSchemaMandant() + "tbl_willenserklaerung ( "
                + "`mandant` int(11) NOT NULL, " + "`willenserklaerungIdent` int(11) NOT NULL, "
                + "`db_version` bigint(20) DEFAULT NULL, " + "`identifikationDurch` int(11) DEFAULT NULL, "
                + "`identifikationZutrittsIdent` char(20) DEFAULT NULL, "
                + "`identifikationZutrittsIdentNeben` char(2) DEFAULT NULL, "
                + "`identifikationKlasse` int(11) DEFAULT NULL, " + "`identifikationStimmkarte` char(20) DEFAULT NULL, "
                + "`identifikationStimmkarteSecond` char(20) DEFAULT NULL, " + "`erteiltAufWeg` int(11) DEFAULT NULL, "
                + "`meldungsIdent` int(11) DEFAULT NULL, " + "`meldungsIdentGast` int(11) DEFAULT NULL, "
                + "`zuVerzeichnisNr1` int(11) DEFAULT NULL, " + "`zuVerzeichnisNr2` int(11) DEFAULT NULL, "
                + "`zuVerzeichnisNr3` int(11) DEFAULT NULL, " + "`zuVerzeichnisNr4` int(11) DEFAULT NULL, "
                + "`zuVerzeichnisNr1Gedruckt` int(11) DEFAULT NULL, "
                + "`zuVerzeichnisNr2Gedruckt` int(11) DEFAULT NULL, "
                + "`zuVerzeichnisNr3Gedruckt` int(11) DEFAULT NULL, "
                + "`zuVerzeichnisNr4Gedruckt` int(11) DEFAULT NULL, " + "`willenserklaerung` int(11) DEFAULT NULL, "
                + "`mc_delayed` int(11) DEFAULT NULL, " + "`pending` int(11) DEFAULT NULL, "
                + "`zutrittsIdent` char(20) DEFAULT NULL, " + "`zutrittsIdentNeben` char(2) DEFAULT NULL, "
                + "`zutrittsIdentErsetzt` char(20) DEFAULT NULL, "
                + "`zutrittsIdentNebenErsetzt` char(2) DEFAULT NULL, " + "`stimmkarte1` varchar(20) DEFAULT NULL, "
                + "`stimmkarte2` varchar(20) DEFAULT NULL, " + "`stimmkarte3` varchar(20) DEFAULT NULL, "
                + "`stimmkarte4` varchar(20) DEFAULT NULL, " + "`stimmkarteSecond` varchar(20) DEFAULT NULL, "
                + "`stimmen` bigint(20) DEFAULT NULL, " + "`aktien` bigint(20) DEFAULT NULL, "
                + "`verweisart` int(11) DEFAULT NULL, " + "`verweisAufWillenserklaerung` int(11) DEFAULT NULL, "
                + "`folgeBuchungFuerIdent` int(11) DEFAULT NULL, "
                + "`willenserklaerungGeberIdent` int(11) DEFAULT NULL, "
                + "`bevollmaechtigterDritterIdent` int(11) DEFAULT NULL, "
                + "`identMeldungZuSammelkarte` int(11) DEFAULT NULL, "
                + "`identGeaenderteMeldungZuSammelkarte` int(11) DEFAULT NULL, "
                + "`veraenderungszeit` char(19) DEFAULT NULL, " + "`benutzernr` int(11) DEFAULT NULL, "
                + "`arbeitsplatz` int(11) DEFAULT NULL, " + "`istKontrolliert` int(11) DEFAULT NULL, "
                + "`protokollnr` int(11) DEFAULT NULL, " + "`storniert` int(11) DEFAULT NULL, "
                + "`storniert_delayed` int(11) DEFAULT NULL, " + "`erzeugtUeber` int(11) DEFAULT NULL, "
                + "`erzeugtUeberLauf` int(11) DEFAULT NULL, " 
                + "`exportiertUeberLauf` int(11) DEFAULT '0', " 
                + "PRIMARY KEY (`willenserklaerungIdent`,`mandant`), "
                + "KEY `IDX_meldungsIdent` (`meldungsIdent`), "
                + "KEY `IDX_bevollmaechtigterIdent` (`bevollmaechtigterDritterIdent`) " + ") ");
        return rc;
    }

    /**************************deleteAll Löschen aller Datensätze  eines Mandanten****************/
    public int deleteAll() {
        dbBasis.resetInterneIdentWillenserklaerung();
        return dbBundle.dbLowLevel
                .deleteMandant("DELETE FROM " + dbBundle.getSchemaMandant() + "tbl_willenserklaerung where mandant=?;");
    }

    /**************************setzt aktuelle Mandantennummer bei allen Datensätzen****************/
    public int updateMandant() {
        return dbBundle.dbLowLevel.rawUpdateMandant(dbBundle.getSchemaMandant() + "tbl_willenserklaerung");
    }

    public void reorgInterneIdent() {
        int lMax = dbBundle.dbLowLevel.liefereHoechsteIdent("SELECT MAX(willenserklaerungIdent) FROM "
                + dbBundle.getSchemaMandant() + "tbl_willenserklaerung ab where ab.mandant=? ");
        if (lMax != -1) {
            dbBundle.dbBasis.resetInterneIdentWillenserklaerung(lMax);
        }
    }

    /** dekodiert die aktuelle Position aus ergebnis in EclWillenserklaerung und gibt dieses zurück*/
    EclWillenserklaerung decodeErgebnis(ResultSet ergebnis) {
        EclWillenserklaerung lWillenserklaerung = new EclWillenserklaerung();

        try {

            lWillenserklaerung.mandant = ergebnis.getInt("w.mandant");
            lWillenserklaerung.willenserklaerungIdent = ergebnis.getInt("w.willenserklaerungIdent");
            lWillenserklaerung.db_version = ergebnis.getLong("w.db_version");

            lWillenserklaerung.identifikationDurch = ergebnis.getInt("w.identifikationDurch");
            lWillenserklaerung.identifikationZutrittsIdent = ergebnis.getString("w.identifikationZutrittsIdent");
            lWillenserklaerung.identifikationZutrittsIdentNeben = ergebnis
                    .getString("w.identifikationZutrittsIdentNeben");
            lWillenserklaerung.identifikationKlasse = ergebnis.getInt("w.identifikationKlasse");
            lWillenserklaerung.identifikationStimmkarte = ergebnis.getString("w.identifikationStimmkarte");
            lWillenserklaerung.identifikationStimmkarteSecond = ergebnis.getString("w.identifikationStimmkarteSecond");
            lWillenserklaerung.erteiltAufWeg = ergebnis.getInt("w.erteiltAufWeg");

            lWillenserklaerung.meldungsIdent = ergebnis.getInt("w.meldungsIdent");
            lWillenserklaerung.meldungsIdentGast = ergebnis.getInt("w.meldungsIdentGast");

            lWillenserklaerung.zuVerzeichnisNr1 = ergebnis.getInt("w.zuVerzeichnisNr1");
            lWillenserklaerung.zuVerzeichnisNr2 = ergebnis.getInt("w.zuVerzeichnisNr2");
            lWillenserklaerung.zuVerzeichnisNr3 = ergebnis.getInt("w.zuVerzeichnisNr3");
            lWillenserklaerung.zuVerzeichnisNr4 = ergebnis.getInt("w.zuVerzeichnisNr4");

            lWillenserklaerung.zuVerzeichnisNr1Gedruckt = ergebnis.getInt("w.zuVerzeichnisNr1Gedruckt");
            lWillenserklaerung.zuVerzeichnisNr2Gedruckt = ergebnis.getInt("w.zuVerzeichnisNr2Gedruckt");
            lWillenserklaerung.zuVerzeichnisNr3Gedruckt = ergebnis.getInt("w.zuVerzeichnisNr3Gedruckt");
            lWillenserklaerung.zuVerzeichnisNr4Gedruckt = ergebnis.getInt("w.zuVerzeichnisNr4Gedruckt");

            lWillenserklaerung.willenserklaerung = ergebnis.getInt("w.willenserklaerung");
            lWillenserklaerung.delayed = ergebnis.getInt("w.mc_delayed");
            lWillenserklaerung.pending = ergebnis.getInt("w.pending");

            lWillenserklaerung.zutrittsIdent = ergebnis.getString("w.zutrittsIdent");
            lWillenserklaerung.zutrittsIdentNeben = ergebnis.getString("w.zutrittsIdentNeben");
            lWillenserklaerung.zutrittsIdentNebenErsetzt = ergebnis.getString("w.zutrittsIdentNebenErsetzt");

            lWillenserklaerung.stimmkarte1 = ergebnis.getString("w.stimmkarte1");
            lWillenserklaerung.stimmkarte2 = ergebnis.getString("w.stimmkarte2");
            lWillenserklaerung.stimmkarte3 = ergebnis.getString("w.stimmkarte3");
            lWillenserklaerung.stimmkarte4 = ergebnis.getString("w.stimmkarte4");
            lWillenserklaerung.stimmkarteSecond = ergebnis.getString("w.stimmkarteSecond");

            lWillenserklaerung.stimmen = ergebnis.getLong("w.stimmen");
            lWillenserklaerung.aktien = ergebnis.getLong("w.aktien");

            lWillenserklaerung.verweisart = ergebnis.getInt("w.verweisart");
            lWillenserklaerung.verweisAufWillenserklaerung = ergebnis.getInt("w.verweisAufWillenserklaerung");
            lWillenserklaerung.folgeBuchungFuerIdent = ergebnis.getInt("w.folgeBuchungFuerIdent");

            lWillenserklaerung.willenserklaerungGeberIdent = ergebnis.getInt("w.willenserklaerungGeberIdent");
            lWillenserklaerung.bevollmaechtigterDritterIdent = ergebnis.getInt("w.bevollmaechtigterDritterIdent");
            lWillenserklaerung.identMeldungZuSammelkarte = ergebnis.getInt("w.identMeldungZuSammelkarte");
            lWillenserklaerung.identGeaenderteMeldungZuSammelkarte = ergebnis
                    .getInt("w.identGeaenderteMeldungZuSammelkarte");

            lWillenserklaerung.veraenderungszeit = ergebnis.getString("w.veraenderungszeit");
            lWillenserklaerung.benutzernr = ergebnis.getInt("w.benutzernr");
            lWillenserklaerung.arbeitsplatz = ergebnis.getInt("w.arbeitsplatz");
            lWillenserklaerung.istKontrolliert = ergebnis.getInt("w.istKontrolliert");
            lWillenserklaerung.protokollnr = ergebnis.getInt("w.protokollnr");

            lWillenserklaerung.storniert = ergebnis.getInt("w.storniert");
            lWillenserklaerung.storniert_delayed = ergebnis.getInt("w.storniert_delayed");
            lWillenserklaerung.erzeugtUeber = ergebnis.getInt("w.erzeugtUeber");
            lWillenserklaerung.erzeugtUeberLauf = ergebnis.getInt("w.erzeugtUeberLauf");
            lWillenserklaerung.exportiertUeberLauf = ergebnis.getInt("w.exportiertUeberLauf");

        } catch (Exception e) {
            CaBug.drucke("DbWillenserklaerung.decodeErgebnis 001");
            System.err.println(" " + e.getMessage());
        }

        return lWillenserklaerung;
    }

    /** Fuellen Prepared Statement mit allen Feldern, beginnend bei offset.
     * Kann sowohl für Insert, als auch für update verwendet werden.
     * 
     * offset= Startposition des ersten Feldes (also z.B. 1)
     */
    private int anzfelder = 51;

    private void fuellePreparedStatementKomplett(PreparedStatement pstm, int offset,
            EclWillenserklaerung lWillenserklaerung) {

        try {
            pstm.setInt(offset, lWillenserklaerung.mandant);
            offset++;
            pstm.setInt(offset, lWillenserklaerung.willenserklaerungIdent);
            offset++;
            pstm.setLong(offset, lWillenserklaerung.db_version);
            offset++;
            pstm.setInt(offset, lWillenserklaerung.identifikationDurch);
            offset++;
            pstm.setString(offset, lWillenserklaerung.identifikationZutrittsIdent);
            offset++;
            pstm.setString(offset, lWillenserklaerung.identifikationZutrittsIdentNeben);
            offset++;
            pstm.setInt(offset, lWillenserklaerung.identifikationKlasse);
            offset++;
            pstm.setString(offset, lWillenserklaerung.identifikationStimmkarte);
            offset++;
            pstm.setString(offset, lWillenserklaerung.identifikationStimmkarteSecond);
            offset++;
            pstm.setInt(offset, lWillenserklaerung.erteiltAufWeg);
            offset++;
            pstm.setInt(offset, lWillenserklaerung.meldungsIdent);
            offset++;

            pstm.setInt(offset, lWillenserklaerung.meldungsIdentGast);
            offset++;
            pstm.setInt(offset, lWillenserklaerung.zuVerzeichnisNr1);
            offset++;
            pstm.setInt(offset, lWillenserklaerung.zuVerzeichnisNr2);
            offset++;
            pstm.setInt(offset, lWillenserklaerung.zuVerzeichnisNr3);
            offset++;
            pstm.setInt(offset, lWillenserklaerung.zuVerzeichnisNr4);
            offset++;
            pstm.setInt(offset, lWillenserklaerung.zuVerzeichnisNr1Gedruckt);
            offset++;
            pstm.setInt(offset, lWillenserklaerung.zuVerzeichnisNr2Gedruckt);
            offset++;
            pstm.setInt(offset, lWillenserklaerung.zuVerzeichnisNr3Gedruckt);
            offset++;
            pstm.setInt(offset, lWillenserklaerung.zuVerzeichnisNr4Gedruckt);
            offset++;
            pstm.setInt(offset, lWillenserklaerung.willenserklaerung);
            offset++;
            pstm.setInt(offset, lWillenserklaerung.delayed);
            offset++;
            pstm.setInt(offset, lWillenserklaerung.pending);
            offset++;
            pstm.setString(offset, lWillenserklaerung.zutrittsIdent);
            offset++;
            pstm.setString(offset, lWillenserklaerung.zutrittsIdentNeben);
            offset++;
            pstm.setString(offset, lWillenserklaerung.zutrittsIdentErsetzt);
            offset++;
            pstm.setString(offset, lWillenserklaerung.zutrittsIdentNebenErsetzt);
            offset++;

            pstm.setString(offset, lWillenserklaerung.stimmkarte1);
            offset++;
            pstm.setString(offset, lWillenserklaerung.stimmkarte2);
            offset++;
            pstm.setString(offset, lWillenserklaerung.stimmkarte3);
            offset++;
            pstm.setString(offset, lWillenserklaerung.stimmkarte4);
            offset++;
            pstm.setString(offset, lWillenserklaerung.stimmkarteSecond);
            offset++;

            pstm.setLong(offset, lWillenserklaerung.stimmen);
            offset++;
            pstm.setLong(offset, lWillenserklaerung.aktien);
            offset++;

            pstm.setInt(offset, lWillenserklaerung.verweisart);
            offset++;
            pstm.setInt(offset, lWillenserklaerung.verweisAufWillenserklaerung);
            offset++;
            pstm.setInt(offset, lWillenserklaerung.folgeBuchungFuerIdent);
            offset++;
            pstm.setInt(offset, lWillenserklaerung.willenserklaerungGeberIdent);
            offset++;
            pstm.setInt(offset, lWillenserklaerung.bevollmaechtigterDritterIdent);
            offset++;
            pstm.setInt(offset, lWillenserklaerung.identMeldungZuSammelkarte);
            offset++;
            pstm.setInt(offset, lWillenserklaerung.identGeaenderteMeldungZuSammelkarte);
            offset++;
            pstm.setString(offset, lWillenserklaerung.veraenderungszeit);
            offset++;

            pstm.setInt(offset, lWillenserklaerung.benutzernr);
            offset++;
            pstm.setInt(offset, lWillenserklaerung.arbeitsplatz);
            offset++;

            pstm.setInt(offset, lWillenserklaerung.istKontrolliert);
            offset++;
            pstm.setInt(offset, lWillenserklaerung.protokollnr);
            offset++;

            pstm.setInt(offset, lWillenserklaerung.storniert);
            offset++;
            pstm.setInt(offset, lWillenserklaerung.storniert_delayed);
            offset++;
            pstm.setInt(offset, lWillenserklaerung.erzeugtUeber);
            offset++;
            pstm.setInt(offset, lWillenserklaerung.erzeugtUeberLauf);
            offset++;
            pstm.setInt(offset, lWillenserklaerung.exportiertUeberLauf);
            offset++;

        } catch (SQLException e) {
            CaBug.drucke("001");
            e.printStackTrace();
        }

    }

    public int insert(EclWillenserklaerung lWillenserklaerung, EclWillenserklaerungZusatz lWillenserklaerungZusatz) {

        int erg;


        /* neue InterneIdent vergeben */
        erg = dbBasis.getInterneIdentWillenserklaerung();
        if (erg < 1) {
            CaBug.drucke("DbWillenserklaerung.insert 002");
            return (erg);
        }
        lWillenserklaerung.willenserklaerungIdent = erg;

        lWillenserklaerung.mandant = dbBundle.clGlobalVar.mandant;
        lWillenserklaerung.zuVerzeichnisNr1 = dbBundle.clGlobalVar.zuVerzeichnisNr1;
        lWillenserklaerung.zuVerzeichnisNr2 = dbBundle.clGlobalVar.zuVerzeichnisNr2;
        lWillenserklaerung.zuVerzeichnisNr3 = dbBundle.clGlobalVar.zuVerzeichnisNr3;
        lWillenserklaerung.zuVerzeichnisNr4 = dbBundle.clGlobalVar.zuVerzeichnisNr4;
        if (lWillenserklaerung.veraenderungszeit.isEmpty()) {
            lWillenserklaerung.veraenderungszeit = CaDatumZeit.DatumZeitStringFuerDatenbank();
        }
        lWillenserklaerung.benutzernr = dbBundle.clGlobalVar.benutzernr;
        lWillenserklaerung.arbeitsplatz = dbBundle.clGlobalVar.arbeitsplatz;
        lWillenserklaerung.protokollnr = dbBundle.clGlobalVar.protokollnr;

        /* VclMeldung einfügen */
        /* Verarbeitungshinweise: 
         * 	>	nachdem InterneIdent immer eindeutig vergeben werden, ist prinzipiell eine "Doppeleinfügung"
         * 		von InterneIdent nicht möglich
         */

        try {

            /*Felder Neuanlage füllen*/
            String sql1 = "INSERT INTO " + dbBundle.getSchemaMandant() + "tbl_willenserklaerung " + "("
                    + "mandant, willenserklaerungIdent, db_version, "
                    + "identifikationDurch, identifikationZutrittsIdent, identifikationZutrittsIdentNeben, identifikationKlasse, identifikationStimmkarte, identifikationStimmkarteSecond, "
                    + "erteiltAufWeg, " + "meldungsIdent, meldungsIdentGast, "
                    + "zuVerzeichnisNr1, zuVerzeichnisNr2, zuVerzeichnisNr3, zuVerzeichnisNr4, "
                    + "zuVerzeichnisNr1Gedruckt, zuVerzeichnisNr2Gedruckt, zuVerzeichnisNr3Gedruckt, zuVerzeichnisNr4Gedruckt, "
                    + "willenserklaerung, mc_delayed, pending, "
                    + "zutrittsIdent, zutrittsIdentNeben, zutrittsIdentErsetzt, zutrittsIdentNebenErsetzt, "
                    + "stimmkarte1, stimmkarte2, stimmkarte3, stimmkarte4, stimmkarteSecond, " + "stimmen, aktien, "
                    + "verweisart, verweisAufWillenserklaerung, folgeBuchungFuerIdent,"
                    + "willenserklaerungGeberIdent, bevollmaechtigterDritterIdent, identMeldungZuSammelkarte, identGeaenderteMeldungZuSammelkarte, "
                    + "veraenderungszeit, "
                    + "benutzernr, arbeitsplatz, istKontrolliert, protokollnr, storniert, storniert_delayed, erzeugtUeber, erzeugtUeberLauf, exportiertUeberLauf "
                    + ")" +

                    "VALUES (" + "?, ?, ?, " + "?, ?, ?, ?, ?, ?, " + "?, " + "?, ?, " + "?, ?, ?, ?, " + "?, ?, ?, ?, "
                    + "?, ?,?, " + "?,?, ?, ?, " + "?,?, ?, ?, ?,  " + "?, ?, " + "?, ?, ?, " + "?,?, ?, ?, " + "?, "
                    + "?,?,?,?,?,?,?,?,?)";
            PreparedStatement pstm1 = verbindung.prepareStatement(sql1, ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);

            fuellePreparedStatementKomplett(pstm1, 1, lWillenserklaerung);

            erg = 0; /*Falls Duplicate Key, dann wird exception geschmissen und erg wohl nicht gefüllt!*/
            //			System.out.println("vor Update");
            dbBasis.beginTransactionNeu();
            erg = pstm1.executeUpdate();
            dbBasis.endTransactionNeu();
            //			System.out.println("nach Update");
            pstm1.close();
        } catch (Exception e2) {
            dbBasis.endTransactionNeu();
            CaBug.drucke("DbWillenserklaerung.insert 001");
            System.err.println(" " + e2.getMessage());
        }

        lWillenserklaerungZusatz.willenserklaerungIdent = lWillenserklaerung.willenserklaerungIdent;
        lWillenserklaerungZusatz.meldungsIdent = lWillenserklaerung.meldungsIdent;
        lWillenserklaerungZusatz.meldungsIdentGast = lWillenserklaerung.meldungsIdentGast;
        lWillenserklaerungZusatz.willenserklaerung = lWillenserklaerung.willenserklaerung;

        dbBundle.dbWillenserklaerungZusatz.insert(lWillenserklaerungZusatz);

        if (erg == 0) {/*Fehler beim Einfügen - d.h. ZutrittsIdent bereits vorhanden*/
            return (-1);
        }

 
        return (1);
    }

    public int leseZuIdent(int ident) {
        int anzInArray = 0;
        try {

            String sql = "SELECT w.* from " + dbBundle.getSchemaMandant() + "tbl_willenserklaerung w where "
                    + "w.mandant=? AND " + "w.willenserklaerungIdent=? ORDER BY w.willenserklaerungIdent;";

            PreparedStatement pstm1 = verbindung.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
            pstm1.setInt(1, dbBundle.clGlobalVar.mandant);
            pstm1.setInt(2, ident);

            ResultSet ergebnis = pstm1.executeQuery();
            ergebnis.last();
            anzInArray = ergebnis.getRow();
            ergebnis.beforeFirst();

            willenserklaerungArray = new EclWillenserklaerung[anzInArray];

            int i = 0;
            while (ergebnis.next() == true) {

                willenserklaerungArray[i] = this.decodeErgebnis(ergebnis);
                i++;

            }
            ergebnis.close();
            pstm1.close();

        } catch (Exception e) {
            CaBug.drucke("DbWillenserklaerung.leseZuIdent 001");
            System.err.println(" " + e.getMessage());
        }
        return (anzInArray);
    }

    public int leseZuMeldungsIdent(int pMeldungsIdent) {
        EclMeldung lMeldung = new EclMeldung();
        lMeldung.meldungsIdent = pMeldungsIdent;
        return leseZuMeldung(lMeldung);
    }

    public int leseZuMeldung(EclMeldung meldung) {
        int anzInArray = 0;
        try {

            String sql = "SELECT w.* from " + dbBundle.getSchemaMandant() + "tbl_willenserklaerung w where "
                    + "w.mandant=? AND " + "w.meldungsIdent=? ORDER BY w.willenserklaerungIdent;";

            PreparedStatement pstm1 = verbindung.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
            pstm1.setInt(1, dbBundle.clGlobalVar.mandant);
            pstm1.setInt(2, meldung.meldungsIdent);

            ResultSet ergebnis = pstm1.executeQuery();
            ergebnis.last();
            anzInArray = ergebnis.getRow();
            ergebnis.beforeFirst();

            willenserklaerungArray = new EclWillenserklaerung[anzInArray];

            int i = 0;
            while (ergebnis.next() == true) {

                willenserklaerungArray[i] = this.decodeErgebnis(ergebnis);
                i++;

            }
            ergebnis.close();
            pstm1.close();

        } catch (Exception e) {
            CaBug.drucke("DbWillenserklaerung.leseZuMeldung 001");
            System.err.println(" " + e.getMessage());
        }
        return (anzInArray);
    }

    /**Liefert die höchste vergebene willenserklärungsIdent für die übergebene Meldung ab. Dient zur Überprüfung, ob seit
     * dem letzten Transaktionsschritt neue Willenserklärungen dazugekommen sind
     */
    public int ermittleHoechsteWillenserklaerungIdentZuMeldung(EclMeldung meldung) {
        int hoechsteWillenserklaerungIdent = 0;
        EclWillenserklaerung lWillenserklaerung = null;
        try {

            String sql = "SELECT w.* from " + dbBundle.getSchemaMandant() + "tbl_willenserklaerung w where "
                    + "w.mandant=? AND " + "w.meldungsIdent=? ORDER BY w.willenserklaerungIdent;";

            PreparedStatement pstm1 = verbindung.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
            pstm1.setInt(1, dbBundle.clGlobalVar.mandant);
            pstm1.setInt(2, meldung.meldungsIdent);

            ResultSet ergebnis = pstm1.executeQuery();
            ergebnis.beforeFirst();
            while (ergebnis.next() == true) {

                lWillenserklaerung = this.decodeErgebnis(ergebnis);
                if (lWillenserklaerung.willenserklaerungIdent > hoechsteWillenserklaerungIdent) {
                    hoechsteWillenserklaerungIdent = lWillenserklaerung.willenserklaerungIdent;
                }
            }
            ergebnis.close();
            pstm1.close();

        } catch (Exception e) {
            CaBug.drucke("DbWillenserklaerung.ermittleHoechsteWillenserklaerungIdentZuMeldung 001");
            System.err.println(" " + e.getMessage());
        }
        return (hoechsteWillenserklaerungIdent);
    }

    public int leseZuMeldungsIdentGast(int pMeldungsIdent) {
        EclMeldung lMeldung = new EclMeldung();
        lMeldung.meldungsIdent = pMeldungsIdent;
        return leseZuMeldungGast(lMeldung);
    }

    public int leseZuMeldungGast(EclMeldung meldung) {
        int anzInArray = 0;
        try {

            String sql = "SELECT w.* from " + dbBundle.getSchemaMandant() + "tbl_willenserklaerung w where "
                    + "w.mandant=? AND " + "w.meldungsIdentGast=? ORDER BY w.willenserklaerungIdent;";

            PreparedStatement pstm1 = verbindung.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
            pstm1.setInt(1, dbBundle.clGlobalVar.mandant);
            pstm1.setInt(2, meldung.meldungsIdent);

            ResultSet ergebnis = pstm1.executeQuery();
            ergebnis.last();
            anzInArray = ergebnis.getRow();
            ergebnis.beforeFirst();

            willenserklaerungArray = new EclWillenserklaerung[anzInArray];

            int i = 0;
            while (ergebnis.next() == true) {
                willenserklaerungArray[i] = this.decodeErgebnis(ergebnis);
                i++;

            }
            ergebnis.close();
            pstm1.close();

        } catch (Exception e) {
            CaBug.drucke("DbWillenserklaerung.leseZuMeldungGast 001");
            System.err.println(" " + e.getMessage());
        }

        return (anzInArray);

    }

    public int ermittleHoechsteWillenserklaerungIdentZuMeldungGast(EclMeldung meldung) {
        int hoechsteWillenserklaerungIdent = 0;
        EclWillenserklaerung lWillenserklaerung = null;
        try {

            String sql = "SELECT w.* from " + dbBundle.getSchemaMandant() + "tbl_willenserklaerung w where "
                    + "w.mandant=? AND " + "w.meldungsIdentGast=? ORDER BY w.willenserklaerungIdent;";

            PreparedStatement pstm1 = verbindung.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
            pstm1.setInt(1, dbBundle.clGlobalVar.mandant);
            pstm1.setInt(2, meldung.meldungsIdent);

            ResultSet ergebnis = pstm1.executeQuery();
            ergebnis.beforeFirst();

            while (ergebnis.next() == true) {
                lWillenserklaerung = this.decodeErgebnis(ergebnis);
                if (lWillenserklaerung.willenserklaerungIdent > hoechsteWillenserklaerungIdent) {
                    hoechsteWillenserklaerungIdent = lWillenserklaerung.willenserklaerungIdent;
                }
            }
            ergebnis.close();
            pstm1.close();

        } catch (Exception e) {
            CaBug.drucke("DbWillenserklaerung.ermittleHoechsteWillenserklaerungIdentZuMeldungGast 001");
            System.err.println(" " + e.getMessage());
        }

        return (hoechsteWillenserklaerungIdent);

    }

    public int leseZuEk(EclZutrittsIdent pEK) {
        int anzInArray = 0;
        try {

            String sql = "SELECT w.* from " + dbBundle.getSchemaMandant() + "tbl_willenserklaerung w where "
                    + "w.mandant=? AND "
                    + "w.zutrittsIdent=? AND w.zutrittsIdentNeben=? AND w.willenserklaerung=21000 ORDER BY w.willenserklaerungIdent;";

            PreparedStatement pstm1 = verbindung.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
            pstm1.setInt(1, dbBundle.clGlobalVar.mandant);
            pstm1.setString(2, pEK.zutrittsIdent);
            pstm1.setString(3, pEK.zutrittsIdentNeben);

            ResultSet ergebnis = pstm1.executeQuery();
            ergebnis.last();
            anzInArray = ergebnis.getRow();
            ergebnis.beforeFirst();

            willenserklaerungArray = new EclWillenserklaerung[anzInArray];

            int i = 0;
            while (ergebnis.next() == true) {
                willenserklaerungArray[i] = this.decodeErgebnis(ergebnis);
                i++;

            }
            ergebnis.close();
            pstm1.close();

        } catch (Exception e) {
            CaBug.drucke("DbWillenserklaerung.leseZuBevollmaechtigten 001");
            System.err.println(" " + e.getMessage());
        }

        return (anzInArray);

    }

    public int lesePraesenzZuIdent(int meldeIdent) {
        int anzInArray = 0;
        try {

            String sql = "SELECT w.* from " + dbBundle.getSchemaMandant() + "tbl_willenserklaerung w where "
                    + "w.mandant=? AND " + "w.meldungsIdent=? AND " + "(" + "w.willenserklaerung="
                    + KonstWillenserklaerung.erstzugang + " OR " + "w.willenserklaerung="
                    + KonstWillenserklaerung.abgang + " OR " + "w.willenserklaerung="
                    + KonstWillenserklaerung.wiederzugang + " OR " + "w.willenserklaerung="
                    + KonstWillenserklaerung.vertreterwechsel + " OR " + "w.willenserklaerung="
                    + KonstWillenserklaerung.wechselInSRV + " OR " + "w.willenserklaerung="
                    + KonstWillenserklaerung.wechselInOrga + " OR " + "w.willenserklaerung="
                    + KonstWillenserklaerung.wechselInDauervollmacht + " OR " + "w.willenserklaerung="
                    + KonstWillenserklaerung.wechselInKIAV + ") " + "ORDER BY w.willenserklaerungIdent;";

            PreparedStatement pstm1 = verbindung.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
            pstm1.setInt(1, dbBundle.clGlobalVar.mandant);
            pstm1.setInt(2, meldeIdent);

            ResultSet ergebnis = pstm1.executeQuery();
            ergebnis.last();
            anzInArray = ergebnis.getRow();
            ergebnis.beforeFirst();

            willenserklaerungArray = new EclWillenserklaerung[anzInArray];

            int i = 0;
            while (ergebnis.next() == true) {
                willenserklaerungArray[i] = this.decodeErgebnis(ergebnis);
                i++;

            }
            ergebnis.close();
            pstm1.close();

        } catch (Exception e) {
            CaBug.drucke("DbWillenserklaerung.leseZuBevollmaechtigten 001");
            System.err.println(" " + e.getMessage());
        }

        return (anzInArray);

    }

    public int leseNichtGepruefte() {
        int anzInArray = 0;
        try {

            String sql = "SELECT w.* from " + dbBundle.getSchemaMandant() + "tbl_willenserklaerung w " + "INNER JOIN "
                    + dbBundle.getSchemaMandant() + "tbl_meldungen m ON w.meldungsIdent= m.meldungsIdent " + "WHERE "
                    + "w.mandant=? AND "
                    + "w.willenserklaerung=21000 AND w.istKontrolliert=0 AND m.meldungstyp!=3 AND m.mandant=? ORDER BY w.willenserklaerungIdent;";

            PreparedStatement pstm1 = verbindung.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
            pstm1.setInt(1, dbBundle.clGlobalVar.mandant);
            pstm1.setInt(2, dbBundle.clGlobalVar.mandant);

            ResultSet ergebnis = pstm1.executeQuery();
            ergebnis.last();
            anzInArray = ergebnis.getRow();
            ergebnis.beforeFirst();

            willenserklaerungArray = new EclWillenserklaerung[anzInArray];

            int i = 0;
            while (ergebnis.next() == true) {
                willenserklaerungArray[i] = this.decodeErgebnis(ergebnis);
                i++;

            }
            ergebnis.close();
            pstm1.close();

        } catch (Exception e) {
            CaBug.drucke("DbWillenserklaerung.leseNichtGepruefte 001");
            System.err.println(" " + e.getMessage());
        }

        return (anzInArray);

    }

    public int leseZuBevollmaechtigten(int pPersonNatJur) {
        return leseZuBevollmaechtigten(pPersonNatJur, true);
    }

    public int leseZuBevollmaechtigten(int pPersonNatJur, boolean auchStornierte) {
        int anzInArray = 0;
        try {

            String sql = "SELECT w.* from " + dbBundle.getSchemaMandant() + "tbl_willenserklaerung w where "
                    + "w.bevollmaechtigterDritterIdent=? and w.willenserklaerung=300 ";
            if (auchStornierte == false) {
                sql = sql + " AND w.storniert=0 ";
            }
            sql = sql + "ORDER BY w.willenserklaerungIdent;";

            PreparedStatement pstm1 = verbindung.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
            pstm1.setInt(1, pPersonNatJur);

            ResultSet ergebnis = pstm1.executeQuery();
            ergebnis.last();
            anzInArray = ergebnis.getRow();
            ergebnis.beforeFirst();

            willenserklaerungArray = new EclWillenserklaerung[anzInArray];

            int i = 0;
            while (ergebnis.next() == true) {
                willenserklaerungArray[i] = this.decodeErgebnis(ergebnis);
                i++;

            }
            ergebnis.close();
            pstm1.close();

        } catch (Exception e) {
            CaBug.drucke("DbWillenserklaerung.leseZuBevollmaechtigten 001");
            System.err.println(" " + e.getMessage());
        }

        return (anzInArray);

    }

    private int lesePreparedStatement(PreparedStatement pstm1) {
        int anzInArray = 0;
        try {
            ResultSet ergebnis = pstm1.executeQuery();
            ergebnis.last();
            anzInArray = ergebnis.getRow();
            ergebnis.beforeFirst();

            willenserklaerungArray = new EclWillenserklaerung[anzInArray];

            int i = 0;
            while (ergebnis.next() == true) {
                willenserklaerungArray[i] = this.decodeErgebnis(ergebnis);
                i++;

            }
            ergebnis.close();
            pstm1.close();

        } catch (Exception e) {
            CaBug.drucke("DbWillenserklaerung.leseAbZeitstempel 001");
            System.err.println(" " + e.getMessage());
        }
        return (anzInArray);

    }

    public int leseAbZeitstempel(String pZeitstempel) {
        return leseAbBisZeitstempel(pZeitstempel, "9999.99.99 99:99:99");
    }

    public int leseAbBisZeitstempel(String pAbZeitstempel, String pBisZeitstempel) {
        int anzInArray = 0;
        try {
            String sql = "SELECT w.* from " + dbBundle.getSchemaMandant() + "tbl_willenserklaerung w where "
                    + "w.mandant=? AND "
                    + "w.veraenderungszeit>=? AND w.veraenderungszeit<=? ORDER BY w.willenserklaerungIdent;";
            PreparedStatement pstm1 = verbindung.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
            pstm1.setInt(1, dbBundle.clGlobalVar.mandant);
            pstm1.setString(2, pAbZeitstempel);
            pstm1.setString(3, pBisZeitstempel);
            anzInArray = lesePreparedStatement(pstm1);
        } catch (Exception e) {
            CaBug.drucke("DbWillenserklaerung.leseAbZeitstempel 001");
            System.err.println(" " + e.getMessage());
        }

        return (anzInArray);
    }

    public int leseZugaengeWechsel() {
        int anzInArray = 0;
        try {
            String sql = "SELECT w.* from " + dbBundle.getSchemaMandant() + "tbl_willenserklaerung w where "
                    + "w.mandant=? AND " + "w.willenserklaerung=21000 OR w.willenserklaerung=21009;";
            PreparedStatement pstm1 = verbindung.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
            pstm1.setInt(1, dbBundle.clGlobalVar.mandant);
            anzInArray = lesePreparedStatement(pstm1);
        } catch (Exception e) {
            CaBug.drucke("DbWillenserklaerung.leseAbZeitstempel 001");
            System.err.println(" " + e.getMessage());
        }

        return (anzInArray);

    }

    public long ermittleSummeVirtuellTeilnehmer(int listenNummer) {
        long summe = 0;
        try {
            String sql = "SELECT COUNT(DISTINCT w.meldungsIdent, w.willenserklaerungGeberIdent) from " + dbBundle.getSchemaMandant() + "tbl_willenserklaerung w where "
                    + "w.mandant=? AND (w.willenserklaerung=23001 OR w.willenserklaerung=23003) AND w.zuVerzeichnisNr1Gedruckt<=? AND w.zuVerzeichnisNr1Gedruckt!=0;";
            PreparedStatement pstm1 = verbindung.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
            pstm1.setInt(1, dbBundle.clGlobalVar.mandant);
            pstm1.setInt(2, listenNummer);
             ResultSet ergebnis = pstm1.executeQuery();
            if (ergebnis.next())
                summe = ergebnis.getLong(1);

      } catch (Exception e) {
            CaBug.drucke("001");
            System.err.println(" " + e.getMessage());
        }

        return (summe);

    }

    public long ermittleSummeVirtuellAktien(int listenNummer) {
        long summe = 0;
       try {
            /*TODO VidKonf  VirtuellSpäter: wenn sich zwischendurch die Aktienzahl ändert, dann paßt das Teilnehmerverzeichnis aktuell nicht mehr ...*/
           /*TODO VidKonf  zuVerzeichnisNr1 noch parametrisieren*/
            String sql = "SELECT DISTINCT w.meldungsIdent, w.stimmen from " + dbBundle.getSchemaMandant() + "tbl_willenserklaerung w where "
                    + "w.mandant=? AND (w.willenserklaerung=23001 OR w.willenserklaerung=23003) AND w.zuVerzeichnisNr1Gedruckt<=? AND w.zuVerzeichnisNr1Gedruckt!=0;";
            PreparedStatement pstm1 = verbindung.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
            pstm1.setInt(1, dbBundle.clGlobalVar.mandant);
            pstm1.setInt(2, listenNummer);
            ResultSet ergebnis = pstm1.executeQuery();
            ergebnis.beforeFirst();

            while (ergebnis.next() == true) {
                summe+=ergebnis.getLong(2);
            }
  
      } catch (Exception e) {
            CaBug.drucke("001");
            System.err.println(" " + e.getMessage());
        }

        return (summe);

    }

    /**Update einer Willenserklärung. Versionsnummer wird um 1 hochgezählt
     */
    public int update(EclWillenserklaerung lWillenserklaerung) {

        lWillenserklaerung.db_version++;
        try {

            String sql = "UPDATE " + dbBundle.getSchemaMandant() + "tbl_willenserklaerung SET "
                    + "mandant=?, willenserklaerungIdent=?, db_version=?, "
                    + "identifikationDurch=?, identifikationZutrittsIdent=?, identifikationZutrittsIdentNeben=?, identifikationKlasse=?, identifikationStimmkarte=?, identifikationStimmkarteSecond=?, "
                    + "erteiltAufWeg=?, " + "meldungsIdent=?, meldungsIdentGast=?, "
                    + "zuVerzeichnisNr1=?, zuVerzeichnisNr2=?, zuVerzeichnisNr3=?, zuVerzeichnisNr4=?, "
                    + "zuVerzeichnisNr1Gedruckt=?, zuVerzeichnisNr2Gedruckt=?, zuVerzeichnisNr3Gedruckt=?, zuVerzeichnisNr4Gedruckt=?, "
                    + "willenserklaerung=?, mc_delayed=?, pending=?, "
                    + "zutrittsIdent=?, zutrittsIdentNeben=?, zutrittsIdentErsetzt=?, zutrittsIdentNebenErsetzt=?, "
                    + "stimmkarte1=?, stimmkarte2=?, stimmkarte3=?, stimmkarte4=?, stimmkarteSecond=?, "
                    + "stimmen=?, aktien=?, " + "verweisart=?, verweisAufWillenserklaerung=?, folgeBuchungFuerIdent=?, "
                    + "willenserklaerungGeberIdent=?, bevollmaechtigterDritterIdent=?, identMeldungZuSammelkarte=?, identGeaenderteMeldungZuSammelkarte=?, "
                    + "veraenderungszeit=?, "
                    + "benutzernr=?, arbeitsplatz=?, istKontrolliert=?, protokollnr=?, storniert=?,  storniert_delayed=?, erzeugtUeber=?, erzeugtUeberLauf=?, exportiertUeberLauf=? "
                    + "WHERE " + "willenserklaerungIdent=? AND " + "db_version=? AND mandant=?";

            PreparedStatement pstm1 = verbindung.prepareStatement(sql);
            fuellePreparedStatementKomplett(pstm1, 1, lWillenserklaerung);
            CaBug.druckeLog("lWillenserklaerung.willenserklaerungIdent="+lWillenserklaerung.willenserklaerungIdent, logDrucken, 10);
            pstm1.setInt(anzfelder + 1, lWillenserklaerung.willenserklaerungIdent);
            CaBug.druckeLog("lWillenserklaerung.db_version="+lWillenserklaerung.db_version, logDrucken, 10);
            pstm1.setLong(anzfelder + 2, lWillenserklaerung.db_version - 1);
            CaBug.druckeLog("lWillenserklaerung.mandant="+lWillenserklaerung.mandant, logDrucken, 10);
            pstm1.setLong(anzfelder + 3, dbBundle.clGlobalVar.mandant);

            //		System.out.println("DbWillenserklaerung.lWillenserklaerung.willenserklaerungIdent="+lWillenserklaerung.willenserklaerungIdent);
            //		System.out.println("DbWillenserklaerung.lWillenserklaerung.db_version-1="+Long.toString(lWillenserklaerung.db_version-1));
            //		System.out.println("DbWillenserklaerung.dbBundle.globalVar.mandant="+dbBundle.globalVar.mandant);

            int ergebnis1 = pstm1.executeUpdate();
            pstm1.close();
            if (ergebnis1 == 0) {
                CaBug.drucke("DbWillenserklaerung.update 002");
                return (CaFehler.pfXyWurdeVonAnderemBenutzerVeraendert);
            }
        } catch (Exception e1) {
            CaBug.drucke("DbWillenserklaerung.update 001");
            System.err.println(" " + e1.getMessage());
            return (CaFehler.pfdXyBereitsVorhanden);

        }

        return (1);
    }

    /**Update einer Willenserklärung. Versionsnummer wird um 1 hochgezählt
     */
    public int update_setzeStorno(int pWillenserklaerungIdent, int pStorniert, int pStorniert_delayed) {

        try {

            String sql = "UPDATE " + dbBundle.getSchemaMandant() + "tbl_willenserklaerung SET "
                    + "storniert=?,  storniert_delayed=? " + "WHERE " + "willenserklaerungIdent=? ";

            PreparedStatement pstm1 = verbindung.prepareStatement(sql);
            pstm1.setInt(1, pStorniert);
            pstm1.setInt(2, pStorniert_delayed);
            pstm1.setInt(3, pWillenserklaerungIdent);

            //		System.out.println("DbWillenserklaerung.lWillenserklaerung.willenserklaerungIdent="+lWillenserklaerung.willenserklaerungIdent);
            //		System.out.println("DbWillenserklaerung.lWillenserklaerung.db_version-1="+Long.toString(lWillenserklaerung.db_version-1));
            //		System.out.println("DbWillenserklaerung.dbBundle.globalVar.mandant="+dbBundle.globalVar.mandant);

            int ergebnis1 = pstm1.executeUpdate();
            pstm1.close();
            if (ergebnis1 == 0) {
                CaBug.drucke("002");
                return (CaFehler.pfXyWurdeVonAnderemBenutzerVeraendert);
            }
        } catch (Exception e1) {
            CaBug.drucke("001");
            System.err.println(" " + e1.getMessage());
            return (CaFehler.pfdXyBereitsVorhanden);

        }

        return (1);
    }

    /**Update einer Willenserklärung. Versionsnummer wird um 1 hochgezählt
     */
    public int delete(EclWillenserklaerung lWillenserklaerung) {

        try {

            String sql = "DELETE FROM " + dbBundle.getSchemaMandant()
                    + "tbl_willenserklaerung WHERE willenserklaerungIdent=? AND db_version=? AND mandant=? ";

            PreparedStatement pstm1 = verbindung.prepareStatement(sql);
            pstm1.setInt(1, lWillenserklaerung.willenserklaerungIdent);
            pstm1.setLong(2, lWillenserklaerung.db_version);
            pstm1.setLong(3, dbBundle.clGlobalVar.mandant);

            int ergebnis1 = pstm1.executeUpdate();
            pstm1.close();
            if (ergebnis1 == 0) {
                return (CaFehler.pfXyWurdeVonAnderemBenutzerVeraendert);
            }
        } catch (Exception e1) {
            CaBug.drucke("DbWillenserklaerung.delete 001");
            System.err.println(" " + e1.getMessage());
            return (CaFehler.pfdXyBereitsVorhanden);

        }

        return (1);
    }

    /**Update einer Willenserklärung. Versionsnummer wird um 1 hochgezählt
     */
    public int updateMitZusatz(EclWillenserklaerung lWillenserklaerung,
            EclWillenserklaerungZusatz lWillenserklaerungZusatz) {

        int rc = this.update(lWillenserklaerung);
        if (rc == CaFehler.pfXyWurdeVonAnderemBenutzerVeraendert) {
            return (CaFehler.pfXyWurdeVonAnderemBenutzerVeraendert);
        }

        dbBundle.dbWillenserklaerungZusatz.update(lWillenserklaerungZusatz);
        return (1);
    }

    /****************************************************************************************************************************************/
    /**************************************Methoden zum Auflösen der Delayed-Willenserklärungen**********************************************/
    /****************************************************************************************************************************************/
    /**Verarbeitungshinweis:
     * Die Delayed-Willenserklärungen werden der Reihe nach abgearbeitet, wie sie erteilt wurden.
     * Es wird dabei nicht nach meldungsIdent sortiert, denn: es sind ja Willesnerklärungen für zwei meldungsident (Gast und Aktionär)
     * möglich.
     */

    /*TODO _Willenserklaerung: ab hier CLose noch nicht eingebaut*/
    /**Hier wird die jeweils nächste eingelesen und zu verarbeitende delayed Willenserklärung von diesen Methoden abgelegt*/
    public EclWillenserklaerung rcDelayedWillenserklaerung = null;

    /**Anzahl der zu verarbeitenden Delayed-Willenserklärungen - ist nach aufloesenDelayed_init gefüllt*/
    public int rcDelayedAnzahl = 0;

    private ResultSet delayedErgebnis = null;

    /**Initialisierung - setzt Select ab*/
    public int initAufloesenDelayed() {
        try {

            String sql = "SELECT w.* from " + dbBundle.getSchemaMandant() + "tbl_willenserklaerung w where "
                    + "w.mandant=? AND w.mc_delayed=1 " + "ORDER BY w.willenserklaerungIdent;";

            PreparedStatement pstm1 = verbindung.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
            pstm1.setInt(1, dbBundle.clGlobalVar.mandant);

            delayedErgebnis = pstm1.executeQuery();
            delayedErgebnis.last();
            rcDelayedAnzahl = delayedErgebnis.getRow();
            delayedErgebnis.beforeFirst();

        } catch (Exception e) {
            CaBug.drucke("DbWillenserklaerung.initAufloesenDelayed 001");
            System.err.println(" " + e.getMessage());
        }

        return (rcDelayedAnzahl);
    }

    /**Liefere nächste Willenserklärung in rcDelayedWeillenserklaerung ab
     * liefert 0, wenn Dateiende erreicht*/
    public int readNextDelayed() {
        try {
            if (delayedErgebnis.next() == false) {
                return (0);
            }
            rcDelayedWillenserklaerung = this.decodeErgebnis(delayedErgebnis);
        } catch (Exception e) {
            CaBug.drucke("DbWillenserklaerung.readNextDelayed 001");
            System.err.println(" " + e.getMessage());
        }
        return 1;
    }
}
