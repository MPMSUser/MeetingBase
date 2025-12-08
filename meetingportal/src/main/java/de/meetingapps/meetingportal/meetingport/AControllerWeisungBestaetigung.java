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
package de.meetingapps.meetingportal.meetingport;

import java.util.List;

import de.meetingapps.meetingportal.meetComAllg.CaBug;
import de.meetingapps.meetingportal.meetComAllg.CaFehler;
import de.meetingapps.meetingportal.meetComBl.BlWillenserklaerung;
import de.meetingapps.meetingportal.meetComEclM.EclAbstimmungM;
import de.meetingapps.meetingportal.meetComEclM.EclAbstimmungenListeM;
import de.meetingapps.meetingportal.meetComEclM.EclDbM;
import de.meetingapps.meetingportal.meetComEclM.EclParamM;
import de.meetingapps.meetingportal.meetComEclM.EclPortalTexteM;
import de.meetingapps.meetingportal.meetComEclM.EclTeilnehmerLoginM;
import de.meetingapps.meetingportal.meetComEclM.EclZugeordneteMeldungM;
import de.meetingapps.meetingportal.meetComEntities.EclAktienregister;
import de.meetingapps.meetingportal.meetComEntities.EclMeldung;
import de.meetingapps.meetingportal.meetComEntities.EclWeisungMeldung;
import de.meetingapps.meetingportal.meetComEntities.EclWeisungMeldungRaw;
import de.meetingapps.meetingportal.meetComKonst.KonstStimmart;
import de.meetingapps.meetingportal.meetComKonst.KonstWillenserklaerung;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@RequestScoped
@Named
@Deprecated
public class AControllerWeisungBestaetigung {

    private int logDrucken = 3;

    @Inject
    EclAbstimmungenListeM eclAbstimmungenListeM;

    @Inject
    private ADlgVariablen aDlgVariablen;
    @Inject
    AFunktionen aFunktionen;
    @Inject
    XSessionVerwaltung xSessionVerwaltung;
    @Inject
    EclPortalTexteM eclPortalTexteM;
    @Inject
    EclParamM eclParamM;
    @Inject
    private EclDbM eclDbM;

    @Inject
    private EclTeilnehmerLoginM eclTeilnehmerLoginM;
    @Inject
    private EclZugeordneteMeldungM eclZugeordneteMeldungM;
    @Inject
    AControllerWeisungBestaetigungSession aControllerWeisungBestaetigungSession;

