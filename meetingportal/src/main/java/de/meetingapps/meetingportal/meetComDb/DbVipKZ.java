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
import java.sql.Statement;

import de.meetingapps.meetingportal.meetComAllg.CaBug;
import de.meetingapps.meetingportal.meetComAllg.CaFehler;
import de.meetingapps.meetingportal.meetComEntities.EclVipKZ;

public class DbVipKZ {
    private Connection verbindung = null;
    //	private DbBasis dbBasis=null;
    private DbBundle dbBundle = null;

    public EclVipKZ vipKZarray[];

    private EclVipKZ vipKZOldVersion = null;

    public DbVipKZ(DbBundle pDbBundle) {
        /* Verbindung in lokale Daten eintragen*/
        if (pDbBundle == null) {
            CaBug.drucke("DbAppTexte.init 001 - dbBundle nicht initialisiert");
            return;
        }
        if (pDbBundle.dbBasis == null) {
            CaBug.drucke("DbAppTexte.init 002 - dbBasis nicht initialisiert");
            return;
        }

        //		dbBasis=pDbBundle.dbBasis;
        verbindung = pDbBundle.dbBasis.verbindung;
        dbBundle = pDbBundle;
        /*TODO _VIPKZ - readInArray wurde aktuell deaktiviert*/
        //		readInArray(); Um gottes willen - wurde bei jeden Open gemacht.
    }

    public int createTable() {
        int rc = 0;
        DbLowLevel lDbLowLevel = new DbLowLevel(dbBundle);
        rc = lDbLowLevel.createTable("CREATE TABLE " + dbBundle.getSchemaAllgemein() + "tbl_vipkz ( "
                + "`kuerzel` char(4) NOT NULL, " + "`beschreibung` varchar(80) DEFAULT NULL, "
                + "`aktion` char(31) DEFAULT NULL, " + "`textErfassenderInternStandard` varchar(2000) DEFAULT NULL, "
                + "`textErfassenderInternPrivilegiert` varchar(2000) DEFAULT NULL, "
                + "`textErfassenderExtern` varchar(2000) DEFAULT NULL, "
                + "`textSelbstbedienung` varchar(2000) DEFAULT NULL, " + "`textVIPMonitor` varchar(2000) DEFAULT NULL, "
                + "`textMCMessage` varchar(2000) DEFAULT NULL, "
                + "`textMCMessageVerteiler` varchar(2000) DEFAULT NULL, "
                + "`textBuehneninformation` varchar(2000) DEFAULT NULL, " + "`textMail` varchar(2000) DEFAULT NULL, "
                + "`textMailVerteiler` varchar(2000) DEFAULT NULL, "
                + "`abbruchZwingendStandard` int(11) DEFAULT NULL, "
                + "`abbruchZwingendPrivilegiert` int(11) DEFAULT NULL, " + "`bedingung` int(11) DEFAULT NULL, "
                + "`aktienzahl` int(11) DEFAULT NULL, " + "`name` varchar(30) DEFAULT NULL, "
                + "`vorname` varchar(30) DEFAULT NULL, " + "PRIMARY KEY (`kuerzel`), "
                + "UNIQUE KEY `kuerzel` (`kuerzel`) " + ") ");
        return rc;
    }

    /** Rückgabe einer Kopie der beim Einlesen zwischengespeicherten "alten" Version*/
    public EclVipKZ getOldVersion() {
        EclVipKZ oldVipKZ = new EclVipKZ();
        vipKZOldVersion.copyTo(oldVipKZ);
        return (oldVipKZ);
    }

    /** "Leert" OldVersion, z.B. für Neuaufnahme*/
    public void clearOldVersion() {
        vipKZOldVersion = new EclVipKZ();
    }

    /** wandel vipKZ.aktion in einen String zum Abspeichern in Datenbank*/
    private String erzeugeStringAktion(EclVipKZ pclVipKZ) {
        String sAktion = "";
        int i;
        for (i = 1; i <= 30; i++) {
            if (pclVipKZ.aktion[i] == 1) {
                sAktion = sAktion + "1";
            } else {
                sAktion = sAktion + "0";
            }
        }
        return sAktion;

    }

