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
import de.meetingapps.meetingportal.meetComEntities.EclMeldungenVipKZAusgeblendet;

/*TODO _DBKlassenKonsolidierung: Close nicht vollständig!*/

public class DbMeldungenVipKZAusgeblendet {

    private DbBundle dbBundle = null;
    private Connection verbindung = null;

    public DbMeldungenVipKZAusgeblendet(DbBundle p_bundle) {
        verbindung = p_bundle.dbBasis.verbindung;
        dbBundle = p_bundle;
    }

    public int createTable() {
        int rc = 0;
        DbLowLevel lDbLowLevel = new DbLowLevel(dbBundle);
        rc = lDbLowLevel.createTable("CREATE TABLE " + dbBundle.getSchemaMandant() + "tbl_meldungenvipkzausgeblendet ( "
                + "`mandant` int(11) DEFAULT NULL, " + "`meldungsIdent` int(11) DEFAULT NULL, "
                + "`vipKZkuerzel` char(4) DEFAULT NULL, " + "`benutzernr` int(11) DEFAULT NULL, "
                + "UNIQUE KEY `cons_gesamt` (`mandant`,`meldungsIdent`,`vipKZkuerzel`,`benutzernr`) " + ") "

        );
        return rc;
    }

    /**************************deleteAll Löschen aller Datensätze  eines Mandanten****************/
    public int deleteAll() {
        int erg = 0;

        try {

            String sql1 = "DELETE FROM " + dbBundle.getSchemaMandant()
                    + "tbl_meldungenVipKZAusgeblendet where mandant=?;";
            PreparedStatement pstm1 = verbindung.prepareStatement(sql1);
            pstm1.setInt(1, dbBundle.clGlobalVar.mandant);

            erg = 0; /*Falls Duplicate Key, dann wird exception geschmissen und erg wohl nicht gefüllt!*/
            erg = pstm1.executeUpdate();

        } catch (Exception e2) {
            CaBug.drucke("DbMeldungenVipKZAusgeblendet.deleteAll 001");
            System.err.println(" " + e2.getMessage());
            return (erg);
        }

        return 1;
    }

    /**************************setzt aktuelle Mandantennummer bei allen Datensätzen****************/
    public int updateMandant() {
        return dbBundle.dbLowLevel.rawUpdateMandant(dbBundle.getSchemaMandant() + "tbl_meldungenVipKZAusgeblendet");
    }

    public int insert(EclMeldungenVipKZAusgeblendet meldungVipKZAusgeblendet) {

        int erg = 0;
        try {

            String sql1 = "INSERT INTO " + dbBundle.getSchemaMandant() + "tbl_meldungenVipKZAusgeblendet "
                    + "(mandant, meldungsIdent, vipKZKuerzel, benutzernr)" + "VALUES (?, ?, ?, ?);";

            PreparedStatement pstm1 = verbindung.prepareStatement(sql1);
            pstm1.setInt(1, dbBundle.clGlobalVar.mandant);
            pstm1.setInt(2, meldungVipKZAusgeblendet.meldungsIdent);
            pstm1.setString(3, meldungVipKZAusgeblendet.vipKZKuerzel);
            pstm1.setInt(4, meldungVipKZAusgeblendet.benutzernr);
            erg = pstm1.executeUpdate();
        } catch (Exception e2) {
            System.err.println(" " + e2.getMessage());
        }

        if (erg == 0) {

            return (CaFehler.pfdXyBereitsVorhanden);
        }

        /*Nun noch in aenderungslog speichern*/
        /*		EclAenderungslog aenderungslogEintrag=new EclAenderungslog();
        		aenderungslogEintrag.tabelle=EnAenderungslogTabelle.toEntity(EnAenderungslogTabelle.meldungen);
        		aenderungslogEintrag.ident=Integer.toString(meldungVipKZ.meldungsIdent);
        		aenderungslogEintrag.aktion=EnAenderungslogAktion.toEntity(EnAenderungslogAktion.neuaufnahme);
        		aenderungslogEintrag.feld="VIP-Kennzeichen";
        		aenderungslogEintrag.alt="";
        		aenderungslogEintrag.neu=meldungVipKZ.vipKZKuerzel+" "+meldungVipKZ.parameter;
        		VMcdbBundle.dbAenderungslog.insert(aenderungslogEintrag);*/

        return (1);
    }

    public int delete(EclMeldungenVipKZAusgeblendet meldungVipKZAusgeblendet) {
        meldungVipKZAusgeblendet.mandant = dbBundle.clGlobalVar.mandant;
        try {
            String sql = "DELETE from " + dbBundle.getSchemaMandant() + "tbl_meldungenVipKZAusgeblendet where "
                    + "mandant=? AND meldungsIdent=? AND vipKZKuerzel=? AND benutzernr=?;";

            PreparedStatement pstm1 = verbindung.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
            pstm1.setInt(1, dbBundle.clGlobalVar.mandant);
            pstm1.setInt(2, meldungVipKZAusgeblendet.meldungsIdent);
            pstm1.setString(3, meldungVipKZAusgeblendet.vipKZKuerzel);
            pstm1.setInt(4, meldungVipKZAusgeblendet.benutzernr);

            int ergebnis1 = pstm1.executeUpdate();
            if (ergebnis1 == 0) {
                return (CaFehler.pfNichtMoeglich);
            }
        } catch (Exception e1) {
            System.err.println(" " + e1.getMessage());
        }

        /*Nun noch in aenderungslog speichern*/
        /*		EclAenderungslog aenderungslogEintrag=new EclAenderungslog();
        		aenderungslogEintrag.tabelle=EnAenderungslogTabelle.toEntity(EnAenderungslogTabelle.meldungen);
        		aenderungslogEintrag.ident=Integer.toString(meldungVipKZ.meldungsIdent);
        		aenderungslogEintrag.aktion=EnAenderungslogAktion.toEntity(EnAenderungslogAktion.loeschung);
        		aenderungslogEintrag.feld="VIP-Kennzeichen";
        		aenderungslogEintrag.alt=meldungVipKZ.vipKZKuerzel+" "+meldungVipKZ.parameter;
        		aenderungslogEintrag.neu="";
        		//		System.out.println("Änderungslog 1");
        		VMcdbBundle.dbAenderungslog.insert(aenderungslogEintrag);
        		//		System.out.println("Änderungslog 2");
        */

        return (1);
    }

}
