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

import java.util.LinkedList;
import java.util.List;

import de.meetingapps.meetingportal.meetComAllg.CaBug;
import de.meetingapps.meetingportal.meetComAllg.CaDatumZeit;
import de.meetingapps.meetingportal.meetComAllg.CaFehler;
import de.meetingapps.meetingportal.meetComBl.BlAbstimmungenWeisungen;
import de.meetingapps.meetingportal.meetComBl.BlNummernformen;
import de.meetingapps.meetingportal.meetComBl.BlTeilnehmerLoginNeu;
import de.meetingapps.meetingportal.meetComBl.BlWillenserklaerungStatusNeu;
import de.meetingapps.meetingportal.meetComEclM.EclAbstimmungenListeM;
import de.meetingapps.meetingportal.meetComEclM.EclBesitzGesamtAuswahl1M;
import de.meetingapps.meetingportal.meetComEclM.EclBesitzGesamtM;
import de.meetingapps.meetingportal.meetComEclM.EclDbM;
import de.meetingapps.meetingportal.meetComEclM.EclLoginDatenM;
import de.meetingapps.meetingportal.meetComEclM.EclParamM;
import de.meetingapps.meetingportal.meetComEntities.EclBesitzAREintrag;
import de.meetingapps.meetingportal.meetComEntities.EclScan;
import de.meetingapps.meetingportal.meetComEntities.EclVerarbeitungsLauf;
import de.meetingapps.meetingportal.meetComEntities.EclVerarbeitungsProtokoll;
import de.meetingapps.meetingportal.meetComHVParam.ParamSpezial;
import de.meetingapps.meetingportal.meetComKonst.KonstEingabeQuelle;
import de.meetingapps.meetingportal.meetComKonst.KonstPortalAktion;
import de.meetingapps.meetingportal.meetComKonst.KonstSkIst;
import de.meetingapps.meetingportal.meetComKonst.KonstVerarbeitungsArt;
import de.meetingapps.meetingportal.meetComKonst.KonstVerarbeitungsStatus;
import de.meetingapps.meetingportal.meetComKonst.KonstWeisungserfassungSicht;
import de.meetingapps.meetingportal.meetComKonst.KonstWillenserklaerung;
import de.meetingapps.meetingportal.meetComKonst.KonstWillenserklaerungVersandartEK;
import de.meetingapps.meetingportal.meetingportTController.TAnmeldenOhneErklaerung;
import de.meetingapps.meetingportal.meetingportTController.TEintrittskarte;
import de.meetingapps.meetingportal.meetingportTController.TSession;
import de.meetingapps.meetingportal.meetingportTController.TWeisungBestaetigung;
import de.meetingapps.meetingportal.meetingportTController.TWillenserklaerungSession;
import de.meetingapps.meetingportal.meetingportTFunktionen.TFunktionen;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;

@RequestScoped
public class BlMScanlauf {
    private int logDrucken = 3;

    private @Inject
    EclDbM eclDbM;
    private @Inject
    EclParamM eclParamM;
    private @Inject
    EclAbstimmungenListeM eclAbstimmungenListeM;

    private @Inject TSession tSession;
    private @Inject EclLoginDatenM eclLoginDatenM;
    private @Inject EclBesitzGesamtM eclBesitzGesamtM;
    private @Inject EclBesitzGesamtAuswahl1M eclBesitzGesamtAuswahl1M;
    private @Inject TFunktionen tFunktionen;
    private @Inject TWillenserklaerungSession tWillenserklaerungSession;
    private @Inject TEintrittskarte tEintrittskarte;
    private @Inject TWeisungBestaetigung tWeisungBestaetigung;
    private @Inject TAnmeldenOhneErklaerung tAnmeldenOhneErklaerung;
 
    private @Inject BlMAbstimmung blMAbstimmung;
    private @Inject BlMAbstimmungsvorschlag blMAbstimmungsvorschlag;

    /****************Eingabe-Werte***************************/
    private int eingabeQuelle = 0;
    private String erteiltZeitpunkt = "";

    /*************Return-Werte****************************/

