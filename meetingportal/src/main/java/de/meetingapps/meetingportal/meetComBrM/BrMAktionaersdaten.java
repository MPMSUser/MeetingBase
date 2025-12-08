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
package de.meetingapps.meetingportal.meetComBrM;

import java.util.List;

import de.meetingapps.meetingportal.meetComAllg.CaBank;
import de.meetingapps.meetingportal.meetComAllg.CaBug;
import de.meetingapps.meetingportal.meetComAllg.CaDatumZeit;
import de.meetingapps.meetingportal.meetComAllg.CaFehler;
import de.meetingapps.meetingportal.meetComAllg.CaString;
import de.meetingapps.meetingportal.meetComBa.BaMailM;
import de.meetingapps.meetingportal.meetComBl.BlAuftragAnbindungAktienregister;
import de.meetingapps.meetingportal.meetComBl.BlNummernformBasis;
import de.meetingapps.meetingportal.meetComBr.BrLiefereMitgliedArt;
import de.meetingapps.meetingportal.meetComBr.BrLieferePersonenListe;
import de.meetingapps.meetingportal.meetComEclM.EclDbM;
import de.meetingapps.meetingportal.meetComEclM.EclLoginDatenM;
import de.meetingapps.meetingportal.meetComEclM.EclPortalTexteM;
import de.meetingapps.meetingportal.meetComEntities.EclAktienregisterWeiterePerson;
import de.meetingapps.meetingportal.meetComEntities.EclLoginDaten;
import de.meetingapps.meetingportal.meetComEntities.EclStaaten;
import de.meetingapps.meetingportal.meetComEntitiesGenossenschaftSys.EgxGetPersoenlicheDatenRC;
import de.meetingapps.meetingportal.meetComEntitiesGenossenschaftSys.EgxGetPersonendatenRC;
import de.meetingapps.meetingportal.meetComEntitiesGenossenschaftSys.EgxPostAdressaenderung;
import de.meetingapps.meetingportal.meetComEntitiesGenossenschaftSys.EgxPostAdressaenderungRC;
import de.meetingapps.meetingportal.meetComEntitiesGenossenschaftSys.EgxPostBankverbindung;
import de.meetingapps.meetingportal.meetComEntitiesGenossenschaftSys.EgxPostBankverbindungRC;
import de.meetingapps.meetingportal.meetComEntitiesGenossenschaftSys.EgxPostEmailaenderung;
import de.meetingapps.meetingportal.meetComEntitiesGenossenschaftSys.EgxPostEmailaenderungRC;
import de.meetingapps.meetingportal.meetComEntitiesGenossenschaftSys.EgxPostGeburtsdatum;
import de.meetingapps.meetingportal.meetComEntitiesGenossenschaftSys.EgxPostGeburtsdatumRC;
import de.meetingapps.meetingportal.meetComEntitiesGenossenschaftSys.EgxPostIban;
import de.meetingapps.meetingportal.meetComEntitiesGenossenschaftSys.EgxPostIbanRC;
import de.meetingapps.meetingportal.meetComEntitiesGenossenschaftSys.EgxPostSteuerid;
import de.meetingapps.meetingportal.meetComEntitiesGenossenschaftSys.EgxPostSteueridRC;
import de.meetingapps.meetingportal.meetComEntitiesGenossenschaftSys.EgxPostTelefon;
import de.meetingapps.meetingportal.meetComEntitiesGenossenschaftSys.EgxPostTelefonRC;
import de.meetingapps.meetingportal.meetComKonst.KonstAuftragArt;
import de.meetingapps.meetingportal.meetComKonst.KonstAuftragModul;
import de.meetingapps.meetingportal.meetComKonst.KonstGruppen;
import de.meetingapps.meetingportal.meetingportTController.PAktionaersdatenSession;
import de.meetingapps.meetingportal.meetingportTController.TLinkSession;
import de.meetingapps.meetingportal.meetingportTController.TPermanentSession;
import de.meetingapps.meetingportal.meetingportTController.TSession;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@RequestScoped
@Named
public class BrMAktionaersdaten {

	private int logDrucken = 10;

	private @Inject EclDbM eclDbM;
	private @Inject TSession tSession;
	private @Inject TPermanentSession tPermanentSession;
	private @Inject PAktionaersdatenSession pAktionaersdatenSession;
	private @Inject EclLoginDatenM eclLoginDatenM;
	private @Inject BrMGenossenschaftCall brMGenossenschaftCall;
    private @Inject BrMAuftraege brMAuftraege;

    private @Inject EclPortalTexteM eclTextePortalM;
    private @Inject TLinkSession tLinkSession;
    private @Inject BaMailM baMailm;

