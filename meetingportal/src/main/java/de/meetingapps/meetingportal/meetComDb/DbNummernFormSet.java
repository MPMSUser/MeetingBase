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

/**Wichtig: Nicht Mandantenfähig! Mandanten-Nummer ist zwar vorhanden, aber 
 * nicht key - key ist nur ident.*/

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import de.meetingapps.meetingportal.meetComAllg.CaBug;
import de.meetingapps.meetingportal.meetComAllg.CaFehler;
import de.meetingapps.meetingportal.meetComBVerwaltung.BvReload;
import de.meetingapps.meetingportal.meetComEntities.EclNummernFormSet;

public class DbNummernFormSet  extends DbRootExecute {

    private Connection verbindung = null;
    private DbBasis dbBasis = null;
    private DbBundle dbBundle = null;

    public EclNummernFormSet ergebnisArray[] = null;

    /*************************Initialisierung***************************/
    public DbNummernFormSet(DbBundle pDbBundle) {
        /* Verbindung in lokale Daten eintragen*/
        if (pDbBundle == null) {
            CaBug.drucke("DbNummernFormSet.init 001 - dbBundle nicht initialisiert");
            return;
        }
        if (pDbBundle.dbBasis == null) {
            CaBug.drucke("DbNummernFormSet.init 002 - dbBasis nicht initialisiert");
            return;
        }

        dbBasis = pDbBundle.dbBasis;
        verbindung = pDbBundle.dbBasis.verbindung;
        dbBundle = pDbBundle;
    }

    /**************Anzahl der Ergebnisse der read*-Methoden**************************/
    public int anzErgebnis() {
        if (ergebnisArray == null) {
            return 0;
        }
        return ergebnisArray.length;
    }

    /**********Liefert pN-tes Element des Ergebnisses der read*Methoden**************
     * pN geht von 0 bis anzErgebnis-1*/
    public EclNummernFormSet ergebnisPosition(int pN) {
        if (ergebnisArray == null) {
            CaBug.drucke("DbNummernFormSet.ergebnisPosition 001");
            return null;
        }
        if (pN < 0) {
            CaBug.drucke("DbNummernFormSet.ergebnisPosition 002");
            return null;
        }
        if (pN >= ergebnisArray.length) {
            CaBug.drucke("DbNummernFormSet.ergebnisPosition 003");
            return null;
        }
        return ergebnisArray[pN];
    }