    private int rcAktuellerVerarbeitungslauf = -1;

    /******Werte, die über verschiedene Funktionen genutzt werden************/

    /**Aktueller Verarbeitungslauf*/
    private EclVerarbeitungsLauf aktuellerVerarbeitungsLauf = null;
    private EclScan eclScan = null;

    private BlAbstimmungenWeisungen blAbstimmungenWeisungenErfassen = null;

    /**eclDbM muß geöffnet sein*/
    public void verarbeiten() {

        
        /*Neuen Verarbeitungslauf generieren*/
        aktuellerVerarbeitungsLauf = new EclVerarbeitungsLauf();
        aktuellerVerarbeitungsLauf.mandant = eclParamM.getClGlobalVar().mandant;
        aktuellerVerarbeitungsLauf.verarbeitungsArt = KonstVerarbeitungsArt.scanLaufAnmeldestelle;
        aktuellerVerarbeitungsLauf.statusDesLaufs = KonstVerarbeitungsStatus.inArbeit;
        aktuellerVerarbeitungsLauf.verarbeitungsZeit = CaDatumZeit.DatumZeitStringFuerDatenbank();
        eclDbM.getDbBundle().dbVerarbeitungsLauf.insert(aktuellerVerarbeitungsLauf);
        System.out.println("aktuellerVerarbeitungsLauf=" + aktuellerVerarbeitungsLauf.ident);
        /*Alle zu verarbeitenden Scan-Sätze diesem Verarbeitungslauf zuordnen*/
        rcAktuellerVerarbeitungslauf = aktuellerVerarbeitungsLauf.ident;
        eclDbM.getDbBundle().dbScan.updateUngeleseneScanVorgaenge(rcAktuellerVerarbeitungslauf, true, true, true, false,
                false, true, true, true);
        eclDbM.getDbBundle().dbBasis.zwischenCommit();

        blAbstimmungenWeisungenErfassen = new BlAbstimmungenWeisungen(true, eclDbM.getDbBundle());
        blAbstimmungenWeisungenErfassen.leseAgendaFuerInterneWeisungenErfassung();

        verarbeitenHintergrund();
    }

    private EclScan[] scanListe = null;

    public void verarbeitenHintergrund() {
        /*Alle zu verarbeitenden Scan-Sätze einlesen*/
        int anzVorgaenge = eclDbM.getDbBundle().dbScan.read_scanVorgaenge(rcAktuellerVerarbeitungslauf);
        scanListe = eclDbM.getDbBundle().dbScan.ergebnisArray;

        for (int i = 0; i < anzVorgaenge; i++) {//Lauf für alle Scanvorgänge
            CaBug.druckeLog("i=" + i, logDrucken, 10);
            verarbeiteEinzelScan(i);
            eclDbM.getDbBundle().dbBasis.zwischenCommit();

        } //Ende Lauf für alle Scanvorgänge

        /*Verarbeitungslauf auf "Fertig" setzen*/
        aktuellerVerarbeitungsLauf.statusDesLaufs = KonstVerarbeitungsStatus.fertig;
        eclDbM.getDbBundle().dbVerarbeitungsLauf.update(aktuellerVerarbeitungsLauf);
        CaBug.druckeLog("Ende verarbeitenHintergrund", logDrucken, 10);
    }

