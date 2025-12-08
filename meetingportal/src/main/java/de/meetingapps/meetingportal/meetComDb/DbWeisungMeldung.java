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
import java.util.LinkedList;
import java.util.List;

import de.meetingapps.meetingportal.meetComAllg.CaBug;
import de.meetingapps.meetingportal.meetComAllg.CaFehler;
import de.meetingapps.meetingportal.meetComEntities.EclWeisungMeldung;
import de.meetingapps.meetingportal.meetComEntities.EclWeisungMeldungEinzelneWeisung;
import de.meetingapps.meetingportal.meetComEntities.EclWeisungMeldungRaw;
import de.meetingapps.meetingportal.meetComEntities.EclWeisungMeldungSplit;
import de.meetingapps.meetingportal.meetComEntities.EclWeisungMeldungSplitRaw;

public class DbWeisungMeldung {

    private int logDrucken=3;
    
    private Connection verbindung = null;
    private DbBasis dbBasis = null;
    private DbBundle dbBundle = null;

    //	ALTER TABLE `db_mc168j2018ap`.`tbl_weisungmeldung` 
    //	ADD COLUMN `skIst` INT(11) NULL DEFAULT NULL AFTER `sammelIdent`,
    //	DROP PRIMARY KEY,
    //	ADD PRIMARY KEY (`mandant`, `weisungIdent`);
    //	;

    /*************************Initialisierung***************************/
    /* Verbindung in lokale Daten eintragen*/
    public DbWeisungMeldung(DbBundle datenbankbundle) {
        if (datenbankbundle.dbBasis == null) {
            System.err.println("vmcdbBasis nicht initialisiert");
            return;
        }
        dbBasis = datenbankbundle.dbBasis;
        verbindung = datenbankbundle.dbBasis.verbindung;
        dbBundle = datenbankbundle;
    }

    /*****************Ergebnis-Array für Standard-Abfragen*******************************/
    /**Soll nicht direkt verwendet werden, sondern nur über die Zugriffsfunktionen!
     * Aktuell noch public, da direkter Zugriff über DbMeldungen
     */
    public EclWeisungMeldung weisungMeldungArray[] = null;
    private EclWeisungMeldungRaw weisungMeldungRawArray[] = null;
    private EclWeisungMeldungSplit weisungMeldungSplitArray[] = null;
    private EclWeisungMeldungSplitRaw weisungMeldungSplitRawArray[] = null;

    public int anzWeisungMeldungGefunden() {
        if (weisungMeldungArray == null) {
            return 0;
        }
        return weisungMeldungArray.length;
    }

    public EclWeisungMeldung weisungMeldungGefunden(int lfd) {
        int lWeisungIdent = 0;
        lWeisungIdent = weisungMeldungArray[lfd].weisungIdent;
        weisungMeldungArray[lfd].weisungMeldungSplit = null;
        if (weisungMeldungSplitArray != null) {
            int i;
            for (i = 0; i < weisungMeldungSplitArray.length; i++) {
                if (weisungMeldungSplitArray[i].weisungIdent == lWeisungIdent) {
                    weisungMeldungArray[lfd].weisungMeldungSplit = weisungMeldungSplitArray[i];
                }
            }
        }
        return weisungMeldungArray[lfd];
    }

    /*****************Ergebnis-Array für leseAktionaersWeisungZuSammelkarte-Abfragen*******************************/
    /**Soll nicht direkt verwendet werden, sondern nur über die Zugriffsfunktionen!
     * Aktuell noch public, da direkter Zugriff über DbMeldungen
     */
    public class PclAktionaersWeisungenZuSammelkarte {
        public EclWeisungMeldung weisungMeldung = null;
        public long stimmen;
        public long stueckAktien;
        public int gattung;
        public String kurzName;

        public String aktionaersnummer;
        public String zutrittsIdent;
        public String stimmkarte;
        public String besitzart;
        public String stimmausschluss;
        public String name;
        public String vorname;
        public String ort;
    }

    private PclAktionaersWeisungenZuSammelkarte aktionaersWeisungenZuSammelkarteArray[] = null;

    public int aktionaersWeisungenZuSammelkarteAnzGefunden() {
        if (aktionaersWeisungenZuSammelkarteArray == null) {
            return 0;
        }
        return aktionaersWeisungenZuSammelkarteArray.length;
    }

    public PclAktionaersWeisungenZuSammelkarte aktionaersWeisungenZuSammelkarteGefunden(int lfd) {
        return aktionaersWeisungenZuSammelkarteArray[lfd];
    }

    public EclWeisungMeldungRaw weisungMeldungRawGefunden(int lfd) {
        //		17.11.2019: Alt. Erscheint ziemlich schwachsinnig.
        //		int lWeisungIdent=0;
        //		lWeisungIdent=weisungMeldungArray[lfd].weisungIdent;
        //		int i;
        //		for (i=0;i<weisungMeldungSplitRawArray.length;i++){
        //			if (weisungMeldungSplitRawArray[i].weisungIdent==lWeisungIdent){
        //				weisungMeldungRawArray[lfd].weisungMeldungSplitRaw=weisungMeldungSplitRawArray[i];
        //			}
        //		}
        //		return weisungMeldungRawArray[lfd];

        int lWeisungIdent = 0;
        lWeisungIdent = weisungMeldungArray[lfd].weisungIdent;

        weisungMeldungRawArray[lfd].weisungMeldungSplitRaw = null;
        if (weisungMeldungSplitRawArray != null) {
            int i;
            for (i = 0; i < weisungMeldungSplitRawArray.length; i++) {
                if (weisungMeldungSplitRawArray[i].weisungIdent == lWeisungIdent) {
                    weisungMeldungRawArray[lfd].weisungMeldungSplitRaw = weisungMeldungSplitRawArray[i];
                }
            }
        }
        return weisungMeldungRawArray[lfd];

    }

    public int createTable() {
        int rc = 0;
        DbLowLevel lDbLowLevel = new DbLowLevel(dbBundle);
        String hSql = "CREATE TABLE " + dbBundle.getSchemaMandant() + "tbl_weisungmeldung ( "
                + "`mandant` int(11) NOT NULL, " + "`weisungIdent` int(11) NOT NULL, "
                + "`meldungsIdent` int(11) DEFAULT NULL, " + "`sammelIdent` int(11) DEFAULT NULL, "
                + "`skIst` int(11) DEFAULT NULL, " + "`willenserklaerungIdent` int(11) DEFAULT NULL, "
                + "`weisungSplit` int(11) DEFAULT NULL, " + "`aktiv` int(11) DEFAULT NULL, "
                + "`istWeisungBriefwahl` int(11) DEFAULT NULL, "
                + "`gemaessFremdenAbstimmungsVorschlagIdent` int(11) DEFAULT NULL, "
                + "`gemaessEigenemAbstimmungsVorschlagIdent` int(11) DEFAULT NULL, "
                + "`stimmartGesamt` int(11) DEFAULT NULL, " + "`aktualisieren` int(11) DEFAULT NULL, ";
        for (int i = 0; i <= 199; i++) {
            hSql = hSql + "`abgabe" + Integer.toString(i) + "` int(11) DEFAULT NULL, ";
        }
        for (int i = 0; i <= 199; i++) {
            hSql = hSql + "`abgabeLautGesamt" + Integer.toString(i) + "` int(11) DEFAULT NULL, ";
        }
        hSql = hSql +

                "PRIMARY KEY (`weisungIdent`,`mandant`), " + "KEY `IDX_meldungsIdent` (`meldungsIdent`), "
                + "KEY `IDX_sammelIdent` (`sammelIdent`) " + ") ";
        rc = lDbLowLevel.createTable(hSql);
        if (rc < 0) {
            return rc;
        }

        lDbLowLevel = new DbLowLevel(dbBundle);
        hSql = "CREATE TABLE " + dbBundle.getSchemaMandant() + "tbl_weisungmeldungraw ( "
                + "`mandant` int(11) NOT NULL, " + "`weisungIdent` int(11) NOT NULL, "
                + "`meldungsIdent` int(11) DEFAULT NULL, " + "`sammelIdent` int(11) DEFAULT NULL, "
                + "`willenserklaerungIdent` int(11) DEFAULT NULL, " + "`bereitsInterpretiert` int(11) DEFAULT NULL, "
                + "`gemaessFremdenAbstimmungsVorschlagIdent` int(11) DEFAULT NULL, "
                + "`gegenFremdenAbstimmungsVorschlagIdent` int(11) DEFAULT NULL, "
                + "`gemaessEigenemAbstimmungsVorschlagIdent` int(11) DEFAULT NULL, "
                + "`gegenEigenemAbstimmungsVorschlagIdent` int(11) DEFAULT NULL, "
                + "`stimmartGesamt` char(20) DEFAULT NULL, " + "`aktualisieren` int(11) DEFAULT NULL, ";
        for (int i = 0; i <= 99; i++) {
            hSql = hSql + "`abgabe" + Integer.toString(i) + "` char(20) DEFAULT NULL, ";
        }
        hSql = hSql + "PRIMARY KEY (`weisungIdent`,`mandant`) " + ") ";
        rc = lDbLowLevel.createTable(hSql);
        if (rc < 0) {
            return rc;
        }

        lDbLowLevel = new DbLowLevel(dbBundle);
        hSql = "CREATE TABLE " + dbBundle.getSchemaMandant() + "tbl_weisungmeldungraw1 ( "
                + "`mandant` int(11) NOT NULL, " + "`weisungIdent` int(11) NOT NULL, "
                + "`meldungsIdent` int(11) DEFAULT NULL, " + "`sammelIdent` int(11) DEFAULT NULL, "
                + "`willenserklaerungIdent` int(11) DEFAULT NULL, ";
        for (int i = 100; i <= 199; i++) {
            hSql = hSql + "`abgabe" + Integer.toString(i) + "` char(20) DEFAULT NULL, ";
        }
        hSql = hSql + "PRIMARY KEY (`weisungIdent`,`mandant`) " + ") ";
        rc = lDbLowLevel.createTable(hSql);
        if (rc < 0) {
            return rc;
        }

        lDbLowLevel = new DbLowLevel(dbBundle);
        hSql = "CREATE TABLE " + dbBundle.getSchemaMandant() + "tbl_weisungmeldungsplit ( "
                + "`mandant` int(11) NOT NULL, " + "`weisungIdent` int(11) NOT NULL, "
                + "`meldungsIdent` int(11) DEFAULT NULL, " + "`sammelIdent` int(11) DEFAULT NULL, "
                + "`willenserklaerungIdent` int(11) DEFAULT NULL, ";
        for (int i = 0; i <= 49; i++) {
            for (int i1 = 0; i1 <= 9; i1++) {
                hSql = hSql + "`abgabe" + Integer.toString(i) + "_" + Integer.toString(i1)
                        + "` bigint(20) DEFAULT NULL, ";
            }
        }
        for (int i = 0; i <= 49; i++) {
            hSql = hSql + "`nichtberechnen" + Integer.toString(i) + "` int(11) DEFAULT NULL, ";
        }
        for (int i = 0; i <= 49; i++) {
            hSql = hSql + "`weisungssummeFalsch" + Integer.toString(i) + "` int(11) DEFAULT NULL, ";
        }
        hSql = hSql + "PRIMARY KEY (`weisungIdent`,`mandant`) " + ") ";
        rc = lDbLowLevel.createTable(hSql);
        if (rc < 0) {
            return rc;
        }

        lDbLowLevel = new DbLowLevel(dbBundle);
        hSql = "CREATE TABLE " + dbBundle.getSchemaMandant() + "tbl_weisungmeldungsplit1 ( "
                + "`mandant` int(11) NOT NULL, " + "`weisungIdent` int(11) NOT NULL, "
                + "`meldungsIdent` int(11) DEFAULT NULL, " + "`sammelIdent` int(11) DEFAULT NULL, "
                + "`willenserklaerungIdent` int(11) DEFAULT NULL, ";
        for (int i = 50; i <= 99; i++) {
            for (int i1 = 0; i1 <= 9; i1++) {
                hSql = hSql + "`abgabe" + Integer.toString(i) + "_" + Integer.toString(i1)
                        + "` bigint(20) DEFAULT NULL, ";
            }
        }
        for (int i = 50; i <= 99; i++) {
            hSql = hSql + "`nichtberechnen" + Integer.toString(i) + "` int(11) DEFAULT NULL, ";
        }
        for (int i = 50; i <= 99; i++) {
            hSql = hSql + "`weisungssummeFalsch" + Integer.toString(i) + "` int(11) DEFAULT NULL, ";
        }
        hSql = hSql + "PRIMARY KEY (`weisungIdent`,`mandant`) " + ") ";
        rc = lDbLowLevel.createTable(hSql);
        if (rc < 0) {
            return rc;
        }

        lDbLowLevel = new DbLowLevel(dbBundle);
        hSql = "CREATE TABLE " + dbBundle.getSchemaMandant() + "tbl_weisungmeldungsplit2 ( "
                + "`mandant` int(11) NOT NULL, " + "`weisungIdent` int(11) NOT NULL, "
                + "`meldungsIdent` int(11) DEFAULT NULL, " + "`sammelIdent` int(11) DEFAULT NULL, "
                + "`willenserklaerungIdent` int(11) DEFAULT NULL, ";
        for (int i = 100; i <= 149; i++) {
            for (int i1 = 0; i1 <= 9; i1++) {
                hSql = hSql + "`abgabe" + Integer.toString(i) + "_" + Integer.toString(i1)
                        + "` bigint(20) DEFAULT NULL, ";
            }
        }
        for (int i = 100; i <= 149; i++) {
            hSql = hSql + "`nichtberechnen" + Integer.toString(i) + "` int(11) DEFAULT NULL, ";
        }
        for (int i = 100; i <= 149; i++) {
            hSql = hSql + "`weisungssummeFalsch" + Integer.toString(i) + "` int(11) DEFAULT NULL, ";
        }
        hSql = hSql + "PRIMARY KEY (`weisungIdent`,`mandant`) " + ") ";
        rc = lDbLowLevel.createTable(hSql);
        if (rc < 0) {
            return rc;
        }

        lDbLowLevel = new DbLowLevel(dbBundle);
        hSql = "CREATE TABLE " + dbBundle.getSchemaMandant() + "tbl_weisungmeldungsplit3 ( "
                + "`mandant` int(11) NOT NULL, " + "`weisungIdent` int(11) NOT NULL, "
                + "`meldungsIdent` int(11) DEFAULT NULL, " + "`sammelIdent` int(11) DEFAULT NULL, "
                + "`willenserklaerungIdent` int(11) DEFAULT NULL, ";
        for (int i = 150; i <= 199; i++) {
            for (int i1 = 0; i1 <= 9; i1++) {
                hSql = hSql + "`abgabe" + Integer.toString(i) + "_" + Integer.toString(i1)
                        + "` bigint(20) DEFAULT NULL, ";
            }
        }
        for (int i = 150; i <= 199; i++) {
            hSql = hSql + "`nichtberechnen" + Integer.toString(i) + "` int(11) DEFAULT NULL, ";
        }
        for (int i = 150; i <= 199; i++) {
            hSql = hSql + "`weisungssummeFalsch" + Integer.toString(i) + "` int(11) DEFAULT NULL, ";
        }
        hSql = hSql + "PRIMARY KEY (`weisungIdent`,`mandant`) " + ") ";
        rc = lDbLowLevel.createTable(hSql);
        if (rc < 0) {
            return rc;
        }

        lDbLowLevel = new DbLowLevel(dbBundle);
        hSql = "CREATE TABLE " + dbBundle.getSchemaMandant() + "tbl_weisungmeldungsplitraw ( "
                + "`mandant` int(11) NOT NULL, " + "`weisungIdent` int(11) NOT NULL, "
                + "`meldungsIdent` int(11) DEFAULT NULL, " + "`sammelIdent` int(11) DEFAULT NULL, "
                + "`willenserklaerungIdent` int(11) DEFAULT NULL, ";
        for (int i = 0; i <= 49; i++) {
            for (int i1 = 0; i1 <= 9; i1++) {
                hSql = hSql + "`abgabe" + Integer.toString(i) + "_" + Integer.toString(i1)
                        + "` bigint(20) DEFAULT NULL, ";
            }
        }
        hSql = hSql + "PRIMARY KEY (`weisungIdent`,`mandant`) " + ") ";
        rc = lDbLowLevel.createTable(hSql);
        if (rc < 0) {
            return rc;
        }

        lDbLowLevel = new DbLowLevel(dbBundle);
        hSql = "CREATE TABLE " + dbBundle.getSchemaMandant() + "tbl_weisungmeldungsplitraw1 ( "
                + "`mandant` int(11) NOT NULL, " + "`weisungIdent` int(11) NOT NULL, "
                + "`meldungsIdent` int(11) DEFAULT NULL, " + "`sammelIdent` int(11) DEFAULT NULL, "
                + "`willenserklaerungIdent` int(11) DEFAULT NULL, ";
        for (int i = 50; i <= 99; i++) {
            for (int i1 = 0; i1 <= 9; i1++) {
                hSql = hSql + "`abgabe" + Integer.toString(i) + "_" + Integer.toString(i1)
                        + "` bigint(20) DEFAULT NULL, ";
            }
        }
        hSql = hSql + "PRIMARY KEY (`weisungIdent`,`mandant`) " + ") ";
        rc = lDbLowLevel.createTable(hSql);
        if (rc < 0) {
            return rc;
        }

        lDbLowLevel = new DbLowLevel(dbBundle);
        hSql = "CREATE TABLE " + dbBundle.getSchemaMandant() + "tbl_weisungmeldungsplitraw2 ( "
                + "`mandant` int(11) NOT NULL, " + "`weisungIdent` int(11) NOT NULL, "
                + "`meldungsIdent` int(11) DEFAULT NULL, " + "`sammelIdent` int(11) DEFAULT NULL, "
                + "`willenserklaerungIdent` int(11) DEFAULT NULL, ";
        for (int i = 100; i <= 149; i++) {
            for (int i1 = 0; i1 <= 9; i1++) {
                hSql = hSql + "`abgabe" + Integer.toString(i) + "_" + Integer.toString(i1)
                        + "` bigint(20) DEFAULT NULL, ";
            }
        }
        hSql = hSql + "PRIMARY KEY (`weisungIdent`,`mandant`) " + ") ";
        rc = lDbLowLevel.createTable(hSql);
        if (rc < 0) {
            return rc;
        }

        lDbLowLevel = new DbLowLevel(dbBundle);
        hSql = "CREATE TABLE " + dbBundle.getSchemaMandant() + "tbl_weisungmeldungsplitraw3 ( "
                + "`mandant` int(11) NOT NULL, " + "`weisungIdent` int(11) NOT NULL, "
                + "`meldungsIdent` int(11) DEFAULT NULL, " + "`sammelIdent` int(11) DEFAULT NULL, "
                + "`willenserklaerungIdent` int(11) DEFAULT NULL, ";
        for (int i = 150; i <= 199; i++) {
            for (int i1 = 0; i1 <= 9; i1++) {
                hSql = hSql + "`abgabe" + Integer.toString(i) + "_" + Integer.toString(i1)
                        + "` bigint(20) DEFAULT NULL, ";
            }
        }
        hSql = hSql + "PRIMARY KEY (`weisungIdent`,`mandant`) " + ") ";
        rc = lDbLowLevel.createTable(hSql);
        if (rc < 0) {
            return rc;
        }

        return 1;
    }

    
    public int copyTable(int pZielLfdNr) {
        String pName="";
        int rc=0;
        //@formatter:off
        CaBug.druckeLog("Start", logDrucken, 3);
        pName=dbBundle.getSchemaMandant() + "tbl_weisungmeldung";rc=copyTableIntern(pName, pZielLfdNr);if (rc!=1) {return rc;}
        CaBug.druckeLog("1", logDrucken, 3);
        pName=dbBundle.getSchemaMandant() + "tbl_weisungmeldungraw";rc=copyTableIntern(pName, pZielLfdNr);if (rc!=1) {return rc;}
        CaBug.druckeLog("2", logDrucken, 3);
        pName=dbBundle.getSchemaMandant() + "tbl_weisungmeldungraw1";rc=copyTableIntern(pName, pZielLfdNr);if (rc!=1) {return rc;}
        CaBug.druckeLog("3", logDrucken, 3);
        pName=dbBundle.getSchemaMandant() + "tbl_weisungmeldungsplit";rc=copyTableIntern(pName, pZielLfdNr);if (rc!=1) {return rc;}
        CaBug.druckeLog("4", logDrucken, 3);
        pName=dbBundle.getSchemaMandant() + "tbl_weisungmeldungsplit1";rc=copyTableIntern(pName, pZielLfdNr);if (rc!=1) {return rc;}
        CaBug.druckeLog("5", logDrucken, 3);
        pName=dbBundle.getSchemaMandant() + "tbl_weisungmeldungsplit2";rc=copyTableIntern(pName, pZielLfdNr);if (rc!=1) {return rc;}
        CaBug.druckeLog("6", logDrucken, 3);
        pName=dbBundle.getSchemaMandant() + "tbl_weisungmeldungsplit3";rc=copyTableIntern(pName, pZielLfdNr);if (rc!=1) {return rc;}
        CaBug.druckeLog("7", logDrucken, 3);
        pName=dbBundle.getSchemaMandant() + "tbl_weisungmeldungsplitraw";rc=copyTableIntern(pName, pZielLfdNr);if (rc!=1) {return rc;}
        CaBug.druckeLog("8", logDrucken, 3);
        pName=dbBundle.getSchemaMandant() + "tbl_weisungmeldungsplitraw1";rc=copyTableIntern(pName, pZielLfdNr);if (rc!=1) {return rc;}
        CaBug.druckeLog("9", logDrucken, 3);
        pName=dbBundle.getSchemaMandant() + "tbl_weisungmeldungsplitraw2";rc=copyTableIntern(pName, pZielLfdNr);if (rc!=1) {return rc;}
        CaBug.druckeLog("10", logDrucken, 3);
        pName=dbBundle.getSchemaMandant() + "tbl_weisungmeldungsplitraw3";rc=copyTableIntern(pName, pZielLfdNr);if (rc!=1) {return rc;}
        CaBug.druckeLog("Ende", logDrucken, 3);
        //@formatter:on
       
        return 1;
    }
    