    /**eclDbM muß geöffnet sein.
     * 
     * pBasisAktionaersdatenErneutVonRegisterHolen==true =>
     * brMGenossenschaftCall.doGetRequestPersoenlicheDaten ist bereits erfolgt, muß also hier nicht wiederholt werden
     * */
	public int holeAktuellenStand(String pAktionaersnummer) {
	    CaBug.druckeLog("", logDrucken, 10);
	    
		/* Aktionärsnummer aufbereiten */
		pAktionaersdatenSession.clear();
		BlNummernformBasis blNummernformBasis = new BlNummernformBasis(eclDbM.getDbBundle());
		String aktionaersnummerFuerRegister = blNummernformBasis
				.aufbereitenKennungFuerRegisterzugriff(pAktionaersnummer);
		String aktionaersnummerFuerGenossenschaftSysWebrequest = blNummernformBasis
				.aufbereitenKennungFuerGenossenschaftSysWebrequest(pAktionaersnummer);
		CaBug.druckeLog(pAktionaersnummer, logDrucken, 10);
        CaBug.druckeLog(aktionaersnummerFuerRegister, logDrucken, 10);
		CaBug.druckeLog(aktionaersnummerFuerGenossenschaftSysWebrequest, logDrucken, 10);

		if (tPermanentSession.isTestModus()) {
		    /*Hier zu Testdaten umsetzen*/

		} else {

			/*
			 * Persönliche Daten füllen
			 */
	        EgxGetPersoenlicheDatenRC egxGetPersoenlicheDatenRC = brMGenossenschaftCall.doGetRequestPersoenlicheDaten(aktionaersnummerFuerGenossenschaftSysWebrequest);
            if (egxGetPersoenlicheDatenRC==null) {
                return CaFehler.perRemoteAktienregisterNichtVerfuegbar;
            }
            pAktionaersdatenSession.setAktionaerSeit(CaDatumZeit.datumJJJJ_MM_TTzuNormal(egxGetPersoenlicheDatenRC.kundeseit));
            pAktionaersdatenSession.setAktionaerAnrede(egxGetPersoenlicheDatenRC.adr_anrede);
            pAktionaersdatenSession.setAktionaerNachname(egxGetPersoenlicheDatenRC.nachname);
            pAktionaersdatenSession.setAktionaerVorname(egxGetPersoenlicheDatenRC.vorname);
            pAktionaersdatenSession.setAktionaerAdresszusatz(egxGetPersoenlicheDatenRC.adr_adresszusatz);
            pAktionaersdatenSession.setAktionaerStrasse(egxGetPersoenlicheDatenRC.adr_str);
            
            String hPlz=egxGetPersoenlicheDatenRC.adr_plz;

            if (egxGetPersoenlicheDatenRC.adr_landname.compareTo("Deutschland")==0 || egxGetPersoenlicheDatenRC.adr_landname.trim().isEmpty()) {
                hPlz=CaString.fuelleLinksNull(hPlz, 5);
            }
            pAktionaersdatenSession.setAktionaerPLZ(hPlz);
            
			pAktionaersdatenSession.setAktionaerOrt(egxGetPersoenlicheDatenRC.adr_ort);
			pAktionaersdatenSession.setAktionaerLand(egxGetPersoenlicheDatenRC.adr_landname);
			pAktionaersdatenSession.setAktionaerTelefon1(egxGetPersoenlicheDatenRC.telefon_privat); // telefon_privat
			pAktionaersdatenSession.setAktionaerTelefon2(egxGetPersoenlicheDatenRC.telefon_geschaeftlich); // telefon_geschaeftlich
			pAktionaersdatenSession.setAktionaerTelefon3(egxGetPersoenlicheDatenRC.mobiltelefon_privat); // telefon_mobil
			pAktionaersdatenSession.setAktionaerTelefon4(egxGetPersoenlicheDatenRC.mobiltelefon_geschaeftlich); // telefon_mobil_geschaeftlich
			
			String aktienregisterEMail=egxGetPersoenlicheDatenRC.email.trim();
			String portalEmail=eclLoginDatenM.getEclLoginDaten().eMailFuerVersand.trim();
			
			/*Für GeDix gilt: egal was von GeDix kommt: E-Mail-Adresse wird immer als Bestätigt
			 * genommen und deshalb die von eclLogin genommen. Es wird nur "auf abweichend" gesetzt,
			 * wenn GeDix-Mail und Portal-Mail unterschiedlich sind, und noch keinerlei Änderungsaufträge
			 * vorliegen (kann bei Erstregistrierung passieren!)
			 */
			pAktionaersdatenSession.setAktionaerEmail(aktienregisterEMail);
 			pAktionaersdatenSession.setAktionaerEmailPortal(portalEmail);
			if ((!aktienregisterEMail.isEmpty()) && (!portalEmail.isEmpty())) {
			    if (!aktienregisterEMail.equalsIgnoreCase(portalEmail)) {
			        pAktionaersdatenSession.setEmailWeichtAb(true);
			    }
			    else {
                    pAktionaersdatenSession.setEmailWeichtAb(false);
			    }
			}
			
			eclDbM.getDbBundle().dbAuftrag.read_auftraegeVorhanden(KonstAuftragModul.ANBINDUNG_AKTIENREGISTER, KonstAuftragArt.ANBINDUNG_AKTIENREGISTER_AENDERN_MEINEDATEN_EMAIL);
			CaBug.druckeLog("eclDbM.getDbBundle().dbAuftrag.anzErgebnis()="+eclDbM.getDbBundle().dbAuftrag.anzErgebnis(), logDrucken, 10);
            if (eclDbM.getDbBundle().dbAuftrag.anzErgebnis()!=0) {
                pAktionaersdatenSession.setAktionaerEmail(portalEmail);
                pAktionaersdatenSession.setEmailWeichtAb(false);
            }
			

			pAktionaersdatenSession.setBankAktionaersname(egxGetPersoenlicheDatenRC.namegut);
			pAktionaersdatenSession.setBankBankname(egxGetPersoenlicheDatenRC.banknamegut);
			pAktionaersdatenSession.setBankIban(CaBank.ibanFuerAnzeige(egxGetPersoenlicheDatenRC.ibangut));
			pAktionaersdatenSession.setBankBic(egxGetPersoenlicheDatenRC.bicgut);

			if (egxGetPersoenlicheDatenRC.ibangut.trim().isEmpty()) {
			    pAktionaersdatenSession.setBankverbindungFehlt(true);
			}
			/*
			 * Personendaten füllen
			 */

			EgxGetPersonendatenRC egxGetPersonendatenRC = brMGenossenschaftCall
					.doGetRequestPersonendaten(aktionaersnummerFuerGenossenschaftSysWebrequest);
	        if (egxGetPersonendatenRC==null) {
	            return CaFehler.perRemoteAktienregisterNichtVerfuegbar;
	        }

			if (egxGetPersonendatenRC.result.size() > 0) {
			    BrLieferePersonenListe brLieferePersonenListe=new BrLieferePersonenListe();
			    brLieferePersonenListe.liefereAusGeDixListe(egxGetPersonendatenRC);
                pAktionaersdatenSession.setZugeordnetePersonenVorhanden(brLieferePersonenListe.rcZugeordnetePersonenVorhanden);
                pAktionaersdatenSession.setZugeordnetePersonVorhanden(brLieferePersonenListe.rcZugeordnetePersonVorhanden);
                pAktionaersdatenSession.setZugeordneteWeiterPersonen(brLieferePersonenListe.rcZugeordneteWeiterPersonen);
                
                pAktionaersdatenSession.setSteuerIdDarfGeaendertWerden(brLieferePersonenListe.rcSteuerIdDarfGeaendertWerden);
                pAktionaersdatenSession.setSteuerIdFehlt(brLieferePersonenListe.rcSteuerIdFehlt);
                
                pAktionaersdatenSession.setGeburtsdatumDarfGeaendertWerden(brLieferePersonenListe.rcGeburtsdatumDarfGeaendertWerden);
                pAktionaersdatenSession.setGeburtsdatumFehlt(brLieferePersonenListe.rcGeburtsdatumFehlt);

                pAktionaersdatenSession.setVollmachtenPostempfaengerVorhanden(brLieferePersonenListe.rcVollmachtenPostempfaengerVorhanden);
                pAktionaersdatenSession.setVollmachtenPostempfaenger(brLieferePersonenListe.rcVollmachtenPostempfaenger);
                
                pAktionaersdatenSession.setPostempfaengerVorhanden(brLieferePersonenListe.rcPostempfaengerVorhanden);
                pAktionaersdatenSession.setPostempfaenger(brLieferePersonenListe.rcPostempfaenger);

			}

			/*Status Aufträge einlesen*/
			brMAuftraege.setzeOberflaechenStatusLtAuftraege(eclLoginDatenM.getEclLoginDaten().ident*(-1));

			/*Änderungskriterien eintragen*/
			int personenArt=holeMitgliedsart();
			switch (personenArt) {
            case KonstGruppen.firmen:pAktionaersdatenSession.setStatusFirma(true);break;
            case KonstGruppen.erbengemeinschaft:pAktionaersdatenSession.setStatusErbengemeinschaft(true);break;
            case KonstGruppen.minderjaehrigesEinzelmitglied:pAktionaersdatenSession.setStatusMinderjaehrig(true);break;
            case KonstGruppen.eheleuteGesamthans:pAktionaersdatenSession.setStatusGesamthans(true);break;
            case KonstGruppen.eheleuteGbR:pAktionaersdatenSession.setStatusEheleuteGbr(true);break;
			case KonstGruppen.einzelmitglied:pAktionaersdatenSession.setStatusNormalesMitglied(true);break;
			}
			
			if (pAktionaersdatenSession.getPostempfaenger()!=null && pAktionaersdatenSession.getPostempfaenger().size()>0) {
			    pAktionaersdatenSession.setPostempfaengerVorhanden(true);
			}
			else {
                pAktionaersdatenSession.setPostempfaengerVorhanden(false);
			}
//			if (personenArt==KonstGruppen.erbengemeinschaft || personenArt==KonstGruppen.firmen) {
//			    pAktionaersdatenSession.setGeburtsdatumAendernZulaessig(false);
//			}
//			else {
//                pAktionaersdatenSession.setGeburtsdatumAendernZulaessig(true);
//			}
			
		}

		return 1;
	}

