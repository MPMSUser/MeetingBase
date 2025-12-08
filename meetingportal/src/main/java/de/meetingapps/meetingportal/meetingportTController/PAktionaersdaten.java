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
package de.meetingapps.meetingportal.meetingportTController;



import de.meetingapps.meetingportal.meetComAllg.CaBank;
import de.meetingapps.meetingportal.meetComAllg.CaBug;
import de.meetingapps.meetingportal.meetComAllg.CaFehler;
import de.meetingapps.meetingportal.meetComAllg.CaString;
import de.meetingapps.meetingportal.meetComBrM.BrMAktionaersdaten;
import de.meetingapps.meetingportal.meetComEclM.EclDbM;
import de.meetingapps.meetingportal.meetComEclM.EclLoginDatenM;
import de.meetingapps.meetingportal.meetComEntities.EclAktienregisterWeiterePerson;
import de.meetingapps.meetingportal.meetComKonst.KonstPortalView;
import de.meetingapps.meetingportal.meetingportTFunktionen.TRemoteAR;
import de.meetingapps.meetingportal.meetingportTFunktionen.TSessionVerwaltung;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@RequestScoped
@Named
public class PAktionaersdaten {

	private int logDrucken = 3;

	private @Inject EclDbM eclDbM;
	private @Inject TSessionVerwaltung tSessionVerwaltung;
	private @Inject TSession tSession;
	private @Inject PAktionaersdatenSession pAktionaersdatenSession;

	private @Inject EclLoginDatenM eclLoginDatenM;

	private @Inject BrMAktionaersdaten brMAktionaersdaten;

    private @Inject TRemoteAR tRemoteAR;

	/**
	 * pOpenDurchfuehren=true => es wird der Open/close in init durchgeführt.
	 */
	public int init(boolean pOpenDurchfuehren) {

		pAktionaersdatenSession.clear();

		if (pOpenDurchfuehren) {
			eclDbM.openAll();
		}

		int rc=brMAktionaersdaten.holeAktuellenStand(eclLoginDatenM.getEclLoginDaten().loginKennung);
		if (rc==CaFehler.perRemoteAktienregisterNichtVerfuegbar) {
	        if (pOpenDurchfuehren) {
	            eclDbM.closeAll();
	        }
		    return CaFehler.perRemoteAktienregisterNichtVerfuegbar;
		}
		
		/* Länderliste füllen */
		eclDbM.getDbBundle().dbStaaten.mandantenabhaengig = true;

		eclDbM.getDbBundle().dbStaaten.readAll(1); // Sortierung nach Name
		pAktionaersdatenSession.setAendLandListe(eclDbM.getDbBundle().dbStaaten.ergebnis());
		eclDbM.getDbBundle().dbStaaten.mandantenabhaengig = false;

		if (pOpenDurchfuehren) {
			eclDbM.closeAll();
		}
		
		return 1;
	}

	/***************************************
	 * Anschrift ändern
	 **************************************************/
	public void doAnschriftAendern() {
		if (!tSessionVerwaltung.pruefeStart(KonstPortalView.P_AKTIONAERSDATEN)) {
			return;
		}
		eclDbM.openAll();
		int rc=brMAktionaersdaten.anschriftAendernVorbereiten(eclLoginDatenM.getEclLoginDaten().loginKennung);
        if (tRemoteAR.pruefeVerfuegbar(rc)==false) {
            eclDbM.closeAll();
            tSessionVerwaltung.setzeEnde();
            return;
        }
		pAktionaersdatenSession.setAendAnschriftAktiv(true);
        pAktionaersdatenSession.setAendPLZGeprueft(CaBank.ibanZuIntern(pAktionaersdatenSession.getAendPLZ()));
		eclDbM.closeAll();
		tSessionVerwaltung.setzeEnde();

	}

