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
import de.meetingapps.meetingportal.meetComEntities.EclVeranstaltungenElement;

/**Hinweis: rueckfragen wird wie fragen behandelt*/

public class  DbVeranstaltungenElement extends DbRoot<EclVeranstaltungenElement> {

//    private int logDrucken=10;
    
    public DbVeranstaltungenElement(DbBundle pDbBundle) {
        super(pDbBundle);
    }

    
    @Override
    String getCreateString() {

        //      @formatter:off
        String createString = "CREATE TABLE " + getSchema() + getTableName()+" ( "
                + "`identElement` int NOT NULL DEFAULT 0, " 
                + "`identVeranstaltung` int NOT NULL DEFAULT 0, " 
                + "`gehoertZuElement` int NOT NULL DEFAULT 0, " 
                + "`position` int NOT NULL DEFAULT 0, " 
                + "`beschreibung` varchar(1000) NOT NULL DEFAULT '', " 
                + "`textIntern` varchar(100) NOT NULL DEFAULT '', " 
                + "`einrueckungsEbene` int NOT NULL DEFAULT 0, " 
                + "`aktivierungsStatus` int NOT NULL DEFAULT 0, " 
                + "`wirdVerwendetWennVorgaengerAktivierungsStatusGleich` int NOT NULL DEFAULT 0, " 
                + "`wirdVerwendetWennVorgaengerGleich` int NOT NULL DEFAULT 0, " 
                + "`wirdVerwendetWennGattung` int NOT NULL DEFAULT 0, " 
                + "`vergleichswertVorgaenger` varchar(100) NOT NULL DEFAULT '', " 
                + "`elementTyp` int NOT NULL DEFAULT 0, " 
                + "`elementDesign` int NOT NULL DEFAULT 0, " 
                + "`vorbelegenMit` int NOT NULL DEFAULT 0, " 
                + "`eingabezwang` int NOT NULL DEFAULT 0, " 
                + "`defaultWert` varchar(100) NOT NULL DEFAULT '', " 
                + "`textExtern` varchar(1000) NOT NULL DEFAULT '', " 
                + "`textExternNach` varchar(1000) NOT NULL DEFAULT '', " 
                + "`minimalWert` int NOT NULL DEFAULT 0, " 
                + "`maximalWert` int NOT NULL DEFAULT 0, " 
                + "`maximalSumme` int NOT NULL DEFAULT 0, " 
                + "`meldungWennMaximalSummeUeberschritten` varchar(200) NOT NULL DEFAULT '', " 
                + "`meldungWennUnzulaessigerWert` varchar(200) NOT NULL DEFAULT '', " 
               + "PRIMARY KEY (`identElement`) " + ") ";
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
         return "tbl_veranstaltungenElement";
    }


    @Override
    String getFeldFuerInterneIdent() {
        return "identElement";
    }

    @Override
    int getAnzFelder() {
        return 24;
    }


    @Override
    EclVeranstaltungenElement decodeErgebnis(ResultSet pErgebnis) {
        EclVeranstaltungenElement lEclReturn = new EclVeranstaltungenElement();

        try {
            lEclReturn.identElement = pErgebnis.getInt("identElement");
            
            lEclReturn.identVeranstaltung = pErgebnis.getInt("identVeranstaltung");
            lEclReturn.gehoertZuElement = pErgebnis.getInt("gehoertZuElement");
            lEclReturn.position = pErgebnis.getInt("position");
            
            lEclReturn.beschreibung = pErgebnis.getString("beschreibung");
            lEclReturn.textIntern = pErgebnis.getString("textIntern");
            
            lEclReturn.einrueckungsEbene = pErgebnis.getInt("einrueckungsEbene");
            lEclReturn.aktivierungsStatus = pErgebnis.getInt("aktivierungsStatus");
            lEclReturn.wirdVerwendetWennVorgaengerAktivierungsStatusGleich = pErgebnis.getInt("wirdVerwendetWennVorgaengerAktivierungsStatusGleich");
            lEclReturn.wirdVerwendetWennVorgaengerGleich = pErgebnis.getInt("wirdVerwendetWennVorgaengerGleich");
            lEclReturn.wirdVerwendetWennGattung = pErgebnis.getInt("wirdVerwendetWennGattung");
            lEclReturn.vergleichswertVorgaenger = pErgebnis.getString("vergleichswertVorgaenger");
            lEclReturn.elementTyp = pErgebnis.getInt("elementTyp");
            lEclReturn.elementDesign = pErgebnis.getInt("elementDesign");
            lEclReturn.vorbelegenMit = pErgebnis.getInt("vorbelegenMit");
            lEclReturn.eingabezwang = pErgebnis.getInt("eingabezwang");
            
            lEclReturn.defaultWert = pErgebnis.getString("defaultWert");
            lEclReturn.textExtern = pErgebnis.getString("textExtern");
            lEclReturn.textExternNach = pErgebnis.getString("textExternNach");

            lEclReturn.minimalWert = pErgebnis.getInt("minimalWert");
            lEclReturn.maximalWert = pErgebnis.getInt("maximalWert");
            lEclReturn.maximalSumme = pErgebnis.getInt("maximalSumme");

            lEclReturn.meldungWennMaximalSummeUeberschritten = pErgebnis.getString("meldungWennMaximalSummeUeberschritten");
            lEclReturn.meldungWennUnzulaessigerWert = pErgebnis.getString("meldungWennUnzulaessigerWert");


            
       } catch (Exception e) {
            CaBug.drucke("001");
            System.err.println(" " + e.getMessage());
        }

        return lEclReturn;
    }



