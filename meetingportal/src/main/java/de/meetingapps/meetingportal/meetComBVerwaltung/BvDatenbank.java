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
package de.meetingapps.meetingportal.meetComBVerwaltung;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

import de.meetingapps.meetingportal.meetComAllg.CaBug;
import de.meetingapps.meetingportal.meetComAllg.CaDatumZeit;
import de.meetingapps.meetingportal.meetComAllg.CaFehler;
import de.meetingapps.meetingportal.meetComDb.DbBerechtigungenTexte;
import de.meetingapps.meetingportal.meetComDb.DbBundle;
import de.meetingapps.meetingportal.meetComDb.DbLowLevel;
import de.meetingapps.meetingportal.meetComDb.DbPersonen;
import de.meetingapps.meetingportal.meetComDb.DbReload;
import de.meetingapps.meetingportal.meetComDbVersion.DbEmittentenV1;
import de.meetingapps.meetingportal.meetComDbVersion.DbEmittentenV2;
import de.meetingapps.meetingportal.meetComDbVersion.DbGeraeteKlasseSetZuordnungV2;
import de.meetingapps.meetingportal.meetComDbVersion.DbGeraeteKlasseV3;
import de.meetingapps.meetingportal.meetComDbVersion.DbGeraeteSetV3;
import de.meetingapps.meetingportal.meetComDbVersion.DbMoveMandanten;
import de.meetingapps.meetingportal.meetComDbVersion.DbNummernformSetV6;
import de.meetingapps.meetingportal.meetComDbVersion.DbParameterV3;
import de.meetingapps.meetingportal.meetComDbVersion.DbUserLoginV4;
import de.meetingapps.meetingportal.meetComEntities.EclAktienregister;
import de.meetingapps.meetingportal.meetComEntities.EclEmittenten;
import de.meetingapps.meetingportal.meetComEntities.EclMeldung;
import de.meetingapps.meetingportal.meetComEntities.EclNummernkreis;
import de.meetingapps.meetingportal.meetComEntities.EclParameter;
import de.meetingapps.meetingportal.meetComEntities.EclPersonenNatJur;
import de.meetingapps.meetingportal.meetComEntities.EclPersonenprognose;
import de.meetingapps.meetingportal.meetComEntities.EclTermine;
import de.meetingapps.meetingportal.meetComHVParam.ParamSpezial;
import de.meetingapps.meetingportal.meetComKonst.KonstPortalFunktionen;
import de.meetingapps.meetingportal.meetComKonst.KonstTermine;

/**Funktionen zum Verwalten der Datenbank - insbesondere auch für Table-Versionen,
 * Table-Anlegen etc. (keine fachlichen Inhalte)
 * 
 * Da hier teilweise mehrere bzw. wechselnde "opens" erfolgen müssen, wird hier
 * ein "Basis-DbBundle" des aktuellen Mandanten übergeben (wg. Parameter etc.),
 * weitere Opens erfolgen ggf. individuell in dieser Klasse.
 */
public class BvDatenbank {

    private int logDrucken = 3;

    /**Datenbank-Versions-Beschreibung:
     * 1 = Table-Versions-Table noch nicht vorhanden, d.h. alles ältere.
     * 
     * 2 = tableVersion angelegt und Inhalt richtig gesetzt.
     * 		tbl_emittenten erweitert 
     *
     * 3 =
     * 		tbl_geraetklassesetzuordnung erweitert
     */
    public int aktuellErforderlicheDatenbankversion = 7;

    /**
     * aktuelle vorhandene Table-Version in der Datenbank
     */
    public int aktuellVorhandeneTableVersion = 1;

    private DbBundle dbBundle = null;

    /**pDbBundle muß bereits geöffnet sein*/
    public BvDatenbank(DbBundle pDbBundle) {
        dbBundle = pDbBundle;
    }

    /**Liefert die aktuelle Table-Version in aktuellVorhandeneTableVersion.
     * 1 = alles ok, Datenbank-Tables können uneingeschränkt verwendet werden.
     * Returnwert ansonsten:
     * > fTableInfoNichtLesbar => Versionsinformation der Tables konnte noch nicht gelesen werden (= rcTableVersion 1)
     * > fTablesMuessenUpgedatetWerden => 
     */
    public int liefereTableVersion() {
        int ergebnis = 0;
        ergebnis = dbBundle.dbDatenbankVerwaltung.holeTableVersion();
        CaBug.druckeLog("ergebnis=" + ergebnis, logDrucken, 10);
        if (ergebnis == -1) {
            aktuellVorhandeneTableVersion = 1;
            return CaFehler.fTableInfoNichtLesbar;
        } else {
            aktuellVorhandeneTableVersion = ergebnis;
            if (ergebnis < aktuellErforderlicheDatenbankversion) {
                return CaFehler.fTablesMuessenUpgedatetWerden;
            }
        }
        return 1;
    }

    /**Voraussetzung: aktuellVorhandeneTableVersion muß vorher über liefereTableVersion() gefüllt sein*/
    public boolean updateTableVersion() {

        if (aktuellVorhandeneTableVersion < aktuellErforderlicheDatenbankversion) {
            CaBug.druckeLog("Updaten", logDrucken, 3);

            if (aktuellVorhandeneTableVersion == 1) {
                dbBundle.mandantenTablesGetrennt = false;

                /******tbl_tableversion anlegen***********/

                CaBug.druckeLog("create tbl_tableVersion", logDrucken, 10);
                dbBundle.dbDatenbankVerwaltung.createTableVersion();

                /***********tbl_emittenten - kann vorhanden sein, oder auch nicht***********/
                DbEmittentenV1 dbEmittentenV1 = new DbEmittentenV1(dbBundle);
                if (!dbEmittentenV1.checkTableVorhanden()) {
                    CaBug.druckeLog("create tbl_emittenten", logDrucken, 10);
                    dbEmittentenV1.createTableV1();
                }

                /*In DB vorhandene Mandanten abfragen (über Nummernkreise), und - falls noch nicht vorhanden - anlegen*/
                dbBundle.dbNummernkreis.readAlleMandanten();
                for (int i = 0; i < dbBundle.dbNummernkreis.anzErgebnis(); i++) {
                    int lZuBearbeitenderMandant = dbBundle.dbNummernkreis.ergebnisPosition(i).mandant;
                    if (lZuBearbeitenderMandant != 0) {
                        if (dbEmittentenV1
                                .read(lZuBearbeitenderMandant) == 0) { /*Mandant noch nicht vorhanden - anlegen*/
                            EclEmittenten lEmittent = new EclEmittenten();
                            lEmittent.mandant = lZuBearbeitenderMandant;
                            BvMandanten lBvMandanten = new BvMandanten();
                            boolean brc = lBvMandanten.findeMandantAusListe(lZuBearbeitenderMandant);
                            if (brc) {
                                lEmittent.bezeichnungKurz = lBvMandanten.rcMandantenText;
                            } else {
                                lEmittent.bezeichnungKurz = "Unbekannt";
                            }
                            dbEmittentenV1.insert(lEmittent);
                            CaBug.druckeLog("Mandant " + lZuBearbeitenderMandant + " in tbl_emittenten eingetragen", logDrucken, 10);
                        }
                    }
                }

                /******tbl_emittenten auf neues Table-Format umstellen*/
                dbEmittentenV1.alterTableV1_V2();

                aktuellVorhandeneTableVersion++;
            }

            if (aktuellVorhandeneTableVersion == 2) {
                dbBundle.mandantenTablesGetrennt = false;

                /*Emittenten nochmal updaten*/
                DbEmittentenV2 dbEmittentenV2 = new DbEmittentenV2(dbBundle);
                dbEmittentenV2.alterTableV2_V3();

                /***********tbl_geraetklassesetzuordnung - Erweitern***********/
                CaBug.druckeLog("alter tbl_geraetklassesetzuordnung", logDrucken, 10);
                DbGeraeteKlasseSetZuordnungV2 dbGeraeteKlasseSetZuordnungV2 = new DbGeraeteKlasseSetZuordnungV2(
                        dbBundle);
                dbGeraeteKlasseSetZuordnungV2.alterTableV2_V3();

                aktuellVorhandeneTableVersion++;
            }

            if (aktuellVorhandeneTableVersion == 3) {
                dbBundle.mandantenTablesGetrennt = false;

                /*******tbl_geraeteSet***************/
                DbGeraeteSetV3 dbGeraeteSetV3 = new DbGeraeteSetV3(dbBundle);
                dbGeraeteSetV3.alterTableGeraeteSetV3_V4();

                /*******tbl_geraeteSet***************/
                DbGeraeteKlasseV3 dbGeraeteKlasseV3 = new DbGeraeteKlasseV3(dbBundle);
                dbGeraeteKlasseV3.alterTableGeraeteKlasseV3_V4();

                /**********tbl_parameter* Spalten droppen***********/
                DbParameterV3 dbParameterV3 = new DbParameterV3(dbBundle);
                dbParameterV3.alterTableParameterV3_V4();
                dbParameterV3.alterTableParameterLfdV3_V4();

                /*******tbl_parameterServer anlegen*******/
                dbBundle.dbParameter.createTable_parameterServer();

                /****tbl_geraetelabelprinter****/
                dbBundle.dbGeraeteLabelPrinter.createTable();

                /******Hier die Parameter-Updates rein, die für alle Mandanten (einschließlich 0) durchgeführt werden müssen*****/
                dbBundle.dbEmittenten.readAll(0);

                hVersion3ParameterJeMandant(0);
                if (dbBundle.dbEmittenten.anzErgebnis() > 0) {
                    for (int i = 0; i < dbBundle.dbEmittenten.anzErgebnis(); i++) {
                        hVersion3ParameterJeMandant(dbBundle.dbEmittenten.ergebnisPosition(i).mandant);
                    }

                }

                /**********tbl_relod***********************/
                DbReload lDbReload = new DbReload(dbBundle);
                lDbReload.createTable();

                aktuellVorhandeneTableVersion++;
            }

            if (aktuellVorhandeneTableVersion == 4) {
                dbBundle.mandantenTablesGetrennt = false;

                /*******tbl_userLogin***************/
                DbUserLoginV4 dbUserLoginV4 = new DbUserLoginV4(dbBundle);
                dbUserLoginV4.alterTableV4_V5();

                aktuellVorhandeneTableVersion++;
            }

            if (aktuellVorhandeneTableVersion == 5) {
                /*Mandanten zwischensichern*/
                int zMandant = dbBundle.clGlobalVar.mandant;
                int zJahr = dbBundle.clGlobalVar.hvJahr;
                String zNummer = dbBundle.clGlobalVar.hvNummer;
                String zDatenbereich = dbBundle.clGlobalVar.datenbereich;

                dbBundle.mandantenTablesGetrennt = false;
                dbBundle.dbEmittenten.readAll(0);
                EclEmittenten[] emittentenListe = dbBundle.dbEmittenten.ergebnisArray;

                dbBundle.mandantenTablesGetrennt = true;

                for (int i = 0; i < emittentenListe.length; i++) {

                    dbBundle.clGlobalVar.mandant = emittentenListe[i].mandant;
                    dbBundle.clGlobalVar.hvJahr = emittentenListe[i].hvJahr;
                    dbBundle.clGlobalVar.hvNummer = emittentenListe[i].hvNummer;
                    dbBundle.clGlobalVar.datenbereich = emittentenListe[i].dbArt;

                    EclEmittenten lEmittenten = new EclEmittenten();
                    lEmittenten.mandant = emittentenListe[i].mandant;
                    lEmittenten.hvJahr = emittentenListe[i].hvJahr;
                    lEmittenten.hvNummer = emittentenListe[i].hvNummer;
                    lEmittenten.dbArt = emittentenListe[i].dbArt;
                    BvMandanten lBvMandanten = new BvMandanten();
                    lBvMandanten.legeMandantNeuAn(dbBundle, lEmittenten, false);

                    /*Aufsplitten der Mandanten*/

                    DbMoveMandanten dbMoveMandanten = new DbMoveMandanten(dbBundle);
                    dbMoveMandanten.move(true);
                }

                /*Mandanten zurückbelegen*/
                dbBundle.clGlobalVar.mandant = zMandant;
                dbBundle.clGlobalVar.hvJahr = zJahr;
                dbBundle.clGlobalVar.hvNummer = zNummer;
                dbBundle.clGlobalVar.datenbereich = zDatenbereich;
                aktuellVorhandeneTableVersion++;

            }

            if (aktuellVorhandeneTableVersion == 6) {

                /**Übergreifende Tables**/
                /*DbNummernformSet*/
                DbNummernformSetV6 dbNummernformSetV6 = new DbNummernformSetV6(dbBundle);
                dbNummernformSetV6.alterTableGeraeteSetV6_V7();

                /*Neue Mandantentables*/

                /*Mandanten zwischensichern*/
                int zMandant = dbBundle.clGlobalVar.mandant;
                int zJahr = dbBundle.clGlobalVar.hvJahr;
                String zNummer = dbBundle.clGlobalVar.hvNummer;
                String zDatenbereich = dbBundle.clGlobalVar.datenbereich;

                dbBundle.dbEmittenten.readAll(0);
                EclEmittenten[] emittentenListe = dbBundle.dbEmittenten.ergebnisArray;

                for (int i = 0; i < emittentenListe.length; i++) {

                    dbBundle.clGlobalVar.mandant = emittentenListe[i].mandant;
                    dbBundle.clGlobalVar.hvJahr = emittentenListe[i].hvJahr;
                    dbBundle.clGlobalVar.hvNummer = emittentenListe[i].hvNummer;
                    dbBundle.clGlobalVar.datenbereich = emittentenListe[i].dbArt;

                    /*Mandantenabhänge Tables*/

                    /*Achtung: diese werden ja bereits beim Anlegen der mandantenabhängigen Tables angelegt.
                     * Muß also nur aktiviert werden, wenn ein Update aus späteren Table-Versionen erfolgt!
                     */
                    //					dbBundle.dbParameter.createTable_parameterLang();
                    //					dbBundle.dbTermine.createTable();
                    //					initialisiereTermine();

                }

                /*Mandanten zurückbelegen*/
                dbBundle.clGlobalVar.mandant = zMandant;
                dbBundle.clGlobalVar.hvJahr = zJahr;
                dbBundle.clGlobalVar.hvNummer = zNummer;
                dbBundle.clGlobalVar.datenbereich = zDatenbereich;

            }

            dbBundle.dbDatenbankVerwaltung.updateTableVersion(aktuellErforderlicheDatenbankversion);
            CaBug.druckeLog("Update fertig", logDrucken, 3);
        }

        return true;
    }

    public int updateTableVersion2020A() {
        int rc = 0;

        dbBundle.dbEmittenten.readAll(0);
        EclEmittenten[] emittentenListe = dbBundle.dbEmittenten.ergebnisArray;
        int anzEmittenten = dbBundle.dbEmittenten.anzErgebnis();
        for (int i = 0; i < anzEmittenten; i++) {
            EclEmittenten lEmittent = emittentenListe[i];
            dbBundle.clGlobalVar.mandant = lEmittent.mandant;
            dbBundle.clGlobalVar.hvJahr = lEmittent.hvJahr;
            dbBundle.clGlobalVar.hvNummer = lEmittent.hvNummer;
            dbBundle.clGlobalVar.datenbereich = lEmittent.dbArt;

            DbLowLevel lDbLowLevel = new DbLowLevel(dbBundle);

            rc = lDbLowLevel.rawOperation("ALTER TABLE " + dbBundle.getSchemaMandant() + "tbl_abstimmungen "
                    + "ADD COLUMN `nummerKey` CHAR(10) NULL DEFAULT NULL AFTER `nummerindexEN`, "
                    + "ADD COLUMN `nummerindexKey` CHAR(10) NULL DEFAULT NULL AFTER `nummerKey`, "
                    + "ADD COLUMN `nummerFormular` CHAR(10) NULL DEFAULT NULL AFTER `nummerindexKey`, "
                    + "ADD COLUMN `nummerindexFormular` CHAR(10) NULL DEFAULT NULL AFTER `nummerFormular`, "
                    + "ADD COLUMN `aktivBeiSRV` INT(11) NULL DEFAULT NULL AFTER `aktivAbstimmungInPortal`, "
                    + "ADD COLUMN `aktivBeiBriefwahl` INT(11) NULL DEFAULT NULL AFTER `aktivBeiSRV`, "
                    + "ADD COLUMN `aktivBeiKIAVDauer` INT(11) NULL DEFAULT NULL AFTER `aktivBeiBriefwahl`, "
                    + "ADD COLUMN `weisungNichtMarkierteSpeichernAlsSRV` INT(11) NULL DEFAULT NULL AFTER `tabletFreiText`, "
                    + "ADD COLUMN `weisungNichtMarkierteSpeichernAlsBriefwahl` INT(11) NULL DEFAULT NULL AFTER `weisungNichtMarkierteSpeichernAlsSRV`, "
                    + "ADD COLUMN `weisungNichtMarkierteSpeichernAlsKIAV` INT(11) NULL DEFAULT NULL AFTER `weisungNichtMarkierteSpeichernAlsBriefwahl`, "
                    + "ADD COLUMN `weisungNichtMarkierteSpeichernAlsDauer` INT(11) NULL DEFAULT NULL AFTER `weisungNichtMarkierteSpeichernAlsKIAV`, "
                    + "ADD COLUMN `weisungNichtMarkierteSpeichernAlsOrg` INT(11) NULL DEFAULT NULL AFTER `weisungNichtMarkierteSpeichernAlsDauer`, "
                    + "ADD COLUMN `weisungHVNichtMarkierteSpeichernAls` INT(11) NULL DEFAULT NULL AFTER `weisungNichtMarkierteSpeichernAlsOrg`, "
                    + "ADD COLUMN `abstimmungNichtMarkierteSpeichernAls` INT(11) NULL DEFAULT NULL AFTER `weisungHVNichtMarkierteSpeichernAls`, "
                    + "ADD COLUMN `beschlussvorschlagGestelltVon` INT(11) NULL DEFAULT NULL AFTER `ergaenzungsantrag`, "
                    + "ADD COLUMN `beschlussvorschlagGestelltVonSonstige` VARCHAR(200) NULL DEFAULT NULL AFTER `beschlussvorschlagGestelltVon`, "
                    + "ADD COLUMN `kandidat` VARCHAR(160) NULL DEFAULT NULL AFTER `anzeigeBezeichnungLangEN`, "
                    + "ADD COLUMN `kandidatEN` VARCHAR(160) NULL DEFAULT NULL AFTER `kandidat`, "
                    + "CHANGE COLUMN `kurzBezeichnung` `kurzBezeichnung` VARCHAR(160) NULL DEFAULT NULL , "
                    + "CHANGE COLUMN `kurzBezeichnungEN` `kurzBezeichnungEN` VARCHAR(160) NULL DEFAULT NULL , "
                    + "CHANGE COLUMN `anzeigeBezeichnungKurz` `anzeigeBezeichnungKurz` VARCHAR(800) NULL DEFAULT NULL , "
                    + "CHANGE COLUMN `anzeigeBezeichnungKurzEN` `anzeigeBezeichnungKurzEN` VARCHAR(800) NULL DEFAULT NULL , "
                    + "DROP PRIMARY KEY, " + "ADD PRIMARY KEY (`mandant`, `ident`); ");
            if (rc < 0) {
                return rc;
            }

            rc = lDbLowLevel
                    .rawOperation("ALTER TABLE " + dbBundle.getSchemaMandant() + "tbl_abstimmungzuabstimmungsblock "
                            + "ADD COLUMN `seite` INT(11) NULL DEFAULT NULL AFTER `position`, " + "DROP PRIMARY KEY, "
                            + "ADD PRIMARY KEY (`mandant`, `ident`); ");
            if (rc < 0) {
                return rc;
            }

            rc = lDbLowLevel.rawOperation("ALTER TABLE " + dbBundle.getSchemaMandant() + "tbl_weisungmeldung "
                    + "ADD COLUMN `skIst` INT(11) NULL DEFAULT NULL AFTER `sammelIdent`, " + "DROP PRIMARY KEY, "
                    + "ADD PRIMARY KEY (`mandant`, `weisungIdent`); ");
            if (rc < 0) {
                return rc;
            }

        }
        return 1;
    }

