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
import de.meetingapps.meetingportal.meetComEntities.EclVeranstaltungenMenue;

/**Hinweis: rueckfragen wird wie fragen behandelt*/

public class  DbVeranstaltungenMenue extends DbRoot<EclVeranstaltungenMenue> {

//    private int logDrucken=10;
    
    public DbVeranstaltungenMenue(DbBundle pDbBundle) {
        super(pDbBundle);
    }

    
    @Override
    String getCreateString() {

        //      @formatter:off
        String createString = "CREATE TABLE " + getSchema() + getTableName()+" ( "
                + "`identVeranstaltung` int NOT NULL DEFAULT 0, " 
                + "`menueNummer` int NOT NULL DEFAULT 0, " 
                + "`positionInMenue` int NOT NULL DEFAULT 0, " 
                + "`textInDiesemMenue` varchar(2000) NOT NULL DEFAULT '', " 
                + "`buttonAnzeigen` int NOT NULL DEFAULT 0, " 
               + "PRIMARY KEY (`identVeranstaltung`, `menueNummer`) " + ") ";
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
         return "tbl_veranstaltungenMenue";
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
    EclVeranstaltungenMenue decodeErgebnis(ResultSet pErgebnis) {
        EclVeranstaltungenMenue lEclReturn = new EclVeranstaltungenMenue();

        try {
            lEclReturn.identVeranstaltung = pErgebnis.getInt("identVeranstaltung");
            lEclReturn.menueNummer = pErgebnis.getInt("menueNummer");
            lEclReturn.positionInMenue = pErgebnis.getInt("positionInMenue");

            lEclReturn.textInDiesemMenue = pErgebnis.getString("textInDiesemMenue");
            lEclReturn.buttonAnzeigen = pErgebnis.getInt("buttonAnzeigen");
            
       } catch (Exception e) {
            CaBug.drucke("001");
            System.err.println(" " + e.getMessage());
        }

        return lEclReturn;
    }



    @Override
    void fuellePreparedStatementKomplett(PreparedStatement pPStm, int pOffset, EclVeranstaltungenMenue pEcl) {
        int startOffset = pOffset; /*Nur erforderlich zum Überprüfen von Programmierfehlern - Feldanzahl*/

        try {
//          @formatter:off
            
            pPStm.setInt(pOffset, pEcl.identVeranstaltung);pOffset++;
            pPStm.setInt(pOffset, pEcl.menueNummer);pOffset++;
            pPStm.setInt(pOffset, pEcl.positionInMenue);pOffset++;
             
            pPStm.setString(pOffset, pEcl.textInDiesemMenue);pOffset++;
            pPStm.setInt(pOffset, pEcl.buttonAnzeigen);pOffset++;

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
            "menueNummer", "positionInMenue",
            "textInDiesemMenue", "buttonAnzeigen"
            }; 
//  @formatter:on
    private boolean initErfolgt=false;
    private void initFelder(){
        if (initErfolgt) {return;}
        initErfolgt=true;
    }

    @Override
    public int insert(EclVeranstaltungenMenue pEcl) {
        initFelder();
        return insertIntern(felder, pEcl);
    }


     public int readAll() {
        PreparedStatement lPStm = null;
        int anzInArray = 0;
        try {
            String lSql = "SELECT * from " + getSchema() + getTableName()+" "
                    + "ORDER BY menueNummer, positionInMenue";
            lPStm = verbindung.prepareStatement(lSql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            anzInArray = readIntern(lPStm);

        } catch (Exception e) {
            CaBug.drucke("003");
            System.err.println(" " + e.getMessage());
            return (-1);
        }
        return (anzInArray);
    }

     public int readMenue(int pMenueNummer) {
         PreparedStatement lPStm = null;
         int anzInArray = 0;
         try {
             String lSql = "SELECT * from " + getSchema() + getTableName()
                     + " WHERE menueNummer=?"
                     + " ORDER BY menueNummer, positionInMenue";
             lPStm = verbindung.prepareStatement(lSql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
             lPStm.setInt(1, pMenueNummer);
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
//    public int update(EclVeranstaltungenMenue pEcl) {
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
