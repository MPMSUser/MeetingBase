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
import de.meetingapps.meetingportal.meetComEntities.EclTeilnehmerStandVerein;

public class DbTeilnehmerStandVerein {

    private Connection verbindung = null;
    private DbBasis dbBasis = null;
    private DbBundle dbBundle = null;

    private EclTeilnehmerStandVerein ergebnisArray[] = null;

    /*************************Initialisierung***************************/
    public DbTeilnehmerStandVerein(DbBundle pDbBundle) {
        /* Verbindung in lokale Daten eintragen*/
        if (pDbBundle == null) {
            CaBug.drucke("DbTeilnehmerStandVerein.init 001 - dbBundle nicht initialisiert");
            return;
        }
        if (pDbBundle.dbBasis == null) {
            CaBug.drucke("DbTeilnehmerStandVerein.init 002 - dbBasis nicht initialisiert");
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
    public EclTeilnehmerStandVerein ergebnisPosition(int pN) {
        if (ergebnisArray == null) {
            CaBug.drucke("DbTeilnehmerStandVerein.ergebnisPosition 001");
            return null;
        }
        if (pN < 0) {
            CaBug.drucke("DbTeilnehmerStandVerein.ergebnisPosition 002");
            return null;
        }
        if (pN >= ergebnisArray.length) {
            CaBug.drucke("DbTeilnehmerStandVerein.ergebnisPosition 003");
            return null;
        }
        return ergebnisArray[pN];
    }

    /**Liefert Array zurücl***********/
    public EclTeilnehmerStandVerein[] ergebnis() {
        return ergebnisArray;
    }

    public int createTable() {
        int rc = 0;
        DbLowLevel lDbLowLevel = new DbLowLevel(dbBundle);
        rc = lDbLowLevel.createTable("CREATE TABLE " + dbBundle.getSchemaMandant() + "tbl_teilnehmerstandverein ( "
                + "`mandant` int(11) DEFAULT NULL, " + "`standZuZeit` char(20) NOT NULL, "
                + "`autoOrManuell` int(11) DEFAULT NULL, "
                + "`anzahlStimmberechtigteMitgliederPraesent` int(11) DEFAULT NULL, "
                + "`anzahlStimmberechtigteMitgliederJemalsPraesent` int(11) DEFAULT NULL, "
                + "`anzahlNichtStimmberechtigteMitgliederPraesent` int(11) DEFAULT NULL, "
                + "`anzahlNichtStimmberechtigteMitgliederJemalsPraesent` int(11) DEFAULT NULL, "
                + "`anzahlGaestePraesent` int(11) DEFAULT NULL, "
                + "`anzahlGaesteJemalsPraesent` int(11) DEFAULT NULL, " + "`anzahlBuchungenZuB0` int(11) DEFAULT NULL, "
                + "`anzahlBuchungenZuB1` int(11) DEFAULT NULL, " + "`anzahlBuchungenZuB2` int(11) DEFAULT NULL, "
                + "`anzahlBuchungenZuB3` int(11) DEFAULT NULL, " + "`anzahlBuchungenZuB4` int(11) DEFAULT NULL, "
                + "`anzahlBuchungenZuB5` int(11) DEFAULT NULL, " + "`anzahlBuchungenZuB6` int(11) DEFAULT NULL, "
                + "`anzahlBuchungenZuB7` int(11) DEFAULT NULL, " + "`anzahlBuchungenZuB8` int(11) DEFAULT NULL, "
                + "`anzahlBuchungenZuB9` int(11) DEFAULT NULL, " + "`anzahlWarnungen` int(11) DEFAULT NULL, "
                + "PRIMARY KEY (`standZuZeit`) " + ") ");
        return rc;
    }

    /**************************deleteAll Löschen aller Datensätze  eines Mandanten****************/
    public int deleteAll() {
        return dbBundle.dbLowLevel.deleteMandant(
                "DELETE FROM " + dbBundle.getSchemaMandant() + "tbl_teilnehmerStandVerein where mandant=?;");
    }

    /**************************setzt aktuelle Mandantennummer bei allen Datensätzen****************/
    public int updateMandant() {
        return dbBundle.dbLowLevel.rawUpdateMandant(dbBundle.getSchemaMandant() + "tbl_teilnehmerStandVerein");
    }

    /********** dekodiert die aktuelle Position aus ergebnis in EclTeilnehmerStandVerein und gibt dieses zurück******/
    private EclTeilnehmerStandVerein decodeErgebnis(ResultSet pErgebnis) {

        EclTeilnehmerStandVerein lTeilnehmerStandVerein = new EclTeilnehmerStandVerein();

        try {

            lTeilnehmerStandVerein.mandant = pErgebnis.getInt("mandant");

            lTeilnehmerStandVerein.standZuZeit = pErgebnis.getString("standZuZeit");

            lTeilnehmerStandVerein.autoOrManuell = pErgebnis.getInt("autoOrManuell");

            lTeilnehmerStandVerein.anzahlStimmberechtigteMitgliederPraesent = pErgebnis
                    .getInt("anzahlStimmberechtigteMitgliederPraesent");
            lTeilnehmerStandVerein.anzahlStimmberechtigteMitgliederJemalsPraesent = pErgebnis
                    .getInt("anzahlStimmberechtigteMitgliederJemalsPraesent");
            lTeilnehmerStandVerein.anzahlNichtStimmberechtigteMitgliederPraesent = pErgebnis
                    .getInt("anzahlNichtStimmberechtigteMitgliederPraesent");
            lTeilnehmerStandVerein.anzahlNichtStimmberechtigteMitgliederJemalsPraesent = pErgebnis
                    .getInt("anzahlNichtStimmberechtigteMitgliederJemalsPraesent");
            lTeilnehmerStandVerein.anzahlGaestePraesent = pErgebnis.getInt("anzahlGaestePraesent");
            lTeilnehmerStandVerein.anzahlGaesteJemalsPraesent = pErgebnis.getInt("anzahlGaesteJemalsPraesent");

            lTeilnehmerStandVerein.anzahlBuchungenZuB0 = pErgebnis.getInt("anzahlBuchungenZuB0");
            lTeilnehmerStandVerein.anzahlBuchungenZuB1 = pErgebnis.getInt("anzahlBuchungenZuB1");
            lTeilnehmerStandVerein.anzahlBuchungenZuB2 = pErgebnis.getInt("anzahlBuchungenZuB2");
            lTeilnehmerStandVerein.anzahlBuchungenZuB3 = pErgebnis.getInt("anzahlBuchungenZuB3");
            lTeilnehmerStandVerein.anzahlBuchungenZuB4 = pErgebnis.getInt("anzahlBuchungenZuB4");
            lTeilnehmerStandVerein.anzahlBuchungenZuB5 = pErgebnis.getInt("anzahlBuchungenZuB5");
            lTeilnehmerStandVerein.anzahlBuchungenZuB6 = pErgebnis.getInt("anzahlBuchungenZuB6");
            lTeilnehmerStandVerein.anzahlBuchungenZuB7 = pErgebnis.getInt("anzahlBuchungenZuB7");
            lTeilnehmerStandVerein.anzahlBuchungenZuB8 = pErgebnis.getInt("anzahlBuchungenZuB8");
            lTeilnehmerStandVerein.anzahlBuchungenZuB9 = pErgebnis.getInt("anzahlBuchungenZuB9");
            lTeilnehmerStandVerein.anzahlWarnungen = pErgebnis.getInt("anzahlWarnungen");

        } catch (Exception e) {
            CaBug.drucke("DbTeilnehmerStandVerein.decodeErgebnis 001");
            System.err.println(" " + e.getMessage());
        }

        return lTeilnehmerStandVerein;
    }

    /********************* Fuellen Prepared Statement mit allen Feldern, beginnend bei offset.*****************
     * Kann sowohl für Insert, als auch für update verwendet werden.
     * 
     * offset= Startposition des ersten Feldes (also z.B. 1)
     */
    private int anzfelder = 20; /*Anpassen auf Anzahl der Felder pro Datensatz*/

    private void fuellePreparedStatementKomplett(PreparedStatement pPStm, int pOffset,
            EclTeilnehmerStandVerein pTeilnehmerStandVerein) {

        int startOffset = pOffset; /*Nur erforderlich zum Überprüfen von Programmierfehlern - Feldanzahl*/

        try {
            pPStm.setInt(pOffset, pTeilnehmerStandVerein.mandant);
            pOffset++;

            pPStm.setString(pOffset, pTeilnehmerStandVerein.standZuZeit);
            pOffset++;

            pPStm.setInt(pOffset, pTeilnehmerStandVerein.autoOrManuell);
            pOffset++;

            pPStm.setInt(pOffset, pTeilnehmerStandVerein.anzahlStimmberechtigteMitgliederPraesent);
            pOffset++;
            pPStm.setInt(pOffset, pTeilnehmerStandVerein.anzahlStimmberechtigteMitgliederJemalsPraesent);
            pOffset++;
            pPStm.setInt(pOffset, pTeilnehmerStandVerein.anzahlNichtStimmberechtigteMitgliederPraesent);
            pOffset++;
            pPStm.setInt(pOffset, pTeilnehmerStandVerein.anzahlNichtStimmberechtigteMitgliederJemalsPraesent);
            pOffset++;
            pPStm.setInt(pOffset, pTeilnehmerStandVerein.anzahlGaestePraesent);
            pOffset++;
            pPStm.setInt(pOffset, pTeilnehmerStandVerein.anzahlGaesteJemalsPraesent);
            pOffset++;

            pPStm.setInt(pOffset, pTeilnehmerStandVerein.anzahlBuchungenZuB0);
            pOffset++;
            pPStm.setInt(pOffset, pTeilnehmerStandVerein.anzahlBuchungenZuB1);
            pOffset++;
            pPStm.setInt(pOffset, pTeilnehmerStandVerein.anzahlBuchungenZuB2);
            pOffset++;
            pPStm.setInt(pOffset, pTeilnehmerStandVerein.anzahlBuchungenZuB3);
            pOffset++;
            pPStm.setInt(pOffset, pTeilnehmerStandVerein.anzahlBuchungenZuB4);
            pOffset++;
            pPStm.setInt(pOffset, pTeilnehmerStandVerein.anzahlBuchungenZuB5);
            pOffset++;
            pPStm.setInt(pOffset, pTeilnehmerStandVerein.anzahlBuchungenZuB6);
            pOffset++;
            pPStm.setInt(pOffset, pTeilnehmerStandVerein.anzahlBuchungenZuB7);
            pOffset++;
            pPStm.setInt(pOffset, pTeilnehmerStandVerein.anzahlBuchungenZuB8);
            pOffset++;
            pPStm.setInt(pOffset, pTeilnehmerStandVerein.anzahlBuchungenZuB9);
            pOffset++;

            pPStm.setInt(pOffset, pTeilnehmerStandVerein.anzahlWarnungen);
            pOffset++;
            if (pOffset - startOffset != anzfelder) {
                /*Nur wg. Überprüfung auf Programmierfehler - alle Felder berücksichtigt?*/
                CaBug.drucke("DbTeilnehmerStandVerein.fuellePreparedStatementKomplett 002");
            }

        } catch (SQLException e) {
            CaBug.drucke("DbTeilnehmerStandVerein.fuellePreparedStatementKomplett 001");
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
    public int insert(EclTeilnehmerStandVerein pTeilnehmerStandVerein) {

        int erg = 0;

        /* Start Transaktion */
        dbBasis.beginTransaction();

        /* neue InterneIdent vergeben */
        /*	In diesem Fall nicht benötigt. Ist nur enthalten wg. "Blaupausenfunktionalität"
        erg=dbBasis.getInterneIdentAktienregisterEintrag();
        if (erg<1){
        	CaBug.drucke("DbTeilnehmerStandVerein.insert 002");
        	dbBasis.rollbackTransaction();dbBasis.endTransaction();return (erg);
        }
        
        lAktienregisterZusatz.aktienregisterIdent=erg;
        
        */

        pTeilnehmerStandVerein.mandant = dbBundle.clGlobalVar.mandant;

        /* Satz einfügen: 
         * Verarbeitungshinweis: 
         * 	>	nachdem InterneIdent immer eindeutig vergeben werden, ist prinzipiell eine "Doppeleinfügung"
         * 		von InterneIdent nicht möglich. Sollte es dazu kommen, ist das immer ein Programmfehler!
         */

        try {

            /*Felder Neuanlage füllen*/
            String lSql = "INSERT INTO " + dbBundle.getSchemaMandant() + "tbl_teilnehmerStandVerein " + "("
                    + "mandant, standZuZeit, autoOrManuell, "
                    + "anzahlStimmberechtigteMitgliederPraesent, anzahlStimmberechtigteMitgliederJemalsPraesent, anzahlNichtStimmberechtigteMitgliederPraesent, "
                    + "anzahlNichtStimmberechtigteMitgliederJemalsPraesent, anzahlGaestePraesent, anzahlGaesteJemalsPraesent,"
                    + "anzahlBuchungenZuB0, anzahlBuchungenZuB1, anzahlBuchungenZuB2, anzahlBuchungenZuB3, anzahlBuchungenZuB4, "
                    + "anzahlBuchungenZuB5, anzahlBuchungenZuB6, anzahlBuchungenZuB7, anzahlBuchungenZuB8, anzahlBuchungenZuB9,  anzahlWarnungen "
                    + ") " + "VALUES (" + "?, ?, ?, " + "?, ?, ?, " + "?, ?, ?, " + "?, ?, ?, ?, ?, "
                    + "?, ?, ?, ?, ?, ? " + ")";
            PreparedStatement lPStm = verbindung.prepareStatement(lSql, ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);

            fuellePreparedStatementKomplett(lPStm, 1, pTeilnehmerStandVerein);

            erg = lPStm.executeUpdate();
            lPStm.close();
        } catch (Exception e2) {
            CaBug.drucke("DbTeilnehmerStandVerein.insert 001");
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

    public int read_all() {
        int anzInArray = 0;
        PreparedStatement lPStm = null;

        try {
            String lSql = "SELECT * from " + dbBundle.getSchemaMandant() + "tbl_teilnehmerStandVerein where "
                    + "mandant=? " + "ORDER BY standZuZeit;";
            lPStm = verbindung.prepareStatement(lSql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            lPStm.setInt(1, dbBundle.clGlobalVar.mandant);

            ResultSet lErgebnis = lPStm.executeQuery();
            lErgebnis.last();
            anzInArray = lErgebnis.getRow();
            lErgebnis.beforeFirst();

            ergebnisArray = new EclTeilnehmerStandVerein[anzInArray];

            int i = 0;
            while (lErgebnis.next() == true) {
                ergebnisArray[i] = this.decodeErgebnis(lErgebnis);
                i++;
            }
            lErgebnis.close();
            lPStm.close();

        } catch (Exception e) {
            CaBug.drucke("DbTeilnehmerStandVerein.read_all 003");
            System.err.println(" " + e.getMessage());
            return (-1);
        }
        return (anzInArray);
    }

}
