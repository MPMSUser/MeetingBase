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
import de.meetingapps.meetingportal.meetComEntities.EclAnrede;

/*************************Offene Fragen wg. Parallelupdates****************
 * 										Anrede 1 wird gelöscht
 * Anrede 1 wird neu eingefügt
 * Anredefremd Land 2 wird neu eingefügt
 * 										Anredefremd 1 wird komplett gelöscht
 * Idee: beim Löschen erst Anredefremd löschen - wenn Anrede 1 dann verändert 
 * dann rollback
 * 
 * Bessere? Idee: Nach dem Update des letzten Satzes noch mal Kopfsatz checken.
 * 
 * Nochbessere Idee: mit Versionsnr arbeiten
 * 	Schwierigkeit: Insert => 1; Delete und gleichzeitig Insert parallel ergibt wieder 1
 */

public class DbAnreden  extends DbRootExecute {

    private Connection verbindung = null;
    //	private DbBasis dbBasis=null;
    private DbBundle dbBundle = null;
    private int sprache = -1;
    private int spracheiststandard = 0;
    private EclAnrede VclAnredeOldVersion;

    /*Array für read-Kommando*/
    public EclAnrede anredenreadarray[];
    public int AnzAnredenInReadArray;

    /*Array für alle Anreden*/
    public EclAnrede anredenarray[];
    public int AnzAnredenInArray;

    /*************************Initialisierung***************************/
    /* Verbindung in lokale Daten eintragen*/
    public DbAnreden(DbBundle pDbBundle) {
        if (pDbBundle.dbBasis == null) {
            System.err.println("dbBasis nicht initialisiert");
            return;
        }
        //		dbBasis=pDbBundle.dbBasis;
        verbindung = pDbBundle.dbBasis.verbindung;
        dbBundle = pDbBundle;

        VclAnredeOldVersion = new EclAnrede();
    }

    /*******************gewählte Sprache einstellen*********************/
    /* sprache = Nr. der Sprache*/
    /* spracheiststandard = 1 => es ist die Standardsprache, keine zusätzliche Sprache angezeigt*/
    public void SetzeSprache(int sprache, int spracheiststandard) {
        this.sprache = sprache;
        this.spracheiststandard = spracheiststandard;
    }

    public int createTable() {
        int rc = 0;
        DbLowLevel lDbLowLevel = new DbLowLevel(dbBundle);
        rc = lDbLowLevel.createTable("CREATE TABLE " + dbBundle.getSchemaAllgemein() + "tbl_anreden ( "
                + "`anredennr` int(11) NOT NULL, " + "`anredentext` varchar(50) DEFAULT NULL, "
                + "`anredenbrief` varchar(50) DEFAULT NULL, " + "`istjuristischePerson` int(11) DEFAULT NULL, "
                + "PRIMARY KEY (`anredennr`), " + "UNIQUE KEY `anredennr` (`anredennr`) " + ") "

        );
        if (rc < 0) {
            return rc;
        }

        rc = lDbLowLevel.createTable("CREATE TABLE " + dbBundle.getSchemaAllgemein() + "tbl_anredenfremd ( "
                + "`anredennr` int(11) NOT NULL, " + "`sprachennr` int(11) NOT NULL, "
                + "`anredentextfremd` varchar(50) DEFAULT NULL, " + "`anredenbrieffremd` varchar(50) DEFAULT NULL, "
                + "PRIMARY KEY (`anredennr`,`sprachennr`) " + ") "

        );
        return rc;

    }

    /**************************deleteAll Löschen aller Datensätze  eines Mandanten****************/
    @Deprecated
    public int deleteAll() {
        /*TODO _Anreden: Anreden sind Mandantenabhängig, deshalb ist delete aktuell nicht aufrufbar!!*/
        int erg = 0;

        try {

            String sql1 = "DELETE FROM " + dbBundle.getSchemaAllgemein() + "tbl_anreden where mandant=?;";
            PreparedStatement pstm1 = verbindung.prepareStatement(sql1);
            pstm1.setInt(1, 999);

            erg = 0; /*Falls Duplicate Key, dann wird exception geschmissen und erg wohl nicht gefüllt!*/
            erg = executeUpdate(pstm1);

            pstm1.close();

        } catch (Exception e2) {
            CaBug.drucke("DbAnreden.deleteAll 001");
            System.err.println(" " + e2.getMessage());
            return (erg);
        }

        return 1;
    }

