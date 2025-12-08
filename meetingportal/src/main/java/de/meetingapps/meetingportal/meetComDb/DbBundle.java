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
package de.meetingapps.meetingportal.meetComDb;

import de.meetingapps.meetingportal.meetComAllg.CaBug;
import de.meetingapps.meetingportal.meetComAllg.CaString;
import de.meetingapps.meetingportal.meetComBVerwaltung.BvReload;
import de.meetingapps.meetingportal.meetComBl.BlFuelleStaticAusDBAufClient;
import de.meetingapps.meetingportal.meetComEntities.EclEmittenten;
import de.meetingapps.meetingportal.meetComEntities.EclFehler;
import de.meetingapps.meetingportal.meetComEntities.EclGeraetKlasseSetZuordnung;
import de.meetingapps.meetingportal.meetComEntities.EclGeraeteSet;
import de.meetingapps.meetingportal.meetComEntities.EclTermine;
import de.meetingapps.meetingportal.meetComEntities.EclUserLogin;
import de.meetingapps.meetingportal.meetComHVParam.ClGlobalVar;
import de.meetingapps.meetingportal.meetComHVParam.HVParam;
import de.meetingapps.meetingportal.meetComHVParam.ParamGeraet;
import de.meetingapps.meetingportal.meetComHVParam.ParamS;
import de.meetingapps.meetingportal.meetComHVParam.ParamServer;

/*TODO _DBÄnderungen
 * 
 * EclEmittenten:
 * 	public String hvCode="";
 * 
 * EclFehler
 * 	public String kuerzel="" (LEN=50);
 * 
 * 
 * DbDrucklauf komplett neu anlegen bei jedem Mandanten
 * 
 * ALTER TABLE `db_meetingcomfort`.`tbl_emittenten` 
	ADD COLUMN `pfadErgKundenOrdner` VARCHAR(80) NULL DEFAULT NULL AFTER `inAuswahl`;
	
	
	ALTER TABLE `db_mc115j2018ap`.`tbl_abstimmungmeldung` 
	ADD COLUMN `gattung` INT(11) NULL DEFAULT NULL AFTER `stimmen`,
	DROP PRIMARY KEY,
	ADD PRIMARY KEY (`mandant`, `meldungsIdent`);



 */

public class DbBundle {

    private int logDrucken=10;
    
    public DbAbstimmungku310 dbAbstimmungku310 = null; //Weitere
    public DbAbstimmungen dbAbstimmungen = null; //ab
    public DbAbstimmungenEinzelAusschluss dbAbstimmungenEinzelAusschluss = null; //abEA
    public DbAbstimmungenZuStimmkarte dbAbstimmungenZuStimmkarte = null; //abZski
    public DbAbstimmungMeldung dbAbstimmungMeldung = null;
    public DbAbstimmungMeldungArchiv dbAbstimmungMeldungArchiv = null;
    public DbAbstimmungMeldungRaw dbAbstimmungMeldungRaw = null;
    public DbAbstimmungMeldungSperre dbAbstimmungMeldungSperre = null;
    public DbAbstimmungMeldungSplit dbAbstimmungMeldungSplit = null;
    public DbAbstimmungsblock dbAbstimmungsblock = null; //abb
    public DbAbstimmungsmonitorEK dbAbstimmungsmonitorEK = null; //amo
    public DbAbstimmungsVorschlag dbAbstimmungsVorschlag = null;
    public DbAbstimmungsVorschlagEmpfehlung dbAbstimmungsVorschlagEmpfehlung = null; //Weitere
    public DbAbstimmungZuAbstimmungsblock dbAbstimmungZuAbstimmungsblock = null; //abZabb
    public DbAenderungslog dbAenderungslog = null;
    public DbAktienregister dbAktienregister = null; // are 
    public DbAktienregisterErgaenzung dbAktienregisterErgaenzung = null; // Weitere - areerg 
    public DbAktienregisterLoginDaten dbAktienregisterLoginDaten = null; //arel.*/
    public DbAktienregisterMailRuecklauf dbAktienregisterMailRuecklauf = null;//armr - Nur im Zusatz
    @Deprecated
    public DbAktienregisterZusatz dbAktienregisterZusatz = null; //arez
    public DbAktienregisterUpdate dbAktienregisterUpdate = null; //(.areu - noch nicht implementiert!)
    public DbAnreden dbAnreden = null; //an., anf.
    public DbAppAktivitaeten dbAppAktivitaeten = null;
    public DbAppTexte dbAppTexte = null;
    public DbAppVersion dbAppVersion = null;
    public DbAuftrag dbAuftrag = null;
    public DbAusstellungsgrund dbAusstellungsgrund = null;
    public DbAutoTest dbAutoTest = null; //aut
    public DbBasis dbBasis = null;
    public DbBestWorkflow dbBestWorkflow = null; //bwo
    public DbBlzBank dbBlzBank = null; //bb - weitere
    public DbInhaberImportAnmeldedaten dbInhaberImportAnmeldedaten = null;
    public DbDatenbankVerwaltung dbDatenbankVerwaltung = null;
    public DbDrucklauf dbDrucklauf = null; //Weitere
    public DbEindeutigeKennung dbEindeutigeKennung = null;
    public DbEindeutigerKey dbEindeutigerKey = null;
    public DbEmittenten dbEmittenten = null; //em.
    @Deprecated
    public DbFragen dbFragen = null; //Weitere
    public DbFehler dbFehler = null;
    public DbGeraeteKlasse dbGeraeteKlasse = null;
    public DbGeraeteKlasseSetZuordnung dbGeraeteKlasseSetZuordnung = null;
    public DbGeraeteLabelPrinter dbGeraeteLabelPrinter = null;
    public DbGeraeteSet dbGeraeteSet = null;
    public DbGruppen dbGruppen = null;
    public DbHVDatenLfd dbHVDatenLfd = null;
    public DbHTable dbHTable = null;
    public DbImportAdresse dbImportAdresse = null;
    public DbImportProfil dbImportProfil = null;
    public DbImportProtokoll dbImportProtokoll = null;
    public DbInfo dbInfo = null;
    public DbInsti dbInsti = null; //insti - weitere
    public DbInstiBestandsZuordnung dbInstiBestandsZuordnung = null; //inbz - weitere
    public DbInstiEmittentenMitZuordnung dbInstiEmittentenMitZuordnung = null; //inemzu
    public DbInstiProv dbInstiProv = null; //inp
    public DbInstiSubZuordnung dbInstiSubZuordnung = null; //instisu - weitere
    public DbIpTracking dbIpTracking = null; //ipt
    public DbIsin dbIsin = null; //i - weitere
    public DbJoined dbJoined = null;
    public DbKommunikationsSprachen dbKommunikationsSprachen = null;
    public DbKonfigAuswertung dbKonfigAuswertung = null; //ka.
    public DbKontaktformularThema dbKontaktformularThema = null; //
    public DbKoordinaten dbKoordinaten = null; // ko. - weitere
    public DbKTracking dbKTracking = null; //kt - weitere

