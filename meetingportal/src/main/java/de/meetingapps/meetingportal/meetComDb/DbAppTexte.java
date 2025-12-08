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

/*TODO _Apptexte: noch nicht final auf Schemas umgestellt. Unklar, wie das fachlich gehandhabt werden soll. Aber gehört ja eh noch überarbeitet ...*/

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import de.meetingapps.meetingportal.meetComAllg.CaBug;
import de.meetingapps.meetingportal.meetComAllg.CaFehler;
import de.meetingapps.meetingportal.meetComEntities.EclAppTexte;

public class DbAppTexte {

    private Connection verbindung = null;
    private DbBasis dbBasis = null;
    private DbBundle dbBundle = null;

    EclAppTexte ergebnisArray[] = null;

    /*************************Initialisierung***************************/
    public DbAppTexte(DbBundle pDbBundle) {
        /* Verbindung in lokale Daten eintragen*/
        if (pDbBundle == null) {
            CaBug.drucke("DbAppTexte.init 001 - dbBundle nicht initialisiert");
            return;
        }
        if (pDbBundle.dbBasis == null) {
            CaBug.drucke("DbAppTexte.init 002 - dbBasis nicht initialisiert");
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
    public EclAppTexte[] ergebnis() {
        return ergebnisArray;
    }

    /**********Liefert pN-tes Element des Ergebnisses der read*Methoden**************
     * pN geht von 0 bis anzErgebnis-1*/
    public EclAppTexte ergebnisPosition(int pN) {
        if (ergebnisArray == null) {
            CaBug.drucke("DbAppTexte.ergebnisPosition 001");
            return null;
        }
        if (pN < 0) {
            CaBug.drucke("DbAppTexte.ergebnisPosition 002");
            return null;
        }
        if (pN >= ergebnisArray.length) {
            CaBug.drucke("DbAppTexte.ergebnisPosition 003");
            return null;
        }
        return ergebnisArray[pN];
    }

    public int createTable() {
        int rc = 0;
        DbLowLevel lDbLowLevel = new DbLowLevel(dbBundle);
        rc = lDbLowLevel.createTable("CREATE TABLE " + dbBundle.getSchemaAllgemein() + "tbl_apptexte ( "
                + "`mandant` int(11) NOT NULL, " + "`sprache` int(11) NOT NULL, " + "`seitennummer` int(11) NOT NULL, "
                + "`ident` int(11) NOT NULL, " + "`lfdNummer` int(11) NOT NULL, "
                + "`letzteVersion` int(11) DEFAULT NULL, " + "`formatierung` int(11) DEFAULT '0', "
                + "`anzeigeText` varchar(400) DEFAULT NULL, "
                + "PRIMARY KEY (`mandant`,`sprache`,`seitennummer`,`ident`,`lfdNummer`) " + ") "

        );
        return rc;
    }

    /**************************deleteAll Löschen aller Datensätze  eines Mandanten****************/
    public int deleteAll() {
        int erg = 0;

        try {

            String sql1 = "DELETE FROM " + dbBundle.getSchemaAllgemein() + "tbl_appTexte where mandant=?;";
            PreparedStatement pstm1 = verbindung.prepareStatement(sql1);
            pstm1.setInt(1, dbBundle.clGlobalVar.mandant);

            erg = 0; /*Falls Duplicate Key, dann wird exception geschmissen und erg wohl nicht gefüllt!*/
            erg = pstm1.executeUpdate();

            pstm1.close();
        } catch (Exception e2) {
            CaBug.drucke("DbAppTexte.deleteAll 001");
            System.err.println(" " + e2.getMessage());
            return (erg);
        }

        return 1;
    }

    /********** dekodiert die aktuelle Position aus ergebnis  und gibt dieses zurück******/
    EclAppTexte decodeErgebnis(ResultSet pErgebnis) {

        EclAppTexte lAppText = new EclAppTexte();

        try {
            lAppText.mandant = pErgebnis.getInt("mandant");
            lAppText.sprache = pErgebnis.getInt("sprache");
            lAppText.seitennummer = pErgebnis.getInt("seitennummer");
            lAppText.ident = pErgebnis.getInt("ident");
            lAppText.lfdNummer = pErgebnis.getInt("lfdNummer");
            lAppText.letzteVersion = pErgebnis.getInt("letzteVersion");
            lAppText.formatierung = pErgebnis.getInt("formatierung");
            lAppText.anzeigetext = pErgebnis.getString("anzeigetext");
        } catch (Exception e) {
            CaBug.drucke("DbAppTexte.decodeErgebnis 001");
            System.err.println(" " + e.getMessage());
        }

        return lAppText;
    }

    /********************* Fuellen Prepared Statement mit allen Feldern, beginnend bei offset.*****************
     * Kann sowohl für Insert, als auch für update verwendet werden.
     * 
     * offset= Startposition des ersten Feldes (also z.B. 1)
     */
    private int anzfelder = 8; /*Anpassen auf Anzahl der Felder pro Datensatz*/

    private void fuellePreparedStatementKomplett(PreparedStatement pPStm, int pOffset, EclAppTexte pAppText) {

        int startOffset = pOffset; /*Nur erforderlich zum Überprüfen von Programmierfehlern - Feldanzahl*/

        try {
            pPStm.setInt(pOffset, pAppText.mandant);
            pOffset++;
            pPStm.setInt(pOffset, pAppText.sprache);
            pOffset++;
            pPStm.setInt(pOffset, pAppText.seitennummer);
            pOffset++;
            pPStm.setInt(pOffset, pAppText.ident);
            pOffset++;
            pPStm.setInt(pOffset, pAppText.lfdNummer);
            pOffset++;
            pPStm.setInt(pOffset, pAppText.letzteVersion);
            pOffset++;
            pPStm.setInt(pOffset, pAppText.formatierung);
            pOffset++;
            pPStm.setString(pOffset, pAppText.anzeigetext);
            pOffset++;

            if (pOffset - startOffset != anzfelder) {
                /*Nur wg. Überprüfung auf Programmierfehler - alle Felder berücksichtigt?*/
                CaBug.drucke("DbAppTexte.fuellePreparedStatementKomplett 002");
            }

        } catch (SQLException e) {
            CaBug.drucke("DbAppTexte.fuellePreparedStatementKomplett 001");
            e.printStackTrace();
        }

    }

    /**Insert
     * 
     * Feld mandant muß vor Übergabe gesetzt werden!
     * 
     * Returnwert:
     * =1 => Insert erfolgreich
     * ansonsten: Fehler
     */
    public int insert(EclAppTexte pAppText) {

        int erg = 0;
        //		System.out.println("Insert: pAppText mandant="+pAppText.mandant+" sprache="+pAppText.sprache+" seitennummer="+pAppText.seitennummer+" ident="+pAppText.ident
        //				+" lfdnummer="+pAppText.lfdNummer);

        /* Start Transaktion */
        dbBasis.beginTransaction();

        try {

            /*Felder Neuanlage füllen*/
            String lSql = "INSERT INTO " + dbBundle.getSchemaAllgemein() + "tbl_appTexte " + "("
                    + "mandant, sprache, seitennummer, ident, lfdNummer, letzteVersion, formatierung, anzeigetext "
                    + ")" + "VALUES (" + "?, ?, ?, ?, ?, ?, ?, ? " + ")";
            PreparedStatement lPStm = verbindung.prepareStatement(lSql, ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);

            fuellePreparedStatementKomplett(lPStm, 1, pAppText);

            erg = lPStm.executeUpdate();
            lPStm.close();
        } catch (Exception e2) {
            CaBug.drucke("DbAppTexte.insert 001");
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

    /**Sprache, Seitennummer, Ident und Mandant müssen übergeben werden*/
    public int read(EclAppTexte pAppVersion) {
        int anzInArray = 0;
        PreparedStatement lPStm = null;

        try {
            String lSql = "SELECT * from " + dbBundle.getSchemaAllgemein() + "tbl_appTexte where " + "mandant=? AND "
                    + "sprache=? AND " + "seitennummer=? AND " + "ident=? ORDER BY lfdNummer;";
            lPStm = verbindung.prepareStatement(lSql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            lPStm.setInt(1, pAppVersion.mandant);
            lPStm.setInt(2, pAppVersion.sprache);
            lPStm.setInt(3, pAppVersion.seitennummer);
            lPStm.setInt(4, pAppVersion.ident);

            ResultSet lErgebnis = lPStm.executeQuery();
            lErgebnis.last();
            anzInArray = lErgebnis.getRow();
            lErgebnis.beforeFirst();

            ergebnisArray = new EclAppTexte[anzInArray];

            int i = 0;
            while (lErgebnis.next() == true) {
                ergebnisArray[i] = this.decodeErgebnis(lErgebnis);
                i++;
            }
            lErgebnis.close();
            lPStm.close();

        } catch (Exception e) {
            CaBug.drucke("DbAppTexte.read 003");
            System.err.println(" " + e.getMessage());
            return (-1);
        }
        return (anzInArray);
    }

    /**Sprache, Seitennummer, Ident und Mandant müssen übergeben werden*/
    public int read_maxVersion(int pMandant, int pSprache) {
        int ergebnis = 0;
        PreparedStatement lPStm = null;

        try {
            String lSql = "SELECT MAX(letzteVersion) from " + dbBundle.getSchemaAllgemein() + "tbl_appTexte where "
                    + "mandant=? AND sprache=? ";
            lPStm = verbindung.prepareStatement(lSql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            lPStm.setInt(1, pMandant);
            lPStm.setInt(2, pSprache);

            ResultSet lErgebnis = lPStm.executeQuery();
            lErgebnis.beforeFirst();
            if (lErgebnis.next() == true) {
                ergebnis = lErgebnis.getInt(1);
            } else {
                ergebnis = 0;
            }

            lErgebnis.close();
            lPStm.close();

        } catch (Exception e) {
            CaBug.drucke("DbAppTexte.read_maxVersion 003");
            System.err.println(" " + e.getMessage());
            return (-1);
        }
        return (ergebnis);
    }

    /**abVersion: ab dieser Versionsnummer wird eingelesen (also >=)*/
    public int read_updateTexte(int pMandant, int pSprache, int abVersion) {
        int anzInArray = 0;
        PreparedStatement lPStm = null;

        try {
            String lSql = "SELECT * from " + dbBundle.getSchemaAllgemein() + "tbl_appTexte where " + "mandant=? AND "
                    + "sprache=? AND " + "letzteVersion>=? " + "ORDER BY seitennummer, ident, lfdNummer;";
            lPStm = verbindung.prepareStatement(lSql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            lPStm.setInt(1, pMandant);
            lPStm.setInt(2, pSprache);
            lPStm.setInt(3, abVersion);

            ResultSet lErgebnis = lPStm.executeQuery();
            lErgebnis.last();
            anzInArray = lErgebnis.getRow();
            lErgebnis.beforeFirst();

            ergebnisArray = new EclAppTexte[anzInArray];

            int i = 0;
            while (lErgebnis.next() == true) {
                ergebnisArray[i] = this.decodeErgebnis(lErgebnis);
                i++;
            }
            lErgebnis.close();
            lPStm.close();

        } catch (Exception e) {
            CaBug.drucke("DbAppTexte.read_updateTexte 003");
            System.err.println(" " + e.getMessage());
            return (-1);
        }
        return (anzInArray);
    }

    /**Update. 
     * 
     * Feld mandant muß gefüllt mit übergeben werden.
     * 
     */
    public int update(EclAppTexte pAppVersion) {

        try {

            String lSql = "UPDATE " + dbBundle.getSchemaAllgemein() + "tbl_appTexte SET "
                    + "mandant=?, sprache=?, seitennummer=?, ident=?, lfdNummer=?, letzteVersion=?, formatierung=?, anzeigetext=? "
                    + "WHERE " + "sprache=? AND seitennummer=? AND ident=? AND lfdNummer=? AND " + "mandant=?";

            PreparedStatement lPStm = verbindung.prepareStatement(lSql);
            fuellePreparedStatementKomplett(lPStm, 1, pAppVersion);
            lPStm.setInt(anzfelder + 1, pAppVersion.sprache);
            lPStm.setInt(anzfelder + 2, pAppVersion.seitennummer);
            lPStm.setInt(anzfelder + 3, pAppVersion.ident);
            lPStm.setInt(anzfelder + 4, pAppVersion.lfdNummer);
            lPStm.setInt(anzfelder + 5, pAppVersion.mandant);

            int ergebnis = lPStm.executeUpdate();
            lPStm.close();
            if (ergebnis == 0) {
                return (CaFehler.pfXyWurdeVonAnderemBenutzerVeraendert);
            }
        } catch (Exception e1) {
            CaBug.drucke("DbAppTexte.update 001");
            System.err.println(" " + e1.getMessage());
            return (CaFehler.pfdXyBereitsVorhanden);
        }

        return (1);
    }

    public int delete(int pSprache, int pSeitennummer, int pIdent, int pMandant) {
        //		System.out.println("pSprache="+pSprache+" pSeitennummer="+pSeitennummer+" pIdent="+pIdent+" pMandant="+pMandant);
        try {

            String sql = "DELETE FROM " + dbBundle.getSchemaAllgemein()
                    + "tbl_appTexte WHERE sprache=? AND seitennummer=? AND ident=? AND mandant=? ";

            PreparedStatement pstm1 = verbindung.prepareStatement(sql);
            pstm1.setInt(1, pSprache);
            pstm1.setInt(2, pSeitennummer);
            pstm1.setInt(3, pIdent);
            pstm1.setInt(4, pMandant);

            int ergebnis1 = pstm1.executeUpdate();
            pstm1.close();
            if (ergebnis1 == 0) {
                return (CaFehler.pfXyWurdeVonAnderemBenutzerVeraendert);
            }
        } catch (Exception e1) {
            CaBug.drucke("DbAppTexte.delete 001");
            System.err.println(" " + e1.getMessage());
            return (CaFehler.pfdXyBereitsVorhanden);
        }

        return (1);
    }

}
