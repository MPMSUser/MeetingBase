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
import de.meetingapps.meetingportal.meetComAllg.CaIntBoolean;
import de.meetingapps.meetingportal.meetComBVerwaltung.BvReload;
import de.meetingapps.meetingportal.meetComEntities.EclUserLogin;

public class DbUserLogin {

    private int logDrucken=3;
    
    private Connection verbindung = null;
    private DbBasis dbBasis = null;
    private DbBundle dbBundle = null;

    public EclUserLogin userLoginArray[] = null;

    /**Ggf. vor Benutzung setzen auf true; anschließend unbedingt wieder auf false setzen!*/
    public boolean profileVerarbeiten=false;

    /*************************Initialisierung***************************/
    /* Verbindung in lokale Daten eintragen*/
    public DbUserLogin(DbBundle datenbankbundle) {
        if (datenbankbundle.dbBasis == null) {
            CaBug.drucke("001 - bBasis nicht initialisiert");
            return;
        }
        dbBasis = datenbankbundle.dbBasis;
        verbindung = datenbankbundle.dbBasis.verbindung;
        dbBundle = datenbankbundle;
    }

    public int anzUserLoginGefunden() {
        if (userLoginArray == null) {
            return 0;
        }
        return userLoginArray.length;
    }

    public EclUserLogin userLoginGefunden(int lfd) {
        return userLoginArray[lfd];
    }

    String getTable() {
        if (profileVerarbeiten) {
            return "tbl_profile";
        }
        else {
            return "tbl_userlogin";
        }
    }

    public int createTable() {
        int rc = 0;
        DbLowLevel lDbLowLevel = new DbLowLevel(dbBundle);
//      @formatter:off
        rc = lDbLowLevel.createTable("CREATE TABLE " + dbBundle.getSchemaAllgemein() + getTable()+" ( "
                + "`mandant` int(11) NOT NULL, " 
                + "`userLoginIdent` int(11) NOT NULL, "
                + "`db_version` bigint(20) DEFAULT NULL, " 
                + "`kennung` char(40) DEFAULT NULL, "
                + "`passwort` char(64) DEFAULT NULL, " 
                + "`name` char(200) DEFAULT NULL, "
                + "`email` char(200) DEFAULT NULL, " 
                + "`gehoertZuInsti` int(11) DEFAULT '0', "
                + "`berechtigungen0` char(20) DEFAULT NULL, " 
                + "`berechtigungen1` char(20) DEFAULT NULL, "
                + "`berechtigungen2` char(20) DEFAULT NULL, " 
                + "`berechtigungen3` char(20) DEFAULT NULL, "
                + "`berechtigungen4` char(20) DEFAULT NULL, " 
                + "`berechtigungen5` char(20) DEFAULT NULL, "
                + "`berechtigungen6` char(20) DEFAULT NULL, " 
                + "`berechtigungen7` char(20) DEFAULT NULL, "
                + "`berechtigungen8` char(20) DEFAULT NULL, " 
                + "`berechtigungen9` char(20) DEFAULT NULL, "
                + "`berechtigungen10` char(20) DEFAULT NULL, " 
                + "`berechtigungen11` char(20) DEFAULT NULL, "
                + "`berechtigungen12` char(20) DEFAULT NULL, " 
                + "`berechtigungen13` char(20) DEFAULT NULL, "
                + "`berechtigungen14` char(20) DEFAULT NULL, " 
                + "`berechtigungen15` char(20) DEFAULT NULL, "
                + "`berechtigungen16` char(20) DEFAULT NULL, " 
                + "`berechtigungen17` char(20) DEFAULT NULL, "
                + "`berechtigungen18` char(20) DEFAULT NULL, " 
                + "`berechtigungen19` char(20) DEFAULT NULL, "
                + "`neuesPasswortErforderlich` int(11) DEFAULT NULL, "
                + "`kennungGesperrtDurchSoftware` int(11) DEFAULT NULL, "
                + "`kennungGesperrtManuell` int(11) DEFAULT NULL, "
                + "`trivialPasswortZulaessig` int(11) DEFAULT NULL, "
                + "`gruppenKennungHV` int(11) DEFAULT NULL, "
                + "`passwortZuletztGeaendertAm` char(19) DEFAULT NULL, "
                + "`altesPasswort1` char(64) DEFAULT NULL, "
                + "`altesPasswort2` char(64) DEFAULT NULL, "
                + "`altesPasswort3` char(64) DEFAULT NULL, "
                + "PRIMARY KEY (`userLoginIdent`,`mandant`) " 
                + ") ");
//      @formatter:on
        return rc;
    }

    
    //	ALTER TABLE `db_meetingcomfort`.`tbl_userlogin` 
    //	ADD COLUMN `gehoertZuInsti` INT(11) NULL DEFAULT NULL AFTER `email`;

