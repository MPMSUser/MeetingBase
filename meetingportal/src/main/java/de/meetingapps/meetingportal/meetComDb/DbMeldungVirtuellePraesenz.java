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
import de.meetingapps.meetingportal.meetComEntities.EclMeldungVirtuellePraesenz;

/**Hinweis: rueckfragen wird wie fragen behandelt*/

public class  DbMeldungVirtuellePraesenz extends DbRoot<EclMeldungVirtuellePraesenz> {

//    private int logDrucken=10;
    
    public DbMeldungVirtuellePraesenz(DbBundle pDbBundle) {
        super(pDbBundle);
    }

    
    
    @Override
    String getCreateString() {

        //      @formatter:off
        String createString = "CREATE TABLE " + dbBundle.getSchemaMandant() + getTableName()+" ( "
                + "`loginkennungIdent` int(11) NOT NULL, "
                + "`meldungsIdent` int(11) NOT NULL, "
                + "`statusPraesenz` int(11) NOT NULL DEFAULT '0', "
                + "`vertreterName` varchar(200) NOT NULL DEFAULT NULL DEFAULT '', " 
                + "`vertreterVorname` varchar(200) NOT NULL DEFAULT NULL DEFAULT '', " 
                + "`vertreterOrt` varchar(200) NOT NULL DEFAULT NULL DEFAULT '', " 
                + "PRIMARY KEY (`loginkennungIdent`, `meldungsIdent`) " + ") ";
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
         return "tbl_meldungVirtuellePraesenz";
    }


    @Override
    String getFeldFuerInterneIdent() {
        return "";
    }

    @Override
    int getAnzFelder() {
        return 6;
    }


    @Override
    EclMeldungVirtuellePraesenz decodeErgebnis(ResultSet pErgebnis) {
        EclMeldungVirtuellePraesenz lEclReturn = new EclMeldungVirtuellePraesenz();

        try {
            lEclReturn.loginkennungIdent = pErgebnis.getInt("loginkennungIdent");
            lEclReturn.meldungsIdent = pErgebnis.getInt("meldungsIdent");
            lEclReturn.statusPraesenz = pErgebnis.getInt("statusPraesenz");
           
            lEclReturn.vertreterName = pErgebnis.getString("vertreterName");
            lEclReturn.vertreterVorname = pErgebnis.getString("vertreterVorname");
            lEclReturn.vertreterOrt = pErgebnis.getString("vertreterOrt");
                   
       } catch (Exception e) {
            CaBug.drucke("001");
            System.err.println(" " + e.getMessage());
        }

        return lEclReturn;
    }



