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
import de.meetingapps.meetingportal.meetComEntities.EclWortmeldetischProtokoll;

/**Hinweis: rueckfragen wird wie fragen behandelt*/

public class  DbWortmeldetischProtokoll extends DbRoot<EclWortmeldetischProtokoll> {

//    private int logDrucken=10;
    
    public DbWortmeldetischProtokoll(DbBundle pDbBundle) {
        super(pDbBundle);
    }

    
    @Override
    String getCreateString() {

//      @formatter:off
       String createString = "CREATE TABLE " + dbBundle.getSchemaMandant() + getTableName()+" ( "
                + "`identWortmeldung` int(11) NOT NULL DEFAULT '0', "
                + "`datumZeit` char(19) NOT NULL DEFAULT '', " 
              
                + "`alterStatus` varchar(60) NOT NULL DEFAULT '', " 
                + "`neuerStatus` varchar(60) NOT NULL DEFAULT '', " 
                
                + "`sonstigeAktion` int(11) NOT NULL DEFAULT '0', "

                + "`nameVornameOrt` varchar(200) NOT NULL DEFAULT '', " 
                
                + "`raumNr` int(11) NOT NULL DEFAULT '0', "
                + "`lfdNrInListe` int(11) NOT NULL DEFAULT '0', "
                
               + "`kommentarIntern` varchar(500) NOT NULL DEFAULT '', " 
               + "`kommentarVersammlungsleiter` varchar(500) NOT NULL DEFAULT '' " 
               + ") ";
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
        return "tbl_wortmeldetischProtokoll";
    }


    @Override
    String getFeldFuerInterneIdent() {
        return "";
    }

    @Override
    int getAnzFelder() {
        return 10;
    }


    @Override
    EclWortmeldetischProtokoll decodeErgebnis(ResultSet pErgebnis) {
        EclWortmeldetischProtokoll lEclReturn = new EclWortmeldetischProtokoll();

        try {
            lEclReturn.identWortmeldung = pErgebnis.getInt("identWortmeldung");

            lEclReturn.datumZeit = pErgebnis.getString("datumZeit");
            lEclReturn.alterStatus = pErgebnis.getString("alterStatus");
            lEclReturn.neuerStatus = pErgebnis.getString("neuerStatus");

            lEclReturn.sonstigeAktion = pErgebnis.getInt("sonstigeAktion");
            lEclReturn.nameVornameOrt = pErgebnis.getString("nameVornameOrt");
            lEclReturn.raumNr = pErgebnis.getInt("raumNr");
            lEclReturn.lfdNrInListe = pErgebnis.getInt("lfdNrInListe");
            
            lEclReturn.kommentarIntern = pErgebnis.getString("kommentarIntern");
            lEclReturn.kommentarVersammlungsleiter = pErgebnis.getString("kommentarVersammlungsleiter");
        } catch (Exception e) {
            CaBug.drucke("001");
            System.err.println(" " + e.getMessage());
        }

        return lEclReturn;
    }



    @Override
    void fuellePreparedStatementKomplett(PreparedStatement pPStm, int pOffset, EclWortmeldetischProtokoll pEcl) {
        int startOffset = pOffset; /*Nur erforderlich zum Überprüfen von Programmierfehlern - Feldanzahl*/

        try {
//          @formatter:off
            
            pPStm.setInt(pOffset, pEcl.identWortmeldung);pOffset++;

            pPStm.setString(pOffset, pEcl.datumZeit);pOffset++;
            pPStm.setString(pOffset, pEcl.alterStatus);pOffset++;
            pPStm.setString(pOffset, pEcl.neuerStatus);pOffset++;

            pPStm.setInt(pOffset, pEcl.sonstigeAktion);pOffset++;
            
            pPStm.setString(pOffset, pEcl.nameVornameOrt);pOffset++;

            pPStm.setInt(pOffset, pEcl.raumNr);pOffset++;
            pPStm.setInt(pOffset, pEcl.lfdNrInListe);pOffset++;

            pPStm.setString(pOffset, pEcl.kommentarIntern);pOffset++;
            pPStm.setString(pOffset, pEcl.kommentarVersammlungsleiter);pOffset++;

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
            "identWortmeldung", "datumZeit", "alterStatus", "neuerStatus", 
            "sonstigeAktion", "nameVornameOrt", "raumNr", "lfdNrInListe", "kommentarIntern", "kommentarVersammlungsleiter"
             
    };
    private boolean initErfolgt=false;
    private void initFelder(){
        if (initErfolgt) {return;}
        initErfolgt=true;
    }

    @Override
    public int insert(EclWortmeldetischProtokoll pEcl) {

        initFelder();
        return insertIntern(felder, pEcl);
    }

    
//    /** Return-Wert = Anzahl der gefundenen Sätze (oder Fehlermeldung <0)
//     * Liefert alle Sätze der versionen, die 0-er Version (also die Einreichung) kommt an erster Position*/
//    public int read(int pIdent) {
//        int anzInArray = 0;
//        PreparedStatement lPStm = null;
//
//        try {
//            String lSql = "SELECT * from " + getSchema() + getTableName()+" where " + 
//                    "infoIdent=? " + "ORDER BY infoIdent;";
//            lPStm = verbindung.prepareStatement(lSql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
//            lPStm.setInt(1, pIdent);
//
//            anzInArray = readIntern(lPStm);
//
//        } catch (Exception e) {
//            CaBug.drucke("003");
//            System.err.println(" " + e.getMessage());
//            return (-1);
//        }
//        return (anzInArray);
//    }
    



}