    public int createTable() {
        int rc = 0;
        DbLowLevel lDbLowLevel = new DbLowLevel(dbBundle);
        String hSql = "CREATE TABLE " + dbBundle.getSchemaAllgemein() + "tbl_nummernformset ( "
                + "`mandant` int(11) DEFAULT NULL, " + "`ident` int(11) NOT NULL, "
                + "`geloescht` int(11) DEFAULT NULL, " + "`ersetztDurch` int(11) DEFAULT NULL, "
                + "`name` varchar(20) DEFAULT NULL, " + "`beschreibung` varchar(500) DEFAULT NULL, "
                + "`klassifizierung` int(11) DEFAULT NULL, " + "`laengeKartennummer1` int(11) DEFAULT NULL, "
                + "`laengeKartennummer2` int(11) DEFAULT NULL, " + "`laengeKartennummer3` int(11) DEFAULT NULL, "
                + "`laengeKartennummer4` int(11) DEFAULT NULL, " + "`laengeKartennummer5` int(11) DEFAULT NULL, "
                + "`laengeKartennummer6` int(11) DEFAULT NULL, " + "`istNumerisch1` int(11) DEFAULT NULL, "
                + "`istNumerisch2` int(11) DEFAULT NULL, " + "`istNumerisch3` int(11) DEFAULT NULL, "
                + "`istNumerisch4` int(11) DEFAULT NULL, " + "`istNumerisch5` int(11) DEFAULT NULL, "
                + "`istNumerisch6` int(11) DEFAULT NULL, " + "`vonGesamt1` int(11) DEFAULT NULL, "
                + "`vonGesamt2` int(11) DEFAULT NULL, " + "`vonGesamt3` int(11) DEFAULT NULL, "
                + "`vonGesamt4` int(11) DEFAULT NULL, " + "`bisGesamt1` int(11) DEFAULT NULL, "
                + "`bisGesamt2` int(11) DEFAULT NULL, " + "`bisGesamt3` int(11) DEFAULT NULL, "
                + "`bisGesamt4` int(11) DEFAULT NULL, " + "`vonAuto1` int(11) DEFAULT NULL, "
                + "`vonAuto2` int(11) DEFAULT NULL, " + "`vonAuto3` int(11) DEFAULT NULL, "
                + "`bisAuto1` int(11) DEFAULT NULL, " + "`bisAuto2` int(11) DEFAULT NULL, "
                + "`bisAuto3` int(11) DEFAULT NULL, " + "`vonManuell1` int(11) DEFAULT NULL, "
                + "`vonManuell2` int(11) DEFAULT NULL, " + "`vonManuell3` int(11) DEFAULT NULL, "
                + "`bisManuell1` int(11) DEFAULT NULL, " + "`bisManuell2` int(11) DEFAULT NULL, "
                + "`bisManuell3` int(11) DEFAULT NULL, " + "`vonSammelnr` int(11) DEFAULT NULL, "
                + "`bisSammelnr` int(11) DEFAULT NULL, ";
        for (int i = 1; i <= 5; i++) {
            for (int i1 = 1; i1 <= 5; i1++) {
                hSql = hSql + "`vonStimmSub" + Integer.toString(i) + Integer.toString(i1) + "` int(11) DEFAULT NULL, ";
            }
            for (int i1 = 1; i1 <= 5; i1++) {
                hSql = hSql + "`bisStimmSub" + Integer.toString(i) + Integer.toString(i1) + "` int(11) DEFAULT NULL, ";
            }
        }
        for (int i = 1; i <= 5; i++) {
            for (int i1 = 1; i1 <= 2; i1++) {
                hSql = hSql + "`vonEintrittSub" + Integer.toString(i) + Integer.toString(i1)
                        + "` int(11) DEFAULT NULL, ";
            }
            for (int i1 = 1; i1 <= 2; i1++) {
                hSql = hSql + "`bisEintrittSub" + Integer.toString(i) + Integer.toString(i1)
                        + "` int(11) DEFAULT NULL, ";
            }
        }
        hSql = hSql + "`kodierungJa` int(11) DEFAULT NULL, " + "`kodierungNein` int(11) DEFAULT NULL, "
                + "`kodierungEnthaltung` int(11) DEFAULT NULL, "
                + "`beiEintrittskarteWirdStimmkarteNebenAnhaengen` int(11) DEFAULT NULL, "
                + "`berechnungsVerfahrenPruefziffer` int(11) DEFAULT NULL, ";
        for (int i = 0; i <= 9; i++) {
            for (int i1 = 0; i1 <= 9; i1++) {
                hSql = hSql + "`kombiZuCode" + Integer.toString(i) + Integer.toString(i1) + "` int(11) DEFAULT NULL, ";
            }
        }
        hSql = hSql + "`klasseZuCode0` int(11) DEFAULT NULL, " + "`klasseZuCode1` int(11) DEFAULT NULL, "
                + "`klasseZuCode2` int(11) DEFAULT NULL, " + "`klasseZuCode3` int(11) DEFAULT NULL, "
                + "`klasseZuCode4` int(11) DEFAULT NULL, " + "`klasseZuCode5` int(11) DEFAULT NULL, "
                + "`klasseZuCode6` int(11) DEFAULT NULL, " + "`klasseZuCode7` int(11) DEFAULT NULL, "
                + "`klasseZuCode8` int(11) DEFAULT NULL, " + "`klasseZuCode9` int(11) DEFAULT NULL, "
                + "`artZuCode0` int(11) DEFAULT NULL, " + "`artZuCode1` int(11) DEFAULT NULL, "
                + "`artZuCode2` int(11) DEFAULT NULL, " + "`artZuCode3` int(11) DEFAULT NULL, "
                + "`artZuCode4` int(11) DEFAULT NULL, " + "`artZuCode5` int(11) DEFAULT NULL, "
                + "`artZuCode6` int(11) DEFAULT NULL, " + "`artZuCode7` int(11) DEFAULT NULL, "
                + "`artZuCode8` int(11) DEFAULT NULL, " + "`artZuCode9` int(11) DEFAULT NULL, ";
        for (int i = 0; i <= 9; i++) {
            for (int i1 = 0; i1 <= 9; i1++) {
                hSql = hSql + "`nummernformZuKlasseArt" + Integer.toString(i) + Integer.toString(i1)
                        + "` int(11) DEFAULT NULL, ";
            }
        }
        hSql = hSql + "PRIMARY KEY (`ident`) " + ") ";

        rc = lDbLowLevel.createTable(hSql);
        return rc;
    }

    /**************************deleteAll Löschen aller Datensätze****************/
    public int deleteAll() {
        int erg = 0;

        dbBasis.resetInterneIdentNummernFormSet();

        try {

            String sql1 = "DELETE FROM " + dbBundle.getSchemaAllgemein() + "tbl_nummernformSet;";
            PreparedStatement pstm1 = verbindung.prepareStatement(sql1);

            erg = 0; /*Falls Duplicate Key, dann wird exception geschmissen und erg wohl nicht gefüllt!*/
            erg = executeUpdate(pstm1);

            pstm1.close();
        } catch (Exception e2) {
            CaBug.drucke("DbNummernFormSet.deleteAll 001");
            System.err.println(" " + e2.getMessage());
            return (erg);
        }

        return 1;
    }

    public void reorgInterneIdent() {
        int lMax;
        lMax = 0;

        PreparedStatement pstm1 = null;
        try {
            int anzInArray;
            String sql = "SELECT MAX(ident) FROM " + dbBundle.getSchemaAllgemein() + "tbl_nummernformSet ";
            pstm1 = verbindung.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);

            ResultSet ergebnis = executeQuery(pstm1);
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
            CaBug.drucke("DbNummernFormSet.reorgInterneIdent 001");
            System.err.println(" " + e.getMessage());
        }

