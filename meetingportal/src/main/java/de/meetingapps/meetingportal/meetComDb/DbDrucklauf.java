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
import de.meetingapps.meetingportal.meetComEntities.EclDrucklauf;

public class DbDrucklauf  extends DbRootExecute  {

    private int logDrucken=10;
    
    private Connection verbindung = null;
    private DbBundle dbBundle = null;

    public EclDrucklauf ergebnisArray[] = null;

    /**Ggf. vor Benutzung setzen; anschließend unbedingt wieder auf true setzen!*/
    public boolean mandantenabhaengig=true;
    
    
    /**Liefert entweder dbBundle.getSchemaMandant() oder dbBundle.getSchemaAllgemein()*/
    private String getSchema() {
        if (mandantenabhaengig) {
            return dbBundle.getSchemaMandant();
        }
        else {
            return dbBundle.getSchemaAllgemein();
        }
    }

    /*************************Initialisierung***************************/
    public DbDrucklauf(DbBundle pDbBundle) {
        /* Verbindung in lokale Daten eintragen*/
        if (pDbBundle == null) {
            CaBug.drucke("DbDrucklauf.init 001 - dbBundle nicht initialisiert");
            return;
        }
        if (pDbBundle.dbBasis == null) {
            CaBug.drucke("DbDrucklauf.init 002 - dbBasis nicht initialisiert");
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
    public EclDrucklauf ergebnisPosition(int pN) {
        if (ergebnisArray == null) {
            CaBug.drucke("DbDrucklauf.ergebnisPosition 001");
            return null;
        }
        if (pN < 0) {
            CaBug.drucke("DbDrucklauf.ergebnisPosition 002");
            return null;
        }
        if (pN >= ergebnisArray.length) {
            CaBug.drucke("DbDrucklauf.ergebnisPosition 003");
            return null;
        }
        return ergebnisArray[pN];
    }

    /**************************deleteAll Löschen aller Datensätze  eines Mandanten****************/
    public int deleteAll() {
        if (mandantenabhaengig) {
        return dbBundle.dbLowLevel
                .deleteMandant("DELETE FROM " + getSchema() + "tbl_drucklauf where mandant=?;");
        }
        else {
            return dbBundle.dbLowLevel
                    .deleteAlle("DELETE FROM " + getSchema() + "tbl_drucklauf ;");
            
        }
    }

    public int updateMandant() {
        return dbBundle.dbLowLevel.rawUpdateMandant(dbBundle.getSchemaMandant() + "tbl_drucklauf");
    }

//    /*******Checken, ob table überhaupt vorhanden ist***************************/
//    public boolean checkTableVorhanden() {
//        DbLowLevel lDbLowLevel = new DbLowLevel(dbBundle);
//        return lDbLowLevel.checkTableVorhanden("SELECT * from tbl_drucklauf WHERE mandant=0; ");
//    }

    /************Neuanlegen Table******************************/
    public int createTable() {
        int rc = 0;
        DbLowLevel lDbLowLevel = new DbLowLevel(dbBundle);
        rc = lDbLowLevel.createTable("CREATE TABLE " + getSchema() + "tbl_drucklauf ( "
                + "`mandant` int(11) NOT NULL, " + "`drucklaufNr` int(11) NOT NULL, "
                + "`db_version` bigint(20) DEFAULT NULL, " + "`durchgefuehrtArbeitsplatzNr` int(11) DEFAULT NULL, "
                + "`durchgefuehrtBenutzerNr` int(11) DEFAULT NULL, " + "`erzeugtAm` char(19) DEFAULT NULL, "
                + "`drucklaufArt` int(11) DEFAULT NULL, " + "`drucklaufSubArt` int(11) DEFAULT '0', "
                + "`nurGepruefteVersandadressen` int(11) DEFAULT NULL, " + "`landSelektion` int(11) DEFAULT NULL, "
                + "`wegSelektion` int(11) DEFAULT NULL, " + "`gaesteOderAktionaereSelektion` int(11) DEFAULT NULL, "
                + "`anzahlSaetze` int(11) DEFAULT NULL, " + "`ersterAktionaer` char(20) DEFAULT NULL, "
                + "`letzterAktionaer` char(20) DEFAULT NULL, " + "`gedruckt` int(11) DEFAULT NULL, "
                + "PRIMARY KEY (`drucklaufNr`, `mandant`) " + ")  ");
        CaBug.druckeLog("DbDrucklauf create table rc=" + rc, logDrucken, 10);
        return rc;
    }

    /********** dekodiert die aktuelle Position aus ergebnis in EclAktienregisterEintrag und gibt dieses zurück******/
    EclDrucklauf decodeErgebnis(ResultSet pErgebnis) {

        EclDrucklauf lDrucklauf = new EclDrucklauf();

        try {
            lDrucklauf.mandant = pErgebnis.getInt("drl.mandant");
            lDrucklauf.drucklaufNr = pErgebnis.getInt("drl.drucklaufNr");
            lDrucklauf.db_version = pErgebnis.getLong("drl.db_version");

            lDrucklauf.durchgefuehrtArbeitsplatzNr = pErgebnis.getInt("drl.durchgefuehrtArbeitsplatzNr");
            lDrucklauf.durchgefuehrtBenutzerNr = pErgebnis.getInt("drl.durchgefuehrtBenutzerNr");

            lDrucklauf.erzeugtAm = pErgebnis.getString("drl.erzeugtAm");

            lDrucklauf.drucklaufArt = pErgebnis.getInt("drl.drucklaufArt");
            lDrucklauf.drucklaufSubArt = pErgebnis.getInt("drl.drucklaufSubArt");

            lDrucklauf.nurGepruefteVersandadressen = pErgebnis.getInt("drl.nurGepruefteVersandadressen");
            lDrucklauf.landSelektion = pErgebnis.getInt("drl.landSelektion");
            lDrucklauf.wegSelektion = pErgebnis.getInt("drl.wegSelektion");
            lDrucklauf.gaesteOderAktionaereSelektion = pErgebnis.getInt("drl.gaesteOderAktionaereSelektion");
            lDrucklauf.anzahlSaetze = pErgebnis.getInt("drl.anzahlSaetze");

            lDrucklauf.ersterAktionaer = pErgebnis.getString("drl.ersterAktionaer");
            lDrucklauf.letzterAktionaer = pErgebnis.getString("drl.letzterAktionaer");

            lDrucklauf.gedruckt = pErgebnis.getInt("drl.gedruckt");

        } catch (Exception e) {
            CaBug.drucke("DbDrucklauf.decodeErgebnis 001");
            System.err.println(" " + e.getMessage());
        }

        return lDrucklauf;
    }

    /********************* Fuellen Prepared Statement mit allen Feldern, beginnend bei offset.*****************
     * Kann sowohl für Insert, als auch für update verwendet werden.
     * 
     * offset= Startposition des ersten Feldes (also z.B. 1)
     */
    private int anzfelder = 16; /*Anpassen auf Anzahl der Felder pro Datensatz*/

    void fuellePreparedStatementKomplett(PreparedStatement pPStm, int pOffset, EclDrucklauf pDrucklauf) {

        int startOffset = pOffset; /*Nur erforderlich zum Überprüfen von Programmierfehlern - Feldanzahl*/

        try {
            pPStm.setInt(pOffset, pDrucklauf.mandant);
            pOffset++;
            pPStm.setInt(pOffset, pDrucklauf.drucklaufNr);
            pOffset++;
            pPStm.setLong(pOffset, pDrucklauf.db_version);
            pOffset++;

            pPStm.setInt(pOffset, pDrucklauf.durchgefuehrtArbeitsplatzNr);
            pOffset++;
            pPStm.setInt(pOffset, pDrucklauf.durchgefuehrtBenutzerNr);
            pOffset++;

            pPStm.setString(pOffset, pDrucklauf.erzeugtAm);
            pOffset++;

            pPStm.setInt(pOffset, pDrucklauf.drucklaufArt);
            pOffset++;
            pPStm.setInt(pOffset, pDrucklauf.drucklaufSubArt);
            pOffset++;

            pPStm.setInt(pOffset, pDrucklauf.nurGepruefteVersandadressen);
            pOffset++;
            pPStm.setInt(pOffset, pDrucklauf.landSelektion);
            pOffset++;
            pPStm.setInt(pOffset, pDrucklauf.wegSelektion);
            pOffset++;
            pPStm.setInt(pOffset, pDrucklauf.gaesteOderAktionaereSelektion);
            pOffset++;
            pPStm.setInt(pOffset, pDrucklauf.anzahlSaetze);
            pOffset++;

            pPStm.setString(pOffset, pDrucklauf.ersterAktionaer);
            pOffset++;
            pPStm.setString(pOffset, pDrucklauf.letzterAktionaer);
            pOffset++;

            pPStm.setInt(pOffset, pDrucklauf.gedruckt);
            pOffset++;

            if (pOffset - startOffset != anzfelder) {
                /*Nur wg. Überprüfung auf Programmierfehler - alle Felder berücksichtigt?*/
                CaBug.drucke("DbDrucklauf.fuellePreparedStatementKomplett 002");
            }

        } catch (SQLException e) {
            CaBug.drucke("DbDrucklauf.fuellePreparedStatementKomplett 001");
            e.printStackTrace();
        }

    }

    /**Insert
     * 
     * Feld drucklaufNr wird hier belegt
     * 
     * Returnwert:
     * =1 => Insert erfolgreich
     * ansonsten: Fehler
     */
    public int insert(EclDrucklauf pDrucklauf) {

        int erg = 0;

        /* neue InterneIdent vergeben */
        if (mandantenabhaengig) {
        erg = dbBundle.dbBasis.getInterneIdentDrucklauf();
        }
        else {
            erg = dbBundle.dbBasis.getInterneIdentDrucklaufAllgemein();
        }
        if (erg < 1) {
            CaBug.drucke("DbDrucklauf.insert 002");
            return (erg);
        }

        CaBug.druckeLog("DbDrucklauf erg=" + erg, logDrucken, 3);
        pDrucklauf.drucklaufNr = erg;
        if (mandantenabhaengig) {
            pDrucklauf.mandant = dbBundle.clGlobalVar.mandant;
        }

        try {
            /*Felder Neuanlage füllen*/
            String lSql = "INSERT INTO " + getSchema() + "tbl_drucklauf " + "("
                    + "mandant, drucklaufNr, db_version, durchgefuehrtArbeitsplatzNr, durchgefuehrtBenutzerNr,"
                    + "erzeugtAm, drucklaufArt, drucklaufSubArt, "
                    + "nurGepruefteVersandadressen, landSelektion, wegSelektion, gaesteOderAktionaereSelektion, anzahlSaetze, "
                    + "ersterAktionaer, letzterAktionaer, gedruckt " + ")" + "VALUES (" + "?, ?, ?, ?, ?, "
                    + "?, ?, ?, " + "?, ?, ?, ?, ?, " + "?, ?, ? " + ")";
            PreparedStatement lPStm = verbindung.prepareStatement(lSql, ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
            fuellePreparedStatementKomplett(lPStm, 1, pDrucklauf);
            erg = executeUpdate(lPStm);
            lPStm.close();
        } catch (Exception e2) {
            CaBug.drucke("DbDrucklauf.insert 001");
            System.err.println(" " + e2.getMessage());
        }

        if (erg == 0) {/*Fehler beim Einfügen - d.h. z.B. primaryKey bereits vorhanden*/
            return (-1);
        }

        return (1);
    }

    /**Lese alle Druckläufe
     * pSelektion==0 => alle Arten
     * pSelektion>0 => nur die mit drucklaufArt==pSelektion
     * */
    public int readAll(int pSelektion, int pDrucklaufSubArt) {
        int anzInArray = 0;
        PreparedStatement lPStm = null;
        String lSql="";
        try {
            if (mandantenabhaengig) {
            lSql = "SELECT * from " + getSchema() + "tbl_drucklauf drl WHERE mandant=? ";
            if (pSelektion > 0) {
                lSql = lSql + "AND drucklaufArt=" + Integer.toString(pSelektion) + " AND drucklaufSubArt="
                        + Integer.toString(pDrucklaufSubArt) + " ";
            }
            lSql = lSql + "ORDER BY drucklaufNr";
            lPStm = verbindung.prepareStatement(lSql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);

            lPStm.setInt(1, dbBundle.clGlobalVar.mandant);
            }
            
            else {
                lSql = "SELECT * from " + getSchema() + "tbl_drucklauf drl ";
                if (pSelektion > 0) {
                    lSql = lSql + "WHERE drucklaufArt=" + Integer.toString(pSelektion) + " AND drucklaufSubArt="
                            + Integer.toString(pDrucklaufSubArt) + " ";
                }
                lSql = lSql + "ORDER BY drucklaufNr";
                lPStm = verbindung.prepareStatement(lSql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);

            }

            ResultSet lErgebnis = executeQuery(lPStm);
            lErgebnis.last();
            anzInArray = lErgebnis.getRow();
            lErgebnis.beforeFirst();

            ergebnisArray = new EclDrucklauf[anzInArray];

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

    /**Einlesen einer bestimmten Aufgabe mit  ident*/
    public int read(int ident) {
        int anzInArray = 0;
        PreparedStatement lPStm = null;

        try {
            String lSql = "SELECT * from " + getSchema()
                    + "tbl_drucklauf drl WHERE drucklaufNr=? ;";
            lPStm = verbindung.prepareStatement(lSql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            lPStm.setInt(1, ident);

            ResultSet lErgebnis = executeQuery(lPStm);
            lErgebnis.last();
            anzInArray = lErgebnis.getRow();
            lErgebnis.beforeFirst();

            ergebnisArray = new EclDrucklauf[anzInArray];

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

    /**Update. Versionsnummer wird um 1 hochgezählt
     * 
     * Feld mandant wird von dieser Funktion nicht verändert.
     * 
     * Zum Sicherstellen der Multiuserfähigkeit muß in jedem Fall der rc CaFehler.pfXyWurdeVonAnderemBenutzerVeraendert
     * nach Aufruf dieser Funktion abgefangen werden.
     * Ansonsten: rc=1 => ok, ansonsten Fehler
     */
    public int update(EclDrucklauf pAufgabe) {

        pAufgabe.db_version++;

        try {
            String lSql = "UPDATE " + getSchema() + "tbl_drucklauf SET "
                    + "mandant=?, drucklaufNr=?, db_version=?, durchgefuehrtArbeitsplatzNr=?, durchgefuehrtBenutzerNr=?,"
                    + "erzeugtAm=?, drucklaufArt=?, drucklaufSubArt=?, "
                    + "nurGepruefteVersandadressen=?, landSelektion=?, wegSelektion=?, gaesteOderAktionaereSelektion=?, anzahlSaetze=?, "
                    + "ersterAktionaer=?, letzterAktionaer=?, gedruckt=? " + "WHERE "
                    + "db_version=? AND drucklaufNr=? ";

            PreparedStatement lPStm = verbindung.prepareStatement(lSql);
            fuellePreparedStatementKomplett(lPStm, 1, pAufgabe);
            lPStm.setLong(anzfelder + 1, pAufgabe.db_version - 1);
            lPStm.setInt(anzfelder + 2, pAufgabe.drucklaufNr);

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

    public int delete(int pIdentAufgabe) {

        try {

            String sql = "DELETE FROM " + getSchema()
                    + "tbl_drucklauf WHERE drucklaufNr=? ";

            PreparedStatement pstm1 = verbindung.prepareStatement(sql);
            pstm1.setInt(1, pIdentAufgabe);

            int ergebnis1 = executeUpdate(pstm1);
            pstm1.close();
            if (ergebnis1 == 0) {
                return (CaFehler.pfXyWurdeVonAnderemBenutzerVeraendert);
            }
        } catch (Exception e1) {
            CaBug.drucke("001");
            System.err.println(" " + e1.getMessage());
            return (CaFehler.pfdXyBereitsVorhanden);
        }

        return (1);
    }

}