    private int copyTableIntern(String pName, int pZielLfdNr) {
        DbLowLevel dbLowLevel=new DbLowLevel(dbBundle);
        
        String quelle=pName;
        String ziel=pName+"_"+Integer.toString(pZielLfdNr);
        return dbLowLevel.copyTable(quelle, ziel);
    }
    
    public boolean pruefeArchivVorhanden(int abstimmungsblockIdent) {
        DbLowLevel dbLowLevel=new DbLowLevel(dbBundle);
        boolean brc=dbLowLevel.checkTableVorhandenNew(dbBundle.getSchemaMandantOhnePunkt(), "tbl_weisungmeldung"+Integer.toString(abstimmungsblockIdent));
        return brc;
    }

    /**************************deleteAll Löschen aller Datensätze  eines Mandanten****************/
    public int deleteAll() {
        int erg = 0;

        dbBasis.resetInterneIdentWeisungMeldung();
        erg = dbBundle.dbLowLevel
                .deleteMandant("DELETE FROM " + dbBundle.getSchemaMandant() + "tbl_weisungMeldung where mandant=?;");
        if (erg < 0) {
            return erg;
        }

        erg = dbBundle.dbLowLevel
                .deleteMandant("DELETE FROM " + dbBundle.getSchemaMandant() + "tbl_weisungMeldungRaw where mandant=?;");
        if (erg < 0) {
            return erg;
        }
        erg = dbBundle.dbLowLevel.deleteMandant(
                "DELETE FROM " + dbBundle.getSchemaMandant() + "tbl_weisungMeldungRaw1 where mandant=?;");
        if (erg < 0) {
            return erg;
        }

        erg = dbBundle.dbLowLevel.deleteMandant(
                "DELETE FROM " + dbBundle.getSchemaMandant() + "tbl_weisungMeldungSplit where mandant=?;");
        if (erg < 0) {
            return erg;
        }
        erg = dbBundle.dbLowLevel.deleteMandant(
                "DELETE FROM " + dbBundle.getSchemaMandant() + "tbl_weisungMeldungSplit1 where mandant=?;");
        if (erg < 0) {
            return erg;
        }
        erg = dbBundle.dbLowLevel.deleteMandant(
                "DELETE FROM " + dbBundle.getSchemaMandant() + "tbl_weisungMeldungSplit2 where mandant=?;");
        if (erg < 0) {
            return erg;
        }
        erg = dbBundle.dbLowLevel.deleteMandant(
                "DELETE FROM " + dbBundle.getSchemaMandant() + "tbl_weisungMeldungSplit3 where mandant=?;");
        if (erg < 0) {
            return erg;
        }

        erg = dbBundle.dbLowLevel.deleteMandant(
                "DELETE FROM " + dbBundle.getSchemaMandant() + "tbl_weisungMeldungSplitRaw where mandant=?;");
        if (erg < 0) {
            return erg;
        }
        erg = dbBundle.dbLowLevel.deleteMandant(
                "DELETE FROM " + dbBundle.getSchemaMandant() + "tbl_weisungMeldungSplitRaw1 where mandant=?;");
        if (erg < 0) {
            return erg;
        }
        erg = dbBundle.dbLowLevel.deleteMandant(
                "DELETE FROM " + dbBundle.getSchemaMandant() + "tbl_weisungMeldungSplitRaw2 where mandant=?;");
        if (erg < 0) {
            return erg;
        }
        erg = dbBundle.dbLowLevel.deleteMandant(
                "DELETE FROM " + dbBundle.getSchemaMandant() + "tbl_weisungMeldungSplitRaw3 where mandant=?;");
        if (erg < 0) {
            return erg;
        }

        return 1;
    }

    /**************************setzt aktuelle Mandantennummer bei allen Datensätzen****************/
    public int updateMandant() {
        dbBundle.dbLowLevel.rawUpdateMandant(dbBundle.getSchemaMandant() + "tbl_weisungMeldung");
        dbBundle.dbLowLevel.rawUpdateMandant(dbBundle.getSchemaMandant() + "tbl_weisungMeldungRaw");
        dbBundle.dbLowLevel.rawUpdateMandant(dbBundle.getSchemaMandant() + "tbl_weisungMeldungRaw1");

        dbBundle.dbLowLevel.rawUpdateMandant(dbBundle.getSchemaMandant() + "tbl_weisungMeldungSplit");
        dbBundle.dbLowLevel.rawUpdateMandant(dbBundle.getSchemaMandant() + "tbl_weisungMeldungSplit1");
        dbBundle.dbLowLevel.rawUpdateMandant(dbBundle.getSchemaMandant() + "tbl_weisungMeldungSplit2");
        dbBundle.dbLowLevel.rawUpdateMandant(dbBundle.getSchemaMandant() + "tbl_weisungMeldungSplit3");

        dbBundle.dbLowLevel.rawUpdateMandant(dbBundle.getSchemaMandant() + "tbl_weisungMeldungSplitRaw");
        dbBundle.dbLowLevel.rawUpdateMandant(dbBundle.getSchemaMandant() + "tbl_weisungMeldungSplitRaw1");
        dbBundle.dbLowLevel.rawUpdateMandant(dbBundle.getSchemaMandant() + "tbl_weisungMeldungSplitRaw2");
        return dbBundle.dbLowLevel.rawUpdateMandant(dbBundle.getSchemaMandant() + "tbl_weisungMeldungSplitRaw3");
    }

    public void reorgInterneIdent() {
        int lMax = dbBundle.dbLowLevel.liefereHoechsteIdent("SELECT MAX(weisungIdent) FROM "
                + dbBundle.getSchemaMandant() + "tbl_weisungMeldung ab where ab.mandant=? ");
        if (lMax != -1) {
            dbBundle.dbBasis.resetInterneIdentWeisungMeldung(lMax);
        }
    }

    /** dekodiert die aktuelle Position aus ergebnis in EclWeisungMeldung und gibt dieses zurück*/
    public EclWeisungMeldung decodeErgebnis(ResultSet ergebnis) {
        EclWeisungMeldung lWeisungMeldung = new EclWeisungMeldung();

        try {
            lWeisungMeldung.mandant = ergebnis.getInt("wm.mandant");
            lWeisungMeldung.weisungIdent = ergebnis.getInt("wm.weisungIdent");
            lWeisungMeldung.meldungsIdent = ergebnis.getInt("wm.meldungsIdent");
            lWeisungMeldung.sammelIdent = ergebnis.getInt("wm.sammelIdent");
            lWeisungMeldung.skIst = ergebnis.getInt("wm.skIst");
            lWeisungMeldung.willenserklaerungIdent = ergebnis.getInt("wm.willenserklaerungIdent");
            lWeisungMeldung.weisungSplit = ergebnis.getInt("wm.weisungSplit");
            lWeisungMeldung.aktiv = ergebnis.getInt("wm.aktiv");
            lWeisungMeldung.istWeisungBriefwahl = ergebnis.getInt("wm.istWeisungBriefwahl");
            lWeisungMeldung.gemaessFremdenAbstimmungsVorschlagIdent = ergebnis
                    .getInt("wm.gemaessFremdenAbstimmungsVorschlagIdent");
            lWeisungMeldung.gemaessEigenemAbstimmungsVorschlagIdent = ergebnis
                    .getInt("wm.gemaessEigenemAbstimmungsVorschlagIdent");
            lWeisungMeldung.stimmartGesamt = ergebnis.getInt("wm.stimmartGesamt");
            lWeisungMeldung.aktualisieren = ergebnis.getInt("wm.aktualisieren");

            int i;
            for (i = 0; i < 200; i++) {
                lWeisungMeldung.abgabe[i] = ergebnis.getInt("wm.abgabe" + Integer.toString(i));
            }
            for (i = 0; i < 200; i++) {
                lWeisungMeldung.abgabeLautGesamt[i] = ergebnis.getInt("wm.abgabeLautGesamt" + Integer.toString(i));
            }

        } catch (Exception e) {
            CaBug.drucke("DbWeisungMeldung.decodeErgebnis 001");
            System.err.println(" " + e.getMessage());
        }

        return lWeisungMeldung;
    }

    /** dekodiert die aktuelle Position aus ergebnis in EclWeisungMeldung und gibt dieses zurück*/
    private EclWeisungMeldungRaw decodeErgebnisRaw(ResultSet ergebnis) {
        EclWeisungMeldungRaw lWeisungMeldungRaw = new EclWeisungMeldungRaw();

        try {
            lWeisungMeldungRaw.mandant = ergebnis.getInt("wmr.mandant");
            lWeisungMeldungRaw.weisungIdent = ergebnis.getInt("wmr.weisungIdent");
            lWeisungMeldungRaw.meldungsIdent = ergebnis.getInt("wmr.meldungsIdent");
            lWeisungMeldungRaw.sammelIdent = ergebnis.getInt("wmr.sammelIdent");
            lWeisungMeldungRaw.willenserklaerungIdent = ergebnis.getInt("wmr.willenserklaerungIdent");
            lWeisungMeldungRaw.bereitsInterpretiert = ergebnis.getInt("wmr.bereitsInterpretiert");
            lWeisungMeldungRaw.gemaessFremdenAbstimmungsVorschlagIdent = ergebnis
                    .getInt("wmr.gemaessFremdenAbstimmungsVorschlagIdent");
            lWeisungMeldungRaw.gegenFremdenAbstimmungsVorschlagIdent = ergebnis
                    .getInt("wmr.gegenFremdenAbstimmungsVorschlagIdent");
            lWeisungMeldungRaw.gemaessEigenemAbstimmungsVorschlagIdent = ergebnis
                    .getInt("wmr.gemaessEigenemAbstimmungsVorschlagIdent");
            lWeisungMeldungRaw.gegenEigenemAbstimmungsVorschlagIdent = ergebnis
                    .getInt("wmr.gegenEigenemAbstimmungsVorschlagIdent");
            lWeisungMeldungRaw.stimmartGesamt = ergebnis.getString("wmr.stimmartGesamt");
            lWeisungMeldungRaw.aktualisieren = ergebnis.getInt("wmr.aktualisieren");

            int i;
            for (i = 0; i < 99; i++) {
                lWeisungMeldungRaw.abgabe[i] = ergebnis.getString("wmr.abgabe" + Integer.toString(i));
            }

        } catch (Exception e) {
            CaBug.drucke("DbWeisungMeldung.decodeErgebnisRaw 001");
            System.err.println(" " + e.getMessage());
        }

        return lWeisungMeldungRaw;
    }

    /** dekodiert die aktuelle Position aus ergebnis in EclWeisungMeldung und gibt dieses zurück*/
    private void decodeErgebnisRaw1(ResultSet ergebnis, EclWeisungMeldungRaw lWeisungMeldungRaw) {

        try {
            int i;
            for (i = 100; i < 199; i++) {
                lWeisungMeldungRaw.abgabe[i] = ergebnis.getString("wmr1.abgabe" + Integer.toString(i));
            }

        } catch (Exception e) {
            CaBug.drucke("DbWeisungMeldung.decodeErgebnisRaw1 001");
            System.err.println(" " + e.getMessage());
        }

        return;
    }

    /** dekodiert die aktuelle Position aus ergebnis in EclWeisungMeldungSplit und gibt dieses zurück*/
    private EclWeisungMeldungSplit decodeErgebnisSplit(ResultSet ergebnis) {
        EclWeisungMeldungSplit lWeisungMeldungSplit = new EclWeisungMeldungSplit();

        try {
            lWeisungMeldungSplit.mandant = ergebnis.getInt("wms.mandant");
            lWeisungMeldungSplit.weisungIdent = ergebnis.getInt("wms.weisungIdent");
            lWeisungMeldungSplit.meldungsIdent = ergebnis.getInt("wms.meldungsIdent");
            lWeisungMeldungSplit.sammelIdent = ergebnis.getInt("wms.sammelIdent");
            lWeisungMeldungSplit.willenserklaerungIdent = ergebnis.getInt("wms.willenserklaerungIdent");

            int i, i1;
            for (i = 0; i < 50; i++) {
                for (i1 = 0; i1 < 10; i1++) {
                    lWeisungMeldungSplit.abgabe[i][i1] = ergebnis
                            .getLong("wms.abgabe" + Integer.toString(i) + "_" + Integer.toString(i1));
                }
                lWeisungMeldungSplit.nichtBerechnen[i] = ergebnis.getInt("wms.nichtBerechnen" + Integer.toString(i));
                lWeisungMeldungSplit.weisungssummeFalsch[i] = ergebnis
                        .getInt("wms.weisungssummeFalsch" + Integer.toString(i));
            }

        } catch (Exception e) {
            CaBug.drucke("DbWeisungMeldung.decodeErgebnisSplit 001");
            System.err.println(" " + e.getMessage());
        }

        return lWeisungMeldungSplit;
    }

    /** dekodiert die aktuelle Position aus ergebnis in lWeisungMeldungSplit**/
    private void decodeErgebnisSplit1(ResultSet ergebnis, EclWeisungMeldungSplit lWeisungMeldungSplit) {

        try {
            int i, i1;
            for (i = 50; i < 100; i++) {
                for (i1 = 0; i1 < 10; i1++) {
                    lWeisungMeldungSplit.abgabe[i][i1] = ergebnis
                            .getLong("wms1.abgabe" + Integer.toString(i) + "_" + Integer.toString(i1));
                }
                lWeisungMeldungSplit.nichtBerechnen[i] = ergebnis.getInt("wms1.nichtBerechnen" + Integer.toString(i));
                lWeisungMeldungSplit.weisungssummeFalsch[i] = ergebnis
                        .getInt("wms1.weisungssummeFalsch" + Integer.toString(i));
            }
        } catch (Exception e) {
            CaBug.drucke("DbWeisungMeldung.decodeErgebnisSplit1 001");
            System.err.println(" " + e.getMessage());
        }

        return;
    }

    /** dekodiert die aktuelle Position aus ergebnis in lWeisungMeldungSplit**/
    private void decodeErgebnisSplit2(ResultSet ergebnis, EclWeisungMeldungSplit lWeisungMeldungSplit) {

        try {
            int i, i1;
            for (i = 100; i < 150; i++) {
                for (i1 = 0; i1 < 10; i1++) {
                    lWeisungMeldungSplit.abgabe[i][i1] = ergebnis
                            .getLong("wms2.abgabe" + Integer.toString(i) + "_" + Integer.toString(i1));
                }
                lWeisungMeldungSplit.nichtBerechnen[i] = ergebnis.getInt("wms2.nichtBerechnen" + Integer.toString(i));
                lWeisungMeldungSplit.weisungssummeFalsch[i] = ergebnis
                        .getInt("wms2.weisungssummeFalsch" + Integer.toString(i));

            }
        } catch (Exception e) {
            CaBug.drucke("DbWeisungMeldung.decodeErgebnisSplit2 001");
            System.err.println(" " + e.getMessage());
        }
        return;
    }

    /** dekodiert die aktuelle Position aus ergebnis in lWeisungMeldungSplit**/
    private void decodeErgebnisSplit3(ResultSet ergebnis, EclWeisungMeldungSplit lWeisungMeldungSplit) {

        try {
            int i, i1;
            for (i = 150; i < 200; i++) {
                for (i1 = 0; i1 < 10; i1++) {
                    lWeisungMeldungSplit.abgabe[i][i1] = ergebnis
                            .getLong("wms3.abgabe" + Integer.toString(i) + "_" + Integer.toString(i1));
                }
                lWeisungMeldungSplit.nichtBerechnen[i] = ergebnis.getInt("wms3.nichtBerechnen" + Integer.toString(i));
                lWeisungMeldungSplit.weisungssummeFalsch[i] = ergebnis
                        .getInt("wms3.weisungssummeFalsch" + Integer.toString(i));

            }
        } catch (Exception e) {
            CaBug.drucke("DbWeisungMeldung.decodeErgebnisSplit3 001");
            System.err.println(" " + e.getMessage());
        }
        return;
    }

