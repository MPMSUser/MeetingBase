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
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.naming.InitialContext;
import javax.sql.DataSource;

import de.meetingapps.meetingportal.meetComAllg.CaBug;
import de.meetingapps.meetingportal.meetComAllg.CaDatumZeit;
import de.meetingapps.meetingportal.meetComEntities.EclNummernkreis;
import de.meetingapps.meetingportal.meetComHVParam.ParamInterneKommunikation;
import de.meetingapps.meetingportal.meetComHVParam.ParamS;
import de.meetingapps.meetingportal.meetComHVParam.SParamProgramm;
import de.meetingapps.meetingportal.meetComKonst.KonstKartenklasse;

/* ******************************************************************************
 * Diese Klasse stellt die Basis-Funktionalität für die Datenbank-Verbindung
 * zur Verfügung.
 * Sie wird für jeden Dialog separat gehalten.
 *
 * Beim Erzeugen der Klasse erfolgt automatisch:
 * > open
 * > readGrunddaten
 *
 *
 * > Öffnen und Schließen der Datenbankverbindung (open, close)
 * 		Stellt eine Verbindung (Connection) zur Datenbank her, die dann von allen anderen Mcdb-Klassen innerhalb
 * 		des Dialogs genutzt werden kann
 *
 * > Beginn und Ende von Transaktionen (beginTransaction, endTransaction, endTransactionFinal)
 * 		Startet eine Transaktion, beendet eine Transaktion.
 * 		Wichtig: da ja Transaktionen nicht "ineinander verschachtelt" werden können, arbeiten - bei verschachteltem
 * 		Aufruf - diese Funktionen wie folgt:
 * 			> besteht bei beginTransaction bereits eine Transaktion, wird keine neue eröffnet
 * 			> endTransaction prüft, wieviele "verschachtelte" Transaktionen versucht wurden zu eröffnen,
 * 				und beendet die Transaktion erst wenn tatsächlich alle "verschachtelungsversuche" wieder beendet wurden
 * 			> endTransactionFinal schließt Transaktion in jedem Fall, egal wieviele Transaktionsschachtelungs-
 * 				versuche noch offen sind
 *
 * > Verwaltung von Basis-Grunddaten, die automatisch bei jedem Dialog benötigt werden. Konkret:
 * 	- Nummernkreise
 */

public class DbBasis  extends DbRootExecute {

//    /** Our Logger. */
//    private static final Logger LOGGER = LogManager.getFormatterLogger(DbBasis.class.getName());

    public static boolean istEE = false;

    private int logDrucken=3;
    
    /* **************Verbindungs-Grunddaten ******************* */
    public Connection verbindung = null;
    public Connection verbindungSperre = null;
    /** True => Fehler ist aufgetreten während der Operation */
    public boolean fehlerDatenbank = false;
    private DbBundle dbBundle = null;

    /* **************für Transaktionsverwaltung *************** */
    private int offeneTransaktionen = 0; /* Zähler für verschachtelte Transaktionen */

    private int offeneTransaktionenNeu = 0; /* Zähler für verschachtelte Transaktionen */


    public DbBasis(DbBundle pDbBundle) {
        super();
        dbBundle = pDbBundle;
        open();
    }

    /**
     * Achtung - nur zu verwenden für "kurzfristigigen Zugriff", d.h. für reinen open und close. readGrunddaten wird
     * NICHT ausgeführt.
     */
    public DbBasis() {
        super();
        dbBundle = null;
        open();
    }

    public void sendeWarnung() {

        return;

    }

    /* ****************************open / close ***************************************************** */

    private boolean internOpenEE() {
        DataSource ds;
        try {
            ds = (DataSource) InitialContext.doLookup("java:/db_meetingcomfort");
            verbindung = ds.getConnection();
            return true;
        } catch (Exception ex) {
            CaBug.drucke("DbBasis.open 001");
            System.out.println("Fehler" + ex.getMessage());
            ex.printStackTrace();
            fehlerDatenbank = true;
            return false;
        }

    }

    public void open() {
//        System.out.println("open Start");
        // Added to detect race conditions when concurrent call to DbBasis.open() from other threads
//        if (LOGGER.isDebugEnabled()) {
//            LOGGER.debug("DbBasis.open() at begin: anzahlOpen: %d", CaZaehlerOpen.anzahlOpen.getValue());
//        }
        
        int rc=CaZaehlerOpen.anzahlOpen.getValue();
        if (rc>10) {
            CaBug.druckeInfo("************************************************************************************************");
            CaBug.druckeInfo("CaZaehlerOpen="+rc);
            CaBug.druckeInfo("************************************************************************************************");
        }

        fehlerDatenbank = false;
        if (DbBasis.istEE == false) {
            try {
                // TODO Driver class package has changed in mysql connector 8 upwards
                // must be: com.mysql.cj.jdbc.Driver when changed in clients pom
                //Früher: com.mysql.jdbc.Driver
                Class.forName("com.mysql.cj.jdbc.Driver");
            } catch (Exception e) {
                System.err.println("Kann Treiber nicht laden:" + e);
                fehlerDatenbank = true;
                return;
            }
            String db = ParamInterneKommunikation.datenbankPfadZurAuswahl[ParamS.clGlobalVar.datenbankPfadNr];
            /*
             * Kann hier verwendet werden, da garantiert nicht in Wildfly
             */
            String user = SParamProgramm.db_kennung;
            String pw = SParamProgramm.db_passwort;
            try {
                verbindung = DriverManager.getConnection(db, user, pw);
            } catch (Exception e) {
                CaBug.drucke("DbBasis.open 001");
                System.err.println(" " + e.getMessage());
                fehlerDatenbank = true;
                return;
            }
        } else {
            // System.out.println("******OPEN*******");
            // DataSource ds;
            // try {
            // ds= (DataSource)InitialContext.doLookup("java:/db_meetingcomfort");
            // verbindung=ds.getConnection();
            // }
            // catch (Exception ex){
            // CaBug.drucke("DbBasis.open 001");
            // System.out.println("Fehler"+ex.getMessage());
            // fehlerDatenbank=true;
            // }
            int wartezaehler = 0;
            while (internOpenEE() == false) {
                sendeWarnung();
                wartezaehler++;
                if (wartezaehler >= 50) {
                    CaBug.drucke("DbBasis.open gezielter Abbruch");
                    // EclMeldung warteMeldung=null;
                    // warteMeldung.adelstitel="Test";
                    throw new IllegalStateException("wartezaehler >= 50");
                }
                System.out.println("Open Wait " + CaDatumZeit.DatumZeitStringFuerDatenbank());

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                System.out.println("Open Ende Wait " + CaDatumZeit.DatumZeitStringFuerDatenbank());
            }
//            if (LOGGER.isDebugEnabled()) {
//                LOGGER.debug("DbBasis.open() at end: anzahlOpen: %d", CaZaehlerOpen.anzahlOpen.increment());
//            }
        }
        // System.out.println("open Ende");
        CaZaehlerOpen.anzahlOpen.increment();
    }

