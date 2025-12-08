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

import de.meetingapps.meetingportal.meetComAllg.CaBug;
import de.meetingapps.meetingportal.meetComAllg.CaDatumZeit;
import de.meetingapps.meetingportal.meetComAllg.CaFehler;
import de.meetingapps.meetingportal.meetComBa.BaMailM;
import de.meetingapps.meetingportal.meetComBl.BlEinsprungLinkPortal;
import de.meetingapps.meetingportal.meetComBl.BlTeilnehmerLoginNeu;
import de.meetingapps.meetingportal.meetComBrM.BrMAktionaersdaten;
import de.meetingapps.meetingportal.meetComEclM.EclBesitzGesamtM;
import de.meetingapps.meetingportal.meetComEclM.EclDbM;
import de.meetingapps.meetingportal.meetComEclM.EclLoginDatenM;
import de.meetingapps.meetingportal.meetComEclM.EclParamM;
import de.meetingapps.meetingportal.meetComEclM.EclPortalTexteM;
import de.meetingapps.meetingportal.meetComKonst.KonstPortalView;
import de.meetingapps.meetingportal.meetingportTFunktionen.TRemoteAR;
import de.meetingapps.meetingportal.meetingportTFunktionen.TSessionVerwaltung;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;



/*******************************************Grundsätzliches**********************************************
 * Aktionärsportal versus Permanent-Portal
 * 
 * Aktionärsportal:
 * > E-Mail optional möglich / optional zwingend
 * > E-Versand optional möglich
 * > eigenes Passwort optional möglich
 * 
 * Permanentportal:
 * > E-Mail zwingend. Lokal gespeichert, synchronisiert mit Aktienregister
 * > E-Versand nicht möglich
 * > eigenes Passwort zwingend.
 *
 * 
 *
 * Beim Start des Permanent-Portals ist damit abzufangen:
 * Variante "bisher nicht im Aktienregister"
 * > Daten von Aktienregister holen und Belegen
 * > Passwort schicken lassen
 * > Start "wie normal".
 * 
 * Start
 * > Abfragen E-Mail:
 * 
 * -- bereits E-Mail hinterlegt - im Portal, im Aktienregister 
 * -- bereits E-Mail bestätigt?
 * -- wenn keine E-Mail hinterlegt, oder zwei E-Mails nicht zusammenpassen, oder E-Mail nicht bestätigt:
 *      > E-Mail abfrage und bestätigen lassen
 * > Abfragen Passwort:
 * 
 * -- noch kein dauerhaftes Passwort vergeben: Passwort zwingend vergeben
 * 
 * > Alle erforderlichen Bestätigungen abfragen, falls noch nicht erfolgt
 */


@RequestScoped
@Named
public class TEinstellungen {

    private int logDrucken=10;
    
    @Inject
    private EclDbM eclDbM;
    private @Inject EclParamM eclParamM;
    @Inject
    private EclPortalTexteM eclTextePortalM;
    private @Inject TEinstellungenSession tEinstellungenSession;
    private @Inject TSession tSession;
    private @Inject TLanguage tLanguage;
    private @Inject EclLoginDatenM eclLoginDatenM;
    private @Inject TLinkSession tLinkSession;
    private @Inject TMenue tMenue;

    private @Inject TLoginLogoutSession tLoginLogoutSession;
    private @Inject TLoginLogout tLoginLogout;

    private @Inject BaMailM baMailm;

    private @Inject TSessionVerwaltung tSessionVerwaltung;

    private @Inject TAuswahl tAuswahl;

    private @Inject BrMAktionaersdaten brMAktionaersdaten;

    private @Inject TRemoteAR tRemoteAR;

    private @Inject TAuswahlSession tAuswahlSession;
    private @Inject TPraesenzZugangAbgang tPraesenzZugangAbgang;
    private @Inject EclBesitzGesamtM eclBesitzGesamtM;

    /*+++++++++++++++++++++Start-Sequenzen+++++++++++++++++++++++++++*/

