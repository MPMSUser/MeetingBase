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
import de.meetingapps.meetingportal.meetComAllg.CaFehler;
import de.meetingapps.meetingportal.meetComBlManaged.BlMAbstimmung;
import de.meetingapps.meetingportal.meetComBlManaged.BlMAbstimmungsvorschlag;
import de.meetingapps.meetingportal.meetComEclM.EclAbstimmungenListeM;
import de.meetingapps.meetingportal.meetComEclM.EclDbM;
import de.meetingapps.meetingportal.meetComEntities.EclAktienregister;
import de.meetingapps.meetingportal.meetComEntities.EclBesitzAREintrag;
import de.meetingapps.meetingportal.meetComEntities.EclZugeordneteMeldungNeu;
import de.meetingapps.meetingportal.meetComKonst.KonstPortalAktion;
import de.meetingapps.meetingportal.meetComKonst.KonstPortalFehlerView;
import de.meetingapps.meetingportal.meetComKonst.KonstPortalView;
import de.meetingapps.meetingportal.meetComKonst.KonstSkIst;
import de.meetingapps.meetingportal.meetComKonst.KonstWeisungserfassungSicht;
import de.meetingapps.meetingportal.meetingportTFunktionen.TPruefeStartNachOpen;
import de.meetingapps.meetingportal.meetingportTFunktionen.TSessionVerwaltung;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@RequestScoped
@Named
public class TNeueWillenserklaerung {

    private @Inject TSessionVerwaltung tSessionVerwaltung;
    private @Inject EclDbM eclDbM;

    private @Inject TWillenserklaerungSession tWillenserklaerungSession;
    private @Inject TSession tSession;
    private @Inject TFehlerViewSession tFehlerViewSession;

    private @Inject BlMAbstimmung blMAbstimmung;
    private @Inject BlMAbstimmungsvorschlag blMAbstimmungsvorschlag;
    private @Inject TPruefeStartNachOpen tPruefeStartNachOpen;
    private @Inject EclAbstimmungenListeM eclAbstimmungenListeM;

    /*******************************Anmelden ohne sonst was ***************/
    public void doAnmeldenOhneWK(EclBesitzAREintrag pEclBesitzAREintrag) {

        if (!tSessionVerwaltung.pruefeStart(KonstPortalView.AUSWAHL)) {
            return;
        }
        try {

            tWillenserklaerungSession.clear();
            tWillenserklaerungSession.setAusgewaehlteHauptAktion(KonstPortalAktion.HAUPT_NEUANMELDUNG);
            tWillenserklaerungSession.setAusgewaehlteAktion(KonstPortalAktion.NUR_ANMELDUNG_OHNE_WEITERE_WILLENSERKLAERUNG);

            eclDbM.openAll();
            if (!tPruefeStartNachOpen.pruefeNachOpenPortalAktuelleHauptAktionAktion()) {
                eclDbM.closeAll();
                return;
            }

            tWillenserklaerungSession.initBesitzAREintragListe();
            tWillenserklaerungSession.addBesitzAREintragListe(pEclBesitzAREintrag);
            tWillenserklaerungSession.ermittleGattungenFuerBesitzAREintragListe();
            tWillenserklaerungSession.ermittleGattungFuerEinzigenBesitzAREintragList();

            eclDbM.closeAll();
        } catch (Exception e) {
            CaBug.drucke("Exception");
            System.out.println(e.getMessage());
            e.printStackTrace();
            eclDbM.closeAll();
        }

        tSessionVerwaltung.setzeEnde(KonstPortalView.NUR_ANMELDUNG);
        return;

    }

    /*******************************Eintrittskarte allgemein******************************************/

    /** Voraussetzung: dbBundle geöffnet*/
    public boolean check2EK(EclBesitzAREintrag pEclBesitzAREintrag) {

        EclAktienregister aktienregisterEintrag = null;

        aktienregisterEintrag = new EclAktienregister();
        aktienregisterEintrag.aktionaersnummer = pEclBesitzAREintrag.aktienregisterEintrag.aktionaersnummer;
        eclDbM.getDbBundle().dbAktienregister.leseZuAktienregisterEintrag(aktienregisterEintrag);
        if (eclDbM.getDbBundle().dbAktienregister.anzErgebnis() == 0) {
            CaBug.drucke("001");
        }
        aktienregisterEintrag = eclDbM.getDbBundle().dbAktienregister.ergebnisPosition(0);

        if (aktienregisterEintrag.stueckAktien <= 1) {
            tSession.trageFehlerEin(CaFehler.afMindestens2AktienErforderlich);
            return false;
        }

        return true;
    }

