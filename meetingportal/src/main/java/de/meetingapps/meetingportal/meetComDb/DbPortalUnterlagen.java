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
import de.meetingapps.meetingportal.meetComEntities.EclPortalUnterlagen;

public class DbPortalUnterlagen extends DbRoot<EclPortalUnterlagen> {

    private int logDrucken = 10;

    public DbPortalUnterlagen(DbBundle pDbBundle) {
        super(pDbBundle);
    }

    @Override
    String getCreateString() {

//      @formatter:off
       String createString = "CREATE TABLE " + getSchema() + getTableName()+" ( "
                + "`ident` int(11) NOT NULL, "
                + "`reihenfolgeLoginOben` int(11) NOT NULL, "
                + "`reihenfolgeLoginUnten` int(11) NOT NULL, "
                + "`reihenfolgeExterneSeite` int(11) NOT NULL, "
                + "`reihenfolgeUnterlagen` int(11) NOT NULL, "
                + "`reihenfolgeBotschaften` int(11) NOT NULL, "
                + "`art` int(11) NOT NULL, "
                + "`artStyle` int(11) NOT NULL, "
                + "`previewLogin` int(11) NOT NULL, "
                + "`previewExterneSeite` int(11) NOT NULL, "
                + "`previewIntern` int(11) NOT NULL, "
                + "`dateiname` varchar(100) NOT NULL, "
                + "`dateiMehrsprachigVorhanden` int(11) NOT NULL, "
                + "`bezeichnungDE` varchar(10000) NOT NULL, "
                + "`bezeichnungEN` varchar(10000) NOT NULL, "
                + "`berechtigt` bigint(20) DEFAULT '0', " 
                + "`aktiv` int(11) NOT NULL, "
                + "`unterlagenbereichMenueNr` INT(11) NULL DEFAULT '0', "
                + "`reihenfolgeUnterlagenbereich` INT(11) NULL DEFAULT '0', "
                + "`previewUnterlagenbereich` INT(11) NULL DEFAULT '0', "
                + "PRIMARY KEY (`ident`) " + ") ";
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
        return "tbl_portalunterlagen";
    }

    @Override
    String getFeldFuerInterneIdent() {
        return "ident";
    }

    @Override
    int getAnzFelder() {
        return 20;
    }

