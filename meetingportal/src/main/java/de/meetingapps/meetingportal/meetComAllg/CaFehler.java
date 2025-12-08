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
package de.meetingapps.meetingportal.meetComAllg;
/*
 eclFehlerM.getFehlertext(CaFehler.)
 */

public class CaFehler {

    /*Meldungen für PFlegedialoge*/
    public final static int pfdUnbekannterFehler = 0;
    public final static int pfdXyBereitsVorhanden = -1;
    public final static int pfFormatXyUnzulaessig = -2;
    public final static int pfXyNichtImZulaessigenNummernkreis = -3;
    public final static int pfAutomatischeVergabeXyNichtMoeglich = -4;
    public final static int pfXyNichtVorhanden = -5;
    public final static int pfXyWurdeVonAnderemBenutzerVeraendert = -6; //Portal
    public final static int pfZuordnungZuSichSelbstNichtZulaessig = -7;
    public final static int pfBitteDatenVollstaendigEingeben = -8;
    public final static int pfBitteXSchluesselYeingeben = -9;
    public final static int pfBitteXYAuswaehlen = -10;
    public final static int pfLoeschenNichtMoeglichSaetzeNochZugeordnet = -11;
    public final static int pfLetzterSatzErreicht = -12;
    public final static int pfErsterSatzErreicht = -13;
    public final static int pfNichtMoeglich = -14;
    public final static int pfNichtEindeutig = -15;

    /*PräsenzMeldungen*/
    public final static int pmMeldungIstStorniert = -1001;
    public final static int pmZutrittsIdentIstStorniert = -1002;
    public final static int pmBereitsAnwesend = -1003;
    public final static int pmNochNichtAnwesend = -1004;
    public final static int pmMeldungsIdentNichtVorhanden = -1005;
    public final static int pmZutrittsIdentNichtVorhanden = -1006;
    public final static int pmZutrittsIdentNichtEindeutig = -1007;
    public final static int pmZutrittsIdentGesperrt = -1008;
    public final static int pmStimmkarteNichtVorhanden = -1009;
    public final static int pmStimmkarteGesperrt = -1010;
    public final static int pmStimmkarteSecondNichtVorhanden = -1011;
    public final static int pmStimmkarteSecondGesperrt = -1012;
    public final static int pmMeldungNichHinreichendSpezifiziert = -1013;
    public final static int pmMeldungAktionaerIstKeinAktionaer = -1014;
    public final static int pmMeldungGastIstKeinGast = -1015;
    public final static int pmZutrittsIdentBereitsVorhanden = -1016;
    public final static int pmZutrittsIdentNichtGesperrt = -1017;
    public final static int pmKeinGastGefunden = -1018;
    public final static int pmStimmkarteBereitsVorhanden = -1019;
    public final static int pmStimmkarteNichtGesperrt = -1020;
    public final static int pmStimmkarteSecondBereitsVorhanden = -1021;
    public final static int pmStimmkarteSecondNichtGesperrt = -1022;
    public final static int pmVollmachtNurFuerAktionaerMoeglich = -1023;
    public final static int pmVollmachtGeberHatKeineVollmacht = -1024;
    public final static int pmVollmachtGeberVollmachtIstWiderrufen = -1025;
    public final static int pmBevollmaechtigterNichtVorhanden = -1026;
    public final static int pmVollmachtBereitsWiderrufen = -1027;
    public final static int pmAktionaerDarfNurDirektenNachfolgerWiderrufen = -1028;
    public final static int pmWeitereVollmachtFuerBevollmaechtigtemVorhanden = -1029;
    public final static int pmBevollmaechtigterDarfNurDirektenNachfolgerWiderrufen = -1030;
    public final static int pmEigenerVollmachtsgeberKannNichtWiderrufenWerden = -1031;
    public final static int pmVollmachtNichtVonVollmachtgeberAbhaenig = -1032;
    public final static int pmSammelkarteUnzulaessig = -1033;
    public final static int pmWillenserklaerungZuDieserSammelkarteUnzulaessig = -1034;
    public final static int pmWeisungsartZuDieserSammelkarteUnzulaessig = -1035;
    public final static int pmKeinEigenerAbstimmungsvorschlagVorhanden = -1036;
    public final static int pmFremderAbstimmungsvorschlagNichtVorhanden = -1037;
    public final static int pmAlteWeisungNichtReferenziert = -1038;
    public final static int pmAlteWeisungBereitsVeraendert = -1039;
    public final static int pmZuordnungZuSammelkarteNichtVorhanden = -1040;
    public final static int pmZuordnungSammelkarteNichtMehrGueltig = -1041;
    public final static int pmZuordnungSammelkarteNichtVonDieserPersonStornierbar = -1042;
    public final static int pmGesamterBestandBereitsAngemeldet = -1043;

    public final static int pmNichtAngemeldeterBestandZuKlein = -1044;
    public final static int pmNichtGenuegendAktienFuerSplit = -1045;

    public final static int pmKeineAnmeldungVorhanden = -1055;
    public final static int pmWillenserklaerungenVorhanden = -1057;

    /**Ab hier neu*/
    public final static int pmKeinAbstimmvorschlagVorhanden = -1058;
    public final static int pmFixAnmeldungVorhanden = -1059;

    public final static int pmNummernformUngueltig = -1060;
    public final static int pmNummernformAktionsnummerUngueltig = -1061;
    public final static int pmNummernformMandantUngueltig = -1062;
    public final static int pmKeineAnmeldungenVorhanden = -1063;
    public final static int pmFixAnmeldungenVorhanden = -1064;
    public final static int pmMehrAls2AnmeldungenVorhanden = -1065;
    public final static int pmStimmkarteNichtAnwesend = -1066;
    public final static int pmStimmkarteSecondNichtAnwesend = -1067;
    public final static int pmKannNichtPersoenlichPraesentWegenSammelkarte = -1068;

    public final static int pmNummernformZutrittsIdentUngueltig = -1069;
    public final static int pmNummernformStimmkarte1Ungueltig = -1070;
    public final static int pmNummernformStimmkarte2Ungueltig = -1071;
    public final static int pmNummernformStimmkarte3Ungueltig = -1072;
    public final static int pmNummernformStimmkarte4Ungueltig = -1073;
    public final static int pmNummernformStimmkarteSecondUngueltig = -1074;
    public final static int pmZuordnungFehlerhaft = -1075;
    public final static int pmStimmkarte1FalschZugeordnet = -1076;
    public final static int pmStimmkarte2FalschZugeordnet = -1077;
    public final static int pmStimmkarte3FalschZugeordnet = -1078;
    public final static int pmStimmkarte4FalschZugeordnet = -1079;
    public final static int pmWarBereitsAnwesend = -1080;

    public final static int pmZuordnungsfehlerZutrittsIdent = -1081;
    public final static int pmZuordnungsfehlerStimmkarte1 = -1082;
    public final static int pmZuordnungsfehlerStimmkarte2 = -1083;
    public final static int pmZuordnungsfehlerStimmkarte3 = -1084;
    public final static int pmZuordnungsfehlerStimmkarte4 = -1085;
    public final static int pmZuordnungsfehlerStimmkarteSecond = -1086;

    public final static int pmZutrittsIdentFehlt = -1087;
    public final static int pmStimmkarte1Fehlt = -1088;
    public final static int pmStimmkarte2Fehlt = -1089;
    public final static int pmStimmkarte3Fehlt = -1090;
    public final static int pmStimmkarte4Fehlt = -1091;
    public final static int pmStimmkarteSecondFehlt = -1092;

    public final static int pmNichtAnwesend = -1093;

