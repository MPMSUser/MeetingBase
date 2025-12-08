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

/**Hinweis: diese Klasse kann als "Musterklasse" / Blaupause für andere Db-Zugriffsklassen verwendet werden*/

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import de.meetingapps.meetingportal.meetComAllg.CaBug;
import de.meetingapps.meetingportal.meetComEntities.EclInsti;

public class DbInsti extends DbRoot<EclInsti> {


    /*************************Initialisierung***************************/
    public DbInsti(DbBundle pDbBundle) {
        super(pDbBundle);
    }

    @Override
    String getCreateString() {

//      @formatter:off
       String createString = "CREATE TABLE " + dbBundle.getSchemaAllgemein() + "tbl_insti ( "
               + "`mandant` int(11) NOT NULL, " + "`ident` int(11) NOT NULL, "
               + "`db_version` bigint(20) DEFAULT NULL, " + "`kurzBezeichnung` varchar(80) DEFAULT NULL, "
               + "`identHauptKontakt` int(11) DEFAULT NULL, " + "`identSuchlaufBegriffe` int(11) DEFAULT NULL, "
               + "`standardSammelkartenAnlageAktiv` INT(11) NULL DEFAULT '0', " + "`weisungsWeitergabe` INT(11) NULL DEFAULT '0', "
               + "`standardSammelkarteMitWeisung` INT(11) NULL DEFAULT '0', "
               + "`standardSammelkarteOhneWeisung` INT(11) NULL DEFAULT '0', "
               + "`standardSammelkarteSRV` INT(11) NULL DEFAULT '0', "
               + "`standardSammelkarteBriefwahl` INT(11) NULL DEFAULT '0', "
               + "`standardSammelkarteDauervollmacht` INT(11) NULL DEFAULT '0', "
               + "`standardSammelkarteGruppennummer` INT(11) NULL DEFAULT '0', "
               + "`standardMeldeName` varchar(80) DEFAULT NULL, " + "`standardMeldeOrt` varchar(80) DEFAULT NULL, "
               + "`festadressenNummer` int(11) DEFAULT NULL, " + "PRIMARY KEY (`mandant`,`ident`) " + ") ";
//      @formatter:on
        return createString;
    }

    @Override
    String getSchema() {
        return dbBundle.getSchemaAllgemein();
    }

    @Override
    void resetInterneIdent(int pHoechsteIdent) {
        dbBundle.dbBasis.resetInterneIdentInsti(pHoechsteIdent);
        return ;
    }

    @Override
    String getTableName() {
        return "tbl_insti";
    }

    @Override
    String getFeldFuerInterneIdent() {
        return "ident";
    }

    @Override
    int getAnzFelder() {
        return 17;
    }

    /**Sollte nur verwendet werden, um alte DB-Klassen von Array auf List umstellen zu können,
     * ohne alle Folge-Funktionen umzubauen
     * @return
     */
    public EclInsti[] ergebnisAlsArray(){
        EclInsti[] rcArray=new EclInsti[anzErgebnis()];
        for (int i=0;i<anzErgebnis();i++) {
            rcArray[i]=ergebnisPosition(i);
        }
        return rcArray;
        
    }

    @Override
    EclInsti decodeErgebnis(ResultSet pErgebnis) {

        EclInsti lEclReturn = new EclInsti();

        try {
            lEclReturn.mandant = pErgebnis.getInt("mandant");
            lEclReturn.ident = pErgebnis.getInt("ident");
            lEclReturn.db_version = pErgebnis.getLong("db_version");

            lEclReturn.kurzBezeichnung = pErgebnis.getString("kurzBezeichnung");
            lEclReturn.identHauptKontakt = pErgebnis.getInt("identHauptKontakt");
            lEclReturn.identSuchlaufBegriffe = pErgebnis.getInt("identSuchlaufBegriffe");
            
            lEclReturn.standardSammelkartenAnlageAktiv = pErgebnis.getInt("standardSammelkartenAnlageAktiv");
            lEclReturn.weisungsWeitergabe = pErgebnis.getInt("weisungsWeitergabe");

            lEclReturn.standardSammelkarteMitWeisung = pErgebnis.getInt("standardSammelkarteMitWeisung");
            lEclReturn.standardSammelkarteOhneWeisung = pErgebnis.getInt("standardSammelkarteOhneWeisung");
            lEclReturn.standardSammelkarteSRV = pErgebnis.getInt("standardSammelkarteSRV");
            lEclReturn.standardSammelkarteBriefwahl = pErgebnis.getInt("standardSammelkarteBriefwahl");
            lEclReturn.standardSammelkarteDauervollmacht = pErgebnis.getInt("standardSammelkarteDauervollmacht");
            
            lEclReturn.standardSammelkarteGruppennummer = pErgebnis.getInt("standardSammelkarteGruppennummer");

            lEclReturn.standardMeldeName = pErgebnis.getString("standardMeldeName");
            lEclReturn.standardMeldeOrt = pErgebnis.getString("standardMeldeOrt");

            lEclReturn.festadressenNummer = pErgebnis.getInt("festadressenNummer");
        } catch (Exception e) {
            CaBug.drucke("001");
            System.err.println(" " + e.getMessage());
        }

        return lEclReturn;
    }


