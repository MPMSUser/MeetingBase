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
import de.meetingapps.meetingportal.meetComEntities.EclInfo;

/**Hinweis: rueckfragen wird wie fragen behandelt*/

public class  DbInfo extends DbRoot<EclInfo> {

//    private int logDrucken=10;
    
    public DbInfo(DbBundle pDbBundle) {
        super(pDbBundle);
    }

    
    @Override
    String getCreateString() {

//      @formatter:off
       String createString = "CREATE TABLE " + dbBundle.getSchemaMandant() + getTableName()+" ( "
                + "`infoIdent` int(11) NOT NULL DEFAULT '0', "
                + "`db_version` bigint(20) DEFAULT NULL, " 
                + "`infoKlasse` int(11) NOT NULL DEFAULT '0', "
                + "`empfaenger` int(11) NOT NULL DEFAULT '0', " 
                + "`infoText` varchar(1000) NOT NULL DEFAULT '', " 
               + "PRIMARY KEY (`infoIdent`) " + ") ";
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
        return "tbl_info";
    }


    @Override
    String getFeldFuerInterneIdent() {
        return "infoIdent";
    }

    @Override
    int getAnzFelder() {
        return 5;
    }


    @Override
    EclInfo decodeErgebnis(ResultSet pErgebnis) {
        EclInfo lEclReturn = new EclInfo();

        try {
            lEclReturn.infoIdent = pErgebnis.getInt("infoIdent");
            lEclReturn.db_version = pErgebnis.getLong("db_version");

            lEclReturn.infoKlasse = pErgebnis.getInt("infoKlasse");
            lEclReturn.empfaenger = pErgebnis.getInt("empfaenger");
            
            lEclReturn.infoText = pErgebnis.getString("infoText");
        } catch (Exception e) {
            CaBug.drucke("001");
            System.err.println(" " + e.getMessage());
        }

        return lEclReturn;
    }



    @Override
    void fuellePreparedStatementKomplett(PreparedStatement pPStm, int pOffset, EclInfo pEcl) {
        int startOffset = pOffset; /*Nur erforderlich zum Überprüfen von Programmierfehlern - Feldanzahl*/

        try {
//          @formatter:off
            
            pPStm.setInt(pOffset, pEcl.infoIdent);pOffset++;
            pPStm.setLong(pOffset, pEcl.db_version);pOffset++;
            pPStm.setInt(pOffset, pEcl.infoKlasse);pOffset++;
            pPStm.setInt(pOffset, pEcl.empfaenger);pOffset++;

            pPStm.setString(pOffset, pEcl.infoText);pOffset++;

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
        felder[0]="infoIdent";
        felder[1]="db_version";
        felder[2]="infoKlasse";
        felder[3]="empfaenger";
        felder[4]="infoText";
        initErfolgt=true;
    }

    @Override
    public int insert(EclInfo pEcl) {

        initFelder();
        return insertIntern(felder, pEcl);
    }

    
    /** Return-Wert = Anzahl der gefundenen Sätze (oder Fehlermeldung <0)
     * Liefert alle Sätze der versionen, die 0-er Version (also die Einreichung) kommt an erster Position*/
    public int read(int pIdent) {
        int anzInArray = 0;
        PreparedStatement lPStm = null;

        try {
            String lSql = "SELECT * from " + getSchema() + getTableName()+" where " + 
                    "infoIdent=? " + "ORDER BY infoIdent;";
            lPStm = verbindung.prepareStatement(lSql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            lPStm.setInt(1, pIdent);

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
    public int update(EclInfo pEcl) {
        int ergebnis=0;
        initFelder();

        pEcl.db_version++;

        try {

            String lSql = setzeUpdateBasisStringZusammen(felder)
                    + " WHERE "
                    + "infoIdent=? AND db_version=? ";

            PreparedStatement lPStm = verbindung.prepareStatement(lSql);
            fuellePreparedStatementKomplett(lPStm, 1, pEcl);
            lPStm.setInt(getAnzFelder() + 1, pEcl.infoIdent);
            lPStm.setLong(getAnzFelder() + 2, pEcl.db_version - 1);

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
    public int delete(int pIdent) {
        int ergebnis=0;
        try {
            String sql = "DELETE FROM " + getSchema()+getTableName()+
                    " WHERE infoIdent=? ";
            PreparedStatement pstm1 = verbindung.prepareStatement(sql);
            pstm1.setInt(1, pIdent);
            ergebnis = deleteIntern(pstm1);
        } catch (Exception e1) {
            CaBug.drucke("001");
            System.err.println(" " + e1.getMessage());
            return (-1);
        }

        return ergebnis;
    }



}
