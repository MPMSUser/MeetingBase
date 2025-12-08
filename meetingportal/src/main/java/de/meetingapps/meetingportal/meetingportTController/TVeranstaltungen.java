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

import java.util.LinkedList;
import java.util.List;

import de.meetingapps.meetingportal.meetComAllg.CaBug;
import de.meetingapps.meetingportal.meetComAllg.CaFehler;
import de.meetingapps.meetingportal.meetComAllg.CaString;
import de.meetingapps.meetingportal.meetComBa.BaMailM;
import de.meetingapps.meetingportal.meetComBl.BlEKFreiwilligesAnmelden;
import de.meetingapps.meetingportal.meetComBl.BlVeranstaltungen;
import de.meetingapps.meetingportal.meetComEclM.EclBesitzGesamtAuswahl1M;
import de.meetingapps.meetingportal.meetComEclM.EclDbM;
import de.meetingapps.meetingportal.meetComEclM.EclLoginDatenM;
import de.meetingapps.meetingportal.meetComEclM.EclParamM;
import de.meetingapps.meetingportal.meetComEclM.EclPortalTexteM;
import de.meetingapps.meetingportal.meetComEclM.EclVeranstaltungM;
import de.meetingapps.meetingportal.meetComEclM.EclVeranstaltungenM;
import de.meetingapps.meetingportal.meetComEntities.EclMeldung;
import de.meetingapps.meetingportal.meetComEntities.EclVeranstaltungenMenue;
import de.meetingapps.meetingportal.meetComEntities.EclZugeordneteMeldungNeu;
import de.meetingapps.meetingportal.meetComKonst.KonstPortalTexte;
import de.meetingapps.meetingportal.meetComKonst.KonstPortalView;
import de.meetingapps.meetingportal.meetingCoreReport.RpBrowserAnzeigen;
import de.meetingapps.meetingportal.meetingportTFunktionen.TPruefeStartNachOpen;
import de.meetingapps.meetingportal.meetingportTFunktionen.TSessionVerwaltung;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;


@RequestScoped
@Named
public class TVeranstaltungen {

    private int logDrucken=10;
    
	@Inject private EclDbM eclDbM;

	
	private @Inject EclPortalTexteM eclPortalTexteM;
	private @Inject BaMailM baMailM;
	private @Inject EclParamM eclParamM;
	private @Inject TSessionVerwaltung tSessionVerwaltung;
	private @Inject TSession tSession;
    private @Inject TVeranstaltungenSession tVeranstaltungenSession;
	private @Inject EclLoginDatenM eclLoginDatenM;
	private @Inject TDialogveranstaltungenSession tDialogveranstaltungenSession;
	private @Inject TLanguage tLanguage;
	private @Inject EclBesitzGesamtAuswahl1M eclBesitzGesamtAuswahl1M;
    private @Inject TPruefeStartNachOpen tPruefeStartNachOpen;
    private @Inject EclVeranstaltungenM eclVeranstaltungenM;