    public int updateTableVersion2020B() {

        dbBundle.openWeitere();
        dbBundle.dbParameterSet.createTable();
        dbBundle.dbParameter.nutzeSetBeginn(0);
        dbBundle.dbParameter.createTable_parameter();
        dbBundle.dbParameter.createTable_parameterLang();
        dbBundle.dbParameter.nutzeSetEnde();

        return 1;
    }

    public int updateTableVersion2020C() {
        //		int rc=0;

        dbBundle.dbEmittenten.readAll(0);
        EclEmittenten[] emittentenListe = dbBundle.dbEmittenten.ergebnisArray;
        int anzEmittenten = dbBundle.dbEmittenten.anzErgebnis();
        for (int i = 0; i < anzEmittenten; i++) {
            EclEmittenten lEmittent = emittentenListe[i];
            dbBundle.clGlobalVar.mandant = lEmittent.mandant;
            dbBundle.clGlobalVar.hvJahr = lEmittent.hvJahr;
            dbBundle.clGlobalVar.hvNummer = lEmittent.hvNummer;
            dbBundle.clGlobalVar.datenbereich = lEmittent.dbArt;

            DbLowLevel lDbLowLevel = new DbLowLevel(dbBundle);

            /*rc=*/lDbLowLevel.rawOperation("ALTER TABLE " + dbBundle.getSchemaMandant() + "tbl_drucklauf "
                    + "ADD COLUMN `drucklaufArt` INT(11) NULL DEFAULT NULL AFTER `erzeugtAm`, " + "DROP PRIMARY KEY, "
                    + "ADD PRIMARY KEY (`mandant`, `drucklaufNr`);");
            //			if (rc<0){return rc;}

        }
        return 1;
    }

    public int updateTableVersion2020D() {

        dbBundle.dbInsti.createTable();
        dbBundle.dbSuchlaufBegriffe.createTable(true);
        dbBundle.dbInstiSubZuordnung.createTable(true);

        DbLowLevel lDbLowLevel = new DbLowLevel(dbBundle);
        /*rc=*/lDbLowLevel.rawOperation("ALTER TABLE " + dbBundle.getSchemaAllgemein() + "tbl_userlogin "
                + "ADD COLUMN `gehoertZuInsti` INT(11) NULL DEFAULT 0 AFTER `email`;");
        //		if (rc<0){return rc;}

        dbBundle.dbEmittenten.readAll(0);
        EclEmittenten[] emittentenListe = dbBundle.dbEmittenten.ergebnisArray;
        int anzEmittenten = dbBundle.dbEmittenten.anzErgebnis();
        for (int i = 0; i < anzEmittenten; i++) {
            EclEmittenten lEmittent = emittentenListe[i];
            dbBundle.clGlobalVar.mandant = lEmittent.mandant;
            dbBundle.clGlobalVar.hvJahr = lEmittent.hvJahr;
            dbBundle.clGlobalVar.hvNummer = lEmittent.hvNummer;
            dbBundle.clGlobalVar.datenbereich = lEmittent.dbArt;

            dbBundle.dbInstiBestandsZuordnung.createTable();
            dbBundle.dbSuchlaufBegriffe.createTable(false);
            dbBundle.dbSuchlaufDefinition.createTable();
            dbBundle.dbSuchlaufErgebnis.createTable();

            dbBundle.dbInstiSubZuordnung.createTable(false);

            /*rc=*/lDbLowLevel.rawOperation("ALTER TABLE " + dbBundle.getSchemaMandant() + "tbl_drucklauf "
                    + "ADD COLUMN `drucklaufSubArt` INT(11) NULL DEFAULT 0 AFTER `drucklaufArt`;");
            //			if (rc<0){return rc;}
        }
        return 1;
    }

    public int updateTableVersion2020E() {

        dbBundle.dbInstiEmittentenMitZuordnung.createTable();
        dbBundle.dbNachricht.createTable();
        dbBundle.dbNachrichtAnhang.createTable();
        dbBundle.dbNachrichtBasisText.createTable();
        dbBundle.dbNachrichtVerwendungsCode.createTable();

        return 1;
    }

    public int updateTableVersion2020F() {
        DbLowLevel lDbLowLevel = new DbLowLevel(dbBundle);
        dbBundle.dbEmittenten.readAll(0);
        EclEmittenten[] emittentenListe = dbBundle.dbEmittenten.ergebnisArray;
        int anzEmittenten = dbBundle.dbEmittenten.anzErgebnis();
        for (int i = 0; i < anzEmittenten; i++) {
            EclEmittenten lEmittent = emittentenListe[i];
            dbBundle.clGlobalVar.mandant = lEmittent.mandant;
            dbBundle.clGlobalVar.hvJahr = lEmittent.hvJahr;
            dbBundle.clGlobalVar.hvNummer = lEmittent.hvNummer;
            dbBundle.clGlobalVar.datenbereich = lEmittent.dbArt;
            lDbLowLevel.rawOperation("ALTER TABLE " + dbBundle.getSchemaMandant() + "tbl_aktienregisterlogindaten "
                    + "ADD COLUMN `anmeldenUnzulaessig` INT(11) NULL DEFAULT 0 AFTER `passwortInitial`, "
                    + "ADD COLUMN `dauerhafteRegistrierungUnzulaessig` INT(11) NULL DEFAULT 0 AFTER `anmeldenUnzulaessig`;");
        }
        return 1;
    }

    public int updateTableVersion2020g() {
        dbBundle.dbEmittenten.readAll(0);
        EclEmittenten[] emittentenListe = dbBundle.dbEmittenten.ergebnisArray;
        int anzEmittenten = dbBundle.dbEmittenten.anzErgebnis();
        for (int i = 0; i < anzEmittenten; i++) {
            EclEmittenten lEmittent = emittentenListe[i];
            dbBundle.clGlobalVar.mandant = lEmittent.mandant;
            dbBundle.clGlobalVar.hvJahr = lEmittent.hvJahr;
            dbBundle.clGlobalVar.hvNummer = lEmittent.hvNummer;
            dbBundle.clGlobalVar.datenbereich = lEmittent.dbArt;
            dbBundle.dbAktienregisterErgaenzung.createTable();
        }
        return 1;
    }

    public int updateTableVersion2020h() {

        dbBundle.dbEmittenten.readAll(0);
        EclEmittenten[] emittentenListe = dbBundle.dbEmittenten.ergebnisArray;
        int anzEmittenten = dbBundle.dbEmittenten.anzErgebnis();
        for (int i = 0; i < anzEmittenten; i++) {
            EclEmittenten lEmittent = emittentenListe[i];
            dbBundle.clGlobalVar.mandant = lEmittent.mandant;
            dbBundle.clGlobalVar.hvJahr = lEmittent.hvJahr;
            dbBundle.clGlobalVar.hvNummer = lEmittent.hvNummer;
            dbBundle.clGlobalVar.datenbereich = lEmittent.dbArt;

            DbLowLevel lDbLowLevel = new DbLowLevel(dbBundle);
            /*rc=*/lDbLowLevel
                    .rawOperation("ALTER TABLE " + dbBundle.getSchemaMandant() + "tbl_aktienregisterergaenzung "
                            + "ADD COLUMN `satzNummer` INT(11) NOT NULL DEFAULT 0 AFTER `aktienregisterIdent`, "
                            + "DROP PRIMARY KEY, " + "ADD PRIMARY KEY (`mandant`, `aktienregisterIdent`, `satzNummer`);"

                    );
            //			if (rc<0){return rc;}

        }
        return 1;
    }

    public int updateTableVersion2020i() {

        dbBundle.dbEmittenten.readAll(0);
        EclEmittenten[] emittentenListe = dbBundle.dbEmittenten.ergebnisArray;
        int anzEmittenten = dbBundle.dbEmittenten.anzErgebnis();
        for (int i = 0; i < anzEmittenten; i++) {
            EclEmittenten lEmittent = emittentenListe[i];
            dbBundle.clGlobalVar.mandant = lEmittent.mandant;
            dbBundle.clGlobalVar.hvJahr = lEmittent.hvJahr;
            dbBundle.clGlobalVar.hvNummer = lEmittent.hvNummer;
            dbBundle.clGlobalVar.datenbereich = lEmittent.dbArt;

            dbBundle.dbVeranstaltung.createTable();
            dbBundle.dbAbstimmungsVorschlagEmpfehlung.createTable();

        }
        return 1;
    }

    public int updateMandantenPersonenprognose() {
        int rc = 0;

        DbLowLevel lDbLowLevel = new DbLowLevel(dbBundle);
        //		Auslesen der vorhandenen Datenbanken
        final List<String> schemas = lDbLowLevel.liefereMandantenSchemas();

        for (String schema : schemas) {

            dbBundle.clGlobalVar.mandant = Integer.valueOf(schema.substring(5, 8));
            dbBundle.clGlobalVar.hvJahr = Integer.valueOf(schema.substring(9, 13));
            dbBundle.clGlobalVar.hvNummer = schema.substring(13, 14);
            dbBundle.clGlobalVar.datenbereich = schema.substring(14, 15);

            CaBug.druckeLog("Schema: " + schema, logDrucken, 10);

            if (!dbBundle.dbPersonenprognose.checkTableVorhandenNew(schema)) {
                CaBug.druckeLog("Table noch nicht vorhanden wird eingefügt.", logDrucken, 10);
                if (dbBundle.dbPersonenprognose.createTable() == 1) {
                    rc = initialisierePersonenprognose();
                }
            } else {
                CaBug.druckeLog("Table bereits vorhanden.", logDrucken, 10);
            }
        }
        return rc;
    }

    public int updateTableVersion2020j() {

        DbLowLevel lDbLowLevel = new DbLowLevel(dbBundle);
        /*rc=*/lDbLowLevel.rawOperation("ALTER TABLE " + dbBundle.getSchemaAllgemein() + "tbl_aufgaben "
                + "ADD COLUMN `drucklaufNr` INT(11) NULL DEFAULT 0 AFTER `userNummerVerarbeitet`;");
        //		if (rc<0){return rc;}
        return 1;
    }

    public int updateTableVersion2020k() {

        DbLowLevel lDbLowLevel = new DbLowLevel(dbBundle);
        /*rc=*/lDbLowLevel.rawOperation("ALTER TABLE " + dbBundle.getSchemaAllgemein() + "tbl_portaltexte "
                + "ADD COLUMN `basisSet` INT(11) NOT NULL DEFAULT 0 AFTER `mandant`;");
        /*rc=*/lDbLowLevel
                .rawOperation("ALTER TABLE " + dbBundle.getSchemaAllgemein() + "tbl_portaltexte " + "DROP PRIMARY KEY, "
                        + "ADD PRIMARY KEY (`mandant`, `identGesamt`, `lfdNummer`, `sprache`, `basisSet`); ");

        dbBundle.dbEmittenten.readAll(0);
        EclEmittenten[] emittentenListe = dbBundle.dbEmittenten.ergebnisArray;
        int anzEmittenten = dbBundle.dbEmittenten.anzErgebnis();
        for (int i = 0; i < anzEmittenten; i++) {
            EclEmittenten lEmittent = emittentenListe[i];
            dbBundle.clGlobalVar.mandant = lEmittent.mandant;
            dbBundle.clGlobalVar.hvJahr = lEmittent.hvJahr;
            dbBundle.clGlobalVar.hvNummer = lEmittent.hvNummer;
            dbBundle.clGlobalVar.datenbereich = lEmittent.dbArt;

            /*rc=*/lDbLowLevel.rawOperation("ALTER TABLE " + dbBundle.getSchemaMandant() + "tbl_portaltexte "
                    + "ADD COLUMN `basisSet` INT(11) NOT NULL DEFAULT 0 AFTER `mandant`;");
            /*rc=*/lDbLowLevel.rawOperation(
                    "ALTER TABLE " + dbBundle.getSchemaMandant() + "tbl_portaltexte " + "DROP PRIMARY KEY, "
                            + "ADD PRIMARY KEY (`mandant`, `identGesamt`, `lfdNummer`, `sprache`, `basisSet`); ");

        }
        return 1;
    }

    public int updateTableVersion2020l() {

//        dbBundle.dbEmittenten.readAll(0);
//        EclEmittenten[] emittentenListe = dbBundle.dbEmittenten.ergebnisArray;
//        int anzEmittenten = dbBundle.dbEmittenten.anzErgebnis();
//        for (int i = 0; i < anzEmittenten; i++) {
//            EclEmittenten lEmittent = emittentenListe[i];
//            dbBundle.clGlobalVar.mandant = lEmittent.mandant;
//            dbBundle.clGlobalVar.hvJahr = lEmittent.hvJahr;
//            dbBundle.clGlobalVar.hvNummer = lEmittent.hvNummer;
//            dbBundle.clGlobalVar.datenbereich = lEmittent.dbArt;
//
//            dbBundle.dbFragen.createTable();
//            dbBundle.dbMitteilungenAlt.createTable();
//
//        }
        return 1;
    }

    public int updateTableVersion2020m() {

        DbLowLevel lDbLowLevel = new DbLowLevel(dbBundle);

        dbBundle.dbEmittenten.readAll(0);
        EclEmittenten[] emittentenListe = dbBundle.dbEmittenten.ergebnisArray;
        int anzEmittenten = dbBundle.dbEmittenten.anzErgebnis();
        for (int i = 0; i < anzEmittenten; i++) {
            EclEmittenten lEmittent = emittentenListe[i];
            dbBundle.clGlobalVar.mandant = lEmittent.mandant;
            dbBundle.clGlobalVar.hvJahr = lEmittent.hvJahr;
            dbBundle.clGlobalVar.hvNummer = lEmittent.hvNummer;
            dbBundle.clGlobalVar.datenbereich = lEmittent.dbArt;

            /*rc=*/lDbLowLevel.rawOperation("ALTER TABLE " + dbBundle.getSchemaMandant() + "tbl_mitteilungen "
                    + "CHANGE COLUMN `mitteilungtext` `mitteilungtext` VARCHAR(21000) NULL DEFAULT NULL ;");

            /*rc=*/lDbLowLevel.rawOperation("ALTER TABLE " + dbBundle.getSchemaMandant() + "tbl_fragen "
                    + "CHANGE COLUMN `fragentext` `fragentext` VARCHAR(21000) NULL DEFAULT NULL ;");

        }
        return 1;
    }

    public int updateTableVersion2020n() {

        dbBundle.dbEmittenten.readAll(0);
        EclEmittenten[] emittentenListe = dbBundle.dbEmittenten.ergebnisArray;
        int anzEmittenten = dbBundle.dbEmittenten.anzErgebnis();
        for (int i = 0; i < anzEmittenten; i++) {
            EclEmittenten lEmittent = emittentenListe[i];
            dbBundle.clGlobalVar.mandant = lEmittent.mandant;
            dbBundle.clGlobalVar.hvJahr = lEmittent.hvJahr;
            dbBundle.clGlobalVar.hvNummer = lEmittent.hvNummer;
            dbBundle.clGlobalVar.datenbereich = lEmittent.dbArt;

            dbBundle.dbKTracking.createTable();

        }
        return 1;
    }

    public int updateTableVersion2021a() {

//        DbLowLevel lDbLowLevel = new DbLowLevel(dbBundle);
//        boolean nurSelektiert = true;
//        dbBundle.dbEmittenten.readAll(0);
//        EclEmittenten[] emittentenListe = dbBundle.dbEmittenten.ergebnisArray;
//        int anzEmittenten = dbBundle.dbEmittenten.anzErgebnis();
//        for (int i = 0; i < anzEmittenten; i++) {
//            EclEmittenten lEmittent = emittentenListe[i];
//            dbBundle.clGlobalVar.mandant = lEmittent.mandant;
//            dbBundle.clGlobalVar.hvJahr = lEmittent.hvJahr;
//            dbBundle.clGlobalVar.hvNummer = lEmittent.hvNummer;
//            dbBundle.clGlobalVar.datenbereich = lEmittent.dbArt;
//
//            if (nurSelektiert) {
//                dbBundle.dbWortmeldungen.createTable();
//            } else {
//
//                dbBundle.dbLoginDaten.createTable();
//                dbBundle.dbVorlaeufigeVollmacht.createTable();
//                dbBundle.dbWortmeldungen.createTable();
//
//                /*rc=*/lDbLowLevel.rawOperation("ALTER TABLE " + dbBundle.getSchemaMandant() + "tbl_meldungen "
//                        + "ADD COLUMN `virtuellerTeilnehmerIdent` INT(11) NULL DEFAULT '0' AFTER `statusWarPraesenz_Delayed`, "
//                        + "ADD INDEX `IDX_VIRTTEIL` (`virtuellerTeilnehmerIdent` ASC); "
//
//                );
//
//                /****Unklar - prüfen ob hier noch rein muß*************/
//                /*DBMeldungen - auch Felder füllen!*/
//                //			ALTER TABLE `db_mc998j2020ap`.`tbl_meldungen` 
//                //			ADD COLUMN `aktienregisterIdent` INT(11) NULL DEFAULT '0' AFTER `aktionaersnummer`;
//                //			ALTER TABLE `db_mc998j2020ap`.`tbl_meldungen` 
//                //			ADD INDEX `IDX_ARIDENT` (`aktienregisterIdent` ASC);
//                //			ALTER TABLE `db_mc998j2020ap`.`tbl_meldungen` 
//                //			ADD COLUMN `fixAnmeldung` INT(11) NULL DEFAULT '0' AFTER `gattung`;
//
//                /*Analog tbl_meldungenprotokoll*/
//
//                /**********Folgendes muß auf jeden Fall noch hier rein***********/
//
//                //			tblMeldungen
//                //			  "KEY `IDX_ARNR` (`aktionaersnummer`) "+
//
//                //			ALTER TABLE `db_mc998j2020ap`.`tbl_meldungen` 
//                //			ADD COLUMN `instiIdent` INT(11) NULL DEFAULT '0' AFTER `skOffenlegung`;
//
//                //			ALTER TABLE `db_mc179j2020ap`.`tbl_meldungen` 
//                //			ADD COLUMN `vorlAnmeldung` INT(11) NULL DEFAULT '0' AFTER `teilnahmeArt`,
//                //			ADD COLUMN `vorlAnmeldungAkt` INT(11) NULL DEFAULT '0' AFTER `vorlAnmeldung`,
//                //			ADD COLUMN `vorlAnmeldungSer` INT(11) NULL DEFAULT '0' AFTER `vorlAnmeldungAkt`,
//
//                //			ALTER TABLE `db_mc998j2020ap`.`tbl_termine` 
//                //			CHANGE COLUMN `terminZeit` `terminZeit` CHAR(8) NULL DEFAULT NULL,
//                //			CHANGE COLUMN `terminDatum` `terminDatum` CHAR(10) NULL DEFAULT NULL ;
//
//                //			ALTER TABLE `db_mc998j2020ap`.`tbl_willenserklaerung` 
//                //			ADD COLUMN `storniert` INT(11) NULL DEFAULT '0' AFTER `protokollnr`,
//                //			ADD COLUMN `storniert_delayed` INT(11) NULL DEFAULT '0' AFTER `storniert`,
//                //			ADD COLUMN `erzeugtUeber` INT(11) NULL DEFAULT '0' AFTER `storniert_delayed`,
//                //			ADD COLUMN `erzeugtUeberLauf` INT(11) NULL DEFAULT '0' AFTER `erzeugtUeber`;
//
//                //			ALTER TABLE `db_mc998j2020ap`.`tbl_willenserklaerung` 
//                //			ADD INDEX `IDX_bevollmaechtigterIdent` (`bevollmaechtigterDritterIdent` ASC);
//
//                //			ALTER TABLE `db_mc179j2020ap`.`tbl_bestworkflow` 
//                //			ADD COLUMN `subverzeichnis` VARCHAR(10) NULL DEFAULT '' AFTER `ident`,
//                //			ADD COLUMN `origOderKopie` INT(11) NULL DEFAULT '0' AFTER `zuAktionaersnummer`,
//                //			ADD COLUMN `vorlVollmachtIdent` INT(11) NULL DEFAULT '0' AFTER `origOderKopie`;
//                //
//
//                ;
//            }
//
//        }
        return 1;
    }