    public final static int pmStimmkarte1BereitsVerwendet = -1094;
    public final static int pmStimmkarte2BereitsVerwendet = -1095;
    public final static int pmStimmkarte3BereitsVerwendet = -1096;
    public final static int pmStimmkarte4BereitsVerwendet = -1097;
    public final static int pmStimmkarteSecondBereitsVerwendet = -1098;

    public final static int pmStimmkarteNichtFuerAbstimmungAktiv = -1099;
    public final static int pmStimmkarteSecondNichtFuerAbstimmungAktiv = -1100;
    public final static int pfZutrittsIdentNebenZuHoch = -1101;

    /*Für einzelne Felder*/
    public final static int feAktianzahlUnzulaessigerWert = -5001;
    public final static int feNameUnzulaessigerWert = -5002;

    /*Technische Fehler*/
    public final static int teAppNichtMehrKompatibel = -99998;
    public final static int teVerbindungsabbruchWebService = -99999;

    /*Fehler für Aktionärs-Portal und WebServices*/
    public final static int afKennungUnbekannt = -100001; //Portal
    public final static int afPasswortFalsch = -100002; //Portal
    public final static int afKommunikationsfehler = -100003;
    public final static int afUnbekannterKommunikationsfehler = -100004;

    public final static int afAusstellungsartGastkarteFehlt = -100005;
    public final static int afKeineGueltigeEmailGastkarte = -100006;
    public final static int afNameGastkarteFehlt = -100007;
    public final static int afOrtGastkarteFehlt = -100008;

    public final static int afAusstellungsArtEKFehlt = -100009;
    public final static int afAusstellungsArtErsteEKFehlt = -100010;
    public final static int afAusstellungsArtZweiteEKFehlt = -100011;
    public final static int afKeineGueltigeEmailEK = -100012;
    public final static int afKeineGueltigeEmailErsteEK = -100013;
    public final static int afKeineGueltigeEmailZweiteEK = -100014;
    public final static int afVollmachtNameEKFehlt = -100015;
    public final static int afVollmachtNameErsteEKFehlt = -100016;
    public final static int afVollmachtNameZweiteEKFehlt = -100017;
    public final static int afVollmachtOrtEKFehlt = -100018;
    public final static int afVollmachtOrtErsteEKFehlt = -100019;
    public final static int afVollmachtOrtZweiteEKFehlt = -100020;

    public final static int afAktienregisterEintragNichtMehrVorhanden = -100021; //Portal

    public final static int afOeffentlicherSchluesselFehlt = -100022;
    public final static int afBevollmaechtigterNichtVorhanden = -100023;
    public final static int afBevollmaechtigterBereitsBevollmaechtigt = -100024;

    public final static int afKeinePersonengemeinschaft = -100025;
    public final static int afMindestens2AktienErforderlich = -100026;

    public final static int afKeineGueltigeEmailGK = -100027;
    public final static int afKeineGueltigeEmailAdresse = -100028; //Portal
    public final static int afEmailAdresseBestaetigungWeichtAb = -100029; //Portal
    public final static int afKeineGueltigeEmail2Adresse = -100030; //Portal
    public final static int afEmail2AdresseBestaetigungWeichtAb = -100031; //Portal

    public final static int afPasswortFehlt = -100032; //Portal
    public final static int afPasswortZuKurz = -100033; //Portal
    public final static int afPasswortBestaetigungWeichtAb = -100034; //Portal

    public final static int afBestaetigungNutzungsbedingungenAktionaersPortalFehlt = -100035; //Portal
    public final static int afBestaetigungNutzungsbedingungenHVPortalFehlt = -100036; //Portal

    public final static int afEMailUnbekannt = -100037;
    public final static int afFalscheKennung = -100038;

    public final static int afEmailBestaetigt = -100039; /*Email bestätigt!*/
    public final static int afEmailBestaetigungsCodeUnbekannt = -100040; //Portal
    public final static int afEmail2Bestaetigt = -100041; /*Email 2 bestätigt!*/
    public final static int afEmail2BestaetigungsCodeUnbekannt = -100042; //Portal

    public final static int afEKAktionaerEmailVerschickt = -100043; /*Eintrittskarte Aktionär per Email verschickt*/
    public final static int afEK2AktionaerEmailVerschickt = -100044; /*Zweite Eintrittskarte Aktionär per Email verschickt*/
    public final static int afEKGastEmailVerschickt = -100045; /*Eintrittskarte Gast per Email verschickt*/

    public final static int afGastNameFehlt = -100046; /*Name ist zwingend auszufüllen!*/
    public final static int afGastVornameFehlt = -100047; /*Vorname ist zwingend auszufüllen!*/
    public final static int afGastOrtFehlt = -100048; /*Ort ist zwingend auszufüllen*/

    public final static int afOeffentlicheIDGesendet = -100049; /*Öffentliche ID gesendet*/
    public final static int afPWVergessenMailGesendet = -100050; /*PW vergessen Link gesendet*/

    public final static int afAndererUserAktiv = -100051; /*Portal Mittlerweile anderer User aktiv gewesen*/

    public final static int afEmailGKBestaetigungFalsch = -100052;
    public final static int afEmailEKBestaetigungFalsch = -100053;
    public final static int afEmailErsteEKBestaetigungFalsch = -100054;
    public final static int afEmailZweiteEKBestaetigungFalsch = -100055;

    public final static int afPWVergessenLinkUngueltig = -100056;

    public final static int afHVNichtVorhanden = -100057;
    public final static int afHVMitDieserKennungNichtZulaessig = -100058;
    public final static int afKennungGesperrt = -100059;
    public final static int afNeuesPasswortErforderlich = -100060;
    public final static int afPasswortNichtSicher = -100061;
    public final static int afPasswortBereitsVerwendet = -100062;
    public final static int afPasswortEMailNichtZulaessig = -100063; //Portal

    public final static int afAbstimmgruppe1ZuViel = -100064;
    public final static int afAbstimmgruppe2ZuViel = -100065;
    public final static int afAbstimmgruppe3ZuViel = -100066;
    public final static int afAbstimmgruppe4ZuViel = -100067;
    public final static int afAbstimmgruppe5ZuViel = -100068;
    public final static int afAbstimmgruppe6ZuViel = -100069;
    public final static int afAbstimmgruppe7ZuViel = -100070;
    public final static int afAbstimmgruppe8ZuViel = -100071;
    public final static int afAbstimmgruppe9ZuViel = -100072;
    public final static int afAbstimmgruppe10ZuViel = -100073;

    public final static int afFalscheKennungOderPasswort = -100074;
    public final static int afStandardMandantNichtGepflegt = -100075;

    public final static int afBestaetigungBerechtigungFehlt = -100076; //Portal
    public final static int afBerechtigungFuerAktionaersportalFehlt = -100077; //Portal
    public final static int afGeburtdatumUnzulaessigesFormat = -100078;
    public final static int afGeburtdatumFalsch = -100079;
    public final static int afKeineEmailAdresseHinterlegt = -100080;
    public final static int afStrasseHausnummerPflichtfeld = -100081;
    public final static int afPLZPflichtfeld = -100082;
    public final static int afOrtPflichtfeld = -100083;
    public final static int afKeineAenderungenEingegeben = -100084;
    public final static int afFalscheAnzahlVeranstaltungenAusgewaehlt = -100085;
    public final static int afNurZahlenZulaessig = -100086;
    public final static int afMaxPersonenUeberschritten = -100087;
    public final static int afNullPersonenAngemeldet = -100088;
    public final static int afAusgebucht = -100089;
    public final static int afBitteAnOderAbmelden = -100090;
    public final static int afPasswortVergessenUeberMailUnzulaessig = -100091;
    public final static int afEMailWurdeErneutVerschickt = -100092; //Portal
    public final static int afVirtuellerTeilnehmerNichtAusgewaehlt = -100093;
    public final static int afFragezuTopZuLang = -100094;
    public final static int afFrageTextZuLang = -100095;
    public final static int afMitteilungTextZuLang = -100096;

