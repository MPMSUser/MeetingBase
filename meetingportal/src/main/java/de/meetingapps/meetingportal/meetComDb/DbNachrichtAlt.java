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

public class DbNachrichtAlt {
//
//    private Connection verbindung = null;
//    private DbBasis dbBasis = null;
//    private DbBundle dbBundle = null;
//
//    private EclNachricht ergebnisArray[] = null;
//
//    /*************************Initialisierung***************************/
//    public DbNachrichtAlt(DbBundle pDbBundle) {
//        /* Verbindung in lokale Daten eintragen*/
//        if (pDbBundle == null) {
//            CaBug.drucke("DbNachricht.init 001 - dbBundle nicht initialisiert");
//            return;
//        }
//        if (pDbBundle.dbBasis == null) {
//            CaBug.drucke("DbNachricht.init 002 - dbBasis nicht initialisiert");
//            return;
//        }
//
//        dbBasis = pDbBundle.dbBasis;
//        verbindung = pDbBundle.dbBasis.verbindung;
//        dbBundle = pDbBundle;
//    }
//
//    /**************Anzahl der Ergebnisse der read*-Methoden**************************/
//    public int anzErgebnis() {
//        if (ergebnisArray == null) {
//            return 0;
//        }
//        return ergebnisArray.length;
//    }
//
//    /**************Anzahl der Ergebnisse der read*-Methoden**************************/
//    public EclNachricht[] ergebnis() {
//        return ergebnisArray;
//    }
//
//    /**********Liefert pN-tes Element des Ergebnisses der read*Methoden**************
//     * pN geht von 0 bis anzErgebnis-1*/
//    public EclNachricht ergebnisPosition(int pN) {
//        if (ergebnisArray == null) {
//            CaBug.drucke("DbNachricht.ergebnisPosition 001");
//            return null;
//        }
//        if (pN < 0) {
//            CaBug.drucke("DbNachricht.ergebnisPosition 002");
//            return null;
//        }
//        if (pN >= ergebnisArray.length) {
//            CaBug.drucke("DbNachricht.ergebnisPosition 003");
//            return null;
//        }
//        return ergebnisArray[pN];
//    }
//
//    public int createTable() {
//        int rc = 0;
//        DbLowLevel lDbLowLevel = new DbLowLevel(dbBundle);
//        rc = lDbLowLevel.createTable("CREATE TABLE " + dbBundle.getSchemaAllgemein() + "tbl_nachricht ( "
//                + "`ident` int(11) NOT NULL, " + "`identMail` int(11) NOT NULL, " + "`mandant` int(11) NOT NULL, "
//                + "`hvJahr` int(11) NOT NULL DEFAULT '2018', " + "`hvNummer` char(1) NOT NULL DEFAULT 'A', "
//                + "`dbArt` char(1) NOT NULL DEFAULT 'P', " + "`istAntwortZuMailIdent` int(11) NOT NULL, "
//                + "`userIdAbsender` int(11) NOT NULL, " + "`userIdEmpfaenger` int(11) NOT NULL, "
//                + "`anzeigeBeimEmpfaengerAusblenden` int(11) NOT NULL, "
//                + "`anzeigeBeimSenderAusblenden` int(11) NOT NULL, " + "`bearbeitenBis` char(19) NOT NULL DEFAULT 'P', "
//                + "`mailIstBearbeitetVomEmpfaengerGesetzt` int(11) NOT NULL, "
//                + "`mailIstBearbeitetVomSenderGesetzt` int(11) NOT NULL, "
//                + "`sendezeitpunkt` char(19) NOT NULL DEFAULT 'P', "
//                + "`mailTextAuchInEmailAuffuehren` int(11) NOT NULL, " + "`anlagenSindVorhanden` int(11) NOT NULL, "
//                + "`verwendungsCode` int(11) NOT NULL, " + "`parameter1` varchar(20) DEFAULT NULL, "
//                + "`parameter2` varchar(20) DEFAULT NULL, " + "`parameter3` varchar(20) DEFAULT NULL, "
//                + "`parameter4` varchar(20) DEFAULT NULL, " + "`parameter5` varchar(20) DEFAULT NULL, "
//                + "`betreff` varchar(80) DEFAULT NULL, " + "`mailText` varchar(2000) DEFAULT NULL, "
//                + "PRIMARY KEY (`ident`) " + ") ");
//        return rc;
//    }
//
//    /**************************deleteAll Löschen aller Datensätze  eines Mandanten****************/
//    public int deleteAll() {
//        dbBundle.dbBasis.resetInterneIdentInsti();
//        return dbBundle.dbLowLevel.deleteAlle /*deleteMandant*/(
//                "DELETE FROM " + dbBundle.getSchemaAllgemein() + "tbl_nachricht;" /* where mandant=?*/);
//    }
//
//    //	/**************************setzt aktuelle Mandantennummer bei allen Datensätzen****************/
//    //	public int updateMandant(){
//    //		return dbBundle.dbLowLevel.rawUpdateMandant(dbBundle.getSchemaAllgemein()+"tbl_nachricht");
//    //	}
//
//    public void reorgInterneIdent() {
//        int lMax = dbBundle.dbLowLevel.liefereHoechsteIdentOhneMandant /*liefereHoechsteIdent*/(
//                "SELECT MAX(ident) FROM " + dbBundle.getSchemaAllgemein() + "tbl_nachricht;" /* where mandant=?*/ );
//        if (lMax != -1) {
//            dbBundle.dbBasis.resetInterneIdentNachricht(lMax);
//        }
//    }
//
//    /********** dekodiert die aktuelle Position aus ergebnis  und gibt dieses zurück******/
//    EclNachricht decodeErgebnis(ResultSet pErgebnis) {
//
//        EclNachricht lEclReturn = new EclNachricht();
//
//        try {
//            lEclReturn.ident = pErgebnis.getInt("nac.ident");
//            lEclReturn.identMail = pErgebnis.getInt("nac.identMail");
//
//            lEclReturn.mandant = pErgebnis.getInt("nac.mandant");
//            lEclReturn.hvJahr = pErgebnis.getInt("nac.hvJahr");
//            lEclReturn.hvNummer = pErgebnis.getString("nac.hvNummer");
//            lEclReturn.dbArt = pErgebnis.getString("nac.dbArt");
//
//            lEclReturn.antwortZuMailIdent = pErgebnis.getInt("nac.istAntwortZuMailIdent");
//            lEclReturn.userIdAbsender = pErgebnis.getInt("nac.userIdAbsender");
//            lEclReturn.userIdEmpfaenger = pErgebnis.getInt("nac.userIdEmpfaenger");
//            lEclReturn.anzeigeBeimEmpfaengerAusblenden = pErgebnis.getInt("nac.anzeigeBeimEmpfaengerAusblenden");
//            lEclReturn.anzeigeBeimSenderAusblenden = pErgebnis.getInt("nac.anzeigeBeimSenderAusblenden");
//
//            lEclReturn.bearbeitenBis = pErgebnis.getString("nac.bearbeitenBis");
//            lEclReturn.mailIstBearbeitetVomEmpfaengerGesetzt = pErgebnis
//                    .getInt("nac.mailIstBearbeitetVomEmpfaengerGesetzt");
//            lEclReturn.mailIstBearbeitetVomSenderGesetzt = pErgebnis.getInt("nac.mailIstBearbeitetVomSenderGesetzt");
//
//            lEclReturn.sendezeitpunkt = pErgebnis.getString("nac.sendezeitpunkt");
//
//            lEclReturn.mailTextAuchInEmailAuffuehren = pErgebnis.getInt("nac.mailTextAuchInEmailAuffuehren");
//            lEclReturn.anlagenSindVorhanden = pErgebnis.getInt("nac.anlagenSindVorhanden");
//            lEclReturn.verwendungsCode = pErgebnis.getInt("nac.verwendungsCode");
//
//            lEclReturn.parameter1 = pErgebnis.getString("nac.parameter1");
//            lEclReturn.parameter2 = pErgebnis.getString("nac.parameter2");
//            lEclReturn.parameter3 = pErgebnis.getString("nac.parameter3");
//            lEclReturn.parameter4 = pErgebnis.getString("nac.parameter4");
//            lEclReturn.parameter5 = pErgebnis.getString("nac.parameter5");
//
//            lEclReturn.betreff = pErgebnis.getString("nac.betreff");
//            lEclReturn.mailText = pErgebnis.getString("nac.mailText");
//
//        } catch (Exception e) {
//            CaBug.drucke("DbNachricht.decodeErgebnis 001");
//            System.err.println(" " + e.getMessage());
//        }
//
//        return lEclReturn;
//    }
//
//    /********************* Fuellen Prepared Statement mit allen Feldern, beginnend bei offset.*****************
//     * Kann sowohl für Insert, als auch für update verwendet werden.
//     * 
//     * offset= Startposition des ersten Feldes (also z.B. 1)
//     */
//    private int anzfelder = 25; /*Anpassen auf Anzahl der Felder pro Datensatz*/
//
//    private void fuellePreparedStatementKomplett(PreparedStatement pPStm, int pOffset, EclNachricht pEcl) {
//
//        int startOffset = pOffset; /*Nur erforderlich zum Überprüfen von Programmierfehlern - Feldanzahl*/
//
//        try {
//            pPStm.setInt(pOffset, pEcl.ident);
//            pOffset++;
//            pPStm.setInt(pOffset, pEcl.identMail);
//            pOffset++;
//
//            pPStm.setInt(pOffset, pEcl.mandant);
//            pOffset++;
//            pPStm.setInt(pOffset, pEcl.hvJahr);
//            pOffset++;
//            pPStm.setString(pOffset, pEcl.hvNummer);
//            pOffset++;
//            pPStm.setString(pOffset, pEcl.dbArt);
//            pOffset++;
//
//            pPStm.setInt(pOffset, pEcl.antwortZuMailIdent);
//            pOffset++;
//            pPStm.setInt(pOffset, pEcl.userIdAbsender);
//            pOffset++;
//            pPStm.setInt(pOffset, pEcl.userIdEmpfaenger);
//            pOffset++;
//            pPStm.setInt(pOffset, pEcl.anzeigeBeimEmpfaengerAusblenden);
//            pOffset++;
//            pPStm.setInt(pOffset, pEcl.anzeigeBeimSenderAusblenden);
//            pOffset++;
//
//            pPStm.setString(pOffset, pEcl.bearbeitenBis);
//            pOffset++;
//            pPStm.setInt(pOffset, pEcl.mailIstBearbeitetVomEmpfaengerGesetzt);
//            pOffset++;
//            pPStm.setInt(pOffset, pEcl.mailIstBearbeitetVomSenderGesetzt);
//            pOffset++;
//
//            pPStm.setString(pOffset, pEcl.sendezeitpunkt);
//            pOffset++;
//
//            pPStm.setInt(pOffset, pEcl.mailTextAuchInEmailAuffuehren);
//            pOffset++;
//            pPStm.setInt(pOffset, pEcl.anlagenSindVorhanden);
//            pOffset++;
//            pPStm.setInt(pOffset, pEcl.verwendungsCode);
//            pOffset++;
//
//            pPStm.setString(pOffset, pEcl.parameter1);
//            pOffset++;
//            pPStm.setString(pOffset, pEcl.parameter2);
//            pOffset++;
//            pPStm.setString(pOffset, pEcl.parameter3);
//            pOffset++;
//            pPStm.setString(pOffset, pEcl.parameter4);
//            pOffset++;
//            pPStm.setString(pOffset, pEcl.parameter5);
//            pOffset++;
//
//            pPStm.setString(pOffset, pEcl.betreff);
//            pOffset++;
//            pPStm.setString(pOffset, pEcl.mailText);
//            pOffset++;
//
//            if (pOffset - startOffset != anzfelder) {
//                /*Nur wg. Überprüfung auf Programmierfehler - alle Felder berücksichtigt?*/
//                CaBug.drucke("DbNachricht.fuellePreparedStatementKomplett 002");
//            }
//
//        } catch (SQLException e) {
//            CaBug.drucke("DbNachricht.fuellePreparedStatementKomplett 001");
//            e.printStackTrace();
//        }
//
//    }
//
//    /**Insert
//     * 
//     * Returnwert:
//     * =1 => Insert erfolgreich
//     * ansonsten: Fehler
//     */
//    public int insert(EclNachricht pEcl) {
//
//        int erg = 0;
//        dbBasis.beginTransaction();
//
//        /* neue InterneIdent vergeben */
//        erg = dbBasis.getInterneIdentNachricht();
//        if (erg < 1) {
//            CaBug.drucke("DbNachricht.insert 002");
//            dbBasis.endTransaction();
//            return (erg);
//        }
//
//        pEcl.ident = erg;
//
//        try {
//
//            /*Felder Neuanlage füllen*/
//            String lSql = "INSERT INTO " + dbBundle.getSchemaAllgemein() + "tbl_nachricht " + "("
//                    + "ident, identMail, mandant, hvJahr, hvNummer, dbArt, "
//                    + "istAntwortZuMailIdent, userIdAbsender, userIdEmpfaenger, "
//                    + "anzeigeBeimEmpfaengerAusblenden, anzeigeBeimSenderAusblenden, "
//                    + "bearbeitenBis, mailIstBearbeitetVomEmpfaengerGesetzt, mailIstBearbeitetVomSenderGesetzt, sendezeitpunkt, "
//                    + "mailTextAuchInEmailAuffuehren, anlagenSindVorhanden, "
//                    + "verwendungsCode, parameter1, parameter2, parameter3, parameter4, parameter5, betreff, mailText "
//                    + ")" + "VALUES (" + "?, ?, ?, ?, ?, ?, " + "?, ?, ?, " + "?, ?, " + "?, ?, ?, ?, " + "?, ?, "
//                    + "?, ?, ?, ?, ?, ?, ?, ?" + ")";
//            PreparedStatement lPStm = verbindung.prepareStatement(lSql, ResultSet.TYPE_SCROLL_INSENSITIVE,
//                    ResultSet.CONCUR_READ_ONLY);
//
//            fuellePreparedStatementKomplett(lPStm, 1, pEcl);
//
//            erg = lPStm.executeUpdate();
//            lPStm.close();
//        } catch (Exception e2) {
//            CaBug.drucke("DbNachricht.insert 001");
//            System.err.println(" " + e2.getMessage());
//        }
//
//        if (erg == 0) {/*Fehler beim Einfügen - d.h. primaryKey bereits vorhanden*/
//            dbBasis.rollbackTransaction();
//            dbBasis.endTransaction();
//            return (-1);
//        }
//
//        /* Ende Transaktion */
//        dbBasis.endTransaction();
//        return (1);
//    }
//
//    /**
//     * 
//     * Return-Wert = Anzahl der gefundenen Sätze (oder Fehlermeldung <0)*/
//    public int read(int pIdent) {
//        int anzInArray = 0;
//        PreparedStatement lPStm = null;
//
//        try {
//            String lSql = "SELECT * from " + dbBundle.getSchemaAllgemein() + "tbl_nachricht nac where " + "nac.ident=? "
//                    + "ORDER BY nac.ident;";
//            lPStm = verbindung.prepareStatement(lSql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
//            lPStm.setInt(1, pIdent);
//
//            ResultSet lErgebnis = lPStm.executeQuery();
//            lErgebnis.last();
//            anzInArray = lErgebnis.getRow();
//            lErgebnis.beforeFirst();
//
//            ergebnisArray = new EclNachricht[anzInArray];
//
//            int i = 0;
//            while (lErgebnis.next() == true) {
//                ergebnisArray[i] = this.decodeErgebnis(lErgebnis);
//                i++;
//            }
//            lErgebnis.close();
//            lPStm.close();
//
//        } catch (Exception e) {
//            CaBug.drucke("DbNachricht.read 003");
//            System.err.println(" " + e.getMessage());
//            return (-1);
//        }
//        return (anzInArray);
//    }
//
//    /**
//     * 
//     * Return-Wert = Anzahl der gefundenen Sätze (oder Fehlermeldung <0)*/
//    public int readZuEmpfaenger(int pUserIdEmpfaenger) {
//        int anzInArray = 0;
//        PreparedStatement lPStm = null;
//
//        try {
//            String lSql = "SELECT * from " + dbBundle.getSchemaAllgemein() + "tbl_nachricht nac where "
//                    + "nac.userIdEmpfaenger=? " + "ORDER BY nac.ident;";
//            lPStm = verbindung.prepareStatement(lSql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
//            lPStm.setInt(1, pUserIdEmpfaenger);
//
//            ResultSet lErgebnis = lPStm.executeQuery();
//            lErgebnis.last();
//            anzInArray = lErgebnis.getRow();
//            lErgebnis.beforeFirst();
//
//            ergebnisArray = new EclNachricht[anzInArray];
//
//            int i = 0;
//            while (lErgebnis.next() == true) {
//                ergebnisArray[i] = this.decodeErgebnis(lErgebnis);
//                i++;
//            }
//            lErgebnis.close();
//            lPStm.close();
//
//        } catch (Exception e) {
//            CaBug.drucke("DbNachricht.readZuEmpfaenger 003");
//            System.err.println(" " + e.getMessage());
//            return (-1);
//        }
//        return (anzInArray);
//    }
//
//    /**
//     * 
//     * Return-Wert = Anzahl der gefundenen Sätze (oder Fehlermeldung <0)*/
//    public int readZuSender(int pUserIdSender) {
//        int anzInArray = 0;
//        PreparedStatement lPStm = null;
//
//        try {
//            String lSql = "SELECT * from " + dbBundle.getSchemaAllgemein() + "tbl_nachricht nac where "
//                    + "nac.userIdAbsender=? " + "ORDER BY nac.ident;";
//            lPStm = verbindung.prepareStatement(lSql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
//            lPStm.setInt(1, pUserIdSender);
//
//            ResultSet lErgebnis = lPStm.executeQuery();
//            lErgebnis.last();
//            anzInArray = lErgebnis.getRow();
//            lErgebnis.beforeFirst();
//
//            ergebnisArray = new EclNachricht[anzInArray];
//
//            int i = 0;
//            while (lErgebnis.next() == true) {
//                ergebnisArray[i] = this.decodeErgebnis(lErgebnis);
//                i++;
//            }
//            lErgebnis.close();
//            lPStm.close();
//
//        } catch (Exception e) {
//            CaBug.drucke("DbNachricht.readZuEmpfaenger 003");
//            System.err.println(" " + e.getMessage());
//            return (-1);
//        }
//        return (anzInArray);
//    }
//
//    /**
//     * 
//     * Return-Wert = Anzahl der gefundenen Sätze (oder Fehlermeldung <0)*/
//    public int readZuIdentMail(int pidentMail) {
//        int anzInArray = 0;
//        PreparedStatement lPStm = null;
//
//        try {
//            String lSql = "SELECT * from " + dbBundle.getSchemaAllgemein() + "tbl_nachricht nac where "
//                    + "nac.identMail=? " + "ORDER BY nac.ident;";
//            lPStm = verbindung.prepareStatement(lSql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
//            lPStm.setInt(1, pidentMail);
//
//            ResultSet lErgebnis = lPStm.executeQuery();
//            lErgebnis.last();
//            anzInArray = lErgebnis.getRow();
//            lErgebnis.beforeFirst();
//
//            ergebnisArray = new EclNachricht[anzInArray];
//
//            int i = 0;
//            while (lErgebnis.next() == true) {
//                ergebnisArray[i] = this.decodeErgebnis(lErgebnis);
//                i++;
//            }
//            lErgebnis.close();
//            lPStm.close();
//
//        } catch (Exception e) {
//            CaBug.drucke("DbNachricht.readZuIdentMail 003");
//            System.err.println(" " + e.getMessage());
//            return (-1);
//        }
//        return (anzInArray);
//    }
//
//    /**Update. 
//     * 
//     * Returnwert:
//     * pfXyWurdeVonAnderemBenutzerVeraendert
//     * -1 => unbekannter Fehler
//     * 1 = Update wurde durchgeführt.
//     * 
//     */
//    public int update(EclNachricht pEcl) {
//
//        try {
//
//            String lSql = "UPDATE " + dbBundle.getSchemaAllgemein() + "tbl_nachricht SET "
//                    + "ident=?, identMail=?, mandant=?, hvJahr=?, hvNummer=?, dbArt=?, "
//                    + "istAntwortZuMailIdent=?, userIdAbsender=?, userIdEmpfaenger=?, "
//                    + "anzeigeBeimEmpfaengerAusblenden=?, anzeigeBeimSenderAusblenden=?, "
//                    + "bearbeitenBis=?, mailIstBearbeitetVomEmpfaengerGesetzt=?, mailIstBearbeitetVomSenderGesetzt=?, sendezeitpunkt=?, "
//                    + "mailTextAuchInEmailAuffuehren=?, anlagenSindVorhanden=?, "
//                    + "verwendungsCode=?, parameter1=?, parameter2=?, parameter3=?, parameter4=?, parameter5=?, betreff=?, mailText=? "
//                    + "WHERE " + "ident=?  ";
//
//            PreparedStatement lPStm = verbindung.prepareStatement(lSql);
//            fuellePreparedStatementKomplett(lPStm, 1, pEcl);
//            lPStm.setInt(anzfelder + 1, pEcl.ident);
//
//            int ergebnis = lPStm.executeUpdate();
//            lPStm.close();
//            if (ergebnis == 0) {
//                return (CaFehler.pfXyWurdeVonAnderemBenutzerVeraendert);
//            }
//        } catch (Exception e1) {
//            CaBug.drucke("DbNachricht.update 001");
//            System.err.println(" " + e1.getMessage());
//            return (-1);
//        }
//
//        return (1);
//    }
//
//    public int setzeEinblenden(int pIdent) {
//
//        try {
//
//            String lSql = "UPDATE " + dbBundle.getSchemaAllgemein() + "tbl_nachricht SET "
//                    + "anzeigeBeimEmpfaengerAusblenden=0 " + "WHERE " + "ident=?  ";
//
//            PreparedStatement lPStm = verbindung.prepareStatement(lSql);
//            lPStm.setInt(1, pIdent);
//
//            int ergebnis = lPStm.executeUpdate();
//            lPStm.close();
//            if (ergebnis == 0) {
//                return (CaFehler.pfXyWurdeVonAnderemBenutzerVeraendert);
//            }
//        } catch (Exception e1) {
//            CaBug.drucke("DbNachricht.setzeEinblenden 001");
//            System.err.println(" " + e1.getMessage());
//            return (-1);
//        }
//
//        return (1);
//    }
//
//    public int setzeAusblenden(int pIdent) {
//
//        try {
//
//            String lSql = "UPDATE " + dbBundle.getSchemaAllgemein() + "tbl_nachricht SET "
//                    + "anzeigeBeimEmpfaengerAusblenden=1 " + "WHERE " + "ident=?  ";
//
//            PreparedStatement lPStm = verbindung.prepareStatement(lSql);
//            lPStm.setInt(1, pIdent);
//
//            int ergebnis = lPStm.executeUpdate();
//            lPStm.close();
//            if (ergebnis == 0) {
//                return (CaFehler.pfXyWurdeVonAnderemBenutzerVeraendert);
//            }
//        } catch (Exception e1) {
//            CaBug.drucke("DbNachricht.setzeEinblenden 001");
//            System.err.println(" " + e1.getMessage());
//            return (-1);
//        }
//
//        return (1);
//    }
//
//    public int setzeBearbeitet(int pIdent) {
//
//        try {
//
//            String lSql = "UPDATE " + dbBundle.getSchemaAllgemein() + "tbl_nachricht SET "
//                    + "mailIstBearbeitetVomEmpfaengerGesetzt=1 " + "WHERE " + "ident=?  ";
//
//            PreparedStatement lPStm = verbindung.prepareStatement(lSql);
//            lPStm.setInt(1, pIdent);
//
//            int ergebnis = lPStm.executeUpdate();
//            lPStm.close();
//            if (ergebnis == 0) {
//                return (CaFehler.pfXyWurdeVonAnderemBenutzerVeraendert);
//            }
//        } catch (Exception e1) {
//            CaBug.drucke("DbNachricht.setzeEinblenden 001");
//            System.err.println(" " + e1.getMessage());
//            return (-1);
//        }
//
//        return (1);
//    }
//
//    public int setzeUnbearbeitet(int pIdent) {
//
//        try {
//
//            String lSql = "UPDATE " + dbBundle.getSchemaAllgemein() + "tbl_nachricht SET "
//                    + "mailIstBearbeitetVomEmpfaengerGesetzt=0 " + "WHERE " + "ident=?  ";
//
//            PreparedStatement lPStm = verbindung.prepareStatement(lSql);
//            lPStm.setInt(1, pIdent);
//
//            int ergebnis = lPStm.executeUpdate();
//            lPStm.close();
//            if (ergebnis == 0) {
//                return (CaFehler.pfXyWurdeVonAnderemBenutzerVeraendert);
//            }
//        } catch (Exception e1) {
//            CaBug.drucke("DbNachricht.setzeEinblenden 001");
//            System.err.println(" " + e1.getMessage());
//            return (-1);
//        }
//
//        return (1);
//    }
//
//    /**Return-Werte:
//     * pfXyWurdeVonAnderemBenutzerVeraendert
//     * -1 => undefinierter Fehler
//     * 1 => Löschen erfolgreich
//     */
//    public int delete(int pIdent) {
//        try {
//
//            String sql = "DELETE FROM " + dbBundle.getSchemaAllgemein() + "tbl_nachricht WHERE ident=? ";
//
//            PreparedStatement pstm1 = verbindung.prepareStatement(sql);
//            pstm1.setInt(1, pIdent);
//
//            int ergebnis1 = pstm1.executeUpdate();
//            pstm1.close();
//            if (ergebnis1 == 0) {
//                return (CaFehler.pfXyWurdeVonAnderemBenutzerVeraendert);
//            }
//        } catch (Exception e1) {
//            CaBug.drucke("DbNachricht.delete 001");
//            System.err.println(" " + e1.getMessage());
//            return (-1);
//        }
//
//        return (1);
//    }

}