    @Override
    void fuellePreparedStatementKomplett(PreparedStatement pPStm, int pOffset, EclVeranstaltungenElement pEcl) {
        int startOffset = pOffset; /*Nur erforderlich zum Überprüfen von Programmierfehlern - Feldanzahl*/

        try {
//          @formatter:off
            
            pPStm.setInt(pOffset, pEcl.identElement);pOffset++;

            pPStm.setInt(pOffset, pEcl.identVeranstaltung);pOffset++;
            pPStm.setInt(pOffset, pEcl.gehoertZuElement);pOffset++;
            pPStm.setInt(pOffset, pEcl.position);pOffset++;
             
            pPStm.setString(pOffset, pEcl.beschreibung);pOffset++;
            pPStm.setString(pOffset, pEcl.textIntern);pOffset++;

            pPStm.setInt(pOffset, pEcl.einrueckungsEbene);pOffset++;
            pPStm.setInt(pOffset, pEcl.aktivierungsStatus);pOffset++;
            pPStm.setInt(pOffset, pEcl.wirdVerwendetWennVorgaengerAktivierungsStatusGleich);pOffset++;
            pPStm.setInt(pOffset, pEcl.wirdVerwendetWennVorgaengerGleich);pOffset++;
            pPStm.setInt(pOffset, pEcl.wirdVerwendetWennGattung);pOffset++;
            pPStm.setString(pOffset, pEcl.vergleichswertVorgaenger);pOffset++;
            pPStm.setInt(pOffset, pEcl.elementTyp);pOffset++;
            pPStm.setInt(pOffset, pEcl.elementDesign);pOffset++;
            pPStm.setInt(pOffset, pEcl.vorbelegenMit);pOffset++;
            pPStm.setInt(pOffset, pEcl.eingabezwang);pOffset++;

            pPStm.setString(pOffset, pEcl.defaultWert);pOffset++;
            pPStm.setString(pOffset, pEcl.textExtern);pOffset++;
            pPStm.setString(pOffset, pEcl.textExternNach);pOffset++;

            pPStm.setInt(pOffset, pEcl.minimalWert);pOffset++;
            pPStm.setInt(pOffset, pEcl.maximalWert);pOffset++;
            pPStm.setInt(pOffset, pEcl.maximalSumme);pOffset++;

            pPStm.setString(pOffset, pEcl.meldungWennMaximalSummeUeberschritten);pOffset++;
            pPStm.setString(pOffset, pEcl.meldungWennUnzulaessigerWert);pOffset++;

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
    private final String[] felder= {"identElement", 
            "identVeranstaltung", "gehoertZuElement", "position",
            "beschreibung", "textIntern", 
            "einrueckungsEbene", "aktivierungsStatus", 
            "wirdVerwendetWennVorgaengerAktivierungsStatusGleich", "wirdVerwendetWennVorgaengerGleich", "wirdVerwendetWennGattung", 
            "vergleichswertVorgaenger", 
            "elementTyp", "elementDesign", "vorbelegenMit", "eingabezwang",
            "defaultWert", "textExtern", "textExternNach", 
            "minimalWert", "maximalWert", "maximalSumme",
            "meldungWennMaximalSummeUeberschritten", "meldungWennUnzulaessigerWert" 
            }; 
//  @formatter:on
    private boolean initErfolgt=false;
    private void initFelder(){
        if (initErfolgt) {return;}
        initErfolgt=true;
    }

    @Override
    public int insert(EclVeranstaltungenElement pEcl) {
        initFelder();
        return insertIntern(felder, pEcl);
    }


    /**Liest alle Demo-Kennungen ein*/
     public int readAll() {
        PreparedStatement lPStm = null;
        int anzInArray = 0;
        try {
            String lSql = "SELECT * from " + getSchema() + getTableName()+" "
                    + "ORDER BY identVeranstaltung, gehoertZuElement, position";
            lPStm = verbindung.prepareStatement(lSql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
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
//    public int update(EclVeranstaltungenElement pEcl) {
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


}