    @Override
    EclPortalUnterlagen decodeErgebnis(ResultSet pErgebnis) {
        EclPortalUnterlagen lEclReturn = new EclPortalUnterlagen();

        try {
            lEclReturn.ident = pErgebnis.getInt("pu.ident");
            
            lEclReturn.reihenfolgeLoginOben = pErgebnis.getInt("pu.reihenfolgeLoginOben");
            lEclReturn.reihenfolgeLoginUnten = pErgebnis.getInt("pu.reihenfolgeLoginUnten");
            lEclReturn.reihenfolgeExterneSeite = pErgebnis.getInt("pu.reihenfolgeExterneSeite");
            lEclReturn.reihenfolgeUnterlagen = pErgebnis.getInt("pu.reihenfolgeUnterlagen");
            lEclReturn.reihenfolgeBotschaften = pErgebnis.getInt("pu.reihenfolgeBotschaften");
            
            lEclReturn.art = pErgebnis.getInt("pu.art");
            lEclReturn.artStyle = pErgebnis.getInt("pu.artStyle");
            
            lEclReturn.previewLogin = pErgebnis.getInt("pu.previewLogin");
            lEclReturn.previewExterneSeite = pErgebnis.getInt("pu.previewExterneSeite");
            lEclReturn.previewIntern = pErgebnis.getInt("pu.previewIntern");
            
            lEclReturn.dateiname = pErgebnis.getString("pu.dateiname");
            lEclReturn.dateiMehrsprachigVorhanden = pErgebnis.getInt("pu.dateiMehrsprachigVorhanden");
            lEclReturn.bezeichnungDE = pErgebnis.getString("pu.bezeichnungDE");
            lEclReturn.bezeichnungEN = pErgebnis.getString("pu.bezeichnungEN");

            long berechtigt=pErgebnis.getLong("pu.berechtigt");
            lEclReturn.berechtigtGast1=((berechtigt & 2)==2);
            lEclReturn.berechtigtGast2=((berechtigt & 4)==4);
            lEclReturn.berechtigtGast3=((berechtigt & 8)==8);
            lEclReturn.berechtigtGast4=((berechtigt & 16)==16);
            lEclReturn.berechtigtGast5=((berechtigt & 32)==32);
            lEclReturn.berechtigtGast6=((berechtigt & 64)==64);
            lEclReturn.berechtigtGast7=((berechtigt & 128)==128);
            lEclReturn.berechtigtGast8=((berechtigt & 256)==256);
            lEclReturn.berechtigtGast9=((berechtigt & 512)==512);
            lEclReturn.berechtigtGast10=((berechtigt & 1024)==1024);
            
            lEclReturn.berechtigtGastOnlineTeilnahmer1=((berechtigt & 2048)==2048);
            lEclReturn.berechtigtGastOnlineTeilnahmer2=((berechtigt & 4096)==4096);
            lEclReturn.berechtigtGastOnlineTeilnahmer3=((berechtigt & 8192)==8192);
            lEclReturn.berechtigtGastOnlineTeilnahmer4=((berechtigt & 16384)==16384);
            lEclReturn.berechtigtGastOnlineTeilnahmer5=((berechtigt & 32768)==32768);
            lEclReturn.berechtigtGastOnlineTeilnahmer6=((berechtigt & 65536)==65536);
            lEclReturn.berechtigtGastOnlineTeilnahmer7=((berechtigt & 131072)==131072);
            lEclReturn.berechtigtGastOnlineTeilnahmer8=((berechtigt & 262144)==262144);
            lEclReturn.berechtigtGastOnlineTeilnahmer9=((berechtigt & 524288)==524288);
            lEclReturn.berechtigtGastOnlineTeilnahmer10=((berechtigt & 1048576)==1048576);

            lEclReturn.berechtigtAktionaer=((berechtigt & 2097152)==2097152);
            lEclReturn.berechtigtAngemeldeterAktionaer=((berechtigt & 4194304)==4194304);
            lEclReturn.berechtigtOnlineTeilnahmeAktionaer=((berechtigt & 8388608)==8388608);

            lEclReturn.aktiv = pErgebnis.getInt("pu.aktiv");
            
            lEclReturn.unterlagenbereichMenueNr = pErgebnis.getInt("pu.unterlagenbereichMenueNr");
            lEclReturn.reihenfolgeUnterlagenbereich = pErgebnis.getInt("pu.reihenfolgeUnterlagenbereich");
            lEclReturn.previewUnterlagenbereich = pErgebnis.getInt("pu.previewUnterlagenbereich");
         

            
        } catch (Exception e) {
            CaBug.drucke("001");
            System.err.println(" " + e.getMessage());
        }

        return lEclReturn;
    }


