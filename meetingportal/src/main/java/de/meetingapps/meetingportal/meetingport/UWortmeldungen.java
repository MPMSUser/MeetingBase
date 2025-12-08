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

import java.util.LinkedList;
import java.util.List;

import de.meetingapps.meetingportal.meetComAllg.CaBug;
import de.meetingapps.meetingportal.meetComAllg.CaDatumZeit;
import de.meetingapps.meetingportal.meetComAllg.CaString;
import de.meetingapps.meetingportal.meetComAllg.CaTokenUtil;
import de.meetingapps.meetingportal.meetComBl.BlInfo;
import de.meetingapps.meetingportal.meetComBl.BlMitteilungen;
import de.meetingapps.meetingportal.meetComBl.BlTeilnehmerLoginNeu;
import de.meetingapps.meetingportal.meetComBlManaged.BlMTeilnehmerKommunikation;
import de.meetingapps.meetingportal.meetComEclM.EclAbstimmungSetM;
import de.meetingapps.meetingportal.meetComEclM.EclBesitzGesamtAuswahl1M;
import de.meetingapps.meetingportal.meetComEclM.EclDbM;
import de.meetingapps.meetingportal.meetComEclM.EclLoginDatenM;
import de.meetingapps.meetingportal.meetComEclM.EclParamM;
import de.meetingapps.meetingportal.meetComEclM.EclUserLoginM;
import de.meetingapps.meetingportal.meetComEh.EhJsfButton;
import de.meetingapps.meetingportal.meetComEh.EhJsfSelectItem;
import de.meetingapps.meetingportal.meetComEntities.EclInfo;
import de.meetingapps.meetingportal.meetComEntities.EclMitteilung;
import de.meetingapps.meetingportal.meetComEntities.EclWortmeldetischAktion;
import de.meetingapps.meetingportal.meetComEntities.EclWortmeldetischProtokoll;
import de.meetingapps.meetingportal.meetComEntities.EclWortmeldetischStatusWeiterleitung;
import de.meetingapps.meetingportal.meetComEntities.EclWortmeldetischViewStatus;
import de.meetingapps.meetingportal.meetComKonst.KonstMitteilungStatus;
import de.meetingapps.meetingportal.meetComKonst.KonstPortalFunktionen;
import de.meetingapps.meetingportal.meetComKonst.KonstSkIst;
import de.meetingapps.meetingportal.meetingportTController.TLoginLogoutSession;
import de.meetingapps.meetingportal.meetingportTFunktionen.TFunktionen;
import de.meetingapps.meetingportal.meetingportTFunktionen.TPortalFunktionen;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@RequestScoped
@Named
public class UWortmeldungen {

    private int logDrucken = 10;

    @Inject
    private USession uSession;
    @Inject
    private TFunktionen tFunktionen;
    private @Inject EclAbstimmungSetM eclAbstimmungSetM;
    private @Inject EclParamM eclParamM;

    @Inject
    private UWortmeldungenSession uWortmeldungenSession;

    private @Inject EclDbM eclDbM;
    @Inject
    private EclUserLoginM eclUserLoginM;

    @Inject
    private XSessionVerwaltung xSessionVerwaltung;
    @Inject
    private EclBesitzGesamtAuswahl1M eclBesitzGesamtAuswahl1M;
    @Inject
    private TPortalFunktionen tPortalFunktionen;

    @Inject
    private TLoginLogoutSession tLoginLogoutSession;
    @Inject
    private EclLoginDatenM eclLoginDatenM;

    private @Inject BlMTeilnehmerKommunikation blMTeilnehmerKommunikation;

    public void initialisieren(int pView) {
        eclDbM.openAll();
        eclDbM.openWeitere();
        int skIst = KonstSkIst.liefereSkIstZuportalFunktion(KonstPortalFunktionen.wortmeldungen);

        CaBug.druckeLog("pView=" + pView, logDrucken, 10);
        CaBug.druckeLog(
                "textUeberschrift=" + eclParamM.getParam().paramPortal.wortmeldetischViewArray[pView].textUeberschrift
                        + " eclParamM.getParam().paramPortal.wortmeldetischViewArray[pView].subViewsIdent[0]="
                        + eclParamM.getParam().paramPortal.wortmeldetischViewArray[pView].subViewsIdent[0],
                logDrucken, 10);

        uWortmeldungenSession.setViewIdent(pView);

        BlMitteilungen blMitteilungen = new BlMitteilungen(true, eclDbM.getDbBundle(),
                KonstPortalFunktionen.wortmeldungen);
        blMitteilungen.paramBelegen();
        blMitteilungen.erzeugeVerwaltungWortmeldungen(eclAbstimmungSetM.getAbstimmungSet(), skIst, pView);
        uWortmeldungenSession.setWortmeldetischViewArray(eclParamM.getParam().paramPortal.wortmeldetischViewArray);
        uWortmeldungenSession.copyFrom(blMitteilungen);
        eclDbM.closeAll();
        uWortmeldungenSession.setInBearbeitung(0);
        generateJwtWsToken();

    }

    public String doAktualisierenVersammlungsleitung() {
        if (!xSessionVerwaltung.pruefeStart("uWortmeldungenVersammlungsleitung", "")) {
            xSessionVerwaltung.setzeEnde();
            return "uDlgFehler";
        }

        initialisieren(1);
        return xSessionVerwaltung.setzeUEnde("uWortmeldungenVersammlungsleitung", true, false,
                eclUserLoginM.getKennung());

    }