    /**eclDbM-Handling in aufrufender Funktion*/
    public int startNurDatenVorbereiten() {
        /*Logik vorbereiten*/
        BlTeilnehmerLoginNeu blTeilnehmerLogin = new BlTeilnehmerLoginNeu(tSession.isPermanentPortal());
        blTeilnehmerLogin.initDB(eclDbM.getDbBundle());
        eclLoginDatenM.copyToForReload(blTeilnehmerLogin);
        int erg = blTeilnehmerLogin.reloadKennung();
        if (erg < 0) {
            /*Fehler aufgetreten*/
            return erg;
        }
        blTeilnehmerLogin.bereiteRegistrierungVor();
        tEinstellungenSession.initialisiere();
        tEinstellungenSession.copyFromVorEinstellungen(blTeilnehmerLogin);
        return 1;

    }

    /**Funktion wird aufgerufen aus verschiedenen Masken, um Maske Einstellungen aufzurufen
     * eclDbM-Handling in aufrufender Funktion.
     * 
     * Return-Wert:
     * <0 => 
     * 		afFunktionNichtAuswaehlbar
     * 		Sonst:
     * 		tSession.trageFehlerEin(erg);
     * 		tFunktionen.setzeEnde("tDlgFehlerSysLogout", false, false);
     * >0 => 
     * 		tFunktionen.setzeEnde("tEinstellungen", true, false);
     * */
    public int startRegistrierungAusAuswahl() {

        int erg = startNurDatenVorbereiten();
        if (erg < 0) {
            /*Fehler aufgetreten*/
            return erg;
        }
        tEinstellungenSession.setErstregistrierung(false);
        return 1;
    }

    /**Funktion wird nach Login aufgerufen, bevor Registrierungsseite aufgerufen wird
     * eclDbM wird nicht benötigt*/
    public void startRegistrierungNachLogin(BlTeilnehmerLoginNeu pBlTeilnehmerLogin) {
        pBlTeilnehmerLogin.bereiteRegistrierungVor();
        tEinstellungenSession.initialisiere();
        tEinstellungenSession.copyFromVorEinstellungen(pBlTeilnehmerLogin);
        tEinstellungenSession.setErstregistrierung(true);

    }

    /*++++++++++++++++Buttons aus tEinstellungen heraus++++++++++++++++++++++++*/
    /**Wird aufgerufen aus der Maske Registrieren oder aus der Maske aEinstellungen*/
    public void doWeiter() {
        if (!tSessionVerwaltung.pruefeStart(liefereSeiteEinstellungen())) {
            return;
        }
        tSession.clearFehler();
        eclDbM.openAll();
        int rc = pruefenUndSpeichern(0);
        /*int auswahlView = */ tAuswahl.startAuswahl(true);
        
        if (eclParamM.liefereZuschaltungHVAutomatischNachLogin() && tAuswahlSession.isOnlineteilnahmeAktiv() 
                && eclParamM.getParam().paramPortal.varianteDialogablauf==0
                && eclBesitzGesamtM.isKennungIstOnlinePraesent()==false) {
            tPraesenzZugangAbgang.initVirtuelleHV();
            tPraesenzZugangAbgang.zugangBuchenVirtuelleHV();
            tAuswahl.startAuswahl(true);
       }
        eclDbM.closeAll();
        if (rc < 0) { 
            if (rc == CaFehler.afAndererUserAktiv) {
                tSessionVerwaltung.setzeEnde(KonstPortalView.FEHLER_VERAENDERT);
                return;
            }
            if (rc == CaFehler.perRemoteAktienregisterNichtVerfuegbar) {
                tSessionVerwaltung.setzeEnde();
                return;
            }
            tSession.trageFehlerEin(rc);
            tSessionVerwaltung.setzeEnde(liefereSeiteEinstellungen());
            return;
        }
        
        if (rc==KonstPortalView.P_AUSWAHL) {
            rc=tMenue.init();
        }

        tSessionVerwaltung.setzeEnde(rc);
        return;
    }

