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
import de.meetingapps.meetingportal.meetComEntities.EclVeranstaltungenReportElement;

/**Hinweis: rueckfragen wird wie fragen behandelt*/

public class  DbVeranstaltungenReportElement extends DbRoot<EclVeranstaltungenReportElement> {

//    private int logDrucken=10;
    
    public DbVeranstaltungenReportElement(DbBundle pDbBundle) {
        super(pDbBundle);
    }

    
    @Override
    String getCreateString() {

        //      @formatter:off
        String createString = "CREATE TABLE " + getSchema() + getTableName()+" ( "
                + "`identReportElement` int NOT NULL DEFAULT 0, " 
                + "`identReportSubElement` int NOT NULL DEFAULT 0, " 

                + "`identReport` int NOT NULL DEFAULT 0, " 
                + "`identVeranstaltung` int NOT NULL DEFAULT 0, "
                
                + "`aktiv` int NOT NULL DEFAULT 0, "
                                
                + "`elementTyp` int NOT NULL DEFAULT 0, " 
                + "`offsetInReport` int NOT NULL DEFAULT 0, " 
                + "`elementBezeichnung` varchar(50) NOT NULL DEFAULT '', " 
                + "`wertBerechnung` int NOT NULL DEFAULT 0, " 
                + "`identElement` int NOT NULL DEFAULT 0, " 
                + "`identDetail` int NOT NULL DEFAULT 0, " 
                + "`identAktion` int NOT NULL DEFAULT 0, " 
                + "`wertAusQuittung` varchar(1000) NOT NULL DEFAULT '', " 
                + "`wertInReport` varchar(1000) NOT NULL DEFAULT '', " 
               + "PRIMARY KEY (`identReportElement`, `identReportSubElement`, `identReport`, `identVeranstaltung`) " + ") ";
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
         return "tbl_veranstaltungenReportElement";
    }


    @Override
    String getFeldFuerInterneIdent() {
        return "";
    }

    @Override
    int getAnzFelder() {
        return 14;
    }


    @Override
    EclVeranstaltungenReportElement decodeErgebnis(ResultSet pErgebnis) {
        EclVeranstaltungenReportElement lEclReturn = new EclVeranstaltungenReportElement();

        try {
            
            lEclReturn.identReportElement = pErgebnis.getInt("identReportElement");
            lEclReturn.identReportSubElement = pErgebnis.getInt("identReportSubElement");
            lEclReturn.identReport = pErgebnis.getInt("identReport");
            lEclReturn.identVeranstaltung = pErgebnis.getInt("identVeranstaltung");

            lEclReturn.aktiv = pErgebnis.getInt("aktiv");

            lEclReturn.elementTyp = pErgebnis.getInt("elementTyp");
            lEclReturn.offsetInReport = pErgebnis.getInt("offsetInReport");

            lEclReturn.elementBezeichnung = pErgebnis.getString("elementBezeichnung");

            lEclReturn.wertBerechnung = pErgebnis.getInt("wertBerechnung");
            lEclReturn.identElement = pErgebnis.getInt("identElement");
            lEclReturn.identDetail = pErgebnis.getInt("identDetail");
            lEclReturn.identAktion = pErgebnis.getInt("identAktion");

            lEclReturn.wertAusQuittung = pErgebnis.getString("wertAusQuittung");
            lEclReturn.wertInReport = pErgebnis.getString("wertInReport");
            
       } catch (Exception e) {
            CaBug.drucke("001");
            System.err.println(" " + e.getMessage());
        }

        return lEclReturn;
    }



