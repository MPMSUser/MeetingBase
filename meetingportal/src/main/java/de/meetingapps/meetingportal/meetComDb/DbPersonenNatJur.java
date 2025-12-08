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
import de.meetingapps.meetingportal.meetComEntities.EclPersonenNatJur;

/**Personen - juristisch und natürlich.
 * Verwendung:
 * > Bevollmächtigte (Dritte) - über DbWillenserklaerung mit DbMeldungen verbunden
 * > "Ausgelagerte" Adressen / Personen / Institute für DbMeldungen - direkt über Fremdschlüssel
 * 	verbunden
 *  
 *
 */
public class DbPersonenNatJur {

    private Connection verbindung = null;
    private DbBasis dbBasis = null;
    private DbBundle dbBundle = null;

    /**Zugriff nur über die Zugriffsfunktionen!
     */
    public EclPersonenNatJur personenNatJurArray[] = null;

    /*************************Initialisierung***************************/
    /* Verbindung in lokale Daten eintragen*/
    public DbPersonenNatJur(DbBundle datenbankbundle) {
        if (datenbankbundle.dbBasis == null) {
            System.err.println("vmcdbBasis nicht initialisiert");
            return;
        }
        dbBasis = datenbankbundle.dbBasis;
        verbindung = datenbankbundle.dbBasis.verbindung;
        dbBundle = datenbankbundle;
    }

    public int anzPersonenNatJurGefunden() {
        if (personenNatJurArray == null) {
            return 0;
        }
        return personenNatJurArray.length;
    }

    public EclPersonenNatJur PersonNatJurGefunden(int lfd) {
        return personenNatJurArray[lfd];
    }

    public int createTable() {
        int rc = 0;
        DbLowLevel lDbLowLevel = new DbLowLevel(dbBundle);
        rc = lDbLowLevel.createTable("CREATE TABLE " + dbBundle.getSchemaMandant() + "tbl_personennatjur ( "
                + "`mandant` int(11) NOT NULL, " + "`ident` int(11) NOT NULL, "
                + "`db_version` bigint(20) DEFAULT NULL, " + "`gehoertZuMeldung` int(11) DEFAULT NULL, "
                + "`istNatuerlichePerson` int(11) DEFAULT NULL, " + "`istSelbePersonWieIdent` int(11) DEFAULT NULL, "
                + "`uebereinstimmungSelbePersonWurdeUeberprueft` int(11) DEFAULT NULL, "
                + "`stimmausschluss` char(13) DEFAULT NULL, " + "`kurzText` char(80) DEFAULT NULL, "
                + "`kurzName` char(80) DEFAULT NULL, " + "`kurzOrt` char(80) DEFAULT NULL, "
                + "`anrede` int(11) DEFAULT NULL, " + "`titel` char(30) DEFAULT NULL, "
                + "`adelstitel` char(30) DEFAULT NULL, " + "`name` char(80) DEFAULT NULL, "
                + "`vorname` char(80) DEFAULT NULL, " + "`zuHdCo` char(80) DEFAULT NULL, "
                + "`zusatz1` char(80) DEFAULT NULL, " + "`zusatz2` char(80) DEFAULT NULL, "
                + "`strasse` char(80) DEFAULT NULL, " + "`land` char(4) DEFAULT NULL, "
                + "`plz` char(20) DEFAULT NULL, " + "`ort` char(80) DEFAULT NULL, "
                + "`identVersandadresse` int(11) DEFAULT NULL, " + "`mailadresse` char(80) DEFAULT NULL, "
                + "`kommunikationssprache` int(11) DEFAULT NULL, " + "`loginKennung` char(20) DEFAULT NULL, "
                + "`loginPasswort` char(20) DEFAULT NULL, " + "`oeffentlicheID` char(30) DEFAULT NULL, "
                + "PRIMARY KEY (`ident`,`mandant`) " + ") ");
        if (rc < 1) {
            return rc;
        }

        lDbLowLevel = new DbLowLevel(dbBundle);
        rc = lDbLowLevel.createTable("CREATE TABLE " + dbBundle.getSchemaMandant() + "tbl_personennatjurprotokoll ( "
                + "`satzArt` int(11) NOT NULL, " + "`protokollIdent` int(11) NOT NULL, "
                + "`mandant` int(11) NOT NULL, " + "`ident` int(11) DEFAULT NULL, "
                + "`db_version` bigint(20) DEFAULT NULL, " + "`gehoertZuMeldung` int(11) DEFAULT NULL, "
                + "`istNatuerlichePerson` int(11) DEFAULT NULL, " + "`istSelbePersonWieIdent` int(11) DEFAULT NULL, "
                + "`uebereinstimmungSelbePersonWurdeUeberprueft` int(11) DEFAULT NULL, "
                + "`stimmausschluss` char(13) DEFAULT NULL, " + "`kurzText` char(80) DEFAULT NULL, "
                + "`kurzName` char(80) DEFAULT NULL, " + "`kurzOrt` char(80) DEFAULT NULL, "
                + "`anrede` int(11) DEFAULT NULL, " + "`titel` char(30) DEFAULT NULL, "
                + "`adelstitel` char(30) DEFAULT NULL, " + "`name` char(80) DEFAULT NULL, "
                + "`vorname` char(80) DEFAULT NULL, " + "`zuHdCo` char(80) DEFAULT NULL, "
                + "`zusatz1` char(80) DEFAULT NULL, " + "`zusatz2` char(80) DEFAULT NULL, "
                + "`strasse` char(80) DEFAULT NULL, " + "`land` char(4) DEFAULT NULL, "
                + "`plz` char(20) DEFAULT NULL, " + "`ort` char(80) DEFAULT NULL, "
                + "`identVersandadresse` int(11) DEFAULT NULL, " + "`mailadresse` char(80) DEFAULT NULL, "
                + "`kommunikationssprache` int(11) DEFAULT NULL, " + "`loginKennung` char(20) DEFAULT NULL, "
                + "`loginPasswort` char(20) DEFAULT NULL, " + "`oeffentlicheID` char(30) DEFAULT NULL, "
                + "PRIMARY KEY (`satzArt`,`protokollIdent`,`mandant`) " + ") ");
        return rc;
    }