	public int anschriftAendernVorbereiten(String pAktionaersnummer) {
		pAktionaersdatenSession.setAendAdresszusatz(pAktionaersdatenSession.getAktionaerAdresszusatz());
		pAktionaersdatenSession.setAendStrasse(pAktionaersdatenSession.getAktionaerStrasse());
		pAktionaersdatenSession.setAendPLZ(pAktionaersdatenSession.getAktionaerPLZ());
		pAktionaersdatenSession.setAendOrt(pAktionaersdatenSession.getAktionaerOrt());
		pAktionaersdatenSession.setAendLand(pAktionaersdatenSession.getAktionaerLand());

		List<EclStaaten> lStaatenListe = pAktionaersdatenSession.getAendLandListe();
		String aktuellesLand = pAktionaersdatenSession.getAktionaerLand();
		if (aktuellesLand.trim().isEmpty()) {
			aktuellesLand = "Deutschland";
		}
		int gef = -1;
		for (EclStaaten iEclStaaten : lStaatenListe) {
			if (iEclStaaten.nameDE.compareToIgnoreCase(aktuellesLand) == 0) {
				gef = iEclStaaten.id;
			}
		}
		pAktionaersdatenSession.setAendLandNummer(gef);
		pAktionaersdatenSession.setAktionaerLandNummer(gef);
		return 1;
	}

	/*
	 * Return ==1 => ok, wurde gespeichert, normaler Hinweis ==2 => ok, wurde
	 * gespeichert, separater Hinweis für juristische Personen etc.
	 * 
	 * ==3 => ok, aber keine Änderungen, d.h. nichts gespeichert
	 * 
	 * ==0 => Fehlermeldung wurde erzeugt, Kein Speichern erfolgt, Änderungsfenster
	 * muß angezeigt bleiben
	 */
	public int anschriftAendernSpeichern(String pAktionaersnummer) {
		BlNummernformBasis blNummernformBasis = new BlNummernformBasis(eclDbM.getDbBundle());
		String aktionaersnummerFuerGenossenschaftSysWebrequest = blNummernformBasis
				.aufbereitenKennungFuerGenossenschaftSysWebrequest(pAktionaersnummer);
		/*****************
		 * Checken, ob überhaupt Änderung vorhanden
		 **********************/
		boolean unterschiedGefunden = false;
		if (!pAktionaersdatenSession.getAendAdresszusatz().trim()
				.equals(pAktionaersdatenSession.getAktionaerAdresszusatz().trim())) {
			unterschiedGefunden = true;
			CaBug.druckeLog("Adresszusatz", logDrucken, 10);
		}
		if (!pAktionaersdatenSession.getAendStrasse().trim()
				.equals(pAktionaersdatenSession.getAktionaerStrasse().trim())) {
			unterschiedGefunden = true;
			CaBug.druckeLog("Strasse", logDrucken, 10);
		}
		if (!pAktionaersdatenSession.getAendPLZ().trim().equals(pAktionaersdatenSession.getAktionaerPLZ().trim())) {
			unterschiedGefunden = true;
			CaBug.druckeLog("PLZ", logDrucken, 10);
		}
		if (!pAktionaersdatenSession.getAendOrt().trim().equals(pAktionaersdatenSession.getAktionaerOrt().trim())) {
			unterschiedGefunden = true;
			CaBug.druckeLog("Ort", logDrucken, 10);
		}
        if ( pAktionaersdatenSession.getAendLandNummer()!=pAktionaersdatenSession.getAktionaerLandNummer() ) {
            unterschiedGefunden = true;
            CaBug.druckeLog("Land", logDrucken, 10);
        }

		if (unterschiedGefunden == false) {
			return 3;
		}

		/*************** Plausibilität prüfen ***************************/

		/* PLZ prüfen - wenn Deutschland dann 5 stellig */
		CaBug.druckeLog("pAktionaersdatenSession.getAendLandNummer()="+pAktionaersdatenSession.getAendLandNummer(), logDrucken, 10);
        if (pAktionaersdatenSession.getAendLandNummer()==276) {
            String hPlz=pAktionaersdatenSession.getAendPLZ().trim();
            hPlz=CaString.fuelleLinksNull(hPlz, 5);
            if (hPlz.length()!=5 || (!CaString.isNummern(hPlz))){
                tSession.trageFehlerEin(CaFehler.perPLZFalsch);
                return 0;
            }
         }
		
        if (pAktionaersdatenSession.getAendOrt().isEmpty()) {
            tSession.trageFehlerEin(CaFehler.perOrtFalsch);
            return 0;
        }
		
		if (tPermanentSession.isTestModus()) {
			if (pAktionaersdatenSession.getAendPLZ().isEmpty()) {
				tSession.trageFehlerEin(CaFehler.perPLZOrtFalsch);
				return 0;
			}
		} else {
		}

		/****************** Speichern *******************************/
		String lSchluessel = "";
		String lZeit = CaDatumZeit.DatumZeitStringFuerDatenbank();

		if (tPermanentSession.isTestModus()) {

		} else {
			String landName = "";
			
			for(EclStaaten eclStaaten : pAktionaersdatenSession.getAendLandListe()) {
				if(pAktionaersdatenSession.getAendLandNummer() == eclStaaten.id) {
					landName = eclStaaten.nameDE;
				}
			}
			EgxPostAdressaenderung egxPostAdressaenderung = null;
			egxPostAdressaenderung = new EgxPostAdressaenderung();
			egxPostAdressaenderung.strasse = pAktionaersdatenSession.getAendStrasse().trim();
			egxPostAdressaenderung.adresszusatz = pAktionaersdatenSession.getAendAdresszusatz().trim();
			
			egxPostAdressaenderung.plz = pAktionaersdatenSession.getAendPLZ().trim();
			egxPostAdressaenderung.ort = pAktionaersdatenSession.getAendOrt().trim();
			egxPostAdressaenderung.land = pAktionaersdatenSession.getAendLandNummer() + " "
					+ landName;
			
			

			EgxPostAdressaenderungRC egxPostAdressaenderungRC = brMGenossenschaftCall
					.doPostRequestAdressaenderung(aktionaersnummerFuerGenossenschaftSysWebrequest, egxPostAdressaenderung);
	        if (egxPostAdressaenderungRC==null) {
	            return CaFehler.perRemoteAktienregisterNichtVerfuegbar;
	        }
			lSchluessel = egxPostAdressaenderungRC.lfnr + ";10410";
		}

		/* Nun lokalen Auftrag speichern */
		BlAuftragAnbindungAktienregister lAuftragAnbindungAktienregister = new BlAuftragAnbindungAktienregister(
				eclDbM.getDbBundle());
		lAuftragAnbindungAktienregister.aendernAnschrift(pAktionaersnummer,
				eclLoginDatenM.getEclLoginDaten().ident * (-1), lSchluessel, lZeit,
				pAktionaersdatenSession.getAendAdresszusatz(), pAktionaersdatenSession.getAendStrasse(),
				pAktionaersdatenSession.getAendPLZ(), pAktionaersdatenSession.getAendOrt(),
				pAktionaersdatenSession.getAendLandNummer()
		);

        pAktionaersdatenSession.setAktionaersdatenAenderungInArbeit(true);

		/*****************
		 * Überprüfen, ob normales Mitglied oder Juristisch etc.
		 *********************/
//		if (pruefeNormalesMitglied()) {
//			return 1;
//		} else {
//			return 2;
//		}
        return 1;
	}

