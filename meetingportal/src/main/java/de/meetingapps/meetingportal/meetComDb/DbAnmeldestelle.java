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

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;

import de.meetingapps.meetingportal.meetComAllg.CaBug;
import de.meetingapps.meetingportal.meetComEntities.EclAnmeldestelle;

public class DbAnmeldestelle  extends DbRootExecute {

    private Connection verbindung = null;
    private DbBundle dbBundle = null;

    /************************* Initialisierung ***************************/
    public DbAnmeldestelle(DbBundle pDbBundle) {
        /* Verbindung in lokale Daten eintragen */
        if (pDbBundle == null) {
            CaBug.drucke("DbAnmeldestelle.init 001 - dbBundle nicht initialisiert");
            return;
        }
        if (pDbBundle.dbBasis == null) {
            CaBug.drucke("DbAnmeldestelle.init 002 - dbBasis nicht initialisiert");
            return;
        }
        verbindung = pDbBundle.dbBasis.verbindung;
        dbBundle = pDbBundle;
    }

    /************ Neuanlegen Table ******************************/
    public int createTable() {
        int rc = 0;
        DbLowLevel lDbLowLevel = new DbLowLevel(dbBundle);
//		@formatter:off
		rc = lDbLowLevel.createTable(
				"CREATE TABLE IF NOT EXISTS " + dbBundle.getSchemaAllgemein() + "tbl_anmeldestelle ( "
				+ "`ident` int(11) AUTO_INCREMENT NOT NULL" 
				+ "`name` varchar(120) NOT NULL, " 
				+ "`strasse` varchar(80) NOT NULL, " 
				+ "`plz` varchar(45) NOT NULL, " 
				+ "`ort` varchar(60) NOT NULL, " 
				+ "`telefon` varchar(45) NOT NULL, " 
				+ "`fax` varchar(45) NOT NULL, " 
				+ "`email` varchar(45) NOT NULL, "
				+ "`notiz` varchar(255) NOT NULL, "
				+ "`zuletztGeandert` timestamp DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP, " 
				+ "PRIMARY KEY (`ident`) " 
				+ ")");
//		@formatter:on
        System.out.println("DbAnmeldestelle create table rc=" + rc);
        return rc;
    }

    public int insert(EclAnmeldestelle eclAnmeldestelle) {

        int erg = 0;

//		@formatter:off
		String sql = "INSERT INTO " + dbBundle.getSchemaAllgemein() + "tbl_anmeldestelle ("
				+ "name, strasse, plz, ort, land, telefon, fax, email, notiz) "
				+ "VALUES ("
				+ "?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
//		@formatter:on

        try (PreparedStatement ps = verbindung.prepareStatement(sql)) {

            ps.setString(1, eclAnmeldestelle.getName());
            ps.setString(2, eclAnmeldestelle.getStrasse());
            ps.setString(3, eclAnmeldestelle.getPlz());
            ps.setString(4, eclAnmeldestelle.getOrt());
            ps.setString(5, eclAnmeldestelle.getLand());
            ps.setString(6, eclAnmeldestelle.getTelefon());
            ps.setString(7, eclAnmeldestelle.getFax());
            ps.setString(8, eclAnmeldestelle.getEmail());
            ps.setString(9, eclAnmeldestelle.getNotiz());

            erg = executeUpdate(ps);

        } catch (SQLException e) {
            e.printStackTrace();
            return erg;
        }
        return erg;
    }

    public int update(EclAnmeldestelle eclAnmeldestelle) {

        int erg = 0;

//		@formatter:off
		String sql = "UPDATE " + dbBundle.getSchemaAllgemein() + "tbl_anmeldestelle SET "
				+ "name=?, strasse=?, plz=?, ort=?, land=?, telefon=?, fax=?, email=?, notiz=? "
				+ "WHERE "
				+ "ident=?";
//		@formatter:on

        try (PreparedStatement ps = verbindung.prepareStatement(sql)) {

            ps.setString(1, eclAnmeldestelle.getName());
            ps.setString(2, eclAnmeldestelle.getStrasse());
            ps.setString(3, eclAnmeldestelle.getPlz());
            ps.setString(4, eclAnmeldestelle.getOrt());
            ps.setString(5, eclAnmeldestelle.getLand());
            ps.setString(6, eclAnmeldestelle.getTelefon());
            ps.setString(7, eclAnmeldestelle.getFax());
            ps.setString(8, eclAnmeldestelle.getEmail());
            ps.setString(9, eclAnmeldestelle.getNotiz());
            ps.setInt(10, eclAnmeldestelle.getIdent());

            erg = executeUpdate(ps);

        } catch (SQLException e) {
            e.printStackTrace();
            return erg;
        }
        return erg;
    }

    public int delete(EclAnmeldestelle eclAnmeldestelle) {

        int erg = 0;

        String sql = "DELETE FROM " + dbBundle.getSchemaMandant() + "tbl_anmeldestelle WHERE ident=?";

        try (PreparedStatement ps = verbindung.prepareStatement(sql)) {

            ps.setInt(1, eclAnmeldestelle.getIdent());
            erg = executeUpdate(ps);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return erg;
    }

    public ArrayList<EclAnmeldestelle> readAll() {

        ArrayList<EclAnmeldestelle> list = new ArrayList<EclAnmeldestelle>();

        String sql = "SELECT * FROM " + dbBundle.getSchemaAllgemein() + "tbl_anmeldestelle";

        try (PreparedStatement ps = verbindung.prepareStatement(sql)) {
            try (ResultSet rs = executeQuery(ps)) {
                while (rs.next())
                    list.add(new EclAnmeldestelle(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4),
                            rs.getString(5), rs.getString(6), rs.getString(7), rs.getString(8), rs.getString(9),
                            rs.getString(10), rs.getTimestamp(11)));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public Timestamp readTimestampByIdent(EclAnmeldestelle eclAnmeldestelle) {

        Timestamp time = null;

        final String sql = "SELECT zuletztGeandert FROM " + dbBundle.getSchemaAllgemein()
                + "tbl_anmeldestelle WHERE ident=?";

        try (PreparedStatement ps = verbindung.prepareStatement(sql)) {
            try (ResultSet rs = executeQuery(ps)) {
                if (rs.next())
                    time = rs.getTimestamp(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return time;
    }
}