    public void close() {
        try {
//            System.out.println("***** Closing *******");
            verbindung.close();
//            if (LOGGER.isDebugEnabled()) {
//                LOGGER.debug("DbBasis.close(): anzahlOpen: %d", CaZaehlerOpen.anzahlOpen.decrement());
//            }
            int rc=CaZaehlerOpen.anzahlOpen.decrement();
            if (rc!=0) {
                CaBug.druckeInfo("************************************************************************************************");
                CaBug.druckeInfo("CaZaehlerOpen="+rc);
                CaBug.druckeInfo("************************************************************************************************");
            }
        } catch (Exception e) {
            CaBug.drucke("DbBasis.close 001");
            System.err.println(" " + e.getMessage());
        }
    }

    
    /* ****************************open / close für Sperre***************************************************** */

    private boolean internOpenEESperre() {
        DataSource ds;
        try {
            ds = (DataSource) InitialContext.doLookup("java:/db_meetingcomfort");
            verbindungSperre = ds.getConnection();
            return true;
        } catch (Exception ex) {
            CaBug.drucke("DbBasis.open 001");
            System.out.println("Fehler" + ex.getMessage());
            ex.printStackTrace();
            fehlerDatenbank = true;
            return false;
        }

    }

    public void openSperre() {
//        System.out.println("open Start");
        // Added to detect race conditions when concurrent call to DbBasis.open() from other threads
//        if (LOGGER.isDebugEnabled()) {
//            LOGGER.debug("DbBasis.open() at begin: anzahlOpen: %d", CaZaehlerOpen.anzahlOpen.getValue());
//        }
        fehlerDatenbank = false;
        if (DbBasis.istEE == false) {
            try {
                // TODO Driver class package has changed in mysql connector 8 upwards
                // must be: com.mysql.cj.jdbc.Driver when changed in clients pom
                //Früher: com.mysql.jdbc.Driver
                Class.forName("com.mysql.cj.jdbc.Driver");
            } catch (Exception e) {
                System.err.println("Kann Treiber nicht laden:" + e);
                fehlerDatenbank = true;
                return;
            }
            String db = ParamInterneKommunikation.datenbankPfadZurAuswahl[ParamS.clGlobalVar.datenbankPfadNr];
            /*
             * Kann hier verwendet werden, da garantiert nicht in Wildfly
             */
            String user = SParamProgramm.db_kennung;
            String pw = SParamProgramm.db_passwort;
            try {
                verbindungSperre = DriverManager.getConnection(db, user, pw);
            } catch (Exception e) {
                CaBug.drucke("001");
                System.err.println(" " + e.getMessage());
                fehlerDatenbank = true;
                return;
            }
        } else {
            // System.out.println("******OPEN*******");
            // DataSource ds;
            // try {
            // ds= (DataSource)InitialContext.doLookup("java:/db_meetingcomfort");
            // verbindung=ds.getConnection();
            // }
            // catch (Exception ex){
            // CaBug.drucke("DbBasis.open 001");
            // System.out.println("Fehler"+ex.getMessage());
            // fehlerDatenbank=true;
            // }
            int wartezaehler = 0;
            while (internOpenEESperre() == false) {
                sendeWarnung();
                wartezaehler++;
                if (wartezaehler >= 50) {
                    CaBug.drucke("DbBasis.openSperre gezielter Abbruch");
                    // EclMeldung warteMeldung=null;
                    // warteMeldung.adelstitel="Test";
                    throw new IllegalStateException("wartezaehler >= 50");
                }
                System.out.println("Open Sperre Wait " + CaDatumZeit.DatumZeitStringFuerDatenbank());

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                System.out.println("Open Sperre Ende Wait " + CaDatumZeit.DatumZeitStringFuerDatenbank());
            }
//            if (LOGGER.isDebugEnabled()) {
//                LOGGER.debug("DbBasis.open() at end: anzahlOpen: %d", CaZaehlerOpen.anzahlOpen.increment());
//            }
        }
        CaZaehlerOpen.anzahlOpenSperre.increment();
    }

    public void closeSperre() {
        try {
//            System.out.println("***** Closing *******");
            verbindungSperre.close();
//            if (LOGGER.isDebugEnabled()) {
//                LOGGER.debug("DbBasis.close(): anzahlOpen: %d", CaZaehlerOpen.anzahlOpen.decrement());
//            }
            int rc=CaZaehlerOpen.anzahlOpenSperre.decrement();
            if (rc!=0) {
                CaBug.druckeInfo("************************************************************************************************");
                CaBug.druckeInfo("CaZaehlerOpenSperre="+rc);
                CaBug.druckeInfo("************************************************************************************************");
            }
        } catch (Exception e) {
            CaBug.drucke("001");
            System.err.println(" " + e.getMessage());
        }
    }

    public void beginTransactionSperre() {
        try {
            verbindungSperre.setAutoCommit(false);
        } catch (Exception e2) {
            CaBug.drucke("001");
            System.err.println(" " + e2.getMessage());
        }
    }

    public void endTransactionSperre() {
        try {
            verbindungSperre.commit();
            verbindungSperre.setAutoCommit(true);
        } catch (Exception e2) {
            CaBug.drucke("001");
            System.err.println(" " + e2.getMessage());
        }

    }

    
    /* *************Transaktionen********************************************* */
    public void printTransaction() {
        System.out.println("printTransaction offeneTransaktionen=" + offeneTransaktionen);
    }

    /*AAAAA Transaktion*/
    @Deprecated
    public void beginTransaction() {
        CaBug.druckeLog("offeneTransaktionen="+offeneTransaktionen, logDrucken, 10);

        // if (istEE){return;} /*noch zu implementieren wie das geht!!*/

        if (offeneTransaktionen == 0) {
            try {
//                verbindung.setAutoCommit(false);
            } catch (Exception e2) {
                CaBug.drucke("DbBasis.beginTransaction 001");
                System.err.println(" " + e2.getMessage());
            }
        }
        offeneTransaktionen++;
    }

    /*AAAAA Transaktion*/
    @Deprecated
    public void endTransaction() {
        CaBug.druckeLog("offeneTransaktionen="+offeneTransaktionen, logDrucken, 10);
        // if (istEE){return;} /*noch zu implementieren wie das geht!!*/

        offeneTransaktionen--;
        if (offeneTransaktionen == 0) {
            this.endTransactionFinal();
        }
    }

    /*AAAAA Transaktion*/
    @Deprecated
    public void zwischenCommit() {
        CaBug.druckeLog("", logDrucken, 10);
        try {
            verbindung.commit();
        } catch (Exception e2) {
            CaBug.drucke("DbBasis.zwischenCommit 001");
            System.err.println(" " + e2.getMessage());
        }

    }

    /*AAAAA Transaktion*/
    @Deprecated
    public void endTransactionFinal() {
        CaBug.druckeLog("offeneTransaktionen="+offeneTransaktionen, logDrucken, 10);
        // if (istEE){return;} /*noch zu implementieren wie das geht!!*/
        offeneTransaktionen = 0;
        try {
//            verbindung.commit();
//            verbindung.setAutoCommit(true);
        } catch (Exception e2) {
            CaBug.drucke("DbBasis.endTransactionFinal 001");
            System.err.println(" " + e2.getMessage());
        }

    }

