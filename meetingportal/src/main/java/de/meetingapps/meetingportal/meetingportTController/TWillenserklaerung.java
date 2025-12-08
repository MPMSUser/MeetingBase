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

import java.util.List;

import de.meetingapps.meetingportal.meetComAllg.CaBug;
import de.meetingapps.meetingportal.meetComAllg.CaFehler;
import de.meetingapps.meetingportal.meetComAllg.CaString;
import de.meetingapps.meetingportal.meetComBa.BaMailM;
import de.meetingapps.meetingportal.meetComBl.BlNummernformBasis;
import de.meetingapps.meetingportal.meetComBl.BlNummernformen;
import de.meetingapps.meetingportal.meetComBl.BlWillenserklaerung;
import de.meetingapps.meetingportal.meetComEclM.EclAbstimmungM;
import de.meetingapps.meetingportal.meetComEclM.EclAbstimmungenListeM;
import de.meetingapps.meetingportal.meetComEclM.EclDbM;
import de.meetingapps.meetingportal.meetComEclM.EclParamM;
import de.meetingapps.meetingportal.meetComEclM.EclPortalTexteM;
import de.meetingapps.meetingportal.meetComEntities.EclAktienregister;
import de.meetingapps.meetingportal.meetComEntities.EclAnrede;
import de.meetingapps.meetingportal.meetComEntities.EclBesitzAREintrag;
import de.meetingapps.meetingportal.meetComEntities.EclLoginDaten;
import de.meetingapps.meetingportal.meetComEntities.EclMeldung;
import de.meetingapps.meetingportal.meetComEntities.EclPersonenNatJur;
import de.meetingapps.meetingportal.meetComEntities.EclWeisungMeldung;
import de.meetingapps.meetingportal.meetComEntities.EclWeisungMeldungRaw;
import de.meetingapps.meetingportal.meetComEntities.EclWillenserklaerung;
import de.meetingapps.meetingportal.meetComEntities.EclWillenserklaerungStatusNeu;
import de.meetingapps.meetingportal.meetComEntities.EclWillenserklaerungZusatz;
import de.meetingapps.meetingportal.meetComEntities.EclZugeordneteMeldungNeu;
import de.meetingapps.meetingportal.meetComEntities.EclZutrittsIdent;
import de.meetingapps.meetingportal.meetComEntities.EclZutrittskarten;
import de.meetingapps.meetingportal.meetComHVParam.ParamSpezial;
import de.meetingapps.meetingportal.meetComKonst.KonstKartenart;
import de.meetingapps.meetingportal.meetComKonst.KonstKartenklasse;
import de.meetingapps.meetingportal.meetComKonst.KonstMeldung;
import de.meetingapps.meetingportal.meetComKonst.KonstPortalAktion;
import de.meetingapps.meetingportal.meetComKonst.KonstPortalFehlerView;
import de.meetingapps.meetingportal.meetComKonst.KonstPortalView;
import de.meetingapps.meetingportal.meetComKonst.KonstStimmart;
import de.meetingapps.meetingportal.meetComKonst.KonstWillenserklaerungVersandartEK;
import de.meetingapps.meetingportal.meetComKonst.KonstWillenserklaerungWeg;
import de.meetingapps.meetingportal.meetComKonst.KonstZugeordneteMeldungArtBeziehung;
import de.meetingapps.meetingportal.meetingCoreReport.RpDrucken;
import de.meetingapps.meetingportal.meetingCoreReport.RpVariablen;
import de.meetingapps.meetingportal.meetingportTFunktionen.TFunktionen;
import de.meetingapps.meetingportal.meetingportTFunktionen.TQuittungen;
import de.meetingapps.meetingportal.meetingportTFunktionen.TSessionVerwaltung;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

/**Erteilung von Willenserklärungen (Anmeldung, Briefwahl etc.) aus Portal / Web-Service heraus*/
@RequestScoped
@Named
public class TWillenserklaerung {

    private int logDrucken = 10;

    private @Inject TSession tSession;

    private @Inject TFunktionen tFunktionen;

    private @Inject EclAbstimmungenListeM eclAbstimmungenListeM;
    private @Inject EclParamM eclParamM;
    private @Inject TSessionVerwaltung tSessionVerwaltung;
    private @Inject TWillenserklaerungSession tWillenserklaerungSession;

    private @Inject EclDbM eclDbM;
    private @Inject EclPortalTexteM eclPortalTexteM;
    private @Inject BaMailM baMailm;
    private @Inject TFehlerViewSession tFehlerViewSession;

    private @Inject TLanguage tLanguage;

    private @Inject TQuittungen tQuittungen;
    
    /**Wird in jedem Fall für anmelden*Meldung benötigt. Aber in Folgewillenserklärung wird darauf zugegriffen,
     * falls vorher Anmeldung erfolgt ist. Deshalb übergreifend deklarieren
     */
    private BlWillenserklaerung lWillenserklaerung = null;
    
    
    public boolean checkKeineNeueWillenserklaerungFuerAnmeldung(EclZugeordneteMeldungNeu iEclZugeordneteMeldungNeu) {
        if (tFunktionen.reCheckKeineNeueWillenserklaerungen(eclDbM.getDbBundle(),
                iEclZugeordneteMeldungNeu.getMeldungsIdent(),
                iEclZugeordneteMeldungNeu.getIdentHoechsteWillenserklaerung()) == false) {
            tFehlerViewSession.setFehlerArt(KonstPortalFehlerView.TRANSAKTIONSABBRUCH_ANDERER_USER_AKTIV);
            tFehlerViewSession.setNextView(KonstPortalView.LOGIN);
            tSessionVerwaltung.setzeEnde(KonstPortalView.FEHLER_VIEW);
            return false;
        }
        return true;
    }

    /**Wird aufgerufen, wenn eine Aktienregistereintrag komplett angemeldet ist, nun aber eine Willenserklärung ausgeführt wird
     * die zwei Meldungen erforder.
     * Bestehende Meldung wird storniert, neue Meldung wird ausgeführt*/
    public boolean storniereMeldungErzeugeZweiMeldungen(EclZugeordneteMeldungNeu pEclZugeordneteMeldung) {
        /*******Stornieren*******/
        lWillenserklaerung = new BlWillenserklaerung();
        lWillenserklaerung.pQuelle = tWillenserklaerungSession.getQuelle();
        lWillenserklaerung.pErteiltAufWeg = tWillenserklaerungSession.getEingabeQuelle();
        lWillenserklaerung.pErteiltZeitpunkt = tWillenserklaerungSession.getErteiltZeitpunkt();
        lWillenserklaerung.pEclAktienregisterEintrag = new EclAktienregister();
        lWillenserklaerung.pEclAktienregisterEintrag.aktienregisterIdent = pEclZugeordneteMeldung.aktienregisterIdent;
        lWillenserklaerung.anmeldungenAusAktienregisterStornieren(eclDbM.getDbBundle());
        CaBug.druckeLog("Storno wg. 2 EK erfolgt", logDrucken, 10);

        /******************************Neu Anmelden - aus Aktienregister mit zwei Aktienbeständen***************************************/
        lWillenserklaerung = new BlWillenserklaerung();
        lWillenserklaerung.pQuelle = tWillenserklaerungSession.getQuelle();
        lWillenserklaerung.pErteiltAufWeg = tWillenserklaerungSession.getEingabeQuelle();
        lWillenserklaerung.pErteiltZeitpunkt = tWillenserklaerungSession.getErteiltZeitpunkt();

        /*Aktienregister füllen*/
        int erg = eclDbM.getDbBundle().dbAktienregister
                .leseZuAktienregisterIdent(pEclZugeordneteMeldung.aktienregisterIdent);
        if (erg <= 0) {/*Aktienregistereintrag nicht mehr vorhanden*/
            tSession.trageFehlerEin(CaFehler.afAktienregisterEintragNichtMehrVorhanden);
            tSessionVerwaltung.setzeEnde();
            return false;
        }

        lWillenserklaerung.pEclAktienregisterEintrag = eclDbM.getDbBundle().dbAktienregister.ergebnisPosition(0);

        /*Restliche Parameter füllen*/
        lWillenserklaerung.pAktienAnmelden = -1; /*Alle Aktien anmelden*/
        lWillenserklaerung.pAnmeldungFix = false; /*Nicht "Fix" anmelden*/
        lWillenserklaerung.pAnzahlAnmeldungen = 2;
        lWillenserklaerung.pWillenserklaerungGeberIdent = -1; /*Aktionär gibt in diesem Fall*/
        lWillenserklaerung.pPersonNatJurFuerAnmeldungVerwenden = lWillenserklaerung.pEclAktienregisterEintrag.personNatJur;

        lWillenserklaerung.anmeldungAusAktienregister(eclDbM.getDbBundle());

        if (lWillenserklaerung.rcIstZulaessig == false) {
            tSession.trageFehlerEin(lWillenserklaerung.rcGrundFuerUnzulaessig);
            tSessionVerwaltung.setzeEnde();
            return false;
        }

        return true;
    }

    /**
     * pMitBestaetigung=true => anmelden ist als einzige Willenserklärungsfunktion
     * aus dem Portal aufgerufen worden, d.h. ggf. wird eine Mailbestätigung veranlaßt.
     * 
     * =false => es folgt eine weitere Willenserklärung wie Briefwahl o.ä.,
     * oder Aufruf nicht aus dem Portal
     * keine Mailbestätigung
     * 
     * Falls Returnwert=true, dann normaler Ablauf, normal beendet.
     * 
     * Falls Returnwert=false, dann Fehlersituation aufgetreten.
     * D.h.: 
     * Entweder 
     * > wird ein Fehlerview angezeigt, in diesem Fall wurde bereits tSessioNVerwaltung.setzeEnde(Fehlerview) gesetzt
     * Oder
     * > es wir nur eine Fehlermeldung eingetragen, dann ist tSession.trageFehlerEin bereits erfolgt. 
     */

