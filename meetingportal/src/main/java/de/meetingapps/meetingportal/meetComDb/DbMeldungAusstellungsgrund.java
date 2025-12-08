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
import de.meetingapps.meetingportal.meetComAllg.CaFehler;
import de.meetingapps.meetingportal.meetComEntities.EclAenderungslog;
import de.meetingapps.meetingportal.meetComEntities.EclAusstellungsgrund;
import de.meetingapps.meetingportal.meetComEntities.EclMeldung;
import de.meetingapps.meetingportal.meetComEntities.EclMeldungAusstellungsgrund;
import de.meetingapps.meetingportal.meetComKonst.EnAenderungslogAktion;
import de.meetingapps.meetingportal.meetComKonst.EnAenderungslogTabelle;

public class DbMeldungAusstellungsgrund {

    private Connection verbindung = null;
    //	private DbBasis dbBasis=null;
    private DbBundle dbBundle = null;

    /*************************Initialisierung***************************/
    /* Verbindung in lokale Daten eintragen*/
    public DbMeldungAusstellungsgrund(DbBundle datenbankbundle) {
        if (datenbankbundle.dbBasis == null) {
            System.err.println("vmcdbBasis nicht initialisiert");
            return;
        }
        // 		dbBasis=datenbankbundle.dbBasis;
        verbindung = datenbankbundle.dbBasis.verbindung;
        dbBundle = datenbankbundle;
    }

    public int createTable() {
        int rc = 0;
        DbLowLevel lDbLowLevel = new DbLowLevel(dbBundle);
        rc = lDbLowLevel.createTable("CREATE TABLE " + dbBundle.getSchemaMandant()
                + "tbl_meldungen_ausstellungsgrund ( " + "`mandant` int(11) DEFAULT NULL, "
                + "`meldungsIdent` int(11) DEFAULT NULL, " + "`ausstellungsgrundKuerzel` char(4) DEFAULT NULL, "
                + "`kommentar` varchar(80) DEFAULT NULL " + ") "

        );
        return rc;
    }

    /**************************deleteAll Löschen aller Datensätze  eines Mandanten****************/
    public int deleteAll() {
        int erg = 0;

        try {

            String sql1 = "DELETE FROM " + dbBundle.getSchemaMandant()
                    + "tbl_meldungen_ausstellungsgrund where mandant=?;";
            PreparedStatement pstm1 = verbindung.prepareStatement(sql1);
            pstm1.setInt(1, dbBundle.clGlobalVar.mandant);

            erg = 0; /*Falls Duplicate Key, dann wird exception geschmissen und erg wohl nicht gefüllt!*/
            erg = pstm1.executeUpdate();

            pstm1.close();

        } catch (Exception e2) {
            CaBug.drucke("DbMeldungAusstellungsgrund.deleteAll 001");
            System.err.println(" " + e2.getMessage());
            return (erg);
        }

        return 1;
    }

    /**************************setzt aktuelle Mandantennummer bei allen Datensätzen****************/
    public int updateMandant() {
        return dbBundle.dbLowLevel.rawUpdateMandant(dbBundle.getSchemaMandant() + "tbl_meldungen_ausstellungsgrund");
    }

    public int insert(EclMeldungAusstellungsgrund meldungAusstellungsgrund) {

        int erg = 0;
        try {

            Statement stm1 = verbindung.createStatement();
            String sql1 = "INSERT INTO " + dbBundle.getSchemaMandant() + "tbl_meldungen_ausstellungsgrund "
                    + "(mandant, meldungsIdent, ausstellungsGrundKuerzel, kommentar)" + "VALUES ("
                    + Integer.toString(dbBundle.clGlobalVar.mandant) + ", "
                    + Integer.toString(meldungAusstellungsgrund.meldungsIdent) + ", " + "'"
                    + meldungAusstellungsgrund.ausstellungsGrundKuerzel + "', " + "'"
                    + meldungAusstellungsgrund.kommentar + "');";
            System.out.println("Bearbeiten SQL");
            System.out.println(sql1);
            erg = stm1.executeUpdate(sql1);
            stm1.close();
        } catch (Exception e2) {
            System.err.println(" " + e2.getMessage());
        }

        if (erg == 0) {/*da kein Unique-Key: darf nicht auftreten!*/

            return (0);
        }

        /*Nun noch in aenderungslog speichern*/
        EclAenderungslog aenderungslogEintrag = new EclAenderungslog();
        aenderungslogEintrag.tabelle = EnAenderungslogTabelle.toEntity(EnAenderungslogTabelle.meldungen);
        aenderungslogEintrag.ident = Integer.toString(meldungAusstellungsgrund.meldungsIdent);
        aenderungslogEintrag.aktion = EnAenderungslogAktion.toEntity(EnAenderungslogAktion.neuaufnahme);
        aenderungslogEintrag.feld = "Ausstellungsgrund";
        aenderungslogEintrag.alt = "";
        aenderungslogEintrag.neu = meldungAusstellungsgrund.ausstellungsGrundKuerzel + " "
                + meldungAusstellungsgrund.kommentar;
        //		System.out.println("Änderungslog 1");
        dbBundle.dbAenderungslog.insert(aenderungslogEintrag);
        //		System.out.println("Änderungslog 2");

        return (1);
    }