	public void doSetAendOrt() {

	    if (pAktionaersdatenSession.getAendLandNummer()==276 || pAktionaersdatenSession.getAendLandNummer()==0) {
	        eclDbM.openAll();
	        eclDbM.openWeitere();

	        String hPlz = pAktionaersdatenSession.getAendPLZ().trim();
	        if (CaString.isNummern(hPlz)) {
	            hPlz = CaString.fuelleLinksNull(hPlz, 5);
	            pAktionaersdatenSession.setAendPLZ(hPlz);
	        }
	        eclDbM.getDbBundle().dbPlzOrt.mandantenabhaengig = true;
	        eclDbM.getDbBundle().dbPlzOrt.readPlz(pAktionaersdatenSession.getAendPLZ());
	        if (eclDbM.getDbBundle().dbPlzOrt.anzErgebnis() > 0) {
	            pAktionaersdatenSession.setAendOrt(eclDbM.getDbBundle().dbPlzOrt.ergebnisPosition(0).ort);
	        } else {
	            pAktionaersdatenSession.setAendOrt("");

	        }
	        eclDbM.getDbBundle().dbPlzOrt.mandantenabhaengig = false;

	        pAktionaersdatenSession.setAendPLZGeprueft(pAktionaersdatenSession.getAendPLZ().trim());

	        eclDbM.closeAll();
	    }
	}

	public void doAnschriftAendernAbbrechen() {
		CaBug.druckeLog("", logDrucken, 10);
		if (!tSessionVerwaltung.pruefeStart(KonstPortalView.P_AKTIONAERSDATEN)) {
			return;
		}
		pAktionaersdatenSession.setAendAnschriftAktiv(false);
		tSession.trageQuittungTextNr(1601, 2);
		tSessionVerwaltung.setzeEnde();
	}

	public void doAnschriftAendernSpeichern() {
		if (!tSessionVerwaltung.pruefeStart(KonstPortalView.P_AKTIONAERSDATEN)) {
			return;
		}
        
		
        pAktionaersdatenSession.setAendStrasse(CaString.replaceStreetAbbreviations(pAktionaersdatenSession.getAendStrasse()));
        
	      /*Ggf noch PLZ-Überprüfung veranlassen, falls noch nicht erfolgt*/
        if (!pAktionaersdatenSession.getAendPLZGeprueft().trim().equals(pAktionaersdatenSession.getAendPLZ().trim())){
            if ((pAktionaersdatenSession.getAendLandNummer()==276 || pAktionaersdatenSession.getAendLandNummer()==0) &&
                 (pAktionaersdatenSession.getAktionaerLandNummer()==276 || pAktionaersdatenSession.getAktionaerLandNummer()==0)   ) {
                /*Nur wenn Land vorher und hinterher Deutsch war nochmal prüfen*/
                
                doSetAendOrt();
            }
        }

		eclDbM.openAll();
		int rc = brMAktionaersdaten.anschriftAendernSpeichern(eclLoginDatenM.getEclLoginDaten().loginKennung);
        if (tRemoteAR.pruefeVerfuegbar(rc)==false) {
        }
        
		CaBug.druckeLog("rc anschriftAendernSpeichern=" + rc, logDrucken, 10);
		if (rc == 1 || rc == 2 || rc == 3) {
			pAktionaersdatenSession.setAendAnschriftAktiv(false);
			/*
			 * Nun noch Meldung setzen: 1= erledigt Text 1598 2="Vollmacht nachreichen" Text
			 * 1599 3=Keine Änderung gespeichert, Text 1600
			 */
			switch (rc) {
			case 1:
				tSession.trageQuittungTextNr(1598, 2);
				break;
			case 2:
				tSession.trageQuittungTextNr(1599, 2);
				break;
			case 3:
				tSession.trageQuittungTextNr(1600, 2);
				break;
			}

		}
		if (rc == 4) {
			/* Neues Fenster mit E-Mail-Bestätigungscode abfrage anzeigen */
		}
		CaBug.druckeLog("AendAnschriftAktiv=" + pAktionaersdatenSession.isAendAnschriftAktiv(), logDrucken, 10);
		eclDbM.closeAll();
		tSessionVerwaltung.setzeEnde();

	}

	/**********************************
	 * Kontaktdaten ändern
	 **********************************************/
	public void doKontaktdatenAendern() {
		if (!tSessionVerwaltung.pruefeStart(KonstPortalView.P_AKTIONAERSDATEN)) {
			return;
		}
		eclDbM.openAll();
		brMAktionaersdaten.kontaktdatenAendernVorbereiten(eclLoginDatenM.getEclLoginDaten().loginKennung);
		pAktionaersdatenSession.setAendKontaktdatenAktiv(true);
		eclDbM.closeAll();
		tSessionVerwaltung.setzeEnde();
	}