    public boolean anmelden1Meldung(EclBesitzAREintrag pEclBesitzAREintrag, boolean pMitBestaetigung) {


        CaBug.druckeLog("pEclBesitzAREintrag.aktienregisterEintrag.aktienregisterIdent=" + pEclBesitzAREintrag.aktienregisterEintrag.aktienregisterIdent, logDrucken, 10);
        /*Prüfen, ob mittlerweile bereits Anmeldung erfolgt ist*/
        if (tFunktionen.reCheckKeineAktienanmeldungen(eclDbM.getDbBundle(), pEclBesitzAREintrag.aktienregisterEintrag.aktienregisterIdent) == false) {
            tFehlerViewSession.setFehlerArt(KonstPortalFehlerView.TRANSAKTIONSABBRUCH_ANDERER_USER_AKTIV);
            tFehlerViewSession.setNextView(KonstPortalView.LOGIN);
            tSessionVerwaltung.setzeEnde(KonstPortalView.FEHLER_VIEW);
            CaBug.druckeLog("false 1", logDrucken, 10);
            return false;

        }
        ;

        /******Anmelden********/
        lWillenserklaerung = new BlWillenserklaerung();
        lWillenserklaerung.pQuelle = tWillenserklaerungSession.getQuelle();
        lWillenserklaerung.pErteiltAufWeg = tWillenserklaerungSession.getEingabeQuelle();
        lWillenserklaerung.pErteiltZeitpunkt = tWillenserklaerungSession.getErteiltZeitpunkt();
        /*Aktienregister füllen*/
        EclAktienregister aktienregisterEintrag = new EclAktienregister();
        aktienregisterEintrag.aktionaersnummer = pEclBesitzAREintrag.aktienregisterEintrag.aktionaersnummer;
        int erg = eclDbM.getDbBundle().dbAktienregister.leseZuAktienregisterEintrag(aktienregisterEintrag);
        if (erg <= 0) {/*Aktienregistereintrag nicht mehr vorhanden*/
            tSession.trageFehlerEin(CaFehler.afAktienregisterEintragNichtMehrVorhanden);
            tSessionVerwaltung.setzeEnde();
            CaBug.druckeLog("false 2", logDrucken, 10);
            return false;
        }


        aktienregisterEintrag = eclDbM.getDbBundle().dbAktienregister.ergebnisPosition(0);
        lWillenserklaerung.pEclAktienregisterEintrag = aktienregisterEintrag;

        /*Restliche Parameter füllen*/
        lWillenserklaerung.pAktienAnmelden = -1; /*Alle Aktien anmelden*/
        lWillenserklaerung.pAnmeldungFix = false; /*Nicht "Fix" anmelden*/
        if (tWillenserklaerungSession.getIntAusgewaehlteAktion() == KonstPortalAktion.ZWEI_EK_SELBST_PERSGEMEINSCHAFT
                || tWillenserklaerungSession
                        .getIntAusgewaehlteAktion() == KonstPortalAktion.ZWEI_EK_SELBST_ODER_VOLLMACHT
                || tWillenserklaerungSession.getIntAusgewaehlteAktion() == KonstPortalAktion.ZWEI_EK_SELBST_FUER_ALLE) {
            CaBug.druckeLog("2 Anmeldungen ausführen", logDrucken, 10);
            lWillenserklaerung.pAnzahlAnmeldungen = 2;
        } else {
            CaBug.druckeLog("1 Anmeldung ausführen", logDrucken, 10);
            lWillenserklaerung.pAnzahlAnmeldungen = 1;
        }
        lWillenserklaerung.pWillenserklaerungGeberIdent = -1; /*Aktionär gibt in diesem Fall*/


        lWillenserklaerung.anmeldungAusAktienregister(eclDbM.getDbBundle());
        if (lWillenserklaerung.rcIstZulaessig == false) {
            tSession.trageFehlerEin(lWillenserklaerung.rcGrundFuerUnzulaessig);
            CaBug.druckeLog("false 3", logDrucken, 10);
            return false;
        }

        if (pMitBestaetigung) {
            tQuittungen.bestaetigenNurAnmeldung(lWillenserklaerung.rcWillenserklaerungIdent, lWillenserklaerung.rcEclMeldungen[0], aktienregisterEintrag);
        }

        
        return true;
    }

    /*********************************************************Eintrittskarte*******************************************************/

