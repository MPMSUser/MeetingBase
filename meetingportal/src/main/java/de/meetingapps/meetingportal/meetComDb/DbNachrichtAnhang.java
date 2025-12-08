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
import de.meetingapps.meetingportal.meetComEntities.EclNachrichtAnhang;

public class DbNachrichtAnhang  extends DbRoot<EclNachrichtAnhang> {

//    private int logDrucken=10;
    
    /**Ggf. vor Benutzung setzen; anschließend unbedingt wieder auf true setzen!*/
    public boolean mandantenabhaengig=true;

    public DbNachrichtAnhang(DbBundle pDbBundle) {
        super(pDbBundle);
    }

    @Override
    String getCreateString() {

        //      @formatter:off
        String createString = "CREATE TABLE " + getSchema() + getTableName()+" ( "
                + "`ident` int(11) NOT NULL, " 
                + "`dbServerIdent` int(11) NOT NULL, " 
                + "`identMail` int(11) NOT NULL, "

               + "`mandant` int(11) NOT NULL, "
               + "`hvJahr` int(11) NOT NULL, " 
               + "`hvNummer` char(1) NOT NULL, "
               + "`dbArt` char(1) NOT NULL, "

                + "`dateiname` varchar(100) DEFAULT NULL, "
                + "`beschreibung` varchar(80) DEFAULT NULL, " 
                + "PRIMARY KEY (`ident`, `dbServerIdent`) " 
                + ") ";

        //      @formatter:on
        return createString;
    }

    @Override
    String getSchema() {
        if (mandantenabhaengig) {
            return dbBundle.getSchemaMandant();
        }
        else {
            return dbBundle.getSchemaAllgemein();
        }
    }

    @Override
    void resetInterneIdent(int pHoechsteIdent) {
        if (mandantenabhaengig) {
            dbBasis.resetInterneIdentNachrichtAnhang();
       }
        else {
            dbBasis.resetInterneIdentNachrichtAnhangOhneMandant();
        }
    }

    @Override
    String getTableName() {
        return "tbl_nachrichtAnhang";
    }

    @Override
    String getFeldFuerInterneIdent() {
        return "ident";
    }

    @Override
    int getAnzFelder() {
        return 9;
    }

    
    @Override
    EclNachrichtAnhang decodeErgebnis(ResultSet pErgebnis) {

        EclNachrichtAnhang lEclReturn = new EclNachrichtAnhang();

        try {
            //          @formatter:off
            lEclReturn.ident = pErgebnis.getInt("ident");
            lEclReturn.dbServerIdent = pErgebnis.getInt("dbServerIdent");
            lEclReturn.identMail = pErgebnis.getInt("identMail");

            lEclReturn.mandant = pErgebnis.getInt("mandant");
            lEclReturn.hvJahr = pErgebnis.getInt("hvJahr");
            lEclReturn.hvNummer = pErgebnis.getString("hvNummer");
            lEclReturn.dbArt = pErgebnis.getString("dbArt");

            lEclReturn.dateiname = pErgebnis.getString("dateiname");
            lEclReturn.beschreibung = pErgebnis.getString("beschreibung");

             //          @formatter:on

        } catch (Exception e) {
            CaBug.drucke("001");
            System.err.println(" " + e.getMessage());
        }

        return lEclReturn;
    }