    public void reorgInterneIdent() {
        int lMax = dbBundle.dbLowLevel.liefereHoechsteIdent(
                "SELECT MAX(ident) FROM " + dbBundle.getSchemaMandant() + "tbl_personennatjur ab where ab.mandant=? ");
        if (lMax != -1) {
            dbBundle.dbBasis.resetInterneIdentPersonenNatJur(lMax);
        }
    }

    /**************************deleteAll Löschen aller Datensätze  eines Mandanten****************/
    public int deleteAll() {
        int erg = 0;

        dbBasis.resetInterneIdentPersonenNatJur();

        erg = dbBundle.dbLowLevel
                .deleteMandant("DELETE FROM " + dbBundle.getSchemaMandant() + "tbl_personenNatJur where mandant=?;");
        if (erg < 1) {
            return erg;
        }
        erg = dbBundle.dbLowLevel.deleteMandant(
                "DELETE FROM " + dbBundle.getSchemaMandant() + "tbl_personenNatJurProtokoll where mandant=?;");
        return erg;
    }

    /**************************setzt aktuelle Mandantennummer bei allen Datensätzen****************/
    public int updateMandant() {
        dbBundle.dbLowLevel.rawUpdateMandant(dbBundle.getSchemaMandant() + "tbl_personenNatJur");
        return dbBundle.dbLowLevel.rawUpdateMandant(dbBundle.getSchemaMandant() + "tbl_personenNatJurProtokoll");
    }

    /** dekodiert die aktuelle Position aus ergebnis in EclPersonenNatJur und gibt dieses zurück*/
    private int anzFelder = 29;

