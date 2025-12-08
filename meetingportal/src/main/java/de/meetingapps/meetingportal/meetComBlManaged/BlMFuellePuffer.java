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
package de.meetingapps.meetingportal.meetComBlManaged;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;

import de.meetingapps.meetingportal.meetComAllg.CaBug;
import de.meetingapps.meetingportal.meetComAllg.CaDatumZeit;
import de.meetingapps.meetingportal.meetComAllg.CaFehler;
import de.meetingapps.meetingportal.meetComBVerwaltung.BvTexte;
import de.meetingapps.meetingportal.meetComBl.BlAbstimmung;
import de.meetingapps.meetingportal.meetComBl.BlAbstimmungenWeisungen;
import de.meetingapps.meetingportal.meetComEclM.EclDbM;
import de.meetingapps.meetingportal.meetComEntities.EclAbstimmungSet;
import de.meetingapps.meetingportal.meetComEntities.EclEmittenten;
import de.meetingapps.meetingportal.meetComEntities.EclFehler;
import de.meetingapps.meetingportal.meetComEntities.EclTermine;
import de.meetingapps.meetingportal.meetComEntities.EclUserLogin;
import de.meetingapps.meetingportal.meetComHVParam.HVParam;
import de.meetingapps.meetingportal.meetComHVParam.ParamGeraet;
import de.meetingapps.meetingportal.meetComHVParam.ParamKeys;
import de.meetingapps.meetingportal.meetComHVParam.ParamLogo;
import de.meetingapps.meetingportal.meetComKonst.KonstWeisungserfassungSicht;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@RequestScoped
@Named
public class BlMFuellePuffer {

	/**
	 * Auf true setzen => Das Laden und der Zugriff auf die Puffer wird im Systemlog
	 * protokolliert
	 */
	private int logDrucken = 3;

	@Inject
	BlMPuffer blmPuffer;
	@Inject
	EclDbM eclDbM;

	/** Liest paramServer aus Table, und trägt sie in den Parameter-Puffer ein */
	public void fuelleServerParameter(int pReloadVersion) {
		eclDbM.getDbBundle().dbParameter.readServer_all();
		blmPuffer.setParamServer(eclDbM.getDbBundle().dbParameter.ergParamServer, pReloadVersion);
		CaBug.druckeLog("BlMFuellePuffer.fuelleServerParameter - Server-Parameter neu eingelesen", logDrucken, 3);
	}

	/**
	 * Liest eclGeraeteSet aus Table, und trägt sie in den Parameter-Puffer ein.
	 * Returnwert 1 oder sysGeraeteSetNichtVorhanden
	 */
	public int fuelleEclGeraeteSet(int pReloadVersion) {
		eclDbM.getDbBundle().dbGeraeteSet.read(eclDbM.getDbBundle().paramServer.geraeteSetIdent);
		if (eclDbM.getDbBundle().dbGeraeteSet.anzErgebnis() != 1) {
			CaBug.drucke("BlMFuellePuffer.fuelleEclGeraeteSet 001 - Geräteset nicht vorhanden");
			return CaFehler.sysGeraeteSetNichtVorhanden;
		}
		blmPuffer.setEclGeraeteSet(eclDbM.getDbBundle().dbGeraeteSet.ergebnisPosition(0), pReloadVersion);
		CaBug.druckeLog("BlMFuellePuffer.fuelleEclGeraeteSet - Geräte-Set neu eingelesen", logDrucken, 3);
		return 1;
	}

	/**
	 * Liest EclGeraetKlasseSetZuordnung für alle Geräte des aktiven Gerätesets ein,
	 * und trägt diese in den Parameter-Puffer ein
	 */
	public void fuelleEclGeraetKlasseSetZuordnung() {
		eclDbM.getDbBundle().dbGeraeteKlasseSetZuordnung
				.readZuGeraeteSet_all(eclDbM.getDbBundle().paramServer.geraeteSetIdent);
		if (eclDbM.getDbBundle().dbGeraeteKlasseSetZuordnung.anzErgebnis() < 1) {
			CaBug.drucke("BlMFuellePuffer.fuelleEclGeraetKlasseSetZuordnung 001 - keine Gerätezuordnung zum Einlesen");
			return;
		}
		blmPuffer.setEclGeraetKlasseSetZuordnungStart();
		for (int i = 0; i < eclDbM.getDbBundle().dbGeraeteKlasseSetZuordnung.anzErgebnis(); i++) {
			blmPuffer.addEclGeraetKlasseSetZuordnung(eclDbM.getDbBundle().dbGeraeteKlasseSetZuordnung.ergebnisArray[i]);
		}
		blmPuffer.setEclGeraetKlasseSetZuordnungEnd();
		CaBug.druckeLog(
				"BlMFuellePuffer.fuelleEclGeraetKlasseSetZuordnung - Geräte-Set-Klasse-Zuordnung neu eingelesen",
				logDrucken, 3);
	}