    /*************************Abspeichern Vollmacht/Weisung an Stimmrechtsvertreter**************************
     * Eingabeparameter:
     * 		ADlgVariablen.ausgewaehlteHauptAktion
     * 
     * 		EclTeilnehmerLoginM.AnmeldeAktionaersnummer (bei Erstanmeldung)
     * 
     * 		EclZugeordneteMeldungM (bei späteren Willenserklärungen)
     * 
     * 		EclAbstimmungenListeM (mit Weisungen) (Alternative 1 oder Alternative 2)
     * 
     * 
     * Rückgabe:
     * 		ADlgVariablen.fehlerMeldung/.fehlerNr
     * 
     * DbBundle wird innerhalb der Funktion geregelt
     * 
     * */
    public String doErteilen() {

        CaBug.druckeLog("AControllerWeisungBestaetigung.doErteilen", logDrucken, 10);

        if (!aFunktionen.pruefeStart("aWeisungBestaetigung")) {
            return "aDlgFehler";
        }
        EclAktienregister aktienregisterEintrag = null;
        int erg = 0;
        int i;

        if (xSessionVerwaltung.getStartPruefen() == 1 && !aDlgVariablen.isBestaetigtDassBerechtigt()
                && ((eclParamM.isCheckboxBeiSRV() && aDlgVariablen.getAusgewaehlteAktion().equals("4"))
                        || (eclParamM.isCheckboxBeiBriefwahl() && aDlgVariablen.getAusgewaehlteAktion().equals("5"))
                        || (eclParamM.isCheckboxBeiKIAV() && aDlgVariablen.getAusgewaehlteAktion().equals("6")))) {
            aDlgVariablen.setFehlerMeldung(eclPortalTexteM.getFehlertext(CaFehler.afBestaetigungBerechtigungFehlt));
            aDlgVariablen.setFehlerNr(CaFehler.afBestaetigungBerechtigungFehlt);
            aFunktionen.setzeEnde();
            return "";
        }

        eclDbM.openAll();

        BlWillenserklaerung lWillenserklaerung = null; /*Wird grundsätzlich nur bei "Erstanmeldung" benötigt - aber 
                                                       dann für Weisungs "If-Zweig" enthalten - deshalb hier
                                                       Definition*/

        aDlgVariablen.setFehlerNr(1);

        if (aDlgVariablen.getAusgewaehlteAktion().compareTo("4") == 0) { /*Vollmacht/Weisung Stimmrechtsvertreter*/
            if (!aFunktionen.pruefeOBWillenserklaerungZulaessig(eclDbM.getDbBundle(),
                    KonstWillenserklaerung.vollmachtUndWeisungAnSRV, aDlgVariablen.getAusgewaehlteHauptAktion())) {
                eclDbM.closeAllAbbruch();
                return aFunktionen.setzeEnde("aTransaktionAbbruch", true, true);
            }
        } else {
            if (!aFunktionen.pruefeOBWillenserklaerungZulaessig(eclDbM.getDbBundle(), KonstWillenserklaerung.briefwahl,
                    aDlgVariablen.getAusgewaehlteHauptAktion())) {
                eclDbM.closeAllAbbruch();
                return aFunktionen.setzeEnde("aTransaktionAbbruch", true, true);
            }

        }

        if (aDlgVariablen.getAusgewaehlteHauptAktion().compareTo("1") == 0) {

            if (aFunktionen.reCheckKeineAktienanmeldungen(eclDbM.getDbBundle()) == false) {
                //	aDlgVariablen.setFehlerMeldung(eclTextePortalM.getFehlertext(CaFehler.afAktienregisterEintragNichtMehrVorhanden));
                aDlgVariablen.setFehlerMeldung("Anderer User aktiv gewesen");
                aDlgVariablen.setFehlerNr(CaFehler.afAndererUserAktiv);
                eclDbM.closeAllAbbruch();
                return aFunktionen.setzeEnde("aDlgAbbruch", true, true);
            }

            /******Anmelden********/
            lWillenserklaerung = new BlWillenserklaerung();
            lWillenserklaerung.pQuelle = aControllerWeisungBestaetigungSession.getQuelle();
            lWillenserklaerung.pErteiltAufWeg = aDlgVariablen.getEingabeQuelle();
            lWillenserklaerung.pErteiltZeitpunkt = aDlgVariablen.getErteiltZeitpunkt();
            /*Aktienregister füllen*/
            aktienregisterEintrag = new EclAktienregister();
            aktienregisterEintrag.aktionaersnummer = eclTeilnehmerLoginM.getAnmeldeAktionaersnummer();
            erg = eclDbM.getDbBundle().dbAktienregister.leseZuAktienregisterEintrag(aktienregisterEintrag);
            if (erg <= 0) {/*Aktienregistereintrag nicht mehr vorhanden*/

                aDlgVariablen.setFehlerMeldung(
                        eclPortalTexteM.getFehlertext(CaFehler.afAktienregisterEintragNichtMehrVorhanden));
                aDlgVariablen.setFehlerNr(CaFehler.afAktienregisterEintragNichtMehrVorhanden);
                eclDbM.closeAllAbbruch();
                aFunktionen.setzeEnde();
                return "";
            }

            aktienregisterEintrag = eclDbM.getDbBundle().dbAktienregister.ergebnisPosition(0);
            lWillenserklaerung.pEclAktienregisterEintrag = aktienregisterEintrag;

            /*Restliche Parameter füllen*/
            lWillenserklaerung.pAktienAnmelden = -1; /*Alle Aktien anmelden*/
            lWillenserklaerung.pAnmeldungFix = false; /*Nicht "Fix" anmelden*/
            if (aDlgVariablen.getAusgewaehlteAktion().compareTo("2") == 0) {
                lWillenserklaerung.pAnzahlAnmeldungen = 2;
            } else {
                lWillenserklaerung.pAnzahlAnmeldungen = 1;

            }
            lWillenserklaerung.pWillenserklaerungGeberIdent = -1; /*Aktionär gibt in diesem Fall*/

            lWillenserklaerung.anmeldungAusAktienregister(eclDbM.getDbBundle());
            if (lWillenserklaerung.rcIstZulaessig == false) {
                aDlgVariablen
                        .setFehlerMeldung(eclPortalTexteM.getFehlertext(lWillenserklaerung.rcGrundFuerUnzulaessig));
                aDlgVariablen.setFehlerNr(lWillenserklaerung.rcGrundFuerUnzulaessig);
                eclDbM.closeAllAbbruch();
                aFunktionen.setzeEnde();
                return "";

            }
        } else {
            if (aFunktionen.reCheckKeineNeueWillenserklaerungen(eclDbM.getDbBundle(),
                    eclZugeordneteMeldungM.getMeldungsIdent(),
                    eclZugeordneteMeldungM.getIdentHoechsteWillenserklaerung()) == false) {
                aDlgVariablen.setFehlerMeldung(eclPortalTexteM.getFehlertext(CaFehler.afAndererUserAktiv));
                aDlgVariablen.setFehlerNr(CaFehler.afAndererUserAktiv);
                eclDbM.closeAllAbbruch();
                return aFunktionen.setzeEnde("aDlgAbbruch", true, true);
            }

        }

        /*in lWillenserklaerung.rcMeldungen[0] steht Anmelde-Ident, die weiterverwendet werden kann*/

        /***Vollmacht/Weisung an Stimmrechstvertreter****/
        BlWillenserklaerung vwWillenserklaerung = new BlWillenserklaerung();
        vwWillenserklaerung.pQuelle = aControllerWeisungBestaetigungSession.getQuelle();
        vwWillenserklaerung.pErteiltAufWeg = aDlgVariablen.getEingabeQuelle();
        vwWillenserklaerung.pErteiltZeitpunkt = aDlgVariablen.getErteiltZeitpunkt();

        if (aDlgVariablen.getAusgewaehlteHauptAktion().compareTo("1") == 0) { /*Erstanmeldung*/
            vwWillenserklaerung.piMeldungsIdentAktionaer = lWillenserklaerung.rcMeldungen[0];
            vwWillenserklaerung.pFolgeFuerWillenserklaerungIdent = lWillenserklaerung.rcWillenserklaerungIdent;
            vwWillenserklaerung.pWillenserklaerungGeberIdent = -1; /*Aktionär*/

        } else {/*Anmeldung bereits früher durchgeführt*/
            vwWillenserklaerung.piMeldungsIdentAktionaer = eclZugeordneteMeldungM.getMeldungsIdent();

            if (eclZugeordneteMeldungM.getArtBeziehung() == 1) {
                vwWillenserklaerung.pWillenserklaerungGeberIdent = -1; /*Aktionär*/
            } else { /*Hier: es handelt sich um eine Untervollmacht. D.h. Geber = angemeldete personNatJur.*/
                vwWillenserklaerung.pWillenserklaerungGeberIdent = eclTeilnehmerLoginM.getAnmeldeIdentPersonenNatJur();
            }
        }

        /*Meldung einlesen, um Gattung zu bestimmen*/
        eclDbM.getDbBundle().dbMeldungen.leseZuIdent(vwWillenserklaerung.piMeldungsIdentAktionaer);
        if (eclDbM.getDbBundle().dbMeldungen.anzErgebnis() < 1) {
            CaBug.drucke("AControllerWeisungBestaetigung.doErteilen 001");
        }
        EclMeldung bearbeiteteMeldung = eclDbM.getDbBundle().dbMeldungen.meldungenArray[0];
        int gattungDerMeldung = bearbeiteteMeldung.liefereGattung();

        /*pAufnahmendeSammelkarteIdent - aufnehmende Sammelkarte*/
        if (aControllerWeisungBestaetigungSession.getAbweichendeSammelkarte() == -1) {
            if (aDlgVariablen.getAusgewaehlteAktion().compareTo("4") == 0) { /*Vollmacht/Weisung Stimmrechtsvertreter*/
                /*Sammelkartennr für Vollmacht/Weisung ermitteln*/
                if (aDlgVariablen.getEingabeQuelle() >= 21 && aDlgVariablen.getEingabeQuelle() <= 29) {
                    eclDbM.getDbBundle().dbMeldungen
                            .leseSammelkarteSRVInternet(gattungDerMeldung/*eclZugeordneteMeldungM.getGattung()*/);
                } else {
                    eclDbM.getDbBundle().dbMeldungen
                            .leseSammelkarteSRVPapier(gattungDerMeldung/*eclZugeordneteMeldungM.getGattung()*/);
                }
                CaBug.druckeLog("AControllerWeisungBestaetigen gattungDerMeldung=" + gattungDerMeldung, logDrucken, 10);
                vwWillenserklaerung.pAufnehmendeSammelkarteIdent = eclDbM
                        .getDbBundle().dbMeldungen.meldungenArray[0].meldungsIdent;
            } else { /*Briefwahl*/
                CaBug.druckeLog("gattungDerMeldung=" + gattungDerMeldung, logDrucken, 10);
                /*Sammelkartennr für Briefwahl ermitteln*/
                if (aDlgVariablen.getEingabeQuelle() >= 21 && aDlgVariablen.getEingabeQuelle() <= 29) {
                    eclDbM.getDbBundle().dbMeldungen
                            .leseSammelkarteBriefwahlInternet(gattungDerMeldung/*eclZugeordneteMeldungM.getGattung()*/);
                } else {
                    eclDbM.getDbBundle().dbMeldungen
                            .leseSammelkarteBriefwahlPapier(gattungDerMeldung/*eclZugeordneteMeldungM.getGattung()*/);
                }
                vwWillenserklaerung.pAufnehmendeSammelkarteIdent = eclDbM
                        .getDbBundle().dbMeldungen.meldungenArray[0].meldungsIdent;
            }
        } else {
            vwWillenserklaerung.pAufnehmendeSammelkarteIdent = aControllerWeisungBestaetigungSession
                    .getAbweichendeSammelkarte();
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
                    //					System.out.println("Abstimmungsgruppe="+gruppe);
                    if (gruppe != 0) {
                        if (lAbstimmungenListe.get(i1).getGewaehlt() != null
                                && lAbstimmungenListe.get(i1).getGewaehlt().compareTo("J") == 0) {
                            //							System.out.println("Gruppe++");
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
                            //					System.out.println("Abstimmungsgruppe="+gruppe);
                            if (gruppe == i2) {
                                lAbstimmungenListe.get(i1).setGewaehlt("U");
                            }
                        }
                    }
                }
            }