    public DbLoginDaten dbLoginDaten = null; //lo
    public DbLoginDatenDemo dbLoginDatenDemo = null; //lod - weitere
    public DbLowLevel dbLowLevel = null;
    public DbMailing dbMailing = null; //weitere
    public DbMailingStatus dbMailingStatus = null; //weitere
    public DbMeldungAusstellungsgrund dbMeldungAusstellungsgrund = null;
    /** me*/
    public DbMeldungen dbMeldungen = null; //m.
    public DbMeldungenMeldungen dbMeldungenMeldungen = null;
    public DbMeldungenVipKZAusgeblendet dbMeldungenVipKZAusgeblendet = null;
    /**vk*/
    public DbMeldungVipKZ dbMeldungVipKZ = null;
    public DbMeldungVirtuellePraesenz dbMeldungVirtuellePraesenz = null;
    public DbMeldungZuSammelkarte dbMeldungZuSammelkarte = null;
    public DbMenueEintrag dbMenueEintrag = null;
    public DbMessageId dbMessageId = null;
    public DbMitteilung dbMitteilung = null; //früher Weitere, nun in allgemeinen Teil drin, da doch sehr häufig im Portal benötigt.
    public DbMitteilungBestand dbMitteilungBestand = null; //Weitere
    @Deprecated
    public DbMitteilungenAlt dbMitteilungenAlt = null; //Weitere
    public DbNavToBM dbNavToBM = null; // ntb. - weitere
    public DbNummernForm dbNummernForm = null;
    public DbNachricht dbNachricht = null; //nac
    public DbNachrichtAnhang dbNachrichtAnhang = null; //nac
    public DbNachrichtBasisText dbNachrichtBasisText = null; //nacba
    public DbNachrichtVerwendungsCode dbNachrichtVerwendungsCode = null; //nacco
    public DbNummernFormSet dbNummernFormSet = null;
    public DbNummernkreis dbNummernkreis = null; //nk
    public DbPlzOrt dbPlzOrt = null; //po - weitere
    public DbParameter dbParameter = null;
    public DbParameterSet dbParameterSet = null; //Weitere - ps

    public DbParamStrukt dbParamStrukt = null; //paramStrukt
    public DbParamStruktAblaufElement dbParamStruktAblaufElement = null; //paramStrukt
    public DbParamStruktAblaufHeader dbParamStruktAblaufHeader = null; //paramStrukt
    public DbParamStruktGruppen dbParamStruktGruppen = null; //paramStrukt
    public DbParamStruktGruppenHeader dbParamStruktGruppenHeader = null; //paramStrukt
    public DbParamStruktPresetArt dbParamStruktPresetArt = null; //paramStrukt
    public DbParamStruktStandard dbParamStruktStandard = null; //paramStrukt
    public DbParamStruktVersammlungstyp dbParamStruktVersammlungstyp = null; //paramStrukt
    public DbParamStruktWerte dbParamStruktWerte = null; //paramStrukt

