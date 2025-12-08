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
import de.meetingapps.meetingportal.meetComBVerwaltung.BvReload;
import de.meetingapps.meetingportal.meetComEntities.EclEmittenten;

/**Hinweis zum Speichern in Tables:
 * tbl_emittenten gibt es mandatenabhängig und global. Felder sind identisch.
 * 
 * mandantenabhängig wird angelegt, damit auch in DaSi alle Emittendendaten enthalten sind, und bei einer
 * Rücksicherung leicht restauriert werden können. Sonst keine Verwendung.
 * 
 * Abspeicherung / Lesen / Versorgung der mandantenabhänigen Table erfolgt im Normalfall transparent.
 * 
 * TODO Lediglich im Dienstprogramm erfolgt das Lesen der mandantenabhängigen Table und das Einfügen / Updaten in
 * das globale Table 
 */
public class DbEmittenten  extends DbRootExecute {

    private Connection verbindung = null;
    private DbBundle dbBundle = null;

    private final boolean KONST_TABLE_MANDANTENABNHAENGIG=true;
    private final boolean KONST_TABLE_GLOBAL=false;
    
    public EclEmittenten ergebnisArray[] = null;

    private String liefereSchema(boolean pMandantenabhaenig) {
        if (pMandantenabhaenig) {
            return dbBundle.getSchemaMandant();
        }
        else {
            return dbBundle.getSchemaAllgemein();
        }
    }
    
    
    /*************************Initialisierung***************************/
    public DbEmittenten(DbBundle pDbBundle) {
        /* Verbindung in lokale Daten eintragen*/
        if (pDbBundle == null) {
            CaBug.drucke("DbEmittenten.init 001 - dbBundle nicht initialisiert");
            return;
        }
        if (pDbBundle.dbBasis == null) {
            CaBug.drucke("DbEmittenten.init 002 - dbBasis nicht initialisiert");
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
    public EclEmittenten ergebnisPosition(int pN) {
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

    /**************************deleteAll Löschen aller Datensätze  eines Mandanten****************/
    public int deleteAll() {
        return dbBundle.dbLowLevel
                .deleteMandant("DELETE FROM " + dbBundle.getSchemaAllgemein() + "tbl_emittenten where mandant=?;");
    }

    /*******Checken, ob table überhaupt vorhanden ist***************************/
    public boolean checkTableVorhanden() {
        DbLowLevel lDbLowLevel = new DbLowLevel(dbBundle);
        return lDbLowLevel.checkTableVorhanden("SELECT * from tbl_emittenten WHERE mandant=0; ");
    }

    
    private int createTable(boolean pMandantenabhaenig) {
        int rc = 0;
        DbLowLevel lDbLowLevel = new DbLowLevel(dbBundle);
        rc = lDbLowLevel.createTable("CREATE TABLE " + this.liefereSchema(pMandantenabhaenig) + "tbl_emittenten ( "
                + "`mandant` int(11) NOT NULL, " + "`hvJahr` int(11) NOT NULL DEFAULT '2018', "
                + "`hvNummer` char(1) NOT NULL DEFAULT 'A', " + "`dbArt` char(1) NOT NULL DEFAULT 'P', "
                + "`hvCode` char(10) DEFAULT NULL, " + "`inDBVorhanden` int(11) DEFAULT '1', "
                + "`db_version` bigint(20) DEFAULT NULL, " + "`bezeichnungKurz` varchar(80) DEFAULT NULL, "
                + "`bezeichnungLang` varchar(200) DEFAULT NULL, " + "`bezeichnungsArtApp` int(11) DEFAULT '1', "
                + "`bezeichnungsArtPortal` int(11) DEFAULT '1', " + "`bezeichnungsArtFormulare` int(11) DEFAULT '1', "
                + "`hvDatum` char(10) DEFAULT NULL, " + "`hvOrt` varchar(50) DEFAULT NULL, "
                + "`portalVorhanden` int(11) DEFAULT NULL, " + "`portalAktuellAktiv` int(11) DEFAULT NULL, "
                + "`portalStandard` int(11) DEFAULT NULL, " 
                + "`portalIstDauerhaft` int(11) DEFAULT NULL, " 
                + "`datenbestandIstProduktiv` int(11) DEFAULT NULL, " 
                + "`portalSprache` int(11) DEFAULT '3', "
                + "`verwendeStandardXHTML` int(11) DEFAULT '0', " + "`appVorhanden` int(11) DEFAULT NULL, "
                + "`appAktiv` int(11) DEFAULT NULL, " + "`appSprache` int(11) DEFAULT '1', "
                + "`emittentenPortalVorhanden` int(11) DEFAULT '1', " + "`emittentenPortalAktiv` int(11) DEFAULT '1', "
                + "`emittentenPortalSprache` int(11) DEFAULT '1', "
                + "`registerAnbindungVorhanden` int(11) DEFAULT '0', "
                + "`registerAnbindungAktiv` int(11) DEFAULT '0', " + "`registerAnbindungSprache` int(11) DEFAULT '0', "
                + "`dbGesperrt` int(11) DEFAULT '0', " + "`inAuswahl` int(11) DEFAULT '3', "
                + "`pfadErgKundenOrdner` varchar(80) DEFAULT NULL, " + "`serverNummer` int(11) DEFAULT '2', " 
                
                + "`abteilung` varchar(100) DEFAULT NULL, "
                + "`strasseGesellschaft` varchar(100) DEFAULT NULL, "
                + "`postleitzahl` varchar(10) DEFAULT NULL, "
                + "`ort` varchar(100) DEFAULT NULL, "
                + "`staatId` int(11) DEFAULT '0', " 
                + "`telefon` varchar(100) DEFAULT NULL, "
                + "`fax` varchar(100) DEFAULT NULL, "
                + "`email` varchar(100) DEFAULT NULL, "

                + "`hvArtSchluessel` int(11) DEFAULT '0', " 
                + "`veranstaltungStrasse` varchar(100) DEFAULT NULL, "
                + "`veranstaltungPostleitzahl` varchar(10) DEFAULT NULL, "
                + "`veranstaltungStaatId` int(11) DEFAULT '0', " 
                + "`veranstaltungGebäude` varchar(100) DEFAULT NULL, "
                + "`kuerzelInhaberImportExport` varchar(15) DEFAULT NULL, "

                + "PRIMARY KEY (`mandant`,`hvJahr`,`hvNummer`,`dbArt`) " + ")  ");
        return rc;
        
    }
    
    /************Neuanlegen Table - global******************************/
    public int createTable() {
        return createTable(KONST_TABLE_GLOBAL);
    }
    public int createTableMandantenabhaengig() {
        return createTable(KONST_TABLE_MANDANTENABNHAENGIG);
    }

    /********** dekodiert die aktuelle Position aus ergebnis in EclAktienregisterEintrag und gibt dieses zurück*****/
    EclEmittenten decodeErgebnis(ResultSet pErgebnis) {

        EclEmittenten lEmittenten = new EclEmittenten();

        try {
            lEmittenten.mandant = pErgebnis.getInt("em.mandant");
            lEmittenten.mandantExtern=lEmittenten.mandant;
            lEmittenten.hvJahr = pErgebnis.getInt("em.hvJahr");
            lEmittenten.hvNummer = pErgebnis.getString("em.hvNummer");
            lEmittenten.dbArt = pErgebnis.getString("em.dbArt");
            lEmittenten.hvCode = pErgebnis.getString("em.hvCode");
            if (lEmittenten.hvCode == null) {
                lEmittenten.hvCode = "";
            }
            lEmittenten.inDbVorhanden = pErgebnis.getInt("em.inDbVorhanden");
            lEmittenten.db_version = pErgebnis.getLong("em.db_version");
            lEmittenten.bezeichnungKurz = pErgebnis.getString("em.bezeichnungKurz");
            lEmittenten.bezeichnungLang = pErgebnis.getString("em.bezeichnungLang");
            lEmittenten.bezeichnungsArtApp = pErgebnis.getInt("em.bezeichnungsArtApp");
            lEmittenten.bezeichnungsArtPortal = pErgebnis.getInt("em.bezeichnungsArtPortal");
            lEmittenten.bezeichnungsArtFormulare = pErgebnis.getInt("em.bezeichnungsArtFormulare");
            lEmittenten.hvDatum = pErgebnis.getString("em.hvDatum");
            lEmittenten.hvOrt = pErgebnis.getString("em.hvOrt");

            lEmittenten.portalVorhanden = pErgebnis.getInt("em.portalVorhanden");
            lEmittenten.portalAktuellAktiv = pErgebnis.getInt("em.portalAktuellAktiv");
            lEmittenten.portalStandard = pErgebnis.getInt("em.portalStandard");
            lEmittenten.portalIstDauerhaft = pErgebnis.getInt("em.portalIstDauerhaft");
            lEmittenten.datenbestandIstProduktiv = pErgebnis.getInt("em.datenbestandIstProduktiv");
            lEmittenten.portalSprache = pErgebnis.getInt("em.portalSprache");
//            lEmittenten.verwendeStandardXHTML = pErgebnis.getInt("em.verwendeStandardXHTML");  In Table noch vorhanden

            lEmittenten.appVorhanden = pErgebnis.getInt("em.appVorhanden");
            lEmittenten.appAktiv = pErgebnis.getInt("em.appAktiv");
            lEmittenten.appSprache = pErgebnis.getInt("em.appSprache");

            lEmittenten.emittentenPortalVorhanden = pErgebnis.getInt("em.emittentenPortalVorhanden");
            lEmittenten.emittentenPortalAktiv = pErgebnis.getInt("em.emittentenPortalAktiv");
            lEmittenten.emittentenPortalSprache = pErgebnis.getInt("em.emittentenPortalSprache");

            lEmittenten.registerAnbindungVorhanden = pErgebnis.getInt("em.registerAnbindungVorhanden");
            lEmittenten.registerAnbindungAktiv = pErgebnis.getInt("em.registerAnbindungAktiv");
            lEmittenten.registerAnbindungSprache = pErgebnis.getInt("em.registerAnbindungSprache");

            lEmittenten.dbGesperrt = pErgebnis.getInt("em.dbGesperrt");
            lEmittenten.inAuswahl = pErgebnis.getInt("em.inAuswahl");
            lEmittenten.pfadErgKundenOrdner = pErgebnis.getString("em.pfadErgKundenOrdner");
            if (lEmittenten.pfadErgKundenOrdner == null) {
                lEmittenten.pfadErgKundenOrdner = "";
            }
            lEmittenten.serverNummer = pErgebnis.getInt("em.serverNummer");

            lEmittenten.abteilung = pErgebnis.getString("em.abteilung");
            lEmittenten.strasseGesellschaft = pErgebnis.getString("em.strasseGesellschaft");
            lEmittenten.postleitzahl = pErgebnis.getString("em.postleitzahl");
            lEmittenten.ort = pErgebnis.getString("em.ort");
            lEmittenten.staatId = pErgebnis.getInt("em.staatId");
            lEmittenten.telefon = pErgebnis.getString("em.telefon");
            lEmittenten.fax = pErgebnis.getString("em.fax");
            lEmittenten.email = pErgebnis.getString("em.email");

            lEmittenten.hvArtSchluessel = pErgebnis.getInt("em.hvArtSchluessel");
            lEmittenten.veranstaltungStrasse = pErgebnis.getString("em.veranstaltungStrasse");
            lEmittenten.veranstaltungPostleitzahl = pErgebnis.getString("em.veranstaltungPostleitzahl");
            lEmittenten.veranstaltungStaatId = pErgebnis.getInt("em.veranstaltungStaatId");
            lEmittenten.veranstaltungGebäude = pErgebnis.getString("em.veranstaltungGebäude");
            lEmittenten.kuerzelInhaberImportExport = pErgebnis.getString("em.kuerzelInhaberImportExport");

        } catch (Exception e) {
            CaBug.drucke("001");
            System.err.println(" " + e.getMessage());
        }

        return lEmittenten;
    }

    /********************* Fuellen Prepared Statement mit allen Feldern, beginnend bei offset.*****************
     * Kann sowohl für Insert, als auch für update verwendet werden.
     * 
     * offset= Startposition des ersten Feldes (also z.B. 1)
     */
    private int anzfelder = 48; 

    void fuellePreparedStatementKomplett(PreparedStatement pPStm, int pOffset, EclEmittenten pEmittenten) {

        int startOffset = pOffset; /*Nur erforderlich zum Überprüfen von Programmierfehlern - Feldanzahl*/
        
        try {
            pPStm.setInt(pOffset, pEmittenten.mandant);
            pOffset++;
            pPStm.setInt(pOffset, pEmittenten.hvJahr);
            pOffset++;
            pPStm.setString(pOffset, pEmittenten.hvNummer);
            pOffset++;
            pPStm.setString(pOffset, pEmittenten.dbArt);
            pOffset++;
            pPStm.setString(pOffset, pEmittenten.hvCode);
            pOffset++;
            pPStm.setInt(pOffset, pEmittenten.inDbVorhanden);
            pOffset++;
            pPStm.setLong(pOffset, pEmittenten.db_version);
            pOffset++;

            pPStm.setString(pOffset, pEmittenten.bezeichnungKurz);
            pOffset++;
            pPStm.setString(pOffset, pEmittenten.bezeichnungLang);
            pOffset++;
            pPStm.setInt(pOffset, pEmittenten.bezeichnungsArtApp);
            pOffset++;
            pPStm.setInt(pOffset, pEmittenten.bezeichnungsArtPortal);
            pOffset++;
            pPStm.setInt(pOffset, pEmittenten.bezeichnungsArtFormulare);
            pOffset++;
            pPStm.setString(pOffset, pEmittenten.hvDatum);
            pOffset++;
            pPStm.setString(pOffset, pEmittenten.hvOrt);
            pOffset++;

            pPStm.setInt(pOffset, pEmittenten.portalVorhanden);
            pOffset++;
            pPStm.setInt(pOffset, pEmittenten.portalAktuellAktiv);
            pOffset++;
            pPStm.setInt(pOffset, pEmittenten.portalStandard);
            pOffset++;
            pPStm.setInt(pOffset, pEmittenten.portalIstDauerhaft);
            pOffset++;
            pPStm.setInt(pOffset, pEmittenten.datenbestandIstProduktiv);
            pOffset++;
            pPStm.setInt(pOffset, pEmittenten.portalSprache);
            pOffset++;
            
            pPStm.setInt(pOffset, 0 /*pEmittenten.verwendeStandardXHTML*/); /*in Table noch vorhanden*/
            pOffset++;

            pPStm.setInt(pOffset, pEmittenten.appVorhanden);
            pOffset++;
            pPStm.setInt(pOffset, pEmittenten.appAktiv);
            pOffset++;
            pPStm.setInt(pOffset, pEmittenten.appSprache);
            pOffset++;

            pPStm.setInt(pOffset, pEmittenten.emittentenPortalVorhanden);
            pOffset++;
            pPStm.setInt(pOffset, pEmittenten.emittentenPortalAktiv);
            pOffset++;
            pPStm.setInt(pOffset, pEmittenten.emittentenPortalSprache);
            pOffset++;

            pPStm.setInt(pOffset, pEmittenten.registerAnbindungVorhanden);
            pOffset++;
            pPStm.setInt(pOffset, pEmittenten.registerAnbindungAktiv);
            pOffset++;
            pPStm.setInt(pOffset, pEmittenten.registerAnbindungSprache);
            pOffset++;

            pPStm.setInt(pOffset, pEmittenten.dbGesperrt);
            pOffset++;
            pPStm.setInt(pOffset, pEmittenten.inAuswahl);
            pOffset++;

            pPStm.setString(pOffset, pEmittenten.pfadErgKundenOrdner);
            pOffset++;

            pPStm.setInt(pOffset, pEmittenten.serverNummer);
            pOffset++;

            pPStm.setString(pOffset, pEmittenten.abteilung);
            pOffset++;
            pPStm.setString(pOffset, pEmittenten.strasseGesellschaft);
            pOffset++;
            pPStm.setString(pOffset, pEmittenten.postleitzahl);
            pOffset++;
            pPStm.setString(pOffset, pEmittenten.ort);
            pOffset++;
            pPStm.setInt(pOffset, pEmittenten.staatId);
            pOffset++;
            pPStm.setString(pOffset, pEmittenten.telefon);
            pOffset++;
            pPStm.setString(pOffset, pEmittenten.fax);
            pOffset++;
            pPStm.setString(pOffset, pEmittenten.email);
            pOffset++;

            pPStm.setInt(pOffset, pEmittenten.hvArtSchluessel);
            pOffset++;
            pPStm.setString(pOffset, pEmittenten.veranstaltungStrasse);
            pOffset++;
            pPStm.setString(pOffset, pEmittenten.veranstaltungPostleitzahl);
            pOffset++;
            pPStm.setInt(pOffset, pEmittenten.veranstaltungStaatId);
            pOffset++;
            pPStm.setString(pOffset, pEmittenten.veranstaltungGebäude);
            pOffset++;
            pPStm.setString(pOffset, pEmittenten.kuerzelInhaberImportExport);
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
    
    private int insert(EclEmittenten pEmittenten, boolean pMandantenabhaengig) {
        int erg = 0;

        try {
            /*Felder Neuanlage füllen*/

            //      @formatter:off

            String lSql = "INSERT INTO "
                    + liefereSchema(pMandantenabhaengig)
                    + "tbl_emittenten " + "("
                    + "mandant, hvJahr, hvNummer, dbArt, hvCode, inDbVorhanden, db_version, "
                    + "bezeichnungKurz, bezeichnungLang, bezeichnungsArtApp, bezeichnungsArtPortal, bezeichnungsArtFormulare, "
                    + "hvDatum, hvOrt, "
                    + "portalVorhanden, portalAktuellAktiv, portalStandard, portalIstDauerhaft, datenbestandIstProduktiv, portalSprache, verwendeStandardXHTML, appVorhanden, appAktiv, appSprache, "
                    + "emittentenPortalVorhanden, emittentenPortalAktiv, emittentenPortalSprache, registerAnbindungVorhanden, registerAnbindungAktiv, registerAnbindungSprache, "
                    + "dbGesperrt, inAuswahl, pfadErgKundenOrdner, serverNummer, "
                    + "abteilung, strasseGesellschaft, postleitzahl, ort, staatId, telefon, fax, email, "
                    + "hvArtSchluessel, veranstaltungStrasse, veranstaltungPostleitzahl, veranstaltungStaatId, veranstaltungGebäude, kuerzelInhaberImportExport "
                    +" ) VALUES ("
                    + "?, ?, ?, ?, ?, ?, ?, " 
                    + "?, ?, ?, ?, ?, " 
                    + "?, ?, " 
                    + "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, "
                    + "?, ?, ?, ?, ?, ?, " 
                    + "?, ?, ?, ?, "
                    + "?, ?, ?, ?, ?, ?, ?, ?, "
                    + "?, ?, ?, ?, ?, ? "
                    + " )";
            //                  @formatter:on


                    
            PreparedStatement lPStm = verbindung.prepareStatement(lSql, ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
            fuellePreparedStatementKomplett(lPStm, 1, pEmittenten);
            erg = executeUpdate(lPStm);
            lPStm.close();
        } catch (Exception e2) {
            CaBug.drucke("001");
            System.err.println(" " + e2.getMessage());
        }

        if (erg == 0) {/*Fehler beim Einfügen - d.h. z.B. primaryKey bereits vorhanden*/
            return (-1);
        }
        
        return erg;
    }
    /**Insert
     * 
     * Feld mandant wird von dieser Funktion nicht selbstständig belegt.
     * 
     * Returnwert:
     * =1 => Insert erfolgreich
     * ansonsten: Fehler
     * 
     * es wird immer in globalem und mandantenabhängigem Table eingefügt.
     */
    public int insert(EclEmittenten pEmittenten) {

        /*Spezial-Funktion: im Reload-Bereich markieren, dass sich Emittentenstamm geändert hat, d.h. Alle Systeme
         * müssen beim nächsten "Open" den Emittentenstamm neu einlesen*/
        BvReload bvReload = new BvReload(dbBundle);
        bvReload.setReloadEmittenten();

        /*Globales Table füllen*/
         int erg=insert(pEmittenten, KONST_TABLE_GLOBAL);
         if (erg==-1) {return erg;}
        
        /*Mandantenabhängiges Table füllen*/
         erg=insert(pEmittenten, KONST_TABLE_MANDANTENABNHAENGIG);
        if (erg==-1) {return erg;}

        return (1);
    }

    public int insertNurMandantenabhaengig(EclEmittenten pEmittenten) {
        /*Mandantenabhängiges Table füllen*/
        int erg=insert(pEmittenten, KONST_TABLE_MANDANTENABNHAENGIG);
        if (erg==-1) {return erg;}
        return (1);
    }
    
    /**Einlesen aller Mandanten
     * pSortiert=1 => nach bezeichnungKurz, pSortiert=0 nur nach Mandantennummer etc.. 
     * anschließend immer nach Mandantennummer, hvJahr, hvNummer, dbArt*/
    public int readAll(int pSortiert) {
        int anzInArray = 0;
        PreparedStatement lPStm = null;

        try {
            String lSql = "SELECT * from " + dbBundle.getSchemaAllgemein() + "tbl_emittenten em ORDER BY ";
            if (pSortiert == 1) {
                lSql = lSql + "bezeichnungKurz, ";
            }
            lSql = lSql + "mandant, hvJahr, hvNummer, dbArt;";
            lPStm = verbindung.prepareStatement(lSql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);

            ResultSet lErgebnis = executeQuery(lPStm);
            lErgebnis.last();
            anzInArray = lErgebnis.getRow();
            lErgebnis.beforeFirst();

            ergebnisArray = new EclEmittenten[anzInArray];

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

    /**Einlesen aller Mandanten
     * pSortiert=1 => nach bezeichnungKurz, pSortiert=0 nur nach Mandantennummer etc.. 
     * anschließend immer nach Mandantennummer, hvJahr, hvNummer, dbArt*/
    public int readAllZuInsti(int pSortiert, int pInsti) {
        int anzInArray = 0;
        PreparedStatement lPStm = null;

        try {
            String lSql = "SELECT * from " + dbBundle.getSchemaAllgemein() + "tbl_emittenten em " + "JOIN "
                    + dbBundle.getSchemaAllgemein()
                    + "tbl_instiEmittentenMitZuordnung inemzu on (em.mandant=inemzu.mandant AND em.hvJahr=inemzu.hvjahr AND em.hvNummer=inemzu.hvNummer AND em.dbArt=inemzu.dbArt) "
                    + "WHERE inemzu.identInsti=? " + "ORDER BY ";
            if (pSortiert == 1) {
                lSql = lSql + "em.bezeichnungKurz, ";
            }
            lSql = lSql + "em.mandant, em.hvJahr, em.hvNummer, em.dbArt;";
            lPStm = verbindung.prepareStatement(lSql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            lPStm.setInt(1, pInsti);

            ResultSet lErgebnis = executeQuery(lPStm);
            lErgebnis.last();
            anzInArray = lErgebnis.getRow();
            lErgebnis.beforeFirst();

            ergebnisArray = new EclEmittenten[anzInArray];

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

    /**Einlesen der letzten gültigen Mandantenbezeichnung für Mandant pMandant*/
    public int readMandantenbezeichnung(int pMandant) {
        int anzInArray = 0;
        PreparedStatement lPStm = null;

        try {
            String lSql = "SELECT * from " + dbBundle.getSchemaAllgemein()
                    + "tbl_emittenten em WHERE mandant=? ORDER BY ";
            lSql = lSql + "mandant, hvJahr, hvNummer, dbArt ;";
            lPStm = verbindung.prepareStatement(lSql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            lPStm.setInt(1, pMandant);

            ResultSet lErgebnis = executeQuery(lPStm);
            lErgebnis.last();
            anzInArray = lErgebnis.getRow();
            lErgebnis.beforeFirst();

            if (anzInArray > 0) {
                ergebnisArray = new EclEmittenten[1];

                /*So lange wiederholen, bis letzter Satz gefunden - immer wieder
                 * in ergebnisArray überschreiben - d.h. es bleibt nur der letzte Satz übrig.
                 * Ginge auch Effizienter :-(*/
                while (lErgebnis.next() == true) {
                    ergebnisArray[0] = this.decodeErgebnis(lErgebnis);
                }

                anzInArray = 1;
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

    /**Einlesen des Standard-Emittenten zu Mandant*/
    public int readStandardHV(int pMandant) {
        int anzInArray = 0;
        PreparedStatement lPStm = null;

        try {
            String lSql = "SELECT * from " + dbBundle.getSchemaAllgemein()
                    + "tbl_emittenten em WHERE mandant=? AND portalStandard=1;";
            lPStm = verbindung.prepareStatement(lSql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            lPStm.setInt(1, pMandant);

            ResultSet lErgebnis = executeQuery(lPStm);
            lErgebnis.last();
            anzInArray = lErgebnis.getRow();
            lErgebnis.beforeFirst();

            ergebnisArray = new EclEmittenten[anzInArray];

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

    /**Einlesen der Emittentendaten für eine bestimmte HV aus Table*/
    public int readEmittentHV(int pMandant, int pHVJahr, String pHVNummer, String pDatenbereich) {
        int anzInArray = 0;
        PreparedStatement lPStm = null;

        try {
            String lSql = "SELECT * from " + dbBundle.getSchemaAllgemein()
                    + "tbl_emittenten em WHERE mandant=? AND hvJahr=? AND hvNummer=? AND dbArt=? ORDER BY ";
            lSql = lSql + "mandant, hvJahr, hvNummer, dbArt ;";
            lPStm = verbindung.prepareStatement(lSql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            lPStm.setInt(1, pMandant);
            lPStm.setInt(2, pHVJahr);
            lPStm.setString(3, pHVNummer);
            lPStm.setString(4, pDatenbereich);

            ResultSet lErgebnis = executeQuery(lPStm);
            lErgebnis.last();
            anzInArray = lErgebnis.getRow();
            lErgebnis.beforeFirst();

            ergebnisArray = new EclEmittenten[anzInArray];

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

    /**Einlesen aller Sätze zu einem Mandanten pMandant. 
     * 
     * Für "neue Datenbank-Versionen": liefert alle HVen zu einem Mandanten.
     * 
     * "Für alte Datenbank-Version": liefert DEN Emittentensatz zu dem Mandanten (da es jeden Mandanten nur einmal gab)
     * (für Datenbank-Versions-Update sinnvoll / benötigt!)*/
    public int readNurMandant(int pMandant) {
        int anzInArray = 0;
        PreparedStatement lPStm = null;

        try {
            String lSql = "SELECT * from " + dbBundle.getSchemaAllgemein() + "tbl_emittenten em where " + "mandant=?;";
            lPStm = verbindung.prepareStatement(lSql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            lPStm.setInt(1, pMandant);

            ResultSet lErgebnis = executeQuery(lPStm);
            lErgebnis.last();
            anzInArray = lErgebnis.getRow();
            lErgebnis.beforeFirst();

            ergebnisArray = new EclEmittenten[anzInArray];

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

    /**Einlesen des Satzes pMandant.*/
    public int read_appAktiv() {
        int anzInArray = 0;
        PreparedStatement lPStm = null;

        try {
            String lSql = "SELECT * from " + dbBundle.getSchemaAllgemein() + "tbl_emittenten em where "
                    + "appAktiv=1 AND portalStandard=1;";
            lPStm = verbindung.prepareStatement(lSql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);

            ResultSet lErgebnis = executeQuery(lPStm);
            lErgebnis.last();
            anzInArray = lErgebnis.getRow();
            lErgebnis.beforeFirst();

            ergebnisArray = new EclEmittenten[anzInArray];

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

    private int update(EclEmittenten pEmittenten, boolean pMandantenabhaengig) {

         try {
            String lSql = "UPDATE " + this.liefereSchema(pMandantenabhaengig) + "tbl_emittenten SET "
                    + "mandant=?, hvJahr=?, hvNummer=?, dbArt=?, hvCode=?, inDbVorhanden=?, db_version=?, "
                    + "bezeichnungKurz=?, bezeichnungLang=?, bezeichnungsArtApp=?, bezeichnungsArtPortal=?, bezeichnungsArtFormulare=?, "
                    + "hvDatum=?, hvOrt=?, "
                    + "portalVorhanden=?, portalAktuellAktiv=?, portalStandard=?, portalIstDauerhaft=?, datenbestandIstProduktiv=?, portalSprache=?, verwendeStandardXHTML=?, appVorhanden=?, appAktiv=?, appSprache=?, "
                    + "emittentenPortalVorhanden=?, emittentenPortalAktiv=?, emittentenPortalSprache=?, registerAnbindungVorhanden=?, registerAnbindungAktiv=?, registerAnbindungSprache=?, "
                    + "dbGesperrt=?, inAuswahl=?, pfadErgKundenOrdner=?, serverNummer=?, "
                    + "abteilung=?, strasseGesellschaft=?, postleitzahl=?, ort=?, staatId=?, telefon=?, fax=?, email=?, "
                    + "hvArtSchluessel=?, veranstaltungStrasse=?, veranstaltungPostleitzahl=?, veranstaltungStaatId=?, veranstaltungGebäude=?, kuerzelInhaberImportExport=?" 
                    + "WHERE "
                    + "mandant=? AND hvJahr=? AND hvNummer=? AND dbArt=? ";
            if (pMandantenabhaengig==KONST_TABLE_GLOBAL) {
                lSql=lSql+"AND db_version=? ";
            }

            PreparedStatement lPStm = verbindung.prepareStatement(lSql);
            fuellePreparedStatementKomplett(lPStm, 1, pEmittenten);
            lPStm.setInt(anzfelder + 1, pEmittenten.mandant);
            lPStm.setInt(anzfelder + 2, pEmittenten.hvJahr);
            lPStm.setString(anzfelder + 3, pEmittenten.hvNummer);
            lPStm.setString(anzfelder + 4, pEmittenten.dbArt);
            if (pMandantenabhaengig==KONST_TABLE_GLOBAL) {
                lPStm.setLong(anzfelder + 5, pEmittenten.db_version - 1);
            }

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
    /**Update. Versionsnummer wird um 1 hochgezählt
     * 
     * Feld mandant wird von dieser Funktion nicht selbstständig belegt.
     * 
     * Zum Sicherstellen der Multiuserfähigkeit muß in jedem Fall der rc CaFehler.pfXyWurdeVonAnderemBenutzerVeraendert
     * nach Aufruf dieser Funktion abgefangen werden.
     * Ansonsten: rc=1 => ok, ansonsten Fehler
     */
    public int update(EclEmittenten pEmittenten) {

        pEmittenten.db_version++;
        
        /*Spezial-Funktion: im Reload-Bereich markieren, dass sich Emittentenstamm geändert hat, d.h. Alle Systeme
         * müssen beim nächsten "Open" den Emittentenstamm neu einlesen*/
        BvReload bvReload = new BvReload(dbBundle);
        bvReload.setReloadEmittenten();

        int ergebnis=update(pEmittenten, KONST_TABLE_GLOBAL);
        if (ergebnis<1) {return ergebnis;}
        
        ergebnis=update(pEmittenten, KONST_TABLE_MANDANTENABNHAENGIG);
        if (ergebnis<1) {
            ergebnis=insertNurMandantenabhaengig(pEmittenten);
            if (ergebnis<1) {
                return ergebnis;
            }
        }

        return (1);
    }

    public int updateMandant() {
        return dbBundle.dbLowLevel.rawUpdateMandant(dbBundle.getSchemaMandant() + "tbl_emittenten");
    }

    /**Funktion so noch nicht brauchbar - da Index mehr Felder als pMandant enthält. Nur als "Blaupause" vorhanden*/
    @Deprecated
    public int delete(int pMandant) {

        try {

            String sql = "DELETE FROM " + dbBundle.getSchemaAllgemein() + "tbl_emittenten WHERE mandant=? ";

            PreparedStatement pstm1 = verbindung.prepareStatement(sql);
            pstm1.setInt(1, pMandant);

            int ergebnis1 = executeUpdate(pstm1);
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

}
