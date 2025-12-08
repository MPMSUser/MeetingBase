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
package de.meetingapps.meetingportal.meetingportTFunktionen;

import de.meetingapps.meetingportal.meetComAllg.CaBug;
import de.meetingapps.meetingportal.meetComAllg.CaFehler;
import de.meetingapps.meetingportal.meetComBl.BlVeranstaltungen;
import de.meetingapps.meetingportal.meetComBl.BlWillenserklaerung;
import de.meetingapps.meetingportal.meetComBl.BlWillenserklaerungStatus;
import de.meetingapps.meetingportal.meetComBl.BlWillenserklaerungStatusNeu;
import de.meetingapps.meetingportal.meetComDb.DbBundle;
import de.meetingapps.meetingportal.meetComEclM.EclBesitzGesamtAuswahl1M;
import de.meetingapps.meetingportal.meetComEclM.EclBesitzGesamtM;
import de.meetingapps.meetingportal.meetComEclM.EclLoginDatenM;
import de.meetingapps.meetingportal.meetComEclM.EclParamM;
import de.meetingapps.meetingportal.meetComEntities.EclAktienregister;
import de.meetingapps.meetingportal.meetComEntities.EclBesitzAREintrag;
import de.meetingapps.meetingportal.meetComEntities.EclBesitzJeKennung;
import de.meetingapps.meetingportal.meetComEntities.EclIpTracking;
import de.meetingapps.meetingportal.meetComEntities.EclMeldung;
import de.meetingapps.meetingportal.meetComEntities.EclWillenserklaerungStatusNeu;
import de.meetingapps.meetingportal.meetComEntities.EclZugeordneteMeldungNeu;
import de.meetingapps.meetingportal.meetComHVParam.ParamSpezial;
import de.meetingapps.meetingportal.meetComKonst.KonstGruppen;
import de.meetingapps.meetingportal.meetComKonst.KonstLoginKennungArt;
import de.meetingapps.meetingportal.meetComKonst.KonstPortalView;
import de.meetingapps.meetingportal.meetComKonst.KonstWillenserklaerung;
import de.meetingapps.meetingportal.meetingportTController.TSession;
import de.meetingapps.meetingportal.meetingportTController.TWillenserklaerungSession;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@RequestScoped
@Named
public class TFunktionen {

    private int logDrucken = 3;

    private @Inject EclParamM eclParamM;
    private @Inject TJsfSession tJsfSession;
    private @Inject EclLoginDatenM eclLoginDatenM;
    private @Inject TSessionVerwaltung tSessionVerwaltung;
    private @Inject EclBesitzGesamtM eclBesitzGesamtM;
    private @Inject TSession tSession;
    private @Inject TWillenserklaerungSession tWillenserklaerungSession;
    private @Inject EclBesitzGesamtAuswahl1M eclBesitzGesamtAuswahl1M;
    private @Inject TPortalFunktionen tPortalFunktionen;

    /**Verwendung bzw. Belegung bei Aufruf durch Bestandspflege / interner
     * Client-Software
     */
    private boolean zusammenfassenVonAnmeldungenMoeglich = true;

    private boolean alleWillenserklaerungen = false;
    
    private boolean weitereKennungenZuladen =true;
    
    private boolean updateTblLogin = true;

    /********************Ip-Tracking*****************************************/
    public void speichereIpTracking(String pKennung, DbBundle pDbBundle) {

        EclIpTracking lIpTracking = tJsfSession.liefereTrackingInfo();
        lIpTracking.mandant = pDbBundle.clGlobalVar.mandant;
        lIpTracking.loginKennung = pKennung;

        pDbBundle.dbIpTracking.insert(lIpTracking);
    }

    /**********************************Logout*************************************/
    public String waehleLogout() {
        String logoutZiel = eclParamM.getParam().paramPortal.logoutZiel;
        if (logoutZiel.isEmpty()) {
            tSessionVerwaltung.setzeEnde(KonstPortalView.LOGIN);
            return "";
        }

        /**Hier Session Invalidaten*/
        tJsfSession.loescheAktuelleSession(logoutZiel);
        return "";
    }

 
    /***********************Status-Verarbeitung***********************************************************/

    /**Einlesen des Status für die eingeloggte Kennung.
     * Je nach Parameterstellung für den Standardablauf (leseStatus) oder ku178/Sonderablauif (leseStatusAuswahl1)
     * 
     * rc=1 => normaler Return
     * rc=2 => es muß Abfrage aufgerufen werden, ob die Meldungen mit Weisung durch andere vertreten werden oder nicht
      */
    public int leseStatusPortal(DbBundle lDbBundle) {
        CaBug.druckeLog("Start", logDrucken, 10);
        int rc = 0;
        if (eclParamM.getParam().paramPortal.varianteDialogablauf == 0) {
            rc = leseStatus(lDbBundle, false);
            if (updateTblLogin) {
                lDbBundle.dbLoginDaten.update_letzterLoginAufServerPositiv(eclLoginDatenM.getEclLoginDaten().ident);
            }
            if (rc==1) {
                if (zusammenfassenVonAnmeldungenMoeglich) {
                    ueberpruefeZweiAnmeldungenZuEiner(lDbBundle);
                }
            }
        } else {
            rc = leseStatusAuswahl1(lDbBundle);
        }
        tPortalFunktionen.belegePortalFunktionenWillenserklaerungenStatusAktiv();

        return rc;
    }

    
    /**  überprüfen, ob zwei Anmeldungen zu einer Anmeldung zusammengefaßt werden können*/
    private void ueberpruefeZweiAnmeldungenZuEiner(DbBundle lDbBundle) {
        CaBug.druckeLog("Start", logDrucken, 10);
       boolean gef=false;
        for (EclBesitzJeKennung iBesitzJeKennung : eclBesitzGesamtM.getBesitzJeKennungListe()) {
            /*Eigener Aktienregisterbestand überprüfen*/
            if (iBesitzJeKennung.eigenerAREintragVorhanden) {
                EclBesitzAREintrag lBesitzAREintrag=iBesitzJeKennung.eigenerAREintragListe.get(0);
                boolean brc=ueberpruefeAREintragZweiAnmeldungenZuEiner(lDbBundle, lBesitzAREintrag);
                gef=gef || brc;
            }
            
            /*Zugeordneter Insti-Bestand (Aktienregister) überprüfen*/
            if (iBesitzJeKennung.instiAREintraegeVorhanden) {
                for (EclBesitzAREintrag iBesitzAREintrag: iBesitzJeKennung.instiAREintraegeListe) {
                    boolean brc=ueberpruefeAREintragZweiAnmeldungenZuEiner(lDbBundle, iBesitzAREintrag);
                    gef=gef || brc;
                }
            }
         }
        /**Falls eine Stornierung durchgeführt wurde, dann Willenserklärungen komplett neu einlesen*/
        if (gef) {
            leseStatus(lDbBundle, false);
        }
    }
    
    
    /**überprüfen, ob zwei Anmeldungen zu einer Anmeldung zusammengefaßt werden können
     * true => es wurde eine Meldung zusammengefaßt*/
    private boolean ueberpruefeAREintragZweiAnmeldungenZuEiner(DbBundle lDbBundle, EclBesitzAREintrag lBesitzAREintrag) {
        CaBug.druckeLog("Start", logDrucken, 10);
        /*Prüfen, ob mehrere Meldungen vorhanden*/
        int anz=lBesitzAREintrag.zugeordneteMeldungenListe.size();
        if (anz<2) {return false;}
        if (ParamSpezial.ku310(lDbBundle.clGlobalVar.mandant)) {return false;}
        
        CaBug.druckeLog("2 oder mehr Meldungen", logDrucken, 10);

        BlWillenserklaerung blWillenserklaerung=new BlWillenserklaerung();
        
        blWillenserklaerung.pErteiltAufWeg=tWillenserklaerungSession.getEingabeQuelle();
        blWillenserklaerung.pErteiltZeitpunkt=tWillenserklaerungSession.getErteiltZeitpunkt();

        blWillenserklaerung.pEclAktienregisterEintrag=new EclAktienregister();
        blWillenserklaerung.pEclAktienregisterEintrag.aktienregisterIdent=lBesitzAREintrag.aktienregisterEintrag.aktienregisterIdent;

        blWillenserklaerung.anmeldungenAusAktienregisterStornieren_pruefe(lDbBundle);
                      
        if (blWillenserklaerung.rcIstZulaessig && +blWillenserklaerung.rcAnzahlMeldungen==2 && tWillenserklaerungSession.isZusammenfassenVonAnmeldungenMoeglich()==true){
            /*Zwei Anmeldungen existieren, diese können storniert werden => Stornieren und eine neue Anmeldung erzeugen*/
            
            /*******Stornieren*******/
            blWillenserklaerung=new BlWillenserklaerung();
            blWillenserklaerung.pErteiltAufWeg=tWillenserklaerungSession.getEingabeQuelle();
            blWillenserklaerung.pErteiltZeitpunkt=tWillenserklaerungSession.getErteiltZeitpunkt();
            
            blWillenserklaerung.pEclAktienregisterEintrag=new EclAktienregister();
            blWillenserklaerung.pEclAktienregisterEintrag.aktienregisterIdent=lBesitzAREintrag.aktienregisterEintrag.aktienregisterIdent;
            blWillenserklaerung.anmeldungenAusAktienregisterStornieren(lDbBundle);
            
            /******Neu Anmelden mit EINEM Aktienbestand*****/
            blWillenserklaerung=new BlWillenserklaerung();
            blWillenserklaerung.pErteiltAufWeg=tWillenserklaerungSession.getEingabeQuelle();
            blWillenserklaerung.pErteiltZeitpunkt=tWillenserklaerungSession.getErteiltZeitpunkt();
            
            /*Aktienregister füllen*/
            EclAktienregister aktienregisterEintrag=new EclAktienregister();
            aktienregisterEintrag.aktionaersnummer=lBesitzAREintrag.aktienregisterEintrag.aktionaersnummer;
            int erg=lDbBundle.dbAktienregister.leseZuAktienregisterEintrag(aktienregisterEintrag);
            if (erg<=0){/*Aktienregistereintrag nicht mehr vorhanden*/
                /*Dann schwerwiegender Fehler - denn dann hätte das Programm schon vorher rausfliegen müssen ....*/
                tSession.setFehlerCode(CaFehler.afAktienregisterEintragNichtMehrVorhanden);
                return true; //Auch in diesem Fall Status neu einlesen!
            }

            aktienregisterEintrag=lDbBundle.dbAktienregister.ergebnisPosition(0);
            blWillenserklaerung.pEclAktienregisterEintrag=aktienregisterEintrag;

            /*Restliche Parameter füllen*/
            blWillenserklaerung.pAktienAnmelden=-1; /*Alle Aktien anmelden*/
            blWillenserklaerung.pAnmeldungFix=false; /*Nicht "Fix" anmelden*/
            blWillenserklaerung.pAnzahlAnmeldungen=1;
            blWillenserklaerung.pWillenserklaerungGeberIdent=-1; /*Aktionär gibt in diesem Fall*/
            blWillenserklaerung.pPersonNatJurFuerAnmeldungVerwenden=aktienregisterEintrag.personNatJur;

            blWillenserklaerung.anmeldungAusAktienregister(lDbBundle);

            if (blWillenserklaerung.rcIstZulaessig==false){
                tSession.setFehlerCode(blWillenserklaerung.rcGrundFuerUnzulaessig);
                 return true;//Auch in diesem Fall Status neu einlesen!
            }
            return true;
        }


        return false;
   }
    