    private void verarbeiteEinzelScan(int i) {
        eclScan = scanListe[i];

//        if (ParamSpezial.ku111(eclScan.mandant)) {
//            if (eclScan.istSRV == 1) {
//                for (int i1 = 1; i1 <= 4; i1++)
//                    if (eclScan.pos[i1].equals("4")) {
//                        eclScan.pos[i1] = "1";
//                    }
//            }
//        }

        if (ParamSpezial.ku108(eclScan.mandant)) {
            if (eclScan.gesamtmarkierung.equals("1")) {
                for (int i1 = 1; i1 <= 7; i1++)
                    if (eclScan.pos[i1].equals("0")) {
                        eclScan.pos[i1] = "1";
                    }
            }
            for (int i1 = 1; i1 <= 7; i1++)
                if (eclScan.pos[i1].equals("0")) {
                    eclScan.pos[i1] = "3";
                }

        }

//        if (ParamSpezial.ku178(eclDbM.getDbBundle().param.mandant)) {
//            eclScan.barcode = CaString.fuelleLinksNull(eclScan.barcode, 11);
//        }
        BlNummernformen blNummernformen = new BlNummernformen(eclDbM.getDbBundle());
        String hAktionaersnummer = blNummernformen.aktienregisterNraufbereitenFuerIntern(eclScan.barcode);
        eclScan.barcode = hAktionaersnummer;

        CaBug.druckeLog("Verarbeite =" + eclScan.barcode, logDrucken, 10);

        /*Sicherheitshalber nochmal checken, ob Nicht-Anmeldestellen-Markierung vorliegt*/
        if (eclScan.istSRVHV != 0 || eclScan.istAbstimmung != 0) {
            schreibeFehlerInsProtokoll(CaFehler.vlFalscheErklaerungImLauf,
                    CaFehler.getFehlertext(CaFehler.vlFalscheErklaerungImLauf, 0));
            return;
        }

        /*Willenerklärungsliste mit den markierten Typen füllen*/
        initWillenserklaerungsListe();
        if (eclScan.istAnmelden == 1) {
            addWillenserklaerungsListe(KonstWillenserklaerung.anmeldungAusAktienregister);
        }
        if (eclScan.istBriefwahl == 1) {
            addWillenserklaerungsListe(KonstWillenserklaerung.briefwahl);
        }
        if (eclScan.istSRV == 1) {
            addWillenserklaerungsListe(KonstWillenserklaerung.vollmachtUndWeisungAnSRV);
        }
        if (eclScan.markiertIst1eK() == true) {
            addWillenserklaerungsListe(KonstWillenserklaerung.eineEKSelbst);
        }
        if (eclScan.ist1EKVollmacht != null && !eclScan.ist1EKVollmacht.isEmpty()
                && eclScan.ist1EKVollmacht.compareTo("0") != 0) {
            addWillenserklaerungsListe(KonstWillenserklaerung.eineEKVollmacht);
        }
        if (eclScan.ist2EK == 1) {
            addWillenserklaerungsListe(KonstWillenserklaerung.zweiEKSelbst);
        }
        if (!willenserklaerungsListeIstEindeutig()) {
            /*Hier könnte ggf. über Regelwerk noch "vereindeutigt werden"*/
            schreibeFehlerInsProtokoll(CaFehler.vlNichtEindeutig, CaFehler.getFehlertext(CaFehler.vlNichtEindeutig, 0));
            return;
        }

        /*Willenserklärung ausführen*/
        if (inWillenserklaerungsListeEnthalten(KonstWillenserklaerung.briefwahl)) {
            if (ParamSpezial.ku178(eclDbM.getDbBundle().param.mandant)) {
                /**Sonderversion ku178 Beiratswahl*/
                fuehreAusSRVBriefwahl(KonstPortalAktion.BRIEFWAHL_NEU, true);
            } else {
                fuehreAusSRVBriefwahl(KonstPortalAktion.BRIEFWAHL_NEU, false);
            }
            return;
        }
        if (inWillenserklaerungsListeEnthalten(KonstWillenserklaerung.vollmachtUndWeisungAnSRV)) {
            if (ParamSpezial.ku178(eclDbM.getDbBundle().param.mandant)) {
                /**Sonderversion ku178 Beiratswahl*/
                fuehreAusSRVBriefwahl(KonstPortalAktion.SRV_NEU, true);
            } else {
                fuehreAusSRVBriefwahl(KonstPortalAktion.SRV_NEU, false);
            }
            return;
        }
        if (inWillenserklaerungsListeEnthalten(KonstWillenserklaerung.anmeldungAusAktienregister)) {
            fuehreAusAnmelden();
            return;
        }
        if (inWillenserklaerungsListeEnthalten(KonstWillenserklaerung.eineEKSelbst)) {
            fuehreAusEK(KonstPortalAktion.EINE_EK_SELBST);
            return;
        }
        if (inWillenserklaerungsListeEnthalten(KonstWillenserklaerung.eineEKVollmacht)) {
            fuehreAusEineEKVollmacht();
            return;
        }
        if (inWillenserklaerungsListeEnthalten(KonstWillenserklaerung.zweiEKSelbst)) {
            fuehreAusEK(KonstPortalAktion.ZWEI_EK_SELBST_FUER_ALLE);
            return;
        }
        CaBug.drucke("001");
    }

    
    
