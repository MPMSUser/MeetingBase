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
import de.meetingapps.meetingportal.meetComEntities.EclAktienregisterLoginDaten;

@Deprecated
public class DbAktienregisterLoginDaten {

    private Connection verbindung = null;
    private DbBundle dbBundle = null;

    public EclAktienregisterLoginDaten ergebnisArray[] = null;

    /*************************Initialisierung***************************/
    public DbAktienregisterLoginDaten(DbBundle pDbBundle) {
        /* Verbindung in lokale Daten eintragen*/
        if (pDbBundle == null) {
            CaBug.drucke("DbAktienregisterLoginDaten.init 001 - dbBundle nicht initialisiert");
            return;
        }
        if (pDbBundle.dbBasis == null) {
            CaBug.drucke("DbAktienregisterLoginDaten.init 002 - dbBasis nicht initialisiert");
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
    public EclAktienregisterLoginDaten ergebnisPosition(int pN) {
        if (ergebnisArray == null) {
            CaBug.drucke("DbAktienregisterLoginDaten.ergebnisPosition 001");
            return null;
        }
        if (pN < 0) {
            CaBug.drucke("DbAktienregisterLoginDaten.ergebnisPosition 002");
            return null;
        }
        if (pN >= ergebnisArray.length) {
            CaBug.drucke("DbAktienregisterLoginDaten.ergebnisPosition 003");
            return null;
        }
        return ergebnisArray[pN];
    }

    public int createTable() {
        int rc = 0;
        DbLowLevel lDbLowLevel = new DbLowLevel(dbBundle);
        rc = lDbLowLevel.createTable("CREATE TABLE " + dbBundle.getSchemaMandant() + "tbl_aktienregisterlogindaten ( "
                + "`mandant` int(11) NOT NULL, " + "`aktienregisterIdent` int(11) NOT NULL, "
                + "`db_version` bigint(20) DEFAULT NULL, " + "`loginKennung` char(20) DEFAULT NULL, "
                + "`passwortVerschluesselt` char(64) DEFAULT NULL, " + "`passwortInitial` char(64) DEFAULT NULL, "
                + "`anmeldenUnzulaessig` int(11) NULL DEFAULT 0, "
                + "`dauerhafteRegistrierungUnzulaessig` int(11) NULL DEFAULT 0, "
                + "PRIMARY KEY (`aktienregisterIdent`,`mandant`), " + "KEY `IDX_loginKennung` (`loginKennung`) "
                + ") ");
        return rc;
    }

    /**************************deleteAll Löschen aller Datensätze  eines Mandanten****************/
    public int deleteAll() {
        int erg = 0;

        try {

//            String sql1 = "DELETE FROM " + dbBundle.getSchemaMandant()
//                    + "tbl_aktienregisterLoginDaten where mandant=?;";
            String sql1 = "TRUNCATE " + dbBundle.getSchemaMandant()
                + "tbl_aktienregisterLoginDaten;";
            PreparedStatement pstm1 = verbindung.prepareStatement(sql1);
//            pstm1.setInt(1, dbBundle.clGlobalVar.mandant);

            erg = 0; /*Falls Duplicate Key, dann wird exception geschmissen und erg wohl nicht gefüllt!*/
            erg = pstm1.executeUpdate();

            pstm1.close();
        } catch (Exception e2) {
            CaBug.drucke("DbAktienregisterLoginDaten.deleteAll 001");
            System.err.println(" " + e2.getMessage());
            return (erg);
        }

        return 1;
    }

    /**************************setzt aktuelle Mandantennummer bei allen Datensätzen****************/
    public int updateMandant() {
        return dbBundle.dbLowLevel.rawUpdateMandant(dbBundle.getSchemaMandant() + "tbl_aktienregisterLoginDaten");
    }

    /********** dekodiert die aktuelle Position aus ergebnis in EclAktienregisterEintrag und gibt dieses zurück******/
    EclAktienregisterLoginDaten decodeErgebnis(ResultSet pErgebnis) {

        EclAktienregisterLoginDaten lAktienregisterLoginDaten = new EclAktienregisterLoginDaten();

        try {

            lAktienregisterLoginDaten.mandant = pErgebnis.getInt("arel.mandant");
            lAktienregisterLoginDaten.aktienregisterIdent = pErgebnis.getInt("arel.aktienregisterIdent");
            lAktienregisterLoginDaten.db_version = pErgebnis.getLong("arel.db_version");

            lAktienregisterLoginDaten.loginKennung = pErgebnis.getString("arel.loginKennung");
            lAktienregisterLoginDaten.passwortVerschluesselt = pErgebnis.getString("arel.passwortVerschluesselt");
            lAktienregisterLoginDaten.passwortInitial = pErgebnis.getString("arel.passwortInitial");

            lAktienregisterLoginDaten.anmeldenUnzulaessig = pErgebnis.getInt("arel.anmeldenUnzulaessig");
            lAktienregisterLoginDaten.dauerhafteRegistrierungUnzulaessig = pErgebnis
                    .getInt("arel.dauerhafteRegistrierungUnzulaessig");

        } catch (Exception e) {
            CaBug.drucke("DbAktienregisterLoginDaten.decodeErgebnis 001");
            System.err.println(" " + e.getMessage());
        }

        return lAktienregisterLoginDaten;
    }

    /********************* Fuellen Prepared Statement mit allen Feldern, beginnend bei offset.*****************
     * Kann sowohl für Insert, als auch für update verwendet werden.
     * 
     * offset= Startposition des ersten Feldes (also z.B. 1)
     */
    private int anzfelder = 8; /*Anpassen auf Anzahl der Felder pro Datensatz*/

    void fuellePreparedStatementKomplett(PreparedStatement pPStm, int pOffset,
            EclAktienregisterLoginDaten pAktienregisterLoginDaten) {

        int startOffset = pOffset; /*Nur erforderlich zum Überprüfen von Programmierfehlern - Feldanzahl*/

        try {
            pPStm.setInt(pOffset, pAktienregisterLoginDaten.mandant);
            pOffset++;
            pPStm.setInt(pOffset, pAktienregisterLoginDaten.aktienregisterIdent);
            pOffset++;
            pPStm.setLong(pOffset, pAktienregisterLoginDaten.db_version);
            pOffset++;

            pPStm.setString(pOffset, pAktienregisterLoginDaten.loginKennung);
            pOffset++;
            pPStm.setString(pOffset, pAktienregisterLoginDaten.passwortVerschluesselt);
            pOffset++;
            pPStm.setString(pOffset, pAktienregisterLoginDaten.passwortInitial);
            pOffset++;

            pPStm.setInt(pOffset, pAktienregisterLoginDaten.anmeldenUnzulaessig);
            pOffset++;
            pPStm.setInt(pOffset, pAktienregisterLoginDaten.dauerhafteRegistrierungUnzulaessig);
            pOffset++;

            if (pOffset - startOffset != anzfelder) {
                /*Nur wg. Überprüfung auf Programmierfehler - alle Felder berücksichtigt?*/
                CaBug.drucke("DbAktienregisterLoginDaten.fuellePreparedStatementKomplett 002");
            }

        } catch (SQLException e) {
            CaBug.drucke("DbAktienregisterLoginDaten.fuellePreparedStatementKomplett 001");
            e.printStackTrace();
        }

    }

    /**Insert
     * 
     * Feld mandant wird von dieser Funktion immer selbstständig belegt.
     * aktienregisterIdent muß gefüllt sein (mit entsprechender Ident aus aktienregister!)
     * 
     * Returnwert:
     * =1 => Insert erfolgreich
     * ansonsten: Fehler
     */
    public int insert(EclAktienregisterLoginDaten pAktienregisterLoginDaten) {

        int erg = 0;

        pAktienregisterLoginDaten.mandant = dbBundle.clGlobalVar.mandant;

        /* Satz einfügen: 
         * Verarbeitungshinweis: 
         * 	>	nachdem InterneIdent immer eindeutig vergeben werden, ist prinzipiell eine "Doppeleinfügung"
         * 		von InterneIdent nicht möglich. Sollte es dazu kommen, ist das immer ein Programmfehler!
         */

        try {

            /*Felder Neuanlage füllen*/
            String lSql = "INSERT INTO " + dbBundle.getSchemaMandant() + "tbl_aktienregisterLoginDaten " + "("
                    + "mandant, aktienregisterIdent, db_version, "
                    + "loginKennung, passwortVerschluesselt, passwortInitial, anmeldenUnzulaessig, dauerhafteRegistrierungUnzulaessig "
                    + ")" + "VALUES (" + "?, ?, ?, " + "?, ?, ?, ?, ? " + ")";
            PreparedStatement lPStm = verbindung.prepareStatement(lSql, ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);

            fuellePreparedStatementKomplett(lPStm, 1, pAktienregisterLoginDaten);

            erg = lPStm.executeUpdate();
            lPStm.close();
        } catch (Exception e2) {
            CaBug.drucke("DbAktienregisterLoginDaten.insert 001");
            System.err.println(" " + e2.getMessage());
        }

        if (erg == 0) {/*Fehler beim Einfügen - d.h. primaryKey bereits vorhanden*/
            return (-1);
        }

        return (1);
    }

    /**Einlesen des Satzes mit aktienregisterLoginDaten.
     * Suche entweder nach aktienregisterIdent, oder (wenn dies =0) dann nach loginKennung*/
    public int read(EclAktienregisterLoginDaten pAktienregisterLoginDaten) {
        int anzInArray = 0;
        PreparedStatement lPStm = null;

        try {
            String lSql = "SELECT * from " + dbBundle.getSchemaMandant() + "tbl_aktienregisterLoginDaten arel where "
                    + "arel.mandant=? AND ";
            if (pAktienregisterLoginDaten.aktienregisterIdent != 0) {
                lSql = lSql + "arel.aktienregisterIdent=?";
            } else {
                lSql = lSql + "arel.loginKennung=?";
            }
            lSql = lSql + " ORDER BY arel.aktienregisterIdent;";
            lPStm = verbindung.prepareStatement(lSql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            lPStm.setInt(1, dbBundle.clGlobalVar.mandant);
            if (pAktienregisterLoginDaten.aktienregisterIdent != 0) {
                lPStm.setInt(2, pAktienregisterLoginDaten.aktienregisterIdent);
            } else {
                lPStm.setString(2, pAktienregisterLoginDaten.loginKennung);
            }

            ResultSet lErgebnis = lPStm.executeQuery();
            lErgebnis.last();
            anzInArray = lErgebnis.getRow();
            lErgebnis.beforeFirst();

            ergebnisArray = new EclAktienregisterLoginDaten[anzInArray];

            int i = 0;
            while (lErgebnis.next() == true) {
                ergebnisArray[i] = this.decodeErgebnis(lErgebnis);
                i++;
            }
            lErgebnis.close();
            lPStm.close();

        } catch (Exception e) {
            CaBug.drucke("DbAktienregisterLoginDaten.read 003");
            System.err.println(" " + e.getMessage());
            return (-1);
        }
        return (anzInArray);
    }

    public int readIdent(int pAktienregisterIdent) {
        EclAktienregisterLoginDaten lAktienregisterLoginDaten = new EclAktienregisterLoginDaten();
        lAktienregisterLoginDaten.aktienregisterIdent = pAktienregisterIdent;
        return read(lAktienregisterLoginDaten);
    }

    /**Update. Versionsnummer wird um 1 hochgezählt
     * 
     * Feld mandant wird von dieser Funktion immer selbstständig belegt.
     * 
     * Zum Sicherstellen der Multiuserfähigkeit muß in jedem Fall der rc CaFehler.pfXyWurdeVonAnderemBenutzerVeraendert
     * nach Aufruf dieser Funktion abgefangen werden.
     * Ansonsten: rc=1 => ok, ansonsten Fehler
     */
    public int update(EclAktienregisterLoginDaten pAktienregisterLoginDaten) {

        pAktienregisterLoginDaten.db_version++;

        try {

            String lSql = "UPDATE " + dbBundle.getSchemaMandant() + "tbl_aktienregisterLoginDaten SET "
                    + "mandant=?, aktienregisterIdent=?, db_version=?, "
                    + "loginKennung=?, passwortVerschluesselt=?, passwortInitial=?, anmeldenUnzulaessig=?, dauerhafteRegistrierungUnzulaessig=? "
                    + "WHERE " + "aktienregisterIdent=? AND " + "db_version=? AND mandant=?";

            PreparedStatement lPStm = verbindung.prepareStatement(lSql);
            fuellePreparedStatementKomplett(lPStm, 1, pAktienregisterLoginDaten);
            lPStm.setInt(anzfelder + 1, pAktienregisterLoginDaten.aktienregisterIdent);
            lPStm.setLong(anzfelder + 2, pAktienregisterLoginDaten.db_version - 1);
            lPStm.setLong(anzfelder + 3, dbBundle.clGlobalVar.mandant);

            int ergebnis = lPStm.executeUpdate();
            lPStm.close();
            if (ergebnis == 0) {
                return (CaFehler.pfXyWurdeVonAnderemBenutzerVeraendert);
            }
        } catch (Exception e1) {
            CaBug.drucke("DbAktienregisterLoginDaten.update 001");
            System.err.println(" " + e1.getMessage());
            return (CaFehler.pfdXyBereitsVorhanden);
        }

        return (1);
    }

    public int delete(int ident) {

        try {

            String sql = "DELETE FROM " + dbBundle.getSchemaMandant()
                    + "tbl_aktienregisterLoginDaten WHERE aktienregisterIdent=? AND mandant=? ";

            PreparedStatement pstm1 = verbindung.prepareStatement(sql);
            pstm1.setInt(1, ident);
            pstm1.setInt(2, dbBundle.clGlobalVar.mandant);

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

    /**
     * ++++++++++++++++++++++++++++++++++++++++++++++++ 
     * Benötigte dbAbfragen für Aufbereitung und Import
     * 
     * @throws SQLException
     */

    // Insert nach Aufbereitung für alle neuen
    
//    public int insertAufbereitung() {
//
//        int erg = 0;
//
//        try {
//
//            String sql = "insert into " + dbBundle.getSchemaMandant()
//                    + "tbl_aktienregisterlogindaten (mandant, aktienregisterIdent, db_version, "
//                    + "loginKennung, passwortVerschluesselt, passwortInitial, anmeldenUnzulaessig, dauerhafteRegistrierungUnzulaessig) values (?,?,?,?,?,?,?,?)";
//
//            PreparedStatement pstmt = verbindung.prepareStatement(sql);
//
//            for (int i = 0; i < EclAktienregisterListen.registerListeUpdateLogin.size(); i++) {
//                fuellePreparedStatementKomplett(pstmt, 1, EclAktienregisterListen.registerListeUpdateLogin.get(i));
//                //				fuellePreparedStatementInsertAufbereitung(pstmt, i, 1);
//                pstmt.executeUpdate();
//                erg++;
//            }
//
//            return erg;
//
//        } catch (SQLException e) {
//            System.out.println(e.toString());
//            return -1;
//        }
//
//    }

    /*
    private void fuellePreparedStatementInsertAufbereitung(PreparedStatement pstmt, int listennummer, int offset) throws SQLException {
    
    	try {
    
    		pstmt.setInt(offset, EclAktienregisterListen.registerListeUpdateLogin.get(listennummer).mandant);
    		offset++;
    		pstmt.setInt(offset, EclAktienregisterListen.registerListeUpdateLogin.get(listennummer).aktienregisterIdent);
    		offset++;
    		pstmt.setLong(offset, EclAktienregisterListen.registerListeUpdateLogin.get(listennummer).db_version);
    		offset++;
    		pstmt.setString(offset, EclAktienregisterListen.registerListeUpdateLogin.get(listennummer).loginKennung);
    		offset++;
    		pstmt.setString(offset, EclAktienregisterListen.registerListeUpdateLogin.get(listennummer).passwortVerschluesselt);
    		offset++;
    		pstmt.setString(offset, EclAktienregisterListen.registerListeUpdateLogin.get(listennummer).passwortInitial);
    		offset++;
    
    	} catch (SQLException e) {
    		System.out.println(e.toString());
    	}
    }
    
    */
}
