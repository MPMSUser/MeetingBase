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
import java.util.LinkedList;
import java.util.List;

import de.meetingapps.meetingportal.meetComAllg.CaBug;
import de.meetingapps.meetingportal.meetComAllg.CaFehler;
import de.meetingapps.meetingportal.meetComEntities.EclAenderungslog;
import de.meetingapps.meetingportal.meetComEntities.EclMeldung;
import de.meetingapps.meetingportal.meetComEntities.EclMeldungenMeldungen;
import de.meetingapps.meetingportal.meetComKonst.EnAenderungslogAktion;
import de.meetingapps.meetingportal.meetComKonst.EnAenderungslogTabelle;
import de.meetingapps.meetingportal.meetComKonst.EnMeldungenMeldungen;

/*TODO _DBKlassenKonsolidierung: Close nicht vollständig!*/

public class DbMeldungenMeldungen {

    private Connection verbindung = null;
    //	private DbBasis dbBasis=null;
    private DbBundle dbBundle = null;

    /*************************Initialisierung***************************/
    /* Verbindung in lokale Daten eintragen*/
    public DbMeldungenMeldungen(DbBundle datenbankbundle) {
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
        rc = lDbLowLevel.createTable("CREATE TABLE " + dbBundle.getSchemaMandant() + "tbl_meldungen_meldungen ( "
                + "`mandant` int(11) DEFAULT NULL, " + "`vonMeldungsIdent` int(11) DEFAULT NULL, "
                + "`zuMeldungsIdent` int(11) DEFAULT NULL, " + "`verwendung` int(11) DEFAULT NULL, "
                + "UNIQUE KEY `cons_gesamt` (`mandant`,`vonMeldungsIdent`,`zuMeldungsIdent`,`verwendung`) " + ") ");
        return rc;
    }

    /**************************deleteAll Löschen aller Datensätze  eines Mandanten****************/
    public int deleteAll() {
        int erg = 0;

        try {

            String sql1 = "DELETE FROM " + dbBundle.getSchemaMandant() + "tbl_meldungen_meldungen where mandant=?;";
            PreparedStatement pstm1 = verbindung.prepareStatement(sql1);
            pstm1.setInt(1, dbBundle.clGlobalVar.mandant);

            erg = 0; /*Falls Duplicate Key, dann wird exception geschmissen und erg wohl nicht gefüllt!*/
            erg = pstm1.executeUpdate();

        } catch (Exception e2) {
            CaBug.drucke("DbMeldungenMeldungen.deleteAll 001");
            System.err.println(" " + e2.getMessage());
            return (erg);
        }

        return 1;
    }

    /**************************setzt aktuelle Mandantennummer bei allen Datensätzen****************/
    public int updateMandant() {
        return dbBundle.dbLowLevel.rawUpdateMandant(dbBundle.getSchemaMandant() + "tbl_meldungen_meldungen");
    }

    public int insert(EclMeldungenMeldungen meldungenMeldungen) {

        int erg = 0;
        try {

            Statement stm1 = verbindung.createStatement();
            String sql1 = "INSERT INTO " + dbBundle.getSchemaMandant() + "tbl_meldungen_meldungen "
                    + "(mandant, vonMeldungsIdent, zuMeldungsIdent, verwendung)" + "VALUES ("
                    + Integer.toString(dbBundle.clGlobalVar.mandant) + ", "
                    + Integer.toString(meldungenMeldungen.vonMeldungsIdent) + ", "
                    + Integer.toString(meldungenMeldungen.zuMeldungsIdent) + ", "
                    + Integer.toString(meldungenMeldungen.verwendung) + ");";
            //			System.out.println("Bearbeiten SQL");
            //			System.out.println(sql1);
            erg = stm1.executeUpdate(sql1);
            //			System.out.println("Ergebnis Insert "+erg);
        } catch (Exception e2) {
            System.err.println(" " + e2.getMessage());
        }

        if (erg == 0) {/*Alles Unique-Key: => Doppelter Eintrag!*/

            return (CaFehler.pfdXyBereitsVorhanden);
        }

        /* Nun noch in aenderungslog speichern - bei vonMeldungsIdent*/
        EclAenderungslog aenderungslogEintrag = new EclAenderungslog();
        aenderungslogEintrag.tabelle = EnAenderungslogTabelle.toEntity(EnAenderungslogTabelle.meldungen);
        aenderungslogEintrag.ident = Integer.toString(meldungenMeldungen.vonMeldungsIdent);
        aenderungslogEintrag.aktion = EnAenderungslogAktion.toEntity(EnAenderungslogAktion.neuaufnahme);
        aenderungslogEintrag.feld = "Zuordnung zu";
        aenderungslogEintrag.alt = "";
        aenderungslogEintrag.neu = Integer.toString(meldungenMeldungen.zuMeldungsIdent) + " "
                + EnMeldungenMeldungen.fromEntity(meldungenMeldungen.verwendung).toString();
        //		System.out.println("Änderungslog 1");
        dbBundle.dbAenderungslog.insert(aenderungslogEintrag);
        //		System.out.println("Änderungslog 2");

        /* Nun noch in aenderungslog speichern - bei zuMeldungsIdent*/
        aenderungslogEintrag = new EclAenderungslog();
        aenderungslogEintrag.tabelle = EnAenderungslogTabelle.toEntity(EnAenderungslogTabelle.meldungen);
        aenderungslogEintrag.ident = Integer.toString(meldungenMeldungen.zuMeldungsIdent);
        aenderungslogEintrag.aktion = EnAenderungslogAktion.toEntity(EnAenderungslogAktion.neuaufnahme);
        aenderungslogEintrag.feld = "Zuordnung von";
        aenderungslogEintrag.alt = "";
        aenderungslogEintrag.neu = Integer.toString(meldungenMeldungen.vonMeldungsIdent) + " "
                + EnMeldungenMeldungen.fromEntity(meldungenMeldungen.verwendung).toString();
        //		System.out.println("Änderungslog 1");
        dbBundle.dbAenderungslog.insert(aenderungslogEintrag);
        //		System.out.println("Änderungslog 2");

        return (1);
    }

