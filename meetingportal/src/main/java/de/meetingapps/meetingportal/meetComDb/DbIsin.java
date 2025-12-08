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
import de.meetingapps.meetingportal.meetComEntities.EclIsin;

public class DbIsin extends  DbRoot<EclIsin> {

    private int logDrucken = 10;

    public DbIsin(DbBundle pDbBundle) {
        super(pDbBundle);
    }

    @Override
    String getCreateString() {

//     @formatter:off
        String createString = "CREATE TABLE " + getSchema() + getTableName()+" ( "
                + "`gaettungId` int(11) NOT NULL, "
                + "`isin` varchar(12) NOT NULL, "
              
                + "PRIMARY KEY (`isin`) " + ") ";
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
        return "tbl_isin";
    }

    @Override
    String getFeldFuerInterneIdent() {
        return "";
    }

    @Override
    int getAnzFelder() {
        return 2;
    }

    @Override
    EclIsin decodeErgebnis(ResultSet pErgebnis) {
        EclIsin lEclReturn = new EclIsin();

        try {
            lEclReturn.gaettungId = pErgebnis.getInt("i.gaettungId");
            lEclReturn.isin = pErgebnis.getString("i.isin");
        } catch (Exception e) {
            CaBug.drucke("001");
            System.err.println(" " + e.getMessage());
        }

        return lEclReturn;
    }

    @Override
    void fuellePreparedStatementKomplett(PreparedStatement pPStm, int pOffset, EclIsin pEcl) {
        int startOffset = pOffset; /*Nur erforderlich zum Überprüfen von Programmierfehlern - Feldanzahl*/

        try {
//          @formatter:off
            
            pPStm.setInt(pOffset, pEcl.gaettungId);pOffset++;
            pPStm.setString(pOffset, pEcl.isin);pOffset++;
           
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
        felder = new String[] { "gaettungId", "isin" };

        initErfolgt = true;
    }

    /**Einfügen; neue ident wird nicht automatisch vergeben;
     */
    @Override
    public int insert(EclIsin pEcl) {

        initFelder();
        return insertIntern(felder, pEcl);
    }

    public int readAll() {
        int anzInArray = 0;
        PreparedStatement lPStm = null;

        try {
            String lSql = "SELECT i.* from " + getSchema() + getTableName() + " i";
            lPStm = verbindung.prepareStatement(lSql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);

            anzInArray = readIntern(lPStm);
        } catch (Exception e) {
            CaBug.drucke("003");
            System.err.println(" " + e.getMessage());
            return (-1);
        }
        return (anzInArray);
    }

    public int delete(String pIsin) {
        int anzahl = 0;
        initFelder();

        try {

            String lSql = "DELETE FROM " + getSchema() + getTableName() + " WHERE isin=?";

            CaBug.druckeLog(lSql, logDrucken, 10);
            PreparedStatement lPStm = verbindung.prepareStatement(lSql);
            lPStm.setString(1, pIsin);

            anzahl = deleteIntern(lPStm);
        } catch (Exception e1) {
            CaBug.drucke("001");
            System.err.println(" " + e1.getMessage());
            return (-1);
        }

        return anzahl;
    }

}