	/**
	 * Liest ParamGeraet für alle Klassen des aktiven Gerätesets ein, und trägt
	 * diese in den Parameter-Puffer ein
	 */
	public void fuelleParamGeraet() {
		eclDbM.getDbBundle().dbGeraeteKlasseSetZuordnung
				.readZuGeraeteSet_all_nurKlasseIdentUnique(eclDbM.getDbBundle().paramServer.geraeteSetIdent);
		if (eclDbM.getDbBundle().dbGeraeteKlasseSetZuordnung.anzErgebnis() < 1) {
			CaBug.drucke("BlMFuellePuffer.fuelleParamGeraet 001 - keine Klassen zum Einlesen");
			return;
		}
		blmPuffer.setParamGeraetStart();
		for (int i = 0; i < eclDbM.getDbBundle().dbGeraeteKlasseSetZuordnung.anzErgebnis(); i++) {
			ParamGeraet hParamGeraet = null;
			int geraeteKlasse = eclDbM.getDbBundle().dbGeraeteKlasseSetZuordnung.ergebnisArray[i].geraeteKlasseIdent;
			eclDbM.getDbBundle().dbParameter.readGerateKlasse_all(geraeteKlasse);
			hParamGeraet = eclDbM.getDbBundle().dbParameter.ergParamGeraet;
			hParamGeraet.serverArt = blmPuffer.getParamServer().serverArt;
			hParamGeraet.serverBezeichnung = blmPuffer.getParamServer().serverBezeichnung;
			if (hParamGeraet.festgelegterMandant > 0) {
				eclDbM.getDbBundle().dbEmittenten.readMandantenbezeichnung(hParamGeraet.festgelegterMandant);
				if (eclDbM.getDbBundle().dbEmittenten.anzErgebnis() > 0) {
					hParamGeraet.festgelegterMandantText = eclDbM.getDbBundle().dbEmittenten
							.ergebnisPosition(0).bezeichnungKurz;
				}
			}
			blmPuffer.addParamGeraet(hParamGeraet);
		}
		blmPuffer.setParamGeraetEnd();
		CaBug.druckeLog("BlMFuellePuffer.fuelleParamGeraet - Geräte-Klassen neu eingelesen", logDrucken, 3);
	}

