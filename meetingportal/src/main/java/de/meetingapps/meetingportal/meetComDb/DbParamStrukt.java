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
import de.meetingapps.meetingportal.meetComHVParam.ParamStrukt;

/**Hinweis: rueckfragen wird wie fragen behandelt*/

public class  DbParamStrukt extends DbRoot<ParamStrukt> {

//    private int logDrucken=10;
    
    public DbParamStrukt(DbBundle pDbBundle) {
        super(pDbBundle);
    }

    
    @Override
    String getCreateString() {

        //      @formatter:off
        String createString = "CREATE TABLE " + getSchema() + getTableName()+" ( "
                + "`ident` int NOT NULL DEFAULT 0, " 
                + "`parameterTable` int NOT NULL DEFAULT 0, " 
                + "`parameterOffsetInTable` int NOT NULL DEFAULT 0, " 
                + "`parameterSubOffsetInTable` int NOT NULL DEFAULT 0, " 
                + "`parameterLaengeInTable` int NOT NULL DEFAULT 0, " 

                + "`parameterGehoertZuPresetArt` int NOT NULL DEFAULT 0, " 

                + "`wertIstTyp` int NOT NULL DEFAULT 0, " 
                + "`pruefroutineAusfuehren` int NOT NULL DEFAULT 0, " 

                + "`pruefenString` int NOT NULL DEFAULT 0, " 
                + "`minimaleLaengeString` int NOT NULL DEFAULT 0, " 
                + "`maximaleLaengeString` int NOT NULL DEFAULT 0, " 
                + "`zulaessigeZeichenString` varchar(1000) NOT NULL DEFAULT '', " 

                + "`pruefenInt` int NOT NULL DEFAULT 0, " 
                + "`minimalWertInt` int NOT NULL DEFAULT 0, " 
                + "`maximalWertInt` int NOT NULL DEFAULT 0, " 
                + "`zulaessigeZiffernInt` varchar(100) NOT NULL DEFAULT '', " 

                + "`werteLesenAus` int NOT NULL DEFAULT 0, " 

                + "`anzeigeForm` int NOT NULL DEFAULT 0, " 
                + "`eingabeLaenge` int NOT NULL DEFAULT 0, " 
                + "`feldLaenge` int NOT NULL DEFAULT 0, " 
                + "`bezeichnungVorEingabefeld` varchar(100) NOT NULL DEFAULT '', " 
                + "`bezeichnungNachEingabefeld` varchar(1000) NOT NULL DEFAULT '', " 
 
                + "`hilfetext` varchar(5000) NOT NULL DEFAULT '', " 

                + "`parameterName` varchar(80) NOT NULL DEFAULT '', " 

                + "`parameterTutNichts` int NOT NULL DEFAULT 0, " 
                + "`parameterIstFuerAllgemeineVerwendungFreigegeben` int NOT NULL DEFAULT 0, " 

               + "PRIMARY KEY (`ident`) " + ") ";
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
         return "tbl_paramStrukt";
    }


    @Override
    String getFeldFuerInterneIdent() {
        return "ident";
    }

    @Override
    int getAnzFelder() {
        return 26;
    }


    @Override
    ParamStrukt decodeErgebnis(ResultSet pErgebnis) {
        ParamStrukt lEclReturn = new ParamStrukt();

        try {
            lEclReturn.ident = pErgebnis.getInt("ident");
            
            lEclReturn.parameterTable = pErgebnis.getInt("parameterTable");
            lEclReturn.parameterOffsetInTable = pErgebnis.getInt("parameterOffsetInTable");
            lEclReturn.parameterSubOffsetInTable = pErgebnis.getInt("parameterSubOffsetInTable");
            lEclReturn.parameterLaengeInTable = pErgebnis.getInt("parameterLaengeInTable");
 
            lEclReturn.parameterGehoertZuPresetArt = pErgebnis.getInt("parameterGehoertZuPresetArt");

            lEclReturn.wertIstTyp = pErgebnis.getInt("wertIstTyp");
            lEclReturn.pruefroutineAusfuehren = pErgebnis.getInt("pruefroutineAusfuehren");
            
            lEclReturn.pruefenString = pErgebnis.getInt("pruefenString");
            lEclReturn.minimaleLaengeString = pErgebnis.getInt("minimaleLaengeString");
            lEclReturn.maximaleLaengeString = pErgebnis.getInt("maximaleLaengeString");
            lEclReturn.zulaessigeZeichenString = pErgebnis.getString("zulaessigeZeichenString");
 
            lEclReturn.pruefenInt = pErgebnis.getInt("pruefenInt");

            lEclReturn.pruefenInt = pErgebnis.getInt("pruefenInt");
            lEclReturn.minimalWertInt = pErgebnis.getInt("minimalWertInt");
            lEclReturn.maximalWertInt = pErgebnis.getInt("maximalWertInt");
            lEclReturn.zulaessigeZiffernInt = pErgebnis.getString("zulaessigeZiffernInt");
 
            lEclReturn.werteLesenAus = pErgebnis.getInt("werteLesenAus");

            lEclReturn.anzeigeForm = pErgebnis.getInt("anzeigeForm");
            lEclReturn.eingabeLaenge = pErgebnis.getInt("eingabeLaenge");
            lEclReturn.feldLaenge = pErgebnis.getInt("feldLaenge");
            lEclReturn.bezeichnungVorEingabefeld = pErgebnis.getString("bezeichnungVorEingabefeld");
            lEclReturn.bezeichnungNachEingabefeld = pErgebnis.getString("bezeichnungNachEingabefeld");
            
            lEclReturn.hilfetext = pErgebnis.getString("hilfetext");
           
            lEclReturn.parameterTutNichts = pErgebnis.getInt("parameterTutNichts");
            lEclReturn.parameterIstFuerAllgemeineVerwendungFreigegeben = pErgebnis.getInt("parameterIstFuerAllgemeineVerwendungFreigegeben");

       } catch (Exception e) {
            CaBug.drucke("001");
            System.err.println(" " + e.getMessage());
        }

        return lEclReturn;
    }