    public int updateTableVersion2021b() {

        boolean nurSelektiert = true;
        dbBundle.dbEmittenten.readAll(0);
        EclEmittenten[] emittentenListe = dbBundle.dbEmittenten.ergebnisArray;
        int anzEmittenten = dbBundle.dbEmittenten.anzErgebnis();
        for (int i = 0; i < anzEmittenten; i++) {
            EclEmittenten lEmittent = emittentenListe[i];
            dbBundle.clGlobalVar.mandant = lEmittent.mandant;
            dbBundle.clGlobalVar.hvJahr = lEmittent.hvJahr;
            dbBundle.clGlobalVar.hvNummer = lEmittent.hvNummer;
            dbBundle.clGlobalVar.datenbereich = lEmittent.dbArt;
            if (nurSelektiert) {
                dbBundle.dbMitteilung.setzeFunktion(KonstPortalFunktionen.fragen);
                dbBundle.dbMitteilung.createTable();
                dbBundle.dbMitteilung.setzeFunktion(KonstPortalFunktionen.wortmeldungen);
                dbBundle.dbMitteilung.createTable();
                dbBundle.dbMitteilung.setzeFunktion(KonstPortalFunktionen.widersprueche);
                dbBundle.dbMitteilung.createTable();
                dbBundle.dbMitteilung.setzeFunktion(KonstPortalFunktionen.antraege);
                dbBundle.dbMitteilung.createTable();
                dbBundle.dbMitteilung.setzeFunktion(KonstPortalFunktionen.sonstigeMitteilungen);
                dbBundle.dbMitteilung.createTable();
            } else {

            }

        }
        return 1;
    }

//    ALTER TABLE `db_mc878j2020ap`.`tbl_abstimmungen` 
//    ADD COLUMN `aktivPreview` INT(11) NULL DEFAULT NULL AFTER `aktiv`,
//    ADD COLUMN `aktivFragen` INT(11) NULL DEFAULT NULL AFTER `aktivBeiKIAVDauer`,
//    ADD COLUMN `aktivAntraege` INT(11) NULL DEFAULT NULL AFTER `aktivFragen`,
//    ADD COLUMN `aktivWidersprueche` INT(11) NULL DEFAULT NULL AFTER `aktivAntraege`,
//    ADD COLUMN `aktivWortmeldungen` INT(11) NULL DEFAULT NULL AFTER `aktivWidersprueche`,
//    ADD COLUMN `aktivSonstMitteilungen` INT(11) NULL DEFAULT NULL AFTER `aktivWortmeldungen`;

    /**Mandanten fest programmiert!*/
    public int updateTableVersion2021Aa() {

//        boolean nurSelektiert = true;
//        dbBundle.dbEmittenten.readAll(0);
//        EclEmittenten[] emittentenListe = dbBundle.dbEmittenten.ergebnisArray;
//        int anzEmittenten = dbBundle.dbEmittenten.anzErgebnis();
//        for (int i = 0; i < anzEmittenten; i++) {
//            EclEmittenten lEmittent = emittentenListe[i];
            dbBundle.clGlobalVar.mandant = 164;
            dbBundle.clGlobalVar.hvJahr = 2021;
            dbBundle.clGlobalVar.hvNummer = "A";
            dbBundle.clGlobalVar.datenbereich = "P";
            
            update2021AktionaereTblLoginDaten(); 
            
            entferne0BestaendeAusAktienregister();
            CaBug.druckeInfo("Null-Bestände gelöscht");
//        }
        return 1;
    }

    
    public int updateTableVersion2021c() {
        boolean nurSelektiert = true;
        DbLowLevel lDbLowLevel = new DbLowLevel(dbBundle);
        dbBundle.dbEmittenten.readAll(0);
        EclEmittenten[] emittentenListe = dbBundle.dbEmittenten.ergebnisArray;
        int anzEmittenten = dbBundle.dbEmittenten.anzErgebnis();
        for (int i = 0; i < anzEmittenten; i++) {
            EclEmittenten lEmittent = emittentenListe[i];
            dbBundle.clGlobalVar.mandant = lEmittent.mandant;
            dbBundle.clGlobalVar.hvJahr = lEmittent.hvJahr;
            dbBundle.clGlobalVar.hvNummer = lEmittent.hvNummer;
            dbBundle.clGlobalVar.datenbereich = lEmittent.dbArt;
            if (nurSelektiert) {
                /*rc=*/lDbLowLevel.rawOperation("ALTER TABLE " + dbBundle.getSchemaMandant() + "tbl_abstimmungen "

              +"ADD COLUMN `aktivPreview` INT(11) NULL DEFAULT NULL AFTER `aktiv`, "
              +"ADD COLUMN `aktivFragen` INT(11) NULL DEFAULT NULL AFTER `aktivBeiKIAVDauer`, "
              +"ADD COLUMN `aktivAntraege` INT(11) NULL DEFAULT NULL AFTER `aktivFragen`, "
              +"ADD COLUMN `aktivWidersprueche` INT(11) NULL DEFAULT NULL AFTER `aktivAntraege`, "
              +"ADD COLUMN `aktivWortmeldungen` INT(11) NULL DEFAULT NULL AFTER `aktivWidersprueche`, "
              +"ADD COLUMN `aktivSonstMitteilungen` INT(11) NULL DEFAULT NULL AFTER `aktivWortmeldungen`; "
                        );

            } else {

            }

        }
        return 1;
       
    }

    public int updateTableVersion2021d() {
        boolean nurSelektiert = true;
        dbBundle.dbEmittenten.readAll(0);
        EclEmittenten[] emittentenListe = dbBundle.dbEmittenten.ergebnisArray;
        int anzEmittenten = dbBundle.dbEmittenten.anzErgebnis();
        for (int i = 0; i < anzEmittenten; i++) {
            EclEmittenten lEmittent = emittentenListe[i];
            dbBundle.clGlobalVar.mandant = lEmittent.mandant;
            dbBundle.clGlobalVar.hvJahr = lEmittent.hvJahr;
            dbBundle.clGlobalVar.hvNummer = lEmittent.hvNummer;
            dbBundle.clGlobalVar.datenbereich = lEmittent.dbArt;
            if (nurSelektiert) {
                dbBundle.dbMitteilungBestand.createTable();

            } else {

            }

        }
        return 1;
       
    }

    public int updateTableVersion2021e() {
        boolean nurSelektiert = true;
        DbLowLevel lDbLowLevel = new DbLowLevel(dbBundle);
        /*rc=*/lDbLowLevel.rawOperation("ALTER TABLE " + dbBundle.getSchemaAllgemein() + "tbl_staaten "
                +"ADD INDEX `IDX_CODE` (`code` ASC); "
                );
       
        
        dbBundle.dbEmittenten.readAll(0);
        EclEmittenten[] emittentenListe = dbBundle.dbEmittenten.ergebnisArray;
        int anzEmittenten = dbBundle.dbEmittenten.anzErgebnis();
        for (int i = 0; i < anzEmittenten; i++) {
            EclEmittenten lEmittent = emittentenListe[i];
            dbBundle.clGlobalVar.mandant = lEmittent.mandant;
            dbBundle.clGlobalVar.hvJahr = lEmittent.hvJahr;
            dbBundle.clGlobalVar.hvNummer = lEmittent.hvNummer;
            dbBundle.clGlobalVar.datenbereich = lEmittent.dbArt;
            if (nurSelektiert) {
                /*rc=*/lDbLowLevel.rawOperation("ALTER TABLE " + dbBundle.getSchemaMandant() + "tbl_meldungen "

              +"ADD INDEX `IDX_MELDUNGSTYP` (`meldungstyp` ASC), "
              +"ADD INDEX `IDX_KLASSE` (`klasse` ASC); "
                        );

            } else {

            }

        }
        return 1;
    }

    public int updateTableVersion2021f() {
        
        dbBundle.dbEmittenten.readAll(0);
        EclEmittenten[] emittentenListe = dbBundle.dbEmittenten.ergebnisArray;
        int anzEmittenten = dbBundle.dbEmittenten.anzErgebnis();
        for (int i = 0; i < anzEmittenten; i++) {
            EclEmittenten lEmittent = emittentenListe[i];
            
            dbBundle.clGlobalVar.mandant = lEmittent.mandant;
            dbBundle.clGlobalVar.hvJahr = lEmittent.hvJahr;
            dbBundle.clGlobalVar.hvNummer = lEmittent.hvNummer;
            dbBundle.clGlobalVar.datenbereich = lEmittent.dbArt;
            
            BvReload bvReload=new BvReload(dbBundle);
            bvReload.setReloadPortalAppTexte(lEmittent.mandant);
            bvReload.setReloadTexte(lEmittent.mandant);

        }
        return 1;
    }

    public int updateTableVersion2021g() {
        
        DbLowLevel lDbLowLevel = new DbLowLevel(dbBundle);

        dbBundle.dbKontaktformularThema.mandantenabhaengig=false;
        dbBundle.dbKontaktformularThema.createTable();
        dbBundle.dbKontaktformularThema.mandantenabhaengig=true;
        
        dbBundle.dbMenueEintrag.mandantenabhaengig=false;
        dbBundle.dbMenueEintrag.createTable();
        dbBundle.dbMenueEintrag.mandantenabhaengig=true;

        dbBundle.dbAuftrag.mandantenabhaengig=false;
        dbBundle.dbAuftrag.createTable();
        dbBundle.dbAuftrag.mandantenabhaengig=true;

        dbBundle.dbNachricht.mandantenabhaengig=false;
        dbBundle.dbNachricht.createTable();
        dbBundle.dbNachricht.mandantenabhaengig=true;

        dbBundle.dbNachrichtAnhang.mandantenabhaengig=false;
        dbBundle.dbNachrichtAnhang.createTable();
        dbBundle.dbNachrichtAnhang.mandantenabhaengig=true;

        dbBundle.dbBlzBank.createTable();
        dbBundle.dbPlzOrt.createTable();
        
        /*rc=*/lDbLowLevel.rawOperation("ALTER TABLE " + dbBundle.getSchemaAllgemein() + "tbl_fehler "
        +"CHANGE COLUMN `fehlermeldung` `fehlermeldung` VARCHAR(400) NULL DEFAULT NULL; ");
       

        
        
        dbBundle.dbEmittenten.readAll(0);
        EclEmittenten[] emittentenListe = dbBundle.dbEmittenten.ergebnisArray;
        int anzEmittenten = dbBundle.dbEmittenten.anzErgebnis();
        for (int i = 0; i < anzEmittenten; i++) {
            EclEmittenten lEmittent = emittentenListe[i];
            
            dbBundle.clGlobalVar.mandant = lEmittent.mandant;
            dbBundle.clGlobalVar.hvJahr = lEmittent.hvJahr;
            dbBundle.clGlobalVar.hvNummer = lEmittent.hvNummer;
            dbBundle.clGlobalVar.datenbereich = lEmittent.dbArt;
            
            dbBundle.dbKontaktformularThema.createTable();
            dbBundle.dbMenueEintrag.createTable();
            dbBundle.dbAuftrag.createTable();
            dbBundle.dbNachricht.createTable();
            dbBundle.dbNachrichtAnhang.createTable();
            
            dbBundle.dbStaaten.mandantenabhaengig=true;
            dbBundle.dbStaaten.createTable();
            dbBundle.dbStaaten.mandantenabhaengig=false;
         
            dbBundle.dbPlzOrt.mandantenabhaengig=true;
            dbBundle.dbPlzOrt.createTable();
            dbBundle.dbPlzOrt.mandantenabhaengig=false;

            dbBundle.dbBlzBank.mandantenabhaengig=true;
            dbBundle.dbBlzBank.createTable();
            dbBundle.dbBlzBank.mandantenabhaengig=false;

//            if (lEmittent.mandant==164) {
                /*rc=*/lDbLowLevel.rawOperation("ALTER TABLE " + dbBundle.getSchemaMandant() + "tbl_logindaten "

              +"ADD COLUMN `hinweisWeitereBestaetigt` INT(11) NULL DEFAULT '0' AFTER `hinweisHVPortalBestaetigt`; "
                        );
                
                

//            } else {
//
//            }

            

            

        }
        return 1;
    }