    public final static int afBeiratswahlSchonAbgegeben = -100097;

    public final static int afFunktionNichtAuswaehlbar = -100098; //Portal

    /**Achtung: nicht als Fehlermeldung im Portal vorhanden - wird durch
     * den Sperrtext in den Parametern ersetzt!
     */
    public final static int afPortalGesperrt = -100099; //Portal

    /**Login aktuell nicht möglich, da gerade anderer Login-Vorgang läuft. In 5 Minuten nochmal probieren*/
    public final static int afUserLoginTemporaerGesperrt = -100100; //Portal

    public final static int afUserAusgeloggtWgParallelLogin = -100101; //Portal

    public final static int afUpdatePraesentNichtMoeglich = -100102; //Portal
    public final static int afDerzeitKeineAbstimmungEroeffnet = -100103; //Portal

    /**Benutzer hat auf "Zurück" geklickt*/
    public final static int afStimmabgabeVomBenutzerAbgebrochen = -100104; //Portal

    /**Stimmabgabe war nicht mehr möglich, weil Stimmabgabe mittlerweile geschlossen ist*/
    public final static int afStimmabgabeVomSystemAbgebrochenBeendet = -100105; //Portal

    /**Stimmabgabe war nicht mehr möglich, weil mittlerweile neue Abstimmung aktiv*/
    public final static int afStimmabgabeVomSystemAbgebrochenNichtAktuell = -100106; //Portal

    /**Statusmeldung, kein Fehler*/
    public final static int afStimmabgabeDurchgefuehrt = -100107; //Portal

    public final static int afPasswortZuEinfach = -100108; //Portal
    public final static int afEMailBestaetigungFalsch = -100109; //Portal
    public final static int afLoginNameFalsch = -100110; //Portal
    public final static int afLoginDatumFalsch = -100111; //Portal
    public final static int afPasswortVergessenFuerSonstigeNichtMoeglich = -100112; //Portal
    public final static int afBestandFuerZugangAuswaehlen = -100113; //Portal

    public final static int afWortmeldungTopZuLang = -100114; //Portal
    public final static int afWortmeldungTextZuLang = -100115; //Portal
    public final static int afWortmeldungWortmelderZuLang = -100116; //Portal
    public final static int afWortmeldungTelNrZuLang = -100117; //Portal

    public final static int afBestaetigungNutzungsbedingungenOnlineTeilnahmeFehlt = -100118; //Portal
    public final static int afWortmeldungWortmelderFehlt = -100119; //Portal
    public final static int afWortmeldungTelefonNrFehlt = -100120; //Portal
    public final static int afNichtStimmberechtigt = -100121; //Portal
    public final static int afBestaetigungPersonOnlineTeilnahmeFehlt = -100122; //Portal
    public final static int afBesitzReloadDurchfuehren = -100123; //Intern
    public final static int afFunktionDerzeitNichtAktiv = -100124; //Portal
    public final static int afBestandFuerStimmabgabeAuswaehlen = -100125; //Portal

    public final static int afPasswortEnthaeltNichtUnterschiedlicheZeichen = - 100126; //Portal
    public final static int afBeiUebernahmeBestandZumWiderrufenAuswaehlen = - 100127; //Portal
    public final static int afBestandNichtAngemeldet = - 100128; 
    public final static int afKIAVSRVBriefwahlBereitsVorhanden = - 100129; 

    public final static int afFrageTextFehlt=-100130;
    public final static int perBestaetigungHinweisWeitere1Fehlt = -100131; //PermPortal
    public final static int perBestaetigungHinweisWeitere2Fehlt = -100132; //PermPortal

    public final static int perAenderungenWerdenNichtUebernommen = -100133; //PermPortal
    public final static int perKontaktformularUebermittelt = -100134; //PermPortal
    public final static int perKontaktformularTextFehlt = -100135; //PermPortal
    public final static int perAktionaersnummerNichtInAktienregisterEnthalten=-100136; //PermPortal
    public final static int perAktionaersnummerNichtInPortalRegistriert=-100137; //PermPortal
    public final static int perPublikationenUebermittelt = -100138; //PermPortal
    public final static int perEMailZwingend = -100139; //PermPortal
    public final static int perPLZOrtFalsch = -100140; //PermPortal
    public final static int perIBANFalsch = -100141; //PermPortal
    public final static int perEmailIstNichtRichtigesFormat=-100142; //PermPortal
    public final static int perEmailBestätigungscodeIstFalsch=-100144; //PermPortal
    public final static int afEmailBestaetigungErforderlich=-100145; //Portal
    public final static int afEmail2BestaetigungErforderlich=-100146; //Portal
    public final static int perPLZFalsch=-100147; //PermPortal
    public final static int perGeburtsdatumFalsch=-100148; //PermPortal
    public final static int perSteuerIdFalsch=-100149; //PermPortal
    public final static int perBicFalsch=-100150; //PermPortal
    public final static int perBanknameFalsch=-100151; //PermPortal
    public final static int perBankAktionaersnameFalsch=-100152; //PermPortal
    public final static int perOrtFalsch=-100153; //PermPortal
    public final static int perKontaktformularThemaFehlt=-100154; //PermPortal
    public final static int perRemoteAktienregisterNichtVerfuegbar=-100155; //PermPortal
    
    public final static int afWortmeldungTopAuswahlFehlt=-100156; //Portal
    public final static int afWortmeldungTopFehlt=-100157; //Portal
 
    public final static int afDateiUploadDateiLeer=-100158; //Portal
    public final static int afDateiUpLoadVideodateiZuGross=-100159; //Portal
    public final static int afDateiUpLoadTextdateiZuGross=-100160; //Portal
    public final static int afDateiUploadFalschesDateiformat=-100161; //Portal
    public final static int afDateiUploadKeineDateiAusgewaehlt=-100162; //Portal
    public final static int afMitteilungHinweisBestaetigen=-100163; //Portal

    public final static int afBestaetigungsEMailVerschickt=-100164; //Portal
    public final static int afNurEineDateiErlaubt=-100165; //Portal
    public final static int afBrowserUnterstuetztUploadNicht=-100166; //Portal

    public final static int afWortmeldungTextFehlt=-100167; //Portal
    public final static int afWiderspruchTextFehlt=-100168; //Portal
    public final static int afAntragTextFehlt=-100169; //Portal
    public final static int afSonstigeMitteilungTextFehlt=-100170; //Portal
    public final static int afBotschaftenEinreichenTextFehlt=-100171; //Portal

    public final static int afMeldungFuerAbstimmungWgVorOrtPraesenzGesperrt=-100172; //Portal

    public final static int pmMitDieserGattungKeinZugangMoeglich=-100173; 
    public final static int pmPWImQRCodeFalsch=-100174; 
    public final static int afVertreterVollstaendigEingeben=-100175; //Portal
    public final static int afGastkarteZweitePersonVollstaendigEingeben=-100176; //Portal

    public final static int pmMeldungNochNichtGeprueft = -100177;

    public final static int afWortmeldungMailZuLang = -100178; //Portal
    public final static int afWortmeldungMailFehlt = -100179; //Portal
 
    public final static int afVersammlungsraumNichtGeoeffnetBeiZugang = -100180; //Portal
    public final static int afVersammlungsraumNichtGeoeffnetBeiAbgang = -100181; //Portal
    public final static int afZurueckziehenDerzeitNichtMoeglich = -100182; //Portal

