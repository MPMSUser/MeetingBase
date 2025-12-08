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
import java.util.LinkedList;
import java.util.List;

import de.meetingapps.meetingportal.meetComAllg.CaBug;
import de.meetingapps.meetingportal.meetComEntities.EclVeranstaltungenWert;

/**Hinweis: rueckfragen wird wie fragen behandelt*/

public class  DbVeranstaltungenWert extends DbRoot<EclVeranstaltungenWert> {

    private int logDrucken=10;
    
    public DbVeranstaltungenWert(DbBundle pDbBundle) {
        super(pDbBundle);
    }

    
    @Override
    String getCreateString() {

        //      @formatter:off
        String createString = "CREATE TABLE " + getSchema() + getTableName()+" ( "
                + "`loginKennung` char(200) NOT NULL DEFAULT '', " 
                + "`identVeranstaltung` int NOT NULL DEFAULT 0, " 
                + "`identElement` int NOT NULL DEFAULT 0, " 
                + "`identDetail` int NOT NULL DEFAULT 0, " 
                + "`identAktion` int NOT NULL DEFAULT 0, " 
                + "`eingabeWert` varchar(1000) NOT NULL DEFAULT '', " 
                + "`ergebnisNrInAKtion` int NOT NULL DEFAULT 0, " 
                + "`ergebnisDerAktion` varchar(100) NOT NULL DEFAULT '', " 
                + "`parameter1` varchar(200) NOT NULL DEFAULT '', " 
                + "`parameter2` varchar(200) NOT NULL DEFAULT '', " 
                + "`parameter3` varchar(200) NOT NULL DEFAULT '', " 
                + "`parameter4` varchar(200) NOT NULL DEFAULT '', " 
               + "PRIMARY KEY (`loginKennung`, `identVeranstaltung`, `identElement`, `identDetail`, `identAktion`, `ergebnisNrInAKtion`) " + ") ";
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
         return "tbl_veranstaltungenWert";
    }


    @Override
    String getFeldFuerInterneIdent() {
        return "";
    }

    @Override
    int getAnzFelder() {
        return 12;
    }


    @Override
    EclVeranstaltungenWert decodeErgebnis(ResultSet pErgebnis) {
        EclVeranstaltungenWert lEclReturn = new EclVeranstaltungenWert();

        try {
            lEclReturn.loginKennung = pErgebnis.getString("loginKennung");
            
            lEclReturn.identVeranstaltung = pErgebnis.getInt("identVeranstaltung");
            lEclReturn.identElement = pErgebnis.getInt("identElement");
            lEclReturn.identDetail = pErgebnis.getInt("identDetail");
            lEclReturn.identAktion = pErgebnis.getInt("identAktion");

            lEclReturn.eingabeWert = pErgebnis.getString("eingabeWert");

            lEclReturn.ergebnisNrInAKtion = pErgebnis.getInt("ergebnisNrInAKtion");
            lEclReturn.ergebnisDerAktion = pErgebnis.getString("ergebnisDerAktion");

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
    void fuellePreparedStatementKomplett(PreparedStatement pPStm, int pOffset, EclVeranstaltungenWert pEcl) {
        int startOffset = pOffset; /*Nur erforderlich zum Überprüfen von Programmierfehlern - Feldanzahl*/

        try {
//          @formatter:off

            pPStm.setString(pOffset, pEcl.loginKennung);pOffset++;

            pPStm.setInt(pOffset, pEcl.identVeranstaltung);pOffset++;
            pPStm.setInt(pOffset, pEcl.identElement);pOffset++;
            pPStm.setInt(pOffset, pEcl.identDetail);pOffset++;
            pPStm.setInt(pOffset, pEcl.identAktion);pOffset++;

            pPStm.setString(pOffset, pEcl.eingabeWert);pOffset++;

            pPStm.setInt(pOffset, pEcl.ergebnisNrInAKtion);pOffset++;

            pPStm.setString(pOffset, pEcl.ergebnisDerAktion);pOffset++;

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
    private final String[] felder= {"loginKennung", 
            "identVeranstaltung", "identElement", "identDetail", "identAktion",
            "eingabeWert", 
            "ergebnisNrInAKtion", "ergebnisDerAktion", 
            "parameter1", "parameter2", "parameter3", "parameter4"
            }; 
//  @formatter:on
    private boolean initErfolgt=false;
    private void initFelder(){
        if (initErfolgt) {return;}
        initErfolgt=true;
    }

    @Override
    public int insert(EclVeranstaltungenWert pEcl) {
        initFelder();
        return insertIntern(felder, pEcl);
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

     /**Liest alle Werte für alle Login-Kennungen ein*/
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

     /**Liest alle Werte für alle Login-Kennung einer Veranstaltung ein*/
     public int readAllVeranstaltung(int pIdentVeranstaltung) {
        PreparedStatement lPStm = null;
        int anzInArray = 0;
        try {
            String lSql = "SELECT * from " + getSchema() + getTableName()+" WHERE identVeranstaltung=? "
                    + "ORDER BY loginKennung, identVeranstaltung, identElement, identDetail, identAktion, ergebnisNrInAKtion  ";
            lPStm = verbindung.prepareStatement(lSql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            lPStm.setInt(1, pIdentVeranstaltung);
            anzInArray = readIntern(lPStm);

        } catch (Exception e) {
            CaBug.drucke("003");
            System.err.println(" " + e.getMessage());
            return (-1);
        }
        return (anzInArray);
    }

     
     
     /**Löscht die Einträge für die loginKennung und schreibt die neuen*/
     public int updateLoginKennung(String pLoginKennung, List<EclVeranstaltungenWert> pEcl) {
         List<Integer> veranstaltungsIdentListe=new LinkedList<Integer>();
         for (int i=0;i<pEcl.size();i++) {
             int veranstaltungIdent=pEcl.get(i).identVeranstaltung;
             int gef=-1;
             for (int i1=0;i1<veranstaltungsIdentListe.size();i1++) {
                 if (veranstaltungsIdentListe.get(i1)==veranstaltungIdent) {
                     gef=i1;
                 }
             }
             if (gef==-1) {
                 veranstaltungsIdentListe.add(veranstaltungIdent);
                 CaBug.druckeLog("VeranstaltungIdent löschen="+veranstaltungIdent, logDrucken, 10);
                 delete(pLoginKennung, veranstaltungIdent);
             }
         }
         
         int ergebnis=0;
         initFelder();

         if (pEcl!=null) {
             for (int i=0;i<pEcl.size();i++) {
                 pEcl.get(i).loginKennung=pLoginKennung;
                 insert(pEcl.get(i));
             }
         }


         return ergebnis;
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
//    public int update(EclVeranstaltungenWert pEcl) {
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
    public int delete(String pLoginKennung, int pVeranstaltungIdent) {
        int ergebnis=0;
        try {
            String sql = "DELETE FROM " + getSchema()+getTableName()+
                    " WHERE loginKennung=? AND identVeranstaltung=? ";
            PreparedStatement pstm1 = verbindung.prepareStatement(sql);
            pstm1.setString(1, pLoginKennung);
            pstm1.setInt(2, pVeranstaltungIdent);
            ergebnis = deleteIntern(pstm1);
        } catch (Exception e1) {
            CaBug.drucke("001");
            System.err.println(" " + e1.getMessage());
            return (-1);
        }

        return ergebnis;
    }



}