    public int updateTableVersion2022b() {
        
        DbLowLevel lDbLowLevel = new DbLowLevel(dbBundle);

        /*
         ALTER TABLE `db_meetingcomfort`.`tbl_emittenten` 
ADD COLUMN `abteilung` VARCHAR(100) NULL DEFAULT '' AFTER `serverNummer`,
ADD COLUMN `strasseGesellschaft` VARCHAR(100) NULL DEFAULT '' AFTER `abteilung`,
ADD COLUMN `postleitzahl` VARCHAR(10) NULL DEFAULT '' AFTER `strasseGesellschaft`,
ADD COLUMN `ort` VARCHAR(100) NULL DEFAULT '' AFTER `postleitzahl`,
ADD COLUMN `staatId` INT(11) NULL DEFAULT '0' AFTER `ort`,
ADD COLUMN `telefon` VARCHAR(100) NULL DEFAULT '' AFTER `staatId`,
ADD COLUMN `fax` VARCHAR(100) NULL DEFAULT '' AFTER `telefon`,
ADD COLUMN `email` VARCHAR(100) NULL DEFAULT '' AFTER `fax`,
ADD COLUMN `hvArtSchluessel` INT(11) NULL DEFAULT '0' AFTER `email`,
ADD COLUMN `veranstaltungStrasse` VARCHAR(100) NULL DEFAULT '' AFTER `hvArtSchluessel`,
ADD COLUMN `veranstaltungPostleitzahl` VARCHAR(10) NULL DEFAULT '' AFTER `veranstaltungStrasse`,
ADD COLUMN `veranstaltungStaatId` INT(11) NULL DEFAULT '0' AFTER `veranstaltungPostleitzahl`,
ADD COLUMN `veranstaltungGebäude` VARCHAR(100) NULL DEFAULT '' AFTER `veranstaltungStaatId`,
ADD COLUMN `kuerzelInhaberImportExport` VARCHAR(15) NULL DEFAULT '' AFTER `veranstaltungGebäude`;

         */
        
        /*rc=*/lDbLowLevel.rawOperation(
                "ALTER TABLE "+dbBundle.getSchemaAllgemein()+"`tbl_emittenten`" 
                +"ADD COLUMN `abteilung` VARCHAR(100) NULL DEFAULT '' AFTER `serverNummer`, "
                +"ADD COLUMN `strasseGesellschaft` VARCHAR(100) NULL DEFAULT '' AFTER `abteilung`, "
                +"ADD COLUMN `postleitzahl` VARCHAR(10) NULL DEFAULT '' AFTER `strasseGesellschaft`, "
                +"ADD COLUMN `ort` VARCHAR(100) NULL DEFAULT '' AFTER `postleitzahl`, "
                +"ADD COLUMN `staatId` INT(11) NULL DEFAULT '0' AFTER `ort`, "
                +"ADD COLUMN `telefon` VARCHAR(100) NULL DEFAULT '' AFTER `staatId`, "
                +"ADD COLUMN `fax` VARCHAR(100) NULL DEFAULT '' AFTER `telefon`, "
                +"ADD COLUMN `email` VARCHAR(100) NULL DEFAULT '' AFTER `fax`, "
                +"ADD COLUMN `hvArtSchluessel` INT(11) NULL DEFAULT '0' AFTER `email`, "
                +"ADD COLUMN `veranstaltungStrasse` VARCHAR(100) NULL DEFAULT '' AFTER `hvArtSchluessel`, "
                +"ADD COLUMN `veranstaltungPostleitzahl` VARCHAR(10) NULL DEFAULT '' AFTER `veranstaltungStrasse`, "
                +"ADD COLUMN `veranstaltungStaatId` INT(11) NULL DEFAULT '0' AFTER `veranstaltungPostleitzahl`, "
                +"ADD COLUMN `veranstaltungGebäude` VARCHAR(100) NULL DEFAULT '' AFTER `veranstaltungStaatId`, "
                +"ADD COLUMN `kuerzelInhaberImportExport` VARCHAR(15) NULL DEFAULT '' AFTER `veranstaltungGebäude`; ");

        /*rc=*/lDbLowLevel.rawOperation(
                "ALTER TABLE "+dbBundle.getSchemaAllgemein()+"`tbl_emittenten`" 
                +"ADD COLUMN `portalIstdauerhaft` INT(11) NULL DEFAULT '0' AFTER `portalStandard` ;");

        /*rc=*/lDbLowLevel.rawOperation(
                "ALTER TABLE "+dbBundle.getSchemaAllgemein()+"`tbl_emittenten`" 
                        +"ADD COLUMN `datenbestandIstProduktiv` INT(11) NULL DEFAULT '0' AFTER `portalIstdauerhaft` ;");

        /*rc=*/lDbLowLevel.rawOperation(
                "ALTER TABLE "+dbBundle.getSchemaAllgemein()+"`tbl_parameterserver`" 
                +"CHANGE COLUMN `wert` `wert` CHAR(200) NULL DEFAULT NULL ;");


        dbBundle.dbEmittenten.readAll(0);
        EclEmittenten[] emittentenListe = dbBundle.dbEmittenten.ergebnisArray;
        int anzEmittenten = dbBundle.dbEmittenten.anzErgebnis();
        for (int i = 0; i < anzEmittenten; i++) {
            EclEmittenten lEmittent = emittentenListe[i];
            
            dbBundle.clGlobalVar.mandant = lEmittent.mandant;
            dbBundle.clGlobalVar.hvJahr = lEmittent.hvJahr;
            dbBundle.clGlobalVar.hvNummer = lEmittent.hvNummer;
            dbBundle.clGlobalVar.datenbereich = lEmittent.dbArt;
            
            if (lEmittent.mandant!=9999) {

                dbBundle.dbIsin.createTable();
                dbBundle.dbEmittenten.createTableMandantenabhaengig();
                dbBundle.dbPortalUnterlagen.createTable();
                
                dbBundle.dbNachricht.createTable();

                /*Emittenten ergänzen falls schon mit mittelneuem Stand vorhanden*/
                /*rc=*/lDbLowLevel.rawOperation(
                        "ALTER TABLE "+dbBundle.getSchemaMandant()+"`tbl_emittenten`" 
                        +"ADD COLUMN `portalIstdauerhaft` INT(11) NULL DEFAULT '0' AFTER `portalStandard` ;");
                
                /*rc=*/lDbLowLevel.rawOperation(
                        "ALTER TABLE "+dbBundle.getSchemaMandant()+"`tbl_emittenten`" 
                        +"ADD COLUMN `datenbestandIstProduktiv` INT(11) NULL DEFAULT '0' AFTER `portalIstdauerhaft` ;");

                /*rc=*/lDbLowLevel.rawOperation(
                        "ALTER TABLE "+dbBundle.getSchemaMandant()+"`tbl_logindaten`" 
                        +"ADD COLUMN `zweiFaktorAuthentifizierungAktiv` INT(11) DEFAULT '0' AFTER `email2BestaetigenLink` ;");

                /*rc=*/lDbLowLevel.rawOperation(
                        "ALTER TABLE "+dbBundle.getSchemaMandant()+"`tbl_willenserklaerung`" 
                        +"ADD COLUMN `exportiertUeberLauf` INT(11) DEFAULT '0' AFTER `erzeugtUeberLauf` ;");

                /*tbl_aktienregister*/
               
                /*
                ALTER TABLE `db_mc925j2021ap`.`tbl_aktienregister` 
                ADD COLUMN `zusatz` VARCHAR(80) NULL DEFAULT '' AFTER `name2`,
                ADD COLUMN `zusatzVersand` VARCHAR(80) NULL DEFAULT '' AFTER `name2Versand`,
                ADD COLUMN `isin` VARCHAR(12) NULL DEFAULT '' AFTER `gattungId`,
                ADD COLUMN `herkunftQuelle` INT(11) NULL DEFAULT '0' AFTER `englischerVersand`,
                ADD COLUMN `herkunftQuelleLfd` VARCHAR(30) NULL DEFAULT '' AFTER `herkunftQuelle`,
                ADD COLUMN `herkunftIdent` INT(11) NULL DEFAULT '0' AFTER `herkunftQuelleLfd`,
                DROP PRIMARY KEY,
                ADD PRIMARY KEY (`mandant`, `aktienregisterIdent`);
                */

                
                /*rc=*/lDbLowLevel.rawOperation(
                        "ALTER TABLE " + dbBundle.getSchemaMandant() + "tbl_aktienregister "
                        + "ADD COLUMN `zusatz` VARCHAR(80) NULL DEFAULT '' AFTER `name2`, "
                        + "ADD COLUMN `zusatzVersand` VARCHAR(80) NULL DEFAULT '' AFTER `name2Versand`, "
                        + "ADD COLUMN `isin` VARCHAR(12) NULL DEFAULT '' AFTER `gattungId`, "
                        + "ADD COLUMN `herkunftQuelle` INT(11) NULL DEFAULT '0' AFTER `englischerVersand`, "
                        + "ADD COLUMN `herkunftQuelleLfd` VARCHAR(30) NULL DEFAULT '' AFTER `herkunftQuelle`, "
                        + "ADD COLUMN `herkunftIdent` INT(11) NULL DEFAULT '0' AFTER `herkunftQuelleLfd` " );

                /*rc=*/lDbLowLevel.rawOperation(
                        "ALTER TABLE " + dbBundle.getSchemaMandant() + "tbl_aktienregisterhistorie "
                        + "ADD COLUMN `zusatz` VARCHAR(80) NULL DEFAULT '' AFTER `name2`, "
                        + "ADD COLUMN `zusatzVersand` VARCHAR(80) NULL DEFAULT '' AFTER `name2Versand`, "
                        + "ADD COLUMN `isin` VARCHAR(12) NULL DEFAULT '' AFTER `gattungId`, "
                        + "ADD COLUMN `herkunftQuelle` INT(11) NULL DEFAULT '0' AFTER `englischerVersand`, "
                        + "ADD COLUMN `herkunftQuelleLfd` VARCHAR(30) NULL DEFAULT '' AFTER `herkunftQuelle`, "
                        + "ADD COLUMN `herkunftIdent` INT(11) NULL DEFAULT '0' AFTER `herkunftQuelleLfd` " );
         
                /*rc=*/lDbLowLevel.rawOperation(
                        "ALTER TABLE " + dbBundle.getSchemaMandant() + "tbl_abstimmungen " 
                        +"ADD COLUMN `aktivBotschaftenEinreichen` INT(11) NULL DEFAULT NULL AFTER `aktivSonstMitteilungen`; ");
                /*rc=*/lDbLowLevel.rawOperation(
                        "ALTER TABLE " + dbBundle.getSchemaMandant() + "tbl_abstimmungen " 
                        +"ADD COLUMN `nummerUnterdruecken` INT(11) NULL DEFAULT NULL AFTER `nummerindexEN`; ");

                dbBundle.dbEmittenten.insertNurMandantenabhaengig(lEmittent);
                
                
                dbBundle.dbMitteilung.setzeFunktion(KonstPortalFunktionen.botschaftenEinreichen);
                dbBundle.dbMitteilung.createTable();
                
                
                /*rc=*/lDbLowLevel.rawOperation(
                        "ALTER TABLE "+ dbBundle.getSchemaMandant() +"tbl_frage " 
                        +"ADD COLUMN `version` INT(11) NOT NULL DEFAULT '0' AFTER `mitteilungIdent`, "
                        +"ADD COLUMN `hinweisWurdeBestaetigt` INT(11) NOT NULL DEFAULT '0' AFTER `lfdNrInListe`, "
                        +"ADD COLUMN `botschaftsart` INT(11) NOT NULL DEFAULT '0' AFTER `hinweisWurdeBestaetigt`, "
                        +"ADD COLUMN `dateiname` VARCHAR(200) NOT NULL DEFAULT '' AFTER `botschaftsart`, "
                        +"ADD COLUMN `freigegeben` INT(11) NOT NULL DEFAULT '0' AFTER `dateiname`, "
                        +"ADD COLUMN `internerDateiname` VARCHAR(20) NOT NULL DEFAULT '' AFTER `freigegeben`, "
                        +"ADD COLUMN `internerDateizusatz` VARCHAR(20) NOT NULL DEFAULT '' AFTER `internerDateiname`, "
                        +"ADD COLUMN `verweisAufUnterlagenident` INT(11) NOT NULL DEFAULT '0' AFTER `internerDateizusatz` ");
                /*rc=*/lDbLowLevel.rawOperation(
                        "ALTER TABLE "+ dbBundle.getSchemaMandant() +"tbl_wortmeldung " 
                        +"ADD COLUMN `version` INT(11) NOT NULL DEFAULT '0' AFTER `mitteilungIdent`, "
                        +"ADD COLUMN `hinweisWurdeBestaetigt` INT(11) NOT NULL DEFAULT '0' AFTER `lfdNrInListe`, "
                        +"ADD COLUMN `botschaftsart` INT(11) NOT NULL DEFAULT '0' AFTER `hinweisWurdeBestaetigt`, "
                        +"ADD COLUMN `dateiname` VARCHAR(200) NOT NULL DEFAULT '' AFTER `botschaftsart`, "
                        +"ADD COLUMN `freigegeben` INT(11) NOT NULL DEFAULT '0' AFTER `dateiname`, "
                        +"ADD COLUMN `internerDateiname` VARCHAR(20) NOT NULL DEFAULT '' AFTER `freigegeben`, "
                        +"ADD COLUMN `internerDateizusatz` VARCHAR(20) NOT NULL DEFAULT '' AFTER `internerDateiname`, "
                        +"ADD COLUMN `verweisAufUnterlagenident` INT(11) NOT NULL DEFAULT '0' AFTER `internerDateizusatz` ");
                /*rc=*/lDbLowLevel.rawOperation(
                        "ALTER TABLE "+ dbBundle.getSchemaMandant() +"tbl_widerspruch " 
                        +"ADD COLUMN `version` INT(11) NOT NULL DEFAULT '0' AFTER `mitteilungIdent`, "
                        +"ADD COLUMN `hinweisWurdeBestaetigt` INT(11) NOT NULL DEFAULT '0' AFTER `lfdNrInListe`, "
                        +"ADD COLUMN `botschaftsart` INT(11) NOT NULL DEFAULT '0' AFTER `hinweisWurdeBestaetigt`, "
                        +"ADD COLUMN `dateiname` VARCHAR(200) NOT NULL DEFAULT '' AFTER `botschaftsart`, "
                        +"ADD COLUMN `freigegeben` INT(11) NOT NULL DEFAULT '0' AFTER `dateiname`, "
                        +"ADD COLUMN `internerDateiname` VARCHAR(20) NOT NULL DEFAULT '' AFTER `freigegeben`, "
                        +"ADD COLUMN `internerDateizusatz` VARCHAR(20) NOT NULL DEFAULT '' AFTER `internerDateiname`, "
                        +"ADD COLUMN `verweisAufUnterlagenident` INT(11) NOT NULL DEFAULT '0' AFTER `internerDateizusatz` ");
                /*rc=*/lDbLowLevel.rawOperation(
                        "ALTER TABLE "+ dbBundle.getSchemaMandant() +"tbl_antrag " 
                        +"ADD COLUMN `version` INT(11) NOT NULL DEFAULT '0' AFTER `mitteilungIdent`, "
                        +"ADD COLUMN `hinweisWurdeBestaetigt` INT(11) NOT NULL DEFAULT '0' AFTER `lfdNrInListe`, "
                        +"ADD COLUMN `botschaftsart` INT(11) NOT NULL DEFAULT '0' AFTER `hinweisWurdeBestaetigt`, "
                        +"ADD COLUMN `dateiname` VARCHAR(200) NOT NULL DEFAULT '' AFTER `botschaftsart`, "
                        +"ADD COLUMN `freigegeben` INT(11) NOT NULL DEFAULT '0' AFTER `dateiname`, "
                        +"ADD COLUMN `internerDateiname` VARCHAR(20) NOT NULL DEFAULT '' AFTER `freigegeben`, "
                        +"ADD COLUMN `internerDateizusatz` VARCHAR(20) NOT NULL DEFAULT '' AFTER `internerDateiname`, "
                        +"ADD COLUMN `verweisAufUnterlagenident` INT(11) NOT NULL DEFAULT '0' AFTER `internerDateizusatz` ");
                /*rc=*/lDbLowLevel.rawOperation(
                        "ALTER TABLE "+ dbBundle.getSchemaMandant() +"tbl_sonstMitteilung " 
                        +"ADD COLUMN `version` INT(11) NOT NULL DEFAULT '0' AFTER `mitteilungIdent`, "
                        +"ADD COLUMN `hinweisWurdeBestaetigt` INT(11) NOT NULL DEFAULT '0' AFTER `lfdNrInListe`, "
                        +"ADD COLUMN `botschaftsart` INT(11) NOT NULL DEFAULT '0' AFTER `hinweisWurdeBestaetigt`, "
                        +"ADD COLUMN `dateiname` VARCHAR(200) NOT NULL DEFAULT '' AFTER `botschaftsart`, "
                        +"ADD COLUMN `freigegeben` INT(11) NOT NULL DEFAULT '0' AFTER `dateiname`, "
                        +"ADD COLUMN `internerDateiname` VARCHAR(20) NOT NULL DEFAULT '' AFTER `freigegeben`, "
                        +"ADD COLUMN `internerDateizusatz` VARCHAR(20) NOT NULL DEFAULT '' AFTER `internerDateiname`, "
                        +"ADD COLUMN `verweisAufUnterlagenident` INT(11) NOT NULL DEFAULT '0' AFTER `internerDateizusatz` ");

                /*rc=*/lDbLowLevel.rawOperation(
                        "ALTER TABLE "+ dbBundle.getSchemaMandant() +"tbl_frage " 
                                +"ADD COLUMN `gattungen` INT(11) NOT NULL DEFAULT '0' AFTER `anzahlAktienZumZeitpunktderMitteilung`, "
                                +"ADD COLUMN `interneVerarbeitungLaufend` VARCHAR(20) NOT NULL DEFAULT '' AFTER `internerDateizusatz`; ");
                /*rc=*/lDbLowLevel.rawOperation(
                        "ALTER TABLE "+ dbBundle.getSchemaMandant() +"tbl_wortmeldung " 
                                +"ADD COLUMN `gattungen` INT(11) NOT NULL DEFAULT '0' AFTER `anzahlAktienZumZeitpunktderMitteilung`, "
                                +"ADD COLUMN `interneVerarbeitungLaufend` VARCHAR(20) NOT NULL DEFAULT '' AFTER `internerDateizusatz`; ");
                /*rc=*/lDbLowLevel.rawOperation(
                        "ALTER TABLE "+ dbBundle.getSchemaMandant() +"tbl_widerspruch " 
                                +"ADD COLUMN `gattungen` INT(11) NOT NULL DEFAULT '0' AFTER `anzahlAktienZumZeitpunktderMitteilung`, "
                                +"ADD COLUMN `interneVerarbeitungLaufend` VARCHAR(20) NOT NULL DEFAULT '' AFTER `internerDateizusatz`; ");
                /*rc=*/lDbLowLevel.rawOperation(
                        "ALTER TABLE "+ dbBundle.getSchemaMandant() +"tbl_antrag " 
                                +"ADD COLUMN `gattungen` INT(11) NOT NULL DEFAULT '0' AFTER `anzahlAktienZumZeitpunktderMitteilung`, "
                                +"ADD COLUMN `interneVerarbeitungLaufend` VARCHAR(20) NOT NULL DEFAULT '' AFTER `internerDateizusatz`; ");
                /*rc=*/lDbLowLevel.rawOperation(
                        "ALTER TABLE "+ dbBundle.getSchemaMandant() +"tbl_sonstMitteilung " 
                                +"ADD COLUMN `gattungen` INT(11) NOT NULL DEFAULT '0' AFTER `anzahlAktienZumZeitpunktderMitteilung`, "
                                +"ADD COLUMN `interneVerarbeitungLaufend` VARCHAR(20) NOT NULL DEFAULT '' AFTER `internerDateizusatz`; ");
                /*rc=*/lDbLowLevel.rawOperation(
                        "ALTER TABLE "+ dbBundle.getSchemaMandant() +"tbl_botschaft " 
                                +"ADD COLUMN `gattungen` INT(11) NOT NULL DEFAULT '0' AFTER `anzahlAktienZumZeitpunktderMitteilung`, "
                                +"ADD COLUMN `interneVerarbeitungLaufend` VARCHAR(20) NOT NULL DEFAULT '' AFTER `internerDateizusatz`; ");
                
            }

        }
        return 1;
    }

    public int updateTableVersion2022c() {
        
        DbLowLevel lDbLowLevel = new DbLowLevel(dbBundle);

        dbBundle.dbUserLogin.profileVerarbeiten=true;
        dbBundle.dbUserLogin.createTable();
        dbBundle.dbUserLogin.profileVerarbeiten=false;

        dbBundle.dbUserProfile.createTable();

        /*rc=*/lDbLowLevel.rawOperation(
                "ALTER TABLE "+dbBundle.getSchemaAllgemein()+"`tbl_userlogin`" 
                +"CHANGE COLUMN `email` `email` CHAR(200) NULL DEFAULT NULL ;");

        return 1;
     }

