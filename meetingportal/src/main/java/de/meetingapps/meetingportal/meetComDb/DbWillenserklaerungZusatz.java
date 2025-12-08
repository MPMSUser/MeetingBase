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
import de.meetingapps.meetingportal.meetComEntities.EclMeldung;
import de.meetingapps.meetingportal.meetComEntities.EclWillenserklaerungZusatz;
import de.meetingapps.meetingportal.meetComKonst.KonstWillenserklaerung;

public class DbWillenserklaerungZusatz {

    private Connection verbindung = null;
    private DbBasis dbBasis = null;
    private DbBundle dbBundle = null;

    /**Soll nicht direkt verwendet werden, sondern nur über die Zugriffsfunktionen!
     * Aktuell noch public, da direkter Zugriff über DbMeldungen
     */
    public EclWillenserklaerungZusatz willenserklaerungArray[] = null;

    /*************************Initialisierung***************************/
    /* Verbindung in lokale Daten eintragen*/
    public DbWillenserklaerungZusatz(DbBundle datenbankbundle) {
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

    public EclWillenserklaerungZusatz willenserklaerungGefunden(int lfd) {
        return willenserklaerungArray[lfd];
    }

    public int createTable() {
        int rc = 0;
        DbLowLevel lDbLowLevel = new DbLowLevel(dbBundle);
        rc = lDbLowLevel.createTable("CREATE TABLE " + dbBundle.getSchemaMandant() + "tbl_willenserklaerungzusatz ( "
                + "`mandant` int(11) NOT NULL, " + "`willenserklaerungIdent` int(11) NOT NULL, "
                + "`db_version` bigint(20) DEFAULT NULL, " + "`meldungsIdent` int(11) DEFAULT NULL, "
                + "`meldungsIdentGast` int(11) DEFAULT NULL, " + "`willenserklaerung` int(11) DEFAULT NULL, "
                + "`aktienregisterIdent` int(11) DEFAULT NULL, " + "`aktienAnmelden` bigint(20) DEFAULT NULL, "
                + "`anmeldungFix` int(11) DEFAULT NULL, " + "`anzahlAnmeldungen` int(11) DEFAULT NULL, "
                + "`anmeldungIstStorniert` int(11) DEFAULT NULL, " + "`versandartEK` int(11) DEFAULT NULL, "
                + "`versandadresse1` char(80) DEFAULT NULL, " + "`versandadresse2` char(80) DEFAULT NULL, "
                + "`versandadresse3` char(80) DEFAULT NULL, " + "`versandadresse4` char(80) DEFAULT NULL, "
                + "`versandadresse5` char(80) DEFAULT NULL, " + "`emailAdresseEK` char(80) DEFAULT NULL, "
                + "`identVertreterPersonNatJur` int(11) DEFAULT NULL, "
                + "`versandadresseUeberprueft` int(11) DEFAULT NULL, "
                + "`versandartEKUeberprueft` int(11) DEFAULT NULL, "
                + "`versandadresse1Ueberprueft` char(80) DEFAULT NULL, "
                + "`versandadresse2Ueberprueft` char(80) DEFAULT NULL, "
                + "`versandadresse3Ueberprueft` char(80) DEFAULT NULL, "
                + "`versandadresse4Ueberprueft` char(80) DEFAULT NULL, "
                + "`versandadresse5Ueberprueft` char(80) DEFAULT NULL, "
                + "`eintrittskarteWurdeGedruckt` int(11) DEFAULT NULL, " + "`erstesDruckDatum` char(19) DEFAULT NULL, "
                + "`letztesDruckDatum` char(19) DEFAULT NULL, " + "`grundNr` int(11) DEFAULT NULL, "
                + "`grundText` char(200) DEFAULT NULL, " + "`quelle` char(100) DEFAULT NULL, "
                + "PRIMARY KEY (`willenserklaerungIdent`,`mandant`), " + "KEY `IDX_meldungsIdent` (`meldungsIdent`) "
                + ") ");
        return rc;
    }

    /**************************deleteAll Löschen aller Datensätze  eines Mandanten****************/
    public int deleteAll() {
        return dbBundle.dbLowLevel.deleteMandant(
                "DELETE FROM " + dbBundle.getSchemaMandant() + "tbl_willenserklaerungzusatz where mandant=?;");
    }

    /**************************setzt aktuelle Mandantennummer bei allen Datensätzen****************/
    public int updateMandant() {
        return dbBundle.dbLowLevel.rawUpdateMandant(dbBundle.getSchemaMandant() + "tbl_willenserklaerungzusatz");
    }

    /** dekodiert die aktuelle Position aus ergebnis in EclWillenserklaerung und gibt dieses zurück*/
    EclWillenserklaerungZusatz decodeErgebnis(ResultSet ergebnis) {
        EclWillenserklaerungZusatz lWillenserklaerung = new EclWillenserklaerungZusatz();

        try {

            lWillenserklaerung.mandant = ergebnis.getInt("wz.mandant");
            lWillenserklaerung.willenserklaerungIdent = ergebnis.getInt("wz.willenserklaerungIdent");
            lWillenserklaerung.db_version = ergebnis.getLong("wz.db_version");

            lWillenserklaerung.meldungsIdent = ergebnis.getInt("wz.meldungsIdent");
            lWillenserklaerung.meldungsIdentGast = ergebnis.getInt("wz.meldungsIdentGast");
            lWillenserklaerung.willenserklaerung = ergebnis.getInt("wz.willenserklaerung");

            lWillenserklaerung.aktienregisterIdent = ergebnis.getInt("wz.aktienregisterIdent");
            lWillenserklaerung.aktienAnmelden = ergebnis.getLong("wz.aktienAnmelden");
            lWillenserklaerung.anmeldungFix = ergebnis.getInt("wz.anmeldungFix");
            lWillenserklaerung.anzahlAnmeldungen = ergebnis.getInt("wz.anzahlAnmeldungen");
            lWillenserklaerung.anmeldungIstStorniert = ergebnis.getInt("wz.anmeldungIstStorniert");
            lWillenserklaerung.versandartEK = ergebnis.getInt("wz.versandartEK");
            lWillenserklaerung.versandadresse1 = ergebnis.getString("wz.versandadresse1");
            lWillenserklaerung.versandadresse2 = ergebnis.getString("wz.versandadresse2");
            lWillenserklaerung.versandadresse3 = ergebnis.getString("wz.versandadresse3");
            lWillenserklaerung.versandadresse4 = ergebnis.getString("wz.versandadresse4");
            lWillenserklaerung.versandadresse5 = ergebnis.getString("wz.versandadresse5");
            lWillenserklaerung.emailAdresseEK = ergebnis.getString("wz.emailAdresseEK");
            lWillenserklaerung.identVertreterPersonNatJur = ergebnis.getInt("wz.identVertreterPersonNatJur");

            lWillenserklaerung.versandadresseUeberprueft = ergebnis.getInt("wz.versandadresseUeberprueft");
            lWillenserklaerung.versandartEKUeberprueft = ergebnis.getInt("wz.versandartEKUeberprueft");
            lWillenserklaerung.versandadresse1Ueberprueft = ergebnis.getString("wz.versandadresse1Ueberprueft");
            lWillenserklaerung.versandadresse2Ueberprueft = ergebnis.getString("wz.versandadresse2Ueberprueft");
            lWillenserklaerung.versandadresse3Ueberprueft = ergebnis.getString("wz.versandadresse3Ueberprueft");
            lWillenserklaerung.versandadresse4Ueberprueft = ergebnis.getString("wz.versandadresse4Ueberprueft");
            lWillenserklaerung.versandadresse5Ueberprueft = ergebnis.getString("wz.versandadresse5Ueberprueft");
            lWillenserklaerung.eintrittskarteWurdeGedruckt = ergebnis.getInt("wz.eintrittskarteWurdeGedruckt");
            lWillenserklaerung.erstesDruckDatum = ergebnis.getString("wz.erstesDruckDatum");
            lWillenserklaerung.letztesDruckDatum = ergebnis.getString("wz.letztesDruckDatum");

            lWillenserklaerung.grundNr = ergebnis.getInt("wz.grundNr");
            lWillenserklaerung.grundText = ergebnis.getString("wz.grundText");

            lWillenserklaerung.quelle = ergebnis.getString("wz.quelle");

        } catch (Exception e) {
            CaBug.drucke("DbWillenserklaerungZusatz.decodeErgebnis 001");
            System.err.println(" " + e.getMessage());
        }

        return lWillenserklaerung;
    }

    /** Fuellen Prepared Statement mit allen Feldern, beginnend bei offset.
     * Kann sowohl für Insert, als auch für update verwendet werden.
     * 
     * offset= Startposition des ersten Feldes (also z.B. 1)
     */
    private int anzfelder = 32;

    private void fuellePreparedStatementKomplett(PreparedStatement pstm, int offset,
            EclWillenserklaerungZusatz lWillenserklaerung) {

        try {
            pstm.setInt(offset, lWillenserklaerung.mandant);
            offset++;
            pstm.setInt(offset, lWillenserklaerung.willenserklaerungIdent);
            offset++;
            pstm.setLong(offset, lWillenserklaerung.db_version);
            offset++;
            pstm.setInt(offset, lWillenserklaerung.meldungsIdent);
            offset++;
            pstm.setInt(offset, lWillenserklaerung.meldungsIdentGast);
            offset++;
            pstm.setInt(offset, lWillenserklaerung.willenserklaerung);
            offset++;
            pstm.setInt(offset, lWillenserklaerung.aktienregisterIdent);
            offset++;
            pstm.setLong(offset, lWillenserklaerung.aktienAnmelden);
            offset++;
            pstm.setInt(offset, lWillenserklaerung.anmeldungFix);
            offset++;
            pstm.setInt(offset, lWillenserklaerung.anzahlAnmeldungen);
            offset++;
            pstm.setInt(offset, lWillenserklaerung.anmeldungIstStorniert);
            offset++;
            pstm.setInt(offset, lWillenserklaerung.versandartEK);
            offset++;
            pstm.setString(offset, CaString.trunc(lWillenserklaerung.versandadresse1, 80));
            offset++;
            pstm.setString(offset, CaString.trunc(lWillenserklaerung.versandadresse2, 80));
            offset++;
            pstm.setString(offset, CaString.trunc(lWillenserklaerung.versandadresse3, 80));
            offset++;
            pstm.setString(offset, CaString.trunc(lWillenserklaerung.versandadresse4, 80));
            offset++;
            pstm.setString(offset, CaString.trunc(lWillenserklaerung.versandadresse5, 80));
            offset++;
            pstm.setString(offset, CaString.trunc(lWillenserklaerung.emailAdresseEK, 80));
            offset++;
            pstm.setInt(offset, lWillenserklaerung.identVertreterPersonNatJur);
            offset++;

            pstm.setInt(offset, lWillenserklaerung.versandadresseUeberprueft);
            offset++;
            pstm.setInt(offset, lWillenserklaerung.versandartEKUeberprueft);
            offset++;
            pstm.setString(offset, CaString.trunc(lWillenserklaerung.versandadresse1Ueberprueft, 80));
            offset++;
            pstm.setString(offset, CaString.trunc(lWillenserklaerung.versandadresse2Ueberprueft, 80));
            offset++;
            pstm.setString(offset, CaString.trunc(lWillenserklaerung.versandadresse3Ueberprueft, 80));
            offset++;
            pstm.setString(offset, CaString.trunc(lWillenserklaerung.versandadresse4Ueberprueft, 80));
            offset++;
            pstm.setString(offset, CaString.trunc(lWillenserklaerung.versandadresse5Ueberprueft, 80));
            offset++;
            pstm.setInt(offset, lWillenserklaerung.eintrittskarteWurdeGedruckt);
            offset++;
            pstm.setString(offset, lWillenserklaerung.erstesDruckDatum);
            offset++;
            pstm.setString(offset, lWillenserklaerung.letztesDruckDatum);
            offset++;

            pstm.setInt(offset, lWillenserklaerung.grundNr);
            offset++;
            pstm.setString(offset, lWillenserklaerung.grundText);
            offset++;
            pstm.setString(offset, CaString.trunc(lWillenserklaerung.quelle, 99));
            offset++;

        } catch (SQLException e) {
            CaBug.drucke("DbWillenserklaerungZusatz.fuellePreparedStatementKomplett 001");
            e.printStackTrace();
        }

    }

    /**Wichtig: ident wird nicht "neu vergeben", sondern aus lWillenserklaerung geholt - sie muß identisch sein
     * mit der von EclWillenserklaerung (ohne Zusatz :-) ). Aufrufer ist dafür verantwortlich!
     */
    int insert(EclWillenserklaerungZusatz lWillenserklaerung) {

        int erg = 0;


        lWillenserklaerung.mandant = dbBundle.clGlobalVar.mandant;

        /* VclMeldung einfügen */
        /* Verarbeitungshinweise: 
         * 	>	nachdem InterneIdent immer eindeutig vergeben werden, ist prinzipiell eine "Doppeleinfügung"
         * 		von InterneIdent nicht möglich
         */

        try {

            /*Felder Neuanlage füllen*/
            String sql1 = "INSERT INTO " + dbBundle.getSchemaMandant() + "tbl_willenserklaerungzusatz " + "("
                    + "mandant, willenserklaerungIdent, db_version, "
                    + "meldungsIdent, meldungsIdentGast, willenserklaerung, "
                    + "aktienregisterIdent, aktienAnmelden, anmeldungFix, anzahlAnmeldungen, anmeldungIstStorniert, versandartEK, "
                    + "versandadresse1, versandadresse2, versandadresse3, versandadresse4, versandadresse5, emailAdresseEK, "
                    + "identVertreterPersonNatJur, " + "versandadresseUeberprueft, versandartEKUeberprueft, "
                    + "versandadresse1Ueberprueft, versandadresse2Ueberprueft, versandadresse3Ueberprueft, versandadresse4Ueberprueft, versandadresse5Ueberprueft, "
                    + "eintrittskarteWurdeGedruckt, erstesDruckDatum, letztesDruckDatum, "

                    + "grundNr, grundText, quelle" + ")" +

                    "VALUES (" + "?, ?, ?, " + "?, ?, ?, " + "?, ?, ?, ?, ?, ?, " + "?, ?, ?, ?, ?, ?, " + "?, "
                    + "?, ?, " + "?, ?, ?, " + "?, ?, ?, ?, ?, " + "?, ?, ?)";
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
            CaBug.drucke("DbWillenserklaerungZusatz.insert 001");
            System.err.println(" " + e2.getMessage());
        }

        if (erg == 0) {/*Fehler beim Einfügen - d.h. ZutrittsIdent bereits vorhanden*/
            return (-1);
        }


        return (1);
    }

    public int leseZuMeldung(EclMeldung meldung) {
        int anzInArray = 0;
        try {

            String sql = "SELECT wz.* from " + dbBundle.getSchemaMandant() + "tbl_willenserklaerungzusatz wz where "
                    + "wz.mandant=? AND " + "wz.meldungsIdent=? ORDER BY wz.willenserklaerungIdent;";

            PreparedStatement pstm1 = verbindung.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
            pstm1.setInt(1, dbBundle.clGlobalVar.mandant);
            pstm1.setInt(2, meldung.meldungsIdent);

            ResultSet ergebnis = pstm1.executeQuery();
            ergebnis.last();
            anzInArray = ergebnis.getRow();
            ergebnis.beforeFirst();

            willenserklaerungArray = new EclWillenserklaerungZusatz[anzInArray];

            int i = 0;
            while (ergebnis.next() == true) {

                willenserklaerungArray[i] = this.decodeErgebnis(ergebnis);
                i++;

            }
            ergebnis.close();
            pstm1.close();

        } catch (Exception e) {
            CaBug.drucke("DbWillenserklaerungZusatz.leseZuMeldung 001");
            System.err.println(" " + e.getMessage());
        }

        return (anzInArray);
    }

    public int leseZuMeldungGast(EclMeldung meldung) {
        int anzInArray = 0;
        try {

            String sql = "SELECT wz.* from " + dbBundle.getSchemaMandant() + "tbl_willenserklaerungzusatz wz where "
                    + "wz.mandant=? AND " + "wz.meldungsIdentGast=? ORDER BY wz.willenserklaerungIdent;";

            PreparedStatement pstm1 = verbindung.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
            pstm1.setInt(1, dbBundle.clGlobalVar.mandant);
            pstm1.setInt(2, meldung.meldungsIdent);

            ResultSet ergebnis = pstm1.executeQuery();
            ergebnis.last();
            anzInArray = ergebnis.getRow();
            ergebnis.beforeFirst();

            willenserklaerungArray = new EclWillenserklaerungZusatz[anzInArray];

            int i = 0;
            while (ergebnis.next() == true) {
                willenserklaerungArray[i] = this.decodeErgebnis(ergebnis);
                i++;

            }
            ergebnis.close();
            pstm1.close();

        } catch (Exception e) {
            CaBug.drucke("DbWillenserklaerungZusatz.leseZuMeldungGast 001");
            System.err.println(" " + e.getMessage());
        }

        return (anzInArray);
    }

    /**Lese Zusatz mit der Ident (d.h. die zu einer bestimmten Willenserklärung gehörende ZusatzWillenserklärung)
     */
    public int leseZuIdent(int pIdent) {
        int anzInArray = 0;
        try {

            String sql = "SELECT wz.* from " + dbBundle.getSchemaMandant() + "tbl_willenserklaerungzusatz wz where "
                    + "wz.mandant=? AND " + "wz.willenserklaerungIdent=?;";

            PreparedStatement pstm1 = verbindung.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
            pstm1.setInt(1, dbBundle.clGlobalVar.mandant);
            pstm1.setInt(2, pIdent);

            ResultSet ergebnis = pstm1.executeQuery();
            ergebnis.last();
            anzInArray = ergebnis.getRow();
            ergebnis.beforeFirst();

            willenserklaerungArray = new EclWillenserklaerungZusatz[anzInArray];

            int i = 0;
            while (ergebnis.next() == true) {
                willenserklaerungArray[i] = this.decodeErgebnis(ergebnis);
                i++;

            }
            ergebnis.close();
            pstm1.close();

        } catch (Exception e) {
            CaBug.drucke("DbWillenserklaerungZusatz.leseZuAktienregisterIdent 001");
            System.err.println(" " + e.getMessage());
        }

        return (anzInArray);
    }

    /**Lese alle Zusätze für Anmeldungs-Willenserklärungen, die einer pAktienregisterIdent zugeordnet sind. Diese entstehen beim Anmeldeprozess aus dem 
     * Aktienregister (nur Namensaktien). Damit können z.B. alle Anmeldungen ermittelt werden (einschließlich Gäste!), die zu
     * einem Aktienregistereintrag genieriert wurden (z.B. im Portal). Hier werden auch die stornierten Anmeldungen mit eingelesen
     */
    public int leseZuAktienregisterIdentMitStorno(int pAktienregisterIdent) {
        int anzInArray = 0;
        try {

            String sql = "SELECT wz.* from " + dbBundle.getSchemaMandant() + "tbl_willenserklaerungzusatz wz where "
                    + "wz.mandant=? AND "
                    + "wz.aktienregisterIdent=? AND (wz.willenserklaerung=? OR wz.willenserklaerung=?) ORDER BY wz.willenserklaerungIdent;";

            PreparedStatement pstm1 = verbindung.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
            pstm1.setInt(1, dbBundle.clGlobalVar.mandant);
            pstm1.setInt(2, pAktienregisterIdent);
            pstm1.setInt(3, KonstWillenserklaerung.anmeldungAusAktienregister);
            //			pstm1.setInt(3, EnWillenserklaerung.toClWillenserklaerung(EnWillenserklaerung.AnmeldungAusAktienregister));
            pstm1.setInt(4, KonstWillenserklaerung.anmeldungGast);
            //			pstm1.setInt(4, EnWillenserklaerung.toClWillenserklaerung(EnWillenserklaerung.AnmeldungGast));

            ResultSet ergebnis = pstm1.executeQuery();
            ergebnis.last();
            anzInArray = ergebnis.getRow();
            ergebnis.beforeFirst();

            willenserklaerungArray = new EclWillenserklaerungZusatz[anzInArray];

            int i = 0;
            while (ergebnis.next() == true) {
                willenserklaerungArray[i] = this.decodeErgebnis(ergebnis);
                i++;

            }
            ergebnis.close();
            pstm1.close();

        } catch (Exception e) {
            CaBug.drucke("DbWillenserklaerungZusatz.leseZuAktienregisterIdent 001");
            System.err.println(" " + e.getMessage());
        }

        return (anzInArray);
    }

    /**Lese alle Zusätze für Anmeldungs-Willenserklärungen, die einer pAktienregisterIdent zugeordnet sind (OHNE stornierte Sätze!). Diese entstehen beim Anmeldeprozess aus dem 
     * Aktienregister (nur Namensaktien). Damit können z.B. alle Anmeldungen ermittelt werden (einschließlich Gäste!), die zu
     * einem Aktienregistereintrag genieriert wurden (z.B. im Portal).
     */
    public int leseZuAktienregisterIdentOhneStorno(int pAktienregisterIdent) {
        int anzInArray = 0;
        try {

            String sql = "SELECT wz.* from " + dbBundle.getSchemaMandant() + "tbl_willenserklaerungzusatz wz where "
                    + "wz.mandant=? AND "
                    + "wz.aktienregisterIdent=? and wz.anmeldungIstStorniert=0 AND (wz.willenserklaerung=? OR wz.willenserklaerung=?) ORDER BY wz.willenserklaerungIdent;";

            PreparedStatement pstm1 = verbindung.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
            pstm1.setInt(1, dbBundle.clGlobalVar.mandant);
            pstm1.setInt(2, pAktienregisterIdent);
            pstm1.setInt(3, KonstWillenserklaerung.anmeldungAusAktienregister);
            //			pstm1.setInt(3, EnWillenserklaerung.toClWillenserklaerung(EnWillenserklaerung.AnmeldungAusAktienregister));
            pstm1.setInt(4, KonstWillenserklaerung.anmeldungGast);
            //			pstm1.setInt(4, EnWillenserklaerung.toClWillenserklaerung(EnWillenserklaerung.AnmeldungGast));

            ResultSet ergebnis = pstm1.executeQuery();
            ergebnis.last();
            anzInArray = ergebnis.getRow();
            ergebnis.beforeFirst();

            willenserklaerungArray = new EclWillenserklaerungZusatz[anzInArray];

            int i = 0;
            while (ergebnis.next() == true) {
                willenserklaerungArray[i] = this.decodeErgebnis(ergebnis);
                i++;

            }
            ergebnis.close();
            pstm1.close();

        } catch (Exception e) {
            CaBug.drucke("DbWillenserklaerungZusatz.leseZuAktienregisterIdent 001");
            System.err.println(" " + e.getMessage());
        }

        return (anzInArray);
    }

    /**Update einer Willenserklärung. Versionsnummer wird um 1 hochgezählt
     */
    int update(EclWillenserklaerungZusatz lWillenserklaerung) {
        lWillenserklaerung.db_version++;
        try {

            String sql = "UPDATE " + dbBundle.getSchemaMandant() + "tbl_willenserklaerungzusatz SET "
                    + "mandant=?, willenserklaerungIdent=?, db_version=?, "
                    + "meldungsIdent=?, meldungsIdentGast=?, willenserklaerung=?, "
                    + "aktienregisterIdent=?, aktienAnmelden=?, anmeldungFix=?, anzahlAnmeldungen=?, anmeldungIstStorniert=?, versandartEK=?, "
                    + "versandadresse1=?, versandadresse2=?, versandadresse3=?, versandadresse4=?, versandadresse5=?, "
                    + "emailAdresseEK=?, identVertreterPersonNatJur=?, "
                    + "versandadresseUeberprueft=?,  versandartEKUeberprueft=?, "
                    + "versandadresse1Ueberprueft=?, versandadresse2Ueberprueft=?, versandadresse3Ueberprueft=?, versandadresse4Ueberprueft=?, versandadresse5Ueberprueft=?, "
                    + "eintrittskarteWurdeGedruckt=?, erstesDruckDatum=?,  letztesDruckDatum=?, "
                    + "grundNr=?,  grundText=?, quelle=? " + "WHERE " + "willenserklaerungIdent=? AND "
                    + "db_version=? AND mandant=?";

            PreparedStatement pstm1 = verbindung.prepareStatement(sql);
            fuellePreparedStatementKomplett(pstm1, 1, lWillenserklaerung);
            pstm1.setInt(anzfelder + 1, lWillenserklaerung.willenserklaerungIdent);
            pstm1.setLong(anzfelder + 2, lWillenserklaerung.db_version - 1);
            pstm1.setLong(anzfelder + 3, dbBundle.clGlobalVar.mandant);

            int ergebnis1 = pstm1.executeUpdate();
            pstm1.close();
            if (ergebnis1 == 0) {
                return (CaFehler.pfXyWurdeVonAnderemBenutzerVeraendert);
            }
        } catch (Exception e1) {
            CaBug.drucke("DbWillenserklaerungZusatz.update 001");
            System.err.println(" " + e1.getMessage());
            return (CaFehler.pfdXyBereitsVorhanden);

        }
        return (1);
    }

}
