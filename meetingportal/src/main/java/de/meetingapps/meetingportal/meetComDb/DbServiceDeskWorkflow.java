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
import java.util.List;

import de.meetingapps.meetingportal.meetComAllg.CaBug;
import de.meetingapps.meetingportal.meetComEntities.EclServiceDeskWorkflow;

/**Hinweis: rueckfragen wird wie fragen behandelt*/

public class  DbServiceDeskWorkflow extends DbRoot<EclServiceDeskWorkflow> {

//    private int logDrucken=10;
    
    public DbServiceDeskWorkflow(DbBundle pDbBundle) {
        super(pDbBundle);
    }

    
    @Override
    String getCreateString() {

//      @formatter:off
       String createString = "CREATE TABLE " + getSchema() + getTableName()+" ( "
               + "`ident` int(11) NOT NULL DEFAULT 0, "
               + "`setNr` int(11) NOT NULL DEFAULT 1, "
 
              + "`istAktiv` int(11) NOT NULL DEFAULT 1, "

              + "`istGast` int(11) NOT NULL DEFAULT 0, "
              + "`istAktionaer` int(11) NOT NULL DEFAULT 0, "
              + "`istSammelkarte` int(11) NOT NULL DEFAULT 0, "
              + "`istAngemeldeterAktionaer` int(11) NOT NULL DEFAULT 0, "
              + "`istNullbestand` int(11) NOT NULL DEFAULT 0, "

              + "`istGeradePraesent` int(11) NOT NULL DEFAULT 0, "
              + "`istGeradeSelbstPraesent` int(11) NOT NULL DEFAULT 0, "
              + "`istGeradeVertretenPraesent` int(11) NOT NULL DEFAULT 0, "
              + "`warPraesent` int(11) NOT NULL DEFAULT 0, "

              + "`istGeradeVirtuellPraesent` int(11) NOT NULL DEFAULT 0, "
              + "`istGeradeVirtuellSelbstPraesent` int(11) NOT NULL DEFAULT 0, "
              + "`istGeradeVirtuellVertretenPraesent` int(11) NOT NULL DEFAULT 0, "
              + "`warVirtuellPraesent` int(11) NOT NULL DEFAULT 0, "

              + "`istAktionaerInSammelkarte` int(11) NOT NULL DEFAULT 0, "
              + "`istInBriefwahlsammelkarte` int(11) NOT NULL DEFAULT 0, "
              + "`istInSRVsammelkarte` int(11) NOT NULL DEFAULT 0, "
              + "`istInKIAVsammelkarte` int(11) NOT NULL DEFAULT 0, "
              + "`istInDauervollmachtsammelkarte` int(11) NOT NULL DEFAULT 0, "
              + "`istInOrgasammelkarte` int(11) NOT NULL DEFAULT 0, "

              + "`hatEKzugeordnet` int(11) NOT NULL DEFAULT 0, "
              + "`hatSKzugeordnet` int(11) NOT NULL DEFAULT 0, "

              + "`hatVollmachtErteilt` int(11) NOT NULL DEFAULT 0, "

              + "`esGibtStimmkartenZumZuordnen` int(11) NOT NULL DEFAULT 0, "
              + "`esGibtStimmkartenZumDrucken` int(11) NOT NULL DEFAULT 0, "

              + "`wertAntwort0` int(11) NOT NULL DEFAULT 0, "
              + "`wertAntwort1` int(11) NOT NULL DEFAULT 0, "
              + "`wertAntwort2` int(11) NOT NULL DEFAULT 0, "
              + "`wertAntwort3` int(11) NOT NULL DEFAULT 0, "
              + "`wertAntwort4` int(11) NOT NULL DEFAULT 0, "
              + "`wertAntwort5` int(11) NOT NULL DEFAULT 0, "
              + "`wertAntwort6` int(11) NOT NULL DEFAULT 0, "
              + "`wertAntwort7` int(11) NOT NULL DEFAULT 0, "
              + "`wertAntwort8` int(11) NOT NULL DEFAULT 0, "
              + "`wertAntwort9` int(11) NOT NULL DEFAULT 0, "

              + "`frageInAntwortSchreiben` int(11) NOT NULL DEFAULT -1, "
              + "`frageEinleitungsText` varchar(200) NOT NULL DEFAULT '', " 
              + "`frage0` varchar(200) NOT NULL DEFAULT '', " 
              + "`frage1` varchar(200) NOT NULL DEFAULT '', " 
              + "`frage2` varchar(200) NOT NULL DEFAULT '', " 
              + "`frage3` varchar(200) NOT NULL DEFAULT '', " 
              + "`frage4` varchar(200) NOT NULL DEFAULT '', " 
 
              + "`ausfuehrenAbfrage` int(11) NOT NULL DEFAULT -1, "
              + "`ausfuehrenAbfrageText0` varchar(200) NOT NULL DEFAULT '', " 
              + "`ausfuehrenAbfrageText1` varchar(200) NOT NULL DEFAULT '', " 
              + "`ausfuehrenAbfrageText2` varchar(200) NOT NULL DEFAULT '', " 
              + "`ausfuehrenAbfrageText3` varchar(200) NOT NULL DEFAULT '', " 
              + "`ausfuehrenAbfrageText4` varchar(200) NOT NULL DEFAULT '', " 
              + "`ausfuehrenAbfrageText5` varchar(200) NOT NULL DEFAULT '', " 
              + "`ausfuehrenAbfrageText6` varchar(200) NOT NULL DEFAULT '', " 
              + "`ausfuehrenAbfrageText7` varchar(200) NOT NULL DEFAULT '', " 
              + "`ausfuehrenAbfrageText8` varchar(200) NOT NULL DEFAULT '', " 
              + "`ausfuehrenAbfrageText9` varchar(200) NOT NULL DEFAULT '', " 
              + "`ausfuehrenAbfrageHinweis0` varchar(200) NOT NULL DEFAULT '', " 
              + "`ausfuehrenAbfrageHinweis1` varchar(200) NOT NULL DEFAULT '', " 
              + "`ausfuehrenAbfrageHinweis2` varchar(200) NOT NULL DEFAULT '', " 
              + "`ausfuehrenAbfrageHinweis3` varchar(200) NOT NULL DEFAULT '', " 
              + "`ausfuehrenAbfrageHinweis4` varchar(200) NOT NULL DEFAULT '', " 
              + "`ausfuehrenAbfrageHinweis5` varchar(200) NOT NULL DEFAULT '', " 
              + "`ausfuehrenAbfrageHinweis6` varchar(200) NOT NULL DEFAULT '', " 
              + "`ausfuehrenAbfrageHinweis7` varchar(200) NOT NULL DEFAULT '', " 
              + "`ausfuehrenAbfrageHinweis8` varchar(200) NOT NULL DEFAULT '', " 
              + "`ausfuehrenAbfrageHinweis9` varchar(200) NOT NULL DEFAULT '', " 
              + "`ausfuehrenAbfrageFunktion0` int(11) NOT NULL DEFAULT 0, "
              + "`ausfuehrenAbfrageFunktion1` int(11) NOT NULL DEFAULT 0, "
              + "`ausfuehrenAbfrageFunktion2` int(11) NOT NULL DEFAULT 0, "
              + "`ausfuehrenAbfrageFunktion3` int(11) NOT NULL DEFAULT 0, "
              + "`ausfuehrenAbfrageFunktion4` int(11) NOT NULL DEFAULT 0, "
              + "`ausfuehrenAbfrageFunktion5` int(11) NOT NULL DEFAULT 0, "
              + "`ausfuehrenAbfrageFunktion6` int(11) NOT NULL DEFAULT 0, "
              + "`ausfuehrenAbfrageFunktion7` int(11) NOT NULL DEFAULT 0, "
              + "`ausfuehrenAbfrageFunktion8` int(11) NOT NULL DEFAULT 0, "
              + "`ausfuehrenAbfrageFunktion9` int(11) NOT NULL DEFAULT 0, "
              + "`ausfuehrenAbfrageErforderlichesRecht0` int(11) NOT NULL DEFAULT 1, "
              + "`ausfuehrenAbfrageErforderlichesRecht1` int(11) NOT NULL DEFAULT 1, "
              + "`ausfuehrenAbfrageErforderlichesRecht2` int(11) NOT NULL DEFAULT 1, "
              + "`ausfuehrenAbfrageErforderlichesRecht3` int(11) NOT NULL DEFAULT 1, "
              + "`ausfuehrenAbfrageErforderlichesRecht4` int(11) NOT NULL DEFAULT 1, "
              + "`ausfuehrenAbfrageErforderlichesRecht5` int(11) NOT NULL DEFAULT 1, "
              + "`ausfuehrenAbfrageErforderlichesRecht6` int(11) NOT NULL DEFAULT 1, "
              + "`ausfuehrenAbfrageErforderlichesRecht7` int(11) NOT NULL DEFAULT 1, "
              + "`ausfuehrenAbfrageErforderlichesRecht8` int(11) NOT NULL DEFAULT 1, "
              + "`ausfuehrenAbfrageErforderlichesRecht9` int(11) NOT NULL DEFAULT 1, "

              + "`ausfuehrenButtonText0` varchar(200) NOT NULL DEFAULT '', " 
              + "`ausfuehrenButtonText1` varchar(200) NOT NULL DEFAULT '', " 
              + "`ausfuehrenButtonText2` varchar(200) NOT NULL DEFAULT '', " 
              + "`ausfuehrenButtonText3` varchar(200) NOT NULL DEFAULT '', " 
              + "`ausfuehrenButtonText4` varchar(200) NOT NULL DEFAULT '', " 
              + "`ausfuehrenButtonText5` varchar(200) NOT NULL DEFAULT '', " 
              + "`ausfuehrenButtonText6` varchar(200) NOT NULL DEFAULT '', " 
              + "`ausfuehrenButtonText7` varchar(200) NOT NULL DEFAULT '', " 
              + "`ausfuehrenButtonText8` varchar(200) NOT NULL DEFAULT '', " 
              + "`ausfuehrenButtonText9` varchar(200) NOT NULL DEFAULT '', " 
              + "`ausfuehrenButtonHinweis0` varchar(200) NOT NULL DEFAULT '', " 
              + "`ausfuehrenButtonHinweis1` varchar(200) NOT NULL DEFAULT '', " 
              + "`ausfuehrenButtonHinweis2` varchar(200) NOT NULL DEFAULT '', " 
              + "`ausfuehrenButtonHinweis3` varchar(200) NOT NULL DEFAULT '', " 
              + "`ausfuehrenButtonHinweis4` varchar(200) NOT NULL DEFAULT '', " 
              + "`ausfuehrenButtonHinweis5` varchar(200) NOT NULL DEFAULT '', " 
              + "`ausfuehrenButtonHinweis6` varchar(200) NOT NULL DEFAULT '', " 
              + "`ausfuehrenButtonHinweis7` varchar(200) NOT NULL DEFAULT '', " 
              + "`ausfuehrenButtonHinweis8` varchar(200) NOT NULL DEFAULT '', " 
              + "`ausfuehrenButtonHinweis9` varchar(200) NOT NULL DEFAULT '', " 
              + "`ausfuehrenButtonFunktion0` int(11) NOT NULL DEFAULT 0, "
              + "`ausfuehrenButtonFunktion1` int(11) NOT NULL DEFAULT 0, "
              + "`ausfuehrenButtonFunktion2` int(11) NOT NULL DEFAULT 0, "
              + "`ausfuehrenButtonFunktion3` int(11) NOT NULL DEFAULT 0, "
              + "`ausfuehrenButtonFunktion4` int(11) NOT NULL DEFAULT 0, "
              + "`ausfuehrenButtonFunktion5` int(11) NOT NULL DEFAULT 0, "
              + "`ausfuehrenButtonFunktion6` int(11) NOT NULL DEFAULT 0, "
              + "`ausfuehrenButtonFunktion7` int(11) NOT NULL DEFAULT 0, "
              + "`ausfuehrenButtonFunktion8` int(11) NOT NULL DEFAULT 0, "
              + "`ausfuehrenButtonFunktion9` int(11) NOT NULL DEFAULT 0, "
              + "`ausfuehrenButtonErforderlichesRecht0` int(11) NOT NULL DEFAULT 1, "
              + "`ausfuehrenButtonErforderlichesRecht1` int(11) NOT NULL DEFAULT 1, "
              + "`ausfuehrenButtonErforderlichesRecht2` int(11) NOT NULL DEFAULT 1, "
              + "`ausfuehrenButtonErforderlichesRecht3` int(11) NOT NULL DEFAULT 1, "
              + "`ausfuehrenButtonErforderlichesRecht4` int(11) NOT NULL DEFAULT 1, "
              + "`ausfuehrenButtonErforderlichesRecht5` int(11) NOT NULL DEFAULT 1, "
              + "`ausfuehrenButtonErforderlichesRecht6` int(11) NOT NULL DEFAULT 1, "
              + "`ausfuehrenButtonErforderlichesRecht7` int(11) NOT NULL DEFAULT 1, "
              + "`ausfuehrenButtonErforderlichesRecht8` int(11) NOT NULL DEFAULT 1, "
              + "`ausfuehrenButtonErforderlichesRecht9` int(11) NOT NULL DEFAULT 1, "

              + "`anzeigeStatus` varchar(1000) NOT NULL DEFAULT '', " 
               
              + "PRIMARY KEY (`ident`, `setNr`) " + ") ";
//      @formatter:on
        return createString;
    }