    /** Voraussetzung: dbBundle geöffnet*/
    public boolean check2EK(EclZugeordneteMeldungNeu pEclZugeordneteMeldungNeu) {

        EclAktienregister aktienregisterEintrag = null;

        aktienregisterEintrag = new EclAktienregister();
        aktienregisterEintrag.aktionaersnummer = pEclZugeordneteMeldungNeu.eclMeldung.aktionaersnummer;
        eclDbM.getDbBundle().dbAktienregister.leseZuAktienregisterEintrag(aktienregisterEintrag);
        if (eclDbM.getDbBundle().dbAktienregister.anzErgebnis() == 0) {
            CaBug.drucke("001");
        }
        aktienregisterEintrag = eclDbM.getDbBundle().dbAktienregister.ergebnisPosition(0);

        if (aktienregisterEintrag.stueckAktien <= 1) {
            tSession.trageFehlerEin(CaFehler.afMindestens2AktienErforderlich);
            return false;
        }

        return true;
    }

    /**Je nach pHauptAktion entweder pEclBesitzAREintrag oder pEclZugeordneteMeldungNeu als null übergeben*/
    public void startAusstellenEK(int pHauptAktion, int pAktion, EclBesitzAREintrag pEclBesitzAREintrag, EclZugeordneteMeldungNeu pEclZugeordneteMeldungNeu) {
        if (!tSessionVerwaltung.pruefeStart(KonstPortalView.AUSWAHL)) {
            return;
        }
        try {

        tWillenserklaerungSession.clear();
        tWillenserklaerungSession.setAusgewaehlteHauptAktion(pHauptAktion);
        tWillenserklaerungSession.setAusgewaehlteAktion(pAktion);
        
        eclDbM.openAll();
        if (!tPruefeStartNachOpen.pruefeNachOpenPortalAktuelleHauptAktionAktion()) {
            eclDbM.closeAll();
            return;
        }


        if (pHauptAktion==KonstPortalAktion.HAUPT_NEUANMELDUNG) {
            tWillenserklaerungSession.initBesitzAREintragListe();
            tWillenserklaerungSession.addBesitzAREintragListe(pEclBesitzAREintrag);
            tWillenserklaerungSession.ermittleGattungenFuerBesitzAREintragListe();
            tWillenserklaerungSession.ermittleGattungFuerEinzigenBesitzAREintragList();
       }
        else {
            tWillenserklaerungSession.initZugeordneteMeldungFuerAusfuehrungListe();
            tWillenserklaerungSession.addZugeordneteMeldungFuerAusfuehrungListe(pEclZugeordneteMeldungNeu, null);
            tWillenserklaerungSession.ermittleGattungenFuerZugeordneteMeldungFuerAusfuehrungListe();
            tWillenserklaerungSession.ermittleGattungFuerEinzigenBesitzZugeordneteMeldungFuerAusfuehrungList();
        }

        boolean ergBool = true;
        if (pAktion == KonstPortalAktion.ZWEI_EK_SELBST_FUER_ALLE) {
            if (pHauptAktion==KonstPortalAktion.HAUPT_NEUANMELDUNG) {
                ergBool = check2EK(pEclBesitzAREintrag);
            }
            else {
                ergBool = check2EK(pEclZugeordneteMeldungNeu);
            }
        }

        eclDbM.closeAll();

        if (ergBool == false) {
            tSessionVerwaltung.setzeEnde();
            return;
        }
        } catch (Exception e) {
            CaBug.drucke("Exception");
            System.out.println(e.getMessage());
            e.printStackTrace();
            eclDbM.closeAll();
        }

        tSessionVerwaltung.setzeEnde(KonstPortalView.EINTRITTSKARTE);
        return;

    }

    /*******************************Eintrittskarte anmelden******************************************/