    public String doAktualisierenView() {
        if (!xSessionVerwaltung.pruefeStart("uWortmeldungenView", "")) {
            xSessionVerwaltung.setzeEnde();
            return "uDlgFehler";
        }

        initialisieren(uWortmeldungenSession.getViewIdent());
        return xSessionVerwaltung.setzeUEnde("uWortmeldungenView", true, false, eclUserLoginM.getKennung());

    }

    /***********************************************Übergreifende FUnktionen für Status-Buttons**********************************/

    private String ggfStatusWeiterleitung(String pAktuellerView, String pNeuerStatus, int pTestStatus) {
        CaBug.druckeLog(
                "pAktuellerView=" + pAktuellerView + " pNeuerStatus=" + pNeuerStatus + " pTestStatus=" + pTestStatus,
                logDrucken, 10);
        String neuerStatus = pNeuerStatus;
        int anzWeiterleitungen = eclParamM.getParam().paramPortal.wortmeldetischStatusWeiterleitung.size();
        boolean weiterLeitungDurchgefuehrt = false;
        for (int i = 0; i < anzWeiterleitungen; i++) {
            EclWortmeldetischStatusWeiterleitung lWortmeldetischStatusWeiterleitung = eclParamM
                    .getParam().paramPortal.wortmeldetischStatusWeiterleitung.get(i);
            if ((lWortmeldetischStatusWeiterleitung.viewBezeichnung.isEmpty()
                    || lWortmeldetischStatusWeiterleitung.viewBezeichnung.equals(pAktuellerView))
                    && lWortmeldetischStatusWeiterleitung.ursprungsStatusBezeichnung.equals(pNeuerStatus)) {
                switch (lWortmeldetischStatusWeiterleitung.nurWennBedingungErfuellt) {
                case "KEINEN_TEST_DURCHFUEHREN":
                    if (eclParamM.getParam().paramPortal.wortmeldungTestDurchfuehren == 0) {
                        neuerStatus = lWortmeldetischStatusWeiterleitung.folgeStatusBezeichnung;
                        weiterLeitungDurchgefuehrt = true;
                    }
                    break;
                case "AUTOMATISCH_IN_REDNERLISTE":
                    if (eclParamM.getParam().paramPortal.wortmeldungNachTestManuellInRednerlisteAufnehmen == 0) {
                        neuerStatus = lWortmeldetischStatusWeiterleitung.folgeStatusBezeichnung;
                        weiterLeitungDurchgefuehrt = true;
                    }
                    break;
                case "BEREITS_GETESTET":
                    CaBug.druckeLog("BEREITS_GETESTET", logDrucken, 10);
                    if (pTestStatus == 1) {
                        neuerStatus = lWortmeldetischStatusWeiterleitung.folgeStatusBezeichnung;
                        weiterLeitungDurchgefuehrt = true;
                    }
                    break;
                }
            }
        }

        if (weiterLeitungDurchgefuehrt) {
            neuerStatus = ggfStatusWeiterleitung(pAktuellerView, neuerStatus, pTestStatus);
        }
        return neuerStatus;
    }