	/**Initialisierung für Status-Anzeige Dialogveranstaltungen
	 * 
	 * pOpenDurchfuehren=true => es wird der Open/close in init durchgeführt, false in aufrufender Funktion.
	 * Bei false wird EclDbM.openweitere in init gehandelt, normaler open und close in aufrufender Funktion*/
	public void init(boolean pOpenDurchfuehren){
	    if (pOpenDurchfuehren) {
	        eclDbM.openAll();
	    }
        eclDbM.openWeitere();
	    initAllgemein(eclLoginDatenM.getEclAktienregister().aktienregisterIdent, eclLoginDatenM.getEclLoginDaten().eMailFuerVersand.trim());
	    leseVeranstaltungen(eclLoginDatenM.getEclAktienregister().aktienregisterIdent);
	    if (pOpenDurchfuehren) {
	        eclDbM.closeAll();
	    }
	}
	
	
    /**Füllt tDialogveranststaltungSession*/
    public void leseVeranstaltungen(int aktionaersIdent) {
        BlVeranstaltungen blVeranstaltungen=new BlVeranstaltungen(true, eclDbM.getDbBundle());
        CaBug.druckeLog("Aktionärsident="+aktionaersIdent, logDrucken, 10);
        blVeranstaltungen.leseAktiveVeranstaltungen(aktionaersIdent);
        List<EclVeranstaltungM> veranstaltungListe=new LinkedList<EclVeranstaltungM>();
        tDialogveranstaltungenSession.setAusgewaehlt("");
        if (blVeranstaltungen.rcVeranstaltungArray!=null) {
            for (int i=0;i<blVeranstaltungen.rcVeranstaltungArray.length;i++) {
                EclVeranstaltungM lVeranstaltungM=new EclVeranstaltungM(blVeranstaltungen.rcVeranstaltungArray[i]);
                veranstaltungListe.add(lVeranstaltungM);
                if (!lVeranstaltungM.isAusgebucht() && lVeranstaltungM.isAusgewaehlt()) {
                    CaBug.druckeLog("i="+i+" ist ausgewählt", logDrucken, 10);
                    tDialogveranstaltungenSession.setAusgewaehlt(Integer.toString(i));
                    lVeranstaltungM.setAnAbgemeldet("1");
                }
            }
        }
        tDialogveranstaltungenSession.setVeranstaltungen(veranstaltungListe);
       
    }

	
	public void initAllgemein(int aktionaersIdent, String pMailAdresse) {
        BlVeranstaltungen blVeranstaltungen=new BlVeranstaltungen(true, eclDbM.getDbBundle());
        blVeranstaltungen.liefereAnmeldeStatus(aktionaersIdent);
 
        tDialogveranstaltungenSession.setAnzPersonen("0");
        tDialogveranstaltungenSession.setZurVeranstaltungAngemeldet(null);

        tDialogveranstaltungenSession.setIstAngemeldet(blVeranstaltungen.rcAngemeldet);
        tDialogveranstaltungenSession.setIstAbgemeldet(blVeranstaltungen.rcAbgemeldet);
        if (blVeranstaltungen.rcAngemeldet) {
            tDialogveranstaltungenSession.setAnzPersonen(Integer.toString(blVeranstaltungen.rcAngemeldetAnzahlPersonen));
            tDialogveranstaltungenSession.setZurVeranstaltungAngemeldet(new EclVeranstaltungM(blVeranstaltungen.rcAngemeldetZuVeranstaltung));
            
        }
        if (blVeranstaltungen.rcAbgemeldet) {
            tDialogveranstaltungenSession.setZurVeranstaltungAngemeldet(new EclVeranstaltungM(blVeranstaltungen.rcAngemeldetZuVeranstaltung));
        }
        tDialogveranstaltungenSession.setZuVeranstaltungenAngemeldetListe(blVeranstaltungen.rcAngemeldetZuVeranstaltungenListe);
       
        tDialogveranstaltungenSession.setMailAdresse(pMailAdresse);
	}
	
    /**Wird beim Initialisieren der Maske aufgerufen, um Änderungsverfolgung zu initialisieren -
     * Änderungsverfolgung dient dazu, um beim Verlassen über menü festzustellen,
     * ob noch Eingaben unabgespeichert sind
     */
    public void aenderungsverfolgungStart() {
    }
    
    /**Liefert beim Verlassen der Maske über Menü true, wenn unabgespeicherte
     * Änderungen vorhanden sind
      */
    public boolean aenderungsverfolgungEtwasUngespeichert() {
        return false;
    }

	/**Initialisierungeigentliche Anmelde-Maske
	 * EclDbM wird in initAnmelden gehandelt*/
	public void initAnmelden() {
        eclDbM.openAll();
        eclDbM.openWeitere();
	    initAnmeldenAllgemein(eclLoginDatenM.getEclAktienregister().aktienregisterIdent);
        eclDbM.closeAll();
	}
	
	public void initAnmeldenAllgemein(int aktionaersIdent) {
	    leseVeranstaltungen(aktionaersIdent);
        tDialogveranstaltungenSession.setAnzPersonen("1");
	}
	
	
	
	public void doAnmelden() {
        if (!tSessionVerwaltung.pruefeStart(KonstPortalView.AUSWAHL1_DIALOGVERANSTALTUNGEN)) {
            return;
        }
		initAnmelden();
        tSessionVerwaltung.setzeEnde(KonstPortalView.AUSWAHL1_ANMELDEN_DIALOGVERANSTALTUNGEN);
	}
	