    public void reorgInterneIdent() {
        int lMax = dbBundle.dbLowLevel.liefereHoechsteIdentOhneMandant("SELECT MAX(userLoginIdent) FROM "
                + dbBundle.getSchemaAllgemein() + getTable()+" ab WHERE userLoginIdent<9000");
        CaBug.druckeLog("reorg UserIdent", logDrucken, 10);
        if (lMax != -1) {
            CaBug.druckeLog("reorg UserIdent Update", logDrucken, 10);
            if (profileVerarbeiten==false) {
                dbBundle.dbBasis.resetInterneIdentUserLoginOhneMandant(lMax);
            }
            else {
                dbBundle.dbBasis.resetInterneIdentUserProfileOhneMandant(lMax);
            }
        }
    }

    /** dekodiert die aktuelle Position aus ergebnis in EclUserLogin und gibt dieses zurück*/
    EclUserLogin decodeErgebnis(ResultSet ergebnis) {
        EclUserLogin lUserLogin = new EclUserLogin();

        try {

            lUserLogin.mandant = ergebnis.getInt("ul.mandant");
            lUserLogin.userLoginIdent = ergebnis.getInt("ul.userLoginIdent");
            lUserLogin.db_version = ergebnis.getLong("ul.db_version");

            lUserLogin.kennung = ergebnis.getString("ul.kennung");
            lUserLogin.passwort = ergebnis.getString("ul.passwort");
            lUserLogin.name = ergebnis.getString("ul.name");
            lUserLogin.email = ergebnis.getString("ul.email");
            lUserLogin.gehoertZuInsti = ergebnis.getInt("ul.gehoertZuInsti");
            int i, i1;
            for (i = 0; i < 20; i++) {
                String hstring = "";
                switch (i) {
                case 0:
                    hstring = ergebnis.getString("ul.berechtigungen0");
                    break;
                case 1:
                    hstring = ergebnis.getString("ul.berechtigungen1");
                    break;
                case 2:
                    hstring = ergebnis.getString("ul.berechtigungen2");
                    break;
                case 3:
                    hstring = ergebnis.getString("ul.berechtigungen3");
                    break;
                case 4:
                    hstring = ergebnis.getString("ul.berechtigungen4");
                    break;
                case 5:
                    hstring = ergebnis.getString("ul.berechtigungen5");
                    break;
                case 6:
                    hstring = ergebnis.getString("ul.berechtigungen6");
                    break;
                case 7:
                    hstring = ergebnis.getString("ul.berechtigungen7");
                    break;
                case 8:
                    hstring = ergebnis.getString("ul.berechtigungen8");
                    break;
                case 9:
                    hstring = ergebnis.getString("ul.berechtigungen9");
                    break;
                case 10:
                    hstring = ergebnis.getString("ul.berechtigungen10");
                    break;
                case 11:
                    hstring = ergebnis.getString("ul.berechtigungen11");
                    break;
                case 12:
                    hstring = ergebnis.getString("ul.berechtigungen12");
                    break;
                case 13:
                    hstring = ergebnis.getString("ul.berechtigungen13");
                    break;
                case 14:
                    hstring = ergebnis.getString("ul.berechtigungen14");
                    break;
                case 15:
                    hstring = ergebnis.getString("ul.berechtigungen15");
                    break;
                case 16:
                    hstring = ergebnis.getString("ul.berechtigungen16");
                    break;
                case 17:
                    hstring = ergebnis.getString("ul.berechtigungen17");
                    break;
                case 18:
                    hstring = ergebnis.getString("ul.berechtigungen18");
                    break;
                case 19:
                    hstring = ergebnis.getString("ul.berechtigungen19");
                    break;
                }
                for (i1 = 0; i1 < 20; i1++) {
                    lUserLogin.berechtigungen[i][i1] = 0;
                    if (hstring != null && hstring.length() > i1) {
                        if (hstring.substring(i1, i1 + 1).compareTo("1") == 0) {
                            lUserLogin.berechtigungen[i][i1] = 1;
                        }
                    }
                }
            }

            lUserLogin.neuesPasswortErforderlich = CaIntBoolean
                    .intToBoolean(ergebnis.getInt("ul.neuesPasswortErforderlich"));
            lUserLogin.kennungGesperrtDurchSoftware = CaIntBoolean
                    .intToBoolean(ergebnis.getInt("ul.kennungGesperrtDurchSoftware"));
            lUserLogin.kennungGesperrtManuell = CaIntBoolean.intToBoolean(ergebnis.getInt("ul.kennungGesperrtManuell"));
            lUserLogin.trivialPasswortZulaessig = CaIntBoolean
                    .intToBoolean(ergebnis.getInt("ul.trivialPasswortZulaessig"));
            lUserLogin.gruppenKennungHV = CaIntBoolean.intToBoolean(ergebnis.getInt("ul.gruppenKennungHV"));
            lUserLogin.passwortZuletztGeaendertAm = ergebnis.getString("ul.passwortZuletztGeaendertAm");
            if (lUserLogin.passwortZuletztGeaendertAm == null) {
                lUserLogin.passwortZuletztGeaendertAm = "";
            }
            lUserLogin.altesPasswort1 = ergebnis.getString("ul.altesPasswort1");
            if (lUserLogin.altesPasswort1 == null) {
                lUserLogin.altesPasswort1 = "";
            }
            lUserLogin.altesPasswort2 = ergebnis.getString("ul.altesPasswort2");
            if (lUserLogin.altesPasswort2 == null) {
                lUserLogin.altesPasswort2 = "";
            }
            lUserLogin.altesPasswort3 = ergebnis.getString("ul.altesPasswort3");
            if (lUserLogin.altesPasswort3 == null) {
                lUserLogin.altesPasswort3 = "";
            }

        } catch (Exception e) {
            CaBug.drucke("DbUserLogin.decodeErgebnis 001");
            System.err.println(" " + e.getMessage());
        }

        return lUserLogin;
    }