    public EclMeldungAusstellungsgrund[] leseZuMeldung(EclMeldung meldung) {
        int anzInArray = 0;
        EclMeldungAusstellungsgrund meldungAusstellungsgrundArray[] = null;
        try {

            Statement stm = verbindung.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            String sql = "SELECT * from " + dbBundle.getSchemaMandant() + "tbl_meldungen_ausstellungsgrund where "
                    + "mandant=" + Integer.toString(dbBundle.clGlobalVar.mandant) + " AND " + "meldungsIdent="
                    + Integer.toString(meldung.meldungsIdent) + ";";

            ResultSet ergebnis = stm.executeQuery(sql);
            ergebnis.last();
            anzInArray = ergebnis.getRow();
            ergebnis.beforeFirst();

            meldungAusstellungsgrundArray = new EclMeldungAusstellungsgrund[anzInArray];

            int i = 0;
            EclAusstellungsgrund ausstellungsgrund = new EclAusstellungsgrund();
            while (ergebnis.next() == true) {
                EclMeldungAusstellungsgrund meldungAusstellungsgrund = new EclMeldungAusstellungsgrund();

                meldungAusstellungsgrund.mandant = ergebnis.getInt("mandant");
                meldungAusstellungsgrund.meldungsIdent = ergebnis.getInt("meldungsIdent");
                meldungAusstellungsgrund.ausstellungsGrundKuerzel = ergebnis.getString("ausstellungsGrundKuerzel");
                meldungAusstellungsgrund.kommentar = ergebnis.getString("kommentar");

                ausstellungsgrund.kuerzel = meldungAusstellungsgrund.ausstellungsGrundKuerzel;
                int erg = dbBundle.dbAusstellungsgrund.readFromArray(ausstellungsgrund);
                if (erg > 0) {
                    meldungAusstellungsgrund.beschreibung = ausstellungsgrund.beschreibung;

                } else {
                    meldungAusstellungsgrund.beschreibung = "";
                }

                meldungAusstellungsgrundArray[i] = meldungAusstellungsgrund;
                i++;

            }
            ergebnis.close();
            stm.close();

        } catch (Exception e) {
            System.err.println("Fehlerbehandlung in DbMeldungAusstellungsgrund.leseZuMeldung fehlt noch");
            System.err.println(" " + e.getMessage());
        }

        return (meldungAusstellungsgrundArray);

    }

    public int delete(EclMeldungAusstellungsgrund meldungAusstellungsgrund) {
        meldungAusstellungsgrund.mandant = dbBundle.clGlobalVar.mandant;
        try {
            Statement stm = verbindung.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            String sql = "DELETE from " + dbBundle.getSchemaMandant() + "tbl_meldungen_ausstellungsgrund where "
                    + "mandant=" + Integer.toString(dbBundle.clGlobalVar.mandant) + " AND " + "meldungsIdent="
                    + Integer.toString(meldungAusstellungsgrund.meldungsIdent) + " AND " + "ausstellungsGrundKuerzel='"
                    + meldungAusstellungsgrund.ausstellungsGrundKuerzel + "' AND " + "kommentar='"
                    + meldungAusstellungsgrund.kommentar + "';";

            int ergebnis1 = stm.executeUpdate(sql);
            stm.close();
            if (ergebnis1 == 0) {
                return (CaFehler.pfXyWurdeVonAnderemBenutzerVeraendert);
            }
        } catch (Exception e1) {
            System.err.println(" " + e1.getMessage());
        }

        /*Nun noch in aenderungslog speichern*/
        EclAenderungslog aenderungslogEintrag = new EclAenderungslog();
        aenderungslogEintrag.tabelle = EnAenderungslogTabelle.toEntity(EnAenderungslogTabelle.meldungen);
        aenderungslogEintrag.ident = Integer.toString(meldungAusstellungsgrund.meldungsIdent);
        aenderungslogEintrag.aktion = EnAenderungslogAktion.toEntity(EnAenderungslogAktion.loeschung);
        aenderungslogEintrag.feld = "Ausstellungsgrund";
        aenderungslogEintrag.alt = meldungAusstellungsgrund.ausstellungsGrundKuerzel + " "
                + meldungAusstellungsgrund.kommentar;
        aenderungslogEintrag.neu = "";
        //		System.out.println("Änderungslog 1");
        dbBundle.dbAenderungslog.insert(aenderungslogEintrag);
        //		System.out.println("Änderungslog 2");

        return (1);
    }

}