    /**rc=1 => normaler Return
     * rc=2 => es muß Abfrage aufgerufen werden, ob die Meldungen mit Weisung durch andere vertreten werden oder nicht
     */
    private int leseStatus(DbBundle lDbBundle, boolean mitStorno) {
        CaBug.druckeLog("", logDrucken, 3);
        int rc=1;
        
        if (lDbBundle.param.paramPortal.nurRawLiveAbstimmung == 1) {
            /*TODO Noch nicht fertig*/
            CaBug.druckeLog("nurRawLiveAbstimmung=1", logDrucken, 10);
            /*Nur "Selbst-Anmeldung" in zugeordneteMeldungenEigeneAktienArray einlesen, ohne weitere Willenserklräungen, - kann nur eine sein*/
            BlWillenserklaerungStatusNeu lWillenserklaerungStatus = new BlWillenserklaerungStatusNeu(lDbBundle);
            if (eclLoginDatenM.getEclLoginDaten().kennungArt == KonstLoginKennungArt.aktienregister) {
                CaBug.druckeLog("Ist KonstLoginKennungArt.aktienregister", logDrucken, 10);
                lWillenserklaerungStatus
                        .fuelleAlles(false);
                eclBesitzGesamtM.copyFrom(lWillenserklaerungStatus);
            }
            return rc;
        }


        CaBug.druckeLog("Vollen Status einlesen", logDrucken, 5);

        BlWillenserklaerungStatusNeu blWillenserklaerungStatusVoll = new BlWillenserklaerungStatusNeu(lDbBundle);
        
        blWillenserklaerungStatusVoll.nurNichtStornierteWillenserklaerungen=!alleWillenserklaerungen;
        blWillenserklaerungStatusVoll.umZugeordneteKennungenErgaenzen=weitereKennungenZuladen;
        
//        CaBug.druckeLog(eclLoginDatenM.getEclPersonenNatJur().name, logDrucken, 1);
        blWillenserklaerungStatusVoll.initAusgangsdaten(eclLoginDatenM.getEclLoginDaten(), eclLoginDatenM.getEclAktienregister(), eclLoginDatenM.getEclPersonenNatJur());
        blWillenserklaerungStatusVoll.piAusblendenMeldungen = eclBesitzGesamtM.getAusblendenMeldungen();

        blWillenserklaerungStatusVoll.fuelleAlles(true);
        int lPersonNatJur = 0;
        if (eclLoginDatenM.liefereKennungArt() == KonstLoginKennungArt.aktienregister) {
            lPersonNatJur = eclLoginDatenM.getEclAktienregister().personNatJur;
        } else {
            lPersonNatJur = eclLoginDatenM.getEclPersonenNatJur().ident;
        }
        blWillenserklaerungStatusVoll.ergaenzeAllesUmPraesenzdaten(lPersonNatJur);
        /*Hat nichts mit Präsenzliste zu tun, sondern mit der Liste aller mit dieser Kennung bereits
         * präsenten / nicht-Präsenten Bestände
         */
        blWillenserklaerungStatusVoll.fuellePraesenzList();
        
        if (eclParamM.getParam().paramPortal.handhabungWeisungDurchVerschiedene==0 && blWillenserklaerungStatusVoll.bereitsErteiltWeisungBriefwahlAllgemeinDurchAndereVorhanden) {
            blWillenserklaerungStatusVoll.fuelleMeldungenBereitsErteiltWeisungAllgemeinDurchAndereListe();
            rc=2;
        }
        eclBesitzGesamtM.copyFrom(blWillenserklaerungStatusVoll);
        ergaenzeBesitzGesamtM();
        if (CaBug.pruefeLog(logDrucken, 10)) {
            blWillenserklaerungStatusVoll.anzeige();
        }
        return rc;

        //		int erg;
        //	
        //		BlWillenserklaerungStatus lWillenserklaerungStatus=new BlWillenserklaerungStatus(lDbBundle);
        //		lWillenserklaerungStatus.piAlleWillenserklaerungen=alleWillenserklaerungen;
        //
        //		switch (eclTeilnehmerLoginM.getAnmeldeKennungArt()){
        //		case 1:{/*Login ist über Aktienregisternummer erfolgt. In diesem Fall "Basis-Meldungen über
        //		 			leseMeldungenZuAktienregisterIdent ermitteln, anschließend personNatJur aus (Aktionärs-)
        //		 			Anmeldungen bestimmen*/
        //			
        //			/*Zuerst jedoch prüfen, ob überhaupt noch Bestand vorhanden*/
        //			/*TODO #App: dieses Verfahren funktioniert nicht, wenn "die innovativen Funktionen des Portals" angeboten werden!
        //			 * Dann nochmal überarbeiten!
        //			 */
        //			
        //			if (eclTeilnehmerLoginM.getStimmen()==0 && xSessionVerwaltung.getStartPruefen()==1){
        //				return "aStatus0Bestand";
        //			}
        //			
        //			eclTeilnehmerLoginM.setAnmeldeIdentPersonenNatJur(0);
        //			lWillenserklaerungStatus.leseMeldungenZuAktienregisterIdent(eclTeilnehmerLoginM.getAnmeldeIdentAktienregister());
        //
        //			/*Nun noch personNatJur bestimmen*/
        //			if (lWillenserklaerungStatus.zugeordneteMeldungenEigeneAktienArray==null || lWillenserklaerungStatus.zugeordneteMeldungenEigeneAktienArray.length==0){
        //				/*Dann noch keine Anmeldungen vorhanden! => Erstanmeldung "Aktienregister"*/
        //				aDlgVariablen.setAusgewaehlteHauptAktion("1");
        //				return "aAnmelden";
        //			}
        //			else{
        //				/*TODO #9 bei Aktienregisteranmeldung funktioniert die Zuordnung von Gästekarten und Bevollmächtigten möglicherweise nicht! Denn die erfolgt über PersonNatJur, die in der 
        //				 * Meldung gespeichert wird. Bei der kompletten Meldungsstornierung verliert man diese
        //				 * PersonNatJur aber - bzw. deren Zuordnung zum Aktienregister. Außerdem ist eine Gastkartenausstellung oder der Erhalt einer Vollmacht nur möglich, wenn eine
        //				 * eigene Anmeldung bereits existiert (sonst ist PersonnatJur=0!)
        //				 */
        //				
        //				/*Entfällt, da nun direkt in BlWillenserklaerung ermittelt!
        //				for (i=0;i<lWillenserklaerungStatus.zugeordneteMeldungenEigeneAktienArray.length;i++){
        //					if (lWillenserklaerungStatus.zugeordneteMeldungenEigeneAktienArray[i].klasse==1){
        //						eclTeilnehmerLoginM.setAnmeldeIdentPersonenNatJur(lWillenserklaerungStatus.zugeordneteMeldungenEigeneAktienArray[i].personNatJurIdent);
        //					}
        //				}
        //				*/
        //				/*Neu :-):*/
        //				eclTeilnehmerLoginM.setAnmeldeIdentPersonenNatJur(lWillenserklaerungStatus.aktienregisterPersonNatJurIdent);
        //				/*Neu Ende*/
        //				
        //				/**Nun überprüfen, ob zwei Anmeldungen zu einer Anmeldung zusammengefaßt werden können*/
        //				BlWillenserklaerung blWillenserklaerung=new BlWillenserklaerung();
        //				blWillenserklaerung.pErteiltAufWeg=aDlgVariablen.getEingabeQuelle();
        //				blWillenserklaerung.pErteiltZeitpunkt=aDlgVariablen.getErteiltZeitpunkt();
        //				blWillenserklaerung.pEclAktienregisterEintrag=new EclAktienregister();
        //				blWillenserklaerung.pEclAktienregisterEintrag.aktienregisterIdent=eclTeilnehmerLoginM.getAnmeldeIdentAktienregister();
        //				blWillenserklaerung.anmeldungenAusAktienregisterStornieren_pruefe(lDbBundle);
        //				if (blWillenserklaerung.rcIstZulaessig && +blWillenserklaerung.rcAnzahlMeldungen==2 && zusammenfassenVonAnmeldungenMoeglich==true){
        //				/*Zwei Anmeldungen existieren, diese können storniert werden => Stornieren und eine neue Anmeldung erzeugen*/
        //					/*******Stornieren*******/
        //					blWillenserklaerung=new BlWillenserklaerung();
        //					blWillenserklaerung.pErteiltAufWeg=aDlgVariablen.getEingabeQuelle();
        //					blWillenserklaerung.pErteiltZeitpunkt=aDlgVariablen.getErteiltZeitpunkt();
        //					blWillenserklaerung.pEclAktienregisterEintrag=new EclAktienregister();
        //					blWillenserklaerung.pEclAktienregisterEintrag.aktienregisterIdent=eclTeilnehmerLoginM.getAnmeldeIdentAktienregister();
        //					blWillenserklaerung.anmeldungenAusAktienregisterStornieren(lDbBundle);
        //					
        //					
        //					/******Neu Anmelden mit EINEM Aktienbestand*****/
        //					blWillenserklaerung=new BlWillenserklaerung();
        //					blWillenserklaerung.pErteiltAufWeg=aDlgVariablen.getEingabeQuelle();
        //					blWillenserklaerung.pErteiltZeitpunkt=aDlgVariablen.getErteiltZeitpunkt();
        //					/*Aktienregister füllen*/
        //					EclAktienregister aktienregisterEintrag=new EclAktienregister();
        //					aktienregisterEintrag.aktionaersnummer=eclTeilnehmerLoginM.getAnmeldeAktionaersnummer();
        //					erg=lDbBundle.dbAktienregister.leseZuAktienregisterEintrag(aktienregisterEintrag);
        //					if (erg<=0){/*Aktienregistereintrag nicht mehr vorhanden*/
        //						/*Dann schwerwiegender Fehler - denn dann hätte das Programm schon vorher rausfliegen müssen ....*/
        //						aDlgVariablen.setFehlerMeldung(eclTextePortalM.getFehlertext(CaFehler.afAktienregisterEintragNichtMehrVorhanden));
        //						aDlgVariablen.setFehlerNr(CaFehler.afAktienregisterEintragNichtMehrVorhanden);
        //						return "";
        //					}
        //
        //					aktienregisterEintrag=lDbBundle.dbAktienregister.ergebnisPosition(0);
        //					blWillenserklaerung.pEclAktienregisterEintrag=aktienregisterEintrag;
        //
        //					/*Restliche Parameter füllen*/
        //					blWillenserklaerung.pAktienAnmelden=-1; /*Alle Aktien anmelden*/
        //					blWillenserklaerung.pAnmeldungFix=false; /*Nicht "Fix" anmelden*/
        //					blWillenserklaerung.pAnzahlAnmeldungen=1;
        //					blWillenserklaerung.pWillenserklaerungGeberIdent=-1; /*Aktionär gibt in diesem Fall*/
        //					blWillenserklaerung.pPersonNatJurFuerAnmeldungVerwenden=eclTeilnehmerLoginM.getAnmeldeIdentPersonenNatJur();
        //
        //					blWillenserklaerung.anmeldungAusAktienregister(lDbBundle);
        //
        //					if (blWillenserklaerung.rcIstZulaessig==false){
        //						aDlgVariablen.setFehlerMeldung(eclTextePortalM.getFehlertext(blWillenserklaerung.rcGrundFuerUnzulaessig));
        //						aDlgVariablen.setFehlerNr(blWillenserklaerung.rcGrundFuerUnzulaessig);
        //						return "";
        //
        //					}
        //
        //					
        //					/*Willenserklärungsbasis neu einlesen*/
        //					lWillenserklaerungStatus.leseMeldungenZuAktienregisterIdent(eclTeilnehmerLoginM.getAnmeldeIdentAktienregister());
        //
        //					
        //					
        //			}
        //
        //			}
        //			break;}
        //		case 3:{/*Login ist personNatJur*/
        //			if (eclTeilnehmerLoginM.getInstiIdent()>0) {
        //				lWillenserklaerungStatus.leseMeldungenEigeneAktienZuInstiIdent(eclTeilnehmerLoginM.getInstiIdent());
        //			}
        //			else {
        //				lWillenserklaerungStatus.leseMeldungenEigeneAktienZuPersonNatJur(eclTeilnehmerLoginM.getAnmeldeIdentPersonenNatJur());
        //			}
        //			break;}
        //		
        //		}
        //
        //		lWillenserklaerungStatus.leseMeldungenEigeneGastkartenZuPersonNatJur(eclTeilnehmerLoginM.getAnmeldeIdentPersonenNatJur());
        //		lWillenserklaerungStatus.leseMeldungenBevollmaechtigtZuPersonNatJur(eclTeilnehmerLoginM.getAnmeldeIdentPersonenNatJur());
        //		lWillenserklaerungStatus.ergaenzeZugeordneteMeldungenUmWillenserklaerungen(eclTeilnehmerLoginM.getAnmeldeIdentPersonenNatJur());
        //		
        //		
        //		/*Liste mit zugeordneten Meldungen / Willenserklärungen füllen*/
        //		eclZugeordneteMeldungListeM.zugeordneteMeldungenEigeneAktienCopyFrom(lWillenserklaerungStatus.zugeordneteMeldungenEigeneAktienArray, mitStorno);
        //		eclZugeordneteMeldungListeM.setGastKartenGemeldet(lWillenserklaerungStatus.gastKartenGemeldetEigeneAktien);
        //		eclZugeordneteMeldungListeM.zugeordneteMeldungenEigeneGastkartenCopyFrom(lWillenserklaerungStatus.zugeordneteMeldungenEigeneGastkartenArray, mitStorno);
        //		eclZugeordneteMeldungListeM.zugeordneteMeldungenBevollmaechtigtCopyFrom(lWillenserklaerungStatus.zugeordneteMeldungenBevollmaechtigtArray, mitStorno);
        //		eclZugeordneteMeldungListeM.setBriefwahlVorhanden(lWillenserklaerungStatus.briefwahlVorhanden);
        //		eclZugeordneteMeldungListeM.setSrvVorhanden(lWillenserklaerungStatus.srvVorhanden);
        //		aDlgVariablen.setAusgewaehlteHauptAktion("2");
        //		aDlgVariablen.setGastKarte(false);
        //
        //
        //		
        //		/**Nun Möglichkeiten für virtuelle HV in Abhängigkeit vom Login setzen*/
        //		aDlgVariablen.setMitteilungenAngebotenLogin(1);
        //		aDlgVariablen.setStreamAngebotenLogin(1);
        //		aDlgVariablen.setFragenAngebotenLogin(1);
        //		aDlgVariablen.setEinstellungenAngebotenLogin(1);
        //		aDlgVariablen.setTeilnehmerverzAngebotenLogin(1);
        //		aDlgVariablen.setAbstimmungsergAngebotenLogin(1);
        //		
        //		if (eclTeilnehmerLoginM.getAnmeldeKennungArt()==3) {
        //			aDlgVariablen.setEinstellungenAngebotenLogin(0);
        //			if (eclTeilnehmerLoginM.getInstiIdent()==-1) {
        //				aDlgVariablen.setMitteilungenAngebotenLogin(0);
        //				aDlgVariablen.setFragenAngebotenLogin(0);
        //				aDlgVariablen.setTeilnehmerverzAngebotenLogin(0);
        //				aDlgVariablen.setAbstimmungsergAngebotenLogin(0);
        //			}
        //		}
        //		
        //		
        //		CaBug.druckeLog("AFunktionen.WaehleAusgangsmaske eclParamM.getParam().paramPortal.varianteDialogablauf="+eclParamM.getParam().paramPortal.varianteDialogablauf, logDrucken);
        //		String auswahlMaske=aDlgVariablen.getAuswahlMaske();
        //		if (eclParamM.getParam().paramPortal.varianteDialogablauf==1) {
        //			/**Bei Dialog-Variante 1 (ku178) - Ausgangsmaske ist immer aAuswahl1*/
        //			if (auswahlMaske.isEmpty()) {
        //				return "aAuswahl1";
        //			}
        //			else {
        //				aDlgVariablen.setAuswahlMaske("");
        //				return auswahlMaske;
        //			}
        //		}
        //
        //		
        //		if (auswahlMaske.isEmpty()) {
        //			return "aStatus";
        //		}
        //		else {
        //			aDlgVariablen.setAuswahlMaske("");
        //			return auswahlMaske;
        //		}

    }