    /**E-Mail Link erneut verschicken*/
    public void doEmailLink() {
        if (!tSessionVerwaltung.pruefeStart(liefereSeiteEinstellungen())) {
            return;
        }
        eclDbM.openAll();
        int rc=pruefenUndSpeichern(1);
        if (rc<0) {
            eclDbM.closeAll();
            tSession.trageFehlerEin(rc);
            tSessionVerwaltung.setzeEnde(liefereSeiteEinstellungen());
            return;
        }
        startNurDatenVorbereiten();
        eclDbM.closeAll();
        tSession.trageFehlerEin(CaFehler.afEMailWurdeErneutVerschickt);

        tSessionVerwaltung.setzeEnde(liefereSeiteEinstellungen());
        return;
    }

    /**Aus Maske aEinstellungen oder aRegistrierung*/
    public void doEmail2Link() {
        if (!tSessionVerwaltung.pruefeStart(liefereSeiteEinstellungen())) {
            return;
        }
        eclDbM.openAll();
        int rc=pruefenUndSpeichern(2);
        if (rc<0) {
            eclDbM.closeAll();
            tSession.trageFehlerEin(rc);
            tSessionVerwaltung.setzeEnde(liefereSeiteEinstellungen());
            return;
        }
        startNurDatenVorbereiten();
        eclDbM.closeAll();
        tSession.trageFehlerEin(CaFehler.afEMailWurdeErneutVerschickt);
        tSessionVerwaltung.setzeEnde(liefereSeiteEinstellungen());
        return;
    }

    
    public String doZumRegisterPortal() {
        if (!tSessionVerwaltung.pruefeStart(liefereSeiteEinstellungen())) {
            return "";
        }
 
        tLoginLogoutSession.setLoginKennung(eclLoginDatenM.getAnmeldeKennungFuerAnzeige());
        tLoginLogoutSession.setLoginPasswort(eclLoginDatenM.getEclLoginDaten().passwortVerschluesselt);
        
        tSessionVerwaltung.setPermanentPortal("1");
        tLoginLogout.doLoginAusfuehren();
        tMenue.clearMenue();
        tSession.setUserEingeloggt("2");
        return "mitgliederportal.xhtml";
    }
    