	public void doKontaktdatenAendernAbbrechen() {
		if (!tSessionVerwaltung.pruefeStart(KonstPortalView.P_AKTIONAERSDATEN)) {
			return;
		}
		pAktionaersdatenSession.setAendKontaktdatenAktiv(false);
		tSession.trageQuittungTextNr(1601, 2);
		tSessionVerwaltung.setzeEnde();
	}

	public void doKontaktdatenAendernSpeichern() {
		if (!tSessionVerwaltung.pruefeStart(KonstPortalView.P_AKTIONAERSDATEN)) {
			return;
		}
		eclDbM.openAll();
		int rc = brMAktionaersdaten.kontaktdatenAendernSpeichern(eclLoginDatenM.getEclLoginDaten().loginKennung);
        tRemoteAR.pruefeVerfuegbar(rc);
		CaBug.druckeLog("rc kontaktdatenAendernSpeichern=" + rc, logDrucken, 10);
		if (rc == 1 || rc == 2 || rc == 3) {
			pAktionaersdatenSession.setAendKontaktdatenAktiv(false);
			/*
			 * Nun noch Meldung setzen: 1= erledigt Text 1598 2="Vollmacht nachreichen" Text
			 * 1599 3=Keine Änderung gespeichert, Text 1600
			 */
			switch (rc) {
			case 1:
				tSession.trageQuittungTextNr(1737, 2);
				break;
			case 2:
				tSession.trageQuittungTextNr(1599, 2);
				break;
			case 3:
				tSession.trageQuittungTextNr(1600, 2);
				break;
			}
            init(false);
		}
		eclDbM.closeAll();
		tSessionVerwaltung.setzeEnde();

	}

	public void doBestaetigungsCodeAbbrechen() {
		CaBug.druckeLog("", logDrucken, 10);
		if (!tSessionVerwaltung.pruefeStart(KonstPortalView.P_AKTIONAERSDATEN)) {
			return;
		}
		pAktionaersdatenSession.setAendBankverbindungAktiv(false);
		pAktionaersdatenSession.setAendKontaktdatenAktiv(false);
		pAktionaersdatenSession.setAendBestaetigungscodeEingeben(false);

		tSession.trageQuittungTextNr(1601, 2);
		tSessionVerwaltung.setzeEnde();
	}

	public void doBestaetigungsCodeSpeichern() {
		if (!tSessionVerwaltung.pruefeStart(KonstPortalView.P_AKTIONAERSDATEN)) {
			return;
		}
		eclDbM.openAll();
		int rc = brMAktionaersdaten.kontaktdatenAendernSpeichern(eclLoginDatenM.getEclLoginDaten().loginKennung);
        tRemoteAR.pruefeVerfuegbar(rc);
		CaBug.druckeLog("rc kontaktdatenAendernSpeichern=" + rc, logDrucken, 10);
		if (rc == 1 || rc == 2 || rc == 3) {
			pAktionaersdatenSession.setAendKontaktdatenAktiv(false);
			pAktionaersdatenSession.setAendBestaetigungscodeEingeben(false);
			/*
			 * Nun noch Meldung setzen: 1= erledigt Text 1598 2="Vollmacht nachreichen" Text
			 * 1599 3=Keine Änderung gespeichert, Text 1600
			 */
			switch (rc) {
			case 1:
				tSession.trageQuittungTextNr(1737, 2);
				break;
			case 2:
				tSession.trageQuittungTextNr(1599, 2);
				break;
			case 3:
				tSession.trageQuittungTextNr(1600, 2);
				break;
			}
			init(false);
		}
		eclDbM.closeAll();
		tSessionVerwaltung.setzeEnde();

	}

	/**********************************
	 * Geburtsdatum ändern
	 **********************************************/

	/**
	 * Darf nur aufgerufen werden, wenn nur eine weitere Person zugeordnet ist
	 * (Stift oben in Leiste)
	 */
	public void doGeburtsdatumAendern() {
		doGeburtsdatumAendernMehrere(pAktionaersdatenSession.getZugeordneteWeiterPersonen().get(0));
	}

	public void doGeburtsdatumAendernMehrere(EclAktienregisterWeiterePerson pPersonZumAendern) {
		if (!tSessionVerwaltung.pruefeStart(KonstPortalView.P_AKTIONAERSDATEN)) {
			return;
		}
		eclDbM.openAll();
		brMAktionaersdaten.geburtsdatumAendernVorbereiten(eclLoginDatenM.getEclLoginDaten().loginKennung,
				pPersonZumAendern);
		pAktionaersdatenSession.setAendGeburtsdatumAktiv(true);
		eclDbM.closeAll();
		tSessionVerwaltung.setzeEnde();
	}

