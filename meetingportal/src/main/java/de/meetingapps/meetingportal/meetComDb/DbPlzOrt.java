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
import de.meetingapps.meetingportal.meetComEntities.EclPlzOrt;

public class DbPlzOrt extends DbRoot<EclPlzOrt> {

    private int logDrucken = 10;

    /**Ggf. vor Benutzung setzen; anschließend unbedingt wieder auf false setzen!*/
    public boolean mandantenabhaengig = false;

    public DbPlzOrt(DbBundle pDbBundle) {
        super(pDbBundle);
    }

    @Override
    String getCreateString() {

//      @formatter:off
       String createString = "CREATE TABLE " + getSchema() + getTableName()+" ( "
                + "`id` int(11) NOT NULL, "
                + "`plz` varchar(10) NOT NULL, "
                + "`ort` varchar(100) NOT NULL, "
                + "`version` int(11) NOT NULL, "
              
                 + "PRIMARY KEY (`plz`, `ort`) " + ") ";
//      @formatter:on
        return createString;
    }

    //  public int createTable() {
    //  int rc = 0;
    //  DbLowLevel lDbLowLevel = new DbLowLevel(dbBundle);
    //  rc = lDbLowLevel.createTable(
    //          "CREATE TABLE " + dbBundle.getSchemaAllgemein() + "tbl_staaten ( " + "`id` int(11) NOT NULL, "
    //                  + "`code` varchar(10) DEFAULT NULL, " + "`nameDE` varchar(100) DEFAULT NULL, "
    //                  + "`nameEN` varchar(100) DEFAULT NULL, " 
    //                  + "PRIMARY KEY (`id`), " 
    //                  + "KEY `IDX_CODE` (`code`) "
    //                  + ") ");
    //  return rc;
    //}

    @Override
    String getSchema() {
        if (mandantenabhaengig) {
            return dbBundle.getSchemaMandant();
        } else {
            return dbBundle.getSchemaAllgemein();
        }
    }

    @Override
    void resetInterneIdent(int pHoechsteIdent) {
    }

    @Override
    String getTableName() {
        return "tbl_plzort";
    }

    @Override
    String getFeldFuerInterneIdent() {
        return "id";
    }

    @Override
    int getAnzFelder() {
        return 4;
    }

    @Override
    EclPlzOrt decodeErgebnis(ResultSet pErgebnis) {
        EclPlzOrt lEclReturn = new EclPlzOrt();

        try {
            lEclReturn.id = pErgebnis.getInt("po.id");
            lEclReturn.plz = pErgebnis.getString("po.plz");
            lEclReturn.ort = pErgebnis.getString("po.ort");
            lEclReturn.version = pErgebnis.getInt("po.version");
        } catch (Exception e) {
            CaBug.drucke("001");
            System.err.println(" " + e.getMessage());
        }

        return lEclReturn;
    }


