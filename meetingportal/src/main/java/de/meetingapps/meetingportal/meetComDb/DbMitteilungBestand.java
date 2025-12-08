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
import de.meetingapps.meetingportal.meetComEntities.EclMitteilungBestand;

public class DbMitteilungBestand extends DbRoot<EclMitteilungBestand> {

    public DbMitteilungBestand(DbBundle pDbBundle) {
        super(pDbBundle);
    }

    
    
    @Override
    String getCreateString() {

        //      @formatter:off
        String createString = "CREATE TABLE " + dbBundle.getSchemaMandant() + getTableName()+" ( "
                + "`ident` int(11) NOT NULL AUTO_INCREMENT, "
                + "`artMitteilung` int(11) NOT NULL, "
                + "`identMitteilung` int(11) NOT NULL, "
                + "`kennungSteller` varchar(20) DEFAULT NULL, " 
                + "`aktionaersnummer` varchar(20) DEFAULT NULL, " 
                + "`meldeIdent` int(11) NOT NULL, "
                + "PRIMARY KEY (`ident`) ) ";
        //      @formatter:on
        return createString;
    }

    @Override
    String getSchema() {
        return dbBundle.getSchemaMandant();
    }


//    @Override
//    void resetInterneIdent(int pHoechsteIdent) {
//    }


    @Override
    String getTableName() {
        return "tbl_mitteilungBestand";
    }


    @Override
    String getFeldFuerInterneIdent() {
        return null;
    }

    @Override
    int getAnzFelder() {
        return 5;
    }


    @Override
    EclMitteilungBestand decodeErgebnis(ResultSet pErgebnis) {
        EclMitteilungBestand lEclReturn = new EclMitteilungBestand();

        try {
            lEclReturn.artMitteilung = pErgebnis.getInt("artMitteilung");
            lEclReturn.identMitteilung = pErgebnis.getInt("identMitteilung");
            
            lEclReturn.kennungSteller = pErgebnis.getString("kennungSteller");
            lEclReturn.aktionaersnummer = pErgebnis.getString("aktionaersnummer");
            lEclReturn.meldeIdent = pErgebnis.getInt("meldeIdent");
             
      } catch (Exception e) {
            CaBug.drucke("001");
            System.err.println(" " + e.getMessage());
        }

        return lEclReturn;
    }



    @Override
    void fuellePreparedStatementKomplett(PreparedStatement pPStm, int pOffset, EclMitteilungBestand pEcl) {
        int startOffset = pOffset; /*Nur erforderlich zum Überprüfen von Programmierfehlern - Feldanzahl*/

        try {
//          @formatter:off
            
//            pPStm.setInt(pOffset, pEcl.ident);pOffset++;
            pPStm.setInt(pOffset, pEcl.artMitteilung);pOffset++;
            pPStm.setInt(pOffset, pEcl.identMitteilung);pOffset++;
            
            pPStm.setString(pOffset, pEcl.kennungSteller);pOffset++;
            pPStm.setString(pOffset, pEcl.aktionaersnummer);pOffset++;
            pPStm.setInt(pOffset, pEcl.meldeIdent);pOffset++;
            
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

    private String[] felder=null; 
    private boolean initErfolgt=false;
    private void initFelder(){
        if (initErfolgt) {return;}
        felder=new String[getAnzFelder()];
        felder[0]="artMitteilung";
        felder[1]="identMitteilung";
        felder[2]="kennungSteller";
        felder[3]="aktionaersnummer";
        felder[4]="meldeIdent";
        initErfolgt=true;
    }

    @Override
    public int insert(EclMitteilungBestand pEcl) {

        initFelder();
        return insertIntern(felder, pEcl);
    }

    /** Return-Wert = Anzahl der gefundenen Sätze (oder Fehlermeldung <0)*/
    public int read(int pArtMitteilung, int pIdentMitteilung) {
        int anzInArray = 0;
        PreparedStatement lPStm = null;

        try {
            String lSql = "SELECT * from " + getSchema() + getTableName()+" where " + 
                    "artMitteilung=? AND identMitteilung=? " + "ORDER BY identMitteilung;";
            lPStm = verbindung.prepareStatement(lSql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            lPStm.setInt(1, pArtMitteilung);
            lPStm.setInt(2, pIdentMitteilung);

            anzInArray = readIntern(lPStm);

        } catch (Exception e) {
            CaBug.drucke("003");
            System.err.println(" " + e.getMessage());
            return (-1);
        }
        return (anzInArray);
    }

    /** Return-Wert = Anzahl der gefundenen Sätze (oder Fehlermeldung <0)*/
    public int readAll_portalFunktion(int pArtMitteilung) {
        PreparedStatement lPStm = null;
        int anzInArray = 0;
        try {
            String lSql = "SELECT * from " + getSchema() + getTableName()+" where " + 
                    "artMitteilung=? " + "ORDER BY identMitteilung;";
            lPStm = verbindung.prepareStatement(lSql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            lPStm.setInt(1, pArtMitteilung);
            anzInArray = readIntern(lPStm);

        } catch (Exception e) {
            CaBug.drucke("003");
            System.err.println(" " + e.getMessage());
            return (-1);
        }
        return (anzInArray);
    }


    


    @Override
    /**Nicht zu verwenden*/
    void resetInterneIdent(int pHoechsteIdent) {
    }



}