    /**Status-Einlesen für auswahl 1*/
    /**Nur "pro-Forma" deprecated. Funktion wird nicht abgeschafft, sondern soll zukünftig nur nicht mehr direkt - sondern
     * über leseStatusPortal - aufgerufen werden.
     */
    @Deprecated
    public int leseStatusAuswahl1(DbBundle dbBundle) {
        CaBug.druckeLog("", logDrucken, 2);

        leseStatus(dbBundle, false);

        eclBesitzGesamtAuswahl1M.clearMitgliedsdaten();

        if (eclBesitzGesamtM.getBesitzJeKennungListe()==null ||
                eclBesitzGesamtM.getBesitzJeKennungListe().get(0)==null
                ) {
            CaBug.druckeLog("Keine Kennungsliste vorhanden", logDrucken, 1);
            return 1;
        }
        boolean eigenerAREintragVorhanden = eclBesitzGesamtM.getBesitzJeKennungListe().get(0).eigenerAREintragVorhanden;
        CaBug.druckeLog("eigenerAREintragVorhanden="+eigenerAREintragVorhanden, logDrucken, 10);
        eclBesitzGesamtAuswahl1M.setEigenerAREintragVorhanden(eigenerAREintragVorhanden);
        boolean erhalteneVollmachtenVorhanden = eclBesitzGesamtM.getBesitzJeKennungListe()
                .get(0).erhalteneVollmachtenVorhanden;
        eclBesitzGesamtAuswahl1M.setErhalteneVollmachtenVorhanden(erhalteneVollmachtenVorhanden);
        boolean vererbteVollmachtenVorhanden = eclBesitzGesamtM.getBesitzJeKennungListe()
                .get(0).erhalteneVollmachtenMitGesetzlichVorhanden;
        eclBesitzGesamtAuswahl1M.setVererbteVollmachtenVorhanden(vererbteVollmachtenVorhanden);

        /*+++++++++++++++++Anmelden/Abmelden vorbelegen++++++++++++++++++++++++*/
        if (eigenerAREintragVorhanden) {
            CaBug.druckeLog("B 001", logDrucken, 10);
            eclBesitzGesamtAuswahl1M.setAnmeldungMoeglich(true);

            CaBug.druckeLog("B 001-A", logDrucken, 10);
            
            EclAktienregister lAktienregister = eclBesitzGesamtM.eigenerBestandAktienregister();
            CaBug.druckeLog("B 001-B", logDrucken, 10);
            BlVeranstaltungen blVeranstaltungen = new BlVeranstaltungen(true, dbBundle);
            CaBug.druckeLog("B 001-C", logDrucken, 10);
            EclMeldung lMeldung=eclBesitzGesamtM.eigenerBestandMeldung();
            CaBug.druckeLog("B 001-D", logDrucken, 10);
           if (lMeldung==null) {
                CaBug.druckeLog("Keine Meldung vorhanden", logDrucken, 1);
                return 1;
            }
            blVeranstaltungen.gv_liefereAnmeldeStatus(lAktienregister, eclBesitzGesamtM.eigenerBestandMeldung());
            CaBug.druckeLog("B 002", logDrucken, 10);

            eclBesitzGesamtAuswahl1M.setAnOderAbgemeldet(blVeranstaltungen.rcGVanOderAbgemeldet);

            eclBesitzGesamtAuswahl1M.setAngemeldet(blVeranstaltungen.rcGVangemeldet);
            eclBesitzGesamtAuswahl1M.setAbgemeldet(blVeranstaltungen.rcGVabgemeldet);
            eclBesitzGesamtAuswahl1M.setZweiPersonenAngemeldet(blVeranstaltungen.rcGVzweiPersonenAngemeldet);
            eclBesitzGesamtAuswahl1M.setVertreterAngemeldet(blVeranstaltungen.rcGVvertreterAngemeldet);

            eclBesitzGesamtAuswahl1M.setVollmachtAnDritteEingetragen(blVeranstaltungen.rcGVvollmachtAnDritteEingetragen);
            if (blVeranstaltungen.rcGVvollmachtsPruefStatus<0) {eclBesitzGesamtAuswahl1M.setVollmachtAnDritteEingetragen(true);}
            eclBesitzGesamtAuswahl1M.setVollmachtAnDritteEingetragenEcl(blVeranstaltungen.rcGVvollmachtAnDritteEingetragenEcl);
            eclBesitzGesamtAuswahl1M.setVollmachtGesetzlichEingetragen(blVeranstaltungen.rcGVvollmachtGesetzlichEingetragen);
            if (blVeranstaltungen.rcGVvollmachtsPruefStatus<0) {eclBesitzGesamtAuswahl1M.setVollmachtGesetzlichEingetragen(false);}
            CaBug.druckeLog("blVeranstaltungen.rcGVvollmachtGesetzlichEingetragen=" + blVeranstaltungen.rcGVvollmachtGesetzlichEingetragen, logDrucken, 10);
            CaBug.druckeLog("B 003", logDrucken, 10);
            /*
            if ((blVeranstaltungen.rcGVvollmachtsPruefStatus<0 && 
                    (blVeranstaltungen.rcGVgruppe!=3 || (blVeranstaltungen.rcGVvertreterAngemeldet && blVeranstaltungen.rcGVgruppe==3))
                    && (ParamSpezial.ku287(eclParamM.getParam().mandant)==false || blVeranstaltungen.rcGVangemeldet==false)
                    
                            )
                            */
            if (blVeranstaltungen.rcGVvollmachtsPruefStatus<0 && 
                    (blVeranstaltungen.rcGVgruppe!=3 || (blVeranstaltungen.rcGVvertreterAngemeldet && blVeranstaltungen.rcGVgruppe==3))) {
                if (ParamSpezial.ku287(eclParamM.getParam().mandant)==true && blVeranstaltungen.rcGVangemeldet==true) {
                    eclBesitzGesamtAuswahl1M.setEkDruckWirdAngezeigt(true);
                }
                else {
                    eclBesitzGesamtAuswahl1M.setEkDruckWirdAngezeigt(false);
                }
            }
            else {
                eclBesitzGesamtAuswahl1M.setEkDruckWirdAngezeigt(true);
            }

            
            if (eclParamM.getParam().paramPortal.freiwilligeAnmeldungMitVertretereingabe==3) {
                /*ku178-Verfahren*/
                eclBesitzGesamtAuswahl1M.setGesetzlVertreterEingetragenNichtGeprueft(blVeranstaltungen.rcGVgesetzlVertreterEingetragenNichtGeprueft);
                eclBesitzGesamtAuswahl1M.setVertreterEingetragenNichtGeprueft(blVeranstaltungen.rcGVvertreterEingetragenNichtGeprueft);
            }
            if (blVeranstaltungen.rcGVvollmachtsPruefStatus>0) {
                eclBesitzGesamtAuswahl1M.setVollmachtAnDritteEingetragen(true);
                eclBesitzGesamtAuswahl1M.setVollmachtGesetzlichEingetragen(true);
            }
            if (blVeranstaltungen.rcGVvollmachtsPruefStatus<0) {
                eclBesitzGesamtAuswahl1M.setVollmachtAnDritteEingetragen(false);
                eclBesitzGesamtAuswahl1M.setVollmachtGesetzlichEingetragen(false);
            }

            eclBesitzGesamtAuswahl1M.setVollmachtGesetzlichEingetragenEcl(blVeranstaltungen.rcGVvollmachtGesetzlichEingetragenEcl);
            eclBesitzGesamtAuswahl1M.setListeAllerVollmachten(blVeranstaltungen.rcGVlisteAllerVollmachten);
            eclBesitzGesamtAuswahl1M.setListeAllerGesetzlichenVollmachten(blVeranstaltungen.rcGVlisteAllerGesetzlichenVollmachten);
            eclBesitzGesamtAuswahl1M.setListeAllerMitGesetzlichenVollmachten(blVeranstaltungen.rcGVlisteAllerMitGesetzlichenVollmachten);
            eclBesitzGesamtAuswahl1M.setListeAllerAnDritteVollmachten(blVeranstaltungen.rcGVlisteAllerAnDritteVollmachten);

            eclBesitzGesamtAuswahl1M.setGruppe(blVeranstaltungen.rcGVgruppe);
            
            eclBesitzGesamtAuswahl1M.setGruppenTextStartAnAbmeldung(KonstGruppen.liefereGruppenTextStartAnAbmeldung(blVeranstaltungen.rcGVgruppe));
            eclBesitzGesamtAuswahl1M.setGruppenTextAnmeldungMitVertreter(KonstGruppen.liefereGruppenTextAnmeldungMitVertreter(blVeranstaltungen.rcGVgruppe));
            eclBesitzGesamtAuswahl1M.setGruppenTextAbmeldungBestaetigung(KonstGruppen.liefereGruppenTextAbmeldungBestaetigung(blVeranstaltungen.rcGVgruppe));
            eclBesitzGesamtAuswahl1M.setGruppenTextVorVollmachtsformular(KonstGruppen.liefereGruppenTextVorVollmachtsformular(blVeranstaltungen.rcGVgruppe));
            eclBesitzGesamtAuswahl1M.setGruppenTextWeisung(KonstGruppen.liefereGruppenTextWeisung(blVeranstaltungen.rcGVgruppe));
            eclBesitzGesamtAuswahl1M.setGruppenTextWeisungAendern(KonstGruppen.liefereGruppenTextWeisungAendern(blVeranstaltungen.rcGVgruppe));
            eclBesitzGesamtAuswahl1M.setGruppenTextWeisungQuittung(KonstGruppen.liefereGruppenTextWeisungQuittung(blVeranstaltungen.rcGVgruppe));
            eclBesitzGesamtAuswahl1M.setGruppenTextVollmachtsButtonAnzeigen(KonstGruppen.liefereGruppenTextVollmachtsButtonAnzeigen(blVeranstaltungen.rcGVgruppe));
            eclBesitzGesamtAuswahl1M.setGruppenTextVollmachtsButton(KonstGruppen.liefereGruppenTextVollmachtsButton(blVeranstaltungen.rcGVgruppe));
            eclBesitzGesamtAuswahl1M.setGruppenTextVollmachtsFormularNr(KonstGruppen.liefereGruppenTextVollmachtsFormularNr(blVeranstaltungen.rcGVgruppe));
            eclBesitzGesamtAuswahl1M.setGruppenTextVollmachtsFormularFuerVertreterNr(KonstGruppen.liefereGruppenTextVollmachtsFormularFuerVertreterNr(blVeranstaltungen.rcGVgruppe));
            eclBesitzGesamtAuswahl1M.setGruppenTextVollmachtsFormularFuerVertreterButton(KonstGruppen.liefereGruppenTextVollmachtsFormularFuerVertreterButton(blVeranstaltungen.rcGVgruppe));

            eclBesitzGesamtAuswahl1M.setGruppenTextEinePerson(KonstGruppen.liefereGruppenTextEinePerson(blVeranstaltungen.rcGVgruppe));
            eclBesitzGesamtAuswahl1M.setGruppenTextZweiPersonen(KonstGruppen.liefereGruppenTextZweiPersonen(blVeranstaltungen.rcGVgruppe));
            eclBesitzGesamtAuswahl1M.setGruppenTextAbmelden(KonstGruppen.liefereGruppenTextAbmelden(blVeranstaltungen.rcGVgruppe));
            eclBesitzGesamtAuswahl1M.setGruppenTextVertreter(KonstGruppen.liefereGruppenTextVertreter(blVeranstaltungen.rcGVgruppe));
            eclBesitzGesamtAuswahl1M.setGruppenTextVertreter1nur1(KonstGruppen.liefereGruppenTextVertreter1nur1(blVeranstaltungen.rcGVgruppe));
            eclBesitzGesamtAuswahl1M.setGruppenTextVertreter1(KonstGruppen.liefereGruppenTextVertreter1(blVeranstaltungen.rcGVgruppe));
            eclBesitzGesamtAuswahl1M.setGruppenTextVertreter2(KonstGruppen.liefereGruppenTextVertreter2(blVeranstaltungen.rcGVgruppe));

            eclBesitzGesamtAuswahl1M.setVollmachtsFormularGesetzlichZulaessig(KonstGruppen.vollmachtsFormularGesetzlichZulaessig(blVeranstaltungen.rcGVgruppe));
            eclBesitzGesamtAuswahl1M.setGruppenTextGesetzlVollmachtFehlt(KonstGruppen.liefereGruppenTextGesetzlVollmachtFehlt(blVeranstaltungen.rcGVgruppe));

            eclBesitzGesamtAuswahl1M.setZweiPersonenZulaessig(blVeranstaltungen.rcGVzweiPersonenMoeglich);
            eclBesitzGesamtAuswahl1M.setGastkarteFuerMitgliedZulaessig(blVeranstaltungen.rcGVgastkarteFuerMitgliedZulaessig);
            eclBesitzGesamtAuswahl1M.setGastkarteFuerZweitePersonZulaessig(blVeranstaltungen.rcGVgastkarteFuerZweitePersonZulaessig);
            eclBesitzGesamtAuswahl1M.setSelbstAnmeldungOhneGesetzlichenVertreterMoeglich(blVeranstaltungen.rcGVselbstAnmeldungOhneGesetzlichenVertreterMoeglich);
            
            eclBesitzGesamtAuswahl1M.setEintrittskarteFuerGeneralversammlungZulaessig(
                    KonstGruppen.eintrittskarteFuerGeneralversammlungZulaessig(blVeranstaltungen.rcGVgruppe)
                            && (blVeranstaltungen.rcGVvertreterAngemeldet==false)
                            );
            
            CaBug.druckeLog("B 004", logDrucken, 10);

            if (blVeranstaltungen.rcGVteilnahmeMoeglich) {
                /**Wird nur gesetzt, wenn true - da Wert gemeinsam bestimmt wird aus Mitgliedsdaten und Bevollmächtigten-Daten*/
                eclBesitzGesamtAuswahl1M.setBestandFuerOTVorhanden(blVeranstaltungen.rcGVteilnahmeMoeglich);
            }

            CaBug.druckeLog("blVeranstaltungen.rcGVangemeldet="+blVeranstaltungen.rcGVangemeldet+" blVeranstaltungen.rcGVabgemeldet="+blVeranstaltungen.rcGVabgemeldet, logDrucken, 10);
            /**Select-Box belegen*/
            if (blVeranstaltungen.rcGVangemeldet) {
                eclBesitzGesamtAuswahl1M.setAnmeldung("1"); //Angemeldet - 1 Person 
                eclBesitzGesamtAuswahl1M.setAnmeldungAlt("1"); 
            }
            if (blVeranstaltungen.rcGVabgemeldet) {
                eclBesitzGesamtAuswahl1M.setAnmeldung("2"); //abgemeldet
                eclBesitzGesamtAuswahl1M.setAnmeldungAlt("2"); 
            }
            if (blVeranstaltungen.rcGVzweiPersonenAngemeldet) {
                eclBesitzGesamtAuswahl1M.setAnmeldung("3"); //2 Personen angemeldet
                eclBesitzGesamtAuswahl1M.setAnmeldungAlt("3"); 
            }
            if (blVeranstaltungen.rcGVvertreterAngemeldet) {
                eclBesitzGesamtAuswahl1M.setAnmeldung("4"); //Bevollmächtigter angemeldet
                eclBesitzGesamtAuswahl1M.setAnmeldungAlt("4"); 
            }
            if (blVeranstaltungen.rcGVOnlineangemeldet) {
                eclBesitzGesamtAuswahl1M.setAnmeldung("5"); //Bevollmächtigter angemeldet
                eclBesitzGesamtAuswahl1M.setAnmeldungAlt("5"); 
            }

            eclBesitzGesamtAuswahl1M.setNameVertreter1(blVeranstaltungen.rcNameVertreter1);
            eclBesitzGesamtAuswahl1M.setNameVertreter1Alt(blVeranstaltungen.rcNameVertreter1);
            eclBesitzGesamtAuswahl1M.setOrtVertreter1(blVeranstaltungen.rcOrtVertreter1);
            eclBesitzGesamtAuswahl1M.setOrtVertreter1Alt(blVeranstaltungen.rcOrtVertreter1);
            eclBesitzGesamtAuswahl1M.setNameVertreter2(blVeranstaltungen.rcNameVertreter2);
            eclBesitzGesamtAuswahl1M.setNameVertreter2Alt(blVeranstaltungen.rcNameVertreter2);
            eclBesitzGesamtAuswahl1M.setOrtVertreter2(blVeranstaltungen.rcOrtVertreter2);
            eclBesitzGesamtAuswahl1M.setOrtVertreter2Alt(blVeranstaltungen.rcOrtVertreter2);
            eclBesitzGesamtAuswahl1M.setGastkarteFuerMitglied(blVeranstaltungen.rcGastkarteFuerMitglieder);
            eclBesitzGesamtAuswahl1M.setGastkarteFuerMitgliedAlt(blVeranstaltungen.rcGastkarteFuerMitglieder);
            eclBesitzGesamtAuswahl1M.setGastkarteFuerZweitePerson(blVeranstaltungen.rcGastkarteFuerZweitePerson);
            eclBesitzGesamtAuswahl1M.setGastkarteFuerZweitePersonAlt(blVeranstaltungen.rcGastkarteFuerZweitePerson);

            eclBesitzGesamtAuswahl1M.setStatusPraesenz(blVeranstaltungen.rcStatusPraesenz);
            eclBesitzGesamtAuswahl1M.setStatusPruefung(blVeranstaltungen.rcStatusPruefung);

            if (eclBesitzGesamtAuswahl1M.isSelbstAnmeldungOhneGesetzlichenVertreterMoeglich()
                    && ((eclBesitzGesamtAuswahl1M.isAngemeldet() && !eclBesitzGesamtAuswahl1M.isVertreterAngemeldet())
                            || eclParamM.getParam().paramPortal.onlineTeilnahmeNurFuerFreiwilligAngemeldete == 0)) {
                eclBesitzGesamtAuswahl1M.setEigenerBestandOTVertretbar(true);
            } else {
                eclBesitzGesamtAuswahl1M.setEigenerBestandOTVertretbar(false);
            }

            if (eclBesitzGesamtAuswahl1M.isSelbstAnmeldungOhneGesetzlichenVertreterMoeglich()
                    && ((!eclBesitzGesamtAuswahl1M.isAngemeldet() || eclBesitzGesamtAuswahl1M.isVertreterAngemeldet())
                            && eclParamM.getParam().paramPortal.onlineTeilnahmeNurFuerFreiwilligAngemeldete == 1)) {
                eclBesitzGesamtAuswahl1M.setKeinEigenerBestandFuerOTNormalesMitglied(true);
            } else {
                eclBesitzGesamtAuswahl1M.setKeinEigenerBestandFuerOTNormalesMitglied(false);
            }

        }
        CaBug.druckeLog("B 005", logDrucken, 10);

        if ((
        /*Vollmachten vorhanden - und Kennung ist entweder Sonstiges Mitglied oder "Normal"-Mitglied (kein
        gesetzlich zu vertretendes)*/
        eclBesitzGesamtM.getBesitzJeKennungListe().get(0).getZugeordneteMeldungenBevollmaechtigtListe().size() > 0
                && ((eclLoginDatenM.getEclLoginDaten().kennungArt == KonstLoginKennungArt.aktienregister
                        && eclBesitzGesamtAuswahl1M.isSelbstAnmeldungOhneGesetzlichenVertreterMoeglich())
                        || eclLoginDatenM.getEclLoginDaten().kennungArt == KonstLoginKennungArt.personenNatJur))
                || /*Vererbte Vollmachten vorhanden*/
                eclBesitzGesamtM.getBesitzJeKennungListe().get(0)
                        .getZugeordneteMeldungenBevollmaechtigtMitGesetzlichListe().size() > 0) {
            eclBesitzGesamtAuswahl1M.setBestandFuerOTVorhanden(true);
        }

        if (eclBesitzGesamtAuswahl1M.isBestandFuerOTVorhanden() == false) {
            if (eclLoginDatenM.getEclLoginDaten().kennungArt == KonstLoginKennungArt.personenNatJur) {
                eclBesitzGesamtAuswahl1M.setKeinBestandFuerOTSonstigePerson(true);
            } else {
                /*Aktionär*/
                if (eclBesitzGesamtAuswahl1M.isSelbstAnmeldungOhneGesetzlichenVertreterMoeglich()) {
                    eclBesitzGesamtAuswahl1M.setKeinBestandFuerOTNormalesMitglied(true);
                } else {
                    eclBesitzGesamtAuswahl1M.setKeinBestandFuerOTGesetzlichZuVertretendesMitglied(true);
                }
            }
        }
        if (eclBesitzGesamtAuswahl1M.isEigenerBestandOTVertretbar()) {
            eclBesitzGesamtAuswahl1M.setAnzPraesenteVorhanden(eclBesitzGesamtM.getAnzPraesenteVorhandenEigene()
                    + eclBesitzGesamtM.getAnzPraesenteVorhandenVertretene());
            eclBesitzGesamtAuswahl1M
                    .setAnzNichtPraesenteVorhanden(eclBesitzGesamtM.getAnzNichtPraesenteVorhandenEigene()
                            + eclBesitzGesamtM.getAnzNichtPraesenteVorhandenVertretene());
        } else {
            eclBesitzGesamtAuswahl1M.setAnzPraesenteVorhanden(eclBesitzGesamtM.getAnzPraesenteVorhandenVertretene());
            eclBesitzGesamtAuswahl1M
                    .setAnzNichtPraesenteVorhanden(eclBesitzGesamtM.getAnzNichtPraesenteVorhandenVertretene());
        }
        return 1;
    }

