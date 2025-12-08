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
import de.meetingapps.meetingportal.meetComEntities.EclInstiProv;

public class DbInstiProv {

    private int logDrucken=10;
    
    private Connection verbindung = null;
    private DbBundle dbBundle = null;

    public EclInstiProv ergebnisArray[] = null;

    /*************************Initialisierung***************************/
    public DbInstiProv(DbBundle pDbBundle) {
        /* Verbindung in lokale Daten eintragen*/
        if (pDbBundle == null) {
            CaBug.drucke("DbInstiProv.init 001 - dbBundle nicht initialisiert");
            return;
        }
        if (pDbBundle.dbBasis == null) {
            CaBug.drucke("DbInstiProv.init 002 - dbBasis nicht initialisiert");
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
    public EclInstiProv ergebnisPosition(int pN) {
        if (ergebnisArray == null) {
            CaBug.drucke("DbInstiProv.ergebnisPosition 001");
            return null;
        }
        if (pN < 0) {
            CaBug.drucke("DbInstiProv.ergebnisPosition 002");
            return null;
        }
        if (pN >= ergebnisArray.length) {
            CaBug.drucke("DbInstiProv.ergebnisPosition 003");
            return null;
        }
        return ergebnisArray[pN];
    }

    /**************************deleteAll Löschen aller Datensätze  eines Mandanten****************/
    public int deleteAll() {
        return dbBundle.dbLowLevel
                .deleteMandant("DELETE FROM " + dbBundle.getSchemaMandant() + "tbl_instiProv where mandant=?;");
    }

    /*******Checken, ob table überhaupt vorhanden ist***************************/
    public boolean checkTableVorhanden() {
        DbLowLevel lDbLowLevel = new DbLowLevel(dbBundle);
        return lDbLowLevel.checkTableVorhanden("SELECT * from tbl_instiProv WHERE mandant=0; ");
    }

    /************Neuanlegen Table******************************/
    public int createTable() {
        int rc = 0;
        DbLowLevel lDbLowLevel = new DbLowLevel(dbBundle);
        String lSql = "CREATE TABLE " + dbBundle.getSchemaMandant() + "tbl_instiProv ( "
                + "`mandant` int(11) NOT NULL, " + "`ident` int(11) NOT NULL, "
                + "`verarbeitungslauf` int(11) NOT NULL, " + "`quellDateiname` varchar(100) DEFAULT NULL, "
                + "`aktionaersnummer` varchar(20) DEFAULT NULL, " + "`aktionaersname` varchar(200) DEFAULT NULL, "
                + "`aktienzahl` bigint(20) DEFAULT NULL, " + "`sollFixAnmeldung` int(11) NOT NULL, "
                + "`gesamtMarkierung` int(11) NOT NULL, ";
        for (int i = 1; i <= 200; i++) {
            lSql = lSql + "`einzelMarkierungen" + Integer.toString(i) + "` int(11) NOT NULL, ";
        }

        lSql = lSql + "`sollGewaehlteSammelkarteIdent` int(11) NOT NULL, "
                + "`sollVorrangFremdbesitz` int(11) NOT NULL, " + "`korrFixAnmeldung` int(11) NOT NULL, "
                + "`korrGewaehlteSammelkarteIdent` int(11) NOT NULL, " + "`korrVorrangFremdbesitz` int(11) NOT NULL, "
                + "`aktionaersnameAR` varchar(200) DEFAULT NULL, " + "`aktienzahlAR` bigint(20) DEFAULT NULL, "
                + "`codeVerarbeitetTest` int(11) NOT NULL, " + "`textVerarbeitetTest` varchar(400) DEFAULT NULL, "
                + "`codeVerarbeitetVerarbeitung` int(11) NOT NULL, "
                + "`textVerarbeitetVerarbeitung` varchar(400) DEFAULT NULL, " + "`sammelkartenart` int(11) NOT NULL, "
                + "PRIMARY KEY (`mandant`, `ident`) " + ")  ";
        rc = lDbLowLevel.createTable(lSql);
        CaBug.druckeLog("DbInstiProv create table rc=" + rc, logDrucken, 10);
        return rc;
    }

    /********** dekodiert die aktuelle Position aus ergebnis in EclAktienregisterEintrag und gibt dieses zurück******/
    EclInstiProv decodeErgebnis(ResultSet pErgebnis) {

        EclInstiProv lInstiProv = new EclInstiProv();

        try {
            lInstiProv.mandant = pErgebnis.getInt("inp.mandant");
            lInstiProv.ident = pErgebnis.getInt("inp.ident");
            lInstiProv.verarbeitungslauf = pErgebnis.getInt("inp.verarbeitungslauf");
            lInstiProv.quellDateiname = pErgebnis.getString("inp.quellDateiname");
            lInstiProv.aktionaersnummer = pErgebnis.getString("inp.aktionaersnummer");
            lInstiProv.aktionaersname = pErgebnis.getString("inp.aktionaersname");
            lInstiProv.aktienzahl = pErgebnis.getLong("inp.aktienzahl");
            lInstiProv.sollFixAnmeldung = pErgebnis.getInt("inp.sollFixAnmeldung");
            lInstiProv.gesamtMarkierung = pErgebnis.getInt("inp.gesamtMarkierung");
            for (int i = 1; i <= 200; i++) {
                lInstiProv.einzelMarkierungen[i] = pErgebnis
                        .getInt("inp.einzelMarkierungen" + Integer.toString(i) + "");
            }
            lInstiProv.sollGewaehlteSammelkarteIdent = pErgebnis.getInt("inp.sollGewaehlteSammelkarteIdent");
            lInstiProv.sollVorrangFremdbesitz = pErgebnis.getInt("inp.sollVorrangFremdbesitz");
            lInstiProv.korrFixAnmeldung = pErgebnis.getInt("inp.korrFixAnmeldung");
            lInstiProv.korrGewaehlteSammelkarteIdent = pErgebnis.getInt("inp.korrGewaehlteSammelkarteIdent");
            lInstiProv.korrVorrangFremdbesitz = pErgebnis.getInt("inp.korrVorrangFremdbesitz");
            lInstiProv.aktionaersnameAR = pErgebnis.getString("inp.aktionaersnameAR");
            lInstiProv.aktienzahlAR = pErgebnis.getLong("inp.aktienzahlAR");
            lInstiProv.codeVerarbeitetTest = pErgebnis.getInt("inp.codeVerarbeitetTest");
            lInstiProv.textVerarbeitetTest = pErgebnis.getString("inp.textVerarbeitetTest");
            lInstiProv.codeVerarbeitetVerarbeitung = pErgebnis.getInt("inp.codeVerarbeitetVerarbeitung");
            lInstiProv.textVerarbeitetVerarbeitung = pErgebnis.getString("inp.textVerarbeitetVerarbeitung");
            lInstiProv.sammelkartenart = pErgebnis.getInt("inp.sammelkartenart");

            /*XXX*/

            if (lInstiProv.korrFixAnmeldung == -1) {
                lInstiProv.istFixAnmeldung = lInstiProv.sollFixAnmeldung;
            } else {
                lInstiProv.istFixAnmeldung = lInstiProv.korrFixAnmeldung;
            }

            if (lInstiProv.korrGewaehlteSammelkarteIdent == -1) {
                lInstiProv.istGewaehlteSammelkarteIdent = lInstiProv.sollGewaehlteSammelkarteIdent;
            } else {
                lInstiProv.istGewaehlteSammelkarteIdent = lInstiProv.korrGewaehlteSammelkarteIdent;
            }

            if (lInstiProv.korrVorrangFremdbesitz == -1) {
                lInstiProv.istVorrangFremdbesitz = lInstiProv.sollVorrangFremdbesitz;
            } else {
                lInstiProv.istVorrangFremdbesitz = lInstiProv.korrVorrangFremdbesitz;
            }

        } catch (Exception e) {
            CaBug.drucke("DbInstiProv.decodeErgebnis 001");
            System.err.println(" " + e.getMessage());
        }

        return lInstiProv;
    }

    /********************* Fuellen Prepared Statement mit allen Feldern, beginnend bei offset.*****************
     * Kann sowohl für Insert, als auch für update verwendet werden.
     * 
     * offset= Startposition des ersten Feldes (also z.B. 1)
     */
    private int anzfelder = 221; /*Anpassen auf Anzahl der Felder pro Datensatz*/

    void fuellePreparedStatementKomplett(PreparedStatement pPStm, int pOffset, EclInstiProv pInstiProv) {

        int startOffset = pOffset; /*Nur erforderlich zum Überprüfen von Programmierfehlern - Feldanzahl*/

        try {
            pPStm.setInt(pOffset, pInstiProv.mandant);
            pOffset++;
            pPStm.setInt(pOffset, pInstiProv.ident);
            pOffset++;
            pPStm.setInt(pOffset, pInstiProv.verarbeitungslauf);
            pOffset++;
            pPStm.setString(pOffset, pInstiProv.quellDateiname);
            pOffset++;
            pPStm.setString(pOffset, pInstiProv.aktionaersnummer);
            pOffset++;
            pPStm.setString(pOffset, pInstiProv.aktionaersname);
            pOffset++;
            pPStm.setLong(pOffset, pInstiProv.aktienzahl);
            pOffset++;
            pPStm.setInt(pOffset, pInstiProv.sollFixAnmeldung);
            pOffset++;
            pPStm.setInt(pOffset, pInstiProv.gesamtMarkierung);
            pOffset++;
            for (int i = 1; i <= 200; i++) {
                pPStm.setInt(pOffset, pInstiProv.einzelMarkierungen[i]);
                pOffset++;
            }
            pPStm.setInt(pOffset, pInstiProv.sollGewaehlteSammelkarteIdent);
            pOffset++;
            pPStm.setInt(pOffset, pInstiProv.sollVorrangFremdbesitz);
            pOffset++;
            pPStm.setInt(pOffset, pInstiProv.korrFixAnmeldung);
            pOffset++;
            pPStm.setInt(pOffset, pInstiProv.korrGewaehlteSammelkarteIdent);
            pOffset++;
            pPStm.setInt(pOffset, pInstiProv.korrVorrangFremdbesitz);
            pOffset++;
            pPStm.setString(pOffset, pInstiProv.aktionaersnameAR);
            pOffset++;
            pPStm.setLong(pOffset, pInstiProv.aktienzahlAR);
            pOffset++;
            pPStm.setInt(pOffset, pInstiProv.codeVerarbeitetTest);
            pOffset++;
            pPStm.setString(pOffset, pInstiProv.textVerarbeitetTest);
            pOffset++;
            pPStm.setInt(pOffset, pInstiProv.codeVerarbeitetVerarbeitung);
            pOffset++;
            pPStm.setString(pOffset, pInstiProv.textVerarbeitetVerarbeitung);
            pOffset++;
            pPStm.setInt(pOffset, pInstiProv.sammelkartenart);
            pOffset++;

            if (pOffset - startOffset != anzfelder) {
                /*Nur wg. Überprüfung auf Programmierfehler - alle Felder berücksichtigt?*/
                CaBug.drucke("DbInstiProv.fuellePreparedStatementKomplett 002");
            }

        } catch (SQLException e) {
            CaBug.drucke("DbInstiProv.fuellePreparedStatementKomplett 001");
            e.printStackTrace();
        }

    }

    /**Insert
     * 
     * Returnwert:
     * =1 => Insert erfolgreich
     * ansonsten: Fehler
     */
    public int insert(EclInstiProv pInstiProv) {

        int erg = 0;

        /* neue InterneIdent vergeben */
        erg = dbBundle.dbBasis.getInterneIdentInstiProv();
        if (erg < 1) {
            CaBug.drucke("DbInstiProv.insert 002");
            return (erg);
        }

        pInstiProv.ident = erg;
        pInstiProv.mandant = dbBundle.clGlobalVar.mandant;

        try {
            /*Felder Neuanlage füllen*/
            String lSql = "INSERT INTO " + dbBundle.getSchemaMandant() + "tbl_instiProv " + "("
                    + "mandant, ident, verarbeitungslauf, "
                    + "quellDateiname, aktionaersnummer, aktionaersname, aktienzahl, sollFixAnmeldung, gesamtMarkierung, ";
            for (int i = 1; i <= 200; i++) {
                lSql = lSql + "einzelMarkierungen" + Integer.toString(i) + ", ";
            }
            lSql = lSql + "sollGewaehlteSammelkarteIdent, sollVorrangFremdbesitz, "
                    + "korrFixAnmeldung, korrGewaehlteSammelkarteIdent, korrVorrangFremdbesitz, aktionaersnameAR, aktienzahlAR, "
                    + "codeVerarbeitetTest, textVerarbeitetTest, codeVerarbeitetVerarbeitung, textVerarbeitetVerarbeitung,  sammelkartenart "
                    + ")" + "VALUES (" + "?, ?, ?, " + "?, ?, ?, ?, ?, ?, ";
            for (int i = 1; i <= 200; i++) {
                lSql = lSql + "?, ";
            }
            lSql = lSql + "?, ?, " + "?, ?, ?, ?, ?, " + "?, ?, ?, ?, ? " + ")";
            PreparedStatement lPStm = verbindung.prepareStatement(lSql, ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
            fuellePreparedStatementKomplett(lPStm, 1, pInstiProv);
            erg = lPStm.executeUpdate();
            lPStm.close();
        } catch (Exception e2) {
            CaBug.drucke("DbInstiProv.insert 001");
            System.err.println(" " + e2.getMessage());
        }

        if (erg == 0) {/*Fehler beim Einfügen - d.h. z.B. primaryKey bereits vorhanden*/
            return (-1);
        }

        return (1);
    }

    /**Lese alle Verarbeitungsläufe
     * */
    public int readAll(int pVerarbeitungslauf) {
        int anzInArray = 0;
        PreparedStatement lPStm = null;

        try {
            String lSql = "SELECT * from " + dbBundle.getSchemaMandant()
                    + "tbl_instiProv inp WHERE inp.mandant=? AND inp.verarbeitungslauf=? ORDER BY inp.ident";
            lPStm = verbindung.prepareStatement(lSql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            lPStm.setInt(1, dbBundle.clGlobalVar.mandant);
            lPStm.setInt(2, pVerarbeitungslauf);

            ResultSet lErgebnis = lPStm.executeQuery();
            lErgebnis.last();
            anzInArray = lErgebnis.getRow();
            lErgebnis.beforeFirst();

            ergebnisArray = new EclInstiProv[anzInArray];

            int i = 0;
            while (lErgebnis.next() == true) {
                ergebnisArray[i] = this.decodeErgebnis(lErgebnis);
                i++;
            }
            lErgebnis.close();
            lPStm.close();

        } catch (Exception e) {
            CaBug.drucke("DbInstiProv.readAll_technisch 003");
            System.err.println(" " + e.getMessage());
            return (-1);
        }
        return (anzInArray);
    }

    /**Einlesen eines bestimmten Laufs mit  ident*/
    public int read(int ident) {
        int anzInArray = 0;
        PreparedStatement lPStm = null;

        try {
            String lSql = "SELECT * from " + dbBundle.getSchemaMandant() + "tbl_instiProv inp "
                    + "WHERE mandant=? AND ident=?;";
            lPStm = verbindung.prepareStatement(lSql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            lPStm.setInt(1, dbBundle.clGlobalVar.mandant);
            lPStm.setInt(2, ident);

            ResultSet lErgebnis = lPStm.executeQuery();
            lErgebnis.last();
            anzInArray = lErgebnis.getRow();
            lErgebnis.beforeFirst();

            ergebnisArray = new EclInstiProv[anzInArray];

            int i = 0;
            while (lErgebnis.next() == true) {
                ergebnisArray[i] = this.decodeErgebnis(lErgebnis);
                i++;
            }

            lErgebnis.close();
            lPStm.close();

        } catch (Exception e) {
            CaBug.drucke("DbInstiProv.read 003");
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
    public int update(EclInstiProv pInstiProv) {

        try {
            String lSql = "UPDATE " + dbBundle.getSchemaMandant() + "tbl_instiProv SET "
                    + "mandant=?, ident=?, verarbeitungslauf=?, "
                    + "quellDateiname=?, aktionaersnummer=?, aktionaersname=?, aktienzahl=?, sollFixAnmeldung=?, gesamtMarkierung=?, ";
            for (int i = 1; i <= 200; i++) {
                lSql = lSql + "einzelMarkierungen" + Integer.toString(i) + "=?, ";
            }
            lSql = lSql + "sollGewaehlteSammelkarteIdent=?, sollVorrangFremdbesitz=?, "
                    + "korrFixAnmeldung=?, korrGewaehlteSammelkarteIdent=?, korrVorrangFremdbesitz=?, aktionaersnameAR=?, aktienzahlAR=?, "
                    + "codeVerarbeitetTest=?, textVerarbeitetTest=?, codeVerarbeitetVerarbeitung=?, textVerarbeitetVerarbeitung=?,  sammelkartenart=? "
                    + "WHERE " + "mandant=? AND ident=?";

            PreparedStatement lPStm = verbindung.prepareStatement(lSql);
            fuellePreparedStatementKomplett(lPStm, 1, pInstiProv);
            lPStm.setInt(anzfelder + 1, pInstiProv.mandant);
            lPStm.setInt(anzfelder + 2, pInstiProv.ident);

            int ergebnis = lPStm.executeUpdate();
            lPStm.close();
            if (ergebnis == 0) {
                return (CaFehler.pfXyWurdeVonAnderemBenutzerVeraendert);
            }
        } catch (Exception e1) {
            CaBug.drucke("DbInstiProv.update 001");
            System.err.println(" " + e1.getMessage());
            return (CaFehler.pfdXyBereitsVorhanden);
        }

        return (1);
    }

    public int delete(int pIdent) {

        try {

            String sql = "DELETE FROM " + dbBundle.getSchemaMandant() + "tbl_instiProv WHERE mandant=? AND ident=?";

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

    public int updateMandant() {
        return dbBundle.dbLowLevel.rawUpdateMandant(dbBundle.getSchemaMandant() + "tbl_instiProv");
    }

}