    /**
     * Achtung - führt keine Transaktions-Beendigung aus! D.h. nach rollbackTransaction() muß noch z.B. endTransaction()
     * aufgerufen werden
     */
    /*AAAAA Transaktion*/
    @Deprecated
    public void rollbackTransaction() {
        // if (istEE){return;} /*noch zu implementieren wie das geht!!*/

        CaBug.druckeInfo("==============================Rollback===========================================");
        try {
            verbindung.rollback();
        } catch (SQLException e) {
            // Auto-generated catch block
            CaBug.drucke("DbBasis.rollbackTransaction 001");
            e.printStackTrace();
        }
    }

    public void beginTransactionNeu() {
        if (offeneTransaktionenNeu!=0) {
            CaBug.drucke("002 Verschachtelte Transaktion");
        }
        offeneTransaktionenNeu++;
        try {
            verbindung.setAutoCommit(false);
        } catch (Exception e2) {
            CaBug.drucke("001");
            System.err.println(" " + e2.getMessage());
        }
    }

    public void endTransactionNeu() {
        if (offeneTransaktionenNeu!=1) {
            CaBug.drucke("002 Verschachtelte Transaktion");
        }
        offeneTransaktionenNeu--;
        try {
            verbindung.commit();
            verbindung.setAutoCommit(true);
        } catch (Exception e2) {
            CaBug.drucke("001");
            System.err.println(" " + e2.getMessage());
        }

    }

    public void zwischenCommitNeu() {
        try {
            verbindung.commit();
        } catch (Exception e2) {
            CaBug.drucke("001");
            System.err.println(" " + e2.getMessage());
        }
    }

    /* ********************liefert zurück, ob für Ident auch Alpha zulässig ist ********** */
    private boolean getNummernkreisIsAlpha(int pKartenklasse) {
        if (dbBundle.param.paramNummernkreise.istNumerisch[pKartenklasse]) {
            return (false);
        } else {
            return (true);
        }
    }

    public boolean getNummernkreisZutrittsIdentIsAlphaGast() {
        return (getNummernkreisIsAlpha(KonstKartenklasse.gastkartennummer));
    }

    public boolean getNummernkreisZutrittsIdentIsAlphaAktionaer() {
        return (getNummernkreisIsAlpha(KonstKartenklasse.eintrittskartennummer));
    }

    public boolean getNummernkreisStimmkarteIsAlpha() {
        return (getNummernkreisIsAlpha(KonstKartenklasse.stimmkartennummer));
    }

    public boolean getNummernkreisStimmkarteSecondIsAlpha() {
        return (getNummernkreisIsAlpha(KonstKartenklasse.stimmkartennummerSecond));
    }

    /* ********************liefert länge für Ident zurück ********** */
    private int getNummernkreisLaengeIdent(int pKartenklasse) {
        return (dbBundle.param.paramNummernkreise.laengeKartennummer[pKartenklasse]);
    }

    public int getNummernkreisLaengeZutrittsIdentGast() {
        return (getNummernkreisLaengeIdent(KonstKartenklasse.gastkartennummer));
    }

    public int getNummernkreisLaengeZutrittsIdentAktionaer() {
        return (getNummernkreisLaengeIdent(KonstKartenklasse.eintrittskartennummer));
    }

    public int getNummernkreisLaengeStimmkarte() {
        return (getNummernkreisLaengeIdent(KonstKartenklasse.stimmkartennummer));
    }

    public int getNummernkreisLaengeStimmkarteSecond() {
        return (getNummernkreisLaengeIdent(KonstKartenklasse.stimmkartennummerSecond));
    }

    /* *****************Vergabe interne Ident - für Nummernkreis mit schluessel ************* */

    /*
     * 001 = Meldungen
     *
     * 011 bis 015 = Eintrittskarten Auto für jeweilige Gattung 
     * 021 bis 025 = Stimmkarten Auto für jeweilige Gattung 
     * 031 = Gästekarten Auto
     *
     * 601 = tbl_willenserklaerung 
     * 602 = tbl_personenNatJur 
     * 603 = tbl_abstimmungsVorschlag 
     * 604 = tbl_weisungMeldung 
     * 605 = tbl_aktienregistereintrag 41533
     * 606 = laufende Nr. für neu vergebene Kennungen (Teilnehmer) 
     * 607 = tbl_abstimmungen
     * 608 = tbl_userLogin 
     * 609 = tbl_gruppen 
     * 610 = tbl_publikation 
     * 611 = tbl_personenNatJurVersandadresse 
     * 612 = Druckläufe 
     * 613 = tbl_abstimmungsblock 
     * 614 = tbl_abstimmungZuAbstimmungsblock 
     * 615 = tblAbstimmungMeldungRaw 
     * 616 = tbl_nummernForm 
     * 617 = tbl_nummernFormSet 
     * 618 = tbl_meldungenProtokoll (
     * 619 = tbl_geraeteKlasse - nicht
     * verwendet!) 
     * 620 = tbl_abstimmungsmonitorek 
     * 621 = App-Texte - letzte Version (bei Mandant: mandantenspezifische
     * Texte; im übergreifenden Bereich: übergreifende Texte 
     * 622 = tbl_aufgaben (SchemaAllgemein) 
     * 623 =
     * tbl_verarbeitungsLauf (SchemaAllgemein) 
     * 624 = tbl_verarbeitungsProtokoll (SchemaAllgemein) 
     * 625 =
     * tbl_konfigauswertung 
     * 626 = tbl_instiProv 
     * 627 = tbl_instiProv - Verarbeitungslauf-Nr. 
     * 628 = Bestätitungs-Workflow
     * - eindeutige Nummer, die im Basis-Workflow verwendet wird zum Reservieren des Satzes gerade in Bearbeitung 
     * 629 = AutoTest. Spezial. Gibt nicht die höchste vergebene Nummer wieder, sondern die höchste bereits verwendete Nummer
     * wieder 
     * 630 =tbl_parameterSet (SchemaAllgemein) 
     * 631 =tbl_insti (SchemaAllgemein) 
     * 632 =tbl_suchlaufBegriffe
     * (SchemaAllgemein + Mandant) 
     * 633 =tbl_suchlaufDefinition 
     * 634 =tbl_suchlaufErgebnis 
     * 635 =tbl_instiBestandsZuordnung
     * 636 =tbl_nachricht (SchemaAllgemein + Mandant) 
     * 637 =mailIdent (SchemaAllgemein) (Verwendung bei Nachricht; aber nicht Satz
     * in Datenbank, sondern eindeutige Ident einer versendeten Nachricht. 
     * 638 =tbl_nachrichtAnhang 
     * 639
     * =tbl_abstimungsVorschlagEmpfehlung 
     * 640 =tbl_veranstaltung 
     * 641 =tbl_widerspruch
     * 642 =tbl_frage
     * 643 =tbl_vorlaeufigeVollmacht 
     * 644 =tbl_wortmeldung
     * 645 =tbl_antrag
     * 646 =tbl_sonstMitteilung
     * 647 =tbl_auftrag (SchemaAllgemein + Mandant)
     * 648 =tbl_botschaft
     * 649 = tbl_userProfile (608=Userlogin)
     * 650 = tbl_vorlaeufigeVollmachtEingabe
     */

