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
import de.meetingapps.meetingportal.meetComEntities.EclKontaktformularThema;

public class DbKontaktformularThema  extends DbRoot<EclKontaktformularThema> {

//    private int logDrucken=10;
    
    /**Ggf. vor Benutzung setzen; anschließend unbedingt wieder auf true setzen!*/
    public boolean mandantenabhaengig=true;

    public DbKontaktformularThema(DbBundle pDbBundle) {
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
                + "`weitergabeText` varchar(150) DEFAULT NULL, " 
                + "`spracheDEText` varchar(150) DEFAULT NULL, " 
                + "`spracheENText` varchar(150) DEFAULT NULL, " 
                + "`sprache3Text` varchar(150) DEFAULT NULL, " 
                + "`sprache4Text` varchar(150) DEFAULT NULL, " 
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
        return "tbl_kontaktformularThema";
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
    EclKontaktformularThema decodeErgebnis(ResultSet pErgebnis) {
        EclKontaktformularThema lEclReturn = new EclKontaktformularThema();

        try {
            lEclReturn.ident = pErgebnis.getInt("ident");
            lEclReturn.db_version = pErgebnis.getLong("db_version");

            lEclReturn.nrHaupt = pErgebnis.getInt("nrHaupt");
            lEclReturn.nrSub = pErgebnis.getInt("nrSub");
            lEclReturn.weitergabeText = pErgebnis.getString("weitergabeText");
            lEclReturn.spracheDEText = pErgebnis.getString("spracheDEText");
            lEclReturn.spracheENText = pErgebnis.getString("spracheENText");
            lEclReturn.sprache3Text = pErgebnis.getString("sprache3Text");
            lEclReturn.sprache4Text = pErgebnis.getString("sprache4Text");
                     
       } catch (Exception e) {
            CaBug.drucke("001");
            System.err.println(" " + e.getMessage());
        }

        return lEclReturn;
    }

    @Override
    void fuellePreparedStatementKomplett(PreparedStatement pPStm, int pOffset, EclKontaktformularThema pEcl) {
        int startOffset = pOffset; /*Nur erforderlich zum Überprüfen von Programmierfehlern - Feldanzahl*/

        try {
//          @formatter:off
            
            pPStm.setInt(pOffset, pEcl.ident);pOffset++;
            pPStm.setLong(pOffset, pEcl.db_version);pOffset++;
            pPStm.setInt(pOffset, pEcl.nrHaupt);pOffset++;
            pPStm.setInt(pOffset, pEcl.nrSub);pOffset++;
             
            pPStm.setString(pOffset, pEcl.weitergabeText);pOffset++;
            pPStm.setString(pOffset, pEcl.spracheDEText);pOffset++;
            pPStm.setString(pOffset, pEcl.spracheENText);pOffset++;
            pPStm.setString(pOffset, pEcl.sprache3Text);pOffset++;
            pPStm.setString(pOffset, pEcl.sprache4Text);pOffset++;
          
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
        felder[4]="weitergabeText";
        felder[5]="spracheDEText";
        felder[6]="spracheENText";
        felder[7]="sprache3Text";
        felder[8]="sprache4Text";
        initErfolgt=true;
    }

    @Override
    public int insert(EclKontaktformularThema pEcl) {

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
                    " ORDER BY nrHaupt, nrSub;";
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
    public List<EclKontaktformularThema> readThemenliste(){
        List<EclKontaktformularThema> neueThemenliste=new LinkedList<EclKontaktformularThema>();
        int rc=readAll();
        if (rc<0) {return null;}
        
        EclKontaktformularThema letztesHauptthema=null;
         
        for (int i=0;i<anzErgebnis();i++) {
            EclKontaktformularThema arbeitsEintrag=ergebnisPosition(i);
            boolean verarbeitet=false;
            if (arbeitsEintrag.nrSub==0) {
                /*Haupteintrag*/
                letztesHauptthema=arbeitsEintrag;
                neueThemenliste.add(arbeitsEintrag);
                verarbeitet=true;
            }
            if (arbeitsEintrag.nrSub!=0) {
                /*Subeintrag*/
                if (letztesHauptthema==null) {
                    CaBug.drucke("002");
                    return null;
                }
                if (letztesHauptthema.nrHaupt!=arbeitsEintrag.nrHaupt) {
                    CaBug.drucke("003");
                    return null;
                }
                letztesHauptthema.subthemenList.add(arbeitsEintrag);
                verarbeitet=true;
            }
            
            if (verarbeitet==false) {
                CaBug.drucke("001");
                return null;
            }
        }
        
        return neueThemenliste;
    }

    
    /**Update. 
     * 
     * Returnwert:
     * pfXyWurdeVonAnderemBenutzerVeraendert
     * -1 => unbekannter Fehler
     * 1 = Update wurde durchgeführt.
     * 
     */
    public int update(EclKontaktformularThema pEcl) {
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
