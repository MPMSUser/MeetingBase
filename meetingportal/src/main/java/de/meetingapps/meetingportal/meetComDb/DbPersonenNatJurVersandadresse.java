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

/**Hinweis: diese Klasse kann als "Musterklasse" / Blaupause für andere Db-Zugriffsklassen verwendet werden*/

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import de.meetingapps.meetingportal.meetComAllg.CaBug;
import de.meetingapps.meetingportal.meetComAllg.CaFehler;
import de.meetingapps.meetingportal.meetComAllg.CaString;
import de.meetingapps.meetingportal.meetComEntities.EclPersonenNatJurVersandadresse;

public class DbPersonenNatJurVersandadresse {

    private Connection verbindung = null;
    private DbBasis dbBasis = null;
    private DbBundle dbBundle = null;

    private EclPersonenNatJurVersandadresse ergebnisArray[] = null;

    /*************************Initialisierung***************************/
    public DbPersonenNatJurVersandadresse(DbBundle pDbBundle) {
        /* Verbindung in lokale Daten eintragen*/
        if (pDbBundle == null) {
            CaBug.drucke("DbPersonenNatJurVersandadresse.init 001 - dbBundle nicht initialisiert");
            return;
        }
        if (pDbBundle.dbBasis == null) {
            CaBug.drucke("DbPersonenNatJurVersandadresse.init 002 - dbBasis nicht initialisiert");
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
    public EclPersonenNatJurVersandadresse ergebnisPosition(int pN) {
        if (ergebnisArray == null) {
            CaBug.drucke("DbPersonenNatJurVersandadresse.ergebnisPosition 001");
            return null;
        }
        if (pN < 0) {
            CaBug.drucke("DbPersonenNatJurVersandadresse.ergebnisPosition 002");
            return null;
        }
        if (pN >= ergebnisArray.length) {
            CaBug.drucke("DbPersonenNatJurVersandadresse.ergebnisPosition 003");
            return null;
        }
        return ergebnisArray[pN];
    }

    public int createTable() {
        int rc = 0;
        DbLowLevel lDbLowLevel = new DbLowLevel(dbBundle);
        rc = lDbLowLevel.createTable("CREATE TABLE " + dbBundle.getSchemaMandant()
                + "tbl_personennatjurversandadresse ( " + "`mandant` int(11) NOT NULL, " + "`ident` int(11) NOT NULL, "
                + "`db_version` bigint(20) DEFAULT NULL, " + "`identPersonenNatJur` int(11) DEFAULT NULL, "
                + "`versandAbweichend` int(11) DEFAULT NULL, " + "`anredeIdVersand` int(11) DEFAULT '0', "
                + "`titelVersand` varchar(80) DEFAULT ' ', " + "`name3Versand` varchar(80) DEFAULT ' ', "
                + "`name2Versand` varchar(80) DEFAULT ' ', " + "`nameVersand` varchar(80) DEFAULT NULL, "
                + "`vornameVersand` varchar(80) DEFAULT NULL, " + "`strasseVersand` varchar(80) DEFAULT NULL, "
                + "`postleitzahlVersand` varchar(10) DEFAULT NULL, " + "`ortVersand` varchar(80) DEFAULT NULL, "
                + "`staatIdVersand` int(11) DEFAULT NULL, " + "PRIMARY KEY (`mandant`,`ident`) " + ") ");
        return rc;
    }

    /**************************deleteAll Löschen aller Datensätze  eines Mandanten****************/
    public int deleteAll() {
        dbBasis.resetInterneIdentPersonenNatJurVersandadresse();
        return dbBundle.dbLowLevel.deleteMandant(
                "DELETE FROM " + dbBundle.getSchemaMandant() + "tbl_personenNatJurVersandadresse where mandant=?;");
    }

    /**************************setzt aktuelle Mandantennummer bei allen Datensätzen****************/
    public int updateMandant() {
        return dbBundle.dbLowLevel.rawUpdateMandant(dbBundle.getSchemaMandant() + "tbl_personenNatJurVersandadresse");
    }

    public void reorgInterneIdent() {
        int lMax = dbBundle.dbLowLevel.liefereHoechsteIdent("SELECT MAX(ident) FROM " + dbBundle.getSchemaMandant()
                + "tbl_personenNatJurVersandadresse ab where ab.mandant=? ");
        if (lMax != -1) {
            dbBundle.dbBasis.resetInterneIdentPersonenNatJurVersandadresse(lMax);
        }
    }

    /********** dekodiert die aktuelle Position aus ergebnis in EclPersonenNatJurVersandadresse und gibt dieses zurück******/
    EclPersonenNatJurVersandadresse decodeErgebnis(ResultSet pErgebnis) {

        EclPersonenNatJurVersandadresse lPersonenNatJurVersandadresse = new EclPersonenNatJurVersandadresse();

        try {

            lPersonenNatJurVersandadresse.mandant = pErgebnis.getInt("pv.mandant");
            lPersonenNatJurVersandadresse.ident = pErgebnis.getInt("pv.ident");
            lPersonenNatJurVersandadresse.db_version = pErgebnis.getLong("pv.db_version");

            lPersonenNatJurVersandadresse.identPersonenNatJur = pErgebnis.getInt("pv.identPersonenNatJur");
            lPersonenNatJurVersandadresse.versandAbweichend = pErgebnis.getInt("pv.versandAbweichend");

            lPersonenNatJurVersandadresse.anredeIdVersand = pErgebnis.getInt("pv.anredeIdVersand");
            lPersonenNatJurVersandadresse.titelVersand = pErgebnis.getString("pv.titelVersand");
            lPersonenNatJurVersandadresse.name3Versand = pErgebnis.getString("pv.name3Versand");
            lPersonenNatJurVersandadresse.name2Versand = pErgebnis.getString("pv.name2Versand");

            lPersonenNatJurVersandadresse.nameVersand = pErgebnis.getString("pv.nameVersand");
            lPersonenNatJurVersandadresse.vornameVersand = pErgebnis.getString("pv.vornameVersand");
            lPersonenNatJurVersandadresse.strasseVersand = pErgebnis.getString("pv.strasseVersand");
            lPersonenNatJurVersandadresse.postleitzahlVersand = pErgebnis.getString("pv.postleitzahlVersand");
            lPersonenNatJurVersandadresse.ortVersand = pErgebnis.getString("pv.ortVersand");
            lPersonenNatJurVersandadresse.staatIdVersand = pErgebnis.getInt("pv.staatIdVersand");

        } catch (Exception e) {
            CaBug.drucke("DbPersonenNatJurVersandadresse.decodeErgebnis 001");
            System.err.println(" " + e.getMessage());
        }

        return lPersonenNatJurVersandadresse;
    }

    /********************* Fuellen Prepared Statement mit allen Feldern, beginnend bei offset.*****************
     * Kann sowohl für Insert, als auch für update verwendet werden.
     * 
     * offset= Startposition des ersten Feldes (also z.B. 1)
     */
    private int anzfelder = 15; /*Anpassen auf Anzahl der Felder pro Datensatz*/

    private void fuellePreparedStatementKomplett(PreparedStatement pPStm, int pOffset,
            EclPersonenNatJurVersandadresse pPersonenNatJurVersandadresse) {

        int startOffset = pOffset; /*Nur erforderlich zum Überprüfen von Programmierfehlern - Feldanzahl*/

        try {
            pPStm.setInt(pOffset, pPersonenNatJurVersandadresse.mandant);
            pOffset++;
            pPStm.setInt(pOffset, pPersonenNatJurVersandadresse.ident);
            pOffset++;
            pPStm.setLong(pOffset, pPersonenNatJurVersandadresse.db_version);
            pOffset++;

            pPStm.setInt(pOffset, pPersonenNatJurVersandadresse.identPersonenNatJur);
            pOffset++;
            pPStm.setInt(pOffset, pPersonenNatJurVersandadresse.versandAbweichend);
            pOffset++;

            pPStm.setInt(pOffset, pPersonenNatJurVersandadresse.anredeIdVersand);
            pOffset++;
            pPStm.setString(pOffset, CaString.trunc(pPersonenNatJurVersandadresse.titelVersand, 80));
            pOffset++;
            pPStm.setString(pOffset, CaString.trunc(pPersonenNatJurVersandadresse.name3Versand, 80));
            pOffset++;
            pPStm.setString(pOffset, CaString.trunc(pPersonenNatJurVersandadresse.name2Versand, 80));
            pOffset++;

            pPStm.setString(pOffset, CaString.trunc(pPersonenNatJurVersandadresse.nameVersand, 80));
            pOffset++;
            pPStm.setString(pOffset, CaString.trunc(pPersonenNatJurVersandadresse.vornameVersand, 80));
            pOffset++;
            pPStm.setString(pOffset, CaString.trunc(pPersonenNatJurVersandadresse.strasseVersand, 80));
            pOffset++;
            pPStm.setString(pOffset, CaString.trunc(pPersonenNatJurVersandadresse.postleitzahlVersand, 10));
            pOffset++;
            pPStm.setString(pOffset, CaString.trunc(pPersonenNatJurVersandadresse.ortVersand, 80));
            pOffset++;
            pPStm.setInt(pOffset, pPersonenNatJurVersandadresse.staatIdVersand);
            pOffset++;

            if (pOffset - startOffset != anzfelder) {
                /*Nur wg. Überprüfung auf Programmierfehler - alle Felder berücksichtigt?*/
                CaBug.drucke("DbPersonenNatJurVersandadresse.fuellePreparedStatementKomplett 002");
            }

        } catch (SQLException e) {
            CaBug.drucke("DbPersonenNatJurVersandadresse.fuellePreparedStatementKomplett 001");
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
    public int insert(EclPersonenNatJurVersandadresse pPersonenNatJurVersandadresse) {

        int erg = 0;

        /* Start Transaktion */
        dbBasis.beginTransaction();

        /* neue InterneIdent vergeben */
        erg = dbBasis.getInterneIdentPersonenNatJurVersandadresse();
        if (erg < 1) {
            CaBug.drucke("DbPersonenNatJurVersandadresse.insert 002");
            dbBasis.rollbackTransaction();
            dbBasis.endTransaction();
            return (erg);
        }

        pPersonenNatJurVersandadresse.ident = erg;

        pPersonenNatJurVersandadresse.mandant = dbBundle.clGlobalVar.mandant;

        /* Satz einfügen: 
         * Verarbeitungshinweis: 
         * 	>	nachdem InterneIdent immer eindeutig vergeben werden, ist prinzipiell eine "Doppeleinfügung"
         * 		von InterneIdent nicht möglich. Sollte es dazu kommen, ist das immer ein Programmfehler!
         */

        try {

            /*Felder Neuanlage füllen*/
            String lSql = "INSERT INTO " + dbBundle.getSchemaMandant() + "tbl_personenNatJurVersandadresse " + "("
                    + "mandant, ident, db_version, " + "identPersonenNatJur, versandAbweichend, anredeIdVersand, "
                    + "titelVersand, name3Versand, name2Versand, nameVersand, vornameVersand, strasseVersand, "
                    + "postleitzahlVersand, ortVersand, staatIdVersand " + ")" + "VALUES (" + "?, ?, ?, " + "?, ?, ?, "
                    + "?, ?, ?, ?, ?, ?, " + "?, ?, ? " + ")";
            PreparedStatement lPStm = verbindung.prepareStatement(lSql, ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);

            fuellePreparedStatementKomplett(lPStm, 1, pPersonenNatJurVersandadresse);

            erg = lPStm.executeUpdate();

            lPStm.close();
        } catch (Exception e2) {
            CaBug.drucke("DbPersonenNatJurVersandadresse.insert 001");
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

    /**Überprüfen, ob insert möglich ist (intern: es wird ein read-Befehl abgesetzt), oder mittlerweile bereits ein 
     * Satz vorhanden ist (dient dem Multiuser-Betrieb ...)
     * Gefüllt sein muss: der Primary Key (in diesem Fall: aktienregisterIdent; mandant wird automatisch gefüllt)
     * 
     * true=insert ist noch möglich
     * false=mittlerweile existiert ein Satz mit diesem Primary Key, oder es sit ein Fehler aufgetreten
     * */
    public boolean insertCheck(EclPersonenNatJurVersandadresse pPersonenNatJurVersandadresse) {
        int anzInArray = 0;
        PreparedStatement lPStm = null;

        if (pPersonenNatJurVersandadresse == null) {
            CaBug.drucke("DbPersonenNatJurVersandadresse.insertCheck 001");
            return false;
        }

        try {
            String lSql = "SELECT pv.* from " + dbBundle.getSchemaMandant()
                    + "tbl_personenNatJurVersandadresse pv where " + "pv.mandant=? AND "
                    + "pv.ident=? ORDER BY pv.ident;";
            lPStm = verbindung.prepareStatement(lSql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            lPStm.setInt(1, dbBundle.clGlobalVar.mandant);
            lPStm.setInt(2, pPersonenNatJurVersandadresse.ident);

            ResultSet lErgebnis = lPStm.executeQuery();
            lErgebnis.last();
            anzInArray = lErgebnis.getRow();
            lErgebnis.beforeFirst();

            lErgebnis.close();
            lPStm.close();
            if (anzInArray > 0) {
                return false;
            } else {
                return true;
            }

        } catch (Exception e) {
            CaBug.drucke("DbPersonenNatJurVersandadresse.insertCheck 003");
            System.err.println(" " + e.getMessage());
            return (false);
        }

    }

    /**Realisierte Selektion derzeit, jeweils einzeln: 
     * ident
     * identPersonenNatJur,
     * 
     * Return-Wert:
     * >=1 => Anzahl der gefundenen Datensätze
     * =0 => nix gefunden
     * <0 => Fehler
     * */
    public int read(EclPersonenNatJurVersandadresse pPersonenNatJurVersandadresse) {
        int anzInArray = 0;
        PreparedStatement lPStm = null;
        int gefSelect = 0; /*Nur zum Aufdecken von Programmierfehlern - in diesem Fall: unvollständige Parameterübergabe*/

        if (pPersonenNatJurVersandadresse == null) {
            CaBug.drucke("DbPersonenNatJurVersandadresse.read 001");
            return -1;
        }

        try {
            if (pPersonenNatJurVersandadresse.ident != 0) {
                gefSelect = 1;
                String lSql = "SELECT pv.* from " + dbBundle.getSchemaMandant()
                        + "tbl_personenNatJurVersandadresse pv where " + "pv.mandant=? AND "
                        + "pv.ident=? ORDER BY pv.ident;";
                lPStm = verbindung.prepareStatement(lSql, ResultSet.TYPE_SCROLL_INSENSITIVE,
                        ResultSet.CONCUR_READ_ONLY);
                lPStm.setInt(1, dbBundle.clGlobalVar.mandant);
                lPStm.setInt(2, pPersonenNatJurVersandadresse.ident);
            }
            if (pPersonenNatJurVersandadresse.identPersonenNatJur != 0) {
                gefSelect = 2;
                String lSql = "SELECT pv.* from " + dbBundle.getSchemaMandant()
                        + "tbl_personenNatJurVersandadresse pv where " + "pv.mandant=? AND "
                        + "pv.identPersonenNatJur=? ORDER BY pv.identPersonenNatJur;";
                lPStm = verbindung.prepareStatement(lSql, ResultSet.TYPE_SCROLL_INSENSITIVE,
                        ResultSet.CONCUR_READ_ONLY);
                lPStm.setInt(1, dbBundle.clGlobalVar.mandant);
                lPStm.setInt(2, pPersonenNatJurVersandadresse.identPersonenNatJur);
            }

            if (gefSelect == 0) {
                CaBug.drucke("DbPersonenNatJurVersandadresse.read 002");
                return -1;
            }

            ResultSet lErgebnis = lPStm.executeQuery();
            lErgebnis.last();
            anzInArray = lErgebnis.getRow();
            lErgebnis.beforeFirst();

            ergebnisArray = new EclPersonenNatJurVersandadresse[anzInArray];

            int i = 0;
            while (lErgebnis.next() == true) {

                ergebnisArray[i] = this.decodeErgebnis(lErgebnis);
                i++;
            }
            lErgebnis.close();
            lPStm.close();

        } catch (Exception e) {
            CaBug.drucke("DbPersonenNatJurVersandadresse.read 003");
            System.err.println(" " + e.getMessage());
            return (-1);
        }
        return (anzInArray);
    }

    /**Update. Versionsnummer wird um 1 hochgezählt
     * 
     * Feld mandant wird von dieser Funktion immer selbstständig belegt.
     * 
     * Zum Sicherstellen der Multiuserfähigkeit muß in jedem Fall der rc CaFehler.pfXyWurdeVonAnderemBenutzerVeraendert
     * nach Aufruf dieser Funktion abgefangen werden.
     * Ansonsten: rc=1 => ok, ansonsten Fehler
     */
    public int update(EclPersonenNatJurVersandadresse pPersonenNatJurVersandadresse) {

        pPersonenNatJurVersandadresse.db_version++;

        try {

            String lSql = "UPDATE " + dbBundle.getSchemaMandant() + "tbl_personenNatJurVersandadresse SET "
                    + "mandant=?, ident=?, db_version=?, "
                    + "identPersonenNatJur=?, versandAbweichend=?, anredeIdVersand=?, "
                    + "titelVersand=?, name3Versand=?, name2Versand=?, nameVersand=?, vornameVersand=?, strasseVersand=?, "
                    + "postleitzahlVersand=?, ortVersand=?, staatIdVersand=? " + "WHERE " + "ident=? AND "
                    + "db_version=? AND mandant=?";

            PreparedStatement lPStm = verbindung.prepareStatement(lSql);
            fuellePreparedStatementKomplett(lPStm, 1, pPersonenNatJurVersandadresse);
            lPStm.setInt(anzfelder + 1, pPersonenNatJurVersandadresse.ident);
            lPStm.setLong(anzfelder + 2, pPersonenNatJurVersandadresse.db_version - 1);
            lPStm.setLong(anzfelder + 3, dbBundle.clGlobalVar.mandant);

            int ergebnis = lPStm.executeUpdate();
            lPStm.close();
            if (ergebnis == 0) {
                return (CaFehler.pfXyWurdeVonAnderemBenutzerVeraendert);
            }
        } catch (Exception e1) {
            CaBug.drucke("DbPersonenNatJurVersandadresse.update 001");
            System.err.println(" " + e1.getMessage());
            return (CaFehler.pfdXyBereitsVorhanden);
        }

        return (1);
    }

    /**Überprüfen, ob update möglich ist (intern: es wird ein read-Befehl abgesetzt), oder mittlerweile bereits der 
     * Satz geändert wurde (dient dem Multiuser-Betrieb ...)
     * Gefüllt sein muss: der Primary Key (in diesem Fall: aktienregisterIdent; mandant wird automatisch gefüllt),
     * sowie db_version
     * 
     * true=update ist noch möglich
     * false=mittlerweile wurde der Satz verändert, oder es ist ein Fehler aufgetreten
     * */
    public boolean updateCheck(EclPersonenNatJurVersandadresse pPersonenNatJurVersandadresse) {
        int anzInArray = 0;
        PreparedStatement lPStm = null;

        if (pPersonenNatJurVersandadresse == null) {
            CaBug.drucke("DbPersonenNatJurVersandadresse.updateCheck 001");
            return false;
        }

        try {
            String lSql = "SELECT pv.* from " + dbBundle.getSchemaMandant()
                    + "tbl_personenNatJurVersandadresse pv where " + "pv.mandant=? AND "
                    + "pv.ident=? and pv.db_version=? ORDER BY pv.ident;";
            lPStm = verbindung.prepareStatement(lSql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            lPStm.setInt(1, dbBundle.clGlobalVar.mandant);
            lPStm.setInt(2, pPersonenNatJurVersandadresse.ident);
            lPStm.setLong(3, pPersonenNatJurVersandadresse.db_version);

            ResultSet lErgebnis = lPStm.executeQuery();
            lErgebnis.last();
            anzInArray = lErgebnis.getRow();
            lErgebnis.beforeFirst();

            lErgebnis.close();
            lPStm.close();

            if (anzInArray > 0) {
                return true;
            } else {
                return false;
            }

        } catch (Exception e) {
            CaBug.drucke("DbPersonenNatJurVersandadresse.updateCheck 003");
            System.err.println(" " + e.getMessage());
            return (false);
        }

    }

}
