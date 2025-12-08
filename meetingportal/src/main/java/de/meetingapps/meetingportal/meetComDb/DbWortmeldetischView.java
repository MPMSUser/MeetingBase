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
import de.meetingapps.meetingportal.meetComEntities.EclWortmeldetischView;

/**Hinweis: rueckfragen wird wie fragen behandelt*/

public class  DbWortmeldetischView extends DbRoot<EclWortmeldetischView> {

//    private int logDrucken=10;
    
    public DbWortmeldetischView(DbBundle pDbBundle) {
        super(pDbBundle);
    }

    
    @Override
    String getCreateString() {

        //      @formatter:off
        String createString = "CREATE TABLE " + getSchema() + getTableName()+" ( "
                + "`viewIdent` int NOT NULL DEFAULT 0, " 
                + "`setNr` int NOT NULL DEFAULT 0, " 
                + "`viewBezeichnung` varchar(60) NOT NULL DEFAULT '', " 
                + "`textInMenue` varchar(40) NOT NULL DEFAULT '', " 
                + "`textUeberschrift` varchar(60) NOT NULL DEFAULT '', " 
                + "`summeAnzeigen0` int NOT NULL DEFAULT 0, " 
                + "`summeAnzeigen1` int NOT NULL DEFAULT 0, " 
                + "`summeAnzeigen2` int NOT NULL DEFAULT 0, " 
                + "`summeAnzeigen3` int NOT NULL DEFAULT 0, " 
                + "`summeAnzeigen4` int NOT NULL DEFAULT 0, " 
                + "`summeAnzeigen5` int NOT NULL DEFAULT 0, " 
                + "`summeAnzeigen6` int NOT NULL DEFAULT 0, " 
                + "`summeAnzeigen7` int NOT NULL DEFAULT 0, " 
                + "`summeAnzeigen8` int NOT NULL DEFAULT 0, " 
                + "`summeAnzeigen9` int NOT NULL DEFAULT 0, " 
                + "`summeTextVorZahl0` varchar(100) NOT NULL DEFAULT '', " 
                + "`summeTextVorZahl1` varchar(100) NOT NULL DEFAULT '', " 
                + "`summeTextVorZahl2` varchar(100) NOT NULL DEFAULT '', " 
                + "`summeTextVorZahl3` varchar(100) NOT NULL DEFAULT '', " 
                + "`summeTextVorZahl4` varchar(100) NOT NULL DEFAULT '', " 
                + "`summeTextVorZahl5` varchar(100) NOT NULL DEFAULT '', " 
                + "`summeTextVorZahl6` varchar(100) NOT NULL DEFAULT '', " 
                + "`summeTextVorZahl7` varchar(100) NOT NULL DEFAULT '', " 
                + "`summeTextVorZahl8` varchar(100) NOT NULL DEFAULT '', " 
                + "`summeTextVorZahl9` varchar(100) NOT NULL DEFAULT '', " 
                + "`summeTextNachZahl0` varchar(100) NOT NULL DEFAULT '', " 
                + "`summeTextNachZahl1` varchar(100) NOT NULL DEFAULT '', " 
                + "`summeTextNachZahl2` varchar(100) NOT NULL DEFAULT '', " 
                + "`summeTextNachZahl3` varchar(100) NOT NULL DEFAULT '', " 
                + "`summeTextNachZahl4` varchar(100) NOT NULL DEFAULT '', " 
                + "`summeTextNachZahl5` varchar(100) NOT NULL DEFAULT '', " 
                + "`summeTextNachZahl6` varchar(100) NOT NULL DEFAULT '', " 
                + "`summeTextNachZahl7` varchar(100) NOT NULL DEFAULT '', " 
                + "`summeTextNachZahl8` varchar(100) NOT NULL DEFAULT '', " 
                + "`summeTextNachZahl9` varchar(100) NOT NULL DEFAULT '', " 
                + "`aktualisierenButtonAnzeigen` int NOT NULL DEFAULT 0, " 
                + "`zurueckButtonAnzeigen` int NOT NULL DEFAULT 0, " 
                + "`sichtVersammlungsleiterAnzeigen` int NOT NULL DEFAULT 0, " 
                + "`subViewsIdent0` int NOT NULL DEFAULT 0, " 
                + "`subViewsIdent1` int NOT NULL DEFAULT 0, " 
                + "`subViewsIdent2` int NOT NULL DEFAULT 0, " 
                + "`subViewsIdent3` int NOT NULL DEFAULT 0, " 
                + "`subViewsIdent4` int NOT NULL DEFAULT 0, " 
                + "`subViewsIdent5` int NOT NULL DEFAULT 0, " 
                + "`subViewsIdent6` int NOT NULL DEFAULT 0, " 
                + "`subViewsIdent7` int NOT NULL DEFAULT 0, " 
                + "`subViewsIdent8` int NOT NULL DEFAULT 0, " 
                + "`subViewsIdent9` int NOT NULL DEFAULT 0, " 
                + "`teststationNrAnzeigen` int NOT NULL DEFAULT 0, " 
                + "`testStationBegriff` varchar(40) NOT NULL DEFAULT '', " 
                + "`redestationNrAnzeigen` int NOT NULL DEFAULT 0, " 
                + "`redeStationBegriff` varchar(40) NOT NULL DEFAULT '', " 
                + "`detailBearbeitungZulassen` int NOT NULL DEFAULT 0, " 
                + "`reihenfolgeBearbeitungZulassen` int NOT NULL DEFAULT 0, " 
                + "`rednerBearbeitungZulassen` int NOT NULL DEFAULT 0, " 
                + "`praesenzAnzeigeAusfuehren` int NOT NULL DEFAULT 0, " 
                + "`pushFuerDiesenView` int NOT NULL DEFAULT 0, " 
                + "`rederaumErgaenzen` int NOT NULL DEFAULT 0, " 
                + "`testraumErgaenzen` int NOT NULL DEFAULT 0, " 
                + "`uhrzeitErgaenzen` int NOT NULL DEFAULT 0, " 
                + "`kontaktDatenAnzeigen` int NOT NULL DEFAULT 0, " 
                + "`infoFelderMitteilungAnzeigen` int NOT NULL DEFAULT 0, " 
                + "`kommentarInternAnzeigen` int NOT NULL DEFAULT 0, " 
                + "`kommentarInternEingabeMoeglich` int NOT NULL DEFAULT 0, " 
                + "`kommentarVersammlungsleiterAnzeigen` int NOT NULL DEFAULT 0, " 
                + "`kommentarVersammlungsleiterEingabeMoeglich` int NOT NULL DEFAULT 0, " 
                + "`nachrichtAnVersammlungsleiterMoeglich` int NOT NULL DEFAULT 0, " 
               + "PRIMARY KEY (`viewIdent`, `setNr`) " + ") ";
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
         return "tbl_wortmeldetischView";
    }