    private void fuehreAusAnmelden() {

        tWillenserklaerungSession.clear();
        tSession.clearFehler();
        
        BlWillenserklaerungStatusNeu blWillenserklaerungStatus=new BlWillenserklaerungStatusNeu(eclDbM.getDbBundle());
        EclBesitzAREintrag lBesitzAREintrag=blWillenserklaerungStatus.einlesenBesitzAREintrag(eclScan.barcode, null);

        if (lBesitzAREintrag == null) {
            schreibeFehlerInsProtokoll(CaFehler.pfXyNichtVorhanden, "Aktionärsnummer nicht vorhanden");
            return;
        }

        tWillenserklaerungSession.initBesitzAREintragListe();
        tWillenserklaerungSession.addBesitzAREintragListe(lBesitzAREintrag);

        tWillenserklaerungSession.setAusgewaehlteHauptAktion(KonstPortalAktion.HAUPT_NEUANMELDUNG);
        tWillenserklaerungSession.setAusgewaehlteAktion(KonstPortalAktion.NUR_ANMELDUNG_OHNE_WEITERE_WILLENSERKLAERUNG);

        tWillenserklaerungSession.setEingabeQuelle(eingabeQuelle);
        tWillenserklaerungSession.setErteiltZeitpunkt(erteiltZeitpunkt);

        tWillenserklaerungSession.setQuelle(eclScan.dateiname);

       boolean ergBool = tAnmeldenOhneErklaerung.anmelden(false);

       if (ergBool == false) {/*Prüfung fehlgeschlagen*/
            schreibeFehlerInsProtokoll(tSession.getFehlerCode(), tSession.getFehlerMeldung());
            if (CaBug.pruefeLog(logDrucken, 10)) {
                CaBug.druckeInfo("BlMScanlauf.fuehreAusEK Fehlerstelle A");
            }
            return;
        } else {/*Prüfung ok - nun Ausführen, und dann Return besetzen*/
            tWillenserklaerungSession.setEingabeQuelle(KonstEingabeQuelle.papierPost_ausserhalbHV);
            ergBool = tEintrittskarte.anlegenAktionaerEK(eclDbM.getDbBundle(), false);

            if (ergBool == false) {
                schreibeFehlerInsProtokoll(tSession.getFehlerCode(), tSession.getFehlerMeldung());
                if (CaBug.pruefeLog(logDrucken, 10)) {
                    CaBug.druckeInfo("BlMScanlauf.fuehreAusEK Fehlerstelle B");
                }
                return;
            } else {
                String quittungsText = "Scanlauf: Nur Anmeldung ";
                schreibeOkInsProtokoll(quittungsText);
            }
        }
    }

    
    /**pAusgewaehlteAktion="1" => 1 EK Selbst
     * 				="30" => 2 EK Selbst
     */
    private void fuehreAusEK(int pAusgewaehlteAktion) {

        tWillenserklaerungSession.clear();
        tSession.clearFehler();
        
        BlWillenserklaerungStatusNeu blWillenserklaerungStatus=new BlWillenserklaerungStatusNeu(eclDbM.getDbBundle());
        EclBesitzAREintrag lBesitzAREintrag=blWillenserklaerungStatus.einlesenBesitzAREintrag(eclScan.barcode, null);

        if (lBesitzAREintrag == null) {
            schreibeFehlerInsProtokoll(CaFehler.pfXyNichtVorhanden, "Aktionärsnummer nicht vorhanden");
            return;
        }

        tWillenserklaerungSession.initBesitzAREintragListe();
        tWillenserklaerungSession.addBesitzAREintragListe(lBesitzAREintrag);

        tWillenserklaerungSession.setAusgewaehlteHauptAktion(KonstPortalAktion.HAUPT_NEUANMELDUNG);
        tWillenserklaerungSession.setAusgewaehlteAktion(pAusgewaehlteAktion);

        tWillenserklaerungSession.setEingabeQuelle(eingabeQuelle);
        tWillenserklaerungSession.setErteiltZeitpunkt(erteiltZeitpunkt);

        tWillenserklaerungSession.setQuelle(eclScan.dateiname);

//        aDlgVariablen.setUeberOeffentlicheID(false);
//        aDlgVariablen.setZielOeffentlicheID("");
//        aDlgVariablen.setPersonNatJurOeffentlicheID(0);
        tWillenserklaerungSession.setEintrittskarteVersandart(KonstWillenserklaerungVersandartEK.VERSANDLAUF_ADRESSE_LAUT_AKTIENREGISTER);
        tWillenserklaerungSession.setEintrittskarteAbweichendeAdresse1("");
        tWillenserklaerungSession.setEintrittskarteAbweichendeAdresse2("");
        tWillenserklaerungSession.setEintrittskarteAbweichendeAdresse3("");
        tWillenserklaerungSession.setEintrittskarteAbweichendeAdresse4("");
        tWillenserklaerungSession.setEintrittskarteAbweichendeAdresse5("");
        tWillenserklaerungSession.setEintrittskarteEmail("");
        tWillenserklaerungSession.setEintrittskarteEmailBestaetigen("");
        tWillenserklaerungSession.setVollmachtName("");
        tWillenserklaerungSession.setVollmachtVorname("");
        tWillenserklaerungSession.setVollmachtOrt("");
        tWillenserklaerungSession.setVollmachtEingeben(false);

        tWillenserklaerungSession.setEintrittskarteVersandart2("");
        tWillenserklaerungSession.setEintrittskarteAbweichendeAdresse12("");
        tWillenserklaerungSession.setEintrittskarteAbweichendeAdresse22("");
        tWillenserklaerungSession.setEintrittskarteAbweichendeAdresse32("");
        tWillenserklaerungSession.setEintrittskarteAbweichendeAdresse42("");
        tWillenserklaerungSession.setEintrittskarteAbweichendeAdresse52("");
        tWillenserklaerungSession.setEintrittskarteEmail2("");
        tWillenserklaerungSession.setEintrittskarteEmail2Bestaetigen("");
        tWillenserklaerungSession.setVollmachtName2("");
        tWillenserklaerungSession.setVollmachtVorname2("");
        tWillenserklaerungSession.setVollmachtOrt2("");
        tWillenserklaerungSession.setVollmachtEingeben2(false);



        Boolean ergBool = tEintrittskarte.pruefeEingabenFuerAktionaerEintrittskarte();

       if (ergBool == false) {/*Prüfung fehlgeschlagen*/
            schreibeFehlerInsProtokoll(tSession.getFehlerCode(), tSession.getFehlerMeldung());
            if (CaBug.pruefeLog(logDrucken, 10)) {
                CaBug.druckeInfo("BlMScanlauf.fuehreAusEK Fehlerstelle A");
            }
            return;
        } else {/*Prüfung ok - nun Ausführen, und dann Return besetzen*/
            tWillenserklaerungSession.setEingabeQuelle(KonstEingabeQuelle.papierPost_ausserhalbHV);
            ergBool = tEintrittskarte.anlegenAktionaerEK(eclDbM.getDbBundle(), false);

            if (ergBool == false) {
                schreibeFehlerInsProtokoll(tSession.getFehlerCode(), tSession.getFehlerMeldung());
                if (CaBug.pruefeLog(logDrucken, 10)) {
                    CaBug.druckeInfo("BlMScanlauf.fuehreAusEK Fehlerstelle B");
                }
                return;
            } else {
                String quittungsText = "";
                if (pAusgewaehlteAktion==KonstPortalAktion.EINE_EK_SELBST) {
                    quittungsText = "Scanlauf: Anmeldung und 1 Eintrittskarte Selbst "
                            + tWillenserklaerungSession.getZutrittsIdent() + "-" + tWillenserklaerungSession.getZutrittsIdentNeben();
                } else {
                    quittungsText = "Scanlauf: Anmeldung und 2 Eintrittskarten Selbst "
                            + tWillenserklaerungSession.getZutrittsIdent() + "-" + tWillenserklaerungSession.getZutrittsIdentNeben() + " "
                            + tWillenserklaerungSession.getZutrittsIdent2() + "-" + tWillenserklaerungSession.getZutrittsIdentNeben2();
                }
                schreibeOkInsProtokoll(quittungsText);
            }
        }
    }

