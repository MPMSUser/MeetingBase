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
import java.util.LinkedList;
import java.util.List;

import de.meetingapps.meetingportal.meetComAllg.CaBug;
import de.meetingapps.meetingportal.meetComEntities.EclMenueEintrag;

public class DbMenueEintrag  extends DbRoot<EclMenueEintrag> {

//    private int logDrucken=10;
    
    /**Ggf. vor Benutzung setzen; anschließend unbedingt wieder auf true setzen!*/
    public boolean mandantenabhaengig=true;

    
    public DbMenueEintrag(DbBundle pDbBundle) {
        super(pDbBundle);
    }

    @Override
    String getCreateString() {

//      @formatter:off
       String createString = "CREATE TABLE " + getSchema() + getTableName()+" ( "
                + "`ident` int(11) NOT NULL, "
                + "`db_version` bigint(20) DEFAULT NULL, " 
                + "`nrHaupt` int(11) NOT NULL, "
                + "`nrSub` int(11) NOT NULL, "
                + "`nrSubSub` int(11) NOT NULL, "
                + "`funktionscode` int(11) NOT NULL, "
                + "`funktionscodeSub` int(11) NOT NULL, "
                + "`textNrGespeichert` int(11) NOT NULL, "
                + "PRIMARY KEY (`ident`) " + ") ";
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
        /**Nicht implementiert*/
    }

    @Override
    String getTableName() {
        return "tbl_menueEintrag";
    }

    @Override
    String getFeldFuerInterneIdent() {
        return "ident";
    }

    @Override
    int getAnzFelder() {
        return 8;
    }

    @Override
    EclMenueEintrag decodeErgebnis(ResultSet pErgebnis) {
        EclMenueEintrag lEclReturn = new EclMenueEintrag();

        try {
            lEclReturn.ident = pErgebnis.getInt("ident");
            lEclReturn.db_version = pErgebnis.getLong("db_version");

            lEclReturn.nrHaupt = pErgebnis.getInt("nrHaupt");
            lEclReturn.nrSub = pErgebnis.getInt("nrSub");
            lEclReturn.nrSubSub = pErgebnis.getInt("nrSubSub");
            lEclReturn.funktionscode = pErgebnis.getInt("funktionscode");
            lEclReturn.funktionscodeSub = pErgebnis.getInt("funktionscodeSub");
            lEclReturn.textNrGespeichert = pErgebnis.getInt("textNrGespeichert");
                     
       } catch (Exception e) {
            CaBug.drucke("001");
            System.err.println(" " + e.getMessage());
        }

        return lEclReturn;
    }

    @Override
    void fuellePreparedStatementKomplett(PreparedStatement pPStm, int pOffset, EclMenueEintrag pEcl) {
        int startOffset = pOffset; /*Nur erforderlich zum Überprüfen von Programmierfehlern - Feldanzahl*/

        try {
//          @formatter:off
            
            pPStm.setInt(pOffset, pEcl.ident);pOffset++;
            pPStm.setLong(pOffset, pEcl.db_version);pOffset++;
            pPStm.setInt(pOffset, pEcl.nrHaupt);pOffset++;
            pPStm.setInt(pOffset, pEcl.nrSub);pOffset++;
            pPStm.setInt(pOffset, pEcl.nrSubSub);pOffset++;
            pPStm.setInt(pOffset, pEcl.funktionscode);pOffset++;
            pPStm.setInt(pOffset, pEcl.funktionscodeSub);pOffset++;
            pPStm.setInt(pOffset, pEcl.textNrGespeichert);pOffset++;
       
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
        felder[2]="nrHaupt";
        felder[3]="nrSub";
        felder[4]="nrSubSub";
        felder[5]="funktionscode";
        felder[6]="funktionscodeSub";
        felder[7]="textNrGespeichert";
        initErfolgt=true;
    }

    @Override
    public int insert(EclMenueEintrag pEcl) {

        /*Ident-Vergabe noch nicht implementiert*/

        initFelder();
        return insertIntern(felder, pEcl);
    }

    /** Return-Wert = Anzahl der gefundenen Sätze (oder Fehlermeldung <0)*/
    public int readAll() {
        PreparedStatement lPStm = null;
        int anzInArray = 0;
        try {
            String lSql = "SELECT * from " + getSchema() + getTableName()+ 
                    " ORDER BY nrHaupt, nrSub, nrSubSub;";
            lPStm = verbindung.prepareStatement(lSql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            anzInArray = readIntern(lPStm);

        } catch (Exception e) {
            CaBug.drucke("003");
            System.err.println(" " + e.getMessage());
            return (-1);
        }
        return (anzInArray);
    }

    
    /**Fehler => return=null*/
    public List<EclMenueEintrag> readMenue(){
        List<EclMenueEintrag> neuesMenue=new LinkedList<EclMenueEintrag>();
        int rc=readAll();
        if (rc<0) {return null;}
        
        EclMenueEintrag letzterHauptmenue=null;
        EclMenueEintrag letzterSubmenue=null;
        
        for (int i=0;i<anzErgebnis();i++) {
            EclMenueEintrag arbeitsEintrag=ergebnisPosition(i);
            boolean verarbeitet=false;
            if (arbeitsEintrag.nrSub==0 && arbeitsEintrag.nrSubSub==0) {
                /*Haupteintrag*/
                letzterHauptmenue=arbeitsEintrag;
                letzterSubmenue=null;
                neuesMenue.add(arbeitsEintrag);
                verarbeitet=true;
            }
            if (arbeitsEintrag.nrSub!=0 && arbeitsEintrag.nrSubSub==0) {
                /*Subeintrag*/
                letzterSubmenue=arbeitsEintrag;
                 if (letzterHauptmenue==null) {
                    CaBug.drucke("002");
                    return null;
                }
                if (letzterHauptmenue.nrHaupt!=arbeitsEintrag.nrHaupt) {
                    CaBug.drucke("003");
                    return null;
                }
                letzterHauptmenue.submenueList.add(arbeitsEintrag);
                verarbeitet=true;
            }
            if (arbeitsEintrag.nrSub!=0 && arbeitsEintrag.nrSubSub!=0) {
                /*Subeintrag*/
                if (letzterSubmenue==null) {
                    CaBug.drucke("004");
                    return null;
                }
                if (letzterHauptmenue.nrHaupt!=arbeitsEintrag.nrHaupt) {
                    CaBug.drucke("005");
                    return null;
                }
                if (letzterSubmenue.nrSub!=arbeitsEintrag.nrSub) {
                    CaBug.drucke("006");
                    return null;
                }
                letzterSubmenue.submenueList.add(arbeitsEintrag);
                verarbeitet=true;
            }
            
            if (verarbeitet==false) {
                CaBug.drucke("001");
                return null;
            }
        }
        
        
        return neuesMenue;
    }
    
    
    
    /**Update. 
     * 
     * Returnwert:
     * pfXyWurdeVonAnderemBenutzerVeraendert
     * -1 => unbekannter Fehler
     * 1 = Update wurde durchgeführt.
     * 
     */
    public int update(EclMenueEintrag pEcl) {
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

    
}
