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

import java.util.ArrayList;
import java.util.List;

import de.meetingapps.meetingportal.meetComAllg.CaBug;
import de.meetingapps.meetingportal.meetComAllg.CaDatumZeit;
import de.meetingapps.meetingportal.meetComAllg.CaFehler;
import de.meetingapps.meetingportal.meetComAllg.CaPasswortErzeugen;
import de.meetingapps.meetingportal.meetComAllg.CaPasswortVerschluesseln;
import de.meetingapps.meetingportal.meetComBl.BlAktienregisterImport;
import de.meetingapps.meetingportal.meetComBl.BlNummernformBasis;
import de.meetingapps.meetingportal.meetComBl.BlWillenserklaerung;
import de.meetingapps.meetingportal.meetComBr.BrLiefereMitgliedArt;
import de.meetingapps.meetingportal.meetComBr.BrLieferePersonenListe;
import de.meetingapps.meetingportal.meetComEclM.EclDbM;
import de.meetingapps.meetingportal.meetComEclM.EclParamM;
import de.meetingapps.meetingportal.meetComEntities.EclAktienregister;
import de.meetingapps.meetingportal.meetComEntities.EclAktienregisterErgaenzung;
import de.meetingapps.meetingportal.meetComEntities.EclAktienregisterWeiterePerson;
import de.meetingapps.meetingportal.meetComEntities.EclLoginDaten;
import de.meetingapps.meetingportal.meetComEntities.EclStaaten;
import de.meetingapps.meetingportal.meetComEntitiesGenossenschaftSys.EgxGetMitgliedJNRC;
import de.meetingapps.meetingportal.meetComEntitiesGenossenschaftSys.EgxGetPersoenlicheDatenRC;
import de.meetingapps.meetingportal.meetComEntitiesGenossenschaftSys.EgxGetPersonendatenRC;
import de.meetingapps.meetingportal.meetComEntitiesGenossenschaftSys.EgxGetPersonendatenResult;
import de.meetingapps.meetingportal.meetComKonst.KonstAktienregisterErgaenzung;
import de.meetingapps.meetingportal.meetComKonst.KonstLoginKennungArt;
import de.meetingapps.meetingportal.meetingportTController.PAktionaersdatenSession;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@RequestScoped
@Named
public class BrMARRemoteZuARLokal {

	private int logDrucken = 10;

    private @Inject EclParamM eclParamM;
	private @Inject EclDbM eclDbM;
	private @Inject BrMGenossenschaftCall brMGenossenschaftCall;
    private @Inject BrMLogin brMLogin;
    private @Inject BrMAktionaersdaten brMAktionaersdaten;

    private @Inject PAktionaersdatenSession pAktionaersdatenSession;

    public int pruefeAR(String pAktionaersnummer) {
        BlNummernformBasis blNummernformBasis = new BlNummernformBasis(eclDbM.getDbBundle());
        String aktionaersnummer = blNummernformBasis.loginKennungAufbereitenFuerIntern(pAktionaersnummer);
        String aktionaersnummerFuerGenossenschaftSysWebrequest = blNummernformBasis.aufbereitenKennungFuerGenossenschaftSysWebrequest(aktionaersnummer);

        EgxGetMitgliedJNRC egxMitgliedJNRC = brMGenossenschaftCall.doGetRequestMitgliedJN(aktionaersnummerFuerGenossenschaftSysWebrequest);
        if (egxMitgliedJNRC==null) {
            return CaFehler.perRemoteAktienregisterNichtVerfuegbar;
        }

        if (!egxMitgliedJNRC.mitgliedjn) {
            return -1;
        }
        return 1;
    }

    public int fuelleAR(String pAktionaersnummer) {
        /*Staaten einlesen*/
        List<EclStaaten> staatenListe=null;
        eclDbM.getDbBundle().dbStaaten.mandantenabhaengig=true;
        eclDbM.getDbBundle().dbStaaten.readAll(0);
        staatenListe=eclDbM.getDbBundle().dbStaaten.ergebnis();
        eclDbM.getDbBundle().dbStaaten.mandantenabhaengig=false;
        
        /*Aufrufen*/
        return fuelleAR(pAktionaersnummer, staatenListe);
        
    }
    
