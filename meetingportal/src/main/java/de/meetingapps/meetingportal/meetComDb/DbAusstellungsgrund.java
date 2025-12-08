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
import java.sql.Statement;

import de.meetingapps.meetingportal.meetComAllg.CaBug;
import de.meetingapps.meetingportal.meetComEntities.EclAusstellungsgrund;
import de.meetingapps.meetingportal.meetComEntities.EclAusstellungsgrundMandant;

public class DbAusstellungsgrund {

    private Connection verbindung = null;
    //	private DbBasis dbBasis=null;
    private DbBundle dbBundle = null;

    public EclAusstellungsgrund ausstellungsgrundArray[];
    public EclAusstellungsgrundMandant ausstellungsgrundMandantArray[];

    public DbAusstellungsgrund(DbBundle p_bundle) {
        verbindung = p_bundle.dbBasis.verbindung;
        dbBundle = p_bundle;
    }

    public int createTable_allgemein() {
        int rc = 0;
        DbLowLevel lDbLowLevel = new DbLowLevel(dbBundle);
        rc = lDbLowLevel.createTable("CREATE TABLE " + dbBundle.getSchemaAllgemein() + "tbl_ausstellungsgrund ( "
                + "`kuerzel` char(4) NOT NULL, " + "`beschreibung` char(50) DEFAULT NULL, "
                + "PRIMARY KEY (`kuerzel`), " + "UNIQUE KEY `kuerzel` (`kuerzel`) " + ") "

        );
        return rc;
    }

    public int createTable_mandant() {
        int rc = 0;
        DbLowLevel lDbLowLevel = new DbLowLevel(dbBundle);
        rc = lDbLowLevel.createTable("CREATE TABLE " + dbBundle.getSchemaMandant() + "tbl_ausstellungsgrundmandant ( "
                + "`kuerzel` char(4) NOT NULL, " + "`mandant` int(11) NOT NULL, "
                + "`db_version` bigint(20) DEFAULT '0', " + "`fuerAktionaere` int(11) DEFAULT NULL, "
                + "`fuerGaeste` int(11) DEFAULT NULL, " + "`fuerAusstellungOderStorno` int(11) DEFAULT NULL, "
                + "PRIMARY KEY (`kuerzel`,`mandant`) " + ") "

        );
        return rc;
    }

    /**************************deleteAll Löschen aller Datensätze  eines Mandanten****************/
    public int deleteAll_Mandant() {
        int erg = 0;

        try {

            String sql1 = "DELETE FROM " + dbBundle.getSchemaMandant()
                    + "tbl_ausstellungsgrundmandant where mandant=?;";
            PreparedStatement pstm1 = verbindung.prepareStatement(sql1);
            pstm1.setInt(1, dbBundle.clGlobalVar.mandant);

            erg = 0; /*Falls Duplicate Key, dann wird exception geschmissen und erg wohl nicht gefüllt!*/
            erg = pstm1.executeUpdate();

            pstm1.close();
        } catch (Exception e2) {
            CaBug.drucke("DbAppTexte.deleteAll 001");
            System.err.println(" " + e2.getMessage());
            return (erg);
        }

        return 1;
    }

    /**************************setzt aktuelle Mandantennummer bei allen Datensätzen****************/
    public int updateMandant() {
        return dbBundle.dbLowLevel.rawUpdateMandant(dbBundle.getSchemaMandant() + "tbl_ausstellungsgrundmandant");
    }

    /** dekodiert die aktuelle Position aus ergebnis in EclGruppeMandant und gibt dieses zurück*/
    private EclAusstellungsgrundMandant decodeErgebnisAusstellungsgrundMandant(ResultSet ergebnis) {
        EclAusstellungsgrundMandant lAusstellungsgrundMandant = new EclAusstellungsgrundMandant();

        try {

            lAusstellungsgrundMandant.kuerzel = ergebnis.getString("kuerzel");
            lAusstellungsgrundMandant.mandant = ergebnis.getInt("mandant");
            lAusstellungsgrundMandant.db_version = ergebnis.getLong("db_version");

            lAusstellungsgrundMandant.fuerAktionaere = ergebnis.getInt("fuerAktionaere");
            lAusstellungsgrundMandant.fuerGaeste = ergebnis.getInt("fuerGaeste");

        } catch (Exception e) {
            CaBug.drucke("DbAusstellungsgrund.decodeErgebnisAusstellungsgrundMandant 001");
            System.err.println(" " + e.getMessage());
        }

        return lAusstellungsgrundMandant;
    }

    public int readFromArray(EclAusstellungsgrund ausstellungsgrund) {
        int i;
        for (i = 0; i < ausstellungsgrundArray.length; i++) {
            if (ausstellungsgrundArray[i].kuerzel.equals(ausstellungsgrund.kuerzel)) {
                ausstellungsgrund.beschreibung = ausstellungsgrundArray[i].beschreibung;
                return (1);
            }
        }

        return (0);
    }

