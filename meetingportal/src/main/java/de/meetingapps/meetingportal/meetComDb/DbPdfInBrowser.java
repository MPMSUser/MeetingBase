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
import de.meetingapps.meetingportal.meetComEntities.EclPdfInBrowser;

public class DbPdfInBrowser {

    private Connection verbindung = null;
    private DbBundle dbBundle = null;

    public EclPdfInBrowser ergebnisArray[] = null;

    /*************************Initialisierung***************************/
    public DbPdfInBrowser(DbBundle pDbBundle) {
        /* Verbindung in lokale Daten eintragen*/
        if (pDbBundle == null) {
            CaBug.drucke("DbPdfInBrowser.init 001 - dbBundle nicht initialisiert");
            return;
        }
        if (pDbBundle.dbBasis == null) {
            CaBug.drucke("DbPdfInBrowser.init 002 - dbBasis nicht initialisiert");
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
    public EclPdfInBrowser ergebnisPosition(int pN) {
        if (ergebnisArray == null) {
            CaBug.drucke("DbPdfInBrowser.ergebnisPosition 001");
            return null;
        }
        if (pN < 0) {
            CaBug.drucke("DbPdfInBrowser.ergebnisPosition 002");
            return null;
        }
        if (pN >= ergebnisArray.length) {
            CaBug.drucke("DbPdfInBrowser.ergebnisPosition 003");
            return null;
        }
        return ergebnisArray[pN];
    }

    //	/**************************deleteAll Löschen aller Datensätze  eines Mandanten****************/
    //	public int deleteAll(){
    //		return dbBundle.dbLowLevel.deleteMandant("DELETE FROM "+dbBundle.getSchemaMandant()+
    //				"tbl_pdfinbrowser where mandant=?;");
    //	}

    /*******Checken, ob table überhaupt vorhanden ist***************************/
    public boolean checkTableVorhanden() {
        DbLowLevel lDbLowLevel = new DbLowLevel(dbBundle);
        return lDbLowLevel.checkTableVorhanden("SELECT * from tbl_pdfinbrowser WHERE ident=0; ");
    }

    //	CREATE TABLE db_meetingcomfort.tbl_pdfinbrowser ( 
    //			  `ident`  bigint(20)  NOT NULL AUTO_INCREMENT, 
    //			  `eindeutigerKey` char(20) NOT NULL, 
    //			  `pdfLink` varchar(200) DEFAULT NULL, 
    //			  PRIMARY KEY (ident)) ;

    /************Neuanlegen Table******************************/
    public int createTable() {
        int rc = 0;
        DbLowLevel lDbLowLevel = new DbLowLevel(dbBundle);
        rc = lDbLowLevel.createTable("CREATE TABLE " + dbBundle.getSchemaAllgemein() + "tbl_pdfinbrowser ( "
                + "`ident`  bigint(20)  NOT NULL AUTO_INCREMENT, " + "`eindeutigerKey` char(20) NOT NULL, "
                + "`pdfLink` varchar(200) DEFAULT NULL, " + "PRIMARY KEY (`ident`) " + ")  ");
        System.out.println("DbPdfInBrowser create table rc=" + rc);
        return rc;
    }

    /********** dekodiert die aktuelle Position aus ergebnis in EclAktienregisterEintrag und gibt dieses zurück******/
    EclPdfInBrowser decodeErgebnis(ResultSet pErgebnis) {

        EclPdfInBrowser lPdfInBrowser = new EclPdfInBrowser();

        try {
            lPdfInBrowser.ident = pErgebnis.getLong("pd.ident");
            lPdfInBrowser.eindeutigerKey = pErgebnis.getString("pd.eindeutigerKey");
            lPdfInBrowser.pdfLink = pErgebnis.getString("pd.pdfLink");

        } catch (Exception e) {
            CaBug.drucke("DbPdfInBrowser.decodeErgebnis 001");
            System.err.println(" " + e.getMessage());
        }

        return lPdfInBrowser;
    }

    /********************* Fuellen Prepared Statement mit allen Feldern, beginnend bei offset.*****************
     * Kann sowohl für Insert, als auch für update verwendet werden.
     * 
     * offset= Startposition des ersten Feldes (also z.B. 1)
     */
    //	private int anzfelder=3; /*Anpassen auf Anzahl der Felder pro Datensatz  ident nicht mit enthalten!*/
    void fuellePreparedStatementKomplett(PreparedStatement pPStm, int pOffset, EclPdfInBrowser pPdfInBrowser) {

        //		int startOffset=pOffset; /*Nur erforderlich zum Überprüfen von Programmierfehlern - Feldanzahl*/

        try {
            //			pOffset++; //Ident nicht setzen - Autoincrement

            pPStm.setString(pOffset, pPdfInBrowser.eindeutigerKey);
            pOffset++;
            pPStm.setString(pOffset, pPdfInBrowser.pdfLink);
            pOffset++;

            //			if (pOffset-startOffset!=anzfelder){ 
            //				/*Nur wg. Überprüfung auf Programmierfehler - alle Felder berücksichtigt?*/
            //				CaBug.drucke("DbPdfInBrowser.fuellePreparedStatementKomplett 002");
            //			}

        } catch (SQLException e) {
            CaBug.drucke("DbPdfInBrowser.fuellePreparedStatementKomplett 001");
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
    public int insert(EclPdfInBrowser pVerarbeitungsLauf) {

        int erg = 0;

        try {
            /*Felder Neuanlage füllen*/
            String lSql = "INSERT INTO " + dbBundle.getSchemaAllgemein() + "tbl_pdfinbrowser " + "("
                    + "eindeutigerKey, pdfLink " + ")" + "VALUES (" + "?, ? " + ")";
            PreparedStatement lPStm = verbindung.prepareStatement(lSql, ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
            fuellePreparedStatementKomplett(lPStm, 1, pVerarbeitungsLauf);
            erg = lPStm.executeUpdate();
            lPStm.close();
        } catch (Exception e2) {
            CaBug.drucke("DbPdfInBrowser.insert 001");
            System.err.println(" " + e2.getMessage());
        }

        if (erg == 0) {/*Fehler beim Einfügen - d.h. z.B. primaryKey bereits vorhanden*/
            return (-1);
        }

        return (1);
    }

    /**Liest Eintrag mit eindeutigerKey
     * */
    public int readKey(String pEindeutigerKey) {
        int anzInArray = 0;
        PreparedStatement lPStm = null;

        try {
            String lSql = "SELECT pd.* FROM " + dbBundle.getSchemaAllgemein() + "tbl_pdfinbrowser pd "
                    + " WHERE pd.eindeutigerKey=? ";
            lPStm = verbindung.prepareStatement(lSql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            lPStm.setString(1, pEindeutigerKey);
            ResultSet lErgebnis = lPStm.executeQuery();
            lErgebnis.last();
            anzInArray = lErgebnis.getRow();
            lErgebnis.beforeFirst();

            ergebnisArray = new EclPdfInBrowser[anzInArray];

            int i = 0;
            while (lErgebnis.next() == true) {
                ergebnisArray[i] = this.decodeErgebnis(lErgebnis);
                i++;
            }
            lErgebnis.close();
            lPStm.close();

        } catch (Exception e) {
            CaBug.drucke("DbPdfInBrowser.readKey 003");
            System.err.println(" " + e.getMessage());
            return (-1);
        }
        return (anzInArray);
    }

    public int delete(long pIdent) {

        try {

            String sql = "DELETE FROM " + dbBundle.getSchemaAllgemein() + "tbl_pdfinbrowser WHERE ident=? ";

            PreparedStatement pstm1 = verbindung.prepareStatement(sql);
            pstm1.setLong(1, pIdent);

            int ergebnis1 = pstm1.executeUpdate();
            pstm1.close();
            if (ergebnis1 == 0) {
                return (CaFehler.pfXyWurdeVonAnderemBenutzerVeraendert);
            }
        } catch (Exception e1) {
            CaBug.drucke("DbPdfInBrowser.delete 001");
            System.err.println(" " + e1.getMessage());
            return (CaFehler.pfdXyBereitsVorhanden);
        }

        return (1);
    }

}