    @Override
    void fuellePreparedStatementKomplett(PreparedStatement pPStm, int pOffset, EclMeldungVirtuellePraesenz pEcl) {
        int startOffset = pOffset; /*Nur erforderlich zum Überprüfen von Programmierfehlern - Feldanzahl*/

        try {
//          @formatter:off
            
            pPStm.setInt(pOffset, pEcl.loginkennungIdent);pOffset++;
            pPStm.setInt(pOffset, pEcl.meldungsIdent);pOffset++;
            pPStm.setInt(pOffset, pEcl.statusPraesenz);pOffset++;

            pPStm.setString(pOffset, pEcl.vertreterName);pOffset++;
            pPStm.setString(pOffset, pEcl.vertreterVorname);pOffset++;
            pPStm.setString(pOffset, pEcl.vertreterOrt);pOffset++;

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
        felder[0]="loginkennungIdent";
        felder[1]="meldungsIdent";
        felder[2]="statusPraesenz";
        felder[3]="vertreterName";
        felder[4]="vertreterVorname";
        felder[5]="vertreterOrt";
         initErfolgt=true;
    }

    @Override
    public int insert(EclMeldungVirtuellePraesenz pEcl) {
        initFelder();
        return insertIntern(felder, pEcl);
    }

    
    /** Return-Wert = Anzahl der gefundenen Sätze (oder Fehlermeldung <0)
     * Liefert alle Sätze der versionen, die 0-er Version (also die Einreichung) kommt an erster Position*/
    public int read(int loginkennungIdent, int meldungsIdent) {
        int anzInArray = 0;
        PreparedStatement lPStm = null;

        try {
            String lSql = "SELECT * from " + getSchema() + getTableName()+" where " + 
                    "loginkennungIdent=? AND meldungsIdent=? " + "ORDER BY meldungsIdent, loginkennungIdent;";
            lPStm = verbindung.prepareStatement(lSql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            lPStm.setInt(1, loginkennungIdent);
            lPStm.setInt(2, meldungsIdent);

            anzInArray = readIntern(lPStm);

        } catch (Exception e) {
            CaBug.drucke("003");
            System.err.println(" " + e.getMessage());
            return (-1);
        }
        return (anzInArray);
    }

    /** Return-Wert = Anzahl der gefundenen Sätze (oder Fehlermeldung <0)
     * Liefert alle Sätze der versionen, die 0-er Version (also die Einreichung) kommt an erster Position
     * 
     **/
    public int read_zuMeldung(int meldungsIdent) {
        int anzInArray = 0;
        PreparedStatement lPStm = null;

        try {
            String lSql = "SELECT * from " + getSchema() + getTableName()+" where " + 
                    "meldungsIdent=? " + "ORDER BY meldungsIdent, loginkennungIdent;";
            lPStm = verbindung.prepareStatement(lSql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
           lPStm.setInt(1, meldungsIdent);

            anzInArray = readIntern(lPStm);

        } catch (Exception e) {
            CaBug.drucke("003");
            System.err.println(" " + e.getMessage());
            return (-1);
        }
        return (anzInArray);
    }


    public int read_zuLoginIdent(int loginkennungIdent) {
        int anzInArray = 0;
        PreparedStatement lPStm = null;

        try {
            String lSql = "SELECT * from " + getSchema() + getTableName()+" where " + 
                    "loginkennungIdent=? " + "ORDER BY meldungsIdent;";
            lPStm = verbindung.prepareStatement(lSql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
           lPStm.setInt(1, loginkennungIdent);

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
    public int update(EclMeldungVirtuellePraesenz pEcl) {
        int ergebnis=0;
        initFelder();

        try {

            String lSql = setzeUpdateBasisStringZusammen(felder)
                    + " WHERE "
                    + "loginkennungIdent=? AND meldungsIdent=? ";

            PreparedStatement lPStm = verbindung.prepareStatement(lSql);
            fuellePreparedStatementKomplett(lPStm, 1, pEcl);
            lPStm.setInt(getAnzFelder() + 1, pEcl.loginkennungIdent);
            lPStm.setInt(getAnzFelder() + 2, pEcl.meldungsIdent);
 
            ergebnis = updateIntern(lPStm);
        } catch (Exception e1) {
            CaBug.drucke("001");
            System.err.println(" " + e1.getMessage());
            return (-1);
        }

        return ergebnis;
    }

    /**pEclMeldungVirtuellePraesenz muß vollständig ausgefüllt sein.
     * Diese Methode überprüft, ob bereits ein passender Satz vorhanden, dann wird dieser
     * "gnadenlos" upgedatet. Ansonsten wird ein neuer Satz eingefügt.
     */
    public int update_Zugang(EclMeldungVirtuellePraesenz pEclMeldungVirtuellePraesenz) {
        read(pEclMeldungVirtuellePraesenz.loginkennungIdent, pEclMeldungVirtuellePraesenz.meldungsIdent);
        if (this.anzErgebnis()==0) {
            return insert(pEclMeldungVirtuellePraesenz);
        }
        else {
            return update(pEclMeldungVirtuellePraesenz);
        }
    }
    
   public int update_Abgang(int ploginKennungIdent, int pMeldungsIdent) {
       int ergebnis=0;
       initFelder();

       try {

           String lSql = "UPDATE "+ getSchema() + getTableName()+
                   " SET statusPraesenz=2 "
                   + " WHERE "
                   + "loginkennungIdent=? AND meldungsIdent=? ";

           PreparedStatement lPStm = verbindung.prepareStatement(lSql);
           lPStm.setInt(1, ploginKennungIdent);
           lPStm.setInt(2, pMeldungsIdent);
 
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
//                    " WHERE mitteilungIdent=? ";
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



}