	public void doZurueck() {
        if (!tSessionVerwaltung.pruefeStart(KonstPortalView.AUSWAHL1_ANMELDEN_DIALOGVERANSTALTUNGEN)) {
            return;
        }
        tSessionVerwaltung.setzeEnde(KonstPortalView.AUSWAHL1_DIALOGVERANSTALTUNGEN);
	}

	
	public void doAnmeldenAusfuehren(EclVeranstaltungM lVeranstaltung) {
        if (!tSessionVerwaltung.pruefeStart(KonstPortalView.AUSWAHL1_ANMELDEN_DIALOGVERANSTALTUNGEN)) {
            return;
        }
		boolean brc=anmeldenAusfuehren(eclLoginDatenM.getEclAktienregister().aktienregisterIdent, lVeranstaltung, true, 1);
		if (brc==false) {
            tSessionVerwaltung.setzeEnde();
		    return;
		}
		
		init(true);

        tSessionVerwaltung.setzeEnde(KonstPortalView.AUSWAHL1_DIALOGVERANSTALTUNGEN);
	}
	
    public boolean anmeldenAusfuehren(int aktienregisterIdent, EclVeranstaltungM lVeranstaltung, boolean eMailZwingend, int anOderAbmelden) {
	
        int fehler=0;
        int anzahlPersonen=1;

        String mailAdresse="";
        if (anOderAbmelden==1) {
            if (eclParamM.getParam().paramPortal.veranstaltungPersonenzahlEingeben==1) {
                String hAnzahlPersonen=tDialogveranstaltungenSession.getAnzPersonen();
                anzahlPersonen=0;
                if (!CaString.isNummern(hAnzahlPersonen) || hAnzahlPersonen.length()>4) {
                    fehler=CaFehler.afNurZahlenZulaessig;
                }
                else {
                    anzahlPersonen=Integer.parseInt(hAnzahlPersonen);
                    if (anzahlPersonen<1) {fehler=CaFehler.afNullPersonenAngemeldet;}
                    if (anzahlPersonen>5) {fehler=CaFehler.afMaxPersonenUeberschritten;}
                }
            }

            /*Mail-Adresse abprüfen*/

            if (eclParamM.getParam().paramPortal.veranstaltungMailVerschicken==1 || eclParamM.getParam().paramPortal.veranstaltungMailVerschicken==2 || eclParamM.getParam().paramPortal.veranstaltungMailVerschicken==3) {
                mailAdresse= tDialogveranstaltungenSession.getMailAdresse().trim();
                if (eMailZwingend && (!CaString.isMailadresse(mailAdresse))) {
                    fehler=CaFehler.afEMailBestaetigungFalsch;
                }
            }

            if (fehler==0) {
                if (lVeranstaltung.getAnzahlAnmeldungenIst()+anzahlPersonen>lVeranstaltung.getMaximaleAnzahlAnmeldungen()) {
                    CaBug.druckeLog("Ausgebucht 1", logDrucken, 10);
                    fehler=CaFehler.afAusgebucht;
                }
            }
            if (fehler!=0) {
                tSession.trageFehlerEin(fehler);
                return false;
            }

            String mailAnhangPfad="";
            
            eclDbM.openAll();
            eclDbM.openWeitere();
            BlVeranstaltungen blVeranstaltungen=new BlVeranstaltungen(true, eclDbM.getDbBundle());
            blVeranstaltungen.anmeldung(aktienregisterIdent, lVeranstaltung.getIdent(), anzahlPersonen, 1, 1);
            
            if (eclParamM.getParam().paramPortal.veranstaltungMailVerschicken==3) {
                BlEKFreiwilligesAnmelden blEKFreiwilligesAnmelden=new BlEKFreiwilligesAnmelden(eclDbM.getDbBundle()); 

                EclZugeordneteMeldungNeu lZugeordneteMeldungNeu=eclBesitzGesamtAuswahl1M.liefereMeldungEigenerBestand();
                EclMeldung lMeldung=lZugeordneteMeldungNeu.eclMeldung;

                blEKFreiwilligesAnmelden.ausfuehrenEKAnzeigenAllgemein(1, eclLoginDatenM.getAnmeldeKennungFuerAnzeige(), tLanguage.getLang(), lMeldung);
                mailAnhangPfad=blEKFreiwilligesAnmelden.rcDateiname;
            }
            eclDbM.closeAll();
            if (blVeranstaltungen.rcUeberbucht) {
                CaBug.druckeLog("Ausgebucht 2", logDrucken, 10);
                initAnmelden();
                tSession.trageFehlerEin(CaFehler.afAusgebucht);
                return false;
            }

            /*Nun Mail-Verschicken*/
            if ((eclParamM.getParam().paramPortal.veranstaltungMailVerschicken==1 || eclParamM.getParam().paramPortal.veranstaltungMailVerschicken==2  || eclParamM.getParam().paramPortal.veranstaltungMailVerschicken==3) && (!mailAdresse.isEmpty())) {
                String hBetreff=eclPortalTexteM.holeIText(KonstPortalTexte.DIALOGVERANSTALTUNG_ZUGANG_MAILBETREFF);
                String hMailText=eclPortalTexteM.holeIText(KonstPortalTexte.DIALOGVERANSTALTUNG_ZUGANG_MAILTEXT);
                if (eclParamM.getParam().paramPortal.veranstaltungMailVerschicken!=3) {
                    mailAnhangPfad=eclParamM.getClGlobalVar().lwPfadAllgemein+"\\"+eclParamM.getParamServer().praefixPfadVerzeichnisse
                            + "reports\\"+eclParamM.getMandantPfad()+
                            "\\ZugangDialog"+Integer.toString(lVeranstaltung.getIdent())+".pdf";
                }
                baMailM.sendenMitAnhang(mailAdresse, 
                        hBetreff,
                        hMailText,
                        mailAnhangPfad
                        );
            }
        }
        else {
            /**Abmelden*/
            eclDbM.openAll();
            eclDbM.openWeitere();
            BlVeranstaltungen blVeranstaltungen=new BlVeranstaltungen(true, eclDbM.getDbBundle());
            blVeranstaltungen.anmeldung(aktienregisterIdent, lVeranstaltung.getIdent(), anzahlPersonen, 1, 2);
            eclDbM.closeAll();
        }
        return true;
	
	}
	
