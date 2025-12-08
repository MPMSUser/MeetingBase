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
import de.meetingapps.meetingportal.meetComAllg.CaFehler;
import de.meetingapps.meetingportal.meetComEntities.EclAenderungslog;
import de.meetingapps.meetingportal.meetComEntities.EclMeldung;
import de.meetingapps.meetingportal.meetComEntities.EclMeldungVipKZ;
import de.meetingapps.meetingportal.meetComEntities.EclVipKZ;
import de.meetingapps.meetingportal.meetComKonst.EnAenderungslogAktion;
import de.meetingapps.meetingportal.meetComKonst.EnAenderungslogTabelle;

/*TODO _DBKlassenKonsolidierung: Close nicht vollständig!*/

public class DbMeldungVipKZ {

    private Connection verbindung = null;
    //	private DbBasis dbBasis=null;
    private DbBundle dbBundle = null;

    /*************************Initialisierung***************************/
    /* Verbindung in lokale Daten eintragen*/
    public DbMeldungVipKZ(DbBundle datenbankbundle) {
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
        rc = lDbLowLevel.createTable("CREATE TABLE " + dbBundle.getSchemaMandant() + "tbl_meldungenvipkz ( "
                + "`mandant` int(11) DEFAULT NULL, " + "`meldungsIdent` int(11) DEFAULT NULL, "
                + "`vipKZKuerzel` char(4) DEFAULT NULL, " + "`parameter` varchar(80) DEFAULT NULL, "
                + "UNIQUE KEY `cons_gesamt` (`mandant`,`meldungsIdent`,`vipKZKuerzel`,`parameter`) " + ") "

        );
        return rc;
    }

    /**************************deleteAll Löschen aller Datensätze  eines Mandanten****************/
    public int deleteAll() {
        int erg = 0;

        try {

            String sql1 = "DELETE FROM " + dbBundle.getSchemaMandant() + "tbl_meldungenVipKZ where mandant=?;";
            PreparedStatement pstm1 = verbindung.prepareStatement(sql1);
            pstm1.setInt(1, dbBundle.clGlobalVar.mandant);

            erg = 0; /*Falls Duplicate Key, dann wird exception geschmissen und erg wohl nicht gefüllt!*/
            erg = pstm1.executeUpdate();

        } catch (Exception e2) {
            CaBug.drucke("DbMeldungVipKZ.deleteAll 001");
            System.err.println(" " + e2.getMessage());
            return (erg);
        }

        return 1;
    }

    /**************************setzt aktuelle Mandantennummer bei allen Datensätzen****************/
    public int updateMandant() {
        return dbBundle.dbLowLevel.rawUpdateMandant(dbBundle.getSchemaMandant() + "tbl_meldungenVipKZ");
    }

    public int insert(EclMeldungVipKZ meldungVipKZ) {

        int erg = 0;
        try {

            String sql1 = "INSERT INTO " + dbBundle.getSchemaMandant() + "tbl_meldungenVipKZ "
                    + "(mandant, meldungsIdent, vipKZKuerzel, parameter)" + "VALUES (?, ?, ?, ?);";

            PreparedStatement pstm1 = verbindung.prepareStatement(sql1);
            pstm1.setInt(1, dbBundle.clGlobalVar.mandant);
            pstm1.setInt(2, meldungVipKZ.meldungsIdent);
            pstm1.setString(3, meldungVipKZ.vipKZKuerzel);
            pstm1.setString(4, meldungVipKZ.parameter);
            erg = pstm1.executeUpdate();
        } catch (Exception e2) {
            System.err.println(" " + e2.getMessage());
        }

        if (erg == 0) {

            return (CaFehler.pfdXyBereitsVorhanden);
        }

        /*Nun noch in aenderungslog speichern*/
        EclAenderungslog aenderungslogEintrag = new EclAenderungslog();
        aenderungslogEintrag.tabelle = EnAenderungslogTabelle.toEntity(EnAenderungslogTabelle.meldungen);
        aenderungslogEintrag.ident = Integer.toString(meldungVipKZ.meldungsIdent);
        aenderungslogEintrag.aktion = EnAenderungslogAktion.toEntity(EnAenderungslogAktion.neuaufnahme);
        aenderungslogEintrag.feld = "VIP-Kennzeichen";
        aenderungslogEintrag.alt = "";
        aenderungslogEintrag.neu = meldungVipKZ.vipKZKuerzel + " " + meldungVipKZ.parameter;
        dbBundle.dbAenderungslog.insert(aenderungslogEintrag);

        return (1);
    }