    /**+++++++++++++++++++++++++++++++++Ergänze EclBesitzGesamtM um Variablenwerte, die nur in Zusammenhang mit den aktiven Portal-Parametern ermittelt werden können++++++++++++*/

    private void ergaenzeBesitzGesamtM() {
        for (int i = 0; i < eclBesitzGesamtM.getBesitzJeKennungListe().size(); i++) {
            EclBesitzJeKennung besitzJeKennung = eclBesitzGesamtM.getBesitzJeKennungListe().get(i);
            ergaenzeBesitzJeKennung(besitzJeKennung);
        }

    }

    private void ergaenzeBesitzJeKennung(EclBesitzJeKennung besitzJeKennung) {
        for (int i = 0; i < besitzJeKennung.eigenerAREintragListe.size(); i++) {
            EclBesitzAREintrag besitzAREintrag = besitzJeKennung.eigenerAREintragListe.get(i);
            ergaenzeAREintrag(besitzAREintrag);
        }

        for (int i = 0; i < besitzJeKennung.zugeordneteMeldungenEigeneGastkartenListe.size(); i++) {
            EclZugeordneteMeldungNeu zugeordneteMeldung = besitzJeKennung.zugeordneteMeldungenEigeneGastkartenListe
                    .get(i);
            ergaenzeZugeordneteMeldung(zugeordneteMeldung);
        }

        for (int i = 0; i < besitzJeKennung.zugeordneteMeldungenBevollmaechtigtListe.size(); i++) {
            EclZugeordneteMeldungNeu zugeordneteMeldung = besitzJeKennung.zugeordneteMeldungenBevollmaechtigtListe
                    .get(i);
            ergaenzeZugeordneteMeldung(zugeordneteMeldung);
        }

        for (int i = 0; i < besitzJeKennung.instiAREintraegeListe.size(); i++) {
            EclBesitzAREintrag besitzAREintrag = besitzJeKennung.instiAREintraegeListe.get(i);
            ergaenzeAREintrag(besitzAREintrag);
        }

        for (int i = 0; i < besitzJeKennung.zugeordneteMeldungenInstiListe.size(); i++) {
            EclZugeordneteMeldungNeu zugeordneteMeldung = besitzJeKennung.zugeordneteMeldungenInstiListe.get(i);
            ergaenzeZugeordneteMeldung(zugeordneteMeldung);
        }

    }