    @Override
    String getFeldFuerInterneIdent() {
        return "viewIdent";
    }

    @Override
    int getAnzFelder() {
        return 67;
    }


    @Override
    EclWortmeldetischView decodeErgebnis(ResultSet pErgebnis) {
        EclWortmeldetischView lEclReturn = new EclWortmeldetischView();

        try {
            lEclReturn.viewIdent = pErgebnis.getInt("viewIdent");
            lEclReturn.setNr = pErgebnis.getInt("setNr");
            lEclReturn.viewBezeichnung = pErgebnis.getString("viewBezeichnung");
            lEclReturn.textInMenue = pErgebnis.getString("textInMenue");
            lEclReturn.textUeberschrift = pErgebnis.getString("textUeberschrift");
            
            lEclReturn.summeAnzeigen=new int[10];
            lEclReturn.summeAnzeigen[0] = pErgebnis.getInt("summeAnzeigen0");
            lEclReturn.summeAnzeigen[1] = pErgebnis.getInt("summeAnzeigen1");
            lEclReturn.summeAnzeigen[2] = pErgebnis.getInt("summeAnzeigen2");
            lEclReturn.summeAnzeigen[3] = pErgebnis.getInt("summeAnzeigen3");
            lEclReturn.summeAnzeigen[4] = pErgebnis.getInt("summeAnzeigen4");
            lEclReturn.summeAnzeigen[5] = pErgebnis.getInt("summeAnzeigen5");
            lEclReturn.summeAnzeigen[6] = pErgebnis.getInt("summeAnzeigen6");
            lEclReturn.summeAnzeigen[7] = pErgebnis.getInt("summeAnzeigen7");
            lEclReturn.summeAnzeigen[8] = pErgebnis.getInt("summeAnzeigen8");
            lEclReturn.summeAnzeigen[9] = pErgebnis.getInt("summeAnzeigen9");

            lEclReturn.summeTextVorZahl=new String[10];
            lEclReturn.summeTextVorZahl[0] = pErgebnis.getString("summeTextVorZahl0");
            lEclReturn.summeTextVorZahl[1] = pErgebnis.getString("summeTextVorZahl1");
            lEclReturn.summeTextVorZahl[2] = pErgebnis.getString("summeTextVorZahl2");
            lEclReturn.summeTextVorZahl[3] = pErgebnis.getString("summeTextVorZahl3");
            lEclReturn.summeTextVorZahl[4] = pErgebnis.getString("summeTextVorZahl4");
            lEclReturn.summeTextVorZahl[5] = pErgebnis.getString("summeTextVorZahl5");
            lEclReturn.summeTextVorZahl[6] = pErgebnis.getString("summeTextVorZahl6");
            lEclReturn.summeTextVorZahl[7] = pErgebnis.getString("summeTextVorZahl7");
            lEclReturn.summeTextVorZahl[8] = pErgebnis.getString("summeTextVorZahl8");
            lEclReturn.summeTextVorZahl[9] = pErgebnis.getString("summeTextVorZahl9");

            lEclReturn.summeTextNachZahl=new String[10];
            lEclReturn.summeTextNachZahl[0] = pErgebnis.getString("summeTextNachZahl0");
            lEclReturn.summeTextNachZahl[1] = pErgebnis.getString("summeTextNachZahl1");
            lEclReturn.summeTextNachZahl[2] = pErgebnis.getString("summeTextNachZahl2");
            lEclReturn.summeTextNachZahl[3] = pErgebnis.getString("summeTextNachZahl3");
            lEclReturn.summeTextNachZahl[4] = pErgebnis.getString("summeTextNachZahl4");
            lEclReturn.summeTextNachZahl[5] = pErgebnis.getString("summeTextNachZahl5");
            lEclReturn.summeTextNachZahl[6] = pErgebnis.getString("summeTextNachZahl6");
            lEclReturn.summeTextNachZahl[7] = pErgebnis.getString("summeTextNachZahl7");
            lEclReturn.summeTextNachZahl[8] = pErgebnis.getString("summeTextNachZahl8");
            lEclReturn.summeTextNachZahl[9] = pErgebnis.getString("summeTextNachZahl9");

            lEclReturn.aktualisierenButtonAnzeigen = pErgebnis.getInt("aktualisierenButtonAnzeigen");
            lEclReturn.zurueckButtonAnzeigen = pErgebnis.getInt("zurueckButtonAnzeigen");
            lEclReturn.sichtVersammlungsleiterAnzeigen = pErgebnis.getInt("sichtVersammlungsleiterAnzeigen");

            lEclReturn.subViewsIdent[0] = pErgebnis.getInt("subViewsIdent0");
            lEclReturn.subViewsIdent[1] = pErgebnis.getInt("subViewsIdent1");
            lEclReturn.subViewsIdent[2] = pErgebnis.getInt("subViewsIdent2");
            lEclReturn.subViewsIdent[3] = pErgebnis.getInt("subViewsIdent3");
            lEclReturn.subViewsIdent[4] = pErgebnis.getInt("subViewsIdent4");
            lEclReturn.subViewsIdent[5] = pErgebnis.getInt("subViewsIdent5");
            lEclReturn.subViewsIdent[6] = pErgebnis.getInt("subViewsIdent6");
            lEclReturn.subViewsIdent[7] = pErgebnis.getInt("subViewsIdent7");
            lEclReturn.subViewsIdent[8] = pErgebnis.getInt("subViewsIdent8");
            lEclReturn.subViewsIdent[9] = pErgebnis.getInt("subViewsIdent9");

            lEclReturn.teststationNrAnzeigen = pErgebnis.getInt("teststationNrAnzeigen");
            lEclReturn.testStationBegriff = pErgebnis.getString("testStationBegriff");
            lEclReturn.redestationNrAnzeigen = pErgebnis.getInt("redestationNrAnzeigen");
            lEclReturn.redeStationBegriff = pErgebnis.getString("redeStationBegriff");

            lEclReturn.detailBearbeitungZulassen = pErgebnis.getInt("detailBearbeitungZulassen");
            lEclReturn.reihenfolgeBearbeitungZulassen = pErgebnis.getInt("reihenfolgeBearbeitungZulassen");
            lEclReturn.rednerBearbeitungZulassen = pErgebnis.getInt("rednerBearbeitungZulassen");
            lEclReturn.praesenzAnzeigeAusfuehren = pErgebnis.getInt("praesenzAnzeigeAusfuehren");

            lEclReturn.pushFuerDiesenView = pErgebnis.getInt("pushFuerDiesenView");
            lEclReturn.rederaumErgaenzen = pErgebnis.getInt("rederaumErgaenzen");
            lEclReturn.testraumErgaenzen = pErgebnis.getInt("testraumErgaenzen");
            lEclReturn.uhrzeitErgaenzen = pErgebnis.getInt("uhrzeitErgaenzen");

            lEclReturn.kontaktDatenAnzeigen = pErgebnis.getInt("kontaktDatenAnzeigen");
            lEclReturn.infoFelderMitteilungAnzeigen = pErgebnis.getInt("infoFelderMitteilungAnzeigen");
            lEclReturn.kommentarInternAnzeigen = pErgebnis.getInt("kommentarInternAnzeigen");
            lEclReturn.kommentarInternEingabeMoeglich = pErgebnis.getInt("kommentarInternEingabeMoeglich");
            lEclReturn.kommentarVersammlungsleiterAnzeigen = pErgebnis.getInt("kommentarVersammlungsleiterAnzeigen");
            lEclReturn.kommentarVersammlungsleiterEingabeMoeglich = pErgebnis.getInt("kommentarVersammlungsleiterEingabeMoeglich");
            lEclReturn.nachrichtAnVersammlungsleiterMoeglich = pErgebnis.getInt("nachrichtAnVersammlungsleiterMoeglich");

       } catch (Exception e) {
            CaBug.drucke("001");
            System.err.println(" " + e.getMessage());
        }

        return lEclReturn;
    }



