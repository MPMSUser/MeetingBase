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
import de.meetingapps.meetingportal.meetComHVParam.ParamStruktVersammlungstyp;


public class  DbParamStruktVersammlungstyp extends DbRoot<ParamStruktVersammlungstyp> {

//    private int logDrucken=10;
    
    public DbParamStruktVersammlungstyp(DbBundle pDbBundle) {
        super(pDbBundle);
    }

    
    @Override
    String getCreateString() {

        //      @formatter:off
        String createString = "CREATE TABLE " + getSchema() + getTableName()+" ( "
                + "`identVersammlungstyp` int NOT NULL DEFAULT 0, " 
                + "`typGehoertZuPresetArt` int NOT NULL DEFAULT 0, " 
                
                + "`erbtVonIdentVersammlungstyp` int NOT NULL DEFAULT 0, " 
                
                + "`kurzText` varchar(100) NOT NULL DEFAULT '', " 
                + "`beschreibung` varchar(2000) NOT NULL DEFAULT '', " 

               + "PRIMARY KEY (`identVersammlungstyp`) " + ") ";
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
         return "tbl_paramStruktVersammlungstyp";
    }


    @Override
    String getFeldFuerInterneIdent() {
        return "identVersammlungstyp";
    }

    @Override
    int getAnzFelder() {
        return 5;
    }


    @Override
    ParamStruktVersammlungstyp decodeErgebnis(ResultSet pErgebnis) {
        ParamStruktVersammlungstyp lEclReturn = new ParamStruktVersammlungstyp();

        try {
            lEclReturn.identVersammlungstyp = pErgebnis.getInt("identVersammlungstyp");
            lEclReturn.typGehoertZuPresetArt = pErgebnis.getInt("typGehoertZuPresetArt");
            lEclReturn.erbtVonIdentVersammlungstyp = pErgebnis.getInt("erbtVonIdentVersammlungstyp");
            
            lEclReturn.kurzText = pErgebnis.getString("kurzText");
            lEclReturn.beschreibung = pErgebnis.getString("beschreibung");
            
       } catch (Exception e) {
            CaBug.drucke("001");
            System.err.println(" " + e.getMessage());
        }

        return lEclReturn;
    }



    @Override
    void fuellePreparedStatementKomplett(PreparedStatement pPStm, int pOffset, ParamStruktVersammlungstyp pEcl) {
        int startOffset = pOffset; /*Nur erforderlich zum Überprüfen von Programmierfehlern - Feldanzahl*/

        try {
//          @formatter:off
            
            pPStm.setInt(pOffset, pEcl.identVersammlungstyp);pOffset++;
            pPStm.setInt(pOffset, pEcl.typGehoertZuPresetArt);pOffset++;
            pPStm.setInt(pOffset, pEcl.erbtVonIdentVersammlungstyp);pOffset++;
            
            pPStm.setString(pOffset, pEcl.kurzText);pOffset++;
            pPStm.setString(pOffset, pEcl.beschreibung);pOffset++;

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
    private final String[] felder= {"identVersammlungstyp", "typGehoertZuPresetArt",
            "erbtVonIdentVersammlungstyp", 
            "kurzText", "beschreibung"
            }; 
//  @formatter:on
    
    private boolean initErfolgt=false;
    private void initFelder(){
        if (initErfolgt) {return;}
        initErfolgt=true;
    }

    @Override
    public int insert(ParamStruktVersammlungstyp pEcl) {
        initFelder();
        return insertIntern(felder, pEcl);
    }


    /**Liest einen Versammlungstyp ein*/
     public int readVersammlungstypIdent(int pIdent) {
        PreparedStatement lPStm = null;
        int anzInArray = 0;
        try {
            String lSql = "SELECT * from " + getSchema() + getTableName()+" "
                    + "WHERE identVersammlungstyp=? ";
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

     /**Liest alle Elemente einer Gruppeein*/
     public int readAll() {
        PreparedStatement lPStm = null;
        int anzInArray = 0;
        try {
            String lSql = "SELECT * from " + getSchema() + getTableName()+" ";
            lSql = lSql + " ORDER BY identVersammlungstyp;";
            lPStm = verbindung.prepareStatement(lSql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
           anzInArray = readIntern(lPStm);

        } catch (Exception e) {
            CaBug.drucke("001");
            System.err.println(" " + e.getMessage());
            return (-1);
        }
        return (anzInArray);
    }

     
     /**Liest alle Elemente einer Gruppe für eine PresetArt ein*/
     public int readAll(int pPresetArt) {
        PreparedStatement lPStm = null;
        int anzInArray = 0;
        try {
            String lSql = "SELECT * from " + getSchema() + getTableName()+" ";
            lSql = lSql + " WHERE typGehoertZuPresetArt=? ";
            lSql = lSql + " ORDER BY identVersammlungstyp;";
            lPStm = verbindung.prepareStatement(lSql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            lPStm.setInt(1, pPresetArt);
           anzInArray = readIntern(lPStm);

        } catch (Exception e) {
            CaBug.drucke("001");
            System.err.println(" " + e.getMessage());
            return (-1);
        }
        return (anzInArray);
    }

//    
//    /**Update. 
//     * 
//     * Returnwert:
//     * pfXyWurdeVonAnderemBenutzerVeraendert
//     * -1 => unbekannter Fehler
//     * 1 = Update wurde durchgeführt.
//     * 
//     */
//    public int update(ParamStruktVersammlungstyp pEcl) {
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