    @Override
    void fuellePreparedStatementKomplett(PreparedStatement pPStm, int pOffset, EclNachrichtAnhang pEcl) {
        int startOffset = pOffset; /*Nur erforderlich zum Überprüfen von Programmierfehlern - Feldanzahl*/

        try {
//          @formatter:off

            pPStm.setInt(pOffset, pEcl.ident);pOffset++;
            pPStm.setInt(pOffset, pEcl.dbServerIdent);pOffset++;
            pPStm.setInt(pOffset, pEcl.identMail);pOffset++;

            pPStm.setInt(pOffset, pEcl.mandant);pOffset++;
            pPStm.setInt(pOffset, pEcl.hvJahr);pOffset++;
            pPStm.setString(pOffset, pEcl.hvNummer);pOffset++;
            pPStm.setString(pOffset, pEcl.dbArt);pOffset++;

            pPStm.setString(pOffset, pEcl.dateiname);pOffset++;
            pPStm.setString(pOffset, pEcl.beschreibung);pOffset++;
           
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
        felder[0]="ident";
        felder[1]="dbServerIdent";
        felder[2]="identMail";
        
        felder[3]="mandant";
        felder[4]="hvJahr";
        felder[5]="hvNummer";
        felder[6]="dbArt";
        
        felder[7]="dateiname";
        felder[8]="beschreibung";
 
        initErfolgt=true;
    }

    /**Belegt ident und dbServerIdent.
     * identMail muß vom Aufrufer belegt werden.
     */
    @Override
    public int insert(EclNachrichtAnhang pEcl) {

        int erg=0;
        if (mandantenabhaengig) {
            erg=dbBasis.getInterneIdentNachrichtAnhang();
        }
        else {
            erg=dbBasis.getInterneIdentNachrichtAnhangOhneMandant();
        }
        pEcl.ident=erg;
        pEcl.dbServerIdent=dbBundle.paramServer.dbServerIdent;
        
        initFelder();
        return insertIntern(felder, pEcl);
    }

    
    /** Return-Wert = Anzahl der gefundenen Sätze (oder Fehlermeldung <0)*/
    public int read(int pIdent, int pDbServerIdent) {
        PreparedStatement lPStm = null;
        int anzInArray = 0;
        try {
            String lSql = "SELECT * from " + getSchema() + getTableName()+ 
                    " where ident=?  AND dbServerIdent=? ORDER BY ident;";
            lPStm = verbindung.prepareStatement(lSql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            lPStm.setInt(1, pIdent);
            lPStm.setInt(2, pDbServerIdent);
            anzInArray = readIntern(lPStm);

        } catch (Exception e) {
            CaBug.drucke("003");
            System.err.println(" " + e.getMessage());
            return (-1);
        }
        return (anzInArray);
    }

    /** Return-Wert = Anzahl der gefundenen Sätze (oder Fehlermeldung <0)*/
    public int read_IdentMail(int pIdentMail, int pDbServerIdent) {
        PreparedStatement lPStm = null;
        int anzInArray = 0;
        try {
            String lSql = "SELECT * from " + getSchema() + getTableName()+ 
                    " where identMail=?  AND dbServerIdent=? ORDER BY ident;";
            lPStm = verbindung.prepareStatement(lSql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            lPStm.setInt(1, pIdentMail);
            lPStm.setInt(2, pDbServerIdent);
            anzInArray = readIntern(lPStm);

        } catch (Exception e) {
            CaBug.drucke("003");
            System.err.println(" " + e.getMessage());
            return (-1);
        }
        return (anzInArray);
    }

    public int updateMandant() {
        return dbBundle.dbLowLevel.rawUpdateMandant(dbBundle.getSchemaMandant() + getTableName());
    }

    
//    /**Update. 
//     * 
//     * Returnwert:
//     * pfXyWurdeVonAnderemBenutzerVeraendert
//     * -1 => unbekannter Fehler
//     * 1 = Update wurde durchgeführt.
//     * 
//     */
//    public int update(EclNachrichtAnhang pEcl) {
//        int anzahl=0;
//        initFelder();
//
//        pEcl.db_version++;
//
//        try {
//
//            String lSql = setzeUpdateBasisStringZusammen(felder)
//                    + " WHERE "
//                    + "ident=? AND db_version=? AND dbServerIdent=? ";
//
//            PreparedStatement lPStm = verbindung.prepareStatement(lSql);
//            fuellePreparedStatementKomplett(lPStm, 1, pEcl);
//            lPStm.setInt(getAnzFelder() + 1, pEcl.ident);
//            lPStm.setLong(getAnzFelder() + 2, pEcl.db_version - 1);
//            lPStm.setInt(getAnzFelder() + 3, pEcl.dbServerIdent);
//
//            anzahl = updateIntern(lPStm);
//        } catch (Exception e1) {
//            CaBug.drucke("001");
//            System.err.println(" " + e1.getMessage());
//            return (-1);
//        }
//
//        return anzahl;
//    }
//
//
//    /**Return-Werte:
//     * pfXyWurdeVonAnderemBenutzerVeraendert
//     * -1 => undefinierter Fehler
//     * 1 => Löschen erfolgreich
//     */
//    public int delete(int pIdent, int pDbServerIdent) {
//        int ergebnis=0;
//        try {
//            String sql = "DELETE FROM " + getSchema()+getTableName()+
//                    " WHERE ident=? AND dbServerIdent=?`";
//            PreparedStatement pstm1 = verbindung.prepareStatement(sql);
//            pstm1.setInt(1, pIdent);
//            pstm1.setInt(2, pDbServerIdent);
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
