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
import de.meetingapps.meetingportal.meetComAllg.CaDatumZeit;
import de.meetingapps.meetingportal.meetComAllg.CaFehler;
import de.meetingapps.meetingportal.meetComAllg.CaString;
import de.meetingapps.meetingportal.meetComBVerwaltung.BvMandanten;
import de.meetingapps.meetingportal.meetComBVerwaltung.BvTablets;
import de.meetingapps.meetingportal.meetComBVerwaltung.BvUserLogin;
import de.meetingapps.meetingportal.meetComBa.BaMailM;
import de.meetingapps.meetingportal.meetComBl.BlAbstimmung;
import de.meetingapps.meetingportal.meetComBl.BlAbstimmungTools;
import de.meetingapps.meetingportal.meetComBl.BlAbstimmungenWeisungen;
import de.meetingapps.meetingportal.meetComBl.BlAktienregister;
import de.meetingapps.meetingportal.meetComBl.BlAppKennungsverwaltung;
import de.meetingapps.meetingportal.meetComBl.BlAppVersionsabgleich;
import de.meetingapps.meetingportal.meetComBl.BlAufgaben;
import de.meetingapps.meetingportal.meetComBl.BlInhaberImport;
import de.meetingapps.meetingportal.meetComBl.BlDrucklauf;
import de.meetingapps.meetingportal.meetComBl.BlEinsprungLinkPortal;
import de.meetingapps.meetingportal.meetComBl.BlFragen;
import de.meetingapps.meetingportal.meetComBl.BlHybridMitglieder;
import de.meetingapps.meetingportal.meetComBl.BlInsti;
import de.meetingapps.meetingportal.meetComBl.BlKennung;
import de.meetingapps.meetingportal.meetComBl.BlMeldungen;
import de.meetingapps.meetingportal.meetComBl.BlMitteilungenAlt;
import de.meetingapps.meetingportal.meetComBl.BlParamStrukt;
import de.meetingapps.meetingportal.meetComBl.BlPdfInBrowser;
import de.meetingapps.meetingportal.meetComBl.BlPersonenprognose;
import de.meetingapps.meetingportal.meetComBl.BlPraesenzAkkreditierung;
import de.meetingapps.meetingportal.meetComBl.BlPraesenzPROV;
import de.meetingapps.meetingportal.meetComBl.BlSammelkarten;
import de.meetingapps.meetingportal.meetComBl.BlStimmkartendruck;
import de.meetingapps.meetingportal.meetComBl.BlSuchen;
import de.meetingapps.meetingportal.meetComBl.BlSuchlauf;
import de.meetingapps.meetingportal.meetComBl.BlTablet;
import de.meetingapps.meetingportal.meetComBl.BlTeilnehmerLoginNeu;
import de.meetingapps.meetingportal.meetComBl.BlTeilnehmerLoginSperre;
import de.meetingapps.meetingportal.meetComBl.BlTeilnehmerdatenLesen;
import de.meetingapps.meetingportal.meetComBl.BlVeranstaltungen;
import de.meetingapps.meetingportal.meetComBl.BlVerarbeitungsLauf;
import de.meetingapps.meetingportal.meetComBl.BlWillenserklaerung;
import de.meetingapps.meetingportal.meetComBl.BlWillenserklaerungBatch;
import de.meetingapps.meetingportal.meetComBl.BlWillenserklaerungStatus;
import de.meetingapps.meetingportal.meetComBl.BlWillenserklaerungStatusNeu;
import de.meetingapps.meetingportal.meetComBlManaged.BlMAbstimmung;
import de.meetingapps.meetingportal.meetComBlManaged.BlMAbstimmungsvorschlag;
import de.meetingapps.meetingportal.meetComBlManaged.BlMAppEmittenten;
import de.meetingapps.meetingportal.meetComBlManaged.BlMFuelleEclMAusPufferOderDBEE;
import de.meetingapps.meetingportal.meetComBlManaged.BlMInstiProv;
import de.meetingapps.meetingportal.meetComBlManaged.BlMPuffer;
import de.meetingapps.meetingportal.meetComBlManaged.BlMScanlauf;
import de.meetingapps.meetingportal.meetComBlManaged.BlMTeilnehmerKommunikation;
import de.meetingapps.meetingportal.meetComDb.DbEmittenten;
import de.meetingapps.meetingportal.meetComEclM.EclAbstimmungM;
import de.meetingapps.meetingportal.meetComEclM.EclAbstimmungenListeM;
import de.meetingapps.meetingportal.meetComEclM.EclBesitzGesamtM;
import de.meetingapps.meetingportal.meetComEclM.EclDbM;
import de.meetingapps.meetingportal.meetComEclM.EclGastM;
import de.meetingapps.meetingportal.meetComEclM.EclKIAVListeM;
import de.meetingapps.meetingportal.meetComEclM.EclKIAVM;
import de.meetingapps.meetingportal.meetComEclM.EclLoginDatenM;
import de.meetingapps.meetingportal.meetComEclM.EclParamM;
import de.meetingapps.meetingportal.meetComEclM.EclStimmkartenBlockM;
import de.meetingapps.meetingportal.meetComEclM.EclTeilnehmerLoginM;
import de.meetingapps.meetingportal.meetComEclM.EclWillenserklaerungStatusM;
import de.meetingapps.meetingportal.meetComEclM.EclZugeordneteMeldungListeM;
import de.meetingapps.meetingportal.meetComEclM.EclZugeordneteMeldungM;
import de.meetingapps.meetingportal.meetComEh.EhGetHVParam;
import de.meetingapps.meetingportal.meetComEntities.EclAktienregister;
import de.meetingapps.meetingportal.meetComEntities.EclAutoTest;
import de.meetingapps.meetingportal.meetComEntities.EclBesitzAREintrag;
import de.meetingapps.meetingportal.meetComEntities.EclBesitzJeKennung;
import de.meetingapps.meetingportal.meetComEntities.EclDrucklauf;
import de.meetingapps.meetingportal.meetComEntities.EclFragen;
import de.meetingapps.meetingportal.meetComEntities.EclGeraeteLabelPrinter;
import de.meetingapps.meetingportal.meetComEntities.EclGeraeteSet;
import de.meetingapps.meetingportal.meetComEntities.EclLoginDaten;
import de.meetingapps.meetingportal.meetComEntities.EclMeldung;
import de.meetingapps.meetingportal.meetComEntities.EclMitteilungenAlt;
import de.meetingapps.meetingportal.meetComEntities.EclPdfInBrowser;
import de.meetingapps.meetingportal.meetComEntities.EclPersonenNatJur;
import de.meetingapps.meetingportal.meetComEntities.EclRegistrierungsdaten;
import de.meetingapps.meetingportal.meetComEntities.EclStaaten;
import de.meetingapps.meetingportal.meetComEntities.EclStimmkarten;
import de.meetingapps.meetingportal.meetComEntities.EclStimmkartenBlock;
import de.meetingapps.meetingportal.meetComEntities.EclSuchergebnis;
import de.meetingapps.meetingportal.meetComEntities.EclTEinstellungenSession;
import de.meetingapps.meetingportal.meetComEntities.EclTLoginDatenM;
import de.meetingapps.meetingportal.meetComEntities.EclVirtuellerTeilnehmer;
import de.meetingapps.meetingportal.meetComEntities.EclWillenserklaerung;
import de.meetingapps.meetingportal.meetComEntities.EclWillenserklaerungStatusNeu;
import de.meetingapps.meetingportal.meetComEntities.EclWillenserklaerungZusatz;
import de.meetingapps.meetingportal.meetComEntities.EclZugeordneteMeldungNeu;
import de.meetingapps.meetingportal.meetComEntities.EclZutrittsIdent;
import de.meetingapps.meetingportal.meetComEntities.EclZutrittskarten;
import de.meetingapps.meetingportal.meetComHVParam.ParamModuleKonfigurierbar_App;
import de.meetingapps.meetingportal.meetComHVParam.SParamProgramm;
import de.meetingapps.meetingportal.meetComKonst.KonstKartenklasse;
import de.meetingapps.meetingportal.meetComKonst.KonstLoginKennungArt;
import de.meetingapps.meetingportal.meetComKonst.KonstPdfArt;
import de.meetingapps.meetingportal.meetComKonst.KonstPortalAktion;
import de.meetingapps.meetingportal.meetComKonst.KonstSkIst;
import de.meetingapps.meetingportal.meetComKonst.KonstStimmkarteIstGesperrt;
import de.meetingapps.meetingportal.meetComKonst.KonstVerarbeitungslaufArt;
import de.meetingapps.meetingportal.meetComKonst.KonstWebService;
import de.meetingapps.meetingportal.meetComKonst.KonstWeisungserfassungSicht;
import de.meetingapps.meetingportal.meetComKonst.KonstWillenserklaerung;
import de.meetingapps.meetingportal.meetComKonst.KonstWillenserklaerungWeg;
import de.meetingapps.meetingportal.meetComReports.RepSammelAnmeldebogen;
import de.meetingapps.meetingportal.meetComReports.RepSammelkarten;
import de.meetingapps.meetingportal.meetComStub.StubAbstimmungen;
import de.meetingapps.meetingportal.meetComStub.StubCtrlLogin;
import de.meetingapps.meetingportal.meetComStub.StubMailing;
import de.meetingapps.meetingportal.meetComStub.StubMandantAnlegen;
import de.meetingapps.meetingportal.meetComStub.StubParameter;
import de.meetingapps.meetingportal.meetComStub.WEStubAbstimmungen;
import de.meetingapps.meetingportal.meetComStub.WEStubAbstimmungenRC;
import de.meetingapps.meetingportal.meetComStub.WEStubBlAbstimmungenWeisungenErfassen;
import de.meetingapps.meetingportal.meetComStub.WEStubBlAbstimmungenWeisungenErfassenRC;
import de.meetingapps.meetingportal.meetComStub.WEStubBlAktienregister;
import de.meetingapps.meetingportal.meetComStub.WEStubBlAktienregisterRC;
import de.meetingapps.meetingportal.meetComStub.WEStubBlAufgaben;
import de.meetingapps.meetingportal.meetComStub.WEStubBlAufgabenRC;
import de.meetingapps.meetingportal.meetComStub.WEStubBlInhaberImport;
import de.meetingapps.meetingportal.meetComStub.WEStubBlInhaberImportRC;
import de.meetingapps.meetingportal.meetComStub.WEStubBlDrucklauf;
import de.meetingapps.meetingportal.meetComStub.WEStubBlDrucklaufRC;
import de.meetingapps.meetingportal.meetComStub.WEStubBlHybridMitglieder;
import de.meetingapps.meetingportal.meetComStub.WEStubBlHybridMitgliederRC;
import de.meetingapps.meetingportal.meetComStub.WEStubBlInsti;
import de.meetingapps.meetingportal.meetComStub.WEStubBlInstiRC;
import de.meetingapps.meetingportal.meetComStub.WEStubBlMeldungen;
import de.meetingapps.meetingportal.meetComStub.WEStubBlMeldungenRC;
import de.meetingapps.meetingportal.meetComStub.WEStubBlPersonenprognose;
import de.meetingapps.meetingportal.meetComStub.WEStubBlPersonenprognoseRC;
import de.meetingapps.meetingportal.meetComStub.WEStubBlSammelkarten;
import de.meetingapps.meetingportal.meetComStub.WEStubBlSammelkartenRC;
import de.meetingapps.meetingportal.meetComStub.WEStubBlStimmkartendruck;
import de.meetingapps.meetingportal.meetComStub.WEStubBlStimmkartendruckRC;
import de.meetingapps.meetingportal.meetComStub.WEStubBlSuchen;
import de.meetingapps.meetingportal.meetComStub.WEStubBlSuchenRC;
import de.meetingapps.meetingportal.meetComStub.WEStubBlSuchlauf;
import de.meetingapps.meetingportal.meetComStub.WEStubBlSuchlaufRC;
import de.meetingapps.meetingportal.meetComStub.WEStubBlTeilnehmerLoginSperre;
import de.meetingapps.meetingportal.meetComStub.WEStubBlTeilnehmerLoginSperreRC;
import de.meetingapps.meetingportal.meetComStub.WEStubBlTeilnehmerdatenLesen;
import de.meetingapps.meetingportal.meetComStub.WEStubBlTeilnehmerdatenLesenRC;
import de.meetingapps.meetingportal.meetComStub.WEStubBlWillenserklaerungBatch;
import de.meetingapps.meetingportal.meetComStub.WEStubBlWillenserklaerungBatchRC;
import de.meetingapps.meetingportal.meetComStub.WEStubCtrlLogin;
import de.meetingapps.meetingportal.meetComStub.WEStubCtrlLoginRC;
import de.meetingapps.meetingportal.meetComStub.WEStubMailing;
import de.meetingapps.meetingportal.meetComStub.WEStubMailingRC;
import de.meetingapps.meetingportal.meetComStub.WEStubMandantAnlegen;
import de.meetingapps.meetingportal.meetComStub.WEStubMandantAnlegenRC;
import de.meetingapps.meetingportal.meetComStub.WEStubParamStrukt;
import de.meetingapps.meetingportal.meetComStub.WEStubParamStruktRC;
import de.meetingapps.meetingportal.meetComStub.WEStubParameter;
import de.meetingapps.meetingportal.meetComStub.WEStubParameterRC;
import de.meetingapps.meetingportal.meetComStub.WEStubRepSammelAnmeldebogen;
import de.meetingapps.meetingportal.meetComStub.WEStubRepSammelAnmeldebogenRC;
import de.meetingapps.meetingportal.meetComStub.WEStubRepSammelkarten;
import de.meetingapps.meetingportal.meetComStub.WEStubRepSammelkartenRC;
import de.meetingapps.meetingportal.meetComWE.*;
import de.meetingapps.meetingportal.meetingportTController.TAnmeldenOhneErklaerung;
import de.meetingapps.meetingportal.meetingportTController.TAuswahl;
import de.meetingapps.meetingportal.meetingportTController.TEinstellungen;
import de.meetingapps.meetingportal.meetingportTController.TEinstellungenSession;
import de.meetingapps.meetingportal.meetingportTController.TEintrittskarte;
import de.meetingapps.meetingportal.meetingportTController.TEintrittskarteStornieren;
import de.meetingapps.meetingportal.meetingportTController.TOSSSession;
import de.meetingapps.meetingportal.meetingportTController.TSession;
import de.meetingapps.meetingportal.meetingportTController.TStimmabgabeku310;
import de.meetingapps.meetingportal.meetingportTController.TVollmachtDritte;
import de.meetingapps.meetingportal.meetingportTController.TVollmachtDritteStornieren;
import de.meetingapps.meetingportal.meetingportTController.TWeisung;
import de.meetingapps.meetingportal.meetingportTController.TWeisungBestaetigung;
import de.meetingapps.meetingportal.meetingportTController.TWeisungStornieren;
import de.meetingapps.meetingportal.meetingportTController.TWillenserklaerungSession;
import de.meetingapps.meetingportal.meetingportTFunktionen.TFunktionen;
import de.meetingapps.meetingportal.meetingportTFunktionen.TSessionVerwaltung;
import jakarta.ejb.Stateless;
import jakarta.ejb.TransactionManagement;
import jakarta.ejb.TransactionManagementType;
import jakarta.inject.Inject;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;

@TransactionManagement(TransactionManagementType.BEAN)
@Path("intern")
@Stateless
public class WAIntern {

    private int logDrucken = 3;

    private @Inject TSession tSession;
    private @Inject TWillenserklaerungSession tWillenserklaerungSession;
    private @Inject TSessionVerwaltung tSessionVerwaltung;
    private @Inject EclLoginDatenM eclLoginDatenM;
    private @Inject TEinstellungen tEinstellungen;
    private @Inject TEinstellungenSession tEinstellungenSession;
    private @Inject TFunktionen tFunktionen;
    private @Inject EclBesitzGesamtM eclBesitzGesamtM;
    private @Inject BlMAbstimmung blMAbstimmung;
    private @Inject BlMAbstimmungsvorschlag blMAbstimmungsvorschlag;

    private @Inject TAuswahl tAuswahl;
    private @Inject TEintrittskarte tEintrittskarte;
    private @Inject TAnmeldenOhneErklaerung tAnmeldenOhneErklaerung;
    private @Inject TWeisung tWeisung;
    private @Inject TWeisungBestaetigung tWeisungBestaetigung;
    private @Inject TVollmachtDritte tVollmachtDritte;
    private @Inject TEintrittskarteStornieren tEintrittskarteStornieren;
    private @Inject TWeisungStornieren tWeisungStornieren;
    private @Inject TVollmachtDritteStornieren tVollmachtDritteStornieren;

    private @Inject BlMTeilnehmerKommunikation blMTeilnehmerKommunikation;

    private @Inject TStimmabgabeku310 tStimmabgabeku310;
    
    @Inject
    EclDbM eclDbM;
    @Inject
    ADlgVariablen aDlgVariablen;
    @Inject
    EclAbstimmungenListeM eclAbstimmungenListeM;
    @Inject
    EclKIAVListeM eclKIAVListeM;
    @Inject
    EclKIAVM eclKIAVM;
    @Inject
    EclTeilnehmerLoginM eclTeilnehmerLoginM;
    @Inject
    EclZugeordneteMeldungListeM eclZugeordneteMeldungListeM;
    @Inject
    EclZugeordneteMeldungM eclZugeordneteMeldungM;
    @Inject
    EclWillenserklaerungStatusM eclWillenserklaerungStatusM;
    @Inject
    EclGastM eclGastM;
    @Inject
    BlMScanlauf blmScanlauf;
    @Inject
    BlMInstiProv blmInstiProv;

    @Inject
    AControllerLogin aControllerLogin;
    @Inject
    AControllerAnmelden aControllerAnmelden;
    @Inject
    AControllerEintrittskarte aControllerEintrittskarte;
    @Inject
    AControllerEintrittskarteQuittung aControllerEintrittskarteQuittung;
    @Inject
    AControllerEintrittskarteStornieren aControllerEintrittskarteStornieren;
    @Inject
    AControllerVollmachtDritte aControllerVollmachtDritte;
    @Inject
    AControllerWeisungBestaetigung aControllerWeisungBestaetigung;
    @Inject
    AControllerWeisungBestaetigungSession aControllerWeisungBestaetigungSession;
    @Inject
    AControllerKIAVAuswahl aControllerKIAVAuswahl;
    @Inject
    AControllerKIAVBestaetigung aControllerKIAVBestaetigung;
    @Inject
    AControllerVollmachtDritteStornieren aControllerVollmachtDritteStornieren;
    @Inject
    AControllerOeffentlicheID aControllerOeffentlicheID;
    @Inject
    AControllerRegistrierung aControllerRegistrierung;
    @Inject
    AControllerRegistrierungSession aControllerRegistrierungSession;
    @Inject
    AControllerPasswortVergessen aControllerPasswortVergessen;
    @Inject
    AControllerPasswortVergessenSession aControllerPasswortVergessenSession;
    @Inject
    AControllerBestaetigenSession aControllerBestaetigenSession;
    @Inject
    XSessionVerwaltung xSessionVerwaltung;
    @Inject
    private AControllerFragen aControllerFragen;
    @Inject
    private AControllerFragenSession aControllerFragenSession;
    @Inject
    private AControllerMitteilungen aControllerMitteilungen;
    @Inject
    private AControllerMitteilungenSession aControllerMitteilungenSession;
    @Inject
    private AControllerAbstimmungserg aControllerAbstimmungserg;
    @Inject
    private AControllerAbstimmungsergSession aControllerAbstimmungsergSession;
    @Inject
    private AControllerTeilnehmerverz aControllerTeilnehmerverz;
    @Inject
    private AControllerTeilnehmerverzSession aControllerTeilnehmerverzSession;

    @Inject
    AFunktionen aFunktionen;
    @Inject
    EclParamM eclParamM;
    @Inject
    BaMailM baMailM;
    @Inject
    BlMPuffer blMPuffer;
    @Inject
    BlMFuelleEclMAusPufferOderDBEE blMFuelleEclMAusPufferOderDBEE;
    @Inject
    BlMAppEmittenten blMAppEmittenten;
    @Inject
    ALanguage aLanguage;

    private String internerUser = "todo";
    private String internesPasswort = "todo";

    private String appUser = "todo";
    private String appPasswort = "todo";

    private String basemenUser = "todo";
    private String basemenPasswort = "todo";

    /**
     * Übergreifende Info: "Benutzerverifikation": I.d.R.: > Die Erst-Verifikation
     * des Teilnehmers erfolgt über Service loginCheck. Im dort zurückgegebenen
     * EclTeilnehmerLoginM sind die Daten für den zukünftigen "Quickverify" bei
     * jedem weiteren Serviceaufruf enthalten, nämlich: anmeldeKennungArt
     * (1=AktionärsIdent; 2=Aktionärsmeldung; 3=PersonNatJur)
     * anmeldeIdentAktienregister anmeldeIdentPersonenNatJur
     *
     * Bei jedem Folgeaufruf von Services, bei denen Teilnehmerdaten abgefragt oder
     * zurückgegeben werden, werden die obigen 3 Felder in WELoginVerify wieder
     * mitübergeben. Beim Aufruf durch eine App werden diese 3 Felder zusammen mit
     * passwort zur erneuten - schnellen - Verifikation verwendet.
     */

    private String ipXForwardedFor = "";
    private String ipProxyClientIP = "";
    private String ipWLProxyClientIP = "";
    private String ipHTTPXFORWARDEDFOR = "";
    private String ipRemoteAddr = "";

    @Context
    private HttpServletRequest hsr;

    public void get_ip() {
        System.out.println("getIP");
        ipXForwardedFor = hsr.getHeader("X-Forwarded-For");
        if (ipXForwardedFor == null) {
            ipXForwardedFor = "null";
        }

        ipProxyClientIP = hsr.getHeader("Proxy-Client-IP");
        if (ipProxyClientIP == null) {
            ipProxyClientIP = "null";
        }

        ipWLProxyClientIP = hsr.getHeader("WL-Proxy-Client-IP");
        if (ipWLProxyClientIP == null) {
            ipWLProxyClientIP = "null";
        }

        ipHTTPXFORWARDEDFOR = hsr.getHeader("HTTP_X_FORWARDED_FOR");
        if (ipHTTPXFORWARDEDFOR == null) {
            ipHTTPXFORWARDEDFOR = "null";
        }

        ipRemoteAddr = hsr.getRemoteAddr();
        if (ipRemoteAddr == null) {
            ipRemoteAddr = "null";
        }

        System.out.println("ipXForwardedFor=" + ipXForwardedFor + "ipProxyClientIP=" + ipProxyClientIP
                + "ipWLProxyClientIP=" + ipWLProxyClientIP + "ipHTTPXFORWARDEDFOR=" + ipHTTPXFORWARDEDFOR
                + "ipRemoteAddr=" + ipRemoteAddr);
    }

    /************* allgemeine Funktionen *************/

    /**
     * Hier wird - bei App - in starteService die String-Aktionärsnummer abgelegt,
     * für die der Login geprüft wurde. Bei den einzelnen Services kann dann
     * überprüft werden, ob die abgegebenen Willenserklärungen hierzu passen.
     */
    private String aktiveAktionaersnummer = "";
    @Deprecated
    private long aktiveAktionaersnummerStimmen = 0;
    @Deprecated
    private int aktiveAktionaersGattung = 0;

    private void fuelleEclZBesitzGesamtM() {
        tFunktionen.setZusammenfassenVonAnmeldungenMoeglich(false);
        tFunktionen.setAlleWillenserklaerungen(true);
        tFunktionen.setWeitereKennungenZuladen(true);
        tFunktionen.setUpdateTblLogin(false);
        tFunktionen.leseStatusPortal(eclDbM.getDbBundle());
        /* EclBesitzGesamtM ist nun gefüllt */
    }

    @Deprecated
    private EclZugeordneteMeldungM pruefeInListe(EclZugeordneteMeldungM pEclZugeordneteMeldungM,
            List<EclZugeordneteMeldungM> listEclZugeordneteMeldungM) {
        if (listEclZugeordneteMeldungM == null) {
            return null;
        }
        for (int i = 0; i < listEclZugeordneteMeldungM.size(); i++) {
            if (listEclZugeordneteMeldungM.get(i).getMeldungsIdent() == pEclZugeordneteMeldungM.getMeldungsIdent()) {
                return listEclZugeordneteMeldungM.get(i);
            }
        }
        return null;
    }

    /**
     * Prüfen, ob MeldungM und WillenserklärungStatus in EclZugeordneteMeldungListeM
     * enthalten sind, und liefert das tatsächlich gespeichert zurück (wg.
     * Verfälschungen in der App)
     */
    @Deprecated
    private EclZugeordneteMeldungM pruefeMeldungZuAktionaerPassend(EclZugeordneteMeldungM pEclZugeordneteMeldungM) {
        EclZugeordneteMeldungM lEclZugeordneteMeldungM = null;
        lEclZugeordneteMeldungM = pruefeInListe(pEclZugeordneteMeldungM,
                eclZugeordneteMeldungListeM.getZugeordneteMeldungenEigeneAktienListeM());
        if (lEclZugeordneteMeldungM != null) {
            return lEclZugeordneteMeldungM;
        }

        lEclZugeordneteMeldungM = pruefeInListe(pEclZugeordneteMeldungM,
                eclZugeordneteMeldungListeM.getZugeordneteMeldungenBevollmaechtigtListeM());
        if (lEclZugeordneteMeldungM != null) {
            return lEclZugeordneteMeldungM;
        }

        lEclZugeordneteMeldungM = pruefeInListe(pEclZugeordneteMeldungM,
                eclZugeordneteMeldungListeM.getZugeordneteMeldungenEigeneGastkartenListeM());
        if (lEclZugeordneteMeldungM != null) {
            return lEclZugeordneteMeldungM;
        }

        return null;
    }

    private EclZugeordneteMeldungNeu pruefeInListe(List<EclZugeordneteMeldungNeu> zugeordneteMeldungenListe,
            EclZugeordneteMeldungNeu pEclZugeordneteMeldung) {
        for (EclZugeordneteMeldungNeu iEclZugeordneteMeldung : zugeordneteMeldungenListe) {
            if (iEclZugeordneteMeldung.meldungsIdent == pEclZugeordneteMeldung.meldungsIdent) {
                return iEclZugeordneteMeldung;
            }
        }

        return null;
    }

    private EclZugeordneteMeldungNeu pruefeMeldungZuEclBesitzAREintrag(EclBesitzAREintrag pEclBesitzAREintrag,
            EclZugeordneteMeldungNeu pEclZugeordneteMeldung) {
        EclZugeordneteMeldungNeu rcEclZugeordneteMeldungNeu = null;

        rcEclZugeordneteMeldungNeu = pruefeInListe(pEclBesitzAREintrag.zugeordneteMeldungenListe,
                pEclZugeordneteMeldung);
        if (rcEclZugeordneteMeldungNeu != null) {
            return rcEclZugeordneteMeldungNeu;
        }

        return null;
    }

    /**
     * Prüfen, ob MeldungM und WillenserklärungStatus in EclZugeordneteMeldungListeM
     * enthalten sind, und liefert das tatsächlich gespeichert zurück (wg.
     * Verfälschungen in der App)
     */
    private EclZugeordneteMeldungNeu pruefeMeldungZuAktionaerPassend(EclZugeordneteMeldungNeu pEclZugeordneteMeldung) {

        EclZugeordneteMeldungNeu rcEclZugeordneteMeldungNeu = null;

        for (EclBesitzJeKennung iEclBesitzJeKennung : eclBesitzGesamtM.getBesitzJeKennungListe()) {
            for (EclBesitzAREintrag iclBesitzAREintrag : iEclBesitzJeKennung.eigenerAREintragListe) {
                rcEclZugeordneteMeldungNeu = pruefeMeldungZuEclBesitzAREintrag(iclBesitzAREintrag,
                        pEclZugeordneteMeldung);
                if (rcEclZugeordneteMeldungNeu != null) {
                    return rcEclZugeordneteMeldungNeu;
                }
            }

            rcEclZugeordneteMeldungNeu = pruefeInListe(iEclBesitzJeKennung.zugeordneteMeldungenBevollmaechtigtListe,
                    pEclZugeordneteMeldung);
            if (rcEclZugeordneteMeldungNeu != null) {
                return rcEclZugeordneteMeldungNeu;
            }

            rcEclZugeordneteMeldungNeu = pruefeInListe(iEclBesitzJeKennung.zugeordneteMeldungenInstiListe,
                    pEclZugeordneteMeldung);
            if (rcEclZugeordneteMeldungNeu != null) {
                return rcEclZugeordneteMeldungNeu;
            }

            for (EclBesitzAREintrag iclBesitzAREintrag : iEclBesitzJeKennung.instiAREintraegeListe) {
                rcEclZugeordneteMeldungNeu = pruefeMeldungZuEclBesitzAREintrag(iclBesitzAREintrag,
                        pEclZugeordneteMeldung);
                if (rcEclZugeordneteMeldungNeu != null) {
                    return rcEclZugeordneteMeldungNeu;
                }
            }

        }

        return null;
    }

    /**
     * Prüfen, ob MeldungM und WillenserklärungStatus in EclZugeordneteMeldungListeM
     * enthalten sind, und liefert das tatsächlich gespeichert zurück (wg.
     * Verfälschungen in der App)
     */
    private EclBesitzAREintrag pruefeAREintragZuAktionaerPassend(String pAktionaersnummer) {

        for (EclBesitzJeKennung iEclBesitzJeKennung : eclBesitzGesamtM.getBesitzJeKennungListe()) {
            for (EclBesitzAREintrag iclBesitzAREintrag : iEclBesitzJeKennung.eigenerAREintragListe) {
                if (iclBesitzAREintrag.aktienregisterEintrag.aktionaersnummer.equals(aktiveAktionaersnummer)) {
                    return iclBesitzAREintrag;
                }
            }

            for (EclBesitzAREintrag iclBesitzAREintrag : iEclBesitzJeKennung.instiAREintraegeListe) {
                if (iclBesitzAREintrag.aktienregisterEintrag.aktionaersnummer.equals(aktiveAktionaersnummer)) {
                    return iclBesitzAREintrag;
                }
            }
        }

        return null;
    }

    @Deprecated
    private EclWillenserklaerungStatusM pruefeWillenserklaerungZuAktionaerPassend(
            EclZugeordneteMeldungM pEclZugeordneteMeldungM, EclWillenserklaerungStatusM pEclWillenserklaerungStatusM) {

        if (pEclZugeordneteMeldungM == null) {
            return null;
        }
        if (pEclZugeordneteMeldungM.getZugeordneteWillenserklaerungenListM() == null) {
            return null;
        }

        for (int i = 0; i < pEclZugeordneteMeldungM.getZugeordneteWillenserklaerungenListM().size(); i++) {
            if (pEclZugeordneteMeldungM.getZugeordneteWillenserklaerungenListM().get(i)
                    .getWillenserklaerungIdent() == pEclWillenserklaerungStatusM.getWillenserklaerungIdent()) {
                return pEclZugeordneteMeldungM.getZugeordneteWillenserklaerungenListM().get(i);
            }
        }
        return null;
    }

    private EclWillenserklaerungStatusNeu pruefeWillenserklaerungZuAktionaerPassend(
            EclZugeordneteMeldungNeu pEclZugeordneteMeldung,
            EclWillenserklaerungStatusNeu pEclWillenserklaerungStatus) {

        if (pEclZugeordneteMeldung == null) {
            return null;
        }
        if (pEclZugeordneteMeldung.getZugeordneteWillenserklaerungenList() == null) {
            return null;
        }

        for (int i = 0; i < pEclZugeordneteMeldung.getZugeordneteWillenserklaerungenList().size(); i++) {
            if (pEclZugeordneteMeldung.getZugeordneteWillenserklaerungenList().get(i)
                    .getWillenserklaerungIdent() == pEclWillenserklaerungStatus.getWillenserklaerungIdent()) {
                return pEclZugeordneteMeldung.getZugeordneteWillenserklaerungenList().get(i);
            }
        }
        return null;
    }

    /**
     * Prüfen Systemberechtigung, Initialisieren Mandant, öffnen Datenbank. Falls
     * pPruefeTeilnehmerLogin true, dann zusätzlich Aktionärspasswort prüfen (muß
     * immer gemacht werden, außer es werden mit dem Dienst ausschließlich
     * Aktionärsunabhängige Daten abgerufen, also z.b. Tagesordnung, Texte etc.)
     *
     * Falls pExternMoeglich false, dann steht dieser Service nicht für Devices beim
     * Aktionär (HV-App) zur Verfügung - da z.B. übergreifende Aktionärsdaten geholt
     * werden.
     *
     * Ergebnis / Returnwert: 1=ok 2=Versionsabgleich erforderlich
     * CaFehler.afFalscheKennung
     */
    private int starteService(WELoginVerify pWELoginVerify, String pServicename, boolean pPruefeTeilnehmerLogin,
            boolean pExternMoeglich) {
        /*Sicherheitsprüfung implementieren*/ 
 
        /* Mandant, Arbeitsplatz, User setzen - vor Open! */
        eclParamM.getClGlobalVar().mandant = pWELoginVerify.getMandant();
        eclParamM.getClGlobalVar().hvJahr = pWELoginVerify.getHvJahr();
        eclParamM.getClGlobalVar().hvNummer = pWELoginVerify.getHvNummer();
        eclParamM.getClGlobalVar().datenbereich = pWELoginVerify.getDatenbereich();
        eclParamM.getClGlobalVar().benutzernr = pWELoginVerify.getBenutzernr();
        eclParamM.getClGlobalVar().arbeitsplatz = pWELoginVerify.getArbeitsplatz();

        /* Datenbanken öffnen - und Parameter setzen */
        eclDbM.openAllOhneParameterCheck();

        blMFuelleEclMAusPufferOderDBEE.fuelleAlleVariablenBeiStart();

        /*
         * Daten werden immer gesetzt - kann nix schaden, auch wenn sie nicht gebraucht
         * werden
         */
        tWillenserklaerungSession.setEingabeQuelle(pWELoginVerify.getEingabeQuelle());
        tWillenserklaerungSession.setErteiltZeitpunkt(pWELoginVerify.getErteiltZeitpunkt());

        tSessionVerwaltung.setStartPruefen(0);

        return 99999;
    }

    /**
     * füllt EclTeilnehmerLoginM aus den übergebenen Daten. Wichtig: vor dieser
     * Funktion braucht bei starteService PruefeTeilnehmerLogin nicht durchgeführt
     * werden, da dies in dieser Funktion noch erfolgt!
     *
     * bei pTechnischerUser==1 wird das Passwort nie abgeprüft. Bei ==2 je nach
     * pMitPasswort.
     */
    private int stelleEclTeilnehmerLoginMBereit(String pKennung, String pPasswort, int pTechnischerUser,
            boolean pMitPasswort) {

        boolean passwortPruefen = false;
        if (pTechnischerUser == 1) {/* Login über "technischen" User internen Client */
        } else {/* TeilnehmerPasswort wird überprüft */
            if (pMitPasswort) {
                passwortPruefen = true;
            }
        }

        BlTeilnehmerLoginNeu blTeilnehmerLogin = new BlTeilnehmerLoginNeu();
        blTeilnehmerLogin.initDB(eclDbM.getDbBundle());
        int erg = blTeilnehmerLogin.findeUndPruefeKennung(pKennung, pPasswort, passwortPruefen);

        if (erg >= 0) {
            blTeilnehmerLogin.pruefeErstregistrierung();

            /* TODO für App: Anderen User rausschmeißen - noch unklar! */

            eclLoginDatenM.copyFrom(blTeilnehmerLogin);

            aktiveAktionaersnummer = eclLoginDatenM.getEclLoginDaten().loginKennung;
        }
        return erg;
    }

    private void abbruchService() {
        System.out.println("WAIntern: abbruchService");
        eclDbM.closeAllAbbruch();
    }

    private void beendeService() {
        System.out.println("WAIntern: beendeService");
        eclDbM.closeAll();
    }

    private void beendeServiceOhneClose() {
        System.out.println("WAIntern: beendeService");
    }

    private void zeigeStubText(String pStubText) {
        CaBug.druckeProt(">>>Stub: "+pStubText+"<<<Verschicken");
    }

 

    @POST
    @Path("versionsabgleich")
    @Produces({ MediaType.APPLICATION_JSON, MediaType.TEXT_XML })
    public WEVersionsabgleichRC versionsabgleich(WEVersionsabgleich pWEVersionsabgleich) {

        WEVersionsabgleichRC weVersionsabgleichRC=new WEVersionsabgleichRC();

        boolean serverPasstZuAktuellerClientVersion=false;

        if (SParamProgramm.programmVersion.equals(pWEVersionsabgleich.aktuelleClientVersion)) {
            serverPasstZuAktuellerClientVersion=true;
        }

        if (serverPasstZuAktuellerClientVersion==false) {
            /*Nun noch überprüfen, ob die Client-Version mit der Serverversion kann*/
            if (SParamProgramm.programmVersionClientAufServer!=null && SParamProgramm.programmVersionClientAufServer.length>0) {
                for (int i=0;i<SParamProgramm.programmVersionClientAufServer.length;i++) {
                    if (pWEVersionsabgleich.aktuelleClientVersion.equals(SParamProgramm.programmVersionClientAufServer[i])) {
                        serverPasstZuAktuellerClientVersion=true;
                    }
                }
            }
        }

        weVersionsabgleichRC.serverPasstZuAktuellerClientVersion=serverPasstZuAktuellerClientVersion;
        weVersionsabgleichRC.aktuelleServerVersion=SParamProgramm.programmVersion;

        weVersionsabgleichRC.rc=1;

        return weVersionsabgleichRC;
    }

    /**************************************
     * loginCheck********************************************
     *
     * Funktion muß aufgerufen werden vor allen anderen Funktionen, die Aktionen zu
     * Teilnehmern (egal ob lesend oder erteilend) ausführen. Aktuell offiziell
     * freigeben (für Namensaktien):
     *
     * Übergabeparameter: WELoginCheck
     *
     * Rückgabe: WELoginCheckRC mit EclTeilnehmerLoginM enthält alle Infos zu dem
     * Aktionär, für den der "Login" in WELoginI angegeben wurde. Im Client-Programm
     * wird dies dann als "globale Variable" hinterlegt, um weitere Aktionen mit den
     * in EclTeilnehmerLoginM enthaltenen Infos zu füllen.
     *
     */
    @POST
    @Path("loginCheck")
    @Produces({ MediaType.APPLICATION_JSON, MediaType.TEXT_XML })
    public WELoginCheckRC loginCheck(WELoginCheck pWELoginCheck) {

        /*Individuell zu implementieren*/
         return null;
    }

    /*
     * TODO _Konsolidieren App: hvDatenGet liefert nur Schrott. Wird m.W. nur noch
     * in "alter-Test-App" verwendet (die noch keine Mandanteverwaltung hat, sondern
     * immer mit mandant 54 arbeitet). Dort noch raus, und dann das hier entfernen
     */
    /*********************
     * HV-Daten anfordern************************************************ Liefert
     * die Daten der aktuellen HV zurück. Wird aber derzeit nicht wirklich gefüllt
     * :-), d.h. hier in dieser Funktion ist fest programmiert, was zurückgegeben
     * wird
     */
    @POST
    @Path("hvDatenGet")
    @Produces({ MediaType.APPLICATION_JSON, MediaType.TEXT_XML })
    public WEHVDatenRC hvDatenGet(WELoginVerify weLoginVerify) {

        WEHVDatenRC weHVDatenRC = new WEHVDatenRC();
        int rc = starteService(weLoginVerify, "hvDatenGet", false, true);
        System.out.println(
                "!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!Deprecated!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        if (rc < 1) {
            weHVDatenRC.setRc(rc);
            return weHVDatenRC;
        }
        if (rc == 2) {
            weHVDatenRC.setPruefeVersion(1);
        }

        weHVDatenRC.setHvNr(1);
        weHVDatenRC.setGesellschaft("Better Orange IR & HV AG");
        weHVDatenRC.setHvDatum("08. Dezember 2016");
        weHVDatenRC.setHvOrt("München");
        weHVDatenRC.setMandant(54);
        weHVDatenRC.setRc(1);

        beendeService();
        return weHVDatenRC;
    }

    /**************************
     * teilnehmerStatusGet*******************************************************
     *
     * @param weLoginVerify
     * @return
     */
    /*Soll ersetzt werden durch teilnehmerStatusClientGet*/
    @Deprecated
    @POST
    @Path("teilnehmerStatusGet")
    @Produces({ MediaType.APPLICATION_JSON, MediaType.TEXT_XML })
    public WETeilnehmerStatusGetRC teilnehmerStatusGet(WELoginVerify weLoginVerify) {

        WETeilnehmerStatusGetRC weTeilnehmerStatusGetRC = new WETeilnehmerStatusGetRC();

        int rc = starteService(weLoginVerify, "teilnehmerStatusGet", true, true);
        if (rc < 1) {
            weTeilnehmerStatusGetRC.setRc(rc);
            return weTeilnehmerStatusGetRC;
        }
        if (rc == 2) {
            weTeilnehmerStatusGetRC.setPruefeVersion(1);
        }

        // if (weLoginVerify.getArbeitsplatz()!=9998){
        if (weLoginVerify.getUser() != 2) {
            tFunktionen.setZusammenfassenVonAnmeldungenMoeglich(false);
            tFunktionen.setAlleWillenserklaerungen(true);
            tFunktionen.setWeitereKennungenZuladen(false);
            tFunktionen.setUpdateTblLogin(false);
        }

        /********************************** XXXXXXXXXXXXXXXXXXXXXXXXXX *********/
        tFunktionen.leseStatusPortal(eclDbM.getDbBundle());

        weTeilnehmerStatusGetRC.besitzJeKennungListe = eclBesitzGesamtM.getBesitzJeKennungListe();

        beendeService();
        return weTeilnehmerStatusGetRC;

    }


    @POST
    @Path("teilnehmerStatusClientGet")
    @Produces({ MediaType.APPLICATION_JSON, MediaType.TEXT_XML })
    public WETeilnehmerStatusClientGetRC teilnehmerStatusClientGet(WETeilnehmerStatusClientGet pWETeilnehmerStatusClientGet) {

        WETeilnehmerStatusClientGetRC weTeilnehmerStatusClientGetRC = new WETeilnehmerStatusClientGetRC();

        int rc = starteService(pWETeilnehmerStatusClientGet.getWeLoginVerify(), "teilnehmerStatusClientGet", true, true);
        if (rc < 1) {
            weTeilnehmerStatusClientGetRC.setRc(rc);
            return weTeilnehmerStatusClientGetRC;
        }
        if (rc == 2) {
            weTeilnehmerStatusClientGetRC.setPruefeVersion(1);
        }


        BlWillenserklaerungStatusNeu blWillenserklaerungStatusVoll = new BlWillenserklaerungStatusNeu(eclDbM.getDbBundle());

        blWillenserklaerungStatusVoll.nurNichtStornierteWillenserklaerungen=false;
        blWillenserklaerungStatusVoll.umZugeordneteKennungenErgaenzen=true;

        blWillenserklaerungStatusVoll.initAusgangsdaten(eclLoginDatenM.getEclLoginDaten(), eclLoginDatenM.getEclAktienregister(), eclLoginDatenM.getEclPersonenNatJur());
        blWillenserklaerungStatusVoll.piAusblendenMeldungen = null;

        blWillenserklaerungStatusVoll.piAnsichtVerarbeitungOderAnalyse = 2;
        blWillenserklaerungStatusVoll.piRueckgabeKurzOderLang = 2;

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

        weTeilnehmerStatusClientGetRC.besitzJeKennungListe = blWillenserklaerungStatusVoll.besitzJeKennungListe;

        beendeService();
        return weTeilnehmerStatusClientGetRC;

    }


    /************************
     * anmeldung
     *************************************************************/
    @POST
    @Path("anmeldung")
    @Produces({ MediaType.APPLICATION_JSON, MediaType.TEXT_XML })
    public WEAnmeldungRC anmeldung(WEAnmeldung weAnmeldung) {
        WEAnmeldungRC weAnmeldungRC = new WEAnmeldungRC();
        int rc = starteService(weAnmeldung.getWeLoginVerify(), "anmeldung", true, true);
        if (rc < 1) {
            weAnmeldungRC.setRc(rc);
            return weAnmeldungRC;
        }
        if (rc == 2) {
            weAnmeldungRC.setPruefeVersion(1);
        }

        if (weAnmeldung.getWeLoginVerify().getUser() == 2) {
            if (weAnmeldung.anmeldeAktionaersnummer.compareTo(aktiveAktionaersnummer) != 0) {
                CaBug.drucke("!!!!!!!!!!!!!eintrittskarte - Hack: " + weAnmeldung.getAnmeldeAktionaersnummer() + "/"
                        + aktiveAktionaersnummer);
                abbruchService();
                weAnmeldungRC.setRc(-99999);
                return weAnmeldungRC;
            }
        }

        tWillenserklaerungSession.setAusgewaehlteHauptAktion(KonstPortalAktion.HAUPT_NEUANMELDUNG);
        tWillenserklaerungSession.setAusgewaehlteAktion(KonstPortalAktion.NUR_ANMELDUNG_OHNE_WEITERE_WILLENSERKLAERUNG);

        /*
         * Aktieneintrag suchen und eintragen, für den die Anmeldung ausgeführt werden
         * soll
         */
        EclBesitzAREintrag lBesitzAREintrag = pruefeAREintragZuAktionaerPassend(weAnmeldung.anmeldeAktionaersnummer);
        if (lBesitzAREintrag == null) {
            CaBug.drucke("001 weAnmeldung.anmeldeAktionaersnummer=" + weAnmeldung.anmeldeAktionaersnummer);
        }
        tWillenserklaerungSession.initBesitzAREintragListe();
        tWillenserklaerungSession.addBesitzAREintragListe(lBesitzAREintrag);

        boolean ergBool = false;

        if (weAnmeldung.getWeLoginVerify().getUser() == 2) {
            /* Transaktion überhaupt noch zulässig? */
            ergBool = tAnmeldenOhneErklaerung.pruefeObZulaessig();
            if (ergBool == false) {
                abbruchService();
                weAnmeldungRC.setRc(-99999);
                return weAnmeldungRC;
            }
        }

        tWillenserklaerungSession.setQuelle(weAnmeldung.quelle);

        ergBool = tAnmeldenOhneErklaerung.anmelden(false);

        if (ergBool == false) {
            weAnmeldungRC.setRc(tSession.getFehlerCode());
            weAnmeldungRC.setFehlerMeldung(tSession.getFehlerMeldung());
            abbruchService();
            return weAnmeldungRC;
        } else {
        }

        beendeService();
        return weAnmeldungRC;
    }

    /************************
     * eintrittskarte*************************************************************
     * Ausstellen einer/zwei Eintrittskarte(n), mit ohne Vollmacht etc. (mit
     * Stimmrecht, also für Aktionäre. Keine Gastkarte!).
     *
     */
    @POST
    @Path("eintrittskarte")
    @Produces({ MediaType.APPLICATION_JSON, MediaType.TEXT_XML })
    public WEEintrittskarteRC eintrittskarte(WEEintrittskarte weEintrittskarte) {
        /*
         * TODO #Konsoldierung - Vision: über App-Schnittstelle wohl keine
         * Willenserklärungen für Bevollmächtigte Meldungen möglich
         */
        WEEintrittskarteRC weEintrittskarteRC = new WEEintrittskarteRC();
        int rc = starteService(weEintrittskarte.getWeLoginVerify(), "eintrittskarte", true, true);
        if (rc < 1) {
            weEintrittskarteRC.setRc(rc);
            return weEintrittskarteRC;
        }
        if (rc == 2) {
            weEintrittskarteRC.setPruefeVersion(1);
        }

        if (weEintrittskarte.getWeLoginVerify().getUser() == 2) {
            if (weEintrittskarte.anmeldeAktionaersnummer.compareTo(aktiveAktionaersnummer) != 0) {
                CaBug.drucke("!!!!!!!!!!!!!eintrittskarte - Hack: " + weEintrittskarte.getAnmeldeAktionaersnummer()
                        + "/" + aktiveAktionaersnummer);
                abbruchService();
                weEintrittskarteRC.setRc(-99999);
                return weEintrittskarteRC;
            }

            if (weEintrittskarte.ausgewaehlteHauptAktion == KonstPortalAktion.HAUPT_BEREITSANGEMELDET) {
                EclZugeordneteMeldungNeu lZugeordneteMeldung = pruefeMeldungZuAktionaerPassend(
                        weEintrittskarte.eclZugeordneteMeldung);
                if (lZugeordneteMeldung == null) {

                    CaBug.drucke("!!!!!!!!!!!!!eintrittskarte - Hack Meldung: " + aktiveAktionaersnummer);
                    abbruchService();
                    weEintrittskarteRC.setRc(-99999);
                    return weEintrittskarteRC;
                }
                weEintrittskarte.eclZugeordneteMeldung = lZugeordneteMeldung;
            }

        }

        tWillenserklaerungSession.setAusgewaehlteHauptAktion(weEintrittskarte.ausgewaehlteHauptAktion);
        tWillenserklaerungSession.setAusgewaehlteAktion(weEintrittskarte.ausgewaehlteAktion);

//        aDlgVariablen.setUeberOeffentlicheID(weEintrittskarte.isUeberOeffentlicheID());
//        aDlgVariablen.setZielOeffentlicheID(weEintrittskarte.getZielOeffentlicheID());
//        aDlgVariablen.setPersonNatJurOeffentlicheID(weEintrittskarte.getPersonNatJurOeffentlicheID());
        tWillenserklaerungSession.setEintrittskarteVersandart(weEintrittskarte.eintrittskarteVersandart);
        tWillenserklaerungSession
                .setEintrittskarteAbweichendeAdresse1(weEintrittskarte.getEintrittskarteAbweichendeAdresse1());
        tWillenserklaerungSession
                .setEintrittskarteAbweichendeAdresse2(weEintrittskarte.getEintrittskarteAbweichendeAdresse2());
        tWillenserklaerungSession
                .setEintrittskarteAbweichendeAdresse3(weEintrittskarte.getEintrittskarteAbweichendeAdresse3());
        tWillenserklaerungSession
                .setEintrittskarteAbweichendeAdresse4(weEintrittskarte.getEintrittskarteAbweichendeAdresse4());
        tWillenserklaerungSession
                .setEintrittskarteAbweichendeAdresse5(weEintrittskarte.getEintrittskarteAbweichendeAdresse5());
        tWillenserklaerungSession.setEintrittskarteEmail(weEintrittskarte.getEintrittskarteEmail());
        tWillenserklaerungSession.setEintrittskarteEmailBestaetigen(weEintrittskarte.getEintrittskarteEmail());
        tWillenserklaerungSession.setVollmachtName(weEintrittskarte.getVollmachtName());
        tWillenserklaerungSession.setVollmachtVorname(weEintrittskarte.getVollmachtVorname());
        tWillenserklaerungSession.setVollmachtOrt(weEintrittskarte.getVollmachtOrt());
        if (!tWillenserklaerungSession.getVollmachtName().isEmpty()) {
            tWillenserklaerungSession.setVollmachtEingeben(true);
        }

        tWillenserklaerungSession.setEintrittskarteVersandart2(weEintrittskarte.eintrittskarteVersandart2);
        tWillenserklaerungSession
                .setEintrittskarteAbweichendeAdresse12(weEintrittskarte.getEintrittskarteAbweichendeAdresse12());
        tWillenserklaerungSession
                .setEintrittskarteAbweichendeAdresse22(weEintrittskarte.getEintrittskarteAbweichendeAdresse22());
        tWillenserklaerungSession
                .setEintrittskarteAbweichendeAdresse32(weEintrittskarte.getEintrittskarteAbweichendeAdresse32());
        tWillenserklaerungSession
                .setEintrittskarteAbweichendeAdresse42(weEintrittskarte.getEintrittskarteAbweichendeAdresse42());
        tWillenserklaerungSession
                .setEintrittskarteAbweichendeAdresse52(weEintrittskarte.getEintrittskarteAbweichendeAdresse52());
        tWillenserklaerungSession.setEintrittskarteEmail2(weEintrittskarte.getEintrittskarteEmail2());
        tWillenserklaerungSession.setEintrittskarteEmail2Bestaetigen(weEintrittskarte.getEintrittskarteEmail2());
        tWillenserklaerungSession.setVollmachtName2(weEintrittskarte.getVollmachtName2());
        tWillenserklaerungSession.setVollmachtVorname2(weEintrittskarte.getVollmachtVorname2());
        tWillenserklaerungSession.setVollmachtOrt2(weEintrittskarte.getVollmachtOrt2());
        if (!tWillenserklaerungSession.getVollmachtName2().isEmpty()) {
            tWillenserklaerungSession.setVollmachtEingeben2(true);
        }

        if (weEintrittskarte.ausgewaehlteHauptAktion == KonstPortalAktion.HAUPT_NEUANMELDUNG) {
            /*
             * Aktieneintrag suchen und eintragen, für den die Anmeldung ausgeführt werden
             * soll
             */
            EclBesitzAREintrag lBesitzAREintrag = pruefeAREintragZuAktionaerPassend(
                    weEintrittskarte.anmeldeAktionaersnummer);
            if (lBesitzAREintrag == null) {
                CaBug.drucke(
                        "001 weEintrittskarte.anmeldeAktionaersnummer=" + weEintrittskarte.anmeldeAktionaersnummer);
            }
            tWillenserklaerungSession.initBesitzAREintragListe();
            tWillenserklaerungSession.addBesitzAREintragListe(lBesitzAREintrag);
        }

        if (weEintrittskarte.eclZugeordneteMeldung != null) {
            tWillenserklaerungSession.initZugeordneteMeldungFuerAusfuehrungListe();
            tWillenserklaerungSession.addZugeordneteMeldungFuerAusfuehrungListe(weEintrittskarte.eclZugeordneteMeldung,
                    null);
        }

        Boolean ergBool = tEintrittskarte.pruefeEingabenFuerAktionaerEintrittskarte();
        if (ergBool == false) {/* Prüfung fehlgeschlagen */
            weEintrittskarteRC.setRc(tSession.getFehlerCode());
            weEintrittskarteRC.setFehlerMeldung(tSession.getFehlerMeldung());
            abbruchService();
            return weEintrittskarteRC;
        }

        /* Prüfung ok - nun Ausführen, und dann Return besetzen */
        if (weEintrittskarte.getWeLoginVerify().getUser() == 2) {
            /* Transaktion überhaupt noch zulässig? */
            ergBool = tEintrittskarte.pruefeObZulaessig();
            if (ergBool == false) {
                abbruchService();
                weEintrittskarteRC.setRc(-99999);
                return weEintrittskarteRC;
            }
        }

        tWillenserklaerungSession.setQuelle(weEintrittskarte.quelle);

        ergBool = tEintrittskarte.anlegenAktionaerEK(eclDbM.getDbBundle(), false);

        if (ergBool == false) {
            weEintrittskarteRC.setRc(tSession.getFehlerCode());
            weEintrittskarteRC.setFehlerMeldung(tSession.getFehlerMeldung());
            abbruchService();
            return weEintrittskarteRC;
        } else {
            // weEintrittskarteRC.setAktienregisterEintragIdent(
            // aControllerEintrittskarte.getAktienregisterEintrag().aktienregisterIdent);
            weEintrittskarteRC.setEintrittskartePdfNr(tWillenserklaerungSession.getEintrittskartePdfNr());
            weEintrittskarteRC.setEintrittskartePdfNr2(tWillenserklaerungSession.getEintrittskartePdfNr2());
            weEintrittskarteRC.setZutrittsIdent(tWillenserklaerungSession.getZutrittsIdent());
            weEintrittskarteRC.setZutrittsIdentNeben(tWillenserklaerungSession.getZutrittsIdentNeben());
            weEintrittskarteRC.setZutrittsIdent2(tWillenserklaerungSession.getZutrittsIdent2());
            weEintrittskarteRC.setZutrittsIdentNeben2(tWillenserklaerungSession.getZutrittsIdentNeben2());
            weEintrittskarteRC.setRcWillenserklaerungIdentAusgefuehrt(
                    tWillenserklaerungSession.getRcWillenserklaerungIdentAusgefuehrt());
            weEintrittskarteRC.setRcWillenserklaerungIdentAusgefuehrtZweit(
                    tWillenserklaerungSession.getRcWillenserklaerungIdentAusgefuehrtZweit());
        }

        beendeService();
        return weEintrittskarteRC;
    }

    /************************
     * eintrittskartePrintGet*************************************************************
     * Liefert ein PDF zurück, das die Eintrittskarte (der übergebenen Nummer)
     * wiedergibt und direkt ausgedruckt werden kann (als PDF)
     */
    @POST
    @Path("eintrittskartePrintGet")
    @Produces({ MediaType.APPLICATION_JSON, MediaType.TEXT_XML })
    public WEEintrittskartePrintGetRC eintrittskartePrintGet(WEEintrittskartePrintGet weEintrittskartePrintGet) {

        WEEintrittskartePrintGetRC weEintrittskartePrintGetRC = new WEEintrittskartePrintGetRC();
        int rc = starteService(weEintrittskartePrintGet.getWeLoginVerify(), "eintrittskartePrintGet", true, true);
        if (rc < 1) {
            weEintrittskartePrintGetRC.setRc(rc);
            return weEintrittskartePrintGetRC;
        }
        if (rc == 2) {
            weEintrittskartePrintGetRC.setPruefeVersion(1);
        }

        weEintrittskartePrintGetRC.setPdfFormular(
                aControllerEintrittskarteQuittung.liefereDruckEintrittskarte(weEintrittskartePrintGet.getDateinr()));

        beendeService();
        return weEintrittskartePrintGetRC;
    }

    /*****************************
     * mailEK************************************************************ Mailen
     * einer bereits ausgestellten EK an eine zu übergebende E-Mail-Adresse. Derzeit
     * nicht weiter verfolgt! Diese Funktion ist für internen Gebrauch deshalb noch
     * nicht freigegeben.
     *
     */
    /*
     * TODO _Anmeldeablauf: Mailen bereits ausgestellter EK an Mailadresse -
     * irgendwann diese Funktion entweder fertig machen oder rausschmeißen
     */
    @POST
    @Path("mailEK")
    @Produces({ MediaType.APPLICATION_JSON, MediaType.TEXT_XML })
    public WEMailEKRC mailEK(WEMailEK weMailEK) {

        WEMailEKRC weMailEKRC = new WEMailEKRC();
        int rc = starteService(weMailEK.getWeLoginVerify(), "mailEK", true, true);
        if (rc < 1) {
            weMailEKRC.setRc(rc);
            return weMailEKRC;
        }
        if (rc == 2) {
            weMailEKRC.setPruefeVersion(1);
        }

        /*
         * In StarteService wurde fuelleEclZugeordneteMeldungListeM und damit
         * EclZugeordnetMeldungListeM gefüllt. Nun darin suchen, ob übergebene Parameter
         * darin enthalten sind - sonst Hack!
         */
        int datengefunden = -1;
        int anz = eclZugeordneteMeldungListeM.getAnzZugeordneteMeldungenEigeneAktienListeM();
        if (anz > 0) {
            for (int i = 0; i < anz; i++) {
                EclZugeordneteMeldungM lZugeordneteMeldungM = eclZugeordneteMeldungListeM
                        .getZugeordneteMeldungenEigeneAktienListeM().get(i);
                if (lZugeordneteMeldungM.isWillenserklaerungenVorhanden()) {
                    List<EclWillenserklaerungStatusM> zugeordneteWillenserklaerungenListM = lZugeordneteMeldungM
                            .getZugeordneteWillenserklaerungenListM();
                    int anzWillenserklaerungen = zugeordneteWillenserklaerungenListM.size();
                    for (int i1 = 0; i1 < anzWillenserklaerungen; i1++) {
                        EclWillenserklaerungStatusM lfdWillenserklaerung = zugeordneteWillenserklaerungenListM.get(i1);
                        if (!lfdWillenserklaerung.isStorniert() && lfdWillenserklaerung
                                .getWillenserklaerung() == KonstWillenserklaerung.neueZutrittsIdentZuMeldung) {
                            aDlgVariablen.setEintrittskarteEmail("");
                            aDlgVariablen.setEintrittskartePdfNr(-1);
                            // aControllerStatus.ekDetails(eclDbM.getDbBundle(), lZugeordneteMeldungM,
                            //         lfdWillenserklaerung);
                            if (aDlgVariablen.getEintrittskarteEmail().compareTo(weMailEK.getEintrittskarteEmail()) == 0
                                    && aDlgVariablen.getEintrittskartePdfNr() == weMailEK.getEintrittskartePdfNr()) {
                                datengefunden = 1;
                            }

                        }

                    }
                }
            }
        }

        eclDbM.closeAll();

        if (datengefunden == -1) { /* Dann Hack, oder Programmfehler, oder parallele Aktionen */
            weMailEKRC.setFehlerNr(CaFehler.pfNichtMoeglich);
            beendeServiceOhneClose();
            return weMailEKRC;
        }

        if (weMailEK.getEintrittskarteEmail() == null || weMailEK.getEintrittskarteEmail().isEmpty()
                || !CaString.isMailadresse(weMailEK.getEintrittskarteEmail())) {
            weMailEKRC.setFehlerNr(CaFehler.afKeineGueltigeEmailErsteEK);
            beendeServiceOhneClose();
            return weMailEKRC;
        }

        if (weMailEK.getEk() == 1) { /* Mail für erste EK */
            aDlgVariablen.setEintrittskartePdfNr(weMailEK.getEintrittskartePdfNr());
            aDlgVariablen.setEintrittskarteEmail(weMailEK.getEintrittskarteEmail());
            aControllerEintrittskarteQuittung.doDetailMailEintrittskarte();
            weMailEKRC.setFehlerNr(CaFehler.melEk1PerMailVerschickt);
        }

        if (weMailEK.getEk() == 2) { /* Mail für erste EK */
            aDlgVariablen.setEintrittskartePdfNr2(weMailEK.getEintrittskartePdfNr());
            aDlgVariablen.setEintrittskarteEmail2(weMailEK.getEintrittskarteEmail());
            aControllerEintrittskarteQuittung.doDetailMailEintrittskarte2();
            weMailEKRC.setFehlerNr(CaFehler.melEk1PerMailVerschickt);
        }

        beendeServiceOhneClose();
        return weMailEKRC;

    }

    /*****************************
     * mailGK************************************************************ Mailen
     * einer bereits ausgestellten GK an eine zu übergebende E-Mail-Adresse. Derzeit
     * nicht weiter verfolgt! Diese Funktion ist für internen Gebrauch deshalb noch
     * nicht freigegeben.
     *
     */
    /*
     * TODO _Anmeldeablauf: Mailen bereits ausgestellter GK an Mailadresse -
     * irgendwann diese Funktion entweder fertig machen oder rausschmeißen
     */
    @POST
    @Path("mailGK")
    @Produces({ MediaType.APPLICATION_JSON, MediaType.TEXT_XML })
    public WEMailGKRC mailGK(WEMailGK weMailGK) {

        WEMailGKRC weMailGKRC = new WEMailGKRC();
        int rc = starteService(weMailGK.getWeLoginVerify(), "mailGK", true, true);
        if (rc < 1) {
            weMailGKRC.setRc(rc);
            return weMailGKRC;
        }
        if (rc == 2) {
            weMailGKRC.setPruefeVersion(1);
        }

        if (weMailGK.getGastkarteEmail() == null || weMailGK.getGastkarteEmail().isEmpty()
                || !CaString.isMailadresse(weMailGK.getGastkarteEmail())) {
            weMailGKRC.setFehlerNr(CaFehler.afKeineGueltigeEmailErsteEK);
            weMailGKRC.setFehlerMeldung("Bitte geben Sie eine gültige Email-Adresse für die Gastkarte ein!");
            return weMailGKRC;
        }

        aDlgVariablen.setGastkartePdfNr(weMailGK.getGastkartePdfNr());
        aDlgVariablen.setGastkarteEmail(weMailGK.getGastkarteEmail());
        aControllerEintrittskarteQuittung.doDetailMailGastkarte();
        weMailGKRC.setFehlerNr(1);
        weMailGKRC.setFehlerMeldung("Gastkarte per Email verschickt!");

        beendeService();
        return weMailGKRC;
    }

    /*****************************
     * oeffentlicheIDPruefen************************************************************
     */
    /* TODO _Oeffentliche ID: Webschnittstelle derzeit nicht fertig */
    @POST
    @Path("oeffentlicheIDPruefen")
    @Produces({ MediaType.APPLICATION_JSON, MediaType.TEXT_XML })
    public WEOeffentlicheIDPruefenRC oeffentlicheIDPruefen(WEOeffentlicheIDPruefen weOeffentlicheIDPruefen) {

        WEOeffentlicheIDPruefenRC weOeffentlicheIDPruefenRC = new WEOeffentlicheIDPruefenRC();
        int rc = starteService(weOeffentlicheIDPruefen.getWeLoginVerify(), "oeffentlicheIDPruefen", true, true);
        if (rc < 1) {
            weOeffentlicheIDPruefenRC.setRc(rc);
            return weOeffentlicheIDPruefenRC;
        }
        if (rc == 2) {
            weOeffentlicheIDPruefenRC.setPruefeVersion(1);
        }

        aDlgVariablen.setAusgewaehlteAktion(weOeffentlicheIDPruefen.getAusgewaehlteAktion());
        aDlgVariablen.setAusgewaehlteHauptAktion(weOeffentlicheIDPruefen.getAusgewaehlteHauptAktion());
        aDlgVariablen.setZielOeffentlicheID(weOeffentlicheIDPruefen.getZielOeffentlicheID());
        if (weOeffentlicheIDPruefen.getEclZugeordneteMeldungM() != null) {
            eclZugeordneteMeldungM.copyFromMOhneStorno(weOeffentlicheIDPruefen.getEclZugeordneteMeldungM());
        }

        aControllerEintrittskarte.doOeffentlicheIDPruefen();

        if (aDlgVariablen.isUeberOeffentlicheID() == false) {
            weOeffentlicheIDPruefenRC.setRc(aDlgVariablen.getFehlerNr());
        }

        weOeffentlicheIDPruefenRC.setFehlerNr(aDlgVariablen.getFehlerNr());
        weOeffentlicheIDPruefenRC.setFehlerMeldung(aDlgVariablen.getFehlerMeldung());
        weOeffentlicheIDPruefenRC.setPersonNatJurOeffentlicheID(aDlgVariablen.getPersonNatJurOeffentlicheID());
        weOeffentlicheIDPruefenRC.setVollmachtName(aDlgVariablen.getVollmachtName());
        weOeffentlicheIDPruefenRC.setVollmachtVorname(aDlgVariablen.getVollmachtVorname());
        weOeffentlicheIDPruefenRC.setVollmachtOrt(aDlgVariablen.getVollmachtOrt());

        beendeService();
        return weOeffentlicheIDPruefenRC;
    }

    /*****************************
     * checkPersonengemeinschaft************************************************************
     */
    @POST
    @Path("checkPersonengemeinschaft")
    @Produces({ MediaType.APPLICATION_JSON, MediaType.TEXT_XML })
    public WECheckPersonengemeinschaftRC checkPersonengemeinschaft(
            WECheckPersonengemeinschaft weCheckPersonengemeinschaft) {

        WECheckPersonengemeinschaftRC weCheckPersonengemeinschaftRC = new WECheckPersonengemeinschaftRC();
        int rc = starteService(weCheckPersonengemeinschaft.getWeLoginVerify(), "checkPersonengemeinschaft", true, true);
        if (rc < 1) {
            weCheckPersonengemeinschaftRC.setRc(rc);
            return weCheckPersonengemeinschaftRC;
        }
        if (rc == 2) {
            weCheckPersonengemeinschaftRC.setPruefeVersion(1);
        }

        boolean ergBool = false;

        eclTeilnehmerLoginM.setAnmeldeAktionaersnummer(weCheckPersonengemeinschaft.getAnmeldeAktionaersnummer());

        ergBool = aControllerAnmelden.checkPersonengemeinschaft();
        eclDbM.closeAll();

        if (ergBool == false) {
            weCheckPersonengemeinschaftRC.setRc(aDlgVariablen.getFehlerNr());
        }

        weCheckPersonengemeinschaftRC.setFehlerNr(aDlgVariablen.getFehlerNr());
        weCheckPersonengemeinschaftRC.setFehlerMeldung(aDlgVariablen.getFehlerMeldung());

        weCheckPersonengemeinschaftRC.setVollmachtName(aDlgVariablen.getVollmachtName());
        weCheckPersonengemeinschaftRC.setVollmachtVorname(aDlgVariablen.getVollmachtVorname());
        weCheckPersonengemeinschaftRC.setVollmachtOrt(aDlgVariablen.getVollmachtOrt());

        weCheckPersonengemeinschaftRC.setVollmachtName2(aDlgVariablen.getVollmachtName2());
        weCheckPersonengemeinschaftRC.setVollmachtVorname2(aDlgVariablen.getVollmachtVorname2());
        weCheckPersonengemeinschaftRC.setVollmachtOrt2(aDlgVariablen.getVollmachtOrt2());

        beendeServiceOhneClose();
        return weCheckPersonengemeinschaftRC;
    }

    /*****************************
     * gastkarte************************************************************
     */
    /* TODO _Gastkarten: Web-Schnittstelle derzeit nicht fertig */
    @POST
    @Path("gastkarte")
    @Produces({ MediaType.APPLICATION_JSON, MediaType.TEXT_XML })
    public WEGastkarteRC gastkarte(WEGastkarte weGastkarte) {

        WEGastkarteRC weGastkarteRC = new WEGastkarteRC();
        int rc = starteService(weGastkarte.getWeLoginVerify(), "gastkarte", true, true);
        if (rc < 1) {
            weGastkarteRC.setRc(rc);
            return weGastkarteRC;
        }
        if (rc == 2) {
            weGastkarteRC.setPruefeVersion(1);
        }

        aDlgVariablen.setAusgewaehlteHauptAktion(weGastkarte.getAusgewaehlteHauptAktion());
        aDlgVariablen.setAusgewaehlteAktion(weGastkarte.getAusgewaehlteAktion());

        aDlgVariablen.setGastkarteName(weGastkarte.getGastkarteName());
        aDlgVariablen.setGastkarteVorname(weGastkarte.getGastkarteVorname());
        aDlgVariablen.setGastkarteOrt(weGastkarte.getGastkarteOrt());

        aDlgVariablen.setGastkarteVersandart(weGastkarte.getGastkarteVersandart());
        aDlgVariablen.setGastkarteNrPersNatJur(weGastkarte.getGastkarteNrPersNatJur());
        aDlgVariablen.setGastkarteEmail(weGastkarte.getGastkarteEmail());
        aDlgVariablen.setGastkarteEmailBestaetigen(weGastkarte.getGastkarteEmail());
        aDlgVariablen.setGastkarteAbweichendeAdresse1(weGastkarte.getGastkarteAbweichendeAdresse1());
        aDlgVariablen.setGastkarteAbweichendeAdresse2(weGastkarte.getGastkarteAbweichendeAdresse2());
        aDlgVariablen.setGastkarteAbweichendeAdresse3(weGastkarte.getGastkarteAbweichendeAdresse3());
        aDlgVariablen.setGastkarteAbweichendeAdresse4(weGastkarte.getGastkarteAbweichendeAdresse4());
        aDlgVariablen.setGastkarteAbweichendeAdresse5(weGastkarte.getGastkarteAbweichendeAdresse5());

        aControllerEintrittskarte.setAktienregisterEintrag(new EclAktienregister());

        EclAktienregister lAktienregisterEintrag = new EclAktienregister();

        if (eclTeilnehmerLoginM.getAnmeldeIdentAktienregister() != 0) {
            lAktienregisterEintrag.aktienregisterIdent = eclTeilnehmerLoginM.getAnmeldeIdentAktienregister();
            eclDbM.getDbBundle().dbAktienregister.leseZuAktienregisterEintrag(lAktienregisterEintrag);
            lAktienregisterEintrag = eclDbM.getDbBundle().dbAktienregister.ergebnisPosition(0);
            eclTeilnehmerLoginM.setAnmeldeAktionaersnummer(lAktienregisterEintrag.aktionaersnummer);
        }

        Boolean ergBool = aControllerEintrittskarte.pruefeEingabenFuerGastkarte();

        if (ergBool == false) {/* Prüfung fehlgeschlagen */
            weGastkarteRC.setRc(aDlgVariablen.getFehlerNr());
            weGastkarteRC.setFehlerMeldung(aDlgVariablen.getFehlerMeldung());
        } else {/* Prüfung ok - nun Ausführen, und dann Return besetzen */
            ergBool = aControllerEintrittskarte.anlegenGastkarte(lAktienregisterEintrag, eclDbM.getDbBundle());

            if (ergBool == false) {
                weGastkarteRC.setRc(aDlgVariablen.getFehlerNr());
                weGastkarteRC.setFehlerMeldung(aDlgVariablen.getFehlerMeldung());
            } else {
                weGastkarteRC.setGastkartePdfNr(aDlgVariablen.getGastkartePdfNr());
                weGastkarteRC.setGastkarteAbweichendeAdresse1(aDlgVariablen.getGastkarteAbweichendeAdresse1());
                weGastkarteRC.setGastkarteAbweichendeAdresse2(aDlgVariablen.getGastkarteAbweichendeAdresse2());
                weGastkarteRC.setGastkarteAbweichendeAdresse3(aDlgVariablen.getGastkarteAbweichendeAdresse3());
                weGastkarteRC.setGastkarteAbweichendeAdresse4(aDlgVariablen.getGastkarteAbweichendeAdresse4());
                weGastkarteRC.setGastkarteAbweichendeAdresse5(aDlgVariablen.getGastkarteAbweichendeAdresse5());
                weGastkarteRC.setGastkarteMeldeIdent(aDlgVariablen.getGastkarteMeldeIdent());
                weGastkarteRC.setGastkarteZutrittsIdent(aDlgVariablen.getGastkartenZutrittsIdent());
                weGastkarteRC.setGastkarteZutrittsIdentNeben(aDlgVariablen.getGastkartenZutrittsIdentNeben());
            }
            /* eclDbM.getDbBundle().dbBasis.endTransaction(); */
        }

        beendeService();
        return weGastkarteRC;
    }

    /************************************
     * abstimmungsListeGet**************************** Liest die Liste der aktuellen
     * Abstimmungen ein
     */
    @POST
    @Path("abstimmungsListeGet")
    @Produces({ MediaType.APPLICATION_JSON, MediaType.TEXT_XML })
    public WEAbstimmungsListeGetRC abstimmungsListeGet(WEAbstimmungsListeGet weAbstimmungsListeGet) {

        WEAbstimmungsListeGetRC weAbstimmungsListeGetRC = new WEAbstimmungsListeGetRC();
        int rc = starteService(weAbstimmungsListeGet.getWeLoginVerify(), "abstimmungsListeGet", false, true);
        if (rc < 1) {
            weAbstimmungsListeGetRC.setRc(rc);
            return weAbstimmungsListeGetRC;
        }
        if (rc == 2) {
            weAbstimmungsListeGetRC.setPruefeVersion(1);
        }

        aFunktionen.leseAbstimmungsliste(eclDbM.getDbBundle(), weAbstimmungsListeGet.gattung,
                weAbstimmungsListeGet.sicht, weAbstimmungsListeGet.skIst);

        weAbstimmungsListeGetRC.setAbstimmungenListeM(new EclAbstimmungenListeM());
        weAbstimmungsListeGetRC.getAbstimmungenListeM()
                .setAbstimmungenListeM(eclAbstimmungenListeM.getAbstimmungenListeM());
        weAbstimmungsListeGetRC.getAbstimmungenListeM()
                .setGegenantraegeListeM(eclAbstimmungenListeM.getGegenantraegeListeM());
        if (weAbstimmungsListeGet.weLoginVerify.getUser() == 2) {
            /* Für App: in App wird Kurzbezeichnung angezeigt ... */
            int laenge = weAbstimmungsListeGetRC.getAbstimmungenListeM().getAbstimmungenListeM().size();
            for (int i = 0; i < laenge; i++) {
                EclAbstimmungM lAbstimmungM = weAbstimmungsListeGetRC.getAbstimmungenListeM().getAbstimmungenListeM()
                        .get(i);
                lAbstimmungM.setAnzeigeBezeichnungKurz(lAbstimmungM.getAnzeigeBezeichnungLang());
            }
            laenge = weAbstimmungsListeGetRC.getAbstimmungenListeM().getGegenantraegeListeM().size();
            for (int i = 0; i < laenge; i++) {
                EclAbstimmungM lAbstimmungM = weAbstimmungsListeGetRC.getAbstimmungenListeM().getGegenantraegeListeM()
                        .get(i);
                lAbstimmungM.setAnzeigeBezeichnungKurz(lAbstimmungM.getAnzeigeBezeichnungLang());
            }
        }
        beendeService();
        return weAbstimmungsListeGetRC;
    }

    /**********************
     * weisungErteilen***************************************************** Erteilen
     * von Vollmacht/Weisung an SRV, Briefwahl
     */
    @POST
    @Path("weisungErteilen")
    @Produces({ MediaType.APPLICATION_JSON, MediaType.TEXT_XML })
    public WEWeisungErteilenRC weisungErteilen(WEWeisungErteilen weWeisungErteilen) {

        int i;
        WEWeisungErteilenRC weWeisungErteilenRC = new WEWeisungErteilenRC();
        int rc = starteService(weWeisungErteilen.getWeLoginVerify(), "weisungErteilen", true, true);
        if (rc < 1) {
            weWeisungErteilenRC.setRc(rc);
            return weWeisungErteilenRC;
        }
        if (rc == 2) {
            weWeisungErteilenRC.setPruefeVersion(1);
        }

        if (weWeisungErteilen.getWeLoginVerify().getUser() == 2) {
            if (weWeisungErteilen.getAnmeldeAktionaersnummer().compareTo(aktiveAktionaersnummer) != 0) {
                CaBug.drucke("!!!!!!!!!!!!!weisungErteilen - Hack: " + weWeisungErteilen.getAnmeldeAktionaersnummer()
                        + "/" + aktiveAktionaersnummer);
                abbruchService();
                weWeisungErteilenRC.setRc(-99999);
                return weWeisungErteilenRC;
            }

            if (weWeisungErteilen.getAusgewaehlteHauptAktion() == KonstPortalAktion.HAUPT_BEREITSANGEMELDET) {
                EclZugeordneteMeldungNeu lZugeordneteMeldung = pruefeMeldungZuAktionaerPassend(
                        weWeisungErteilen.getEclZugeordneteMeldung());
                if (lZugeordneteMeldung == null) {

                    CaBug.drucke("!!!!!!!!!!!!!weisungErteilen - Hack Meldung: " + aktiveAktionaersnummer);
                    abbruchService();
                    weWeisungErteilenRC.setRc(-99999);
                    return weWeisungErteilenRC;
                }
                weWeisungErteilen.setEclZugeordneteMeldung(lZugeordneteMeldung);
            }

        }

        tWillenserklaerungSession.setAusgewaehlteHauptAktion(weWeisungErteilen.getAusgewaehlteHauptAktion());
        tWillenserklaerungSession.setAusgewaehlteAktion(weWeisungErteilen.getAusgewaehlteAktion());

        /* Bei Alternative 2 eigentlich nicht mehr erforderlich */
        int lSicht = 0;
        if (weWeisungErteilen.getWeLoginVerify().getUser() == 2) {
            lSicht = KonstWeisungserfassungSicht.portalWeisungserfassung;
        } else {
            lSicht = KonstWeisungserfassungSicht.interneWeisungserfassung;
        }

        int lSkIst = 0;

        switch (tWillenserklaerungSession.getIntAusgewaehlteAktion()) {
        case KonstPortalAktion.SRV_NEU:
            lSkIst = KonstSkIst.srv;
            break;
        case KonstPortalAktion.BRIEFWAHL_NEU:
            lSkIst = KonstSkIst.briefwahl;
            break;
        case KonstPortalAktion.KIAV_MIT_WEISUNG_NEU:
            lSkIst = KonstSkIst.kiav;
            break;
        case KonstPortalAktion.DAUERVOLLMACHT_MIT_WEISUNG_NEU:
            lSkIst = KonstSkIst.dauervollmacht;
            break;
        case KonstPortalAktion.ORGANISATORISCH_MIT_WEISUNG_NEU:
            lSkIst = KonstSkIst.organisatorisch;
            break;
        }

        if (weWeisungErteilen.getWeLoginVerify().getUser() == KonstWebService.USER_APP) {
            /* Transaktion überhaupt noch zulässig? */
            if (tWeisungBestaetigung.pruefeObZulaessigErteilen() == false) {
                abbruchService();
                weWeisungErteilenRC.setRc(-99999);
                return weWeisungErteilenRC;
            }
        }

        if (weWeisungErteilen.getAusgewaehlteHauptAktion() == KonstPortalAktion.HAUPT_NEUANMELDUNG) {
            /*
             * Aktieneintrag suchen und eintragen, für den die Anmeldung ausgeführt werden
             * soll
             */
            EclBesitzAREintrag lBesitzAREintrag = pruefeAREintragZuAktionaerPassend(
                    weWeisungErteilen.getAnmeldeAktionaersnummer());
            if (lBesitzAREintrag == null) {
                CaBug.drucke("001 weWeisungErteilen.anmeldeAktionaersnummer="
                        + weWeisungErteilen.getAnmeldeAktionaersnummer());
            }
            tWillenserklaerungSession.initBesitzAREintragListe();
            tWillenserklaerungSession.addBesitzAREintragListe(lBesitzAREintrag);
            tWillenserklaerungSession.ermittleGattungenFuerBesitzAREintragListe();
        }

        if (weWeisungErteilen.getEclZugeordneteMeldung() != null) {
            tWillenserklaerungSession.initZugeordneteMeldungFuerAusfuehrungListe();
            tWillenserklaerungSession
                    .addZugeordneteMeldungFuerAusfuehrungListe(weWeisungErteilen.getEclZugeordneteMeldung(), null);
            tWillenserklaerungSession.ermittleGattungenFuerZugeordneteMeldungFuerAusfuehrungListe();
        }
        blMAbstimmung.leseWeisungsliste(tWillenserklaerungSession.getGattungVorhanden(), lSkIst, lSicht);
        blMAbstimmungsvorschlag.leseAbstimmungsvorschlagGesellschaft(eclDbM.getDbBundle());

        if (weWeisungErteilen.getWeLoginVerify().getUser() == KonstWebService.USER_APP) {/* App -> Dann Alternative 1 */
            CaBug.druckeLog("Alternative 1", logDrucken, 10);
            eclAbstimmungenListeM.setAlternative(1);
            eclAbstimmungenListeM.setWeisungMeldung(null);
            eclAbstimmungenListeM.setWeisungMeldungRaw(null);
            for (i = 0; i < eclAbstimmungenListeM.getAbstimmungenListeM().size(); i++) {
                eclAbstimmungenListeM.getAbstimmungenListeM().get(i)
                        .setGewaehlt(weWeisungErteilen.getWeisungenAgenda().get(i));
            }
            for (i = 0; i < eclAbstimmungenListeM.getGegenantraegeListeM().size(); i++) {
                eclAbstimmungenListeM.getGegenantraegeListeM().get(i)
                        .setGewaehlt(weWeisungErteilen.getWeisungenGegenantraege().get(i));
            }
        } else { /* Alternative 2 - fertig aufbereitete Weisungen */
            CaBug.druckeLog("Alternative 2", logDrucken, 10);
            eclAbstimmungenListeM.setAlternative(2);
            eclAbstimmungenListeM.setWeisungMeldung(weWeisungErteilen.getWeisungMeldung());
            eclAbstimmungenListeM.setWeisungMeldungRaw(weWeisungErteilen.getWeisungMeldungRaw());
        }

        if (weWeisungErteilen.getWeLoginVerify().getUser() == KonstWebService.USER_APP) {
            /* Nur bei App: maximale Stimmabgabe prüfen */

            List<EclAbstimmungM> lAbstimmungenListe = eclAbstimmungenListeM.getAbstimmungenListeM();
            boolean brc = tWeisung.pruefeAbstimmungsgruppen(lAbstimmungenListe);
            if (brc == false) {
                weWeisungErteilenRC.setRc(tSession.getFehlerCode());
                abbruchService();
                CaBug.druckeLog("Gruppenfehler!", logDrucken, 10);
                return weWeisungErteilenRC;
            }

        }

//        CaBug.druckeLog("--------------------------------------------------------------1------------", logDrucken, 10);
//        try {
//            Thread.sleep(5000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }


        tWillenserklaerungSession.setQuelle(weWeisungErteilen.getQuelle());
        tWillenserklaerungSession.setAbweichendeSammelkarte(weWeisungErteilen.getSammelIdent());
        boolean brc = tWeisungBestaetigung.erteilen(false);

        if (brc == false) {
            weWeisungErteilenRC.setRc(tSession.getFehlerCode());
        } else {
            weWeisungErteilenRC.setRc(1);
        }

        beendeService();
        return weWeisungErteilenRC;
    }

    /*************************************
     * kiavListeGet*********************************** Es werden die KIAVs
     * geliefert, die für weLoginVerify.eingabeQuelle freigegeben sind
     */
    @POST
    @Path("kiavListeGet")
    @Produces({ MediaType.APPLICATION_JSON, MediaType.TEXT_XML })
    public WEKIAVListeGetRC kiavListeGet(WEKIAVListeGet weKIAVListeGet) {

        WEKIAVListeGetRC weKIAVListeGetRC = new WEKIAVListeGetRC();
        int rc = starteService(weKIAVListeGet.getWeLoginVerify(), "kiavListeGet", false, true);
        if (rc < 1) {
            weKIAVListeGetRC.setRc(rc);
            return weKIAVListeGetRC;
        }
        if (rc == 2) {
            weKIAVListeGetRC.setPruefeVersion(1);
        }

        WELoginVerify lWELoginVerify = weKIAVListeGet.getWeLoginVerify();

        if (lWELoginVerify.getArbeitsplatz() != 9998) { /* Für App nicht ausführen! */
            aDlgVariablen.setAusgewaehlteAktion(weKIAVListeGet.getArt());
        } else {
            aDlgVariablen.setAusgewaehlteAktion("6"); /*
                                                       * TODO #APP Muß auf App-Seite noch angepaßt werden, damit auch
                                                       * Orga etc. funktionieren
                                                       */
        }

        aControllerAnmelden.leseKIAVListe(eclDbM.getDbBundle());

        weKIAVListeGetRC.setKiavListeM(new EclKIAVListeM());
        weKIAVListeGetRC.getKiavListeM().setKiavListeM(eclKIAVListeM.getKiavListeM());

        beendeService();
        return weKIAVListeGetRC;
    }

    /*******************************
     * kiavAbstimmvorschlagGet
     **************************************************/
    @POST
    @Path("kiavAbstimmvorschlagGet")
    @Produces({ MediaType.APPLICATION_JSON, MediaType.TEXT_XML })
    public WEKIAVAbstimmvorschlagGetRC kiavAbstimmvorschlagGet(WEKIAVAbstimmvorschlagGet weKIAVAbstimmvorschlagGet) {

        WEKIAVAbstimmvorschlagGetRC weKIAVAbstimmvorschlagGetRC = new WEKIAVAbstimmvorschlagGetRC();
        int rc = starteService(weKIAVAbstimmvorschlagGet.getWeLoginVerify(), "kiavAbstimmvorschlagGet", false, true);
        if (rc < 1) {
            weKIAVAbstimmvorschlagGetRC.setRc(rc);
            return weKIAVAbstimmvorschlagGetRC;
        }
        if (rc == 2) {
            weKIAVAbstimmvorschlagGetRC.setPruefeVersion(1);
        }

        eclKIAVM.copyFromM(weKIAVAbstimmvorschlagGet.getKiavm());

        int lSicht = KonstWeisungserfassungSicht.portalWeisungserfassung;
        int lSkIst = KonstSkIst.kiav;
        int lAktuelleGattung = eclKIAVM.getGattung();
        aFunktionen.leseAbstimmungsliste(eclDbM.getDbBundle(), lAktuelleGattung, lSicht, lSkIst);

        aControllerKIAVAuswahl.leseAbstimmungsvorschlag(eclDbM.getDbBundle());
        eclDbM.closeAll();

        if (eclKIAVM.getAbstimmungsVorschlagIdent() == 0) {
            weKIAVAbstimmvorschlagGetRC.setRc(CaFehler.pmKeinAbstimmvorschlagVorhanden);
        }
        weKIAVAbstimmvorschlagGetRC.setAbstimmungenListeM(new EclAbstimmungenListeM());
        weKIAVAbstimmvorschlagGetRC.getAbstimmungenListeM()
                .setAbstimmungenListeM(eclAbstimmungenListeM.getAbstimmungenListeM());
        weKIAVAbstimmvorschlagGetRC.getAbstimmungenListeM()
                .setGegenantraegeListeM(eclAbstimmungenListeM.getGegenantraegeListeM());

        beendeServiceOhneClose();
        return weKIAVAbstimmvorschlagGetRC;
    }

    /******************************************
     * kiavErteilen
     ***************************************************/
    @POST
    @Path("kiavErteilen")
    @Produces({ MediaType.APPLICATION_JSON, MediaType.TEXT_XML })
    public WEKIAVErteilenRC kiavErteilen(WEKIAVErteilen weKIAVErteilen) {

        int i;
        WEKIAVErteilenRC weKIAVErteilenRC = new WEKIAVErteilenRC();
        int rc = starteService(weKIAVErteilen.getWeLoginVerify(), "kiavErteilen", true, true);
        if (rc < 1) {
            weKIAVErteilenRC.setRc(rc);
            return weKIAVErteilenRC;
        }
        if (rc == 2) {
            weKIAVErteilenRC.setPruefeVersion(1);
        }

        eclTeilnehmerLoginM.setAnmeldeAktionaersnummer(weKIAVErteilen.getAnmeldeAktionaersnummer());

        if (weKIAVErteilen.getZugeordneteMeldungM() != null) {
            eclZugeordneteMeldungM.copyFromMOhneStorno(weKIAVErteilen.getZugeordneteMeldungM());
        }
        /*---*/
        eclKIAVM.copyFromM(weKIAVErteilen.getKiavM());

        aDlgVariablen.setAusgewaehlteHauptAktion(weKIAVErteilen.getAusgewaehlteHauptAktion());
        aDlgVariablen.setAusgewaehlteAktion(weKIAVErteilen.getAusgewaehlteAktion());

        int lSicht = 0;
        if (weKIAVErteilen.getWeLoginVerify().getUser() == 2) {
            lSicht = KonstWeisungserfassungSicht.portalWeisungserfassung;
        } else {
            lSicht = KonstWeisungserfassungSicht.interneWeisungserfassung;
        }

        int lSkIst = KonstSkIst.kiav;

        aFunktionen.leseAbstimmungsliste(eclDbM.getDbBundle(), eclTeilnehmerLoginM.getGattung(), lSicht, lSkIst);
        eclDbM.closeAll();

        for (i = 0; i < eclAbstimmungenListeM.getAbstimmungenListeM().size(); i++) {
            eclAbstimmungenListeM.getAbstimmungenListeM().get(i)
                    .setGewaehlt(weKIAVErteilen.getWeisungenAgenda().get(i));
        }
        for (i = 0; i < eclAbstimmungenListeM.getGegenantraegeListeM().size(); i++) {
            String h;
            h = weKIAVErteilen.getWeisungenGegenantraege().get(i);
            if (h.compareTo("X") == 0) {
                eclAbstimmungenListeM.getGegenantraegeListeM().get(i).setMarkiert(true);
            } else {
                eclAbstimmungenListeM.getGegenantraegeListeM().get(i).setMarkiert(false);
                eclAbstimmungenListeM.getGegenantraegeListeM().get(i)
                        .setGewaehlt(weKIAVErteilen.getWeisungenGegenantraege().get(i));

            }
        }

        aControllerKIAVBestaetigung.doErteilen();
        weKIAVErteilenRC.setRc(aDlgVariablen.getFehlerNr());
        weKIAVErteilenRC.setRcWillenserklaerungIdentAusgefuehrt(aDlgVariablen.getRcWillenserklaerungIdentAusgefuehrt());

        beendeServiceOhneClose();
        return weKIAVErteilenRC;
    }

    /* TODO _Oeffentliche ID: Web-Schnittstelle */
    @POST
    @Path("oeffentlicheIDSenden")
    @Produces({ MediaType.APPLICATION_JSON, MediaType.TEXT_XML })
    public WEOeffentlicheIDSendenRC oeffentlicheIDSenden(WEOeffentlicheIDSenden weOeffentlicheIDSenden) {

        WEOeffentlicheIDSendenRC weOeffentlicheIDSendenRC = new WEOeffentlicheIDSendenRC();
        int rc = starteService(weOeffentlicheIDSenden.getWeLoginVerify(), "oeffentlicheIDSenden", true, true);
        if (rc < 1) {
            weOeffentlicheIDSendenRC.setRc(rc);
            return weOeffentlicheIDSendenRC;
        }
        if (rc == 2) {
            weOeffentlicheIDSendenRC.setPruefeVersion(1);
        }

        eclDbM.closeAll();

        aControllerOeffentlicheID.setMailAdresse(weOeffentlicheIDSenden.getMailAdresse());
        aControllerOeffentlicheID.setMailBetreff(weOeffentlicheIDSenden.getMailBetreff());
        aControllerOeffentlicheID.setMailText(weOeffentlicheIDSenden.getMailText());

        aControllerOeffentlicheID.doSenden();
        weOeffentlicheIDSendenRC.setRc(1);

        beendeServiceOhneClose();
        return weOeffentlicheIDSendenRC;

    }

    /**************************
     * detailKarteGet
     **************************************/
    @POST
    @Path("detailKarteGet")
    @Produces({ MediaType.APPLICATION_JSON, MediaType.TEXT_XML })
    public WEDetailKarteGetRC detailKarteGet(WEDetailKarteGet wEDetailKarteGet) {

        WEDetailKarteGetRC weDetailKarteGetRC = new WEDetailKarteGetRC();
        int rc = starteService(wEDetailKarteGet.getWeLoginVerify(), "detailKarteGet", true, true);
        if (rc < 1) {
            weDetailKarteGetRC.setRc(rc);
            return weDetailKarteGetRC;
        }
        if (rc == 2) {
            weDetailKarteGetRC.setPruefeVersion(1);
        }

        /* Zum Testen fürs Hacken ... */
        // wEDetailKarteGet.getZugeordneteMeldungM().setMeldungsIdent(37);
        // wEDetailKarteGet.getEclWillenserklaerungStatusM().setWillenserklaerungIdent(345);

        if (wEDetailKarteGet.getWeLoginVerify().getUser() == 2) {
            /* Hack-Check */
            EclZugeordneteMeldungM lZugeordneteMeldungM = pruefeMeldungZuAktionaerPassend(
                    wEDetailKarteGet.getZugeordneteMeldungM());
            if (lZugeordneteMeldungM == null) {

                CaBug.drucke("!!!!!!!!!!!!!detailKarteGet - Hack Meldung: " + aktiveAktionaersnummer);
                abbruchService();
                weDetailKarteGetRC.setRc(-99999);
                return weDetailKarteGetRC;
            }
            wEDetailKarteGet.setZugeordneteMeldungM(lZugeordneteMeldungM);

            EclWillenserklaerungStatusM lWillenserklaerungStatusM = pruefeWillenserklaerungZuAktionaerPassend(
                    wEDetailKarteGet.getZugeordneteMeldungM(), wEDetailKarteGet.getEclWillenserklaerungStatusM());
            if (lWillenserklaerungStatusM == null) {

                CaBug.drucke("!!!!!!!!!!!!!detailKarteGet - Hack Willenserklärung: " + aktiveAktionaersnummer);
                abbruchService();
                weDetailKarteGetRC.setRc(-99999);
                return weDetailKarteGetRC;
            }
            wEDetailKarteGet.setEclWillenserklaerungStatusM(lWillenserklaerungStatusM);

            // System.out.println("Pruefen ok!");
        }

        aDlgVariablen.setAusgewaehlteHauptAktion(wEDetailKarteGet.getAusgewaehlteHauptAktion());
        aDlgVariablen.setAusgewaehlteAktion(wEDetailKarteGet.getAusgewaehlteAktion());
        eclDbM.closeAll();

        // aControllerStatus.doDetails(wEDetailKarteGet.getZugeordneteMeldungM(),
        //         wEDetailKarteGet.getEclWillenserklaerungStatusM());

        weDetailKarteGetRC.setAusgewaehlteAktion(aDlgVariablen.getAusgewaehlteAktion());
        weDetailKarteGetRC.setArtEintrittskarte(aDlgVariablen.getArtEintrittskarte());
        weDetailKarteGetRC.setEintrittskarteVersandart(aDlgVariablen.getEintrittskarteVersandart());
        weDetailKarteGetRC.setEintrittskarteAbweichendeAdresse1(aDlgVariablen.getEintrittskarteAbweichendeAdresse1());
        weDetailKarteGetRC.setEintrittskarteAbweichendeAdresse2(aDlgVariablen.getEintrittskarteAbweichendeAdresse2());
        weDetailKarteGetRC.setEintrittskarteAbweichendeAdresse3(aDlgVariablen.getEintrittskarteAbweichendeAdresse3());
        weDetailKarteGetRC.setEintrittskarteAbweichendeAdresse4(aDlgVariablen.getEintrittskarteAbweichendeAdresse4());
        weDetailKarteGetRC.setEintrittskarteAbweichendeAdresse5(aDlgVariablen.getEintrittskarteAbweichendeAdresse5());
        weDetailKarteGetRC.setEintrittskarteEmail(aDlgVariablen.getEintrittskarteEmail());
        weDetailKarteGetRC.setVollmachtName(aDlgVariablen.getVollmachtName());
        weDetailKarteGetRC.setVollmachtVorname(aDlgVariablen.getVollmachtVorname());
        weDetailKarteGetRC.setVollmachtOrt(aDlgVariablen.getVollmachtOrt());
        weDetailKarteGetRC.setEintrittskartePdfNr(aDlgVariablen.getEintrittskartePdfNr());
        weDetailKarteGetRC.setGastM(new EclGastM());
        weDetailKarteGetRC.getGastM().copyFromM(eclGastM);
        weDetailKarteGetRC.setIdentMasterGast(aDlgVariablen.getIdentMasterGast());
        weDetailKarteGetRC.setGruppenausstellung(aDlgVariablen.isGruppenausstellung());

        beendeServiceOhneClose();
        return weDetailKarteGetRC;
    }

    /*************************************************************************
     * liefert alle Infos, die für eine "Bestätigungsseite der Stornierung"
     * erforderlich sind, also z.B. auch die aktuellen Weisungen, falls es eine
     * Stornierung einer Vollmacht/Weisung o.ä. geht
     *
     * @param weStornierenVorbereitenGet
     * @return
     */
    @POST
    @Path("stornierenVorbereitenGet")
    @Produces({ MediaType.APPLICATION_JSON, MediaType.TEXT_XML })
    public WEStornierenVorbereitenGetRC stornierenVorbereitenGet(
            WEStornierenVorbereitenGet weStornierenVorbereitenGet) {

        WEStornierenVorbereitenGetRC weStornierenVorbereitenGetRC = new WEStornierenVorbereitenGetRC();
        int rc = starteService(weStornierenVorbereitenGet.getWeLoginVerify(), "stornierenVorbereitenGet", true, true);
        if (rc < 1) {
            weStornierenVorbereitenGetRC.setRc(rc);
            return weStornierenVorbereitenGetRC;
        }
        if (rc == 2) {
            weStornierenVorbereitenGetRC.setPruefeVersion(1);
        }

        if (weStornierenVorbereitenGet.getWeLoginVerify().getUser() == 2) {

            EclZugeordneteMeldungNeu lZugeordneteMeldung = pruefeMeldungZuAktionaerPassend(
                    weStornierenVorbereitenGet.getZugeordneteMeldung());
            if (lZugeordneteMeldung == null) {

                CaBug.drucke("!!!!!!!!!!!!!weisungErteilen - Hack Meldung: " + aktiveAktionaersnummer);
                abbruchService();
                weStornierenVorbereitenGetRC.setRc(-99999);
                return weStornierenVorbereitenGetRC;
            }
            weStornierenVorbereitenGet.setZugeordneteMeldung(lZugeordneteMeldung);

            EclWillenserklaerungStatusNeu lWillenserklaerungStatus = pruefeWillenserklaerungZuAktionaerPassend(
                    weStornierenVorbereitenGet.getZugeordneteMeldung(),
                    weStornierenVorbereitenGet.getEclWillenserklaerungStatus());
            if (lWillenserklaerungStatus == null) {

                CaBug.drucke(
                        "!!!!!!!!!!!!!stornierenVorbereitenGet - Hack Willenserklärung: " + aktiveAktionaersnummer);
                abbruchService();
                weStornierenVorbereitenGetRC.setRc(-99999);
                return weStornierenVorbereitenGetRC;
            }
            weStornierenVorbereitenGet.setEclWillenserklaerungStatus(lWillenserklaerungStatus);
        }

        tWillenserklaerungSession.setAusgewaehlteHauptAktion(weStornierenVorbereitenGet.getAusgewaehlteHauptAktion());
        tWillenserklaerungSession.setAusgewaehlteAktion(weStornierenVorbereitenGet.getAusgewaehlteAktion());

        int lSichtart = 0;
        if (weStornierenVorbereitenGet.getWeLoginVerify().getUser() == 2) {
            lSichtart = KonstWeisungserfassungSicht.portalWeisungserfassung;
        } else {
            lSichtart = KonstWeisungserfassungSicht.interneWeisungserfassung;
        }

        tAuswahl.stornierenAusfuehren(weStornierenVorbereitenGet.getZugeordneteMeldung(),
                weStornierenVorbereitenGet.getEclWillenserklaerungStatus(), lSichtart);

        weStornierenVorbereitenGetRC.setAusgewaehlteAktion(tWillenserklaerungSession.getIntAusgewaehlteAktion());

        weStornierenVorbereitenGetRC.setMeldungsIdent(tWillenserklaerungSession.getMeldungsIdentListe().get(0));

        weStornierenVorbereitenGetRC.setWeisungenSind(tWillenserklaerungSession.getWeisungenSindListe().get(0));
        weStornierenVorbereitenGetRC
                .setWillenserklaerungIdent(tWillenserklaerungSession.getWillenserklaerungIdentListe().get(0));
        weStornierenVorbereitenGetRC.setSammelIdent(tWillenserklaerungSession.getSammelIdentListe().get(0));
        weStornierenVorbereitenGetRC.setWeisungsIdent(tWillenserklaerungSession.getWeisungsIdentListe().get(0));

        weStornierenVorbereitenGetRC.setAbstimmungenListeM(new EclAbstimmungenListeM());
        weStornierenVorbereitenGetRC.getAbstimmungenListeM()
                .setAbstimmungenListeM(eclAbstimmungenListeM.getAbstimmungenListeM());
        weStornierenVorbereitenGetRC.getAbstimmungenListeM()
                .setGegenantraegeListeM(eclAbstimmungenListeM.getGegenantraegeListeM());

        weStornierenVorbereitenGetRC.setKiavM(new EclKIAVM());
        eclKIAVM.copyToM(weStornierenVorbereitenGetRC.getKiavM());
        weStornierenVorbereitenGetRC.getKiavM().copyFromM(eclKIAVM);

        beendeService();
        return weStornierenVorbereitenGetRC;
    }

    /************************************
     * eintrittskarteStornierenGet
     **********************/
    @POST
    @Path("eintrittskarteStornierenGet")
    @Produces({ MediaType.APPLICATION_JSON, MediaType.TEXT_XML })
    public WEEintrittskarteStornierenGetRC eintrittskarteStornierenGet(
            WEEintrittskarteStornierenGet weEintrittskarteStornierenGet) {

        WEEintrittskarteStornierenGetRC weEintrittskarteStornierenGetRC = new WEEintrittskarteStornierenGetRC();
        int rc = starteService(weEintrittskarteStornierenGet.getWeLoginVerify(), "eintrittskarteStornierenGet", true,
                true);
        if (rc < 1) {
            weEintrittskarteStornierenGetRC.setRc(rc);
            return weEintrittskarteStornierenGetRC;
        }
        if (rc == 2) {
            weEintrittskarteStornierenGetRC.setPruefeVersion(1);
        }

        if (weEintrittskarteStornierenGet.getWeLoginVerify().getUser() == 2) {

            EclZugeordneteMeldungNeu lZugeordneteMeldung = pruefeMeldungZuAktionaerPassend(
                    weEintrittskarteStornierenGet.getZugeordneteMeldung());
            if (lZugeordneteMeldung == null) {

                CaBug.drucke("!!!!!!!!!!!!!weisungErteilen - Hack Meldung: " + aktiveAktionaersnummer);
                abbruchService();
                weEintrittskarteStornierenGetRC.setRc(-99999);
                return weEintrittskarteStornierenGetRC;
            }
            weEintrittskarteStornierenGet.setZugeordneteMeldung(lZugeordneteMeldung);

            EclWillenserklaerungStatusNeu lWillenserklaerungStatus = pruefeWillenserklaerungZuAktionaerPassend(
                    weEintrittskarteStornierenGet.getZugeordneteMeldung(),
                    weEintrittskarteStornierenGet.getEclWillenserklaerungStatus());
            if (lWillenserklaerungStatus == null) {

                CaBug.drucke(
                        "!!!!!!!!!!!!!stornierenVorbereitenGet - Hack Willenserklärung: " + aktiveAktionaersnummer);
                abbruchService();
                weEintrittskarteStornierenGetRC.setRc(-99999);
                return weEintrittskarteStornierenGetRC;
            }
            weEintrittskarteStornierenGet.setEclWillenserklaerungStatus(lWillenserklaerungStatus);
        }

        tWillenserklaerungSession
                .setAusgewaehlteHauptAktion(weEintrittskarteStornierenGet.getAusgewaehlteHauptAktion());
        tWillenserklaerungSession.setAusgewaehlteAktion(weEintrittskarteStornierenGet.getAusgewaehlteAktion());

        if (weEintrittskarteStornierenGet.getWeLoginVerify().getUser() == 2) {
            /* Transaktion überhaupt noch zulässig? */
            if (!tEintrittskarteStornieren.pruefenObStornierungZulaessig()) {
                abbruchService();
                weEintrittskarteStornierenGetRC.setRc(-99999);
                return weEintrittskarteStornierenGetRC;
            }
        }

        tWillenserklaerungSession.initZugeordneteMeldungFuerAusfuehrungListe();
        tWillenserklaerungSession.addZugeordneteMeldungFuerAusfuehrungListe(
                weEintrittskarteStornierenGet.getZugeordneteMeldung(),
                weEintrittskarteStornierenGet.getEclWillenserklaerungStatus());

        boolean brc = tEintrittskarteStornieren.stornierenAusfuehren(false);

        if (brc == false) {
            weEintrittskarteStornierenGetRC.setRc(tSession.getFehlerCode());
        } else {
            weEintrittskarteStornierenGetRC.setRc(1);
        }

        beendeService();
        return weEintrittskarteStornierenGetRC;
    }

    /*****************************
     * weisungStornierenGet
     *************************************/
    @POST
    @Path("weisungStornierenGet")
    @Produces({ MediaType.APPLICATION_JSON, MediaType.TEXT_XML })
    public WEWeisungStornierenGetRC weisungStornierenGet(WEWeisungStornierenGet weWeisungStornierenGet) {

        WEWeisungStornierenGetRC weWeisungStornierenGetRC = new WEWeisungStornierenGetRC();
        int rc = starteService(weWeisungStornierenGet.getWeLoginVerify(), "weisungStornierenGet", true, true);
        if (rc < 1) {
            weWeisungStornierenGetRC.setRc(rc);
            return weWeisungStornierenGetRC;
        }
        if (rc == 2) {
            weWeisungStornierenGetRC.setPruefeVersion(1);
        }

        if (weWeisungStornierenGet.getWeLoginVerify().getUser() == 2) {

            EclZugeordneteMeldungNeu lZugeordneteMeldung = pruefeMeldungZuAktionaerPassend(
                    weWeisungStornierenGet.getZugeordneteMeldung());
            if (lZugeordneteMeldung == null) {

                CaBug.drucke("!!!!!!!!!!!!!weisungErteilen - Hack Meldung: " + aktiveAktionaersnummer);
                abbruchService();
                weWeisungStornierenGetRC.setRc(-99999);
                return weWeisungStornierenGetRC;
            }
            weWeisungStornierenGet.setZugeordneteMeldung(lZugeordneteMeldung);

            EclWillenserklaerungStatusNeu lWillenserklaerungStatus = pruefeWillenserklaerungZuAktionaerPassend(
                    weWeisungStornierenGet.getZugeordneteMeldung(),
                    weWeisungStornierenGet.getEclWillenserklaerungStatus());
            if (lWillenserklaerungStatus == null) {

                CaBug.drucke(
                        "!!!!!!!!!!!!!stornierenVorbereitenGet - Hack Willenserklärung: " + aktiveAktionaersnummer);
                abbruchService();
                weWeisungStornierenGetRC.setRc(-99999);
                return weWeisungStornierenGetRC;
            }
            weWeisungStornierenGet.setEclWillenserklaerungStatus(lWillenserklaerungStatus);
        }

        tWillenserklaerungSession.setAusgewaehlteHauptAktion(weWeisungStornierenGet.getAusgewaehlteHauptAktion());
        tWillenserklaerungSession.setAusgewaehlteAktion(weWeisungStornierenGet.getAusgewaehlteAktion());

        if (weWeisungStornierenGet.getWeLoginVerify().getUser() == 2) {
            /* Transaktion überhaupt noch zulässig? */
            if (!tWeisungStornieren.pruefenWeisungStornierenZulaessig()) {
                abbruchService();
                weWeisungStornierenGetRC.setRc(-99999);
                return weWeisungStornierenGetRC;
            }
        }

        tWillenserklaerungSession.initZugeordneteMeldungFuerAusfuehrungListe();
        tWillenserklaerungSession.addZugeordneteMeldungFuerAusfuehrungListe(
                weWeisungStornierenGet.getZugeordneteMeldung(), weWeisungStornierenGet.getEclWillenserklaerungStatus());

        tWillenserklaerungSession.setLastElementOfMeldungsIdentListe(weWeisungStornierenGet.getMeldungsIdent());
        tWillenserklaerungSession
                .setLastElementOfWillenserklaerungIdentListe(weWeisungStornierenGet.getWillenserklaerungIdent());
        tWillenserklaerungSession.setLastElementOfSammelIdentListe(weWeisungStornierenGet.getSammelIdent());
//        tWillenserklaerungSession.setLastElementOfWeisungsIdentListe(weWeisungStornierenGet.getWeisungsIdent());

        boolean brc = tWeisungStornieren.stornierenAusfuehren(false);

        if (brc == false) {
            weWeisungStornierenGetRC.setRc(tSession.getFehlerCode());
        } else {
            weWeisungStornierenGetRC.setRc(1);
        }

        beendeService();
        return weWeisungStornierenGetRC;
    }

    /***********************************
     * vollmachtDritteStornierenGet
     **************************************************/
    @POST
    @Path("vollmachtDritteStornierenGet")
    @Produces({ MediaType.APPLICATION_JSON, MediaType.TEXT_XML })
    public WEVollmachtDritteStornierenGetRC vollmachtDritteStornierenGet(
            WEVollmachtDritteStornierenGet weVollmachtDritteStornierenGet) {

        /* Nahezu identisch zu eintrittskarteStornierenGet */
        WEVollmachtDritteStornierenGetRC weVollmachtDritteStornierenGetRC = new WEVollmachtDritteStornierenGetRC();
        int rc = starteService(weVollmachtDritteStornierenGet.getWeLoginVerify(), "vollmachtDritteStornierenGet", true,
                true);
        if (rc < 1) {
            weVollmachtDritteStornierenGetRC.setRc(rc);
            return weVollmachtDritteStornierenGetRC;
        }
        if (rc == 2) {
            weVollmachtDritteStornierenGetRC.setPruefeVersion(1);
        }

        if (weVollmachtDritteStornierenGet.getWeLoginVerify().getUser() == 2) {

            EclZugeordneteMeldungNeu lZugeordneteMeldung = pruefeMeldungZuAktionaerPassend(
                    weVollmachtDritteStornierenGet.getZugeordneteMeldung());
            if (lZugeordneteMeldung == null) {

                CaBug.drucke("!!!!!!!!!!!!!weisungErteilen - Hack Meldung: " + aktiveAktionaersnummer);
                abbruchService();
                weVollmachtDritteStornierenGetRC.setRc(-99999);
                return weVollmachtDritteStornierenGetRC;
            }
            weVollmachtDritteStornierenGet.setZugeordneteMeldung(lZugeordneteMeldung);

            EclWillenserklaerungStatusNeu lWillenserklaerungStatus = pruefeWillenserklaerungZuAktionaerPassend(
                    weVollmachtDritteStornierenGet.getZugeordneteMeldung(),
                    weVollmachtDritteStornierenGet.getEclWillenserklaerungStatus());
            if (lWillenserklaerungStatus == null) {

                CaBug.drucke(
                        "!!!!!!!!!!!!!stornierenVorbereitenGet - Hack Willenserklärung: " + aktiveAktionaersnummer);
                abbruchService();
                weVollmachtDritteStornierenGetRC.setRc(-99999);
                return weVollmachtDritteStornierenGetRC;
            }
            weVollmachtDritteStornierenGet.setEclWillenserklaerungStatus(lWillenserklaerungStatus);
        }

        tWillenserklaerungSession
                .setAusgewaehlteHauptAktion(weVollmachtDritteStornierenGet.getAusgewaehlteHauptAktion());
        tWillenserklaerungSession.setAusgewaehlteAktion(weVollmachtDritteStornierenGet.getAusgewaehlteAktion());

        tWillenserklaerungSession.initZugeordneteMeldungFuerAusfuehrungListe();
        tWillenserklaerungSession.addZugeordneteMeldungFuerAusfuehrungListe(
                weVollmachtDritteStornierenGet.getZugeordneteMeldung(),
                weVollmachtDritteStornierenGet.getEclWillenserklaerungStatus());

        boolean brc = tVollmachtDritteStornieren.stornierenAusfuehren(false);

        if (brc == false) {
            weVollmachtDritteStornierenGetRC.setRc(tSession.getFehlerCode());
        } else {
            weVollmachtDritteStornierenGetRC.setRc(1);
        }

        beendeService();
        return weVollmachtDritteStornierenGetRC;
    }

    /*********************************************
     * weisungAendernVorbereitenGet
     ***************************************************/
    @POST
    @Path("weisungAendernVorbereitenGet")
    @Produces({ MediaType.APPLICATION_JSON, MediaType.TEXT_XML })
    public WEWeisungAendernVorbereitenGetRC weisungAendernVorbereitenGet(
            WEWeisungAendernVorbereitenGet weWeisungAendernVorbereitenGet) {

        WEWeisungAendernVorbereitenGetRC weWeisungAendernVorbereitenGetRC = new WEWeisungAendernVorbereitenGetRC();
        int rc = starteService(weWeisungAendernVorbereitenGet.getWeLoginVerify(), "weisungAendernVorbereitenGet", true,
                true);
        if (rc < 1) {
            weWeisungAendernVorbereitenGetRC.setRc(rc);
            return weWeisungAendernVorbereitenGetRC;
        }
        if (rc == 2) {
            weWeisungAendernVorbereitenGetRC.setPruefeVersion(1);
        }

        if (weWeisungAendernVorbereitenGet.getWeLoginVerify().getUser() == 2) {

            EclZugeordneteMeldungNeu lZugeordneteMeldung = pruefeMeldungZuAktionaerPassend(
                    weWeisungAendernVorbereitenGet.getZugeordneteMeldung());
            if (lZugeordneteMeldung == null) {

                CaBug.drucke("!!!!!!!!!!!!!weisungErteilen - Hack Meldung: " + aktiveAktionaersnummer);
                abbruchService();
                weWeisungAendernVorbereitenGetRC.setRc(-99999);
                return weWeisungAendernVorbereitenGetRC;
            }
            weWeisungAendernVorbereitenGet.setZugeordneteMeldung(lZugeordneteMeldung);

            EclWillenserklaerungStatusNeu lWillenserklaerungStatus = pruefeWillenserklaerungZuAktionaerPassend(
                    weWeisungAendernVorbereitenGet.getZugeordneteMeldung(),
                    weWeisungAendernVorbereitenGet.getEclWillenserklaerungStatus());
            if (lWillenserklaerungStatus == null) {

                CaBug.drucke(
                        "!!!!!!!!!!!!!stornierenVorbereitenGet - Hack Willenserklärung: " + aktiveAktionaersnummer);
                abbruchService();
                weWeisungAendernVorbereitenGetRC.setRc(-99999);
                return weWeisungAendernVorbereitenGetRC;
            }
            weWeisungAendernVorbereitenGet.setEclWillenserklaerungStatus(lWillenserklaerungStatus);
        }

        tWillenserklaerungSession
                .setAusgewaehlteHauptAktion(weWeisungAendernVorbereitenGet.getAusgewaehlteHauptAktion());
        tWillenserklaerungSession.setAusgewaehlteAktion(weWeisungAendernVorbereitenGet.getAusgewaehlteAktion());

        int lSichtart = 0;
        if (weWeisungAendernVorbereitenGet.getWeLoginVerify().getUser() == 2) {
            lSichtart = KonstWeisungserfassungSicht.portalWeisungserfassung;
        } else {
            lSichtart = KonstWeisungserfassungSicht.interneWeisungserfassung;
        }

        tAuswahl.aendernAusfuehren(weWeisungAendernVorbereitenGet.getZugeordneteMeldung(),
                weWeisungAendernVorbereitenGet.getEclWillenserklaerungStatus(), lSichtart);

        weWeisungAendernVorbereitenGetRC.setMeldungsIdent(tWillenserklaerungSession.getMeldungsIdentListe().get(0));

        weWeisungAendernVorbereitenGetRC
                .setWillenserklaerungIdent(tWillenserklaerungSession.getWillenserklaerungIdentListe().get(0));
        weWeisungAendernVorbereitenGetRC.setSammelIdent(tWillenserklaerungSession.getSammelIdentListe().get(0));
        weWeisungAendernVorbereitenGetRC.setWeisungsIdent(tWillenserklaerungSession.getWeisungsIdentListe().get(0));

        weWeisungAendernVorbereitenGetRC.setAbstimmungenListeM(new EclAbstimmungenListeM());
        weWeisungAendernVorbereitenGetRC.getAbstimmungenListeM()
                .setAbstimmungenListeM(eclAbstimmungenListeM.getAbstimmungenListeM());
        weWeisungAendernVorbereitenGetRC.getAbstimmungenListeM()
                .setGegenantraegeListeM(eclAbstimmungenListeM.getGegenantraegeListeM());

        weWeisungAendernVorbereitenGetRC.setWeisungMeldung(eclAbstimmungenListeM.getWeisungMeldung());
        weWeisungAendernVorbereitenGetRC.setWeisungMeldungRaw(eclAbstimmungenListeM.getWeisungMeldungRaw());

        // for (int i=1;i<20;i++) {
        // System.out.println("WAIntern i="+i+"
        // "+weWeisungAendernVorbereitenGetRC.getWeisungMeldung().abgabe[i]);
        // }
        weWeisungAendernVorbereitenGetRC.setKiavM(new EclKIAVM());
        eclKIAVM.copyToM(weWeisungAendernVorbereitenGetRC.getKiavM());

        beendeService();
        return weWeisungAendernVorbereitenGetRC;
    }

    /**************************************
     * weisungAendernGet
     ********************************************/
    @POST
    @Path("weisungAendernGet")
    @Produces({ MediaType.APPLICATION_JSON, MediaType.TEXT_XML })
    public WEWeisungAendernGetRC weisungAendernGet(WEWeisungAendernGet weWeisungAendernGet) {

        WEWeisungAendernGetRC weWeisungAendernGetRC = new WEWeisungAendernGetRC();
        int rc = starteService(weWeisungAendernGet.getWeLoginVerify(), "weisungAendernGet", true, true);
        if (rc < 1) {
            weWeisungAendernGetRC.setRc(rc);
            return weWeisungAendernGetRC;
        }
        if (rc == 2) {
            weWeisungAendernGetRC.setPruefeVersion(1);
        }

        if (weWeisungAendernGet.getWeLoginVerify().getUser() == 2) {

            EclZugeordneteMeldungNeu lZugeordneteMeldung = pruefeMeldungZuAktionaerPassend(
                    weWeisungAendernGet.eclZugeordneteMeldung);
            if (lZugeordneteMeldung == null) {

                CaBug.drucke("!!!!!!!!!!!!!weisungErteilen - Hack Meldung: " + aktiveAktionaersnummer);
                abbruchService();
                weWeisungAendernGetRC.setRc(-99999);
                return weWeisungAendernGetRC;
            }
            weWeisungAendernGet.eclZugeordneteMeldung = lZugeordneteMeldung;

            EclWillenserklaerungStatusNeu lWillenserklaerungStatus = pruefeWillenserklaerungZuAktionaerPassend(
                    weWeisungAendernGet.eclZugeordneteMeldung, weWeisungAendernGet.eclWillenserklaerungStatus);
            if (lWillenserklaerungStatus == null) {

                CaBug.drucke(
                        "!!!!!!!!!!!!!stornierenVorbereitenGet - Hack Willenserklärung: " + aktiveAktionaersnummer);
                abbruchService();
                weWeisungAendernGetRC.setRc(-99999);
                return weWeisungAendernGetRC;
            }
            weWeisungAendernGet.eclWillenserklaerungStatus = lWillenserklaerungStatus;
        }

        tWillenserklaerungSession.setAusgewaehlteHauptAktion(weWeisungAendernGet.ausgewaehlteHauptAktion);
        tWillenserklaerungSession.setAusgewaehlteAktion(weWeisungAendernGet.ausgewaehlteAktion);

        if (weWeisungAendernGet.getWeLoginVerify().getUser() == 2) {
            /* Transaktion überhaupt noch zulässig? */
            if (!tWeisungBestaetigung.pruefeObZulaessigErteilen()) {
                abbruchService();
                weWeisungAendernGetRC.setRc(-99999);
                return weWeisungAendernGetRC;
            }
        }

        /* Bei Alternative 2 eigentlich nicht mehr erforderlich */

        int lSicht = 0;
        if (weWeisungAendernGet.getWeLoginVerify().getUser() == 2) {
            lSicht = KonstWeisungserfassungSicht.portalWeisungserfassung;
        } else {
            lSicht = KonstWeisungserfassungSicht.interneWeisungserfassung;
        }

        int lSkIst = 0;
        switch (tWillenserklaerungSession.getIntAusgewaehlteAktion()) {
        case KonstPortalAktion.SRV_AENDERN:
            lSkIst = KonstSkIst.srv;
            break;
        case KonstPortalAktion.BRIEFWAHL_AENDERN:
            lSkIst = KonstSkIst.briefwahl;
            break;
        case KonstPortalAktion.KIAV_WEISUNG_AENDERN:
            lSkIst = KonstSkIst.kiav;
            break;
        }

        tWillenserklaerungSession.initZugeordneteMeldungFuerAusfuehrungListe();
        tWillenserklaerungSession.addZugeordneteMeldungFuerAusfuehrungListe(weWeisungAendernGet.eclZugeordneteMeldung,
                weWeisungAendernGet.eclWillenserklaerungStatus);
        tWillenserklaerungSession.ermittleGattungenFuerZugeordneteMeldungFuerAusfuehrungListe();

        tWillenserklaerungSession.setLastElementOfMeldungsIdentListe(weWeisungAendernGet.getMeldungsIdent());
        tWillenserklaerungSession
                .setLastElementOfWillenserklaerungIdentListe(weWeisungAendernGet.getWillenserklaerungIdent());
        tWillenserklaerungSession.setLastElementOfSammelIdentListe(weWeisungAendernGet.getSammelIdent());

        tWillenserklaerungSession.setLastElementOfWeisungsIdentListe(weWeisungAendernGet.getWeisungsIdent());

        blMAbstimmung.leseWeisungsliste(tWillenserklaerungSession.getGattungVorhanden(), lSkIst, lSicht);
        blMAbstimmungsvorschlag.leseAbstimmungsvorschlagGesellschaft(eclDbM.getDbBundle());

        if (weWeisungAendernGet.getWeLoginVerify().getUser() == 2) {/* App -> Dann Alternative 1 */
            CaBug.druckeLog("Alternative 1", logDrucken, 10);
            eclAbstimmungenListeM.setAlternative(1);
            eclAbstimmungenListeM.setWeisungMeldung(null);
            eclAbstimmungenListeM.setWeisungMeldungRaw(null);
            for (int i = 0; i < eclAbstimmungenListeM.getAbstimmungenListeM().size(); i++) {
                eclAbstimmungenListeM.getAbstimmungenListeM().get(i)
                        .setGewaehlt(weWeisungAendernGet.getWeisungenAgenda().get(i));
            }

            for (int i = 0; i < eclAbstimmungenListeM.getGegenantraegeListeM().size(); i++) {
                String h;
                h = weWeisungAendernGet.getWeisungenGegenantraege().get(i);
                if (h.compareTo("X") == 0) {
                    eclAbstimmungenListeM.getGegenantraegeListeM().get(i).setMarkiert(true);
                } else {
                    eclAbstimmungenListeM.getGegenantraegeListeM().get(i).setMarkiert(false);
                    eclAbstimmungenListeM.getGegenantraegeListeM().get(i)
                            .setGewaehlt(weWeisungAendernGet.getWeisungenGegenantraege().get(i));
                }
            }
        } else { /* Alternative 2 - fertig aufbereitete Weisungen */
            CaBug.druckeLog("Alternative 2", logDrucken, 10);
            eclAbstimmungenListeM.setAlternative(2);
            eclAbstimmungenListeM.setWeisungMeldung(weWeisungAendernGet.getWeisungMeldung());
            eclAbstimmungenListeM.setWeisungMeldungRaw(weWeisungAendernGet.getWeisungMeldungRaw());
        }

        if (weWeisungAendernGet.getWeLoginVerify().getUser() == KonstWebService.USER_APP) {
            /* Nur bei App: maximale Stimmabgabe prüfen */

            List<EclAbstimmungM> lAbstimmungenListe = eclAbstimmungenListeM.getAbstimmungenListeM();
            boolean brc = tWeisung.pruefeAbstimmungsgruppen(lAbstimmungenListe);
            if (brc == false) {
                weWeisungAendernGetRC.setRc(tSession.getFehlerCode());
                abbruchService();
                System.out.println("Gruppenfehler!");
                return weWeisungAendernGetRC;
            }

        }

//        tWeisungBestaetigung.doAendernSpeichern(); =Total Buggy => Änderungen wurden nicht gespeichert
        tWeisungBestaetigung.aendernErteilen();

        beendeService();
        return weWeisungAendernGetRC;
    }

    /*****************************************
     * vollmachtDritteGet
     **********************************************************/
    @POST
    @Path("vollmachtDritteGet")
    @Produces({ MediaType.APPLICATION_JSON, MediaType.TEXT_XML })
    public WEVollmachtDritteGetRC vollmachtDritteGet(WEVollmachtDritteGet weVollmachtDritteGet) {

        WEVollmachtDritteGetRC weVollmachtDritteGetRC = new WEVollmachtDritteGetRC();
        int rc = starteService(weVollmachtDritteGet.getWeLoginVerify(), "vollmachtDritteGet", true, true);
        if (rc < 1) {
            weVollmachtDritteGetRC.setRc(rc);
            return weVollmachtDritteGetRC;
        }
        if (rc == 2) {
            weVollmachtDritteGetRC.setPruefeVersion(1);
        }

        tWillenserklaerungSession.setAusgewaehlteHauptAktion(weVollmachtDritteGet.getAusgewaehlteHauptAktion());
        tWillenserklaerungSession.setAusgewaehlteAktion(weVollmachtDritteGet.getAusgewaehlteAktion());

        tWillenserklaerungSession.setVollmachtName(weVollmachtDritteGet.getVollmachtName());
        tWillenserklaerungSession.setVollmachtVorname(weVollmachtDritteGet.getVollmachtVorname());
        tWillenserklaerungSession.setVollmachtOrt(weVollmachtDritteGet.getVollmachtOrt());

        if (weVollmachtDritteGet.getEclZugeordneteMeldung() != null) {
            tWillenserklaerungSession.initZugeordneteMeldungFuerAusfuehrungListe();
            tWillenserklaerungSession
                    .addZugeordneteMeldungFuerAusfuehrungListe(weVollmachtDritteGet.getEclZugeordneteMeldung(), null);
            tWillenserklaerungSession.ermittleGattungenFuerZugeordneteMeldungFuerAusfuehrungListe();
        }

        boolean brc = tVollmachtDritte.ausstellen(false);
        if (brc == false) {
            weVollmachtDritteGetRC.setRc(tSession.getFehlerCode());
        } else {
            weVollmachtDritteGetRC.setRc(1);
        }

        beendeService();
        return weVollmachtDritteGetRC;

    }

    /*
     * TODO _Praesenz: WEB-Schnittstelle überprüfen, setzePraesentGet - prüfen, was
     * hier noch zu tun ist
     */
    @POST
    @Path("setzePraesentGet")
    @Produces({ MediaType.APPLICATION_JSON, MediaType.TEXT_XML })
    public WESetzePraesentGetRC setzePraesentGet(WESetzePraesentGet weSetzePraesentGet) {

        WESetzePraesentGetRC weSetzePraesentGetRC = new WESetzePraesentGetRC();
        int rc = starteService(weSetzePraesentGet.getWeLoginVerify(), "setzePraesentGet", true, true);
        if (rc < 1) {
            weSetzePraesentGetRC.setRc(rc);
            return weSetzePraesentGetRC;
        }
        if (rc == 2) {
            weSetzePraesentGetRC.setPruefeVersion(1);
        }

        BlPraesenzPROV blPraesenz = new BlPraesenzPROV();
        int erg = blPraesenz.setzePraesent(eclDbM.getDbBundle(), weSetzePraesentGet.getMeldungsIdent(),
                weSetzePraesentGet.getKartenart(), weSetzePraesentGet.getKartennr(),
                weSetzePraesentGet.getIdentPersonNatJur());
        weSetzePraesentGetRC.setRc(erg);

        beendeService();
        return weSetzePraesentGetRC;

    }

    /*
     * TODO _AbstimmungElektronischerStimmkartenblock - unklar, was hier zu tun ist
     */
    @POST
    @Path("stimmkartenBlockGet")
    @Produces({ MediaType.APPLICATION_JSON, MediaType.TEXT_XML })
    public WEStimmkartenblockGetRC stimmkartenBlockGet(WELoginVerify weLoginVerify) {

        WEStimmkartenblockGetRC weStimmkartenblockGetRC = new WEStimmkartenblockGetRC();
        int rc = starteService(weLoginVerify, "stimmkartenBlockGet", false, true);
        if (rc < 1) {
            weStimmkartenblockGetRC.setRc(rc);
            return weStimmkartenblockGetRC;
        }
        if (rc == 2) {
            weStimmkartenblockGetRC.setPruefeVersion(1);
        }

        BlAbstimmungTools blAbstimmungTools = new BlAbstimmungTools();
        blAbstimmungTools.initialisieren(eclDbM.getDbBundle());
        blAbstimmungTools.leseStimmkartenblock();
        EclStimmkartenBlock stimmkartenblock = blAbstimmungTools.rcEclStimmkartenBlock;

        // stimmkartenblock.init(eclDbM.getDbBundle());
        EclStimmkartenBlockM lEclStimmkartenBlockM = new EclStimmkartenBlockM();
        lEclStimmkartenBlockM.copyFrom(stimmkartenblock);

        // /*IF ELSE nur für Testausgabe*/
        // if (lEclStimmkartenBlockM.getStimmkartenBlockM()!=null){
        // System.out.println("WAINtern stimmkartenBlockGet Stimmkartenblock ist
        // ungleich null");
        // int i,i1;
        // for (i=0;i<lEclStimmkartenBlockM.getStimmkartenBlockM().length;i++){
        // System.out.println("i="+lEclStimmkartenBlockM.getStimmkartenBlockM()[i].getStimmkartenBezeichnung());
        // for
        // (i1=0;i1<lEclStimmkartenBlockM.getStimmkartenBlockM()[i].getAbstimmungenListeM().size();i1++){
        // System.out.println("Abstimmung i1 ="+i1+"
        // "+lEclStimmkartenBlockM.getStimmkartenBlockM()[i].getAbstimmungenListeM().get(i1).getAnzeigeBezeichnungKurz());
        // }
        // }
        // }
        // else{System.out.println("WAINtern stimmkartenBlockGet ist null");}

        weStimmkartenblockGetRC.setAbstimmungenListeM(lEclStimmkartenBlockM);
        /*
         * FIXME: für die Produktiv-App war der folgende Bereich aktiv (stimmblock immer
         * null). Testen, warum das so war? Wahrscheinlich nur wg. Information-Hiding.
         * Ggf. Parametrisieren
         */
        // if (weLoginVerify.getUser()==2){//Temporär. Wird für App-Abstimmung etc.
        // natürlich dann gebraucht
        // weStimmkartenblockGetRC.setAbstimmungenListeM(null);
        //// System.out.println("WAInternApp: AbstimmungenListeM auf null");
        // }

        beendeService();
        return weStimmkartenblockGetRC;

    }

    /*************************************
     * versandAdressePruefenGet
     ***********************************/
    @POST
    @Path("versandAdressePruefenGet")
    @Produces({ MediaType.APPLICATION_JSON, MediaType.TEXT_XML })
    public WEVersandAdressePruefenGetRC versandAdressePruefenGet(
            WEVersandAdressePruefenGet weVersandAdressePruefenGet) {

        WEVersandAdressePruefenGetRC weVersandAdressePruefenGetRC = new WEVersandAdressePruefenGetRC();
        int rc = starteService(weVersandAdressePruefenGet.getWeLoginVerify(), "versandAdressePruefenGet", false, false);
        if (rc < 1) {
            weVersandAdressePruefenGetRC.setRc(rc);
            return weVersandAdressePruefenGetRC;
        }
        if (rc == 2) {
            weVersandAdressePruefenGetRC.setPruefeVersion(1);
        }

        eclDbM.getDbBundle().dbJoined.read_versandadressePruefen(weVersandAdressePruefenGet.isNurInternet(),
                weVersandAdressePruefenGet.isNurNochNichtGeprueft(),
                weVersandAdressePruefenGet.isNurAbweichendeEingegeben());

        weVersandAdressePruefenGetRC.setErgebnisArrayMeldung(eclDbM.getDbBundle().dbJoined.ergebnisMeldung());
        weVersandAdressePruefenGetRC
                .setErgebnisArrayPersonenNatJur(eclDbM.getDbBundle().dbJoined.ergebnisPersonenNatJur());
        weVersandAdressePruefenGetRC.setErgebnisArrayPersonenNatJurVersandadresse(
                eclDbM.getDbBundle().dbJoined.ergebnisPersonenNatJurVersandadresse());
        weVersandAdressePruefenGetRC
                .setErgebnisArrayWillenserklaerung(eclDbM.getDbBundle().dbJoined.ergebnisWillenserklaerung());
        weVersandAdressePruefenGetRC.setErgebnisArrayWillenserklaerungZusatz(
                eclDbM.getDbBundle().dbJoined.ergebnisWillenserklaerungZusatz());
        weVersandAdressePruefenGetRC.setErgebnisArrayAnrede(eclDbM.getDbBundle().dbJoined.ergebnisAnrede());
        weVersandAdressePruefenGetRC
                .setErgebnisArrayAnredeVersand(eclDbM.getDbBundle().dbJoined.ergebnisAnredeVersand());
        weVersandAdressePruefenGetRC.setErgebnisArrayStaaten(eclDbM.getDbBundle().dbJoined.ergebnisStaaten());
        weVersandAdressePruefenGetRC.setErgebnisArrayStaaten1(eclDbM.getDbBundle().dbJoined.ergebnisStaaten1());
        weVersandAdressePruefenGetRC
                .setErgebnisArrayAktienregister(eclDbM.getDbBundle().dbJoined.ergebnisAktienregisterEintrag());

        beendeService();
        return weVersandAdressePruefenGetRC;
    }

    /*************************************
     * versandAdresseKorrigieren
     ***********************************/
    @POST
    @Path("versandAdresseKorrigieren")
    @Produces({ MediaType.APPLICATION_JSON, MediaType.TEXT_XML })
    public WEVersandAdresseKorrigierenRC versandAdresseKorrigieren(
            WEVersandAdresseKorrigieren weVersandAdresseKorrigieren) {

        int erg;

        WEVersandAdresseKorrigierenRC weVersandAdresseKorrigierenRC = new WEVersandAdresseKorrigierenRC();
        int rc = starteService(weVersandAdresseKorrigieren.getWeLoginVerify(), "versandAdresseKorrigieren", false,
                false);
        if (rc < 1) {
            weVersandAdresseKorrigierenRC.setRc(rc);
            return weVersandAdresseKorrigierenRC;
        }
        if (rc == 2) {
            weVersandAdresseKorrigierenRC.setPruefeVersion(1);
        }

        erg = eclDbM.getDbBundle().dbWillenserklaerung.updateMitZusatz(
                weVersandAdresseKorrigieren.getWillenserklaerung(),
                weVersandAdresseKorrigieren.getWillenserklaerungZusatz());
        if (erg < 1) {
            weVersandAdresseKorrigierenRC.setRc(erg);
            abbruchService();
            return weVersandAdresseKorrigierenRC;

        }

        beendeService();
        return weVersandAdresseKorrigierenRC;
    }

    /*************************************
     * eintrittskartenDrucklaufGet
     ***********************************/
    @POST
    @Path("eintrittskartenDrucklaufGet")
    @Produces({ MediaType.APPLICATION_JSON, MediaType.TEXT_XML })
    public WEEintrittskartenDrucklaufGetRC eintrittskartenDrucklaufGet(
            WEEintrittskartenDrucklaufGet weEintrittskartenDrucklaufGet) {

        WEEintrittskartenDrucklaufGetRC weEintrittskartenDrucklaufGetRC = new WEEintrittskartenDrucklaufGetRC();
        int rc = starteService(weEintrittskartenDrucklaufGet.getWeLoginVerify(), "eintrittskartenDrucklaufGet", false,
                false);
        if (rc < 1) {
            weEintrittskartenDrucklaufGetRC.setRc(rc);
            return weEintrittskartenDrucklaufGetRC;
        }
        if (rc == 2) {
            weEintrittskartenDrucklaufGetRC.setPruefeVersion(1);
        }

//
//        BlEintrittskartenStapelDruck blEintrittskartenStapelDruck=new BlEintrittskartenStapelDruck(eclDbM.getDbBundle());
//        blEintrittskartenStapelDruck.read_eintrittskartenDruck(weEintrittskartenDrucklaufGet.isErstDruck(),
//                weEintrittskartenDrucklaufGet.getDrucklauf(), weEintrittskartenDrucklaufGet.isNurGepruefte(), "", "",
//                weEintrittskartenDrucklaufGet.getGastOderAktionaer(), weEintrittskartenDrucklaufGet.getVonEkNummer(),
//                weEintrittskartenDrucklaufGet.getBisEkNummer());
//
//        EclAktienregister lAktienregister=null;
//        lAktienregister.adresszeile1="";
//        /*????????????????????????????????????????????*/

        eclDbM.getDbBundle().dbJoined.read_eintrittskartenDruck(weEintrittskartenDrucklaufGet.isErstDruck(),
                weEintrittskartenDrucklaufGet.getDrucklauf(), weEintrittskartenDrucklaufGet.isNurGepruefte(), "", "",
                weEintrittskartenDrucklaufGet.getGastOderAktionaer(), weEintrittskartenDrucklaufGet.getVonEkNummer(),
                weEintrittskartenDrucklaufGet.getBisEkNummer(), weEintrittskartenDrucklaufGet.getEkVersandFuerAlleImPortalAngefordertenSelektion());

        if (weEintrittskartenDrucklaufGet.isErstDruck()) {
            weEintrittskartenDrucklaufGetRC.setDrucklauf(eclDbM.getDbBundle().dbBasis.getInterneIdentDrucklauf());
        } else {
            weEintrittskartenDrucklaufGetRC.setDrucklauf(weEintrittskartenDrucklaufGet.getDrucklauf());
        }

        weEintrittskartenDrucklaufGetRC.setErgebnisArrayMeldung(eclDbM.getDbBundle().dbJoined.ergebnisMeldung());
        weEintrittskartenDrucklaufGetRC
                .setErgebnisArrayPersonenNatJur(eclDbM.getDbBundle().dbJoined.ergebnisPersonenNatJur());
        weEintrittskartenDrucklaufGetRC.setErgebnisArrayPersonenNatJurVersandadresse(
                /*eclDbM.getDbBundle().dbJoined.ergebnisPersonenNatJurVersandadresse()*/null);
        weEintrittskartenDrucklaufGetRC
                .setErgebnisArrayWillenserklaerung(eclDbM.getDbBundle().dbJoined.ergebnisWillenserklaerung());
        weEintrittskartenDrucklaufGetRC.setErgebnisArrayWillenserklaerungZusatz(
                eclDbM.getDbBundle().dbJoined.ergebnisWillenserklaerungZusatz());
        weEintrittskartenDrucklaufGetRC.setErgebnisArrayAnrede(eclDbM.getDbBundle().dbJoined.ergebnisAnrede());
        weEintrittskartenDrucklaufGetRC
                .setErgebnisArrayAnredeVersand(/*eclDbM.getDbBundle().dbJoined.ergebnisAnredeVersand()*/null);
        weEintrittskartenDrucklaufGetRC.setErgebnisArrayStaaten(eclDbM.getDbBundle().dbJoined.ergebnisStaaten());
        weEintrittskartenDrucklaufGetRC.setErgebnisArrayStaaten1(/*eclDbM.getDbBundle().dbJoined.ergebnisStaaten1()*/null);
        weEintrittskartenDrucklaufGetRC
                .setErgebnisArrayPersonenNatJur1(eclDbM.getDbBundle().dbJoined.ergebnisPersonenNatJur1());
        weEintrittskartenDrucklaufGetRC
                .setErgebnisArrayAktienregister(eclDbM.getDbBundle().dbJoined.ergebnisAktienregisterEintrag());
        weEintrittskartenDrucklaufGetRC.setErgebnisArrayLoginDaten(eclDbM.getDbBundle().dbJoined.ergebnisLoginDaten());
        weEintrittskartenDrucklaufGetRC
                .setErgebnisArrayLoginDaten1(eclDbM.getDbBundle().dbJoined.ergebnisLoginDaten1());

        int anzSaetzeInArray = weEintrittskartenDrucklaufGetRC.getErgebnisArrayMeldung().length;
        int anzSaetzeTatsaechlich = 0; // die tatschälich auszudruckenden EK, d.h. anzSaetzeInArray ohne ausselektierte
                                       // Null-Sätze

        int wegSelektion = weEintrittskartenDrucklaufGet.getWegSelektion();
        /** 0=alle, 1=Deutschland, 2=Ausland */
        int landSelektion = weEintrittskartenDrucklaufGet.getLandSelektion();

        String ersterAktionaer = "", letzterAktionaer = "";
        int offsetErsterAktionaer = -1;
        int offsetLetzterAktionaer = -1;

        for (int i = 0; i < anzSaetzeInArray; i++) {
            int gef = 1;

            EclMeldung lMeldung = weEintrittskartenDrucklaufGetRC.getErgebnisArrayMeldung()[i];
            EclWillenserklaerung lWillenserklaerung = weEintrittskartenDrucklaufGetRC
                    .getErgebnisArrayWillenserklaerung()[i];
            EclWillenserklaerungZusatz lWillenserklaerungZusatz = weEintrittskartenDrucklaufGetRC
                    .getErgebnisArrayWillenserklaerungZusatz()[i];
            EclStaaten lEclStaaten = weEintrittskartenDrucklaufGetRC.getErgebnisArrayStaaten()[i];
            EclStaaten lEclStaaten1 = null; //weEintrittskartenDrucklaufGetRC.getErgebnisArrayStaaten1()[i];

            /* Prüfen wegSelektion */
            if (wegSelektion == 2) {
                if (lWillenserklaerung.erteiltAufWeg >= 21 && lWillenserklaerung.erteiltAufWeg <= 29) {
                    gef = 0;
                }
            }
            if (wegSelektion == 1) {
                if (lWillenserklaerung.erteiltAufWeg < 21 || lWillenserklaerung.erteiltAufWeg > 29) {
                    gef = 0;
                }
            }

            /* Prüfen Land */
            int versandadresseverwenden = 1;/*
                                             * 1=Im Aktienregister hinterlegte Adresse; 3=eingegebene abweichende
                                             * Versandadresse
                                             */
            if (lWillenserklaerungZusatz.versandartEK == 2) {/* Manuell eingegebene Versandadresse */
                versandadresseverwenden = 3;
            }
            if (lWillenserklaerungZusatz.versandadresseUeberprueft == 2) {
                if (lWillenserklaerungZusatz.versandartEKUeberprueft == 1) {
                    versandadresseverwenden = 1;
                }
                if (lWillenserklaerungZusatz.versandartEKUeberprueft == 2
                        || lWillenserklaerungZusatz.versandartEKUeberprueft == 99) {
                    versandadresseverwenden = 3;
                }
            }
            switch (versandadresseverwenden) {
            case 1: {/* Versandadresse.* - im Aktienregister hinterlegte Versandadresse */
                if (lEclStaaten1 != null && lEclStaaten1.code != null) {
                    if (lEclStaaten1.code.compareTo("DE") == 0) {
                        if (landSelektion == 2) {
                            gef = 0;
                        }
                    } else {
                        if (landSelektion == 1) {
                            gef = 0;
                        }
                    }
                } else {
                    if (lEclStaaten != null && lEclStaaten.code != null /*Ehemals: lEclStaaten1 - machte aber keinen Sinn!*/) {
                        if (lEclStaaten.code.compareTo("DE") == 0) {
                            if (landSelektion == 2) {
                                gef = 0;
                            }
                        } else {
                            if (landSelektion == 1) {
                                gef = 0;
                            }
                        }
                    } else { // Kein Staat gefunden. Deshalb sicherheitshalber in Ausland rein
                        if (landSelektion == 1) {
                            gef = 0;
                        }
                    }
                }
                break;
            }
            case 3: {/*
                      * manuelle Versandadresse - Wenn Aktionär im Inland, dann Inland, sonst im
                      * Zweifelfall ans Auslande
                      */
                if ((lEclStaaten1 != null && lEclStaaten1.code != null && lEclStaaten1.code.compareTo("DE") == 0)
                        || (lEclStaaten != null && lEclStaaten.code != null && lEclStaaten.code.compareTo("DE") == 0)) {
                    if (landSelektion == 2) {
                        gef = 0;
                    }
                } else {
                    if (landSelektion == 1) {
                        gef = 0;
                    }
                }
                break;
            }
            }

            if (gef == 0) {
                /*
                 * Eine der überprüften Selektionen wurde nicht erfüllt => rausschmeißen (auf
                 * null setzen)
                 */
                weEintrittskartenDrucklaufGetRC.getErgebnisArrayMeldung()[i] = null;
            } else {
                anzSaetzeTatsaechlich++;

                letzterAktionaer = lMeldung.aktionaersnummer;
                offsetLetzterAktionaer = i;

                if (offsetErsterAktionaer == -1) {
                    ersterAktionaer = lMeldung.aktionaersnummer;
                    offsetErsterAktionaer = i;
                }
            }
        }

        weEintrittskartenDrucklaufGetRC.setAnzahlSaetzeTatsaechlich(anzSaetzeTatsaechlich);
        weEintrittskartenDrucklaufGetRC.setErsterAktionaer(ersterAktionaer);
        weEintrittskartenDrucklaufGetRC.setOffsetErsterAktionaer(offsetErsterAktionaer);
        weEintrittskartenDrucklaufGetRC.setLetzterAktionaer(letzterAktionaer);
        weEintrittskartenDrucklaufGetRC.setOffsetLetzterAktionaer(offsetLetzterAktionaer);

        /* Nun Drucklauf speichern */
        if (weEintrittskartenDrucklaufGet.isErstDruck()) {
            EclDrucklauf lDrucklauf = new EclDrucklauf();
            lDrucklauf.durchgefuehrtArbeitsplatzNr = weEintrittskartenDrucklaufGet.getWeLoginVerify().getArbeitsplatz();
            lDrucklauf.durchgefuehrtBenutzerNr = weEintrittskartenDrucklaufGet.getWeLoginVerify().getBenutzernr();
            lDrucklauf.erzeugtAm = CaDatumZeit.DatumZeitStringFuerDatenbank();
            lDrucklauf.drucklaufArt = 1;
            if (weEintrittskartenDrucklaufGet.isNurGepruefte()) {
                lDrucklauf.nurGepruefteVersandadressen = 1;
            } else {
                lDrucklauf.nurGepruefteVersandadressen = 0;
            }
            lDrucklauf.landSelektion = landSelektion;
            lDrucklauf.wegSelektion = wegSelektion;
            lDrucklauf.gaesteOderAktionaereSelektion = weEintrittskartenDrucklaufGet.getGastOderAktionaer();
            lDrucklauf.anzahlSaetze = anzSaetzeTatsaechlich;
            lDrucklauf.ersterAktionaer = ersterAktionaer;
            lDrucklauf.letzterAktionaer = letzterAktionaer;

            eclDbM.getDbBundle().openWeitere();
            eclDbM.getDbBundle().dbDrucklauf.insert(lDrucklauf);
            weEintrittskartenDrucklaufGetRC.setDrucklauf(lDrucklauf.drucklaufNr);
        }
        /*
         * TODO _Anmeldeablauf: Eintrittskartendruck. Derzeit noch einige
         * Vereinfachungen. Drucklauf nicht 1:1 wiederholbar, wenn z.B. Eintrittskarten
         * in neuem Drucklauf nochmal gedruckt. Auch keine Übersicht möglich über die
         * Druckläufe. Auch nicht jeder Druck nachvollziehbar.
         */

        beendeService();
        return weEintrittskartenDrucklaufGetRC;
    }

    /*************************************
     * eintrittskartenDrucklaufEinzelGet
     ***********************************/
    @POST
    @Path("eintrittskartenDrucklaufEinzelGet")
    @Produces({ MediaType.APPLICATION_JSON, MediaType.TEXT_XML })
    public WEEintrittskartenDrucklaufGetRC eintrittskartenDrucklaufEinzelGet(
            WEEintrittskartenDrucklaufEinzelGet weEintrittskartenDrucklaufEinzelGet) {

        WEEintrittskartenDrucklaufGetRC weEintrittskartenDrucklaufGetRC = new WEEintrittskartenDrucklaufGetRC();
        int rc = starteService(weEintrittskartenDrucklaufEinzelGet.getWeLoginVerify(),
                "eintrittskartenDrucklaufEinzelGet", false, false);
        if (rc < 1) {
            weEintrittskartenDrucklaufGetRC.setRc(rc);
            return weEintrittskartenDrucklaufGetRC;
        }
        if (rc == 2) {
            weEintrittskartenDrucklaufGetRC.setPruefeVersion(1);
        }

        eclDbM.getDbBundle().dbJoined.read_eintrittskartenDruck(false, 0, false,
                weEintrittskartenDrucklaufEinzelGet.getEkNummer(),
                weEintrittskartenDrucklaufEinzelGet.getEkNummerNeben(),
                weEintrittskartenDrucklaufEinzelGet.getGastOderAktionaer(), "", "", weEintrittskartenDrucklaufEinzelGet.getEkVersandFuerAlleImPortalAngefordertenSelektion());

        weEintrittskartenDrucklaufGetRC.setErgebnisArrayMeldung(eclDbM.getDbBundle().dbJoined.ergebnisMeldung());
        weEintrittskartenDrucklaufGetRC
                .setErgebnisArrayPersonenNatJur(eclDbM.getDbBundle().dbJoined.ergebnisPersonenNatJur());
        weEintrittskartenDrucklaufGetRC.setErgebnisArrayPersonenNatJurVersandadresse(
                eclDbM.getDbBundle().dbJoined.ergebnisPersonenNatJurVersandadresse());
        weEintrittskartenDrucklaufGetRC
                .setErgebnisArrayWillenserklaerung(eclDbM.getDbBundle().dbJoined.ergebnisWillenserklaerung());
        weEintrittskartenDrucklaufGetRC.setErgebnisArrayWillenserklaerungZusatz(
                eclDbM.getDbBundle().dbJoined.ergebnisWillenserklaerungZusatz());
        weEintrittskartenDrucklaufGetRC.setErgebnisArrayAnrede(eclDbM.getDbBundle().dbJoined.ergebnisAnrede());
        weEintrittskartenDrucklaufGetRC
                .setErgebnisArrayAnredeVersand(eclDbM.getDbBundle().dbJoined.ergebnisAnredeVersand());
        weEintrittskartenDrucklaufGetRC.setErgebnisArrayStaaten(eclDbM.getDbBundle().dbJoined.ergebnisStaaten());
        weEintrittskartenDrucklaufGetRC.setErgebnisArrayStaaten1(eclDbM.getDbBundle().dbJoined.ergebnisStaaten1());
        weEintrittskartenDrucklaufGetRC
                .setErgebnisArrayPersonenNatJur1(eclDbM.getDbBundle().dbJoined.ergebnisPersonenNatJur1());
        weEintrittskartenDrucklaufGetRC
                .setErgebnisArrayAktienregister(eclDbM.getDbBundle().dbJoined.ergebnisAktienregisterEintrag());
        weEintrittskartenDrucklaufGetRC.setErgebnisArrayLoginDaten(eclDbM.getDbBundle().dbJoined.ergebnisLoginDaten());
        weEintrittskartenDrucklaufGetRC
                .setErgebnisArrayLoginDaten1(eclDbM.getDbBundle().dbJoined.ergebnisLoginDaten1());

        /*
         * TODO _Anmeldeablauf: Eintrittskartendruck. Derzeit noch einige
         * Vereinfachungen. Drucklauf nicht 1:1 wiederholbar, wenn z.B. Eintrittskarten
         * in neuem Drucklauf nochmal gedruckt. Auch keine Übersicht möglich über die
         * Druckläufe. Auch nicht jeder Druck nachvollziehbar.
         */

        beendeService();
        return weEintrittskartenDrucklaufGetRC;
    }

    /*************************************
     * eintrittskarteGedruckt
     ***********************************/
    @POST
    @Path("eintrittskarteGedruckt")
    @Produces({ MediaType.APPLICATION_JSON, MediaType.TEXT_XML })
    public WEEintrittskarteGedrucktRC eintrittskarteGedruckt(WEEintrittskarteGedruckt weEintrittskarteGedruckt) {

        int erg;

        WEEintrittskarteGedrucktRC weEintrittskarteGedrucktRC = new WEEintrittskarteGedrucktRC();
        int rc = starteService(weEintrittskarteGedruckt.getWeLoginVerify(), "eintrittskarteGedruckt", false, false);
        if (rc < 1) {
            weEintrittskarteGedrucktRC.setRc(rc);
            return weEintrittskarteGedrucktRC;
        }
        if (rc == 2) {
            weEintrittskarteGedrucktRC.setPruefeVersion(1);
        }

        /* Gedruckte Eintrittskarten als Gedruckt in Willenserklärung markieren */
        for (int i = 0; i < weEintrittskarteGedruckt.getListWillenserklaerung().size(); i++) {
            weEintrittskarteGedruckt.getListWillenserklaerungZusatz()
                    .get(i).eintrittskarteWurdeGedruckt = weEintrittskarteGedruckt.getDrucklauf();
            String druckDatum = CaDatumZeit.DatumZeitStringFuerDatenbank();
            if (weEintrittskarteGedruckt.getListWillenserklaerungZusatz().get(i).erstesDruckDatum.isEmpty()) {
                weEintrittskarteGedruckt.getListWillenserklaerungZusatz().get(i).erstesDruckDatum = druckDatum;
            }
            weEintrittskarteGedruckt.getListWillenserklaerungZusatz().get(i).letztesDruckDatum = druckDatum;

            erg = eclDbM.getDbBundle().dbWillenserklaerung.updateMitZusatz(
                    weEintrittskarteGedruckt.getListWillenserklaerung().get(i),
                    weEintrittskarteGedruckt.getListWillenserklaerungZusatz().get(i));
            if (erg < 1) {
                weEintrittskarteGedrucktRC.setRc(erg);
                abbruchService();
                return weEintrittskarteGedrucktRC;
            }

        }

        /* Drucklauf als "Gedruckt" kennzeichnen */
        eclDbM.getDbBundle().openWeitere();
        eclDbM.getDbBundle().dbDrucklauf.read(weEintrittskarteGedruckt.getDrucklauf());
        if (eclDbM.getDbBundle().dbDrucklauf.anzErgebnis() != 0) {
            EclDrucklauf lDrucklauf = eclDbM.getDbBundle().dbDrucklauf.ergebnisPosition(0);
            lDrucklauf.gedruckt = 1;
            eclDbM.getDbBundle().dbDrucklauf.update(lDrucklauf);
        }

        beendeService();
        return weEintrittskarteGedrucktRC;
    }

    /*************************************
     * drucklaeufeGet
     ***********************************/
    @POST
    @Path("drucklaeufeGet")
    @Produces({ MediaType.APPLICATION_JSON, MediaType.TEXT_XML })
    public WEDrucklaeufeGetRC drucklaeufeGet(WEDrucklaeufeGet weDrucklaeufeGet) {

        WEDrucklaeufeGetRC weDrucklaeufeGetRC = new WEDrucklaeufeGetRC();
        int rc = starteService(weDrucklaeufeGet.getWeLoginVerify(), "drucklaeufeGet", false, false);
        if (rc < 1) {
            weDrucklaeufeGetRC.setRc(rc);
            return weDrucklaeufeGetRC;
        }
        if (rc == 2) {
            weDrucklaeufeGetRC.setPruefeVersion(1);
        }

        eclDbM.getDbBundle().openWeitere();
        if (weDrucklaeufeGet.drucklaufArt==KonstVerarbeitungslaufArt.neuesPasswort) {
            eclDbM.getDbBundle().dbDrucklauf.mandantenabhaengig=false;
        }
        eclDbM.getDbBundle().dbDrucklauf.readAll(weDrucklaeufeGet.drucklaufArt, weDrucklaeufeGet.drucklaufSubArt);
        eclDbM.getDbBundle().dbDrucklauf.mandantenabhaengig=true;

        if (eclDbM.getDbBundle().dbDrucklauf.anzErgebnis() > 0) {
            weDrucklaeufeGetRC.drucklaufArray = eclDbM.getDbBundle().dbDrucklauf.ergebnisArray;
        } else {
            weDrucklaeufeGetRC.drucklaufArray = null;
        }
        beendeService();
        return weDrucklaeufeGetRC;
    }

    /*************************************
     * bestandAnmelden
     ***********************************/
    @POST
    @Path("bestandAnmelden")
    @Produces({ MediaType.APPLICATION_JSON, MediaType.TEXT_XML })
    public WEBestandAnmeldenRC bestandAnmelden(WEBestandAnmelden weBestandAnmelden) {

        int erg;

        WEBestandAnmeldenRC weBestandAnmeldenRC = new WEBestandAnmeldenRC();
        int rc = starteService(weBestandAnmelden.getWeLoginVerify(), "bestandAnmelden", true, true);
        if (rc < 1) {
            weBestandAnmeldenRC.setRc(rc);
            return weBestandAnmeldenRC;
        }
        if (rc == 2) {
            weBestandAnmeldenRC.setPruefeVersion(1);
        }

        WELoginVerify lWELoginVerify = weBestandAnmelden.getWeLoginVerify();

        BlWillenserklaerung lWillenserklaerung = null;

        lWillenserklaerung = new BlWillenserklaerung();
        lWillenserklaerung.pErteiltAufWeg = lWELoginVerify.getEingabeQuelle();
        lWillenserklaerung.pErteiltZeitpunkt = lWELoginVerify.getErteiltZeitpunkt();
        /* Aktienregister füllen */
        EclAktienregister aktienregisterEintrag = new EclAktienregister();
        aktienregisterEintrag.aktienregisterIdent = weBestandAnmelden.getAktienregisterIdent();
        erg = eclDbM.getDbBundle().dbAktienregister.leseZuAktienregisterEintrag(aktienregisterEintrag);
        if (erg <= 0) {/* Aktienregistereintrag nicht mehr vorhanden */
            weBestandAnmeldenRC.setRc(CaFehler.afAktienregisterEintragNichtMehrVorhanden);
            abbruchService();
            return weBestandAnmeldenRC;
        }

        aktienregisterEintrag = eclDbM.getDbBundle().dbAktienregister.ergebnisPosition(0);
        lWillenserklaerung.pEclAktienregisterEintrag = aktienregisterEintrag;

        /* Restliche Parameter füllen */
        if (weBestandAnmelden.getAnmeldeart() != 11) {
            lWillenserklaerung.pAktienAnmelden = -1; /* Alle Aktien anmelden */
            lWillenserklaerung.pAnmeldungFix = false; /* Nicht "Fix" anmelden */
            lWillenserklaerung.pAnzahlAnmeldungen = weBestandAnmelden.getAnmeldeart();
        } else {
            lWillenserklaerung.pAktienAnmelden = weBestandAnmelden.getAnzahlAktienFix(); /* Aktien anmelden */
            lWillenserklaerung.pAnmeldungFix = true; /* "Fix" anmelden */
        }
        lWillenserklaerung.pWillenserklaerungGeberIdent = -1; /* Aktionär gibt in diesem Fall */

        lWillenserklaerung.anmeldungAusAktienregister(eclDbM.getDbBundle());

        if (lWillenserklaerung.rcIstZulaessig == false) {
            weBestandAnmeldenRC.setRc(lWillenserklaerung.rcGrundFuerUnzulaessig);
            abbruchService();
            return weBestandAnmeldenRC;
        }

        weBestandAnmeldenRC.setMeldungsIdentAngemeldet(lWillenserklaerung.rcMeldungen[0]);

        beendeService();
        return weBestandAnmeldenRC;
    }

    /*************************************
     * bestandFixAnmelden
     ***********************************/
    @POST
    @Path("bestandFixAnmelden")
    @Produces({ MediaType.APPLICATION_JSON, MediaType.TEXT_XML })
    public WEBestandAnmeldenRC bestandFixAnmelden(WEBestandAnmelden weBestandAnmelden) {

        int erg;

        WEBestandAnmeldenRC weBestandAnmeldenRC = new WEBestandAnmeldenRC();
        int rc = starteService(weBestandAnmelden.getWeLoginVerify(), "bestandFixAnmelden", true, true);
        if (rc < 1) {
            weBestandAnmeldenRC.setRc(rc);
            return weBestandAnmeldenRC;
        }
        if (rc == 2) {
            weBestandAnmeldenRC.setPruefeVersion(1);
        }

        WELoginVerify lWELoginVerify = weBestandAnmelden.getWeLoginVerify();

        BlWillenserklaerung lWillenserklaerung = null;

        lWillenserklaerung = new BlWillenserklaerung();
        lWillenserklaerung.pErteiltAufWeg = lWELoginVerify.getEingabeQuelle();
        lWillenserklaerung.pErteiltZeitpunkt = lWELoginVerify.getErteiltZeitpunkt();
        /* Aktienregister füllen */
        EclAktienregister aktienregisterEintrag = new EclAktienregister();
        aktienregisterEintrag.aktienregisterIdent = weBestandAnmelden.getAktienregisterIdent();
        erg = eclDbM.getDbBundle().dbAktienregister.leseZuAktienregisterEintrag(aktienregisterEintrag);
        if (erg <= 0) {/* Aktienregistereintrag nicht mehr vorhanden */
            weBestandAnmeldenRC.setRc(CaFehler.afAktienregisterEintragNichtMehrVorhanden);
            abbruchService();
            return weBestandAnmeldenRC;
        }

        aktienregisterEintrag = eclDbM.getDbBundle().dbAktienregister.ergebnisPosition(0);
        lWillenserklaerung.pEclAktienregisterEintrag = aktienregisterEintrag;

        lWillenserklaerung.pAktienAnmelden = weBestandAnmelden.getAnzahlAktienFix(); /* Aktien anmelden */
        lWillenserklaerung.pAnmeldungFix = true; /* "Fix" anmelden */

        lWillenserklaerung.pWillenserklaerungGeberIdent = -1; /* Aktionär gibt in diesem Fall */

        lWillenserklaerung.anmeldungAusAktienregister(eclDbM.getDbBundle());

        if (lWillenserklaerung.rcIstZulaessig == false) {
            weBestandAnmeldenRC.setRc(lWillenserklaerung.rcGrundFuerUnzulaessig);
            abbruchService();
            return weBestandAnmeldenRC;

        }

        weBestandAnmeldenRC.setMeldungsIdentAngemeldet(lWillenserklaerung.rcMeldungen[0]);

        beendeService();
        return weBestandAnmeldenRC;
    }

    /*************************************
     * bestandFixAendern
     ***********************************/
    @POST
    @Path("bestandFixAendern")
    @Produces({ MediaType.APPLICATION_JSON, MediaType.TEXT_XML })
    public WEBestandFixAendernRC bestandFixAendern(WEBestandFixAendern weBestandFixAendern) {

        WEBestandFixAendernRC weBestandFixAendernRC = new WEBestandFixAendernRC();
        int rc = starteService(weBestandFixAendern.getWeLoginVerify(), "bestandFixAendern", true, true);
        if (rc < 1) {
            weBestandFixAendernRC.setRc(rc);
            return weBestandFixAendernRC;
        }
        if (rc == 2) {
            weBestandFixAendernRC.setPruefeVersion(1);
        }

        WELoginVerify lWELoginVerify = weBestandFixAendern.getWeLoginVerify();

        BlWillenserklaerung lWillenserklaerung = null;

        lWillenserklaerung = new BlWillenserklaerung();
        lWillenserklaerung.pErteiltAufWeg = lWELoginVerify.getEingabeQuelle();
        lWillenserklaerung.pErteiltZeitpunkt = lWELoginVerify.getErteiltZeitpunkt();

        lWillenserklaerung.piMeldungsIdentAktionaer = weBestandFixAendern.getMeldeint();
        lWillenserklaerung.pAktienAnmelden = weBestandFixAendern.getAnzahlAktienFix(); /* Aktien anmelden */
        lWillenserklaerung.pAnmeldungFix = true; /* "Fix" anmelden */

        lWillenserklaerung.pWillenserklaerungGeberIdent = -1; /* Aktionär gibt in diesem Fall */

        lWillenserklaerung.anmeldungFixAendern(eclDbM.getDbBundle());

        if (lWillenserklaerung.rcIstZulaessig == false) {
            weBestandFixAendernRC.setRc(lWillenserklaerung.rcGrundFuerUnzulaessig);
            abbruchService();
            return weBestandFixAendernRC;

        }

        beendeService();
        return weBestandFixAendernRC;
    }

    /*************************************
     * anmeldungStornieren
     ***********************************/
    @POST
    @Path("anmeldungStornieren")
    @Produces({ MediaType.APPLICATION_JSON, MediaType.TEXT_XML })
    public WEAnmeldungStornierenRC anmeldungStornieren(WEAnmeldungStornieren weAnmeldungStornieren) {

        WEAnmeldungStornierenRC weAnmeldungStornierenRC = new WEAnmeldungStornierenRC();
        int rc = starteService(weAnmeldungStornieren.getWeLoginVerify(), "anmeldungStornieren", true, true);
        if (rc < 1) {
            weAnmeldungStornierenRC.setRc(rc);
            return weAnmeldungStornierenRC;
        }
        if (rc == 2) {
            weAnmeldungStornierenRC.setPruefeVersion(1);
        }

        BlWillenserklaerung lWillenserklaerung = null;

        WELoginVerify lWELoginVerify = weAnmeldungStornieren.getWeLoginVerify();

        lWillenserklaerung = new BlWillenserklaerung();
        lWillenserklaerung.pErteiltAufWeg = lWELoginVerify.getEingabeQuelle();
        lWillenserklaerung.pErteiltZeitpunkt = lWELoginVerify.getErteiltZeitpunkt();
        lWillenserklaerung.piMeldungsIdentAktionaer = weAnmeldungStornieren.getMeldungsIdent();
        lWillenserklaerung.pWillenserklaerungGeberIdent = -1; /* Aktionär gibt in diesem Fall */

        lWillenserklaerung.anmeldungStornieren(eclDbM.getDbBundle());
        if (lWillenserklaerung.rcIstZulaessig == false) {
            weAnmeldungStornierenRC.setRc(lWillenserklaerung.rcGrundFuerUnzulaessig);
            abbruchService();
            return weAnmeldungStornierenRC;
        }

        beendeService();
        return weAnmeldungStornierenRC;
    }

    /*************************************
     * aktienregisterNeuerEintrag
     ***********************************/
    @POST
    @Path("aktienregisterNeuerEintrag")
    @Produces({ MediaType.APPLICATION_JSON, MediaType.TEXT_XML })
    public WEAktienregisterNeuerEintragRC aktienregisterNeuerEintrag(
            WEAktienregisterNeuerEintrag weAktienregisterNeuerEintrag) {

        WEAktienregisterNeuerEintragRC weAktienregisterNeuerEintragRC = new WEAktienregisterNeuerEintragRC();
        int rc = starteService(weAktienregisterNeuerEintrag.getWeLoginVerify(), "aktienregisterNeuerEintrag", false,
                false);
        if (rc < 1) {
            weAktienregisterNeuerEintragRC.setRc(rc);
            return weAktienregisterNeuerEintragRC;
        }
        if (rc == 2) {
            weAktienregisterNeuerEintragRC.setPruefeVersion(1);
        }

        EclAktienregister eclAktienregisterEintrag = new EclAktienregister();
        eclAktienregisterEintrag.nachname = weAktienregisterNeuerEintrag.name;
        eclAktienregisterEintrag.aktionaersnummer = weAktienregisterNeuerEintrag.aktionaersnummer;
        if (eclDbM.getDbBundle().param.paramBasis.namensaktienAktiv) {
            if (eclAktienregisterEintrag.aktionaersnummer
                    .length() <= eclDbM.getDbBundle().param.paramBasis.laengeAktionaersnummer) {
                eclAktienregisterEintrag.aktionaersnummer += "0";
            }
        }

        eclAktienregisterEintrag.vorname = weAktienregisterNeuerEintrag.vorname;
        eclAktienregisterEintrag.ort = weAktienregisterNeuerEintrag.ort;
        eclAktienregisterEintrag.stueckAktien = weAktienregisterNeuerEintrag.stueckAktien;
        eclAktienregisterEintrag.stimmen = weAktienregisterNeuerEintrag.stueckAktien;
        eclAktienregisterEintrag.besitzart = "#";
        eclAktienregisterEintrag.gattungId = 1;

        boolean nichtvorhanden = eclDbM.getDbBundle().dbAktienregister.insertCheck(eclAktienregisterEintrag);
        if (!nichtvorhanden) {
            weAktienregisterNeuerEintragRC.setRc(CaFehler.pfdXyBereitsVorhanden);
            abbruchService();
            return weAktienregisterNeuerEintragRC;
        }

        int rc1;
        rc1 = eclDbM.getDbBundle().dbAktienregister.insert(eclAktienregisterEintrag);
        weAktienregisterNeuerEintragRC.setRc(rc1);

        BlKennung lkennung = new BlKennung(eclDbM.getDbBundle());
        lkennung.neueKennungUndOeffentlicheID(eclAktienregisterEintrag.nachname, eclAktienregisterEintrag.vorname);

        EclLoginDaten lLoginDaten = new EclLoginDaten();
        lLoginDaten.loginKennung = eclAktienregisterEintrag.aktionaersnummer;
        lLoginDaten.kennungArt = KonstLoginKennungArt.aktienregister;
        lLoginDaten.aktienregisterIdent = eclAktienregisterEintrag.aktienregisterIdent;
        lLoginDaten.passwortVerschluesselt = lkennung.ergebnisPasswortInitialVerschluesselt;
        lLoginDaten.passwortInitial = lkennung.ergebnisPasswortInitialFuerDatenbank;

        rc1 = eclDbM.getDbBundle().dbLoginDaten.insert(lLoginDaten);

        beendeService();
        return weAktienregisterNeuerEintragRC;
    }

    /*************************************
     * aktionaersStatusDetailGet
     ***********************************/
    @POST
    @Path("aktionaersStatusDetailGet")
    @Produces({ MediaType.APPLICATION_JSON, MediaType.TEXT_XML })
    public WEAktionaersStatusDetailGetRC aktionaersStatusDetailGet(
            WEAktionaersStatusDetailGet weAktionaersStatusDetailGet) {

        WEAktionaersStatusDetailGetRC weAktionaersStatusDetailGetRC = new WEAktionaersStatusDetailGetRC();
        int rc = starteService(weAktionaersStatusDetailGet.getWeLoginVerify(), "aktionaersStatusDetailGet", true, true);
        if (rc < 1) {
            System.out.println("Return bei A");
            weAktionaersStatusDetailGetRC.setRc(rc);
            return weAktionaersStatusDetailGetRC;
        }
        if (rc == 2) {
            weAktionaersStatusDetailGetRC.setPruefeVersion(1);
        }

        // Keine Ahnung warum das noch drin war ...
        // aFunktionen.leseAbstimmungsliste(eclDbM.getDbBundle());


        BlWillenserklaerungStatus blWillenserklaerungStatus = new BlWillenserklaerungStatus(eclDbM.getDbBundle());
        EclAktienregister lAktienregisterEintrag = new EclAktienregister();
        lAktienregisterEintrag.aktionaersnummer = weAktionaersStatusDetailGet.aktionaersnummer;

        /* int rc1= */eclDbM.getDbBundle().dbAktienregister.leseZuAktienregisterEintrag(lAktienregisterEintrag);
        // if (rc1==1){}
        lAktienregisterEintrag = eclDbM.getDbBundle().dbAktienregister.ergebnisPosition(0);
        weAktionaersStatusDetailGetRC.eclAktienregisterEintrag = lAktienregisterEintrag;

        blWillenserklaerungStatus.piAnsichtVerarbeitungOderAnalyse = 2;
        blWillenserklaerungStatus.piRueckgabeKurzOderLang = 2;
        blWillenserklaerungStatus.piSelektionGeberOderAlle = 2;
        blWillenserklaerungStatus.leseMeldungenZuAktienregisterIdent(lAktienregisterEintrag.aktienregisterIdent);
        blWillenserklaerungStatus.ergaenzeZugeordneteMeldungenUmWillenserklaerungen(-2);

        weAktionaersStatusDetailGetRC.piSelektionGeberOderAlle = blWillenserklaerungStatus.piSelektionGeberOderAlle;
        weAktionaersStatusDetailGetRC.piRueckgabeKurzOderLang = blWillenserklaerungStatus.piRueckgabeKurzOderLang;
        weAktionaersStatusDetailGetRC.piAnsichtVerarbeitungOderAnalyse = blWillenserklaerungStatus.piAnsichtVerarbeitungOderAnalyse;
        weAktionaersStatusDetailGetRC.piAlleWillenserklaerungen = blWillenserklaerungStatus.piAlleWillenserklaerungen;
        weAktionaersStatusDetailGetRC.zugeordneteMeldungenEigeneAktienArray = blWillenserklaerungStatus.zugeordneteMeldungenEigeneAktienArray;
        weAktionaersStatusDetailGetRC.gastKartenGemeldetEigeneAktien = blWillenserklaerungStatus.gastKartenGemeldetEigeneAktien;
        weAktionaersStatusDetailGetRC.aktienregisterPersonNatJurIdent = blWillenserklaerungStatus.aktienregisterPersonNatJurIdent;
        weAktionaersStatusDetailGetRC.zugeordneteMeldungenEigeneGastkartenArray = blWillenserklaerungStatus.zugeordneteMeldungenEigeneGastkartenArray;
        weAktionaersStatusDetailGetRC.zugeordneteMeldungenBevollmaechtigtArray = blWillenserklaerungStatus.zugeordneteMeldungenBevollmaechtigtArray;
        weAktionaersStatusDetailGetRC.briefwahlVorhanden = blWillenserklaerungStatus.briefwahlVorhanden;
        weAktionaersStatusDetailGetRC.srvVorhanden = blWillenserklaerungStatus.srvVorhanden;
        weAktionaersStatusDetailGetRC.rcHatNichtNurPortalWillenserklaerungen = blWillenserklaerungStatus.rcHatNichtNurPortalWillenserklaerungen;
        weAktionaersStatusDetailGetRC.rcDatumLetzteWillenserklaerung = blWillenserklaerungStatus.rcDatumLetzteWillenserklaerung;
        weAktionaersStatusDetailGetRC.rcListeVollmachten = blWillenserklaerungStatus.rcListeVollmachten;





        System.out.println("weAktionaersStatusDetailGetRC.getRc()=" + weAktionaersStatusDetailGetRC.getRc());
        beendeService();
        return weAktionaersStatusDetailGetRC;
    }

    /*************************************
     * eintrittskarteKorrigierenGET
     ***********************************/
    @POST
    @Path("eintrittskarteKorrigierenGET")
    @Produces({ MediaType.APPLICATION_JSON, MediaType.TEXT_XML })
    public WEEintrittskarteKorrigieren eintrittskarteKorrigierenGET(
            WEEintrittskarteKorrigieren weEintrittskarteKorrigieren) {

        WEEintrittskarteKorrigieren weEintrittskarteKorrigierenRC = new WEEintrittskarteKorrigieren();
        int rc = starteService(weEintrittskarteKorrigieren.getWeLoginVerify(), "eintrittskarteKorrigierenGET", false,
                false);
        if (rc < 1) {
            weEintrittskarteKorrigierenRC.setRc(rc);
            return weEintrittskarteKorrigierenRC;
        }
        if (rc == 2) {
            weEintrittskarteKorrigierenRC.setPruefeVersion(1);
        }

        EclWillenserklaerung eclWillenserklaerung = new EclWillenserklaerung();
        EclWillenserklaerungZusatz eclWillenserklaerungZusatz = new EclWillenserklaerungZusatz();

        EclWillenserklaerung eclWillenserklaerung2 = new EclWillenserklaerung();
        EclWillenserklaerungZusatz eclWillenserklaerungZusatz2 = new EclWillenserklaerungZusatz();

        EclPersonenNatJur eclPersonenNatJur = new EclPersonenNatJur();

        eclDbM.getDbBundle().dbWillenserklaerung
                .leseZuIdent(weEintrittskarteKorrigieren.willenserklaerung.willenserklaerungIdent);
        eclWillenserklaerung = eclDbM.getDbBundle().dbWillenserklaerung.willenserklaerungGefunden(0);

        eclDbM.getDbBundle().dbWillenserklaerungZusatz
                .leseZuIdent(weEintrittskarteKorrigieren.willenserklaerung.willenserklaerungIdent);
        if (eclDbM.getDbBundle().dbWillenserklaerungZusatz.anzWillenserklaerungGefunden() != 0) {
            eclWillenserklaerungZusatz = eclDbM.getDbBundle().dbWillenserklaerungZusatz.willenserklaerungGefunden(0);
        } else {
            eclWillenserklaerungZusatz = null;
        }

        if (eclWillenserklaerung2 != null
                && weEintrittskarteKorrigieren.willenserklaerung2.willenserklaerungIdent != 0) {
            eclDbM.getDbBundle().dbWillenserklaerung
                    .leseZuIdent(weEintrittskarteKorrigieren.willenserklaerung2.willenserklaerungIdent);
            eclWillenserklaerung2 = eclDbM.getDbBundle().dbWillenserklaerung.willenserklaerungGefunden(0);

            eclDbM.getDbBundle().dbWillenserklaerungZusatz
                    .leseZuIdent(weEintrittskarteKorrigieren.willenserklaerung2.willenserklaerungIdent);
            if (eclDbM.getDbBundle().dbWillenserklaerungZusatz.anzWillenserklaerungGefunden() != 0) {
                eclWillenserklaerungZusatz2 = eclDbM.getDbBundle().dbWillenserklaerungZusatz
                        .willenserklaerungGefunden(0);
            } else {
                eclWillenserklaerungZusatz2 = null;
            }

            eclDbM.getDbBundle().dbPersonenNatJur.read(eclWillenserklaerung2.bevollmaechtigterDritterIdent);
            eclPersonenNatJur = eclDbM.getDbBundle().dbPersonenNatJur.PersonNatJurGefunden(0);
        } else { /*
                  * Nur willenserklaerung selbst. Prüfen - enthält diese bevollmächtigten
                  * Dritter?
                  */
            if (eclWillenserklaerung.bevollmaechtigterDritterIdent != 0) {
                eclDbM.getDbBundle().dbPersonenNatJur.read(eclWillenserklaerung.bevollmaechtigterDritterIdent);
                eclPersonenNatJur = eclDbM.getDbBundle().dbPersonenNatJur.PersonNatJurGefunden(0);
            }
        }

        weEintrittskarteKorrigierenRC.willenserklaerung = eclWillenserklaerung;
        weEintrittskarteKorrigierenRC.willenserklaerungZusatz = eclWillenserklaerungZusatz;

        weEintrittskarteKorrigierenRC.willenserklaerung2 = eclWillenserklaerung2;
        weEintrittskarteKorrigierenRC.willenserklaerungZusatz2 = eclWillenserklaerungZusatz2;

        weEintrittskarteKorrigierenRC.personNatJur = eclPersonenNatJur;

        beendeService();
        return weEintrittskarteKorrigierenRC;
    }

    /*************************************
     * eintrittskarteKorrigierenPUT
     ***********************************/
    @POST
    @Path("eintrittskarteKorrigierenPUT")
    @Produces({ MediaType.APPLICATION_JSON, MediaType.TEXT_XML })
    public WEEintrittskarteKorrigieren eintrittskarteKorrigierenPUT(
            WEEintrittskarteKorrigieren weEintrittskarteKorrigieren) {
        int rc1 = 0;

        WEEintrittskarteKorrigieren weEintrittskarteKorrigierenRC = new WEEintrittskarteKorrigieren();
        int rc = starteService(weEintrittskarteKorrigieren.getWeLoginVerify(), "eintrittskarteKorrigierenPUT", false,
                false);
        if (rc < 1) {
            weEintrittskarteKorrigierenRC.setRc(rc);
            return weEintrittskarteKorrigierenRC;
        }
        if (rc == 2) {
            weEintrittskarteKorrigierenRC.setPruefeVersion(1);
        }

        EclWillenserklaerung eclWillenserklaerung = weEintrittskarteKorrigieren.willenserklaerung;
        EclWillenserklaerungZusatz eclWillenserklaerungZusatz = weEintrittskarteKorrigieren.willenserklaerungZusatz;

        eclWillenserklaerungZusatz.eintrittskarteWurdeGedruckt = 0; /* Eintrittskarte auf "nicht gedruckt" setzen */

        rc1 = eclDbM.getDbBundle().dbWillenserklaerung.updateMitZusatz(eclWillenserklaerung,
                eclWillenserklaerungZusatz);
        if (rc1 < 1) {
            weEintrittskarteKorrigierenRC.setRc(rc1);
            abbruchService();
            return weEintrittskarteKorrigierenRC;
        }

        EclWillenserklaerung eclWillenserklaerung2 = weEintrittskarteKorrigieren.willenserklaerung2;
        EclWillenserklaerungZusatz eclWillenserklaerungZusatz2 = weEintrittskarteKorrigieren.willenserklaerungZusatz2;
        EclPersonenNatJur eclPersonenNatJur = weEintrittskarteKorrigieren.personNatJur;

        int meldeIdent = -1;
        if (eclWillenserklaerung2 != null
                && weEintrittskarteKorrigieren.willenserklaerung2.willenserklaerungIdent != 0) {
            rc1 = eclDbM.getDbBundle().dbWillenserklaerung.updateMitZusatz(eclWillenserklaerung2,
                    eclWillenserklaerungZusatz2);
            if (rc1 < 1) {
                weEintrittskarteKorrigierenRC.setRc(rc1);
                abbruchService();
                return weEintrittskarteKorrigierenRC;
            }
            meldeIdent = eclWillenserklaerung2.meldungsIdent;
        }

        if (eclPersonenNatJur.ident != 0) {
            rc1 = eclDbM.getDbBundle().dbPersonenNatJur.update(eclPersonenNatJur);
            if (rc1 < 1) {
                weEintrittskarteKorrigierenRC.setRc(rc1);
                abbruchService();
                return weEintrittskarteKorrigierenRC;
            }

        }
        if (meldeIdent != -1) {
            /* Nun noch Vertretername im Meldesatz korrigieren */
            rc1 = eclDbM.getDbBundle().dbMeldungen.leseZuIdent(meldeIdent);
            if (rc1 < 1) {
                weEintrittskarteKorrigierenRC.setRc(rc1);
                abbruchService();
                return weEintrittskarteKorrigierenRC;
            }
            EclMeldung lMeldung = eclDbM.getDbBundle().dbMeldungen.meldungenArray[0];
            lMeldung.vertreterName = eclPersonenNatJur.name;
            lMeldung.vertreterVorname = eclPersonenNatJur.vorname;
            lMeldung.vertreterOrt = eclPersonenNatJur.ort;
            eclDbM.getDbBundle().dbMeldungen.update(lMeldung);
        }

        beendeService();
        return weEintrittskarteKorrigierenRC;
    }

    /*************************************
     * praesenzStatusabfrage
     ***********************************/
    @POST
    @Path("praesenzStatusabfrage")
    @Produces({ MediaType.APPLICATION_JSON, MediaType.TEXT_XML })
    public WEPraesenzStatusabfrageRC praesenzStatusabfrage(WEPraesenzStatusabfrage wePraesenzStatusabfrage) {

        WEPraesenzStatusabfrageRC wePraesenzStatusabfrageRC = new WEPraesenzStatusabfrageRC();
        /* TODO Achtung App - es erfolgt keine Überprüfung des Teilnehmers */
        int rc = starteService(wePraesenzStatusabfrage.getWeLoginVerify(), "praesenzStatusabfrage", false, true);
        if (rc < 1) {
            wePraesenzStatusabfrageRC.setRc(rc);
            return wePraesenzStatusabfrageRC;
        }

        BlPraesenzAkkreditierung blPraesenz = new BlPraesenzAkkreditierung(eclDbM.getDbBundle());
        wePraesenzStatusabfrageRC = blPraesenz.statusabfrage(wePraesenzStatusabfrage);
        if (rc == 2) {
            wePraesenzStatusabfrageRC.setPruefeVersion(1);
        }

        beendeService();
        return wePraesenzStatusabfrageRC;
    }

    /*************************************
     * praesenzBuchen
     ***********************************/
    @POST
    @Path("praesenzBuchen")
    @Produces({ MediaType.APPLICATION_JSON, MediaType.TEXT_XML })
    public WEPraesenzBuchenRC praesenzBuchen(WEPraesenzBuchen wePraesenzBuchen) {

        WEPraesenzBuchenRC wePraesenzBuchenRC = new WEPraesenzBuchenRC();
        /* FIXME Wg. App wieder ermöglicht! - ggf. Parametrisieren */
        /* TODO Achtung App - es erfolgt keine Überprüfung des Teilnehmers */
        int rc = starteService(wePraesenzBuchen.getWeLoginVerify(), "praesenzBuchen", false, true);
        // int rc=starteService(wePraesenzBuchen.getWeLoginVerify(),"praesenzBuchen",
        // false, false);

        if (rc < 1) {
            wePraesenzBuchenRC.setRc(rc);
            return wePraesenzBuchenRC;
        }

        BlPraesenzAkkreditierung blPraesenz = new BlPraesenzAkkreditierung(eclDbM.getDbBundle());
        wePraesenzBuchenRC = blPraesenz.buchen(wePraesenzBuchen);
        if (rc == 2) {
            wePraesenzBuchenRC.setPruefeVersion(1);
        }

        beendeService();
        return wePraesenzBuchenRC;
    }

    /*************************************
     * suchen
     ***********************************/
    @POST
    @Path("suchen")
    @Produces({ MediaType.APPLICATION_JSON, MediaType.TEXT_XML })
    public WESuchenRC suchen(WESuchen weSuchen) {

        WESuchenRC weSuchenRC = new WESuchenRC();
        int rc = starteService(weSuchen.getWeLoginVerify(), "suchen", false, false);
        if (rc < 1) {
            weSuchenRC.setRc(rc);
            return weSuchenRC;
        }
        if (rc == 2) {
            weSuchenRC.setPruefeVersion(1);
        }

        if (weSuchen.suchart == 1) {
            eclDbM.getDbBundle().dbJoined.read_suchenName(weSuchen.suchBegriff);
            weSuchenRC.suchergebnis = eclDbM.getDbBundle().dbJoined.ergebnisSuchergebnis();
        }
        if (weSuchen.suchart == 2) {
            eclDbM.getDbBundle().dbJoined.read_suchenVertreter(weSuchen.suchBegriff);
            weSuchenRC.suchergebnis = eclDbM.getDbBundle().dbJoined.ergebnisSuchergebnis();
        }
        if (weSuchen.suchart == 3) {
            eclDbM.getDbBundle().dbJoined.read_suchenZutrittsIdent(weSuchen.suchBegriff);
            weSuchenRC.suchergebnis = eclDbM.getDbBundle().dbJoined.ergebnisSuchergebnis();
        }
        if (weSuchen.suchart == 4) {
            eclDbM.getDbBundle().dbJoined.read_suchenStimmkarte(weSuchen.suchBegriff);
            weSuchenRC.suchergebnis = eclDbM.getDbBundle().dbJoined.ergebnisSuchergebnis();
        }
        if (weSuchen.suchart == 5) {
            eclDbM.getDbBundle().dbJoined.read_suchenNameMeldungen(weSuchen.suchBegriff);
            weSuchenRC.suchergebnis = eclDbM.getDbBundle().dbJoined.ergebnisSuchergebnis();
        }
        if (weSuchen.suchart == 6) {
            eclDbM.getDbBundle().dbJoined.read_suchenAktionaersnummer(weSuchen.suchBegriff);
            weSuchenRC.suchergebnis = eclDbM.getDbBundle().dbJoined.ergebnisSuchergebnis();
        }
        if (weSuchen.suchart == 7) {
            EclMeldung lMeldung = new EclMeldung();
            lMeldung.zusatzfeld3 = weSuchen.suchBegriff;
            eclDbM.getDbBundle().dbMeldungen.leseZuZusatzfeld3(lMeldung);
            int anzahl = eclDbM.getDbBundle().dbMeldungen.meldungenArray.length;
            if (anzahl == 0) {
                weSuchenRC.suchergebnis = null;
            } else {
                weSuchenRC.suchergebnis = new EclSuchergebnis[anzahl];
                for (int i = 0; i < anzahl; i++) {
                    weSuchenRC.suchergebnis[i] = new EclSuchergebnis();
                    weSuchenRC.suchergebnis[i].zutrittsIdent = eclDbM
                            .getDbBundle().dbMeldungen.meldungenArray[i].zutrittsIdent;
                }
            }
        }
        if (weSuchen.suchart == 8) {
            int anz = weSuchen.suchBegriffArray.length;
            EclSuchergebnis[][] lSuchergebnisArray;
            lSuchergebnisArray = new EclSuchergebnis[anz][];
            int gesamtanzahl = 0;
            for (int i = 0; i < anz; i++) {
                eclDbM.getDbBundle().dbJoined.read_suchenZutrittsIdent(weSuchen.suchBegriffArray[i]);
                lSuchergebnisArray[i] = eclDbM.getDbBundle().dbJoined.ergebnisSuchergebnis();
                gesamtanzahl += lSuchergebnisArray[i].length;
            }

            weSuchenRC.suchergebnis = new EclSuchergebnis[gesamtanzahl];
            int offset = 0;
            for (int i = 0; i < anz; i++) {
                for (int i1 = 0; i1 < lSuchergebnisArray[i].length; i1++) {
                    weSuchenRC.suchergebnis[offset] = lSuchergebnisArray[i][i1];
                    offset++;
                }
            }

        }

        beendeService();
        return weSuchenRC;
    }

    /*************************************
     * abstimmungLeseAktivenAbstimmungsblock
     ***********************************/
    @POST
    @Path("abstimmungLeseAktivenAbstimmungsblock")
    @Produces({ MediaType.APPLICATION_JSON, MediaType.TEXT_XML })
    public WEAbstimmungLeseAktivenAbstimmungsblockRC abstimmungLeseAktivenAbstimmungsblock(
            WEAbstimmungLeseAktivenAbstimmungsblock weAbstimmungLeseAktivenAbstimmungsblock) {

        WEAbstimmungLeseAktivenAbstimmungsblockRC weAbstimmungLeseAktivenAbstimmungsblockRC = new WEAbstimmungLeseAktivenAbstimmungsblockRC();
        int rc = starteService(weAbstimmungLeseAktivenAbstimmungsblock.getWeLoginVerify(),
                "abstimmungLeseAktivenAbstimmungsblock", false, true);
        if (rc < 1) {
            weAbstimmungLeseAktivenAbstimmungsblockRC.setRc(rc);
            return weAbstimmungLeseAktivenAbstimmungsblockRC;
        }
        if (rc == 2) {
            weAbstimmungLeseAktivenAbstimmungsblockRC.setPruefeVersion(1);
        }

        BlAbstimmung blAbstimmung = new BlAbstimmung(eclDbM.getDbBundle());
        boolean erg = blAbstimmung.leseAktivenAbstimmungsblock();
        // if (erg){}
        weAbstimmungLeseAktivenAbstimmungsblockRC.aktiverAbstimmungsblockIstElektronischAktiv=blAbstimmung.aktiverAbstimmungsblockIstElektronischAktiv;
        weAbstimmungLeseAktivenAbstimmungsblockRC.aktiverAbstimmungsblock = blAbstimmung.aktiverAbstimmungsblock;
        weAbstimmungLeseAktivenAbstimmungsblockRC.abstimmungenZuAktivenBlock = blAbstimmung.abstimmungenZuAktivenBlock;
        weAbstimmungLeseAktivenAbstimmungsblockRC.abstimmungen = blAbstimmung.abstimmungen;
        weAbstimmungLeseAktivenAbstimmungsblockRC.abstimmungsVersion =blAbstimmung.abstimmungenVersion;

        weAbstimmungLeseAktivenAbstimmungsblockRC.rc = 1;

        beendeService();
        return weAbstimmungLeseAktivenAbstimmungsblockRC;
    }

    /*************************************
     * abstimmungAktionaerLesen
     ***********************************/
    @POST
    @Path("abstimmungAktionaerLesen")
    @Produces({ MediaType.APPLICATION_JSON, MediaType.TEXT_XML })
    public WEAbstimmungAktionaerLesenRC abstimmungAktionaerLesen(
            WEAbstimmungAktionaerLesen weAbstimmungAktionaerLesen) {

        WEAbstimmungAktionaerLesenRC weAbstimmungAktionaerLesenRC = new WEAbstimmungAktionaerLesenRC();
        /* TODO Achtung App - es erfolgt keine Überprüfung des Teilnehmers */
        int rc = starteService(weAbstimmungAktionaerLesen.getWeLoginVerify(), "abstimmungAktionaerLesen", false, true);
        if (rc < 1) {
            weAbstimmungAktionaerLesenRC.setRc(rc);
            return weAbstimmungAktionaerLesenRC;
        }
        if (rc == 2) {
            weAbstimmungAktionaerLesenRC.setPruefeVersion(1);
        }

        /*
         * TODO $AbstimmungKonsolidieren Hinweise: aktuell bei Tausch 1:1 über
         * zutrittsIdent gelöst. Müßte eigentlich stimmkarten sein!
         */
        int lMeldeIdent = 0;
        if (weAbstimmungAktionaerLesen.kartenklasse == KonstKartenklasse.stimmkartennummer) {
            int rc2 = eclDbM.getDbBundle().dbStimmkarten.read(weAbstimmungAktionaerLesen.stimmblockNummer, 1);
            if (rc2 < 1) {// Stimmkartennummer nicht vorhanden
                weAbstimmungAktionaerLesenRC.rc = -2;
                beendeService();
                return weAbstimmungAktionaerLesenRC;
            }
            EclStimmkarten eclStimmkarten = eclDbM.getDbBundle().dbStimmkarten.ergebnisPosition(0);
            if (eclStimmkarten.stimmkarteIstGesperrt >= 1) {// Stimmkartennummer gesperrt
                weAbstimmungAktionaerLesenRC.rc = -1;
                beendeService();
                return weAbstimmungAktionaerLesenRC;
            }
            lMeldeIdent = eclStimmkarten.meldungsIdentAktionaer;
        } else { /* Einlesen über ZutrittsIdent */
            EclZutrittsIdent lZutrittsIdent = new EclZutrittsIdent();
            lZutrittsIdent.zutrittsIdent = weAbstimmungAktionaerLesen.stimmblockNummer;
            lZutrittsIdent.zutrittsIdentNeben = weAbstimmungAktionaerLesen.neben;
            int rc2 = eclDbM.getDbBundle().dbZutrittskarten.readAktionaer(lZutrittsIdent, 1);
            if (rc2 < 1) {// ZutrittsIdent nicht vorhanden
                weAbstimmungAktionaerLesenRC.rc = -2;
                beendeService();
                return weAbstimmungAktionaerLesenRC;
            }
            EclZutrittskarten eclZutrittskarte = eclDbM.getDbBundle().dbZutrittskarten.ergebnisPosition(0);
            if (eclZutrittskarte.zutrittsIdentIstGesperrt != KonstStimmkarteIstGesperrt.aktiv) {// Eintrittskarte gesperrt
                weAbstimmungAktionaerLesenRC.rc = -1;
                beendeService();
                return weAbstimmungAktionaerLesenRC;
            }
            lMeldeIdent = eclZutrittskarte.meldungsIdentAktionaer;
        }

        EclMeldung meldung = new EclMeldung();
        meldung.meldungsIdent = lMeldeIdent;
        weAbstimmungAktionaerLesenRC.meldungsIdent = meldung.meldungsIdent;
        eclDbM.getDbBundle().dbMeldungen.leseZuMeldungsIdent(meldung);
        meldung = eclDbM.getDbBundle().dbMeldungen.meldungenArray[0];

        weAbstimmungAktionaerLesenRC.name = meldung.name;
        weAbstimmungAktionaerLesenRC.vorname = meldung.vorname;
        weAbstimmungAktionaerLesenRC.ort = meldung.ort;
        weAbstimmungAktionaerLesenRC.gattungId=meldung.liefereGattung();

        if (meldung.statusPraesenz_Delayed != 1) {// Nicht präsent
            weAbstimmungAktionaerLesenRC.rc = -4;
            if (meldung.statusWarPraesenz_Delayed == 0) {
                weAbstimmungAktionaerLesenRC.rc = -5;
            }
        }
        if (meldung.meldungstyp == 3) {// In Sammelkarte
            weAbstimmungAktionaerLesenRC.rc = -3;
            beendeService();
            return weAbstimmungAktionaerLesenRC;
        }

        BlAbstimmung blAbstimmung = new BlAbstimmung(eclDbM.getDbBundle());
        /* boolean erg= */blAbstimmung.leseAktivenAbstimmungsblock();
        weAbstimmungAktionaerLesenRC.abstimmungsVersion=blAbstimmung.abstimmungenVersion;
        // if (erg){}
        int anzAgenda = blAbstimmung.getAnzAbstimmungenInAktivemAbstimmungsblock();

        blAbstimmung.initDb(eclDbM.getDbBundle());
        weAbstimmungAktionaerLesenRC.abstimmart = new int[anzAgenda];
        for (int i = 0; i < anzAgenda; i++) {
            weAbstimmungAktionaerLesenRC.abstimmart[i] = 0;
        }
        boolean rcBool = blAbstimmung.liefereAktuelleAbstimmungZuMeldeIdent(meldung.meldungsIdent);
        if (rcBool == true) { /* Dann noch keine bishere Abstimmung vorhanden */
            for (int i = 0; i < anzAgenda; i++) {/* Bisheriges Abstimmverhalten anzeigen */
                weAbstimmungAktionaerLesenRC.abstimmart[i] = blAbstimmung
                        .liefereAktuelleMarkierungZuAbstimmungsPosition(i);
            }
        }
        if (meldung.meldungstyp == 2) {
            if (blAbstimmung.pruefeObWeisungenZuSammelkarte(meldung.meldungsIdent)) {
                weAbstimmungAktionaerLesenRC.rc = -6;

            }
        }

        beendeService();
        return weAbstimmungAktionaerLesenRC;
    }

    /*************************************
     * abstimmungSpeichern
     ***********************************/
    @POST
    @Path("abstimmungSpeichern")
    @Produces({ MediaType.APPLICATION_JSON, MediaType.TEXT_XML })
    public WEAbstimmungSpeichernRC abstimmungSpeichern(WEAbstimmungSpeichern weAbstimmungSpeichern) {

        WEAbstimmungSpeichernRC weAbstimmungSpeichernRC = new WEAbstimmungSpeichernRC();
        /* TODO Achtung App - es erfolgt keine Überprüfung des Teilnehmers */
        int rc = starteService(weAbstimmungSpeichern.getWeLoginVerify(), "abstimmungSpeichern", false, true);
        if (rc < 1) {
            weAbstimmungSpeichernRC.setRc(rc);
            return weAbstimmungSpeichernRC;
        }
        if (rc == 2) {
            weAbstimmungSpeichernRC.setPruefeVersion(1);
        }

        EclMeldung meldung = new EclMeldung();
        meldung.meldungsIdent = weAbstimmungSpeichern.meldungsIdent;
        System.out.println("WAIntern meldungsIedent=" + meldung.meldungsIdent);
        eclDbM.getDbBundle().dbMeldungen.leseZuMeldungsIdent(meldung);
        meldung = eclDbM.getDbBundle().dbMeldungen.meldungenArray[0];

        BlAbstimmung blAbstimmung = new BlAbstimmung(eclDbM.getDbBundle());
        boolean erg = blAbstimmung.leseAktivenAbstimmungsblock();
        // if (erg){}
        int anzAgenda = blAbstimmung.getAnzAbstimmungenInAktivemAbstimmungsblock();
        blAbstimmung.initDb(eclDbM.getDbBundle());
        /*
         * TODO _Abstimmung: hier noch Speichern so abändern, dass getrennt nach App,
         * Tablet etc.
         */
        blAbstimmung.starteSpeichernFuerMeldung(meldung, weAbstimmungSpeichern.zeitstempelraw, false,
                KonstWillenserklaerungWeg.abstTablet, 0, "", 1);

        for (int i = 0; i < anzAgenda; i++) {/* Bisheriges Abstimmverhalten anzeigen */
            int abstimmart = weAbstimmungSpeichern.abstimmart[i];
            blAbstimmung.setzeMarkierungZuAbstimmungsPosition(abstimmart, i, KonstWillenserklaerungWeg.abstTablet);
        }

        blAbstimmung.beendeSpeichernFuerMeldung();

        weAbstimmungSpeichernRC.abstimmart = new int[anzAgenda];
        for (int i = 0; i < anzAgenda; i++) {
            weAbstimmungSpeichernRC.abstimmart[i] = 0;
        }
        boolean rcBool = blAbstimmung.liefereAktuelleAbstimmungZuMeldeIdent(meldung.meldungsIdent);
        if (rcBool == true) {
            for (int i = 0; i < anzAgenda; i++) {/* Bisheriges Abstimmverhalten anzeigen */
                weAbstimmungSpeichernRC.abstimmart[i] = blAbstimmung.liefereAktuelleMarkierungZuAbstimmungsPosition(i);
            }
        }

        beendeService();
        return weAbstimmungSpeichernRC;
    }

    /*************************************
     * tabletSetzeStatusAbstimmungAuf
     ***********************************/
    @POST
    @Path("tabletSetzeStatusAbstimmungAuf")
    @Produces({ MediaType.APPLICATION_JSON, MediaType.TEXT_XML })
    public WETabletSetzeStatusRC tabletSetzeStatusAbstimmungAuf(WETabletSetzeStatus weTabletSetzeStatus) {

        WETabletSetzeStatusRC weTabletSetzeStatusRC = new WETabletSetzeStatusRC();
        int rc = starteService(weTabletSetzeStatus.getWeLoginVerify(), "tabletSetzeStatusAbstimmungAuf", false, false);
        if (rc < 1) {
            weTabletSetzeStatusRC.setRc(rc);
            return weTabletSetzeStatusRC;
        }
        if (rc == 2) {
            weTabletSetzeStatusRC.setPruefeVersion(1);
        }

        BlTablet blTablet = new BlTablet();
        blTablet.init(eclDbM.getDbBundle());
        blTablet.setzeStatusTabletAbstimmungOffen(weTabletSetzeStatus.tabletNummer);
        weTabletSetzeStatusRC.tabletIstPersoenlichesTablet=blTablet.tabletIstPersoenlichesTablet;
        weTabletSetzeStatusRC.tabletIstBereitsZugordnet=blTablet.tabletIstBereitsZugordnet;
        weTabletSetzeStatusRC.nummerIdentifikation=blTablet.nummerIdentifikation;
        weTabletSetzeStatusRC.anzeigeName=blTablet.anzeigeName;

        beendeService();
        return weTabletSetzeStatusRC;
    }

    /*************************************
     * tabletSetzeStatusAbstimmungBeendet
     ***********************************/
    @POST
    @Path("tabletSetzeStatusAbstimmungBeendet")
    @Produces({ MediaType.APPLICATION_JSON, MediaType.TEXT_XML })
    public WETabletSetzeStatusRC tabletSetzeStatusAbstimmungBeendet(WETabletSetzeStatus weTabletSetzeStatus) {

        WETabletSetzeStatusRC weTabletSetzeStatusRC = new WETabletSetzeStatusRC();
        int rc = starteService(weTabletSetzeStatus.getWeLoginVerify(), "tabletSetzeStatusAbstimmungBeendet", false,
                false);
        if (rc < 1) {
            weTabletSetzeStatusRC.setRc(rc);
            return weTabletSetzeStatusRC;
        }
        if (rc == 2) {
            weTabletSetzeStatusRC.setPruefeVersion(1);
        }

        BlTablet blTablet = new BlTablet();
        blTablet.init(eclDbM.getDbBundle());
        blTablet.setzeStatusTabletAbstimmungGeschlossen(weTabletSetzeStatus.tabletNummer);

        beendeService();
        return weTabletSetzeStatusRC;
    }

    /*************************************
     * hvParameterGet
     ***********************************/
    @POST
    @Path("hvParameterGet")
    @Produces({ MediaType.APPLICATION_JSON, MediaType.TEXT_XML })
    public WEHVParameterGetRC hvParameterGet(WEHVParameterGet weHVParameterGet) {

        WEHVParameterGetRC weHVParameterGetRC = new WEHVParameterGetRC();
        ;
        int rc = starteService(weHVParameterGet.getWeLoginVerify(), "hvParameterGet", false, true);
        if (rc < 1) {
            weHVParameterGetRC.setRc(rc);
            return weHVParameterGetRC;
        }
        if (rc == 2) {
            weHVParameterGetRC.setPruefeVersion(1);
        }

        int arbeitsplatzNr = weHVParameterGet.getWeLoginVerify().getArbeitsplatz();

        EhGetHVParam ehGetHVParam = blMPuffer.getHVParam(eclParamM.getClGlobalVar().mandant,
                eclParamM.getClGlobalVar().hvJahr, eclParamM.getClGlobalVar().hvNummer,
                eclParamM.getClGlobalVar().datenbereich);
        weHVParameterGetRC.hvParam = ehGetHVParam.hvParam;

        weHVParameterGetRC.paramGeraet = blMPuffer.getParamGeraetZuGeraetenummer(arbeitsplatzNr);
        weHVParameterGetRC.emittent = blMPuffer.getEmittent(eclParamM.getClGlobalVar().mandant,
                eclParamM.getClGlobalVar().hvJahr, eclParamM.getClGlobalVar().hvNummer,
                eclParamM.getClGlobalVar().datenbereich);
        weHVParameterGetRC.terminlisteTechnisch = blMPuffer.getTechnischeTermine(
                eclParamM.getClGlobalVar().mandant,
                eclParamM.getClGlobalVar().hvJahr, 
                eclParamM.getClGlobalVar().hvNummer,
                eclParamM.getClGlobalVar().datenbereich).tempTechnischeTermineArray;
        /* TODO Notlösung - gehört einfach in die Pufferverwaltung */
        eclDbM.getDbBundle().dbGeraeteLabelPrinter.read(arbeitsplatzNr);
        if (eclDbM.getDbBundle().dbGeraeteLabelPrinter.anzErgebnis() > 0) {
            EclGeraeteLabelPrinter lGeraeteLabelPrinter = eclDbM.getDbBundle().dbGeraeteLabelPrinter.ergebnisArray[0];
            weHVParameterGetRC.paramGeraet.labelDruckerIPAdresse = lGeraeteLabelPrinter.ipLabelprinter;
            System.out.println("arbeitsplatznr=" + arbeitsplatzNr + " labelDruckerIPAdresse="
                    + weHVParameterGetRC.paramGeraet.labelDruckerIPAdresse);
        }

        if (arbeitsplatzNr < 9000) {
            // eclDbM.getDbBundle().dbParameter.readGeraete_all(arbeitsplatzNr);
            weHVParameterGetRC.paramGeraet = blMPuffer.getParamGeraetZuGeraetenummer(arbeitsplatzNr);
            // eclDbM.getDbBundle().paramGeraet;
        }

        beendeService();
        return weHVParameterGetRC;
    }

    /*************************************
     * geraeteParameterGet
     ***********************************/
    @POST
    @Path("geraeteParameterGet")
    @Produces({ MediaType.APPLICATION_JSON, MediaType.TEXT_XML })
    public WEGeraeteParameterGetRC geraeteParameterGet(WEGeraeteParameterGet weGeraeteParameterGet) {

        WEGeraeteParameterGetRC weGeraeteParameterGetRC = new WEGeraeteParameterGetRC();
        ;
        int rc = starteService(weGeraeteParameterGet.getWeLoginVerify(), "geraeteParameterGet", false, false);
        if (rc < 1) {
            weGeraeteParameterGetRC.setRc(rc);
            return weGeraeteParameterGetRC;
        }
        if (rc == 2) {
            weGeraeteParameterGetRC.setPruefeVersion(1);
        }

        /* Alt: */
        // int
        // arbeitsplatzNr=weGeraeteParameterGet.getWeLoginVerify().getArbeitsplatz();
        // int geraeteSet=eclDbM.getDbBundle().paramServer.geraeteSetIdent;
        // eclDbM.getDbBundle().dbGeraeteSet.read(geraeteSet);
        // if (eclDbM.getDbBundle().dbGeraeteSet.anzErgebnis()<1){
        // weGeraeteParameterGetRC.eclGeraeteSet=new EclGeraeteSet();
        // weGeraeteParameterGetRC.eclGeraeteSet.ident=-1;
        // beendeService();
        // return weGeraeteParameterGetRC;
        // }
        // weGeraeteParameterGetRC.eclGeraeteSet=eclDbM.getDbBundle().dbGeraeteSet.ergebnisPosition(0);
        //
        // eclDbM.getDbBundle().dbParameter.readGeraete_all(arbeitsplatzNr);
        // weGeraeteParameterGetRC.paramGeraet=eclDbM.getDbBundle().dbParameter.ergParamGeraet;

        weGeraeteParameterGetRC.eclGeraeteSet = eclParamM.getEclGeraeteSet();
        weGeraeteParameterGetRC.paramGeraet = eclParamM.getParamGeraet();
        if (weGeraeteParameterGetRC.eclGeraeteSet == null || weGeraeteParameterGetRC.paramGeraet == null) {
            weGeraeteParameterGetRC.eclGeraeteSet = new EclGeraeteSet();
            weGeraeteParameterGetRC.eclGeraeteSet.ident = -1;
            beendeService();
            return weGeraeteParameterGetRC;
        }

        beendeService();
        return weGeraeteParameterGetRC;
    }

    /*************************************
     * leseEmittenten
     ***********************************/
    @POST
    @Path("leseEmittenten")
    @Produces({ MediaType.APPLICATION_JSON, MediaType.TEXT_XML })
    public WELeseEmittentenRC leseEmittenten(WELeseEmittenten weLeseEmittenten) {

        WELeseEmittentenRC weLeseEmittentenRC = new WELeseEmittentenRC();
        ;
        int rc = starteService(weLeseEmittenten.getWeLoginVerify(), "leseEmittenten", false, false);
        if (rc < 1) {
            weLeseEmittentenRC.setRc(rc);
            return weLeseEmittentenRC;
        }
        if (rc == 2) {
            weLeseEmittentenRC.setPruefeVersion(1);
        }

        BvMandanten lBvMandanten = new BvMandanten();
        weLeseEmittentenRC.emittentenListe = lBvMandanten.liefereEmittentenListeFuerNrAuswahl(eclDbM.getDbBundle());

        beendeService();
        return weLeseEmittentenRC;
    }

    /*************************************
     * kiavFuerVollmachtDritteGet
     ***********************************/
    @POST
    @Path("kiavFuerVollmachtDritteGet")
    @Produces({ MediaType.APPLICATION_JSON, MediaType.TEXT_XML })
    public WEKIAVFuerVollmachtDritteRC kiavFuerVollmachtDritteGet(WEKIAVFuerVollmachtDritte weKIAVFuerVollmachtDritte) {

        WEKIAVFuerVollmachtDritteRC weKIAVFuerVollmachtDritteRC = new WEKIAVFuerVollmachtDritteRC();
        int rc = starteService(weKIAVFuerVollmachtDritte.getWeLoginVerify(), "kiavFuerVollmachtDritteGet", false,
                false);
        if (rc < 1) {
            weKIAVFuerVollmachtDritteRC.setRc(rc);
            return weKIAVFuerVollmachtDritteRC;
        }
        if (rc == 2) {
            weKIAVFuerVollmachtDritteRC.setPruefeVersion(1);
        }

        BlSammelkarten blSammelkarten = new BlSammelkarten(true, eclDbM.getDbBundle());
        weKIAVFuerVollmachtDritteRC.listKIAVFuerVollmachtDritte = blSammelkarten.kiavFuerVollmachtDritteHV();

        beendeService();
        return weKIAVFuerVollmachtDritteRC;
    }

    /*************************************
     * appTexteHolen********************************** Holt nur die
     * Mandantenübergreifenden Texte! Mandant in diesem Fall nicht belegt!
     */
    @POST
    @Path("appTexteHolen")
    @Produces({ MediaType.APPLICATION_JSON, MediaType.TEXT_XML })
    public WEAPPTexteHolenGetRC appTexteHolen(WEAPPTexteHolenGet weAPPTexteHolenGet) {

        WEAPPTexteHolenGetRC weAPPTexteHolenGetRC = new WEAPPTexteHolenGetRC();

        int rc = starteService(weAPPTexteHolenGet.getWeLoginVerify(), "appTexteHolen", false, true);
        if (rc < 1) {
            weAPPTexteHolenGetRC.setRc(rc);
            return weAPPTexteHolenGetRC;
        }
        if (rc == 2) {
            weAPPTexteHolenGetRC.setPruefeVersion(1);
        }
        System.out.println("sprache=" + weAPPTexteHolenGet.sprache + " mandant=" + weAPPTexteHolenGet.mandant
                + " aktuelleVersion in App=" + weAPPTexteHolenGet.aktuelleVersion);

        /*
         * Diese Zeile wurde eingefügt, damit immer ALLE Texte geholt werden, egal ob
         * sie neuer sind oder nicht (weil in der App nun immer ALLES gelöscht wird vor
         * dem Update Soll das mal wieder zurückgestellt werden, dann diese Zeile
         * entfernen!
         */
        weAPPTexteHolenGet.aktuelleVersion = 0;

        BlAppVersionsabgleich blAppVersionsabgleich = new BlAppVersionsabgleich();
        weAPPTexteHolenGetRC.anzahlZeilen = blAppVersionsabgleich.holeTexteZumUpdaten(eclDbM.getDbBundle(),
                weAPPTexteHolenGet.sprache, weAPPTexteHolenGet.mandant, weAPPTexteHolenGet.aktuelleVersion);
        weAPPTexteHolenGetRC.updateTexteArray = blAppVersionsabgleich.updateTexte;

        // for (int i=0;i<blAppVersionsabgleich.updateTexte.length;i++){
        // System.out.println("i="+i+"
        // letzteVersion="+blAppVersionsabgleich.updateTexte[i].letzteVersion+"
        // sprache="+blAppVersionsabgleich.updateTexte[i].sprache);
        // }
        weAPPTexteHolenGetRC.disclaimerAppAktivNutzungsbedingungen = blAppVersionsabgleich.disclaimerAppAktivNutzungsbedingungen;
        weAPPTexteHolenGetRC.disclaimerAppAktivDatenschutz = blAppVersionsabgleich.disclaimerAppAktivDatenschutz;
        weAPPTexteHolenGetRC.disclaimerAppAktivVerwendungPersoenlicherDaten = blAppVersionsabgleich.disclaimerAppAktivVerwendungPersoenlicherDaten;

        beendeService();
        return weAPPTexteHolenGetRC;
    }

    /*************************************
     * appHVParamAPPHolen
     **********************************/
    @POST
    @Path("appHVParamAppHolen")
    @Produces({ MediaType.APPLICATION_JSON, MediaType.TEXT_XML })
    public WEAPPHVParamAppHolenRC appHVParamAppHolen(WEAPPHVParamAppHolen weAPPHVParamAppHolen) {

        WEAPPHVParamAppHolenRC weAPPHVParamAppHolenRC = new WEAPPHVParamAppHolenRC();
        int rc = starteService(weAPPHVParamAppHolen.getWeLoginVerify(), "appHVParamAppHolen", false, true);
        if (rc < 1) {
            weAPPHVParamAppHolenRC.setRc(rc);
            return weAPPHVParamAppHolenRC;
        }
        if (rc == 2) {
            weAPPHVParamAppHolenRC.setPruefeVersion(1);
        }

        weAPPHVParamAppHolenRC.paramPortal = eclDbM.getDbBundle().param.paramPortal;
        weAPPHVParamAppHolenRC.paramModuleKonfigurierbar_App = new ParamModuleKonfigurierbar_App();
        weAPPHVParamAppHolenRC.paramModuleKonfigurierbar_App.copyFromParamModuleKonfigurierbar(eclDbM.getDbBundle());
        weAPPHVParamAppHolenRC.terminlisteTechnisch = eclDbM.getDbBundle().terminlisteTechnisch;
        weAPPHVParamAppHolenRC.aktuellerEmittent = eclDbM.getDbBundle().eclEmittent;
        weAPPHVParamAppHolenRC.gattungBezeichnung = eclDbM.getDbBundle().param.paramBasis.gattungBezeichnung;
        weAPPHVParamAppHolenRC.anzahlJaJeAbstimmungsgruppe = eclDbM
                .getDbBundle().param.paramAbstimmungParameter.anzahlJaJeAbstimmungsgruppe;
        beendeService();
        return weAPPHVParamAppHolenRC;
    }

    /*************************************
     * appCheckVersion********************************** Detailcheck der Versionen
     * (Texte und App)
     */
    @POST
    @Path("appCheckVersion")
    @Produces({ MediaType.APPLICATION_JSON, MediaType.TEXT_XML })
    public WEAPPCheckVersionRC appCheckVersion(WEAPPCheckVersion weAPPCheckVersion) {

        WEAPPCheckVersionRC weAPPCheckVersionRC = new WEAPPCheckVersionRC();
        int rc = starteService(weAPPCheckVersion.getWeLoginVerify(), "appCheckVersion", false, true);
        if (rc < 1) {
            weAPPCheckVersionRC.setRc(rc);
            return weAPPCheckVersionRC;
        }

        BlAppVersionsabgleich blAppVersionsabgleich = new BlAppVersionsabgleich();
        blAppVersionsabgleich.mandantPruefen = weAPPCheckVersion.mandantPruefen;
        blAppVersionsabgleich.textVersionMandantDE = weAPPCheckVersion.textVersionMandantDE;
        blAppVersionsabgleich.textVersionMandantEN = weAPPCheckVersion.textVersionMandantEN;
        blAppVersionsabgleich.aktuelleVersionApp = weAPPCheckVersion.aktuelleVersionApp;
        blAppVersionsabgleich.checkAppVersion(eclDbM.getDbBundle());
        weAPPCheckVersionRC.mandantErgebnis = blAppVersionsabgleich.mandantErgebnis;
        weAPPCheckVersionRC.updateTextMandantDE = blAppVersionsabgleich.updateTextMandantDE;
        weAPPCheckVersionRC.updateTextMandantEN = blAppVersionsabgleich.updateTextMandantEN;
        weAPPCheckVersionRC.updateAppVersion = blAppVersionsabgleich.updateAppVersion;

        // try {
        // Thread.sleep(4000);
        // } catch (InterruptedException e) {
        // e.printStackTrace();
        // }

        beendeService();
        return weAPPCheckVersionRC;
    }

    /*************************************
     * appHoleAktiveEmittenten********************************** Holen aller der
     * Mandanten, die gerade eine aktive App unterstützen
     */
    @POST
    @Path("appHoleAktiveEmittenten")
    @Produces({ MediaType.APPLICATION_JSON, MediaType.TEXT_XML })
    public WEAPPHoleAktiveEmittentenRC appHoleAktiveEmittenten(WEAPPHoleAktiveEmittenten weAPPHoleAktiveEmittenten) {

        WEAPPHoleAktiveEmittentenRC weAPPHoleAktiveEmittentenRC = new WEAPPHoleAktiveEmittentenRC();
        int rc = starteService(weAPPHoleAktiveEmittenten.getWeLoginVerify(), "appHoleAktiveEmittenten", false, true);
        if (rc < 1) {
            weAPPHoleAktiveEmittentenRC.setRc(rc);
            return weAPPHoleAktiveEmittentenRC;
        }

        BvMandanten bvMandanten = new BvMandanten();
        weAPPHoleAktiveEmittentenRC.aktiveEmittenten = bvMandanten.liefereEmittentenMitAktiverApp(eclDbM.getDbBundle());

        beendeService();
        return weAPPHoleAktiveEmittentenRC;
    }

    /*************************************
     * appKonsolidierenKennung********************************** Holen aller der
     * Mandanten, die gerade eine aktive App unterstützen
     */
    @POST
    @Path("appKonsolidierenKennung")
    @Produces({ MediaType.APPLICATION_JSON, MediaType.TEXT_XML })
    public WEAPPKonsolidierenKennungRC appKonsoliderenKennung(WEAPPKonsolidierenKennung weAPPKonsolidierenKennung) {

        WEAPPKonsolidierenKennungRC weAPPKonsolidierenKennungRC = new WEAPPKonsolidierenKennungRC();
        int rc = starteService(weAPPKonsolidierenKennung.getWeLoginVerify(), "appKonsolidierenKennung", false, true);// Kein
                                                                                                                     // Teilnehemerlogin
                                                                                                                     // Prüfen,
                                                                                                                     // da
                                                                                                                     // dies
                                                                                                                     // in
                                                                                                                     // der
                                                                                                                     // folgenden
                                                                                                                     // Businesslogik
                                                                                                                     // kemacht
                                                                                                                     // wird!
        if (rc < 1) {
            weAPPKonsolidierenKennungRC.setRc(rc);
            return weAPPKonsolidierenKennungRC;
        }

        if (logDrucken == 3) {
            System.out.println("zugeordneteKennungen Len=" + weAPPKonsolidierenKennung.zugeordneteKennungen.length);
            if (weAPPKonsolidierenKennung.zugeordneteEmittenten != null) {
                System.out
                        .println("zugeordneteEmittenten Len=" + weAPPKonsolidierenKennung.zugeordneteEmittenten.length);
            } else {
                System.out.println("null");
            }
        }
        BlAppKennungsverwaltung blAppKennungsverwaltung = new BlAppKennungsverwaltung(eclDbM.getDbBundle());
        blAppKennungsverwaltung.zugeordneteKennungenArray = weAPPKonsolidierenKennung.zugeordneteKennungen;

        // System.out.println("WAIntern.appKonsolidierenKennung Input =
        // "+weAPPKonsolidierenKennung.zugeordneteKennungen.length);
        // for (int i=0;i<weAPPKonsolidierenKennung.zugeordneteKennungen.length;i++){
        // System.out.println("i="+i+"
        // kennung="+weAPPKonsolidierenKennung.zugeordneteKennungen[i].kennung);
        // System.out.println("i="+i+"
        // anmeldeKennungArt="+weAPPKonsolidierenKennung.zugeordneteKennungen[i].anmeldeKennungArt);
        // System.out.println("i="+i+"
        // aktienregisterIdent="+weAPPKonsolidierenKennung.zugeordneteKennungen[i].aktienregisterIdent);
        // }
        blAppKennungsverwaltung.pruefeObKennungenGueltig();
        // System.out.println("nach pruefe =
        // "+weAPPKonsolidierenKennung.zugeordneteKennungen.length);
        // for (int i=0;i<weAPPKonsolidierenKennung.zugeordneteKennungen.length;i++){
        // System.out.println("i="+i+"
        // kennung="+weAPPKonsolidierenKennung.zugeordneteKennungen[i].kennung);
        // System.out.println("i="+i+"
        // anmeldeKennungArt="+weAPPKonsolidierenKennung.zugeordneteKennungen[i].anmeldeKennungArt);
        // System.out.println("i="+i+"
        // returnVerarbeitung="+weAPPKonsolidierenKennung.zugeordneteKennungen[i].returnVerarbeitung);
        // }

        blAppKennungsverwaltung.eliminiereDuplettenInKennungen();
        // System.out.println("nach eliminiere =
        // "+weAPPKonsolidierenKennung.zugeordneteKennungen.length);
        // for (int i=0;i<weAPPKonsolidierenKennung.zugeordneteKennungen.length;i++){
        // System.out.println("i="+i+"
        // kennung="+weAPPKonsolidierenKennung.zugeordneteKennungen[i].kennung);
        // }

        blAppKennungsverwaltung.holeMeldungsStatus();

        // System.out.println("Output =
        // "+blAppKennungsverwaltung.zugeordneteKennungenArrayNeu.length);
        // for (int
        // i=0;i<blAppKennungsverwaltung.zugeordneteKennungenArrayNeu.length;i++){
        // System.out.println("i="+i+"
        // kennung="+blAppKennungsverwaltung.zugeordneteKennungenArrayNeu[i].kennung+"
        // rc="+blAppKennungsverwaltung.zugeordneteKennungenArrayNeu[i].returnVerarbeitung);
        // System.out.println("Vertretungsart="+blAppKennungsverwaltung.zugeordneteKennungenArrayNeu[i].vertretungsArt);
        // }

        blMAppEmittenten.setEclAppZugeordneteEmittenten(weAPPKonsolidierenKennung.zugeordneteEmittenten);
        blMAppEmittenten.aktualisiereEmittenten();
        weAPPKonsolidierenKennungRC.zugeordneteEmittenten = blMAppEmittenten.getEclAppZugeordneteEmittenten();

        weAPPKonsolidierenKennungRC.zugeordneteKennungen = blAppKennungsverwaltung.zugeordneteKennungenArrayNeu;

        beendeService();
        return weAPPKonsolidierenKennungRC;
    }

    /**
     * Für App - zum Übertragen der für Registrierung / Einstellungen erforderlichen
     * Werte. Die zum Aktionär gehörigen Daten werden in eclTeilnehmerLoginM mit
     * abgespeichert.
     *
     * @return
     */
    private EclRegistrierungsdaten uebertrageDatenVonDlgInEcl() {

        EclRegistrierungsdaten lRegistrierungsdaten = new EclRegistrierungsdaten();

        lRegistrierungsdaten.anzeigeMeldung = aDlgVariablen.isAnzeigeMeldung();
        lRegistrierungsdaten.anzeigeMeldungsText1 = aDlgVariablen.isAnzeigeMeldungsText1();
        lRegistrierungsdaten.anzeigeMeldungsText2 = aDlgVariablen.isAnzeigeMeldungsText2();

        lRegistrierungsdaten.emailbestaetigen = aDlgVariablen.isEmailbestaetigen();
        lRegistrierungsdaten.email2bestaetigen = aDlgVariablen.isEmail2bestaetigen();

        lRegistrierungsdaten.passwortBereitsVergeben = aDlgVariablen.isPasswortBereitsVergeben();
        lRegistrierungsdaten.ausgewaehltVergabeEigenesPasswort = aDlgVariablen.isAusgewaehltVergabeEigenesPasswort();
        lRegistrierungsdaten.neuesPasswort = aDlgVariablen.isNeuesPasswort();

        lRegistrierungsdaten.anzeigeHinweisDatenschutzerklaerung = aDlgVariablen
                .isAnzeigeHinweisDatenschutzerklaerung();
        lRegistrierungsdaten.anzeigeHinweisAktionaersPortalBestaetigen = aDlgVariablen
                .isAnzeigeHinweisAktionaersPortalBestaetigen();
        lRegistrierungsdaten.anzeigeHinweisHVPortalBestaetigen = aDlgVariablen.isAnzeigeHinweisHVPortalBestaetigen();

        lRegistrierungsdaten.tlKommunikationssprache = eclTeilnehmerLoginM.getAktienregisterZusatzM()
                .getKommunikationssprache();
        lRegistrierungsdaten.tlEMailRegistrierungAnzeige = eclTeilnehmerLoginM.getAktienregisterZusatzM()
                .iseMailRegistrierungAnzeige();
        lRegistrierungsdaten.tlEMailFuerVersand = eclTeilnehmerLoginM.getAktienregisterZusatzM().geteMailFuerVersand();
        lRegistrierungsdaten.tlEMailFuerVersandBestaetigen = eclTeilnehmerLoginM.getAktienregisterZusatzM()
                .geteMailFuerVersandBestaetigen();

        lRegistrierungsdaten.tlKontaktTelefonPrivat = eclTeilnehmerLoginM.getAktienregisterZusatzM()
                .getKontaktTelefonPrivat();
        lRegistrierungsdaten.tlKontaktTelefonGeschaeftlich = eclTeilnehmerLoginM.getAktienregisterZusatzM()
                .getKontaktTelefonGeschaeftlich();
        lRegistrierungsdaten.tlKontaktTelefonMobil = eclTeilnehmerLoginM.getAktienregisterZusatzM()
                .getKontaktTelefonMobil();
        lRegistrierungsdaten.tlKontaktTelefonFax = eclTeilnehmerLoginM.getAktienregisterZusatzM()
                .getKontaktTelefonFax();

        lRegistrierungsdaten.eingeloggtePersonArray = new String[eclTeilnehmerLoginM.getEingeloggtePersonListe()
                .size()];
        for (int i = 0; i < lRegistrierungsdaten.eingeloggtePersonArray.length; i++) {
            lRegistrierungsdaten.eingeloggtePersonArray[i] = eclTeilnehmerLoginM.getEingeloggtePersonListe().get(i);
        }

        return lRegistrierungsdaten;
    }

    /*************************************
     * registrierungDurchfuehren********************************** Ergebnis der
     * Erstregistrierung oder der Einstellungen zurückspeichern Fehlermeldungen:
     * pfErstregistrierungMittlerweileAbgeschlossen
     */
    @POST
    @Path("registrierungDurchfuehren")
    @Produces({ MediaType.APPLICATION_JSON, MediaType.TEXT_XML })
    public WERegistrierungDurchfuehrenRC registrierungDurchfuehren(
            WERegistrierungDurchfuehren weRegistrierungDurchfuehren) {

        WERegistrierungDurchfuehrenRC weRegistrierungDurchfuehrenRC = new WERegistrierungDurchfuehrenRC();
        /*
         * Keine Überprüfung des Teilnehmers - die erfolgt im folgenden in dieser
         * Routine (da sowieso eclTeilnehmerLoginM geholt werden muß!)
         */
        int rc = starteService(weRegistrierungDurchfuehren.getWeLoginVerify(), "registrierungDurchfuehren", false,
                true);
        if (rc < 1) {
            weRegistrierungDurchfuehrenRC.setRc(rc);
            return weRegistrierungDurchfuehrenRC;
        }

        int erg;
        erg = stelleEclTeilnehmerLoginMBereit(weRegistrierungDurchfuehren.getWeLoginVerify().getKennung(),
                weRegistrierungDurchfuehren.getWeLoginVerify().getPasswort(),
                weRegistrierungDurchfuehren.getWeLoginVerify().getUser(), true);

        if (erg >= 0) {
            aControllerRegistrierung.startRegistrierung();

            eclDbM.closeAll();

            aControllerRegistrierungSession.setErstregistrierung(weRegistrierungDurchfuehren.erstregistrierung);

            eclTeilnehmerLoginM.getAktienregisterZusatzM()
                    .setKommunikationssprache(weRegistrierungDurchfuehren.kommunikationssprache);
            eclTeilnehmerLoginM.getAktienregisterZusatzM()
                    .seteMailRegistrierungAnzeige(weRegistrierungDurchfuehren.eMailRegistrierungAnzeige);

            eclTeilnehmerLoginM.getAktienregisterZusatzM()
                    .seteMailFuerVersand(weRegistrierungDurchfuehren.eMailFuerVersand);
            eclTeilnehmerLoginM.getAktienregisterZusatzM()
                    .seteMailFuerVersandBestaetigen(weRegistrierungDurchfuehren.eMailFuerVersandBestaetigen);

            eclTeilnehmerLoginM.getAktienregisterZusatzM()
                    .setKontaktTelefonPrivat(weRegistrierungDurchfuehren.kontaktTelefonPrivat);
            eclTeilnehmerLoginM.getAktienregisterZusatzM()
                    .setKontaktTelefonGeschaeftlich(weRegistrierungDurchfuehren.kontaktTelefonGeschaeftlich);
            eclTeilnehmerLoginM.getAktienregisterZusatzM()
                    .setKontaktTelefonMobil(weRegistrierungDurchfuehren.kontaktTelefonMobil);
            eclTeilnehmerLoginM.getAktienregisterZusatzM()
                    .setKontaktTelefonFax(weRegistrierungDurchfuehren.kontaktTelefonFax);

            aDlgVariablen.setAusgewaehltVergabeEigenesPasswort(
                    weRegistrierungDurchfuehren.ausgewaehltVergabeEigenesPasswort);
            aDlgVariablen.setNeuesPasswort(weRegistrierungDurchfuehren.ausgewaehltNeuesPasswort);

            eclTeilnehmerLoginM.getAktienregisterZusatzM().setNeuesPasswort(weRegistrierungDurchfuehren.neuesPasswort);
            eclTeilnehmerLoginM.getAktienregisterZusatzM()
                    .setNeuesPasswortBestaetigung(weRegistrierungDurchfuehren.neuesPasswortBestaetigung);

            eclTeilnehmerLoginM.getAktienregisterZusatzM().setHinweisAktionaersPortalBestaetigtAnzeige(
                    weRegistrierungDurchfuehren.hinweisAktionaersPortalBestaetigtAnzeige);
            eclTeilnehmerLoginM.getAktienregisterZusatzM()
                    .setHinweisHVPortalBestaetigtAnzeige(weRegistrierungDurchfuehren.hinweisHVPortalBestaetigtAnzeige);

            aControllerRegistrierungSession
                    .seteMailBestaetigungVerschicken(weRegistrierungDurchfuehren.eMailBestaetigungVerschicken);

            aControllerRegistrierungSession.setWurdeUeberAppAngefordert(true);

            aControllerRegistrierungSession
                    .seteMailBestaetigungsCode(weRegistrierungDurchfuehren.eMailBestaetigungsCode);

            aControllerRegistrierung.pruefenUndSpeichern();
            if (aDlgVariablen.getFehlerNr() < 0) {
                weRegistrierungDurchfuehrenRC.rc = aDlgVariablen.getFehlerNr();
            } else {
                weRegistrierungDurchfuehrenRC.neuesPasswortVerschluesselt = aControllerRegistrierungSession
                        .getPasswortVerschluesselt();
                weRegistrierungDurchfuehrenRC.esGibtVveraenderungenFuerBestaetigungsanzeige = aControllerRegistrierungSession
                        .isEsGibtVveraenderungenFuerBestaetigungsanzeige();

                weRegistrierungDurchfuehrenRC.eMailVersandNeuRegistriert = aControllerRegistrierungSession
                        .iseMailVersandNeuRegistriert();
                weRegistrierungDurchfuehrenRC.eMailVersandDeRegistriert = aControllerRegistrierungSession
                        .iseMailVersandDeRegistriert();
                weRegistrierungDurchfuehrenRC.neueEmailAdresseEingetragen = aControllerRegistrierungSession
                        .isNeueEmailAdresseEingetragen();
                weRegistrierungDurchfuehrenRC.neueEmailAdresse = aControllerRegistrierungSession.getNeueEmailAdresse();
                weRegistrierungDurchfuehrenRC.neueEmailAdresseAusgetragen = aControllerRegistrierungSession
                        .isNeueEmailAdresseAusgetragen();
                weRegistrierungDurchfuehrenRC.emailNochNichtBestaetigt = aControllerRegistrierungSession
                        .isEmailNochNichtBestaetigt();
                weRegistrierungDurchfuehrenRC.neueEmail2AdresseEingetragen = aControllerRegistrierungSession
                        .isNeueEmail2AdresseEingetragen();
                weRegistrierungDurchfuehrenRC.neueEmail2Adresse = aControllerRegistrierungSession
                        .getNeueEmail2Adresse();
                weRegistrierungDurchfuehrenRC.neueEmail2AdresseAusgetragen = aControllerRegistrierungSession
                        .isNeueEmail2AdresseAusgetragen();
                weRegistrierungDurchfuehrenRC.email2NochNichtBestaetigt = aControllerRegistrierungSession
                        .isEmail2NochNichtBestaetigt();
                weRegistrierungDurchfuehrenRC.dauerhaftesPasswortAktiviert = aControllerRegistrierungSession
                        .isDauerhaftesPasswortAktiviert();
                weRegistrierungDurchfuehrenRC.dauerhaftesPasswortDeAktiviert = aControllerRegistrierungSession
                        .isDauerhaftesPasswortDeAktiviert();
                weRegistrierungDurchfuehrenRC.dauerhaftesPasswortGeaendert = aControllerRegistrierungSession
                        .isDauerhaftesPasswortGeaendert();

            }
        } else {// Bei Fehler: keine weiteren Daten zurückgeben!
            eclDbM.closeAll();
            weRegistrierungDurchfuehrenRC.rc = erg;
        }

        beendeServiceOhneClose();
        return weRegistrierungDurchfuehrenRC;
    }

    /*************************************
     * anmeldenAbmeldenStatus
     **********************************/
    @POST
    @Path("blVeranstaltungen")
    @Produces({ MediaType.APPLICATION_JSON, MediaType.TEXT_XML })
    public WEBlVeranstaltungenRC blVeranstaltungen(WEBlVeranstaltungen weBlVeranstaltungen) {

        WEBlVeranstaltungenRC weBlVeranstaltungenRC = new WEBlVeranstaltungenRC();
        int rc = starteService(weBlVeranstaltungen.getWeLoginVerify(), "blVeranstaltungen", true, true);
        if (rc < 1) {
            weBlVeranstaltungenRC.setRc(rc);
            return weBlVeranstaltungenRC;
        }

        int erg;
        erg = stelleEclTeilnehmerLoginMBereit(weBlVeranstaltungen.getWeLoginVerify().getKennung(),
                weBlVeranstaltungen.getWeLoginVerify().getPasswort(), weBlVeranstaltungen.getWeLoginVerify().getUser(),
                true);

        int aktionaersIdent = eclTeilnehmerLoginM.getAnmeldeIdentAktienregister();
        eclDbM.openWeitere();
        if (erg >= 0) {
            BlVeranstaltungen blVeranstaltungen = new BlVeranstaltungen(true, eclDbM.getDbBundle());
            switch (weBlVeranstaltungen.stubFunktion) {
            case 1: {
                zeigeStubText("leseAktiveVeranstaltungen");
                blVeranstaltungen.leseAktiveVeranstaltungen(aktionaersIdent);
                weBlVeranstaltungenRC.rcVeranstaltungArray = blVeranstaltungen.rcVeranstaltungArray;
                break;
            }
            case 2: {
                zeigeStubText("liefereAnmeldeStatus");
                blVeranstaltungen.liefereAnmeldeStatus(aktionaersIdent);
                weBlVeranstaltungenRC.rcAngemeldet = blVeranstaltungen.rcAngemeldet;
                weBlVeranstaltungenRC.rcAngemeldetAnzahlPersonen = blVeranstaltungen.rcAngemeldetAnzahlPersonen;
                weBlVeranstaltungenRC.rcAngemeldetZuVeranstaltung = blVeranstaltungen.rcAngemeldetZuVeranstaltung;
                break;
            }
            case 3: {
                zeigeStubText("widerrufeAnmeldung");
                blVeranstaltungen.widerrufeAnmeldung(aktionaersIdent, weBlVeranstaltungen.pDurchAktionaerOderAdmin);
                break;
            }
            case 4: {
                zeigeStubText("anmeldung");
                blVeranstaltungen.anmeldung(aktionaersIdent, weBlVeranstaltungen.pVeranstaltung,
                        weBlVeranstaltungen.pPersonenZahl, weBlVeranstaltungen.pDurchAktionaerOderAdmin, 1);
                weBlVeranstaltungenRC.rcUeberbucht = blVeranstaltungen.rcUeberbucht;
                break;
            }
            case 5: {
                /** Lahmgelegt - wg. Umbau. Damit App nicht mehr lauffähig */
                zeigeStubText("gv_liefereAnmeldeStatus");
                // blVeranstaltungen.gv_liefereAnmeldeStatus(aktionaersIdent);
                weBlVeranstaltungenRC.rcGVAngemeldet = blVeranstaltungen.rcGVAngemeldet;
                weBlVeranstaltungenRC.rcGVBundesland = blVeranstaltungen.rcGVBundesland;
                weBlVeranstaltungenRC.rcZweiPersonenMoeglich = blVeranstaltungen.rcZweiPersonenMoeglich;
                break;
            }
            case 6: {
                zeigeStubText("gv_anAbmeldung");
                blVeranstaltungen.gv_anAbmeldung(aktionaersIdent, weBlVeranstaltungen.pAnOderAbmeldenOderZwei,
                        weBlVeranstaltungen.pDurchAktionaerOderAdmin);
                break;
            }
            }
        } else {// Bei Fehler: keine weiteren Daten zurückgeben!
            eclDbM.closeAll();
            weBlVeranstaltungenRC.rc = erg;
        }

        beendeService();
        CaBug.druckeLog("weBlVeranstaltungenRC.rc=" + weBlVeranstaltungenRC.rc, logDrucken, 10);
        return weBlVeranstaltungenRC;
    }

    /**********************************
     * Parameter-etc.-Kommunikation
     ***********************************************/
    /*************************************************************************************************************/

    /*************************************
     * einstellungenHolen********************************** Registrierungsstatus
     * holen
     */
    @POST
    @Path("einstellungenHolen")
    @Produces({ MediaType.APPLICATION_JSON, MediaType.TEXT_XML })
    public WEEinstellungenHolenRC einstellungenHolen(WEEinstellungenHolen weEinstellungenHolen) {

        WEEinstellungenHolenRC weEinstellungenHolenRC = new WEEinstellungenHolenRC();
        /*
         * Keine Überprüfung des Teilnehmers - die erfolgt im folgenden in dieser
         * Routine (erstmalig für Verbindungsaufbau)
         */
        int rc = starteService(weEinstellungenHolen.getWeLoginVerify(), "registrierungDurchfuehren", false, true);
        if (rc < 1) {
            weEinstellungenHolenRC.setRc(rc);
            return weEinstellungenHolenRC;
        }

        int erg;
        erg = stelleEclTeilnehmerLoginMBereit(weEinstellungenHolen.getWeLoginVerify().getKennung(),
                weEinstellungenHolen.getWeLoginVerify().getPasswort(),
                weEinstellungenHolen.getWeLoginVerify().getUser(), true);
        eclDbM.closeAll();

        if (erg >= 0) {
            /* Aus AControllerRegistrierung.doEinstellungen kopiert */
            aControllerRegistrierung.doEinstellungen();
            weEinstellungenHolenRC.registrierungsdaten = this.uebertrageDatenVonDlgInEcl();
        } else {
            weEinstellungenHolenRC.registrierungsdaten = null;
        }

        beendeServiceOhneClose();
        return weEinstellungenHolenRC;
    }

    /**********************************
     * User-Kommunikation
     *********************************************************/
    /*************************************************************************************************************/

    /*************************************
     * pruefeLoginUser
     ***********************************/
    @POST
    @Path("pruefeLoginUser")
    @Produces({ MediaType.APPLICATION_JSON, MediaType.TEXT_XML })
    public WEPruefeLoginUserRC pruefeLoginUser(WEPruefeLoginUser wePruefeLoginUser) {

        WEPruefeLoginUserRC wePruefeLoginUserRC = new WEPruefeLoginUserRC();
        ;
        int rc = starteService(wePruefeLoginUser.getWeLoginVerify(), "pruefeLoginUser", false, false);
        if (rc < 1) {
            wePruefeLoginUserRC.setRc(rc);
            return wePruefeLoginUserRC;
        }
        if (rc == 2) {
            wePruefeLoginUserRC.setPruefeVersion(1);
        }

        /* User prüfen */
        BvUserLogin bvUserLogin = new BvUserLogin();
        rc = bvUserLogin.pruefeKennung(eclDbM.getDbBundle(), wePruefeLoginUser.pKennung, wePruefeLoginUser.pPasswort,
                wePruefeLoginUser.pPruefePasswort, wePruefeLoginUser.pNurFuerMandant, 2);
        if (rc < 0) {
            wePruefeLoginUserRC.setRc(rc);
            beendeService();
            return wePruefeLoginUserRC;
        }

        wePruefeLoginUserRC.eclUserLogin = bvUserLogin.rcUserLogin;

        if (wePruefeLoginUser.mandantPruefen) {
            /* Mandant vorhanden? */
            BvMandanten bvMandanten = new BvMandanten();
            wePruefeLoginUserRC.eclEmittenten = bvMandanten.pruefeHVVorhanden(eclDbM.getDbBundle(),
                    wePruefeLoginUser.pMandant, wePruefeLoginUser.pHVJahr, wePruefeLoginUser.pHVNummer,
                    wePruefeLoginUser.pDatenbereich);
            if (wePruefeLoginUserRC.eclEmittenten == null) {
                wePruefeLoginUserRC.setRc(CaFehler.afHVNichtVorhanden);
                beendeService();
                return wePruefeLoginUserRC;
            }

            if (wePruefeLoginUserRC.eclUserLogin
                    .pruefe_userDarfMandantBearbeiten(wePruefeLoginUserRC.eclEmittenten) == false) {
                wePruefeLoginUserRC.setRc(CaFehler.afHVMitDieserKennungNichtZulaessig);
                beendeService();
                return wePruefeLoginUserRC;
            }
        }

        beendeService();
        return wePruefeLoginUserRC;
    }

    /*************************************
     * loginPasswortAendern********************************** Ändert das Passwort
     * eines "normalen" Users. Für Rücksetzen eines Aktionärspasswort (durch den
     * Aktionär, Passwort-Vergessen-Funktion) siehe passwortSetzen
     */
    @POST
    @Path("loginPasswortAendern")
    @Produces({ MediaType.APPLICATION_JSON, MediaType.TEXT_XML })
    public WELoginPasswortAendernRC loginPasswortAendern(WELoginPasswortAendern weLoginPasswortAendern) {

        WELoginPasswortAendernRC weLoginPasswortAendernRC = new WELoginPasswortAendernRC();
        ;
        int rc = starteService(weLoginPasswortAendern.getWeLoginVerify(), "loginPasswortAendern", false, false);
        if (rc < 1) {
            weLoginPasswortAendernRC.setRc(rc);
            return weLoginPasswortAendernRC;
        }
        if (rc == 2) {
            weLoginPasswortAendernRC.setPruefeVersion(1);
        }

        /* User prüfen */
        BvUserLogin bvUserLogin = new BvUserLogin();
        rc = bvUserLogin.setzeNeuesPasswort(eclDbM.getDbBundle(), weLoginPasswortAendern.userLogin,
                weLoginPasswortAendern.altesPasswort, weLoginPasswortAendern.neuesPasswort);

        weLoginPasswortAendernRC.setRc(rc);
        beendeService();
        return weLoginPasswortAendernRC;
    }

    /*************************************
     * passwortVergessen**********************************
     */
    @POST
    @Path("passwortVergessen")
    @Produces({ MediaType.APPLICATION_JSON, MediaType.TEXT_XML })
    public WEPasswortVergessenRC passwortVergessen(WEPasswortVergessen wePasswortVergessen) {

        aDlgVariablen.clearDlg();
        aDlgVariablen.clearFehlerMeldung();
        WEPasswortVergessenRC wePasswortVergessenRC = new WEPasswortVergessenRC();
        /* Keine Überprüfung des Teilnehmers - die erfolgt im folgenden - kein Login! */
        int rc = starteService(wePasswortVergessen.getWeLoginVerify(), "passwortVergessen", false, true);
        if (rc < 1) {
            wePasswortVergessenRC.setRc(rc);
            return wePasswortVergessenRC;
        }

        eclDbM.closeAll();

        aControllerPasswortVergessenSession.setLoginKennung(wePasswortVergessen.loginKennung);
        aControllerPasswortVergessenSession.setEmailAdresse(wePasswortVergessen.emailAdresse);
        aControllerPasswortVergessenSession.setStrasse(wePasswortVergessen.strasse);
        aControllerPasswortVergessenSession.setOrt(wePasswortVergessen.ort);
        aControllerPasswortVergessenSession.setGeburtsdatum(wePasswortVergessen.geburtsdatum);

        aControllerPasswortVergessenSession.setAuswahlAnbieten(wePasswortVergessen.auswahlAnbieten);
        aControllerPasswortVergessenSession.setEingabeAnbieten(wePasswortVergessen.eingabeAnbieten);
        aControllerPasswortVergessenSession.setEingabeSelektiert(wePasswortVergessen.eingabeSelektiert);
        aControllerPasswortVergessenSession.setTextVariante(wePasswortVergessen.textVariante);
        aControllerPasswortVergessenSession.setEmailAdresse(wePasswortVergessen.emailAdresse);
        aControllerPasswortVergessenSession.setWurdeUeberAppAngefordert(wePasswortVergessen.wurdeUeberAppAngefordert);
        aLanguage.setSpracheNummer(wePasswortVergessen.sprache);
        aControllerPasswortVergessen.doPasswortZuruecksetzen();
        wePasswortVergessenRC.rc = aDlgVariablen.getFehlerNr();
        System.out.println("wePasswortVergessenRC.rc=" + wePasswortVergessenRC.rc);
        wePasswortVergessenRC.textVarianteQuittung = aControllerPasswortVergessenSession.getTextVarianteQuittung();

        beendeServiceOhneClose();
        return wePasswortVergessenRC;
    }

    /*************************************
     * passwortSetzen**********************************
     */
    @POST
    @Path("passwortSetzen")
    @Produces({ MediaType.APPLICATION_JSON, MediaType.TEXT_XML })
    public WEPasswortSetzenRC passwortSetzen(WEPasswortSetzen wePasswortSetzen) {

        WEPasswortSetzenRC wePasswortSetzenRC = new WEPasswortSetzenRC();
        /* Keine Überprüfung des Teilnehmers - die erfolgt im folgenden - kein Login! */
        int rc = starteService(wePasswortSetzen.getWeLoginVerify(), "passwortSetzenVergessen", false, true);
        if (rc < 1) {
            wePasswortSetzenRC.setRc(rc);
            return wePasswortSetzenRC;
        }

        eclDbM.closeAll();

        aControllerPasswortVergessenSession.setLoginKennung(wePasswortSetzen.loginKennung);
        aControllerPasswortVergessenSession.setBestaetigungsCode(wePasswortSetzen.bestaetigungsCode);
        aControllerPasswortVergessenSession.setPasswort(wePasswortSetzen.passwort);
        aControllerPasswortVergessenSession.setPasswortBestaetigung(wePasswortSetzen.passwortBestaetigung);
        aControllerPasswortVergessen.doSpeichern();
        wePasswortSetzenRC.rc = aControllerPasswortVergessenSession.getFehlerNr();

        beendeServiceOhneClose();
        return wePasswortSetzenRC;
    }

    /*************************************
     * tabletRuecksetzenPruefen**********************************
     */
    @POST
    @Path("tabletRuecksetzenPruefen")
    @Produces({ MediaType.APPLICATION_JSON, MediaType.TEXT_XML })
    public WETabletRuecksetzenRC tabletRuecksetzenPruefen(WETabletRuecksetzen weTabletRuecksetzen) {

        WETabletRuecksetzenRC weTabletRuecksetzenRC = new WETabletRuecksetzenRC();
        /* Keine Überprüfung des Teilnehmers - die erfolgt im folgenden - kein Login! */
        int rc = starteService(weTabletRuecksetzen.getWeLoginVerify(), "tabletRuecksetzenPruefen", false, true);
        if (rc < 1) {
            weTabletRuecksetzenRC.setRc(rc);
            return weTabletRuecksetzenRC;
        }

        BvTablets bvTablets = new BvTablets(eclDbM.getDbBundle());
        weTabletRuecksetzenRC.ruecksetzen = bvTablets.pruefeObRuecksetzen(weTabletRuecksetzen.geraeteNummer);

        beendeService();
        return weTabletRuecksetzenRC;
    }

    /*************************************
     * tabletRuecksetzen**********************************
     */
    @POST
    @Path("tabletRuecksetzen")
    @Produces({ MediaType.APPLICATION_JSON, MediaType.TEXT_XML })
    public WETabletRuecksetzenRC tabletRuecksetzen(WETabletRuecksetzen weTabletRuecksetzen) {

        WETabletRuecksetzenRC weTabletRuecksetzenRC = new WETabletRuecksetzenRC();
        /* Keine Überprüfung des Teilnehmers - die erfolgt im folgenden - kein Login! */
        int rc = starteService(weTabletRuecksetzen.getWeLoginVerify(), "tabletRuecksetzen", false, true);
        if (rc < 1) {
            weTabletRuecksetzenRC.setRc(rc);
            return weTabletRuecksetzenRC;
        }

        BvTablets bvTablets = new BvTablets(eclDbM.getDbBundle());
        bvTablets.ruecksetzenErledigt(weTabletRuecksetzen.geraeteNummer);
        weTabletRuecksetzenRC.ruecksetzen = true;

        beendeService();
        return weTabletRuecksetzenRC;
    }

    /*************************************
     * autoTest**********************************
     */
    @POST
    @Path("autoTest")
    @Produces({ MediaType.APPLICATION_JSON, MediaType.TEXT_XML })
    public WEAutoTestRC autoTest(WEAutoTest weAutoTest) {

        WEAutoTestRC weAutoTestRC = new WEAutoTestRC();
        /* Keine Überprüfung des Teilnehmers - die erfolgt im folgenden - kein Login! */
        int rc = starteService(weAutoTest.getWeLoginVerify(), "autoTest", false, true);
        if (rc < 1) {
            weAutoTestRC.setRc(rc);
            return weAutoTestRC;
        }

        eclDbM.getDbBundle().openWeitere();
        eclDbM.getDbBundle().dbAutoTest.readNext();
        EclAutoTest lAutoTest = eclDbM.getDbBundle().dbAutoTest.ergebnisPosition(0);
        weAutoTestRC.scanString = lAutoTest.scanString;
        System.out.println("autotest=" + lAutoTest.ident + " " + lAutoTest.scanString);

        beendeService();
        return weAutoTestRC;
    }

    //
    //
    // @Resource
    // WebServiceContext wsContext;
    //
    // private void beendeSession(){
    //
    // }
    //
    //

    /*************************************
     * laufScanStarten
     ***********************************/
    @POST
    @Path("laufScanStarten")
    @Produces({ MediaType.APPLICATION_JSON, MediaType.TEXT_XML })
    public WELaufScanStartenRC laufScanStarten(WELaufScanStarten weLaufScanStarten) {

        WELaufScanStartenRC weLaufScanStartenRC = new WELaufScanStartenRC();
        int rc = starteService(weLaufScanStarten.getWeLoginVerify(), "laufScanStarten", false, false);
        if (rc < 1) {
            weLaufScanStartenRC.setRc(rc);
            return weLaufScanStartenRC;
        }
        if (rc == 2) {
            weLaufScanStartenRC.setPruefeVersion(1);
        }

        blmScanlauf.setEingabeQuelle(weLaufScanStarten.getWeLoginVerify().getEingabeQuelle());
        blmScanlauf.setErteiltZeitpunkt(weLaufScanStarten.getWeLoginVerify().getErteiltZeitpunkt());

        blmScanlauf.verarbeiten();

        weLaufScanStartenRC.laufNummer = blmScanlauf.getRcAktuellerVerarbeitungslauf();

        beendeService();
        return weLaufScanStartenRC;
    }

    /*************************************
     * laufInstiProvStarten
     ***********************************/
    @POST
    @Path("laufInstiProvStarten")
    @Produces({ MediaType.APPLICATION_JSON, MediaType.TEXT_XML })
    public WELaufInstiProvStartenRC laufInstiProvStarten(WELaufInstiProvStarten weLaufInstiProvStarten) {

        WELaufInstiProvStartenRC weLaufInstiProvStartenRC = new WELaufInstiProvStartenRC();
        int rc = starteService(weLaufInstiProvStarten.getWeLoginVerify(), "laufInstiProvStarten", false, false);
        if (rc < 1) {
            weLaufInstiProvStartenRC.setRc(rc);
            return weLaufInstiProvStartenRC;
        }
        if (rc == 2) {
            weLaufInstiProvStartenRC.setPruefeVersion(1);
        }

        weLaufInstiProvStartenRC = blmInstiProv.verarbeiten(weLaufInstiProvStarten);

        beendeService();
        return weLaufInstiProvStartenRC;
    }

    /*************************************
     * laufInfoGet
     ***********************************/
    @POST
    @Path("laufInfoGet")
    @Produces({ MediaType.APPLICATION_JSON, MediaType.TEXT_XML })
    public WELaufInfoGetRC laufInfoGet(WELaufInfoGet weLaufInfoGet) {

        WELaufInfoGetRC weLaufInfoGetRC = new WELaufInfoGetRC();
        int rc = starteService(weLaufInfoGet.getWeLoginVerify(), "laufInfoGet", false, false);
        if (rc < 1) {
            weLaufInfoGetRC.setRc(rc);
            return weLaufInfoGetRC;
        }
        if (rc == 2) {
            weLaufInfoGetRC.setPruefeVersion(1);
        }

        BlVerarbeitungsLauf blVerarbeitungsLauf = new BlVerarbeitungsLauf(eclDbM.getDbBundle());
        if (weLaufInfoGet.angeforderteInfo == 0) {/* Alle Läufe */
            blVerarbeitungsLauf.leseAlleVerarbeitungslaeufeZuMandant();
            weLaufInfoGetRC.rueckgabeArt = 0;
            weLaufInfoGetRC.verarbeitungsLaeufe = blVerarbeitungsLauf.rcVerarbeitungsLaeufe;
        } else {/* Einzelner Suchlauf */
            blVerarbeitungsLauf.leseScanLaufZuVerarbeitungslauf(weLaufInfoGet.angeforderteInfo);
            weLaufInfoGetRC.rueckgabeArt = blVerarbeitungsLauf.rcVerarbeitungsLaeufe[0].verarbeitungsArt;
            weLaufInfoGetRC.verarbeitungsLaeufe = blVerarbeitungsLauf.rcVerarbeitungsLaeufe;
            weLaufInfoGetRC.verarbeitungsProtokoll = blVerarbeitungsLauf.rcVerarbeitungsProtokoll;
            weLaufInfoGetRC.scanlauf = blVerarbeitungsLauf.rcScanlauf;
        }

        beendeService();
        return weLaufInfoGetRC;
    }

    /*************************************
     * stubAbstimmungen
     ***********************************/
    @POST
    @Path("stubAbstimmungen")
    @Produces({ MediaType.APPLICATION_JSON, MediaType.TEXT_XML })
    public WEStubAbstimmungenRC stubAbstimmungen(WEStubAbstimmungen weStubAbstimmungen) {

        WEStubAbstimmungenRC weStubAbstimmungenRC = new WEStubAbstimmungenRC();
        int rc = starteService(weStubAbstimmungen.getWeLoginVerify(), "stubAbstimmungen", false, false);
        if (rc < 1) {
            weStubAbstimmungenRC.setRc(rc);
            return weStubAbstimmungenRC;
        }
        if (rc == 2) {
            weStubAbstimmungenRC.setPruefeVersion(1);
        }

        StubAbstimmungen stubAbstimmungen = new StubAbstimmungen(true, eclDbM.getDbBundle());
        switch (weStubAbstimmungen.stubFunktion) {
        case 1:
            zeigeStubText("speichernVeraenderteAbstimmungenAbhaengigVonFilterTyp");
            rc = stubAbstimmungen.speichernVeraenderteAbstimmungenAbhaengigVonFilterTyp(weStubAbstimmungen.filterTyp,
                    weStubAbstimmungen.abstimmungenArray, weStubAbstimmungen.angezeigteAbstimmungZuAbstimmungsblock,
                    weStubAbstimmungen.angezeigteAbstimmungZuStimmkarte, weStubAbstimmungen.abstimmungWurdeVeraendert);
            weStubAbstimmungenRC.abstimmungWurdeVeraendert = weStubAbstimmungen.abstimmungWurdeVeraendert;
            break;
        case 2:
            zeigeStubText("speichernVeraenderteAngezeigteAbstimmungen");
            rc = stubAbstimmungen.speichernVeraenderteAngezeigteAbstimmungen(weStubAbstimmungen.abstimmungenArray,
                    weStubAbstimmungen.abstimmungWurdeVeraendert);
            weStubAbstimmungenRC.abstimmungWurdeVeraendert = weStubAbstimmungen.abstimmungWurdeVeraendert;
            weStubAbstimmungenRC.angezeigteAbstimmungen = weStubAbstimmungen.abstimmungenArray;
            break;
        case 3:
            zeigeStubText("speichernVeraenderteAngezeigteAbstimmungsbloecke");
            rc = stubAbstimmungen.speichernVeraenderteAngezeigteAbstimmungsbloecke(
                    weStubAbstimmungen.abstimmungsblockArray, weStubAbstimmungen.abstimmungWurdeVeraendert);
            weStubAbstimmungenRC.abstimmungWurdeVeraendert = weStubAbstimmungen.abstimmungWurdeVeraendert;
            weStubAbstimmungenRC.abstimmungsblockListe = weStubAbstimmungen.abstimmungsblockArray;
            break;
        case 4:
            zeigeStubText("speichernVeraenderteAngezeigteElekStimmkarte");
            rc = stubAbstimmungen.speichernVeraenderteAngezeigteElekStimmkarte(weStubAbstimmungen.elekStimmkarteArray,
                    weStubAbstimmungen.abstimmungWurdeVeraendert);
            weStubAbstimmungenRC.abstimmungWurdeVeraendert = weStubAbstimmungen.abstimmungWurdeVeraendert;
            weStubAbstimmungenRC.stimmkarteInhalt = weStubAbstimmungen.elekStimmkarteArray;
            break;
        case 5:
            zeigeStubText("speichernListeNeueAbstimmungZuAbstimmungsblock");
            rc = stubAbstimmungen.speichernListeNeueAbstimmungZuAbstimmungsblock(
                    weStubAbstimmungen.listAbstimmungZuAbstimmungsblock);
            break;
        case 6:
            zeigeStubText("loeschenListeAbstimmungZuAbstimmungsblock");
            rc = stubAbstimmungen
                    .loeschenListeAbstimmungZuAbstimmungsblock(weStubAbstimmungen.listAbstimmungZuAbstimmungsblock);
            break;
        case 7:
            zeigeStubText("speichernListeNeueAbstimmungZuElekStimmkarte");
            rc = stubAbstimmungen
                    .speichernListeNeueAbstimmungZuElekStimmkarte(weStubAbstimmungen.listAbstimmungZuStimmkarte);
            break;
        case 8:
            zeigeStubText("insertElekStimmkarte");
            rc = stubAbstimmungen.insertElekStimmkarte(weStubAbstimmungen.elekStimmkarte);
            break;
        case 9:
            zeigeStubText("loeschenListeAbstimmungZuElekStimmkarte");
            rc = stubAbstimmungen
                    .loeschenListeAbstimmungZuElekStimmkarte(weStubAbstimmungen.listAbstimmungZuStimmkarte);
            break;
        case 10:
            zeigeStubText("loescheAbstimmung");
            rc = stubAbstimmungen.loescheAbstimmung(weStubAbstimmungen.ident);
            break;
        case 11:
            zeigeStubText("loescheAbstimmVorgang");
            rc = stubAbstimmungen.loescheAbstimmVorgang(weStubAbstimmungen.ident);
            break;
        case 12:
            zeigeStubText("loescheElekStimmkarte");
            rc = stubAbstimmungen.loescheElekStimmkarte(weStubAbstimmungen.ident);
            break;
        case 13:
            zeigeStubText("zeigeSortierungAbstimmungenInit");
            rc = stubAbstimmungen.zeigeSortierungAbstimmungenInit(weStubAbstimmungen.filterTyp,
                    weStubAbstimmungen.filterIdent, weStubAbstimmungen.sortierung);
            weStubAbstimmungenRC.angezeigteAbstimmungen = stubAbstimmungen.angezeigteAbstimmungen;
            weStubAbstimmungenRC.abstimmungWurdeVeraendert = stubAbstimmungen.abstimmungWurdeVeraendert;
            weStubAbstimmungenRC.angezeigteAbstimmungZuAbstimmungsblock = stubAbstimmungen.angezeigteAbstimmungZuAbstimmungsblock;
            weStubAbstimmungenRC.angezeigteAbstimmungZuStimmkarte = stubAbstimmungen.angezeigteAbstimmungZuStimmkarte;
            break;
        case 14:
            zeigeStubText("fuelleFilterFuerAnzeige");
            rc = stubAbstimmungen.fuelleFilterFuerAnzeige();
            weStubAbstimmungenRC.abstimmungsblockListe = stubAbstimmungen.abstimmungsblockListe;
            weStubAbstimmungenRC.stimmkarteInhalt = stubAbstimmungen.stimmkarteInhalt;
            break;
        case 15:
            zeigeStubText("fuelleComboAbstimmung");
            rc = stubAbstimmungen.fuelleComboAbstimmung();
            weStubAbstimmungenRC.angezeigteAbstimmungen = stubAbstimmungen.abstimmungListe;
            break;
        case 16:
            zeigeStubText("insertAbstimmVorgang");
            rc = stubAbstimmungen.insertAbstimmVorgang(weStubAbstimmungen.abstimmungsvorgang);
            break;
        case 17:
            zeigeStubText("insertAbstimmung");
            rc = stubAbstimmungen.insertAbstimmung(weStubAbstimmungen.abstimmung,
                    weStubAbstimmungen.abstimmungZuAbstimmungsblock);
            break;
        case 18:
            zeigeStubText("speichernReload");
            rc = stubAbstimmungen.speichernReload(weStubAbstimmungen.reloadWeisungenAbbruch,
                    weStubAbstimmungen.reloadAbstimmungenAbbruch, weStubAbstimmungen.reload);
            break;
        }

        weStubAbstimmungenRC.rc = rc;
        beendeService();
        return weStubAbstimmungenRC;
    }

    /*************************************
     * stubBlSammelkarten
     ***********************************/
    @POST
    @Path("stubBlSammelkarten")
    @Produces({ MediaType.APPLICATION_JSON, MediaType.TEXT_XML })
    public WEStubBlSammelkartenRC stubBlSammelkarten(WEStubBlSammelkarten weStubBlSammelkarten) {

        WEStubBlSammelkartenRC weStubBlSammelkartenRC = new WEStubBlSammelkartenRC();
        int rc = starteService(weStubBlSammelkarten.getWeLoginVerify(), "stubBlSammelkarten", false, false);
        if (rc < 1) {
            weStubBlSammelkartenRC.setRc(rc);
            return weStubBlSammelkartenRC;
        }
        if (rc == 2) {
            weStubBlSammelkartenRC.setPruefeVersion(1);
        }

        BlSammelkarten blSammelkarten = new BlSammelkarten(true, eclDbM.getDbBundle());
        switch (weStubBlSammelkarten.stubFunktion) {
        case 1:
            zeigeStubText("setzeWeisungFuerTOPFuerAlleAktionaereMitWeisung");
            rc = blSammelkarten.setzeWeisungFuerTOPFuerAlleAktionaereMitWeisung(weStubBlSammelkarten.weisungsPos,
                    weStubBlSammelkarten.artFuerKIAV, weStubBlSammelkarten.artFuerSRV,
                    weStubBlSammelkarten.artFuerOrganisatorisch, weStubBlSammelkarten.artFuerBriefwahl,
                    weStubBlSammelkarten.artFuerDauervollmacht);
            break;
        case 2:
            zeigeStubText("neueSammelkarteSpeichern");
            blSammelkarten.meldung = weStubBlSammelkarten.meldung;
            blSammelkarten.weisungMeldung = weStubBlSammelkarten.weisungMeldung;
            blSammelkarten.weisungMeldungSplit = weStubBlSammelkarten.weisungMeldungSplit;
            blSammelkarten.weisungMeldungRaw = weStubBlSammelkarten.weisungMeldungRaw;
            blSammelkarten.weisungMeldungSplitRaw = weStubBlSammelkarten.weisungMeldungSplitRaw;
            blSammelkarten.abstimmungsvorschlag = weStubBlSammelkarten.abstimmungsvorschlag;
            rc = blSammelkarten.neueSammelkarteSpeichern();
            break;
        case 3:
            zeigeStubText("kiavFuerVollmachtDritteHV");
            rc = 1;
            weStubBlSammelkartenRC.eclKIAVFuerVollmachtDritte = blSammelkarten.kiavFuerVollmachtDritteHV();
            break;
        case 4:
            zeigeStubText("holeSammelkartenMeldeDaten");
            rc = blSammelkarten.holeSammelkartenMeldeDaten(weStubBlSammelkarten.pNurAktive,
                    weStubBlSammelkarten.pSammelIdent);
            weStubBlSammelkartenRC.rcSammelMeldung = blSammelkarten.rcSammelMeldung;
            break;
        case 5:
            zeigeStubText("holeSammelkartenDaten");
            rc = blSammelkarten.holeSammelkartenDaten(weStubBlSammelkarten.pNurAktive,
                    weStubBlSammelkarten.pSammelIdent);
            weStubBlSammelkartenRC.rcSammelMeldung = blSammelkarten.rcSammelMeldung;
            weStubBlSammelkartenRC.rcZutrittskartenArray = blSammelkarten.rcZutrittskartenArray;
            weStubBlSammelkartenRC.rcStimmkartenArray = blSammelkarten.rcStimmkartenArray;
            weStubBlSammelkartenRC.rcWillensErklVollmachtenAnDritteArray = blSammelkarten.rcWillensErklVollmachtenAnDritteArray;
            break;
        case 6:
            zeigeStubText("leseKopfWeisungUndAktionaereZuSammelkarte");
            rc = blSammelkarten.leseKopfWeisungUndAktionaereZuSammelkarte(weStubBlSammelkarten.meldung);
            weStubBlSammelkartenRC.rcWeisungenSammelkopf = blSammelkarten.rcWeisungenSammelkopf;
            weStubBlSammelkartenRC.rcAktionaersSummen = blSammelkarten.rcAktionaersSummen;
            weStubBlSammelkartenRC.aktionaereAktiv = blSammelkarten.aktionaereAktiv;
            weStubBlSammelkartenRC.aktionaereInaktiv = blSammelkarten.aktionaereInaktiv;
            weStubBlSammelkartenRC.aktionaereWiderrufen = blSammelkarten.aktionaereWiderrufen;
            weStubBlSammelkartenRC.aktionaereGeaendert = blSammelkarten.aktionaereGeaendert;
            break;
        case 7:
            zeigeStubText("leseVertreterAllerSammelkarten");
            rc = blSammelkarten.leseVertreterAllerSammelkarten();
            weStubBlSammelkartenRC.rcVertreterListe = blSammelkarten.rcVertreterListe;
            break;
        case 8:
            zeigeStubText("neuerVertreterFuerSammelkarte");
            rc = blSammelkarten.neuerVertreterFuerSammelkarte(weStubBlSammelkarten.pSammelIdent,
                    weStubBlSammelkarten.vertreterName, weStubBlSammelkarten.vertreterVorname,
                    weStubBlSammelkarten.vertreterOrt);
            break;
        case 9:
            zeigeStubText("neuerBestehenderVertreterFuerSammelkarte");
            rc = blSammelkarten.neuerBestehenderVertreterFuerSammelkarte(weStubBlSammelkarten.pSammelIdent,
                    weStubBlSammelkarten.vertreterIdent);
            break;
        case 10:
            zeigeStubText("storniereBestehenderVertreterFuerSammelkarte");
            rc = blSammelkarten.storniereBestehenderVertreterFuerSammelkarte(weStubBlSammelkarten.pSammelIdent,
                    weStubBlSammelkarten.vertreterIdent);
            break;
        case 11:
            zeigeStubText("neueEKFuerSammelkarte");
            rc = blSammelkarten.neueEKFuerSammelkarte(weStubBlSammelkarten.pSammelIdent, weStubBlSammelkarten.ekNummer);
            break;
        case 12:
            zeigeStubText("neueEKFuerAlleSammelkarten");
            rc = blSammelkarten.neueEKFuerAlleSammelkarten();
            break;
        case 13:
            zeigeStubText("sucheNeueEKFuerSammelkarte");
            weStubBlSammelkartenRC.rcEintrittskarte = blSammelkarten.sucheNeueEKFuerSammelkarte();
            break;
        case 14:
            zeigeStubText("sperreEKderSammelkarte");
            rc = blSammelkarten.sperreEKderSammelkarte(weStubBlSammelkarten.pSammelIdent,
                    weStubBlSammelkarten.ekNummer);
            break;
        case 15:
            zeigeStubText("widerrufeMeldungInSammelkarte");
            rc = blSammelkarten.widerrufeMeldungInSammelkarte(weStubBlSammelkarten.pSammelIdent,
                    weStubBlSammelkarten.skIst, weStubBlSammelkarten.meldungIdent);
            break;
        case 16:
            zeigeStubText("widerrufeMeldungArrayInSammelkarte");
            rc = blSammelkarten.widerrufeMeldungArrayInSammelkarte(weStubBlSammelkarten.pSammelIdent,
                    weStubBlSammelkarten.skIst, weStubBlSammelkarten.meldungIdentArray);
            break;
        case 17:
            zeigeStubText("aendereWeisungMeldungInSammelkarte");
            rc = blSammelkarten.aendereWeisungMeldungInSammelkarte(weStubBlSammelkarten.pSammelIdent,
                    weStubBlSammelkarten.skIst, weStubBlSammelkarten.meldungIdent, weStubBlSammelkarten.weisungMeldung,
                    weStubBlSammelkarten.abgabe);
            break;
        case 18:
            zeigeStubText("aendernWeisungMeldungArrayInSammelkarte");
            rc = blSammelkarten.aendernWeisungMeldungArrayInSammelkarte(weStubBlSammelkarten.pSammelIdent,
                    weStubBlSammelkarten.skIst, weStubBlSammelkarten.meldungIdentArray,
                    weStubBlSammelkarten.weisungMeldungArray);
            break;
        case 19:
            zeigeStubText("neuInSammelkarteMeldung");
            rc = blSammelkarten.neuInSammelkarteMeldung(weStubBlSammelkarten.pSammelIdent, weStubBlSammelkarten.skIst,
                    weStubBlSammelkarten.skWeisungsartZulaessigNeu, weStubBlSammelkarten.meldungIdent,
                    weStubBlSammelkarten.weisungMeldung, weStubBlSammelkarten.abgabe);
            break;
        case 20:
            zeigeStubText("umbuchenSammelkarteMeldungArray");
            rc = blSammelkarten.umbuchenSammelkarteMeldungArray(weStubBlSammelkarten.pSammelIdent,
                    weStubBlSammelkarten.skIst, weStubBlSammelkarten.sammelIdentNeu, weStubBlSammelkarten.skIstNeu,
                    weStubBlSammelkarten.skWeisungsartZulaessigNeu, weStubBlSammelkarten.meldungIdentArray,
                    weStubBlSammelkarten.weisungMeldungArray);
            break;
        case 21:
            zeigeStubText("leseSammelkartenlisteFuerWeisungen");
            rc = blSammelkarten.leseSammelkartenlisteFuerWeisungen(weStubBlSammelkarten.art,
                    weStubBlSammelkarten.internetOderPapierOderHV);
            weStubBlSammelkartenRC.rcSammelkartenFuerWeisungserfassung = blSammelkarten.rcSammelkartenFuerWeisungserfassung;
            weStubBlSammelkartenRC.rcSammelkartenFuerWeisungserfassungAnzahl = blSammelkarten.rcSammelkartenFuerWeisungserfassungAnzahl;
            weStubBlSammelkartenRC.rcSammelkartenFuerWeisungserfassungMeldeIdentFuerStandard = blSammelkarten.rcSammelkartenFuerWeisungserfassungMeldeIdentFuerStandard;
            weStubBlSammelkartenRC.rcSammelkartenFuerWeisungserfassungAusgewaehlteMeldeIdent = blSammelkarten.rcSammelkartenFuerWeisungserfassungAusgewaehlteMeldeIdent;
            weStubBlSammelkartenRC.rcSammelkartenFuerWeisungAktuellerWegInternetOderPapierOderHV = blSammelkarten.rcSammelkartenFuerWeisungAktuellerWegInternetOderPapierOderHV;
            break;
        case 22:
            zeigeStubText("aktivierenSammelkarteMeldungArray");
            rc = blSammelkarten.aktivierenSammelkarteMeldungArray(weStubBlSammelkarten.pSammelIdent,
                    weStubBlSammelkarten.skIst, 
                    weStubBlSammelkarten.meldungIdentArray,
                    weStubBlSammelkarten.weisungMeldungArray);
            break;
        }

        weStubBlSammelkartenRC.rc = rc;
        beendeService();
        return weStubBlSammelkartenRC;
    }

    /*************************************
     * stubMandantAnlegen
     ***********************************/
    @POST
    @Path("stubMandantAnlegen")
    @Produces({ MediaType.APPLICATION_JSON, MediaType.TEXT_XML })
    public WEStubMandantAnlegenRC stubMandantAnlegen(WEStubMandantAnlegen weStubMandantAnlegen) {

        WEStubMandantAnlegenRC weStubMandantAnlegenRC = new WEStubMandantAnlegenRC();
        int rc = starteService(weStubMandantAnlegen.getWeLoginVerify(), "stubMandantAnlegen", false, false);
        if (rc < 1) {
            weStubMandantAnlegenRC.setRc(rc);
            return weStubMandantAnlegenRC;
        }
        if (rc == 2) {
            weStubMandantAnlegenRC.setPruefeVersion(1);
        }

        StubMandantAnlegen stubMandantAnlegen = new StubMandantAnlegen(true, eclDbM.getDbBundle());
        zeigeStubText(Integer.toString(weStubMandantAnlegen.stubFunktion));
        switch (weStubMandantAnlegen.stubFunktion) {
        case 1:
            rc = stubMandantAnlegen.holeEmittentenFuerAuswahl();
            weStubMandantAnlegenRC.emittentenListe = stubMandantAnlegen.emittentenListe;
            weStubMandantAnlegenRC.mandantenArray = stubMandantAnlegen.mandantenArray;
            break;
        case 2:
            rc = stubMandantAnlegen.anlegenTablesUndEmittent(weStubMandantAnlegen.pEmittenten);
            break;
        case 3:
            rc = stubMandantAnlegen.verwendeParameterSet(weStubMandantAnlegen.pParameterSet);
            break;
        case 4:
            rc = stubMandantAnlegen.verwendeMandantHoleParameter(weStubMandantAnlegen.pEmittenten);
            weStubMandantAnlegenRC.ergHVParam=stubMandantAnlegen.ergHVParam;
            weStubMandantAnlegenRC.isinListe=stubMandantAnlegen.isinListe;
            weStubMandantAnlegenRC.portalTexteArray=stubMandantAnlegen.portalTexteArray;
            break;
        case 5:
            stubMandantAnlegen.ergHVParam=weStubMandantAnlegen.ergHVParam;
            stubMandantAnlegen.isinListe=weStubMandantAnlegen.isinListe;
            rc = stubMandantAnlegen.uebernehmeEmittentendaten(weStubMandantAnlegen.pEmittenten);
            break;
        case 6:
            stubMandantAnlegen.portalTexteArray=weStubMandantAnlegen.portalTexteArray;
            rc = stubMandantAnlegen.uebernehmePortalAppTexte();
            break;
        case 7:
            rc = stubMandantAnlegen.sammelkartenSandardAnlegen();
            break;
        case 8:
            rc = stubMandantAnlegen.sammelkartenSandardAnlegenErgaenzen();
            break;
        case 9:
            stubMandantAnlegen.ergHVParam=weStubMandantAnlegen.ergHVParam;
            rc = stubMandantAnlegen.verwendeMandantSchreibeParameter();
            break;
       }

        weStubMandantAnlegenRC.rc = rc;
        beendeService();
        return weStubMandantAnlegenRC;
    }

    /*************************************
     * stubCtrlLogin
     ***********************************/
    @POST
    @Path("stubCtrlLogin")
    @Produces({ MediaType.APPLICATION_JSON, MediaType.TEXT_XML })
    public WEStubCtrlLoginRC stubCtrlLogin(WEStubCtrlLogin weStubCtrlLogin) {

        WEStubCtrlLoginRC weStubCtrlLoginRC = new WEStubCtrlLoginRC();
        int rc = starteService(weStubCtrlLogin.getWeLoginVerify(), "stubCtrlLogin", false, false);
        if (rc < 1) {
            weStubCtrlLoginRC.setRc(rc);
            return weStubCtrlLoginRC;
        }
        if (rc == 2) {
            weStubCtrlLoginRC.setPruefeVersion(1);
        }

        StubCtrlLogin stubCtrlLogin = new StubCtrlLogin(true, eclDbM.getDbBundle());
        switch (weStubCtrlLogin.stubFunktion) {
        case 1:
            rc = stubCtrlLogin.liefereMandantenArray();
            weStubCtrlLoginRC.rcMandantenArray = stubCtrlLogin.rcMandantenArray;
            break;
        }

        weStubCtrlLoginRC.rc = rc;
        beendeService();
        return weStubCtrlLoginRC;
    }

    /*************************************
     * stubBlTeilnehmerdatenLesen
     ***********************************/
    @POST
    @Path("stubBlTeilnehmerdatenLesen")
    @Produces({ MediaType.APPLICATION_JSON, MediaType.TEXT_XML })
    public WEStubBlTeilnehmerdatenLesenRC stubBlTeilnehmerdatenLesen(
            WEStubBlTeilnehmerdatenLesen weStubBlTeilnehmerdatenLesen) {

        WEStubBlTeilnehmerdatenLesenRC weStubBlTeilnehmerdatenLesenRC = new WEStubBlTeilnehmerdatenLesenRC();
        int rc = starteService(weStubBlTeilnehmerdatenLesen.getWeLoginVerify(), "stubBlTeilnehmerdatenLesen", false,
                false);
        if (rc < 1) {
            weStubBlTeilnehmerdatenLesenRC.setRc(rc);
            return weStubBlTeilnehmerdatenLesenRC;
        }
        if (rc == 2) {
            weStubBlTeilnehmerdatenLesenRC.setPruefeVersion(1);
        }

        BlTeilnehmerdatenLesen blTeilnehmerdatenLesen = new BlTeilnehmerdatenLesen(true, eclDbM.getDbBundle());
        switch (weStubBlTeilnehmerdatenLesen.stubFunktion) {
        case 1:
            rc = blTeilnehmerdatenLesen.leseMeldung(weStubBlTeilnehmerdatenLesen.pVorbelegungKartenklasse,
                    weStubBlTeilnehmerdatenLesen.pEingabeString, weStubBlTeilnehmerdatenLesen.pZulaessigeKartenarten);
            weStubBlTeilnehmerdatenLesenRC.rcFehlerMeldungText = blTeilnehmerdatenLesen.rcFehlerMeldungText;
            weStubBlTeilnehmerdatenLesenRC.rcEclMeldung = blTeilnehmerdatenLesen.rcEclMeldung;
            weStubBlTeilnehmerdatenLesenRC.rcPersonNatJurIdent = blTeilnehmerdatenLesen.rcPersonNatJurIdent;
            break;
        }

        weStubBlTeilnehmerdatenLesenRC.rc = rc;
        beendeService();
        return weStubBlTeilnehmerdatenLesenRC;
    }

    /*************************************
     * stubBlAbstimmungenWeisungenErfassen
     ***********************************/
    @POST
    @Path("stubBlAbstimmungenWeisungenErfassen")
    @Produces({ MediaType.APPLICATION_JSON, MediaType.TEXT_XML })
    public WEStubBlAbstimmungenWeisungenErfassenRC stubBlAbstimmungenWeisungenErfassen(
            WEStubBlAbstimmungenWeisungenErfassen weStubBlAbstimmungenWeisungenErfassen) {

        WEStubBlAbstimmungenWeisungenErfassenRC weStubBlAbstimmungenWeisungenErfassenRC = new WEStubBlAbstimmungenWeisungenErfassenRC();
        int rc = starteService(weStubBlAbstimmungenWeisungenErfassen.getWeLoginVerify(),
                "stubBlAbstimmungenWeisungenErfassen", false, false);
        if (rc < 1) {
            weStubBlAbstimmungenWeisungenErfassenRC.setRc(rc);
            return weStubBlAbstimmungenWeisungenErfassenRC;
        }
        if (rc == 2) {
            weStubBlAbstimmungenWeisungenErfassenRC.setPruefeVersion(1);
        }

        BlAbstimmungenWeisungen blAbstimmungenWeisungenErfassen = new BlAbstimmungenWeisungen(true,
                eclDbM.getDbBundle());
        switch (weStubBlAbstimmungenWeisungenErfassen.stubFunktion) {
        case 1:
            rc = blAbstimmungenWeisungenErfassen.leseAgendaWeisungen(weStubBlAbstimmungenWeisungenErfassen.sicht,
                    weStubBlAbstimmungenWeisungenErfassen.gegenantraegeSeparat,
                    weStubBlAbstimmungenWeisungenErfassen.aktuellOderPreview);
            weStubBlAbstimmungenWeisungenErfassenRC.rcAgendaArray = blAbstimmungenWeisungenErfassen.rcAgendaArray;
            weStubBlAbstimmungenWeisungenErfassenRC.rcGegenantraegeArray = blAbstimmungenWeisungenErfassen.rcGegenantraegeArray;
            weStubBlAbstimmungenWeisungenErfassenRC.rcAnzAgendaArray = blAbstimmungenWeisungenErfassen.rcAnzAgendaArray;
            weStubBlAbstimmungenWeisungenErfassenRC.rcAnzGegenantraegeArray = blAbstimmungenWeisungenErfassen.rcAnzGegenantraegeArray;
            break;
        case 2:
            blAbstimmungenWeisungenErfassen.rcWeisungMeldung = weStubBlAbstimmungenWeisungenErfassen.rcWeisungMeldung;
            blAbstimmungenWeisungenErfassen.rcWeisungMeldungRaw = weStubBlAbstimmungenWeisungenErfassen.rcWeisungMeldungRaw;
            weStubBlAbstimmungenWeisungenErfassenRC.rcArray = blAbstimmungenWeisungenErfassen.erzeugeSRVHV(
                    weStubBlAbstimmungenWeisungenErfassen.ausgewaehlteKIAV,
                    weStubBlAbstimmungenWeisungenErfassen.meldungsIdent, weStubBlAbstimmungenWeisungenErfassen.quelle);
            weStubBlAbstimmungenWeisungenErfassenRC.rcWeisungMeldungRaw = blAbstimmungenWeisungenErfassen.rcWeisungMeldungRaw;
            weStubBlAbstimmungenWeisungenErfassenRC.rcWeisungMeldung = blAbstimmungenWeisungenErfassen.rcWeisungMeldung;
            break;
        case 3:
            weStubBlAbstimmungenWeisungenErfassenRC.eclScanArray = blAbstimmungenWeisungenErfassen.scanDateiInit(
                    KonstWillenserklaerung.verlassenUndVollmachtUndWeisungAnSRV,
                    weStubBlAbstimmungenWeisungenErfassen.pAusgewaehlteKartenklasse,
                    weStubBlAbstimmungenWeisungenErfassen.pZulaessigeKartenarten,
                    weStubBlAbstimmungenWeisungenErfassen.pSammelkartenFuerGattungen);
            break;
        case 4:
            blAbstimmungenWeisungenErfassen.ausgewaehlteKartenklasse = weStubBlAbstimmungenWeisungenErfassen.pAusgewaehlteKartenklasse;
            blAbstimmungenWeisungenErfassen.zulaessigeKartenarten = weStubBlAbstimmungenWeisungenErfassen.pZulaessigeKartenarten;
            blAbstimmungenWeisungenErfassen.sammelkartenFuerGattungen = weStubBlAbstimmungenWeisungenErfassen.pSammelkartenFuerGattungen;

            blAbstimmungenWeisungenErfassen.rcAgendaArray = weStubBlAbstimmungenWeisungenErfassen.rcAgendaArray;
            blAbstimmungenWeisungenErfassen.rcGegenantraegeArray = weStubBlAbstimmungenWeisungenErfassen.rcGegenantraegeArray;
            blAbstimmungenWeisungenErfassen.rcAnzAgendaArray = weStubBlAbstimmungenWeisungenErfassen.rcAnzAgendaArray;
            blAbstimmungenWeisungenErfassen.rcAnzGegenantraegeArray = weStubBlAbstimmungenWeisungenErfassen.rcAnzGegenantraegeArray;

            weStubBlAbstimmungenWeisungenErfassenRC.rcArray = blAbstimmungenWeisungenErfassen
                    .scanDateiVerarbeiten(weStubBlAbstimmungenWeisungenErfassen.pScanBuendel);
            weStubBlAbstimmungenWeisungenErfassenRC.rcGeleseneNummer = blAbstimmungenWeisungenErfassen.rcGeleseneNummer;
            weStubBlAbstimmungenWeisungenErfassenRC.rcFehlerMeldungString = blAbstimmungenWeisungenErfassen.rcFehlerMeldungString;
            break;
        }

        weStubBlAbstimmungenWeisungenErfassenRC.rc = rc;
        beendeService();
        return weStubBlAbstimmungenWeisungenErfassenRC;
    }

    /*************************************
     * stubBlAbstimmungenWeisungenErfassen
     ***********************************/
    @POST
    @Path("stubRepSammelkarten")
    @Produces({ MediaType.APPLICATION_JSON, MediaType.TEXT_XML })
    public WEStubRepSammelkartenRC stubRepSammelkarten(WEStubRepSammelkarten weStubRepSammelkarten) {

        WEStubRepSammelkartenRC weStubRepSammelkartenRC = new WEStubRepSammelkartenRC();
        int rc = starteService(weStubRepSammelkarten.getWeLoginVerify(), "stubRepSammelkarten", false, false);
        if (rc < 1) {
            weStubRepSammelkartenRC.setRc(rc);
            return weStubRepSammelkartenRC;
        }
        if (rc == 2) {
            weStubRepSammelkartenRC.setPruefeVersion(1);
        }

        RepSammelkarten repSammelkarten = new RepSammelkarten(true, eclDbM.getDbBundle(),
                KonstWeisungserfassungSicht.internWeisungsreports);
        switch (weStubRepSammelkarten.stubFunktion) {
        case 1:
            weStubRepSammelkartenRC.anzahlSaetze = repSammelkarten.weisungsKontrolle_sub_erzeugeNeuenDrucklauf(
                    weStubRepSammelkarten.arbeitsplatznr, weStubRepSammelkarten.benutzernr);
            weStubRepSammelkartenRC.neuerDrucklaufNr = repSammelkarten.neuerDrucklaufNr;
            rc = 1;
            break;

        case 2:
            weStubRepSammelkartenRC.anzahlSaetze = repSammelkarten
                    .weisungsKontrolle_sub_leseDaten(weStubRepSammelkarten.pDrucklaufnr);
            weStubRepSammelkartenRC.ergebnisArrayMeldung = repSammelkarten.ergebnisArrayMeldung;
            weStubRepSammelkartenRC.ergebnisArrayWillenserklaerung = repSammelkarten.ergebnisArrayWillenserklaerung;
            weStubRepSammelkartenRC.ergebnisArrayWeisungMeldung = repSammelkarten.ergebnisArrayWeisungMeldung;
            rc = 1;
            break;
        }

        weStubRepSammelkartenRC.rc = rc;
        beendeService();
        return weStubRepSammelkartenRC;
    }

    /*************************************
     * stubBlInsti
     ***********************************/
    @POST
    @Path("stubBlInsti")
    @Produces({ MediaType.APPLICATION_JSON, MediaType.TEXT_XML })
    public WEStubBlInstiRC stubBlInsti(WEStubBlInsti weStubBlInsti) {

        WEStubBlInstiRC weStubBlInstiRC = new WEStubBlInstiRC();
        int rc = starteService(weStubBlInsti.getWeLoginVerify(), "stubBlInsti", false, false);
        if (rc < 1) {
            weStubBlInstiRC.setRc(rc);
            return weStubBlInstiRC;
        }
        if (rc == 2) {
            weStubBlInstiRC.setPruefeVersion(1);
        }

        eclDbM.openWeitere();
        BlInsti blInsti = new BlInsti(true, eclDbM.getDbBundle());
        switch (weStubBlInsti.stubFunktion) {
        case 1:
            zeigeStubText("holeInstiDaten");
            rc = blInsti.holeInstiDaten(weStubBlInsti.pInstiIdent);
            weStubBlInstiRC.rcInsti = blInsti.rcInsti;
            break;
        case 2:
            zeigeStubText("neueInstiSpeichern");
            blInsti.insti = weStubBlInsti.insti;
            rc = blInsti.neueInstiSpeichern();
            break;
        case 3:
            zeigeStubText("leseInstiZusatzdaten");
            rc = blInsti.leseInstiZusatzdaten(weStubBlInsti.insti);
            weStubBlInstiRC.rcSuchlaufBegriffe = blInsti.rcSuchlaufBegriffe;
            weStubBlInstiRC.rcEclInstiSubZuordnungUeblichArray = blInsti.rcEclInstiSubZuordnungUeblichArray;
            weStubBlInstiRC.rcEclInstiSubZuordnungTatsaechlichArray = blInsti.rcEclInstiSubZuordnungTatsaechlichArray;
            break;
        case 4:
            zeigeStubText("speichereInstiSubZuordnung");
            rc = blInsti.speichereInstiSubZuordnung(weStubBlInsti.pGlobal, weStubBlInsti.pInstiIdent,
                    weStubBlInsti.pInstiSubZuordnung);
            break;
        case 5:
            zeigeStubText("aendernInstiSpeichern");
            rc = blInsti.aendernInstiSpeichern(weStubBlInsti.insti, weStubBlInsti.suchlaufBegriffe);
            weStubBlInstiRC.insti = blInsti.insti;
            weStubBlInstiRC.rcSuchlaufBegriffe = blInsti.rcSuchlaufBegriffe;
            break;
        case 6:
            zeigeStubText("leseInstiKennungen");
            rc = blInsti.leseInstiKennungen(weStubBlInsti.insti);
            weStubBlInstiRC.rcUserLoginZuInsti = blInsti.rcUserLoginZuInsti;
            break;
        case 7:
            zeigeStubText("liefereZugeordneteAktienZahlFuerAktionaersnummer");
            rc = blInsti.liefereZugeordneteAktienZahlFuerAktionaersnummer(weStubBlInsti.pInstiIdent,
                    weStubBlInsti.pAktionaersIdent);
            weStubBlInstiRC.rcAktienzahl = blInsti.rcAktienzahl;
            break;
        case 8:
            zeigeStubText("fuelleInstiBestandsZuordnung");
            rc = blInsti.fuelleInstiBestandsZuordnung(weStubBlInsti.pInstiIdent);
            weStubBlInstiRC.rcRegInstiBestandsZuordnung = blInsti.rcRegInstiBestandsZuordnung;
            weStubBlInstiRC.rcRegAktienregister = blInsti.rcRegAktienregister;
            weStubBlInstiRC.rcRegMeldung = blInsti.rcRegMeldung;
            weStubBlInstiRC.rcRegUserLogin = blInsti.rcRegUserLogin;

            weStubBlInstiRC.rcMeldInstiBestandsZuordnung = blInsti.rcMeldInstiBestandsZuordnung;
            weStubBlInstiRC.rcMeldAktienregister = blInsti.rcMeldAktienregister;
            weStubBlInstiRC.rcMeldMeldung = blInsti.rcMeldMeldung;
            weStubBlInstiRC.rcMeldUserLogin = blInsti.rcMeldUserLogin;

            break;
        case 9:
            zeigeStubText("loeschenZuordnung");
            rc = blInsti.loeschenZuordnung(weStubBlInsti.pZuordnungenLoeschen);

            break;

        }

        weStubBlInstiRC.rc = rc;
        beendeService();
        return weStubBlInstiRC;
    }

    /*************************************
     * stubBlSuchlauf
     ***********************************/
    @POST
    @Path("stubBlSuchlauf")
    @Produces({ MediaType.APPLICATION_JSON, MediaType.TEXT_XML })
    public WEStubBlSuchlaufRC stubBlSuchlauf(WEStubBlSuchlauf weBlStubSuchlauf) {

        WEStubBlSuchlaufRC weBlStubSuchlaufRC = new WEStubBlSuchlaufRC();
        int rc = starteService(weBlStubSuchlauf.getWeLoginVerify(), "stubBlSuchlauf", false, false);
        if (rc < 1) {
            weBlStubSuchlaufRC.setRc(rc);
            return weBlStubSuchlaufRC;
        }
        if (rc == 2) {
            weBlStubSuchlaufRC.setPruefeVersion(1);
        }

        eclDbM.openWeitere();
        BlSuchlauf blSuchlauf = new BlSuchlauf(true, eclDbM.getDbBundle());
        switch (weBlStubSuchlauf.stubFunktion) {
        case 1:
            zeigeStubText("leseSuchlaufDefinition");
            rc = blSuchlauf.leseSuchlaufDefinition(weBlStubSuchlauf.pVerwendung, weBlStubSuchlauf.pParameter1,
                    weBlStubSuchlauf.pParameter2, weBlStubSuchlauf.pParameter3, weBlStubSuchlauf.pParameter4,
                    weBlStubSuchlauf.pParameter5);
            weBlStubSuchlaufRC.eclSuchlaufDefinition = blSuchlauf.eclSuchlaufDefinition;
            weBlStubSuchlaufRC.suchlaufBegriffe = blSuchlauf.suchlaufBegriffe;
            break;
        case 2:
            zeigeStubText("erzeugeSuchlaufDefinition");
            rc = blSuchlauf.erzeugeSuchlaufDefinition(weBlStubSuchlauf.pSuchlaufDefinition);
            weBlStubSuchlaufRC.eclSuchlaufDefinition = blSuchlauf.eclSuchlaufDefinition;
            break;
        case 3:
            zeigeStubText("leseSuchlaufErgebnisEin");
            blSuchlauf.eclSuchlaufDefinition = weBlStubSuchlauf.pSuchlaufDefinition;
            rc = blSuchlauf.leseSuchlaufErgebnisEin();
            weBlStubSuchlaufRC.suchlaufErgebnisAlleListe = blSuchlauf.suchlaufErgebnisAlleListe;
            weBlStubSuchlaufRC.aktienregisterAlleListe = blSuchlauf.aktienregisterAlleListe;
            weBlStubSuchlaufRC.meldungAlleListe = blSuchlauf.meldungAlleListe;
            break;
        case 4:
            zeigeStubText("holeNeueSuchlaufIdents");
            rc = blSuchlauf.holeNeueSuchlaufIdents(weBlStubSuchlauf.pAnzahl);
            weBlStubSuchlaufRC.neueSuchlaufIdents = blSuchlauf.neueSuchlaufIdents;
            break;
        case 5:
            zeigeStubText("speichern");
            blSuchlauf.suchlaufErgebnisAlleListe = weBlStubSuchlauf.suchlaufErgebnisAlleListe;
            rc = blSuchlauf.speichern(weBlStubSuchlauf.pMitReset);
            weBlStubSuchlaufRC.suchlaufErgebnisAlleListe = blSuchlauf.suchlaufErgebnisAlleListe;
            break;
        case 6:
            zeigeStubText("sucheNachSuchbegriffen");
            blSuchlauf.suchlaufErgebnisAlleListe = weBlStubSuchlauf.suchlaufErgebnisAlleListe;
            blSuchlauf.aktienregisterAlleListe = weBlStubSuchlauf.aktienregisterAlleListe;
            blSuchlauf.meldungAlleListe = weBlStubSuchlauf.meldungAlleListe;
            blSuchlauf.suchlaufBegriffe = weBlStubSuchlauf.suchlaufBegriffe;
            blSuchlauf.eclSuchlaufDefinition = weBlStubSuchlauf.pSuchlaufDefinition;
            rc = blSuchlauf.sucheNachSuchbegriffen(weBlStubSuchlauf.aktienregisterOderMeldungen,
                    weBlStubSuchlauf.nurAktionaereOderNurVertreterOderAktionaereVertreter,
                    weBlStubSuchlauf.durchsuchenSammelkarten, weBlStubSuchlauf.durchsuchenInSammelkarten,
                    weBlStubSuchlauf.durchsuchenGaeste, weBlStubSuchlauf.auchInaktiveAufnehmen);
            weBlStubSuchlaufRC.suchlaufErgebnisAlleListe = blSuchlauf.suchlaufErgebnisAlleListe;
            weBlStubSuchlaufRC.aktienregisterAlleListe = blSuchlauf.aktienregisterAlleListe;
            weBlStubSuchlaufRC.meldungAlleListe = blSuchlauf.meldungAlleListe;
            weBlStubSuchlaufRC.rcNichtMehrEnthalten = blSuchlauf.rcNichtMehrEnthalten;
            weBlStubSuchlaufRC.rcAnzahlErgaenzt = blSuchlauf.rcAnzahlErgaenzt;
            weBlStubSuchlaufRC.rcSchonVorhanden = blSuchlauf.rcSchonVorhanden;
            break;
        }

        weBlStubSuchlaufRC.rc = rc;
        beendeService();
        return weBlStubSuchlaufRC;
    }

    /*************************************
     * stubBlSuchen
     ***********************************/
    @POST
    @Path("stubBlSuchen")
    @Produces({ MediaType.APPLICATION_JSON, MediaType.TEXT_XML })
    public WEStubBlSuchenRC stubBlSuchen(WEStubBlSuchen weBlStubSuchen) {

        WEStubBlSuchenRC weBlStubSuchenRC = new WEStubBlSuchenRC();
        int rc = starteService(weBlStubSuchen.getWeLoginVerify(), "stubBlSuchen", false, false);
        if (rc < 1) {
            weBlStubSuchenRC.setRc(rc);
            return weBlStubSuchenRC;
        }
        if (rc == 2) {
            weBlStubSuchenRC.setPruefeVersion(1);
        }

        eclDbM.openWeitere();
        BlSuchen blSuchen = new BlSuchen(true, eclDbM.getDbBundle());
        switch (weBlStubSuchen.stubFunktion) {
        case 1:
            zeigeStubText("sucheAusfuehren");
            rc = blSuchen.sucheAusfuehren(weBlStubSuchen.pSuchlaufbegriffArt, weBlStubSuchen.pSuchbegriff,
                    weBlStubSuchen.pDurchsuchenSammelkarten, weBlStubSuchen.pDurchsuchenInSammelkarten,
                    weBlStubSuchen.pDurchsuchenGaeste, weBlStubSuchen.pDurchsuchenNurGaeste);
            weBlStubSuchenRC.rcAktienregister = blSuchen.rcAktienregister;
            weBlStubSuchenRC.rcMeldungen = blSuchen.rcMeldungen;
            weBlStubSuchenRC.rcSonstigeVollmacht = blSuchen.rcSonstigeVollmacht;

            weBlStubSuchenRC.rcStimmkarten = blSuchen.rcStimmkarten;
            weBlStubSuchenRC.rcZutrittskarten = blSuchen.rcZutrittskarten;

            break;
        }

        weBlStubSuchenRC.rc = rc;
        beendeService();
        return weBlStubSuchenRC;
    }

    /*************************************
     * stubBlDrucklauf
     ***********************************/
    @POST
    @Path("stubBlDrucklauf")
    @Produces({ MediaType.APPLICATION_JSON, MediaType.TEXT_XML })
    public WEStubBlDrucklaufRC stubBlDrucklauf(WEStubBlDrucklauf weStubBlDrucklauf) {

        WEStubBlDrucklaufRC weStubBlDrucklaufRC = new WEStubBlDrucklaufRC();
        int rc = starteService(weStubBlDrucklauf.getWeLoginVerify(), "stubBlDrucklauf", false, false);
        if (rc < 1) {
            weStubBlDrucklaufRC.setRc(rc);
            return weStubBlDrucklaufRC;
        }
        if (rc == 2) {
            weStubBlDrucklaufRC.setPruefeVersion(1);
        }

        eclDbM.openWeitere();
        BlDrucklauf blDrucklauf = new BlDrucklauf(true, eclDbM.getDbBundle());
        switch (weStubBlDrucklauf.stubFunktion) {
        case 1:
            zeigeStubText("sucheAusfuehren");
            rc = blDrucklauf.erzeugeNeuenDrucklauf(weStubBlDrucklauf.arbeitsplatznr, weStubBlDrucklauf.benutzernr,
                    weStubBlDrucklauf.pVerarbeitungslaufArt, weStubBlDrucklauf.pVerarbeitungslaufSubArt, weStubBlDrucklauf.pMandantenabhaengigOderGlobal);
            weStubBlDrucklaufRC.rcDrucklauf = blDrucklauf.rcDrucklauf;
            break;
        case 2:
            zeigeStubText("updateAnzDrucklauf");
            rc = blDrucklauf.updateAnzDrucklauf(weStubBlDrucklauf.pAnzahl, weStubBlDrucklauf.pMandantenabhaengigOderGlobal);
            weStubBlDrucklaufRC.rcDrucklauf = blDrucklauf.rcDrucklauf;
            break;
        }

        weStubBlDrucklaufRC.rc = rc;
        beendeService();
        return weStubBlDrucklaufRC;
    }

    /*************************************
     * stubRepSammelAnmeldebogen
     ***********************************/
    @POST
    @Path("stubRepSammelAnmeldebogen")
    @Produces({ MediaType.APPLICATION_JSON, MediaType.TEXT_XML })
    public WEStubRepSammelAnmeldebogenRC stubRepSammelAnmeldebogen(
            WEStubRepSammelAnmeldebogen weStubRepSammelAnmeldebogen) {

        WEStubRepSammelAnmeldebogenRC weStubRepSammelAnmeldebogenRC = new WEStubRepSammelAnmeldebogenRC();
        int rc = starteService(weStubRepSammelAnmeldebogen.getWeLoginVerify(), "stubRepSammelAnmeldebogen", false,
                false);
        if (rc < 1) {
            weStubRepSammelAnmeldebogenRC.setRc(rc);
            return weStubRepSammelAnmeldebogenRC;
        }
        if (rc == 2) {
            weStubRepSammelAnmeldebogenRC.setPruefeVersion(1);
        }

        eclDbM.openWeitere();
        RepSammelAnmeldebogen repSammelAnmeldebogen = new RepSammelAnmeldebogen(true, eclDbM.getDbBundle());
        switch (weStubRepSammelAnmeldebogen.stubFunktion) {
        case 1:
            zeigeStubText("sammelAnmeldebogenDrucklauf_sub_erzeugeNeuenDrucklauf");
            rc = repSammelAnmeldebogen.sammelAnmeldebogenDrucklauf_sub_erzeugeNeuenDrucklauf(
                    weStubRepSammelAnmeldebogen.arbeitsplatznr, weStubRepSammelAnmeldebogen.benutzernr,
                    weStubRepSammelAnmeldebogen.pEclInsti);
            weStubRepSammelAnmeldebogenRC.neuerDrucklaufNr = repSammelAnmeldebogen.neuerDrucklaufNr;
            break;
        }

        weStubRepSammelAnmeldebogenRC.rc = rc;
        beendeService();
        return weStubRepSammelAnmeldebogenRC;
    }

    /*************************************
     * stubParameter
     ***********************************/
    @POST
    @Path("stubParameter")
    @Produces({ MediaType.APPLICATION_JSON, MediaType.TEXT_XML })
    public WEStubParameterRC stubParameter(WEStubParameter weStubParameter) {

        WEStubParameterRC weStubParameterRC = new WEStubParameterRC();
        int rc = starteService(weStubParameter.getWeLoginVerify(), "stubParameter", false, false);
        if (rc < 1) {
            weStubParameterRC.setRc(rc);
            return weStubParameterRC;
        }
        if (rc == 2) {
            weStubParameterRC.setPruefeVersion(1);
        }

        eclDbM.openWeitere();
        StubParameter stubParameter = new StubParameter(true, eclDbM.getDbBundle());
        switch (weStubParameter.stubFunktion) {
        case 1:
            zeigeStubText("updateHVParam_all");
            rc = stubParameter.updateHVParam_all(weStubParameter.pHVParam);
            break;
        case 2:
            zeigeStubText("leseHVParam_all");
            rc = stubParameter.leseHVParam_all(weStubParameter.pMandant, weStubParameter.pHvJahr,
                    weStubParameter.pHvNummer, weStubParameter.pDatenbereich);
            weStubParameterRC.rcHVParam = stubParameter.rcHVParam;
            break;
        case 3:
            zeigeStubText("belegeStandardMandant");
            rc = stubParameter.belegeStandardMandant(weStubParameter.pMandant);
            weStubParameterRC.rcHVParam = stubParameter.rcHVParam;
            weStubParameterRC.rcMandant = stubParameter.rcMandant;
            weStubParameterRC.rcHvJahr = stubParameter.rcHvJahr;
            weStubParameterRC.rcHvNummer = stubParameter.rcHvNummer;
            weStubParameterRC.rcDatenbereich = stubParameter.rcDatenbereich;
            break;
        case 4:
            zeigeStubText("leseEmittent");
            rc = stubParameter.leseEmittent(weStubParameter.pMandant, weStubParameter.pHvJahr,
                    weStubParameter.pHvNummer, weStubParameter.pDatenbereich);
            weStubParameterRC.rcEmittent = stubParameter.rcEmittent;
            break;
        case 5:
            zeigeStubText("updateEmittent");
            rc = stubParameter.updateEmittent(weStubParameter.pEmittent);
            break;
        }

        weStubParameterRC.rc = rc;
        beendeService();
        return weStubParameterRC;
    }

    /*************************************
     * stubBlPersonenprognose
     ***********************************/
    @POST
    @Path("stubBlPersonenprognose")
    @Produces({ MediaType.APPLICATION_JSON, MediaType.TEXT_XML })
    public WEStubBlPersonenprognoseRC stubBlPersonenprognose(WEStubBlPersonenprognose weStubBlPersonenprognose) {

        WEStubBlPersonenprognoseRC weStubBlPersonenprognoseRC = new WEStubBlPersonenprognoseRC();
        int rc = starteService(weStubBlPersonenprognose.getWeLoginVerify(), "stubBlPersonenprognose", false, false);
        if (rc < 1) {
            weStubBlPersonenprognoseRC.setRc(rc);
            return weStubBlPersonenprognoseRC;
        }
        if (rc == 2) {
            weStubBlPersonenprognoseRC.setPruefeVersion(1);
        }

        BlPersonenprognose blPersonenprognose = new BlPersonenprognose(true, eclDbM.getDbBundle());
        switch (weStubBlPersonenprognose.stubFunktion) {
        case 1:
            zeigeStubText("holePersonenprognoseDaten");
            rc = blPersonenprognose.intialisierePrognose();

            weStubBlPersonenprognoseRC.listPp = blPersonenprognose.listPp;
            weStubBlPersonenprognoseRC.prediction = blPersonenprognose.prediction;
            weStubBlPersonenprognoseRC.presentPersons = blPersonenprognose.presentPersons;
            weStubBlPersonenprognoseRC.update = blPersonenprognose.update;
            weStubBlPersonenprognoseRC.listRegMeldung = blPersonenprognose.listRegMeldung;
            weStubBlPersonenprognoseRC.anzRegMeldung = blPersonenprognose.anzRegMeldung;
            weStubBlPersonenprognoseRC.shares = blPersonenprognose.shares;
            weStubBlPersonenprognoseRC.regShareholder = blPersonenprognose.regShareholder;
            break;

        case 2:
            zeigeStubText("Speichern");
            rc = blPersonenprognose.save(weStubBlPersonenprognose.list);
            break;

        case 3:
            zeigeStubText("Loeschen");
            rc = blPersonenprognose.deletePrognose(weStubBlPersonenprognose.data);
            break;

        case 4:
            zeigeStubText("Kalkuliere Aenderung");
            rc = blPersonenprognose.calculateChange(weStubBlPersonenprognose.list, weStubBlPersonenprognose.distances);

            weStubBlPersonenprognoseRC.distances = blPersonenprognose.distances;
            weStubBlPersonenprognoseRC.listPp = blPersonenprognose.listPp;
            weStubBlPersonenprognoseRC.prediction = blPersonenprognose.prediction;
            weStubBlPersonenprognoseRC.presentPersons = blPersonenprognose.presentPersons;
            weStubBlPersonenprognoseRC.update = blPersonenprognose.update;
            break;

        case 5:
            zeigeStubText("Neue Kalkulierung");
            rc = blPersonenprognose.calculation(weStubBlPersonenprognose.list, eclParamM.getEclEmittent().hvOrt,
                    eclParamM.getEclEmittent().veranstaltungPostleitzahl);

            weStubBlPersonenprognoseRC.listPp = blPersonenprognose.listPp;
            weStubBlPersonenprognoseRC.prediction = blPersonenprognose.prediction;
            weStubBlPersonenprognoseRC.update = blPersonenprognose.update;
            weStubBlPersonenprognoseRC.presentPersons = blPersonenprognose.presentPersons;
            weStubBlPersonenprognoseRC.listRegMeldung = blPersonenprognose.listRegMeldung;
            weStubBlPersonenprognoseRC.anzRegMeldung = blPersonenprognose.anzRegMeldung;
            weStubBlPersonenprognoseRC.shares = blPersonenprognose.shares;
            weStubBlPersonenprognoseRC.regShareholder = blPersonenprognose.regShareholder;
            weStubBlPersonenprognoseRC.listPraesentMeldung = blPersonenprognose.listPraesentMeldung;
            weStubBlPersonenprognoseRC.hvKoordinaten = blPersonenprognose.hvKoordinaten;
            weStubBlPersonenprognoseRC.distances = blPersonenprognose.distances;
            break;

        }

        weStubBlPersonenprognoseRC.rc = rc;
        beendeService();
        return weStubBlPersonenprognoseRC;
    }

    /*************************************
     * stubBlAufgaben
     ***********************************/
    @POST
    @Path("stubBlAufgaben")
    @Produces({ MediaType.APPLICATION_JSON, MediaType.TEXT_XML })
    public WEStubBlAufgabenRC stubBlAufgaben(WEStubBlAufgaben weStubBlAufgaben) {

        WEStubBlAufgabenRC weStubBlAufgabenRC = new WEStubBlAufgabenRC();
        int rc = starteService(weStubBlAufgaben.getWeLoginVerify(), "stubBlAufgaben", false, false);
        if (rc < 1) {
            weStubBlAufgabenRC.setRc(rc);
            return weStubBlAufgabenRC;
        }
        if (rc == 2) {
            weStubBlAufgabenRC.setPruefeVersion(1);
        }

        eclDbM.openWeitere();
        BlAufgaben blAufgaben = new BlAufgaben(true, eclDbM.getDbBundle());
        switch (weStubBlAufgaben.stubFunktion) {
        case 1:
            zeigeStubText("pw_vorbereitenPruefen");
            rc = blAufgaben.pw_vorbereitenPruefen();
            weStubBlAufgabenRC.rcAufgabenListe = blAufgaben.rcAufgabenListe;
            weStubBlAufgabenRC.rcAktienregisterListe = blAufgaben.rcAktienregisterListe;
            weStubBlAufgabenRC.rcPersonNatJurListe = blAufgaben.rcPersonNatJurListe;
            break;
        case 2:
            zeigeStubText("pw_aufbereitenDrucklauf");
            rc = blAufgaben.pw_aufbereitenDrucklauf(weStubBlAufgaben.pDrucklauf, weStubBlAufgaben.pArbeitsplatznr,
                    weStubBlAufgaben.pBenutzernr);
            weStubBlAufgabenRC.rcAufgabenListe = blAufgaben.rcAufgabenListe;
            weStubBlAufgabenRC.rcAktienregisterListe = blAufgaben.rcAktienregisterListe;
            weStubBlAufgabenRC.rcPersonNatJurListe = blAufgaben.rcPersonNatJurListe;
            weStubBlAufgabenRC.rcPasswortListe = blAufgaben.rcPasswortListe;
            weStubBlAufgabenRC.rcKennungListe = blAufgaben.rcKennungListe;

            weStubBlAufgabenRC.rcMandantListe = blAufgaben.rcMandantListe;
            weStubBlAufgabenRC.rcHvJahrListe = blAufgaben.rcHvJahrListe;
            weStubBlAufgabenRC.rcHvNummer = blAufgaben.rcHvNummer;
            weStubBlAufgabenRC.rcDatenbereich = blAufgaben.rcDatenbereich;
            weStubBlAufgabenRC.rcEmittent = blAufgaben.rcEmittent;

            weStubBlAufgabenRC.rcFehlercode = blAufgaben.rcFehlercode;
            break;
        case 3:
            zeigeStubText("pw_speicherePruefergebnis");
            blAufgaben.rcAufgabenListe = weStubBlAufgaben.rcAufgabenListe;
            rc = blAufgaben.pw_speicherePruefergebnis();
            break;

        }

        weStubBlAufgabenRC.rc = rc;
        beendeService();
        return weStubBlAufgabenRC;
    }

    
    /*************************************
     * stubMailing
     ***********************************/
    @POST
    @Path("stubMailing")
    @Produces({ MediaType.APPLICATION_JSON, MediaType.TEXT_XML })
    public WEStubMailingRC stubMailing(WEStubMailing weStubMailing) {

        WEStubMailingRC weStubMailingRC = new WEStubMailingRC();
        int rc = starteService(weStubMailing.getWeLoginVerify(), "stubMailing", false, false);
        if (rc < 1) {
            weStubMailingRC.setRc(rc);
            return weStubMailingRC;
        }
        if (rc == 2) {
            weStubMailingRC.setPruefeVersion(1);
        }

        eclDbM.openWeitere();
        StubMailing stubMailing = new StubMailing(true, eclDbM.getDbBundle());
        switch (weStubMailing.stubFunktion) {
        case 1:
            zeigeStubText("speichereMailingStatusListe");
            rc = stubMailing.speichereMailingStatusListe(weStubMailing.stati);
            break;

        case 2:
            zeigeStubText("holeEclMailingList");
            rc = stubMailing.holeEclMailingList();
            weStubMailingRC.rcEclMailingList=stubMailing.rcEclMailingList;
            break;

        case 3:
            zeigeStubText("speichereEclMailingList");
            rc = stubMailing.speichereEclMailingList(weStubMailing.eclMailingList);
            break;

        }

        weStubMailingRC.rc = rc;
        beendeService();
        return weStubMailingRC;
    }

    
    
    /*************************************
     * virtuelleHV
     ***********************************/
    @POST
    @Path("virtuelleHV")
    @Produces({ MediaType.APPLICATION_JSON, MediaType.TEXT_XML })
    public WEVirtuelleHVRC virtuelleHV(WEVirtuelleHV weVirtuelleHV) {

        WEVirtuelleHVRC weVirtuelleHVRC = new WEVirtuelleHVRC();
        int rc = starteService(weVirtuelleHV.getWeLoginVerify(), "virtuelleHV", true, true);
        if (rc < 1) {
            weVirtuelleHVRC.setRc(rc);
            return weVirtuelleHVRC;
        }
        if (rc == 2) {
            weVirtuelleHVRC.setPruefeVersion(1);
        }
        eclDbM.closeAll();

        EclVirtuellerTeilnehmer lVirtuellerTeilnehmer = null;

        switch (weVirtuelleHV.funktion) {
        case 1:
            zeigeStubText("fragenVorbereiten");
            aControllerFragen.initNeueFrage();
            weVirtuelleHVRC.maxZeichen = aControllerFragenSession.getMaxZeichen();
            weVirtuelleHVRC.maxFragenMitteilungen = aControllerFragenSession.getMaxFragen();
            weVirtuelleHVRC.virtuelleTeilnehmerListM = aControllerFragenSession.getVirtuelleTeilnehmerListM();
            weVirtuelleHVRC.fragenGestelltListe = aControllerFragenSession.getFragenGestelltListe();
            if (weVirtuelleHVRC.fragenGestelltListe == null) {
                CaBug.druckeLog("fragenGestelltListe=null", logDrucken, 5);
            }
            rc = 1;
            break;
        case 2:
            zeigeStubText("frageStellen");

            EclFragen lFrage = new EclFragen();
            lFrage.zuTop = weVirtuelleHV.frageZuTop;
            lFrage.fragentext = weVirtuelleHV.frageMitteilungText;

            eclDbM.openAll();
            eclDbM.openWeitere();

            lVirtuellerTeilnehmer = weVirtuelleHV.steller.copyTo();

            BlFragen blFragen = new BlFragen(true, eclDbM.getDbBundle());
            blFragen.neueFrageSpeichern(lFrage, eclTeilnehmerLoginM.getAnmeldeIdentAktienregister(),
                    lVirtuellerTeilnehmer);
            eclDbM.closeAll();

            aControllerFragen.initNeueFrage();
            weVirtuelleHVRC.maxZeichen = aControllerFragenSession.getMaxZeichen();
            weVirtuelleHVRC.maxFragenMitteilungen = aControllerFragenSession.getMaxFragen();
            weVirtuelleHVRC.virtuelleTeilnehmerListM = aControllerFragenSession.getVirtuelleTeilnehmerListM();
            weVirtuelleHVRC.fragenGestelltListe = aControllerFragenSession.getFragenGestelltListe();
            if (weVirtuelleHVRC.fragenGestelltListe == null) {
                CaBug.druckeLog("fragenGestelltListe=null", logDrucken, 10);
            }
            rc = 1;

            break;
        case 3:
            zeigeStubText("mitteilungenVorbereiten");
            aControllerMitteilungen.initNeueMitteilung();
            weVirtuelleHVRC.maxZeichen = aControllerMitteilungenSession.getMaxZeichen();
            weVirtuelleHVRC.maxFragenMitteilungen = aControllerMitteilungenSession.getMaxMitteilungen();
            weVirtuelleHVRC.virtuelleTeilnehmerListM = aControllerMitteilungenSession.getVirtuelleTeilnehmerListM();
            weVirtuelleHVRC.mitteilungenGestelltListe = aControllerMitteilungenSession.getMitteilungenGestelltListe();
            rc = 1;
            break;
        case 4:
            zeigeStubText("mitteilungStellen");

            EclMitteilungenAlt lMitteilung = new EclMitteilungenAlt();
            lMitteilung.mitteilungtext = weVirtuelleHV.frageMitteilungText;

            eclDbM.openAll();
            eclDbM.openWeitere();

            lVirtuellerTeilnehmer = weVirtuelleHV.steller.copyTo();

            BlMitteilungenAlt blMitteilungen = new BlMitteilungenAlt(true, eclDbM.getDbBundle());
            blMitteilungen.neueMitteilungSpeichern(lMitteilung, eclTeilnehmerLoginM.getAnmeldeIdentAktienregister(),
                    lVirtuellerTeilnehmer);
            eclDbM.closeAll();

            aControllerMitteilungen.initNeueMitteilung();
            weVirtuelleHVRC.maxZeichen = aControllerMitteilungenSession.getMaxZeichen();
            weVirtuelleHVRC.maxFragenMitteilungen = aControllerMitteilungenSession.getMaxMitteilungen();
            weVirtuelleHVRC.virtuelleTeilnehmerListM = aControllerMitteilungenSession.getVirtuelleTeilnehmerListM();
            weVirtuelleHVRC.mitteilungenGestelltListe = aControllerMitteilungenSession.getMitteilungenGestelltListe();
            rc = 1;

            break;
        case 5:
            zeigeStubText("videoStream");
            break;
        case 6:
            zeigeStubText("teilnehmerverzeichnis");
            aControllerTeilnehmerverz.init();
            weVirtuelleHVRC.absTeilButtonList = aControllerTeilnehmerverzSession.getAbsTeilButtonList();
            rc = 1;
            break;
        case 7:
            zeigeStubText("abstimmungsergebnis");
            aControllerAbstimmungserg.init();
            weVirtuelleHVRC.absTeilButtonList = aControllerAbstimmungsergSession.getAbsTeilButtonList();
            rc = 1;
            break;
        case 8:
            zeigeStubText("pdf-Anzeigen " + KonstPdfArt.getText(weVirtuelleHV.pdfArt));
            String pfad = "";
            String dateiname = "";
            switch (weVirtuelleHV.pdfArt) {
            case KonstPdfArt.teilnehmerverzeichnis:
                if (eclParamM.getTeilnehmerverzAngeboten() == 1) {
                    pfad = eclParamM.getClGlobalVar().lwPfadAllgemein + "\\"+eclParamM.getParamServer().praefixPfadVerzeichnisse
                            + "ausdruckeintern\\"
                            + eclParamM.getMandantPfad() + "\\" + "Praesenz"
                            + Integer.toString(weVirtuelleHV.pdfLfdNummer) + ".pdf";
                    dateiname = "Praesenz" + Integer.toString(weVirtuelleHV.pdfLfdNummer) + ".pdf";
                }
                break;
            case KonstPdfArt.teilnehmerverzeichnisZusammenstellung:
                if (eclParamM.getLfdHVTeilnehmerverzIstMoeglich() == 1) {
                    pfad = eclParamM.getClGlobalVar().lwPfadAllgemein + "\\"+eclParamM.getParamServer().praefixPfadVerzeichnisse
                            + "ausdruckeintern\\"
                            + eclParamM.getMandantPfad() + "\\" + "Praesenz"
                            + Integer.toString(weVirtuelleHV.pdfLfdNummer) + "zusammen.pdf";
                    dateiname = "Praesenz" + Integer.toString(weVirtuelleHV.pdfLfdNummer) + "zusammen.pdf";
                }
                break;
            case KonstPdfArt.abstimmungsergebnis:
                if (eclParamM.getLfdHVAbstimmungsergIstMoeglich() == 1) {
                    pfad = eclParamM.getClGlobalVar().lwPfadAllgemein + "\\"+eclParamM.getParamServer().praefixPfadVerzeichnisse
                            + "ausdruckeintern\\"
                            + eclParamM.getMandantPfad() + "\\" + "Abstimmungserg"
                            + Integer.toString(weVirtuelleHV.pdfLfdNummer) + ".pdf";
                    dateiname = "Abstimmungserg" + Integer.toString(weVirtuelleHV.pdfLfdNummer) + ".pdf";
                }
                break;
            case KonstPdfArt.sonstigesDokumentAufHV:
                int gef = 0;
                switch (weVirtuelleHV.pdfLfdNummer) {
                case 1:
                    if (eclParamM.getParam().paramPortal.unterlage1Aktiv == 1) {
                        gef = 1;
                    }
                    break;
                case 2:
                    if (eclParamM.getParam().paramPortal.unterlage2Aktiv == 1) {
                        gef = 1;
                    }
                    break;
                case 3:
                    if (eclParamM.getParam().paramPortal.unterlage3Aktiv == 1) {
                        gef = 1;
                    }
                    break;
                case 4:
                    if (eclParamM.getParam().paramPortal.unterlage4Aktiv == 1) {
                        gef = 1;
                    }
                    break;
                case 5:
                    if (eclParamM.getParam().paramPortal.unterlage5Aktiv == 1) {
                        gef = 1;
                    }
                    break;
                case 6:
                    if (eclParamM.getParam().paramPortal.unterlage6Aktiv == 1) {
                        gef = 1;
                    }
                    break;
                case 7:
                    if (eclParamM.getParam().paramPortal.unterlage7Aktiv == 1) {
                        gef = 1;
                    }
                    break;
                case 8:
                    if (eclParamM.getParam().paramPortal.unterlage8Aktiv == 1) {
                        gef = 1;
                    }
                    break;
                case 9:
                    if (eclParamM.getParam().paramPortal.unterlage9Aktiv == 1) {
                        gef = 1;
                    }
                    break;
                case 10:
                    if (eclParamM.getParam().paramPortal.unterlage10Aktiv == 1) {
                        gef = 1;
                    }
                    break;
                }
                if (gef == 1) {
                    pfad = eclParamM.getClGlobalVar().lwPfadAllgemein + "\\"+eclParamM.getParamServer().praefixPfadVerzeichnisse
                            + "reports\\" + eclParamM.getMandantPfad()
                            + "\\" + "u" + Integer.toString(weVirtuelleHV.pdfLfdNummer) + "de.pdf";
                    dateiname = "u" + Integer.toString(weVirtuelleHV.pdfLfdNummer) + "de.pdf";
                }
                break;
            }
            if (!pfad.isEmpty()) {
                eclDbM.openAll();
                eclDbM.openWeitere();
                BlPdfInBrowser blPdfInBrowser = new BlPdfInBrowser(eclDbM.getDbBundle());
                EclPdfInBrowser lPdfInBrowser = blPdfInBrowser.erzeugeEinmalKey(pfad);
                BlEinsprungLinkPortal lBlEinsprungLinkPortal = new BlEinsprungLinkPortal(eclDbM.getDbBundle());
                String kompletterLink = lBlEinsprungLinkPortal.linkFuerPDFAnzeigen(lPdfInBrowser.eindeutigerKey,
                        lPdfInBrowser.ident);
                eclDbM.closeAll();

                /** TODO App Anzeige von PDFs Notlösung für ku164 - App */
                kompletterLink = "https://portal.better-orange.de/k_S-2020_$_l!ste/" + dateiname;
                /* Notlösung Ende */

                weVirtuelleHVRC.linkFuerPdfShow = kompletterLink;

                System.out.println("kompletterLink=" + kompletterLink);
            }
            rc = 1;
            break;

        }

        weVirtuelleHVRC.rc = rc;
        beendeServiceOhneClose();
        return weVirtuelleHVRC;
    }

    // /*************************************monitor***********************************/
    // @POST
    // @Path("monitor")
    // @Produces( {MediaType.APPLICATION_JSON, MediaType.TEXT_XML} )
    // public WEMonitorRC monitor(WEMonitor weMonitor){
    //
    //
    // WEMonitorRC weMonitorRC=null;
    //
    // System.out.println("WAIntern.monitor");
    //
    // aDlgVariablen.setEingabeQuelle(51);
    // aFunktionen.setStartPruefen(0);
    //
    // WELoginVerify lWELoginVerify=weMonitor.getWeLoginVerify();
    //
    // setzeMandantVerify(lWELoginVerify);
    // eclDbM.openAll();
    //
    // if (this.pruefeLoginVerifyUndSetzeMandant(lWELoginVerify)==false){
    // weMonitorRC=new WEMonitorRC();
    // weMonitorRC.setRc(CaFehler.afFalscheKennung);
    // eclDbM.closeAllAbbruch();
    // return weMonitorRC;
    // }
    //
    // weMonitorRC=new WEMonitorRC();
    // int
    // rc=eclDbM.getDbBundle().dbBasis.getInterneIdentWillenserklaerungForUpdateReturn();
    // if (rc==-1){
    // weMonitorRC.rc=-1;
    // System.out.println("Überwachung nicht ok
    // "+CaDatumZeit.DatumZeitStringFuerDatenbank());
    // baMailM.senden("Portal.Alert@better-orange.de",
    // "Aufwachen 1, Fehler!",
    // "Aufwachen"
    // );
    // baMailM.senden("Portal.Alert@better-orange.de",
    // "Aufwachen 2, Fehler!",
    // "Aufwachen"
    // );
    // baMailM.senden("Portal.Alert@better-orange.de",
    // "Aufwachen 3, Fehler!",
    // "Aufwachen"
    // );
    // }
    // else{
    // weMonitorRC.rc=1;
    //
    // }
    //
    //
    //
    // eclDbM.closeAll();
    // return weMonitorRC;
    // }

    @POST
    @Path("basemen")
    @Produces({ MediaType.APPLICATION_JSON, MediaType.TEXT_XML })
    public WEBasemenRC basemen(WEBasemen weBasemen) {
        String lKennung = weBasemen.uKennung;
        String lPasswort = weBasemen.uPasswort;

        if (lKennung.compareTo(basemenUser) != 0) {
            System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!Basemen Hack 01!!!!!!!!!!!!!!!!!!");
            return null;
        }
        if (lPasswort.compareTo(basemenPasswort) != 0) {
            System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!Basemen Hack 02!!!!!!!!!!!!!!!!!!");
            return null;
        }

        System.out.println("Kennung=" + weBasemen.uKennung);
        System.out.println("Funktion=" + weBasemen.funktionscode);

        WEBasemenRC weBasemenRC = new WEBasemenRC();

        weBasemenRC.rc = 1;

        return weBasemenRC;
    }

    @POST
    @Path("stubBlAktienregister")
    @Produces({ MediaType.APPLICATION_JSON, MediaType.TEXT_XML })
    public WEStubBlAktienregisterRC stubBlAktienregister(WEStubBlAktienregister weStubBlAktienregister) {

        WEStubBlAktienregisterRC weStubBlAktienregisterRC = new WEStubBlAktienregisterRC();
        int rc = starteService(weStubBlAktienregister.getWeLoginVerify(), "stubBlAktienregister", false, false);
        if (rc < 1) {
            weStubBlAktienregisterRC.setRc(rc);
            return weStubBlAktienregisterRC;
        }
        if (rc == 2) {
            weStubBlAktienregisterRC.setPruefeVersion(1);
        }

        BlAktienregister blAktienregister = new BlAktienregister(true, eclDbM.getDbBundle());
        blAktienregister.setParameter(weStubBlAktienregister);
        switch (weStubBlAktienregister.stubFunktion) {
        case 1:
            zeigeStubText("Read Aktienregister M" + eclParamM.getClGlobalVar().mandant);
            rc = blAktienregister.listenLaden();

            weStubBlAktienregisterRC.staaten = blAktienregister.staaten;
            weStubBlAktienregisterRC.registerListe = blAktienregister.registerListe;
            weStubBlAktienregisterRC.bestand = blAktienregister.bestand;
            break;
        case 2:
            zeigeStubText("Insert Aktienregister M" + eclParamM.getClGlobalVar().mandant);
            rc = blAktienregister.insertRegister();
            break;
        case 4:
            zeigeStubText("Update Aktienregister M" + eclParamM.getClGlobalVar().mandant);
            rc = blAktienregister.updateRegister();
            
            weStubBlAktienregisterRC.count = blAktienregister.count;
            break;
        case 5:
            zeigeStubText("Lese Bestand Aktienregister M" + eclParamM.getClGlobalVar().mandant);
            rc = blAktienregister.readAktienregisterBestand();

            weStubBlAktienregisterRC.bestand = blAktienregister.bestand;
            break;
        case 6:
            zeigeStubText("Lese Bestand Meldungen M" + eclParamM.getClGlobalVar().mandant);
            rc = blAktienregister.readMeldeBestand();

            weStubBlAktienregisterRC.meldebestand = blAktienregister.meldebestand;
            break;
        case 7:
            zeigeStubText("Insert Aktienregister - M" + eclParamM.getClGlobalVar().mandant);
            rc = blAktienregister.insertDatensatz(weStubBlAktienregister.eintrag, weStubBlAktienregister.inhaber);

            weStubBlAktienregisterRC.eintrag = blAktienregister.eintrag;
            break;
        case 8:
            zeigeStubText("Update Aktienregister - ANR:" + weStubBlAktienregister.eintrag.aktionaersnummer + " - M" + eclParamM.getClGlobalVar().mandant);
            rc = blAktienregister.updateDatensatz(weStubBlAktienregister.eintrag);
            break;
        case 9:
            zeigeStubText("Read Aktienregister M" + eclParamM.getClGlobalVar().mandant);
            rc = blAktienregister.searchRegister(weStubBlAktienregister.suche, weStubBlAktienregister.sort, weStubBlAktienregister.offset, weStubBlAktienregister.limit);

            weStubBlAktienregisterRC.registerListe = blAktienregister.registerListe;
            weStubBlAktienregisterRC.count = blAktienregister.count;
            break;
        case 10:
            zeigeStubText("Read ISINs M" + eclParamM.getClGlobalVar().mandant);
            rc = blAktienregister.readAktienregisterIsin();

            weStubBlAktienregisterRC.isinList = blAktienregister.isinList;
            break;
        case 11:
            zeigeStubText("Sperre Aktionaer: " + weStubBlAktienregister.eintrag.aktionaersnummer + " - M" + eclParamM.getClGlobalVar().mandant);
            rc = blAktienregister.sperreAktionaer(weStubBlAktienregister.eintrag);
            break;
        case 12:
            zeigeStubText("Reset PW: " + weStubBlAktienregister.filter + " - M" + eclParamM.getClGlobalVar().mandant);
            rc = blAktienregister.resetPasswort(weStubBlAktienregister.filter);
            
            weStubBlAktienregisterRC.count = blAktienregister.count;
            break;
        }
        weStubBlAktienregisterRC.rc = rc;
        beendeService();
        return weStubBlAktienregisterRC;
    }

    @POST
    @Path("stubBlTeilnehmerLoginSperre")
    @Produces({ MediaType.APPLICATION_JSON, MediaType.TEXT_XML })
    public WEStubBlTeilnehmerLoginSperreRC stubBlTeilnehmerLoginSperre(WEStubBlTeilnehmerLoginSperre weStubBlTeilnehmerLoginSperre) {

        WEStubBlTeilnehmerLoginSperreRC weStubBlTeilnehmerLoginSperreRC = new WEStubBlTeilnehmerLoginSperreRC();
        int rc = starteService(weStubBlTeilnehmerLoginSperre.getWeLoginVerify(), "stubBlTeilnehmerLoginSperre", false, false);
        if (rc < 1) {
            weStubBlTeilnehmerLoginSperreRC.setRc(rc);
            return weStubBlTeilnehmerLoginSperreRC;
        }
        if (rc == 2) {
            weStubBlTeilnehmerLoginSperreRC.setPruefeVersion(1);
        }

        BlTeilnehmerLoginSperre blTeilnehmerLoginSperre = new BlTeilnehmerLoginSperre(true, eclDbM.getDbBundle());
        switch (weStubBlTeilnehmerLoginSperre.stubFunktion) {
        case 1:
            zeigeStubText("alle0BestaendeSperren");
            rc = blTeilnehmerLoginSperre.alle0BestaendeSperren();
            break;
        case 2:
            zeigeStubText("freigebenKennung");
            rc = blTeilnehmerLoginSperre.freigebenKennung(weStubBlTeilnehmerLoginSperre.pKennung);
            break;
        case 3:
            zeigeStubText("sperrenKennung");
            rc = blTeilnehmerLoginSperre.sperrenKennung(weStubBlTeilnehmerLoginSperre.pKennung);
            break;
        }
        weStubBlTeilnehmerLoginSperreRC.rc = rc;
        beendeService();
        return weStubBlTeilnehmerLoginSperreRC;
    }

    @POST
    @Path("stubBlMeldungen")
    @Produces({ MediaType.APPLICATION_JSON, MediaType.TEXT_XML })
    public WEStubBlMeldungenRC stubBlMeldungen(WEStubBlMeldungen weStubBlMeldungen) {

        WEStubBlMeldungenRC weStubBlMeldungenRC = new WEStubBlMeldungenRC();
        int rc = starteService(weStubBlMeldungen.getWeLoginVerify(), "stubBlMeldungen", false, false);
        if (rc < 1) {
            weStubBlMeldungenRC.setRc(rc);
            return weStubBlMeldungenRC;
        }
        if (rc == 2) {
            weStubBlMeldungenRC.setPruefeVersion(1);
        }

        BlMeldungen blMeldungen = new BlMeldungen(true, eclDbM.getDbBundle());
        blMeldungen.setParameter(weStubBlMeldungen);
        switch (weStubBlMeldungen.stubFunktion) {
        case 1:
            zeigeStubText("Update Meldung - Stimmausschluss");
            rc = blMeldungen.updateMeldung(weStubBlMeldungen.mitPersonNatJur);

            break;
        case 2:
            zeigeStubText("Lese Praesenz fuer Uebersicht");
            rc = blMeldungen.readPraesenz();
            
            weStubBlMeldungenRC.setParameter(blMeldungen);
            break;
        }
        weStubBlMeldungenRC.rc = rc;
        beendeService();
        return weStubBlMeldungenRC;
    }

    @POST
    @Path("stubBlInhaberImport")
    @Produces({ MediaType.APPLICATION_JSON, MediaType.TEXT_XML })
    public WEStubBlInhaberImportRC stubBlInhaberImport(WEStubBlInhaberImport weStubBlInhaberImport) {

        WEStubBlInhaberImportRC weStubBlInhaberImportRC = new WEStubBlInhaberImportRC();
        int rc = starteService(weStubBlInhaberImport.getWeLoginVerify(), "stubBlInhaberImport", false, false);
        if (rc < 1) {
            weStubBlInhaberImportRC.setRc(rc);
            return weStubBlInhaberImportRC;
        }
        if (rc == 2)
            weStubBlInhaberImportRC.setPruefeVersion(1);

        BlInhaberImport blInhaberImport = new BlInhaberImport(true, eclDbM.getDbBundle());
        blInhaberImport.setParameter(weStubBlInhaberImport);
        switch (weStubBlInhaberImport.stubFunktion) {
        case 1:
            zeigeStubText("Lese Bestand InhaberImport M" + eclParamM.getClGlobalVar().mandant);
            rc = blInhaberImport.loadLists();

            weStubBlInhaberImportRC.setParameter(blInhaberImport);
            break;
        case 2:
            zeigeStubText("Bearbeite Datei");
            rc = blInhaberImport.prepareFile();

            weStubBlInhaberImportRC.setParameter(blInhaberImport);
            break;
        case 3:
            zeigeStubText("Importiere InhaberImport Datei");
            rc = blInhaberImport.doImport();

            weStubBlInhaberImportRC.setParameter(blInhaberImport);
            break;
        case 4:
            zeigeStubText("Lese Import Protokoll");
            rc = blInhaberImport.readProtokoll();
            
            weStubBlInhaberImportRC.setParameter(blInhaberImport);
            break;
        case 5:
            zeigeStubText("Insert Import Protokoll");
            rc = blInhaberImport.insertProtokoll();
            
            weStubBlInhaberImportRC.setParameter(blInhaberImport);
            break;
        }
        weStubBlInhaberImportRC.rc = rc;
        beendeService();
        return weStubBlInhaberImportRC;
    }

    @POST
    @Path("stubBlWillenserklaerungBatch")
    @Produces({ MediaType.APPLICATION_JSON, MediaType.TEXT_XML })
    public WEStubBlWillenserklaerungBatchRC stubBlWillenserklaerungBatch(WEStubBlWillenserklaerungBatch weStubBlWillenserklaerungBatch) {

        WEStubBlWillenserklaerungBatchRC weStubBlWillenserklaerungBatchRC = new WEStubBlWillenserklaerungBatchRC();
        int rc = starteService(weStubBlWillenserklaerungBatch.getWeLoginVerify(), "stubBlWillenserklaerungBatch", false, false);
        if (rc < 1) {
            weStubBlWillenserklaerungBatchRC.setRc(rc);
            return weStubBlWillenserklaerungBatchRC;
        }
        if (rc == 2)
            weStubBlWillenserklaerungBatchRC.setPruefeVersion(1);

        BlWillenserklaerungBatch blWillenserklaerungBatch = new BlWillenserklaerungBatch(true, eclDbM.getDbBundle());
        switch (weStubBlWillenserklaerungBatch.stubFunktion) {
        case 1:
            zeigeStubText("erzeugeEKFuerAlleAngemeldeten");
            rc = blWillenserklaerungBatch.erzeugeEKFuerAlleAngemeldeten();
            weStubBlWillenserklaerungBatchRC.rcAnzahlAngemeldeteOhneEK=blWillenserklaerungBatch.rcAnzahlAngemeldeteOhneEK;
            break;
        case 2:
            zeigeStubText("liefereAnzahlAngemeldeteOhneEK");
            rc = blWillenserklaerungBatch.liefereAnzahlAngemeldeteOhneEK();
            weStubBlWillenserklaerungBatchRC.rcAnzahlAngemeldeteOhneEK=blWillenserklaerungBatch.rcAnzahlAngemeldeteOhneEK;
            break;
        }
        weStubBlWillenserklaerungBatchRC.rc = rc;
        beendeService();
        return weStubBlWillenserklaerungBatchRC;
    }

    @POST
    @Path("stubBlstimmkartendruck")
    @Produces({ MediaType.APPLICATION_JSON, MediaType.TEXT_XML })
    public WEStubBlStimmkartendruckRC stubBlstimmkartendruck(WEStubBlStimmkartendruck weStubBlStimmkartendruck) {

        WEStubBlStimmkartendruckRC weStubBlStimmkartendruckRC = new WEStubBlStimmkartendruckRC();
        int rc = starteService(weStubBlStimmkartendruck.getWeLoginVerify(), "stubBlstimmkartendruck", false, false);
        if (rc < 1) {
            weStubBlStimmkartendruckRC.setRc(rc);
            return weStubBlStimmkartendruckRC;
        }
        if (rc == 2)
            weStubBlStimmkartendruckRC.setPruefeVersion(1);

        BlStimmkartendruck blStimmkartendruck = new BlStimmkartendruck(true, eclDbM.getDbBundle());
        switch (weStubBlStimmkartendruck.stubFunktion) {
        case 1:
            zeigeStubText("holeMeldedatenFuerTausch1Zu1");
            rc = blStimmkartendruck.holeMeldedatenFuerTausch1Zu1(weStubBlStimmkartendruck.pGattung,
                    weStubBlStimmkartendruck.pDrucklauf, weStubBlStimmkartendruck.pSelektion,
                    weStubBlStimmkartendruck.pVonNr, weStubBlStimmkartendruck.pBisNr, weStubBlStimmkartendruck.pDrucklaufNr, weStubBlStimmkartendruck.pSortierung);
            weStubBlStimmkartendruckRC.rcMeldungsliste=blStimmkartendruck.rcMeldungsliste;
            break;
        case 2:
            zeigeStubText("holeAbstimmungsblockFuerStimmkartendruck");
            rc = blStimmkartendruck.holeAbstimmungsblockFuerStimmkartendruck();
            weStubBlStimmkartendruckRC.aktiverAbstimmungsblock=blStimmkartendruck.aktiverAbstimmungsblock;
            weStubBlStimmkartendruckRC.abstimmungenZuAktivenBlock=blStimmkartendruck.abstimmungenZuAktivenBlock;
            weStubBlStimmkartendruckRC.abstimmungen=blStimmkartendruck.abstimmungen;
            weStubBlStimmkartendruckRC.stimmkartenUeberschriftenStehenZurVerfuegung=blStimmkartendruck.stimmkartenUeberschriftenStehenZurVerfuegung;
            break;
         }
        weStubBlStimmkartendruckRC.rc = rc;
        beendeService();
        return weStubBlStimmkartendruckRC;
    }

    @POST
    @Path("hybridMitglieder")
    @Produces({ MediaType.APPLICATION_JSON, MediaType.TEXT_XML })
    public WEHybridMitgliederRC hybridMitglieder(WEHybridMitglieder weHybridMitglieder) {

        WEHybridMitgliederRC weHybridMitgliederRC = new WEHybridMitgliederRC();
        int rc = starteService(weHybridMitglieder.getWeLoginVerify(), "hybridMitglieder", false, false);
        if (rc < 1) {
            weHybridMitgliederRC.setRc(rc);
            return weHybridMitgliederRC;
        }
        if (rc == 2)
            weHybridMitgliederRC.setPruefeVersion(1);

        BlHybridMitglieder blHybridMitglieder= new BlHybridMitglieder(true, eclDbM.getDbBundle());
        blHybridMitglieder.weHybridMitgliederRC=weHybridMitgliederRC;
        blHybridMitglieder.serverAusfuehrenFunktion(weHybridMitglieder);
        weHybridMitgliederRC = blHybridMitglieder.weHybridMitgliederRC;
        beendeService();
        return weHybridMitgliederRC;
    }

    @POST
    @Path("stubBlHybridMitglieder")
    @Produces({ MediaType.APPLICATION_JSON, MediaType.TEXT_XML })
    public WEStubBlHybridMitgliederRC stubBlHybridMitglieder(WEStubBlHybridMitglieder weStubBlHybridMitglieder) {

        WEStubBlHybridMitgliederRC weStubBlHybridMitgliederRC = new WEStubBlHybridMitgliederRC();
        int rc = starteService(weStubBlHybridMitglieder.getWeLoginVerify(), "stubBlHybridMitglieder", false, false);
        if (rc < 1) {
            weStubBlHybridMitgliederRC.setRc(rc);
            return weStubBlHybridMitgliederRC;
        }
        if (rc == 2)
            weStubBlHybridMitgliederRC.setPruefeVersion(1);

        BlHybridMitglieder blHybridMitglieder = new BlHybridMitglieder(true, eclDbM.getDbBundle());
        switch (weStubBlHybridMitglieder.stubFunktion) {
        case 1:
            zeigeStubText("updatePraesenteVonServer");
            blHybridMitglieder.rcPraesenteMeldungenAufServer=weStubBlHybridMitglieder.rcPraesenteMeldungenAufServer;
            rc = blHybridMitglieder.updatePraesenteVonServer();
            break;
        case 2:
            zeigeStubText("holePraesenteVonHVServer");
            rc = blHybridMitglieder.holePraesenteVonHVServer();
            weStubBlHybridMitgliederRC.rcPraesenteMeldungenAufHVServer=blHybridMitglieder.rcPraesenteMeldungenAufHVServer;
            break;
     }
        weStubBlHybridMitgliederRC.rc = rc;
        beendeService();
        return weStubBlHybridMitgliederRC;
    }

    @POST
    @Path("stubParamStrukt")
    @Produces({ MediaType.APPLICATION_JSON, MediaType.TEXT_XML })
    public WEStubParamStruktRC stubParamStrukt(WEStubParamStrukt weStubParamStrukt) {

        WEStubParamStruktRC weStubParamStruktRC = new WEStubParamStruktRC();
        int rc = starteService(weStubParamStrukt.getWeLoginVerify(), "stubParamStrukt", false, false);
        if (rc < 1) {
            weStubParamStruktRC.setRc(rc);
            return weStubParamStruktRC;
        }
        if (rc == 2)
            weStubParamStruktRC.setPruefeVersion(1);

        BlParamStrukt blParamStrukt = new BlParamStrukt(true, eclDbM.getDbBundle());
        switch (weStubParamStrukt.stubFunktion) {
        case 1:
            zeigeStubText("paramStruktGruppeListe");
            rc = blParamStrukt.leseParameterGruppeZumBearbeiten(weStubParamStrukt.paramStruktGruppe);
            weStubParamStruktRC.paramStruktGruppeHeader=blParamStrukt.paramStruktGruppeHeader;
            weStubParamStruktRC.emittent=blParamStrukt.emittent;
            
            break;
        case 2:
            zeigeStubText("leseVersammlungstypen");
            rc = blParamStrukt.leseVersammlungstypen(weStubParamStrukt.presetArt);
            weStubParamStruktRC.paramStruktVersammlungstypListe=blParamStrukt.paramStruktVersammlungstypListe;
            break;
        case 3:
            zeigeStubText("speichereParameterGruppe");
            rc = blParamStrukt.speichereParameterGruppe(weStubParamStrukt.paramStruktGruppeHeader, weStubParamStrukt.emittent);
            break;
        case 4:
            zeigeStubText("leseParameterFuerWechselVersammlungstyp");
            rc = blParamStrukt.leseParameterFuerWechselVersammlungstyp(weStubParamStrukt.presetArt, weStubParamStrukt.veranstaltungstypNeu);
            weStubParamStruktRC.paramStruktListe=blParamStrukt.paramStruktListe;
            weStubParamStruktRC.emittent=blParamStrukt.emittent;
            break;
        case 5:
            zeigeStubText("speichereParameterFuerWechselVersammlungstyp");
            rc = blParamStrukt.speichereParameterFuerWechselVersammlungstyp(weStubParamStrukt.presetArt, weStubParamStrukt.veranstaltungstypNeu, weStubParamStrukt.paramStruktListe, weStubParamStrukt.emittent);
            break;
        case 6:
            zeigeStubText("leseGruppenheaderFuerGruppenAuswahl");
            rc = blParamStrukt.leseGruppenheaderFuerGruppenAuswahl();
            weStubParamStruktRC.paramStruktGruppenHeaderListe=blParamStrukt.paramStruktGruppenHeaderListe;
            break;
        case 7:
            zeigeStubText("speichereNeuenVersammlungstyp");
            rc = blParamStrukt.speichereNeuenVersammlungstyp(weStubParamStrukt.presetArt, weStubParamStrukt.beschreibungKurz, weStubParamStrukt.beschreibungLang, weStubParamStrukt.vererbenVon);
            break;
        case 8:
            zeigeStubText("lesePresetArten");
            rc = blParamStrukt.lesePresetArten();
            weStubParamStruktRC.paramStruktPresetArtListe=blParamStrukt.paramStruktPresetArtListe;
            break;
        }
        weStubParamStruktRC.rc = rc;
        beendeService();
        return weStubParamStruktRC;
    }

    @POST
    @Path("serverVerbund")
    @Produces({ MediaType.APPLICATION_JSON, MediaType.TEXT_XML })
    public WEServerVerbundRC hybridMitglieder(WEServerVerbund weServerVerbund) {

        WEServerVerbundRC weServerVerbundRC = new WEServerVerbundRC();
        int rc = starteService(weServerVerbund.getWeLoginVerify(), "serverVerbund", false, false);
        if (rc < 1) {
            weServerVerbundRC.setRc(rc);
            return weServerVerbundRC;
        }
        if (rc == 2)
            weServerVerbundRC.setPruefeVersion(1);

        switch (weServerVerbund.funktion) {
        case 1:/*Test-Funktion*/
            rc=1;
            weServerVerbundRC.rcFachlicherRC=1;
            break;
        case 2:/*Komme in Testraum*/
            rc=1;
            weServerVerbundRC.rcFachlicherRC=2;
            blMTeilnehmerKommunikation.fuehreAusWortmeldungAktionKommeInTestraum(weServerVerbund.loginKennung, weServerVerbund.mandant);
            break;
        case 3:/*Verlasse Testraum*/
            rc=1;
            weServerVerbundRC.rcFachlicherRC=2;
            blMTeilnehmerKommunikation.fuehreAusWortmeldungAktionVerlasseTestraum(weServerVerbund.loginKennung, weServerVerbund.mandant);
            break;
        case 4:/*Komme in Rederaum*/
            rc=1;
            weServerVerbundRC.rcFachlicherRC=2;
            blMTeilnehmerKommunikation.fuehreAusWortmeldungAktionKommeInRederaum(weServerVerbund.loginKennung, weServerVerbund.mandant);
            break;
        case 5:/*Verlasse Rederaum*/
            rc=1;
            weServerVerbundRC.rcFachlicherRC=2;
            blMTeilnehmerKommunikation.fuehreAusWortmeldungAktionVerlasseRederaum(weServerVerbund.loginKennung, weServerVerbund.mandant);
            break;
        case 6:/*Jetzt sprechen*/
            rc=1;
            weServerVerbundRC.rcFachlicherRC=2;
            blMTeilnehmerKommunikation.fuehreAusWortmeldungAktionJetztSprechen(weServerVerbund.loginKennung, weServerVerbund.mandant);
            break;
        case 7:/*Verlasse Testraum Nicht OK*/
            rc=1;
            weServerVerbundRC.rcFachlicherRC=2;
            blMTeilnehmerKommunikation.fuehreAusWortmeldungAktionVerlasseTestraumNichtOK(weServerVerbund.loginKennung, weServerVerbund.mandant);
            break;
        case 8:/*Aktualisiere VL*/
            rc=1;
            weServerVerbundRC.rcFachlicherRC=2;
            blMTeilnehmerKommunikation.fuehreAuswortmeldungAktionAktualisiereVL(weServerVerbund.loginKennung, weServerVerbund.mandant);
            break;
        case 9:/*Test nicht erreicht*/
            rc=1;
            weServerVerbundRC.rcFachlicherRC=2;
            blMTeilnehmerKommunikation.fuehreAuswortmeldungAktionTestNichtErreicht(weServerVerbund.loginKennung, weServerVerbund.mandant);
            break;
        case 10:/*Rede nicht erreicht*/
            rc=1;
            weServerVerbundRC.rcFachlicherRC=2;
            blMTeilnehmerKommunikation.fuehreAuswortmeldungAktionRedeNichtErreicht(weServerVerbund.loginKennung, weServerVerbund.mandant);
            break;
        }

//        BlHybridMitglieder blHybridMitglieder = new BlHybridMitglieder(true, eclDbM.getDbBundle());
//        switch (weStubBlHybridMitglieder.stubFunktion) {
//        case 1:
//            zeigeStubText("updatePraesenteVonServer");
//            blHybridMitglieder.rcPraesenteMeldungenAufServer=weStubBlHybridMitglieder.rcPraesenteMeldungenAufServer;
//            rc = blHybridMitglieder.updatePraesenteVonServer();
//            break;
//        case 2:
//            zeigeStubText("holePraesenteVonHVServer");
//            rc = blHybridMitglieder.holePraesenteVonHVServer();
//            weStubBlHybridMitgliederRC.rcPraesenteMeldungenAufHVServer=blHybridMitglieder.rcPraesenteMeldungenAufHVServer;
//            break;
//     }
        weServerVerbundRC.rc = rc;
        beendeService();
        return weServerVerbundRC;
    }


    
    /*************************************
     * abstimmungku310AktionaerLesen
     ***********************************/
    @POST
    @Path("abstimmungku310AktionaerLesen")
    @Produces({ MediaType.APPLICATION_JSON, MediaType.TEXT_XML })
    public WEAbstimmungku310AktionaerLesenRC abstimmungku310AktionaerLesen(
            WEAbstimmungku310AktionaerLesen weAbstimmungku310AktionaerLesen) {

        WEAbstimmungku310AktionaerLesenRC weAbstimmungku310AktionaerLesenRC = new WEAbstimmungku310AktionaerLesenRC();
        /* TODO Achtung App - es erfolgt keine Überprüfung des Teilnehmers */
        int rc = starteService(weAbstimmungku310AktionaerLesen.getWeLoginVerify(), "abstimmungku310AktionaerLesen", false, true);
        if (rc < 1) {
            weAbstimmungku310AktionaerLesenRC.setRc(rc);
            return weAbstimmungku310AktionaerLesenRC;
        }
        if (rc == 2) {
            weAbstimmungku310AktionaerLesenRC.setPruefeVersion(1);
        }

        eclDbM.openWeitere();

        tStimmabgabeku310.abstimmungku310AktionaerLesen(weAbstimmungku310AktionaerLesen, weAbstimmungku310AktionaerLesenRC);

        beendeService();
        return weAbstimmungku310AktionaerLesenRC;
    }

    /*************************************
     * abstimmungku310StimmeAbgeben
     ***********************************/
    @POST
    @Path("abstimmungku310StimmeAbgeben")
    @Produces({ MediaType.APPLICATION_JSON, MediaType.TEXT_XML })
    public WEAbstimmungku310StimmeAbgebenRC abstimmungku310StimmeAbgeben(
            WEAbstimmungku310StimmeAbgeben weAbstimmungku310StimmeAbgeben) {

        WEAbstimmungku310StimmeAbgebenRC weAbstimmungku310StimmeAbgebenRC = new WEAbstimmungku310StimmeAbgebenRC();
        /* TODO Achtung App - es erfolgt keine Überprüfung des Teilnehmers */
        int rc = starteService(weAbstimmungku310StimmeAbgeben.getWeLoginVerify(), "abstimmungku310StimmeAbgeben", false, true);
        if (rc < 1) {
            weAbstimmungku310StimmeAbgebenRC.setRc(rc);
            return weAbstimmungku310StimmeAbgebenRC;
        }
        if (rc == 2) {
            weAbstimmungku310StimmeAbgebenRC.setPruefeVersion(1);
        }

        eclDbM.openWeitere();

        boolean brc=tStimmabgabeku310.abstimmungku310StimmeAbgeben(weAbstimmungku310StimmeAbgeben.abstimmungku310);

        if (brc==false) {
            weAbstimmungku310StimmeAbgebenRC.setRc(-99999);
        }
        beendeService();
        return weAbstimmungku310StimmeAbgebenRC;
    }
   
    
    
    

}