    /**Selektion: 0=alle, 1=nur die Ausstellungsgründe für Aktionäre, 2=nur die Ausstellungsgründe für Gäste.
     * Liest nur die für den Mandant passenden ein*/
    public void readInArray(int selektion, int pFuerAusstellungOderStorno) {

        PreparedStatement stm = null;
        int anzInArray = 0;
        try {
            if (selektion == 0) {
                String sql = "SELECT * from " + dbBundle.getSchemaMandant() + "tbl_ausstellungsgrundmandant WHERE "
                        + " mandant=? AND fuerAusstellungOderStorno=? ORDER BY kuerzel;";
                stm = verbindung.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            }
            if (selektion == 1) {
                String sql = "SELECT * from " + dbBundle.getSchemaMandant() + "tbl_ausstellungsgrundmandant where "
                        + "fuerAktionaere=1 AND mandant=? AND fuerAusstellungOderStorno=? ORDER BY kuerzel;";
                stm = verbindung.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            }
            if (selektion == 2) {
                String sql = "SELECT * from " + dbBundle.getSchemaMandant() + "tbl_ausstellungsgrundmandant where "
                        + "fuerGaeste=1 AND mandant=? AND fuerAusstellungOderStorno=? ORDER BY kuerzel;";
                stm = verbindung.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            }

            stm.setInt(1, dbBundle.clGlobalVar.mandant);
            stm.setInt(2, pFuerAusstellungOderStorno);
            ResultSet ergebnis = stm.executeQuery();
            ergebnis.last();
            anzInArray = ergebnis.getRow();
            ergebnis.beforeFirst();
            ausstellungsgrundMandantArray = new EclAusstellungsgrundMandant[anzInArray];

            int i = 0;
            while (ergebnis.next() == true) {
                ausstellungsgrundMandantArray[i] = this.decodeErgebnisAusstellungsgrundMandant(ergebnis);
                i++;
            }

            EclAusstellungsgrund[] hAusstellungsgrundArray = new EclAusstellungsgrund[anzInArray];

            for (int i1 = 0; i1 < anzInArray; i1++) {
                leseAusstellungsgrund_ZuKuerzel(ausstellungsgrundMandantArray[i1].kuerzel);
                hAusstellungsgrundArray[i1] = this.ausstellungsgrundArray[0];
            }
            ausstellungsgrundArray = hAusstellungsgrundArray;

            ergebnis.close();
            stm.close();

        } catch (Exception e) {
            CaBug.drucke("DbAusstellungsgrund.readInArray 001");
            System.err.println(" " + e.getMessage());
        }
        return;
    }

    public void readInArrayUebergreifend() {

        try {

            Statement stm = verbindung.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            String sql = "SELECT * from " + dbBundle.getSchemaAllgemein() + "tbl_ausstellungsgrund";
            ResultSet ergebnis = stm.executeQuery(sql);
            ergebnis.last();
            int anz = ergebnis.getRow();
            ergebnis.beforeFirst();

            ausstellungsgrundArray = new EclAusstellungsgrund[anz];

            int i = 0;
            while (ergebnis.next() == true) {

                EclAusstellungsgrund ausstellungsgrund = new EclAusstellungsgrund();
                ausstellungsgrund.kuerzel = ergebnis.getString("kuerzel");
                ausstellungsgrund.beschreibung = ergebnis.getString("beschreibung");

                ausstellungsgrundArray[i] = ausstellungsgrund;
                i++;

            }
            ergebnis.close();
            stm.close();

        } catch (Exception e) {
            System.err.println(" " + e.getMessage());
        }
        return;
    }

    public int leseAusstellungsgrund_ZuKuerzel(String pKuerzel) {
        int anzInArray = 0;
        try {

            String sql = "SELECT * from " + dbBundle.getSchemaAllgemein() + "tbl_ausstellungsgrund where "
                    + "kuerzel=?;";

            PreparedStatement pstm1 = verbindung.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
            pstm1.setString(1, pKuerzel);

            ResultSet ergebnis = pstm1.executeQuery();
            ergebnis.last();
            anzInArray = ergebnis.getRow();
            ergebnis.beforeFirst();

            ausstellungsgrundArray = new EclAusstellungsgrund[anzInArray];

            int i = 0;
            while (ergebnis.next() == true) {
                EclAusstellungsgrund ausstellungsgrund = new EclAusstellungsgrund();
                ausstellungsgrund.kuerzel = ergebnis.getString("kuerzel");
                ausstellungsgrund.beschreibung = ergebnis.getString("beschreibung");
                ausstellungsgrundArray[i] = ausstellungsgrund;
                i++;

            }
            ergebnis.close();
            pstm1.close();

        } catch (Exception e) {
            CaBug.drucke("DbAusstellungsgrund.leseAusstellungsgrund_ZuKuerzel 001");
            System.err.println(" " + e.getMessage());
        }

        return (anzInArray);
    }

}
