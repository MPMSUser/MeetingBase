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

import de.meetingapps.meetingportal.meetComEntities.EclMessageId;

public class DbMessageId extends DbRoot<EclMessageId> {

    public DbMessageId(DbBundle pDbBundle) {
        super(pDbBundle);
    }

    @Override
    String getCreateString() {
//      @formatter:off
        String createString = "CREATE TABLE IF NOT EXISTS " + getSchema() + getTableName()+" ( "
                + "`messageId` VARCHAR(35) NOT NULL, "
                + "PRIMARY KEY (`messageId`) " + ") ";
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
        return "tbl_message_id";
    }

    @Override
    String getFeldFuerInterneIdent() {
        return null;
    }

    @Override
    int getAnzFelder() {
        return 1;
    }

    @Override
    EclMessageId decodeErgebnis(ResultSet pErgebnis) {
        try {
            return new EclMessageId(pErgebnis.getString(1));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    void fuellePreparedStatementKomplett(PreparedStatement pPStm, int pOffset, EclMessageId pEcl) {
    }

    @Override
    public int insert(EclMessageId value) {
        return 0;
    }

    public String insert(int length) {

        EclMessageId value = new EclMessageId(length);

        final String sql = "INSERT INTO " + getSchema() + getTableName() + "(messageId) VALUES (?)";

        try (PreparedStatement ps = verbindung.prepareStatement(sql)) {

            ps.setString(1, value.getMessageId());

            if (ps.executeUpdate() == 1) {
                return value.getMessageId();
            }
        } catch (SQLException e) {
            return insert(length);
        }
        return null;
    }

    public int readById(EclMessageId value) {

        final String sql = "SELECT * FROM " + getSchema() + getTableName() + " WHERE messageId=?";

        try (PreparedStatement ps = verbindung.prepareStatement(sql)) {

            ps.setString(1, value.getMessageId());

            try (ResultSet rs = executeQuery(ps)) {
                if (rs.next()) {
                    return 1;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
