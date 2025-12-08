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

import de.meetingapps.meetingportal.meetComAllg.CaBug;
import de.meetingapps.meetingportal.meetComAllg.CaDatumZeit;
import de.meetingapps.meetingportal.meetComEntities.EclAenderungslog;

public class DbAenderungslog  extends DbRootExecute {

    private Connection verbindung = null;
    //	private DbBasis dbBasis=null;
    private DbBundle dbBundle = null;

    /*************************Initialisierung***************************/
    /* Verbindung in lokale Daten eintragen*/
    public DbAenderungslog(DbBundle datenbankbundle) {
        if (datenbankbundle.dbBasis == null) {
            System.err.println("Aenderungslog: vmcdbBasis nicht initialisiert");
            return;
        }
        // 		dbBasis=datenbankbundle.dbBasis;
        verbindung = datenbankbundle.dbBasis.verbindung;
        dbBundle = datenbankbundle;
    }

    public int createTable() {
        int rc = 0;
        DbLowLevel lDbLowLevel = new DbLowLevel(dbBundle);
        rc = lDbLowLevel.createTable("CREATE TABLE " + dbBundle.getSchemaMandant() + "tbl_aenderungslog ( "
                + "`tabelle` int(11) DEFAULT NULL, " + "`mandant` int(11) DEFAULT NULL, "
                + "`ident` varchar(50) DEFAULT NULL, " + "`aktion` int(11) DEFAULT NULL, "
                + "`feld` varchar(50) DEFAULT NULL, " + "`alt` varchar(1000) DEFAULT NULL, "
                + "`neu` varchar(1000) DEFAULT NULL, " + "`geaendertDatumUhrzeit` char(19) DEFAULT NULL " + ")  ");
        return rc;
    }

    /**************************deleteAll Löschen aller Datensätze  eines Mandanten****************/
    public int deleteAll() {
        int erg = 0;

        try {

            String sql1 = "DELETE FROM " + dbBundle.getSchemaMandant() + "tbl_aenderungslog where mandant=?;";
            PreparedStatement pstm1 = verbindung.prepareStatement(sql1);
            pstm1.setInt(1, dbBundle.clGlobalVar.mandant);

            erg = 0; /*Falls Duplicate Key, dann wird exception geschmissen und erg wohl nicht gefüllt!*/
            erg = executeUpdate(pstm1);
            pstm1.close();
        } catch (Exception e2) {
            CaBug.drucke("DbAenderungslog.deleteAll 001");
            System.err.println(" " + e2.getMessage());
            return (erg);
        }

        return 1;
    }

    /**************************setzt aktuelle Mandantennummer bei allen Datensätzen****************/
    public int updateMandant() {
        return dbBundle.dbLowLevel.rawUpdateMandant(dbBundle.getSchemaMandant() + "tbl_aenderungslog");
    }

    /** Speichert einen Eintrag in das aenderungslog. Alle Felder müssen gefüllt sein, mit Ausnahme von geaendertDatumUhrzeit und mandant -
     * dieses wird automatisch in dieser Funktion gefüllt*/
    public int insert(EclAenderungslog aenderungslogEintrag) {

        int erg = 0;
        aenderungslogEintrag.geaendertDatumUhrzeit = CaDatumZeit.DatumZeitStringFuerDatenbank();

        try {

            String sql1 = "INSERT INTO " + dbBundle.getSchemaMandant() + "tbl_aenderungslog "
                    + "(tabelle, mandant, ident, " + "aktion, feld, " + "alt, neu, " + "geaendertDatumUhrzeit) "
                    + "VALUES (?, ?, ?, ?, ?, ?, ?, ?);";

            PreparedStatement pstm1 = verbindung.prepareStatement(sql1, ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
            pstm1.setInt(1, aenderungslogEintrag.tabelle);
            pstm1.setInt(2, dbBundle.clGlobalVar.mandant);
            pstm1.setString(3, aenderungslogEintrag.ident);
            pstm1.setInt(4, aenderungslogEintrag.aktion);
            pstm1.setString(5, aenderungslogEintrag.feld);
            pstm1.setString(6, aenderungslogEintrag.alt);
            pstm1.setString(7, aenderungslogEintrag.neu);
            pstm1.setString(8, aenderungslogEintrag.geaendertDatumUhrzeit);

            erg = executeUpdate(pstm1);
            pstm1.close();
        } catch (Exception e2) {
            System.err.println(" " + e2.getMessage());
        }

        if (erg == 0) {/*da kein Unique-Key: darf nicht auftreten!*/

            return (0);
        }

        return (1);
    }

    /** liest aus aenderungslog gemäß den in logselektion übergebenen Kriterien, sortiert nach Datum. Such-Kriterien müssen sein: tabelle, ident*/
    public EclAenderungslog[] leseAenderungsLog(EclAenderungslog logselektion) {

        int anzInArray = 0;
        EclAenderungslog aenderungslogArray[] = null;
        try {

            String sql = "SELECT * from " + dbBundle.getSchemaMandant() + "tbl_aenderungslog where "
                    + "mandant=? AND tabelle=? AND ident=? ORDER BY geaendertDatumUhrzeit;";

            PreparedStatement pstm1 = verbindung.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
            pstm1.setInt(1, dbBundle.clGlobalVar.mandant);
            pstm1.setInt(2, logselektion.tabelle);
            pstm1.setString(3, logselektion.ident);

            ResultSet ergebnis = executeQuery(pstm1);
            ergebnis.last();
            anzInArray = ergebnis.getRow();
            ergebnis.beforeFirst();

            aenderungslogArray = new EclAenderungslog[anzInArray];

            int i = 0;
            EclAenderungslog aenderungslogeintrag = new EclAenderungslog();
            while (ergebnis.next() == true) {
                aenderungslogeintrag = new EclAenderungslog();

                aenderungslogeintrag.tabelle = ergebnis.getInt("tabelle");
                aenderungslogeintrag.mandant = ergebnis.getInt("mandant");
                aenderungslogeintrag.ident = ergebnis.getString("ident");
                aenderungslogeintrag.aktion = ergebnis.getInt("aktion");
                aenderungslogeintrag.feld = ergebnis.getString("feld");
                aenderungslogeintrag.alt = ergebnis.getString("alt");
                aenderungslogeintrag.neu = ergebnis.getString("neu");
                aenderungslogeintrag.geaendertDatumUhrzeit = ergebnis.getString("geaendertDatumUhrzeit");

                aenderungslogArray[i] = aenderungslogeintrag;
                i++;

            }
            ergebnis.close();
            pstm1.close();

        } catch (Exception e) {
            System.err.println(" " + e.getMessage());
        }

        return (aenderungslogArray);

    }

}
