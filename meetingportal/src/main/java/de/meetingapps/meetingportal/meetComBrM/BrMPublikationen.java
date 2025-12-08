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

import de.meetingapps.meetingportal.meetComAllg.CaBug;
import de.meetingapps.meetingportal.meetComAllg.CaDatumZeit;
import de.meetingapps.meetingportal.meetComAllg.CaFehler;
import de.meetingapps.meetingportal.meetComBl.BlAuftragAnbindungAktienregister;
import de.meetingapps.meetingportal.meetComBl.BlNummernformBasis;
import de.meetingapps.meetingportal.meetComEclM.EclDbM;
import de.meetingapps.meetingportal.meetComEntitiesGenossenschaftSys.EgxGetPersoenlicheDatenRC;
import de.meetingapps.meetingportal.meetComEntitiesGenossenschaftSys.EgxPostNewsletter;
import de.meetingapps.meetingportal.meetComEntitiesGenossenschaftSys.EgxPostNewsletterRC;
import de.meetingapps.meetingportal.meetComKonst.KonstAuftragArt;
import de.meetingapps.meetingportal.meetComKonst.KonstAuftragModul;
import de.meetingapps.meetingportal.meetingportTController.TPermanentSession;
import de.meetingapps.meetingportal.meetingportTController.TPublikationenSession;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@RequestScoped
@Named
public class BrMPublikationen {
	private int logDrucken = 10;

	private @Inject EclDbM eclDbM;
	private @Inject TPermanentSession tPermanentSession;
	private @Inject TPublikationenSession tPublikationenSession;

	private @Inject BrMGenossenschaftCall brMGenossenschaftCall;

	/** pAktionaersnummer muß im Format "für intern" sein */
	public int holeAktuelleEinstellungen(String pAktionaersnummer) {

		/* Aktionärsnummer aufbereiten */
		BlNummernformBasis blNummernformBasis = new BlNummernformBasis(eclDbM.getDbBundle());
//        String aktionaersnummerFuerRegister=blNummernformBasis.aufbereitenKennungFuerRegisterzugriff(pAktionaersnummer);
		String aktionaersnummerFuerGenossenschaftSysWebrequest = blNummernformBasis
				.aufbereitenKennungFuerGenossenschaftSysWebrequest(pAktionaersnummer);

		String versandart = "";

		if (tPermanentSession.isTestModus()) {
			versandart = "2";
		} else {
			/* Über Schnittstelle Aktionärsdaten (allgemein) holen */
			/* get persönliche Daten */
			EgxGetPersoenlicheDatenRC egxGetPersoenlicheDatenRC = brMGenossenschaftCall
					.doGetRequestPersoenlicheDaten(aktionaersnummerFuerGenossenschaftSysWebrequest);
            if (egxGetPersoenlicheDatenRC==null) {
                return CaFehler.perRemoteAktienregisterNichtVerfuegbar;
            }

			switch (egxGetPersoenlicheDatenRC.kein_newsletter) {
			case "J":
				versandart = "3";
				break;

			case "N":
				versandart = egxGetPersoenlicheDatenRC.versandart;
				if(versandart.equals("")) {
					versandart = "2";
				}
				break;

			case "U":
				versandart = "2";
				break;
			}
		}

		tPublikationenSession.setVersandartNewsletter(versandart);

		/*
		 * Nun noch überprüfen, ob ggf. bereits noch nicht verarbeitete Aufträge
		 * vorliegen
		 */
		return 1;
	}

	public int schreibeEinstellungen(String pAktionaersnummer, int pUserId, int pVersandart) {

		/* Aktionärsnummer aufbereiten */
		BlNummernformBasis blNummernformBasis = new BlNummernformBasis(eclDbM.getDbBundle());
		String aktionaersnummerFuerGenossenschaftSysWebrequest = blNummernformBasis
				.aufbereitenKennungFuerGenossenschaftSysWebrequest(pAktionaersnummer);

		String lSchluessel = "";
		String lZeit = CaDatumZeit.DatumZeitStringFuerDatenbank();

		if (tPermanentSession.isTestModus()) {
			lSchluessel = "123456789";
		} else {
			EgxPostNewsletter egxPostNewsletter = new EgxPostNewsletter();
			String versandart = "";
			if (pVersandart != 3) {
				if (pVersandart == 1) {
					versandart = "2 - Post";
				} else if (pVersandart == 2) {
					versandart = "1 - E-Mail";
				}
				egxPostNewsletter.kein_newsletter = "N";
				egxPostNewsletter.versandart = versandart;
			} else {
				egxPostNewsletter.kein_newsletter = "J";
			}
			CaBug.druckeLog("++++++++++++++++++++++++++++++++++++ " + egxPostNewsletter.kein_newsletter, logDrucken,
					10);
			CaBug.druckeLog("++++++++++++++++++++++++++++++++++++ " + egxPostNewsletter.versandart, logDrucken, 10);

			EgxPostNewsletterRC egxPostNewsletterRC = brMGenossenschaftCall
					.doPostRequestNewsletter(aktionaersnummerFuerGenossenschaftSysWebrequest, egxPostNewsletter);
            if (egxPostNewsletterRC==null) {
                return CaFehler.perRemoteAktienregisterNichtVerfuegbar;
            }
			CaBug.druckeLog(egxPostNewsletterRC.toString(), logDrucken, 10);
		}

		BlAuftragAnbindungAktienregister lAuftragAnbindungAktienregister = new BlAuftragAnbindungAktienregister(
				eclDbM.getDbBundle());
		lAuftragAnbindungAktienregister.aendernNewsletter(pAktionaersnummer, pUserId, lSchluessel, lZeit, pVersandart);
		return 1;
	}

	public int pruefeObAenderungenInArbeit(String pAktionaersnummer, int pUserId) {

		int anz = eclDbM.getDbBundle().dbAuftrag.read_aenderungenInArbeit(pUserId,
				KonstAuftragModul.ANBINDUNG_AKTIENREGISTER,
				KonstAuftragArt.ANBINDUNG_AKTIENREGISTER_AENDERN_NEWSLETTER);
		if (anz > 0) {
			tPublikationenSession.setAenderungenInArbeit(true);
		} else {
			tPublikationenSession.setAenderungenInArbeit(false);
		}

		return 1;
	}

}
