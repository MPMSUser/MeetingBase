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
import java.util.ArrayList;
import java.util.List;

import de.meetingapps.meetingportal.meetComAllg.CaBug;
import de.meetingapps.meetingportal.meetComAllg.CaDatumZeit;
import de.meetingapps.meetingportal.meetComAllg.CaFehler;
import de.meetingapps.meetingportal.meetComAllg.CaString;
import de.meetingapps.meetingportal.meetComBl.BlAktienregisterNummerAufbereiten;
import de.meetingapps.meetingportal.meetComBl.BlMonitorVipKZ;
import de.meetingapps.meetingportal.meetComEntities.EclMeldung;
import de.meetingapps.meetingportal.meetComEntities.EclMeldungVipKZ;
import de.meetingapps.meetingportal.meetComEntities.EclPersonenNatJur;

/*TODO _DBKlassenKonsolidierung Close nicht vollständig!*/

public class DbMeldungen {
    private Statement batchstatement;

    private EclMeldung VclMeldungOldVersion;

    /*Array für read-Kommando*/
    @Deprecated
    public EclMeldung meldungenreadarray[];
    @Deprecated
    public int AnzMeldungenInReadArray;

    /* *********************************************************************************************************
     * Ab hier: "offizielle Methoden" für McdbMeldungen, "zweiter Versuch" - i.d.R. wird gefundene
     * Meldung im übergebenen Parameter EclMeldung zurückgegeben - mittlerweile auch nicht mehr langfristig zu
     * verwenden!
     * *********************************************************************************************************
     */

    /** Fuellen Prepared Statement "WHERE"-Klausel*
     * -2 => ab 76 (d.h. vorher voll gefüllt)*/
    private void fuellePreparedStatementWhere(PreparedStatement pstm, int pOffset, EclMeldung VclMeldung) {
        int offset = pOffset;
        if (offset == -2) {
            offset = this.anzfelder + 1;
        }
        try {
            pstm.setInt(offset, VclMeldung.meldungsIdent);
            offset++;
            pstm.setLong(offset, VclMeldung.db_version - 1);
            offset++;
            pstm.setInt(offset, dbBundle.clGlobalVar.mandant);
            offset++;
            pstm.setInt(offset, VclMeldung.klasse);
            offset++;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        offset++;

    }

    //	/***************Interne Read-Routine, Ergebnis in VMclMeldung ablegen; mit Basis-Infos**********************
    //	 * ehemals read_selectedString*/
    //	@Deprecated
    //	private int readold_PreparedStatement(PreparedStatement pstm, EclMeldung VMclMeldung){
    //
    //		EclMeldung hMeldung;
    //		
    //		try {
    ////			Statement stm=verbindung.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
    ////			ResultSet ergebnis=stm.executeQuery(SelectString);
    //			ResultSet ergebnis=pstm.executeQuery();
    //
    //			if (ergebnis.next()==true){
    //				
    //				hMeldung=decodeErgebnis(ergebnis);hMeldung.copyTo(VMclMeldung);
    //				VclMeldungOldVersion=new EclMeldung();
    //				VMclMeldung.copyTo(VclMeldungOldVersion);
    //				if (ergebnis.next()==true){/*Dann Ergebnis nicht eindeutig => Fehlermeldung*/
    //					return (CaFehler.pfNichtEindeutig);
    //				}
    //			
    //			}
    //			else{return (CaFehler.pfXyNichtVorhanden);}
    //				
    //		} catch (Exception e){
    //			System.err.println(" "+e.getMessage());
    //		}
    //		
    //
    //		/* **********An hier: "Zusatzinformationen" aus anderen Tables/Sätzen einlesen****************/
    //		if (VMclMeldung.meldungsIdentVorgaenger!=0){/* Vorgänger bzgl. ersetzte Eintrittskarten einlesen*/
    //			
    //			try {
    //
    //				String sql="SELECT zutrittsIdent, zutrittsIdentVers from "+dbBundle.getSchemaMandant()+"tbl_meldungen "+
    //						"where meldungsIdent=? and mandant=?";
    //				PreparedStatement pstm1=verbindung.prepareStatement(sql,ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
    //				pstm1.setInt(1, VMclMeldung.meldungsIdentVorgaenger);
    //				pstm1.setInt(2, dbBundle.clGlobalVar.mandant);
    //				ResultSet ergebnis=pstm1.executeQuery();
    //
    //				if (ergebnis.next()==true){
    //					VMclMeldung.zutrittsIdentVorgaenger=ergebnis.getString("zutrittsIdent");
    //					VMclMeldung.zutrittsIdentVersVorgaenger=ergebnis.getInt("zutrittsIdentVers");
    //
    //				}
    //				else{return (0);}
    //
    //			} catch (Exception e){
    //				System.err.println(" "+e.getMessage());
    //			}
    //		}
    //
    //
    //		if (VMclMeldung.meldungsIdentNachfolger!=0){/* Nachfolger bzgl. ersetzte Eintrittskarten einlesen*/
    //			
    //			try {
    //				
    //				String sql="SELECT zutrittsIdent, zutrittsIdentVers from "+dbBundle.getSchemaMandant()+"tbl_meldungen "+
    //						"where meldungsIdent=? and mandant=?";
    //				PreparedStatement pstm1=verbindung.prepareStatement(sql,ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
    //				pstm1.setInt(1, VMclMeldung.meldungsIdentNachfolger);
    //				pstm1.setInt(2, dbBundle.clGlobalVar.mandant);
    //				ResultSet ergebnis=pstm1.executeQuery();
    //
    //				if (ergebnis.next()==true){
    //					VMclMeldung.zutrittsIdentNachfolger=ergebnis.getString("zutrittsIdent");
    //					VMclMeldung.zutrittsIdentVersNachfolger=ergebnis.getInt("zutrittsIdentVers");
    //
    //				}
    //				else{return (0);}
    //					
    //			} catch (Exception e){
    //				System.err.println(" "+e.getMessage());
    //			}
    //		}
    //
    //
    //		
    //		/*Präsenzveränderungen*/
    //		/*TODO -Deprecated Funktioniert so nicht. Zu unterscheiden, ob Gast oder Aktionär!*/
    //		dbBundle.dbWillenserklaerung.leseZuMeldung(VMclMeldung);
    //		VMclMeldung.praesenzveraenderungarray=dbBundle.dbWillenserklaerung.willenserklaerungArray;
    //
    //		/*Meldungsausstellungsgrund*/
    //		VMclMeldung.meldungausstellungsgrundarray=dbBundle.dbMeldungAusstellungsgrund.leseZuMeldung(VMclMeldung);
    //		/*VipKZ*/
    //		VMclMeldung.meldungVipKZarray=dbBundle.dbMeldungVipKZ.leseZuMeldung(VMclMeldung);
    //		/*zuMeldungen*/
    //		VMclMeldung.zuMeldungenarray=dbBundle.dbMeldungenMeldungen.leseZuMeldungen(VMclMeldung);
    //		/*vonMeldungen*/
    //		VMclMeldung.vonMeldungenarray=dbBundle.dbMeldungenMeldungen.leseVonMeldungen(VMclMeldung);
    //		/*Aenderungslog*/
    //
    //		EclAenderungslog logselektion=new EclAenderungslog();
    //		logselektion.ident=Integer.toString(VMclMeldung.meldungsIdent);
    //		logselektion.tabelle=EnAenderungslogTabelle.toEntity(EnAenderungslogTabelle.meldungen);
    //		VMclMeldung.aenderungslogarray=dbBundle.dbAenderungslog.leseAenderungsLog(logselektion);
    //		
    //
    //		return (1);
    //	
    //	}

    //	/* ****************************************************************************
    //	 * Einlesen eines Satzes, Sucher über clMeldung.zutrittsIdent und clMeldung.klasse
    //	 * 
    //	 * Es werden nur "nicht-versionierte" zutrittsIdent gefunden!
    //	 * 
    //	 * Suchfeld wird auf richtiges Format abgeprüft, ggf. returncode <1 => Fehler im Suchfeld,
    //	 * oder nicht gefunden etc.
    //	 */
    //	@Deprecated
    //	public int read_ZutrittsIdentGast(EclMeldung VMclMeldung){
    //		VMclMeldung.klasse=0;
    //		return (read_ZutrittsIdent(VMclMeldung));
    //	}

    //	public int read_ZutrittsIdentKlasse(EclMeldung VMclMeldung){
    //		/*Feld leer?*/
    //		VMclMeldung.zutrittsIdent.trim();
    //		if (VMclMeldung.zutrittsIdent.isEmpty()){return (-2);}
    //		
    //		/*Feld auf richtiges Format prüfen*/
    //		int erg=0;
    //		
    //		switch (VMclMeldung.klasse)
    //		{
    //			case 1:erg=lDbBasis.pruefeZutrittsIdentAktionaer(VMclMeldung.zutrittsIdent);break;
    //			case 0:erg=lDbBasis.pruefeZutrittsIdentGast(VMclMeldung.zutrittsIdent);break;
    //		}
    //
    //		
    //		if (erg<1){return (erg);}
    //
    //		
    //		String sql="SELECT * from tbl_meldungen m where m.zutrittsIdent=? "+
    //				" and m.mandant=? and m.klasse=?";
    //		PreparedStatement pstm1=null;
    //		try {
    //			pstm1 = verbindung.prepareStatement(sql,ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
    //			pstm1.setString(1, VMclMeldung.zutrittsIdent);
    //			pstm1.setInt(2, lDbBundle.globalVar.mandant);
    //			pstm1.setInt(3, VMclMeldung.klasse);
    //		} catch (SQLException e) {
    //			e.printStackTrace();
    //		}
    //
    //		return (readold_PreparedStatement(pstm1, VMclMeldung));
    //		
    //	}	

    //	/* ****************************************************************************
    //	 * Einlesen eines Satzes, Sucher über clMeldung.zutrittsIdent
    //	 * 
    //	 * Es werden nur "nicht-versionierte" zutrittsIdent gefunden!
    //	 * 
    //	 * Suchfeld wird NICHT! auf richtiges Format abgeprüft!
    //	 * 
    //	 * Das Ergebnis ist möglicherweise nicht eindeutig (wenn Gäste und Aktionäre
    //	 * einen nicht-separaten Nummernkreis haben), in diesem Fall
    //	 * Fehlermeldung
    //	 */
    //	public int read_ZutrittsIdent(EclMeldung VMclMeldung){
    //		/*Feld leer?*/
    //		VMclMeldung.zutrittsIdent=VMclMeldung.zutrittsIdent.trim();
    //		if (VMclMeldung.zutrittsIdent.isEmpty()){return (-2);}
    //		
    //		String sql="SELECT * from "+dbBundle.getSchemaMandant()+"tbl_meldungen m where m.zutrittsIdent=? "+
    //				" and m.mandant=?";
    //		PreparedStatement pstm1=null;
    //		try {
    //			pstm1 = verbindung.prepareStatement(sql,ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
    //			pstm1.setString(1, VMclMeldung.zutrittsIdent);
    //			pstm1.setInt(2, dbBundle.clGlobalVar.mandant);
    //		} catch (SQLException e) {
    //			e.printStackTrace();
    //		}
    //		return (readold_PreparedStatement(pstm1, VMclMeldung));
    //		
    //	}	

    //	/* ****************************************************************************
    //	 * Einlesen eines Satzes, Sucher über clMeldung.stimmkarte
    //	 * 
    //	 * Suchfeld wird NICHT! auf richtiges Format abgeprüft!
    //	 * 
    //	 */
    //	public int read_Stimmkarte(EclMeldung VMclMeldung){
    //		/*Feld leer?*/
    //		VMclMeldung.stimmkarte=VMclMeldung.stimmkarte.trim();
    //		if (VMclMeldung.stimmkarte.isEmpty()){return (-2);}
    //		
    //		String sql="SELECT * from "+dbBundle.getSchemaMandant()+"tbl_meldungen m where m.stimmkarte=? "+
    //				" and m.mandant=?";
    //		PreparedStatement pstm1=null;
    //		try {
    //			pstm1 = verbindung.prepareStatement(sql,ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
    //			pstm1.setString(1, VMclMeldung.stimmkarte);
    //			pstm1.setInt(2, dbBundle.clGlobalVar.mandant);
    //		} catch (SQLException e) {
    //			e.printStackTrace();
    //		}
    //		return (readold_PreparedStatement(pstm1, VMclMeldung));
    //		
    //	}	
    //

    //	/* ****************************************************************************
    //	 * Einlesen eines Satzes, Sucher über clMeldung.stimmkarteSecond
    //	 * 
    //	 * Suchfeld wird NICHT! auf richtiges Format abgeprüft!
    //	 * 
    //	 */
    //	
    //	
    //	public int read_StimmkarteSecond(EclMeldung VMclMeldung){
    //		/*Feld leer?*/
    //		VMclMeldung.stimmkarteSecond=VMclMeldung.stimmkarteSecond.trim();
    //		if (VMclMeldung.stimmkarteSecond.isEmpty()){return (-2);}
    //		
    //		String sql="SELECT * from "+dbBundle.getSchemaMandant()+"tbl_meldungen m where m.stimmkarteSecond=? "+
    //				" and m.mandant=?";
    //		PreparedStatement pstm1=null;
    //		try {
    //			pstm1 = verbindung.prepareStatement(sql,ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
    //			pstm1.setString(1, VMclMeldung.stimmkarteSecond);
    //			pstm1.setInt(2, dbBundle.clGlobalVar.mandant);
    //		} catch (SQLException e) {
    //			e.printStackTrace();
    //		}
    //		return (readold_PreparedStatement(pstm1, VMclMeldung));
    //		
    //	}	
    //

    //	
    //	/** *************** reines Einlesen eines Satzes über MeldungsIdent**************************
    //	 * Unabhängig von Versionierung/Stornierung etc. - Satz wird immer geliefert!
    //	 * Keine Pufferung, keine OldVersion etc.
    //	 * */
    //	public int readraw_MeldungsIdent(EclMeldung VMclMeldung){
    //		
    //		
    //		String sql="SELECT * from "+dbBundle.getSchemaMandant()+"tbl_meldungen m where m.meldungsIdent=? "+
    //				" and m.mandant=?";
    //		
    //		PreparedStatement pstm1=null;
    //		try {
    //			pstm1 = verbindung.prepareStatement(sql,ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
    //			pstm1.setInt(1, VMclMeldung.meldungsIdent);
    //			pstm1.setInt(2, dbBundle.clGlobalVar.mandant);
    //		} catch (SQLException e) {
    //			e.printStackTrace();
    //		}
    //
    //		
    //		try {
    //			ResultSet ergebnis=pstm1.executeQuery();
    //
    //			if (ergebnis.next()==true){
    //				EclMeldung hMeldung;
    //				hMeldung=decodeErgebnis(ergebnis);hMeldung.copyTo(VMclMeldung);
    //				}
    //			else{return (-5);}
    //				
    //		} catch (Exception e){
    //			System.err.println(" "+e.getMessage());
    //		}
    //		
    //		return (1);
    //	}

    //	@Deprecated
    //	public int readraw_ZutrittsIdentGast(EclMeldung VMclMeldung){
    //		VMclMeldung.klasse=0;
    //		return (readraw_ZutrittsIdent(VMclMeldung));
    //	}

    //	/** *************** reines Einlesen eines Satzes über ZutrittsIdent**************************
    //	 * Unabhängig von Versionierung/Stornierung etc. - Satz wird immer geliefert!
    //	 * Keine Pufferung, keine OldVersion etc..
    //	 * Nur die gültige ZutrittsIdent wird geliefert
    //	 * */
    //	public int readraw_ZutrittsIdent(EclMeldung VMclMeldung){
    //		
    //		String sql="SELECT * from "+dbBundle.getSchemaMandant()+"tbl_meldungen m where m.zutrittsIdent=?" +
    //				" and m.mandant=? and klasse=? and m.zutrittsIdentVers=0";
    //
    //		PreparedStatement pstm1=null;
    //		try {
    //			pstm1 = verbindung.prepareStatement(sql,ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
    //			pstm1.setString(1, VMclMeldung.zutrittsIdent);
    //			pstm1.setInt(2, dbBundle.clGlobalVar.mandant);
    //			pstm1.setInt(3, VMclMeldung.klasse);
    //		} catch (SQLException e) {
    //			e.printStackTrace();
    //		}
    //
    //		
    //		
    //		try {
    //			ResultSet ergebnis=pstm1.executeQuery();
    //
    //			if (ergebnis.next()==true){
    //					EclMeldung hMeldung;
    //				hMeldung=decodeErgebnis(ergebnis);hMeldung.copyTo(VMclMeldung);
    //			}
    //			else{return (-5);}
    //				
    //		} catch (Exception e){
    //			System.err.println(" "+e.getMessage());
    //		}
    //		
    //		return (1);
    //	}
    //	

    /** ***************Interne Funktion: liefereWhereString ********************************
     * Setzt den where-String für Updates zusammen und liefert
     * diesen zurück
     */

    private String liefereWhereString() { /* mit "where" */

        //		Integer oSgruppe=VclMeldungOldVersion.gruppe;
        //		Integer oSkommunikationssprache=VclMeldungOldVersion.kommunikationssprache;
        //		Integer oSmeldungsIdent=VclMeldungOldVersion.meldungsIdent;
        //		Integer oSmeldungsIdentVorgaenger=VclMeldungOldVersion.meldungsIdentVorgaenger;
        //		Integer oSmeldungsIdentNachfolger=VclMeldungOldVersion.meldungsIdentNachfolger;
        //		Integer oSmandant=lDbBundle.mandant;
        //		Integer oSmeldungAktiv=VclMeldungOldVersion.meldungAktiv;
        //		Integer oSzutrittsIdentVers=VclMeldungOldVersion.zutrittsIdentVers;
        //		Integer oSklasse=1;
        //		Integer oSanrede=VclMeldungOldVersion.anrede;
        //		Integer oSzusatzrechte=VclMeldungOldVersion.zusatzrechte;

        String whereString = "WHERE " + "meldungsIdent=? AND db_version=? AND mandant=? AND klasse=?";

        return (whereString);
    }

    //	/* ************************************************************
    //	 * Freigeben der zutrittsIdent, die in VMclMeldung gespeichert ist
    //	 */
    //	public int freigebenzutrittsIdent(EclMeldung VMclMeldung){
    //	
    //		/*Vorgehen:
    //		 * zutrittsIdentVers auf 1 setzen.
    //		 * Dann versuchen upzudaten
    //		 * Wenn Duplicate key, dann zutrittsIdentVers erhöhen, nochmal versuchen usw.
    //		 * Wenn update erfolgreich, dann return
    //		 * wenn kein Update möglich, aber auch kein Duplicate Key, dann wurde Satz verändert
    //		 */
    //		
    //		int zutrittsIdentVers=0;
    //		int update=0;
    //		while (update==0){
    //			zutrittsIdentVers++;
    //
    //			try {
    //				String sql="UPDATE "+dbBundle.getSchemaMandant()+"tbl_meldungen "
    //						+"SET zutrittsIdentVers=? geaendertDatumUhrzeit= ? "
    //						+ this.liefereWhereString();
    //
    //				PreparedStatement pstm1=verbindung.prepareStatement(sql,ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
    //				pstm1.setInt(1, zutrittsIdentVers);
    //				pstm1.setString(2, CaDatumZeit.DatumZeitStringFuerDatenbank());
    //				fuellePreparedStatementWhere(pstm1,3,VMclMeldung); /*Where-Teil*/
    //
    //
    //				int ergebnis1=pstm1.executeUpdate();
    //				if (ergebnis1==0){
    //					update=1;return (-6);}
    //				if (ergebnis1==1){update=1;}
    //			} catch (Exception e1){
    //				System.err.println(" "+e1.getMessage());
    //
    //			}
    //		}
    //		
    //		/*Aenderungslog schreiben*/
    //		EclAenderungslog aenderungsLog=new EclAenderungslog();
    //		aenderungsLog.tabelle=EnAenderungslogTabelle.toEntity(EnAenderungslogTabelle.meldungen);
    //		aenderungsLog.ident=Integer.toString(VMclMeldung.meldungsIdent);
    //		aenderungsLog.aktion=EnAenderungslogAktion.toEntity(EnAenderungslogAktion.freigabeZutrittsIdent);
    //		aenderungsLog.feld="Zutrittsidentifikation";
    //		aenderungsLog.alt=VclMeldungOldVersion.zutrittsIdent;
    //		aenderungsLog.neu="("+VMclMeldung.zutrittsIdent+")."+Integer.toString(zutrittsIdentVers);
    //		dbBundle.dbAenderungslog.insert(aenderungsLog);
    //
    //		
    //		
    //		
    //		return (1);
    //	}

    //	/* ***************Ersetzen einer zutrittsIdent**************************************
    //	 * Ablauf:
    //	 * Grundsätzliches:
    //	 * > Alles innerhalb einer Transaktion!
    //	 * > Es wird ein neuer meldungen-Satz erstellt, in den die bisherige Eintrittskarte nebst
    //	 * 		Grunddaten eingetragen wird
    //	 * > dieser neue meldungen-Satz wird ggf. zwischen aktuellem Satz und bereits vorhandenen
    //	 * 		früheren "Verkettungen" eingereiht.
    //	 * > VMclMeldung.zutrittsIdent enthält die neue ZutrittsIdentifikation, die beim Ersetzen
    //	 * 		zwangsweise mit vergeben werden muß. Kann aber leer sein (für spätere Aktionärs-Meldungen ...)
    //	 * 
    //	 * Detailablauf
    //	 * > Holen einer neuen InterneIdent
    //	 * > Prüfen bisherige Vorgänger-InterneIdent vorhanden? Wenn ja, dann muß später umgehängt werden,
    //	 * 		und die alte erst zwischengespeichert werden
    //	 * > Update von VMclMeldung mit "Vorgänger=die neue InterneIdent" und neuer zutrittsIdent
    //	 * 		- Falls der Update nicht möglich, wurde Satz verändert => Abbruch
    //	 * 		- falls Duplicate key, dann ist neue zutrittsIdent schon vorhanden => Abbruch
    //	 * > "alte" clMeldungsdaten 
    //	 * 			- mit neuer InterneIdent 
    //	 * 			- alter zutrittsIdent (aus "oldVersion")
    //	 * 			- alter VorgängerIdent (aus "oldVersion") 
    //	 * 			- nachfolgerIdent mit Verweis auf "aktiven" Satz
    //	 * 		 	- meldungAktiv auf "stornierte ZutrittsIdent" setzen
    //	 * 		einfügen
    //	 * > Falls bisherige Vorgänger-InterneIdent vorhanden:
    //	 * 		- update bisherigen Vorgänger-InterneIdent-Satz dort den Nachfolger-InterneIdent auf
    //	 * 			den neu angestellten Satz
    //	 * */

    //	@Deprecated
    //	public int ersetzenzutrittsIdentGast(EclMeldung VMclMeldung){
    //		VMclMeldung.klasse=0;
    //		return (ersetzenzutrittsIdent(VMclMeldung));
    //	}

    //	public int ersetzenzutrittsIdent(EclMeldung VMclMeldung){
    //	
    //		/* *****Vorabprüfungen durchführen ******* */
    //		/* zutrittsIdent */
    //		VMclMeldung.zutrittsIdent.trim();
    //		if (!VMclMeldung.zutrittsIdent.isEmpty()){
    //			int erg=0;
    //			switch (VMclMeldung.klasse)
    //			{
    //				case 1:erg=lDbBasis.pruefeZutrittsIdentAktionaer(VMclMeldung.zutrittsIdent);break;
    //				case 0:erg=lDbBasis.pruefeZutrittsIdentGast(VMclMeldung.zutrittsIdent);break;
    //			}
    //
    //			if (erg<1){return (erg);}
    //		}
    //
    //		
    //		lDbBasis.beginTransaction();
    //
    //		/*Jetzige meldungsIdentVorgaenger zwischenspeichern - für den Fall dass Umhängen erforderlich*/
    //		Integer jetzigemeldungsIdentVorgaenger=VMclMeldung.meldungsIdentVorgaenger;
    //		
    //		Integer neuemeldungsIdent=lDbBasis.getInterneIdentMeldungen();
    //		
    //		/*****Update des aktuellen Satzes - mit neuer meldungsIdentVorgaenger und neuer zutrittsIdent****/
    //		
    //		/*Falls neue zutrittsIdent = alter zutrittsIdent => Fehler*/
    //		if (VMclMeldung.zutrittsIdent.equals(VclMeldungOldVersion.zutrittsIdent)){
    //			lDbBasis.rollbackTransaction();
    //			lDbBasis.endTransaction();
    //			return (-1);
    //		}
    //		/*Falls zutrittsIdent leer: neue zutrittsIdent vergeben*/
    //		if (VMclMeldung.zutrittsIdent.isEmpty()){
    //			int erg=0;
    //			switch (VMclMeldung.klasse)
    //			{
    //				case 1:erg=lDbBasis.pruefeZutrittsIdentAktionaer(VMclMeldung.zutrittsIdent);break;
    //				case 0:erg=lDbBasis.pruefeZutrittsIdentGast(VMclMeldung.zutrittsIdent);break;
    //			}
    //			
    //			if (erg<1){
    //				lDbBasis.rollbackTransaction();
    //				lDbBasis.endTransaction();
    //				return (erg);
    //			}
    //			VMclMeldung.zutrittsIdent=Integer.toString(erg);
    //		}
    //		else{
    //			/*zutrittsIdent nicht leer - d.h. prüfen, ob manuell vergebene höher als "letzte vergebene" ist
    //			 * ggf. updaten
    //			 */
    //			int erg=0;
    //			switch (VMclMeldung.klasse)
    //			{
    //				case 1:erg=lDbBasis.updateLetzteZutrittsIdentAktionaer(VMclMeldung.zutrittsIdent);break;
    //				case 0:erg=lDbBasis.updateLetzteZutrittsIdentGast(VMclMeldung.zutrittsIdent);break;
    //			}
    //
    //			
    //		}
    //		
    //		
    //		try {
    //			String sql="UPDATE tbl_meldungen "
    //					+"SET "+
    //					"meldungsIdentVorgaenger=?, "+
    //					"zutrittsIdent=?, geaendertDatumUhrzeit=? "
    //					+ this.liefereWhereString();
    //			
    //			PreparedStatement pstm1=verbindung.prepareStatement(sql,ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
    //			pstm1.setInt(1, neuemeldungsIdent);
    //			pstm1.setString(2, VMclMeldung.zutrittsIdent);
    //			pstm1.setString(3, CaDatumZeit.DatumZeitStringFuerDatenbank());
    //			fuellePreparedStatementWhere(pstm1,4,VMclMeldung); /*Where-Teil*/
    //			int ergebnis1=pstm1.executeUpdate();
    //			if (ergebnis1==0){/*Dann wurde Datensatz mittlerweile verändert!*/
    //				lDbBasis.rollbackTransaction();lDbBasis.endTransaction();return (-6);}
    //		} catch (Exception e1){
    //			System.err.println(" "+e1.getMessage());
    //			/*Duplicate Key -> neue zutrittsIdent schon vorhanden, Vorgang nicht durchführbar*/
    //			/*Rollback durchführen*/
    //			lDbBasis.rollbackTransaction();lDbBasis.endTransaction();
    //			return (-1);
    //
    //
    //		}
    //
    //
    //		
    //		/* Kopieren der "alten" clMeldungsdaten (oldVersion) auf die neue InterneIdent und Insert 
    //		 * > "alte" clMeldungsdaten 
    //		 * 			- mit neuer InterneIdent 
    //		 * 			- alter zutrittsIdent (aus "oldVersion")
    //		 * 			- alter VorgängerIdent (aus "oldVersion") 
    //		 * 			- nachfolgerIdent mit Verweis auf "aktiven" Satz
    //		 * 			- meldungAktiv auf "stornierte ZutrittsIdent" setzen
    //		 * 		einfügen
    //		*/
    //		EclMeldung zSpeicher=new EclMeldung(); /*Zum zwischenspeichern*/
    //		zSpeicher.meldungsIdent=VMclMeldung.meldungsIdent;
    //		zSpeicher.zutrittsIdent=VMclMeldung.zutrittsIdent;
    //		zSpeicher.meldungsIdentVorgaenger=VMclMeldung.meldungsIdentVorgaenger;
    //		zSpeicher.meldungsIdentNachfolger=VMclMeldung.meldungsIdentNachfolger;
    //		zSpeicher.meldungAktiv=VMclMeldung.meldungAktiv;
    //		zSpeicher.klasse=VMclMeldung.klasse;
    //		
    //		VMclMeldung.meldungsIdent=neuemeldungsIdent;
    //		VMclMeldung.zutrittsIdent=VclMeldungOldVersion.zutrittsIdent;
    //		VMclMeldung.meldungsIdentNachfolger=zSpeicher.meldungsIdent;
    //		VMclMeldung.meldungAktiv=3;
    //		
    //		int erg=this.insert_old(VMclMeldung);
    //		if (erg!=1){
    //			lDbBasis.rollbackTransaction();
    //			lDbBasis.endTransaction();
    //			return (0);
    //		}
    //		VMclMeldung.meldungsIdent=zSpeicher.meldungsIdent;
    //		VMclMeldung.zutrittsIdent=zSpeicher.zutrittsIdent;
    //		VMclMeldung.meldungsIdentVorgaenger=neuemeldungsIdent;
    //		VMclMeldung.meldungsIdentNachfolger=0;
    //		VMclMeldung.meldungAktiv=zSpeicher.meldungAktiv;
    //
    //		 /* > Falls bisherige Vorgänger-InterneIdent vorhanden:
    //			 * 		- update bisherigen Vorgänger-InterneIdent-Satz dort den Nachfolger-InterneIdent auf
    //			 * 			den neu angestellten Satz
    //			 * */
    //		
    //
    //		if (jetzigemeldungsIdentVorgaenger!=0){
    //			try {
    //				Statement stm=verbindung.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
    //				String sql="UPDATE tbl_meldungen "
    //						+"SET meldungsIdentNachfolger=? "
    //						+ "where meldungsIdent=? AND mandant=?";
    //
    //				PreparedStatement pstm1=verbindung.prepareStatement(sql);
    //				pstm1.setInt(1, neuemeldungsIdent);
    //				pstm1.setInt(2, jetzigemeldungsIdentVorgaenger);
    //				pstm1.setInt(3, VclMeldungOldVersion.mandant);
    //
    //				int ergebnis1=pstm1.executeUpdate();
    //				if (ergebnis1==0){/*Dann wurde Datensatz mittlerweile verändert!*/
    //					lDbBasis.rollbackTransaction();lDbBasis.endTransaction();return (0);}
    //			} catch (Exception e1){
    //				System.err.println(" "+e1.getMessage());
    //			}
    //		}
    //		
    //		/*Aenderungslog schreiben*/
    //		EclAenderungslog aenderungsLog=new EclAenderungslog();
    //		aenderungsLog.tabelle=EnAenderungslogTabelle.toEntity(EnAenderungslogTabelle.meldungen);
    //		aenderungsLog.ident=Integer.toString(VMclMeldung.meldungsIdent);
    //		aenderungsLog.aktion=EnAenderungslogAktion.toEntity(EnAenderungslogAktion.neueZutrittsIdent);
    //		aenderungsLog.feld="Zutrittsidentifikation";
    //		aenderungsLog.alt=VclMeldungOldVersion.zutrittsIdent;
    //		aenderungsLog.neu=VMclMeldung.zutrittsIdent;
    //		lDbBundle.dbAenderungslog.insert(aenderungsLog);
    //		
    //		
    //		lDbBasis.endTransaction();
    //		return (1);
    //	}

    /**************Updaten des Feldes praesenzStatus, und Übertragen in VclMeldungOldVersion**************
     * wird noch in BlWillenserklaerung verwendet*/
    @Deprecated
    public int updateStatusPraesenz(EclMeldung VMclMeldung) {

        /*****Update des aktuellen Satzes - mit neuem StatusPraesenz****/

        try {
            String sql = "UPDATE " + dbBundle.getSchemaMandant() + "tbl_meldungen " + "SET "
                    + "statusPraesenz=?, statusWarPraesenz=?, statusPraesenzGast=?, statusWarPraesenzGast=? "
                    + this.liefereWhereString();

            PreparedStatement pstm1 = verbindung.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_UPDATABLE);
            pstm1.setInt(1, VMclMeldung.statusPraesenz);
            pstm1.setInt(2, VMclMeldung.statusWarPraesenz);
            pstm1.setInt(3, VMclMeldung.statusPraesenzGast);
            pstm1.setInt(4, VMclMeldung.statusWarPraesenzGast);

            fuellePreparedStatementWhere(pstm1, 4, VMclMeldung); /*Where-Teil*/
            int ergebnis1 = pstm1.executeUpdate();
            if (ergebnis1 == 0) {/*Dann wurde Datensatz mittlerweile verändert!*/
                return (-6);
            }
        } catch (Exception e1) {
            System.err.println(" " + e1.getMessage());
            return (-1);
        }

        /*Spotbugs: kompletten Komplex überprüfen*/
        VclMeldungOldVersion.statusPraesenz = VMclMeldung.statusPraesenz;
        VclMeldungOldVersion.statusWarPraesenz = VMclMeldung.statusWarPraesenz;
        VclMeldungOldVersion.statusPraesenzGast = VMclMeldung.statusPraesenzGast;
        VclMeldungOldVersion.statusWarPraesenzGast = VMclMeldung.statusWarPraesenzGast;
        return (1);
    }

