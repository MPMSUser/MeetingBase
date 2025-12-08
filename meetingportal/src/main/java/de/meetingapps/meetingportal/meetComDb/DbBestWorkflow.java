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
import de.meetingapps.meetingportal.meetComEntities.EclBestWorkflow;

public class DbBestWorkflow {

    private int logDrucken=10;
    
    private Connection verbindung = null;
    private DbBundle dbBundle = null;

    public EclBestWorkflow ergebnisArray[] = null;

    /*************************Initialisierung***************************/
    public DbBestWorkflow(DbBundle pDbBundle) {
        /* Verbindung in lokale Daten eintragen*/
        if (pDbBundle == null) {
            CaBug.drucke("DbBestWorkflow.init 001 - dbBundle nicht initialisiert");
            return;
        }
        if (pDbBundle.dbBasis == null) {
            CaBug.drucke("DbBestWorkflow.init 002 - dbBasis nicht initialisiert");
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
    public EclBestWorkflow ergebnisPosition(int pN) {
        if (ergebnisArray == null) {
            CaBug.drucke("DbBestWorkflow.ergebnisPosition 001");
            return null;
        }
        if (pN < 0) {
            CaBug.drucke("DbBestWorkflow.ergebnisPosition 002");
            return null;
        }
        if (pN >= ergebnisArray.length) {
            CaBug.drucke("DbBestWorkflow.ergebnisPosition 003");
            return null;
        }
        return ergebnisArray[pN];
    }

    /**************************deleteAll Löschen aller Datensätze  eines Mandanten****************/
    public int deleteAll() {
        return dbBundle.dbLowLevel
                .deleteMandant("DELETE FROM " + dbBundle.getSchemaMandant() + "tbl_bestworkflow where mandant=?;");
    }

    /**************************setzt aktuelle Mandantennummer bei allen Datensätzen****************/
    public int updateMandant() {
        return dbBundle.dbLowLevel.rawUpdateMandant(dbBundle.getSchemaMandant() + "tbl_bestworkflow");
    }

    /*******Checken, ob table überhaupt vorhanden ist***************************/
    public boolean checkTableVorhanden() {
        DbLowLevel lDbLowLevel = new DbLowLevel(dbBundle);
        return lDbLowLevel.checkTableVorhanden("SELECT * from tbl_bestworkflow WHERE mandant=0; ");
    }

    /************Neuanlegen Table******************************/
    public int createTable() {
        int rc = 0;
        DbLowLevel lDbLowLevel = new DbLowLevel(dbBundle);
        rc = lDbLowLevel.createTable("CREATE TABLE " + dbBundle.getSchemaMandant() + "tbl_bestworkflow ( "
                + "`mandant` int(11) NOT NULL, " + "`ident`  bigint(20)  NOT NULL AUTO_INCREMENT, "
                + "`subverzeichnis` varchar(10) DEFAULT NULL, " + "`dateinameBestaetigung` varchar(100) DEFAULT NULL, "
                + "`dateinameImportAm` char(19) DEFAULT NULL, " + "`zuAktionaersnummer` varchar(20) DEFAULT NULL, "
                + "`origOderKopie` int(11) NOT NULL, " + "`vorlVollmachtIdent` int(11) NOT NULL, "
                + "`inBearbeitungDurchUserNr` int(11) NOT NULL, " + "`pruefstatusErgebnis` int(11) NOT NULL, "
                + "`pruefstatusVorgang` int(11) NOT NULL, " + "`wurdeQualitaetsgesichert` int(11) NOT NULL, "
                + "`kommentar` varchar(400) DEFAULT NULL, " + "`bearbeitungsZeit` char(19) DEFAULT NULL, "
                + "`bearbeitungsUser` int(11) NOT NULL, " + "`bearbeitungsUserName` varchar(40) DEFAULT NULL, "
                + "PRIMARY KEY (`ident`) " + ")  ");
        CaBug.druckeLog("DbBestWorkflow create table rc=" + rc, logDrucken, 10);
        return rc;
    }

    /********** dekodiert die aktuelle Position aus ergebnis in EclAktienregisterEintrag und gibt dieses zurück******/
    EclBestWorkflow decodeErgebnis(ResultSet pErgebnis) {

        EclBestWorkflow lBestWorkflow = new EclBestWorkflow();

        try {
            lBestWorkflow.mandant = pErgebnis.getInt("bwo.mandant");
            lBestWorkflow.ident = pErgebnis.getInt("bwo.ident");
            lBestWorkflow.subverzeichnis = pErgebnis.getString("bwo.subverzeichnis");
            lBestWorkflow.dateinameBestaetigung = pErgebnis.getString("bwo.dateinameBestaetigung");
            lBestWorkflow.dateinameImportAm = pErgebnis.getString("bwo.dateinameImportAm");
            lBestWorkflow.zuAktionaersnummer = pErgebnis.getString("bwo.zuAktionaersnummer");
            lBestWorkflow.origOderKopie = pErgebnis.getInt("bwo.origOderKopie");
            lBestWorkflow.vorlVollmachtIdent = pErgebnis.getInt("bwo.vorlVollmachtIdent");
            lBestWorkflow.inBearbeitungDurchUserNr = pErgebnis.getInt("bwo.inBearbeitungDurchUserNr");
            lBestWorkflow.pruefstatusErgebnis = pErgebnis.getInt("bwo.pruefstatusErgebnis");
            lBestWorkflow.pruefstatusVorgang = pErgebnis.getInt("bwo.pruefstatusVorgang");
            lBestWorkflow.wurdeQualitaetsgesichert = pErgebnis.getInt("bwo.wurdeQualitaetsgesichert");
            lBestWorkflow.kommentar = pErgebnis.getString("bwo.kommentar");
            lBestWorkflow.bearbeitungsZeit = pErgebnis.getString("bwo.bearbeitungsZeit");
            lBestWorkflow.bearbeitungsUser = pErgebnis.getInt("bwo.bearbeitungsUser");
            lBestWorkflow.bearbeitungsUserName = pErgebnis.getString("bwo.bearbeitungsUserName");

        } catch (Exception e) {
            CaBug.drucke("DbBestWorkflow.decodeErgebnis 001");
            System.err.println(" " + e.getMessage());
        }

        return lBestWorkflow;
    }

    /********************* Fuellen Prepared Statement mit allen Feldern, beginnend bei offset.*****************
     * Kann sowohl für Insert, als auch für update verwendet werden.
     * 
     * offset= Startposition des ersten Feldes (also z.B. 1)
     */
    private int anzfelder = 15; /*Anpassen auf Anzahl der Felder pro Datensatz  ident nicht mit enthalten!*/

    void fuellePreparedStatementKomplett(PreparedStatement pPStm, int pOffset, EclBestWorkflow pIpTracking) {

        //		int startOffset=pOffset; /*Nur erforderlich zum Überprüfen von Programmierfehlern - Feldanzahl*/

        try {
            pPStm.setInt(pOffset, pIpTracking.mandant);
            pOffset++;
            //			pOffset++; //Ident nicht setzen - Autoincrement

            pPStm.setString(pOffset, pIpTracking.subverzeichnis);
            pOffset++;
            pPStm.setString(pOffset, pIpTracking.dateinameBestaetigung);
            pOffset++;
            pPStm.setString(pOffset, pIpTracking.dateinameImportAm);
            pOffset++;
            pPStm.setString(pOffset, pIpTracking.zuAktionaersnummer);
            pOffset++;
            pPStm.setInt(pOffset, pIpTracking.origOderKopie);
            pOffset++;
            pPStm.setInt(pOffset, pIpTracking.vorlVollmachtIdent);
            pOffset++;
            pPStm.setInt(pOffset, pIpTracking.inBearbeitungDurchUserNr);
            pOffset++;
            pPStm.setInt(pOffset, pIpTracking.pruefstatusErgebnis);
            pOffset++;
            pPStm.setInt(pOffset, pIpTracking.pruefstatusVorgang);
            pOffset++;
            pPStm.setInt(pOffset, pIpTracking.wurdeQualitaetsgesichert);
            pOffset++;
            pPStm.setString(pOffset, pIpTracking.kommentar);
            pOffset++;
            pPStm.setString(pOffset, pIpTracking.bearbeitungsZeit);
            pOffset++;
            pPStm.setInt(pOffset, pIpTracking.bearbeitungsUser);
            pOffset++;
            pPStm.setString(pOffset, pIpTracking.bearbeitungsUserName);
            pOffset++;

            //			if (pOffset-startOffset!=anzfelder){ 
            //				/*Nur wg. Überprüfung auf Programmierfehler - alle Felder berücksichtigt?*/
            //				CaBug.drucke("DbBestWorkflow.fuellePreparedStatementKomplett 002");
            //			}

        } catch (SQLException e) {
            CaBug.drucke("DbBestWorkflow.fuellePreparedStatementKomplett 001");
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
    public int insert(EclBestWorkflow pVerarbeitungsLauf) {

        int erg = 0;
        PreparedStatement lPStm = null;
        String lSql="";
        
        try {
            /*Felder Neuanlage füllen*/
            lSql = "INSERT INTO " + dbBundle.getSchemaMandant() + "tbl_bestworkflow " + "(" + "mandant, "
                    + "subverzeichnis, dateinameBestaetigung, dateinameImportAm, zuAktionaersnummer, origOderKopie, vorlVollmachtIdent, inBearbeitungDurchUserNr, pruefstatusErgebnis, pruefstatusVorgang, "
                    + "wurdeQualitaetsgesichert, kommentar, bearbeitungsZeit, bearbeitungsUser, bearbeitungsUserName "
                    + ")" + "VALUES (" + "?, " + "?, ?, ?, ?, ?, ?, ?, ?, ?, " + "?, ?, ?, ?, ? " + ")";
            lPStm = verbindung.prepareStatement(lSql, ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
            fuellePreparedStatementKomplett(lPStm, 1, pVerarbeitungsLauf);
            erg = lPStm.executeUpdate();
            lPStm.close();
        } catch (Exception e2) {
            CaBug.drucke(" 001");
            System.err.println(" " + e2.getMessage());
        }

        if (erg == 0) {/*Fehler beim Einfügen - d.h. z.B. primaryKey bereits vorhanden*/
            return (-1);
        }

        try {
            lSql = "SELECT LAST_INSERT_ID();";
            lPStm = verbindung.prepareStatement(lSql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            ResultSet lErgebnis = lPStm.executeQuery();
            
            lErgebnis.last();
            
            lErgebnis.beforeFirst();
            lErgebnis.next();
            int lastIdent=lErgebnis.getInt(1);
            pVerarbeitungsLauf.ident=lastIdent;
            
        } catch (Exception e2) {
            CaBug.drucke("002");
            System.err.println(" " + e2.getMessage());
        }
              
        return (1);
    }

    /**Einlesen eines bestimmten Laufs mit  ident*/
    public int readZuident(long pIdent) {
        int anzInArray = 0;
        PreparedStatement lPStm = null;

        try {
            String lSql = "SELECT * from " + dbBundle.getSchemaMandant() + "tbl_bestworkflow bwo WHERE ident=?;";
            lPStm = verbindung.prepareStatement(lSql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            lPStm.setLong(1, pIdent);

            ResultSet lErgebnis = lPStm.executeQuery();
            lErgebnis.last();
            anzInArray = lErgebnis.getRow();
            lErgebnis.beforeFirst();

            ergebnisArray = new EclBestWorkflow[anzInArray];

            int i = 0;
            while (lErgebnis.next() == true) {
                ergebnisArray[i] = this.decodeErgebnis(lErgebnis);
                i++;
            }

            lErgebnis.close();
            lPStm.close();

        } catch (Exception e) {
            CaBug.drucke("DbBestWorkflow.readZuident 003");
            System.err.println(" " + e.getMessage());
            return (-1);
        }
        return (anzInArray);
    }

    /**Einlesen eines bestimmten Laufs mit  ident*/
    public int readZuVorlVollmacht(int pIdent) {
        int anzInArray = 0;
        PreparedStatement lPStm = null;

        try {
            String lSql = "SELECT * from " + dbBundle.getSchemaMandant()
                    + "tbl_bestworkflow bwo WHERE vorlVollmachtIdent=?;";
            lPStm = verbindung.prepareStatement(lSql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            lPStm.setInt(1, pIdent);

            ResultSet lErgebnis = lPStm.executeQuery();
            lErgebnis.last();
            anzInArray = lErgebnis.getRow();
            lErgebnis.beforeFirst();

            ergebnisArray = new EclBestWorkflow[anzInArray];

            int i = 0;
            while (lErgebnis.next() == true) {
                ergebnisArray[i] = this.decodeErgebnis(lErgebnis);
                i++;
            }

            lErgebnis.close();
            lPStm.close();

        } catch (Exception e) {
            CaBug.drucke("DbBestWorkflow.readZuident 003");
            System.err.println(" " + e.getMessage());
            return (-1);
        }
        return (anzInArray);
    }

    /**Einlesen Vorgänge
     * pStatus 1 bis 4 wie in Table. 99 = Mitgliedsnummer, steht dann im String
     * notqs=nur nicht Qualitätsgesichert*/
    public int readVorgaengeZuident(int pStatus, boolean pNotQS, String pMitgliedsNr) {
        int anzInArray = 0;
        PreparedStatement lPStm = null;

        try {
            //			String lSql="SELECT DISTINCT bwo.dateinameBestaetigung, bwo.zuAktionaersnummer, bwo.pruefstatusErgebnis from "+
            //					dbBundle.getSchemaMandant()+"tbl_bestworkflow bwo WHERE ";
            String lSql = "SELECT * from " + dbBundle.getSchemaMandant() + "tbl_bestworkflow bwo WHERE ";
            if (pStatus < 99) {
                lSql = lSql + " bwo.pruefstatusErgebnis=? ";
            } else {
                lSql = lSql + " bwo.zuAktionaersnummer=? ";
            }
            if (pNotQS == true) {
                lSql = lSql + " AND bwo.wurdeQualitaetsgesichert=0 ";
            }

            lPStm = verbindung.prepareStatement(lSql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            if (pStatus < 99) {
                lPStm.setInt(1, pStatus);
            } else {
                lPStm.setString(1, pMitgliedsNr + "0");
            }

            ResultSet lErgebnis = lPStm.executeQuery();
            lErgebnis.last();
            anzInArray = lErgebnis.getRow();
            lErgebnis.beforeFirst();

            ergebnisArray = new EclBestWorkflow[anzInArray];

            int i = 0;
            while (lErgebnis.next() == true) {
                //				EclBestWorkflow lBestWorkflow=new EclBestWorkflow();
                //				lBestWorkflow.dateinameBestaetigung=lErgebnis.getString("bwo.dateinameBestaetigung");
                //				lBestWorkflow.zuAktionaersnummer=lErgebnis.getString("bwo.zuAktionaersnummer");
                //				lBestWorkflow.pruefstatusErgebnis=lErgebnis.getInt("bwo.pruefstatusErgebnis");
                //
                //				ergebnisArray[i]=lBestWorkflow;
                ergebnisArray[i] = this.decodeErgebnis(lErgebnis);
                i++;
            }

            lErgebnis.close();
            lPStm.close();

        } catch (Exception e) {
            CaBug.drucke("DbBestWorkflow.readVorgaengeZuident 003");
            System.err.println(" " + e.getMessage());
            return (-1);
        }
        return (anzInArray);
    }

    /**Einlesen eines bestimmten Laufs mit  ident*/
    public int readZuDatei(String pDateinameBestaetigung) {
        int anzInArray = 0;
        PreparedStatement lPStm = null;

        try {
            String lSql = "SELECT * from " + dbBundle.getSchemaMandant()
                    + "tbl_bestworkflow bwo WHERE bwo.dateinameBestaetigung=?;";
            lPStm = verbindung.prepareStatement(lSql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            lPStm.setString(1, pDateinameBestaetigung);

            ResultSet lErgebnis = lPStm.executeQuery();
            lErgebnis.last();
            anzInArray = lErgebnis.getRow();
            lErgebnis.beforeFirst();

            ergebnisArray = new EclBestWorkflow[anzInArray];

            int i = 0;
            while (lErgebnis.next() == true) {
                ergebnisArray[i] = this.decodeErgebnis(lErgebnis);
                i++;
            }

            lErgebnis.close();
            lPStm.close();

        } catch (Exception e) {
            CaBug.drucke("DbBestWorkflow.read 003");
            System.err.println(" " + e.getMessage());
            return (-1);
        }
        return (anzInArray);
    }

    public int readZuDateiUndAktionaer(String pDateinameBestaetigung, String pAktionaersnummer) {
        int anzInArray = 0;
        PreparedStatement lPStm = null;

        try {
            String lSql = "SELECT * from " + dbBundle.getSchemaMandant()
                    + "tbl_bestworkflow bwo WHERE bwo.dateinameBestaetigung=? and bwo.zuAktionaersnummer=?;";
            lPStm = verbindung.prepareStatement(lSql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            lPStm.setString(1, pDateinameBestaetigung);
            lPStm.setString(2, pAktionaersnummer);

            ResultSet lErgebnis = lPStm.executeQuery();
            lErgebnis.last();
            anzInArray = lErgebnis.getRow();
            lErgebnis.beforeFirst();

            ergebnisArray = new EclBestWorkflow[anzInArray];

            int i = 0;
            while (lErgebnis.next() == true) {
                ergebnisArray[i] = this.decodeErgebnis(lErgebnis);
                i++;
            }

            lErgebnis.close();
            lPStm.close();

        } catch (Exception e) {
            CaBug.drucke("DbBestWorkflow.readZuDateiUndAktionaer 003");
            System.err.println(" " + e.getMessage());
            return (-1);
        }
        return (anzInArray);
    }

    public int readZuAktionaer(String pAktionaersnummer) {
        int anzInArray = 0;
        PreparedStatement lPStm = null;

        try {
            String lSql = "SELECT * from " + dbBundle.getSchemaMandant()
                    + "tbl_bestworkflow bwo WHERE bwo.zuAktionaersnummer=?;";
            lPStm = verbindung.prepareStatement(lSql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            lPStm.setString(1, pAktionaersnummer);

            ResultSet lErgebnis = lPStm.executeQuery();
            lErgebnis.last();
            anzInArray = lErgebnis.getRow();
            lErgebnis.beforeFirst();

            ergebnisArray = new EclBestWorkflow[anzInArray];

            int i = 0;
            while (lErgebnis.next() == true) {
                ergebnisArray[i] = this.decodeErgebnis(lErgebnis);
                i++;
            }

            lErgebnis.close();
            lPStm.close();

        } catch (Exception e) {
            CaBug.drucke("DbBestWorkflow.readZuAktionaer 003");
            System.err.println(" " + e.getMessage());
            return (-1);
        }
        return (anzInArray);
    }

    /**Einlesen eines bestimmten Laufs mit  inBeartungDurchUserNr*/
    public int readInBearbeitung(int pInBearbeitungDurchUserNr) {
        int anzInArray = 0;
        PreparedStatement lPStm = null;

        try {
            String lSql = "SELECT * from " + dbBundle.getSchemaMandant()
                    + "tbl_bestworkflow bwo WHERE inBearbeitungDurchUserNr=?;";
            lPStm = verbindung.prepareStatement(lSql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            lPStm.setInt(1, pInBearbeitungDurchUserNr);

            ResultSet lErgebnis = lPStm.executeQuery();
            lErgebnis.last();
            anzInArray = lErgebnis.getRow();
            lErgebnis.beforeFirst();

            ergebnisArray = new EclBestWorkflow[anzInArray];

            int i = 0;
            while (lErgebnis.next() == true) {
                ergebnisArray[i] = this.decodeErgebnis(lErgebnis);
                i++;
            }

            lErgebnis.close();
            lPStm.close();

        } catch (Exception e) {
            CaBug.drucke("DbBestWorkflow.readInBearbeitung 003");
            System.err.println(" " + e.getMessage());
            return (-1);
        }
        return (anzInArray);
    }

    /**Bestimmen der Anzahl Dokumente mit PruefStatus
     * 0 = Basisprüfen Dokumente (nur die Dokumenten-Elemente durchsuchen)
     * 1 = Angenommen
     * 2 = Abgelehnt
     * 3 = Wiedervorlage (nur die Dokumenten-Elemente durchsuchen)
     * 4 = Andere Dokumente*/
    public int readAnzahl(int pPruefStatusErgebnis) {

        int anzInArray = 0;
        PreparedStatement lPStm = null;

        try {
            String lSql = "SELECT DISTINCT bwo.dateinameBestaetigung from " + dbBundle.getSchemaMandant()
                    + "tbl_bestworkflow bwo WHERE pruefstatusErgebnis=?";
            if (pPruefStatusErgebnis==0 || pPruefStatusErgebnis==3) {
                lSql=lSql+" AND origOderKopie!=3";
            }
            lSql = lSql +";";
            lPStm = verbindung.prepareStatement(lSql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            lPStm.setInt(1, pPruefStatusErgebnis);

            ResultSet lErgebnis = lPStm.executeQuery();
            lErgebnis.last();
            anzInArray = lErgebnis.getRow();
            lErgebnis.close();
            lPStm.close();

        } catch (Exception e) {
            CaBug.drucke("DbBestWorkflow.readAnzahl 003");
            System.err.println(" " + e.getMessage());
            return (-1);
        }
        return (anzInArray);
    }

    /**Bestimmen der Anzahl Dokumente in Bearbeitung*/
    public int readAnzahlInBearbeitung() {

        int anzInArray = 0;
        PreparedStatement lPStm = null;

        try {
            String lSql = "SELECT DISTINCT bwo.dateinameBestaetigung from " + dbBundle.getSchemaMandant()
                    + "tbl_bestworkflow bwo WHERE inBearbeitungDurchUserNr!=0;";
            lPStm = verbindung.prepareStatement(lSql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);

            ResultSet lErgebnis = lPStm.executeQuery();
            lErgebnis.last();
            anzInArray = lErgebnis.getRow();
            lErgebnis.close();
            lPStm.close();

        } catch (Exception e) {
            CaBug.drucke("DbBestWorkflow.readAnzahlInBearbeitung 003");
            System.err.println(" " + e.getMessage());
            return (-1);
        }
        return (anzInArray);
    }

    //	/**Lese alle Verarbeitungsläufe
    //	 * Falls pMandant>0 dann nur die für diesen Mandanten
    //	 * */
    //	public int readAll(int pMandant) {
    //		int anzInArray=0;
    //		PreparedStatement lPStm=null;
    //		
    //		try {
    //			String lSql="SELECT * from "+dbBundle.getSchemaMandant()+"tbl_bestworkflow vl ";
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
    //			ergebnisArray=new EclBestWorkflow[anzInArray];
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
    //			CaBug.drucke("DbBestWorkflow.readAll_technisch 003");
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
    //			String lSql="SELECT * from "+dbBundle.getSchemaMandant()+"tbl_bestworkflow vl WHERE ident=?;";
    //			lPStm=verbindung.prepareStatement(lSql,ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
    //			lPStm.setInt(1, ident);
    //
    //			ResultSet lErgebnis=lPStm.executeQuery();
    //			lErgebnis.last();
    //			anzInArray=lErgebnis.getRow();
    //			lErgebnis.beforeFirst();
    //			
    //			ergebnisArray=new EclBestWorkflow[anzInArray];
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
    //			CaBug.drucke("DbBestWorkflow.read 003");
    //			System.err.println(" "+e.getMessage());
    //			return (-1);
    //		}
    //		return (anzInArray);
    //	}
    //
    /**Update. Versionsnummer wird um 1 hochgezählt
     * 
     * Feld mandant wird von dieser Funktion nicht verändert.
     * 
     * Zum Sicherstellen der Multiuserfähigkeit muß in jedem Fall der rc CaFehler.pfXyWurdeVonAnderemBenutzerVeraendert
     * nach Aufruf dieser Funktion abgefangen werden.
     * Ansonsten: rc=1 => ok, ansonsten Fehler
     */
    public int update(EclBestWorkflow pBestWorkflow) {

        try {
            String lSql = "UPDATE " + dbBundle.getSchemaMandant() + "tbl_bestworkflow SET " + "mandant=?, "
                    + "subverzeichnis=?, dateinameBestaetigung=?, dateinameImportAm=?, zuAktionaersnummer=?, origOderKopie=?, vorlVollmachtIdent=?, inBearbeitungDurchUserNr=?, pruefstatusErgebnis=?, pruefstatusVorgang=?, "
                    + "wurdeQualitaetsgesichert=?, kommentar=?, bearbeitungsZeit=?, bearbeitungsUser=?, bearbeitungsUserName=? "
                    + "WHERE " + "ident=? ";

            PreparedStatement lPStm = verbindung.prepareStatement(lSql);
            fuellePreparedStatementKomplett(lPStm, 1, pBestWorkflow);
            lPStm.setLong(anzfelder + 1, pBestWorkflow.ident);

            int ergebnis = lPStm.executeUpdate();
            lPStm.close();
            if (ergebnis == 0) {
                return (CaFehler.pfXyWurdeVonAnderemBenutzerVeraendert);
            }
        } catch (Exception e1) {
            CaBug.drucke("DbBestWorkflow.update 001");
            System.err.println(" " + e1.getMessage());
            return (CaFehler.pfdXyBereitsVorhanden);
        }

        return (1);
    }

    public int updateAufNichtVerwenden(long pIdent) {

        try {
            String lSql = "UPDATE " + dbBundle.getSchemaMandant() + "tbl_bestworkflow SET "
                    + "pruefstatusErgebnis=4 "
                    + "WHERE " + "ident=? ";

            PreparedStatement lPStm = verbindung.prepareStatement(lSql);
           lPStm.setLong(1, pIdent);

            int ergebnis = lPStm.executeUpdate();
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

    /**0, wenn nichts mehr in Bearbeitung; ansonsten der Satz in bearebtigun ganz normal als Ergebnis*/
    public int reserviereFuerBasis() {

        int erg = 0;
        int ergebnis;
        erg = dbBundle.dbBasis.getInterneIdentBestWorkflowBasis();

        try {
            String lSql = "UPDATE " + dbBundle.getSchemaMandant() + "tbl_bestworkflow SET "
                    + "inBearbeitungDurchUserNr=? " + "WHERE "
                    + "inBearbeitungDurchUserNr=0 AND pruefstatusErgebnis=0 "
                    + "AND origOderKopie!=3 LIMIT 1 ";

            PreparedStatement lPStm = verbindung.prepareStatement(lSql);
            lPStm.setInt(1, erg);

            ergebnis = lPStm.executeUpdate();
            lPStm.close();
            if (ergebnis == 0) {
                return (CaFehler.pfXyWurdeVonAnderemBenutzerVeraendert);
            }
        } catch (Exception e1) {
            CaBug.drucke("DbBestWorkflow.reserviereFuerBasis 001");
            System.err.println(" " + e1.getMessage());
            return (CaFehler.pfdXyBereitsVorhanden);
        }

        if (ergebnis < 1) {
            return 0;
        }

        readInBearbeitung(erg);
        return (1);
    }

    /**0, wenn nichts mehr in Bearbeitung; ansonsten der Satz in bearebtigun ganz normal als Ergebnis*/
    public int reservierungAlleZuruecksetzen() {

        int ergebnis;

        try {
            String lSql = "UPDATE " + dbBundle.getSchemaMandant() + "tbl_bestworkflow SET "
                    + "inBearbeitungDurchUserNr=0 " + "WHERE " + "inBearbeitungDurchUserNr!=0";

            PreparedStatement lPStm = verbindung.prepareStatement(lSql);

            ergebnis = lPStm.executeUpdate();
            lPStm.close();
            if (ergebnis == 0) {
                return (CaFehler.pfXyWurdeVonAnderemBenutzerVeraendert);
            }
        } catch (Exception e1) {
            CaBug.drucke("DbBestWorkflow.reservierungAlleZuruecksetzen 001");
            System.err.println(" " + e1.getMessage());
            return (CaFehler.pfdXyBereitsVorhanden);
        }

        return (1);
    }

    //	public int delete(int pIdent){
    //		
    //		try {
    //			
    //		String sql="DELETE FROM "+dbBundle.getSchemaMandant()+"tbl_bestworkflow WHERE ident=? ";
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
