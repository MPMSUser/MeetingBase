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


public class DbNachrichtAnhang_Alt {
//
//    private Connection verbindung = null;
//    private DbBasis dbBasis = null;
//    private DbBundle dbBundle = null;
//
//    private EclNachrichtAnhang ergebnisArray[] = null;
//
//    /*************************Initialisierung***************************/
//    public DbNachrichtAnhang_Alt(DbBundle pDbBundle) {
//        /* Verbindung in lokale Daten eintragen*/
//        if (pDbBundle == null) {
//            CaBug.drucke("DbNachrichtAnhang.init 001 - dbBundle nicht initialisiert");
//            return;
//        }
//        if (pDbBundle.dbBasis == null) {
//            CaBug.drucke("DbNachrichtAnhang.init 002 - dbBasis nicht initialisiert");
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
//    public EclNachrichtAnhang[] ergebnis() {
//        return ergebnisArray;
//    }
//
//    /**********Liefert pN-tes Element des Ergebnisses der read*Methoden**************
//     * pN geht von 0 bis anzErgebnis-1*/
//    public EclNachrichtAnhang ergebnisPosition(int pN) {
//        if (ergebnisArray == null) {
//            CaBug.drucke("DbNachrichtAnhang.ergebnisPosition 001");
//            return null;
//        }
//        if (pN < 0) {
//            CaBug.drucke("DbNachrichtAnhang.ergebnisPosition 002");
//            return null;
//        }
//        if (pN >= ergebnisArray.length) {
//            CaBug.drucke("DbNachrichtAnhang.ergebnisPosition 003");
//            return null;
//        }
//        return ergebnisArray[pN];
//    }
//
//    public int createTable() {
//        int rc = 0;
//        DbLowLevel lDbLowLevel = new DbLowLevel(dbBundle);
//        rc = lDbLowLevel.createTable("CREATE TABLE " + dbBundle.getSchemaAllgemein() + "tbl_nachrichtAnhang ( "
//                + "`ident` int(11) NOT NULL, " 
//                + "`dateiname` varchar(100) DEFAULT NULL, "
//                + "`beschreibung` varchar(80) DEFAULT NULL, " 
//                + "`identMail` int(11) NOT NULL, "
//                + "PRIMARY KEY (`ident`) " + ") ");
//        return rc;
//    }
//
//    /**************************deleteAll Löschen aller Datensätze  eines Mandanten****************/
//    public int deleteAll() {
//        dbBundle.dbBasis.resetInterneIdentNachrichtAnhang();
//        return dbBundle.dbLowLevel.deleteAlle /*deleteMandant*/(
//                "DELETE FROM " + dbBundle.getSchemaAllgemein() + "tbl_nachrichtAnhang;" /* where mandant=?*/);
//    }
//
//    //	/**************************setzt aktuelle Mandantennummer bei allen Datensätzen****************/
//    //	public int updateMandant(){
//    //		return dbBundle.dbLowLevel.rawUpdateMandant(dbBundle.getSchemaAllgemein()+"tbl_nachrichtAnhang");
//    //	}
//
//    public void reorgInterneIdent() {
//        int lMax = dbBundle.dbLowLevel
//                .liefereHoechsteIdentOhneMandant /*liefereHoechsteIdent*/("SELECT MAX(ident) FROM "
//                        + dbBundle.getSchemaAllgemein() + "tbl_nachrichtAnhang;" /* where mandant=?*/ );
//        if (lMax != -1) {
//            dbBundle.dbBasis.resetInterneIdentNachrichtAnhang(lMax);
//        }
//    }
//
//    /********** dekodiert die aktuelle Position aus ergebnis  und gibt dieses zurück******/
//    EclNachrichtAnhang decodeErgebnis(ResultSet pErgebnis) {
//
//        EclNachrichtAnhang lEclReturn = new EclNachrichtAnhang();
//
//        try {
//            lEclReturn.ident = pErgebnis.getInt("ident");
//            lEclReturn.dateiname = pErgebnis.getString("dateiname");
//            lEclReturn.beschreibung = pErgebnis.getString("beschreibung");
//            lEclReturn.identMail = pErgebnis.getInt("ident");
//        } catch (Exception e) {
//            CaBug.drucke("DbNachrichtAnhang.decodeErgebnis 001");
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
//    private int anzfelder = 4; /*Anpassen auf Anzahl der Felder pro Datensatz*/
//
//    private void fuellePreparedStatementKomplett(PreparedStatement pPStm, int pOffset, EclNachrichtAnhang pEcl) {
//
//        int startOffset = pOffset; /*Nur erforderlich zum Überprüfen von Programmierfehlern - Feldanzahl*/
//
//        try {
//            pPStm.setInt(pOffset, pEcl.ident);
//            pOffset++;
//            pPStm.setString(pOffset, pEcl.dateiname);
//            pOffset++;
//            pPStm.setString(pOffset, pEcl.beschreibung);
//            pOffset++;
//            pPStm.setInt(pOffset, pEcl.identMail);
//            pOffset++;
//
//            if (pOffset - startOffset != anzfelder) {
//                /*Nur wg. Überprüfung auf Programmierfehler - alle Felder berücksichtigt?*/
//                CaBug.drucke("DbNachrichtAnhang.fuellePreparedStatementKomplett 002");
//            }
//
//        } catch (SQLException e) {
//            CaBug.drucke("DbNachrichtAnhang.fuellePreparedStatementKomplett 001");
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
//    public int insert(EclNachrichtAnhang pEcl) {
//
//        int erg = 0;
//        dbBasis.beginTransaction();
//
//        /* neue InterneIdent vergeben */
//        erg = dbBasis.getInterneIdentNachrichtAnhang();
//        if (erg < 1) {
//            CaBug.drucke("DbNachrichtAnhang.insert 002");
//            dbBasis.endTransaction();
//            return (erg);
//        }
//
//        pEcl.ident = erg;
//
//        try {
//
//            /*Felder Neuanlage füllen*/
//            String lSql = "INSERT INTO " + dbBundle.getSchemaAllgemein() + "tbl_nachrichtAnhang " + "("
//                    + "ident, dateiname, beschreibung, identMail " + ")" + "VALUES (" + "?, ?, ?, ? " + ")";
//            PreparedStatement lPStm = verbindung.prepareStatement(lSql, ResultSet.TYPE_SCROLL_INSENSITIVE,
//                    ResultSet.CONCUR_READ_ONLY);
//
//            fuellePreparedStatementKomplett(lPStm, 1, pEcl);
//
//            erg = lPStm.executeUpdate();
//            lPStm.close();
//        } catch (Exception e2) {
//            CaBug.drucke("DbNachrichtAnhang.insert 001");
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
//    public int read_IdentMail(int pIdentMail) {
//        int anzInArray = 0;
//        PreparedStatement lPStm = null;
//
//        try {
//            String lSql = "SELECT * from " + dbBundle.getSchemaAllgemein() + "tbl_nachrichtAnhang where "
//                    + "identMail=? " + "ORDER BY ident;";
//            lPStm = verbindung.prepareStatement(lSql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
//            lPStm.setInt(1, pIdentMail);
//
//            ResultSet lErgebnis = lPStm.executeQuery();
//            lErgebnis.last();
//            anzInArray = lErgebnis.getRow();
//            lErgebnis.beforeFirst();
//
//            ergebnisArray = new EclNachrichtAnhang[anzInArray];
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
//            CaBug.drucke("DbNachrichtAnhang.read 003");
//            System.err.println(" " + e.getMessage());
//            return (-1);
//        }
//        return (anzInArray);
//    }
//
//    //	/**Update. 
//    //	 * 
//    //	 * Returnwert:
//    //	 * pfXyWurdeVonAnderemBenutzerVeraendert
//    //	 * -1 => unbekannter Fehler
//    //	 * 1 = Update wurde durchgeführt.
//    //	 * 
//    //	 */
//    //	public int update(EclNachrichtAnhang pEcl){
//    //
//    //		try {
//    //
//    //			String lSql="UPDATE "+dbBundle.getSchemaAllgemein()+"tbl_nachrichtAnhang SET "
//    //					+ "mandant=?, ident=?, db_version=?, "
//    //					+ "kurzBezeichnung=?, identHauptKontakt=?, identSuchlaufBegriffe=?, standardSammelkarteMitWeisung=?, standardSammelkarteOhneWeisung=?, standardSammelkarteGruppennummer=?, "
//    //					+ "standardMeldeName=?, standardMeldeOrt=?, festadressenNummer=? "
//    //					+"WHERE "
//    //					+"mandant=? AND ident=? AND db_version=? ";
//    //
//    //			PreparedStatement lPStm=verbindung.prepareStatement(lSql);
//    //			fuellePreparedStatementKomplett(lPStm,1,pEcl); 
//    //			lPStm.setInt(anzfelder+1, pEcl.mandant);
//    //			lPStm.setInt(anzfelder+2, pEcl.ident);
//    //			lPStm.setLong(anzfelder+3, pEcl.db_version-1);
//    //
//    //			int ergebnis=lPStm.executeUpdate();
//    //			lPStm.close();
//    //			if (ergebnis==0){return (CaFehler.pfXyWurdeVonAnderemBenutzerVeraendert);}
//    //		} catch (Exception e1){
//    //			CaBug.drucke("DbNachrichtAnhang.update 001");
//    //			System.err.println(" "+e1.getMessage());
//    //			return (-1);
//    //		}
//    //
//    //		return (1);
//    //	}
//    //	
//    //	/**Return-Werte:
//    //	 * pfXyWurdeVonAnderemBenutzerVeraendert
//    //	 * -1 => undefinierter Fehler
//    //	 * 1 => Löschen erfolgreich
//    //	 */
//    //	public int delete(int pIdent){
//    //		try {
//    //			
//    //		String sql="DELETE FROM "+dbBundle.getSchemaAllgemein()+"tbl_nachrichtAnhang WHERE ident=? AND mandant=? ";
//    //
//    //		PreparedStatement pstm1=verbindung.prepareStatement(sql);
//    //		pstm1.setInt(1, pIdent);
//    //		pstm1.setInt(2, 0 /*dbBundle.clGlobalVar.mandant*/);
//    //
//    //		int ergebnis1=pstm1.executeUpdate();
//    //		pstm1.close();
//    //		if (ergebnis1==0){
//    //			return (CaFehler.pfXyWurdeVonAnderemBenutzerVeraendert);}
//    //		} catch (Exception e1){
//    //			CaBug.drucke("DbNachrichtAnhang.delete 001");
//    //			System.err.println(" "+e1.getMessage());
//    //			return (-1);
//    //		}
//    //
//    //		return (1);
//    //	}

}
