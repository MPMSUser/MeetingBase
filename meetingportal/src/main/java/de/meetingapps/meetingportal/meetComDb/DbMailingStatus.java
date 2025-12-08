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
import de.meetingapps.meetingportal.meetComEntities.EclMailingStatus;

/**Hinweis: rueckfragen wird wie fragen behandelt*/

public class  DbMailingStatus extends DbRoot<EclMailingStatus> {

//    private int logDrucken=10;
    
    public DbMailingStatus(DbBundle pDbBundle) {
        super(pDbBundle);
    }

    
    @Override
    String getCreateString() {

        //      @formatter:off
        String createString = "CREATE TABLE " + getSchema() + getTableName()+" ( "
                + "`mailingStatusIdent` int NOT NULL AUTO_INCREMENT, " 
                + "`mandant` int DEFAULT 0, "
                + "`db_version` bigint(20) DEFAULT 0, "
 
                + "`aktienregisterIdent` int DEFAULT 0, "

                + "`email` varchar(200) NOT NULL DEFAULT '', " 
                + "`job_id` varchar(200) NOT NULL DEFAULT '', " 
                + "`event_phase` varchar(200) NOT NULL DEFAULT '', " 
                + "`event_state` varchar(200) NOT NULL DEFAULT '', " 
                + "`event_type` varchar(200) NOT NULL DEFAULT '', " 
                + "`event_subType` varchar(200) NOT NULL DEFAULT '', " 
                + "`event_description` varchar(200) NOT NULL DEFAULT '', " 
                + "`timestamp` varchar(200) NOT NULL DEFAULT '', " 
                
               + "PRIMARY KEY (`mailingStatusIdent`) " + ") ";
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
         return "tbl_mailingStatus";
    }


    @Override
    String getFeldFuerInterneIdent() {
        return "mailingStatusIdent";
    }

    @Override
    int getAnzFelder() {
        return 12;
    }


    @Override
    EclMailingStatus decodeErgebnis(ResultSet pErgebnis) {
        EclMailingStatus lEclReturn = new EclMailingStatus();

        try {
            lEclReturn.mailingStatusIdent = pErgebnis.getInt("mailingStatusIdent");
            lEclReturn.mandant = pErgebnis.getInt("mandant");
            lEclReturn.db_version = pErgebnis.getLong("db_version");

            lEclReturn.aktienregisterIdent = pErgebnis.getInt("aktienregisterIdent");

            lEclReturn.email = pErgebnis.getString("email");
            lEclReturn.job_id = pErgebnis.getString("job_id");
            lEclReturn.event_phase = pErgebnis.getString("event_phase");
            lEclReturn.event_state = pErgebnis.getString("event_state");
            lEclReturn.event_type = pErgebnis.getString("event_type");
            lEclReturn.event_subType = pErgebnis.getString("event_subType");
            lEclReturn.event_description = pErgebnis.getString("event_description");
            lEclReturn.timestamp = pErgebnis.getString("timestamp");
            
            
       } catch (Exception e) {
            CaBug.drucke("001");
            System.err.println(" " + e.getMessage());
        }

        return lEclReturn;
    }



    @Override
    void fuellePreparedStatementKomplett(PreparedStatement pPStm, int pOffset, EclMailingStatus pEcl) {
        int startOffset = pOffset; /*Nur erforderlich zum Überprüfen von Programmierfehlern - Feldanzahl*/

        try {
//          @formatter:off
            
            pPStm.setInt(pOffset, pEcl.mailingStatusIdent);pOffset++;
            pPStm.setInt(pOffset, pEcl.mandant);pOffset++;
            pPStm.setLong(pOffset, pEcl.db_version);pOffset++;

            pPStm.setInt(pOffset, pEcl.aktienregisterIdent);pOffset++;

            pPStm.setString(pOffset, pEcl.email);pOffset++;
            pPStm.setString(pOffset, pEcl.job_id);pOffset++;
            pPStm.setString(pOffset, pEcl.event_phase);pOffset++;
            pPStm.setString(pOffset, pEcl.event_state);pOffset++;
            pPStm.setString(pOffset, pEcl.event_type);pOffset++;
            pPStm.setString(pOffset, pEcl.event_subType);pOffset++;
            pPStm.setString(pOffset, pEcl.event_description);pOffset++;
            pPStm.setString(pOffset, pEcl.timestamp);pOffset++;

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
            "mailingStatusIdent", "mandant", 
            "db_version", 
            "aktienregisterIdent", 
            "email", "job_id", "event_phase", "event_state", 
            "event_type", "event_subType", "event_description", "timestamp"
            }; 
//  @formatter:on
    private boolean initErfolgt=false;
    private void initFelder(){
        if (initErfolgt) {return;}
        initErfolgt=true;
    }

    @Override
    public int insert(EclMailingStatus pEcl) {
        initFelder();
        return insertIntern(felder, pEcl);
    }


    /**Liest alle Demo-Kennungen ein*/
     public int readAll() {
        PreparedStatement lPStm = null;
        int anzInArray = 0;
        try {
            String lSql = "SELECT * from " + getSchema() + getTableName()+" "
                    + "ORDER BY mailingStatusIdent";
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
//    public int update(EclMailingStatus pEcl) {
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