    public boolean eintrittskarte(EclZugeordneteMeldungNeu pEclZugeordneteMeldung, boolean pMitBestaetigung) {

        /*Idents der PersoneneNatJur, die für den ersten bzw. zweiten Bevollmächtigten vergeben werden*/
        int identPersonenNatJurVollmacht = 0;
        int identPersonenNatJurVollmacht2 = 0;

        int anzMeldungen = 1; //Anzahl der Anmeldungen, für die eine EK ausgestellt werden muß*/
        if (KonstPortalAktion.wkErfordertZweiMeldungen(tWillenserklaerungSession.getIntAusgewaehlteAktion())) {
            anzMeldungen = 2;
        }

        /*Nun Eintrittskarten erzeugen für alle erzeugten Meldungen*/
        for (int i = 0; i < anzMeldungen; i++) {
            BlWillenserklaerung ekWillenserklaerung = new BlWillenserklaerung();
            ekWillenserklaerung.pQuelle = tWillenserklaerungSession.getQuelle();
            ekWillenserklaerung.pErteiltAufWeg = tWillenserklaerungSession.getEingabeQuelle();
            ekWillenserklaerung.pErteiltZeitpunkt = tWillenserklaerungSession.getErteiltZeitpunkt();

            if (tWillenserklaerungSession
                    .getIntAusgewaehlteHauptAktion() == KonstPortalAktion.HAUPT_NEUANMELDUNG /*Erstanmeldung*/
                    ||
                    /*Bereits angemeldet, aber nun 2 EKs neu ausgestellt*/
                    KonstPortalAktion.wkErfordertZweiMeldungen(tWillenserklaerungSession.getIntAusgewaehlteAktion())) {
                ekWillenserklaerung.piMeldungsIdentAktionaer = lWillenserklaerung.rcMeldungen[i];
                ekWillenserklaerung.pFolgeFuerWillenserklaerungIdent = lWillenserklaerung.rcWillenserklaerungIdent;
            } else {/*Anmeldung bereits früher durchgeführt*/
                ekWillenserklaerung.piMeldungsIdentAktionaer = pEclZugeordneteMeldung.getMeldungsIdent();
            }

            /*Meldung einlesen, für die die Eintrittskarte ausgestellt werden soll*/
            eclDbM.getDbBundle().dbMeldungen.leseZuIdent(ekWillenserklaerung.piMeldungsIdentAktionaer);
            EclMeldung lEclMeldung = eclDbM.getDbBundle().dbMeldungen.meldungenArray[0];

            /*Aktienregisterdaten und login-Daten für Aktienregistereintrag einlesen*/
            eclDbM.getDbBundle().dbAktienregister.leseZuAktienregisternummer(lEclMeldung.aktionaersnummer);
            EclAktienregister lEclAktienregister=eclDbM.getDbBundle().dbAktienregister.ergebnisPosition(0);
            eclDbM.getDbBundle().dbLoginDaten.read_aktienregisterIdent(lEclAktienregister.aktienregisterIdent);
            EclLoginDaten lLoginDatenAktienregister=eclDbM.getDbBundle().dbLoginDaten.ergebnisPosition(0);
           
            long stimmenEK = lEclMeldung.stimmen;

            //            /*XXX Klären  evtl. ganz raus?*/
            //            versandadresseverwenden=1;
            //            /*Versandadresse einlesen .... ist noch relativ temporär, wird ggf. doppelt gemacht*/
            //            if (pDbBundle.dbMeldungen.meldungenArray[0].identVersandadresse!=0){
            //                EclPersonenNatJurVersandadresse l1PersonenNatJurVersandadresse=new EclPersonenNatJurVersandadresse();
            //                l1PersonenNatJurVersandadresse.ident=pDbBundle.dbMeldungen.meldungenArray[0].identVersandadresse;
            //                pDbBundle.dbPersonenNatJurVersandadresse.read(l1PersonenNatJurVersandadresse);
            //                if (i==0){
            //                    eclTeilnehmerVersandadresseM.copyFrom(pDbBundle.dbPersonenNatJurVersandadresse.ergebnisPosition(0));
            //                }
            //                else{
            //                    eclTeilnehmerVersandadresse2M.copyFrom(pDbBundle.dbPersonenNatJurVersandadresse.ergebnisPosition(0));
            //                }
            //                versandadresseverwenden=2;
            //            }

            if (tWillenserklaerungSession
                    .getIntAusgewaehlteHauptAktion() == KonstPortalAktion.HAUPT_NEUANMELDUNG /*Erstanmeldung*/
                    ||
                    /*Bereits angemeldet, aber nun 2 EKs neu ausgestellt*/
                    KonstPortalAktion.wkErfordertZweiMeldungen(tWillenserklaerungSession.getIntAusgewaehlteAktion())) {
                ekWillenserklaerung.pWillenserklaerungGeberIdent = -1; /*Aktionär*/
            } else {
                if (pEclZugeordneteMeldung
                        .getArtBeziehung() == KonstZugeordneteMeldungArtBeziehung.EIGENE_AKTIEN_AUS_AKTIENREGISTER
                        || pEclZugeordneteMeldung
                        .getArtBeziehung() == KonstZugeordneteMeldungArtBeziehung.INSTI_AKTIENREGISTER
                        || pEclZugeordneteMeldung
                        .getArtBeziehung() == KonstZugeordneteMeldungArtBeziehung.INSTI_MELDUNG) {
                    ekWillenserklaerung.pWillenserklaerungGeberIdent = -1; /*Aktionär*/
                } else { /*Hier: es handelt sich um eine Untervollmacht. D.h. Geber = angemeldete personNatJur.*/
                    ekWillenserklaerung.pWillenserklaerungGeberIdent = pEclZugeordneteMeldung.personNatJurIdent;
                }
            }

            /*Versandart*/
            int lVersandart = 0;
            if (i == 0 || tWillenserklaerungSession
                    .getIntAusgewaehlteAktion() == KonstPortalAktion.ZWEI_EK_SELBST_FUER_ALLE) {
                lVersandart = Integer.parseInt(tWillenserklaerungSession.getEintrittskarteVersandart());
                if (tWillenserklaerungSession.isUeberOeffentlicheID()) {
                    /*Übertragung an Bevollmächtigten über ÖffentlicheID!*/
                    /*TODO Versandart noch überprüfen - eigene Versandart?*/
                    lVersandart = 3;
                }
            } else {
                lVersandart = Integer.parseInt(tWillenserklaerungSession.getEintrittskarteVersandart2());
            }

            ekWillenserklaerung.pVersandartEK = lVersandart;

            switch (ekWillenserklaerung.pVersandartEK) {
            case KonstWillenserklaerungVersandartEK.VERSANDLAUF_ADRESSE_LAUT_AKTIENREGISTER:
                /*Aufnahme in Sammelbatch, an Adresse im Aktienregister - bei nächstem Drucklauf ausdrucken und versenden*/
                break;
            case KonstWillenserklaerungVersandartEK.VERSANDLAUF_ADRESSE_ABWEICHEND_WIE_EINGEGEBEN:
                /*Aufnahme in Sammelbatch, an Versandadresse - bei nächstem Drucklauf ausdrucken und versenden*/
                //                versandadresseverwenden=3; - nicht mehr benötigt
                if (i == 0 || tWillenserklaerungSession
                        .getIntAusgewaehlteAktion() == KonstPortalAktion.ZWEI_EK_SELBST_FUER_ALLE) {
                    ekWillenserklaerung.pVersandadresse1 = tWillenserklaerungSession
                            .getEintrittskarteAbweichendeAdresse1();
                    ekWillenserklaerung.pVersandadresse2 = tWillenserklaerungSession
                            .getEintrittskarteAbweichendeAdresse2();
                    ekWillenserklaerung.pVersandadresse3 = tWillenserklaerungSession
                            .getEintrittskarteAbweichendeAdresse3();
                    ekWillenserklaerung.pVersandadresse4 = tWillenserklaerungSession
                            .getEintrittskarteAbweichendeAdresse4();
                    ekWillenserklaerung.pVersandadresse5 = tWillenserklaerungSession
                            .getEintrittskarteAbweichendeAdresse5();
                } else {
                    ekWillenserklaerung.pVersandadresse1 = tWillenserklaerungSession
                            .getEintrittskarteAbweichendeAdresse12();
                    ekWillenserklaerung.pVersandadresse2 = tWillenserklaerungSession
                            .getEintrittskarteAbweichendeAdresse22();
                    ekWillenserklaerung.pVersandadresse3 = tWillenserklaerungSession
                            .getEintrittskarteAbweichendeAdresse32();
                    ekWillenserklaerung.pVersandadresse4 = tWillenserklaerungSession
                            .getEintrittskarteAbweichendeAdresse42();
                    ekWillenserklaerung.pVersandadresse5 = tWillenserklaerungSession
                            .getEintrittskarteAbweichendeAdresse52();
                }
                break;
            case KonstWillenserklaerungVersandartEK.ONLINE_AUSDRUCK:
                /*Online-Ausdruck (im Portal) erfolgt*/
                break;
            case KonstWillenserklaerungVersandartEK.ONLINE_EMAIL:
                /*Versand per Email (im Portal) erfolgt*/
                if (i == 0 || tWillenserklaerungSession
                        .getIntAusgewaehlteAktion() == KonstPortalAktion.ZWEI_EK_SELBST_FUER_ALLE) {
                    ekWillenserklaerung.pEmailAdresseEK = tWillenserklaerungSession.getEintrittskarteEmail();
                } else {
                    ekWillenserklaerung.pEmailAdresseEK = tWillenserklaerungSession.getEintrittskarteEmail2();
                }
                break;
            }

            ekWillenserklaerung.neueZutrittsIdentZuMeldung(eclDbM.getDbBundle());
            tWillenserklaerungSession
                    .setRcWillenserklaerungIdentAusgefuehrt(ekWillenserklaerung.rcWillenserklaerungIdent);
            if (ekWillenserklaerung.rcIstZulaessig == false) {
                tSession.trageFehlerEin(ekWillenserklaerung.rcGrundFuerUnzulaessig);
                return false;
            }

            EclZutrittsIdent zutrittsIdentAktionaer = new EclZutrittsIdent();
            zutrittsIdentAktionaer.zutrittsIdent = ekWillenserklaerung.pZutrittsIdent.zutrittsIdent;
            zutrittsIdentAktionaer.zutrittsIdentNeben = ekWillenserklaerung.pZutrittsIdent.zutrittsIdentNeben;
            if (i == 0) {
                tWillenserklaerungSession.setZutrittsIdent(zutrittsIdentAktionaer.zutrittsIdent);
                tWillenserklaerungSession.setZutrittsIdentNeben(zutrittsIdentAktionaer.zutrittsIdentNeben);
            } else {
                tWillenserklaerungSession.setZutrittsIdent2(zutrittsIdentAktionaer.zutrittsIdent);
                tWillenserklaerungSession.setZutrittsIdentNeben2(zutrittsIdentAktionaer.zutrittsIdentNeben);
            }
            int dateinr = ekWillenserklaerung.rcWillenserklaerungIdent;

            BlWillenserklaerung vmWillenserklaerung = null;
            /*Ggf. Vollmachten eintragen*/
            String vollmachtVorname="", vollmachtName="", vollmachtOrt="";
            
            if (tWillenserklaerungSession
                    .getIntAusgewaehlteAktion() == KonstPortalAktion.ZWEI_EK_SELBST_PERSGEMEINSCHAFT
                    || tWillenserklaerungSession.getIntAusgewaehlteAktion() == KonstPortalAktion.EINE_EK_MIT_VOLLMACHT
                    || (tWillenserklaerungSession
                            .getIntAusgewaehlteAktion() == KonstPortalAktion.ZWEI_EK_SELBST_ODER_VOLLMACHT
                            && /*Bei 2 EK selbst oder Vollmacht: Vollmacht ist optional*/
                            ((i == 0 && tWillenserklaerungSession.isVollmachtEingeben())
                                    || (i == 1 && tWillenserklaerungSession.isVollmachtEingeben2())))) {
                vmWillenserklaerung = new BlWillenserklaerung();
                vmWillenserklaerung.pQuelle = tWillenserklaerungSession.getQuelle();
                vmWillenserklaerung.pErteiltAufWeg = tWillenserklaerungSession.getEingabeQuelle();
                vmWillenserklaerung.pErteiltZeitpunkt = tWillenserklaerungSession.getErteiltZeitpunkt();

                if (tWillenserklaerungSession
                        .getIntAusgewaehlteHauptAktion() == KonstPortalAktion.HAUPT_NEUANMELDUNG /*Erstanmeldung*/
                        ||
                        /*Bereits angemeldet, aber nun 2 EKs neu ausgestellt*/
                        KonstPortalAktion
                                .wkErfordertZweiMeldungen(tWillenserklaerungSession.getIntAusgewaehlteAktion())) {
                    vmWillenserklaerung.piMeldungsIdentAktionaer = lWillenserklaerung.rcMeldungen[i];
                } else {/*Anmeldung bereits früher durchgeführt*/
                    vmWillenserklaerung.piMeldungsIdentAktionaer = pEclZugeordneteMeldung.getMeldungsIdent();
                }

                vmWillenserklaerung.pFolgeFuerWillenserklaerungIdent = ekWillenserklaerung.rcWillenserklaerungIdent;

                if (tWillenserklaerungSession
                        .getIntAusgewaehlteHauptAktion() == KonstPortalAktion.HAUPT_NEUANMELDUNG /*Erstanmeldung*/
                        ||
                        /*Bereits angemeldet, aber nun 2 EKs neu ausgestellt*/
                        KonstPortalAktion
                                .wkErfordertZweiMeldungen(tWillenserklaerungSession.getIntAusgewaehlteAktion())) {
                    vmWillenserklaerung.pWillenserklaerungGeberIdent = -1; /*Aktionär*/
                } else {
                    if (pEclZugeordneteMeldung
                            .getArtBeziehung() == KonstZugeordneteMeldungArtBeziehung.EIGENE_AKTIEN_AUS_AKTIENREGISTER
                            || pEclZugeordneteMeldung
                                    .getArtBeziehung() == KonstZugeordneteMeldungArtBeziehung.INSTI_AKTIENREGISTER
                            || pEclZugeordneteMeldung
                                    .getArtBeziehung() == KonstZugeordneteMeldungArtBeziehung.INSTI_MELDUNG) {
                        vmWillenserklaerung.pWillenserklaerungGeberIdent = -1; /*Aktionär*/
                    } else { /*Hier: es handelt sich um eine Untervollmacht. D.h. Geber = angemeldete personNatJur.*/
                        vmWillenserklaerung.pWillenserklaerungGeberIdent = pEclZugeordneteMeldung.personNatJurIdent;
                    }
                }

                EclPersonenNatJur personNatJur = new EclPersonenNatJur();
                ;
                if (!tWillenserklaerungSession.isUeberOeffentlicheID()) { /*Dann neuen Bevollmächtigten anlegen*/
                    personNatJur.ident = 0; /*Neue Person*/
                    if (i == 0) {
                        boolean verbandGewaehlt=false;
                        if (ParamSpezial.ku303mitVerbaenden(eclParamM.getClGlobalVar().mandant)) {
                            String hVerband=tWillenserklaerungSession.getNzVerband();
                            if (hVerband!=null && hVerband.trim().isEmpty()==false) {
                                hVerband=hVerband.trim();
                                tWillenserklaerungSession.setVollmachtVorname("");
                                tWillenserklaerungSession.setVollmachtOrt("");
                                verbandGewaehlt=true;
                                switch (hVerband) {
                                case "1":
                                    tWillenserklaerungSession.setVollmachtName("Rübenanbauer- und Aktionärsverband Nord");
                                    break;
                                case "2":
                                    tWillenserklaerungSession.setVollmachtName("Güstrower Rübenanbauerverband");
                                    break;
                                case "3":
                                    tWillenserklaerungSession.setVollmachtName("ZAV Niedersachsen-Mitte");
                                    break;
                                case "4":
                                    tWillenserklaerungSession.setVollmachtName("ZAV Magdeburg");
                                    break;
                                case "5":
                                    tWillenserklaerungSession.setVollmachtName("ZAV Niedersachsen Ost");
                                    break;
                                case "6":
                                    tWillenserklaerungSession.setVollmachtName("ZAV Schleswig-Holstein");
                                    break;
                                case "7":
                                    tWillenserklaerungSession.setVollmachtName("ZAV Südniedersachsen");
                                    break;
                                }
                            }
                        }
                        personNatJur.vorname = tWillenserklaerungSession.getVollmachtVorname();
                        personNatJur.name = tWillenserklaerungSession.getVollmachtName();
                        personNatJur.ort = tWillenserklaerungSession.getVollmachtOrt();
                        
                        vollmachtVorname=personNatJur.vorname;
                        vollmachtName=personNatJur.name;
                        vollmachtOrt=personNatJur.ort;
                        
                        if (ParamSpezial.ku302_303(eclParamM.getClGlobalVar().mandant)) {
                            if (verbandGewaehlt==false) {
                                String hVertretungsArt=tWillenserklaerungSession.getNzVertretungsart();
                                if (hVertretungsArt==null) {hVertretungsArt="";}
                                hVertretungsArt=CaString.fuelleLinksNull(hVertretungsArt, 2);
                                personNatJur.mailadresse=hVertretungsArt+" "+tWillenserklaerungSession.getNzGpNummer();
                            }
                        }
                        
                    } else {
                        personNatJur.vorname = tWillenserklaerungSession.getVollmachtVorname2();
                        personNatJur.name = tWillenserklaerungSession.getVollmachtName2();
                        personNatJur.ort = tWillenserklaerungSession.getVollmachtOrt2();
                        
                        vollmachtVorname=personNatJur.vorname;
                        vollmachtName=personNatJur.name;
                        vollmachtOrt=personNatJur.ort;
                    }
                } else { /*Auf bestehenden Bevollmächtigten über öffentliche ID übertragen*/
                    personNatJur.ident = tWillenserklaerungSession.getPersonNatJurOeffentlicheID();
                }
                vmWillenserklaerung.pEclPersonenNatJur = personNatJur;

                vmWillenserklaerung.vollmachtAnDritte(eclDbM.getDbBundle());

                tWillenserklaerungSession
                        .setRcWillenserklaerungIdentAusgefuehrtZweit(vmWillenserklaerung.rcWillenserklaerungIdent);

                if (vmWillenserklaerung.rcIstZulaessig == false) {
                    tSession.trageFehlerEin(vmWillenserklaerung.rcGrundFuerUnzulaessig);
                    return false;
                }
                if (i == 0) {
                    identPersonenNatJurVollmacht = vmWillenserklaerung.pEclPersonenNatJur.ident;
                } else {
                    identPersonenNatJurVollmacht2 = vmWillenserklaerung.pEclPersonenNatJur.ident;
                }

                /*Nun noch die Eintrittskarten-WillenserklärungZusatz um die IdentPersonNatJur des Vertreters ergänzen*/

                EclWillenserklaerung tWillenserklaerung = new EclWillenserklaerung();
                EclWillenserklaerungZusatz tWillenserklaerungZusatz = new EclWillenserklaerungZusatz();
                eclDbM.getDbBundle().dbWillenserklaerung.leseZuIdent(ekWillenserklaerung.rcWillenserklaerungIdent);
                tWillenserklaerung = eclDbM.getDbBundle().dbWillenserklaerung.willenserklaerungGefunden(0);
                eclDbM.getDbBundle().dbWillenserklaerungZusatz
                        .leseZuIdent(ekWillenserklaerung.rcWillenserklaerungIdent);
                tWillenserklaerungZusatz = eclDbM.getDbBundle().dbWillenserklaerungZusatz.willenserklaerungGefunden(0);
                tWillenserklaerungZusatz.identVertreterPersonNatJur = vmWillenserklaerung.pEclPersonenNatJur.ident;
                eclDbM.getDbBundle().dbWillenserklaerung.updateMitZusatz(tWillenserklaerung, tWillenserklaerungZusatz);

                /*Nun noch die Eintrittskarte erweitern um die ausgestellte Person*/
                eclDbM.getDbBundle().dbZutrittskarten.readAktionaer(zutrittsIdentAktionaer, 1);
                EclZutrittskarten lEclZutrittskarte = eclDbM.getDbBundle().dbZutrittskarten.ergebnisPosition(0);
                lEclZutrittskarte.ausgestelltAufPersonenNatJurIdent = vmWillenserklaerung.pEclPersonenNatJur.ident;
                eclDbM.getDbBundle().dbZutrittskarten.update(lEclZutrittskarte);
            }

            /*Eintrittskarten-PDF erzeugen*/
            if (lVersandart == KonstWillenserklaerungVersandartEK.ONLINE_AUSDRUCK
                    || lVersandart == KonstWillenserklaerungVersandartEK.ONLINE_EMAIL) {

                if (i == 0) {
                    tWillenserklaerungSession.setEintrittskartePdfNr(dateinr);
                } else {
                    tWillenserklaerungSession.setEintrittskartePdfNr2(dateinr);
                }

                RpDrucken rpDrucken = new RpDrucken();
                rpDrucken.initServer();
                rpDrucken.exportFormat = 8;
                rpDrucken.exportDatei = "zutrittsdokumentM" + eclParamM.getClGlobalVar().getMandantString()
                        + Integer.toString(dateinr);
                rpDrucken.initFormular(eclDbM.getDbBundle());

                /*Variablen füllen - sowie Dokumentvorlage*/
                RpVariablen rpVariablen = new RpVariablen(eclDbM.getDbBundle());
                if (lVersandart == KonstWillenserklaerungVersandartEK.ONLINE_AUSDRUCK) {
                    rpVariablen.ekSelbstdruck(rpDrucken);
                } else {
                    rpVariablen.ekMail(rpDrucken);
                }
                rpDrucken.startFormular();

                /*0=Aufruf aus Deutschem Portal, 1=Aufruf aus Englischem Portal*/
                if (tLanguage.getLang()==1) {
                    rpVariablen.fuelleVariable(rpDrucken, "IstEnglisch", "0");
                    CaBug.druckeLog("IstEnglisch=0", logDrucken, 10);
                }
                else {
                    rpVariablen.fuelleVariable(rpDrucken, "IstEnglisch", "1");
                    CaBug.druckeLog("IstEnglisch=1", logDrucken, 3);
                }

                
                BlNummernformen blNummernformen = new BlNummernformen(eclDbM.getDbBundle());

                rpVariablen.fuelleVariable(rpDrucken, "Nummern.ZutrittsIdent", BlNummernformen.verketteEKMitNeben(
                        zutrittsIdentAktionaer.zutrittsIdent, zutrittsIdentAktionaer.zutrittsIdentNeben, 0));
                rpVariablen.fuelleVariable(rpDrucken, "Nummern.ZutrittsIdentKurz",
                        zutrittsIdentAktionaer.zutrittsIdent);
                rpVariablen.fuelleVariable(rpDrucken, "Nummern.ZutrittsIdentKomplett",
                        blNummernformen.formatiereNrKomplett(zutrittsIdentAktionaer.zutrittsIdent,
                                zutrittsIdentAktionaer.zutrittsIdentNeben, KonstKartenklasse.eintrittskartennummerNeben,
                                KonstKartenart.erstzugang));
                //              System.out.println("ZutrittsIdentKomplett="+blNummernformen.formatiereNrKomplett(zutrittsIdentAktionaer.zutrittsIdent, zutrittsIdentAktionaer.zutrittsIdentNeben, KonstKartenklasse.eintrittskartennummerNeben, KonstKartenart.erstzugang));
                //              rpVariablen.fuelleVariable(rpDrucken, "Nummern.ZutrittsIdentKomplett", blNummernformen.formatiereEKNrKomplett(zutrittsIdentAktionaer.zutrittsIdent, zutrittsIdentAktionaer.zutrittsIdentNeben));
                rpVariablen.fuelleVariable(rpDrucken, "Nummern.Aktionaersnummer",
                        BlNummernformBasis.aufbereitenInternFuerExtern(lEclMeldung.aktionaersnummer, eclDbM.getDbBundle())
                        );

                rpVariablen.fuelleVariable(rpDrucken, "Besitz.Stimmen", Long.toString(stimmenEK));
                rpVariablen.fuelleVariable(rpDrucken, "Besitz.StimmenDE", CaString.toStringDE(stimmenEK));
                rpVariablen.fuelleVariable(rpDrucken, "Besitz.StimmenEN", CaString.toStringEN(stimmenEK));

                /*Nun passwort und Zugangsdaten anzeigen*/
                /*Zugangsdaten des Aktionärs immer setzen*/
               String aInitialPasswort=lLoginDatenAktienregister.lieferePasswortInitialClean();
                rpVariablen.fuelleVariable(rpDrucken, "Aktionaer.Passwort", aInitialPasswort);
                rpVariablen.fuelleVariable(rpDrucken, "Aktionaer.KennungAlternativ", lLoginDatenAktienregister.loginKennungAlternativ);
                
                /*Prophylaktisch setzen - wird ggf. im Falle einer Vollmacht überschrieben*/
                rpVariablen.fuelleVariable(rpDrucken, "Vertreter.KennungVorrang", "0");

                
                if (tWillenserklaerungSession
                        .getIntAusgewaehlteAktion() == KonstPortalAktion.ZWEI_EK_SELBST_PERSGEMEINSCHAFT
                        || tWillenserklaerungSession.getIntAusgewaehlteAktion() == KonstPortalAktion.EINE_EK_MIT_VOLLMACHT
                        || (tWillenserklaerungSession
                                .getIntAusgewaehlteAktion() == KonstPortalAktion.ZWEI_EK_SELBST_ODER_VOLLMACHT
                                && /*Bei 2 EK selbst oder Vollmacht: Vollmacht ist optional*/
                                ((i == 0 && tWillenserklaerungSession.isVollmachtEingeben())
                                        || (i == 1 && tWillenserklaerungSession.isVollmachtEingeben2())))) {
                    /*Zugangsdaten des Bevollmächtigten*/
                    String vKennung=vmWillenserklaerung.rcLoginDaten.loginKennung;
                    String vInitialpasswort=vmWillenserklaerung.rcLoginDaten.lieferePasswortInitialClean();
                    rpVariablen.fuelleVariable(rpDrucken, "Vertreter.Kennung", vKennung);
                    rpVariablen.fuelleVariable(rpDrucken, "Vertreter.KennungAlternativ", vmWillenserklaerung.rcLoginDaten.loginKennungAlternativ);
                    rpVariablen.fuelleVariable(rpDrucken, "Vertreter.Passwort", vInitialpasswort);
                    rpVariablen.fuelleVariable(rpDrucken, "Vertreter.KennungVorrang", "1");
                 }
                
 
                rpVariablen.fuelleVariable(rpDrucken, "Aktionaer.Titel", lEclMeldung.titel);
                rpVariablen.fuelleVariable(rpDrucken, "Aktionaer.Name", lEclMeldung.name);
                rpVariablen.fuelleVariable(rpDrucken, "Aktionaer.Vorname", lEclMeldung.vorname);
                rpVariablen.fuelleVariable(rpDrucken, "Aktionaer.Plz", lEclMeldung.plz);
                rpVariablen.fuelleVariable(rpDrucken, "Aktionaer.Ort", lEclMeldung.ort);
                if (!lEclMeldung.land.isEmpty()) {
                    eclDbM.getDbBundle().dbStaaten.readCode(lEclMeldung.land);
                    if (eclDbM.getDbBundle().dbStaaten.anzErgebnis() > 0) {
                        rpVariablen.fuelleVariable(rpDrucken, "Aktionaer.Landeskuerzel",
                                eclDbM.getDbBundle().dbStaaten.ergebnisPosition(0).code);
                        rpVariablen.fuelleVariable(rpDrucken, "Aktionaer.Land",
                                eclDbM.getDbBundle().dbStaaten.ergebnisPosition(0).nameDE);
                    }
                }
                rpVariablen.fuelleVariable(rpDrucken, "Aktionaer.Strasse", lEclMeldung.strasse);
                /*Anrede füllen*/
                int anredenNr = lEclMeldung.anrede;
                EclAnrede hAnrede = new EclAnrede();
                if (anredenNr != 0) {
                    eclDbM.getDbBundle().dbAnreden.SetzeSprache(2, 0);
                    eclDbM.getDbBundle().dbAnreden.ReadAnrede_Anredennr(anredenNr);
                    hAnrede = new EclAnrede();
                    if (eclDbM.getDbBundle().dbAnreden.AnzAnredenInReadArray > 0) {
                        hAnrede = eclDbM.getDbBundle().dbAnreden.anredenreadarray[0];
                    }
                    rpVariablen.fuelleVariable(rpDrucken, "Aktionaer.AnredeDE", hAnrede.anredentext);
                    rpVariablen.fuelleVariable(rpDrucken, "Aktionaer.AnredeEN", hAnrede.anredentextfremd);
                    rpVariablen.fuelleVariable(rpDrucken, "Aktionaer.BriefanredeDE", hAnrede.anredenbrief);
                    rpVariablen.fuelleVariable(rpDrucken, "Aktionaer.BriefanredeEN", hAnrede.anredenbrieffremd);
                }

                /*Kombi-Felder füllen*/
                String titelVornameName = "";
                if (lEclMeldung.titel.length() != 0) {
                    titelVornameName = titelVornameName + lEclMeldung.titel + " ";
                }
                if (lEclMeldung.vorname.length() != 0) {
                    titelVornameName = titelVornameName + lEclMeldung.vorname + " ";
                }
                titelVornameName = titelVornameName + lEclMeldung.name;
                rpVariablen.fuelleVariable(rpDrucken, "Aktionaer.TitelVornameName", titelVornameName);

                String nameVornameTitel = "";
                nameVornameTitel = lEclMeldung.name;
                if (lEclMeldung.titel.length() != 0 || lEclMeldung.vorname.length() != 0) {
                    nameVornameTitel = nameVornameTitel + ",";
                }
                if (lEclMeldung.titel.length() != 0) {
                    nameVornameTitel = nameVornameTitel + " " + lEclMeldung.titel;
                }
                if (lEclMeldung.vorname.length() != 0) {
                    nameVornameTitel = nameVornameTitel + " " + lEclMeldung.vorname;
                }
                rpVariablen.fuelleVariable(rpDrucken, "Aktionaer.NameVornameTitel", nameVornameTitel);

                String kompletteAnredeDE = hAnrede.anredenbrief;
                String kompletteAnredeEN = hAnrede.anredenbrieffremd;
                if (hAnrede.istjuristischePerson != 1) {
                    if (lEclMeldung.titel.length() != 0) {
                        kompletteAnredeDE = kompletteAnredeDE + " " + lEclMeldung.titel;
                        kompletteAnredeEN = kompletteAnredeEN + " " + lEclMeldung.titel;
                    }
                    if (lEclMeldung.name.length() != 0) {
                        kompletteAnredeDE = kompletteAnredeDE + " " + lEclMeldung.name;
                        kompletteAnredeEN = kompletteAnredeEN + " " + lEclMeldung.name;
                    }
                }
                rpVariablen.fuelleVariable(rpDrucken, "Aktionaer.KompletteAnredeDE", kompletteAnredeDE);
                rpVariablen.fuelleVariable(rpDrucken, "Aktionaer.KompletteAnredeEN", kompletteAnredeEN);

                String besitzArtKuerzel = lEclMeldung.besitzart;
                String besitzArt = "", besitzArtEN = "";
                switch (besitzArtKuerzel) {
                case "E": {
                    besitzArt = "Eigenbesitz";
                    besitzArtEN = "Proprietary Possession";
                    break;
                }
                case "F": {
                    besitzArt = "Fremdbesitz";
                    besitzArtEN = "Minority Interests";
                    break;
                }
                case "V": {
                    besitzArt = "Vollmachtsbesitz";
                    besitzArtEN = "Proxy Possession";
                    break;
                }
                }
                rpVariablen.fuelleVariable(rpDrucken, "Aktionaer.BesitzArtKuerzel", besitzArtKuerzel);
                rpVariablen.fuelleVariable(rpDrucken, "Aktionaer.BesitzArt", besitzArt);
                rpVariablen.fuelleVariable(rpDrucken, "Aktionaer.BesitzArtEN", besitzArtEN);
                rpVariablen.fuelleVariable(rpDrucken, "Aktionaer.GattungKurz",
                        eclParamM.getParam().paramBasis.getGattungBezeichnungKurz(lEclMeldung.liefereGattung()));

                if (tWillenserklaerungSession
                        .getIntAusgewaehlteAktion() == KonstPortalAktion.ZWEI_EK_SELBST_PERSGEMEINSCHAFT
                        || tWillenserklaerungSession
                                .getIntAusgewaehlteAktion() == KonstPortalAktion.EINE_EK_MIT_VOLLMACHT
                        || (tWillenserklaerungSession
                                .getIntAusgewaehlteAktion() == KonstPortalAktion.ZWEI_EK_SELBST_ODER_VOLLMACHT
                                && /*Bei 2 EK selbst oder Vollmacht: Vollmacht ist optional*/
                                ((i == 0 && tWillenserklaerungSession.isVollmachtEingeben())
                                        || (i == 1 && tWillenserklaerungSession.isVollmachtEingeben2())))) {
                    if (i == 0) {
                        rpVariablen.fuelleVariable(rpDrucken, "Vertreter.Name",
                                tWillenserklaerungSession.getVollmachtName());
                        rpVariablen.fuelleVariable(rpDrucken, "Vertreter.Vorname",
                                tWillenserklaerungSession.getVollmachtVorname());
                        rpVariablen.fuelleVariable(rpDrucken, "Vertreter.Ort",
                                tWillenserklaerungSession.getVollmachtOrt());
                    } else {
                        rpVariablen.fuelleVariable(rpDrucken, "Vertreter.Name",
                                tWillenserklaerungSession.getVollmachtName2());
                        rpVariablen.fuelleVariable(rpDrucken, "Vertreter.Vorname",
                                tWillenserklaerungSession.getVollmachtVorname2());
                        rpVariablen.fuelleVariable(rpDrucken, "Vertreter.Ort",
                                tWillenserklaerungSession.getVollmachtOrt2());
                    }
                } else {
                    rpVariablen.fuelleVariable(rpDrucken, "Vertreter.Name", "");
                    rpVariablen.fuelleVariable(rpDrucken, "Vertreter.Vorname", "");
                    rpVariablen.fuelleVariable(rpDrucken, "Vertreter.Ort", "");
                    rpVariablen.fuelleVariable(rpDrucken, "Vertreter.Kennung", "");
                    rpVariablen.fuelleVariable(rpDrucken, "Vertreter.KennungAlternativ", "");
                    rpVariablen.fuelleVariable(rpDrucken, "Vertreter.Passwort", "");
                }

                //                /*Gestrichen - keine Versandadresse bei Online-EKs*/
                //                if (i==0 || aDlgVariablen.getAusgewaehlteAktion().compareTo("30")==0){
                //                rpVariablen.fuelleVariable(rpDrucken, "Versandadresse.Zeile1", aDlgVariablen.getEintrittskarteAbweichendeAdresse1());
                //                rpVariablen.fuelleVariable(rpDrucken, "Versandadresse.Zeile2", aDlgVariablen.getEintrittskarteAbweichendeAdresse2());
                //                rpVariablen.fuelleVariable(rpDrucken, "Versandadresse.Zeile3", aDlgVariablen.getEintrittskarteAbweichendeAdresse3());
                //                rpVariablen.fuelleVariable(rpDrucken, "Versandadresse.Zeile4", aDlgVariablen.getEintrittskarteAbweichendeAdresse4());
                //                rpVariablen.fuelleVariable(rpDrucken, "Versandadresse.Zeile5", aDlgVariablen.getEintrittskarteAbweichendeAdresse5());
                //                }
                //                else{
                //                    rpVariablen.fuelleVariable(rpDrucken, "Versandadresse.Zeile1", aDlgVariablen.getEintrittskarteAbweichendeAdresse12());
                //                    rpVariablen.fuelleVariable(rpDrucken, "Versandadresse.Zeile2", aDlgVariablen.getEintrittskarteAbweichendeAdresse22());
                //                    rpVariablen.fuelleVariable(rpDrucken, "Versandadresse.Zeile3", aDlgVariablen.getEintrittskarteAbweichendeAdresse32());
                //                    rpVariablen.fuelleVariable(rpDrucken, "Versandadresse.Zeile4", aDlgVariablen.getEintrittskarteAbweichendeAdresse42());
                //                    rpVariablen.fuelleVariable(rpDrucken, "Versandadresse.Zeile5", aDlgVariablen.getEintrittskarteAbweichendeAdresse52());
                //                    
                //                }
                rpVariablen.fuelleVariable(rpDrucken, "Versandadresse.Zeile1", "");
                rpVariablen.fuelleVariable(rpDrucken, "Versandadresse.Zeile2", "");
                rpVariablen.fuelleVariable(rpDrucken, "Versandadresse.Zeile3", "");
                rpVariablen.fuelleVariable(rpDrucken, "Versandadresse.Zeile4", "");
                rpVariablen.fuelleVariable(rpDrucken, "Versandadresse.Zeile5", "");
                rpVariablen.fuelleVariable(rpDrucken, "Versandadresse.Zeile6", "");
                rpVariablen.fuelleVariable(rpDrucken, "Versandadresse.Zeile7", "");

                //Start printing
                rpDrucken.druckenFormular();
                rpDrucken.endeFormular();

                if (lVersandart == KonstWillenserklaerungVersandartEK.ONLINE_EMAIL) {
                    /*Nun per Mail versenden*/
                    String lMail = "";
                    if (i == 0 || tWillenserklaerungSession
                            .getIntAusgewaehlteAktion() == KonstPortalAktion.ZWEI_EK_SELBST_FUER_ALLE) {
                        lMail = tWillenserklaerungSession.getEintrittskarteEmail();
                    } else {
                        lMail = tWillenserklaerungSession.getEintrittskarteEmail2();
                    }

                    String hMailText = "", hBetreff = "";
                    hBetreff = eclPortalTexteM.holeText("218");
                    hMailText = eclPortalTexteM.holeText("219");

                    baMailm.sendenMitAnhang(lMail, hBetreff, hMailText, eclParamM.getClGlobalVar().lwPfadAllgemein
                            + "\\"+eclParamM.getParamServer().praefixPfadVerzeichnisse
                            + "ausdrucke\\" + eclParamM.getMandantPfad() + "\\zutrittsdokumentM"
                            + eclParamM.getClGlobalVar().getMandantString() + Integer.toString(dateinr) + ".pdf");
                }

            }
            
            if (pMitBestaetigung) {
                tQuittungen.bestaetigenEintrittskarte(
                        ekWillenserklaerung.rcWillenserklaerungIdent, 
                        lEclMeldung, lEclAktienregister, 
                        zutrittsIdentAktionaer,
                        0, vollmachtVorname, vollmachtName, vollmachtOrt,
                        false);
            }


        }

        /*Nun noch ggf. zweite Vollmacht (bei Personengemeinschaften) eintragen*/
        if (tWillenserklaerungSession.getIntAusgewaehlteAktion() == KonstPortalAktion.ZWEI_EK_SELBST_PERSGEMEINSCHAFT) {
            for (int i = 0; i < 2; i++) {
                /*Nun noch zweiten Bevollmächtigten eintragen*/
                BlWillenserklaerung vmWillenserklaerung = new BlWillenserklaerung();
                vmWillenserklaerung.pQuelle = tWillenserklaerungSession.getQuelle();
                vmWillenserklaerung.pErteiltAufWeg = tWillenserklaerungSession.getEingabeQuelle();
                vmWillenserklaerung.pErteiltZeitpunkt = tWillenserklaerungSession.getErteiltZeitpunkt();
                vmWillenserklaerung.piMeldungsIdentAktionaer = lWillenserklaerung.rcMeldungen[i];
                vmWillenserklaerung.pFolgeFuerWillenserklaerungIdent = lWillenserklaerung.rcWillenserklaerungIdent;
                vmWillenserklaerung.pWillenserklaerungGeberIdent = -1; /*Aktionär*/
                EclPersonenNatJur personNatJur = new EclPersonenNatJur();

                personNatJur.ident = 0; /*Neue Person*/
                if (i == 1) { /*Zweite EK => 1. Vertreter noch eintragen*/
                    personNatJur.ident = identPersonenNatJurVollmacht;
                } else { /*Erste EK => 2. Vertreter noch eintragen*/
                    personNatJur.ident = identPersonenNatJurVollmacht2;
                }
                vmWillenserklaerung.pEclPersonenNatJur = personNatJur;
                vmWillenserklaerung.vollmachtAnDritte(eclDbM.getDbBundle());
                if (vmWillenserklaerung.rcIstZulaessig == false) {
                    tSession.trageFehlerEin(vmWillenserklaerung.rcGrundFuerUnzulaessig);
                    tSessionVerwaltung.setzeEnde();
                    return false;
                }
            }

        }

        return true;
    }

    
    