    private String ausfuehrenDoSetzeStatus(EclMitteilung pWortmeldung, String pNeuerStatus, int pTestRaumVonButton,
            int pRedeRaumVonButton) {
        CaBug.druckeLog("pNeuerStatus=" + pNeuerStatus, logDrucken, 10);
        String aktuelleMaske = xSessionVerwaltung.getAktuelleMaske();
        if (!xSessionVerwaltung.pruefeStart(new String[] { "uWortmeldungen", "uWortmeldungenView" }, "")) {
            xSessionVerwaltung.setzeEnde();
            return "uDlgFehler";
        }

        int lAlterStatus = KonstMitteilungStatus.liefereNurStatus(pWortmeldung.status);
        String alterStatus = eclParamM.getParam().paramPortal.wortmeldetischStatusArray[lAlterStatus].statusBezeichnung;

        String aktuellerView = eclParamM.getParam().paramPortal.wortmeldetischViewArray[uWortmeldungenSession
                .getViewIdent()].viewBezeichnung;
        eclDbM.openAll();
        eclDbM.openWeitere();

        /*Teststatus aus loginDaten ermitteln*/
        int loginIdent = pWortmeldung.loginIdent;
        eclDbM.getDbBundle().dbLoginDaten.read_ident(loginIdent);
        int testStatus = eclDbM.getDbBundle().dbLoginDaten.ergebnisPosition(0).konferenzTestDurchgefuehrt;

        /*Hier ggf. automatische Statutsweiterschaltungen, z.B. bei WARTEND_FUER_AUFNAHMEN_IN_REDNERLISTE, TEST_VERSUCH*/
        pNeuerStatus = ggfStatusWeiterleitung(aktuellerView, pNeuerStatus, testStatus);
        CaBug.druckeLog("pNeuerStatus nach Weiterleitung=" + pNeuerStatus, logDrucken, 10);

        int neuerStatusOffset = -1;
        int neuerStatusNr = -1;
        int statusanz = eclParamM.getParam().paramPortal.wortmeldetischStatusArray.length;
        for (int i = 0; i < statusanz; i++) {
            if (eclParamM.getParam().paramPortal.wortmeldetischStatusArray[i].statusBezeichnung.equals(pNeuerStatus)) {
                neuerStatusOffset = i;
                neuerStatusNr = eclParamM.getParam().paramPortal.wortmeldetischStatusArray[i].statusIdent;
            }
        }

        int stationsNr = liefereStationsNr(neuerStatusOffset, pTestRaumVonButton, pRedeRaumVonButton);
        if (stationsNr == -1) {
            eclDbM.closeAll();
            return xSessionVerwaltung.setzeUEnde(aktuelleMaske, false, false, eclUserLoginM.getKennung());
        }

        eclDbM.getDbBundle().dbMitteilung.setzeFunktion(KonstPortalFunktionen.wortmeldungen);
        int rc = 0;
        int lfdNrInListe = pWortmeldung.lfdNrInListe;
        if (!pNeuerStatus.startsWith("UNVERAENDERT_")) {/*Dann neuen Status nicht zurückspeichern!*/
            if (eclParamM.getParam().paramPortal.wortmeldetischStatusArray[neuerStatusOffset].beiEintrittAnsEnde == 1
                    || pWortmeldung.lfdNrInListe == 0) {
                rc = eclDbM.getDbBundle().dbMitteilung.update_statusUndAnsEnde(pWortmeldung.mitteilungIdent,
                        pWortmeldung.db_version, neuerStatusNr, stationsNr);
                if (rc > 0) {
                    lfdNrInListe = rc;
                }
            } else {
                rc = eclDbM.getDbBundle().dbMitteilung.update_status(pWortmeldung.mitteilungIdent,
                        pWortmeldung.db_version, neuerStatusNr, stationsNr);
            }
            if (rc < 1) {
                eclDbM.closeAll();
                uSession.setFehlermeldung(
                        "Bereits von anderem Benutzer verändert - bitte aktualisieren und Bearbeitung neu starten");
                return xSessionVerwaltung.setzeUEnde(aktuelleMaske, false, false, eclUserLoginM.getKennung());
            }
        }

        CaBug.druckeLog("AA", logDrucken, 10);

        ausfuehrenAktionenBeiStatusAenderung(pWortmeldung, alterStatus, pNeuerStatus, stationsNr);
        CaBug.druckeLog("AB", logDrucken, 10);

        EclWortmeldetischProtokoll lWortmeldetischProtokoll = new EclWortmeldetischProtokoll();
        lWortmeldetischProtokoll.identWortmeldung = pWortmeldung.mitteilungIdent;
        lWortmeldetischProtokoll.datumZeit = CaDatumZeit.DatumZeitStringFuerDatenbank();
        lWortmeldetischProtokoll.alterStatus = alterStatus;
        lWortmeldetischProtokoll.neuerStatus = pNeuerStatus;
        lWortmeldetischProtokoll.sonstigeAktion = 1;
        lWortmeldetischProtokoll.raumNr = stationsNr;
        lWortmeldetischProtokoll.lfdNrInListe = lfdNrInListe;
        eclDbM.getDbBundle().dbWortmeldetischProtokoll.insert(lWortmeldetischProtokoll);

        eclDbM.closeAll();
        CaBug.druckeLog("AC", logDrucken, 10);

        initialisieren(uWortmeldungenSession.getViewIdent());
        CaBug.druckeLog("AD", logDrucken, 10);
        return xSessionVerwaltung.setzeUEnde(aktuelleMaske, true, false, eclUserLoginM.getKennung());
    }
    
    private int liefereStationsNr(int pNeuerStatusOffset, int pTestRaumVonButton, int pRedeRaumVonButton) {
        int stationsNr = 0;
        if (eclParamM.getParam().paramPortal.wortmeldetischStatusArray[pNeuerStatusOffset].testraumErgaenzen == 1) {
            if (pTestRaumVonButton != 0) {
                return pTestRaumVonButton;
            }
            stationsNr = liefereTestNr();
            if (stationsNr < 1) {
                uSession.setFehlermeldung("Bitte Test-Stations-Nr überprüfen");
                return -1;
            }
        }
        if (eclParamM.getParam().paramPortal.wortmeldetischStatusArray[pNeuerStatusOffset].rederaumErgaenzen == 1) {
            if (pRedeRaumVonButton != 0) {
                return pRedeRaumVonButton;
            }
            stationsNr = liefereTelefonieNr();
            if (stationsNr < 1) {
                uSession.setFehlermeldung("Bitte Rede-Stations-Nr überprüfen");
                return -1;
            }
        }
        return stationsNr;

    }