    /** ****************************************************************************
     * Block von in sich abhängigen Funktionen für das Durchlesen und Verarbeiten
     * der Meldungen für den VIP Monitor.
     * 
     * Grundablauf:
     * 
     * readarray_xxxx_initbasis - initialisiert verschiedene Buffer für aneinander zu verkettendende 
     * Select-Vorgänge
     * 
     * readarray_xxxx_init - initialisiert Lesevorgang und führt ihn aus
     * 		xxxx gibt dabei Verwendungszweck wieder.
     * 		Welche "xxxx" aufgerufen wurde, wird intern in readarray_art
     * 		gespeichert.
     * 		Derzeit implementiert:
     * 			1 = SucheNachNamen = Verwendung für Suchfunktion, Suche nach Namen
     * 
     * readarray_getoffset - liefert clMeldung, aber nur die Felder gefüllt, die
     * 		gemäß xxxx (siehe oben) implementiert sind (aus Effizienzgründen).
     * 		Die Funktion ist so implementiert, dass wiederholte getoffset mit selbem
     * 		offset "gepuffert" werden, d.h. nicht wiederholt ausgeführt werden.
     */

    public int[] readarray_SucheVipKZMonitor_anzahl;
    private int[] readarray_SucheVipKZMonitor_art; /*=1 => VIPKZ kommt aus Select (bei direkt zugeordneten); =2 => kommt nicht aus Select*/
    private PreparedStatement[] readarray_SucheVipKZMonitor_pstm;
    private String[] readarray_SucheVipKZMonitor_sql;
    private ResultSet[] readarray_SucheVipKZMonitor_ergebnis;

    /** Diese Funktion stellt eine anzahl Buffer fürs halten von Selektions-Ergebnissen bereit.
     * Verwendung: Vip-Monitor, für die verschiedenen Selektionsläufe.
     * @param anzahl
     */
    public void readarray_SucheVipKZMonitor_initbasis(int anzahl) {
        readarray_SucheVipKZMonitor_anzahl = new int[anzahl];
        readarray_SucheVipKZMonitor_art = new int[anzahl];
        readarray_SucheVipKZMonitor_pstm = new PreparedStatement[anzahl];
        readarray_SucheVipKZMonitor_sql = new String[anzahl];
        readarray_SucheVipKZMonitor_ergebnis = new ResultSet[anzahl];
        //		System.out.println("Init Basis anzahl="+anzahl);
        return;
    }