    public boolean storniereEintrittskarte(EclZugeordneteMeldungNeu iEclZugeordneteMeldungNeu, EclWillenserklaerungStatusNeu iEclWillenserklaerungStatusNeu, boolean pMitBestaetigung) {

        CaBug.druckeLog("A", logDrucken, 10);
        BlWillenserklaerung willenserklaerung = new BlWillenserklaerung();
        willenserklaerung.pQuelle = tWillenserklaerungSession.getQuelle();
        willenserklaerung.pErteiltAufWeg = tWillenserklaerungSession.getEingabeQuelle();
        willenserklaerung.pErteiltZeitpunkt = tWillenserklaerungSession.getErteiltZeitpunkt();
        
        EclZutrittsIdent hZutrittsIdent=new EclZutrittsIdent();
        hZutrittsIdent.zutrittsIdent=iEclWillenserklaerungStatusNeu.getZutrittsIdent();
        hZutrittsIdent.zutrittsIdentNeben=iEclWillenserklaerungStatusNeu.getZutrittsIdentNeben();
        
        willenserklaerung.piZutrittsIdent = hZutrittsIdent;

        if (tWillenserklaerungSession.getIntAusgewaehlteAktion()==KonstPortalAktion.GK_STORNIEREN) {
            willenserklaerung.piKlasse = KonstMeldung.MELDUNG_IST_GAST;
        } else {
            willenserklaerung.piKlasse = KonstMeldung.MELDUNG_IST_AKTIONAER;
        }
        willenserklaerung.pWillenserklaerungGeberIdent = -1; /*Aktionär*/
        CaBug.druckeLog("B", logDrucken, 10);

        willenserklaerung.sperrenZutrittsIdent(eclDbM.getDbBundle());
        CaBug.druckeLog("C", logDrucken, 10);

        /*Falls nicht möglich: Fehlermeldung, zum Anmelden*/
        if (willenserklaerung.rcIstZulaessig == false) {
            tSession.trageFehlerEin(willenserklaerung.rcGrundFuerUnzulaessig);
            tSessionVerwaltung.setzeEnde();
            return false;
         }
        CaBug.druckeLog("D", logDrucken, 10);

        int vollmachtPersonNatJur=0;

        if (tWillenserklaerungSession.getIntAusgewaehlteAktion()==KonstPortalAktion.EK_MIT_VOLLMACHT_STORNIEREN) {
            CaBug.druckeLog("E", logDrucken, 10);
              /*Eintrittskarte mit Vollmacht - nun noch die Vollmacht stornieren*/
            BlWillenserklaerung vmWillenserklaerung = new BlWillenserklaerung();
            vmWillenserklaerung.pQuelle = tWillenserklaerungSession.getQuelle();
            vmWillenserklaerung.pErteiltAufWeg = tWillenserklaerungSession.getEingabeQuelle();
            vmWillenserklaerung.pErteiltZeitpunkt = tWillenserklaerungSession.getErteiltZeitpunkt();
            vmWillenserklaerung.piMeldungsIdentAktionaer = iEclZugeordneteMeldungNeu.getMeldungsIdent();
            EclPersonenNatJur lPersonenNatJur = new EclPersonenNatJur();
            lPersonenNatJur.ident = iEclWillenserklaerungStatusNeu.getBevollmaechtigterDritterIdent();
            vollmachtPersonNatJur=lPersonenNatJur.ident;
            vmWillenserklaerung.pEclPersonenNatJur = lPersonenNatJur;
//            vmWillenserklaerung.pWillenserklaerungGeberIdent = -1; /*Aktionär*/ /*TODO War mal früher so - kann das entfallen?*/
            if (iEclZugeordneteMeldungNeu.personNatJurIdent==0) {
                vmWillenserklaerung.pWillenserklaerungGeberIdent = -1; /*Aktionär*/
            }
            else {
                vmWillenserklaerung.pWillenserklaerungGeberIdent = iEclZugeordneteMeldungNeu.personNatJurIdent;
            }
            CaBug.druckeLog("F", logDrucken, 10);
            vmWillenserklaerung.widerrufVollmachtAnDritte(eclDbM.getDbBundle());
            CaBug.druckeLog("G", logDrucken, 10);
            if (vmWillenserklaerung.rcIstZulaessig == false) {
                CaBug.druckeLog("H willenserklaerung.rcGrundFuerUnzulaessig="+willenserklaerung.rcGrundFuerUnzulaessig, logDrucken, 10);
               tSession.trageFehlerEin(willenserklaerung.rcGrundFuerUnzulaessig);
                tSessionVerwaltung.setzeEnde();
                return false;
            }
        }

        if (pMitBestaetigung) {
            tQuittungen.bestaetigenEintrittskarte(
                    willenserklaerung.rcWillenserklaerungIdent, 
                    iEclZugeordneteMeldungNeu.eclMeldung, null, 
                    hZutrittsIdent,
                    vollmachtPersonNatJur, "", "", "",
                    true);
        }

        CaBug.druckeLog("I", logDrucken, 10);
      
        return true;
    }
    
    
    /**Login-Kennung und Passwort für Eintrittskarte
     * 
     * Aufbereitungslogik:
     * > EK Selbst: 
     * >> Ermitteln der zugehörigen Kennung, mit der der User eingeloggt ist bzw. die sich der User
     * zugeordnet hat. Falls dort Initialpasswort: dieses aufdrucken, sonst "eigenes Passwort vergeben"
     * 
     * > EK Bevollmächtigt:
     * >> Wenn ein neuer Bevollmächtigter angelegt wird, dann muß in BlWillenserklaerung Kennung und
     * Passwort erzeugt und zurückgegeben werden => diese verwenden
     * >> Wenn kein neuer Bevollmächtigter angelegt wurde, dann darf das Passwort _NICHT_ mit angedruckt
     * werden, da sonst der "Ausdruckende" den Zugang auf alle anderen Daten des Bevollmächtigten
     * erhalten würde. In diesem Fall also nur Ausdruck der Zugangskennung
     */