    EclPersonenNatJur decodeErgebnis(ResultSet ergebnis) {
        EclPersonenNatJur lPersonNatJur = new EclPersonenNatJur();

        try {
            lPersonNatJur.mandant = ergebnis.getInt("p.mandant");
            lPersonNatJur.ident = ergebnis.getInt("p.ident");
            lPersonNatJur.istSelbePersonWieIdent = ergebnis.getInt("p.istSelbePersonWieIdent");
            lPersonNatJur.uebereinstimmungSelbePersonWurdeUeberprueft = ergebnis
                    .getInt("p.uebereinstimmungSelbePersonWurdeUeberprueft");
            lPersonNatJur.db_version = ergebnis.getLong("p.db_version");

            lPersonNatJur.gehoertZuMeldung = ergebnis.getInt("p.gehoertZuMeldung");

            lPersonNatJur.istNatuerlichePerson = ergebnis.getInt("p.istNatuerlichePerson");
            lPersonNatJur.stimmausschluss = ergebnis.getString("p.stimmausschluss");

            lPersonNatJur.kurztext = ergebnis.getString("p.kurztext");
            lPersonNatJur.kurzName = ergebnis.getString("p.kurzName");
            lPersonNatJur.kurzOrt = ergebnis.getString("p.kurzOrt");

            lPersonNatJur.anrede = ergebnis.getInt("p.anrede");
            lPersonNatJur.titel = ergebnis.getString("p.titel");
            lPersonNatJur.adelstitel = ergebnis.getString("p.adelstitel");

            lPersonNatJur.name = ergebnis.getString("p.name");
            lPersonNatJur.vorname = ergebnis.getString("p.vorname");
            lPersonNatJur.zuHdCo = ergebnis.getString("p.zuHdCo");
            lPersonNatJur.zusatz1 = ergebnis.getString("p.zusatz1");
            lPersonNatJur.zusatz2 = ergebnis.getString("p.zusatz2");
            lPersonNatJur.strasse = ergebnis.getString("p.strasse");
            lPersonNatJur.land = ergebnis.getString("p.land");
            lPersonNatJur.plz = ergebnis.getString("p.plz");
            lPersonNatJur.ort = ergebnis.getString("p.ort");
            lPersonNatJur.identVersandadresse = ergebnis.getInt("p.identVersandadresse");
            lPersonNatJur.mailadresse = ergebnis.getString("p.mailadresse");

            lPersonNatJur.kommunikationssprache = ergebnis.getInt("p.kommunikationssprache");
            lPersonNatJur.loginKennung = ergebnis.getString("p.loginKennung");
            lPersonNatJur.loginPasswort = ergebnis.getString("p.loginPasswort");
            lPersonNatJur.oeffentlicheID = ergebnis.getString("p.oeffentlicheID");
        } catch (Exception e) {
            CaBug.drucke("DbPersonenNatJur.decodeErgebnis 001");
            System.err.println(" " + e.getMessage());
        }

        return lPersonNatJur;
    }

    EclPersonenNatJur decodeErgebnis1(ResultSet ergebnis) {
        EclPersonenNatJur lPersonNatJur = new EclPersonenNatJur();

        try {
            lPersonNatJur.mandant = ergebnis.getInt("p1.mandant");
            lPersonNatJur.ident = ergebnis.getInt("p1.ident");
            lPersonNatJur.istSelbePersonWieIdent = ergebnis.getInt("p1.istSelbePersonWieIdent");
            lPersonNatJur.uebereinstimmungSelbePersonWurdeUeberprueft = ergebnis
                    .getInt("p1.uebereinstimmungSelbePersonWurdeUeberprueft");
            lPersonNatJur.db_version = ergebnis.getLong("p1.db_version");

            lPersonNatJur.gehoertZuMeldung = ergebnis.getInt("p1.gehoertZuMeldung");

            lPersonNatJur.istNatuerlichePerson = ergebnis.getInt("p1.istNatuerlichePerson");
            lPersonNatJur.stimmausschluss = ergebnis.getString("p1.stimmausschluss");

            lPersonNatJur.kurztext = ergebnis.getString("p1.kurztext");
            lPersonNatJur.kurzName = ergebnis.getString("p1.kurzName");
            lPersonNatJur.kurzOrt = ergebnis.getString("p1.kurzOrt");

            lPersonNatJur.anrede = ergebnis.getInt("p1.anrede");
            lPersonNatJur.titel = ergebnis.getString("p1.titel");
            lPersonNatJur.adelstitel = ergebnis.getString("p1.adelstitel");

            lPersonNatJur.name = ergebnis.getString("p1.name");
            lPersonNatJur.vorname = ergebnis.getString("p1.vorname");
            lPersonNatJur.zuHdCo = ergebnis.getString("p1.zuHdCo");
            lPersonNatJur.zusatz1 = ergebnis.getString("p1.zusatz1");
            lPersonNatJur.zusatz2 = ergebnis.getString("p1.zusatz2");
            lPersonNatJur.strasse = ergebnis.getString("p1.strasse");
            lPersonNatJur.land = ergebnis.getString("p1.land");
            lPersonNatJur.plz = ergebnis.getString("p1.plz");
            lPersonNatJur.ort = ergebnis.getString("p1.ort");
            lPersonNatJur.identVersandadresse = ergebnis.getInt("p1.identVersandadresse");
            lPersonNatJur.mailadresse = ergebnis.getString("p1.mailadresse");

            lPersonNatJur.kommunikationssprache = ergebnis.getInt("p1.kommunikationssprache");
            lPersonNatJur.loginKennung = ergebnis.getString("p1.loginKennung");
            lPersonNatJur.loginPasswort = ergebnis.getString("p1.loginPasswort");
            lPersonNatJur.oeffentlicheID = ergebnis.getString("p1.oeffentlicheID");
        } catch (Exception e) {
            CaBug.drucke("DbPersonenNatJur.decodeErgebnis1 001");
            System.err.println(" " + e.getMessage());
        }

        return lPersonNatJur;
    }

