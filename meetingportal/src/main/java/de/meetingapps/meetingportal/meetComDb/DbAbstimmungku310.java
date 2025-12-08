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
import de.meetingapps.meetingportal.meetComEntities.EclAbstimmungku310;

/**Hinweis: rueckfragen wird wie fragen behandelt*/

public class  DbAbstimmungku310 extends DbRoot<EclAbstimmungku310> {

//    private int logDrucken=10;
    
    public DbAbstimmungku310(DbBundle pDbBundle) {
        super(pDbBundle);
    }

    
    @Override
    String getCreateString() {

//      @formatter:off
       String createString = "CREATE TABLE " + getSchema() + getTableName()+" ( "
               + "`aktienregisterIdent` int(11) NOT NULL DEFAULT 0, "
               + "`identWeisungssatz` int(11) NOT NULL DEFAULT 0, "
 
              + "`stimmabgabeDurchgefuehrt` int(11) NOT NULL DEFAULT 0, "
              + "`stimmenUebernehmen` int(11) NOT NULL DEFAULT 0, "
              + "`geberOderNehmer` int(11) NOT NULL DEFAULT 0, "

              + "`gesamtStimmen` bigint(20) NOT NULL DEFAULT 0, "
              + "`jaStimmen` bigint(20) NOT NULL DEFAULT 0, "
              + "`neinStimmen` bigint(20) NOT NULL DEFAULT 0, "
              + "`enthaltungStimmen` bigint(20) NOT NULL DEFAULT 0, "
               
              + "PRIMARY KEY (`aktienregisterIdent`, `identWeisungssatz`, `geberOderNehmer`) " + ") ";
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
         return "tbl_abstimmungku310";
    }


    @Override
    String getFeldFuerInterneIdent() {
        return "";
    }

    @Override
    int getAnzFelder() {
        return 9;
    }


    @Override
    EclAbstimmungku310 decodeErgebnis(ResultSet pErgebnis) {
        EclAbstimmungku310 lEclReturn = new EclAbstimmungku310();

        try {
            lEclReturn.aktienregisterIdent = pErgebnis.getInt("aktienregisterIdent");
            lEclReturn.identWeisungssatz = pErgebnis.getInt("identWeisungssatz");

            lEclReturn.stimmabgabeDurchgefuehrt = pErgebnis.getInt("stimmabgabeDurchgefuehrt");
            lEclReturn.stimmenUebernehmen = pErgebnis.getInt("stimmenUebernehmen");
            lEclReturn.geberOderNehmer = pErgebnis.getInt("geberOderNehmer");

            lEclReturn.gesamtStimmen = pErgebnis.getLong("gesamtStimmen");
            lEclReturn.jaStimmen = pErgebnis.getLong("jaStimmen");
            lEclReturn.neinStimmen = pErgebnis.getLong("neinStimmen");
            lEclReturn.enthaltungStimmen = pErgebnis.getLong("enthaltungStimmen");
            
                     
       } catch (Exception e) {
            CaBug.drucke("001");
            System.err.println(" " + e.getMessage());
        }

        return lEclReturn;
    }