    /***************** Übergreifende Funktionen - rein intern ********************************/
    /**
     * select vor Update - letzte interne Ident einlesen mitMandant=true => Mandantenabhängig, sonst übergreifend
     * minimum=Beginn des Nummernkreises, ab den Ident vergeben werden soll. z.B. 1="normaler Nummernkreis"
     */
    private int selectVorUpdate(int schluessel, boolean mitMandant, int minimum) {
        int mandant = 0;
        String schema = dbBundle.getSchemaAllgemein();
        if (mitMandant) {
            mandant = dbBundle.clGlobalVar.mandant;
            schema = dbBundle.getSchemaMandant();
        }

        int interneIdent = 0;

        try {

            Statement stm = verbindung.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            String sql = "SELECT letzteInterneIdent from " + schema + "tbl_nummernkreise where mandant=" + Integer.toString(mandant) + " AND schluessel=" + Integer.toString(schluessel)
                    + " FOR UPDATE";

            ResultSet ergebnis = executeQuery(stm, sql);

            if (ergebnis.next() == true) {
                interneIdent = ergebnis.getInt("letzteInterneIdent");
            }
            ergebnis.close();
            stm.close();
            if (minimum > interneIdent) {
                interneIdent = minimum - 1;
            }
            return interneIdent;

        } catch (Exception e) {
            CaBug.drucke("001");
            System.out.println("schluessel=" + schluessel + " mitMandant=" + mitMandant + " minimum=" + minimum + " schema=" + schema + " mandant=" + mandant);
            System.err.println(" " + e.getMessage());
            return -1;
        }
    }

    /** hole letzte Ident Mit Warten, falls gesperrt */
    private int selectVorUpdateMitWarten(int schluessel, boolean mitMandant, int minimum) {
        int interneIdent = 0;

        // System.out.println("in getInterneIdent");
        /* letzteInterneIdent aus tbl_nummernkreise mit schluessel, einlesen */

        int wartezaehler = 0;
        interneIdent = selectVorUpdate(schluessel, mitMandant, minimum);
        while (interneIdent == -1) {
            sendeWarnung();
            wartezaehler++;
            if (wartezaehler >= 50) {
                CaBug.drucke("DbBasis.selectVorUpdateMitWarten gezielter Abbruch");
                this.close();
                // EclMeldung warteMeldung=null;
                // warteMeldung.adelstitel="Test";
                throw new IllegalStateException("wartezaeler >= 50");
            }
            System.out.println("getInterneIdentForUpdate Schlüssel=" + schluessel + " Wait " + CaDatumZeit.DatumZeitStringFuerDatenbank());
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("getInterneIdentForUpdate Ende Wait " + CaDatumZeit.DatumZeitStringFuerDatenbank());
            interneIdent = selectVorUpdate(schluessel, mitMandant, minimum);
        }
        return interneIdent;
    }

    /***** Rein zum "vorsorglichen Sperren. Speichert den vergebenen Schlüssel NICHT zurück ****/
    private int getInterneIdentHolenOhneUpdate(int schluessel, boolean mitMandant, int minimum) {
        /* Transaktion beginnen */
        beginTransactionNeu();
        int interneIdent = selectVorUpdateMitWarten(schluessel, mitMandant, minimum);
        endTransactionNeu();
        return (interneIdent);
    }

    /** Gibt ersten neuen Wert zurück, und setzt diesen bereits als verbraucht */
    private int getInterneIdentMehrere(int schluessel, int anzahl, boolean mitMandant, int minimum) {
        int ersteNeueIdent=-1;
        try {
            int mandant = 0;
            String schema = dbBundle.getSchemaAllgemein();
            if (mitMandant) {
                mandant = dbBundle.clGlobalVar.mandant;
                schema = dbBundle.getSchemaMandant();
            }

            /* Transaktion beginnen */
            beginTransactionNeu();

            /* letzteInterneIdent aus tbl_nummernkreise mit schluessel, einlesen */
            int interneIdent = selectVorUpdateMitWarten(schluessel, mitMandant, minimum);

            /* Dann um gewünschte Anzahl erhöhen */
            ersteNeueIdent = interneIdent + 1;

            interneIdent = interneIdent + anzahl;

            /* Dann zurückspeichern */

            Statement stm = verbindung.createStatement();
            String sql = "UPDATE " + schema + "tbl_nummernkreise SET letzteInterneIdent=" + Integer.toString(interneIdent) + " where mandant=" + Integer.toString(mandant) + " AND schluessel="
                    + Integer.toString(schluessel);
            int ergebnis1 = executeUpdate(stm, sql);
            if (ergebnis1 < 1) { /* Satz nicht vorhanden - Insert */
                CaBug.druckeInfo("DbBasis.resetInterneIdent 004 - Schlüssel " + schluessel + " neu eingefügt");
                sql = "INSERT INTO " + schema + "tbl_nummernkreise (schluessel, mandant, letzteInterneIdent) " + "VALUES (?, ?, ?)";
                PreparedStatement stm1 = verbindung.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                stm1.setInt(1, schluessel);
                stm1.setInt(2, mandant);
                stm1.setInt(3, interneIdent);
                ergebnis1 = executeUpdate(stm1);
            }

            stm.close();
            /* Transaktion beenden */
            endTransactionNeu();
        } catch (Exception e1) {
            CaBug.drucke("002");
            endTransactionNeu();
            System.err.println(" " + e1.getMessage());
            System.out.println(" " + e1.getMessage());

        }

        return (ersteNeueIdent);
    }

    private int resetInterneIdent(int schluessel, boolean mitMandant, int wert) {
        System.out.println("resetInterneIdent schluessel=" + schluessel + " mitMandant=" + mitMandant + " wert=" + wert);
        int mandant = 0;
        String schema = dbBundle.getSchemaAllgemein();
        if (mitMandant) {
            mandant = dbBundle.clGlobalVar.mandant;
            schema = dbBundle.getSchemaMandant();
        }

        int ergebnis1 = -1;
        try {

            String sql = "UPDATE " + schema + "tbl_nummernkreise SET letzteInterneIdent=?" + " where mandant=" + Integer.toString(mandant) + " AND schluessel=" + Integer.toString(schluessel);
            PreparedStatement stm = verbindung.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            stm.setInt(1, wert);
            ergebnis1 = executeUpdate(stm);
            if (ergebnis1 < 1) { /* Satz nicht vorhanden - Insert */
                CaBug.druckeInfo("DbBasis.resetInterneIdent 004 - Schlüssel " + schluessel + " neu eingefügt");
                sql = "INSERT INTO " + schema + "tbl_nummernkreise (schluessel, mandant, letzteInterneIdent) " + "VALUES (?, ?, ?)";
                PreparedStatement stm1 = verbindung.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                stm1.setInt(1, schluessel);
                stm1.setInt(2, mandant);
                stm1.setInt(3, wert);
                ergebnis1 = executeUpdate(stm1);
            }
            stm.close();
        } catch (Exception e1) {
            CaBug.drucke("DbBasis.resetInterneIdent 002");
            System.err.println(" " + e1.getMessage());
            System.out.println(" " + e1.getMessage());
            ergebnis1 = -1;
        }

        return (ergebnis1);
    }

    /***************** Mit Mandantennummer ******************************/
    private int getInterneIdent(int schluessel) {
        return getInterneIdentMehrere(schluessel, 1, true, 1);
    }

    /* Gibt ersten neuen Wert zurück */
    private int getInterneIdentMehrere(int schluessel, int anzahl) {
        return getInterneIdentMehrere(schluessel, anzahl, true, 1);
    }

