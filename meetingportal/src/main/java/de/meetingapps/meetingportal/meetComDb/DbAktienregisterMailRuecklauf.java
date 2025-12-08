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
import de.meetingapps.meetingportal.meetComEntities.EclAktienregisterMailRuecklauf;

public class DbAktienregisterMailRuecklauf {

    private Connection verbindung = null;
    private DbBundle dbBundle = null;

    public EclAktienregisterMailRuecklauf ergebnisArray[] = null;

    /*************************Initialisierung***************************/
    public DbAktienregisterMailRuecklauf(DbBundle pDbBundle) {
        /* Verbindung in lokale Daten eintragen*/
        if (pDbBundle == null) {
            CaBug.drucke("DbAktienregisterMailRuecklauf.init 001 - dbBundle nicht initialisiert");
            return;
        }
        if (pDbBundle.dbBasis == null) {
            CaBug.drucke("DbAktienregisterMailRuecklauf.init 002 - dbBasis nicht initialisiert");
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
    public EclAktienregisterMailRuecklauf ergebnisPosition(int pN) {
        if (ergebnisArray == null) {
            CaBug.drucke("DbAktienregisterMailRuecklauf.ergebnisPosition 001");
            return null;
        }
        if (pN < 0) {
            CaBug.drucke("DbAktienregisterMailRuecklauf.ergebnisPosition 002");
            return null;
        }
        if (pN >= ergebnisArray.length) {
            CaBug.drucke("DbAktienregisterMailRuecklauf.ergebnisPosition 003");
            return null;
        }
        return ergebnisArray[pN];
    }

    /**************************deleteAll Löschen aller Datensätze  eines Mandanten****************/
    public int deleteAll() {
        return dbBundle.dbLowLevel.deleteMandant(
                "DELETE FROM " + dbBundle.getSchemaMandant() + "tbl_aktienregisterMailRuecklauf where mandant=?;");
    }

    /*******Checken, ob table überhaupt vorhanden ist***************************/
    public boolean checkTableVorhanden() {
        DbLowLevel lDbLowLevel = new DbLowLevel(dbBundle);
        return lDbLowLevel.checkTableVorhanden("SELECT * from tbl_aktienregisterMailRuecklauf WHERE mandant=0; ");
    }

    /************Neuanlegen Table******************************/
    public int createTable() {
        int rc = 0;
        DbLowLevel lDbLowLevel = new DbLowLevel(dbBundle);
        rc = lDbLowLevel
                .createTable("CREATE TABLE " + dbBundle.getSchemaMandant() + "tbl_aktienregisterMailRuecklauf ( "
                        + "`mandant` int(11) NOT NULL, " + "`ident`  bigint(20)  NOT NULL AUTO_INCREMENT, "
                        + "`aktionaersnummer` varchar(20) DEFAULT NULL, " + "`mailadresse` varchar(150) DEFAULT NULL, "
                        + "`passwort` varchar(50) DEFAULT NULL, " + "PRIMARY KEY (`ident`) " + ")  ");
        System.out.println("DbAktienregisterMailRuecklauf create table rc=" + rc);
        return rc;
    }

    /********** dekodiert die aktuelle Position aus ergebnis in EclAktienregisterEintrag und gibt dieses zurück******/
    EclAktienregisterMailRuecklauf decodeErgebnis(ResultSet pErgebnis) {

        EclAktienregisterMailRuecklauf lAktienregisterMailRuecklauf = new EclAktienregisterMailRuecklauf();

        try {
            lAktienregisterMailRuecklauf.mandant = pErgebnis.getInt("armr.mandant");
            lAktienregisterMailRuecklauf.ident = pErgebnis.getInt("armr.ident");
            lAktienregisterMailRuecklauf.aktionaersnummer = pErgebnis.getString("armr.aktionaersnummer");
            lAktienregisterMailRuecklauf.mailadresse = pErgebnis.getString("armr.mailadresse");
            lAktienregisterMailRuecklauf.passwort = pErgebnis.getString("armr.passwort");

        } catch (Exception e) {
            CaBug.drucke("DbAktienregisterMailRuecklauf.decodeErgebnis 001");
            System.err.println(" " + e.getMessage());
        }

        return lAktienregisterMailRuecklauf;
    }

    /********************* Fuellen Prepared Statement mit allen Feldern, beginnend bei offset.*****************
     * Kann sowohl für Insert, als auch für update verwendet werden.
     * 
     * offset= Startposition des ersten Feldes (also z.B. 1)
     */
    //	private int anzfelder=5; /*Anpassen auf Anzahl der Felder pro Datensatz  ident nicht mit enthalten!*/
    void fuellePreparedStatementKomplett(PreparedStatement pPStm, int pOffset,
            EclAktienregisterMailRuecklauf pAktiernegisterMailRuecklauf) {

        //		int startOffset=pOffset; /*Nur erforderlich zum Überprüfen von Programmierfehlern - Feldanzahl*/

        try {
            pPStm.setInt(pOffset, pAktiernegisterMailRuecklauf.mandant);
            pOffset++;
            //			pOffset++; //Ident nicht setzen - Autoincrement

            pPStm.setString(pOffset, pAktiernegisterMailRuecklauf.aktionaersnummer);
            pOffset++;
            pPStm.setString(pOffset, pAktiernegisterMailRuecklauf.mailadresse);
            pOffset++;
            pPStm.setString(pOffset, pAktiernegisterMailRuecklauf.passwort);
            pOffset++;

            //			if (pOffset-startOffset!=anzfelder){ 
            //				/*Nur wg. Überprüfung auf Programmierfehler - alle Felder berücksichtigt?*/
            //				CaBug.drucke("DbAktienregisterMailRuecklauf.fuellePreparedStatementKomplett 002");
            //			}

        } catch (SQLException e) {
            CaBug.drucke("DbAktienregisterMailRuecklauf.fuellePreparedStatementKomplett 001");
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
    public int insert(EclAktienregisterMailRuecklauf pVerarbeitungsLauf) {

        int erg = 0;

        try {
            /*Felder Neuanlage füllen*/
            String lSql = "INSERT INTO " + dbBundle.getSchemaMandant() + "tbl_aktienregisterMailRuecklauf " + "("
                    + "mandant, " + "aktionaersnummer, mailadresse, passwort " + ")" + "VALUES (" + "?, " + "?, ?, ?"
                    + ")";
            PreparedStatement lPStm = verbindung.prepareStatement(lSql, ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
            fuellePreparedStatementKomplett(lPStm, 1, pVerarbeitungsLauf);
            erg = lPStm.executeUpdate();
            lPStm.close();
        } catch (Exception e2) {
            CaBug.drucke("DbAktienregisterMailRuecklauf.insert 001");
            System.err.println(" " + e2.getMessage());
        }

        if (erg == 0) {/*Fehler beim Einfügen - d.h. z.B. primaryKey bereits vorhanden*/
            return (-1);
        }

        return (1);
    }

    public int updateMandant() {
        return dbBundle.dbLowLevel.rawUpdateMandant(dbBundle.getSchemaMandant() + "tbl_aktienregisterMailRuecklauf");
    }

    //	/**Einlesen eines bestimmten Laufs mit  ident*/
    //	public int readZuident(long pIdent) {
    //		int anzInArray=0;
    //		PreparedStatement lPStm=null;
    //		
    //		try {
    //			String lSql="SELECT * from "+dbBundle.getSchemaMandant()+"tbl_aktienregisterMailRuecklauf armr WHERE ident=?;";
    //			lPStm=verbindung.prepareStatement(lSql,ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
    //			lPStm.setLong(1, pIdent);
    //
    //			ResultSet lErgebnis=lPStm.executeQuery();
    //			lErgebnis.last();
    //			anzInArray=lErgebnis.getRow();
    //			lErgebnis.beforeFirst();
    //			
    //			ergebnisArray=new EclAktienregisterMailRuecklauf[anzInArray];
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
    //			CaBug.drucke("DbAktienregisterMailRuecklauf.readZuident 003");
    //			System.err.println(" "+e.getMessage());
    //			return (-1);
    //		}
    //		return (anzInArray);
    //	}
    //
    //	
    //	
    //	/**Einlesen Vorgänge
    //	 * pStatus 1 bis 3 wie in Table. 4 = Mitgliedsnummer, steht dann im String
    //	 * notqs=nur nicht Qualitätsgesichert*/
    //	public int readVorgaengeZuident(int pStatus, boolean pNotQS, String pMitgliedsNr) {
    //		int anzInArray=0;
    //		PreparedStatement lPStm=null;
    //		
    //		try {
    //			String lSql="SELECT DISTINCT armr.dateinameBestaetigung, armr.zuAktionaersnummer, armr.pruefstatusErgebnis from "+
    //					dbBundle.getSchemaMandant()+"tbl_aktienregisterMailRuecklauf armr WHERE ";
    //			if (pStatus<4){
    //				lSql=lSql+" armr.pruefstatusErgebnis=? "; 
    //			}
    //			else{
    //				lSql=lSql+" armr.zuAktionaersnummer=? ";
    //			}
    //			if (pNotQS==true){
    //				lSql=lSql+" AND armr.wurdeQualitaetsgesichert=0 ";
    //			}
    //			
    //			lPStm=verbindung.prepareStatement(lSql,ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
    //			if (pStatus<4){
    //				lPStm.setInt(1, pStatus);
    //			}
    //			else{
    //				lPStm.setString(1, pMitgliedsNr);
    //			}
    //			
    //
    //			ResultSet lErgebnis=lPStm.executeQuery();
    //			lErgebnis.last();
    //			anzInArray=lErgebnis.getRow();
    //			lErgebnis.beforeFirst();
    //			
    //			ergebnisArray=new EclAktienregisterMailRuecklauf[anzInArray];
    //
    //			int i=0;
    //			while (lErgebnis.next()==true){
    //				EclAktienregisterMailRuecklauf lBestWorkflow=new EclAktienregisterMailRuecklauf();
    //				lBestWorkflow.dateinameBestaetigung=lErgebnis.getString("armr.dateinameBestaetigung");
    //				lBestWorkflow.zuAktionaersnummer=lErgebnis.getString("armr.zuAktionaersnummer");
    //				lBestWorkflow.pruefstatusErgebnis=lErgebnis.getInt("armr.pruefstatusErgebnis");
    //
    //				ergebnisArray[i]=lBestWorkflow;
    //				i++;
    //			}
    //			
    //			lErgebnis.close();
    //			lPStm.close();
    //				
    //		} catch (Exception e){
    //			CaBug.drucke("DbAktienregisterMailRuecklauf.readVorgaengeZuident 003");
    //			System.err.println(" "+e.getMessage());
    //			return (-1);
    //		}
    //		return (anzInArray);
    //	}
    //
    //	
    //	
    //	/**Einlesen eines bestimmten Laufs mit  ident*/
    //	public int readZuDatei(String pDateinameBestaetigung) {
    //		int anzInArray=0;
    //		PreparedStatement lPStm=null;
    //		
    //		try {
    //			String lSql="SELECT * from "+dbBundle.getSchemaMandant()+"tbl_aktienregisterMailRuecklauf armr WHERE armr.dateinameBestaetigung=?;";
    //			lPStm=verbindung.prepareStatement(lSql,ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
    //			lPStm.setString(1, pDateinameBestaetigung);
    //
    //			ResultSet lErgebnis=lPStm.executeQuery();
    //			lErgebnis.last();
    //			anzInArray=lErgebnis.getRow();
    //			lErgebnis.beforeFirst();
    //			
    //			ergebnisArray=new EclAktienregisterMailRuecklauf[anzInArray];
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
    //			CaBug.drucke("DbAktienregisterMailRuecklauf.read 003");
    //			System.err.println(" "+e.getMessage());
    //			return (-1);
    //		}
    //		return (anzInArray);
    //	}
    //
    //
    //	public int readZuDateiUndAktionaer(String pDateinameBestaetigung, String pAktionaersnummer) {
    //		int anzInArray=0;
    //		PreparedStatement lPStm=null;
    //		
    //		try {
    //			String lSql="SELECT * from "+dbBundle.getSchemaMandant()+"tbl_aktienregisterMailRuecklauf armr WHERE armr.dateinameBestaetigung=? and armr.zuAktionaersnummer=?;";
    //			lPStm=verbindung.prepareStatement(lSql,ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
    //			lPStm.setString(1, pDateinameBestaetigung);
    //			lPStm.setString(2, pAktionaersnummer);
    //
    //			ResultSet lErgebnis=lPStm.executeQuery();
    //			lErgebnis.last();
    //			anzInArray=lErgebnis.getRow();
    //			lErgebnis.beforeFirst();
    //			
    //			ergebnisArray=new EclAktienregisterMailRuecklauf[anzInArray];
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
    //			CaBug.drucke("DbAktienregisterMailRuecklauf.readZuDateiUndAktionaer 003");
    //			System.err.println(" "+e.getMessage());
    //			return (-1);
    //		}
    //		return (anzInArray);
    //	}
    //
    //	public int readZuAktionaer(String pAktionaersnummer) {
    //		int anzInArray=0;
    //		PreparedStatement lPStm=null;
    //		
    //		try {
    //			String lSql="SELECT * from "+dbBundle.getSchemaMandant()+"tbl_aktienregisterMailRuecklauf armr WHERE armr.zuAktionaersnummer=?;";
    //			lPStm=verbindung.prepareStatement(lSql,ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
    //			lPStm.setString(1, pAktionaersnummer);
    //
    //			ResultSet lErgebnis=lPStm.executeQuery();
    //			lErgebnis.last();
    //			anzInArray=lErgebnis.getRow();
    //			lErgebnis.beforeFirst();
    //			
    //			ergebnisArray=new EclAktienregisterMailRuecklauf[anzInArray];
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
    //			CaBug.drucke("DbAktienregisterMailRuecklauf.readZuAktionaer 003");
    //			System.err.println(" "+e.getMessage());
    //			return (-1);
    //		}
    //		return (anzInArray);
    //	}
    //
    //	/**Einlesen eines bestimmten Laufs mit  inBeartungDurchUserNr*/
    //	public int readInBearbeitung(int pInBearbeitungDurchUserNr) {
    //		int anzInArray=0;
    //		PreparedStatement lPStm=null;
    //		
    //		try {
    //			String lSql="SELECT * from "+dbBundle.getSchemaMandant()+"tbl_aktienregisterMailRuecklauf armr WHERE inBearbeitungDurchUserNr=?;";
    //			lPStm=verbindung.prepareStatement(lSql,ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
    //			lPStm.setInt(1, pInBearbeitungDurchUserNr);
    //
    //			ResultSet lErgebnis=lPStm.executeQuery();
    //			lErgebnis.last();
    //			anzInArray=lErgebnis.getRow();
    //			lErgebnis.beforeFirst();
    //			
    //			ergebnisArray=new EclAktienregisterMailRuecklauf[anzInArray];
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
    //			CaBug.drucke("DbAktienregisterMailRuecklauf.readInBearbeitung 003");
    //			System.err.println(" "+e.getMessage());
    //			return (-1);
    //		}
    //		return (anzInArray);
    //	}
    //
    //	
    //	/**Bestimmen der Anzahl Dokumente mit PruefStatus*/
    //	public int readAnzahl(int pPruefStatusErgebnis){
    //		
    //		int anzInArray=0;
    //		PreparedStatement lPStm=null;
    //		
    //		try {
    //			String lSql="SELECT DISTINCT armr.dateinameBestaetigung from "+dbBundle.getSchemaMandant()+"tbl_aktienregisterMailRuecklauf armr WHERE pruefstatusErgebnis=?;";
    //			lPStm=verbindung.prepareStatement(lSql,ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
    //			lPStm.setInt(1, pPruefStatusErgebnis);
    //
    //			ResultSet lErgebnis=lPStm.executeQuery();
    //			lErgebnis.last();
    //			anzInArray=lErgebnis.getRow();
    //			lErgebnis.close();
    //			lPStm.close();
    //				
    //		} catch (Exception e){
    //			CaBug.drucke("DbAktienregisterMailRuecklauf.readAnzahl 003");
    //			System.err.println(" "+e.getMessage());
    //			return (-1);
    //		}
    //		return (anzInArray);
    //	}
    //
    //	/**Bestimmen der Anzahl Dokumente in Bearbeitung*/
    //	public int readAnzahlInBearbeitung(){
    //		
    //		int anzInArray=0;
    //		PreparedStatement lPStm=null;
    //		
    //		try {
    //			String lSql="SELECT DISTINCT armr.dateinameBestaetigung from "+dbBundle.getSchemaMandant()+"tbl_aktienregisterMailRuecklauf armr WHERE inBearbeitungDurchUserNr!=0;";
    //			lPStm=verbindung.prepareStatement(lSql,ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
    //
    //			ResultSet lErgebnis=lPStm.executeQuery();
    //			lErgebnis.last();
    //			anzInArray=lErgebnis.getRow();
    //			lErgebnis.close();
    //			lPStm.close();
    //				
    //		} catch (Exception e){
    //			CaBug.drucke("DbAktienregisterMailRuecklauf.readAnzahlInBearbeitung 003");
    //			System.err.println(" "+e.getMessage());
    //			return (-1);
    //		}
    //		return (anzInArray);
    //	}
    //
    //
    //
    //	
    //	
    ////	/**Lese alle Verarbeitungsläufe
    ////	 * Falls pMandant>0 dann nur die für diesen Mandanten
    ////	 * */
    ////	public int readAll(int pMandant) {
    ////		int anzInArray=0;
    ////		PreparedStatement lPStm=null;
    ////		
    ////		try {
    ////			String lSql="SELECT * from "+dbBundle.getSchemaMandant()+"tbl_aktienregisterMailRuecklauf vl ";
    ////			if (pMandant!=0){
    ////				lSql=lSql+" WHERE mandant=? ";
    ////			}
    ////			lSql=lSql+" ORDER BY ident";
    ////			lPStm=verbindung.prepareStatement(lSql,ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
    ////			if (pMandant!=0){
    ////				lPStm.setInt(1, pMandant);
    ////			}
    ////
    ////			ResultSet lErgebnis=lPStm.executeQuery();
    ////			lErgebnis.last();
    ////			anzInArray=lErgebnis.getRow();
    ////			lErgebnis.beforeFirst();
    ////
    ////			ergebnisArray=new EclAktienregisterMailRuecklauf[anzInArray];
    ////
    ////			int i=0;
    ////			while (lErgebnis.next()==true){
    ////				ergebnisArray[i]=this.decodeErgebnis(lErgebnis);
    ////				i++;
    ////			}
    ////			lErgebnis.close();
    ////			lPStm.close();
    ////				
    ////		} catch (Exception e){
    ////			CaBug.drucke("DbAktienregisterMailRuecklauf.readAll_technisch 003");
    ////			System.err.println(" "+e.getMessage());
    ////			return (-1);
    ////		}
    ////		return (anzInArray);
    ////	}
    ////
    ////	
    ////	/**Einlesen eines bestimmten Laufs mit  ident*/
    ////	public int read(int ident) {
    ////		int anzInArray=0;
    ////		PreparedStatement lPStm=null;
    ////		
    ////		try {
    ////			String lSql="SELECT * from "+dbBundle.getSchemaMandant()+"tbl_aktienregisterMailRuecklauf vl WHERE ident=?;";
    ////			lPStm=verbindung.prepareStatement(lSql,ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
    ////			lPStm.setInt(1, ident);
    ////
    ////			ResultSet lErgebnis=lPStm.executeQuery();
    ////			lErgebnis.last();
    ////			anzInArray=lErgebnis.getRow();
    ////			lErgebnis.beforeFirst();
    ////			
    ////			ergebnisArray=new EclAktienregisterMailRuecklauf[anzInArray];
    ////
    ////			int i=0;
    ////			while (lErgebnis.next()==true){
    ////				ergebnisArray[i]=this.decodeErgebnis(lErgebnis);
    ////				i++;
    ////			}
    ////			
    ////			lErgebnis.close();
    ////			lPStm.close();
    ////				
    ////		} catch (Exception e){
    ////			CaBug.drucke("DbAktienregisterMailRuecklauf.read 003");
    ////			System.err.println(" "+e.getMessage());
    ////			return (-1);
    ////		}
    ////		return (anzInArray);
    ////	}
    ////
    //	/**Update. Versionsnummer wird um 1 hochgezählt
    //	 * 
    // 	 * Feld mandant wird von dieser Funktion nicht verändert.
    //	 * 
    //	 * Zum Sicherstellen der Multiuserfähigkeit muß in jedem Fall der rc CaFehler.pfXyWurdeVonAnderemBenutzerVeraendert
    //	 * nach Aufruf dieser Funktion abgefangen werden.
    //	 * Ansonsten: rc=1 => ok, ansonsten Fehler
    //	 */
    //	public int update(EclAktienregisterMailRuecklauf pBestWorkflow){
    //		
    //		try {
    //			String lSql="UPDATE "+dbBundle.getSchemaMandant()+"tbl_aktienregisterMailRuecklauf SET "
    //					+ "mandant=?, "
    //					+ "dateinameBestaetigung=?, dateinameImportAm=?, zuAktionaersnummer=?, inBearbeitungDurchUserNr=?, pruefstatusErgebnis=?, pruefstatusVorgang=?, "
    //					+ "wurdeQualitaetsgesichert=?, kommentar=?, bearbeitungsZeit=?, bearbeitungsUser=?, bearbeitungsUserName=? "
    //					+"WHERE "
    //					+"ident=? ";
    //
    //			PreparedStatement lPStm=verbindung.prepareStatement(lSql);
    //			fuellePreparedStatementKomplett(lPStm,1,pBestWorkflow); 
    //			lPStm.setLong(anzfelder+1, pBestWorkflow.ident);
    //
    //			int ergebnis=lPStm.executeUpdate();
    //			lPStm.close();
    //			if (ergebnis==0){return (CaFehler.pfXyWurdeVonAnderemBenutzerVeraendert);}
    //		} catch (Exception e1){
    //			CaBug.drucke("DbAktienregisterMailRuecklauf.update 001");
    //			System.err.println(" "+e1.getMessage());
    //			return (CaFehler.pfdXyBereitsVorhanden);
    //		}
    //
    //		return (1);
    //	}
    //	
    //	
    //	/**0, wenn nichts mehr in Bearbeitung; ansonsten der Satz in bearebtigun ganz normal als Ergebnis*/
    //	public int reserviereFuerBasis(){
    //		
    //		int erg=0;
    //		int ergebnis;
    //		erg=dbBundle.dbBasis.getInterneIdentBestWorkflowBasis();
    //		
    //		try {
    //			String lSql="UPDATE "+dbBundle.getSchemaMandant()+"tbl_aktienregisterMailRuecklauf SET "
    //					+ "inBearbeitungDurchUserNr=? "
    //					+"WHERE "
    //					+"inBearbeitungDurchUserNr=0 AND pruefstatusErgebnis=0 LIMIT 1 ";
    //
    //			PreparedStatement lPStm=verbindung.prepareStatement(lSql);
    //			lPStm.setInt(1, erg);
    //
    //			ergebnis=lPStm.executeUpdate();
    //			lPStm.close();
    //			if (ergebnis==0){return (CaFehler.pfXyWurdeVonAnderemBenutzerVeraendert);}
    //		} catch (Exception e1){
    //			CaBug.drucke("DbAktienregisterMailRuecklauf.reserviereFuerBasis 001");
    //			System.err.println(" "+e1.getMessage());
    //			return (CaFehler.pfdXyBereitsVorhanden);
    //		}
    //
    //		if (ergebnis<1){return 0;}
    //		
    //		readInBearbeitung(erg);
    //		return (1);
    //	}
    //	
    //	/**0, wenn nichts mehr in Bearbeitung; ansonsten der Satz in bearebtigun ganz normal als Ergebnis*/
    //	public int reservierungAlleZuruecksetzen(){
    //		
    //		int erg=0;
    //		int ergebnis;
    //		erg=dbBundle.dbBasis.getInterneIdentBestWorkflowBasis();
    //		
    //		try {
    //			String lSql="UPDATE "+dbBundle.getSchemaMandant()+"tbl_aktienregisterMailRuecklauf SET "
    //					+ "inBearbeitungDurchUserNr=0 "
    //					+"WHERE "
    //					+"inBearbeitungDurchUserNr!=0";
    //
    //			PreparedStatement lPStm=verbindung.prepareStatement(lSql);
    //
    //			ergebnis=lPStm.executeUpdate();
    //			lPStm.close();
    //			if (ergebnis==0){return (CaFehler.pfXyWurdeVonAnderemBenutzerVeraendert);}
    //		} catch (Exception e1){
    //			CaBug.drucke("DbAktienregisterMailRuecklauf.reservierungAlleZuruecksetzen 001");
    //			System.err.println(" "+e1.getMessage());
    //			return (CaFehler.pfdXyBereitsVorhanden);
    //		}
    //
    //		return (1);
    //	}
    //
    //

    //	public int delete(int pIdent){
    //		
    //		try {
    //			
    //		String sql="DELETE FROM "+dbBundle.getSchemaMandant()+"tbl_aktienregisterMailRuecklauf WHERE ident=? ";
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