    public DbPdfInBrowser dbPdfInBrowser = null; //Weitere - pd
    public DbPersonen dbPersonen = null; //Weitere - pers
    public DbPersonenNatJur dbPersonenNatJur = null; //p., p1.
    public DbPersonenNatJurVersandadresse dbPersonenNatJurVersandadresse = null; //pv.
    public DbPersonenprognose dbPersonenprognose = null; // pp. - weitere
    public DbPortalTexte dbPortalTexte = null;
    public DbPortalUnterlagen dbPortalUnterlagen = null; //pu.
    public DbPraesenz dbPraesenz = null;
    public DbPraesenzliste dbPraesenzliste = null;
    public DbPublikation dbPublikation = null;
    public DbScan dbScan = null; //sc.
    public DbServiceDeskSet dbServiceDeskSet = null;
    public DbServiceDeskWorkflow dbServiceDeskWorkflow = null;
    public DbSperre dbSperre = null;
    public DbSprachen dbSprachen = null;
    public DbSRDMessage dbSRDMessage = null;
    public DbStaaten dbStaaten = null; //st., st1.
    /**vka*/
    public DbStimmkarteInhalt dbStimmkarteInhalt = null; //ski
    public DbStimmkarten dbStimmkarten = null; //sk
    public DbStimmkartenSecond dbStimmkartenSecond = null;
    public DbSuchlaufBegriffe dbSuchlaufBegriffe = null; //Weitere
    public DbSuchlaufDefinition dbSuchlaufDefinition = null; //Weitere
    public DbSuchlaufErgebnis dbSuchlaufErgebnis = null; //sue Weitere
    public DbTablet dbTablet = null;
    public DbTeilnehmerStandVerein dbTeilnehmerStandVerein = null;
    public DbTermine dbTermine = null; //tm
    public DbUserLogin dbUserLogin = null; //ul
    public DbUserProfile dbUserProfile = null; //up
    public DbVeranstaltung dbVeranstaltung = null; //Weitere

    public DbVeranstaltungenVeranstaltung dbVeranstaltungenVeranstaltung = null; //Weitere (eigentlich, aber temporär wegen Einlesungen bei Parametern in allgemeinen
    public DbVeranstaltungenElement dbVeranstaltungenElement = null; //Weitere
    public DbVeranstaltungenElementDetail dbVeranstaltungenElementDetail = null; //Weitere
    public DbVeranstaltungenMenue dbVeranstaltungenMenue = null; //Weitere
    public DbVeranstaltungenAktion dbVeranstaltungenAktion = null; //Weitere
    public DbVeranstaltungenQuittungElement dbVeranstaltungenQuittungElement = null; //Weitere
    public DbVeranstaltungenReportElement dbVeranstaltungenReportElement = null; //Weitere
    public DbVeranstaltungenWert dbVeranstaltungenWert = null; //Weitere

    public DbVerarbeitungsLauf dbVerarbeitungsLauf = null; //vl
    public DbVerarbeitungsProtokoll dbVerarbeitungsProtokoll = null; //vp
    public DbVertretungsArten dbVertretungsarten = null;
    public DbVorlaeufigeVollmacht dbVorlaeufigeVollmacht = null; //vv 
    public DbVorlaeufigeVollmachtEingabe dbVorlaeufigeVollmachtEingabe = null; //Weitere 
    public DbVipKZ dbVipKZ = null;
    public DbWeisungMeldung dbWeisungMeldung = null; //(wm. - noch nicht implementiert)
    public DbWillenserklaerung dbWillenserklaerung = null; //w.
    public DbWillenserklaerungZusatz dbWillenserklaerungZusatz = null; //wz.

    public DbWortmeldetischAktion dbWortmeldetischAktion = null;
    public DbWortmeldetischFolgeStatus dbWortmeldetischFolgeStatus = null;
    public DbWortmeldetischProtokoll dbWortmeldetischProtokoll = null;
    public DbWortmeldetischSet dbWortmeldetischSet = null;
    public DbWortmeldetischStatus dbWortmeldetischStatus = null;
    public DbWortmeldetischStatusWeiterleitung dbWortmeldetischStatusWeiterleitung = null;
    public DbWortmeldetischView dbWortmeldetischView = null;
    public DbWortmeldetischViewStatus dbWortmeldetischViewStatus = null;

    @Deprecated
    public DbWortmeldungen dbWortmeldungen = null; //Weitere
    public DbZugeordneteAktionaerePraesenz dbZugeordneteAktionaerePraesenz = null; //weitere
    public DbZuordnungKennung dbZuordnungKennung = null;
    public DbZutrittskarten dbZutrittskarten = null; //zk.

    /**Wird bei jedem Close und normalem Open auf false gesetzt.
     * Nur bei openweitere auf true.
     */
    private boolean weitereSindOffen = false;

    public EclEmittenten eclEmittent = null;

    public ParamServer paramServer = null;
    public EclGeraeteSet eclGeraeteSet = null;
    public EclGeraetKlasseSetZuordnung eclGeraetKlasseSetZuordnung = null;
    public ParamGeraet paramGeraet = null;

    public HVParam param = null;

    public EclTermine[] terminlisteTechnisch = null;
    public EclTermine liefereTermin(int pIdentTermin) {
        CaBug.druckeLog("pIdentTermin="+pIdentTermin, logDrucken, 10);
        if (terminlisteTechnisch==null) {
            CaBug.druckeLog("return null 1", logDrucken, 10);
            return null;
        }
        for (EclTermine iTermin: terminlisteTechnisch) {
            if (iTermin.identTermin==pIdentTermin) {
                return iTermin;
            }
        }
        CaBug.druckeLog("return null 2", logDrucken, 10);
        return null;
    }