    /****************Neu-Einfügen des übergebenen Objekts clAnrede in Datenbank********/
    /*Rückgabewert = 1 => Einfügen ok
     * 			   = 0 => Anrede bereits vorhanden
     */
    public int insert(EclAnrede VclAnrede) {

        int ergebnis2 = 0;

        try {
            verbindung.setAutoCommit(false);
        } catch (Exception e2) {
            System.err.println(" " + e2.getMessage());
        }

        try {
            String sql1 = "INSERT INTO " + dbBundle.getSchemaAllgemein() + "tbl_anreden "
                    + "(anredennr, anredentext, anredenbrief, istjuristischePerson)" + "VALUES (?, ?, ?, ?)";

            PreparedStatement pstm1 = verbindung.prepareStatement(sql1);
            pstm1.setInt(1, VclAnrede.anredennr);
            pstm1.setString(2, VclAnrede.anredentext);
            pstm1.setString(3, VclAnrede.anredenbrief);
            pstm1.setInt(4, VclAnrede.istjuristischePerson);

            ergebnis2 = executeUpdate(pstm1);

            pstm1.close();
        } catch (Exception e2) {
            System.err.println(" " + e2.getMessage());
        }

        if (ergebnis2 == 0) {/*Anrede bereits vorhanden*/
            try {
                verbindung.setAutoCommit(true);
            } catch (Exception e2) {
                System.err.println(" " + e2.getMessage());
            }

            return (0);
        }

        /*Falls nicht Standard-Sprache: Übersetzung einfügen*/
        if (spracheiststandard != 1) {
            try {

                String sql1 = "INSERT INTO " + dbBundle.getSchemaAllgemein() + "tbl_anredenfremd "
                        + "(anredennr, sprachennr, anredentextfremd, anredenbrieffremd)" + "VALUES (?, ?, ?, ?)";

                PreparedStatement pstm1 = verbindung.prepareStatement(sql1);
                pstm1.setInt(1, VclAnrede.anredennr);
                pstm1.setInt(2, sprache);
                pstm1.setString(3, VclAnrede.anredentextfremd);
                pstm1.setString(4, VclAnrede.anredenbrieffremd);
                ergebnis2 = pstm1.executeUpdate(sql1);
                pstm1.close();
            } catch (Exception e2) {
                System.err.println(" " + e2.getMessage());
            }

        }
        try {
            verbindung.commit();
            verbindung.setAutoCommit(true);
        } catch (Exception e2) {
            System.err.println(" " + e2.getMessage());
        }

        return (1);
    }

    /**************Einlesen einer Anrede über Nr in ReadArray, mit Speichern in oldversion****/
    /*Rückgabewert: AnzAnredenInReadArray*/
    /*In Public anredenreadarray[] stehen die Sprachen-Elemente drin*/