    /** Fuellen Prepared Statement mit allen Feldern, beginnend bei offset.
     * Kann sowohl für Insert, als auch für update verwendet werden.
     * 
     * offset= Startposition des ersten Feldes (also z.B. 1)
     */
    private int anzfelder = 37;

    private void fuellePreparedStatementKomplett(PreparedStatement pstm, int offset, EclUserLogin lUserLogin) {

        try {
            pstm.setInt(offset, lUserLogin.mandant);
            offset++;
            pstm.setInt(offset, lUserLogin.userLoginIdent);
            offset++;
            pstm.setLong(offset, lUserLogin.db_version);
            offset++;

            pstm.setString(offset, lUserLogin.kennung);
            offset++;
            pstm.setString(offset, lUserLogin.passwort);
            offset++;
            pstm.setString(offset, lUserLogin.name);
            offset++;
            pstm.setString(offset, lUserLogin.email);
            offset++;
            pstm.setInt(offset, lUserLogin.gehoertZuInsti);
            offset++;

            int i, i1;
            for (i = 0; i < 20; i++) {
                String hstring = "";
                for (i1 = 0; i1 < 20; i1++) {
                    if (lUserLogin.berechtigungen[i][i1] == 1) {
                        hstring = hstring + "1";
                    } else {
                        hstring = hstring + " ";
                    }
                }
                pstm.setString(offset, hstring);
                offset++;
            }

            pstm.setInt(offset, CaIntBoolean.booleanToInt(lUserLogin.neuesPasswortErforderlich));
            offset++;
            pstm.setInt(offset, CaIntBoolean.booleanToInt(lUserLogin.kennungGesperrtDurchSoftware));
            offset++;
            pstm.setInt(offset, CaIntBoolean.booleanToInt(lUserLogin.kennungGesperrtManuell));
            offset++;
            pstm.setInt(offset, CaIntBoolean.booleanToInt(lUserLogin.trivialPasswortZulaessig));
            offset++;
            pstm.setInt(offset, CaIntBoolean.booleanToInt(lUserLogin.gruppenKennungHV));
            offset++;
            pstm.setString(offset, lUserLogin.passwortZuletztGeaendertAm);
            offset++;
            pstm.setString(offset, lUserLogin.altesPasswort1);
            offset++;
            pstm.setString(offset, lUserLogin.altesPasswort2);
            offset++;
            pstm.setString(offset, lUserLogin.altesPasswort3);
            offset++;

        } catch (SQLException e) {
            CaBug.drucke("001");
            e.printStackTrace();
        }

    }

