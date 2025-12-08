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
import de.meetingapps.meetingportal.meetComEntities.EclMailing;

/**Hinweis: rueckfragen wird wie fragen behandelt*/

public class  DbMailing extends DbRoot<EclMailing> {

//    private int logDrucken=10;
    
    public DbMailing(DbBundle pDbBundle) {
        super(pDbBundle);
    }

    
    @Override
    String getCreateString() {

        //      @formatter:off
        String createString = "CREATE TABLE IF NOT EXISTS " + getSchema() + getTableName()+" ( "
                + "`mailingIdent` int NOT NULL DEFAULT 0, " 
                + "`db_version` bigint(20) DEFAULT 0, "
                + "`name` varchar(200) NOT NULL DEFAULT '', " 
                + "`betreff` varchar(600) NOT NULL DEFAULT '', " 
                + "`htmlMail` text NOT NULL, " 
                + "`alternativMailText` text NOT NULL, " 
               + "PRIMARY KEY (`mailingIdent`) " + ") ";
        //      @formatter:on
        return createString;
    }

    
    public void initialisiere() {
        EclMailing eclMailing=new EclMailing();
        
        eclMailing.mailingIdent=1;
        this.insert(eclMailing);
        eclMailing.mailingIdent=2;
        this.insert(eclMailing);
        eclMailing.mailingIdent=3;
        this.insert(eclMailing);
        eclMailing.mailingIdent=4;
        this.insert(eclMailing);
        eclMailing.mailingIdent=5;
        this.insert(eclMailing);
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
         return "tbl_mailing";
    }


    @Override
    String getFeldFuerInterneIdent() {
        return "mailingIdent";
    }

    @Override
    int getAnzFelder() {
        return 6;
    }


    @Override
    EclMailing decodeErgebnis(ResultSet pErgebnis) {
        EclMailing lEclReturn = new EclMailing();

        try {
            lEclReturn.mailingIdent = pErgebnis.getInt("mailingIdent");
            lEclReturn.db_version = pErgebnis.getLong("db_version");
           
            lEclReturn.name = pErgebnis.getString("name");
            lEclReturn.betreff = pErgebnis.getString("betreff");
            lEclReturn.htmlMail = pErgebnis.getString("htmlMail");
            lEclReturn.alternativMailText = pErgebnis.getString("alternativMailText");
            
            
       } catch (Exception e) {
            CaBug.drucke("001");
            System.err.println(" " + e.getMessage());
        }

        return lEclReturn;
    }



    @Override
    void fuellePreparedStatementKomplett(PreparedStatement pPStm, int pOffset, EclMailing pEcl) {
        int startOffset = pOffset; /*Nur erforderlich zum Überprüfen von Programmierfehlern - Feldanzahl*/

        try {
//          @formatter:off
            
            pPStm.setInt(pOffset, pEcl.mailingIdent);pOffset++;
            pPStm.setLong(pOffset, pEcl.db_version);pOffset++;

            pPStm.setString(pOffset, pEcl.name);pOffset++;
            pPStm.setString(pOffset, pEcl.betreff);pOffset++;
            pPStm.setString(pOffset, pEcl.htmlMail);pOffset++;
            pPStm.setString(pOffset, pEcl.alternativMailText);pOffset++;

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

//  @formatter:off
    private final String[] felder= {
            "mailingIdent", 
            "db_version", 
            "name", "betreff", "htmlMail", 
            "alternativMailText"
            }; 
//  @formatter:on
    private boolean initErfolgt=false;
    private void initFelder(){
        if (initErfolgt) {return;}
        initErfolgt=true;
    }

    @Override
    public int insert(EclMailing pEcl) {
        initFelder();
        return insertIntern(felder, pEcl);
    }


    /**Liest alle Demo-Kennungen ein*/
     public int readAll() {
        PreparedStatement lPStm = null;
        int anzInArray = 0;
        try {
            String lSql = "SELECT * from " + getSchema() + getTableName()+" "
                    + "ORDER BY mailingIdent";
            lPStm = verbindung.prepareStatement(lSql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            anzInArray = readIntern(lPStm);

        } catch (Exception e) {
            CaBug.drucke("003");
            System.err.println(" " + e.getMessage());
            return (-1);
        }
        return (anzInArray);
    }

//     public int read(int pIdent) {
//        PreparedStatement lPStm = null;
//        int anzInArray = 0;
//        try {
//            String lSql = "SELECT * from " + getSchema() + getTableName()+" ";
//            lSql = lSql + " WHERE setIdent=?;";
//            lPStm = verbindung.prepareStatement(lSql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
//            lPStm.setInt(1, pIdent);
//           anzInArray = readIntern(lPStm);
//
//        } catch (Exception e) {
//            CaBug.drucke("003");
//            System.err.println(" " + e.getMessage());
//            return (-1);
//        }
//        return (anzInArray);
//    }
//
//    
//    /**Update. 
//     * 
//     * Returnwert:
//     * pfXyWurdeVonAnderemBenutzerVeraendert
//     * -1 => unbekannter Fehler
//     * 1 = Update wurde durchgeführt.
//     * 
//     */
//    public int update(EclMailing pEcl) {
//        int ergebnis=0;
//        initFelder();
//
//        try {
//
//            String lSql = setzeUpdateBasisStringZusammen(felder)
//                    + " WHERE "
//                    + "setIdent=? ";
//
//            PreparedStatement lPStm = verbindung.prepareStatement(lSql);
//            fuellePreparedStatementKomplett(lPStm, 1, pEcl);
//            lPStm.setInt(getAnzFelder() + 1, pEcl.setIdent);
//
//            ergebnis = updateIntern(lPStm);
//        } catch (Exception e1) {
//            CaBug.drucke("001");
//            System.err.println(" " + e1.getMessage());
//            return (-1);
//        }
//
//        return ergebnis;
//    }
//
//
// 
//    /**Return-Werte:
//     * pfXyWurdeVonAnderemBenutzerVeraendert
//     * -1 => undefinierter Fehler
//     * 1 => Löschen erfolgreich
//     */
//    public int delete(int pIdent) {
//        int ergebnis=0;
//        try {
//            String sql = "DELETE FROM " + getSchema()+getTableName()+
//                    " WHERE setIdent=? ";
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


}