    public final static int afInhaltsHinweiseZwingend=-100183; //Portal

    public final static int afJnWeisungBestaetigungFehlt=-100184; //Portal
    
    
    public final static int nzVollmachtPersonOderVerband=-100185; //Portal, nur für ku302_303
    public final static int nzVollmachtVertretungsartEingeben=-100186; //Portal, nur für ku302_303
    
    public final static int afLoginVerzoegertAnfang=-100187; //Portal
    public final static int afLoginVerzoegertEnde=-100188; //Portal
    
    public final static int afFalschesCaptcha=-100189; //Portal
    
    public final static int afKennungBereitsZugeordnet=-100190; //Portal
    public final static int afEigeneKennungNichtZuordenbar=-100191; //Portal

    public final static int afHinweisBeiVollmachtBestaetigt=-100192; //Portal

    public final static int afGastAnredeZwingend=-100193; //Portal
    public final static int afGastTitelZwingend=-100194; //Portal
    public final static int afGastAdelstitelZwingend=-100195; //Portal
    public final static int afGastNameZwingend=-100196; //Portal
    public final static int afGastVornameZwingend=-100197; //Portal
    public final static int afGastZuHaendenZwingend=-100198; //Portal
    public final static int afGastZusatz1Zwingend=-100199; //Portal
    public final static int afGastZusatz2Zwingend=-100200; //Portal
    public final static int afGastStrasseZwingend=-100201; //Portal
    public final static int afGastLandZwingend=-100202; //Portal
    public final static int afGastPLZZwingend=-100203; //Portal
    public final static int afGastOrtZwingend=-100204; //Portal
    public final static int afGastMailadresseZwingend=-100205; //Portal
    public final static int afGastMailAdresseFalsch=-100206; //Portal
    public final static int afGastKommunikationsspracheZwingend=-100207; //Portal

    public final static int afGastWurdeStorniert=-100208; //Portal
    public final static int afGastKartePerMailVerschickt=-100209; //Portal
    
    public final static int afVollmachtAllePflichtfelderEingeben=-100210; //Portal
    public final static int afVollmachtAktionaersnummerUnzulaessig=-100211; //Portal
    
    public final static int afVollmachtNichtErteilt=-100212; //Portal
    public final static int afGesetzlVollmachtNichtGespeichert=-100213; //Portal
    
    /*Wird dazu verwendet, um einen freien Text einzutragen*/
    public final static int generalFreierText=-999999;
    /*Neu Anfang*/

    public final static int pmNummernformKontrollzahlFalsch = -1102;
    public final static int pmNummernformStimmartFalsch = -1103;
    public final static int pmAppVersionFalsch = -1104;
    public final static int pmZutrittsIdentBereitsVonAndererPersonVerwendet = -1105;
    public final static int pmSammelkartenBuchungAufDiesemGeraetNichtMoeglich = -1106;
    public final static int pmNullBestandBuchenNichtMoeglich = -1107;
    public final static int pmInSammelkarteEnthalten = -1108;
    public final static int pmServiceschalter = -1109;
    public final static int pmNummernformPruefzifferFalsch = -1110;
    public final static int pmPersonNichtVorhanden = -1111;
    public final static int pmNichtPraesent = -1112;
    public final static int pmDoppeltNunUngueltig = -1113;
    public final static int pmGattungNichtStimmberechtigt = -1114;
    public final static int pmEintrittskarteVerweistAufGast = -1115;
    public final static int pmStimmkarteVerweistAufGast = -1116;
    /*Neu Ende*/

    public final static int pfErstregistrierungMittlerweileAbgeschlossen = -1117;
    public final static int afHinterlegteEmailAdresseEingeben = -1118;
    public final static int afHinterlegteStrasseOrtEingeben = -1119;
    public final static int afEVersandErfordertPasswort = -1120; //Portal

    public final static int pmNummernformStimmkartenSubNummernkreisUngueltig = -1121;
    public final static int pmNummernformHVIdentNrUngueltig = -1122;

    public final static int pmSammelkarteFalscheGattung = -1123;
    public final static int pmSammelkarteNichtPraesentAberAktionaer = -1124;

    public final static int pmAbstimmungNichtLoeschbarStimmkarteZugeordnet = -1125;
    public final static int pmAbstimmungNichtLoeschbarAbstimmvorgangZugeordnet = -1126;
    public final static int pmAbstimmungVorgangNichtLoeschbarAbstimmvorgangZugeordnet = -1127;
    public final static int pmElekStimmkarteNichtLoeschbarAbstimmvorgangZugeordnet = -1128;

    public final static int afEVersandErfordertEMail = -1129; //Portal
    public final static int afPasswortErfordertEMail = -1130; //Portal
    public final static int afEBestaetigungErfordertEMail = -1131; //Portal

    /*Verwaltungs-Fehler, Systemfehler*/
    public final static int fTableInfoNichtLesbar = -200001;
    public final static int fTablesMuessenUpgedatetWerden = -200002;
    public final static int sysGeraeteSetNichtVorhanden = -200003;
    public final static int sysGeraeteKlasseSetZuordnungNichtVorhanden = -200004;
    public final static int sysGeraeteKlasseNichtVorhanden = -200005;
    public final static int sysPortalSpracheNichtVorhanden = -200006;
    public final static int sysPortalSpracheNichtAktiv = -200007;

    public final static int rpJobCantBeInitialized = -200008;
    public final static int rpLanguageFileNotFound = -200009;
    public final static int rpErrorWhilePrinting = -200010;
    public final static int rpAbbruchBeiAuswahl = -200011;

    /*Verarbeitungslauf-Fehler*/
    public final static int vlFalscheErklaerungImLauf = -300001;
    public final static int vlNichtEindeutig = -300002; //Fehler oder Warnung
    public final static int vlEkMitVollmachtNichtMoeglich = -300003;

    /*Meldungen, keine Fehler!*/
    public final static int melEk1PerMailVerschickt = -900001;
    public final static int melEk2PerMailVerschickt = -900002;

    public final static int gast = 1;
    public final static int meldungenmeldungen = 2;
    public final static int vipKZ = 3;
    public final static int meldungenVipAusgeblendet = 4;