	/**
	 * =====================================================================Kontaktdaten=====================================
	 */
	public int kontaktdatenAendernVorbereiten(String pAktionaersnummer) {
		pAktionaersdatenSession.setAendTelefon1(pAktionaersdatenSession.getAktionaerTelefon1());
		pAktionaersdatenSession.setAendTelefon2(pAktionaersdatenSession.getAktionaerTelefon2());
		pAktionaersdatenSession.setAendTelefon3(pAktionaersdatenSession.getAktionaerTelefon3());
		pAktionaersdatenSession.setAendTelefon4(pAktionaersdatenSession.getAktionaerTelefon4());
		pAktionaersdatenSession.setAendEmail(pAktionaersdatenSession.getAktionaerEmail());
		return 1;
	}

	/*
	 * Return ==1 => ok, wurde gespeichert, normaler Hinweis ==2 => ok, wurde
	 * gespeichert, separater Hinweis für juristische Personen etc.
	 * 
	 * 
	 * ==3 => ok, aber keine Änderungen, d.h. nichts gespeichert
	 * ==4 => ok, aber E-Mail-Bestätigungscode ist noch einzugeben.
	 * 
	 * ==0 => Fehlermeldung wurde erzeugt, Kein Speichern erfolgt, Änderungsfenster
	 * muß angezeigt bleiben
	 */
	public int kontaktdatenAendernSpeichern(String pAktionaersnummer) {
		/*****************
		 * Checken, ob überhaupt Änderung vorhanden
		 **********************/
		BlNummernformBasis blNummernformBasis = new BlNummernformBasis(eclDbM.getDbBundle());
		String aktionaersnummerFuerGenossenschaftSysWebrequest = blNummernformBasis
				.aufbereitenKennungFuerGenossenschaftSysWebrequest(pAktionaersnummer);
		boolean unterschiedGefunden = false;
		if (!pAktionaersdatenSession.getAendTelefon1().trim()
				.equals(pAktionaersdatenSession.getAktionaerTelefon1().trim())) {
			unterschiedGefunden = true;
			CaBug.druckeLog("Telefon1", logDrucken, 10);
		}
		if (!pAktionaersdatenSession.getAendTelefon2().trim()
				.equals(pAktionaersdatenSession.getAktionaerTelefon2().trim())) {
			unterschiedGefunden = true;
			CaBug.druckeLog("Telefon2", logDrucken, 10);
		}
		if (!pAktionaersdatenSession.getAendTelefon3().trim()
				.equals(pAktionaersdatenSession.getAktionaerTelefon3().trim())) {
			unterschiedGefunden = true;
			CaBug.druckeLog("Telefon3", logDrucken, 10);
		}
		if (!pAktionaersdatenSession.getAendTelefon4().trim()
				.equals(pAktionaersdatenSession.getAktionaerTelefon4().trim())) {
			unterschiedGefunden = true;
			CaBug.druckeLog("Telefon4", logDrucken, 10);
		}
		if (!pAktionaersdatenSession.getAendEmail().trim().equals(pAktionaersdatenSession.getAktionaerEmail().trim())) {
			unterschiedGefunden = true;
			CaBug.druckeLog("Email", logDrucken, 10);
		}

		if (unterschiedGefunden == false) {
			return 3;
		}

		/*Telefondaten umbauen*/
		String alterString="", neuerString="";
		
		alterString=pAktionaersdatenSession.getAendTelefon1().trim();
		neuerString=ueberarbeiteTelefonnummer(alterString);
		pAktionaersdatenSession.setAendTelefon1(neuerString);

		alterString=pAktionaersdatenSession.getAendTelefon2().trim();
		neuerString=ueberarbeiteTelefonnummer(alterString);
		pAktionaersdatenSession.setAendTelefon2(neuerString);

		alterString=pAktionaersdatenSession.getAendTelefon3().trim();
		neuerString=ueberarbeiteTelefonnummer(alterString);
		pAktionaersdatenSession.setAendTelefon3(neuerString);

		alterString=pAktionaersdatenSession.getAendTelefon4().trim();
		neuerString=ueberarbeiteTelefonnummer(alterString);
		pAktionaersdatenSession.setAendTelefon4(neuerString);

		/*************** Plausibilität prüfen ***************************/

		/**Email ist zwingend einzugeben*/
		String hEmail=pAktionaersdatenSession.getAendEmail().trim();
		if (hEmail.isEmpty()) {//Email ist zwingend einzugeben
		    tSession.trageFehlerEin(CaFehler.perEMailZwingend);
		    return 0;
		}
		if (!CaString.isMailadresse(hEmail)) {
            tSession.trageFehlerEin(CaFehler.perEmailIstNichtRichtigesFormat);
            return 0;
		}
		
		
		if (tPermanentSession.isTestModus()) {
		} else {
		}

		/***********Email-verändert - dann Mail verschicken und Bestätigungscode anfordern********************/
        if (!pAktionaersdatenSession.getAendEmail().trim().equals(pAktionaersdatenSession.getAktionaerEmail().trim()) && pAktionaersdatenSession.isAendBestaetigungscodeEingeben()==false) {
            /**Code erzeugen*/
            String aendVorgegebenerBestaetigungscode=eclDbM.getDbBundle().dbEindeutigerKey.getNextFree();
            eclDbM.getDbBundle().reOpen();
            pAktionaersdatenSession.setAendVorgegebenerBestaetigungscode(aendVorgegebenerBestaetigungscode);
            
            /**Code per E-Mail verschicken*/
            tLinkSession.setEinsprungsLinkNurCode(aendVorgegebenerBestaetigungscode);
            String hMailText = "", hBetreff = "";
            hBetreff = eclTextePortalM.holeText("220");
            hMailText = eclTextePortalM.holeText("221");

            baMailm.senden(pAktionaersdatenSession.getAendEmail().trim(), hBetreff, hMailText);
            
            pAktionaersdatenSession.setAendBestaetigungscodeEingeben(true);
            return 4;
        }
		
        
        /*********Email-Veränderungscode abfragen**********************/
		if (pAktionaersdatenSession.isAendBestaetigungscodeEingeben()==true) {
		    CaBug.druckeLog("E-Mail-Bestätigungscodeabfrage aktiv", logDrucken, 10);
		    if (!pAktionaersdatenSession.getAendVorgegebenerBestaetigungscode().trim().equalsIgnoreCase(pAktionaersdatenSession.getAendBestaetigungscode().trim().toUpperCase())){
	            CaBug.druckeLog("E-Mail-Bestätigungscode falsch", logDrucken, 10);
		        pAktionaersdatenSession.setAendBestaetigungscodeZusatztextAnzeigen(true);
                pAktionaersdatenSession.setAendBestaetigungscodeZusatztext(1654);
	            return 0;
		    }
            CaBug.druckeLog("E-Mail-Bestätigungscode richtig", logDrucken, 10);
		}
		/****************** Speichern *******************************/
		String lSchluessel = "";
		String lZeit = CaDatumZeit.DatumZeitStringFuerDatenbank();

		BlAuftragAnbindungAktienregister lAuftragAnbindungAktienregister = new BlAuftragAnbindungAktienregister(
		        eclDbM.getDbBundle());

		if (tPermanentSession.isTestModus()) {

		} else {
			if (!pAktionaersdatenSession.getAendTelefon1().trim()
					.equals(pAktionaersdatenSession.getAktionaerTelefon1().trim())) {

				String pflegeArt = "";

				if (pAktionaersdatenSession.getAendTelefon1().trim().equals("")
						&& !pAktionaersdatenSession.getAktionaerTelefon1().trim().equals("")) {
					pflegeArt = "Löschung";
				} else if (!pAktionaersdatenSession.getAendTelefon1().trim().equals("")
						&& !pAktionaersdatenSession.getAktionaerTelefon1().trim().equals("")) {
					pflegeArt = "Pflege";
				} else if (!pAktionaersdatenSession.getAendTelefon1().trim().equals("")
						&& pAktionaersdatenSession.getAktionaerTelefon1().trim().equals("")) {
					pflegeArt = "Neuanlage";
				}

				EgxPostTelefon egxPostTelefon = null;
				egxPostTelefon = new EgxPostTelefon();
				egxPostTelefon.art = pflegeArt;
				egxPostTelefon.telefonart = "Telefon privat";
				egxPostTelefon.telefonartnr = 1;
				egxPostTelefon.telefonnummer = pAktionaersdatenSession.getAendTelefon1().trim();
				
				
				EgxPostTelefonRC egxPostTelefonRC = brMGenossenschaftCall.doPostRequestTelefon(aktionaersnummerFuerGenossenschaftSysWebrequest, egxPostTelefon);
	            if (egxPostTelefonRC==null) {
	                return CaFehler.perRemoteAktienregisterNichtVerfuegbar;
	            }
				lSchluessel = egxPostTelefonRC.lfnr + ";10412";
				
				
		        /* Nun lokalen Auftrag speichern */
		        lAuftragAnbindungAktienregister.aendernKontaktdaten(KonstAuftragArt.ANBINDUNG_AKTIENREGISTER_AENDERN_MEINEDATEN_TELEFON1, pAktionaersnummer,
		                eclLoginDatenM.getEclLoginDaten().ident * (-1), lSchluessel, lZeit,
		                pAktionaersdatenSession.getAendTelefon1());
                pAktionaersdatenSession.setTelefonAenderungInArbeit(true);

			}
			if (!pAktionaersdatenSession.getAendTelefon2().trim()
					.equals(pAktionaersdatenSession.getAktionaerTelefon2().trim())) {
				
				String pflegeArt = "";

				if (pAktionaersdatenSession.getAendTelefon2().trim().equals("")
						&& !pAktionaersdatenSession.getAktionaerTelefon2().trim().equals("")) {
					pflegeArt = "Löschung";
				} else if (!pAktionaersdatenSession.getAendTelefon2().trim().equals("")
						&& !pAktionaersdatenSession.getAktionaerTelefon2().trim().equals("")) {
					pflegeArt = "Pflege";
				} else if (!pAktionaersdatenSession.getAendTelefon2().trim().equals("")
						&& pAktionaersdatenSession.getAktionaerTelefon2().trim().equals("")) {
					pflegeArt = "Neuanlage";
				}

				EgxPostTelefon egxPostTelefon = null;
				egxPostTelefon = new EgxPostTelefon();
				egxPostTelefon.art = pflegeArt;
				egxPostTelefon.telefonart = "Telefon geschäftlich";
				egxPostTelefon.telefonartnr = 2;
				egxPostTelefon.telefonnummer = pAktionaersdatenSession.getAendTelefon2().trim();
				
				EgxPostTelefonRC egxPostTelefonRC = brMGenossenschaftCall.doPostRequestTelefon(aktionaersnummerFuerGenossenschaftSysWebrequest, egxPostTelefon);
                if (egxPostTelefonRC==null) {
                    return CaFehler.perRemoteAktienregisterNichtVerfuegbar;
                }
				lSchluessel = egxPostTelefonRC.lfnr + ";10412";

                /* Nun lokalen Auftrag speichern */
                lAuftragAnbindungAktienregister.aendernKontaktdaten(KonstAuftragArt.ANBINDUNG_AKTIENREGISTER_AENDERN_MEINEDATEN_TELEFON2, pAktionaersnummer,
                        eclLoginDatenM.getEclLoginDaten().ident * (-1), lSchluessel, lZeit,
                        pAktionaersdatenSession.getAendTelefon2());
                pAktionaersdatenSession.setTelefonAenderungInArbeit(true);

			}
			if (!pAktionaersdatenSession.getAendTelefon3().trim()
					.equals(pAktionaersdatenSession.getAktionaerTelefon3().trim())) {
				
				String pflegeArt = "";

				if (pAktionaersdatenSession.getAendTelefon3().trim().equals("")
						&& !pAktionaersdatenSession.getAktionaerTelefon3().trim().equals("")) {
					pflegeArt = "Löschung";
				} else if (!pAktionaersdatenSession.getAendTelefon3().trim().equals("")
						&& !pAktionaersdatenSession.getAktionaerTelefon3().trim().equals("")) {
					pflegeArt = "Pflege";
				} else if (!pAktionaersdatenSession.getAendTelefon3().trim().equals("")
						&& pAktionaersdatenSession.getAktionaerTelefon3().trim().equals("")) {
					pflegeArt = "Neuanlage";
				}

				EgxPostTelefon egxPostTelefon = null;
				egxPostTelefon = new EgxPostTelefon();
				egxPostTelefon.art = pflegeArt;
				egxPostTelefon.telefonart = "Mobiltelefon privat";
				egxPostTelefon.telefonartnr = 7;
				egxPostTelefon.telefonnummer = pAktionaersdatenSession.getAendTelefon3().trim();
				
				EgxPostTelefonRC egxPostTelefonRC = brMGenossenschaftCall.doPostRequestTelefon(aktionaersnummerFuerGenossenschaftSysWebrequest, egxPostTelefon);
                if (egxPostTelefonRC==null) {
                    return CaFehler.perRemoteAktienregisterNichtVerfuegbar;
                }
				lSchluessel = egxPostTelefonRC.lfnr + ";10412";

                /* Nun lokalen Auftrag speichern */
                lAuftragAnbindungAktienregister.aendernKontaktdaten(KonstAuftragArt.ANBINDUNG_AKTIENREGISTER_AENDERN_MEINEDATEN_TELEFON3, pAktionaersnummer,
                        eclLoginDatenM.getEclLoginDaten().ident * (-1), lSchluessel, lZeit,
                        pAktionaersdatenSession.getAendTelefon3());
                pAktionaersdatenSession.setTelefonAenderungInArbeit(true);
			}
			if (!pAktionaersdatenSession.getAendTelefon4().trim()
					.equals(pAktionaersdatenSession.getAktionaerTelefon4().trim())) {
				
				String pflegeArt = "";

				if (pAktionaersdatenSession.getAendTelefon4().trim().equals("")
						&& !pAktionaersdatenSession.getAktionaerTelefon4().trim().equals("")) {
					pflegeArt = "Löschung";
				} else if (!pAktionaersdatenSession.getAendTelefon4().trim().equals("")
						&& !pAktionaersdatenSession.getAktionaerTelefon4().trim().equals("")) {
					pflegeArt = "Pflege";
				} else if (!pAktionaersdatenSession.getAendTelefon4().trim().equals("")
						&& pAktionaersdatenSession.getAktionaerTelefon4().trim().equals("")) {
					pflegeArt = "Neuanlage";
				}

				EgxPostTelefon egxPostTelefon = null;
				egxPostTelefon = new EgxPostTelefon();
				egxPostTelefon.art = pflegeArt;
				egxPostTelefon.telefonart = "Mobiltelefon geschäftlich";
				egxPostTelefon.telefonartnr = 8;
				egxPostTelefon.telefonnummer = pAktionaersdatenSession.getAendTelefon4().trim();
				
				EgxPostTelefonRC egxPostTelefonRC = brMGenossenschaftCall.doPostRequestTelefon(aktionaersnummerFuerGenossenschaftSysWebrequest, egxPostTelefon);
                if (egxPostTelefonRC==null) {
                    return CaFehler.perRemoteAktienregisterNichtVerfuegbar;
                }
				lSchluessel = egxPostTelefonRC.lfnr + ";10412";

                /* Nun lokalen Auftrag speichern */
                lAuftragAnbindungAktienregister.aendernKontaktdaten(KonstAuftragArt.ANBINDUNG_AKTIENREGISTER_AENDERN_MEINEDATEN_TELEFON4, pAktionaersnummer,
                        eclLoginDatenM.getEclLoginDaten().ident * (-1), lSchluessel, lZeit,
                        pAktionaersdatenSession.getAendTelefon4());
                pAktionaersdatenSession.setTelefonAenderungInArbeit(true);
			}
			
			if (!pAktionaersdatenSession.getAendEmail().trim()
					.equals(pAktionaersdatenSession.getAktionaerEmail().trim())) {
			    
			    /*Als erstes E-Mail-Adresse lokal speichern - denn das kann schief gehen, und ist das relevanteste*/
                int rc=eclDbM.getDbBundle().dbLoginDaten.read_loginKennung(pAktionaersnummer);
			    EclLoginDaten lLoginDaten=eclDbM.getDbBundle().dbLoginDaten.ergebnisPosition(0);
			    lLoginDaten.eMailFuerVersand=pAktionaersdatenSession.getAendEmail().trim();
			    lLoginDaten.emailBestaetigt=1;
			    lLoginDaten.emailBestaetigenLink="";
			    rc=eclDbM.getDbBundle().dbLoginDaten.update(lLoginDaten);
		        if (rc < 1) {
		            tSession.trageFehlerEin(CaFehler.afAndererUserAktiv);
		            return 0;
		        }
		        eclLoginDatenM.setEclLoginDaten(lLoginDaten);
			    
		        rc=mailAnGenossenschaftSys(pAktionaersnummer, pAktionaersdatenSession.getAendEmail().trim(), lZeit);
                if (rc==CaFehler.perRemoteAktienregisterNichtVerfuegbar) {
                    return CaFehler.perRemoteAktienregisterNichtVerfuegbar;
                }
               pAktionaersdatenSession.setEmailAenderungInArbeit(true);

			}
		}


		/*****************
		 * Überprüfen, ob normales Mitglied oder Juristisch etc.
		 *********************/
//      if (pruefeNormalesMitglied()) {
//      return 1;
//  } else {
//      return 2;
//  }
    return 1;
	}

	
	/*Hinweis: einige Werte hier neu berechnet, obwohl im Zusammenhang mit pAktionaersdaten eigentlich schon vorhanden.
	 * Aber notwendig, da als Standalone-Funktion auch für Erst-Registrierung erforderlich
	 * 
	 */
	public int mailAnGenossenschaftSys(String pAktionaersnummer, String pMailAdresse, String pZeit) {
	    BlNummernformBasis blNummernformBasis = new BlNummernformBasis(eclDbM.getDbBundle());
	    String aktionaersnummerFuerGenossenschaftSysWebrequest = blNummernformBasis
	            .aufbereitenKennungFuerGenossenschaftSysWebrequest(pAktionaersnummer);

	    EgxPostEmailaenderung egxPostEmailaenderung = null;
	    egxPostEmailaenderung = new EgxPostEmailaenderung();
	    egxPostEmailaenderung.emailadresse = pMailAdresse;
	    EgxPostEmailaenderungRC egxPostEmailaenderungRC = brMGenossenschaftCall
	            .doPostRequestEmailaenderung(aktionaersnummerFuerGenossenschaftSysWebrequest, egxPostEmailaenderung);
        if (egxPostEmailaenderungRC==null) {
            return CaFehler.perRemoteAktienregisterNichtVerfuegbar;
        }
	    String lSchluessel = egxPostEmailaenderungRC.lfnr + ";10411";
	    System.out.println(egxPostEmailaenderungRC);

	    /* Nun lokalen Auftrag speichern */
	    BlAuftragAnbindungAktienregister lAuftragAnbindungAktienregister = new BlAuftragAnbindungAktienregister(
	            eclDbM.getDbBundle());
	    lAuftragAnbindungAktienregister.aendernKontaktdaten(KonstAuftragArt.ANBINDUNG_AKTIENREGISTER_AENDERN_MEINEDATEN_EMAIL, pAktionaersnummer,
	            eclLoginDatenM.getEclLoginDaten().ident * (-1), lSchluessel, pZeit,
	            /*pAktionaersdatenSession.getAendEmail()*/pMailAdresse);

	    return 1;
	}
	
	
	
	
	/**
	 * =====================================================================Geburtsdatum=====================================
	 */
	public int geburtsdatumAendernVorbereiten(String pAktionaersnummer, EclAktienregisterWeiterePerson pWeiterePerson) {
		pAktionaersdatenSession.setAendGeburtsdatum(pWeiterePerson.geburtsdatum);
		pAktionaersdatenSession.setAendPerson(pWeiterePerson);
		pAktionaersdatenSession.setAendName(pWeiterePerson.nachname);
		pAktionaersdatenSession.setAendVorname(pWeiterePerson.vorname);
		return 1;
	}