    public int updateTableVersion2022d() {

        dbBundle.dbServiceDeskSet.createTable();
        dbBundle.dbServiceDeskWorkflow.createTable();
        
        dbBundle.dbWortmeldetischAktion.createTable();
        dbBundle.dbWortmeldetischFolgeStatus.createTable();
        dbBundle.dbWortmeldetischSet.createTable();
        dbBundle.dbWortmeldetischStatus.createTable();
        dbBundle.dbWortmeldetischStatusWeiterleitung.createTable();
        dbBundle.dbWortmeldetischView.createTable();
        dbBundle.dbWortmeldetischViewStatus.createTable();

        DbLowLevel lDbLowLevel = new DbLowLevel(dbBundle);

        
        /*rc=*/lDbLowLevel.rawOperation(
                        "ALTER TABLE "+dbBundle.getSchemaAllgemein()+"tbl_insti "
                        +"ADD COLUMN `standardSammelkarteSRV` INT(11) NULL DEFAULT '0' AFTER `standardSammelkarteOhneWeisung`, "
                        +"ADD COLUMN `standardSammelkarteBriefwahl` INT(11) NULL DEFAULT '0' AFTER `standardSammelkarteSRV`, "
                        +"ADD COLUMN `standardSammelkarteDauervollmacht` INT(11) NULL DEFAULT '0' AFTER `standardSammelkarteBriefwahl`;");

        
        lDbLowLevel.rawOperation(
                "ALTER TABLE "+dbBundle.getSchemaAllgemein()+"tbl_insti " 
                +"ADD COLUMN `standardSammelkartenAnlageAktiv` INT(11) NULL DEFAULT '1' AFTER `identSuchlaufBegriffe`, "
                +"ADD COLUMN `weisungsWeitergabe` INT(11) NULL DEFAULT '0' AFTER `standardSammelkartenAnlageAktiv`;");

        
         dbBundle.dbEmittenten.readAll(0);
        EclEmittenten[] emittentenListe = dbBundle.dbEmittenten.ergebnisArray;
        int anzEmittenten = dbBundle.dbEmittenten.anzErgebnis();
        for (int i = 0; i < /*0*/ anzEmittenten; i++) {
            EclEmittenten lEmittent = emittentenListe[i];
            
            dbBundle.clGlobalVar.mandant = lEmittent.mandant;
            dbBundle.clGlobalVar.hvJahr = lEmittent.hvJahr;
            dbBundle.clGlobalVar.hvNummer = lEmittent.hvNummer;
            dbBundle.clGlobalVar.datenbereich = lEmittent.dbArt;
            
            if (lEmittent.mandant!=9999) {

                dbBundle.dbAbstimmungMeldungSperre.createTable();
                dbBundle.dbMeldungVirtuellePraesenz.createTable();
                dbBundle.dbInfo.createTable();
                dbBundle.dbWortmeldetischProtokoll.createTable();

                
//                dbBundle.dbVeranstaltungenVeranstaltung.dropTable();
//                dbBundle.dbVeranstaltungenElement.dropTable();
//                dbBundle.dbVeranstaltungenElementDetail.dropTable();
//                dbBundle.dbVeranstaltungenAktion.dropTable();
//                dbBundle.dbVeranstaltungenQuittungElement.dropTable();
//                dbBundle.dbVeranstaltungenWert.dropTable();

                dbBundle.dbVeranstaltungenVeranstaltung.createTable();
                dbBundle.dbVeranstaltungenElement.createTable();
                dbBundle.dbVeranstaltungenElementDetail.createTable();
                dbBundle.dbVeranstaltungenAktion.createTable();
                dbBundle.dbVeranstaltungenQuittungElement.createTable();
                dbBundle.dbVeranstaltungenWert.createTable();

                /*rc=*/lDbLowLevel.rawOperation(
                        "ALTER TABLE "+ dbBundle.getSchemaMandant() +"tbl_zutrittskarten " 
                                +"DROP INDEX `cons_zutritt`; "
                                );

                /*rc=*/lDbLowLevel.rawOperation(
                        "ALTER TABLE "+ dbBundle.getSchemaMandant() +"tbl_zutrittskarten " 
                                +"CHANGE COLUMN `mandant` `mandant` INT(11) NOT NULL , "
                                +"CHANGE COLUMN `zutrittsIdent` `zutrittsIdent` CHAR(20) NOT NULL , "
                                +"CHANGE COLUMN `zutrittsIdentNeben` `zutrittsIdentNeben` CHAR(2) NOT NULL , "
                                +"CHANGE COLUMN `zutrittsIdentKlasse` `zutrittsIdentKlasse` INT(11) NOT NULL , "
                                +"CHANGE COLUMN `zutrittsIdentVers` `zutrittsIdentVers` INT(11) NOT NULL , "
                                +"CHANGE COLUMN `zutrittsIdentVers_Delayed` `zutrittsIdentVers_Delayed` INT(11) NOT NULL , "
                                +"ADD PRIMARY KEY (`mandant`, `zutrittsIdent`, `zutrittsIdentNeben`, `zutrittsIdentKlasse`, `zutrittsIdentVers`, `zutrittsIdentVers_Delayed`); "
                                );

                
                /*rc=*/lDbLowLevel.rawOperation(
                        "ALTER TABLE "+ dbBundle.getSchemaMandant() +"tbl_stimmkarten " 
                                +"DROP INDEX `cons_stimmkarte`; "
                                );

                /*rc=*/lDbLowLevel.rawOperation(
                        "ALTER TABLE "+ dbBundle.getSchemaMandant() +"tbl_stimmkarten " 
                                +"CHANGE COLUMN `mandant` `mandant` INT(11) NOT NULL , "
                                +"CHANGE COLUMN `stimmkarte` `stimmkarte` CHAR(20) NOT NULL , "
                                +"CHANGE COLUMN `stimmkarteVers` `stimmkarteVers` INT(11) NOT NULL , "
                                +"CHANGE COLUMN `stimmkarteVers_Delayed` `stimmkarteVers_Delayed` INT(11) NOT NULL , "
                                +"ADD PRIMARY KEY (`mandant`, `stimmkarte`, `stimmkarteVers`, `stimmkarteVers_Delayed`); "
                                );
                
                /*rc=*/lDbLowLevel.rawOperation(
                        "ALTER TABLE "+ dbBundle.getSchemaMandant() +"tbl_meldungenprotokoll " 
                                +"DROP PRIMARY KEY, "
                                +"ADD PRIMARY KEY (`meldungsIdent`, `mandant`, `satzArt`, `protokollIdent`); "
                                );
               
                /*rc=*/lDbLowLevel.rawOperation(
                        "ALTER TABLE "+ dbBundle.getSchemaMandant() +"tbl_frage " 
                                +"ADD COLUMN `kontaktDatenTelefon` VARCHAR(100) NOT NULL DEFAULT '' AFTER `kontaktDaten`, "
                                +"ADD COLUMN `inhaltsHinweise` INT(11) NOT NULL DEFAULT '0' AFTER `mitteilungZuTop199`; "
                                );
                /*rc=*/lDbLowLevel.rawOperation(
                        "ALTER TABLE "+ dbBundle.getSchemaMandant() +"tbl_frage " 
                                +"ADD COLUMN `raumNr` INT NOT NULL DEFAULT '0' AFTER `status`,"
                                +"ADD COLUMN `kommentarIntern` VARCHAR(500) NOT NULL DEFAULT '' AFTER `verweisAufUnterlagenident`,"
                                +"ADD COLUMN `kommentarVersammlungsleiter` VARCHAR(500) NOT NULL DEFAULT '' AFTER `kommentarIntern`;"
                                );
                /*rc=*/lDbLowLevel.rawOperation(
                        "ALTER TABLE "+ dbBundle.getSchemaMandant() +"tbl_frage " 
                                +"CHANGE COLUMN `hinweisWurdeBestaetigt` `hinweisWurdeBestaetigt` INT NOT NULL DEFAULT '0' AFTER `kontaktDatenTelefon`; "
                                );
                /*rc=*/lDbLowLevel.rawOperation(
                        "ALTER TABLE "+ dbBundle.getSchemaMandant() +"tbl_frage " 
                                +"CHANGE COLUMN `interneVerarbeitungLaufend` `interneVerarbeitungLaufend` INT NOT NULL DEFAULT '0'; "
                                );
               
                /*rc=*/lDbLowLevel.rawOperation(
                        "ALTER TABLE "+ dbBundle.getSchemaMandant() +"tbl_wortmeldung " 
                                +"ADD COLUMN `kontaktDatenTelefon` VARCHAR(100) NOT NULL DEFAULT '' AFTER `kontaktDaten`, "
                                +"ADD COLUMN `inhaltsHinweise` INT(11) NOT NULL DEFAULT '0' AFTER `mitteilungZuTop199`; "
                                );
                /*rc=*/lDbLowLevel.rawOperation(
                        "ALTER TABLE "+ dbBundle.getSchemaMandant() +"tbl_wortmeldung " 
                                +"ADD COLUMN `raumNr` INT NOT NULL DEFAULT '0' AFTER `status`,"
                                +"ADD COLUMN `kommentarIntern` VARCHAR(500) NOT NULL DEFAULT '' AFTER `verweisAufUnterlagenident`,"
                                +"ADD COLUMN `kommentarVersammlungsleiter` VARCHAR(500) NOT NULL DEFAULT '' AFTER `kommentarIntern`;"
                                );
                /*rc=*/lDbLowLevel.rawOperation(
                        "ALTER TABLE "+ dbBundle.getSchemaMandant() +"tbl_wortmeldung " 
                                +"CHANGE COLUMN `hinweisWurdeBestaetigt` `hinweisWurdeBestaetigt` INT NOT NULL DEFAULT '0' AFTER `kontaktDatenTelefon`; "
                                );
                /*rc=*/lDbLowLevel.rawOperation(
                        "ALTER TABLE "+ dbBundle.getSchemaMandant() +"tbl_wortmeldung " 
                                +"CHANGE COLUMN `interneVerarbeitungLaufend` `interneVerarbeitungLaufend` INT NOT NULL DEFAULT '0'; "
                                );

                /*rc=*/lDbLowLevel.rawOperation(
                        "ALTER TABLE "+ dbBundle.getSchemaMandant() +"tbl_widerspruch " 
                                +"ADD COLUMN `kontaktDatenTelefon` VARCHAR(100) NOT NULL DEFAULT '' AFTER `kontaktDaten`, "
                                +"ADD COLUMN `inhaltsHinweise` INT(11) NOT NULL DEFAULT '0' AFTER `mitteilungZuTop199`; "
                                );
                /*rc=*/lDbLowLevel.rawOperation(
                        "ALTER TABLE "+ dbBundle.getSchemaMandant() +"tbl_widerspruch " 
                                +"ADD COLUMN `raumNr` INT NOT NULL DEFAULT '0' AFTER `status`,"
                                +"ADD COLUMN `kommentarIntern` VARCHAR(500) NOT NULL DEFAULT '' AFTER `verweisAufUnterlagenident`,"
                                +"ADD COLUMN `kommentarVersammlungsleiter` VARCHAR(500) NOT NULL DEFAULT '' AFTER `kommentarIntern`;"
                                );
                /*rc=*/lDbLowLevel.rawOperation(
                        "ALTER TABLE "+ dbBundle.getSchemaMandant() +"tbl_widerspruch " 
                                +"CHANGE COLUMN `hinweisWurdeBestaetigt` `hinweisWurdeBestaetigt` INT NOT NULL DEFAULT '0' AFTER `kontaktDatenTelefon`; "
                                );
                /*rc=*/lDbLowLevel.rawOperation(
                        "ALTER TABLE "+ dbBundle.getSchemaMandant() +"tbl_widerspruch " 
                                +"CHANGE COLUMN `interneVerarbeitungLaufend` `interneVerarbeitungLaufend` INT NOT NULL DEFAULT '0'; "
                                );

                /*rc=*/lDbLowLevel.rawOperation(
                        "ALTER TABLE "+ dbBundle.getSchemaMandant() +"tbl_antrag " 
                                +"ADD COLUMN `kontaktDatenTelefon` VARCHAR(100) NOT NULL DEFAULT '' AFTER `kontaktDaten`, "
                                +"ADD COLUMN `inhaltsHinweise` INT(11) NOT NULL DEFAULT '0' AFTER `mitteilungZuTop199`; "
                                );
                /*rc=*/lDbLowLevel.rawOperation(
                        "ALTER TABLE "+ dbBundle.getSchemaMandant() +"tbl_antrag " 
                                +"ADD COLUMN `raumNr` INT NOT NULL DEFAULT '0' AFTER `status`,"
                                +"ADD COLUMN `kommentarIntern` VARCHAR(500) NOT NULL DEFAULT '' AFTER `verweisAufUnterlagenident`,"
                                +"ADD COLUMN `kommentarVersammlungsleiter` VARCHAR(500) NOT NULL DEFAULT '' AFTER `kommentarIntern`;"
                                );
                /*rc=*/lDbLowLevel.rawOperation(
                        "ALTER TABLE "+ dbBundle.getSchemaMandant() +"tbl_antrag " 
                                 +"CHANGE COLUMN `hinweisWurdeBestaetigt` `hinweisWurdeBestaetigt` INT NOT NULL DEFAULT '0' AFTER `kontaktDatenTelefon`; "
                                );
                /*rc=*/lDbLowLevel.rawOperation(
                        "ALTER TABLE "+ dbBundle.getSchemaMandant() +"tbl_antrag " 
                                +"CHANGE COLUMN `interneVerarbeitungLaufend` `interneVerarbeitungLaufend` INT NOT NULL DEFAULT '0'; "
                                );

                /*rc=*/lDbLowLevel.rawOperation(
                        "ALTER TABLE "+ dbBundle.getSchemaMandant() +"tbl_sonstMitteilung " 
                                +"ADD COLUMN `kontaktDatenTelefon` VARCHAR(100) NOT NULL DEFAULT '' AFTER `kontaktDaten`, "
                                +"ADD COLUMN `inhaltsHinweise` INT(11) NOT NULL DEFAULT '0' AFTER `mitteilungZuTop199`; "
                                );
                /*rc=*/lDbLowLevel.rawOperation(
                        "ALTER TABLE "+ dbBundle.getSchemaMandant() +"tbl_sonstMitteilung " 
                                +"ADD COLUMN `raumNr` INT NOT NULL DEFAULT '0' AFTER `status`,"
                                +"ADD COLUMN `kommentarIntern` VARCHAR(500) NOT NULL DEFAULT '' AFTER `verweisAufUnterlagenident`,"
                                +"ADD COLUMN `kommentarVersammlungsleiter` VARCHAR(500) NOT NULL DEFAULT '' AFTER `kommentarIntern`;"
                                );
                /*rc=*/lDbLowLevel.rawOperation(
                        "ALTER TABLE "+ dbBundle.getSchemaMandant() +"tbl_sonstMitteilung " 
                                +"CHANGE COLUMN `hinweisWurdeBestaetigt` `hinweisWurdeBestaetigt` INT NOT NULL DEFAULT '0' AFTER `kontaktDatenTelefon`; "
                                );
                /*rc=*/lDbLowLevel.rawOperation(
                        "ALTER TABLE "+ dbBundle.getSchemaMandant() +"tbl_sonstMitteilung " 
                                +"CHANGE COLUMN `interneVerarbeitungLaufend` `interneVerarbeitungLaufend` INT NOT NULL DEFAULT '0'; "
                                );

                /*rc=*/lDbLowLevel.rawOperation(
                        "ALTER TABLE "+ dbBundle.getSchemaMandant() +"tbl_botschaft " 
                                +"ADD COLUMN `kontaktDatenTelefon` VARCHAR(100) NOT NULL DEFAULT '' AFTER `kontaktDaten`, "
                                +"ADD COLUMN `inhaltsHinweise` INT(11) NOT NULL DEFAULT '0' AFTER `mitteilungZuTop199`; "
                                );
                /*rc=*/lDbLowLevel.rawOperation(
                        "ALTER TABLE "+ dbBundle.getSchemaMandant() +"tbl_botschaft " 
                                +"ADD COLUMN `raumNr` INT NOT NULL DEFAULT '0' AFTER `status`,"
                                +"ADD COLUMN `kommentarIntern` VARCHAR(500) NOT NULL DEFAULT '' AFTER `verweisAufUnterlagenident`,"
                                +"ADD COLUMN `kommentarVersammlungsleiter` VARCHAR(500) NOT NULL DEFAULT '' AFTER `kommentarIntern`;"
                                );
                /*rc=*/lDbLowLevel.rawOperation(
                        "ALTER TABLE "+ dbBundle.getSchemaMandant() +"tbl_botschaft " 
                                +"CHANGE COLUMN `hinweisWurdeBestaetigt` `hinweisWurdeBestaetigt` INT NOT NULL DEFAULT '0' AFTER `kontaktDatenTelefon`; "
                                );
                /*rc=*/lDbLowLevel.rawOperation(
                        "ALTER TABLE "+ dbBundle.getSchemaMandant() +"tbl_botschaft " 
                                +"CHANGE COLUMN `interneVerarbeitungLaufend` `interneVerarbeitungLaufend` INT NOT NULL DEFAULT '0'; "
                                );

                 
                /*rc=*/lDbLowLevel.rawOperation(
                        "ALTER TABLE "+ dbBundle.getSchemaMandant() +"tbl_logindaten " 
                                +"ADD COLUMN `konferenzTestDurchgefuehrt` INT(11) NULL DEFAULT '0' AFTER `zweiFaktorAuthentifizierungAktiv`, "
                                +"ADD COLUMN `konferenzTestAblauf` INT(11) NULL DEFAULT '0' AFTER `konferenzTestDurchgefuehrt`, "
                                +"ADD COLUMN `konferenzSprechen` INT(11) NULL DEFAULT '0' AFTER `konferenzTestAblauf`; "
                                );
              
                
               /*rc=*/lDbLowLevel.rawOperation(
                       "ALTER TABLE "+ dbBundle.getSchemaMandant() +"tbl_logindaten " 
                               +"ADD COLUMN `registrierungAktionaersPortalErfolgt` INT(11) NULL DEFAULT '0' AFTER `konferenzSprechen`, "
                               +"ADD COLUMN `registrierungAktionaersPortalErfolgtZeitstempel` BIGINT(20) NULL DEFAULT '0' AFTER `registrierungAktionaersPortalErfolgt`, "
                               +"ADD COLUMN `registrierungMitgliederPortalErfolgt` INT(11) NULL DEFAULT '0' AFTER `registrierungAktionaersPortalErfolgtZeitstempel`, "
                               +"ADD COLUMN `registrierungMitgliederPortalErfolgtZeitstempel` BIGINT(20) NULL DEFAULT '0' AFTER `registrierungMitgliederPortalErfolgt`; "
                               );
              

               /*rc=*/lDbLowLevel.rawOperation(
                       "ALTER TABLE "+ dbBundle.getSchemaMandant() +"tbl_portalunterlagen " 
                               +"ADD COLUMN `unterlagenbereichMenueNr` INT(11) NULL DEFAULT '0' AFTER `aktiv`, "
                               +"ADD COLUMN `reihenfolgeUnterlagenbereich` INT(11) NULL DEFAULT '0' AFTER `unterlagenbereichMenueNr`, "
                               +"ADD COLUMN `previewUnterlagenbereich` INT(11) NULL DEFAULT '0' AFTER `reihenfolgeUnterlagenbereich`; "
                               );
              
               /*rc=*/lDbLowLevel.rawOperation(
                       "ALTER TABLE "+ dbBundle.getSchemaMandant() +"tbl_menueEintrag " 
                               +"ADD COLUMN `funktionscodeSub` INT(11) NOT NULL DEFAULT '0' AFTER `funktionscode`, "
                               +"ADD COLUMN `textNrGespeichert` INT(11) NOT NULL DEFAULT '0' AFTER `funktionscodeSub`; "
                               );

               dbBundle.dbSuchlaufDefinition.createTable();
               dbBundle.dbLoginDatenDemo.createTable();
            }

        }
        return 1;
    }

    public int updateTableVersion2023a() {
        dbBundle.dbEmittenten.readAll(0);
        EclEmittenten[] emittentenListe = dbBundle.dbEmittenten.ergebnisArray;
        int anzEmittenten = dbBundle.dbEmittenten.anzErgebnis();
        for (int i = 0; i < /*0*/ anzEmittenten; i++) {
            EclEmittenten lEmittent = emittentenListe[i];

            dbBundle.clGlobalVar.mandant = lEmittent.mandant;
            dbBundle.clGlobalVar.hvJahr = lEmittent.hvJahr;
            dbBundle.clGlobalVar.hvNummer = lEmittent.hvNummer;
            dbBundle.clGlobalVar.datenbereich = lEmittent.dbArt;

            if (lEmittent.mandant==178) {
                dbBundle.dbZugeordneteAktionaerePraesenz.createTable();

            }
        }
        return 1;
    }

    
    public int updateTableVersion2023b() {


//        DbLowLevel lDbLowLevel = new DbLowLevel(dbBundle);
         
        dbBundle.dbEmittenten.readAll(0);
        EclEmittenten[] emittentenListe = dbBundle.dbEmittenten.ergebnisArray;
        
        int anzEmittenten = dbBundle.dbEmittenten.anzErgebnis();
        for (int i = 0; i < /*0*/ anzEmittenten; i++) {
            EclEmittenten lEmittent = emittentenListe[i];
            
            dbBundle.clGlobalVar.mandant = lEmittent.mandant;
            dbBundle.clGlobalVar.hvJahr = lEmittent.hvJahr;
            dbBundle.clGlobalVar.hvNummer = lEmittent.hvNummer;
            dbBundle.clGlobalVar.datenbereich = lEmittent.dbArt;
            
            if (ParamSpezial.ku310(lEmittent.mandant)) {

                dbBundle.dbAbstimmungku310.createTable();
            }

        }
        return 1;
    }

