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
import java.util.List;

import de.meetingapps.meetingportal.meetComAllg.CaBug;
import de.meetingapps.meetingportal.meetComAllg.CaFehler;
import de.meetingapps.meetingportal.meetComAllg.CaPasswortVerschluesseln;
import de.meetingapps.meetingportal.meetComBl.BlNummernformBasis;
import de.meetingapps.meetingportal.meetComEntities.EclLoginDaten;
import de.meetingapps.meetingportal.meetComEntities.EclLoginDatenDemo;
import de.meetingapps.meetingportal.meetComKonst.KonstHinweisWeitere;

public class DbLoginDaten  extends DbRootExecute {

    private int logDrucken = 3;

    private Connection verbindung = null;
    // private DbBasis dbBasis=null;
    private DbBundle dbBundle = null;

    private EclLoginDaten ergebnisArray[] = null;

    private boolean dbGlobal = false;

    private String dbSchema() {
        if (dbGlobal) {
            return dbBundle.getSchemaAllgemein();
        } else {
            return dbBundle.getSchemaMandant();
        }

    }

    /************************* Initialisierung ***************************/
    public DbLoginDaten(DbBundle pDbBundle, boolean pGlobal) {
        /* Verbindung in lokale Daten eintragen */
        if (pDbBundle == null) {
            CaBug.drucke("DbLoginDaten.init 001 - dbBundle nicht initialisiert");
            return;
        }
        if (pDbBundle.dbBasis == null) {
            CaBug.drucke("DbLoginDaten.init 002 - dbBasis nicht initialisiert");
            return;
        }

        // dbBasis=pDbBundle.dbBasis;
        verbindung = pDbBundle.dbBasis.verbindung;
        dbBundle = pDbBundle;

        dbGlobal = pGlobal;
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
    public EclLoginDaten[] ergebnis() {
        return ergebnisArray;
    }

    /**********
     * Liefert pN-tes Element des Ergebnisses der read*Methoden************** pN
     * geht von 0 bis anzErgebnis-1
     */
    public EclLoginDaten ergebnisPosition(int pN) {
        if (ergebnisArray == null) {
            CaBug.drucke("DbLoginDaten.ergebnisPosition 001");
            return null;
        }
        if (pN < 0) {
            CaBug.drucke("DbLoginDaten.ergebnisPosition 002");
            return null;
        }
        if (pN >= ergebnisArray.length) {
            CaBug.drucke("DbLoginDaten.ergebnisPosition 003");
            return null;
        }
        return ergebnisArray[pN];
    }

    public int createTable() {
        int rc = 0;
        DbLowLevel lDbLowLevel = new DbLowLevel(dbBundle);
        rc = lDbLowLevel.createTable("CREATE TABLE " + dbSchema() + "tbl_logindaten ( "
                + "`db_version` bigint(20) DEFAULT 0, " + "`ident` int(11) NOT NULL AUTO_INCREMENT, "
                + "`loginKennung` char(20) NOT NULL DEFAULT '', " + "`kennungArt` int(11) NOT NULL DEFAULT 0, "
                + "`letzterLoginAufServer` int(11) NOT NULL DEFAULT 0, " + "`zeitstempel` bigint(20) DEFAULT '0', "
                + "`aktienregisterIdent` int(11) NOT NULL, " + "`personenNatJurIdent` int(11) NOT NULL, "
                + "`meldeIdent` int(11) NOT NULL, " + "`loginKennungAlternativ` char(100) NOT NULL DEFAULT '', " +

                "`passwortVerschluesselt` char(64) DEFAULT NULL, " + "`passwortInitial` char(64) DEFAULT NULL, " +

                "`passwortAlternativVerschluesselt` char(64) DEFAULT NULL, "
                + "`passwortAlternativInitial` char(64) DEFAULT NULL, " +

                "`letzterLoginZeit` char(19) DEFAULT NULL, " + "`loginZaehler` int(11) NULL DEFAULT 0, " +

                "`anmeldenUnzulaessig` int(11) NULL DEFAULT 0, "
                + "`dauerhafteRegistrierungUnzulaessig` int(11) NULL DEFAULT 0, "
                + "`berechtigungPortal` bigint(20) DEFAULT '0', " +

                "`hinweisAktionaersPortalBestaetigt` int(11) DEFAULT NULL, "
                + "`hinweisHVPortalBestaetigt` int(11) DEFAULT NULL, "
                + "`hinweisWeitereBestaetigt` int(11) DEFAULT '0', "
                + "`eVersandRegistrierung` int(11) DEFAULT NULL, "
                + "`eVersandRegistrierungErstZeitpunkt` char(19) DEFAULT NULL, "
                + "`eigenesPasswort` int(11) DEFAULT NULL, " + "`passwortVergessenLink` char(30) DEFAULT NULL, "
                + "`passwortVergessenZeitpunkt` char(19) DEFAULT NULL, "
                + "`kommunikationssprache` int(11) DEFAULT NULL, " + "`eMailFuerVersand` char(100) DEFAULT NULL, "
                + "`emailBestaetigt` int(11) DEFAULT NULL, " + "`emailBestaetigenLink` char(30) DEFAULT NULL, "
                + "`eMail2FuerVersand` char(100) DEFAULT NULL, " 
                + "`email2Bestaetigt` int(11) DEFAULT NULL, "
                + "`email2BestaetigenLink` char(30) DEFAULT NULL, " 
                + "`zweiFaktorAuthentifizierungAktiv` int(11) DEFAULT '0', "
                + "`konferenzTestDurchgefuehrt` int(11) DEFAULT '0', "
                + "`konferenzTestAblauf` int(11) DEFAULT '0', "
                + "`konferenzSprechen` int(11) DEFAULT '0', "
                + "`registrierungAktionaersPortalErfolgt` int(11) DEFAULT '0', "
                + "`registrierungAktionaersPortalErfolgtZeitstempel` BIGINT(20) DEFAULT '0', "
                + "`registrierungMitgliederPortalErfolgt` int(11) DEFAULT '0', "
                + "`registrierungMitgliederPortalErfolgtZeitstempel` BIGINT(20) DEFAULT '0', "

                + "PRIMARY KEY (`loginKennung`), " + "KEY `IDX_loginKennungAlternativ` (`loginKennungAlternativ`), "
                + "KEY `IDX_ident` (`ident`), " + "KEY `IDX_aktienregisterIdent` (`aktienregisterIdent`), "
                + "KEY `IDX_personenNatJurIdent` (`personenNatJurIdent`), " + "KEY `IDX_meldeIdent` (`meldeIdent`), "
                + "KEY `IDX_passwortVergessenLink` (`passwortVergessenLink`) " + ") ");
        return rc;
    }

    /**************************
     * deleteAll Löschen aller Datensätze eines Mandanten
     ****************/
    public int deleteAll() {
        return dbBundle.dbLowLevel.deleteAlle("DELETE FROM " + dbSchema() + "tbl_logindaten;");
    }

    /**********
     * dekodiert die aktuelle Position aus ergebnis und gibt dieses zurück
     ******/
    EclLoginDaten decodeErgebnis(ResultSet pErgebnis) {
        return decodeErgebnisIntern(pErgebnis, "lo");
    }
    
    private EclLoginDaten decodeErgebnisIntern(ResultSet pErgebnis, String pPraefix) {

        EclLoginDaten lEclReturn = new EclLoginDaten();

        try {
            lEclReturn.db_version = pErgebnis.getLong(pPraefix+".db_version");
            lEclReturn.ident = pErgebnis.getInt(pPraefix+".ident");
            lEclReturn.loginKennung = pErgebnis.getString(pPraefix+".loginKennung");
            lEclReturn.loginKennungExtern=lEclReturn.loginKennung;
            lEclReturn.kennungArt = pErgebnis.getInt(pPraefix+".kennungArt");
            lEclReturn.letzterLoginAufServer = pErgebnis.getInt(pPraefix+".letzterLoginAufServer");
            lEclReturn.zeitstempel = pErgebnis.getLong(pPraefix+".zeitstempel");
            lEclReturn.aktienregisterIdent = pErgebnis.getInt(pPraefix+".aktienregisterIdent");
            lEclReturn.personenNatJurIdent = pErgebnis.getInt(pPraefix+".personenNatJurIdent");
            lEclReturn.meldeIdent = pErgebnis.getInt(pPraefix+".meldeIdent");
            lEclReturn.loginKennungAlternativ = pErgebnis.getString(pPraefix+".loginKennungAlternativ");

            lEclReturn.passwortVerschluesselt = pErgebnis.getString(pPraefix+".passwortVerschluesselt");
            lEclReturn.passwortInitial = pErgebnis.getString(pPraefix+".passwortInitial");

            lEclReturn.passwortAlternativVerschluesselt = pErgebnis.getString(pPraefix+".passwortAlternativVerschluesselt");
            lEclReturn.passwortAlternativInitial = pErgebnis.getString(pPraefix+".passwortAlternativInitial");

            lEclReturn.letzterLoginZeit = pErgebnis.getString(pPraefix+".letzterLoginZeit");
            lEclReturn.loginZaehler = pErgebnis.getInt(pPraefix+".loginZaehler");

            lEclReturn.anmeldenUnzulaessig = pErgebnis.getInt(pPraefix+".anmeldenUnzulaessig");
            lEclReturn.dauerhafteRegistrierungUnzulaessig = pErgebnis.getInt(pPraefix+".dauerhafteRegistrierungUnzulaessig");
            lEclReturn.berechtigungPortal = pErgebnis.getLong(pPraefix+".berechtigungPortal");

            lEclReturn.hinweisAktionaersPortalBestaetigt = pErgebnis.getInt(pPraefix+".hinweisAktionaersPortalBestaetigt");
            lEclReturn.hinweisHVPortalBestaetigt = pErgebnis.getInt(pPraefix+".hinweisHVPortalBestaetigt");
            lEclReturn.hinweisWeitereBestaetigt = pErgebnis.getInt(pPraefix+".hinweisWeitereBestaetigt");
            lEclReturn.eVersandRegistrierung = pErgebnis.getInt(pPraefix+".eVersandRegistrierung");
            lEclReturn.eVersandRegistrierungErstZeitpunkt = pErgebnis
                    .getString(pPraefix+".eVersandRegistrierungErstZeitpunkt");
            lEclReturn.eigenesPasswort = pErgebnis.getInt(pPraefix+".eigenesPasswort");

            lEclReturn.passwortVergessenLink = pErgebnis.getString(pPraefix+".passwortVergessenLink");
            lEclReturn.passwortVergessenZeitpunkt = pErgebnis.getString(pPraefix+".passwortVergessenZeitpunkt");
            lEclReturn.kommunikationssprache = pErgebnis.getInt(pPraefix+".kommunikationssprache");

            lEclReturn.eMailFuerVersand = pErgebnis.getString(pPraefix+".eMailFuerVersand");
            lEclReturn.emailBestaetigt = pErgebnis.getInt(pPraefix+".emailBestaetigt");
            lEclReturn.emailBestaetigenLink = pErgebnis.getString(pPraefix+".emailBestaetigenLink");

            lEclReturn.eMail2FuerVersand = pErgebnis.getString(pPraefix+".eMail2FuerVersand");
            lEclReturn.email2Bestaetigt = pErgebnis.getInt(pPraefix+".email2Bestaetigt");
            lEclReturn.email2BestaetigenLink = pErgebnis.getString(pPraefix+".email2BestaetigenLink");
            lEclReturn.zweiFaktorAuthentifizierungAktiv = pErgebnis.getInt(pPraefix+".zweiFaktorAuthentifizierungAktiv");

            lEclReturn.konferenzTestDurchgefuehrt = pErgebnis.getInt(pPraefix+".konferenzTestDurchgefuehrt");
            lEclReturn.konferenzTestAblauf = pErgebnis.getInt(pPraefix+".konferenzTestAblauf");
            lEclReturn.konferenzSprechen = pErgebnis.getInt(pPraefix+".konferenzSprechen");

            lEclReturn.registrierungAktionaersPortalErfolgt = pErgebnis.getInt(pPraefix+".registrierungAktionaersPortalErfolgt");
            lEclReturn.registrierungAktionaersPortalErfolgtZeitstempel = pErgebnis.getLong(pPraefix+".registrierungAktionaersPortalErfolgtZeitstempel");
            lEclReturn.registrierungMitgliederPortalErfolgt = pErgebnis.getInt(pPraefix+".registrierungMitgliederPortalErfolgt");
            lEclReturn.registrierungMitgliederPortalErfolgtZeitstempel = pErgebnis.getLong(pPraefix+".registrierungMitgliederPortalErfolgtZeitstempel");
           
        } catch (Exception e) {
            CaBug.drucke("001");
            System.err.println(" " + e.getMessage());
        }

        return lEclReturn;
    }

    EclLoginDaten decodeErgebnis1(ResultSet pErgebnis) {
        return decodeErgebnisIntern(pErgebnis, "lo1");
    }


    /*********************
     * Fuellen Prepared Statement mit allen Feldern, beginnend bei
     * offset.***************** Kann sowohl für Insert, als auch für update
     * verwendet werden.
     * 
     * offset= Startposition des ersten Feldes (also z.B. 1)
     */
    private int anzfelder = 39; /* Anpassen auf Anzahl der Felder pro Datensatz */

    private void fuellePreparedStatementKomplett(PreparedStatement pPStm, int pOffset, EclLoginDaten pEcl) {

        int startOffset = pOffset; /* Nur erforderlich zum Überprüfen von Programmierfehlern - Feldanzahl */

        try {
            pPStm.setLong(pOffset, pEcl.db_version);
            pOffset++;
            pPStm.setString(pOffset, pEcl.loginKennung);
            pOffset++;
            pPStm.setInt(pOffset, pEcl.kennungArt);
            pOffset++;
            /*
             * letzterLoginAufServer und zeitstempel wird bewußt übersprungen. Hintergrund:
             * > ein User ist in den Einstellungen und Speichert > gleichzeitig loggt sich
             * ein anderer User ein und updated letzterLoginAufServer Dieser darf durch das
             * Einstellungs-Speichern nicht überschrieben werden.
             */
            // pPStm.setInt(pOffset, pEcl.letzterLoginAufServer);pOffset++;
            // pPStm.setLong(pOffset, pEcl.zeitstempel);pOffset++;
            pPStm.setInt(pOffset, pEcl.aktienregisterIdent);
            pOffset++;
            pPStm.setInt(pOffset, pEcl.personenNatJurIdent);
            pOffset++;
            pPStm.setInt(pOffset, pEcl.meldeIdent);
            pOffset++;
            pPStm.setString(pOffset, pEcl.loginKennungAlternativ);
            pOffset++;

            pPStm.setString(pOffset, pEcl.passwortVerschluesselt);
            pOffset++;
            pPStm.setString(pOffset, pEcl.passwortInitial);
            pOffset++;

            pPStm.setString(pOffset, pEcl.passwortAlternativVerschluesselt);
            pOffset++;
            pPStm.setString(pOffset, pEcl.passwortAlternativInitial);
            pOffset++;

            pPStm.setString(pOffset, pEcl.letzterLoginZeit);
            pOffset++;
            pPStm.setInt(pOffset, pEcl.loginZaehler);
            pOffset++;

            pPStm.setInt(pOffset, pEcl.anmeldenUnzulaessig);
            pOffset++;
            pPStm.setInt(pOffset, pEcl.dauerhafteRegistrierungUnzulaessig);
            pOffset++;
            pPStm.setLong(pOffset, pEcl.berechtigungPortal);
            pOffset++;

            pPStm.setInt(pOffset, pEcl.hinweisAktionaersPortalBestaetigt);
            pOffset++;
            pPStm.setInt(pOffset, pEcl.hinweisHVPortalBestaetigt);
            pOffset++;
            pPStm.setInt(pOffset, pEcl.hinweisWeitereBestaetigt);
            pOffset++;
            pPStm.setInt(pOffset, pEcl.eVersandRegistrierung);
            pOffset++;
            pPStm.setString(pOffset, pEcl.eVersandRegistrierungErstZeitpunkt);
            pOffset++;
            pPStm.setInt(pOffset, pEcl.eigenesPasswort);
            pOffset++;

            pPStm.setString(pOffset, pEcl.passwortVergessenLink);
            pOffset++;
            pPStm.setString(pOffset, pEcl.passwortVergessenZeitpunkt);
            pOffset++;
            pPStm.setInt(pOffset, pEcl.kommunikationssprache);
            pOffset++;

            pPStm.setString(pOffset, pEcl.eMailFuerVersand);
            pOffset++;
            pPStm.setInt(pOffset, pEcl.emailBestaetigt);
            pOffset++;
            pPStm.setString(pOffset, pEcl.emailBestaetigenLink);
            pOffset++;

            pPStm.setString(pOffset, pEcl.eMail2FuerVersand);
            pOffset++;
            pPStm.setInt(pOffset, pEcl.email2Bestaetigt);
            pOffset++;
            pPStm.setString(pOffset, pEcl.email2BestaetigenLink);
            pOffset++;
            pPStm.setInt(pOffset, pEcl.zweiFaktorAuthentifizierungAktiv);
            pOffset++;

            pPStm.setInt(pOffset, pEcl.konferenzTestDurchgefuehrt);
            pOffset++;
            pPStm.setInt(pOffset, pEcl.konferenzTestAblauf);
            pOffset++;
            pPStm.setInt(pOffset, pEcl.konferenzSprechen);
            pOffset++;

            pPStm.setInt(pOffset, pEcl.registrierungAktionaersPortalErfolgt);
            pOffset++;
            pPStm.setLong(pOffset, pEcl.registrierungAktionaersPortalErfolgtZeitstempel);
            pOffset++;
            pPStm.setInt(pOffset, pEcl.registrierungMitgliederPortalErfolgt);
            pOffset++;
            pPStm.setLong(pOffset, pEcl.registrierungMitgliederPortalErfolgtZeitstempel);
            pOffset++;

            if (pOffset - startOffset != anzfelder) {
                /* Nur wg. Überprüfung auf Programmierfehler - alle Felder berücksichtigt? */
                CaBug.drucke("DbLoginDaten.fuellePreparedStatementKomplett 002");
            }

        } catch (SQLException e) {
            CaBug.drucke("DbLoginDaten.fuellePreparedStatementKomplett 001");
            e.printStackTrace();
        }

    }

    private String liefereInsertSql() {
        //@formatter:off
        return 
                "INSERT INTO " + dbSchema() + "tbl_logindaten " + "("
                + "db_version, loginKennung, kennungArt, aktienregisterIdent, personenNatJurIdent, meldeIdent, loginKennungAlternativ, "
                + "passwortVerschluesselt, passwortInitial, passwortAlternativVerschluesselt, passwortAlternativInitial, "
                + "letzterLoginZeit, loginZaehler, anmeldenUnzulaessig, dauerhafteRegistrierungUnzulaessig, berechtigungPortal, "
                + "hinweisAktionaersPortalBestaetigt, hinweisHVPortalBestaetigt, hinweisWeitereBestaetigt, eVersandRegistrierung, eVersandRegistrierungErstZeitpunkt, eigenesPasswort, "
                + "passwortVergessenLink, passwortVergessenZeitpunkt, kommunikationssprache, "
                + "eMailFuerVersand, emailBestaetigt, emailBestaetigenLink, "
                + "eMail2FuerVersand, email2Bestaetigt, email2BestaetigenLink, zweiFaktorAuthentifizierungAktiv, konferenzTestDurchgefuehrt, konferenzTestAblauf, "
                + "konferenzSprechen, registrierungAktionaersPortalErfolgt, registrierungAktionaersPortalErfolgtZeitstempel, registrierungMitgliederPortalErfolgt, registrierungMitgliederPortalErfolgtZeitstempel " + ")" + "VALUES ("
                + "?, ?, ?, ?, ?, ?, ?, " 
                + "?, ?, ?, ?, " 
                + "?, ?, ?, ?, ?, " 
                + "?, ?, ?, ?, ?, ?, " 
                + "?, ?, ?, "
                + "?, ?, ?, " 
                + "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? " + ")";
        //@formatter:on
    }

    
    /**
     * Insert
     * 
     * Achtung: letzterLoginAufServer wird nicht ge-Inserted!
     * 
     * Returnwert: =1 => Insert erfolgreich ansonsten: Fehler
     */
    public int insert(EclLoginDaten pEcl) {

        if (pEcl.loginKennungAlternativ.isEmpty() && dbBundle.param.paramPortal.alternativeLoginKennung!=0) {
            pEcl.loginKennungAlternativ=dbBundle.dbEindeutigeKennung.getNextFree();
        }
        
        int erg = 0;

        try {

            /* Felder Neuanlage füllen */
            String lSql =liefereInsertSql();
            PreparedStatement lPStm = verbindung.prepareStatement(lSql, ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);

            fuellePreparedStatementKomplett(lPStm, 1, pEcl);

            erg = executeUpdate(lPStm);
            lPStm.close();
        } catch (Exception e2) {
            CaBug.drucke("DbLoginDaten.insert 001");
            System.err.println(" " + e2.getMessage());
        }

        if (erg == 0) {/* Fehler beim Einfügen - d.h. primaryKey bereits vorhanden */
            return (-1);
        }

        return (1);
    }

    private int readRumpf(PreparedStatement lPStm) {
        int anzInArray = 0;

        if (dbSchema().equalsIgnoreCase("db_mc000j0000.")) {
            CaBug.drucke("001 - Mandant=db_mc000j0000");
            return -1;
        }
        try {

            ResultSet lErgebnis = executeQuery(lPStm);
            lErgebnis.last();
            anzInArray = lErgebnis.getRow();
            lErgebnis.beforeFirst();

            ergebnisArray = new EclLoginDaten[anzInArray];

            int i = 0;
            while (lErgebnis.next() == true) {
                ergebnisArray[i] = this.decodeErgebnis(lErgebnis);
                i++;
            }
            lErgebnis.close();
            lPStm.close();

        } catch (Exception e) {
            CaBug.drucke("003");
            System.err.println(" " + e.getMessage());
            return (-1);
        }
        return (anzInArray);
    }

    /**
     * pLoginKennung muß vollständig aufgearbeitet sein, d.h. bei
     * Aktienregisterkennung mit anschließender 0 oder 1. Return-Wert = Anzahl der
     * gefundenen Sätze (oder Fehlermeldung <0)
     */
    public int read_loginKennung(String pLoginKennung) {
        try {
            String lSql = "SELECT * from " + dbSchema() + "tbl_logindaten lo where " + "lo.loginKennung=?; ";
            PreparedStatement lPStm = verbindung.prepareStatement(lSql, ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
            String hLoginKennung = BlNummernformBasis.aufbereitenFuerDatenbankZugriff(pLoginKennung, dbBundle);
            CaBug.druckeLog("hLoginKennung=" + hLoginKennung, logDrucken, 10);
            lPStm.setString(1, hLoginKennung);
            return readRumpf(lPStm);
        } catch (Exception e) {
            CaBug.drucke("003");
            System.err.println(" " + e.getMessage());
            return (-1);
        }
    }

    /** Return-Wert = Anzahl der gefundenen Sätze (oder Fehlermeldung <0) */
    public int read_loginKennungAlternative(String pLoginKennung) {
        try {
            String lSql = "SELECT * from " + dbSchema() + "tbl_logindaten lo where " + "lo.loginKennungAlternativ=?; ";
            PreparedStatement lPStm = verbindung.prepareStatement(lSql, ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
            lPStm.setString(1, pLoginKennung);
            return readRumpf(lPStm);
        } catch (Exception e) {
            CaBug.drucke("003");
            System.err.println(" " + e.getMessage());
            return (-1);
        }
    }

    /** Return-Wert = Anzahl der gefundenen Sätze (oder Fehlermeldung <0) */
    public int read_aktienregisterIdent(int pAktienregisterIdent) {
        try {
            String lSql = "SELECT * from " + dbSchema() + "tbl_logindaten lo where " + "lo.aktienregisterIdent=?; ";
            PreparedStatement lPStm = verbindung.prepareStatement(lSql, ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
            lPStm.setInt(1, pAktienregisterIdent);
            return readRumpf(lPStm);
        } catch (Exception e) {
            CaBug.drucke("003");
            System.err.println(" " + e.getMessage());
            return (-1);
        }
    }

    /** Return-Wert = Anzahl der gefundenen Sätze (oder Fehlermeldung <0) */
    public int read_personNatJurIdent(int pNatJurIdent) {
        try {
            String lSql = "SELECT * from " + dbSchema() + "tbl_logindaten lo where " + "lo.personenNatJurIdent=?; ";
            PreparedStatement lPStm = verbindung.prepareStatement(lSql, ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
            lPStm.setInt(1, pNatJurIdent);
            return readRumpf(lPStm);
        } catch (Exception e) {
            CaBug.drucke("003");
            System.err.println(" " + e.getMessage());
            return (-1);
        }
    }

    /** Return-Wert = Anzahl der gefundenen Sätze (oder Fehlermeldung <0) */
    public int read_ident(int pIdent) {
        try {
            String lSql = "SELECT * from " + dbSchema() + "tbl_logindaten lo where " + "lo.ident=?; ";
            PreparedStatement lPStm = verbindung.prepareStatement(lSql, ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
            lPStm.setInt(1, pIdent);
            return readRumpf(lPStm);
        } catch (Exception e) {
            CaBug.drucke("003");
            System.err.println(" " + e.getMessage());
            return (-1);
        }
    }

    /** Return-Wert = Anzahl der gefundenen Sätze (oder Fehlermeldung <0) */
    public int read_all() {
        try {
            String lSql = "SELECT * from " + dbSchema() + "tbl_logindaten lo;  ";
            PreparedStatement lPStm = verbindung.prepareStatement(lSql, ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
             return readRumpf(lPStm);
        } catch (Exception e) {
            CaBug.drucke("003");
            System.err.println(" " + e.getMessage());
            return (-1);
        }
    }

   /** Return-Wert = Anzahl der gefundenen Sätze (oder Fehlermeldung <0) */
    public int read_emailBestaetigenLink(String pEmailBestaetigenLink) {
        try {
            String lSql = "SELECT * from " + dbSchema() + "tbl_logindaten lo where " + "lo.emailBestaetigenLink=?; ";
            PreparedStatement lPStm = verbindung.prepareStatement(lSql, ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
            lPStm.setString(1, pEmailBestaetigenLink);
            return readRumpf(lPStm);
        } catch (Exception e) {
            CaBug.drucke("003");
            System.err.println(" " + e.getMessage());
            return (-1);
        }
    }

    /** Return-Wert = Anzahl der gefundenen Sätze (oder Fehlermeldung <0) */
    public int read_email2BestaetigenLink(String pEmail2BestaetigenLink) {
        try {
            String lSql = "SELECT * from " + dbSchema() + "tbl_logindaten lo where " + "lo.email2BestaetigenLink=?; ";
            PreparedStatement lPStm = verbindung.prepareStatement(lSql, ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
            lPStm.setString(1, pEmail2BestaetigenLink);
            return readRumpf(lPStm);
        } catch (Exception e) {
            CaBug.drucke("003");
            System.err.println(" " + e.getMessage());
            return (-1);
        }
    }

    /** Return-Wert = Anzahl der gefundenen Sätze (oder Fehlermeldung <0) */
    public int read_sucheEmail(String pEmail) {
        try {
            String lSql = "SELECT * from " + dbSchema() + "tbl_logindaten lo where " + "lo.eMailFuerVersand like ?; ";
            PreparedStatement lPStm = verbindung.prepareStatement(lSql, ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
            lPStm.setString(1, pEmail);
            return readRumpf(lPStm);
        } catch (Exception e) {
            CaBug.drucke("003");
            System.err.println(" " + e.getMessage());
            return (-1);
        }
    }


    /**Liefert Ergebnis, aus SUM, COUNT o.ä.*/
    long readInternErgebnis(PreparedStatement pPStm) {
        long ergebnis = 0;
        try {
             ResultSet lErgebnis = executeQuery(pPStm);

            lErgebnis.last();
            lErgebnis.beforeFirst();

            while (lErgebnis.next() == true) {
                ergebnis = lErgebnis.getLong(1);
            }

            lErgebnis.close();
            pPStm.close();

        } catch (Exception e) {
            CaBug.drucke("003");
            CaBug.out(" " + e.getMessage());
            return (-1);
        }
        return ergebnis;

    }

    public long read_anzNutzerAktionaersportal(long pTimestamp) {
        long ergebnis=0;
        String sql = "SELECT COUNT(*) from " + dbSchema() + "tbl_logindaten lo "
                + "where lo.registrierungAktionaersPortalErfolgt>0 AND lo.registrierungAktionaersPortalErfolgtZeitstempel<=?";
        try {
            PreparedStatement pstm = verbindung.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            pstm.setLong(1, pTimestamp);
            ergebnis=readInternErgebnis(pstm);
        } catch (SQLException e) {
            CaBug.drucke("001");
            System.err.println(" " + e.getMessage());
        }
        return ergebnis;
    }
    
    public long read_anzNutzerMitgliederportal(long pTimestamp) {
        long ergebnis=0;
        String sql = "SELECT COUNT(*) from " + dbSchema() + "tbl_logindaten lo "
                + "where lo.registrierungMitgliederPortalErfolgt>0 AND lo.registrierungMitgliederPortalErfolgtZeitstempel<=?";
        try {
            PreparedStatement pstm = verbindung.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            pstm.setLong(1, pTimestamp);
            ergebnis=readInternErgebnis(pstm);
        } catch (SQLException e) {
            CaBug.drucke("001");
            System.err.println(" " + e.getMessage());
        }
        return ergebnis;
    }

    public long read_anzNutzerInsgesamtportal(long pTimestamp) {
        long ergebnis=0;
        String sql = "SELECT COUNT(*) from " + dbSchema() + "tbl_logindaten lo "
                + "where (lo.registrierungMitgliederPortalErfolgt>0 AND lo.registrierungMitgliederPortalErfolgtZeitstempel<=?)"
                + " OR "
                + "(lo.registrierungAktionaersPortalErfolgt>0 AND lo.registrierungAktionaersPortalErfolgtZeitstempel<=?)";
        try {
            PreparedStatement pstm = verbindung.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            pstm.setLong(1, pTimestamp);
            pstm.setLong(2, pTimestamp);
            ergebnis=readInternErgebnis(pstm);
        } catch (SQLException e) {
            CaBug.drucke("001");
            System.err.println(" " + e.getMessage());
        }
        return ergebnis;
    }
    
    public int read_anzNeuerEmail2() {

        int erg = 0;

        String sql = "SELECT count(*) FROM " + dbSchema() + "tbl_logindaten lo "
                + "LEFT JOIN " + dbSchema() + "tbl_aktienregister are ON lo.aktienregisterIdent = are.aktienregisterIdent "
                + "WHERE lo.eMail2FuerVersand != are.emailVersand";

        try (PreparedStatement pstmt = verbindung.prepareStatement(sql)) {
            try (ResultSet lErgebnis = pstmt.executeQuery()) {
                if (lErgebnis.next())
                    erg = lErgebnis.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return erg;
        }
        return erg;
    }
  

    
    /**
     * Update.
     * 
     * Achtung: letzterLoginAufServer/zeitstempel wird _nicht_ upgedated! Dafür
     * separate Funktion.
     * 
     * Returnwert: pfXyWurdeVonAnderemBenutzerVeraendert -1 => unbekannter Fehler 1
     * = Update wurde durchgeführt.
     * 
     */
    public int update(EclLoginDaten pEcl) {

        pEcl.db_version++;

        try {

            String lSql = "UPDATE " + dbSchema() + "tbl_logindaten SET "
                    + "db_version=?, loginKennung=?, kennungArt=?, aktienregisterIdent=?, personenNatJurIdent=?, meldeIdent=?, loginKennungAlternativ=?, "
                    + "passwortVerschluesselt=?, passwortInitial=?, passwortAlternativVerschluesselt=?, passwortAlternativInitial=?, "
                    + "letzterLoginZeit=?, loginZaehler=?, anmeldenUnzulaessig=?, dauerhafteRegistrierungUnzulaessig=?, berechtigungPortal=?, "
                    + "hinweisAktionaersPortalBestaetigt=?, hinweisHVPortalBestaetigt=?, hinweisWeitereBestaetigt=?, eVersandRegistrierung=?, eVersandRegistrierungErstZeitpunkt=?, eigenesPasswort=?, "
                    + "passwortVergessenLink=?, passwortVergessenZeitpunkt=?, kommunikationssprache=?, "
                    + "eMailFuerVersand=?, emailBestaetigt=?, emailBestaetigenLink=?, "
                    + "eMail2FuerVersand=?, email2Bestaetigt=?, email2BestaetigenLink=?, zweiFaktorAuthentifizierungAktiv=?, konferenzTestDurchgefuehrt=?, konferenzTestAblauf=?, "
                    + "konferenzSprechen=?, registrierungAktionaersPortalErfolgt=?, registrierungAktionaersPortalErfolgtZeitstempel=?, registrierungMitgliederPortalErfolgt=?, registrierungMitgliederPortalErfolgtZeitstempel=? " 
                    + "WHERE "
                    + "loginKennung=? AND db_version=? ";

            PreparedStatement lPStm = verbindung.prepareStatement(lSql);
            fuellePreparedStatementKomplett(lPStm, 1, pEcl);
            lPStm.setString(anzfelder + 1, pEcl.loginKennung);
            lPStm.setLong(anzfelder + 2, pEcl.db_version - 1);

            int ergebnis = executeUpdate(lPStm);
            lPStm.close();
            if (ergebnis == 0) {
                return (CaFehler.pfXyWurdeVonAnderemBenutzerVeraendert);
            }
        } catch (Exception e1) {
            CaBug.drucke("001");
            System.err.println(" " + e1.getMessage());
            return (-1);
        }

        return (1);
    }

    
    public int updateKonferenzTestDurchgefuehrt(int loginIdent, int pNeuerTeststatus) {
        try {

            String lSql = "UPDATE " + dbSchema() + "tbl_logindaten SET "
                    + "db_version=db_version+1, konferenzTestDurchgefuehrt=? " + "WHERE " + "ident=? ";

            PreparedStatement lPStm = verbindung.prepareStatement(lSql);
            lPStm.setInt(1, pNeuerTeststatus);
            lPStm.setInt(2, loginIdent);

            int ergebnis = lPStm.executeUpdate();
            lPStm.close();
            if (ergebnis == 0) {
                return (CaFehler.pfXyWurdeVonAnderemBenutzerVeraendert);
            }
        } catch (Exception e1) {
            CaBug.drucke("001");
            System.err.println(" " + e1.getMessage());
            return (-1);
        }
        return 1;
    }
    
    public int updateKonferenzTestAblauf(int loginIdent, int pNeuerStatus) {
        try {

            String lSql = "UPDATE " + dbSchema() + "tbl_logindaten SET "
                    + "db_version=db_version+1, konferenzTestAblauf=? " + "WHERE " + "ident=? ";

            PreparedStatement lPStm = verbindung.prepareStatement(lSql);
            lPStm.setInt(1, pNeuerStatus);
            lPStm.setInt(2, loginIdent);

            int ergebnis = lPStm.executeUpdate();
            lPStm.close();
            if (ergebnis == 0) {
                return (CaFehler.pfXyWurdeVonAnderemBenutzerVeraendert);
            }
        } catch (Exception e1) {
            CaBug.drucke("001");
            System.err.println(" " + e1.getMessage());
            return (-1);
        }
        return 1;
    }


    public int updateKonferenzSprechen(int loginIdent, int pNeuerStatus) {
        try {

            String lSql = "UPDATE " + dbSchema() + "tbl_logindaten SET "
                    + "db_version=db_version+1, konferenzSprechen=? " + "WHERE " + "ident=? ";

            PreparedStatement lPStm = verbindung.prepareStatement(lSql);
            lPStm.setInt(1, pNeuerStatus);
            lPStm.setInt(2, loginIdent);

            int ergebnis = lPStm.executeUpdate();
            lPStm.close();
            if (ergebnis == 0) {
                return (CaFehler.pfXyWurdeVonAnderemBenutzerVeraendert);
            }
        } catch (Exception e1) {
            CaBug.drucke("001");
            System.err.println(" " + e1.getMessage());
            return (-1);
        }
        return 1;
    }
    
    
    
    public int updateHinweisWeitere(int loginIdent, int hinweisWeitereIdent, boolean setzen) {
        read_ident(loginIdent);
        int lHinweisWeitereBestaetigt=this.ergebnisPosition(0).hinweisWeitereBestaetigt;
        lHinweisWeitereBestaetigt=KonstHinweisWeitere.leereBit(KonstHinweisWeitere.WEISUNG_QUITTUNG_JN, lHinweisWeitereBestaetigt);
        
        if (setzen) {
            lHinweisWeitereBestaetigt=(lHinweisWeitereBestaetigt | hinweisWeitereIdent);
        }
        
        try {

            
            String lSql = "UPDATE " + dbSchema() + "tbl_logindaten SET "
                    + "hinweisWeitereBestaetigt=? " + "WHERE " + "ident=? ";

            PreparedStatement lPStm = verbindung.prepareStatement(lSql);
            lPStm.setInt(1, lHinweisWeitereBestaetigt);
            lPStm.setInt(2, loginIdent);

            int ergebnis = lPStm.executeUpdate();
            lPStm.close();
            if (ergebnis == 0) {
                return (CaFehler.pfXyWurdeVonAnderemBenutzerVeraendert);
            }
        } catch (Exception e1) {
            CaBug.drucke("001");
            System.err.println(" " + e1.getMessage());
            return (-1);
        }
        return lHinweisWeitereBestaetigt;
    }

    
    /**
     * Nur Hilfsroutine! Resettet alle Passwörter auf das übergebene
     * Initial-Passwort für den aktuellen Mandanten. Das Passwort wird als Parameter
     * unverschlüsselt übergeben und sowohl als Initialpasswort als auch
     * als verschlüsseltes Passwort eingetragen.
     * 
     * Außerdem wird die hinterlegte E-Mail-Adresse auf '' gesetzt.
     */
    public int resetPasswort(String pPasswort) {

        String verschluesseltesPasswort=CaPasswortVerschluesseln.verschluesseln(pPasswort);
        String initailPasswort=CaPasswortVerschluesseln.codeInitialPW(pPasswort);
        
        try {

            String lSql = "UPDATE " + dbSchema() + "tbl_logindaten SET "
                    + "passwortVerschluesselt=?, passwortInitial=?, eMailFuerVersand=''";

            PreparedStatement lPStm = verbindung.prepareStatement(lSql);
            lPStm.setString(1, verschluesseltesPasswort);
            lPStm.setString(2, initailPasswort);

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

    
    public int resetPasswortDemo(EclLoginDatenDemo pEclLoginDatenDemo) {

        String verschluesseltesPasswort=CaPasswortVerschluesseln.verschluesseln(pEclLoginDatenDemo.passwortDemo);
        String initailPasswort=CaPasswortVerschluesseln.codeInitialPW(pEclLoginDatenDemo.passwortDemo);
        
        try {

            String lSql = "UPDATE " + dbSchema() + "tbl_logindaten SET "
                    + "passwortVerschluesselt=?, passwortInitial=?, eigenesPasswort=0 WHERE loginKennung=? ";

            PreparedStatement lPStm = verbindung.prepareStatement(lSql);
            lPStm.setString(1, verschluesseltesPasswort);
            lPStm.setString(2, initailPasswort);
            lPStm.setString(3, pEclLoginDatenDemo.loginKennung);

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

    
    /**Aktienregister bleibt grundsätzlich erhalten, aber für alle Aktionäre
     * wird gesetzt, dass sie beim nächsten Portallogin Disclaimer etc. wieder bestätigen müssen
     * (hinweisAktionaersPortalBestaetigt=0)
     */
    public int updateSetzeHinweisAktionaersPortalBestaetigtAuf0() {
        return updateSetzeHinweisAktionaersPortalBestaetigtAuf0("");
    }

    public int updateSetzeHinweisAktionaersPortalBestaetigtAuf0(String pLoginKennung) {
        try {

            String lSql = "UPDATE " + dbSchema() + "tbl_logindaten SET "
                    + "hinweisAktionaersPortalBestaetigt=0, "
                    + "registrierungAktionaersPortalErfolgt=0, registrierungAktionaersPortalErfolgtZeitstempel=0 ";

            if (!pLoginKennung.isEmpty()) {
                lSql=lSql+" WHERE loginKennung=?";
            }
            
            PreparedStatement lPStm = verbindung.prepareStatement(lSql);
            
            if (!pLoginKennung.isEmpty()) {
                lPStm.setString(1, pLoginKennung);
            }

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

    /**Aktienregister bleibt grundsätzlich erhalten, aber für alle Aktionäre
     * wird gesetzt, dass sie beim nächsten Portallogin Disclaimer etc. wieder bestätigen müssen
     * (hinweisHVPortalBestaetigt=0)
     */
    public int updateSetzeHinweisHVPortalBestaetigtAuf0() {
        return updateSetzeHinweisHVPortalBestaetigtAuf0("");
    }
    public int updateSetzeHinweisHVPortalBestaetigtAuf0(String pLoginKennung) {

        try {

            String lSql = "UPDATE " + dbSchema() + "tbl_logindaten SET "
                    + "hinweisHVPortalBestaetigt=0 ";

            if (!pLoginKennung.isEmpty()) {
                lSql=lSql+" WHERE loginKennung=?";
            }

            PreparedStatement lPStm = verbindung.prepareStatement(lSql);

            if (!pLoginKennung.isEmpty()) {
                lPStm.setString(1, pLoginKennung);
            }

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

    public int updateEmailRegistrierung0(String pLoginKennung) {

        try {

            String lSql = "UPDATE " + dbSchema() + "tbl_logindaten SET "
                    + "eVersandRegistrierung=0, eVersandRegistrierungErstZeitpunkt='', "
                    + "eMailFuerVersand='', emailBestaetigt=0, emailBestaetigenLink=''  ";

            if (!pLoginKennung.isEmpty()) {
                lSql=lSql+" WHERE loginKennung=?";
            }

            PreparedStatement lPStm = verbindung.prepareStatement(lSql);

            if (!pLoginKennung.isEmpty()) {
                lPStm.setString(1, pLoginKennung);
            }

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

    /**
     * Nur Hilfsroutine! Resettet alle Passwörter / verschlüsselt auf das übergebene
     * Passwort für den aktuellen Mandanten
     */
    public int updateSetzeEMailRegistrierungAufErneutAbfragen() {

        try {

            String lSql = "UPDATE " + dbSchema() + "tbl_logindaten SET "
                    + "eVersandRegistrierung=0 WHERE eVersandRegistrierung=1 ";

            PreparedStatement lPStm = verbindung.prepareStatement(lSql);

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

    /**
     * Nur Hilfsroutine!
     *  
     * Aktienregister bleibt grundsätzlich erhalten, aber für alle Aktionäre
     * wird gesetzt, dass sie beim nächsten Portallogin die sonstigen Hinweise wieder bestätigen müssen.
     * In pRuecksetzen sind dabei die Bits zu übergeben, die auf 0 gesetzt werden sollen! Also z.B.
     * pRuecksetzen=3 => die Bits für 1 und 2 werden zurückgesetzt 
     *
     */
    public int updateSetzeHinweisWeitereBestaetigtAuf0(int pRuecksetzen) {
        return updateSetzeHinweisWeitereBestaetigtAuf0("", pRuecksetzen);
    }
    
    public int updateSetzeHinweisWeitereBestaetigtAuf0(String pLoginKennung, int pRuecksetzen) {

        int lRuecksetzen = ~pRuecksetzen; //Bitweise negieren
        try {

            String lSql = "UPDATE " + dbSchema() + "tbl_logindaten SET "
                    + "hinweisWeitereBestaetigt=hinweisWeitereBestaetigt & ?,"
                    + "registrierungMitgliederPortalErfolgt=0, registrierungMitgliederPortalErfolgtZeitstempel=0 ";

            PreparedStatement lPStm = verbindung.prepareStatement(lSql);
            lPStm.setInt(1, lRuecksetzen);

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

    
    /**
     * Update der login-Server-Nummer für ident.
     *
     * db_version wird nicht erhöht! -1 => unbekannter Fehler 1 = Update wurde
     * durchgeführt.
     * 
     */
    public int update_letzterLoginAufServer(int pIdent, int pServerNummerAlt, long pZeitstempelAlt,
            int pServerNummerNeu, long pZeitstempelNeu) {

        try {

            String lSql = "UPDATE " + dbSchema() + "tbl_logindaten SET " + "letzterLoginAufServer=?, zeitstempel=? "
                    + "WHERE " + "ident=? AND letzterLoginAufServer=? AND zeitstempel=?  ";

            PreparedStatement lPStm = verbindung.prepareStatement(lSql);
            lPStm.setInt(1, pServerNummerNeu);
            lPStm.setLong(2, pZeitstempelNeu);
            lPStm.setInt(3, pIdent);
            lPStm.setInt(4, pServerNummerAlt);
            lPStm.setLong(5, pZeitstempelAlt);

            int ergebnis = executeUpdate(lPStm);
            lPStm.close();
            if (ergebnis == 0) { // Dann konnte kein Update durchgeführt werden
                return 0;
            }
        } catch (Exception e1) {
            CaBug.drucke("001");
            System.err.println(" " + e1.getMessage());
            return (-1);
        }

        return (1);
    }

    /**
     * Update der login-Server-Nummer für ident auf positiven Serverwert (nicht
     * nachladen)
     *
     * db_version wird nicht erhöht! -1 => unbekannter Fehler 1 = Update wurde
     * durchgeführt.
     * 
     */
    public int update_letzterLoginAufServerPositiv(int pIdent) {

        try {

            String lSql = "UPDATE " + dbSchema() + "tbl_logindaten SET "
                    + "letzterLoginAufServer=ABS(letzterLoginAufServer) " + "WHERE " + "ident=? ";

            PreparedStatement lPStm = verbindung.prepareStatement(lSql);
            lPStm.setInt(1, pIdent);

            int ergebnis = executeUpdate(lPStm);
            lPStm.close();
            if (ergebnis == 0) { // Dann konnte kein Update durchgeführt werden
                return 0;
            }
        } catch (Exception e1) {
            CaBug.drucke("001");
            System.err.println(" " + e1.getMessage());
            return (-1);
        }

        return (1);
    }

    /**
     * Update der login-Server-Nummer für ident auf negativen Serverwert
     * (nachladen)
     *
     * db_version wird nicht erhöht! -1 => unbekannter Fehler 1 = Update wurde
     * durchgeführt.
     * 
     */
    public int update_letzterLoginAufServerNegativ(int pIdent) {

        try {

            String lSql = "UPDATE " + dbSchema() + "tbl_logindaten SET "
                    + "letzterLoginAufServer=ABS(letzterLoginAufServer)*(-1) " + "WHERE " + "ident=? ";

            PreparedStatement lPStm = verbindung.prepareStatement(lSql);
            lPStm.setInt(1, pIdent);

            int ergebnis = executeUpdate(lPStm);
            lPStm.close();
            if (ergebnis == 0) { // Dann konnte kein Update durchgeführt werden
                return 0;
            }
        } catch (Exception e1) {
            CaBug.drucke("001");
            System.err.println(" " + e1.getMessage());
            return (-1);
        }

        return (1);
    }

    public int update_letzterLoginAufServerNegativ(String pKennung) {

        try {

            String lSql = "UPDATE " + dbSchema() + "tbl_logindaten SET "
                    + "letzterLoginAufServer=ABS(letzterLoginAufServer)*(-1) " + "WHERE " + "loginKennung=? ";

            PreparedStatement lPStm = verbindung.prepareStatement(lSql);
            lPStm.setString(1, pKennung);

            int ergebnis = executeUpdate(lPStm);
            lPStm.close();
            if (ergebnis == 0) { // Dann konnte kein Update durchgeführt werden
                return 0;
            }
        } catch (Exception e1) {
            CaBug.drucke("001");
            System.err.println(" " + e1.getMessage());
            return (-1);
        }

        return (1);
    }

    
    /**pGesperrt=true => User wird gesperrt; =false => User wird entsperrt*/
    public int update_userGesperrt(String pKennung, boolean pGesperrt) {
        int lAnmeldenUnzulaessig=0;
        if (pGesperrt) {
            lAnmeldenUnzulaessig=1;
        }

        try {

            String lSql = "UPDATE " + dbSchema() + "tbl_logindaten SET "
                    + "anmeldenUnzulaessig=? " + "WHERE " + "loginKennung=? ";

            PreparedStatement lPStm = verbindung.prepareStatement(lSql);
            lPStm.setInt(1, lAnmeldenUnzulaessig);
            lPStm.setString(2, pKennung);

            int ergebnis = executeUpdate(lPStm);
            lPStm.close();
            if (ergebnis == 0) { // Dann konnte kein Update durchgeführt werden
                return 0;
            }
        } catch (Exception e1) {
            CaBug.drucke("001");
            System.err.println(" " + e1.getMessage());
            return (-1);
        }

        return (1);
    }

    
    /**Setzt alle User auf Gesperrt*/
    public int update_alleUserGesperrt(){
        try {

            String lSql = "UPDATE " + dbSchema() + "tbl_logindaten SET "
                    + "anmeldenUnzulaessig=1 ";

            PreparedStatement lPStm = verbindung.prepareStatement(lSql);

            int ergebnis = executeUpdate(lPStm);
            lPStm.close();
            if (ergebnis == 0) { // Dann konnte kein Update durchgeführt werden
                return 0;
            }
        } catch (Exception e1) {
            CaBug.drucke("001");
            System.err.println(" " + e1.getMessage());
            return (-1);
        }

        return (1);
    }

    
    /**Setzt alle User auf "nicht getestet"*/
    public int update_alleUserResetWortmeldeablauf(){
        try {

            String lSql = "UPDATE " + dbSchema() + "tbl_logindaten SET "
                    + "konferenzTestDurchgefuehrt=0, konferenzTestAblauf=0, konferenzSprechen=0 ";

            PreparedStatement lPStm = verbindung.prepareStatement(lSql);

            int ergebnis = executeUpdate(lPStm);
            lPStm.close();
            if (ergebnis == 0) { // Dann konnte kein Update durchgeführt werden
                return 0;
            }
        } catch (Exception e1) {
            CaBug.drucke("001");
            System.err.println(" " + e1.getMessage());
            return (-1);
        }

        return (1);
    }
    
    public int updateEmail2() {
        
            int erg = 0;

            String sql = "UPDATE " + dbSchema() + "tbl_logindaten lo "
                    + "LEFT JOIN  " + dbSchema() + "tbl_aktienregister are ON lo.aktienregisterIdent = are.aktienregisterIdent "
                    + "SET lo.eMail2FuerVersand = are.emailVersand, lo.email2Bestaetigt = 2 "
                    + "WHERE lo.eMail2FuerVersand != are.emailVersand";

            try (PreparedStatement pstmt = verbindung.prepareStatement(sql)) {

                erg = executeUpdate(pstmt);

            } catch (SQLException e) {
                e.printStackTrace();
            }
            return erg;
    }

    
    /**
     * Return-Werte: pfXyWurdeVonAnderemBenutzerVeraendert -1 => undefinierter
     * Fehler 1 => Löschen erfolgreich
     */
    public int delete(String pLoginKennung) {
        try {

            String sql = "DELETE FROM " + dbSchema() + "tbl_logindaten WHERE loginKennung=? ";

            PreparedStatement pstm1 = verbindung.prepareStatement(sql);
            pstm1.setString(1, pLoginKennung);

            int ergebnis1 = executeUpdate(pstm1);
            pstm1.close();
            if (ergebnis1 == 0) {
                return (CaFehler.pfXyWurdeVonAnderemBenutzerVeraendert);
            }
        } catch (Exception e1) {
            CaBug.drucke("001");
            System.err.println(" " + e1.getMessage());
            return (-1);
        }

        return (1);
    }

    /**Löscht alle Kennungen, die mit S beginnen.
     * 
     * Wird benötigt fürs Zurücksetzen der Meldedaten
    */
    public int deleteSKennungen() {
        try {

            String sql = "DELETE FROM " + dbSchema() + "tbl_logindaten WHERE loginKennung like 'S%' ";

            PreparedStatement pstm1 = verbindung.prepareStatement(sql);

            int ergebnis1 = executeUpdate(pstm1);
            pstm1.close();
            if (ergebnis1 == 0) {
                return (CaFehler.pfXyWurdeVonAnderemBenutzerVeraendert);
            }
        } catch (Exception e1) {
            CaBug.drucke("001");
            System.err.println(" " + e1.getMessage());
            return (-1);
        }
        return (1);
    }

    public int insertAufbereitungListe(List<EclLoginDaten> list) {

        int erg = 0;

        String sql = liefereInsertSql();

        try (PreparedStatement pstmt = verbindung.prepareStatement(sql)) {

            for (EclLoginDaten pData : list) {

                fuellePreparedStatementKomplett(pstmt, 1, pData);
                executeUpdate(pstmt);
                erg++;
            }

        } catch (SQLException e) {
            CaBug.drucke("001");
            System.out.println(e.toString());
            return -1;
        }
        return erg;
    }
}
