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
import de.meetingapps.meetingportal.meetComEntities.EclWortmeldungen;

@Deprecated
public class DbWortmeldungen {

    private Connection verbindung = null;
    private DbBasis dbBasis = null;
    private DbBundle dbBundle = null;

    private EclWortmeldungen ergebnisArray[] = null;

    /*************************Initialisierung***************************/
    public DbWortmeldungen(DbBundle pDbBundle) {
        /* Verbindung in lokale Daten eintragen*/
        if (pDbBundle == null) {
            CaBug.drucke("001 - dbBundle nicht initialisiert");
            return;
        }
        if (pDbBundle.dbBasis == null) {
            CaBug.drucke("002 - dbBasis nicht initialisiert");
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
    public EclWortmeldungen[] ergebnis() {
        return ergebnisArray;
    }

    /**********Liefert pN-tes Element des Ergebnisses der read*Methoden**************
     * pN geht von 0 bis anzErgebnis-1*/
    public EclWortmeldungen ergebnisPosition(int pN) {
        if (ergebnisArray == null) {
            CaBug.drucke("001");
            return null;
        }
        if (pN < 0) {
            CaBug.drucke("002");
            return null;
        }
        if (pN >= ergebnisArray.length) {
            CaBug.drucke("003");
            return null;
        }
        return ergebnisArray[pN];
    }

    public int createTable() {
        int rc = 0;
        DbLowLevel lDbLowLevel = new DbLowLevel(dbBundle);
        rc = lDbLowLevel.createTable("CREATE TABLE " + dbBundle.getSchemaMandant() + "tbl_wortmeldungen ( "
                + "`ident` int(11) NOT NULL, " + "`db_version` bigint(20) DEFAULT NULL, "
                + "`loginKennungIdent` int(11) NOT NULL, " + "`melderIdentText` varchar(20) DEFAULT NULL, "
                + "`wortmelder` varchar(100) DEFAULT NULL, " + "`telefonNr` varchar(100) DEFAULT NULL, "
                + "`zuTop` varchar(100) DEFAULT NULL, " + "`wortmeldungtext` varchar(21000) DEFAULT NULL, "
                + "`zeitpunktDerWortmeldung` varchar(19) DEFAULT NULL, " + "`drucklaufNr` int(11) NOT NULL, "
                + "`status` int(11) NOT NULL, " + "`lfdNrInRednerliste` int(11) NOT NULL, " + "PRIMARY KEY (`ident`) "
                + ") ");
        return rc;
    }

    /**************************deleteAll Löschen aller Datensätze  eines Mandanten****************/
    public int deleteAll() {
        dbBundle.dbBasis.resetInterneIdentWortmeldungen();
        return dbBundle.dbLowLevel.deleteAlle("DELETE FROM " + dbBundle.getSchemaMandant() + "tbl_wortmeldungen;");
    }

    public void reorgInterneIdent() {
        int lMax = dbBundle.dbLowLevel
                .liefereHoechsteIdent("SELECT MAX(ident) FROM " + dbBundle.getSchemaMandant() + "tbl_wortmeldungen;");
        if (lMax != -1) {
            dbBundle.dbBasis.resetInterneIdentWortmeldungen(lMax);
        }
    }

    /********** dekodiert die aktuelle Position aus ergebnis  und gibt dieses zurück******/
    EclWortmeldungen decodeErgebnis(ResultSet pErgebnis) {

        EclWortmeldungen lEclReturn = new EclWortmeldungen();

        try {
            lEclReturn.ident = pErgebnis.getInt("ident");
            lEclReturn.db_version = pErgebnis.getLong("db_version");

            lEclReturn.loginKennungIdent = pErgebnis.getInt("loginKennungIdent");
            lEclReturn.melderIdentText = pErgebnis.getString("melderIdentText");

            lEclReturn.wortmelder = pErgebnis.getString("wortmelder");
            lEclReturn.telefonNr = pErgebnis.getString("telefonNr");
            lEclReturn.zuTop = pErgebnis.getString("zuTop");
            lEclReturn.wortmeldungtext = pErgebnis.getString("wortmeldungtext");

            lEclReturn.zeitpunktDerWortmeldung = pErgebnis.getString("zeitpunktDerWortmeldung");
            lEclReturn.drucklaufNr = pErgebnis.getInt("drucklaufNr");
            lEclReturn.status = pErgebnis.getInt("status");
            lEclReturn.lfdNrInRednerliste = pErgebnis.getInt("lfdNrInRednerliste");
        } catch (Exception e) {
            CaBug.drucke("001");
            System.err.println(" " + e.getMessage());
        }

        return lEclReturn;
    }

    /********************* Fuellen Prepared Statement mit allen Feldern, beginnend bei offset.*****************
     * Kann sowohl für Insert, als auch für update verwendet werden.
     * 
     * offset= Startposition des ersten Feldes (also z.B. 1)
     */
    private int anzfelder = 12; /*Anpassen auf Anzahl der Felder pro Datensatz*/

    private void fuellePreparedStatementKomplett(PreparedStatement pPStm, int pOffset, EclWortmeldungen pEcl) {

        int startOffset = pOffset; /*Nur erforderlich zum Überprüfen von Programmierfehlern - Feldanzahl*/

        try {
            pPStm.setInt(pOffset, pEcl.ident);
            pOffset++;
            pPStm.setLong(pOffset, pEcl.db_version);
            pOffset++;

            pPStm.setInt(pOffset, pEcl.loginKennungIdent);
            pOffset++;
            pPStm.setString(pOffset, pEcl.melderIdentText);
            pOffset++;

            pPStm.setString(pOffset, pEcl.wortmelder);
            pOffset++;
            pPStm.setString(pOffset, pEcl.telefonNr);
            pOffset++;
            pPStm.setString(pOffset, pEcl.zuTop);
            pOffset++;
            pPStm.setString(pOffset, pEcl.wortmeldungtext);
            pOffset++;

            pPStm.setString(pOffset, pEcl.zeitpunktDerWortmeldung);
            pOffset++;
            pPStm.setInt(pOffset, pEcl.drucklaufNr);
            pOffset++;
            pPStm.setInt(pOffset, pEcl.status);
            pOffset++;
            pPStm.setInt(pOffset, pEcl.lfdNrInRednerliste);
            pOffset++;

            if (pOffset - startOffset != anzfelder) {
                /*Nur wg. Überprüfung auf Programmierfehler - alle Felder berücksichtigt?*/
                CaBug.drucke("002");
            }

        } catch (SQLException e) {
            CaBug.drucke("001");
            e.printStackTrace();
        }

    }

    /**Insert
     * 
     * Returnwert:
     * =1 => Insert erfolgreich
     * ansonsten: Fehler
     */
    public int insert(EclWortmeldungen pEcl) {

        int erg = 0;
        dbBasis.beginTransaction();

        /* neue InterneIdent vergeben */
        erg = dbBasis.getInterneIdentWortmeldungen();
        if (erg < 1) {
            CaBug.drucke("002");
            dbBasis.endTransaction();
            return (erg);
        }

        pEcl.ident = erg;

        try {

            /*Felder Neuanlage füllen*/
            String lSql = "INSERT INTO " + dbBundle.getSchemaMandant() + "tbl_wortmeldungen " + "("
                    + "ident, db_version, " + "loginKennungIdent, melderIdentText, "
                    + "wortmelder, telefonNr, zuTop, wortmeldungtext, zeitpunktDerWortmeldung, drucklaufNr, "
                    + "status, lfdNrInRednerliste " + ")" + "VALUES (" + "?, ?, " + "?, ?, " + "?, ?, ?, ?, ?, ?, "
                    + "?, ?" + ")";
            PreparedStatement lPStm = verbindung.prepareStatement(lSql, ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);

            fuellePreparedStatementKomplett(lPStm, 1, pEcl);

            erg = lPStm.executeUpdate();
            lPStm.close();
        } catch (Exception e2) {
            CaBug.drucke("001");
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

            ergebnisArray = new EclWortmeldungen[anzInArray];

            int i = 0;
            while (lErgebnis.next() == true) {
                ergebnisArray[i] = this.decodeErgebnis(lErgebnis);
                i++;
            }
            lErgebnis.close();
            pPStm.close();

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
    public int read(int pWortmeldungIdent) {
        PreparedStatement lPStm = null;
        int anzInArray = 0;
        try {
            String lSql = "SELECT * from " + dbBundle.getSchemaMandant() + "tbl_wortmeldungen where " + "ident=? "
                    + "ORDER BY ident;";
            lPStm = verbindung.prepareStatement(lSql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            lPStm.setInt(1, pWortmeldungIdent);
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
    public int readAll_loginKennungIdent(int pLoginKennungIdent) {
        PreparedStatement lPStm = null;
        int anzInArray = 0;
        try {
            String lSql = "SELECT * from " + dbBundle.getSchemaMandant() + "tbl_wortmeldungen where "
                    + "loginKennungIdent=? " + "ORDER BY ident;";
            lPStm = verbindung.prepareStatement(lSql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            lPStm.setInt(1, pLoginKennungIdent);
            anzInArray = readIntern(lPStm);

        } catch (Exception e) {
            CaBug.drucke("003");
            System.err.println(" " + e.getMessage());
            return (-1);
        }
        return (anzInArray);
    }

    /**pDrucklauf kann 0 sein, dann alle*/
    public int readAll_wortmeldungen(int pDrucklauf) {
        PreparedStatement lPStm = null;
        int anzInArray = 0;
        try {
            String lSql = "SELECT * from " + dbBundle.getSchemaMandant() + "tbl_wortmeldungen ";
            if (pDrucklauf != 0) {
                lSql = lSql + "where  drucklaufNr=? ";
            }
            lSql = lSql + "ORDER BY ident;";
            lPStm = verbindung.prepareStatement(lSql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            if (pDrucklauf != 0) {
                lPStm.setInt(1, pDrucklauf);
            }
            anzInArray = readIntern(lPStm);

        } catch (Exception e) {
            CaBug.drucke("003");
            System.err.println(" " + e.getMessage());
            return (-1);
        }
        return (anzInArray);
    }

    /**pDrucklauf kann 0 sein, dann alle*/
    public int readAll_rednerliste() {
        PreparedStatement lPStm = null;
        int anzInArray = 0;
        try {
            String lSql = "SELECT * from " + dbBundle.getSchemaMandant() + "tbl_wortmeldungen ";
            lSql = lSql + "ORDER BY lfdNrInRednerliste, ident;";
            lPStm = verbindung.prepareStatement(lSql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            anzInArray = readIntern(lPStm);

        } catch (Exception e) {
            CaBug.drucke("003");
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
    public int update(EclWortmeldungen pEcl) {

        pEcl.db_version++;

        try {

            String lSql = "UPDATE " + dbBundle.getSchemaMandant() + "tbl_wortmeldungen SET " + "ident=?, db_version=?, "
                    + "loginKennungIdent=?, melderIdentText=?, "
                    + "wortmelder=?, telefonNr=?, zuTop=?, wortmeldungtext=?, zeitpunktDerWortmeldung=?, drucklaufNr=?, "
                    + "status=?, lfdNrInRednerliste=? " + "WHERE " + "ident=? AND db_version=? ";

            PreparedStatement lPStm = verbindung.prepareStatement(lSql);
            fuellePreparedStatementKomplett(lPStm, 1, pEcl);
            lPStm.setInt(anzfelder + 1, pEcl.ident);
            lPStm.setLong(anzfelder + 2, pEcl.db_version - 1);

            int ergebnis = lPStm.executeUpdate();
            lPStm.close();
            if (ergebnis == 0) {
                return (CaFehler.pfXyWurdeVonAnderemBenutzerVeraendert);
            }
        } catch (Exception e1) {
            CaBug.drucke("001");
            System.err.println(" " + e1.getMessage());
            return (-1);
        }

        return (1);
    }

    public int updateUndAnsEnde(EclWortmeldungen pEcl) {

        PreparedStatement lPStm = null;
        int ergebnis = 0;
        int maxWert = 0;

        String lSql = "SELECT MAX(lfdNrInRednerliste) " + "FROM " + dbBundle.getSchemaMandant() + "tbl_wortmeldungen ";

        try {
            lPStm = verbindung.prepareStatement(lSql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);

            ResultSet lErgebnis = lPStm.executeQuery();
            lErgebnis.last();
            lErgebnis.beforeFirst();

            while (lErgebnis.next() == true) {
                ergebnis = lErgebnis.getInt(1);
            }
            maxWert = ergebnis;
            lErgebnis.close();
            lPStm.close();

        } catch (SQLException e) {
            CaBug.drucke("003");
            System.err.println(" " + e.getMessage());
            return (-1);
        }

        pEcl.db_version++;
        pEcl.lfdNrInRednerliste = maxWert + 1;

        try {

            lSql = "UPDATE " + dbBundle.getSchemaMandant() + "tbl_wortmeldungen SET " + "ident=?, db_version=?, "
                    + "loginKennungIdent=?, melderIdentText=?, "
                    + "wortmelder=?, telefonNr=?, zuTop=?, wortmeldungtext=?, zeitpunktDerWortmeldung=?, drucklaufNr=?, "
                    + "status=?, lfdNrInRednerliste=? " + "WHERE " + "ident=? AND db_version=? ";

            lPStm = verbindung.prepareStatement(lSql);
            fuellePreparedStatementKomplett(lPStm, 1, pEcl);
            lPStm.setInt(anzfelder + 1, pEcl.ident);
            lPStm.setLong(anzfelder + 2, pEcl.db_version - 1);

            ergebnis = lPStm.executeUpdate();
            lPStm.close();
            if (ergebnis == 0) {
                return (CaFehler.pfXyWurdeVonAnderemBenutzerVeraendert);
            }
        } catch (Exception e1) {
            CaBug.drucke("001");
            System.err.println(" " + e1.getMessage());
            return (-1);
        }

        return (1);
    }

    /**Update. 
     * 
     * Returnwert:
     * pfXyWurdeVonAnderemBenutzerVeraendert
     * -1 => unbekannter Fehler
     * 1 = Update wurde durchgeführt.
     * 
     */

    public int update_status(int pIdent, int pStatus) {

        try {

            String lSql = "UPDATE " + dbBundle.getSchemaMandant() + "tbl_wortmeldungen SET "
                    + "db_version=db_version+1, status=? " + "WHERE " + "ident=? ";

            PreparedStatement lPStm = verbindung.prepareStatement(lSql);
            lPStm.setInt(1, pStatus);
            lPStm.setInt(2, pIdent);

            int ergebnis = lPStm.executeUpdate();
            lPStm.close();
            if (ergebnis == 0) {
                return (CaFehler.pfXyWurdeVonAnderemBenutzerVeraendert);
            }
        } catch (Exception e1) {
            CaBug.drucke("001");
            System.err.println(" " + e1.getMessage());
            return (-1);
        }

        return (1);
    }

    public int update_statusZurueckgezogen(int pIdent) {

        try {

            String lSql = "UPDATE " + dbBundle.getSchemaMandant() + "tbl_wortmeldungen SET "
                    + "db_version=db_version+1, status=7 " + "WHERE " + "ident=? ";

            PreparedStatement lPStm = verbindung.prepareStatement(lSql);
            lPStm.setInt(1, pIdent);

            int ergebnis = lPStm.executeUpdate();
            lPStm.close();
            if (ergebnis == 0) {
                return (CaFehler.pfXyWurdeVonAnderemBenutzerVeraendert);
            }
        } catch (Exception e1) {
            CaBug.drucke("001");
            System.err.println(" " + e1.getMessage());
            return (-1);
        }

        return (1);
    }

    public int update_statusGesprochen(int pIdent) {

        try {

            String lSql = "UPDATE " + dbBundle.getSchemaMandant() + "tbl_wortmeldungen SET "
                    + "db_version=db_version+1, status=5 " + "WHERE " + "ident=? ";

            PreparedStatement lPStm = verbindung.prepareStatement(lSql);
            lPStm.setInt(1, pIdent);

            int ergebnis = lPStm.executeUpdate();
            lPStm.close();
            if (ergebnis == 0) {
                return (CaFehler.pfXyWurdeVonAnderemBenutzerVeraendert);
            }
        } catch (Exception e1) {
            CaBug.drucke("001");
            System.err.println(" " + e1.getMessage());
            return (-1);
        }

        return (1);
    }

    public int update_statusNichtErreichtErledigt(int pIdent) {

        try {

            String lSql = "UPDATE " + dbBundle.getSchemaMandant() + "tbl_wortmeldungen SET "
                    + "db_version=db_version+1, status=4 " + "WHERE " + "ident=? ";

            PreparedStatement lPStm = verbindung.prepareStatement(lSql);
            lPStm.setInt(1, pIdent);

            int ergebnis = lPStm.executeUpdate();
            lPStm.close();
            if (ergebnis == 0) {
                return (CaFehler.pfXyWurdeVonAnderemBenutzerVeraendert);
            }
        } catch (Exception e1) {
            CaBug.drucke("001");
            System.err.println(" " + e1.getMessage());
            return (-1);
        }

        return (1);
    }

    public int update_nichtErreichtAnsEnde(int pIdent) {

        PreparedStatement lPStm = null;
        int ergebnis = 0;
        int maxWert = 0;

        String lSql = "SELECT MAX(lfdNrInRednerliste) " + "FROM " + dbBundle.getSchemaMandant() + "tbl_wortmeldungen ";

        try {
            lPStm = verbindung.prepareStatement(lSql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);

            ResultSet lErgebnis = lPStm.executeQuery();
            lErgebnis.last();
            lErgebnis.beforeFirst();

            while (lErgebnis.next() == true) {
                ergebnis = lErgebnis.getInt(1);
            }
            maxWert = ergebnis;
            lErgebnis.close();
            lPStm.close();

        } catch (SQLException e) {
            CaBug.drucke("003");
            System.err.println(" " + e.getMessage());
            return (-1);
        }

        try {

            lSql = "UPDATE " + dbBundle.getSchemaMandant() + "tbl_wortmeldungen SET "
                    + "lfdNrInRednerliste=?, status=2 " + "WHERE " + "ident=? ";

            lPStm = verbindung.prepareStatement(lSql);
            lPStm.setInt(1, maxWert + 1);
            lPStm.setInt(2, pIdent);

            ergebnis = lPStm.executeUpdate();
            lPStm.close();
            if (ergebnis == 0) {
                return (CaFehler.pfXyWurdeVonAnderemBenutzerVeraendert);
            }
        } catch (Exception e1) {
            CaBug.drucke("001");
            System.err.println(" " + e1.getMessage());
            return (-1);
        }

        return (1);
    }

    public int updateNeuerDrucklauf(int pDrucklaufnr) {

        int ergebnis = 0;
        try {

            String lSql = "UPDATE " + dbBundle.getSchemaMandant() + "tbl_wortmeldungen SET " + "drucklaufNr=? "
                    + "WHERE " + "drucklaufNr=0 ";

            PreparedStatement lPStm = verbindung.prepareStatement(lSql);
            lPStm.setInt(1, pDrucklaufnr);

            ergebnis = lPStm.executeUpdate();
            lPStm.close();
        } catch (Exception e1) {
            CaBug.drucke("001");
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

            String sql = "DELETE FROM " + dbBundle.getSchemaMandant() + "tbl_wortmeldungen WHERE ident=? ";

            PreparedStatement pstm1 = verbindung.prepareStatement(sql);
            pstm1.setInt(1, pIdent);

            int ergebnis1 = pstm1.executeUpdate();
            pstm1.close();
            if (ergebnis1 == 0) {
                return (CaFehler.pfXyWurdeVonAnderemBenutzerVeraendert);
            }
        } catch (Exception e1) {
            CaBug.drucke("001");
            System.err.println(" " + e1.getMessage());
            return (-1);
        }

        return (1);
    }

}