    public int updateTableVersion2024a() {

//        namensSuche();
//        if (1==1) {return 1;}

      DbLowLevel lDbLowLevel = new DbLowLevel(dbBundle);
      
      String hString="ALTER TABLE "+ dbBundle.getSchemaAllgemein() +"tbl_wortmeldetischView "
              + "ADD COLUMN `reihenfolgeBearbeitungZulassen` int NOT NULL DEFAULT 0 AFTER `detailBearbeitungZulassen`, "
              + "ADD COLUMN `rednerBearbeitungZulassen` int NOT NULL DEFAULT 0 AFTER `reihenfolgeBearbeitungZulassen`, "
              + "ADD COLUMN `praesenzAnzeigeAusfuehren` int NOT NULL DEFAULT 0 AFTER `rednerBearbeitungZulassen` "
              ;

      /*rc=*/lDbLowLevel.rawOperation(hString);
     
      
      dbBundle.dbEmittenten.readAll(0);
      EclEmittenten[] emittentenListe = dbBundle.dbEmittenten.ergebnisArray;
      
      int anzEmittenten = dbBundle.dbEmittenten.anzErgebnis();
      for (int i = 0; i < /*0*/ anzEmittenten; i++) {
          EclEmittenten lEmittent = emittentenListe[i];
          
          dbBundle.clGlobalVar.mandant = lEmittent.mandant;
          dbBundle.clGlobalVar.hvJahr = lEmittent.hvJahr;
          dbBundle.clGlobalVar.hvNummer = lEmittent.hvNummer;
          dbBundle.clGlobalVar.datenbereich = lEmittent.dbArt;
          
          dbBundle.dbEindeutigeKennung.createTable();
          dbBundle.dbSperre.createTable();
          dbBundle.dbSperre.initialisiere();
          
          hString="ALTER TABLE "+ dbBundle.getSchemaMandant() +"tbl_abstimmungmeldung " ;
          for (int i1=0;i1<200;i1++) {
              if (i1==0) {
                  hString = hString + "ADD COLUMN `abstimmungsweg" + Integer.toString(i1) + "` int(11) NULL DEFAULT '0' AFTER `abgabe199`, ";
              }
              else {
                  hString = hString + "ADD COLUMN `abstimmungsweg" + Integer.toString(i1) + "` int(11) NULL DEFAULT '0' AFTER `abstimmungsweg"+Integer.toString(i1-1)+"`, ";
              }
          }
          for (int i1=0;i1<200;i1++) {
              if (i1==0) {
                  hString = hString + "ADD COLUMN `abstimmungDurchSammelkarte" + Integer.toString(i1) + "` int(11) NULL DEFAULT '0' AFTER `abstimmungsweg199`, ";
              }
              else {
                  hString = hString + "ADD COLUMN `abstimmungDurchSammelkarte" + Integer.toString(i1) + "` int(11) NULL DEFAULT '0' AFTER `abstimmungDurchSammelkarte"+Integer.toString(i1-1)+"`, ";
              }
          }
          hString = hString + "ADD COLUMN `stimmabgabeDirektErfolgt` int(11) NULL DEFAULT '0' AFTER `abstimmungDurchSammelkarte199` ";
         
          /*rc=*/lDbLowLevel.rawOperation(hString);


      }
      return 1;
  }

    /*Siehe temporärer Einbau in updateTableVersion2024a()*/
   public int namensSuche() {

        String nameSuchen="V.I.P";

        dbBundle.dbEmittenten.readAll(0);
        EclEmittenten[] emittentenListe = dbBundle.dbEmittenten.ergebnisArray;
        
        int anzEmittenten = dbBundle.dbEmittenten.anzErgebnis();
        int anzGefunden=0;
        
        for (int i = 0; i < /*0*/ anzEmittenten; i++) {
            EclEmittenten lEmittent = emittentenListe[i];
            
            dbBundle.clGlobalVar.mandant = lEmittent.mandant;
            dbBundle.clGlobalVar.hvJahr = lEmittent.hvJahr;
            dbBundle.clGlobalVar.hvNummer = lEmittent.hvNummer;
            dbBundle.clGlobalVar.datenbereich = lEmittent.dbArt;
            
            int anz=dbBundle.dbAktienregister.leseNamensfelder(nameSuchen);
            for (int i1=0;i1<anz;i1++) {
                EclAktienregister lAktienregister=dbBundle.dbAktienregister.ergebnisPosition(i1);
                System.out.println("tbl_aktienregister "+dbBundle.getSchemaMandant()+" "+lAktienregister.nachname+" "+
                lAktienregister.name1+" "+lAktienregister.vorname+" "+lAktienregister.name2+" "+lAktienregister.zusatz);
            }
            anzGefunden+=anz;

            anz=dbBundle.dbPersonenNatJur.readNamensfelder(nameSuchen);
            for (int i1=0;i1<anz;i1++) {
                EclPersonenNatJur lPersonenNatJur=dbBundle.dbPersonenNatJur.PersonNatJurGefunden(i1);
                
                int personIdent=lPersonenNatJur.ident;
                int meldungGefunden=dbBundle.dbMeldungen.leseZuPersonenNatJurIdent(personIdent);
                boolean druckenUndefiniert=false;
                boolean druckenSammelkarteMitBestand=false;
                if (meldungGefunden>0) {
                    EclMeldung lMeldung=dbBundle.dbMeldungen.meldungenArray[0];
                    if (lMeldung.meldungstyp==2) {
                       if (lMeldung.stueckAktien!=0) {
                           druckenSammelkarteMitBestand=true;
                       }
                    }
                    else {
                        druckenUndefiniert=true;
                    }
                }
                else {
                    druckenUndefiniert=true;
                }
                if (druckenUndefiniert) {
                    System.out.println("Undefiniert");
                }
                if (druckenSammelkarteMitBestand) {
                    System.out.println("Sammelkarte Mit Bestand");
                }
                if (druckenUndefiniert || druckenSammelkarteMitBestand) {
                    System.out.println("tbl_personennatjur "+dbBundle.getSchemaMandant()+" "+lPersonenNatJur.name+" "+
                            lPersonenNatJur.vorname+" "+lPersonenNatJur.zuHdCo+" "+lPersonenNatJur.zusatz1+" "+lPersonenNatJur.zusatz2);
                }
            }

            anzGefunden+=anz;

        }
        System.out.println("anzGefunden="+anzGefunden);
        return 1;
    }

    
    public int updateTableVersion2024b() {
        dbBundle.dbMailingStatus.createTable();
        
        dbBundle.dbEmittenten.readAll(0);
        EclEmittenten[] emittentenListe = dbBundle.dbEmittenten.ergebnisArray;
        
        int anzEmittenten = dbBundle.dbEmittenten.anzErgebnis();
        for (int i = 0; i < /*0*/ anzEmittenten; i++) {
            EclEmittenten lEmittent = emittentenListe[i];
            
            dbBundle.clGlobalVar.mandant = lEmittent.mandant;
            dbBundle.clGlobalVar.hvJahr = lEmittent.hvJahr;
            dbBundle.clGlobalVar.hvNummer = lEmittent.hvNummer;
            dbBundle.clGlobalVar.datenbereich = lEmittent.dbArt;
        
            dbBundle.dbMailing.createTable();
            dbBundle.dbMailing.initialisiere();

            dbBundle.dbVeranstaltungenMenue.createTable();
            dbBundle.dbVeranstaltungenReportElement.createTable();

        }
        return 1;
    }

    public int updateTableVersion2025a() {
        
        dbBundle.openParamStrukt();
        dbBundle.dbParamStrukt.createTable();
        dbBundle.dbParamStruktAblaufElement.createTable();
        dbBundle.dbParamStruktAblaufHeader.createTable();
        dbBundle.dbParamStruktGruppen.createTable();
        dbBundle.dbParamStruktGruppenHeader.createTable();
        dbBundle.dbParamStruktPresetArt.createTable();
        dbBundle.dbParamStruktStandard.createTable();
        dbBundle.dbParamStruktVersammlungstyp.createTable();
        dbBundle.dbParamStruktWerte.createTable();
        
        dbBundle.dbEmittenten.readAll(0);
        EclEmittenten[] emittentenListe = dbBundle.dbEmittenten.ergebnisArray;
        
        int anzEmittenten = dbBundle.dbEmittenten.anzErgebnis();
        for (int i = 0; i < /*0*/ anzEmittenten; i++) {
            EclEmittenten lEmittent = emittentenListe[i];
            
            dbBundle.clGlobalVar.mandant = lEmittent.mandant;
            dbBundle.clGlobalVar.hvJahr = lEmittent.hvJahr;
            dbBundle.clGlobalVar.hvNummer = lEmittent.hvNummer;
            dbBundle.clGlobalVar.datenbereich = lEmittent.dbArt;
        
            dbBundle.dbVorlaeufigeVollmachtEingabe.createTable();

        }

        return 1;
    }

    
    /*
     * 2022d
     * create DbSuchlaufDefinition
     */
    
    /**Mandantenbelegung und Open erfolgt in aufrufender Funktion*/
//    private void update2021AktienregisterTables() {
//        DbLowLevel lDbLowLevel = new DbLowLevel(dbBundle);
//
//        /*tbl_aktienregister*/
//        /*rc=*/lDbLowLevel.rawOperation("ALTER TABLE " + dbBundle.getSchemaMandant() + "tbl_aktienregister "
//                + "ADD COLUMN `gruppe` INT(11) NULL DEFAULT '0' AFTER `stimmausschluss`, " + "DROP PRIMARY KEY, "
//                + "ADD PRIMARY KEY (`mandant`, `aktienregisterIdent`); ");
//        /*rc=*/lDbLowLevel.rawOperation("ALTER TABLE " + dbBundle.getSchemaMandant() + "tbl_aktienregister "
//                + "ADD COLUMN `personNatJur` INT(11) NULL DEFAULT '0' AFTER `aktionaersnummer`, " + "DROP PRIMARY KEY, "
//                + "ADD PRIMARY KEY (`mandant`, `aktienregisterIdent`); ");
//
//        /*tbl_aktienregisterhistorie*/
//        /*rc=*/lDbLowLevel.rawOperation("ALTER TABLE " + dbBundle.getSchemaMandant() + "tbl_aktienregisterhistorie "
//                + "ADD COLUMN `gruppe` INT(11) NULL DEFAULT '0' AFTER `stimmausschluss`; ");
//        /*rc=*/lDbLowLevel.rawOperation("ALTER TABLE " + dbBundle.getSchemaMandant() + "tbl_aktienregisterhistorie "
//                + "ADD COLUMN `personNatJur` INT(11) NULL DEFAULT '0' AFTER `aktionaersnummer`; ");
//
//        dbBundle.dbLoginDaten.createTable();
//
//    }

    /**Mandantenbelegung und Open erfolgt in aufrufender Funktion*/
    public void update2021AktionaereTblLoginDaten() {

//        CaBug.druckeLog("Start", logDrucken, 10);
//        update2021AktienregisterTables();
//        CaBug.druckeLog("nach Update", logDrucken, 10);
//        dbBundle.dbAktienregister.leseAlle();
//        CaBug.druckeLog("nach leseAlle", logDrucken, 10);
//        for (int i = 0; i < dbBundle.dbAktienregister.anzErgebnis(); i++) {
//            EclAktienregister lAktienregister = dbBundle.dbAktienregister.ergebnisPosition(i);
//
//            dbBundle.dbAktienregisterLoginDaten.readIdent(lAktienregister.aktienregisterIdent);
//            EclAktienregisterLoginDaten lAktienregisterLoginDaten = dbBundle.dbAktienregisterLoginDaten
//                    .ergebnisPosition(0);
//
//            EclAktienregisterZusatz lAktienregisterZusatz = new EclAktienregisterZusatz();
//            lAktienregisterZusatz.aktienregisterIdent = lAktienregister.aktienregisterIdent;
//            dbBundle.dbAktienregisterZusatz.read(lAktienregisterZusatz);
//            if (dbBundle.dbAktienregisterZusatz.anzErgebnis() == 0) {
//                lAktienregisterZusatz = null;
//            } else {
//                lAktienregisterZusatz = dbBundle.dbAktienregisterZusatz.ergebnisPosition(0);
//            }
//
//            EclLoginDaten lLoginDaten = new EclLoginDaten();
//
//            lLoginDaten.loginKennung = lAktienregister.aktionaersnummer;
//            lLoginDaten.kennungArt = KonstLoginKennungArt.aktienregister;
//            lLoginDaten.aktienregisterIdent = lAktienregister.aktienregisterIdent;
//
//            lLoginDaten.passwortVerschluesselt = lAktienregisterLoginDaten.passwortVerschluesselt;
//            lLoginDaten.passwortInitial = lAktienregisterLoginDaten.passwortInitial;
//            lLoginDaten.anmeldenUnzulaessig = lAktienregisterLoginDaten.anmeldenUnzulaessig;
//            lLoginDaten.dauerhafteRegistrierungUnzulaessig = lAktienregisterLoginDaten.dauerhafteRegistrierungUnzulaessig;
//            lLoginDaten.berechtigungPortal = 0;
//
//            if (lAktienregisterZusatz != null) {
//                lLoginDaten.hinweisAktionaersPortalBestaetigt = lAktienregisterZusatz.hinweisAktionaersPortalBestaetigt;
//                lLoginDaten.hinweisHVPortalBestaetigt = lAktienregisterZusatz.hinweisHVPortalBestaetigt;
//                lLoginDaten.eVersandRegistrierung = lAktienregisterZusatz.eMailRegistrierung;
//                lLoginDaten.eVersandRegistrierungErstZeitpunkt = lAktienregisterZusatz.eMailRegistrierungErstZeitpunkt;
//                lLoginDaten.eigenesPasswort = lAktienregisterZusatz.eigenesPasswort;
//                lLoginDaten.passwortVergessenLink = lAktienregisterZusatz.passwortVergessenLink;
//                lLoginDaten.passwortVergessenZeitpunkt = lAktienregisterZusatz.passwortVergessenZeitpunkt;
//                lLoginDaten.kommunikationssprache = lAktienregisterZusatz.kommunikationssprache;
//                lLoginDaten.eMailFuerVersand = lAktienregisterZusatz.eMailFuerVersand;
//                lLoginDaten.emailBestaetigt = lAktienregisterZusatz.emailBestaetigt;
//                lLoginDaten.emailBestaetigenLink = lAktienregisterZusatz.emailBestaetigenLink;
//                lLoginDaten.eMail2FuerVersand = lAktienregisterZusatz.eMail2FuerVersand;
//                lLoginDaten.email2Bestaetigt = lAktienregisterZusatz.email2Bestaetigt;
//                lLoginDaten.email2BestaetigenLink = lAktienregisterZusatz.email2BestaetigenLink;
//            }
//
//            dbBundle.dbLoginDaten.insert(lLoginDaten);
//            if (i % 500 == 0) {
//                CaBug.druckeInfo("update2021AktionaereTblLoginDaten Daten umgeformt " + i 
//                        + CaDatumZeit.DatumZeitStringFuerDatenbank());
//            }
//
//        }
    }

    private void hVersion3ParameterJeMandant(int pMandant) {

        /*pLocalAktienregisterImportZulaessig aus DB raus*/
        dbBundle.dbParameter.deleteParameterLocal(pMandant, 18);

        /*geraeteSetIdent aus parameter in parameterServer übertragen*/
        hCopyParameterParameterZuServer(pMandant, 74, 51);

        /*pLocalPraefixLink aus parameterLocal in parameterServer übertragen*/
        hCopyParameterLocalZuServer(pMandant, 6, 101);
        /*pLocalPraefixLinkAlternativ aus parameterLocal in parameterServer übertragen*/
        hCopyParameterLocalZuServer(pMandant, 7, 102);

        /*pLocalInternPasswort aus parameterLocal in parameterServer übertragen*/
        hCopyParameterLocalZuServer(pMandant, 15, 105);

    }

    /**Kopieren eines Parameter von tbl_parameter zu tbl_parameterServer. Danach Löschen*/
    private void hCopyParameterParameterZuServer(int pMandant, int pVonLocalIdent, int pZuSerververIdent) {
        int rc = 0;
        int hMandant = dbBundle.clGlobalVar.mandant;
        EclParameter lParameter = new EclParameter();
        lParameter.ident = pVonLocalIdent;
        lParameter.mandant = hMandant;
        dbBundle.dbParameter.read(lParameter);
        if (dbBundle.dbParameter.anzErgebnis() == 0) {
            lParameter.mandant = 0;
            dbBundle.dbParameter.read(lParameter);
        }
        if (dbBundle.dbParameter.anzErgebnis() > 0) {
            dbBundle.clGlobalVar.mandant = pMandant;
            lParameter = dbBundle.dbParameter.ergebnisPosition(0);
            lParameter.mandant = 0;
            lParameter.ident = pZuSerververIdent;
            rc = dbBundle.dbParameter.updateServer(lParameter);
            if (rc < 1) {
                dbBundle.dbParameter.insertServer(lParameter);
            }
        }
        dbBundle.dbParameter.delete(hMandant, pVonLocalIdent);
        dbBundle.clGlobalVar.mandant = hMandant;

    }

    /**Kopieren eines Parameter von tbl_parameterlocal zu tbl_parameterServer. Danach Löschen*/
    private void hCopyParameterLocalZuServer(int pMandant, int pVonLocalIdent, int pZuSerververIdent) {
        int rc = 0;
        int hMandant = dbBundle.clGlobalVar.mandant;
        EclParameter lParameter = new EclParameter();
        lParameter.ident = pVonLocalIdent;
        lParameter.mandant = hMandant;
        dbBundle.dbParameter.readLocal(lParameter);
        if (dbBundle.dbParameter.anzErgebnis() == 0) {
            lParameter.mandant = 0;
            dbBundle.dbParameter.readLocal(lParameter);
        }
        if (dbBundle.dbParameter.anzErgebnis() > 0) {
            dbBundle.clGlobalVar.mandant = pMandant;
            lParameter = dbBundle.dbParameter.ergebnisPosition(0);
            lParameter.mandant = 0;
            lParameter.ident = pZuSerververIdent;
            rc = dbBundle.dbParameter.updateServer(lParameter);
            if (rc < 1) {
                dbBundle.dbParameter.insertServer(lParameter);
            }
        }
        dbBundle.dbParameter.delete(hMandant, pVonLocalIdent);
        dbBundle.clGlobalVar.mandant = hMandant;

    }

