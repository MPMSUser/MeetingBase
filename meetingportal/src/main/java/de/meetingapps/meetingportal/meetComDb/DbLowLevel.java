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
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import de.meetingapps.meetingportal.meetComAllg.CaBug;

public class DbLowLevel {

    private int logDrucken=10;
    
    private Connection verbindung = null;
    // private DbBasis dbBasis=null;
    private DbBundle dbBundle = null;

    /************************* Initialisierung ***************************/
    public DbLowLevel(DbBundle pDbBundle) {
        /* Verbindung in lokale Daten eintragen */
        if (pDbBundle == null) {
            CaBug.drucke("DbLowLevel.init 001 - dbBundle nicht initialisiert");
            return;
        }
        if (pDbBundle.dbBasis == null) {
            CaBug.drucke("DbLowLevel.init 002 - dbBasis nicht initialisiert");
            return;
        }

        // dbBasis=pDbBundle.dbBasis;
        verbindung = pDbBundle.dbBasis.verbindung;
        dbBundle = pDbBundle;
    }

    // Auslesen aller Kunden Datenbanken
    public List<String> liefereMandantenSchemas() {

        List<String> schemas = new ArrayList<>();

        try {

            DatabaseMetaData metaData = verbindung.getMetaData();

            ResultSet rs = metaData.getCatalogs();

            while (rs.next()) {
                String schema = rs.getString(1);
                // Pattern sucht nur nach möglichen db_mcDDDjDDDDww kompinationen und wird
                // direkt auf ident komprimiert
                final Pattern pattern = Pattern.compile("(?<=db_mc(0{0,2}))[1-9]{0,3}(?=j\\d{4}\\w{0,2})");
                final Matcher matcher = pattern.matcher(schema);

                if (matcher.find()) {
                    schemas.add(matcher.group(0));
                }
            }

            rs.close();

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }

        // return schemas.isEmpty() ? null : schemas;
        return schemas;
    }

    /**
     * @param schema Durchsuchte Datenbank (ohne Punkt) Bsp. db_mc111j2023ap
     * @param tbl Gesuchter Table
     * @return vorhanden = true
     */
    public boolean checkTableVorhandenNew(String schema, String tbl) {
        Boolean exist = false;

        try {
            DatabaseMetaData metaData = verbindung.getMetaData();

            ResultSet rs = metaData.getTables(schema, null, tbl, null);

            if (rs.next())
                exist = true;
            
            rs.close();

        } catch (SQLException e) {
            e.printStackTrace();
            return exist;
        }
        return exist;
    }

    public List<String> liefereMandantenSchemasKomplett() {

        List<String> schemas = new ArrayList<>();

        try {

            DatabaseMetaData metaData = verbindung.getMetaData();

            ResultSet rs = metaData.getCatalogs();

            while (rs.next()) {
                String schema = rs.getString(1);
                // Liefert alle Datenbanken die mit db_mc beginnen
                final Pattern pattern = Pattern.compile("^(db_mc).+");
                final Matcher matcher = pattern.matcher(schema);

                if (matcher.find()) {
                    schemas.add(matcher.group(0));
                }
            }

            rs.close();

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }

        // return schemas.isEmpty() ? null : schemas;
        return schemas;
    }

