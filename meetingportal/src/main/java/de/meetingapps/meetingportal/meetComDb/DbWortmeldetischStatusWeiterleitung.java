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
import de.meetingapps.meetingportal.meetComEntities.EclWortmeldetischStatusWeiterleitung;

/**Hinweis: rueckfragen wird wie fragen behandelt*/

public class  DbWortmeldetischStatusWeiterleitung extends DbRoot<EclWortmeldetischStatusWeiterleitung> {

//    private int logDrucken=10;
    
    public DbWortmeldetischStatusWeiterleitung(DbBundle pDbBundle) {
        super(pDbBundle);
    }

    
    @Override
    String getCreateString() {

        //      @formatter:off
        String createString = "CREATE TABLE " + getSchema() + getTableName()+" ( "
                + "`setNr` int NOT NULL DEFAULT 0, " 
                + "`viewBezeichnung` varchar(60) NOT NULL DEFAULT '', " 
                + "`ursprungsStatusBezeichnung` varchar(60) NOT NULL DEFAULT '', " 
                + "`folgeStatusBezeichnung` varchar(60) NOT NULL DEFAULT '', " 
                + "`nurWennBedingungErfuellt` varchar(60) NOT NULL DEFAULT '', " 
               + "PRIMARY KEY (`setNr`, `viewBezeichnung`, `ursprungsStatusBezeichnung`, `folgeStatusBezeichnung`, `nurWennBedingungErfuellt`) " + ") ";
        //      @formatter:on
        return createString;
    }

    @Override
    String getSchema() {
        return dbBundle.getSchemaAllgemein();
    }


    @Override
    void resetInterneIdent(int pHoechsteIdent) {
    }


    @Override
    String getTableName() {
         return "tbl_wortmeldetischStatusWeiterleitung";
    }


    @Override
    String getFeldFuerInterneIdent() {
        return "";
    }

    @Override
    int getAnzFelder() {
        return 5;
    }


    @Override
    EclWortmeldetischStatusWeiterleitung decodeErgebnis(ResultSet pErgebnis) {
        EclWortmeldetischStatusWeiterleitung lEclReturn = new EclWortmeldetischStatusWeiterleitung();

        try {
            lEclReturn.setNr = pErgebnis.getInt("setNr");
            lEclReturn.viewBezeichnung = pErgebnis.getString("viewBezeichnung");
            lEclReturn.ursprungsStatusBezeichnung = pErgebnis.getString("ursprungsStatusBezeichnung");
            lEclReturn.folgeStatusBezeichnung = pErgebnis.getString("folgeStatusBezeichnung");
            lEclReturn.nurWennBedingungErfuellt = pErgebnis.getString("nurWennBedingungErfuellt");

       } catch (Exception e) {
            CaBug.drucke("001");
            System.err.println(" " + e.getMessage());
        }

        return lEclReturn;
    }



    @Override
    void fuellePreparedStatementKomplett(PreparedStatement pPStm, int pOffset, EclWortmeldetischStatusWeiterleitung pEcl) {
        int startOffset = pOffset; /*Nur erforderlich zum Überprüfen von Programmierfehlern - Feldanzahl*/

        try {
//          @formatter:off
            
            pPStm.setInt(pOffset, pEcl.setNr);pOffset++;
            pPStm.setString(pOffset, pEcl.viewBezeichnung);pOffset++;
            pPStm.setString(pOffset, pEcl.ursprungsStatusBezeichnung);pOffset++;
            pPStm.setString(pOffset, pEcl.folgeStatusBezeichnung);pOffset++;
            pPStm.setString(pOffset, pEcl.nurWennBedingungErfuellt);pOffset++;
 
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
    private final String[] felder= {"setNr", 
            "viewBezeichnung", "ursprungsStatusBezeichnung", "folgeStatusBezeichnung", "nurWennBedingungErfuellt"
            }; 
//  @formatter:on
    private boolean initErfolgt=false;
    private void initFelder(){
        if (initErfolgt) {return;}
        initErfolgt=true;
    }

    @Override
    public int insert(EclWortmeldetischStatusWeiterleitung pEcl) {
        initFelder();
        return insertIntern(felder, pEcl);
    }


    /**Liest alle Demo-Kennungen ein*/
     public int readAll(int pSetNr) {
        PreparedStatement lPStm = null;
        int anzInArray = 0;
        try {
            String lSql = "SELECT * from " + getSchema() + getTableName()+" ";
            lSql = lSql + "WHERE setNr=?;";
            lPStm = verbindung.prepareStatement(lSql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            lPStm.setInt(1, pSetNr);
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
//    public int update(EclWortmeldetischStatusWeiterleitung pEcl) {
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

     /**Return-Werte:
      * pfXyWurdeVonAnderemBenutzerVeraendert
      * -1 => undefinierter Fehler
      * 1 => Löschen erfolgreich
      */
     public int deleteSet(int pSetNr) {
         int ergebnis=0;
         try {
             String sql = "DELETE FROM " + getSchema()+getTableName()+
                     " WHERE setNr=? ";
             PreparedStatement pstm1 = verbindung.prepareStatement(sql);
             pstm1.setInt(1, pSetNr);
             ergebnis = deleteIntern(pstm1);
         } catch (Exception e1) {
             CaBug.drucke("001");
             System.err.println(" " + e1.getMessage());
             return (-1);
         }

         return ergebnis;
     }

}
