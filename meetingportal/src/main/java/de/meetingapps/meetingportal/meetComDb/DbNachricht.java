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
import de.meetingapps.meetingportal.meetComEntities.EclNachricht;

public class DbNachricht  extends DbRoot<EclNachricht> {

//    private int logDrucken=10;
    
    /**Ggf. vor Benutzung setzen; anschließend unbedingt wieder auf true setzen!*/
    public boolean mandantenabhaengig=true;

    public DbNachricht(DbBundle pDbBundle) {
        super(pDbBundle);
    }

    @Override
    String getCreateString() {

//      @formatter:off
       String createString = "CREATE TABLE " + getSchema() + getTableName()+" ( "
               + "`ident` int(11) NOT NULL, " 
               + "`db_version` bigint(20) DEFAULT NULL, " 
               + "`dbServerIdent` int(11) NOT NULL, " 
               + "`identMail` int(11) NOT NULL, "
               
               + "`mandant` int(11) NOT NULL, "
               + "`hvJahr` int(11) NOT NULL, " 
               + "`hvNummer` char(1) NOT NULL, "
               + "`dbArt` char(1) NOT NULL, "
               
               + "`antwortZuMailIdent` int(11) NOT NULL, "
               + "`antwortZuMailIdentDbServer` int(11) NOT NULL, "
               
               + "`userIdAbsender` int(11) NOT NULL, " 
               + "`userIdEmpfaenger` int(11) NOT NULL, "

               + "`anzeigeBeimEmpfaengerAusblenden` int(11) NOT NULL, "
               + "`anzeigeBeimSenderAusblenden` int(11) NOT NULL, "
               + "`anzeigeBeimEmpfaengerGelesen` int(11) NOT NULL, "
              
               + "`bearbeitenBis` char(19) NOT NULL, "
               + "`mailIstBearbeitetVomEmpfaengerGesetzt` int(11) NOT NULL, "
               + "`mailIstBearbeitetVomSenderGesetzt` int(11) NOT NULL, "
               
               + "`sendezeitpunkt` char(19) NOT NULL, "
               
               + "`mailTextAuchInEmailAuffuehren` int(11) NOT NULL, " 
               + "`anlagenSindVorhanden` int(11) NOT NULL, "
               + "`verwendungsCode` int(11) NOT NULL, " 
               + "`parameter1` varchar(20) DEFAULT NULL, "
               + "`parameter2` varchar(20) DEFAULT NULL, " 
               + "`parameter3` varchar(20) DEFAULT NULL, "
               + "`parameter4` varchar(20) DEFAULT NULL, " 
               + "`parameter5` varchar(20) DEFAULT NULL, "
               
               + "`betreff` varchar(80) DEFAULT NULL, " 
               + "`mailText` varchar(10000) DEFAULT NULL, "
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
            dbBasis.resetInterneIdentNachricht();
            dbBasis.resetInterneIdentNachrichtIdentMail();
       }
        else {
            dbBasis.resetInterneIdentNachrichtOhneMandant();
            dbBasis.resetInterneIdentNachrichtIdentMailOhneMandant();
        }
    }

    @Override
    String getTableName() {
        return "tbl_nachricht";
    }

    @Override
    String getFeldFuerInterneIdent() {
        return "ident";
    }

    @Override
    int getAnzFelder() {
        return 29;
    }

    
    @Override
    EclNachricht decodeErgebnis(ResultSet pErgebnis) {

        EclNachricht lEclReturn = new EclNachricht();

        try {
            //          @formatter:off
            lEclReturn.ident = pErgebnis.getInt("nac.ident");
            lEclReturn.db_version = pErgebnis.getLong("nac.db_version");
            lEclReturn.dbServerIdent = pErgebnis.getInt("nac.dbServerIdent");

            lEclReturn.identMail = pErgebnis.getInt("nac.identMail");

            lEclReturn.mandant = pErgebnis.getInt("nac.mandant");
            lEclReturn.hvJahr = pErgebnis.getInt("nac.hvJahr");
            lEclReturn.hvNummer = pErgebnis.getString("nac.hvNummer");
            lEclReturn.dbArt = pErgebnis.getString("nac.dbArt");

            lEclReturn.antwortZuMailIdent = pErgebnis.getInt("nac.antwortZuMailIdent");
            lEclReturn.antwortZuMailIdentDbServer = pErgebnis.getInt("nac.antwortZuMailIdentDbServer");

            lEclReturn.userIdAbsender = pErgebnis.getInt("nac.userIdAbsender");
            lEclReturn.userIdEmpfaenger = pErgebnis.getInt("nac.userIdEmpfaenger");

            lEclReturn.anzeigeBeimEmpfaengerAusblenden = pErgebnis.getInt("nac.anzeigeBeimEmpfaengerAusblenden");
            lEclReturn.anzeigeBeimSenderAusblenden = pErgebnis.getInt("nac.anzeigeBeimSenderAusblenden");
            lEclReturn.anzeigeBeimEmpfaengerGelesen = pErgebnis.getInt("nac.anzeigeBeimEmpfaengerGelesen");

            lEclReturn.bearbeitenBis = pErgebnis.getString("nac.bearbeitenBis");
            lEclReturn.mailIstBearbeitetVomEmpfaengerGesetzt = pErgebnis.getInt("nac.mailIstBearbeitetVomEmpfaengerGesetzt");
            lEclReturn.mailIstBearbeitetVomSenderGesetzt = pErgebnis.getInt("nac.mailIstBearbeitetVomSenderGesetzt");

            lEclReturn.sendezeitpunkt = pErgebnis.getString("nac.sendezeitpunkt");

            lEclReturn.mailTextAuchInEmailAuffuehren = pErgebnis.getInt("nac.mailTextAuchInEmailAuffuehren");
            lEclReturn.anlagenSindVorhanden = pErgebnis.getInt("nac.anlagenSindVorhanden");
            lEclReturn.verwendungsCode = pErgebnis.getInt("nac.verwendungsCode");

            lEclReturn.parameter1 = pErgebnis.getString("nac.parameter1");
            lEclReturn.parameter2 = pErgebnis.getString("nac.parameter2");
            lEclReturn.parameter3 = pErgebnis.getString("nac.parameter3");
            lEclReturn.parameter4 = pErgebnis.getString("nac.parameter4");
            lEclReturn.parameter5 = pErgebnis.getString("nac.parameter5");

            lEclReturn.betreff = pErgebnis.getString("nac.betreff");
            lEclReturn.mailText = pErgebnis.getString("nac.mailText");
            //          @formatter:on

        } catch (Exception e) {
            CaBug.drucke("001");
            System.err.println(" " + e.getMessage());
        }

        return lEclReturn;
    }

    @Override
    void fuellePreparedStatementKomplett(PreparedStatement pPStm, int pOffset, EclNachricht pEcl) {
        int startOffset = pOffset; /*Nur erforderlich zum Überprüfen von Programmierfehlern - Feldanzahl*/

        try {
//          @formatter:off

            pPStm.setInt(pOffset, pEcl.ident);pOffset++;
            pPStm.setLong(pOffset, pEcl.db_version);pOffset++;
            pPStm.setInt(pOffset, pEcl.dbServerIdent);pOffset++;
            pPStm.setInt(pOffset, pEcl.identMail);pOffset++;

            pPStm.setInt(pOffset, pEcl.mandant);pOffset++;
            pPStm.setInt(pOffset, pEcl.hvJahr);pOffset++;
            pPStm.setString(pOffset, pEcl.hvNummer);pOffset++;
            pPStm.setString(pOffset, pEcl.dbArt);pOffset++;

            pPStm.setInt(pOffset, pEcl.antwortZuMailIdent);pOffset++;
            pPStm.setInt(pOffset, pEcl.antwortZuMailIdentDbServer);pOffset++;
            
            pPStm.setInt(pOffset, pEcl.userIdAbsender);pOffset++;
            pPStm.setInt(pOffset, pEcl.userIdEmpfaenger);pOffset++;
            
            pPStm.setInt(pOffset, pEcl.anzeigeBeimEmpfaengerAusblenden);pOffset++;
            pPStm.setInt(pOffset, pEcl.anzeigeBeimSenderAusblenden);pOffset++;
            pPStm.setInt(pOffset, pEcl.anzeigeBeimEmpfaengerGelesen);pOffset++;

            pPStm.setString(pOffset, pEcl.bearbeitenBis);pOffset++;
            pPStm.setInt(pOffset, pEcl.mailIstBearbeitetVomEmpfaengerGesetzt);pOffset++;
            pPStm.setInt(pOffset, pEcl.mailIstBearbeitetVomSenderGesetzt);pOffset++;

            pPStm.setString(pOffset, pEcl.sendezeitpunkt);pOffset++;

            pPStm.setInt(pOffset, pEcl.mailTextAuchInEmailAuffuehren);pOffset++;
            pPStm.setInt(pOffset, pEcl.anlagenSindVorhanden);pOffset++;
            pPStm.setInt(pOffset, pEcl.verwendungsCode);pOffset++;

            pPStm.setString(pOffset, pEcl.parameter1);pOffset++;
            pPStm.setString(pOffset, pEcl.parameter2);pOffset++;
            pPStm.setString(pOffset, pEcl.parameter3);pOffset++;
            pPStm.setString(pOffset, pEcl.parameter4);pOffset++;
            pPStm.setString(pOffset, pEcl.parameter5);pOffset++;

            pPStm.setString(pOffset, pEcl.betreff);pOffset++;
            pPStm.setString(pOffset, pEcl.mailText);pOffset++;
           
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
        felder[1]="db_version";
        felder[2]="dbServerIdent";
        felder[3]="identMail";
        
        felder[4]="mandant";
        felder[5]="hvJahr";
        felder[6]="hvNummer";
        felder[7]="dbArt";
        
        felder[8]="antwortZuMailIdent";
        felder[9]="antwortZuMailIdentDbServer";
        felder[10]="userIdAbsender";
        
        felder[11]="userIdEmpfaenger";
        felder[12]="anzeigeBeimEmpfaengerAusblenden";
        felder[13]="anzeigeBeimSenderAusblenden";
        felder[14]="anzeigeBeimEmpfaengerGelesen";
        
        felder[15]="bearbeitenBis";
        felder[16]="mailIstBearbeitetVomEmpfaengerGesetzt";
        felder[17]="mailIstBearbeitetVomSenderGesetzt";
        felder[18]="sendezeitpunkt";

        felder[19]="mailTextAuchInEmailAuffuehren";
        felder[20]="anlagenSindVorhanden";
        felder[21]="verwendungsCode";
        
        felder[22]="parameter1";
        felder[23]="parameter2";

        felder[24]="parameter3";
        felder[25]="parameter4";
        felder[26]="parameter5";
        
        felder[27]="betreff";
        felder[28]="mailText";

        initErfolgt=true;
    }

    /**Belegt ident und dbServerIdent.
     * identMail muß vom Aufrufer belegt werden.
     */
    @Override
    public int insert(EclNachricht pEcl) {

        int erg=0;
        if (mandantenabhaengig) {
            erg=dbBasis.getInterneIdentNachricht();
        }
        else {
            erg=dbBasis.getInterneIdentNachrichtOhneMandant();
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
                    " nac where nac.ident=?  AND nac.dbServerIdent=? ORDER BY nac.ident;";
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
    public int readZuEmpfaenger(int pUserIdEmpfaenger) {
        PreparedStatement lPStm = null;
        int anzInArray = 0;
        try {
            String lSql = "SELECT * from " + getSchema() + getTableName()+ 
                    " nac where nac.userIdEmpfaenger=? ORDER BY nac.ident;";
            lPStm = verbindung.prepareStatement(lSql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            lPStm.setInt(1, pUserIdEmpfaenger);
            anzInArray = readIntern(lPStm);

        } catch (Exception e) {
            CaBug.drucke("003");
            System.err.println(" " + e.getMessage());
            return (-1);
        }
        return (anzInArray);
    }

    /** Return-Wert = Anzahl der gefundenen Sätze (oder Fehlermeldung <0)*/
    public int readZuSender(int pUserIdSender) {
        PreparedStatement lPStm = null;
        int anzInArray = 0;
        try {
            String lSql = "SELECT * from " + getSchema() + getTableName()+ 
                    " nac where nac.userIdAbsender=? ORDER BY nac.ident;";
            lPStm = verbindung.prepareStatement(lSql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            lPStm.setInt(1, pUserIdSender);
            anzInArray = readIntern(lPStm);

        } catch (Exception e) {
            CaBug.drucke("003");
            System.err.println(" " + e.getMessage());
            return (-1);
        }
        return (anzInArray);
    }

    /** Return-Wert = Anzahl der gefundenen Sätze (oder Fehlermeldung <0)*/
    public int readZuIdentMail(int pidentMail, int pDbServerIdent) {
        PreparedStatement lPStm = null;
        int anzInArray = 0;
        try {
            String lSql = "SELECT * from " + getSchema() + getTableName()+ 
                    " nac where nac.identMail=? AND nac.dbServerIdent=? ORDER BY nac.ident;";
            lPStm = verbindung.prepareStatement(lSql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            lPStm.setInt(1, pidentMail);
            lPStm.setInt(2, pDbServerIdent);
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
    public int update(EclNachricht pEcl) {
        int anzahl=0;
        initFelder();

        pEcl.db_version++;

        try {

            String lSql = setzeUpdateBasisStringZusammen(felder)
                    + " WHERE "
                    + "ident=? AND db_version=? AND dbServerIdent=? ";

            PreparedStatement lPStm = verbindung.prepareStatement(lSql);
            fuellePreparedStatementKomplett(lPStm, 1, pEcl);
            lPStm.setInt(getAnzFelder() + 1, pEcl.ident);
            lPStm.setLong(getAnzFelder() + 2, pEcl.db_version - 1);
            lPStm.setInt(getAnzFelder() + 3, pEcl.dbServerIdent);

            anzahl = updateIntern(lPStm);
        } catch (Exception e1) {
            CaBug.drucke("001");
            System.err.println(" " + e1.getMessage());
            return (-1);
        }

        return anzahl;
    }

    public int updateMandant() {
        return dbBundle.dbLowLevel.rawUpdateMandant(dbBundle.getSchemaMandant() + getTableName());
    }

    public int setzeEinblenden(int pIdent, int pDbServerIdent) {
        int anzahl=0;

        try {

            String lSql = "UPDATE " + getSchema() + getTableName()+" SET "
                    + "anzeigeBeimEmpfaengerAusblenden=0 " + "WHERE " + "ident=?  AND dbServerIdent=? ";

            PreparedStatement lPStm = verbindung.prepareStatement(lSql);
            lPStm.setInt(1, pIdent);
            lPStm.setInt(2, pDbServerIdent);

            anzahl = updateIntern(lPStm);
        } catch (Exception e1) {
            CaBug.drucke("001");
            System.err.println(" " + e1.getMessage());
            return (-1);
        }

        return anzahl;
    }

    public int setzeAusblenden(int pIdent, int pDbServerIdent) {
        int anzahl=0;

        try {

            String lSql = "UPDATE " + getSchema() + getTableName()+" SET "
                    + "anzeigeBeimEmpfaengerAusblenden=1 " + "WHERE " + "ident=?  AND dbServerIdent=? ";

            PreparedStatement lPStm = verbindung.prepareStatement(lSql);
            lPStm.setInt(1, pIdent);
            lPStm.setInt(2, pDbServerIdent);

            anzahl = updateIntern(lPStm);
        } catch (Exception e1) {
            CaBug.drucke("001");
            System.err.println(" " + e1.getMessage());
            return (-1);
        }

        return anzahl;
    }

    public int setzeBearbeitet(int pIdent, int pDbServerIdent) {
        int anzahl=0;

        try {

            String lSql = "UPDATE " + getSchema() + getTableName()+" SET "
                    + "mailIstBearbeitetVomEmpfaengerGesetzt=1 " + "WHERE " + "ident=? AND pDbServerIdent=?  ";

            PreparedStatement lPStm = verbindung.prepareStatement(lSql);
            lPStm.setInt(1, pIdent);
            lPStm.setInt(2, pDbServerIdent);

            anzahl = updateIntern(lPStm);
        } catch (Exception e1) {
            CaBug.drucke("001");
            System.err.println(" " + e1.getMessage());
            return (-1);
        }

        return anzahl;
    }

    public int setzeUnbearbeitet(int pIdent, int pDbServerIdent) {
        int anzahl=0;

        try {

            String lSql = "UPDATE " + getSchema() + getTableName()+" SET "
                    + "mailIstBearbeitetVomEmpfaengerGesetzt=0 " + "WHERE " + "ident=? AND dbServerIdent=? ";

            PreparedStatement lPStm = verbindung.prepareStatement(lSql);
            lPStm.setInt(1, pIdent);
            lPStm.setInt(2, pDbServerIdent);

            anzahl = updateIntern(lPStm);
        } catch (Exception e1) {
            CaBug.drucke("001");
            System.err.println(" " + e1.getMessage());
            return (-1);
        }

        return anzahl;
    }

    public int setzeGelesenBeimEmpfaenger(int pIdent, int pDbServerIdent) {
        int anzahl=0;

        try {

            String lSql = "UPDATE " + getSchema() + getTableName()+" SET "
                    + "anzeigeBeimEmpfaengerGelesen=1 " + "WHERE " + "ident=? AND dbServerIdent=? ";

            PreparedStatement lPStm = verbindung.prepareStatement(lSql);
            lPStm.setInt(1, pIdent);
            lPStm.setInt(2, pDbServerIdent);

            anzahl = updateIntern(lPStm);
        } catch (Exception e1) {
            CaBug.drucke("001");
            System.err.println(" " + e1.getMessage());
            return (-1);
        }

        return anzahl;
    }

    /**Return-Werte:
     * pfXyWurdeVonAnderemBenutzerVeraendert
     * -1 => undefinierter Fehler
     * 1 => Löschen erfolgreich
     */
    public int delete(int pIdent, int pDbServerIdent) {
        int ergebnis=0;
        try {
            String sql = "DELETE FROM " + getSchema()+getTableName()+
                    " WHERE ident=? AND dbServerIdent=?`";
            PreparedStatement pstm1 = verbindung.prepareStatement(sql);
            pstm1.setInt(1, pIdent);
            pstm1.setInt(2, pDbServerIdent);
            ergebnis = deleteIntern(pstm1);
        } catch (Exception e1) {
            CaBug.drucke("001");
            System.err.println(" " + e1.getMessage());
            return (-1);
        }

        return ergebnis;
    }

}
