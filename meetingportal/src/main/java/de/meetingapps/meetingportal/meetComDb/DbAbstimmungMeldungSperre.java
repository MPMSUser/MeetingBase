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
import de.meetingapps.meetingportal.meetComEntities.EclAbstimmungMeldungSperre;

/**Hinweis: rueckfragen wird wie fragen behandelt*/

public class  DbAbstimmungMeldungSperre extends DbRoot<EclAbstimmungMeldungSperre> {

//    private int logDrucken=10;
    
    public DbAbstimmungMeldungSperre(DbBundle pDbBundle) {
        super(pDbBundle);
    }

    
    @Override
    String getCreateString() {

//      @formatter:off
       String createString = "CREATE TABLE " + getSchema() + getTableName()+" ( "
                + "`meldungIdentGesperrt` int(11) NOT NULL, "
               + "PRIMARY KEY (`meldungIdentGesperrt`) " + ") ";
//      @formatter:on
        return createString;
    }

 
    @Override
    void resetInterneIdent(int pHoechsteIdent) {
    }

    @Override
    String getFeldFuerInterneIdent() {
        return "meldungIdentGesperrt";
    }

    @Override
    int getAnzFelder() {
        return 1;
    }


    @Override
    EclAbstimmungMeldungSperre decodeErgebnis(ResultSet pErgebnis) {
        EclAbstimmungMeldungSperre lEclReturn = new EclAbstimmungMeldungSperre();

        try {
            lEclReturn.meldungIdentGesperrt = pErgebnis.getInt("meldungIdentGesperrt");
                     
       } catch (Exception e) {
            CaBug.drucke("001");
            System.err.println(" " + e.getMessage());
        }

        return lEclReturn;
    }



    @Override
    void fuellePreparedStatementKomplett(PreparedStatement pPStm, int pOffset, EclAbstimmungMeldungSperre pEcl) {
        int startOffset = pOffset; /*Nur erforderlich zum Überprüfen von Programmierfehlern - Feldanzahl*/

        try {
//          @formatter:off
            
            pPStm.setInt(pOffset, pEcl.meldungIdentGesperrt);pOffset++;

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

    private String[] felder=null; 
    private boolean initErfolgt=false;
    private void initFelder(){
        if (initErfolgt) {return;}
        felder=new String[getAnzFelder()];
        felder[0]="meldungIdentGesperrt";
        initErfolgt=true;
    }

    @Override
    public int insert(EclAbstimmungMeldungSperre pEcl) {

        initFelder();
        return insertIntern(felder, pEcl);
    }

    
    /** Return-Wert = Anzahl der gefundenen Sätze (oder Fehlermeldung <0)
     * Liefert alle Sätze der versionen, die 0-er Version (also die Einreichung) kommt an erster Position*/
    public int read(int pMeldungsIdent) {
        int anzInArray = 0;
        PreparedStatement lPStm = null;

        try {
            String lSql = "SELECT * from " + getSchema() + getTableName()+" where " + 
                    "meldungIdentGesperrt=? ;";
            lPStm = verbindung.prepareStatement(lSql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            lPStm.setInt(1, pMeldungsIdent);

            anzInArray = readIntern(lPStm);

        } catch (Exception e) {
            CaBug.drucke("003");
            System.err.println(" " + e.getMessage());
            return (-1);
        }
        return (anzInArray);
    }
    

//    public int delete_all(){
//        int ergebnis=0;
//        try {
//            String sql = "DELETE FROM " + getSchema() + getTableName()+" ";
//            PreparedStatement pstm1 = verbindung.prepareStatement(sql);
//            ergebnis = deleteIntern(pstm1);
//        } catch (Exception e1) {
//            CaBug.drucke("001");
//            System.err.println(" " + e1.getMessage());
//            return (-1);
//        }
//
//        return ergebnis;
//    }


    @Override
    String getSchema() {
        return dbBundle.getSchemaMandant();
    }


    @Override
    String getTableName() {
        return "tbl_abstimmungMeldungSperre";
    }



}
