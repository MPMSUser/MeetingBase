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
import de.meetingapps.meetingportal.meetComHVParam.ParamStruktGruppen;

/**Hinweis: rueckfragen wird wie fragen behandelt*/

public class  DbParamStruktGruppen extends DbRoot<ParamStruktGruppen> {

//    private int logDrucken=10;
    
    public DbParamStruktGruppen(DbBundle pDbBundle) {
        super(pDbBundle);
    }

    
    @Override
    String getCreateString() {

        //      @formatter:off
        String createString = "CREATE TABLE " + getSchema() + getTableName()+" ( "
                + "`identGruppe` int NOT NULL DEFAULT 0, " 
                + "`lfdNr` int NOT NULL DEFAULT 0, " 
                
                + "`elementTyp` int NOT NULL DEFAULT 0, " 
                + "`identParamStrukt` int NOT NULL DEFAULT 0, "
                
                + "`textFeld` varchar(500) NOT NULL DEFAULT '', " 
                + "`darstellungsAttribut` int NOT NULL DEFAULT 0, " 

                + "`nurImExpertenmodusAnzeigen` int NOT NULL DEFAULT 0, " 
                + "`anzeigenWennModulAktiv` bigint(20) NOT NULL DEFAULT '0', "

                + "`button1Funktionscode` int NOT NULL DEFAULT 0, " 
                + "`button1Text` varchar(500) NOT NULL DEFAULT '', " 

                + "`button2Funktionscode` int NOT NULL DEFAULT 0, " 
                + "`button2Text` varchar(500) NOT NULL DEFAULT '', " 

                + "`elementHorizontalAnordnen` int NOT NULL DEFAULT 0, " 
                + "`anzeigeInSpalte` int NOT NULL DEFAULT 0, " 

               + "PRIMARY KEY (`identGruppe`, `lfdNr`) " + ") ";
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
         return "tbl_paramStruktGruppen";
    }


    @Override
    String getFeldFuerInterneIdent() {
        return "identGruppe";
    }

    @Override
    int getAnzFelder() {
        return 14;
    }


    @Override
    ParamStruktGruppen decodeErgebnis(ResultSet pErgebnis) {
        ParamStruktGruppen lEclReturn = new ParamStruktGruppen();

        try {
            lEclReturn.identGruppe = pErgebnis.getInt("identGruppe");
            lEclReturn.lfdNr = pErgebnis.getInt("lfdNr");
            
            lEclReturn.elementTyp = pErgebnis.getInt("elementTyp");
            lEclReturn.identParamStrukt = pErgebnis.getInt("identParamStrukt");
           
            lEclReturn.textFeld = pErgebnis.getString("textFeld");
            lEclReturn.darstellungsAttribut = pErgebnis.getInt("darstellungsAttribut");

            lEclReturn.nurImExpertenmodusAnzeigen = pErgebnis.getInt("nurImExpertenmodusAnzeigen");
            lEclReturn.anzeigenWennModulAktiv = pErgebnis.getLong("anzeigenWennModulAktiv");

            lEclReturn.button1Funktionscode = pErgebnis.getInt("button1Funktionscode");
            lEclReturn.button1Text = pErgebnis.getString("button1Text");

            lEclReturn.button2Funktionscode = pErgebnis.getInt("button2Funktionscode");
            lEclReturn.button2Text = pErgebnis.getString("button2Text");

            lEclReturn.elementHorizontalAnordnen = pErgebnis.getInt("elementHorizontalAnordnen");
            lEclReturn.anzeigeInSpalte = pErgebnis.getInt("anzeigeInSpalte");


       } catch (Exception e) {
            CaBug.drucke("001");
            System.err.println(" " + e.getMessage());
        }

        return lEclReturn;
    }



    @Override
    void fuellePreparedStatementKomplett(PreparedStatement pPStm, int pOffset, ParamStruktGruppen pEcl) {
        int startOffset = pOffset; /*Nur erforderlich zum Überprüfen von Programmierfehlern - Feldanzahl*/

        try {
//          @formatter:off
            
            pPStm.setInt(pOffset, pEcl.identGruppe);pOffset++;
            pPStm.setInt(pOffset, pEcl.lfdNr);pOffset++;
            
            pPStm.setInt(pOffset, pEcl.elementTyp);pOffset++;
            pPStm.setInt(pOffset, pEcl.identParamStrukt);pOffset++;

            pPStm.setString(pOffset, pEcl.textFeld);pOffset++;
            pPStm.setInt(pOffset, pEcl.darstellungsAttribut);pOffset++;

            pPStm.setInt(pOffset, pEcl.nurImExpertenmodusAnzeigen);pOffset++;
            pPStm.setLong(pOffset, pEcl.anzeigenWennModulAktiv);pOffset++;

            pPStm.setInt(pOffset, pEcl.button1Funktionscode);pOffset++;
            pPStm.setString(pOffset, pEcl.button1Text);pOffset++;

            pPStm.setInt(pOffset, pEcl.button2Funktionscode);pOffset++;
            pPStm.setString(pOffset, pEcl.button2Text);pOffset++;

            pPStm.setInt(pOffset, pEcl.elementHorizontalAnordnen);pOffset++;
            pPStm.setInt(pOffset, pEcl.anzeigeInSpalte);pOffset++;


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
    private final String[] felder= {"identGruppe", 
            "lfdNr", 
            "elementTyp", "identParamStrukt", 
            "textFeld", "darstellungsAttribut",
            "nurImExpertenmodusAnzeigen", "anzeigenWennModulAktiv",
            "button1Funktionscode", "button1Text",
            "button2Funktionscode", "button2Text",
            "elementHorizontalAnordnen", "anzeigeInSpalte"
            }; 
//  @formatter:on
    
    private boolean initErfolgt=false;
    private void initFelder(){
        if (initErfolgt) {return;}
        initErfolgt=true;
    }

    @Override
    public int insert(ParamStruktGruppen pEcl) {
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

     /**Liest alle Elemente einer Gruppe ein*/
     public int readGruppe(int pIdentGruppe) {
        PreparedStatement lPStm = null;
        int anzInArray = 0;
        try {
            String lSql = "SELECT * from " + getSchema() + getTableName()+" ";
            lSql = lSql + " WHERE identGruppe=? ORDER BY lfdNr;";
            lPStm = verbindung.prepareStatement(lSql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            lPStm.setInt(1, pIdentGruppe);
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
//    public int update(ParamStruktGruppen pEcl) {
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