    private int resetInterneIdent(int schluessel) {
        return resetInterneIdent(schluessel, true, 0);
    }

    private int resetInterneIdent(int schluessel, int wert) {
        return resetInterneIdent(schluessel, true, wert);
    }

//    /***** Rein zum "vorsorglilchen Sperren ****/
//    private int getInterneIdentForUpdate(int schluessel) {
//        return getInterneIdentHolenOhneUpdate(schluessel, true, 1);
//    }


    /*****************
     * ohne Mandantennummer (d.h. Ident werden Mandantenübergreifend vergeben
     ******************************/
    private int getInterneIdentOhneMandant(int schluessel) {
        return getInterneIdentMehrere(schluessel, 1, false, 1);
    }

    private int resetInterneIdentOhneMandant(int schluessel) {
        return resetInterneIdentOhneMandant(schluessel, 0);
    }

    private int resetInterneIdentOhneMandant(int schluessel, int wert) {
        return resetInterneIdent(schluessel, false, wert);
    }

    /* *************************Vergabe interne Ident für bestimmten Typ************************************* */
    public int getInterneIdentMeldungen() {
        return (getInterneIdent(1));
    }

    public int resetInterneIdentMeldungen() {
        return (resetInterneIdent(1));
    }

    public int resetInterneIdentMeldungen(int pWert) {
        return (resetInterneIdent(1, pWert));
    }

    /** pGattung=1 bis 5 */
    public int getEintrittskartennummer(int pGattung) {
        int startwert = dbBundle.param.paramNummernkreise.vonSubEintrittskartennummer[pGattung][2];
        return (getInterneIdentMehrere(10 + pGattung, 1, true, startwert));
    }

    public int getEintrittskartennummerPruefe(int pGattung) {
        int startwert = dbBundle.param.paramNummernkreise.vonSubEintrittskartennummer[pGattung][2];
        return (getInterneIdentHolenOhneUpdate(10 + pGattung, true, startwert) + 1);
    }

    public int resetEintrittskartennummer(int pGattung) {
        return (resetInterneIdent(10 + pGattung));
    }

    public int resetEintrittskartennummer(int pGattung, int pWert) {
        return (resetInterneIdent(10 + pGattung, pWert));
    }

    /** pGattung=1 bis 5 */
    public int getStimmkartennummer(int pGattung) {
        int startwert = dbBundle.param.paramNummernkreise.vonSubStimmkartennummer[pGattung][5];
        return (getInterneIdentMehrere(20 + pGattung, 1, true, startwert));
    }

    public int getStimmkartennummerPruefe(int pGattung) {
        int startwert = dbBundle.param.paramNummernkreise.vonSubStimmkartennummer[pGattung][5];
        return (getInterneIdentHolenOhneUpdate(20 + pGattung, true, startwert) + 1);
    }

    public int resetStimmkartennummer(int pGattung) {
        return (resetInterneIdent(20 + pGattung));
    }

    public int resetStimmkartennummer(int pGattung, int pWert) {
        return (resetInterneIdent(20 + pGattung, pWert));
    }

    public int getGastkartennummer() {
        int startwert = dbBundle.param.paramNummernkreise.vonKartennummerAuto[KonstKartenklasse.gastkartennummer];
        return (getInterneIdentMehrere(21, 1, true, startwert));
    }

    public int getGastkartennummerPruefe() {
        int startwert = dbBundle.param.paramNummernkreise.vonKartennummerAuto[KonstKartenklasse.gastkartennummer];
        return (getInterneIdentHolenOhneUpdate(21, true, startwert) + 1);
    }

    public int resetGastkartennummer() {
        return (resetInterneIdent(21));
    }

    public int resetGastkartennummer(int pWert) {
        return (resetInterneIdent(21, pWert));
    }

    public int getInterneIdentWillenserklaerung() {
        int rc;
        rc = getInterneIdent(601);
        return rc;
    }

    public int resetInterneIdentWillenserklaerung() {
        int rc;
        rc = resetInterneIdent(601);
        return rc;
    }

    public int resetInterneIdentWillenserklaerung(int pWert) {
        int rc;
        rc = resetInterneIdent(601, pWert);
        return rc;
    }

//    /** Rein zum vorsorglichen Sperren */
//    @Deprecated
//    /*AAAAA Transaktion ErsterSchritt*/
//    public int ggetInterneIdentWillenserklaerungForUpdate() {
//        int rc;
//        rc = getInterneIdentForUpdate(601);
//        return rc;
//    }

    public int getInterneIdentPersonenNatJur() {
        return (getInterneIdent(602));
    }

    public int resetInterneIdentPersonenNatJur() {
        return (resetInterneIdent(602));
    }

    public int resetInterneIdentPersonenNatJur(int pWert) {
        return (resetInterneIdent(602, pWert));
    }

    public int getInterneIdentAbstimmungsVorschlag() {
        return (getInterneIdent(603));
    }

    public int resetInterneIdentAbstimmungsVorschlag() {
        return (resetInterneIdent(603));
    }

    public int resetInterneIdentAbstimmungsVorschlag(int pWert) {
        return (resetInterneIdent(603, pWert));
    }

    public int getInterneIdentWeisungMeldung() {
        return (getInterneIdent(604));
    }

    public int resetInterneIdentWeisungMeldung() {
        return (resetInterneIdent(604));
    }

    public int resetInterneIdentWeisungMeldung(int pWert) {
        return (resetInterneIdent(604, pWert));
    }

    public int getInterneIdentAktienregisterEintrag() {
        return (getInterneIdent(605));
    }

    public int resetInterneIdentAktienregisterEintrag() {
        return (resetInterneIdent(605));
    }

    public int resetInterneIdentAktienregisterEintrag(int pWert) {
        return (resetInterneIdent(605, pWert));
    }

    /** Werden für mehrere Vergaben verwendet! */
    private int aktuellerZaehlerReservierte = 0;
    private int aktuellerZaehlerReservierteMax = 0;

    public int getInterneIdentAktienregisterEintragReserviereMehrere(int pAnzahl) {
        aktuellerZaehlerReservierte = getInterneIdentMehrere(605, pAnzahl) - 1;
        aktuellerZaehlerReservierteMax = aktuellerZaehlerReservierte + pAnzahl;
        return 1;
    }

    public int getInterneIdentAktienregisterEintragNext() {
        aktuellerZaehlerReservierte++;
        if (aktuellerZaehlerReservierte > aktuellerZaehlerReservierteMax) {
            CaBug.drucke("DbBasis.getInterneIdentAktienregisterEintragNext 001");
            return -1;
        }
        return aktuellerZaehlerReservierte;
    }

    public int getInterneIdentKennung() {
        return (getInterneIdent(606));
    }

    public int resetInterneIdentKennung() {
        return (resetInterneIdent(606));
    }

    public int getInterneIdentAbstimmung() {
        return (getInterneIdent(607));
    }

    public int resetInterneIdentAbstimmung() {
        return (resetInterneIdent(607));
    }

    public int resetInterneIdentAbstimmung(int pWert) {
        return (resetInterneIdent(607, pWert));
    }

    public int getInterneIdentUserLoginOhneMandant() {
        return (getInterneIdentOhneMandant(608));
    }

    public int resetInterneIdentUserLoginOhneMandant() {
        return (resetInterneIdentOhneMandant(608));
    }

