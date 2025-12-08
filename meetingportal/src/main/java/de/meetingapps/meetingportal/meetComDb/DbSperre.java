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

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import de.meetingapps.meetingportal.meetComAllg.CaBug;
import de.meetingapps.meetingportal.meetComAllg.CaDatumZeit;
import de.meetingapps.meetingportal.meetComEntities.EclSperre;

/**Hinweis: rueckfragen wird wie fragen behandelt*/

public class  DbSperre extends DbRoot<EclSperre> {

//    private int logDrucken=10;
    
    public DbSperre(DbBundle pDbBundle) {
        super(pDbBundle);
    }

    
    @Override
    String getCreateString() {

//      @formatter:off
       String createString = "CREATE TABLE " + getSchema() + getTableName()+" ( "
               + "`ident` int(11) NOT NULL DEFAULT 0, "
               + "`wert1` int(11) NOT NULL DEFAULT 0, "
               + "`wert2` int(11) NOT NULL DEFAULT 0, "
               + "`wert3` int(11) NOT NULL DEFAULT 0, "
               
              + "PRIMARY KEY (`ident`) " + ") ";
//      @formatter:on
        return createString;
    }

    public void initialisiere() {
        EclSperre eclSperre=new EclSperre();
        eclSperre.ident=1;
        this.insert(eclSperre);
        eclSperre.ident=2;
        this.insert(eclSperre);
        eclSperre.ident=3;
        this.insert(eclSperre);
    }
    
    
    @Override
    String getSchema() {
        return dbBundle.getSchemaMandant();
    }


    @Override
    void resetInterneIdent(int pHoechsteIdent) {
    }


    @Override
    String getTableName() {
         return "tbl_sperre";
    }


    @Override
    String getFeldFuerInterneIdent() {
        return "";
    }

    @Override
    int getAnzFelder() {
        return 4;
    }


    @Override
    EclSperre decodeErgebnis(ResultSet pErgebnis) {
        EclSperre lEclReturn = new EclSperre();

        try {
            lEclReturn.ident = pErgebnis.getInt("ident");
            lEclReturn.wert1 = pErgebnis.getInt("wert1");
            lEclReturn.wert2 = pErgebnis.getInt("wert2");
            lEclReturn.wert3 = pErgebnis.getInt("wert3");
                     
       } catch (Exception e) {
            CaBug.drucke("001");
            System.err.println(" " + e.getMessage());
        }

        return lEclReturn;
    }



    @Override
    void fuellePreparedStatementKomplett(PreparedStatement pPStm, int pOffset, EclSperre pEcl) {
        int startOffset = pOffset; /*Nur erforderlich zum Überprüfen von Programmierfehlern - Feldanzahl*/

        try {
//          @formatter:off
            
            pPStm.setInt(pOffset, pEcl.ident);pOffset++;
            pPStm.setInt(pOffset, pEcl.wert1);pOffset++;
            pPStm.setInt(pOffset, pEcl.wert2);pOffset++;
            pPStm.setInt(pOffset, pEcl.wert3);pOffset++;

//          @formatter:on

            if (pOffset - startOffset != getAnzFelder()) {
                /*Nur wg. Überprüfung auf Programmierfehler - alle Felder berücksichtigt?*/
                CaBug.drucke("002");
            }

        } catch (SQLException e) {
            CaBug.drucke("001");
            e.printStackTrace();
        }
    }

    private String[] felder= {
            "ident",
            "wert1",
            "wert2",
            "wert3"
     };
    
    private boolean initErfolgt=false;
    private void initFelder(){
        if (initErfolgt) {return;}
        initErfolgt=true;
    }

    @Override
    public int insert(EclSperre pEcl) {
        initFelder();
        
        return insertIntern(felder, pEcl);
    }



