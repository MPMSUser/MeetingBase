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
import de.meetingapps.meetingportal.meetComAllg.CaString;
import de.meetingapps.meetingportal.meetComEntities.EclAktienregisterZusatz;

@Deprecated
public class DbAktienregisterZusatz {

    private Connection verbindung = null;
    private DbBasis dbBasis = null;
    private DbBundle dbBundle = null;

    private EclAktienregisterZusatz ergebnisArray[] = null;

    /*************************Initialisierung***************************/
    public DbAktienregisterZusatz(DbBundle pDbBundle) {
        /* Verbindung in lokale Daten eintragen*/
        if (pDbBundle == null) {
            CaBug.drucke("DbAktienregisterZusatz.init 001 - dbBundle nicht initialisiert");
            return;
        }
        if (pDbBundle.dbBasis == null) {
            CaBug.drucke("DbAktienregisterZusatz.init 002 - dbBasis nicht initialisiert");
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

    /**********Liefert pN-tes Element des Ergebnisses der read*Methoden**************
     * pN geht von 0 bis anzErgebnis-1*/
    public EclAktienregisterZusatz ergebnisPosition(int pN) {
        if (ergebnisArray == null) {
            CaBug.drucke("DbAktienregisterZusatz.ergebnisPosition 001");
            return null;
        }
        if (pN < 0) {
            CaBug.drucke("DbAktienregisterZusatz.ergebnisPosition 002");
            return null;
        }
        if (pN >= ergebnisArray.length) {
            CaBug.drucke("DbAktienregisterZusatz.ergebnisPosition 003");
            return null;
        }
        return ergebnisArray[pN];
    }

    public int createTable() {
        int rc = 0;
        DbLowLevel lDbLowLevel = new DbLowLevel(dbBundle);
        rc = lDbLowLevel.createTable("CREATE TABLE " + dbBundle.getSchemaMandant() + "tbl_aktienregisterzusatz ( "
                + "`mandant` int(11) NOT NULL, " + "`aktienregisterIdent` int(11) NOT NULL, "
                + "`db_version` bigint(20) DEFAULT NULL, "
                + "`hinweisAktionaersPortalBestaetigt` int(11) DEFAULT NULL, "
                + "`hinweisHVPortalBestaetigt` int(11) DEFAULT NULL, " + "`eMailRegistrierung` int(11) DEFAULT NULL, "
                + "`eMailRegistrierungErstZeitpunkt` char(19) DEFAULT NULL, "
                + "`eigenesPasswort` int(11) DEFAULT NULL, " + "`passwortVergessenLink` char(30) DEFAULT NULL, "
                + "`passwortVergessenZeitpunkt` char(19) DEFAULT NULL, "
                + "`kommunikationssprache` int(11) DEFAULT NULL, " + "`eMailFuerVersand` char(80) DEFAULT NULL, "
                + "`emailBestaetigt` int(11) DEFAULT NULL, " + "`emailBestaetigenLink` char(30) DEFAULT NULL, "
                + "`eMail2FuerVersand` char(40) DEFAULT NULL, " + "`email2Bestaetigt` int(11) DEFAULT NULL, "
                + "`email2BestaetigenLink` char(30) DEFAULT NULL, "
                + "`publikationenZustellung0` int(11) DEFAULT NULL, "
                + "`publikationenZustellung1` int(11) DEFAULT NULL, "
                + "`publikationenZustellung2` int(11) DEFAULT NULL, "
                + "`publikationenZustellung3` int(11) DEFAULT NULL, "
                + "`publikationenZustellung4` int(11) DEFAULT NULL, "
                + "`publikationenZustellung5` int(11) DEFAULT NULL, "
                + "`publikationenZustellung6` int(11) DEFAULT NULL, "
                + "`publikationenZustellung7` int(11) DEFAULT NULL, "
                + "`publikationenZustellung8` int(11) DEFAULT NULL, "
                + "`publikationenZustellung9` int(11) DEFAULT NULL, " + "`kontaktTelefonPrivat` char(40) DEFAULT NULL, "
                + "`kontaktTelefonGeschaeftlich` char(40) DEFAULT NULL, "
                + "`kontaktTelefonMobil` char(40) DEFAULT NULL, " + "`kontaktTelefonFax` char(40) DEFAULT NULL, "
                + "PRIMARY KEY (`aktienregisterIdent`,`mandant`), "
                + "KEY `IDX_passwortVergessenLink` (`passwortVergessenLink`) " + ") ");
        return rc;
    }

    /**************************deleteAll Löschen aller Datensätze  eines Mandanten****************/
    public int deleteAll() {
        int erg = 0;

        try {

            String sql1 = "DELETE FROM " + dbBundle.getSchemaMandant() + "tbl_aktienregisterZusatz where mandant=?;";
            PreparedStatement pstm1 = verbindung.prepareStatement(sql1);
            pstm1.setInt(1, dbBundle.clGlobalVar.mandant);

            erg = 0; /*Falls Duplicate Key, dann wird exception geschmissen und erg wohl nicht gefüllt!*/
            erg = pstm1.executeUpdate();

            pstm1.close();
        } catch (Exception e2) {
            CaBug.drucke("DbAktienregisterZusatz.deleteAll 001");
            System.err.println(" " + e2.getMessage());
            return (erg);
        }

        return 1;
    }

    /**************************setzt aktuelle Mandantennummer bei allen Datensätzen****************/
    public int updateMandant() {
        return dbBundle.dbLowLevel.rawUpdateMandant(dbBundle.getSchemaMandant() + "tbl_aktienregisterZusatz");
    }

    /********** dekodiert die aktuelle Position aus ergebnis in EclAktienregisterEintrag und gibt dieses zurück******/
    EclAktienregisterZusatz decodeErgebnis(ResultSet pErgebnis) {

        EclAktienregisterZusatz lAktienregisterZusatz = new EclAktienregisterZusatz();

        try {

            lAktienregisterZusatz.mandant = pErgebnis.getInt("arez.mandant");
            lAktienregisterZusatz.aktienregisterIdent = pErgebnis.getInt("arez.aktienregisterIdent");
            lAktienregisterZusatz.db_version = pErgebnis.getLong("arez.db_version");

            lAktienregisterZusatz.hinweisAktionaersPortalBestaetigt = pErgebnis
                    .getInt("arez.hinweisAktionaersPortalBestaetigt");
            lAktienregisterZusatz.hinweisHVPortalBestaetigt = pErgebnis.getInt("arez.hinweisHVPortalBestaetigt");
            lAktienregisterZusatz.eMailRegistrierung = pErgebnis.getInt("arez.eMailRegistrierung");
            lAktienregisterZusatz.eMailRegistrierungErstZeitpunkt = pErgebnis
                    .getString("arez.eMailRegistrierungErstZeitpunkt");
            lAktienregisterZusatz.eigenesPasswort = pErgebnis.getInt("arez.eigenesPasswort");

            lAktienregisterZusatz.passwortVergessenLink = pErgebnis.getString("arez.passwortVergessenLink");
            lAktienregisterZusatz.passwortVergessenZeitpunkt = pErgebnis.getString("arez.passwortVergessenZeitpunkt");
            lAktienregisterZusatz.kommunikationssprache = pErgebnis.getInt("arez.kommunikationssprache");

            lAktienregisterZusatz.eMailFuerVersand = pErgebnis.getString("arez.eMailFuerVersand");
            lAktienregisterZusatz.emailBestaetigt = pErgebnis.getInt("arez.emailBestaetigt");
            lAktienregisterZusatz.emailBestaetigenLink = pErgebnis.getString("arez.emailBestaetigenLink");

            lAktienregisterZusatz.eMail2FuerVersand = pErgebnis.getString("arez.eMail2FuerVersand");
            lAktienregisterZusatz.email2Bestaetigt = pErgebnis.getInt("arez.email2Bestaetigt");
            lAktienregisterZusatz.email2BestaetigenLink = pErgebnis.getString("arez.email2BestaetigenLink");

            lAktienregisterZusatz.publikationenZustellung[0] = pErgebnis.getInt("arez.publikationenZustellung0");
            lAktienregisterZusatz.publikationenZustellung[1] = pErgebnis.getInt("arez.publikationenZustellung1");
            lAktienregisterZusatz.publikationenZustellung[2] = pErgebnis.getInt("arez.publikationenZustellung2");
            lAktienregisterZusatz.publikationenZustellung[3] = pErgebnis.getInt("arez.publikationenZustellung3");
            lAktienregisterZusatz.publikationenZustellung[4] = pErgebnis.getInt("arez.publikationenZustellung4");
            lAktienregisterZusatz.publikationenZustellung[5] = pErgebnis.getInt("arez.publikationenZustellung5");
            lAktienregisterZusatz.publikationenZustellung[6] = pErgebnis.getInt("arez.publikationenZustellung6");
            lAktienregisterZusatz.publikationenZustellung[7] = pErgebnis.getInt("arez.publikationenZustellung7");
            lAktienregisterZusatz.publikationenZustellung[8] = pErgebnis.getInt("arez.publikationenZustellung8");
            lAktienregisterZusatz.publikationenZustellung[9] = pErgebnis.getInt("arez.publikationenZustellung9");

            lAktienregisterZusatz.kontaktTelefonPrivat = pErgebnis.getString("arez.kontaktTelefonPrivat");
            lAktienregisterZusatz.kontaktTelefonGeschaeftlich = pErgebnis.getString("arez.kontaktTelefonGeschaeftlich");
            lAktienregisterZusatz.kontaktTelefonMobil = pErgebnis.getString("arez.kontaktTelefonMobil");
            lAktienregisterZusatz.kontaktTelefonFax = pErgebnis.getString("arez.kontaktTelefonFax");
        } catch (Exception e) {
            CaBug.drucke("DbAktienregisterZusatz.decodeErgebnis 001");
            System.err.println(" " + e.getMessage());
        }

        return lAktienregisterZusatz;
    }

    /********************* Fuellen Prepared Statement mit allen Feldern, beginnend bei offset.*****************
     * Kann sowohl für Insert, als auch für update verwendet werden.
     * 
     * offset= Startposition des ersten Feldes (also z.B. 1)
     */
    private int anzfelder = 31; /*Anpassen auf Anzahl der Felder pro Datensatz*/

    private void fuellePreparedStatementKomplett(PreparedStatement pPStm, int pOffset,
            EclAktienregisterZusatz pAktienregisterZusatz) {

        int startOffset = pOffset; /*Nur erforderlich zum Überprüfen von Programmierfehlern - Feldanzahl*/

        try {
            pPStm.setInt(pOffset, pAktienregisterZusatz.mandant);
            pOffset++;
            pPStm.setInt(pOffset, pAktienregisterZusatz.aktienregisterIdent);
            pOffset++;
            pPStm.setLong(pOffset, pAktienregisterZusatz.db_version);
            pOffset++;

            pPStm.setInt(pOffset, pAktienregisterZusatz.hinweisAktionaersPortalBestaetigt);
            pOffset++;
            pPStm.setInt(pOffset, pAktienregisterZusatz.hinweisHVPortalBestaetigt);
            pOffset++;
            pPStm.setInt(pOffset, pAktienregisterZusatz.eMailRegistrierung);
            pOffset++;
            pPStm.setString(pOffset, pAktienregisterZusatz.eMailRegistrierungErstZeitpunkt);
            pOffset++;
            pPStm.setInt(pOffset, pAktienregisterZusatz.eigenesPasswort);
            pOffset++;

            pPStm.setString(pOffset, pAktienregisterZusatz.passwortVergessenLink);
            pOffset++;
            pPStm.setString(pOffset, pAktienregisterZusatz.passwortVergessenZeitpunkt);
            pOffset++;
            pPStm.setInt(pOffset, pAktienregisterZusatz.kommunikationssprache);
            pOffset++;

            pPStm.setString(pOffset, CaString.trunc(pAktienregisterZusatz.eMailFuerVersand, 80));
            pOffset++;
            pPStm.setInt(pOffset, pAktienregisterZusatz.emailBestaetigt);
            pOffset++;
            pPStm.setString(pOffset, pAktienregisterZusatz.emailBestaetigenLink);
            pOffset++;

            pPStm.setString(pOffset, CaString.trunc(pAktienregisterZusatz.eMail2FuerVersand, 40));
            pOffset++;
            pPStm.setInt(pOffset, pAktienregisterZusatz.email2Bestaetigt);
            pOffset++;
            pPStm.setString(pOffset, pAktienregisterZusatz.email2BestaetigenLink);
            pOffset++;

            pPStm.setInt(pOffset, pAktienregisterZusatz.publikationenZustellung[0]);
            pOffset++;
            pPStm.setInt(pOffset, pAktienregisterZusatz.publikationenZustellung[1]);
            pOffset++;
            pPStm.setInt(pOffset, pAktienregisterZusatz.publikationenZustellung[2]);
            pOffset++;
            pPStm.setInt(pOffset, pAktienregisterZusatz.publikationenZustellung[3]);
            pOffset++;
            pPStm.setInt(pOffset, pAktienregisterZusatz.publikationenZustellung[4]);
            pOffset++;
            pPStm.setInt(pOffset, pAktienregisterZusatz.publikationenZustellung[5]);
            pOffset++;
            pPStm.setInt(pOffset, pAktienregisterZusatz.publikationenZustellung[6]);
            pOffset++;
            pPStm.setInt(pOffset, pAktienregisterZusatz.publikationenZustellung[7]);
            pOffset++;
            pPStm.setInt(pOffset, pAktienregisterZusatz.publikationenZustellung[8]);
            pOffset++;
            pPStm.setInt(pOffset, pAktienregisterZusatz.publikationenZustellung[9]);
            pOffset++;

            pPStm.setString(pOffset, CaString.trunc(pAktienregisterZusatz.kontaktTelefonPrivat, 40));
            pOffset++;
            pPStm.setString(pOffset, CaString.trunc(pAktienregisterZusatz.kontaktTelefonGeschaeftlich, 40));
            pOffset++;
            pPStm.setString(pOffset, CaString.trunc(pAktienregisterZusatz.kontaktTelefonMobil, 40));
            pOffset++;
            pPStm.setString(pOffset, CaString.trunc(pAktienregisterZusatz.kontaktTelefonFax, 40));
            pOffset++;

            if (pOffset - startOffset != anzfelder) {
                /*Nur wg. Überprüfung auf Programmierfehler - alle Felder berücksichtigt?*/
                CaBug.drucke("DbAktienregisterZusatz.fuellePreparedStatementKomplett 002");
            }

        } catch (SQLException e) {
            CaBug.drucke("DbAktienregisterZusatz.fuellePreparedStatementKomplett 001");
            e.printStackTrace();
        }

    }

    /**Insert
     * 
     * Feld mandant wird von dieser Funktion immer selbstständig belegt.
     * 
     * Returnwert:
     * =1 => Insert erfolgreich
     * ansonsten: Fehler
     */
    public int insert(EclAktienregisterZusatz pAktienregisterZusatz) {

        int erg = 0;

        /* Start Transaktion */
        dbBasis.beginTransaction();

        /* neue InterneIdent vergeben */
        /*	In diesem Fall nicht benötigt. Ist nur enthalten wg. "Blaupausenfunktionalität"
        erg=dbBasis.getInterneIdentAktienregisterEintrag();
        if (erg<1){
        	CaBug.drucke("DbAktienregisterZusatz.insert 002");
        	dbBasis.rollbackTransaction();dbBasis.endTransaction();return (erg);
        }
        
        lAktienregisterZusatz.aktienregisterIdent=erg;
        
        */

        pAktienregisterZusatz.mandant = dbBundle.clGlobalVar.mandant;

        /* Satz einfügen: 
         * Verarbeitungshinweis: 
         * 	>	nachdem InterneIdent immer eindeutig vergeben werden, ist prinzipiell eine "Doppeleinfügung"
         * 		von InterneIdent nicht möglich. Sollte es dazu kommen, ist das immer ein Programmfehler!
         */

        try {

            /*Felder Neuanlage füllen*/
            String lSql = "INSERT INTO " + dbBundle.getSchemaMandant() + "tbl_aktienregisterZusatz " + "("
                    + "mandant, aktienregisterIdent, db_version, "
                    + "hinweisAktionaersPortalBestaetigt, hinweisHVPortalBestaetigt, eMailRegistrierung, eMailRegistrierungErstZeitpunkt, eigenesPasswort, "
                    + "passwortVergessenLink, passwortVergessenZeitpunkt, kommunikationssprache, "
                    + "eMailFuerVersand, emailBestaetigt, emailBestaetigenLink, "
                    + "eMail2FuerVersand, email2Bestaetigt, email2BestaetigenLink, "
                    + "publikationenZustellung0, publikationenZustellung1, publikationenZustellung2, publikationenZustellung3, publikationenZustellung4, "
                    + "publikationenZustellung5, publikationenZustellung6, publikationenZustellung7, publikationenZustellung8, publikationenZustellung9, "
                    + "kontaktTelefonPrivat, kontaktTelefonGeschaeftlich, kontaktTelefonMobil, kontaktTelefonFax" + ")"
                    + "VALUES (" + "?, ?, ?, " + "?, ?, ?, ?, ?, " + "?, ?, ?, " + "?, ?, ?, " + "?, ?, ?, "
                    + "?, ?, ?, ?, ?, " + "?, ?, ?, ?, ?, " + "?, ?, ?, ? " + ")";
            PreparedStatement lPStm = verbindung.prepareStatement(lSql, ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);

            fuellePreparedStatementKomplett(lPStm, 1, pAktienregisterZusatz);

            erg = lPStm.executeUpdate();
            lPStm.close();
        } catch (Exception e2) {
            CaBug.drucke("DbAktienregisterZusatz.insert 001");
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

    /**Überprüfen, ob insert möglich ist (intern: es wird ein read-Befehl abgesetzt), oder mittlerweile bereits ein 
     * Satz vorhanden ist (dient dem Multiuser-Betrieb ...)
     * Gefüllt sein muss: der Primary Key (in diesem Fall: aktienregisterIdent; mandant wird automatisch gefüllt)
     * 
     * true=insert ist noch möglich
     * false=mittlerweile existiert ein Satz mit diesem Primary Key, oder es sit ein Fehler aufgetreten
     * */
    public boolean insertCheck(EclAktienregisterZusatz pAktienregisterZusatz) {
        int anzInArray = 0;
        PreparedStatement lPStm = null;

        if (pAktienregisterZusatz == null) {
            CaBug.drucke("DbAktienregisterZusatz.insertCheck 001");
            return false;
        }

        try {
            String lSql = "SELECT arez.* from " + dbBundle.getSchemaMandant() + "tbl_aktienregisterZusatz arez where "
                    + "arez.mandant=? AND " + "arez.aktienregisterIdent=? ORDER BY arez.aktienregisterIdent;";
            lPStm = verbindung.prepareStatement(lSql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            lPStm.setInt(1, dbBundle.clGlobalVar.mandant);
            lPStm.setInt(2, pAktienregisterZusatz.aktienregisterIdent);

            ResultSet lErgebnis = lPStm.executeQuery();
            lErgebnis.last();
            anzInArray = lErgebnis.getRow();
            lErgebnis.beforeFirst();

            lErgebnis.close();
            lPStm.close();

            if (anzInArray > 0) {
                return false;
            } else {
                return true;
            }

        } catch (Exception e) {
            CaBug.drucke("DbAktienregisterZusatz.insertCheck 003");
            System.err.println(" " + e.getMessage());
            return (false);
        }

    }

    /**Realisierte Selektion derzeit, jeweils einzeln: 
     * aktienregisterIdent
     * passwortVergessenLink,
     * emailBestaetigenLink,
     * email2BestaetigenLink
     * emailFuerVersand
     * 
     * Return-Wert:
     * >=1 => Anzahl der gefundenen Datensätze
     * =0 => nix gefunden
     * <0 => Fehler
     * */
    public int read(EclAktienregisterZusatz pAktienregisterZusatz) {
        int anzInArray = 0;
        PreparedStatement lPStm = null;
        int gefSelect = 0; /*Nur zum Aufdecken von Programmierfehlern - in diesem Fall: unvollständige Parameterübergabe*/

        if (pAktienregisterZusatz == null) {
            CaBug.drucke("DbAktienregisterZusatz.read 001");
            return -1;
        }

        try {
            if (pAktienregisterZusatz.aktienregisterIdent != 0) {
                gefSelect = 1;
                String lSql = "SELECT arez.* from " + dbBundle.getSchemaMandant()
                        + "tbl_aktienregisterZusatz arez where " + "arez.mandant=? AND "
                        + "arez.aktienregisterIdent=? ORDER BY arez.aktienregisterIdent;";
                lPStm = verbindung.prepareStatement(lSql, ResultSet.TYPE_SCROLL_INSENSITIVE,
                        ResultSet.CONCUR_READ_ONLY);
                lPStm.setInt(1, dbBundle.clGlobalVar.mandant);
                lPStm.setInt(2, pAktienregisterZusatz.aktienregisterIdent);
            }
            if (!pAktienregisterZusatz.passwortVergessenLink.isEmpty()) {
                gefSelect = 2;
                String lSql = "SELECT arez.* from " + dbBundle.getSchemaMandant()
                        + "tbl_aktienregisterZusatz arez where " + "arez.mandant=? AND "
                        + "arez.passwortVergessenLink=? ORDER BY arez.passwortVergessenLink;";
                lPStm = verbindung.prepareStatement(lSql, ResultSet.TYPE_SCROLL_INSENSITIVE,
                        ResultSet.CONCUR_READ_ONLY);
                lPStm.setInt(1, dbBundle.clGlobalVar.mandant);
                lPStm.setString(2, pAktienregisterZusatz.passwortVergessenLink);
            }
            if (!pAktienregisterZusatz.emailBestaetigenLink.isEmpty()) {
                gefSelect = 3;
                String lSql = "SELECT arez.* from " + dbBundle.getSchemaMandant()
                        + "tbl_aktienregisterZusatz arez where " + "arez.mandant=? AND "
                        + "arez.emailBestaetigenLink=? ORDER BY arez.emailBestaetigenLink;";
                lPStm = verbindung.prepareStatement(lSql, ResultSet.TYPE_SCROLL_INSENSITIVE,
                        ResultSet.CONCUR_READ_ONLY);
                lPStm.setInt(1, dbBundle.clGlobalVar.mandant);
                lPStm.setString(2, pAktienregisterZusatz.emailBestaetigenLink);
            }
            if (!pAktienregisterZusatz.email2BestaetigenLink.isEmpty()) {
                gefSelect = 4;
                String lSql = "SELECT arez.* from " + dbBundle.getSchemaMandant()
                        + "tbl_aktienregisterZusatz arez where " + "arez.mandant=? AND "
                        + "arez.email2BestaetigenLink=? ORDER BY arez.email2BestaetigenLink;";
                lPStm = verbindung.prepareStatement(lSql, ResultSet.TYPE_SCROLL_INSENSITIVE,
                        ResultSet.CONCUR_READ_ONLY);
                lPStm.setInt(1, dbBundle.clGlobalVar.mandant);
                lPStm.setString(2, pAktienregisterZusatz.email2BestaetigenLink);
            }
            if (!pAktienregisterZusatz.eMailFuerVersand.isEmpty()) {
                gefSelect = 4;
                String lSql = "SELECT arez.* from " + dbBundle.getSchemaMandant()
                        + "tbl_aktienregisterZusatz arez where " + "arez.mandant=? AND "
                        + "arez.eMailFuerVersand=? ORDER BY arez.eMailFuerVersand;";
                lPStm = verbindung.prepareStatement(lSql, ResultSet.TYPE_SCROLL_INSENSITIVE,
                        ResultSet.CONCUR_READ_ONLY);
                lPStm.setInt(1, dbBundle.clGlobalVar.mandant);
                lPStm.setString(2, pAktienregisterZusatz.eMailFuerVersand);
            }

            if (gefSelect == 0) {
                CaBug.drucke("DbAktienregisterZusatz.read 002");
                return -1;
            }

            ResultSet lErgebnis = lPStm.executeQuery();
            lErgebnis.last();
            anzInArray = lErgebnis.getRow();
            lErgebnis.beforeFirst();

            ergebnisArray = new EclAktienregisterZusatz[anzInArray];

            int i = 0;
            while (lErgebnis.next() == true) {
                ergebnisArray[i] = this.decodeErgebnis(lErgebnis);
                i++;
            }
            lErgebnis.close();
            lPStm.close();

        } catch (Exception e) {
            CaBug.drucke("DbAktienregisterZusatz.read 003");
            System.err.println(" " + e.getMessage());
            return (-1);
        }
        return (anzInArray);
    }

    /**Update. Versionsnummer wird um 1 hochgezählt
     * 
     * Feld mandant wird von dieser Funktion immer selbstständig belegt.
     * 
     * Zum Sicherstellen der Multiuserfähigkeit muß in jedem Fall der rc CaFehler.pfXyWurdeVonAnderemBenutzerVeraendert
     * nach Aufruf dieser Funktion abgefangen werden.
     * Ansonsten: rc=1 => ok, ansonsten Fehler
     */
    public int update(EclAktienregisterZusatz pAktienregisterZusatz) {

        pAktienregisterZusatz.db_version++;

        try {

            String lSql = "UPDATE " + dbBundle.getSchemaMandant() + "tbl_aktienregisterZusatz SET "
                    + "mandant=?, aktienregisterIdent=?, db_version=?, "
                    + "hinweisAktionaersPortalBestaetigt=?, hinweisHVPortalBestaetigt=?, eMailRegistrierung=?, eMailRegistrierungErstZeitpunkt=?, eigenesPasswort=?, "
                    + "passwortVergessenLink=?, passwortVergessenZeitpunkt=?, kommunikationssprache=?, "
                    + "eMailFuerVersand=?, emailBestaetigt=?, emailBestaetigenLink=?, "
                    + "eMail2FuerVersand=?, email2Bestaetigt=?, email2BestaetigenLink=?, "
                    + "publikationenZustellung0=?, publikationenZustellung1=?, publikationenZustellung2=?, publikationenZustellung3=?, publikationenZustellung4=?, "
                    + "publikationenZustellung5=?, publikationenZustellung6=?, publikationenZustellung7=?, publikationenZustellung8=?, publikationenZustellung9=?, "
                    + "kontaktTelefonPrivat=?, kontaktTelefonGeschaeftlich=?, kontaktTelefonMobil=?, kontaktTelefonFax=? "
                    + "WHERE " + "aktienregisterIdent=? AND " + "db_version=? AND mandant=?";

            PreparedStatement lPStm = verbindung.prepareStatement(lSql);
            fuellePreparedStatementKomplett(lPStm, 1, pAktienregisterZusatz);
            lPStm.setInt(anzfelder + 1, pAktienregisterZusatz.aktienregisterIdent);
            lPStm.setLong(anzfelder + 2, pAktienregisterZusatz.db_version - 1);
            lPStm.setInt(anzfelder + 3, dbBundle.clGlobalVar.mandant);

            int ergebnis = lPStm.executeUpdate();
            lPStm.close();
            if (ergebnis == 0) {
                return (CaFehler.pfXyWurdeVonAnderemBenutzerVeraendert);
            }
        } catch (Exception e1) {
            CaBug.drucke("DbAktienregisterZusatz.update 001");
            System.err.println(" " + e1.getMessage());
            return (CaFehler.pfdXyBereitsVorhanden);
        }

        return (1);
    }

    /**Überprüfen, ob update möglich ist (intern: es wird ein read-Befehl abgesetzt), oder mittlerweile bereits der 
     * Satz geändert wurde (dient dem Multiuser-Betrieb ...)
     * Gefüllt sein muss: der Primary Key (in diesem Fall: aktienregisterIdent; mandant wird automatisch gefüllt),
     * sowie db_version
     * 
     * Ergebnis des read wird normal im Ergebnis-Array abgelegt.
     * 
     * true=update ist noch möglich
     * false=mittlerweile wurde der Satz verändert, oder es ist ein Fehler aufgetreten
     * */
    public boolean updateCheck(EclAktienregisterZusatz pAktienregisterZusatz) {
        int anzInArray = 0;
        PreparedStatement lPStm = null;

        if (pAktienregisterZusatz == null) {
            CaBug.drucke("DbAktienregisterZusatz.updateCheck 001");
            return false;
        }

        try {
            String lSql = "SELECT arez.* from " + dbBundle.getSchemaMandant() + "tbl_aktienregisterZusatz arez where "
                    + "arez.mandant=? AND "
                    + "arez.aktienregisterIdent=? and arez.db_version=? ORDER BY arez.aktienregisterIdent;";
            lPStm = verbindung.prepareStatement(lSql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            lPStm.setInt(1, dbBundle.clGlobalVar.mandant);
            lPStm.setInt(2, pAktienregisterZusatz.aktienregisterIdent);
            lPStm.setLong(3, pAktienregisterZusatz.db_version);

            ResultSet lErgebnis = lPStm.executeQuery();
            lErgebnis.last();
            anzInArray = lErgebnis.getRow();
            lErgebnis.beforeFirst();

            ergebnisArray = new EclAktienregisterZusatz[anzInArray];

            int i = 0;
            while (lErgebnis.next() == true) {
                ergebnisArray[i] = this.decodeErgebnis(lErgebnis);
                i++;
            }

            lErgebnis.close();
            lPStm.close();

            if (anzInArray > 0) {
                return true;
            } else {
                return false;
            }

        } catch (Exception e) {
            CaBug.drucke("DbAktienregisterZusatz.updateCheck 003");
            System.err.println(" " + e.getMessage());
            return (false);
        }

    }

    /**Update. 
     * 
     * Feld mandant wird von dieser Funktion immer selbstständig belegt.
     * Feld hinweisAktionaersPortalBestaetigt wird für alle Aktionäre auf 0 gesetzt.
     * Ansonsten: rc=1 => ok, ansonsten Fehler
     */
    public int updateSetzeHinweisAktionaersPortalBestaetigtAuf0() {

        try {

            String lSql = "UPDATE " + dbBundle.getSchemaMandant() + "tbl_aktienregisterZusatz SET "
                    + "hinweisAktionaersPortalBestaetigt=0 " + "WHERE " + "mandant=?";

            PreparedStatement lPStm = verbindung.prepareStatement(lSql);
            lPStm.setInt(1, dbBundle.clGlobalVar.mandant);

            int ergebnis = lPStm.executeUpdate();
            lPStm.close();
            if (ergebnis == 0) {
                return (CaFehler.pfXyWurdeVonAnderemBenutzerVeraendert);
            }
        } catch (Exception e1) {
            CaBug.drucke("DbAktienregisterZusatz.updateSetzeHinweisAktionaersPortalBestaetigtAuf0 001");
            System.err.println(" " + e1.getMessage());
            return (CaFehler.pfdXyBereitsVorhanden);
        }

        return (1);
    }

    /**Update. 
     * 
     * Feld mandant wird von dieser Funktion immer selbstständig belegt.
     * Feld hinweisHVPortalBestaetigt wird für alle Aktionäre auf 0 gesetzt.
     * Ansonsten: rc=1 => ok, ansonsten Fehler
     */
    public int updateSetzeHinweisHVPortalBestaetigtAuf0() {

        try {

            String lSql = "UPDATE " + dbBundle.getSchemaMandant() + "tbl_aktienregisterZusatz SET "
                    + "hinweisHVPortalBestaetigt=0 " + "WHERE " + "mandant=?";

            PreparedStatement lPStm = verbindung.prepareStatement(lSql);
            lPStm.setInt(1, dbBundle.clGlobalVar.mandant);

            int ergebnis = lPStm.executeUpdate();
            lPStm.close();
            if (ergebnis == 0) {
                return (CaFehler.pfXyWurdeVonAnderemBenutzerVeraendert);
            }
        } catch (Exception e1) {
            CaBug.drucke("DbAktienregisterZusatz.updateSetzeHinweisAktionaersPortalBestaetigtAuf0 001");
            System.err.println(" " + e1.getMessage());
            return (CaFehler.pfdXyBereitsVorhanden);
        }

        return (1);
    }

    /**Update. 
     * 
     * Feld mandant wird von dieser Funktion immer selbstständig belegt.
     * Feld eMailRegistrierung wird für alle Aktionäre auf 0 gesetzt, die nicht bereits
     * die Registrierung durchgeführt haben (99). D.h. Neuaufforderung zur Registrierung nach
     * dem nächsten Einloggen.
     * Ansonsten: rc=1 => ok, ansonsten Fehler
     */
    public int updateSetzeEMailRegistrierungZurueck() {

        try {

            String lSql = "UPDATE " + dbBundle.getSchemaMandant() + "tbl_aktienregisterZusatz SET "
                    + "eMailRegistrierung=0 " + "WHERE eMailRegistrierung!=99 " + "AND mandant=?";

            PreparedStatement lPStm = verbindung.prepareStatement(lSql);
            lPStm.setInt(1, dbBundle.clGlobalVar.mandant);

            int ergebnis = lPStm.executeUpdate();
            lPStm.close();
            if (ergebnis == 0) {
                return (CaFehler.pfXyWurdeVonAnderemBenutzerVeraendert);
            }
        } catch (Exception e1) {
            CaBug.drucke("DbAktienregisterZusatz.updateSetzeHinweisAktionaersPortalBestaetigtAuf0 001");
            System.err.println(" " + e1.getMessage());
            return (CaFehler.pfdXyBereitsVorhanden);
        }

        return (1);
    }

}