	public int fuelleAR(String pAktionaersnummer, List<EclStaaten> staatenListe) {
	    
	    /* gemacht, damit pAktionaersdaten gefüllt wird. Wird benötigt zum
	     * Setzen von Mitglieder-Gruppen-spezifischen Sachen
	     */
	    int rc=brMAktionaersdaten.holeAktuellenStand(pAktionaersnummer);
	    if (rc==CaFehler.perRemoteAktienregisterNichtVerfuegbar) {
	        return CaFehler.perRemoteAktienregisterNichtVerfuegbar;
	    }
	    
        BlNummernformBasis blNummernformBasis = new BlNummernformBasis(eclDbM.getDbBundle());
        String aktionaersnummerFuerGenossenschaftSysWebrequest = blNummernformBasis
                .aufbereitenKennungFuerGenossenschaftSysWebrequest(pAktionaersnummer);

        /**
         * Daten von GenossenschaftSys Abrufen
         */
        EgxGetPersoenlicheDatenRC egxGetPersoenlicheDatenRC = brMGenossenschaftCall
                .doGetRequestPersoenlicheDaten(aktionaersnummerFuerGenossenschaftSysWebrequest);
        if (egxGetPersoenlicheDatenRC==null) {
            return CaFehler.perRemoteAktienregisterNichtVerfuegbar;
        }

        CaBug.druckeLog(egxGetPersoenlicheDatenRC.toString(), logDrucken, 10);
        EgxGetPersonendatenRC egxGetPersonendatenRC = brMGenossenschaftCall
                .doGetRequestPersonendaten(aktionaersnummerFuerGenossenschaftSysWebrequest);
        if (egxGetPersonendatenRC==null) {
            return CaFehler.perRemoteAktienregisterNichtVerfuegbar;
        }
        CaBug.druckeLog(egxGetPersonendatenRC.toString(), logDrucken, 10);
        
        fuelleAR(pAktionaersnummer, staatenListe, egxGetPersoenlicheDatenRC, egxGetPersonendatenRC);
        return 1;
	}
	
	/**Aktionärsdaten sind bereits über BrmLogin.pruefeNachLogin eingelesen worden;
	 * Staaten sind noch nicht eingelesen
	 */
	public void fuelleARNachBrmLogin(String pAktionaersnummer) {
	    /*Staaten einlesen*/
	    List<EclStaaten> staatenListe=null;
	    eclDbM.getDbBundle().dbStaaten.mandantenabhaengig=true;
	    eclDbM.getDbBundle().dbStaaten.readAll(0);
	    staatenListe=eclDbM.getDbBundle().dbStaaten.ergebnis();
        eclDbM.getDbBundle().dbStaaten.mandantenabhaengig=false;
	    
	    /*Aufrufen*/
        fuelleAR(pAktionaersnummer, staatenListe, brMLogin.getRcEgxGetPersoenlicheDatenRC(), brMLogin.getRcEgxGetPersonendatenRC());

	}
	
	
	
