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
import de.meetingapps.meetingportal.meetComEntities.EclStaaten;

public class DbStaaten extends DbRoot<EclStaaten> {

    private int logDrucken = 10;

    /**Ggf. vor Benutzung setzen; anschließend unbedingt wieder auf false setzen!*/
    public boolean mandantenabhaengig = false;

    public DbStaaten(DbBundle pDbBundle) {
        super(pDbBundle);
    }

    @Override
    String getCreateString() {

//      @formatter:off
       String createString = "CREATE TABLE " + getSchema() + getTableName()+" ( "
                + "`id` int(11) NOT NULL, "
                + "`code` varchar(10) DEFAULT NULL, "
                + "`nameDE` varchar(100) DEFAULT NULL, "
                + "`nameEN` varchar(100) DEFAULT NULL, "
                + "PRIMARY KEY (`id`), KEY `IDX_CODE` (`code`) " + ") ";
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
        return "tbl_staaten";
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
    EclStaaten decodeErgebnis(ResultSet pErgebnis) {
        EclStaaten lEclReturn = new EclStaaten();

        try {
            lEclReturn.id = pErgebnis.getInt("st.id");
            lEclReturn.code = pErgebnis.getString("st.code");
            lEclReturn.nameDE = pErgebnis.getString("st.nameDE");
            lEclReturn.nameEN = pErgebnis.getString("st.nameEN");
        } catch (Exception e) {
            CaBug.drucke("001");
            System.err.println(" " + e.getMessage());
        }

        return lEclReturn;
    }

    EclStaaten decodeErgebnis1(ResultSet pErgebnis) {
        EclStaaten lEclReturn = new EclStaaten();

        try {
            lEclReturn.id = pErgebnis.getInt("st1.id");
            lEclReturn.code = pErgebnis.getString("st1.code");
            lEclReturn.nameDE = pErgebnis.getString("st1.nameDE");
            lEclReturn.nameEN = pErgebnis.getString("st1.nameEN");
        } catch (Exception e) {
            CaBug.drucke("001");
            System.err.println(" " + e.getMessage());
        }

        return lEclReturn;
    }

    @Override
    void fuellePreparedStatementKomplett(PreparedStatement pPStm, int pOffset, EclStaaten pEcl) {
        int startOffset = pOffset; /*Nur erforderlich zum Überprüfen von Programmierfehlern - Feldanzahl*/

        try {
//          @formatter:off
            
            pPStm.setInt(pOffset, pEcl.id);pOffset++;
            pPStm.setString(pOffset, pEcl.code);pOffset++;
            pPStm.setString(pOffset, pEcl.nameDE);pOffset++;
            pPStm.setString(pOffset, pEcl.nameEN);pOffset++;
           
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
        felder = new String[] { "id", "code", "nameDE", "nameEN" };

        initErfolgt = true;
    }

    /**Einfügen; neue ident wird nicht automatisch vergeben;
     */
    @Override
    public int insert(EclStaaten pEcl) {

        initFelder();
        return insertIntern(felder, pEcl);
    }

    public int readId(int id) {
        int anzInArray = 0;
        PreparedStatement lPStm = null;

        try {
            String lSql = "SELECT st.* from " + getSchema() + getTableName() + " st where " + "st.id=?;";
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

    public int readCode(String code) {
        int anzInArray = 0;
        PreparedStatement lPStm = null;

        try {
            String lSql = "SELECT st.* from " + getSchema() + getTableName() + " st where " + "st.code=?;";
            lPStm = verbindung.prepareStatement(lSql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            lPStm.setString(1, code);

            anzInArray = readIntern(lPStm);
        } catch (Exception e) {
            CaBug.drucke("003");
            System.err.println(" " + e.getMessage());
            return (-1);
        }
        return (anzInArray);
    }

    public int update(EclStaaten pEcl) {
        int anzahl = 0;
        initFelder();

        try {

            String lSql = setzeUpdateBasisStringZusammen(felder) + " WHERE " + "id=? ";

            PreparedStatement lPStm = verbindung.prepareStatement(lSql);
            fuellePreparedStatementKomplett(lPStm, 1, pEcl);
            lPStm.setInt(getAnzFelder() + 1, pEcl.id);

            anzahl = updateIntern(lPStm);
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

    //    EclStaaten decodeErgebnis(ResultSet pErgebnis) {
    //
    //        EclStaaten lStaaten = new EclStaaten();
    //
    //        try {
    //
    //            lStaaten.id = pErgebnis.getInt("st.id");
    //
    //            lStaaten.code = pErgebnis.getString("st.code");
    //            lStaaten.nameDE = pErgebnis.getString("st.nameDE");
    //            lStaaten.nameEN = pErgebnis.getString("st.nameEN");
    //        } catch (Exception e) {
    //            CaBug.drucke("DbStaaten.decodeErgebnis 001");
    //            System.err.println(" " + e.getMessage());
    //        }
    //
    //        return lStaaten;
    //    }

    //    /* Für Joined-Befehle */
    //    EclStaaten decodeErgebnis1(ResultSet pErgebnis) {
    //
    //        EclStaaten lStaaten = new EclStaaten();
    //
    //        try {
    //
    //            lStaaten.id = pErgebnis.getInt("st1.id");
    //
    //            lStaaten.code = pErgebnis.getString("st1.code");
    //            lStaaten.nameDE = pErgebnis.getString("st1.nameDE");
    //            lStaaten.nameEN = pErgebnis.getString("st1.nameEN");
    //        } catch (Exception e) {
    //            CaBug.drucke("DbStaaten.decodeErgebnis1 001");
    //            System.err.println(" " + e.getMessage());
    //        }
    //
    //        return lStaaten;
    //    }

    // /********************* Fuellen Prepared Statement mit allen Feldern,
    // beginnend bei offset.*****************
    // * Kann sowohl für Insert, als auch für update verwendet werden.
    // *
    // * offset= Startposition des ersten Feldes (also z.B. 1)
    // */
    // private int anzfelder=4; /*Anpassen auf Anzahl der Felder pro Datensatz*/
    // private void fuellePreparedStatementKomplett(PreparedStatement pPStm, int
    // pOffset, EclStaaten pStaaten){
    //
    // int startOffset=pOffset; /*Nur erforderlich zum Überprüfen von
    // Programmierfehlern - Feldanzahl*/
    //
    // try {
    // pPStm.setInt(pOffset, pStaaten.id);pOffset++;
    // pPStm.setString(pOffset, pStaaten.code);pOffset++;
    // pPStm.setString(pOffset, pStaaten.nameDE);pOffset++;
    // pPStm.setString(pOffset, pStaaten.nameEN);pOffset++;
    //
    // if (pOffset-startOffset!=anzfelder){
    // /*Nur wg. Überprüfung auf Programmierfehler - alle Felder berücksichtigt?*/
    // CaBug.drucke("DbStaaten.fuellePreparedStatementKomplett 002");
    // }
    //
    // } catch (SQLException e) {
    // CaBug.drucke("DbStaaten.fuellePreparedStatementKomplett 001");
    // e.printStackTrace();
    // }
    //
    // }

    /**pSortierung:
     * 0=wahlfrei
     * 1=Name
     * 
      */
    public int readAll(int pSortierung) {
        int anzInArray = 0;
        PreparedStatement lPStm = null;

        try {
            String lSql = "SELECT st.* from " + getSchema() + getTableName() + " st ";
            switch (pSortierung) {
            case 1:
                lSql = lSql + " ORDER BY nameDE ";
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