    /**rc==11 => Versammlungsleiter wurde aktualisiert*/
    private int ausfuehrenAktionenBeiStatusAenderung(EclMitteilung pWortmeldung, String pAlterStatus,
            String pNeuerStatus, int telefonieNr) {
        CaBug.druckeLog("pAlterStatus=" + pAlterStatus + " pNeuerStatus=" + pNeuerStatus, logDrucken, 10);
        if (pAlterStatus == pNeuerStatus) {
            CaBug.druckeLog("0", logDrucken, 10);
            return 0;
        }

        int vlAktualisiert = 0;
        int anzAktionen = eclParamM.getParam().paramPortal.wortmeldetischAktion.size();
        String aktuellerView = eclParamM.getParam().paramPortal.wortmeldetischViewArray[uWortmeldungenSession
                .getViewIdent()].viewBezeichnung;
        for (int i = 0; i < anzAktionen; i++) {
            EclWortmeldetischAktion lWortmeldetischAktion = eclParamM.getParam().paramPortal.wortmeldetischAktion
                    .get(i);
            if (lWortmeldetischAktion.viewBezeichnung.isEmpty()
                    || lWortmeldetischAktion.viewBezeichnung.equals(aktuellerView)) {
                if ((lWortmeldetischAktion.vonStatusBezeichnung.isEmpty()
                        || lWortmeldetischAktion.vonStatusBezeichnung.equals(pAlterStatus))
                        && (lWortmeldetischAktion.zuStatusBezeichnung.isEmpty()
                                || lWortmeldetischAktion.zuStatusBezeichnung.equals(pNeuerStatus))) {
                    /*Bedingung ist erfüllt, nun Aktion ausführen*/
                    switch (lWortmeldetischAktion.aktionBezeichnung) {
                    case "SETZE_TESTSTATUS_TEILNEHMER_ERFOLGREICH":
                        setzeTestStatusInEclLoginDaten(pWortmeldung, 1);
                        break;
                    case "SETZE_TESTSTATUS_TEILNEHMER_NICHT_ERFOLGREICH":
                        setzeTestStatusInEclLoginDaten(pWortmeldung, -1);
                        break;
                    case "FORDERE_AUF_IN_TESTRAUM_KOMMEN":
                        fordereAufInTestraumKommen(pWortmeldung, telefonieNr);
                        break;
                    case "FORDERE_LINK_FUER_TESTRAUM_AN":
                        // fordereLinkFuerTestraumAn(pWortmeldung, telefonieNr);
                        break;
                    case "FORDERE_AUF_TESTRAUM_VERLASSEN":
                        fordereAufTestVerlassen(pWortmeldung, telefonieNr);
                        break;
                    case "FORDERE_AUF_IN_REDERAUM_KOMMEN":
                        fordereAufInRederaumKommen(pWortmeldung, telefonieNr);
                        break;
                    case "FORDERE_LINK_FUER_REDERAUM_AN":
                        // fordereLinkFuerRederaumAn(pWortmeldung, telefonieNr);
                        break;
                    case "FORDERE_AUF_REDERAUM_VERLASSEN":
                        fordereAufRederaumVerlassen(pWortmeldung, telefonieNr);
                        break;
                    case "FORDERE_AUF_SPRECHEN":
                        fordereAufSprechen(pWortmeldung, telefonieNr);
                        break;
                    case "FORDERE_AUF_TESTRAUM_VERLASSEN_NICHT_OK":
                        fordereAufTestVerlassenNichtOK(pWortmeldung, telefonieNr);
                        break;
                    case "AKTUALISIERE_VL_VIEW":
                        aktualisiereVL();
                        vlAktualisiert = 11;
                        break;
                    case "FORDERE_AUF_TEST_NICHT_ERREICHT":
                        fordereAufTestNichtErreicht(pWortmeldung, telefonieNr);
                        break;
                    case "FORDERE_AUF_REDE_NICHT_ERREICHT":
                        fordereAufRedeNichtErreicht(pWortmeldung, telefonieNr);
                        break;  
                    }
                }
            }

        }
        CaBug.druckeLog("E", logDrucken, 10);
        return vlAktualisiert;
    }

    /**pNeuerTeststatus = -1, 0, 1 - siehe EclLogindaten.konferenzTestDurchgefuehrt*/
    private void setzeTestStatusInEclLoginDaten(EclMitteilung pWortmeldung, int pNeuerTeststatus) {
        int loginIdent = pWortmeldung.loginIdent;
        CaBug.druckeLog("loginIdent=" + loginIdent + " pNeuerTeststatus=" + pNeuerTeststatus, logDrucken, 10);
        eclDbM.getDbBundle().dbLoginDaten.updateKonferenzTestDurchgefuehrt(loginIdent, pNeuerTeststatus);

    }

    private void fordereAufInTestraumKommen(EclMitteilung pWortmeldung, int pRaumNr) {
        int loginIdent = pWortmeldung.loginIdent;
        int neuerStatus = pRaumNr + 256;
        eclDbM.getDbBundle().dbLoginDaten.updateKonferenzTestAblauf(loginIdent, neuerStatus);

        String loginKennung = leseLoginKennung(loginIdent);
        String mandant = eclParamM.getMandantString();
        /*******Aufrufen*****************/
        blMTeilnehmerKommunikation.wortmeldungAktionKommeInTestraum(loginKennung, mandant);

    }

    private void fordereAufTestVerlassen(EclMitteilung pWortmeldung, int pRaumNr) {
        int loginIdent = pWortmeldung.loginIdent;
        int neuerStatus = pRaumNr + 2048;
        eclDbM.getDbBundle().dbLoginDaten.updateKonferenzTestAblauf(loginIdent, neuerStatus);

        String loginKennung = leseLoginKennung(loginIdent);
        String mandant = eclParamM.getMandantString();
        /*******Aufrufen*****************/
        blMTeilnehmerKommunikation.wortmeldungAktionVerlasseTestraum(loginKennung, mandant);
    }