    /** Fuellen Prepared Statement mit allen Feldern, beginnend bei offset.
     * Kann sowohl für Insert, als auch für update verwendet werden.
     * 
     * offset= Startposition des ersten Feldes (also z.B. 1)
     */
    private void fuellePreparedStatementKomplett(PreparedStatement pstm, int offset, EclPersonenNatJur lPersonNatJur) {

        lPersonNatJur.mandant = dbBundle.clGlobalVar.mandant;
        try {
            pstm.setInt(offset, lPersonNatJur.mandant);
            offset++;
            pstm.setInt(offset, lPersonNatJur.ident);
            offset++;
            pstm.setInt(offset, lPersonNatJur.istSelbePersonWieIdent);
            offset++;
            pstm.setInt(offset, lPersonNatJur.uebereinstimmungSelbePersonWurdeUeberprueft);
            offset++;
            pstm.setLong(offset, lPersonNatJur.db_version);
            offset++;
            pstm.setLong(offset, lPersonNatJur.gehoertZuMeldung);
            offset++;

            pstm.setInt(offset, lPersonNatJur.istNatuerlichePerson);
            offset++;
            pstm.setString(offset, lPersonNatJur.stimmausschluss);
            offset++;

            pstm.setString(offset, CaString.trunc(lPersonNatJur.kurztext, 80));
            offset++;

            /*KurzName füllen, falls leer*/
            if (lPersonNatJur.kurzName.isEmpty()) {
                lPersonNatJur.kurzName = lPersonNatJur.name;
                if (!lPersonNatJur.vorname.isEmpty()) {
                    lPersonNatJur.kurzName = lPersonNatJur.kurzName + ", " + lPersonNatJur.vorname;
                }
                CaString.trunc(lPersonNatJur.kurzName, 80);
            }
            /*KurzOrt füllen, falls leer*/
            if (lPersonNatJur.kurzOrt.isEmpty()) {
                lPersonNatJur.kurzOrt = lPersonNatJur.ort;
                CaString.trunc(lPersonNatJur.ort, 80);
            }
            pstm.setString(offset, CaString.trunc(lPersonNatJur.kurzName, 80));
            offset++;
            pstm.setString(offset, CaString.trunc(lPersonNatJur.kurzOrt, 80));
            offset++;

            pstm.setInt(offset, lPersonNatJur.anrede);
            offset++;
            pstm.setString(offset, CaString.trunc(lPersonNatJur.titel, 30));
            offset++;
            pstm.setString(offset, CaString.trunc(lPersonNatJur.adelstitel, 30));
            offset++;

            pstm.setString(offset, CaString.trunc(lPersonNatJur.name, 80));
            offset++;
            pstm.setString(offset, CaString.trunc(lPersonNatJur.vorname, 80));
            offset++;
            pstm.setString(offset, CaString.trunc(lPersonNatJur.zuHdCo, 80));
            offset++;
            pstm.setString(offset, CaString.trunc(lPersonNatJur.zusatz1, 80));
            offset++;
            pstm.setString(offset, CaString.trunc(lPersonNatJur.zusatz2, 80));
            offset++;
            pstm.setString(offset, CaString.trunc(lPersonNatJur.strasse, 80));
            offset++;
            pstm.setString(offset, CaString.trunc(lPersonNatJur.land, 4));
            offset++;
            pstm.setString(offset, CaString.trunc(lPersonNatJur.plz, 20));
            offset++;
            pstm.setString(offset, CaString.trunc(lPersonNatJur.ort, 80));
            offset++;
            pstm.setInt(offset, lPersonNatJur.identVersandadresse);
            offset++;
            pstm.setString(offset, CaString.trunc(lPersonNatJur.mailadresse, 80));
            offset++;
            pstm.setInt(offset, lPersonNatJur.kommunikationssprache);
            offset++;

            pstm.setString(offset, CaString.trunc(lPersonNatJur.loginKennung, 20));
            offset++;
            pstm.setString(offset, CaString.trunc(lPersonNatJur.loginPasswort, 20));
            offset++;
            pstm.setString(offset, lPersonNatJur.oeffentlicheID);
            offset++;

        } catch (SQLException e) {
            CaBug.drucke("DbPersonenNatJur.fuellePreparedStatementKomplett 001");
            e.printStackTrace();
        }

    }