	/*
	 * Return ==1 => ok, wurde gespeichert, normaler Hinweis ==2 => ok, wurde
	 * gespeichert, separater Hinweis für juristische Personen etc.
	 * 
	 * ==3 => ok, aber keine Änderungen, d.h. nichts gespeichert
	 * 
	 * ==0 => Fehlermeldung wurde erzeugt, Kein Speichern erfolgt, Änderungsfenster
	 * muß angezeigt bleiben
	 */
	public int geburtsdatumAendernSpeichern(String pAktionaersnummer) {
		/*****************
		 * Checken, ob überhaupt Änderung vorhanden
		 * 
		 **********************/
		BlNummernformBasis blNummernformBasis = new BlNummernformBasis(eclDbM.getDbBundle());
		String aktionaersnummerFuerGenossenschaftSysWebrequest = blNummernformBasis
				.aufbereitenKennungFuerGenossenschaftSysWebrequest(pAktionaersnummer);
		boolean unterschiedGefunden = false;
		if (!pAktionaersdatenSession.getAendGeburtsdatum().trim()
				.equals(pAktionaersdatenSession.getAendPerson().getGeburtsdatum().trim())) {
			unterschiedGefunden = true;
			CaBug.druckeLog("Geburtsdatum", logDrucken, 10);
		}

		if (unterschiedGefunden == false) {
			return 3;
		}

		/*************** Plausibilität prüfen ***************************/

		if (!CaString.isDatum(pAktionaersdatenSession.getAendGeburtsdatum().trim())){
            tSession.trageFehlerEin(CaFehler.perGeburtsdatumFalsch);
            return 0;
		}
		
		
		/****************** Speichern *******************************/
		String lSchluessel = "";
		String lZeit = CaDatumZeit.DatumZeitStringFuerDatenbank();

		/*
		 * Als erstes Request an GeDix absetzen (weil Schlüssel benötigt wird, und nur
		 * dann auch überhaupt durchführbar)
		 */
		if (tPermanentSession.isTestModus()) {

		} else {
			EgxPostGeburtsdatum egxPostGeburtsdatum = null;
			egxPostGeburtsdatum = new EgxPostGeburtsdatum();
			egxPostGeburtsdatum.personen_lfnr = pAktionaersdatenSession.getAendPerson().getIdent();
			egxPostGeburtsdatum.geburtsdatum = CaDatumZeit.datumNormalZuJJJJ_MM_TT(pAktionaersdatenSession.getAendGeburtsdatum().trim());
			EgxPostGeburtsdatumRC egxPostGeburtsdatumRC = brMGenossenschaftCall.doPostRequestGeburtsdatum(aktionaersnummerFuerGenossenschaftSysWebrequest, egxPostGeburtsdatum);
	        if (egxPostGeburtsdatumRC==null) {
	            return CaFehler.perRemoteAktienregisterNichtVerfuegbar;
	        }
			lSchluessel = egxPostGeburtsdatumRC.lfnr + ";10361";
			
			CaBug.druckeLog(egxPostGeburtsdatumRC.toString(), logDrucken, 10);
		}

		/* Nun lokalen Auftrag speichern */
		BlAuftragAnbindungAktienregister lAuftragAnbindungAktienregister = new BlAuftragAnbindungAktienregister(
				eclDbM.getDbBundle());
		lAuftragAnbindungAktienregister.aendernGeburtsdatum(pAktionaersnummer,
				eclLoginDatenM.getEclLoginDaten().ident * (-1), lSchluessel, lZeit,
				pAktionaersdatenSession.getAendGeburtsdatum(), pAktionaersdatenSession.getAendPerson().getIdent());
        pAktionaersdatenSession.setGeburtsdatumAenderungInArbeit(true);

		/*****************
		 * Überprüfen, ob normales Mitglied oder Juristisch etc.
		 *********************/
//      if (pruefeNormalesMitglied()) {
//      return 1;
//  } else {
//      return 2;
//  }
    return 1;
	}