    public int ReadAnrede_Anredennr(int Vanredennr) {

        try {

            String sql = "SELECT * from " + dbBundle.getSchemaAllgemein() + "tbl_anreden where anredennr=?";
            PreparedStatement pstm = verbindung.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
            pstm.setInt(1, Vanredennr);
            ResultSet ergebnis = executeQuery(pstm);
            ergebnis.last();
            AnzAnredenInReadArray = ergebnis.getRow();
            ergebnis.beforeFirst();

            anredenreadarray = new EclAnrede[AnzAnredenInReadArray];

            int i = 0;
            while (ergebnis.next() == true) {
                EclAnrede anrede = new EclAnrede();
                anrede.anredennr = ergebnis.getInt("anredennr");
                VclAnredeOldVersion.anredennr = ergebnis.getInt("anredennr");
                anrede.anredentext = ergebnis.getString("anredentext");
                VclAnredeOldVersion.anredentext = ergebnis.getString("anredentext");
                anrede.anredenbrief = ergebnis.getString("anredenbrief");
                VclAnredeOldVersion.anredenbrief = ergebnis.getString("anredenbrief");
                anrede.istjuristischePerson = ergebnis.getInt("istjuristischePerson");
                VclAnredeOldVersion.istjuristischePerson = ergebnis.getInt("istjuristischePerson");

                /*Zweite Sprache dazuholen*/
                if (spracheiststandard != 1) {
                    String sql1 = "SELECT * from " + dbBundle.getSchemaAllgemein()
                            + "tbl_anredenfremd where anredennr=? and sprachennr=?";

                    PreparedStatement pstm1 = verbindung.prepareStatement(sql1, ResultSet.TYPE_SCROLL_INSENSITIVE,
                            ResultSet.CONCUR_READ_ONLY);
                    pstm1.setInt(1, Vanredennr);
                    pstm1.setInt(2, sprache);

                    ResultSet ergebnis1 = executeQuery(pstm1);

                    while (ergebnis1.next() == true) {
                        anrede.anredentextfremd = ergebnis1.getString("anredentextfremd");
                        VclAnredeOldVersion.anredentextfremd = ergebnis1.getString("anredentextfremd");
                        anrede.anredenbrieffremd = ergebnis1.getString("anredenbrieffremd");
                        VclAnredeOldVersion.anredenbrieffremd = ergebnis1.getString("anredenbrieffremd");
                    }
                }

                anredenreadarray[i] = anrede;
                i++;

            }
            ergebnis.close();
            pstm.close();

        } catch (Exception e) {
            System.err.println(" " + e.getMessage());
        }
        return (AnzAnredenInReadArray);
    }

    /********** dekodiert die aktuelle Position aus ergebnis aus joined-Read und gibt dieses zurück******/
    EclAnrede decodeErgebnis(ResultSet pErgebnis) {

        EclAnrede lAnrede = new EclAnrede();

        try {

            lAnrede.anredennr = pErgebnis.getInt("an.anredennr");
            lAnrede.anredentext = pErgebnis.getString("an.anredentext");
            lAnrede.anredenbrief = pErgebnis.getString("an.anredenbrief");
            lAnrede.istjuristischePerson = pErgebnis.getInt("an.istjuristischePerson");

            lAnrede.anredentextfremd = pErgebnis.getString("anf.anredentextfremd");
            lAnrede.anredenbrieffremd = pErgebnis.getString("anf.anredenbrieffremd");

        } catch (Exception e) {
            CaBug.drucke("DbAnreden.decodeErgebnis 001");
            System.err.println(" " + e.getMessage());
        }

        return lAnrede;
    }

    /********** dekodiert die aktuelle Position aus ergebnis aus joined-Read-Versand und gibt dieses zurück******/
    EclAnrede decodeErgebnisVersand(ResultSet pErgebnis) {

        EclAnrede lAnrede = new EclAnrede();

        try {

            lAnrede.anredennr = pErgebnis.getInt("anv.anredennr");
            lAnrede.anredentext = pErgebnis.getString("anv.anredentext");
            lAnrede.anredenbrief = pErgebnis.getString("anv.anredenbrief");
            lAnrede.istjuristischePerson = pErgebnis.getInt("anv.istjuristischePerson");

            lAnrede.anredentextfremd = pErgebnis.getString("anfv.anredentextfremd");
            lAnrede.anredenbrieffremd = pErgebnis.getString("anfv.anredenbrieffremd");

        } catch (Exception e) {
            CaBug.drucke("DbAnreden.decodeErgebnisVersand 001");
            System.err.println(" " + e.getMessage());
        }

        return lAnrede;
    }