	private void fuelleAR(String pAktionaersnummer, List<EclStaaten> staatenListe, EgxGetPersoenlicheDatenRC egxGetPersoenlicheDatenRC, EgxGetPersonendatenRC egxGetPersonendatenRC) {

	    CaBug.druckeLog("", logDrucken, 10);
	    
        BlNummernformBasis blNummernformBasis = new BlNummernformBasis(eclDbM.getDbBundle());
        String aktionaersnummerIntern=blNummernformBasis.loginKennungAufbereitenFuerIntern(pAktionaersnummer);
//        String aktionaersnummerFuerGenossenschaftSysWebrequest = blNummernformBasis
//                .aufbereitenKennungFuerGenossenschaftSysWebrequest(pAktionaersnummer);

		BlAktienregisterImport blAktienregisterImport = new BlAktienregisterImport(staatenListe);
		EclAktienregister lAktienregister = new EclAktienregister();
		EclAktienregisterErgaenzung lAktienregisterErgaenzung = new EclAktienregisterErgaenzung();

		lAktienregister.mandant = eclParamM.getClGlobalVar().mandant;
		lAktienregister.aktionaersnummer = blAktienregisterImport
				.helAktionaersnummerku178(/*aktionaersnummerFuerGenossenschaftSysWebrequest*/aktionaersnummerIntern);
		lAktienregister.anredeId = 0;
		lAktienregister.nachname = egxGetPersoenlicheDatenRC.nachname;
		lAktienregister.vorname = egxGetPersoenlicheDatenRC.vorname;
		lAktienregister.strasse = egxGetPersoenlicheDatenRC.adr_str;
		lAktienregister.postleitzahl = blAktienregisterImport.helPLZku178(
				String.valueOf(egxGetPersoenlicheDatenRC.adr_plz), egxGetPersoenlicheDatenRC.adr_landname);
		lAktienregister.ort = egxGetPersoenlicheDatenRC.adr_ort;
		lAktienregister.staatId = helFindeStaatByName(egxGetPersoenlicheDatenRC.adr_landname, staatenListe);
		lAktienregister.anredeIdVersand = blAktienregisterImport
				.helFindeAnredeku178(egxGetPersoenlicheDatenRC.adr_anrede);
		lAktienregister.nachnameVersand = egxGetPersoenlicheDatenRC.adr_nachname;
		lAktienregister.vornameVersand = egxGetPersoenlicheDatenRC.vorname;
		lAktienregister.strasseVersand = egxGetPersoenlicheDatenRC.adr_str;
		lAktienregister.postleitzahlVersand = blAktienregisterImport.helPLZku178(
				String.valueOf(egxGetPersoenlicheDatenRC.adr_plz), egxGetPersoenlicheDatenRC.adr_landname);
		lAktienregister.ortVersand = egxGetPersoenlicheDatenRC.adr_ort;
		
		String hStaat=egxGetPersoenlicheDatenRC.adr_landname;
		if (hStaat.trim().isEmpty()) {
		    hStaat="Deutschland";
		}
		lAktienregister.staatIdVersand = helFindeStaatByName(hStaat, staatenListe);
		CaBug.druckeLog("egxGetPersoenlicheDatenRC.adr_landname="+egxGetPersoenlicheDatenRC.adr_landname+" hStaat="+hStaat+" lAktienregister.staatIdVersand="+lAktienregister.staatIdVersand, logDrucken, 10);
		
		lAktienregister.nameKomplett = blAktienregisterImport.helCheckNameKomplettku178(lAktienregister.vorname,
				lAktienregister.nachname, lAktienregister.name1);
		lAktienregister.versandAbweichend = 0;
		lAktienregister.stueckAktien = 1;
		lAktienregister.stimmen = 1;
		lAktienregister.besitzart = "E";
		lAktienregister.stimmausschluss = "";
		lAktienregister.gattungId = 1;
		
		
		BrLieferePersonenListe brLieferePersonenListe =new BrLieferePersonenListe();
		brLieferePersonenListe.liefereAusGeDixListe(egxGetPersonendatenRC);
        List<EclAktienregisterWeiterePerson> zugeordneteWeiterPersonen=brLieferePersonenListe.rcZugeordneteWeiterPersonen;
		
		BrLiefereMitgliedArt brLiefereMitgliedArt=new BrLiefereMitgliedArt();
		lAktienregister.gruppe = brLiefereMitgliedArt.liefereMitgliedstatus(egxGetPersoenlicheDatenRC.vorname, egxGetPersoenlicheDatenRC.nachname, zugeordneteWeiterPersonen);
		
		
		final List<String> adresszeilen = blAktienregisterImport.helAdresszeilenAufbereitenku178(
				lAktienregister.vornameVersand, lAktienregister.nachnameVersand, lAktienregister.strasseVersand,
				lAktienregister.postleitzahlVersand, lAktienregister.ortVersand,
				helFindeStaatNameNeu(lAktienregister.staatIdVersand, staatenListe),
				egxGetPersoenlicheDatenRC.adr_adresszusatz);

		lAktienregister.adresszeile1 = adresszeilen.get(0).trim();
		lAktienregister.adresszeile2 = adresszeilen.get(1).trim();
		lAktienregister.adresszeile3 = adresszeilen.get(2).trim();
		lAktienregister.adresszeile4 = adresszeilen.get(3).trim();
		lAktienregister.adresszeile5 = adresszeilen.get(4).trim();
		lAktienregister.adresszeile6 = adresszeilen.get(5).trim();

		lAktienregister.versandNummer = 0;

		lAktienregisterErgaenzung.mandant = lAktienregister.mandant;

		List<String> geburtsdaten = new ArrayList<String>();

		if (egxGetPersonendatenRC.result.size() > 0) {
			for (EgxGetPersonendatenResult result : egxGetPersonendatenRC.result) {
				if (result.art.equals("natürliche Person (Mitglied / Teil des Mitglieds)")) {
					geburtsdaten.add(CaDatumZeit.datumJJJJ_MM_TTzuNormal(result.geburt));
				}
			}
		}
		
		if(geburtsdaten.size() > 0) {
			lAktienregisterErgaenzung.ergaenzungLangString[KonstAktienregisterErgaenzung.ku178_GeburtsdatumMitglied] = geburtsdaten.get(0);
			if(geburtsdaten.size() > 1) {
				lAktienregisterErgaenzung.ergaenzungLangString[KonstAktienregisterErgaenzung.ku178_GeburtsdatumEhegatte] = geburtsdaten.get(1);
			}
		}
		
		lAktienregisterErgaenzung.ergaenzungLangString[KonstAktienregisterErgaenzung.ku178_Name] = lAktienregister.nachname
				+ lAktienregister.nachname;
		lAktienregisterErgaenzung.ergaenzungLangString[KonstAktienregisterErgaenzung.ku178_Vorname] = lAktienregister.vorname;
		lAktienregisterErgaenzung.ergaenzungLangString[KonstAktienregisterErgaenzung.ku178_StrasseHausnummer] = lAktienregister.strasse;
		lAktienregisterErgaenzung.ergaenzungLangString[KonstAktienregisterErgaenzung.ku178_PLZ] = lAktienregister.postleitzahl;
		lAktienregisterErgaenzung.ergaenzungLangString[KonstAktienregisterErgaenzung.ku178_Ort] = lAktienregister.ort;
		lAktienregisterErgaenzung.ergaenzungLangString[KonstAktienregisterErgaenzung.ku178_Gruppe] = String
				.valueOf(lAktienregister.gruppe);
		
		EclLoginDaten loginDaten = new EclLoginDaten();

        int pwLaenge = eclParamM.getParam().paramPortal.passwortMindestLaenge;

        CaPasswortErzeugen caPasswortErzeugen = new CaPasswortErzeugen();

        loginDaten.loginKennung = lAktienregister.aktionaersnummer;
        loginDaten.kennungArt = KonstLoginKennungArt.aktienregister;
        loginDaten.passwortInitial = caPasswortErzeugen.generatePWInitial(pwLaenge, 2, 2, true);
        loginDaten.passwortVerschluesselt = CaPasswortVerschluesseln
                .verschluesselnAusInitialPW(loginDaten.passwortInitial, pwLaenge);
        if (pAktionaersdatenSession.renderZugangsdatenNurPerPost()==true) {
            loginDaten.dauerhafteRegistrierungUnzulaessig = 1;
        }
        else {
            loginDaten.dauerhafteRegistrierungUnzulaessig =0;
            loginDaten.eVersandRegistrierung=99;
        }
         
        /**Nun checken - schon vorhanden?*/
        eclDbM.getDbBundle().dbAktienregister.leseZuAktienregisternummer(aktionaersnummerIntern);
        int anzAktienregister=eclDbM.getDbBundle().dbAktienregister.anzErgebnis();
        if (anzAktienregister==0) {
            CaBug.druckeLog("Aktienregister komplett übertragen", logDrucken, 10);
            /*Noch nicht vorhanden - Einfügen*/
            
            /*nur zur Sicherheit - idents updaten*/
            eclDbM.getDbBundle().dbAktienregister.reorgInterneIdent();
            int rc=eclDbM.getDbBundle().dbAktienregister.insert(lAktienregister);
            CaBug.druckeLog("lAktienregister.aktionaersnummer="+lAktienregister.aktionaersnummer+" rc="+rc, logDrucken, 10);
            
            lAktienregisterErgaenzung.aktienregisterIdent=lAktienregister.aktienregisterIdent;
            eclDbM.getDbBundle().dbAktienregisterErgaenzung.insert(lAktienregisterErgaenzung);
            
            loginDaten.aktienregisterIdent=lAktienregister.aktienregisterIdent;
            eclDbM.getDbBundle().dbLoginDaten.insert(loginDaten);
            
            /**Nun noch für Generalversammlung anmelden*/
            BlWillenserklaerung lWillenserklaerung = new BlWillenserklaerung();
            lWillenserklaerung.pEclAktienregisterEintrag = lAktienregister;
            lWillenserklaerung.pAktienAnmelden = -1;
            lWillenserklaerung.pAnzahlAnmeldungen = 1;

            lWillenserklaerung.anmeldungAusAktienregister(eclDbM.getDbBundle());
            boolean brc = lWillenserklaerung.rcIstZulaessig;
            if (brc==false) {
                CaBug.drucke("001 "+lWillenserklaerung.rcGrundFuerUnzulaessig+" / "+CaFehler.getFehlertext(lWillenserklaerung.rcGrundFuerUnzulaessig, 0));
            }
            
        }
        else {
            /*Bereits vorhanden - nun Updaten*/
            /*Es wird nur Straße / ort upgedated, um Passwort-Versand richtig zu machen, und nur in Satz mit Endung 0*/
            EclAktienregister lAktienregisterAlt=null;
            for (int i=0;i<anzAktienregister;i++) {
               lAktienregisterAlt= eclDbM.getDbBundle().dbAktienregister.ergebnisPosition(i);
               if (lAktienregisterAlt.aktionaersnummer.endsWith("0")) {
                   /*Nun Adressdaten übertragen*/
                   lAktienregisterAlt.anredeId = lAktienregister.anredeId;
                   lAktienregisterAlt.nachname = lAktienregister.nachname;
                   lAktienregisterAlt.vorname = lAktienregister.vorname;
                   lAktienregisterAlt.strasse = lAktienregister.strasse;
                   lAktienregisterAlt.postleitzahl = lAktienregister.postleitzahl;
                   lAktienregisterAlt.ort = lAktienregister.ort;
                   lAktienregisterAlt.staatId = lAktienregister.staatId;
                   lAktienregisterAlt.nachnameVersand = lAktienregister.nachnameVersand;
                   lAktienregisterAlt.vornameVersand = lAktienregister.vornameVersand;
                   lAktienregisterAlt.strasseVersand = lAktienregister.strasseVersand;
                   lAktienregisterAlt.postleitzahlVersand = lAktienregister.postleitzahlVersand;
                   lAktienregisterAlt.ortVersand = lAktienregister.ortVersand;
                   lAktienregisterAlt.staatIdVersand = lAktienregister.staatIdVersand;
                   lAktienregisterAlt.nameKomplett = lAktienregister.nameKomplett;
                   lAktienregisterAlt.versandAbweichend = lAktienregister.versandAbweichend;
                   lAktienregisterAlt.adresszeile1 = lAktienregister.adresszeile1;
                   lAktienregisterAlt.adresszeile2 = lAktienregister.adresszeile2;
                   lAktienregisterAlt.adresszeile3 = lAktienregister.adresszeile3;
                   lAktienregisterAlt.adresszeile4 = lAktienregister.adresszeile4;
                   lAktienregisterAlt.adresszeile5 = lAktienregister.adresszeile5;
                   lAktienregisterAlt.adresszeile6 = lAktienregister.adresszeile6;
                   
                   eclDbM.getDbBundle().dbAktienregister.update(lAktienregisterAlt);

               }
                
            }
         }

	}

	private int helFindeStaatByName(String land, List<EclStaaten> staatenListe) {

		if (land.isBlank())
			return 56;

		EclStaaten staat = staatenListe.stream().filter(e -> e.nameDE.toLowerCase().equals(land.toLowerCase()))
				.findAny().orElse(null);

		return staat == null ? 1 : staat.id;
	}

	private String helFindeStaatNameNeu(int id, List<EclStaaten> staatenListe) {

		EclStaaten staat = staatenListe.stream().filter(e -> e.id == id).findAny().orElse(null);

		return staat == null ? "" : staat.nameDE;

	}


}