    @Override
    void fuellePreparedStatementKomplett(PreparedStatement pPStm, int pOffset, EclPortalUnterlagen pEcl) {
        int startOffset = pOffset; /*Nur erforderlich zum Überprüfen von Programmierfehlern - Feldanzahl*/

        try {
//          @formatter:off
            
            pPStm.setInt(pOffset, pEcl.ident);pOffset++;

            pPStm.setInt(pOffset, pEcl.reihenfolgeLoginOben);pOffset++;
            pPStm.setInt(pOffset, pEcl.reihenfolgeLoginUnten);pOffset++;
            pPStm.setInt(pOffset, pEcl.reihenfolgeExterneSeite);pOffset++;
            pPStm.setInt(pOffset, pEcl.reihenfolgeUnterlagen);pOffset++;
            pPStm.setInt(pOffset, pEcl.reihenfolgeBotschaften);pOffset++;

             pPStm.setInt(pOffset, pEcl.art);pOffset++;
            pPStm.setInt(pOffset, pEcl.artStyle);pOffset++;
            
            pPStm.setInt(pOffset, pEcl.previewLogin);pOffset++;
            pPStm.setInt(pOffset, pEcl.previewExterneSeite);pOffset++;
            pPStm.setInt(pOffset, pEcl.previewIntern);pOffset++;


            pPStm.setString(pOffset, pEcl.dateiname);pOffset++;
            pPStm.setInt(pOffset, pEcl.dateiMehrsprachigVorhanden);pOffset++;
            pPStm.setString(pOffset, pEcl.bezeichnungDE);pOffset++;
            pPStm.setString(pOffset, pEcl.bezeichnungEN);pOffset++;
           
            long berechtigt=0;
            if (pEcl.berechtigtGast1) {berechtigt=(berechtigt | 2);}
            if (pEcl.berechtigtGast2) {berechtigt=(berechtigt | 4);}
            if (pEcl.berechtigtGast3) {berechtigt=(berechtigt | 8);}
            if (pEcl.berechtigtGast4) {berechtigt=(berechtigt | 16);}
            if (pEcl.berechtigtGast5) {berechtigt=(berechtigt | 32);}
            if (pEcl.berechtigtGast6) {berechtigt=(berechtigt | 64);}
            if (pEcl.berechtigtGast7) {berechtigt=(berechtigt | 128);}
            if (pEcl.berechtigtGast8) {berechtigt=(berechtigt | 256);}
            if (pEcl.berechtigtGast9) {berechtigt=(berechtigt | 512);}
            if (pEcl.berechtigtGast10) {berechtigt=(berechtigt | 1024);}

            if (pEcl.berechtigtGastOnlineTeilnahmer1) {berechtigt=(berechtigt | 2048);}
            if (pEcl.berechtigtGastOnlineTeilnahmer2) {berechtigt=(berechtigt | 4096);}
            if (pEcl.berechtigtGastOnlineTeilnahmer3) {berechtigt=(berechtigt | 8192);}
            if (pEcl.berechtigtGastOnlineTeilnahmer4) {berechtigt=(berechtigt | 16384);}
            if (pEcl.berechtigtGastOnlineTeilnahmer5) {berechtigt=(berechtigt | 32768);}
            if (pEcl.berechtigtGastOnlineTeilnahmer6) {berechtigt=(berechtigt | 65536);}
            if (pEcl.berechtigtGastOnlineTeilnahmer7) {berechtigt=(berechtigt | 131072);}
            if (pEcl.berechtigtGastOnlineTeilnahmer8) {berechtigt=(berechtigt | 262144);}
            if (pEcl.berechtigtGastOnlineTeilnahmer9) {berechtigt=(berechtigt | 524288);}
            if (pEcl.berechtigtGastOnlineTeilnahmer10) {berechtigt=(berechtigt | 1048576);}

            if (pEcl.berechtigtAktionaer) {berechtigt=(berechtigt | 2097152);}
            if (pEcl.berechtigtAngemeldeterAktionaer) {berechtigt=(berechtigt | 4194304);}
            if (pEcl.berechtigtOnlineTeilnahmeAktionaer) {berechtigt=(berechtigt | 8388608);}

            pPStm.setLong(pOffset, berechtigt);pOffset++;

            pPStm.setInt(pOffset, pEcl.aktiv);pOffset++;

            pPStm.setInt(pOffset, pEcl.unterlagenbereichMenueNr);pOffset++;
            pPStm.setInt(pOffset, pEcl.reihenfolgeUnterlagenbereich);pOffset++;
            pPStm.setInt(pOffset, pEcl.previewUnterlagenbereich);pOffset++;


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

    private String[] felder = null;
    private boolean initErfolgt = false;

    private void initFelder() {
        if (initErfolgt) {
            return;
        }
        felder = new String[] { "ident", 
                "reihenfolgeLoginOben", "reihenfolgeLoginUnten", "reihenfolgeExterneSeite", "reihenfolgeUnterlagen", "reihenfolgeBotschaften",
                "art", "artStyle",
                "previewLogin", "previewExterneSeite", "previewIntern", 
                "dateiname", "dateiMehrsprachigVorhanden", "bezeichnungDE", "bezeichnungEN",
                "berechtigt", "aktiv",
                "unterlagenbereichMenueNr", "reihenfolgeUnterlagenbereich", "previewUnterlagenbereich" 
                };

        initErfolgt = true;
    }

    @Override
    public int insert(EclPortalUnterlagen pEcl) {
        /*Maxident bestimmen*/
        initFelder();
        readAll();
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
        int rc=insertIntern(felder, pEcl);
        if (rc<1) {
            CaBug.drucke("001");
            return rc;
        }
        return -1;
    }

    /**Einfügen; ident wird automatisch;
     * 
     * Einfügen/Update läuft hier so:
     * > Alle löschen
     * > Alle einfügen.
     * 
     * Ein einzelnes Einfügen ist nicht vorgesehen!
     */
    public int insert(List<EclPortalUnterlagen> pEclList) {

        initFelder();
        deleteAll();
        int maxIdent=0;
        for (int i=0;i<pEclList.size();i++) {
            if (pEclList.get(i).ident>maxIdent) {
                maxIdent=pEclList.get(i).ident;
            }
        }
       
        for (int i=0;i<pEclList.size();i++) {
            EclPortalUnterlagen pEcl=pEclList.get(i);
            if (pEcl.ident==0) {
                maxIdent++;
                pEcl.ident=maxIdent;
            }
            int rc=insertIntern(felder, pEcl);
            if (rc<1) {
                CaBug.drucke("001");
                return rc;
            }
        }
        return pEclList.size();
    }

    public int readId(int id) {
        int anzInArray = 0;
        PreparedStatement lPStm = null;

        try {
            String lSql = "SELECT pu.* from " + getSchema() + getTableName() + " pu where " + "pu.ident=?;";
            lPStm = verbindung.prepareStatement(lSql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            lPStm.setInt(1, id);

            anzInArray = readIntern(lPStm);
        } catch (Exception e) {
            CaBug.drucke("003");
            System.err.println(" " + e.getMessage());
            return (-1);
        }
        return (anzInArray);
    }

    /**Verwendung z.B. um die Unterlage zu finden, die einen bestimmten Videolink enthält*/
    public int readDateiname(String pDateiname) {
        int anzInArray = 0;
        PreparedStatement lPStm = null;

        try {
            String lSql = "SELECT pu.* from " + getSchema() + getTableName() + " pu where " + "pu.dateiname=?;";
            lPStm = verbindung.prepareStatement(lSql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            lPStm.setString(1, pDateiname);

            anzInArray = readIntern(lPStm);
        } catch (Exception e) {
            CaBug.drucke("003");
            System.err.println(" " + e.getMessage());
            return (-1);
        }
        return (anzInArray);
    }

    public int readAll() {
        int anzInArray = 0;
        PreparedStatement lPStm = null;

        try {
            String lSql = "SELECT pu.* from " + getSchema() + getTableName() + " pu ;";
            lPStm = verbindung.prepareStatement(lSql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);

            anzInArray = readIntern(lPStm);
        } catch (Exception e) {
            CaBug.drucke("003");
            System.err.println(" " + e.getMessage());
            return (-1);
        }
        return (anzInArray);
    }

 
    public int update(EclPortalUnterlagen pEcl) {
        int ergebnis=0;
        initFelder();

        try {

            String lSql = setzeUpdateBasisStringZusammen(felder)
                    + " WHERE "
                    + "ident=?  ";

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


 
    public int delete(int pId) {
        int anzahl = 0;
        initFelder();

        try {

            String lSql = "DELETE FROM " + getSchema() + getTableName() + " WHERE " + " ident=? ";

            CaBug.druckeLog(lSql, logDrucken, 10);
            PreparedStatement lPStm = verbindung.prepareStatement(lSql);
            lPStm.setInt(1, pId);

            anzahl = deleteIntern(lPStm);
        } catch (Exception e1) {
            CaBug.drucke("001");
            System.err.println(" " + e1.getMessage());
            return (-1);
        }

        return anzahl;
    }


 
}