    public static String getFehlertext(int fehler, int art) {
        String artText1 = "";
        String artText2 = "";

        switch (art) {
        case 0:
            artText1 = "";
            artText2 = "";
            break;
        case 1:
            artText1 = "Gastkarten-Nr,";
            artText2 = "Gastkarte";
            break;
        case 2:
            artText1 = "Zuordnung";
            artText2 = "Zuordnung";
            break;
        case 3:
            artText1 = "VIP-Kürzel";
            artText2 = "VIP-Kennzeichen";
            break;
        case 4:
            artText1 = "Ausblendung";
            artText2 = "Ausblendung";
            break;
        }

        switch (fehler) {
        case 0:
            return "Unbekannter Fehler!";
        case -1:
            return artText1 + " bereits vorhanden!";
        case -2:
            return "Format " + artText1 + " unzulässig!";
        case -3:
            return artText1 + " nicht im zulässigen Nummernkreis!";
        case -4:
            return "Automatische Vergabe " + artText1 + " nicht möglich!";
        case -5:
            return artText1 + " nicht vorhanden!";
        case -6:
            return artText2
                    + " wurde von anderem Benutzer verändert und kann nicht weiter verarbeitet oder gespeichert werden!";
        case -7:
            return "Zuordnung zu sich selbst nicht zulässig!";
        case -8:
            return "Bitte Daten vollständig eingeben!";
        case -9:
            return "Bitte " + artText1 + " eingeben!";
        case -10:
            return "Bitte " + artText2 + " auswählen!";
        case -11:
            return "Löschen nicht möglich - noch zugeordnete Sätze!";
        case -12:
            return "Letzter Satz erreicht!";
        case -13:
            return "Erster Satz erreicht!";
        case -14:
            return "Nicht möglich!";
        case -15:
            return "pfNichtEindeutig";

        case -1001:
            return "Meldung ist storniert!";
        case -1002:
            return "Zugangskarte ist storniert!";
        case -1003:
            return "Bereits anwesend!";
        case -1004:
            return "Noch nicht anwesend!";
        case -1005:
            return "pmMeldungsIdentNichtVorhanden";
        case -1006:
            return "pmZutrittsIdentNichtVorhanden";
        case -1007:
            return "pmZutrittsIdentNichtEindeutig";
        case -1008:
            return "pmZutrittsIdentGesperrt";
        case -1009:
            return "pmStimmkarteNichtVorhanden";
        case -1010:
            return "pmStimmkarteGesperrt";
        case -1011:
            return "pmStimmkarteSecondNichtVorhanden";
        case -1012:
            return "pmStimmkarteSecondGesperrt";
        case -1013:
            return "pmMeldungNichHinreichendSpezifiziert";
        case -1014:
            return "pmMeldungAktionaerIstKeinAktionaer";
        case -1015:
            return "pmMeldungGastIstKeinGast";
        case -1016:
            return "pmZutrittsIdentBereitsVorhanden";
        case -1017:
            return "pmZutrittsIdentNichtGesperrt";
        case -1018:
            return "pmKeinGastGefunden";
        case -1019:
            return "pmStimmkarteBereitsVorhanden";
        case -1020:
            return "pmStimmkarteNichtGesperrt";
        case -1021:
            return "pmStimmkarteSecondBereitsVorhanden";
        case -1022:
            return "pmStimmkarteSecondNichtGesperrt";
        case -1023:
            return "pmVollmachtNurFuerAktionaerMoeglich";
        case -1024:
            return "pmVollmachtGeberHatKeineVollmacht";
        case -1025:
            return "pmVollmachtGeberVollmachtIstWiderrufen";
        case -1026:
            return "pmBevollmaechtigterNichtVorhanden";
        case -1027:
            return "pmVollmachtBereitsWiderrufen";
        case -1028:
            return "pmAktionaerDarfNurDirektenNachfolgerWiderrufen";
        case -1029:
            return "pmWeitereVollmachtFuerBevollmaechtigtemVorhanden";
        case -1030:
            return "pmBevollmaechtigterDarfNurDirektenNachfolgerWiderrufen";
        case -1031:
            return "pmEigenerVollmachtsgeberKannNichtWiderrufenWerden";
        case -1032:
            return "pmVollmachtNichtVonVollmachtgeberAbhaenig";
        case -1033:
            return "pmSammelkarteUnzulaessig";
        case -1034:
            return "pmWillenserklaerungZuDieserSammelkarteUnzulaessig";
        case -1035:
            return "pmWeisungsartZuDieserSammelkarteUnzulaessig";
        case -1036:
            return "pmKeinEigenerAbstimmungsvorschlagVorhanden";
        case -1037:
            return "pmFremderAbstimmungsvorschlagNichtVorhanden";
        case -1038:
            return "pmAlteWeisungNichtReferenziert";
        case -1039:
            return "pmAlteWeisungBereitsVeraendert";
        case -1040:
            return "pmZuordnungZuSammelkarteNichtVorhanden";
        case -1041:
            return "pmZuordnungSammelkarteNichtMehrGueltig";
        case -1042:
            return "pmZuordnungSammelkarteNichtVonDieserPersonStornierbar";
        case -1043:
            return "pmGesamterBestandBereitsAngemeldet";

        case -1044:
            return "pmNichtAngemeldeterBestandZuKlein";
        case -1045:
            return "pmNichtGenuegendAktienFuerSplit";

        case -1055:
            return "pmKeineAnmeldungVorhanden";
        case -1057:
            return "pmWillenserklaerungenVorhanden";

        case -1058:
            return "pmKeinAbstimmvorschlagVorhanden";
        case -1059:
            return "pmFixAnmeldungVorhanden";

        case -1060:
            return "pmNummernformUngueltig";
        case -1061:
            return "pmNummernformAktionsnummerUngueltig";
        case -1062:
            return "pmNummernformMandantUngueltig";
        case -1063:
            return "pmKeineAnmeldungenVorhanden";
        case -1064:
            return "pmFixAnmeldungenVorhanden";
        case -1065:
            return "pmMehrAls2AnmeldungenVorhanden";
        case -1066:
            return "pmStimmkarteNichtAnwesend";
        case -1067:
            return "pmStimmkarteSecondNichtAnwesend";
        case -1068:
            return "pmKannNichtPersoenlichPraesentWegenSammelkarte";

        case -1069:
            return "pmNummernformZutrittsIdentUngueltig";
        case -1070:
            return "pmNummernformStimmkarte1Ungueltig";
        case -1071:
            return "pmNummernformStimmkarte2Ungueltig";
        case -1072:
            return "pmNummernformStimmkarte3Ungueltig";
        case -1073:
            return "pmNummernformStimmkarte4Ungueltig";
        case -1074:
            return "pmNummernformStimmkarteSecondUngueltig";
        case -1075:
            return "pmZuordnungFehlerhaft";
        case -1076:
            return "pmStimmkarte1FalschZugeordnet";
        case -1077:
            return "pmStimmkarte2FalschZugeordnet";
        case -1078:
            return "pmStimmkarte3FalschZugeordnet";
        case -1079:
            return "pmStimmkarte4FalschZugeordnet";
        case -1080:
            return "pmWarBereitsAnwesend";

        case -1081:
            return "pmZuordnungsfehlerZutrittsIdent";
        case -1082:
            return "pmZuordnungsfehlerStimmkarte1";
        case -1083:
            return "pmZuordnungsfehlerStimmkarte2";
        case -1084:
            return "pmZuordnungsfehlerStimmkarte3";
        case -1085:
            return "pmZuordnungsfehlerStimmkarte4";
        case -1086:
            return "pmZuordnungsfehlerStimmkarteSecond";

        case -1087:
            return "pmZutrittsIdentFehlt";
        case -1088:
            return "pmStimmkarte1Fehlt";
        case -1089:
            return "pmStimmkarte2Fehlt";
        case -1090:
            return "pmStimmkarte3Fehlt";
        case -1091:
            return "pmStimmkarte4Fehlt";
        case -1092:
            return "pmStimmkarteSecondFehlt";

        case -1093:
            return "pmNichtAnwesend";
        case -1094:
            return "pmStimmkarte1BereitsVerwendet";
        case -1095:
            return "pmStimmkarte2BereitsVerwendet";
        case -1096:
            return "pmStimmkarte3BereitsVerwendet";
        case -1097:
            return "pmStimmkarte4BereitsVerwendet";
        case -1098:
            return "pmStimmkarteSecondBereitsVerwendet";

        case -1099:
            return "pmStimmkarteNichtFuerAbstimmungAktiv";
        case -1100:
            return "pmStimmkarteSecondNichtFuerAbstimmungAktiv";
        case -1101:
            return "pfZutrittsIdentNebenZuHoch";

        case -1102:
            return "pmNummernformKontrollzahlFalsch";
        case -1103:
            return "pmNummernformStimmartFalsch";
        case -1104:
            return "pmAppVersionFalsch";
        case -1105:
            return "pmZutrittsIdentBereitsVonAndererPersonVerwendet";
        case -1106:
            return "pmSammelkartenBuchungAufDiesemGeraetNichtMoeglich";
        case -1107:
            return "pmNullBestandBuchenNichtMoeglich";
        case -1108:
            return "pmInSammelkarteEnthalten";
        case -1109:
            return "pmServiceschalter";
        case -1110:
            return "pmNummernformPruefzifferFalsch";
        case -1111:
            return "pmPersonNichtVorhanden";
        case -1112:
            return "pmNichtPraesent";
        case -1113:
            return "pmDoppeltNunUngueltig";
        case -1114:
            return "pmGattungNichtStimmberechtigt";
        case -1115:
            return "pmEintrittskarteVerweistAufGast";
        case -1116:
            return "pmStimmkarteVerweistAufGast";
        case -1117:
            return "pfErstregistrierungMittlerweileAbgeschlossen";
        case -1118:
            return "afHinterlegteEmailAdresseEingeben";
        case -1119:
            return "afHinterlegteStrasseOrtEingeben";
        case -1120:
            return "afEVersandErfordertPasswort";
        case -1121:
            return "pmNummernformStimmkartenSubNummernkreisUngueltig";
        case -1122:
            return "pmNummernformHVIdentNrUngueltig";
        case -1123:
            return "pmSammelkarteFalscheGattung";
        case -1124:
            return "pmSammelkarteNichtPraesentAberAktionaer";
        case -1125:
            return "Ident ist (mindestens) einer Stimmkarte zugeordnet!";
        case -1126:
            return "Ident ist (mindestens) einem Abstimmungsvorgang zugeordnet!";
        case -1127:
            return "Diesem Abstimmvorgang ist (mindestens) eine Abstimmung zugeordnet!";
        case -1128:
            return "Dieser elektronischen Stimmkarte ist (mindestens) eine Abstimmung zugeordnet!";

        case -1129:
            return "Registrierung für E-Versand erfordert Eingabe einer E-Mail-Adresse!";
        case -1130:
            return "Vergabe eines eigenes Passwortes erfordert Eingabe einer E-Mail-Adresse!";
        case -1131:
            return "Aktivierung Bestätigungen per E-Mail erfordert Eingabe einer E-Mail-Adresse!";

        case -5001:
            return "Bitte geben Sie eine gültige Aktienzahl ein!";
        case -5002:
            return "Bitte geben Sie einen Namen ein!";

        case -99999:
            return "Verbindungsabbruch Web Service!";

        case -100001:
            return "Kennung unbekannt!";
        case -100002:
            return "Falsches Passwort!";
        case -100003:
            return "Kommunikationsfehler!";
        case -100004:
            return "Unbekannter Kommunikationsfehler!";

        case -100005:
            return "Bitte wählen Sie eine Ausstellungsart für die Gastkarte aus!";
        case -100006:
            return "Bitte geben Sie eine gültige Email-Adresse für die Gastkarte ein!";
        case -100007:
            return "Bitte geben Sie den Namen des Gastes ein!";
        case -100008:
            return "Bitte geben Sie den Ort des Gastes ein!";

        case -100009:
            return "Bitte wählen Sie eine Ausstellungsart für die Eintrittskarte aus!";
        case -100010:
            return "Bitte wählen Sie eine Ausstellungsart für die erste Eintrittskarte aus!";
        case -100011:
            return "Bitte wählen Sie eine Ausstellungsart für die zweite Eintrittskarte aus!";
        case -100012:
            return "Bitte geben Sie eine gültige Email-Adresse für die Eintrittskarte ein!";
        case -100013:
            return "Bitte geben Sie eine gültige Email-Adresse für die erste Eintrittskarte ein!";
        case -100014:
            return "Bitte geben Sie eine gültige Email-Adresse für die zweite Eintrittskarte ein!";
        case -100015:
            return "Bitte geben Sie einen Namen für den Bevollmächtigten ein!";
        case -100016:
            return "Bitte geben Sie einen Namen für den ersten Bevollmächtigten ein!";
        case -100017:
            return "Bitte geben Sie einen Namen für den zweiten Bevollmächtigten ein!";
        case -100018:
            return "Bitte geben Sie einen Ort für den Bevollmächtigten ein!";
        case -100019:
            return "Bitte geben Sie einen Ort für den ersten Bevollmächtigten ein!";
        case -100020:
            return "Bitte geben Sie einen Ort für den zweiten Bevollmächtigten ein!";

        case -100021:
            return "Aktienregistereintrag nicht mehr vorhanden - bitte wenden Sie sich an die Hotline!";

        case -100022:
            return "Bitte öffentlichen Schlüssel des Bevollmächtigten eingeben!";
        case -100023:
            return "Bevollmächtigter nicht gefunden!";
        case -100024:
            return "Bevollmächtigter hat bereits eine Vollmacht von Ihnen!";

        case -100025:
            return "Nur für Personengemeinschaft möglich!";
        case -100026:
            return "Mindestens 2 Aktien erforderlich!";

        case -100027:
            return "Bitte geben Sie eine gültige Email-Adresse für die Gastkarte ein!";
        case -100028:
            return "Bitte geben Sie eine gültige E-Mail-Adresse ein!";
        case -100029:
            return "Die Bestätigungs-EMail-Adresse weicht ab!";
        case -100030:
            return "Bitte geben Sie eine gültige 2. E-Mail-Adresse ein!";
        case -100031:
            return "Die 2. Bestätigungs-EMail-Adresse weicht ab!";

        case -100032:
            return "Passwort Fehlt!";
        case -100033:
            return "Bitte geben Sie ein Passwort mit mindestens 6 Zeichen ein!";
        case -100034:
            return "Das Bestätigungspasswort muß mit dem Passwort übereinstimmen!";

        case -100035:
            return "Bitte bestätigen Sie die Nutzungsbedingungen des Aktionärsportals!";
        case -100036:
            return "Bitte bestätigen Sie die Nutzungshinweise des HV-Portals!";

        case -100037:
            return "E-Mail-Adresse unbekannt!";
        case -100038:
            return "Falsche Kennung!";

        case -100039:
            return "Email bestätigt";
        case -100040:
            return "Email-Bestätigungs-Code unbekannt!";
        case -100041:
            return "Email 2 bestätigt";
        case -100042:
            return "Email 2-Bestätigungs-Code unbekannt!";
        case -100043:
            return "Eintrittskarte Aktionär per Email verschickt";
        case -100044:
            return "zweite Eintrittskarte Aktionär per Email verschickt";
        case -100045:
            return "Eintrittskarte Gast per Email verschickt";
        case -100046:
            return "Name ist zwingend auszufüllen!";
        case -100047:
            return "Vorname ist zwingend auszufüllen!";
        case -100048:
            return "Ort ist zwingend auszufüllen!";
        case -100049:
            return "Öffentliche ID gesendet";
        case -100050:
            return "PW vergessen Link gesendet";
        case -100051:
            return "afAndererUserAktiv";
        case -100052:
            return "afEmailGKBestaetigungFalsch";
        case -100053:
            return "afEmailEKBestaetigungFalsch";
        case -100054:
            return "afEmailErsteEKBestaetigungFalsch";
        case -100055:
            return "afEmailZweiteEKBestaetigungFalsch";
        case -100056:
            return "afPWVergessenLinkUngueltig";
        case -100057:
            return "afHVNichtVorhanden";
        case -100058:
            return "afHVMitDieserKennungNichtZulaessig";
        case -100059:
            return "Kennung ist gesperrt! Bitte wenden Sie sich an Ihren Administrator";
        case -100060:
            return "afNeuesPasswortErforderlich";
        case -100061:
            return "afPasswortNichtSicher";
        case -100062:
            return "afPasswortBereitsVerwendet";
        case -100063:
            return "afPasswortEMailNichtZulaessig";

        case -100064:
            return "afAbstimmgruppe1ZuViel";
        case -100065:
            return "afAbstimmgruppe2ZuViel";
        case -100066:
            return "afAbstimmgruppe3ZuViel";
        case -100067:
            return "afAbstimmgruppe4ZuViel";
        case -100068:
            return "afAbstimmgruppe5ZuViel";
        case -100069:
            return "afAbstimmgruppe6ZuViel";
        case -100070:
            return "afAbstimmgruppe7ZuViel";
        case -100071:
            return "afAbstimmgruppe8ZuViel";
        case -100072:
            return "afAbstimmgruppe9ZuViel";
        case -100073:
            return "afAbstimmgruppe10ZuViel";
        case -100074:
            return "Kennung oder Passwort falsch";
        case -100075:
            return "Standard-HV nicht korrekt eingetragen - bitte wenden Sie sich an Ihren Administrator";
        case -100076:
            return "Bitte bestätigen Sie, dass Sie zur Abgabe der Willenserklärung berechtigt sind";
        case -100077:
            return "Sie haben leider keine Berechtigung zur Nutzung des Internetservice";
        case -100078:
            return "Bitte geben Sie das im Mitgliederverzeichnis hinterlegte Geburtsdatum in der Form MM.TT.JJJJ ein";
        case -100079:
            return "Das eingegebene Geburtsdatum stimmt nicht mit dem im Mitgliederverzeichnis hinterlegtem überein";
        case -100080:
            return "Sie haben keine E-Mail-Adresse hinterlegt - bitte wählen Sie den schriftlichen Versand des Passwortes";
        case -100081:
            return "Straße ist ein Pflichtfeld";
        case -100082:
            return "PLZ ist ein Pflichtfeld";
        case -100083:
            return "Ort ist ein Pflichtfeld";
        case -100084:
            return "Sie haben keine Daten geändert";
        case -100085:
            return "Sie müssen genau eine Veranstaltung auswählen";
        case -100086:
            return "Personenanzahl: nur Zahlen zulässig";
        case -100087:
            return "Maximal 5 Personen zulässig";
        case -100088:
            return "Mindestens 1 Person anmelden";
        case -100089:
            return "Vielen Dank für Ihr Interesse an der Teilnahme dieser Dialogveranstaltung. " + 
                    "Leider ist diese Veranstaltung bereits ausgebucht. " + 
                    "Bitte wählen Sie alternativ eine andere Veranstaltung aus " + 
                    "oder versuchen Sie es zu einem späteren Zeitpunkt erneut, da zwischenzeitlich Absagen erfolgen können.";
                    
        case -100090:
            return "Bitte markieren Sie, ob Sie sich an oder abmelden";
        case -100091:
            return "Aufgrund Mitgliederstatus Passwort per Mail nicht zulässig";
        case -100092:
            return "E-Mail wurde verschickt";
        case -100093:
            return "Bitte kennzeichnen Sie, wer Sie sind";
        case -100094:
            return "Text zu TOP zu lang";
        case -100095:
            return "Fragentext zu lang";
        case -100096:
            return "Text der Mitteilung zu lang";

        case -100097:
            return "Beiratswahl bereits abgegeben";
        case -100098:
            return "Funktion nicht auswählbar";
        case -100099:
            return "Portal gesperrt";
        case -100100:
            return "UserLogin gerade nicht möglich - bitte in einigen Minuten nochmal probieren";
        case -100101:
            return "User ausgeloggt wg. parallelem Login";
        case -100102:
            return "Präsenzveränderung derzeit nicht möglich - bitte später nochmal versuchen";
        case -100103:
            return "Derzeit ist keine Abstimmung eröffnet";
        case -100104:
            return "Sie haben keine Stimmabgabe durchgeführt";
        case -100105:
            return "Abstimmung wurde geschlossen - keine Stimmabgabe mehr möglich";
        case -100106:
            return "Abstimmung verändert - bitte Stimmabgabe neu starten";
        case -100107:
            return "Stimmabgabe durchgeführt";

        case -100108:
            return "Das Passwort muß mindestens ein Sonderzeichen, eine Zahl, einen Groß- und einen Kleinbuchstaben enthalten";
        case -100109:
            return "Bitte geben Sie eine korrekte E-Mail-Adresse an";
        case -100110:
            return "Bitte geben Sie korrekte Login-Daten ein";
        case -100111:
            return "Bitte geben Sie korrekte Login-Daten ein";
        case -100112:
            return "Passwort-Vergessen für sonstige Kennungen nicht möglich";
        case -100113:
            return "Bitte wählen Sie mindestens einen Bestand für den Zugang aus";

        case -100114:
            return "Eingabe zu Top ist zu lang (maximal 100 Zeichen)";
        case -100115:
            return "Text ist zu lang";
        case -100116:
            return "Name zu lang (maximal 100 Zeichen)";
        case -100117:
            return "Telefon-Nr zu lang (maximal 100 Zeichen)";
        case -100118:
            return "Bitte bestätigen Sie die Nutzungsbedingungen für die virtuelle Teilnahme";
        case -100119:
            return "Bitte geben Sie Ihren Namen ein";
        case -100120:
            return "Bitte geben Sie Ihre Telefonnummer ein";
        case -100121:
            return "Sie sind leider nicht stimmberechtigt";
        case -100122:
            return "Bitte bestätigen Sie Ihren Namen";
        case -100123:
            return "afBesitzReloadDurchfuehren";
        case -100124:
            return "Diese Funktion ist derzeit nicht aktiv";
        case -100125:
            return "Bitte wählen Sie mindestens einen Bestand für die Stimmabgabe aus";
        case -100126:
            return "Das Passwort muß mindestens eine Zahl, einen Kleinbuchstaben und einen Großbuchstaben enthalten";
        case -100127:
            return "Bitte wählen Sie einen Bestand zum Widerrufen aus";
        case -100128:
            return "Bestand nicht angemeldet";
        case -100129:
            return "afKIAVSRVBriefwahlBereitsVorhanden";
        case -100130:
           return "afWortmeldungTextFehlt";
        case -100131:
            return "afBestaetigungHinweisWeitere1Fehlt";
        case -100132:
            return "afBestaetigungHinweisWeitere2Fehlt";

        case -100133:
            return "Achtung - Eingaben werden nicht verarbeitet";
        case -100134:
            return "Kontaktformular wurde übermittelt";
        case -100135:
            return "Bitte Text eingeben";
        case -100136:
            return "afAktionaersnummerNichtInAktienregisterEnthalten";
        case -100137:
            return "afAktionaersnummerNichtInPortalRegistriert";
        case -100138:
            return "afPublikationenUebermittelt";
        case -100139:
            return "afEMailZwingend";
        case -100140:
            return "perPLZOrtFalsch";
        case -100141:
            return "perIBANFalsch";
        case -100142:
            return "perEmailIstNichtRichtigesFormat";
        case -100143:
            return "perEmailIstNichtRichtigesFormat";
        case -100144:
            return "perEmailBestätigungscodeIstFalsch";
        case -100145:
            return "afEmailBestaetigungErforderlich";
        case -100146:
            return "afEmail2BestaetigungErforderlich";
        case -100147:
            return "perPLZFalsch";
        case -100148:
            return "perGeburtsdatumFalsch";
        case -100149:
            return "perSteuerIdFalsch";
        case -100150:
            return "perBicFalsch";
        case -100151:
            return "Bitte geben Sie den Namen Ihrer Bank ein";
        case -100152:
            return "Bitte geben Sie den Kontoinhaber ein ";
        case -100153:
            return "Bitte geben Sie den Ort ein";
        case -100154:
            return "Bitte wählen Sie ein Thema";
        case -100155:
            return "Leider steht das Mitgliederportal derzeit nicht zur Verfügung. Bitte versuchen Sie es später wieder. Das BetterSmart-Portal für die Generalversammlung steht Ihnen unabhängig davon unter www.ku178.net/bettersmart zur Verfügung.";
        case -100156:
            return "Bitte wählen Sie mindestens einen Tagesordnungspunkt aus";
        case -100157:
            return "Text zu TOP fehlt";

        case -100158:
            return "Die Datei ist nicht vorhanden oder leer";
        case -100159:
            return "Videodatei ist zu groß";
        case -100160:
            return "Textdatei ist zu groß";
        case -100161:
            return "Unzulässiges Dateiformat";
        case -100162:
            return "Bitte eine Datei auswählen";
        case -100163:
            return "Bitte bestätigen Sie die Hinweise";
        case -100164:
            return "Bestätigung wurde per E-Mail verschickt";
        case -100165:
            return "Es darf nur eine einzige Datei ausgewählt werden";
        case -100166:
            return "Leider unterstützt Ihr Browser den Upload nicht - bitte wenden Sie sich an unsere Hotline";

        case -100167:
            return "Bitte geben Sie Ihre Wortmeldung ein";
        case -100168:
            return "Bitte geben Sie Ihren Widerspruch ein";
        case -100169:
            return "Bitte geben Sie Ihren Antrag ein";
        case -100170:
            return "Bitte geben Sie Ihre Mitteilung ein";
        case -100171:
            return "Bitte geben Sie Ihre Stellungnahme ein";
        case -100172:
            return "Abstimmung Online nicht möglich, da vor Ort anwesend";
        case -100173:
            return "Mit dieser Gattung ist kein Zugang möglich";
        case -100174:
            return "Das gescannte Passwort ist falsch";
        case -100175:
            return "Bitte geben Sie die Vertreterdaten vollständig ein (Name, Vorname, Ort)";
        case -100176:
            return "Bitte geben Sie die Daten für die weitere Person vollständig ein (Name, Vorname, Ort)";
        case -100177:
            return "Noch nicht geprüft - kein Zugang möglich";
        case -100178:
            return "E-Mail-Adresse zu lang (maximal 100 Zeichen)";
        case -100179:
            return "Bitte geben Sie Ihre E-Mail-Adresse ein";
        case -100180:
            return "Der Versammlungsraum steht derzeit nicht zur Verfügung";
        case -100181:
            return "Die Hauptversammlung ist geschlossen";
        case -100182:
            return "Zurückziehen derzeit nicht möglich";
        case -100183:
            return "Mindestens ein Hinweis zum Inhalt muß ausgewählt werden";
        case -100184:
            return "Bitte wählen Sie aus, ob Sie die Tasse haben wollen";
        case -100185:
            return "Bitte geben Sie als Vertreter entweder eine Person ein oder wählen Sie einen Verband aus";
        case -100186:
            return "Sie haben eine Person bevollmächtigt - bitte geben Sie an, um welche Personengruppe es sich handelt";
        case -100187:
            return "Der nächste Anmeldeversuch ist in ";
        case -100188:
            return " Sekunden möglich";
        case -100189:
            return "Bitte korrekten Captcha eingeben";
        case -100190:
            return "Kennung ist bereits zugeordnet";
        case -100191:
            return "Eigene Kennung kann nicht zugeordnet werden";
        case -100192:
            return "Bitte bestätigen Sie die Zulässigkeit der Vollmacht gemäß Satzung";

        case -100193:
            return "Anrede ist zwingend einzugeben";
        case -100194:
            return "Titel ist zwingend einzugeben";
        case -100195:
            return "Adelstitel ist zwingend einzugeben";
        case -100196:
            return "Name ist zwingend einzugeben";
        case -100197:
            return "Vorname ist zwingend einzugeben";
        case -100198:
            return "Zu Händen ist zwingend einzugeben";
        case -100199:
            return "Zusatz 1 ist zwingend einzugeben";
        case -100200:
            return "Zusatz 2 ist zwingend einzugeben";
        case -100201:
            return "Strasse ist zwingend einzugeben";
        case -100202:
            return "Land ist zwingend einzugeben";
        case -100203:
            return "PLZ ist zwingend einzugeben";
        case -100204:
            return "Ort ist zwingend einzugeben";
        case -100205:
            return "Mailadresse ist zwingend einzugeben";
        case -100206:
            return "Bitte gültige Mailadresse eingeben";
        case -100207:
            return "Kommunikationssprache ist zwingend einzugeben";
        case -100208:
            return "Gastkarte wurde storniert";
        case -100209:
            return "Gastkarte wurde per Mail verschickt";


        case -100210:
            return "Bitte geben Sie alle Pflichtfelder ein";
        case -100211:
            return "Bei der Mitgliedsnummer sind nur bis zu 6 Ziffern zulaessig";

        case -100212:
            return "Sie haben Daten für einen Bevollmächtigten eingegeben, aber keine Vollmacht erteilt. Bitte erteilen Sie die Vollmacht, oder löschen Sie die Eingaben beim Bevollmächtigten";
        case -100213:
            return "Sie haben Daten für einen gesetzlichen bzw. zur Vertretung ermächtigten Vertreter eingegeben, diese jedoch nicht gespeichert. Bitte speichern Sie den Vertreter, oder löschen Sie die Eingaben des Vertreters";

            
        case -200001:
            return "fTableInfoNichtLesbar";
        case -200002:
            return "fTablesMuessenUpgedatetWerden";
        case -200003:
            return "sysGeraeteSetNichtVorhanden: Systemfehler - aktives Geräteset ist nicht vorhanden!";
        case -200004:
            return "sysGeraeteKlasseSetZuordnungNichtVorhanden";
        case -200005:
            return "sysGeraeteKlasseNichtVorhanden";
        case -200006:
            return "sysPortalSpracheNichtVorhanden";
        case -200007:
            return "sysPortalSpracheNichtAktiv";
        case -200008:
            return "rpJobCantBeInitialized";
        case -200009:
            return "rpLanguageFileNotFound";
        case -200010:
            return "rpErrorWhilePrinting";
        case -200011:
            return "rpAbbruchBeiAuswahl";

        case -300001:
            return "Falsche Erklärung im Anmelde-Scanlauf (Abstimmung oder SRV-HV)";
        case -300002:
            return "Markierungen nicht eindeutig";
        case -300003:
            return "Eintrittskarten mit Vollmacht im Scan-Lauf nicht möglich";

        case -900001:
            return "melEk1PerMailVerschickt";
        case -900002:
            return "melEk2PerMailVerschickt";

        default:
            return "Unbehandelter Fehler!";
        }
    }

    /** Zeigt Fehlermeldung zu fehlerNr in einem Popup mit titel an.
     * 
     * @param aufrufendesPanel = Aufgrufendes Dialogumfeld vom Tpy Jpanel
     * @param dialogTyp = wird an getFehlertext weitergegeben, 1 = Gast
     * @param fehlerNr
     * @param titel = Anzeige im Popup
     */
    //	public static void fehlermeldungPopUp(JPanel aufrufendesPanel,int dialogTyp, int fehlerNr, String titel){
    //		String fehler=CaFehler.getFehlertext(fehlerNr, dialogTyp);
    //		JOptionPane.showMessageDialog(aufrufendesPanel, fehler,titel,JOptionPane.ERROR_MESSAGE);
    //
    //	}

}