    /**Auf "Weiter" geklickt bei der Möglichkeit, sich zu mehreren Veranstaltungen anzumelden*/
    public void doAnmeldenMulti() {
        if (!tSessionVerwaltung.pruefeStart(KonstPortalView.AUSWAHL1_ANMELDEN_DIALOGVERANSTALTUNGEN)) {
            return;
        }
       
        List<EclVeranstaltungM> veranstaltungen=tDialogveranstaltungenSession.getVeranstaltungen();
        for (EclVeranstaltungM iEclVeranstaltungM: veranstaltungen) {
            if (iEclVeranstaltungM.getAnAbgemeldet()!=null && iEclVeranstaltungM.getAnAbgemeldet().equals("1")) {
                /*boolean brc=*/anmeldenAusfuehren(eclLoginDatenM.getEclAktienregister().aktienregisterIdent, iEclVeranstaltungM, true, 1); 
            }
            else {
                /*boolean brc=*/anmeldenAusfuehren(eclLoginDatenM.getEclAktienregister().aktienregisterIdent, iEclVeranstaltungM, true, 2); 
            }
        }
        init(true);

        tSessionVerwaltung.setzeEnde(KonstPortalView.AUSWAHL1_DIALOGVERANSTALTUNGEN);

    }

	/*************************aAuswahl1Dialogveranstaltungen*************************************/

	
	public void doStornieren() {
	    if (!tSessionVerwaltung.pruefeStart(KonstPortalView.AUSWAHL1_DIALOGVERANSTALTUNGEN)) {
	        return;
	    }
		eclDbM.openAll();
		eclDbM.openWeitere();
		BlVeranstaltungen blVeranstaltungen=new BlVeranstaltungen(true, eclDbM.getDbBundle());
		blVeranstaltungen.widerrufeAnmeldung(eclLoginDatenM.getEclAktienregister().aktienregisterIdent, 1);
		eclDbM.closeAll();
		
        init(true);
        tSessionVerwaltung.setzeEnde(KonstPortalView.AUSWAHL1_DIALOGVERANSTALTUNGEN);
	}
	
	   public void doEKAnzeigen() {
	        if (!tSessionVerwaltung.pruefeStart(KonstPortalView.AUSWAHL1_DIALOGVERANSTALTUNGEN)) {
	            return;
	        }
	        eclDbM.openAll();

	        BlEKFreiwilligesAnmelden blEKFreiwilligesAnmelden=new BlEKFreiwilligesAnmelden(eclDbM.getDbBundle()); 

	        EclZugeordneteMeldungNeu lZugeordneteMeldungNeu=eclBesitzGesamtAuswahl1M.liefereMeldungEigenerBestand();
	        EclMeldung lMeldung=lZugeordneteMeldungNeu.eclMeldung;

	        blEKFreiwilligesAnmelden.ausfuehrenEKAnzeigenAllgemein(1, eclLoginDatenM.getAnmeldeKennungFuerAnzeige(), tLanguage.getLang(), lMeldung);
	        eclDbM.closeAll();
	        
	        RpBrowserAnzeigen rpBrowserAnzeigen = new RpBrowserAnzeigen();
	        rpBrowserAnzeigen.zeigen(blEKFreiwilligesAnmelden.rcDateiname);
	        
	        init(true);
	        tSessionVerwaltung.setzeEnde(KonstPortalView.AUSWAHL1_DIALOGVERANSTALTUNGEN);
	    }

	
	