    /**Löscht alle Mandantenabhängigen Inhalte (Tables bleiben erhalten)*/
    public int deleteAllMandantenTablesInhalte() {
        dbBundle.dbAbstimmungen.deleteAll();
        dbBundle.dbAbstimmungenEinzelAusschluss.deleteAll();
        dbBundle.dbAbstimmungenZuStimmkarte.deleteAll();
        dbBundle.dbAbstimmungMeldung.deleteAll();
        dbBundle.dbAbstimmungMeldungRaw.deleteAll();
        dbBundle.dbAbstimmungMeldungSplit.deleteAll();
        dbBundle.dbAbstimmungsblock.deleteAll();
        dbBundle.dbAbstimmungsmonitorEK.deleteAll();
        dbBundle.dbAbstimmungsVorschlag.deleteAll();
        dbBundle.dbAbstimmungZuAbstimmungsblock.deleteAll();
        dbBundle.dbAbstimmungsVorschlagEmpfehlung.deleteAll();
        dbBundle.dbAenderungslog.deleteAll();
        dbBundle.dbAktienregister.deleteAll();
        // Historie
        dbBundle.dbAktienregisterErgaenzung.deleteAll();
        //MailRuecklauf
        dbBundle.dbAuftrag.deleteAll();
        dbBundle.dbAusstellungsgrund.deleteAll_Mandant();
        // dbAutoTest
        dbBundle.dbBestWorkflow.deleteAll();
        
        dbBundle.dbBlzBank.mandantenabhaengig=true;
        dbBundle.dbBlzBank.deleteAll();
        dbBundle.dbBlzBank.mandantenabhaengig=false;

        dbBundle.dbDrucklauf.deleteAll();
        // Emittenten mandantenabhängig
        dbBundle.dbGruppen.deleteAll_mandant();
        dbBundle.dbHVDatenLfd.deleteAll();
        
        dbBundle.dbInstiBestandsZuordnung.deleteAll();
        dbBundle.dbInstiProv.deleteAll();
        dbBundle.dbInstiSubZuordnung.deleteAll(false);
        dbBundle.dbIpTracking.deleteAll();
        dbBundle.dbIsin.deleteAll();
        dbBundle.dbKTracking.deleteAll();
        dbBundle.dbKonfigAuswertung.deleteAll();
        dbBundle.dbKontaktformularThema.deleteAll();
        dbBundle.dbLoginDaten.deleteAll();

        dbBundle.dbMeldungen.deleteAll();
        dbBundle.dbMeldungAusstellungsgrund.deleteAll();
        dbBundle.dbMeldungenMeldungen.deleteAll();
        dbBundle.dbMeldungVirtuellePraesenz.deleteAll();
        dbBundle.dbMeldungVipKZ.deleteAll();
        dbBundle.dbMeldungenVipKZAusgeblendet.deleteAll();
        dbBundle.dbMeldungZuSammelkarte.deleteAll();
        
        //dbMenueEintrag

        dbBundle.dbMitteilung.setzeFunktion(KonstPortalFunktionen.fragen);
        dbBundle.dbMitteilung.deleteAll();
        dbBundle.dbMitteilung.setzeFunktion(KonstPortalFunktionen.wortmeldungen);
        dbBundle.dbMitteilung.deleteAll();
        dbBundle.dbMitteilung.setzeFunktion(KonstPortalFunktionen.widersprueche);
        dbBundle.dbMitteilung.deleteAll();
        dbBundle.dbMitteilung.setzeFunktion(KonstPortalFunktionen.antraege);
        dbBundle.dbMitteilung.deleteAll();
        dbBundle.dbMitteilung.setzeFunktion(KonstPortalFunktionen.sonstigeMitteilungen);
        dbBundle.dbMitteilung.deleteAll();
        dbBundle.dbMitteilungBestand.deleteAll();

        dbBundle.dbNummernkreis.deleteAll(); //Löscht nur Mandanten
        
        //		dbBundle.dbParameter.deleteAll_parameter();
        //		dbBundle.dbParameter.deleteAll_parameterLang();
        //		dbBundle.dbParameter.deleteAll_parameterLfd();
        //		dbBundle.dbParameter.deleteAll_parameterLocal();

        //dbPersonen
        
        
        dbBundle.dbPersonenNatJur.deleteAll();
        dbBundle.dbPersonenNatJurVersandadresse.deleteAll();
        dbBundle.dbPersonenprognose.deleteAll();
        
        dbBundle.dbPlzOrt.mandantenabhaengig=true;
        dbBundle.dbPlzOrt.deleteAll();
        dbBundle.dbPlzOrt.mandantenabhaengig=false;
        
        //		dbBundle.dbPortalTexte.deleteAll(true);
        //      dbBundle.dbPortalUnterlagen.deleteAll(true);

        dbBundle.dbPraesenz.deleteAll();
        dbBundle.dbPraesenzliste.deleteAll();
        dbBundle.dbPublikation.deleteAll();
        
        dbBundle.dbStaaten.mandantenabhaengig=true;
        dbBundle.dbStaaten.deleteAll();
        dbBundle.dbStaaten.mandantenabhaengig=false;
        
        dbBundle.dbStimmkarteInhalt.deleteAll();
        dbBundle.dbStimmkarten.deleteAll();
        dbBundle.dbStimmkartenSecond.deleteAll();
        dbBundle.dbSuchlaufErgebnis.deleteAll();
        
        dbBundle.dbTeilnehmerStandVerein.deleteAll();
        //		dbBundle.dbTermine.deleteAll();
        dbBundle.dbVeranstaltung.deleteAll();
        
        //dbVeranstaltungen* werden bewußt nicht zurückgesetzt!
        
        
        dbBundle.dbVorlaeufigeVollmacht.deleteAll();
        dbBundle.dbVorlaeufigeVollmachtEingabe.deleteAll();
        
        dbBundle.dbWeisungMeldung.deleteAll();
        dbBundle.dbWillenserklaerung.deleteAll();
        dbBundle.dbWillenserklaerungZusatz.deleteAll();
        dbBundle.dbZuordnungKennung.deleteAll();
        dbBundle.dbZutrittskarten.deleteAll();
        
        dbBundle.dbBasis.deleteAll(); /*Eigentlich auch Konfigurationstble - noch zu überdenken*/

        /*Nicht zurückgesetzt, da mandantenübergreifend aktuell, sind ("delete" dort auch nicht implementiert):
         * DbAnreden
         * DbAusstellungsgrund
         * DbGruppen
         * DbKommunikationsSprachen
         * DbSprachen
         * DbVertretungsArten
         * DbVipKZ
         */
        /*Nicht zurückgesetzt, da Konfigurationstables:
         * DbFehler
         * DbParameter
         * DbUserLogin
         */

        return 1;
    }

    /**überschreibt in allen Mandantentables die gespeicherte Mandantenummer mit der aktuellen.
     * Dient zum "Nacharbeiten" der Tables nach einem Mandanten-Copy.
     * Achtung!!! Fehlertexte!!!!
     * 
     * Überprüft am 04.09.2024
     * */
    public int updateAllMandantenTablesMandantenNummer() {
        /*dbAbstimmungku310*/
        dbBundle.dbAbstimmungen.updateMandant();
        dbBundle.dbAbstimmungenEinzelAusschluss.updateMandant();
        dbBundle.dbAbstimmungenZuStimmkarte.updateMandant();
        dbBundle.dbAbstimmungMeldung.updateMandant();
        dbBundle.dbAbstimmungMeldungArchiv.updateMandant();
        dbBundle.dbAbstimmungMeldungRaw.updateMandant();
        /*dbAbstimmungmeldungsperre*/
        dbBundle.dbAbstimmungMeldungSplit.updateMandant();
        dbBundle.dbAbstimmungsblock.updateMandant();
        dbBundle.dbAbstimmungsmonitorEK.updateMandant();
        dbBundle.dbAbstimmungsVorschlag.updateMandant();
        dbBundle.dbAbstimmungsVorschlagEmpfehlung.updateMandant();
        dbBundle.dbAbstimmungZuAbstimmungsblock.updateMandant();
        dbBundle.dbAenderungslog.updateMandant();
        dbBundle.dbAktienregister.updateMandant();
        dbBundle.dbAktienregisterErgaenzung.updateMandant();
        /*dbAktienregisterLoginDaten*/
        dbBundle.dbAktienregisterMailRuecklauf.updateMandant();
        /*dbAktienregisterHistorie bei dbAktienregister mit enthalten*/
        dbBundle.dbAktienregisterMailRuecklauf.updateMandant();
        /*dbAktienregisterZusatz*/
        dbBundle.dbAktienregisterUpdate.updateMandant();
        /*antrag*/
        dbBundle.dbAuftrag.updateMandant();
        dbBundle.dbAusstellungsgrund.updateMandant();
        dbBundle.dbAutoTest.updateMandant();
        
        dbBundle.dbBestWorkflow.updateMandant();
        /*dbBlzBank*/
        /*dbBotschaft*/
        
        /*dbInhaberImportAnmeldedaten*/
        dbBundle.dbDrucklauf.updateMandant();
        dbBundle.dbEmittenten.updateMandant();
        
        /*frage*/
        
        dbBundle.dbGruppen.updateMandant();
        dbBundle.dbHVDatenLfd.updateMandant();
        
        /*dbInfo*/
        
        dbBundle.dbInstiBestandsZuordnung.updateMandant();
        dbBundle.dbInstiProv.updateMandant();
        dbBundle.dbInstiSubZuordnung.updateMandant();
        dbBundle.dbIpTracking.updateMandant();
        dbBundle.dbKTracking.updateMandant();
        dbBundle.dbKonfigAuswertung.updateMandant();
        dbBundle.dbKonfigAuswertung.updateMandant();
        /*dbLoginDatenDemo*/
        /*dbMailing*/
        dbBundle.dbMeldungen.updateMandant();
        dbBundle.dbMeldungAusstellungsgrund.updateMandant();
        dbBundle.dbMeldungenMeldungen.updateMandant();
        /*meldungenProtokoll in meldungen enthalten*/
        dbBundle.dbMeldungVipKZ.updateMandant();
        dbBundle.dbMeldungenVipKZAusgeblendet.updateMandant();
        dbBundle.dbMeldungZuSammelkarte.updateMandant();
        
        dbBundle.dbNachricht.updateMandant();
        dbBundle.dbNachrichtAnhang.updateMandant();

        dbBundle.dbNummernkreis.updateMandant(); //Löscht nur Mandanten
        
        dbBundle.dbParameter.updateMandant();

        /*personen*/
        dbBundle.dbPersonenNatJur.updateMandant();
        dbBundle.dbPersonenNatJurVersandadresse.updateMandant();
//       dbBundle.dbPersonenprognose.updateMandant();
        /*personenwar*/
        
//        dbBundle.dbPlzOrt.updateMandant();
       
        dbBundle.dbPortalTexte.updateMandant();
//        dbBundle.dbPortalUnterlagen.createTable();
        
        //		dbBundle.dbPraesenz.updateMandant();
        dbBundle.dbPraesenzliste.updateMandant();
        dbBundle.dbPublikation.updateMandant();
        
        /*sonstMitteilung*/
        /*dbSperre*/
        /*staaten*/
        
        dbBundle.dbStimmkarteInhalt.updateMandant();
        dbBundle.dbStimmkarten.updateMandant();
        dbBundle.dbStimmkartenSecond.updateMandant();
        dbBundle.dbSuchlaufDefinition.updateMandant();
        dbBundle.dbSuchlaufErgebnis.updateMandant();
        
        dbBundle.dbTeilnehmerStandVerein.updateMandant();
        dbBundle.dbTermine.updateMandant();
        dbBundle.dbVeranstaltung.updateMandant();
        /*veranstaltungenveranstaltung*/
        /*veranstaltungenelement*/
        /*veranstaltungenelementdetail*/
        /*dbVeranstaltungenMenue*/
       /*veranstaltungenaktion*/
        /*veranstaltungenquittungelement*/
        /*veranstaltungenwert*/
        /*dbVeranstaltungenReportElement*/
//        dbBundle.dbVorlaeufigeVollmacht.updateMandant();
        dbBundle.dbWeisungMeldung.updateMandant();
        /*widerspruch*/
        dbBundle.dbWillenserklaerung.updateMandant();
        dbBundle.dbWillenserklaerungZusatz.updateMandant();
        /*wortmeldetischprotokoll*/
        /*wortmeldung*/
        
        /*dnbZugeordneteKennung*/
        dbBundle.dbZutrittskarten.updateMandant();
        //	dbBundle.dbBasis.updateMandant(); /*In Nummernkreis enthalten Eigentlich auch Konfigurationstble - noch zu überdenken*/

        /*Nicht zurückgesetzt, da mandantenübergreifend aktuell, sind ("delete" dort auch nicht implementiert):
         * DbAnreden
         * DbAusstellungsgrund
         * DbGruppen
         * DbKommunikationsSprachen
         * DbSprachen
         * DbVertretungsArten
         * DbVipKZ
         */
        /*Nicht zurückgesetzt, da Konfigurationstables:
         * DbFehler
         * DbParameter
         * DbUserLogin
         */

        return 1;
    }

    public boolean legeMandantenTablesAn() {
        CaBug.druckeLog("Neuanlage Start für Mandant " + dbBundle.clGlobalVar.mandant, logDrucken, 3);

        dbBundle.openWeitere();

        dbBundle.dbAbstimmungen.createTable();
        dbBundle.dbAbstimmungenEinzelAusschluss.createTable();
        dbBundle.dbAbstimmungenZuStimmkarte.createTable();
        dbBundle.dbAbstimmungMeldung.createTable();
        dbBundle.dbAbstimmungMeldungRaw.createTable();
        dbBundle.dbAbstimmungMeldungSperre.createTable();
        dbBundle.dbAbstimmungMeldungSplit.createTable();
        dbBundle.dbAbstimmungsblock.createTable();
        dbBundle.dbAbstimmungsmonitorEK.createTable();
        dbBundle.dbAbstimmungsVorschlag.createTable();
        dbBundle.dbAbstimmungsVorschlagEmpfehlung.createTable();
        dbBundle.dbAbstimmungZuAbstimmungsblock.createTable();
        dbBundle.dbAenderungslog.createTable();
        dbBundle.dbAktienregister.createTable();
        dbBundle.dbAktienregister.createTable_aktienregisterHistorie();
        dbBundle.dbAktienregisterErgaenzung.createTable();
        dbBundle.dbAktienregisterMailRuecklauf.createTable();
        dbBundle.dbAuftrag.createTable();
        dbBundle.dbAusstellungsgrund.createTable_mandant();
        dbBundle.dbAutoTest.createTable();
        dbBundle.dbBestWorkflow.createTable();
        
        dbBundle.dbBlzBank.mandantenabhaengig=true;
        dbBundle.dbBlzBank.createTable();
        dbBundle.dbBlzBank.mandantenabhaengig=false;
        
        dbBundle.dbInhaberImportAnmeldedaten.createTable();
        dbBundle.dbDrucklauf.createTable();
        dbBundle.dbEindeutigeKennung.createTable(); //Anschließend durch Modul Werte vorbelegen
        dbBundle.dbEmittenten.createTableMandantenabhaengig();
        dbBundle.dbGruppen.createTable_mandant();
        dbBundle.dbHVDatenLfd.createTable();
        
        dbBundle.dbInfo.createTable();
        
        dbBundle.dbInstiBestandsZuordnung.createTable();
        dbBundle.dbInstiProv.createTable();
        dbBundle.dbInstiSubZuordnung.createTable(false);
        dbBundle.dbIpTracking.createTable();
        dbBundle.dbIsin.createTable();
        dbBundle.dbKTracking.createTable();
        dbBundle.dbKonfigAuswertung.createTable();
        dbBundle.dbKontaktformularThema.createTable();
        dbBundle.dbLoginDaten.createTable();
        dbBundle.dbLoginDatenDemo.createTable();
        
        dbBundle.dbMailing.createTable();
        dbBundle.dbMailing.initialisiere();
        
        dbBundle.dbMeldungen.createTable(); /*Beinhaltet auch tbl_meldungenprotokoll*/
        dbBundle.dbMeldungAusstellungsgrund.createTable();
        dbBundle.dbMeldungenMeldungen.createTable();
        dbBundle.dbMeldungVipKZ.createTable();
        dbBundle.dbMeldungenVipKZAusgeblendet.createTable();
        dbBundle.dbMeldungVirtuellePraesenz.createTable();
        dbBundle.dbMeldungZuSammelkarte.createTable();
        
        dbBundle.dbMenueEintrag.createTable();
        
        dbBundle.dbMitteilung.setzeFunktion(KonstPortalFunktionen.fragen);
        dbBundle.dbMitteilung.createTable();
        dbBundle.dbMitteilung.setzeFunktion(KonstPortalFunktionen.wortmeldungen);
        dbBundle.dbMitteilung.createTable();
        dbBundle.dbMitteilung.setzeFunktion(KonstPortalFunktionen.widersprueche);
        dbBundle.dbMitteilung.createTable();
        dbBundle.dbMitteilung.setzeFunktion(KonstPortalFunktionen.antraege);
        dbBundle.dbMitteilung.createTable();
        dbBundle.dbMitteilung.setzeFunktion(KonstPortalFunktionen.sonstigeMitteilungen);
        dbBundle.dbMitteilung.createTable();
        dbBundle.dbMitteilung.setzeFunktion(KonstPortalFunktionen.botschaftenEinreichen);
        dbBundle.dbMitteilung.createTable();

        dbBundle.dbMitteilungBestand.createTable();
        
        dbBundle.dbNachricht.createTable();
        dbBundle.dbNachrichtAnhang.createTable();
        
        dbBundle.dbNummernkreis.createTable();
        
        dbBundle.dbParameter.createTable_parameter();
        dbBundle.dbParameter.createTable_parameterLang();
        dbBundle.dbParameter.createTable_parameterLfd();
        dbBundle.dbParameter.createTable_parameterLocal();

        DbPersonen dbPersonen = new DbPersonen(dbBundle);
        dbPersonen.createTable(); /*Beinhaltet auch tbl_personenwar*/
        
        dbBundle.dbPersonenNatJur.createTable(); /*Beinhaltet auch personennatjurprotokoll*/
        dbBundle.dbPersonenNatJurVersandadresse.createTable();
        dbBundle.dbPersonenprognose.createTable();
        
        dbBundle.dbPlzOrt.mandantenabhaengig=true;
        dbBundle.dbPlzOrt.createTable();
        dbBundle.dbPlzOrt.mandantenabhaengig=false;

        dbBundle.dbPortalTexte.createTable(true);
        dbBundle.dbPortalUnterlagen.createTable();
      
        //dbPraesenz??????
        dbBundle.dbPraesenzliste.createTable();
        dbBundle.dbPublikation.createTable();
       
        dbBundle.dbSperre.createTable();
        dbBundle.dbSperre.initialisiere();

        dbBundle.dbStaaten.mandantenabhaengig=true;
        dbBundle.dbStaaten.createTable();
        dbBundle.dbStaaten.mandantenabhaengig=false;
        
        dbBundle.dbStimmkarteInhalt.createTable();
        dbBundle.dbStimmkarten.createTable();
        dbBundle.dbStimmkartenSecond.createTable();
        dbBundle.dbSuchlaufDefinition.createTable();
        dbBundle.dbSuchlaufErgebnis.createTable();
        
        dbBundle.dbTeilnehmerStandVerein.createTable();
        dbBundle.dbTermine.createTable();
        dbBundle.dbVeranstaltung.createTable();

        dbBundle.dbVeranstaltungenVeranstaltung.createTable();
        dbBundle.dbVeranstaltungenElement.createTable();
        dbBundle.dbVeranstaltungenElementDetail.createTable();
        dbBundle.dbVeranstaltungenMenue.createTable();
        dbBundle.dbVeranstaltungenAktion.createTable();
        dbBundle.dbVeranstaltungenQuittungElement.createTable();
        dbBundle.dbVeranstaltungenWert.createTable();
        dbBundle.dbVeranstaltungenReportElement.createTable();

        
        dbBundle.dbVorlaeufigeVollmacht.createTable();
        dbBundle.dbVorlaeufigeVollmachtEingabe.createTable();
        
        dbBundle.dbWeisungMeldung.createTable();
        dbBundle.dbWillenserklaerung.createTable();
        dbBundle.dbWillenserklaerungZusatz.createTable();
        
        dbBundle.dbWortmeldetischProtokoll.createTable();
        
        dbBundle.dbZuordnungKennung.createTable();
        dbBundle.dbZutrittskarten.createTable();

        CaBug.druckeLog("Neuanlage fertig für Mandant " + dbBundle.clGlobalVar.mandant, logDrucken, 3);
        return true;
    }