    private String insertStringFelder() {
        return "mandant, ident, istSelbePersonWieIdent, uebereinstimmungSelbePersonWurdeUeberprueft, db_version, "
                + "gehoertZuMeldung, istNatuerlichePerson, stimmausschluss, kurztext, kurzName, kurzOrt, "
                + "anrede, titel, adelstitel, " + "name, vorname, zuHdCo, zusatz1, zusatz2, "
                + "strasse, land, plz, ort, identVersandadresse, " + "mailadresse, " + "kommunikationssprache,"
                + "loginKennung, loginPasswort, oeffentlicheID";
    }

    private String insertStringPlatzhalter() {
        return "?, ?, ?, ?, ?, " + "?, ?, ?, ?, ?, ?," + "?, ?, ?, " + "?, ?, ?, ?, ?, " + "?, ?, ?, ?, ?, " + "?, "
                + "?, " + "?, ?, ?";
    }

    public int insert(EclPersonenNatJur lPersonNatJur) {

        int erg;

        /* Start Transaktion */
        dbBasis.beginTransaction();

        /* neue InterneIdent vergeben */
        erg = dbBasis.getInterneIdentPersonenNatJur();
        if (erg < 1) {
            CaBug.drucke("DbPersonenNatJur.insert 002");
            dbBasis.endTransaction();
            return (erg);
        }
        lPersonNatJur.ident = erg;

        /*lPersonNatJur einfügen */
        /* Verarbeitungshinweise: 
         * 	>	nachdem InterneIdent immer eindeutig vergeben werden, ist prinzipiell eine "Doppeleinfügung"
         * 		von InterneIdent nicht möglich
         */

        try {

            /*Felder Neuanlage füllen*/
            String sql1 = "INSERT INTO " + dbBundle.getSchemaMandant() + "tbl_personenNatJur " + "("
                    + insertStringFelder() + ")" + "VALUES (" + insertStringPlatzhalter() + ")";
            PreparedStatement pstm1 = verbindung.prepareStatement(sql1, ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);

            fuellePreparedStatementKomplett(pstm1, 1, lPersonNatJur);

            erg = 0; /*Falls Duplicate Key, dann wird exception geschmissen und erg wohl nicht gefüllt!*/
            erg = pstm1.executeUpdate();
            pstm1.close();
        } catch (Exception e2) {
            CaBug.drucke("DbPersonenNatJur.insert 001");
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

    public int insertProtokoll(int protokollIdent, int pAenderungsart, EclPersonenNatJur lPersonNatJur,
            String pUpdateZeit, int pUserNummer, int pArbeitsplatzNummer) {

        int erg = 0;

        /* Start Transaktion */
        dbBasis.beginTransaction();

        /*lPersonNatJur einfügen */
        try {

            /*Felder Neuanlage füllen*/
            String sql1 = "INSERT INTO " + dbBundle.getSchemaMandant() + "tbl_personenNatJurProtokoll " + "("
                    + "satzArt, protokollIdent, " + insertStringFelder() + ")" + "VALUES (" + "?,?,"
                    + insertStringPlatzhalter() + ")";
            PreparedStatement pstm1 = verbindung.prepareStatement(sql1, ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);

            pstm1.setInt(1, pAenderungsart);
            pstm1.setInt(2, protokollIdent);
            fuellePreparedStatementKomplett(pstm1, 3, lPersonNatJur);

            erg = 0; /*Falls Duplicate Key, dann wird exception geschmissen und erg wohl nicht gefüllt!*/
            erg = pstm1.executeUpdate();
            pstm1.close();
        } catch (Exception e2) {
            CaBug.drucke("DbPersonenNatJur.insertProtokoll 001");
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

    /**Einlesen in array der PersonNatJur, mit ident*/
    public int read(int lIdent) {
        int anzInArray = 0;
        try {

            String sql = "SELECT p.* from " + dbBundle.getSchemaMandant() + "tbl_personenNatJur p where "
                    + "p.mandant=? AND " + "p.ident=?;";

            PreparedStatement pstm1 = verbindung.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
            pstm1.setInt(1, dbBundle.clGlobalVar.mandant);
            pstm1.setInt(2, lIdent);

            ResultSet ergebnis = pstm1.executeQuery();
            ergebnis.last();
            anzInArray = ergebnis.getRow();
            ergebnis.beforeFirst();

            personenNatJurArray = new EclPersonenNatJur[anzInArray];
            //	System.out.println("anzinarray"+anzInArray);
            int i = 0;
            while (ergebnis.next() == true) {
                personenNatJurArray[i] = this.decodeErgebnis(ergebnis);
                i++;

            }
            ergebnis.close();
            pstm1.close();

        } catch (Exception e) {
            CaBug.drucke("DbPersonenNatJur.read 001");
            System.err.println(" " + e.getMessage());
        }

        return (anzInArray);
    }

    
    public int readNamensfelder(String pName) {
        int anzInArray = 0;
        try {

            String sql = "SELECT p.* from " + dbBundle.getSchemaMandant() + "tbl_personenNatJur p "
                    + "where "
                    + "p.name LIKE ? OR "
                    + "p.vorname LIKE ? OR "
                    + "p.zuHdCo LIKE ? OR "
                    + "p.zusatz1 LIKE ? OR "
                    + "p.zusatz2 LIKE ? "
                    ;

            PreparedStatement pstm1 = verbindung.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
            pstm1.setString(1, "%"+pName+"%");
            pstm1.setString(2, "%"+pName+"%");
            pstm1.setString(3, "%"+pName+"%");
            pstm1.setString(4, "%"+pName+"%");
            pstm1.setString(5, "%"+pName+"%");

            ResultSet ergebnis = pstm1.executeQuery();
            ergebnis.last();
            anzInArray = ergebnis.getRow();
            ergebnis.beforeFirst();

            personenNatJurArray = new EclPersonenNatJur[anzInArray];
            //  System.out.println("anzinarray"+anzInArray);
            int i = 0;
            while (ergebnis.next() == true) {
                personenNatJurArray[i] = this.decodeErgebnis(ergebnis);
                i++;

            }
            ergebnis.close();
            pstm1.close();

        } catch (Exception e) {
            CaBug.drucke("DbPersonenNatJur.read 001");
            System.err.println(" " + e.getMessage());
        }

        return (anzInArray);
    }

    
    /**Realisiert derzeit, jeweils:  
     * loginKennung
     * oeffentlicheID*/
    public int leseZuPersonenNatJur(EclPersonenNatJur lPersonenNatJur) {
        String sql;
        PreparedStatement pstm1 = null;
        int anzInArray = 0;
        try {

            if (!lPersonenNatJur.loginKennung.isEmpty()) {
                sql = "SELECT p.* from " + dbBundle.getSchemaMandant() + "tbl_personenNatJur p where "
                        + "p.mandant=? AND " + "p.loginKennung=?;";

                pstm1 = verbindung.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                pstm1.setInt(1, dbBundle.clGlobalVar.mandant);
                pstm1.setString(2, lPersonenNatJur.loginKennung);
            }
            if (!lPersonenNatJur.oeffentlicheID.isEmpty()) {
                sql = "SELECT p.* from " + dbBundle.getSchemaMandant() + "tbl_personenNatJur p where "
                        + "p.mandant=? AND " + "p.oeffentlicheID=?;";

                pstm1 = verbindung.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                pstm1.setInt(1, dbBundle.clGlobalVar.mandant);
                pstm1.setString(2, lPersonenNatJur.oeffentlicheID);
            }

            ResultSet ergebnis = pstm1.executeQuery();
            ergebnis.last();
            anzInArray = ergebnis.getRow();
            ergebnis.beforeFirst();

            personenNatJurArray = new EclPersonenNatJur[anzInArray];

            int i = 0;
            while (ergebnis.next() == true) {
                personenNatJurArray[i] = this.decodeErgebnis(ergebnis);
                i++;

            }
            ergebnis.close();
            pstm1.close();

        } catch (Exception e) {
            CaBug.drucke("DbPersonenNatJur.leseZuPersonenNatJur 001");
            System.err.println(" " + e.getMessage());
        }

        return (anzInArray);
    }

    /**Einlesen in array aller PersonenNatJur, bei denen kurztext!="" ist (also alle "allgemein interessanten / auswählbaren)*/
    public int leseAlleMitKurztext() {
        int anzInArray = 0;
        try {

            String sql = "SELECT p.* from " + dbBundle.getSchemaMandant() + "tbl_personenNatJur p where "
                    + "p.mandant=? AND " + "p.kurztext!=?;";

            PreparedStatement pstm1 = verbindung.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
            pstm1.setInt(1, dbBundle.clGlobalVar.mandant);
            pstm1.setString(2, "");

            ResultSet ergebnis = pstm1.executeQuery();
            ergebnis.last();
            anzInArray = ergebnis.getRow();
            ergebnis.beforeFirst();

            personenNatJurArray = new EclPersonenNatJur[anzInArray];

            int i = 0;
            while (ergebnis.next() == true) {
                personenNatJurArray[i] = this.decodeErgebnis(ergebnis);
                i++;

            }

            ergebnis.close();
            pstm1.close();

        } catch (Exception e) {
            CaBug.drucke("DbPersonenNatJur.leseAlleMitKurztext 001");
            System.err.println(" " + e.getMessage());
        }

        return (anzInArray);
    }

    public int leseKennung(String pKennung) {
        int anzInArray = 0;
        try {

            String sql = "SELECT p.* from " + dbBundle.getSchemaMandant() + "tbl_personenNatJur p where "
                    + "p.mandant=? AND " + "p.loginKennung=?;";

            PreparedStatement pstm1 = verbindung.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
            pstm1.setInt(1, dbBundle.clGlobalVar.mandant);
            pstm1.setString(2, pKennung);

            ResultSet ergebnis = pstm1.executeQuery();
            ergebnis.last();
            anzInArray = ergebnis.getRow();
            ergebnis.beforeFirst();

            personenNatJurArray = new EclPersonenNatJur[anzInArray];

            int i = 0;
            while (ergebnis.next() == true) {
                personenNatJurArray[i] = this.decodeErgebnis(ergebnis);
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

    
    
    /**Suchen nach Name*/
    public int leseAlleGaesteMitName(String pName) {
        int anzInArray = 0;
        try {

            String sql = "SELECT p.* from " + dbBundle.getSchemaMandant() + "tbl_personenNatJur p where "
                    + "p.mandant=? AND p.loginKennung like 'S%' AND " + "(p.name like ? OR p.vorname like ?);";

            PreparedStatement pstm1 = verbindung.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
            pstm1.setInt(1, dbBundle.clGlobalVar.mandant);
            pstm1.setString(2, "%" + pName + "%");
            pstm1.setString(3, "%" + pName + "%");

            ResultSet ergebnis = pstm1.executeQuery();
            ergebnis.last();
            anzInArray = ergebnis.getRow();
            ergebnis.beforeFirst();

            personenNatJurArray = new EclPersonenNatJur[anzInArray];

            int i = 0;
            while (ergebnis.next() == true) {
                personenNatJurArray[i] = this.decodeErgebnis(ergebnis);
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

    public String[] kiavArray = null;

    /**Einlesen in array aller PersonenNatJur, die als Vertreter bei einer Sammelkarte eingetragen waren oder sind*/
    public int leseAlleSammelkartenVertreter() {
        int anzInArray = 0;
        try {
            String sql =

                    /**
                    
                    "SELECT p.* FROM tbl_personennatjur p WHERE p.ident IN" 
                    + "(SELECT DISTINCT w.bevollmaechtigterDritterIdent FROM tbl_willenserklaerung w WHERE w.meldungsIdent IN "
                    + "(SELECT m.meldungsIdent FROM tbl_meldungen m WHERE m.mandant=? AND meldungstyp=2) "
                    + "AND w.willenserklaerung=300);";
                    */

                    "SELECT DISTINCT m.meldungsIdent, p1.kurzName, p.*  FROM " + dbBundle.getSchemaMandant()
                            + "tbl_personennatjur p " + "INNER JOIN (" + dbBundle.getSchemaMandant()
                            + "tbl_willenserklaerung w INNER JOIN " + "(" + dbBundle.getSchemaMandant()
                            + "tbl_meldungen m INNER JOIN " + dbBundle.getSchemaMandant()
                            + "tbl_PersonennatJur p1 on m.personenNatJurIdent=p1.ident) "
                            + "on w.meldungsIdent=m.MeldungsIdent) "
                            + "on p.ident=w.bevollmaechtigterDritterIdent WHERE m.meldungstyp=2 AND w.willenserklaerung=300 AND "
                            + "p.mandant=? AND w.mandant=? AND m.mandant=? AND p1.mandant=? ORDER BY p.name;";

            PreparedStatement pstm1 = verbindung.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
            pstm1.setInt(1, dbBundle.clGlobalVar.mandant);
            pstm1.setInt(2, dbBundle.clGlobalVar.mandant);
            pstm1.setInt(3, dbBundle.clGlobalVar.mandant);
            pstm1.setInt(4, dbBundle.clGlobalVar.mandant);

            ResultSet ergebnis = pstm1.executeQuery();
            ergebnis.last();
            anzInArray = ergebnis.getRow();
            ergebnis.beforeFirst();

            personenNatJurArray = new EclPersonenNatJur[anzInArray];
            kiavArray = new String[anzInArray];

            int i = 0;
            while (ergebnis.next() == true) {
                personenNatJurArray[i] = this.decodeErgebnis(ergebnis);
                kiavArray[i] = ergebnis.getString("p1.kurzName");

                i++;

            }
            ergebnis.close();
            pstm1.close();

        } catch (Exception e) {
            CaBug.drucke("DbPersonenNatJur.leseAlleSammelkartenVertreter 001");
            System.err.println(" " + e.getMessage());
        }

        return (anzInArray);
    }

    /**Update einer EclPersonenNatJur. Versionsnummer wird um 1 hochgezählt
     */
    public int update(EclPersonenNatJur lPersonNatJur) {

        lPersonNatJur.db_version++;
        try {

            String sql = "UPDATE " + dbBundle.getSchemaMandant() + "tbl_personenNatJur SET "
                    + "mandant=?, ident=?, istSelbePersonWieIdent=?, uebereinstimmungSelbePersonWurdeUeberprueft=?, db_version=?, "
                    + "gehoertZuMeldung=?, istNatuerlichePerson=?, stimmausschluss=?, kurztext=?, kurzName=?, kurzOrt=?, "
                    + "anrede=?, titel=?, adelstitel=?, " + "name=?, vorname=?, zuHdCo=?, zusatz1=?, zusatz2=?, "
                    + "strasse=?, land=?, plz=?, ort=?, identVersandadresse=?, " + "mailadresse=?, "
                    + "kommunikationssprache=?, " + "loginKennung=?, loginPasswort=?, oeffentlicheID=?" + " WHERE "
                    + "ident=? AND " + "db_version=? AND " + "mandant=?";

            PreparedStatement pstm1 = verbindung.prepareStatement(sql);
            fuellePreparedStatementKomplett(pstm1, 1, lPersonNatJur);
            pstm1.setInt(this.anzFelder + 1, lPersonNatJur.ident);
            pstm1.setLong(this.anzFelder + 2, lPersonNatJur.db_version - 1);
            pstm1.setLong(this.anzFelder + 3, dbBundle.clGlobalVar.mandant);

            int ergebnis1 = pstm1.executeUpdate();
            pstm1.close();
            if (ergebnis1 == 0) {
                return (CaFehler.pfXyWurdeVonAnderemBenutzerVeraendert);
            }
        } catch (Exception e1) {
            CaBug.drucke("DbPersonenNatJur.update 001");
            System.err.println(" " + e1.getMessage());
            return (CaFehler.pfdXyBereitsVorhanden);

        }

        return (1);
    }

}