    /*+++++++++++++++++++++Buttons aus tEinstellungenBestaetigung++++++++++++*/
    public void doBestaetigungWeiter() {
        if (!tSessionVerwaltung.pruefeStart(liefereSeiteEinstellungen_Bestaetigung())) {
            return;
        }
        
        if (tEinstellungenSession.isQuittungNeueEmailAdresseEingetragen()) {
            if (tEinstellungenSession.geteMailBestaetigungsCodeBestaetigung().trim().isEmpty() && eclParamM.getParam().paramPortal.emailBestaetigenIstZwingend==1) {
                tSession.trageFehlerEin(CaFehler.afEmailBestaetigungErforderlich);
                tSessionVerwaltung.setzeEnde();
                return;
            }
        }
        
        eclDbM.openAll();
        boolean emailAnRemoteRegister=false;
        if (!tEinstellungenSession.geteMailBestaetigungsCodeBestaetigung().trim().isEmpty() ||
                (!tEinstellungenSession.geteMail2BestaetigungsCodeBestaetigung().trim().isEmpty())
                ) {
            BlTeilnehmerLoginNeu blTeilnehmerLogin = new BlTeilnehmerLoginNeu(tSession.isPermanentPortal());
            blTeilnehmerLogin.initDB(eclDbM.getDbBundle());
            eclLoginDatenM.copyToForReload(blTeilnehmerLogin);
            int rc=blTeilnehmerLogin.bestaetigungsCodeBestaetigungsSeite(
                    tEinstellungenSession.geteMailBestaetigungsCodeBestaetigung(),
                    tEinstellungenSession.geteMail2BestaetigungsCodeBestaetigung());
            if (rc<0) {
                eclDbM.closeAll();
                tSession.trageFehlerEin(rc);
                tSessionVerwaltung.setzeEnde();
                return;
            }
            eclLoginDatenM.copyEclLoginDatenFrom(blTeilnehmerLogin);
            emailAnRemoteRegister=true;
        }
        
        if (emailAnRemoteRegister) {
            if (tSession.isPermanentPortal()) {
                int rc=brMAktionaersdaten.mailAnGenossenschaftSys(eclLoginDatenM.getEclLoginDaten().loginKennung, eclLoginDatenM.getEclLoginDaten().eMailFuerVersand, CaDatumZeit.DatumZeitStringFuerDatenbank());
                if (tRemoteAR.pruefeVerfuegbar(rc)==false) {
                   eclDbM.closeAll();
                   tSessionVerwaltung.setzeEnde();
                   return;
                }

            }
        }

        int auswahlView = tAuswahl.startAuswahl(true);
        
        if (eclParamM.liefereZuschaltungHVAutomatischNachLogin() && tAuswahlSession.isOnlineteilnahmeAktiv() 
                && eclParamM.getParam().paramPortal.varianteDialogablauf==0
                && eclBesitzGesamtM.isKennungIstOnlinePraesent()==false) {
            tPraesenzZugangAbgang.initVirtuelleHV();
            tPraesenzZugangAbgang.zugangBuchenVirtuelleHV();
            tAuswahl.startAuswahl(true);
       }

        
        eclDbM.closeAll();
        if (auswahlView < 0) {
            tSession.trageFehlerEin(auswahlView);
            tSessionVerwaltung.setzeEnde(KonstPortalView.fehlerSysLogout);
            return;
        }

        if (auswahlView==KonstPortalView.P_AUSWAHL) {
            auswahlView=tMenue.init();
        }

        tSessionVerwaltung.setzeEnde(auswahlView);
        return;
    }

    public void doBestaetigungZurueck() {
        if (!tSessionVerwaltung.pruefeStart(liefereSeiteEinstellungen_Bestaetigung())) {
            return;
        }
        tSessionVerwaltung.setzeEnde(liefereSeiteEinstellungen());
        return;
    }

    /*+++++++++++++++++++++++++++++++++Verarbeitungslogiken++++++++++++++++++++++++++++*/
    
    public int liefereSeiteEinstellungen() {
        if (tSession.isPermanentPortal()) {return KonstPortalView.P_EINSTELLUNGEN;}
        else {
            return KonstPortalView.EINSTELLUNGEN;
        }
    }
    
    public int liefereSeiteEinstellungen_Bestaetigung() {
        if (tSession.isPermanentPortal()) {return KonstPortalView.P_EINSTELLUNGEN_BESTAETIGUNG;}
        else {
            return KonstPortalView.EINSTELLUNGEN_BESTAETIGUNG;
        }
    }