	/**
	 * =====================================================================SteuerId=====================================
	 */
	public int steuerIdAendernVorbereiten(String pAktionaersnummer, EclAktienregisterWeiterePerson pWeiterePerson) {
		pAktionaersdatenSession.setAendSteuerId(pWeiterePerson.steuerId);
		pAktionaersdatenSession.setAendPerson(pWeiterePerson);
		pAktionaersdatenSession.setAendName(pWeiterePerson.nachname);
		pAktionaersdatenSession.setAendVorname(pWeiterePerson.vorname);
		return 1;
	}

	/*
	 * Return ==1 => ok, wurde gespeichert, normaler Hinweis ==2 => ok, wurde
	 * gespeichert, separater Hinweis für juristische Personen etc.
	 * 
	 * ==3 => ok, aber keine Änderungen, d.h. nichts gespeichert
	 * 
	 * ==0 => Fehlermeldung wurde erzeugt, Kein Speichern erfolgt, Änderungsfenster
	 * muß angezeigt bleiben
	 */
	public int steuerIdAendernSpeichern(String pAktionaersnummer) {
		BlNummernformBasis blNummernformBasis = new BlNummernformBasis(eclDbM.getDbBundle());
		String aktionaersnummerFuerGenossenschaftSysWebrequest = blNummernformBasis
				.aufbereitenKennungFuerGenossenschaftSysWebrequest(pAktionaersnummer);
		/*****************
		 * Checken, ob überhaupt Änderung vorhanden
		 **********************/
		boolean unterschiedGefunden = false;
		if (!pAktionaersdatenSession.getAendSteuerId().trim()
				.equals(pAktionaersdatenSession.getAendPerson().getSteuerId().trim())) {
			unterschiedGefunden = true;
			CaBug.druckeLog("SteuerID", logDrucken, 10);
		}

		if (unterschiedGefunden == false) {
			return 3;
		}

		/*************** Plausibilität prüfen ***************************/

		String hSteuerId=pAktionaersdatenSession.getAendSteuerId().trim();
		if (hSteuerId.length()!=11 || (!CaString.isNummern(hSteuerId))) {
            tSession.trageFehlerEin(CaFehler.perSteuerIdFalsch);
            return 0;
		}

		/****************** Speichern *******************************/
		String lSchluessel = "";
		String lZeit = CaDatumZeit.DatumZeitStringFuerDatenbank();

		/*
		 * Als erstes Request an GeDix absetzen (weil Schlüssel benötigt wird, und nur
		 * dann auch überhaupt durchführbar)
		 */
		if (tPermanentSession.isTestModus()) {

		} else {
			EgxPostSteuerid egxPostSteuerid = null;
			egxPostSteuerid = new EgxPostSteuerid();
			egxPostSteuerid.personen_lfnr = pAktionaersdatenSession.getAendPerson().getIdent();
			egxPostSteuerid.steuerid = pAktionaersdatenSession.getAendSteuerId().trim();
			
			EgxPostSteueridRC egxPostSteueridRC = brMGenossenschaftCall.doPostRequestSteuerid(aktionaersnummerFuerGenossenschaftSysWebrequest, egxPostSteuerid);
            if (egxPostSteueridRC==null) {
                return CaFehler.perRemoteAktienregisterNichtVerfuegbar;
            }
			lSchluessel = egxPostSteueridRC.lfnr + ";10360";
			CaBug.druckeLog(egxPostSteueridRC.toString(), logDrucken, 10);
		}

		/* Nun lokalen Auftrag speichern */
		BlAuftragAnbindungAktienregister lAuftragAnbindungAktienregister = new BlAuftragAnbindungAktienregister(
				eclDbM.getDbBundle());
		lAuftragAnbindungAktienregister.aendernSteuerId(pAktionaersnummer,
				eclLoginDatenM.getEclLoginDaten().ident * (-1), lSchluessel, lZeit,
				pAktionaersdatenSession.getAendSteuerId(), pAktionaersdatenSession.getAendPerson().getIdent());
        pAktionaersdatenSession.setSteuerIdAenderungInArbeit(true);
        
		/*****************
		 * Überprüfen, ob normales Mitglied oder Juristisch etc.
		 *********************/
		if (pruefeNormalesMitglied()) {
			return 1;
		} else {
			return 2;
		}
	}

