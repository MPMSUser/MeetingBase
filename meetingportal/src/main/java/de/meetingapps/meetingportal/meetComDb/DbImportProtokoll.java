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

import de.meetingapps.meetingportal.meetComEntities.EclImportProtokoll;

public class DbImportProtokoll extends DbRoot<EclImportProtokoll> {

    public DbImportProtokoll(DbBundle pDbBundle) {
        super(pDbBundle);
    }

    @Override
    String getCreateString() {
        // @formatter:off
        return "CREATE TABLE IF NOT EXISTS " + getSchema() + getTableName() +" ( "
                + "`ident` INT AUTO_INCREMENT NOT NULL, " 
                + "`userLoginIdent` INT NOT NULL, "
                + "`ekVon` INT NOT NULL, "
                + "`ekBis` INT NOT NULL, "
                + "`name` VARCHAR(80) NOT NULL, "
                + "`datei` VARCHAR(32) NOT NULL, "
                + "`erstellt` TIMESTAMP DEFAULT CURRENT_TIMESTAMP, "
                + "PRIMARY KEY (`ident`))";
        // @formatter:on
    }

    @Override
    String getSchema() {
        return dbBundle.getSchemaMandant();
    }

    @Override
    String getTableName() {
        return "tbl_import_protokoll";
    }
    
    public int deleteAll() {
        DbLowLevel lDbLowLevel = new DbLowLevel(dbBundle);

        if (lDbLowLevel.checkTableVorhandenNew(dbBundle.getSchemaMandantOhnePunkt(), getTableName())) {
            return lDbLowLevel.truncMandant("TRUNCATE " + getSchema() + getTableName());
        }
        return 1;
    }

    public int insert(EclImportProtokoll eclInhaberImportProtokoll) {

        int erg = -1;

        final String sql = "INSERT INTO " + getSchema() + getTableName()
                + "(userLoginIdent, ekVon, ekBis, name, datei) VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement ps = verbindung.prepareStatement(sql)) {

            ps.setInt(1, eclInhaberImportProtokoll.getUserLoginIdent());
            ps.setInt(2, eclInhaberImportProtokoll.getEkVon());
            ps.setInt(3, eclInhaberImportProtokoll.getEkBis());
            ps.setString(4, eclInhaberImportProtokoll.getName());
            ps.setString(5, eclInhaberImportProtokoll.getDatei());

            erg = executeUpdate(ps);

        } catch (SQLException e) {
            e.printStackTrace();
            return erg;
        }
        return erg;
    }

    private EclImportProtokoll createData(ResultSet rs) throws SQLException {

        return new EclImportProtokoll(rs.getInt(1), rs.getInt(2), rs.getInt(3), rs.getInt(4), rs.getString(5),
                rs.getString(6), rs.getTimestamp(7));

    }

    public List<EclImportProtokoll> readAll() {

        List<EclImportProtokoll> list = new ArrayList<>();

        final String sql = "SELECT * FROM " + getSchema() + getTableName();

        try (PreparedStatement ps = verbindung.prepareStatement(sql)) {

            try (ResultSet rs = executeQuery(ps)) {

                while (rs.next()) {
                    list.add(createData(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
    
    public EclImportProtokoll readByDatei(String datei) {
        
        final String sql = "SELECT * FROM " + getSchema() + getTableName() + " WHERE datei=?";

        try (PreparedStatement ps = verbindung.prepareStatement(sql)) {
            
            ps.setString(1, datei);

            try (ResultSet rs = executeQuery(ps)) {

                if (rs.next()) {
                    return createData(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
        return null;
    }
    
    @Override
    int getAnzFelder() {
        return 0;
    }

    @Override
    void resetInterneIdent(int pHoechsteIdent) {
    }

    @Override
    String getFeldFuerInterneIdent() {
        return null;
    }

    @Override
    EclImportProtokoll decodeErgebnis(ResultSet pErgebnis) {
        return null;
    }

    @Override
    void fuellePreparedStatementKomplett(PreparedStatement pPStm, int pOffset, EclImportProtokoll pEcl) {
    }

}
