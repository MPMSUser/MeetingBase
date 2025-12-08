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
import java.util.LinkedList;
import java.util.List;

import de.meetingapps.meetingportal.meetComAllg.CaBug;
import de.meetingapps.meetingportal.meetComAllg.CaFehler;
import de.meetingapps.meetingportal.meetComAllg.CaPasswortErzeugen;
import de.meetingapps.meetingportal.meetComAllg.CaString;
import de.meetingapps.meetingportal.meetComBVerwaltung.BvReload;
import de.meetingapps.meetingportal.meetComEntities.EclEindeutigeKennung;
import de.meetingapps.meetingportal.meetComEntities.EclGeraeteParameter;
import de.meetingapps.meetingportal.meetComEntities.EclNummernFormSet;
import de.meetingapps.meetingportal.meetComEntities.EclParameter;
import de.meetingapps.meetingportal.meetComEntities.EclPortalFunktion;
import de.meetingapps.meetingportal.meetComEntities.EclPortalPhase;
import de.meetingapps.meetingportal.meetComEntities.EclPortalUnterlagen;
import de.meetingapps.meetingportal.meetComEntities.EclReload;
import de.meetingapps.meetingportal.meetComEntities.EclVeranstaltungenAktion;
import de.meetingapps.meetingportal.meetComEntities.EclVeranstaltungenElement;
import de.meetingapps.meetingportal.meetComEntities.EclVeranstaltungenElementDetail;
import de.meetingapps.meetingportal.meetComEntities.EclVeranstaltungenVeranstaltung;
import de.meetingapps.meetingportal.meetComEntities.EclWortmeldetischFolgeStatus;
import de.meetingapps.meetingportal.meetComEntities.EclWortmeldetischStatus;
import de.meetingapps.meetingportal.meetComEntities.EclWortmeldetischView;
import de.meetingapps.meetingportal.meetComEntities.EclWortmeldetischViewStatus;
import de.meetingapps.meetingportal.meetComHVParam.HVParam;
import de.meetingapps.meetingportal.meetComHVParam.ParamGeraet;
import de.meetingapps.meetingportal.meetComHVParam.ParamNummernformen;
import de.meetingapps.meetingportal.meetComHVParam.ParamNummernkreise;
import de.meetingapps.meetingportal.meetComHVParam.ParamServer;
import de.meetingapps.meetingportal.meetComHVParam.ParamServerStatic;
import de.meetingapps.meetingportal.meetComKonst.KonstStimmart;

/**Hinweis: da teilweise Mandantenübergreifend, und Zugriff auch über Puffer möglich, läuft in dieser Db-Klasse einiges
 * anders als in den Standard-Db-Klassen! Bitte Hinweise zu den einzelnen Funktionen beachten!
 */
public class DbParameter  extends DbRootExecute {

    private int logDrucken=3;
    private Connection verbindung = null;
    private DbBasis dbBasis = null;
    private DbBundle dbBundle = null;

    private EclParameter ergebnisArray[] = null;
    private EclGeraeteParameter ergebnisArrayGeraet[] = null;

    /**Ergebnis von read*_all*/
    public ParamGeraet ergParamGeraet = null;
    public ParamServer ergParamServer = null;
    public HVParam ergHVParam = null;

    /**Wird auf true gesetzt, um DB-Operationen auf "Set" auszuführen. Achtung -
     * unmittelbar vor DB-Aufruf setzen und dann gleich wieder zurücksetzen!
     */
    private boolean nutzeSet = false;
    private int parameterSetIdent = 0;

    /**Muß unmittelbar - wirklich unmittelbar - vor dem Aufruf der entsprechenden
     * DB-Operation erfolgen
     */
    public void nutzeSetBeginn(int pParameterSetIdent) {
        nutzeSet = true;
        parameterSetIdent = pParameterSetIdent;
    }

    /**Muß unmittelbar - wirklich unmittelbar - vor dem Aufruf der entsprechenden
     * DB-Operation erfolgen
     */
    public void nutzeSetEnde() {
        nutzeSet = false;
    }

    /**Liefert den zu verwendenden Table - je nach nutzeSet*/
    private String liefereTblParameter() {
        if (nutzeSet) {
            return dbBundle.getSchemaAllgemein() + "tbl_setparameter";
        } else {
            return dbBundle.getSchemaMandant() + "tbl_parameter";
        }
    }

    /**Liefert den zu verwendenden Table - je nach nutzeSet*/
    private String liefereTblParameterLang() {
        if (nutzeSet) {
            return dbBundle.getSchemaAllgemein() + "tbl_setparameterLang";
        } else {
            return dbBundle.getSchemaMandant() + "tbl_parameterLang";
        }
    }

    /*************************Initialisierung***************************/
    public DbParameter(DbBundle pDbBundle) {
        /* Verbindung in lokale Daten eintragen*/
        if (pDbBundle == null) {
            CaBug.drucke("DbParameter.init 001 - dbBundle nicht initialisiert");
            return;
        }
        if (pDbBundle.dbBasis == null) {
            CaBug.drucke("DbParameter.init 002 - dbBasis nicht initialisiert");
            return;
        }

        dbBasis = pDbBundle.dbBasis;
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
    public EclParameter ergebnisPosition(int pN) {
        if (ergebnisArray == null) {
            CaBug.drucke("DbParameter.ergebnisPosition 001");
            return null;
        }
        if (pN < 0) {
            CaBug.drucke("DbParameter.ergebnisPosition 002");
            return null;
        }
        if (pN >= ergebnisArray.length) {
            CaBug.drucke("DbParameter.ergebnisPosition 003");
            return null;
        }
        return ergebnisArray[pN];
    }

    /************Neuanlegen Table******************************/
    public int createTable_parameterServer() {
        int rc = 0;
        DbLowLevel lDbLowLevel = new DbLowLevel(dbBundle);
        rc = lDbLowLevel.createTable("CREATE TABLE " + dbBundle.getSchemaAllgemein() + "tbl_parameterServer ( "
                + "`mandant` int(11) NOT NULL, " + "`ident` int(11) NOT NULL, "
                + "`db_version` bigint(20) DEFAULT NULL, " + "`wert` char(200) DEFAULT NULL, "
                + "`beschreibung` char(80) DEFAULT NULL, " + "PRIMARY KEY (`ident`,`mandant`) " + ") ");
        return rc;
    }

    public int createTable_parameterGeraete() {
        int rc = 0;
        DbLowLevel lDbLowLevel = new DbLowLevel(dbBundle);
        rc = lDbLowLevel.createTable("CREATE TABLE " + dbBundle.getSchemaAllgemein() + "tbl_geraeteparameter ( "
                + "`klasse` int(11) NOT NULL, " + "`ident` int(11) NOT NULL, "
                + "`db_version` bigint(20) DEFAULT NULL, " + "`wert` char(40) DEFAULT NULL, "
                + "`beschreibung` char(80) DEFAULT NULL, " + "PRIMARY KEY (`ident`,`klasse`) " + ") ");
        return rc;
    }

    public int createTable_parameter() {
        int rc = 0;
        DbLowLevel lDbLowLevel = new DbLowLevel(dbBundle);
        rc = lDbLowLevel.createTable("CREATE TABLE " + liefereTblParameter() + " ( " + "`mandant` int(11) NOT NULL, "
                + "`ident` int(11) NOT NULL, " + "`db_version` bigint(20) DEFAULT NULL, "
                + "`wert` char(40) DEFAULT NULL, " + "`beschreibung` char(80) DEFAULT NULL, "
                + "PRIMARY KEY (`ident`,`mandant`) " + ") ");
        return rc;
    }

    /**Hinweis: parameterLang unterscheidet sich lediglich durch die Länge des Wertes. Es können also
     * weitgehend alle Routinen von tbl_parameter unverändert übernommen werden!
     */
    public int createTable_parameterLang() {
        int rc = 0;
        DbLowLevel lDbLowLevel = new DbLowLevel(dbBundle);
        rc = lDbLowLevel.createTable("CREATE TABLE " + liefereTblParameterLang() + "( " + "`mandant` int(11) NOT NULL, "
                + "`ident` int(11) NOT NULL, " + "`db_version` bigint(20) DEFAULT NULL, "
                + "`wert` varchar(200) DEFAULT NULL, " + "`beschreibung` char(80) DEFAULT NULL, "
                + "PRIMARY KEY (`ident`,`mandant`) " + ") ");
        return rc;
    }

    /**Hinweis: parameterLfd wird nicht mehr verwendet! Ist hier nur noch drin wg. Umarbeitung Mandanten. War zu faul, das dort
     * einzuarbeiten.*/
    public int createTable_parameterLfd() {
        int rc = 0;
        DbLowLevel lDbLowLevel = new DbLowLevel(dbBundle);
        rc = lDbLowLevel.createTable("CREATE TABLE " + dbBundle.getSchemaMandant() + "tbl_parameterlfd ( "
                + "`mandant` int(11) NOT NULL, " + "`ident` int(11) NOT NULL, "
                + "`db_version` bigint(20) DEFAULT NULL, " + "`wert` char(40) DEFAULT NULL, "
                + "`beschreibung` char(80) DEFAULT NULL, " + "PRIMARY KEY (`ident`,`mandant`) " + ") ");
        return rc;
    }

    public int createTable_parameterLocal() {
        int rc = 0;
        DbLowLevel lDbLowLevel = new DbLowLevel(dbBundle);
        rc = lDbLowLevel.createTable("CREATE TABLE " + dbBundle.getSchemaMandant() + "tbl_parameterlocal ( "
                + "`mandant` int(11) NOT NULL, " + "`ident` int(11) NOT NULL, "
                + "`db_version` bigint(20) DEFAULT NULL, " + "`wert` char(40) DEFAULT NULL, "
                + "`beschreibung` char(80) DEFAULT NULL, " + "`selektionPortal` int(11) DEFAULT NULL, "
                + "`selektionApp` int(11) DEFAULT NULL, " + "PRIMARY KEY (`ident`,`mandant`) " + ") ");
        return rc;
    }

    /**************************deleteAll Löschen aller Datensätze  eines Mandanten****************/
    public int deleteAll_parameter() {
        return dbBundle.dbLowLevel.deleteMandant("DELETE FROM " + liefereTblParameter() + " where mandant=?;");
    }

    public int deleteAll_parameterLang() {
        return dbBundle.dbLowLevel.deleteMandant("DELETE FROM " + liefereTblParameterLang() + " where mandant=?;");
    }

    public int deleteAll_parameterLfd() {
        return dbBundle.dbLowLevel
                .deleteMandant("DELETE FROM " + dbBundle.getSchemaMandant() + "tbl_parameterlfd where mandant=?;");
    }

    public int deleteAll_parameterLocal() {
        return dbBundle.dbLowLevel
                .deleteMandant("DELETE FROM " + dbBundle.getSchemaMandant() + "tbl_parameterlocal where mandant=?;");
    }

    /**************************setzt aktuelle Mandantennummer bei allen Datensätzen****************/
    public int updateMandant() {
        dbBundle.dbLowLevel.rawUpdateMandant(liefereTblParameter());
        dbBundle.dbLowLevel.rawUpdateMandant(liefereTblParameterLang());
        dbBundle.dbLowLevel.rawUpdateMandant(dbBundle.getSchemaMandant() + "tbl_parameterlfd");
        return dbBundle.dbLowLevel.rawUpdateMandant(dbBundle.getSchemaMandant() + "tbl_parameterlocal");
    }

    /********** dekodiert die aktuelle Position aus ergebnis und gibt dieses zurück. ******/
    private EclParameter decodeErgebnis(ResultSet pErgebnis) {

        EclParameter lParameter = new EclParameter();

        try {
            lParameter.mandant = pErgebnis.getInt("mandant");
            lParameter.ident = pErgebnis.getInt("ident");
            lParameter.db_version = pErgebnis.getLong("db_version");
            lParameter.wert = pErgebnis.getString("wert");
            lParameter.beschreibung = pErgebnis.getString("beschreibung");
        } catch (Exception e) {
            CaBug.drucke("DbParameter.decodeErgebnis 001");
            System.err.println(" " + e.getMessage());
        }

        return lParameter;
    }

    /********** dekodiert die aktuelle Position aus ergebnis und gibt dieses zurück******/
    private EclGeraeteParameter decodeErgebnisGeraet(ResultSet pErgebnis) {

        EclGeraeteParameter lGeraeteParameter = new EclGeraeteParameter();

        try {
            lGeraeteParameter.klasse = pErgebnis.getInt("klasse");
            lGeraeteParameter.ident = pErgebnis.getInt("ident");
            lGeraeteParameter.db_version = pErgebnis.getLong("db_version");
            lGeraeteParameter.wert = pErgebnis.getString("wert");
            lGeraeteParameter.beschreibung = pErgebnis.getString("beschreibung");
        } catch (Exception e) {
            CaBug.drucke("DbParameter.decodeErgebnisGeraet 001");
            System.err.println(" " + e.getMessage());
        }

        return lGeraeteParameter;
    }

    /********************* Fuellen Prepared Statement mit allen Feldern, beginnend bei offset.*****************
     * Kann sowohl für Insert, als auch für update verwendet werden.
     * 
     * offset= Startposition des ersten Feldes (also z.B. 1)
     */
    private int anzfelder = 5; /*Anpassen auf Anzahl der Felder pro Datensatz*/

    private void fuellePreparedStatementKomplett(PreparedStatement pPStm, int pOffset, EclParameter pParameter) {

        int startOffset = pOffset; /*Nur erforderlich zum Überprüfen von Programmierfehlern - Feldanzahl*/

        try {
            pPStm.setInt(pOffset, pParameter.mandant);
            pOffset++;
            pPStm.setInt(pOffset, pParameter.ident);
            pOffset++;
            pPStm.setLong(pOffset, pParameter.db_version);
            pOffset++;

            pPStm.setString(pOffset, pParameter.wert);
            pOffset++;
            pPStm.setString(pOffset, pParameter.beschreibung);
            pOffset++;

            if (pOffset - startOffset != anzfelder) {
                /*Nur wg. Überprüfung auf Programmierfehler - alle Felder berücksichtigt?*/
                CaBug.drucke("DbParameter.fuellePreparedStatementKomplett 002");
            }

        } catch (SQLException e) {
            CaBug.drucke("DbParameter.fuellePreparedStatementKomplett 001");
            e.printStackTrace();
        }

    }

    /********************* Fuellen Prepared Statement mit allen Feldern, beginnend bei offset.*****************
     * Kann sowohl für Insert, als auch für update verwendet werden.
     * 
     * offset= Startposition des ersten Feldes (also z.B. 1)
     */
    private int anzfelderGeraeteParameter = 5; /*Anpassen auf Anzahl der Felder pro Datensatz*/

    private void fuellePreparedStatementKomplettGeraeteParameter(PreparedStatement pPStm, int pOffset,
            EclGeraeteParameter pGeraeteParameter) {

        int startOffset = pOffset; /*Nur erforderlich zum Überprüfen von Programmierfehlern - Feldanzahl*/

        try {
            pPStm.setInt(pOffset, pGeraeteParameter.klasse);
            pOffset++;
            pPStm.setInt(pOffset, pGeraeteParameter.ident);
            pOffset++;
            pPStm.setLong(pOffset, pGeraeteParameter.db_version);
            pOffset++;

            pPStm.setString(pOffset, pGeraeteParameter.wert);
            pOffset++;
            pPStm.setString(pOffset, pGeraeteParameter.beschreibung);
            pOffset++;

            if (pOffset - startOffset != anzfelderGeraeteParameter) {
                /*Nur wg. Überprüfung auf Programmierfehler - alle Felder berücksichtigt?*/
                CaBug.drucke("DbParameter.fuellePreparedStatementKomplettGeraeteParameter 002");
            }

        } catch (SQLException e) {
            CaBug.drucke("DbParameter.fuellePreparedStatementKomplettGeraeteParameter 001");
            e.printStackTrace();
        }

    }

    /**********************************Insert - einzelner Satz*************************************************************
     * Verwendet von inser und insertServer.
     * 
     * Feld mandant wird von der aufrufenden Funktion immer selbstständig belegt.
     * 
     * Achtung: "ident" muß vorbelegt sein!
     * Returnwert:
     * =1 => Insert erfolgreich
     * ansonsten: Fehler
     */
    private int insertRaw(EclParameter pParameter, String pSql) {
        int erg = 0;

        try {
            PreparedStatement lPStm = verbindung.prepareStatement(pSql, ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
            fuellePreparedStatementKomplett(lPStm, 1, pParameter);
            erg = executeUpdate(lPStm);
            lPStm.close();
        } catch (Exception e2) {
            CaBug.drucke("DbParameter.insert 001");
            System.err.println(" " + e2.getMessage());
        }

        if (erg == 0) {/*Fehler beim Einfügen - d.h. primaryKey bereits vorhanden*/
            return (-1);
        }

        return (1);
    }

    /**Insert eines einzelnen Satzes in tbl_parameter
     * Achtung: "ident" muß vorbelegt sein!
     * Returnwert:
     * =1 => Insert erfolgreich
     * ansonsten: Fehler
     */
    public int insert(EclParameter pParameter) {
        if (!nutzeSet) {
            BvReload bvReload = new BvReload(dbBundle);
            bvReload.setReloadParameter(dbBundle.clGlobalVar.mandant);
        }
        return insert_ohneReload(pParameter);
    }

    public int insertLang(EclParameter pParameter) {
        if (!nutzeSet) {
            BvReload bvReload = new BvReload(dbBundle);
            bvReload.setReloadParameter(dbBundle.clGlobalVar.mandant);
        }
        return insertLang_ohneReload(pParameter);
    }

    public int insert_ohneReload(EclParameter pParameter) {
        String lSql = "INSERT INTO " + liefereTblParameter() + " (" + "mandant, ident, db_version, "
                + "wert, beschreibung " + ")" + "VALUES (" + "?, ?, ?, " + "?, ? " + ")";
        if (nutzeSet) {
            pParameter.mandant = parameterSetIdent;
        } else {
            pParameter.mandant = dbBundle.clGlobalVar.mandant;
        }

        return insertRaw(pParameter, lSql);
    }

    public int insertLang_ohneReload(EclParameter pParameter) {
        String lSql = "INSERT INTO " + liefereTblParameterLang() + " (" + "mandant, ident, db_version, "
                + "wert, beschreibung " + ")" + "VALUES (" + "?, ?, ?, " + "?, ? " + ")";
        if (nutzeSet) {
            pParameter.mandant = parameterSetIdent;
        } else {
            pParameter.mandant = dbBundle.clGlobalVar.mandant;
        }

        return insertRaw(pParameter, lSql);
    }

    /**Insert eines einzelnen Satzes in tbl_parameter
     * Achtung: "ident" muß vorbelegt sein!
     * Returnwert:
     * =1 => Insert erfolgreich
     * ansonsten: Fehler
     */
    public int insertLocal(EclParameter pParameter) {
        if (!nutzeSet) {
            BvReload bvReload = new BvReload(dbBundle);
            bvReload.setReloadParameter(dbBundle.clGlobalVar.mandant);
        }
        return insertLocal_ohneReload(pParameter);
    }

    public int insertLocal_ohneReload(EclParameter pParameter) {
        String lSql = "INSERT INTO " + dbBundle.getSchemaMandant() + "tbl_parameterLocal " + "("
                + "mandant, ident, db_version, " + "wert, beschreibung " + ")" + "VALUES (" + "?, ?, ?, " + "?, ? "
                + ")";
        pParameter.mandant = dbBundle.clGlobalVar.mandant;

        return insertRaw(pParameter, lSql);
    }

    /**Insert eines einzelnen Satzes in tbl_parameterServer	 
     * Achtung: "ident" muß vorbelegt sein!
     * Returnwert:
     * =1 => Insert erfolgreich
     * ansonsten: Fehler
     */
    public int insertServer(EclParameter pParameter) {
        if (!nutzeSet) {
            BvReload bvReload = new BvReload(dbBundle);
            bvReload.setReloadParameterServer();
        }
        return insertServer_ohneReload(pParameter);
    }

    public int insertServer_ohneReload(EclParameter pParameter) {
        String lSql = "INSERT INTO " + dbBundle.getSchemaAllgemein() + "tbl_parameterServer " + "("
                + "mandant, ident, db_version, " + "wert, beschreibung " + ")" + "VALUES (" + "?, ?, ?, " + "?, ? "
                + ")";

        pParameter.mandant = 0; //Server-Parameter sind immer mandanten-unabhängig
        return insertRaw(pParameter, lSql);
    }

    /**Insert eines einzelnen Satzes in tbl_parameterServer
     * Achtung: "ident" muß vorbelegt sein!
     * Returnwert:
     * =1 => Insert erfolgreich
     * ansonsten: Fehler
     */
    public int insertGeraeteParameter(EclGeraeteParameter pGeraeteParameter) {
        if (!nutzeSet) {
            BvReload bvReload = new BvReload(dbBundle);
            bvReload.setReloadParameterGeraete();
        }
        return insertGeraeteParameter_ohneReload(pGeraeteParameter);
    }

    public int insertGeraeteParameter_ohneReload(EclGeraeteParameter pGeraeteParameter) {
        int erg = 0;

        /* Start Transaktion */
        dbBasis.beginTransaction();

        /* Satz einfügen: 
         * Verarbeitungshinweis: 
         * 	>	nachdem InterneIdent immer eindeutig vergeben werden, ist prinzipiell eine "Doppeleinfügung"
         * 		von InterneIdent nicht möglich. Sollte es dazu kommen, ist das immer ein Programmfehler!
         */

        try {

            /*Felder Neuanlage füllen*/
            String lSql = "INSERT INTO " + dbBundle.getSchemaAllgemein() + "tbl_geraeteParameter " + "("
                    + "klasse, ident, db_version, " + "wert, beschreibung " + ")" + "VALUES (" + "?, ?, ?, " + "?, ? "
                    + ")";
            PreparedStatement lPStm = verbindung.prepareStatement(lSql, ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);

            fuellePreparedStatementKomplettGeraeteParameter(lPStm, 1, pGeraeteParameter);

            erg = executeUpdate(lPStm);
            lPStm.close();
        } catch (Exception e2) {
            CaBug.drucke("DbParameter.insertGeraeteParameter 001");
            System.err.println(" " + e2.getMessage());
        }

        if (erg == 0) {/*Fehler beim Einfügen - d.h. primaryKey bereits vorhanden*/
            dbBasis.rollbackTransaction();
            dbBasis.endTransaction();
            return (-1);
        }

        /* Ende Transaktion */
        dbBasis.endTransaction();
        return (1);
    }

    /*******************************Read - einzelner Satz**********************************************/
    private int readRaw(EclParameter lParameter, String pSql) {
        int anzInArray = 0;
        PreparedStatement lPStm = null;
        int gefSelect = 0; /*Nur zum Aufdecken von Programmierfehlern - in diesem Fall: unvollständige Parameterübergabe*/

        if (lParameter == null) {
            CaBug.drucke("DbParameter.readRaw 001");
            return -1;
        }

        try {
            if (lParameter.ident != 0) {
                gefSelect = 1;
                lPStm = verbindung.prepareStatement(pSql, ResultSet.TYPE_SCROLL_INSENSITIVE,
                        ResultSet.CONCUR_READ_ONLY);
                lPStm.setInt(1, lParameter.mandant);
                lPStm.setInt(2, lParameter.ident);
            }

            if (gefSelect == 0) {
                CaBug.drucke("DbParameter.readRaw 002");
                return -1;
            }

            ResultSet lErgebnis = executeQuery(lPStm);
            lErgebnis.last();
            anzInArray = lErgebnis.getRow();
            lErgebnis.beforeFirst();

            ergebnisArray = new EclParameter[anzInArray];

            int i = 0;
            while (lErgebnis.next() == true) {
                ergebnisArray[i] = this.decodeErgebnis(lErgebnis);
                i++;
            }
            lErgebnis.close();
            lPStm.close();

        } catch (Exception e) {
            CaBug.drucke("DbParameter.readRaw 003");
            System.err.println(" " + e.getMessage());
            return (-1);
        }

        return (anzInArray);
    }

    /**Realisierte Selektion derzeit, jeweils einzeln: 
     * ident + mandant (!!!)
     * 
     * Return-Wert:
     * >=1 => Anzahl der gefundenen Datensätze
     * =0 => nix gefunden
     * <0 => Fehler
     * */
    public int read(EclParameter lParameter) {

        String lSql = "SELECT * from " + liefereTblParameter() + " where " + "mandant=? AND "
                + "ident=? ORDER BY ident;";
        return readRaw(lParameter, lSql);
    }

    public int read(int pParameterIdent) {
        EclParameter lParameter=new EclParameter();
        lParameter.ident=pParameterIdent;
        lParameter.mandant=dbBundle.clGlobalVar.mandant;
        return read(lParameter);
    }
    
    public int readLang(EclParameter lParameter) {

        String lSql = "SELECT * from " + liefereTblParameterLang() + " where " + "mandant=? AND "
                + "ident=? ORDER BY ident;";
        return readRaw(lParameter, lSql);
    }

    public int readLang(int pParameterIdent) {
        EclParameter lParameter=new EclParameter();
        lParameter.ident=pParameterIdent;
        lParameter.mandant=dbBundle.clGlobalVar.mandant;
        return readLang(lParameter);
    }

    /**Realisierte Selektion derzeit, jeweils einzeln: 
     * ident + mandant (!!!)
     * 
     * Return-Wert:
     * >=1 => Anzahl der gefundenen Datensätze
     * =0 => nix gefunden
     * <0 => Fehler
     * */
    public int readLocal(EclParameter lParameter) {
        String lSql = "SELECT * from " + dbBundle.getSchemaMandant() + "tbl_ParameterLocal where " + "mandant=? AND "
                + "ident=? ORDER BY ident;";
        return readRaw(lParameter, lSql);
    }

    /**Realisierte Selektion derzeit, jeweils einzeln: 
     * ident + mandant (!!!)
     * 
     * Return-Wert:
     * >=1 => Anzahl der gefundenen Datensätze
     * =0 => nix gefunden
     * <0 => Fehler
     * */
    public int readServer(EclParameter lParameter) {
        String lSql = "SELECT * from " + dbBundle.getSchemaAllgemein() + "tbl_parameterServer where " + "mandant=? AND "
                + "ident=? ORDER BY ident;";
        return readRaw(lParameter, lSql);
    }

    /*********************************Update einzelner Satz************************************/
    /**pParameter.Mandant wird von aufrufender Funktion gefüllt!*/
    private int updateRaw(EclParameter pParameter, String pSql) {

        try {
            PreparedStatement lPStm = verbindung.prepareStatement(pSql);
            fuellePreparedStatementKomplett(lPStm, 1, pParameter);
            lPStm.setInt(anzfelder + 1, pParameter.ident);
            lPStm.setInt(anzfelder + 2, pParameter.mandant);

            int ergebnis = executeUpdate(lPStm);
            lPStm.close();
            if (ergebnis == 0) {
                return (CaFehler.pfXyWurdeVonAnderemBenutzerVeraendert);
            }
        } catch (Exception e1) {
            CaBug.drucke("DbParameter.updateRaw 001");
            System.err.println(" " + e1.getMessage());
            return (CaFehler.pfdXyBereitsVorhanden);
        }

        return (1);

    }

    /**Update. Versionsnummer wird um 1 hochgezählt
     * 
     * Zum Sicherstellen der Multiuserfähigkeit muß in jedem Fall der rc CaFehler.pfXyWurdeVonAnderemBenutzerVeraendert
     * nach Aufruf dieser Funktion abgefangen werden.
     * Ansonsten: rc=1 => ok, ansonsten Fehler
     */
    public int update(EclParameter pParameter) {
        if (!nutzeSet) {
            BvReload bvReload = new BvReload(dbBundle);
            bvReload.setReloadParameter(dbBundle.clGlobalVar.mandant);
        }
        return update_ohneReload(pParameter);
    }

    public int updateLang(EclParameter pParameter) {
        if (!nutzeSet) {
            BvReload bvReload = new BvReload(dbBundle);
            bvReload.setReloadParameter(dbBundle.clGlobalVar.mandant);
        }
        return updateLang_ohneReload(pParameter);
    }

    public int update_ohneReload(EclParameter pParameter) {
        String lSql = "UPDATE " + liefereTblParameter() + " SET " + "mandant=?, ident=?, db_version=?, "
                + "wert=?, beschreibung=? " + "WHERE " + "ident=? " + "AND mandant=?";

        if (nutzeSet) {
            pParameter.mandant = parameterSetIdent;
        } else {
            pParameter.mandant = dbBundle.clGlobalVar.mandant;
        }
        return updateRaw(pParameter, lSql);
    }

    public int updateLang_ohneReload(EclParameter pParameter) {

        String lSql = "UPDATE " + liefereTblParameterLang() + " SET " + "mandant=?, ident=?, db_version=?, "
                + "wert=?, beschreibung=? " + "WHERE " + "ident=? " + "AND mandant=?";

        if (nutzeSet) {
            pParameter.mandant = parameterSetIdent;
        } else {
            pParameter.mandant = dbBundle.clGlobalVar.mandant;
        }
        return updateRaw(pParameter, lSql);
    }

    /**Update. Versionsnummer wird um 1 hochgezählt
     * 
     * Zum Sicherstellen der Multiuserfähigkeit muß in jedem Fall der rc CaFehler.pfXyWurdeVonAnderemBenutzerVeraendert
     * nach Aufruf dieser Funktion abgefangen werden.
     * Ansonsten: rc=1 => ok, ansonsten Fehler
     */
    public int updateLocal(EclParameter pParameter) {
        if (!nutzeSet) {
            BvReload bvReload = new BvReload(dbBundle);
            bvReload.setReloadParameter(dbBundle.clGlobalVar.mandant);
        }
        return updateLocal_ohneReload(pParameter);
    }

    public int updateLocal_ohneReload(EclParameter pParameter) {

        String lSql = "UPDATE " + dbBundle.getSchemaMandant() + "tbl_ParameterLocal SET "
                + "mandant=?, ident=?, db_version=?, " + "wert=?, beschreibung=? " + "WHERE " + "ident=? "
                + "AND mandant=?";

        pParameter.mandant = dbBundle.clGlobalVar.mandant;
        return updateRaw(pParameter, lSql);
    }

    /**Update. Versionsnummer wird um 1 hochgezählt
     * 
     * Zum Sicherstellen der Multiuserfähigkeit muß in jedem Fall der rc CaFehler.pfXyWurdeVonAnderemBenutzerVeraendert
     * nach Aufruf dieser Funktion abgefangen werden.
     * Ansonsten: rc=1 => ok, ansonsten Fehler
     */
    public int updateServer(EclParameter pParameter) {
        if (!nutzeSet) {
            BvReload bvReload = new BvReload(dbBundle);
            bvReload.setReloadParameterServer();
        }
        return updateServer_ohneReload(pParameter);

    }

    public int updateServer_ohneReload(EclParameter pParameter) {
        String lSql = "UPDATE " + dbBundle.getSchemaAllgemein() + "tbl_parameterServer SET "
                + "mandant=?, ident=?, db_version=?, " + "wert=?, beschreibung=? " + "WHERE " + "ident=? AND "
                + "mandant=?";
        pParameter.mandant = 0; //Server-Parameter sind immer mandanten-unabhängig
        return updateRaw(pParameter, lSql);
    }

    /**Update. Versionsnummer wird um 1 hochgezählt
     * 
     * Zum Sicherstellen der Multiuserfähigkeit muß in jedem Fall der rc CaFehler.pfXyWurdeVonAnderemBenutzerVeraendert
     * nach Aufruf dieser Funktion abgefangen werden.
     * Ansonsten: rc=1 => ok, ansonsten Fehler
     */
    public int updateGeraeteParameter(EclGeraeteParameter pGeraeteParameter) {
        if (!nutzeSet) {
            BvReload bvReload = new BvReload(dbBundle);
            bvReload.setReloadParameterGeraete();
        }
        return updateGeraeteParameter_ohneReload(pGeraeteParameter);

    }

    public int updateGeraeteParameter_ohneReload(EclGeraeteParameter pGeraeteParameter) {

        pGeraeteParameter.db_version++;

        try {

            String lSql = "UPDATE " + dbBundle.getSchemaAllgemein() + "tbl_geraeteParameter SET "
                    + "klasse=?, ident=?, db_version=?, " + "wert=?, beschreibung=? " + "WHERE " + "ident=? AND "
                    + "klasse=?";

            PreparedStatement lPStm = verbindung.prepareStatement(lSql);
            fuellePreparedStatementKomplettGeraeteParameter(lPStm, 1, pGeraeteParameter);
            lPStm.setInt(anzfelderGeraeteParameter + 1, pGeraeteParameter.ident);
            lPStm.setInt(anzfelderGeraeteParameter + 2, pGeraeteParameter.klasse);

            int ergebnis = executeUpdate(lPStm);
            lPStm.close();
            if (ergebnis == 0) {
                return (CaFehler.pfXyWurdeVonAnderemBenutzerVeraendert);
            }
        } catch (Exception e1) {
            CaBug.drucke("DbParameter.updateGeraeteParameter 001");
            System.err.println(" " + e1.getMessage());
            return (CaFehler.pfdXyBereitsVorhanden);
        }

        return (1);

    }

    /**********************Satz Delete************************************************/
    /**pSql so aufbauen, dass als erster Parameter mandant/Klasse (Klasse
     * bei geräteParameter) in pSql ist, als zweiter dann pIdent*/
    public int deleteRaw(int pMandant, int pIdent, String pSql) {
        try {

            PreparedStatement pstm1 = verbindung.prepareStatement(pSql);
            pstm1.setInt(1, pMandant);
            pstm1.setInt(2, pIdent);

            int ergebnis1 = executeUpdate(pstm1);
            pstm1.close();
            if (ergebnis1 == 0) {
                return (CaFehler.pfXyWurdeVonAnderemBenutzerVeraendert);
            }
        } catch (Exception e1) {
            CaBug.drucke("DbParameter.deleteRaw 001");
            System.err.println(" " + e1.getMessage());
            return (CaFehler.pfdXyBereitsVorhanden);
        }

        return (1);
    }

    /**Löschen Einzelsatz aus tbl_parameter*/
    public int delete(int pMandant, int pIdent) {
        String pSql = "DELETE FROM " + liefereTblParameter() + " WHERE mandant=? AND ident=? ";
        return deleteRaw(pMandant, pIdent, pSql);
    }

    /**Löschen Einzelsatz aus tbl_parameterLang*/
    public int deleteLang(int pMandant, int pIdent) {
        String pSql = "DELETE FROM " + liefereTblParameterLang() + " WHERE mandant=? AND ident=? ";
        return deleteRaw(pMandant, pIdent, pSql);
    }

    /**Löschen Einzelsatz aus tbl_parameterServer*/
    public int deleteServer(int pMandant, int pIdent) {
        String pSql = "DELETE FROM " + dbBundle.getSchemaAllgemein()
                + "tbl_parameterServer WHERE mandant=? AND ident=? ";
        return deleteRaw(pMandant, pIdent, pSql);
    }

    /**Löschen Einzelsatz aus tbl_ParameterLocal*/
    public int deleteParameterLocal(int pMandant, int pIdent) {
        String pSql = "DELETE FROM " + dbBundle.getSchemaMandant() + "tbl_ParameterLocal WHERE mandant=? AND ident=? ";
        return deleteRaw(pMandant, pIdent, pSql);
    }

    /**Löschen Einzelsatz aus tbl_geraeteParameter*/
    public int deleteGeraeteParameter(int pKlasse, int pIdent) {
        String pSql = "DELETE FROM " + dbBundle.getSchemaAllgemein()
                + "tbl_geraeteParameter WHERE klasse=? AND ident=? ";
        return deleteRaw(pKlasse, pIdent, pSql);
    }

    /**Löschen aller Parameter einer Klasse aus tbl_geraeteParameter*/
    public int deleteGeraeteParameter_all(int pKlasse) {
        String pSql = "DELETE FROM " + dbBundle.getSchemaAllgemein() + "tbl_geraeteParameter WHERE klasse=? ";

        try {

            PreparedStatement pstm1 = verbindung.prepareStatement(pSql);
            pstm1.setInt(1, pKlasse);

            int ergebnis1 = executeUpdate(pstm1);
            pstm1.close();
            if (ergebnis1 == 0) {
                return (CaFehler.pfXyWurdeVonAnderemBenutzerVeraendert);
            }
        } catch (Exception e1) {
            CaBug.drucke("DbParameter.deleteGeraeteParameter_all 001");
            System.err.println(" " + e1.getMessage());
            return (CaFehler.pfdXyBereitsVorhanden);
        }

        return (1);
    }

    /***************read_all und Subroutinen***************************/
    private boolean liefereErgBoolean(int pI) {
        boolean hBoolean = true;
        int hInt = Integer.parseInt(ergebnisArray[pI].wert);
        if (hInt == 1) {
            hBoolean = true;
        } else {
            hBoolean = false;
        }
        return hBoolean;
    }

    private int liefereErgInt(int pI) {
        int hWert = 0;
        hWert = Integer.parseInt(ergebnisArray[pI].wert);
        return hWert;
    }

    private long liefereErgLong(int pI) {
        long hWert = 0;
        hWert = Long.parseLong(ergebnisArray[pI].wert);
        return hWert;
    }

    private double liefereErgDouble(int pI) {
        double hWert = 0;
        hWert = Double.parseDouble(ergebnisArray[pI].wert);
        return hWert;
    }

    private String liefereErgString(int pI) {
        String hWert = "";
        hWert = ergebnisArray[pI].wert.trim();
        return hWert;
    }

    /**Liefert false, wenn eingabe 0, sonst true*/
    private boolean liefereBoolToInt(int eingabe) {
        if (eingabe == 0) {
            return false;
        } else {
            return true;
        }
    }

    /**********************Einlesen aller Parameter*************************************************************/

    /**Ergebnis ist in ergebnisHVParam
     * Entspricht:
     * readParameter_all
     * ergänzt um:
     * > readLocal_all()
     * > readNummernformen_all()
     * > readLfd_all
     * nur in ergebnisHVParam statt direkt in pDbBundle
     * */
    public int readHVParam_all() {
        int anzInArray = 0;
        int erg = 0;
        PreparedStatement lPStm = null;

        ergHVParam = new HVParam();

        CaBug.druckeLog("HV Parameter aus DB laden", logDrucken, 3);

        DbReload dbReload=new DbReload(dbBundle);
        EclReload lReload=new EclReload();
        lReload.ident=1; // HV-Parameter
        lReload.mandant=dbBundle.clGlobalVar.mandant;
        dbReload.read(lReload);
        if (dbReload.anzErgebnis()>0) {
            lReload=dbReload.ergebnisPosition(0);
        }
        CaBug.druckeLog("lReload.reload="+lReload.reload, logDrucken, 10);
        ergHVParam.reloadVersionParameter=lReload.reload;
        
        try {
            String lSql = "SELECT * from " + liefereTblParameter() + " where " + "mandant=? OR mandant=0 "
                    + "ORDER BY ident, mandant;";
            lPStm = verbindung.prepareStatement(lSql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            if (nutzeSet) {
                lPStm.setInt(1, parameterSetIdent);
            } else {
                lPStm.setInt(1, dbBundle.clGlobalVar.mandant);
            }

            ResultSet lErgebnis = executeQuery(lPStm);
            lErgebnis.last();
            anzInArray = lErgebnis.getRow();
            lErgebnis.beforeFirst();

            ergebnisArray = new EclParameter[anzInArray];

            Boolean hBoolean = true;
            int hInt = 0;
            int i = 0;
            String hString=""; //Wird zum Umformen in mehreren Case-Bereichen benötigt
            while (lErgebnis.next() == true) {

                ergebnisArray[i] = this.decodeErgebnis(lErgebnis);

                switch (ergebnisArray[i].ident) {
                case 1:
                    ergHVParam.paramPortal.bestaetigenDialog = liefereErgInt(i);
                    break;
                case 2:
                    ergHVParam.paramPortal.quittungDialog = liefereErgInt(i);
                    break;
                case 3:
                    ergHVParam.paramPortal.passwortCaseSensitiv = liefereErgInt(i);
                    break;
                case 4:
                    ergHVParam.paramPortal.passwortMindestLaenge = liefereErgInt(i);
                    break;
                case 5:
                    ergHVParam.paramPortal.pNichtmarkiertSpeichernAls = liefereErgInt(i);
                    break;
                case 8:
                    ergHVParam.paramPortal.bestaetigenHinweisAktionaersportal = liefereErgInt(i);
                    break;
                case 9:
                    ergHVParam.paramPortal.bestaetigenHinweisHVportal = liefereErgInt(i);
                    break;
                case 10:
                    ergHVParam.paramPortal.zusaetzlicheEKDritteMoeglich = liefereErgInt(i);
                    break;
                case 11:
                    ergHVParam.paramPortal.ekUndWeisungGleichzeitigMoeglich = liefereErgInt(i);
                    break;
                case 13:
                    break;//GelöschtpWebServicesApp
                case 14:
                    break;//Gelöscht pLetzterAktienregisterImport
                case 16:
                    ergHVParam.paramPortal.erstregistrierungImmer = liefereErgInt(i);
                    break;
                case 17: {
                    for (int i1 = 0; i1 < 6; i1++) {
                        ergHVParam.paramAkkreditierung.pPraesenzStornierenAusSammelZwingend[i1] = Integer
                                .parseInt(ergebnisArray[i].wert.substring(i1, i1 + 1));
                    }
                    break;
                }

                /*Weitere Parameter zur Stimmkartenzuordnung siehe auch ab 700*/
                case 18: {
                    if (ergebnisArray[i].wert.length() == 25) {
                        for (int i1 = 0; i1 < 5; i1++) {
                            for (int i2 = 0; i2 < 5; i2++) {
                                int offset = i1 * 5 + i2;
                                ergHVParam.paramAkkreditierung.pPraesenzStimmkartenZuordnenGattung[i1][i2] = Integer
                                        .parseInt(ergebnisArray[i].wert.substring(offset, offset + 1));
                            }
                        }
                    } else {
                        CaBug.drucke("DbParameter.readHVParam_all - pPraesenzStimmkartenZuordnenGattung zu kurz");
                    }
                    break;
                }
                case 19: {
                    if (ergebnisArray[i].wert.length() == 25) {
                        for (int i1 = 0; i1 < 5; i1++) {
                            for (int i2 = 0; i2 < 5; i2++) {
                                int offset = i1 * 5 + i2;
                                ergHVParam.paramAkkreditierung.pPraesenzStimmkartenZuordnenAppGattung[i1][i2] = Integer
                                        .parseInt(ergebnisArray[i].wert.substring(offset, offset + 1));
                            }
                        }
                    } else {
                        CaBug.drucke("DbParameter.readHVParam_all - pPraesenzStimmkartenZuordnenAppGattung zu kurz");
                    }
                    break;
                }

                //				case 20:ergHVParam.paramAkkreditierung.pPraesenzStimmkartenZuordnenGattung1Text[1]=liefereErgString(i);break;
                //				case 21:ergHVParam.paramAkkreditierung.pPraesenzStimmkartenZuordnenGattung1Text[2]=liefereErgString(i);break;
                //				case 22:ergHVParam.paramAkkreditierung.pPraesenzStimmkartenZuordnenGattung1Text[3]=liefereErgString(i);break;

                case 23: {
                    if (ergebnisArray[i].wert.length() == 5) {
                        for (int i1 = 0; i1 < 5; i1++) {
                            ergHVParam.paramAkkreditierung.pPraesenzStimmkarteSecondZuordnenGattung[i1] = Integer
                                    .parseInt(ergebnisArray[i].wert.substring(i1, i1 + 1));
                        }
                    } else {
                        CaBug.drucke("DbParameter.readHVParam_all - pPraesenzStimmkarteSecondZuordnenGattung zu kurz");
                    }
                    break;
                }
                //				case 24:ergHVParam.paramAkkreditierung.pPraesenzStimmkarteSecondZuordnenGattung1Text=liefereErgString(i);break;

                //				case 25:{
                //				for (int i1=0;i1<4;i1++){
                //					ergHVParam.paramAkkreditierung.pPraesenzStimmkartenZuordnenGattung2[i1]=Integer.parseInt(ergebnisArray[i].wert.substring(i1, i1+1));
                //				}
                //				break;
                //				}
                //				case 26:ergHVParam.paramAkkreditierung.pPraesenzStimmkartenZuordnenGattung2Text[0]=liefereErgString(i);break;
                //				case 27:ergHVParam.paramAkkreditierung.pPraesenzStimmkartenZuordnenGattung2Text[1]=liefereErgString(i);break;
                //				case 28:ergHVParam.paramAkkreditierung.pPraesenzStimmkartenZuordnenGattung2Text[2]=liefereErgString(i);break;
                //				case 29:ergHVParam.paramAkkreditierung.pPraesenzStimmkartenZuordnenGattung2Text[3]=liefereErgString(i);break;
                //				case 30:ergHVParam.paramAkkreditierung.pPraesenzStimmkarteSecondZuordnenGattung2=liefereErgInt(i);break;
                //				case 31:ergHVParam.paramAkkreditierung.pPraesenzStimmkarteSecondZuordnenGattung2Text=liefereErgString(i);break;

                /*32 entfernt, ehemals pPraesenzStimmkartenZuordnenAuchBeiAppIdent*/
//                case 32:
//                    ergHVParam.paramAkkreditierung.pPraesenzStimmkartenZuordnenAuchBeiAppIdent = liefereErgInt(i);
//                    break;
                case 33:
                    ergHVParam.paramAkkreditierung.pPraesenzBeiErscheinenAndereVollmachtenStornieren = liefereErgInt(i);
                    break;
                case 34:
                    break;//Gelöscht:pDemoAkkreditierungAktiv

                /*werden automatisch aus den neuen Wegen belegt!*/
                //				case 35:ergHVParam.paramBasis.pGrundkapital=liefereErgLong(i);break;
                //				case 36:ergHVParam.paramBasis.pWertEinerAktie=liefereErgDouble(i);break;
                //				case 37:ergHVParam.paramBasis.pGrundkapitalVermindert=liefereErgLong(i);break;

                //				case 38:{
                //				for (int i1=0;i1<4;i1++){
                //					ergHVParam.paramAkkreditierung.pPraesenzStimmkartenZuordnenAppGattung1[i1]=Integer.parseInt(ergebnisArray[i].wert.substring(i1, i1+1));
                //				}
                //				break;
                //				}
                //				case 39:{
                //				for (int i1=0;i1<4;i1++){
                //					ergHVParam.paramAkkreditierung.pPraesenzStimmkartenZuordnenAppGattung2[i1]=Integer.parseInt(ergebnisArray[i].wert.substring(i1, i1+1));
                //				}
                //				break;
                //				}

                /*Ab hier sortiert (so halbwegs) :-) */
                /*************ParamModuleKonfigurierbar*********************************/
                //				case 40:ergHVParam.paramModuleKonfigurierbar.aktionaersportal=liefereErgBoolean(i);break; //Kommt aus EclEmittent
                case 41:
                    ergHVParam.paramModuleKonfigurierbar.elektronischesTeilnehmerverzeichnis = liefereErgBoolean(i);
                    break;
                case 42:
                    ergHVParam.paramModuleKonfigurierbar.tabletAbstimmung = liefereErgBoolean(i);
                    break;
                //				case 43:ergHVParam.paramModuleKonfigurierbar.hvApp=liefereErgBoolean(i);break; //Kommt aus EclEmittent
                case 44:
                    ergHVParam.paramModuleKonfigurierbar.hvAppAbstimmung = liefereErgBoolean(i);
                    break;
                case 45:
                    ergHVParam.paramModuleKonfigurierbar.elektronischerStimmblock = liefereErgBoolean(i);
                    break;
                case 46:
                    ergHVParam.paramModuleKonfigurierbar.onlineTeilnahme = liefereErgBoolean(i);
                    break;
                case 47:
                    ergHVParam.paramModuleKonfigurierbar.elektronischeWeisungserfassungHV = liefereErgBoolean(i);
                    break;
                case 48:
                    ergHVParam.paramModuleKonfigurierbar.englischeAgenda = liefereErgBoolean(i);
                    break;
                case 49:
                    ergHVParam.paramModuleKonfigurierbar.hvAppHVFunktionen = liefereErgBoolean(i);
                    break;
                case 50:
                    ergHVParam.paramModuleKonfigurierbar.briefwahl = liefereErgBoolean(i);
                    break;
                case 51:
                    ergHVParam.paramModuleKonfigurierbar.scannerAbstimmung = liefereErgBoolean(i);
                    break;
                case 52:
                    ergHVParam.paramModuleKonfigurierbar.weisungenSchnittstelleExternesSystem = liefereErgBoolean(i);
                    break;
                case 53:
                    ergHVParam.paramModuleKonfigurierbar.hvForm = liefereErgInt(i);
                    break;
                case 54:
                    ergHVParam.paramModuleKonfigurierbar.mehrereGattungen = liefereErgBoolean(i);
                    break;

                /********************************ParamBasis*****************************/
                case 60:
                    ergHVParam.paramBasis.inhaberaktienAktiv = liefereErgBoolean(i);
                    break;
                case 61:
                    ergHVParam.paramBasis.namensaktienAktiv = liefereErgBoolean(i);
                    break;
                case 62: {
                    for (int i1 = 0; i1 < 5; i1++) {
                        if (ergebnisArray[i].wert.length() > i1) {
                            hInt = Integer.parseInt(ergebnisArray[i].wert.substring(i1, i1 + 1));
                            if (hInt == 1) {
                                hBoolean = true;
                            } else {
                                hBoolean = false;
                            }
                            ergHVParam.paramBasis.gattungAktiv[i1] = hBoolean;
                        }
                    }
                    break;
                }
                case 63:
                case 64:
                case 65:
                case 66:
                case 67:
                    ergHVParam.paramBasis.gattungBezeichnung[ergebnisArray[i].ident - 63] = liefereErgString(i);
                    break;
                case 68:
                case 69:
                case 70:
                case 71:
                case 72:
                    ergHVParam.paramBasis.gattungBezeichnungKurz[ergebnisArray[i].ident - 68] = liefereErgString(i);
                    break;
                case 73:
                    ergHVParam.paramBasis.laengeAktionaersnummer = liefereErgInt(i);
                    break;

                case 74:
                    ergHVParam.paramBasis.zweiterMandantNr = liefereErgInt(i);
                    break;
                case 75:
                    ergHVParam.paramBasis.zweiterMandantHVJahr = liefereErgInt(i);
                    break;
                case 76:
                    ergHVParam.paramBasis.zweiterMandantHVNummer = liefereErgString(i);
                    break;
                case 77:
                    ergHVParam.paramBasis.zweiterMandantDatenbereich = liefereErgString(i);
                    break;
                case 78:
                    ergHVParam.paramBasis.investorenSind = liefereErgInt(i);
                    break;
                case 79:
                    ergHVParam.paramBasis.ekSeitenzahl = liefereErgInt(i);
                    break;

                /*****************Nummernformen*******************************************/
                case 80:
                    ergHVParam.paramNummernformen.ident = liefereErgInt(i);
                    break;
                case 81:
                    ergHVParam.paramPruefzahlen.identifikationsNummer = liefereErgString(i);
                    ergHVParam.paramPruefzahlen.laengeIdentifikationsNummer = ergHVParam.paramPruefzahlen.identifikationsNummer
                            .length();
                    break;
                case 82:
                    ergHVParam.paramPruefzahlen.dreistelligeKontrollzahl = liefereErgString(i);
                    break;
                case 83:
                    ergHVParam.paramPruefzahlen.zweistelligeKontrollzahl = liefereErgString(i);
                    break;
                case 84:
                    ergHVParam.paramPruefzahlen.einstelligeKontrollzahl = liefereErgString(i);
                    break;
                case 85:
                    ergHVParam.paramNummernformen.ignoriereKartenart = liefereErgInt(i);
                    break;

                /******ParamBasis - Fortsetzung**********/
                case 86:
                    ergHVParam.paramBasis.ohneNullAktionaersnummer = liefereErgInt(i);
                    break;
                case 87:
                    ergHVParam.paramBasis.eindeutigeHVKennung = liefereErgString(i);
                    break;
                case 88:
                    ergHVParam.paramBasis.isin[0] = liefereErgString(i);
                    break;
                case 89:
                    ergHVParam.paramBasis.isin[1] = liefereErgString(i);
                    break;
                case 90:
                    ergHVParam.paramBasis.isin[2] = liefereErgString(i);
                    break;
                case 91:
                    ergHVParam.paramBasis.isin[3] = liefereErgString(i);
                    break;
                case 92:
                    ergHVParam.paramBasis.isin[4] = liefereErgString(i);
                    break;
                case 93:
                case 94:
                case 95:
                case 96:
                case 97:
                    ergHVParam.paramBasis.gattungBezeichnungEN[ergebnisArray[i].ident - 93] = liefereErgString(i);
                    break;
                case 98:
                    ergHVParam.paramBasis.ekFormularGetrenntJeVersandweg = liefereErgInt(i);
                    break;

//                case 99:
//                    ergHVParam.paramBasis.veranstaltungstyp = liefereErgInt(i);
//                    break;
                    /*Siehe 2670 bis 2679*/

                /*********************ParameterAbstimmungParameter*****************************/
                case 100:
                    ergHVParam.paramAbstimmungParameter.abstimmenDuerfen_nurPraesente_PraesenteUndGewesene = liefereErgInt(
                            i);
                    break;
                case 101:
                    ergHVParam.paramAbstimmungParameter.priorisierung_ElektronischVorPapier_PapierVorElektronisch = liefereErgInt(
                            i);
                    break;
                case 102:
                    ergHVParam.paramAbstimmungParameter.beiMehrfacherStimmabgabe_letzteAbgabeGilt_ungleicheUngueltig = liefereErgInt(
                            i);
                    break;
                case 103:
                    ergHVParam.paramAbstimmungParameter.beiAbstimmungEintrittskartennummerZulaessig = liefereErgInt(i);
                    break;
                case 104:
                    ergHVParam.paramAbstimmungParameter.beiTabletAbstimmungNurAufgerufeneStimmZettelZulaessig = liefereErgInt(
                            i);
                    break;
                case 105:
                    ergHVParam.paramAbstimmungParameter.in100PZAuchEnthaltungen = liefereErgInt(i);
                    break;
                case 106:
                    ergHVParam.paramAbstimmungParameter.in100PZAuchUngueltige = liefereErgInt(i);
                    break;
                case 107:
                    ergHVParam.paramAbstimmungParameter.in100PZAuchNichtStimmberechtigte = liefereErgInt(i);
                    break;
                case 108:
                    ergHVParam.paramAbstimmungParameter.in100PZAuchNichtTeilnahme = liefereErgInt(i);
                    break;
                case 109:
                    ergHVParam.paramAbstimmungParameter.abstimmenTabletNichtPraesenteWerdenPraesentGesetzt = liefereErgInt(
                            i);
                    break;
                case 110:
                    ergHVParam.paramAbstimmungParameter.beiTabletAllesJa = liefereErgInt(i);
                    break;
                case 111:
                    ergHVParam.paramAbstimmungParameter.beiTabletAllesNein = liefereErgInt(i);
                    break;
                case 112:
                    ergHVParam.paramAbstimmungParameter.beiTabletAllesEnthaltung = liefereErgInt(i);
                    break;
                case 113:
                    ergHVParam.paramAbstimmungParameter.beiTabletAbstimmungBlaettern = liefereErgInt(i);
                    break;

                case 131:
                    ergHVParam.paramAbstimmungParameter.anzahlJaJeAbstimmungsgruppe[1] = liefereErgInt(i);
                    break;
                case 132:
                    ergHVParam.paramAbstimmungParameter.anzahlJaJeAbstimmungsgruppe[2] = liefereErgInt(i);
                    break;
                case 133:
                    ergHVParam.paramAbstimmungParameter.anzahlJaJeAbstimmungsgruppe[3] = liefereErgInt(i);
                    break;
                case 134:
                    ergHVParam.paramAbstimmungParameter.anzahlJaJeAbstimmungsgruppe[4] = liefereErgInt(i);
                    break;
                case 135:
                    ergHVParam.paramAbstimmungParameter.anzahlJaJeAbstimmungsgruppe[5] = liefereErgInt(i);
                    break;
                case 136:
                    ergHVParam.paramAbstimmungParameter.anzahlJaJeAbstimmungsgruppe[6] = liefereErgInt(i);
                    break;
                case 137:
                    ergHVParam.paramAbstimmungParameter.anzahlJaJeAbstimmungsgruppe[7] = liefereErgInt(i);
                    break;
                case 138:
                    ergHVParam.paramAbstimmungParameter.anzahlJaJeAbstimmungsgruppe[8] = liefereErgInt(i);
                    break;
                case 139:
                    ergHVParam.paramAbstimmungParameter.anzahlJaJeAbstimmungsgruppe[9] = liefereErgInt(i);
                    break;
                case 140:
                    ergHVParam.paramAbstimmungParameter.anzahlJaJeAbstimmungsgruppe[10] = liefereErgInt(i);
                    break;
                case 141:
                    ergHVParam.paramAbstimmungParameter.beiTabletGesamtmarkierungTextAnzeigen = liefereErgInt(i);
                    break;
                case 142:
                    ergHVParam.paramAbstimmungParameter.auswertenPPListeZeilen = liefereErgInt(i);
                    break;
                case 143:
                    ergHVParam.paramAbstimmungParameter.beiTabletGesamtmarkierungFuerAlle = liefereErgInt(i);
                    break;
                case 144:
                    ergHVParam.paramAbstimmungParameter.beiTabletTextKurzOderLang = liefereErgInt(i);
                    break;
                /*Fortsetzung bei 2000*/

                /******************Portal-Parameter***************************************************/
                //				case 150:ergHVParam.paramPortal.portalAuchInEnglischVerfuegbar=liefereErgInt(i);break;
                case 151:
                    ergHVParam.paramPortal.personenAnzeigeAnredeMitAufnahmen = liefereErgInt(i);
                    break;
                case 152:
                    ergHVParam.paramPortal.textPostfachMitAufnahmen = liefereErgInt(i);
                    break;
                case 153:
                    ergHVParam.paramPortal.letzterAktienregisterUpdate = liefereErgString(i);
                    break;
                case 154:
                    ergHVParam.paramPortal.registrierungFuerEmailVersandMoeglich = liefereErgInt(i);
                    break;
                case 155:
                    ergHVParam.paramPortal.gewinnspielAktiv = liefereErgInt(i);
                    break;
                case 156:
                    ergHVParam.paramPortal.separateTeilnahmebedingungenFuerGewinnspiel = liefereErgInt(i);
                    break;
                case 157:
                    ergHVParam.paramPortal.dauerhaftesPasswortMoeglich = liefereErgInt(i);
                    break;
                case 158:
                    ergHVParam.paramPortal.separateDatenschutzerklaerung = liefereErgInt(i);
                    break;
                case 159:
                    ergHVParam.paramPortal.separateNutzungshinweise = liefereErgInt(i);
                    break;
                case 160:
                    ergHVParam.paramPortal.bestaetigungSeparateAktionaersPortalHinweiseErforderlich = liefereErgInt(i);
                    break;
                case 161:
                    ergHVParam.paramPortal.bestaetigungSeparateHVPortalHinweiseErforderlich = liefereErgInt(i);
                    break;

                case 162:
                    ergHVParam.paramPortal.gesamtMarkierungJa = liefereErgInt(i);
                    break;
                case 163:
                    ergHVParam.paramPortal.gesamtMarkierungNein = liefereErgInt(i);
                    break;
                case 164:
                    ergHVParam.paramPortal.gesamtMarkierungImSinne = liefereErgInt(i);
                    break;
                case 165:
                    ergHVParam.paramPortal.gesamtMarkierungGegenSinne = liefereErgInt(i);
                    break;
                case 166:
                    ergHVParam.paramPortal.markierungJa = liefereErgInt(i);
                    break;
                case 167:
                    ergHVParam.paramPortal.markierungNein = liefereErgInt(i);
                    break;
                case 168:
                    ergHVParam.paramPortal.markierungEnthaltung = liefereErgInt(i);
                    break;
                case 169:
                    ergHVParam.paramPortal.markierungLoeschen = liefereErgInt(i);
                    break;
//                  Gibts nicht mehr
//                    case 170:
//                    ergHVParam.paramPortal.markierungGegenJa = liefereErgInt(i);
//                    break;
//                case 171:
//                    ergHVParam.paramPortal.markierungGegenNein = liefereErgInt(i);
//                    break;
//                case 172:
//                    ergHVParam.paramPortal.markierungGegenEnthaltung = liefereErgInt(i);
//                    break;
//                case 173:
//                    ergHVParam.paramPortal.markierungGegenLoeschen = liefereErgInt(i);
//                    break;

                case 174:
                    ergHVParam.paramPortal.gastkartenAnforderungMoeglich = liefereErgInt(i);
                    break;
                case 175:
                    ergHVParam.paramPortal.oeffentlicheIDMoeglich = liefereErgInt(i);
                    break;

                case 176:
                    ergHVParam.paramPortal.ekSelbstMoeglich = liefereErgInt(i);
                    break;
                case 177:
                    ergHVParam.paramPortal.ekVollmachtMoeglich = liefereErgInt(i);
                    break;
                case 178:
                    ergHVParam.paramPortal.ek2PersonengemeinschaftMoeglich = liefereErgInt(i);
                    break;
                case 179:
                    ergHVParam.paramPortal.ek2MitOderOhneVollmachtMoeglich = liefereErgInt(i);
                    break;
                case 180:
                    ergHVParam.paramPortal.ek2SelbstMoeglich = liefereErgInt(i);
                    break;

                case 181:
                    ergHVParam.paramPortal.verfahrenPasswortVergessen = liefereErgInt(i);
                    break;
                /*182-183: Local*/
                case 184:
                    ergHVParam.paramPortal.anzeigeStartseite = liefereErgInt(i);
                    break;
                case 185:
                    ergHVParam.paramPortal.gesamtMarkierungEnthaltung = liefereErgInt(i);
                    break;
                case 186:
                    ergHVParam.paramPortalServer.statusOnlineTicket = liefereErgInt(i);
                    break;

                case 187:
                    ergHVParam.paramPortal.briefwahlAngeboten = liefereErgInt(i);
                    break;
                case 188:
                    ergHVParam.paramPortal.vollmachtDritteAngeboten = liefereErgInt(i);
                    break;
                case 189:
                    ergHVParam.paramPortal.vollmachtKIAVAngeboten = liefereErgInt(i);
                    break;
                case 190: {
                    for (int i1 = 0; i1 <= 4; i1++) {
                        ergHVParam.paramPortal.portalFuerGattungMoeglich[i1] = Integer
                                .parseInt(ergebnisArray[i].wert.substring(i1, i1 + 1));
                    }
                    break;
                }
                case 191: {
                    for (int i1 = 0; i1 <= 4; i1++) {
                        ergHVParam.paramPortal.stimmabgabeFuerGattungMoeglich[i1] = Integer
                                .parseInt(ergebnisArray[i].wert.substring(i1, i1 + 1));
                    }
                    break;
                }
                case 192:
                    ergHVParam.paramPortal.phasePortal = liefereErgInt(i);
                    break;
                case 193:
                    ergHVParam.paramPortal.datumsformatDE = liefereErgInt(i);
                    break;
                case 194:
                    ergHVParam.paramPortal.datumsformatEN = liefereErgInt(i);
                    break;
                //				case 195:ergHVParam.paramPortal.loginInhaberaktien=liefereErgInt(i);break;
                case 196:
                    ergHVParam.paramPortal.gesamtMarkierungAllesLoeschen = liefereErgInt(i);
                    break;
                case 197:
                    ergHVParam.paramPortal.gegenantraegeWeisungenMoeglich = liefereErgInt(i);
                    break;
//                gibts nicht mehr
//                    case 198:
//                    ergHVParam.paramPortal.markierungGegenUnterstuetzen = liefereErgInt(i);
//                    break;
                case 199:
                    ergHVParam.paramPortal.gegenantragsText = liefereErgInt(i);
                    break;
                case 200:
                    ergHVParam.paramPortal.mehrereStimmrechtsvertreter = liefereErgInt(i);
                    break;
//                case 201:
//                case 202:
//                case 203:
//                case 204:
//                case 205:
//                case 206:
//                case 207:
//                case 208:
//                case 209:
//                case 210:
//                case 211:
//                case 212:
//                case 213:
//                case 214:
//                case 215:
//                case 216:
//                case 217:
//                case 218:
//                case 219:
//                case 220: {
//                    int tempL = ergebnisArray[i].wert.length();
//                    for (int i1 = 0; i1 <= 19; i1++) {
//                        if (tempL > i1) {
//                            ergHVParam.paramPortal.phasenDetails[ergebnisArray[i].ident - 200][i1] = Integer
//                                    .parseInt(ergebnisArray[i].wert.substring(i1, i1 + 1));
//                        } else {
//                            ergHVParam.paramPortal.phasenDetails[ergebnisArray[i].ident - 200][i1] = 0;
//                        }
//                    }
//                    break;
//                }
                case 221:
                case 222:
                case 223:
                case 224:
                case 225:
                case 226:
                case 227:
                case 228:
                case 229:
                case 230:
                case 231:
                case 232:
                case 233:
                case 234:
                case 235:
                case 236:
                case 237:
                case 238:
                case 239:
                case 240:
                    ergHVParam.paramPortalServer.phasenNamen[ergebnisArray[i].ident - 220] = liefereErgString(i);
                    break;
                case 241:
                    ergHVParam.paramPortal.adressaenderungMoeglich = liefereErgInt(i);
                    break;
                case 242:
                    ergHVParam.paramPortal.artSprachumschaltung = liefereErgInt(i);
                    break;
                case 243:
                    ergHVParam.paramPortalServer.appInstallButtonsAnzeigen = liefereErgInt(i);
                    break;
                case 244:
                    ergHVParam.paramPortal.kommunikationsspracheAuswahl = liefereErgInt(i);
                    break;
                case 245:
                    ergHVParam.paramPortal.publikationenAnbieten = liefereErgInt(i);
                    break;
                case 246:
                    ergHVParam.paramPortal.kontaktDetailsAnbieten = liefereErgInt(i);
                    break;
                case 247:
                    ergHVParam.paramPortal.kontaktFenster = liefereErgInt(i);
                    break;
                case 248:
                    ergHVParam.paramPortal.standardTexteBeruecksichtigen = liefereErgInt(i);
                    break;
                case 249:
                    ergHVParam.paramPortal.impressumEmittent = liefereErgInt(i);
                    break;
                case 250:
                    ergHVParam.paramPortal.emailNurBeiEVersandOderPasswort = liefereErgInt(i);
                    break;
                case 251:
                    ergHVParam.paramPortal.varianteDialogablauf = liefereErgInt(i);
                    break;
                case 252:
                    ergHVParam.paramPortal.loginGesperrt = liefereErgInt(i);
                    break;
                case 253:
                    ergHVParam.paramPortal.loginIPTrackingAktiv = liefereErgInt(i);
                    break;
                case 254:
                    ergHVParam.paramPortal.lfdHVDialogVeranstaltungenInMenue = liefereErgInt(i);
                    break;
                case 255:
                    ergHVParam.paramPortal.checkboxBeiSRV = liefereErgInt(i);
                    break;
                case 256:
                    ergHVParam.paramPortal.checkboxBeiKIAV = liefereErgInt(i);
                    break;
                case 257:
                    ergHVParam.paramPortal.checkboxBeiBriefwahl = liefereErgInt(i);
                    break;
                case 258:
                    ergHVParam.paramPortal.bestaetigungsseiteEinstellungen = liefereErgInt(i);
                    break;
                case 259:
                    ergHVParam.paramPortal.verfahrenPasswortVergessenAblauf = liefereErgInt(i);
                    break;
                case 260:
                    ergHVParam.paramPortal.absendeMailAdresse = liefereErgInt(i);
                    break;

                case 261:
                    ergHVParam.paramPortal.lfdHVGeneralversammlungInMenue = liefereErgInt(i);
                    break;
                case 262:
                    ergHVParam.paramPortal.lfdHVUnterlagenInMenue = liefereErgInt(i);
                    break;
                case 263:
                    ergHVParam.paramPortal.lfdHVEinstellungenInMenue = liefereErgInt(i);
                    break;
                case 264:
                    ergHVParam.paramPortal.lfdHVGeneralversammlungBriefwahlInMenue = liefereErgInt(i);
                    break;
                case 265:
                    ergHVParam.paramPortal.reihenfolgeRegistrierung = liefereErgInt(i);
                    break;

                case 266:
                    ergHVParam.paramPortal.passwortPerPostPruefen = liefereErgInt(i);
                    break;
                case 267:
                    ergHVParam.paramPortal.basisSetStandardTexteVerwenden = liefereErgInt(i);
                    break;
                case 268:
                    ergHVParam.paramPortal.srvAngeboten = liefereErgInt(i);
                    break;

                case 269:
                    ergHVParam.paramPortal.logoName = liefereErgString(i);
                    break;
                case 270:
                    ergHVParam.paramPortal.logoBreite = liefereErgInt(i);
                    break;
                case 271:
                    ergHVParam.paramPortal.logoHoehe = liefereErgInt(i);
                    break;
                case 272:
                    ergHVParam.paramPortal.cssName = liefereErgString(i);
                    break;
                case 273:
                    ergHVParam.paramPortal.designKuerzel = liefereErgString(i);
                    break;
                case 274:
                    ergHVParam.paramPortal.anzeigeStimmen = liefereErgInt(i);
                    break;
                case 275:
                    ergHVParam.paramPortal.anmeldenOhneWeitereWK = liefereErgInt(i);
                    break;
                case 276:
                    ergHVParam.paramPortal.logoutObenOderUnten = liefereErgInt(i);
                    break;

                case 277:
                    ergHVParam.paramPortal.lfdHVGeneralversammlungTeilnahmeInMenue = liefereErgInt(i);
                    break;

                case 278:
                    ergHVParam.paramPortal.loginVerfahren = liefereErgInt(i);
                    break;
                case 279:
                    ergHVParam.paramPortal.bestaetigungBeiWeisung = liefereErgInt(i);
                    break;

                case 280:
                    ergHVParam.paramPortal.lfdHVGeneralversammlungTeilnahmeGast = liefereErgInt(i);
                    break;

                case 281:
                    ergHVParam.paramPortal.lfdHVBeiratswahlInMenue = liefereErgInt(i);
                    break;
                case 282:
                    ergHVParam.paramPortal.captchaVerwenden = liefereErgInt(i);
                    break;
                case 283:
                    ergHVParam.paramPortal.loginVerzoegerungAbVersuch = liefereErgInt(i);
                    break;
                case 284:
                    ergHVParam.paramPortal.alternativeLoginKennung = liefereErgInt(i);
                    break;
                case 285:
                    ergHVParam.paramPortal.teilnehmerKannSichWeitereKennungenZuordnen = liefereErgInt(i);
                    break;
                case 286:
                    ergHVParam.paramPortal.bestaetigungPerEmailUeberallZulassen = liefereErgInt(i);
                    break;
                case 287:
                    ergHVParam.paramPortal.loginVerzoegerungSekunden = liefereErgInt(i);
                    break;
                case 288:
                    ergHVParam.paramPortal.kennungAufbereiten = liefereErgInt(i);
                    break;
                case 289:
                    ergHVParam.paramPortal.kennungAufbereitenFuerAnzeige = liefereErgInt(i);
                    break;

                case 290:
                    ergHVParam.paramPortal.checkboxBeiVollmacht = liefereErgInt(i);
                    break;

                /******************Gästemodul*************************************************************************/
                case 300:
                    ergHVParam.paramGaesteModul.mailVerschickenGK = liefereErgInt(i);
                    break;
                /*301 bis 304: local*/
                case 305:
                    ergHVParam.paramGaesteModul.feldAnredeVerwenden = liefereErgInt(i);
                    break;
                case 306:
                    ergHVParam.paramGaesteModul.feldTitelVerwenden = liefereErgInt(i);
                    break;
                case 307:
                    ergHVParam.paramGaesteModul.feldAdelstitelVerwenden = liefereErgInt(i);
                    break;
                case 308:
                    ergHVParam.paramGaesteModul.feldNameVerwenden = liefereErgInt(i);
                    break;
                case 309:
                    ergHVParam.paramGaesteModul.feldVornameVerwenden = liefereErgInt(i);
                    break;
                case 310:
                    ergHVParam.paramGaesteModul.feldZuHaendenVerwenden = liefereErgInt(i);
                    break;
                case 311:
                    ergHVParam.paramGaesteModul.feldZusatz1Verwenden = liefereErgInt(i);
                    break;
                case 312:
                    ergHVParam.paramGaesteModul.feldZusatz2Verwenden = liefereErgInt(i);
                    break;
                case 313:
                    ergHVParam.paramGaesteModul.feldStrasseVerwenden = liefereErgInt(i);
                    break;
                case 314:
                    ergHVParam.paramGaesteModul.feldLandVerwenden = liefereErgInt(i);
                    break;
                case 315:
                    ergHVParam.paramGaesteModul.feldPLZVerwenden = liefereErgInt(i);
                    break;
                case 316:
                    ergHVParam.paramGaesteModul.feldOrtVerwenden = liefereErgInt(i);
                    break;
                case 317:
                    ergHVParam.paramGaesteModul.feldMailadresseVerwenden = liefereErgInt(i);
                    break;
                case 318:
                    ergHVParam.paramGaesteModul.feldKommunikationsspracheVerwenden = liefereErgInt(i);
                    break;
                case 319:
                    ergHVParam.paramGaesteModul.feldGruppeVerwenden = liefereErgInt(i);
                    break;
                case 320:
                    ergHVParam.paramGaesteModul.feldAusstellungsgrundVerwenden = liefereErgInt(i);
                    break;
                case 321:
                    ergHVParam.paramGaesteModul.feldVipVerwenden = liefereErgInt(i);
                    break;

                case 322:
                    ergHVParam.paramGaesteModul.feldZuHaendenBezeichnung = liefereErgString(i);
                    break;
                case 323:
                    ergHVParam.paramGaesteModul.feldZusatz1Bezeichnung = liefereErgString(i);
                    break;
                case 324:
                    ergHVParam.paramGaesteModul.feldZusatz2Bezeichnung = liefereErgString(i);
                    break;

                case 325:
                    ergHVParam.paramGaesteModul.buttonSpeichernAnzeigen = liefereErgInt(i);
                    break;
                case 326:
                    ergHVParam.paramGaesteModul.buttonSpeichernDruckenAnzeigen = liefereErgInt(i);
                    break;

                    
                case 327:
                    ergHVParam.paramGaesteModul.feldAnredeVerwendenPortal = liefereErgInt(i);
                    break;
                case 328:
                    ergHVParam.paramGaesteModul.feldTitelVerwendenPortal = liefereErgInt(i);
                    break;
                case 329:
                    ergHVParam.paramGaesteModul.feldAdelstitelVerwendenPortal = liefereErgInt(i);
                    break;
                case 330:
                    ergHVParam.paramGaesteModul.feldNameVerwendenPortal = liefereErgInt(i);
                    break;
                case 331:
                    ergHVParam.paramGaesteModul.feldVornameVerwendenPortal = liefereErgInt(i);
                    break;
                case 332:
                    ergHVParam.paramGaesteModul.feldZuHaendenVerwendenPortal = liefereErgInt(i);
                    break;
                case 333:
                    ergHVParam.paramGaesteModul.feldZusatz1VerwendenPortal = liefereErgInt(i);
                    break;
                case 334:
                    ergHVParam.paramGaesteModul.feldZusatz2VerwendenPortal = liefereErgInt(i);
                    break;
                case 335:
                    ergHVParam.paramGaesteModul.feldStrasseVerwendenPortal = liefereErgInt(i);
                    break;
                case 336:
                    ergHVParam.paramGaesteModul.feldLandVerwendenPortal = liefereErgInt(i);
                    break;
                case 337:
                    ergHVParam.paramGaesteModul.feldPLZVerwendenPortal = liefereErgInt(i);
                    break;
                case 338:
                    ergHVParam.paramGaesteModul.feldOrtVerwendenPortal = liefereErgInt(i);
                    break;
                case 339:
                    ergHVParam.paramGaesteModul.feldMailadresseVerwendenPortal = liefereErgInt(i);
                    break;
                case 340:
                    ergHVParam.paramGaesteModul.feldKommunikationsspracheVerwendenPortal = liefereErgInt(i);
                    break;
 
                    
                /*********************Akkreditierung*************************************************/
                case 400:
                    ergHVParam.paramAkkreditierung.gaesteKartenHabenWiederzugangAbgangsCode = liefereErgBoolean(i);
                    break;
                case 401:
                    ergHVParam.paramAkkreditierung.eintrittskarteWirdAuchFuerAbgangWiederzugangVerwendet = liefereErgBoolean(
                            i);
                    break;
                case 402:
                    ergHVParam.paramAkkreditierung.eintrittskarteWirdStimmkarte = liefereErgBoolean(i);
                    break;
                case 403:
                    ergHVParam.paramAkkreditierung.labelAuchFuerAppIdent = liefereErgBoolean(i);
                    break;
                case 404:
                    ergHVParam.paramAkkreditierung.positionVertretername = liefereErgInt(i);
                    break;
                case 405:
                    ergHVParam.paramAkkreditierung.delayArt = liefereErgInt(i);
                    break;
                case 406:
                    ergHVParam.paramAkkreditierung.beiZugangStornierenAusSammelAutomatisch = liefereErgInt(i);
                    break;

                case 407:
                    ergHVParam.paramAkkreditierung.skOffenlegungSRV = liefereErgInt(i);
                    break;
                case 408:
                    ergHVParam.paramAkkreditierung.skOffenlegungKIAV = liefereErgInt(i);
                    break;
                case 409:
                    ergHVParam.paramAkkreditierung.skOffenlegungDauer = liefereErgInt(i);
                    break;
                case 410:
                    ergHVParam.paramAkkreditierung.skOffenlegungOrga = liefereErgInt(i);
                    break;
                case 411:
                    ergHVParam.paramAkkreditierung.formularNachErstzugang = liefereErgInt(i);
                    break;
                case 412:
                    ergHVParam.paramAkkreditierung.zusaetzlichesInitialpasswortBeiErstzugang = liefereErgInt(i);
                    break;
                case 413:
                    ergHVParam.paramAkkreditierung.serviceDeskSetNr=liefereErgInt(i);
                    break;
                case 414:
                    ergHVParam.paramAkkreditierung.label_Font=liefereErgInt(i);
                    break;
                case 415:
                    ergHVParam.paramAkkreditierung.labeldruckVerfahren=liefereErgInt(i);
                    break;

                /***********Ehemals parameterLfd-Table. Neue Nummer=alte Nummer+500***********************/
                case 501:
                    ergHVParam.paramPortal.lfdHVPortalInBetrieb = liefereErgInt(i);
                    break;
                case 502:
                    ergHVParam.paramPortal.lfdHVPortalErstanmeldungIstMoeglich = liefereErgInt(i);
                    break;
                case 503:
                    ergHVParam.paramPortal.lfdHVPortalEKIstMoeglich = liefereErgInt(i);
                    break;
                case 504:
                    ergHVParam.paramPortal.lfdHVPortalSRVIstMoeglich = liefereErgInt(i);
                    break;
                case 505:
                    ergHVParam.paramPortal.lfdHVPortalBriefwahlIstMoeglich = liefereErgInt(i);
                    break;
                case 506:
                    ergHVParam.paramPortal.lfdHVPortalKIAVIstMoeglich = liefereErgInt(i);
                    break;
                case 507:
                    ergHVParam.paramPortal.lfdHVPortalVollmachtDritteIstMoeglich = liefereErgInt(i);
                    break;

                case 508: {
                    for (int i1 = 0; i1 < 6; i1++) {
                        ergHVParam.paramAkkreditierung.pLfdPraesenzStornierenAusSammelMoeglich[i1] = Integer
                                .parseInt(ergebnisArray[i].wert.substring(i1, i1 + 1));
                    }
                    break;
                }
                case 509: {
                    for (int i1 = 0; i1 < 6; i1++) {
                        ergHVParam.paramAkkreditierung.pLfdPraesenzDeaktivierenAusSammelMoeglich[i1] = Integer
                                .parseInt(ergebnisArray[i].wert.substring(i1, i1 + 1));
                    }
                    break;
                }
                case 510: {
                    for (int i1 = 0; i1 < 6; i1++) {
                        ergHVParam.paramAkkreditierung.pLfdPraesenzErteilenInSammelMoeglich[i1] = Integer
                                .parseInt(ergebnisArray[i].wert.substring(i1, i1 + 1));
                    }
                    break;
                }
                //				case 511:ergHVParam.parameterAlt.pLfdPraesenzDelayed=liefereErgInt(i);break;
                case 512:
                    ergHVParam.paramAkkreditierung.plfdHVGestartet = liefereErgInt(i);
                    break;

                case 513:
                    ergHVParam.paramAkkreditierung.plfdHVDelayed = liefereErgInt(i);
                    break;
                case 514:
                    ergHVParam.paramPortalServer.pLfdPortalStartNr = liefereErgInt(i);
                    break;
                case 515:
                    ergHVParam.paramPortal.lfdVorDerHVNachDerHV = liefereErgInt(i);
                    break;

                /***********************600 bis 699: Grundkapital sowie andere Gattungsabhängige Parameter**********************************/
                case 600:
                case 601:
                case 602:
                case 603:
                case 604: {
                    ergHVParam.paramBasis.grundkapitalStueck[ergebnisArray[i].ident - 600] = liefereErgLong(i);
                    //					System.out.println("Parameter lesen i="+i+"ergHVParam.paramBasis.grundkapital[i-600]="+ergHVParam.paramBasis.grundkapital[i-600]);
                    break;
                }
                case 605:
                case 606:
                case 607:
                case 608:
                case 609: {
                    ergHVParam.paramBasis.grundkapitalVermindertStueck[ergebnisArray[i].ident - 605] = liefereErgLong(i);
                    break;
                }
                case 610:
                case 611:
                case 612:
                case 613:
                case 614: {
                    ergHVParam.paramBasis.wertEinerAktie[ergebnisArray[i].ident - 610] = liefereErgDouble(i);
                    break;
                }
                case 615:
                case 616:
                case 617:
                case 618:
                case 619: {
                    ergHVParam.paramBasis.anzahlNachkommastellenKapital[ergebnisArray[i].ident - 615] = liefereErgInt(
                            i);
                    break;
                }
                case 620:
                case 621:
                case 622:
                case 623:
                case 624: {
                    ergHVParam.paramBasis.eintrittskarteNeuVergeben[ergebnisArray[i].ident - 620] = liefereErgBoolean(
                            i);
                    break;
                }

                case 625:
                case 626:
                case 627:
                case 628:
                case 629: {
                    ergHVParam.paramBasis.zugangMoeglich[ergebnisArray[i].ident - 625] = liefereErgBoolean(
                            i);
                    break;
                }
                
                case 630:
                case 631:
                case 632:
                case 633:
                case 634: {
                    ergHVParam.paramBasis.grundkapitalEigeneAktienStueck[ergebnisArray[i].ident - 630] = liefereErgLong(i);
                    break;
                }


                /*******700: Stimmblockzuordnung********************************
                 * siehe außerhalb des Case
                 */

                /***************800: Präsenzliste*************************
                 * siehe außerhalb des Case*/

                /*****************900: Bestandsverwaltung********************************/

                case 900:
                case 901:
                case 902:
                case 903:
                case 904:
                case 905:
                case 906:
                    hString = liefereErgString(i);
                    int ident = ergebnisArray[i].ident;
                    int startzeile = (ident - 900) * 3;
                    for (int zeile1 = 0; zeile1 < 3; zeile1++) {
                        for (int spalte2 = 0; spalte2 < 4; spalte2++) {
                            String hString1 = hString.substring(zeile1 * 12 + spalte2 * 3,
                                    zeile1 * 12 + spalte2 * 3 + 3);
                            int abstimmIdent = Integer.parseInt(hString1);
                            ergHVParam.paramBestandsverwaltung.weisungQS[startzeile + zeile1][spalte2] = abstimmIdent;
                        }
                    }
                    break;

                /***********************1000 bis 1500 reserviert für ablaufAbstimmung***************/
                /*1000 = Anzahl der Steuerelemente*/
                case 1000:
                    int anz = liefereErgInt(i);
                    ergHVParam.paramAbstimmungParameter.ablaufAbstimmung = new int[anz];
                    ergHVParam.paramAbstimmungParameter.ablaufAbstimmungErledigt = new boolean[anz];
                    break;

                /********************2000 bis 2200 Fortsetzung Abstimmungsparameter*********************************/
                case 2000:
                    ergHVParam.paramAbstimmungParameter.weisungNichtMarkierteSpeichernAlsSRV = liefereErgInt(i);
                    break;
                case 2001:
                    ergHVParam.paramAbstimmungParameter.weisungNichtMarkierteSpeichernAlsBriefwahl = liefereErgInt(i);
                    break;
                case 2002:
                    ergHVParam.paramAbstimmungParameter.weisungNichtMarkierteSpeichernAlsKIAV = liefereErgInt(i);
                    break;
                case 2003:
                    ergHVParam.paramAbstimmungParameter.weisungNichtMarkierteSpeichernAlsDauer = liefereErgInt(i);
                    break;
                case 2004:
                    ergHVParam.paramAbstimmungParameter.weisungNichtMarkierteSpeichernAlsOrg = liefereErgInt(i);
                    break;

                case 2005:
                    ergHVParam.paramAbstimmungParameter.weisungHVNichtMarkierteSpeichernAls = liefereErgInt(i);
                    break;
                case 2006:
                    ergHVParam.paramAbstimmungParameter.abstimmungNichtMarkierteSpeichern = liefereErgInt(i);
                    break;

                case 2007:
                    ergHVParam.paramAbstimmungParameter.weisungVorbelegungMitSRV = liefereErgInt(i);
                    break;
                case 2008:
                    ergHVParam.paramAbstimmungParameter.weisungVorbelegungMitBriefwahl = liefereErgInt(i);
                    break;
                case 2009:
                    ergHVParam.paramAbstimmungParameter.weisungVorbelegungMitKIAV = liefereErgInt(i);
                    break;
                case 2010:
                    ergHVParam.paramAbstimmungParameter.weisungVorbelegungMitDauer = liefereErgInt(i);
                    break;
                case 2011:
                    ergHVParam.paramAbstimmungParameter.weisungVorbelegungMitOrg = liefereErgInt(i);
                    break;
                case 2012:
                    ergHVParam.paramAbstimmungParameter.weisungHVVorbelegungMit = liefereErgInt(i);
                    break;
                case 2013:
                    ergHVParam.paramAbstimmungParameter.abstimmungVorbelegungMit = liefereErgInt(i);
                    break;

                case 2014:
                    ergHVParam.paramAbstimmungParameter.undefinierteWeisungenZaehlenAls = liefereErgInt(i);
                    break;
                case 2015:
                    ergHVParam.paramAbstimmungParameter.ungueltigeZaehlenAlsEnthaltung = liefereErgInt(i);
                    break;
                case 2016:
                    ergHVParam.paramAbstimmungParameter.sortierungDruckausgabeIndividuell = liefereErgInt(i);
                    break;
                case 2017:
                    ergHVParam.paramAbstimmungParameter.textVerwendenTablet = liefereErgInt(i);
                    break;
                case 2018:
                    ergHVParam.paramAbstimmungParameter.textVerwendenFormular = liefereErgInt(i);
                    break;
                case 2019:
                    ergHVParam.paramAbstimmungParameter.nichtTeilnahmeMoeglich = liefereErgInt(i);
                    break;
//                case 2020:
//                    Nicht mehr verwendet
//                    ergHVParam.paramAbstimmungParameter.gegenantragMarkiertMoeglich = liefereErgInt(i);
//                    break;
                case 2021:
                    ergHVParam.paramAbstimmungParameter.anzeigeBezeichnungKurzAktiv = liefereErgInt(i);
                    break;

                case 2022:
                    ergHVParam.paramAbstimmungParameter.weisungenGegenantraegePortalSeparat = liefereErgBoolean(i);
                    break;
                case 2023:
                    ergHVParam.paramAbstimmungParameter.weisungenGegenantraegeInternSeparat = liefereErgBoolean(i);
                    break;
                case 2024:
                    ergHVParam.paramAbstimmungParameter.weisungenGegenantraegeVerlassenHVSeparat = liefereErgBoolean(i);
                    break;

                case 2025:
                    ergHVParam.paramAbstimmungParameter.eingabezwangSRV = liefereErgBoolean(i);
                    break;
                case 2026:
                    ergHVParam.paramAbstimmungParameter.eingabezwangBriefwahl = liefereErgBoolean(i);
                    break;
                case 2027:
                    ergHVParam.paramAbstimmungParameter.eingabezwangKIAV = liefereErgBoolean(i);
                    break;
                case 2028:
                    ergHVParam.paramAbstimmungParameter.eingabezwangDauer = liefereErgBoolean(i);
                    break;
                case 2029:
                    ergHVParam.paramAbstimmungParameter.eingabezwangOrg = liefereErgBoolean(i);
                    break;
                case 2030:
                    ergHVParam.paramAbstimmungParameter.eingabezwangWeisungHV = liefereErgBoolean(i);
                    break;
                case 2031:
                    ergHVParam.paramAbstimmungParameter.eingabezwangAbstimmung = liefereErgBoolean(i);
                    break;

                case 2032:
                    ergHVParam.paramAbstimmungParameter.beiTabletFarbeJa = liefereErgString(i);
                    break;
                case 2033:
                    ergHVParam.paramAbstimmungParameter.beiTabletFarbeNein = liefereErgString(i);
                    break;
                case 2034:
                    ergHVParam.paramAbstimmungParameter.beiTabletFarbeEnthaltung = liefereErgString(i);
                    break;

                case 2035:
                    ergHVParam.paramAbstimmungParameter.beiTabletFarbeUnmarkiertJa = liefereErgString(i);
                    break;
                case 2036:
                    ergHVParam.paramAbstimmungParameter.beiTabletFarbeUnmarkiertNein = liefereErgString(i);
                    break;
                case 2037:
                    ergHVParam.paramAbstimmungParameter.beiTabletFarbeUnmarkiertEnthaltung = liefereErgString(i);
                    break;

                case 2038:
                    ergHVParam.paramAbstimmungParameter.beiTabletFarbeGesamtmarkierungJa = liefereErgString(i);
                    break;
                case 2039:
                    ergHVParam.paramAbstimmungParameter.beiTabletFarbeGesamtmarkierungNein = liefereErgString(i);
                    break;
                case 2040:
                    ergHVParam.paramAbstimmungParameter.beiTabletFarbeGesamtmarkierungEnthaltung = liefereErgString(i);
                    break;
                case 2041:
                    ergHVParam.paramAbstimmungParameter.abstimmungsLoadBeiAktivierungImAbstimmungsablauf = liefereErgInt(i);
                    break;
                case 2042:
                    ergHVParam.paramAbstimmungParameter.stimmzetteldruckAbstimmungsblockBasis = liefereErgInt(i);
                    break;
                case 2043:
                    ergHVParam.paramAbstimmungParameter.stimmzetteldruckAnzahlStimmzettel = liefereErgInt(i);
                    break;
                case 2044:
                    ergHVParam.paramAbstimmungParameter.briefwahlAusgebenInAbstimmungsergebnis = liefereErgInt(i);
                    break;

                /********************2201 bis 2400 Fortsetzung Portal-Parameter*********************************/
                //				case 2201:ergHVParam.paramPortal.lfdHVStreamIstMoeglich=liefereErgInt(i);break;
                case 2202:
                    ergHVParam.paramPortal.lfdHVMitteilungIstMoeglich = liefereErgInt(i);
                    break;
                case 2203:
                    ergHVParam.paramPortal.lfdHVFragenStufe1IstMoeglich = liefereErgInt(i);
                    break;
                case 2204:
                    ergHVParam.paramPortal.lfdHVFragenStufe2IstMoeglich = liefereErgInt(i);
                    break;
                case 2205:
                    ergHVParam.paramPortal.lfdHVFragenStufe3IstMoeglich = liefereErgInt(i);
                    break;
                case 2206:
                    ergHVParam.paramPortal.lfdHVFragenStufe4IstMoeglich = liefereErgInt(i);
                    break;

                case 2207:
                    ergHVParam.paramPortal.widerspruecheAnzahlJeAktionaer = liefereErgInt(i);
                    break;
                case 2208:
                    ergHVParam.paramPortal.widerspruecheLaenge = liefereErgInt(i);
                    break;
                case 2209:
                    ergHVParam.paramPortal.mitteilungNurFuerAngemeldete = liefereErgInt(i);
                    break;
                case 2210:
                    ergHVParam.paramPortal.mitteilungNurMitAbgegebenerStimme = liefereErgInt(i);
                    break;

                case 2211:
                    ergHVParam.paramPortal.streamAnbieter = liefereErgInt(i);
                    break;
                case 2212:
                    ergHVParam.paramPortal.streamTestlinkWirdAngeboten = liefereErgInt(i);
                    break;

                case 2213:
                    ergHVParam.paramPortal.fragenBisAktien = liefereErgInt(i);
                    break;
                case 2214:
                    ergHVParam.paramPortal.fragenAbAktien = liefereErgInt(i);
                    break;

                case 2215:
                    ergHVParam.paramPortal.fragenAnzahlJeAktionaer = liefereErgInt(i);
                    break;
                case 2216:
                    ergHVParam.paramPortal.fragenLaenge = liefereErgInt(i);
                    break;
                case 2217:
                    ergHVParam.paramPortal.fragenAnzahlStufe2 = liefereErgInt(i);
                    break;
                case 2218:
                    ergHVParam.paramPortal.fragenLaengeStufe2 = liefereErgInt(i);
                    break;
                case 2219:
                    ergHVParam.paramPortal.fragenAnzahlStufe3 = liefereErgInt(i);
                    break;
                case 2220:
                    ergHVParam.paramPortal.fragenLaengeStufe3 = liefereErgInt(i);
                    break;
                case 2221:
                    ergHVParam.paramPortal.fragenAnzahlStufe4 = liefereErgInt(i);
                    break;
                case 2222:
                    ergHVParam.paramPortal.fragenLaengeStufe4 = liefereErgInt(i);
                    break;

                case 2223:
                    ergHVParam.paramPortal.fragenStellerAbfragen = liefereErgInt(i);
                    break;

                case 2224:
                    ergHVParam.paramPortal.mitteilungenAngeboten = liefereErgInt(i);
                    break;

                //				case 2225:ergHVParam.paramPortal.streamAngeboten=liefereErgInt(i);break;
                case 2226:
                    ergHVParam.paramPortal.streamNurFuerAngemeldete = liefereErgInt(i);
                    break;

                case 2227:
                    ergHVParam.paramPortal.fragenAngeboten = liefereErgInt(i);
                    break;
                case 2228:
                    ergHVParam.paramPortal.fragenNurFuerAngemeldete = liefereErgInt(i);
                    break;

                case 2229:
                    ergHVParam.paramPortal.widerspruecheStellerAbfragen = liefereErgInt(i);
                    break;
                case 2230:
                    ergHVParam.paramPortal.mitteilungenDialogTopAngeboten = liefereErgInt(i);
                    break;
                case 2231:
                    ergHVParam.paramPortal.mitteilungenTextAngeboten = liefereErgInt(i);
                    break;
                case 2232:
                    ergHVParam.paramPortal.briefwahlZusaetzlichZuSRVMoeglich = liefereErgInt(i);
                    break;

                case 2233:
                    ergHVParam.paramPortal.teilnehmerverzAngeboten = liefereErgInt(i);
                    break;
                case 2234:
                    ergHVParam.paramPortal.teilnehmerverzZusammenstellung = liefereErgInt(i);
                    break;
                case 2235:
                    ergHVParam.paramPortal.teilnehmerverzLetzteNr = liefereErgInt(i);
                    break;

//                case 2236:
//                    ergHVParam.paramPortal.abstimmungsergAngeboten = liefereErgInt(i);
//                    break;
                case 2237:
                    ergHVParam.paramPortal.abstimmungsergLetzteNr = liefereErgInt(i);
                    break;

                case 2238:
                    ergHVParam.paramPortal.widerspruecheStellerZulaessig = liefereErgInt(i);
                    break;
                case 2239:
                    ergHVParam.paramPortal.fragenStellerZulaessig = liefereErgInt(i);
                    break;

                case 2240:
                    ergHVParam.paramPortal.widerspruecheMailBeiEingang = liefereErgInt(i);
                    break;
                case 2241:
                    ergHVParam.paramPortal.fragenExternerZugriff = liefereErgInt(i);
                    break;

                case 2242:
                    ergHVParam.paramPortal.ekText = liefereErgString(i);
                    break;
                case 2243:
                    ergHVParam.paramPortal.ekTextMitArtikel = liefereErgString(i);
                    break;
                case 2244:
                    ergHVParam.paramPortal.ekTextEN = liefereErgString(i);
                    break;
                case 2245:
                    ergHVParam.paramPortal.ekTextENMitArtikel = liefereErgString(i);
                    break;

                case 2246:
                    ergHVParam.paramPortal.lfdHVTeilnehmerverzIstMoeglich = liefereErgInt(i);
                    break;
                case 2247:
                    ergHVParam.paramPortal.lfdHVAbstimmungsergIstMoeglich = liefereErgInt(i);
                    break;

                case 2248:
                    ergHVParam.paramPortal.timeoutAufLang = liefereErgInt(i);
                    break;
                case 2249:
                    ergHVParam.paramPortal.mitteilungsButtonImmerSichtbar = liefereErgInt(i);
                    break;
                case 2250:
                    ergHVParam.paramPortal.fragenMailBeiEingang = liefereErgInt(i);
                    break;

                    /**2251 bis 2260 - unterlage1aktiv etc - entfernt 08.01.2022*/

                case 2261:
                    ergHVParam.paramPortal.streamAnbieter2 = liefereErgInt(i);
                    break;

                    /**2262 bis 2271 - unterlage11aktiv etc - entfernt 08.01.2022*/


                case 2272:
                    ergHVParam.paramPortal.unterlagenAngeboten = liefereErgInt(i);
                    break;

                //				case 2273:ergHVParam.paramPortal.onlineTeilnahmeAngeboten=liefereErgInt(i);break;
                //				case 2274:ergHVParam.paramPortal.onlineTeilnahmeAktiv=liefereErgInt(i);break;
                case 2275:
                    ergHVParam.paramPortal.nurRawLiveAbstimmung = liefereErgInt(i);
                    break;
                case 2276:
                    ergHVParam.paramPortal.testModus = liefereErgInt(i);
                    break;
                case 2277:
                    ergHVParam.paramPortal.websocketsMoeglich = liefereErgInt(i);
                    break;
                case 2278:
                    ergHVParam.paramPortal.doppelLoginGesperrt = liefereErgInt(i);
                    break;

                case 2279:
                    ergHVParam.paramPortal.onlineTeilnehmerAbfragen = liefereErgInt(i);
                    break;

                case 2280:
                    ergHVParam.paramPortal.wortmeldungMailBeiEingang = liefereErgInt(i);
                    break;
                case 2281:
                    ergHVParam.paramPortal.wortmeldungStellerAbfragen = liefereErgInt(i);
                    break;
                case 2282:
                    ergHVParam.paramPortal.wortmeldungNameAbfragen = liefereErgInt(i);
                    break;
                case 2283:
                    ergHVParam.paramPortal.wortmeldungKurztextAbfragen = liefereErgInt(i);
                    break;
                case 2284:
                    ergHVParam.paramPortal.wortmeldungLangtextAbfragen = liefereErgInt(i);
                    break;
                case 2285:
                    ergHVParam.paramPortal.wortmeldungListeAnzeigen = liefereErgInt(i);
                    break;
                case 2286:
                    ergHVParam.paramPortal.wortmeldungStellerZulaessig = liefereErgInt(i);
                    break;
                case 2287:
                    ergHVParam.paramPortal.wortmeldungAnzahlJeAktionaer = liefereErgInt(i);
                    break;
                case 2288:
                    ergHVParam.paramPortal.wortmeldungLaenge = liefereErgInt(i);
                    break;

                case 2289:
                    ergHVParam.paramPortal.onlineTeilnahmeSeparateNutzungsbedingungen = liefereErgInt(i);
                    break;
                case 2290:
                    ergHVParam.paramPortal.onlineTeilnahmeGastInSeparatemMenue = liefereErgInt(i);
                    break;
                case 2291:
                    ergHVParam.paramPortal.onlineTeilnahmeNurFuerFreiwilligAngemeldete = liefereErgInt(i);
                    break;
                case 2292:
                    ergHVParam.paramPortal.onlineTeilnahmeAktionaerAlsGast = liefereErgInt(i);
                    break;

                case 2293:
                    ergHVParam.paramPortal.teilnehmerverzBeginnendBei = liefereErgInt(i);
                    break;

                case 2294:
                    ergHVParam.paramPortal.fragenNameAbfragen = liefereErgInt(i);
                    break;
                case 2295:
                    ergHVParam.paramPortal.fragenKontaktdatenAbfragen = liefereErgInt(i);
                    break;
                case 2296:
                    ergHVParam.paramPortal.fragenKontaktdatenEMailVorschlagen = liefereErgInt(i);
                    break;
                case 2297:
                    ergHVParam.paramPortal.fragenKurztextAbfragen = liefereErgInt(i);
                    break;
                case 2298:
                    ergHVParam.paramPortal.fragenTopListeAnbieten = liefereErgInt(i);
                    break;
                case 2299:
                    ergHVParam.paramPortal.fragenLangtextAbfragen = liefereErgInt(i);
                    break;
                case 2300:
                    ergHVParam.paramPortal.fragenZurueckziehenMoeglich = liefereErgInt(i);
                    break;

                case 2301:
                    ergHVParam.paramPortal.wortmeldungKontaktdatenAbfragen = liefereErgInt(i);
                    break;
                case 2302:
                    ergHVParam.paramPortal.wortmeldungKontaktdatenEMailVorschlagen = liefereErgInt(i);
                    break;
                case 2303:
                    ergHVParam.paramPortal.wortmeldungTopListeAnbieten = liefereErgInt(i);
                    break;
                case 2304:
                    ergHVParam.paramPortal.wortmeldungZurueckziehenMoeglich = liefereErgInt(i);
                    break;

                case 2305:
                    ergHVParam.paramPortal.widerspruecheNameAbfragen = liefereErgInt(i);
                    break;
                case 2306:
                    ergHVParam.paramPortal.widerspruecheKontaktdatenAbfragen = liefereErgInt(i);
                    break;
                case 2307:
                    ergHVParam.paramPortal.widerspruecheKontaktdatenEMailVorschlagen = liefereErgInt(i);
                    break;
                case 2308:
                    ergHVParam.paramPortal.widerspruecheKurztextAbfragen = liefereErgInt(i);
                    break;
                case 2309:
                    ergHVParam.paramPortal.widerspruecheTopListeAnbieten = liefereErgInt(i);
                    break;
                case 2310:
                    ergHVParam.paramPortal.widerspruecheLangtextAbfragen = liefereErgInt(i);
                    break;
                case 2311:
                    ergHVParam.paramPortal.widerspruecheZurueckziehenMoeglich = liefereErgInt(i);
                    break;

                case 2312:
                    ergHVParam.paramPortal.antraegeStellerAbfragen = liefereErgInt(i);
                    break;
                case 2313:
                    ergHVParam.paramPortal.antraegeNameAbfragen = liefereErgInt(i);
                    break;
                case 2314:
                    ergHVParam.paramPortal.antraegeKontaktdatenAbfragen = liefereErgInt(i);
                    break;
                case 2315:
                    ergHVParam.paramPortal.antraegeKontaktdatenEMailVorschlagen = liefereErgInt(i);
                    break;
                case 2316:
                    ergHVParam.paramPortal.antraegeKurztextAbfragen = liefereErgInt(i);
                    break;
                case 2317:
                    ergHVParam.paramPortal.antraegeTopListeAnbieten = liefereErgInt(i);
                    break;
                case 2318:
                    ergHVParam.paramPortal.antraegeLangtextAbfragen = liefereErgInt(i);
                    break;
                case 2319:
                    ergHVParam.paramPortal.antraegeZurueckziehenMoeglich = liefereErgInt(i);
                    break;
                case 2320:
                    ergHVParam.paramPortal.antraegeLaenge = liefereErgInt(i);
                    break;
                case 2321:
                    ergHVParam.paramPortal.antraegeAnzahlJeAktionaer = liefereErgInt(i);
                    break;
                case 2322:
                    ergHVParam.paramPortal.antraegeStellerZulaessig = liefereErgInt(i);
                    break;
                case 2323:
                    ergHVParam.paramPortal.antraegeMailBeiEingang = liefereErgInt(i);
                    break;

                case 2324:
                    ergHVParam.paramPortal.sonstMitteilungenStellerAbfragen = liefereErgInt(i);
                    break;
                case 2325:
                    ergHVParam.paramPortal.sonstMitteilungenNameAbfragen = liefereErgInt(i);
                    break;
                case 2326:
                    ergHVParam.paramPortal.sonstMitteilungenKontaktdatenAbfragen = liefereErgInt(i);
                    break;
                case 2327:
                    ergHVParam.paramPortal.sonstMitteilungenKontaktdatenEMailVorschlagen = liefereErgInt(i);
                    break;
                case 2328:
                    ergHVParam.paramPortal.sonstMitteilungenKurztextAbfragen = liefereErgInt(i);
                    break;
                case 2329:
                    ergHVParam.paramPortal.sonstMitteilungenTopListeAnbieten = liefereErgInt(i);
                    break;
                case 2330:
                    ergHVParam.paramPortal.sonstMitteilungenLangtextAbfragen = liefereErgInt(i);
                    break;
                case 2331:
                    ergHVParam.paramPortal.sonstMitteilungenZurueckziehenMoeglich = liefereErgInt(i);
                    break;
                case 2332:
                    ergHVParam.paramPortal.sonstMitteilungenLaenge = liefereErgInt(i);
                    break;
                case 2333:
                    ergHVParam.paramPortal.sonstMitteilungenAnzahlJeAktionaer = liefereErgInt(i);
                    break;
                case 2334:
                    ergHVParam.paramPortal.sonstMitteilungenStellerZulaessig = liefereErgInt(i);
                    break;
                case 2335:
                    ergHVParam.paramPortal.sonstMitteilungenMailBeiEingang = liefereErgInt(i);
                    break;

                case 2336:
                    ergHVParam.paramPortal.cookieHinweis = liefereErgInt(i);
                    break;
                case 2337:
                    ergHVParam.paramPortal.vollmachtDritteUndAndereWKMoeglich = liefereErgInt(i);
                    break;
                case 2338:
                    ergHVParam.paramPortal.handhabungWeisungDurchVerschiedene = liefereErgInt(i);
                    break;

                case 2339:
                    ergHVParam.paramPortal.farbeHeader = liefereErgString(i);
                    break;
                case 2340:
                    ergHVParam.paramPortal.farbeButton = liefereErgString(i);
                    break;
                case 2341:
                    ergHVParam.paramPortal.farbeButtonHover = liefereErgString(i);
                    break;
                case 2342:
                    ergHVParam.paramPortal.farbeButtonSchrift = liefereErgString(i);
                    break;
                case 2343:
                    ergHVParam.paramPortal.farbeLink = liefereErgString(i);
                    break;
                case 2344:
                    ergHVParam.paramPortal.farbeLinkHover = liefereErgString(i);
                    break;
                case 2345:
                    ergHVParam.paramPortal.farbeListeUngerade = liefereErgString(i);
                    break;
                case 2346:
                    ergHVParam.paramPortal.farbeListeGerade = liefereErgString(i);
                    break;
                case 2347:
                    ergHVParam.paramPortal.farbeHintergrund = liefereErgString(i);
                    break;
                case 2348:
                    ergHVParam.paramPortal.farbeText = liefereErgString(i);
                    break;
                case 2349:
                    ergHVParam.paramPortal.farbeUeberschriftHintergrund = liefereErgString(i);
                    break;
                case 2350:
                    ergHVParam.paramPortal.farbeUeberschrift = liefereErgString(i);
                    break;
                case 2354:
                    ergHVParam.paramPortal.logoPosition = liefereErgInt(i);
                    break;
                case 2355:
                    ergHVParam.paramPortal.farbeCookieHintHintergrund = liefereErgString(i);
                    break;
                case 2356:
                    ergHVParam.paramPortal.farbeCookieHintSchrift = liefereErgString(i);
                    break;
                case 2357:
                    ergHVParam.paramPortal.farbeCookieHintButton = liefereErgString(i);
                    break;
                case 2358:
                    ergHVParam.paramPortal.farbeCookieHintButtonSchrift = liefereErgString(i);
                    break;   
                    
                case 2359:
                    ergHVParam.paramPortal.schriftgroesseGlobal = liefereErgString(i);
                    break;    
                case 2360:
                    ergHVParam.paramPortal.logoMindestbreite = liefereErgString(i);
                    break;    
                case 2361:
                    ergHVParam.paramPortal.farbeHintergrundBtn00 = liefereErgString(i);
                    break;    
                case 2362:
                    ergHVParam.paramPortal.farbeSchriftBtn00 = liefereErgString(i);
                    break;    
                case 2363:
                    ergHVParam.paramPortal.farbeRahmenBtn00 = liefereErgString(i);
                    break;    
                case 2364:
                    ergHVParam.paramPortal.breiteRahmenBtn00 = liefereErgString(i);
                    break;    
                case 2365:
                    ergHVParam.paramPortal.radiusRahmenBtn00 = liefereErgString(i);
                    break;    
                case 2366:
                    ergHVParam.paramPortal.stilRahmenBtn00 = liefereErgString(i);
                    break;    
                case 2367:
                    ergHVParam.paramPortal.farbeHintergrundBtn00Hover = liefereErgString(i);
                    break;    
                case 2368:
                    ergHVParam.paramPortal.farbeSchriftBtn00Hover = liefereErgString(i);
                    break;
                    
                case 2369:
                    ergHVParam.paramPortal.farbeRahmenBtn00Hover = liefereErgString(i);
                    break;    
                case 2370:
                    ergHVParam.paramPortal.breiteRahmenBtn00Hover = liefereErgString(i);
                    break;    
                case 2371:
                    ergHVParam.paramPortal.radiusRahmenBtn00Hover = liefereErgString(i);
                    break;    
                case 2372:
                    ergHVParam.paramPortal.stilRahmenBtn00Hover = liefereErgString(i);
                    break;    
                case 2373:
                    ergHVParam.paramPortal.farbeFocus = liefereErgString(i);
                    break;    
                case 2374:
                    ergHVParam.paramPortal.farbeError = liefereErgString(i);
                    break;    
                case 2375:
                    ergHVParam.paramPortal.farbeErrorSchrift = liefereErgString(i);
                    break;    
                case 2376:
                    ergHVParam.paramPortal.farbeWarning = liefereErgString(i);
                    break;    
                case 2377:
                    ergHVParam.paramPortal.farbeWarningSchrift = liefereErgString(i);
                    break;    
                case 2378:
                    ergHVParam.paramPortal.farbeSuccess = liefereErgString(i);
                    break;
                    
                case 2379:
                    ergHVParam.paramPortal.farbeSuccessSchrift = liefereErgString(i);
                    break;    
                case 2380:
                    ergHVParam.paramPortal.farbeRahmenEingabefelder = liefereErgString(i);
                    break;    
                case 2381:
                    ergHVParam.paramPortal.breiteRahmenEingabefelder = liefereErgString(i);
                    break;    
                case 2382:
                    ergHVParam.paramPortal.radiusRahmenEingabefelder = liefereErgString(i);
                    break;    
                case 2383:
                    ergHVParam.paramPortal.stilRahmenEingabefelder = liefereErgString(i);
                    break;    
                case 2384:
                    ergHVParam.paramPortal.farbeHintergrundLogoutBtn = liefereErgString(i);
                    break;    
                case 2385:
                    ergHVParam.paramPortal.farbeSchriftLogoutBtn = liefereErgString(i);
                    break;    
                case 2386:
                    ergHVParam.paramPortal.farbeRahmenLogoutBtn = liefereErgString(i);
                    break;    
                case 2387:
                    ergHVParam.paramPortal.breiteRahmenLogoutBtn = liefereErgString(i);
                    break;    
                case 2388:
                    ergHVParam.paramPortal.radiusRahmenLogoutBtn = liefereErgString(i);
                    break;
                    
                case 2389:
                    ergHVParam.paramPortal.stilRahmenLogoutBtn = liefereErgString(i);
                    break;    
                case 2390:
                    ergHVParam.paramPortal.farbeHintergrundLogoutBtnHover = liefereErgString(i);
                    break;    
                case 2391:
                    ergHVParam.paramPortal.farbeSchriftLogoutBtnHover = liefereErgString(i);
                    break;    
                case 2392:
                    ergHVParam.paramPortal.farbeRahmenLogoutBtnHover = liefereErgString(i);
                    break;    
                case 2393:
                    ergHVParam.paramPortal.breiteRahmenLogoutBtnHover = liefereErgString(i);
                    break;    
                case 2394:
                    ergHVParam.paramPortal.radiusRahmenLogoutBtnHover = liefereErgString(i);
                    break;    
                case 2395:
                    ergHVParam.paramPortal.stilRahmenLogoutBtnHover = liefereErgString(i);
                    break;    
                case 2396:
                    ergHVParam.paramPortal.farbeHintergrundLoginBtn = liefereErgString(i);
                    break;    
                case 2397:
                    ergHVParam.paramPortal.farbeSchriftLoginBtn = liefereErgString(i);
                    break;    
                case 2398:
                    ergHVParam.paramPortal.farbeRahmenLoginBtn = liefereErgString(i);
                    break;
                    
                case 2399:
                    ergHVParam.paramPortal.breiteRahmenLoginBtn = liefereErgString(i);
                    break;    
                case 2400:
                    ergHVParam.paramPortal.radiusRahmenLoginBtn = liefereErgString(i);
                    break;    
                case 2401:
                    ergHVParam.paramPortal.stilRahmenLoginBtn = liefereErgString(i);
                    break;    
                case 2402:
                    ergHVParam.paramPortal.farbeHintergrundLoginBtnHover = liefereErgString(i);
                    break;    
                case 2403:
                    ergHVParam.paramPortal.farbeSchriftLoginBtnHover = liefereErgString(i);
                    break;    
                case 2404:
                    ergHVParam.paramPortal.farbeRahmenLoginBtnHover = liefereErgString(i);
                    break;    
                case 2405:
                    ergHVParam.paramPortal.breiteRahmenLoginBtnHover = liefereErgString(i);
                    break;    
                case 2406:
                    ergHVParam.paramPortal.radiusRahmenLoginBtnHover = liefereErgString(i);
                    break;    
                case 2407:
                    ergHVParam.paramPortal.stilRahmenLoginBtnHover = liefereErgString(i);
                    break;    
                case 2408:
                    ergHVParam.paramPortal.farbeRahmenLoginBereich = liefereErgString(i);
                    break;
                    
                case 2409:
                    ergHVParam.paramPortal.breiteRahmenLoginBereich = liefereErgString(i);
                    break;    
                case 2410:
                    ergHVParam.paramPortal.radiusRahmenLoginBereich = liefereErgString(i);
                    break;    
                case 2411:
                    ergHVParam.paramPortal.stilRahmenLoginBereich = liefereErgString(i);
                    break;    
                case 2412:
                    ergHVParam.paramPortal.farbeHintergrundLoginBereich = liefereErgString(i);
                    break;    
                case 2413:
                    ergHVParam.paramPortal.farbeLinkLoginBereich = liefereErgString(i);
                    break;    
                case 2414:
                    ergHVParam.paramPortal.farbeLinkHoverLoginBereich = liefereErgString(i);
                    break;    
                case 2415:
                    ergHVParam.paramPortal.farbeRahmenEingabefelderLoginBereich = liefereErgString(i);
                    break;    
                case 2416:
                    ergHVParam.paramPortal.breiteRahmenEingabefelderLoginBereich = liefereErgString(i);
                    break;    
                case 2417:
                    ergHVParam.paramPortal.radiusRahmenEingabefelderLoginBereich = liefereErgString(i);
                    break;    
                case 2418:
                    ergHVParam.paramPortal.stilRahmenEingabefelderLoginBereich = liefereErgString(i);
                    break;
                    
                case 2419:
                    ergHVParam.paramPortal.farbeBestandsbereichUngeradeReihe = liefereErgString(i);
                    break;    
                case 2420:
                    ergHVParam.paramPortal.farbeBestandsbereichGeradeReihe = liefereErgString(i);
                    break;    
                case 2421:
                    ergHVParam.paramPortal.farbeLineUntenBestandsbereich = liefereErgString(i);
                    break;    
                case 2422:
                    ergHVParam.paramPortal.breiteLineUntenBestandsbereich = liefereErgString(i);
                    break;    
                case 2423:
                    ergHVParam.paramPortal.stilLineUntenBestandsbereich = liefereErgString(i);
                    break;    
                case 2424:
                    ergHVParam.paramPortal.farbeRahmenAnmeldeuebersicht = liefereErgString(i);
                    break;    
                case 2425:
                    ergHVParam.paramPortal.breiteRahmenAnmeldeuebersicht = liefereErgString(i);
                    break;    
                case 2426:
                    ergHVParam.paramPortal.radiusRahmenAnmeldeuebersicht = liefereErgString(i);
                    break;    
                case 2427:
                    ergHVParam.paramPortal.stilRahmenAnmeldeuebersicht = liefereErgString(i);
                    break;    
                case 2428:
                    ergHVParam.paramPortal.farbeTrennlinieAnmeldeuebersicht = liefereErgString(i);
                    break;
                    
                case 2429:
                    ergHVParam.paramPortal.breiteTrennlinieAnmeldeuebersicht = liefereErgString(i);
                    break;    
                case 2430:
                    ergHVParam.paramPortal.stilTrennlinieAnmeldeuebersicht = liefereErgString(i);
                    break;    
                case 2431:
                    ergHVParam.paramPortal.farbeRahmenErteilteWillenserklärungen = liefereErgString(i);
                    break;    
                case 2432:
                    ergHVParam.paramPortal.breiteRahmenErteilteWillenserklärungen = liefereErgString(i);
                    break;    
                case 2433:
                    ergHVParam.paramPortal.radiusRahmenErteilteWillenserklärungen = liefereErgString(i);
                    break;    
                case 2434:
                    ergHVParam.paramPortal.stilRahmenErteilteWillenserklärungen = liefereErgString(i);
                    break;    
                case 2435:
                    ergHVParam.paramPortal.farbeHintergrundErteilteWillenserklärungen = liefereErgString(i);
                    break;    
                case 2436:
                    ergHVParam.paramPortal.farbeSchriftErteilteWillenserklärungen = liefereErgString(i);
                    break;    
                case 2437:
                    ergHVParam.paramPortal.farbeRahmenAbstimmungstabelle = liefereErgString(i);
                    break;    
                case 2438:
                    ergHVParam.paramPortal.breiteRahmenAbstimmungstabelle = liefereErgString(i);
                    break;
                    
                case 2439:
                    ergHVParam.paramPortal.radiusRahmenAbstimmungstabelle = liefereErgString(i);
                    break;    
                case 2440:
                    ergHVParam.paramPortal.stilRahmenAbstimmungstabelle = liefereErgString(i);
                    break;    
                case 2441:
                    ergHVParam.paramPortal.farbeHintergrundAbstimmungstabelleUngeradeReihen = liefereErgString(i);
                    break;    
                case 2442:
                    ergHVParam.paramPortal.farbeSchriftAbstimmungstabelleUngeradeReihen = liefereErgString(i);
                    break;    
                case 2443:
                    ergHVParam.paramPortal.farbeHintergrundAbstimmungstabelleGeradeReihen = liefereErgString(i);
                    break;    
                case 2444:
                    ergHVParam.paramPortal.farbeSchriftAbstimmungstabelleGeradeReihen = liefereErgString(i);
                    break;    
                case 2445:
                    ergHVParam.paramPortal.farbeHintergrundWeisungJa = liefereErgString(i);
                    break;    
                case 2446:
                    ergHVParam.paramPortal.farbeSchriftWeisungJa = liefereErgString(i);
                    break;    
                case 2447:
                    ergHVParam.paramPortal.farbeRahmenWeisungJa = liefereErgString(i);
                    break;    
                case 2448:
                    ergHVParam.paramPortal.farbeHintergrundWeisungJaChecked = liefereErgString(i);
                    break;
                   
                case 2449:
                    ergHVParam.paramPortal.farbeSchriftWeisungJaChecked = liefereErgString(i);
                    break;    
                case 2450:
                    ergHVParam.paramPortal.farbeRahmenWeisungJaChecked = liefereErgString(i);
                    break;    
                case 2451:
                    ergHVParam.paramPortal.farbeHintergrundWeisungNein = liefereErgString(i);
                    break;    
                case 2452:
                    ergHVParam.paramPortal.farbeSchriftWeisungNein = liefereErgString(i);
                    break;    
                case 2453:
                    ergHVParam.paramPortal.farbeRahmenWeisungNein = liefereErgString(i);
                    break;    
                case 2454:
                    ergHVParam.paramPortal.farbeHintergrundWeisungNeinChecked = liefereErgString(i);
                    break;    
                case 2455:
                    ergHVParam.paramPortal.farbeSchriftWeisungNeinChecked = liefereErgString(i);
                    break;    
                case 2456:
                    ergHVParam.paramPortal.farbeRahmenWeisungNeinChecked = liefereErgString(i);
                    break;    
                case 2457:
                    ergHVParam.paramPortal.farbeHintergrundWeisungEnthaltung = liefereErgString(i);
                    break;    
                case 2458:
                    ergHVParam.paramPortal.farbeSchriftWeisungEnthaltung = liefereErgString(i);
                    break;
                    
                case 2459:
                    ergHVParam.paramPortal.farbeRahmenWeisungEnthaltung = liefereErgString(i);
                    break;    
                case 2460:
                    ergHVParam.paramPortal.farbeHintergrundWeisungEnthaltungChecked = liefereErgString(i);
                    break;    
                case 2461:
                    ergHVParam.paramPortal.farbeSchriftWeisungEnthaltungChecked = liefereErgString(i);
                    break;    
                case 2462:
                    ergHVParam.paramPortal.farbeRahmenWeisungEnthaltungChecked = liefereErgString(i);
                    break;    
                case 2463:
                    ergHVParam.paramPortal.farbeHintergrundFooterTop = liefereErgString(i);
                    break;    
                case 2464:
                    ergHVParam.paramPortal.farbeSchriftFooterTop = liefereErgString(i);
                    break;    
                case 2465:
                    ergHVParam.paramPortal.farbeLinkFooterTop = liefereErgString(i);
                    break;    
                case 2466:
                    ergHVParam.paramPortal.farbeLinkFooterTopHover = liefereErgString(i);
                    break;    
                case 2467:
                    ergHVParam.paramPortal.farbeHintergrundFooterBottom = liefereErgString(i);
                    break;    
                case 2468:
                    ergHVParam.paramPortal.farbeSchriftFooterBottom = liefereErgString(i);
                    break;
                    
                case 2469:
                    ergHVParam.paramPortal.farbeLinkFooterBottom = liefereErgString(i);
                    break;    
                case 2470:
                    ergHVParam.paramPortal.farbeLinkFooterBottomHover = liefereErgString(i);
                    break;    
                case 2471:
                    ergHVParam.paramPortal.farbeHintergrundModal = liefereErgString(i);
                    break;    
                case 2472:
                    ergHVParam.paramPortal.farbeSchriftModal = liefereErgString(i);
                    break;    
                case 2473:
                    ergHVParam.paramPortal.farbeHintergrundModalHeader = liefereErgString(i);
                    break;    
                case 2474:
                    ergHVParam.paramPortal.farbeSchriftModalHeader = liefereErgString(i);
                    break;    
                case 2475:
                    ergHVParam.paramPortal.farbeTrennlinieModal = liefereErgString(i);
                    break;    
                case 2476:
                    ergHVParam.paramPortal.farbeHintergrundUntenButtons = liefereErgString(i);
                    break;    
                case 2477:
                    ergHVParam.paramPortal.farbeSchriftUntenButtons = liefereErgString(i);
                    break;    
                case 2478:
                    ergHVParam.paramPortal.farbeRahmenUntenButtons = liefereErgString(i);
                    break;
                    
                case 2479:
                    ergHVParam.paramPortal.breiteRahmenUntenButtons = liefereErgString(i);
                    break;    
                case 2480:
                    ergHVParam.paramPortal.radiusRahmenUntenButtons = liefereErgString(i);
                    break;    
                case 2481:
                    ergHVParam.paramPortal.stilRahmenUntenButtons = liefereErgString(i);
                    break;    
                case 2482:
                    ergHVParam.paramPortal.farbeHintergrundUntenButtonsHover = liefereErgString(i);
                    break;    
                case 2483:
                    ergHVParam.paramPortal.farbeSchriftUntenButtonsHover = liefereErgString(i);
                    break;    
                case 2484:
                    ergHVParam.paramPortal.farbeRahmenUntenButtonsHover = liefereErgString(i);
                    break;    
                case 2485:
                    ergHVParam.paramPortal.breiteRahmenUntenButtonsHover = liefereErgString(i);
                    break;    
                case 2486:
                    ergHVParam.paramPortal.radiusRahmenUntenButtonsHover = liefereErgString(i);
                    break;    
                case 2487:
                    ergHVParam.paramPortal.stilRahmenUntenButtonsHover = liefereErgString(i);
                    break;
                    
                case 2488:
                    ergHVParam.paramPortal.bestaetigungBeiWeisungMitTOP = liefereErgInt(i);
                    break;
                case 2489:
                    ergHVParam.paramPortal.anzeigeStimmenKennung = liefereErgInt(i);
                    break;
                case 2490:
                    ergHVParam.paramPortal.weisungenAktuellNichtMoeglich = liefereErgInt(i);
                    break;
                case 2491:
                    ergHVParam.paramPortal.sammelkartenFuerAenderungSperren = liefereErgInt(i);
                    break;
                case 2492:
                    ergHVParam.paramPortal.erklAnPos1 = liefereErgInt(i);
                    break;
                case 2493:
                    ergHVParam.paramPortal.erklAnPos2 = liefereErgInt(i);
                    break;
                case 2494:
                    ergHVParam.paramPortal.erklAnPos3 = liefereErgInt(i);
                    break;
                case 2495:
                    ergHVParam.paramPortal.erklAnPos4 = liefereErgInt(i);
                    break;
                case 2496:
                    ergHVParam.paramPortal.erklAnPos5 = liefereErgInt(i);
                    break;
                /*reserviert bis 2512 - 20 Positionen*/
                case 2513:
                    ergHVParam.paramPortal.kontaktformularAktiv = liefereErgInt(i);
                    break;
                case 2514:
                    ergHVParam.paramPortal.kontaktformularBeiEingangMail = liefereErgInt(i);
                    break;
                case 2515:
                    ergHVParam.paramPortal.kontaktformularBeiEingangMailInhaltAufnehmen = liefereErgInt(i);
                    break;
                case 2516:
                    ergHVParam.paramPortal.kontaktformularBeiEingangAufgabe = liefereErgInt(i);
                    break;
                case 2517:
                    ergHVParam.paramPortal.kontaktformularAnzahlKontaktfelder = liefereErgInt(i);
                    break;
                case 2518:
                    ergHVParam.paramPortal.kontaktformularTelefonKontaktAbfragen = liefereErgInt(i);
                    break;
                case 2519:
                    ergHVParam.paramPortal.kontaktformularMailKontaktAbfragen = liefereErgInt(i);
                    break;
                case 2520:
                    ergHVParam.paramPortal.kontaktformularThemaAnbieten = liefereErgInt(i);
                    break;
                case 2521:
                    ergHVParam.paramPortal.kontaktSonstigeMoeglichkeitenAnbieten = liefereErgInt(i);
                    break;
                case 2522:
                    ergHVParam.paramPortal.kontaktSonstigeMoeglichkeitenObenOderUnten = liefereErgInt(i);
                    break;
                case 2523:
                    ergHVParam.paramPortal.registerAnbindung = liefereErgInt(i);
                    break;
                case 2524:
                    ergHVParam.paramPortal.registerAnbindungOberflaeche = liefereErgInt(i);
                    break;
                case 2525:
                    ergHVParam.paramPortal.registerAnbindungCheckBeiLogin = liefereErgInt(i);
                    break;
                  
                case 2526:
                    ergHVParam.paramPortal.wortmeldungVLListeAnzeigen = liefereErgInt(i);
                    break;
 
                case 2527:
                    ergHVParam.paramPortal.verfahrenPasswortVergessenBeiEmailHinterlegtAuchPost = liefereErgInt(i);
                    break;
                case 2528:
                    ergHVParam.paramPortal.linkEmailEnthaeltLinkOderCode = liefereErgInt(i);
                    break;
                case 2529:
                    ergHVParam.paramPortal.bestaetigenHinweisWeitere = liefereErgInt(i);
                    break;
                case 2530:
                    ergHVParam.paramPortal.kontaktformularThemenListeGlobalLokal = liefereErgInt(i);
                    break;

                case 2531:
                    ergHVParam.paramPortal.api_jwt_expiration_time = liefereErgInt(i);
                    break;
                case 2532:
                    ergHVParam.paramPortal.api_key_name = liefereErgString(i);
                    break;

                case 2533:
                    ergHVParam.paramPortal.freiwillingAnmeldenPraesenzOderOnline = liefereErgInt(i);
                    break;
                case 2534:
                    ergHVParam.paramPortal.veranstaltungMailVerschicken = liefereErgInt(i);
                    break;
                case 2535:
                    ergHVParam.paramPortal.veranstaltungPersonenzahlEingeben = liefereErgInt(i);
                    break;
                case 2536:
                    ergHVParam.paramPortal.veranstaltungMehrfachAuswaehlbarJeGruppe = liefereErgInt(i);
                    break;

                case 2537:
                    ergHVParam.paramPortal.botschaftenStellerAbfragen = liefereErgInt(i);
                    break;
                case 2538:
                    ergHVParam.paramPortal.botschaftenNameAbfragen = liefereErgInt(i);
                    break;
                case 2539:
                    ergHVParam.paramPortal.botschaftenKontaktdatenAbfragen = liefereErgInt(i);
                    break;
                case 2540:
                    ergHVParam.paramPortal.botschaftenKontaktdatenEMailVorschlagen = liefereErgInt(i);
                    break;
                case 2541:
                    ergHVParam.paramPortal.botschaftenKurztextAbfragen = liefereErgInt(i);
                    break;
                case 2542:
                    ergHVParam.paramPortal.botschaftenTopListeAnbieten = liefereErgInt(i);
                    break;
                case 2543:
                    ergHVParam.paramPortal.botschaftenLangtextAbfragen = liefereErgInt(i);
                    break;
                case 2544:
                    ergHVParam.paramPortal.botschaftenZurueckziehenMoeglich = liefereErgInt(i);
                    break;
                case 2545:
                    ergHVParam.paramPortal.botschaftenAnzahlJeAktionaer = liefereErgInt(i);
                    break;
                case 2546:
                    ergHVParam.paramPortal.botschaftenStellerZulaessig = liefereErgInt(i);
                    break;
                case 2547:
                    ergHVParam.paramPortal.botschaftenMailBeiEingang = liefereErgInt(i);
                    break;
                case 2548:
                    ergHVParam.paramPortal.botschaftenVideo = liefereErgInt(i);
                    break;
                case 2549:
                    hString= liefereErgString(i);
                    for (int i1=1;i1<=15;i1++) {
                        if (i1<hString.length()) {
                            ergHVParam.paramPortal.botschaftenVideoFormate[i1]=Integer.parseInt(hString.substring(i1, i1+1));
                        }
                    }
                    break;
                /*2550: nicht mehr verwendet*/
//                case 2550:
//                    ergHVParam.paramPortal.botschaftenText = liefereErgInt(i);
//                    break;
                case 2551:
                    hString= liefereErgString(i);
                    for (int i1=1;i1<=15;i1++) {
                        if (i1<hString.length()) {
                            ergHVParam.paramPortal.botschaftenTextFormate[i1]=Integer.parseInt(hString.substring(i1, i1+1));
                        }
                    }
                    break;
                case 2552:
                    ergHVParam.paramPortal.wortmeldungArt = liefereErgInt(i);
                    break;

                case 2553:
                    ergHVParam.paramPortal.botschaftenVideoLaenge = liefereErgInt(i);
                    break;
                case 2554:
                    ergHVParam.paramPortal.botschaftenLaenge = liefereErgInt(i);
                    break;

                case 2555:
                    ergHVParam.paramPortal.fragenHinweisGelesen = liefereErgInt(i);
                    break;
                case 2556:
                    ergHVParam.paramPortal.wortmeldungHinweisGelesen = liefereErgInt(i);
                    break;
                case 2557:
                    ergHVParam.paramPortal.widerspruecheHinweisGelesen = liefereErgInt(i);
                    break;
                case 2558:
                    ergHVParam.paramPortal.antraegeHinweisGelesen = liefereErgInt(i);
                    break;
                case 2559:
                    ergHVParam.paramPortal.sonstMitteilungenHinweisGelesen = liefereErgInt(i);
                    break;
                case 2560:
                    ergHVParam.paramPortal.botschaftenHinweisGelesen = liefereErgInt(i);
                    break;

                case 2561:
                    ergHVParam.paramPortal.botschaftenVoranmeldungErforderlich = liefereErgInt(i);
                    break;
                case 2562:
                    ergHVParam.paramPortal.botschaftenTextDatei = liefereErgInt(i);
                    break;
                case 2563:
                    ergHVParam.paramPortal.botschaftenTextDateiLaenge = liefereErgInt(i);
                    break;
                case 2564:
                    ergHVParam.paramPortal.fragenRueckfragenErmoeglichen = liefereErgInt(i);
                    break;

                case 2565:
                    ergHVParam.paramPortal.fragenHinweisVorbelegenMit = liefereErgInt(i);
                    break;
                case 2566:
                    ergHVParam.paramPortal.wortmeldungHinweisVorbelegenMit = liefereErgInt(i);
                    break;
                case 2567:
                    ergHVParam.paramPortal.widerspruecheHinweisVorbelegenMit = liefereErgInt(i);
                    break;
                case 2568:
                    ergHVParam.paramPortal.antraegeHinweisVorbelegenMit = liefereErgInt(i);
                    break;
                case 2569:
                    ergHVParam.paramPortal.sonstMitteilungenHinweisVorbelegenMit = liefereErgInt(i);
                    break;
                case 2570:
                    ergHVParam.paramPortal.botschaftenHinweisVorbelegenMit = liefereErgInt(i);
                    break;

                case 2571:
                    ergHVParam.paramPortal.shrinkFormat = liefereErgString(i);
                    break;
                case 2572:
                    ergHVParam.paramPortal.shrinkAufloesung = liefereErgString(i);
                    break;

                case 2573:
                    ergHVParam.paramPortal.fragezeichenHinweiseVerwenden = liefereErgInt(i);
                    break;

                case 2574:
                    ergHVParam.paramPortal.emailBestaetigenIstZwingend = liefereErgInt(i);
                    break;

                case 2575:
                    ergHVParam.paramPortal.botschaftenLangtextUndDateiNurAlternativ = liefereErgInt(i);
                    break;

                case 2576:
                    ergHVParam.paramPortal.dialogveranstaltungAktiv = liefereErgInt(i);
                    break;
                case 2577:
                    ergHVParam.paramPortal.farbeLadebalkenUploadLeer = liefereErgString(i);
                    break;
                case 2578:
                    ergHVParam.paramPortal.farbeLadebalkenUploadFull = liefereErgString(i);
                    break;

                case 2579:
                    ergHVParam.paramPortal.inHVPortalKeineEinstellungenFuerAktionaere = liefereErgInt(i);
                    break;
                case 2580:
                    ergHVParam.paramPortal.inHVPortalKeineEmailUndKeinPasswortFuerAktionaere = liefereErgInt(i);
                    break;
                case 2581:
                    ergHVParam.paramPortal.vollmachtsnachweisAufStartseiteAktiv = liefereErgInt(i);
                    break;
                case 2582:
                    ergHVParam.paramPortal.freiwilligeAnmeldungEKDruckMoeglich = liefereErgInt(i);
                    break;
                case 2583:
                    ergHVParam.paramPortal.registerAnbindungZurHV = liefereErgInt(i);
                    break;
                case 2584:
                    ergHVParam.paramPortal.registerAnbindungVonHV = liefereErgInt(i);
                    break;
                case 2585:
                    ergHVParam.paramPortal.mailEingabeServiceline = liefereErgInt(i);
                    break;

                case 2586:
                    ergHVParam.paramBasis.profileKlasse = liefereErgString(i);
                    break;
                case 2587:
                    ergHVParam.paramPortal.freiwilligeAnmeldungNurPapier = liefereErgInt(i);
                    break;

                case 2588:
                    ergHVParam.paramPortal.onlineAbstimmungBerechtigungSeparatPruefen = liefereErgInt(i);
                    break;
                case 2589:
                    ergHVParam.paramPortal.hybridTeilnahmeAktiv = liefereErgInt(i);
                    break;
                    
                case 2590:
                    ergHVParam.paramPortal.freiwilligeAnmeldungMitVertretereingabe = liefereErgInt(i);
                    break;
                case 2591:
                    ergHVParam.paramPortal.freiwilligeAnmeldungZweiPersonenFuerMinderjaehrigeMoeglich = liefereErgInt(i);
                    break;

                case 2592:
                    ergHVParam.paramAkkreditierung.beiZugangSelbstVertreterIgnorierenZulaessig = liefereErgInt(i);
                    break;
                case 2593:
                    ergHVParam.paramAkkreditierung.ungepruefteKartenNichtBuchen = liefereErgInt(i);
                    break;
                case 2594:
                    ergHVParam.paramAkkreditierung.auszugebendeStimmkartenInMeldungAnzeigen = liefereErgInt(i);
                    break;

                case 2595:
                    ergHVParam.paramPortal.fragenKontaktdatenTelefonAbfragen = liefereErgInt(i);
                    break;
                case 2596:
                    ergHVParam.paramPortal.wortmeldungKontaktdatenTelefonAbfragen = liefereErgInt(i);
                    break;
                case 2597:
                    ergHVParam.paramPortal.widerspruecheKontaktdatenTelefonAbfragen = liefereErgInt(i);
                    break;
                case 2598:
                    ergHVParam.paramPortal.sonstMitteilungenKontaktdatenTelefonAbfragen = liefereErgInt(i);
                    break;
                case 2599:
                    ergHVParam.paramPortal.botschaftenKontaktdatenTelefonAbfragen = liefereErgInt(i);
                    break;

                case 2600:
                case 2601:
                case 2602:
                case 2603:
                case 2604:
                case 2605:
                case 2606:
                case 2607:
                case 2608:
                case 2609:
                    int offset=ergebnisArray[i].ident;
                    hString=liefereErgString(i);
                    for (int i1=0;i1<6;i1++) {
                        if (i1<hString.length()) {
                            ergHVParam.paramPortal.inhaltsHinweiseAktiv[offset-2600][i1]=hString.substring(i1, i1+1).equals("1");
                        }
                    }
                    break;
                    
                case 2610:
                    ergHVParam.paramPortal.wortmeldungTestDurchfuehren = liefereErgInt(i);
                    break;
                case 2611:
                    ergHVParam.paramPortal.wortmeldungRedeAufrufZweitenVersuchDurchfuehren = liefereErgInt(i);
                    break;
                case 2612:
                    ergHVParam.paramPortal.konfRaumAnzahl[0] = liefereErgInt(i);
                    break;
                case 2613:
                    ergHVParam.paramPortal.konfRaumAnzahl[1] = liefereErgInt(i);
                    break;
                case 2614:
                    ergHVParam.paramPortal.konfGlobaleTestraeumeNutzen = liefereErgInt(i);
                    break;
                case 2615:
                    ergHVParam.paramPortal.wortmeldungNachTestManuellInRednerlisteAufnehmen = liefereErgInt(i);
                    break;
                case 2616:
                    ergHVParam.paramPortal.zuschaltungHVAutomatischNachLogin = liefereErgInt(i);
                    break;

                case 2617:
                    hString=liefereErgString(i);
                    for (int i1=0;i1<5;i1++) {
                        if (i1<hString.length()) {
                            ergHVParam.paramPortal.veranstaltungenAktivFuerGattung[i1]=hString.substring(i1, i1+1).equals("1");
                        }
                    }
                    break;
                case 2618:
                    hString=liefereErgString(i);
                    for (int i1=0;i1<5;i1++) {
                        if (i1<hString.length()) {
                            ergHVParam.paramPortal.freiwilligeAnmeldungAktivFuerGattung[i1]=hString.substring(i1, i1+1).equals("1");
                        }
                    }
                    break;

                case 2619:
                    ergHVParam.paramPortal.wortmeldetischSetNr = liefereErgInt(i);
                    break;
                case 2620:
                    ergHVParam.paramPortal.konfBackupAktiv = liefereErgInt(i);
                    break;
                    
                case 2621:
                    ergHVParam.paramPortal.konfRaumBIdPW[0][0] = liefereErgString(i);
                    break;
                case 2622:
                    ergHVParam.paramPortal.konfRaumBIdPW[0][1] = liefereErgString(i);
                    break;
                case 2623:
                    ergHVParam.paramPortal.konfRaumBIdPW[0][2] = liefereErgString(i);
                    break;
                    
                case 2624:
                    ergHVParam.paramPortal.konfRaumBIdPW[1][0] = liefereErgString(i);
                    break;
                case 2625:
                    ergHVParam.paramPortal.konfRaumBIdPW[1][1] = liefereErgString(i);
                    break;
                case 2626:
                    ergHVParam.paramPortal.konfRaumBIdPW[1][2] = liefereErgString(i);
                    break;

                case 2627:
                    ergHVParam.paramPortal.wortmeldungInhaltsHinweiseAktiv = liefereErgInt(i);
                    break;

                case 2628:
                    ergHVParam.paramPortal.schriftgroesseVersammlunsleiterView = liefereErgString(i);
                    break;

                case 2629:
                    ergHVParam.paramPortal.jnAbfrageBeiWeisungQuittung = liefereErgInt(i);
                    break;

                case 2630:
                    ergHVParam.paramPortal.emailVersandRegistrierungOderWiderspruch = liefereErgInt(i);
                    break;
                case 2631:
                    ergHVParam.paramPortal.fragenZurueckziehenMoeglichNurWennAktiv = liefereErgInt(i);
                    break;
                case 2632:
                    ergHVParam.paramPortal.wortmeldungZurueckziehenMoeglichNurWennAktiv = liefereErgInt(i);
                    break;
                case 2633:
                    ergHVParam.paramPortal.widerspruecheZurueckziehenMoeglichNurWennAktiv = liefereErgInt(i);
                    break;
                case 2634:
                    ergHVParam.paramPortal.antraegeZurueckziehenMoeglichNurWennAktiv = liefereErgInt(i);
                    break;
                case 2635:
                    ergHVParam.paramPortal.sonstMitteilungenZurueckziehenMoeglichNurWennAktiv = liefereErgInt(i);
                    break;
                case 2636:
                    ergHVParam.paramPortal.botschaftenZurueckziehenMoeglichNurWennAktiv = liefereErgInt(i);
                    break;
                case 2637:
                    ergHVParam.paramPortal.zuschaltungHVStreamAutomatischStarten = liefereErgInt(i);
                    break;
                case 2638:
                    ergHVParam.paramPortal.onlineTeilnahmeTeilnehmerNameBestaetigen = liefereErgInt(i);
                    break;
                case 2639:
                    ergHVParam.paramPortal.bestaetigungStimmabgabeNachHV = liefereErgInt(i);
                    break;
                case 2640:
                    ergHVParam.paramPortal.ku178SepaIstAktiv = liefereErgInt(i);
                    break;
                case 2644:
                    ergHVParam.paramPortal.wortmeldungNurEineOffeneZulaessig = liefereErgInt(i);
                    break;
                case 2645:
                    ergHVParam.paramPortal.srvZusaetzlichZuBriefwahlMoeglich = liefereErgInt(i);
                    break;
                case 2646:
                    ergHVParam.paramPortal.briefwahlAlsOnlineStimmabgabe = liefereErgInt(i);
                    break;
                case 2647:
                    ergHVParam.paramPortal.multiUserImPortalIgnorieren = liefereErgInt(i);
                    break;
                case 2648:
                    ergHVParam.paramPortal.weisungenAktuellNichtMoeglichAberBriefwahlSchon = liefereErgInt(i);
                    break;

                case 2649:
                    ergHVParam.paramPortal.antraegeTextInOberflaecheLang = liefereErgString(i);
                    break;
                case 2650:
                    ergHVParam.paramPortal.antraegeTextInOberflaecheKurz = liefereErgString(i);
                    break;
                case 2651:
                    ergHVParam.paramPortal.botschaftenTextInOberflaecheLang = liefereErgString(i);
                    break;
                case 2652:
                    ergHVParam.paramPortal.botschaftenTextInOberflaecheKurz = liefereErgString(i);
                    break;
                case 2653:
                    ergHVParam.paramPortal.fragenTextInOberflaecheLang = liefereErgString(i);
                    break;
                case 2654:
                    ergHVParam.paramPortal.fragenTextInOberflaecheKurz = liefereErgString(i);
                    break;
                case 2655:
                    ergHVParam.paramPortal.sonstMitteilungenTextInOberflaecheLang = liefereErgString(i);
                    break;
                case 2656:
                    ergHVParam.paramPortal.sonstMitteilungenTextInOberflaecheKurz = liefereErgString(i);
                    break;
                case 2657:
                    ergHVParam.paramPortal.widerspruecheTextInOberflaecheLang = liefereErgString(i);
                    break;
                case 2658:
                    ergHVParam.paramPortal.widerspruecheTextInOberflaecheKurz = liefereErgString(i);
                    break;

                case 2659:
                    ergHVParam.paramPortal.ekVersandFuerAlleImPortalAngeforderten = liefereErgInt(i);
                    break;
                case 2660:
                    ergHVParam.paramPortal.wortmeldungTextInOberflaecheLang = liefereErgString(i);
                    break;
                case 2661:
                    ergHVParam.paramPortal.wortmeldungTextInOberflaecheKurz = liefereErgString(i);
                    break;
                    
                case 2662:
                    ergHVParam.paramPortal.antraegeKontaktdatenTelefonAbfragen = liefereErgInt(i);
                    break;

                case 2663:
                    ergHVParam.paramPortal.emailVersandZweitEMailAusRegister = liefereErgInt(i);
                    break;

                case 2664:
                    ergHVParam.paramBasis.hoechststimmrechtHackAktiv = liefereErgInt(i);
                    break;
                case 2665:
                    ergHVParam.paramBasis.stimmenBeiPraesenzReduzierenUmStimmen = liefereErgInt(i);
                    break;

                    /**Noch frei bis 2669*/
                    
                /**Basis*/
                case 2670:
                case 2671:
                case 2672:
                case 2673:
                case 2674:
                case 2675:
                case 2676:
                case 2677:
                case 2678:
                case 2679:
                    offset=ergebnisArray[i].ident;
                    hInt=liefereErgInt(i);
                    ergHVParam.paramBasis.veranstaltungstyp[offset-2670]=hInt;
                    break;
                    
                
                    
                   /*INFO hier fortsetzen*/
                    
               }

                
                /***********3001 bis 3100 - autoDruckerVerwendung****************************/
                if (ergebnisArray[i].ident>=3001 && ergebnisArray[i].ident<=3100) {
                    ergHVParam.paramBasis.autoDruckerVerwendung[ergebnisArray[i].ident-3000] = liefereErgInt(i);
                }
              
                /***********************700: Stimmblockzuordnung (Fortsetzung - siehe auch 18 ff)***********************/

                if (ergebnisArray[i].ident >= 700 && ergebnisArray[i].ident < 725) {
                    int offset = ergebnisArray[i].ident - 700;
                    int offset1 = offset / 5;
                    int offset2 = offset % 5;
                    ergHVParam.paramAkkreditierung.pPraesenzStimmkartenZuordnenGattungText[offset1][offset2] = liefereErgString(
                            i);
                }
                if (ergebnisArray[i].ident >= 725 && ergebnisArray[i].ident < 730) {
                    int offset = ergebnisArray[i].ident - 725;
                    ergHVParam.paramAkkreditierung.pPraesenzStimmkarteSecondZuordnenGattungText[offset] = liefereErgString(
                            i);
                }

                /***************800: Präsenzliste**************************/
                if (ergebnisArray[i].ident >= 800 && ergebnisArray[i].ident < 812) {
                    int offset = ergebnisArray[i].ident - 800;
                    int offset1 = offset / 4;//i
                    int offset2 = offset % 4;//i1
                    //					System.out.println("offset="+offset+" offset1="+offset1+" offset2="+offset2);
                    hString = liefereErgString(i);
                    for (int i2 = 0; i2 <= 5; i2++) {/*0=alle, i2 = Gattung*/
                        //						System.out.println("i2="+i2);
                        ergHVParam.paramPraesenzliste.einzeldruckFormatListe[offset1][offset2][i2] = hString
                                .substring(i2 * 6, i2 * 6 + 2);
                        ergHVParam.paramPraesenzliste.einzeldruckFormatZusammenstellung[offset1][offset2][i2] = hString
                                .substring(i2 * 6 + 2, i2 * 6 + 2 + 2);
                        ergHVParam.paramPraesenzliste.einzeldruckInPDFaufnehmen[offset1][offset2][i2] = Integer
                                .parseInt(hString.substring(i2 * 6 + 4, i2 * 6 + 4 + 1));
                    }

                }

                /****************1001-1500: ablaufAbstimmung******************************************/
                if (ergebnisArray[i].ident > 1000 && ergebnisArray[i].ident <= 1500) {
                    int offsetInArray = ergebnisArray[i].ident - 1001;
                    if (offsetInArray < ergHVParam.paramAbstimmungParameter.ablaufAbstimmung.length) {
                        ergHVParam.paramAbstimmungParameter.ablaufAbstimmung[offsetInArray] = liefereErgInt(i);
                        ergHVParam.paramAbstimmungParameter.ablaufAbstimmungErledigt[offsetInArray] = false;
                    } else {
                        CaBug.drucke("DbParameter.readHVParam_all 004 - ablaufAbstimmungsarray zu klein");
                    }
                }

                /****************90001-90040 (reserviert bis 9200): EclPortalFunktion******************************************/
                if (ergebnisArray[i].ident >= 90001 && ergebnisArray[i].ident <= 90040) {
                    int offsetInArray = ergebnisArray[i].ident - 90000;
                    hString = CaString.fuelleRechtsBlank(liefereErgString(i), 40);
                    EclPortalFunktion lPortalFunktion = new EclPortalFunktion();
                    if (hString.substring(0, 1).equals("1")) {
                        lPortalFunktion.wirdAngeboten = true;
                    }

                    if (hString.substring(1, 2).equals("1")) {
                        lPortalFunktion.berechtigtGast1 = true;
                    }
                    if (hString.substring(2, 3).equals("1")) {
                        lPortalFunktion.berechtigtGast2 = true;
                    }
                    if (hString.substring(3, 4).equals("1")) {
                        lPortalFunktion.berechtigtGast3 = true;
                    }
                    if (hString.substring(4, 5).equals("1")) {
                        lPortalFunktion.berechtigtGast4 = true;
                    }
                    if (hString.substring(5, 6).equals("1")) {
                        lPortalFunktion.berechtigtGast5 = true;
                    }
                    if (hString.substring(6, 7).equals("1")) {
                        lPortalFunktion.berechtigtGast6 = true;
                    }
                    if (hString.substring(7, 8).equals("1")) {
                        lPortalFunktion.berechtigtGast7 = true;
                    }
                    if (hString.substring(8, 9).equals("1")) {
                        lPortalFunktion.berechtigtGast8 = true;
                    }
                    if (hString.substring(9, 10).equals("1")) {
                        lPortalFunktion.berechtigtGast9 = true;
                    }
                    if (hString.substring(10, 11).equals("1")) {
                        lPortalFunktion.berechtigtGast10 = true;
                    }

                    if (hString.substring(11, 12).equals("1")) {
                        lPortalFunktion.berechtigtGastOnlineTeilnahmer1 = true;
                    }
                    if (hString.substring(12, 13).equals("1")) {
                        lPortalFunktion.berechtigtGastOnlineTeilnahmer2 = true;
                    }
                    if (hString.substring(13, 14).equals("1")) {
                        lPortalFunktion.berechtigtGastOnlineTeilnahmer3 = true;
                    }
                    if (hString.substring(14, 15).equals("1")) {
                        lPortalFunktion.berechtigtGastOnlineTeilnahmer4 = true;
                    }
                    if (hString.substring(15, 16).equals("1")) {
                        lPortalFunktion.berechtigtGastOnlineTeilnahmer5 = true;
                    }
                    if (hString.substring(16, 17).equals("1")) {
                        lPortalFunktion.berechtigtGastOnlineTeilnahmer6 = true;
                    }
                    if (hString.substring(17, 18).equals("1")) {
                        lPortalFunktion.berechtigtGastOnlineTeilnahmer7 = true;
                    }
                    if (hString.substring(18, 19).equals("1")) {
                        lPortalFunktion.berechtigtGastOnlineTeilnahmer8 = true;
                    }
                    if (hString.substring(19, 20).equals("1")) {
                        lPortalFunktion.berechtigtGastOnlineTeilnahmer9 = true;
                    }
                    if (hString.substring(20, 21).equals("1")) {
                        lPortalFunktion.berechtigtGastOnlineTeilnahmer10 = true;
                    }

                    if (hString.substring(21, 22).equals("1")) {
                        lPortalFunktion.berechtigtAktionaer = true;
                    }
                    if (hString.substring(22, 23).equals("1")) {
                        lPortalFunktion.berechtigtAngemeldeterAktionaer = true;
                    }
                    if (hString.substring(23, 24).equals("1")) {
                        lPortalFunktion.berechtigtOnlineTeilnahmeAktionaer = true;
                    }

                    lPortalFunktion.aktiv = Integer.parseInt(hString.substring(24, 25));

                    ergHVParam.paramPortal.eclPortalFunktion[offsetInArray] = lPortalFunktion;
                }

                /****************90201-90220 (reserviert bis 90400): EclPortalPhase******************************************/
                if (ergebnisArray[i].ident >= 90201 && ergebnisArray[i].ident <= 90220) {
                    int offsetInArray = ergebnisArray[i].ident - 90200;
                    hString = CaString.fuelleRechtsBlank(liefereErgString(i), 40);
                    EclPortalPhase lPortalPhase = new EclPortalPhase();

                    if (hString.substring(0, 1).equals("1")) {
                        lPortalPhase.gewinnspielAktiv = true;
                    }
                    if (hString.substring(1, 2).equals("1")) {
                        lPortalPhase.lfdHVPortalInBetrieb = true;
                    }

                    lPortalPhase.lfdVorDerHVNachDerHV = Integer.parseInt(hString.substring(2, 3));

                    if (hString.substring(3, 4).equals("1")) {
                        lPortalPhase.lfdHVPortalErstanmeldungIstMoeglich = true;
                    }
                    if (hString.substring(4, 5).equals("1")) {
                        lPortalPhase.lfdHVPortalEKIstMoeglich = true;
                    }
                    if (hString.substring(5, 6).equals("1")) {
                        lPortalPhase.lfdHVPortalSRVIstMoeglich = true;
                    }
                    if (hString.substring(6, 7).equals("1")) {
                        lPortalPhase.lfdHVPortalBriefwahlIstMoeglich = true;
                    }
                    if (hString.substring(7, 8).equals("1")) {
                        lPortalPhase.lfdHVPortalKIAVIstMoeglich = true;
                    }
                    if (hString.substring(8, 9).equals("1")) {
                        lPortalPhase.lfdHVPortalVollmachtDritteIstMoeglich = true;
                    }

                    if (hString.substring(9, 10).equals("1")) {
                        lPortalPhase.lfdHVStreamIstMoeglich = true;
                    }
                    if (hString.substring(10, 11).equals("1")) {
                        lPortalPhase.lfdHVFragenIstMoeglich = true;
                    }
                    if (hString.substring(11, 12).equals("1")) {
                        lPortalPhase.lfdHVWortmeldungenIstMoeglich = true;
                    }
                    if (hString.substring(12, 13).equals("1")) {
                        lPortalPhase.lfdHVWiderspruecheIstMoeglich = true;
                    }
                    if (hString.substring(13, 14).equals("1")) {
                        lPortalPhase.lfdHVAntraegeIstMoeglich = true;
                    }
                    if (hString.substring(14, 15).equals("1")) {
                        lPortalPhase.lfdHVSonstigeMitteilungenIstMoeglich = true;
                    }
                    if (hString.substring(15, 16).equals("1")) {
                        lPortalPhase.lfdHVChatIstMoeglich = true;
                    }
                    if (hString.substring(16, 17).equals("1")) {
                        lPortalPhase.lfdHVUnterlagenGruppe1IstMoeglich = true;
                    }
                    if (hString.substring(17, 18).equals("1")) {
                        lPortalPhase.lfdHVUnterlagenGruppe2IstMoeglich = true;
                    }
                    if (hString.substring(18, 19).equals("1")) {
                        lPortalPhase.lfdHVUnterlagenGruppe3IstMoeglich = true;
                    }
                    if (hString.substring(19, 20).equals("1")) {
                        lPortalPhase.lfdHVUnterlagenGruppe4IstMoeglich = true;
                    }
                    if (hString.substring(20, 21).equals("1")) {
                        lPortalPhase.lfdHVUnterlagenGruppe5IstMoeglich = true;
                    }
                    if (hString.substring(21, 22).equals("1")) {
                        lPortalPhase.lfdHVTeilnehmerverzIstMoeglich = true;
                    }
                    if (hString.substring(22, 23).equals("1")) {
                        lPortalPhase.lfdHVAbstimmungsergIstMoeglich = true;
                    }

                    if (hString.substring(23, 24).equals("1")) {
                        lPortalPhase.manuellAktiv = true;
                    }
                    if (hString.substring(24, 25).equals("1")) {
                        lPortalPhase.manuellDeaktiv = true;
                    }
                    if (hString.substring(25, 26).equals("1")) {
                        lPortalPhase.lfdHVBotschaftenEinreichenIstMoeglich = true;
                    }
                    if (hString.substring(26, 27).equals("1")) {
                        lPortalPhase.lfdHVBotschaftenIstMoeglich = true;
                    }
                    if (hString.substring(27, 28).equals("1")) {
                        lPortalPhase.lfdHVRueckfragenIstMoeglich = true;
                    }

                    ergHVParam.paramPortal.eclPortalPhase[offsetInArray] = lPortalPhase;
                }

                /****************91001-92000 : EclPortalUnterlagen (analog auch in lang)******************************************/
                /*Hinweis: nur vorläufg noch enthalten, um Übergang zu ermöglichen. Eine Rückspeicherung in die Parameter erfolgt
                 * nicht mehr. Sobald in der neuen tbl_unterlagen etwas enthalten ist, wird das hier dort dann überschrieben
                 */
                if (ergebnisArray[i].ident >= 91001 && ergebnisArray[i].ident <= 92000) {
                    hString = CaString.fuelleRechtsBlank(liefereErgString(i), 40);
                    EclPortalUnterlagen lPortalUnterlagen = new EclPortalUnterlagen();

                    lPortalUnterlagen.dateiMehrsprachigVorhanden=1;
                    
                    lPortalUnterlagen.reihenfolgeUnterlagen = Integer.parseInt(hString.substring(0, 5));
                    //					if (hString.substring(5, 6).equals("1")) {lPortalUnterlagen.wirdAngeboten=true;}
                    lPortalUnterlagen.art = Integer.parseInt(hString.substring(6, 7));

                    if (hString.substring(7, 8).equals("1")) {
                        lPortalUnterlagen.berechtigtGast1 = true;
                    }
                    if (hString.substring(8, 9).equals("1")) {
                        lPortalUnterlagen.berechtigtGast2 = true;
                    }
                    if (hString.substring(9, 10).equals("1")) {
                        lPortalUnterlagen.berechtigtGast3 = true;
                    }
                    if (hString.substring(10, 11).equals("1")) {
                        lPortalUnterlagen.berechtigtGast4 = true;
                    }
                    if (hString.substring(11, 12).equals("1")) {
                        lPortalUnterlagen.berechtigtGast5 = true;
                    }
                    if (hString.substring(12, 13).equals("1")) {
                        lPortalUnterlagen.berechtigtGast6 = true;
                    }
                    if (hString.substring(13, 14).equals("1")) {
                        lPortalUnterlagen.berechtigtGast7 = true;
                    }
                    if (hString.substring(14, 15).equals("1")) {
                        lPortalUnterlagen.berechtigtGast8 = true;
                    }
                    if (hString.substring(15, 16).equals("1")) {
                        lPortalUnterlagen.berechtigtGast9 = true;
                    }
                    if (hString.substring(16, 17).equals("1")) {
                        lPortalUnterlagen.berechtigtGast10 = true;
                    }

                    if (hString.substring(17, 18).equals("1")) {
                        lPortalUnterlagen.berechtigtGastOnlineTeilnahmer1 = true;
                    }
                    if (hString.substring(18, 19).equals("1")) {
                        lPortalUnterlagen.berechtigtGastOnlineTeilnahmer2 = true;
                    }
                    if (hString.substring(19, 20).equals("1")) {
                        lPortalUnterlagen.berechtigtGastOnlineTeilnahmer3 = true;
                    }
                    if (hString.substring(20, 21).equals("1")) {
                        lPortalUnterlagen.berechtigtGastOnlineTeilnahmer4 = true;
                    }
                    if (hString.substring(21, 22).equals("1")) {
                        lPortalUnterlagen.berechtigtGastOnlineTeilnahmer5 = true;
                    }
                    if (hString.substring(22, 23).equals("1")) {
                        lPortalUnterlagen.berechtigtGastOnlineTeilnahmer6 = true;
                    }
                    if (hString.substring(23, 24).equals("1")) {
                        lPortalUnterlagen.berechtigtGastOnlineTeilnahmer7 = true;
                    }
                    if (hString.substring(24, 25).equals("1")) {
                        lPortalUnterlagen.berechtigtGastOnlineTeilnahmer8 = true;
                    }
                    if (hString.substring(25, 26).equals("1")) {
                        lPortalUnterlagen.berechtigtGastOnlineTeilnahmer9 = true;
                    }
                    if (hString.substring(26, 27).equals("1")) {
                        lPortalUnterlagen.berechtigtGastOnlineTeilnahmer10 = true;
                    }

                    if (hString.substring(27, 28).equals("1")) {
                        lPortalUnterlagen.berechtigtAktionaer = true;
                    }
                    if (hString.substring(28, 29).equals("1")) {
                        lPortalUnterlagen.berechtigtAngemeldeterAktionaer = true;
                    }
                    if (hString.substring(29, 30).equals("1")) {
                        lPortalUnterlagen.berechtigtOnlineTeilnahmeAktionaer = true;
                    }

                    lPortalUnterlagen.aktiv = Integer.parseInt(hString.substring(30, 31));
                    lPortalUnterlagen.dateiname = hString.substring(31, 39).trim();

                    ergHVParam.paramPortal.eclPortalUnterlagen.add(lPortalUnterlagen);
                }

                i++;

            }

            /*Nun noch Parameter aus EclEmittenten übertragen*/
            if (dbBundle.eclEmittent != null) {
                ergHVParam.paramModuleKonfigurierbar.aktionaersportal = liefereBoolToInt(
                        dbBundle.eclEmittent.portalVorhanden);
                ergHVParam.paramModuleKonfigurierbar.emittentenportal = liefereBoolToInt(
                        dbBundle.eclEmittent.emittentenPortalVorhanden);
                ergHVParam.paramModuleKonfigurierbar.registeranbindung = liefereBoolToInt(
                        dbBundle.eclEmittent.registerAnbindungVorhanden);
                ergHVParam.paramModuleKonfigurierbar.hvApp = liefereBoolToInt(dbBundle.eclEmittent.appVorhanden);
            } else {
                CaBug.druckeInfo("DbParameter.readHVParam_all - eclEmittent dürfte nicht null sein");
            }

            lErgebnis.close();
            lPStm.close();

        } catch (Exception e) {
            CaBug.drucke("003");
            System.err.println(" " + e.getMessage());
            e.printStackTrace();
            return (-1);
        }

        /******Nun noch parameterLang - nach selbem Verfahren*************/
        try {
            String lSql = "SELECT * from " + liefereTblParameterLang() + " where " + "mandant=? OR mandant=0 "
                    + "ORDER BY ident, mandant;";
            lPStm = verbindung.prepareStatement(lSql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            lPStm.setInt(1, dbBundle.clGlobalVar.mandant);

            ResultSet lErgebnis = executeQuery(lPStm);
            lErgebnis.last();
            anzInArray = lErgebnis.getRow();
            lErgebnis.beforeFirst();

            ergebnisArray = new EclParameter[anzInArray];

            int i = 0;
            while (lErgebnis.next() == true) {
                ergebnisArray[i] = this.decodeErgebnis(lErgebnis);

                switch (ergebnisArray[i].ident) {

                /*PortalParam*/
                case 1:
                    ergHVParam.paramPortalServer.httpPfadTeil1 = liefereErgString(i);
                    break;
                case 2:
                    ergHVParam.paramPortalServer.httpPfadTeil2 = liefereErgString(i);
                    break;
                case 3:
                    ergHVParam.paramPortal.stimmrechtsvertreterNameDE = liefereErgString(i);
                    break;
                case 4:
                    ergHVParam.paramPortal.linkTagesordnung = liefereErgString(i);
                    break;
                case 5:
                    ergHVParam.paramPortal.linkGegenantraege = liefereErgString(i);
                    break;
                case 6:
                    ergHVParam.paramPortal.linkEinladungsPDF = liefereErgString(i);
                    break;
                case 7:
                    ergHVParam.paramPortal.emailAdresseLink = liefereErgString(i);
                    break;
                case 8:
                    ergHVParam.paramPortal.emailAdresseText = liefereErgString(i);
                    break;
                case 9:
                    ergHVParam.paramPortal.stimmrechtsvertreterNameEN = liefereErgString(i);
                    break;
                case 10:
                    ergHVParam.paramAbstimmungParameter.veroeffentlichtImBundesanzeigerVom = liefereErgString(i);
                    break;
                case 11:
                    ergHVParam.paramPortal.loginGesperrtTextDeutsch = liefereErgString(i);
                    break;
                case 12:
                    ergHVParam.paramPortal.loginGesperrtTextEnglisch = liefereErgString(i);
                    break;
                case 13:
                    ergHVParam.paramPortal.linkNutzungsbedingungenAktionaersPortal = liefereErgString(i);
                    break;
                case 14:
                    ergHVParam.paramPortal.linkNutzungsbedingungenHVPortal = liefereErgString(i);
                    break;
                case 15:
                    ergHVParam.paramPortal.linkDatenschutzhinweise = liefereErgString(i);
                    break;
                case 16:
                    ergHVParam.paramPortal.linkImpressum = liefereErgString(i);
                    break;
                case 17:
                    ergHVParam.paramPortal.streamLink = liefereErgString(i).trim();
                    break;
                case 18:
                    ergHVParam.paramPortal.streamID = liefereErgString(i).trim();
                    break;
                case 19:
                    ergHVParam.paramPortal.linkDatenschutzhinweiseKunde = liefereErgString(i);
                    break;
                case 20:
                    ergHVParam.paramPortal.logoutZiel = liefereErgString(i);
                    break;

                case 21:
                    ergHVParam.paramPortal.widerspruecheMailVerteiler1 = liefereErgString(i);
                    break;
                case 22:
                    ergHVParam.paramPortal.widerspruecheMailVerteiler2 = liefereErgString(i);
                    break;
                case 23:
                    ergHVParam.paramPortal.widerspruecheMailVerteiler3 = liefereErgString(i);
                    break;

                case 24:
                    ergHVParam.paramPortal.fragenMailVerteiler1 = liefereErgString(i);
                    break;
                case 25:
                    ergHVParam.paramPortal.fragenMailVerteiler2 = liefereErgString(i);
                    break;
                case 26:
                    ergHVParam.paramPortal.fragenMailVerteiler3 = liefereErgString(i);
                    break;

                /*27 bis 36...: unterlageButton1DE etc - entfernt 08.01.2022*/
                /*37 bis 46...: unterlageButton1EN etc - entfernt 08.01.2022*/
 

                case 47:
                    ergHVParam.paramPortal.streamLink2 = liefereErgString(i).trim();
                    break;
                case 48:
                    ergHVParam.paramPortal.streamID2 = liefereErgString(i).trim();
                    break;

                /*49 bis 58...: unterlageButton11DE etc - entfernt 08.01.2022*/
                /*59 bis 68...: unterlageButton11EN etc - entfernt 08.01.2022*/

                case 69:
                    ergHVParam.paramPortal.wortmeldungMailVerteiler1 = liefereErgString(i);
                    break;
                case 70:
                    ergHVParam.paramPortal.wortmeldungMailVerteiler2 = liefereErgString(i);
                    break;
                case 71:
                    ergHVParam.paramPortal.wortmeldungMailVerteiler3 = liefereErgString(i);
                    break;

                case 72:
                    ergHVParam.paramPortal.antraegeMailVerteiler1 = liefereErgString(i);
                    break;
                case 73:
                    ergHVParam.paramPortal.antraegeMailVerteiler2 = liefereErgString(i);
                    break;
                case 74:
                    ergHVParam.paramPortal.antraegeMailVerteiler3 = liefereErgString(i);
                    break;

                case 75:
                    ergHVParam.paramPortal.sonstMitteilungenMailVerteiler1 = liefereErgString(i);
                    break;
                case 76:
                    ergHVParam.paramPortal.sonstMitteilungenMailVerteiler2 = liefereErgString(i);
                    break;
                case 77:
                    ergHVParam.paramPortal.sonstMitteilungenMailVerteiler3 = liefereErgString(i);
                    break;
                case 78:
                    ergHVParam.paramBasis.mailConsultant = liefereErgString(i);
                    break;
                case 79:
                    ergHVParam.paramPortal.vollmachtEmailAdresseLink = liefereErgString(i);
                    break;
                case 80:
                    ergHVParam.paramPortal.vollmachtEmailAdresseText = liefereErgString(i);
                    break;
                case 81:
                    ergHVParam.paramPortal.kontaktformularBeiEingangMailAn = liefereErgString(i);
                    break;
                case 82:
                    ergHVParam.paramPortal.api_client_id = liefereErgString(i);
                    break;
                case 83:
                    ergHVParam.paramPortal.api_client_secret = liefereErgString(i);
                    break;
                case 84:
                    ergHVParam.paramPortal.api_base_url = liefereErgString(i);
                    break;
                case 85:
                    ergHVParam.paramPortal.api_key_id = liefereErgString(i);
                    break;
                case 86:
                    ergHVParam.paramPortal.api_ping_url = liefereErgString(i);
                    break;
                case 101:
                    ergHVParam.paramPortal.streamLink += liefereErgString(i).trim();
                    break;
                case 102:
                    ergHVParam.paramPortal.streamLink += liefereErgString(i).trim();
                    break;
                case 103:
                    ergHVParam.paramPortal.streamID += liefereErgString(i).trim();
                    break;
                case 104:
                    ergHVParam.paramPortal.streamID += liefereErgString(i).trim();
                    break;

                case 105:
                    ergHVParam.paramPortal.streamLink2 += liefereErgString(i).trim();
                    break;
                case 106:
                    ergHVParam.paramPortal.streamLink2 += liefereErgString(i).trim();
                    break;
                 case 107:
                    ergHVParam.paramPortal.streamID2 += liefereErgString(i).trim();
                    break;
                 case 108:
                     ergHVParam.paramPortal.streamID2 += liefereErgString(i).trim();
                     break;

                 /*109, 110, 111 - nicht mehr verwendet. Ehemals für konfTenandID, konfClientID, konfClientSecret*/
 
                 case 113:
                     ergHVParam.paramPortal.botschaftenMailVerteiler1 = liefereErgString(i);
                     break;
                 case 114:
                     ergHVParam.paramPortal.botschaftenMailVerteiler2 = liefereErgString(i);
                     break;
                 case 115:
                     ergHVParam.paramPortal.botschaftenMailVerteiler3 = liefereErgString(i);
                     break;
                 case 116:
                     ergHVParam.paramPortal.kurzLinkPortal = liefereErgString(i);
                     break;
                 case 117:
                     ergHVParam.paramPortal.subdomainPortal = liefereErgString(i);
                     break;

                 case 118:
                     String hString=liefereErgString(i);
                     if (hString.length()<175) {
                         while (hString.length()<175) {
                             hString=hString+1;
                         }
                         CaBug.druckeInfo("ParameterLang kürzer als 175 Zeichen");
                     }
                     /*0 bis 24: pPraesenzStimmkarteAktiv */
                     for (int i1 = 0; i1 < 5; i1++) { /*Für alle Gattungen*/
                         for (int i2 = 0; i2 < 5; i2++) { //Für alle Zuordnungen
                             int offset = i1 * 5 + i2;
                             ergHVParam.paramAkkreditierung.pPraesenzStimmkarteAktiv[i1][i2] = Integer.parseInt(hString.substring(offset, offset + 1));
                         }
                     }
                     /*25 bis 74: pPraesenzStimmkarteFormularnummer - jeweils 2 Zeichen */
                     for (int i1 = 0; i1 < 5; i1++) { /*Für alle Gattungen*/
                         for (int i2 = 0; i2 < 5; i2++) { //Für alle Zuordnungen
                             int offset = (i1 * 5 + i2)*2+25;
                             ergHVParam.paramAkkreditierung.pPraesenzStimmkarteFormularnummer[i1][i2] = hString.substring(offset, offset +2);
                         }
                     }
                     /*75 bis 99: pPraesenzStimmkarteTauschBeliebig*/
                     for (int i1 = 0; i1 < 5; i1++) { /*Für alle Gattungen*/
                         for (int i2 = 0; i2 < 5; i2++) { //Für alle Zuordnungen
                             int offset = (i1 * 5 + i2)+75;
                             ergHVParam.paramAkkreditierung.pPraesenzStimmkarteTauschBeliebig[i1][i2] = Integer.parseInt(hString.substring(offset, offset + 1));
                         }
                     }
                     /*100 bis 124: pPraesenzStimmkarteNachdruckServiceDesk*/
                     for (int i1 = 0; i1 < 5; i1++) { /*Für alle Gattungen*/
                         for (int i2 = 0; i2 < 5; i2++) { //Für alle Zuordnungen
                             int offset = (i1 * 5 + i2)+100;
                             ergHVParam.paramAkkreditierung.pPraesenzStimmkarteNachdruckServiceDesk[i1][i2] = Integer.parseInt(hString.substring(offset, offset + 1));
                         }
                     }
                     /*125 bis 174: pPraesenzStimmkarteDruckerBeiErstzugang - jeweils 2 Zeichen */
                     for (int i1 = 0; i1 < 5; i1++) { /*Für alle Gattungen*/
                         for (int i2 = 0; i2 < 5; i2++) { //Für alle Zuordnungen
                             int offset = (i1 * 5 + i2)*2+125;
                             ergHVParam.paramAkkreditierung.pPraesenzStimmkarteDruckerBeiErstzugang[i1][i2] = hString.substring(offset, offset +2).trim();
                         }
                     }
                     
                 case 119:
                     ergHVParam.paramPortal.apiku178_url = liefereErgString(i);
                     break;

                 case 120:
                 case 121:
                 case 122:
                 case 123:
                 case 124:
                 case 125:
                 case 126:
                 case 127:
                 case 128:
                 case 129:
                     ergHVParam.paramPortal.inhaltsHinweiseTextDE[ergebnisArray[i].ident-120]=liefereErgString(i);
                     break;
                     
                 case 130:
                 case 131:
                 case 132:
                 case 133:
                 case 134:
                 case 135:
                 case 136:
                 case 137:
                 case 138:
                 case 139:
                     ergHVParam.paramPortal.inhaltsHinweiseTextEN[ergebnisArray[i].ident-130]=liefereErgString(i);
                     break;

                 case 140:
                     ergHVParam.paramPortal.konfEventId=liefereErgString(i);
                     break;
                     
                 case 141:
                 case 142:
                 case 143:
                     ergHVParam.paramPortal.konfRaumId[0][ergebnisArray[i].ident-141]=liefereErgString(i);
                     break;
                 case 144:
                 case 145:
                 case 146:
                     ergHVParam.paramPortal.konfRaumId[1][ergebnisArray[i].ident-144]=liefereErgString(i);
                     break;

                 case 147:
                     ergHVParam.paramPortal.konfBackupServer=liefereErgString(i);
                     break;
                     
                 case 148:
                 case 149:
                 case 150:
                     ergHVParam.paramPortal.konfRaumBId[0][ergebnisArray[i].ident-148]=liefereErgString(i);
                     break;
                 case 151:
                 case 152:
                 case 153:
                     ergHVParam.paramPortal.konfRaumBId[1][ergebnisArray[i].ident-151]=liefereErgString(i);
                     break;

                     
                 case 154:
                 case 155:
                 case 156:
                     ergHVParam.paramPortal.konfRaumId[0][ergebnisArray[i].ident-154+3]=liefereErgString(i);
                     break;
                 case 157:
                 case 158:
                 case 159:
                     ergHVParam.paramPortal.konfRaumId[1][ergebnisArray[i].ident-157+3]=liefereErgString(i);
                     break;

                 case 160:
                 case 161:
                 case 162:
                     ergHVParam.paramPortal.konfRaumBId[0][ergebnisArray[i].ident-160+3]=liefereErgString(i);
                     break;
                 case 163:
                 case 164:
                 case 165:
                     ergHVParam.paramPortal.konfRaumBId[1][ergebnisArray[i].ident-163+3]=liefereErgString(i);
                     break;
                 case 172:
                 case 173:
                 case 174:
                 case 175:
                 case 176:
                 case 177:
                 case 178:
                 case 179:
                 case 180:
                 case 181:
                     String hString1=liefereErgString(i);
                     if (hString1.length()>=10) {
                         int hOffset=ergebnisArray[i].ident-172;
                         String zString=hString1.substring(0,3);
                         zString=CaString.entferneBlankRechts(zString);
                         ergHVParam.paramAkkreditierung.label_xPos[hOffset]=Integer.parseInt(zString);
                         
                         zString=hString1.substring(3,6);
                         zString=CaString.entferneBlankRechts(zString);
                         ergHVParam.paramAkkreditierung.label_yPos[hOffset]=Integer.parseInt(zString);
                         
                         zString=hString1.substring(6,8);
                         zString=CaString.entferneBlankRechts(zString);
                         ergHVParam.paramAkkreditierung.label_fontSize[hOffset]=Integer.parseInt(zString);
                         
                         zString=hString1.substring(8,10);
                         zString=CaString.entferneBlankRechts(zString);
                         ergHVParam.paramAkkreditierung.label_zeilenBedingung[hOffset]=Integer.parseInt(zString);
                         
                         if (hString1.length()>=11) {
                             zString=hString1.substring(10);
                             ergHVParam.paramAkkreditierung.labelString[hOffset]=CaString.entferneBlankRechts(zString);
                         }
                     }
                     break;
                 case 182:
                     ergHVParam.paramAkkreditierung.label_QRCode=liefereErgString(i);
                     break;
                
                     
                     /*INFO hier fortsetzen*/

                }

                /****************91001-92000 : EclPortalUnterlagen DE (analog auch in kurz)******************************************/
                /*Nur noch temporär drin - siehe Beschreibung bei Kurz!*/
                if (ergebnisArray[i].ident >= 91001 && ergebnisArray[i].ident <= 92000) {

                    int offset = ergebnisArray[i].ident - 91001;
                    if (offset < ergHVParam.paramPortal.eclPortalUnterlagen.size()) {
                        ergHVParam.paramPortal.eclPortalUnterlagen.get(offset).bezeichnungDE = liefereErgString(i);
                    }
                }

                /****************92001-93000 : EclPortalUnterlagen EN (analog auch in kurz)******************************************/
                /*Nur noch temporär drin - siehe Beschreibung bei Kurz!*/
                if (ergebnisArray[i].ident >= 92001 && ergebnisArray[i].ident <= 93000) {

                    int offset = ergebnisArray[i].ident - 92001;
                    if (offset < ergHVParam.paramPortal.eclPortalUnterlagen.size()) {
                        ergHVParam.paramPortal.eclPortalUnterlagen.get(offset).bezeichnungEN = liefereErgString(i);
                    }
                }

                i++;

            }
            lErgebnis.close();
            lPStm.close();

        } catch (Exception e) {
            CaBug.drucke("004");
            System.err.println(" " + e.getMessage());
            e.printStackTrace();
            return (-1);
        }

        erg = readLocal_all();
        if (erg < 0) {
            return erg;
        }
        erg = readNummernformen_all();
        if (erg < 0) {
            return erg;
        }
        erg=dbBundle.dbPortalUnterlagen.readAll();
        if (erg>0) {
            /*Dann bereits neue vorhanden - aus Parameter-alt überschreiben. Wenn dort rausgenommen wird,
             * dann hier immer setzen!
             */
            ergHVParam.paramPortal.eclPortalUnterlagen=dbBundle.dbPortalUnterlagen.ergebnis();
        }
        else {
            ergHVParam.paramPortal.eclPortalUnterlagen=new LinkedList<EclPortalUnterlagen>();
        }
        if (ergHVParam.paramPortal.eclPortalUnterlagen==null) {
            ergHVParam.paramPortal.eclPortalUnterlagen=new LinkedList<EclPortalUnterlagen>();
        }
        
        /*+++++++++++++++++++Wortmelde-Workflow einlesen (eigentlich Server-übergreifend)+++++++++++++++++++++*/
        /**in wortmeldetischStatusArray werden alle Stati abgelegt*/
        int maxWortmeldetischStatusIdent=0;
        dbBundle.dbWortmeldetischStatus.readAll(ergHVParam.paramPortal.wortmeldetischSetNr);
        for (int i=0;i<dbBundle.dbWortmeldetischStatus.anzErgebnis();i++) {
            if (dbBundle.dbWortmeldetischStatus.ergebnisPosition(i).statusIdent>maxWortmeldetischStatusIdent) {
                maxWortmeldetischStatusIdent= dbBundle.dbWortmeldetischStatus.ergebnisPosition(i).statusIdent;
            }
        }
        EclWortmeldetischStatus[] wortmeldetischStatusArray=new EclWortmeldetischStatus[maxWortmeldetischStatusIdent+1];
        for (int i=0;i<=maxWortmeldetischStatusIdent;i++) {
            wortmeldetischStatusArray[i]=null;
        }
        for (int i=0;i<dbBundle.dbWortmeldetischStatus.anzErgebnis();i++) {
            wortmeldetischStatusArray[dbBundle.dbWortmeldetischStatus.ergebnisPosition(i).statusIdent]=dbBundle.dbWortmeldetischStatus.ergebnisPosition(i);
        }
        for (int i=0;i<=maxWortmeldetischStatusIdent;i++) {
            if (wortmeldetischStatusArray[i]==null) {
                wortmeldetischStatusArray[i]=new EclWortmeldetischStatus();
            }
        }
        ergHVParam.paramPortal.wortmeldetischStatusArray=wortmeldetischStatusArray;
        CaBug.druckeLog("maxWortmeldetischStatusIdent="+maxWortmeldetischStatusIdent, logDrucken, 10);
        
        /**wortmeldetischViewArray*/
        

        int maxWortmeldetischViewIdent=0;
        dbBundle.dbWortmeldetischView.readAll(ergHVParam.paramPortal.wortmeldetischSetNr);
        CaBug.druckeLog("wortmeldetischView dbBundle.dbWortmeldetischView.anzErgebnis()="+dbBundle.dbWortmeldetischView.anzErgebnis(), logDrucken, 10);
        
        for (int i=0;i<dbBundle.dbWortmeldetischView.anzErgebnis();i++) {
            if (dbBundle.dbWortmeldetischView.ergebnisPosition(i).viewIdent>maxWortmeldetischViewIdent) {
                maxWortmeldetischViewIdent= dbBundle.dbWortmeldetischView.ergebnisPosition(i).viewIdent;
            }
        }
        EclWortmeldetischView[] wortmeldetischViewArray=new EclWortmeldetischView[maxWortmeldetischViewIdent+1];
        for (int i=0;i<=maxWortmeldetischViewIdent;i++) {
            wortmeldetischViewArray[i]=null;
        }
        for (int i=0;i<dbBundle.dbWortmeldetischView.anzErgebnis();i++) {
            wortmeldetischViewArray[i]= dbBundle.dbWortmeldetischView.ergebnisPosition(i);
        }
       for (int i=0;i<=maxWortmeldetischViewIdent;i++) {
            if (wortmeldetischViewArray[i]==null) {
                wortmeldetischViewArray[i]=new EclWortmeldetischView();
            }
        }
        ergHVParam.paramPortal.wortmeldetischViewArray=wortmeldetischViewArray;

        /**wortmeldetischViewArray.statusArray*/
        for (int i=0;i<=maxWortmeldetischViewIdent;i++) {
            wortmeldetischViewArray[i].statusArray=new EclWortmeldetischViewStatus[maxWortmeldetischStatusIdent+1];
            for (int i1=0;i1<=maxWortmeldetischStatusIdent;i1++) {
                wortmeldetischViewArray[i].statusArray[i1]=new EclWortmeldetischViewStatus();
            }
        }
        dbBundle.dbWortmeldetischViewStatus.readAll(ergHVParam.paramPortal.wortmeldetischSetNr);
        for (int i=0;i<dbBundle.dbWortmeldetischViewStatus.anzErgebnis();i++) {
            EclWortmeldetischViewStatus lWortmeldetischViewStatus=dbBundle.dbWortmeldetischViewStatus.ergebnisPosition(i);
            int gefViewIdent=-1;
            for (int i1=0;i1<=maxWortmeldetischViewIdent;i1++) {
                if (lWortmeldetischViewStatus.viewBezeichnung.compareToIgnoreCase(wortmeldetischViewArray[i1].viewBezeichnung)==0) {
                    gefViewIdent=i1;
                }
            }
            if (gefViewIdent==-1) {
                CaBug.drucke("lWortmeldetischViewStatus.viewBezeichnung ="+lWortmeldetischViewStatus.viewBezeichnung+" nicht gefunden");
            }
            int gefStatusIdent=-1;
            for (int i1=0;i1<=maxWortmeldetischStatusIdent;i1++) {
                CaBug.druckeLog("i1="+i1+" lWortmeldetischViewStatus.statusBezeichnung="+lWortmeldetischViewStatus.statusBezeichnung+" wortmeldetischStatusArray[i1].statusBezeichnung="+wortmeldetischStatusArray[i1].statusBezeichnung, logDrucken, 10);
                if (lWortmeldetischViewStatus.statusBezeichnung.compareToIgnoreCase(wortmeldetischStatusArray[i1].statusBezeichnung)==0) {
                    gefStatusIdent=i1;
                }
            }
            if (gefStatusIdent==-1) {
                CaBug.drucke("lWortmeldetischViewStatus.statusBezeichnung ="+lWortmeldetischViewStatus.statusBezeichnung+" nicht gefunden");
            }
            if  (gefViewIdent!=-1 && gefStatusIdent!=-1) {
                lWortmeldetischViewStatus.rederaumErgaenzen=wortmeldetischStatusArray[gefStatusIdent].rederaumErgaenzen;
                lWortmeldetischViewStatus.testraumErgaenzen=wortmeldetischStatusArray[gefStatusIdent].testraumErgaenzen;
                wortmeldetischViewArray[gefViewIdent].statusArray[gefStatusIdent]=lWortmeldetischViewStatus;
            }
        }

        /**wortmeldetischViewArray.statusArray.wortmeldetischFolgeStatusList*/
        dbBundle.dbWortmeldetischFolgeStatus.readAll(ergHVParam.paramPortal.wortmeldetischSetNr);
        for (int i=0;i<dbBundle.dbWortmeldetischFolgeStatus.anzErgebnis();i++) {
            EclWortmeldetischFolgeStatus lWortmeldetischFolgeStatus=dbBundle.dbWortmeldetischFolgeStatus.ergebnisPosition(i);
            CaBug.druckeLog("lWortmeldetischFolgeStatus.viewBezeichnung="+lWortmeldetischFolgeStatus.viewBezeichnung+
                    " lWortmeldetischFolgeStatus.ursprungsStatusBezeichnung="
                    +lWortmeldetischFolgeStatus.ursprungsStatusBezeichnung+
                    " lWortmeldetischFolgeStatus.buttonBezeichnung="+lWortmeldetischFolgeStatus.buttonBezeichnung , logDrucken, 10);
            int gefViewIdent=-1;
            for (int i1=0;i1<=maxWortmeldetischViewIdent;i1++) {
                if (lWortmeldetischFolgeStatus.viewBezeichnung.compareToIgnoreCase(wortmeldetischViewArray[i1].viewBezeichnung)==0) {
                    gefViewIdent=i1;
                }
            }
            if (gefViewIdent==-1) {
                CaBug.drucke("lWortmeldetischFolgeStatus.viewBezeichnung ="+lWortmeldetischFolgeStatus.viewBezeichnung+" nicht gefunden");
            }
            int gefStatusIdent=-1;
            for (int i1=0;i1<=maxWortmeldetischStatusIdent;i1++) {
                if (lWortmeldetischFolgeStatus.ursprungsStatusBezeichnung.compareToIgnoreCase(wortmeldetischStatusArray[i1].statusBezeichnung)==0) {
                    gefStatusIdent=i1;
                }
            }
            if (gefStatusIdent==-1) {
                CaBug.drucke("lWortmeldetischFolgeStatus.ursprungsStatusBezeichnung ="+lWortmeldetischFolgeStatus.ursprungsStatusBezeichnung+" nicht gefunden");
            }
            if  (gefViewIdent!=-1 && gefStatusIdent!=-1) {
                CaBug.druckeLog("Button anhängen wortmeldetischViewArray[gefViewIdent].viewBezeichnung="+wortmeldetischViewArray[gefViewIdent].viewBezeichnung
                        +"Button anhängen wortmeldetischViewArray[gefViewIdent].statusArray[gefStatusIdent].anzeigeTextInDieserView="+wortmeldetischViewArray[gefViewIdent].statusArray[gefStatusIdent].anzeigeTextInDieserView
                        , logDrucken, 10);
                wortmeldetischViewArray[gefViewIdent].statusArray[gefStatusIdent].wortmeldetischFolgeStatusList.add(lWortmeldetischFolgeStatus);
            }
            
        }
 
        /**wortmeldetisch sonstige Arrays*/
        dbBundle.dbWortmeldetischAktion.readAll(ergHVParam.paramPortal.wortmeldetischSetNr);
        ergHVParam.paramPortal.wortmeldetischAktion=dbBundle.dbWortmeldetischAktion.ergebnis();

        dbBundle.dbWortmeldetischStatusWeiterleitung.readAll(ergHVParam.paramPortal.wortmeldetischSetNr);
        ergHVParam.paramPortal.wortmeldetischStatusWeiterleitung=dbBundle.dbWortmeldetischStatusWeiterleitung.ergebnis();
        
        /*++++++++++++++++++++++++++++++++Veranstaltungen++++++++++++++++++++++++++++*/
        List<EclVeranstaltungenVeranstaltung> veranstaltungsListe=new LinkedList<EclVeranstaltungenVeranstaltung>();

        dbBundle.dbVeranstaltungenVeranstaltung.readAll();
        veranstaltungsListe=dbBundle.dbVeranstaltungenVeranstaltung.ergebnis();

        dbBundle.dbVeranstaltungenElement.readAll();
        List<EclVeranstaltungenElement> veranstaltungenElementListe=dbBundle.dbVeranstaltungenElement.ergebnis();
        
        CaBug.druckeLog("veranstaltungenElementListe.size()="+veranstaltungenElementListe.size(), logDrucken, 8);
        
        for (int i=0;i<veranstaltungenElementListe.size();i++) {
            EclVeranstaltungenElement lVeranstaltungenElement=veranstaltungenElementListe.get(i);
            if (lVeranstaltungenElement.gehoertZuElement==0) {
                /*Element ist direkt unter der Veranstaltung*/
                for (int i1=0;i1<veranstaltungsListe.size();i1++) {
                    if (veranstaltungsListe.get(i1).identVeranstaltung==lVeranstaltungenElement.identVeranstaltung) {
                        veranstaltungsListe.get(i1).veranstaltungenElementListe.add(lVeranstaltungenElement);
                        CaBug.druckeLog("Add zu Veranstaltung", logDrucken, 8);
                    }
                }
            }
            else {
                /*Element ist einem anderen Element untergeordnet*/
                for (int i1=0;i1<veranstaltungenElementListe.size();i1++) {
                    if (veranstaltungenElementListe.get(i1).identElement==lVeranstaltungenElement.gehoertZuElement) {
                        veranstaltungenElementListe.get(i1).veranstaltungenElementListe.add(lVeranstaltungenElement);
                        CaBug.druckeLog("Add "+i+" zu Element "+i1, logDrucken, 8);
                    }
                }
            }
            
        }
        
        dbBundle.dbVeranstaltungenElementDetail.readAll();
        List<EclVeranstaltungenElementDetail> veranstaltungenElementDetailListe=dbBundle.dbVeranstaltungenElementDetail.ergebnis();
        CaBug.druckeLog("veranstaltungenElementDetailListe.size()="+veranstaltungenElementDetailListe.size(), logDrucken, 8);
        for (int i=0;i<veranstaltungenElementDetailListe.size();i++) {
            CaBug.druckeLog("i="+i, logDrucken, 8);
            EclVeranstaltungenElementDetail lVeranstaltungenElementDetail=veranstaltungenElementDetailListe.get(i);
            for (int i1=0;i1<veranstaltungenElementListe.size();i1++) {
                CaBug.druckeLog("i1="+i1, logDrucken, 8);
                if (veranstaltungenElementListe.get(i1).identElement==lVeranstaltungenElementDetail.gehoertZuElement) {
                    CaBug.druckeLog("add veranstaltungenElementListe.get(i1).identElement="+veranstaltungenElementListe.get(i1).identElement
                            +" lVeranstaltungenElementDetail.identDetail="+lVeranstaltungenElementDetail.identDetail, logDrucken, 8);
                    veranstaltungenElementListe.get(i1).veranstaltungenElementDetailListe.add(lVeranstaltungenElementDetail);
                }
            }
        
        }

        dbBundle.dbVeranstaltungenAktion.readAll();
        List<EclVeranstaltungenAktion> veranstaltungenAktionListe=dbBundle.dbVeranstaltungenAktion.ergebnis();
        CaBug.druckeLog("veranstaltungenAktionListe.size()="+veranstaltungenAktionListe.size(), logDrucken, 8);
        for (int i=0;i<veranstaltungenAktionListe.size();i++) {
            CaBug.druckeLog("i="+i, logDrucken, 8);
            EclVeranstaltungenAktion lVeranstaltungenAktion=veranstaltungenAktionListe.get(i);
            
            for (int i1=0;i1<veranstaltungenElementListe.size();i1++) {
                CaBug.druckeLog("i1="+i1, logDrucken, 8);
                if (veranstaltungenElementListe.get(i1).identElement==lVeranstaltungenAktion.gehoertZuElement) {
                    CaBug.druckeLog("add veranstaltungenElementListe.get(i1).identElement="+veranstaltungenElementListe.get(i1).identElement
                            +" lVeranstaltungenAktion.identAktion="+lVeranstaltungenAktion.identAktion, logDrucken, 8);
                    veranstaltungenElementListe.get(i1).veranstaltungenAktionListe.add(lVeranstaltungenAktion);
                }
            }

            for (int i1=0;i1<veranstaltungenElementDetailListe.size();i1++) {
                CaBug.druckeLog("i1="+i1, logDrucken, 8);
                if (veranstaltungenElementDetailListe.get(i1).identDetail==lVeranstaltungenAktion.gehoertZuElementDetail) {
                    CaBug.druckeLog("add veranstaltungenElementDetailListe.get(i1).identDetail="+veranstaltungenElementDetailListe.get(i1).identDetail
                            +" lVeranstaltungenAktion.identAktion="+lVeranstaltungenAktion.identAktion, logDrucken, 8);
                    veranstaltungenElementDetailListe.get(i1).veranstaltungenAktionListe.add(lVeranstaltungenAktion);
                }
            }

        }

        ergHVParam.paramPortal.veranstaltungsListe=veranstaltungsListe;
        
        dbBundle.dbVeranstaltungenQuittungElement.readAll();
        ergHVParam.paramPortal.veranstaltungenQuittungListe=dbBundle.dbVeranstaltungenQuittungElement.ergebnisArray;
        
        /*++++++++++++++++++++Eindeutige Kennungen++++++++++++++++++++++++++++++*/
        dbBundle.dbEindeutigeKennung.readIdent(-1);
        if (dbBundle.dbEindeutigeKennung.anzErgebnis()==0) {
             ergHVParam.paramPortal.anzahlEindeutigeKennungenVorhanden=0;
        }
        else {
            ergHVParam.paramPortal.anzahlEindeutigeKennungenVorhanden=dbBundle.dbEindeutigeKennung.ergebnisPosition(0).lfd;
        }
        ergHVParam.paramPortal.anzahlEindeutigeKennungenVorhandenVorAenderung=ergHVParam.paramPortal.anzahlEindeutigeKennungenVorhanden;

        dbBundle.dbEindeutigeKennung.readIdent(0);
        if (dbBundle.dbEindeutigeKennung.anzErgebnis()==0) {
             ergHVParam.paramPortal.anzahlEindeutigeKennungenVerbraucht=0;
        }
        else {
            ergHVParam.paramPortal.anzahlEindeutigeKennungenVerbraucht=dbBundle.dbEindeutigeKennung.ergebnisPosition(0).lfd;
        }

        return 1;
    }

    /**Füllen des "Local-Teils" in ergHVParam.
     * ergHVParam muß vorher initialisiert sein
     * 
     * Entspricht in etwas readLocal_all_alt, nur nicht in dbBundle sondern in ergHVParam.
     */
    public int readLocal_all() {
        int anzInArray = 0;
        PreparedStatement lPStm = null;
        try {
            String lSql = "SELECT * from " + dbBundle.getSchemaMandant() + "tbl_ParameterLocal where "
                    + "mandant=? OR mandant=0 " + "ORDER BY ident, mandant;";
            lPStm = verbindung.prepareStatement(lSql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            lPStm.setInt(1, dbBundle.clGlobalVar.mandant);

            ResultSet lErgebnis = executeQuery(lPStm);
            lErgebnis.last();
            anzInArray = lErgebnis.getRow();
            lErgebnis.beforeFirst();

            ergebnisArray = new EclParameter[anzInArray];

            int i = 0;
            while (lErgebnis.next() == true) {

                ergebnisArray[i] = this.decodeErgebnis(lErgebnis);

                switch (ergebnisArray[i].ident) {
                case 12:
                    break;//Gelöscht: pLocalFormularLaufwerk{
                case 17:
                    break;//Gelöscht:pLocalProduktionsbetrieb
                case 19:
                    ergHVParam.paramBasis.pLocalVerzoegerungOpen = liefereErgInt(i);
                    break;

                /******Portal***************************/
                case 182:
                    ergHVParam.paramPortalServer.passwortVergessenMailAnAdresse = ergebnisArray[i].wert;
                    break;
                case 183:
                    ergHVParam.paramPortalServer.passwortVergessenMailAnAdresse += ergebnisArray[i].wert;
                    break;

                /******************Gästemodul*************************************************************************/
                case 301:
                    ergHVParam.paramGaesteModul.bccAdresse1 = ergebnisArray[i].wert;
                    break;
                case 302:
                    ergHVParam.paramGaesteModul.bccAdresse1 += ergebnisArray[i].wert;
                    break;
                case 303:
                    ergHVParam.paramGaesteModul.bccAdresse2 = ergebnisArray[i].wert;
                    break;
                case 304:
                    ergHVParam.paramGaesteModul.bccAdresse2 += ergebnisArray[i].wert;
                    break;
                }
                i++;
            }

            lErgebnis.close();
            lPStm.close();

        } catch (Exception e) {
            CaBug.drucke("DbParameter.read 004");
            System.err.println(" " + e.getMessage());
            return (-1);
        }
        return 1;
    }

    /**Einlesen der Nummernformen in ergHVParam.
     * ergHVParam muß vorher initialisiert sein.
     * 
     * Wie readNummernformen_all_alt, nur in ergHVParam statt in dbBundle
     */
    public int readNummernformen_all() {
        ParamNummernformen paramNummernformen = ergHVParam.paramNummernformen;/*Nur zur Tipp-Erleichterung*/
        ParamNummernkreise paramNummernkreise = ergHVParam.paramNummernkreise;/*Nur zur Tipp-Erleichterung*/
        /**NummernformenSet einlesen*/
        EclNummernFormSet lNummernFormSet = new EclNummernFormSet();
        lNummernFormSet.ident = ergHVParam.paramNummernformen.ident;
        if (lNummernFormSet.ident != 0) {
            dbBundle.dbNummernFormSet.read(lNummernFormSet);
            lNummernFormSet = dbBundle.dbNummernFormSet.ergebnisPosition(0);

            paramNummernformen.klasseZuCode = lNummernFormSet.klasseZuCode;
            paramNummernformen.artZuCode = lNummernFormSet.artZuCode;
            paramNummernformen.kombiZuCode = lNummernFormSet.kombiZuCode;
            paramNummernformen.stimmartZuCode[KonstStimmart.ja] = lNummernFormSet.kodierungJa;
            paramNummernformen.stimmartZuCode[KonstStimmart.nein] = lNummernFormSet.kodierungNein;
            paramNummernformen.stimmartZuCode[KonstStimmart.enthaltung] = lNummernFormSet.kodierungEnthaltung;
            paramNummernformen.nummernformZuKlasseArt = lNummernFormSet.nummernformZuKlasseArt;

            ergHVParam.paramPruefzahlen.berechnungsVerfahrenPruefziffer=lNummernFormSet.berechnungsVerfahrenPruefziffer;
            /*Nummernkreise*/
            paramNummernkreise.laengeKartennummer = lNummernFormSet.laengeKartennummer;
            {
                int l = 1;
                for (int i = 1; i <= 7; i++) {
                    if (paramNummernkreise.laengeKartennummer[i] != 0 && l < paramNummernkreise.laengeKartennummer[i]) {
                        l = paramNummernkreise.laengeKartennummer[i];
                    }
                }
                paramNummernkreise.laengeOhnePruefziffern = l;
            }

            paramNummernkreise.istNumerisch = lNummernFormSet.istNumerisch;
            paramNummernkreise.einNummernkreisIstAlpha = false;
            /*TODO _Nummernformen: Nummernkreis ist Alpha wird nicht unterstützt*/
            //			for (int i=1;i<=7;i++){
            //				if (paramNummernkreise.istNumerisch[i]==false){
            //					paramNummernkreise.einNummernkreisIstAlpha=true;
            //				}
            //			}
            paramNummernkreise.vonKartennummerGesamt = lNummernFormSet.vonKartennummerGesamt;
            paramNummernkreise.bisKartennummerGesamt = lNummernFormSet.bisKartennummerGesamt;
            paramNummernkreise.vonKartennummerAuto = lNummernFormSet.vonKartennummerAuto;
            paramNummernkreise.bisKartennummerAuto = lNummernFormSet.bisKartennummerAuto;
            paramNummernkreise.vonKartennummerManuell = lNummernFormSet.vonKartennummerManuell;
            paramNummernkreise.bisKartennummerManuell = lNummernFormSet.bisKartennummerManuell;

            paramNummernkreise.vonSammelkartennummer = lNummernFormSet.vonSammelkartennummer;
            paramNummernkreise.bisSammelkartennummer = lNummernFormSet.bisSammelkartennummer;

            paramNummernkreise.vonSubEintrittskartennummer = lNummernFormSet.vonSubEintrittskartennummer;
            paramNummernkreise.bisSubEintrittskartennummer = lNummernFormSet.bisSubEintrittskartennummer;

            paramNummernkreise.vonSubStimmkartennummer = lNummernFormSet.vonSubStimmkartennummer;
            paramNummernkreise.bisSubStimmkartennummer = lNummernFormSet.bisSubStimmkartennummer;

            /*Nummern-Definitionen
             * 
             * Hinweis: der Einfachheithalber wird eine Liste aufgebaut, bei der an n.ter Stelle
             * die Nummernform mit der ident n steht.
             * Optimierung: nur die Nummernformen einlesen, die gebraucht werden, und dann
             * ident in nummernformZuKlasseArt korrigieren*/
            paramNummernformen.nummernDefinition = new ArrayList<>();
            paramNummernformen.nummernDefinition.add(new ArrayList<Character>());/*Element 0 anfügen - nicht verwendet*/

            dbBundle.dbNummernForm.read_all();
            int anz = 1;
            for (int i = 0; i < dbBundle.dbNummernForm.anzErgebnis(); i++) {
                int offset = dbBundle.dbNummernForm.ergebnisArray[i].ident;
                while (anz < offset) {
                    paramNummernformen.nummernDefinition
                            .add(new ArrayList<Character>());/*Lückenfüller anfügen - nicht verwendet*/
                    anz++;
                }
                paramNummernformen.nummernDefinition.add(new ArrayList<Character>());
                String nummernKodierung = dbBundle.dbNummernForm.ergebnisArray[i].kodierung;
                for (int i1 = 0; i1 < nummernKodierung.length(); i1++) {
                    char hString = nummernKodierung.charAt(i1);
                    //				System.out.println("Nummernform Char="+hString);
                    paramNummernformen.nummernDefinition.get(offset).add(hString);
                }
                anz++;
            }
        }

        return 1;
    }

    /**Liest alle zur Verfügung stehende Parameteren für diesen Mandanten, aufsteigend sortiert nach position
     * und legt diesen in ergParamServer ab.
     * 
     * Return-Wert:
     * >=1 => Anzahl der gefundenen Datensätze
     * =0 => nix gefunden
     * <0 => Fehler
     * */
    public int readServer_all() {
        ergParamServer = new ParamServer();
        int anzInArray = 0;
        PreparedStatement lPStm = null;
        try {
            String lSql = "SELECT * from " + dbBundle.getSchemaAllgemein() + "tbl_parameterServer where " + "mandant=0 "
                    + "ORDER BY ident;";
            lPStm = verbindung.prepareStatement(lSql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);

            ResultSet lErgebnis = executeQuery(lPStm);
            lErgebnis.last();
            anzInArray = lErgebnis.getRow();
            lErgebnis.beforeFirst();

            ergebnisArray = new EclParameter[anzInArray];

            int i = 0;
            while (lErgebnis.next() == true) {

                ergebnisArray[i] = this.decodeErgebnis(lErgebnis);

                switch (ergebnisArray[i].ident) {
                case 1:
                    ergParamServer.serverBezeichnung = liefereErgString(i);
                    break;
                case 2:
                    ergParamServer.serverArt = liefereErgInt(i);
                    break;

                case 51:
                    ergParamServer.geraeteSetIdent = liefereErgInt(i);
                    break;

                case 101:
                    ergParamServer.pLocalPraefixLink = liefereErgString(i);
                    ergParamServer.pLocalPraefixLinkExtern=ergParamServer.pLocalPraefixLink;
                    break;
                case 102:
                    ergParamServer.pLocalPraefixLinkAlternativ = liefereErgString(i);
                    break;
                case 103:
                    ergParamServer.pSubdomainPraefixLink = liefereErgString(i);
                    break;
                case 104:
                    ergParamServer.pSubdomainSuffixLink = liefereErgString(i);
                    break;
                case 105:
                    ergParamServer.pLocalInternPasswort = liefereErgInt(i);
                    break;
                case 106:
                    ergParamServer.passwortAgingEin = liefereErgInt(i);
                    break;
                case 107:
                    ergParamServer.pLocalInternPasswortMeeting = liefereErgInt(i);
                    break;
                case 108:
                    ergParamServer.dbServerIdent = liefereErgInt(i);
                    break;
                case 109:
                    ergParamServer.loginGesperrt = liefereErgInt(i);
                    break;
                case 110:
                    ergParamServer.loginGesperrtTextDeutsch = liefereErgString(i);
                    break;
                case 111:
                    ergParamServer.loginGesperrtTextEnglisch = liefereErgString(i);
                    break;
                case 112:
                    ergParamServer.verbundServerAktiv[0] = (liefereErgInt(i)==1);
                    break;
                case 113:
                    ergParamServer.verbundServerAktiv[1] = (liefereErgInt(i)==1);
                    break;
                case 114:
                    ergParamServer.verbundServerAktiv[2] = (liefereErgInt(i)==1);
                    break;
                case 115:
                    ergParamServer.verbundServerAktiv[3] = (liefereErgInt(i)==1);
                    break;
                case 116:
                    ergParamServer.verbundServerAktiv[4] = (liefereErgInt(i)==1);
                    break;
                case 117:
                    ergParamServer.verbundServerAdresse[0] = liefereErgString(i);
                    break;
                case 118:
                    ergParamServer.verbundServerAdresse[1] = liefereErgString(i);
                    break;
                case 119:
                    ergParamServer.verbundServerAdresse[2] = liefereErgString(i);
                    break;
                case 120:
                    ergParamServer.verbundServerAdresse[3] = liefereErgString(i);
                    break;
                case 121:
                    ergParamServer.verbundServerAdresse[4] = liefereErgString(i);
                    break;
                case 122:
                    ergParamServer.webSocketsLocalHost = liefereErgString(i);
                    break;
                case 123:
                    ergParamServer.standardPortaltextePflegbar = liefereErgInt(i);
                    break;
                case 124:
                    ergParamServer.nummernformenPflegbar = liefereErgInt(i);
                    break;
                case 125:
                    ergParamServer.instisPflegbar = liefereErgInt(i);
                    break;
                case 126:
                    ergParamServer.praefixPfadVerzeichnisse = liefereErgString(i);
                    break;
                case 127:
                    ergParamServer.timeoutSperrlogik = liefereErgLong(i);
                    break;
                case 128:
                    ergParamServer.domainLinkErsetzen = liefereErgString(i);
                    break;
                case 129:
                    ergParamServer.domainLinkErsetzenDurch = liefereErgString(i);
                    break;
                case 130:
                    ergParamServer.portalInitialPasswortTestBestand = liefereErgString(i);
                    break;
              }
                
                /*201 bis 300: autoDrucker*/
                if (ergebnisArray[i].ident>=201 && ergebnisArray[i].ident<=300) {
                    ergParamServer.autoDrucker[ergebnisArray[i].ident-200]=liefereErgString(i);
                }

                i++;

            }
            
            dbBundle.dbServiceDeskSet.readAll();
            ergParamServer.serviceDeskSetList=dbBundle.dbServiceDeskSet.ergebnis();

            dbBundle.dbServiceDeskWorkflow.readAll();
            ergParamServer.serviceDeskWorkflowList=dbBundle.dbServiceDeskWorkflow.ergebnis();

            ParamServerStatic.initialisiere(ergParamServer);
            lErgebnis.close();
            lPStm.close();

        } catch (Exception e) {
            CaBug.drucke("DbParameter.readServer_all 003");
            System.err.println(" " + e.getMessage());
            return (-1);
        }

        return (anzInArray);
    }

    /*****************readGerate_all und Sub-Routinen**************************/
    private boolean liefereErgGeraetBoolean(int pI) {
        boolean hBoolean = true;
        int hInt = Integer.parseInt(ergebnisArrayGeraet[pI].wert);
        if (hInt == 1) {
            hBoolean = true;
        } else {
            hBoolean = false;
        }
        return hBoolean;
    }

    private int liefereErgGeraetInt(int pI) {
        int hWert = 0;
        hWert = Integer.parseInt(ergebnisArrayGeraet[pI].wert);
        return hWert;
    }

    private String liefereErgGeraetString(int pI) {
        String hWert = "";
        hWert = ergebnisArrayGeraet[pI].wert.trim();
        return hWert;
    }

    /**Liest alle zur Verfügung stehende Parameteren für das Geräteset dieses Mandanten
    	 * für den Arbeitsplatz pArbeitsplatz ein
    	 * 
    	 *
    	 * Return-Wert:
    	 * >=1 => Anzahl der gefundenen Datensätze
    	 * =0 => nix gefunden
    	 * <0 => Fehler
    	 * */
    public int readGeraete_all(int pArbeitsplatzNummer) {
        int erg;

        CaBug.druckeLog("DbParameter pArbeitsplatzNummer=" + pArbeitsplatzNummer, logDrucken, 3);
        CaBug.druckeLog("DbParameter geraeteSetIdent=" + dbBundle.paramServer.geraeteSetIdent, logDrucken, 10);
        /*Geräteklasse bestimmen*/
        int lGeraeteklasse = 0;
        int zuruecksetzen = 0;
        erg = dbBundle.dbGeraeteKlasseSetZuordnung.read(dbBundle.paramServer.geraeteSetIdent, pArbeitsplatzNummer);
        if (erg >= 1) {
            zuruecksetzen = dbBundle.dbGeraeteKlasseSetZuordnung.ergebnisArray[0].lokaleDatenZuruecksetzenBeimNaechstenStart;
            lGeraeteklasse = dbBundle.dbGeraeteKlasseSetZuordnung.ergebnisArray[0].geraeteKlasseIdent;
        }

        int rc = readGerateKlasse_all(lGeraeteklasse);

        if (zuruecksetzen == 1) {
            ergParamGeraet.lokalZuruecksetzen = true;
        } else {
            ergParamGeraet.lokalZuruecksetzen = false;
        }

        ergParamGeraet.serverArt = dbBundle.paramServer.serverArt;
        ergParamGeraet.serverBezeichnung = dbBundle.paramServer.serverBezeichnung;

        if (ergParamGeraet.festgelegterMandant > 0) {
            dbBundle.dbEmittenten.readMandantenbezeichnung(ergParamGeraet.festgelegterMandant);
            if (dbBundle.dbEmittenten.anzErgebnis() > 0) {
                ergParamGeraet.festgelegterMandantText = dbBundle.dbEmittenten.ergebnisPosition(0).bezeichnungKurz;
            }
        }
        /*Für vorausgewählte Mandantennummer noch Bezeichnung einsetzen*/
        dbBundle.paramGeraet = ergParamGeraet;

        return rc;
    }

    /**Liest alle zur Verfügung stehende Parameteren für die Geräteklasse pGeraeteKlasse ein.
     * Ergebnis wird in ergParamGeraet abgelegt!
     *
     * Return-Wert:
     * >=1 => Anzahl der gefundenen Datensätze
     * =0 => nix gefunden
     * <0 => Fehler
     * */
    public int readGerateKlasse_all(int pGeraeteKlasse) {
        PreparedStatement lPStm = null;
        int anzInArray = 0;

        /*Nun alle Parameter für diese Geräteklasse einlesen*/
        ergParamGeraet = new ParamGeraet();

        ergParamGeraet.identKlasse = pGeraeteKlasse;

        dbBundle.dbGeraeteKlasse.read(pGeraeteKlasse);
        if (dbBundle.dbGeraeteKlasse.anzErgebnis() > 0) {
            ergParamGeraet.beschreibungKlasse = dbBundle.dbGeraeteKlasse.ergebnisPosition(0).kurzBeschreibung;
        } else {
            return -1;
        }

        try {
            String lSql = "SELECT * from " + dbBundle.getSchemaAllgemein() + "tbl_geraeteParameter where " + "klasse=? "
                    + "ORDER BY ident, klasse;";
            lPStm = verbindung.prepareStatement(lSql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            lPStm.setInt(1, pGeraeteKlasse);

            ResultSet lErgebnis = executeQuery(lPStm);
            lErgebnis.last();
            anzInArray = lErgebnis.getRow();
            lErgebnis.beforeFirst();

            ergebnisArrayGeraet = new EclGeraeteParameter[anzInArray];

            int i = 0;
            while (lErgebnis.next() == true) {

                ergebnisArrayGeraet[i] = this.decodeErgebnisGeraet(lErgebnis);
                switch (ergebnisArrayGeraet[i].ident) {

                case 1: {
                    ergParamGeraet.akkreditierungVertreterErfassungAktiv = liefereErgGeraetBoolean(i);
                    break;
                }
                case 2: {
                    ergParamGeraet.akkreditierungScanFeldFuerBestaetigungAktiv = liefereErgGeraetBoolean(i);
                    break;
                }
                //					case 3:{ergParamGeraet.akkreditierungAbstimmungMoeglich=liefereErgGeraetBoolean(i);break;} /*Nicht mehr verwendet*/
                case 4: {
                    ergParamGeraet.akkreditierungAnzeigeBelegeBuendeln = liefereErgGeraetBoolean(i);
                    break;
                }
                case 5: {
                    ergParamGeraet.akkreditierungSammelkartenBuchenMoeglich = liefereErgGeraetBoolean(i);
                    break;
                }
                case 6: {
                    ergParamGeraet.akkreditierungLabeldruckFuerAktionaer = liefereErgGeraetBoolean(i);
                    break;
                }
                case 7: {
                    ergParamGeraet.nurGueltigeStimmkartenNummerBeiTabletAbstimmung = liefereErgGeraetBoolean(i);
                    break;
                }
                case 8: {
                    ergParamGeraet.protokollAnzMaxZugaenge = liefereErgGeraetInt(i);
                    break;
                }
                case 9: {
                    ergParamGeraet.protokollAnzMax = liefereErgGeraetInt(i);
                    break;
                }
                case 10: {
                    ergParamGeraet.akkreditierungDelayIgnorieren = liefereErgGeraetBoolean(i);
                    break;
                }
                case 11: {
                    ergParamGeraet.abstimmungTabletHochformat = liefereErgGeraetBoolean(i);
                    break;
                }
                case 12: {
                    ergParamGeraet.abstimmungTabletTyp = liefereErgGeraetInt(i);
                    break;
                }
                case 13: {
                    ergParamGeraet.akkreditierungShortcutsAktiv = liefereErgGeraetBoolean(i);
                    break;
                }
                case 14: {
                    ergParamGeraet.vertreterKorrekturBeiKontrollerfassungMoeglich = liefereErgGeraetBoolean(i);
                    break;
                }

                case 15: {
                    ergParamGeraet.festgelegterMandant = liefereErgGeraetInt(i);
                    break;
                }
                case 16: {
                    ergParamGeraet.festgelegtesJahr = liefereErgGeraetInt(i);
                    break;
                }
                case 17: {
                    ergParamGeraet.festgelegteHVNummer = liefereErgGeraetString(i);
                    break;
                }
                case 18: {
                    ergParamGeraet.festgelegteDatenbank = liefereErgGeraetString(i);
                    break;
                }
                case 19: {
                    ergParamGeraet.festgelegterBenutzername = liefereErgGeraetString(i);
                    break;
                }

                case 20: {
                    ergParamGeraet.lwPfadAllgemein = liefereErgGeraetString(i);
                    break;
                }
                case 21: {
                    ergParamGeraet.lwPfadSicherung1 = liefereErgGeraetString(i);
                    break;
                }
                case 22: {
                    ergParamGeraet.lwPfadSicherung2 = liefereErgGeraetString(i);
                    break;
                }
                case 23: {
                    ergParamGeraet.lwPfadExportFuerPraesentation = liefereErgGeraetString(i);
                    break;
                }
                case 24: {
                    ergParamGeraet.lwPfadExportFuerBuehnensystem = liefereErgGeraetString(i);
                    break;
                }
                case 25: {
                    ergParamGeraet.lwPfadExportExcelFuerPowerpoint = liefereErgGeraetString(i);
                    break;
                }

                case 26: {
                    ergParamGeraet.bonDruckerIstZugeordnet = liefereErgGeraetBoolean(i);
                    break;
                }
                case 27: {
                    ergParamGeraet.labelDruckerIPAdresse = liefereErgGeraetString(i);
                    break;
                }

                case 28: {
                    ergParamGeraet.moduleHVMaster = liefereErgGeraetBoolean(i);
                    break;
                }
                case 29: {
                    ergParamGeraet.moduleFrontOffice = liefereErgGeraetBoolean(i);
                    break;
                }
                case 30: {
                    ergParamGeraet.moduleKontrolle = liefereErgGeraetBoolean(i);
                    break;
                }
                case 31: {
                    ergParamGeraet.moduleServiceDesk = liefereErgGeraetBoolean(i);
                    break;
                }
                case 32: {
                    ergParamGeraet.moduleTeilnahmeverzeichnis = liefereErgGeraetBoolean(i);
                    break;
                }

                case 33: {
                    ergParamGeraet.moduleTabletAbstimmung = liefereErgGeraetBoolean(i);
                    break;
                }
                case 34: {
                    ergParamGeraet.moduleBestandsverwaltung = liefereErgGeraetBoolean(i);
                    break;
                }
                case 35: {
                    ergParamGeraet.moduleHotline = liefereErgGeraetBoolean(i);
                    break;
                }

                case 36: {
                    ergParamGeraet.programmStartKontrollScreenAnzeigen = liefereErgGeraetBoolean(i);
                    break;
                }

                case 37: {
                    ergParamGeraet.moduleAktienregisterImport = liefereErgGeraetBoolean(i);
                    break;
                }
                case 38: {
                    ergParamGeraet.moduleDesigner = liefereErgGeraetBoolean(i);
                    break;
                }

                case 39: {
                    ergParamGeraet.lwPfadKundenordnerBasis = liefereErgGeraetString(i);
                    break;
                }

                case 40: {
                    ergParamGeraet.dbVorrangig = liefereErgGeraetBoolean(i);
                    break;
                }

                case 41: {
                    ergParamGeraet.festgelegtesJahrIstFix = liefereErgGeraetBoolean(i);
                    break;
                }
                case 42: {
                    ergParamGeraet.festgelegteHVNummerFix = liefereErgGeraetBoolean(i);
                    break;
                }
                case 43: {
                    ergParamGeraet.festgelegteDatenbankFix = liefereErgGeraetBoolean(i);
                    break;
                }
                case 44: {
                    ergParamGeraet.festgelegterMandantIstFix = liefereErgGeraetBoolean(i);
                    break;
                }
                case 45: {
                    ergParamGeraet.festgelegteBenutzernameFix = liefereErgGeraetBoolean(i);
                    break;
                }
                case 46: {
                    ergParamGeraet.lwPfadGrossdokumente = liefereErgGeraetString(i);
                    break;
                }
                case 47: {
                    ergParamGeraet.akkreditierungSuchfunktionAktiv = liefereErgGeraetBoolean(i);
                    break;
                }
                case 48: {
                    ergParamGeraet.abstimmungTabletPersoenlichZugeordnet = liefereErgGeraetBoolean(i);
                    break;
                }
                case 49:
                    ergParamGeraet.abstimmungTabletTestmodus = liefereErgGeraetBoolean(i);
                    break;
                case 50:
                    ergParamGeraet.abtimmungTabletFullScreen = liefereErgGeraetBoolean(i);
                    break;
                case 51: 
                    ergParamGeraet.abstimmungTabletXSize = liefereErgGeraetString(i);
                    break;
                case 52: 
                    ergParamGeraet.abstimmungTabletYSize = liefereErgGeraetString(i);
                    break;
 
                }

                i++;
            }

            lErgebnis.close();
            lPStm.close();

        } catch (Exception e) {
            CaBug.drucke("DbParameter.readGerateKlasse_all 003");
            System.err.println(" " + e.getMessage());
            e.printStackTrace();
            return (-1);
        }
        return anzInArray;
    }

    private int delete_ablaufAbstimmung() {
        int erg = 0;

        try {

            String sql1 = "DELETE FROM " + liefereTblParameter() + " where mandant=? and ident>=1000 and ident<=1200;";
            PreparedStatement pstm1 = verbindung.prepareStatement(sql1);
            pstm1.setInt(1, dbBundle.clGlobalVar.mandant);

            erg = 0; /*Falls Duplicate Key, dann wird exception geschmissen und erg wohl nicht gefüllt!*/
            erg = executeUpdate(pstm1);

            pstm1.close();
        } catch (Exception e2) {
            CaBug.drucke("001");
            System.err.println(" " + e2.getMessage());
            return (erg);
        }

        return 1;

    }


    /**pDatenbank:
     * 1 = tbl_parameter (update/insert)
     * 2 = tbl_parameterServer (updateServer/insertServer)
     * 3 = tbl_parameterLocal
     */
    private void setzeErgBoolean(int pIdent, boolean pWert, String pBezeichnung, int pDatenbank) {
        String hWert = "";
        if (pWert) {
            hWert = "1";
        } else {
            hWert = "0";
        }
        setzeErgString(pIdent, hWert, pBezeichnung, pDatenbank);
    }

    private void setzeErgInt(int pIdent, int pWert, String pBezeichnung, int pDatenbank) {
        String hWert = "";
        hWert = Integer.toString(pWert);
        setzeErgString(pIdent, hWert, pBezeichnung, pDatenbank);
    }

    private void setzeErgLong(int pIdent, long pWert, String pBezeichnung, int pDatenbank) {
        String hWert = "";
        hWert = Long.toString(pWert);
        setzeErgString(pIdent, hWert, pBezeichnung, pDatenbank);
    }

    private void setzeErgDouble(int pIdent, double pWert, String pBezeichnung, int pDatenbank) {
        String hWert = "";
        hWert = Double.toString(pWert);
        setzeErgString(pIdent, hWert, pBezeichnung, pDatenbank);
    }

    public void setzeErgString(int pIdent, String pWert, String pBezeichnung, int pDatenbank) {
        int rc = 0;
        EclParameter lParameter = new EclParameter();
        lParameter.ident = pIdent;
        lParameter.wert = pWert;
        if (lParameter.wert.length() > 40 && pDatenbank != 4 && pDatenbank!=2) {
            lParameter.wert = lParameter.wert.substring(0, 40);
        }
        if (lParameter.wert.length() > 200 && (pDatenbank == 4 || pDatenbank==2)) {
            lParameter.wert = lParameter.wert.substring(0, 200);
        }
        lParameter.beschreibung = pBezeichnung;
        switch (pDatenbank) {
        case 1: {
            rc = this.update_ohneReload(lParameter);
            if (rc < 1) {
                this.insert_ohneReload(lParameter);
            }
            break;
        }
        case 2: {
            rc = this.updateServer_ohneReload(lParameter);
            if (rc < 1) {
                this.insertServer_ohneReload(lParameter);
            }
            break;
        }
        case 3: {
            rc = this.updateLocal_ohneReload(lParameter);
            if (rc < 1) {
                this.insertLocal_ohneReload(lParameter);
            }
            break;
        }
        case 4: {
            rc = this.updateLang_ohneReload(lParameter);
            if (rc < 1) {
                this.insertLang_ohneReload(lParameter);
            }
            break;
        }
        }
    }

    /**nummernformen werden NICHT upgedatet! Dies muß über dbNummernform, dbNummernFormSet, dbNummernkreis erfolgen*/
    public int updateHVParam_allForce() {
        return updateHVParam_all(true);
    }
    
    /**nummernformen werden NICHT upgedatet! Dies muß über dbNummernform, dbNummernFormSet, dbNummernkreis erfolgen*/
    public int updateHVParam_all() {
        return updateHVParam_all(false);
    }
    
    /**nummernformen werden NICHT upgedatet! Dies muß über dbNummernform, dbNummernFormSet, dbNummernkreis erfolgen
     * pForce = true => Parameter werden immer upgedated, egal ob von anderem Benutzer verändert
     * */
    private int updateHVParam_all(boolean pForce) {
        /*XXX Achtung - noch abgleichen / ändern: erfolgt updateHVParam_all jetzt aus ergHVParam oder aus dbBundle.param heraus?*/
 
//      @formatter:off

        
        DbReload dbReload=new DbReload(dbBundle);
        EclReload lReload=new EclReload();
        lReload.ident=1; // HV-Parameter
        lReload.mandant=dbBundle.clGlobalVar.mandant;
        lReload.reload=ergHVParam.reloadVersionParameter;
        
        /*Durch einlesen überprüfen, ob schon existent; sonst Insert*/
        int anzReload=dbReload.read(lReload);
        if (anzReload==0) {
            lReload.reload=1;
            dbReload.insert(lReload);
        }
        
        else {
            if (pForce==false) {
                int erg=dbReload.updateErhoheVersionsnummer(lReload);
                CaBug.druckeLog("erg="+erg, logDrucken, 10);
                if (erg==CaFehler.pfXyWurdeVonAnderemBenutzerVeraendert) {
                    return CaFehler.pfXyWurdeVonAnderemBenutzerVeraendert;
                }
            }
        }

        
        String hString = "";

        setzeErgInt(1, ergHVParam.paramPortal.bestaetigenDialog, "pBestaetigenDialog", 1);
        setzeErgInt(2, ergHVParam.paramPortal.quittungDialog, "pQuittungDialog", 1);
        setzeErgInt(3, ergHVParam.paramPortal.passwortCaseSensitiv, "pPasswortCaseSensitiv", 1);
        setzeErgInt(4, ergHVParam.paramPortal.passwortMindestLaenge, "pPasswortMindestLaenge", 1);
        setzeErgInt(5, ergHVParam.paramPortal.pNichtmarkiertSpeichernAls, "pNichtmarkiertSpeichernAls", 1);
        setzeErgInt(8, ergHVParam.paramPortal.bestaetigenHinweisAktionaersportal, "pBestaetigenHinweisAktionaersportal",
                1);
        setzeErgInt(9, ergHVParam.paramPortal.bestaetigenHinweisHVportal, "pBestaetigenHinweisHVportal", 1);
        setzeErgInt(10, ergHVParam.paramPortal.zusaetzlicheEKDritteMoeglich, "pZusaetzlicheEKDritteMoeglich", 1);
        setzeErgInt(11, ergHVParam.paramPortal.ekUndWeisungGleichzeitigMoeglich, "pEKUndWeisungGleichzeitigMoeglich",
                1);
        //Gelöscht pLetzterAktienregisterImport
        setzeErgInt(16, ergHVParam.paramPortal.erstregistrierungImmer, "pErstregistrierungImmer", 1);

        hString = "";
        for (int i1 = 0; i1 < 6; i1++) {
            hString = hString
                    + Integer.toString(ergHVParam.paramAkkreditierung.pPraesenzStornierenAusSammelZwingend[i1]);
        }
        setzeErgString(17, hString, "pPraesenzStornierenAusSammelZwingend[]", 1);

        /*Weitere Parameter zur Stimmkartenzuordnung siehe auch ab 700*/
        hString = "";
        for (int i1 = 0; i1 < 5; i1++) {
            for (int i2 = 0; i2 < 5; i2++) {
                hString = hString
                        + Integer.toString(ergHVParam.paramAkkreditierung.pPraesenzStimmkartenZuordnenGattung[i1][i2]);
            }
        }
        setzeErgString(18, hString, "pPraesenzStimmkartenZuordnenGattung[][]", 1);

        hString = "";
        for (int i1 = 0; i1 < 5; i1++) {
            for (int i2 = 0; i2 < 5; i2++) {
                hString = hString + Integer.toString(ergHVParam.paramAkkreditierung.pPraesenzStimmkartenZuordnenAppGattung[i1][i2]);
            }
        }
        setzeErgString(19, hString, "pPraesenzStimmkartenZuordnenAppGattung[][]", 1);

        setzeErgString(20, "", "leer", 1);
        setzeErgString(21, "", "leer", 1);
        setzeErgString(22, "", "leer", 1);

        hString = "";
        for (int i1 = 0; i1 < 5; i1++) {
            hString = hString
                    + Integer.toString(ergHVParam.paramAkkreditierung.pPraesenzStimmkarteSecondZuordnenGattung[i1]);
        }
        setzeErgString(23, hString, "pPraesenzStimmkarteSecondZuordnenGattung[]", 1);

        setzeErgString(24, "", "leer", 1);
        setzeErgString(25, "", "leer", 1);
        setzeErgString(26, "", "leer", 1);
        setzeErgString(27, "", "leer", 1);
        setzeErgString(28, "", "leer", 1);
        setzeErgString(29, "", "leer", 1);
        setzeErgString(30, "", "leer", 1);
        setzeErgString(31, "", "leer", 1);

        /*32 entfernt, ehemals pPraesenzStimmkartenZuordnenAuchBeiAppIdent*/
//        setzeErgInt(32, ergHVParam.paramAkkreditierung.pPraesenzStimmkartenZuordnenAuchBeiAppIdent,
//                "pPraesenzStimmkartenZuordnenAuchBeiAppIdent", 1);
        setzeErgInt(33, ergHVParam.paramAkkreditierung.pPraesenzBeiErscheinenAndereVollmachtenStornieren,
                "pPraesenzBeiErscheinenAndereVollmachtenStornieren", 1);

        /*Werden nicht mehr verwendet.*/
        //		setzeErgLong(35, ergHVParam.paramBasis.pGrundkapital, "pGrundkapital", 1);
        //		setzeErgDouble(36, ergHVParam.paramBasis.pWertEinerAktie, "pWertEinerAktie", 1);
        //		setzeErgLong(37, ergHVParam.paramBasis.pGrundkapitalVermindert, "pGrundkapitalVermindert", 1);

        setzeErgString(38, "", "leer", 1);
        setzeErgString(39, "", "leer", 1);

        /*************ParamModuleKonfigurierbar*********************************/
        setzeErgBoolean(40, false, "frei - früher aktionaersportal", 1);
        setzeErgBoolean(41, ergHVParam.paramModuleKonfigurierbar.elektronischesTeilnehmerverzeichnis,
                "elektronischesTeilnehmerverzeichnis", 1);
        setzeErgBoolean(42, ergHVParam.paramModuleKonfigurierbar.tabletAbstimmung, "tabletAbstimmung", 1);
        setzeErgBoolean(43, false, "frei - früher hvApp", 1);
        setzeErgBoolean(44, ergHVParam.paramModuleKonfigurierbar.hvAppAbstimmung, "hvAppAbstimmung", 1);
        setzeErgBoolean(45, ergHVParam.paramModuleKonfigurierbar.elektronischerStimmblock, "elektronischerStimmblock",
                1);
        setzeErgBoolean(46, ergHVParam.paramModuleKonfigurierbar.onlineTeilnahme, "onlineTeilnahme", 1);
        setzeErgBoolean(47, ergHVParam.paramModuleKonfigurierbar.elektronischeWeisungserfassungHV,
                "elektronischeWeisungserfassungHV", 1);
        setzeErgBoolean(48, ergHVParam.paramModuleKonfigurierbar.englischeAgenda, "englischeAgenda", 1);
        setzeErgBoolean(49, ergHVParam.paramModuleKonfigurierbar.hvAppHVFunktionen, "hvAppHVFunktionen", 1);
        setzeErgBoolean(50, ergHVParam.paramModuleKonfigurierbar.briefwahl, "briefwahl", 1);
        setzeErgBoolean(51, ergHVParam.paramModuleKonfigurierbar.scannerAbstimmung, "scannerAbstimmung", 1);
        setzeErgBoolean(52, ergHVParam.paramModuleKonfigurierbar.weisungenSchnittstelleExternesSystem,
                "weisungenSchnittstelleExternesSystem", 1);
        setzeErgInt(53, ergHVParam.paramModuleKonfigurierbar.hvForm, "hvForm", 1);
        setzeErgBoolean(54, ergHVParam.paramModuleKonfigurierbar.mehrereGattungen, "mehrereGattungen", 1);

        /********************************ParamBasis*****************************/
        setzeErgBoolean(60, ergHVParam.paramBasis.inhaberaktienAktiv, "inhaberaktienAktiv", 1);
        setzeErgBoolean(61, ergHVParam.paramBasis.namensaktienAktiv, "namensaktienAktiv", 1);

        hString = "";
        for (int i1 = 0; i1 < 5; i1++) {
            if (ergHVParam.paramBasis.gattungAktiv[i1]) {
                hString = hString + "1";
            } else {
                hString = hString + "0";
            }
        }
        setzeErgString(62, hString, "gattungAktiv[]", 1);

        for (int i1 = 0; i1 < 5; i1++) {
            setzeErgString(63 + i1, ergHVParam.paramBasis.gattungBezeichnung[i1],
                    "gattungBezeichnung[" + Integer.toString(i1) + "]", 1);
        }

        for (int i1 = 0; i1 < 5; i1++) {
            setzeErgString(68 + i1, ergHVParam.paramBasis.gattungBezeichnungKurz[i1],
                    "gattungBezeichnungKurz[" + Integer.toString(i1) + "]", 1);
        }

        setzeErgInt(73, ergHVParam.paramBasis.laengeAktionaersnummer, "laengeAktionaersnummer", 1);

        setzeErgInt(74, ergHVParam.paramBasis.zweiterMandantNr, "zweiterMandantNr", 1);
        setzeErgInt(75, ergHVParam.paramBasis.zweiterMandantHVJahr, "zweiterMandantHVJahr", 1);
        setzeErgString(76, ergHVParam.paramBasis.zweiterMandantHVNummer, "zweiterMandantHVNummer", 1);
        setzeErgString(77, ergHVParam.paramBasis.zweiterMandantDatenbereich, "zweiterMandantDatenbereich", 1);
        setzeErgInt(78, ergHVParam.paramBasis.investorenSind, "investorenSind", 1);
        setzeErgInt(79, ergHVParam.paramBasis.ekSeitenzahl, "ekSeitenzahl", 1);

        /********************************paramNummernformen*****************************/
        setzeErgInt(80, ergHVParam.paramNummernformen.ident, "aktives NummernformenSet", 1);
        setzeErgString(81, ergHVParam.paramPruefzahlen.identifikationsNummer, "Identifikationsnummer", 1);
        setzeErgString(82, ergHVParam.paramPruefzahlen.dreistelligeKontrollzahl, "dreistelligeKontrollzahl", 1);
        setzeErgString(83, ergHVParam.paramPruefzahlen.zweistelligeKontrollzahl, "zweistelligeKontrollzahl", 1);
        setzeErgString(84, ergHVParam.paramPruefzahlen.einstelligeKontrollzahl, "einstelligeKontrollzahl", 1);
//        setzeErgInt(85, ergHVParam.paramNummernformen.ignoriereKartenart, "ignoriereKartenart", 1);

        /********************************ParamBasis Fortsetzung*****************************/
        setzeErgInt(86, ergHVParam.paramBasis.ohneNullAktionaersnummer, "ohneNullAktionaersnummer", 1);
        setzeErgString(87, ergHVParam.paramBasis.eindeutigeHVKennung, "eindeutigeHVKennung", 1);
        setzeErgString(88, ergHVParam.paramBasis.isin[0], "isin1", 1);
        setzeErgString(89, ergHVParam.paramBasis.isin[1], "isin2", 1);
        setzeErgString(90, ergHVParam.paramBasis.isin[2], "isin3", 1);
        setzeErgString(91, ergHVParam.paramBasis.isin[3], "isin4", 1);
        setzeErgString(92, ergHVParam.paramBasis.isin[4], "isin5", 1);
        
        for (int i1 = 0; i1 < 5; i1++) {
            setzeErgString(93 + i1, ergHVParam.paramBasis.gattungBezeichnungEN[i1],
                    "gattungBezeichnungEN[" + Integer.toString(i1) + "]", 1);
        }

        setzeErgInt(98, ergHVParam.paramBasis.ekFormularGetrenntJeVersandweg, "ekFormularGetrenntJeVersandweg", 1);
//        setzeErgInt(99, ergHVParam.paramBasis.veranstaltungstyp, "veranstaltungstyp", 1);

        /***ParameterAbstimmungParameter**/
        setzeErgInt(100, ergHVParam.paramAbstimmungParameter.abstimmenDuerfen_nurPraesente_PraesenteUndGewesene,
                "abstimmenDuerfen_nurPraesente_PraesenteUndGewesene", 1);
        setzeErgInt(101, ergHVParam.paramAbstimmungParameter.priorisierung_ElektronischVorPapier_PapierVorElektronisch,
                "priorisierung_ElektronischVorPapier_PapierVorElektronisch", 1);
        setzeErgInt(102,
                ergHVParam.paramAbstimmungParameter.beiMehrfacherStimmabgabe_letzteAbgabeGilt_ungleicheUngueltig,
                "beiMehrfacherStimmabgabe_letzteAbgabeGilt_ungleicheUngueltig", 1);
        setzeErgInt(103, ergHVParam.paramAbstimmungParameter.beiAbstimmungEintrittskartennummerZulaessig,
                "beiAbstimmungEintrittskartennummerZulaessig", 1);
        setzeErgInt(104, ergHVParam.paramAbstimmungParameter.beiTabletAbstimmungNurAufgerufeneStimmZettelZulaessig,
                "beiTabletAbstimmungNurAufgerufeneStimmZettelZulaessig", 1);
        setzeErgInt(105, ergHVParam.paramAbstimmungParameter.in100PZAuchEnthaltungen, "in100PZAuchEnthaltungen", 1);
        setzeErgInt(106, ergHVParam.paramAbstimmungParameter.in100PZAuchUngueltige, "in100PZAuchUngueltige", 1);
        setzeErgInt(107, ergHVParam.paramAbstimmungParameter.in100PZAuchNichtStimmberechtigte,
                "in100PZAuchNichtStimmberechtigte", 1);
        setzeErgInt(108, ergHVParam.paramAbstimmungParameter.in100PZAuchNichtTeilnahme, "in100PZAuchNichtTeilnahme", 1);
        setzeErgInt(109, ergHVParam.paramAbstimmungParameter.abstimmenTabletNichtPraesenteWerdenPraesentGesetzt,
                "abstimmenTabletNichtPraesenteWerdenPraesentGesetzt", 1);

        setzeErgInt(110, ergHVParam.paramAbstimmungParameter.beiTabletAllesJa, "beiTabletAllesJa", 1);
        setzeErgInt(111, ergHVParam.paramAbstimmungParameter.beiTabletAllesNein, "beiTabletAllesNein", 1);
        setzeErgInt(112, ergHVParam.paramAbstimmungParameter.beiTabletAllesEnthaltung, "beiTabletAllesEnthaltung", 1);
        setzeErgInt(113, ergHVParam.paramAbstimmungParameter.beiTabletAbstimmungBlaettern,
                "beiTabletAbstimmungBlaettern", 1);

        setzeErgInt(131, ergHVParam.paramAbstimmungParameter.anzahlJaJeAbstimmungsgruppe[1],
                "anzahlJaJeAbstimmungsgruppe[1]", 1);
        setzeErgInt(132, ergHVParam.paramAbstimmungParameter.anzahlJaJeAbstimmungsgruppe[2],
                "anzahlJaJeAbstimmungsgruppe[2]", 1);
        setzeErgInt(133, ergHVParam.paramAbstimmungParameter.anzahlJaJeAbstimmungsgruppe[3],
                "anzahlJaJeAbstimmungsgruppe[3]", 1);
        setzeErgInt(134, ergHVParam.paramAbstimmungParameter.anzahlJaJeAbstimmungsgruppe[4],
                "anzahlJaJeAbstimmungsgruppe[4]", 1);
        setzeErgInt(135, ergHVParam.paramAbstimmungParameter.anzahlJaJeAbstimmungsgruppe[5],
                "anzahlJaJeAbstimmungsgruppe[5]", 1);
        setzeErgInt(136, ergHVParam.paramAbstimmungParameter.anzahlJaJeAbstimmungsgruppe[6],
                "anzahlJaJeAbstimmungsgruppe[6]", 1);
        setzeErgInt(137, ergHVParam.paramAbstimmungParameter.anzahlJaJeAbstimmungsgruppe[7],
                "anzahlJaJeAbstimmungsgruppe[7]", 1);
        setzeErgInt(138, ergHVParam.paramAbstimmungParameter.anzahlJaJeAbstimmungsgruppe[8],
                "anzahlJaJeAbstimmungsgruppe[8]", 1);
        setzeErgInt(139, ergHVParam.paramAbstimmungParameter.anzahlJaJeAbstimmungsgruppe[9],
                "anzahlJaJeAbstimmungsgruppe[9]", 1);
        setzeErgInt(140, ergHVParam.paramAbstimmungParameter.anzahlJaJeAbstimmungsgruppe[10],
                "anzahlJaJeAbstimmungsgruppe[10]", 1);
        setzeErgInt(141, ergHVParam.paramAbstimmungParameter.beiTabletGesamtmarkierungTextAnzeigen,
                "beiTabletGesamtmarkierungTextAnzeigen", 1);
        setzeErgInt(142, ergHVParam.paramAbstimmungParameter.auswertenPPListeZeilen, "auswertenPPListeZeilen", 1);
        setzeErgInt(143, ergHVParam.paramAbstimmungParameter.beiTabletGesamtmarkierungFuerAlle,
                "beiTabletGesamtmarkierungFuerAlle", 1);
        setzeErgInt(144, ergHVParam.paramAbstimmungParameter.beiTabletTextKurzOderLang, "beiTabletTextKurzOderLang", 1);
        /*Fortsetzung bei 2000*/

        /***ParamPortal******/
        //		setzeErgInt(150, ergHVParam.paramPortal.portalAuchInEnglischVerfuegbar, "portalAuchInEnglischVerfuegbar", 1);
        setzeErgInt(151, ergHVParam.paramPortal.personenAnzeigeAnredeMitAufnahmen, "personenAnzeigeAnredeMitAufnahmen",
                1);
        setzeErgInt(152, ergHVParam.paramPortal.textPostfachMitAufnahmen, "textPostfachMitAufnahmen", 1);
        setzeErgString(153, ergHVParam.paramPortal.letzterAktienregisterUpdate, "letzterAktienregisterUpdate", 1);
        setzeErgInt(154, ergHVParam.paramPortal.registrierungFuerEmailVersandMoeglich,
                "registrierungFuerEmailVersandMoeglich", 1);
        setzeErgInt(155, ergHVParam.paramPortal.gewinnspielAktiv, "gewinnspielAktiv", 1);
        setzeErgInt(156, ergHVParam.paramPortal.separateTeilnahmebedingungenFuerGewinnspiel,
                "separateTeilnahmebedingungenFuerGewinnspiel", 1);
        setzeErgInt(157, ergHVParam.paramPortal.dauerhaftesPasswortMoeglich, "dauerhaftesPasswortMoeglich", 1);
        setzeErgInt(158, ergHVParam.paramPortal.separateDatenschutzerklaerung, "separateDatenschutzerklaerung", 1);
        setzeErgInt(159, ergHVParam.paramPortal.separateNutzungshinweise, "separateNutzungshinweise", 1);
        setzeErgInt(160, ergHVParam.paramPortal.bestaetigungSeparateAktionaersPortalHinweiseErforderlich,
                "bestaetigungSeparateAktionaersPortalHinweiseErforderlich", 1);
        setzeErgInt(161, ergHVParam.paramPortal.bestaetigungSeparateHVPortalHinweiseErforderlich,
                "bestaetigungSeparateHVPortalHinweiseErforderlich", 1);
        setzeErgInt(162, ergHVParam.paramPortal.gesamtMarkierungJa, "gesamtMarkierungJa", 1);
        setzeErgInt(163, ergHVParam.paramPortal.gesamtMarkierungNein, "gesamtMarkierungNein", 1);
        setzeErgInt(164, ergHVParam.paramPortal.gesamtMarkierungImSinne, "gesamtMarkierungImSinne", 1);
        setzeErgInt(165, ergHVParam.paramPortal.gesamtMarkierungGegenSinne, "gesamtMarkierungGegenSinne", 1);
        setzeErgInt(166, ergHVParam.paramPortal.markierungJa, "markierungJa", 1);
        setzeErgInt(167, ergHVParam.paramPortal.markierungNein, "markierungNein", 1);
        setzeErgInt(168, ergHVParam.paramPortal.markierungEnthaltung, "markierungEnthaltung", 1);
        setzeErgInt(169, ergHVParam.paramPortal.markierungLoeschen, "markierungLoeschen", 1);
//        setzeErgInt(170, ergHVParam.paramPortal.markierungGegenJa, "markierungGegenJa", 1);
//        setzeErgInt(171, ergHVParam.paramPortal.markierungGegenNein, "markierungGegenNein", 1);
//        setzeErgInt(172, ergHVParam.paramPortal.markierungGegenEnthaltung, "markierungGegenEnthaltung", 1);
//        setzeErgInt(173, ergHVParam.paramPortal.markierungGegenLoeschen, "markierungGegenLoeschen", 1);
        setzeErgInt(174, ergHVParam.paramPortal.gastkartenAnforderungMoeglich, "gastkartenAnforderungMoeglich", 1);
        setzeErgInt(175, ergHVParam.paramPortal.oeffentlicheIDMoeglich, "oeffentlicheIDMoeglich", 1);
        setzeErgInt(176, ergHVParam.paramPortal.ekSelbstMoeglich, "ekSelbstMoeglich", 1);
        setzeErgInt(177, ergHVParam.paramPortal.ekVollmachtMoeglich, "ekVollmachtMoeglich", 1);
        setzeErgInt(178, ergHVParam.paramPortal.ek2PersonengemeinschaftMoeglich, "ek2PersonengemeinschaftMoeglich", 1);
        setzeErgInt(179, ergHVParam.paramPortal.ek2MitOderOhneVollmachtMoeglich, "ek2MitOderOhneVollmachtMoeglich", 1);
        setzeErgInt(180, ergHVParam.paramPortal.ek2SelbstMoeglich, "ek2SelbstMoeglich", 1);
        setzeErgInt(181, ergHVParam.paramPortal.verfahrenPasswortVergessen, "verfahrenPasswortVergessen", 1);

        setzeErgInt(184, ergHVParam.paramPortal.anzeigeStartseite, "anzeigeStartseite", 1);
        setzeErgInt(185, ergHVParam.paramPortal.gesamtMarkierungEnthaltung, "gesamtMarkierungEnthaltung", 1);
        setzeErgInt(186, ergHVParam.paramPortalServer.statusOnlineTicket, "statusOnlineTicket", 1);

        setzeErgInt(187, ergHVParam.paramPortal.briefwahlAngeboten, "briefwahlAngeboten", 1);
        setzeErgInt(188, ergHVParam.paramPortal.vollmachtDritteAngeboten, "vollmachtDritteAngeboten", 1);
        setzeErgInt(189, ergHVParam.paramPortal.vollmachtKIAVAngeboten, "vollmachtKIAVAngeboten", 1);

        hString = "";
        for (int i1 = 0; i1 <= 4; i1++) {
            hString = hString + Integer.toString(ergHVParam.paramPortal.portalFuerGattungMoeglich[i1]);
        }
        setzeErgString(190, hString, "portalFuerGattungMoeglich[]", 1);

        hString = "";
        for (int i1 = 0; i1 <= 4; i1++) {
            hString = hString + Integer.toString(ergHVParam.paramPortal.stimmabgabeFuerGattungMoeglich[i1]);
        }
        setzeErgString(191, hString, "stimmabgabeFuerGattungMoeglich[]", 1);

        setzeErgInt(192, ergHVParam.paramPortal.phasePortal, "phasePortal", 1);
        setzeErgInt(193, ergHVParam.paramPortal.datumsformatDE, "datumsformatDE", 1);
        setzeErgInt(194, ergHVParam.paramPortal.datumsformatEN, "datumsformatEN", 1);
        //		setzeErgInt(195, ergHVParam.paramPortal.loginInhaberaktien, "loginInhaberaktien", 1);
        setzeErgInt(196, ergHVParam.paramPortal.gesamtMarkierungAllesLoeschen, "gesamtMarkierungAllesLoeschen", 1);
        setzeErgInt(197, ergHVParam.paramPortal.gegenantraegeWeisungenMoeglich, "gegenantraegeWeisungenMoeglich", 1);
//        setzeErgInt(198, ergHVParam.paramPortal.markierungGegenUnterstuetzen, "markierungGegenUnterstuetzen", 1);
        setzeErgInt(199, ergHVParam.paramPortal.gegenantragsText, "gegenantragsText", 1);
        setzeErgInt(200, ergHVParam.paramPortal.mehrereStimmrechtsvertreter, "mehrereStimmrechtsvertreter", 1);

//        
//        for (int i = 201; i <= 220; i++) {
//            hString = "";
//            for (int i1 = 0; i1 <= 19; i1++) {
//                hString = hString + Integer.toString(ergHVParam.paramPortal.phasenDetails[i - 200][i1]);
//            }
//            setzeErgString(i, hString, "phasenDetails[]", 1);
//
//        }

        for (int i = 221; i <= 240; i++) {
            setzeErgString(i, ergHVParam.paramPortalServer.phasenNamen[i - 220], "phasenNamen[]", 1);
        }

        setzeErgInt(241, ergHVParam.paramPortal.adressaenderungMoeglich, "adressaenderungMoeglich", 1);
        setzeErgInt(242, ergHVParam.paramPortal.artSprachumschaltung, "artSprachumschaltung", 1);
        setzeErgInt(243, ergHVParam.paramPortalServer.appInstallButtonsAnzeigen, "appInstallButtonsAnzeigen", 1);
        setzeErgInt(244, ergHVParam.paramPortal.kommunikationsspracheAuswahl, "kommunikationsspracheAuswahl", 1);
        setzeErgInt(245, ergHVParam.paramPortal.publikationenAnbieten, "publikationenAnbieten", 1);
        setzeErgInt(246, ergHVParam.paramPortal.kontaktDetailsAnbieten, "kontaktDetailsAnbieten", 1);
        setzeErgInt(247, ergHVParam.paramPortal.kontaktFenster, "kontaktFenster", 1);
        setzeErgInt(248, ergHVParam.paramPortal.standardTexteBeruecksichtigen, "standardTexteBeruecksichtigen", 1);
        setzeErgInt(249, ergHVParam.paramPortal.impressumEmittent, "impressumEmittent", 1);
        setzeErgInt(250, ergHVParam.paramPortal.emailNurBeiEVersandOderPasswort, "emailNurBeiEVersandOderPasswort", 1);
        setzeErgInt(251, ergHVParam.paramPortal.varianteDialogablauf, "varianteDialogablauf", 1);
        setzeErgInt(252, ergHVParam.paramPortal.loginGesperrt, "loginGesperrt", 1);
        setzeErgInt(253, ergHVParam.paramPortal.loginIPTrackingAktiv, "loginIPTrackingAktiv", 1);
        setzeErgInt(254, ergHVParam.paramPortal.lfdHVDialogVeranstaltungenInMenue, "lfdHVDialogVeranstaltungenInMenue", 1);
        setzeErgInt(255, ergHVParam.paramPortal.checkboxBeiSRV, "checkboxBeiSRV", 1);
        setzeErgInt(256, ergHVParam.paramPortal.checkboxBeiKIAV, "checkboxBeiKIAV", 1);
        setzeErgInt(257, ergHVParam.paramPortal.checkboxBeiBriefwahl, "checkboxBeiBriefwahl", 1);
        setzeErgInt(258, ergHVParam.paramPortal.bestaetigungsseiteEinstellungen, "bestaetigungsseiteEinstellungen", 1);
        setzeErgInt(259, ergHVParam.paramPortal.verfahrenPasswortVergessenAblauf, "verfahrenPasswortVergessenAblauf",
                1);
        setzeErgInt(260, ergHVParam.paramPortal.absendeMailAdresse, "absendeMailAdresse", 1);

        setzeErgInt(261, ergHVParam.paramPortal.lfdHVGeneralversammlungInMenue, "lfdHVGeneralversammlungInMenue", 1);
        setzeErgInt(262, ergHVParam.paramPortal.lfdHVUnterlagenInMenue, "lfdHVDialogVeranstaltungenInMenue",
                1);
        setzeErgInt(263, ergHVParam.paramPortal.lfdHVEinstellungenInMenue, "lfdHVEinstellungenInMenue", 1);
        setzeErgInt(264, ergHVParam.paramPortal.lfdHVGeneralversammlungBriefwahlInMenue, "lfdHVGeneralversammlungBriefwahlInMenue", 1);
        setzeErgInt(265, ergHVParam.paramPortal.reihenfolgeRegistrierung, "reihenfolgeRegistrierung", 1);
        setzeErgInt(266, ergHVParam.paramPortal.passwortPerPostPruefen, "passwortPerPostPruefen", 1);
        setzeErgInt(267, ergHVParam.paramPortal.basisSetStandardTexteVerwenden, "basisSetStandardTexteVerwenden", 1);
        setzeErgInt(268, ergHVParam.paramPortal.srvAngeboten, "srvAngeboten", 1);

        setzeErgString(269, ergHVParam.paramPortal.logoName, "logoName", 1);
        setzeErgInt(270, ergHVParam.paramPortal.logoBreite, "logoBreite", 1);
        setzeErgInt(271, ergHVParam.paramPortal.logoHoehe, "logoHoehe", 1);
        setzeErgString(272, ergHVParam.paramPortal.cssName, "cssName", 1);
        setzeErgString(273, ergHVParam.paramPortal.designKuerzel, "designKuerzel", 1);
        setzeErgInt(274, ergHVParam.paramPortal.anzeigeStimmen, "anzeigeStimmen", 1);
        setzeErgInt(275, ergHVParam.paramPortal.anmeldenOhneWeitereWK, "anmeldenOhneWeitereWK", 1);
        setzeErgInt(276, ergHVParam.paramPortal.logoutObenOderUnten, "logoutObenOderUnten", 1);

        setzeErgInt(277, ergHVParam.paramPortal.lfdHVGeneralversammlungTeilnahmeInMenue, "lfdHVGeneralversammlungTeilnahmeInMenue", 1);

        setzeErgInt(278, ergHVParam.paramPortal.loginVerfahren, "loginVerfahren", 1);
        setzeErgInt(279, ergHVParam.paramPortal.bestaetigungBeiWeisung, "bestaetigungBeiWeisung", 1);

        setzeErgInt(280, ergHVParam.paramPortal.lfdHVGeneralversammlungTeilnahmeGast, "lfdHVGeneralversammlungTeilnahmeGast", 1);
        setzeErgInt(281, ergHVParam.paramPortal.lfdHVBeiratswahlInMenue, "lfdHVBeiratswahlInMenue", 1);

        setzeErgInt(282, ergHVParam.paramPortal.captchaVerwenden, "captchaVerwenden", 1);
        setzeErgInt(283, ergHVParam.paramPortal.loginVerzoegerungAbVersuch, "loginVerzoegungAbVersuch", 1);
        setzeErgInt(284, ergHVParam.paramPortal.alternativeLoginKennung, "alternativeLoginKennung", 1);
        setzeErgInt(285, ergHVParam.paramPortal.teilnehmerKannSichWeitereKennungenZuordnen, "teilnehmerKannSichWeitereKennungenZuordnen", 1);
        setzeErgInt(286, ergHVParam.paramPortal.bestaetigungPerEmailUeberallZulassen, "bestaetigungPerEmailUeberallZulassen", 1);
        setzeErgInt(287, ergHVParam.paramPortal.loginVerzoegerungSekunden, "loginVerzoegungAbVersuch", 1);
        setzeErgInt(288, ergHVParam.paramPortal.kennungAufbereiten, "kennungAufbereiten", 1);
        setzeErgInt(289, ergHVParam.paramPortal.kennungAufbereitenFuerAnzeige, "kennungAufbereitenFuerAnzeige", 1);
        setzeErgInt(290, ergHVParam.paramPortal.checkboxBeiVollmacht, "checkboxBeiVollmacht", 1);

        /***ParamGästemodul*****/
        setzeErgInt(300, ergHVParam.paramGaesteModul.mailVerschickenGK, "mailVerschickenGK", 1);
        /*301 bis 304: lokal*/
        setzeErgInt(305, ergHVParam.paramGaesteModul.feldAnredeVerwenden, "feldAnredeVerwenden", 1);
        setzeErgInt(306, ergHVParam.paramGaesteModul.feldTitelVerwenden, "feldTitelVerwenden", 1);
        setzeErgInt(307, ergHVParam.paramGaesteModul.feldAdelstitelVerwenden, "feldAdelstitelVerwenden", 1);
        setzeErgInt(308, ergHVParam.paramGaesteModul.feldNameVerwenden, "feldNameVerwenden", 1);
        setzeErgInt(309, ergHVParam.paramGaesteModul.feldVornameVerwenden, "feldVornameVerwenden", 1);
        setzeErgInt(310, ergHVParam.paramGaesteModul.feldZuHaendenVerwenden, "feldZuHaendenVerwenden", 1);
        setzeErgInt(311, ergHVParam.paramGaesteModul.feldZusatz1Verwenden, "feldZusatz1Verwenden", 1);
        setzeErgInt(312, ergHVParam.paramGaesteModul.feldZusatz2Verwenden, "feldZusatz2Verwenden", 1);
        setzeErgInt(313, ergHVParam.paramGaesteModul.feldStrasseVerwenden, "feldStrasseVerwenden", 1);
        setzeErgInt(314, ergHVParam.paramGaesteModul.feldLandVerwenden, "feldLandVerwenden", 1);
        setzeErgInt(315, ergHVParam.paramGaesteModul.feldPLZVerwenden, "feldPLZVerwenden", 1);
        setzeErgInt(316, ergHVParam.paramGaesteModul.feldOrtVerwenden, "feldOrtVerwenden", 1);
        setzeErgInt(317, ergHVParam.paramGaesteModul.feldMailadresseVerwenden, "feldMailadresseVerwenden", 1);
        setzeErgInt(318, ergHVParam.paramGaesteModul.feldKommunikationsspracheVerwenden, "feldKommunikationsspracheVerwenden", 1);
        setzeErgInt(319, ergHVParam.paramGaesteModul.feldGruppeVerwenden, "feldGruppeVerwenden", 1);
        setzeErgInt(320, ergHVParam.paramGaesteModul.feldAusstellungsgrundVerwenden, "feldAusstellungsgrundVerwenden",
                1);
        setzeErgInt(321, ergHVParam.paramGaesteModul.feldVipVerwenden, "feldVipVerwenden", 1);

        setzeErgString(322, ergHVParam.paramGaesteModul.feldZuHaendenBezeichnung, "feldZuHaendenBezeichnung", 1);
        setzeErgString(323, ergHVParam.paramGaesteModul.feldZusatz1Bezeichnung, "feldZusatz1Bezeichnung", 1);
        setzeErgString(324, ergHVParam.paramGaesteModul.feldZusatz2Bezeichnung, "feldZusatz2Bezeichnung", 1);

        setzeErgInt(325, ergHVParam.paramGaesteModul.buttonSpeichernAnzeigen, "buttonSpeichernAnzeigen", 1);
        setzeErgInt(326, ergHVParam.paramGaesteModul.buttonSpeichernDruckenAnzeigen, "buttonSpeichernDruckenAnzeigen", 1);

        setzeErgInt(327, ergHVParam.paramGaesteModul.feldAnredeVerwendenPortal, "feldAnredeVerwendenPortal", 1);
        setzeErgInt(328, ergHVParam.paramGaesteModul.feldTitelVerwendenPortal, "feldTitelVerwendenPortal", 1);
        setzeErgInt(329, ergHVParam.paramGaesteModul.feldAdelstitelVerwendenPortal, "feldAdelstitelVerwendenPortal", 1);
        setzeErgInt(330, ergHVParam.paramGaesteModul.feldNameVerwendenPortal, "feldNameVerwendenPortal", 1);
        setzeErgInt(331, ergHVParam.paramGaesteModul.feldVornameVerwendenPortal, "feldVornameVerwendenPortal", 1);
        setzeErgInt(332, ergHVParam.paramGaesteModul.feldZuHaendenVerwendenPortal, "feldZuHaendenVerwendenPortal", 1);
        setzeErgInt(333, ergHVParam.paramGaesteModul.feldZusatz1VerwendenPortal, "feldZusatz1VerwendenPortal", 1);
        setzeErgInt(334, ergHVParam.paramGaesteModul.feldZusatz2VerwendenPortal, "feldZusatz2VerwendenPortal", 1);
        setzeErgInt(335, ergHVParam.paramGaesteModul.feldStrasseVerwendenPortal, "feldStrasseVerwendenPortal", 1);
        setzeErgInt(336, ergHVParam.paramGaesteModul.feldLandVerwendenPortal, "feldLandVerwendenPortal", 1);
        setzeErgInt(337, ergHVParam.paramGaesteModul.feldPLZVerwendenPortal, "feldPLZVerwendenPortal", 1);
        setzeErgInt(338, ergHVParam.paramGaesteModul.feldOrtVerwendenPortal, "feldOrtVerwendenPortal", 1);
        setzeErgInt(339, ergHVParam.paramGaesteModul.feldMailadresseVerwendenPortal, "feldMailadresseVerwendenPortal", 1);
        setzeErgInt(340, ergHVParam.paramGaesteModul.feldKommunikationsspracheVerwendenPortal, "feldKommunikationsspracheVerwendenPortal", 1);

        /*********************Akkreditierung*************************************************/
        setzeErgBoolean(400, ergHVParam.paramAkkreditierung.gaesteKartenHabenWiederzugangAbgangsCode,
                "gaesteKartenHabenWiederzugangAbgangsCode", 1);
        setzeErgBoolean(401, ergHVParam.paramAkkreditierung.eintrittskarteWirdAuchFuerAbgangWiederzugangVerwendet,
                "eintrittskarteWirdAuchFuerAbgangWiederzugangVerwendet", 1);
        setzeErgBoolean(402, ergHVParam.paramAkkreditierung.eintrittskarteWirdStimmkarte,
                "eintrittskarteWirdStimmkarte", 1);
        setzeErgBoolean(403, ergHVParam.paramAkkreditierung.labelAuchFuerAppIdent, "labelAuchFuerAppIdent", 1);
        setzeErgInt(404, ergHVParam.paramAkkreditierung.positionVertretername, "positionVertretername", 1);
        setzeErgInt(405, ergHVParam.paramAkkreditierung.delayArt, "delayArt", 1);
        setzeErgInt(406, ergHVParam.paramAkkreditierung.beiZugangStornierenAusSammelAutomatisch,
                "beiZugangStornierenAusSammelAutomatisch", 1);

        setzeErgInt(407, ergHVParam.paramAkkreditierung.skOffenlegungSRV, "skOffenlegungSRV", 1);
        setzeErgInt(408, ergHVParam.paramAkkreditierung.skOffenlegungKIAV, "skOffenlegungKIAV", 1);
        setzeErgInt(409, ergHVParam.paramAkkreditierung.skOffenlegungDauer, "skOffenlegungDauer", 1);
        setzeErgInt(410, ergHVParam.paramAkkreditierung.skOffenlegungOrga, "skOffenlegungOrga", 1);
        setzeErgInt(411, ergHVParam.paramAkkreditierung.formularNachErstzugang, "formularNachErstzugang", 1);
        setzeErgInt(412, ergHVParam.paramAkkreditierung.zusaetzlichesInitialpasswortBeiErstzugang, "zusaetzlichesInitialpasswortBeiErstzugang", 1);
        setzeErgInt(413, ergHVParam.paramAkkreditierung.serviceDeskSetNr, "serviceDeskSetNr", 1);
        setzeErgInt(414, ergHVParam.paramAkkreditierung.label_Font, "label_Font", 1);
        setzeErgInt(415, ergHVParam.paramAkkreditierung.labeldruckVerfahren, "labeldruckVerfahren", 1);

        /***********Ehemals parameterLfd-Table. Neue Nummer=alte Nummer+500***********************/
        setzeErgInt(501, ergHVParam.paramPortal.lfdHVPortalInBetrieb, "pLfdHVPortalInBetrieb", 1);
        setzeErgInt(502, ergHVParam.paramPortal.lfdHVPortalErstanmeldungIstMoeglich,
                "pLfdHVPortalErstanmeldungIstMoeglich", 1);
        setzeErgInt(503, ergHVParam.paramPortal.lfdHVPortalEKIstMoeglich, "pLfdHVPortalEKIstMoeglich", 1);
        setzeErgInt(504, ergHVParam.paramPortal.lfdHVPortalSRVIstMoeglich, "pLfdHVPortalSRVIstMoeglich", 1);
        setzeErgInt(505, ergHVParam.paramPortal.lfdHVPortalBriefwahlIstMoeglich, "pLfdHVPortalBriefwahlIstMoeglich", 1);
        setzeErgInt(506, ergHVParam.paramPortal.lfdHVPortalKIAVIstMoeglich, "pLfdHVPortalKIAVIstMoeglich", 1);
        setzeErgInt(507, ergHVParam.paramPortal.lfdHVPortalVollmachtDritteIstMoeglich,
                "pLfdHVPortalVollmachtDritteIstMoeglich", 1);

        hString = "";
        for (int i1 = 0; i1 < 6; i1++) {
            hString = hString
                    + Integer.toString(ergHVParam.paramAkkreditierung.pLfdPraesenzStornierenAusSammelMoeglich[i1]);
        }
        setzeErgString(508, hString, "pLfdPraesenzStornierenAusSammelMoeglich[]", 1);

        hString = "";
        for (int i1 = 0; i1 < 6; i1++) {
            hString = hString
                    + Integer.toString(ergHVParam.paramAkkreditierung.pLfdPraesenzDeaktivierenAusSammelMoeglich[i1]);
        }
        setzeErgString(509, hString, "pLfdPraesenzDeaktivierenAusSammelMoeglich[]", 1);

        hString = "";
        for (int i1 = 0; i1 < 6; i1++) {
            hString = hString
                    + Integer.toString(ergHVParam.paramAkkreditierung.pLfdPraesenzErteilenInSammelMoeglich[i1]);
        }
        setzeErgString(510, hString, "pLfdPraesenzDeaktivierenAusSammelMoeglich[]", 1);

        setzeErgInt(512, ergHVParam.paramAkkreditierung.plfdHVGestartet, "plfdHVGestartet", 1);
        setzeErgInt(513, ergHVParam.paramAkkreditierung.plfdHVDelayed, "plfdHVDelayed", 1);
        setzeErgInt(514, ergHVParam.paramPortalServer.pLfdPortalStartNr, "pLfdPortalStartNr", 1);
        setzeErgInt(515, ergHVParam.paramPortal.lfdVorDerHVNachDerHV, "pLfdVorDerHVNachDerHV", 1);

        /***********************600 bis 699: Grundkapital sowie andere Gattungsabhängige Parameter**********************************/
        for (int i1 = 0; i1 < 5; i1++) {
            setzeErgLong(600 + i1, ergHVParam.paramBasis.grundkapitalStueck[i1],
                    "grundkapitalStueck" + Integer.toString(i1 + 1), 1);
        }
        for (int i1 = 0; i1 < 5; i1++) {
            setzeErgLong(605 + i1, ergHVParam.paramBasis.grundkapitalVermindertStueck[i1],
                    "grundkapitalVermindertStueck" + Integer.toString(i1 + 1), 1);
        }
        for (int i1 = 0; i1 < 5; i1++) {
            setzeErgDouble(610 + i1, ergHVParam.paramBasis.wertEinerAktie[i1],
                    "wertEinerAktie" + Integer.toString(i1 + 1), 1);
        }
        for (int i1 = 0; i1 < 5; i1++) {
            setzeErgInt(615 + i1, ergHVParam.paramBasis.anzahlNachkommastellenKapital[i1],
                    "anzahlNachkommastellenKapital" + Integer.toString(i1 + 1), 1);
        }
        for (int i1 = 0; i1 < 5; i1++) {
            setzeErgBoolean(620 + i1, ergHVParam.paramBasis.eintrittskarteNeuVergeben[i1],
                    "eintrittskarteNeuVergeben" + Integer.toString(i1 + 1), 1);
        }
        for (int i1 = 0; i1 < 5; i1++) {
            setzeErgBoolean(625 + i1, ergHVParam.paramBasis.zugangMoeglich[i1],
                    "zugangMoeglich" + Integer.toString(i1 + 1), 1);
        }
        for (int i1 = 0; i1 < 5; i1++) {
            setzeErgLong(630 + i1, ergHVParam.paramBasis.grundkapitalEigeneAktienStueck[i1],
                    "grundkapitalEigeneAktienStueck" + Integer.toString(i1 + 1), 1);
        }

        
        
        /***********************700: Stimmblockzuordnung (Fortsetzung - siehe auch 18 ff)***********************/
        for (int i1 = 0; i1 < 5; i1++) {
            for (int i2 = 0; i2 < 5; i2++) {
                setzeErgString(700 + (i1 * 5) + i2,
                        ergHVParam.paramAkkreditierung.pPraesenzStimmkartenZuordnenGattungText[i1][i2],
                        "pPraesenzStimmkartenZuordnenGattungText[" + Integer.toString(i1) + "][" + Integer.toString(i2)
                                + "]",
                        1);

            }
        }
        for (int i1 = 0; i1 < 5; i1++) {
            setzeErgString(725 + i1, ergHVParam.paramAkkreditierung.pPraesenzStimmkarteSecondZuordnenGattungText[i1],
                    "pPraesenzStimmkarteSecondZuordnenGattungText[" + Integer.toString(i1) + "]", 1);
        }

        /***********************800: Präsenzliste*********************************/
        /*800-811*/
        for (int i = 0; i < 3; i++) {
            for (int i1 = 0; i1 < 4; i1++) {/*Sortierung*/
                String hString1 = "";
                for (int i2 = 0; i2 <= 5; i2++) {/*0=alle, i2 = Gattung*/
                    hString1 += ergHVParam.paramPraesenzliste.einzeldruckFormatListe[i][i1][i2];
                    hString1 += ergHVParam.paramPraesenzliste.einzeldruckFormatZusammenstellung[i][i1][i2];
                    hString1 += Integer.toString(ergHVParam.paramPraesenzliste.einzeldruckInPDFaufnehmen[i][i1][i2]);
                    hString1 += ".";
                }
                String hString2 = "einzeldruck[" + Integer.toString(i) + "][" + Integer.toString(i1) + "]";
                setzeErgString(800 + i * 4 + i1, hString1, hString2, 1);
            }
        }

        /*****************900: Bestandsverwaltung********************************/
        /*900 bis 906*/
        for (int i = 900; i < 907; i++) {
            int startzeile = (i - 900) * 3;
            String hString1 = "";
            for (int zeile1 = 0; zeile1 < 3; zeile1++) {
                for (int spalte2 = 0; spalte2 < 4; spalte2++) {
                    int abstimmIdent = ergHVParam.paramBestandsverwaltung.weisungQS[startzeile + zeile1][spalte2];
                    String hString2 = Integer.toString(abstimmIdent);
                    hString2 = CaString.fuelleLinksNull(hString2, 3);
                    hString1 += hString2;
                }
            }
            String hString3 = "weisungQS " + Integer.toString(startzeile) + " bis " + Integer.toString(startzeile + 2);
            setzeErgString(i, hString1, hString3, 1);
        }

        /********************2000 bis 2200 Fortsetzung Abstimmungsparameter*********************************/
        setzeErgInt(2000, ergHVParam.paramAbstimmungParameter.weisungNichtMarkierteSpeichernAlsSRV,
                "weisungNichtMarkierteSpeichernAlsSRV", 1);
        setzeErgInt(2001, ergHVParam.paramAbstimmungParameter.weisungNichtMarkierteSpeichernAlsBriefwahl,
                "weisungNichtMarkierteSpeichernAlsBriefwahl", 1);
        setzeErgInt(2002, ergHVParam.paramAbstimmungParameter.weisungNichtMarkierteSpeichernAlsKIAV,
                "weisungNichtMarkierteSpeichernAlsKIAV", 1);
        setzeErgInt(2003, ergHVParam.paramAbstimmungParameter.weisungNichtMarkierteSpeichernAlsDauer,
                "weisungNichtMarkierteSpeichernAlsDauer", 1);
        setzeErgInt(2004, ergHVParam.paramAbstimmungParameter.weisungNichtMarkierteSpeichernAlsOrg,
                "weisungNichtMarkierteSpeichernAlsOrg", 1);

        setzeErgInt(2005, ergHVParam.paramAbstimmungParameter.weisungHVNichtMarkierteSpeichernAls,
                "weisungHVNichtMarkierteSpeichernAls", 1);
        setzeErgInt(2006, ergHVParam.paramAbstimmungParameter.abstimmungNichtMarkierteSpeichern,
                "abstimmungNichtMarkierteSpeichern", 1);

        setzeErgInt(2007, ergHVParam.paramAbstimmungParameter.weisungVorbelegungMitSRV, "weisungVorbelegungMitSRV", 1);
        setzeErgInt(2008, ergHVParam.paramAbstimmungParameter.weisungVorbelegungMitBriefwahl,
                "weisungVorbelegungMitBriefwahl", 1);
        setzeErgInt(2009, ergHVParam.paramAbstimmungParameter.weisungVorbelegungMitKIAV, "weisungVorbelegungMitKIAV",
                1);
        setzeErgInt(2010, ergHVParam.paramAbstimmungParameter.weisungVorbelegungMitDauer, "weisungVorbelegungMitDauer",
                1);
        setzeErgInt(2011, ergHVParam.paramAbstimmungParameter.weisungVorbelegungMitOrg, "weisungVorbelegungMitOrg", 1);
        setzeErgInt(2012, ergHVParam.paramAbstimmungParameter.weisungHVVorbelegungMit, "weisungHVVorbelegungMit", 1);
        setzeErgInt(2013, ergHVParam.paramAbstimmungParameter.abstimmungVorbelegungMit, "abstimmungVorbelegungMit", 1);

        setzeErgInt(2014, ergHVParam.paramAbstimmungParameter.undefinierteWeisungenZaehlenAls,
                "undefinierteWeisungenZaehlenAls", 1);
        setzeErgInt(2015, ergHVParam.paramAbstimmungParameter.ungueltigeZaehlenAlsEnthaltung,
                "ungueltigeZaehlenAlsEnthaltung", 1);
        setzeErgInt(2016, ergHVParam.paramAbstimmungParameter.sortierungDruckausgabeIndividuell,
                "sortierungDruckausgabeIndividuell", 1);
        setzeErgInt(2017, ergHVParam.paramAbstimmungParameter.textVerwendenTablet, "textVerwendenTablet", 1);
        setzeErgInt(2018, ergHVParam.paramAbstimmungParameter.textVerwendenFormular, "textVerwendenFormular", 1);
        setzeErgInt(2019, ergHVParam.paramAbstimmungParameter.nichtTeilnahmeMoeglich, "nichtTeilnahmeMoeglich", 1);
//      Nicht mehr verwendet
//        setzeErgInt(2020, ergHVParam.paramAbstimmungParameter.gegenantragMarkiertMoeglich,
//                "gegenantragMarkiertMoeglich", 1);
        setzeErgInt(2021, ergHVParam.paramAbstimmungParameter.anzeigeBezeichnungKurzAktiv,
                "anzeigeBezeichnungKurzAktiv", 1);

        setzeErgBoolean(2022, ergHVParam.paramAbstimmungParameter.weisungenGegenantraegePortalSeparat,
                "weisungenGegenantraegePortalSeparat", 1);
        setzeErgBoolean(2023, ergHVParam.paramAbstimmungParameter.weisungenGegenantraegeInternSeparat,
                "weisungenGegenantraegeInternSeparat", 1);
        setzeErgBoolean(2024, ergHVParam.paramAbstimmungParameter.weisungenGegenantraegeVerlassenHVSeparat,
                "weisungenGegenantraegeVerlassenHVSeparat", 1);

        setzeErgBoolean(2025, ergHVParam.paramAbstimmungParameter.eingabezwangSRV, "eingabezwangSRV", 1);
        setzeErgBoolean(2026, ergHVParam.paramAbstimmungParameter.eingabezwangBriefwahl, "eingabezwangBriefwahl", 1);
        setzeErgBoolean(2027, ergHVParam.paramAbstimmungParameter.eingabezwangKIAV, "eingabezwangKIAV", 1);
        setzeErgBoolean(2028, ergHVParam.paramAbstimmungParameter.eingabezwangDauer, "eingabezwangDauer", 1);
        setzeErgBoolean(2029, ergHVParam.paramAbstimmungParameter.eingabezwangOrg, "eingabezwangOrg", 1);
        setzeErgBoolean(2030, ergHVParam.paramAbstimmungParameter.eingabezwangWeisungHV, "eingabezwangWeisungHV", 1);
        setzeErgBoolean(2031, ergHVParam.paramAbstimmungParameter.eingabezwangAbstimmung, "eingabezwangAbstimmung", 1);

        setzeErgString(2032, ergHVParam.paramAbstimmungParameter.beiTabletFarbeJa, "beiTabletFarbeJa", 1);
        setzeErgString(2033, ergHVParam.paramAbstimmungParameter.beiTabletFarbeNein, "beiTabletFarbeNein", 1);
        setzeErgString(2034, ergHVParam.paramAbstimmungParameter.beiTabletFarbeEnthaltung, "beiTabletFarbeEnthaltung", 1);

        setzeErgString(2035, ergHVParam.paramAbstimmungParameter.beiTabletFarbeUnmarkiertJa, "beiTabletFarbeUnmarkiertJa", 1);
        setzeErgString(2036, ergHVParam.paramAbstimmungParameter.beiTabletFarbeUnmarkiertNein, "beiTabletFarbeUnmarkiertNein", 1);
        setzeErgString(2037, ergHVParam.paramAbstimmungParameter.beiTabletFarbeUnmarkiertEnthaltung, "beiTabletFarbeUnmarkiertEnthaltung", 1);

        setzeErgString(2038, ergHVParam.paramAbstimmungParameter.beiTabletFarbeGesamtmarkierungJa, "beiTabletFarbeGesamtmarkierungJa", 1);
        setzeErgString(2039, ergHVParam.paramAbstimmungParameter.beiTabletFarbeGesamtmarkierungNein, "beiTabletFarbeGesamtmarkierungNein", 1);
        setzeErgString(2040, ergHVParam.paramAbstimmungParameter.beiTabletFarbeGesamtmarkierungEnthaltung, "beiTabletFarbeGesamtmarkierungEnthaltung", 1);
        setzeErgInt(2041, ergHVParam.paramAbstimmungParameter.abstimmungsLoadBeiAktivierungImAbstimmungsablauf, "abstimmungsLoadBeiAktivierungImAbstimmungsablauf", 1);

        setzeErgInt(2042, ergHVParam.paramAbstimmungParameter.stimmzetteldruckAbstimmungsblockBasis, "stimmzetteldruckAbstimmungsblockBasis", 1);
        setzeErgInt(2043, ergHVParam.paramAbstimmungParameter.stimmzetteldruckAnzahlStimmzettel, "stimmzetteldruckAnzahlStimmzettel", 1);
        setzeErgInt(2044, ergHVParam.paramAbstimmungParameter.briefwahlAusgebenInAbstimmungsergebnis, "briefwahlAusgebenInAbstimmungsergebnis", 1);
        
        /********************2201 bis 2400 Fortsetzung Portal-Parameter*********************************/
        //		setzeErgInt(2201, ergHVParam.paramPortal.lfdHVStreamIstMoeglich, "lfdHVStreamIstMoeglich", 1);
        setzeErgInt(2202, ergHVParam.paramPortal.lfdHVMitteilungIstMoeglich, "lfdHVMitteilungIstMoeglich", 1);
        setzeErgInt(2203, ergHVParam.paramPortal.lfdHVFragenStufe1IstMoeglich, "lfdHVFragenStufe1IstMoeglich", 1);
        setzeErgInt(2204, ergHVParam.paramPortal.lfdHVFragenStufe2IstMoeglich, "lfdHVFragenStufe2IstMoeglich", 1);
        setzeErgInt(2205, ergHVParam.paramPortal.lfdHVFragenStufe3IstMoeglich, "lfdHVFragenStufe3IstMoeglich", 1);
        setzeErgInt(2206, ergHVParam.paramPortal.lfdHVFragenStufe4IstMoeglich, "lfdHVFragenStufe4IstMoeglich", 1);

        setzeErgInt(2207, ergHVParam.paramPortal.widerspruecheAnzahlJeAktionaer, "mitteilungenAnzahlJeAktionaer", 1);
        setzeErgInt(2208, ergHVParam.paramPortal.widerspruecheLaenge, "mitteilungLaenge", 1);
        setzeErgInt(2209, ergHVParam.paramPortal.mitteilungNurFuerAngemeldete, "mitteilungNurFuerAngemeldete", 1);
        setzeErgInt(2210, ergHVParam.paramPortal.mitteilungNurMitAbgegebenerStimme, "mitteilungNurMitAbgegebenerStimme",
                1);

        setzeErgInt(2211, ergHVParam.paramPortal.streamAnbieter, "streamAnbieter", 1);
        setzeErgInt(2212, ergHVParam.paramPortal.streamTestlinkWirdAngeboten, "streamTestlinkWirdAngeboten", 1);

        setzeErgInt(2213, ergHVParam.paramPortal.fragenBisAktien, "fragenBisAktien", 1);
        setzeErgInt(2214, ergHVParam.paramPortal.fragenAbAktien, "fragenAbAktien", 1);

        setzeErgInt(2215, ergHVParam.paramPortal.fragenAnzahlJeAktionaer, "fragenAnzahlStufe1", 1);
        setzeErgInt(2216, ergHVParam.paramPortal.fragenLaenge, "fragenLaengeStufe1", 1);
        setzeErgInt(2217, ergHVParam.paramPortal.fragenAnzahlStufe2, "fragenAnzahlStufe2", 1);
        setzeErgInt(2218, ergHVParam.paramPortal.fragenLaengeStufe2, "fragenLaengeStufe2", 1);
        setzeErgInt(2219, ergHVParam.paramPortal.fragenAnzahlStufe3, "fragenAnzahlStufe3", 1);
        setzeErgInt(2220, ergHVParam.paramPortal.fragenLaengeStufe3, "fragenLaengeStufe3", 1);
        setzeErgInt(2221, ergHVParam.paramPortal.fragenAnzahlStufe4, "fragenAnzahlStufe4", 1);
        setzeErgInt(2222, ergHVParam.paramPortal.fragenLaengeStufe4, "fragenLaengeStufe4", 1);

        setzeErgInt(2223, ergHVParam.paramPortal.fragenStellerAbfragen, "fragenstellerAbfragen", 1);

        setzeErgInt(2224, ergHVParam.paramPortal.mitteilungenAngeboten, "mitteilungenAngeboten", 1);

        //		setzeErgInt(2225, ergHVParam.paramPortal.streamAngeboten, "streamAngeboten", 1);
        setzeErgInt(2226, ergHVParam.paramPortal.streamNurFuerAngemeldete, "streamNurFuerAngemeldete", 1);

        setzeErgInt(2227, ergHVParam.paramPortal.fragenAngeboten, "fragenAngeboten", 1);
        setzeErgInt(2228, ergHVParam.paramPortal.fragenNurFuerAngemeldete, "fragenNurFuerAngemeldete", 1);

        setzeErgInt(2229, ergHVParam.paramPortal.widerspruecheStellerAbfragen, "mitteilungsstellerAbfragen", 1);
        setzeErgInt(2230, ergHVParam.paramPortal.mitteilungenDialogTopAngeboten, "mitteilungenDialogTopAngeboten", 1);
        setzeErgInt(2231, ergHVParam.paramPortal.mitteilungenTextAngeboten, "mitteilungenTextAngeboten", 1);
        setzeErgInt(2232, ergHVParam.paramPortal.briefwahlZusaetzlichZuSRVMoeglich, "briefwahlZusaetzlichZuSRVMoeglich",
                1);

        setzeErgInt(2233, ergHVParam.paramPortal.teilnehmerverzAngeboten, "teilnehmerverzAngeboten", 1);
        setzeErgInt(2234, ergHVParam.paramPortal.teilnehmerverzZusammenstellung, "teilnehmerverzZusammenstellung", 1);
        setzeErgInt(2235, ergHVParam.paramPortal.teilnehmerverzLetzteNr, "teilnehmerverzLetzteNr", 1);

//        setzeErgInt(2236, ergHVParam.paramPortal.abstimmungsergAngeboten, "abstimmungsergAngeboten", 1);
        setzeErgInt(2237, ergHVParam.paramPortal.abstimmungsergLetzteNr, "abstimmungsergLetzteNr", 1);

        setzeErgInt(2238, ergHVParam.paramPortal.widerspruecheStellerZulaessig, "mitteilungsstellerZulaessig", 1);
        setzeErgInt(2239, ergHVParam.paramPortal.fragenStellerZulaessig, "fragenstellerZulaessig", 1);

        setzeErgInt(2240, ergHVParam.paramPortal.widerspruecheMailBeiEingang, "mitteilungenMailBeiEingang", 1);
        setzeErgInt(2241, ergHVParam.paramPortal.fragenExternerZugriff, "fragenZugriffExtern", 1);

        setzeErgString(2242, ergHVParam.paramPortal.ekText, "ekText", 1);
        setzeErgString(2243, ergHVParam.paramPortal.ekTextMitArtikel, "ekTextMitArtikel", 1);
        setzeErgString(2244, ergHVParam.paramPortal.ekTextEN, "ekTextEN", 1);
        setzeErgString(2245, ergHVParam.paramPortal.ekTextENMitArtikel, "ekTextENMitArtikel", 1);

        setzeErgInt(2246, ergHVParam.paramPortal.lfdHVTeilnehmerverzIstMoeglich, "lfdHVTeilnehmerverzIstMoeglich", 1);
        setzeErgInt(2247, ergHVParam.paramPortal.lfdHVAbstimmungsergIstMoeglich, "lfdHVAbstimmungsergIstMoeglich", 1);

        setzeErgInt(2248, ergHVParam.paramPortal.timeoutAufLang, "timeoutAufLang", 1);
        setzeErgInt(2249, ergHVParam.paramPortal.mitteilungsButtonImmerSichtbar, "mitteilungsButtonImmerSichtbar", 1);
        setzeErgInt(2250, ergHVParam.paramPortal.fragenMailBeiEingang, "fragenMailBeiEingang", 1);

        /**2251 bis 2260 - unterlage1aktiv etc - entfernt 08.01.2022*/


        setzeErgInt(2261, ergHVParam.paramPortal.streamAnbieter2, "streamAnbieter2", 1);

        /**2262 bis 2271 - unterlage11aktiv etc - entfernt 08.01.2022*/

        setzeErgInt(2272, ergHVParam.paramPortal.unterlagenAngeboten, "unterlagenAngeboten", 1);

        //		setzeErgInt(2273, ergHVParam.paramPortal.onlineTeilnahmeAngeboten, "onlineTeilnahmeAngeboten", 1);
        //		setzeErgInt(2274, ergHVParam.paramPortal.onlineTeilnahmeAktiv, "onlineTeilnahmeAktiv", 1);
        setzeErgInt(2275, ergHVParam.paramPortal.nurRawLiveAbstimmung, "nurRawLiveAbstimmung", 1);
        setzeErgInt(2276, ergHVParam.paramPortal.testModus, "testModus", 1);
        setzeErgInt(2277, ergHVParam.paramPortal.websocketsMoeglich, "websocketsMoeglich", 1);
        setzeErgInt(2278, ergHVParam.paramPortal.doppelLoginGesperrt, "doppelLoginGesperrt", 1);

        setzeErgInt(2279, ergHVParam.paramPortal.onlineTeilnehmerAbfragen, "onlineTeilnehmerAbfragen", 1);

        setzeErgInt(2280, ergHVParam.paramPortal.wortmeldungMailBeiEingang, "wortmeldungMailBeiEingang", 1);
        setzeErgInt(2281, ergHVParam.paramPortal.wortmeldungStellerAbfragen, "wortmelderAbfragen", 1);
        setzeErgInt(2282, ergHVParam.paramPortal.wortmeldungNameAbfragen, "wortmelderEingeben", 1);
        setzeErgInt(2283, ergHVParam.paramPortal.wortmeldungKurztextAbfragen, "wortmeldungTOPEingeben", 1);
        setzeErgInt(2284, ergHVParam.paramPortal.wortmeldungLangtextAbfragen, "wortmeldungTextEingeben", 1);
        setzeErgInt(2285, ergHVParam.paramPortal.wortmeldungListeAnzeigen, "wortmeldeListeAnzeigen", 1);
        setzeErgInt(2286, ergHVParam.paramPortal.wortmeldungStellerZulaessig, "wortmelderZulaessig", 1);
        setzeErgInt(2287, ergHVParam.paramPortal.wortmeldungAnzahlJeAktionaer, "wortmeldungenAnzahlJeAktionaer", 1);
        setzeErgInt(2288, ergHVParam.paramPortal.wortmeldungLaenge, "wortmeldungenLaenge", 1);
        setzeErgInt(2289, ergHVParam.paramPortal.onlineTeilnahmeSeparateNutzungsbedingungen,
                "onlineTeilnahmeSeparateNutzungsbedingungen", 1);
        setzeErgInt(2290, ergHVParam.paramPortal.onlineTeilnahmeGastInSeparatemMenue,
                "onlineTeilnahmeGastInSeparatemMenue", 1);
        setzeErgInt(2291, ergHVParam.paramPortal.onlineTeilnahmeNurFuerFreiwilligAngemeldete,
                "onlineTeilnahmeNurFuerFreiwilligAngemeldete", 1);
        setzeErgInt(2292, ergHVParam.paramPortal.onlineTeilnahmeAktionaerAlsGast, "onlineTeilnahmeAktionaerAlsGast", 1);

        setzeErgInt(2293, ergHVParam.paramPortal.teilnehmerverzBeginnendBei, "teilnehmerverzBeginnendBei", 1);

        setzeErgInt(2294, ergHVParam.paramPortal.fragenNameAbfragen, "fragenNameAbfragen", 1);
        setzeErgInt(2295, ergHVParam.paramPortal.fragenKontaktdatenAbfragen, "fragenKontaktdatenAbfragen", 1);
        setzeErgInt(2296, ergHVParam.paramPortal.fragenKontaktdatenEMailVorschlagen, "fragenKontaktdatenEMailVorschlagen", 1);
        setzeErgInt(2297, ergHVParam.paramPortal.fragenKurztextAbfragen, "fragenKurztextAbfragen", 1);
        setzeErgInt(2298, ergHVParam.paramPortal.fragenTopListeAnbieten, "fragenTopListeAnbieten", 1);
        setzeErgInt(2299, ergHVParam.paramPortal.fragenLangtextAbfragen, "fragenLangtextAbfragen", 1);
        setzeErgInt(2300, ergHVParam.paramPortal.fragenZurueckziehenMoeglich, "fragenZurueckziehenMoeglich", 1);

        setzeErgInt(2301, ergHVParam.paramPortal.wortmeldungKontaktdatenAbfragen, "wortmeldungKontaktdatenAbfragen", 1);
        setzeErgInt(2302, ergHVParam.paramPortal.wortmeldungKontaktdatenEMailVorschlagen, "wortmeldungKontaktdatenEMailVorschlagen", 1);
        setzeErgInt(2303, ergHVParam.paramPortal.wortmeldungTopListeAnbieten, "wortmeldungTopListeAnbieten", 1);
        setzeErgInt(2304, ergHVParam.paramPortal.wortmeldungZurueckziehenMoeglich, "wortmeldungZurueckziehenMoeglich", 1);

        setzeErgInt(2305, ergHVParam.paramPortal.widerspruecheNameAbfragen, "widerspruecheNameAbfragen", 1);
        setzeErgInt(2306, ergHVParam.paramPortal.widerspruecheKontaktdatenAbfragen, "widerspruecheKontaktdatenAbfragen", 1);
        setzeErgInt(2307, ergHVParam.paramPortal.widerspruecheKontaktdatenEMailVorschlagen, "widerspruecheKontaktdatenEMailVorschlagen", 1);
        setzeErgInt(2308, ergHVParam.paramPortal.widerspruecheKurztextAbfragen, "widerspruecheKurztextAbfragen", 1);
        setzeErgInt(2309, ergHVParam.paramPortal.widerspruecheTopListeAnbieten, "widerspruecheTopListeAnbieten", 1);
        setzeErgInt(2310, ergHVParam.paramPortal.widerspruecheLangtextAbfragen, "widerspruecheLangtextAbfragen", 1);
        setzeErgInt(2311, ergHVParam.paramPortal.widerspruecheZurueckziehenMoeglich, "widerspruecheZurueckziehenMoeglich", 1);

        setzeErgInt(2312, ergHVParam.paramPortal.antraegeStellerAbfragen, "antraegeStellerAbfragen", 1);
        setzeErgInt(2313, ergHVParam.paramPortal.antraegeNameAbfragen, "antraegeNameAbfragen", 1);
        setzeErgInt(2314, ergHVParam.paramPortal.antraegeKontaktdatenAbfragen, "antraegeKontaktdatenAbfragen", 1);
        setzeErgInt(2315, ergHVParam.paramPortal.antraegeKontaktdatenEMailVorschlagen, "antraegeKontaktdatenEMailVorschlagen", 1);
        setzeErgInt(2316, ergHVParam.paramPortal.antraegeKurztextAbfragen, "antraegeKurztextAbfragen", 1);
        setzeErgInt(2317, ergHVParam.paramPortal.antraegeTopListeAnbieten, "antraegeTopListeAnbieten", 1);
        setzeErgInt(2318, ergHVParam.paramPortal.antraegeLangtextAbfragen, "antraegeLangtextAbfragen", 1);
        setzeErgInt(2319, ergHVParam.paramPortal.antraegeZurueckziehenMoeglich, "antraegeZurueckziehenMoeglich", 1);
        setzeErgInt(2320, ergHVParam.paramPortal.antraegeLaenge, "antraegeLaenge", 1);
        setzeErgInt(2321, ergHVParam.paramPortal.antraegeAnzahlJeAktionaer, "antraegeAnzahlJeAktionaer", 1);
        setzeErgInt(2322, ergHVParam.paramPortal.antraegeStellerZulaessig, "antraegeStellerZulaessig", 1);
        setzeErgInt(2323, ergHVParam.paramPortal.antraegeMailBeiEingang, "antraegeMailBeiEingang", 1);

        setzeErgInt(2324, ergHVParam.paramPortal.sonstMitteilungenStellerAbfragen, "sonstMitteilungenStellerAbfragen", 1);
        setzeErgInt(2325, ergHVParam.paramPortal.sonstMitteilungenNameAbfragen, "sonstMitteilungenNameAbfragen", 1);
        setzeErgInt(2326, ergHVParam.paramPortal.sonstMitteilungenKontaktdatenAbfragen, "sonstMitteilungenKontaktdatenAbfragen", 1);
        setzeErgInt(2327, ergHVParam.paramPortal.sonstMitteilungenKontaktdatenEMailVorschlagen, "sonstMitteilungenKontaktdatenEMailVorschlagen", 1);
        setzeErgInt(2328, ergHVParam.paramPortal.sonstMitteilungenKurztextAbfragen, "sonstMitteilungenKurztextAbfragen", 1);
        setzeErgInt(2329, ergHVParam.paramPortal.sonstMitteilungenTopListeAnbieten, "sonstMitteilungenTopListeAnbieten", 1);
        setzeErgInt(2330, ergHVParam.paramPortal.sonstMitteilungenLangtextAbfragen, "sonstMitteilungenLangtextAbfragen", 1);
        setzeErgInt(2331, ergHVParam.paramPortal.sonstMitteilungenZurueckziehenMoeglich, "sonstMitteilungenZurueckziehenMoeglich", 1);
        setzeErgInt(2332, ergHVParam.paramPortal.sonstMitteilungenLaenge, "sonstMitteilungenLaenge", 1);
        setzeErgInt(2333, ergHVParam.paramPortal.sonstMitteilungenAnzahlJeAktionaer, "sonstMitteilungenAnzahlJeAktionaer", 1);
        setzeErgInt(2334, ergHVParam.paramPortal.sonstMitteilungenStellerZulaessig, "sonstMitteilungenStellerZulaessig", 1);
        setzeErgInt(2335, ergHVParam.paramPortal.sonstMitteilungenMailBeiEingang, "sonstMitteilungenMailBeiEingang", 1);

        setzeErgInt(2336, ergHVParam.paramPortal.cookieHinweis, "cookieHinweis", 1);
        setzeErgInt(2337, ergHVParam.paramPortal.vollmachtDritteUndAndereWKMoeglich, "vollmachtDritteUndAndereWKMoeglich", 1);
        setzeErgInt(2338, ergHVParam.paramPortal.handhabungWeisungDurchVerschiedene, "handhabungWeisungDurchVerschiedene", 1);

        setzeErgString(2339, ergHVParam.paramPortal.farbeHeader, "farbeHeader", 1);
        setzeErgString(2340, ergHVParam.paramPortal.farbeButton, "farbeButton", 1);
        setzeErgString(2341, ergHVParam.paramPortal.farbeButtonHover, "farbeButtonHover", 1);
        setzeErgString(2342, ergHVParam.paramPortal.farbeButtonSchrift, "farbeButtonSchrift", 1);
        setzeErgString(2343, ergHVParam.paramPortal.farbeLink, "farbeLink", 1);
        setzeErgString(2344, ergHVParam.paramPortal.farbeLinkHover, "farbeLinkHover", 1);
        setzeErgString(2345, ergHVParam.paramPortal.farbeListeUngerade, "farbeListeUngerade", 1);
        setzeErgString(2346, ergHVParam.paramPortal.farbeListeGerade, "farbeListeGerade", 1);
        setzeErgString(2347, ergHVParam.paramPortal.farbeHintergrund, "farbeHintergrund", 1);
        setzeErgString(2348, ergHVParam.paramPortal.farbeText, "farbeText", 1);
        setzeErgString(2349, ergHVParam.paramPortal.farbeUeberschriftHintergrund, "farbeUeberschriftHintergrund", 1);
        setzeErgString(2350, ergHVParam.paramPortal.farbeUeberschrift, "farbeUeberschrift", 1);
        

        setzeErgInt(2354, ergHVParam.paramPortal.logoPosition, "logoPosition", 1);

        setzeErgString(2355, ergHVParam.paramPortal.farbeCookieHintHintergrund, "farbeCookieHintHintergrund", 1);
        setzeErgString(2356, ergHVParam.paramPortal.farbeCookieHintSchrift, "farbeCookieHintSchrift", 1);
        setzeErgString(2357, ergHVParam.paramPortal.farbeCookieHintButton, "farbeCookieHintButton", 1);
        setzeErgString(2358, ergHVParam.paramPortal.farbeCookieHintButtonSchrift, "farbeCookieHintButtonSchrift", 1);

        setzeErgString(2359, ergHVParam.paramPortal.schriftgroesseGlobal, "schriftgroesseGlobal", 1);
        setzeErgString(2360, ergHVParam.paramPortal.logoMindestbreite, "logoMindestbreite", 1);
        setzeErgString(2361, ergHVParam.paramPortal.farbeHintergrundBtn00, "farbeHintergrundBtn00", 1);
        setzeErgString(2362, ergHVParam.paramPortal.farbeSchriftBtn00, "farbeSchriftBtn00", 1);
        setzeErgString(2363, ergHVParam.paramPortal.farbeRahmenBtn00, "farbeRahmenBtn00", 1);
        setzeErgString(2364, ergHVParam.paramPortal.breiteRahmenBtn00, "breiteRahmenBtn00", 1);
        setzeErgString(2365, ergHVParam.paramPortal.radiusRahmenBtn00, "radiusRahmenBtn00", 1);
        setzeErgString(2366, ergHVParam.paramPortal.stilRahmenBtn00, "stilRahmenBtn00", 1);
        setzeErgString(2367, ergHVParam.paramPortal.farbeHintergrundBtn00Hover, "farbeHintergrundBtn00Hover", 1);
        setzeErgString(2368, ergHVParam.paramPortal.farbeSchriftBtn00Hover, "farbeSchriftBtn00Hover", 1);
        
        setzeErgString(2369, ergHVParam.paramPortal.farbeRahmenBtn00Hover, "farbeRahmenBtn00Hover", 1);
        setzeErgString(2370, ergHVParam.paramPortal.breiteRahmenBtn00Hover, "breiteRahmenBtn00Hover", 1);
        setzeErgString(2371, ergHVParam.paramPortal.radiusRahmenBtn00Hover, "radiusRahmenBtn00Hover", 1);
        setzeErgString(2372, ergHVParam.paramPortal.stilRahmenBtn00Hover, "stilRahmenBtn00Hover", 1);
        setzeErgString(2373, ergHVParam.paramPortal.farbeFocus, "farbeFocus", 1);
        setzeErgString(2374, ergHVParam.paramPortal.farbeError, "farbeError", 1);
        setzeErgString(2375, ergHVParam.paramPortal.farbeErrorSchrift, "farbeErrorSchrift", 1);
        setzeErgString(2376, ergHVParam.paramPortal.farbeWarning, "farbeWarning", 1);
        setzeErgString(2377, ergHVParam.paramPortal.farbeWarningSchrift, "farbeWarningSchrift", 1);
        setzeErgString(2378, ergHVParam.paramPortal.farbeSuccess, "farbeSuccess", 1);
        
        setzeErgString(2379, ergHVParam.paramPortal.farbeSuccessSchrift, "farbeSuccessSchrift", 1);
        setzeErgString(2380, ergHVParam.paramPortal.farbeRahmenEingabefelder, "farbeRahmenEingabefelder", 1);
        setzeErgString(2381, ergHVParam.paramPortal.breiteRahmenEingabefelder, "breiteRahmenEingabefelder", 1);
        setzeErgString(2382, ergHVParam.paramPortal.radiusRahmenEingabefelder, "radiusRahmenEingabefelder", 1);
        setzeErgString(2383, ergHVParam.paramPortal.stilRahmenEingabefelder, "stilRahmenEingabefelder", 1);
        setzeErgString(2384, ergHVParam.paramPortal.farbeHintergrundLogoutBtn, "farbeHintergrundLogoutBtn", 1);
        setzeErgString(2385, ergHVParam.paramPortal.farbeSchriftLogoutBtn, "farbeSchriftLogoutBtn", 1);
        setzeErgString(2386, ergHVParam.paramPortal.farbeRahmenLogoutBtn, "farbeRahmenLogoutBtn", 1);
        setzeErgString(2387, ergHVParam.paramPortal.breiteRahmenLogoutBtn, "breiteRahmenLogoutBtn", 1);
        setzeErgString(2388, ergHVParam.paramPortal.radiusRahmenLogoutBtn, "radiusRahmenLogoutBtn", 1);
        
        setzeErgString(2389, ergHVParam.paramPortal.stilRahmenLogoutBtn, "stilRahmenLogoutBtn", 1);
        setzeErgString(2390, ergHVParam.paramPortal.farbeHintergrundLogoutBtnHover, "farbeHintergrundLogoutBtnHover", 1);
        setzeErgString(2391, ergHVParam.paramPortal.farbeSchriftLogoutBtnHover, "farbeSchriftLogoutBtnHover", 1);
        setzeErgString(2392, ergHVParam.paramPortal.farbeRahmenLogoutBtnHover, "farbeRahmenLogoutBtnHover", 1);
        setzeErgString(2393, ergHVParam.paramPortal.breiteRahmenLogoutBtnHover, "breiteRahmenLogoutBtnHover", 1);
        setzeErgString(2394, ergHVParam.paramPortal.radiusRahmenLogoutBtnHover, "radiusRahmenLogoutBtnHover", 1);
        setzeErgString(2395, ergHVParam.paramPortal.stilRahmenLogoutBtnHover, "stilRahmenLogoutBtnHover", 1);
        setzeErgString(2396, ergHVParam.paramPortal.farbeHintergrundLoginBtn, "farbeHintergrundLoginBtn", 1);
        setzeErgString(2397, ergHVParam.paramPortal.farbeSchriftLoginBtn, "farbeSchriftLoginBtn", 1);
        setzeErgString(2398, ergHVParam.paramPortal.farbeRahmenLoginBtn, "farbeRahmenLoginBtn", 1);
        
        setzeErgString(2399, ergHVParam.paramPortal.breiteRahmenLoginBtn, "breiteRahmenLoginBtn", 1);
        setzeErgString(2400, ergHVParam.paramPortal.radiusRahmenLoginBtn, "radiusRahmenLoginBtn", 1);
        setzeErgString(2401, ergHVParam.paramPortal.stilRahmenLoginBtn, "stilRahmenLoginBtn", 1);
        setzeErgString(2402, ergHVParam.paramPortal.farbeHintergrundLoginBtnHover, "farbeHintergrundLoginBtnHover", 1);
        setzeErgString(2403, ergHVParam.paramPortal.farbeSchriftLoginBtnHover, "farbeSchriftLoginBtnHover", 1);
        setzeErgString(2404, ergHVParam.paramPortal.farbeRahmenLoginBtnHover, "farbeRahmenLoginBtnHover", 1);
        setzeErgString(2405, ergHVParam.paramPortal.breiteRahmenLoginBtnHover, "breiteRahmenLoginBtnHover", 1);
        setzeErgString(2406, ergHVParam.paramPortal.radiusRahmenLoginBtnHover, "radiusRahmenLoginBtnHover", 1);
        setzeErgString(2407, ergHVParam.paramPortal.stilRahmenLoginBtnHover, "stilRahmenLoginBtnHover", 1);
        setzeErgString(2408, ergHVParam.paramPortal.farbeRahmenLoginBereich, "farbeRahmenLoginBereich", 1);
        
        setzeErgString(2409, ergHVParam.paramPortal.breiteRahmenLoginBereich, "breiteRahmenLoginBereich", 1);
        setzeErgString(2410, ergHVParam.paramPortal.radiusRahmenLoginBereich, "radiusRahmenLoginBereich", 1);
        setzeErgString(2411, ergHVParam.paramPortal.stilRahmenLoginBereich, "stilRahmenLoginBereich", 1);
        setzeErgString(2412, ergHVParam.paramPortal.farbeHintergrundLoginBereich, "farbeHintergrundLoginBereich", 1);
        setzeErgString(2413, ergHVParam.paramPortal.farbeLinkLoginBereich, "farbeLinkLoginBereich", 1);
        setzeErgString(2414, ergHVParam.paramPortal.farbeLinkHoverLoginBereich, "farbeLinkHoverLoginBereich", 1);
        setzeErgString(2415, ergHVParam.paramPortal.farbeRahmenEingabefelderLoginBereich, "farbeRahmenEingabefelderLoginBereich", 1);
        setzeErgString(2416, ergHVParam.paramPortal.breiteRahmenEingabefelderLoginBereich, "breiteRahmenEingabefelderLoginBereich", 1);
        setzeErgString(2417, ergHVParam.paramPortal.radiusRahmenEingabefelderLoginBereich, "radiusRahmenEingabefelderLoginBereich", 1);
        setzeErgString(2418, ergHVParam.paramPortal.stilRahmenEingabefelderLoginBereich, "stilRahmenEingabefelderLoginBereich", 1);
        
        setzeErgString(2419, ergHVParam.paramPortal.farbeBestandsbereichUngeradeReihe, "farbeBestandsbereichUngeradeReihe", 1);
        setzeErgString(2420, ergHVParam.paramPortal.farbeBestandsbereichGeradeReihe, "farbeBestandsbereichGeradeReihe", 1);
        setzeErgString(2421, ergHVParam.paramPortal.farbeLineUntenBestandsbereich, "farbeLineUntenBestandsbereich", 1);
        setzeErgString(2422, ergHVParam.paramPortal.breiteLineUntenBestandsbereich, "breiteLineUntenBestandsbereich", 1);
        setzeErgString(2423, ergHVParam.paramPortal.stilLineUntenBestandsbereich, "stilLineUntenBestandsbereich", 1);
        setzeErgString(2424, ergHVParam.paramPortal.farbeRahmenAnmeldeuebersicht, "farbeRahmenAnmeldeuebersicht", 1);
        setzeErgString(2425, ergHVParam.paramPortal.breiteRahmenAnmeldeuebersicht, "breiteRahmenAnmeldeuebersicht", 1);
        setzeErgString(2426, ergHVParam.paramPortal.radiusRahmenAnmeldeuebersicht, "radiusRahmenAnmeldeuebersicht", 1);
        setzeErgString(2427, ergHVParam.paramPortal.stilRahmenAnmeldeuebersicht, "stilRahmenAnmeldeuebersicht", 1);
        setzeErgString(2428, ergHVParam.paramPortal.farbeTrennlinieAnmeldeuebersicht, "farbeTrennlinieAnmeldeuebersicht", 1);
        
        setzeErgString(2429, ergHVParam.paramPortal.breiteTrennlinieAnmeldeuebersicht, "breiteTrennlinieAnmeldeuebersicht", 1);
        setzeErgString(2430, ergHVParam.paramPortal.stilTrennlinieAnmeldeuebersicht, "stilTrennlinieAnmeldeuebersicht", 1);
        setzeErgString(2431, ergHVParam.paramPortal.farbeRahmenErteilteWillenserklärungen, "farbeRahmenErteilteWillenserklärungen", 1);
        setzeErgString(2432, ergHVParam.paramPortal.breiteRahmenErteilteWillenserklärungen, "breiteRahmenErteilteWillenserklärungen", 1);
        setzeErgString(2433, ergHVParam.paramPortal.radiusRahmenErteilteWillenserklärungen, "radiusRahmenErteilteWillenserklärungen", 1);
        setzeErgString(2434, ergHVParam.paramPortal.stilRahmenErteilteWillenserklärungen, "stilRahmenErteilteWillenserklärungen", 1);
        setzeErgString(2435, ergHVParam.paramPortal.farbeHintergrundErteilteWillenserklärungen, "farbeHintergrundErteilteWillenserklärungen", 1);
        setzeErgString(2436, ergHVParam.paramPortal.farbeSchriftErteilteWillenserklärungen, "farbeSchriftErteilteWillenserklärungen", 1);
        setzeErgString(2437, ergHVParam.paramPortal.farbeRahmenAbstimmungstabelle, "farbeRahmenAbstimmungstabelle", 1);
        setzeErgString(2438, ergHVParam.paramPortal.breiteRahmenAbstimmungstabelle, "breiteRahmenAbstimmungstabelle", 1);
        
        setzeErgString(2439, ergHVParam.paramPortal.radiusRahmenAbstimmungstabelle, "radiusRahmenAbstimmungstabelle", 1);
        setzeErgString(2440, ergHVParam.paramPortal.stilRahmenAbstimmungstabelle, "stilRahmenAbstimmungstabelle", 1);
        setzeErgString(2441, ergHVParam.paramPortal.farbeHintergrundAbstimmungstabelleUngeradeReihen, "farbeHintergrundAbstimmungstabelleUngeradeReihen", 1);
        setzeErgString(2442, ergHVParam.paramPortal.farbeSchriftAbstimmungstabelleUngeradeReihen, "farbeSchriftAbstimmungstabelleUngeradeReihen", 1);
        setzeErgString(2443, ergHVParam.paramPortal.farbeHintergrundAbstimmungstabelleGeradeReihen, "farbeHintergrundAbstimmungstabelleGeradeReihen", 1);
        setzeErgString(2444, ergHVParam.paramPortal.farbeSchriftAbstimmungstabelleGeradeReihen, "farbeSchriftAbstimmungstabelleGeradeReihen", 1);
        setzeErgString(2445, ergHVParam.paramPortal.farbeHintergrundWeisungJa, "farbeHintergrundWeisungJa", 1);
        setzeErgString(2446, ergHVParam.paramPortal.farbeSchriftWeisungJa, "farbeSchriftWeisungJa", 1);
        setzeErgString(2447, ergHVParam.paramPortal.farbeRahmenWeisungJa, "farbeRahmenWeisungJa", 1);
        setzeErgString(2448, ergHVParam.paramPortal.farbeHintergrundWeisungJaChecked, "farbeHintergrundWeisungJaChecked", 1);
        
        setzeErgString(2449, ergHVParam.paramPortal.farbeSchriftWeisungJaChecked, "farbeSchriftWeisungJaChecked", 1);
        setzeErgString(2450, ergHVParam.paramPortal.farbeRahmenWeisungJaChecked, "farbeRahmenWeisungJaChecked", 1);
        setzeErgString(2451, ergHVParam.paramPortal.farbeHintergrundWeisungNein, "farbeHintergrundWeisungNein", 1);
        setzeErgString(2452, ergHVParam.paramPortal.farbeSchriftWeisungNein, "farbeSchriftWeisungNein", 1);
        setzeErgString(2453, ergHVParam.paramPortal.farbeRahmenWeisungNein, "farbeRahmenWeisungNein", 1);
        setzeErgString(2454, ergHVParam.paramPortal.farbeHintergrundWeisungNeinChecked, "farbeHintergrundWeisungNeinChecked", 1);
        setzeErgString(2455, ergHVParam.paramPortal.farbeSchriftWeisungNeinChecked, "farbeSchriftWeisungNeinChecked", 1);
        setzeErgString(2456, ergHVParam.paramPortal.farbeRahmenWeisungNeinChecked, "farbeRahmenWeisungNeinChecked", 1);
        setzeErgString(2457, ergHVParam.paramPortal.farbeHintergrundWeisungEnthaltung, "farbeHintergrundWeisungEnthaltung", 1);
        setzeErgString(2458, ergHVParam.paramPortal.farbeSchriftWeisungEnthaltung, "farbeSchriftWeisungEnthaltung", 1);
        
        setzeErgString(2459, ergHVParam.paramPortal.farbeRahmenWeisungEnthaltung, "farbeRahmenWeisungEnthaltung", 1);
        setzeErgString(2460, ergHVParam.paramPortal.farbeHintergrundWeisungEnthaltungChecked, "farbeHintergrundWeisungEnthaltungChecked", 1);
        setzeErgString(2461, ergHVParam.paramPortal.farbeSchriftWeisungEnthaltungChecked, "farbeSchriftWeisungEnthaltungChecked", 1);
        setzeErgString(2462, ergHVParam.paramPortal.farbeRahmenWeisungEnthaltungChecked, "farbeRahmenWeisungEnthaltungChecked", 1);
        setzeErgString(2463, ergHVParam.paramPortal.farbeHintergrundFooterTop, "farbeHintergrundFooterTop", 1);
        setzeErgString(2464, ergHVParam.paramPortal.farbeSchriftFooterTop, "farbeSchriftFooterTop", 1);
        setzeErgString(2465, ergHVParam.paramPortal.farbeLinkFooterTop, "farbeLinkFooterTop", 1);
        setzeErgString(2466, ergHVParam.paramPortal.farbeLinkFooterTopHover, "farbeLinkFooterTopHover", 1);
        setzeErgString(2467, ergHVParam.paramPortal.farbeHintergrundFooterBottom, "farbeHintergrundFooterBottom", 1);
        setzeErgString(2468, ergHVParam.paramPortal.farbeSchriftFooterBottom, "farbeSchriftFooterBottom", 1);
        
        setzeErgString(2469, ergHVParam.paramPortal.farbeLinkFooterBottom, "farbeLinkFooterBottom", 1);
        setzeErgString(2470, ergHVParam.paramPortal.farbeLinkFooterBottomHover, "farbeLinkFooterBottomHover", 1);
        setzeErgString(2471, ergHVParam.paramPortal.farbeHintergrundModal, "farbeHintergrundModal", 1);
        setzeErgString(2472, ergHVParam.paramPortal.farbeSchriftModal, "farbeSchriftModal", 1);
        setzeErgString(2473, ergHVParam.paramPortal.farbeHintergrundModalHeader, "farbeHintergrundModalHeader", 1);
        setzeErgString(2474, ergHVParam.paramPortal.farbeSchriftModalHeader, "farbeSchriftModalHeader", 1);
        setzeErgString(2475, ergHVParam.paramPortal.farbeTrennlinieModal, "farbeTrennlinieModal", 1);
        setzeErgString(2476, ergHVParam.paramPortal.farbeHintergrundUntenButtons, "farbeHintergrundUntenButtons", 1);
        setzeErgString(2477, ergHVParam.paramPortal.farbeSchriftUntenButtons, "farbeSchriftUntenButtons", 1);
        setzeErgString(2478, ergHVParam.paramPortal.farbeRahmenUntenButtons, "farbeRahmenUntenButtons", 1);
        
        setzeErgString(2479, ergHVParam.paramPortal.breiteRahmenUntenButtons, "breiteRahmenUntenButtons", 1);
        setzeErgString(2480, ergHVParam.paramPortal.radiusRahmenUntenButtons, "radiusRahmenUntenButtons", 1);
        setzeErgString(2481, ergHVParam.paramPortal.stilRahmenUntenButtons, "stilRahmenUntenButtons", 1);
        setzeErgString(2482, ergHVParam.paramPortal.farbeHintergrundUntenButtonsHover, "farbeHintergrundUntenButtonsHover", 1);
        setzeErgString(2483, ergHVParam.paramPortal.farbeSchriftUntenButtonsHover, "farbeSchriftUntenButtonsHover", 1);
        setzeErgString(2484, ergHVParam.paramPortal.farbeRahmenUntenButtonsHover, "farbeRahmenUntenButtonsHover", 1);
        setzeErgString(2485, ergHVParam.paramPortal.breiteRahmenUntenButtonsHover, "breiteRahmenUntenButtonsHover", 1);
        setzeErgString(2486, ergHVParam.paramPortal.radiusRahmenUntenButtonsHover, "radiusRahmenUntenButtonsHover", 1);
        setzeErgString(2487, ergHVParam.paramPortal.stilRahmenUntenButtonsHover, "stilRahmenUntenButtonsHover", 1);
        
        setzeErgInt(2488, ergHVParam.paramPortal.bestaetigungBeiWeisungMitTOP, "bestaetigungBeiWeisungMitTOP", 1);
        setzeErgInt(2489, ergHVParam.paramPortal.anzeigeStimmenKennung, "anzeigeStimmenKennung", 1);
        setzeErgInt(2490, ergHVParam.paramPortal.weisungenAktuellNichtMoeglich, "weisungenAktuellNichtMoeglich", 1);
        setzeErgInt(2491, ergHVParam.paramPortal.sammelkartenFuerAenderungSperren, "sammelkartenFuerAenderungSperren", 1);

        setzeErgInt(2492, ergHVParam.paramPortal.erklAnPos1, "erklAnPos1", 1);
        setzeErgInt(2493, ergHVParam.paramPortal.erklAnPos2, "erklAnPos2", 1);
        setzeErgInt(2494, ergHVParam.paramPortal.erklAnPos3, "erklAnPos3", 1);
        setzeErgInt(2495, ergHVParam.paramPortal.erklAnPos4, "erklAnPos4", 1);
        setzeErgInt(2496, ergHVParam.paramPortal.erklAnPos5, "erklAnPos5", 1);

        /*reserviert bis 2512 - 20 Positionen*/
        setzeErgInt(2513, ergHVParam.paramPortal.kontaktformularAktiv, "kontaktformularAktiv", 1);
        setzeErgInt(2514, ergHVParam.paramPortal.kontaktformularBeiEingangMail, "kontaktformularBeiEingangMail", 1);
        setzeErgInt(2515, ergHVParam.paramPortal.kontaktformularBeiEingangMailInhaltAufnehmen, "kontaktformularBeiEingangMailInhaltAufnehmen", 1);
        setzeErgInt(2516, ergHVParam.paramPortal.kontaktformularBeiEingangAufgabe, "kontaktformularBeiEingangAufgabe", 1);
        setzeErgInt(2517, ergHVParam.paramPortal.kontaktformularAnzahlKontaktfelder, "kontaktformularAnzahlKontaktfelder", 1);
        setzeErgInt(2518, ergHVParam.paramPortal.kontaktformularTelefonKontaktAbfragen, "kontaktformularTelefonKontaktAbfragen", 1);
        setzeErgInt(2519, ergHVParam.paramPortal.kontaktformularMailKontaktAbfragen, "kontaktformularMailKontaktAbfragen", 1);
        setzeErgInt(2520, ergHVParam.paramPortal.kontaktformularThemaAnbieten, "kontaktformularThemaAnbieten", 1);
        setzeErgInt(2521, ergHVParam.paramPortal.kontaktSonstigeMoeglichkeitenAnbieten, "kontaktSonstigeMoeglichkeitenAnbieten", 1);
        setzeErgInt(2522, ergHVParam.paramPortal.kontaktSonstigeMoeglichkeitenObenOderUnten, "kontaktSonstigeMoeglichkeitenObenOderUnten", 1);

        setzeErgInt(2523, ergHVParam.paramPortal.registerAnbindung, "registerAnbindung", 1);
        setzeErgInt(2524, ergHVParam.paramPortal.registerAnbindungOberflaeche, "registerAnbindungOberflaeche", 1);
        setzeErgInt(2525, ergHVParam.paramPortal.registerAnbindungCheckBeiLogin, "registerAnbindungCheckBeiLogin", 1);

        setzeErgInt(2526, ergHVParam.paramPortal.wortmeldungVLListeAnzeigen, "wortmeldeVLListeAnzeigen", 1);

        setzeErgInt(2527, ergHVParam.paramPortal.verfahrenPasswortVergessenBeiEmailHinterlegtAuchPost, "verfahrenPasswortVergessenBeiEmailHinterlegtAuchPost", 1);
        setzeErgInt(2528, ergHVParam.paramPortal.linkEmailEnthaeltLinkOderCode, "linkEmailEnthaeltLinkOderCode", 1);
        setzeErgInt(2529, ergHVParam.paramPortal.bestaetigenHinweisWeitere, "bestaetigenHinweisWeitere", 1);
        setzeErgInt(2530, ergHVParam.paramPortal.kontaktformularThemenListeGlobalLokal, "kontaktformularThemenListeGlobalLokal", 1);
 
        setzeErgInt(2531, ergHVParam.paramPortal.api_jwt_expiration_time, "api_jwt_expiration_time", 1);
        setzeErgString(2532, ergHVParam.paramPortal.api_key_name, "api_jwt_expiration_time", 1);

        setzeErgInt(2533, ergHVParam.paramPortal.freiwillingAnmeldenPraesenzOderOnline, "freiwillingAnmeldenPraesenzOderOnline", 1);

        setzeErgInt(2534, ergHVParam.paramPortal.veranstaltungMailVerschicken, "veranstaltungMailVerschicken", 1);
        setzeErgInt(2535, ergHVParam.paramPortal.veranstaltungPersonenzahlEingeben, "veranstaltungPersonenzahlEingeben", 1);
        setzeErgInt(2536, ergHVParam.paramPortal.veranstaltungMehrfachAuswaehlbarJeGruppe, "veranstaltungMehrfachAuswaehlbarJeGruppe", 1);
 
        setzeErgInt(2537, ergHVParam.paramPortal.botschaftenStellerAbfragen, "botschaftenStellerAbfragen", 1);
        setzeErgInt(2538, ergHVParam.paramPortal.botschaftenNameAbfragen, "botschaftenNameAbfragen", 1);
        setzeErgInt(2539, ergHVParam.paramPortal.botschaftenKontaktdatenAbfragen, "botschaftenKontaktdatenAbfragen", 1);
        setzeErgInt(2540, ergHVParam.paramPortal.botschaftenKontaktdatenEMailVorschlagen, "botschaftenKontaktdatenEMailVorschlagen", 1);
        setzeErgInt(2541, ergHVParam.paramPortal.botschaftenKurztextAbfragen, "botschaftenKurztextAbfragen", 1);
        setzeErgInt(2542, ergHVParam.paramPortal.botschaftenTopListeAnbieten, "botschaftenTopListeAnbieten", 1);
        setzeErgInt(2543, ergHVParam.paramPortal.botschaftenLangtextAbfragen, "botschaftenLangtextAbfragen", 1);
        setzeErgInt(2544, ergHVParam.paramPortal.botschaftenZurueckziehenMoeglich, "botschaftenZurueckziehenMoeglich", 1);
        setzeErgInt(2545, ergHVParam.paramPortal.botschaftenAnzahlJeAktionaer, "botschaftenAnzahlJeAktionaer", 1);
        setzeErgInt(2546, ergHVParam.paramPortal.botschaftenStellerZulaessig, "botschaftenStellerZulaessig", 1);
        setzeErgInt(2547, ergHVParam.paramPortal.botschaftenMailBeiEingang, "botschaftenMailBeiEingang", 1);
        
        setzeErgInt(2548, ergHVParam.paramPortal.botschaftenVideo, "botschaftenVideo", 1);
        hString="-";
        for (int i1=1;i1<=15;i1++) {
            hString+=Integer.toString(ergHVParam.paramPortal.botschaftenVideoFormate[i1]);
        }
        setzeErgString(2549, hString, "botschaftenVideoFormate", 1);

//        setzeErgInt(2550, ergHVParam.paramPortal.botschaftenText, "botschaftenText", 1);
        hString="-";
        for (int i1=1;i1<=15;i1++) {
            hString+=Integer.toString(ergHVParam.paramPortal.botschaftenTextFormate[i1]);
        }
        setzeErgString(2551, hString, "botschaftenTextFormate", 1);

        setzeErgInt(2552, ergHVParam.paramPortal.wortmeldungArt, "wortmeldungArt", 1);

        setzeErgInt(2553, ergHVParam.paramPortal.botschaftenVideoLaenge, "botschaftenVideoLaenge", 1);
        setzeErgInt(2554, ergHVParam.paramPortal.botschaftenLaenge, "botschaftenLaenge", 1);

        setzeErgInt(2555, ergHVParam.paramPortal.fragenHinweisGelesen, "fragenHinweisGelesen", 1);
        setzeErgInt(2556, ergHVParam.paramPortal.wortmeldungHinweisGelesen, "wortmeldungHinweisGelesen", 1);
        setzeErgInt(2557, ergHVParam.paramPortal.widerspruecheHinweisGelesen, "widerspruecheHinweisGelesen", 1);
        setzeErgInt(2558, ergHVParam.paramPortal.antraegeHinweisGelesen, "antraegeHinweisGelesen", 1);
        setzeErgInt(2559, ergHVParam.paramPortal.sonstMitteilungenHinweisGelesen, "sonstMitteilungenHinweisGelesen", 1);
        setzeErgInt(2560, ergHVParam.paramPortal.botschaftenHinweisGelesen, "botschaftenHinweisGelesen", 1);

        setzeErgInt(2561, ergHVParam.paramPortal.botschaftenVoranmeldungErforderlich, "botschaftenVoranmeldungErforderlich", 1);
        setzeErgInt(2562, ergHVParam.paramPortal.botschaftenTextDatei, "botschaftenTextDatei", 1);
        setzeErgInt(2563, ergHVParam.paramPortal.botschaftenTextDateiLaenge, "botschaftenTextDateiLaenge", 1);

        setzeErgInt(2564, ergHVParam.paramPortal.fragenRueckfragenErmoeglichen, "fragenRueckfragenErmoeglichen", 1);

        setzeErgInt(2565, ergHVParam.paramPortal.fragenHinweisVorbelegenMit, "fragenHinweisVorbelegenMit", 1);
        setzeErgInt(2566, ergHVParam.paramPortal.wortmeldungHinweisVorbelegenMit, "wortmeldungHinweisVorbelegenMit", 1);
        setzeErgInt(2567, ergHVParam.paramPortal.widerspruecheHinweisVorbelegenMit, "widerspruecheHinweisVorbelegenMit", 1);
        setzeErgInt(2568, ergHVParam.paramPortal.antraegeHinweisVorbelegenMit, "antraegeHinweisVorbelegenMit", 1);
        setzeErgInt(2569, ergHVParam.paramPortal.sonstMitteilungenHinweisVorbelegenMit, "sonstMitteilungenHinweisVorbelegenMit", 1);
        setzeErgInt(2570, ergHVParam.paramPortal.botschaftenHinweisVorbelegenMit, "botschaftenHinweisVorbelegenMit", 1);

        setzeErgString(2571, ergHVParam.paramPortal.shrinkFormat, "shrinkFormat", 1);
        setzeErgString(2572, ergHVParam.paramPortal.shrinkAufloesung, "shrinkAufloesung", 1);

        setzeErgInt(2573, ergHVParam.paramPortal.fragezeichenHinweiseVerwenden, "fragezeichenHinweiseVerwenden", 1);

        setzeErgInt(2574, ergHVParam.paramPortal.emailBestaetigenIstZwingend, "emailBestaetigenIstZwingend", 1);

        setzeErgInt(2575, ergHVParam.paramPortal.botschaftenLangtextUndDateiNurAlternativ, "botschaftenLangtextUndDateiNurAlternativ", 1);

        setzeErgInt(2576, ergHVParam.paramPortal.dialogveranstaltungAktiv, "dialogveranstaltungAktiv", 1);
        setzeErgString(2577, ergHVParam.paramPortal.farbeLadebalkenUploadLeer, "farbeLadebalkenUploadLeer", 1);
        setzeErgString(2578, ergHVParam.paramPortal.farbeLadebalkenUploadFull, "farbeLadebalkenUploadFull", 1);

        setzeErgInt(2579, ergHVParam.paramPortal.inHVPortalKeineEinstellungenFuerAktionaere, "inHVPortalKeineEinstellungenFuerAktionaere", 1);
        setzeErgInt(2580, ergHVParam.paramPortal.inHVPortalKeineEmailUndKeinPasswortFuerAktionaere, "inHVPortalKeineEmailUndKeinPasswortFuerAktionaere", 1);
        setzeErgInt(2581, ergHVParam.paramPortal.vollmachtsnachweisAufStartseiteAktiv, "vollmachtsnachweisAufStartseiteAktiv", 1);
        setzeErgInt(2582, ergHVParam.paramPortal.freiwilligeAnmeldungEKDruckMoeglich, "freiwilligeAnmeldungEKDruckMoeglich", 1);
        setzeErgInt(2583, ergHVParam.paramPortal.registerAnbindungZurHV, "registerAnbindungZurHV", 1);
        setzeErgInt(2584, ergHVParam.paramPortal.registerAnbindungVonHV, "registerAnbindungVonHV", 1);
        setzeErgInt(2585, ergHVParam.paramPortal.mailEingabeServiceline, "mailEingabeServiceline", 1);

        setzeErgString(2586, ergHVParam.paramBasis.profileKlasse, "profileKlasse", 1);
        setzeErgInt(2587, ergHVParam.paramPortal.freiwilligeAnmeldungNurPapier, "freiwilligeAnmeldungNurPapier", 1);
        setzeErgInt(2588, ergHVParam.paramPortal.onlineAbstimmungBerechtigungSeparatPruefen, "onlineAbstimmungBerechtigungSeparatPruefen", 1);
        setzeErgInt(2589, ergHVParam.paramPortal.hybridTeilnahmeAktiv, "hybridTeilnahmeAktiv", 1);
        
        setzeErgInt(2590, ergHVParam.paramPortal.freiwilligeAnmeldungMitVertretereingabe, "freiwilligeAnmeldungMitVertretereingabe", 1);
        setzeErgInt(2591, ergHVParam.paramPortal.freiwilligeAnmeldungZweiPersonenFuerMinderjaehrigeMoeglich, "freiwilligeAnmeldungZweiPersonenFuerMinderjaehrigeMoeglich", 1);

        setzeErgInt(2592, ergHVParam.paramAkkreditierung.beiZugangSelbstVertreterIgnorierenZulaessig, "beiZugangSelbstVertreterIgnorioerenZulaessig", 1);
        setzeErgInt(2593, ergHVParam.paramAkkreditierung.ungepruefteKartenNichtBuchen, "ungepruefteKartenNichtBuchen", 1);
        setzeErgInt(2594, ergHVParam.paramAkkreditierung.auszugebendeStimmkartenInMeldungAnzeigen, "auszugebendeStimmkartenInMeldungAnzeigen", 1);

        setzeErgInt(2595, ergHVParam.paramPortal.fragenKontaktdatenTelefonAbfragen, "fragenKontaktdatenTelefonAbfragen", 1);
        setzeErgInt(2596, ergHVParam.paramPortal.wortmeldungKontaktdatenTelefonAbfragen, "wortmeldungKontaktdatenTelefonAbfragen", 1);
        setzeErgInt(2597, ergHVParam.paramPortal.widerspruecheKontaktdatenTelefonAbfragen, "widerspruecheKontaktdatenTelefonAbfragen", 1);
        setzeErgInt(2598, ergHVParam.paramPortal.sonstMitteilungenKontaktdatenTelefonAbfragen, "sonstMitteilungenKontaktdatenTelefonAbfragen", 1);
        setzeErgInt(2599, ergHVParam.paramPortal.botschaftenKontaktdatenTelefonAbfragen, "botschaftenKontaktdatenTelefonAbfragen", 1);

        for (int i1=2600;i1<=2609;i1++) {
            hString="";
            for (int i2=0;i2<6;i2++) {
                if (ergHVParam.paramPortal.inhaltsHinweiseAktiv[i1-2600][i2]) {
                    hString+="1";
                }
                else {
                    hString+="0";
                }
            }
            setzeErgString(i1,hString, "inhaltsHinweiseAktiv "+Integer.toString(i1-2600), 1);
        }
        
        setzeErgInt(2610, ergHVParam.paramPortal.wortmeldungTestDurchfuehren, "wortmeldungTestDurchfuehren", 1);
        setzeErgInt(2611, ergHVParam.paramPortal.wortmeldungRedeAufrufZweitenVersuchDurchfuehren, "wortmeldungRedeAufrufZweitenVersuchDurchfuehren", 1);
        setzeErgInt(2612, ergHVParam.paramPortal.konfRaumAnzahl[0], "konfRaumAnzahl[0]", 1);
        setzeErgInt(2613, ergHVParam.paramPortal.konfRaumAnzahl[1], "konfRaumAnzahl[1]", 1);
        setzeErgInt(2614, ergHVParam.paramPortal.konfGlobaleTestraeumeNutzen, "konfGlobaleTestraeumeNutzen", 1);
        setzeErgInt(2615, ergHVParam.paramPortal.wortmeldungNachTestManuellInRednerlisteAufnehmen, "wortmeldungNachTestManuellInRednerlisteAufnehmen", 1);
        setzeErgInt(2616, ergHVParam.paramPortal.zuschaltungHVAutomatischNachLogin, "zuschaltungHVAutomatischNachLogin", 1);
      
        hString="";
        for (int i2=0;i2<5;i2++) {
            if (ergHVParam.paramPortal.veranstaltungenAktivFuerGattung[i2]) {
                hString+="1";
            }
            else {
                hString+="0";
            }
        }
        setzeErgString(2617,hString, "veranstaltungenAktivFuerGattung ", 1);
        
        hString="";
        for (int i2=0;i2<5;i2++) {
            if (ergHVParam.paramPortal.freiwilligeAnmeldungAktivFuerGattung[i2]) {
                hString+="1";
            }
            else {
                hString+="0";
            }
        }
        setzeErgString(2618,hString, "freiwilligeAnmeldungAktivFuerGattung ", 1);
        setzeErgInt(2619, ergHVParam.paramPortal.wortmeldetischSetNr, "wortmeldetischSetNr", 1);
        setzeErgInt(2620, ergHVParam.paramPortal.konfBackupAktiv, "konfBackupAktiv", 1);
 
        setzeErgString(2621,ergHVParam.paramPortal.konfRaumBIdPW[0][0], "konfRaumBIdPW[0][0] ", 1);
        setzeErgString(2622,ergHVParam.paramPortal.konfRaumBIdPW[0][1], "konfRaumBIdPW[0][1] ", 1);
        setzeErgString(2623,ergHVParam.paramPortal.konfRaumBIdPW[0][2], "konfRaumBIdPW[0][2] ", 1);

        setzeErgString(2624,ergHVParam.paramPortal.konfRaumBIdPW[1][0], "konfRaumBIdPW[1][0] ", 1);
        setzeErgString(2625,ergHVParam.paramPortal.konfRaumBIdPW[1][1], "konfRaumBIdPW[1][1] ", 1);
        setzeErgString(2626,ergHVParam.paramPortal.konfRaumBIdPW[1][2], "konfRaumBIdPW[1][2] ", 1);
        setzeErgInt(2627,ergHVParam.paramPortal.wortmeldungInhaltsHinweiseAktiv, "wortmeldungInhaltsHinweiseAktiv", 1);
        setzeErgString(2628,ergHVParam.paramPortal.schriftgroesseVersammlunsleiterView, "schriftgroesseVersammlunsleiterView", 1);

        setzeErgInt(2629,ergHVParam.paramPortal.jnAbfrageBeiWeisungQuittung, "jnAbfrageBeiWeisungQuittung", 1);

        setzeErgInt(2630,ergHVParam.paramPortal.emailVersandRegistrierungOderWiderspruch, "emailVersandRegistrierungOderWiderspruch", 1);
        setzeErgInt(2631,ergHVParam.paramPortal.fragenZurueckziehenMoeglichNurWennAktiv, "fragenZurueckziehenMoeglichNurWennAktiv", 1);
        setzeErgInt(2632,ergHVParam.paramPortal.wortmeldungZurueckziehenMoeglichNurWennAktiv, "wortmeldungZurueckziehenMoeglichNurWennAktiv", 1);
        setzeErgInt(2633,ergHVParam.paramPortal.widerspruecheZurueckziehenMoeglichNurWennAktiv, "widerspruecheZurueckziehenMoeglichNurWennAktiv", 1);
        setzeErgInt(2634,ergHVParam.paramPortal.antraegeZurueckziehenMoeglichNurWennAktiv, "antraegeZurueckziehenMoeglichNurWennAktiv", 1);
        setzeErgInt(2635,ergHVParam.paramPortal.sonstMitteilungenZurueckziehenMoeglichNurWennAktiv, "sonstMitteilungenZurueckziehenMoeglichNurWennAktiv", 1);
        setzeErgInt(2636,ergHVParam.paramPortal.botschaftenZurueckziehenMoeglichNurWennAktiv, "botschaftenZurueckziehenMoeglichNurWennAktiv", 1);
        setzeErgInt(2637,ergHVParam.paramPortal.zuschaltungHVStreamAutomatischStarten, "zuschaltungHVStreamAutomatischStarten", 1);
        setzeErgInt(2638,ergHVParam.paramPortal.onlineTeilnahmeTeilnehmerNameBestaetigen, "onlineTeilnahmeTeilnehmerNameBestaetigen", 1);
        setzeErgInt(2639,ergHVParam.paramPortal.bestaetigungStimmabgabeNachHV, "bestaetigungStimmabgabeNachHV", 1);
        setzeErgInt(2640,ergHVParam.paramPortal.ku178SepaIstAktiv, "ku178SepaIstAktiv", 1);
      
        setzeErgInt(2644,ergHVParam.paramPortal.wortmeldungNurEineOffeneZulaessig, "wortmeldungNurEineOffeneZulaessig", 1);
        setzeErgInt(2645,ergHVParam.paramPortal.srvZusaetzlichZuBriefwahlMoeglich, "srvZusaetzlichZuBriefwahlMoeglich", 1);
        setzeErgInt(2646,ergHVParam.paramPortal.briefwahlAlsOnlineStimmabgabe, "briefwahlAlsOnlineStimmabgabe", 1);
        setzeErgInt(2647,ergHVParam.paramPortal.multiUserImPortalIgnorieren, "multiUserImPortalIgnorieren", 1);
        setzeErgInt(2648, ergHVParam.paramPortal.weisungenAktuellNichtMoeglichAberBriefwahlSchon, "weisungenAktuellNichtMoeglichAberBriefwahlSchon", 1);
       
        setzeErgString(2649, ergHVParam.paramPortal.antraegeTextInOberflaecheLang, "antraegeTextInOberflächeLang", 1);
        setzeErgString(2650, ergHVParam.paramPortal.antraegeTextInOberflaecheKurz, "antraegeTextInOberflächeKurz", 1);
        setzeErgString(2651, ergHVParam.paramPortal.botschaftenTextInOberflaecheLang, "botschaftenTextInOberflächeLang", 1);
        setzeErgString(2652, ergHVParam.paramPortal.botschaftenTextInOberflaecheKurz, "botschaftenTextInOberflächeKurz", 1);
        setzeErgString(2653, ergHVParam.paramPortal.fragenTextInOberflaecheLang, "fragenTextInOberflächeLang", 1);
        setzeErgString(2654, ergHVParam.paramPortal.fragenTextInOberflaecheKurz, "fragenTextInOberflächeKurz", 1);
        setzeErgString(2655, ergHVParam.paramPortal.sonstMitteilungenTextInOberflaecheLang, "sonstMitteilungenTextInOberflächeLang", 1);
        setzeErgString(2656, ergHVParam.paramPortal.sonstMitteilungenTextInOberflaecheKurz, "sonstMitteilungenTextInOberflächeKurz", 1);
        setzeErgString(2657, ergHVParam.paramPortal.widerspruecheTextInOberflaecheLang, "widerspruecheTextInOberflächeLang", 1);
        setzeErgString(2658, ergHVParam.paramPortal.widerspruecheTextInOberflaecheKurz, "widerspruecheTextInOberflächeKurz", 1);

        setzeErgInt(2659, ergHVParam.paramPortal.ekVersandFuerAlleImPortalAngeforderten, "ekVersandFuerAlleImPortalAngeforderten", 1);

        setzeErgString(2660, ergHVParam.paramPortal.wortmeldungTextInOberflaecheLang, "wortmeldungTextInOberflaecheLang", 1);
        setzeErgString(2661, ergHVParam.paramPortal.wortmeldungTextInOberflaecheKurz, "wortmeldungTextInOberflaecheKurz", 1);

        setzeErgInt(2662, ergHVParam.paramPortal.antraegeKontaktdatenTelefonAbfragen, "antraegeKontaktdatenTelefonAbfragen", 1);
        setzeErgInt(2663, ergHVParam.paramPortal.emailVersandZweitEMailAusRegister, "emailVersandZweitEMailAusRegister", 1);

        setzeErgInt(2664, ergHVParam.paramBasis.hoechststimmrechtHackAktiv, "hoechststimmrechtHackAktiv", 1);
        setzeErgInt(2665, ergHVParam.paramBasis.stimmenBeiPraesenzReduzierenUmStimmen, "stimmenBeiPraesenzReduzierenUmStimmen", 1);

        
        for (int i1=0;i1<10;i1++) {
            setzeErgInt(2670+i1, ergHVParam.paramBasis.veranstaltungstyp[i1], "veranstaltungstyp", 1);
        }

        
        /*INFO hier fortsetzen*/
        
        /***********3001 bis 3100 - autoDruckerVerwendung****************************/
        for (int i1=1;i1<=100;i1++) {
            setzeErgInt(i1+3000, ergHVParam.paramBasis.autoDruckerVerwendung[i1], "autoDruckerVerwendung"+Integer.toString(i1), 1);
        }


        
        
        /****************ablaufAbstimmung******************************************/
        delete_ablaufAbstimmung();

        int anz = 0;
        if (ergHVParam.paramAbstimmungParameter.ablaufAbstimmung != null) {
            anz = ergHVParam.paramAbstimmungParameter.ablaufAbstimmung.length;
        }
        setzeErgInt(1000, anz, "Anzahl Elemente Steuerung Abstimmung", 1);

        for (int i = 0; i < anz; i++) {
            setzeErgInt(1001 + i, ergHVParam.paramAbstimmungParameter.ablaufAbstimmung[i],
                    "Elemente Steuerung Abstimmung", 1);
        }

        /****************90001-90040 (reserviert bis 9200): EclPortalFunktion******************************************/
        for (int i = 1; i <= 40; i++) {
            EclPortalFunktion lPortalFunktion = ergHVParam.paramPortal.eclPortalFunktion[i];
            hString = "";
            if (lPortalFunktion.wirdAngeboten) {
                hString += "1";
            } else {
                hString += "0";
            }

            if (lPortalFunktion.berechtigtGast1) {
                hString += "1";
            } else {
                hString += "0";
            }
            if (lPortalFunktion.berechtigtGast2) {
                hString += "1";
            } else {
                hString += "0";
            }
            if (lPortalFunktion.berechtigtGast3) {
                hString += "1";
            } else {
                hString += "0";
            }
            if (lPortalFunktion.berechtigtGast4) {
                hString += "1";
            } else {
                hString += "0";
            }
            if (lPortalFunktion.berechtigtGast5) {
                hString += "1";
            } else {
                hString += "0";
            }
            if (lPortalFunktion.berechtigtGast6) {
                hString += "1";
            } else {
                hString += "0";
            }
            if (lPortalFunktion.berechtigtGast7) {
                hString += "1";
            } else {
                hString += "0";
            }
            if (lPortalFunktion.berechtigtGast8) {
                hString += "1";
            } else {
                hString += "0";
            }
            if (lPortalFunktion.berechtigtGast9) {
                hString += "1";
            } else {
                hString += "0";
            }
            if (lPortalFunktion.berechtigtGast10) {
                hString += "1";
            } else {
                hString += "0";
            }

            if (lPortalFunktion.berechtigtGastOnlineTeilnahmer1) {
                hString += "1";
            } else {
                hString += "0";
            }
            if (lPortalFunktion.berechtigtGastOnlineTeilnahmer2) {
                hString += "1";
            } else {
                hString += "0";
            }
            if (lPortalFunktion.berechtigtGastOnlineTeilnahmer3) {
                hString += "1";
            } else {
                hString += "0";
            }
            if (lPortalFunktion.berechtigtGastOnlineTeilnahmer4) {
                hString += "1";
            } else {
                hString += "0";
            }
            if (lPortalFunktion.berechtigtGastOnlineTeilnahmer5) {
                hString += "1";
            } else {
                hString += "0";
            }
            if (lPortalFunktion.berechtigtGastOnlineTeilnahmer6) {
                hString += "1";
            } else {
                hString += "0";
            }
            if (lPortalFunktion.berechtigtGastOnlineTeilnahmer7) {
                hString += "1";
            } else {
                hString += "0";
            }
            if (lPortalFunktion.berechtigtGastOnlineTeilnahmer8) {
                hString += "1";
            } else {
                hString += "0";
            }
            if (lPortalFunktion.berechtigtGastOnlineTeilnahmer9) {
                hString += "1";
            } else {
                hString += "0";
            }
            if (lPortalFunktion.berechtigtGastOnlineTeilnahmer10) {
                hString += "1";
            } else {
                hString += "0";
            }

            if (lPortalFunktion.berechtigtAktionaer) {
                hString += "1";
            } else {
                hString += "0";
            }
            if (lPortalFunktion.berechtigtAngemeldeterAktionaer) {
                hString += "1";
            } else {
                hString += "0";
            }
            if (lPortalFunktion.berechtigtOnlineTeilnahmeAktionaer) {
                hString += "1";
            } else {
                hString += "0";
            }

            hString += Integer.toString(lPortalFunktion.aktiv);

            setzeErgString(90000 + i, CaString.fuelleRechtsBlank(hString, 40),
                    "eclPortalFunktion " + Integer.toString(i), 1);
        }

        /****************90201-90220 (reserviert bis 9400): EclPortalPhase******************************************/
        for (int i = 1; i <= 20; i++) {
            EclPortalPhase lPortalPhase = ergHVParam.paramPortal.eclPortalPhase[i];
            hString = "";
            if (lPortalPhase.gewinnspielAktiv) {
                hString += "1";
            } else {
                hString += "0";
            }
            if (lPortalPhase.lfdHVPortalInBetrieb) {
                hString += "1";
            } else {
                hString += "0";
            }

            hString += Integer.toString(lPortalPhase.lfdVorDerHVNachDerHV);

            if (lPortalPhase.lfdHVPortalErstanmeldungIstMoeglich) {
                hString += "1";
            } else {
                hString += "0";
            }
            if (lPortalPhase.lfdHVPortalEKIstMoeglich) {
                hString += "1";
            } else {
                hString += "0";
            }
            if (lPortalPhase.lfdHVPortalSRVIstMoeglich) {
                hString += "1";
            } else {
                hString += "0";
            }
            if (lPortalPhase.lfdHVPortalBriefwahlIstMoeglich) {
                hString += "1";
            } else {
                hString += "0";
            }
            if (lPortalPhase.lfdHVPortalKIAVIstMoeglich) {
                hString += "1";
            } else {
                hString += "0";
            }
            if (lPortalPhase.lfdHVPortalVollmachtDritteIstMoeglich) {
                hString += "1";
            } else {
                hString += "0";
            }

            if (lPortalPhase.lfdHVStreamIstMoeglich) {
                hString += "1";
            } else {
                hString += "0";
            }
            if (lPortalPhase.lfdHVFragenIstMoeglich) {
                hString += "1";
            } else {
                hString += "0";
            }
            if (lPortalPhase.lfdHVWortmeldungenIstMoeglich) {
                hString += "1";
            } else {
                hString += "0";
            }
            if (lPortalPhase.lfdHVWiderspruecheIstMoeglich) {
                hString += "1";
            } else {
                hString += "0";
            }
            if (lPortalPhase.lfdHVAntraegeIstMoeglich) {
                hString += "1";
            } else {
                hString += "0";
            }
            if (lPortalPhase.lfdHVSonstigeMitteilungenIstMoeglich) {
                hString += "1";
            } else {
                hString += "0";
            }
            if (lPortalPhase.lfdHVChatIstMoeglich) {
                hString += "1";
            } else {
                hString += "0";
            }
            if (lPortalPhase.lfdHVUnterlagenGruppe1IstMoeglich) {
                hString += "1";
            } else {
                hString += "0";
            }
            if (lPortalPhase.lfdHVUnterlagenGruppe2IstMoeglich) {
                hString += "1";
            } else {
                hString += "0";
            }
            if (lPortalPhase.lfdHVUnterlagenGruppe3IstMoeglich) {
                hString += "1";
            } else {
                hString += "0";
            }
            if (lPortalPhase.lfdHVUnterlagenGruppe4IstMoeglich) {
                hString += "1";
            } else {
                hString += "0";
            }
            if (lPortalPhase.lfdHVUnterlagenGruppe5IstMoeglich) {
                hString += "1";
            } else {
                hString += "0";
            }
            if (lPortalPhase.lfdHVTeilnehmerverzIstMoeglich) {
                hString += "1";
            } else {
                hString += "0";
            }
            if (lPortalPhase.lfdHVAbstimmungsergIstMoeglich) {
                hString += "1";
            } else {
                hString += "0";
            }

            if (lPortalPhase.manuellAktiv) {
                hString += "1";
            } else {
                hString += "0";
            }
            if (lPortalPhase.manuellDeaktiv) {
                hString += "1";
            } else {
                hString += "0";
            }
            if (lPortalPhase.lfdHVBotschaftenEinreichenIstMoeglich) {
                hString += "1";
            } else {
                hString += "0";
            }
            if (lPortalPhase.lfdHVBotschaftenIstMoeglich) {
                hString += "1";
            } else {
                hString += "0";
            }
            if (lPortalPhase.lfdHVRueckfragenIstMoeglich) {
                hString += "1";
            } else {
                hString += "0";
            }

            setzeErgString(90200 + i, CaString.fuelleRechtsBlank(hString, 40), "eclPortalPhase " + Integer.toString(i),
                    1);
        }

        /*****************91001-92000 : EclPortalUnterlagen (analog auch in lang)*********************************/
        /*Entfernt 08.01.2022 - überführt in tbl_unterlagen*/

        /*******************ParamLang****************************/
        /*Portal*/
        setzeErgString(1, ergHVParam.paramPortalServer.httpPfadTeil1, "httpPfadTeil1", 4);
        setzeErgString(2, ergHVParam.paramPortalServer.httpPfadTeil2, "httpPfadTeil2", 4);
        setzeErgString(3, ergHVParam.paramPortal.stimmrechtsvertreterNameDE, "stimmrechtsvertreterName", 4);
        setzeErgString(4, ergHVParam.paramPortal.linkTagesordnung, "linkTagesordnung", 4);
        setzeErgString(5, ergHVParam.paramPortal.linkGegenantraege, "linkGegenantraege", 4);
        setzeErgString(6, ergHVParam.paramPortal.linkEinladungsPDF, "linkEinladungsPDF", 4);
        setzeErgString(7, ergHVParam.paramPortal.emailAdresseLink, "emailAdresseLink", 4);
        setzeErgString(8, ergHVParam.paramPortal.emailAdresseText, "emailAdresseText", 4);
        setzeErgString(9, ergHVParam.paramPortal.stimmrechtsvertreterNameEN, "stimmrechtsvertreterNameEN", 4);
        setzeErgString(10, ergHVParam.paramAbstimmungParameter.veroeffentlichtImBundesanzeigerVom,
                "veroeffentlichtImBundesanzeigerVom", 4);
        setzeErgString(11, ergHVParam.paramPortal.loginGesperrtTextDeutsch, "loginGesperrtTextDeutsch", 4);
        setzeErgString(12, ergHVParam.paramPortal.loginGesperrtTextEnglisch, "loginGesperrtTextEnglisch", 4);
        setzeErgString(13, ergHVParam.paramPortal.linkNutzungsbedingungenAktionaersPortal,
                "linkNutzungsbedingungenAktionaersPortal", 4);
        setzeErgString(14, ergHVParam.paramPortal.linkNutzungsbedingungenHVPortal, "linkNutzungsbedingungenHVPortal",
                4);
        setzeErgString(15, ergHVParam.paramPortal.linkDatenschutzhinweise, "linkDatenschutzhinweise", 4);
        setzeErgString(16, ergHVParam.paramPortal.linkImpressum, "linkImpressum", 4);

        String[] speichernArray=CaString.zerlegeInStrings(ergHVParam.paramPortal.streamLink, 200, 3);
        setzeErgString(17, speichernArray[0], "streamLink", 4);
        setzeErgString(101, speichernArray[1], "streamLink", 4);
        setzeErgString(102, speichernArray[2], "streamLink", 4);
        
        speichernArray=CaString.zerlegeInStrings(ergHVParam.paramPortal.streamID, 200, 3);
        setzeErgString(18, speichernArray[0], "streamID", 4);
        setzeErgString(103, speichernArray[1], "streamID", 4);
        setzeErgString(104, speichernArray[2], "streamID", 4);

        setzeErgString(19, ergHVParam.paramPortal.linkDatenschutzhinweiseKunde, "linkDatenschutzhinweiseKunde", 4);
        setzeErgString(20, ergHVParam.paramPortal.logoutZiel, "logoutZiel", 4);

        setzeErgString(21, ergHVParam.paramPortal.widerspruecheMailVerteiler1, "mitteilungenMailVerteiler1", 4);
        setzeErgString(22, ergHVParam.paramPortal.widerspruecheMailVerteiler2, "mitteilungenMailVerteiler2", 4);
        setzeErgString(23, ergHVParam.paramPortal.widerspruecheMailVerteiler3, "mitteilungenMailVerteiler3", 4);

        setzeErgString(24, ergHVParam.paramPortal.fragenMailVerteiler1, "fragenMailVerteiler1", 4);
        setzeErgString(25, ergHVParam.paramPortal.fragenMailVerteiler2, "fragenMailVerteiler2", 4);
        setzeErgString(26, ergHVParam.paramPortal.fragenMailVerteiler3, "fragenMailVerteiler3", 4);

        /*27 bis 36...: unterlageButton1DE etc - entfernt 08.01.2022*/
        /*37 bis 46...: unterlageButton1EN etc - entfernt 08.01.2022*/

        speichernArray=CaString.zerlegeInStrings(ergHVParam.paramPortal.streamLink2, 200, 3);
        setzeErgString(47, speichernArray[0], "streamLink2", 4);
        setzeErgString(105, speichernArray[1], "streamLink2", 4);
        setzeErgString(106, speichernArray[2], "streamLink2", 4);
        
        speichernArray=CaString.zerlegeInStrings(ergHVParam.paramPortal.streamID2, 200, 3);
        setzeErgString(48, speichernArray[0], "streamID2", 4);
        setzeErgString(107, speichernArray[1], "streamID2", 4);
        setzeErgString(108, speichernArray[2], "streamID2", 4);

        /*49 bis 58...: unterlageButton11DE etc - entfernt 08.01.2022*/
        /*59 bis 68...: unterlageButton11EN etc - entfernt 08.01.2022*/
 
        setzeErgString(69, ergHVParam.paramPortal.wortmeldungMailVerteiler1, "wortmeldungMailVerteiler1", 4);
        setzeErgString(70, ergHVParam.paramPortal.wortmeldungMailVerteiler2, "wortmeldungMailVerteiler2", 4);
        setzeErgString(71, ergHVParam.paramPortal.wortmeldungMailVerteiler3, "wortmeldungMailVerteiler3", 4);

        setzeErgString(72, ergHVParam.paramPortal.antraegeMailVerteiler1, "antraegeMailVerteiler1", 4);
        setzeErgString(73, ergHVParam.paramPortal.antraegeMailVerteiler2, "antraegeMailVerteiler2", 4);
        setzeErgString(74, ergHVParam.paramPortal.antraegeMailVerteiler3, "antraegeMailVerteiler3", 4);

        setzeErgString(75, ergHVParam.paramPortal.sonstMitteilungenMailVerteiler1, "sonstMitteilungenMailVerteiler1", 4);
        setzeErgString(76, ergHVParam.paramPortal.sonstMitteilungenMailVerteiler2, "sonstMitteilungenMailVerteiler2", 4);
        setzeErgString(77, ergHVParam.paramPortal.sonstMitteilungenMailVerteiler3, "sonstMitteilungenMailVerteiler3", 4);

        setzeErgString(78, ergHVParam.paramBasis.mailConsultant, "mailConsultant", 4);

        setzeErgString(79, ergHVParam.paramPortal.vollmachtEmailAdresseLink, "vollmachtEmailAdresseLink", 4);
        setzeErgString(80, ergHVParam.paramPortal.vollmachtEmailAdresseText, "vollmachtEmailAdresseText", 4);
        setzeErgString(81, ergHVParam.paramPortal.kontaktformularBeiEingangMailAn, "kontaktformularBeiEingangMailAn", 4);

        setzeErgString(82, ergHVParam.paramPortal.api_client_id, "api_client_id", 4);
        setzeErgString(83, ergHVParam.paramPortal.api_client_secret, "api_client_secret", 4);
        setzeErgString(84, ergHVParam.paramPortal.api_base_url, "api_base_url", 4);
        setzeErgString(85, ergHVParam.paramPortal.api_key_id, "api_key_id", 4);
        setzeErgString(86, ergHVParam.paramPortal.api_ping_url, "api_ping_url", 4);
        /*101 bis 108 für Ergänzung zu 17/18 und 47/48 verwendet!*/
        /*109, 110, 111 - nicht mehr verwendet. Ehemals für konfTenandID, konfClientID, konfClientSecret*/
        setzeErgString(112, ergHVParam.paramPortal.konfSessionId, "konfSessionId", 4);
        setzeErgString(113, ergHVParam.paramPortal.botschaftenMailVerteiler1, "botschaftenMailVerteiler1", 4);
        setzeErgString(114, ergHVParam.paramPortal.botschaftenMailVerteiler2, "botschaftenMailVerteiler2", 4);
        setzeErgString(115, ergHVParam.paramPortal.botschaftenMailVerteiler3, "botschaftenMailVerteiler3", 4);
        setzeErgString(116, ergHVParam.paramPortal.kurzLinkPortal, "kurzLinkPortal", 4);
        setzeErgString(117, ergHVParam.paramPortal.subdomainPortal, "subdomainPortal", 4);
        
        /*118*/
        hString="";
        /*0 bis 24: pPraesenzStimmkarteAktiv */
        for (int i1 = 0; i1 < 5; i1++) {
            for (int i2 = 0; i2 < 5; i2++) {
                hString = hString + Integer.toString(ergHVParam.paramAkkreditierung.pPraesenzStimmkarteAktiv[i1][i2]);
            }
        }
        /*25 bis 74: pPraesenzStimmkarteFormularnummer - jeweils 2 Zeichen */
        for (int i1 = 0; i1 < 5; i1++) { /*Für alle Gattungen*/
            for (int i2 = 0; i2 < 5; i2++) { //Für alle Zuordnungen
                String hString1=ergHVParam.paramAkkreditierung.pPraesenzStimmkarteFormularnummer[i1][i2].trim();
                if (hString1.length()>2) {hString1="01";}
                if (hString1.isEmpty()) {hString1="01";}
                if (hString1.length()<2) {hString1="0"+hString1;}
                hString = hString + hString1;
            }
        }
        /*75 bis 99: pPraesenzStimmkarteTauschBeliebig*/
        for (int i1 = 0; i1 < 5; i1++) { /*Für alle Gattungen*/
            for (int i2 = 0; i2 < 5; i2++) { //Für alle Zuordnungen
                hString = hString + Integer.toString(ergHVParam.paramAkkreditierung.pPraesenzStimmkarteTauschBeliebig[i1][i2]);
            }
        }
        /*100 bis 124: pPraesenzStimmkarteNachdruckServiceDesk*/
        for (int i1 = 0; i1 < 5; i1++) { /*Für alle Gattungen*/
            for (int i2 = 0; i2 < 5; i2++) { //Für alle Zuordnungen
                hString = hString + Integer.toString(ergHVParam.paramAkkreditierung.pPraesenzStimmkarteNachdruckServiceDesk[i1][i2]);
            }
        }
        /*125 bis 174: pPraesenzStimmkarteDruckerBeiErstzugang - jeweils 2 Zeichen */
        for (int i1 = 0; i1 < 5; i1++) { /*Für alle Gattungen*/
            for (int i2 = 0; i2 < 5; i2++) { //Für alle Zuordnungen
                String hString1=ergHVParam.paramAkkreditierung.pPraesenzStimmkarteDruckerBeiErstzugang[i1][i2].trim();
                if (hString1.length()>2) {hString1="  ";}
                if (hString1.isEmpty()) {hString1="  ";}
                if (hString1.length()<2) {hString1="0"+hString1;}
                hString = hString + hString1;
            }
        }
        
        setzeErgString(118, hString, "Stimmkarten", 4);
        
        setzeErgString(119, ergHVParam.paramPortal.apiku178_url, "apiku178_url", 4);

        for (int i=120;i<=129;i++) {
            setzeErgString(i, ergHVParam.paramPortal.inhaltsHinweiseTextDE[i-120], "inhaltsHinweiseTextDE "+Integer.toString(i-120), 4);
        }

        for (int i=130;i<=139;i++) {
            setzeErgString(i, ergHVParam.paramPortal.inhaltsHinweiseTextEN[i-130], "inhaltsHinweiseTextEN "+Integer.toString(i-130), 4);
        }
       
        setzeErgString(140, ergHVParam.paramPortal.konfEventId, "konfEventId", 4);

        for (int i=141;i<=143;i++) {
            setzeErgString(i, ergHVParam.paramPortal.konfRaumId[0][i-141], "konfRaumId[0] "+Integer.toString(i-141), 4);
        }

        for (int i=144;i<=146;i++) {
            setzeErgString(i, ergHVParam.paramPortal.konfRaumId[1][i-144], "konfRaumId[1] "+Integer.toString(i-144), 4);
        }

        setzeErgString(147, ergHVParam.paramPortal.konfBackupServer, "konfBackupServer", 4);

        for (int i=148;i<=150;i++) {
            setzeErgString(i, ergHVParam.paramPortal.konfRaumBId[0][i-148], "konfRaumBId[0] "+Integer.toString(i-148), 4);
        }

        for (int i=151;i<=153;i++) {
            setzeErgString(i, ergHVParam.paramPortal.konfRaumBId[1][i-151], "konfRaumBId[1] "+Integer.toString(i-144), 4);
        }

        for (int i=154;i<=156;i++) {
            setzeErgString(i, ergHVParam.paramPortal.konfRaumId[0][i-154+3], "konfRaumId[0] "+Integer.toString(i-153+3), 4);
        }

        for (int i=157;i<=159;i++) {
            setzeErgString(i, ergHVParam.paramPortal.konfRaumId[1][i-157+3], "konfRaumId[1] "+Integer.toString(i-157+3), 4);
        }

        for (int i=160;i<=162;i++) {
            setzeErgString(i, ergHVParam.paramPortal.konfRaumBId[0][i-160+3], "konfRaumBId[0] "+Integer.toString(i-160+3), 4);
        }

        for (int i=163;i<=165;i++) {
            setzeErgString(i, ergHVParam.paramPortal.konfRaumBId[1][i-163+3], "konfRaumBId[1] "+Integer.toString(i-163+3), 4);
        }

        for (int i=172;i<=181;i++) {
            int hOffset=i-172;
            String hString1="";
            
            String zString=Integer.toString(ergHVParam.paramAkkreditierung.label_xPos[hOffset]);
            zString=CaString.fuelleLinksNull(zString, 3);
            hString1+=zString;
            
            zString=Integer.toString(ergHVParam.paramAkkreditierung.label_yPos[hOffset]);
            zString=CaString.fuelleLinksNull(zString, 3);
            hString1+=zString;

            zString=Integer.toString(ergHVParam.paramAkkreditierung.label_fontSize[hOffset]);
            zString=CaString.fuelleLinksNull(zString, 2);
            hString1+=zString;

            zString=Integer.toString(ergHVParam.paramAkkreditierung.label_zeilenBedingung[hOffset]);
            zString=CaString.fuelleLinksNull(zString, 2);
            hString1+=zString;

            hString1+=ergHVParam.paramAkkreditierung.labelString[hOffset];
            
            setzeErgString(i, hString1, "labelFormat "+Integer.toString(hOffset), 4);
        }

        setzeErgString(182, ergHVParam.paramAkkreditierung.label_QRCode, "label_QRCode", 4);
       
        /*INFO hier fortsetzen*/

        
        /****************91001-92000 : EclPortalUnterlagen DE (analog auch in kurz)******************************************/
        for (int i = 0; i < ergHVParam.paramPortal.eclPortalUnterlagen.size(); i++) {
            setzeErgString(91001 + i, ergHVParam.paramPortal.eclPortalUnterlagen.get(i).bezeichnungDE,
                    "EclPortalUnterlagenDE " + Integer.toString(i), 4);
        }

        /****************92001-93000 : EclPortalUnterlagen EN (analog auch in kurz)******************************************/
        for (int i = 0; i < ergHVParam.paramPortal.eclPortalUnterlagen.size(); i++) {
            setzeErgString(92001 + i, ergHVParam.paramPortal.eclPortalUnterlagen.get(i).bezeichnungEN,
                    "EclPortalUnterlagenEN " + Integer.toString(i), 4);
        }

        updateLocal_all();

        dbBundle.dbPortalUnterlagen.insert(ergHVParam.paramPortal.eclPortalUnterlagen);
        
        if (!nutzeSet) {
            BvReload bvReload = new BvReload(dbBundle);
            bvReload.setReloadParameter(dbBundle.clGlobalVar.mandant);
        }
//      @formatter:on

        /*+++++++++++++++++++++++++++++++++Eindeutige Kennungen updaten++++++++++++++++++++++++++++++*/
        if (ergHVParam.paramPortal.anzahlEindeutigeKennungenVorhanden>ergHVParam.paramPortal.anzahlEindeutigeKennungenVorhandenVorAenderung) {
            CaPasswortErzeugen caPasswortErzeugen=new CaPasswortErzeugen();
            
            for (int i=ergHVParam.paramPortal.anzahlEindeutigeKennungenVorhandenVorAenderung+1;
                    i<=ergHVParam.paramPortal.anzahlEindeutigeKennungenVorhanden;i++) {
                CaBug.druckeLog("Eindeutige Kennung i="+i,logDrucken, 10);
                boolean neueKennungErzeugt=false;
                while (neueKennungErzeugt==false) {
                    CaBug.druckeLog("while neueKennung==false", logDrucken, 10);
                    String naechsteKennung=caPasswortErzeugen.generatePW(8, 0, 2, true, false);
                    CaBug.druckeLog("naechsteKennung="+naechsteKennung, logDrucken, 10);
                    String anfangsBuchstaben=naechsteKennung.substring(0,1);
                    if (anfangsBuchstaben.compareTo("A")>=0 && anfangsBuchstaben.compareTo("Z")<=0) {
                        CaBug.druckeLog("Anfangsbuchstaben paßt", logDrucken, 10);
                        boolean insertMoeglich=dbBundle.dbEindeutigeKennung.insertCheck(naechsteKennung);
                        if (insertMoeglich==true) {
                            CaBug.druckeLog("bereitsVorhanden==false", logDrucken, 10);
                            EclEindeutigeKennung lEindeutigeKennung=new EclEindeutigeKennung();
                            lEindeutigeKennung.eindeutigeKennung=naechsteKennung;
                            dbBundle.dbEindeutigeKennung.insert(lEindeutigeKennung);
                            neueKennungErzeugt=true;
                        }
                    }
                }
            }
        }
        
        
        return 1;
    }

    private int updateLocal_all() {
        String origString = "", hString1 = "", hString2 = "";

        setzeErgInt(19, ergHVParam.paramBasis.pLocalVerzoegerungOpen, "pLocalVerzoegerungOpen", 3);

        /******Portal***************************/

        origString = ergHVParam.paramPortalServer.passwortVergessenMailAnAdresse;//Aufteilung / Zusammensetzung funktioniert nur, wenn kein Blank enthalten
        if (origString.length() <= 40) {
            hString1 = origString;
            hString2 = "";
        } else {
            hString1 = origString.substring(0, 40);
            hString2 = origString.substring(40);
        }
        setzeErgString(182, hString1, "passwortVergessenMailAnAdresse Teil 1", 3);
        setzeErgString(183, hString2, "passwortVergessenMailAnAdresse Teil 2", 3);

        /******************Gästemodul*************************************************************************/
        origString = ergHVParam.paramGaesteModul.bccAdresse1;
        if (origString.length() <= 40) {
            hString1 = origString;
            hString2 = "";
        } else {
            hString1 = origString.substring(0, 40);
            hString2 = origString.substring(40);
        }
        setzeErgString(301, hString1, "bccAdresse1 Teil 1", 3);
        setzeErgString(302, hString2, "bccAdresse1 Teil 2", 3);

        origString = ergHVParam.paramGaesteModul.bccAdresse2;
        if (origString.length() <= 40) {
            hString1 = origString;
            hString2 = "";
        } else {
            hString1 = origString.substring(0, 40);
            hString2 = origString.substring(40);
        }
        setzeErgString(303, hString1, "bccAdresse2 Teil 1", 3);
        setzeErgString(304, hString2, "bccAdresse2 Teil 2", 3);

        return 1;
    }

    private void setzeErgGeraetBoolean(int pIdent, int pGeraeteklasse, boolean pWert, String pBezeichnung) {
        String hWert = "";
        if (pWert) {
            hWert = "1";
        } else {
            hWert = "0";
        }
        setzeErgGeraetString(pIdent, pGeraeteklasse, hWert, pBezeichnung);
    }

    private void setzeErgGeraetInt(int pIdent, int pGeraeteklasse, int pWert, String pBezeichnung) {
        String hWert = "";
        hWert = Integer.toString(pWert);
        setzeErgGeraetString(pIdent, pGeraeteklasse, hWert, pBezeichnung);
    }

    private void setzeErgGeraetString(int pIdent, int pGeraeteklasse, String pWert, String pBezeichnung) {
        int rc = 0;
        EclGeraeteParameter lGeraeteParameter = null;

        lGeraeteParameter = new EclGeraeteParameter();
        lGeraeteParameter.klasse = pGeraeteklasse;
        lGeraeteParameter.ident = pIdent;
        lGeraeteParameter.wert = pWert;
        lGeraeteParameter.beschreibung = pBezeichnung;
        rc = this.updateGeraeteParameter_ohneReload(lGeraeteParameter);
        if (rc < 1) {
            this.insertGeraeteParameter_ohneReload(lGeraeteParameter);
        }
    }

    public int updateServer_all() {

        setzeErgString(1, dbBundle.paramServer.serverBezeichnung, "serverBezeichnung", 2);
        setzeErgInt(2, dbBundle.paramServer.serverArt, "serverArt", 2);

        setzeErgInt(51, dbBundle.paramServer.geraeteSetIdent, "geraeteSetIdent", 2);

        setzeErgString(101, dbBundle.paramServer.pLocalPraefixLink, "pLocalPraefixLink", 2);
        setzeErgString(102, dbBundle.paramServer.pLocalPraefixLinkAlternativ, "pLocalPraefixLinkAlternativ", 2);
        setzeErgString(103, dbBundle.paramServer.pSubdomainPraefixLink, "pSubdomainPraefixLink", 2);
        setzeErgString(104, dbBundle.paramServer.pSubdomainSuffixLink, "pSubdomainSuffixLink", 2);
        setzeErgInt(105, dbBundle.paramServer.pLocalInternPasswort, "pLocalInternPasswort", 2);
        setzeErgInt(106, dbBundle.paramServer.passwortAgingEin, "passwortAgingEin", 2);
        setzeErgInt(107, dbBundle.paramServer.pLocalInternPasswortMeeting, "pLocalInternPasswortMeeting", 2);
        setzeErgInt(108, dbBundle.paramServer.dbServerIdent, "dbServerIdent", 2);
        setzeErgInt(109, dbBundle.paramServer.loginGesperrt, "loginGesperrt", 2);
        setzeErgString(110, dbBundle.paramServer.loginGesperrtTextDeutsch, "loginGesperrtTextDeutsch", 2);
        setzeErgString(111, dbBundle.paramServer.loginGesperrtTextEnglisch, "loginGesperrtTextEnglisch", 2);

        setzeErgBoolean(112, dbBundle.paramServer.verbundServerAktiv[0], "verbundServerAktiv[0]", 2);
        setzeErgBoolean(113, dbBundle.paramServer.verbundServerAktiv[1], "verbundServerAktiv[1]", 2);
        setzeErgBoolean(114, dbBundle.paramServer.verbundServerAktiv[2], "verbundServerAktiv[2]", 2);
        setzeErgBoolean(115, dbBundle.paramServer.verbundServerAktiv[3], "verbundServerAktiv[3]", 2);
        setzeErgBoolean(116, dbBundle.paramServer.verbundServerAktiv[4], "verbundServerAktiv[4]", 2);
  
        setzeErgString(117, dbBundle.paramServer.verbundServerAdresse[0], "verbundServerAdresse[0]", 2);
        setzeErgString(118, dbBundle.paramServer.verbundServerAdresse[1], "verbundServerAdresse[1]", 2);
        setzeErgString(119, dbBundle.paramServer.verbundServerAdresse[2], "verbundServerAdresse[2]", 2);
        setzeErgString(120, dbBundle.paramServer.verbundServerAdresse[3], "verbundServerAdresse[3]", 2);
        setzeErgString(121, dbBundle.paramServer.verbundServerAdresse[4], "verbundServerAdresse[4]", 2);

        setzeErgString(122, dbBundle.paramServer.webSocketsLocalHost, "webSocketsLocalHost", 2);

        setzeErgInt(123, dbBundle.paramServer.standardPortaltextePflegbar, "standardPortaltextePflegbar", 2);
        setzeErgInt(124, dbBundle.paramServer.nummernformenPflegbar, "nummernformenPflegbar", 2);
        setzeErgInt(125, dbBundle.paramServer.instisPflegbar, "instisPflegbar", 2);
        setzeErgString(126, dbBundle.paramServer.praefixPfadVerzeichnisse, "praefixPfadVerzeichnisse", 2);
        setzeErgLong(127, dbBundle.paramServer.timeoutSperrlogik, "timeoutSperrlogik", 2);
        setzeErgString(128, dbBundle.paramServer.domainLinkErsetzen, "domainLinkErsetzen", 2);
        setzeErgString(129, dbBundle.paramServer.domainLinkErsetzenDurch, "domainLinkErsetzenDurch", 2);
        setzeErgString(130, dbBundle.paramServer.portalInitialPasswortTestBestand, "portalInitialPasswortTestBestand", 2);

        if (!nutzeSet) {
            BvReload bvReload = new BvReload(dbBundle);
            bvReload.setReloadParameterServer();
        }

        for (int i=201;i<=300;i++) {
            setzeErgString(i, dbBundle.paramServer.autoDrucker[i-200], "autoDrucker "+Integer.toString(i-200), 2);
            
        }
        return 1;
    }

    public int updateGeraete_all(ParamGeraet pParamGeraet) {
        /*Geräteklasse bestimmen*/
        int lGeraeteklasse = pParamGeraet.identKlasse;

        setzeErgGeraetBoolean(1, lGeraeteklasse, pParamGeraet.akkreditierungVertreterErfassungAktiv,
                "akkreditierungVertreterErfassungAktiv");
        setzeErgGeraetBoolean(2, lGeraeteklasse, pParamGeraet.akkreditierungScanFeldFuerBestaetigungAktiv,
                "akkreditierungScanFeldFuerBestaetigungAktiv");
        //		setzeErgGeraetBoolean(3, lGeraeteklasse, pParamGeraet.akkreditierungAbstimmungMoeglich, "akkreditierungAbstimmungMoeglich"); /*Nicht mehr verwendet*/
        setzeErgGeraetBoolean(4, lGeraeteklasse, pParamGeraet.akkreditierungAnzeigeBelegeBuendeln,
                "akkreditierungAnzeigeBelegeBuendeln");
        setzeErgGeraetBoolean(5, lGeraeteklasse, pParamGeraet.akkreditierungSammelkartenBuchenMoeglich,
                "akkreditierungSammelkartenBuchenMoeglich");
        setzeErgGeraetBoolean(6, lGeraeteklasse, pParamGeraet.akkreditierungLabeldruckFuerAktionaer,
                "akkreditierungLabeldruckFuerAktionaer");
        setzeErgGeraetBoolean(7, lGeraeteklasse, pParamGeraet.nurGueltigeStimmkartenNummerBeiTabletAbstimmung,
                "nurGueltigeStimmkartenNummerBeiTabletAbstimmung");
        setzeErgGeraetInt(8, lGeraeteklasse, pParamGeraet.protokollAnzMaxZugaenge,
                "protokollAnzMaxZugaengeprotokollAnzMaxZugaengeprotokollAnzMaxZugaenge");
        setzeErgGeraetInt(9, lGeraeteklasse, pParamGeraet.protokollAnzMax, "protokollAnzMax");
        setzeErgGeraetBoolean(10, lGeraeteklasse, pParamGeraet.akkreditierungDelayIgnorieren,
                "akkreditierungDelayIgnorieren");
        setzeErgGeraetBoolean(11, lGeraeteklasse, pParamGeraet.abstimmungTabletHochformat,
                "abstimmungTabletHochformat");
        setzeErgGeraetInt(12, lGeraeteklasse, pParamGeraet.abstimmungTabletTyp, "abstimmungTabletTyp");
        setzeErgGeraetBoolean(13, lGeraeteklasse, pParamGeraet.akkreditierungShortcutsAktiv,
                "akkreditierungShortcutsAktiv");
        setzeErgGeraetBoolean(14, lGeraeteklasse, pParamGeraet.vertreterKorrekturBeiKontrollerfassungMoeglich,
                "vertreterKorrekturBeiKontrollerfassungMoeglich");

        setzeErgGeraetInt(15, lGeraeteklasse, pParamGeraet.festgelegterMandant, "festgelegterMandant");
        setzeErgGeraetInt(16, lGeraeteklasse, pParamGeraet.festgelegtesJahr, "festgelegtesJahr");
        setzeErgGeraetString(17, lGeraeteklasse, pParamGeraet.festgelegteHVNummer, "festgelegteHVNummer");
        setzeErgGeraetString(18, lGeraeteklasse, pParamGeraet.festgelegteDatenbank, "festgelegteDatenbank");
        setzeErgGeraetString(19, lGeraeteklasse, pParamGeraet.festgelegterBenutzername, "festgelegterBenutzername");

        setzeErgGeraetString(20, lGeraeteklasse, pParamGeraet.lwPfadAllgemein, "lwPfadAllgemein");
        setzeErgGeraetString(21, lGeraeteklasse, pParamGeraet.lwPfadSicherung1, "lwPfadSicherung1");
        setzeErgGeraetString(22, lGeraeteklasse, pParamGeraet.lwPfadSicherung2, "lwPfadSicherung2");
        setzeErgGeraetString(23, lGeraeteklasse, pParamGeraet.lwPfadExportFuerPraesentation,
                "lwPfadExportFuerPraesentation");
        setzeErgGeraetString(24, lGeraeteklasse, pParamGeraet.lwPfadExportFuerBuehnensystem,
                "lwPfadExportFuerBuehnensystem");
        setzeErgGeraetString(25, lGeraeteklasse, pParamGeraet.lwPfadExportExcelFuerPowerpoint,
                "lwPfadExportExcelFuerPowerpoint");

        setzeErgGeraetBoolean(26, lGeraeteklasse, pParamGeraet.bonDruckerIstZugeordnet, "bonDruckerIstZugeordnet");
        setzeErgGeraetString(27, lGeraeteklasse, pParamGeraet.labelDruckerIPAdresse, "labelDruckerIPAdresse");

        setzeErgGeraetBoolean(28, lGeraeteklasse, pParamGeraet.moduleHVMaster, "moduleHVMaster");
        setzeErgGeraetBoolean(29, lGeraeteklasse, pParamGeraet.moduleFrontOffice, "moduleFrontOffice");
        setzeErgGeraetBoolean(30, lGeraeteklasse, pParamGeraet.moduleKontrolle, "moduleKontrolle");
        setzeErgGeraetBoolean(31, lGeraeteklasse, pParamGeraet.moduleServiceDesk, "moduleServiceDesk");
        setzeErgGeraetBoolean(32, lGeraeteklasse, pParamGeraet.moduleTeilnahmeverzeichnis,
                "moduleTeilnahmeverzeichnis");

        setzeErgGeraetBoolean(33, lGeraeteklasse, pParamGeraet.moduleTabletAbstimmung, "moduleTabletAbstimmung");
        setzeErgGeraetBoolean(34, lGeraeteklasse, pParamGeraet.moduleBestandsverwaltung, "moduleBestandsverwaltung");
        setzeErgGeraetBoolean(35, lGeraeteklasse, pParamGeraet.moduleHotline, "moduleHotline");

        setzeErgGeraetBoolean(36, lGeraeteklasse, pParamGeraet.programmStartKontrollScreenAnzeigen,
                "programmStartKontrollScreenAnzeigen");

        setzeErgGeraetBoolean(37, lGeraeteklasse, pParamGeraet.moduleAktienregisterImport,
                "moduleAktienregisterImport");
        setzeErgGeraetBoolean(38, lGeraeteklasse, pParamGeraet.moduleDesigner, "moduleDesigner");

        setzeErgGeraetString(39, lGeraeteklasse, pParamGeraet.lwPfadKundenordnerBasis, "lwPfadKundenordnerBasis");

        setzeErgGeraetBoolean(40, lGeraeteklasse, pParamGeraet.dbVorrangig, "dbVorrangig");

        setzeErgGeraetBoolean(41, lGeraeteklasse, pParamGeraet.festgelegtesJahrIstFix, "festgelegtesJahrIstFix");
        setzeErgGeraetBoolean(42, lGeraeteklasse, pParamGeraet.festgelegteHVNummerFix, "festgelegteHVNummerFix");
        setzeErgGeraetBoolean(43, lGeraeteklasse, pParamGeraet.festgelegteDatenbankFix, "festgelegteDatenbankFix");
        setzeErgGeraetBoolean(44, lGeraeteklasse, pParamGeraet.festgelegterMandantIstFix, "festgelegterMandantIstFix");
        setzeErgGeraetBoolean(45, lGeraeteklasse, pParamGeraet.festgelegteBenutzernameFix, "festgelegteBenutzernameFix");
        setzeErgGeraetString(46, lGeraeteklasse, pParamGeraet.lwPfadGrossdokumente, "lwPfadAllgemein");
        setzeErgGeraetBoolean(47, lGeraeteklasse, pParamGeraet.akkreditierungSuchfunktionAktiv, "akkreditierungSuchfunktionAktiv");
        setzeErgGeraetBoolean(48, lGeraeteklasse, pParamGeraet.abstimmungTabletPersoenlichZugeordnet, "abstimmungTabletPersoenlichZugeordnet");
        setzeErgGeraetBoolean(49, lGeraeteklasse, pParamGeraet.abstimmungTabletTestmodus, "abstimmungTabletTestmodus");
        setzeErgGeraetBoolean(50, lGeraeteklasse, pParamGeraet.abtimmungTabletFullScreen, "abtimmungTabletFullScreen");
        setzeErgGeraetString(51, lGeraeteklasse, pParamGeraet.abstimmungTabletXSize, "abstimmungTabletXSize");
        setzeErgGeraetString(52, lGeraeteklasse, pParamGeraet.abstimmungTabletYSize, "abstimmungTabletYSize");

        if (!nutzeSet) {
            BvReload bvReload = new BvReload(dbBundle);
            bvReload.setReloadParameterGeraete();
        }

        return 1;
    }

}
