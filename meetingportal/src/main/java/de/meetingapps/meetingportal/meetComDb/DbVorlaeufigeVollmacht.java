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
import de.meetingapps.meetingportal.meetComEntities.EclVorlaeufigeVollmacht;

public class DbVorlaeufigeVollmacht {

    private Connection verbindung = null;
    private DbBasis dbBasis = null;
    private DbBundle dbBundle = null;

    private EclVorlaeufigeVollmacht ergebnisArray[] = null;

    /*************************Initialisierung***************************/
    public DbVorlaeufigeVollmacht(DbBundle pDbBundle) {
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
    public EclVorlaeufigeVollmacht[] ergebnis() {
        return ergebnisArray;
    }

    /**********Liefert pN-tes Element des Ergebnisses der read*Methoden**************
     * pN geht von 0 bis anzErgebnis-1*/
    public EclVorlaeufigeVollmacht ergebnisPosition(int pN) {
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
        rc = lDbLowLevel.createTable("CREATE TABLE " + dbBundle.getSchemaMandant() + "tbl_vorlaeufigeVollmacht ( "
                + "`ident` int(11) NOT NULL, " + "`db_version` bigint(20) DEFAULT NULL, "
                + "`storniert` int(11) NOT NULL, " + "`erteiltVonArt` int(11) NOT NULL, "
                + "`erteiltVonIdent` int(11) NOT NULL, " + "`eMailVollmachtgeber` varchar(100) DEFAULT NULL, "
                + "`bevollmaechtigterArt` int(11) NOT NULL, " + "`bevollmaechtigterArtText` varchar(100) DEFAULT NULL, "
                + "`bevollmaechtigterAktienregisterIdent` int(11) NOT NULL, "
                + "`bevollmaechtigterTitel` varchar(30) DEFAULT NULL, "
                + "`bevollmaechtigterName` varchar(80) DEFAULT NULL, "
                + "`bevollmaechtigterVorname` varchar(80) DEFAULT NULL, "
                + "`bevollmaechtigterZusatz1` varchar(80) DEFAULT NULL, "
                + "`bevollmaechtigterZusatz2` varchar(80) DEFAULT NULL, "
                + "`bevollmaechtigterStrasse` varchar(80) DEFAULT NULL, "
                + "`bevollmaechtigterPlz` varchar(20) DEFAULT NULL, "
                + "`bevollmaechtigterOrt` varchar(80) DEFAULT NULL, "
                + "`bevollmaechtigterEMail` varchar(100) DEFAULT NULL, " + "`eingabeDatum` varchar(50) DEFAULT NULL, "
                + "`eingabeOrt` varchar(50) DEFAULT NULL, " + "`pruefstatus` int(11) NOT NULL, "
                + "`abgelehntWeil` int(11) NOT NULL, " + "`abgelehntWeilText` varchar(300) DEFAULT NULL, "
                + "`bevollmaechtigterAusgefuehrtArt` int(11) NOT NULL, "
                + "`bevollmaechtigterAusgefuehrtIdent` int(11) NOT NULL, "
                + "`bevollmaechtigterAusgefuehrtIstGesetzlich` int(11) NOT NULL, " + "PRIMARY KEY (`ident`), "
                + "KEY `IDX_ERTEILT` (`erteiltVonIdent`), "
                + "KEY `IDX_BEVOLL` (`bevollmaechtigterAktienregisterIdent`), "
                + "KEY `IDX_BEVOLLGEPRUEFT` (`bevollmaechtigterAusgefuehrtIdent`) " + ") ");
        return rc;
    }

    /**************************deleteAll Löschen aller Datensätze  eines Mandanten****************/
    public int deleteAll() {
        dbBundle.dbBasis.resetInterneIdentVorlaeufigeVollmacht();
        return dbBundle.dbLowLevel
                .deleteAlle("DELETE FROM " + dbBundle.getSchemaMandant() + "tbl_vorlaeufigeVollmacht;");
    }

    //	/**************************setzt aktuelle Mandantennummer bei allen Datensätzen****************/
    //	public int updateMandant(){
    //		return dbBundle.dbLowLevel.rawUpdateMandant(dbBundle.getSchemaMandant()+"tbl_vorlaeufigeVollmacht");
    //	}

    public void reorgInterneIdent() {
        int lMax = dbBundle.dbLowLevel.liefereHoechsteIdentOhneMandant(
                "SELECT MAX(ident) FROM " + dbBundle.getSchemaMandant() + "tbl_vorlaeufigeVollmacht;");
        if (lMax != -1) {
            dbBundle.dbBasis.resetInterneIdentVorlaeufigeVollmacht(lMax);
        }
    }

    /********** dekodiert die aktuelle Position aus ergebnis  und gibt dieses zurück******/
    EclVorlaeufigeVollmacht decodeErgebnis(ResultSet pErgebnis) {

        EclVorlaeufigeVollmacht lEclReturn = new EclVorlaeufigeVollmacht();

        try {
            lEclReturn.ident = pErgebnis.getInt("ident");
            lEclReturn.db_version = pErgebnis.getLong("db_version");

            lEclReturn.storniert = pErgebnis.getInt("storniert");
            lEclReturn.erteiltVonArt = pErgebnis.getInt("erteiltVonArt");
            lEclReturn.erteiltVonIdent = pErgebnis.getInt("erteiltVonIdent");
            lEclReturn.eMailVollmachtgeber = pErgebnis.getString("eMailVollmachtgeber");
            lEclReturn.bevollmaechtigterArt = pErgebnis.getInt("bevollmaechtigterArt");
            lEclReturn.bevollmaechtigterArtText = pErgebnis.getString("bevollmaechtigterArtText");
            lEclReturn.bevollmaechtigterAktienregisterIdent = pErgebnis.getInt("bevollmaechtigterAktienregisterIdent");
            lEclReturn.bevollmaechtigterTitel = pErgebnis.getString("bevollmaechtigterTitel");
            lEclReturn.bevollmaechtigterName = pErgebnis.getString("bevollmaechtigterName");
            lEclReturn.bevollmaechtigterVorname = pErgebnis.getString("bevollmaechtigterVorname");
            lEclReturn.bevollmaechtigterZusatz1 = pErgebnis.getString("bevollmaechtigterZusatz1");
            lEclReturn.bevollmaechtigterZusatz2 = pErgebnis.getString("bevollmaechtigterZusatz2");
            lEclReturn.bevollmaechtigterStrasse = pErgebnis.getString("bevollmaechtigterStrasse");
            lEclReturn.bevollmaechtigterPlz = pErgebnis.getString("bevollmaechtigterPlz");
            lEclReturn.bevollmaechtigterOrt = pErgebnis.getString("bevollmaechtigterOrt");
            lEclReturn.bevollmaechtigterEMail = pErgebnis.getString("bevollmaechtigterEMail");
            lEclReturn.eingabeDatum = pErgebnis.getString("eingabeDatum");
            lEclReturn.eingabeOrt = pErgebnis.getString("eingabeOrt");
            lEclReturn.pruefstatus = pErgebnis.getInt("pruefstatus");
            lEclReturn.abgelehntWeil = pErgebnis.getInt("abgelehntWeil");
            lEclReturn.abgelehntWeilText = pErgebnis.getString("abgelehntWeilText");
            lEclReturn.bevollmaechtigterAusgefuehrtArt = pErgebnis.getInt("bevollmaechtigterAusgefuehrtArt");
            lEclReturn.bevollmaechtigterAusgefuehrtIdent = pErgebnis.getInt("bevollmaechtigterAusgefuehrtIdent");
            lEclReturn.bevollmaechtigterAusgefuehrtIstGesetzlich = pErgebnis
                    .getInt("bevollmaechtigterAusgefuehrtIstGesetzlich");
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
    private int anzfelder = 26; /*Anpassen auf Anzahl der Felder pro Datensatz*/

    private void fuellePreparedStatementKomplett(PreparedStatement pPStm, int pOffset, EclVorlaeufigeVollmacht pEcl) {

        int startOffset = pOffset; /*Nur erforderlich zum Überprüfen von Programmierfehlern - Feldanzahl*/

        try {
            pPStm.setInt(pOffset, pEcl.ident);
            pOffset++;
            pPStm.setLong(pOffset, pEcl.db_version);
            pOffset++;

            pPStm.setInt(pOffset, pEcl.storniert);
            pOffset++;
            pPStm.setInt(pOffset, pEcl.erteiltVonArt);
            pOffset++;
            pPStm.setInt(pOffset, pEcl.erteiltVonIdent);
            pOffset++;
            pPStm.setString(pOffset, pEcl.eMailVollmachtgeber);
            pOffset++;
            pPStm.setInt(pOffset, pEcl.bevollmaechtigterArt);
            pOffset++;
            pPStm.setString(pOffset, pEcl.bevollmaechtigterArtText);
            pOffset++;
            pPStm.setInt(pOffset, pEcl.bevollmaechtigterAktienregisterIdent);
            pOffset++;
            pPStm.setString(pOffset, pEcl.bevollmaechtigterTitel);
            pOffset++;
            pPStm.setString(pOffset, pEcl.bevollmaechtigterName);
            pOffset++;
            pPStm.setString(pOffset, pEcl.bevollmaechtigterVorname);
            pOffset++;
            pPStm.setString(pOffset, pEcl.bevollmaechtigterZusatz1);
            pOffset++;
            pPStm.setString(pOffset, pEcl.bevollmaechtigterZusatz2);
            pOffset++;
            pPStm.setString(pOffset, pEcl.bevollmaechtigterStrasse);
            pOffset++;
            pPStm.setString(pOffset, pEcl.bevollmaechtigterPlz);
            pOffset++;
            pPStm.setString(pOffset, pEcl.bevollmaechtigterOrt);
            pOffset++;
            pPStm.setString(pOffset, pEcl.bevollmaechtigterEMail);
            pOffset++;
            pPStm.setString(pOffset, pEcl.eingabeDatum);
            pOffset++;
            pPStm.setString(pOffset, pEcl.eingabeOrt);
            pOffset++;
            pPStm.setInt(pOffset, pEcl.pruefstatus);
            pOffset++;
            pPStm.setInt(pOffset, pEcl.abgelehntWeil);
            pOffset++;
            pPStm.setString(pOffset, pEcl.abgelehntWeilText);
            pOffset++;
            pPStm.setInt(pOffset, pEcl.bevollmaechtigterAusgefuehrtArt);
            pOffset++;
            pPStm.setInt(pOffset, pEcl.bevollmaechtigterAusgefuehrtIdent);
            pOffset++;
            pPStm.setInt(pOffset, pEcl.bevollmaechtigterAusgefuehrtIstGesetzlich);
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
    public int insert(EclVorlaeufigeVollmacht pEcl) {

        int erg = 0;

        /* neue InterneIdent vergeben */
        erg = dbBasis.getInterneIdentVorlaeufigeVollmacht();
        if (erg < 1) {
            CaBug.drucke("002");
            return (erg);
        }

        pEcl.ident = erg;

        try {

            /*Felder Neuanlage füllen*/
            String lSql = "INSERT INTO " + dbBundle.getSchemaMandant() + "tbl_vorlaeufigeVollmacht " + "("
                    + "ident, db_version, storniert, erteiltVonArt, erteiltVonIdent, eMailVollmachtgeber, "
                    + "bevollmaechtigterArt, bevollmaechtigterArtText, bevollmaechtigterAktienregisterIdent, "
                    + "bevollmaechtigterTitel, bevollmaechtigterName, bevollmaechtigterVorname, "
                    + "bevollmaechtigterZusatz1, bevollmaechtigterZusatz2, bevollmaechtigterStrasse, "
                    + "bevollmaechtigterPlz, bevollmaechtigterOrt, bevollmaechtigterEMail, eingabeDatum, eingabeOrt, "
                    + "pruefstatus, abgelehntWeil, abgelehntWeilText, bevollmaechtigterAusgefuehrtArt, bevollmaechtigterAusgefuehrtIdent, bevollmaechtigterAusgefuehrtIstGesetzlich "
                    + ")" + "VALUES (" + "?, ?, ?, ?, ?, ?, " + "?, ?, ?, " + "?, ?, ?, " + "?, ?, ?,"
                    + "?, ?, ?, ?, ?, " + "?, ?, ?, ?, ?, ? " + ")";
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
            return (-1);
        }

        return (1);
    }

    private int readRaw(PreparedStatement lPStm) {
        int anzInArray = 0;

        try {
            ResultSet lErgebnis = lPStm.executeQuery();
            lErgebnis.last();
            anzInArray = lErgebnis.getRow();
            lErgebnis.beforeFirst();

            ergebnisArray = new EclVorlaeufigeVollmacht[anzInArray];

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

    /**Ident müssen übergeben werden.
     * 
     * Return-Wert = Anzahl der gefundenen Sätze (oder Fehlermeldung <0)*/
    public int read(EclVorlaeufigeVollmacht pEcl) {
        PreparedStatement lPStm = null;

        try {
            String lSql = "SELECT * from " + dbBundle.getSchemaMandant() + "tbl_vorlaeufigeVollmacht where "
                    + "ident=? " + "ORDER BY ident;";
            lPStm = verbindung.prepareStatement(lSql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            lPStm.setInt(1, pEcl.ident);
            return readRaw(lPStm);
        } catch (Exception e) {
            CaBug.drucke("003");
            System.err.println(" " + e.getMessage());
            return (-1);
        }
    }

    /**Ident müssen übergeben werden.
     * 
     * Return-Wert = Anzahl der gefundenen Sätze (oder Fehlermeldung <0)*/
    public int readIdent(int pIdent) {
        PreparedStatement lPStm = null;

        try {
            String lSql = "SELECT * from " + dbBundle.getSchemaMandant() + "tbl_vorlaeufigeVollmacht where "
                    + "ident=? " + "ORDER BY ident;";
            lPStm = verbindung.prepareStatement(lSql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            lPStm.setInt(1, pIdent);
            return readRaw(lPStm);
        } catch (Exception e) {
            CaBug.drucke("003");
            System.err.println(" " + e.getMessage());
            return (-1);
        }
    }

    public int readErteiltVonAktionaer(int pAktienregisterIdent) {
        PreparedStatement lPStm = null;

        try {
            String lSql = "SELECT * from " + dbBundle.getSchemaMandant() + "tbl_vorlaeufigeVollmacht where "
                    + "erteiltVonIdent=? AND erteiltVonArt=1 " + "ORDER BY ident;";
            lPStm = verbindung.prepareStatement(lSql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            lPStm.setInt(1, pAktienregisterIdent);
            return readRaw(lPStm);
        } catch (Exception e) {
            CaBug.drucke("003");
            System.err.println(" " + e.getMessage());
            return (-1);
        }
    }

    /**Es können zwei gültige Vollmachten geben - eine gesetzliche, und eine an Dritte*/
    public int readErteiltUndGueltigVonAktionaer(int pAktienregisterIdent) {
        PreparedStatement lPStm = null;

        try {
            String lSql = "SELECT * from " + dbBundle.getSchemaMandant() + "tbl_vorlaeufigeVollmacht where "
                    + "erteiltVonIdent=? AND erteiltVonArt=1 AND storniert=0 " + "ORDER BY ident;";
            lPStm = verbindung.prepareStatement(lSql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            lPStm.setInt(1, pAktienregisterIdent);
            return readRaw(lPStm);
        } catch (Exception e) {
            CaBug.drucke("003");
            System.err.println(" " + e.getMessage());
            return (-1);
        }
    }

    public int readErteiltVonPerson(int pIdent) {
        PreparedStatement lPStm = null;

        try {
            String lSql = "SELECT * from " + dbBundle.getSchemaMandant() + "tbl_vorlaeufigeVollmacht where "
                    + "erteiltVonIdent=? AND erteiltVonArt=2 " + "ORDER BY ident;";
            lPStm = verbindung.prepareStatement(lSql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            lPStm.setInt(1, pIdent);
            return readRaw(lPStm);
        } catch (Exception e) {
            CaBug.drucke("003");
            System.err.println(" " + e.getMessage());
            return (-1);
        }
    }

    public int readGegebenAnAktionaer(int pIdent) {
        PreparedStatement lPStm = null;

        try {
            String lSql = "SELECT * from " + dbBundle.getSchemaMandant() + "tbl_vorlaeufigeVollmacht where "
                    + "bevollmaechtigterAusgefuehrtIdent=? AND bevollmaechtigterArt=1 " + "ORDER BY ident;";
            lPStm = verbindung.prepareStatement(lSql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            lPStm.setInt(1, pIdent);
            return readRaw(lPStm);
        } catch (Exception e) {
            CaBug.drucke("003");
            System.err.println(" " + e.getMessage());
            return (-1);
        }
    }

    public int readGegebenAnPerson(int pIdent) {
        PreparedStatement lPStm = null;

        try {
            String lSql = "SELECT * from " + dbBundle.getSchemaMandant() + "tbl_vorlaeufigeVollmacht where "
                    + "bevollmaechtigterAusgefuehrtIdent=? AND bevollmaechtigterArt=2 " + "ORDER BY ident;";
            lPStm = verbindung.prepareStatement(lSql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            lPStm.setInt(1, pIdent);
            return readRaw(lPStm);
        } catch (Exception e) {
            CaBug.drucke("003");
            System.err.println(" " + e.getMessage());
            return (-1);
        }
    }

    /**Update. 
     * 
     * Returnwert:
     * pfXyWurdeVonAnderemBenutzerVeraendert
     * -1 => unbekannter Fehler
     * 1 = Update wurde durchgeführt.
     * 
     */
    public int update(EclVorlaeufigeVollmacht pEcl) {

        pEcl.db_version++;

        try {

            String lSql = "UPDATE " + dbBundle.getSchemaMandant() + "tbl_vorlaeufigeVollmacht SET "
                    + "ident=?, db_version=?, storniert=?, erteiltVonArt=?, erteiltVonIdent=?, eMailVollmachtgeber=?, "
                    + "bevollmaechtigterArt=?, bevollmaechtigterArtText=?, bevollmaechtigterAktienregisterIdent=?, "
                    + "bevollmaechtigterTitel=?, bevollmaechtigterName=?, bevollmaechtigterVorname=?, "
                    + "bevollmaechtigterZusatz1=?, bevollmaechtigterZusatz2=?, bevollmaechtigterStrasse=?, "
                    + "bevollmaechtigterPlz=?, bevollmaechtigterOrt=?, bevollmaechtigterEMail=?, eingabeDatum=?, eingabeOrt=?, "
                    + "pruefstatus=?, abgelehntWeil=?, abgelehntWeilText=?, bevollmaechtigterAusgefuehrtArt=?, bevollmaechtigterAusgefuehrtIdent=?, bevollmaechtigterAusgefuehrtIstGesetzlich=? "
                    + "WHERE " + " ident=? AND db_version=? ";

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

    /**Return-Werte:
     * pfXyWurdeVonAnderemBenutzerVeraendert
     * -1 => undefinierter Fehler
     * 1 => Löschen erfolgreich
     */
    public int delete(int pIdent) {
        try {

            String sql = "DELETE FROM " + dbBundle.getSchemaMandant() + "tbl_vorlaeufigeVollmacht WHERE ident=?";

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