    /** dekodiert die aktuelle Position aus ergebnis in EvlVIpKZ und gibt dieses zurück*/
    private EclVipKZ decodeErgebnis(ResultSet ergebnis) {
        EclVipKZ lclVipKZ = new EclVipKZ();

        try {

            lclVipKZ.kuerzel = ergebnis.getString("vk.kuerzel");
            lclVipKZ.beschreibung = ergebnis.getString("vk.beschreibung");
            String hstring;
            char hchar;
            hstring = ergebnis.getString("vk.aktion");
            int i1;

            for (i1 = 0; i1 < 30; i1++) {
                if (hstring != null) {
                    if (hstring.length() > i1) {

                        hchar = hstring.charAt(i1);
                        if (hchar == '1') {
                            lclVipKZ.aktion[i1 + 1] = 1;
                        } else {
                            lclVipKZ.aktion[i1 + 1] = 0;
                        }

                    }
                }

            }

            lclVipKZ.textErfassenderInternStandard = ergebnis.getString("vk.textErfassenderInternStandard");
            lclVipKZ.textErfassenderInternPrivilegiert = ergebnis.getString("vk.textErfassenderInternPrivilegiert");
            lclVipKZ.textErfassenderExtern = ergebnis.getString("vk.textErfassenderExtern");
            lclVipKZ.textSelbstbedienung = ergebnis.getString("vk.textSelbstbedienung");
            lclVipKZ.textVIPMonitor = ergebnis.getString("vk.textVIPMonitor");
            lclVipKZ.textMCMessage = ergebnis.getString("vk.textMCMessage");
            lclVipKZ.textMCMessageVerteiler = ergebnis.getString("vk.textMCMessageVerteiler");
            lclVipKZ.textBuehneninformation = ergebnis.getString("vk.textBuehneninformation");
            lclVipKZ.textMail = ergebnis.getString("vk.textMail");
            lclVipKZ.textMailVerteiler = ergebnis.getString("vk.textMailVerteiler");
            lclVipKZ.abbruchZwingendStandard = ergebnis.getInt("vk.abbruchZwingendStandard");
            lclVipKZ.abbruchZwingendPrivilegiert = ergebnis.getInt("vk.abbruchZwingendPrivilegiert");
            lclVipKZ.bedingung = ergebnis.getInt("vk.bedingung");
            lclVipKZ.aktienzahl = ergebnis.getInt("vk.aktienzahl");
            lclVipKZ.name = ergebnis.getString("vk.name");
            lclVipKZ.vorname = ergebnis.getString("vk.vorname");
        } catch (Exception e) {
            System.err.println(" " + e.getMessage());
        }

        return lclVipKZ;
    }

    /** Einlesen vin vipKZ direkt aus DB*/
    public int read(EclVipKZ vipKZ) {

        EclVipKZ hVipKZ;

        String sql = "SELECT * from " + dbBundle.getSchemaAllgemein() + "tbl_vipKZ vk where vk.kuerzel=?";
        try {

            PreparedStatement pstm1 = verbindung.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
            pstm1.setString(1, vipKZ.kuerzel);
            ResultSet ergebnis = pstm1.executeQuery();

            //			Statement stm=verbindung.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
            //			ResultSet ergebnis=stm.executeQuery(sql);

            if (ergebnis.next() == true) {
                hVipKZ = decodeErgebnis(ergebnis);
                hVipKZ.copyTo(vipKZ);
                vipKZOldVersion = new EclVipKZ();
                vipKZ.copyTo(vipKZOldVersion);

            } else {
                return (-5);
            }

        } catch (Exception e) {
            System.err.println(" " + e.getMessage());
        }

        return (1);
    }