    @Override
    void fuellePreparedStatementKomplett(PreparedStatement pPStm, int pOffset, EclInsti pEcl) {

        int startOffset = pOffset; /*Nur erforderlich zum Überprüfen von Programmierfehlern - Feldanzahl*/

        try {
            pPStm.setInt(pOffset, pEcl.mandant);
            pOffset++;
            pPStm.setInt(pOffset, pEcl.ident);
            pOffset++;
            pPStm.setLong(pOffset, pEcl.db_version);
            pOffset++;

            pPStm.setString(pOffset, pEcl.kurzBezeichnung);
            pOffset++;
            pPStm.setInt(pOffset, pEcl.identHauptKontakt);
            pOffset++;
            pPStm.setInt(pOffset, pEcl.identSuchlaufBegriffe);
            pOffset++;
            
            pPStm.setInt(pOffset, pEcl.standardSammelkartenAnlageAktiv);
            pOffset++;
            pPStm.setInt(pOffset, pEcl.weisungsWeitergabe);
            pOffset++;

            pPStm.setInt(pOffset, pEcl.standardSammelkarteMitWeisung);
            pOffset++;
            pPStm.setInt(pOffset, pEcl.standardSammelkarteOhneWeisung);
            pOffset++;
            pPStm.setInt(pOffset, pEcl.standardSammelkarteSRV);
            pOffset++;
            pPStm.setInt(pOffset, pEcl.standardSammelkarteBriefwahl);
            pOffset++;
            pPStm.setInt(pOffset, pEcl.standardSammelkarteDauervollmacht);
            pOffset++;
            
            pPStm.setInt(pOffset, pEcl.standardSammelkarteGruppennummer);
            pOffset++;

            pPStm.setString(pOffset, pEcl.standardMeldeName);
            pOffset++;
            pPStm.setString(pOffset, pEcl.standardMeldeOrt);
            pOffset++;

            pPStm.setInt(pOffset, pEcl.festadressenNummer);
            pOffset++;

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
        felder[0]="mandant";
        felder[1]="ident";
        felder[2]="db_version";
        felder[3]="kurzBezeichnung";
        felder[4]="identHauptKontakt";
        felder[5]="identSuchlaufBegriffe";
        
        felder[6]="standardSammelkartenAnlageAktiv";
        felder[7]="weisungsWeitergabe";

        felder[8]="standardSammelkarteMitWeisung";
        felder[9]="standardSammelkarteOhneWeisung";
        felder[10]="standardSammelkarteSRV";
        felder[11]="standardSammelkarteBriefwahl";
        felder[12]="standardSammelkarteDauervollmacht";
        felder[13]="standardSammelkarteGruppennummer";
        felder[14]="standardMeldeName";
        felder[15]="standardMeldeOrt";
        felder[16]="festadressenNummer";
        initErfolgt=true;
    }

    
    @Override
    public int insert(EclInsti pEcl) {

        /* neue InterneIdent vergeben */
        int erg = 0;
        erg = dbBasis.getInterneIdentInsti();
        if (erg < 1) {
            CaBug.drucke("002");
            return (erg);
        }

        pEcl.ident = erg;

        pEcl.mandant = 0;

        initFelder();
        return insertIntern(felder, pEcl);
    }

    /**Mandant und Ident müssen übergeben werden.
     * 
     * Return-Wert = Anzahl der gefundenen Sätze (oder Fehlermeldung <0)*/
    public int readZuIdent(int pIdent) {
        int anzInArray = 0;
        PreparedStatement lPStm = null;

        try {
            String lSql = "SELECT * from " + getSchema() + getTableName() + " where "
                    + "ident=? " + "ORDER BY ident;";
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

    public int readAlle() {
        int anzInArray = 0;
        PreparedStatement lPStm = null;

        try {
            String lSql = "SELECT * from " + getSchema() + getTableName()  
                    + " ORDER BY ident;";
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
    public int update(EclInsti pEcl) {
        int ergebnis=0;
        initFelder();

        pEcl.db_version++;

        try {
            String lSql = setzeUpdateBasisStringZusammen(felder)
                    + " WHERE "
                    + "ident=? AND db_version=? ";
 
            PreparedStatement lPStm = verbindung.prepareStatement(lSql);
            fuellePreparedStatementKomplett(lPStm, 1, pEcl);
            lPStm.setInt(getAnzFelder() + 1, pEcl.ident);
            lPStm.setLong(getAnzFelder() + 2, pEcl.db_version - 1);

            ergebnis = updateIntern(lPStm);
        } catch (Exception e1) {
            CaBug.drucke("001");
            System.err.println(" " + e1.getMessage());
            return (-1);
        }

        return ergebnis;
    }

 
}