	public void doGeburtsdatumAendernAbbrechen() {
		System.out.println(
				"===============================================doGeburtsdatumAendernAbbrechen======================");
		CaBug.druckeLog("", logDrucken, 10);
		if (!tSessionVerwaltung.pruefeStart(KonstPortalView.P_AKTIONAERSDATEN)) {
			return;
		}
		pAktionaersdatenSession.setAendGeburtsdatumAktiv(false);
		tSession.trageQuittungTextNr(1601, 2);
		tSessionVerwaltung.setzeEnde();
	}

	public void doGeburtsdatumAendernSpeichern() {
		if (!tSessionVerwaltung.pruefeStart(KonstPortalView.P_AKTIONAERSDATEN)) {
			return;
		}
		eclDbM.openAll();
		int rc = brMAktionaersdaten.geburtsdatumAendernSpeichern(eclLoginDatenM.getEclLoginDaten().loginKennung);
        tRemoteAR.pruefeVerfuegbar(rc);
		CaBug.druckeLog("rc geburtsdatumAendernSpeichern=" + rc, logDrucken, 10);
		if (rc == 1 || rc == 2 || rc == 3) {
			pAktionaersdatenSession.setAendGeburtsdatumAktiv(false);
			/*
			 * Nun noch Meldung setzen: 1= erledigt Text 1598 2="Vollmacht nachreichen" Text
			 * 1599 3=Keine Änderung gespeichert, Text 1600
			 */
			switch (rc) {
			case 1:
				tSession.trageQuittungTextNr(1598, 2);
				break;
			case 2:
				tSession.trageQuittungTextNr(1599, 2);
				break;
			case 3:
				tSession.trageQuittungTextNr(1600, 2);
				break;
			}
		}
		eclDbM.closeAll();
		tSessionVerwaltung.setzeEnde();

	}

	/**********************************
	 * SteuerId ändern
	 **********************************************/

	/**
	 * Darf nur aufgerufen werden, wenn nur eine weitere Person zugeordnet ist
	 * (Stift oben in Leiste)
	 */
	public void doSteuerIdAendern() {
		doSteuerIdAendernMehrere(pAktionaersdatenSession.getZugeordneteWeiterPersonen().get(0));
	}

	public void doSteuerIdAendernMehrere(EclAktienregisterWeiterePerson pPersonZumAendern) {
		if (!tSessionVerwaltung.pruefeStart(KonstPortalView.P_AKTIONAERSDATEN)) {
			return;
		}
		eclDbM.openAll();
		brMAktionaersdaten.steuerIdAendernVorbereiten(eclLoginDatenM.getEclLoginDaten().loginKennung,
				pPersonZumAendern);
		pAktionaersdatenSession.setAendSteuerIdAktiv(true);
		eclDbM.closeAll();
		tSessionVerwaltung.setzeEnde();
	}

	public void doSteuerIdAendernAbbrechen() {
		if (!tSessionVerwaltung.pruefeStart(KonstPortalView.P_AKTIONAERSDATEN)) {
			return;
		}
		pAktionaersdatenSession.setAendSteuerIdAktiv(false);
		tSession.trageQuittungTextNr(1601, 2);
		tSessionVerwaltung.setzeEnde();
	}

	public void doSteuerIdAendernSpeichern() {
		if (!tSessionVerwaltung.pruefeStart(KonstPortalView.P_AKTIONAERSDATEN)) {
			return;
		}
		eclDbM.openAll();
		int rc = brMAktionaersdaten.steuerIdAendernSpeichern(eclLoginDatenM.getEclLoginDaten().loginKennung);
        tRemoteAR.pruefeVerfuegbar(rc);
		CaBug.druckeLog("rc steuerIdAendernSpeichern=" + rc, logDrucken, 10);
		if (rc == 1 || rc == 2 || rc == 3) {
			pAktionaersdatenSession.setAendSteuerIdAktiv(false);
			/*
			 * Nun noch Meldung setzen: 1= erledigt Text 1598 2="Vollmacht nachreichen" Text
			 * 1599 3=Keine Änderung gespeichert, Text 1600
			 */
			switch (rc) {
			case 1:
				tSession.trageQuittungTextNr(1598, 2);
				break;
			case 2:
				tSession.trageQuittungTextNr(1599, 2);
				break;
			case 3:
				tSession.trageQuittungTextNr(1600, 2);
				break;
			}
		}
		eclDbM.closeAll();
		tSessionVerwaltung.setzeEnde();

	}


