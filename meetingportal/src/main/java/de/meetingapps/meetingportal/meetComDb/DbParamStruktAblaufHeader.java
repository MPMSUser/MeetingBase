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
import de.meetingapps.meetingportal.meetComHVParam.ParamStruktAblaufHeader;

/**Hinweis: rueckfragen wird wie fragen behandelt*/

public class  DbParamStruktAblaufHeader extends DbRoot<ParamStruktAblaufHeader> {

//    private int logDrucken=10;
    
    public DbParamStruktAblaufHeader(DbBundle pDbBundle) {
        super(pDbBundle);
    }

    
    @Override
    String getCreateString() {

        //      @formatter:off
        String createString = "CREATE TABLE " + getSchema() + getTableName()+" ( "
                + "`identAblauf` int NOT NULL DEFAULT 0, " 

                + "`ablaufArt` int NOT NULL DEFAULT 0, " 
                + "`lfdNr` int NOT NULL DEFAULT 0, " 
                
                + "`kurzBeschreibung` varchar(50) NOT NULL DEFAULT '', " 
                + "`beschreibung` varchar(2000) NOT NULL DEFAULT '', " 
                
                + "`inAblaufauswahlEnthalten` bigint(20) NOT NULL DEFAULT 0, " 
                + "`fuerAblaufauswahlGesperrt` int NOT NULL DEFAULT 0, " 

                + "`anzeigenWennBerechtigungVorhanden` bigint(20) NOT NULL DEFAULT '0', "

               + "PRIMARY KEY (`identAblauf`) " + ") ";
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
         return "tbl_paramStruktAblaufHeader";
    }


    @Override
    String getFeldFuerInterneIdent() {
        return "identAblauf";
    }

    @Override
    int getAnzFelder() {
        return 8;
    }


    @Override
    ParamStruktAblaufHeader decodeErgebnis(ResultSet pErgebnis) {
        ParamStruktAblaufHeader lEclReturn = new ParamStruktAblaufHeader();

        try {
            lEclReturn.identAblauf = pErgebnis.getInt("identAblauf");

            lEclReturn.ablaufArt = pErgebnis.getInt("ablaufArt");
            lEclReturn.lfdNr = pErgebnis.getInt("lfdNr");

            lEclReturn.kurzBeschreibung = pErgebnis.getString("kurzBeschreibung");
            lEclReturn.beschreibung = pErgebnis.getString("beschreibung");
            
            lEclReturn.inAblaufauswahlEnthalten = pErgebnis.getLong("inAblaufauswahlEnthalten");
            lEclReturn.fuerAblaufauswahlGesperrt = pErgebnis.getInt("fuerAblaufauswahlGesperrt");
            
            lEclReturn.anzeigenWennBerechtigungVorhanden = pErgebnis.getLong("anzeigenWennBerechtigungVorhanden");

       } catch (Exception e) {
            CaBug.drucke("001");
            System.err.println(" " + e.getMessage());
        }

        return lEclReturn;
    }



    @Override
    void fuellePreparedStatementKomplett(PreparedStatement pPStm, int pOffset, ParamStruktAblaufHeader pEcl) {
        int startOffset = pOffset; /*Nur erforderlich zum Überprüfen von Programmierfehlern - Feldanzahl*/

        try {
//          @formatter:off
            
            pPStm.setInt(pOffset, pEcl.identAblauf);pOffset++;

            pPStm.setInt(pOffset, pEcl.ablaufArt);pOffset++;
            pPStm.setInt(pOffset, pEcl.lfdNr);pOffset++;

            pPStm.setString(pOffset, pEcl.kurzBeschreibung);pOffset++;
            pPStm.setString(pOffset, pEcl.beschreibung);pOffset++;
            
            pPStm.setLong(pOffset, pEcl.inAblaufauswahlEnthalten);pOffset++;
            pPStm.setInt(pOffset, pEcl.fuerAblaufauswahlGesperrt);pOffset++;

            pPStm.setLong(pOffset, pEcl.anzeigenWennBerechtigungVorhanden);pOffset++;

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
    private final String[] felder= {"identAblauf", 
            "ablaufArt", "lfdNr", 
            "kurzBeschreibung", "beschreibung", 
            "inAblaufauswahlEnthalten", "fuerAblaufauswahlGesperrt",
            "anzeigenWennBerechtigungVorhanden"
            }; 
//  @formatter:on
    
    private boolean initErfolgt=false;
    private void initFelder(){
        if (initErfolgt) {return;}
        initErfolgt=true;
    }

    @Override
    public int insert(ParamStruktAblaufHeader pEcl) {
        initFelder();
        return insertIntern(felder, pEcl);
    }


//    /**Liest alle Elemente einer Gruppe ein*/
//     public int readGruppe(int pIdent) {
//        PreparedStatement lPStm = null;
//        int anzInArray = 0;
//        try {
//            String lSql = "SELECT * from " + getSchema() + getTableName()+" "
//                    + "ORDER BY gehoertZuElement, gehoertZuElementDetail, position";
//            lPStm = verbindung.prepareStatement(lSql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
//            anzInArray = readIntern(lPStm);
//
//        } catch (Exception e) {
//            CaBug.drucke("003");
//            System.err.println(" " + e.getMessage());
//            return (-1);
//        }
//        return (anzInArray);
//    }

     /**Liest Header-Element eines Ablaufs ein*/
     public int readAblauf(int pIdentAblauf) {
        PreparedStatement lPStm = null;
        int anzInArray = 0;
        try {
            String lSql = "SELECT * from " + getSchema() + getTableName()+" ";
            lSql = lSql + " WHERE identAblauf=?;";
            lPStm = verbindung.prepareStatement(lSql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            lPStm.setInt(1, pIdentAblauf);
           anzInArray = readIntern(lPStm);

        } catch (Exception e) {
            CaBug.drucke("001");
            System.err.println(" " + e.getMessage());
            return (-1);
        }
        return (anzInArray);
    }

     
     /**Liest alle Elemente einer Gruppe ein*/
     public int readAlleFuerAuswahlAbstimmungsablauf() {
        PreparedStatement lPStm = null;
        int anzInArray = 0;
        try {
            String lSql = "SELECT * from " + getSchema() + getTableName()+" ";
            lSql = lSql + " WHERE fuerAblaufauswahlGesperrt=0 AND ablaufArt=1 ORDER BY lfdNr;";
            lPStm = verbindung.prepareStatement(lSql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
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
//    public int update(ParamStruktAblaufHeader pEcl) {
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
