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
package de.meetingapps.meetingportal.meetComDbVersion;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import de.meetingapps.meetingportal.meetComAllg.CaBug;
import de.meetingapps.meetingportal.meetComAllg.CaFehler;
import de.meetingapps.meetingportal.meetComDb.DbBundle;
import de.meetingapps.meetingportal.meetComDb.DbLowLevel;
import de.meetingapps.meetingportal.meetComEntities.EclEmittenten;

public class DbEmittentenV1 {

    private Connection verbindung = null;
    private DbBundle dbBundle = null;

    public EclEmittenten ergebnisArray[] = null;

    /*************************Initialisierung***************************/
    public DbEmittentenV1(DbBundle pDbBundle) {
        /* Verbindung in lokale Daten eintragen*/
        if (pDbBundle == null) {
            CaBug.drucke("DbEmittenten.init 001 - dbBundle nicht initialisiert");
            return;
        }
        if (pDbBundle.dbBasis == null) {
            CaBug.drucke("DbEmittenten.init 002 - dbBasis nicht initialisiert");
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
    public EclEmittenten ergebnisPosition(int pN) {
        if (ergebnisArray == null) {
            CaBug.drucke("DbEmittenten.ergebnisPosition 001");
            return null;
        }
        if (pN < 0) {
            CaBug.drucke("DbEmittenten.ergebnisPosition 002");
            return null;
        }
        if (pN >= ergebnisArray.length) {
            CaBug.drucke("DbEmittenten.ergebnisPosition 003");
            return null;
        }
        return ergebnisArray[pN];
    }

    /**************************deleteAll Löschen aller Datensätze  eines Mandanten****************/
    public int deleteAll() {
        int erg = 0;

        try {

            String sql1 = "DELETE FROM tbl_emittenten where mandant=?;";
            PreparedStatement pstm1 = verbindung.prepareStatement(sql1);
            pstm1.setInt(1, dbBundle.clGlobalVar.mandant);

            erg = 0; /*Falls Duplicate Key, dann wird exception geschmissen und erg wohl nicht gefüllt!*/
            erg = pstm1.executeUpdate();

            pstm1.close();
        } catch (Exception e2) {
            CaBug.drucke("DbEmittenten.deleteAll 001");
            System.err.println(" " + e2.getMessage());
            return (erg);
        }

        return 1;
    }

    /*******Checken, ob table überhaupt vorhanden ist***************************/
    public boolean checkTableVorhanden() {
        DbLowLevel lDbLowLevel = new DbLowLevel(dbBundle);
        return lDbLowLevel.checkTableVorhanden("SELECT * from tbl_emittenten WHERE mandant=0; ");

    }

    /************Neuanlegen Table - Version 1******************************/
    public int createTableV1() {
        int rc = 0;
        DbLowLevel lDbLowLevel = new DbLowLevel(dbBundle);
        rc = lDbLowLevel.createTable("CREATE TABLE `tbl_emittenten` ( " + "`mandant` int(11) NOT NULL, "
                + "`db_version` bigint(20) DEFAULT NULL, " + "`bezeichnungKurz` varchar(80) DEFAULT NULL, "
                + "`bezeichnungLang` varchar(200) DEFAULT NULL, " + "`hvDatum` char(10) DEFAULT NULL, "
                + "`hvOrt` varchar(50) DEFAULT NULL, " + "`portalVorhanden` int(11) DEFAULT NULL, "
                + "`portalAktuellAktiv` int(11) DEFAULT NULL, " + "`appVorhanden` int(11) DEFAULT NULL, "
                + "`appAktiv` int(11) DEFAULT NULL, " + "PRIMARY KEY (`mandant`) " + ") ");
        return rc;
    }

    public int alterTableV1_V2() {
        int rc = 0;
        DbLowLevel lDbLowLevel = new DbLowLevel(dbBundle);

        rc = lDbLowLevel.rawOperation("ALTER TABLE db_meetingcomfort.tbl_emittenten "
                + "ADD COLUMN bezeichnungsArtApp INT(11) NULL DEFAULT 1 AFTER bezeichnungLang;");
        if (rc < 0) {
            return rc;
        }

        rc = lDbLowLevel.rawOperation("ALTER TABLE db_meetingcomfort.tbl_emittenten "
                + "ADD COLUMN bezeichnungsArtPortal INT(11) NULL DEFAULT 1 AFTER bezeichnungsArtApp;");
        if (rc < 0) {
            return rc;
        }

        rc = lDbLowLevel.rawOperation("ALTER TABLE db_meetingcomfort.tbl_emittenten "
                + "ADD COLUMN bezeichnungsArtFormulare INT(11) NULL DEFAULT 1 AFTER bezeichnungsArtPortal;");
        if (rc < 0) {
            return rc;
        }

        rc = lDbLowLevel.rawOperation("ALTER TABLE db_meetingcomfort.tbl_emittenten "
                + "ADD COLUMN hvJahr INT(11) NOT NULL DEFAULT 2018 AFTER mandant;");
        if (rc < 0) {
            return rc;
        }

        rc = lDbLowLevel.rawOperation("ALTER TABLE db_meetingcomfort.tbl_emittenten "
                + "ADD COLUMN hvNummer CHAR(1) NOT NULL DEFAULT 'A' AFTER hvJahr;");
        if (rc < 0) {
            return rc;
        }

        rc = lDbLowLevel.rawOperation("ALTER TABLE db_meetingcomfort.tbl_emittenten "
                + "ADD COLUMN dbArt CHAR(1) NOT NULL DEFAULT 'P' AFTER hvNummer;");
        if (rc < 0) {
            return rc;
        }

        rc = lDbLowLevel.rawOperation("ALTER TABLE db_meetingcomfort.tbl_emittenten "
                + "ADD COLUMN inDBVorhanden INT(11) NULL DEFAULT 1 AFTER dbArt;");
        if (rc < 0) {
            return rc;
        }

        rc = lDbLowLevel.rawOperation("ALTER TABLE db_meetingcomfort.tbl_emittenten "
                + "ADD COLUMN dbGesperrt INT(11) NULL DEFAULT 0 AFTER appAktiv;");
        if (rc < 0) {
            return rc;
        }

        rc = lDbLowLevel.rawOperation("ALTER TABLE db_meetingcomfort.tbl_emittenten "
                + "ADD COLUMN inAuswahl INT(11) NULL DEFAULT 3 AFTER dbGesperrt;");
        if (rc < 0) {
            return rc;
        }

        rc = lDbLowLevel.rawOperation("ALTER TABLE db_meetingcomfort.tbl_emittenten "
                + "ADD COLUMN portalSprache INT(11) NULL DEFAULT 3 AFTER portalAktuellAktiv;");
        if (rc < 0) {
            return rc;
        }

        rc = lDbLowLevel.rawOperation("ALTER TABLE db_meetingcomfort.tbl_emittenten "
                + "ADD COLUMN verwendeStandardXHTML INT(11) NULL DEFAULT 0 AFTER portalSprache;");
        if (rc < 0) {
            return rc;
        }

        rc = lDbLowLevel.rawOperation("ALTER TABLE db_meetingcomfort.tbl_emittenten "
                + "ADD COLUMN appSprache INT(11) NULL DEFAULT 1 AFTER appAktiv;");
        if (rc < 0) {
            return rc;
        }

        rc = lDbLowLevel.rawOperation("ALTER TABLE db_meetingcomfort.tbl_emittenten "
                + "ADD COLUMN emittentenPortalVorhanden INT(11) NULL DEFAULT 1 AFTER appSprache;");
        if (rc < 0) {
            return rc;
        }

        rc = lDbLowLevel.rawOperation("ALTER TABLE db_meetingcomfort.tbl_emittenten "
                + "ADD COLUMN emittentenPortalAktiv INT(11) NULL DEFAULT 1 AFTER emittentenPortalVorhanden;");
        if (rc < 0) {
            return rc;
        }

        rc = lDbLowLevel.rawOperation("ALTER TABLE db_meetingcomfort.tbl_emittenten "
                + "ADD COLUMN emittentenPortalSprache INT(11) NULL DEFAULT 1 AFTER emittentenPortalAktiv;");
        if (rc < 0) {
            return rc;
        }

        rc = lDbLowLevel.rawOperation("ALTER TABLE db_meetingcomfort.tbl_emittenten "
                + "ADD COLUMN registerAnbindungVorhanden INT(11) NULL DEFAULT 0 AFTER emittentenPortalSprache;");
        if (rc < 0) {
            return rc;
        }

        rc = lDbLowLevel.rawOperation("ALTER TABLE db_meetingcomfort.tbl_emittenten "
                + "ADD COLUMN registerAnbindungAktiv INT(11) NULL DEFAULT 0 AFTER registerAnbindungVorhanden;");
        if (rc < 0) {
            return rc;
        }

        rc = lDbLowLevel.rawOperation("ALTER TABLE db_meetingcomfort.tbl_emittenten "
                + "ADD COLUMN registerAnbindungSprache INT(11) NULL DEFAULT 0 AFTER registerAnbindungAktiv;");
        if (rc < 0) {
            return rc;
        }

        rc = lDbLowLevel.rawOperation("ALTER TABLE db_meetingcomfort.tbl_emittenten "
                + "ADD COLUMN portalStandard INT(11) NULL DEFAULT 0 AFTER portalAktuellAktiv;");
        if (rc < 0) {
            return rc;
        }

        rc = lDbLowLevel.rawOperation("ALTER TABLE db_meetingcomfort.tbl_emittenten " + "DROP PRIMARY KEY, "
                + "ADD PRIMARY KEY (`mandant`, `hvJahr`, `hvNummer`, `dbArt`); ");
        if (rc < 0) {
            return rc;
        }

        return rc;

    }

    /********** dekodiert die aktuelle Position aus ergebnis in EclAktienregisterEintrag und gibt dieses zurück******/
    EclEmittenten decodeErgebnis(ResultSet pErgebnis) {

        EclEmittenten lEmittenten = new EclEmittenten();

        try {

            lEmittenten.mandant = pErgebnis.getInt("em.mandant");
            lEmittenten.mandantExtern = lEmittenten.mandant;
            lEmittenten.db_version = pErgebnis.getLong("em.db_version");
            lEmittenten.bezeichnungKurz = pErgebnis.getString("em.bezeichnungKurz");
            lEmittenten.bezeichnungLang = pErgebnis.getString("em.bezeichnungLang");
            lEmittenten.hvDatum = pErgebnis.getString("em.hvDatum");
            lEmittenten.hvOrt = pErgebnis.getString("em.hvOrt");
            lEmittenten.portalVorhanden = pErgebnis.getInt("em.portalVorhanden");
            lEmittenten.portalAktuellAktiv = pErgebnis.getInt("em.portalAktuellAktiv");
            lEmittenten.appVorhanden = pErgebnis.getInt("em.appVorhanden");
            lEmittenten.appAktiv = pErgebnis.getInt("em.appAktiv");
        } catch (Exception e) {
            CaBug.drucke("DbEmittenten.decodeErgebnis 001");
            System.err.println(" " + e.getMessage());
        }

        return lEmittenten;
    }

    /********************* Fuellen Prepared Statement mit allen Feldern, beginnend bei offset.*****************
     * Kann sowohl für Insert, als auch für update verwendet werden.
     * 
     * offset= Startposition des ersten Feldes (also z.B. 1)
     */
    private int anzfelder = 10; /*Anpassen auf Anzahl der Felder pro Datensatz*/

    void fuellePreparedStatementKomplett(PreparedStatement pPStm, int pOffset, EclEmittenten pEmittenten) {

        int startOffset = pOffset; /*Nur erforderlich zum Überprüfen von Programmierfehlern - Feldanzahl*/

        try {
            pPStm.setInt(pOffset, pEmittenten.mandant);
            pOffset++;
            pPStm.setLong(pOffset, pEmittenten.db_version);
            pOffset++;

            pPStm.setString(pOffset, pEmittenten.bezeichnungKurz);
            pOffset++;
            pPStm.setString(pOffset, pEmittenten.bezeichnungLang);
            pOffset++;
            pPStm.setString(pOffset, pEmittenten.hvDatum);
            pOffset++;
            pPStm.setString(pOffset, pEmittenten.hvOrt);
            pOffset++;

            pPStm.setInt(pOffset, pEmittenten.portalVorhanden);
            pOffset++;
            pPStm.setInt(pOffset, pEmittenten.portalAktuellAktiv);
            pOffset++;
            pPStm.setInt(pOffset, pEmittenten.appVorhanden);
            pOffset++;
            pPStm.setInt(pOffset, pEmittenten.appAktiv);
            pOffset++;

            if (pOffset - startOffset != anzfelder) {
                /*Nur wg. Überprüfung auf Programmierfehler - alle Felder berücksichtigt?*/
                CaBug.drucke("DbEmittenten.fuellePreparedStatementKomplett 002");
            }

        } catch (SQLException e) {
            CaBug.drucke("DbEmittenten.fuellePreparedStatementKomplett 001");
            e.printStackTrace();
        }

    }

    /**Insert
     * 
     * Feld mandant wird von dieser Funktion nicht selbstständig belegt.
     * 
     * Returnwert:
     * =1 => Insert erfolgreich
     * ansonsten: Fehler
     */
    public int insert(EclEmittenten pEmittenten) {

        int erg = 0;

        try {

            /*Felder Neuanlage füllen*/
            String lSql = "INSERT INTO tbl_emittenten " + "(" + "mandant, db_version, "
                    + "bezeichnungKurz, bezeichnungLang, hvDatum, hvOrt, "
                    + "portalVorhanden, portalAktuellAktiv, appVorhanden, appAktiv)" + "VALUES (" + "?, ?, "
                    + "?, ?, ?, ?, " + "?, ?, ?, ?" + ")";
            PreparedStatement lPStm = verbindung.prepareStatement(lSql, ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);

            fuellePreparedStatementKomplett(lPStm, 1, pEmittenten);

            erg = lPStm.executeUpdate();
            lPStm.close();
        } catch (Exception e2) {
            CaBug.drucke("DbEmittenten.insert 001");
            System.err.println(" " + e2.getMessage());
        }

        if (erg == 0) {/*Fehler beim Einfügen - d.h. primaryKey bereits vorhanden*/
            return (-1);
        }

        return (1);
    }

    /**Einlesen des Satzes pMandant.*/
    public int read(int pMandant) {
        int anzInArray = 0;
        PreparedStatement lPStm = null;

        try {
            String lSql = "SELECT * from tbl_emittenten em where " + "em.mandant=?;";
            lPStm = verbindung.prepareStatement(lSql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            lPStm.setInt(1, pMandant);

            ResultSet lErgebnis = lPStm.executeQuery();
            lErgebnis.last();
            anzInArray = lErgebnis.getRow();
            lErgebnis.beforeFirst();

            ergebnisArray = new EclEmittenten[anzInArray];

            int i = 0;
            while (lErgebnis.next() == true) {
                ergebnisArray[i] = this.decodeErgebnis(lErgebnis);
                i++;
            }
            lErgebnis.close();
            lPStm.close();

        } catch (Exception e) {
            CaBug.drucke("DbEmittenten.read 003");
            System.err.println(" " + e.getMessage());
            return (-1);
        }
        return (anzInArray);
    }

    /**Einlesen des Satzes pMandant.*/
    public int read_appAktiv() {
        int anzInArray = 0;
        PreparedStatement lPStm = null;

        try {
            String lSql = "SELECT * from tbl_emittenten em where " + "appAktiv=1;";
            lPStm = verbindung.prepareStatement(lSql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);

            ResultSet lErgebnis = lPStm.executeQuery();
            lErgebnis.last();
            anzInArray = lErgebnis.getRow();
            lErgebnis.beforeFirst();

            ergebnisArray = new EclEmittenten[anzInArray];

            int i = 0;
            while (lErgebnis.next() == true) {
                ergebnisArray[i] = this.decodeErgebnis(lErgebnis);
                i++;
            }
            lErgebnis.close();
            lPStm.close();

        } catch (Exception e) {
            CaBug.drucke("DbEmittenten.read 003");
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
    public int update(EclEmittenten pEmittenten) {

        pEmittenten.db_version++;

        try {
            String lSql = "UPDATE tbl_emittenten SET " + "mandant=?, db_version=?, "
                    + "bezeichnungKurz=?, bezeichnungLang=?, hvDatum=?, hvOrt=?, "
                    + "portalVorhanden=?, portalAktuellAktiv=?, appVorhanden=?, appAktiv=? " + "WHERE "
                    + "db_version=? AND mandant=?";

            PreparedStatement lPStm = verbindung.prepareStatement(lSql);
            fuellePreparedStatementKomplett(lPStm, 1, pEmittenten);
            lPStm.setLong(anzfelder + 1, pEmittenten.db_version - 1);
            lPStm.setInt(anzfelder + 2, pEmittenten.mandant);

            int ergebnis = lPStm.executeUpdate();
            lPStm.close();
            if (ergebnis == 0) {
                return (CaFehler.pfXyWurdeVonAnderemBenutzerVeraendert);
            }
        } catch (Exception e1) {
            CaBug.drucke("DbEmittenten.update 001");
            System.err.println(" " + e1.getMessage());
            return (CaFehler.pfdXyBereitsVorhanden);
        }

        return (1);
    }

    public int delete(int pMandant) {

        try {

            String sql = "DELETE FROM tbl_emittenten WHERE mandant=? ";

            PreparedStatement pstm1 = verbindung.prepareStatement(sql);
            pstm1.setInt(1, pMandant);

            int ergebnis1 = pstm1.executeUpdate();
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
