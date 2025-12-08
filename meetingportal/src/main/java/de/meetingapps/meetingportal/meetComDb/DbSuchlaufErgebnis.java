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
import de.meetingapps.meetingportal.meetComEntities.EclSuchlaufErgebnis;

public class DbSuchlaufErgebnis {

    private Connection verbindung = null;
    private DbBasis dbBasis = null;
    private DbBundle dbBundle = null;

    private EclSuchlaufErgebnis ergebnisArray[] = null;

    /*************************Initialisierung***************************/
    public DbSuchlaufErgebnis(DbBundle pDbBundle) {
        /* Verbindung in lokale Daten eintragen*/
        if (pDbBundle == null) {
            CaBug.drucke("DbSuchlaufErgebnis.init 001 - dbBundle nicht initialisiert");
            return;
        }
        if (pDbBundle.dbBasis == null) {
            CaBug.drucke("DbSuchlaufErgebnis.init 002 - dbBasis nicht initialisiert");
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

    /**************Anzahl der Ergebnisse der read*-Methoden**************************/
    public EclSuchlaufErgebnis[] ergebnis() {
        return ergebnisArray;
    }

    /**********Liefert pN-tes Element des Ergebnisses der read*Methoden**************
     * pN geht von 0 bis anzErgebnis-1*/
    public EclSuchlaufErgebnis ergebnisPosition(int pN) {
        if (ergebnisArray == null) {
            CaBug.drucke("DbSuchlaufErgebnis.ergebnisPosition 001");
            return null;
        }
        if (pN < 0) {
            CaBug.drucke("DbSuchlaufErgebnis.ergebnisPosition 002");
            return null;
        }
        if (pN >= ergebnisArray.length) {
            CaBug.drucke("DbSuchlaufErgebnis.ergebnisPosition 003");
            return null;
        }
        return ergebnisArray[pN];
    }

    public int createTable() {
        int rc = 0;
        DbLowLevel lDbLowLevel = new DbLowLevel(dbBundle);
        rc = lDbLowLevel.createTable("CREATE TABLE " + dbBundle.getSchemaMandant() + "tbl_suchlaufErgebnis ( "
                + "`mandant` int(11) NOT NULL, " + "`ident` int(11) NOT NULL, "
                + "`identSuchlaufDefinition` int(11) NOT NULL, " + "`entstandenAus` int(11) NOT NULL, "
                + "`einzelSuchBegriff` varchar(100) NOT NULL, "
                + "`veraenderungGegenueberLetztemSuchlauf` int(11) DEFAULT NULL, "
                + "`identAktienregister` int(11) NOT NULL, " + "`identMelderegister` int(11) NOT NULL, "
                + "`wurdeVerarbeitet` int(11) DEFAULT NULL, "
                + "`verarbeitetNichtMehrInSuchergebnis` int(11) DEFAULT NULL, "
                + "`wurdeAusSucheAusgegrenzt` int(11) DEFAULT NULL, " + "`ausgegrenztWeil` varchar(400) DEFAULT NULL, "
                + "`gefundeneVollmachtName` varchar(100) DEFAULT NULL, " + "`parameter1` varchar(100) DEFAULT NULL, "
                + "`parameter2` varchar(100) DEFAULT NULL, " + "`parameter3` varchar(100) DEFAULT NULL, "
                + "`parameter4` varchar(100) DEFAULT NULL, " + "`parameter5` varchar(100) DEFAULT NULL, "
                + "PRIMARY KEY (`mandant`,`ident`) " + ") ");
        return rc;
    }

    /**************************deleteAll Löschen aller Datensätze  eines Mandanten****************/
    public int deleteAll() {
        return dbBundle.dbLowLevel
                .deleteMandant("DELETE FROM " + dbBundle.getSchemaMandant() + "tbl_suchlaufErgebnis where mandant=?");
    }

    /**************************setzt aktuelle Mandantennummer bei allen Datensätzen****************/
    public int updateMandant() {
        return dbBundle.dbLowLevel.rawUpdateMandant(dbBundle.getSchemaMandant() + "tbl_suchlaufErgebnis");
    }

    /********** dekodiert die aktuelle Position aus ergebnis  und gibt dieses zurück******/
    EclSuchlaufErgebnis decodeErgebnis(ResultSet pErgebnis) {

        EclSuchlaufErgebnis lEclReturn = new EclSuchlaufErgebnis();

        try {
            lEclReturn.mandant = pErgebnis.getInt("sue.mandant");
            lEclReturn.ident = pErgebnis.getInt("sue.ident");
            lEclReturn.identSuchlaufDefinition = pErgebnis.getInt("sue.identSuchlaufDefinition");
            lEclReturn.entstandenAus = pErgebnis.getInt("sue.entstandenAus");
            lEclReturn.einzelSuchBegriff = pErgebnis.getString("sue.einzelSuchBegriff");
            lEclReturn.veraenderungGegenueberLetztemSuchlauf = pErgebnis
                    .getInt("sue.veraenderungGegenueberLetztemSuchlauf");
            lEclReturn.identAktienregister = pErgebnis.getInt("sue.identAktienregister");
            lEclReturn.identMelderegister = pErgebnis.getInt("sue.identMelderegister");
            lEclReturn.wurdeVerarbeitet = pErgebnis.getInt("sue.wurdeVerarbeitet");
            lEclReturn.verarbeitetNichtMehrInSuchergebnis = pErgebnis.getInt("sue.verarbeitetNichtMehrInSuchergebnis");
            lEclReturn.wurdeAusSucheAusgegrenzt = pErgebnis.getInt("sue.wurdeAusSucheAusgegrenzt");

            lEclReturn.ausgegrenztWeil = pErgebnis.getString("sue.ausgegrenztWeil");
            lEclReturn.gefundeneVollmachtName = pErgebnis.getString("sue.gefundeneVollmachtName");

            lEclReturn.parameter1 = pErgebnis.getString("sue.parameter1");
            lEclReturn.parameter2 = pErgebnis.getString("sue.parameter2");
            lEclReturn.parameter3 = pErgebnis.getString("sue.parameter3");
            lEclReturn.parameter4 = pErgebnis.getString("sue.parameter4");
            lEclReturn.parameter5 = pErgebnis.getString("sue.parameter5");

        } catch (Exception e) {
            CaBug.drucke("DbSuchlaufErgebnis.decodeErgebnis 001");
            System.err.println(" " + e.getMessage());
        }

        return lEclReturn;
    }

    /********************* Fuellen Prepared Statement mit allen Feldern, beginnend bei offset.*****************
     * Kann sowohl für Insert, als auch für update verwendet werden.
     * 
     * offset= Startposition des ersten Feldes (also z.B. 1)
     */
    private int anzfelder = 18; /*Anpassen auf Anzahl der Felder pro Datensatz*/

    private void fuellePreparedStatementKomplett(PreparedStatement pPStm, int pOffset, EclSuchlaufErgebnis pEcl) {

        int startOffset = pOffset; /*Nur erforderlich zum Überprüfen von Programmierfehlern - Feldanzahl*/

        try {
            pPStm.setInt(pOffset, pEcl.mandant);
            pOffset++;
            pPStm.setInt(pOffset, pEcl.ident);
            pOffset++;
            pPStm.setInt(pOffset, pEcl.identSuchlaufDefinition);
            pOffset++;
            pPStm.setInt(pOffset, pEcl.entstandenAus);
            pOffset++;
            pPStm.setString(pOffset, pEcl.einzelSuchBegriff);
            pOffset++;
            pPStm.setInt(pOffset, pEcl.veraenderungGegenueberLetztemSuchlauf);
            pOffset++;
            pPStm.setInt(pOffset, pEcl.identAktienregister);
            pOffset++;
            pPStm.setInt(pOffset, pEcl.identMelderegister);
            pOffset++;
            pPStm.setInt(pOffset, pEcl.wurdeVerarbeitet);
            pOffset++;
            pPStm.setInt(pOffset, pEcl.verarbeitetNichtMehrInSuchergebnis);
            pOffset++;
            pPStm.setInt(pOffset, pEcl.wurdeAusSucheAusgegrenzt);
            pOffset++;

            pPStm.setString(pOffset, pEcl.ausgegrenztWeil);
            pOffset++;
            pPStm.setString(pOffset, pEcl.gefundeneVollmachtName);
            pOffset++;

            pPStm.setString(pOffset, pEcl.parameter1);
            pOffset++;
            pPStm.setString(pOffset, pEcl.parameter2);
            pOffset++;
            pPStm.setString(pOffset, pEcl.parameter3);
            pOffset++;
            pPStm.setString(pOffset, pEcl.parameter4);
            pOffset++;
            pPStm.setString(pOffset, pEcl.parameter5);
            pOffset++;

            if (pOffset - startOffset != anzfelder) {
                /*Nur wg. Überprüfung auf Programmierfehler - alle Felder berücksichtigt?*/
                CaBug.drucke("DbSuchlaufErgebnis.fuellePreparedStatementKomplett 002");
            }

        } catch (SQLException e) {
            CaBug.drucke("DbSuchlaufErgebnis.fuellePreparedStatementKomplett 001");
            e.printStackTrace();
        }

    }

    /**Insert
     * 
     * Returnwert:
     * =1 => Insert erfolgreich
     * ansonsten: Fehler
     */
    public int insert(EclSuchlaufErgebnis pEcl) {

        int erg = 0;
        dbBasis.beginTransaction();

        pEcl.mandant = dbBundle.clGlobalVar.mandant;

        try {

            /*Felder Neuanlage füllen*/
            String lSql = "INSERT INTO " + dbBundle.getSchemaMandant() + "tbl_suchlaufErgebnis " + "("
                    + "mandant, ident, identSuchlaufDefinition, "
                    + "entstandenAus, einzelSuchBegriff, veraenderungGegenueberLetztemSuchlauf, "
                    + "identAktienregister, identMelderegister, wurdeVerarbeitet, verarbeitetNichtMehrInSuchergebnis, wurdeAusSucheAusgegrenzt, "
                    + "ausgegrenztWeil, gefundeneVollmachtName, parameter1, parameter2, parameter3, parameter4, parameter5  "
                    + ")" + "VALUES (" + "?, ?, ?, " + "?, ?, ?, " + "?, ?, ?, ?, ?, " + "?, ?, ?, ?, ?, ?, ? " + ")";
            PreparedStatement lPStm = verbindung.prepareStatement(lSql, ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);

            fuellePreparedStatementKomplett(lPStm, 1, pEcl);

            erg = lPStm.executeUpdate();
            lPStm.close();
        } catch (Exception e2) {
            CaBug.drucke("DbSuchlaufErgebnis.insert 001");
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

    /**Mandant und identSuchlaufDefinition müssen übergeben werden.
     * 
     * Return-Wert = Anzahl der gefundenen Sätze (oder Fehlermeldung <0)*/
    public int readIdentSuchlaufDefinition(int pIdnetSuchlaufDefinition) {
        int anzInArray = 0;
        PreparedStatement lPStm = null;

        try {
            String lSql = "SELECT sue.* from " + dbBundle.getSchemaMandant() + "tbl_suchlaufErgebnis sue where "
                    + "sue.mandant=? AND " + "sue.identSuchlaufDefinition=? " + "ORDER BY sue.ident;";
            lPStm = verbindung.prepareStatement(lSql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            lPStm.setInt(1, dbBundle.clGlobalVar.mandant);
            lPStm.setInt(2, pIdnetSuchlaufDefinition);

            ResultSet lErgebnis = lPStm.executeQuery();
            lErgebnis.last();
            anzInArray = lErgebnis.getRow();
            lErgebnis.beforeFirst();

            ergebnisArray = new EclSuchlaufErgebnis[anzInArray];

            int i = 0;
            while (lErgebnis.next() == true) {
                ergebnisArray[i] = this.decodeErgebnis(lErgebnis);
                i++;
            }
            lErgebnis.close();
            lPStm.close();

        } catch (Exception e) {
            CaBug.drucke("DbSuchlaufErgebnis.read 003");
            System.err.println(" " + e.getMessage());
            return (-1);
        }
        return (anzInArray);
    }

    /**Update. 
     * 
     * Returnwert:
     * pfXyWurdeVonAnderemBenutzerVeraendert
     * -1 => unbekannter Fehler
     * 1 = Update wurde durchgeführt.
     * 
     */
    public int update(EclSuchlaufErgebnis pEcl) {
        try {

            String lSql = "UPDATE " + dbBundle.getSchemaMandant() + "tbl_suchlaufErgebnis SET "
                    + "mandant=?, ident=?, identSuchlaufDefinition=?, "
                    + "entstandenAus=?, einzelSuchBegriff=?, veraenderungGegenueberLetztemSuchlauf=?, "
                    + "identAktienregister=?, identMelderegister=?, wurdeVerarbeitet=?, verarbeitetNichtMehrInSuchergebnis=?, wurdeAusSucheAusgegrenzt=?, "
                    + "ausgegrenztWeil=?, gefundeneVollmachtName=?, parameter1=?, parameter2=?, parameter3=?, parameter4=?, parameter5=?  "
                    + "WHERE " + "mandant=? AND ident=? ";

            PreparedStatement lPStm = verbindung.prepareStatement(lSql);
            fuellePreparedStatementKomplett(lPStm, 1, pEcl);
            lPStm.setInt(anzfelder + 1, pEcl.mandant);
            lPStm.setInt(anzfelder + 2, pEcl.ident);

            int ergebnis = lPStm.executeUpdate();
            lPStm.close();
            if (ergebnis == 0) {
                return (CaFehler.pfXyWurdeVonAnderemBenutzerVeraendert);
            }
        } catch (Exception e1) {
            CaBug.drucke("DbSuchlaufErgebnis.update 001");
            System.err.println(" " + e1.getMessage());
            return (-1);
        }

        return (1);
    }

    /**Return-Werte:
     * pfXyWurdeVonAnderemBenutzerVeraendert
     * -1 => undefinierter Fehler
     * 1 => Löschen erfolgreich
     */
    public int delete(int pIdent) {
        /*XXXNoch komplett zu überarbeiten*/
        try {

            String sql = "DELETE FROM " + dbBundle.getSchemaMandant()
                    + "tbl_suchlaufErgebnis WHERE ident=? AND mandant=? ";

            PreparedStatement pstm1 = verbindung.prepareStatement(sql);
            pstm1.setInt(1, pIdent);
            pstm1.setInt(2, dbBundle.clGlobalVar.mandant);

            int ergebnis1 = pstm1.executeUpdate();
            pstm1.close();
            if (ergebnis1 == 0) {
                return (CaFehler.pfXyWurdeVonAnderemBenutzerVeraendert);
            }
        } catch (Exception e1) {
            CaBug.drucke("DbSuchlaufErgebnis.delete 001");
            System.err.println(" " + e1.getMessage());
            return (-1);
        }

        return (1);
    }

}
