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
import de.meetingapps.meetingportal.meetComEntities.EclAuftrag;
import de.meetingapps.meetingportal.meetComKonst.KonstAuftragStatus;

public class DbAuftrag  extends DbRoot<EclAuftrag>  {

//    private int logDrucken=10;
    
    /**Ggf. vor Benutzung setzen; anschließend unbedingt wieder auf true setzen!*/
    public boolean mandantenabhaengig=true;

    public DbAuftrag(DbBundle pDbBundle) {
        super(pDbBundle);
    }

    @Override
    String getCreateString() {

//      @formatter:off
       String createString = "CREATE TABLE " + getSchema() + getTableName()+" ( "
                + "`ident` int(11) NOT NULL, "
                + "`identVersion` int(11) NOT NULL, "
                + "`db_version` bigint(20) DEFAULT NULL, " 
                + "`dbServerIdent` int(11) NOT NULL, "
                + "`lfdNr` int(11) NOT NULL, "
                
                + "`mandant` int(11) NOT NULL, "
                + "`hvJahr` int(11) NOT NULL, "
                + "`hvNummer` char(4) DEFAULT NULL, " 
                + "`datenbereich` char(1) DEFAULT NULL, " 
                
                + "`gehoertZuModul` int(11) NOT NULL, "
                + "`auftragsArt` int(11) NOT NULL, "
                + "`status` int(11) NOT NULL, "
                + "`statusAuftragsArt` int(11) NOT NULL, "
                + "`statusInfoDetail` int(11) NOT NULL, "
                + "`statusInfoFreitext` varchar(1000) DEFAULT NULL, " 
                
                + "`eingangsweg` int(11) DEFAULT NULL, " 
                + "`userIdAuftraggeber` int(11) DEFAULT NULL, " 
                + "`freitextAuftraggeber` varchar(1000) DEFAULT NULL, " 
                + "`verweisAufImport` varchar(100) DEFAULT NULL, " 
                + "`userIdErfasser` int(11) DEFAULT NULL, " 
                + "`userIdVerarbeitet` int(11) DEFAULT NULL, " 
                + "`userIdAktuellerBearbeiter` int(11) DEFAULT NULL, " 
                + "`drucklaufNr` int(11) DEFAULT NULL, " 
               
                + "`schluessel` varchar(200) DEFAULT NULL, " 
                + "`zeitStart` varchar(19) DEFAULT NULL, " 
                + "`zeitErledigt` varchar(19) DEFAULT NULL, "
                + "`zeitLetzterStatuswechsel` varchar(19) DEFAULT NULL, "
                
                + "`startAnzeigeGelesen` int(11) NOT NULL, "
                + "`startAnzeigeGeloescht` int(11) NOT NULL, "
                + "`erledigtAnzeigeGelesen` int(11) NOT NULL, "
                + "`erledigtAnzeigeGeloescht` int(11) NOT NULL, "
                + "`statusAuftragsArtGelesen` int(11) NOT NULL, "
                + "`statusAuftragsArtGeloescht` int(11) NOT NULL, "

                + "`freitextBeschreibung` varchar(10000) DEFAULT NULL, " 

                + "`parameterTextLang0` varchar(300) NOT NULL, "
                + "`parameterTextLang1` varchar(300) NOT NULL, "
                + "`parameterTextLang2` varchar(300) NOT NULL, "
                + "`parameterTextLang3` varchar(300) NOT NULL, "
                + "`parameterTextLang4` varchar(300) NOT NULL, "
                + "`parameterTextLang5` varchar(300) NOT NULL, "
                + "`parameterTextLang6` varchar(300) NOT NULL, "
                + "`parameterTextLang7` varchar(300) NOT NULL, "
                + "`parameterTextLang8` varchar(300) NOT NULL, "
                + "`parameterTextLang9` varchar(300) NOT NULL, "
                + "`parameterTextKurz0` varchar(40) NOT NULL, "
                + "`parameterTextKurz1` varchar(40) NOT NULL, "
                + "`parameterTextKurz2` varchar(40) NOT NULL, "
                + "`parameterTextKurz3` varchar(40) NOT NULL, "
                + "`parameterTextKurz4` varchar(40) NOT NULL, "
                + "`parameterInt0` int(11) NOT NULL, "
                + "`parameterInt1` int(11) NOT NULL, "
                + "`parameterInt2` int(11) NOT NULL, "
                + "`parameterInt3` int(11) NOT NULL, "
                + "`parameterInt4` int(11) NOT NULL, "
                + "`ergebnisTextLang0` varchar(300) NOT NULL, "
                + "`ergebnisTextLang1` varchar(300) NOT NULL, "
                + "`ergebnisTextLang2` varchar(300) NOT NULL, "
                + "`ergebnisTextLang3` varchar(300) NOT NULL, "
                + "`ergebnisTextLang4` varchar(300) NOT NULL, "
                + "`ergebnisTextLang5` varchar(300) NOT NULL, "
                + "`ergebnisTextLang6` varchar(300) NOT NULL, "
                + "`ergebnisTextLang7` varchar(300) NOT NULL, "
                + "`ergebnisTextLang8` varchar(300) NOT NULL, "
                + "`ergebnisTextLang9` varchar(300) NOT NULL, "
                + "`ergebnisTextKurz0` varchar(300) NOT NULL, "
                + "`ergebnisTextKurz1` varchar(300) NOT NULL, "
                + "`ergebnisTextKurz2` varchar(300) NOT NULL, "
                + "`ergebnisTextKurz3` varchar(300) NOT NULL, "
                + "`ergebnisTextKurz4` varchar(300) NOT NULL, "
                + "`ergebnisInt0` int(11) NOT NULL, "
                + "`ergebnisInt1` int(11) NOT NULL, "
                + "`ergebnisInt2` int(11) NOT NULL, "
                + "`ergebnisInt3` int(11) NOT NULL, "
                + "`ergebnisInt4` int(11) NOT NULL, "
                + "PRIMARY KEY (`ident`, `identVersion`, `dbServerIdent`, `lfdNr`) " + ") ";
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
            dbBasis.resetInterneIdentAuftrag();
        }
        else {
            dbBasis.resetInterneIdentAuftragOhneMandant();
        }
    }

    @Override
    String getTableName() {
        return "tbl_auftrag";
    }

    @Override
    String getFeldFuerInterneIdent() {
        return "ident";
    }

    @Override
    int getAnzFelder() {
        return 74;
    }

    
    @Override
    EclAuftrag decodeErgebnis(ResultSet pErgebnis) {
        EclAuftrag lEclReturn = new EclAuftrag();

        try {
            lEclReturn.ident = pErgebnis.getInt("ident");
            lEclReturn.identVersion = pErgebnis.getInt("identVersion");
            lEclReturn.db_version = pErgebnis.getLong("db_version");
            lEclReturn.dbServerIdent = pErgebnis.getInt("dbServerIdent");
            lEclReturn.lfdNr = pErgebnis.getInt("lfdNr");

            lEclReturn.mandant = pErgebnis.getInt("mandant");
            lEclReturn.hvJahr = pErgebnis.getInt("hvJahr");
            lEclReturn.hvNummer = pErgebnis.getString("hvNummer");
            lEclReturn.datenbereich = pErgebnis.getString("datenbereich");

            lEclReturn.gehoertZuModul = pErgebnis.getInt("gehoertZuModul");
            lEclReturn.auftragsArt = pErgebnis.getInt("auftragsArt");
            lEclReturn.status = pErgebnis.getInt("status");
            lEclReturn.statusAuftragsArt = pErgebnis.getInt("statusAuftragsArt");
            lEclReturn.statusInfoDetail = pErgebnis.getInt("statusInfoDetail");
            lEclReturn.statusInfoFreitext = pErgebnis.getString("statusInfoFreitext");

            lEclReturn.eingangsweg = pErgebnis.getInt("eingangsweg");
            lEclReturn.userIdAuftraggeber = pErgebnis.getInt("userIdAuftraggeber");
            lEclReturn.freitextAuftraggeber = pErgebnis.getString("freitextAuftraggeber");
            lEclReturn.verweisAufImport = pErgebnis.getString("verweisAufImport");
            lEclReturn.userIdErfasser = pErgebnis.getInt("userIdAuftraggeber");
            lEclReturn.userIdVerarbeitet = pErgebnis.getInt("userIdVerarbeitet");
            lEclReturn.userIdAktuellerBearbeiter = pErgebnis.getInt("userIdAktuellerBearbeiter");
            lEclReturn.drucklaufNr = pErgebnis.getInt("drucklaufNr");
            
            lEclReturn.schluessel = pErgebnis.getString("schluessel");
            lEclReturn.zeitStart = pErgebnis.getString("zeitStart");
            lEclReturn.zeitErledigt = pErgebnis.getString("zeitErledigt");
            lEclReturn.zeitLetzterStatuswechsel = pErgebnis.getString("zeitLetzterStatuswechsel");

            lEclReturn.startAnzeigeGelesen = pErgebnis.getInt("startAnzeigeGelesen");
            lEclReturn.startAnzeigeGeloescht = pErgebnis.getInt("startAnzeigeGeloescht");
            lEclReturn.erledigtAnzeigeGelesen = pErgebnis.getInt("erledigtAnzeigeGelesen");
            lEclReturn.erledigtAnzeigeGeloescht = pErgebnis.getInt("erledigtAnzeigeGeloescht");
            lEclReturn.statusAuftragsArtGelesen = pErgebnis.getInt("statusAuftragsArtGelesen");
            lEclReturn.statusAuftragsArtGeloescht = pErgebnis.getInt("statusAuftragsArtGeloescht");

            lEclReturn.freitextBeschreibung = pErgebnis.getString("freitextBeschreibung");
            
            lEclReturn.parameterTextLang[0] = pErgebnis.getString("parameterTextLang0");
            lEclReturn.parameterTextLang[1] = pErgebnis.getString("parameterTextLang1");
            lEclReturn.parameterTextLang[2] = pErgebnis.getString("parameterTextLang2");
            lEclReturn.parameterTextLang[3] = pErgebnis.getString("parameterTextLang3");
            lEclReturn.parameterTextLang[4] = pErgebnis.getString("parameterTextLang4");
            lEclReturn.parameterTextLang[5] = pErgebnis.getString("parameterTextLang5");
            lEclReturn.parameterTextLang[6] = pErgebnis.getString("parameterTextLang6");
            lEclReturn.parameterTextLang[7] = pErgebnis.getString("parameterTextLang7");
            lEclReturn.parameterTextLang[8] = pErgebnis.getString("parameterTextLang8");
            lEclReturn.parameterTextLang[9] = pErgebnis.getString("parameterTextLang9");

            lEclReturn.parameterTextKurz[0] = pErgebnis.getString("parameterTextKurz0");
            lEclReturn.parameterTextKurz[1] = pErgebnis.getString("parameterTextKurz1");
            lEclReturn.parameterTextKurz[2] = pErgebnis.getString("parameterTextKurz2");
            lEclReturn.parameterTextKurz[3] = pErgebnis.getString("parameterTextKurz3");
            lEclReturn.parameterTextKurz[4] = pErgebnis.getString("parameterTextKurz4");

            lEclReturn.parameterInt[0] = pErgebnis.getInt("parameterInt0");
            lEclReturn.parameterInt[1] = pErgebnis.getInt("parameterInt1");
            lEclReturn.parameterInt[2] = pErgebnis.getInt("parameterInt2");
            lEclReturn.parameterInt[3] = pErgebnis.getInt("parameterInt3");
            lEclReturn.parameterInt[4] = pErgebnis.getInt("parameterInt4");

            lEclReturn.ergebnisTextLang[0] = pErgebnis.getString("ergebnisTextLang0");
            lEclReturn.ergebnisTextLang[1] = pErgebnis.getString("ergebnisTextLang1");
            lEclReturn.ergebnisTextLang[2] = pErgebnis.getString("ergebnisTextLang2");
            lEclReturn.ergebnisTextLang[3] = pErgebnis.getString("ergebnisTextLang3");
            lEclReturn.ergebnisTextLang[4] = pErgebnis.getString("ergebnisTextLang4");
            lEclReturn.ergebnisTextLang[5] = pErgebnis.getString("ergebnisTextLang5");
            lEclReturn.ergebnisTextLang[6] = pErgebnis.getString("ergebnisTextLang6");
            lEclReturn.ergebnisTextLang[7] = pErgebnis.getString("ergebnisTextLang7");
            lEclReturn.ergebnisTextLang[8] = pErgebnis.getString("ergebnisTextLang8");
            lEclReturn.ergebnisTextLang[9] = pErgebnis.getString("ergebnisTextLang9");

            lEclReturn.ergebnisTextKurz[0] = pErgebnis.getString("ergebnisTextKurz0");
            lEclReturn.ergebnisTextKurz[1] = pErgebnis.getString("ergebnisTextKurz1");
            lEclReturn.ergebnisTextKurz[2] = pErgebnis.getString("ergebnisTextKurz2");
            lEclReturn.ergebnisTextKurz[3] = pErgebnis.getString("ergebnisTextKurz3");
            lEclReturn.ergebnisTextKurz[4] = pErgebnis.getString("ergebnisTextKurz4");

            lEclReturn.ergebnisInt[0] = pErgebnis.getInt("ergebnisInt0");
            lEclReturn.ergebnisInt[1] = pErgebnis.getInt("ergebnisInt1");
            lEclReturn.ergebnisInt[2] = pErgebnis.getInt("ergebnisInt2");
            lEclReturn.ergebnisInt[3] = pErgebnis.getInt("ergebnisInt3");
            lEclReturn.ergebnisInt[4] = pErgebnis.getInt("ergebnisInt4");
       } catch (Exception e) {
            CaBug.drucke("001");
            System.err.println(" " + e.getMessage());
        }

        return lEclReturn;
    }

    @Override
    void fuellePreparedStatementKomplett(PreparedStatement pPStm, int pOffset, EclAuftrag pEcl) {
        int startOffset = pOffset; /*Nur erforderlich zum Überprüfen von Programmierfehlern - Feldanzahl*/

        try {
//          @formatter:off
            
            pPStm.setInt(pOffset, pEcl.ident);pOffset++;
            pPStm.setInt(pOffset, pEcl.identVersion);pOffset++;
            pPStm.setLong(pOffset, pEcl.db_version);pOffset++;
            pPStm.setInt(pOffset, pEcl.dbServerIdent);pOffset++;
            pPStm.setInt(pOffset, pEcl.lfdNr);pOffset++;
            
            pPStm.setInt(pOffset, pEcl.mandant);pOffset++;
            pPStm.setInt(pOffset, pEcl.hvJahr);pOffset++;
            pPStm.setString(pOffset, pEcl.hvNummer);pOffset++;
            pPStm.setString(pOffset, pEcl.datenbereich);pOffset++;
            
            pPStm.setInt(pOffset, pEcl.gehoertZuModul);pOffset++;
            pPStm.setInt(pOffset, pEcl.auftragsArt);pOffset++;
            pPStm.setInt(pOffset, pEcl.status);pOffset++;
            pPStm.setInt(pOffset, pEcl.statusAuftragsArt);pOffset++;
            pPStm.setInt(pOffset, pEcl.statusInfoDetail);pOffset++;
            pPStm.setString(pOffset, pEcl.statusInfoFreitext);pOffset++;
           
            pPStm.setInt(pOffset, pEcl.eingangsweg);pOffset++;
            pPStm.setInt(pOffset, pEcl.userIdAuftraggeber);pOffset++;
            pPStm.setString(pOffset, pEcl.freitextAuftraggeber);pOffset++;
            pPStm.setString(pOffset, pEcl.verweisAufImport);pOffset++;
            pPStm.setInt(pOffset, pEcl.userIdErfasser);pOffset++;
            pPStm.setInt(pOffset, pEcl.userIdVerarbeitet);pOffset++;
            pPStm.setInt(pOffset, pEcl.userIdAktuellerBearbeiter);pOffset++;
            pPStm.setInt(pOffset, pEcl.drucklaufNr);pOffset++;
            
            pPStm.setString(pOffset, pEcl.schluessel);pOffset++;
            pPStm.setString(pOffset, pEcl.zeitStart);pOffset++;
            pPStm.setString(pOffset, pEcl.zeitErledigt);pOffset++;
            pPStm.setString(pOffset, pEcl.zeitLetzterStatuswechsel);pOffset++;

            pPStm.setInt(pOffset, pEcl.startAnzeigeGelesen);pOffset++;
            pPStm.setInt(pOffset, pEcl.startAnzeigeGeloescht);pOffset++;
            pPStm.setInt(pOffset, pEcl.erledigtAnzeigeGelesen);pOffset++;
            pPStm.setInt(pOffset, pEcl.erledigtAnzeigeGeloescht);pOffset++;
            pPStm.setInt(pOffset, pEcl.statusAuftragsArtGelesen);pOffset++;
            pPStm.setInt(pOffset, pEcl.statusAuftragsArtGeloescht);pOffset++;

            pPStm.setString(pOffset, pEcl.freitextBeschreibung);pOffset++;

            pPStm.setString(pOffset, pEcl.parameterTextLang[0]);pOffset++;
            pPStm.setString(pOffset, pEcl.parameterTextLang[1]);pOffset++;
            pPStm.setString(pOffset, pEcl.parameterTextLang[2]);pOffset++;
            pPStm.setString(pOffset, pEcl.parameterTextLang[3]);pOffset++;
            pPStm.setString(pOffset, pEcl.parameterTextLang[4]);pOffset++;
            pPStm.setString(pOffset, pEcl.parameterTextLang[5]);pOffset++;
            pPStm.setString(pOffset, pEcl.parameterTextLang[6]);pOffset++;
            pPStm.setString(pOffset, pEcl.parameterTextLang[7]);pOffset++;
            pPStm.setString(pOffset, pEcl.parameterTextLang[8]);pOffset++;
            pPStm.setString(pOffset, pEcl.parameterTextLang[9]);pOffset++;

            pPStm.setString(pOffset, pEcl.parameterTextKurz[0]);pOffset++;
            pPStm.setString(pOffset, pEcl.parameterTextKurz[1]);pOffset++;
            pPStm.setString(pOffset, pEcl.parameterTextKurz[2]);pOffset++;
            pPStm.setString(pOffset, pEcl.parameterTextKurz[3]);pOffset++;
            pPStm.setString(pOffset, pEcl.parameterTextKurz[4]);pOffset++;

            pPStm.setInt(pOffset, pEcl.parameterInt[0]);pOffset++;
            pPStm.setInt(pOffset, pEcl.parameterInt[1]);pOffset++;
            pPStm.setInt(pOffset, pEcl.parameterInt[2]);pOffset++;
            pPStm.setInt(pOffset, pEcl.parameterInt[3]);pOffset++;
            pPStm.setInt(pOffset, pEcl.parameterInt[4]);pOffset++;

            pPStm.setString(pOffset, pEcl.ergebnisTextLang[0]);pOffset++;
            pPStm.setString(pOffset, pEcl.ergebnisTextLang[1]);pOffset++;
            pPStm.setString(pOffset, pEcl.ergebnisTextLang[2]);pOffset++;
            pPStm.setString(pOffset, pEcl.ergebnisTextLang[3]);pOffset++;
            pPStm.setString(pOffset, pEcl.ergebnisTextLang[4]);pOffset++;
            pPStm.setString(pOffset, pEcl.ergebnisTextLang[5]);pOffset++;
            pPStm.setString(pOffset, pEcl.ergebnisTextLang[6]);pOffset++;
            pPStm.setString(pOffset, pEcl.ergebnisTextLang[7]);pOffset++;
            pPStm.setString(pOffset, pEcl.ergebnisTextLang[8]);pOffset++;
            pPStm.setString(pOffset, pEcl.ergebnisTextLang[9]);pOffset++;

            pPStm.setString(pOffset, pEcl.ergebnisTextKurz[0]);pOffset++;
            pPStm.setString(pOffset, pEcl.ergebnisTextKurz[1]);pOffset++;
            pPStm.setString(pOffset, pEcl.ergebnisTextKurz[2]);pOffset++;
            pPStm.setString(pOffset, pEcl.ergebnisTextKurz[3]);pOffset++;
            pPStm.setString(pOffset, pEcl.ergebnisTextKurz[4]);pOffset++;

            pPStm.setInt(pOffset, pEcl.ergebnisInt[0]);pOffset++;
            pPStm.setInt(pOffset, pEcl.ergebnisInt[1]);pOffset++;
            pPStm.setInt(pOffset, pEcl.ergebnisInt[2]);pOffset++;
            pPStm.setInt(pOffset, pEcl.ergebnisInt[3]);pOffset++;
            pPStm.setInt(pOffset, pEcl.ergebnisInt[4]);pOffset++;
           
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
        felder= new String[] {
                "ident",
                "identVersion",
                "db_version",
                "dbServerIdent",
                "lfdNr",

                "mandant",
                "hvJahr",
                "hvNummer",
                "datenbereich",

                "gehoertZuModul",
                "auftragsArt",
                "status",
                "statusAuftragsArt",
                "statusInfoDetail",
                "statusInfoFreitext",

                "eingangsweg",
                "userIdAuftraggeber",
                "freitextAuftraggeber",
                "verweisAufImport",
                "userIdErfasser",
                "userIdVerarbeitet",
                "userIdAktuellerBearbeiter",
                "drucklaufNr",
               
                "schluessel",
                "zeitStart",
                "zeitErledigt",
                "zeitLetzterStatuswechsel",

                "startAnzeigeGelesen",
                "startAnzeigeGeloescht",
                "erledigtAnzeigeGelesen",
                "erledigtAnzeigeGeloescht",
                "statusAuftragsArtGelesen",
                "statusAuftragsArtGeloescht",

                "freitextBeschreibung",

                "parameterTextLang0",
                "parameterTextLang1",
                "parameterTextLang2",
                "parameterTextLang3",
                "parameterTextLang4",
                "parameterTextLang5",
                "parameterTextLang6",
                "parameterTextLang7",
                "parameterTextLang8",
                "parameterTextLang9",

                "parameterTextKurz0",
                "parameterTextKurz1",
                "parameterTextKurz2",
                "parameterTextKurz3",
                "parameterTextKurz4",

                "parameterInt0",
                "parameterInt1",
                "parameterInt2",
                "parameterInt3",
                "parameterInt4",

                "ergebnisTextLang0",
                "ergebnisTextLang1",
                "ergebnisTextLang2",
                "ergebnisTextLang3",
                "ergebnisTextLang4",
                "ergebnisTextLang5",
                "ergebnisTextLang6",
                "ergebnisTextLang7",
                "ergebnisTextLang8",
                "ergebnisTextLang9",

                "ergebnisTextKurz0",
                "ergebnisTextKurz1",
                "ergebnisTextKurz2",
                "ergebnisTextKurz3",
                "ergebnisTextKurz4",

                "ergebnisInt0",
                "ergebnisInt1",
                "ergebnisInt2",
                "ergebnisInt3",
                "ergebnisInt4"
        };

        initErfolgt=true;
    }

    /**Einfügen eines neuen Auftrags (d.h. neue ident wird vergeben;
     * identVersion muß vorher 0 gesetzt werden), also nicht geeignet für
     * historischen Eintrag
     */
    @Override
    public int insert(EclAuftrag pEcl) {

        int erg=0;
        if (mandantenabhaengig) {
            erg=dbBasis.getInterneIdentAuftrag();
        }
        else {
            erg=dbBasis.getInterneIdentAuftragOhneMandant();
            pEcl.mandant=dbBundle.clGlobalVar.mandant;
            pEcl.hvJahr=dbBundle.clGlobalVar.hvJahr;
            pEcl.hvNummer=dbBundle.clGlobalVar.hvNummer;
            pEcl.datenbereich=dbBundle.clGlobalVar.datenbereich;
            
        }
        pEcl.ident=erg;
        pEcl.dbServerIdent=dbBundle.paramServer.dbServerIdent;
        
        initFelder();
        return insertIntern(felder, pEcl);
    }

    
    /**Einfügen eines neuen Auftrags (d.h. neue ident wird vergeben;
     * identVersion muß vorher 0 gesetzt werden), also nicht geeignet für
     * historischen Eintrag
     */
    public int insert(EclAuftrag[] pEcl) {
        initFelder();
        
        int erg=0;
        if (mandantenabhaengig) {
            erg=dbBasis.getInterneIdentAuftrag();
        }
        else {
            erg=dbBasis.getInterneIdentAuftragOhneMandant();
        }
        
        int rc=0;
        for (int i=0;i<pEcl.length;i++) {
            pEcl[i].ident=erg;
            pEcl[i].lfdNr=i;
            pEcl[i].dbServerIdent=dbBundle.paramServer.dbServerIdent;
            
            if (mandantenabhaengig==false) {
                pEcl[i].mandant=dbBundle.clGlobalVar.mandant;
                pEcl[i].hvJahr=dbBundle.clGlobalVar.hvJahr;
                pEcl[i].hvNummer=dbBundle.clGlobalVar.hvNummer;
                pEcl[i].datenbereich=dbBundle.clGlobalVar.datenbereich;
            }

            
            rc=insertIntern(felder, pEcl[i]);
            if (rc<1) {return rc;}
        }
        
        return rc;
    }
    
    /*TODO Insert für historische Einträge (d.h. beibehalten von ident, hochzählen von identversion)
     * noch zu tun
     */
    /*TODO Read-Befehle noch zu Ergänzen / Verfeinern*/
    
    /** Return-Wert = Anzahl der gefundenen Sätze (oder Fehlermeldung <0)
     * es werden nur "aktuelle Einträge" geholt, d.h. keine historischen Sätze*/
    public int read_allBenutzer(int pUserIdAuftraggeber) {
        PreparedStatement lPStm = null;
        int anzInArray = 0;
        try {
            if (mandantenabhaengig) {
                String lSql = "SELECT * from " + getSchema() + getTableName()+ 
                        " where userIdAuftraggeber=? AND identVersion=0 ORDER BY ident, lfdNr;";
                lPStm = verbindung.prepareStatement(lSql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                lPStm.setInt(1, pUserIdAuftraggeber);
            }
            else {
                String lSql = "SELECT * from " + getSchema() + getTableName()+ 
                        " where userIdAuftraggeber=? AND identVersion=0 and mandant=? AND hvJahr=? AND hvNummer=? AND datenbereich=? ORDER BY ident, lfdNr;";
                lPStm = verbindung.prepareStatement(lSql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                lPStm.setInt(1, pUserIdAuftraggeber);
                lPStm.setInt(2, dbBundle.clGlobalVar.mandant);
                lPStm.setInt(3, dbBundle.clGlobalVar.hvJahr);
                lPStm.setString(4, dbBundle.clGlobalVar.hvNummer);
                lPStm.setString(5, dbBundle.clGlobalVar.datenbereich);
            }
            anzInArray = readIntern(lPStm);

        } catch (Exception e) {
            CaBug.drucke("003");
            System.err.println(" " + e.getMessage());
            return (-1);
        }
        return (anzInArray);
    }

    /** es werden nur "aktuelle Einträge" geholt, d.h. keine historischen Sätze*/
    public int read_aenderungenInArbeit(int pUserIdAuftraggeber, int pModul, int pAuftragsart) {
        PreparedStatement lPStm = null;
        int anzInArray = 0;
        try {
            String lSql = "SELECT * from " + getSchema() + getTableName()+ 
                    " where userIdAuftraggeber=? AND gehoertZuModul=? AND auftragsArt=? AND status=? "
                    + "AND identVersion=0 ORDER BY ident, lfdNr ;";
            lPStm = verbindung.prepareStatement(lSql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            lPStm.setInt(1, pUserIdAuftraggeber);
            lPStm.setInt(2, pModul);
            lPStm.setInt(3, pAuftragsart);
            lPStm.setInt(4, KonstAuftragStatus.IN_ARBEIT);
            anzInArray = readIntern(lPStm);

        } catch (Exception e) {
            CaBug.drucke("003");
            System.err.println(" " + e.getMessage());
            return (-1);
        }
        return (anzInArray);
    }
 
    
    
    /** es werden nur "aktuelle Einträge" geholt, d.h. keine historischen Sätze*/
    public int read_aenderungenInArbeit(int pUserIdAuftraggeber, int pModul) {
        PreparedStatement lPStm = null;
        int anzInArray = 0;
        try {
            String lSql = "SELECT * from " + getSchema() + getTableName()+ 
                    " where userIdAuftraggeber=? AND gehoertZuModul=? AND status=? "
                    + "AND identVersion=0 ORDER BY ident, lfdNr ;";
            lPStm = verbindung.prepareStatement(lSql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            lPStm.setInt(1, pUserIdAuftraggeber);
            lPStm.setInt(2, pModul);
            lPStm.setInt(3, KonstAuftragStatus.IN_ARBEIT);
            anzInArray = readIntern(lPStm);

        } catch (Exception e) {
            CaBug.drucke("003");
            System.err.println(" " + e.getMessage());
            return (-1);
        }
        return (anzInArray);
    }

    
    /** es werden nur "aktuelle Einträge" geholt, d.h. keine historischen Sätze
     * 
     * pModul gemäß KonstAuftragModul
     * pAuftragsart gemäß KonstAuftragArt
     * */
    public int read_auftraegeVorhanden(int pModul, int pAuftragsart) {
        PreparedStatement lPStm = null;
        int anzInArray = 0;
        try {
            String lSql = "SELECT * from " + getSchema() + getTableName()+ 
                    " where gehoertZuModul=? AND auftragsArt=? AND status=? "
                    + "AND identVersion=0 ORDER BY ident, lfdNr ;";
            lPStm = verbindung.prepareStatement(lSql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            lPStm.setInt(1, pModul);
            lPStm.setInt(2, pAuftragsart);
            lPStm.setInt(3, KonstAuftragStatus.IN_ARBEIT);
            anzInArray = readIntern(lPStm);

        } catch (Exception e) {
            CaBug.drucke("003");
            System.err.println(" " + e.getMessage());
            return (-1);
        }
        return (anzInArray);
    }

  
    /** es werden nur "aktuelle Einträge" geholt, d.h. keine historischen Sätze
     * 
     * pModul gemäß KonstAuftragModul
     * zeitStart von (JJJJ.MM.TT HH:MM:SS) / bis 
     * */
    public int read_auftraegeVorhanden(int pModul, String pVonDatum, String pBisDatum) {
        PreparedStatement lPStm = null;
        int anzInArray = 0;
        try {
            String lSql = "SELECT * from " + getSchema() + getTableName()+ 
                    " where gehoertZuModul=?  "
                    + "AND identVersion=0 AND zeitStart>=? && zeitstart<=? ORDER BY ident, lfdNr ;";
            lPStm = verbindung.prepareStatement(lSql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            lPStm.setInt(1, pModul);
            lPStm.setString(2, pVonDatum);
            lPStm.setString(3, pBisDatum);
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
    public int update(EclAuftrag[] pEcl) {
        int ergebnis=0;
        int anzahl=0;
        initFelder();

        for (int i=0;i<pEcl.length;i++) {
            pEcl[i].db_version++;

            try {

                String lSql = setzeUpdateBasisStringZusammen(felder)
                        + " WHERE "
                        + "ident=? AND identVersion=? AND db_version=? AND dbServerIdent=? AND lfdNr=? ";

                PreparedStatement lPStm = verbindung.prepareStatement(lSql);
                fuellePreparedStatementKomplett(lPStm, 1, pEcl[i]);
                lPStm.setInt(getAnzFelder() + 1, pEcl[i].ident);
                lPStm.setInt(getAnzFelder() + 2, pEcl[i].identVersion);
                lPStm.setLong(getAnzFelder() + 3, pEcl[i].db_version - 1);
                lPStm.setInt(getAnzFelder() + 4, pEcl[i].dbServerIdent);
                lPStm.setInt(getAnzFelder() + 5, pEcl[i].lfdNr);

                ergebnis = updateIntern(lPStm);
                if (ergebnis>0) {anzahl+=ergebnis;}
            } catch (Exception e1) {
                CaBug.drucke("001");
                System.err.println(" " + e1.getMessage());
                return (-1);
            }
        }

        return anzahl;
    }

    public int updateMandant() {
        return dbBundle.dbLowLevel.rawUpdateMandant(dbBundle.getSchemaMandant() + getTableName());
    }

    
    /**Löscht alle Sätze zu pIdent (also eigentlichen Satz und historische Sätze
     * 
     * Return-Werte:
     * pfXyWurdeVonAnderemBenutzerVeraendert
     * -1 => undefinierter Fehler
     * 1 => Löschen erfolgreich
     */
    public int delete(int pIdent, int pDbServerIdent) {
        int ergebnis=0;
        try {
            String sql = "DELETE FROM " + getSchema()+getTableName()+
                    " WHERE ident=? and dbServerIdent=? ";
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

    /**Löscht Aufträge eines Users*/
    public int deleteBenutzer(int pUserIdAuftraggeber) {
        int ergebnis=0;
        try {
            String sql = "DELETE FROM " + getSchema()+getTableName()+
                    " WHERE userIdAuftraggeber=? ";
            PreparedStatement pstm1 = verbindung.prepareStatement(sql);
            pstm1.setInt(1, pUserIdAuftraggeber);
            ergebnis = deleteIntern(pstm1);
        } catch (Exception e1) {
            CaBug.drucke("001");
            System.err.println(" " + e1.getMessage());
            return (-1);
        }

        return ergebnis;
    }

    public int setzeStatusAuftragsArtGelesen(int pIdent, int pDbServerIdent) {
        String lSql="";
        if (mandantenabhaengig) {
            lSql = "UPDATE " + getSchema() + getTableName()+" SET "
                    + "statusAuftragsArtGelesen=1 " + "WHERE " + "ident=? AND identVersion=0 AND dbServerIdent=? ";

        }
        else {
            lSql = "UPDATE " + getSchema() + getTableName()+" SET "
                    + "statusAuftragsArtGelesen=1 " + "WHERE "
                    + "mandant=? AND hvJahr=? AND hvNummer=? AND datenbereich=? AND "
                    + "ident=? AND identVersion=0 AND dbServerIdent=? ";

        }
        return setzeStatus(lSql, pIdent, pDbServerIdent);

    }

    public int setzeStatusAuftragsArtGeloescht(int pIdent, int pDbServerIdent) {

        String lSql="";
        if (mandantenabhaengig) {
            lSql = "UPDATE " + getSchema() + getTableName()+" SET "
                    + "statusAuftragsArtGeloescht=1 " + "WHERE " + "ident=? AND identVersion=0 AND dbServerIdent=? ";

        }
        else {
            lSql = "UPDATE " + getSchema() + getTableName()+" SET "
                    + "statusAuftragsArtGeloescht=1 " + "WHERE "
                    + "mandant=? AND hvJahr=? AND hvNummer=? AND datenbereich=? AND "
                    + "ident=? AND identVersion=0 AND dbServerIdent=? ";

        }
        
        return setzeStatus(lSql, pIdent, pDbServerIdent);
     }

    public int setzeStartAnzeigeGelesen(int pIdent, int pDbServerIdent) {
        String lSql="";
        if (mandantenabhaengig) {
            lSql = "UPDATE " + getSchema() + getTableName()+" SET "
                    + "startAnzeigeGelesen=1 " + "WHERE " + "ident=? AND identVersion=0 AND dbServerIdent=? ";

        }
        else {
            lSql = "UPDATE " + getSchema() + getTableName()+" SET "
                    + "startAnzeigeGelesen=1 " + "WHERE "
                    + "mandant=? AND hvJahr=? AND hvNummer=? AND datenbereich=? AND "
                    + "ident=? AND identVersion=0 AND dbServerIdent=? ";

        }
        return setzeStatus(lSql, pIdent, pDbServerIdent);

    }

    public int setzeStartAnzeigeGeloescht(int pIdent, int pDbServerIdent) {

        String lSql="";
        if (mandantenabhaengig) {
            lSql = "UPDATE " + getSchema() + getTableName()+" SET "
                    + "startAnzeigeGeloescht=1 " + "WHERE " + "ident=? AND identVersion=0 AND dbServerIdent=? ";

        }
        else {
            lSql = "UPDATE " + getSchema() + getTableName()+" SET "
                    + "startAnzeigeGeloescht=1 " + "WHERE "
                    + "mandant=? AND hvJahr=? AND hvNummer=? AND datenbereich=? AND "
                    + "ident=? AND identVersion=0 AND dbServerIdent=? ";

        }
        
        return setzeStatus(lSql, pIdent, pDbServerIdent);
     }

    private int setzeStatus(String pSql, int pIdent, int pDbServerIdent ) {
        int anzahl=0;

        try {

            PreparedStatement lPStm=null;
            if (mandantenabhaengig) {
                lPStm = verbindung.prepareStatement(pSql);
                lPStm.setInt(1, pIdent);
                lPStm.setInt(2, pDbServerIdent);
            }
            else {
                 lPStm = verbindung.prepareStatement(pSql);
                lPStm.setInt(1, dbBundle.clGlobalVar.mandant);
                lPStm.setInt(2, dbBundle.clGlobalVar.hvJahr);
                lPStm.setString(3, dbBundle.clGlobalVar.hvNummer);
                lPStm.setString(4, dbBundle.clGlobalVar.datenbereich);
                lPStm.setInt(5, pIdent);
                lPStm.setInt(6, pDbServerIdent);
            }

            anzahl = updateIntern(lPStm);
        } catch (Exception e1) {
            CaBug.drucke("001");
            System.err.println(" " + e1.getMessage());
            return (-1);
        }

        return anzahl;
        
    }
    
}