    /**
     * aControllerRegistrierungSession:
     * > wurdeUeberAppAngefordert
     * 
     * eclDbM muß in aufrufender Funktion gehandelt werden.
     * 
     * pAufrufButton:
     * 0 => einfach "Weiter" gedrückt
     * 1 => E-Mail-Bestätigungslink 1 gedrückt
     * 2 => E-Mail-Bestätigungslink 2 gedrückt
     * 
     * Return:Wert:
     * <0 = Fehler - siehe BlTeilnehmerLogin.speichereRegistrierungAb
     * Gesondert zu behandeln:
     * CaFehler.afAndererUserAktiv
     * >0 ok, weiter je nach Returnwert:
     * KonstPortalView der aufzurufenden Maske (auswahl wird ggf. bereits
     * vorbereitet)
     * 
     */
    public int pruefenUndSpeichern(int pAufrufButton) {
        CaBug.druckeLog("pAufrufButton="+pAufrufButton, logDrucken, 10);
        
        BlTeilnehmerLoginNeu blTeilnehmerLogin = new BlTeilnehmerLoginNeu(tSession.isPermanentPortal());
        blTeilnehmerLogin.initDB(eclDbM.getDbBundle());
        eclLoginDatenM.copyToForReload(blTeilnehmerLogin);
       
        tEinstellungenSession.copyToNachEinstellungen(blTeilnehmerLogin);
        /*rc = Gesamtrückgabewert, nicht "zwischenverwenden!*/
        int rc = blTeilnehmerLogin.speichereRegistrierungAb(pAufrufButton);
        if (rc < 0 /* && 
                (
                (rc!=CaFehler.afEmailBestaetigungErforderlich && rc!=CaFehler.afEmail2BestaetigungErforderlich)
                || pAufrufButton==0)
                */ ) {
            CaBug.druckeLog("Return mit fehler="+CaFehler.getFehlertext(rc, 0), logDrucken, 10);
            return rc;
        }
        if (rc == 1) {
            /*Bestätigungsseite wird aufgerufen - Werte füllen*/
            tEinstellungenSession.copyFromVorBestaetigungsSeite(blTeilnehmerLogin);
            tEinstellungenSession.seteMailBestaetigungsCodeBestaetigung("");
            tEinstellungenSession.seteMail2BestaetigungsCodeBestaetigung("");
        }

        tEinstellungenSession.copyFromNachEinstellungenWeiter(blTeilnehmerLogin);

        CaBug.druckeLog("Stelle A", logDrucken, 10);

        /*Nun ggf. E-Mails verschicken*/
        if (blTeilnehmerLogin.emailBestaetigenMailVerschicken || pAufrufButton == 1) {
            String sprache = "DE";
            if (tLanguage.getLang() == 2) {
                sprache = "EN";
            }
            BlEinsprungLinkPortal lBlEinsprungLinkPortal = new BlEinsprungLinkPortal(eclDbM.getDbBundle());
            tLinkSession.setEinsprungsLinkFuerEmail(lBlEinsprungLinkPortal.linkFuerEmailBestaetigung(
                    tSession.getDifZeit(), tSession.getHvJahr(), tSession.getHvNummer(), tSession.getDatenbereich(),
                    sprache, tSession.getTestModus(), blTeilnehmerLogin.eclLoginDaten.emailBestaetigenLink, 1));
            tLinkSession.setEinsprungsLinkNurCode(blTeilnehmerLogin.eclLoginDaten.emailBestaetigenLink);

            String hMailText = "", hBetreff = "";
            if (!tSession.isPermanentPortal()) {
                hBetreff = eclTextePortalM.holeText("220");
                hMailText = eclTextePortalM.holeText("221");
            }
            else {
                hBetreff = eclTextePortalM.holeText("1974");
                hMailText = eclTextePortalM.holeText("1975");
            }

            //Texte 282 und 283 nicht mehr verwendet
            CaBug.druckeLog("Stelle B", logDrucken, 10);

            baMailm.senden(blTeilnehmerLogin.eclLoginDaten.eMailFuerVersand, hBetreff, hMailText);
        }

        if (blTeilnehmerLogin.email2BestaetigenMailVerschicken || pAufrufButton == 2) {
            String sprache = "DE";
            if (tLanguage.getLang() == 2) {
                sprache = "EN";
            }
            BlEinsprungLinkPortal lBlEinsprungLinkPortal = new BlEinsprungLinkPortal(eclDbM.getDbBundle());
            tLinkSession.setEinsprungsLinkFuerEmail(lBlEinsprungLinkPortal.linkFuerEmailBestaetigung(
                    tSession.getDifZeit(), tSession.getHvJahr(), tSession.getHvNummer(), tSession.getDatenbereich(),
                    sprache, tSession.getTestModus(), blTeilnehmerLogin.eclLoginDaten.email2BestaetigenLink, 2));
            tLinkSession.setEinsprungsLinkNurCode(blTeilnehmerLogin.eclLoginDaten.email2BestaetigenLink);

            String hMailText = "", hBetreff = "";
            if (!tEinstellungenSession.isWurdeUeberAppAngefordert()) {
                hBetreff = eclTextePortalM.holeText("220");
                hMailText = eclTextePortalM.holeText("221");
            } else {
                hBetreff = eclTextePortalM.holeText("282");
                hMailText = eclTextePortalM.holeText("283");
            }

            baMailm.senden(blTeilnehmerLogin.eclLoginDaten.eMail2FuerVersand, hBetreff, hMailText);
        }

        if (blTeilnehmerLogin.emailAnRemoteRegister) {
            if (tSession.isPermanentPortal()) {
                int rc1=brMAktionaersdaten.mailAnGenossenschaftSys(eclLoginDatenM.getEclLoginDaten().loginKennung, blTeilnehmerLogin.eclLoginDaten.eMailFuerVersand, CaDatumZeit.DatumZeitStringFuerDatenbank());
                if (tRemoteAR.pruefeVerfuegbar(rc1)==false) {
                    return CaFehler.perRemoteAktienregisterNichtVerfuegbar;
                 }
                
            }
        }
        
        if (pAufrufButton == 0) {
            if (rc == 1) {
                rc = liefereSeiteEinstellungen_Bestaetigung();
            } else {
                rc = tAuswahl.startAuswahl(true);
            }
        }

        return rc;
    }

    
    
    
    