    /**Lesen ein Satz in Richtung Dateiende, beginnend bei vipKZ*/
    public int read_inRichtungEnde(EclVipKZ vipKZ) {
        EclVipKZ hVipKZ;

        String sql = "SELECT * from " + dbBundle.getSchemaAllgemein()
                + "tbl_vipKZ vk where vk.kuerzel>? ORDER BY vk.kuerzel ASC LIMIT 1";
        try {

            PreparedStatement pstm1 = verbindung.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
            pstm1.setString(1, vipKZ.kuerzel);
            ResultSet ergebnis = pstm1.executeQuery();

            //			Statement stm=verbindung.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
            //			ResultSet ergebnis=stm.executeQuery(sql);

            if (ergebnis.next() == true) {
                hVipKZ = decodeErgebnis(ergebnis);
                hVipKZ.copyTo(vipKZ);
                vipKZOldVersion = new EclVipKZ();
                vipKZ.copyTo(vipKZOldVersion);

            } else {
                return (CaFehler.pfLetzterSatzErreicht);
            }

        } catch (Exception e) {
            System.err.println(" " + e.getMessage());
        }

        return (1);

    }

    /**Lesen ein Satz in Richtung Dateianfang, beginnend bei vipKZ*/
    public int read_inRichtungAnfang(EclVipKZ vipKZ) {
        EclVipKZ hVipKZ;

        String sql = "SELECT * from " + dbBundle.getSchemaAllgemein()
                + "tbl_vipKZ vk where vk.kuerzel<? ORDER BY vk.kuerzel DESC LIMIT 1";
        try {

            PreparedStatement pstm1 = verbindung.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
            pstm1.setString(1, vipKZ.kuerzel);
            ResultSet ergebnis = pstm1.executeQuery();

            //			Statement stm=verbindung.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
            //			ResultSet ergebnis=stm.executeQuery(sql);

            if (ergebnis.next() == true) {
                hVipKZ = decodeErgebnis(ergebnis);
                hVipKZ.copyTo(vipKZ);
                vipKZOldVersion = new EclVipKZ();
                vipKZ.copyTo(vipKZOldVersion);

            } else {
                return (CaFehler.pfErsterSatzErreicht);
            }

        } catch (Exception e) {
            System.err.println(" " + e.getMessage());
        }

        return (1);

    }

