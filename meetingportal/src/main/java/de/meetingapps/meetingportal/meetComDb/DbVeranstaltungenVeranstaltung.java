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
import de.meetingapps.meetingportal.meetComEntities.EclVeranstaltungenVeranstaltung;

/**Hinweis: rueckfragen wird wie fragen behandelt*/

public class  DbVeranstaltungenVeranstaltung extends DbRoot<EclVeranstaltungenVeranstaltung> {

//    private int logDrucken=10;
    
    public DbVeranstaltungenVeranstaltung(DbBundle pDbBundle) {
        super(pDbBundle);
    }

    
    @Override
    String getCreateString() {

        //      @formatter:off
        String createString = "CREATE TABLE " + getSchema() + getTableName()+" ( "
                + "`identVeranstaltung` int NOT NULL DEFAULT 0, " 
                + "`menueNummer1` int NOT NULL DEFAULT 0, " 
                + "`positionInMenue1` int NOT NULL DEFAULT 0, " 
                + "`menueNummer2` int NOT NULL DEFAULT 0, " 
                + "`positionInMenue2` int NOT NULL DEFAULT 0, " 
                + "`menueNummer3` int NOT NULL DEFAULT 0, " 
                + "`positionInMenue3` int NOT NULL DEFAULT 0, " 
                + "`beschreibung` varchar(1000) NOT NULL DEFAULT '', " 
                + "`textIntern` varchar(100) NOT NULL DEFAULT '', " 
                + "`aktivierungsStatus` int NOT NULL DEFAULT 0, " 
               + "PRIMARY KEY (`identVeranstaltung`) " + ") ";
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
         return "tbl_veranstaltungenVeranstaltung";
    }


    @Override
    String getFeldFuerInterneIdent() {
        return "identVeranstaltung";
    }

    @Override
    int getAnzFelder() {
        return 10;
    }


    @Override
    EclVeranstaltungenVeranstaltung decodeErgebnis(ResultSet pErgebnis) {
        EclVeranstaltungenVeranstaltung lEclReturn = new EclVeranstaltungenVeranstaltung();

        try {
            lEclReturn.identVeranstaltung = pErgebnis.getInt("identVeranstaltung");
            
            lEclReturn.menueNummer1 = pErgebnis.getInt("menueNummer1");
            lEclReturn.positionInMenue1 = pErgebnis.getInt("positionInMenue1");
            lEclReturn.menueNummer2 = pErgebnis.getInt("menueNummer2");
            lEclReturn.positionInMenue2 = pErgebnis.getInt("positionInMenue2");
            lEclReturn.menueNummer3 = pErgebnis.getInt("menueNummer3");
            lEclReturn.positionInMenue3 = pErgebnis.getInt("positionInMenue3");
            
            lEclReturn.beschreibung = pErgebnis.getString("beschreibung");
            lEclReturn.textIntern = pErgebnis.getString("textIntern");
            
            lEclReturn.aktivierungsStatus = pErgebnis.getInt("aktivierungsStatus");

       } catch (Exception e) {
            CaBug.drucke("001");
            System.err.println(" " + e.getMessage());
        }

        return lEclReturn;
    }



    @Override
    void fuellePreparedStatementKomplett(PreparedStatement pPStm, int pOffset, EclVeranstaltungenVeranstaltung pEcl) {
        int startOffset = pOffset; /*Nur erforderlich zum Überprüfen von Programmierfehlern - Feldanzahl*/

        try {
//          @formatter:off
            
            pPStm.setInt(pOffset, pEcl.identVeranstaltung);pOffset++;
            
            pPStm.setInt(pOffset, pEcl.menueNummer1);pOffset++;
            pPStm.setInt(pOffset, pEcl.positionInMenue1);pOffset++;
            pPStm.setInt(pOffset, pEcl.menueNummer2);pOffset++;
            pPStm.setInt(pOffset, pEcl.positionInMenue2);pOffset++;
            pPStm.setInt(pOffset, pEcl.menueNummer3);pOffset++;
            pPStm.setInt(pOffset, pEcl.positionInMenue3);pOffset++;
            
            pPStm.setString(pOffset, pEcl.beschreibung);pOffset++;
            pPStm.setString(pOffset, pEcl.textIntern);pOffset++;

            pPStm.setInt(pOffset, pEcl.aktivierungsStatus);pOffset++;

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
    private final String[] felder= {"identVeranstaltung", 
            "menueNummer1", "positionInMenue1", 
            "menueNummer2", "positionInMenue2", 
            "menueNummer3", "positionInMenue3", 
            "beschreibung", "textIntern", 
            "aktivierungsStatus"
            }; 
//  @formatter:on
    private boolean initErfolgt=false;
    private void initFelder(){
        if (initErfolgt) {return;}
        initErfolgt=true;
    }

    @Override
    public int insert(EclVeranstaltungenVeranstaltung pEcl) {
        initFelder();
        return insertIntern(felder, pEcl);
    }


    /**Liest alle Demo-Kennungen ein*/
     public int readAll() {
        PreparedStatement lPStm = null;
        int anzInArray = 0;
        try {
            String lSql = "SELECT * from " + getSchema() + getTableName()+" ";
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
//    public int update(EclVeranstaltungenVeranstaltung pEcl) {
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