    /**********************************
	 * Bankverbindung ändern
	 **********************************************/

	public void doSetAendBank() {

	    String blz = "";

	    String ibanEingabe=CaBank.ibanZuIntern(pAktionaersdatenSession.getAendIban());
	    if (ibanEingabe.length() == 22) {

	        blz = ibanEingabe.substring(4, 12);

	        eclDbM.openAll();
	        eclDbM.openWeitere();

	        eclDbM.getDbBundle().dbBlzBank.mandantenabhaengig = true;
	        eclDbM.getDbBundle().dbBlzBank.readBlz(blz);
	        if (eclDbM.getDbBundle().dbBlzBank.anzErgebnis() > 0) {
	            pAktionaersdatenSession.setAendBankname(eclDbM.getDbBundle().dbBlzBank.ergebnisPosition(0).bankname);
	            pAktionaersdatenSession.setAendBic(eclDbM.getDbBundle().dbBlzBank.ergebnisPosition(0).bic);
	        } else {
	            pAktionaersdatenSession.setAendBankname("");
	            pAktionaersdatenSession.setAendBic("");

	        }
	        eclDbM.getDbBundle().dbBlzBank.mandantenabhaengig = false;


	        eclDbM.closeAll();
	    }

        pAktionaersdatenSession.setAendIbanGeprueft(ibanEingabe);

	}

    public void doBankverbindungAendern() {
		if (!tSessionVerwaltung.pruefeStart(KonstPortalView.P_AKTIONAERSDATEN)) {
			return;
		}
		eclDbM.openAll();
		brMAktionaersdaten.bankverbindungAendernVorbereiten(eclLoginDatenM.getEclLoginDaten().loginKennung);
		pAktionaersdatenSession.setAendBankverbindungAktiv(true);
        pAktionaersdatenSession.setAendIbanGeprueft(CaBank.ibanZuIntern(pAktionaersdatenSession.getAendIban()));

		eclDbM.closeAll();
		tSessionVerwaltung.setzeEnde();
	}

	public void doBankverbindungAendernAbbrechen() {
		if (!tSessionVerwaltung.pruefeStart(KonstPortalView.P_AKTIONAERSDATEN)) {
			return;
		}
		pAktionaersdatenSession.setAendBankverbindungAktiv(false);
		tSession.trageQuittungTextNr(1601, 2);
		tSessionVerwaltung.setzeEnde();
	}

	public void doBankverbindungAendernSpeichern() {
		if (!tSessionVerwaltung.pruefeStart(KonstPortalView.P_AKTIONAERSDATEN)) {
			return;
		}
		
		/*Ggf noch Iban-Überprüfung veranlassen, falls noch nicht erfolgt*/
		if (!pAktionaersdatenSession.getAendIbanGeprueft().equals(CaBank.ibanZuIntern(pAktionaersdatenSession.getAendIban()))){
		    doSetAendBank();
		}
		
		eclDbM.openAll();
		int rc = brMAktionaersdaten.bankverbindungAendernSpeichern(eclLoginDatenM.getEclLoginDaten().loginKennung);
        tRemoteAR.pruefeVerfuegbar(rc);
		CaBug.druckeLog("rc bankverbindungAendernSpeichern=" + rc, logDrucken, 10);
		if (rc == 1 || rc == 2 || rc == 3) {
			pAktionaersdatenSession.setAendBankverbindungAktiv(false);
			/*
			 * Nun noch Meldung setzen: 1= erledigt Text 1598 2="Vollmacht nachreichen" Text
			 * 1599 3=Keine Änderung gespeichert, Text 1600
			 */
			switch (rc) {
			case 1:
				tSession.trageQuittungTextNr(1598, 2);
				break;
			case 2:
				tSession.trageQuittungTextNr(1599, 2);
				break;
			case 3:
				tSession.trageQuittungTextNr(1600, 2);
				break;
			}
		}
		eclDbM.closeAll();
		tSessionVerwaltung.setzeEnde();

	}

	/********************************************************************************************/

}
