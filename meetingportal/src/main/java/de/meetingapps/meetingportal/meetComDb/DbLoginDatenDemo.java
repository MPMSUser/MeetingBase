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
import de.meetingapps.meetingportal.meetComEntities.EclLoginDatenDemo;

/**Hinweis: rueckfragen wird wie fragen behandelt*/

public class  DbLoginDatenDemo extends DbRoot<EclLoginDatenDemo> {

//    private int logDrucken=10;
    
    public DbLoginDatenDemo(DbBundle pDbBundle) {
        super(pDbBundle);
    }

    
    @Override
    String getCreateString() {

//      @formatter:off
       String createString = "CREATE TABLE " + getSchema() + getTableName()+" ( "
                + "`loginKennung` char(20) NOT NULL DEFAULT '', " 
                + "`passwortDemo` varchar(30) NOT NULL DEFAULT '', " 
                + "`verwendungszweck` varchar(200) NOT NULL DEFAULT '', " 
               + "PRIMARY KEY (`loginKennung`) " + ") ";
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
         return "tbl_loginDatenDemo";
    }


    @Override
    String getFeldFuerInterneIdent() {
        return "";
    }

    @Override
    int getAnzFelder() {
        return 3;
    }


    @Override
    EclLoginDatenDemo decodeErgebnis(ResultSet pErgebnis) {
        EclLoginDatenDemo lEclReturn = new EclLoginDatenDemo();

        try {
            lEclReturn.loginKennung = pErgebnis.getString("loginKennung");
            lEclReturn.passwortDemo = pErgebnis.getString("passwortDemo");
            lEclReturn.verwendungszweck = pErgebnis.getString("verwendungszweck");
                     
       } catch (Exception e) {
            CaBug.drucke("001");
            System.err.println(" " + e.getMessage());
        }

        return lEclReturn;
    }



    @Override
    void fuellePreparedStatementKomplett(PreparedStatement pPStm, int pOffset, EclLoginDatenDemo pEcl) {
        int startOffset = pOffset; /*Nur erforderlich zum Überprüfen von Programmierfehlern - Feldanzahl*/

        try {
//          @formatter:off
            
            pPStm.setString(pOffset, pEcl.loginKennung);pOffset++;
            pPStm.setString(pOffset, pEcl.passwortDemo);pOffset++;
            pPStm.setString(pOffset, pEcl.verwendungszweck);pOffset++;

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
        felder[0]="loginKennung";
        felder[1]="passwortDemo";
        felder[2]="verwendungszweck";
         initErfolgt=true;
    }

    @Override
    public int insert(EclLoginDatenDemo pEcl) {
        initFelder();
        return insertIntern(felder, pEcl);
    }


    /**Liest alle Demo-Kennungen ein*/
     public int readAll() {
        PreparedStatement lPStm = null;
        int anzInArray = 0;
        try {
            String lSql = "SELECT * from " + getSchema() + getTableName()+" ";
            lSql = lSql + " ORDER BY loginKennung;";
            lPStm = verbindung.prepareStatement(lSql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
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
    public int update(EclLoginDatenDemo pEcl) {
        int ergebnis=0;
        initFelder();

        try {

            String lSql = setzeUpdateBasisStringZusammen(felder)
                    + " WHERE "
                    + "loginKennung=? ";

            PreparedStatement lPStm = verbindung.prepareStatement(lSql);
            fuellePreparedStatementKomplett(lPStm, 1, pEcl);
            lPStm.setString(getAnzFelder() + 1, pEcl.loginKennung);

            ergebnis = updateIntern(lPStm);
        } catch (Exception e1) {
            CaBug.drucke("001");
            System.err.println(" " + e1.getMessage());
            return (-1);
        }

        return ergebnis;
    }

    /**Passwort für alle Demo-Aktionäre wird upgedated*/
    public int updatePasswort(String pPasswort) {
        int ergebnis=0;

        try {
            String lSql="UPDATE "+getSchema()+getTableName()+" SET "
                    + "passwortDemo=? ";


            PreparedStatement lPStm = verbindung.prepareStatement(lSql);
            lPStm.setString(1, pPasswort);

            ergebnis = updateIntern(lPStm);
        } catch (Exception e1) {
            CaBug.drucke("001");
            System.err.println(" " + e1.getMessage());
            return (-1);
        }

        return ergebnis;
    }

 
    /**Return-Werte:
     * pfXyWurdeVonAnderemBenutzerVeraendert
     * -1 => undefinierter Fehler
     * 1 => Löschen erfolgreich
     */
    public int delete(String pLoginKennung) {
        int ergebnis=0;
        try {
            String sql = "DELETE FROM " + getSchema()+getTableName()+
                    " WHERE loginKennung=? ";
            PreparedStatement pstm1 = verbindung.prepareStatement(sql);
            pstm1.setString(1, pLoginKennung);
            ergebnis = deleteIntern(pstm1);
        } catch (Exception e1) {
            CaBug.drucke("001");
            System.err.println(" " + e1.getMessage());
            return (-1);
        }

        return ergebnis;
    }



}
