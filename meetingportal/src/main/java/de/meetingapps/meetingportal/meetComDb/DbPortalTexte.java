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
import de.meetingapps.meetingportal.meetComEntities.EclPortalText;

public class DbPortalTexte  extends DbRootExecute {

    private Connection verbindung = null;
    private DbBasis dbBasis = null;
    private DbBundle dbBundle = null;

    EclPortalText ergebnisArray[] = null;
    int maxIdentGesamt = 0;

    /*************************Initialisierung***************************/
    public DbPortalTexte(DbBundle pDbBundle) {
        /* Verbindung in lokale Daten eintragen*/
        if (pDbBundle == null) {
            CaBug.drucke("DbPortalTexte.init 001 - dbBundle nicht initialisiert");
            return;
        }
        if (pDbBundle.dbBasis == null) {
            CaBug.drucke("DbPortalTexte.init 002 - dbBasis nicht initialisiert");
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
    public EclPortalText[] ergebnis() {
        return ergebnisArray;
    }

    /**Nach read_all kann damit die höchste gelesene identGesamt abgefragt werden*/
    public int ergebnisMaxIdentGesamt() {
        return maxIdentGesamt;
    }

    /**********Liefert pN-tes Element des Ergebnisses der read*Methoden**************
     * pN geht von 0 bis anzErgebnis-1*/
    public EclPortalText ergebnisPosition(int pN) {
        if (ergebnisArray == null) {
            CaBug.drucke("DbPortalTexte.ergebnisPosition 001");
            return null;
        }
        if (pN < 0) {
            CaBug.drucke("DbPortalTexte.ergebnisPosition 002");
            return null;
        }
        if (pN >= ergebnisArray.length) {
            CaBug.drucke("DbPortalTexte.ergebnisPosition 003");
            return null;
        }
        return ergebnisArray[pN];
    }

    public int createTable(boolean mitMandant) {
        int rc = 0;
        String schema = "";
        if (mitMandant) {
            schema = dbBundle.getSchemaMandant();
            rc = hCreateTable(schema, "tbl_portaltexte");
        } else {
            schema = dbBundle.getSchemaAllgemein();
            rc = hCreateTable(schema, "tbl_portaltexte");
            if (rc < 1) {
                return rc;
            }
            rc = hCreateTable(schema, "tbl_portaltextebo");
        }
        return rc;
    }

    private int hCreateTable(String pSchema, String pTableName) {
        int rc;
        DbLowLevel lDbLowLevel = new DbLowLevel(dbBundle);
        rc = lDbLowLevel.createTable("CREATE TABLE " + pSchema + pTableName + " ( " + "`mandant` int(11) NOT NULL, "
                + "`basisSet` int(11) NOT NULL, " + "`db_version` bigint(20) DEFAULT NULL, "
                + "`textInPortal` int(11) NOT NULL, " + "`identGesamt` int(11) NOT NULL, "
                + "`verbundenMitIdentGesamt` int(11) NOT NULL, " + "`lfdNummer` int(11) NOT NULL, "
                + "`textInApp` int(11) NOT NULL, " + "`seitennummer` int(11) NOT NULL, " + "`ident` int(11) NOT NULL, "
                + "`sprache` int(11) NOT NULL, " + "`seitenName` varchar(50) DEFAULT NULL, "
                + "`beschreibung` varchar(400) DEFAULT NULL, " + "`letzteVersion` int(11) NOT NULL, "
                + "`portalVonStandardVerwenden` int(11) NOT NULL, " + "`portalText` varchar(400) DEFAULT NULL, "
                + "`portalAdaptivAbweichend` int(11) NOT NULL, " + "`portalAdaptivText` varchar(400) DEFAULT NULL, "
                + "`appAbweichend` int(11) NOT NULL, " + "`appText` varchar(400) DEFAULT NULL, "
                + "PRIMARY KEY (`mandant`,`sprache`,`identGesamt`,`lfdNummer`, `basisSet`) " + ") "

        );
        return rc;
    }

    /**************************deleteAll Löschen aller Datensätze  eines Mandanten****************/
    public int deleteAll(boolean mitMandant) {

        DbLowLevel lDbLowLevel = new DbLowLevel(dbBundle);

        if (mitMandant) {
            lDbLowLevel
                    .deleteMandant("DELETE FROM " + dbBundle.getSchemaMandant() + "tbl_portaltexte where mandant=?;");
        } else {
            lDbLowLevel.deleteMandant("DELETE FROM " + dbBundle.getSchemaAllgemein() + "tbl_portaltexte;");
            lDbLowLevel.deleteMandant("DELETE FROM " + dbBundle.getSchemaAllgemein() + "tbl_portaltextebo;");
        }
        return 1;
    }

    /**************************setzt aktuelle Mandantennummer bei allen Datensätzen****************/
    public int updateMandant() {
        return dbBundle.dbLowLevel.rawUpdateMandant(dbBundle.getSchemaMandant() + "tbl_portaltexte");
    }

    /********** dekodiert die aktuelle Position aus ergebnis  und gibt dieses zurück******/
    EclPortalText decodeErgebnis(ResultSet pErgebnis) {

        EclPortalText lPortalText = new EclPortalText();
        int hInt = 0;

        try {
            lPortalText.mandant = pErgebnis.getInt("mandant");
            lPortalText.basisSet = pErgebnis.getInt("basisSet");
            lPortalText.db_version = pErgebnis.getLong("db_version");

            hInt = pErgebnis.getInt("textInPortal");
            if (hInt == 1) {
                lPortalText.textInPortal = true;
            } else {
                lPortalText.textInPortal = false;
            }

            lPortalText.identGesamt = pErgebnis.getInt("identGesamt");
            lPortalText.verbundenMitIdentGesamt = pErgebnis.getInt("verbundenMitIdentGesamt");
            lPortalText.lfdNummer = pErgebnis.getInt("lfdNummer");

            hInt = pErgebnis.getInt("textInApp");
            if (hInt == 1) {
                lPortalText.textInApp = true;
            } else {
                lPortalText.textInApp = false;
            }

            lPortalText.seitennummer = pErgebnis.getInt("seitennummer");
            lPortalText.ident = pErgebnis.getInt("ident");
            lPortalText.sprache = pErgebnis.getInt("sprache");
            
            lPortalText.seitenName = pErgebnis.getString("seitenName");
            if (lPortalText.seitenName==null) {
                lPortalText.seitenName="";
            }
            lPortalText.beschreibung = pErgebnis.getString("beschreibung");
            if (lPortalText.beschreibung==null) {
                lPortalText.beschreibung="";
            }
            
            lPortalText.letzteVersion = pErgebnis.getInt("letzteVersion");

            hInt = pErgebnis.getInt("portalVonStandardVerwenden");
            if (hInt == 1) {
                lPortalText.portalVonStandardVerwenden = true;
            } else {
                lPortalText.portalVonStandardVerwenden = false;
            }
            lPortalText.portalText = pErgebnis.getString("portalText");

            hInt = pErgebnis.getInt("portalAdaptivAbweichend");
            if (hInt == 1) {
                lPortalText.portalAdaptivAbweichend = true;
            } else {
                lPortalText.portalAdaptivAbweichend = false;
            }
            lPortalText.portalAdaptivText = pErgebnis.getString("portalAdaptivText");

            hInt = pErgebnis.getInt("appAbweichend");
            if (hInt == 1) {
                lPortalText.appAbweichend = true;
            } else {
                lPortalText.appAbweichend = false;
            }
            lPortalText.appText = pErgebnis.getString("appText");
        } catch (Exception e) {
            CaBug.drucke("DbPortalTexte.decodeErgebnis 001");
            System.err.println(" " + e.getMessage());
        }

        return lPortalText;
    }

    /********************* Fuellen Prepared Statement mit allen Feldern, beginnend bei offset.*****************
     * Kann sowohl für Insert, als auch für update verwendet werden.
     * 
     * offset= Startposition des ersten Feldes (also z.B. 1)
     */
    private int anzfelder = 20; /*Anpassen auf Anzahl der Felder pro Datensatz*/

    private void fuellePreparedStatementKomplett(PreparedStatement pPStm, int pOffset, EclPortalText pPortalText) {

        int startOffset = pOffset; /*Nur erforderlich zum Überprüfen von Programmierfehlern - Feldanzahl*/

        try {
            pPStm.setInt(pOffset, pPortalText.mandant);
            pOffset++;
            pPStm.setInt(pOffset, pPortalText.basisSet);
            pOffset++;
            pPStm.setLong(pOffset, pPortalText.db_version);
            pOffset++;

            if (pPortalText.textInPortal) {
                pPStm.setInt(pOffset, 1);
                pOffset++;
            } else {
                pPStm.setInt(pOffset, 0);
                pOffset++;
            }

            pPStm.setInt(pOffset, pPortalText.identGesamt);
            pOffset++;
            pPStm.setInt(pOffset, pPortalText.verbundenMitIdentGesamt);
            pOffset++;
            pPStm.setInt(pOffset, pPortalText.lfdNummer);
            pOffset++;

            if (pPortalText.textInApp) {
                pPStm.setInt(pOffset, 1);
                pOffset++;
            } else {
                pPStm.setInt(pOffset, 0);
                pOffset++;
            }

            pPStm.setInt(pOffset, pPortalText.seitennummer);
            pOffset++;
            pPStm.setInt(pOffset, pPortalText.ident);
            pOffset++;
            pPStm.setInt(pOffset, pPortalText.sprache);
            pOffset++;
            pPStm.setString(pOffset, pPortalText.seitenName);
            pOffset++;
            pPStm.setString(pOffset, pPortalText.beschreibung);
            pOffset++;
            pPStm.setInt(pOffset, pPortalText.letzteVersion);
            pOffset++;

            if (pPortalText.portalVonStandardVerwenden) {
                pPStm.setInt(pOffset, 1);
                pOffset++;
            } else {
                pPStm.setInt(pOffset, 0);
                pOffset++;
            }
            pPStm.setString(pOffset, pPortalText.portalText);
            pOffset++;

            if (pPortalText.portalAdaptivAbweichend) {
                pPStm.setInt(pOffset, 1);
                pOffset++;
            } else {
                pPStm.setInt(pOffset, 0);
                pOffset++;
            }
            pPStm.setString(pOffset, pPortalText.portalAdaptivText);
            pOffset++;

            if (pPortalText.appAbweichend) {
                pPStm.setInt(pOffset, 1);
                pOffset++;
            } else {
                pPStm.setInt(pOffset, 0);
                pOffset++;
            }
            pPStm.setString(pOffset, pPortalText.appText);
            pOffset++;

            if (pOffset - startOffset != anzfelder) {
                /*Nur wg. Überprüfung auf Programmierfehler - alle Felder berücksichtigt?*/
                CaBug.drucke("DbPortalTexte.fuellePreparedStatementKomplett 002");
            }

        } catch (SQLException e) {
            CaBug.drucke("DbPortalTexte.fuellePreparedStatementKomplett 001");
            e.printStackTrace();
        }

    }

    /**Insert
     * 
     * Feld mandant muß vor Übergabe gesetzt werden!
     * 
     * pUebergreifend (nur wenn mitMandant=false): Mandantenübergreifende
     * Texte für App
     * 
     * Returnwert:
     * =1 => Insert erfolgreich
     * ansonsten: Fehler
     */
    public int insert(EclPortalText pPortalText, boolean mitMandant, boolean pUebergreifend) {

        int erg = 0;
        String schema = "";
        String tablename = "";
        if (mitMandant) {
            schema = dbBundle.getSchemaMandant();
            tablename = "tbl_portaltexte";
            pPortalText.mandant = dbBundle.clGlobalVar.mandant;
        } else {
            schema = dbBundle.getSchemaAllgemein();
            if (pUebergreifend) {
                tablename = "tbl_portaltextebo";
            } else {
                tablename = "tbl_portaltexte";
            }
            pPortalText.mandant = 0;
        }

        /* Start Transaktion */
        dbBasis.beginTransaction();

        try {

            /*Felder Neuanlage füllen*/
            String lSql = "INSERT INTO " + schema + tablename + " ("
                    + "mandant, basisSet, db_version, textInPortal, identGesamt, verbundenMitIdentGesamt, lfdNummer, textInApp, seitennummer, ident, "
                    + "sprache, seitenName, beschreibung, letzteVersion, " + "portalVonStandardVerwenden, portalText, "
                    + "portalAdaptivAbweichend, portalAdaptivText, " + "appAbweichend, appText " + ")" + "VALUES ("
                    + "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, " + "?, ?, ?, ?, " + "?, ?, ?, ?, ?, ? " + ")";
            PreparedStatement lPStm = verbindung.prepareStatement(lSql, ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);

            fuellePreparedStatementKomplett(lPStm, 1, pPortalText);

            erg = executeUpdate(lPStm);
            lPStm.close();
        } catch (Exception e2) {
            CaBug.drucke("DbPortalTexte.insert 001");
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

    /**identGesamt, sprache müssen übergeben werden
     * 
     * pUebergreifend (nur wenn mitMandant=false): Mandantenübergreifende
     * Texte für App
     * */
    public int read(EclPortalText pPortalText, boolean mitMandant, boolean pUebergreifend, int pbasisSet) {
        int anzInArray = 0;

        String schema = "";
        int hMandant = 0;
        String tablename = "";
        if (mitMandant) {
            hMandant = dbBundle.clGlobalVar.mandant;
            schema = dbBundle.getSchemaMandant();
            tablename = "tbl_portaltexte";
        } else {
            schema = dbBundle.getSchemaAllgemein();
            if (pUebergreifend) {
                tablename = "tbl_portaltextebo";
            } else {
                tablename = "tbl_portaltexte";
            }
        }

        PreparedStatement lPStm = null;

        try {
            String lSql = "SELECT * from " + schema + tablename + " where " + "mandant=? AND " + "basisSet=? AND "
                    + "sprache=? AND " + "identGesamt=? ORDER BY identGesamt;";
            lPStm = verbindung.prepareStatement(lSql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            lPStm.setInt(1, hMandant);
            lPStm.setInt(2, pbasisSet);
            lPStm.setInt(3, pPortalText.sprache);
            lPStm.setInt(4, pPortalText.identGesamt);

            ResultSet lErgebnis = executeQuery(lPStm);
            lErgebnis.last();
            anzInArray = lErgebnis.getRow();
            lErgebnis.beforeFirst();

            ergebnisArray = new EclPortalText[anzInArray];

            int i = 0;
            while (lErgebnis.next() == true) {
                ergebnisArray[i] = this.decodeErgebnis(lErgebnis);
                i++;
            }
            lErgebnis.close();
            lPStm.close();

        } catch (Exception e) {
            CaBug.drucke("DbPortalTexte.read 003");
            System.err.println(" " + e.getMessage());
            return (-1);
        }
        return (anzInArray);
    }

    /** 
     * pUebergreifend (nur wenn mitMandant=false): Mandantenübergreifende
     * Texte für App
     * 
     * pSprache=-1: alle Sprachen werden eingelesen
     * */
    public int read_all(int pSprache, boolean mitMandant, boolean pUebergreifend, int pbasisSet) {
        return read_identGesamt(-1, pSprache, mitMandant, pUebergreifend, pbasisSet);
    }
    
    /**pIdentGesamt==-1 => es werden alle gelesen*/
    public int read_identGesamt(int pIdentGesamt, int pSprache, boolean mitMandant, boolean pUebergreifend, int pbasisSet) {
        int anzInArray = 0;

        String schema = "";
        int hMandant = 0;
        String tablename = "";
        if (mitMandant) {
            hMandant = dbBundle.clGlobalVar.mandant;
            schema = dbBundle.getSchemaMandant();
            tablename = "tbl_portaltexte";
        } else {
            schema = dbBundle.getSchemaAllgemein();
            if (pUebergreifend) {
                tablename = "tbl_portaltextebo";
            } else {
                tablename = "tbl_portaltexte";
            }
        }

        PreparedStatement lPStm = null;

        try {
            String lSql = "SELECT * from " + schema + tablename + " where " + "mandant=? AND basisSet=? ";
            if (pIdentGesamt != -1) {
                lSql = lSql + "AND identGesamt=? ";
            }
            if (pSprache != -1) {
                lSql = lSql + "AND sprache=? ";
            }
            lSql = lSql + "ORDER BY identGesamt, lfdNummer;";
            lPStm = verbindung.prepareStatement(lSql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            int lOffset=1;
            lPStm.setInt(lOffset, hMandant);lOffset++;
            lPStm.setInt(lOffset, pbasisSet);lOffset++;
            if (pIdentGesamt != -1) {
                lPStm.setInt(lOffset, pIdentGesamt);lOffset++;
            }
            if (pSprache != -1) {
                lPStm.setInt(lOffset, pSprache);lOffset++;
            }

            ResultSet lErgebnis = executeQuery(lPStm);
            lErgebnis.last();
            anzInArray = lErgebnis.getRow();
            lErgebnis.beforeFirst();

            ergebnisArray = new EclPortalText[anzInArray];

            int i = 0;
            maxIdentGesamt = 0;
            while (lErgebnis.next() == true) {
                ergebnisArray[i] = this.decodeErgebnis(lErgebnis);
                if (ergebnisArray[i].identGesamt > maxIdentGesamt) {
                    maxIdentGesamt = ergebnisArray[i].identGesamt;
                }
                i++;
            }
            lErgebnis.close();
            lPStm.close();

        } catch (Exception e) {
            CaBug.drucke("DbPortalTexte.read 003");
            System.err.println(" " + e.getMessage());
            return (-1);
        }
        return (anzInArray);
    }

    /**Sprache, und Mandant müssen übergeben werden
     * 
     * pUebergreifend (nur wenn mitMandant=false): Mandantenübergreifende
     * Texte für App
     * 
     * */
    public int read_maxVersion(int pSprache, boolean mitMandant, boolean pUebergreifend, int pbasisSet) {
        int ergebnis = 0;
        String schema = "";
        int hMandant = 0;
        String tablename = "";
        if (mitMandant) {
            hMandant = dbBundle.clGlobalVar.mandant;
            schema = dbBundle.getSchemaMandant();
            tablename = "tbl_portaltexte";
        } else {
            schema = dbBundle.getSchemaAllgemein();
            if (pUebergreifend) {
                tablename = "tbl_portaltextebo";
            } else {
                tablename = "tbl_portaltexte";
            }
        }

        PreparedStatement lPStm = null;

        try {
            String lSql = "SELECT MAX(letzteVersion) from " + schema + tablename + " where "
                    + "mandant=? AND basisSet=? AND sprache=? ";
            lPStm = verbindung.prepareStatement(lSql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            lPStm.setInt(1, hMandant);
            lPStm.setInt(2, pbasisSet);
            lPStm.setInt(3, pSprache);

            ResultSet lErgebnis = executeQuery(lPStm);
            lErgebnis.beforeFirst();
            if (lErgebnis.next() == true) {
                ergebnis = lErgebnis.getInt(1);
            } else {
                ergebnis = 0;
            }

            lErgebnis.close();
            lPStm.close();

        } catch (Exception e) {
            CaBug.drucke("DbPortalTexte.read_maxVersion 003");
            System.err.println(" " + e.getMessage());
            return (-1);
        }
        return (ergebnis);
    }

    /**Update. 
     * 
     * Alle Felder (ohne mandant) müssen mit übergeben werden.
     * 
     * pUebergreifend (nur wenn mitMandant=false): Mandantenübergreifende
     * Texte für App
     */
    public int update(EclPortalText pPortalText, boolean mitMandant, boolean pUebergreifend) {

        String schema = "";
        int hMandant = 0;
        String tablename = "";
        if (mitMandant) {
            hMandant = dbBundle.clGlobalVar.mandant;
            schema = dbBundle.getSchemaMandant();
            tablename = "tbl_portaltexte";
        } else {
            schema = dbBundle.getSchemaAllgemein();
            if (pUebergreifend) {
                tablename = "tbl_portaltextebo";
            } else {
                tablename = "tbl_portaltexte";
            }
        }

        try {
            String lSql = "UPDATE " + schema + tablename + " SET "
                    + "mandant=?, basisSet=?, db_version=?, textInPortal=?, identGesamt=?, verbundenMitIdentGesamt=?, lfdNummer=?, textInApp=?, seitennummer=?, ident=?, "
                    + "sprache=?, seitenName=?, beschreibung=?, letzteVersion=?, "
                    + "portalVonStandardVerwenden=?, portalText=?, "
                    + "portalAdaptivAbweichend=?, portalAdaptivText=?, " + "appAbweichend=?, appText=? " + "WHERE "
                    + "sprache=? AND identGesamt=? AND lfdNummer=? AND " + "mandant=? and basisSet=? ";

            pPortalText.mandant = hMandant;
            PreparedStatement lPStm = verbindung.prepareStatement(lSql);
            fuellePreparedStatementKomplett(lPStm, 1, pPortalText);
            lPStm.setInt(anzfelder + 1, pPortalText.sprache);
            lPStm.setInt(anzfelder + 2, pPortalText.identGesamt);
            lPStm.setInt(anzfelder + 3, pPortalText.lfdNummer);
            lPStm.setInt(anzfelder + 4, pPortalText.mandant);
            lPStm.setInt(anzfelder + 5, pPortalText.basisSet);

            int ergebnis = executeUpdate(lPStm);
            lPStm.close();
            if (ergebnis == 0) {
                return (CaFehler.pfXyWurdeVonAnderemBenutzerVeraendert);
            }
        } catch (Exception e1) {
            CaBug.drucke("DbPortalTexte.update 001");
            System.err.println(" " + e1.getMessage());
            return (CaFehler.pfdXyBereitsVorhanden);
        }

        return (1);
    }

    /**Update der App_Version für einen speziellen Text einer Sprache. 
     * 
     * pUebergreifend (nur wenn mitMandant=false): Mandantenübergreifende
     * Texte für App
     */
    public int updateAppVersion(int pIdentGesamt, int pSprache, int pNeueAppVersion, boolean mitMandant,
            boolean pUebergreifend) {

        String schema = "";
        int hMandant = 0;
        String tablename = "";
        if (mitMandant) {
            hMandant = dbBundle.clGlobalVar.mandant;
            schema = dbBundle.getSchemaMandant();
            tablename = "tbl_portaltexte";
        } else {
            schema = dbBundle.getSchemaAllgemein();
            if (pUebergreifend) {
                tablename = "tbl_portaltextebo";
            } else {
                tablename = "tbl_portaltexte";
            }
        }

        try {
            String lSql = "UPDATE " + schema + tablename + " SET " + "letzteVersion=? " + "WHERE "
                    + "sprache=? AND identGesamt=? AND mandant=?";

            PreparedStatement lPStm = verbindung.prepareStatement(lSql);
            lPStm.setInt(1, pNeueAppVersion);
            lPStm.setInt(2, pSprache);
            lPStm.setInt(3, pIdentGesamt);
            lPStm.setInt(4, hMandant);

            int ergebnis = executeUpdate(lPStm);
            lPStm.close();
            if (ergebnis == 0) {
                return (CaFehler.pfXyWurdeVonAnderemBenutzerVeraendert);
            }
        } catch (Exception e1) {
            CaBug.drucke("DbPortalTexte.updateAppVersion 001");
            System.err.println(" " + e1.getMessage());
            return (CaFehler.pfdXyBereitsVorhanden);
        }

        return (1);
    }

    /**Derzeit nur getestet und durchdacht für mitMandant=true*/
    public int speichereAll(EclPortalText[] pTexte, boolean mitMandant) {
        deleteAll(mitMandant);
        for (int i = 0; i < pTexte.length; i++) {
            pTexte[i].mandant = dbBundle.clGlobalVar.mandant;
            insert(pTexte[i], mitMandant, false);
        }
        return 1;
    }

    /**************************delete Löschen aller Datensätze  einer globalen Nummer***************
     * pUebergreifend (nur wenn mitMandant=false): Mandantenübergreifende
     * Texte für App
     * */
    public int delete(int pNummer, boolean mitMandant, boolean pUebergreifend, int pBasisSet) {
        int erg = 0;
        String schema = "";
        int hMandant = 0;
        String tablename = "";
        if (mitMandant) {
            hMandant = dbBundle.clGlobalVar.mandant;
            schema = dbBundle.getSchemaMandant();
            tablename = "tbl_portaltexte";
        } else {
            schema = dbBundle.getSchemaAllgemein();
            if (pUebergreifend) {
                tablename = "tbl_portaltextebo";
            } else {
                tablename = "tbl_portaltexte";
            }
        }

        try {

            String sql1 = "DELETE FROM " + schema + tablename + " WHERE mandant=? AND identGesamt=? AND basisSet=?;";
            PreparedStatement pstm1 = verbindung.prepareStatement(sql1);
            pstm1.setInt(1, hMandant);
            pstm1.setInt(2, pNummer);
            pstm1.setInt(3, pBasisSet);

            erg = 0; /*Falls Duplicate Key, dann wird exception geschmissen und erg wohl nicht gefüllt!*/
            erg = executeUpdate(pstm1);

            pstm1.close();
        } catch (Exception e2) {
            CaBug.drucke("DbPortalTexte.delete 001");
            System.err.println(" " + e2.getMessage());
            return (erg);
        }

        return 1;
    }

    public int erzeugeNeuesBasisSet(int pVonbasisSet, int pBisbasisSet) {
        int anzInArray = 0;

        String schema = "";
        int hMandant = 0;
        String tablename = "";
        schema = dbBundle.getSchemaAllgemein();
        tablename = "tbl_portaltexte";

        PreparedStatement lPStm = null;

        try {
            String lSql = "SELECT * from " + schema + tablename + " where " + "mandant=? AND basisSet=? ";
            lSql = lSql + "ORDER BY identGesamt, lfdNummer;";
            lPStm = verbindung.prepareStatement(lSql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            lPStm.setInt(1, hMandant);
            lPStm.setInt(2, pVonbasisSet);

            ResultSet lErgebnis = executeQuery(lPStm);
            lErgebnis.last();
            anzInArray = lErgebnis.getRow();
            lErgebnis.beforeFirst();

            ergebnisArray = new EclPortalText[anzInArray];

            int i = 0;
            maxIdentGesamt = 0;
            while (lErgebnis.next() == true) {
                ergebnisArray[i] = this.decodeErgebnis(lErgebnis);
                if (ergebnisArray[i].identGesamt > maxIdentGesamt) {
                    maxIdentGesamt = ergebnisArray[i].identGesamt;
                }
                i++;
            }
            lErgebnis.close();
            lPStm.close();

        } catch (Exception e) {
            CaBug.drucke("erzeugeNeuesbasisSet.read 003");
            System.err.println(" " + e.getMessage());
            return (-1);
        }

        for (int i = 0; i < anzInArray; i++) {
            EclPortalText lPortalText = ergebnisArray[i];
            lPortalText.basisSet = pBisbasisSet;
            this.insert(lPortalText, false, false);
        }

        return 1;
    }

}