    public int insert(EclUserLogin lUserLogin, boolean pMitMandant) {
        return insert(lUserLogin, pMitMandant, true);
    }

    public int insert(EclUserLogin lUserLogin, boolean pMitMandant, boolean pMitUserIdentVergeben) {

        int erg = 0;

        /* Start Transaktion */
        dbBasis.beginTransaction();

        if (pMitUserIdentVergeben) {
            /* neue InterneIdent vergeben. Immer mandantenübergreifend, da Ident auch Primary Key für Puffer ist */
            if (profileVerarbeiten==false) {
                erg = dbBasis.getInterneIdentUserLoginOhneMandant();
            }
            else {
                erg = dbBasis.getInterneIdentUserProfileOhneMandant();
            }
            if (erg < 1) {
                CaBug.drucke("002");
                dbBasis.endTransaction();
                return (erg);
            }

            lUserLogin.userLoginIdent = erg;
        }

        if (pMitMandant) {
            if (profileVerarbeiten) {
                lUserLogin.mandant = 1;
            }
            else {
                lUserLogin.mandant = dbBundle.clGlobalVar.mandant;
            }
        } else {
            lUserLogin.mandant = 0;
        }

        /* VclMeldung einfügen */
        /* Verarbeitungshinweise: 
         * 	>	nachdem InterneIdent immer eindeutig vergeben werden, ist prinzipiell eine "Doppeleinfügung"
         * 		von InterneIdent nicht möglich
         */

        try {

            /*Felder Neuanlage füllen*/
            String sql1 = "INSERT INTO " + dbBundle.getSchemaAllgemein() + getTable()+ " ("
                    + "mandant, userLoginIdent, db_version, " + "kennung, passwort, name, email, gehoertZuInsti, "
                    + "berechtigungen0, berechtigungen1, berechtigungen2, berechtigungen3, berechtigungen4, "
                    + "berechtigungen5, berechtigungen6, berechtigungen7, berechtigungen8, berechtigungen9, "
                    + "berechtigungen10, berechtigungen11, berechtigungen12, berechtigungen13, berechtigungen14, "
                    + "berechtigungen15, berechtigungen16, berechtigungen17, berechtigungen18, berechtigungen19, "
                    + "neuesPasswortErforderlich, kennungGesperrtDurchSoftware, kennungGesperrtManuell, trivialPasswortZulaessig, gruppenKennungHV, passwortZuletztGeaendertAm, altesPasswort1, altesPasswort2, altesPasswort3"
                    + ")" +

                    "VALUES (" + "?, ?, ?, " + "?, ?, ?, ?, ?, " + "?, ?, ?, ?, ?, " + "?, ?, ?, ?, ?, "
                    + "?, ?, ?, ?, ?, " + "?, ?, ?, ?, ?, " + "?, ?, ?, ?, ?, ?, ?, ?, ? " + ")";
            PreparedStatement pstm1 = verbindung.prepareStatement(sql1, ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);

            fuellePreparedStatementKomplett(pstm1, 1, lUserLogin);

            erg = 0; /*Falls Duplicate Key, dann wird exception geschmissen und erg wohl nicht gefüllt!*/
            //			System.out.println("vor Update");
            erg = pstm1.executeUpdate();
            //			System.out.println("nach Update");
            pstm1.close();

        } catch (Exception e2) {
            CaBug.drucke("001");
            System.err.println(" " + e2.getMessage());
        }

        if (erg == 0) {/*Fehler beim Einfügen - d.h. ZutrittsIdent bereits vorhanden*/
            dbBasis.endTransaction();
            return (-1);
        }

        BvReload bvReload = new BvReload(dbBundle);
        bvReload.setReloadUserLogin();

        /* Ende Transaktion */
        dbBasis.endTransaction();

        return (1);
    }