    private void ergaenzeAREintrag(EclBesitzAREintrag besitzAREintrag) {
        for (int i = 0; i < besitzAREintrag.zugeordneteMeldungenListe.size(); i++) {
            EclZugeordneteMeldungNeu zugeordneteMeldung = besitzAREintrag.zugeordneteMeldungenListe.get(i);
            ergaenzeZugeordneteMeldung(zugeordneteMeldung);
        }
    }

    private void ergaenzeZugeordneteMeldung(EclZugeordneteMeldungNeu zugeordneteMeldung) {
        int laenge = zugeordneteMeldung.zugeordneteWillenserklaerungenList.size();
        for (int i = 0; i < laenge; i++) {
            EclWillenserklaerungStatusNeu willenserklaerungStatus = zugeordneteMeldung.zugeordneteWillenserklaerungenList
                    .get(i);
            ergaenzeZugeordneteWillenserklaerung(willenserklaerungStatus);

        }

        //@formatter:off
        zugeordneteMeldung.weitereWillenserklaerungMoeglich = (
                /*Vollmacht Dritte immer möglich, wenn aktiv*/
                tSession.isPortalVollmachtDritteIstMoeglich() ||
                /*Noch keine EK vergeben, und keine Weisung erteilt, und angeboten*/
                (zugeordneteMeldung.anzPersZutrittsIdentSelbst + zugeordneteMeldung.anzPersZutrittsIdentVollmacht
                        + zugeordneteMeldung.anzPersKIAVSRV == 0
                        && (tSession.isPortalEKIstMoeglich() || tSession.isPortalSRVIstMoeglich()
                                || tSession.isPortalBriefwahlIstMoeglich() || tSession.isPortalKIAVIstMoeglich()))
                ||
                /*Falls Eintrittskarten aktiv: Ek an Dritte immer möglich? oder keine EK erteilt und Weisung und EK gleichzeitig möglich*/
                (tSession.isPortalEKIstMoeglich() && (eclParamM.getParam().paramPortal.zusaetzlicheEKDritteMoeglich == 1
                //							|| Herausgenommen, da in obiger Bedingung bereits enthalten (noch nichts erteilt)
                //							(anzZutrittsIdentSelbst+anzZutrittsIdentVollmacht+anzKIAVSRV==0)
                || (zugeordneteMeldung.anzPersZutrittsIdentSelbst
                        + zugeordneteMeldung.anzPersZutrittsIdentVollmacht == 0
                        && eclParamM.getParam().paramPortal.ekUndWeisungGleichzeitigMoeglich == 1)))
                ||
                /*Falls was mit Weisungen angeboten, und noch nichts erteilt, oder Ek erteilt und Weisungen und EK gleichzeitig möglich*/
                ((tSession.isPortalSRVIstMoeglich() || tSession.isPortalBriefwahlIstMoeglich()
                        || tSession.isPortalKIAVIstMoeglich())
                        && ((zugeordneteMeldung.anzPersZutrittsIdentSelbst
                                + zugeordneteMeldung.anzPersZutrittsIdentVollmacht
                                + zugeordneteMeldung.anzPersKIAVSRV == 0) || (

                                        zugeordneteMeldung.anzPersKIAVSRV == 0
                                        && eclParamM.getParam().paramPortal.ekUndWeisungGleichzeitigMoeglich == 1))));

        zugeordneteMeldung.weitereEKMoeglich = ((zugeordneteMeldung.anzPersZutrittsIdentSelbst
                + zugeordneteMeldung.anzPersZutrittsIdentVollmacht + zugeordneteMeldung.anzPersKIAVSRV == 0
                && (tSession.isPortalEKIstMoeglich()))
                || (tSession.isPortalEKIstMoeglich()
                        && (eclParamM.getParam().paramPortal.zusaetzlicheEKDritteMoeglich == 1
                        || (zugeordneteMeldung.anzPersZutrittsIdentSelbst
                                + zugeordneteMeldung.anzPersZutrittsIdentVollmacht
                                + zugeordneteMeldung.anzPersKIAVSRV == 0)
                        || (zugeordneteMeldung.anzPersZutrittsIdentSelbst
                                + zugeordneteMeldung.anzPersZutrittsIdentVollmacht == 0
                                && eclParamM.getParam().paramPortal.ekUndWeisungGleichzeitigMoeglich == 1))));

        zugeordneteMeldung.weitereSRVMoeglich = (zugeordneteMeldung.bereitsErteiltWeisungBriefwahlAllgemeinDurchAndere == false
                && tSession.isPortalSRVIstMoeglich()
                && (zugeordneteMeldung.anzPersZutrittsIdentSelbst + zugeordneteMeldung.anzPersZutrittsIdentVollmacht
                        + zugeordneteMeldung.anzPersKIAVSRV == 0
                        || (zugeordneteMeldung.anzPersKIAVSRV == 0
                        && eclParamM.getParam().paramPortal.ekUndWeisungGleichzeitigMoeglich == 1)
                        ||
                        /*Singulus - SRV möglich, wenn auch Briefwahl schon erteilt wurde*/
                        (zugeordneteMeldung.anzPersSRV == 0
                        && eclParamM.getParam().paramPortal.srvZusaetzlichZuBriefwahlMoeglich == 1)));

        zugeordneteMeldung.weitereKIAVMoeglich = (zugeordneteMeldung.bereitsErteiltWeisungBriefwahlAllgemeinDurchAndere == false
                && tSession.isPortalKIAVIstMoeglich()
                && (zugeordneteMeldung.anzPersZutrittsIdentSelbst + zugeordneteMeldung.anzPersZutrittsIdentVollmacht
                        + zugeordneteMeldung.anzPersKIAVSRV == 0
                        || (zugeordneteMeldung.anzPersKIAVSRV == 0
                        && eclParamM.getParam().paramPortal.ekUndWeisungGleichzeitigMoeglich == 1)));

        zugeordneteMeldung.weitereBriefwahlMoeglich = (zugeordneteMeldung.bereitsErteiltWeisungBriefwahlAllgemeinDurchAndere == false
                && tSession.isPortalBriefwahlIstMoeglich()
                && (zugeordneteMeldung.anzPersZutrittsIdentSelbst + zugeordneteMeldung.anzPersZutrittsIdentVollmacht
                        + zugeordneteMeldung.anzPersKIAVSRV == 0
                        || (zugeordneteMeldung.anzPersKIAVSRV == 0
                        && eclParamM.getParam().paramPortal.ekUndWeisungGleichzeitigMoeglich == 1)
                        ||
                        /*ku168*/
                        (zugeordneteMeldung.anzPersBriefwahl == 0
                        && eclParamM.getParam().paramPortal.briefwahlZusaetzlichZuSRVMoeglich == 1)));

        zugeordneteMeldung.weitereVollmachtDritteMoeglich = tSession.isPortalVollmachtDritteIstMoeglich();
        zugeordneteMeldung.weitereEKSelbstMoeglich = ((tSession.isPortalEKIstMoeglich())
                && ((zugeordneteMeldung.anzPersKIAVSRV == 0
                || eclParamM.getParam().paramPortal.ekUndWeisungGleichzeitigMoeglich == 1)
                        && (zugeordneteMeldung.anzPersZutrittsIdentSelbst
                                + zugeordneteMeldung.anzPersZutrittsIdentVollmacht == 0
                                || eclParamM.getParam().paramPortal.zusaetzlicheEKDritteMoeglich == 1)
                        && (zugeordneteMeldung.anzPersZutrittsIdentSelbst == 0)

                        ));

        zugeordneteMeldung.weitereEKDritteMoeglich = ((tSession.isPortalEKIstMoeglich())
                && ((zugeordneteMeldung.anzPersKIAVSRV == 0
                || eclParamM.getParam().paramPortal.ekUndWeisungGleichzeitigMoeglich == 1)
                        && (zugeordneteMeldung.anzPersZutrittsIdentSelbst
                                + zugeordneteMeldung.anzPersZutrittsIdentVollmacht == 0
                                || eclParamM.getParam().paramPortal.zusaetzlicheEKDritteMoeglich == 1)
                        && (zugeordneteMeldung.anzPersZutrittsIdentVollmacht == 0
                        || eclParamM.getParam().paramPortal.zusaetzlicheEKDritteMoeglich == 1)

                        ));

        if (zugeordneteMeldung.weitereEKMoeglich == true || zugeordneteMeldung.weitereSRVMoeglich == true
                || zugeordneteMeldung.weitereKIAVMoeglich == true
                || zugeordneteMeldung.weitereBriefwahlMoeglich == true) {
            zugeordneteMeldung.weitereWillenserklaerungMoeglichNurVollmachtDritte = false;
        } else {
            zugeordneteMeldung.weitereWillenserklaerungMoeglichNurVollmachtDritte = true;

        }

        zugeordneteMeldung.weisungNichtMoeglichWeilVollmachtDritte=
                (
                        /*Parameter entsprechend eingestellt*/
                    eclParamM.getParam().paramPortal.vollmachtDritteUndAndereWKMoeglich==0 
                    && 
                    /*Vollmacht an Dritte bereits vorhanden*/
                    (zugeordneteMeldung.anzPersZutrittsIdentVollmacht>0 || zugeordneteMeldung.anzPersVollmachtenDritte>0)
                );

        zugeordneteMeldung.vollmachtDritteNichtMoeglichWeilWeisungErteilt=
                (
                        /*Parameter entsprechend eingestellt*/
                    eclParamM.getParam().paramPortal.vollmachtDritteUndAndereWKMoeglich==0
                    &&
                    /*Weisung bereits vorhanden*/
                    zugeordneteMeldung.anzAlleKIAVSRV>0
                 );

        
        zugeordneteMeldung.vollmachtDritteNichtMoeglichWeilBereitsVollmacht=
                (
                        /*Parameter entsprechend eingestellt*/
                    eclParamM.getParam().paramPortal.vollmachtDritteUndAndereWKMoeglich==0
                    &&
                    /*Vollmacht an Dritte bereits vorhanden*/
                    (zugeordneteMeldung.anzPersZutrittsIdentVollmacht>0 || zugeordneteMeldung.anzPersVollmachtenDritte>0)
                );

        //@formatter:on

        if (laenge == 0 && (zugeordneteMeldung.artBeziehung == 1 || zugeordneteMeldung.artBeziehung == 4)) {
            /**Meldung gehört zu zugeordnetem Aktienregistereintrag, und hat keine Willenserklärung => Dummy Willenserklärung anfügen*/
            EclWillenserklaerungStatusNeu lWillenserklaerungStatusNeu = new EclWillenserklaerungStatusNeu();
            lWillenserklaerungStatusNeu.istLeerDummy = true;
            zugeordneteMeldung.zugeordneteWillenserklaerungenList.add(lWillenserklaerungStatusNeu);

        }
    }