	public void doDialogveranstaltungenZurueck() {
        if (!tSessionVerwaltung.pruefeStart(KonstPortalView.AUSWAHL1_DIALOGVERANSTALTUNGEN)) {
            return;
        }
		
        tSessionVerwaltung.setzeEnde(KonstPortalView.AUSWAHL1);
		
	}
	
	/*************************Menü******************************************/
    public void initMenue(boolean pOpenDurchfuehren, int pSubFunktion){
        if (pOpenDurchfuehren) {
            eclDbM.openAll();
        }
        eclDbM.openWeitere();
        BlVeranstaltungen blVeranstaltungen=new BlVeranstaltungen(true, eclDbM.getDbBundle());
        blVeranstaltungen.erzeugeVeranstaltungslisteFuerMenue(pSubFunktion);
        tVeranstaltungenSession.setVeranstaltungenFuerMenueListe(blVeranstaltungen.rcVeranstaltungenFuerMenueListe);
        tVeranstaltungenSession.setSubFunktion(pSubFunktion);
        if (pOpenDurchfuehren) {
            eclDbM.closeAll();
        }
    }

    private void initDetail(EclVeranstaltungenMenue pVeranstaltungMenue) {
        eclVeranstaltungenM.clear();
        
//      if (logDrucken==10) {
//          eclVeranstaltungenM.setZeigeInterneInfo(true);
//      }
      
      /*Prüfen - liegt bereits Anmeldung für dieses Mitglied für diese Veranstaltung vor?*/
      /*Quittung "Vorher" anzeigen*/
      BlVeranstaltungen blVeranstaltungen=new BlVeranstaltungen(true, eclDbM.getDbBundle());
      CaBug.druckeLog("pVeranstaltungMenue.identVeranstaltung="+pVeranstaltungMenue.identVeranstaltung, logDrucken, 10);
      blVeranstaltungen.erzeugeVeranstaltungslisteFuerTeilnehmer(BlVeranstaltungen.LAUT_VERANSTALTUNGS_IDENT, pVeranstaltungMenue.identVeranstaltung);
      CaBug.druckeLog("C", logDrucken, 10);
      eclVeranstaltungenM.copyFromBlVeranstaltungen(blVeranstaltungen);
      CaBug.druckeLog("D", logDrucken, 10);
     
      blVeranstaltungen.loginKennung=eclLoginDatenM.getEclLoginDaten().loginKennung;
      CaBug.druckeLog("E", logDrucken, 10);
      blVeranstaltungen.belegeWerteVeranstaltungsliste();
      CaBug.druckeLog("F", logDrucken, 10);
      if (blVeranstaltungen.veranstaltungenWerteVorhanden==false) {
          CaBug.druckeLog("Erstanmeldung=true", logDrucken, 10);
          eclVeranstaltungenM.setAnmeldungDurchfuehren(true);
      }
      else {
          CaBug.druckeLog("Erstanmeldung=false", logDrucken, 10);
          eclVeranstaltungenM.setAnmeldungDurchfuehren(false);
          /**Prüfe muß durchgeführt werden, damit inLetzterVerarbeitungEnthalten richtig gesetzt wird!*/
          blVeranstaltungen.pruefeVeranstaltungsliste(false);

          blVeranstaltungen.rcQuittungsArt=1;
          blVeranstaltungen.aufbereitenQuittung();
          eclVeranstaltungenM.setVeranstaltungenQuittungListe(blVeranstaltungen.rcVeranstaltungenQuittungListe);
      }
        
    }
    
    public void doDetail(EclVeranstaltungenMenue pVeranstaltungMenue) {
        CaBug.druckeLog("A", logDrucken, 10);
        if (!tSessionVerwaltung.pruefeStart(KonstPortalView.P_VERANSTALTUNGEN_AUSWAHL)) {
            return;
        }

        eclDbM.openAll();
        eclDbM.openWeitere();

        if (!tPruefeStartNachOpen.pruefeStartNachOpen(0)) {
            tSessionVerwaltung.setzeEnde();
            eclDbM.closeAll();
            return;
        }
        
        CaBug.druckeLog("B", logDrucken, 10);
        initDetail(pVeranstaltungMenue);
        
        tVeranstaltungenSession.setAusgewaehlteVeranstaltungMenue(pVeranstaltungMenue);
        
        eclDbM.closeAll();
        
        tSessionVerwaltung.setzeEnde(KonstPortalView.P_VERANSTALTUNGEN_DETAIL);
        
    }
 
    
    /****************************************Detailanzeige*******************************************************/
    