            /**Gruppenbearbeitung Ende*/

            for (i = 0; i < lAbstimmungenListe.size(); i++) {

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

            for (i = 0; i < lGegenantraegeListe.size(); i++) {

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
        if (aDlgVariablen.getAusgewaehlteAktion().compareTo("4") == 0) { /*Vollmacht/Weisung Stimmrechtsvertreter*/
            vwWillenserklaerung.vollmachtUndWeisungAnSRV(eclDbM.getDbBundle());
        } else { /*Briefwahl*/
            vwWillenserklaerung.briefwahl(eclDbM.getDbBundle());
        }
        aDlgVariablen.setRcWillenserklaerungIdentAusgefuehrt(vwWillenserklaerung.rcWillenserklaerungIdent);

        /*Falls nicht möglich: Fehlermeldung, zum Anmelden*/
        if (vwWillenserklaerung.rcIstZulaessig == false) {
            aDlgVariablen.setFehlerMeldung(eclPortalTexteM.getFehlertext(vwWillenserklaerung.rcGrundFuerUnzulaessig));
            aDlgVariablen.setFehlerNr(vwWillenserklaerung.rcGrundFuerUnzulaessig);
            eclDbM.closeAllAbbruch();
        }

        if (eclParamM.getParam().paramPortal.quittungDialog == 1) {
            eclDbM.closeAll();
            return aFunktionen.setzeEnde("aWeisungQuittung", true, false);
        } else {
            String naechsteMaske = aFunktionen.waehleAusgangsmaske(eclDbM.getDbBundle());
            eclDbM.closeAll();
            return aFunktionen.setzeEnde(naechsteMaske, true, true);
        }
    }

    /**Ändern von Weisungen (SRV, Briefwahl, KIAV) - tatsächlich durchführen
     * 
     * Eingabeparameter:
     * 		aDlgVariablen.ausgewaehlteAktion
     * 		aDlgVariablen.meldungsIdent
     * 		aDlgVariablen.sammelIdent
     * 		aDlgVariablen.weisungIdent
     * 		aDlgVariablen.willenserklaerungIdent
     * 		eclAbstimmungenListeM
     * 		eclZugeordneteMeldung
     * 		eclTeilnehmerLogin.anmeldeIdentPersonenNatJur
     * 	
     * Ausgabeparameter:
     * 		aDlgVariablen.fehlerMeldung/.fehlerNr
     * Zum Ende hin wird aFunktione.waehleAusgangsmaske aufgerufen, d.h. EclZugeordneteMeldungListeM wird
     * neu ermittelt.
     * 
     * dbBundle wird innerhalb der Funktion gehandled.
     */
    public String doAendernSpeichern() {
        int i;
        CaBug.druckeLog("AControllerWeisungBestaetigung.doAendernSpeichern", logDrucken, 10);
        if (!aFunktionen.pruefeStart("aWeisungBestaetigung")) {
            return "aDlgFehler";
        }

        if (xSessionVerwaltung.getStartPruefen() == 1 && !aDlgVariablen.isBestaetigtDassBerechtigt()
                && ((eclParamM.isCheckboxBeiSRV() && aDlgVariablen.getAusgewaehlteAktion().equals("10"))
                        || (eclParamM.isCheckboxBeiBriefwahl() && aDlgVariablen.getAusgewaehlteAktion().equals("11"))
                        || (eclParamM.isCheckboxBeiKIAV() && aDlgVariablen.getAusgewaehlteAktion().equals("12")))) {
            aDlgVariablen.setFehlerMeldung(eclPortalTexteM.getFehlertext(CaFehler.afBestaetigungBerechtigungFehlt));
            aDlgVariablen.setFehlerNr(CaFehler.afBestaetigungBerechtigungFehlt);
            aFunktionen.setzeEnde();
            return "";
        }

        eclDbM.openAll();
        aDlgVariablen.clearFehlerMeldung();

        BlWillenserklaerung vwWillenserklaerung = new BlWillenserklaerung();
        vwWillenserklaerung.pQuelle = aControllerWeisungBestaetigungSession.getQuelle();
        vwWillenserklaerung.pErteiltAufWeg = aDlgVariablen.getEingabeQuelle();
        vwWillenserklaerung.pErteiltZeitpunkt = aDlgVariablen.getErteiltZeitpunkt();
        vwWillenserklaerung.piMeldungsIdentAktionaer = aDlgVariablen.getMeldungsIdent();

        /*Ändern setzt immer voraus, dass vorher schon der Anmeldeprozess durchgelaufen ist. D.h.: hier ist immer
         * ausgewaehlteHauptaktion=2, d.h. in eclZugeordneteMeldung steht zur Verfügung
         */

        if (aDlgVariablen.getAusgewaehlteAktion().compareTo("10") == 0) { /*Vollmacht/Weisung Stimmrechtsvertreter*/
            if (!aFunktionen.pruefeOBWillenserklaerungZulaessig(eclDbM.getDbBundle(),
                    KonstWillenserklaerung.vollmachtUndWeisungAnSRV, aDlgVariablen.getAusgewaehlteHauptAktion())) {
                eclDbM.closeAllAbbruch();
                return aFunktionen.setzeEnde("aTransaktionAbbruch", true, true);
            }
        }
        if (aDlgVariablen.getAusgewaehlteAktion().compareTo("11") == 0) { /*Briefwahl*/
            if (!aFunktionen.pruefeOBWillenserklaerungZulaessig(eclDbM.getDbBundle(), KonstWillenserklaerung.briefwahl,
                    aDlgVariablen.getAusgewaehlteHauptAktion())) {
                eclDbM.closeAllAbbruch();
                return aFunktionen.setzeEnde("aTransaktionAbbruch", true, true);
            }

        }
        if (aDlgVariablen.getAusgewaehlteAktion().compareTo("12") == 0) { /*KIAV*/
            if (!aFunktionen.pruefeOBWillenserklaerungZulaessig(eclDbM.getDbBundle(),
                    KonstWillenserklaerung.vollmachtUndWeisungAnKIAV, aDlgVariablen.getAusgewaehlteHauptAktion())) {
                eclDbM.closeAllAbbruch();
                return aFunktionen.setzeEnde("aTransaktionAbbruch", true, true);
            }

        }

        if (aFunktionen.reCheckKeineNeueWillenserklaerungen(eclDbM.getDbBundle(),
                eclZugeordneteMeldungM.getMeldungsIdent(),
                eclZugeordneteMeldungM.getIdentHoechsteWillenserklaerung()) == false) {
            aDlgVariablen.setFehlerMeldung(eclPortalTexteM.getFehlertext(CaFehler.afAndererUserAktiv));
            aDlgVariablen.setFehlerNr(CaFehler.afAndererUserAktiv);
            eclDbM.closeAllAbbruch();
            return aFunktionen.setzeEnde("aDlgAbbruch", true, true);
        }

        if (eclZugeordneteMeldungM.getArtBeziehung() == 1) {
            vwWillenserklaerung.pWillenserklaerungGeberIdent = -1; /*Aktionär*/
        } else { /*Hier: es handelt sich um eine Untervollmacht. D.h. Geber = angemeldete personNatJur.*/
            vwWillenserklaerung.pWillenserklaerungGeberIdent = eclTeilnehmerLoginM.getAnmeldeIdentPersonenNatJur();
        }

        vwWillenserklaerung.pAufnehmendeSammelkarteIdent = aDlgVariablen.getSammelIdent();

        /*Abgegebene Weisung (uninterpretiert)
        public EclWeisungMeldungRaw pEclWeisungMeldungRaw=null;*/
        vwWillenserklaerung.pEclWeisungMeldungRaw = new EclWeisungMeldungRaw();
        /*Abgegebene Weisung (interpretiert)
        public EclWeisungMeldung pEclWeisungMeldung=null;*/
        vwWillenserklaerung.pEclWeisungMeldung = new EclWeisungMeldung();
        /*Alte WeisungsIdent, d.h. Ident der Weisung, die geändert wird*/
        vwWillenserklaerung.pEclWeisungMeldung.weisungIdent = aDlgVariablen.getWeisungsIdent();
        /*Alte Willenserklärung, die zu der zu ändernden Weisung gehört*/
        vwWillenserklaerung.pEclWeisungMeldung.willenserklaerungIdent = aDlgVariablen.getWillenserklaerungIdent();

        System.out.println("AControllerWeisungBestaetigung");
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
                    //					System.out.println("Abstimmungsgruppe="+gruppe);
                    if (gruppe != 0) {
                        if (lAbstimmungenListe.get(i1).getGewaehlt() != null
                                && lAbstimmungenListe.get(i1).getGewaehlt().compareTo("J") == 0) {
                            //							System.out.println("Gruppe++");
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
                            //					System.out.println("Abstimmungsgruppe="+gruppe);
                            if (gruppe == i2) {
                                lAbstimmungenListe.get(i1).setGewaehlt("U");
                            }
                        }
                    }

                }
            }