    /********************************************Briefwahl / SRV / KIAV****************************************************************/
    public boolean srvBriefwahlBeiErstanmeldung(EclBesitzAREintrag pEclBesitzAREintrag, boolean pMitBestaetigung) {
        return srvBriefwahl(pEclBesitzAREintrag, null, pMitBestaetigung);
    }

    public boolean srvBriefwahlZuBestehenderAnmeldung(EclZugeordneteMeldungNeu pEclZugeordneteMeldung, boolean pMitBestaetigung) {
        return srvBriefwahl(null, pEclZugeordneteMeldung, pMitBestaetigung);
    }

    /**pEclZugeordneteMeldung nur gefüllt bei Aufruf über srvBriefwahlZuBestehenderAnmeldung*/
    private boolean srvBriefwahl(EclBesitzAREintrag pEclBesitzAREintrag,
            EclZugeordneteMeldungNeu pEclZugeordneteMeldung, boolean pMitBestaetigung) {
        
        /*Übergabe-Werte für bestaetigen*/
        boolean bestaetigungErstAnmeldung=false;
        EclAktienregister bestaetigungAktienregister=null;
        EclMeldung bestaetigungMeldung=null;
        
        
        BlWillenserklaerung vwWillenserklaerung = new BlWillenserklaerung();
        vwWillenserklaerung.pQuelle = tWillenserklaerungSession.getQuelle();
        vwWillenserklaerung.pErteiltAufWeg = tWillenserklaerungSession.getEingabeQuelle();
        vwWillenserklaerung.pErteiltZeitpunkt = tWillenserklaerungSession.getErteiltZeitpunkt();

        /*Gattung aus AREintrag bzg. Meldung, wird für Auswahl der betreffenden Sammelkarte benötigt*/
        int gattungDerMeldung = 0;

        if (tWillenserklaerungSession.getIntAusgewaehlteHauptAktion() == KonstPortalAktion.HAUPT_NEUANMELDUNG) {
            /*Erstanmeldung
             *In lWillenserklaerung.rcMeldungen[0] steht Anmelde-Ident, die weiterverwendet werden kann */
            vwWillenserklaerung.piMeldungsIdentAktionaer = lWillenserklaerung.rcMeldungen[0];
            vwWillenserklaerung.pFolgeFuerWillenserklaerungIdent = lWillenserklaerung.rcWillenserklaerungIdent;
            vwWillenserklaerung.pWillenserklaerungGeberIdent = -1; /*Aktionär*/

            gattungDerMeldung = pEclBesitzAREintrag.aktienregisterEintrag.getGattungId();
            
            bestaetigungErstAnmeldung=true;
            bestaetigungMeldung=lWillenserklaerung.rcEclMeldungen[0];
            bestaetigungAktienregister=pEclBesitzAREintrag.aktienregisterEintrag;

        } else {/*Anmeldung bereits früher durchgeführt*/
            vwWillenserklaerung.piMeldungsIdentAktionaer = pEclZugeordneteMeldung.getMeldungsIdent();

            if (pEclZugeordneteMeldung
                    .getArtBeziehung() == KonstZugeordneteMeldungArtBeziehung.EIGENE_AKTIEN_AUS_AKTIENREGISTER
                    || pEclZugeordneteMeldung
                            .getArtBeziehung() == KonstZugeordneteMeldungArtBeziehung.INSTI_AKTIENREGISTER
                    || pEclZugeordneteMeldung.getArtBeziehung() == KonstZugeordneteMeldungArtBeziehung.INSTI_MELDUNG) {
                vwWillenserklaerung.pWillenserklaerungGeberIdent = -1; /*Aktionär*/
            } else { /*Hier: es handelt sich um eine Untervollmacht. D.h. Geber = angemeldete personNatJur.*/
                vwWillenserklaerung.pWillenserklaerungGeberIdent = pEclZugeordneteMeldung.personNatJurIdent;
            }

            gattungDerMeldung = pEclZugeordneteMeldung.eclMeldung.liefereGattung();
            
            bestaetigungMeldung=pEclZugeordneteMeldung.eclMeldung;
        }

        CaBug.druckeLog("gattungDerMeldung=" + gattungDerMeldung, logDrucken, 10);

        /*pAufnahmendeSammelkarteIdent - aufnehmende Sammelkarte*/
        if (tWillenserklaerungSession.getAbweichendeSammelkarte() == -1) {
            if (tWillenserklaerungSession
                    .getIntAusgewaehlteAktion() == KonstPortalAktion.SRV_NEU) { /*Vollmacht/Weisung Stimmrechtsvertreter*/
                /*Sammelkartennr für Vollmacht/Weisung ermitteln*/
                if (KonstWillenserklaerungWeg
                        .getWeisungsWegPapierOderElektronisch(tWillenserklaerungSession.getEingabeQuelle()) == 2) {
                    eclDbM.getDbBundle().dbMeldungen.leseSammelkarteSRVInternet(gattungDerMeldung);
                } else {
                    eclDbM.getDbBundle().dbMeldungen.leseSammelkarteSRVPapier(gattungDerMeldung);
                }
                vwWillenserklaerung.pAufnehmendeSammelkarteIdent = eclDbM
                        .getDbBundle().dbMeldungen.meldungenArray[0].meldungsIdent;
            } else { /*Briefwahl*/
                /*Sammelkartennr für Briefwahl ermitteln*/
                if (KonstWillenserklaerungWeg
                        .getWeisungsWegPapierOderElektronisch(tWillenserklaerungSession.getEingabeQuelle()) == 2) {
                    eclDbM.getDbBundle().dbMeldungen.leseSammelkarteBriefwahlInternet(gattungDerMeldung);
                } else {
                    eclDbM.getDbBundle().dbMeldungen.leseSammelkarteBriefwahlPapier(gattungDerMeldung);
                }
                vwWillenserklaerung.pAufnehmendeSammelkarteIdent = eclDbM
                        .getDbBundle().dbMeldungen.meldungenArray[0].meldungsIdent;
            }
        } else {
            vwWillenserklaerung.pAufnehmendeSammelkarteIdent = tWillenserklaerungSession.getAbweichendeSammelkarte();
        }

        /*Abgegebene Weisung (uninterpretiert)
        public EclWeisungMeldungRaw pEclWeisungMeldungRaw=null;*/
        vwWillenserklaerung.pEclWeisungMeldungRaw = new EclWeisungMeldungRaw();
        /*Abgegebene Weisung (interpretiert)
        public EclWeisungMeldung pEclWeisungMeldung=null;*/
        vwWillenserklaerung.pEclWeisungMeldung = new EclWeisungMeldung();

        if (eclAbstimmungenListeM.getAlternative() == 1) { /**Alternative 1*/

            List<EclAbstimmungM> lAbstimmungenListe = eclAbstimmungenListeM.getAbstimmungenListeM();

            /**Zur Sicherheit (falls manuelle Eingabe etc.): zu viel abgegebene Stimmen bei Gruppen auf Ungültig setzen*/
            int[] anzahlJeGruppe = new int[11];
            for (int i1 = 1; i1 <= 10; i1++) {
                anzahlJeGruppe[i1] = 0;
            }

            for (int i1 = 0; i1 < lAbstimmungenListe.size(); i1++) {
                if (!lAbstimmungenListe.get(i1).isUeberschrift()) {

                    int gruppe = lAbstimmungenListe.get(i1).getZuAbstimmungsgruppe();
                    //                  System.out.println("Abstimmungsgruppe="+gruppe);
                    if (gruppe != 0) {
                        if (lAbstimmungenListe.get(i1).getGewaehlt() != null
                                && lAbstimmungenListe.get(i1).getGewaehlt().compareTo("J") == 0) {
                            //                          System.out.println("Gruppe++");
                            anzahlJeGruppe[gruppe]++;
                        }
                    }
                }
            }

            for (int i2 = 1; i2 <= 10; i2++) {
                if (anzahlJeGruppe[i2] > eclParamM
                        .getParam().paramAbstimmungParameter.anzahlJaJeAbstimmungsgruppe[i2]) {
                    for (int i1 = 0; i1 < lAbstimmungenListe.size(); i1++) {
                        if (!lAbstimmungenListe.get(i1).isUeberschrift()) {

                            int gruppe = lAbstimmungenListe.get(i1).getZuAbstimmungsgruppe();
                            //                  System.out.println("Abstimmungsgruppe="+gruppe);
                            if (gruppe == i2) {
                                lAbstimmungenListe.get(i1).setGewaehlt("U");
                            }
                        }
                    }
                }
            }

            /**Gruppenbearbeitung Ende*/

            for (int i = 0; i < lAbstimmungenListe.size(); i++) {

                int posInWeisung = lAbstimmungenListe.get(i).getIdentWeisungssatz();

                if (!lAbstimmungenListe.get(i).isUeberschrift() && lAbstimmungenListe.get(i).getGewaehlt() != null) {
                    switch (lAbstimmungenListe.get(i).getGewaehlt()) {
                    case "J":
                        vwWillenserklaerung.pEclWeisungMeldungRaw.abgabe[posInWeisung] = " X";
                        vwWillenserklaerung.pEclWeisungMeldung.abgabe[posInWeisung] = 1;
                        break;
                    case "N":
                        vwWillenserklaerung.pEclWeisungMeldungRaw.abgabe[posInWeisung] = "  X";
                        vwWillenserklaerung.pEclWeisungMeldung.abgabe[posInWeisung] = 2;
                        break;
                    case "E":
                        vwWillenserklaerung.pEclWeisungMeldungRaw.abgabe[posInWeisung] = "   X";
                        vwWillenserklaerung.pEclWeisungMeldung.abgabe[posInWeisung] = 3;
                        break;
                    case "U":
                        vwWillenserklaerung.pEclWeisungMeldungRaw.abgabe[posInWeisung] = "    X";
                        vwWillenserklaerung.pEclWeisungMeldung.abgabe[posInWeisung] = 4;
                        break;

                    }
                }
            }

            List<EclAbstimmungM> lGegenantraegeListe = eclAbstimmungenListeM.getGegenantraegeListeM();

            for (int i = 0; i < lGegenantraegeListe.size(); i++) {

                int posInWeisung = lGegenantraegeListe.get(i).getIdentWeisungssatz();

                if (!lGegenantraegeListe.get(i).isUeberschrift() && lGegenantraegeListe.get(i).isMarkiert()) {
                    vwWillenserklaerung.pEclWeisungMeldungRaw.abgabe[posInWeisung] = "            X";
                    vwWillenserklaerung.pEclWeisungMeldung.abgabe[posInWeisung] = 12;
                } else {
                    if (!lGegenantraegeListe.get(i).isUeberschrift()
                            && lGegenantraegeListe.get(i).getGewaehlt() != null) {
                        switch (lGegenantraegeListe.get(i).getGewaehlt()) {
                        case "J":
                            vwWillenserklaerung.pEclWeisungMeldungRaw.abgabe[posInWeisung] = " X";
                            vwWillenserklaerung.pEclWeisungMeldung.abgabe[posInWeisung] = 1;
                            break;
                        case "N":
                            vwWillenserklaerung.pEclWeisungMeldungRaw.abgabe[posInWeisung] = "  X";
                            vwWillenserklaerung.pEclWeisungMeldung.abgabe[posInWeisung] = 2;
                            break;
                        case "E":
                            vwWillenserklaerung.pEclWeisungMeldungRaw.abgabe[posInWeisung] = "   X";
                            vwWillenserklaerung.pEclWeisungMeldung.abgabe[posInWeisung] = 3;
                            break;
                        case "U":
                            vwWillenserklaerung.pEclWeisungMeldungRaw.abgabe[posInWeisung] = "    X";
                            vwWillenserklaerung.pEclWeisungMeldung.abgabe[posInWeisung] = 4;
                            break;

                        }
                    }
                }

            }
        }

        else { /******Alternative 2 - ist fertig aufbereitet************/
            vwWillenserklaerung.pEclWeisungMeldung = eclAbstimmungenListeM.getWeisungMeldung();
            vwWillenserklaerung.pEclWeisungMeldungRaw = eclAbstimmungenListeM.getWeisungMeldungRaw();
            for (int i1 = 0; i1 < 200; i1++) {
                if (vwWillenserklaerung.pEclWeisungMeldung.abgabe[i1] == -999) {
                    vwWillenserklaerung.pEclWeisungMeldung.abgabe[i1] = KonstStimmart.nichtMarkiert;

                }
            }
        }

        /*Willenserklärung speichern*/
        switch (tWillenserklaerungSession.getIntAusgewaehlteAktion()) {
        case KonstPortalAktion.SRV_NEU:
            vwWillenserklaerung.vollmachtUndWeisungAnSRV(eclDbM.getDbBundle());
            break;
        case KonstPortalAktion.BRIEFWAHL_NEU:
            vwWillenserklaerung.briefwahl(eclDbM.getDbBundle());
            break;
        case KonstPortalAktion.KIAV_NUR_VOLLMACHT_NEU:
        case KonstPortalAktion.KIAV_VOLLMACHT_UND_WEISUNG_DEDIZIERT_NEU:
        case KonstPortalAktion.KIAV_VOLLMACHT_UND_WEISUNG_GEMAESS_VORSCHLAG_NEU:
            vwWillenserklaerung.vollmachtUndWeisungAnKIAV(eclDbM.getDbBundle());
            break;
        case KonstPortalAktion.DAUERVOLLMACHT_NUR_VOLLMACHT_NEU:
        case KonstPortalAktion.DAUERVOLLMACHT_VOLLMACHT_UND_WEISUNG_DEDIZIERT_NEU:
        case KonstPortalAktion.DAUERVOLLMACHT_VOLLMACHT_UND_WEISUNG_GEMAESS_VORSCHLAG_NEU:
            vwWillenserklaerung.dauervollmachtAnKIAV(eclDbM.getDbBundle());
            break;
        case KonstPortalAktion.ORGANISATORISCH_NUR_VOLLMACHT_NEU:
        case KonstPortalAktion.ORGANISATORISCH_VOLLMACHT_UND_WEISUNG_DEDIZIERT_NEU:
        case KonstPortalAktion.ORGANISATORISCH_VOLLMACHT_UND_WEISUNG_GEMAESS_VORSCHLAG_NEU:
            vwWillenserklaerung.organisatorischMitWeisungInSammelkarte(eclDbM.getDbBundle());
            break;
        }


        
        
        tWillenserklaerungSession.setRcWillenserklaerungIdentAusgefuehrt(vwWillenserklaerung.rcWillenserklaerungIdent);

        /*Falls nicht möglich: Fehlermeldung, zum Anmelden*/
        if (vwWillenserklaerung.rcIstZulaessig == false) {
            tSession.trageFehlerEin(vwWillenserklaerung.rcGrundFuerUnzulaessig);
            return false;
        }

        if (pMitBestaetigung) {
            tQuittungen.bestaetigenSrvBriefwahl(vwWillenserklaerung.rcWillenserklaerungIdent, bestaetigungErstAnmeldung, bestaetigungMeldung, bestaetigungAktienregister);
        }

        return true;

    }