    public int resetInterneIdentUserLoginOhneMandant(int pWert) {
        return (resetInterneIdentOhneMandant(608, pWert));
    }

    public int getInterneIdentUserProfileOhneMandant() {
        return (getInterneIdentOhneMandant(649));
    }

    public int resetInterneIdentUserProfileOhneMandant() {
        return (resetInterneIdentOhneMandant(649));
    }

    public int resetInterneIdentUserProfileOhneMandant(int pWert) {
        return (resetInterneIdentOhneMandant(649, pWert));
    }

    public int getInterneIdentGruppen() {
        return (getInterneIdent(609));
    }

    public int resetInterneIdentGruppen() {
        return (resetInterneIdent(609));
    }

    public int resetInterneIdentGruppen(int pWert) {
        return (resetInterneIdent(609, pWert));
    }

    public int getInterneIdentPublikation() {
        return (getInterneIdent(610));
    }

    public int resetInterneIdentPublikation() {
        return (resetInterneIdent(610));
    }

    public int resetInterneIdentPublikation(int pWert) {
        return (resetInterneIdent(610, pWert));
    }

    public int getInterneIdentPersonenNatJurVersandadresse() {
        return (getInterneIdent(611));
    }

    public int resetInterneIdentPersonenNatJurVersandadresse() {
        return (resetInterneIdent(611));
    }

    public int resetInterneIdentPersonenNatJurVersandadresse(int pWert) {
        return (resetInterneIdent(611, pWert));
    }

    public int getInterneIdentDrucklauf() {
        return (getInterneIdent(612));
    }

    public int resetInterneIdentDrucklauf() {
        return (resetInterneIdent(612));
    }

    public int getInterneIdentDrucklaufAllgemein() {
        return (getInterneIdentOhneMandant(612));
    }

    public int resetInterneIdentDrucklaufAllgemein() {
        return (resetInterneIdentOhneMandant(612));
    }


    public int getInterneIdentAbstimmungsblock() {
        return (getInterneIdent(613));
    }

    public int resetInterneIdentAbstimmungsblock() {
        return (resetInterneIdent(613));
    }

    public int resetInterneIdentAbstimmungsblock(int pWert) {
        return (resetInterneIdent(613, pWert));
    }

    public int getInterneIdentAbstimmungZuAbstimmungsblock() {
        return (getInterneIdent(614));
    }

    public int resetInterneIdentAbstimmungZuAbstimmungsblock() {
        return (resetInterneIdent(614));
    }

    public int resetInterneIdentAbstimmungZuAbstimmungsblock(int pWert) {
        return (resetInterneIdent(614, pWert));
    }

    public int getInterneIdentAbstimmungMeldungRaw() {
        return (getInterneIdent(615));
    }

    public int resetInterneIdentAbstimmungMeldungRaw() {
        return (resetInterneIdent(615));
    }

    public int resetInterneIdentAbstimmungMeldungRaw(int pWert) {
        return (resetInterneIdent(615, pWert));
    }

    public int getInterneIdentNummernForm() {
        return (getInterneIdentOhneMandant(616));
    }

    public int resetInterneIdentNummernForm() {
        return (resetInterneIdentOhneMandant(616));
    }

    public int resetInterneIdentNummernForm(int pWert) {
        return (resetInterneIdentOhneMandant(616, pWert));
    }

    public int getInterneIdentNummernFormSet() {
        return (getInterneIdentOhneMandant(617));
    }

    public int resetInterneIdentNummernFormSet() {
        return (resetInterneIdentOhneMandant(617));
    }

    public int resetInterneIdentNummernFormSet(int pWert) {
        return (resetInterneIdentOhneMandant(617, pWert));
    }

    public int getInterneIdentMeldungenProtokoll() {
        return (getInterneIdent(618));
    }

    public int resetInterneIdentMeldungenProtokoll() {
        return (resetInterneIdent(618));
    }

    public int resetInterneIdentMeldungenProtokoll(int pWert) {
        return (resetInterneIdent(618, pWert));
    }

    public int getInterneIdentAbstimmungsmonitorEK() {
        return (getInterneIdent(620));
    }

    public int resetInterneIdentAbstimmungsmonitorEK() {
        return (resetInterneIdent(620));
    }

    public int resetInterneIdentAbstimmungsmonitorEK(int pWert) {
        return (resetInterneIdent(620, pWert));
    }

    public int getAppVersionMandant() {
        return (getInterneIdent(621));
    }

    public int resetAppVersionMandant() {
        return (resetInterneIdent(621));
    }

    public int rresetAppVersionMandant(int pWert) {
        return (resetInterneIdent(621, pWert));
    }

    public int getAppVersionUebergreifend() {
        return (getInterneIdentOhneMandant(621));
    }

    public int resetAppVersionUebergreifend() {
        return (resetInterneIdentOhneMandant(621));
    }

    public int resetAppVersionUebergreifend(int pWert) {
        return (resetInterneIdentOhneMandant(621, pWert));
    }

    public int getInterneIdentAufgabenOhneMandant() {
        return (getInterneIdentOhneMandant(622));
    }

    public int resetInterneIdentAufgabenOhneMandant() {
        return (resetInterneIdentOhneMandant(622));
    }

    public int resetInterneIdentAufgabenOhneMandant(int pWert) {
        return (resetInterneIdentOhneMandant(622, pWert));
    }

    public int getInterneIdentVerarbeitungsLaufOhneMandant() {
        return (getInterneIdentOhneMandant(623));
    }

    public int resetInterneIdentVerarbeitungsLaufOhneMandant() {
        return (resetInterneIdentOhneMandant(623));
    }

    public int resetInterneIdentVerarbeitungsLaufOhneMandant(int pWert) {
        return (resetInterneIdentOhneMandant(623, pWert));
    }

    public int getInterneIdentVerarbeitungsProtokollOhneMandant() {
        return (getInterneIdentOhneMandant(624));
    }

    public int resetInterneIdentVerarbeitungsProtokollOhneMandant() {
        return (resetInterneIdentOhneMandant(624));
    }

    public int resetInterneIdentVerarbeitungsProtokollOhneMandant(int pWert) {
        return (resetInterneIdentOhneMandant(624, pWert));
    }

    public int getInterneIdentKonfigAuswertung() {
        return (getInterneIdent(625));
    }

    public int resetInterneIdentKonfigAuswertung() {
        return (resetInterneIdent(625));
    }

    public int resetInterneIdentKonfigAuswertung(int pWert) {
        return (resetInterneIdent(625, pWert));
    }

    public int getInterneIdentInstiProv() {
        return (getInterneIdent(626));
    }

    public int resetInterneIdentInstiProv() {
        return (resetInterneIdent(626));
    }

    public int resetInterneIdentInstiProv(int pWert) {
        return (resetInterneIdent(626, pWert));
    }

    public int getInterneIdentInstiProvLauf() {
        return (getInterneIdent(627));
    }

    public int resetInterneIdentInstiProvLauf() {
        return (resetInterneIdent(627));
    }

    public int resetInterneIdentInstiProvLauf(int pWert) {
        return (resetInterneIdent(627, pWert));
    }

    public int getInterneIdentBestWorkflowBasis() {
        return (getInterneIdent(628));
    }