    private void fuehreAusEineEKVollmacht() {
        schreibeFehlerInsProtokoll(CaFehler.vlEkMitVollmachtNichtMoeglich,
                CaFehler.getFehlertext(CaFehler.vlEkMitVollmachtNichtMoeglich, 0));
    }

    /**"4"=SRV, "5"=Briefwahl
     * pku178==true => ku178 Beiratswahl*/
    private void fuehreAusSRVBriefwahl(int pAusgewaehlteAktion, boolean pku178) {
        CaBug.druckeLog("", logDrucken, 3);

        tWillenserklaerungSession.clear();
        tSession.clearFehler();

        int gattung=0;
        if (!pku178) {
            BlWillenserklaerungStatusNeu blWillenserklaerungStatus=new BlWillenserklaerungStatusNeu(eclDbM.getDbBundle());
            EclBesitzAREintrag lBesitzAREintrag=blWillenserklaerungStatus.einlesenBesitzAREintrag(eclScan.barcode, null);

            if (lBesitzAREintrag == null) {
                schreibeFehlerInsProtokoll(CaFehler.pfXyNichtVorhanden, "Aktionärsnummer nicht vorhanden");
                return;
            }

            tWillenserklaerungSession.initBesitzAREintragListe();
            tWillenserklaerungSession.addBesitzAREintragListe(lBesitzAREintrag);

            /*Gattung ermitteln*/
            tWillenserklaerungSession.ermittleGattungenFuerBesitzAREintragListe();
            gattung=tWillenserklaerungSession.ermittleGattungFuerEinzigenBesitzAREintragList();
        }
        else {
              BlTeilnehmerLoginNeu blTeilnehmerLogin = new BlTeilnehmerLoginNeu();
              blTeilnehmerLogin.initDB(eclDbM.getDbBundle());
            int erg = blTeilnehmerLogin.findeUndPruefeKennung(eclScan.barcode, "", false);
            if (erg < 0) {
                schreibeFehlerInsProtokoll(CaFehler.pfXyNichtVorhanden, "Aktionärsnummer nicht vorhanden");
                 return;
            }
 
            eclLoginDatenM.copyFrom(blTeilnehmerLogin);
            tFunktionen.leseStatusPortal(eclDbM.getDbBundle());
            if (eclBesitzGesamtAuswahl1M.liefereBriefwahlDurchAndereErteilt()==true ||
                    
                    /*TODO ku178: Das funktioniert nicht mehr nach Portal-Briefwahl-Ende!*/
                    eclBesitzGesamtAuswahl1M.liefereBriefwahlNeuMoeglich()==false   ) {
              /*Beiratswahl bereits durchgeführt*/
              schreibeFehlerInsProtokoll(CaFehler.afBeiratswahlSchonAbgegeben,
                      CaFehler.getFehlertext(CaFehler.afBeiratswahlSchonAbgegeben, 0));
              return;
                
            }
            gattung=1;
  
            tWillenserklaerungSession.initZugeordneteMeldungFuerAusfuehrungListe();
            tWillenserklaerungSession.addZugeordneteMeldungFuerAusfuehrungListe(eclBesitzGesamtM.getBesitzJeKennungListe().get(0).eigenerAREintragListe.get(0).zugeordneteMeldungenListe
                    .get(0), null);
            tWillenserklaerungSession.ermittleGattungenFuerZugeordneteMeldungFuerAusfuehrungListe();
            tWillenserklaerungSession.ermittleGattungFuerEinzigenBesitzZugeordneteMeldungFuerAusfuehrungList();

//          aFunktionen.belegeMitErsterMeldung(eclDbM.getDbBundle());
//            if (eclZugeordneteMeldungListeM.isBriefwahlVorhanden()) {
//
//                /*Beiratswahl bereits durchgeführt*/
//                schreibeFehlerInsProtokoll(CaFehler.afBeiratswahlSchonAbgegeben,
//                        CaFehler.getFehlertext(CaFehler.afBeiratswahlSchonAbgegeben, 0));
//                return;
//            }

        }

        /*Sammelkarte einlesen*/
        int lSkIst = 0;
       if (pAusgewaehlteAktion==KonstPortalAktion.BRIEFWAHL_NEU) {
            eclDbM.getDbBundle().dbMeldungen.leseSammelkarteBriefwahlPapier(gattung);
            lSkIst = KonstSkIst.briefwahl;
        } else {
            eclDbM.getDbBundle().dbMeldungen.leseSammelkarteSRVPapier(gattung);
            lSkIst = KonstSkIst.srv;
        }
        int sammelIdent = eclDbM.getDbBundle().dbMeldungen.meldungenArray[0].meldungsIdent;

        /*Abstimmungen zu dieser Gattung einlesen*/
        blMAbstimmung.leseWeisungsliste(tWillenserklaerungSession.getGattungVorhanden(), lSkIst, KonstWeisungserfassungSicht.interneWeisungserfassung);
        blMAbstimmungsvorschlag.leseAbstimmungsvorschlagGesellschaft(eclDbM.getDbBundle());

        CaBug.druckeLog("Standard", logDrucken, 10);
        
        if (!pku178) {
            tWillenserklaerungSession.setAusgewaehlteHauptAktion(KonstPortalAktion.HAUPT_NEUANMELDUNG);
        }
        else {
            tWillenserklaerungSession.setAusgewaehlteHauptAktion(KonstPortalAktion.HAUPT_BEREITSANGEMELDET);
        }
        tWillenserklaerungSession.setAusgewaehlteAktion(pAusgewaehlteAktion);

        tWillenserklaerungSession.setEingabeQuelle(eingabeQuelle);
        tWillenserklaerungSession.setErteiltZeitpunkt(erteiltZeitpunkt);

        tWillenserklaerungSession.setQuelle(eclScan.dateiname);


        int offset = 1;
        for (int i = 0; i < eclAbstimmungenListeM.getAbstimmungenListeM().size(); i++) {
            if (!eclAbstimmungenListeM.getAbstimmungenListeM().get(i).isUeberschrift()) {
                /*J/N/E/U*/
                String scanPos = eclScan.pos[offset];
                String abstimmung = "";
                if (scanPos.equals("1")) {
                    abstimmung = "J";
                }
                if (scanPos.equals("2")) {
                    abstimmung = "N";
                }
                if (scanPos.equals("3")) {
                    abstimmung = "E";
                }
                if (scanPos.equals("???")) {
                    abstimmung = "U";
                }
                eclAbstimmungenListeM.getAbstimmungenListeM().get(i).setGewaehlt(abstimmung);
                offset++;
            }
        }

        /*
        for (i=0;i<eclAbstimmungenListeM.getGegenantraegeListeM().size();i++){
        	String h;
        	h=weWeisungErteilen.getWeisungenGegenantraege().get(i);
        	if (h.compareTo("X")==0){
        		eclAbstimmungenListeM.getGegenantraegeListeM().get(i).setMarkiert(true);}
        	else{
        		eclAbstimmungenListeM.getGegenantraegeListeM().get(i).setMarkiert(false);
         		eclAbstimmungenListeM.getGegenantraegeListeM().get(i).setGewaehlt(weWeisungErteilen.getWeisungenGegenantraege().get(i));
        	}
        }
        */

        tWillenserklaerungSession.setAbweichendeSammelkarte(sammelIdent);
        eclAbstimmungenListeM.setAlternative(1);
        boolean brc=tWeisungBestaetigung.erteilen(false);

        if (brc==false) {
            schreibeFehlerInsProtokoll(tSession.getFehlerCode(), tSession.getFehlerMeldung());
            return;
        } else {
            String quittungsText = "";
            if (pAusgewaehlteAktion==KonstPortalAktion.SRV_NEU) {
                quittungsText = "Scanlauf: Anmeldung und Vollmacht/Weisung an Stimmrechtsvertreter";
            } else {
                quittungsText = "Scanlauf: Anmeldung und Briefwahl";
            }
            if (pku178) {quittungsText="ku178: "+quittungsText;}
           schreibeOkInsProtokoll(quittungsText);
        }
    }


