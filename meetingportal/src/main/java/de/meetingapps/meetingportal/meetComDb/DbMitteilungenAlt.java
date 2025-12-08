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
import de.meetingapps.meetingportal.meetComEntities.EclMitteilungenAlt;

@Deprecated
public class DbMitteilungenAlt {

    private Connection verbindung = null;
    private DbBasis dbBasis = null;
    private DbBundle dbBundle = null;

    private EclMitteilungenAlt ergebnisArray[] = null;

    /*************************Initialisierung***************************/
    public DbMitteilungenAlt(DbBundle pDbBundle) {
        /* Verbindung in lokale Daten eintragen*/
        if (pDbBundle == null) {
            CaBug.drucke("DbMitteilungen.init 001 - dbBundle nicht initialisiert");
            return;
        }
        if (pDbBundle.dbBasis == null) {
            CaBug.drucke("DbMitteilungen.init 002 - dbBasis nicht initialisiert");
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
    public EclMitteilungenAlt[] ergebnis() {
        return ergebnisArray;
    }

    /**********Liefert pN-tes Element des Ergebnisses der read*Methoden**************
     * pN geht von 0 bis anzErgebnis-1*/
    public EclMitteilungenAlt ergebnisPosition(int pN) {
        if (ergebnisArray == null) {
            CaBug.drucke("DbMitteilungen.ergebnisPosition 001");
            return null;
        }
        if (pN < 0) {
            CaBug.drucke("DbMitteilungen.ergebnisPosition 002");
            return null;
        }
        if (pN >= ergebnisArray.length) {
            CaBug.drucke("DbMitteilungen.ergebnisPosition 003");
            return null;
        }
        return ergebnisArray[pN];
    }

    public int createTable() {
        int rc = 0;
        DbLowLevel lDbLowLevel = new DbLowLevel(dbBundle);
        String createString = "CREATE TABLE " + dbBundle.getSchemaMandant() + "tbl_mitteilungen ( "
                + "`mandant` int(11) NOT NULL, " + "`mitteilungIdent` int(11) NOT NULL, "
                + "`db_version` bigint(20) DEFAULT NULL, " + "`aktienregisterIdent` int(11) NOT NULL, "
                + "`instiIdent` int(11) NOT NULL, " + "`vertreterIdent` int(11) NOT NULL, "
                + "`stellerIdent` varchar(20) DEFAULT NULL, " + "`artDerMitteilung` int(11) NOT NULL, ";
        for (int i = 0; i < 200; i++) {
            createString = createString + "`mitteilungZuTop" + Integer.toString(i) + "` int(11) NOT NULL, ";
        }
        createString = createString + "`mitteilungtext` varchar(21000) DEFAULT NULL, "
                + "`zeitpunktDerMitteilung` varchar(19) DEFAULT NULL, " + "`drucklaufNr` int(11) NOT NULL, "
                + "PRIMARY KEY (`mandant`,`mitteilungIdent`) " + ") ";
        rc = lDbLowLevel.createTable(createString);
        return rc;
    }

    /**************************deleteAll Löschen aller Datensätze  eines Mandanten****************/
    public int deleteAll() {
        dbBundle.dbBasis.resetInterneIdentFrage();
        return dbBundle.dbLowLevel
                .deleteMandant("DELETE FROM " + dbBundle.getSchemaMandant() + "tbl_mitteilungen where mandant=?;");
    }

    /**************************setzt aktuelle Mandantennummer bei allen Datensätzen****************/
    public int updateMandant() {
        return dbBundle.dbLowLevel.rawUpdateMandant(dbBundle.getSchemaMandant() + "tbl_mitteilungen");
    }

    public void reorgInterneIdent() {
        int lMax = dbBundle.dbLowLevel.liefereHoechsteIdent("SELECT MAX(mitteilungIdent) FROM "
                + dbBundle.getSchemaMandant() + "tbl_mitteilungen where mandant=?;");
        if (lMax != -1) {
            dbBundle.dbBasis.resetInterneIdentFrage(lMax);
        }
    }


    /********** dekodiert die aktuelle Position aus ergebnis  und gibt dieses zurück******/
    EclMitteilungenAlt decodeErgebnis(ResultSet pErgebnis) {

        EclMitteilungenAlt lEclReturn = new EclMitteilungenAlt();

        try {
            lEclReturn.mandant = pErgebnis.getInt("mandant");
            lEclReturn.mitteilungIdent = pErgebnis.getInt("mitteilungIdent");
            lEclReturn.db_version = pErgebnis.getLong("db_version");

            lEclReturn.aktienregisterIdent = pErgebnis.getInt("aktienregisterIdent");
            lEclReturn.instiIdent = pErgebnis.getInt("instiIdent");
            lEclReturn.vertreterIdent = pErgebnis.getInt("vertreterIdent");
            lEclReturn.stellerIdent = pErgebnis.getString("stellerIdent");

            lEclReturn.artDerMitteilung = pErgebnis.getInt("artDerMitteilung");

            for (int i = 0; i < 200; i++) {
                lEclReturn.mitteilungZuTop[i] = pErgebnis.getInt("mitteilungZuTop" + Integer.toString(i));
            }

            lEclReturn.mitteilungtext = pErgebnis.getString("mitteilungtext");

            lEclReturn.zeitpunktDerMitteilung = pErgebnis.getString("zeitpunktDerMitteilung");
            lEclReturn.drucklaufNr = pErgebnis.getInt("drucklaufNr");
        } catch (Exception e) {
            CaBug.drucke("DbMitteilungen.decodeErgebnis 001");
            System.err.println(" " + e.getMessage());
        }

        return lEclReturn;
    }

    /*XXX*/

    /********************* Fuellen Prepared Statement mit allen Feldern, beginnend bei offset.*****************
     * Kann sowohl für Insert, als auch für update verwendet werden.
     * 
     * offset= Startposition des ersten Feldes (also z.B. 1)
     */
    private int anzfelder = 211; /*Anpassen auf Anzahl der Felder pro Datensatz*/

    private void fuellePreparedStatementKomplett(PreparedStatement pPStm, int pOffset, EclMitteilungenAlt pEcl) {

        int startOffset = pOffset; /*Nur erforderlich zum Überprüfen von Programmierfehlern - Feldanzahl*/

        try {
            pPStm.setInt(pOffset, pEcl.mandant);
            pOffset++;
            pPStm.setInt(pOffset, pEcl.mitteilungIdent);
            pOffset++;
            pPStm.setLong(pOffset, pEcl.db_version);
            pOffset++;

            pPStm.setInt(pOffset, pEcl.aktienregisterIdent);
            pOffset++;
            pPStm.setInt(pOffset, pEcl.instiIdent);
            pOffset++;
            pPStm.setInt(pOffset, pEcl.vertreterIdent);
            pOffset++;
            pPStm.setString(pOffset, pEcl.stellerIdent);
            pOffset++;

            pPStm.setInt(pOffset, pEcl.artDerMitteilung);
            pOffset++;
            for (int i = 0; i < 200; i++) {
                pPStm.setInt(pOffset, pEcl.mitteilungZuTop[i]);
                pOffset++;
            }
            pPStm.setString(pOffset, pEcl.mitteilungtext);
            pOffset++;

            pPStm.setString(pOffset, pEcl.zeitpunktDerMitteilung);
            pOffset++;
            pPStm.setInt(pOffset, pEcl.drucklaufNr);
            pOffset++;

            if (pOffset - startOffset != anzfelder) {
                /*Nur wg. Überprüfung auf Programmierfehler - alle Felder berücksichtigt?*/
                CaBug.drucke("DbMitteilungen.fuellePreparedStatementKomplett 002");
            }

        } catch (SQLException e) {
            CaBug.drucke("DbMitteilungen.fuellePreparedStatementKomplett 001");
            e.printStackTrace();
        }

    }

    /**Insert
     * 
     * Returnwert:
     * =1 => Insert erfolgreich
     * ansonsten: Fehler
     */
    public int insert(EclMitteilungenAlt pEcl) {

        int erg = 0;
        dbBasis.beginTransaction();

        /* neue InterneIdent vergeben */
        erg = dbBasis.getInterneIdentFrage();
        if (erg < 1) {
            CaBug.drucke("DbMitteilungen.insert 002");
            dbBasis.endTransaction();
            return (erg);
        }

        pEcl.mitteilungIdent = erg;

        pEcl.mandant = dbBundle.clGlobalVar.mandant;

        try {

            /*Felder Neuanlage füllen*/
            String lSql = "INSERT INTO " + dbBundle.getSchemaMandant() + "tbl_mitteilungen " + "("
                    + "mandant, mitteilungIdent, db_version, "
                    + "aktienregisterIdent, instiIdent, vertreterIdent, stellerIdent, " + "artDerMitteilung, ";
            for (int i = 0; i < 200; i++) {
                lSql = lSql + "mitteilungZuTop" + Integer.toString(i) + ", ";
            }
            lSql = lSql + "mitteilungtext, zeitpunktDerMitteilung, drucklaufNr " + ")" + "VALUES (" + "?, ?, ?, "
                    + "?, ?, ?, ?, " + "?, ";
            for (int i = 0; i < 200; i++) {
                lSql = lSql + "?, ";
            }
            lSql = lSql + "?, ?, ? " + ")";
            PreparedStatement lPStm = verbindung.prepareStatement(lSql, ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);

            fuellePreparedStatementKomplett(lPStm, 1, pEcl);

            erg = lPStm.executeUpdate();
            lPStm.close();
        } catch (Exception e2) {
            CaBug.drucke("DbMitteilungen.insert 001");
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

    private int readIntern(PreparedStatement pPStm) {
        int anzInArray = 0;

        try {

            ResultSet lErgebnis = pPStm.executeQuery();
            lErgebnis.last();
            anzInArray = lErgebnis.getRow();
            lErgebnis.beforeFirst();

            ergebnisArray = new EclMitteilungenAlt[anzInArray];

            int i = 0;
            while (lErgebnis.next() == true) {
                ergebnisArray[i] = this.decodeErgebnis(lErgebnis);
                i++;
            }
            lErgebnis.close();
            pPStm.close();

        } catch (Exception e) {
            CaBug.drucke("DbMitteilungen.readIntern 003");
            System.err.println(" " + e.getMessage());
            return (-1);
        }
        return (anzInArray);

    }

    /**Mandant und Ident müssen übergeben werden.
     * 
     * Return-Wert = Anzahl der gefundenen Sätze (oder Fehlermeldung <0)*/
    public int read(int pMitteilungsIdent) {
        int anzInArray = 0;
        PreparedStatement lPStm = null;

        try {
            String lSql = "SELECT * from " + dbBundle.getSchemaMandant() + "tbl_mitteilungen where " + "mandant=? AND "
                    + "mitteilungIdent=? " + "ORDER BY mitteilungIdent;";
            lPStm = verbindung.prepareStatement(lSql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            lPStm.setInt(1, dbBundle.clGlobalVar.mandant);
            lPStm.setInt(2, pMitteilungsIdent);

            anzInArray = readIntern(lPStm);

        } catch (Exception e) {
            CaBug.drucke("DbMitteilungen.read 003");
            System.err.println(" " + e.getMessage());
            return (-1);
        }
        return (anzInArray);
    }

    /*Ident müssen übergeben werden.
     * 
     * Return-Wert = Anzahl der gefundenen Sätze (oder Fehlermeldung <0)*/
    public int readAll_aktionaersIdent(int pAktionaersIdent) {
        PreparedStatement lPStm = null;
        int anzInArray = 0;
        try {
            String lSql = "SELECT * from " + dbBundle.getSchemaMandant() + "tbl_mitteilungen where " + "mandant=? AND "
                    + "aktienregisterIdent=? " + "ORDER BY mitteilungIdent;";
            lPStm = verbindung.prepareStatement(lSql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            lPStm.setInt(1, dbBundle.clGlobalVar.mandant);
            lPStm.setInt(2, pAktionaersIdent);
            anzInArray = readIntern(lPStm);

        } catch (Exception e) {
            CaBug.drucke("DbFragen.readAll_aktionaersIdent 003");
            System.err.println(" " + e.getMessage());
            return (-1);
        }
        return (anzInArray);
    }

    /*Ident müssen übergeben werden.
     * 
     * Return-Wert = Anzahl der gefundenen Sätze (oder Fehlermeldung <0)*/
    public int readAll_personNatJur(int pPersonNatJurIdent) {
        PreparedStatement lPStm = null;
        int anzInArray = 0;
        try {
            String lSql = "SELECT * from " + dbBundle.getSchemaMandant() + "tbl_mitteilungen where " + "mandant=? AND "
                    + "vertreterIdent=? " + "ORDER BY mitteilungIdent;";
            lPStm = verbindung.prepareStatement(lSql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            lPStm.setInt(1, dbBundle.clGlobalVar.mandant);
            lPStm.setInt(2, pPersonNatJurIdent);
            anzInArray = readIntern(lPStm);

        } catch (Exception e) {
            CaBug.drucke("003");
            System.err.println(" " + e.getMessage());
            return (-1);
        }
        return (anzInArray);
    }

    /*Ident müssen übergeben werden.
     * 
     * Return-Wert = Anzahl der gefundenen Sätze (oder Fehlermeldung <0)*/
    public int readAll_instiIdent(int pAktionaersIdent) {
        PreparedStatement lPStm = null;
        int anzInArray = 0;
        try {
            String lSql = "SELECT * from " + dbBundle.getSchemaMandant() + "tbl_mitteilungen where " + "mandant=? AND "
                    + "instiIdent=? " + "ORDER BY mitteilungIdent;";
            lPStm = verbindung.prepareStatement(lSql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            lPStm.setInt(1, dbBundle.clGlobalVar.mandant);
            lPStm.setInt(2, pAktionaersIdent);
            anzInArray = readIntern(lPStm);

        } catch (Exception e) {
            CaBug.drucke("DbFragen.readAll_aktionaersIdent 003");
            System.err.println(" " + e.getMessage());
            return (-1);
        }
        return (anzInArray);
    }

    /**pDrucklauf kann 0 sein, dann alle*/
    public int readAll_fragen(int pDrucklauf) {
        PreparedStatement lPStm = null;
        int anzInArray = 0;
        try {
            String lSql = "SELECT * from " + dbBundle.getSchemaMandant() + "tbl_mitteilungen where " + "mandant=? ";
            if (pDrucklauf != 0) {
                lSql = lSql + "AND drucklaufNr=? ";
            }
            lSql = lSql + "ORDER BY mitteilungIdent;";
            lPStm = verbindung.prepareStatement(lSql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            lPStm.setInt(1, dbBundle.clGlobalVar.mandant);
            if (pDrucklauf != 0) {
                lPStm.setInt(2, pDrucklauf);
            }
            anzInArray = readIntern(lPStm);

        } catch (Exception e) {
            CaBug.drucke("DbMitteilungen.readAll_aktionaersIdent 003");
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
    public int update(EclMitteilungenAlt pEcl) {

        pEcl.db_version++;

        try {

            String lSql = "UPDATE " + dbBundle.getSchemaMandant() + "tbl_mitteilungen SET "
                    + "mandant=?, mitteilungIdent=?, db_version=?, "
                    + "aktienregisterIdent=?, instiIdent=?, vertreterIdent=?, stellerIdent=?,  "
                    + "artDerMitteilung=?, ";
            for (int i = 0; i < 200; i++) {
                lSql = lSql + "mitteilungZuTop" + Integer.toString(i) + "=?, ";
            }
            lSql = lSql + "mitteilungtext=?, zeitpunktDerMitteilung=?, drucklaufNr=? " + "WHERE "
                    + "mandant=? AND mitteilungIdent=? AND db_version=? ";

            PreparedStatement lPStm = verbindung.prepareStatement(lSql);
            fuellePreparedStatementKomplett(lPStm, 1, pEcl);
            lPStm.setInt(anzfelder + 1, pEcl.mandant);
            lPStm.setInt(anzfelder + 2, pEcl.mitteilungIdent);
            lPStm.setLong(anzfelder + 3, pEcl.db_version - 1);

            int ergebnis = lPStm.executeUpdate();
            lPStm.close();
            if (ergebnis == 0) {
                return (CaFehler.pfXyWurdeVonAnderemBenutzerVeraendert);
            }
        } catch (Exception e1) {
            CaBug.drucke("DbMitteilungen.update 001");
            System.err.println(" " + e1.getMessage());
            return (-1);
        }

        return (1);
    }

    public int updateNeuerDrucklauf(int pDrucklaufnr) {

        int ergebnis = 0;
        try {

            String lSql = "UPDATE " + dbBundle.getSchemaMandant() + "tbl_mitteilungen SET " + "drucklaufNr=? "
                    + "WHERE " + "mandant=? AND drucklaufNr=0 ";

            PreparedStatement lPStm = verbindung.prepareStatement(lSql);
            lPStm.setInt(1, pDrucklaufnr);
            lPStm.setInt(2, dbBundle.clGlobalVar.mandant);

            ergebnis = lPStm.executeUpdate();
            lPStm.close();
        } catch (Exception e1) {
            CaBug.drucke("DbMitteilungen.updateNeuerDrucklauf 001");
            System.err.println(" " + e1.getMessage());
            return (-1);
        }

        return (ergebnis);
    }

    /**Return-Werte:
     * pfXyWurdeVonAnderemBenutzerVeraendert
     * -1 => undefinierter Fehler
     * 1 => Löschen erfolgreich
     */
    public int delete(int pIdent) {
        try {

            String sql = "DELETE FROM " + dbBundle.getSchemaMandant()
                    + "tbl_mitteilungen WHERE mitteilungIdent=? AND mandant=? ";

            PreparedStatement pstm1 = verbindung.prepareStatement(sql);
            pstm1.setInt(1, pIdent);
            pstm1.setInt(2, dbBundle.clGlobalVar.mandant);

            int ergebnis1 = pstm1.executeUpdate();
            pstm1.close();
            if (ergebnis1 == 0) {
                return (CaFehler.pfXyWurdeVonAnderemBenutzerVeraendert);
            }
        } catch (Exception e1) {
            CaBug.drucke("DbMitteilungen.delete 001");
            System.err.println(" " + e1.getMessage());
            return (-1);
        }

        return (1);
    }

}