	/**
	 * =====================================================================Bankverbindung=====================================
	 */
	public int bankverbindungAendernVorbereiten(String pAktionaersnummer) {
		pAktionaersdatenSession.setAendAktionaersname(pAktionaersdatenSession.getBankAktionaersname());
        pAktionaersdatenSession.setAendBankname(pAktionaersdatenSession.getBankBankname());
		pAktionaersdatenSession.setAendIban(pAktionaersdatenSession.getBankIban());
        pAktionaersdatenSession.setAendBic(pAktionaersdatenSession.getBankBic());

//      if (pruefeNormalesMitglied()) {
//      return 1;
//  } else {
//      return 2;
//  }
    return 1;
	}

	/*
	 * Return ==1 => ok, wurde gespeichert, normaler Hinweis ==2 => ok, wurde
	 * gespeichert, separater Hinweis für juristische Personen etc.
	 * 
	 * ==3 => ok, aber keine Änderungen, d.h. nichts gespeichert
	 * 
	 * ==0 => Fehlermeldung wurde erzeugt, Kein Speichern erfolgt, Änderungsfenster
	 * muß angezeigt bleiben
	 */
	public int bankverbindungAendernSpeichern(String pAktionaersnummer) {
		/*****************
		 * Checken, ob überhaupt Änderung vorhanden
		 **********************/
		BlNummernformBasis blNummernformBasis = new BlNummernformBasis(eclDbM.getDbBundle());
		String aktionaersnummerFuerGenossenschaftSysWebrequest = blNummernformBasis
				.aufbereitenKennungFuerGenossenschaftSysWebrequest(pAktionaersnummer);
		boolean unterschiedGefunden = false;
		if (!pAktionaersdatenSession.getAendAktionaersname().trim().equals(pAktionaersdatenSession.getBankAktionaersname().trim())) {
			unterschiedGefunden = true;
			CaBug.druckeLog("Name", logDrucken, 10);
		}
		if (!pAktionaersdatenSession.getAendBankname().trim().equals(pAktionaersdatenSession.getBankBankname().trim())) {
			unterschiedGefunden = true;
			CaBug.druckeLog("Bankname", logDrucken, 10);
		}
        if (!pAktionaersdatenSession.getAendIban().trim().equals(pAktionaersdatenSession.getBankIban().trim())) {
            unterschiedGefunden = true;
            CaBug.druckeLog("IBAN", logDrucken, 10);
        }
        if (!pAktionaersdatenSession.getAendBic().trim().equals(pAktionaersdatenSession.getBankBic().trim())) {
            unterschiedGefunden = true;
            CaBug.druckeLog("Bic", logDrucken, 10);
        }

		if (unterschiedGefunden == false) {
			return 3;
		}

		/*************** Plausibilität prüfen ***************************/

		String ibanZumVerarbeiten=CaBank.ibanZuIntern(pAktionaersdatenSession.getAendIban());
		if (tPermanentSession.isTestModus()) {
		} else {
            if (!CaString.isNummernAlphaOderLeer(pAktionaersdatenSession.getAendBic())) {
                tSession.trageFehlerEin(CaFehler.perBicFalsch);
                return 0;
            }
		    
            if (pAktionaersdatenSession.getAendBic().isEmpty()) {
                tSession.trageFehlerEin(CaFehler.perBicFalsch);
                return 0;
            }
            if (pAktionaersdatenSession.getAendBankname().isEmpty()) {
                tSession.trageFehlerEin(CaFehler.perBanknameFalsch);
                return 0;
            }
            if (pAktionaersdatenSession.getAendAktionaersname().isEmpty()) {
                tSession.trageFehlerEin(CaFehler.perBankAktionaersnameFalsch);
                return 0;
            }
           
            
			EgxPostIban egxPostIban = null;
			egxPostIban = new EgxPostIban();
			
			EgxPostIbanRC egxPostIbanRC = brMGenossenschaftCall.doPostRequestIban(aktionaersnummerFuerGenossenschaftSysWebrequest, ibanZumVerarbeiten, egxPostIban);
			if (egxPostIbanRC==null || !egxPostIbanRC.success) {
			    tSession.trageFehlerEin(CaFehler.perIBANFalsch);
			    return 0;
			}
            CaBug.druckeLog(egxPostIbanRC.toString(), logDrucken, 10);
		}

		/****************** Speichern *******************************/
		String lSchluessel = "";
		String lZeit = CaDatumZeit.DatumZeitStringFuerDatenbank();

		
		if (tPermanentSession.isTestModus()) {

		} else {
			EgxPostBankverbindung egxPostBankverbindung = null;
			egxPostBankverbindung = new EgxPostBankverbindung();
			egxPostBankverbindung.name_gutschrift = pAktionaersdatenSession.getAendAktionaersname();
			egxPostBankverbindung.iban_gutschrift = ibanZumVerarbeiten;
			egxPostBankverbindung.bankname_gutschrift = pAktionaersdatenSession.getAendBankname().trim();
			egxPostBankverbindung.bic_gutschrift = pAktionaersdatenSession.getAendBic().trim();
			egxPostBankverbindung.bestaetigung = "Keine Bestätigung";
			EgxPostBankverbindungRC egxPostBankverbindungRC = brMGenossenschaftCall.doPostRequestBankverbindung(aktionaersnummerFuerGenossenschaftSysWebrequest, egxPostBankverbindung);
            if (egxPostBankverbindungRC==null) {
                return CaFehler.perRemoteAktienregisterNichtVerfuegbar;
            }
			lSchluessel = egxPostBankverbindungRC.lfnr + ";10400";
			CaBug.druckeLog(egxPostBankverbindungRC.toString(), logDrucken, 10);
		}

		/* Nun lokalen Auftrag speichern */
		BlAuftragAnbindungAktienregister lAuftragAnbindungAktienregister = new BlAuftragAnbindungAktienregister(
				eclDbM.getDbBundle());
		lAuftragAnbindungAktienregister.aendernBankverbindung(pAktionaersnummer,
				eclLoginDatenM.getEclLoginDaten().ident * (-1), lSchluessel, lZeit,
				pAktionaersdatenSession.getAendAktionaersname(), 
                pAktionaersdatenSession.getAendBankname(), 
                ibanZumVerarbeiten,
                pAktionaersdatenSession.getAendBic()
				);
        pAktionaersdatenSession.setBankverbindungAenderungInArbeit(true);
        
		/*****************
		 * Überprüfen, ob normales Mitglied oder Juristisch etc.
		 *********************/
//      if (pruefeNormalesMitglied()) {
//      return 1;
//  } else {
//      return 2;
//  }
    return 1;
	}