    private void ergaenzeZugeordneteWillenserklaerung(EclWillenserklaerungStatusNeu willenserklaerungStatus) {
        willenserklaerungStatus.aendernIstZulaessigPortal = willenserklaerungStatus.aendernIstZulaessig
                & willenserklaerungPortalAktiv(willenserklaerungStatus.willenserklaerung);
        willenserklaerungStatus.stornierenIstZulaessigPortal = (willenserklaerungStatus.stornierenIstZulaessig
                && willenserklaerungPortalAktiv(willenserklaerungStatus.willenserklaerung));

    }

    /*********************Re-Check vor Willenserklärungsspeicherung********************************/
    /**Für Neuanmeldung: prüft, ob nachwievor keine Anmeldungen für den eingeloggten Aktionär vorhanden sind.
     * 
     * Wird immer nochmal aufgerufen, bevor tatsächlich eine Anmeldung durchgeführt wird (in den Routinen selbst),
     * um sicherzustellen dass zwischen "Ersteingabe" und tatsächlichem Anmelden nicht zwischendurch von
     * einem anderen Arbeitsplatz aus mittlerweile eine Anmeldung erfolgt ist.
     * 
     * true -> es sind noch keine Anmeldungen vorhanden
     * false -> es sind bereits Anmeldungen vorhanden
     * 
     * 
     * TODO - funktioniert so nicht, wird aber auch nicht verwendet aktuell - ggf. überprüfen*/
    @Deprecated
    public boolean reCheckKeineAktienanmeldungen(DbBundle lDbBundle) {
        return reCheckKeineAktienanmeldungen(lDbBundle,
                tWillenserklaerungSession.getBesitzAREintrag().aktienregisterEintrag.aktienregisterIdent);
    }

