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
import java.util.ArrayList;
import java.util.List;

import de.meetingapps.meetingportal.meetComAllg.CaBug;
import de.meetingapps.meetingportal.meetComEntities.EclImportProfil;

public class DbImportProfil {

    private Connection verbindung = null;
    private DbBundle dbBundle = null;
    final int anzFelder = EclImportProfil.anzFelder;

    /************************* Initialisierung ***************************/
    public DbImportProfil(DbBundle pDbBundle) {
        /* Verbindung in lokale Daten eintragen */
        if (pDbBundle == null) {
            CaBug.drucke("DbImportProfil.init 001 - dbBundle nicht initialisiert");
            return;
        }
        if (pDbBundle.dbBasis == null) {
            CaBug.drucke("DbImportProfil.init 002 - dbBasis nicht initialisiert");
            return;
        }

        verbindung = pDbBundle.dbBasis.verbindung;
        dbBundle = pDbBundle;
    }

    public int createTable() {
        int rc = 0;
        DbLowLevel lDbLowLevel = new DbLowLevel(dbBundle);
//      @formatter:off
        String createString = "CREATE TABLE IF NOT EXISTS " + dbBundle.getSchemaAllgemein() + "tbl_importprofil ( "
                + "`ident` int(11) AUTO_INCREMENT, " 
                + "`name` varchar(80) NOT NULL, "
                + "`klasse` varchar(80), "
                + "`zuletztGeandert` timestamp DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP, ";
        for (int i = 0; i < anzFelder; i++) {
            createString = createString + "`dateiFeld" + i + "` varchar(60) NULL, "
                    + "`datenbankFeld" + i + "` varchar(60) NULL, ";
        }
        createString = createString + "PRIMARY KEY (`ident`))";
//      @formatter:on
        rc = lDbLowLevel.createTable(createString);
        return rc;
    }

    public int updateTable() {
        int rc = 0;
        DbLowLevel lDbLowLevel = new DbLowLevel(dbBundle);
        if (lDbLowLevel.pruefeSpalteVorhanden(dbBundle.getSchemaAllgemein() + "tbl_importprofil", "klasse")) {
            // @formatter:off
            String createString = "ALTER TABLE " + dbBundle.getSchemaAllgemein() + "tbl_importprofil ADD klasse varchar(80) AFTER name";
            for (int i = 0; i < 50; i++) 
                createString += ", MODIFY dateiFeld" + i + " varchar(60) NULL, MODIFY datenbankFeld" + i + " varchar(60) NULL";
            
            for(int i = 50; i < anzFelder; i++)
                createString += ", ADD dateiFeld" + i + " varchar(60) NULL, ADD datenbankFeld" + i + " varchar(60) NULL";

            rc = lDbLowLevel.rawOperation(createString);
            // @formatter:on
        } else {
            rc = 1;
        }
        return rc;
    }

    public int insert(EclImportProfil profil) {

        int erg = 0;

//      @formatter:off
        String sql = "INSERT INTO " + dbBundle.getSchemaAllgemein() + "tbl_importprofil ("
                + "name, klasse ";
        for (int i = 0; i < anzFelder; i++) 
            sql = sql + ", dateiFeld" + i + ", datenbankFeld" + i;
        sql = sql + ") VALUES ( ?, ?";
        for (int i = 0; i < anzFelder; i++) 
            sql = sql + ", ?, ?";
        sql = sql + ")";
//      @formatter:on

        try (PreparedStatement ps = verbindung.prepareStatement(sql)) {

            ps.setString(++erg, profil.getName());
            ps.setString(++erg, profil.getKlasse());

            for (int i = 0; i < anzFelder; i++) {
                ps.setString(++erg, profil.getDateiFeld()[i]);
                ps.setString(++erg, profil.getDatenbankFeld()[i]);
            }

            erg = ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
            return erg;
        }
        return erg;
    }

    public int update(EclImportProfil profil) {
        int erg = 0;

//      @formatter:off
        String sql = "UPDATE " + dbBundle.getSchemaAllgemein() + "tbl_importprofil SET name=?, klasse=?";
        for (int i = 0; i < anzFelder; i++) 
            sql = sql + ", dateiFeld" + i + "=?, datenbankFeld" + i + "=?";
        sql = sql + "WHERE ident=?";
//      @formatter:on

        try (PreparedStatement ps = verbindung.prepareStatement(sql)) {

            ps.setString(++erg, profil.getName());

            for (int i = 0; i < anzFelder; i++) {
                ps.setString(++erg, profil.getDateiFeld()[i]);
                ps.setString(++erg, profil.getDatenbankFeld()[i]);
            }
            ps.setInt(++erg, profil.getIdent());

            erg = ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
            return erg;
        }
        return erg;
    }

    public int delete(EclImportProfil profil) {

        int erg = 0;

        String sql = "DELETE FROM " + dbBundle.getSchemaMandant() + "tbl_importprofil WHERE ident=?";

        try (PreparedStatement ps = verbindung.prepareStatement(sql)) {

            ps.setInt(1, profil.getIdent());
            erg = ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return erg;
    }

    public List<EclImportProfil> readAll() {

        ArrayList<EclImportProfil> list = new ArrayList<>();

        String sql = "SELECT * FROM " + dbBundle.getSchemaAllgemein() + "tbl_importprofil";

        try (PreparedStatement ps = verbindung.prepareStatement(sql)) {

            try (ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {
                    String[] dateiFeld = new String[anzFelder];
                    String[] datenbankFeld = new String[anzFelder];

                    int x = 4;

                    for (int i = 0; i < anzFelder; i++) {

                        dateiFeld[i] = rs.getString(++x);

                        datenbankFeld[i] = rs.getString(++x);
                    }
                    list.add(new EclImportProfil(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getTimestamp(4),
                            dateiFeld, datenbankFeld));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

}