	/**************************************
	 * Übergreifende Funktionen
	 *************************/
	
	private int holeMitgliedsart() {
        String vorname = pAktionaersdatenSession.getAktionaerVorname();
        String nachname = pAktionaersdatenSession.getAktionaerNachname();
        List<EclAktienregisterWeiterePerson> zugeordneteWeiterPersonen = pAktionaersdatenSession
                .getZugeordneteWeiterPersonen();

        BrLiefereMitgliedArt brLiefereMitgliedArt=new BrLiefereMitgliedArt();
        int personenArt=brLiefereMitgliedArt.liefereMitgliedstatus(vorname, nachname, zugeordneteWeiterPersonen);
        CaBug.druckeLog("Personenart="+personenArt, logDrucken, 10);
        return personenArt;
	    
	}
	
	private boolean pruefeNormalesMitglied() {
        int personenArt=holeMitgliedsart();
        
        switch (personenArt) {
        case KonstGruppen.eheleuteGesamthans:return false;
        case KonstGruppen.erbengemeinschaft:return false;
        case KonstGruppen.firmen:return false;
        case KonstGruppen.minderjaehrigesEinzelmitglied:return false;
       }
        
        return true;

	}

	private String ueberarbeiteTelefonnummer(String alteTelefonnummer) {
	    String neueTelefonnummer="";
	    if (alteTelefonnummer!=null && alteTelefonnummer.length()>0) {
	        for (int i=0;i<alteTelefonnummer.length();i++) {
	            String zeichen=alteTelefonnummer.substring(i,i+1);
	            String neuesZeichen="";
	            switch (zeichen) {
	            case "0":
                case "1":
                case "2":
                case "3":
                case "4":
                case "5":
                case "6":
                case "7":
                case "8":
                case "9":
                    neuesZeichen=zeichen;
                    break;
                case "/":
                    neuesZeichen=zeichen;
                    break;
                case "+":
                    neuesZeichen="00";
                    break;
	            }
	            
	            neueTelefonnummer+=neuesZeichen;
	        }
	    }
	    
	    return neueTelefonnummer;
	}

	
	
}