    /*****************************Protokoll-Aufnahme******************************/
    private void schreibeFehlerInsProtokoll(int pFehlerNr, String pFehlerText) {
        EclVerarbeitungsProtokoll eclVerarbeitungsProtokoll = new EclVerarbeitungsProtokoll();

        eclVerarbeitungsProtokoll.mandant = eclParamM.getClGlobalVar().mandant;
        eclVerarbeitungsProtokoll.ident = eclScan.ident;
        eclVerarbeitungsProtokoll.verarbeitungslauf = aktuellerVerarbeitungsLauf.ident;

        eclVerarbeitungsProtokoll.ergebnis = -1;
        eclVerarbeitungsProtokoll.codeFehler = pFehlerNr;
        eclVerarbeitungsProtokoll.textFehler = pFehlerText;

        eclDbM.getDbBundle().dbVerarbeitungsProtokoll.insert(eclVerarbeitungsProtokoll);
    }

    private void schreibeOkInsProtokoll(String pVerarbeitungsText) {
        EclVerarbeitungsProtokoll eclVerarbeitungsProtokoll = new EclVerarbeitungsProtokoll();

        eclVerarbeitungsProtokoll.mandant = eclParamM.getClGlobalVar().mandant;
        eclVerarbeitungsProtokoll.ident = eclScan.ident;
        eclVerarbeitungsProtokoll.verarbeitungslauf = aktuellerVerarbeitungsLauf.ident;

        eclVerarbeitungsProtokoll.ergebnis = 1;
        eclVerarbeitungsProtokoll.textVerarbeitet = pVerarbeitungsText;

        eclDbM.getDbBundle().dbVerarbeitungsProtokoll.insert(eclVerarbeitungsProtokoll);
    }

