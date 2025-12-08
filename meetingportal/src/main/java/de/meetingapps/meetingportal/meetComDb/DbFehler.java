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
import de.meetingapps.meetingportal.meetComEntities.EclFehler;

/**Hinweis: da teilweise Mandantenübergreifend, und Zugriff auch über Puffer möglich, läuft in dieser Db-Klasse einiges
 * anders als in den Standard-Db-Klassen! Bitte Hinweise zu den einzelnen Funktionen beachten!
 */

/*TODO _AppTexte: auch DbFehler überprüfen - derzeit noch alles NICHT im Mandantenschema!*/
public class DbFehler  extends DbRootExecute {

    private int logDrucken=10;
    
    private Connection verbindung = null;
    private DbBasis dbBasis = null;
    private DbBundle dbBundle = null;

    public EclFehler ergebnisArray[] = null;

    /*************************Initialisierung***************************/
    public DbFehler(DbBundle pDbBundle) {
        /* Verbindung in lokale Daten eintragen*/
        if (pDbBundle == null) {
            CaBug.drucke("DbFehler.init 001 - dbBundle nicht initialisiert");
            return;
        }
        if (pDbBundle.dbBasis == null) {
            CaBug.drucke("DbFehler.init 002 - dbBasis nicht initialisiert");
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
    public EclFehler ergebnisPosition(int pN) {
        if (ergebnisArray == null) {
            CaBug.drucke("DbFehler.ergebnisPosition 001");
            return null;
        }
        if (pN < 0) {
            CaBug.drucke("DbFehler.ergebnisPosition 002");
            return null;
        }
        if (pN >= ergebnisArray.length) {
            CaBug.drucke("DbFehler.ergebnisPosition 003");
            return null;
        }
        return ergebnisArray[pN];
    }

    public int createTable() {
        int rc = 0;
        DbLowLevel lDbLowLevel = new DbLowLevel(dbBundle);
        rc = lDbLowLevel.createTable("CREATE TABLE " + dbBundle.getSchemaAllgemein() + "tbl_fehler ( "
                + "`mandant` int(11) NOT NULL, " + "`basisSet` int(11) NOT NULL, " + "`ident` int(11) NOT NULL, "
                + "`kuerzel` char(70) DEFAULT NULL, " + "`db_version` bigint(20) DEFAULT NULL, "
                + "`sprache` int(11) NOT NULL, " + "`fehlermeldung` varchar(400) DEFAULT NULL, "
                + "`selektionPortal` int(11) DEFAULT NULL, " + "`selektionApp` int(11) DEFAULT NULL, "
                + "PRIMARY KEY (`ident`,`basisSet`,`mandant`,`sprache`) " + ") ");
        return rc;
    }

    /********** dekodiert die aktuelle Position aus ergebnis und gibt dieses zurück******/
    private EclFehler decodeErgebnis(ResultSet pErgebnis) {

        EclFehler lFehler = new EclFehler();

        try {

            lFehler.mandant = pErgebnis.getInt("mandant");
            lFehler.basisSet = pErgebnis.getInt("basisSet");
            lFehler.ident = pErgebnis.getInt("ident");
            lFehler.kuerzel = pErgebnis.getString("kuerzel");
            lFehler.db_version = pErgebnis.getLong("db_version");

            lFehler.sprache = pErgebnis.getInt("sprache");

            lFehler.fehlermeldung = pErgebnis.getString("fehlermeldung");
            lFehler.selektionPortal = pErgebnis.getInt("selektionPortal");
            lFehler.selektionApp = pErgebnis.getInt("selektionApp");

        } catch (Exception e) {
            CaBug.drucke("DbFehler.decodeErgebnis 001");
            System.err.println(" " + e.getMessage());
        }

        return lFehler;
    }

    /********************* Fuellen Prepared Statement mit allen Feldern, beginnend bei offset.*****************
     * Kann sowohl für Insert, als auch für update verwendet werden.
     * 
     * offset= Startposition des ersten Feldes (also z.B. 1)
     */
    private int anzfelder = 9; /*Anpassen auf Anzahl der Felder pro Datensatz*/

    private void fuellePreparedStatementKomplett(PreparedStatement pPStm, int pOffset, EclFehler pParameter) {

        int startOffset = pOffset; /*Nur erforderlich zum Überprüfen von Programmierfehlern - Feldanzahl*/

        try {
            pPStm.setInt(pOffset, pParameter.mandant);
            pOffset++;
            pPStm.setInt(pOffset, pParameter.basisSet);
            pOffset++;
            pPStm.setInt(pOffset, pParameter.ident);
            pOffset++;
            pPStm.setString(pOffset, pParameter.kuerzel);
            pOffset++;
            pPStm.setLong(pOffset, pParameter.db_version);
            pOffset++;

            pPStm.setInt(pOffset, pParameter.sprache);
            pOffset++;
            pPStm.setString(pOffset, pParameter.fehlermeldung);
            pOffset++;

            pPStm.setInt(pOffset, pParameter.selektionPortal);
            pOffset++;
            pPStm.setInt(pOffset, pParameter.selektionApp);
            pOffset++;

            if (pOffset - startOffset != anzfelder) {
                /*Nur wg. Überprüfung auf Programmierfehler - alle Felder berücksichtigt?*/
                CaBug.drucke("DbFehler.fuellePreparedStatementKomplett 002");
            }

        } catch (SQLException e) {
            CaBug.drucke("DbFehler.fuellePreparedStatementKomplett 001");
            e.printStackTrace();
        }

    }

    /**Insert
     * 
     * Feld mandant wird von dieser Funktion immer selbstständig belegt.
     * 
     * Achtung: "ident" muß vorbelegt sein!
     * Returnwert:
     * =1 => Insert erfolgreich
     * ansonsten: Fehler
     */
    public int insert(EclFehler pParameter) {

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
            String lSql = "INSERT INTO " + dbBundle.getSchemaAllgemein() + "tbl_fehler " + "("
                    + "mandant, basisSet, ident, kuerzel, db_version, sprache, fehlermeldung, "
                    + "selektionPortal, selektionApp " + ")" + "VALUES (" + "?, ?, ?, ?, ?, ?, ?,  " + "?, ? "
                    + ")";
            PreparedStatement lPStm = verbindung.prepareStatement(lSql, ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);

            fuellePreparedStatementKomplett(lPStm, 1, pParameter);

            erg = executeUpdate(lPStm);
            CaBug.druckeLog("erg="+erg, logDrucken, 10);
            lPStm.close();
        } catch (Exception e2) {
            CaBug.drucke("DbFehler.insert 001");
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

    /**Realisierte Selektion derzeit, jeweils einzeln: 
     * basisSet + ident + mandant (!!!)
     * 
     * Return-Wert:
     * >=1 => Anzahl der gefundenen Datensätze
     * =0 => nix gefunden
     * <0 => Fehler
     * */
    public int read(EclFehler lFehler) {
        int anzInArray = 0;
        PreparedStatement lPStm = null;
        int gefSelect = 0; /*Nur zum Aufdecken von Programmierfehlern - in diesem Fall: unvollständige Parameterübergabe*/

        if (lFehler == null) {
            CaBug.drucke("DbFehler.read 001");
            return -1;
        }

        try {
            if (lFehler.ident != 0) {
                gefSelect = 1;
                String lSql = "SELECT * from " + dbBundle.getSchemaAllgemein() + "tbl_fehler where " + "mandant=? AND "
                        + "basisSet=? AND ident=? ORDER BY ident;";
                lPStm = verbindung.prepareStatement(lSql, ResultSet.TYPE_SCROLL_INSENSITIVE,
                        ResultSet.CONCUR_READ_ONLY);
                lPStm.setInt(1, lFehler.mandant);
                lPStm.setInt(2, lFehler.basisSet);
                lPStm.setInt(3, lFehler.ident);
            }

            if (gefSelect == 0) {
                CaBug.drucke("DbFehler.read 002");
                return -1;
            }

            ResultSet lErgebnis = executeQuery(lPStm);
            lErgebnis.last();
            anzInArray = lErgebnis.getRow();
            lErgebnis.beforeFirst();

            ergebnisArray = new EclFehler[anzInArray];

            int i = 0;
            while (lErgebnis.next() == true) {

                ergebnisArray[i] = this.decodeErgebnis(lErgebnis);
                i++;

            }
            lErgebnis.close();
            lPStm.close();

        } catch (Exception e) {
            CaBug.drucke("DbFehler.read 003");
            System.err.println(" " + e.getMessage());
            return (-1);
        }

        return (anzInArray);
    }

    /**Liest alle zur Verfügung stehende Fehler für diesen Mandanten und diese Sprache, aufsteigend sortiert nach position.
     * 
     * Return-Wert:
     * >=1 => Anzahl der gefundenen Datensätze
     * =0 => nix gefunden
     * <0 => Fehler
     * */
    public int read_all(String sprache) {
        return read_allSelekt(sprache, 1, -1);
    }

    public int read_allNurApp(String sprache) {
        return read_allSelekt(sprache, 2, -1);
    }

    /**1=alle
     * 2=nur App
     * 
     * pBasisSet=-1 => BasisSet aus Parameter übernehmen
     */
    private int read_allSelekt(String sprache, int selektion, int pBasisSet) {
        int lSprache = 0;
        if (sprache.compareTo("DE") == 0) {
            lSprache = 1;
        }
        if (sprache.compareTo("EN") == 0) {
            lSprache = 2;
        }
        int anzInArray = 0;
        PreparedStatement lPStm = null;

        int basisSet = pBasisSet;
        if (basisSet == -1) {
            basisSet = dbBundle.param.paramPortal.basisSetStandardTexteVerwenden;
        }

        try {
            String lSql = "SELECT * from " + dbBundle.getSchemaAllgemein() + "tbl_fehler where "
                    + "(mandant=? OR mandant=0) AND (sprache=? OR sprache=0) AND basisSet=? ";
            if (selektion == 2) {
                lSql = lSql + "AND selektionApp=1 ";
            }
            lSql = lSql + "ORDER BY ident, sprache, mandant;";
            lPStm = verbindung.prepareStatement(lSql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            lPStm.setInt(1, dbBundle.clGlobalVar.mandant);
            lPStm.setInt(2, lSprache);
            lPStm.setInt(3, basisSet);

            ResultSet lErgebnis = executeQuery(lPStm);
            lErgebnis.last();
            anzInArray = lErgebnis.getRow();
            lErgebnis.beforeFirst();

            ergebnisArray = new EclFehler[anzInArray];

            int i = 0;
            while (lErgebnis.next() == true) {

                ergebnisArray[i] = this.decodeErgebnis(lErgebnis);
                i++;

            }
            lErgebnis.close();
            lPStm.close();

        } catch (Exception e) {
            CaBug.drucke("DbFehler.read_all 003");
            System.err.println(" " + e.getMessage());
            return (-1);
        }

        return (anzInArray);
    }

    /**Update. Versionsnummer wird um 1 hochgezählt
     * 
     * Feld mandant muß vor dieser Funktion belegt werden!!
     * 
     * Zum Sicherstellen der Multiuserfähigkeit muß in jedem Fall der rc CaFehler.pfXyWurdeVonAnderemBenutzerVeraendert
     * nach Aufruf dieser Funktion abgefangen werden.
     * Ansonsten: rc=1 => ok, ansonsten Fehler
     */
    public int update(EclFehler pParameter) {

        pParameter.db_version++;

        try {

            String lSql = "UPDATE " + dbBundle.getSchemaAllgemein() + "tbl_fehler SET "
                    + "mandant=?, basisSet=?, ident=?, kuerzel=?, db_version=?, " + "sprache=?, fehlermeldung=?, "
                    + "selektionPortal=?, selektionApp=? " + "WHERE " + "ident=? AND basisSet=? AND "
                    + "db_version=? AND mandant=? AND sprache=?";

            PreparedStatement lPStm = verbindung.prepareStatement(lSql);
            fuellePreparedStatementKomplett(lPStm, 1, pParameter);
            lPStm.setInt(anzfelder + 1, pParameter.ident);
            lPStm.setInt(anzfelder + 2, pParameter.basisSet);
            lPStm.setLong(anzfelder + 3, pParameter.db_version - 1);
            lPStm.setLong(anzfelder + 4, pParameter.mandant);
            lPStm.setInt(anzfelder + 5, pParameter.sprache);

            int ergebnis = executeUpdate(lPStm);
            lPStm.close();
            if (ergebnis == 0) {
                return (CaFehler.pfXyWurdeVonAnderemBenutzerVeraendert);
            }
        } catch (Exception e1) {
            CaBug.drucke("DbFehler.update 001");
            System.err.println(" " + e1.getMessage());
            return (CaFehler.pfdXyBereitsVorhanden);
        }

        return (1);
    }
    
    /**Gefüllt muß sein: mandant, ident, basisSet.
     * Löscht die Einträge aller Sprachen dazu*/
    public int delete(EclFehler pParameter) {
        CaBug.druckeLog("", logDrucken, 10);
        
        try {

            String lSql = "DELETE FROM " + dbBundle.getSchemaAllgemein() + "tbl_fehler "
                    +" WHERE " + "ident=? AND basisSet=? AND "
                    + " mandant=? ";

            PreparedStatement lPStm = verbindung.prepareStatement(lSql);
            lPStm.setInt(1, pParameter.ident);
            lPStm.setInt(2, pParameter.basisSet);
            lPStm.setInt(3, pParameter.mandant);

            int ergebnis = executeUpdate(lPStm);
            CaBug.druckeLog("ergebnis="+ergebnis, logDrucken, 10);
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

}