    public int readarray_SucheVipKZMonitorKuerzel_init(int offset, String[] selektierteKuerzel, int selektionB,
            int selektionC, int selektionD) {

        readarray_SucheVipKZMonitor_art[offset] = 1;

        /*Die Sequenz zum Zusammenbau des SQL-Statements erfolgt 2x: einmal zum Zusammenbau des PreparedStatements selbst, anschließend zum Füllen
         * der Felder 
         */

        /*Step 1: Inner Join und Basisselektion - einschließlich der ausgewählten VIP-Kürzel*/
        readarray_SucheVipKZMonitor_sql[offset] = "SELECT * " + "from " + dbBundle.getSchemaMandant()
                + "tbl_meldungen me INNER JOIN " + dbBundle.getSchemaMandant()
                + "tbl_meldungenVipKZ vk on (me.meldungsIdent=vk.meldungsIdent) "
                + "where me.mandant=? AND me.meldungAktiv=1 AND vk.vipKZKuerzel IN ";
        String whereKuerzel = "(";
        int i;
        for (i = 0; i < selektierteKuerzel.length; i++) {
            if (i != 0) {
                whereKuerzel = whereKuerzel + ", ";
            }
            whereKuerzel = whereKuerzel + "?";
        }
        whereKuerzel = whereKuerzel + ") ";
        readarray_SucheVipKZMonitor_sql[offset] = readarray_SucheVipKZMonitor_sql[offset] + whereKuerzel;

        /*Step 2: Selektion C anfügen*/
        if (selektionC != BlMonitorVipKZ.C_ALLEMELDUNGUNG) {
            if (selektionC == BlMonitorVipKZ.C_GAESTE) {
                readarray_SucheVipKZMonitor_sql[offset] = readarray_SucheVipKZMonitor_sql[offset] + " and me.klasse=0";
            } else {
                readarray_SucheVipKZMonitor_sql[offset] = readarray_SucheVipKZMonitor_sql[offset] + " and me.klasse=1";
            }
        }

        /*Step 3: Selektion D anfügen*/
        if (selektionD != BlMonitorVipKZ.D_ALLE) {

            if (selektionD == BlMonitorVipKZ.D_ANWESEND) {
                readarray_SucheVipKZMonitor_sql[offset] = readarray_SucheVipKZMonitor_sql[offset] + " and "
                        + "((me.statusPraesenz=1 and me.klasse=1) or (me.statusPraesenzGast=1 and me.klasse=1))";
            }
            if (selektionD == BlMonitorVipKZ.D_ABWESEND) {
                readarray_SucheVipKZMonitor_sql[offset] = readarray_SucheVipKZMonitor_sql[offset] + " and "
                        + "(((me.statusPraesenz=0 or me.statusPraesenz=2) and me.klasse=1)" + "or"
                        + "((me.statusPraesenzGast=0 or me.statusPraesenzGast=2) and me.klasse=0))";
            }
            if (selektionD == BlMonitorVipKZ.D_WARANWESEND) {
                readarray_SucheVipKZMonitor_sql[offset] = readarray_SucheVipKZMonitor_sql[offset] + " and "
                        + "(((me.statusPraesenz=2 or me.statusPraesenz=1) and me.klasse=1) " + "or "
                        + "((me.statusPraesenzGast=2 or me.statusPraesenzGast=1) and me.klasse=0))";
            }

        }

        /*Step 4: Selektion B anfügen*/
        if (selektionB != BlMonitorVipKZ.B_ALLEEINAUSGEBLENDETEN) {
            readarray_SucheVipKZMonitor_sql[offset] = readarray_SucheVipKZMonitor_sql[offset] + " and me.meldungsIdent";
            if (selektionB == BlMonitorVipKZ.B_NURNICHTAUSGEBLENDETE) {
                readarray_SucheVipKZMonitor_sql[offset] = readarray_SucheVipKZMonitor_sql[offset] + " not";
            }
            readarray_SucheVipKZMonitor_sql[offset] = readarray_SucheVipKZMonitor_sql[offset]
                    + " in (select vka.meldungsIdent from " + dbBundle.getSchemaMandant()
                    + "tbl_meldungenVipKZAusgeblendet vka"
                    + " where vka.mandant=? and (vka.benutzernr=? or vka.benutzernr=0) and vka.vipKZKuerzel=vk.vipKZKuerzel)";
        }

        /*Steps beendet*/
        //		System.out.println("SQL="+readarray_SucheVipKZMonitor_sql[offset]);
        int hoffset = 1;
        try {
            readarray_SucheVipKZMonitor_pstm[offset] = verbindung.prepareStatement(
                    readarray_SucheVipKZMonitor_sql[offset], ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
            /*Step 1: Inner Join und Basisselektion - einschließlich der ausgewählten VIP-Kürzel*/
            readarray_SucheVipKZMonitor_pstm[offset].setInt(hoffset, dbBundle.clGlobalVar.mandant);
            hoffset++;
            for (i = 0; i < selektierteKuerzel.length; i++) {
                readarray_SucheVipKZMonitor_pstm[offset].setString(hoffset, selektierteKuerzel[i]);
                hoffset++;
            }
            /*Step 2: Selektion C anfügen*/
            /*Step 3: Selektion D anfügen*/
            /*Step 4: Selektion B anfügen*/
            if (selektionB != BlMonitorVipKZ.B_ALLEEINAUSGEBLENDETEN) {
                readarray_SucheVipKZMonitor_pstm[offset].setInt(hoffset, dbBundle.clGlobalVar.mandant);
                hoffset++;
                readarray_SucheVipKZMonitor_pstm[offset].setInt(hoffset, dbBundle.clGlobalVar.benutzernr);
                hoffset++;
            }

            /*Steps beendet*/

            readarray_SucheVipKZMonitor_ergebnis[offset] = readarray_SucheVipKZMonitor_pstm[offset].executeQuery();
            readarray_SucheVipKZMonitor_ergebnis[offset].last();
            readarray_SucheVipKZMonitor_anzahl[offset] = readarray_SucheVipKZMonitor_ergebnis[offset].getRow();
            readarray_SucheVipKZMonitor_ergebnis[offset].beforeFirst();

        } catch (SQLException e1) {
            System.err.println("Fehler hierdrin " + e1.getMessage());
            e1.printStackTrace();
        }

        return (readarray_SucheVipKZMonitor_anzahl[offset]); /*andere Returnwerte für Fehlerbehandlung vorgesehen*/

    }

    public int readarray_SucheVipKZMonitorAktien_init(int offset, int aktien, String kuerzel, int selektionB,
            int selektionC, int selektionD) {
        /*TODO _VIPKZ: readarray_SucheVipKZMonitor_init*/
        return (0);
    }

    public int readarray_SucheVipKZMonitorStimmausschluss_init(int offset, String kuerzel, int selektionB,
            int selektionC, int selektionD) {
        /*TODO _VIPKZ: readarray_SucheVipKZMonitor_init*/
        return (0);
    }

    public int readarray_SucheVipKZMonitorNameVorname_init(int offset, String name, String vorname, String kuerzel,
            int selektionB, int selektionC, int selektionD) {
        /*TODO _VIPKZ: readarray_SucheVipKZMonitor_init*/

        //		System.out.println("In NameVorname_init 1");

        readarray_SucheVipKZMonitor_art[offset] = 2;

        /*Die Sequenz zum Zusammenbau des SQL-Statements erfolgt 2x: einmal zum Zusammenbau des PreparedStatements selbst, anschließend zum Füllen
         * der Felder 
         */

        /*Step 1: Basisselektion - Name und ggf. Vorname*/
        readarray_SucheVipKZMonitor_sql[offset] = "SELECT * " + "from " + dbBundle.getSchemaMandant()
                + "tbl_meldungen me " + "where me.mandant=? AND me.meldungAktiv=1 AND me.name LIKE ?";

        if (!vorname.isEmpty()) { /*Auch nach Vornamen suchen*/
            readarray_SucheVipKZMonitor_sql[offset] = readarray_SucheVipKZMonitor_sql[offset]
                    + " AND me.vorname LIKE ?";
        }

        /*Step 2: Selektion C anfügen*/
        if (selektionC != BlMonitorVipKZ.C_ALLEMELDUNGUNG) {
            if (selektionC == BlMonitorVipKZ.C_GAESTE) {
                readarray_SucheVipKZMonitor_sql[offset] = readarray_SucheVipKZMonitor_sql[offset] + " and me.klasse=0";
            } else {
                readarray_SucheVipKZMonitor_sql[offset] = readarray_SucheVipKZMonitor_sql[offset] + " and me.klasse=1";
            }
        }

        /*Step 3: Selektion D anfügen*/
        if (selektionD != BlMonitorVipKZ.D_ALLE) {

            if (selektionD == BlMonitorVipKZ.D_ANWESEND) {
                readarray_SucheVipKZMonitor_sql[offset] = readarray_SucheVipKZMonitor_sql[offset] + " and "
                        + "((me.statusPraesenz=1 and me.klasse=1) or (me.statusPraesenzGast=1 and me.klasse=1))";
            }
            if (selektionD == BlMonitorVipKZ.D_ABWESEND) {
                readarray_SucheVipKZMonitor_sql[offset] = readarray_SucheVipKZMonitor_sql[offset] + " and "
                        + "(((me.statusPraesenz=0 or me.statusPraesenz=2) and me.klasse=1)" + "or"
                        + "((me.statusPraesenzGast=0 or me.statusPraesenzGast=2) and me.klasse=0))";
            }
            if (selektionD == BlMonitorVipKZ.D_WARANWESEND) {
                readarray_SucheVipKZMonitor_sql[offset] = readarray_SucheVipKZMonitor_sql[offset] + " and "
                        + "(((me.statusPraesenz=2 or me.statusPraesenz=1) and me.klasse=1) " + "or "
                        + "((me.statusPraesenzGast=2 or me.statusPraesenzGast=1) and me.klasse=0))";
            }

        }

        /*Step 4: Selektion B anfügen*/
        if (selektionB != BlMonitorVipKZ.B_ALLEEINAUSGEBLENDETEN) {
            readarray_SucheVipKZMonitor_sql[offset] = readarray_SucheVipKZMonitor_sql[offset] + " and me.meldungsIdent";
            if (selektionB == BlMonitorVipKZ.B_NURNICHTAUSGEBLENDETE) {
                readarray_SucheVipKZMonitor_sql[offset] = readarray_SucheVipKZMonitor_sql[offset] + " not";
            }
            readarray_SucheVipKZMonitor_sql[offset] = readarray_SucheVipKZMonitor_sql[offset]
                    + " in (select vka.meldungsIdent from " + dbBundle.getSchemaMandant()
                    + "tbl_meldungenVipKZAusgeblendet vka"
                    + " where vka.mandant=? and (vka.benutzernr=? or vka.benutzernr=0) and vka.vipKZKuerzel=?)";
        }

        /*Steps beendet*/

        int hoffset = 1;
        try {
            readarray_SucheVipKZMonitor_pstm[offset] = verbindung.prepareStatement(
                    readarray_SucheVipKZMonitor_sql[offset], ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
            /*Step 1: Basisselektion - Name und ggf. Vorname*/
            readarray_SucheVipKZMonitor_pstm[offset].setInt(hoffset, dbBundle.clGlobalVar.mandant);
            hoffset++;
            readarray_SucheVipKZMonitor_pstm[offset].setString(hoffset, "%" + name + "%");
            hoffset++;
            if (!vorname.isEmpty()) { /*Auch nach Vornamen suchen*/
                readarray_SucheVipKZMonitor_pstm[offset].setString(hoffset, "%" + vorname + "%");
                hoffset++;
            }

            /*Step 2: Selektion C anfügen*/
            /*Step 3: Selektion D anfügen*/
            if (selektionB != BlMonitorVipKZ.B_ALLEEINAUSGEBLENDETEN) {
                readarray_SucheVipKZMonitor_pstm[offset].setInt(hoffset, dbBundle.clGlobalVar.mandant);
                hoffset++;
                readarray_SucheVipKZMonitor_pstm[offset].setInt(hoffset, dbBundle.clGlobalVar.benutzernr);
                hoffset++;
                readarray_SucheVipKZMonitor_pstm[offset].setString(hoffset, kuerzel);
                hoffset++;
            }

            /*Steps beendet*/

            readarray_SucheVipKZMonitor_ergebnis[offset] = readarray_SucheVipKZMonitor_pstm[offset].executeQuery();
            readarray_SucheVipKZMonitor_ergebnis[offset].last();
            readarray_SucheVipKZMonitor_anzahl[offset] = readarray_SucheVipKZMonitor_ergebnis[offset].getRow();
            readarray_SucheVipKZMonitor_ergebnis[offset].beforeFirst();

        } catch (SQLException e1) {
            System.err.println("Fehler hierdrin " + e1.getMessage());
            e1.printStackTrace();
        }

        //		System.out.println("In NameVorname_init 11");
        //		System.out.println("readarray_anzahl"+readarray_SucheVipKZMonitor_anzahl[offset]);
        return (readarray_SucheVipKZMonitor_anzahl[offset]); /*andere Returnwerte für Fehlerbehandlung vorgesehen*/

    }

    public int readarray_SucheVipKZMonitorVirtuelleKarten_init(int offset, String kuerzel, int selektionB,
            int selektionC, int selektionD) {
        /*TODO _VIPKZ: readarray_SucheVipKZMonitor_init*/
        return (0);
    }

    public int readarray_SucheVipKZMonitor_getoffset(int buffer, int offset, EclMeldung VclMeldung) {

        //		System.out.println("Offset"+offset);
        try {

            readarray_SucheVipKZMonitor_ergebnis[buffer].absolute(offset);
            if (readarray_SucheVipKZMonitor_ergebnis[buffer].next() == true) {

                VclMeldung.meldungsIdent = readarray_SucheVipKZMonitor_ergebnis[buffer].getInt("me.meldungsIdent");
                VclMeldung.meldungAktiv = readarray_SucheVipKZMonitor_ergebnis[buffer].getInt("me.meldungAktiv");
                VclMeldung.zutrittsIdent = readarray_SucheVipKZMonitor_ergebnis[buffer].getString("me.zutrittsIdent");
                VclMeldung.zutrittsIdentVers = readarray_SucheVipKZMonitor_ergebnis[buffer]
                        .getInt("me.zutrittsIdentVers");
                VclMeldung.statusPraesenz = readarray_SucheVipKZMonitor_ergebnis[buffer].getInt("me.statusPraesenz");
                VclMeldung.statusPraesenzGast = readarray_SucheVipKZMonitor_ergebnis[buffer]
                        .getInt("me.statusPraesenzGast");

                VclMeldung.name = readarray_SucheVipKZMonitor_ergebnis[buffer].getString("me.name");
                VclMeldung.vorname = readarray_SucheVipKZMonitor_ergebnis[buffer].getString("me.vorname");
                VclMeldung.ort = readarray_SucheVipKZMonitor_ergebnis[buffer].getString("me.ort");
                VclMeldung.meldungVipKZarray = new EclMeldungVipKZ[1];
                VclMeldung.meldungVipKZarray[0] = new EclMeldungVipKZ();
                if (readarray_SucheVipKZMonitor_art[buffer] == 1) {
                    VclMeldung.meldungVipKZarray[0].vipKZKuerzel = readarray_SucheVipKZMonitor_ergebnis[buffer]
                            .getString("vk.vipKZKuerzel");
                } else {
                    VclMeldung.meldungVipKZarray[0].vipKZKuerzel = "";
                }

                //					System.out.println("nach bufferMeldungfüllen");
            }

        } catch (SQLException e) {
            //  Auto-generated catch block
            e.printStackTrace();
        }

        //		System.out.println("Name etc"+VclMeldung.name+VclMeldung.vorname+VclMeldung.ort);
        return (1); /*andere Returnwerte für Fehlerbehandlung vorgesehen*/

    }

    /** Rückgabe einer Kopie der beim Einlesen zwischengespeicherten "alten" Version von meldung*/
    public EclMeldung getOldVersion() {
        EclMeldung oldMeldung = new EclMeldung();
        /*Spotbugs: kompletten Bereich überprüfen*/
        VclMeldungOldVersion.copyTo(oldMeldung);
        return (oldMeldung);
    }

    /****************************************************************************************************************************************/
    /**************************************Methoden zum Auflösen der Delayed-Willenserklärungen**********************************************/
    /****************************************************************************************************************************************/
    /**Hier wird die jeweils nächste eingelesen und zu verarbeitende delayed Willenserklärung von diesen Methoden abgelegt*/
    public EclMeldung rcDelayedMeldung = null;

    /**Anzahl der zu verarbeitenden Delayed-Meldungen - ist nach aufloesenDelayed_init gefüllt*/
    public int rcDelayedAnzahl = 0;

    private ResultSet delayedErgebnis = null;

    /**Initialisierung - setzt Select ab*/
    public int initAufloesenDelayed() {
        try {

            String sql = "";
            sql = "SELECT * from " + dbBundle.getSchemaMandant() + "tbl_meldungen m " + "INNER JOIN "
                    + dbBundle.getSchemaMandant() + "tbl_personenNatJur p ON m.personenNatJurIdent=p.ident "
                    + "where m.meldungAktiv=1 " + " and m.mandant=? and p.mandant=? AND m.delayedVorhanden=1";

            PreparedStatement pstm1 = verbindung.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
            pstm1.setInt(1, dbBundle.clGlobalVar.mandant);
            pstm1.setInt(2, dbBundle.clGlobalVar.mandant);

            delayedErgebnis = pstm1.executeQuery();
            delayedErgebnis.last();
            rcDelayedAnzahl = delayedErgebnis.getRow();
            delayedErgebnis.beforeFirst();

        } catch (Exception e) {
            CaBug.drucke("DbMeldungen.initAufloesenDelayed 001");
            System.err.println(" " + e.getMessage());
        }

        return (rcDelayedAnzahl);
    }

    /**Liefere nächste Willenserklärung in rcDelayedWeillenserklaerung ab
     * liefert 0, wenn Dateiende erreicht*/
    public int readNextDelayed() {
        try {
            if (delayedErgebnis.next() == false) {
                return (0);
            }
            rcDelayedMeldung = this.decodeErgebnis(delayedErgebnis);
        } catch (Exception e) {
            CaBug.drucke("DbMeldungen.readNexDelayed 001");
            System.err.println(" " + e.getMessage());
        }
        return 1;
    }

    /* *********************************************************************************************************
     * *********************************************************************************************************
     * Bis hierher: Methoden, für zweiten Versuch , noch unkonsolidiert - nicht mehr für neue Funktionen
     * verwenden.
     * 
     * Ab hier: "offizielle Methoden" für McdbMeldungen, gefundene Meldungen werden im Array abgelegt
     * (oldVersion in separatem Array)
     * 
     * oldVersion wird nun wie folgt gehandhabt: bei verändernden Sätzen wird aus dem Ergebnis-Array der zugehörige
     * "Old"-Satz herausgesucht und als oldVersion verwendet.
     * *********************************************************************************************************
     */

    /* Hinweis: OldVersion wird zwar noch gefüllt, aber nicht mehr für Update-Prüfung verwendet! Umgestellt auf
     * db_version
     */

    private Connection verbindung = null;
    private DbBasis dbBasis = null;
    private DbBundle dbBundle = null;

    /**Offizielles, neues (14.09.2020) Array für die 
     * Ergebnisrückgabe, zusammen mit anzErgebnis()
     */
    public EclMeldung meldungenArray[] = null;

    /**************Anzahl der Ergebnisse der read*-Methoden**************************/
    public int anzErgebnis() {
        if (meldungenArray == null) {
            return 0;
        }
        return meldungenArray.length;
    }

    /*************************Initialisierung***************************/

    public DbMeldungen(DbBundle pDbBundle) {
        if (pDbBundle.dbBasis == null) {
            System.err.println("vmcdbBasis nicht initialisiert!");
            return;
        }
        dbBasis = pDbBundle.dbBasis;
        verbindung = pDbBundle.dbBasis.verbindung;
        dbBundle = pDbBundle;
    }

    public int createTable() {
        
        /*
ALTER TABLE `db_mc878j2020ap`.`tbl_meldungen` 
ADD INDEX `IDX_MELDUNGSTYP` (`meldungstyp` ASC),
ADD INDEX `IDX_KLASSE` (`klasse` ASC);
;

         */
        
        int rc = 0;
        DbLowLevel lDbLowLevel = new DbLowLevel(dbBundle);
        rc = lDbLowLevel.createTable("CREATE TABLE " + dbBundle.getSchemaMandant() + "tbl_meldungen ( "
                + "`meldungsIdent` int(11) NOT NULL, " + "`db_version` bigint(20) DEFAULT '0', "
                + "`mandant` int(11) NOT NULL DEFAULT '0', " + "`meldungAktiv` int(11) DEFAULT '0', "
                + "`klasse` int(11) DEFAULT '0', " + "`aktionaersnummer` varchar(20) DEFAULT '', "
                + "`aktienregisterIdent` int(11) DEFAULT '0', " + "`externeIdent` varchar(20) DEFAULT '', "
                + "`meldungstyp` int(11) DEFAULT '0', " + "`skMitWeisungen` int(11) DEFAULT '0', "
                + "`skIst` int(11) DEFAULT '0', " + "`skWeisungsartZulaessig` int(11) DEFAULT '0', "
                + "`skBuchbarInternet` int(11) DEFAULT '0', " + "`skBuchbarPapier` int(11) DEFAULT '0', "
                + "`skBuchbarHV` int(11) DEFAULT '0', " + "`skBuchbarVollmachtDritte` int(11) DEFAULT '0', "
                + "`skBuchbarVollmachtDritteHV` int(11) DEFAULT '0', " + "`skOffenlegung` int(11) DEFAULT '0', "
                + "`instiIdent` int(11) DEFAULT '0', " + "`stimmkarteVorab` varchar(20) DEFAULT '', "
                + "`stimmkarteSecondVorab` varchar(20) DEFAULT '', " + "`loginKennung` char(20) DEFAULT '', "
                + "`loginPasswort` char(20) DEFAULT '', " + "`gattung` int(11) DEFAULT '0', "
                + "`fixAnmeldung` int(11) DEFAULT '0', " + "`stueckAktien` bigint(20) DEFAULT '0', "
                + "`stimmen` bigint(20) DEFAULT '0', " + "`stueckAktienDruckEK` bigint(20) DEFAULT '0', "
                + "`stimmenDruckEK` bigint(20) DEFAULT '0', " + "`besitzart` char(1) DEFAULT '', "
                + "`stimmausschluss` char(13) DEFAULT '', " + "`zusatzrechte` int(11) DEFAULT '0', "
                + "`personenNatJurIdent` int(11) DEFAULT '0', " + "`anrede` int(11) DEFAULT '0', "
                + "`titel` varchar(30) DEFAULT '', " + "`adelstitel` varchar(30) DEFAULT '', "
                + "`name` varchar(80) DEFAULT '', " + "`vorname` varchar(80) DEFAULT '', "
                + "`zuHdCo` varchar(80) DEFAULT '', " + "`zusatz1` varchar(80) DEFAULT '', "
                + "`zusatz2` varchar(80) DEFAULT '', " + "`strasse` varchar(80) DEFAULT '', "
                + "`land` char(4) DEFAULT '', " + "`plz` varchar(20) DEFAULT '', " + "`ort` varchar(80) DEFAULT '', "
                + "`gebDatum` varchar(10) DEFAULT '', " + "`mailadresse` varchar(80) DEFAULT '', "
                + "`kommunikationssprache` int(11) DEFAULT '0', " + "`zusatzfeld1` varchar(40) DEFAULT '', "
                + "`zusatzfeld2` varchar(40) DEFAULT '', " + "`zusatzfeld3` varchar(40) DEFAULT '', "
                + "`zusatzfeld4` varchar(40) DEFAULT '', " + "`zusatzfeld5` varchar(40) DEFAULT '', "
                + "`gruppe` int(11) DEFAULT '0', " + "`neuanlageDatumUhrzeit` char(19) DEFAULT '', "
                + "`geaendertDatumUhrzeit` char(19) DEFAULT '', "
                + "`manuellGeaendertSeitLetztemImport` int(11) DEFAULT '0', "
                + "`delayedVorhanden` int(11) DEFAULT '0', " + "`pendingVorhanden` int(11) DEFAULT '0', "
                + "`zutrittsIdent` varchar(20) DEFAULT '', " + "`zutrittsIdent_Delayed` varchar(20) DEFAULT '', "
                + "`stimmkarte` varchar(20) DEFAULT '', " + "`stimmkarte_Delayed` varchar(20) DEFAULT '', "
                + "`stimmkarteSecond` varchar(20) DEFAULT '', " + "`stimmkarteSecond_Delayed` varchar(20) DEFAULT '', "
                + "`willenserklaerung` int(11) DEFAULT '0', " + "`willenserklaerung_Delayed` int(11) DEFAULT '0', "
                + "`willenserklaerungIdent` int(11) DEFAULT '0', "
                + "`willenserklaerungIdent_Delayed` int(11) DEFAULT '0', " + "`veraenderungszeit` char(19) DEFAULT '', "
                + "`veraenderungszeit_Delayed` char(19) DEFAULT '', " + "`erteiltAufWeg` int(11) DEFAULT '0', "
                + "`erteiltAufWeg_Delayed` int(11) DEFAULT '0', "
                + "`meldungEnthaltenInSammelkarte` int(11) DEFAULT '0', "
                + "`meldungEnthaltenInSammelkarte_Delayed` int(11) DEFAULT '0', "
                + "`meldungEnthaltenInSammelkarteArt` int(11) DEFAULT '0', "
                + "`meldungEnthaltenInSammelkarteArt_Delayed` int(11) DEFAULT '0', "
                + "`weisungVorhanden` int(11) DEFAULT '0', " + "`weisungVorhanden_Delayed` int(11) DEFAULT '0', "
                + "`vertreterName` varchar(80) DEFAULT '', " + "`vertreterName_Delayed` varchar(80) DEFAULT NULL, "
                + "`vertreterVorname` varchar(80) DEFAULT '', "
                + "`vertreterVorname_Delayed` varchar(80) DEFAULT NULL, " + "`vertreterOrt` varchar(80) DEFAULT '', "
                + "`vertreterOrt_Delayed` varchar(80) DEFAULT '', " + "`vertreterIdent` int(11) DEFAULT '0', "
                + "`vertreterIdent_Delayed` int(11) DEFAULT '0', "
                + "`willenserklaerungMitVollmacht` int(11) DEFAULT '0', "
                + "`willenserklaerungMitVollmacht_Delayed` int(11) DEFAULT '0', "
                + "`statusPraesenz` int(11) DEFAULT '0', " + "`statusPraesenz_Delayed` int(11) DEFAULT '0', "
                + "`statusWarPraesenz` int(11) DEFAULT '0', " + "`statusWarPraesenz_Delayed` int(11) DEFAULT '0', "
                + "`virtuellerTeilnehmerIdent` int(11) DEFAULT '0', " + "`teilnahmeArt` int(11) DEFAULT '0', "
                + "`vorlAnmeldung` int(11) DEFAULT '0', " + "`vorlAnmeldungAkt` int(11) DEFAULT '0', "
                + "`vorlAnmeldungSer` int(11) DEFAULT '0', " 
                + "PRIMARY KEY (`meldungsIdent`,`mandant`),"
                + "KEY `IDX_ARIDENT` (`aktienregisterIdent`), " 
                + "KEY `IDX_VIRTTEIL` (`virtuellerTeilnehmerIdent`), "
                + "KEY `IDX_ARNR` (`aktionaersnummer`), "
                + "KEY `IDX_MELDUNGSTYP` (`meldungstyp`), "
                + "KEY `IDX_KLASSE` (`klasse`) "
                + ") ");
        if (rc < 0) {
            return rc;
        }

        lDbLowLevel = new DbLowLevel(dbBundle);
        rc = lDbLowLevel.createTable("CREATE TABLE " + dbBundle.getSchemaMandant() + "tbl_meldungenprotokoll ( "
                + "`satzArt` int(11) NOT NULL, " + "`protokollIdent` int(11) NOT NULL, "
                + "`updateZeit` char(19) DEFAULT NULL, " + "`userNummer` int(11) DEFAULT NULL, "
                + "`arbeitsplatzNummer` int(11) DEFAULT NULL, " + "`meldungsIdent` int(11) NOT NULL, "
                + "`db_version` bigint(20) DEFAULT '0', " + "`mandant` int(11) NOT NULL DEFAULT '0', "
                + "`meldungAktiv` int(11) DEFAULT '0', " + "`klasse` int(11) DEFAULT '0', "
                + "`aktionaersnummer` varchar(20) DEFAULT '', " + "`aktienregisterIdent` int(11) DEFAULT '0', "
                + "`externeIdent` varchar(20) DEFAULT '', " + "`meldungstyp` int(11) DEFAULT '0', "
                + "`skMitWeisungen` int(11) DEFAULT '0', " + "`skIst` int(11) DEFAULT '0', "
                + "`skWeisungsartZulaessig` int(11) DEFAULT '0', " + "`skBuchbarInternet` int(11) DEFAULT '0', "
                + "`skBuchbarPapier` int(11) DEFAULT '0', " + "`skBuchbarHV` int(11) DEFAULT '0', "
                + "`skBuchbarVollmachtDritte` int(11) DEFAULT '0', "
                + "`skBuchbarVollmachtDritteHV` int(11) DEFAULT '0', " + "`skOffenlegung` int(11) DEFAULT '0', "
                + "`instiIdent` int(11) DEFAULT '0', " + "`stimmkarteVorab` varchar(20) DEFAULT '', "
                + "`stimmkarteSecondVorab` varchar(20) DEFAULT '', " + "`loginKennung` char(20) DEFAULT '', "
                + "`loginPasswort` char(20) DEFAULT '', " + "`gattung` int(11) DEFAULT '0', "
                + "`stueckAktien` bigint(20) DEFAULT '0', " + "`fixAnmeldung` int(11) DEFAULT '0', "
                + "`stimmen` bigint(20) DEFAULT '0', " + "`stueckAktienDruckEK` bigint(20) DEFAULT '0', "
                + "`stimmenDruckEK` bigint(20) DEFAULT '0', " + "`besitzart` char(1) DEFAULT '', "
                + "`stimmausschluss` char(13) DEFAULT '', " + "`zusatzrechte` int(11) DEFAULT '0', "
                + "`personenNatJurIdent` int(11) DEFAULT '0', " + "`anrede` int(11) DEFAULT '0', "
                + "`titel` varchar(30) DEFAULT '', " + "`adelstitel` varchar(30) DEFAULT '', "
                + "`name` varchar(80) DEFAULT '', " + "`vorname` varchar(80) DEFAULT '', "
                + "`zuHdCo` varchar(80) DEFAULT '', " + "`zusatz1` varchar(80) DEFAULT '', "
                + "`zusatz2` varchar(80) DEFAULT '', " + "`strasse` varchar(80) DEFAULT '', "
                + "`land` char(4) DEFAULT '', " + "`plz` varchar(20) DEFAULT '', " + "`ort` varchar(80) DEFAULT '', "
                + "`gebDatum` varchar(10) DEFAULT '', " + "`mailadresse` varchar(80) DEFAULT '', "
                + "`kommunikationssprache` int(11) DEFAULT '0', " + "`zusatzfeld1` varchar(40) DEFAULT '', "
                + "`zusatzfeld2` varchar(40) DEFAULT '', " + "`zusatzfeld3` varchar(40) DEFAULT '', "
                + "`zusatzfeld4` varchar(40) DEFAULT '', " + "`zusatzfeld5` varchar(40) DEFAULT '', "
                + "`gruppe` int(11) DEFAULT '0', " + "`neuanlageDatumUhrzeit` char(19) DEFAULT '', "
                + "`geaendertDatumUhrzeit` char(19) DEFAULT '', "
                + "`manuellGeaendertSeitLetztemImport` int(11) DEFAULT '0', "
                + "`delayedVorhanden` int(11) DEFAULT '0', " + "`pendingVorhanden` int(11) DEFAULT '0', "
                + "`zutrittsIdent` varchar(20) DEFAULT '', " + "`zutrittsIdent_Delayed` varchar(20) DEFAULT '', "
                + "`stimmkarte` varchar(20) DEFAULT '', " + "`stimmkarte_Delayed` varchar(20) DEFAULT '', "
                + "`stimmkarteSecond` varchar(20) DEFAULT '', " + "`stimmkarteSecond_Delayed` varchar(20) DEFAULT '', "
                + "`willenserklaerung` int(11) DEFAULT '0', " + "`willenserklaerung_Delayed` int(11) DEFAULT '0', "
                + "`willenserklaerungIdent` int(11) DEFAULT '0', "
                + "`willenserklaerungIdent_Delayed` int(11) DEFAULT '0', " + "`veraenderungszeit` char(19) DEFAULT '', "
                + "`veraenderungszeit_Delayed` char(19) DEFAULT '', " + "`erteiltAufWeg` int(11) DEFAULT '0', "
                + "`erteiltAufWeg_Delayed` int(11) DEFAULT '0', "
                + "`meldungEnthaltenInSammelkarte` int(11) DEFAULT '0', "
                + "`meldungEnthaltenInSammelkarte_Delayed` int(11) DEFAULT '0', "
                + "`meldungEnthaltenInSammelkarteArt` int(11) DEFAULT '0', "
                + "`meldungEnthaltenInSammelkarteArt_Delayed` int(11) DEFAULT '0', "
                + "`weisungVorhanden` int(11) DEFAULT '0', " + "`weisungVorhanden_Delayed` int(11) DEFAULT '0', "
                + "`vertreterName` varchar(80) DEFAULT '', " + "`vertreterName_Delayed` varchar(80) DEFAULT NULL, "
                + "`vertreterVorname` varchar(80) DEFAULT '', "
                + "`vertreterVorname_Delayed` varchar(80) DEFAULT NULL, " + "`vertreterOrt` varchar(80) DEFAULT '', "
                + "`vertreterOrt_Delayed` varchar(80) DEFAULT '', " + "`vertreterIdent` int(11) DEFAULT '0', "
                + "`vertreterIdent_Delayed` int(11) DEFAULT '0', "
                + "`willenserklaerungMitVollmacht` int(11) DEFAULT '0', "
                + "`willenserklaerungMitVollmacht_Delayed` int(11) DEFAULT '0', "
                + "`statusPraesenz` int(11) DEFAULT '0', " + "`statusPraesenz_Delayed` int(11) DEFAULT '0', "
                + "`statusWarPraesenz` int(11) DEFAULT '0', " + "`statusWarPraesenz_Delayed` int(11) DEFAULT '0', "
                + "`virtuellerTeilnehmerIdent` int(11) DEFAULT '0', " + "`teilnahmeArt` int(11) DEFAULT '0', "
                + "`vorlAnmeldung` int(11) DEFAULT '0', " + "`vorlAnmeldungAkt` int(11) DEFAULT '0', "
                + "`vorlAnmeldungSer` int(11) DEFAULT '0', " + "PRIMARY KEY (`meldungsIdent`, `mandant`, `satzArt`, `protokollIdent`),"
                + "KEY `IDX_PROTO` (`satzArt`,`protokollIdent`,`mandant`) " + ") "

        );
        return rc;

    }

    /**************************deleteAll Löschen aller Datensätze in dbMeldungen eines Mandanten****************/
    public int deleteAll() {
        int erg = 0;

        dbBasis.resetInterneIdentMeldungen();

        try {

            String sql1 = "DELETE FROM " + dbBundle.getSchemaMandant() + "tbl_meldungen where mandant=?;";
            PreparedStatement pstm1 = verbindung.prepareStatement(sql1);
            pstm1.setInt(1, dbBundle.clGlobalVar.mandant);

            erg = 0; /*Falls Duplicate Key, dann wird exception geschmissen und erg wohl nicht gefüllt!*/
            erg = pstm1.executeUpdate();
            pstm1.close();

        } catch (Exception e2) {
            CaBug.drucke("DbMeldungen.deleteAll 001");
            System.err.println(" " + e2.getMessage());
            return (erg);
        }

        dbBasis.resetInterneIdentMeldungenProtokoll();

        try {

            String sql1 = "DELETE FROM " + dbBundle.getSchemaMandant() + "tbl_meldungenProtokoll where mandant=?;";
            PreparedStatement pstm1 = verbindung.prepareStatement(sql1);
            pstm1.setInt(1, dbBundle.clGlobalVar.mandant);

            erg = 0; /*Falls Duplicate Key, dann wird exception geschmissen und erg wohl nicht gefüllt!*/
            erg = pstm1.executeUpdate();
            pstm1.close();

        } catch (Exception e2) {
            CaBug.drucke("DbMeldungen.deleteAll 002");
            System.err.println(" " + e2.getMessage());
            return (erg);
        }

        return 1;
    }

    /**************************setzt aktuelle Mandantennummer bei allen Datensätzen****************/
    public int updateMandant() {
        dbBundle.dbLowLevel.rawUpdateMandant(dbBundle.getSchemaMandant() + "tbl_meldungen");
        return dbBundle.dbLowLevel.rawUpdateMandant(dbBundle.getSchemaMandant() + "tbl_meldungenProtokoll");
    }

    public void reorgInterneIdent() {
        int lMax;
        lMax = 0;

        PreparedStatement pstm1 = null;
        try {
            int anzInArray;
            String sql = "SELECT MAX(meldungsIdent) FROM " + dbBundle.getSchemaMandant() + "tbl_meldungen ab where "
                    + "ab.mandant=? ";
            pstm1 = verbindung.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
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
            CaBug.drucke("DbMeldungen.reorgInterneIdent 001");
            System.err.println(" " + e.getMessage());
        }

        dbBasis.resetInterneIdentMeldungen(lMax);

        lMax = 0;

        pstm1 = null;
        try {
            int anzInArray;
            String sql = "SELECT MAX(protokollIdent) FROM " + dbBundle.getSchemaMandant()
                    + "tbl_meldungenprotokoll ab where " + "ab.mandant=? ";
            pstm1 = verbindung.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
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
            CaBug.drucke("DbMeldungen.reorgInterneIdent 002");
            System.err.println(" " + e.getMessage());
        }

        dbBasis.resetInterneIdentMeldungenProtokoll(lMax);

    }

    /** dekodiert die aktuelle Position aus ergebnis in EclMeldung und gibt dieses zurück
     * 82 Positionen enthalten.*/
    EclMeldung decodeErgebnis(ResultSet ergebnis) {
        EclMeldung VMclMeldung = new EclMeldung();

        try {
            //				lclVipKZ.textMailVerteiler=ergebnis.getString("vk.textMailVerteiler");
            //				lclVipKZ.abbruchZwingendStandard=ergebnis.getInt("vk.abbruchZwingendStandard");

            VMclMeldung.meldungsIdent = ergebnis.getInt("m.meldungsIdent");
            VMclMeldung.db_version = ergebnis.getLong("m.db_version");
            VMclMeldung.mandant = ergebnis.getInt("m.mandant");
            VMclMeldung.meldungAktiv = ergebnis.getInt("m.meldungAktiv");
            VMclMeldung.klasse = ergebnis.getInt("m.klasse");
            VMclMeldung.aktionaersnummer = ergebnis.getString("m.aktionaersnummer");
            VMclMeldung.aktienregisterIdent = ergebnis.getInt("m.aktienregisterIdent");
            VMclMeldung.externeIdent = ergebnis.getString("m.externeIdent");
            VMclMeldung.meldungstyp = ergebnis.getInt("m.meldungstyp");
            VMclMeldung.skMitWeisungen = ergebnis.getInt("m.skMitWeisungen");
            VMclMeldung.skIst = ergebnis.getInt("m.skIst");
            VMclMeldung.skWeisungsartZulaessig = ergebnis.getInt("m.skWeisungsartZulaessig");

            VMclMeldung.skBuchbarInternet = ergebnis.getInt("m.skBuchbarInternet");
            VMclMeldung.skBuchbarPapier = ergebnis.getInt("m.skBuchbarPapier");
            VMclMeldung.skBuchbarHV = ergebnis.getInt("m.skBuchbarHV");
            VMclMeldung.skBuchbarVollmachtDritte = ergebnis.getInt("m.skBuchbarVollmachtDritte");
            VMclMeldung.skBuchbarVollmachtDritteHV = ergebnis.getInt("m.skBuchbarVollmachtDritteHV");
            VMclMeldung.skOffenlegung = ergebnis.getInt("m.skOffenlegung");
            VMclMeldung.instiIdent = ergebnis.getInt("m.instiIdent");
            VMclMeldung.stimmkarteVorab = ergebnis.getString("m.stimmkarteVorab");
            VMclMeldung.stimmkarteSecondVorab = ergebnis.getString("m.stimmkarteSecondVorab");
            VMclMeldung.gattung = ergebnis.getInt("m.gattung");
            VMclMeldung.fixAnmeldung = ergebnis.getInt("m.fixAnmeldung");
            VMclMeldung.stueckAktien = ergebnis.getLong("m.stueckAktien");
            VMclMeldung.stimmen = ergebnis.getLong("m.stimmen");

            VMclMeldung.stueckAktienDruckEK = ergebnis.getLong("m.stueckAktienDruckEK");
            VMclMeldung.stimmenDruckEK = ergebnis.getLong("m.stimmenDruckEK");
            VMclMeldung.besitzart = ergebnis.getString("m.besitzart");

            VMclMeldung.stimmausschluss = ergebnis.getString("m.stimmausschluss");
            if (VMclMeldung.stimmausschluss == null) {
                VMclMeldung.stimmausschluss = "";
            }

            VMclMeldung.zusatzrechte = ergebnis.getInt("m.zusatzrechte");

            VMclMeldung.personenNatJurIdent = ergebnis.getInt("m.personenNatJurIdent");

            VMclMeldung.db_version_personenNatJur = ergebnis.getLong("p.db_version");

            VMclMeldung.kurzName = ergebnis.getString("p.kurzName");
            VMclMeldung.kurzOrt = ergebnis.getString("p.kurzOrt");
            VMclMeldung.anrede = ergebnis.getInt("p.anrede");
            VMclMeldung.titel = ergebnis.getString("p.titel");
            VMclMeldung.adelstitel = ergebnis.getString("p.adelstitel");
            VMclMeldung.name = ergebnis.getString("p.name");
            VMclMeldung.vorname = ergebnis.getString("p.vorname");

            VMclMeldung.zuHdCo = ergebnis.getString("p.zuHdCo");
            VMclMeldung.zusatz1 = ergebnis.getString("p.zusatz1");
            VMclMeldung.zusatz2 = ergebnis.getString("p.zusatz2");
            VMclMeldung.strasse = ergebnis.getString("p.strasse");
            VMclMeldung.land = ergebnis.getString("p.land");
            VMclMeldung.plz = ergebnis.getString("p.plz");
            VMclMeldung.ort = ergebnis.getString("p.ort");
            VMclMeldung.identVersandadresse = ergebnis.getInt("p.identVersandadresse");
            VMclMeldung.mailadresse = ergebnis.getString("p.mailadresse");
            VMclMeldung.kommunikationssprache = ergebnis.getInt("p.kommunikationssprache");
            VMclMeldung.loginKennung = ergebnis.getString("p.loginKennung");
            VMclMeldung.loginPasswort = ergebnis.getString("p.loginPasswort");
            VMclMeldung.oeffentlicheID = ergebnis.getString("p.oeffentlicheID");

            VMclMeldung.zusatzfeld1 = ergebnis.getString("m.zusatzfeld1");
            VMclMeldung.zusatzfeld2 = ergebnis.getString("m.zusatzfeld2");
            VMclMeldung.zusatzfeld3 = ergebnis.getString("m.zusatzfeld3");
            VMclMeldung.zusatzfeld4 = ergebnis.getString("m.zusatzfeld4");
            VMclMeldung.zusatzfeld5 = ergebnis.getString("m.zusatzfeld5");
            VMclMeldung.gruppe = ergebnis.getInt("m.gruppe");
            VMclMeldung.neuanlageDatumUhrzeit = ergebnis.getString("m.neuanlageDatumUhrzeit");
            VMclMeldung.geaendertDatumUhrzeit = ergebnis.getString("m.geaendertDatumUhrzeit");
            VMclMeldung.manuellGeaendertSeitLetztemImport = ergebnis.getInt("m.manuellGeaendertSeitLetztemImport");
            VMclMeldung.delayedVorhanden = ergebnis.getInt("m.delayedVorhanden");

            VMclMeldung.pendingVorhanden = ergebnis.getInt("m.pendingVorhanden");
            VMclMeldung.zutrittsIdent = ergebnis.getString("m.zutrittsIdent");
            VMclMeldung.zutrittsIdent_Delayed = ergebnis.getString("m.zutrittsIdent_Delayed");
            VMclMeldung.stimmkarte = ergebnis.getString("m.stimmkarte");
            VMclMeldung.stimmkarte_Delayed = ergebnis.getString("m.stimmkarte_Delayed");
            VMclMeldung.stimmkarteSecond = ergebnis.getString("m.stimmkarteSecond");
            VMclMeldung.stimmkarteSecond_Delayed = ergebnis.getString("m.stimmkarteSecond_Delayed");

            VMclMeldung.willenserklaerung = ergebnis.getInt("m.willenserklaerung");
            VMclMeldung.willenserklaerung_Delayed = ergebnis.getInt("m.willenserklaerung_Delayed");
            VMclMeldung.willenserklaerungIdent = ergebnis.getInt("m.willenserklaerungIdent");
            VMclMeldung.willenserklaerungIdent_Delayed = ergebnis.getInt("m.willenserklaerungIdent_Delayed");
            VMclMeldung.veraenderungszeit = ergebnis.getString("m.veraenderungszeit");
            VMclMeldung.veraenderungszeit_Delayed = ergebnis.getString("m.veraenderungszeit_Delayed");
            VMclMeldung.erteiltAufWeg = ergebnis.getInt("m.erteiltAufWeg");
            VMclMeldung.erteiltAufWeg_Delayed = ergebnis.getInt("m.erteiltAufWeg_Delayed");

            VMclMeldung.meldungEnthaltenInSammelkarte = ergebnis.getInt("m.meldungEnthaltenInSammelkarte");
            VMclMeldung.meldungEnthaltenInSammelkarte_Delayed = ergebnis
                    .getInt("m.meldungEnthaltenInSammelkarte_Delayed");
            VMclMeldung.meldungEnthaltenInSammelkarteArt = ergebnis.getInt("m.meldungEnthaltenInSammelkarteArt");

            VMclMeldung.meldungEnthaltenInSammelkarteArt_Delayed = ergebnis
                    .getInt("m.meldungEnthaltenInSammelkarteArt_Delayed");
            VMclMeldung.weisungVorhanden = ergebnis.getInt("m.weisungVorhanden");
            VMclMeldung.weisungVorhanden_Delayed = ergebnis.getInt("m.weisungVorhanden_Delayed");
            VMclMeldung.vertreterName = ergebnis.getString("m.vertreterName");
            VMclMeldung.vertreterName_Delayed = ergebnis.getString("m.vertreterName_Delayed");
            VMclMeldung.vertreterVorname = ergebnis.getString("m.vertreterVorname");
            VMclMeldung.vertreterVorname_Delayed = ergebnis.getString("m.vertreterVorname_Delayed");
            VMclMeldung.vertreterOrt = ergebnis.getString("m.vertreterOrt");
            VMclMeldung.vertreterOrt_Delayed = ergebnis.getString("m.vertreterOrt_Delayed");
            VMclMeldung.vertreterIdent = ergebnis.getInt("m.vertreterIdent");
            VMclMeldung.vertreterIdent_Delayed = ergebnis.getInt("m.vertreterIdent_Delayed");
            VMclMeldung.willenserklaerungMitVollmacht = ergebnis.getInt("m.willenserklaerungMitVollmacht");
            VMclMeldung.willenserklaerungMitVollmacht_Delayed = ergebnis
                    .getInt("m.willenserklaerungMitVollmacht_Delayed");
            VMclMeldung.statusPraesenz = ergebnis.getInt("m.statusPraesenz");

            VMclMeldung.statusPraesenz_Delayed = ergebnis.getInt("m.statusPraesenz_Delayed");
            VMclMeldung.statusWarPraesenz = ergebnis.getInt("m.statusWarPraesenz");
            VMclMeldung.statusWarPraesenz_Delayed = ergebnis.getInt("m.statusWarPraesenz_Delayed");
            VMclMeldung.virtuellerTeilnehmerIdent = ergebnis.getInt("m.virtuellerTeilnehmerIdent");
            VMclMeldung.teilnahmeArt = ergebnis.getInt("m.teilnahmeArt");
            VMclMeldung.vorlAnmeldung = ergebnis.getInt("m.vorlAnmeldung");
            VMclMeldung.vorlAnmeldungAkt = ergebnis.getInt("m.vorlAnmeldungAkt");
            VMclMeldung.vorlAnmeldungSer = ergebnis.getInt("m.vorlAnmeldungSer");

        } catch (Exception e) {
            CaBug.drucke("DbMeldungen.decodeErgebnis 001");
            System.err.println(" " + e.getMessage());
        }

        return VMclMeldung;
    }

    /** dekodiert die aktuelle Position aus ergebnis in EvlVIpKZ und gibt dieses zurück
     * Dabei werden nur die Felder gefüllt, die in liefereSelectStringShort selektiert
     * wurden*/
    private EclMeldung decodeErgebnisShort(ResultSet ergebnis) {
        EclMeldung VMclMeldung = new EclMeldung();

        try {

            VMclMeldung.meldungsIdent = ergebnis.getInt("m.meldungsIdent");
            VMclMeldung.db_version = ergebnis.getLong("m.db_version");
            VMclMeldung.mandant = ergebnis.getInt("m.mandant");
            VMclMeldung.meldungAktiv = ergebnis.getInt("m.meldungAktiv");
            VMclMeldung.klasse = ergebnis.getInt("m.klasse");
            VMclMeldung.aktionaersnummer = ergebnis.getString("m.aktionaersnummer");
            VMclMeldung.aktienregisterIdent = ergebnis.getInt("m.aktienregisterIdent");
            VMclMeldung.externeIdent = ergebnis.getString("m.externeIdent");
            VMclMeldung.meldungstyp = ergebnis.getInt("m.meldungstyp");
            VMclMeldung.skMitWeisungen = ergebnis.getInt("m.skMitWeisungen");
            VMclMeldung.skIst = ergebnis.getInt("m.skIst");
            VMclMeldung.skWeisungsartZulaessig = ergebnis.getInt("m.skWeisungsartZulaessig");
            VMclMeldung.skBuchbarInternet = ergebnis.getInt("m.skBuchbarInternet");
            VMclMeldung.skBuchbarPapier = ergebnis.getInt("m.skBuchbarPapier");
            VMclMeldung.skBuchbarHV = ergebnis.getInt("m.skBuchbarHV");
            VMclMeldung.skBuchbarVollmachtDritte = ergebnis.getInt("m.skBuchbarVollmachtDritte");
            VMclMeldung.skBuchbarVollmachtDritteHV = ergebnis.getInt("m.skBuchbarVollmachtDritteHV");
            VMclMeldung.gattung = ergebnis.getInt("m.gattung");
            VMclMeldung.fixAnmeldung = ergebnis.getInt("m.fixAnmeldung");
            VMclMeldung.stueckAktien = ergebnis.getLong("m.stueckAktien");
            VMclMeldung.stimmen = ergebnis.getLong("m.stimmen");
            VMclMeldung.besitzart = ergebnis.getString("m.besitzart");
            VMclMeldung.stimmausschluss = ergebnis.getString("m.stimmausschluss");
            VMclMeldung.name = ergebnis.getString("p.name");
            VMclMeldung.vorname = ergebnis.getString("p.vorname");
            VMclMeldung.ort = ergebnis.getString("p.ort");
//            VMclMeldung.identVersandadresse = ergebnis.getInt("p.identVersandadresse");

            VMclMeldung.delayedVorhanden = ergebnis.getInt("m.delayedVorhanden");
            VMclMeldung.pendingVorhanden = ergebnis.getInt("m.pendingVorhanden");
            VMclMeldung.zutrittsIdent = ergebnis.getString("m.zutrittsIdent");
            VMclMeldung.zutrittsIdent_Delayed = ergebnis.getString("m.zutrittsIdent_Delayed");
            VMclMeldung.stimmkarte = ergebnis.getString("m.stimmkarte");
            VMclMeldung.stimmkarte_Delayed = ergebnis.getString("m.stimmkarte_Delayed");
            VMclMeldung.stimmkarteSecond = ergebnis.getString("m.stimmkarteSecond");
            VMclMeldung.stimmkarteSecond_Delayed = ergebnis.getString("m.stimmkarteSecond_Delayed");

            VMclMeldung.meldungEnthaltenInSammelkarte = ergebnis.getInt("m.meldungEnthaltenInSammelkarte");
            VMclMeldung.meldungEnthaltenInSammelkarte_Delayed = ergebnis.getInt("m.meldungEnthaltenInSammelkarte_Delayed");
            VMclMeldung.meldungEnthaltenInSammelkarteArt = ergebnis.getInt("m.meldungEnthaltenInSammelkarteArt");
            VMclMeldung.meldungEnthaltenInSammelkarteArt_Delayed = ergebnis.getInt("m.meldungEnthaltenInSammelkarteArt_Delayed");
            VMclMeldung.weisungVorhanden = ergebnis.getInt("m.weisungVorhanden");
            VMclMeldung.weisungVorhanden_Delayed = ergebnis.getInt("m.weisungVorhanden_Delayed");
            VMclMeldung.vertreterName = ergebnis.getString("m.vertreterName");
            VMclMeldung.vertreterName_Delayed = ergebnis.getString("m.vertreterName_Delayed");
            VMclMeldung.vertreterVorname = ergebnis.getString("m.vertreterVorname");
            VMclMeldung.vertreterVorname_Delayed = ergebnis.getString("m.vertreterVorname_Delayed");
            VMclMeldung.vertreterOrt = ergebnis.getString("m.vertreterOrt");
            VMclMeldung.vertreterOrt_Delayed = ergebnis.getString("m.vertreterOrt_Delayed");
            VMclMeldung.willenserklaerungMitVollmacht = ergebnis.getInt("m.willenserklaerungMitVollmacht");
            VMclMeldung.willenserklaerungMitVollmacht_Delayed = ergebnis.getInt("m.willenserklaerungMitVollmacht_Delayed");
            VMclMeldung.statusPraesenz = ergebnis.getInt("m.statusPraesenz");
            VMclMeldung.statusPraesenz_Delayed = ergebnis.getInt("m.statusPraesenz_Delayed");
            VMclMeldung.statusWarPraesenz = ergebnis.getInt("m.statusWarPraesenz");
            VMclMeldung.statusWarPraesenz_Delayed = ergebnis.getInt("m.statusWarPraesenz_Delayed");
            VMclMeldung.virtuellerTeilnehmerIdent = ergebnis.getInt("m.virtuellerTeilnehmerIdent");

        } catch (Exception e) {
            CaBug.drucke("DbMeldungen.decodeErgebnisShort 001");
            System.err.println(" " + e.getMessage());
        }

        return VMclMeldung;
    }

    /** Fuellen Prepared Statement mit allen Feldern, beginnend bei offset (i.d.R. =1).
     * Kann sowohl für Insert, als auch für update (2x, mit
     * unterschiedlichem vipKZ - einmal neue Werte, einmal oldversion -) verwendet werden.
     * 
     */
    private int anzfelder = 81; /*Nur Anzahl Felder für DbMeldungen, ohne DbPersonenNatJur!*/

    private void fuellePreparedStatementKomplett(PreparedStatement pstm, int pOffset, EclMeldung pMeldung) {
        /*Erstes ? ist Position 1*/

        int offset = pOffset;

        try {
            pstm.setInt(offset, pMeldung.meldungsIdent);
            offset++;
            pstm.setLong(offset, pMeldung.db_version);
            offset++;
            pstm.setInt(offset, dbBundle.clGlobalVar.mandant);
            offset++;
            pstm.setInt(offset, pMeldung.meldungAktiv);
            offset++;
            pstm.setInt(offset, pMeldung.klasse);
            offset++;
            pstm.setString(offset, pMeldung.aktionaersnummer);
            offset++;
            pstm.setInt(offset, pMeldung.aktienregisterIdent);
            offset++;
            pstm.setString(offset, pMeldung.externeIdent);
            offset++;
            pstm.setInt(offset, pMeldung.meldungstyp);
            offset++;
            pstm.setInt(offset, pMeldung.skMitWeisungen);
            offset++;
            pstm.setInt(offset, pMeldung.skIst);
            offset++;
            pstm.setInt(offset, pMeldung.skWeisungsartZulaessig);
            offset++;

            pstm.setInt(offset, pMeldung.skBuchbarInternet);
            offset++;
            pstm.setInt(offset, pMeldung.skBuchbarPapier);
            offset++;
            pstm.setInt(offset, pMeldung.skBuchbarHV);
            offset++;
            pstm.setInt(offset, pMeldung.skBuchbarVollmachtDritte);
            offset++;
            pstm.setInt(offset, pMeldung.skBuchbarVollmachtDritteHV);
            offset++;
            pstm.setInt(offset, pMeldung.skOffenlegung);
            offset++;
            pstm.setInt(offset, pMeldung.instiIdent);
            offset++;
            pstm.setString(offset, pMeldung.stimmkarteVorab);
            offset++;
            pstm.setString(offset, pMeldung.stimmkarteSecondVorab);
            offset++;
            pstm.setInt(offset, pMeldung.gattung);
            offset++;
            pstm.setInt(offset, pMeldung.fixAnmeldung);
            offset++;
            pstm.setLong(offset, pMeldung.stueckAktien);
            offset++;
            pstm.setLong(offset, pMeldung.stimmen);
            offset++;

            pstm.setLong(offset, pMeldung.stueckAktienDruckEK);
            offset++;
            pstm.setLong(offset, pMeldung.stimmenDruckEK);
            offset++;
            pstm.setString(offset, pMeldung.besitzart);
            offset++;
            pstm.setString(offset, pMeldung.stimmausschluss);
            offset++;
            pstm.setInt(offset, pMeldung.zusatzrechte);
            offset++;

            pstm.setInt(offset, pMeldung.personenNatJurIdent);
            offset++;

            pstm.setString(offset, pMeldung.zusatzfeld1);
            offset++;
            pstm.setString(offset, pMeldung.zusatzfeld2);
            offset++;
            pstm.setString(offset, pMeldung.zusatzfeld3);
            offset++;
            pstm.setString(offset, pMeldung.zusatzfeld4);
            offset++;
            pstm.setString(offset, pMeldung.zusatzfeld5);
            offset++;
            pstm.setInt(offset, pMeldung.gruppe);
            offset++;
            pstm.setString(offset, pMeldung.neuanlageDatumUhrzeit);
            offset++;
            pstm.setString(offset, pMeldung.geaendertDatumUhrzeit);
            offset++;
            pstm.setInt(offset, pMeldung.manuellGeaendertSeitLetztemImport);
            offset++;
            pstm.setInt(offset, pMeldung.delayedVorhanden);
            offset++;

            pstm.setInt(offset, pMeldung.pendingVorhanden);
            offset++;
            pstm.setString(offset, pMeldung.zutrittsIdent);
            offset++;
            pstm.setString(offset, pMeldung.zutrittsIdent_Delayed);
            offset++;
            pstm.setString(offset, pMeldung.stimmkarte);
            offset++;
            pstm.setString(offset, pMeldung.stimmkarte_Delayed);
            offset++;
            pstm.setString(offset, pMeldung.stimmkarteSecond);
            offset++;
            pstm.setString(offset, pMeldung.stimmkarteSecond_Delayed);
            offset++;

            pstm.setInt(offset, pMeldung.willenserklaerung);
            offset++;
            pstm.setInt(offset, pMeldung.willenserklaerung_Delayed);
            offset++;
            pstm.setInt(offset, pMeldung.willenserklaerungIdent);
            offset++;
            pstm.setInt(offset, pMeldung.willenserklaerungIdent_Delayed);
            offset++;
            pstm.setString(offset, pMeldung.veraenderungszeit);
            offset++;
            pstm.setString(offset, pMeldung.veraenderungszeit);
            offset++;
            pstm.setInt(offset, pMeldung.erteiltAufWeg);
            offset++;
            pstm.setInt(offset, pMeldung.erteiltAufWeg_Delayed);
            offset++;

            pstm.setInt(offset, pMeldung.meldungEnthaltenInSammelkarte);
            offset++;
            pstm.setInt(offset, pMeldung.meldungEnthaltenInSammelkarte_Delayed);
            offset++;
            pstm.setInt(offset, pMeldung.meldungEnthaltenInSammelkarteArt);
            offset++;

            pstm.setInt(offset, pMeldung.meldungEnthaltenInSammelkarteArt_Delayed);
            offset++;
            pstm.setInt(offset, pMeldung.weisungVorhanden);
            offset++;
            pstm.setInt(offset, pMeldung.weisungVorhanden_Delayed);
            offset++;
            pstm.setString(offset, CaString.trunc(pMeldung.vertreterName, 80));
            offset++;
            pstm.setString(offset, CaString.trunc(pMeldung.vertreterName_Delayed, 80));
            offset++;
            pstm.setString(offset, CaString.trunc(pMeldung.vertreterVorname, 80));
            offset++;
            pstm.setString(offset, CaString.trunc(pMeldung.vertreterVorname_Delayed, 80));
            offset++;
            pstm.setString(offset, CaString.trunc(pMeldung.vertreterOrt, 80));
            offset++;
            pstm.setString(offset, CaString.trunc(pMeldung.vertreterOrt_Delayed, 80));
            offset++;
            pstm.setInt(offset, pMeldung.vertreterIdent);
            offset++;
            pstm.setInt(offset, pMeldung.vertreterIdent_Delayed);
            offset++;
            pstm.setInt(offset, pMeldung.willenserklaerungMitVollmacht);
            offset++;
            pstm.setInt(offset, pMeldung.willenserklaerungMitVollmacht_Delayed);
            offset++;
            pstm.setInt(offset, pMeldung.statusPraesenz);
            offset++;

            pstm.setInt(offset, pMeldung.statusPraesenz_Delayed);
            offset++;
            pstm.setInt(offset, pMeldung.statusWarPraesenz);
            offset++;
            pstm.setInt(offset, pMeldung.statusWarPraesenz_Delayed);
            offset++;
            pstm.setInt(offset, pMeldung.virtuellerTeilnehmerIdent);
            offset++;
            pstm.setInt(offset, pMeldung.teilnahmeArt);
            offset++;

            pstm.setInt(offset, pMeldung.vorlAnmeldung);
            offset++;
            pstm.setInt(offset, pMeldung.vorlAnmeldungAkt);
            offset++;
            pstm.setInt(offset, pMeldung.vorlAnmeldungSer);
            offset++;

            /*Ab hier in personenNatJur ausgelagert:
            pstm.setInt(offset, pMeldung.anrede);offset++;
            pstm.setString(offset, pMeldung.titel);offset++;
            pstm.setString(offset, pMeldung.adelstitel);offset++;
            pstm.setString(offset, pMeldung.name);offset++;
            pstm.setString(offset, pMeldung.vorname);offset++;
            
            pstm.setString(offset, pMeldung.zuHdCo);offset++;
            pstm.setString(offset, pMeldung.zusatz1);offset++;
            pstm.setString(offset, pMeldung.zusatz2);offset++;
            pstm.setString(offset, pMeldung.strasse);offset++;
            pstm.setString(offset, pMeldung.land);offset++;
            pstm.setString(offset, pMeldung.plz);offset++;
            pstm.setString(offset, pMeldung.ort);offset++;
            pstm.setString(offset, pMeldung.mailadresse);offset++;
            pstm.setInt(offset, pMeldung.kommunikationssprache);offset++;
            pstm.setString(offset, VclMeldung.loginKennung);offset++;
            pstm.setString(offset, VclMeldung.loginPasswort);offset++;
            */

        } catch (SQLException e) {
            CaBug.drucke("DbMeldungen.fuellePreparedStatementKomplett 001");
            System.err.println(" " + e.getMessage());
        }

    }

    /************************************read_PreparedStatement****************************************
     * Interne Read-Routine, Ergebnis in meldungenArray ablegen; nur Basis-Infos, d.h. aus:
     * > DbMeldungen
     * */
    private int read_PreparedStatement(PreparedStatement pstm) {
        int anzInArray = 0;

        try {
            ResultSet ergebnis = pstm.executeQuery();
            ergebnis.last();
            anzInArray = ergebnis.getRow();
            ergebnis.beforeFirst();

            meldungenArray = new EclMeldung[anzInArray];

            int i = 0;
            while (ergebnis.next() == true) {

                meldungenArray[i] = this.decodeErgebnis(ergebnis);
                i++;

            }
            ergebnis.close();
            pstm.close();

        } catch (Exception e) {
            CaBug.drucke("DbMeldungen.read_PreparedStatement 001");
            System.err.println(" " + e.getMessage());
        }

        return (anzInArray);

    }

    /******************************************************************************
     * Einlesen von einer oder mehrerer Meldungen, Suche über lMeldung.
     *  Derzeit implementiert: Suche nach (jeweils):
     *  > pMeldung.aktionaersnummer
     *  
     *  Rückgabewert: anz der gefundenen Sätze
     */
    public int leseZuAktionaersnummer(EclMeldung pMeldung) {
        String sql = "";
        sql = "SELECT * from " + dbBundle.getSchemaMandant() + "tbl_meldungen m " + "INNER JOIN "
                + dbBundle.getSchemaMandant() + "tbl_personenNatJur p ON m.personenNatJurIdent=p.ident "
                + "where m.meldungAktiv=1 and m.aktionaersnummer=? " + " and m.mandant=? and p.mandant=?";

        PreparedStatement pstm1 = null;
        try {
            pstm1 = verbindung.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            pMeldung.aktionaersnummer = BlAktienregisterNummerAufbereiten.aufbereitenFuerDatenbankZugriff(
                    pMeldung.aktionaersnummer, dbBundle.param.paramBasis.laengeAktionaersnummer,
                    dbBundle.clGlobalVar.mandant);
            pstm1.setString(1, pMeldung.aktionaersnummer);
            pstm1.setInt(2, dbBundle.clGlobalVar.mandant);
            pstm1.setInt(3, dbBundle.clGlobalVar.mandant);
        } catch (SQLException e) {
            CaBug.drucke("DbMeldungen.leseZuAktionaersnummer 001");
            System.err.println(" " + e.getMessage());
        }

        return (read_PreparedStatement(pstm1));

    }
 
    public int leseZuAktionaersnummer(String pAktionaersnummer) {
        String sql = "";
        sql = "SELECT * from " + dbBundle.getSchemaMandant() + "tbl_meldungen m " + "INNER JOIN "
                + dbBundle.getSchemaMandant() + "tbl_personenNatJur p ON m.personenNatJurIdent=p.ident "
                + "where m.meldungAktiv=1 and m.aktionaersnummer=? " + " and m.mandant=? and p.mandant=?";

        PreparedStatement pstm1 = null;
        try {
            pstm1 = verbindung.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            pAktionaersnummer = BlAktienregisterNummerAufbereiten.aufbereitenFuerDatenbankZugriff(
                    pAktionaersnummer, dbBundle.param.paramBasis.laengeAktionaersnummer,
                    dbBundle.clGlobalVar.mandant);
            pstm1.setString(1, pAktionaersnummer);
            pstm1.setInt(2, dbBundle.clGlobalVar.mandant);
            pstm1.setInt(3, dbBundle.clGlobalVar.mandant);
        } catch (SQLException e) {
            CaBug.drucke("DbMeldungen.leseZuAktionaersnummer 001");
            System.err.println(" " + e.getMessage());
        }

        return (read_PreparedStatement(pstm1));

    }

    /******************************************************************************
     * Einlesen von einer oder mehrerer Meldungen, Suche über lMeldung.
     *  Derzeit implementiert: Suche nach (jeweils):
     *  > pMeldung.aktionaersnummer
     *  
     *  Rückgabewert: anz der gefundenen Sätze
     */
    public int leseZuZusatzfeld3(EclMeldung pMeldung) {
        String sql = "";
        sql = "SELECT * from " + dbBundle.getSchemaMandant() + "tbl_meldungen m " + "INNER JOIN "
                + dbBundle.getSchemaMandant() + "tbl_personenNatJur p ON m.personenNatJurIdent=p.ident "
                + "where m.meldungAktiv=1 and m.zusatzfeld3=? " + " and m.mandant=? and p.mandant=?";

        PreparedStatement pstm1 = null;
        try {
            pstm1 = verbindung.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            pstm1.setString(1, pMeldung.zusatzfeld3);
            pstm1.setInt(2, dbBundle.clGlobalVar.mandant);
            pstm1.setInt(3, dbBundle.clGlobalVar.mandant);
        } catch (SQLException e) {
            CaBug.drucke("DbMeldungen.leseZuZusatzfeld3 001");
            System.err.println(" " + e.getMessage());
        }

        return (read_PreparedStatement(pstm1));

    }

    public int leseZuZusatzfeld5() {
        String sql = "";
        sql = "SELECT * from " + dbBundle.getSchemaMandant() + "tbl_meldungen m " + "INNER JOIN "
                + dbBundle.getSchemaMandant() + "tbl_personenNatJur p ON m.personenNatJurIdent=p.ident "
                + "where m.meldungAktiv=1 and m.zusatzfeld5='1' " + " and m.mandant=? and p.mandant=?";

        PreparedStatement pstm1 = null;
        try {
            pstm1 = verbindung.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            pstm1.setInt(1, dbBundle.clGlobalVar.mandant);
            pstm1.setInt(2, dbBundle.clGlobalVar.mandant);
        } catch (SQLException e) {
            CaBug.drucke("DbMeldungen.leseZuZusatzfeld3 001");
            System.err.println(" " + e.getMessage());
        }

        return (read_PreparedStatement(pstm1));

    }

    public int leseEkNummer(String pEkNummer) {
        String sql = "";
        sql = "SELECT * from " + dbBundle.getSchemaMandant() + "tbl_meldungen m " + "INNER JOIN "
                + dbBundle.getSchemaMandant() + "tbl_personenNatJur p ON m.personenNatJurIdent=p.ident "
                + "where m.meldungAktiv=1 and m.zutrittsIdent=? and m.mandant=? and p.mandant=?";

        PreparedStatement pstm1 = null;
        try {
            pstm1 = verbindung.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            pstm1.setString(1, pEkNummer);
            pstm1.setInt(2, dbBundle.clGlobalVar.mandant);
            pstm1.setInt(3, dbBundle.clGlobalVar.mandant);
        } catch (SQLException e) {
            CaBug.drucke("DbMeldungen.leseZuZusatzfeld3 001");
            System.err.println(" " + e.getMessage());
        }

        return (read_PreparedStatement(pstm1));

    }

    /******************************************************************************
     * Einlesen von einer Meldung, Suche über Ident.
     * 
     * Liefert nur aktive Sätze zurück!
     * 
     *  Rückgabewert: anz der gefundenen Sätze
     */
    public int leseZuIdent(int pIdent) {
        String sql = "";
        sql = "SELECT * from " + dbBundle.getSchemaMandant() + "tbl_meldungen m " + "INNER JOIN "
                + dbBundle.getSchemaMandant() + "tbl_personenNatJur p ON m.personenNatJurIdent=p.ident "
                + "where m.meldungAktiv=1 and m.meldungsIdent=? " + " and m.mandant=? and p.mandant=?";

        PreparedStatement pstm1 = null;
        try {
            pstm1 = verbindung.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            pstm1.setInt(1, pIdent);
            pstm1.setInt(2, dbBundle.clGlobalVar.mandant);
            pstm1.setInt(3, dbBundle.clGlobalVar.mandant);
        } catch (SQLException e) {
            CaBug.drucke("DbMeldungen.leseZuIdent 001");
            System.err.println(" " + e.getMessage());
        }

        return (read_PreparedStatement(pstm1));

    }

    /******************************************************************************
     * Einlesen von einer oder mehrerer Meldungen, Suche über lMeldung.
     *  Derzeit implementiert: Suche nach (jeweils):
     *  > pMeldung.personenNatJurIdent
     *  
     *  Rückgabewert: anz der gefundenen Sätze
     */
    public int leseZuPersonenNatJurIdent(EclMeldung pMeldung) {
        String sql = "";
        sql = "SELECT * from " + dbBundle.getSchemaMandant() + "tbl_meldungen m " + "INNER JOIN "
                + dbBundle.getSchemaMandant() + "tbl_personenNatJur p ON m.personenNatJurIdent=p.ident "
                + "where m.personenNatJurIdent=? AND m.meldungAktiv=1" + " and m.mandant=? and p.mandant=?";

        PreparedStatement pstm1 = null;
        try {
            pstm1 = verbindung.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            pstm1.setInt(1, pMeldung.personenNatJurIdent);
            pstm1.setInt(2, dbBundle.clGlobalVar.mandant);
            pstm1.setInt(3, dbBundle.clGlobalVar.mandant);
        } catch (SQLException e) {
            CaBug.drucke("DbMeldungen.leseZuPersonenNatJurIdent 001");
            System.err.println(" " + e.getMessage());
        }

        return (read_PreparedStatement(pstm1));

    }

    public int leseZuPersonenNatJurIdent(int pIdent) {
        String sql = "";
        sql = "SELECT * from " + dbBundle.getSchemaMandant() + "tbl_meldungen m " + "INNER JOIN "
                + dbBundle.getSchemaMandant() + "tbl_personenNatJur p ON m.personenNatJurIdent=p.ident "
                + "where m.personenNatJurIdent=? AND m.meldungAktiv=1" + " and m.mandant=? and p.mandant=?";

        PreparedStatement pstm1 = null;
        try {
            pstm1 = verbindung.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            pstm1.setInt(1, pIdent);
            pstm1.setInt(2, dbBundle.clGlobalVar.mandant);
            pstm1.setInt(3, dbBundle.clGlobalVar.mandant);
        } catch (SQLException e) {
            CaBug.drucke("001");
            System.err.println(" " + e.getMessage());
        }

        return (read_PreparedStatement(pstm1));

    }

    /******************************************************************************
     * Einlesen von einer oder mehrerer Meldungen, Suche über lMeldung, eingegrenzt auf Klasse.
     *  Derzeit implementiert: Suche nach (jeweils):
     *  > pMeldung.personenNatJurIdent
     *  
     *  Rückgabewert: anz der gefundenen Sätze
     */
    public int leseZuPersonenNatJurIdent(EclMeldung pMeldung, int pKlasse) {
        String sql = "";
        sql = "SELECT * from " + dbBundle.getSchemaMandant() + "tbl_meldungen m " + "INNER JOIN "
                + dbBundle.getSchemaMandant() + "tbl_personenNatJur p ON m.personenNatJurIdent=p.ident "
                + "where m.meldungAktiv=1 and m.personenNatJurIdent=? "
                + " and m.mandant=? and p.mandant=? and m.klasse=?";

        PreparedStatement pstm1 = null;
        try {
            pstm1 = verbindung.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            pstm1.setInt(1, pMeldung.personenNatJurIdent);
            pstm1.setInt(2, dbBundle.clGlobalVar.mandant);
            pstm1.setInt(3, dbBundle.clGlobalVar.mandant);
            pstm1.setInt(4, pKlasse);
        } catch (SQLException e) {
            CaBug.drucke("DbMeldungen.leseZuPersonenNatJurIdent 001");
            System.err.println(" " + e.getMessage());
        }

        return (read_PreparedStatement(pstm1));

    }

    /**pSortierung 1 => zutrittsIdent
     * pSortierung 2 => Nachname*/
    public int leseMeldungenMitEKSelektionGattung(int pGattung, boolean pAktionaereNichtInSammelkarte, 
            boolean pAktionaereInSammelkarte, boolean pSammelkarten, int pSortierung) {
        String sql = "";
        sql = "SELECT * from " + dbBundle.getSchemaMandant() + "tbl_meldungen m " + "INNER JOIN "
                + dbBundle.getSchemaMandant() + "tbl_personenNatJur p ON m.personenNatJurIdent=p.ident "
                + "where m.meldungAktiv=1 AND m.zutrittsIdent!='' AND ( ";
        
        boolean selektionEnthalten=false;
        if (pAktionaereNichtInSammelkarte) {
            if (selektionEnthalten==true) {
                sql+=" OR ";
            }
            selektionEnthalten=true;
            sql+=" m.meldungstyp=1 ";
        }
        if (pAktionaereInSammelkarte) {
            if (selektionEnthalten==true) {
                sql+=" OR ";
            }
            selektionEnthalten=true;
            sql+=" m.meldungstyp=3 ";
        }
        if (pSammelkarten) {
            if (selektionEnthalten==true) {
                sql+=" OR ";
            }
            selektionEnthalten=true;
            sql+=" m.meldungstyp=2 ";
        }
       
        sql+=" ) AND m.mandant=? and p.mandant=? ";
        if (pGattung > 0) {
            sql = sql + " and m.gattung=?";
        }

        if (pSortierung==1) {
            sql=sql+" ORDER BY m.zutrittsIdent ";
        }
        if (pSortierung==2) {
            sql=sql+" ORDER BY p.name ";
        }
            
        PreparedStatement pstm1 = null;
        try {
            pstm1 = verbindung.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            pstm1.setInt(1, dbBundle.clGlobalVar.mandant);
            pstm1.setInt(2, dbBundle.clGlobalVar.mandant);
            if (pGattung > 0) {
                pstm1.setInt(3, pGattung);
            }
        } catch (SQLException e) {
            CaBug.drucke("DbMeldungen.leseAlleMeldungenGattung 001");
            System.err.println(" " + e.getMessage());
        }

        return (read_PreparedStatement(pstm1));

    }

    /**************************Alle Meldungen***********************
     */
    /**pGattung =-1 => alle; sonst nur die Meldungen (also auch Sammelkarten!) der entsprechenden Gattung*/

    public int leseAlleMeldungenGattung(int pGattung) {
        String sql = "";
        sql = "SELECT * from " + dbBundle.getSchemaMandant() + "tbl_meldungen m " + "INNER JOIN "
                + dbBundle.getSchemaMandant() + "tbl_personenNatJur p ON m.personenNatJurIdent=p.ident "
                + "where m.meldungAktiv=1 and m.mandant=? and p.mandant=? ";
        if (pGattung > 0) {
            sql = sql + " and m.gattung=?";
        }

        PreparedStatement pstm1 = null;
        try {
            pstm1 = verbindung.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            pstm1.setInt(1, dbBundle.clGlobalVar.mandant);
            pstm1.setInt(2, dbBundle.clGlobalVar.mandant);
            if (pGattung > 0) {
                pstm1.setInt(3, pGattung);
            }
        } catch (SQLException e) {
            CaBug.drucke("DbMeldungen.leseAlleMeldungenGattung 001");
            System.err.println(" " + e.getMessage());
        }

        return (read_PreparedStatement(pstm1));

    }

    public int leseAlleMeldungen() {
        return leseAlleMeldungenGattung(-1);
    }

    /**pGattung =-1 => alle; sonst nur die Meldungen (also auch Sammelkarten!) der entsprechenden Gattung.
     * Keine Gäste, sondern nur Aktionäre*/
    public int leseAlleMeldungenAnwesend(int pGattung, boolean pMitOnline) {
        String sql = "";
        sql = "SELECT * from " + dbBundle.getSchemaMandant() + "tbl_meldungen m " + "INNER JOIN "
                + dbBundle.getSchemaMandant() + "tbl_personenNatJur p ON m.personenNatJurIdent=p.ident "
                + "where (m.statusPraesenz=1";
        if (pMitOnline) {
            sql=sql+" OR m.zusatzfeld4='1'";
        }
        sql=sql + ") and klasse=1 and m.mandant=? and p.mandant=? ";
        if (pGattung > 0) {
            sql = sql + " and m.gattung=?";
        }
        sql=sql+" ORDER BY m.aktionaersnummer";

        PreparedStatement pstm1 = null;
        try {
            pstm1 = verbindung.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            pstm1.setInt(1, dbBundle.clGlobalVar.mandant);
            pstm1.setInt(2, dbBundle.clGlobalVar.mandant);
            if (pGattung > 0) {
                pstm1.setInt(3, pGattung);
            }
        } catch (SQLException e) {
            CaBug.drucke("DbMeldungen.leseAlleMeldungenGattung 001");
            System.err.println(" " + e.getMessage());
        }

        return (read_PreparedStatement(pstm1));
    }

    /**pGattung =-1 => alle; sonst nur die Meldungen (also auch Sammelkarten!) der entsprechenden Gattung.
     * Keine Gäste, sondern nur Aktionäre*/
    public int leseAlleMeldungenIstOderWarAnwesend(int pGattung, boolean pMitOnline) {
        String sql = "";
        sql = "SELECT * from " + dbBundle.getSchemaMandant() + "tbl_meldungen m " + "INNER JOIN "
                + dbBundle.getSchemaMandant() + "tbl_personenNatJur p ON m.personenNatJurIdent=p.ident "
                + "where (m.statusPraesenz=1 OR m.statusPraesenz=2";
        if (pMitOnline) {
            sql=sql+" OR m.zusatzfeld4='1' OR m.zusatzfeld4='2'";
        }
        sql=sql + ") and klasse=1 and m.mandant=? and p.mandant=? ";
        if (pGattung > 0) {
            sql = sql + " and m.gattung=?";
        }
        sql=sql+" ORDER BY m.aktionaersnummer";

        PreparedStatement pstm1 = null;
        try {
            pstm1 = verbindung.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            pstm1.setInt(1, dbBundle.clGlobalVar.mandant);
            pstm1.setInt(2, dbBundle.clGlobalVar.mandant);
            if (pGattung > 0) {
                pstm1.setInt(3, pGattung);
            }
        } catch (SQLException e) {
            CaBug.drucke("DbMeldungen.leseAlleMeldungenGattung 001");
            System.err.println(" " + e.getMessage());
        }

        return (read_PreparedStatement(pstm1));
    }

    /**************************Alle Meldungen mit Stimmausschluß***********************
     */
    public int leseStimmausschluss() {

        String sql = "";
        sql = "SELECT * from " + dbBundle.getSchemaMandant() + "tbl_meldungen m " + "INNER JOIN "
                + dbBundle.getSchemaMandant() + "tbl_personenNatJur p ON m.personenNatJurIdent=p.ident "
                + "where m.meldungAktiv=1 and m.stimmausschluss!='' " + " and m.mandant=? and p.mandant=?";

        PreparedStatement pstm1 = null;
        try {
            pstm1 = verbindung.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            pstm1.setInt(1, dbBundle.clGlobalVar.mandant);
            pstm1.setInt(2, dbBundle.clGlobalVar.mandant);
        } catch (SQLException e) {
            CaBug.drucke("DbMeldungen.leseSammelkarteSRVInternet 001");
            System.err.println(" " + e.getMessage());
        }

        return (read_PreparedStatement(pstm1));

    }

    /**************************Alle Meldungen mit Stimmausschluß - einzelnes Kennzeichen***********************
     */
    public int leseStimmausschlussKZ(String pKennzeichen) {

        String sql = "";
        sql = "SELECT * from " + dbBundle.getSchemaMandant() + "tbl_meldungen m " + "INNER JOIN "
                + dbBundle.getSchemaMandant() + "tbl_personenNatJur p ON m.personenNatJurIdent=p.ident "
                + "where m.meldungAktiv=1 " + " and m.mandant=? and p.mandant=? and m.stimmausschluss like ? ";

        PreparedStatement pstm1 = null;
        try {
            pstm1 = verbindung.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            pstm1.setInt(1, dbBundle.clGlobalVar.mandant);
            pstm1.setInt(2, dbBundle.clGlobalVar.mandant);
            pstm1.setString(3, "%" + pKennzeichen + "%");
        } catch (SQLException e) {
            CaBug.drucke("DbMeldungen.leseSammelkarteSRVInternet 001");
            System.err.println(" " + e.getMessage());
        }

        return (read_PreparedStatement(pstm1));

    }

    /**Liefert alle Meldungen, für die eine freiwillige An- oder Abmeldungp erfolgt ist*/
    public int leseAlleMeldungenFreiwilligeAnAbmeldung() {
        String sql = "";
        sql = "SELECT * from " + dbBundle.getSchemaMandant() + "tbl_meldungen m " + "INNER JOIN "
                + dbBundle.getSchemaMandant() + "tbl_personenNatJur p ON m.personenNatJurIdent=p.ident "
                + "where m.vorlAnmeldung>0 and m.mandant=? and p.mandant=? ";
        sql=sql+" ORDER BY m.aktionaersnummer";

        PreparedStatement pstm1 = null;
        try {
            pstm1 = verbindung.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            pstm1.setInt(1, dbBundle.clGlobalVar.mandant);
            pstm1.setInt(2, dbBundle.clGlobalVar.mandant);
        } catch (SQLException e) {
            CaBug.drucke("001");
            System.err.println(" " + e.getMessage());
        }

        return (read_PreparedStatement(pstm1));
    }


    /**Liefert alle Meldungen, die in einer Sammelkarte sind und 0 Aktien haben*/
    public int leseAlleMeldungenInSammelkarteMitNullBestand() {
        String sql = "";
        sql = "SELECT * from " + dbBundle.getSchemaMandant() + "tbl_meldungen m " + "INNER JOIN "
                + dbBundle.getSchemaMandant() + "tbl_personenNatJur p ON m.personenNatJurIdent=p.ident "
                + "where m.meldungstyp=3 and m.stueckAktien=0 ";
        sql=sql+" ORDER BY m.aktionaersnummer";

        PreparedStatement pstm1 = null;
        try {
            pstm1 = verbindung.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            pstm1.setInt(1, dbBundle.clGlobalVar.mandant);
            pstm1.setInt(2, dbBundle.clGlobalVar.mandant);
        } catch (SQLException e) {
            CaBug.drucke("001");
            System.err.println(" " + e.getMessage());
        }

        return (read_PreparedStatement(pstm1));
    }

    /**************************Sammelkartennr für Vollmacht/Weisung Internet ermitteln************************
     * Vorgehen: erste mit Erteilung über Internet auswählen, nach den Kriterien:
     * meldungstyp=2 (=> Sammelkarte)
     * skBuchbarInternet=1 (=> Im internet bebuchbar)
     * skIst=2 (=> Stimmrechtsvertreter)
     * 
     * pGattung=-1 => alle, sonst nur die mit Gattung
     */
    public int leseSammelkarteSRVInternet(int pGattung) {
        return leseSammelkarteSRVInternet(pGattung, false);
    }
    
    public int leseSammelkarteSRVInternet(int pGattung, boolean pMitInaktiven) {

        String sql = "";
        sql = "SELECT * from " + dbBundle.getSchemaMandant() + "tbl_meldungen m " + "INNER JOIN "
                + dbBundle.getSchemaMandant() + "tbl_personenNatJur p ON m.personenNatJurIdent=p.ident "
                + "where m.meldungstyp=2 " + " and m.skBuchbarInternet=1 and m.skIst=2 "
                + " and m.mandant=? and p.mandant=?";
        if (pGattung != -1) {
            sql = sql + " and m.gattung=?";
        }
        if (pMitInaktiven==false) {
            sql=sql+" and m.meldungAktiv=1";
        }

        PreparedStatement pstm1 = null;
        try {
            pstm1 = verbindung.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            pstm1.setInt(1, dbBundle.clGlobalVar.mandant);
            pstm1.setInt(2, dbBundle.clGlobalVar.mandant);
            if (pGattung != -1) {
                pstm1.setInt(3, pGattung);
            }
        } catch (SQLException e) {
            CaBug.drucke("DbMeldungen.leseSammelkarteSRVInternet 001");
            System.err.println(" " + e.getMessage());
        }

        return (read_PreparedStatement(pstm1));

    }

    /**************************Sammelkartennr für Vollmacht/Weisung Papier ermitteln************************
     * Vorgehen: erste mit Erteilung über Internet auswählen, nach den Kriterien:
     * meldungstyp=2 (=> Sammelkarte)
     * skBuchbarPapier=1 (=> Papier bebuchbar)
     * skIst=2 (=> Stimmrechtsvertreter)
     * 
     * pGattung=-1 => alle, sonst nur die mit Gattung
     */
    public int leseSammelkarteSRVPapier(int pGattung) {
        return leseSammelkarteSRVPapier(pGattung, false);
    }
    
    public int leseSammelkarteSRVPapier(int pGattung, boolean pMitInaktiven) {

        String sql = "";
        sql = "SELECT * from " + dbBundle.getSchemaMandant() + "tbl_meldungen m " + "INNER JOIN "
                + dbBundle.getSchemaMandant() + "tbl_personenNatJur p ON m.personenNatJurIdent=p.ident "
                + "where m.meldungstyp=2 " + " and m.skBuchbarPapier=1 and m.skIst=2 "
                + " and m.mandant=? and p.mandant=?";
        if (pGattung != -1) {
            sql = sql + " and m.gattung=?";
        }
        if (pMitInaktiven==false) {
            sql=sql+" and m.meldungAktiv=1";
        }

        PreparedStatement pstm1 = null;
        try {
            pstm1 = verbindung.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            pstm1.setInt(1, dbBundle.clGlobalVar.mandant);
            pstm1.setInt(2, dbBundle.clGlobalVar.mandant);
            if (pGattung != -1) {
                pstm1.setInt(3, pGattung);
            }
        } catch (SQLException e) {
            CaBug.drucke("DbMeldungen.leseSammelkarteSRVPapier 001");
            System.err.println(" " + e.getMessage());
        }

        return (read_PreparedStatement(pstm1));

    }

    /**************************Sammelkartennr für Vollmacht/Weisung HV ermitteln************************
     * Vorgehen: erste mit Erteilung über Internet auswählen, nach den Kriterien:
     * meldungstyp=2 (=> Sammelkarte)
     * skBuchbarHV=1 (=> HV bebuchbar)
     * skIst=2 (=> Stimmrechtsvertreter)
     * 
     * pGattung=-1 => alle, sonst nur die mit Gattung
     */
    public int leseSammelkarteSRVHV(int pGattung) {
        return leseSammelkarteSRVHV(pGattung, false);
    }
    
    public int leseSammelkarteSRVHV(int pGattung, boolean pMitInaktiven) {

        String sql = "";
        sql = "SELECT * from " + dbBundle.getSchemaMandant() + "tbl_meldungen m " + "INNER JOIN "
                + dbBundle.getSchemaMandant() + "tbl_personenNatJur p ON m.personenNatJurIdent=p.ident "
                + "where m.meldungstyp=2 " + " and m.skBuchbarHV=1 and m.skIst=2 "
                + " and m.mandant=? and p.mandant=?";
        if (pGattung != -1) {
            sql = sql + " and m.gattung=?";
        }
        if (pMitInaktiven==false) {
            sql=sql+" and m.meldungAktiv=1";
        }

        PreparedStatement pstm1 = null;
        try {
            pstm1 = verbindung.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            pstm1.setInt(1, dbBundle.clGlobalVar.mandant);
            pstm1.setInt(2, dbBundle.clGlobalVar.mandant);
            if (pGattung != -1) {
                pstm1.setInt(3, pGattung);
            }
        } catch (SQLException e) {
            CaBug.drucke("DbMeldungen.leseSammelkarteSRVHV 001");
            System.err.println(" " + e.getMessage());
        }

        return (read_PreparedStatement(pstm1));

    }

    /**************************Sammelkartennr für Vollmacht/Weisung insgesamt ermitteln************************
     * Vorgehen: erste mit Erteilung über Internet auswählen, nach den Kriterien:
     * meldungstyp=2 (=> Sammelkarte)
     * skIst=2 (=> Stimmrechtsvertreter)
     * 
     * pGattung=-1 => alle, sonst nur die mit Gattung
     */
    public int leseSammelkarteSRV(int pGattung) {
        return leseSammelkarteSRV(pGattung, false);
    }
    
    public int leseSammelkarteSRV(int pGattung, boolean pMitInaktiven) {

        String sql = "";
        sql = "SELECT * from " + dbBundle.getSchemaMandant() + "tbl_meldungen m " + "INNER JOIN "
                + dbBundle.getSchemaMandant() + "tbl_personenNatJur p ON m.personenNatJurIdent=p.ident "
                + "where m.meldungstyp=2 " + " and m.skIst=2 "
                + " and m.mandant=? and p.mandant=?";
        if (pGattung != -1) {
            sql = sql + " and m.gattung=?";
        }
        if (pMitInaktiven==false) {
            sql=sql+" and m.meldungAktiv=1";
        }

        PreparedStatement pstm1 = null;
        try {
            pstm1 = verbindung.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            pstm1.setInt(1, dbBundle.clGlobalVar.mandant);
            pstm1.setInt(2, dbBundle.clGlobalVar.mandant);
            if (pGattung != -1) {
                pstm1.setInt(3, pGattung);
            }
        } catch (SQLException e) {
            CaBug.drucke("DbMeldungen.leseSammelkarteSRVHV 001");
            System.err.println(" " + e.getMessage());
        }

        return (read_PreparedStatement(pstm1));

    }

    /**************************Sammelkartennr für Briefwahl Internet ermitteln************************
     * Vorgehen: erste mit Erteilung über Internet auswählen, nach den Kriterien:
     * meldungstyp=2 (=> Sammelkarte)
     * skBuchbarInternet=1 (=> Im internet bebuchbar)
     * skIst=4 (=> Briefwahl)
     * 
     * pGattung=-1 => alle, sonst nur die mit Gattung
     */
    public int leseSammelkarteBriefwahlInternet(int pGattung) {
        return leseSammelkarteBriefwahlInternet(pGattung, false);
    }
    
    public int leseSammelkarteBriefwahlInternet(int pGattung, boolean pMitInaktiven) {

        String sql = "";
        sql = "SELECT * from " + dbBundle.getSchemaMandant() + "tbl_meldungen m " + "INNER JOIN "
                + dbBundle.getSchemaMandant() + "tbl_personenNatJur p ON m.personenNatJurIdent=p.ident "
                + "where m.meldungstyp=2 " + " and m.skBuchbarInternet=1 and m.skIst=4 "
                + " and m.mandant=? and p.mandant=?";
        if (pGattung != -1) {
            sql = sql + " and m.gattung=?";
        }
        if (pMitInaktiven==false) {
            sql=sql+" and m.meldungAktiv=1";
        }

        PreparedStatement pstm1 = null;
        try {
            pstm1 = verbindung.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            pstm1.setInt(1, dbBundle.clGlobalVar.mandant);
            pstm1.setInt(2, dbBundle.clGlobalVar.mandant);
            if (pGattung != -1) {
                pstm1.setInt(3, pGattung);
            }
        } catch (SQLException e) {
            CaBug.drucke("DbMeldungen.leseSammelkarteBriefwahlInternet 001");
            System.err.println(" " + e.getMessage());
        }

        return (read_PreparedStatement(pstm1));

    }

    /**************************Sammelkartennr für Briefwahl Papier ermitteln************************
     * Vorgehen: erste mit Erteilung über Internet auswählen, nach den Kriterien:
     * meldungstyp=2 (=> Sammelkarte)
     * skBuchbarInternet=1 (=> Im internet bebuchbar)
     * skIst=4 (=> Briefwahl)
     * 
     * pGattung=-1 => alle, sonst nur die mit Gattung
     */
    public int leseSammelkarteBriefwahlPapier(int pGattung) {
        return leseSammelkarteBriefwahlPapier(pGattung, false);
        
    }

    public int leseSammelkarteBriefwahlPapier(int pGattung, boolean pMitInaktiven) {

        String sql = "";
        sql = "SELECT * from " + dbBundle.getSchemaMandant() + "tbl_meldungen m " + "INNER JOIN "
                + dbBundle.getSchemaMandant() + "tbl_personenNatJur p ON m.personenNatJurIdent=p.ident "
                + "where m.meldungstyp=2 " + " and m.skBuchbarPapier=1 and m.skIst=4 "
                + " and m.mandant=? and p.mandant=?";
        if (pGattung != -1) {
            sql = sql + " and m.gattung=?";
        }
        if (pMitInaktiven==false) {
            sql=sql+" and m.meldungAktiv=1";
        }

        PreparedStatement pstm1 = null;
        try {
            pstm1 = verbindung.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            pstm1.setInt(1, dbBundle.clGlobalVar.mandant);
            pstm1.setInt(2, dbBundle.clGlobalVar.mandant);
            if (pGattung != -1) {
                pstm1.setInt(3, pGattung);
            }
        } catch (SQLException e) {
            CaBug.drucke("DbMeldungen.leseSammelkarteBriefwahlInternet 001");
            System.err.println(" " + e.getMessage());
        }

        return (read_PreparedStatement(pstm1));

    }

    /**************************Sammelkarten Briefwahl einlesen************************
     * meldungstyp=2 (=> Sammelkarte)
     * skIst=4 (=> Briefwahl)
     * 
     * pGattung=-1 => alle, sonst nur die mit Gattung
     */
    public int leseSammelkartenBriefwahl(int pGattung) {

        String sql = "";
        sql = "SELECT * from " + dbBundle.getSchemaMandant() + "tbl_meldungen m " + "INNER JOIN "
                + dbBundle.getSchemaMandant() + "tbl_personenNatJur p ON m.personenNatJurIdent=p.ident "
                + "where m.meldungAktiv=1 and m.meldungstyp=2 " + " and m.skIst=4 "
                + " and m.mandant=? and p.mandant=?";
        if (pGattung != -1) {
            sql = sql + " and m.gattung=?";
        }

        PreparedStatement pstm1 = null;
        try {
            pstm1 = verbindung.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            pstm1.setInt(1, dbBundle.clGlobalVar.mandant);
            pstm1.setInt(2, dbBundle.clGlobalVar.mandant);
            if (pGattung != -1) {
                pstm1.setInt(3, pGattung);
            }
        } catch (SQLException e) {
            CaBug.drucke("DbMeldungen.leseSammelkartenBriefwahl 001");
            System.err.println(" " + e.getMessage());
        }

        return (read_PreparedStatement(pstm1));
    }

    /**************************Sammelkartennr für KIAV internet ermitteln************************
     * Vorgehen: erste mit Erteilung über Internet auswählen, nach den Kriterien:
     * meldungstyp=2 (=> Sammelkarte)
     * skBuchbarInternet=1 (=> Im internet bebuchbar)
     * skIst=1 (=> KIAV)
     * 
     * pGattung=-1 => alle, sonst nur die mit Gattung
     */
    public int leseSammelkarteKIAVInternet(int pGattung) {
        return leseSammelkarteKIAVIntern(pGattung, 2);
    }

    /**************************Sammelkartennr für KIAV papier ermitteln************************
     * Selekn:
     * meldungstyp=2 (=> Sammelkarte)
     * skBuchbarPapier=1 (=> Im internet bebuchbar)
     * skIst=1 (=> KIAV)
     * 
     * pGattung=-1 => alle, sonst nur die mit Gattung
     */
    public int leseSammelkarteKIAVPapier(int pGattung) {
        return leseSammelkarteKIAVIntern(pGattung, 1);
    }

    public int leseSammelkarteKIAV(int pGattung) {
        return leseSammelkarteKIAVIntern(pGattung, 0);
    }

    /**art=0 => alle, =1 => Papier, =2 => Internet*/
    private int leseSammelkarteKIAVIntern(int pGattung, int art) {

        String sql = "";
        sql = "SELECT * from " + dbBundle.getSchemaMandant() + "tbl_meldungen m " + "INNER JOIN "
                + dbBundle.getSchemaMandant() + "tbl_personenNatJur p ON m.personenNatJurIdent=p.ident "
                + "where m.meldungAktiv=1 and m.meldungstyp=2 ";
        if (art == 1) {
            sql = sql + " and m.skBuchbarPapier=1 ";
        }
        if (art == 2) {
            sql = sql + " and m.skBuchbarInternet=1 ";
        }
        sql = sql + "and m.skIst=1 " + " and m.mandant=? and p.mandant=?";
        if (pGattung != -1) {
            sql = sql + " and m.gattung=?";
        }

        PreparedStatement pstm1 = null;
        try {
            pstm1 = verbindung.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            pstm1.setInt(1, dbBundle.clGlobalVar.mandant);
            pstm1.setInt(2, dbBundle.clGlobalVar.mandant);
            if (pGattung != -1) {
                pstm1.setInt(3, pGattung);
            }
        } catch (SQLException e) {
            CaBug.drucke("DbMeldungen.leseSammelkarteBriefwahlInternet 001");
            System.err.println(" " + e.getMessage());
        }

        return (read_PreparedStatement(pstm1));

    }

    /**************************Sammelkarten KIAV für Vollmacht Dritte (HV - intern) ermitteln************************
     * meldungstyp=2 (=> Sammelkarte)
     * skBuchbarVollmachtDritteHV=1 
     * skIst=1 (=> KIAV)
     * 
     * pGattung=-1 => alle, sonst nur die mit Gattung
     */
    public int leseSammelkarteKIAVVollmachtDritteHV(int pGattung) {

        String sql = "";
        sql = "SELECT * from " + dbBundle.getSchemaMandant() + "tbl_meldungen m " + "INNER JOIN "
                + dbBundle.getSchemaMandant() + "tbl_personenNatJur p ON m.personenNatJurIdent=p.ident "
                + "where m.meldungAktiv=1 and m.meldungstyp=2 " + " and m.skBuchbarVollmachtDritteHV=1 and m.skIst=1 "
                + " and m.mandant=? and p.mandant=?";
        if (pGattung != -1) {
            sql = sql + " and m.gattung=?";
        }

        PreparedStatement pstm1 = null;
        try {
            pstm1 = verbindung.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            pstm1.setInt(1, dbBundle.clGlobalVar.mandant);
            pstm1.setInt(2, dbBundle.clGlobalVar.mandant);
            if (pGattung != -1) {
                pstm1.setInt(3, pGattung);
            }
        } catch (SQLException e) {
            CaBug.drucke("DbMeldungen.leseSammelkarteBriefwahlInternet 001");
            System.err.println(" " + e.getMessage());
        }

        return (read_PreparedStatement(pstm1));

    }

    /**************************Sammelkartennr für Orga internet ermitteln************************
     * Vorgehen: erste mit Erteilung über Internet auswählen, nach den Kriterien:
     * meldungstyp=2 (=> Sammelkarte)
     * skBuchbarInternet=1 (=> Im internet bebuchbar)
     * skIst=3 (=> Orga)
     * 
     * pGattung=-1 => alle, sonst nur die mit Gattung
     */
    public int leseSammelkarteOrgaInternet(int pGattung) {
        return leseSammelkarteOrgaIntern(pGattung, 2);
    }

    /**************************Sammelkartennr für Orga papier ermitteln************************
     * Vorgehen: erste mit Erteilung über Internet auswählen, nach den Kriterien:
     * meldungstyp=2 (=> Sammelkarte)
     * skBuchbarPapier=1 (=> Im Papier bebuchbar)
     * skIst=3 (=> Orga)
     * 
     * pGattung=-1 => alle, sonst nur die mit Gattung
     */
    public int leseSammelkarteOrgaPapier(int pGattung) {
        return leseSammelkarteOrgaIntern(pGattung, 1);
    }

    public int leseSammelkarteOrga(int pGattung) {
        return leseSammelkarteOrgaIntern(pGattung, 0);
    }

    /**art=0 => alle, =1 => Papier, =2 => Internet*/
    private int leseSammelkarteOrgaIntern(int pGattung, int art) {

        String sql = "";
        sql = "SELECT * from " + dbBundle.getSchemaMandant() + "tbl_meldungen m " + "INNER JOIN "
                + dbBundle.getSchemaMandant() + "tbl_personenNatJur p ON m.personenNatJurIdent=p.ident "
                + "where m.meldungAktiv=1 and m.meldungstyp=2 ";
        if (art == 1) {
            sql = sql + " and m.skBuchbarPapier=1 ";
        }
        if (art == 2) {
            sql = sql + " and m.skBuchbarInternet=1 ";
        }
        sql = sql + "and m.skIst=3 " + " and m.mandant=? and p.mandant=?";
        if (pGattung != -1) {
            sql = sql + " and m.gattung=?";
        }

        PreparedStatement pstm1 = null;
        try {
            pstm1 = verbindung.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            pstm1.setInt(1, dbBundle.clGlobalVar.mandant);
            pstm1.setInt(2, dbBundle.clGlobalVar.mandant);
            if (pGattung != -1) {
                pstm1.setInt(3, pGattung);
            }
        } catch (SQLException e) {
            CaBug.drucke("DbMeldungen.leseSammelkarteOrgaPapier 001");
            System.err.println(" " + e.getMessage());
        }

        return (read_PreparedStatement(pstm1));

    }

    /**************************Sammelkartennr für Dauervollmacht internet ermitteln************************
     * Vorgehen: erste mit Erteilung über Internet auswählen, nach den Kriterien:
     * meldungstyp=2 (=> Sammelkarte)
     * skBuchbarInternet=1 (=> Im internet bebuchbar)
     * skIst=5 (=> Dauervollmacht)
     * 
     * pGattung=-1 => alle, sonst nur die mit Gattung
     */
    public int leseSammelkarteDauervollmachtInternet(int pGattung) {
        return leseSammelkarteDauervollmacht(pGattung, 2);
    }

    /**************************Sammelkartennr für Dauervollmacht papier ermitteln************************
     * Vorgehen: erste mit Erteilung über Internet auswählen, nach den Kriterien:
     * meldungstyp=2 (=> Sammelkarte)
     * skBuchbarPapier=1 (=> Im Papier bebuchbar)
     * skIst=5 (=> Dauervollmacht)
     * 
     * pGattung=-1 => alle, sonst nur die mit Gattung
     */
    public int leseSammelkarteDauervollmachtPapier(int pGattung) {
        return leseSammelkarteDauervollmacht(pGattung, 1);
    }

    public int leseSammelkarteDauervollmacht(int pGattung) {
        return leseSammelkarteDauervollmacht(pGattung, 0);
    }

    /**art=0 => alle, =1 => Papier, =2 => Internet*/
    private int leseSammelkarteDauervollmacht(int pGattung, int art) {

        String sql = "";
        sql = "SELECT * from " + dbBundle.getSchemaMandant() + "tbl_meldungen m " + "INNER JOIN "
                + dbBundle.getSchemaMandant() + "tbl_personenNatJur p ON m.personenNatJurIdent=p.ident "
                + "where m.meldungAktiv=1 and m.meldungstyp=2 ";
        if (art == 1) {
            sql = sql + " and m.skBuchbarPapier=1 ";
        }
        if (art == 2) {
            sql = sql + " and m.skBuchbarInternet=1 ";
        }
        sql = sql + "and m.skIst=5 " + " and m.mandant=? and p.mandant=?";
        if (pGattung != -1) {
            sql = sql + " and m.gattung=?";
        }

        PreparedStatement pstm1 = null;
        try {
            pstm1 = verbindung.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            pstm1.setInt(1, dbBundle.clGlobalVar.mandant);
            pstm1.setInt(2, dbBundle.clGlobalVar.mandant);
            if (pGattung != -1) {
                pstm1.setInt(3, pGattung);
            }

        } catch (SQLException e) {
            CaBug.drucke("DbMeldungen.leseSammelkarteDauervollmachtPapier 001");
            System.err.println(" " + e.getMessage());
        }

        return (read_PreparedStatement(pstm1));

    }

    public int leseSammelkartenZuInsti(int pGattung, int instIdent) {

        String sql = "";
        sql = "SELECT * from " + dbBundle.getSchemaMandant() + "tbl_meldungen m " + "INNER JOIN "
                + dbBundle.getSchemaMandant() + "tbl_personenNatJur p ON m.personenNatJurIdent=p.ident "
                + "where m.meldungAktiv=1 and m.meldungstyp=2 and m.instiIdent=? ";
 
        PreparedStatement pstm1 = null;
        try {
            pstm1 = verbindung.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            pstm1.setInt(1, instIdent);

        } catch (SQLException e) {
            CaBug.drucke("001");
            System.err.println(" " + e.getMessage());
        }

        return (read_PreparedStatement(pstm1));

    }

    public int leseSammelkartenArtZuInsti(int pGattung, int instIdent, int pSkIst) {
        return leseSammelkartenArtZuInsti(pGattung, instIdent, pSkIst, false);
    }
    
    public int leseSammelkartenArtZuInsti(int pGattung, int instIdent, int pSkIst, boolean pMitInaktiven) {

        String sql = "";
        sql = "SELECT * from " + dbBundle.getSchemaMandant() + "tbl_meldungen m " + "INNER JOIN "
                + dbBundle.getSchemaMandant() + "tbl_personenNatJur p ON m.personenNatJurIdent=p.ident "
                + "where m.meldungstyp=2 and m.instiIdent=? AND m.skIst=? ";
 
        if (pMitInaktiven==false) {
            sql=sql+" and m.meldungAktiv=1";
        }

        if (pGattung!=0) {
            sql=sql+" and m.gattung="+Integer.toString(pGattung);
        }
        PreparedStatement pstm1 = null;
        try {
            pstm1 = verbindung.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            pstm1.setInt(1, instIdent);
            pstm1.setInt(2, pSkIst);

        } catch (SQLException e) {
            CaBug.drucke("001");
            System.err.println(" " + e.getMessage());
        }

        return (read_PreparedStatement(pstm1));

    }

    /**************************Alle aktiven Sammelkarten einlesen************************
     * pGattung=-1 => alle, sonst nur die mit Gattung
     */
    public int leseAlleAktivenSammelkarten(int pGattung) {
        return leseAlleSammelkartenMitAuswahl(pGattung, 1);
    }
    public int leseAlleInaktivenSammelkarten(int pGattung) {
        return leseAlleSammelkartenMitAuswahl(pGattung, -1);
    }

    /**Alle Sammelkarten einlesen (auch inaktive).
     * pGattung=-1 => alle, sonst nur die mit Gattung
     */
    public int leseWirklichAlleSammelkarten(int pGattung) {
        return leseAlleSammelkartenMitAuswahl(pGattung, 0);
    }

    /**pNurAktive =1 => nur Aktive
     *            =0 => alle
     *            =-1 => nur inaktive
     */
    public int leseAlleSammelkartenMitAuswahl(int pGattung, int pNurAktive) {

        String sql = "";
        sql = "SELECT * from " + dbBundle.getSchemaMandant() + "tbl_meldungen m " + "INNER JOIN "
                + dbBundle.getSchemaMandant()
                + "tbl_personenNatJur p ON m.personenNatJurIdent=p.ident where m.meldungstyp=2 and ";
        if (pNurAktive==1) {
            sql = sql + "m.meldungAktiv=1 and ";
        }
        if (pNurAktive==-1) {
            sql = sql + "m.meldungAktiv!=1 and ";
        }
        sql = sql + "m.mandant=? and p.mandant=?";
        if (pGattung != -1) {
            sql = sql + " and m.gattung=?";
        }

        PreparedStatement pstm1 = null;
        try {
            pstm1 = verbindung.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            pstm1.setInt(1, dbBundle.clGlobalVar.mandant);
            pstm1.setInt(2, dbBundle.clGlobalVar.mandant);
            if (pGattung != -1) {
                pstm1.setInt(3, pGattung);
            }
        } catch (SQLException e) {
            CaBug.drucke("DbMeldungen.leseAlleSammelkarten 001");
            System.err.println(" " + e.getMessage());
        }

        return (read_PreparedStatement(pstm1));

    }

    /**************************Alle Gastkarten einlesen************************
     */
    public int leseAlleGastkarten() {

        String sql = "";
        sql = "SELECT * from " + dbBundle.getSchemaMandant() + "tbl_meldungen m " + "INNER JOIN "
                + dbBundle.getSchemaMandant() + "tbl_personenNatJur p ON m.personenNatJurIdent=p.ident "
                + "where m.meldungAktiv=1 and m.klasse=0 " + " and m.mandant=? and p.mandant=?";

        PreparedStatement pstm1 = null;
        try {
            pstm1 = verbindung.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            pstm1.setInt(1, dbBundle.clGlobalVar.mandant);
            pstm1.setInt(2, dbBundle.clGlobalVar.mandant);
        } catch (SQLException e) {
            CaBug.drucke("DbMeldungen.leseAlleGastkarten 001");
            System.err.println(" " + e.getMessage());
        }

        return (read_PreparedStatement(pstm1));

    }

    
    /***********************Alle Aktionäre, die angemeldet sind aber keine EK ausgestellt haben (nur aktive)***********************
     */
    public int leseAlleAktionaersmeldungenOhneEK() {

        String sql = "";
        sql = "SELECT * from " + dbBundle.getSchemaMandant() + "tbl_meldungen m " + "INNER JOIN "
                + dbBundle.getSchemaMandant() + "tbl_personenNatJur p ON m.personenNatJurIdent=p.ident "
                + "where m.meldungAktiv=1 and m.klasse=1 AND (m.meldungstyp=1 or m.meldungstyp=3) and m.zutrittsIdent='' and m.mandant=? and p.mandant=?";

        PreparedStatement pstm1 = null;
        try {
            pstm1 = verbindung.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            pstm1.setInt(1, dbBundle.clGlobalVar.mandant);
            pstm1.setInt(2, dbBundle.clGlobalVar.mandant);
        } catch (SQLException e) {
            CaBug.drucke("001");
            System.err.println(" " + e.getMessage());
        }

        return (read_PreparedStatement(pstm1));

    }

    /**************************Alle  einlesen************************
     */
    public int leseAllePotentiellenPraesenzaenderungenAktionaere() {

        String sql = "";
        sql = "SELECT * from " + dbBundle.getSchemaMandant() + "tbl_meldungen m " + "INNER JOIN "
                + dbBundle.getSchemaMandant() + "tbl_personenNatJur p ON m.personenNatJurIdent=p.ident "
                + "where m.meldungAktiv=1 AND m.klasse=1 AND m.meldungstyp!=3 " + " and m.mandant=? and p.mandant=?";

        PreparedStatement pstm1 = null;
        try {
            pstm1 = verbindung.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            pstm1.setInt(1, dbBundle.clGlobalVar.mandant);
            pstm1.setInt(2, dbBundle.clGlobalVar.mandant);
        } catch (SQLException e) {
            CaBug.drucke("DbMeldungen.leseAllePotentiellenPraesenzaenderungenAktionaere 001");
            System.err.println(" " + e.getMessage());
        }

        return (read_PreparedStatement(pstm1));

    }

    /** ****************Einlesen eines Satzes über MeldungsIdent**************************
     * Unabhängig von Versionierung/Stornierung etc. - Satz wird immer geliefert!
     * */
    public int leseZuMeldungsIdent(EclMeldung pMeldung) {
        return leseZuMeldungsIdent(pMeldung.meldungsIdent);
    }

    public int leseZuMeldungsIdent(int pMeldungsIdent) {
        String sql = "";
        sql = "SELECT * from " + dbBundle.getSchemaMandant() + "tbl_meldungen m " + "INNER JOIN "
                + dbBundle.getSchemaMandant() + "tbl_personenNatJur p ON m.personenNatJurIdent=p.ident "
                + "where m.meldungsIdent=? " + " and m.mandant=? and p.mandant=?";

        PreparedStatement pstm1 = null;
        try {
            pstm1 = verbindung.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            pstm1.setInt(1, pMeldungsIdent);
            pstm1.setInt(2, dbBundle.clGlobalVar.mandant);
            pstm1.setInt(3, dbBundle.clGlobalVar.mandant);
        } catch (SQLException e) {
            CaBug.drucke("DbMeldungen.leseZuAktionaersnummer 001");
            System.err.println(" " + e.getMessage());
        }

        return (read_PreparedStatement(pstm1));
    }

    /** ****************Einlesen eines Satzes über MeldungsIdent**************************
     * Unabhängig von Versionierung/Stornierung etc. - Satz wird immer geliefert!
     * */
    public int leseZuAktienregisterIdent(int pAktienregisterIdent, boolean pNurAktive) {

        String sql = "";
        sql = "SELECT * from " + dbBundle.getSchemaMandant() + "tbl_meldungen m " + "INNER JOIN "
                + dbBundle.getSchemaMandant() + "tbl_personenNatJur p ON m.personenNatJurIdent=p.ident "
                + "where m.aktienregisterIdent=? ";
        if (pNurAktive) {
            sql = sql + " AND m.meldungAktiv=1 ";
        }

        PreparedStatement pstm1 = null;
        try {
            pstm1 = verbindung.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            pstm1.setInt(1, pAktienregisterIdent);
        } catch (SQLException e) {
            CaBug.drucke("001");
            System.err.println(" " + e.getMessage());
        }

        return (read_PreparedStatement(pstm1));
    }

    
    /** ****************Einlesen aller Sätze die zu prüfen sind (Vorläufige Vollmachten ...)**************************
     * Unabhängig von Versionierung/Stornierung etc. - Satz wird immer geliefert!
     * */
    public int leseAlleUngeprueften() {

        String sql = "";
        sql = "SELECT * from " + dbBundle.getSchemaMandant() + "tbl_meldungen m " + "INNER JOIN "
                + dbBundle.getSchemaMandant() + "tbl_personenNatJur p ON m.personenNatJurIdent=p.ident "
                + "where zusatzfeld1='-1' or zusatzfeld1='-2' ";

        PreparedStatement pstm1 = null;
        try {
            pstm1 = verbindung.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        } catch (SQLException e) {
            CaBug.drucke("001");
            System.err.println(" " + e.getMessage());
        }

        return (read_PreparedStatement(pstm1));
    }

    private String insertStringFelder() {
        return "meldungsIdent, db_version,  " + "mandant, meldungAktiv, klasse, "
                + "aktionaersnummer, aktienregisterIdent, externeIdent, "
                + "meldungstyp, skMitWeisungen, skIst, skWeisungsartZulaessig, "

                + "skBuchbarInternet, skBuchbarPapier, skBuchbarHV, skBuchbarVollmachtDritte, skBuchbarVollmachtDritteHV, skOffenlegung, instiIdent, "
                + "stimmkarteVorab, stimmkarteSecondVorab, " + "gattung, fixAnmeldung, stueckAktien, stimmen, "

                + "stueckAktienDruckEK, stimmenDruckEk, " + "besitzart, stimmausschluss, "
                + "zusatzrechte, personenNatJurIdent, "

                + "zusatzfeld1, zusatzfeld2, zusatzfeld3, zusatzfeld4, zusatzfeld5, " + "gruppe, "
                + "neuanlageDatumUhrzeit,  " + "geaendertDatumUhrzeit, " + "manuellGeaendertSeitLetztemImport, "
                + "delayedVorhanden, "

                + "pendingVorhanden, " + "zutrittsIdent, zutrittsIdent_Delayed, " + "stimmkarte, stimmkarte_Delayed, "
                + "stimmkarteSecond, stimmkarteSecond_Delayed, " + "willenserklaerung, willenserklaerung_Delayed,"
                + "willenserklaerungIdent, willenserklaerungIdent_Delayed,"
                + "veraenderungszeit, veraenderungszeit_Delayed," + "erteiltAufWeg, erteiltAufWeg_Delayed,"
                + "meldungEnthaltenInSammelkarte, meldungEnthaltenInSammelkarte_Delayed, "
                + "meldungEnthaltenInSammelkarteArt, "

                + "meldungEnthaltenInSammelkarteArt_Delayed, " + "weisungVorhanden, weisungVorhanden_Delayed, "

                + "vertreterName, vertreterName_Delayed, " + "vertreterVorname, vertreterVorname_Delayed, "
                + "vertreterOrt, vertreterOrt_Delayed, " + "vertreterIdent, vertreterIdent_Delayed, "
                + "willenserklaerungMitVollmacht, willenserklaerungMitVollmacht_Delayed, " + "statusPraesenz, "

                + "statusPraesenz_Delayed, "
                + "statusWarPraesenz, statusWarPraesenz_Delayed, virtuellerTeilnehmerIdent, "
                + "teilnahmeArt, vorlAnmeldung, vorlAnmeldungAkt, vorlAnmeldungSer ";
    }

    private String insertStringPlatzhalter() {
        return "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?," //20
                + "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?," + "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,"
                + "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?, " //20
                + "? "; //1
    }

    private EclPersonenNatJur copyPersonNatJurFromMeldung(EclMeldung pMeldung) {
        EclPersonenNatJur lPersonenNatJur = new EclPersonenNatJur();

        lPersonenNatJur.mandant = pMeldung.mandant;
        lPersonenNatJur.istSelbePersonWieIdent = pMeldung.istSelbePersonWieIdent;
        lPersonenNatJur.uebereinstimmungSelbePersonWurdeUeberprueft = pMeldung.uebereinstimmungSelbePersonWurdeUeberprueft;
        lPersonenNatJur.gehoertZuMeldung = 1;
        /*TODOLang: 
         * > istNatuerlichePerson setzen
         * > Stimmausschluß handhaben?
         * */
        lPersonenNatJur.anrede = pMeldung.anrede;
        lPersonenNatJur.titel = pMeldung.titel;
        lPersonenNatJur.adelstitel = pMeldung.adelstitel;
        lPersonenNatJur.name = pMeldung.name;
        lPersonenNatJur.vorname = pMeldung.vorname;
        lPersonenNatJur.kurzName = lPersonenNatJur.name;
        if (!lPersonenNatJur.vorname.isEmpty()) {
            lPersonenNatJur.kurzName = lPersonenNatJur.kurzName + " " + lPersonenNatJur.vorname;
        }
        lPersonenNatJur.zuHdCo = pMeldung.zuHdCo;
        lPersonenNatJur.zusatz1 = pMeldung.zusatz1;
        lPersonenNatJur.zusatz2 = pMeldung.zusatz2;
        lPersonenNatJur.strasse = pMeldung.strasse;
        lPersonenNatJur.land = pMeldung.land;
        lPersonenNatJur.plz = pMeldung.plz;
        lPersonenNatJur.ort = pMeldung.ort;
        lPersonenNatJur.identVersandadresse = pMeldung.identVersandadresse;
        lPersonenNatJur.kurzOrt = lPersonenNatJur.ort;
        lPersonenNatJur.mailadresse = pMeldung.mailadresse;
        lPersonenNatJur.kommunikationssprache = pMeldung.kommunikationssprache;
        lPersonenNatJur.loginKennung = pMeldung.loginKennung;
        lPersonenNatJur.loginPasswort = pMeldung.loginPasswort;
        lPersonenNatJur.oeffentlicheID = pMeldung.oeffentlicheID;

        return lPersonenNatJur;
    }

    /** ****************************************************************************
     * Einfügen eines neuen Satzes 
     * 
     * Dabei: 
     * > ggf. automatische Nummernvergabe von meldungsIdent (wenn meldungsIdent=0)
     * 		Wenn <>0, dann muß auch nummernkreis entsprechend vorher geupdated worden sein!
     *
     * > PersonenNatJur wird automatisch angelegt, außer wenn personenNatJurIdent bereits mit
     * 	einer Zahl vorbelegt - dann wird nur der Verweis auf diesen bestehenden PersonenNatJur-Satz
     * 	gespeichert. Der Aufrufer muß sicherstellen, dass dieser Satz auch existiert und "paßt"
      * 
     * > mandant wird mit dieser Funktion automatisch vorbelegt.
     */
    public int insert(EclMeldung pMeldung) {

        int neuePersonenNatJur = 0;
        int erg = 0;
        EclPersonenNatJur lPersonenNatJur = null;

        /* Start Transaktion */
        dbBasis.beginTransaction();

        if (pMeldung.meldungsIdent == 0) {
            /* neue InterneIdent vergeben */
            erg = dbBasis.getInterneIdentMeldungen();
            if (erg < 1) {
                CaBug.drucke("DbMeldungen.insert 001");
                dbBasis.endTransaction();
                return (erg);
            }
            pMeldung.meldungsIdent = erg;
        }

        /*Falls personenNatJurIdent==0, dann muß eine neue angelegt werden. In diesem Fall hier
         * ident ermitteln, das diese in tbl_meldungen mit gespeichert werden muß*/

        if (pMeldung.personenNatJurIdent == 0) {
            lPersonenNatJur = copyPersonNatJurFromMeldung(pMeldung);

            erg = dbBundle.dbPersonenNatJur.insert(lPersonenNatJur);
            if (erg < 1) {
                CaBug.drucke("DbMeldungen.insert 004");
                dbBasis.endTransaction();
                return (erg);
            }
            pMeldung.personenNatJurIdent = lPersonenNatJur.ident;
            neuePersonenNatJur = 1;
        }

        /* VclMeldung einfügen */
        /* Verarbeitungshinweise: 
         * 	>	nachdem InterneIdent immer eindeutig vergeben werden, ist prinzipiell eine "Doppeleinfügung"
         * 		von InterneIdent nicht möglich
         */

        try {

            pMeldung.neuanlageDatumUhrzeit = CaDatumZeit.DatumZeitStringFuerDatenbank();
            pMeldung.db_version = 0;

            /*Felder Neuanlage füllen für tbl_meldungen*/
            //				System.out.println("A");
            String sql1 = "INSERT INTO " + dbBundle.getSchemaMandant() + "tbl_meldungen " + "(" + insertStringFelder()
                    + ")" +

                    "VALUES (" + insertStringPlatzhalter() + ")";
            PreparedStatement pstm1 = verbindung.prepareStatement(sql1);

            fuellePreparedStatementKomplett(pstm1, 1, pMeldung);

            erg = 0; /*Falls Duplicate Key, dann wird exception geschmissen und erg wohl nicht gefüllt!*/
            erg = pstm1.executeUpdate();
            pstm1.close();

        } catch (Exception e2) {
            CaBug.drucke("DbMeldungen.insert 002");
            System.err.println(" " + e2.getMessage());
            dbBasis.endTransaction();
            return (erg);
        }

        if (erg == 0) {/*Fehler beim Einfügen*/
            CaBug.drucke("DbMeldungen.insert 003");
            dbBasis.endTransaction();
            return CaFehler.pfdXyBereitsVorhanden;
        }

        //			/* Änderungsprotokoll-Table füllen*/
        //			EclAenderungslog aenderungsLog=new EclAenderungslog();
        //			aenderungsLog.tabelle=EnAenderungslogTabelle.toEntity(EnAenderungslogTabelle.meldungen);
        //			aenderungsLog.ident=Integer.toString(pMeldung.meldungsIdent);
        //			aenderungsLog.aktion=EnAenderungslogAktion.toEntity(EnAenderungslogAktion.neuaufnahme);
        //			aenderungsLog.feld="";
        //			aenderungsLog.alt="";
        //			aenderungsLog.neu="";
        //			lDbBundle.dbAenderungslog.insert(aenderungsLog);

        /* Ende Transaktion */
        dbBasis.endTransaction();

        return (1);
    }

    private int protokollIdent = 0;

    /**Fügt die Meldung in das Update-Protokoll ein.
     * pAenderungsart=1 => alter Satz; protokollIdent wird in diesem Fall vergeben und zwischengespeichert
     * pAenderungsart=2 => neuer Satz; zwischengespeicherte protokollIdent wird verwendet
     */
    private int insertProtokoll(int pAenderungsart, EclMeldung pMeldung, String pUpdateZeit, int pUserNummer,
            int pArbeitsplatzNummer) {

        int erg = 0;
        EclPersonenNatJur lPersonenNatJur = null;

        /* Start Transaktion */
        dbBasis.beginTransaction();

        if (pAenderungsart == 1) {
            /* neue InterneIdent vergeben */
            erg = dbBasis.getInterneIdentMeldungenProtokoll();
            if (erg < 1) {
                CaBug.drucke("DbMeldungen.insertProtokoll 001");
                dbBasis.endTransaction();
                return (erg);
            }
            protokollIdent = erg;
        }

        /*PersonNatJur in Protokoll speichern*/
        lPersonenNatJur = copyPersonNatJurFromMeldung(pMeldung);

        erg = dbBundle.dbPersonenNatJur.insertProtokoll(protokollIdent, pAenderungsart, lPersonenNatJur, pUpdateZeit,
                pUserNummer, pArbeitsplatzNummer);
        if (erg < 1) {
            CaBug.drucke("DbMeldungen.insertProtokoll 004");
            dbBasis.endTransaction();
            return (erg);
        }

        /*Meldung einfügen*/
        try {

            pMeldung.neuanlageDatumUhrzeit = CaDatumZeit.DatumZeitStringFuerDatenbank();
            pMeldung.db_version = 0;

            /*Felder Neuanlage füllen für tbl_meldungen*/
            //				System.out.println("A");
            String sql1 = "INSERT INTO " + dbBundle.getSchemaMandant() + "tbl_meldungenProtokoll " + "("
                    + "satzArt, protokollIdent, updateZeit, userNummer, arbeitsplatzNummer, " + insertStringFelder()
                    + ")" +

                    "VALUES (" + "?,?,?,?,?," + insertStringPlatzhalter() + ")";
            PreparedStatement pstm1 = verbindung.prepareStatement(sql1);

            pstm1.setInt(1, pAenderungsart);
            pstm1.setInt(2, protokollIdent);
            pstm1.setString(3, pUpdateZeit);
            pstm1.setInt(4, pUserNummer);
            pstm1.setInt(5, pArbeitsplatzNummer);

            fuellePreparedStatementKomplett(pstm1, 6, pMeldung);

            erg = 0; /*Falls Duplicate Key, dann wird exception geschmissen und erg wohl nicht gefüllt!*/
            erg = pstm1.executeUpdate();
            pstm1.close();

        } catch (Exception e2) {
            CaBug.drucke("DbMeldungen.insertProtokoll 002");
            System.err.println(" " + e2.getMessage());
            dbBasis.endTransaction();
            return (erg);
        }

        if (erg == 0) {/*Fehler beim Einfügen*/
            CaBug.drucke("DbMeldungen.insertProtokoll 003");
            dbBasis.endTransaction();
            return CaFehler.pfdXyBereitsVorhanden;
        }

        /* Ende Transaktion */
        dbBasis.endTransaction();

        return (1);

    }

    /* ************************Update eines Satzes *******************************************
     * Vorbedingung: muß über eine read-Funktion eingelesen sein, damit VclMeldungOldVersion
     * gefüllt ist
     ******************************************************************************************/
    public int update(EclMeldung pMeldung) {
        return update(pMeldung, true);
    }

    public int update(EclMeldung pMeldung, boolean pMitPersonNatJur) {

        try {
            pMeldung.geaendertDatumUhrzeit = CaDatumZeit.DatumZeitStringFuerDatenbank();
            pMeldung.db_version++;

            //				Statement stm=verbindung.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            String sql = "UPDATE " + dbBundle.getSchemaMandant() + "tbl_meldungen " + "SET "
                    + "meldungsIdent=?, db_version=?, " + "mandant=?, meldungAktiv=?, klasse=?, "
                    + "aktionaersnummer=?, aktienregisterIdent=?, externeIdent=?, meldungstyp=?, skMitWeisungen=?, skIst=?, skWeisungsartZulaessig=?, "
                    + "skBuchbarInternet=?, skBuchbarPapier=?, skBuchbarHV=?, skBuchbarVollmachtDritte=?, skBuchbarVollmachtDritteHV=?, skOffenlegung=?, instiIdent=?, "
                    + "stimmkarteVorab=?, stimmkarteSecondVorab=?, "
                    + "gattung=?, fixAnmeldung=?, stueckAktien=?, stimmen=?, " +

                    "stueckAktienDruckEk=?, stimmenDruckEK=?, " + "besitzart=?, stimmausschluss=?, "
                    + "zusatzrechte=?, " +

                    "personenNatJurIdent=?, " +

                    "zusatzfeld1=?, zusatzfeld2=?, zusatzfeld3=?, zusatzfeld4=?, zusatzfeld5=?, " + "gruppe=?, "
                    + "neuanlageDatumUhrzeit=?, " + "geaendertDatumUhrzeit=?, "
                    + "manuellGeaendertSeitLetztemImport=?, " + "delayedVorhanden=?, " +

                    "pendingVorhanden=?, " + "zutrittsIdent=?, zutrittsIdent_Delayed=?, "
                    + "stimmkarte=?, stimmkarte_Delayed=?, " + "stimmkarteSecond=?, stimmkarteSecond_Delayed=?, "
                    + "willenserklaerung=?, willenserklaerung_Delayed=?, "
                    + "willenserklaerungIdent=?, willenserklaerungIdent_Delayed=?, "
                    + "veraenderungszeit=?, veraenderungszeit_Delayed=?, "
                    + "erteiltAufWeg=?, erteiltAufWeg_Delayed=?, "
                    + "meldungEnthaltenInSammelkarte=?, meldungEnthaltenInSammelkarte_Delayed=?, "
                    + "meldungEnthaltenInSammelkarteArt=?, " +

                    "meldungEnthaltenInSammelkarteArt_Delayed=?, " + "weisungVorhanden=?, weisungVorhanden_Delayed=?, "
                    + "vertreterName=?, vertreterName_Delayed=?, " + "vertreterVorname=?, vertreterVorname_Delayed=?, "
                    + "vertreterOrt=?, vertreterOrt_Delayed=?, "
                    + "vertreterIdent=?, vertreterIdent_Delayed=?, willenserklaerungMitVollmacht=?, willenserklaerungMitVollmacht_Delayed=?, "
                    + "statusPraesenz=?, " +

                    "statusPraesenz_Delayed=?, "
                    + "statusWarPraesenz=?, statusWarPraesenz_Delayed=?, virtuellerTeilnehmerIdent=?, "
                    + "teilnahmeArt=?, vorlAnmeldung=?, vorlAnmeldungAkt=?, vorlAnmeldungSer=? "
                    + this.liefereWhereString();
            // VMclMeldung.geaendertDatumUhrzeit
            PreparedStatement pstm1 = verbindung.prepareStatement(sql);
            fuellePreparedStatementKomplett(pstm1, 1, pMeldung); /*SET-Teil*/
            fuellePreparedStatementWhere(pstm1, -2, pMeldung); /*Where-Teil*/

            int ergebnis1 = pstm1.executeUpdate();
            pstm1.close();
            if (ergebnis1 == 0) {
                return (CaFehler.pfXyWurdeVonAnderemBenutzerVeraendert);
            }
        } catch (Exception e1) {
            CaBug.drucke("DbMeldungen.update 001");
            System.err.println(" " + e1.getMessage());

        }

        if (pMitPersonNatJur) {
            /*Nun noch PersonenNatJur updaten*/
            EclPersonenNatJur lPersonenNatJur = new EclPersonenNatJur();
            lPersonenNatJur.mandant = pMeldung.mandant;
            lPersonenNatJur.db_version = pMeldung.db_version_personenNatJur;
            lPersonenNatJur.gehoertZuMeldung = 1;
            lPersonenNatJur.ident = pMeldung.personenNatJurIdent;
            lPersonenNatJur.istSelbePersonWieIdent = pMeldung.istSelbePersonWieIdent;
            lPersonenNatJur.uebereinstimmungSelbePersonWurdeUeberprueft = pMeldung.uebereinstimmungSelbePersonWurdeUeberprueft;
            /*TODOLang: 
             * > istNatuerlichePerson setzen
             * > Stimmausschluß handhaben?
             * */
            lPersonenNatJur.anrede = pMeldung.anrede;
            lPersonenNatJur.titel = pMeldung.titel;
            lPersonenNatJur.adelstitel = pMeldung.adelstitel;
            lPersonenNatJur.name = pMeldung.name;
            lPersonenNatJur.vorname = pMeldung.vorname;
            lPersonenNatJur.kurzName = lPersonenNatJur.name;
            if (lPersonenNatJur.vorname != null && !lPersonenNatJur.vorname.isEmpty()) {
                lPersonenNatJur.kurzName = lPersonenNatJur.kurzName + " " + lPersonenNatJur.vorname;
            }
            lPersonenNatJur.zuHdCo = pMeldung.zuHdCo;
            lPersonenNatJur.zusatz1 = pMeldung.zusatz1;
            lPersonenNatJur.zusatz2 = pMeldung.zusatz2;
            lPersonenNatJur.strasse = pMeldung.strasse;
            lPersonenNatJur.land = pMeldung.land;
            lPersonenNatJur.plz = pMeldung.plz;
            lPersonenNatJur.ort = pMeldung.ort;
            lPersonenNatJur.identVersandadresse = pMeldung.identVersandadresse;
            lPersonenNatJur.kurzOrt = lPersonenNatJur.ort;
            lPersonenNatJur.mailadresse = pMeldung.mailadresse;
            lPersonenNatJur.kommunikationssprache = pMeldung.kommunikationssprache;
            lPersonenNatJur.loginKennung = pMeldung.loginKennung;
            lPersonenNatJur.loginPasswort = pMeldung.loginPasswort;
            lPersonenNatJur.oeffentlicheID = pMeldung.oeffentlicheID;

            int erg = dbBundle.dbPersonenNatJur.update(lPersonenNatJur);
            if (erg < 1) {
                CaBug.drucke("DbMeldungen.update 002 erg=" + erg);
                return (erg);
            }
            pMeldung.db_version_personenNatJur = lPersonenNatJur.db_version; /*Rückübertragung! Sonst würde zweiter Update schief gehen*/
        }

        /*Änderungslog erst mal deaktiviert*/
        //			/*Aenderungslog schreiben*/
        //			if (pMeldung.meldungAktiv==2 && VclMeldungOldVersion.meldungAktiv==1){ /*Meldung wurde storniert*/
        //				EclAenderungslog aenderungsLog=new EclAenderungslog();
        //				aenderungsLog.tabelle=EnAenderungslogTabelle.toEntity(EnAenderungslogTabelle.meldungen);
        //				aenderungsLog.ident=Integer.toString(pMeldung.meldungsIdent);
        //				aenderungsLog.aktion=EnAenderungslogAktion.toEntity(EnAenderungslogAktion.meldungStorniert);
        //				aenderungsLog.feld="";
        //				aenderungsLog.alt="";
        //				aenderungsLog.neu="";
        //				lDbBundle.dbAenderungslog.insert(aenderungsLog);
        //			}
        //			if (!pMeldung.aktionaersnummer.equals(VclMeldungOldVersion.aktionaersnummer)){
        //				EclAenderungslog aenderungsLog=new EclAenderungslog();
        //				aenderungsLog.tabelle=EnAenderungslogTabelle.toEntity(EnAenderungslogTabelle.meldungen);
        //				aenderungsLog.ident=Integer.toString(pMeldung.meldungsIdent);
        //				aenderungsLog.aktion=EnAenderungslogAktion.toEntity(EnAenderungslogAktion.aenderung);
        //				aenderungsLog.feld="Aktionärsnummer";
        //				aenderungsLog.alt=VclMeldungOldVersion.aktionaersnummer;
        //				aenderungsLog.neu=pMeldung.aktionaersnummer;
        //				lDbBundle.dbAenderungslog.insert(aenderungsLog);
        //			}
        //			if (!pMeldung.externeIdent.equals(VclMeldungOldVersion.externeIdent)){
        //				EclAenderungslog aenderungsLog=new EclAenderungslog();
        //				aenderungsLog.tabelle=EnAenderungslogTabelle.toEntity(EnAenderungslogTabelle.meldungen);
        //				aenderungsLog.ident=Integer.toString(pMeldung.meldungsIdent);
        //				aenderungsLog.aktion=EnAenderungslogAktion.toEntity(EnAenderungslogAktion.aenderung);
        //				aenderungsLog.feld="Externe ID";
        //				aenderungsLog.alt=VclMeldungOldVersion.externeIdent;
        //				aenderungsLog.neu=pMeldung.externeIdent;
        //				lDbBundle.dbAenderungslog.insert(aenderungsLog);
        //			}
        //			if (pMeldung.skMitWeisungen!=VclMeldungOldVersion.skMitWeisungen){
        //				EclAenderungslog aenderungsLog=new EclAenderungslog();
        //				aenderungsLog.tabelle=EnAenderungslogTabelle.toEntity(EnAenderungslogTabelle.meldungen);
        //				aenderungsLog.ident=Integer.toString(pMeldung.meldungsIdent);
        //				aenderungsLog.aktion=EnAenderungslogAktion.toEntity(EnAenderungslogAktion.aenderung);
        //				aenderungsLog.feld="skMitWeisungen";
        //				aenderungsLog.alt=Integer.toString(VclMeldungOldVersion.skMitWeisungen);
        //				aenderungsLog.neu=Integer.toString(pMeldung.skMitWeisungen);
        //				lDbBundle.dbAenderungslog.insert(aenderungsLog);
        //			}
        //			if (pMeldung.skIst!=VclMeldungOldVersion.skIst){
        //				EclAenderungslog aenderungsLog=new EclAenderungslog();
        //				aenderungsLog.tabelle=EnAenderungslogTabelle.toEntity(EnAenderungslogTabelle.meldungen);
        //				aenderungsLog.ident=Integer.toString(pMeldung.meldungsIdent);
        //				aenderungsLog.aktion=EnAenderungslogAktion.toEntity(EnAenderungslogAktion.aenderung);
        //				aenderungsLog.feld="skIst";
        //				aenderungsLog.alt=Integer.toString(VclMeldungOldVersion.skIst);
        //				aenderungsLog.neu=Integer.toString(pMeldung.skIst);
        //				lDbBundle.dbAenderungslog.insert(aenderungsLog);
        //			}
        //			if (pMeldung.skWeisungsartZulaessig!=VclMeldungOldVersion.skWeisungsartZulaessig){
        //				EclAenderungslog aenderungsLog=new EclAenderungslog();
        //				aenderungsLog.tabelle=EnAenderungslogTabelle.toEntity(EnAenderungslogTabelle.meldungen);
        //				aenderungsLog.ident=Integer.toString(pMeldung.meldungsIdent);
        //				aenderungsLog.aktion=EnAenderungslogAktion.toEntity(EnAenderungslogAktion.aenderung);
        //				aenderungsLog.feld="skWeisungsartZulaessig";
        //				aenderungsLog.alt=Integer.toString(VclMeldungOldVersion.skWeisungsartZulaessig);
        //				aenderungsLog.neu=Integer.toString(pMeldung.skWeisungsartZulaessig);
        //				lDbBundle.dbAenderungslog.insert(aenderungsLog);
        //			}
        //			if (pMeldung.skBuchbarInternet!=VclMeldungOldVersion.skBuchbarInternet){
        //				EclAenderungslog aenderungsLog=new EclAenderungslog();
        //				aenderungsLog.tabelle=EnAenderungslogTabelle.toEntity(EnAenderungslogTabelle.meldungen);
        //				aenderungsLog.ident=Integer.toString(pMeldung.meldungsIdent);
        //				aenderungsLog.aktion=EnAenderungslogAktion.toEntity(EnAenderungslogAktion.aenderung);
        //				aenderungsLog.feld="skBuchbarInternet";
        //				aenderungsLog.alt=Integer.toString(VclMeldungOldVersion.skBuchbarInternet);
        //				aenderungsLog.neu=Integer.toString(pMeldung.skBuchbarInternet);
        //				lDbBundle.dbAenderungslog.insert(aenderungsLog);
        //			}
        //			if (pMeldung.skBuchbarPapier!=VclMeldungOldVersion.skBuchbarPapier){
        //				EclAenderungslog aenderungsLog=new EclAenderungslog();
        //				aenderungsLog.tabelle=EnAenderungslogTabelle.toEntity(EnAenderungslogTabelle.meldungen);
        //				aenderungsLog.ident=Integer.toString(pMeldung.meldungsIdent);
        //				aenderungsLog.aktion=EnAenderungslogAktion.toEntity(EnAenderungslogAktion.aenderung);
        //				aenderungsLog.feld="skBuchbarPapier";
        //				aenderungsLog.alt=Integer.toString(VclMeldungOldVersion.skBuchbarPapier);
        //				aenderungsLog.neu=Integer.toString(pMeldung.skBuchbarPapier);
        //				lDbBundle.dbAenderungslog.insert(aenderungsLog);
        //			}
        //			if (pMeldung.skBuchbarHV!=VclMeldungOldVersion.skBuchbarHV){
        //				EclAenderungslog aenderungsLog=new EclAenderungslog();
        //				aenderungsLog.tabelle=EnAenderungslogTabelle.toEntity(EnAenderungslogTabelle.meldungen);
        //				aenderungsLog.ident=Integer.toString(pMeldung.meldungsIdent);
        //				aenderungsLog.aktion=EnAenderungslogAktion.toEntity(EnAenderungslogAktion.aenderung);
        //				aenderungsLog.feld="skBuchbarHV";
        //				aenderungsLog.alt=Integer.toString(VclMeldungOldVersion.skBuchbarHV);
        //				aenderungsLog.neu=Integer.toString(pMeldung.skBuchbarHV);
        //				lDbBundle.dbAenderungslog.insert(aenderungsLog);
        //			}
        //			if (pMeldung.gattung!=VclMeldungOldVersion.gattung){
        //				EclAenderungslog aenderungsLog=new EclAenderungslog();
        //				aenderungsLog.tabelle=EnAenderungslogTabelle.toEntity(EnAenderungslogTabelle.meldungen);
        //				aenderungsLog.ident=Integer.toString(pMeldung.meldungsIdent);
        //				aenderungsLog.aktion=EnAenderungslogAktion.toEntity(EnAenderungslogAktion.aenderung);
        //				aenderungsLog.feld="gattung";
        //				aenderungsLog.alt=Integer.toString(VclMeldungOldVersion.gattung);
        //				aenderungsLog.neu=Integer.toString(pMeldung.gattung);
        //				lDbBundle.dbAenderungslog.insert(aenderungsLog);
        //			}
        //			if (pMeldung.stueckAktien!=VclMeldungOldVersion.stueckAktien){
        //				EclAenderungslog aenderungsLog=new EclAenderungslog();
        //				aenderungsLog.tabelle=EnAenderungslogTabelle.toEntity(EnAenderungslogTabelle.meldungen);
        //				aenderungsLog.ident=Integer.toString(pMeldung.meldungsIdent);
        //				aenderungsLog.aktion=EnAenderungslogAktion.toEntity(EnAenderungslogAktion.aenderung);
        //				aenderungsLog.feld="stueckAktien";
        //				aenderungsLog.alt=Long.toString(VclMeldungOldVersion.stueckAktien);
        //				aenderungsLog.neu=Long.toString(pMeldung.stueckAktien);
        //				lDbBundle.dbAenderungslog.insert(aenderungsLog);
        //			}
        //			if (pMeldung.stimmen!=VclMeldungOldVersion.stimmen){
        //				EclAenderungslog aenderungsLog=new EclAenderungslog();
        //				aenderungsLog.tabelle=EnAenderungslogTabelle.toEntity(EnAenderungslogTabelle.meldungen);
        //				aenderungsLog.ident=Integer.toString(pMeldung.meldungsIdent);
        //				aenderungsLog.aktion=EnAenderungslogAktion.toEntity(EnAenderungslogAktion.aenderung);
        //				aenderungsLog.feld="stimmen";
        //				aenderungsLog.alt=Long.toString(VclMeldungOldVersion.stimmen);
        //				aenderungsLog.neu=Long.toString(pMeldung.stimmen);
        //				lDbBundle.dbAenderungslog.insert(aenderungsLog);
        //			}
        //			if (pMeldung.stueckAktienDruckEK!=VclMeldungOldVersion.stueckAktienDruckEK){
        //				EclAenderungslog aenderungsLog=new EclAenderungslog();
        //				aenderungsLog.tabelle=EnAenderungslogTabelle.toEntity(EnAenderungslogTabelle.meldungen);
        //				aenderungsLog.ident=Integer.toString(pMeldung.meldungsIdent);
        //				aenderungsLog.aktion=EnAenderungslogAktion.toEntity(EnAenderungslogAktion.aenderung);
        //				aenderungsLog.feld="stueckAktienDruckEK";
        //				aenderungsLog.alt=Long.toString(VclMeldungOldVersion.stueckAktienDruckEK);
        //				aenderungsLog.neu=Long.toString(pMeldung.stueckAktienDruckEK);
        //				lDbBundle.dbAenderungslog.insert(aenderungsLog);
        //			}
        //			if (pMeldung.stimmenDruckEK!=VclMeldungOldVersion.stimmenDruckEK){
        //				EclAenderungslog aenderungsLog=new EclAenderungslog();
        //				aenderungsLog.tabelle=EnAenderungslogTabelle.toEntity(EnAenderungslogTabelle.meldungen);
        //				aenderungsLog.ident=Integer.toString(pMeldung.meldungsIdent);
        //				aenderungsLog.aktion=EnAenderungslogAktion.toEntity(EnAenderungslogAktion.aenderung);
        //				aenderungsLog.feld="stimmenDruckEK";
        //				aenderungsLog.alt=Long.toString(VclMeldungOldVersion.stimmenDruckEK);
        //				aenderungsLog.neu=Long.toString(pMeldung.stimmenDruckEK);
        //				lDbBundle.dbAenderungslog.insert(aenderungsLog);
        //			}
        //			if (!pMeldung.besitzart.equals(VclMeldungOldVersion.besitzart)){
        //				EclAenderungslog aenderungsLog=new EclAenderungslog();
        //				aenderungsLog.tabelle=EnAenderungslogTabelle.toEntity(EnAenderungslogTabelle.meldungen);
        //				aenderungsLog.ident=Integer.toString(pMeldung.meldungsIdent);
        //				aenderungsLog.aktion=EnAenderungslogAktion.toEntity(EnAenderungslogAktion.aenderung);
        //				aenderungsLog.feld="besitzart";
        //				aenderungsLog.alt=VclMeldungOldVersion.besitzart;
        //				aenderungsLog.neu=pMeldung.besitzart;
        //				lDbBundle.dbAenderungslog.insert(aenderungsLog);
        //			}
        //			if (!pMeldung.stimmausschluss.equals(VclMeldungOldVersion.stimmausschluss)){
        //				EclAenderungslog aenderungsLog=new EclAenderungslog();
        //				aenderungsLog.tabelle=EnAenderungslogTabelle.toEntity(EnAenderungslogTabelle.meldungen);
        //				aenderungsLog.ident=Integer.toString(pMeldung.meldungsIdent);
        //				aenderungsLog.aktion=EnAenderungslogAktion.toEntity(EnAenderungslogAktion.aenderung);
        //				aenderungsLog.feld="stimmausschluss";
        //				aenderungsLog.alt=VclMeldungOldVersion.stimmausschluss;
        //				aenderungsLog.neu=pMeldung.stimmausschluss;
        //				lDbBundle.dbAenderungslog.insert(aenderungsLog);
        //			}
        //			if (pMeldung.teilnahmeArt!=VclMeldungOldVersion.teilnahmeArt){
        //				EclAenderungslog aenderungsLog=new EclAenderungslog();
        //				aenderungsLog.tabelle=EnAenderungslogTabelle.toEntity(EnAenderungslogTabelle.meldungen);
        //				aenderungsLog.ident=Integer.toString(pMeldung.meldungsIdent);
        //				aenderungsLog.aktion=EnAenderungslogAktion.toEntity(EnAenderungslogAktion.aenderung);
        //				aenderungsLog.feld="teilnahmeArt";
        //				aenderungsLog.alt=Integer.toString(VclMeldungOldVersion.teilnahmeArt);
        //				aenderungsLog.neu=Integer.toString(pMeldung.teilnahmeArt);
        //				lDbBundle.dbAenderungslog.insert(aenderungsLog);
        //			}
        //			if (pMeldung.zusatzrechte!=VclMeldungOldVersion.zusatzrechte){
        //				EclAenderungslog aenderungsLog=new EclAenderungslog();
        //				aenderungsLog.tabelle=EnAenderungslogTabelle.toEntity(EnAenderungslogTabelle.meldungen);
        //				aenderungsLog.ident=Integer.toString(pMeldung.meldungsIdent);
        //				aenderungsLog.aktion=EnAenderungslogAktion.toEntity(EnAenderungslogAktion.aenderung);
        //				aenderungsLog.feld="Zusatzrechte";
        //				aenderungsLog.alt=Integer.toString(VclMeldungOldVersion.zusatzrechte);
        //				aenderungsLog.neu=Integer.toString(pMeldung.zusatzrechte);
        //				lDbBundle.dbAenderungslog.insert(aenderungsLog);
        //			}
        //
        //
        //
        //			if (pMeldung.anrede!=VclMeldungOldVersion.anrede){
        //				EclAenderungslog aenderungsLog=new EclAenderungslog();
        //				aenderungsLog.tabelle=EnAenderungslogTabelle.toEntity(EnAenderungslogTabelle.meldungen);
        //				aenderungsLog.ident=Integer.toString(pMeldung.meldungsIdent);
        //				aenderungsLog.aktion=EnAenderungslogAktion.toEntity(EnAenderungslogAktion.aenderung);
        //				aenderungsLog.feld="Anrede";
        //				aenderungsLog.alt=Integer.toString(VclMeldungOldVersion.anrede);
        //				aenderungsLog.neu=Integer.toString(pMeldung.anrede);
        //				lDbBundle.dbAenderungslog.insert(aenderungsLog);
        //			}
        //			if (!pMeldung.titel.equals(VclMeldungOldVersion.titel)){
        //				EclAenderungslog aenderungsLog=new EclAenderungslog();
        //				aenderungsLog.tabelle=EnAenderungslogTabelle.toEntity(EnAenderungslogTabelle.meldungen);
        //				aenderungsLog.ident=Integer.toString(pMeldung.meldungsIdent);
        //				aenderungsLog.aktion=EnAenderungslogAktion.toEntity(EnAenderungslogAktion.aenderung);
        //				aenderungsLog.feld="Titel";
        //				aenderungsLog.alt=VclMeldungOldVersion.titel;
        //				aenderungsLog.neu=pMeldung.titel;
        //				lDbBundle.dbAenderungslog.insert(aenderungsLog);
        //			}
        //			if (!pMeldung.adelstitel.equals(VclMeldungOldVersion.adelstitel)){
        //				EclAenderungslog aenderungsLog=new EclAenderungslog();
        //				aenderungsLog.tabelle=EnAenderungslogTabelle.toEntity(EnAenderungslogTabelle.meldungen);
        //				aenderungsLog.ident=Integer.toString(pMeldung.meldungsIdent);
        //				aenderungsLog.aktion=EnAenderungslogAktion.toEntity(EnAenderungslogAktion.aenderung);
        //				aenderungsLog.feld="Adelstitel";
        //				aenderungsLog.alt=VclMeldungOldVersion.adelstitel;
        //				aenderungsLog.neu=pMeldung.adelstitel;
        //				lDbBundle.dbAenderungslog.insert(aenderungsLog);
        //			}
        //			if (!pMeldung.name.equals(VclMeldungOldVersion.name)){
        //				EclAenderungslog aenderungsLog=new EclAenderungslog();
        //				aenderungsLog.tabelle=EnAenderungslogTabelle.toEntity(EnAenderungslogTabelle.meldungen);
        //				aenderungsLog.ident=Integer.toString(pMeldung.meldungsIdent);
        //				aenderungsLog.aktion=EnAenderungslogAktion.toEntity(EnAenderungslogAktion.aenderung);
        //				aenderungsLog.feld="Name";
        //				aenderungsLog.alt=VclMeldungOldVersion.name;
        //				aenderungsLog.neu=pMeldung.name;
        //				lDbBundle.dbAenderungslog.insert(aenderungsLog);
        //			}
        //			if (!pMeldung.vorname.equals(VclMeldungOldVersion.vorname)){
        //				EclAenderungslog aenderungsLog=new EclAenderungslog();
        //				aenderungsLog.tabelle=EnAenderungslogTabelle.toEntity(EnAenderungslogTabelle.meldungen);
        //				aenderungsLog.ident=Integer.toString(pMeldung.meldungsIdent);
        //				aenderungsLog.aktion=EnAenderungslogAktion.toEntity(EnAenderungslogAktion.aenderung);
        //				aenderungsLog.feld="Vorname";
        //				aenderungsLog.alt=VclMeldungOldVersion.vorname;
        //				aenderungsLog.neu=pMeldung.vorname;
        //				lDbBundle.dbAenderungslog.insert(aenderungsLog);
        //			}
        //			if (!pMeldung.zuHdCo.equals(VclMeldungOldVersion.zuHdCo)){
        //				EclAenderungslog aenderungsLog=new EclAenderungslog();
        //				aenderungsLog.tabelle=EnAenderungslogTabelle.toEntity(EnAenderungslogTabelle.meldungen);
        //				aenderungsLog.ident=Integer.toString(pMeldung.meldungsIdent);
        //				aenderungsLog.aktion=EnAenderungslogAktion.toEntity(EnAenderungslogAktion.aenderung);
        //				aenderungsLog.feld="zu Händen";
        //				aenderungsLog.alt=VclMeldungOldVersion.zuHdCo;
        //				aenderungsLog.neu=pMeldung.zuHdCo;
        //				lDbBundle.dbAenderungslog.insert(aenderungsLog);
        //			}
        //			if (!pMeldung.zusatz1.equals(VclMeldungOldVersion.zusatz1)){
        //				EclAenderungslog aenderungsLog=new EclAenderungslog();
        //				aenderungsLog.tabelle=EnAenderungslogTabelle.toEntity(EnAenderungslogTabelle.meldungen);
        //				aenderungsLog.ident=Integer.toString(pMeldung.meldungsIdent);
        //				aenderungsLog.aktion=EnAenderungslogAktion.toEntity(EnAenderungslogAktion.aenderung);
        //				aenderungsLog.feld="Zusatz 1";
        //				aenderungsLog.alt=VclMeldungOldVersion.zusatz1;
        //				aenderungsLog.neu=pMeldung.zusatz1;
        //				lDbBundle.dbAenderungslog.insert(aenderungsLog);
        //			}
        //			if (!pMeldung.zusatz2.equals(VclMeldungOldVersion.zusatz2)){
        //				EclAenderungslog aenderungsLog=new EclAenderungslog();
        //				aenderungsLog.tabelle=EnAenderungslogTabelle.toEntity(EnAenderungslogTabelle.meldungen);
        //				aenderungsLog.ident=Integer.toString(pMeldung.meldungsIdent);
        //				aenderungsLog.aktion=EnAenderungslogAktion.toEntity(EnAenderungslogAktion.aenderung);
        //				aenderungsLog.feld="Zusatz 2";
        //				aenderungsLog.alt=VclMeldungOldVersion.zusatz2;
        //				aenderungsLog.neu=pMeldung.zusatz2;
        //				lDbBundle.dbAenderungslog.insert(aenderungsLog);
        //			}
        //			if (!pMeldung.strasse.equals(VclMeldungOldVersion.strasse)){
        //				EclAenderungslog aenderungsLog=new EclAenderungslog();
        //				aenderungsLog.tabelle=EnAenderungslogTabelle.toEntity(EnAenderungslogTabelle.meldungen);
        //				aenderungsLog.ident=Integer.toString(pMeldung.meldungsIdent);
        //				aenderungsLog.aktion=EnAenderungslogAktion.toEntity(EnAenderungslogAktion.aenderung);
        //				aenderungsLog.feld="Strasse";
        //				aenderungsLog.alt=VclMeldungOldVersion.strasse;
        //				aenderungsLog.neu=pMeldung.strasse;
        //				lDbBundle.dbAenderungslog.insert(aenderungsLog);
        //			}
        //			if (!pMeldung.land.equals(VclMeldungOldVersion.land)){
        //				EclAenderungslog aenderungsLog=new EclAenderungslog();
        //				aenderungsLog.tabelle=EnAenderungslogTabelle.toEntity(EnAenderungslogTabelle.meldungen);
        //				aenderungsLog.ident=Integer.toString(pMeldung.meldungsIdent);
        //				aenderungsLog.aktion=EnAenderungslogAktion.toEntity(EnAenderungslogAktion.aenderung);
        //				aenderungsLog.feld="Land";
        //				aenderungsLog.alt=VclMeldungOldVersion.land;
        //				aenderungsLog.neu=pMeldung.land;
        //				lDbBundle.dbAenderungslog.insert(aenderungsLog);
        //			}
        //			if (!pMeldung.plz.equals(VclMeldungOldVersion.plz)){
        //				EclAenderungslog aenderungsLog=new EclAenderungslog();
        //				aenderungsLog.tabelle=EnAenderungslogTabelle.toEntity(EnAenderungslogTabelle.meldungen);
        //				aenderungsLog.ident=Integer.toString(pMeldung.meldungsIdent);
        //				aenderungsLog.aktion=EnAenderungslogAktion.toEntity(EnAenderungslogAktion.aenderung);
        //				aenderungsLog.feld="PLZ";
        //				aenderungsLog.alt=VclMeldungOldVersion.plz;
        //				aenderungsLog.neu=pMeldung.plz;
        //				lDbBundle.dbAenderungslog.insert(aenderungsLog);
        //			}
        //			if (!pMeldung.ort.equals(VclMeldungOldVersion.ort)){
        //				EclAenderungslog aenderungsLog=new EclAenderungslog();
        //				aenderungsLog.tabelle=EnAenderungslogTabelle.toEntity(EnAenderungslogTabelle.meldungen);
        //				aenderungsLog.ident=Integer.toString(pMeldung.meldungsIdent);
        //				aenderungsLog.aktion=EnAenderungslogAktion.toEntity(EnAenderungslogAktion.aenderung);
        //				aenderungsLog.feld="Ort";
        //				aenderungsLog.alt=VclMeldungOldVersion.ort;
        //				aenderungsLog.neu=pMeldung.ort;
        //				lDbBundle.dbAenderungslog.insert(aenderungsLog);
        //			}
        //			if (!pMeldung.gebDatum.equals(VclMeldungOldVersion.gebDatum)){
        //				EclAenderungslog aenderungsLog=new EclAenderungslog();
        //				aenderungsLog.tabelle=EnAenderungslogTabelle.toEntity(EnAenderungslogTabelle.meldungen);
        //				aenderungsLog.ident=Integer.toString(pMeldung.meldungsIdent);
        //				aenderungsLog.aktion=EnAenderungslogAktion.toEntity(EnAenderungslogAktion.aenderung);
        //				aenderungsLog.feld="gebDatum";
        //				aenderungsLog.alt=VclMeldungOldVersion.gebDatum;
        //				aenderungsLog.neu=pMeldung.gebDatum;
        //				lDbBundle.dbAenderungslog.insert(aenderungsLog);
        //			}
        //			if (!pMeldung.mailadresse.equals(VclMeldungOldVersion.mailadresse)){
        //				EclAenderungslog aenderungsLog=new EclAenderungslog();
        //				aenderungsLog.tabelle=EnAenderungslogTabelle.toEntity(EnAenderungslogTabelle.meldungen);
        //				aenderungsLog.ident=Integer.toString(pMeldung.meldungsIdent);
        //				aenderungsLog.aktion=EnAenderungslogAktion.toEntity(EnAenderungslogAktion.aenderung);
        //				aenderungsLog.feld="Mailadresse";
        //				aenderungsLog.alt=VclMeldungOldVersion.mailadresse;
        //				aenderungsLog.neu=pMeldung.mailadresse;
        //				lDbBundle.dbAenderungslog.insert(aenderungsLog);
        //			}
        //			if (pMeldung.kommunikationssprache!=VclMeldungOldVersion.kommunikationssprache){
        //				EclAenderungslog aenderungsLog=new EclAenderungslog();
        //				aenderungsLog.tabelle=EnAenderungslogTabelle.toEntity(EnAenderungslogTabelle.meldungen);
        //				aenderungsLog.ident=Integer.toString(pMeldung.meldungsIdent);
        //				aenderungsLog.aktion=EnAenderungslogAktion.toEntity(EnAenderungslogAktion.aenderung);
        //				aenderungsLog.feld="Kommunikationssprache";
        //				aenderungsLog.alt=Integer.toString(VclMeldungOldVersion.kommunikationssprache);
        //				aenderungsLog.neu=Integer.toString(pMeldung.kommunikationssprache);
        //				lDbBundle.dbAenderungslog.insert(aenderungsLog);
        //			}
        //			if (!pMeldung.zusatzfeld1.equals(VclMeldungOldVersion.zusatzfeld1)){
        //				EclAenderungslog aenderungsLog=new EclAenderungslog();
        //				aenderungsLog.tabelle=EnAenderungslogTabelle.toEntity(EnAenderungslogTabelle.meldungen);
        //				aenderungsLog.ident=Integer.toString(pMeldung.meldungsIdent);
        //				aenderungsLog.aktion=EnAenderungslogAktion.toEntity(EnAenderungslogAktion.aenderung);
        //				aenderungsLog.feld="zusatzfeld1";
        //				aenderungsLog.alt=VclMeldungOldVersion.zusatzfeld1;
        //				aenderungsLog.neu=pMeldung.zusatzfeld1;
        //				lDbBundle.dbAenderungslog.insert(aenderungsLog);
        //			}
        //			if (!pMeldung.zusatzfeld2.equals(VclMeldungOldVersion.zusatzfeld2)){
        //				EclAenderungslog aenderungsLog=new EclAenderungslog();
        //				aenderungsLog.tabelle=EnAenderungslogTabelle.toEntity(EnAenderungslogTabelle.meldungen);
        //				aenderungsLog.ident=Integer.toString(pMeldung.meldungsIdent);
        //				aenderungsLog.aktion=EnAenderungslogAktion.toEntity(EnAenderungslogAktion.aenderung);
        //				aenderungsLog.feld="zusatzfeld2";
        //				aenderungsLog.alt=VclMeldungOldVersion.zusatzfeld2;
        //				aenderungsLog.neu=pMeldung.zusatzfeld2;
        //				lDbBundle.dbAenderungslog.insert(aenderungsLog);
        //			}
        //			if (!pMeldung.zusatzfeld3.equals(VclMeldungOldVersion.zusatzfeld3)){
        //				EclAenderungslog aenderungsLog=new EclAenderungslog();
        //				aenderungsLog.tabelle=EnAenderungslogTabelle.toEntity(EnAenderungslogTabelle.meldungen);
        //				aenderungsLog.ident=Integer.toString(pMeldung.meldungsIdent);
        //				aenderungsLog.aktion=EnAenderungslogAktion.toEntity(EnAenderungslogAktion.aenderung);
        //				aenderungsLog.feld="zusatzfeld3";
        //				aenderungsLog.alt=VclMeldungOldVersion.zusatzfeld3;
        //				aenderungsLog.neu=pMeldung.zusatzfeld3;
        //				lDbBundle.dbAenderungslog.insert(aenderungsLog);
        //			}
        //			if (!pMeldung.zusatzfeld4.equals(VclMeldungOldVersion.zusatzfeld4)){
        //				EclAenderungslog aenderungsLog=new EclAenderungslog();
        //				aenderungsLog.tabelle=EnAenderungslogTabelle.toEntity(EnAenderungslogTabelle.meldungen);
        //				aenderungsLog.ident=Integer.toString(pMeldung.meldungsIdent);
        //				aenderungsLog.aktion=EnAenderungslogAktion.toEntity(EnAenderungslogAktion.aenderung);
        //				aenderungsLog.feld="zusatzfeld4";
        //				aenderungsLog.alt=VclMeldungOldVersion.zusatzfeld4;
        //				aenderungsLog.neu=pMeldung.zusatzfeld4;
        //				lDbBundle.dbAenderungslog.insert(aenderungsLog);
        //			}
        //			if (!pMeldung.zusatzfeld5.equals(VclMeldungOldVersion.zusatzfeld5)){
        //				EclAenderungslog aenderungsLog=new EclAenderungslog();
        //				aenderungsLog.tabelle=EnAenderungslogTabelle.toEntity(EnAenderungslogTabelle.meldungen);
        //				aenderungsLog.ident=Integer.toString(pMeldung.meldungsIdent);
        //				aenderungsLog.aktion=EnAenderungslogAktion.toEntity(EnAenderungslogAktion.aenderung);
        //				aenderungsLog.feld="zusatzfeld5";
        //				aenderungsLog.alt=VclMeldungOldVersion.zusatzfeld5;
        //				aenderungsLog.neu=pMeldung.zusatzfeld5;
        //				lDbBundle.dbAenderungslog.insert(aenderungsLog);
        //			}
        //
        //
        //
        //			if (pMeldung.gruppe!=VclMeldungOldVersion.gruppe){
        //				EclAenderungslog aenderungsLog=new EclAenderungslog();
        //				aenderungsLog.tabelle=EnAenderungslogTabelle.toEntity(EnAenderungslogTabelle.meldungen);
        //				aenderungsLog.ident=Integer.toString(pMeldung.meldungsIdent);
        //				aenderungsLog.aktion=EnAenderungslogAktion.toEntity(EnAenderungslogAktion.aenderung);
        //				aenderungsLog.feld="Gruppe";
        //				aenderungsLog.alt=Integer.toString(VclMeldungOldVersion.gruppe);
        //				aenderungsLog.neu=Integer.toString(pMeldung.gruppe);
        //				lDbBundle.dbAenderungslog.insert(aenderungsLog);
        //			}
        //
        //			if (pMeldung.manuellGeaendertSeitLetztemImport!=VclMeldungOldVersion.manuellGeaendertSeitLetztemImport){
        //				EclAenderungslog aenderungsLog=new EclAenderungslog();
        //				aenderungsLog.tabelle=EnAenderungslogTabelle.toEntity(EnAenderungslogTabelle.meldungen);
        //				aenderungsLog.ident=Integer.toString(pMeldung.meldungsIdent);
        //				aenderungsLog.aktion=EnAenderungslogAktion.toEntity(EnAenderungslogAktion.aenderung);
        //				aenderungsLog.feld="manuellGeaendertSeitLetztemImport";
        //				aenderungsLog.alt=Integer.toString(VclMeldungOldVersion.manuellGeaendertSeitLetztemImport);
        //				aenderungsLog.neu=Integer.toString(pMeldung.manuellGeaendertSeitLetztemImport);
        //				lDbBundle.dbAenderungslog.insert(aenderungsLog);
        //			}

        return (1);
    }

    /**Führt eine Update-Aktion aus, (aber nur wenn tatsächlich Veränderungen vorliegen),
     * und schreibt zusätzliche Protokollsatz weg
     */
    public int update_MitLog(EclMeldung pNeueMeldung, EclMeldung pAlteMeldung) {
        int rc;
        rc = update(pNeueMeldung);
        if (rc < 1) {
            return rc;
        }
        String updateZeit = CaDatumZeit.DatumZeitStringFuerDatenbank();
        int userNummer = dbBundle.clGlobalVar.benutzernr;
        int arbeitsplatzNummer = dbBundle.clGlobalVar.arbeitsplatz;

        insertProtokoll(1, pAlteMeldung, updateZeit, userNummer, arbeitsplatzNummer);
        insertProtokoll(2, pNeueMeldung, updateZeit, userNummer, arbeitsplatzNummer);

        return rc;
    }

    /**Update der login-Server-Nummer für ident. 
     *
     * db_version wird nicht erhöht!
     * -1 => unbekannter Fehler
     * 1 = Update wurde durchgeführt.
     * 
     */
    public int updatePraesenz(int pMeldeIdent, int pKennungIdent, int pZugangOderAbgang) {

        try {
            int istPraesenz1, istPraesenz2, sollPraesenz;
            if (pZugangOderAbgang == 1) { //Zugang
                istPraesenz1 = 0;
                istPraesenz2 = 2;
                sollPraesenz = 1;
            } else {//Abgang
                istPraesenz1 = 1;
                istPraesenz2 = 1;
                sollPraesenz = 2;
                pKennungIdent = 0;
            }

            String lSql = "UPDATE " + dbBundle.getSchemaMandant() + "tbl_meldungen SET "
                    + "statusPraesenz=?, statusPraesenz_Delayed=?, statusWarPraesenz=?, statusWarPraesenz_Delayed=?, virtuellerTeilnehmerIdent=?,"
                    + "db_version=db_version+1 " + "WHERE "
                    + "meldungsIdent=? AND (statusPraesenz=? OR statusPraesenz=?) ";

            PreparedStatement lPStm = verbindung.prepareStatement(lSql);
            lPStm.setInt(1, sollPraesenz);
            lPStm.setInt(2, sollPraesenz);
            lPStm.setInt(3, 1);
            lPStm.setInt(4, 1);
            lPStm.setInt(5, pKennungIdent);
            lPStm.setInt(6, pMeldeIdent);
            lPStm.setInt(7, istPraesenz1);
            lPStm.setInt(8, istPraesenz2);

            int ergebnis = lPStm.executeUpdate();
            lPStm.close();
            if (ergebnis == 0) { //Dann konnte kein Update durchgeführt werden
                return 0;
            }
        } catch (Exception e1) {
            CaBug.drucke("001");
            System.err.println(" " + e1.getMessage());
            return (-1);
        }

        return (1);
    }

    /**Bucht alle Meldungen, bei denen pKennungIdent eingetragen ist, als Abgang*/
    public int updatePraesenzAbgangFuerTeilnehmerIdent(int pKennungIdent) {

        try {

            String lSql = "UPDATE " + dbBundle.getSchemaMandant() + "tbl_meldungen SET "
                    + "statusPraesenz=?, virtuellerTeilnehmerIdent=? " + "db_version=db_version+1 " + "WHERE "
                    + "virtuellerTeilnehmerIdent=? AND statusPraesenz=1 ";

            PreparedStatement lPStm = verbindung.prepareStatement(lSql);
            lPStm.setInt(1, 2);
            lPStm.setInt(2, 0);
            lPStm.setInt(3, pKennungIdent);

            int ergebnis = lPStm.executeUpdate();
            lPStm.close();
            if (ergebnis == 0) { //Dann konnte kein Update durchgeführt werden
                return 0;
            }
        } catch (Exception e1) {
            CaBug.drucke("001");
            System.err.println(" " + e1.getMessage());
            return (-1);
        }

        return (1);
    }

    /**Überträgt statusPraesenz aus pMeldungen in zusatzfeld4*/
    public int updateOnlinePraesenz(List<EclMeldung> pMeldungen) {
        
        /*Step 1: alle bisherigen Kennzeichen zurücksetzen*/

        try {

            String lSql = "UPDATE " + dbBundle.getSchemaMandant() + "tbl_meldungen SET "
                    + "zusatzfeld4='' WHERE "
                    + "zusatzfeld4!='' ";

            PreparedStatement lPStm = verbindung.prepareStatement(lSql);

            int ergebnis = lPStm.executeUpdate();
            lPStm.close();
        } catch (Exception e1) {
            CaBug.drucke("001");
            System.err.println(" " + e1.getMessage());
            return (-1);
        }

        /*Step 2: für Meldungen in Liste den Status setzen*/
        if (pMeldungen!=null) {
            for (int i=0;i<pMeldungen.size();i++) {
                int lMeldungsIdent=pMeldungen.get(i).meldungsIdent;
                String lStatusPraesenz=Integer.toString(pMeldungen.get(i).statusPraesenz);
                try {

                    String lSql = "UPDATE " + dbBundle.getSchemaMandant() + "tbl_meldungen SET "
                            + "zusatzfeld4=? WHERE "
                            + "meldungsIdent=? ";

                    PreparedStatement lPStm = verbindung.prepareStatement(lSql);
                    lPStm.setString(1, lStatusPraesenz);
                    lPStm.setInt(2, lMeldungsIdent);
 
                    int ergebnis = lPStm.executeUpdate();
                    lPStm.close();
                    if (ergebnis == 0) { //Dann konnte kein Update durchgeführt werden
                        CaBug.drucke("002");
                        return 0;
                    }
                } catch (Exception e1) {
                    CaBug.drucke("003");
                    System.err.println(" " + e1.getMessage());
                    return (-1);
                }
            }
        }
        
        
        return 1;
    }
    
    
    
    /** Liefert (anstelle von *) die Feldaufreihung für einen Select, bei dem nicht alle Felder
     * zur Verfügung stehen, sondern nur diejenigen, die z.B. für Suchanzeigen u.ä. benötigt werden
     * @return
     */
    private String liefereSelectStringShort() {
        String selectString = "";
        selectString = "m.meldungsIdent, m.db_version, m.mandant, m.meldungAktiv, m.klasse, "
                + "m.aktionaersnummer, m.aktienregisterIdent, m.externeIdent, "
                + "m.meldungstyp, m.skMitWeisungen, m.skIst, m.skWeisungsartZulaessig, m.skBuchbarInternet, m.skBuchbarPapier, m.skBuchbarHV, m.skBuchbarVollmachtDritte, m.skBuchbarVollmachtDritteHV, "
                + "m.gattung, m.fixAnmeldung, m.stueckAktien, m.stimmen, m.besitzart, m.stimmausschluss, m.personenNatJurIdent, "
                + "p.name, p.vorname, p.ort, " + "m.delayedVorhanden, m.pendingVorhanden, "
                + "m.zutrittsIdent, m.zutrittsIdent_Delayed, " + "m.stimmkarte, m.stimmkarte_Delayed, "
                + "m.stimmkarteSecond, m.stimmkarteSecond_Delayed, "
                + "m.meldungEnthaltenInSammelkarte, m.meldungEnthaltenInSammelkarte_Delayed, "
                + "m.meldungEnthaltenInSammelkarteArt, m.meldungEnthaltenInSammelkarteArt_Delayed, "
                + "m.weisungVorhanden, m.weisungVorhanden_Delayed, " + "m.vertreterName, m.vertreterName_Delayed, "
                + "m.vertreterVorname, m.vertreterVorname_Delayed, " + "m.vertreterOrt, m.vertreterOrt_Delayed, "
                + "m.willenserklaerungMitVollmacht, m.willenserklaerungMitVollmacht_Delayed, "
                + "m.statusPraesenz, m.statusPraesenz_Delayed, "
                + "m.statusWarPraesenz, m.statusWarPraesenz_Delayed, m.virtuellerTeilnehmerIdent, "
                + "p.mandant, p.ident" + "";
        return selectString;
    }

    /** ****************************************************************************
     * Block von in sich abhängigen Funktionen für das Durchlesen und Verarbeiten
     * der Meldungen.
     * 
     * Grundablauf:
     * 
     * readarray_init_xxxx - initialisiert Lesevorgang und führt ihn aus
     * 		xxxx gibt dabei Verwendungszweck wieder.
     * 		Welche "xxxx" aufgerufen wurde, wird intern in readarray_art
     * 		gespeichert.
     * 		Derzeit implementiert:
     * 			1 = SucheNachNamen = Verwendung für Suchfunktion, Suche nach Namen; Parameter = klasse, -1 = alle
     * 
     * readarray_getoffset - liefert clMeldung, aber nur die Felder gefüllt, die
     * 		gemäß xxxx (siehe oben) implementiert sind (aus Effizienzgründen).
     * 		Die Funktion ist so implementiert, dass wiederholte getoffset mit selbem
     * 		offset "gepuffert" werden, d.h. nicht wiederholt ausgeführt werden.
     */
    public int readarray_anzahl;
    private int readarray_art;
    private int readarray_aktuelleroffset;
    private EclMeldung bufferMeldung;
    private PreparedStatement readarray_pstm;
    private String readarray_sql;
    private ResultSet readarray_ergebnis;

    public int readarray_SucheNachNamen_init(int pKlasse, String pName) {

        readarray_art = 1;
        readarray_aktuelleroffset = -1;
        pName = pName.trim();

        try {

            if (pName.isEmpty()) {
                readarray_sql = "SELECT " + liefereSelectStringShort() + " from " + dbBundle.getSchemaMandant()
                        + "tbl_meldungen m " + "INNER JOIN " + dbBundle.getSchemaMandant()
                        + "tbl_personenNatJur p ON m.personenNatJurIdent=p.ident "
                        + "where m.mandant=? and p.mandant=? and m.meldungaktiv=1 ";
                if (pKlasse != -1) {
                    readarray_sql = readarray_sql + " and m.klasse=?";
                }

                readarray_pstm = verbindung.prepareStatement(readarray_sql, ResultSet.TYPE_SCROLL_INSENSITIVE,
                        ResultSet.CONCUR_READ_ONLY);
                readarray_pstm.setInt(1, dbBundle.clGlobalVar.mandant);
                readarray_pstm.setInt(2, dbBundle.clGlobalVar.mandant);
                if (pKlasse != -1) {
                    readarray_pstm.setInt(3, pKlasse);
                }

            } else {
                readarray_sql = "SELECT " + liefereSelectStringShort() + " from " + dbBundle.getSchemaMandant()
                        + "tbl_meldungen m " + "INNER JOIN " + dbBundle.getSchemaMandant()
                        + "tbl_personenNatJur p ON m.personenNatJurIdent=p.ident "
                        + "where UPPER(p.name) LIKE UPPER(?) and m.mandant=? and p.mandant=? and m.meldungaktiv=1 ";
                if (pKlasse != -1) {
                    readarray_sql = readarray_sql + " and klasse=?";
                }

                readarray_pstm = verbindung.prepareStatement(readarray_sql, ResultSet.TYPE_SCROLL_INSENSITIVE,
                        ResultSet.CONCUR_READ_ONLY);
                readarray_pstm.setString(1, "%" + pName + "%");
                readarray_pstm.setInt(2, dbBundle.clGlobalVar.mandant);
                readarray_pstm.setInt(3, dbBundle.clGlobalVar.mandant);
                if (pKlasse != -1) {
                    readarray_pstm.setInt(4, pKlasse);
                }

            }

            readarray_ergebnis = readarray_pstm.executeQuery();
            readarray_ergebnis.last();
            readarray_anzahl = readarray_ergebnis.getRow();
            readarray_ergebnis.beforeFirst();

        } catch (Exception e) {
            CaBug.drucke("DbMeldungen.readarray_SucheNachNamen_init 001");
            System.err.println(" " + e.getMessage());
        }
        return (1); /*andere Returnwerte für Fehlerbehandlung vorgesehen*/
    }

    public int readarray_SucheNachNamen_getoffset(int offset, EclMeldung pMeldung) {
        //			System.out.println("Offset"+offset);
        if (readarray_aktuelleroffset != offset) {
            try {
                readarray_ergebnis.absolute(offset);
                readarray_aktuelleroffset = offset;
                bufferMeldung = null;
                if (readarray_ergebnis.next() == true) {
                    bufferMeldung = decodeErgebnisShort(readarray_ergebnis);
                    //						System.out.println("nach bufferMeldungfüllen");
                }

            } catch (SQLException e) {
                CaBug.drucke("DbMeldungen.readarray_SucheNachNamen_getoffset 001");
                e.printStackTrace();
            }
        }
        bufferMeldung.copyTo(pMeldung);
        //			System.out.println("Name etc"+VclMeldung.name+VclMeldung.vorname+VclMeldung.ort);
        return (1); /*andere Returnwerte für Fehlerbehandlung vorgesehen*/

    }

    /**************Gästeliste********************************************************/
    private ResultSet lErgebnis_liste = null;
    private PreparedStatement lPStm_liste = null;

    /**Sortierung:
     * 1=zutrittsident
     * 2=Name
     * 3=meldeIdent
     */
    public int readinit_gastListe(int sortierung, int selektion) {
        int anzInArray = 0;

        try {
            String lSql = "SELECT * from " + dbBundle.getSchemaMandant() + "tbl_meldungen m " + "INNER JOIN "
                    + dbBundle.getSchemaMandant() + "tbl_personenNatJur p ON m.personenNatJurIdent=p.ident "
                    + "WHERE m.mandant=? and p.mandant=? " + "AND m.meldungAktiv=1 " + "AND m.klasse=0 ";
            switch (selektion) {
            //				case 1:{lSql=lSql+" AND (m.meldungstyp=1 OR m.meldungstyp=3) ";break;}
            //				case 2:{lSql=lSql+" AND m.meldungstyp=2 ";break;}
            }
            switch (sortierung) {
            case 1: {
                lSql = lSql + " ORDER BY m.zutrittsIdent ";
                break;
            }
            case 2: {
                lSql = lSql + " ORDER BY p.name ";
                break;
            }
            case 3: {
                lSql = lSql + " ORDER BY m.meldungsIdent ";
                break;
            }
            }

            //				System.out.println(lSql);

            lPStm_liste = verbindung.prepareStatement(lSql, ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
            lPStm_liste.setInt(1, dbBundle.clGlobalVar.mandant);
            lPStm_liste.setInt(2, dbBundle.clGlobalVar.mandant);

            lErgebnis_liste = lPStm_liste.executeQuery();
            lErgebnis_liste.last();
            anzInArray = lErgebnis_liste.getRow();
            lErgebnis_liste.beforeFirst();

            meldungenArray = new EclMeldung[1];

        } catch (Exception e) {
            CaBug.drucke("DbMeldungen.readinit_gastListe 003");
            System.err.println(" " + e.getMessage());
            return (-1);
        }
        return (anzInArray);

    }

    /**
     * 
     * @return
     * true=Satz gelesen
     * false=ende erreicht, kein Satz mehr gelesen
     */
    public boolean readnext_gastliste() {
        try {
            if (lErgebnis_liste.next() == true) {
                meldungenArray[0] = decodeErgebnis(lErgebnis_liste);

                return true;
            } else {
                lErgebnis_liste.close();
                lPStm_liste.close();
                return false;
            }

        } catch (SQLException e) {
            CaBug.drucke("DbMeldungen.readnext_gastliste 003");
            System.err.println(" " + e.getMessage());
            return false;
        }
    }

    /*******************Verein**************************************/

    private int read_PreparedStatementVerein(PreparedStatement pstm) {
        int anzInArray = 0;

        try {
            ResultSet ergebnis = pstm.executeQuery();
            ergebnis.last();
            anzInArray = ergebnis.getRow();
            ergebnis.close();
            pstm.close();

        } catch (Exception e) {
            CaBug.drucke("DbMeldungen.read_PreparedStatementVerein 001");
            System.err.println(" " + e.getMessage());
        }

        return (anzInArray);

    }

    public int anzahlStimmberechtigtePraesent() {

        String sql = "";
        sql = "SELECT * from " + dbBundle.getSchemaMandant() + "tbl_meldungen m "
                + "where m.zusatzfeld2='S' and m.statusPraesenz=1  " + " and m.mandant=?";

        PreparedStatement pstm1 = null;
        try {
            pstm1 = verbindung.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            pstm1.setInt(1, dbBundle.clGlobalVar.mandant);
        } catch (SQLException e) {
            CaBug.drucke("DbMeldungen.leseSammelkartenBriefwahl 001");
            System.err.println(" " + e.getMessage());
        }

        return (read_PreparedStatementVerein(pstm1));
    }

    public int anzahlStimmberechtigteJemalsPraesent() {

        String sql = "";
        sql = "SELECT * from " + dbBundle.getSchemaMandant() + "tbl_meldungen m "
                + "where m.zusatzfeld2='S' and (m.statusPraesenz=1 OR m.statusPraesenz=2) " + " and m.mandant=?";

        PreparedStatement pstm1 = null;
        try {
            pstm1 = verbindung.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            pstm1.setInt(1, dbBundle.clGlobalVar.mandant);
        } catch (SQLException e) {
            CaBug.drucke("DbMeldungen.leseSammelkartenBriefwahl 001");
            System.err.println(" " + e.getMessage());
        }

        return (read_PreparedStatementVerein(pstm1));
    }

    public int anzahlNichtStimmberechtigtePraesent() {

        String sql = "";
        sql = "SELECT * from " + dbBundle.getSchemaMandant() + "tbl_meldungen m "
                + "where m.zusatzfeld2!='S' and m.statusPraesenz=1  " + " and m.mandant=?";

        PreparedStatement pstm1 = null;
        try {
            pstm1 = verbindung.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            pstm1.setInt(1, dbBundle.clGlobalVar.mandant);
        } catch (SQLException e) {
            CaBug.drucke("DbMeldungen.leseSammelkartenBriefwahl 001");
            System.err.println(" " + e.getMessage());
        }

        return (read_PreparedStatementVerein(pstm1));
    }

    public int anzahlNichtStimmberechtigteJemalsPraesent() {

        String sql = "";
        sql = "SELECT * from " + dbBundle.getSchemaMandant() + "tbl_meldungen m "
                + "where m.zusatzfeld2!='S' and (m.statusPraesenz=1 OR m.statusPraesenz=2)  " + " and m.mandant=?";

        PreparedStatement pstm1 = null;
        try {
            pstm1 = verbindung.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            pstm1.setInt(1, dbBundle.clGlobalVar.mandant);
        } catch (SQLException e) {
            CaBug.drucke("DbMeldungen.leseSammelkartenBriefwahl 001");
            System.err.println(" " + e.getMessage());
        }

        return (read_PreparedStatementVerein(pstm1));
    }

    private long read_PreparedStatementAnzahlLong(String pSql) {
        long ergebnis = 0;

        PreparedStatement pstm1 = null;
        try {
            pstm1 = verbindung.prepareStatement(pSql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            pstm1.setInt(1, dbBundle.clGlobalVar.mandant);

            ResultSet lErgebnis = pstm1.executeQuery();
            lErgebnis.last();
            lErgebnis.beforeFirst();

            while (lErgebnis.next() == true) {
                ergebnis = lErgebnis.getLong(1);
            }
            lErgebnis.close();
            pstm1.close();

        } catch (SQLException e) {
            CaBug.drucke("DbMeldungen.read_PreparedStatementAnzahl 001");
            System.err.println(" " + e.getMessage());
        }

        return ergebnis;

    }

    /**Summe Briefwahlstimmen (ku217)*/
    public long anzahlStimmenBriefwahl() {
        String sql = "";
        sql = "SELECT SUM(stimmen) from " + dbBundle.getSchemaMandant() + "tbl_meldungen m "
                + "where m.meldungAktiv=1 and m.meldungstyp=3 and m.gattung=1 " + " and m.mandant=?";
        return read_PreparedStatementAnzahlLong(sql);
    }

    private int read_PreparedStatementAnzahl(String pSql) {
        int ergebnis = 0;

        PreparedStatement pstm1 = null;
        try {
            pstm1 = verbindung.prepareStatement(pSql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            pstm1.setInt(1, dbBundle.clGlobalVar.mandant);

            ResultSet lErgebnis = pstm1.executeQuery();
            lErgebnis.last();
            lErgebnis.beforeFirst();

            while (lErgebnis.next() == true) {
                ergebnis = lErgebnis.getInt(1);
            }
            lErgebnis.close();
            pstm1.close();

        } catch (SQLException e) {
            CaBug.drucke("DbMeldungen.read_PreparedStatementAnzahl 001");
            System.err.println(" " + e.getMessage());
        }

        return ergebnis;

    }

    /**Anzahl Briefwähler (ku217)*/
    public int anzahlMitgliederBriefwahl() {
        String sql = "";
        sql = "SELECT COUNT(*) from " + dbBundle.getSchemaMandant() + "tbl_meldungen m "
                + "where m.meldungAktiv=1 and m.meldungstyp=3 and m.gattung=1 " + " and m.mandant=?";
        return read_PreparedStatementAnzahl(sql);
    }

    public int anzahlMitgliederStimmberechtigtAktuell() {
        String sql = "";
        sql = "SELECT COUNT(*) from " + dbBundle.getSchemaMandant() + "tbl_meldungen m "
                + "where m.zusatzfeld2='S' and m.statusPraesenz=1  " + " and m.mandant=?";
        return read_PreparedStatementAnzahl(sql);
    }

    public int anzahlMitgliederStimmberechtigtJemals() {
        String sql = "";
        sql = "SELECT COUNT(*) from " + dbBundle.getSchemaMandant() + "tbl_meldungen m "
                + "where m.zusatzfeld2='S' and (m.statusPraesenz=1 OR m.statusPraesenz=2) " + " and m.mandant=?";
        return read_PreparedStatementAnzahl(sql);
    }

    public int anzahlMitgliederNichtStimmberechtigtAktuell() {
        String sql = "";
        sql = "SELECT COUNT(*) from " + dbBundle.getSchemaMandant() + "tbl_meldungen m "
                + "where m.zusatzfeld2!='S' and m.statusPraesenz=1  " + "and (m.zusatzfeld4='Mitglied' OR m.zusatzfeld4='EOW') "
                + " and m.mandant=?";
        return read_PreparedStatementAnzahl(sql);
    }

    public int anzahlMitgliederNichtStimmberechtigtJemals() {
        String sql = "";
        sql = "SELECT COUNT(*) from " + dbBundle.getSchemaMandant() + "tbl_meldungen m "
                + "where m.zusatzfeld2!='S' and (m.statusPraesenz=1 OR m.statusPraesenz=2) "
                + "and (m.zusatzfeld4='Mitglied' OR m.zusatzfeld4='EOW') " + " and m.mandant=?";
        return read_PreparedStatementAnzahl(sql);
    }

    public int anzahlGaesteNichtStimmberechtigtAktuell() {
        String sql = "";
        sql = "SELECT COUNT(*) from " + dbBundle.getSchemaMandant() + "tbl_meldungen m "
                + "where m.zusatzfeld2!='S' and m.statusPraesenz=1  "
                + "and (m.zusatzfeld4='BMO' OR m.zusatzfeld4='GOW'  OR m.zusatzfeld4='BOW') " + " and m.mandant=?";
        return read_PreparedStatementAnzahl(sql);
    }

    public int anzahlGaesteNichtStimmberechtigtJemals() {
        String sql = "";
        sql = "SELECT COUNT(*) from " + dbBundle.getSchemaMandant() + "tbl_meldungen m "
                + "where m.zusatzfeld2!='S' and (m.statusPraesenz=1 OR m.statusPraesenz=2) "
                + "and (m.zusatzfeld4='BMO' OR m.zusatzfeld4='GOW'  OR m.zusatzfeld4='BOW') " + " and m.mandant=?";
        return read_PreparedStatementAnzahl(sql);

    }

    public int anzahlWarnungenPraesent() {
        String sql = "";
        sql = "SELECT COUNT(*) from " + dbBundle.getSchemaMandant() + "tbl_meldungen m "
                + "where m.zusatzfeld5='1' and m.statusPraesenz=1  " + " and m.mandant=?";
        return read_PreparedStatementAnzahl(sql);
    }

    public int updateNurPersonenNatJur(EclMeldung pMeldung) {

        /*Nun noch PersonenNatJur updaten*/
        EclPersonenNatJur lPersonenNatJur = new EclPersonenNatJur();
        lPersonenNatJur.mandant = pMeldung.mandant;
        lPersonenNatJur.db_version = pMeldung.db_version_personenNatJur;
        lPersonenNatJur.gehoertZuMeldung = 1;
        lPersonenNatJur.ident = pMeldung.personenNatJurIdent;
        lPersonenNatJur.istSelbePersonWieIdent = pMeldung.istSelbePersonWieIdent;
        lPersonenNatJur.uebereinstimmungSelbePersonWurdeUeberprueft = pMeldung.uebereinstimmungSelbePersonWurdeUeberprueft;
        /*TODOLang: 
         * > istNatuerlichePerson setzen
         * > Stimmausschluß handhaben?
         * */
        lPersonenNatJur.anrede = pMeldung.anrede;
        lPersonenNatJur.titel = pMeldung.titel;
        lPersonenNatJur.adelstitel = pMeldung.adelstitel;
        lPersonenNatJur.name = pMeldung.name;
        lPersonenNatJur.vorname = pMeldung.vorname;
        lPersonenNatJur.kurzName = lPersonenNatJur.name;
        if (lPersonenNatJur.vorname != null && !lPersonenNatJur.vorname.isEmpty()) {
            lPersonenNatJur.kurzName = lPersonenNatJur.kurzName + " " + lPersonenNatJur.vorname;
        }
        lPersonenNatJur.zuHdCo = pMeldung.zuHdCo;
        lPersonenNatJur.zusatz1 = pMeldung.zusatz1;
        lPersonenNatJur.zusatz2 = pMeldung.zusatz2;
        lPersonenNatJur.strasse = pMeldung.strasse;
        lPersonenNatJur.land = pMeldung.land;
        lPersonenNatJur.plz = pMeldung.plz;
        lPersonenNatJur.ort = pMeldung.ort;
        lPersonenNatJur.identVersandadresse = pMeldung.identVersandadresse;
        lPersonenNatJur.kurzOrt = lPersonenNatJur.ort;
        lPersonenNatJur.mailadresse = pMeldung.mailadresse;
        lPersonenNatJur.kommunikationssprache = pMeldung.kommunikationssprache;
        lPersonenNatJur.loginKennung = pMeldung.loginKennung;
        lPersonenNatJur.loginPasswort = pMeldung.loginPasswort;
        lPersonenNatJur.oeffentlicheID = pMeldung.oeffentlicheID;

        int erg = dbBundle.dbPersonenNatJur.update(lPersonenNatJur);
        if (erg < 1) {
            CaBug.drucke("DbMeldungen.update 002 erg=" + erg);
            return (erg);
        }

        return (1);
    }

    public int checkAktienanzahl() {
        String lSql = "SELECT SUM(m.stimmen) FROM " + dbBundle.getSchemaMandant()
        + "tbl_meldungen m WHERE m.mandant=? AND m.meldungaktiv=1 AND m.klasse=1 AND "
        + "(m.meldungstyp=1 OR m.meldungstyp=3)";

        return read_PreparedStatementAnzahl(lSql);
    }

    public List<EclMeldung> readPraesenz() {
        List<EclMeldung> list = new ArrayList<>();
        
        //@formatter:off
        String sql = "SELECT a.aktionaersnummer, a.zutrittsIdent, a.meldungEnthaltenInSammelkarte, a.stimmen,"
                + " b.vorname, b.name, b.ort, c.vorname, c.name, c.ort, a.statusPraesenz, b.kurzName, c.kurzName" 
                + " FROM " + dbBundle.getSchemaMandant() + "tbl_meldungen a"
                + " LEFT JOIN " + dbBundle.getSchemaMandant() + "tbl_personennatjur b ON a.personenNatJurIdent = b.ident"
                + " LEFT JOIN " + dbBundle.getSchemaMandant() + "tbl_personennatjur c ON a.vertreterIdent = c.ident"
                + " where a.statusPraesenz != 0 and a.aktionaersnummer != ''";
        //@formatter:on
        
        try (PreparedStatement pstmt = verbindung.prepareStatement(sql)) {
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    EclMeldung tmp = new EclMeldung();
                    tmp.aktionaersnummer = rs.getString(1);
                    tmp.zutrittsIdent = rs.getString(2);
                    tmp.meldungEnthaltenInSammelkarte = rs.getInt(3);
                    tmp.stimmen = rs.getInt(4);
                    tmp.vorname = rs.getString(5);
                    tmp.name = rs.getString(6);
                    tmp.ort = rs.getString(7);
                    tmp.vertreterVorname = rs.getString(8);
                    tmp.vertreterName = rs.getString(9);
                    tmp.vertreterOrt = rs.getString(10);
                    tmp.statusPraesenz = rs.getInt(11);
                    tmp.zusatz1 = rs.getString(13) != null ? rs.getString(13) : rs.getString(12);
                    list.add(tmp);
                }
            }
        } catch (Exception e) {
            CaBug.drucke("DbMeldungen readPraesenz");
            System.err.println(" " + e.getMessage());
        }
        return list;
    }
    /*++++++++++++++++++++++Ab hier alte Funktionen, die nicht mehr benötigt werden+++++++++++++++++++++++*/

    //		@Deprecated 
    //		/****************Neu-Einfügen des übergebenen Objekts clMeldung in Datenbank********/
    //		/*Rückgabewert = 1 => Einfügen ok
    //		 * 			   = 0 => Meldung bereits vorhanden
    //		 */
    //		public int insertInBatch(EclMeldung VclMeldung){
    //		
    //			
    //			
    //			Integer omeldungsIdent;
    //			Integer omeldungsIdentVorgaenger;
    //			Integer omeldungsIdentNachfolger;
    //			Integer omandant;
    //			Integer omeldungAktiv;
    //			Integer oklasse;
    //			Integer omeldungstyp;
    //			Integer omeldungEnthaltenInSK;
    //			Integer ogattung;
    //			Long ostueckAktien;
    //			Long ostimmen;
    //			Long ostueckAktienDruckEK;
    //			Long ostimmenDruckEK;
    //			Integer ostatusPraesenz;
    //			Integer oteilnahmeArt;
    //			Integer oanrede;
    //			Integer omanuellGeaendertSeitLetztemImport;
    //			Integer ogruppe;
    //			Integer okommunikationssprache;
    //
    //		
    //			omeldungsIdent=VclMeldung.meldungsIdent;
    //			omeldungsIdentVorgaenger=VclMeldung.meldungsIdentVorgaenger;
    //			omeldungsIdentNachfolger=VclMeldung.meldungsIdentNachfolger;
    //			omandant=VclMeldung.mandant;
    //			omeldungAktiv=VclMeldung.meldungAktiv;
    //			oklasse=VclMeldung.klasse;
    //			omeldungstyp=VclMeldung.meldungstyp;
    //			omeldungEnthaltenInSK=VclMeldung.meldungEnthaltenInSammelkarte;
    //			ogattung=VclMeldung.gattung;
    //			ostueckAktien=VclMeldung.stueckAktien;
    //			ostimmen=VclMeldung.stimmen;
    //			ostueckAktienDruckEK=VclMeldung.stueckAktienDruckEK;
    //			ostimmenDruckEK=VclMeldung.stimmenDruckEK;
    //			ostatusPraesenz=VclMeldung.statusPraesenz;
    //			oteilnahmeArt=VclMeldung.teilnahmeArt;
    //			oanrede=VclMeldung.anrede;
    //			omanuellGeaendertSeitLetztemImport=VclMeldung.manuellGeaendertSeitLetztemImport;
    //			ogruppe=VclMeldung.gruppe;
    //			okommunikationssprache=VclMeldung.kommunikationssprache;
    //
    //			
    //			int ergebnis2=0;
    //			
    //			/*try {
    //				verbindung.setAutoCommit(false);
    //			} catch (Exception e2){
    //				System.err.println(" "+e2.getMessage());
    //			}*/
    //			
    //			try {
    //				Integer dbf_istjuristischePerson=0;
    //				/*????????????????????
    //				Statement stm1=verbindung.createStatement();
    //				*/
    //				
    //				String sql1="INSERT INTO "+dbBundle.getSchemaMandant()+"tbl_meldungen "+
    //						"("+
    //						/*"meldungsIdent, "+*/
    //						"mandant, meldungAktiv, klasse, zutrittsIdent, externeIdent, meldungstyp, "+
    //						"meldungEnthaltenInSK, stimmkarte, stimmkarteSecond, stimmkarteVorab, stimmkarteSecondVorab, "+
    //						"gattung, stueckAktien, stimmen, stueckAktienDruckEK, stimmenDruckEK, besitzart, "+
    //						"stimmausschluss, statusPraesenz, teilnahmeArt, anrede, titel, adelstitel, "+
    //						"name, vorname, zuHdCo, zusatz1, zusatz2, strasse, land, plz, ort, mailadresse, "+
    //						"kommunikationssprache, gebDatum, "+
    //						"zusatzfeld1, zusatzfeld2, zusatzfeld3, zusatzfeld4, zusatzfeld5, "+
    //						"gruppe, neuanlageDatumUhrzeit, geaendertDatumUhrzeit, manuellGeaendertSeitLetztemImport"+
    //						")"+
    //						"VALUES ("+
    //						/*omeldungsIdent.toString()+", "+*/
    //						omandant.toString()+", "+
    //						oklasse.toString()+", "+
    //						"'"+omeldungAktiv.toString()+"', ";
    //						if (VclMeldung.zutrittsIdent.compareTo("")==0){
    //							sql1=sql1+"NULL,";
    //						}
    //						else{
    //							sql1=sql1+"'"+VclMeldung.zutrittsIdent+"', ";
    //						}
    //						sql1=sql1+
    //						
    //						"'"+VclMeldung.externeIdent+"', "+
    //						omeldungstyp.toString()+", "+
    //						omeldungEnthaltenInSK.toString()+", "+
    //						"'"+VclMeldung.stimmkarte+"', "+
    //						"'"+VclMeldung.stimmkarteSecond+"', "+
    //						"'"+VclMeldung.stimmkarteVorab+"', "+
    //						"'"+VclMeldung.stimmkarteSecondVorab+"', "+
    //						ogattung.toString()+", "+
    //						ostueckAktien.toString()+", "+
    //						ostimmen.toString()+", "+
    //						ostueckAktienDruckEK.toString()+", "+
    //						ostimmenDruckEK.toString()+", "+
    //						"'"+VclMeldung.besitzart+"', "+
    //						"'"+VclMeldung.stimmausschluss+"', "+
    //						ostatusPraesenz.toString()+", "+
    //						oteilnahmeArt.toString()+", "+
    //						oanrede.toString()+", "+
    //						"'"+VclMeldung.titel+"', "+
    //						"'"+VclMeldung.adelstitel+"', "+
    //						"'"+VclMeldung.name+"', "+
    //						"'"+VclMeldung.vorname+"', "+
    //						"'"+VclMeldung.zuHdCo+"', "+
    //						"'"+VclMeldung.zusatz1+"', "+
    //						"'"+VclMeldung.zusatz2+"', "+
    //						"'"+VclMeldung.strasse+"', "+
    //						"'"+VclMeldung.land+"', "+
    //						"'"+VclMeldung.plz+"', "+
    //						"'"+VclMeldung.ort+"', "+
    //						"'"+VclMeldung.mailadresse+"', "+
    //						okommunikationssprache.toString()+", "+
    //						"'"+VclMeldung.gebDatum+"', "+
    //						"'"+VclMeldung.zusatzfeld1+"', "+
    //						"'"+VclMeldung.zusatzfeld2+"', "+
    //						"'"+VclMeldung.zusatzfeld3+"', "+
    //						"'"+VclMeldung.zusatzfeld4+"', "+
    //						"'"+VclMeldung.zusatzfeld5+"', "+
    //						ogruppe.toString()+", "+
    //						"'"+VclMeldung.neuanlageDatumUhrzeit+"', "+
    //						"'"+VclMeldung.geaendertDatumUhrzeit+"', "+
    //						omanuellGeaendertSeitLetztemImport.toString()+
    //						")";
    //				/*System.out.println("Bearbeiten SQL");*/
    //				/*System.out.println(sql1);*/
    //				/* ******************
    //				ergebnis2=stm1.executeUpdate(sql1);
    //				*/
    //				
    //				batchstatement.addBatch(sql1);
    //				
    //				/*System.out.println("Ergebnis Insert "+ergebnis2);*/
    //			} catch (Exception e2){
    //				System.err.println(" "+e2.getMessage());
    //			}
    //
    //			/*
    //			try {
    //				verbindung.commit();
    //				verbindung.setAutoCommit(true);
    //			} catch (Exception e2){
    //				System.err.println(" "+e2.getMessage());
    //			}
    //
    //			try {
    //				verbindung.setAutoCommit(true);
    //			} catch (Exception e2){
    //				System.err.println(" "+e2.getMessage());
    //			}
    //	*/
    //			return (1);
    //		}

    //		/** ****************************************************************************
    //		 * Einfügen eines neuen Satzes - für Gäste - nicht mehr verwenden!
    //		 * 
    //		 * Dabei: 
    //		 * > ggf. automatische Nummernvergabe der ZutrittsIdent (wenn zutrittsIdent=leer)
    //		 * > ggf. automatische Nummernvergabe von meldungsIdent (wenn meldungsIdent=0)
    //		 * 		Wenn <>0, dann muß auch nummernkreis entsprechend vorher geupdated worden sein!
    //		 * 
    //		 * Wichtig: die Felder klasse=0 und mandant, 
    //		 * werden mit dieser
    //		 * Funktion automatisch vorbelegt.
    //		 */
    //		@Deprecated
    //		public int insertGast(EclMeldung VclMeldung){
    //			
    //			VclMeldung.klasse=0;
    //			return insert(VclMeldung);
    //			
    //		}

    //		/** ****************************************************************************
    //		 * Einfügen eines neuen Satzes - für Gäste - nicht mehr verwenden!
    //		 * 
    //		 * Dabei: 
    //		 * > ggf. automatische Nummernvergabe der ZutrittsIdent (wenn zutrittsIdent=leer)
    //		 * > ggf. automatische Nummernvergabe von meldungsIdent (wenn meldungsIdent=0)
    //		 * 		Wenn <>0, dann muß auch nummernkreis entsprechend vorher geupdated worden sein!
    //		 * 
    //		 * Wichtig: mandant wird mit dieser Funktion automatisch vorbelegt.
    //		 */
    //		@Deprecated
    //		public int insert_old(EclMeldung VclMeldung){
    //			
    //			int erg=0;
    //			
    //			/* *****Vorabprüfungen durchführen ******* */
    //			/* zutrittsIdent */
    //			VclMeldung.zutrittsIdent.trim();
    //			if (!VclMeldung.zutrittsIdent.isEmpty()){
    //				switch (VclMeldung.klasse)
    //				{
    //					case 1:erg=lDbBasis.pruefeZutrittsIdentAktionaer(VclMeldung.zutrittsIdent);break;
    //					case 0:erg=lDbBasis.pruefeZutrittsIdentGast(VclMeldung.zutrittsIdent);break;
    //				}
    //				if (erg<1){return (erg);}
    //			}
    //			
    //			/* Start Transaktion */
    //			lDbBasis.beginTransaction();
    //
    //			/*Falls zutrittsIdent leer: neue zutrittsIdent vergeben*/
    //			if (VclMeldung.zutrittsIdent.isEmpty()){
    //				switch (VclMeldung.klasse)
    //				{
    //					case 1:erg=lDbBasis.getZutrittsIdentAktionaer();break;
    //					case 0:erg=lDbBasis.getZutrittsIdentGast();break;
    //				}
    //
    //				if (erg<1){
    //					lDbBasis.endTransaction();
    //					return (erg);
    //				}
    //				VclMeldung.zutrittsIdent=Integer.toString(erg);
    //			}
    //			else{
    //				/*zutrittsIdent nicht leer - d.h. prüfen, ob manuell vergebene höher als "letzte vergebene" ist
    //				 * ggf. updaten
    //				 */
    //				switch (VclMeldung.klasse)
    //				{
    //					case 1:erg=lDbBasis.updateLetzteZutrittsIdentAktionaer(VclMeldung.zutrittsIdent);break;
    //					case 0:erg=lDbBasis.updateLetzteZutrittsIdentGast(VclMeldung.zutrittsIdent);break;
    //				}
    //
    //				
    //			}
    //			
    //			if (VclMeldung.meldungsIdent==0){
    //				/* neue InterneIdent vergeben */
    //				erg=lDbBasis.getInterneIdentMeldungen();
    //				if (erg<1){
    //					lDbBasis.endTransaction();return (erg);
    //				}
    //				VclMeldung.meldungsIdent=erg;
    //			}
    //			
    //			/* VclMeldung einfügen */
    //			/* Verarbeitungshinweise: 
    //			 * 	>	nachdem InterneIdent immer eindeutig vergeben werden, ist prinzipiell eine "Doppeleinfügung"
    //			 * 		von InterneIdent nicht möglich
    //			 *  >	Aber: zutrittsIdent könnte bereits parallel vergeben worden sein - insofern muß diese
    //			 *  	abgefragt bzw. berücksichtigt werden (ggf. kein Neueinfügen möglich) 
    //			 */
    //			
    //			
    //			try {
    //				
    //				VclMeldung.neuanlageDatumUhrzeit=CaDatumZeit.DatumZeitStringFuerDatenbank();
    //				VclMeldung.db_version=0;
    //				
    //				/*Felder Neuanlage füllen*/
    ////				System.out.println("A");
    //				String sql1="INSERT INTO tbl_meldungen "+
    //						"("
    //						+ "meldungsIdent, db_version,  "
    //						+ "mandant, meldungAktiv, klasse, "
    //						+ "aktionaersnummer, externeIdent, "
    //						+ "meldungstyp, skMitWeisungen, skIst, skWeisungsartZulaessig, "
    //						
    //						+ "skBuchbarInternet, skBuchbarPapier, skBuchbarHV,"
    //						+ "stimmkarteVorab, stimmkarteSecondVorab, "
    //						+ "loginKennung, loginPasswort, "
    //						+ "gattung, stueckAktien, stimmen, "
    //				
    //						+ "stueckAktienDruckEK, stimmenDruckEk, "
    //						+ "besitzart, stimmausschluss, "
    //						+ "zusatzrechte, "
    //						+ "anrede, titel, adelstitel, "
    //						+ "name, vorname, "
    //						+ "zuHdCo, zusatz1, zusatz2, "
    //						+ "strasse, land, plz, ort, "
    //						+ "gebDatum, "
    //						+ "mailadresse, "
    //						+ "kommunikationssprache, "
    //	
    //						+ "zusatzfeld1, zusatzfeld2, zusatzfeld3, zusatzfeld4, zusatzfeld5, "
    //						+ "gruppe, "
    //						+ "neuanlageDatumUhrzeit,  "
    //						+ "geaendertDatumUhrzeit, "
    //						+ "manuellGeaendertSeitLetztemImport, "
    //						+ "delayedVorhanden, "
    //	
    //						+ "pendingVorhanden, "
    //						+ "zutrittsIdent, zutrittsIdent_Delayed, "
    //						+ "stimmkarte, stimmkarte_Delayed, "
    //						+ "stimmkarteSecond, stimmkarteSecond_Delayed, "
    //						+ "meldungEnthaltenInSammelkarte, meldungEnthaltenInSammelkarte_Delayed, "
    //						+ "meldungEnthaltenInSammelkarteArt, "
    //						
    //						+ "meldungEnthaltenInSammelkarteArt_Delayed, "
    //						+ "weisungVorhanden, weisungVorhanden_Delayed, "
    //						
    //						+ "vertreterName, vertreterName_Delayed, "
    //						+ "vertreterOrt, vertreterOrt_Delayed, "
    //						+ "willenserklaerungMitVollmacht, willenserklaerungMitVollmacht_Delayed, "
    //						+ "statusPraesenz, "
    //
    //						+ "statusPraesenz_Delayed, "
    //						+ "statusWarPraesenz, statusWarPraesenz_Delayed, "
    //						+ "teilnahmeArt "
    //						+ ")"+
    //						
    //						"VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,"
    //						+       "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?," 
    //						+       "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?," 
    //						+       "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
    //				PreparedStatement pstm1=verbindung.prepareStatement(sql1);
    //
    //				fuellePreparedStatementKomplett(pstm1,1,VclMeldung);
    //
    //				erg=0; /*Falls Duplicate Key, dann wird exception geschmissen und erg wohl nicht gefüllt!*/
    //				erg=pstm1.executeUpdate();
    //
    //			} catch (Exception e2){
    //				System.err.println(" "+e2.getMessage());
    //			}
    //			
    //
    //			if (erg==0){/*Fehler beim Einfügen - d.h. ZutrittsIdent bereits vorhanden*/
    //				lDbBasis.endTransaction();
    //				return CaFehler.pfdXyBereitsVorhanden;
    //			} 
    //			
    //			/* Änderungsprotokoll-Table füllen*/
    //			EclAenderungslog aenderungsLog=new EclAenderungslog();
    //			aenderungsLog.tabelle=EnAenderungslogTabelle.toEntity(EnAenderungslogTabelle.meldungen);
    //			aenderungsLog.ident=Integer.toString(VclMeldung.meldungsIdent);
    //			aenderungsLog.aktion=EnAenderungslogAktion.toEntity(EnAenderungslogAktion.neuaufnahme);
    //			aenderungsLog.feld="";
    //			aenderungsLog.alt="";
    //			aenderungsLog.neu="";
    //			lDbBundle.dbAenderungslog.insert(aenderungsLog);
    //
    //			
    //			/* Ende Transaktion */
    //			lDbBasis.endTransaction();
    //			
    //			
    //			return (1);
    //		}

    //		@Deprecated 
    //		public int batchinsertstart(){
    //			
    //			
    //			try {
    //
    //			batchstatement=verbindung.createStatement();
    //			} catch (Exception e2){
    //				System.err.println(" "+e2.getMessage());
    //			}
    //
    //			try {
    //			verbindung.setAutoCommit(false);
    //		} catch (Exception e2){
    //			System.err.println(" "+e2.getMessage());
    //		}
    //
    //			return (1);
    //		}
    //	
    //	
    //		@Deprecated 
    //		public int batchinsertend(){
    //			
    //			
    //			try {
    //
    //			batchstatement.executeBatch();
    //			} catch (Exception e2){
    //				System.err.println(" "+e2.getMessage());
    //			}
    //			try {
    //				verbindung.commit();
    //				verbindung.setAutoCommit(true);
    //			} catch (Exception e2){
    //				System.err.println(" "+e2.getMessage());
    //			}
    //
    //			return (1);
    //		}

    //		 /* Hinweis: OldVersion wird zwar noch gefüllt, aber nicht mehr für Update-Prüfung verwendet! Umgestellt auf
    //		  * db_version
    //		  */
    //			
    //			
    //			/** "Leert" OldVersion, z.B. für Neuaufnahme*/
    //			public void clearOldVersion(){
    //				VclMeldungOldVersion=new EclMeldung();
    //			}

}
