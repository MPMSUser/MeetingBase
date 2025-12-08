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
import de.meetingapps.meetingportal.meetComEntities.EclVeranstaltungenAktion;

/**Hinweis: rueckfragen wird wie fragen behandelt*/

public class  DbVeranstaltungenAktion extends DbRoot<EclVeranstaltungenAktion> {

//    private int logDrucken=10;
    
    public DbVeranstaltungenAktion(DbBundle pDbBundle) {
        super(pDbBundle);
    }

    
    @Override
    String getCreateString() {

        //      @formatter:off
        String createString = "CREATE TABLE " + getSchema() + getTableName()+" ( "
                + "`identAktion` int NOT NULL DEFAULT 0, " 
                + "`gehoertZuElement` int NOT NULL DEFAULT 0, " 
                + "`gehoertZuElementDetail` int NOT NULL DEFAULT 0, " 
                + "`position` int NOT NULL DEFAULT 0, " 
                + "`aktion` int NOT NULL DEFAULT 0, " 
                + "`parameter1` char(200) NOT NULL DEFAULT '', " 
                + "`parameter2` char(200) NOT NULL DEFAULT '', " 
                + "`parameter3` char(200) NOT NULL DEFAULT '', " 
                + "`parameter4` char(200) NOT NULL DEFAULT '', " 
               + "PRIMARY KEY (`identAktion`) " + ") ";
        //      @formatter:on
        return createString;
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
         return "tbl_veranstaltungenAktion";
    }


    @Override
    String getFeldFuerInterneIdent() {
        return "identAktion";
    }

    @Override
    int getAnzFelder() {
        return 9;
    }


    @Override
    EclVeranstaltungenAktion decodeErgebnis(ResultSet pErgebnis) {
        EclVeranstaltungenAktion lEclReturn = new EclVeranstaltungenAktion();

        try {
            lEclReturn.identAktion = pErgebnis.getInt("identAktion");
            
            lEclReturn.gehoertZuElement = pErgebnis.getInt("gehoertZuElement");
            lEclReturn.gehoertZuElementDetail = pErgebnis.getInt("gehoertZuElementDetail");
            lEclReturn.position = pErgebnis.getInt("position");
            lEclReturn.aktion = pErgebnis.getInt("aktion");
            
            lEclReturn.parameter1 = pErgebnis.getString("parameter1");
            lEclReturn.parameter2 = pErgebnis.getString("parameter2");
            lEclReturn.parameter3 = pErgebnis.getString("parameter3");
            lEclReturn.parameter4 = pErgebnis.getString("parameter4");
           
       } catch (Exception e) {
            CaBug.drucke("001");
            System.err.println(" " + e.getMessage());
        }

        return lEclReturn;
    }



    @Override
    void fuellePreparedStatementKomplett(PreparedStatement pPStm, int pOffset, EclVeranstaltungenAktion pEcl) {
        int startOffset = pOffset; /*Nur erforderlich zum Überprüfen von Programmierfehlern - Feldanzahl*/

        try {
//          @formatter:off
            
            pPStm.setInt(pOffset, pEcl.identAktion);pOffset++;

            pPStm.setInt(pOffset, pEcl.gehoertZuElement);pOffset++;
            pPStm.setInt(pOffset, pEcl.gehoertZuElementDetail);pOffset++;
            pPStm.setInt(pOffset, pEcl.position);pOffset++;
            pPStm.setInt(pOffset, pEcl.aktion);pOffset++;
            
            pPStm.setString(pOffset, pEcl.parameter1);pOffset++;
            pPStm.setString(pOffset, pEcl.parameter2);pOffset++;
            pPStm.setString(pOffset, pEcl.parameter3);pOffset++;
            pPStm.setString(pOffset, pEcl.parameter4);pOffset++;

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
    private final String[] felder= {"identAktion", 
            "gehoertZuElement", "gehoertZuElementDetail", "position", "aktion", 
            "parameter1", "parameter2", "parameter3", "parameter4"
            }; 
//  @formatter:on
    private boolean initErfolgt=false;
    private void initFelder(){
        if (initErfolgt) {return;}
        initErfolgt=true;
    }

    @Override
    public int insert(EclVeranstaltungenAktion pEcl) {
        initFelder();
        return insertIntern(felder, pEcl);
    }


    /**Liest alle Demo-Kennungen ein*/
     public int readAll() {
        PreparedStatement lPStm = null;
        int anzInArray = 0;
        try {
            String lSql = "SELECT * from " + getSchema() + getTableName()+" "
                    + "ORDER BY gehoertZuElement, gehoertZuElementDetail, position";
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
//    public int update(EclVeranstaltungenAktion pEcl) {
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