    @Override
    void fuellePreparedStatementKomplett(PreparedStatement pPStm, int pOffset, EclVeranstaltungenReportElement pEcl) {
        int startOffset = pOffset; /*Nur erforderlich zum Überprüfen von Programmierfehlern - Feldanzahl*/

        try {
//          @formatter:off

            pPStm.setInt(pOffset, pEcl.identReportElement);pOffset++;
            pPStm.setInt(pOffset, pEcl.identReportSubElement);pOffset++;
            pPStm.setInt(pOffset, pEcl.identReport);pOffset++;
            pPStm.setInt(pOffset, pEcl.identVeranstaltung);pOffset++;
            
            pPStm.setInt(pOffset, pEcl.aktiv);pOffset++;
            
            pPStm.setInt(pOffset, pEcl.elementTyp);pOffset++;
            pPStm.setInt(pOffset, pEcl.offsetInReport);pOffset++;

            pPStm.setString(pOffset, pEcl.elementBezeichnung);pOffset++;

            pPStm.setInt(pOffset, pEcl.wertBerechnung);pOffset++;
            pPStm.setInt(pOffset, pEcl.identElement);pOffset++;
            pPStm.setInt(pOffset, pEcl.identDetail);pOffset++;
            pPStm.setInt(pOffset, pEcl.identAktion);pOffset++;

            pPStm.setString(pOffset, pEcl.wertAusQuittung);pOffset++;
            pPStm.setString(pOffset, pEcl.wertInReport);pOffset++;
            
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
            "identReportElement", "identReportSubElement", "identReport", "identVeranstaltung",
            "aktiv", "elementTyp", "offsetInReport",
            "elementBezeichnung", 
            "wertBerechnung", "identElement", "identDetail", "identAktion",
            "wertAusQuittung", "wertInReport"
            }; 
//  @formatter:on
    private boolean initErfolgt=false;
    private void initFelder(){
        if (initErfolgt) {return;}
        initErfolgt=true;
    }

    @Override
    public int insert(EclVeranstaltungenReportElement pEcl) {
        initFelder();
        return insertIntern(felder, pEcl);
    }

    
     public int readAlleAktivenReportsFuerAuswahl() {
        PreparedStatement lPStm = null;
        int anzInArray = 0;
        try {
            String lSql = "SELECT * from " + getSchema() + getTableName()+" "
                    + "WHERE aktiv=1 AND elementTyp=0 ";
            lSql=lSql+" ORDER BY identVeranstaltung, identReport ";
            lPStm = verbindung.prepareStatement(lSql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            anzInArray = readIntern(lPStm);

        } catch (Exception e) {
            CaBug.drucke("003");
            System.err.println(" " + e.getMessage());
            return (-1);
        }
        return (anzInArray);
    }

     
     /**Liest alle Werte für eine Login-Kennung ein.
      * pIdentVeranstaltung==-1 => für alle Veranstaltungen
      * >=0 => nur für diese Veranstaltung*/
      public int readReportIdent(int pIdentReport, int pIdentVeranstaltung) {
         PreparedStatement lPStm = null;
         int anzInArray = 0;
         try {
             String lSql = "SELECT * from " + getSchema() + getTableName()+" "
                     + "WHERE aktiv=1 AND identReport=? AND identVeranstaltung=? "
                     + "ORDER BY offsetInReport, identReportElement, identReportSubElement";
             lPStm = verbindung.prepareStatement(lSql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
             lPStm.setInt(1, pIdentReport);
             lPStm.setInt(2, pIdentVeranstaltung);
             anzInArray = readIntern(lPStm);

         } catch (Exception e) {
             CaBug.drucke("003");
             System.err.println(" " + e.getMessage());
             return (-1);
         }
         return (anzInArray);
     }
   

    /**Liest alle Werte für eine Login-Kennung ein.
     * pIdentVeranstaltung==-1 => für alle Veranstaltungen
     * >=0 => nur für diese Veranstaltung*/
     public int readLoginKennung(String pLoginKennung, int pIdentVeranstaltung) {
        PreparedStatement lPStm = null;
        int anzInArray = 0;
        try {
            String lSql = "SELECT * from " + getSchema() + getTableName()+" "
                    + "WHERE loginKennung=? ";
            if (pIdentVeranstaltung!=-1) {
                lSql=lSql+" AND identVeranstaltung=? ";
            }
            lSql=lSql+" ORDER BY identVeranstaltung, identElement, identDetail, identAktion, ergebnisNrInAKtion  ";
            lPStm = verbindung.prepareStatement(lSql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            lPStm.setString(1, pLoginKennung);
            if (pIdentVeranstaltung!=-1) {
                lPStm.setInt(2, pIdentVeranstaltung);
            }
            anzInArray = readIntern(lPStm);

        } catch (Exception e) {
            CaBug.drucke("003");
            System.err.println(" " + e.getMessage());
            return (-1);
        }
        return (anzInArray);
    }

     /**Liest alle Werte für eine Login-Kennung ein*/
     public int readAll() {
        PreparedStatement lPStm = null;
        int anzInArray = 0;
        try {
            String lSql = "SELECT * from " + getSchema() + getTableName()+" "
                    + "ORDER BY loginKennung, identVeranstaltung, identElement, identDetail, identAktion, ergebnisNrInAKtion  ";
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
//    public int update(EclVeranstaltungenReportElement pEcl) {
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
    /**Return-Werte:
     * pfXyWurdeVonAnderemBenutzerVeraendert
     * -1 => undefinierter Fehler
     * 1 => Löschen erfolgreich
     */
//    public int delete(String pLoginKennung, int pVeranstaltungIdent) {
//        int ergebnis=0;
//        try {
//            String sql = "DELETE FROM " + getSchema()+getTableName()+
//                    " WHERE loginKennung=? AND identVeranstaltung=? ";
//            PreparedStatement pstm1 = verbindung.prepareStatement(sql);
//            pstm1.setString(1, pLoginKennung);
//            pstm1.setInt(2, pVeranstaltungIdent);
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