    /**
     * @param datenbank Datenbank welche ausgelesen werden soll (Punkt am Ende wird
     *                  entfernt wenn vorhanden)
     * @param vorlage   String welchen die gesuchten Tables enthalten sollen
     * @return List<String> Alle Tables der Datenbank welche mit der Vorlage
     *         übereinstimmen
     */
    public List<String> liefereMandantenTables(String datenbank, String vorlage) {

        datenbank = datenbank.replaceAll(".$", "");
        List<String> tables = new ArrayList<>();

        try {
            final DatabaseMetaData metaData = verbindung.getMetaData();
            
            try (ResultSet rs = metaData.getTables(datenbank, null, "%", null)) {
                if (rs!=null) {
                    while (rs.next())
                        tables.add(rs.getString("TABLE_NAME"));

                    tables = tables.stream().filter(e -> e.contains(vorlage.toLowerCase())).collect(Collectors.toList());
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tables;
    }

    /******* Checken, ob table überhaupt vorhanden ist ***************************/
    public boolean checkTableVorhanden(String pCreateStatement) {
        PreparedStatement lPStm = null;

        try {
            String lSql = pCreateStatement;
            lPStm = verbindung.prepareStatement(lSql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);

            ResultSet lErgebnis = lPStm.executeQuery();
            lErgebnis.last();
            lErgebnis.beforeFirst();
            lErgebnis.close();
            lPStm.close();

        } catch (SQLException e) {
            /*
             * Wird in diesem Fall erwartet - nämlich wenn Datenbank so alt ist, dass noch
             * kein Table vorhanden ist.
             */
            CaBug.drucke("DbLowLevel.holeTableVersion 001 " + pCreateStatement);
            System.err.println(" " + e.getMessage());
            return (false);
        } catch (Exception e) {
            CaBug.drucke("DbLowLevel.holeTableVersion 003 " + pCreateStatement);
            System.err.println(" " + e.getMessage());
            return (false);
        }
        return (true);

    }

    public int createSchema(String pCreateStatement) {

        try {
            String lSql = pCreateStatement + " DEFAULT CHARACTER SET utf8;";

            PreparedStatement lPStm = verbindung.prepareStatement(lSql);

            lPStm.executeUpdate();
            lPStm.close();
        } catch (Exception e1) {
            CaBug.drucke("001 " + pCreateStatement);
            System.err.println(" " + e1.getMessage());
            return (-1);
        }
        return 1;
    }

    public int createTable(String pCreateStatement) {

        try {
            String lSql = pCreateStatement + " ENGINE=InnoDB DEFAULT CHARSET=utf8;";

            PreparedStatement lPStm = verbindung.prepareStatement(lSql);

            lPStm.executeUpdate();
            lPStm.close();
        } catch (Exception e1) {
            CaBug.drucke("DbLowLevel.createTable 001 " + pCreateStatement);
            System.err.println(" " + e1.getMessage());
            return (-1);
        }

        return (1);
    }

    public int dropTable(String pDopStatement) {
        CaBug.druckeLog("pDopStatement="+pDopStatement, logDrucken, 10);
        try {
            String lSql = pDopStatement ;

            PreparedStatement lPStm = verbindung.prepareStatement(lSql);

            lPStm.executeUpdate();
            lPStm.close();
        } catch (Exception e1) {
            CaBug.drucke("001 " + pDopStatement);
            System.err.println(" " + e1.getMessage());
            return (-1);
        }
   
        return (1);
    }
    
    public int deleteMandant(String pStatement) {

        try {
            String lSql = pStatement;
            PreparedStatement lPStm = verbindung.prepareStatement(lSql);
            lPStm.setInt(1, dbBundle.clGlobalVar.mandant);
            lPStm.executeUpdate();
            lPStm.close();
        } catch (Exception e1) {
            CaBug.drucke("DbLowLevel.deleteMandant 001 " + pStatement);
            System.err.println(" " + e1.getMessage());
            return (-1);
        }

        return (1);
    }

    public int truncMandant(String pStatement) {

        try {
            String lSql = pStatement;
            PreparedStatement lPStm = verbindung.prepareStatement(lSql);
            lPStm.executeUpdate();
            lPStm.close();
        } catch (Exception e1) {
            CaBug.drucke("DbLowLevel.deleteMandant 001 " + pStatement);
            System.err.println(" " + e1.getMessage());
            return (-1);
        }

        return (1);
    }

    public int deleteAlle(String pStatement) {

        try {
            String lSql = pStatement;
            PreparedStatement lPStm = verbindung.prepareStatement(lSql);
            lPStm.executeUpdate();
            lPStm.close();
        } catch (Exception e1) {
            CaBug.drucke("DbLowLevel.deleteAlle 001 " + pStatement);
            System.err.println(" " + e1.getMessage());
            return (-1);
        }

        return (1);
    }

    public int liefereHoechsteIdent(String pStatement) {
        int lMax;
        lMax = 0;

        PreparedStatement pstm1 = null;
        try {
            int anzInArray;
            pstm1 = verbindung.prepareStatement(pStatement, ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
            pstm1.setInt(1, dbBundle.clGlobalVar.mandant);

            ResultSet ergebnis = pstm1.executeQuery();
            ergebnis.last();
            anzInArray = ergebnis.getRow();
            ergebnis.beforeFirst();

            if (anzInArray > 0) {
                ergebnis.next();
                lMax = ergebnis.getInt(1);
            }
            ergebnis.close();
            pstm1.close();

        } catch (Exception e) {
            CaBug.drucke("001 " + pStatement);
            System.err.println(" " + e.getMessage());
            return -1;
        }
        return lMax;
    }

    public int liefereHoechsteIdentOhneMandant(String pStatement) {
        int lMax;
        lMax = 0;

        PreparedStatement pstm1 = null;
        try {
            int anzInArray;
            pstm1 = verbindung.prepareStatement(pStatement, ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
            ResultSet ergebnis = pstm1.executeQuery();
            ergebnis.last();
            anzInArray = ergebnis.getRow();
            ergebnis.beforeFirst();

            if (anzInArray > 0) {
                ergebnis.next();
                lMax = ergebnis.getInt(1);
            }
            ergebnis.close();
            pstm1.close();

        } catch (Exception e) {
            CaBug.drucke("001 " + pStatement);
            System.err.println(" " + e.getMessage());
            return -1;
        }
        return lMax;
    }

    public int liefereHoechsteIdentString(String pStatement) {
        int lMax;
        lMax = 0;

        PreparedStatement pstm1 = null;
        try {
            int anzInArray;
            pstm1 = verbindung.prepareStatement(pStatement, ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
            pstm1.setInt(1, dbBundle.clGlobalVar.mandant);

            ResultSet ergebnis = pstm1.executeQuery();
            ergebnis.last();
            anzInArray = ergebnis.getRow();
            ergebnis.beforeFirst();

            if (anzInArray > 0) {
                ergebnis.next();
                String hString = ergebnis.getString(1);
                if (hString != null) {
                    hString = hString.trim();
                    if (!hString.isEmpty()) {
                        lMax = Integer.parseInt(hString);
                    }
                }
            }
            ergebnis.close();
            pstm1.close();

        } catch (Exception e) {
            CaBug.drucke("DbLowLevel.liefereHoechsteIdent 001 " + pStatement);
            System.err.println(" " + e.getMessage());
            return -1;
        }
        return lMax;
    }

    public int rawOperation(String pStatement) {
        // System.out.println("rawOperation "+pStatement);

        try {
            String lSql = pStatement;

            PreparedStatement lPStm = verbindung.prepareStatement(lSql);

            lPStm.executeUpdate();
            lPStm.close();
        } catch (Exception e1) {
            CaBug.drucke("001" + pStatement);
            System.err.println(" " + e1.getMessage());
            return (-1);
        }

        return (1);
    }

    public int rawRead(String pStatement) {
        PreparedStatement pstm1 = null;
        int anzInArray;
        try {
            pstm1 = verbindung.prepareStatement(pStatement, ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);

            ResultSet ergebnis = pstm1.executeQuery();
            ergebnis.last();
            anzInArray = ergebnis.getRow();
            ergebnis.beforeFirst();
            ergebnis.close();
            pstm1.close();

        } catch (Exception e) {
            CaBug.drucke("001 " + pStatement);
            System.err.println(" " + e.getMessage());
            return -1;
        }
        return anzInArray;
    }

    public int rawUpdateMandant(String table) {
        // System.out.println("rawOperation "+pStatement);
        String pStatement = "UPDATE " + table + " SET mandant=?";
        try {
            String lSql = pStatement;

            PreparedStatement lPStm = verbindung.prepareStatement(lSql);
            lPStm.setInt(1, dbBundle.clGlobalVar.mandant);

            lPStm.executeUpdate();
            lPStm.close();
        } catch (Exception e1) {
            CaBug.drucke("DbLowLevel.rawUpdateMandant 001 " + pStatement);
            System.err.println(" " + e1.getMessage());
            return (-1);
        }

        return (1);
    }

    public int copyTable(String pQuelle, String pZiel) {

        /* Drop zieltable */
        try {
            String lSql = "DROP TABLE IF EXISTS " + pZiel;

            PreparedStatement lPStm = verbindung.prepareStatement(lSql);

            lPStm.executeUpdate();
            lPStm.close();
        } catch (Exception e1) {
            CaBug.drucke("001");
            System.err.println(" " + e1.getMessage());
            return (-1);
        }

        /* zieltable neu anlegen */
        try {
            String lSql = "CREATE TABLE " + pZiel + " LIKE " + pQuelle;

            PreparedStatement lPStm = verbindung.prepareStatement(lSql);

            lPStm.executeUpdate();
            lPStm.close();
        } catch (Exception e1) {
            CaBug.drucke("002");
            System.err.println(" " + e1.getMessage());
            return (-1);
        }

        /* Kopieren */
        try {
            String lSql = "INSERT INTO " + pZiel + " SELECT * FROM " + pQuelle;

            PreparedStatement lPStm = verbindung.prepareStatement(lSql);

            lPStm.executeUpdate();
            lPStm.close();
        } catch (Exception e1) {
            CaBug.drucke("003");
            System.err.println(" " + e1.getMessage());
            return (-1);
        }

        return 1;
    }
    
    public boolean pruefeSpalteVorhanden(String table, String column) {
        final String lSql = "SHOW COLUMNS FROM " + table + " WHERE Field = '" + column + "'";

        try (PreparedStatement pstmt = verbindung.prepareStatement(lSql)) {
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next())
                    return false;
            }
        } catch (SQLException e) {
            CaBug.drucke("003");
            System.err.println(" " + e.getMessage());
        }
        return true;
    }
    
    public boolean pruefePrimaryVorhanden(String table, String column) {
        final String lSql = "SHOW KEYS FROM " + table + " WHERE Key_name = 'PRIMARY' and Column_name = '" + column + "'";
        
        try (PreparedStatement pstmt = verbindung.prepareStatement(lSql)) {
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next())
                    return false;
            }
        } catch (SQLException e) {
            CaBug.drucke("003");
            System.err.println(" " + e.getMessage());
        }
        return true;
    }

    public boolean pruefeIndexVorhanden(String table, String column) {
        final String lSql = "SHOW KEYS FROM " + table + " WHERE Key_name = 'IDX_Nummer' and Column_name = '" + column + "'";
        
        try (PreparedStatement pstmt = verbindung.prepareStatement(lSql)) {
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next())
                    return false;
            }
        } catch (SQLException e) {
            CaBug.drucke("003");
            System.err.println(" " + e.getMessage());
        }
        return true;
    }

}