        dbBasis.resetInterneIdentNummernFormSet(lMax);

    }

    /********** dekodiert die aktuelle Position aus ergebnis in EclAktienregisterEintrag und gibt dieses zurück******/
    private EclNummernFormSet decodeErgebnis(ResultSet pErgebnis) {

        EclNummernFormSet lNummernFormSet = new EclNummernFormSet();

        try {

            lNummernFormSet.mandant = pErgebnis.getInt("mandant");
            lNummernFormSet.ident = pErgebnis.getInt("ident");
            lNummernFormSet.geloescht = pErgebnis.getInt("geloescht");
            lNummernFormSet.ersetztDurch = pErgebnis.getInt("ersetztDurch");

            lNummernFormSet.name = pErgebnis.getString("name");
            lNummernFormSet.beschreibung = pErgebnis.getString("beschreibung");

            lNummernFormSet.klassifizierung = pErgebnis.getInt("klassifizierung");

            lNummernFormSet.laengeKartennummer[0] = 0;
            lNummernFormSet.laengeKartennummer[1] = pErgebnis.getInt("laengeKartennummer1");
            lNummernFormSet.laengeKartennummer[2] = pErgebnis.getInt("laengeKartennummer2");
            lNummernFormSet.laengeKartennummer[3] = pErgebnis.getInt("laengeKartennummer3");
            lNummernFormSet.laengeKartennummer[4] = pErgebnis.getInt("laengeKartennummer4");
            lNummernFormSet.laengeKartennummer[5] = pErgebnis.getInt("laengeKartennummer5");
            lNummernFormSet.laengeKartennummer[5] = 5;
            lNummernFormSet.laengeKartennummer[6] = pErgebnis
                    .getInt("laengeKartennummer6"); /*TODO _Nummernformen Konsolidierung*/
            lNummernFormSet.laengeKartennummer[7] = lNummernFormSet.laengeKartennummer[1];

            lNummernFormSet.istNumerisch[0] = true;
            if (pErgebnis.getInt("istNumerisch1") == 1) {
                lNummernFormSet.istNumerisch[1] = true;
            } else {
                lNummernFormSet.istNumerisch[1] = false;
            }
            if (pErgebnis.getInt("istNumerisch2") == 1) {
                lNummernFormSet.istNumerisch[2] = true;
            } else {
                lNummernFormSet.istNumerisch[2] = false;
            }
            if (pErgebnis.getInt("istNumerisch3") == 1) {
                lNummernFormSet.istNumerisch[3] = true;
            } else {
                lNummernFormSet.istNumerisch[3] = false;
            }
            if (pErgebnis.getInt("istNumerisch4") == 1) {
                lNummernFormSet.istNumerisch[4] = true;
            } else {
                lNummernFormSet.istNumerisch[4] = false;
            }
            lNummernFormSet.istNumerisch[5] = true;
            lNummernFormSet.istNumerisch[6] = true;
            lNummernFormSet.istNumerisch[7] = lNummernFormSet.istNumerisch[1];

            lNummernFormSet.vonKartennummerGesamt[0] = 0;
            lNummernFormSet.vonKartennummerGesamt[1] = pErgebnis.getInt("vonGesamt1");
            lNummernFormSet.vonKartennummerGesamt[2] = pErgebnis.getInt("vonGesamt2");
            lNummernFormSet.vonKartennummerGesamt[3] = pErgebnis.getInt("vonGesamt3");
            lNummernFormSet.vonKartennummerGesamt[4] = pErgebnis.getInt("vonGesamt4");
            lNummernFormSet.vonKartennummerGesamt[5] = 1;
            lNummernFormSet.vonKartennummerGesamt[6] = 1;
            lNummernFormSet.vonKartennummerGesamt[7] = lNummernFormSet.vonKartennummerGesamt[1];

            lNummernFormSet.bisKartennummerGesamt[0] = 0;
            lNummernFormSet.bisKartennummerGesamt[1] = pErgebnis.getInt("bisGesamt1");
            lNummernFormSet.bisKartennummerGesamt[2] = pErgebnis.getInt("bisGesamt2");
            lNummernFormSet.bisKartennummerGesamt[3] = pErgebnis.getInt("bisGesamt3");
            lNummernFormSet.bisKartennummerGesamt[4] = pErgebnis.getInt("bisGesamt4");
            lNummernFormSet.bisKartennummerGesamt[5] = 9999999;
            lNummernFormSet.bisKartennummerGesamt[6] = 9999999;
            lNummernFormSet.bisKartennummerGesamt[7] = lNummernFormSet.bisKartennummerGesamt[1];

            lNummernFormSet.vonKartennummerAuto[0] = 0;
            lNummernFormSet.vonKartennummerAuto[1] = pErgebnis.getInt("vonAuto1");
            lNummernFormSet.vonKartennummerAuto[2] = pErgebnis.getInt("vonAuto2");
            lNummernFormSet.vonKartennummerAuto[3] = pErgebnis.getInt("vonAuto3");
            lNummernFormSet.vonKartennummerAuto[4] = 1;
            lNummernFormSet.vonKartennummerAuto[5] = 1;
            lNummernFormSet.vonKartennummerAuto[6] = 1;
            lNummernFormSet.vonKartennummerAuto[7] = lNummernFormSet.vonKartennummerAuto[1];

            lNummernFormSet.bisKartennummerAuto[0] = 0;
            lNummernFormSet.bisKartennummerAuto[1] = pErgebnis.getInt("bisAuto1");
            lNummernFormSet.bisKartennummerAuto[2] = pErgebnis.getInt("bisAuto2");
            lNummernFormSet.bisKartennummerAuto[3] = pErgebnis.getInt("bisAuto3");
            lNummernFormSet.bisKartennummerAuto[4] = 9999999;
            lNummernFormSet.bisKartennummerAuto[5] = 9999999;
            lNummernFormSet.bisKartennummerAuto[6] = 9999999;
            lNummernFormSet.bisKartennummerAuto[7] = lNummernFormSet.bisKartennummerAuto[1];

            lNummernFormSet.vonKartennummerManuell[0] = 0;
            lNummernFormSet.vonKartennummerManuell[1] = pErgebnis.getInt("vonManuell1");
            lNummernFormSet.vonKartennummerManuell[2] = pErgebnis.getInt("vonManuell2");
            lNummernFormSet.vonKartennummerManuell[3] = pErgebnis.getInt("vonManuell3");
            lNummernFormSet.vonKartennummerManuell[4] = 1;
            lNummernFormSet.vonKartennummerManuell[5] = 1;
            lNummernFormSet.vonKartennummerManuell[6] = 1;
            lNummernFormSet.vonKartennummerManuell[7] = lNummernFormSet.vonKartennummerManuell[1];

            lNummernFormSet.bisKartennummerManuell[0] = 0;
            lNummernFormSet.bisKartennummerManuell[1] = pErgebnis.getInt("bisManuell1");
            lNummernFormSet.bisKartennummerManuell[2] = pErgebnis.getInt("bisManuell2");
            lNummernFormSet.bisKartennummerManuell[3] = pErgebnis.getInt("bisManuell3");
            lNummernFormSet.bisKartennummerManuell[4] = 9999999;
            lNummernFormSet.bisKartennummerManuell[5] = 9999999;
            lNummernFormSet.bisKartennummerManuell[6] = 9999999;
            lNummernFormSet.bisKartennummerManuell[7] = lNummernFormSet.bisKartennummerManuell[1];

            lNummernFormSet.vonSammelkartennummer = pErgebnis.getInt("vonSammelnr");
            lNummernFormSet.bisSammelkartennummer = pErgebnis.getInt("bisSammelnr");

            for (int i = 1; i < 6; i++) {
                for (int i1 = 1; i1 < 6; i1++) {
                    lNummernFormSet.vonSubStimmkartennummer[i][i1] = pErgebnis
                            .getInt("vonStimmSub" + Integer.toString(i) + Integer.toString(i1));
                }
                for (int i1 = 1; i1 < 6; i1++) {
                    lNummernFormSet.bisSubStimmkartennummer[i][i1] = pErgebnis
                            .getInt("bisStimmSub" + Integer.toString(i) + Integer.toString(i1));
                }
            }

            for (int i = 1; i < 6; i++) {
                for (int i1 = 1; i1 < 3; i1++) {
                    lNummernFormSet.vonSubEintrittskartennummer[i][i1] = pErgebnis
                            .getInt("vonEintrittSub" + Integer.toString(i) + Integer.toString(i1));
                }
                for (int i1 = 1; i1 < 3; i1++) {
                    lNummernFormSet.bisSubEintrittskartennummer[i][i1] = pErgebnis
                            .getInt("bisEintrittSub" + Integer.toString(i) + Integer.toString(i1));
                }
            }

            lNummernFormSet.kodierungJa = pErgebnis.getInt("kodierungJa");
            lNummernFormSet.kodierungNein = pErgebnis.getInt("kodierungNein");
            lNummernFormSet.kodierungEnthaltung = pErgebnis.getInt("kodierungEnthaltung");

            lNummernFormSet.beiEintrittskarteWirdStimmkarteNebenAnhaengen = pErgebnis
                    .getInt("beiEintrittskarteWirdStimmkarteNebenAnhaengen");
            lNummernFormSet.berechnungsVerfahrenPruefziffer = pErgebnis.getInt("berechnungsVerfahrenPruefziffer");

            for (int i = 0; i < 10; i++) {
                for (int i1 = 0; i1 < 10; i1++) {
                    lNummernFormSet.kombiZuCode[i][i1] = pErgebnis
                            .getInt("kombiZuCode" + Integer.toString(i) + Integer.toString(i1));
                }
            }
            for (int i = 0; i < 10; i++) {
                lNummernFormSet.klasseZuCode[i] = pErgebnis.getInt("klasseZuCode" + Integer.toString(i));
            }
            for (int i = 0; i < 10; i++) {
                lNummernFormSet.artZuCode[i] = pErgebnis.getInt("artZuCode" + Integer.toString(i));
            }
            for (int i = 0; i < 10; i++) {
                for (int i1 = 0; i1 < 10; i1++) {
                    lNummernFormSet.nummernformZuKlasseArt[i][i1] = pErgebnis
                            .getInt("nummernformZuKlasseArt" + Integer.toString(i) + Integer.toString(i1));
                }
            }

        } catch (Exception e) {
            CaBug.drucke("DbNummernFormSet.decodeErgebnis 001");
            System.err.println(" " + e.getMessage());
        }

        return lNummernFormSet;
    }

    /********************* Fuellen Prepared Statement mit allen Feldern, beginnend bei offset.*****************
     * Kann sowohl für Insert, als auch für update verwendet werden.
     * 
     * offset= Startposition des ersten Feldes (also z.B. 1)
     */
    private int anzfelder = 234 + 20 + 82; /*Anpassen auf Anzahl der Felder pro Datensatz*/

    private void fuellePreparedStatementKomplett(PreparedStatement pPStm, int pOffset,
            EclNummernFormSet pNummernFormSet) {

        int startOffset = pOffset; /*Nur erforderlich zum Überprüfen von Programmierfehlern - Feldanzahl*/

        try {
            pPStm.setInt(pOffset, pNummernFormSet.mandant);
            pOffset++;
            pPStm.setInt(pOffset, pNummernFormSet.ident);
            pOffset++;
            pPStm.setInt(pOffset, pNummernFormSet.geloescht);
            pOffset++;

            pPStm.setInt(pOffset, pNummernFormSet.ersetztDurch);
            pOffset++;

            pPStm.setString(pOffset, pNummernFormSet.name);
            pOffset++;
            pPStm.setString(pOffset, pNummernFormSet.beschreibung);
            pOffset++;

            pPStm.setInt(pOffset, pNummernFormSet.klassifizierung);
            pOffset++;

            pPStm.setInt(pOffset, pNummernFormSet.laengeKartennummer[1]);
            pOffset++;
            pPStm.setInt(pOffset, pNummernFormSet.laengeKartennummer[2]);
            pOffset++;
            pPStm.setInt(pOffset, pNummernFormSet.laengeKartennummer[3]);
            pOffset++;
            pPStm.setInt(pOffset, pNummernFormSet.laengeKartennummer[4]);
            pOffset++;
            pPStm.setInt(pOffset, pNummernFormSet.laengeKartennummer[5]);
            pOffset++;
            pPStm.setInt(pOffset, pNummernFormSet.laengeKartennummer[6]);
            pOffset++;

            if (pNummernFormSet.istNumerisch[1]) {
                pPStm.setInt(pOffset, 1);
            } else {
                pPStm.setInt(pOffset, 0);
            }
            ;
            pOffset++;
            if (pNummernFormSet.istNumerisch[2]) {
                pPStm.setInt(pOffset, 1);
            } else {
                pPStm.setInt(pOffset, 0);
            }
            ;
            pOffset++;
            if (pNummernFormSet.istNumerisch[3]) {
                pPStm.setInt(pOffset, 1);
            } else {
                pPStm.setInt(pOffset, 0);
            }
            ;
            pOffset++;
            if (pNummernFormSet.istNumerisch[4]) {
                pPStm.setInt(pOffset, 1);
            } else {
                pPStm.setInt(pOffset, 0);
            }
            ;
            pOffset++;
            if (pNummernFormSet.istNumerisch[5]) {
                pPStm.setInt(pOffset, 1);
            } else {
                pPStm.setInt(pOffset, 0);
            }
            ;
            pOffset++;
            if (pNummernFormSet.istNumerisch[6]) {
                pPStm.setInt(pOffset, 1);
            } else {
                pPStm.setInt(pOffset, 0);
            }
            ;
            pOffset++;

            pPStm.setInt(pOffset, pNummernFormSet.vonKartennummerGesamt[1]);
            pOffset++;
            pPStm.setInt(pOffset, pNummernFormSet.vonKartennummerGesamt[2]);
            pOffset++;
            pPStm.setInt(pOffset, pNummernFormSet.vonKartennummerGesamt[3]);
            pOffset++;
            pPStm.setInt(pOffset, pNummernFormSet.vonKartennummerGesamt[4]);
            pOffset++;

            pPStm.setInt(pOffset, pNummernFormSet.bisKartennummerGesamt[1]);
            pOffset++;
            pPStm.setInt(pOffset, pNummernFormSet.bisKartennummerGesamt[2]);
            pOffset++;
            pPStm.setInt(pOffset, pNummernFormSet.bisKartennummerGesamt[3]);
            pOffset++;
            pPStm.setInt(pOffset, pNummernFormSet.bisKartennummerGesamt[4]);
            pOffset++;

            pPStm.setInt(pOffset, pNummernFormSet.vonKartennummerAuto[1]);
            pOffset++;
            pPStm.setInt(pOffset, pNummernFormSet.vonKartennummerAuto[2]);
            pOffset++;
            pPStm.setInt(pOffset, pNummernFormSet.vonKartennummerAuto[3]);
            pOffset++;

            pPStm.setInt(pOffset, pNummernFormSet.bisKartennummerAuto[1]);
            pOffset++;
            pPStm.setInt(pOffset, pNummernFormSet.bisKartennummerAuto[2]);
            pOffset++;
            pPStm.setInt(pOffset, pNummernFormSet.bisKartennummerAuto[3]);
            pOffset++;

            pPStm.setInt(pOffset, pNummernFormSet.vonKartennummerManuell[1]);
            pOffset++;
            pPStm.setInt(pOffset, pNummernFormSet.vonKartennummerManuell[2]);
            pOffset++;
            pPStm.setInt(pOffset, pNummernFormSet.vonKartennummerManuell[3]);
            pOffset++;

            pPStm.setInt(pOffset, pNummernFormSet.bisKartennummerManuell[1]);
            pOffset++;
            pPStm.setInt(pOffset, pNummernFormSet.bisKartennummerManuell[2]);
            pOffset++;
            pPStm.setInt(pOffset, pNummernFormSet.bisKartennummerManuell[3]);
            pOffset++;

            pPStm.setInt(pOffset, pNummernFormSet.vonSammelkartennummer);
            pOffset++;
            pPStm.setInt(pOffset, pNummernFormSet.bisSammelkartennummer);
            pOffset++;

            for (int i = 1; i < 6; i++) {
                for (int i1 = 1; i1 < 6; i1++) {
                    pPStm.setInt(pOffset, pNummernFormSet.vonSubStimmkartennummer[i][i1]);
                    pOffset++;
                }
                for (int i1 = 1; i1 < 6; i1++) {
                    pPStm.setInt(pOffset, pNummernFormSet.bisSubStimmkartennummer[i][i1]);
                    pOffset++;
                }
            }

            for (int i = 1; i < 6; i++) {
                for (int i1 = 1; i1 < 3; i1++) {
                    pPStm.setInt(pOffset, pNummernFormSet.vonSubEintrittskartennummer[i][i1]);
                    pOffset++;
                }
                for (int i1 = 1; i1 < 3; i1++) {
                    pPStm.setInt(pOffset, pNummernFormSet.bisSubEintrittskartennummer[i][i1]);
                    pOffset++;
                }
            }

            pPStm.setInt(pOffset, pNummernFormSet.kodierungJa);
            pOffset++;
            pPStm.setInt(pOffset, pNummernFormSet.kodierungNein);
            pOffset++;
            pPStm.setInt(pOffset, pNummernFormSet.kodierungEnthaltung);
            pOffset++;

            pPStm.setInt(pOffset, pNummernFormSet.beiEintrittskarteWirdStimmkarteNebenAnhaengen);
            pOffset++;
            pPStm.setInt(pOffset, pNummernFormSet.berechnungsVerfahrenPruefziffer);
            pOffset++;

            for (int i = 0; i < 10; i++) {
                for (int i1 = 0; i1 < 10; i1++) {
                    pPStm.setInt(pOffset, pNummernFormSet.kombiZuCode[i][i1]);
                    pOffset++;
                }
            }
            for (int i = 0; i < 10; i++) {
                pPStm.setInt(pOffset, pNummernFormSet.klasseZuCode[i]);
                pOffset++;
            }
            for (int i = 0; i < 10; i++) {
                pPStm.setInt(pOffset, pNummernFormSet.artZuCode[i]);
                pOffset++;
            }
            for (int i = 0; i < 10; i++) {
                for (int i1 = 0; i1 < 10; i1++) {
                    pPStm.setInt(pOffset, pNummernFormSet.nummernformZuKlasseArt[i][i1]);
                    pOffset++;
                }
            }

            if (pOffset - startOffset != anzfelder) {
                /*Nur wg. Überprüfung auf Programmierfehler - alle Felder berücksichtigt?*/
                CaBug.drucke("DbNummernFormSet.fuellePreparedStatementKomplett 002");
                System.out.println("pOffset=" + pOffset + " startOffset=" + startOffset + " anzfelder=" + anzfelder);
            }

        } catch (SQLException e) {
            CaBug.drucke("DbNummernFormSet.fuellePreparedStatementKomplett 001");
            e.printStackTrace();
        }

    }

    /**Insert
     * 
     * Feld mandant wird von dieser Funktion NICHT belegt.
     * 
     * Returnwert:
     * =1 => Insert erfolgreich
     * ansonsten: Fehler
     */
    public int insert(EclNummernFormSet pNummernForm) {
        int erg = 0;

        /* Start Transaktion */
        dbBasis.beginTransaction();

        /* neue InterneIdent vergeben */
        erg = dbBasis.getInterneIdentNummernFormSet();
        if (erg < 1) {
            CaBug.drucke("DbNummernFormSet.insert 002");
            dbBasis.rollbackTransaction();
            dbBasis.endTransaction();
            return (erg);
        }
        pNummernForm.ident = erg;

        //		pNummernForm.mandant=dbBundle.globalVar.mandant;

        /* Satz einfügen: 
         * Verarbeitungshinweis: 
         * 	>	nachdem InterneIdent immer eindeutig vergeben werden, ist prinzipiell eine "Doppeleinfügung"
         * 		von InterneIdent nicht möglich. Sollte es dazu kommen, ist das immer ein Programmfehler!
         */

        try {

            /*Felder Neuanlage füllen*/
            String lSql = "INSERT INTO " + dbBundle.getSchemaAllgemein() + "tbl_nummernformSet " + "("
                    + "mandant, ident, geloescht, " + "ersetztDurch, name, beschreibung, " + "klassifizierung, ";
            for (int i = 1; i <= 6; i++) {
                lSql = lSql + "laengeKartennummer" + Integer.toString(i) + ", ";
            }
            for (int i = 1; i <= 6; i++) {
                lSql = lSql + "istNumerisch" + Integer.toString(i) + ", ";
            }
            for (int i = 1; i <= 4; i++) {
                lSql = lSql + "vonGesamt" + Integer.toString(i) + ", ";
            }
            for (int i = 1; i <= 4; i++) {
                lSql = lSql + "bisGesamt" + Integer.toString(i) + ", ";
            }
            for (int i = 1; i <= 3; i++) {
                lSql = lSql + "vonAuto" + Integer.toString(i) + ", ";
            }
            for (int i = 1; i <= 3; i++) {
                lSql = lSql + "bisAuto" + Integer.toString(i) + ", ";
            }
            for (int i = 1; i <= 3; i++) {
                lSql = lSql + "vonManuell" + Integer.toString(i) + ", ";
            }
            for (int i = 1; i <= 3; i++) {
                lSql = lSql + "bisManuell" + Integer.toString(i) + ", ";
            }
            lSql = lSql + "vonSammelnr, bisSammelnr, ";
            for (int i = 1; i < 6; i++) {
                for (int i1 = 1; i1 < 6; i1++) {
                    lSql = lSql + "vonStimmSub" + Integer.toString(i) + Integer.toString(i1) + ", ";
                }
                for (int i1 = 1; i1 < 6; i1++) {
                    lSql = lSql + "bisStimmSub" + Integer.toString(i) + Integer.toString(i1) + ", ";
                }
            }
            for (int i = 1; i < 6; i++) {
                for (int i1 = 1; i1 < 3; i1++) {
                    lSql = lSql + "vonEintrittSub" + Integer.toString(i) + Integer.toString(i1) + ", ";
                }
                for (int i1 = 1; i1 < 3; i1++) {
                    lSql = lSql + "bisEintrittSub" + Integer.toString(i) + Integer.toString(i1) + ", ";
                }
            }

            lSql = lSql + "kodierungJa, kodierungNein, kodierungEnthaltung, "
                    + "beiEintrittskarteWirdStimmkarteNebenAnhaengen, berechnungsVerfahrenPruefziffer ";
            for (int i = 0; i < 10; i++) {
                for (int i1 = 0; i1 < 10; i1++) {
                    lSql = lSql + ", kombiZuCode" + Integer.toString(i) + Integer.toString(i1);
                }
            }
            for (int i = 0; i < 10; i++) {
                lSql = lSql + ", klasseZuCode" + Integer.toString(i);
            }
            for (int i = 0; i < 10; i++) {
                lSql = lSql + ", artZuCode" + Integer.toString(i);
            }
            for (int i = 0; i < 10; i++) {
                for (int i1 = 0; i1 < 10; i1++) {
                    lSql = lSql + ", nummernformZuKlasseArt" + Integer.toString(i) + Integer.toString(i1);
                }
            }

            lSql = lSql + ")" + "VALUES (" + "?, ?, ?, " + "?, ?, ?, " + "?, ?, ?, ?, " + "?, ?, ?, ? ";
            for (int i = 0; i < 220 + 20 + 82; i++) {
                lSql = lSql + ", ?";
            }

            lSql = lSql + ")";
            PreparedStatement lPStm = verbindung.prepareStatement(lSql, ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
            fuellePreparedStatementKomplett(lPStm, 1, pNummernForm);

            erg = executeUpdate(lPStm);
            lPStm.close();
        } catch (Exception e2) {
            CaBug.drucke("DbNummernFormSet.insert 001");
            System.err.println(" " + e2.getMessage());
        }

        if (erg == 0) {/*Fehler beim Einfügen - d.h. primaryKey bereits vorhanden*/
            dbBasis.rollbackTransaction();
            dbBasis.endTransaction();
            return (-1);
        }

        BvReload bvReload = new BvReload(dbBundle);
        bvReload.setReloadParameter(dbBundle.clGlobalVar.mandant);

        /* Ende Transaktion */
        dbBasis.endTransaction();
        return (1);
    }

    public int read(EclNummernFormSet pNummernForm) {
        int anzInArray = 0;
        PreparedStatement lPStm = null;

        try {
            String lSql = "SELECT * from " + dbBundle.getSchemaAllgemein() + "tbl_nummernformSet where "
                    + "ident=? ORDER BY ident;";
            lPStm = verbindung.prepareStatement(lSql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            lPStm.setInt(1, pNummernForm.ident);

            ResultSet lErgebnis = executeQuery(lPStm);
            lErgebnis.last();
            anzInArray = lErgebnis.getRow();
            lErgebnis.beforeFirst();

            ergebnisArray = new EclNummernFormSet[anzInArray];

            int i = 0;
            while (lErgebnis.next() == true) {
                ergebnisArray[i] = this.decodeErgebnis(lErgebnis);
                i++;
            }
            lErgebnis.close();
            lPStm.close();

        } catch (Exception e) {
            CaBug.drucke("DbNummernFormSet.read 003");
            System.err.println(" " + e.getMessage());
            return (-1);
        }
        return (anzInArray);
    }

    public int read_all() {
        int anzInArray = 0;
        PreparedStatement lPStm = null;

        try {
            String lSql = "SELECT * from " + dbBundle.getSchemaAllgemein() + "tbl_nummernformSet " + "ORDER BY ident;";
            lPStm = verbindung.prepareStatement(lSql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);

            ResultSet lErgebnis = executeQuery(lPStm);
            lErgebnis.last();
            anzInArray = lErgebnis.getRow();
            lErgebnis.beforeFirst();

            ergebnisArray = new EclNummernFormSet[anzInArray];

            int i = 0;
            while (lErgebnis.next() == true) {
                ergebnisArray[i] = this.decodeErgebnis(lErgebnis);
                i++;
            }
            lErgebnis.close();
            lPStm.close();

        } catch (Exception e) {
            CaBug.drucke("DbNummernFormSet.read_all 003");
            System.err.println(" " + e.getMessage());
            return (-1);
        }
        return (anzInArray);
    }

    /**Update.
     *
     * Nicht Multiuserfähig ...
     * 
     * Ansonsten: rc=1 => ok, ansonsten Fehler
     */
    public int update(EclNummernFormSet pNummernForm) {

        try {

            String lSql = "UPDATE " + dbBundle.getSchemaAllgemein() + "tbl_nummernformSet SET "
                    + "mandant=?, ident=?, geloescht=?, " + "ersetztDurch=?, name=?, beschreibung=?, "
                    + "klassifizierung=?, ";
            for (int i = 1; i <= 6; i++) {
                lSql = lSql + "laengeKartennummer" + Integer.toString(i) + "=?, ";
            }
            for (int i = 1; i <= 6; i++) {
                lSql = lSql + "istNumerisch" + Integer.toString(i) + "=?, ";
            }
            for (int i = 1; i <= 4; i++) {
                lSql = lSql + "vonGesamt" + Integer.toString(i) + "=?, ";
            }
            for (int i = 1; i <= 4; i++) {
                lSql = lSql + "bisGesamt" + Integer.toString(i) + "=?, ";
            }
            for (int i = 1; i <= 3; i++) {
                lSql = lSql + "vonAuto" + Integer.toString(i) + "=?, ";
            }
            for (int i = 1; i <= 3; i++) {
                lSql = lSql + "bisAuto" + Integer.toString(i) + "=?, ";
            }
            for (int i = 1; i <= 3; i++) {
                lSql = lSql + "vonManuell" + Integer.toString(i) + "=?, ";
            }
            for (int i = 1; i <= 3; i++) {
                lSql = lSql + "bisManuell" + Integer.toString(i) + "=?, ";
            }
            lSql = lSql + "vonSammelnr=?, bisSammelnr=?, ";
            for (int i = 1; i < 6; i++) {
                for (int i1 = 1; i1 < 6; i1++) {
                    lSql = lSql + "vonStimmSub" + Integer.toString(i) + Integer.toString(i1) + "=?, ";
                }
                for (int i1 = 1; i1 < 6; i1++) {
                    lSql = lSql + "bisStimmSub" + Integer.toString(i) + Integer.toString(i1) + "=?, ";
                }
            }
            for (int i = 1; i < 6; i++) {
                for (int i1 = 1; i1 < 3; i1++) {
                    lSql = lSql + "vonEintrittSub" + Integer.toString(i) + Integer.toString(i1) + "=?, ";
                }
                for (int i1 = 1; i1 < 3; i1++) {
                    lSql = lSql + "bisEintrittSub" + Integer.toString(i) + Integer.toString(i1) + "=?, ";
                }
            }

            lSql = lSql + "kodierungJa=?, kodierungNein=?, kodierungEnthaltung=?, "
                    + "beiEintrittskarteWirdStimmkarteNebenAnhaengen=?, berechnungsVerfahrenPruefziffer=? ";

            for (int i = 0; i < 10; i++) {
                for (int i1 = 0; i1 < 10; i1++) {
                    lSql = lSql + ", kombiZuCode" + Integer.toString(i) + Integer.toString(i1) + "=?";
                }
            }
            for (int i = 0; i < 10; i++) {
                lSql = lSql + ", klasseZuCode" + Integer.toString(i) + "=?";
            }
            for (int i = 0; i < 10; i++) {
                lSql = lSql + ", artZuCode" + Integer.toString(i) + "=?";
            }
            for (int i = 0; i < 10; i++) {
                for (int i1 = 0; i1 < 10; i1++) {
                    lSql = lSql + ", nummernformZuKlasseArt" + Integer.toString(i) + Integer.toString(i1) + "=? ";
                }
            }
            lSql = lSql + "WHERE " + "ident=?";

            PreparedStatement lPStm = verbindung.prepareStatement(lSql);
            fuellePreparedStatementKomplett(lPStm, 1, pNummernForm);
            lPStm.setInt(anzfelder + 1, pNummernForm.ident);

            int ergebnis = executeUpdate(lPStm);
            lPStm.close();
            if (ergebnis == 0) {
                return (CaFehler.pfXyWurdeVonAnderemBenutzerVeraendert);
            }
        } catch (Exception e1) {
            CaBug.drucke("DbNummernFormSet.update 001");
            System.err.println(" " + e1.getMessage());
            return (CaFehler.pfdXyBereitsVorhanden);
        }

        BvReload bvReload = new BvReload(dbBundle);
        bvReload.setReloadParameter(dbBundle.clGlobalVar.mandant);

        return (1);
    }

    public int delete(int ident) {

        try {

            String sql = "DELETE FROM " + dbBundle.getSchemaAllgemein() + "tbl_nummernformSet WHERE ident=? ";

            PreparedStatement pstm1 = verbindung.prepareStatement(sql);
            pstm1.setInt(1, ident);

            int ergebnis1 = executeUpdate(pstm1);
            pstm1.close();
            if (ergebnis1 == 0) {
                return (CaFehler.pfXyWurdeVonAnderemBenutzerVeraendert);
            }
        } catch (Exception e1) {
            CaBug.drucke("DbAbstimmungen.delete 001");
            System.err.println(" " + e1.getMessage());
            return (CaFehler.pfdXyBereitsVorhanden);
        }

        return (1);
    }

}