    @Override
    void fuellePreparedStatementKomplett(PreparedStatement pPStm, int pOffset, EclWortmeldetischView pEcl) {
        int startOffset = pOffset; /*Nur erforderlich zum Überprüfen von Programmierfehlern - Feldanzahl*/

        try {
//          @formatter:off
            
            pPStm.setInt(pOffset, pEcl.viewIdent);pOffset++;
            pPStm.setInt(pOffset, pEcl.setNr);pOffset++;
            pPStm.setString(pOffset, pEcl.viewBezeichnung);pOffset++;
            pPStm.setString(pOffset, pEcl.textInMenue);pOffset++;
            pPStm.setString(pOffset, pEcl.textUeberschrift);pOffset++;

            for (int i=0;i<10;i++) {
                pPStm.setInt(pOffset, pEcl.summeAnzeigen[i]);pOffset++;
            }

            for (int i=0;i<10;i++) {
                pPStm.setString(pOffset, pEcl.summeTextVorZahl[i]);pOffset++;
            }

            for (int i=0;i<10;i++) {
                pPStm.setString(pOffset, pEcl.summeTextNachZahl[i]);pOffset++;
            }

            pPStm.setInt(pOffset, pEcl.aktualisierenButtonAnzeigen);pOffset++;
            pPStm.setInt(pOffset, pEcl.zurueckButtonAnzeigen);pOffset++;
            pPStm.setInt(pOffset, pEcl.sichtVersammlungsleiterAnzeigen);pOffset++;

            for (int i=0;i<10;i++) {
                pPStm.setInt(pOffset, pEcl.subViewsIdent[i]);pOffset++;
            }

            pPStm.setInt(pOffset, pEcl.teststationNrAnzeigen);pOffset++;
            pPStm.setString(pOffset, pEcl.testStationBegriff);pOffset++;
            pPStm.setInt(pOffset, pEcl.redestationNrAnzeigen);pOffset++;
            pPStm.setString(pOffset, pEcl.redeStationBegriff);pOffset++;

            pPStm.setInt(pOffset, pEcl.detailBearbeitungZulassen);pOffset++;
            pPStm.setInt(pOffset, pEcl.reihenfolgeBearbeitungZulassen);pOffset++;
            pPStm.setInt(pOffset, pEcl.rednerBearbeitungZulassen);pOffset++;
            pPStm.setInt(pOffset, pEcl.praesenzAnzeigeAusfuehren);pOffset++;

            pPStm.setInt(pOffset, pEcl.pushFuerDiesenView);pOffset++;
            pPStm.setInt(pOffset, pEcl.rederaumErgaenzen);pOffset++;
            pPStm.setInt(pOffset, pEcl.testraumErgaenzen);pOffset++;
            pPStm.setInt(pOffset, pEcl.uhrzeitErgaenzen);pOffset++;

            pPStm.setInt(pOffset, pEcl.kontaktDatenAnzeigen);pOffset++;
            pPStm.setInt(pOffset, pEcl.infoFelderMitteilungAnzeigen);pOffset++;
            pPStm.setInt(pOffset, pEcl.kommentarInternAnzeigen);pOffset++;
            pPStm.setInt(pOffset, pEcl.kommentarInternEingabeMoeglich);pOffset++;
            pPStm.setInt(pOffset, pEcl.kommentarVersammlungsleiterAnzeigen);pOffset++;
            pPStm.setInt(pOffset, pEcl.kommentarVersammlungsleiterEingabeMoeglich);pOffset++;
            pPStm.setInt(pOffset, pEcl.nachrichtAnVersammlungsleiterMoeglich);pOffset++;

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

//  @formatter:off
    private final String[] felder= {"viewIdent", "setNr", 
            "viewBezeichnung", "textInMenue", "textUeberschrift",
            "summeAnzeigen0", "summeAnzeigen1", "summeAnzeigen2", "summeAnzeigen3", "summeAnzeigen4",
            "summeAnzeigen5", "summeAnzeigen6", "summeAnzeigen7", "summeAnzeigen8", "summeAnzeigen9",
            "summeTextVorZahl0", "summeTextVorZahl1", "summeTextVorZahl2", "summeTextVorZahl3", "summeTextVorZahl4",
            "summeTextVorZahl5", "summeTextVorZahl6", "summeTextVorZahl7", "summeTextVorZahl8", "summeTextVorZahl9",
            "summeTextNachZahl0", "summeTextNachZahl1", "summeTextNachZahl2", "summeTextNachZahl3", "summeTextNachZahl4",
            "summeTextNachZahl5", "summeTextNachZahl6", "summeTextNachZahl7", "summeTextNachZahl8", "summeTextNachZahl9",
            "aktualisierenButtonAnzeigen", "zurueckButtonAnzeigen", "sichtVersammlungsleiterAnzeigen", 
            "subViewsIdent0", "subViewsIdent1", "subViewsIdent2", "subViewsIdent3", "subViewsIdent4",
            "subViewsIdent5", "subViewsIdent6", "subViewsIdent7", "subViewsIdent8", "subViewsIdent9",

            "teststationNrAnzeigen", "testStationBegriff", "redestationNrAnzeigen", "redeStationBegriff",
            "detailBearbeitungZulassen", "reihenfolgeBearbeitungZulassen", "rednerBearbeitungZulassen", "praesenzAnzeigeAusfuehren", 
            "pushFuerDiesenView", "rederaumErgaenzen", "testraumErgaenzen", "uhrzeitErgaenzen",
            "kontaktDatenAnzeigen", "infoFelderMitteilungAnzeigen",
            "kommentarInternAnzeigen", "kommentarInternEingabeMoeglich", "kommentarVersammlungsleiterAnzeigen", "kommentarVersammlungsleiterEingabeMoeglich",
            "nachrichtAnVersammlungsleiterMoeglich"
            }; 
//  @formatter:on
    private boolean initErfolgt=false;
    private void initFelder(){
        if (initErfolgt) {return;}
        initErfolgt=true;
    }

    @Override
    public int insert(EclWortmeldetischView pEcl) {
        initFelder();
        return insertIntern(felder, pEcl);
    }


    /**Liest alle Demo-Kennungen ein*/
     public int readAll(int pSetNr) {
        PreparedStatement lPStm = null;
        int anzInArray = 0;
        try {
            String lSql = "SELECT * from " + getSchema() + getTableName()+" ";
            lSql = lSql + "WHERE setNr=? ORDER BY viewIdent;";
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

//     public int read(int pIdent) {
//        PreparedStatement lPStm = null;
//        int anzInArray = 0;
//        try {
//            String lSql = "SELECT * from " + getSchema() + getTableName()+" ";
//            lSql = lSql + " WHERE setIdent=?;";
//            lPStm = verbindung.prepareStatement(lSql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
//            lPStm.setInt(1, pIdent);
//           anzInArray = readIntern(lPStm);
//
//        } catch (Exception e) {
//            CaBug.drucke("003");
//            System.err.println(" " + e.getMessage());
//            return (-1);
//        }
//        return (anzInArray);
//    }
//
//    
//    /**Update. 
//     * 
//     * Returnwert:
//     * pfXyWurdeVonAnderemBenutzerVeraendert
//     * -1 => unbekannter Fehler
//     * 1 = Update wurde durchgeführt.
//     * 
//     */
//    public int update(EclWortmeldetischView pEcl) {
//        int ergebnis=0;
//        initFelder();
//
//        try {
//
//            String lSql = setzeUpdateBasisStringZusammen(felder)
//                    + " WHERE "
//                    + "setIdent=? ";
//
//            PreparedStatement lPStm = verbindung.prepareStatement(lSql);
//            fuellePreparedStatementKomplett(lPStm, 1, pEcl);
//            lPStm.setInt(getAnzFelder() + 1, pEcl.setIdent);
//
//            ergebnis = updateIntern(lPStm);
//        } catch (Exception e1) {
//            CaBug.drucke("001");
//            System.err.println(" " + e1.getMessage());
//            return (-1);
//        }
//
//        return ergebnis;
//    }
//
//
// 
//    /**Return-Werte:
//     * pfXyWurdeVonAnderemBenutzerVeraendert
//     * -1 => undefinierter Fehler
//     * 1 => Löschen erfolgreich
//     */
//    public int delete(int pIdent) {
//        int ergebnis=0;
//        try {
//            String sql = "DELETE FROM " + getSchema()+getTableName()+
//                    " WHERE setIdent=? ";
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
//

     /**Return-Werte:
      * pfXyWurdeVonAnderemBenutzerVeraendert
      * -1 => undefinierter Fehler
      * 1 => Löschen erfolgreich
      */
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
