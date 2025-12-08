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
import de.meetingapps.meetingportal.meetComEntities.EclAbstimmungsVorschlagEmpfehlung;

public class DbAbstimmungsVorschlagEmpfehlung  extends DbRootExecute {

    private int logDrucken = 3;

    private Connection verbindung = null;
    private DbBasis dbBasis = null;
    private DbBundle dbBundle = null;

    private EclAbstimmungsVorschlagEmpfehlung ergebnisArray[] = null;

    /*************************Initialisierung***************************/
    public DbAbstimmungsVorschlagEmpfehlung(DbBundle pDbBundle) {
        /* Verbindung in lokale Daten eintragen*/
        if (pDbBundle == null) {
            CaBug.drucke("DbAbstimmungsVorschlagEmpfehlung.init 001 - dbBundle nicht initialisiert");
            return;
        }
        if (pDbBundle.dbBasis == null) {
            CaBug.drucke("DbAbstimmungsVorschlagEmpfehlung.init 002 - dbBasis nicht initialisiert");
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
    public EclAbstimmungsVorschlagEmpfehlung[] ergebnis() {
        return ergebnisArray;
    }

    /**********Liefert pN-tes Element des Ergebnisses der read*Methoden**************
     * pN geht von 0 bis anzErgebnis-1*/
    public EclAbstimmungsVorschlagEmpfehlung ergebnisPosition(int pN) {
        if (ergebnisArray == null) {
            CaBug.drucke("DbAbstimmungsVorschlagEmpfehlung.ergebnisPosition 001");
            return null;
        }
        if (pN < 0) {
            CaBug.drucke("DbAbstimmungsVorschlagEmpfehlung.ergebnisPosition 002");
            return null;
        }
        if (pN >= ergebnisArray.length) {
            CaBug.drucke("DbAbstimmungsVorschlagEmpfehlung.ergebnisPosition 003");
            return null;
        }
        return ergebnisArray[pN];
    }

    public int createTable() {
        int rc = 0;
        DbLowLevel lDbLowLevel = new DbLowLevel(dbBundle);
        String hString = "CREATE TABLE " + dbBundle.getSchemaMandant() + "tbl_abstimmungsVorschlagEmpfehlung ( "
                + "`mandant` int(11) NOT NULL, " + "`ident` int(11) NOT NULL, " + "`identMail` int(11) DEFAULT NULL, "
                + "`erstellungszeitpunkt` char(19) NOT NULL DEFAULT 'P', " + "`erstelltVonUserId` int(11) NOT NULL, "
                + "`kurzBeschreibung` varchar(200) DEFAULT NULL, ";
        for (int i = 0; i < 200; i++) {
            hString = hString + "`empfehlungFuerAbstimmungsIdent" + Integer.toString(i) + "` int(11) DEFAULT NULL, ";
        }
        hString = hString + "PRIMARY KEY (`mandant`,`ident`) " + ") ";
        rc = lDbLowLevel.createTable(hString);

        return rc;
    }

    /**************************deleteAll Löschen aller Datensätze  eines Mandanten****************/
    public int deleteAll() {
        dbBundle.dbBasis.resetInterneIdentInsti();
        return dbBundle.dbLowLevel.deleteMandant(
                "DELETE FROM " + dbBundle.getSchemaMandant() + "tbl_abstimmungsVorschlagEmpfehlung where mandant=?");
    }

    /**************************setzt aktuelle Mandantennummer bei allen Datensätzen****************/
    public int updateMandant() {
        return dbBundle.dbLowLevel.rawUpdateMandant(dbBundle.getSchemaMandant() + "tbl_abstimmungsVorschlagEmpfehlung");
    }

    public void reorgInterneIdent() {
        int lMax = dbBundle.dbLowLevel
                .liefereHoechsteIdentOhneMandant /*liefereHoechsteIdent*/("SELECT MAX(ident) FROM "
                        + dbBundle.getSchemaMandant() + "tbl_abstimmungsVorschlagEmpfehlung;" /* where mandant=?*/ );
        if (lMax != -1) {
            dbBundle.dbBasis.resetInterneIdentAbstimungsVorschlagEmpfehlung(lMax);
        }
    }

    /********** dekodiert die aktuelle Position aus ergebnis  und gibt dieses zurück******/
    EclAbstimmungsVorschlagEmpfehlung decodeErgebnis(ResultSet pErgebnis) {

        EclAbstimmungsVorschlagEmpfehlung lEclReturn = new EclAbstimmungsVorschlagEmpfehlung();

        try {
            lEclReturn.mandant = pErgebnis.getInt("mandant");
            lEclReturn.ident = pErgebnis.getInt("ident");
            lEclReturn.identMail = pErgebnis.getInt("identMail");
            lEclReturn.erstellungszeitpunkt = pErgebnis.getString("erstellungszeitpunkt");
            lEclReturn.erstelltVonUserId = pErgebnis.getInt("erstelltVonUserId");
            lEclReturn.kurzBeschreibung = pErgebnis.getString("kurzBeschreibung");

            for (int i = 0; i < 200; i++) {
                lEclReturn.empfehlungFuerAbstimmungsIdent[i] = pErgebnis
                        .getInt("empfehlungFuerAbstimmungsIdent" + Integer.toString(i));
            }

        } catch (Exception e) {
            CaBug.drucke("DbAbstimmungsVorschlagEmpfehlung.decodeErgebnis 001");
            System.err.println(" " + e.getMessage());
        }

        return lEclReturn;
    }

    /********************* Fuellen Prepared Statement mit allen Feldern, beginnend bei offset.*****************
     * Kann sowohl für Insert, als auch für update verwendet werden.
     * 
     * offset= Startposition des ersten Feldes (also z.B. 1)
     */
    private int anzfelder = 206; /*Anpassen auf Anzahl der Felder pro Datensatz*/

    private void fuellePreparedStatementKomplett(PreparedStatement pPStm, int pOffset,
            EclAbstimmungsVorschlagEmpfehlung pEcl) {

        int startOffset = pOffset; /*Nur erforderlich zum Überprüfen von Programmierfehlern - Feldanzahl*/

        try {
            pPStm.setInt(pOffset, pEcl.mandant);
            pOffset++;
            pPStm.setInt(pOffset, pEcl.ident);
            pOffset++;
            pPStm.setInt(pOffset, pEcl.identMail);
            pOffset++;
            pPStm.setString(pOffset, pEcl.erstellungszeitpunkt);
            pOffset++;
            pPStm.setInt(pOffset, pEcl.erstelltVonUserId);
            pOffset++;
            pPStm.setString(pOffset, pEcl.kurzBeschreibung);
            pOffset++;

            for (int i = 0; i < 200; i++) {
                pPStm.setInt(pOffset, pEcl.empfehlungFuerAbstimmungsIdent[i]);
                pOffset++;
            }

            if (pOffset - startOffset != anzfelder) {
                /*Nur wg. Überprüfung auf Programmierfehler - alle Felder berücksichtigt?*/
                CaBug.drucke("DbAbstimmungsVorschlagEmpfehlung.fuellePreparedStatementKomplett 002");
            }

        } catch (SQLException e) {
            CaBug.drucke("DbAbstimmungsVorschlagEmpfehlung.fuellePreparedStatementKomplett 001");
            e.printStackTrace();
        }

    }

    /**Insert
     * 
     * Returnwert:
     * =1 => Insert erfolgreich
     * ansonsten: Fehler
     */
    public int insert(EclAbstimmungsVorschlagEmpfehlung pEcl) {
        CaBug.druckeLog("insert A", logDrucken, 10);
        int erg = 0;
        dbBasis.beginTransaction();

        /* neue InterneIdent vergeben */
        erg = dbBasis.getInterneIdentAbstimungsVorschlagEmpfehlung();
        if (erg < 1) {
            CaBug.drucke("DbAbstimmungsVorschlagEmpfehlung.insert 002");
            dbBasis.endTransaction();
            return (erg);
        }

        pEcl.ident = erg;

        pEcl.mandant = dbBundle.clGlobalVar.mandant;

        try {

            /*Felder Neuanlage füllen*/
            String lSql = "INSERT INTO " + dbBundle.getSchemaMandant() + "tbl_abstimmungsVorschlagEmpfehlung " + "("
                    + "mandant, ident, identMail, " + "erstellungszeitpunkt, erstelltVonUserId, kurzBeschreibung";
            for (int i = 0; i < 200; i++) {
                lSql = lSql + ", empfehlungFuerAbstimmungsIdent" + Integer.toString(i);
            }
            lSql = lSql + ")" + "VALUES (" + "?, ?, ?, " + "?, ?, ? ";
            for (int i = 0; i < 200; i++) {
                lSql = lSql + ", ?";
            }
            lSql = lSql + " )";
            PreparedStatement lPStm = verbindung.prepareStatement(lSql, ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);

            fuellePreparedStatementKomplett(lPStm, 1, pEcl);

            erg = executeUpdate(lPStm);
            lPStm.close();
        } catch (Exception e2) {
            CaBug.drucke("DbAbstimmungsVorschlagEmpfehlung.insert 001");
            System.err.println(" " + e2.getMessage());
        }

        if (erg == 0) {/*Fehler beim Einfügen - d.h. primaryKey bereits vorhanden*/
            dbBasis.rollbackTransaction();
            dbBasis.endTransaction();
            return (-1);
        }
        CaBug.druckeLog("insert E",  logDrucken, 10);

        /* Ende Transaktion */
        dbBasis.endTransaction();
        return (1);
    }

    /** Return-Wert = Anzahl der gefundenen Sätze (oder Fehlermeldung <0)*/
    public int read(int pIdent) {
        int anzInArray = 0;
        PreparedStatement lPStm = null;

        try {
            String lSql = "SELECT * from " + dbBundle.getSchemaMandant() + "tbl_abstimmungsVorschlagEmpfehlung where "
                    + "mandant=? AND " + "ident=? " + "ORDER BY ident;";
            lPStm = verbindung.prepareStatement(lSql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            lPStm.setInt(1, dbBundle.clGlobalVar.mandant);
            lPStm.setInt(2, pIdent);

            ResultSet lErgebnis = executeQuery(lPStm);
            lErgebnis.last();
            anzInArray = lErgebnis.getRow();
            lErgebnis.beforeFirst();

            ergebnisArray = new EclAbstimmungsVorschlagEmpfehlung[anzInArray];

            int i = 0;
            while (lErgebnis.next() == true) {
                ergebnisArray[i] = this.decodeErgebnis(lErgebnis);
                i++;
            }
            lErgebnis.close();
            lPStm.close();

        } catch (Exception e) {
            CaBug.drucke("DbAbstimmungsVorschlagEmpfehlung.read 003");
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
    public int update(EclAbstimmungsVorschlagEmpfehlung pEcl) {
        CaBug.druckeLog("update A", logDrucken, 10);

        try {

            String lSql = "UPDATE " + dbBundle.getSchemaMandant() + "tbl_abstimmungsVorschlagEmpfehlung SET "
                    + "mandant=?, ident=?, identMail=?, "
                    + "erstellungszeitpunkt=?, erstelltVonUserId=?, kurzBeschreibung=? ";

            for (int i = 0; i < 200; i++) {
                lSql = lSql + ", empfehlungFuerAbstimmungsIdent" + Integer.toString(i) + "=?";
            }
            lSql = lSql + " WHERE " + "mandant=? AND ident=? ";

            PreparedStatement lPStm = verbindung.prepareStatement(lSql);
            fuellePreparedStatementKomplett(lPStm, 1, pEcl);
            lPStm.setInt(anzfelder + 1, pEcl.mandant);
            lPStm.setInt(anzfelder + 2, pEcl.ident);

            int ergebnis = executeUpdate(lPStm);
            lPStm.close();
            if (ergebnis == 0) {
                return (CaFehler.pfXyWurdeVonAnderemBenutzerVeraendert);
            }
        } catch (Exception e1) {
            CaBug.drucke("DbAbstimmungsVorschlagEmpfehlung.update 001");
            System.err.println(" " + e1.getMessage());
            return (-1);
        }

        CaBug.druckeLog("update E", logDrucken, 10);

        return (1);
    }

    /**Return-Werte:
     * pfXyWurdeVonAnderemBenutzerVeraendert
     * -1 => undefinierter Fehler
     * 1 => Löschen erfolgreich
     */
    public int delete(int pIdent) {
        try {

            String sql = "DELETE FROM " + dbBundle.getSchemaMandant()
                    + "tbl_abstimmungsVorschlagEmpfehlung WHERE ident=? AND mandant=? ";

            PreparedStatement pstm1 = verbindung.prepareStatement(sql);
            pstm1.setInt(1, pIdent);
            pstm1.setInt(2, dbBundle.clGlobalVar.mandant);

            int ergebnis1 = executeUpdate(pstm1);
            pstm1.close();
            if (ergebnis1 == 0) {
                return (CaFehler.pfXyWurdeVonAnderemBenutzerVeraendert);
            }
        } catch (Exception e1) {
            CaBug.drucke("DbAbstimmungsVorschlagEmpfehlung.delete 001");
            System.err.println(" " + e1.getMessage());
            return (-1);
        }

        return (1);
    }

}