    private void initialisiereNummernkreis(int pSchluessel) {
        EclNummernkreis nummernkreis = null;

        nummernkreis = new EclNummernkreis();
        nummernkreis.schluessel = pSchluessel;
        nummernkreis.letzteInterneIdent = 0;
        nummernkreis.alphaZutrittsIdent = 0;
        nummernkreis.letzteZutrittsIdent = 0;
        dbBundle.dbBasis.insertNummernkreis(nummernkreis);

    }

    /**pVariante:
     * 1 = feste Datums
     * 2 = bis Ende Rede Vorsitzenden
     * 3 = bis Ende Generaldebatte
     * @param pI
     * @param pVariante
     */
    private void legeTerminAn(int pI, int pVariante) {
        CaBug.druckeLog("pI=" + pI, logDrucken, 10);
        EclTermine lTermin = new EclTermine();
        lTermin.beschreibung = KonstTermine.getText(pI);
        String hString = "";
        switch (pI) {
        case 101:
            hString = "1=Port. noch nicht in Betrieb/vor Start";
            dbBundle.dbParameter.setzeErgString(pI + 120, hString, "phasenNamen[]", 1);
            lTermin.beschreibung += ": " + hString;
            break;
        case 102:
            hString = "2=Nur Email-Registr./vor Einl.Vers.";
            dbBundle.dbParameter.setzeErgString(pI + 120, hString, "phasenNamen[]", 1);
            lTermin.beschreibung += ": " + hString;
            break;
        case 103:
            hString = "3=Anm.möglich/Vers. bis letzt.Anmeldet.";
            dbBundle.dbParameter.setzeErgString(pI + 120, hString, "phasenNamen[]", 1);
            lTermin.textDatumZeitFuerPortalDE = "bis zum aa.aa.aaaa, 24:00 Uhr MESZ";
            lTermin.textDatumZeitFuerPortalEN = "until May 15, 24:00 Uhr CEST";
            lTermin.beschreibung += ": " + hString;
            break;
        case 104:
            hString = "4=Anmeldeschl.bis 1.Ende/Std:Schluß SRV";
            dbBundle.dbParameter.setzeErgString(pI + 120, hString, "phasenNamen[]", 1);
            switch (pVariante) {
            case 1:
                lTermin.textDatumZeitFuerPortalDE = "bis zum aa.aa.aaaa, 24:00 Uhr MESZ";
                lTermin.textDatumZeitFuerPortalEN = "until May 15, 24:00 Uhr CEST";
                break;
            case 2:
                lTermin.textDatumZeitFuerPortalDE = "bis zum Ende der Rede des Vorstandes auf der HV";
                lTermin.textDatumZeitFuerPortalEN = "until the end of the speach of the board";
                break;
            case 3:
                lTermin.textDatumZeitFuerPortalDE = "bis zum Ende der Generaldebatte auf der HV";
                lTermin.textDatumZeitFuerPortalEN = "until the end of the blabla at the meeting";
                break;
            }
            lTermin.beschreibung += ": " + hString;
            break;
        case 105:
            hString = "5=1.Ende-2.Ende/nicht im Standard";
            dbBundle.dbParameter.setzeErgString(pI + 120, hString, "phasenNamen[]", 1);
            lTermin.beschreibung += ": " + hString;
            break;
        case 106:
            hString = "6=2.Ende-3.Ende/Std: Vollm3 wäh.HV)";
            dbBundle.dbParameter.setzeErgString(pI + 120, hString, "phasenNamen[]", 1);
            lTermin.textDatumZeitFuerPortalDE = "bis zum Ende der Hauptversammlung";
            lTermin.textDatumZeitFuerPortalEN = "until the end of the meating";
            lTermin.beschreibung += ": " + hString;
            break;
        case 107:
            hString = "7=Nach der HV";
            dbBundle.dbParameter.setzeErgString(pI + 120, hString, "phasenNamen[]", 1);
            lTermin.beschreibung += ": " + hString;
            break;
        /*Alt - nicht mehr verwenden!
        	case 111:
        		hString="11=Beginn Hotline";
        		dbBundle.dbParameter.setzeErgString(pI+120, hString,  "phasenNamen[]", 1);
        		lTermin.beschreibung+=": "+hString;break;
        	case 112:
        		hString="12=Ende Hotline";
        		dbBundle.dbParameter.setzeErgString(pI+120, hString,  "phasenNamen[]", 1);
        		lTermin.beschreibung+=": "+hString;break;
        	case 113:
        		lTermin.textDatumZeitFuerPortalDE="ab dem aa.aa.aaaa";
        		lTermin.textDatumZeitFuerPortalEN="beginning from May 15";
        		hString="13=Beg. StreamingTest";
        		dbBundle.dbParameter.setzeErgString(pI+120, hString,  "phasenNamen[]", 1);
        		lTermin.beschreibung+=": "+hString;break;
        	case 114:
        		lTermin.textDatumZeitFuerPortalDE="ab dem aa.aa.aaaa, 10.00 Uhr";
        		lTermin.textDatumZeitFuerPortalEN="beginning from May 15, 11:00 a.m. CEST";
        		hString="14=Beg. HV Streaming";
        		dbBundle.dbParameter.setzeErgString(pI+120, hString,  "phasenNamen[]", 1);
        		lTermin.beschreibung+=": "+hString;break;
        	case 115:
        		lTermin.textDatumZeitFuerPortalDE="ab dem aa.aa.aaaa";
        		lTermin.textDatumZeitFuerPortalEN="beginning from May 15";
        		hString="15=Beg. Fragen";
        		dbBundle.dbParameter.setzeErgString(pI+120, hString,  "phasenNamen[]", 1);
        		lTermin.beschreibung+=": "+hString;break;
        	case 116:
        		lTermin.textDatumZeitFuerPortalDE="bis zum aa.aa.aaaa, 24:00 Uhr MESZ";
        		lTermin.textDatumZeitFuerPortalEN="until May 15, 24:00 Uhr CEST";
        		hString="16=Ende Fragen";
        		dbBundle.dbParameter.setzeErgString(pI+120, hString,  "phasenNamen[]", 1);
        		lTermin.beschreibung+=": "+hString;break;
        		*/
        }
        if (pI >= 121 && pI <= 158) {
            hString = KonstTermine.getText(pI);
        }

        CaBug.druckeLog("lTermin.beschreibung=" + lTermin.beschreibung, logDrucken, 10);
        lTermin.identTermin = pI;
        lTermin.technischErforderlicherTermin = 1;
        lTermin.mandant = dbBundle.clGlobalVar.mandant;
        dbBundle.dbTermine.insert(lTermin);
    }

    /*Temporär public - wg. AdminDiv im HVMaster*/
    public void initialisiereTermine(int pVariante) {
        for (int i = 1; i <= 7; i++) {
            legeTerminAn(i, pVariante);
        }
        for (int i = 101; i <= 120; i++) {
            legeTerminAn(i, pVariante);
        }
        for (int i = 201; i <= 202; i++) {
            legeTerminAn(i, pVariante);
        }
        initialisiereTermineErgaenze2021();
        initialisiereTermineErgaenze2022();
    }

    /**Ergänzt bestehende Termine um die für 2021 neuen*/
    public void initialisiereTermineErgaenze2021() {
        for (int i = 121; i <= 152; i++) {
            legeTerminAn(i, 0);
        }
    }

    /**Ergänzt bestehende Termine um die für 2022 neuen*/
    public void initialisiereTermineErgaenze2022() {
        for (int i = 8; i <= 9; i++) {
            legeTerminAn(i, 0);
        }
        for (int i=153;i<161;i++) {
            legeTerminAn(i, 0);
        }
    }

    /**Tables müssen bereits vorhanden sein. Werden nun mit den "Mindest-Daten" gefüllt*/
    public void initialisiereMandantenTables() {

        /*Nummernkreise*/
        initialisiereNummernkreis(1);
        for (int i = 11; i <= 15; i++) {
            initialisiereNummernkreis(i);
        }
        for (int i = 21; i <= 25; i++) {
            initialisiereNummernkreis(i);
        }
        initialisiereNummernkreis(31);
        for (int i = 601; i <= 622; i++) {
            initialisiereNummernkreis(i);
        }

        /*Termine (technische)*/
        initialisiereTermine(1);

        /*Personenprognose*/
        initialisierePersonenprognose();

    }

    private int initialisierePersonenprognose() {

        final Timestamp update = Timestamp.valueOf(LocalDateTime.now(ZoneId.of("UTC")));

        final EclPersonenprognose set1 = new EclPersonenprognose(1, "bis 25 km", 25, 80.00, 0, 0, 0, 0, update);
        final EclPersonenprognose set2 = new EclPersonenprognose(2, "bis 50 km", 50, 60.00, 0, 0, 0, 0, update);
        final EclPersonenprognose set3 = new EclPersonenprognose(3, "bis 100 km", 100, 40.00, 0, 0, 0, 0, update);
        final EclPersonenprognose set5 = new EclPersonenprognose(5, "bis 1000 km", 1000, 10.00, 0, 0, 0, 0, update);

        final EclPersonenprognose[] defaultSets = { set1, set2, set3, set5 };

        int i = 0;
        for (EclPersonenprognose set : defaultSets) {
            CaBug.druckeLog("Insert Datensatz: " + ++i, logDrucken, 10);
            if (dbBundle.dbPersonenprognose.insert(set) < 0)
                return -1;
        }
        return 1;
    }

    /**Legt nur die Tables an, sowie die Grundinitialisierung in tbl_nummernkreise.
     * 
     * Anschließend noch zu tun:
     * 
     * > Manuell importieren: 
     * >>> tbl_berechtigungenTexte
     * >>> tbl_fehler
     * >>> tbl_appTexte
     * 
     * >>> tbl_geraeteKlassen
     * >>> tbl_geraetKlasseSetZuordnung
     * 
     * > mit Modul: eindeutigerKey belegen
     * @return
     */
    public boolean legeMandantenUebergreifendeTablesAn() {
        dbBundle.dbAnreden.createTable();
        dbBundle.dbAppTexte.createTable();
        dbBundle.dbAppVersion.createTable();
        
        dbBundle.dbAuftrag.mandantenabhaengig=false;
        dbBundle.dbAuftrag.createTable();
        dbBundle.dbAuftrag.mandantenabhaengig=true;

        dbBundle.dbAusstellungsgrund.createTable_allgemein();

        DbBerechtigungenTexte dbBerechtigungenTexte = new DbBerechtigungenTexte(dbBundle);
        dbBerechtigungenTexte.createTable(); //Anschließend Durch Import Werte vorbelegen!
        dbBundle.dbDatenbankVerwaltung.updateTableVersion(aktuellErforderlicheDatenbankversion);

        dbBundle.dbBlzBank.createTable();

        dbBundle.dbDatenbankVerwaltung.createTableVersion();
        
        dbBundle.dbDrucklauf.mandantenabhaengig=false;
        dbBundle.dbDrucklauf.createTable();
        dbBundle.dbDrucklauf.mandantenabhaengig=true;
        
        dbBundle.dbEindeutigerKey.createTable(); //Anschließend durch Modul Werte vorbelegen
        dbBundle.dbEmittenten.createTable();
        dbBundle.dbFehler.createTable();
        dbBundle.dbGeraeteKlasse.createTable();
        dbBundle.dbGeraeteKlasseSetZuordnung.createTable();
        dbBundle.dbGeraeteLabelPrinter.createTable();
        dbBundle.dbGeraeteSet.createTable();
        dbBundle.dbGruppen.createTable_allgemein();
        dbBundle.dbKommunikationsSprachen.createTable();
        
        dbBundle.dbKontaktformularThema.mandantenabhaengig=false;
        dbBundle.dbKontaktformularThema.createTable();
        dbBundle.dbKontaktformularThema.mandantenabhaengig=true;
        
        dbBundle.dbMailingStatus.createTable();
        
        dbBundle.dbMenueEintrag.mandantenabhaengig=false;
        dbBundle.dbMenueEintrag.createTable();
        dbBundle.dbMenueEintrag.mandantenabhaengig=true;

        dbBundle.dbNummernForm.createTable();
        dbBundle.dbNummernFormSet.createTable();
        
        dbBundle.dbParameter.createTable_parameterServer();
        dbBundle.dbParameter.createTable_parameterGeraete();

        dbBundle.openParamStrukt();
        dbBundle.dbParamStrukt.createTable();
        dbBundle.dbParamStruktAblaufElement.createTable();
        dbBundle.dbParamStruktAblaufHeader.createTable();
        dbBundle.dbParamStruktGruppen.createTable();
        dbBundle.dbParamStruktGruppenHeader.createTable();
        dbBundle.dbParamStruktPresetArt.createTable();
        dbBundle.dbParamStruktStandard.createTable();
        dbBundle.dbParamStruktVersammlungstyp.createTable();
        dbBundle.dbParamStruktWerte.createTable();
        
        dbBundle.dbPlzOrt.createTable();

        DbReload dbReload = new DbReload(dbBundle);
        dbReload.createTable();

        dbBundle.dbScan.createTable();
        dbBundle.dbServiceDeskSet.createTable();
        dbBundle.dbServiceDeskWorkflow.createTable();
        dbBundle.dbSprachen.createTable();
        dbBundle.dbStaaten.createTable();
        dbBundle.dbTablet.createTable();
        dbBundle.dbUserLogin.createTable();
        dbBundle.dbVerarbeitungsLauf.createTable();
        dbBundle.dbVertretungsarten.createTable();
        
        dbBundle.dbWortmeldetischAktion.createTable();
        dbBundle.dbWortmeldetischFolgeStatus.createTable();
        dbBundle.dbWortmeldetischSet.createTable();
        dbBundle.dbWortmeldetischStatus.createTable();
        dbBundle.dbWortmeldetischStatusWeiterleitung.createTable();
        dbBundle.dbWortmeldetischView.createTable();
        dbBundle.dbWortmeldetischViewStatus.createTable();
        
        dbBundle.dbVipKZ.createTable();

        return true;
    }

    
    
    public void entferne0BestaendeAusAktienregister() {
        dbBundle.dbAktienregister.leseAlle();
        for (int i = 0; i < dbBundle.dbAktienregister.anzErgebnis(); i++) {
            EclAktienregister lAktienregister = dbBundle.dbAktienregister.ergebnisPosition(i);
            if (lAktienregister.stueckAktien==0) {
                dbBundle.dbLoginDaten.delete(lAktienregister.aktionaersnummer);
            }
            if (i % 500 == 0) {
                CaBug.druckeInfo("entferne0BestaendeAusAktienregister " + i 
                        + CaDatumZeit.DatumZeitStringFuerDatenbank());
            }
        }
        CaBug.druckeInfo("Vor delete0Bestand");
        dbBundle.dbAktienregister.delete0Bestand();
        CaBug.druckeInfo("Nach delete0Bestand");

    }
    
    
    public void reorgIdents() {
        CaBug.druckeLog("", logDrucken, 3);
        dbBundle.dbPersonenNatJur.reorgInterneIdent();
        dbBundle.dbAktienregister.reorgInterneIdent();
  
        /*Mandantenübergreifend*/
        dbBundle.dbNummernForm.reorgInterneIdent();
        dbBundle.dbNummernFormSet.reorgInterneIdent();

        /**Problem war: auf jedem Server werden neue User angelegt. Damit werden idents doppelt vergeben.
         * 
         * Um 10000 herum wurden eh technische User angelegt.
         * Deshalb:
         * MaxIdents wurden nun einmal gesetzt:
         * auf Portal01: 100000
         * auf Portal02: 200000
         * auf Portal03: 300000
         * 
         * Damit können nun unabhängig User angelegt und von Server zu Server kopiert werden.
         * Die MaxIdents dürfen allerdings nie zurückgesetzt werden .... sonst ist wieder das alte Problem
         * 
         * Maxident: ident=608
         * Basisinitialisierung (am 13.03.2023) erfolgt auf xxx200, um die bereits angelegten User ggf. "hochzuschwenken"
         * und ggf. auf andere Server zu übertragen.
         * 
         * Hinweis: maxidentProfile=649, wurde auf 10 gesetzt.
         */
//        dbBundle.dbUserLogin.reorgInterneIdent();

        dbBundle.dbUserLogin.profileVerarbeiten=true;
        dbBundle.dbUserLogin.reorgInterneIdent();
        dbBundle.dbUserLogin.profileVerarbeiten=false;
        
        dbBundle.dbInsti.reorgInterneIdent();
        dbBundle.dbSuchlaufBegriffe.reorgInterneIdent(true);
    }

    public boolean testCreate() {
        //		for (int i=0;i<300;i++){
        //			for (int i1=0;i1<200;i1++){
        //				String lStatement=createTest_A+"M"+CaString.fuelleLinksNull(Integer.toString(i),3)+"L"+CaString.fuelleLinksNull(Integer.toString(i1),3)+createTest_B;
        //				dbBundle.dbDatenbankVerwaltung.rawOperation(lStatement);
        //			}
        //		}
        return true;
    }

    public boolean testDrop() {
        //		for (int i=0;i<300;i++){
        //			for (int i1=0;i1<200;i1++){
        //				String lStatement=dropTest_A+"M"+CaString.fuelleLinksNull(Integer.toString(i),3)+"L"+CaString.fuelleLinksNull(Integer.toString(i1),3)+dropTest_B;
        //				dbBundle.dbDatenbankVerwaltung.rawOperation(lStatement);
        //			}
        //		}
        return true;
    }

    public boolean testSchema() {
        //		for (int i=101;i<501;i++){
        //			String lStatement=schemaTest_A+"M"+CaString.fuelleLinksNull(Integer.toString(i),3)+schemaTest_B;
        //			dbBundle.dbDatenbankVerwaltung.rawOperation(lStatement);
        //		}
        return true;
    }

    public boolean testInsertMassen() {
        //		dbBundle.dbDatenbankVerwaltung.createTable();
        //		for (int i=2;i<200000;i++){
        //			String lStatement="INSERT INTO tbl_tableversion "
        //					+ "SET mandant=0, ident="+Integer.toString(i)+", db_version=0, wert=''";
        //			dbBundle.dbDatenbankVerwaltung.rawOperation(lStatement);
        //		}
        return true;
    }

    /**************Ab hier: Create-Skripts für Neuanlegen************************/

    //	
    //	private String createTest_A="CREATE TABLE `tbl_test";
    //	private String createTest_B="` ( "
    //			  +"`mandant` int(11) NOT NULL, "
    //			  +"`ident` int(11) NOT NULL, "
    //			  +"`db_version` bigint(20) DEFAULT NULL, "
    //			  +"`wert` char(20) DEFAULT NULL, "
    //			  +"PRIMARY KEY (`mandant`,`ident`) "
    //			  +") ENGINE=InnoDB DEFAULT CHARSET=utf8; ";
    //
    //
    //	private String dropTest_A="DROP TABLE `tbl_test";
    //	private String dropTest_B="` ; ";
    //
    //	private String schemaTest_A="CREATE DATABASE `dbTest";
    //	private String schemaTest_B="` ; ";
}
