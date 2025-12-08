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
import de.meetingapps.meetingportal.meetComEntities.EclZugeordneteAktionaerePraesenz;

/**Hinweis: rueckfragen wird wie fragen behandelt*/

public class  DbZugeordneteAktionaerePraesenz extends DbRoot<EclZugeordneteAktionaerePraesenz> {

//    private int logDrucken=10;
    
    public DbZugeordneteAktionaerePraesenz(DbBundle pDbBundle) {
        super(pDbBundle);
    }

    
    @Override
    String getCreateString() {

        //      @formatter:off
        String createString = "CREATE TABLE " + getSchema() + getTableName()+" ( "
                + "`anwesendePersonLoginIdent` char(11) NOT NULL DEFAULT '', " 
                + "`zugeordneterAktionaer` char(11) NOT NULL DEFAULT '', " 
                + "`geerbtVonZugeordnetemAktionaer` char(11) NOT NULL DEFAULT '', " 
                + "`zuordnungsArt` int NOT NULL DEFAULT 0, " 
                + "`zuordnungIstAktiv` int NOT NULL DEFAULT 0, " 
                + "`zuordnungAktiviertZeitpunkt` char(11) NOT NULL DEFAULT '', " 
                + "`zuordnungDeaktiviertZeitpunkt` char(11) NOT NULL DEFAULT '', " 
                + "PRIMARY KEY (`anwesendePersonLoginIdent`, `zugeordneterAktionaer`, "
                + "`geerbtVonZugeordnetemAktionaer`, `zuordnungIstAktiv`, `zuordnungsArt`) " + ") ";
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
         return "tbl_zugeordneteAktionaerePraesenz";
    }


    @Override
    String getFeldFuerInterneIdent() {
        return "";
    }

    @Override
    int getAnzFelder() {
        return 7;
    }


    @Override
    EclZugeordneteAktionaerePraesenz decodeErgebnis(ResultSet pErgebnis) {
        EclZugeordneteAktionaerePraesenz lEclReturn = new EclZugeordneteAktionaerePraesenz();

        try {
            lEclReturn.anwesendePersonLoginIdent = pErgebnis.getString("anwesendePersonLoginIdent");
            lEclReturn.zugeordneterAktionaer = pErgebnis.getString("zugeordneterAktionaer");
            lEclReturn.geerbtVonZugeordnetemAktionaer = pErgebnis.getString("geerbtVonZugeordnetemAktionaer");
           
            lEclReturn.zuordnungsArt = pErgebnis.getInt("zuordnungsArt");
            lEclReturn.zuordnungIstAktiv = pErgebnis.getInt("zuordnungIstAktiv");

            lEclReturn.zuordnungAktiviertZeitpunkt = pErgebnis.getString("zuordnungAktiviertZeitpunkt");
            lEclReturn.zuordnungDeaktiviertZeitpunkt = pErgebnis.getString("zuordnungDeaktiviertZeitpunkt");
 
            /*Zusatzfeld - alter Inhalt sozusagen*/
            lEclReturn.statusInDatenbank=lEclReturn.zuordnungIstAktiv;
            
       } catch (Exception e) {
            CaBug.drucke("001");
            System.err.println(" " + e.getMessage());
        }

        return lEclReturn;
    }