    private void fordereAufInRederaumKommen(EclMitteilung pWortmeldung, int pRaumNr) {
        int loginIdent = pWortmeldung.loginIdent;
        int neuerStatus = pRaumNr + 256;
        eclDbM.getDbBundle().dbLoginDaten.updateKonferenzSprechen(loginIdent, neuerStatus);

        String loginKennung = leseLoginKennung(loginIdent);
        String mandant = eclParamM.getMandantString();
        /*******Aufrufen*****************/
        blMTeilnehmerKommunikation.wortmeldungAktionKommeInRederaum(loginKennung, mandant);
    }

    private void fordereAufRederaumVerlassen(EclMitteilung pWortmeldung, int pRaumNr) {
        int loginIdent = pWortmeldung.loginIdent;
        int neuerStatus = pRaumNr + 2048;
        eclDbM.getDbBundle().dbLoginDaten.updateKonferenzSprechen(loginIdent, neuerStatus);

        String loginKennung = leseLoginKennung(loginIdent);
        String mandant = eclParamM.getMandantString();
        /*******Aufrufen*****************/
        blMTeilnehmerKommunikation.wortmeldungAktionVerlasseRederaum(loginKennung, mandant);
    }

    private void fordereAufSprechen(EclMitteilung pWortmeldung, int pRaumNr) {
        int loginIdent = pWortmeldung.loginIdent;
        int neuerStatus = pRaumNr + 512;
        eclDbM.getDbBundle().dbLoginDaten.updateKonferenzSprechen(loginIdent, neuerStatus);

        String loginKennung = leseLoginKennung(loginIdent);
        String mandant = eclParamM.getMandantString();
        /*******Aufrufen*****************/
        blMTeilnehmerKommunikation.wortmeldungAktionJetztSprechen(loginKennung, mandant);
    }

    private void fordereAufTestVerlassenNichtOK(EclMitteilung pWortmeldung, int pRaumNr) {
        int loginIdent = pWortmeldung.loginIdent;
        int neuerStatus = pRaumNr + 2048;
        eclDbM.getDbBundle().dbLoginDaten.updateKonferenzTestAblauf(loginIdent, neuerStatus);

        String loginKennung = leseLoginKennung(loginIdent);
        String mandant = eclParamM.getMandantString();
        /*******Aufrufen*****************/
        blMTeilnehmerKommunikation.wortmeldungAktionVerlasseTestraumNichtOK(loginKennung, mandant);
    }

    private void aktualisiereVL() {
        String loginKennung = "vl";
        String mandant = eclParamM.getMandantString();
        /*******Aufrufen*****************/
        blMTeilnehmerKommunikation.wortmeldungAktionAktualisiereVL(loginKennung, mandant);
    }

    private void fordereAufTestNichtErreicht(EclMitteilung pWortmeldung, int pRaumNr) {
        int loginIdent = pWortmeldung.loginIdent;
        int neuerStatus = pRaumNr + 2048;
        eclDbM.getDbBundle().dbLoginDaten.updateKonferenzTestAblauf(loginIdent, neuerStatus);

        String loginKennung = leseLoginKennung(loginIdent);
        String mandant = eclParamM.getMandantString();
        /*******Aufrufen*****************/
        blMTeilnehmerKommunikation.wortmeldungAktionTestNichtErreicht(loginKennung, mandant);
    }

    private void fordereAufRedeNichtErreicht(EclMitteilung pWortmeldung, int pRaumNr) {
        int loginIdent = pWortmeldung.loginIdent;
        int neuerStatus = pRaumNr + 2048;
        eclDbM.getDbBundle().dbLoginDaten.updateKonferenzSprechen(loginIdent, neuerStatus);

        String loginKennung = leseLoginKennung(loginIdent);
        String mandant = eclParamM.getMandantString();
        /*******Aufrufen*****************/
        blMTeilnehmerKommunikation.wortmeldungAktionRedeNichtErreicht(loginKennung, mandant);
    }

    private String leseLoginKennung(int pLoginIdent) {
        eclDbM.getDbBundle().dbLoginDaten.read_ident(pLoginIdent);
        String loginKennung = eclDbM.getDbBundle().dbLoginDaten.ergebnisPosition(0).loginKennung;
        return loginKennung;
    }

    /**Status-Buttons*/
    public String doButtonStatusAenderung(EclMitteilung pWortmeldung, EhJsfButton pButton) {
        return ausfuehrenDoSetzeStatus(pWortmeldung, pButton.folgeStatusBezeichnung, pButton.testRaumNr,
                pButton.redeRaumNr);
    }

    public String doRednerpositionVeraendern(EclMitteilung pWortmeldung) {
        String rcString = doBearbeiten(pWortmeldung);
        uWortmeldungenSession.setInBearbeitung(5);
        return rcString;
    }

    public String doRednernameVeraendern(EclMitteilung pWortmeldung) {
        String rcString = doBearbeiten(pWortmeldung);
        uWortmeldungenSession.setInBearbeitung(6);
        return rcString;
    }

    public String doKommentarIntern(EclMitteilung pWortmeldung) {
        String rcString = doBearbeiten(pWortmeldung);
        uWortmeldungenSession.setInBearbeitung(2);
        return rcString;
    }

    public String doKommentarVersammlungsleiter(EclMitteilung pWortmeldung) {
        String rcString = doBearbeiten(pWortmeldung);
        uWortmeldungenSession.setInBearbeitung(3);
        return rcString;
    }