	/**
	 * Liest HVParam für aktuellen Mandanten ein, und trägt diese in den
	 * Parameter-Puffer ein
	 */
	public void fuelleMandantenParam(int pReloadParameter) {
		HVParam lHVParam = null;

		/* HVParam */
		eclDbM.getDbBundle().dbParameter.readHVParam_all();
		lHVParam = eclDbM.getDbBundle().dbParameter.ergHVParam;
		lHVParam.mandant = eclDbM.getDbBundle().clGlobalVar.mandant;
		lHVParam.hvJahr = eclDbM.getDbBundle().clGlobalVar.hvJahr;
		lHVParam.hvNummer = eclDbM.getDbBundle().clGlobalVar.hvNummer;
		lHVParam.datenbereich = eclDbM.getDbBundle().clGlobalVar.datenbereich;

		/* Nun Menü-Struktur und und Kontaktformular-Themen ergänzen */
		CaBug.druckeLog("lHVParam.paramPortal.registerAnbindungOberflaeche="+lHVParam.paramPortal.registerAnbindungOberflaeche, logDrucken, 10);
		if (lHVParam.paramPortal.registerAnbindungOberflaeche != 0) {
			lHVParam.paramPortal.menueListe = eclDbM.getDbBundle().dbMenueEintrag.readMenue();
			CaBug.druckeLog("lHVParam.paramPortal.menueListe==null="+(lHVParam.paramPortal.menueListe==null),logDrucken, 10);
			lHVParam.paramPortal.kontaktformularThemenListe = eclDbM.getDbBundle().dbKontaktformularThema
					.readThemenliste();
		}

		/* Nun Phasen und Portalfunktionen durch Zeitangaben in Terminen ergänzen */
		CaBug.druckeLog("Phasen und Termine um Zeitstamps ergänzen", logDrucken, 10);
		eclDbM.getDbBundle().dbTermine.readAll_technisch(0);
		EclTermine[] lTerminArray = eclDbM.getDbBundle().dbTermine.ergebnisArray;
		if (lTerminArray != null) {
			CaBug.druckeLog("lTerminArray!=null", logDrucken, 10);
			for (int i = 0; i < lTerminArray.length; i++) {
				int lIdent = lTerminArray[i].identTermin;
				
				String datumZeit = CaDatumZeit.datumJJJJMMTTzuNormal(lTerminArray[i].terminDatum) + " "
						+ lTerminArray[i].terminZeit;
				long zeitStamp = 0;
				if (datumZeit.length() != 1) {
					zeitStamp = CaDatumZeit.DatumZeitStringToLong(datumZeit);
				}
				if (lIdent >= 101 && lIdent <= 120) {
					/* PortalPhase */
					lHVParam.paramPortal.eclPortalPhase[lIdent - 100].offenVon = zeitStamp;
				}
				if (lIdent >= 121 && lIdent <= 152) {
					int hlIdent = lIdent - 120;
					int hOffset = hlIdent / 2;
					int hRest = hlIdent % 2;
					hOffset += hRest;
					if (hRest == 1) {
						lHVParam.paramPortal.eclPortalFunktion[hOffset].offenVon = zeitStamp;
					} else {
						lHVParam.paramPortal.eclPortalFunktion[hOffset].offenBis = zeitStamp;
					}
					CaBug.druckeLog(
							"lIdent=" + lIdent + " hOffset=" + hOffset + " hRest=" + hRest + "zeitStamp=" + zeitStamp,
							logDrucken, 10);
				}
			}
		}

		/* +++++Logo einlesen++++++++++ */

		String filename = lHVParam.paramPortal.logoName;
		CaBug.druckeLog("Logo Filename=" + filename, logDrucken, 10);

		File file = new File(eclDbM.getDbBundle().lieferePfadMeetingReports() + "//", filename);

		byte[] logoPortalAusFile = null;
		try {
			logoPortalAusFile = Files.readAllBytes(file.toPath());
		} catch (IOException e) {
			CaBug.druckeInfo("001 - Logo-File nicht gefunden " + eclDbM.getDbBundle().lieferePfadMeetingReports() + " "
					+ filename);
		}

		if (logoPortalAusFile==null) {
	        file = new File(eclDbM.getDbBundle().lieferePfadMeetingReportsOhneSubVerzeichnis() + "//", "logoFehlt.jpg");

	        logoPortalAusFile = null;
	        try {
	            logoPortalAusFile = Files.readAllBytes(file.toPath());
	        } catch (IOException e) {
	            CaBug.drucke("002 - logoFehlt-Pfad nicht gefunden");
	        }
		}
		ParamLogo paramLogo = new ParamLogo();
		paramLogo.logoPortal = logoPortalAusFile;
		paramLogo.logoPortalDateiname = filename;
		paramLogo.logoPortalLength = String.valueOf(file.length());
		paramLogo.logoPortalDisposition = "inline; filename=\"" + filename + "\"";

		/* ++++++++++++private Key einlesen+++++++++++++++++++++ */
		ParamKeys paramKeys = new ParamKeys();
		if (lHVParam.paramPortal.registerAnbindung == 1) {
			/* ku178 / GenossenschaftSys */
			PrivateKey lPrivateKey = null;
			String filenamePK = lHVParam.paramPortal.api_key_name;
			CaBug.druckeLog("PrivateKey Filename=" + filenamePK, logDrucken, 10);

			File filePK = new File(eclDbM.getDbBundle().lieferePfadMeetingReports() + "//", filenamePK);

			String key = null;
			try {
				key = new String(Files.readAllBytes(filePK.toPath()), Charset.defaultCharset());
			} catch (IOException e) {
				CaBug.drucke("Fehler 001 in PrivateKey Lesen");
				e.printStackTrace();
			}

			if (key == null || key.length() == 0) {
				CaBug.drucke("Fehler 004 in PrivateKey Lesen");
			} else {
				String privateKeyPEM = key.replace("-----BEGIN RSA PRIVATE KEY-----", "")
						.replace("-----BEGIN PRIVATE KEY-----", "").replaceAll(System.lineSeparator(), "")
						.replace("-----END RSA PRIVATE KEY-----", "").replace("-----END PRIVATE KEY-----", "");

				byte[] pkcs8EncodedBytes = Base64.getDecoder().decode(privateKeyPEM);

				PKCS8EncodedKeySpec ks = new PKCS8EncodedKeySpec(pkcs8EncodedBytes);
				KeyFactory kf = null;
				try {
					kf = KeyFactory.getInstance("RSA");
				} catch (NoSuchAlgorithmException e) {
					CaBug.drucke("Fehler 002 in PrivateKey Lesen");
					e.printStackTrace();
				}
				try {
					lPrivateKey = kf.generatePrivate(ks);
				} catch (InvalidKeySpecException e) {
					CaBug.drucke("Fehler 003 in PrivateKey Lesen");
					e.printStackTrace();
				}
				paramKeys.privateKeyGenossenschaftSys = lPrivateKey;
			}
		}

		blmPuffer.addOderReplaceHVParam(lHVParam, paramLogo, paramKeys, pReloadParameter);
		CaBug.druckeLog("BlMFuellePuffer.fuelleMandantenParam - HV-Parameter Mandant "
				+ eclDbM.getDbBundle().clGlobalVar.mandant + " neu eingelesen", logDrucken, 3);
	}