    public boolean reCheckKeineAktienanmeldungen(DbBundle lDbBundle, int pAktienregisterIdent) {
        BlWillenserklaerungStatus lWillenserklaerungStatus = new BlWillenserklaerungStatus(lDbBundle);
        lWillenserklaerungStatus.leseMeldungenZuAktienregisterIdent(pAktienregisterIdent);
        CaBug.druckeLog("pAktienregisterIdent=" + pAktienregisterIdent, logDrucken, 10);

        if (lWillenserklaerungStatus.zugeordneteMeldungenEigeneAktienArray == null
                || lWillenserklaerungStatus.zugeordneteMeldungenEigeneAktienArray.length == 0) {
            CaBug.druckeLog("return true", logDrucken, 10);
            return true;
        }

        CaBug.druckeLog("return false", logDrucken, 10);
        return false;
    }

    /**Checken, ob für eine Meldungsident keine höhere Willenserklärung vergeben wurde, als übergeben
     * 
     * Verwendung: wurde die gerade Bearbeitete meldung anderweitig schon verändert?
     */
    public boolean reCheckKeineNeueWillenserklaerungen(DbBundle lDbBundle, int meldeIdent,
            int bisherHoechsteWillenserklaerungIdent) {
        if (lDbBundle.param.paramPortal.multiUserImPortalIgnorieren==1) {
            return true;
        }
        int anz;
        EclMeldung lMeldung = new EclMeldung();
        lMeldung.meldungsIdent = meldeIdent;
        anz = lDbBundle.dbWillenserklaerung.ermittleHoechsteWillenserklaerungIdentZuMeldung(lMeldung);
        if (anz == bisherHoechsteWillenserklaerungIdent) {
            return true;
        }

        return false;
    }