    @Override
    void fuellePreparedStatementKomplett(PreparedStatement pPStm, int pOffset, EclZugeordneteAktionaerePraesenz pEcl) {
        int startOffset = pOffset; /*Nur erforderlich zum Überprüfen von Programmierfehlern - Feldanzahl*/

        try {
//          @formatter:off

            pPStm.setString(pOffset, pEcl.anwesendePersonLoginIdent);pOffset++;
            pPStm.setString(pOffset, pEcl.zugeordneterAktionaer);pOffset++;
            pPStm.setString(pOffset, pEcl.geerbtVonZugeordnetemAktionaer);pOffset++;

            pPStm.setInt(pOffset, pEcl.zuordnungsArt);pOffset++;
            pPStm.setInt(pOffset, pEcl.zuordnungIstAktiv);pOffset++;

            pPStm.setString(pOffset, pEcl.zuordnungAktiviertZeitpunkt);pOffset++;
            pPStm.setString(pOffset, pEcl.zuordnungDeaktiviertZeitpunkt);pOffset++;
             
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
    private final String[] felder= {"anwesendePersonLoginIdent", "zugeordneterAktionaer",
            "geerbtVonZugeordnetemAktionaer",
            "zuordnungsArt", "zuordnungIstAktiv", 
             "zuordnungAktiviertZeitpunkt", "zuordnungDeaktiviertZeitpunkt"
            }; 
//  @formatter:on
    private boolean initErfolgt=false;
    private void initFelder(){
        if (initErfolgt) {return;}
        initErfolgt=true;
    }

    @Override
    public int insert(EclZugeordneteAktionaerePraesenz pEcl) {
        initFelder();
        return insertIntern(felder, pEcl);
    }


    /**Liest alle Werte für eine Login-Kennung ein*/
     public int readLoginKennung(String pLoginKennung) {
        PreparedStatement lPStm = null;
        int anzInArray = 0;
        try {
            String lSql = "SELECT * from " + getSchema() + getTableName()+" "
                    + "WHERE anwesendePersonLoginIdent=? ORDER BY zugeordneterAktionaer  ";
            lPStm = verbindung.prepareStatement(lSql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            lPStm.setString(1, pLoginKennung);
            anzInArray = readIntern(lPStm);

        } catch (Exception e) {
            CaBug.drucke("003");
            System.err.println(" " + e.getMessage());
            return (-1);
        }
        return (anzInArray);
    }

     /**Liest alle Werte für eine Login-Kennung ein*/
     public int readAlleAktivenVertreterEinesAktionaers(String pAktionaersnummer) {
        PreparedStatement lPStm = null;
        int anzInArray = 0;
        try {
            String lSql = "SELECT * from " + getSchema() + getTableName()+" "
                    + "WHERE zugeordneterAktionaer=? AND zuordnungIstAktiv=1";
            lPStm = verbindung.prepareStatement(lSql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            lPStm.setString(1, pAktionaersnummer);
            anzInArray = readIntern(lPStm);

        } catch (Exception e) {
            CaBug.drucke("003");
            System.err.println(" " + e.getMessage());
            return (-1);
        }
        return (anzInArray);
    }

     /**Liest alle Werte für eine Login-Kennung ein*/
     public int readAlleVertreterEinesAktionaers(String pAktionaersnummer) {
        PreparedStatement lPStm = null;
        int anzInArray = 0;
        try {
            String lSql = "SELECT * from " + getSchema() + getTableName()+" "
                    + "WHERE zugeordneterAktionaer=?";
            lPStm = verbindung.prepareStatement(lSql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            lPStm.setString(1, pAktionaersnummer);
            anzInArray = readIntern(lPStm);

        } catch (Exception e) {
            CaBug.drucke("003");
            System.err.println(" " + e.getMessage());
            return (-1);
        }
        return (anzInArray);
    }

     /**Löscht die Einträge für die loginKennung und schreibt die neuen*/
     public int updateLoginKennung(String pLoginKennung, List<EclZugeordneteAktionaerePraesenz> pEcl) {
         delete(pLoginKennung);
         
         int ergebnis=0;
         initFelder();

         if (pEcl!=null) {
             for (int i=0;i<pEcl.size();i++) {
                 insert(pEcl.get(i));
             }
         }


         return ergebnis;
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
//    public int update(EclZugeordneteAktionaerePraesenz pEcl) {
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
    /**Return-Werte:
     * pfXyWurdeVonAnderemBenutzerVeraendert
     * -1 => undefinierter Fehler
     * 1 => Löschen erfolgreich
     */
    public int delete(String pLoginKennung) {
        int ergebnis=0;
        try {
            String sql = "DELETE FROM " + getSchema()+getTableName()+
                    " WHERE anwesendePersonLoginIdent=? ";
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