    /** Liest alle meldungen ein, die dem Parameter meldung zugeordnet sind*/
    public EclMeldungenMeldungen[] leseVonMeldungen(EclMeldung meldung) {
        int anzInArray = 0;
        EclMeldungenMeldungen meldungenMeldungenArray[] = null;
        try {

            Statement stm = verbindung.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            String sql = "SELECT * from " + dbBundle.getSchemaMandant() + "tbl_meldungen_meldungen where " + "mandant="
                    + Integer.toString(dbBundle.clGlobalVar.mandant) + " AND " + "zuMeldungsIdent="
                    + Integer.toString(meldung.meldungsIdent) + ";";
            //					System.out.println("sql vonMeldungen="+sql);

            ResultSet ergebnis = stm.executeQuery(sql);
            ergebnis.last();
            anzInArray = ergebnis.getRow();
            ergebnis.beforeFirst();
            //System.out.println("anzInArray"+anzInArray);
            meldungenMeldungenArray = new EclMeldungenMeldungen[anzInArray];

            int i = 0;
            EclMeldung vonMeldung = new EclMeldung();
            while (ergebnis.next() == true) {
                //				System.out.println("While i="+i);
                EclMeldungenMeldungen meldungenMeldungen = new EclMeldungenMeldungen();

                meldungenMeldungen.mandant = ergebnis.getInt("mandant");
                meldungenMeldungen.vonMeldungsIdent = ergebnis.getInt("vonMeldungsIdent");
                meldungenMeldungen.zuMeldungsIdent = ergebnis.getInt("zuMeldungsIdent");
                meldungenMeldungen.verwendung = ergebnis.getInt("verwendung");

                vonMeldung.meldungsIdent = meldungenMeldungen.vonMeldungsIdent;
                //				System.out.println("Vor readraw");
                int erg = dbBundle.dbMeldungen.leseZuMeldungsIdent(vonMeldung);
                //				System.out.println("Nach Readraw");
                if (erg > 0) {
                    meldungenMeldungen.name = dbBundle.dbMeldungen.meldungenArray[0].name;
                    meldungenMeldungen.vorname = dbBundle.dbMeldungen.meldungenArray[0].vorname;
                    meldungenMeldungen.ort = dbBundle.dbMeldungen.meldungenArray[0].ort;
                }

                int erg1 = dbBundle.dbZutrittskarten.readZuMeldungsIdentGast(vonMeldung.meldungsIdent);
                if (erg1 > 0) {
                    meldungenMeldungen.zutrittsIdent = dbBundle.dbZutrittskarten.ergebnisPosition(0).zutrittsIdent;
                }

                meldungenMeldungenArray[i] = meldungenMeldungen;
                i++;
                //				System.out.println("While i="+i+"Ende");
            }

        } catch (Exception e) {
            System.err.println(" " + e.getMessage());
        }

        return (meldungenMeldungenArray);

    }

    
    /** Liest alle meldungen ein, die dem Parameter meldung zugeordnet sind*/
    public List<EclMeldungenMeldungen> leseVonMeldungenZuVerwendung(int pVonMeldung, int pVerwendung) {
        List<EclMeldungenMeldungen> meldungenMeldungenList = new LinkedList<EclMeldungenMeldungen>();
        try {

            Statement stm = verbindung.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            String sql = "SELECT * from " + dbBundle.getSchemaMandant() + "tbl_meldungen_meldungen where " + "vonMeldungsIdent="
                    + Integer.toString(pVonMeldung) + " AND (verwendung="+Integer.toString(pVerwendung)+" OR verwendung="
                    + Integer.toString(pVerwendung*(-1))
                    +");";
            //                  System.out.println("sql vonMeldungen="+sql);

            ResultSet ergebnis = stm.executeQuery(sql);
            ergebnis.beforeFirst();

            EclMeldung vonMeldung = new EclMeldung();
            while (ergebnis.next() == true) {
                //              System.out.println("While i="+i);
                EclMeldungenMeldungen meldungenMeldungen = new EclMeldungenMeldungen();

                meldungenMeldungen.mandant = ergebnis.getInt("mandant");
                meldungenMeldungen.vonMeldungsIdent = ergebnis.getInt("vonMeldungsIdent");
                meldungenMeldungen.zuMeldungsIdent = ergebnis.getInt("zuMeldungsIdent");
                meldungenMeldungen.verwendung = ergebnis.getInt("verwendung");

                vonMeldung.meldungsIdent = meldungenMeldungen.vonMeldungsIdent;
                //              System.out.println("Vor readraw");
                int erg = dbBundle.dbMeldungen.leseZuMeldungsIdent(meldungenMeldungen.zuMeldungsIdent);
                //              System.out.println("Nach Readraw");
                if (erg > 0) {
                    meldungenMeldungen.name = dbBundle.dbMeldungen.meldungenArray[0].name;
                    meldungenMeldungen.vorname = dbBundle.dbMeldungen.meldungenArray[0].vorname;
                    meldungenMeldungen.ort = dbBundle.dbMeldungen.meldungenArray[0].ort;
                    meldungenMeldungen.zutrittsIdent=dbBundle.dbMeldungen.meldungenArray[0].zutrittsIdent;
                    meldungenMeldungen.mailAdresse=dbBundle.dbMeldungen.meldungenArray[0].mailadresse;
                }


                meldungenMeldungenList.add(meldungenMeldungen);
                //              System.out.println("While i="+i+"Ende");
            }

        } catch (Exception e) {
            CaBug.drucke("001");
            System.err.println(" " + e.getMessage());
        }

        return (meldungenMeldungenList);

    }

    
    