    @Override
    void fuellePreparedStatementKomplett(PreparedStatement pPStm, int pOffset, EclAbstimmungku310 pEcl) {
        int startOffset = pOffset; /*Nur erforderlich zum Überprüfen von Programmierfehlern - Feldanzahl*/

        try {
//          @formatter:off
            
            pPStm.setInt(pOffset, pEcl.aktienregisterIdent);pOffset++;
            pPStm.setInt(pOffset, pEcl.identWeisungssatz);pOffset++;

            pPStm.setInt(pOffset, pEcl.stimmabgabeDurchgefuehrt);pOffset++;
            pPStm.setInt(pOffset, pEcl.stimmenUebernehmen);pOffset++;
            pPStm.setInt(pOffset, pEcl.geberOderNehmer);pOffset++;

            pPStm.setLong(pOffset, pEcl.gesamtStimmen);pOffset++;
            pPStm.setLong(pOffset, pEcl.jaStimmen);pOffset++;
            pPStm.setLong(pOffset, pEcl.neinStimmen);pOffset++;
            pPStm.setLong(pOffset, pEcl.enthaltungStimmen);pOffset++;

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

    private String[] felder= {
            "aktienregisterIdent",
            "identWeisungssatz",

            "stimmabgabeDurchgefuehrt",
            "stimmenUebernehmen",
            "geberOderNehmer",

            "gesamtStimmen",
            "jaStimmen",
            "neinStimmen",
            "enthaltungStimmen"
     };
    
    private boolean initErfolgt=false;
    private void initFelder(){
        if (initErfolgt) {return;}
        initErfolgt=true;
    }

    @Override
    public int insert(EclAbstimmungku310 pEcl) {
        initFelder();
        
        return insertIntern(felder, pEcl);
    }



     public int read(int pAktienregisterIdent, int pidentWeisungssatz) {
        PreparedStatement lPStm = null;
        int anzInArray = 0;
        try {
            String lSql = "SELECT * from " + getSchema() + getTableName()+" ";
            lSql=lSql+" WHERE aktienregisterIdent=? AND identWeisungssatz=? ";
            lSql = lSql + " ORDER BY geberOderNehmer;";
            lPStm = verbindung.prepareStatement(lSql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            lPStm.setInt(1, pAktienregisterIdent);
            lPStm.setInt(2, pidentWeisungssatz);
            anzInArray = readIntern(lPStm);

        } catch (Exception e) {
            CaBug.drucke("003");
            System.err.println(" " + e.getMessage());
            return (-1);
        }
        return (anzInArray);
    }

     public int read_alleFuerBestimmteAbstimmung(int pidentWeisungssatz) {
         PreparedStatement lPStm = null;
         int anzInArray = 0;
         try {
             String lSql = "SELECT * from " + getSchema() + getTableName()+" ";
             lSql=lSql+" WHERE identWeisungssatz=? ";
             lSql = lSql + " ;";
             lPStm = verbindung.prepareStatement(lSql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
             lPStm.setInt(1, pidentWeisungssatz);
             anzInArray = readIntern(lPStm);

         } catch (Exception e) {
             CaBug.drucke("003");
             System.err.println(" " + e.getMessage());
             return (-1);
         }
         return (anzInArray);
     }


    
    /**Update. 
     * 
     * Returnwert:
     * pfXyWurdeVonAnderemBenutzerVeraendert
     * -1 => unbekannter Fehler
     * 1 = Update wurde durchgeführt.
     * 
     */
    public int update(EclAbstimmungku310 pEcl) {
        int ergebnis=0;
        initFelder();

        try {

            String lSql = setzeUpdateBasisStringZusammen(felder)
                    + " WHERE "
                    + "aktienregisterIdent=? AND identWeisungssatz=? AND geberOderNehmer=?";

            PreparedStatement lPStm = verbindung.prepareStatement(lSql);
            fuellePreparedStatementKomplett(lPStm, 1, pEcl);
            lPStm.setInt(getAnzFelder() + 1, pEcl.aktienregisterIdent);
            lPStm.setInt(getAnzFelder() + 2, pEcl.identWeisungssatz);
            lPStm.setInt(getAnzFelder() + 3, pEcl.geberOderNehmer);

            ergebnis = updateIntern(lPStm);
        } catch (Exception e1) {
            CaBug.drucke("001");
            System.err.println(" " + e1.getMessage());
            return (-1);
        }

        return ergebnis;
    }

 
//    /**Return-Werte:
//     * pfXyWurdeVonAnderemBenutzerVeraendert
//     * -1 => undefinierter Fehler
//     * 1 => Löschen erfolgreich
//     */
//    public int delete(int pIdent) {
//        int ergebnis=0;
//        try {
//            String sql = "DELETE FROM " + getSchema()+getTableName()+
//                    " WHERE ident=? ";
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
//    public int deleteSet(int pSetNr) {
//        int ergebnis=0;
//        try {
//            String sql = "DELETE FROM " + getSchema()+getTableName()+
//                    " WHERE setNr=? ";
//            PreparedStatement pstm1 = verbindung.prepareStatement(sql);
//            pstm1.setInt(1, pSetNr);
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