    /***************Willenserklärungsliste************************/
    private List<Integer> willenserklaerungsListe = null;

    private void initWillenserklaerungsListe() {
        willenserklaerungsListe = new LinkedList<Integer>();
    }

    private void addWillenserklaerungsListe(int pWillenserklaerung) {
        willenserklaerungsListe.add(pWillenserklaerung);
    }

    private boolean willenserklaerungsListeIstEindeutig() {
        if (willenserklaerungsListe.size() > 1) {
            return false;
        }
        return true;
    }

    private boolean inWillenserklaerungsListeEnthalten(int pWillenserklaerung) {
        if (willenserklaerungsListe == null || willenserklaerungsListe.size() == 0) {
            return false;
        }
        for (int i = 0; i < willenserklaerungsListe.size(); i++) {
            if (willenserklaerungsListe.get(i) == pWillenserklaerung) {
                return true;
            }
        }
        return false;
    }

    public int getEingabeQuelle() {
        return eingabeQuelle;
    }

    public void setEingabeQuelle(int eingabeQuelle) {
        this.eingabeQuelle = eingabeQuelle;
    }

    public String getErteiltZeitpunkt() {
        return erteiltZeitpunkt;
    }

    public void setErteiltZeitpunkt(String erteiltZeitpunkt) {
        this.erteiltZeitpunkt = erteiltZeitpunkt;
    }

    public int getRcAktuellerVerarbeitungslauf() {
        return rcAktuellerVerarbeitungslauf;
    }

    public void setRcAktuellerVerarbeitungslauf(int rcAktuellerVerarbeitungslauf) {
        this.rcAktuellerVerarbeitungslauf = rcAktuellerVerarbeitungslauf;
    }

}