    @Override
    void fuellePreparedStatementKomplett(PreparedStatement pPStm, int pOffset, EclPlzOrt pEcl) {
        int startOffset = pOffset; /*Nur erforderlich zum Überprüfen von Programmierfehlern - Feldanzahl*/

        try {
//          @formatter:off
            
            pPStm.setInt(pOffset, pEcl.id);pOffset++;
            pPStm.setString(pOffset, pEcl.plz);pOffset++;
            pPStm.setString(pOffset, pEcl.ort);pOffset++;
            pPStm.setInt(pOffset, pEcl.version);pOffset++;
           
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
        felder = new String[] { "id", "plz", "ort", "version" };

        initErfolgt = true;
    }

    /**Einfügen; neue ident wird nicht automatisch vergeben;
     */
    @Override
    public int insert(EclPlzOrt pEcl) {

        initFelder();
        return insertIntern(felder, pEcl);
    }

    public int readId(int id) {
        int anzInArray = 0;
        PreparedStatement lPStm = null;

        try {
            String lSql = "SELECT po.* from " + getSchema() + getTableName() + " po where " + "po.id=?;";
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

    public int readPlz(String plz) {
        int anzInArray = 0;
        PreparedStatement lPStm = null;

        try {
            String lSql = "SELECT po.* from " + getSchema() + getTableName() + " po where " + "po.plz=?;";
            lPStm = verbindung.prepareStatement(lSql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            lPStm.setString(1, plz);

            anzInArray = readIntern(lPStm);
        } catch (Exception e) {
            CaBug.drucke("003");
            System.err.println(" " + e.getMessage());
            return (-1);
        }
        return (anzInArray);
    }

    public int readPlzOrt(String plz, String ort) {
        int anzInArray = 0;
        PreparedStatement lPStm = null;

        try {
            String lSql = "SELECT po.* from " + getSchema() + getTableName() + " po where " + "po.plz=? AND po.ort=?;";
            lPStm = verbindung.prepareStatement(lSql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            lPStm.setString(1, plz);
            lPStm.setString(2, ort);

            anzInArray = readIntern(lPStm);
        } catch (Exception e) {
            CaBug.drucke("003");
            System.err.println(" " + e.getMessage());
            return (-1);
        }
        return (anzInArray);
    }

    public int update(EclPlzOrt pEcl) {
        int anzahl = 0;
        initFelder();

        try {

            String lSql = setzeUpdateBasisStringZusammen(felder) + " WHERE " + "plz=? AND ort=?";

            PreparedStatement lPStm = verbindung.prepareStatement(lSql);
            fuellePreparedStatementKomplett(lPStm, 1, pEcl);
            lPStm.setString(getAnzFelder() + 1, pEcl.plz);
            lPStm.setString(getAnzFelder() + 2, pEcl.ort);

            anzahl = updateIntern(lPStm);
        } catch (Exception e1) {
            CaBug.drucke("001");
            System.err.println(" " + e1.getMessage());
            return (-1);
        }

        return anzahl;
    }

    public int updateAllUnverarbeitet() {
        int anzahl = 0;
        initFelder();

        try {

            String lSql = "UPDATE "+ getSchema() + getTableName() +" SET version=0;";

            PreparedStatement lPStm = verbindung.prepareStatement(lSql);

            anzahl = updateIntern(lPStm);
        } catch (Exception e1) {
            CaBug.drucke("001");
            System.err.println(" " + e1.getMessage());
            return (-1);
        }

        return anzahl;
    }

    public int deleteAllUnverarbeitet() {
        int anzahl = 0;
        initFelder();

        try {

            String lSql = "DELETE FROM " + getSchema() + getTableName() + " WHERE " + " version=0 ";

            CaBug.druckeLog(lSql, logDrucken, 10);
            PreparedStatement lPStm = verbindung.prepareStatement(lSql);

            anzahl = deleteIntern(lPStm);
        } catch (Exception e1) {
            CaBug.drucke("001");
            System.err.println(" " + e1.getMessage());
            return (-1);
        }

        return anzahl;
    }

    public int delete(int pId) {
        int anzahl = 0;
        initFelder();

        try {

            String lSql = "DELETE FROM " + getSchema() + getTableName() + " WHERE " + " id=? ";

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


    /**pSortierung:
     * 0=wahlfrei
     * 1=Plz
     * 2=Ort
     * 
      */
    public int readAll(int pSortierung) {
        int anzInArray = 0;
        PreparedStatement lPStm = null;

        try {
            String lSql = "SELECT po.* from " + getSchema() + getTableName() + " po ";
            switch (pSortierung) {
            case 1:
                lSql = lSql + " ORDER BY po.plz ";
                break;
            case 2:
                lSql = lSql + " ORDER BY po.ort ";
                break;
           }
            lPStm = verbindung.prepareStatement(lSql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);

            anzInArray = readIntern(lPStm);
        } catch (Exception e) {
            CaBug.drucke("003");
            System.err.println(" " + e.getMessage());
            return (-1);
        }
        return (anzInArray);
    }

 
}