    /** Anmelden und eine Eintrittskarte "Selbst" ausstellen - aus iAuswahl*/
    public void doAnmeldenEineEKSelbst(EclBesitzAREintrag pEclBesitzAREintrag) {
        startAusstellenEK(KonstPortalAktion.HAUPT_NEUANMELDUNG, KonstPortalAktion.EINE_EK_SELBST, pEclBesitzAREintrag, null);
    }

    public void doAnmeldenEineEKMitVollmacht(EclBesitzAREintrag pEclBesitzAREintrag) {
        startAusstellenEK(KonstPortalAktion.HAUPT_NEUANMELDUNG, KonstPortalAktion.EINE_EK_MIT_VOLLMACHT, pEclBesitzAREintrag, null);
    }

    /**Zwei Eintrittskarte (jede EK) mit/ohne Vollmacht ausstellen - aus aAnmelden*/
    public void doAnmeldenZweiEKSelbst(EclBesitzAREintrag pEclBesitzAREintrag) {
        startAusstellenEK(KonstPortalAktion.HAUPT_NEUANMELDUNG, KonstPortalAktion.ZWEI_EK_SELBST_FUER_ALLE, pEclBesitzAREintrag, null);
    }

    
    /*****************************Eintrittskarte zu Meldung - ohne Anmeldung*********************************/
    
     
    public void doNeueWillenserklaerungEineEKSelbst(EclZugeordneteMeldungNeu pEclZugeordneteMeldungNeu) {
        startAusstellenEK(KonstPortalAktion.HAUPT_BEREITSANGEMELDET, KonstPortalAktion.EINE_EK_SELBST, null, pEclZugeordneteMeldungNeu);
    }
    
    public void doNeueWillenserklaerungZweiEKSelbst(EclZugeordneteMeldungNeu pEclZugeordneteMeldungNeu) {
        startAusstellenEK(KonstPortalAktion.HAUPT_BEREITSANGEMELDET, KonstPortalAktion.ZWEI_EK_SELBST_FUER_ALLE, null, pEclZugeordneteMeldungNeu);
    }
    
    public void doNeueWillenserklaerungEineEKmitVollmacht(EclZugeordneteMeldungNeu pEclZugeordneteMeldungNeu) {
        startAusstellenEK(KonstPortalAktion.HAUPT_BEREITSANGEMELDET, KonstPortalAktion.EINE_EK_MIT_VOLLMACHT, null, pEclZugeordneteMeldungNeu);
    }
    
    /**********************************neue "Weisungen" (mit Anmeldung)**********************************/
    /**Briefwahl anmelden aus iAuswahl*/
    public void doAnmeldenBriefwahl(EclBesitzAREintrag pEclBesitzAREintrag) {
        startVwSRVoderBriefwahl(KonstPortalAktion.HAUPT_NEUANMELDUNG, KonstPortalAktion.BRIEFWAHL_NEU, pEclBesitzAREintrag, null);

    }

    /**SRV anmelden aus iAuswahl*/
    public void doAnmeldenVwSrv(EclBesitzAREintrag pEclBesitzAREintrag) {
        startVwSRVoderBriefwahl(KonstPortalAktion.HAUPT_NEUANMELDUNG, KonstPortalAktion.SRV_NEU, pEclBesitzAREintrag, null);

    }

    
    /***************************************Weisungen allgemein********************************/
    