    public void doAbbrechen() {
        
        if (!tSessionVerwaltung.pruefeStart(KonstPortalView.P_VERANSTALTUNGEN_DETAIL)) {
            return;
        }
        
        initMenue(true, tVeranstaltungenSession.getSubFunktion());
        
        tSessionVerwaltung.setzeEnde(KonstPortalView.P_VERANSTALTUNGEN_AUSWAHL);

    }
    
    public void doSenden() {
        if (!tSessionVerwaltung.pruefeStart(KonstPortalView.P_VERANSTALTUNGEN_DETAIL)) {
            return;
        }

        eclDbM.openAll();
        eclDbM.openWeitere();
        
        BlVeranstaltungen blVeranstaltungen=new BlVeranstaltungen(true, eclDbM.getDbBundle());

        /*Falls Veranstaltungen, dann Veranstaltungen prüfen*/
        if (eclVeranstaltungenM.pruefeObVeranstaltungenVorhanden()==true) {
            blVeranstaltungen.rcVeranstaltungenListe=eclVeranstaltungenM.getVeranstaltungenListe();

            boolean brc=blVeranstaltungen.pruefeVeranstaltungsliste(true);
            if (brc==false) {
                tSession.trageFehlerEinMitArt(blVeranstaltungen.rcFehlerText, 1);
                eclDbM.closeAll();
                tSessionVerwaltung.setzeEnde();
                return;
            }
        }

        /*Veranstaltungen speichern*/
        blVeranstaltungen.loginKennung=eclLoginDatenM.getEclLoginDaten().loginKennung;
        blVeranstaltungen.aktionenAusfuehren();
        blVeranstaltungen.speichereWerteVeranstaltungsliste();
        blVeranstaltungen.rcQuittungsArt=2;
        blVeranstaltungen.aufbereitenQuittung();
        /*Werte müssen neu eingelesen werden, damit ggf. alte Werte für Veränderung richtig belegt sind.
         * Muß nach aufbereitenQuittung erfolgen, weil sonst Veränderungen nicht mehr richtig
         * erkannt werden!
         */
        blVeranstaltungen.belegeWerteVeranstaltungsliste();
        eclVeranstaltungenM.setVeranstaltungenQuittungListe(blVeranstaltungen.rcVeranstaltungenQuittungListe);

        /*Nun ggf. noch Mails verschicken*/
        if (blVeranstaltungen.rcMailBetreff!=null && blVeranstaltungen.rcMailBetreff.size()!=0) {
            String mailAdresse=eclLoginDatenM.getEclLoginDaten().eMailFuerVersand.trim();
            if (!mailAdresse.isEmpty()) {
                for (int i=0;i<blVeranstaltungen.rcMailBetreff.size();i++) {
                    
                    String mailBetreffNr=blVeranstaltungen.rcMailBetreff.get(i);
                    String mailBetreff=eclPortalTexteM.holeText(mailBetreffNr);
                    
                    String mailTextNr=blVeranstaltungen.rcMailText.get(i);
                    String mailText=eclPortalTexteM.holeText(mailTextNr);
                    
                    String mailAnhang="";
                    String rcMailAnhang=blVeranstaltungen.rcMailAnhang.get(i);
                    if (rcMailAnhang.isEmpty()==false) {
                        mailAnhang=eclDbM.getDbBundle().lieferePfadMeetingReports()+"\\"+rcMailAnhang;
                    }

                    baMailM.sendenMitAnhang(mailAdresse, mailBetreff, mailText, mailAnhang);
                }
            }
        }
        
        initDetail(tVeranstaltungenSession.getAusgewaehlteVeranstaltungMenue());
        
        
        eclDbM.closeAll();
        tSessionVerwaltung.setzeEnde();

    }
    
    public void doAendern() {
        
        if (!tSessionVerwaltung.pruefeStart(KonstPortalView.P_VERANSTALTUNGEN_DETAIL)) {
            return;
        }

        eclVeranstaltungenM.setAnmeldungDurchfuehren(true);
        
        tSessionVerwaltung.setzeEnde(KonstPortalView.P_VERANSTALTUNGEN_DETAIL);

    }

    
}