    /******************************Entmistete Funktionen************************/
    //	/*Die folgenden Funktionen werden aufgerufen aus den verschiedenen Masken - und verweisen alle auf doEinstellungen*/
    //	public String doFehlerEinstellungen(){
    //		String naechsteMaske="";
    //		if (!aFunktionen.pruefeStart("aRegistrierung")){return "aDlgFehler";}
    //		
    //		aFunktionen.setzeEnde("aEinstellungen", true, true);
    //
    //		naechsteMaske=doEinstellungen();
    //		aFunktionen.setzeEnde();return naechsteMaske;
    //	}
    //

    //	/**Voraussetzung: eclDbM gefüllt und offen.
    //	 * Ergebnis: eclPublikationisteM gefüllt.
    //	 */
    //	public void initPublikationListe(){
    //		List<EclPublikationM> lPublikationListeM=new LinkedList<>();
    //		
    //		int anz,i,i1,i2;
    //		anz=eclDbM.getDbBundle().dbPublikation.read_all();
    //		for (i=0;i<anz;i++){
    //			boolean zulaessig=false;
    //			for (i1=0;i1<10;i1++){
    //				if (eclDbM.getDbBundle().dbPublikation.ergebnisPosition(i).publikationenZustellung[i1]!=0){zulaessig=true;}
    //			}
    //			if (zulaessig==true){
    //				EclPublikationM lPublikationM=new EclPublikationM();
    //				lPublikationM.copyFrom(eclDbM.getDbBundle().dbPublikation.ergebnisPosition(i));
    //				
    //				/*Nun noch die selektierten Wege markieren*/
    //				int selektionen=eclTeilnehmerLoginM.getAktienregisterZusatzM().getPublikationenZustellung()[lPublikationM.getPosition()];
    //				int selektionspotenz=1;
    //				for (i2=0;i2<10;i2++){
    //					if ((selektionen & selektionspotenz)==selektionspotenz){lPublikationM.getPublikationAngefordert()[i2]=true;}
    //					selektionspotenz=selektionspotenz*2;
    //				}
    //				
    //				lPublikationListeM.add(lPublikationM);
    //			}
    //		}
    //
    //		eclPublikationListeM.setPublikationListeM(lPublikationListeM);
    //	}