    private void startVwSRVoderBriefwahl(int pHauptAktion, int pAktion, EclBesitzAREintrag pEclBesitzAREintrag, EclZugeordneteMeldungNeu pEclZugeordneteMeldungNeu) {
        if (!tSessionVerwaltung.pruefeStart(KonstPortalView.AUSWAHL)) {
            return;
        }

        tWillenserklaerungSession.clear();
        
        tWillenserklaerungSession.setAusgewaehlteHauptAktion(pHauptAktion);
        tWillenserklaerungSession.setAusgewaehlteAktion(pAktion);
        try {

        /*Öffnen*/
        eclDbM.openAll();
   
        if (pHauptAktion==KonstPortalAktion.HAUPT_NEUANMELDUNG) {
            tWillenserklaerungSession.initBesitzAREintragListe();
            tWillenserklaerungSession.addBesitzAREintragListe(pEclBesitzAREintrag);
            tWillenserklaerungSession.ermittleGattungenFuerBesitzAREintragListe();
            tWillenserklaerungSession.ermittleGattungFuerEinzigenBesitzAREintragList();
       }
        else {
            tWillenserklaerungSession.initZugeordneteMeldungFuerAusfuehrungListe();
            tWillenserklaerungSession.addZugeordneteMeldungFuerAusfuehrungListe(pEclZugeordneteMeldungNeu, null);
            tWillenserklaerungSession.ermittleGattungenFuerZugeordneteMeldungFuerAusfuehrungListe();
            tWillenserklaerungSession.ermittleGattungFuerEinzigenBesitzZugeordneteMeldungFuerAusfuehrungList();
         }

 
        if (pAktion == KonstPortalAktion.BRIEFWAHL_NEU) {
            blMAbstimmung.leseWeisungsliste(tWillenserklaerungSession.getGattungVorhanden(), KonstSkIst.briefwahl, KonstWeisungserfassungSicht.portalWeisungserfassung);
        } else {
            blMAbstimmung.leseWeisungsliste(tWillenserklaerungSession.getGattungVorhanden(), KonstSkIst.srv, KonstWeisungserfassungSicht.portalWeisungserfassung);
        }
        if (eclAbstimmungenListeM.getAbstimmungenListeM().size()==0 && eclAbstimmungenListeM.getGegenantraegeListeM().size()==0) {
            if (pAktion == KonstPortalAktion.BRIEFWAHL_NEU) {
                tFehlerViewSession.setFehlerArt(KonstPortalFehlerView.BRIEFWAHL_NICHT_MOEGLICH_KEINE_ABSTIMMUNGEN__ABBRUCH);
            }
            else {
                tFehlerViewSession.setFehlerArt(KonstPortalFehlerView.SRV_NICHT_MOEGLICH_KEINE_ABSTIMMUNGEN__ABBRUCH);
            }
            tFehlerViewSession.setNextView(tPruefeStartNachOpen.liefereAuswahlView());
            tSessionVerwaltung.setzeEnde(KonstPortalView.FEHLER_VIEW);
            eclDbM.closeAll();
            return;
        }
        
        blMAbstimmungsvorschlag.leseAbstimmungsvorschlagGesellschaft(eclDbM.getDbBundle());

        /*Darf erst an dieser Stelle erfolgen, weil sonst die Abstimmungsversion, die bei pruefeNachOpen gecheckt wird,
         * noch nicht aktualisiert wurde
         */
        if (!tPruefeStartNachOpen.pruefeNachOpenPortalAktuelleHauptAktionAktion()) {
            eclDbM.closeAll();
            return;
        }

        
        
        eclDbM.closeAll();

        if (pHauptAktion==KonstPortalAktion.HAUPT_NEUANMELDUNG) {
            tWillenserklaerungSession.setGeberIstDerAktionaerSelbst(true);
        }
        } catch (Exception e) {
            CaBug.drucke("Exception");
            System.out.println(e.getMessage());
            e.printStackTrace();
            eclDbM.closeAll();
        }
        tSessionVerwaltung.setzeEnde(KonstPortalView.WEISUNG);

    }

    /************************************neue "Weisungen" (ohne Anmeldung)****************************************************/
    /**Briefwahl - als neue Willenserklärung ohne Anmeldung*/
    public void doNeueWillenserklaerungBriefwahl(EclZugeordneteMeldungNeu pEclZugeordneteMeldungNeu) {
        startVwSRVoderBriefwahl(KonstPortalAktion.HAUPT_BEREITSANGEMELDET, KonstPortalAktion.BRIEFWAHL_NEU, null, pEclZugeordneteMeldungNeu);
    }

    /**Stimmrechtsvertreter - als neue Willenserklärung ohne Anmeldung*/
    public void doNeueWillenserklaerungSRV(EclZugeordneteMeldungNeu pEclZugeordneteMeldungNeu) {
        startVwSRVoderBriefwahl(KonstPortalAktion.HAUPT_BEREITSANGEMELDET, KonstPortalAktion.SRV_NEU, null, pEclZugeordneteMeldungNeu);
    }


}