    public int resetInterneIdentBestWorkflowBasis() {
        return (resetInterneIdent(628));
    }

    public int resetInterneIdentBestWorkflowBasis(int pWert) {
        return (resetInterneIdent(628, pWert));
    }

    public int getInterneIdentAutoTest() {
        return (getInterneIdent(629));
    }

    public int resetInterneIdentAutoTest() {
        return (resetInterneIdent(629));
    }

    public int resetInterneIdentAutoTest(int pWert) {
        return (resetInterneIdent(629, pWert));
    }

    public int getInterneIdentParameterSet() {
        return (getInterneIdentOhneMandant(630));
    }

    public int resetInterneIdentParameterSet() {
        return (resetInterneIdentOhneMandant(630));
    }

    public int resetInterneIdentParameterSet(int pWert) {
        return (resetInterneIdentOhneMandant(630, pWert));
    }

    public int getInterneIdentInsti() {
        return (getInterneIdentOhneMandant(631));
    }

    public int resetInterneIdentInsti() {
        return (resetInterneIdentOhneMandant(631));
    }

    public int resetInterneIdentInsti(int pWert) {
        return (resetInterneIdentOhneMandant(631, pWert));
    }

    public int getInterneIdentSuchlaufBegriffeOhneMandant() {
        return (getInterneIdentOhneMandant(632));
    }

    public int resetInterneIdentSuchlaufBegriffeOhneMandant() {
        return (resetInterneIdentOhneMandant(632));
    }

    public int resetInterneIdentSuchlaufBegriffeOhneMandant(int pWert) {
        return (resetInterneIdentOhneMandant(632, pWert));
    }

    public int getInterneIdentSuchlaufBegriffeMitMandant() {
        return (getInterneIdent(632));
    }

    public int resetInterneIdentSuchlaufBegriffeMitMandant() {
        return (resetInterneIdent(632));
    }

    public int resetInterneIdentSuchlaufBegriffeMitMandant(int pWert) {
        return (resetInterneIdent(632, pWert));
    }

    public int getInterneIdentSuchlaufDefinition() {
        return (getInterneIdent(633));
    }

    public int resetInterneIdentSuchlaufDefinition() {
        return (resetInterneIdent(633));
    }

    public int resetInterneIdentSuchlaufDefinition(int pWert) {
        return (resetInterneIdent(633, pWert));
    }

    public int getInterneIdentSuchlaufErgebnis() {
        return (getInterneIdent(634));
    }

    public int resetInterneIdentSuchlaufErgebnis() {
        return (resetInterneIdent(634));
    }

    public int resetInterneIdentSuchlaufErgebnis(int pWert) {
        return (resetInterneIdent(634, pWert));
    }

    public int getInterneIdentSuchlaufErgebnisReserviereMehrere(int pAnzahl) {
        aktuellerZaehlerReservierte = getInterneIdentMehrere(634, pAnzahl) - 1;
        aktuellerZaehlerReservierteMax = aktuellerZaehlerReservierte + pAnzahl;
        return 1;
    }

    public int getInterneIdentSuchlaufErgebnisNext() {
        aktuellerZaehlerReservierte++;
        if (aktuellerZaehlerReservierte > aktuellerZaehlerReservierteMax) {
            CaBug.drucke("DbBasis.getInterneIdentSuchlaufErgebnisNext 001");
            return -1;
        }
        return aktuellerZaehlerReservierte;
    }

    public int getInterneIdentInstiBestandsZuordnung() {
        return (getInterneIdent(635));
    }

    public int resetInterneIdentInstiBestandsZuordnung() {
        return (resetInterneIdent(635));
    }

    public int resetInterneIdentInstiBestandsZuordnung(int pWert) {
        return (resetInterneIdent(635, pWert));
    }

    public int getInterneIdentNachricht() {
        return (getInterneIdent(636));
    }

    public int resetInterneIdentNachricht() {
        return (resetInterneIdent(636));
    }

    public int resetInterneIdentNachricht(int pWert) {
        return (resetInterneIdent(636, pWert));
    }

    public int getInterneIdentNachrichtIdentMail() {
        return (getInterneIdent(637));
    }

    public int resetInterneIdentNachrichtIdentMail() {
        return (resetInterneIdent(637));
    }

    public int resetInterneIdentNachrichtIdentMail(int pWert) {
        return (resetInterneIdent(637, pWert));
    }

    public int getInterneIdentNachrichtOhneMandant() {
        return (getInterneIdentOhneMandant(636));
    }

    public int resetInterneIdentNachrichtOhneMandant() {
        return (resetInterneIdentOhneMandant(636));
    }

    public int resetInterneIdentNachrichtOhneMandant(int pWert) {
        return (resetInterneIdentOhneMandant(636, pWert));
    }

    public int getInterneIdentNachrichtIdentMailOhneMandant() {
        return (getInterneIdentOhneMandant(637));
    }

    public int resetInterneIdentNachrichtIdentMailOhneMandant() {
        return (resetInterneIdentOhneMandant(637));
    }

    public int resetInterneIdentNachrichtIdentMailOhneMandant(int pWert) {
        return (resetInterneIdentOhneMandant(637, pWert));
    }

    public int getInterneIdentNachrichtAnhang() {
        return (getInterneIdent(638));
    }

    public int resetInterneIdentNachrichtAnhang() {
        return (resetInterneIdent(638));
    }

    public int resetInterneIdentNachrichtAnhang(int pWert) {
        return (resetInterneIdent(638, pWert));
    }

    public int getInterneIdentNachrichtAnhangOhneMandant() {
        return (getInterneIdentOhneMandant(638));
    }

    public int resetInterneIdentNachrichtAnhangOhneMandant() {
        return (resetInterneIdentOhneMandant(638));
    }

    public int resetInterneIdentNachrichtAnhangOhneMandant(int pWert) {
        return (resetInterneIdentOhneMandant(638, pWert));
    }

    public int getInterneIdentAbstimungsVorschlagEmpfehlung() {
        return (getInterneIdent(639));
    }

    public int resetInterneIdentAbstimungsVorschlagEmpfehlung() {
        return (resetInterneIdent(639));
    }

    public int resetInterneIdentAbstimungsVorschlagEmpfehlung(int pWert) {
        return (resetInterneIdent(639, pWert));
    }

    public int getInterneIdentVeranstaltung() {
        return (getInterneIdent(640));
    }

    public int resetInterneIdentVeranstaltung() {
        return (resetInterneIdent(640));
    }

    public int resetInterneIdentVeranstaltung(int pWert) {
        return (resetInterneIdent(640, pWert));
    }

    public int getInterneIdentWiderspruch() {
        return (getInterneIdent(641));
    }

    public int resetInterneIdentWiderspruch() {
        return (resetInterneIdent(641));
    }

    public int resetInterneIdentWiderspruch(int pWert) {
        return (resetInterneIdent(641, pWert));
    }

    public int getInterneIdentFrage() {
        return (getInterneIdent(642));
    }

    public int resetInterneIdentFrage() {
        return (resetInterneIdent(642));
    }

    public int resetInterneIdentFrage(int pWert) {
        return (resetInterneIdent(642, pWert));
    }

    public int getInterneIdentVorlaeufigeVollmacht() {
        return (getInterneIdent(643));
    }

