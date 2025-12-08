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
import de.meetingapps.meetingportal.meetComEntities.EclAktienregisterErgaenzung;

public class DbAktienregisterErgaenzung  extends DbRootExecute {

    int logDrucken=3;
    
    private Connection verbindung = null;
    private DbBundle dbBundle = null;

    public EclAktienregisterErgaenzung ergebnisArray[] = null;

    /*************************Initialisierung***************************/
    public DbAktienregisterErgaenzung(DbBundle pDbBundle) {
        /* Verbindung in lokale Daten eintragen*/
        if (pDbBundle == null) {
            CaBug.drucke("DbAktienregisterErgaenzung.init 001 - dbBundle nicht initialisiert");
            return;
        }
        if (pDbBundle.dbBasis == null) {
            CaBug.drucke("DbAktienregisterErgaenzung.init 002 - dbBasis nicht initialisiert");
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
    public EclAktienregisterErgaenzung ergebnisPosition(int pN) {
        if (ergebnisArray == null) {
            CaBug.drucke("DbAktienregisterErgaenzung.ergebnisPosition 001");
            return null;
        }
        if (pN < 0) {
            CaBug.drucke("DbAktienregisterErgaenzung.ergebnisPosition 002");
            return null;
        }
        if (pN >= ergebnisArray.length) {
            CaBug.drucke("DbAktienregisterErgaenzung.ergebnisPosition 003");
            return null;
        }
        return ergebnisArray[pN];
    }

    /**************************deleteAll Löschen aller Datensätze  eines Mandanten****************/
    public int deleteAll() {
//        return dbBundle.dbLowLevel.deleteMandant(
//                "DELETE FROM " + dbBundle.getSchemaMandant() + "tbl_aktienregisterErgaenzung where mandant=?;");
        return dbBundle.dbLowLevel.truncMandant(
                "TRUNCATE " + dbBundle.getSchemaMandant() + "tbl_aktienregisterErgaenzung");
    }

    /************Neuanlegen Table******************************/
    public int createTable() {
        int rc = 0;
        DbLowLevel lDbLowLevel = new DbLowLevel(dbBundle);
        String createString = "CREATE TABLE " + dbBundle.getSchemaMandant() + "tbl_aktienregisterErgaenzung ( "
                + "`mandant` int(11) NOT NULL, " + "`aktienregisterIdent` int(11) NOT NULL, "
                + "`satzNummer` int(11) NOT NULL, " + "`db_version` bigint(20) DEFAULT NULL ";
        for (int i = 0; i < 30; i++) {
            createString = createString + ", `ergaenzungLangString" + Integer.toString(i)
                    + "` varchar(120) DEFAULT NULL" + ", `ergaenzungKurzString" + Integer.toString(i)
                    + "` varchar(40) DEFAULT NULL";
        }
        for (int i = 0; i < 30; i++) {
            createString = createString + ", `ergaenzungKennzeichen" + Integer.toString(i) + "` int(11) DEFAULT NULL";
        }
        createString = createString + ", PRIMARY KEY (`mandant`, `aktienregisterIdent`, `satzNummer`) " + ")  ";
        rc = lDbLowLevel.createTable(createString);
        CaBug.druckeLog("DbAktienregisterErgaenzung create table rc=" + rc, logDrucken, 10);
        return rc;
    }

    /********** dekodiert die aktuelle Position aus ergebnis in EclAktienregisterEintrag und gibt dieses zurück******/
    EclAktienregisterErgaenzung decodeErgebnis(ResultSet pErgebnis) {

        EclAktienregisterErgaenzung lAktienregisterErgaenzung = new EclAktienregisterErgaenzung();

        try {
            lAktienregisterErgaenzung.mandant = pErgebnis.getInt("areerg.mandant");
            lAktienregisterErgaenzung.aktienregisterIdent = pErgebnis.getInt("areerg.aktienregisterIdent");
            lAktienregisterErgaenzung.satzNummer = pErgebnis.getInt("areerg.satzNummer");
            lAktienregisterErgaenzung.db_version = pErgebnis.getLong("areerg.db_version");
            for (int i = 0; i < 30; i++) {
                lAktienregisterErgaenzung.ergaenzungLangString[i] = pErgebnis
                        .getString("areerg.ergaenzungLangString" + Integer.toString(i));
                lAktienregisterErgaenzung.ergaenzungKurzString[i] = pErgebnis
                        .getString("areerg.ergaenzungKurzString" + Integer.toString(i));
            }
            for (int i = 0; i < 30; i++) {
                lAktienregisterErgaenzung.ergaenzungKennzeichen[i] = pErgebnis
                        .getInt("areerg.ergaenzungKennzeichen" + Integer.toString(i));
            }

        } catch (Exception e) {
            CaBug.drucke("DbAktienregisterErgaenzung.decodeErgebnis 001");
            System.err.println(" " + e.getMessage());
        }

        return lAktienregisterErgaenzung;
    }

    /********************* Fuellen Prepared Statement mit allen Feldern, beginnend bei offset.*****************
     * Kann sowohl für Insert, als auch für update verwendet werden.
     * 
     * offset= Startposition des ersten Feldes (also z.B. 1)
     */
    private int anzfelder = 94; /*Anpassen auf Anzahl der Felder pro Datensatz  ident nicht mit enthalten!*/

    void fuellePreparedStatementKomplett(PreparedStatement pPStm, int pOffset,
            EclAktienregisterErgaenzung lAktienregisterErgaenzung) {

        int startOffset = pOffset; /*Nur erforderlich zum Überprüfen von Programmierfehlern - Feldanzahl*/

        try {
            pPStm.setInt(pOffset, lAktienregisterErgaenzung.mandant);
            pOffset++;
            pPStm.setInt(pOffset, lAktienregisterErgaenzung.aktienregisterIdent);
            pOffset++;
            pPStm.setInt(pOffset, lAktienregisterErgaenzung.satzNummer);
            pOffset++;
            pPStm.setLong(pOffset, lAktienregisterErgaenzung.db_version);
            pOffset++;
            for (int i = 0; i < 30; i++) {
                pPStm.setString(pOffset, lAktienregisterErgaenzung.ergaenzungLangString[i]);
                pOffset++;
                pPStm.setString(pOffset, lAktienregisterErgaenzung.ergaenzungKurzString[i]);
                pOffset++;
            }
            for (int i = 0; i < 30; i++) {
                pPStm.setInt(pOffset, lAktienregisterErgaenzung.ergaenzungKennzeichen[i]);
                pOffset++;
            }

            if (pOffset - startOffset != anzfelder) {
                /*Nur wg. Überprüfung auf Programmierfehler - alle Felder berücksichtigt?*/
                CaBug.drucke("DbAktienregisterErgaenzung.fuellePreparedStatementKomplett 002");
            }

        } catch (SQLException e) {
            CaBug.drucke("DbAktienregisterErgaenzung.fuellePreparedStatementKomplett 001");
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
    public int insert(EclAktienregisterErgaenzung pVerarbeitungsLauf) {

        int erg = 0;

        try {
            /*Felder Neuanlage füllen*/
            String lSql = "INSERT INTO " + dbBundle.getSchemaMandant() + "tbl_aktienregisterErgaenzung " + "("
                    + "mandant, " + "aktienregisterIdent, satzNummer, db_version";
            for (int i = 0; i < 30; i++) {
                lSql = lSql + ", ergaenzungLangString" + Integer.toString(i) + ", ergaenzungKurzString"
                        + Integer.toString(i);
            }
            for (int i = 0; i < 30; i++) {
                lSql = lSql + ", ergaenzungKennzeichen" + Integer.toString(i);
            }
            lSql = lSql + " )" + "VALUES (" + "?, " + "?, ?, ?";
            for (int i = 0; i < 90; i++) {
                lSql = lSql + ", ?";
            }
            lSql = lSql + ")";
            PreparedStatement lPStm = verbindung.prepareStatement(lSql, ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
            fuellePreparedStatementKomplett(lPStm, 1, pVerarbeitungsLauf);
            erg = executeUpdate(lPStm);
            lPStm.close();
        } catch (Exception e2) {
            CaBug.drucke("DbAktienregisterErgaenzung.insert 001");
            System.err.println(" " + e2.getMessage());
        }

        if (erg == 0) {/*Fehler beim Einfügen - d.h. z.B. primaryKey bereits vorhanden*/
            return (-1);
        }

        return (1);
    }

    /**Einlesen eines bestimmten Laufs mit  ident*/
    public int readZuident(int pIdent) {
        int anzInArray = 0;
        PreparedStatement lPStm = null;

        try {
            String lSql = "SELECT * from " + dbBundle.getSchemaMandant()
                    + "tbl_aktienregisterErgaenzung areerg WHERE areerg.aktienregisterIdent=? ORDER BY areerg.satzNummer;";
            lPStm = verbindung.prepareStatement(lSql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            lPStm.setInt(1, pIdent);

            ResultSet lErgebnis = executeQuery(lPStm);
            lErgebnis.last();
            anzInArray = lErgebnis.getRow();
            lErgebnis.beforeFirst();

            ergebnisArray = new EclAktienregisterErgaenzung[anzInArray];

            int i = 0;
            while (lErgebnis.next() == true) {
                ergebnisArray[i] = this.decodeErgebnis(lErgebnis);
                i++;
            }

            lErgebnis.close();
            lPStm.close();

        } catch (Exception e) {
            CaBug.drucke("DbAktienregisterErgaenzung.readZuident 003");
            System.err.println(" " + e.getMessage());
            return (-1);
        }
        return (anzInArray);
    }

    /**Einlesen eines bestimmten Laufs mit  ident*/
    public int readAll() {
        int anzInArray = 0;
        PreparedStatement lPStm = null;

        try {
            String lSql = "SELECT * from " + dbBundle.getSchemaMandant()
                    + "tbl_aktienregisterErgaenzung areerg WHERE areerg.satzNummer=0;";
            lPStm = verbindung.prepareStatement(lSql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);

            ResultSet lErgebnis = executeQuery(lPStm);
            lErgebnis.last();
            anzInArray = lErgebnis.getRow();
            lErgebnis.beforeFirst();

            ergebnisArray = new EclAktienregisterErgaenzung[anzInArray];

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

    /**Einlesen aller Sätze, die sich zur 1. Dialogveranstaltung angemeldet oder abgemeldet haben*/
    public int readAngemeldeteDialogveranstaltung(int pVeranstaltungsnummer) {
        int anzInArray = 0;
        PreparedStatement lPStm = null;

        try {
            String lSql = "SELECT * from " + dbBundle.getSchemaMandant()
                    + "tbl_aktienregisterErgaenzung areerg WHERE ergaenzungKurzString"+Integer.toString(pVeranstaltungsnummer)+" LIKE '1%' "
                            + "OR ergaenzungKurzString"+Integer.toString(pVeranstaltungsnummer)+" LIKE '2%' ";
            lPStm = verbindung.prepareStatement(lSql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);

            ResultSet lErgebnis = executeQuery(lPStm);
            lErgebnis.last();
            anzInArray = lErgebnis.getRow();
            lErgebnis.beforeFirst();

            ergebnisArray = new EclAktienregisterErgaenzung[anzInArray];

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
    public int update(EclAktienregisterErgaenzung lAktienregisterErgaenzung) {

        try {
            String lSql = "UPDATE " + dbBundle.getSchemaMandant() + "tbl_aktienregisterErgaenzung SET " + "mandant=?, "
                    + "aktienregisterIdent=?, satzNummer=?, db_version=?";
            for (int i = 0; i < 30; i++) {
                lSql = lSql + ", ergaenzungLangString" + Integer.toString(i) + "=?" + ", ergaenzungKurzString"
                        + Integer.toString(i) + "=?";
            }
            for (int i = 0; i < 30; i++) {
                lSql = lSql + ", ergaenzungKennzeichen" + Integer.toString(i) + "=?";
            }
            lSql = lSql + " ";
            lSql = lSql + " WHERE aktienregisterIdent=? AND satzNummer=? ";

            PreparedStatement lPStm = verbindung.prepareStatement(lSql);
            fuellePreparedStatementKomplett(lPStm, 1, lAktienregisterErgaenzung);
            lPStm.setInt(anzfelder + 1, lAktienregisterErgaenzung.aktienregisterIdent);
            lPStm.setInt(anzfelder + 2, lAktienregisterErgaenzung.satzNummer);

            int ergebnis = executeUpdate(lPStm);
            lPStm.close();
            if (ergebnis == 0) {
                return (CaFehler.pfXyWurdeVonAnderemBenutzerVeraendert);
            }
        } catch (Exception e1) {
            CaBug.drucke("DbAktienregisterErgaenzung.update 001");
            System.err.println(" " + e1.getMessage());
            return (CaFehler.pfdXyBereitsVorhanden);
        }

        return (1);
    }

    public int updateMandant() {
        return dbBundle.dbLowLevel.rawUpdateMandant(dbBundle.getSchemaMandant() + "tbl_aktienregisterErgaenzung");
    }

    public int delete(int pIdent) {

        try {

            String sql = "DELETE FROM " + dbBundle.getSchemaMandant() + "tbl_aktienregisterErgaenzung WHERE ident=? ";

            PreparedStatement pstm1 = verbindung.prepareStatement(sql);
            pstm1.setInt(1, pIdent);

            int ergebnis1 = executeUpdate(pstm1);
            pstm1.close();
            if (ergebnis1 == 0) {
                return (CaFehler.pfXyWurdeVonAnderemBenutzerVeraendert);
            }
        } catch (Exception e1) {
            CaBug.drucke("DbAktienregisterErgaenzung.delete 001");
            System.err.println(" " + e1.getMessage());
            return (CaFehler.pfdXyBereitsVorhanden);
        }

        return (1);
    }

    public int setzeZurueck(int pIdent) {
        try {
            String lSql = "UPDATE " + dbBundle.getSchemaMandant() + "tbl_aktienregisterErgaenzung SET "; 
            for (int i = 0; i < 24; i++) {
                lSql = lSql + "ergaenzungKurzString" + Integer.toString(i) + "='', ";
            }
            for (int i = 0; i < 24; i++) {
                lSql = lSql + "ergaenzungKennzeichen" + Integer.toString(i) + "=0 ";
                if (i<23) {
                    lSql = lSql + ", ";
                }
            }
           lSql = lSql + " WHERE aktienregisterIdent=? ";

            PreparedStatement lPStm = verbindung.prepareStatement(lSql);
            lPStm.setInt(1, pIdent);

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
}