    /****************Willenserkläerung - aktiv-Abfrage*************************************/
    public boolean willenserklaerungPortalAktiv(int pWillenserklaerungIdent) {
        CaBug.druckeLog("pWillenserklaerungIdent=" + pWillenserklaerungIdent, logDrucken, 10);
        switch (pWillenserklaerungIdent) {
        case KonstWillenserklaerung.neueZutrittsIdentZuMeldung: {
            if (eclParamM.getParam().paramPortal.ekSelbstMoeglich == 0 || tSession.isPortalEKIstMoeglich() == false) {
                return false;
            }
            break;
        }
        case KonstWillenserklaerung.neueZutrittsIdentZuMeldung_VollmachtAnDritte: {
            if (eclParamM.getParam().paramPortal.ekVollmachtMoeglich == 0
                    || tSession.isPortalEKIstMoeglich() == false) {
                return false;
            }
            break;
        }
        case KonstWillenserklaerung.vollmachtUndWeisungAnSRV: {
            if (eclParamM.getParam().paramPortal.srvAngeboten == 0 || tSession.isPortalSRVIstMoeglich() == false) {
                return false;
            }
            break;
        }
        case KonstWillenserklaerung.briefwahl: {
            if (eclParamM.getParam().paramPortal.briefwahlAngeboten == 0
                    || tSession.isPortalBriefwahlIstMoeglich() == false) {
                return false;
            }
            break;
        }
        case KonstWillenserklaerung.vollmachtAnDritte: {
            if (eclParamM.getParam().paramPortal.vollmachtDritteAngeboten == 0
                    || tSession.isPortalVollmachtDritteIstMoeglich() == false) {
                return false;
            }
            break;
        }
        }
        return true;
    }

    /****************Welche Auswahl soll aufgerufen werden nach div. Funktionen************************************/
    /**Nach Abgabe einer Willenserklärung*/
    public int waehleAuswahl() {
        if (eclParamM.getParam().paramPortal.varianteDialogablauf == 1) {
            if (tWillenserklaerungSession.getIntAusgewaehlteAktion()==4
                    || tWillenserklaerungSession.getIntAusgewaehlteAktion()==10
                    || tWillenserklaerungSession.getIntAusgewaehlteAktion()==13
                                        ) {
                return KonstPortalView.AUSWAHL1_GENERALVERSAMMLUNG_BEIRATSWAHL;
            }
            else {
                return KonstPortalView.AUSWAHL1_GENERALVERSAMMLUNG_BRIEFWAHL;
            }
        
        }
        return KonstPortalView.AUSWAHL;
    }

//    /**Bei Zurück in neuer Willenserklärung*/
//    public int waehleAuswahlZurueckNeueWillenserklaerung() {
//        if (eclParamM.getParam().paramPortal.varianteDialogablauf == 1) {
//            return KonstPortalView.AUSWAHL1_GENERALVERSAMMLUNG;
//        }
//        return KonstPortalView.neueWillenserklaerung;
//    }

    /**Nach / bei Präsenzfunktionen*/
    public int waehleAuswahlNachPraesenzfunktion() {
        if (eclParamM.getParam().paramPortal.varianteDialogablauf == 1) {
            return KonstPortalView.AUSWAHL1_TEILNAHME;
        }
        return KonstPortalView.AUSWAHL;
    }

    public boolean isZusammenfassenVonAnmeldungenMoeglich() {
        return zusammenfassenVonAnmeldungenMoeglich;
    }

    public void setZusammenfassenVonAnmeldungenMoeglich(boolean zusammenfassenVonAnmeldungenMoeglich) {
        this.zusammenfassenVonAnmeldungenMoeglich = zusammenfassenVonAnmeldungenMoeglich;
    }


    public boolean isWeitereKennungenZuladen() {
        return weitereKennungenZuladen;
    }

    public void setWeitereKennungenZuladen(boolean weitereKennungenZuladen) {
        this.weitereKennungenZuladen = weitereKennungenZuladen;
    }

    public boolean isAlleWillenserklaerungen() {
        return alleWillenserklaerungen;
    }

    public void setAlleWillenserklaerungen(boolean alleWillenserklaerungen) {
        this.alleWillenserklaerungen = alleWillenserklaerungen;
    }

    public boolean isUpdateTblLogin() {
        return updateTblLogin;
    }

    public void setUpdateTblLogin(boolean updateTblLogin) {
        this.updateTblLogin = updateTblLogin;
    }

    
}