    /** dekodiert die aktuelle Position aus ergebnis in EclWeisungMeldungSplitRaw und gibt dieses zurück*/
    private EclWeisungMeldungSplitRaw decodeErgebnisSplitRaw(ResultSet ergebnis) {
        EclWeisungMeldungSplitRaw lWeisungMeldungSplitRaw = new EclWeisungMeldungSplitRaw();

        try {
            lWeisungMeldungSplitRaw.mandant = ergebnis.getInt("wmsr.mandant");
            lWeisungMeldungSplitRaw.weisungIdent = ergebnis.getInt("wmsr.weisungIdent");
            lWeisungMeldungSplitRaw.meldungsIdent = ergebnis.getInt("wmsr.meldungsIdent");
            lWeisungMeldungSplitRaw.sammelIdent = ergebnis.getInt("wmsr.sammelIdent");
            lWeisungMeldungSplitRaw.willenserklaerungIdent = ergebnis.getInt("wmsr.willenserklaerungIdent");

            int i, i1;
            for (i = 0; i < 50; i++) {
                for (i1 = 0; i1 < 10; i1++) {
                    lWeisungMeldungSplitRaw.abgabe[i][i1] = ergebnis
                            .getLong("wmsr.abgabe" + Integer.toString(i) + "_" + Integer.toString(i1));
                }
            }

        } catch (Exception e) {
            CaBug.drucke("DbWeisungMeldung.decodeErgebnisSplitRaw 001");
            System.err.println(" " + e.getMessage());
        }

        return lWeisungMeldungSplitRaw;
    }

    /** dekodiert die aktuelle Position aus ergebnis in lWeisungMeldungSplit**/
    private void decodeErgebnisSplitRaw1(ResultSet ergebnis, EclWeisungMeldungSplitRaw lWeisungMeldungSplitRaw) {

        try {
            int i, i1;
            for (i = 50; i < 100; i++) {
                for (i1 = 0; i1 < 10; i1++) {
                    lWeisungMeldungSplitRaw.abgabe[i][i1] = ergebnis
                            .getLong("wmsr1.abgabe" + Integer.toString(i) + "_" + Integer.toString(i1));
                }
            }
        } catch (Exception e) {
            CaBug.drucke("DbWeisungMeldung.decodeErgebnisSplitRaw1 001");
            System.err.println(" " + e.getMessage());
        }
        return;
    }

    /** dekodiert die aktuelle Position aus ergebnis in lWeisungMeldungSplit**/
    private void decodeErgebnisSplitRaw2(ResultSet ergebnis, EclWeisungMeldungSplitRaw lWeisungMeldungSplitRaw) {

        try {
            int i, i1;
            for (i = 100; i < 150; i++) {
                for (i1 = 0; i1 < 10; i1++) {
                    lWeisungMeldungSplitRaw.abgabe[i][i1] = ergebnis
                            .getLong("wmsr2.abgabe" + Integer.toString(i) + "_" + Integer.toString(i1));
                }

            }
        } catch (Exception e) {
            CaBug.drucke("DbWeisungMeldung.decodeErgebnisSplitRaw2 001");
            System.err.println(" " + e.getMessage());
        }
        return;
    }

    /** dekodiert die aktuelle Position aus ergebnis in lWeisungMeldungSplit**/
    private void decodeErgebnisSplitRaw3(ResultSet ergebnis, EclWeisungMeldungSplitRaw lWeisungMeldungSplitRaw) {

        try {
            int i, i1;
            for (i = 150; i < 200; i++) {
                for (i1 = 0; i1 < 10; i1++) {
                    lWeisungMeldungSplitRaw.abgabe[i][i1] = ergebnis
                            .getLong("wmsr3.abgabe" + Integer.toString(i) + "_" + Integer.toString(i1));
                }
            }
        } catch (Exception e) {
            CaBug.drucke("DbWeisungMeldung.decodeErgebnisSplitRaw3 001");
            System.err.println(" " + e.getMessage());
        }
        return;
    }

    /** Fuellen Prepared Statement mit allen Feldern, beginnend bei offset.
     * Kann sowohl für Insert, als auch für update verwendet werden.
     * 
     * offset= Startposition des ersten Feldes (also z.B. 1)
     */
    private int anzfelder = 413;

    private void fuellePreparedStatementKomplett(PreparedStatement pstm, int offset,
            EclWeisungMeldung lWeisungMeldung) {

        try {
            pstm.setInt(offset, lWeisungMeldung.mandant);
            offset++;
            pstm.setInt(offset, lWeisungMeldung.weisungIdent);
            offset++;
            pstm.setInt(offset, lWeisungMeldung.meldungsIdent);
            offset++;
            pstm.setInt(offset, lWeisungMeldung.sammelIdent);
            offset++;
            pstm.setInt(offset, lWeisungMeldung.skIst);
            offset++;
            pstm.setInt(offset, lWeisungMeldung.willenserklaerungIdent);
            offset++;
            pstm.setInt(offset, lWeisungMeldung.weisungSplit);
            offset++;
            pstm.setInt(offset, lWeisungMeldung.aktiv);
            offset++;
            pstm.setInt(offset, lWeisungMeldung.istWeisungBriefwahl);
            offset++;
            pstm.setInt(offset, lWeisungMeldung.gemaessFremdenAbstimmungsVorschlagIdent);
            offset++;
            pstm.setInt(offset, lWeisungMeldung.gemaessEigenemAbstimmungsVorschlagIdent);
            offset++;
            pstm.setInt(offset, lWeisungMeldung.stimmartGesamt);
            offset++;
            pstm.setInt(offset, lWeisungMeldung.aktualisieren);
            offset++;

            int i;
            for (i = 0; i < 200; i++) {
                pstm.setInt(offset, lWeisungMeldung.abgabe[i]);
                offset++;
            }
            for (i = 0; i < 200; i++) {
                pstm.setInt(offset, lWeisungMeldung.abgabeLautGesamt[i]);
                offset++;
            }

        } catch (SQLException e) {
            CaBug.drucke("DbWeisungMeldung.fuellePreparedStatementKomplett 001");
            e.printStackTrace();
        }
    }

    private int anzfelderRaw = 112;

    private void fuellePreparedStatementKomplettRaw(PreparedStatement pstm, int offset,
            EclWeisungMeldungRaw lWeisungMeldungRaw) {

        try {
            pstm.setInt(offset, lWeisungMeldungRaw.mandant);
            offset++;
            pstm.setInt(offset, lWeisungMeldungRaw.weisungIdent);
            offset++;
            pstm.setInt(offset, lWeisungMeldungRaw.meldungsIdent);
            offset++;
            pstm.setInt(offset, lWeisungMeldungRaw.sammelIdent);
            offset++;
            pstm.setInt(offset, lWeisungMeldungRaw.willenserklaerungIdent);
            offset++;
            pstm.setInt(offset, lWeisungMeldungRaw.bereitsInterpretiert);
            offset++;
            pstm.setInt(offset, lWeisungMeldungRaw.gemaessFremdenAbstimmungsVorschlagIdent);
            offset++;
            pstm.setInt(offset, lWeisungMeldungRaw.gegenFremdenAbstimmungsVorschlagIdent);
            offset++;
            pstm.setInt(offset, lWeisungMeldungRaw.gemaessEigenemAbstimmungsVorschlagIdent);
            offset++;
            pstm.setInt(offset, lWeisungMeldungRaw.gegenEigenemAbstimmungsVorschlagIdent);
            offset++;
            pstm.setString(offset, lWeisungMeldungRaw.stimmartGesamt);
            offset++;
            pstm.setInt(offset, lWeisungMeldungRaw.aktualisieren);
            offset++;

            int i;
            for (i = 0; i < 100; i++) {
                pstm.setString(offset, lWeisungMeldungRaw.abgabe[i]);
                offset++;
            }

        } catch (SQLException e) {
            CaBug.drucke("DbWeisungMeldung.fuellePreparedStatementKomplettRaw 001");
            e.printStackTrace();
        }
    }

    private int anzfelderRaw1 = 105; /*???111*/

    private void fuellePreparedStatementKomplettRaw1(PreparedStatement pstm, int offset,
            EclWeisungMeldungRaw lWeisungMeldungRaw) {

        try {
            pstm.setInt(offset, lWeisungMeldungRaw.mandant);
            offset++;
            pstm.setInt(offset, lWeisungMeldungRaw.weisungIdent);
            offset++;
            pstm.setInt(offset, lWeisungMeldungRaw.meldungsIdent);
            offset++;
            pstm.setInt(offset, lWeisungMeldungRaw.sammelIdent);
            offset++;
            pstm.setInt(offset, lWeisungMeldungRaw.willenserklaerungIdent);
            offset++;

            int i;
            for (i = 100; i < 200; i++) {
                pstm.setString(offset, lWeisungMeldungRaw.abgabe[i]);
                offset++;
            }
        } catch (SQLException e) {
            CaBug.drucke("DbWeisungMeldung.fuellePreparedStatementKomplettRaw1 001");
            e.printStackTrace();
        }
    }

    private int anzfelderSplit = 605;

    private void fuellePreparedStatementKomplettSplit(PreparedStatement pstm, int offset,
            EclWeisungMeldungSplit lWeisungMeldungSplit) {

        try {
            pstm.setInt(offset, lWeisungMeldungSplit.mandant);
            offset++;
            pstm.setInt(offset, lWeisungMeldungSplit.weisungIdent);
            offset++;
            pstm.setInt(offset, lWeisungMeldungSplit.meldungsIdent);
            offset++;
            pstm.setInt(offset, lWeisungMeldungSplit.sammelIdent);
            offset++;
            pstm.setInt(offset, lWeisungMeldungSplit.willenserklaerungIdent);
            offset++;

            int i, i1;
            for (i = 0; i < 50; i++) {
                for (i1 = 0; i1 < 10; i1++) {
                    pstm.setLong(offset, lWeisungMeldungSplit.abgabe[i][i1]);
                    offset++;
                }
            }
            for (i = 0; i < 50; i++) {
                pstm.setInt(offset, lWeisungMeldungSplit.nichtBerechnen[i]);
                offset++;
            }
            for (i = 0; i < 50; i++) {
                pstm.setInt(offset, lWeisungMeldungSplit.weisungssummeFalsch[i]);
                offset++;
            }

        } catch (SQLException e) {
            CaBug.drucke("DbWeisungMeldung.fuellePreparedStatementKomplettSplit 001");
            e.printStackTrace();
        }

    }

    private int anzfelderSplit1 = 605;

    private void fuellePreparedStatementKomplettSplit1(PreparedStatement pstm, int offset,
            EclWeisungMeldungSplit lWeisungMeldungSplit) {

        try {
            pstm.setInt(offset, lWeisungMeldungSplit.mandant);
            offset++;
            pstm.setInt(offset, lWeisungMeldungSplit.weisungIdent);
            offset++;
            pstm.setInt(offset, lWeisungMeldungSplit.meldungsIdent);
            offset++;
            pstm.setInt(offset, lWeisungMeldungSplit.sammelIdent);
            offset++;
            pstm.setInt(offset, lWeisungMeldungSplit.willenserklaerungIdent);
            offset++;

            int i, i1;
            for (i = 50; i < 100; i++) {
                for (i1 = 0; i1 < 10; i1++) {
                    pstm.setLong(offset, lWeisungMeldungSplit.abgabe[i][i1]);
                    offset++;
                }
            }
            for (i = 50; i < 100; i++) {
                pstm.setInt(offset, lWeisungMeldungSplit.nichtBerechnen[i]);
                offset++;
            }
            for (i = 50; i < 100; i++) {
                pstm.setInt(offset, lWeisungMeldungSplit.weisungssummeFalsch[i]);
                offset++;
            }
        } catch (SQLException e) {
            CaBug.drucke("DbWeisungMeldung.fuellePreparedStatementKomplettSplit1 001");
            e.printStackTrace();
        }
    }

    private int anzfelderSplit2 = 605;

    private void fuellePreparedStatementKomplettSplit2(PreparedStatement pstm, int offset,
            EclWeisungMeldungSplit lWeisungMeldungSplit) {

        try {
            pstm.setInt(offset, lWeisungMeldungSplit.mandant);
            offset++;
            pstm.setInt(offset, lWeisungMeldungSplit.weisungIdent);
            offset++;
            pstm.setInt(offset, lWeisungMeldungSplit.meldungsIdent);
            offset++;
            pstm.setInt(offset, lWeisungMeldungSplit.sammelIdent);
            offset++;
            pstm.setInt(offset, lWeisungMeldungSplit.willenserklaerungIdent);
            offset++;

            int i, i1;
            for (i = 100; i < 150; i++) {
                for (i1 = 0; i1 < 10; i1++) {
                    pstm.setLong(offset, lWeisungMeldungSplit.abgabe[i][i1]);
                    offset++;
                }
            }
            for (i = 100; i < 150; i++) {
                pstm.setInt(offset, lWeisungMeldungSplit.nichtBerechnen[i]);
                offset++;
            }
            for (i = 100; i < 150; i++) {
                pstm.setInt(offset, lWeisungMeldungSplit.weisungssummeFalsch[i]);
                offset++;
            }
        } catch (SQLException e) {
            CaBug.drucke("DbWeisungMeldung.fuellePreparedStatementKomplettSplit2 001");
            e.printStackTrace();
        }
    }

    private int anzfelderSplit3 = 605;

    private void fuellePreparedStatementKomplettSplit3(PreparedStatement pstm, int offset,
            EclWeisungMeldungSplit lWeisungMeldungSplit) {

        try {
            pstm.setInt(offset, lWeisungMeldungSplit.mandant);
            offset++;
            pstm.setInt(offset, lWeisungMeldungSplit.weisungIdent);
            offset++;
            pstm.setInt(offset, lWeisungMeldungSplit.meldungsIdent);
            offset++;
            pstm.setInt(offset, lWeisungMeldungSplit.sammelIdent);
            offset++;
            pstm.setInt(offset, lWeisungMeldungSplit.willenserklaerungIdent);
            offset++;

            int i, i1;
            for (i = 150; i < 200; i++) {
                for (i1 = 0; i1 < 10; i1++) {
                    pstm.setLong(offset, lWeisungMeldungSplit.abgabe[i][i1]);
                    offset++;
                }
            }
            for (i = 150; i < 200; i++) {
                pstm.setInt(offset, lWeisungMeldungSplit.nichtBerechnen[i]);
                offset++;
            }
            for (i = 150; i < 200; i++) {
                pstm.setInt(offset, lWeisungMeldungSplit.weisungssummeFalsch[i]);
                offset++;
            }

        } catch (SQLException e) {
            CaBug.drucke("DbWeisungMeldung.fuellePreparedStatementKomplettSplit3 001");
            e.printStackTrace();
        }

    }

    private int anzfelderSplitRaw = 505;

    private void fuellePreparedStatementKomplettSplitRaw(PreparedStatement pstm, int offset,
            EclWeisungMeldungSplitRaw lWeisungMeldungSplitRaw) {

        try {
            pstm.setInt(offset, lWeisungMeldungSplitRaw.mandant);
            offset++;
            pstm.setInt(offset, lWeisungMeldungSplitRaw.weisungIdent);
            offset++;
            pstm.setInt(offset, lWeisungMeldungSplitRaw.meldungsIdent);
            offset++;
            pstm.setInt(offset, lWeisungMeldungSplitRaw.sammelIdent);
            offset++;
            pstm.setInt(offset, lWeisungMeldungSplitRaw.willenserklaerungIdent);
            offset++;

            int i, i1;
            for (i = 0; i < 50; i++) {
                for (i1 = 0; i1 < 10; i1++) {
                    pstm.setLong(offset, lWeisungMeldungSplitRaw.abgabe[i][i1]);
                    offset++;
                }
            }

        } catch (SQLException e) {
            CaBug.drucke("DbWeisungMeldung.fuellePreparedStatementKomplettSplitRaw 001");
            e.printStackTrace();
        }

    }

    private int anzfelderSplitRaw1 = 505;

    private void fuellePreparedStatementKomplettSplitRaw1(PreparedStatement pstm, int offset,
            EclWeisungMeldungSplitRaw lWeisungMeldungSplitRaw) {

        try {
            pstm.setInt(offset, lWeisungMeldungSplitRaw.mandant);
            offset++;
            pstm.setInt(offset, lWeisungMeldungSplitRaw.weisungIdent);
            offset++;
            pstm.setInt(offset, lWeisungMeldungSplitRaw.meldungsIdent);
            offset++;
            pstm.setInt(offset, lWeisungMeldungSplitRaw.sammelIdent);
            offset++;
            pstm.setInt(offset, lWeisungMeldungSplitRaw.willenserklaerungIdent);
            offset++;

            int i, i1;
            for (i = 50; i < 100; i++) {
                for (i1 = 0; i1 < 10; i1++) {
                    pstm.setLong(offset, lWeisungMeldungSplitRaw.abgabe[i][i1]);
                    offset++;
                }
            }

        } catch (SQLException e) {
            CaBug.drucke("DbWeisungMeldung.fuellePreparedStatementKomplettSplitRaw1 001");
            e.printStackTrace();
        }

    }

    private int anzfelderSplitRaw2 = 505;

    private void fuellePreparedStatementKomplettSplitRaw2(PreparedStatement pstm, int offset,
            EclWeisungMeldungSplitRaw lWeisungMeldungSplitRaw) {

        try {
            pstm.setInt(offset, lWeisungMeldungSplitRaw.mandant);
            offset++;
            pstm.setInt(offset, lWeisungMeldungSplitRaw.weisungIdent);
            offset++;
            pstm.setInt(offset, lWeisungMeldungSplitRaw.meldungsIdent);
            offset++;
            pstm.setInt(offset, lWeisungMeldungSplitRaw.sammelIdent);
            offset++;
            pstm.setInt(offset, lWeisungMeldungSplitRaw.willenserklaerungIdent);
            offset++;

            int i, i1;
            for (i = 100; i < 150; i++) {
                for (i1 = 0; i1 < 10; i1++) {
                    pstm.setLong(offset, lWeisungMeldungSplitRaw.abgabe[i][i1]);
                    offset++;
                }
            }

        } catch (SQLException e) {
            CaBug.drucke("DbWeisungMeldung.fuellePreparedStatementKomplettSplitRaw2 001");
            e.printStackTrace();
        }

    }

    private int anzfelderSplitRaw3 = 505;

    private void fuellePreparedStatementKomplettSplitRaw3(PreparedStatement pstm, int offset,
            EclWeisungMeldungSplitRaw lWeisungMeldungSplitRaw) {

        try {
            pstm.setInt(offset, lWeisungMeldungSplitRaw.mandant);
            offset++;
            pstm.setInt(offset, lWeisungMeldungSplitRaw.weisungIdent);
            offset++;
            pstm.setInt(offset, lWeisungMeldungSplitRaw.meldungsIdent);
            offset++;
            pstm.setInt(offset, lWeisungMeldungSplitRaw.sammelIdent);
            offset++;
            pstm.setInt(offset, lWeisungMeldungSplitRaw.willenserklaerungIdent);
            offset++;

            int i, i1;
            for (i = 150; i < 200; i++) {
                for (i1 = 0; i1 < 10; i1++) {
                    pstm.setLong(offset, lWeisungMeldungSplitRaw.abgabe[i][i1]);
                    offset++;
                }
            }

        } catch (SQLException e) {
            CaBug.drucke("DbWeisungMeldung.fuellePreparedStatementKomplettSplitRaw3 001");
            e.printStackTrace();
        }

    }