    /**Steht nur bei EE zur Verfügung, nicht bei Clients!*/
    public EclFehler fehlerDeutschArray[] = null;
    /**Steht nur bei EE zur Verfügung, nicht bei Clients!*/
    public EclFehler fehlerEnglischArray[] = null;

    public ClGlobalVar clGlobalVar = null;

    public EclUserLogin eclUserLogin = null;

    /**Nicht in openall, sondern bei openWeitere*/
    public DbAufgaben dbAufgaben = null; //auf.	

    public boolean mandantenTablesGetrennt = true;

    /*****************************Datenbank-Open/Close, mit Parameterverwaltung**********************************************/

    /**Voraussetzung: Parameter sind bereits eingelesen.
     * Nur für Clients, nicht für EE - für EE EclDbM.openall benutzen!*/
    public DbBundle() {
        refreshParameterAusStatic();

    }

    /**Kann aufgerufen werden, wenn DbBundle bereits existiert, und die Parameter in Dbbundle neu aus den Static-Werten ParamS etc. 
     * geladen werden sollen. Nur für Clients, nicht für EE!
     */
    public void refreshParameterAusStatic() {
        /*Kann immer durchgeführt werden - falls alles noch nicht eingelesen, dann wird halt Null kopiert*/
        eclEmittent = ParamS.eclEmittent;
        paramServer = ParamS.paramServer;
        eclGeraeteSet = ParamS.eclGeraeteSet;
        eclGeraetKlasseSetZuordnung = ParamS.eclGeraetKlasseSetZuordnung;
        paramGeraet = ParamS.paramGeraet;
        param = ParamS.param;
        terminlisteTechnisch = ParamS.terminlisteTechnisch;
        if (terminlisteTechnisch==null) {
            CaBug.druckeLog("terminlisteTechnisch==null", logDrucken, 10);
        }

        eclUserLogin = ParamS.eclUserLogin;

        //		fehlerDeutschArray=FehlerS.deutscharray;
        //		fehlerEnglischArray=FehlerS.englischarray;

        clGlobalVar = ParamS.clGlobalVar;

    }

    /**Initialisierung für EclDbM. pDummy nur zur Unterscheidung von normalem DbBundle() erforderlich*/
    public DbBundle(boolean pDummy) {

    }

    /**Open und Überprüfen, ob Reload der Parameter erforderlich. 
     * 
     * Darf nur verwendet werden, wenn die Parameter bereits eingelesen sind!
     * 
     * Darf NICHT in Wildfly-Server-Umgebung verwendet werden! Dort nur openAllOhneParameterCheck
     * verwenden*/
    public void openAll() {
        openAllOhneParameterCheck();
        BvReload bvReload = new BvReload(this);
        bvReload.checkReload(clGlobalVar.mandant);
        BlFuelleStaticAusDBAufClient blFuellePufferAusDBAufClient = new BlFuelleStaticAusDBAufClient(this);
        blFuellePufferAusDBAufClient.fuelleGlobalParam(false, bvReload);
        if (clGlobalVar.mandant != 0) {
            blFuellePufferAusDBAufClient.fuelleMandantenParam(false, bvReload);
        }
        blFuellePufferAusDBAufClient.fuelleUserLogin(false, bvReload);
    }

    /**Initialisieren auf "Open in EE"; danach openAllOhneParameterCheck und ggf. openWeitere aufrufen*/
    public void openEE() {
        DbBasis.setIstEE(true);
        openAllOhneParameterCheck();
        dbBasis.beginTransaction();
    }

    /**Nur open. Wird verwendet für die Client-Startprozedur für das Einlesen der Parameter,
     * für die Pflege der Parameter (sonst werden die gerade geänderten möglicherweise
     * vor dem Speichern überschrieben!),
     * sowie aus EclDbM heraus (EclDbM übernimmt ggf. Reload)
     */
    public void openAllOhneParameterCheck() {
        dbBasis = new DbBasis(this);

        initialisiereDbKlassen();
    }