    public boolean srvBriefwahlAendern(int pI, EclZugeordneteMeldungNeu pEclZugeordneteMeldung) {

        CaBug.druckeLog("", logDrucken, 10);
        /*Ändern setzt immer voraus, dass vorher schon der Anmeldeprozess durchgelaufen ist. D.h.: hier ist immer
         * ausgewaehlteHauptaktion=2, d.h. in eclZugeordneteMeldung steht zur Verfügung
         */

        BlWillenserklaerung vwWillenserklaerung = new BlWillenserklaerung();
        vwWillenserklaerung.pQuelle = tWillenserklaerungSession.getQuelle();
        vwWillenserklaerung.pErteiltAufWeg = tWillenserklaerungSession.getEingabeQuelle();
        vwWillenserklaerung.pErteiltZeitpunkt = tWillenserklaerungSession.getErteiltZeitpunkt();
        vwWillenserklaerung.piMeldungsIdentAktionaer = tWillenserklaerungSession.getMeldungsIdentListe().get(pI);

        if (pEclZugeordneteMeldung
                .getArtBeziehung() == KonstZugeordneteMeldungArtBeziehung.EIGENE_AKTIEN_AUS_AKTIENREGISTER
                || pEclZugeordneteMeldung.getArtBeziehung() == KonstZugeordneteMeldungArtBeziehung.INSTI_AKTIENREGISTER
                || pEclZugeordneteMeldung.getArtBeziehung() == KonstZugeordneteMeldungArtBeziehung.INSTI_MELDUNG) {
            vwWillenserklaerung.pWillenserklaerungGeberIdent = -1; /*Aktionär*/
        } else { /*Hier: es handelt sich um eine Untervollmacht. D.h. Geber = angemeldete personNatJur.*/
            vwWillenserklaerung.pWillenserklaerungGeberIdent = pEclZugeordneteMeldung.personNatJurIdent;
        }

        vwWillenserklaerung.pAufnehmendeSammelkarteIdent = tWillenserklaerungSession.getSammelIdentListe().get(pI);

        /*Abgegebene Weisung (uninterpretiert)
        public EclWeisungMeldungRaw pEclWeisungMeldungRaw=null;*/
        vwWillenserklaerung.pEclWeisungMeldungRaw = new EclWeisungMeldungRaw();
        /*Abgegebene Weisung (interpretiert)
        public EclWeisungMeldung pEclWeisungMeldung=null;*/
        vwWillenserklaerung.pEclWeisungMeldung = new EclWeisungMeldung();
        /*Alte WeisungsIdent, d.h. Ident der Weisung, die geändert wird*/
        vwWillenserklaerung.pEclWeisungMeldung.weisungIdent = tWillenserklaerungSession.getWeisungsIdentListe().get(pI);
        /*Alte Willenserklärung, die zu der zu ändernden Weisung gehört*/
        vwWillenserklaerung.pEclWeisungMeldung.willenserklaerungIdent = tWillenserklaerungSession
                .getWillenserklaerungIdentListe().get(pI);

        if (eclAbstimmungenListeM.getAlternative() == 1) { /**Alternative 1*/

            CaBug.druckeLog("in Alternative 1", logDrucken, 10);
            List<EclAbstimmungM> lAbstimmungenListe = eclAbstimmungenListeM.getAbstimmungenListeM();

            /**Zur Sicherheit (falls manuelle Eingabe etc.): zu viel abgegebene Stimmen bei Gruppen auf Ungültig setzen*/
            int[] anzahlJeGruppe = new int[11];
            for (int i1 = 1; i1 <= 10; i1++) {
                anzahlJeGruppe[i1] = 0;
            }

            for (int i1 = 0; i1 < lAbstimmungenListe.size(); i1++) {
                if (!lAbstimmungenListe.get(i1).isUeberschrift()) {

                    int gruppe = lAbstimmungenListe.get(i1).getZuAbstimmungsgruppe();
                    //                  System.out.println("Abstimmungsgruppe="+gruppe);
                    if (gruppe != 0) {
                        if (lAbstimmungenListe.get(i1).getGewaehlt() != null
                                && lAbstimmungenListe.get(i1).getGewaehlt().compareTo("J") == 0) {
                            //                          System.out.println("Gruppe++");
                            anzahlJeGruppe[gruppe]++;
                        }
                    }
                }
            }

            for (int i2 = 1; i2 <= 10; i2++) {
                if (anzahlJeGruppe[i2] > eclParamM
                        .getParam().paramAbstimmungParameter.anzahlJaJeAbstimmungsgruppe[i2]) {
                    for (int i1 = 0; i1 < lAbstimmungenListe.size(); i1++) {
                        if (!lAbstimmungenListe.get(i1).isUeberschrift()) {

                            int gruppe = lAbstimmungenListe.get(i1).getZuAbstimmungsgruppe();
                            //                  System.out.println("Abstimmungsgruppe="+gruppe);
                            if (gruppe == i2) {
                                lAbstimmungenListe.get(i1).setGewaehlt("U");
                            }
                        }
                    }

                }
            }

            /**Gruppenbearbeitung Ende*/

            for (int i = 0; i < lAbstimmungenListe.size(); i++) {

                int posInWeisung = lAbstimmungenListe.get(i).getIdentWeisungssatz();

                if (!lAbstimmungenListe.get(i).isUeberschrift() && lAbstimmungenListe.get(i).getGewaehlt() != null) {
                    switch (lAbstimmungenListe.get(i).getGewaehlt()) {
                    case "J":
                        vwWillenserklaerung.pEclWeisungMeldungRaw.abgabe[posInWeisung] = " X";
                        vwWillenserklaerung.pEclWeisungMeldung.abgabe[posInWeisung] = 1;
                        break;
                    case "N":
                        vwWillenserklaerung.pEclWeisungMeldungRaw.abgabe[posInWeisung] = "  X";
                        vwWillenserklaerung.pEclWeisungMeldung.abgabe[posInWeisung] = 2;
                        break;
                    case "E":
                        vwWillenserklaerung.pEclWeisungMeldungRaw.abgabe[posInWeisung] = "   X";
                        vwWillenserklaerung.pEclWeisungMeldung.abgabe[posInWeisung] = 3;
                        break;
                    case "U":
                        vwWillenserklaerung.pEclWeisungMeldungRaw.abgabe[posInWeisung] = "    X";
                        vwWillenserklaerung.pEclWeisungMeldung.abgabe[posInWeisung] = 4;
                        break;
                    case "2":
                        vwWillenserklaerung.pEclWeisungMeldungRaw.abgabe[posInWeisung] = "       X";
                        vwWillenserklaerung.pEclWeisungMeldung.abgabe[posInWeisung] = 7;
                        break;
                    case "S":
                        vwWillenserklaerung.pEclWeisungMeldungRaw.abgabe[posInWeisung] = "         X";
                        vwWillenserklaerung.pEclWeisungMeldung.abgabe[posInWeisung] = 9;
                        break;

                    }
                }
            }

            List<EclAbstimmungM> lGegenantraegeListe = eclAbstimmungenListeM.getGegenantraegeListeM();

            for (int i = 0; i < lGegenantraegeListe.size(); i++) {

                int posInWeisung = lGegenantraegeListe.get(i).getIdentWeisungssatz();

                if (!lGegenantraegeListe.get(i).isUeberschrift() && lGegenantraegeListe.get(i).isMarkiert()) {
                    vwWillenserklaerung.pEclWeisungMeldungRaw.abgabe[posInWeisung] = "            X";
                    vwWillenserklaerung.pEclWeisungMeldung.abgabe[posInWeisung] = 12;
                } else {
                    if (!lGegenantraegeListe.get(i).isUeberschrift()
                            && lGegenantraegeListe.get(i).getGewaehlt() != null) {
                        switch (lGegenantraegeListe.get(i).getGewaehlt()) {
                        case "J":
                            vwWillenserklaerung.pEclWeisungMeldungRaw.abgabe[posInWeisung] = " X";
                            vwWillenserklaerung.pEclWeisungMeldung.abgabe[posInWeisung] = 1;
                            break;
                        case "N":
                            vwWillenserklaerung.pEclWeisungMeldungRaw.abgabe[posInWeisung] = "  X";
                            vwWillenserklaerung.pEclWeisungMeldung.abgabe[posInWeisung] = 2;
                            break;
                        case "E":
                            vwWillenserklaerung.pEclWeisungMeldungRaw.abgabe[posInWeisung] = "   X";
                            vwWillenserklaerung.pEclWeisungMeldung.abgabe[posInWeisung] = 3;
                            break;
                        case "U":
                            vwWillenserklaerung.pEclWeisungMeldungRaw.abgabe[posInWeisung] = "    X";
                            vwWillenserklaerung.pEclWeisungMeldung.abgabe[posInWeisung] = 4;
                            break;
                        case "2":
                            vwWillenserklaerung.pEclWeisungMeldungRaw.abgabe[posInWeisung] = "       X";
                            vwWillenserklaerung.pEclWeisungMeldung.abgabe[posInWeisung] = 7;
                            break;
                        case "S":
                            vwWillenserklaerung.pEclWeisungMeldungRaw.abgabe[posInWeisung] = "         X";
                            vwWillenserklaerung.pEclWeisungMeldung.abgabe[posInWeisung] = 9;
                            break;

                        }
                    }
                }

            }
        } else { /******Alternative 2 - ist fertig aufbereitet************/
            CaBug.druckeLog("in Alternative 2", logDrucken, 10);
            vwWillenserklaerung.pEclWeisungMeldung.abgabe = eclAbstimmungenListeM.getWeisungMeldung().abgabe;
            vwWillenserklaerung.pEclWeisungMeldungRaw.abgabe = eclAbstimmungenListeM.getWeisungMeldungRaw().abgabe;
            for (int i1 = 0; i1 < 200; i1++) {
                //              if (i1<20) {System.out.println("i1="+vwWillenserklaerung.pEclWeisungMeldung.abgabe[i1]);}
                if (vwWillenserklaerung.pEclWeisungMeldung.abgabe[i1] == -999) {
                    vwWillenserklaerung.pEclWeisungMeldung.abgabe[i1] = KonstStimmart.nichtMarkiert;

                }
            }
        }

        /*Sonstige Parameter für Willenserklärung Weisungsänderung speichern*/
        /*Derzeit nicht erforderlich*/

        /*Willenserklärung speichern*/
        if (tWillenserklaerungSession.getIntAusgewaehlteAktion() == KonstPortalAktion.SRV_AENDERN) {
            vwWillenserklaerung.aendernWeisungAnSRV(eclDbM.getDbBundle());
        }
        if (tWillenserklaerungSession.getIntAusgewaehlteAktion() == KonstPortalAktion.BRIEFWAHL_AENDERN) {
            vwWillenserklaerung.aendernBriefwahl(eclDbM.getDbBundle());
        }
        if (tWillenserklaerungSession.getIntAusgewaehlteAktion() == KonstPortalAktion.KIAV_WEISUNG_AENDERN) {
            vwWillenserklaerung.aendernWeisungAnKIAV(eclDbM.getDbBundle());
        }
        if (tWillenserklaerungSession.getIntAusgewaehlteAktion() == KonstPortalAktion.DAUERVOLLMACHT_AENDERN) {
            vwWillenserklaerung.aendernWeisungDauervollmachtAnKIAV(eclDbM.getDbBundle());
        }
        if (tWillenserklaerungSession.getIntAusgewaehlteAktion() == KonstPortalAktion.ORGANISATORISCH_AENDERN) {
            vwWillenserklaerung.aendernWeisungOrganisatorischInSammelkarte(eclDbM.getDbBundle());
        }

        /*Falls nicht möglich: Fehlermeldung, zum Anmelden*/
        if (vwWillenserklaerung.rcIstZulaessig == false) {
            tSession.trageFehlerEin(vwWillenserklaerung.rcGrundFuerUnzulaessig);
            return false;
        }
        
        tQuittungen.bestaetigenSrvBriefwahlAendern(vwWillenserklaerung.rcWillenserklaerungIdent, pEclZugeordneteMeldung);
        return true;
    }


    
    /********************Standard getter und setter***********************************************************/
    
//    public BlWillenserklaerung getlWillenserklaerung() {
//        return lWillenserklaerung;
//    }
//
//    public void setlWillenserklaerung(BlWillenserklaerung lWillenserklaerung) {
//        this.lWillenserklaerung = lWillenserklaerung;
//    }


}