	/**
	 * Liest HVParam für aktuellen Mandanten ein, und trägt diese in den
	 * Parameter-Puffer ein
	 */
	public void fuelleMandantenTexte(int pReloadTexte) {
		EclFehler lFehlerDeutschArray[] = null;
		EclFehler lFehlerEnglischArray[] = null;
		String[] lPortalTexteDEArray = null;
		String[] lPortalTexteENArray = null;
		String[] lPortalTexteAdaptivDEArray = null;
		String[] lPortalTexteAdaptivENArray = null;

		if (eclDbM.getDbBundle().clGlobalVar.mandant == 0) {
			return;
		}

		CaBug.druckeLog("mandantentexte werden aus Datei gelesen", logDrucken, 3);
		/* Fehlermeldung */
		eclDbM.getDbBundle().dbFehler.read_all("DE");
		lFehlerDeutschArray = eclDbM.getDbBundle().dbFehler.ergebnisArray;

		eclDbM.getDbBundle().dbFehler.read_all("EN");
		lFehlerEnglischArray = eclDbM.getDbBundle().dbFehler.ergebnisArray;

		/* Texte */
		BvTexte bvTexte = new BvTexte();
		bvTexte.lesePortalTexteFuerAnzeige(eclDbM.getDbBundle(), false, 1);
		lPortalTexteDEArray = bvTexte.rcPortalTexte;
		lPortalTexteAdaptivDEArray = bvTexte.rcPortalAdaptivTexte;
		if ((eclDbM.getDbBundle().eclEmittent.portalSprache & 2) == 2) {
			bvTexte.lesePortalTexteFuerAnzeige(eclDbM.getDbBundle(), false, 2);
			lPortalTexteENArray = bvTexte.rcPortalTexte;
			lPortalTexteAdaptivENArray = bvTexte.rcPortalAdaptivTexte;
		}

		blmPuffer.addOderReplaceHVTexte(eclDbM.getDbBundle().clGlobalVar, lFehlerDeutschArray, lFehlerEnglischArray,
				lPortalTexteDEArray, lPortalTexteAdaptivDEArray, lPortalTexteENArray, lPortalTexteAdaptivENArray,
				pReloadTexte);
		CaBug.druckeLog("BlMFuellePuffer.fuelleMandantenTexte - Fülle MandantenTexte Mandant "
				+ eclDbM.getDbBundle().clGlobalVar.mandant + " neu eingelesen", logDrucken, 3);
	}

	public void fuelleTechnischeTermine(int pReloadTermine) {

		/* Technische Termine */
		eclDbM.getDbBundle().dbTermine.readAll_technisch(0);
		blmPuffer.addOderReplaceTechnischeTermine(eclDbM.getDbBundle().clGlobalVar,
				eclDbM.getDbBundle().dbTermine.ergebnisArray, pReloadTermine);
		CaBug.druckeLog("BlMFuellePuffer.fuelleTechnischeTermine - Fülle TechnischeTermine Mandant "
				+ eclDbM.getDbBundle().clGlobalVar.mandant + " neu eingelesen", logDrucken, 3);
	}