    @Override
    String getSchema() {
        return dbBundle.getSchemaAllgemein();
    }


    @Override
    void resetInterneIdent(int pHoechsteIdent) {
    }


    @Override
    String getTableName() {
         return "tbl_serviceDeskWorkflow";
    }


    @Override
    String getFeldFuerInterneIdent() {
        return "ident";
    }

    @Override
    int getAnzFelder() {
        return 126;
    }


    @Override
    EclServiceDeskWorkflow decodeErgebnis(ResultSet pErgebnis) {
        EclServiceDeskWorkflow lEclReturn = new EclServiceDeskWorkflow();

        try {
            lEclReturn.ident = pErgebnis.getInt("ident");
            lEclReturn.setNr = pErgebnis.getInt("setNr");

            lEclReturn.istAktiv = pErgebnis.getInt("istAktiv");

            lEclReturn.istGast = pErgebnis.getInt("istGast");
            lEclReturn.istAktionaer = pErgebnis.getInt("istAktionaer");
            lEclReturn.istSammelkarte = pErgebnis.getInt("istSammelkarte");
            lEclReturn.istAngemeldeterAktionaer = pErgebnis.getInt("istAngemeldeterAktionaer");
            lEclReturn.istNullbestand = pErgebnis.getInt("istNullbestand");

            lEclReturn.istGeradePraesent = pErgebnis.getInt("istGeradePraesent");
            lEclReturn.istGeradeSelbstPraesent = pErgebnis.getInt("istGeradeSelbstPraesent");
            lEclReturn.istGeradeVertretenPraesent = pErgebnis.getInt("istGeradeVertretenPraesent");
            lEclReturn.warPraesent = pErgebnis.getInt("warPraesent");

            lEclReturn.istGeradeVirtuellPraesent = pErgebnis.getInt("istGeradeVirtuellPraesent");
            lEclReturn.istGeradeVirtuellSelbstPraesent = pErgebnis.getInt("istGeradeVirtuellSelbstPraesent");
            lEclReturn.istGeradeVirtuellVertretenPraesent = pErgebnis.getInt("istGeradeVirtuellVertretenPraesent");
            lEclReturn.warVirtuellPraesent = pErgebnis.getInt("warVirtuellPraesent");

            lEclReturn.istAktionaerInSammelkarte = pErgebnis.getInt("istAktionaerInSammelkarte");
            lEclReturn.istInBriefwahlsammelkarte = pErgebnis.getInt("istInBriefwahlsammelkarte");
            lEclReturn.istInSRVsammelkarte = pErgebnis.getInt("istInSRVsammelkarte");
            lEclReturn.istInKIAVsammelkarte = pErgebnis.getInt("istInKIAVsammelkarte");
            lEclReturn.istInDauervollmachtsammelkarte = pErgebnis.getInt("istInDauervollmachtsammelkarte");
            lEclReturn.istInOrgasammelkarte = pErgebnis.getInt("istInOrgasammelkarte");

            lEclReturn.hatEKzugeordnet = pErgebnis.getInt("hatEKzugeordnet");
            lEclReturn.hatSKzugeordnet = pErgebnis.getInt("hatSKzugeordnet");

            lEclReturn.hatVollmachtErteilt = pErgebnis.getInt("hatVollmachtErteilt");

            lEclReturn.esGibtStimmkartenZumZuordnen = pErgebnis.getInt("esGibtStimmkartenZumZuordnen");
            lEclReturn.esGibtStimmkartenZumDrucken = pErgebnis.getInt("esGibtStimmkartenZumDrucken");

            for (int i=0;i<10;i++) {
                lEclReturn.wertAntwort[i] = pErgebnis.getInt("wertAntwort"+Integer.toString(i));
            }
            
            lEclReturn.frageInAntwortSchreiben = pErgebnis.getInt("frageInAntwortSchreiben");
            lEclReturn.frageEinleitungsText = pErgebnis.getString("frageEinleitungsText");
            for (int i=0;i<5;i++) {
                lEclReturn.frage[i] = pErgebnis.getString("frage"+Integer.toString(i));
            }
           
            lEclReturn.ausfuehrenAbfrage = pErgebnis.getInt("ausfuehrenAbfrage");
            for (int i=0;i<10;i++) {
                lEclReturn.ausfuehrenAbfrageText[i] = pErgebnis.getString("ausfuehrenAbfrageText"+Integer.toString(i));
            }
            for (int i=0;i<10;i++) {
                lEclReturn.ausfuehrenAbfrageHinweis[i] = pErgebnis.getString("ausfuehrenAbfrageHinweis"+Integer.toString(i));
            }
            for (int i=0;i<10;i++) {
                lEclReturn.ausfuehrenAbfrageFunktion[i] = pErgebnis.getInt("ausfuehrenAbfrageFunktion"+Integer.toString(i));
            }
            for (int i=0;i<10;i++) {
                lEclReturn.ausfuehrenAbfrageErforderlichesRecht[i] = pErgebnis.getInt("ausfuehrenAbfrageErforderlichesRecht"+Integer.toString(i));
            }
           
            for (int i=0;i<10;i++) {
                lEclReturn.ausfuehrenButtonText[i] = pErgebnis.getString("ausfuehrenButtonText"+Integer.toString(i));
            }
            for (int i=0;i<10;i++) {
                lEclReturn.ausfuehrenButtonHinweis[i] = pErgebnis.getString("ausfuehrenButtonHinweis"+Integer.toString(i));
            }
            for (int i=0;i<10;i++) {
                lEclReturn.ausfuehrenButtonFunktion[i] = pErgebnis.getInt("ausfuehrenButtonFunktion"+Integer.toString(i));
            }
            for (int i=0;i<10;i++) {
                lEclReturn.ausfuehrenButtonErforderlichesRecht[i] = pErgebnis.getInt("ausfuehrenButtonErforderlichesRecht"+Integer.toString(i));
            }
           
            lEclReturn.anzeigeStatus = pErgebnis.getString("anzeigeStatus");
                     
       } catch (Exception e) {
            CaBug.drucke("001");
            System.err.println(" " + e.getMessage());
        }

        return lEclReturn;
    }