    /**Hinweise: 
     * > es wird immer auch Raw mit Erzeugt! Die Identifikationsfelder von Raw werden in dieser Funktion gefüllt - unabhängig davon
     * wie sie in lWeisungMeldungRaw gefüllt sind
     * > split wird automatisch mit erzeugt, wenn in lWeisungMeldung entsprechende Verweise enthalten sind. Auch in diesem Fall werden
     * dann die Identifikationsfelder in dieser Funktion gefüllt*/
    public int insert(EclWeisungMeldung lWeisungMeldung, EclWeisungMeldungRaw lWeisungMeldungRaw) {

        int erg, satznr;
        EclWeisungMeldungSplit lWeisungMeldungSplit;
        EclWeisungMeldungSplitRaw lWeisungMeldungSplitRaw;

        lWeisungMeldungSplit = lWeisungMeldung.weisungMeldungSplit;
        lWeisungMeldungSplitRaw = lWeisungMeldungRaw.weisungMeldungSplitRaw;

        /* Start Transaktion */
        dbBasis.beginTransaction();

        /* neue InterneIdent vergeben */
        erg = dbBasis.getInterneIdentWeisungMeldung();
        if (erg < 1) {
            CaBug.drucke("DbWeisungMeldung.insert 002");
            dbBasis.endTransaction();
            return (erg);
        }
        satznr = erg;

        /*
         * tbl_weisungMeldung*/

        lWeisungMeldung.weisungIdent = satznr;

        lWeisungMeldung.mandant = dbBundle.clGlobalVar.mandant;

        /* Verarbeitungshinweise: 
         * 	>	nachdem InterneIdent immer eindeutig vergeben werden, ist prinzipiell eine "Doppeleinfügung"
         * 		von InterneIdent nicht möglich
         */

        try {

            /*Felder Neuanlage füllen*/
            String sql1 = "INSERT INTO " + dbBundle.getSchemaMandant() + "tbl_weisungMeldung " + "("
                    + "mandant, weisungIdent, " + "meldungsIdent, sammelIdent, skIst, willenserklaerungIdent, "
                    + "weisungSplit, " + "aktiv, istWeisungBriefwahl, "
                    + "gemaessFremdenAbstimmungsVorschlagIdent, gemaessEigenemAbstimmungsVorschlagIdent, stimmartGesamt, aktualisieren";

            int i;
            for (i = 0; i < 200; i++) {
                sql1 = sql1 + ", abgabe" + Integer.toString(i);
            }
            for (i = 0; i < 200; i++) {
                sql1 = sql1 + ", abgabeLautGesamt" + Integer.toString(i);
            }
            sql1 = sql1 + ")" + "VALUES (" + "?, ?, " + "?, ?, ?, ?, " + "?, " + "?, ?, " + "?, ?, ?, ?";
            for (i = 0; i < 200; i++) {
                sql1 = sql1 + ", ?, ?";
            }

            sql1 = sql1 + " )";
            PreparedStatement pstm1 = verbindung.prepareStatement(sql1, ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);

            fuellePreparedStatementKomplett(pstm1, 1, lWeisungMeldung);

            erg = 0; /*Falls Duplicate Key, dann wird exception geschmissen und erg wohl nicht gefüllt!*/
            //			System.out.println("vor Update");
            erg = pstm1.executeUpdate();
            //			System.out.println("nach Update");
            pstm1.close();
        } catch (Exception e2) {
            CaBug.drucke("DbWeisungMeldung.insert 001");
            System.err.println(" " + e2.getMessage());
        }

        if (erg == 0) {/*Fehler beim Einfügen - d.h. bereits vorhanden*/
            dbBasis.rollbackTransaction();
            dbBasis.endTransaction();
            return (-1);
        }

        /** tbl_weisungMeldungRaw*/
        lWeisungMeldungRaw.weisungIdent = satznr;

        lWeisungMeldungRaw.mandant = dbBundle.clGlobalVar.mandant;
        lWeisungMeldungRaw.meldungsIdent = lWeisungMeldung.meldungsIdent;
        lWeisungMeldungRaw.sammelIdent = lWeisungMeldung.sammelIdent;
        lWeisungMeldungRaw.willenserklaerungIdent = lWeisungMeldung.willenserklaerungIdent;

        /* Verarbeitungshinweise: 
         * 	>	nachdem InterneIdent immer eindeutig vergeben werden, ist prinzipiell eine "Doppeleinfügung"
         * 		von InterneIdent nicht möglich
         */

        try {

            /*Felder Neuanlage füllen*/
            String sql1 = "INSERT INTO " + dbBundle.getSchemaMandant() + "tbl_weisungMeldungRaw " + "("
                    + "mandant, weisungIdent, " + "meldungsIdent, sammelIdent, willenserklaerungIdent, "
                    + "bereitsInterpretiert, "
                    + "gemaessFremdenAbstimmungsVorschlagIdent, gegenFremdenAbstimmungsVorschlagIdent, "
                    + "gemaessEigenemAbstimmungsVorschlagIdent, gegenEigenemAbstimmungsVorschlagIdent, "
                    + "stimmartGesamt, aktualisieren";

            int i;
            for (i = 0; i < 100; i++) {
                sql1 = sql1 + ", abgabe" + Integer.toString(i);
            }

            sql1 = sql1 + ")" + "VALUES (" + "?, ?, " + "?, ?, ?, " + "?, " + "?, ?, " + "?, ?, " + "?, ?";
            for (i = 0; i < 100; i++) {
                sql1 = sql1 + ", ?";
            }

            sql1 = sql1 + " )";
            PreparedStatement pstm1 = verbindung.prepareStatement(sql1, ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);

            fuellePreparedStatementKomplettRaw(pstm1, 1, lWeisungMeldungRaw);

            erg = 0; /*Falls Duplicate Key, dann wird exception geschmissen und erg wohl nicht gefüllt!*/
            //			System.out.println("vor Update");
            erg = pstm1.executeUpdate();
            //			System.out.println("nach Update");
            pstm1.close();
        } catch (Exception e2) {
            CaBug.drucke("DbWeisungMeldung.insert 003");
            System.err.println(" " + e2.getMessage());
        }

        if (erg == 0) {/*Fehler beim Einfügen - d.h. bereits vorhanden*/
            dbBasis.rollbackTransaction();
            dbBasis.endTransaction();
            return (-1);
        }

        /** tbl_weisungMeldungRaw1*/
        lWeisungMeldungRaw.weisungIdent = satznr;

        lWeisungMeldungRaw.mandant = dbBundle.clGlobalVar.mandant;
        lWeisungMeldungRaw.meldungsIdent = lWeisungMeldung.meldungsIdent;
        lWeisungMeldungRaw.sammelIdent = lWeisungMeldung.sammelIdent;
        lWeisungMeldungRaw.willenserklaerungIdent = lWeisungMeldung.willenserklaerungIdent;

        /* Verarbeitungshinweise: 
         * 	>	nachdem InterneIdent immer eindeutig vergeben werden, ist prinzipiell eine "Doppeleinfügung"
         * 		von InterneIdent nicht möglich
         */

        try {

            /*Felder Neuanlage füllen*/
            String sql1 = "INSERT INTO " + dbBundle.getSchemaMandant() + "tbl_weisungMeldungRaw1 " + "("
                    + "mandant, weisungIdent, " + "meldungsIdent, sammelIdent, willenserklaerungIdent";

            int i;
            for (i = 100; i < 200; i++) {
                sql1 = sql1 + ", abgabe" + Integer.toString(i);
            }

            sql1 = sql1 + ")" + "VALUES (" + "?, ?, " + "?, ?, ?";
            for (i = 0; i < 100; i++) {
                sql1 = sql1 + ", ?";
            }

            sql1 = sql1 + " )";
            PreparedStatement pstm1 = verbindung.prepareStatement(sql1, ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);

            fuellePreparedStatementKomplettRaw1(pstm1, 1, lWeisungMeldungRaw);

            erg = 0; /*Falls Duplicate Key, dann wird exception geschmissen und erg wohl nicht gefüllt!*/
            //			System.out.println("vor Update");
            erg = pstm1.executeUpdate();
            //			System.out.println("nach Update");
            pstm1.close();
        } catch (Exception e2) {
            CaBug.drucke("DbWeisungMeldung.insert 004");
            System.err.println(" " + e2.getMessage());
        }

        if (erg == 0) {/*Fehler beim Einfügen - d.h. bereits vorhanden*/
            dbBasis.rollbackTransaction();
            dbBasis.endTransaction();
            return (-1);
        }

        if (lWeisungMeldung.weisungSplit == 1) {
            /*Weisungssplit vorhanden - Abspeichern*/

            /** tbl_weisungMeldungSplit*/
            lWeisungMeldungSplit.weisungIdent = satznr;

            lWeisungMeldungSplit.mandant = dbBundle.clGlobalVar.mandant;
            lWeisungMeldungSplit.meldungsIdent = lWeisungMeldung.meldungsIdent;
            lWeisungMeldungSplit.sammelIdent = lWeisungMeldung.sammelIdent;
            lWeisungMeldungSplit.willenserklaerungIdent = lWeisungMeldung.willenserklaerungIdent;

            /* Verarbeitungshinweise: 
             * 	>	nachdem InterneIdent immer eindeutig vergeben werden, ist prinzipiell eine "Doppeleinfügung"
             * 		von InterneIdent nicht möglich
             */

            try {

                /*Felder Neuanlage füllen*/
                String sql1 = "INSERT INTO " + dbBundle.getSchemaMandant() + "tbl_weisungMeldungSplit " + "("
                        + "mandant, weisungIdent, " + "meldungsIdent, sammelIdent, willenserklaerungIdent";

                int i, i1;
                for (i = 0; i < 50; i++) {
                    for (i1 = 0; i1 < 10; i1++) {
                        sql1 = sql1 + ", abgabe" + Integer.toString(i) + "_" + Integer.toString(i1);
                    }
                }
                for (i = 0; i < 50; i++) {
                    sql1 = sql1 + ", nichtBerechnen" + Integer.toString(i);
                }
                for (i = 0; i < 50; i++) {
                    sql1 = sql1 + ", weisungssummeFalsch" + Integer.toString(i);
                }

                sql1 = sql1 + ")" + "VALUES (" + "?, ?, " + "?, ?, ?";
                for (i = 0; i < 50; i++) {
                    for (i1 = 0; i1 < 10; i1++) {
                        sql1 = sql1 + ", ?";
                    }
                }
                for (i = 0; i < 50; i++) {
                    sql1 = sql1 + ", ?";
                }
                for (i = 0; i < 50; i++) {
                    sql1 = sql1 + ", ?";
                }

                sql1 = sql1 + " )";
                PreparedStatement pstm1 = verbindung.prepareStatement(sql1, ResultSet.TYPE_SCROLL_INSENSITIVE,
                        ResultSet.CONCUR_READ_ONLY);

                fuellePreparedStatementKomplettSplit(pstm1, 1, lWeisungMeldungSplit);

                erg = 0; /*Falls Duplicate Key, dann wird exception geschmissen und erg wohl nicht gefüllt!*/
                //				System.out.println("vor Update");
                erg = pstm1.executeUpdate();
                //				System.out.println("nach Update");
                pstm1.close();
            } catch (Exception e2) {
                CaBug.drucke("DbWeisungMeldung.insert 004");
                System.err.println(" " + e2.getMessage());
            }

            if (erg == 0) {/*Fehler beim Einfügen - d.h. bereits vorhanden*/
                dbBasis.rollbackTransaction();
                dbBasis.endTransaction();
                return (-1);
            }

            /** tbl_weisungMeldungSplit1*/

            /* Verarbeitungshinweise: 
             * 	>	nachdem InterneIdent immer eindeutig vergeben werden, ist prinzipiell eine "Doppeleinfügung"
             * 		von InterneIdent nicht möglich
             */

            try {

                /*Felder Neuanlage füllen*/
                String sql1 = "INSERT INTO " + dbBundle.getSchemaMandant() + "tbl_weisungMeldungSplit1 " + "("
                        + "mandant, weisungIdent, " + "meldungsIdent, sammelIdent, willenserklaerungIdent";

                int i, i1;
                for (i = 50; i < 100; i++) {
                    for (i1 = 0; i1 < 10; i1++) {
                        sql1 = sql1 + ", abgabe" + Integer.toString(i) + "_" + Integer.toString(i1);
                    }
                }
                for (i = 50; i < 100; i++) {
                    sql1 = sql1 + ", nichtBerechnen" + Integer.toString(i);
                }
                for (i = 50; i < 100; i++) {
                    sql1 = sql1 + ", weisungssummeFalsch" + Integer.toString(i);
                }

                sql1 = sql1 + ")" + "VALUES (" + "?, ?, " + "?, ?, ?";
                for (i = 0; i < 50; i++) {
                    for (i1 = 0; i1 < 10; i1++) {
                        sql1 = sql1 + ", ?";
                    }
                }
                for (i = 0; i < 50; i++) {
                    sql1 = sql1 + ", ?";
                }
                for (i = 0; i < 50; i++) {
                    sql1 = sql1 + ", ?";
                }

                sql1 = sql1 + " )";
                PreparedStatement pstm1 = verbindung.prepareStatement(sql1, ResultSet.TYPE_SCROLL_INSENSITIVE,
                        ResultSet.CONCUR_READ_ONLY);

                fuellePreparedStatementKomplettSplit1(pstm1, 1, lWeisungMeldungSplit);

                erg = 0; /*Falls Duplicate Key, dann wird exception geschmissen und erg wohl nicht gefüllt!*/
                //				System.out.println("vor Update");
                erg = pstm1.executeUpdate();
                //				System.out.println("nach Update");
                pstm1.close();
            } catch (Exception e2) {
                CaBug.drucke("DbWeisungMeldung.insert 005");
                System.err.println(" " + e2.getMessage());
            }

            if (erg == 0) {/*Fehler beim Einfügen - d.h. bereits vorhanden*/
                dbBasis.rollbackTransaction();
                dbBasis.endTransaction();
                return (-1);
            }

            /** tbl_weisungMeldungSplit2*/

            /* Verarbeitungshinweise: 
             * 	>	nachdem InterneIdent immer eindeutig vergeben werden, ist prinzipiell eine "Doppeleinfügung"
             * 		von InterneIdent nicht möglich
             */

            try {

                /*Felder Neuanlage füllen*/
                String sql1 = "INSERT INTO " + dbBundle.getSchemaMandant() + "tbl_weisungMeldungSplit2 " + "("
                        + "mandant, weisungIdent, " + "meldungsIdent, sammelIdent, willenserklaerungIdent";

                int i, i1;
                for (i = 100; i < 150; i++) {
                    for (i1 = 0; i1 < 10; i1++) {
                        sql1 = sql1 + ", abgabe" + Integer.toString(i) + "_" + Integer.toString(i1);
                    }
                }
                for (i = 100; i < 150; i++) {
                    sql1 = sql1 + ", nichtBerechnen" + Integer.toString(i);
                }
                for (i = 100; i < 150; i++) {
                    sql1 = sql1 + ", weisungssummeFalsch" + Integer.toString(i);
                }

                sql1 = sql1 + ")" + "VALUES (" + "?, ?, " + "?, ?, ?";
                for (i = 0; i < 50; i++) {
                    for (i1 = 0; i1 < 10; i1++) {
                        sql1 = sql1 + ", ?";
                    }
                }
                for (i = 0; i < 50; i++) {
                    sql1 = sql1 + ", ?";
                }
                for (i = 0; i < 50; i++) {
                    sql1 = sql1 + ", ?";
                }

                sql1 = sql1 + " )";
                PreparedStatement pstm1 = verbindung.prepareStatement(sql1, ResultSet.TYPE_SCROLL_INSENSITIVE,
                        ResultSet.CONCUR_READ_ONLY);

                fuellePreparedStatementKomplettSplit2(pstm1, 1, lWeisungMeldungSplit);

                erg = 0; /*Falls Duplicate Key, dann wird exception geschmissen und erg wohl nicht gefüllt!*/
                //				System.out.println("vor Update");
                erg = pstm1.executeUpdate();
                //				System.out.println("nach Update");
                pstm1.close();
            } catch (Exception e2) {
                CaBug.drucke("DbWeisungMeldung.insert 006");
                System.err.println(" " + e2.getMessage());
            }

            if (erg == 0) {/*Fehler beim Einfügen - d.h. bereits vorhanden*/
                dbBasis.rollbackTransaction();
                dbBasis.endTransaction();
                return (-1);
            }

            /** tbl_weisungMeldungSplit3*/

            /* Verarbeitungshinweise: 
             * 	>	nachdem InterneIdent immer eindeutig vergeben werden, ist prinzipiell eine "Doppeleinfügung"
             * 		von InterneIdent nicht möglich
             */

            try {

                /*Felder Neuanlage füllen*/
                String sql1 = "INSERT INTO " + dbBundle.getSchemaMandant() + "tbl_weisungMeldungSplit3 " + "("
                        + "mandant, weisungIdent, " + "meldungsIdent, sammelIdent, willenserklaerungIdent";

                int i, i1;
                for (i = 150; i < 200; i++) {
                    for (i1 = 0; i1 < 10; i1++) {
                        sql1 = sql1 + ", abgabe" + Integer.toString(i) + "_" + Integer.toString(i1);
                    }
                }
                for (i = 150; i < 200; i++) {
                    sql1 = sql1 + ", nichtBerechnen" + Integer.toString(i);
                }
                for (i = 150; i < 200; i++) {
                    sql1 = sql1 + ", weisungssummeFalsch" + Integer.toString(i);
                }

                sql1 = sql1 + ")" + "VALUES (" + "?, ?, " + "?, ?, ?";
                for (i = 0; i < 50; i++) {
                    for (i1 = 0; i1 < 10; i1++) {
                        sql1 = sql1 + ", ?";
                    }
                }
                for (i = 0; i < 50; i++) {
                    sql1 = sql1 + ", ?";
                }
                for (i = 0; i < 50; i++) {
                    sql1 = sql1 + ", ?";
                }

                sql1 = sql1 + " )";
                PreparedStatement pstm1 = verbindung.prepareStatement(sql1, ResultSet.TYPE_SCROLL_INSENSITIVE,
                        ResultSet.CONCUR_READ_ONLY);

                fuellePreparedStatementKomplettSplit3(pstm1, 1, lWeisungMeldungSplit);

                erg = 0; /*Falls Duplicate Key, dann wird exception geschmissen und erg wohl nicht gefüllt!*/
                //				System.out.println("vor Update");
                erg = pstm1.executeUpdate();
                //				System.out.println("nach Update");
                pstm1.close();
            } catch (Exception e2) {
                CaBug.drucke("DbWeisungMeldung.insert 006");
                System.err.println(" " + e2.getMessage());
            }

            if (erg == 0) {/*Fehler beim Einfügen - d.h. bereits vorhanden*/
                dbBasis.rollbackTransaction();
                dbBasis.endTransaction();
                return (-1);
            }

            /** tbl_weisungMeldungSplitRaw*/
            lWeisungMeldungSplitRaw.weisungIdent = satznr;

            lWeisungMeldungSplitRaw.mandant = dbBundle.clGlobalVar.mandant;
            lWeisungMeldungSplitRaw.meldungsIdent = lWeisungMeldung.meldungsIdent;
            lWeisungMeldungSplitRaw.sammelIdent = lWeisungMeldung.sammelIdent;
            lWeisungMeldungSplitRaw.willenserklaerungIdent = lWeisungMeldung.willenserklaerungIdent;

            /* Verarbeitungshinweise: 
             * 	>	nachdem InterneIdent immer eindeutig vergeben werden, ist prinzipiell eine "Doppeleinfügung"
             * 		von InterneIdent nicht möglich
             */

            try {

                /*Felder Neuanlage füllen*/
                String sql1 = "INSERT INTO " + dbBundle.getSchemaMandant() + "tbl_weisungMeldungSplitRaw " + "("
                        + "mandant, weisungIdent, " + "meldungsIdent, sammelIdent, willenserklaerungIdent";

                int i, i1;
                for (i = 0; i < 50; i++) {
                    for (i1 = 0; i1 < 10; i1++) {
                        sql1 = sql1 + ", abgabe" + Integer.toString(i) + "_" + Integer.toString(i1);
                    }
                }

                sql1 = sql1 + ")" + "VALUES (" + "?, ?, " + "?, ?, ?";
                for (i = 0; i < 50; i++) {
                    for (i1 = 0; i1 < 10; i1++) {
                        sql1 = sql1 + ", ?";
                    }
                }

                sql1 = sql1 + " )";
                PreparedStatement pstm1 = verbindung.prepareStatement(sql1, ResultSet.TYPE_SCROLL_INSENSITIVE,
                        ResultSet.CONCUR_READ_ONLY);

                fuellePreparedStatementKomplettSplitRaw(pstm1, 1, lWeisungMeldungSplitRaw);

                erg = 0; /*Falls Duplicate Key, dann wird exception geschmissen und erg wohl nicht gefüllt!*/
                //				System.out.println("vor Update");
                erg = pstm1.executeUpdate();
                //				System.out.println("nach Update");
                pstm1.close();
            } catch (Exception e2) {
                CaBug.drucke("DbWeisungMeldung.insert 007");
                System.err.println(" " + e2.getMessage());
            }

            if (erg == 0) {/*Fehler beim Einfügen - d.h. bereits vorhanden*/
                dbBasis.rollbackTransaction();
                dbBasis.endTransaction();
                return (-1);
            }

            /** tbl_weisungMeldungSplitRaw1*/

            /* Verarbeitungshinweise: 
             * 	>	nachdem InterneIdent immer eindeutig vergeben werden, ist prinzipiell eine "Doppeleinfügung"
             * 		von InterneIdent nicht möglich
             */

            try {

                /*Felder Neuanlage füllen*/
                String sql1 = "INSERT INTO " + dbBundle.getSchemaMandant() + "tbl_weisungMeldungSplitRaw1 " + "("
                        + "mandant, weisungIdent, " + "meldungsIdent, sammelIdent, willenserklaerungIdent";

                int i, i1;
                for (i = 50; i < 100; i++) {
                    for (i1 = 0; i1 < 10; i1++) {
                        sql1 = sql1 + ", abgabe" + Integer.toString(i) + "_" + Integer.toString(i1);
                    }
                }

                sql1 = sql1 + ")" + "VALUES (" + "?, ?, " + "?, ?, ?";
                for (i = 0; i < 50; i++) {
                    for (i1 = 0; i1 < 10; i1++) {
                        sql1 = sql1 + ", ?";
                    }
                }

                sql1 = sql1 + " )";
                PreparedStatement pstm1 = verbindung.prepareStatement(sql1, ResultSet.TYPE_SCROLL_INSENSITIVE,
                        ResultSet.CONCUR_READ_ONLY);

                fuellePreparedStatementKomplettSplitRaw1(pstm1, 1, lWeisungMeldungSplitRaw);

                erg = 0; /*Falls Duplicate Key, dann wird exception geschmissen und erg wohl nicht gefüllt!*/
                //				System.out.println("vor Update");
                erg = pstm1.executeUpdate();
                //				System.out.println("nach Update");
                pstm1.close();
            } catch (Exception e2) {
                CaBug.drucke("DbWeisungMeldung.insert 008");
                System.err.println(" " + e2.getMessage());
            }

            if (erg == 0) {/*Fehler beim Einfügen - d.h. bereits vorhanden*/
                dbBasis.rollbackTransaction();
                dbBasis.endTransaction();
                return (-1);
            }

            /** tbl_weisungMeldungSplit2*/

            /* Verarbeitungshinweise: 
             * 	>	nachdem InterneIdent immer eindeutig vergeben werden, ist prinzipiell eine "Doppeleinfügung"
             * 		von InterneIdent nicht möglich
             */

            try {

                /*Felder Neuanlage füllen*/
                String sql1 = "INSERT INTO " + dbBundle.getSchemaMandant() + "tbl_weisungMeldungSplitRaw2 " + "("
                        + "mandant, weisungIdent, " + "meldungsIdent, sammelIdent, willenserklaerungIdent";

                int i, i1;
                for (i = 100; i < 150; i++) {
                    for (i1 = 0; i1 < 10; i1++) {
                        sql1 = sql1 + ", abgabe" + Integer.toString(i) + "_" + Integer.toString(i1);
                    }
                }

                sql1 = sql1 + ")" + "VALUES (" + "?, ?, " + "?, ?, ?";
                for (i = 0; i < 50; i++) {
                    for (i1 = 0; i1 < 10; i1++) {
                        sql1 = sql1 + ", ?";
                    }
                }

                sql1 = sql1 + " )";
                PreparedStatement pstm1 = verbindung.prepareStatement(sql1, ResultSet.TYPE_SCROLL_INSENSITIVE,
                        ResultSet.CONCUR_READ_ONLY);

                fuellePreparedStatementKomplettSplitRaw2(pstm1, 1, lWeisungMeldungSplitRaw);

                erg = 0; /*Falls Duplicate Key, dann wird exception geschmissen und erg wohl nicht gefüllt!*/
                //				System.out.println("vor Update");
                erg = pstm1.executeUpdate();
                //				System.out.println("nach Update");
                pstm1.close();
            } catch (Exception e2) {
                CaBug.drucke("DbWeisungMeldung.insert 010");
                System.err.println(" " + e2.getMessage());
            }

            if (erg == 0) {/*Fehler beim Einfügen - d.h. bereits vorhanden*/
                dbBasis.rollbackTransaction();
                dbBasis.endTransaction();
                return (-1);
            }

            /** tbl_weisungMeldungSplitRaw3*/

            /* Verarbeitungshinweise: 
             * 	>	nachdem InterneIdent immer eindeutig vergeben werden, ist prinzipiell eine "Doppeleinfügung"
             * 		von InterneIdent nicht möglich
             */

            try {

                /*Felder Neuanlage füllen*/
                String sql1 = "INSERT INTO " + dbBundle.getSchemaMandant() + "tbl_weisungMeldungSplitRaw3 " + "("
                        + "mandant, weisungIdent, " + "meldungsIdent, sammelIdent, willenserklaerungIdent";

                int i, i1;
                for (i = 150; i < 200; i++) {
                    for (i1 = 0; i1 < 10; i1++) {
                        sql1 = sql1 + ", abgabe" + Integer.toString(i) + "_" + Integer.toString(i1);
                    }
                }

                sql1 = sql1 + ")" + "VALUES (" + "?, ?, " + "?, ?, ?";
                for (i = 0; i < 50; i++) {
                    for (i1 = 0; i1 < 10; i1++) {
                        sql1 = sql1 + ", ?";
                    }
                }

                sql1 = sql1 + " )";
                PreparedStatement pstm1 = verbindung.prepareStatement(sql1, ResultSet.TYPE_SCROLL_INSENSITIVE,
                        ResultSet.CONCUR_READ_ONLY);

                fuellePreparedStatementKomplettSplitRaw3(pstm1, 1, lWeisungMeldungSplitRaw);

                erg = 0; /*Falls Duplicate Key, dann wird exception geschmissen und erg wohl nicht gefüllt!*/
                //				System.out.println("vor Update");
                erg = pstm1.executeUpdate();
                //				System.out.println("nach Update");
                pstm1.close();
            } catch (Exception e2) {
                CaBug.drucke("DbWeisungMeldung.insert 011");
                System.err.println(" " + e2.getMessage());
            }

            if (erg == 0) {/*Fehler beim Einfügen - d.h. bereits vorhanden*/
                dbBasis.rollbackTransaction();
                dbBasis.endTransaction();
                return (-1);
            }

        }

        /* Ende Transaktion */
        dbBasis.endTransaction();

        return (1);
    }

