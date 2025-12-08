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
import de.meetingapps.meetingportal.meetComEntities.EclIpTracking;

public class DbIpTracking {
    
    private int logDrucken=10;

    private Connection verbindung = null;
    private DbBundle dbBundle = null;

    public EclIpTracking ergebnisArray[] = null;

    /*************************Initialisierung***************************/
    public DbIpTracking(DbBundle pDbBundle) {
        /* Verbindung in lokale Daten eintragen*/
        if (pDbBundle == null) {
            CaBug.drucke("DbIpTracking.init 001 - dbBundle nicht initialisiert");
            return;
        }
        if (pDbBundle.dbBasis == null) {
            CaBug.drucke("DbIpTracking.init 002 - dbBasis nicht initialisiert");
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
    public EclIpTracking ergebnisPosition(int pN) {
        if (ergebnisArray == null) {
            CaBug.drucke("DbIpTracking.ergebnisPosition 001");
            return null;
        }
        if (pN < 0) {
            CaBug.drucke("DbIpTracking.ergebnisPosition 002");
            return null;
        }
        if (pN >= ergebnisArray.length) {
            CaBug.drucke("DbIpTracking.ergebnisPosition 003");
            return null;
        }
        return ergebnisArray[pN];
    }

    /**************************deleteAll Löschen aller Datensätze  eines Mandanten****************/
    public int deleteAll() {
        return dbBundle.dbLowLevel
                .deleteMandant("DELETE FROM " + dbBundle.getSchemaMandant() + "tbl_iptracking where mandant=?;");
    }

    /*******Checken, ob table überhaupt vorhanden ist***************************/
    public boolean checkTableVorhanden() {
        DbLowLevel lDbLowLevel = new DbLowLevel(dbBundle);
        return lDbLowLevel.checkTableVorhanden("SELECT * from tbl_iptracking WHERE mandant=0; ");
    }

    /************Neuanlegen Table******************************/
    public int createTable() {
        int rc = 0;
        DbLowLevel lDbLowLevel = new DbLowLevel(dbBundle);
        rc = lDbLowLevel.createTable("CREATE TABLE " + dbBundle.getSchemaMandant() + "tbl_iptracking ( "
                + "`mandant` int(11) NOT NULL, " + "`ident`  bigint(20)  NOT NULL AUTO_INCREMENT, "
                + "`ipXForwardedFor` varchar(200) DEFAULT NULL, " + "`ipProxyClientIP` varchar(200) DEFAULT NULL, "
                + "`ipWLProxyClientIP` varchar(200) DEFAULT NULL, "
                + "`ipHTTPXFORWARDEDFOR` varchar(200) DEFAULT NULL, " + "`ipRemoteAddr` varchar(200) DEFAULT NULL, "
                + "`loginKennung` varchar(200) DEFAULT NULL, " + "`datumZeit` char(19) DEFAULT NULL, "
                + "PRIMARY KEY (`ident`) " + ")  ");
        CaBug.druckeLog("DbIpTracking create table rc=" + rc, logDrucken, 10);
        return rc;
    }

    /********** dekodiert die aktuelle Position aus ergebnis in EclAktienregisterEintrag und gibt dieses zurück******/
    EclIpTracking decodeErgebnis(ResultSet pErgebnis) {

        EclIpTracking lIptracking = new EclIpTracking();

        try {
            lIptracking.mandant = pErgebnis.getInt("ipt.mandant");
            lIptracking.ident = pErgebnis.getInt("ipt.ident");
            lIptracking.ipXForwardedFor = pErgebnis.getString("ipt.ipXForwardedFor");
            lIptracking.ipProxyClientIP = pErgebnis.getString("ipt.ipProxyClientIP");
            lIptracking.ipWLProxyClientIP = pErgebnis.getString("ipt.ipWLProxyClientIP");
            lIptracking.ipHTTPXFORWARDEDFOR = pErgebnis.getString("ipt.ipHTTPXFORWARDEDFOR");
            lIptracking.ipRemoteAddr = pErgebnis.getString("ipt.ipRemoteAddr");
            lIptracking.loginKennung = pErgebnis.getString("ipt.loginKennung");
            lIptracking.datumZeit = pErgebnis.getString("ipt.datumZeit");

        } catch (Exception e) {
            CaBug.drucke("DbIpTracking.decodeErgebnis 001");
            System.err.println(" " + e.getMessage());
        }

        return lIptracking;
    }

    /********************* Fuellen Prepared Statement mit allen Feldern, beginnend bei offset.*****************
     * Kann sowohl für Insert, als auch für update verwendet werden.
     * 
     * offset= Startposition des ersten Feldes (also z.B. 1)
     */
    //	private int anzfelder=9; /*Anpassen auf Anzahl der Felder pro Datensatz  ident nicht mit enthalten!*/
    void fuellePreparedStatementKomplett(PreparedStatement pPStm, int pOffset, EclIpTracking pIpTracking) {

        //		int startOffset=pOffset; /*Nur erforderlich zum Überprüfen von Programmierfehlern - Feldanzahl*/

        try {
            pPStm.setInt(pOffset, pIpTracking.mandant);
            pOffset++;
            //			pOffset++; //Ident nicht setzen - Autoincrement

            if (pIpTracking.ipXForwardedFor == null) {
                pIpTracking.ipXForwardedFor = "";
            }
            pPStm.setString(pOffset, pIpTracking.ipXForwardedFor);
            pOffset++;

            if (pIpTracking.ipProxyClientIP == null) {
                pIpTracking.ipProxyClientIP = "";
            }
            pPStm.setString(pOffset, pIpTracking.ipProxyClientIP);
            pOffset++;

            if (pIpTracking.ipWLProxyClientIP == null) {
                pIpTracking.ipWLProxyClientIP = "";
            }
            pPStm.setString(pOffset, pIpTracking.ipWLProxyClientIP);
            pOffset++;

            if (pIpTracking.ipHTTPXFORWARDEDFOR == null) {
                pIpTracking.ipHTTPXFORWARDEDFOR = "";
            }
            pPStm.setString(pOffset, pIpTracking.ipHTTPXFORWARDEDFOR);
            pOffset++;

            if (pIpTracking.ipRemoteAddr == null) {
                pIpTracking.ipRemoteAddr = "";
            }
            pPStm.setString(pOffset, pIpTracking.ipRemoteAddr);
            pOffset++;

            pPStm.setString(pOffset, pIpTracking.loginKennung);
            pOffset++;
            pPStm.setString(pOffset, pIpTracking.datumZeit);
            pOffset++;

            //			if (pOffset-startOffset!=anzfelder){ 
            //				/*Nur wg. Überprüfung auf Programmierfehler - alle Felder berücksichtigt?*/
            //				CaBug.drucke("DbIpTracking.fuellePreparedStatementKomplett 002");
            //			}

        } catch (SQLException e) {
            CaBug.drucke("DbIpTracking.fuellePreparedStatementKomplett 001");
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
    public int insert(EclIpTracking pVerarbeitungsLauf) {

        int erg = 0;

        try {
            /*Felder Neuanlage füllen*/
            String lSql = "INSERT INTO " + dbBundle.getSchemaMandant() + "tbl_iptracking " + "(" + "mandant, "
                    + "ipXForwardedFor, ipProxyClientIP, ipWLProxyClientIP, ipHTTPXFORWARDEDFOR, ipRemoteAddr, "
                    + "loginKennung, datumZeit  " + ")" + "VALUES (" + "?, " + "?, ?, ?, ?, ?, " + "?, ? " + ")";
            PreparedStatement lPStm = verbindung.prepareStatement(lSql, ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
            fuellePreparedStatementKomplett(lPStm, 1, pVerarbeitungsLauf);
            erg = lPStm.executeUpdate();
            lPStm.close();
        } catch (Exception e2) {
            CaBug.drucke("DbIpTracking.insert 001");
            System.err.println(" " + e2.getMessage());
        }

        if (erg == 0) {/*Fehler beim Einfügen - d.h. z.B. primaryKey bereits vorhanden*/
            return (-1);
        }

        return (1);
    }

    public int updateMandant() {
        return dbBundle.dbLowLevel.rawUpdateMandant(dbBundle.getSchemaMandant() + "tbl_iptracking");
    }

    //	/**Lese alle Verarbeitungsläufe
    //	 * Falls pMandant>0 dann nur die für diesen Mandanten
    //	 * */
    //	public int readAll(int pMandant) {
    //		int anzInArray=0;
    //		PreparedStatement lPStm=null;
    //		
    //		try {
    //			String lSql="SELECT * from "+dbBundle.getSchemaMandant()+"tbl_iptracking vl ";
    //			if (pMandant!=0){
    //				lSql=lSql+" WHERE mandant=? ";
    //			}
    //			lSql=lSql+" ORDER BY ident";
    //			lPStm=verbindung.prepareStatement(lSql,ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
    //			if (pMandant!=0){
    //				lPStm.setInt(1, pMandant);
    //			}
    //
    //			ResultSet lErgebnis=lPStm.executeQuery();
    //			lErgebnis.last();
    //			anzInArray=lErgebnis.getRow();
    //			lErgebnis.beforeFirst();
    //
    //			ergebnisArray=new EclIpTracking[anzInArray];
    //
    //			int i=0;
    //			while (lErgebnis.next()==true){
    //				ergebnisArray[i]=this.decodeErgebnis(lErgebnis);
    //				i++;
    //			}
    //			lErgebnis.close();
    //			lPStm.close();
    //				
    //		} catch (Exception e){
    //			CaBug.drucke("DbIpTracking.readAll_technisch 003");
    //			System.err.println(" "+e.getMessage());
    //			return (-1);
    //		}
    //		return (anzInArray);
    //	}
    //
    //	
    //	/**Einlesen eines bestimmten Laufs mit  ident*/
    //	public int read(int ident) {
    //		int anzInArray=0;
    //		PreparedStatement lPStm=null;
    //		
    //		try {
    //			String lSql="SELECT * from "+dbBundle.getSchemaMandant()+"tbl_iptracking vl WHERE ident=?;";
    //			lPStm=verbindung.prepareStatement(lSql,ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
    //			lPStm.setInt(1, ident);
    //
    //			ResultSet lErgebnis=lPStm.executeQuery();
    //			lErgebnis.last();
    //			anzInArray=lErgebnis.getRow();
    //			lErgebnis.beforeFirst();
    //			
    //			ergebnisArray=new EclIpTracking[anzInArray];
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
    //			CaBug.drucke("DbIpTracking.read 003");
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
    //	public int update(EclIpTracking pVerarbeitungsLauf){
    //		
    //		try {
    //			String lSql="UPDATE "+dbBundle.getSchemaMandant()+"tbl_iptracking SET "
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
    //			CaBug.drucke("DbIpTracking.update 001");
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
    //		String sql="DELETE FROM "+dbBundle.getSchemaMandant()+"tbl_iptracking WHERE ident=? ";
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