            /**Gruppenbearbeitung Ende*/

            for (i = 0; i < lAbstimmungenListe.size(); i++) {

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

            for (i = 0; i < lGegenantraegeListe.size(); i++) {

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
                //				if (i1<20) {System.out.println("i1="+vwWillenserklaerung.pEclWeisungMeldung.abgabe[i1]);}
                if (vwWillenserklaerung.pEclWeisungMeldung.abgabe[i1] == -999) {
                    vwWillenserklaerung.pEclWeisungMeldung.abgabe[i1] = KonstStimmart.nichtMarkiert;

                }
            }
        }

        /*Sonstige Parameter für Willenserklärung Weisungsänderung speichern*/
        /*Derzeit nicht erforderlich*/
        System.out.println("Nach Alternativen if and else fertig");
        /*Willenserklärung speichern*/
        if (aDlgVariablen.getAusgewaehlteAktion().compareTo("10") == 0) { /*Vollmacht/Weisung Stimmrechtsvertreter*/
            vwWillenserklaerung.aendernWeisungAnSRV(eclDbM.getDbBundle());
        }
        if (aDlgVariablen.getAusgewaehlteAktion().compareTo("11") == 0) { /*Briefwahl*/
            vwWillenserklaerung.aendernBriefwahl(eclDbM.getDbBundle());
        }
        if (aDlgVariablen.getAusgewaehlteAktion().compareTo("12") == 0) { /*KIAV*/
            vwWillenserklaerung.aendernWeisungAnKIAV(eclDbM.getDbBundle());
        }
        if (aDlgVariablen.getAusgewaehlteAktion().compareTo("39") == 0) { /*Dauervollmacht*/
            vwWillenserklaerung.aendernWeisungDauervollmachtAnKIAV(eclDbM.getDbBundle());
        }
        if (aDlgVariablen.getAusgewaehlteAktion().compareTo("40") == 0) { /*Organisatorisch*/
            vwWillenserklaerung.aendernWeisungOrganisatorischInSammelkarte(eclDbM.getDbBundle());
        }