    public int leseZuWeisungIdent(int lWeisungIdent, boolean mitRaw) {
        return leseIdent(lWeisungIdent, mitRaw, 1, false);
    }

    public int leseZuMeldungsIdent(int lMeldungsIdent, boolean mitRaw) {
        return leseIdent(lMeldungsIdent, mitRaw, 2, false);
    }

    public int leseZuMeldungsIdentNurAktive(int lMeldungsIdent, boolean mitRaw) {
        return leseIdent(lMeldungsIdent, mitRaw, 2, true);
    }

    public int leseZuSammelIdent(int lSammelIdent, boolean mitRaw) {
        return leseIdent(lSammelIdent, mitRaw, 3, false);
    }

    public int leseZuWillenserklaerungIdent(int lWillenserklaerungIdent, boolean mitRaw) {
        return leseIdent(lWillenserklaerungIdent, mitRaw, 4, false);
    }

    private int leseIdent(int lIdent, boolean mitRaw, int lIdentArt, boolean nurAktive) {
        int anzInArray = 0, anzInArrayRaw = 0, anzInArraySplit = 0;
        int splitVorhanden = 0;
        try {

            String sql = "";
            switch (lIdentArt) {
            case 1:
                sql = "SELECT wm.* from " + dbBundle.getSchemaMandant() + "tbl_weisungMeldung wm where "
                        + "wm.mandant=? AND ";
                if (nurAktive) {
                    sql = sql + "wm.aktiv=1 AND ";
                }
                sql = sql + "wm.weisungIdent=? ORDER BY wm.weisungIdent;";
                break;
            case 2:
                sql = "SELECT wm.* from " + dbBundle.getSchemaMandant() + "tbl_weisungMeldung wm where "
                        + "wm.mandant=? AND ";
                if (nurAktive) {
                    sql = sql + "wm.aktiv=1 AND ";
                }
                sql = sql + "wm.meldungsIdent=? ORDER BY wm.weisungIdent;";
                break;
            case 3:
                sql = "SELECT * from " + dbBundle.getSchemaMandant() + "tbl_weisungMeldung wm where "
                        + "wm.mandant=? AND ";
                if (nurAktive) {
                    sql = sql + "wm.aktiv=1 AND ";
                }
                sql = sql + "wm.sammelIdent=? ORDER BY wm.weisungIdent;";
                break;
            case 4:
                sql = "SELECT * from " + dbBundle.getSchemaMandant() + "tbl_weisungMeldung wm where "
                        + "wm.mandant=? AND ";
                if (nurAktive) {
                    sql = sql + "wm.aktiv=1 AND ";
                }
                sql = sql + "wm.willenserklaerungIdent=? ORDER BY wm.weisungIdent;";
                break;
            }

            PreparedStatement pstm1 = verbindung.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
            pstm1.setInt(1, dbBundle.clGlobalVar.mandant);
            pstm1.setInt(2, lIdent);

            ResultSet ergebnis = pstm1.executeQuery();
            ergebnis.last();
            anzInArray = ergebnis.getRow();
            ergebnis.beforeFirst();

            weisungMeldungArray = new EclWeisungMeldung[anzInArray];

            int i = 0;
            while (ergebnis.next() == true) {
                weisungMeldungArray[i] = this.decodeErgebnis(ergebnis);
                if (weisungMeldungArray[i].weisungSplit == 1) {
                    splitVorhanden = 1;
                }
                i++;
            }
            ergebnis.close();
            pstm1.close();

        } catch (Exception e) {
            CaBug.drucke("DbWeisungMeldung.leseIdent 001");
            System.err.println(" " + e.getMessage());
        }

        if (mitRaw == true) {
            /*********Raws dazu einlesen***********************/
            try { /*Raw*/

                String sql = "";
                switch (lIdentArt) {
                case 1:
                    sql = "SELECT wmr.* from " + dbBundle.getSchemaMandant() + "tbl_weisungMeldungRaw wmr where "
                            + "wmr.mandant=? AND " + "wmr.weisungIdent=? ORDER BY wmr.weisungIdent;";
                    break;
                case 2:
                    sql = "SELECT wmr.* from " + dbBundle.getSchemaMandant() + "tbl_weisungMeldungRaw wmr where "
                            + "wmr.mandant=? AND " + "wmr.meldungsIdent=? ORDER BY wmr.weisungIdent;";
                    break;
                case 3:
                    sql = "SELECT wmr.* from " + dbBundle.getSchemaMandant() + "tbl_weisungMeldungRaw wmr where "
                            + "wmr.mandant=? AND " + "wmr.sammelIdent=? ORDER BY wmr.weisungIdent;";
                    break;
                case 4:
                    sql = "SELECT wmr.* from " + dbBundle.getSchemaMandant() + "tbl_weisungMeldungRaw wmr where "
                            + "wmr.mandant=? AND " + "wmr.willenserklaerungIdent=? ORDER BY wmr.weisungIdent;";
                    break;
                }

                PreparedStatement pstm1 = verbindung.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE,
                        ResultSet.CONCUR_READ_ONLY);
                pstm1.setInt(1, dbBundle.clGlobalVar.mandant);
                pstm1.setInt(2, lIdent);

                ResultSet ergebnis = pstm1.executeQuery();
                ergebnis.last();
                anzInArrayRaw = ergebnis.getRow();
                ergebnis.beforeFirst();

                weisungMeldungRawArray = new EclWeisungMeldungRaw[anzInArrayRaw];

                int i = 0;
                while (ergebnis.next() == true) {
                    weisungMeldungRawArray[i] = this.decodeErgebnisRaw(ergebnis);
                    i++;
                }
                ergebnis.close();
                pstm1.close();

            } catch (Exception e) {
                CaBug.drucke("DbWeisungMeldung.leseIdent 002");
                System.err.println(" " + e.getMessage());
            }

            try { /*Raw1*/

                String sql = "";
                switch (lIdentArt) {
                case 1:
                    sql = "SELECT wmr1.* from " + dbBundle.getSchemaMandant() + "tbl_weisungMeldungRaw1 wmr1 where "
                            + "wmr1.mandant=? AND " + "wmr1.weisungIdent=? ORDER BY wmr1.weisungIdent;";
                    break;
                case 2:
                    sql = "SELECT wmr1.* from " + dbBundle.getSchemaMandant() + "tbl_weisungMeldungRaw1 wmr1 where "
                            + "wmr1.mandant=? AND " + "wmr1.meldungsIdent=? ORDER BY wmr1.weisungIdent;";
                    break;
                case 3:
                    sql = "SELECT wmr1.* from " + dbBundle.getSchemaMandant() + "tbl_weisungMeldungRaw1 wmr1 where "
                            + "wmr1.mandant=? AND " + "wmr1.sammelIdent=? ORDER BY wmr1.weisungIdent;";
                    break;
                case 4:
                    sql = "SELECT wmr1.* from " + dbBundle.getSchemaMandant() + "tbl_weisungMeldungRaw1 wmr1 where "
                            + "wmr1.mandant=? AND " + "wmr1.willenserklaerungIdent=? ORDER BY wmr1.weisungIdent;";
                    break;
                }

                PreparedStatement pstm1 = verbindung.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE,
                        ResultSet.CONCUR_READ_ONLY);
                pstm1.setInt(1, dbBundle.clGlobalVar.mandant);
                pstm1.setInt(2, lIdent);

                ResultSet ergebnis = pstm1.executeQuery();
                ergebnis.last();
                ergebnis.beforeFirst();

                int i = 0;
                while (ergebnis.next() == true) {
                    this.decodeErgebnisRaw1(ergebnis, weisungMeldungRawArray[i]);
                    i++;
                }
                ergebnis.close();
                pstm1.close();

            } catch (Exception e) {
                CaBug.drucke("DbWeisungMeldung.leseIdent 003");
                System.err.println(" " + e.getMessage());
            }

        }

        if (splitVorhanden == 1) { /*Dann Split einlesen*/
            /*Split*/
            try {

                String sql = "";
                switch (lIdentArt) {
                case 1:
                    sql = "SELECT wms.* from " + dbBundle.getSchemaMandant() + "tbl_weisungMeldungSplit wms where "
                            + "wms.mandant=? AND " + "wms.weisungIdent=? ORDER BY wms.weisungIdent;";
                    break;
                case 2:
                    sql = "SELECT wms.* from " + dbBundle.getSchemaMandant() + "tbl_weisungMeldungSplit wms where "
                            + "wms.mandant=? AND " + "wms.meldungsIdent=? ORDER BY wms.weisungIdent;";
                    break;
                case 3:
                    sql = "SELECT wms.* from " + dbBundle.getSchemaMandant() + "tbl_weisungMeldungSplit wms where "
                            + "wms.mandant=? AND " + "wms.sammelIdent=? ORDER BY wms.weisungIdent;";
                    break;
                case 4:
                    sql = "SELECT wms.* from " + dbBundle.getSchemaMandant() + "tbl_weisungMeldungSplit wms where "
                            + "wms.mandant=? AND " + "wms.willenserklaerungIdent=? ORDER BY wms.weisungIdent;";
                    break;
                }

                PreparedStatement pstm1 = verbindung.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE,
                        ResultSet.CONCUR_READ_ONLY);
                pstm1.setInt(1, dbBundle.clGlobalVar.mandant);
                pstm1.setInt(2, lIdent);

                ResultSet ergebnis = pstm1.executeQuery();
                ergebnis.last();
                anzInArraySplit = ergebnis.getRow();
                ergebnis.beforeFirst();

                weisungMeldungSplitArray = new EclWeisungMeldungSplit[anzInArraySplit];

                int i = 0;
                while (ergebnis.next() == true) {
                    weisungMeldungSplitArray[i] = this.decodeErgebnisSplit(ergebnis);
                    i++;
                }
                ergebnis.close();
                pstm1.close();

            } catch (Exception e) {
                CaBug.drucke("DbWeisungMeldung.leseIdent 004");
                System.err.println(" " + e.getMessage());
            }

            try {
                /*Split1 */
                String sql = "";
                switch (lIdentArt) {
                case 1:
                    sql = "SELECT wms1.* from " + dbBundle.getSchemaMandant() + "tbl_weisungMeldungSplit1 wms1 where "
                            + "wms1.mandant=? AND " + "wms1.weisungIdent=? ORDER BY wms1.weisungIdent;";
                    break;
                case 2:
                    sql = "SELECT wms1.* from " + dbBundle.getSchemaMandant() + "tbl_weisungMeldungSplit1 wms1 where "
                            + "wms1.mandant=? AND " + "wms1.meldungsIdent=? ORDER BY wms1.weisungIdent;";
                    break;
                case 3:
                    sql = "SELECT wms1.* from " + dbBundle.getSchemaMandant() + "tbl_weisungMeldungSplit1 wms1 where "
                            + "wms1.mandant=? AND " + "wms1.sammelIdent=? ORDER BY wms1.weisungIdent;";
                    break;
                case 4:
                    sql = "SELECT wms1.* from " + dbBundle.getSchemaMandant() + "tbl_weisungMeldungSplit1 wms1 where "
                            + "wms1.mandant=? AND " + "wms1.willenserklaerungIdent=? ORDER BY wms1.weisungIdent;";
                    break;
                }

                PreparedStatement pstm1 = verbindung.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE,
                        ResultSet.CONCUR_READ_ONLY);
                pstm1.setInt(1, dbBundle.clGlobalVar.mandant);
                pstm1.setInt(2, lIdent);

                ResultSet ergebnis = pstm1.executeQuery();
                ergebnis.last();
                anzInArraySplit = ergebnis.getRow();
                ergebnis.beforeFirst();

                int i = 0;
                while (ergebnis.next() == true) {
                    this.decodeErgebnisSplit1(ergebnis, weisungMeldungSplitArray[i]);
                    i++;
                }
                ergebnis.close();
                pstm1.close();

            } catch (Exception e) {
                CaBug.drucke("DbWeisungMeldung.leseIdent 012");
                System.err.println(" " + e.getMessage());
            }

            try {
                /*Split2 */
                String sql = "";
                switch (lIdentArt) {
                case 1:
                    sql = "SELECT wms2.* from " + dbBundle.getSchemaMandant() + "tbl_weisungMeldungSplit2 wms2 where "
                            + "wms2.mandant=? AND " + "wms2.weisungIdent=? ORDER BY wms2.weisungIdent;";
                    break;
                case 2:
                    sql = "SELECT wms2.* from " + dbBundle.getSchemaMandant() + "tbl_weisungMeldungSplit2 wms2 where "
                            + "wms2.mandant=? AND " + "wms2.meldungsIdent=? ORDER BY wms2.weisungIdent;";
                    break;
                case 3:
                    sql = "SELECT wms2.* from " + dbBundle.getSchemaMandant() + "tbl_weisungMeldungSplit2 wms2 where "
                            + "wms2.mandant=? AND " + "wms2.sammelIdent=? ORDER BY wms2.weisungIdent;";
                    break;
                case 4:
                    sql = "SELECT wms2.* from " + dbBundle.getSchemaMandant() + "tbl_weisungMeldungSplit2 wms2 where "
                            + "wms2.mandant=? AND " + "wms2.willenserklaerungIdent=? ORDER BY wms2.weisungIdent;";
                    break;
                }

                PreparedStatement pstm1 = verbindung.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE,
                        ResultSet.CONCUR_READ_ONLY);
                pstm1.setInt(1, dbBundle.clGlobalVar.mandant);
                pstm1.setInt(2, lIdent);

                ResultSet ergebnis = pstm1.executeQuery();
                ergebnis.last();
                anzInArraySplit = ergebnis.getRow();
                ergebnis.beforeFirst();

                int i = 0;
                while (ergebnis.next() == true) {
                    this.decodeErgebnisSplit2(ergebnis, weisungMeldungSplitArray[i]);
                    i++;
                }
                ergebnis.close();
                pstm1.close();

            } catch (Exception e) {
                CaBug.drucke("DbWeisungMeldung.leseIdent 013");
                System.err.println(" " + e.getMessage());
            }

            try {
                /*Split3 */
                String sql = "";
                switch (lIdentArt) {
                case 1:
                    sql = "SELECT wms3.* from " + dbBundle.getSchemaMandant() + "tbl_weisungMeldungSplit3 wms3 where "
                            + "wms3.mandant=? AND " + "wms3.weisungIdent=? ORDER BY wms3.weisungIdent;";
                    break;
                case 2:
                    sql = "SELECT wms3.* from " + dbBundle.getSchemaMandant() + "tbl_weisungMeldungSplit3 wms3 where "
                            + "wms3.mandant=? AND " + "wms3.meldungsIdent=? ORDER BY wms3.weisungIdent;";
                    break;
                case 3:
                    sql = "SELECT wms3.* from " + dbBundle.getSchemaMandant() + "tbl_weisungMeldungSplit3 wms3 where "
                            + "wms3.mandant=? AND " + "wms3.sammelIdent=? ORDER BY wms3.weisungIdent;";
                    break;
                case 4:
                    sql = "SELECT wms3.* from " + dbBundle.getSchemaMandant() + "tbl_weisungMeldungSplit3 wms3 where "
                            + "wms3.mandant=? AND " + "wms3.willenserklaerungIdent=? ORDER BY wms3.weisungIdent;";
                    break;
                }

                PreparedStatement pstm1 = verbindung.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE,
                        ResultSet.CONCUR_READ_ONLY);
                pstm1.setInt(1, dbBundle.clGlobalVar.mandant);
                pstm1.setInt(2, lIdent);

                ResultSet ergebnis = pstm1.executeQuery();
                ergebnis.last();
                anzInArraySplit = ergebnis.getRow();
                ergebnis.beforeFirst();

                int i = 0;
                while (ergebnis.next() == true) {
                    this.decodeErgebnisSplit3(ergebnis, weisungMeldungSplitArray[i]);
                    i++;
                }
                ergebnis.close();
                pstm1.close();

            } catch (Exception e) {
                CaBug.drucke("DbWeisungMeldung.leseIdent 014");
                System.err.println(" " + e.getMessage());
            }

            if (mitRaw == true) {
                /*********SplitRaws dazu einlesen***********************/
                try { /*Raw*/
                    String sql = "";
                    switch (lIdentArt) {

                    case 1:
                        sql = "SELECT wmsr.* from " + dbBundle.getSchemaMandant()
                                + "tbl_weisungMeldungSplitRaw wmsr where " + "wmsr.mandant=? AND "
                                + "wmsr.weisungIdent=? ORDER BY wmsr.weisungIdent;";
                        break;
                    case 2:
                        sql = "SELECT wmsr.* from " + dbBundle.getSchemaMandant()
                                + "tbl_weisungMeldungSplitRaw wmsr where " + "wmsr.mandant=? AND "
                                + "wmsr.meldungsIdent=? ORDER BY wmsr.weisungIdent;";
                        break;
                    case 3:
                        sql = "SELECT wmsr.* from " + dbBundle.getSchemaMandant()
                                + "tbl_weisungMeldungSplitRaw wmsr where " + "wmsr.mandant=? AND "
                                + "wmsr.sammelIdent=? ORDER BY wmsr.weisungIdent;";
                        break;
                    case 4:
                        sql = "SELECT wmsr.* from " + dbBundle.getSchemaMandant()
                                + "tbl_weisungMeldungSplitRaw wmsr where " + "wmsr.mandant=? AND "
                                + "wmsr.willenserklaerungIdent=? ORDER BY wmsr.weisungIdent;";
                        break;
                    }

                    PreparedStatement pstm1 = verbindung.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE,
                            ResultSet.CONCUR_READ_ONLY);
                    pstm1.setInt(1, dbBundle.clGlobalVar.mandant);
                    pstm1.setInt(2, lIdent);

                    ResultSet ergebnis = pstm1.executeQuery();
                    ergebnis.last();
                    anzInArraySplit = ergebnis.getRow();
                    ergebnis.beforeFirst();

                    weisungMeldungSplitRawArray = new EclWeisungMeldungSplitRaw[anzInArraySplit];

                    int i = 0;
                    while (ergebnis.next() == true) {
                        weisungMeldungSplitRawArray[i] = this.decodeErgebnisSplitRaw(ergebnis);
                        i++;
                    }
                    ergebnis.close();
                    pstm1.close();

                } catch (Exception e) {
                    CaBug.drucke("DbWeisungMeldung.leseIdent 005");
                    System.err.println(" " + e.getMessage());
                }

                try { /*Raw1*/
                    String sql = "";
                    switch (lIdentArt) {

                    case 1:
                        sql = "SELECT wmsr1.* from " + dbBundle.getSchemaMandant()
                                + "tbl_weisungMeldungSplitRaw1 wmsr1 where " + "wmsr1.mandant=? AND "
                                + "wmsr1.weisungIdent=? ORDER BY wmsr1.weisungIdent;";
                        break;
                    case 2:
                        sql = "SELECT wmsr1.* from " + dbBundle.getSchemaMandant()
                                + "tbl_weisungMeldungSplitRaw1 wmsr1 where " + "wmsr1.mandant=? AND "
                                + "wmsr1.meldungsIdent=? ORDER BY wmsr1.weisungIdent;";
                        break;
                    case 3:
                        sql = "SELECT wmsr1.* from " + dbBundle.getSchemaMandant()
                                + "tbl_weisungMeldungSplitRaw1 wmsr1 where " + "wmsr1.mandant=? AND "
                                + "wmsr1.sammelIdent=? ORDER BY wmsr1.weisungIdent;";
                        break;
                    case 4:
                        sql = "SELECT wmsr1.* from " + dbBundle.getSchemaMandant()
                                + "tbl_weisungMeldungSplitRaw1 wmsr1 where " + "wmsr1.mandant=? AND "
                                + "wmsr1.willenserklaerungIdent=? ORDER BY wmsr1.weisungIdent;";
                        break;
                    }

                    PreparedStatement pstm1 = verbindung.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE,
                            ResultSet.CONCUR_READ_ONLY);
                    pstm1.setInt(1, dbBundle.clGlobalVar.mandant);
                    pstm1.setInt(2, lIdent);

                    ResultSet ergebnis = pstm1.executeQuery();
                    ergebnis.last();
                    ergebnis.beforeFirst();

                    int i = 0;
                    while (ergebnis.next() == true) {
                        this.decodeErgebnisSplitRaw1(ergebnis, weisungMeldungSplitRawArray[i]);
                        i++;
                    }
                    ergebnis.close();
                    pstm1.close();

                } catch (Exception e) {
                    CaBug.drucke("DbWeisungMeldung.leseIdent 006");
                    System.err.println(" " + e.getMessage());
                }

                try { /*Raw2*/
                    String sql = "";
                    switch (lIdentArt) {

                    case 1:
                        sql = "SELECT wmsr2.* from " + dbBundle.getSchemaMandant()
                                + "tbl_weisungMeldungSplitRaw2 wmsr2 where " + "wmsr2.mandant=? AND "
                                + "wmsr2.weisungIdent=? ORDER BY wmsr2.weisungIdent;";
                        break;
                    case 2:
                        sql = "SELECT wmsr2.* from " + dbBundle.getSchemaMandant()
                                + "tbl_weisungMeldungSplitRaw2 wmsr2 where " + "wmsr2.mandant=? AND "
                                + "wmsr2.meldungsIdent=? ORDER BY wmsr2.weisungIdent;";
                        break;
                    case 3:
                        sql = "SELECT wmsr2.* from " + dbBundle.getSchemaMandant()
                                + "tbl_weisungMeldungSplitRaw2 wmsr2 where " + "wmsr2.mandant=? AND "
                                + "wmsr2.sammelIdent=? ORDER BY wmsr2.weisungIdent;";
                        break;
                    case 4:
                        sql = "SELECT wmsr2.* from " + dbBundle.getSchemaMandant()
                                + "tbl_weisungMeldungSplitRaw2 wmsr2 where " + "wmsr2.mandant=? AND "
                                + "wmsr2.willenserklaerungIdent=? ORDER BY wmsr2.weisungIdent;";
                        break;
                    }

                    PreparedStatement pstm1 = verbindung.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE,
                            ResultSet.CONCUR_READ_ONLY);
                    pstm1.setInt(1, dbBundle.clGlobalVar.mandant);
                    pstm1.setInt(2, lIdent);

                    ResultSet ergebnis = pstm1.executeQuery();
                    ergebnis.last();
                    ergebnis.beforeFirst();

                    int i = 0;
                    while (ergebnis.next() == true) {
                        this.decodeErgebnisSplitRaw2(ergebnis, weisungMeldungSplitRawArray[i]);
                        i++;
                    }
                    ergebnis.close();
                    pstm1.close();

                } catch (Exception e) {
                    CaBug.drucke("DbWeisungMeldung.leseIdent 007");
                    System.err.println(" " + e.getMessage());
                }

                try { /*Raw3*/
                    String sql = "";
                    switch (lIdentArt) {

                    case 1:
                        sql = "SELECT wmsr3.* from " + dbBundle.getSchemaMandant()
                                + "tbl_weisungMeldungSplitRaw3 wmsr3 where " + "wmsr3.mandant=? AND "
                                + "wmsr3.weisungIdent=? ORDER BY wmsr3.weisungIdent;";
                        break;
                    case 2:
                        sql = "SELECT wmsr3.* from " + dbBundle.getSchemaMandant()
                                + "tbl_weisungMeldungSplitRaw3 wmsr3 where " + "wmsr3.mandant=? AND "
                                + "wmsr3.meldungsIdent=? ORDER BY wmsr3.weisungIdent;";
                        break;
                    case 3:
                        sql = "SELECT wmsr3.* from " + dbBundle.getSchemaMandant()
                                + "tbl_weisungMeldungSplitRaw3 wmsr3 where " + "wmsr3.mandant=? AND "
                                + "wmsr3.sammelIdent=? ORDER BY wmsr3.weisungIdent;";
                        break;
                    case 4:
                        sql = "SELECT wmsr3.* from " + dbBundle.getSchemaMandant()
                                + "tbl_weisungMeldungSplitRaw3 wmsr3 where " + "wmsr3.mandant=? AND "
                                + "wmsr3.willenserklaerungIdent=? ORDER BY wmsr3.weisungIdent;";
                        break;
                    }

                    PreparedStatement pstm1 = verbindung.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE,
                            ResultSet.CONCUR_READ_ONLY);
                    pstm1.setInt(1, dbBundle.clGlobalVar.mandant);
                    pstm1.setInt(2, lIdent);

                    ResultSet ergebnis = pstm1.executeQuery();
                    ergebnis.last();
                    ergebnis.beforeFirst();

                    int i = 0;
                    while (ergebnis.next() == true) {
                        this.decodeErgebnisSplitRaw3(ergebnis, weisungMeldungSplitRawArray[i]);
                        i++;
                    }
                    ergebnis.close();
                    pstm1.close();

                } catch (Exception e) {
                    CaBug.drucke("DbWeisungMeldung.leseIdent 008");
                    System.err.println(" " + e.getMessage());
                }

            }

        }

        return (anzInArray);

    }

    /**Füllt nur weisungMeldung*/
    public int leseInaktive() {
        int anzInArray = 0;
        try {

            String sql = "";
            sql = "SELECT wm.* from " + dbBundle.getSchemaMandant() + "tbl_weisungMeldung wm where "
                    + "wm.mandant=? AND wm.aktiv=0";

            PreparedStatement pstm1 = verbindung.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
            pstm1.setInt(1, dbBundle.clGlobalVar.mandant);

            ResultSet ergebnis = pstm1.executeQuery();
            ergebnis.last();
            anzInArray = ergebnis.getRow();
            ergebnis.beforeFirst();

            weisungMeldungArray = new EclWeisungMeldung[anzInArray];

            int i = 0;
            while (ergebnis.next() == true) {
                weisungMeldungArray[i] = this.decodeErgebnis(ergebnis);
                i++;
            }
            ergebnis.close();
            pstm1.close();

        } catch (Exception e) {
            CaBug.drucke("DbWeisungMeldung.leseInaktive 001");
            System.err.println(" " + e.getMessage());
        }

        return (anzInArray);
    }


    
    /**Füllt nur weisungMeldung*/
    public int leseAktiveZuMeldung(int pMeldung) {
        int anzInArray = 0;
        try {

            String sql = "";
            sql = "SELECT wm.* from " + dbBundle.getSchemaMandant() + "tbl_weisungMeldung wm where "
                    + "wm.mandant=? AND wm.aktiv=1 AND wm.meldungsIdent=?";

            PreparedStatement pstm1 = verbindung.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
            pstm1.setInt(1, dbBundle.clGlobalVar.mandant);
            pstm1.setInt(2, pMeldung);

            ResultSet ergebnis = pstm1.executeQuery();
            ergebnis.last();
            anzInArray = ergebnis.getRow();
            ergebnis.beforeFirst();

            weisungMeldungArray = new EclWeisungMeldung[anzInArray];

            int i = 0;
            while (ergebnis.next() == true) {
                weisungMeldungArray[i] = this.decodeErgebnis(ergebnis);
                i++;
            }
            ergebnis.close();
            pstm1.close();

        } catch (Exception e) {
            CaBug.drucke("DbWeisungMeldung.leseInaktive 001");
            System.err.println(" " + e.getMessage());
        }

        return (anzInArray);
    }

    /**Liefere "aktiv"-Kennzeichen von weisung lWeisungIdent (z.B. zur Überprüfung ob
     * Weisung aktiv ist, oder bereits geändert/storniert wurde**/
    public int leseAktivKZ(int lWeisungIdent) {

        try {
            String sql = "SELECT weisungIdent, aktiv from " + dbBundle.getSchemaMandant() + "tbl_weisungMeldung where "
                    + "mandant=? AND weisungIdent=? ORDER BY weisungIdent;";
            PreparedStatement pstm1;
            pstm1 = verbindung.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            pstm1.setInt(1, dbBundle.clGlobalVar.mandant);
            pstm1.setInt(2, lWeisungIdent);
            ResultSet ergebnis = pstm1.executeQuery();
            while (ergebnis.next() == true) {
                int lAktiv = ergebnis.getInt("aktiv");
                return lAktiv;

            }
            ergebnis.close();
            pstm1.close();

        } catch (SQLException e) {
            CaBug.drucke("DbWeisungMeldung.leseAktivKZ 001");
            System.err.println(" " + e.getMessage());
        }

        return -255;
    }

    /**Update eines Abstimmvorschlags. Achtung:  Update muß innerhalb einer Transaktion erfolgen.
     */
    public int update(EclWeisungMeldung lWeisungMeldung, EclWeisungMeldungRaw lWeisungMeldungRaw, boolean mitRaw) {

        EclWeisungMeldungSplit lWeisungMeldungSplit = null;
        EclWeisungMeldungSplitRaw lWeisungMeldungSplitRaw = null;
        ;

        lWeisungMeldungSplit = lWeisungMeldung.weisungMeldungSplit;

        if (mitRaw == true) {
            lWeisungMeldungSplitRaw = lWeisungMeldungRaw.weisungMeldungSplitRaw;
        }

        try {

            String sql = "UPDATE " + dbBundle.getSchemaMandant() + "tbl_weisungMeldung SET "
                    + "mandant=?, weisungIdent=?, "
                    + "meldungsIdent=?, sammelIdent=?, skIst=?, willenserklaerungIdent=?, " + "weisungSplit=?, "
                    + "aktiv=?, istWeisungBriefwahl=?, "
                    + "gemaessFremdenAbstimmungsVorschlagIdent=?, gemaessEigenemAbstimmungsVorschlagIdent=?, stimmartGesamt=?, aktualisieren=?";
            int i;
            for (i = 0; i < 200; i++) {
                sql = sql + ", abgabe" + Integer.toString(i) + "=?";
            }
            for (i = 0; i < 200; i++) {
                sql = sql + ", abgabeLautGesamt" + Integer.toString(i) + "=?";
            }
            sql = sql + " WHERE " + "weisungIdent=? AND " + "mandant=?";

            PreparedStatement pstm1 = verbindung.prepareStatement(sql);
            fuellePreparedStatementKomplett(pstm1, 1, lWeisungMeldung);
            pstm1.setInt(anzfelder + 1, lWeisungMeldung.weisungIdent);
            pstm1.setLong(anzfelder + 2, dbBundle.clGlobalVar.mandant);

            int ergebnis1 = pstm1.executeUpdate();
            pstm1.close();
            if (ergebnis1 == 0) {
                return (CaFehler.pfdUnbekannterFehler);
            }
        } catch (Exception e1) {
            CaBug.drucke("DbWeisungMeldung.update 001");
            System.err.println(" " + e1.getMessage());
            return (CaFehler.pfdUnbekannterFehler);

        }

        if (mitRaw == true) {
            try {

                /*Raw*/
                String sql = "UPDATE " + dbBundle.getSchemaMandant() + "tbl_weisungMeldungRaw SET "
                        + "mandant=?, weisungIdent=?, " + "meldungsIdent=?, sammelIdent=?, willenserklaerungIdent=?, "
                        + "bereitsInterpretiert=?, "
                        + "gemaessFremdenAbstimmungsVorschlagIdent=?, gegenFremdenAbstimmungsVorschlagIdent=?, "
                        + "gemaessEigenemAbstimmungsVorschlagIdent=?, gegenEigenemAbstimmungsVorschlagIdent=?, "
                        + "stimmartGesamt=?";
                int i;
                for (i = 0; i < 100; i++) {
                    sql = sql + ", abgabe" + Integer.toString(i) + "=?";
                }
                sql = sql + " WHERE " + "weisungIdent=? AND " + "mandant=?";

                PreparedStatement pstm1 = verbindung.prepareStatement(sql);
                fuellePreparedStatementKomplettRaw(pstm1, 1, lWeisungMeldungRaw);
                pstm1.setInt(anzfelderRaw + 1, lWeisungMeldungRaw.weisungIdent);
                pstm1.setLong(anzfelderRaw + 2, dbBundle.clGlobalVar.mandant);

                int ergebnis1 = pstm1.executeUpdate();
                pstm1.close();
                if (ergebnis1 == 0) {
                    return (CaFehler.pfdUnbekannterFehler);
                }
            } catch (Exception e1) {
                CaBug.drucke("DbWeisungMeldung.update 002");
                System.err.println(" " + e1.getMessage());
                return (CaFehler.pfdUnbekannterFehler);

            }

            try {

                /*Raw1*/
                String sql = "UPDATE " + dbBundle.getSchemaMandant() + "tbl_weisungMeldungRaw1 SET "
                        + "mandant=?, weisungIdent=?, " + "meldungsIdent=?, sammelIdent=?, willenserklaerungIdent=?";
                int i;
                for (i = 100; i < 200; i++) {
                    sql = sql + ", abgabe" + Integer.toString(i) + "=?";
                }
                sql = sql + " WHERE " + "weisungIdent=? AND " + "mandant=?";

                PreparedStatement pstm1 = verbindung.prepareStatement(sql);
                fuellePreparedStatementKomplettRaw1(pstm1, 1, lWeisungMeldungRaw);
                pstm1.setInt(anzfelderRaw1 + 1, lWeisungMeldungRaw.weisungIdent);
                pstm1.setLong(anzfelderRaw1 + 2, dbBundle.clGlobalVar.mandant);

                int ergebnis1 = pstm1.executeUpdate();
                pstm1.close();
                if (ergebnis1 == 0) {
                    return (CaFehler.pfdUnbekannterFehler);
                }
            } catch (Exception e1) {
                CaBug.drucke("DbWeisungMeldung.update 003");
                System.err.println(" " + e1.getMessage());
                return (CaFehler.pfdUnbekannterFehler);

            }

        }

        if (lWeisungMeldung.weisungSplit == 1) { /*Split vorhanden - ebenfalls Updaten*/
            try {

                /*Split*/
                String sql = "UPDATE " + dbBundle.getSchemaMandant() + "tbl_weisungMeldungSplit SET "
                        + "mandant=?, weisungIdent=?, " + "meldungsIdent=?, sammelIdent=?, willenserklaerungIdent=?";

                int i, i1;
                for (i = 0; i < 50; i++) {
                    for (i1 = 0; i1 < 10; i1++) {
                        sql = sql + ", abgabe" + Integer.toString(i) + "_" + Integer.toString(i1) + "=?";
                    }
                }
                for (i = 0; i < 50; i++) {
                    sql = sql + ", nichtBerechnen" + Integer.toString(i) + "=?";
                }
                for (i = 0; i < 50; i++) {
                    sql = sql + ", weisungssummeFalsch" + Integer.toString(i) + "=?";
                }

                sql = sql + " WHERE " + "weisungIdent=? AND " + "mandant=?";

                PreparedStatement pstm1 = verbindung.prepareStatement(sql);
                fuellePreparedStatementKomplettSplit(pstm1, 1, lWeisungMeldungSplit);
                pstm1.setInt(anzfelderSplit + 1, lWeisungMeldungSplit.weisungIdent);
                pstm1.setLong(anzfelderSplit + 2, dbBundle.clGlobalVar.mandant);

                int ergebnis1 = pstm1.executeUpdate();
                pstm1.close();
                if (ergebnis1 == 0) {
                    return (CaFehler.pfdUnbekannterFehler);
                }
            } catch (Exception e1) {
                CaBug.drucke("DbWeisungMeldung.update 004");
                System.err.println(" " + e1.getMessage());
                return (CaFehler.pfdUnbekannterFehler);

            }

            /*Split1*/
            try {
                String sql = "UPDATE " + dbBundle.getSchemaMandant() + "tbl_weisungMeldungSplit1 SET "
                        + "mandant=?, weisungIdent=?, " + "meldungsIdent=?, sammelIdent=?, willenserklaerungIdent=?";

                int i, i1;
                for (i = 50; i < 100; i++) {
                    for (i1 = 0; i1 < 10; i1++) {
                        sql = sql + ", abgabe" + Integer.toString(i) + "_" + Integer.toString(i1) + "=?";
                    }
                }
                for (i = 50; i < 100; i++) {
                    sql = sql + ", nichtBerechnen" + Integer.toString(i) + "=?";
                }
                for (i = 50; i < 100; i++) {
                    sql = sql + ", weisungssummeFalsch" + Integer.toString(i) + "=?";
                }

                sql = sql + " WHERE " + "weisungIdent=? AND " + "mandant=?";

                PreparedStatement pstm1 = verbindung.prepareStatement(sql);
                fuellePreparedStatementKomplettSplit1(pstm1, 1, lWeisungMeldungSplit);
                pstm1.setInt(anzfelderSplit1 + 1, lWeisungMeldungSplit.weisungIdent);
                pstm1.setLong(anzfelderSplit1 + 2, dbBundle.clGlobalVar.mandant);

                int ergebnis1 = pstm1.executeUpdate();
                pstm1.close();
                if (ergebnis1 == 0) {
                    return (CaFehler.pfdUnbekannterFehler);
                }
            } catch (Exception e1) {
                CaBug.drucke("DbWeisungMeldung.update 005");
                System.err.println(" " + e1.getMessage());
                return (CaFehler.pfdUnbekannterFehler);

            }

            /*Split2*/
            try {
                String sql = "UPDATE " + dbBundle.getSchemaMandant() + "tbl_weisungMeldungSplit2 SET "
                        + "mandant=?, weisungIdent=?, " + "meldungsIdent=?, sammelIdent=?, willenserklaerungIdent=?";

                int i, i1;
                for (i = 100; i < 150; i++) {
                    for (i1 = 0; i1 < 10; i1++) {
                        sql = sql + ", abgabe" + Integer.toString(i) + "_" + Integer.toString(i1) + "=?";
                    }
                }
                for (i = 100; i < 150; i++) {
                    sql = sql + ", nichtBerechnen" + Integer.toString(i) + "=?";
                }
                for (i = 100; i < 150; i++) {
                    sql = sql + ", weisungssummeFalsch" + Integer.toString(i) + "=?";
                }

                sql = sql + " WHERE " + "weisungIdent=? AND " + "mandant=?";

                PreparedStatement pstm1 = verbindung.prepareStatement(sql);
                fuellePreparedStatementKomplettSplit2(pstm1, 1, lWeisungMeldungSplit);
                pstm1.setInt(anzfelderSplit2 + 1, lWeisungMeldungSplit.weisungIdent);
                pstm1.setLong(anzfelderSplit2 + 2, dbBundle.clGlobalVar.mandant);

                int ergebnis1 = pstm1.executeUpdate();
                pstm1.close();
                if (ergebnis1 == 0) {
                    return (CaFehler.pfdUnbekannterFehler);
                }
            } catch (Exception e1) {
                CaBug.drucke("DbWeisungMeldung.update 005");
                System.err.println(" " + e1.getMessage());
                return (CaFehler.pfdUnbekannterFehler);

            }

            /*Split3*/
            try {
                String sql = "UPDATE " + dbBundle.getSchemaMandant() + "tbl_weisungMeldungSplit3 SET "
                        + "mandant=?, weisungIdent=?, " + "meldungsIdent=?, sammelIdent=?, willenserklaerungIdent=?";

                int i, i1;
                for (i = 150; i < 200; i++) {
                    for (i1 = 0; i1 < 10; i1++) {
                        sql = sql + ", abgabe" + Integer.toString(i) + "_" + Integer.toString(i1) + "=?";
                    }
                }
                for (i = 150; i < 200; i++) {
                    sql = sql + ", nichtBerechnen" + Integer.toString(i) + "=?";
                }
                for (i = 150; i < 200; i++) {
                    sql = sql + ", weisungssummeFalsch" + Integer.toString(i) + "=?";
                }

                sql = sql + " WHERE " + "weisungIdent=? AND " + "mandant=?";

                PreparedStatement pstm1 = verbindung.prepareStatement(sql);
                fuellePreparedStatementKomplettSplit3(pstm1, 1, lWeisungMeldungSplit);
                pstm1.setInt(anzfelderSplit3 + 1, lWeisungMeldungSplit.weisungIdent);
                pstm1.setLong(anzfelderSplit3 + 2, dbBundle.clGlobalVar.mandant);

                int ergebnis1 = pstm1.executeUpdate();
                pstm1.close();
                if (ergebnis1 == 0) {
                    return (CaFehler.pfdUnbekannterFehler);
                }
            } catch (Exception e1) {
                CaBug.drucke("DbWeisungMeldung.update 006");
                System.err.println(" " + e1.getMessage());
                return (CaFehler.pfdUnbekannterFehler);

            }

            if (mitRaw == true) {

                try {
                    /*Raw*/
                    String sql = "UPDATE " + dbBundle.getSchemaMandant() + "tbl_weisungMeldungSplitRaw SET "
                            + "mandant=?, weisungIdent=?, "
                            + "meldungsIdent=?, sammelIdent=?, willenserklaerungIdent=?";

                    int i, i1;
                    for (i = 0; i < 50; i++) {
                        for (i1 = 0; i1 < 10; i1++) {
                            sql = sql + ", abgabe" + Integer.toString(i) + "_" + Integer.toString(i1) + "=?";
                        }
                    }

                    sql = sql + " WHERE " + "weisungIdent=? AND " + "mandant=?";

                    PreparedStatement pstm1 = verbindung.prepareStatement(sql);
                    fuellePreparedStatementKomplettSplitRaw(pstm1, 1, lWeisungMeldungSplitRaw);
                    pstm1.setInt(anzfelderSplitRaw + 1, lWeisungMeldungSplitRaw.weisungIdent);
                    pstm1.setLong(anzfelderSplitRaw + 2, dbBundle.clGlobalVar.mandant);

                    int ergebnis1 = pstm1.executeUpdate();
                    pstm1.close();
                    if (ergebnis1 == 0) {
                        return (CaFehler.pfdUnbekannterFehler);
                    }
                } catch (Exception e1) {
                    CaBug.drucke("DbWeisungMeldung.update 007");
                    System.err.println(" " + e1.getMessage());
                    return (CaFehler.pfdUnbekannterFehler);

                }

                try {
                    /*Raw1*/
                    String sql = "UPDATE " + dbBundle.getSchemaMandant() + "tbl_weisungMeldungSplitRaw1 SET "
                            + "mandant=?, weisungIdent=?, "
                            + "meldungsIdent=?, sammelIdent=?, willenserklaerungIdent=?";

                    int i, i1;
                    for (i = 50; i < 100; i++) {
                        for (i1 = 0; i1 < 10; i1++) {
                            sql = sql + ", abgabe" + Integer.toString(i) + "_" + Integer.toString(i1) + "=?";
                        }
                    }

                    sql = sql + " WHERE " + "weisungIdent=? AND " + "mandant=?";

                    PreparedStatement pstm1 = verbindung.prepareStatement(sql);
                    fuellePreparedStatementKomplettSplitRaw1(pstm1, 1, lWeisungMeldungSplitRaw);
                    pstm1.setInt(anzfelderSplitRaw1 + 1, lWeisungMeldungSplitRaw.weisungIdent);
                    pstm1.setLong(anzfelderSplitRaw1 + 2, dbBundle.clGlobalVar.mandant);

                    int ergebnis1 = pstm1.executeUpdate();
                    pstm1.close();
                    if (ergebnis1 == 0) {
                        return (CaFehler.pfdUnbekannterFehler);
                    }
                } catch (Exception e1) {
                    CaBug.drucke("DbWeisungMeldung.update 008");
                    System.err.println(" " + e1.getMessage());
                    return (CaFehler.pfdUnbekannterFehler);

                }

                try {
                    /*Raw2*/
                    String sql = "UPDATE " + dbBundle.getSchemaMandant() + "tbl_weisungMeldungSplitRaw2 SET "
                            + "mandant=?, weisungIdent=?, "
                            + "meldungsIdent=?, sammelIdent=?, willenserklaerungIdent=?";

                    int i, i1;
                    for (i = 100; i < 150; i++) {
                        for (i1 = 0; i1 < 10; i1++) {
                            sql = sql + ", abgabe" + Integer.toString(i) + "_" + Integer.toString(i1) + "=?";
                        }
                    }

                    sql = sql + " WHERE " + "weisungIdent=? AND " + "mandant=?";

                    PreparedStatement pstm1 = verbindung.prepareStatement(sql);
                    fuellePreparedStatementKomplettSplitRaw2(pstm1, 1, lWeisungMeldungSplitRaw);
                    pstm1.setInt(anzfelderSplitRaw2 + 1, lWeisungMeldungSplitRaw.weisungIdent);
                    pstm1.setLong(anzfelderSplitRaw2 + 2, dbBundle.clGlobalVar.mandant);

                    int ergebnis1 = pstm1.executeUpdate();
                    pstm1.close();
                    if (ergebnis1 == 0) {
                        return (CaFehler.pfdUnbekannterFehler);
                    }
                } catch (Exception e1) {
                    CaBug.drucke("DbWeisungMeldung.update 009");
                    System.err.println(" " + e1.getMessage());
                    return (CaFehler.pfdUnbekannterFehler);

                }

                try {
                    /*Raw3*/
                    String sql = "UPDATE " + dbBundle.getSchemaMandant() + "tbl_weisungMeldungSplitRaw3 SET "
                            + "mandant=?, weisungIdent=?, "
                            + "meldungsIdent=?, sammelIdent=?, willenserklaerungIdent=?";

                    int i, i1;
                    for (i = 150; i < 200; i++) {
                        for (i1 = 0; i1 < 10; i1++) {
                            sql = sql + ", abgabe" + Integer.toString(i) + "_" + Integer.toString(i1) + "=?";
                        }
                    }

                    sql = sql + " WHERE " + "weisungIdent=? AND " + "mandant=?";

                    PreparedStatement pstm1 = verbindung.prepareStatement(sql);
                    fuellePreparedStatementKomplettSplitRaw3(pstm1, 1, lWeisungMeldungSplitRaw);
                    pstm1.setInt(anzfelderSplitRaw3 + 1, lWeisungMeldungSplitRaw.weisungIdent);
                    pstm1.setLong(anzfelderSplitRaw3 + 2, dbBundle.clGlobalVar.mandant);

                    int ergebnis1 = pstm1.executeUpdate();
                    pstm1.close();
                    if (ergebnis1 == 0) {
                        return (CaFehler.pfdUnbekannterFehler);
                    }
                } catch (Exception e1) {
                    CaBug.drucke("DbWeisungMeldung.update 010");
                    System.err.println(" " + e1.getMessage());
                    return (CaFehler.pfdUnbekannterFehler);

                }

            }

        }
        return (1);
    }

    /**Setze "aktiv"-Kennzeichen von weisung lWeisungIdent**/
    public int updateAktivKZ(int lWeisungIdent, int lAktiv) {

        try {

            String sql = "UPDATE " + dbBundle.getSchemaMandant() + "tbl_weisungMeldung SET aktiv=? WHERE "
                    + "weisungIdent=? AND " + "mandant=?";

            PreparedStatement pstm1 = verbindung.prepareStatement(sql);
            pstm1.setInt(1, lAktiv);
            pstm1.setInt(2, lWeisungIdent);
            pstm1.setLong(3, dbBundle.clGlobalVar.mandant);

            int ergebnis1 = pstm1.executeUpdate();
            pstm1.close();
            if (ergebnis1 == 0) {
                return (CaFehler.pfdUnbekannterFehler);
            }
        } catch (Exception e1) {
            CaBug.drucke("DbWeisungMeldung.update 001");
            System.err.println(" " + e1.getMessage());
            return (CaFehler.pfdUnbekannterFehler);

        }
        return 1;

    }

    public int updateEinzelWeisungZuSammelkartenMeldungen(int sammelIdent, int pWeisungsPos, int stimmart) {

        try {

            String sql = "UPDATE " + dbBundle.getSchemaMandant() + "tbl_weisungMeldung SET abgabe"
                    + Integer.toString(pWeisungsPos) + "=? WHERE " + "sammelIdent=? and mandant=?";

            PreparedStatement pstm1 = verbindung.prepareStatement(sql);
            pstm1.setInt(1, stimmart);
            pstm1.setInt(2, sammelIdent);
            pstm1.setInt(3, dbBundle.clGlobalVar.mandant);

            /*int ergebnis1=*/pstm1.executeUpdate();
            pstm1.close();
        } catch (Exception e1) {
            CaBug.drucke("DbWeisungMeldung.updateEinzelWeisungZuSammelkartenMeldungen 001");
            System.err.println(" " + e1.getMessage());
            return (CaFehler.pfdUnbekannterFehler);

        }

        return 1;

    }

    public int updateFreiWeisung() {

        for (int i = 60; i <= 70; i++) {
            System.out.println("i=" + i);
            try {

                String sql = "UPDATE " + dbBundle.getSchemaMandant() + "tbl_weisungMeldung SET abgabe"
                        + Integer.toString(i) + "=3 WHERE " + "meldungsIdent>17 AND abgabe" + Integer.toString(i)
                        + "=0 and mandant=?";

                PreparedStatement pstm1 = verbindung.prepareStatement(sql);
                pstm1.setInt(1, dbBundle.clGlobalVar.mandant);

                /*int ergebnis1=*/pstm1.executeUpdate();
                pstm1.close();
            } catch (Exception e1) {
                CaBug.drucke("DbWeisungMeldung.update 001");
                System.err.println(" " + e1.getMessage());
                return (CaFehler.pfdUnbekannterFehler);

            }

        }

        return 1;

    }

    /**Einlesen aller Aktionäre mit deren Weisungen, zu einer bestimmten Sammelkarte; 
     * lAktiv=1 oder =0 wird entsprechend ausgewertet	
     */
    public int leseAktionaersWeisungZuSammelkarte(int lIdentSammellkarte, int lAktiv) {
        int anzInArray = 0;
        try {

            //			String sql="SELECT wm.*, m.stimmen, m.aktionaersnummer, m.zutrittsIdent, m.stimmkarte, m.besitzart, p.kurzName, p.name, p.vorname, p.ort from "
            //			+ "("+dbBundle.getSchemaMandant()+"tbl_weisungMeldung wm INNER JOIN "+dbBundle.getSchemaMandant()+"tbl_meldungen m on wm.meldungsIdent=m.meldungsIdent) "
            //			+ "INNER JOIN "+dbBundle.getSchemaMandant()+"tbl_PersonenNatJur p ON m.personenNatJurIdent=p.ident "
            //			+ "WHERE wm.sammelIdent=? AND wm.aktiv=? AND wm.mandant=? AND m.mandant=? AND p.mandant=? ORDER BY m.zutrittsIdent;"
            //			;

            String sql = "SELECT wm.*, m.stimmen, m.stueckAktien, m.gattung, m.aktionaersnummer, m.zutrittsIdent, m.stimmkarte, m.besitzart, m.stimmausschluss, p.kurzName, p.name, p.vorname, p.ort from "
                    + "(" + dbBundle.getSchemaMandant() + "tbl_weisungMeldung wm INNER JOIN "
                    + dbBundle.getSchemaMandant() + "tbl_meldungen m on wm.meldungsIdent=m.meldungsIdent) "
                    + "INNER JOIN " + dbBundle.getSchemaMandant()
                    + "tbl_PersonenNatJur p ON m.personenNatJurIdent=p.ident "
                    + "WHERE wm.sammelIdent=? AND wm.aktiv=? AND wm.mandant=? AND m.mandant=? AND p.mandant=? ORDER BY m.zutrittsIdent;";

            PreparedStatement pstm1 = verbindung.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
            pstm1.setInt(1, lIdentSammellkarte);
            pstm1.setInt(2, lAktiv);
            pstm1.setInt(3, dbBundle.clGlobalVar.mandant);
            pstm1.setInt(4, dbBundle.clGlobalVar.mandant);
            pstm1.setInt(5, dbBundle.clGlobalVar.mandant);

            ResultSet ergebnis = pstm1.executeQuery();
            ergebnis.last();
            anzInArray = ergebnis.getRow();
            ergebnis.beforeFirst();

            aktionaersWeisungenZuSammelkarteArray = new PclAktionaersWeisungenZuSammelkarte[anzInArray];

            int i = 0;
            while (ergebnis.next() == true) {
                aktionaersWeisungenZuSammelkarteArray[i] = new PclAktionaersWeisungenZuSammelkarte();
                aktionaersWeisungenZuSammelkarteArray[i].weisungMeldung = this.decodeErgebnis(ergebnis);
                aktionaersWeisungenZuSammelkarteArray[i].stueckAktien = ergebnis.getLong("m.stueckAktien");
                aktionaersWeisungenZuSammelkarteArray[i].stimmen = ergebnis.getLong("m.stimmen");
                aktionaersWeisungenZuSammelkarteArray[i].gattung = ergebnis.getInt("m.gattung");
                aktionaersWeisungenZuSammelkarteArray[i].kurzName = ergebnis.getString("p.kurzName");

                aktionaersWeisungenZuSammelkarteArray[i].aktionaersnummer = ergebnis.getString("m.aktionaersnummer");
                aktionaersWeisungenZuSammelkarteArray[i].zutrittsIdent = ergebnis.getString("m.zutrittsIdent");
                aktionaersWeisungenZuSammelkarteArray[i].stimmkarte = ergebnis.getString("m.stimmkarte");
                aktionaersWeisungenZuSammelkarteArray[i].besitzart = ergebnis.getString("m.besitzart");
                aktionaersWeisungenZuSammelkarteArray[i].stimmausschluss = ergebnis.getString("m.stimmausschluss");
                aktionaersWeisungenZuSammelkarteArray[i].name = ergebnis.getString("p.name");
                aktionaersWeisungenZuSammelkarteArray[i].vorname = ergebnis.getString("p.vorname");
                aktionaersWeisungenZuSammelkarteArray[i].ort = ergebnis.getString("p.ort");
                i++;
            }
            ergebnis.close();
            pstm1.close();

        } catch (Exception e) {
            CaBug.drucke("DbWeisungMeldung.leseAktionaersWeisungZuSammelkarte 001");
            System.err.println(" " + e.getMessage());
        }
        return anzInArray;

    }
    
    public int leseZuSammelkarteAusArchiv(int pSammelIdent, int pArchivNr) {
        int anzInArray = 0;
        try {

            String sql = "";
            sql = "SELECT * from " + dbBundle.getSchemaMandant() + "tbl_weisungMeldung_"
                    + Integer.toString(pArchivNr)
                    +" wm where "
                    + "wm.mandant=? AND ";
            sql = sql + "wm.aktiv=1 AND ";
            sql = sql + "wm.sammelIdent=? ORDER BY wm.weisungIdent;";

            PreparedStatement pstm1 = verbindung.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
            pstm1.setInt(1, dbBundle.clGlobalVar.mandant);
            pstm1.setInt(2, pSammelIdent);

            ResultSet ergebnis = pstm1.executeQuery();
            ergebnis.last();
            anzInArray = ergebnis.getRow();
            ergebnis.beforeFirst();

            weisungMeldungArray = new EclWeisungMeldung[anzInArray];

            int i = 0;
            while (ergebnis.next() == true) {
                weisungMeldungArray[i] = this.decodeErgebnis(ergebnis);
                i++;
            }
            ergebnis.close();
            pstm1.close();

        } catch (Exception e) {
            CaBug.drucke("001");
            System.err.println(" " + e.getMessage());
        }
       
        return anzInArray;
       
    }
    
    /*================================Funktionen zum effizienten Bearbeiten einer einzelnen Weisung========================*/
    
    private List<EclWeisungMeldungEinzelneWeisung> ergebnis_einzelneWeisung=null;
    
    public List<EclWeisungMeldungEinzelneWeisung> ergebnis_einzelneWeisung(){
        return ergebnis_einzelneWeisung;
    }
    
    public int leseAktionaersWeisungenZuWeisungsIdent_einzelneWeisung(int pWeisungsIdent) {
        try {

            String sql=
                    "SELECT weisungIdent, abgabe"+Integer.toString(pWeisungsIdent)+" FROM "+dbBundle.getSchemaMandant() + "tbl_weisungMeldung WHERE "
                            + "aktiv=1 AND sammelIdent!=0";
            PreparedStatement pstm1 = verbindung.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
            ResultSet ergebnis = pstm1.executeQuery();
            ergebnis.beforeFirst();
            
            ergebnis_einzelneWeisung=new LinkedList<EclWeisungMeldungEinzelneWeisung>();
            while (ergebnis.next() == true) {
                EclWeisungMeldungEinzelneWeisung lWeisungMeldungEinzelneWeisung=new EclWeisungMeldungEinzelneWeisung();
                
                /**Dekodieren*/
                lWeisungMeldungEinzelneWeisung.weisungIdent=ergebnis.getInt("weisungIdent");
                lWeisungMeldungEinzelneWeisung.abgabe=ergebnis.getInt("abgabe" + Integer.toString(pWeisungsIdent));
                
                ergebnis_einzelneWeisung.add(lWeisungMeldungEinzelneWeisung);
            }
            ergebnis.close();
            pstm1.close();
        } catch (Exception e) {
            CaBug.drucke("001");
            System.err.println(" " + e.getMessage());
        }
        return 1;
    }
    
    public int updateAktionaersWeisungen_einzelneWeisung(int pWeisungsIdent, int pIdentWeisungssatzZiel, int pWeisungsart, boolean pNurUnmarkierte) {
        try {

            String sql = "UPDATE " + dbBundle.getSchemaMandant() + "tbl_weisungMeldung SET "
                    + "abgabe"+Integer.toString(pIdentWeisungssatzZiel)+"=? "
                    + " WHERE " + "weisungIdent=? ";
            if (pNurUnmarkierte) {
                sql+="AND abgabe"+Integer.toString(pIdentWeisungssatzZiel)+"=0 ";
            }

            PreparedStatement pstm1 = verbindung.prepareStatement(sql);
            pstm1.setInt(1, pWeisungsart);
            pstm1.setInt(2, pWeisungsIdent);

            /*int ergebnis1 = */pstm1.executeUpdate();
            pstm1.close();
        } catch (Exception e1) {
            CaBug.drucke("001");
            System.err.println(" " + e1.getMessage());
            return (CaFehler.pfdUnbekannterFehler);

        }
        return 1;
    }
    
    
    /**************************************************Temporär für Merge******************************************/
    /**Füllt nur weisungMeldung. aus _1. Sortiert nach meldungsIdent, weisungIdent*/
    public int leseAktive_1() {
        int anzInArray = 0;
        try {

            String sql = "";
            sql = "SELECT wm.* from " + dbBundle.getSchemaMandant() + "tbl_weisungMeldung_1 wm where "
                    + "wm.mandant=? AND wm.aktiv=1 AND wm.sammelIdent!=0 ORDER BY wm.meldungsIdent, wm.weisungIdent";

            PreparedStatement pstm1 = verbindung.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
            pstm1.setInt(1, dbBundle.clGlobalVar.mandant);

            ResultSet ergebnis = pstm1.executeQuery();
            ergebnis.last();
            anzInArray = ergebnis.getRow();
            ergebnis.beforeFirst();

            weisungMeldungArray = new EclWeisungMeldung[anzInArray];

            int i = 0;
            while (ergebnis.next() == true) {
                weisungMeldungArray[i] = this.decodeErgebnis(ergebnis);
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

    public int leseAktive_merge() {
        int anzInArray = 0;
        try {

            String sql = "";
            sql = "SELECT wm.* from " + dbBundle.getSchemaMandant() + "tbl_weisungMeldung_merge wm where "
                    + "wm.mandant=? AND wm.aktiv=1 AND wm.sammelIdent!=0 ORDER BY wm.meldungsIdent, wm.weisungIdent";

            PreparedStatement pstm1 = verbindung.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
            pstm1.setInt(1, dbBundle.clGlobalVar.mandant);

            ResultSet ergebnis = pstm1.executeQuery();
            ergebnis.last();
            anzInArray = ergebnis.getRow();
            ergebnis.beforeFirst();

            weisungMeldungArray = new EclWeisungMeldung[anzInArray];

            int i = 0;
            while (ergebnis.next() == true) {
                weisungMeldungArray[i] = this.decodeErgebnis(ergebnis);
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

    /**Füllt nur weisungMeldung. aus aktuellem Bestand. Sortiert nach meldungsIdent, weisungIdent*/
    public int leseAktive_Aktuelle() {
        int anzInArray = 0;
        try {

            String sql = "";
            sql = "SELECT wm.* from " + dbBundle.getSchemaMandant() + "tbl_weisungMeldung wm where "
                    + "wm.mandant=? AND wm.aktiv=1 AND wm.sammelIdent!=0 ORDER BY wm.meldungsIdent, wm.weisungIdent";

            PreparedStatement pstm1 = verbindung.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
            pstm1.setInt(1, dbBundle.clGlobalVar.mandant);

            ResultSet ergebnis = pstm1.executeQuery();
            ergebnis.last();
            anzInArray = ergebnis.getRow();
            ergebnis.beforeFirst();

            weisungMeldungArray = new EclWeisungMeldung[anzInArray];

            int i = 0;
            while (ergebnis.next() == true) {
                weisungMeldungArray[i] = this.decodeErgebnis(ergebnis);
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

    public int createTable_Merge() {
        DbLowLevel lDbLowLevel = new DbLowLevel(dbBundle);
        lDbLowLevel.dropTable("DROP TABLE "+dbBundle.getSchemaMandant() + "tbl_weisungmeldung_merge");
        
        int rc = 0;
        
        
        String hSql = "CREATE TABLE " + dbBundle.getSchemaMandant() + "tbl_weisungmeldung_merge ( "
                + "`mandant` int(11) NOT NULL, " + "`weisungIdent` int(11) NOT NULL, "
                + "`meldungsIdent` int(11) DEFAULT NULL, " + "`sammelIdent` int(11) DEFAULT NULL, "
                + "`skIst` int(11) DEFAULT NULL, " + "`willenserklaerungIdent` int(11) DEFAULT NULL, "
                + "`weisungSplit` int(11) DEFAULT NULL, " + "`aktiv` int(11) DEFAULT NULL, "
                + "`istWeisungBriefwahl` int(11) DEFAULT NULL, "
                + "`gemaessFremdenAbstimmungsVorschlagIdent` int(11) DEFAULT NULL, "
                + "`gemaessEigenemAbstimmungsVorschlagIdent` int(11) DEFAULT NULL, "
                + "`stimmartGesamt` int(11) DEFAULT NULL, " + "`aktualisieren` int(11) DEFAULT NULL, ";
        for (int i = 0; i <= 199; i++) {
            hSql = hSql + "`abgabe" + Integer.toString(i) + "` int(11) DEFAULT NULL, ";
        }
        for (int i = 0; i <= 199; i++) {
            hSql = hSql + "`abgabeLautGesamt" + Integer.toString(i) + "` int(11) DEFAULT NULL, ";
        }
        hSql = hSql +

                "PRIMARY KEY (`weisungIdent`,`mandant`), " + "KEY `IDX_meldungsIdent` (`meldungsIdent`), "
                + "KEY `IDX_sammelIdent` (`sammelIdent`) " + ") ";
        rc = lDbLowLevel.createTable(hSql);
        if (rc < 0) {
            return rc;
        }
        return 1;
    }

    public int insert_Merge(EclWeisungMeldung lWeisungMeldung) {

        int erg=0;
        lWeisungMeldung.mandant = dbBundle.clGlobalVar.mandant;

        /* Verarbeitungshinweise: 
         *  >   nachdem InterneIdent immer eindeutig vergeben werden, ist prinzipiell eine "Doppeleinfügung"
         *      von InterneIdent nicht möglich
         */

        try {

            /*Felder Neuanlage füllen*/
            String sql1 = "INSERT INTO " + dbBundle.getSchemaMandant() + "tbl_weisungMeldung_merge " + "("
                    + "mandant, weisungIdent, " + "meldungsIdent, sammelIdent, skIst, willenserklaerungIdent, "
                    + "weisungSplit, " + "aktiv, istWeisungBriefwahl, "
                    + "gemaessFremdenAbstimmungsVorschlagIdent, gemaessEigenemAbstimmungsVorschlagIdent, stimmartGesamt, aktualisieren";

            int i;
            for (i = 0; i < 200; i++) {
                sql1 = sql1 + ", abgabe" + Integer.toString(i);
            }
            for (i = 0; i < 200; i++) {
                sql1 = sql1 + ", abgabeLautGesamt" + Integer.toString(i);
            }
            sql1 = sql1 + ")" + "VALUES (" + "?, ?, " + "?, ?, ?, ?, " + "?, " + "?, ?, " + "?, ?, ?, ?";
            for (i = 0; i < 200; i++) {
                sql1 = sql1 + ", ?, ?";
            }

            sql1 = sql1 + " )";
            PreparedStatement pstm1 = verbindung.prepareStatement(sql1, ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);

            fuellePreparedStatementKomplett(pstm1, 1, lWeisungMeldung);

            erg = 0; /*Falls Duplicate Key, dann wird exception geschmissen und erg wohl nicht gefüllt!*/
            //          System.out.println("vor Update");
            erg = pstm1.executeUpdate();
            //          System.out.println("nach Update");
            pstm1.close();
        } catch (Exception e2) {
            CaBug.drucke("DbWeisungMeldung.insert 001");
            System.err.println(" " + e2.getMessage());
        }
        return erg;
    }

    public int pruefeObInMerge(int lIdent) {
        int anzInArray = 0;
        try {

            String sql = "";
                sql = "SELECT wm.* from " + dbBundle.getSchemaMandant() + "tbl_weisungMeldung_merge wm where "
                        + "wm.mandant=? AND ";
                     sql = sql + "wm.aktiv=1 AND ";
                 sql = sql + "wm.meldungsIdent=? ORDER BY wm.weisungIdent;";

            PreparedStatement pstm1 = verbindung.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
            pstm1.setInt(1, dbBundle.clGlobalVar.mandant);
            pstm1.setInt(2, lIdent);

            ResultSet ergebnis = pstm1.executeQuery();
            ergebnis.last();
            anzInArray = ergebnis.getRow();
            ergebnis.close();
            pstm1.close();

        } catch (Exception e) {
            CaBug.drucke("001");
            System.err.println(" " + e.getMessage());
        }
        return anzInArray;
    }

    /**Füllt nur weisungMeldung*/
    public int leseAktiveZuMeldung_1(int pMeldung) {
        int anzInArray = 0;
        try {

            String sql = "";
            sql = "SELECT wm.* from " + dbBundle.getSchemaMandant() + "tbl_weisungMeldung_1 wm where "
                    + "wm.mandant=? AND wm.aktiv=1 AND wm.meldungsIdent=?";

            PreparedStatement pstm1 = verbindung.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
            pstm1.setInt(1, dbBundle.clGlobalVar.mandant);
            pstm1.setInt(2, pMeldung);

            ResultSet ergebnis = pstm1.executeQuery();
            ergebnis.last();
            anzInArray = ergebnis.getRow();
            ergebnis.beforeFirst();

            weisungMeldungArray = new EclWeisungMeldung[anzInArray];

            int i = 0;
            while (ergebnis.next() == true) {
                weisungMeldungArray[i] = this.decodeErgebnis(ergebnis);
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

}