    //	/***********Initialisierung für aRegistrierung und aEinstellungen**************************
    //	 * Return-Wert:
    //	 * > Prüft, ob die Erstregistrierungsseite aufgerufen werden muß, oder direkt zum HV-Teil gegangen werden kann
    //	 * > true=Erstregistrierung
    //	 * Ansonsten: Die für aRegistrierung/aEinstellungen benötigten, unten aufgeführten Variablen, werden
    //	 * gesetzt.
    //	 * 
    //	 * Voraussetzung: eclTeilnehmerLoginM ist gefüllt.
    //	 * 
    //	 * Abhängig von:
    //	 * > bereits Newsletterregistrierung angeboten?
    //	 * > bereits eigenes Passwort angeboten?
    //	 * > Disclaimer noch zu bestätigen?
    //	 * 
    //	 * Gesetzt sind anschließend:
    //	 * > aDlgVariablen.anzeigeMeldung
    //	 * > aDlgVariablen.anzeigeMeldungsText1
    //	 * > aDlgVariablen.anzeigeMeldungsText2
    //	 * 
    //	 * > aDlgVariablen.emailbestaetigen (bei Erstregistrierung nicht gebraucht, dennoch auf false setzen)
    //	 * > aDlgVariablen.email2bestaetigen (bei Erstregistrierung nicht gebraucht, dennoch auf false setzen)
    //	 * 
    //	 * > aDlgVariablen.passwortBereitsVergeben
    //	 * > aDlgVariablen.ausgewaehltVergabeEigenesPasswort
    //	 * > aDlgVariablen.neuesPasswort (ist immer false - Checkbox für Änderung)
    //	 * 
    //	 * > aDlgVariablen.anzeigeHinweisDatenschutzerklaerung  
    //	 * > aDlgVariablen.anzeigeHinweisHVPortalBestaetigen
    //	 * > aDlgVariablen.anzeigeHinweisAktionaersPortalBestaetigen
    //	 * 
    //	 * 
    //	 */
    //	public boolean ppruefeErstregistrierung(){
    //		startNurDatenVorbereiten();
    //		aDlgVariablen.setAnzeigeMeldung(false);
    //		aDlgVariablen.setAnzeigeMeldungsText1(false);
    //		aDlgVariablen.setAnzeigeMeldungsText2(false);
    //		
    //		aDlgVariablen.setEmailbestaetigen(false);
    //		aDlgVariablen.setEmail2bestaetigen(false);
    //		
    //		aDlgVariablen.setPasswortBereitsVergeben(false);
    //		aDlgVariablen.setAusgewaehltVergabeEigenesPasswort(false);
    //		aDlgVariablen.setNeuesPasswort(false);
    //		
    //		aDlgVariablen.setAnzeigeHinweisDatenschutzerklaerung(false);
    //		aDlgVariablen.setAnzeigeHinweisHVPortalBestaetigen(false);
    //		aDlgVariablen.setAnzeigeHinweisAktionaersPortalBestaetigen(false);
    //
    //		/*eclTeilnehmerLoginM.getAktienregisterZusatzM() neu einlesen, da möglicherweise mittlerweile verändert
    //		 * (z.B. durch Email-Bestätigungs-Ablauf)
    //		 */
    //		EclAktienregisterZusatz lAktienregisterZusatz=new EclAktienregisterZusatz();
    //		lAktienregisterZusatz.aktienregisterIdent=eclTeilnehmerLoginM.getAktienregisterZusatzM().getAktienregisterIdent();
    //		if (lAktienregisterZusatz.aktienregisterIdent!=0){
    //			eclDbM.getDbBundle().dbAktienregisterZusatz.read(lAktienregisterZusatz);
    //			if (eclDbM.getDbBundle().dbAktienregisterZusatz.anzErgebnis()==1){
    //				lAktienregisterZusatz=eclDbM.getDbBundle().dbAktienregisterZusatz.ergebnisPosition(0);
    //				eclTeilnehmerLoginM.getAktienregisterZusatzM().copyFrom(lAktienregisterZusatz);
    //			}
    //		}
    //
    //		/*Meldungen anzeigen?*/
    //		if (eclTeilnehmerLoginM.getAktienregisterZusatzM().getEmailBestaetigt()==0 && (!eclTeilnehmerLoginM.getAktienregisterZusatzM().geteMailFuerVersand().isEmpty())){
    //			/*Email-Adresse 1 vorhanden und noch nicht bestätigt?*/
    //			aDlgVariablen.setAnzeigeMeldung(true);
    //			aDlgVariablen.setAnzeigeMeldungsText1(true);
    //			aDlgVariablen.setEmailbestaetigen(true);
    //		}
    //		if (eclTeilnehmerLoginM.getAktienregisterZusatzM().getEmail2Bestaetigt()==0 && (!eclTeilnehmerLoginM.getAktienregisterZusatzM().geteMail2FuerVersand().isEmpty())){
    //			/*Email-Adresse 2 vorhanden und noch nicht bestätigt?*/
    //			aDlgVariablen.setAnzeigeMeldung(true);
    //			aDlgVariablen.setAnzeigeMeldungsText2(true);
    //			aDlgVariablen.setEmail2bestaetigen(true);
    //		}
    //
    //		/*Passwort Registrierung*/
    //		if (eclTeilnehmerLoginM.getAktienregisterZusatzM().getEigenesPasswort()==99 ||
    //				eclTeilnehmerLoginM.getAktienregisterZusatzM().getEigenesPasswort()==98){
    //			/*Bereits eigenes Passwort vergeben*/
    //			aDlgVariablen.setPasswortBereitsVergeben(true);
    //			aDlgVariablen.setAusgewaehltVergabeEigenesPasswort(true);
    //		}
    //		
    //		/*Hinweis Aktionärs Portal anzuzeigen?*/
    //		if ((eclParamM.getParam().paramPortal.bestaetigenHinweisAktionaersportal!=0 && (eclTeilnehmerLoginM.getAktienregisterZusatzM().getHinweisAktionaersPortalBestaetigt()==0 || eclTeilnehmerLoginM.getAktienregisterZusatzM().getHinweisAktionaersPortalBestaetigt()==2)) ||
    //				eclParamM.getParam().paramPortal.bestaetigenHinweisAktionaersportal==2 /*Dann immer bestätigen bei jeder Anmeldung!*/
    //				){
    //			aDlgVariablen.setAnzeigeHinweisAktionaersPortalBestaetigen(true);
    //		}
    //		
    //		/*Hinweis HV Portal anzuzeigen?*/
    //		if ((eclParamM.getParam().paramPortal.bestaetigenHinweisHVportal!=0 && (eclTeilnehmerLoginM.getAktienregisterZusatzM().getHinweisHVPortalBestaetigt()==0 || eclTeilnehmerLoginM.getAktienregisterZusatzM().getHinweisHVPortalBestaetigt()==2)) ||
    //				eclParamM.getParam().paramPortal.bestaetigenHinweisHVportal==2 /*Dann immer bestätigen bei jeder Anmeldung!*/
    //				){
    //			aDlgVariablen.setAnzeigeHinweisHVPortalBestaetigen(true);
    //		}
    //		
    //		/*Falls einer der Hinweise (Portal oder hV) zu bestätigen ist, dann auch Datenschutzerklärungshinweis anzeigen*/
    //		if ((aDlgVariablen.isAnzeigeHinweisAktionaersPortalBestaetigen()==true || aDlgVariablen.isAnzeigeHinweisHVPortalBestaetigen()==true)
    //				&& eclParamM.getParam().paramPortal.separateDatenschutzerklaerung==1){
    //			aDlgVariablen.setAnzeigeHinweisDatenschutzerklaerung(true);
    //		}
    //
    //		/*Checken, ob irgendwas auf der Registrierungsmaske angezeigt werden soll - nur dann die Maske selbst aufrufen, ansonsten überspringen*/
    //		if (aDlgVariablen.isAnzeigeMeldung()==true ||
    //				aDlgVariablen.isAnzeigeHinweisDatenschutzerklaerung()==true ||	
    //				aDlgVariablen.isAnzeigeHinweisAktionaersPortalBestaetigen()==true ||	
    //				aDlgVariablen.isAnzeigeHinweisHVPortalBestaetigen()==true	
    //				)
    //		{
    //			return true;
    //		}
    //		return false;
    //	}
    //

}