    @Override
    void fuellePreparedStatementKomplett(PreparedStatement pPStm, int pOffset, ParamStrukt pEcl) {
        int startOffset = pOffset; /*Nur erforderlich zum Überprüfen von Programmierfehlern - Feldanzahl*/

        try {
//          @formatter:off
            
            pPStm.setInt(pOffset, pEcl.ident);pOffset++;

            pPStm.setInt(pOffset, pEcl.parameterTable);pOffset++;
            pPStm.setInt(pOffset, pEcl.parameterOffsetInTable);pOffset++;
            pPStm.setInt(pOffset, pEcl.parameterSubOffsetInTable);pOffset++;
            pPStm.setInt(pOffset, pEcl.parameterLaengeInTable);pOffset++;

            pPStm.setInt(pOffset, pEcl.parameterGehoertZuPresetArt);pOffset++;

            pPStm.setInt(pOffset, pEcl.wertIstTyp);pOffset++;
            pPStm.setInt(pOffset, pEcl.pruefroutineAusfuehren);pOffset++;
            
            pPStm.setInt(pOffset, pEcl.pruefenString);pOffset++;
            pPStm.setInt(pOffset, pEcl.minimaleLaengeString);pOffset++;
            pPStm.setInt(pOffset, pEcl.maximaleLaengeString);pOffset++;
            pPStm.setString(pOffset, pEcl.zulaessigeZeichenString);pOffset++;

            pPStm.setInt(pOffset, pEcl.pruefenInt);pOffset++;
            pPStm.setInt(pOffset, pEcl.minimalWertInt);pOffset++;
            pPStm.setInt(pOffset, pEcl.maximalWertInt);pOffset++;
            pPStm.setString(pOffset, pEcl.zulaessigeZiffernInt);pOffset++;
 
            pPStm.setInt(pOffset, pEcl.werteLesenAus);pOffset++;

            pPStm.setInt(pOffset, pEcl.anzeigeForm);pOffset++;
            pPStm.setInt(pOffset, pEcl.eingabeLaenge);pOffset++;
            pPStm.setInt(pOffset, pEcl.feldLaenge);pOffset++;
            pPStm.setString(pOffset, pEcl.bezeichnungVorEingabefeld);pOffset++;
            pPStm.setString(pOffset, pEcl.bezeichnungNachEingabefeld);pOffset++;

            pPStm.setString(pOffset, pEcl.hilfetext);pOffset++;

            pPStm.setInt(pOffset, pEcl.parameterTutNichts);pOffset++;
            pPStm.setInt(pOffset, pEcl.parameterIstFuerAllgemeineVerwendungFreigegeben);pOffset++;

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
    private final String[] felder= {"ident", 
            "parameterTable", "parameterOffsetInTable", "parameterSubOffsetInTable", "parameterLaengeInTable", 
            "parameterGehoertZuPresetArt",
            "wertIstTyp", "pruefroutineAusfuehren", 
            "pruefenString", "minimaleLaengeString", "maximaleLaengeString", "zulaessigeZeichenString",
            "pruefenInt", "minimalWertInt", "maximalWertInt", "zulaessigeZiffernInt",
            "werteLesenAus", 
            "anzeigeForm", "eingabeLaenge", "feldLaenge", "bezeichnungVorEingabefeld", "bezeichnungNachEingabefeld",
            "hilfetext", "parameterTutNichts", "parameterIstFuerAllgemeineVerwendungFreigegeben"
            }; 
//  @formatter:on
    private boolean initErfolgt=false;
    private void initFelder(){
        if (initErfolgt) {return;}
        initErfolgt=true;
    }

    @Override
    public int insert(ParamStrukt pEcl) {
        initFelder();
        return insertIntern(felder, pEcl);
    }


//    /**Liest alle Demo-Kennungen ein*/
//     public int readAll() {
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

     public int read(int pIdent) {
        PreparedStatement lPStm = null;
        int anzInArray = 0;
        try {
            String lSql = "SELECT * from " + getSchema() + getTableName()+" ";
            lSql = lSql + " WHERE ident=?;";
            lPStm = verbindung.prepareStatement(lSql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            lPStm.setInt(1, pIdent);
           anzInArray = readIntern(lPStm);

        } catch (Exception e) {
            CaBug.drucke("001");
            System.err.println(" " + e.getMessage());
            return (-1);
        }
        return (anzInArray);
    }

//     public int readAll() {
//         PreparedStatement lPStm = null;
//         int anzInArray = 0;
//         try {
//             String lSql = "SELECT * from " + getSchema() + getTableName()+" ";
//             lSql = lSql + " ORDER BY parameterTable, parameterOffsetInTable;";
//             lPStm = verbindung.prepareStatement(lSql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
//            anzInArray = readIntern(lPStm);
//
//         } catch (Exception e) {
//             CaBug.drucke("001");
//             System.err.println(" " + e.getMessage());
//             return (-1);
//         }
//         return (anzInArray);
//     }

     public int readAll(int pPresetArt) {
         PreparedStatement lPStm = null;
         int anzInArray = 0;
         try {
             String lSql = "SELECT * from " + getSchema() + getTableName()+" ";
             lSql=lSql+" WHERE parameterGehoertZuPresetArt=? ";
             lSql = lSql + " ORDER BY parameterTable, parameterOffsetInTable;";
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



}