    public String doNachrichtAnVersammlungsleiter() {
        if (!xSessionVerwaltung.pruefeStart("uWortmeldungenView", "")) {
            xSessionVerwaltung.setzeEnde();
            return "uDlgFehler";
        }
        eclDbM.openAll();
        eclDbM.openWeitere();
        BlInfo blInfo = new BlInfo(eclDbM.getDbBundle());
        EclInfo lInfo = blInfo.liefereNachrichtFuerVersamnmlungsleiter();
        eclDbM.closeAll();
        uWortmeldungenSession.setInBearbeitung(4);
        uWortmeldungenSession.setbInfo(lInfo);
        return xSessionVerwaltung.setzeUEnde("uWortmeldungenView", true, false, eclUserLoginM.getKennung());
    }

    public String doBearbeiten(EclMitteilung pWortmeldung) {
        if (!xSessionVerwaltung.pruefeStart("uWortmeldungenView", "")) {
            xSessionVerwaltung.setzeEnde();
            return "uDlgFehler";
        }
        eclDbM.openAll();
        eclDbM.openWeitere();

        /**Liste für select-Items bei "Status-Bearbeiten"*/
        List<EhJsfSelectItem> bearbeitenSelectItemListe = new LinkedList<EhJsfSelectItem>();

        int anzahlStatusseInDetailView = eclParamM.getParam().paramPortal.wortmeldetischViewArray[2].statusArray.length;
        for (int i = 0; i < anzahlStatusseInDetailView; i++) {
            EclWortmeldetischViewStatus lWortmeldetischViewStatus = eclParamM
                    .getParam().paramPortal.wortmeldetischViewArray[2].statusArray[i];
            if (lWortmeldetischViewStatus.anzeigeDesStatusInDieserView == 1) {
                bearbeitenSelectItemListe
                        .add(new EhJsfSelectItem(lWortmeldetischViewStatus.anzeigeTextInDieserView, i));
            }
        }

        uWortmeldungenSession.setBearbeitenSelectItemListe(bearbeitenSelectItemListe);

        /**Neue LfdNr mit Blank vorbelegen - wäre dann unverändert*/
        uWortmeldungenSession.setNeueLfdNr("");

        eclDbM.getDbBundle().dbMitteilung.setzeFunktion(KonstPortalFunktionen.wortmeldungen);
        eclDbM.getDbBundle().dbMitteilung.read(pWortmeldung.mitteilungIdent);
        EclMitteilung lWortmeldung = eclDbM.getDbBundle().dbMitteilung.ergebnisPosition(0);
        BlMitteilungen blMitteilungen = new BlMitteilungen(true, eclDbM.getDbBundle(),
                KonstPortalFunktionen.wortmeldungen);
        blMitteilungen.ergaenzeMitteilung(lWortmeldung);

        uWortmeldungenSession.setAktuellerStatus(eclParamM
                .getParam().paramPortal.wortmeldetischViewArray[2].statusArray[lWortmeldung.status].anzeigeTextInDieserView);
        CaBug.druckeLog("uWortmeldungenSession.getAktuellerStatus()=" + uWortmeldungenSession.getAktuellerStatus(),
                logDrucken, 10);
        String lLoginKennung = lWortmeldung.loginKennung;
        BlTeilnehmerLoginNeu blTeilnehmerLogin = new BlTeilnehmerLoginNeu();
        blTeilnehmerLogin.initDB(eclDbM.getDbBundle());
        int erg = blTeilnehmerLogin.findeUndPruefeKennung(lLoginKennung, "", false);
        if (erg < 0) {
            eclDbM.closeAll();
            uSession.setFehlermeldung("Systemfehler");
            return xSessionVerwaltung.setzeUEnde("uWortmeldungenView", false, false, eclUserLoginM.getKennung());
        }

        tLoginLogoutSession.clearFehler();
        eclLoginDatenM.copyFrom(blTeilnehmerLogin);
        tPortalFunktionen.belegePortalFunktionenWillenserklaerungenStatusAktiv();
        if (eclParamM.getParam().paramPortal.varianteDialogablauf == 1) {
            tFunktionen.leseStatusAuswahl1(eclDbM.getDbBundle());

            if (eclBesitzGesamtAuswahl1M.getAnzPraesenteVorhanden() > 0) {
                uWortmeldungenSession.setPraesenteVorhanden(true);
            } else {
                uWortmeldungenSession.setPraesenteVorhanden(false);
            }
        } else {
            eclDbM.getDbBundle().dbMeldungVirtuellePraesenz.read_zuLoginIdent(eclLoginDatenM.getEclLoginDaten().ident);
            uWortmeldungenSession.setPraesenteVorhanden(false);
            int anz = eclDbM.getDbBundle().dbMeldungVirtuellePraesenz.anzErgebnis();
            for (int i = 0; i < anz; i++) {
                if (eclDbM.getDbBundle().dbMeldungVirtuellePraesenz.ergebnisPosition(i).statusPraesenz == 1) {
                    uWortmeldungenSession.setPraesenteVorhanden(true);
                }
            }
        }

        eclDbM.closeAll();
        uWortmeldungenSession.setInBearbeitung(1);
        uWortmeldungenSession.setbWortmeldung(lWortmeldung);
        uWortmeldungenSession.setNeuerStatus(Integer.toString(lWortmeldung.status));
        return xSessionVerwaltung.setzeUEnde("uWortmeldungenView", true, false, eclUserLoginM.getKennung());
    }