    private void initialisiereDbKlassen() {

        dbAbstimmungen = new DbAbstimmungen(this);
        dbAbstimmungenEinzelAusschluss = new DbAbstimmungenEinzelAusschluss(this);
        dbAbstimmungenZuStimmkarte = new DbAbstimmungenZuStimmkarte(this);
        dbAbstimmungMeldung = new DbAbstimmungMeldung(this);
        dbAbstimmungMeldungArchiv = new DbAbstimmungMeldungArchiv(this);
        dbAbstimmungMeldungRaw = new DbAbstimmungMeldungRaw(this);
        dbAbstimmungMeldungSperre = new DbAbstimmungMeldungSperre(this);
        dbAbstimmungMeldungSplit = new DbAbstimmungMeldungSplit(this);
        dbAbstimmungsblock = new DbAbstimmungsblock(this);
        dbAbstimmungsmonitorEK = new DbAbstimmungsmonitorEK(this);
        dbAbstimmungsVorschlag = new DbAbstimmungsVorschlag(this);
        dbAbstimmungZuAbstimmungsblock = new DbAbstimmungZuAbstimmungsblock(this);
        dbAenderungslog = new DbAenderungslog(this);
        dbAktienregister = new DbAktienregister(this);
        dbAktienregisterErgaenzung = new DbAktienregisterErgaenzung(this);
        dbAktienregisterLoginDaten = new DbAktienregisterLoginDaten(this);
        dbAktienregisterZusatz = new DbAktienregisterZusatz(this);
        dbAktienregisterUpdate = new DbAktienregisterUpdate(this);
        dbAnreden = new DbAnreden(this);
        dbAppTexte = new DbAppTexte(this);
        dbAppVersion = new DbAppVersion(this);
        dbAuftrag = new DbAuftrag(this);
        dbAusstellungsgrund = new DbAusstellungsgrund(this);
        dbBestWorkflow = new DbBestWorkflow(this);
        dbInhaberImportAnmeldedaten = new DbInhaberImportAnmeldedaten(this);
        dbDatenbankVerwaltung = new DbDatenbankVerwaltung(this);
        dbEindeutigeKennung = new DbEindeutigeKennung(this);
        dbEindeutigerKey = new DbEindeutigerKey(this);
        dbEmittenten = new DbEmittenten(this);
        dbFehler = new DbFehler(this);
        dbGeraeteKlasse = new DbGeraeteKlasse(this);
        dbGeraeteKlasseSetZuordnung = new DbGeraeteKlasseSetZuordnung(this);
        dbGeraeteLabelPrinter = new DbGeraeteLabelPrinter(this);
        dbGeraeteSet = new DbGeraeteSet(this);
        dbGruppen = new DbGruppen(this);
        dbHVDatenLfd = new DbHVDatenLfd(this);
        dbImportAdresse = new DbImportAdresse(this);
        dbImportProfil = new DbImportProfil(this);
        dbImportProtokoll = new DbImportProtokoll(this);
        dbInfo = new DbInfo(this);
        dbInstiProv = new DbInstiProv(this);
        dbIpTracking = new DbIpTracking(this);
        dbJoined = new DbJoined(this);
        dbKommunikationsSprachen = new DbKommunikationsSprachen(this);
        dbKonfigAuswertung = new DbKonfigAuswertung(this);
        dbKontaktformularThema = new DbKontaktformularThema(this);
        dbLoginDaten = new DbLoginDaten(this, false);
        dbLoginDatenDemo = new DbLoginDatenDemo(this);
        dbLowLevel = new DbLowLevel(this);
        dbMeldungAusstellungsgrund = new DbMeldungAusstellungsgrund(this);
        dbMeldungen = new DbMeldungen(this);
        dbMeldungenMeldungen = new DbMeldungenMeldungen(this);
        dbMeldungenVipKZAusgeblendet = new DbMeldungenVipKZAusgeblendet(this);
        dbMeldungVipKZ = new DbMeldungVipKZ(this);
        dbMeldungVirtuellePraesenz = new DbMeldungVirtuellePraesenz(this);
        dbMeldungZuSammelkarte = new DbMeldungZuSammelkarte(this);
        dbMenueEintrag = new DbMenueEintrag(this);
        dbMitteilung = new DbMitteilung(this);
        dbNummernForm = new DbNummernForm(this);
        dbNummernFormSet = new DbNummernFormSet(this);
        dbNummernkreis = new DbNummernkreis(this);
        dbParameter = new DbParameter(this);
        dbPersonenNatJur = new DbPersonenNatJur(this);
        dbPersonenNatJurVersandadresse = new DbPersonenNatJurVersandadresse(this);
        dbPortalTexte = new DbPortalTexte(this);
        dbPortalUnterlagen = new DbPortalUnterlagen(this);
        dbPraesenz = new DbPraesenz(this);
        dbPraesenzliste = new DbPraesenzliste(this);
        dbPublikation = new DbPublikation(this);
        dbScan = new DbScan(this);
        dbServiceDeskSet = new DbServiceDeskSet(this);
        dbServiceDeskWorkflow = new DbServiceDeskWorkflow(this);
        dbSperre = new DbSperre(this);
        dbSprachen = new DbSprachen(this);
        dbStaaten = new DbStaaten(this);
        dbStimmkarteInhalt = new DbStimmkarteInhalt(this);
        dbStimmkarten = new DbStimmkarten(this);
        dbStimmkartenSecond = new DbStimmkartenSecond(this);
        dbTablet = new DbTablet(this);
        dbTeilnehmerStandVerein = new DbTeilnehmerStandVerein(this);
        dbTermine = new DbTermine(this);
        dbUserLogin = new DbUserLogin(this);
        dbUserProfile = new DbUserProfile(this);
        dbVerarbeitungsLauf = new DbVerarbeitungsLauf(this);
        dbVerarbeitungsProtokoll = new DbVerarbeitungsProtokoll(this);

        /*Eigentlich unter "weitere"*/
        dbVeranstaltungenVeranstaltung = new DbVeranstaltungenVeranstaltung(this);
        dbVeranstaltungenElement = new DbVeranstaltungenElement(this);
        dbVeranstaltungenElementDetail = new DbVeranstaltungenElementDetail(this);
        dbVeranstaltungenMenue = new DbVeranstaltungenMenue(this);
        dbVeranstaltungenAktion = new DbVeranstaltungenAktion(this);
        dbVeranstaltungenQuittungElement = new DbVeranstaltungenQuittungElement(this);
        dbVeranstaltungenWert = new DbVeranstaltungenWert(this);
        dbVeranstaltungenReportElement = new DbVeranstaltungenReportElement(this);

        dbVertretungsarten = new DbVertretungsArten(this);
        dbVipKZ = new DbVipKZ(this);
        dbVorlaeufigeVollmacht = new DbVorlaeufigeVollmacht(this);
        dbWeisungMeldung = new DbWeisungMeldung(this);
        dbWillenserklaerung = new DbWillenserklaerung(this);
        dbWillenserklaerungZusatz = new DbWillenserklaerungZusatz(this);

        dbWortmeldetischAktion = new DbWortmeldetischAktion(this);
        dbWortmeldetischFolgeStatus = new DbWortmeldetischFolgeStatus(this);
        dbWortmeldetischProtokoll = new DbWortmeldetischProtokoll(this);
        dbWortmeldetischSet = new DbWortmeldetischSet(this);
        dbWortmeldetischStatus = new DbWortmeldetischStatus(this);
        dbWortmeldetischStatusWeiterleitung = new DbWortmeldetischStatusWeiterleitung(this);
        dbWortmeldetischView = new DbWortmeldetischView(this);
        dbWortmeldetischViewStatus = new DbWortmeldetischViewStatus(this);

        dbZuordnungKennung = new DbZuordnungKennung(this);
        dbZutrittskarten = new DbZutrittskarten(this);

        weitereSindOffen = false;
    }

