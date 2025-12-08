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
import java.util.ArrayList;
import java.util.List;

import de.meetingapps.meetingportal.meetComAllg.CaBug;
import de.meetingapps.meetingportal.meetComEntities.EclImportAdresse;

public class DbImportAdresse extends DbRoot<EclImportAdresse> {

    public DbImportAdresse(DbBundle pDbBundle) {
        super(pDbBundle);
    }

    @Override
    String getCreateString() {
//      @formatter:off
        String createString = "CREATE TABLE IF NOT EXISTS " + getSchema() + getTableName()+" ( "
                + "`ident` int(11) AUTO_INCREMENT NOT NULL, "
                + "`adresse` varchar(255) NOT NULL, "
                + "`strasse` varchar(80) NOT NULL, "
                + "`adresszusatz` varchar(80) NOT NULL, "
                + "`postleitzahl` varchar(20) NOT NULL, "
                + "`ort` varchar(80) NOT NULL, "
                + "`staat` varchar(3) NOT NULL, "
                + "PRIMARY KEY (`ident`, `adresse`) " + ") ";
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
        return "tbl_import_adresse";
    }

    @Override
    String getFeldFuerInterneIdent() {
        return "ident";
    }

    @Override
    int getAnzFelder() {
        return 7;
    }

    @Override
    EclImportAdresse decodeErgebnis(ResultSet pErgebnis) {
        EclImportAdresse lEclReturn = new EclImportAdresse();

        try {
            lEclReturn.setIdent(pErgebnis.getInt("ia.ident"));
            lEclReturn.setAdresse(pErgebnis.getString("ia.adresse"));
            lEclReturn.setStrasse(pErgebnis.getString("ia.strasse"));
            lEclReturn.setAdresszusatz(pErgebnis.getString("ia.adresszusatz"));
            lEclReturn.setPostleitzahl(pErgebnis.getString("ia.postleitzahl"));
            lEclReturn.setOrt(pErgebnis.getString("ia.ort"));
            lEclReturn.setStaat(pErgebnis.getString("ia.staat"));
        } catch (Exception e) {
            CaBug.drucke("001");
            System.err.println(" " + e.getMessage());
        }

        return lEclReturn;
    }

    @Override
    void fuellePreparedStatementKomplett(PreparedStatement pPStm, int pOffset, EclImportAdresse pEcl) {
    }

    @Override
    public int insert(EclImportAdresse pEcl) {

        int erg = -1;

        final String sql = "INSERT INTO " + getSchema() + getTableName()
                + " (ident, adresse, strasse, adresszusatz, postleitzahl, ort, staat) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement ps = verbindung.prepareStatement(sql)) {

            ps.setInt(1, 0);
            ps.setString(2, pEcl.getAdresse());
            ps.setString(3, pEcl.getStrasse());
            ps.setString(4, pEcl.getAdresszusatz());
            ps.setString(5, pEcl.getPostleitzahl());
            ps.setString(6, pEcl.getOrt());
            ps.setString(7, pEcl.getStaat());

            erg = ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
            return erg;
        }
        return erg;
    }
    
    public List<EclImportAdresse> readAll() {
        
        List<EclImportAdresse> list = new ArrayList<EclImportAdresse>();

        String sql = "SELECT * FROM " + getSchema() + getTableName() + " ia";

        try (PreparedStatement ps = verbindung.prepareStatement(sql)) {

            try (ResultSet rs = ps.executeQuery()) {

                while (rs.next())
                    list.add(decodeErgebnis(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

}