     public int read(int pIdent) {
        PreparedStatement lPStm = null;
        int anzInArray = 0;
        try {
            String lSql = "SELECT * from " + getSchema() + getTableName()+" ";
            lSql=lSql+" WHERE ident=? ";
            lPStm = verbindung.prepareStatement(lSql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            lPStm.setInt(1, pIdent);
            anzInArray = readIntern(lPStm);

        } catch (Exception e) {
            CaBug.drucke("003");
            System.err.println(" " + e.getMessage());
            return (-1);
        }
        return (anzInArray);
    }

    
    /**Update. 
     * 
     * Returnwert:
     * pfXyWurdeVonAnderemBenutzerVeraendert
     * -1 => unbekannter Fehler
     * 1 = Update wurde durchgeführt.
     * 
     */
    public int update(EclSperre pEcl) {
        int ergebnis=0;
        initFelder();

        try {

            String lSql = setzeUpdateBasisStringZusammen(felder)
                    + " WHERE "
                    + "ident=? ";

            PreparedStatement lPStm = verbindung.prepareStatement(lSql);
            fuellePreparedStatementKomplett(lPStm, 1, pEcl);
            lPStm.setInt(getAnzFelder() + 1, pEcl.ident);

            ergebnis = updateIntern(lPStm);
        } catch (Exception e1) {
            CaBug.drucke("001");
            System.err.println(" " + e1.getMessage());
            return (-1);
        }

        return ergebnis;
    }

 
//    /**Return-Werte:
//     * pfXyWurdeVonAnderemBenutzerVeraendert
//     * -1 => undefinierter Fehler
//     * 1 => Löschen erfolgreich
//     */
//    public int delete(int pIdent) {
//        int ergebnis=0;
//        try {
//            String sql = "DELETE FROM " + getSchema()+getTableName()+
//                    " WHERE ident=? ";
//            PreparedStatement pstm1 = verbindung.prepareStatement(sql);
//            pstm1.setInt(1, pIdent);
//            ergebnis = deleteIntern(pstm1);
//        } catch (Exception e1) {
//            CaBug.drucke("001");
//            System.err.println(" " + e1.getMessage());
//            return (-1);
//        }
//
//        return ergebnis;
//    }
//
//    public int deleteSet(int pSetNr) {
//        int ergebnis=0;
//        try {
//            String sql = "DELETE FROM " + getSchema()+getTableName()+
//                    " WHERE setNr=? ";
//            PreparedStatement pstm1 = verbindung.prepareStatement(sql);
//            pstm1.setInt(1, pSetNr);
//            ergebnis = deleteIntern(pstm1);
//        } catch (Exception e1) {
//            CaBug.drucke("001");
//            System.err.println(" " + e1.getMessage());
//            return (-1);
//        }
//
//        return ergebnis;
//    }
//

    private int selectVorUpdate(int pIdent) {
        PreparedStatement lPStm = null;
        try {
            String lSql = "SELECT * from " + getSchema() + getTableName()+" ";
            lSql=lSql+" WHERE ident=? FOR UPDATE";
            lPStm = this.dbBasis.verbindungSperre.prepareStatement(lSql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            lPStm.setInt(1, pIdent);
            ResultSet lErgebnis = executeQuery(lPStm);
            lErgebnis.close();
            lPStm.close();


        } catch (Exception e) {
            CaBug.drucke("003");
            System.err.println(" " + e.getMessage());
            return (-1);
        }
        return (1);
        
    }
    
    public int setzeSperre(int pIdent) {
        this.dbBasis.openSperre();
        dbBasis.beginTransactionSperre();
        int rc = 0;

        // System.out.println("in getInterneIdent");
        /* letzteInterneIdent aus tbl_nummernkreise mit schluessel, einlesen */

        int wartezaehler = 0;
        rc = selectVorUpdate(pIdent);
        while (rc == -1) {
            wartezaehler++;
            if (wartezaehler >= 50) {
                CaBug.drucke( "DbSperre.setzeSperre "+pIdent+" gezielter Abbruch");
                throw new IllegalStateException("wartezaehler >= 50");
            }
            System.out.println("setzeSperre Schlüssel=" + pIdent + " Wait " + CaDatumZeit.DatumZeitStringFuerDatenbank());
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("setzeSperre Ende Wait " + CaDatumZeit.DatumZeitStringFuerDatenbank());
            rc = selectVorUpdate(pIdent);
        }
        return rc;
        
    }
    
    public int beendeSperre() {
        dbBasis.endTransactionSperre();
        this.dbBasis.closeSperre();
        return 1;
        
    }
}