    public String doAbbrechen() {
        if (!xSessionVerwaltung.pruefeStart("uWortmeldungenView", "")) {
            xSessionVerwaltung.setzeEnde();
            return "uDlgFehler";
        }
        uWortmeldungenSession.setInBearbeitung(0);
        return xSessionVerwaltung.setzeUEnde("uWortmeldungenView", true, false, eclUserLoginM.getKennung());
    }

    /**Ausgewertet wird auch bearbeitungsstatus - 1, 2, 3, 5, 6*/
    private String speichernAusfuehren(boolean pAnsEnde) {
        if (!xSessionVerwaltung.pruefeStart("uWortmeldungenView", "")) {
            xSessionVerwaltung.setzeEnde();
            return "uDlgFehler";
        }

        int bearbeiten = uWortmeldungenSession.getInBearbeitung();

        int neueLfdNr = 0;
        if (bearbeiten == 1 || bearbeiten == 5) {
            String hNeueLfdNr = uWortmeldungenSession.getNeueLfdNr().trim();
            if (!hNeueLfdNr.isEmpty()) {
                if (!CaString.isNummern(hNeueLfdNr)) {
                    uSession.setFehlermeldung("Bitte eine gültige neue laufende Nummer eingeben (oder leer lassen)");
                    return xSessionVerwaltung.setzeUEnde("uWortmeldungenView", false, false,
                            eclUserLoginM.getKennung());
                }
                neueLfdNr = Integer.parseInt(hNeueLfdNr);
            }
        }

        int neuerStatus = 0;
        if (bearbeiten == 1) {
            String hNeuerStatus = uWortmeldungenSession.getNeuerStatus();
            if (hNeuerStatus == null) {
                neuerStatus = uWortmeldungenSession.getbWortmeldung().status;
            } else {
                neuerStatus = Integer.parseInt(hNeuerStatus);
            }
        }

        if (bearbeiten == 1) {
            if (neueLfdNr != 0 && pAnsEnde) {
                uSession.setFehlermeldung("neue lfd Nr und ans Ende gleichzeitig nicht zulässig");
                return xSessionVerwaltung.setzeUEnde("uWortmeldungenView", false, false, eclUserLoginM.getKennung());
            }
        }

        EclMitteilung lMitteilung = uWortmeldungenSession.getbWortmeldung();
        if (!pAnsEnde && (bearbeiten == 1 || bearbeiten == 5)) {
            if (lMitteilung.lfdNrInListe == 0 && neueLfdNr == 0) {
                uSession.setFehlermeldung(
                        "Status ohne LfdNr nicht zulässig - entweder ans Ende setzen oder neue lfd.Nr. vergeben");
                return xSessionVerwaltung.setzeUEnde("uWortmeldungenView", false, false, eclUserLoginM.getKennung());
            }
        }

        int alterStatus = KonstMitteilungStatus.liefereNurStatus(lMitteilung.status);

        eclDbM.openAll();
        eclDbM.openWeitere();

        eclDbM.getDbBundle().dbMitteilung.setzeFunktion(KonstPortalFunktionen.wortmeldungen);

        int stationsNr = 0;

        if (bearbeiten == 1) {
            liefereStationsNr(neuerStatus, 0, 0);
            if (neuerStatus != alterStatus) {
                if (stationsNr == -1) {
                    eclDbM.closeAll();
                    return xSessionVerwaltung.setzeUEnde("uWortmeldungenView", false, false,
                            eclUserLoginM.getKennung());
                }
            }
        }

        if (bearbeiten == 1 || bearbeiten == 5) {
            if (neueLfdNr != 0) {
                eclDbM.getDbBundle().dbMitteilung.update_LfdNr(neueLfdNr);
                lMitteilung.lfdNrInListe = neueLfdNr;
                lMitteilung.db_version++; //MUß hier manuell hochgezählt werden, da im Update_LfdNr verändert
            }
        }

        if (bearbeiten == 1) {
            lMitteilung.status = neuerStatus;

            if (neuerStatus != alterStatus) {
                lMitteilung.raumNr = stationsNr;
            }
        }

        int rc = 0;
        if (!pAnsEnde) {
            rc = eclDbM.getDbBundle().dbMitteilung.update(lMitteilung);
        } else {
            rc = eclDbM.getDbBundle().dbMitteilung.updateUndAnsEnde(lMitteilung);
        }

        if (rc != 1) {
            eclDbM.closeAll();
            uSession.setFehlermeldung(
                    "Bereits von anderem Benutzer verändert - bitte aktualisieren und Bearbeitung neu starten");
            uWortmeldungenSession.setInBearbeitung(0);
            return xSessionVerwaltung.setzeUEnde("uWortmeldungenView", false, false, eclUserLoginM.getKennung());
        }

        int rcVL = 0;
        if (bearbeiten == 1) {
            rcVL = ausfuehrenAktionenBeiStatusAenderung(lMitteilung,
                    eclParamM.getParam().paramPortal.wortmeldetischStatusArray[alterStatus].statusBezeichnung,
                    eclParamM.getParam().paramPortal.wortmeldetischStatusArray[neuerStatus].statusBezeichnung,
                    stationsNr);
        }
        if (rcVL != 11) {
            aktualisiereVL();
        }

        EclWortmeldetischProtokoll lWortmeldetischProtokoll = new EclWortmeldetischProtokoll();
        lWortmeldetischProtokoll.identWortmeldung = lMitteilung.mitteilungIdent;
        lWortmeldetischProtokoll.datumZeit = CaDatumZeit.DatumZeitStringFuerDatenbank();
        lWortmeldetischProtokoll.alterStatus = eclParamM
                .getParam().paramPortal.wortmeldetischStatusArray[alterStatus].statusBezeichnung;
        lWortmeldetischProtokoll.neuerStatus = eclParamM
                .getParam().paramPortal.wortmeldetischStatusArray[neuerStatus].statusBezeichnung;
        ;
        lWortmeldetischProtokoll.sonstigeAktion = 2;
        lWortmeldetischProtokoll.raumNr = lMitteilung.raumNr;
        lWortmeldetischProtokoll.lfdNrInListe = lMitteilung.lfdNrInListe;
        lWortmeldetischProtokoll.kommentarIntern = lMitteilung.kommentarIntern;
        lWortmeldetischProtokoll.kommentarVersammlungsleiter = lMitteilung.kommentarVersammlungsleiter;
        eclDbM.getDbBundle().dbWortmeldetischProtokoll.insert(lWortmeldetischProtokoll);

        eclDbM.closeAll();
        uWortmeldungenSession.setInBearbeitung(0);
        initialisieren(uWortmeldungenSession.getViewIdent());
        return xSessionVerwaltung.setzeUEnde("uWortmeldungenView", true, false, eclUserLoginM.getKennung());
    }

