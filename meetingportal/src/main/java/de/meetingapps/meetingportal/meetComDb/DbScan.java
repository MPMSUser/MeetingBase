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
import de.meetingapps.meetingportal.meetComEntities.EclScan;

public class DbScan {

//    int logDrucken=10;
    
    private Connection verbindung = null;
    private DbBundle dbBundle = null;

    public EclScan ergebnisArray[] = null;

       /*
        ALTER TABLE `db_meetingcomfort`.`tbl_scan` 
        ADD COLUMN `istAnmelden` INT(11) NULL DEFAULT NULL AFTER `verarbeitet`;

        */
    /*************************Initialisierung***************************/
    public DbScan(DbBundle pDbBundle) {
        /* Verbindung in lokale Daten eintragen*/
        if (pDbBundle == null) {
            CaBug.drucke("DbScan.init 001 - dbBundle nicht initialisiert");
            return;
        }
        if (pDbBundle.dbBasis == null) {
            CaBug.drucke("DbScan.init 002 - dbBasis nicht initialisiert");
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
    public EclScan ergebnisPosition(int pN) {
        if (ergebnisArray == null) {
            CaBug.drucke("DbScan.ergebnisPosition 001");
            return null;
        }
        if (pN < 0) {
            CaBug.drucke("DbScan.ergebnisPosition 002");
            return null;
        }
        if (pN >= ergebnisArray.length) {
            CaBug.drucke("DbScan.ergebnisPosition 003");
            return null;
        }
        return ergebnisArray[pN];
    }

    public int createTable() {
        int rc = 0;
        DbLowLevel lDbLowLevel = new DbLowLevel(dbBundle);
        String hSql = "CREATE TABLE " + dbBundle.getSchemaAllgemein() + "tbl_scan ( " + "`mandant` int(11) NOT NULL, "
                + "`ident` int(11) NOT NULL AUTO_INCREMENT, " + "`barcode` varchar(45) DEFAULT NULL, "
                + "`dateiname` varchar(100) DEFAULT NULL, " + "`scanNummer` varchar(12) DEFAULT NULL, "
                + "`verarbeitet` int(11) DEFAULT NULL, " 
                + "`istAnmelden` int(11) DEFAULT NULL, "
                + "`istBriefwahl` int(11) DEFAULT NULL, "
                + "`istSRV` int(11) DEFAULT NULL, " + "`istSRVHV` int(11) DEFAULT NULL, "
                + "`istAbstimmung` int(11) DEFAULT NULL, " + "`ist1EK` char(9) DEFAULT NULL, "
                + "`ist1EKVollmacht` char(9) DEFAULT NULL, " + "`ist2EK` int(11) DEFAULT NULL, "
                + "`gesamtmarkierung` char(3) DEFAULT NULL, ";

        for (int i = 1; i <= 60; i++) {
            hSql = hSql + "`pos" + CaString.fuelleLinksNull(Integer.toString(i), 0) + "` char(3) DEFAULT NULL, ";
        }
        hSql = hSql + "`geraeteNummer` varchar(10) DEFAULT NULL, " + "`zeitstempel` varchar(20) DEFAULT NULL, "
                + "PRIMARY KEY (`ident`,`mandant`), " + "UNIQUE KEY `ident_UNIQUE` (`ident`) " + ") ";

        rc = lDbLowLevel.createTable(hSql);
        return rc;
    }

    /**************************deleteAll Löschen aller Datensätze  eines Mandanten****************/
    public int deleteAll() {
        return dbBundle.dbLowLevel
                .deleteMandant("DELETE FROM " + dbBundle.getSchemaAllgemein() + "tbl_scan where mandant=?;");
    }

    /********** dekodiert die aktuelle Position aus ergebnis in EclScan und gibt dieses zurück******/
    EclScan decodeErgebnis(ResultSet pErgebnis) {

        EclScan lScan = new EclScan();

        try {

            lScan.mandant = pErgebnis.getInt("sc.mandant");
            lScan.ident = pErgebnis.getInt("sc.ident");

            lScan.barcode = pErgebnis.getString("sc.barcode");
            if (pErgebnis.wasNull()) {
                lScan.barcode = "";
            }

            lScan.dateiname = pErgebnis.getString("sc.dateiname");
            if (pErgebnis.wasNull()) {
                lScan.dateiname = "";
            }
            lScan.scanNummer = pErgebnis.getString("sc.scanNummer");
            if (pErgebnis.wasNull()) {
                lScan.scanNummer = "";
            }

            lScan.verarbeitet = pErgebnis.getInt("sc.verarbeitet");

            lScan.istAnmelden = pErgebnis.getInt("sc.istAnmelden");
            if (pErgebnis.wasNull()) {
                lScan.istAnmelden = 0;
            }

            lScan.istBriefwahl = pErgebnis.getInt("sc.istBriefwahl");
            if (pErgebnis.wasNull()) {
                lScan.istBriefwahl = 0;
            }

            lScan.istSRV = pErgebnis.getInt("sc.istSRV");
            if (pErgebnis.wasNull()) {
                lScan.istSRV = 0;
            }

            lScan.istSRVHV = pErgebnis.getInt("sc.istSRVHV");
            if (pErgebnis.wasNull()) {
                lScan.istSRVHV = 0;
            }

            lScan.istAbstimmung = pErgebnis.getInt("sc.istAbstimmung");
            if (pErgebnis.wasNull()) {
                lScan.istAbstimmung = 0;
            }

            lScan.ist1EK = pErgebnis.getString("sc.ist1EK");
            if (pErgebnis.wasNull()) {
                lScan.ist1EK = "";
            }

            lScan.ist1EKVollmacht = pErgebnis.getString("sc.ist1EKVollmacht");
            if (pErgebnis.wasNull()) {
                lScan.ist1EKVollmacht = "";
            }

            lScan.ist2EK = pErgebnis.getInt("sc.ist2EK");
            if (pErgebnis.wasNull()) {
                lScan.ist2EK = 0;
            }

            lScan.gesamtmarkierung = pErgebnis.getString("sc.gesamtmarkierung");
            if (pErgebnis.wasNull()) {
                lScan.gesamtmarkierung = "";
            }

            lScan.pos = new String[61];
            lScan.pos[0] = "";
            for (int i = 1; i <= 60; i++) {
                lScan.pos[i] = pErgebnis.getString("sc.pos" + CaString.fuelleLinksNull(Integer.toString(i), 2));
//                if (i<5) {
//                    CaBug.druckeLog("i="+i+" lScan.pos[i]="+lScan.pos[i], logDrucken, 10);
//                }
                if (pErgebnis.wasNull()) {
                    lScan.pos[i] = "";
                }
            }

            lScan.geraeteNummer = pErgebnis.getString("sc.geraeteNummer");
            if (pErgebnis.wasNull()) {
                lScan.geraeteNummer = "";
            }
            lScan.zeitstempel = pErgebnis.getString("sc.zeitstempel");
            if (pErgebnis.wasNull()) {
                lScan.zeitstempel = "";
            }

        } catch (Exception e) {
            CaBug.drucke("DbScan.decodeErgebnis 001");
            System.err.println(" " + e.getMessage());
        }

        return lScan;
    }

    /********************* Fuellen Prepared Statement mit allen Feldern, beginnend bei offset.*****************
     * Kann sowohl für Insert, als auch für update verwendet werden.
     * 
     * offset= Startposition des ersten Feldes (also z.B. 1)
     */
    private int anzfelder = 77; /*Anpassen auf Anzahl der Felder pro Datensatz*/

    void fuellePreparedStatementKomplett(PreparedStatement pPStm, int pOffset, EclScan pScan) {

        int startOffset = pOffset; /*Nur erforderlich zum Überprüfen von Programmierfehlern - Feldanzahl*/

        try {
            pPStm.setInt(pOffset, pScan.mandant);
            pOffset++;
            pPStm.setInt(pOffset, pScan.ident);
            pOffset++;

            pPStm.setString(pOffset, pScan.barcode);
            pOffset++;

            pPStm.setString(pOffset, pScan.dateiname);
            pOffset++;
            pPStm.setString(pOffset, pScan.scanNummer);
            pOffset++;

            pPStm.setInt(pOffset, pScan.verarbeitet);
            pOffset++;

            pPStm.setInt(pOffset, pScan.istAnmelden);
            pOffset++;
            pPStm.setInt(pOffset, pScan.istBriefwahl);
            pOffset++;
            pPStm.setInt(pOffset, pScan.istSRV);
            pOffset++;
            pPStm.setInt(pOffset, pScan.istSRVHV);
            pOffset++;
            pPStm.setInt(pOffset, pScan.istAbstimmung);
            pOffset++;
            pPStm.setString(pOffset, pScan.ist1EK);
            pOffset++;
            pPStm.setString(pOffset, pScan.ist1EKVollmacht);
            pOffset++;
            pPStm.setInt(pOffset, pScan.ist2EK);
            pOffset++;

            pPStm.setString(pOffset, pScan.gesamtmarkierung);
            pOffset++;

            for (int i = 1; i <= 60; i++) {
                pPStm.setString(pOffset, pScan.pos[i]);
                pOffset++;
            }

            pPStm.setString(pOffset, pScan.geraeteNummer);
            pOffset++;
            pPStm.setString(pOffset, pScan.zeitstempel);
            pOffset++;

            if (pOffset - startOffset != anzfelder) {
                /*Nur wg. Überprüfung auf Programmierfehler - alle Felder berücksichtigt?*/
                CaBug.drucke("DbScan.fuellePreparedStatementKomplett 002");
            }

        } catch (SQLException e) {
            CaBug.drucke("DbScan.fuellePreparedStatementKomplett 001");
            e.printStackTrace();
        }

    }

    /**Insert
     * 
     * Feld mandant wird von dieser Funktion immer selbstständig belegt.
     * 
     * Returnwert:
     * =1 => Insert erfolgreich
     * ansonsten: Fehler
     */
    public int insert(EclScan pScan) {

        int erg = 0;

        pScan.mandant = dbBundle.clGlobalVar.mandant;

        try {
            /*Felder Neuanlage füllen*/
            String lSql = "INSERT INTO " + dbBundle.getSchemaAllgemein() + "tbl_scan " + "("
                    + "mandant, ident, barcode, "
                    + "dateiname, scanNummer, verarbeitet, istAnmelden, istBriefwahl, istSRV, istSRVHV, istAbstimmung, "
                    + "ist1EK, ist1EKVollmacht, ist2EK, gesamtmarkierung, ";
            for (int i = 1; i <= 60; i++) {
                lSql = lSql + "pos" + CaString.fuelleLinksNull(Integer.toString(i), 2) + ", ";
            }
            lSql = lSql + "geraeteNummer, zeitstempel)" + "VALUES (" + "?, ?, ?, "
                    + "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ";
            for (int i = 1; i <= 60; i++) {
                lSql = lSql + "?, ";
            }
            lSql = lSql + "?, ?" + ")";
            System.out.println(lSql);
            PreparedStatement lPStm = verbindung.prepareStatement(lSql, ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);

            fuellePreparedStatementKomplett(lPStm, 1, pScan);

            erg = lPStm.executeUpdate();
            lPStm.close();
        } catch (Exception e2) {
            CaBug.drucke("DbScan.insert 001");
            System.err.println(" " + e2.getMessage());
        }

        if (erg == 0) {/*Fehler beim Einfügen - d.h. primaryKey bereits vorhanden*/
            return (-1);
        }

        return (1);
    }

    /**Einlesen des Satzes*/
    public int read(int pIdent) {
        int anzInArray = 0;
        PreparedStatement lPStm = null;

        try {
            String lSql = "SELECT sc.* from " + dbBundle.getSchemaAllgemein() + "tbl_scan sc where "
                    + "sc.mandant=? AND sc.ident=?;";
            lPStm = verbindung.prepareStatement(lSql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            lPStm.setInt(1, dbBundle.clGlobalVar.mandant);
            lPStm.setInt(2, pIdent);

            ResultSet lErgebnis = lPStm.executeQuery();
            lErgebnis.last();
            anzInArray = lErgebnis.getRow();
            lErgebnis.beforeFirst();

            ergebnisArray = new EclScan[anzInArray];

            int i = 0;
            while (lErgebnis.next() == true) {
                ergebnisArray[i] = this.decodeErgebnis(lErgebnis);
                i++;
            }
            lErgebnis.close();
            lPStm.close();

        } catch (Exception e) {
            CaBug.drucke("DbScan.read 003");
            System.err.println(" " + e.getMessage());
            return (-1);
        }
        return (anzInArray);
    }

    /**Einlesen des Satzes mit barcode=XXXXX*/
    public int read_keysatz() {
        int anzInArray = 0;
        PreparedStatement lPStm = null;

        try {
            String lSql = "SELECT sc.* from " + dbBundle.getSchemaAllgemein() + "tbl_scan sc where "
                    + "sc.mandant=? AND sc.barcode='XXXXX';";
            lPStm = verbindung.prepareStatement(lSql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            lPStm.setInt(1, dbBundle.clGlobalVar.mandant);

            ResultSet lErgebnis = lPStm.executeQuery();
            lErgebnis.last();
            anzInArray = lErgebnis.getRow();
            lErgebnis.beforeFirst();

            ergebnisArray = new EclScan[anzInArray];

            int i = 0;
            while (lErgebnis.next() == true) {
                ergebnisArray[i] = this.decodeErgebnis(lErgebnis);
                i++;
            }
            lErgebnis.close();
            lPStm.close();

        } catch (Exception e) {
            CaBug.drucke("DbScan.read 003");
            System.err.println(" " + e.getMessage());
            return (-1);
        }
        return (anzInArray);
    }

    /**Update. 
     * 
     * Feld mandant wird von dieser Funktion selbstständig belegt.
     * 
     * Ansonsten: rc=1 => ok, ansonsten Fehler
     */
    public int update(EclScan pScan) {

        pScan.mandant = dbBundle.clGlobalVar.mandant;

        try {
            String lSql = "UPDATE " + dbBundle.getSchemaAllgemein() + "tbl_scan SET "
                    + "mandant=?, ident=?, barcode=?, "
                    + "dateiname=?, scanNummer=?, verarbeitet=?, istAnmelden=?, istBriefwahl=?, istSRV=?, istSRVHV=?, istAbstimmung=?, "
                    + "ist1EK=?, ist1EKVollmacht=?, ist2EK=?, gesamtmarkierung=?, ";
            for (int i = 1; i <= 60; i++) {
                lSql = lSql + "pos" + CaString.fuelleLinksNull(Integer.toString(i), 2) + "=?, ";
            }

            lSql = lSql + "geraeteNummer=?, zeitstempel=? " + "WHERE " + "mandant=? AND ident=?";

            PreparedStatement lPStm = verbindung.prepareStatement(lSql);
            fuellePreparedStatementKomplett(lPStm, 1, pScan);
            lPStm.setInt(anzfelder + 1, pScan.mandant);
            lPStm.setInt(anzfelder + 2, pScan.ident);

            int ergebnis = lPStm.executeUpdate();
            lPStm.close();
            if (ergebnis == 0) {
                return (CaFehler.pfXyWurdeVonAnderemBenutzerVeraendert);
            }
        } catch (Exception e1) {
            CaBug.drucke("DbScan.update 001");
            System.err.println(" " + e1.getMessage());
            return (CaFehler.pfdXyBereitsVorhanden);
        }

        return (1);
    }

    public int delete(int pIdent) {

        try {

            String sql = "DELETE FROM " + dbBundle.getSchemaAllgemein() + "tbl_scan WHERE mandant=? and ident=? ";

            PreparedStatement pstm1 = verbindung.prepareStatement(sql);
            pstm1.setInt(1, dbBundle.clGlobalVar.mandant);
            pstm1.setInt(2, pIdent);

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

    /**Vergibt eine neue ScanLaufNummer und schreibt diese in DB zurück. Liefert diese als 
     * Returnwert zurück
     */
    public int holeNeueScanLaufNummer() {
        int aktuellerScanLauf = 0;
        int anzGefunden = read_keysatz();
        if (anzGefunden == 0) {/*Neuen Key-Sazu anlegen*/
            aktuellerScanLauf = 1;
            EclScan lScan = new EclScan();
            lScan.barcode = "XXXXX";
            lScan.verarbeitet = aktuellerScanLauf;
            insert(lScan);
        } else {
            EclScan lScan = ergebnisArray[0];
            aktuellerScanLauf = lScan.verarbeitet + 1;
            lScan.verarbeitet = aktuellerScanLauf;
            update(lScan);
        }
        return aktuellerScanLauf;
    }

    /**Ermittelt die letzte bereits vergebene ScanLaufNummer und gibt diese zurück.*/
    public int holeLetzteScanLaufNummer() {
        int aktuellerScanLauf = 0;
        int anzGefunden = read_keysatz();
        if (anzGefunden == 0) {/*Noch kein Key-Satz vorhanden*/
            aktuellerScanLauf = -1;
        } else {
            EclScan lScan = ergebnisArray[0];
            aktuellerScanLauf = lScan.verarbeitet;
        }
        return aktuellerScanLauf;
    }

    @Deprecated
    public int updateUngeleseneScanVorgaenge(int pAktuellerScanLauf) {

        try {
            String lSql = "UPDATE " + dbBundle.getSchemaAllgemein() + "tbl_scan SET " + " verarbeitet=? " + "WHERE "
                    + "mandant=? AND (verarbeitet is Null OR verarbeitet=0)";

            PreparedStatement lPStm = verbindung.prepareStatement(lSql);
            lPStm.setInt(1, pAktuellerScanLauf);
            lPStm.setInt(2, dbBundle.clGlobalVar.mandant);

            int ergebnis = lPStm.executeUpdate();
            lPStm.close();
            if (ergebnis == 0) {
                return (CaFehler.pfXyWurdeVonAnderemBenutzerVeraendert);
            }
        } catch (Exception e1) {
            CaBug.drucke("DbScan.updateUngeleseneScanVorgaenge 001");
            System.err.println(" " + e1.getMessage());
            return (CaFehler.pfdXyBereitsVorhanden);
        }

        return (1);
    }

    /**Eines der mit True übergebenen Felder muß ungleich null oder 0 sein, d.h. markiert sein*/
    public int updateUngeleseneScanVorgaenge(int pAktuellerScanLauf, boolean pIstAnmelden, boolean pIstBriefwahl, boolean pIstSRV,
            boolean pIstSRVHV, boolean pIstAbstimmung, boolean pIst1EK, boolean pIst1EKVollmacht, boolean pIst2EK) {

        boolean etwasSelektiert = (pIstBriefwahl | pIstSRV | pIstSRVHV | pIstAbstimmung | pIst1EK | pIst1EKVollmacht
                | pIst2EK);
        try {
            String lSql = "UPDATE " + dbBundle.getSchemaAllgemein() + "tbl_scan SET " + " verarbeitet=? " + "WHERE "
                    + "mandant=? AND (verarbeitet is Null OR verarbeitet=0)";
            if (etwasSelektiert) {
                boolean bereitsEinWert = false;
                lSql = lSql + " AND (";

                if (pIstAnmelden) {
                    lSql = lSql + "istAnmelden=1 ";
                    bereitsEinWert = true;
                }
                if (pIstBriefwahl) {
                    if (bereitsEinWert) {
                        lSql = lSql + " OR ";
                    }
                    lSql = lSql + "istBriefwahl=1 ";
                    bereitsEinWert = true;
                }
                if (pIstSRV) {
                    if (bereitsEinWert) {
                        lSql = lSql + " OR ";
                    }
                    lSql = lSql + "istSRV=1 ";
                    bereitsEinWert = true;
                }
                if (pIstSRVHV) {
                    if (bereitsEinWert) {
                        lSql = lSql + " OR ";
                    }
                    lSql = lSql + "istSRVHV=1 ";
                    bereitsEinWert = true;
                }
                if (pIstAbstimmung) {
                    if (bereitsEinWert) {
                        lSql = lSql + " OR ";
                    }
                    lSql = lSql + "istAbstimmung=1 ";
                    bereitsEinWert = true;
                }
                if (pIst1EK) {
                    if (bereitsEinWert) {
                        lSql = lSql + " OR ";
                    }
                    lSql = lSql + "ist1EK='1' OR ist1EK='2' OR ist1EK='???' ";
                    bereitsEinWert = true;
                }
                if (pIst1EKVollmacht) {
                    if (bereitsEinWert) {
                        lSql = lSql + " OR ";
                    }
                    lSql = lSql + "(ist1EKVollmacht!='' AND ist1EKVollmacht!='0') ";
                    bereitsEinWert = true;
                }
                if (pIst2EK) {
                    if (bereitsEinWert) {
                        lSql = lSql + " OR ";
                    }
                    lSql = lSql + "ist2EK=1 ";
                    bereitsEinWert = true;
                }

                lSql = lSql + " )";
            }

            PreparedStatement lPStm = verbindung.prepareStatement(lSql);
            lPStm.setInt(1, pAktuellerScanLauf);
            lPStm.setInt(2, dbBundle.clGlobalVar.mandant);

            int ergebnis = lPStm.executeUpdate();
            lPStm.close();
            if (ergebnis == 0) {
                return (CaFehler.pfXyWurdeVonAnderemBenutzerVeraendert);
            }
        } catch (Exception e1) {
            CaBug.drucke("DbScan.updateUngeleseneScanVorgaenge 001");
            System.err.println(" " + e1.getMessage());
            return (CaFehler.pfdXyBereitsVorhanden);
        }

        return (1);
    }

    public int read_scanVorgaenge(int pAktuellerScanLauf) {
        int anzInArray = 0;
        PreparedStatement lPStm = null;

        try {
            String lSql = "SELECT sc.* from " + dbBundle.getSchemaAllgemein() + "tbl_scan sc where "
                    + "sc.mandant=? AND sc.verarbeitet=? AND sc.barcode!='XXXXX';";
            lPStm = verbindung.prepareStatement(lSql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            lPStm.setInt(1, dbBundle.clGlobalVar.mandant);
            lPStm.setInt(2, pAktuellerScanLauf);

            ResultSet lErgebnis = lPStm.executeQuery();
            lErgebnis.last();
            anzInArray = lErgebnis.getRow();
            lErgebnis.beforeFirst();

            ergebnisArray = new EclScan[anzInArray];

            int i = 0;
            while (lErgebnis.next() == true) {
                ergebnisArray[i] = this.decodeErgebnis(lErgebnis);
                i++;
            }
            lErgebnis.close();
            lPStm.close();

        } catch (Exception e) {
            CaBug.drucke("DbScan.read_scanVorgaenge 003");
            System.err.println(" " + e.getMessage());
            return (-1);
        }
        return (anzInArray);
    }

}