    /** ****************************************************************************
     * Einfügen eines neuen Satzes.
     */
    public int insert(EclVipKZ pclVipKZ) {

        int erg;

        /* *****Vorabprüfungen durchführen ******* */
        /* zutrittsIdent */
        pclVipKZ.kuerzel = pclVipKZ.kuerzel.trim();
        if (pclVipKZ.kuerzel.isEmpty()) {
            return CaFehler.pfBitteXSchluesselYeingeben;
        }

        try {

            //			/*Felder Neuanlage füllen*/
            //			Statement stm1=verbindung.createStatement();
            //			String sql1="INSERT INTO tbl_vipKZ "+
            //					"("
            //					+ "kuerzel, beschreibung, "
            //					+ "aktion, "
            //					+ "textErfassenderInternStandard, textErfassenderInternPrivilegiert, "
            //					+ "textErfassenderExtern, textSelbstbedienung, "
            //					+ "textVIPMonitor, "
            //					+ "textMCMessage, textMCMessageVerteiler, "
            //					+ "textBuehneninformation, "
            //					+ "textMail, textMailVerteiler,"
            //					+ "abbruchZwingendStandard, abbruchZwingendPrivilegiert, "
            //					+ "bedingung, "
            //					+ "aktienzahl, "
            //					+ "name, vorname"
            //					+ ") "+
            //					
            //					"VALUES ("+
            //					"'"+pclVipKZ.kuerzel+"', '"+pclVipKZ.beschreibung+"', "+
            //					"'"+erzeugeStringAktion(pclVipKZ)+"', "+
            //					"'"+pclVipKZ.textErfassenderInternStandard+"', '"+pclVipKZ.textErfassenderInternPrivilegiert+"', "+
            //					"'"+pclVipKZ.textErfassenderExtern+"', '"+pclVipKZ.textSelbstbedienung+"', "+
            //					"'"+pclVipKZ.textVIPMonitor+"', "+
            //					"'"+pclVipKZ.textMCMessage+"', '"+pclVipKZ.textMCMessageVerteiler+"', "+
            //					"'"+pclVipKZ.textBuehneninformation+"', "+
            //					"'"+pclVipKZ.textMail+"', '"+pclVipKZ.textMailVerteiler+"', "+
            //					Integer.toString(pclVipKZ.abbruchZwingendStandard)+", "+Integer.toString(pclVipKZ.abbruchZwingendPrivilegiert)+", "+
            //					Integer.toString(pclVipKZ.bedingung)+", "+
            //					Integer.toString(pclVipKZ.aktienzahl)+", "+
            //					"'"+pclVipKZ.name+"', '"+pclVipKZ.vorname+"'"+
            //					")";
            //						
            //			
            //			erg=0; /*Falls Duplicate Key, dann wird exception geschmissen und erg wohl nicht gefüllt!*/
            //			erg=stm1.executeUpdate(sql1);
            //		} catch (Exception e2){
            //			System.err.println(" "+e2.getMessage());
            //			erg=0;
            //		}

            /*Felder Neuanlage füllen*/
            String sql1 = "INSERT INTO " + dbBundle.getSchemaAllgemein() + "tbl_vipKZ " + "("
                    + "kuerzel, beschreibung, " + "aktion, "
                    + "textErfassenderInternStandard, textErfassenderInternPrivilegiert, "
                    + "textErfassenderExtern, textSelbstbedienung, " + "textVIPMonitor, "
                    + "textMCMessage, textMCMessageVerteiler, " + "textBuehneninformation, "
                    + "textMail, textMailVerteiler," + "abbruchZwingendStandard, abbruchZwingendPrivilegiert, "
                    + "bedingung, " + "aktienzahl, " + "name, vorname" + ") "
                    + "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

            PreparedStatement pstm1 = verbindung.prepareStatement(sql1);

            fuellePreparedStatementKomplett(pstm1, 1, pclVipKZ);

            erg = 0; /*Falls Duplicate Key, dann wird exception geschmissen und erg wohl nicht gefüllt!*/
            erg = pstm1.executeUpdate();
        } catch (Exception e2) {
            System.err.println(" " + e2.getMessage());
            erg = 0;
        }

        if (erg == 0) {/*Fehler beim Einfügen - d.h. ZutrittsIdent bereits vorhanden*/
            return CaFehler.pfdXyBereitsVorhanden;
        }

        return (1);
    }

    /** ***************Interne Funktion: liefereWhereString ********************************
     * Setzt den where-String für Updates aus vipKZOldVersion zusammen und liefert
     * diesen zurück
     */