    public String doSpeichern() {
        return speichernAusfuehren(false);
    }

    public String doSpeichernAnsEnde() {
        return speichernAusfuehren(true);
    }

    public String doSpeichernKommentarIntern() {
        return speichernAusfuehren(false);
    }

    public String doSpeichernRednerpositionVeraendern() {
        /*AAAAA*/
        return speichernAusfuehren(false);
    }

    public String doSpeichernRednernameVeraendern() {
        /*AAAAA*/
        return speichernAusfuehren(false);
    }

    public String doSpeichernKommentarVersammlungsleiter() {
        return speichernAusfuehren(false);
    }

    public String doSpeichernNachrichtAnVersammlungsleiter() {
        if (!xSessionVerwaltung.pruefeStart("uWortmeldungenView", "")) {
            xSessionVerwaltung.setzeEnde();
            return "uDlgFehler";
        }

        eclDbM.openAll();
        eclDbM.openWeitere();

        int rc = eclDbM.getDbBundle().dbInfo.update(uWortmeldungenSession.getbInfo());
        if (rc != 1) {
            eclDbM.closeAll();
            uSession.setFehlermeldung(
                    "Bereits von anderem Benutzer verändert - bitte aktualisieren und Bearbeitung neu starten");
            uWortmeldungenSession.setInBearbeitung(0);
            return xSessionVerwaltung.setzeUEnde("uWortmeldungenView", false, false, eclUserLoginM.getKennung());
        }
        eclDbM.closeAll();
        aktualisiereVL();
        uWortmeldungenSession.setInBearbeitung(0);
        initialisieren(uWortmeldungenSession.getViewIdent());
        return xSessionVerwaltung.setzeUEnde("uWortmeldungenView", true, false, eclUserLoginM.getKennung());

    }

    private int liefereTestNr() {
        String hNr = uWortmeldungenSession.getTestNr();
        if (!CaString.isNummern(hNr)) {
            return -1;
        }
        int nr = Integer.parseInt(hNr);
        if (nr < 1 || nr > eclParamM.getParam().paramPortal.konfRaumAnzahl[0]) {
            return -1;
        }
        return nr;
    }

    private int liefereTelefonieNr() {
        String hNr = uWortmeldungenSession.getTelefonieNr();
        if (!CaString.isNummern(hNr)) {
            return -1;
        }
        int nr = Integer.parseInt(hNr);
        if (nr < 1 || nr > eclParamM.getParam().paramPortal.konfRaumAnzahl[1]) {
            return -1;
        }
        return nr;
    }

    private void generateJwtWsToken() {
        CaTokenUtil caTokenUtil = new CaTokenUtil();
        String jwt = caTokenUtil.generateToken("vl", eclParamM.getParam().mandant);
        uWortmeldungenSession.setJwtWsToken(jwt);
    }

    public String doClearTestraumLink() {
        if (!xSessionVerwaltung.pruefeStart("uWortmeldungenView", "")) {
            xSessionVerwaltung.setzeEnde();
            return "uDlgFehler";
        }
        uWortmeldungenSession.setTestraumLink("");
        return xSessionVerwaltung.setzeUEnde("uWortmeldungenView", true, false, eclUserLoginM.getKennung());
    }

    public String doClearRederaumLink() {
        if (!xSessionVerwaltung.pruefeStart("uWortmeldungenView", "")) {
            xSessionVerwaltung.setzeEnde();
            return "uDlgFehler";
        }
        uWortmeldungenSession.setRederaumLink("");
        return xSessionVerwaltung.setzeUEnde("uWortmeldungenView", true, false, eclUserLoginM.getKennung());
    }

}