    /**Eröffnet nur "Weitere"*/
    public void openWeitere() {
        dbAbstimmungku310 = new DbAbstimmungku310(this);
        dbAktienregisterMailRuecklauf = new DbAktienregisterMailRuecklauf(this);
        dbAbstimmungsVorschlagEmpfehlung = new DbAbstimmungsVorschlagEmpfehlung(this);
        dbAppAktivitaeten = new DbAppAktivitaeten(this);
        dbAufgaben = new DbAufgaben(this);
        dbAutoTest = new DbAutoTest(this);

        dbBlzBank = new DbBlzBank(this);

        dbDrucklauf = new DbDrucklauf(this);
        dbFragen = new DbFragen(this);
        dbHTable = new DbHTable(this);

        dbInsti = new DbInsti(this);
        dbInstiBestandsZuordnung = new DbInstiBestandsZuordnung(this);
        dbInstiEmittentenMitZuordnung = new DbInstiEmittentenMitZuordnung(this);
        dbInstiSubZuordnung = new DbInstiSubZuordnung(this);

        dbIsin = new DbIsin(this);

        dbKoordinaten = new DbKoordinaten(this);
        dbKTracking = new DbKTracking(this);
        dbMailing = new DbMailing(this);
        dbMailingStatus = new DbMailingStatus(this);
        dbMessageId = new DbMessageId(this);
        dbMitteilungBestand = new DbMitteilungBestand(this);
        dbMitteilungenAlt = new DbMitteilungenAlt(this);
        dbNachricht = new DbNachricht(this);
        dbNachrichtAnhang = new DbNachrichtAnhang(this);
        dbNachrichtBasisText = new DbNachrichtBasisText(this);
        dbNachrichtVerwendungsCode = new DbNachrichtVerwendungsCode(this);

        dbNavToBM = new DbNavToBM(this);
        dbPlzOrt = new DbPlzOrt(this);

        dbParameterSet = new DbParameterSet(this);
        dbPdfInBrowser = new DbPdfInBrowser(this);
        dbPersonen = new DbPersonen(this);
        dbPersonenprognose = new DbPersonenprognose(this);
        dbSRDMessage = new DbSRDMessage(this);
        dbSuchlaufBegriffe = new DbSuchlaufBegriffe(this);
        dbSuchlaufDefinition = new DbSuchlaufDefinition(this);
        dbSuchlaufErgebnis = new DbSuchlaufErgebnis(this);
        dbVeranstaltung = new DbVeranstaltung(this);
        
        dbVorlaeufigeVollmachtEingabe = new DbVorlaeufigeVollmachtEingabe(this);

        dbZugeordneteAktionaerePraesenz = new DbZugeordneteAktionaerePraesenz(this);

        dbWortmeldungen = new DbWortmeldungen(this);

        weitereSindOffen = true;
    }

    public void openParamStrukt() {
        dbParamStrukt = new DbParamStrukt(this);
        dbParamStruktAblaufElement=new DbParamStruktAblaufElement(this);
        dbParamStruktAblaufHeader=new DbParamStruktAblaufHeader(this);
        dbParamStruktGruppen = new DbParamStruktGruppen(this);
        dbParamStruktGruppenHeader = new DbParamStruktGruppenHeader(this);
        dbParamStruktPresetArt = new DbParamStruktPresetArt(this);
        dbParamStruktStandard = new DbParamStruktStandard(this);
        dbParamStruktVersammlungstyp = new DbParamStruktVersammlungstyp(this);
        dbParamStruktWerte = new DbParamStruktWerte(this);
    }

    public void closeAll() {
        dbBasis.close();
        weitereSindOffen = false;
    }

    /**Schließen innerhalt einer EE-Umgebung*/
    public void closeEEAll() {
        dbBasis.endTransactionFinal();
        dbBasis.close();
        weitereSindOffen = false;
    }