    public int resetInterneIdentVorlaeufigeVollmacht() {
        return (resetInterneIdent(643));
    }

    public int resetInterneIdentVorlaeufigeVollmacht(int pWert) {
        return (resetInterneIdent(643, pWert));
    }

    public int getInterneIdentWortmeldungen() {
        return (getInterneIdent(644));
    }

    public int resetInterneIdentWortmeldungen() {
        return (resetInterneIdent(644));
    }

    public int resetInterneIdentWortmeldungen(int pWert) {
        return (resetInterneIdent(644, pWert));
    }

    public int getInterneIdentAntraege() {
        return (getInterneIdent(645));
    }

    public int resetInterneIdentAntraege() {
        return (resetInterneIdent(645));
    }

    public int resetInterneIdentAntraege(int pWert) {
        return (resetInterneIdent(645, pWert));
    }

    public int getInterneIdentSonstMitteilungen() {
        return (getInterneIdent(646));
    }

    public int resetInterneIdentSonstMitteilungen() {
        return (resetInterneIdent(646));
    }

    public int resetInterneIdentSonstMitteilungen(int pWert) {
        return (resetInterneIdent(646, pWert));
    }

    public int getInterneIdentAuftrag() {
        return (getInterneIdent(647));
    }

    public int resetInterneIdentAuftrag() {
        return (resetInterneIdent(647));
    }

    public int resetInterneIdentAuftrag(int pWert) {
        return (resetInterneIdent(647, pWert));
    }

    public int getInterneIdentAuftragOhneMandant() {
        return (getInterneIdentOhneMandant(647));
    }

    public int resetInterneIdentAuftragOhneMandant() {
        return (resetInterneIdentOhneMandant(647));
    }

    public int resetInterneIdentAuftragOhneMandant(int pWert) {
        return (resetInterneIdentOhneMandant(647, pWert));
    }

    public int getInterneIdentBotschaften() {
        return (getInterneIdent(648));
    }

    public int resetInterneIdentBotschaften() {
        return (resetInterneIdent(648));
    }

    public int resetInterneIdentBotschaften(int pWert) {
        return (resetInterneIdent(648, pWert));
    }

    public int getInterneIdentVorlaeufigeVollmachtEingabe() {
        return (getInterneIdent(650));
    }

    public int resetInterneIdentVorlaeufigeVollmachtEingabe() {
        return (resetInterneIdent(650));
    }

    public int resetInterneIdentVorlaeufigeVollmachtEingabe(int pWert) {
        return (resetInterneIdent(650, pWert));
    }




    /*****************************
     * Ab hier "neue" Funktionen, bereits nach "neuem" Verfahren, für Zugrif auf tbl_nummernkreise
     ***********/

    /************************** deleteAll Löschen aller Datensätze eines Mandanten ****************/
    public int deleteAll() {
        int erg = 0;

        try {

            String sql1 = "DELETE FROM " + dbBundle.getSchemaMandant() + "tbl_nummernkreise where mandant=?;";
            PreparedStatement pstm1 = verbindung.prepareStatement(sql1);
            pstm1.setInt(1, dbBundle.clGlobalVar.mandant);

            erg = 0; /* Falls Duplicate Key, dann wird exception geschmissen und erg wohl nicht gefüllt! */
            erg = executeUpdate(pstm1);
            pstm1.close();

        } catch (Exception e2) {
            CaBug.drucke("DbBasis.deleteAll 001");
            System.err.println(" " + e2.getMessage());
            return (erg);
        }

        return 1;
    }

    /************************** setzt aktuelle Mandantennummer bei allen Datensätzen ****************/
    /* Siehe tbl_nummernkreise! */

    /*********************
     * Fuellen Prepared Statement mit allen Feldern, beginnend bei offset.***************** Kann sowohl für Insert, als
     * auch für update verwendet werden.
     *
     * offset= Startposition des ersten Feldes (also z.B. 1)
     */
    private int anzfelder = 8; /* Anpassen auf Anzahl der Felder pro Datensatz */

    private void fuellePreparedStatementKomplett(PreparedStatement pPStm, int pOffset, EclNummernkreis pNummernkreis) {

        int startOffset = pOffset; /* Nur erforderlich zum Überprüfen von Programmierfehlern - Feldanzahl */

        try {
            pPStm.setInt(pOffset, pNummernkreis.mandant);
            pOffset++;
            pPStm.setInt(pOffset, pNummernkreis.schluessel);
            pOffset++;
            pPStm.setInt(pOffset, pNummernkreis.letzteInterneIdent);
            pOffset++;
            pPStm.setInt(pOffset, pNummernkreis.alphaZutrittsIdent);
            pOffset++;
            pPStm.setInt(pOffset, pNummernkreis.laengeZutrittsIdent);
            pOffset++;
            pPStm.setInt(pOffset, pNummernkreis.minZutrittsIdent);
            pOffset++;
            pPStm.setInt(pOffset, pNummernkreis.maxZutrittsIdent);
            pOffset++;
            pPStm.setInt(pOffset, pNummernkreis.letzteZutrittsIdent);
            pOffset++;
            if (pOffset - startOffset != anzfelder) {
                /* Nur wg. Überprüfung auf Programmierfehler - alle Felder berücksichtigt? */
                CaBug.drucke("DbBasis.fuellePreparedStatementKomplett 002");
            }

        } catch (SQLException e) {
            CaBug.drucke("DbBasis.fuellePreparedStatementKomplett 001");
            e.printStackTrace();
        }

    }

    /**
     * Insert
     *
     * Feld mandant wird von dieser Funktion immer selbstständig belegt.
     *
     * Returnwert: =1 => Insert erfolgreich ansonsten: Fehler
     */
    public int insertNummernkreis(EclNummernkreis pNummernkreis) {

        int erg = 0;

        pNummernkreis.mandant = dbBundle.clGlobalVar.mandant;

        /*
         * Satz einfügen: Verarbeitungshinweis: > nachdem InterneIdent immer eindeutig vergeben werden, ist prinzipiell
         * eine "Doppeleinfügung" von InterneIdent nicht möglich. Sollte es dazu kommen, ist das immer ein
         * Programmfehler!
         */

        try {

            /* Felder Neuanlage füllen */
            String lSql = "INSERT INTO " + dbBundle.getSchemaMandant() + "tbl_nummernkreise " + "(" + "mandant, schluessel, " + "letzteInterneIdent, alphaZutrittsIdent, laengeZutrittsIdent, "
                    + "minZutrittsIdent, maxZutrittsIdent, letzteZutrittsIdent" + ")" + "VALUES (" + "?, ?, " + "?, ?, ?, " + "?, ?, ? " + ")";
            PreparedStatement lPStm = verbindung.prepareStatement(lSql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);

            fuellePreparedStatementKomplett(lPStm, 1, pNummernkreis);

            erg = executeUpdate(lPStm);
            lPStm.close();
        } catch (Exception e2) {
            CaBug.drucke("DbBasis.insertNummernkreis 001");
            System.err.println(" " + e2.getMessage());
        }

        if (erg == 0) {/* Fehler beim Einfügen - d.h. primaryKey bereits vorhanden */
            return (-1);
        }

        /* Ende Transaktion */
        return (1);
    }

    public static void setIstEE(boolean pWert) {
        istEE = pWert;
    }

}