    /** Liest alle meldungen ein, auf die die Parameter meldung verweist*/
    public EclMeldungenMeldungen[] leseZuMeldungen(EclMeldung meldung) {
        int anzInArray = 0;
        EclMeldungenMeldungen meldungenMeldungenArray[] = null;
        //		System.out.println("leseZuMeldungen");
        try {

            Statement stm = verbindung.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            String sql = "SELECT * from " + dbBundle.getSchemaMandant() + "tbl_meldungen_meldungen where " + "mandant="
                    + Integer.toString(dbBundle.clGlobalVar.mandant) + " AND " + "vonMeldungsIdent="
                    + Integer.toString(meldung.meldungsIdent) + ";";
            //					System.out.println("sql="+sql);
            ResultSet ergebnis = stm.executeQuery(sql);
            ergebnis.last();
            anzInArray = ergebnis.getRow();
            ergebnis.beforeFirst();

            meldungenMeldungenArray = new EclMeldungenMeldungen[anzInArray];

            int i = 0;
            //			System.out.println("Ergebnis interpretieren");
            EclMeldung zuMeldung = new EclMeldung();
            while (ergebnis.next() == true) {
                EclMeldungenMeldungen meldungenMeldungen = new EclMeldungenMeldungen();

                meldungenMeldungen.mandant = ergebnis.getInt("mandant");
                meldungenMeldungen.vonMeldungsIdent = ergebnis.getInt("vonMeldungsIdent");
                meldungenMeldungen.zuMeldungsIdent = ergebnis.getInt("zuMeldungsIdent");
                meldungenMeldungen.verwendung = ergebnis.getInt("verwendung");

                zuMeldung.meldungsIdent = meldungenMeldungen.zuMeldungsIdent;
                int erg = dbBundle.dbMeldungen.leseZuMeldungsIdent(zuMeldung);
                if (erg > 0) {
                    meldungenMeldungen.name = dbBundle.dbMeldungen.meldungenArray[0].name;
                    meldungenMeldungen.vorname = dbBundle.dbMeldungen.meldungenArray[0].vorname;
                    meldungenMeldungen.ort = dbBundle.dbMeldungen.meldungenArray[0].ort;
                }

                int erg1 = dbBundle.dbZutrittskarten.readZuMeldungsIdentGast(zuMeldung.meldungsIdent);
                if (erg1 > 0) {
                    meldungenMeldungen.zutrittsIdent = dbBundle.dbZutrittskarten.ergebnisPosition(0).zutrittsIdent;
                }

                meldungenMeldungenArray[i] = meldungenMeldungen;
                i++;

            }

        } catch (Exception e) {
            System.err.println(" " + e.getMessage());
        }

        //		System.out.println("ReadzuMeldungen fertig");
        return (meldungenMeldungenArray);

    }