    private String liefereWhereString() { /* mit "where" */

        //		String whereString="where "+
        //				"kuerzel='"+vipKZOldVersion.kuerzel+"' AND "+
        //				"beschreibung='"+vipKZOldVersion.beschreibung+"' AND "+
        //				"aktion='"+erzeugeStringAktion(vipKZOldVersion)+"' AND "+
        //				"textErfassenderInternStandard='"+vipKZOldVersion.textErfassenderInternStandard+"' AND "+
        //				"textErfassenderInternPrivilegiert='"+vipKZOldVersion.textErfassenderInternPrivilegiert+"' AND "+
        //				"textErfassenderExtern='"+vipKZOldVersion.textErfassenderExtern+"' AND "+
        //				"textSelbstbedienung='"+vipKZOldVersion.textSelbstbedienung+"' AND "+
        //				"textVIPMonitor='"+vipKZOldVersion.textVIPMonitor+"' AND "+
        //				"textMCMessage='"+vipKZOldVersion.textMCMessage+"' AND "+
        //				"textMCMessageVerteiler='"+vipKZOldVersion.textMCMessageVerteiler+"' AND "+
        //				"textBuehneninformation='"+vipKZOldVersion.textBuehneninformation+"' AND "+
        //				"textMail='"+vipKZOldVersion.textMail+"' AND "+
        //				"textMailVerteiler='"+vipKZOldVersion.textMailVerteiler+"' AND "+
        //				"abbruchZwingendStandard="+Integer.toString(vipKZOldVersion.abbruchZwingendStandard)+" AND "+
        //				"abbruchZwingendPrivilegiert="+Integer.toString(vipKZOldVersion.abbruchZwingendPrivilegiert)+" AND "+
        //				"bedingung="+Integer.toString(vipKZOldVersion.bedingung)+" AND "+
        //				"aktienzahl="+vipKZOldVersion.aktienzahl+" AND "+
        //				"name='"+vipKZOldVersion.name+"' AND "+
        //				"vorname='"+vipKZOldVersion.vorname+"' ";

        String whereString = "where " + "kuerzel=? AND beschreibung=?  AND " + "aktion=? AND "
                + "textErfassenderInternStandard=? AND textErfassenderInternPrivilegiert=? AND "
                + "textErfassenderExtern=? AND textSelbstbedienung=? AND " + "textVIPMonitor=? AND "
                + "textMCMessage=? AND textMCMessageVerteiler=? AND " + "textBuehneninformation=? AND "
                + "textMail=? AND textMailVerteiler=? AND "
                + "abbruchZwingendStandard=? AND abbruchZwingendPrivilegiert=? AND " + "bedingung=? AND "
                + "aktienzahl=? AND " + "name=? AND vorname=? ";

        return (whereString);
    }