    /**Mit Vorsicht zu genießen - ausführlich testen!
     * Kann aufgerufen werden, egal ob Open mit EclDbM oder mit DbBundle erfolgte.
     * Schließt Transaktion und Verbindung und eröffnet diese neu.
     * Damit sollten alle Locks gelöst sein.
     * 
     * Allerdings sind danach auch alle DB-Klassen neu initialisiert, d.h. Ergebnis-Arrays sind alle leer!
     */
    public void reOpen() {
        boolean vorherWeitereSindOffen = weitereSindOffen;

        if (DbBasis.istEE) {
            dbBasis.endTransactionFinal();
        }
        dbBasis.close();

        dbBasis.open();
        initialisiereDbKlassen();
        if (DbBasis.istEE) {
            dbBasis.beginTransaction();
        }
        if (vorherWeitereSindOffen) {
            openWeitere();

        }
        weitereSindOffen = vorherWeitereSindOffen;
    }

    /***************************************Mandantenzugriff***************************************************************/
    /**Liefert Mandanten-Nummer 3-stellig mit führenden 0 in String*/
    public String getMandantString() {
        String h = Integer.toString(this.clGlobalVar.mandant);
        while (h.length() < 3) {
            h = "0" + h;
        }
        return h;
    }

    /**Liefert "M"+Mandantennummer + j + HVJAhr + hvnummer+datenbereich, um dies in den Pfad aufzunehmen*/
    public String getMandantPfad() {
        return "M" + getMandantString() + "J" + CaString.fuelleLinksNull(Integer.toString(clGlobalVar.hvJahr), 4)
                + clGlobalVar.hvNummer + clGlobalVar.datenbereich;
    }

    public void setzeMandantAufNull() {
        clGlobalVar.mandant = 0;
        clGlobalVar.hvJahr = 0;
        clGlobalVar.hvNummer = "0";
        clGlobalVar.datenbereich = "0";
    }

    /**liest aus pSchemaMandant den mandanten, und überträgt diesen in clGlobalVar*/
    public void setzeMandantAusSchemaMandant(String pSchemaMandant) {
        clGlobalVar.mandant = Integer.parseInt(pSchemaMandant.substring(5, 8));
        clGlobalVar.hvJahr = Integer.parseInt(pSchemaMandant.substring(9, 13));
        clGlobalVar.hvNummer = pSchemaMandant.substring(13, 14);
        clGlobalVar.datenbereich = pSchemaMandant.substring(14, 15);
        //		System.out.println("clGlobalVar.mandant="+clGlobalVar.mandant);
        //		System.out.println("clGlobalVar.hvJahr="+clGlobalVar.hvJahr);
        //		System.out.println("clGlobalVar.hvNummer="+clGlobalVar.hvNummer);
        //		System.out.println("clGlobalVar.datenbereich="+clGlobalVar.datenbereich);
    }

    /**********************************Mandanten-Teil / Datenbankschemas*******************************************/

    public int rcMandant = 0;
    public int rcHVJahr = 0;
    public String rcHVNummer = "";
    public String rcDatenbereich = "";

    /**liest aus pSchemaMandant den mandanten, und überträgt diesen in clGlobalVar*/
    public void setzeRCMandantAusSchemaMandant(String pSchemaMandant) {
        rcMandant = Integer.parseInt(pSchemaMandant.substring(5, 8));
        rcHVJahr = Integer.parseInt(pSchemaMandant.substring(9, 13));
        rcHVNummer = pSchemaMandant.substring(13, 14);
        rcDatenbereich = pSchemaMandant.substring(14, 15);
        //		System.out.println("clGlobalVar.mandant="+clGlobalVar.mandant);
        //		System.out.println("clGlobalVar.hvJahr="+clGlobalVar.hvJahr);
        //		System.out.println("clGlobalVar.hvNummer="+clGlobalVar.hvNummer);
        //		System.out.println("clGlobalVar.datenbereich="+clGlobalVar.datenbereich);
    }

    public String getSchemaMandant() {
        if (this.mandantenTablesGetrennt) {
            return "db_mc" + getMandantString() + "j"
                    + CaString.fuelleLinksNull(Integer.toString(clGlobalVar.hvJahr), 4) + clGlobalVar.hvNummer
                    + clGlobalVar.datenbereich + ".";
        } else {
            return getSchemaAllgemein();
        }
    }

    public String getSchemaMandantOhnePunkt() {
        return getSchemaMandant().replace(".", "");
    }

    public String getSchemaAllgemein() {
        return "db_meetingcomfort.";
    }

    /****************************************Pfadnamen*************************************************/

    /**Liefert Pfad für meetingreports in der Form
     * 	D:\\meetingreports\\mc
     */
    public String lieferePfadMeetingReportsAllgemein() {
        return clGlobalVar.lwPfadAllgemein + "\\" + this.paramServer.praefixPfadVerzeichnisse + "reports\\mc";
    }

    /**Liefert Pfad für meetingreports in der Form
     * 	D:\\meetingreports\\M001J2020AP
     */
    public String lieferePfadMeetingReports() {
        return clGlobalVar.lwPfadAllgemein + "\\" + this.paramServer.praefixPfadVerzeichnisse + "reports\\"
                + getMandantPfad();
    }

