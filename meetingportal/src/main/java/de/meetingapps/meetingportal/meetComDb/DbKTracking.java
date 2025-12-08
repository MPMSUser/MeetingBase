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
import de.meetingapps.meetingportal.meetComAllg.CaDatumZeit;
import de.meetingapps.meetingportal.meetComEntities.EclKTracking;

public class DbKTracking {

    private int logDrucken=10;
    
    private Connection verbindung = null;
    private DbBundle dbBundle = null;

    public EclKTracking ergebnisArray[] = null;

    /*************************Initialisierung***************************/
    public DbKTracking(DbBundle pDbBundle) {
        /* Verbindung in lokale Daten eintragen*/
        if (pDbBundle == null) {
            CaBug.drucke("DbKTracking.init 001 - dbBundle nicht initialisiert");
            return;
        }
        if (pDbBundle.dbBasis == null) {
            CaBug.drucke("DbKTracking.init 002 - dbBasis nicht initialisiert");
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
    public EclKTracking ergebnisPosition(int pN) {
        if (ergebnisArray == null) {
            CaBug.drucke("DbKTracking.ergebnisPosition 001");
            return null;
        }
        if (pN < 0) {
            CaBug.drucke("DbKTracking.ergebnisPosition 002");
            return null;
        }
        if (pN >= ergebnisArray.length) {
            CaBug.drucke("DbKTracking.ergebnisPosition 003");
            return null;
        }
        return ergebnisArray[pN];
    }

    /**************************deleteAll Löschen aller Datensätze  eines Mandanten****************/
    public int deleteAll() {
        return dbBundle.dbLowLevel
                .deleteMandant("DELETE FROM " + dbBundle.getSchemaMandant() + "tbl_ktracking where mandant=?;");
    }

    /*******Checken, ob table überhaupt vorhanden ist***************************/
    public boolean checkTableVorhanden() {
        DbLowLevel lDbLowLevel = new DbLowLevel(dbBundle);
        return lDbLowLevel.checkTableVorhanden("SELECT * from tbl_ktracking WHERE mandant=0; ");
    }

    /************Neuanlegen Table******************************/
    public int createTable() {
        int rc = 0;
        DbLowLevel lDbLowLevel = new DbLowLevel(dbBundle);
        rc = lDbLowLevel.createTable("CREATE TABLE " + dbBundle.getSchemaMandant() + "tbl_ktracking ( "
                + "`mandant` int(11) NOT NULL, " + "`ident`  bigint(20)  NOT NULL AUTO_INCREMENT, "
                + "`funktion` int(11) DEFAULT NULL, " + "`loginKennung` varchar(200) DEFAULT NULL, "
                + "`datumZeit` char(19) DEFAULT NULL, " + "PRIMARY KEY (`ident`) " + ")  ");
        CaBug.druckeLog("DbKTracking create table rc=" + rc, logDrucken, 10);
        return rc;
    }

    /********** dekodiert die aktuelle Position aus ergebnis in EclAktienregisterEintrag und gibt dieses zurück******/
    EclKTracking decodeErgebnis(ResultSet pErgebnis) {

        EclKTracking lIptracking = new EclKTracking();

        try {
            lIptracking.mandant = pErgebnis.getInt("kt.mandant");
            lIptracking.ident = pErgebnis.getInt("kt.ident");
            lIptracking.funktion = pErgebnis.getInt("kt.funktion");
            lIptracking.loginKennung = pErgebnis.getString("kt.loginKennung");
            lIptracking.datumZeit = pErgebnis.getString("kt.datumZeit");

        } catch (Exception e) {
            CaBug.drucke("DbKTracking.decodeErgebnis 001");
            System.err.println(" " + e.getMessage());
        }

        return lIptracking;
    }

    /********************* Fuellen Prepared Statement mit allen Feldern, beginnend bei offset.*****************
     * Kann sowohl für Insert, als auch für update verwendet werden.
     * 
     * offset= Startposition des ersten Feldes (also z.B. 1)
     */
    //	private int anzfelder=5; /*Anpassen auf Anzahl der Felder pro Datensatz  ident nicht mit enthalten!*/
    void fuellePreparedStatementKomplett(PreparedStatement pPStm, int pOffset, EclKTracking pIpTracking) {

        //		int startOffset=pOffset; /*Nur erforderlich zum Überprüfen von Programmierfehlern - Feldanzahl*/

        try {
            pPStm.setInt(pOffset, pIpTracking.mandant);
            pOffset++;
            //			pOffset++; //Ident nicht setzen - Autoincrement

            pPStm.setInt(pOffset, pIpTracking.funktion);
            pOffset++;
            pPStm.setString(pOffset, pIpTracking.loginKennung);
            pOffset++;
            pPStm.setString(pOffset, pIpTracking.datumZeit);
            pOffset++;

            //			if (pOffset-startOffset!=anzfelder){ 
            //				/*Nur wg. Überprüfung auf Programmierfehler - alle Felder berücksichtigt?*/
            //				CaBug.drucke("DbKTracking.fuellePreparedStatementKomplett 002");
            //			}

        } catch (SQLException e) {
            CaBug.drucke("DbKTracking.fuellePreparedStatementKomplett 001");
            e.printStackTrace();
        }

    }

    /**Insert
     * 
     * Feld mandant wird von dieser Funktion nicht selbstständig belegt.
     * 
     * Returnwert:
     * =1 => Insert erfolgreich
     * ansonsten: Fehler
     */
    public int insert(EclKTracking pVerarbeitungsLauf) {

        int erg = 0;

        try {
            /*Felder Neuanlage füllen*/
            String lSql = "INSERT INTO " + dbBundle.getSchemaMandant() + "tbl_ktracking " + "(" + "mandant, funktion, "
                    + "loginKennung, datumZeit  " + ")" + "VALUES (" + "?, ?, " + "?, ? " + ")";
            PreparedStatement lPStm = verbindung.prepareStatement(lSql, ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
            fuellePreparedStatementKomplett(lPStm, 1, pVerarbeitungsLauf);
            erg = lPStm.executeUpdate();
            lPStm.close();
        } catch (Exception e2) {
            CaBug.drucke("DbKTracking.insert 001");
            System.err.println(" " + e2.getMessage());
        }

        if (erg == 0) {/*Fehler beim Einfügen - d.h. z.B. primaryKey bereits vorhanden*/
            return (-1);
        }

        return (1);
    }

    /**Liest alle Kennungen, die am Tag die Funktion ausgelöst haben.
     * Tag="" => an allen Tagen.
     * Ansonsten Tag="TT.MM.JJJJ"
     * Gefüllt wird ergebnisarray, aber nur das Feld loginKennung
     * 
     * Sortiert nach Kennung
     * */
    public int readAllDistinct(int pFunktion, String pTag) {
        int anzInArray = 0;
        PreparedStatement lPStm = null;

        try {
            String lSql = "SELECT DISTINCT kt.loginKennung FROM " + dbBundle.getSchemaMandant() + "tbl_ktracking kt "
                    + " WHERE kt.mandant=? AND kt.funktion=? ";
            if (!pTag.isEmpty()) {
                lSql = lSql + " AND kt.datumZeit LIKE ? ";
            }
            lSql = lSql + " ORDER BY kt.loginKennung ";
            lPStm = verbindung.prepareStatement(lSql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            lPStm.setInt(1, dbBundle.clGlobalVar.mandant);
            lPStm.setInt(2, pFunktion);
            if (!pTag.isEmpty()) {
                String hDatum = CaDatumZeit.DatumStringFuerDatenbank(pTag);
                lPStm.setString(3, hDatum + "%");
            }
            ResultSet lErgebnis = lPStm.executeQuery();
            lErgebnis.last();
            anzInArray = lErgebnis.getRow();
            lErgebnis.beforeFirst();

            ergebnisArray = new EclKTracking[anzInArray];

            int i = 0;
            while (lErgebnis.next() == true) {
                //				ergebnisArray[i]=this.decodeErgebnis(lErgebnis);
                ergebnisArray[i] = new EclKTracking();
                ergebnisArray[i].loginKennung = lErgebnis.getString("kt.loginKennung");
                i++;
            }
            lErgebnis.close();
            lPStm.close();

        } catch (Exception e) {
            CaBug.drucke("DbKTracking.readAllDistinct 003");
            System.err.println(" " + e.getMessage());
            return (-1);
        }
        return (anzInArray);
    }

    /**Liest alle Kennungen, die am Tag die Funktion ausgelöst haben.
     * Tag="" => an allen Tagen.
     * Ansonsten Tag="TT.MM.JJJJ"
     * Gefüllt wird ergebnisarray, d.h. Kennungen können mehrfach vorhanden sein, wenn mehrfach geklickt.
     * 
     * Sortiert nach Kennung
     * */
    public int readAll(int pFunktion, String pTag) {
        int anzInArray = 0;
        PreparedStatement lPStm = null;

        try {
            String lSql = "SELECT * FROM " + dbBundle.getSchemaMandant() + "tbl_ktracking kt "
                    + " WHERE kt.mandant=? AND kt.funktion=? ";
            if (!pTag.isEmpty()) {
                lSql = lSql + " AND kt.datumZeit LIKE ? ";
            }
            lSql = lSql + " ORDER BY kt.loginKennung ";
            lPStm = verbindung.prepareStatement(lSql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            lPStm.setInt(1, dbBundle.clGlobalVar.mandant);
            lPStm.setInt(2, pFunktion);
            if (!pTag.isEmpty()) {
                String hDatum = CaDatumZeit.DatumStringFuerDatenbank(pTag);
                lPStm.setString(3, hDatum + "%");
            }
            ResultSet lErgebnis = lPStm.executeQuery();
            lErgebnis.last();
            anzInArray = lErgebnis.getRow();
            lErgebnis.beforeFirst();

            ergebnisArray = new EclKTracking[anzInArray];

            int i = 0;
            while (lErgebnis.next() == true) {
                ergebnisArray[i] = this.decodeErgebnis(lErgebnis);
                i++;
            }
            lErgebnis.close();
            lPStm.close();

        } catch (Exception e) {
            CaBug.drucke("DbKTracking.readAll 003");
            System.err.println(" " + e.getMessage());
            return (-1);
        }
        return (anzInArray);
    }

    
    public int updateMandant() {
        return dbBundle.dbLowLevel.rawUpdateMandant(dbBundle.getSchemaMandant() + "tbl_ktracking");
    }

    //
    //	
    //	/**Einlesen eines bestimmten Laufs mit  ident*/
    //	public int read(int ident) {
    //		int anzInArray=0;
    //		PreparedStatement lPStm=null;
    //		
    //		try {
    //			String lSql="SELECT * from "+dbBundle.getSchemaMandant()+"tbl_ktracking vl WHERE ident=?;";
    //			lPStm=verbindung.prepareStatement(lSql,ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
    //			lPStm.setInt(1, ident);
    //
    //			ResultSet lErgebnis=lPStm.executeQuery();
    //			lErgebnis.last();
    //			anzInArray=lErgebnis.getRow();
    //			lErgebnis.beforeFirst();
    //			
    //			ergebnisArray=new EclKTracking[anzInArray];
    //
    //			int i=0;
    //			while (lErgebnis.next()==true){
    //				ergebnisArray[i]=this.decodeErgebnis(lErgebnis);
    //				i++;
    //			}
    //			
    //			lErgebnis.close();
    //			lPStm.close();
    //				
    //		} catch (Exception e){
    //			CaBug.drucke("DbKTracking.read 003");
    //			System.err.println(" "+e.getMessage());
    //			return (-1);
    //		}
    //		return (anzInArray);
    //	}
    //
    //	/**Update. Versionsnummer wird um 1 hochgezählt
    //	 * 
    // 	 * Feld mandant wird von dieser Funktion nicht verändert.
    //	 * 
    //	 * Zum Sicherstellen der Multiuserfähigkeit muß in jedem Fall der rc CaFehler.pfXyWurdeVonAnderemBenutzerVeraendert
    //	 * nach Aufruf dieser Funktion abgefangen werden.
    //	 * Ansonsten: rc=1 => ok, ansonsten Fehler
    //	 */
    //	public int update(EclKTracking pVerarbeitungsLauf){
    //		
    //		try {
    //			String lSql="UPDATE "+dbBundle.getSchemaMandant()+"tbl_ktracking SET "
    //					+ "mandant=?, ident=?, verarbeitungsArt=?, statusDesLaufs=?, verarbeitungsZeit=?, "
    //					+ "argument0=?, argument1=?, argument2=?, argument3=?, argument4=?, "
    //					+ "argument5=?, argument6=?, argument7=?, argument8=?, argument9=? "
    //					+"WHERE "
    //					+"ident=? ";
    //
    //			PreparedStatement lPStm=verbindung.prepareStatement(lSql);
    //			fuellePreparedStatementKomplett(lPStm,1,pVerarbeitungsLauf); 
    //			lPStm.setInt(anzfelder+1, pVerarbeitungsLauf.ident);
    //
    //			int ergebnis=lPStm.executeUpdate();
    //			lPStm.close();
    //			if (ergebnis==0){return (CaFehler.pfXyWurdeVonAnderemBenutzerVeraendert);}
    //		} catch (Exception e1){
    //			CaBug.drucke("DbKTracking.update 001");
    //			System.err.println(" "+e1.getMessage());
    //			return (CaFehler.pfdXyBereitsVorhanden);
    //		}
    //
    //		return (1);
    //	}
    //	
    //	public int delete(int pIdent){
    //		
    //		try {
    //			
    //		String sql="DELETE FROM "+dbBundle.getSchemaMandant()+"tbl_ktracking WHERE ident=? ";
    //
    //		PreparedStatement pstm1=verbindung.prepareStatement(sql);
    //		pstm1.setInt(1, pIdent);
    //
    //		int ergebnis1=pstm1.executeUpdate();
    //		pstm1.close();
    //		if (ergebnis1==0){
    //			return (CaFehler.pfXyWurdeVonAnderemBenutzerVeraendert);}
    //		} catch (Exception e1){
    //			CaBug.drucke("DbAbstimmungen.delete 001");
    //			System.err.println(" "+e1.getMessage());
    //			return (CaFehler.pfdXyBereitsVorhanden);
    //		}
    //
    //		return (1);
    //	}

}