    /** Fuellen Prepared Statement mit allen Feldern, beginnend bei offset.
     * Kann sowohl für Insert, als auch für update (2x, mit unterschiedlichen Offsets und
     * unterschiedlichem vipKZ - einmal neue Werte, einmal oldversion -) verwendet werden.
     * 
     * offset= Startposition des ersten Feldes (also z.B. 1)
     */
    private void fuellePreparedStatementKomplett(PreparedStatement pstm, int offset, EclVipKZ pclVipKZ) {

        /*Erstes ? ist Position 1*/

        try {
            pstm.setString(offset + 0, pclVipKZ.kuerzel);
            pstm.setString(offset + 1, pclVipKZ.beschreibung);
            pstm.setString(offset + 2, erzeugeStringAktion(pclVipKZ));
            pstm.setString(offset + 3, pclVipKZ.textErfassenderInternStandard);
            pstm.setString(offset + 4, pclVipKZ.textErfassenderInternPrivilegiert);
            pstm.setString(offset + 5, pclVipKZ.textErfassenderExtern);
            pstm.setString(offset + 6, pclVipKZ.textSelbstbedienung);
            pstm.setString(offset + 7, pclVipKZ.textVIPMonitor);
            pstm.setString(offset + 8, pclVipKZ.textMCMessage);
            pstm.setString(offset + 9, pclVipKZ.textMCMessageVerteiler);
            pstm.setString(offset + 10, pclVipKZ.textBuehneninformation);
            pstm.setString(offset + 11, pclVipKZ.textMail);
            pstm.setString(offset + 12, pclVipKZ.textMailVerteiler);
            pstm.setInt(offset + 13, pclVipKZ.abbruchZwingendStandard);
            pstm.setInt(offset + 14, pclVipKZ.abbruchZwingendPrivilegiert);
            pstm.setInt(offset + 15, pclVipKZ.bedingung);
            pstm.setInt(offset + 16, pclVipKZ.aktienzahl);
            pstm.setString(offset + 17, pclVipKZ.name);
            pstm.setString(offset + 18, pclVipKZ.vorname);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    /** ************************Update eines Satzes *******************************************
     * Vorbedingung: muß über eine read-Funktion eingelesen sein, damit vipKZOldVersion
     * gefüllt ist
     ******************************************************************************************/
    public int update(EclVipKZ pclVipKZ) {

        //		try {
        //			
        //		
        //			Statement stm=verbindung.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
        //			String sql="UPDATE tbl_vipKZ "
        //			+"SET "+
        //			"kuerzel='"+pclVipKZ.kuerzel+"', "+
        //			"beschreibung='"+pclVipKZ.beschreibung+"', "+
        //			"aktion='"+erzeugeStringAktion(pclVipKZ)+"', "+
        //			"textErfassenderInternStandard='"+pclVipKZ.textErfassenderInternStandard+"', "+
        //			"textErfassenderInternPrivilegiert='"+pclVipKZ.textErfassenderInternPrivilegiert+"', "+
        //			"textErfassenderExtern='"+pclVipKZ.textErfassenderExtern+"', "+
        //			"textSelbstbedienung='"+pclVipKZ.textSelbstbedienung+"', "+
        //			"textVIPMonitor='"+pclVipKZ.textVIPMonitor+"', "+
        //			"textMCMessage='"+pclVipKZ.textMCMessage+"', "+
        //			"textMCMessageVerteiler='"+pclVipKZ.textMCMessageVerteiler+"', "+
        //			"textBuehneninformation='"+pclVipKZ.textBuehneninformation+"', "+
        //			"textMail='"+pclVipKZ.textMail+"', "+
        //			"textMailVerteiler='"+pclVipKZ.textMailVerteiler+"', "+
        //			"abbruchZwingendStandard="+Integer.toString(pclVipKZ.abbruchZwingendStandard)+", "+
        //			"abbruchZwingendPrivilegiert="+Integer.toString(pclVipKZ.abbruchZwingendPrivilegiert)+", "+
        //			"bedingung="+Integer.toString(pclVipKZ.bedingung)+", "+
        //			"aktienzahl="+pclVipKZ.aktienzahl+", "+
        //			"name='"+pclVipKZ.name+"', "+
        //			"vorname='"+pclVipKZ.vorname+"' "
        //					+ this.liefereWhereString();
        //			
        //			int ergebnis1=stm.executeUpdate(sql);
        //			if (ergebnis1==0){
        //				return (-6);}
        //			} catch (Exception e1){
        //				System.err.println(" "+e1.getMessage());
        //				
        //			}

        try {

            String sql = "UPDATE " + dbBundle.getSchemaAllgemein() + "tbl_vipKZ SET "
                    + "kuerzel=?, beschreibung=?, aktion=?, "
                    + "textErfassenderInternStandard=?, textErfassenderInternPrivilegiert=?,"
                    + "textErfassenderExtern=?, textSelbstbedienung=?, " + "textVIPMonitor=?, "
                    + "textMCMessage=?, textMCMessageVerteiler=?, " + "textBuehneninformation=?, "
                    + "textMail=?, textMailVerteiler=?, " + "abbruchZwingendStandard=?, abbruchZwingendPrivilegiert=?, "
                    + "bedingung=?, " + "aktienzahl=?, " + "name=?, vorname=? " + this.liefereWhereString();

            PreparedStatement pstm1 = verbindung.prepareStatement(sql);
            fuellePreparedStatementKomplett(pstm1, 1, pclVipKZ); /*SET-Teil*/
            fuellePreparedStatementKomplett(pstm1, 20, vipKZOldVersion); /*Where-Teil*/

            int ergebnis1 = pstm1.executeUpdate();
            if (ergebnis1 == 0) {
                return (-6);
            }
        } catch (Exception e1) {
            System.err.println(" " + e1.getMessage());

        }

        return (1);
    }

    /** ************************Delete eines Satzes *******************************************
     * Vorbedingung: muß über eine read-Funktion eingelesen sein, damit vipKZOldVersion
     * gefüllt ist
     ******************************************************************************************/
    public int delete(EclVipKZ pclVipKZ) {

        String sql;

        /*Transaktion beginnen*/
        dbBundle.dbBasis.beginTransaction();

        /*Prüfen, ob Kürzel meldungen zugeordnet*/
        /*TODO _VIPKZ*/
        sql = "SELECT * from " + dbBundle.getSchemaAllgemein() + "tbl_meldungenVipKZ vk where vk.vipKZKuerzel=?";
        try {

            PreparedStatement pstm1 = verbindung.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
            pstm1.setString(1, pclVipKZ.kuerzel);

            //			Statement stm=verbindung.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
            ResultSet ergebnis = pstm1.executeQuery();
            if (ergebnis.next() == true) {
                dbBundle.dbBasis.endTransaction();
                return (CaFehler.pfLoeschenNichtMoeglichSaetzeNochZugeordnet);
            }
        } catch (Exception e) {
            System.err.println(" " + e.getMessage());
        }

        /*Ab hier: Löschen wird durchgeführt*/

        /*Löschen von "ausgeschlossenen" aus entsprechendem Table*/
        sql = "DELETE from " + dbBundle.getSchemaAllgemein() + "tbl_VipKZAllgemeinAusschluesse where kuerzel=?";
        try {
            PreparedStatement pstm1 = verbindung.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
            pstm1.setString(1, pclVipKZ.kuerzel);

            //			Statement stm=verbindung.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            /*int ergebnis1=*/pstm1.executeUpdate();
        } catch (Exception e1) {
            System.err.println(" " + e1.getMessage());

        }

        /*Löschen des VIP-Kennzeichens selbst*/
        sql = "DELETE from " + dbBundle.getSchemaAllgemein() + "tbl_VipKZ " + this.liefereWhereString();
        try {
            PreparedStatement pstm1 = verbindung.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
            fuellePreparedStatementKomplett(pstm1, 1, vipKZOldVersion); /*Where-Teil*/

            //			Statement stm=verbindung.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            //			int ergebnis1=stm.executeUpdate(sql);
            int ergebnis1 = pstm1.executeUpdate();

            if (ergebnis1 == 0) {
                dbBundle.dbBasis.rollbackTransaction();
                dbBundle.dbBasis.endTransaction();
                return (CaFehler.pfXyWurdeVonAnderemBenutzerVeraendert);
            }
        } catch (Exception e1) {
            System.err.println(" " + e1.getMessage());

        }

        /*Transaktion beenden*/
        dbBundle.dbBasis.endTransaction();

        return (1);
    }

    /*****Ab hier: "dauerhaften" Array verwalten***************************************/

    public int readFromArray(EclVipKZ vipKZ) {
        int i;
        for (i = 0; i < vipKZarray.length; i++) {
            if (vipKZarray[i].kuerzel.equals(vipKZ.kuerzel)) {
                vipKZ.beschreibung = vipKZarray[i].beschreibung;
                return (1);
            }
        }

        return (0);
    }

    /** Selektieren aller VipKZ die in Array gespeichert sind*/
    public void selectAllArray() {
        int i;
        for (i = 0; i < vipKZarray.length; i++) {
            vipKZarray[i].selektiert = 1;
        }

    }

    /** De-Selektieren aller VipKZ die in Array gespeichert sind*/
    public void deselectAllArray() {
        int i;
        for (i = 0; i < vipKZarray.length; i++) {
            vipKZarray[i].selektiert = 0;
        }

    }

    public void readInArray() {

        try {

            Statement stm = verbindung.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            String sql = "SELECT * from " + dbBundle.getSchemaAllgemein() + "tbl_vipKZ vk";
            ResultSet ergebnis = stm.executeQuery(sql);
            ergebnis.last();
            int anz = ergebnis.getRow();
            ergebnis.beforeFirst();

            vipKZarray = new EclVipKZ[anz];

            int i = 0;
            while (ergebnis.next() == true) {

                EclVipKZ vipKZ = decodeErgebnis(ergebnis);

                vipKZarray[i] = vipKZ;
                i++;

            }

        } catch (Exception e) {
            System.err.println(" " + e.getMessage());
        }
        return;

    }

}