    public EclMeldungVipKZ[] leseZuMeldung(EclMeldung meldung) {
        int anzInArray = 0;
        EclMeldungVipKZ meldungVipKZArray[] = null;
        try {

            String sql = "SELECT * from " + dbBundle.getSchemaMandant() + "tbl_meldungenVipKZ where "
                    + "mandant=? AND meldungsIdent=?;";

            PreparedStatement pstm1 = verbindung.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
            pstm1.setInt(1, dbBundle.clGlobalVar.mandant);
            pstm1.setInt(2, meldung.meldungsIdent);
            ResultSet ergebnis = pstm1.executeQuery();
            ergebnis.last();
            anzInArray = ergebnis.getRow();
            ergebnis.beforeFirst();

            meldungVipKZArray = new EclMeldungVipKZ[anzInArray];

            int i = 0;
            EclVipKZ vipKZ = new EclVipKZ();
            while (ergebnis.next() == true) {
                EclMeldungVipKZ meldungVipKZ = new EclMeldungVipKZ();

                meldungVipKZ.mandant = ergebnis.getInt("mandant");
                meldungVipKZ.meldungsIdent = ergebnis.getInt("meldungsIdent");
                meldungVipKZ.vipKZKuerzel = ergebnis.getString("vipKZKuerzel");
                meldungVipKZ.parameter = ergebnis.getString("parameter");

                vipKZ.kuerzel = meldungVipKZ.vipKZKuerzel;
                int erg = dbBundle.dbVipKZ.readFromArray(vipKZ);
                if (erg > 0) {
                    meldungVipKZ.beschreibung = vipKZ.beschreibung;

                } else {
                    meldungVipKZ.beschreibung = "";
                }

                meldungVipKZArray[i] = meldungVipKZ;
                i++;

            }

        } catch (Exception e) {
            System.err.println("Fehlerbehandlung in dbMeldungVipKZ.lesezumeldung fehlt noch");
            System.err.println(" " + e.getMessage());
        }

        return (meldungVipKZArray);

    }

    public int delete(EclMeldungVipKZ meldungVipKZ) {
        meldungVipKZ.mandant = dbBundle.clGlobalVar.mandant;
        try {
            String sql = "DELETE from " + dbBundle.getSchemaMandant() + "tbl_meldungenVipKZ where "
                    + "mandant=? AND meldungsIdent=? AND vipKZKuerzel=? AND parameter=?;";

            PreparedStatement pstm1 = verbindung.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
            pstm1.setInt(1, dbBundle.clGlobalVar.mandant);
            pstm1.setInt(2, meldungVipKZ.meldungsIdent);
            pstm1.setString(3, meldungVipKZ.vipKZKuerzel);
            pstm1.setString(4, meldungVipKZ.parameter);

            int ergebnis1 = pstm1.executeUpdate();
            if (ergebnis1 == 0) {
                return (CaFehler.pfXyWurdeVonAnderemBenutzerVeraendert);
            }
        } catch (Exception e1) {
            System.err.println(" " + e1.getMessage());
        }

        /*Nun noch in aenderungslog speichern*/
        EclAenderungslog aenderungslogEintrag = new EclAenderungslog();
        aenderungslogEintrag.tabelle = EnAenderungslogTabelle.toEntity(EnAenderungslogTabelle.meldungen);
        aenderungslogEintrag.ident = Integer.toString(meldungVipKZ.meldungsIdent);
        aenderungslogEintrag.aktion = EnAenderungslogAktion.toEntity(EnAenderungslogAktion.loeschung);
        aenderungslogEintrag.feld = "VIP-Kennzeichen";
        aenderungslogEintrag.alt = meldungVipKZ.vipKZKuerzel + " " + meldungVipKZ.parameter;
        aenderungslogEintrag.neu = "";
        //		System.out.println("Änderungslog 1");
        dbBundle.dbAenderungslog.insert(aenderungslogEintrag);
        //		System.out.println("Änderungslog 2");

        return (1);
    }

}
