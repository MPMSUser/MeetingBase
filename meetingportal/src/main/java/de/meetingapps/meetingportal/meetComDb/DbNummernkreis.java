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
import de.meetingapps.meetingportal.meetComEntities.EclNummernkreis;

public class DbNummernkreis extends DbRootExecute {

    private Connection verbindung = null;
    //	private DbBasis dbBasis=null;
    private DbBundle dbBundle = null;

    EclNummernkreis ergebnisArray[] = null;

    /*************************Initialisierung***************************/
    public DbNummernkreis(DbBundle pDbBundle) {
        /* Verbindung in lokale Daten eintragen*/
        if (pDbBundle == null) {
            CaBug.drucke("DbNummernkreis.init 001 - dbBundle nicht initialisiert");
            return;
        }
        if (pDbBundle.dbBasis == null) {
            CaBug.drucke("DbNummernkreis.init 002 - dbBasis nicht initialisiert");
            return;
        }

        //		dbBasis=pDbBundle.dbBasis;
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
    public EclNummernkreis[] ergebnis() {
        return ergebnisArray;
    }

    public int createTable() {
        int rc = 0;
        DbLowLevel lDbLowLevel = new DbLowLevel(dbBundle);
        rc = lDbLowLevel.createTable("CREATE TABLE " + dbBundle.getSchemaMandant() + "tbl_nummernkreise ( "
                + "`mandant` int(11) NOT NULL, " + "`schluessel` int(11) NOT NULL, "
                + "`letzteInterneIdent` int(11) DEFAULT NULL, " + "`alphaZutrittsIdent` int(11) DEFAULT NULL, "
                + "`laengeZutrittsIdent` int(11) DEFAULT NULL, " + "`minZutrittsIdent` int(11) DEFAULT NULL, "
                + "`maxZutrittsIdent` int(11) DEFAULT NULL, " + "`letzteZutrittsIdent` int(11) DEFAULT NULL, "
                + "PRIMARY KEY (`schluessel`,`mandant`) " + ") "

        );
        return rc;
    }

    /**********Liefert pN-tes Element des Ergebnisses der read*Methoden**************
     * pN geht von 0 bis anzErgebnis-1*/
    public EclNummernkreis ergebnisPosition(int pN) {
        if (ergebnisArray == null) {
            CaBug.drucke("DbNummernkreis.ergebnisPosition 001");
            return null;
        }
        if (pN < 0) {
            CaBug.drucke("DbNummernkreis.ergebnisPosition 002");
            return null;
        }
        if (pN >= ergebnisArray.length) {
            CaBug.drucke("DbNummernkreis.ergebnisPosition 003");
            return null;
        }
        return ergebnisArray[pN];
    }

    /**************************deleteAll Löschen aller Datensätze  eines Mandanten****************/
    public int deleteAll() {
        int erg = 0;

        try {

            String sql1 = "DELETE FROM " + dbBundle.getSchemaMandant() + "tbl_nummernkreise where mandant=?;";
            PreparedStatement pstm1 = verbindung.prepareStatement(sql1);
            pstm1.setInt(1, dbBundle.clGlobalVar.mandant);

            erg = 0; /*Falls Duplicate Key, dann wird exception geschmissen und erg wohl nicht gefüllt!*/
            erg = executeUpdate(pstm1);

            pstm1.close();
        } catch (Exception e2) {
            CaBug.drucke("DbNummernkreis.deleteAll 001");
            System.err.println(" " + e2.getMessage());
            return (erg);
        }

        return 1;
    }

    /**************************setzt aktuelle Mandantennummer bei allen Datensätzen****************/
    public int updateMandant() {
        return dbBundle.dbLowLevel.rawUpdateMandant(dbBundle.getSchemaMandant() + "tbl_nummernkreise");
    }

    /********** dekodiert die aktuelle Position aus ergebnis  und gibt dieses zurück******/
    EclNummernkreis decodeErgebnis(ResultSet pErgebnis) {

        EclNummernkreis lNummernkreis = new EclNummernkreis();

        try {
            lNummernkreis.mandant = pErgebnis.getInt("nk.mandant");
            lNummernkreis.schluessel = pErgebnis.getInt("nk.schluessel");
            lNummernkreis.letzteInterneIdent = pErgebnis.getInt("nk.letzteInterneIdent");
            lNummernkreis.alphaZutrittsIdent = pErgebnis.getInt("nk.alphaZutrittsIdent");
            lNummernkreis.laengeZutrittsIdent = pErgebnis.getInt("nk.laengeZutrittsIdent");
            lNummernkreis.minZutrittsIdent = pErgebnis.getInt("nk.minZutrittsIdent");
            lNummernkreis.maxZutrittsIdent = pErgebnis.getInt("nk.maxZutrittsIdent");
            lNummernkreis.letzteZutrittsIdent = pErgebnis.getInt("nk.letzteZutrittsIdent");
        } catch (Exception e) {
            CaBug.drucke("DbNummernkreis.decodeErgebnis 001");
            System.err.println(" " + e.getMessage());
        }

        return lNummernkreis;
    }

    /********************* Fuellen Prepared Statement mit allen Feldern, beginnend bei offset.*****************
     * Kann sowohl für Insert, als auch für update verwendet werden.
     * 
     * offset= Startposition des ersten Feldes (also z.B. 1)
     */
    private int anzfelder = 8; /*Anpassen auf Anzahl der Felder pro Datensatz*/

    private void fuellePreparedStatementKomplett(PreparedStatement pPStm, int pOffset, EclNummernkreis lNummernkreis) {

        int startOffset = pOffset; /*Nur erforderlich zum Überprüfen von Programmierfehlern - Feldanzahl*/

        try {
            pPStm.setInt(pOffset, lNummernkreis.mandant);
            pOffset++;
            pPStm.setInt(pOffset, lNummernkreis.schluessel);
            pOffset++;
            pPStm.setInt(pOffset, lNummernkreis.letzteInterneIdent);
            pOffset++;
            pPStm.setInt(pOffset, lNummernkreis.alphaZutrittsIdent);
            pOffset++;
            pPStm.setInt(pOffset, lNummernkreis.laengeZutrittsIdent);
            pOffset++;
            pPStm.setInt(pOffset, lNummernkreis.minZutrittsIdent);
            pOffset++;
            pPStm.setInt(pOffset, lNummernkreis.maxZutrittsIdent);
            pOffset++;
            pPStm.setInt(pOffset, lNummernkreis.letzteZutrittsIdent);
            pOffset++;

            if (pOffset - startOffset != anzfelder) {
                /*Nur wg. Überprüfung auf Programmierfehler - alle Felder berücksichtigt?*/
                CaBug.drucke("DbNummernkreis.fuellePreparedStatementKomplett 002");
            }

        } catch (SQLException e) {
            CaBug.drucke("DbNummernkreis.fuellePreparedStatementKomplett 001");
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
    public int insert(EclNummernkreis pNummernkreis) {

        int erg = 0;

        pNummernkreis.mandant = dbBundle.clGlobalVar.mandant;

        try {

            /*Felder Neuanlage füllen*/
            String lSql = "INSERT INTO " + dbBundle.getSchemaMandant() + "tbl_nummernkreise " + "("
                    + "mandant, schluessel, letzteInterneIdent, alphaZutrittsIdent,"
                    + "laengeZutrittsIdent, minZutrittsIdent, maxZutrittsIdent, letzteZutrittsIdent" + ") " + "VALUES ("
                    + "?, ?, ?, ?, " + "?, ?, ?, ? " + ")";
            PreparedStatement lPStm = verbindung.prepareStatement(lSql, ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);

            fuellePreparedStatementKomplett(lPStm, 1, pNummernkreis);

            erg = executeUpdate(lPStm);
            lPStm.close();
        } catch (Exception e2) {
            CaBug.drucke("DbNummernkreis.insert 001");
            System.err.println(" " + e2.getMessage());
        }

        if (erg == 0) {/*Fehler beim Einfügen - d.h. primaryKey bereits vorhanden*/
            return (-1);
        }

        return (1);
    }

    public int readZuSchluessel(int pSchluessel) {
        int anzInArray = 0;
        PreparedStatement lPStm = null;

        try {
            String lSql = "SELECT nk.* from " + dbBundle.getSchemaMandant() + "tbl_nummernkreise nk where "
                    + "nk.mandant=? AND " + "nk.schluessel=?;";
            lPStm = verbindung.prepareStatement(lSql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            lPStm.setInt(1, dbBundle.clGlobalVar.mandant);
            lPStm.setInt(2, pSchluessel);

            ResultSet lErgebnis = executeQuery(lPStm);
            lErgebnis.last();
            anzInArray = lErgebnis.getRow();
            lErgebnis.beforeFirst();

            ergebnisArray = new EclNummernkreis[anzInArray];

            int i = 0;
            while (lErgebnis.next() == true) {
                ergebnisArray[i] = this.decodeErgebnis(lErgebnis);
                i++;
            }
            lErgebnis.close();
            lPStm.close();

        } catch (Exception e) {
            CaBug.drucke("DbNummernkreis.readZuSchluessel 003");
            System.err.println(" " + e.getMessage());
            return (-1);
        }
        return (anzInArray);
    }

    /**Liest alle Mandanten ein, für die ein Eintrag in tbl_nummernkreis enthalten ist. Füllt im ergebnisarray NUR das Feld mandant!*/
    public int readAlleMandanten() {
        int anzInArray = 0;
        PreparedStatement lPStm = null;

        try {
            String lSql = "SELECT DISTINCT nk.mandant from " + dbBundle.getSchemaMandant() + "tbl_nummernkreise nk "
                    + "ORDER BY nk.mandant;";
            lPStm = verbindung.prepareStatement(lSql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);

            ResultSet lErgebnis = executeQuery(lPStm);
            lErgebnis.last();
            anzInArray = lErgebnis.getRow();
            lErgebnis.beforeFirst();

            ergebnisArray = new EclNummernkreis[anzInArray];

            int i = 0;
            while (lErgebnis.next() == true) {
                ergebnisArray[i] = new EclNummernkreis();
                ergebnisArray[i].mandant = lErgebnis.getInt("nk.mandant");
                i++;
            }
            lErgebnis.close();
            lPStm.close();

        } catch (Exception e) {
            CaBug.drucke("DbNummernkreis.readAlleMandanten 003");
            System.err.println(" " + e.getMessage());
            return (-1);
        }
        return (anzInArray);
    }

    public int delete(int pSchluessel) {

        try {

            String sql = "DELETE FROM " + dbBundle.getSchemaMandant()
                    + "tbl_nummernkreise WHERE schluessel=? AND mandant=? ";

            PreparedStatement pstm1 = verbindung.prepareStatement(sql);
            pstm1.setInt(1, pSchluessel);
            pstm1.setInt(2, dbBundle.clGlobalVar.mandant);

            int ergebnis1 = executeUpdate(pstm1);
            pstm1.close();
            if (ergebnis1 == 0) {
                return (CaFehler.pfXyWurdeVonAnderemBenutzerVeraendert);
            }
        } catch (Exception e1) {
            CaBug.drucke("DbNummernkreis.delete 001");
            System.err.println(" " + e1.getMessage());
            return (CaFehler.pfdXyBereitsVorhanden);
        }

        return (1);
    }

}