    /**Liefert Pfad für meetingausdrucke in der Form
     *  D:\\meetingausdrucke\\M001J2020AP
     */
    public String lieferePfadMeetingAusdrucke() {
        return clGlobalVar.lwPfadAllgemein + "\\" + this.paramServer.praefixPfadVerzeichnisse + "ausdrucke\\"
                + getMandantPfad();
    }

    /**Liefert Pfad für meetingausdruckeintern in der Form
     *  D:\\meetingausdruckeintern\\M001J2020AP
     */
    public String lieferePfadMeetingAusdruckeIntern() {
        return clGlobalVar.lwPfadAllgemein + "\\" + this.paramServer.praefixPfadVerzeichnisse + "ausdruckeintern\\"
                + getMandantPfad();
    }

    /**Liefert Pfad für meetingoutput in der Form
     *  D:\\meetingoutput\\M001J2020AP
     */
    public String lieferePfadMeetingOutput() {
        return clGlobalVar.lwPfadAllgemein + "\\" + this.paramServer.praefixPfadVerzeichnisse + "output\\"
                + getMandantPfad();
    }

    /**Liefert Pfad für meetingoutput in der Form
     *  D:\\meetingoutput
     */
    public String lieferePfadMeetingOutputOhneSubVerzeichnis() {
        return clGlobalVar.lwPfadAllgemein + "\\" + this.paramServer.praefixPfadVerzeichnisse + "output";
    }

    /**Liefert Pfad für meetingpdf in der Form
     *  D:\\meetingpdf\\M001J2020AP
     */
    public String lieferePfadMeetingPdf() {
        return clGlobalVar.lwPfadAllgemein + "\\" + this.paramServer.praefixPfadVerzeichnisse + "pdf\\"
                + getMandantPfad();
    }

    /**Liefert Pfad für meetingreports in der Form
     *  D:\\meetingreports
     */
    public String lieferePfadMeetingReportsOhneSubVerzeichnis() {
        return clGlobalVar.lwPfadAllgemein + "\\" + this.paramServer.praefixPfadVerzeichnisse + "reports";
    }

    /*******************************Funktionen zur Parameter-Auswertung***********************************************/

    public boolean mindestensEineStimmkarteWirdZugeordnet() {
        boolean rc = false;
        for (int i = 1; i <= 5; i++) {
            if (this.param.paramBasis.getGattungAktiv(i)) {
                for (int i2 = 0; i2 < 5; i2++)
                    if (param.paramAkkreditierung.pPraesenzStimmkartenZuordnenGattung[i - 1][i2] == 1
                            || param.paramAkkreditierung.pPraesenzStimmkartenZuordnenAppGattung[i - 1][i2] == 1) {
                        rc = true;
                    }
            }
        }
        return rc;
    }

    /***********************************Delay- und Pending-Funktionen***********************************************/

    /**Hinweis zu den folgenden Funktionen: diese sind redundant in ParamAkkreditierung und DbBundle enthalten,
    * um je nach Situation leichten Zugriff zu erreichen. Unbedingt immer identisch halten!
    */

    /**++++++++Alte Delay-Funktionen - derzeit keine Logik dahinter. Nur aufgenommen, um die Reste aus DbBasis zu entfernen++++++*/
    /** Delay für alle Willenserklärungen */

    /**
     * Verwaltung des "Delay" von Willenserklärungen Parameter = Willenserklärung Rückgabe = true => Willenserklärung is
     * delayed, ansonsten false
     */
    @Deprecated
    public boolean willenserklaerungIstDelayed(int willenserklaerung) {
        return param.paramAkkreditierung.willenserklaerungIstDelayed(willenserklaerung);
    }

    /**
     * Falls true => erfolgt Zu/Abgang mit der selben Ident, dann: > delayed Zugang mit Abgang wird aufgelöst > delayed
     * Abgang mit Zugang wird aufgelöst
     */
    @Deprecated
    public boolean delayAufloesenAbZuGleicheIdent() {
        return param.paramAkkreditierung.delayAufloesenAbZuGleicheIdent();
    }

    /**
     * Falls true => erfolgt Zu/Abgang auch ggf. mit unterschiedlichen Ident, dann: > delayed Zugang mit Abgang wird
     * aufgelöst > delayed Abgang mit Zugang wird aufgelöst
     */
    @Deprecated
    public boolean delayAufloesenAbZuVerschiedeneIdent() {
        return param.paramAkkreditierung.delayAufloesenAbZuVerschiedeneIdent();
    }

    /**
     * Falls true, dann kann eine Vollmacht, oder auch Briefwahl, die delayed ist, widerrufen werden und der Vorgang
     * wird dann "nicht-delayed" durchgeführt.
     */
    @Deprecated
    public boolean delayWiderrufVollmachtBriefwahl() {
        return param.paramAkkreditierung.delayWiderrufVollmachtBriefwahl();
    }

    /**Pending - derzeit keine Logik dahinter. Nur aufgenommen, um die Reste aus DbBasis zu entfernen*/
    public boolean pendingIstMoeglich(int willenserklaerung) {
        return param.paramAkkreditierung.pendingIstMoeglich(willenserklaerung);
    }

    public boolean pendingIstZulaessig() {
        return param.paramAkkreditierung.pendingIstZulaessig();
    }

    /***********************************Delay- und Pending-Funktionen Ende***********************************************/

}