    public int leseZuKennung(String kennung, boolean pMitMandant) {
        int anzInArray = 0;
        try {

            String sql = "SELECT * from " + dbBundle.getSchemaAllgemein() + getTable()+" ul where "
                    + "ul.mandant=? AND " + "ul.kennung=? ORDER BY ul.userLoginIdent;";

            PreparedStatement pstm1 = verbindung.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
            if (pMitMandant) {
                if (profileVerarbeiten) {
                    pstm1.setInt(1, 1);
                }
                else {
                    pstm1.setInt(1, dbBundle.clGlobalVar.mandant);
                }
            } else {
                pstm1.setInt(1, 0);
            }
            pstm1.setString(2, kennung);

            ResultSet ergebnis = pstm1.executeQuery();
            ergebnis.last();
            anzInArray = ergebnis.getRow();
            ergebnis.beforeFirst();

            userLoginArray = new EclUserLogin[anzInArray];

            int i = 0;
            while (ergebnis.next() == true) {
                userLoginArray[i] = this.decodeErgebnis(ergebnis);
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

    /**Kennungen müssen ja mittlerweile eindeutig sein. Diese Funktion für uLogin verwenden - da wird anschließend
     * abgefragt, ob Kennung zulässig oder nicht
     */
    public int leseZuKennung(String kennung) {
        int anzInArray = 0;
        try {

            String sql = "SELECT * from " + dbBundle.getSchemaAllgemein() + getTable()+" ul where "
                    + "ul.kennung=? ORDER BY ul.userLoginIdent;";

            PreparedStatement pstm1 = verbindung.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
            pstm1.setString(1, kennung);

            ResultSet ergebnis = pstm1.executeQuery();
            ergebnis.last();
            anzInArray = ergebnis.getRow();
            ergebnis.beforeFirst();

            userLoginArray = new EclUserLogin[anzInArray];

            int i = 0;
            while (ergebnis.next() == true) {
                userLoginArray[i] = this.decodeErgebnis(ergebnis);
                i++;
            }
            ergebnis.close();
            pstm1.close();

        } catch (Exception e) {
            CaBug.drucke("DbUserLogin.leseZuKennung 001");
            System.err.println(" " + e.getMessage());
        }

        return (anzInArray);
    }

    /**Füllt Ergebnis-Array!*/
    public int pruefeKennungVorhanden(String kennung) {
        int anzInArray = 0;
        try {

            String sql = "SELECT * from " + dbBundle.getSchemaAllgemein() + getTable()+" ul where "
                    + "ul.kennung=? ORDER BY ul.userLoginIdent;";

            PreparedStatement pstm1 = verbindung.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
            pstm1.setString(1, kennung);

            ResultSet ergebnis = pstm1.executeQuery();
            ergebnis.last();
            anzInArray = ergebnis.getRow();
            ergebnis.beforeFirst();

            userLoginArray = new EclUserLogin[anzInArray];

            int i = 0;
            while (ergebnis.next() == true) {
                userLoginArray[i] = this.decodeErgebnis(ergebnis);
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

    public int leseZuIdent(int pIdent) {
        int anzInArray = 0;
        try {

            String sql = "SELECT * from " + dbBundle.getSchemaAllgemein() + getTable()+" ul where "
                    + "ul.userLoginIdent=? ORDER BY ul.userLoginIdent;";

            PreparedStatement pstm1 = verbindung.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
            pstm1.setInt(1, pIdent);

            ResultSet ergebnis = pstm1.executeQuery();
            ergebnis.last();
            anzInArray = ergebnis.getRow();
            ergebnis.beforeFirst();

            userLoginArray = new EclUserLogin[anzInArray];

            int i = 0;
            while (ergebnis.next() == true) {
                userLoginArray[i] = this.decodeErgebnis(ergebnis);
                i++;
            }
            ergebnis.close();
            pstm1.close();

        } catch (Exception e) {
            CaBug.drucke("DbUserLogin.leseZuIdent 001");
            System.err.println(" " + e.getMessage());
        }

        return (anzInArray);
    }

    public int lese_all(boolean pMitMandant) {
        int anzInArray = 0;
        try {

            String sql = "SELECT * from " + dbBundle.getSchemaAllgemein() + getTable()+" ul WHERE " + "ul.mandant=? "
                    + "ORDER BY ul.userLoginIdent;";

            PreparedStatement pstm1 = verbindung.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
            if (pMitMandant) {
                if (profileVerarbeiten) {
                    pstm1.setInt(1, 1);
                }
                else {
                    pstm1.setInt(1, dbBundle.clGlobalVar.mandant);
               }
            } else {
                pstm1.setInt(1, 0);
            }

            ResultSet ergebnis = pstm1.executeQuery();
            ergebnis.last();
            anzInArray = ergebnis.getRow();
            ergebnis.beforeFirst();

            userLoginArray = new EclUserLogin[anzInArray];

            int i = 0;
            while (ergebnis.next() == true) {
                userLoginArray[i] = this.decodeErgebnis(ergebnis);
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

    public int lese_allZuInsti(int pInstiIdent, int pMandant) {
        int anzInArray = 0;
        try {

            String sql = "SELECT * from " + dbBundle.getSchemaAllgemein() + getTable()+" ul WHERE "
                    + "ul.gehoertZuInsti=? ";
            if (pMandant != 0) {
                sql = sql + " AND ul.mandant=? ";
            }
            sql = sql + "ORDER BY ul.userLoginIdent;";

            PreparedStatement pstm1 = verbindung.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
            pstm1.setInt(1, pInstiIdent);
            if (pMandant != 0) {
                pstm1.setInt(2, pMandant);
            }

            ResultSet ergebnis = pstm1.executeQuery();
            ergebnis.last();
            anzInArray = ergebnis.getRow();
            ergebnis.beforeFirst();

            userLoginArray = new EclUserLogin[anzInArray];

            int i = 0;
            while (ergebnis.next() == true) {
                userLoginArray[i] = this.decodeErgebnis(ergebnis);
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

    public int lese_allZuInsti() {
        int anzInArray = 0;
        try {

            String sql = "SELECT * from " + dbBundle.getSchemaAllgemein() + getTable()+" ul WHERE "
                    + "ul.gehoertZuInsti>0 ";
            sql = sql + "ORDER BY ul.userLoginIdent;";

            PreparedStatement pstm1 = verbindung.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);

            ResultSet ergebnis = pstm1.executeQuery();
            ergebnis.last();
            anzInArray = ergebnis.getRow();
            ergebnis.beforeFirst();

            userLoginArray = new EclUserLogin[anzInArray];

            int i = 0;
            while (ergebnis.next() == true) {
                userLoginArray[i] = this.decodeErgebnis(ergebnis);
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

    /**Nur für Profile: klasse ist in email gespeichert, nur Mandantenabhängige Profile*/
    public int lese_alleProfileZuKlasse(String pKlasse) {
        int anzInArray = 0;
        try {

            String sql = "SELECT * from " + dbBundle.getSchemaAllgemein() + getTable()+" ul WHERE "
                    + "ul.email=? ";
            sql = sql + " AND ul.mandant=1 ";
            sql = sql + "ORDER BY ul.userLoginIdent;";

            PreparedStatement pstm1 = verbindung.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
            pstm1.setString(1, pKlasse);

            ResultSet ergebnis = pstm1.executeQuery();
            ergebnis.last();
            anzInArray = ergebnis.getRow();
            ergebnis.beforeFirst();

            userLoginArray = new EclUserLogin[anzInArray];

            int i = 0;
            while (ergebnis.next() == true) {
                userLoginArray[i] = this.decodeErgebnis(ergebnis);
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

    /**Nur für Profile: klasse ist in email gespeichert, nur Mandantenabhängige Profile*/
    public int lese_alleProfileMandantenUnabhaenig() {
        int anzInArray = 0;
        try {

            String sql = "SELECT * from " + dbBundle.getSchemaAllgemein() + getTable()+" ul WHERE ";
             sql = sql + " ul.mandant=0 ";
            sql = sql + "ORDER BY ul.userLoginIdent;";

            PreparedStatement pstm1 = verbindung.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);

            ResultSet ergebnis = pstm1.executeQuery();
            ergebnis.last();
            anzInArray = ergebnis.getRow();
            ergebnis.beforeFirst();

            userLoginArray = new EclUserLogin[anzInArray];

            int i = 0;
            while (ergebnis.next() == true) {
                userLoginArray[i] = this.decodeErgebnis(ergebnis);
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

    /**Update einer Willenserklärung. Versionsnummer wird um 1 hochgezählt
     */
    public int update(EclUserLogin lUserLogin) {
        lUserLogin.db_version++;
        try {

            String sql = "UPDATE " + dbBundle.getSchemaAllgemein() + getTable()+" SET "
                    + "mandant=?, userLoginIdent=?, db_version=?, "
                    + "kennung=?, passwort=?, name=?, email=?, gehoertZuInsti=?, "
                    + "berechtigungen0=?, berechtigungen1=?, berechtigungen2=?, berechtigungen3=?, berechtigungen4=?, "
                    + "berechtigungen5=?, berechtigungen6=?, berechtigungen7=?, berechtigungen8=?, berechtigungen9=?, "
                    + "berechtigungen10=?, berechtigungen11=?, berechtigungen12=?, berechtigungen13=?, berechtigungen14=?, "
                    + "berechtigungen15=?, berechtigungen16=?, berechtigungen17=?, berechtigungen18=?, berechtigungen19=?, "
                    + "neuesPasswortErforderlich=?, kennungGesperrtDurchSoftware=?, kennungGesperrtManuell=?, trivialPasswortZulaessig=?, gruppenKennungHV=?, passwortZuletztGeaendertAm=?, altesPasswort1=?, altesPasswort2=?, altesPasswort3=? "
                    + "WHERE " + "userLoginIdent=? AND " + "db_version=? AND mandant=?";

            PreparedStatement pstm1 = verbindung.prepareStatement(sql);
            fuellePreparedStatementKomplett(pstm1, 1, lUserLogin);
            pstm1.setInt(anzfelder + 1, lUserLogin.userLoginIdent);
            pstm1.setLong(anzfelder + 2, lUserLogin.db_version - 1);
            pstm1.setLong(anzfelder + 3, lUserLogin.mandant);

            int ergebnis1 = pstm1.executeUpdate();
            pstm1.close();
            if (ergebnis1 == 0) {
                return (CaFehler.pfXyWurdeVonAnderemBenutzerVeraendert);
            }
        } catch (Exception e1) {
            CaBug.drucke("DbUserLogin.update 001");
            System.err.println(" " + e1.getMessage());
            return (CaFehler.pfdXyBereitsVorhanden);

        }

        BvReload bvReload = new BvReload(dbBundle);
        bvReload.setReloadUserLogin();

        return (1);
    }

    /**Update einer Willenserklärung. Versionsnummer wird um 1 hochgezählt
     */
    public int updateUnabhaenigVonVersion(EclUserLogin lUserLogin) {
        lUserLogin.db_version++;
        try {

            String sql = "UPDATE " + dbBundle.getSchemaAllgemein() + getTable()+" SET "
                    + "mandant=?, userLoginIdent=?, db_version=?, "
                    + "kennung=?, passwort=?, name=?, email=?, gehoertZuInsti=?, "
                    + "berechtigungen0=?, berechtigungen1=?, berechtigungen2=?, berechtigungen3=?, berechtigungen4=?, "
                    + "berechtigungen5=?, berechtigungen6=?, berechtigungen7=?, berechtigungen8=?, berechtigungen9=?, "
                    + "berechtigungen10=?, berechtigungen11=?, berechtigungen12=?, berechtigungen13=?, berechtigungen14=?, "
                    + "berechtigungen15=?, berechtigungen16=?, berechtigungen17=?, berechtigungen18=?, berechtigungen19=?, "
                    + "neuesPasswortErforderlich=?, kennungGesperrtDurchSoftware=?, kennungGesperrtManuell=?, trivialPasswortZulaessig=?, gruppenKennungHV=?, passwortZuletztGeaendertAm=?, altesPasswort1=?, altesPasswort2=?, altesPasswort3=? "
                    + "WHERE " + "userLoginIdent=? AND " + " mandant=?";

            PreparedStatement pstm1 = verbindung.prepareStatement(sql);
            fuellePreparedStatementKomplett(pstm1, 1, lUserLogin);
            pstm1.setInt(anzfelder + 1, lUserLogin.userLoginIdent);
            pstm1.setLong(anzfelder + 2, lUserLogin.mandant);

            int ergebnis1 = pstm1.executeUpdate();
            pstm1.close();
            if (ergebnis1 == 0) {
                return (CaFehler.pfXyWurdeVonAnderemBenutzerVeraendert);
            }
        } catch (Exception e1) {
            CaBug.drucke("DbUserLogin.update 001");
            System.err.println(" " + e1.getMessage());
            return (CaFehler.pfdXyBereitsVorhanden);

        }

        BvReload bvReload = new BvReload(dbBundle);
        bvReload.setReloadUserLogin();

        return (1);
    }

    
    
}