    public int delete(EclMeldungenMeldungen meldungenMeldungen) {
        meldungenMeldungen.mandant = dbBundle.clGlobalVar.mandant;
        try {
            Statement stm = verbindung.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            String sql = "DELETE from " + dbBundle.getSchemaMandant() + "tbl_meldungen_meldungen where " + "mandant="
                    + Integer.toString(dbBundle.clGlobalVar.mandant) + " AND " + "vonMeldungsIdent="
                    + Integer.toString(meldungenMeldungen.vonMeldungsIdent) + " AND " + "zuMeldungsIdent="
                    + Integer.toString(meldungenMeldungen.zuMeldungsIdent) + " AND " + "verwendung="
                    + Integer.toString(meldungenMeldungen.verwendung) 
                    +";";

            int ergebnis1 = stm.executeUpdate(sql);
            if (ergebnis1 == 0) {
                return (CaFehler.pfXyWurdeVonAnderemBenutzerVeraendert);
            }
        } catch (Exception e1) {
            System.err.println(" " + e1.getMessage());
        }

        /* Nun noch in aenderungslog speichern - bei vonMeldungsIdent*/
        EclAenderungslog aenderungslogEintrag = new EclAenderungslog();
        aenderungslogEintrag.tabelle = EnAenderungslogTabelle.toEntity(EnAenderungslogTabelle.meldungen);
        aenderungslogEintrag.ident = Integer.toString(meldungenMeldungen.vonMeldungsIdent);
        aenderungslogEintrag.aktion = EnAenderungslogAktion.toEntity(EnAenderungslogAktion.loeschung);
        aenderungslogEintrag.feld = "Zuordnung zu";
        aenderungslogEintrag.neu = "";
        aenderungslogEintrag.alt = Integer.toString(meldungenMeldungen.zuMeldungsIdent) + " "
                + EnMeldungenMeldungen.fromEntity(meldungenMeldungen.verwendung).toString();
        //		System.out.println("Änderungslog 1");
        dbBundle.dbAenderungslog.insert(aenderungslogEintrag);
        //		System.out.println("Änderungslog 2");

        /* Nun noch in aenderungslog speichern - bei zuMeldungsIdent*/
        aenderungslogEintrag = new EclAenderungslog();
        aenderungslogEintrag.tabelle = EnAenderungslogTabelle.toEntity(EnAenderungslogTabelle.meldungen);
        aenderungslogEintrag.ident = Integer.toString(meldungenMeldungen.zuMeldungsIdent);
        aenderungslogEintrag.aktion = EnAenderungslogAktion.toEntity(EnAenderungslogAktion.loeschung);
        aenderungslogEintrag.feld = "Zuordnung von";
        aenderungslogEintrag.neu = "";
        aenderungslogEintrag.alt = Integer.toString(meldungenMeldungen.vonMeldungsIdent) + " "
                + EnMeldungenMeldungen.fromEntity(meldungenMeldungen.verwendung).toString();
        //		System.out.println("Änderungslog 1");
        dbBundle.dbAenderungslog.insert(aenderungslogEintrag);
        //		System.out.println("Änderungslog 2");

        return (1);
    }

    
    public int storniere(EclMeldungenMeldungen meldungenMeldungen) {
        meldungenMeldungen.mandant = dbBundle.clGlobalVar.mandant;
        try {
            Statement stm = verbindung.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            String sql = "UPDATE " + dbBundle.getSchemaMandant() + "tbl_meldungen_meldungen "
                    + "SET verwendung=verwendung*(-1) "
                    + "where " + "mandant="
                    + Integer.toString(dbBundle.clGlobalVar.mandant) + " AND " + "vonMeldungsIdent="
                    + Integer.toString(meldungenMeldungen.vonMeldungsIdent) + " AND " + "zuMeldungsIdent="
                    + Integer.toString(meldungenMeldungen.zuMeldungsIdent) + " AND " + "verwendung="
                    + Integer.toString(meldungenMeldungen.verwendung) + ";";

            int ergebnis1 = stm.executeUpdate(sql);
            if (ergebnis1 == 0) {
                return (CaFehler.pfXyWurdeVonAnderemBenutzerVeraendert);
            }
        } catch (Exception e1) {
            CaBug.drucke("001");
            System.err.println(" " + e1.getMessage());
        }

 
        return (1);
    }

    
}