        /*Falls nicht möglich: Fehlermeldung, zum Anmelden*/
        if (vwWillenserklaerung.rcIstZulaessig == false) {
            System.out.println("Fehler=" + eclPortalTexteM.getFehlertext(vwWillenserklaerung.rcGrundFuerUnzulaessig));
            aDlgVariablen.setFehlerMeldung(eclPortalTexteM.getFehlertext(vwWillenserklaerung.rcGrundFuerUnzulaessig));
            aDlgVariablen.setFehlerNr(vwWillenserklaerung.rcGrundFuerUnzulaessig);
            eclDbM.closeAllAbbruch();
            aFunktionen.setzeEnde();
            return "";
        }

        System.out.println("Vor waehleAusgangsmaske");
        /*String returnString=*/aFunktionen.waehleAusgangsmaske(eclDbM.getDbBundle());

        aFunktionen.setzeEnde("aWeisungQuittung", true, false);

        if (eclParamM.getParam().paramPortal.quittungDialog == 1) {
            eclDbM.closeAll();

            aFunktionen.setzeEnde();
            return "aWeisungQuittung";
        } else {
            String naechsteMaske = aFunktionen.waehleAusgangsmaske(eclDbM.getDbBundle());
            eclDbM.closeAll();
            return aFunktionen.setzeEnde(naechsteMaske, true, true);
        }

    }

    public String doZurueck() {
        if (!aFunktionen.pruefeStart("aWeisungBestaetigung")) {
            return "aDlgFehler";
        }
        aDlgVariablen.setBestaetigtDassBerechtigt(false);
        return aFunktionen.setzeEnde("aWeisung", true, false);

    }

    public String doZurueckAendern() {
        if (!aFunktionen.pruefeStart("aWeisungBestaetigung")) {
            return "aDlgFehler";
        }
        aDlgVariablen.setBestaetigtDassBerechtigt(false);
        return aFunktionen.setzeEnde("aWeisungAendern", true, false);
    }

    /**Abmelden*/
    public String doAbmelden() {
        aDlgVariablen.clearLogin();
        return aFunktionen.waehleLogout();
    }

    public String getQuelle() {
        return aControllerWeisungBestaetigungSession.getQuelle();
    }

    public void setQuelle(String quelle) {
        aControllerWeisungBestaetigungSession.setQuelle(quelle);
    }

}