    public int readJoined_AnredenNr(int pAnredennr) {

        int anzInArray = 0;
        PreparedStatement lPStm = null;

        if (pAnredennr == 0) {
            CaBug.drucke("DbAnreden.readJoined_AnredenNr 001");
            return -1;
        }

        try {
            String lSql = "SELECT an.*, anf.* from " + dbBundle.getSchemaAllgemein() + "tbl_anreden an LEFT OUTER JOIN "
                    + dbBundle.getSchemaAllgemein() + "tbl_anredenfremd anf " + "ON (an.anredennr=anf.anredennr)"
                    + "WHERE an.anredennr=? AND anf.sprachennr=?;";
            lPStm = verbindung.prepareStatement(lSql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            lPStm.setInt(1, pAnredennr);
            lPStm.setInt(2, sprache);

            ResultSet lErgebnis = executeQuery(lPStm);
            lErgebnis.last();
            anzInArray = lErgebnis.getRow();
            lErgebnis.beforeFirst();

            anredenreadarray = new EclAnrede[anzInArray];

            int i = 0;
            while (lErgebnis.next() == true) {

                anredenreadarray[i] = this.decodeErgebnis(lErgebnis);
                i++;
            }
            lErgebnis.close();
            lPStm.close();

        } catch (Exception e) {
            CaBug.drucke("DbAnreden.readJoined_AnredenNr 003");
            System.err.println(" " + e.getMessage());
            return (-1);
        }
        return (anzInArray);
    }

    /****************Update des übergebenen Objekts clAnrede in Datenbank********/
    /*Rückgabewert = 1 => Update ok
     * 			   = 0 => Anrede nicht vorhanden
     * 			   =-1 => Update nicht möglich - verändert
     */

    public int update(EclAnrede VclAnrede) {

        Integer oNranrede;
        oNranrede = VclAnrede.anredennr;

        try {
            verbindung.setAutoCommit(false);
        } catch (Exception e2) {
            System.err.println(" " + e2.getMessage());
        }

        try {

            Statement stm = verbindung.createStatement();
            String sql = "UPDATE " + dbBundle.getSchemaAllgemein() + "tbl_anreden SET anredentext='"
                    + VclAnrede.anredentext + "', anredenbrief='" + VclAnrede.anredenbrief + "' WHERE anredennr="
                    + oNranrede.toString() + " and anredentext='" + VclAnredeOldVersion.anredentext
                    + "' and anredenbrief='" + VclAnredeOldVersion.anredenbrief + "' and istjuristischePerson ="
                    + VclAnredeOldVersion.istjuristischePerson;
            int ergebnis1 = stm.executeUpdate(sql);
            stm.close();
            if (ergebnis1 != 1) {
                verbindung.setAutoCommit(true);

                return (-1);

            }

        } catch (Exception e1) {
            System.err.println(" " + e1.getMessage());
        }

        /*Falls nicht Standard-Sprache: Übersetzung einfügen*/
        if (spracheiststandard != 1) {
            Integer osprache;
            osprache = sprache;

            try {

                Statement stm = verbindung.createStatement();
                String sql = "UPDATE " + dbBundle.getSchemaAllgemein() + "tbl_anredenfremd SET anredentextfremd='"
                        + VclAnrede.anredentextfremd + "', anredenbrieffremd='" + VclAnrede.anredenbrieffremd
                        + "' WHERE anredennr=" + oNranrede.toString() + " AND sprachennr=" + osprache.toString()
                        + " AND anredentextfremd='" + VclAnredeOldVersion.anredentextfremd + "' AND anredenbrieffremd='"
                        + VclAnredeOldVersion.anredenbrieffremd + "'";
                int ergebnis1 = stm.executeUpdate(sql);
                stm.close();
                if (ergebnis1 == 0) {
                    /*Vor Insert prüfen, ob Fremdsprachenteil vorhanden!*/

                    Statement stm4 = verbindung.createStatement();
                    String sql4 = "SELECT * from tbl_anredenfremd where anredennr=" + oNranrede.toString()
                            + " and sprachennr=" + osprache.toString();
                    ResultSet ergebnis4 = stm4.executeQuery(sql4);
                    int ii = 0;
                    while (ergebnis4.next() == true) {
                        ii = 1;
                    }
                    stm4.close();

                    if (ii == 1) {

                        verbindung.rollback();
                        verbindung.setAutoCommit(true);

                        return (-1);

                    }

                    try {

                        Statement stm1 = verbindung.createStatement();
                        String sql1 = "INSERT INTO " + dbBundle.getSchemaAllgemein() + "tbl_anredenfremd "
                                + "(anredennr, sprachennr, anredentextfremd, anredenbrieffremd)" + "VALUES ("
                                + oNranrede.toString() + ", " + osprache.toString() + ", '" + VclAnrede.anredentextfremd
                                + "', '" + VclAnrede.anredenbrieffremd + "')";
                        /*int ergebnis3=*/stm1.executeUpdate(sql1);
                        stm1.close();

                    } catch (Exception e2) {
                        System.err.println(" " + e2.getMessage());
                    }

                }
            } catch (Exception e1) {
                System.err.println(" " + e1.getMessage());
            }

        }
        try {
            verbindung.commit();
            verbindung.setAutoCommit(true);
        } catch (Exception e2) {
            System.err.println(" " + e2.getMessage());
        }

        return (1);
    }

    /****************Delete des übergebenen Objekts clAnrede in Datenbank********/
    /*Rückgabewert = 1 => Delete ok
     * 			   = 0 => Anrede nicht vorhanden
     * 			   =-1 => Delete nicht möglich - verändert
     * 	Hinweis: Fremde Texte werden nicht auf Veränderung überprüft!
     */

    public int delete(EclAnrede VclAnrede) {

        Integer oNranrede;
        oNranrede = VclAnrede.anredennr;

        try {
            verbindung.setAutoCommit(false);
        } catch (Exception e2) {
            System.err.println(" " + e2.getMessage());
        }

        /*Löschen durchführen*/

        try {
            Statement stm = verbindung.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            String sql = "DELETE from " + dbBundle.getSchemaAllgemein() + "tbl_anreden where anredennr="
                    + oNranrede.toString() + " and anredentext='" + VclAnredeOldVersion.anredentext
                    + "' and anredenbrief='" + VclAnredeOldVersion.anredenbrief + "' and istjuristischePerson ="
                    + VclAnredeOldVersion.istjuristischePerson;

            int ergebnis1 = stm.executeUpdate(sql);
            stm.close();
            if (ergebnis1 == 0) {
                verbindung.setAutoCommit(true);
                return (-1);
            }
        } catch (Exception e1) {
            System.err.println(" " + e1.getMessage());
        }

        try {
            Statement stm = verbindung.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            String sql = "DELETE from " + dbBundle.getSchemaAllgemein() + "tbl_anredenfremd where anredennr="
                    + oNranrede.toString();
            /*int ergebnis1=*/stm.executeUpdate(sql);
            stm.close();

        } catch (Exception e1) {
            System.err.println(" " + e1.getMessage());
        }

        try {
            verbindung.commit();
            verbindung.setAutoCommit(true);
        } catch (Exception e2) {
            System.err.println(" " + e2.getMessage());
        }

        return (1);
    }

    /*****************Anreden in Array einlesen*******************************/
    /*Rückgabewert: AnzAnredenInArray*/
    /*Vorbedingung: SetzeSprache ist aufgerufen*/
    /*In Public anredenarray[] stehen anzahl die Anreden-Elemente drin*/
    public int readInArray() {

        try {

            Statement stm = verbindung.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            String sql = "SELECT * from " + dbBundle.getSchemaAllgemein() + "tbl_anreden";
            ResultSet ergebnis = stm.executeQuery(sql);
            ergebnis.last();
            AnzAnredenInArray = ergebnis.getRow();
            ergebnis.beforeFirst();

            anredenarray = new EclAnrede[AnzAnredenInArray];

            int i = 0;
            while (ergebnis.next() == true) {
                EclAnrede anrede = new EclAnrede();
                anrede.anredennr = ergebnis.getInt("anredennr");
                anrede.istjuristischePerson = ergebnis.getInt("istjuristischePerson");
                anrede.anredentext = ergebnis.getString("anredentext");
                anrede.anredenbrief = ergebnis.getString("anredenbrief");
                anrede.anredentextfremd = "";
                anrede.anredenbrieffremd = "";

                /*Zweite Sprache dazuholen*/
                if (spracheiststandard != 1) {
                    Statement stm1 = verbindung.createStatement();
                    String sql1 = "SELECT * from " + dbBundle.getSchemaAllgemein() + "tbl_anredenfremd where anredennr="
                            + anrede.anredennr + " and sprachennr=" + sprache;
                    ResultSet ergebnis1 = stm1.executeQuery(sql1);

                    while (ergebnis1.next() == true) {
                        anrede.anredentextfremd = ergebnis1.getString("anredentextfremd");
                        anrede.anredenbrieffremd = ergebnis1.getString("anredenbrieffremd");
                    }
                }

                anredenarray[i] = anrede;
                i++;

            }
            ergebnis.close();
            stm.close();

        } catch (Exception e) {
            System.err.println(" " + e.getMessage());
        }
        return (AnzAnredenInArray);

    }

}