    @Override
    void fuellePreparedStatementKomplett(PreparedStatement pPStm, int pOffset, EclServiceDeskWorkflow pEcl) {
        int startOffset = pOffset; /*Nur erforderlich zum Überprüfen von Programmierfehlern - Feldanzahl*/

        try {
//          @formatter:off
            
            pPStm.setInt(pOffset, pEcl.ident);pOffset++;
            pPStm.setInt(pOffset, pEcl.setNr);pOffset++;

            pPStm.setInt(pOffset, pEcl.istAktiv);pOffset++;

            pPStm.setInt(pOffset, pEcl.istGast);pOffset++;
            pPStm.setInt(pOffset, pEcl.istAktionaer);pOffset++;
            pPStm.setInt(pOffset, pEcl.istSammelkarte);pOffset++;
            pPStm.setInt(pOffset, pEcl.istAngemeldeterAktionaer);pOffset++;
            pPStm.setInt(pOffset, pEcl.istNullbestand);pOffset++;

            pPStm.setInt(pOffset, pEcl.istGeradePraesent);pOffset++;
            pPStm.setInt(pOffset, pEcl.istGeradeSelbstPraesent);pOffset++;
            pPStm.setInt(pOffset, pEcl.istGeradeVertretenPraesent);pOffset++;
            pPStm.setInt(pOffset, pEcl.warPraesent);pOffset++;

            pPStm.setInt(pOffset, pEcl.istGeradeVirtuellPraesent);pOffset++;
            pPStm.setInt(pOffset, pEcl.istGeradeVirtuellSelbstPraesent);pOffset++;
            pPStm.setInt(pOffset, pEcl.istGeradeVirtuellVertretenPraesent);pOffset++;
            pPStm.setInt(pOffset, pEcl.warVirtuellPraesent);pOffset++;

            pPStm.setInt(pOffset, pEcl.istAktionaerInSammelkarte);pOffset++;
            pPStm.setInt(pOffset, pEcl.istInBriefwahlsammelkarte);pOffset++;
            pPStm.setInt(pOffset, pEcl.istInSRVsammelkarte);pOffset++;
            pPStm.setInt(pOffset, pEcl.istInKIAVsammelkarte);pOffset++;
            pPStm.setInt(pOffset, pEcl.istInDauervollmachtsammelkarte);pOffset++;
            pPStm.setInt(pOffset, pEcl.istInOrgasammelkarte);pOffset++;

            pPStm.setInt(pOffset, pEcl.hatEKzugeordnet);pOffset++;
            pPStm.setInt(pOffset, pEcl.hatSKzugeordnet);pOffset++;

            pPStm.setInt(pOffset, pEcl.hatVollmachtErteilt);pOffset++;

            pPStm.setInt(pOffset, pEcl.esGibtStimmkartenZumZuordnen);pOffset++;
            pPStm.setInt(pOffset, pEcl.esGibtStimmkartenZumDrucken);pOffset++;

            for (int i=0;i<10;i++) {
                pPStm.setInt(pOffset, pEcl.wertAntwort[i]);pOffset++;
            }

            pPStm.setInt(pOffset, pEcl.frageInAntwortSchreiben);pOffset++;
            pPStm.setString(pOffset, pEcl.frageEinleitungsText);pOffset++;

            for (int i=0;i<5;i++) {
                pPStm.setString(pOffset, pEcl.frage[i]);pOffset++;
            }

            pPStm.setInt(pOffset, pEcl.ausfuehrenAbfrage);pOffset++;
            for (int i=0;i<10;i++) {
                pPStm.setString(pOffset, pEcl.ausfuehrenAbfrageText[i]);pOffset++;
            }
            for (int i=0;i<10;i++) {
                pPStm.setString(pOffset, pEcl.ausfuehrenAbfrageHinweis[i]);pOffset++;
            }
            for (int i=0;i<10;i++) {
                pPStm.setInt(pOffset, pEcl.ausfuehrenAbfrageFunktion[i]);pOffset++;
            }
            for (int i=0;i<10;i++) {
                pPStm.setInt(pOffset, pEcl.ausfuehrenAbfrageErforderlichesRecht[i]);pOffset++;
            }

            for (int i=0;i<10;i++) {
                pPStm.setString(pOffset, pEcl.ausfuehrenButtonText[i]);pOffset++;
            }
            for (int i=0;i<10;i++) {
                pPStm.setString(pOffset, pEcl.ausfuehrenButtonHinweis[i]);pOffset++;
            }
            for (int i=0;i<10;i++) {
                pPStm.setInt(pOffset, pEcl.ausfuehrenButtonFunktion[i]);pOffset++;
            }
            for (int i=0;i<10;i++) {
                pPStm.setInt(pOffset, pEcl.ausfuehrenButtonErforderlichesRecht[i]);pOffset++;
            }

            pPStm.setString(pOffset, pEcl.anzeigeStatus);pOffset++;

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
            "ident",
            "setNr",

            "istAktiv",

            "istGast",
            "istAktionaer",
            "istSammelkarte",
            "istAngemeldeterAktionaer",
            "istNullbestand",
            
            "istGeradePraesent",
            "istGeradeSelbstPraesent",
            "istGeradeVertretenPraesent",
            "warPraesent",
            
            "istGeradeVirtuellPraesent",
            "istGeradeVirtuellSelbstPraesent",
            "istGeradeVirtuellVertretenPraesent",
            "warVirtuellPraesent",
            
            "istAktionaerInSammelkarte",
            "istInBriefwahlsammelkarte",
            "istInSRVsammelkarte",
            "istInKIAVsammelkarte",
            "istInDauervollmachtsammelkarte",
            "istInOrgasammelkarte",
            
            "hatEKzugeordnet",
            "hatSKzugeordnet",
            
            "hatVollmachtErteilt",
            
            "esGibtStimmkartenZumZuordnen",
            "esGibtStimmkartenZumDrucken",
            
            "wertAntwort0",
            "wertAntwort1",
            "wertAntwort2",
            "wertAntwort3",
            "wertAntwort4",
            "wertAntwort5",
            "wertAntwort6",
            "wertAntwort7",
            "wertAntwort8",
            "wertAntwort9",
            
            "frageInAntwortSchreiben",
            "frageEinleitungsText",
            "frage0",
            "frage1",
            "frage2",
            "frage3",
            "frage4",
            
            "ausfuehrenAbfrage",
            "ausfuehrenAbfrageText0",
            "ausfuehrenAbfrageText1",
            "ausfuehrenAbfrageText2",
            "ausfuehrenAbfrageText3",
            "ausfuehrenAbfrageText4",
            "ausfuehrenAbfrageText5",
            "ausfuehrenAbfrageText6",
            "ausfuehrenAbfrageText7",
            "ausfuehrenAbfrageText8",
            "ausfuehrenAbfrageText9",
            "ausfuehrenAbfrageHinweis0",
            "ausfuehrenAbfrageHinweis1",
            "ausfuehrenAbfrageHinweis2",
            "ausfuehrenAbfrageHinweis3",
            "ausfuehrenAbfrageHinweis4",
            "ausfuehrenAbfrageHinweis5",
            "ausfuehrenAbfrageHinweis6",
            "ausfuehrenAbfrageHinweis7",
            "ausfuehrenAbfrageHinweis8",
            "ausfuehrenAbfrageHinweis9",
            "ausfuehrenAbfrageFunktion0",
            "ausfuehrenAbfrageFunktion1",
            "ausfuehrenAbfrageFunktion2",
            "ausfuehrenAbfrageFunktion3",
            "ausfuehrenAbfrageFunktion4",
            "ausfuehrenAbfrageFunktion5",
            "ausfuehrenAbfrageFunktion6",
            "ausfuehrenAbfrageFunktion7",
            "ausfuehrenAbfrageFunktion8",
            "ausfuehrenAbfrageFunktion9",
            "ausfuehrenAbfrageErforderlichesRecht0",
            "ausfuehrenAbfrageErforderlichesRecht1",
            "ausfuehrenAbfrageErforderlichesRecht2",
            "ausfuehrenAbfrageErforderlichesRecht3",
            "ausfuehrenAbfrageErforderlichesRecht4",
            "ausfuehrenAbfrageErforderlichesRecht5",
            "ausfuehrenAbfrageErforderlichesRecht6",
            "ausfuehrenAbfrageErforderlichesRecht7",
            "ausfuehrenAbfrageErforderlichesRecht8",
            "ausfuehrenAbfrageErforderlichesRecht9",
            
            "ausfuehrenButtonText0",
            "ausfuehrenButtonText1",
            "ausfuehrenButtonText2",
            "ausfuehrenButtonText3",
            "ausfuehrenButtonText4",
            "ausfuehrenButtonText5",
            "ausfuehrenButtonText6",
            "ausfuehrenButtonText7",
            "ausfuehrenButtonText8",
            "ausfuehrenButtonText9",
            "ausfuehrenButtonHinweis0",
            "ausfuehrenButtonHinweis1",
            "ausfuehrenButtonHinweis2",
            "ausfuehrenButtonHinweis3",
            "ausfuehrenButtonHinweis4",
            "ausfuehrenButtonHinweis5",
            "ausfuehrenButtonHinweis6",
            "ausfuehrenButtonHinweis7",
            "ausfuehrenButtonHinweis8",
            "ausfuehrenButtonHinweis9",
            "ausfuehrenButtonFunktion0",
            "ausfuehrenButtonFunktion1",
            "ausfuehrenButtonFunktion2",
            "ausfuehrenButtonFunktion3",
            "ausfuehrenButtonFunktion4",
            "ausfuehrenButtonFunktion5",
            "ausfuehrenButtonFunktion6",
            "ausfuehrenButtonFunktion7",
            "ausfuehrenButtonFunktion8",
            "ausfuehrenButtonFunktion9",
            "ausfuehrenButtonErforderlichesRecht0",
            "ausfuehrenButtonErforderlichesRecht1",
            "ausfuehrenButtonErforderlichesRecht2",
            "ausfuehrenButtonErforderlichesRecht3",
            "ausfuehrenButtonErforderlichesRecht4",
            "ausfuehrenButtonErforderlichesRecht5",
            "ausfuehrenButtonErforderlichesRecht6",
            "ausfuehrenButtonErforderlichesRecht7",
            "ausfuehrenButtonErforderlichesRecht8",
            "ausfuehrenButtonErforderlichesRecht9",
            
            "anzeigeStatus"
     };
    
    private boolean initErfolgt=false;
    private void initFelder(){
        if (initErfolgt) {return;}
        initErfolgt=true;
    }

    @Override
    public int insert(EclServiceDeskWorkflow pEcl) {
        initFelder();
        
        readAllSet(pEcl.setNr);
        int maxIdent=0;
        for (int i=0;i<ergebnisArray.size();i++) {
            if (ergebnisArray.get(i).ident>maxIdent) {
                maxIdent=ergebnisArray.get(i).ident;
            }
        }
        
        if (pEcl.ident==0) {
            maxIdent++;
            pEcl.ident=maxIdent;
        }
         
        return insertIntern(felder, pEcl);
    }

    public int insert(List<EclServiceDeskWorkflow> pEclList, int pSetNr) {
        initFelder();
        
        deleteSet(pSetNr);
        int maxIdent=0;
        for (int i=0;i<pEclList.size();i++) {
            EclServiceDeskWorkflow pEcl=pEclList.get(i);
            maxIdent++;
            pEcl.ident=maxIdent;
            int rc=insertIntern(felder, pEcl);
            if (rc<1) {
                CaBug.drucke("001");
                return rc;
            }
        }
        return pEclList.size();
    }


    /**Liest alle Demo-Kennungen ein*/
     public int readAll() {
        PreparedStatement lPStm = null;
        int anzInArray = 0;
        try {
            String lSql = "SELECT * from " + getSchema() + getTableName()+" ";
            lSql = lSql + " ORDER BY ident;";
            lPStm = verbindung.prepareStatement(lSql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            anzInArray = readIntern(lPStm);

        } catch (Exception e) {
            CaBug.drucke("003");
            System.err.println(" " + e.getMessage());
            return (-1);
        }
        return (anzInArray);
    }

     /**Liest alle Demo-Kennungen ein*/
     public int readAllSet(int pSetNr) {
        PreparedStatement lPStm = null;
        int anzInArray = 0;
        try {
            String lSql = "SELECT * from " + getSchema() + getTableName()+" ";
            lSql = lSql + " WHERE setNr=? ORDER BY ident;";
            lPStm = verbindung.prepareStatement(lSql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            lPStm.setInt(1, pSetNr);
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
    public int update(EclServiceDeskWorkflow pEcl) {
        int ergebnis=0;
        initFelder();

        try {

            String lSql = setzeUpdateBasisStringZusammen(felder)
                    + " WHERE "
                    + "ident=? ";

            PreparedStatement lPStm = verbindung.prepareStatement(lSql);
            fuellePreparedStatementKomplett(lPStm, 1, pEcl);
            lPStm.setInt(getAnzFelder() + 1, pEcl.ident);

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
                    " WHERE ident=? ";
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

    public int deleteSet(int pSetNr) {
        int ergebnis=0;
        try {
            String sql = "DELETE FROM " + getSchema()+getTableName()+
                    " WHERE setNr=? ";
            PreparedStatement pstm1 = verbindung.prepareStatement(sql);
            pstm1.setInt(1, pSetNr);
            ergebnis = deleteIntern(pstm1);
        } catch (Exception e1) {
            CaBug.drucke("001");
            System.err.println(" " + e1.getMessage());
            return (-1);
        }

        return ergebnis;
    }


}