	public void fuelleAbstimmungSet(int pReloadWeisungen, int pReloadAbstimmungen,
			int pReloaAbstimmungenWeisungenOhneAbbruch) {

		EclAbstimmungSet lAbstimmungSet = new EclAbstimmungSet();

		BlAbstimmungenWeisungen blAbstimmungenWeisungen = new BlAbstimmungenWeisungen(true, eclDbM.getDbBundle());
		/* Alle aktiven für Portal einlesen */
		blAbstimmungenWeisungen.leseAgendaWeisungen(KonstWeisungserfassungSicht.portalWeisungserfassung,
				eclDbM.getDbBundle().param.paramAbstimmungParameter.weisungenGegenantraegePortalSeparat, 1);
		lAbstimmungSet.agendaArrayPortalWeisungserfassung = blAbstimmungenWeisungen.rcAgendaArray;
		lAbstimmungSet.gegenantraegeArrayPortalWeisungserfassung = blAbstimmungenWeisungen.rcGegenantraegeArray;

		/* Alle Preview für Portal einlesen */
		blAbstimmungenWeisungen.leseAgendaWeisungen(KonstWeisungserfassungSicht.portalWeisungserfassung,
				eclDbM.getDbBundle().param.paramAbstimmungParameter.weisungenGegenantraegePortalSeparat, 2);
		lAbstimmungSet.agendaArrayPortalWeisungserfassungPreview = blAbstimmungenWeisungen.rcAgendaArray;
		lAbstimmungSet.gegenantraegeArrayPortalWeisungserfassungPreview = blAbstimmungenWeisungen.rcGegenantraegeArray;

		/* Alle aktiven für interne Weisungserfassung einlesen */
		blAbstimmungenWeisungen = new BlAbstimmungenWeisungen(true, eclDbM.getDbBundle());
		blAbstimmungenWeisungen.leseAgendaWeisungen(KonstWeisungserfassungSicht.interneWeisungserfassung,
				eclDbM.getDbBundle().param.paramAbstimmungParameter.weisungenGegenantraegePortalSeparat, 1);
		lAbstimmungSet.agendaArrayInternWeisungserfassung = blAbstimmungenWeisungen.rcAgendaArray;
		lAbstimmungSet.gegenantraegeArrayInternWeisungserfassung = blAbstimmungenWeisungen.rcGegenantraegeArray;

		BlAbstimmung blAbstimmung = new BlAbstimmung(eclDbM.getDbBundle());
		blAbstimmung.setzeAktivenAbstimmungsblockSortierenNachAufTable();
		blAbstimmung.leseAktivenAbstimmungsblock();
		lAbstimmungSet.aktivenAbstimmungsblockSortierenNach = blAbstimmung.aktivenAbstimmungsblockSortierenNach;
		lAbstimmungSet.aktiverAbstimmungsblock = blAbstimmung.aktiverAbstimmungsblock;
		lAbstimmungSet.aktiverAbstimmungsblockIstElektronischAktiv = blAbstimmung.aktiverAbstimmungsblockIstElektronischAktiv;
		lAbstimmungSet.abstimmungenZuAktivenBlock = blAbstimmung.abstimmungenZuAktivenBlock;
		lAbstimmungSet.abstimmungen = blAbstimmung.abstimmungen;

		blmPuffer.addOderReplaceAbstimmungen(eclDbM.getDbBundle().clGlobalVar, lAbstimmungSet, pReloadWeisungen,
				pReloadAbstimmungen, pReloaAbstimmungenWeisungenOhneAbbruch);
		CaBug.druckeLog(" Mandant " + eclDbM.getDbBundle().clGlobalVar.mandant + " neu eingelesen", logDrucken, 3);
	}

	/**
	 * Liest Emittenten-Liste ein, und trägt diese in den Parameter-Puffer ein
	 */
	public void fuelleEmittentenArray(int pReloadVersion) {
		EclEmittenten[] lEmittentenArray = null;

		eclDbM.getDbBundle().dbEmittenten.readAll(0);
		lEmittentenArray = eclDbM.getDbBundle().dbEmittenten.ergebnisArray;

		blmPuffer.setEmittentenArray(lEmittentenArray, pReloadVersion);
		CaBug.druckeLog("BlMFuellePuffer.fuelleEmittentenArray pReloadVersion=" + pReloadVersion
				+ " - Emittenten neu eingelesen", logDrucken, 3);
	}

	/**
	 * Liest HVParam für aktuellen Mandanten ein, und trägt diese in den
	 * Parameter-Puffer ein
	 */
	public void fuelleUserLogin(int pBenutzerNummer, int pReloadUserLogin) {
		EclUserLogin lUserLogin = null;

		eclDbM.getDbBundle().dbUserLogin.leseZuIdent(pBenutzerNummer);
		lUserLogin = eclDbM.getDbBundle().dbUserLogin.userLoginArray[0];

		blmPuffer.addOderReplaceEclUserLogin(lUserLogin, pReloadUserLogin);
		CaBug.druckeLog("BlMFuellePuffer.fuelleUserLogin - EclUserLogin " + pReloadUserLogin + " neu eingelesen",
				logDrucken, 32);
	}

}
