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
package de.meetingapps.meetingclient.meetingHVMaster;

import java.io.IOException;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;

import org.apache.http.ParseException;

import de.meetingapps.meetingclient.meetingClientDialoge.CaZeigeHinweis;
import de.meetingapps.meetingclient.meetingClientDialoge.CtrlRoot;
import de.meetingapps.meetingportal.meetComAllg.CaBug;
import de.meetingapps.meetingportal.meetComAllg.CaFehler;
import de.meetingapps.meetingportal.meetComAllg.CaString;
import de.meetingapps.meetingportal.meetComDb.DbBundle;
import de.meetingapps.meetingportal.meetComEntities.EclPortalUnterlagen;
import de.meetingapps.meetingportal.meetComHVParam.ParamS;
import de.meetingapps.meetingportal.meetComKonst.KonstPortalFunktionen;
import de.meetingapps.meetingportal.meetComStub.StubParameter;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputControl;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.WindowEvent;

/**
 * The Class CtrlParameterPortalApp.
 */
public class CtrlParameterPortalApp extends CtrlRoot {

    /** The log drucken. */
    private int logDrucken = 10;

    /** The resources. */
    @FXML
    private ResourceBundle resources;

    /** The location. */
    @FXML
    private URL location;

    /** The tb pane annzeige. */
    @FXML
    private TabPane tbPaneAnnzeige;

    /** The tp allgemein. */
    @FXML
    private Tab tpAllgemein;

    /** The tf portal auch in englisch verfuegbar. */
    @FXML
    private TextField tfPortalAuchInEnglischVerfuegbar;

    /** The tf datumsformat DE. */
    @FXML
    private TextField tfDatumsformatDE;

    /** The tf datumsformat EN. */
    @FXML
    private TextField tfDatumsformatEN;

    /** The tf letzter aktienregister update. */
    @FXML
    private TextField tfLetzterAktienregisterUpdate;

    /** The tf art sprachumschaltung. */
    @FXML
    private TextField tfArtSprachumschaltung;

    /** The tf bestaetigen dialog. */
    @FXML
    private TextField tfBestaetigenDialog;

    /** The tf quittung dialog. */
    @FXML
    private TextField tfQuittungDialog;

    /** The tf kontakt fenster. */
    @FXML
    private TextField tfKontaktFenster;

    /** The tf personen anzeige anrede mit aufnahmen. */
    @FXML
    private TextField tfPersonenAnzeigeAnredeMitAufnahmen;

    /** The tf text postfach mit aufnahmen. */
    @FXML
    private TextField tfTextPostfachMitAufnahmen;

    /** The tf portal aktuell aktiv. */
    @FXML
    private TextField tfPortalAktuellAktiv;

    /** The tf portal standard. */
    @FXML
    private TextField tfPortalStandard;

    /** The tf test modus. */
    @FXML
    private TextField tfTestModus;

    /** The tf anzeige stimmen kennung. */
    @FXML
    private TextField tfAnzeigeStimmenKennung;

    /** The tf portal ist dauerhaft. */
    @FXML
    private TextField tfPortalIstDauerhaft;

    /** The tp login. */
    @FXML
    private Tab tpLogin;

    /** The tf app install buttons anzeigen. */
    @FXML
    private TextField tfAppInstallButtonsAnzeigen;

    /** The tf passwort case sensitiv. */
    @FXML
    private TextField tfPasswortCaseSensitiv;

    /** The tf passwort mindest laenge. */
    @FXML
    private TextField tfPasswortMindestLaenge;

    /** The tf verfahren passwort vergessen. */
    @FXML
    private TextField tfVerfahrenPasswortVergessen;

    /** The cb gattung 1 moeglich. */
    @FXML
    private CheckBox cbGattung1Moeglich;

    /** The cb gattung 2 moeglich. */
    @FXML
    private CheckBox cbGattung2Moeglich;

    /** The cb gattung 3 moeglich. */
    @FXML
    private CheckBox cbGattung3Moeglich;

    /** The cb gattung 4 moeglich. */
    @FXML
    private CheckBox cbGattung4Moeglich;

    /** The cb gattung 5 moeglich. */
    @FXML
    private CheckBox cbGattung5Moeglich;

    /** The tf login gesperrt. */
    @FXML
    private TextField tfLoginGesperrt;

    /** The tf login gesperrt text deutsch. */
    @FXML
    private TextField tfLoginGesperrtTextDeutsch;

    /** The tf login gesperrt text englisch. */
    @FXML
    private TextField tfLoginGesperrtTextEnglisch;

    /** The tf login IP tracking. */
    @FXML
    private TextField tfLoginIPTracking;

    /** The tf passwort per post pruefen. */
    @FXML
    private TextField tfPasswortPerPostPruefen;

    /** The tf logout ziel. */
    @FXML
    private TextField tfLogoutZiel;

    /** The tf login verfahren. */
    @FXML
    private TextField tfLoginVerfahren;

    /** The tf cookie hinweis. */
    @FXML
    private TextField tfCookieHinweis;

    /** The tf captcha verwenden. */
    @FXML
    private TextField tfCaptchaVerwenden;

    /** The tf login verzoegerung ab versuch. */
    @FXML
    private TextField tfLoginVerzoegerungAbVersuch;

    /** The tf login verzoegerung sekunden. */
    @FXML
    private TextField tfLoginVerzoegerungSekunden;

    /** The tf alternative login kennung. */
    @FXML
    private TextField tfAlternativeLoginKennung;

    /** The tf anzahl eindeutige kennungen vorhanden. */
    @FXML
    private TextField tfAnzahlEindeutigeKennungenVorhanden;

    /** The lbl anzahl eindeutige kennungen verbraucht. */
    @FXML
    private Label lblAnzahlEindeutigeKennungenVerbraucht;

    /** The tf teilnehmer kann sich weitere kennungen zuordnen. */
    @FXML
    private TextField tfTeilnehmerKannSichWeitereKennungenZuordnen;

    /** The tf kennung aufbereiten. */
    @FXML
    private TextField tfKennungAufbereiten;

    /** The tf kennung aufbereiten fuer anzeige. */
    @FXML
    private TextField tfKennungAufbereitenFuerAnzeige;

    /** The tf verfahren passwort vergessen bei email hinterlegt auch post. */
    @FXML
    private TextField tfVerfahrenPasswortVergessenBeiEmailHinterlegtAuchPost;

    /** The tf link email enthaelt link oder code. */
    @FXML
    private TextField tfLinkEmailEnthaeltLinkOderCode;

    /** The tf email bestaetigen ist zwingend. */
    @FXML
    private TextField tfEmailBestaetigenIstZwingend;

    /** The tp registrierung. */
    @FXML
    private Tab tpRegistrierung;

    /** The tf dauerhaftes passwort moeglich. */
    @FXML
    private TextField tfDauerhaftesPasswortMoeglich;

    /** The tf registrierung fuer email versand moeglich. */
    @FXML
    private TextField tfRegistrierungFuerEmailVersandMoeglich;

    @FXML
    private TextField tfEmailVersandRegistrierungOderWiderspruch;

    /** The tf adressaenderung moeglich. */
    @FXML
    private TextField tfAdressaenderungMoeglich;

    /** The tf kommunikationssprache auswahl. */
    @FXML
    private TextField tfKommunikationsspracheAuswahl;

    /** The tf publikationen anbieten. */
    @FXML
    private TextField tfPublikationenAnbieten;

    /** The tf kontakt details anbieten. */
    @FXML
    private TextField tfKontaktDetailsAnbieten;

    /** The tf separate teilnahmebedingungen fuer gewinnspiel. */
    @FXML
    private TextField tfSeparateTeilnahmebedingungenFuerGewinnspiel;

    /** The tf separate datenschutzerklaerung. */
    @FXML
    private TextField tfSeparateDatenschutzerklaerung;

    /** The tf bestaetigen hinweis aktionaersportal. */
    @FXML
    private TextField tfBestaetigenHinweisAktionaersportal;

    /** The tf bestaetigen hinweis H vportal. */
    @FXML
    private TextField tfBestaetigenHinweisHVportal;

    /** The tf email nur bei E versand oder passwort. */
    @FXML
    private TextField tfEmailNurBeiEVersandOderPasswort;

    /** The tf bestaetigungsseite einstellungen. */
    @FXML
    private TextField tfBestaetigungsseiteEinstellungen;

    /** The tf absende mail adresse. */
    @FXML
    private TextField tfAbsendeMailAdresse;

    /** The tf reihenfolge registrierung. */
    @FXML
    private TextField tfReihenfolgeRegistrierung;

    /** The tf mail eingabe serviceline. */
    @FXML
    private TextField tfMailEingabeServiceline;

    /** The tp erklaerungen. */
    @FXML
    private Tab tpErklaerungen;

    /** The tf briefwahl angeboten. */
    @FXML
    private TextField tfBriefwahlAngeboten;

    /** The tf vollmacht dritte angeboten. */
    @FXML
    private TextField tfVollmachtDritteAngeboten;

    /** The tf vollmacht KIAV angeboten. */
    @FXML
    private TextField tfVollmachtKIAVAngeboten;

    /** The tf gastkarten anforderung moeglich. */
    @FXML
    private TextField tfGastkartenAnforderungMoeglich;

    /** The tf oeffentliche ID moeglich. */
    @FXML
    private TextField tfOeffentlicheIDMoeglich;

    /** The tf zusaetzliche EK dritte moeglich. */
    @FXML
    private TextField tfZusaetzlicheEKDritteMoeglich;

    /** The tf ek und weisung gleichzeitig moeglich. */
    @FXML
    private TextField tfEkUndWeisungGleichzeitigMoeglich;

    /** The tf ek selbst moeglich. */
    @FXML
    private TextField tfEkSelbstMoeglich;

    /** The tf ek vollmacht moeglich. */
    @FXML
    private TextField tfEkVollmachtMoeglich;

    /** The tf ek 2 personengemeinschaft moeglich. */
    @FXML
    private TextField tfEk2PersonengemeinschaftMoeglich;

    /** The tf ek 2 mit oder ohne vollmacht moeglich. */
    @FXML
    private TextField tfEk2MitOderOhneVollmachtMoeglich;

    /** The tf ek 2 selbst moeglich. */
    @FXML
    private TextField tfEk2SelbstMoeglich;

    /** The tf srv angeboten. */
    @FXML
    private TextField tfSrvAngeboten;

    /** The tf briefwahl zusaetzlich zu SRV moeglich. */
    @FXML
    private TextField tfBriefwahlZusaetzlichZuSRVMoeglich;

    /** The tf anmelden ohne weitere WK. */
    @FXML
    private TextField tfAnmeldenOhneWeitereWK;

    /** The tf vollmacht dritte und andere WK moeglich. */
    @FXML
    private TextField tfVollmachtDritteUndAndereWKMoeglich;

    /** The tf handhabung weisung durch verschiedene. */
    @FXML
    private TextField tfHandhabungWeisungDurchVerschiedene;

    /** The tf weisung aktuell nicht moeglich. */
    @FXML
    private TextField tfWeisungAktuellNichtMoeglich;

    @FXML
    private TextField tfWeisungAktuellNichtMoeglichAberBriefwahlSchon;

    /** The tf erkl an pos 1. */
    @FXML
    private TextField tfErklAnPos1;

    /** The tf erkl an pos 2. */
    @FXML
    private TextField tfErklAnPos2;

    /** The tf erkl an pos 3. */
    @FXML
    private TextField tfErklAnPos3;

    /** The tf erkl an pos 4. */
    @FXML
    private TextField tfErklAnPos4;

    /** The tf erkl an pos 5. */
    @FXML
    private TextField tfErklAnPos5;

    /** The tf vollmachtsnachweis auf startseite aktiv. */
    @FXML
    private TextField tfVollmachtsnachweisAufStartseiteAktiv;

    @FXML
    private TextField tfBestaetigungStimmabgabeNachHV;

    @FXML
    private TextField tfBestaetigungPerEmailUeberallZulassen;

    /** The tp weisung. */
    @FXML
    private Tab tpWeisung;

    /** The tf P nichtmarkiert speichern als. */
    @FXML
    private TextField tfPNichtmarkiertSpeichernAls;

    /** The tf gegenantraege weisungen moeglich. */
    @FXML
    private TextField tfGegenantraegeWeisungenMoeglich;

    /** The tf gegenantrags text. */
    @FXML
    private TextField tfGegenantragsText;

    /** The cb gesamt markierung ja. */
    @FXML
    private CheckBox cbGesamtMarkierungJa;

    /** The cb gesamt markierung nein. */
    @FXML
    private CheckBox cbGesamtMarkierungNein;

    /** The cb gesamt markierung enthaltung. */
    @FXML
    private CheckBox cbGesamtMarkierungEnthaltung;

    /** The cb gesamt markierung im sinne. */
    @FXML
    private CheckBox cbGesamtMarkierungImSinne;

    /** The cb gesamt markierung gegen sinne. */
    @FXML
    private CheckBox cbGesamtMarkierungGegenSinne;

    /** The cb gesamt markierung alles loeschen. */
    @FXML
    private CheckBox cbGesamtMarkierungAllesLoeschen;

    /** The cb markierung ja. */
    @FXML
    private CheckBox cbMarkierungJa;

    /** The cb markierung nein. */
    @FXML
    private CheckBox cbMarkierungNein;

    /** The cb markierung enthaltung. */
    @FXML
    private CheckBox cbMarkierungEnthaltung;

    /** The cb markierung loeschen. */
    @FXML
    private CheckBox cbMarkierungLoeschen;

    /** The cb checkbox bei SRV. */
    @FXML
    private CheckBox cbCheckboxBeiSRV;

    /** The cb checkbox bei briefwahl. */
    @FXML
    private CheckBox cbCheckboxBeiBriefwahl;

    /** The cb checkbox bei KIAV. */
    @FXML
    private CheckBox cbCheckboxBeiVollmacht;

    /** The cb checkbox bei KIAV. */
    @FXML
    private CheckBox cbCheckboxBeiKIAV;

    /** The cb jn abfrage bei weisung quittung. */
    @FXML
    private CheckBox cbJnAbfrageBeiWeisungQuittung;

    /** The tf bestaetigung bei weisung. */
    @FXML
    private TextField tfBestaetigungBeiWeisung;

    /** The tf bestaetigung bei weisung mit TOP. */
    @FXML
    private TextField tfBestaetigungBeiWeisungMitTOP;

    /** The tf sammelkarten fuer aenderung sperren. */
    @FXML
    private TextField tfSammelkartenFuerAenderungSperren;

    /** The tp text inhalte. */
    @FXML
    private Tab tpTextInhalte;

    /** The tf standard texte beruecksichtigen. */
    @FXML
    private TextField tfStandardTexteBeruecksichtigen;

    /** The tf mehrere stimmrechtsvertreter. */
    @FXML
    private TextField tfMehrereStimmrechtsvertreter;

    /** The tf stimmrechtsvertreter name DE. */
    @FXML
    private TextField tfStimmrechtsvertreterNameDE;

    /** The tf stimmrechtsvertreter name EN. */
    @FXML
    private TextField tfStimmrechtsvertreterNameEN;

    /** The tf link tagesordnung. */
    @FXML
    private TextField tfLinkTagesordnung;

    /** The tf link gegenantraege. */
    @FXML
    private TextField tfLinkGegenantraege;

    /** The tf link einladungs PDF. */
    @FXML
    private TextField tfLinkEinladungsPDF;

    /** The tf email adresse link. */
    @FXML
    private TextField tfEmailAdresseLink;

    /** The tf email adresse text. */
    @FXML
    private TextField tfEmailAdresseText;

    /** The tf basis set standard texte verwenden. */
    @FXML
    private TextField tfBasisSetStandardTexteVerwenden;

    /** The tf link nutzungsbedingungen aktionaers portal. */
    @FXML
    private TextField tfLinkNutzungsbedingungenAktionaersPortal;

    /** The tf link nutzungsbedingungen HV portal. */
    @FXML
    private TextField tfLinkNutzungsbedingungenHVPortal;

    /** The tf link datenschutzhinweise. */
    @FXML
    private TextField tfLinkDatenschutzhinweise;

    /** The tf link impressum. */
    @FXML
    private TextField tfLinkImpressum;

    /** The tf logo breite. */
    @FXML
    private TextField tfLogoBreite;

    /** The tf logo name. */
    @FXML
    private TextField tfLogoName;

    /** The tf CSS name. */
    @FXML
    private TextField tfCSSName;

    /** The tf link datenschutzhinweise kunde. */
    @FXML
    private TextField tfLinkDatenschutzhinweiseKunde;

    /** The tf ek text. */
    @FXML
    private TextField tfEkText;

    /** The tf ek text mit artikel. */
    @FXML
    private TextField tfEkTextMitArtikel;

    /** The tf ek text EN. */
    @FXML
    private TextField tfEkTextEN;

    /** The ek text EN mit artikel. */
    @FXML
    private TextField ekTextENMitArtikel;

    /** The tf design kuerzel. */
    @FXML
    private TextField tfDesignKuerzel;

    /** The tf logout oben oder unten. */
    @FXML
    private TextField tfLogoutObenOderUnten;

    /** The tf logo position. */
    @FXML
    private TextField tfLogoPosition;

    /** The tf vollmacht email adresse link. */
    @FXML
    private TextField tfVollmachtEmailAdresseLink;

    /** The tf vollmacht email adresse text. */
    @FXML
    private TextField tfVollmachtEmailAdresseText;

    /** The tf kurz link portal. */
    @FXML
    private TextField tfKurzLinkPortal;

    /** The tf fragezeichen hinweise verwenden. */
    @FXML
    private TextField tfFragezeichenHinweiseVerwenden;

    /** The tf subdomain portal. */
    @FXML
    private TextField tfSubdomainPortal;

    /** The tp text inhalte 2. */
    @FXML
    private Tab tpTextInhalte2;

    /** The tf kachel farbe. */
    @FXML
    private TextField tfKachelFarbe;

    /** The tf theme farbe. */
    @FXML
    private TextField tfThemeFarbe;

    /** The tf schriftgroesse global. */
    @FXML
    private TextField tfSchriftgroesseGlobal;

    /** The tf farbe hintergrund. */
    @FXML
    private TextField tfFarbeHintergrund;

    /** The tf farbe text. */
    @FXML
    private TextField tfFarbeText;

    /** The tf farbe header. */
    @FXML
    private TextField tfFarbeHeader;

    /** The tf logo mindestbreite. */
    @FXML
    private TextField tfLogoMindestbreite;

    /** The tf farbe hintergrund logout btn. */
    @FXML
    private TextField tfFarbeHintergrundLogoutBtn;

    /** The tf farbe schrift logout btn. */
    @FXML
    private TextField tfFarbeSchriftLogoutBtn;

    /** The tf farbe rahmen logout btn. */
    @FXML
    private TextField tfFarbeRahmenLogoutBtn;

    /** The tf breite rahmen logout btn. */
    @FXML
    private TextField tfBreiteRahmenLogoutBtn;

    /** The tf radius rahmen logout btn. */
    @FXML
    private TextField tfRadiusRahmenLogoutBtn;

    /** The tf stil rahmen logout btn. */
    @FXML
    private TextField tfStilRahmenLogoutBtn;

    /** The tf farbe hintergrund logout btn hover. */
    @FXML
    private TextField tfFarbeHintergrundLogoutBtnHover;

    /** The tf farbe schrift logout btn hover. */
    @FXML
    private TextField tfFarbeSchriftLogoutBtnHover;

    /** The tf farbe rahmen logout btn hover. */
    @FXML
    private TextField tfFarbeRahmenLogoutBtnHover;

    /** The tf breite rahmen logout btn hover. */
    @FXML
    private TextField tfBreiteRahmenLogoutBtnHover;

    /** The tf radius rahmen logout btn hover. */
    @FXML
    private TextField tfRadiusRahmenLogoutBtnHover;

    /** The tf stil rahmen logout btn hover. */
    @FXML
    private TextField tfStilRahmenLogoutBtnHover;

    /** The tf farbe ueberschrift hintergrund. */
    @FXML
    private TextField tfFarbeUeberschriftHintergrund;

    /** The tf farbe ueberschrift. */
    @FXML
    private TextField tfFarbeUeberschrift;

    /** The tf farbe hintergrund footer top. */
    @FXML
    private TextField tfFarbeHintergrundFooterTop;

    /** The tf farbe schrift footer top. */
    @FXML
    private TextField tfFarbeSchriftFooterTop;

    /** The tf farbe link footer top. */
    @FXML
    private TextField tfFarbeLinkFooterTop;

    /** The tf farbe link footer top hover. */
    @FXML
    private TextField tfFarbeLinkFooterTopHover;

    /** The tf farbe hintergrund footer bottom. */
    @FXML
    private TextField tfFarbeHintergrundFooterBottom;

    /** The tf farbe schrift footer bottom. */
    @FXML
    private TextField tfFarbeSchriftFooterBottom;

    /** The tf farbe link footer bottom. */
    @FXML
    private TextField tfFarbeLinkFooterBottom;

    /** The tf farbe link footer bottom hover. */
    @FXML
    private TextField tfFarbeLinkFooterBottomHover;

    /** The tf farbe hintergrund btn 00. */
    @FXML
    private TextField tfFarbeHintergrundBtn00;

    /** The tf farbe schrift btn 00. */
    @FXML
    private TextField tfFarbeSchriftBtn00;

    /** The tf farbe rahmen btn 00. */
    @FXML
    private TextField tfFarbeRahmenBtn00;

    /** The tf breite rahmen btn 00. */
    @FXML
    private TextField tfBreiteRahmenBtn00;

    /** The tf radius rahmen btn 00. */
    @FXML
    private TextField tfRadiusRahmenBtn00;

    /** The tf stil rahmen btn 00. */
    @FXML
    private TextField tfStilRahmenBtn00;

    /** The tf farbe hintergrund btn 00 hover. */
    @FXML
    private TextField tfFarbeHintergrundBtn00Hover;

    /** The tf farbe schrift btn 00 hover. */
    @FXML
    private TextField tfFarbeSchriftBtn00Hover;

    /** The tf farbe rahmen btn 00 hover. */
    @FXML
    private TextField tfFarbeRahmenBtn00Hover;

    /** The tf breite rahmen btn 00 hover. */
    @FXML
    private TextField tfBreiteRahmenBtn00Hover;

    /** The tf radius rahmen btn 00 hover. */
    @FXML
    private TextField tfRadiusRahmenBtn00Hover;

    /** The tf stil rahmen btn 00 hover. */
    @FXML
    private TextField tfStilRahmenBtn00Hover;

    /** The tf farbe link. */
    @FXML
    private TextField tfFarbeLink;

    /** The tf farbe link hover. */
    @FXML
    private TextField tfFarbeLinkHover;

    /** The tf farbe rahmen eingabefelder. */
    @FXML
    private TextField tfFarbeRahmenEingabefelder;

    /** The tf breite rahmen eingabefelder. */
    @FXML
    private TextField tfBreiteRahmenEingabefelder;

    /** The tf radius rahmen eingabefelder. */
    @FXML
    private TextField tfRadiusRahmenEingabefelder;

    /** The tf stil rahmen eingabefelder. */
    @FXML
    private TextField tfStilRahmenEingabefelder;

    /** The tf farbe focus. */
    @FXML
    private TextField tfFarbeFocus;

    /** The tf farbe error. */
    @FXML
    private TextField tfFarbeError;

    /** The tf farbe error schrift. */
    @FXML
    private TextField tfFarbeErrorSchrift;

    /** The tf farbe warning. */
    @FXML
    private TextField tfFarbeWarning;

    /** The tf farbe warning schrift. */
    @FXML
    private TextField tfFarbeWarningSchrift;

    /** The tf farbe success. */
    @FXML
    private TextField tfFarbeSuccess;

    /** The tf farbe success schrift. */
    @FXML
    private TextField tfFarbeSuccessSchrift;

    /** The tf farbe hintergrund login btn. */
    @FXML
    private TextField tfFarbeHintergrundLoginBtn;

    /** The tf farbe schrift login btn. */
    @FXML
    private TextField tfFarbeSchriftLoginBtn;

    /** The tf farbe rahmen login btn. */
    @FXML
    private TextField tfFarbeRahmenLoginBtn;

    /** The tf breite rahmen login btn. */
    @FXML
    private TextField tfBreiteRahmenLoginBtn;

    /** The tf radius rahmen login btn. */
    @FXML
    private TextField tfRadiusRahmenLoginBtn;

    /** The tf stil rahmen login btn. */
    @FXML
    private TextField tfStilRahmenLoginBtn;

    /** The tf farbe hintergrund login btn hover. */
    @FXML
    private TextField tfFarbeHintergrundLoginBtnHover;

    /** The tf farbe schrift login btn hover. */
    @FXML
    private TextField tfFarbeSchriftLoginBtnHover;

    /** The tf farbe rahmen login btn hover. */
    @FXML
    private TextField tfFarbeRahmenLoginBtnHover;

    /** The tf breite rahmen login btn hover. */
    @FXML
    private TextField tfBreiteRahmenLoginBtnHover;

    /** The tf radius rahmen login btn hover. */
    @FXML
    private TextField tfRadiusRahmenLoginBtnHover;

    /** The tf stil rahmen login btn hover. */
    @FXML
    private TextField tfStilRahmenLoginBtnHover;

    /** The tf farbe rahmen login bereich. */
    @FXML
    private TextField tfFarbeRahmenLoginBereich;

    /** The tf breite rahmen login bereich. */
    @FXML
    private TextField tfBreiteRahmenLoginBereich;

    /** The tf radius rahmen login bereich. */
    @FXML
    private TextField tfRadiusRahmenLoginBereich;

    /** The tf stil rahmen login bereich. */
    @FXML
    private TextField tfStilRahmenLoginBereich;

    /** The tf farbe hintergrund login bereich. */
    @FXML
    private TextField tfFarbeHintergrundLoginBereich;

    /** The tf farbe link login bereich. */
    @FXML
    private TextField tfFarbeLinkLoginBereich;

    /** The tf farbe link hover login bereich. */
    @FXML
    private TextField tfFarbeLinkHoverLoginBereich;

    /** The tf farbe rahmen eingabefelder login bereich. */
    @FXML
    private TextField tfFarbeRahmenEingabefelderLoginBereich;

    /** The tf breite rahmen eingabefelder login bereich. */
    @FXML
    private TextField tfBreiteRahmenEingabefelderLoginBereich;

    /** The tf radius rahmen eingabefelder login bereich. */
    @FXML
    private TextField tfRadiusRahmenEingabefelderLoginBereich;

    /** The tf stil rahmen eingabefelder login bereich. */
    @FXML
    private TextField tfStilRahmenEingabefelderLoginBereich;

    /** The tf farbe liste ungerade. */
    @FXML
    private TextField tfFarbeListeUngerade;

    /** The tf farbe liste gerade. */
    @FXML
    private TextField tfFarbeListeGerade;

    /** The tf farbe bestandsbereich ungerade reihe. */
    @FXML
    private TextField tfFarbeBestandsbereichUngeradeReihe;

    /** The tf farbe bestandsbereich gerade reihe. */
    @FXML
    private TextField tfFarbeBestandsbereichGeradeReihe;

    /** The tf farbe line unten bestandsbereich. */
    @FXML
    private TextField tfFarbeLineUntenBestandsbereich;

    /** The tf breite line unten bestandsbereich. */
    @FXML
    private TextField tfBreiteLineUntenBestandsbereich;

    /** The tf stil line unten bestandsbereich. */
    @FXML
    private TextField tfStilLineUntenBestandsbereich;

    /** The tf farbe rahmen anmeldeuebersicht. */
    @FXML
    private TextField tfFarbeRahmenAnmeldeuebersicht;

    /** The tf breite rahmen anmeldeuebersicht. */
    @FXML
    private TextField tfBreiteRahmenAnmeldeuebersicht;

    /** The tf radius rahmen anmeldeuebersicht. */
    @FXML
    private TextField tfRadiusRahmenAnmeldeuebersicht;

    /** The tf stil rahmen anmeldeuebersicht. */
    @FXML
    private TextField tfStilRahmenAnmeldeuebersicht;

    /** The tf farbe trennlinie anmeldeuebersicht. */
    @FXML
    private TextField tfFarbeTrennlinieAnmeldeuebersicht;

    /** The tf breite trennlinie anmeldeuebersicht. */
    @FXML
    private TextField tfBreiteTrennlinieAnmeldeuebersicht;

    /** The tf stil trennlinie anmeldeuebersicht. */
    @FXML
    private TextField tfStilTrennlinieAnmeldeuebersicht;

    /** The tf farbe rahmen erteilte willenserklärungen. */
    @FXML
    private TextField tfFarbeRahmenErteilteWillenserklärungen;

    /** The tf breite rahmen erteilte willenserklärungen. */
    @FXML
    private TextField tfBreiteRahmenErteilteWillenserklärungen;

    /** The tf radius rahmen erteilte willenserklärungen. */
    @FXML
    private TextField tfRadiusRahmenErteilteWillenserklärungen;

    /** The tf stil rahmen erteilte willenserklärungen. */
    @FXML
    private TextField tfStilRahmenErteilteWillenserklärungen;

    /** The tf farbe hintergrund erteilte willenserklärungen. */
    @FXML
    private TextField tfFarbeHintergrundErteilteWillenserklärungen;

    /** The tf farbe schrift erteilte willenserklärungen. */
    @FXML
    private TextField tfFarbeSchriftErteilteWillenserklärungen;

    /** The tf farbe rahmen abstimmungstabelle. */
    @FXML
    private TextField tfFarbeRahmenAbstimmungstabelle;

    /** The tf breite rahmen abstimmungstabelle. */
    @FXML
    private TextField tfBreiteRahmenAbstimmungstabelle;

    /** The tf radius rahmen abstimmungstabelle. */
    @FXML
    private TextField tfRadiusRahmenAbstimmungstabelle;

    /** The tf stil rahmen abstimmungstabelle. */
    @FXML
    private TextField tfStilRahmenAbstimmungstabelle;

    /** The tf farbe hintergrund abstimmungstabelle ungerade reihen. */
    @FXML
    private TextField tfFarbeHintergrundAbstimmungstabelleUngeradeReihen;

    /** The tf farbe schrift abstimmungstabelle ungerade reihen. */
    @FXML
    private TextField tfFarbeSchriftAbstimmungstabelleUngeradeReihen;

    /** The tf farbe hintergrund abstimmungstabelle gerade reihen. */
    @FXML
    private TextField tfFarbeHintergrundAbstimmungstabelleGeradeReihen;

    /** The tf farbe schrift abstimmungstabelle gerade reihen. */
    @FXML
    private TextField tfFarbeSchriftAbstimmungstabelleGeradeReihen;

    /** The tf farbe hintergrund weisung ja. */
    @FXML
    private TextField tfFarbeHintergrundWeisungJa;

    /** The tf farbe schrift weisung ja. */
    @FXML
    private TextField tfFarbeSchriftWeisungJa;

    /** The tf farbe rahmen weisung ja. */
    @FXML
    private TextField tfFarbeRahmenWeisungJa;

    /** The tf farbe hintergrund weisung ja checked. */
    @FXML
    private TextField tfFarbeHintergrundWeisungJaChecked;

    /** The tf farbe schrift weisung ja checked. */
    @FXML
    private TextField tfFarbeSchriftWeisungJaChecked;

    /** The tf farbe rahmen weisung ja checked. */
    @FXML
    private TextField tfFarbeRahmenWeisungJaChecked;

    /** The tf farbe hintergrund weisung nein. */
    @FXML
    private TextField tfFarbeHintergrundWeisungNein;

    /** The tf farbe schrift weisung nein. */
    @FXML
    private TextField tfFarbeSchriftWeisungNein;

    /** The tf farbe rahmen weisung nein. */
    @FXML
    private TextField tfFarbeRahmenWeisungNein;

    /** The tf farbe hintergrund weisung nein checked. */
    @FXML
    private TextField tfFarbeHintergrundWeisungNeinChecked;

    /** The tf farbe schrift weisung nein checked. */
    @FXML
    private TextField tfFarbeSchriftWeisungNeinChecked;

    /** The tf farbe rahmen weisung nein checked. */
    @FXML
    private TextField tfFarbeRahmenWeisungNeinChecked;

    /** The tf farbe hintergrund weisung enthaltung. */
    @FXML
    private TextField tfFarbeHintergrundWeisungEnthaltung;

    /** The tf farbe schrift weisung enthaltung. */
    @FXML
    private TextField tfFarbeSchriftWeisungEnthaltung;

    /** The tf farbe rahmen weisung enthaltung. */
    @FXML
    private TextField tfFarbeRahmenWeisungEnthaltung;

    /** The tf farbe hintergrund weisung enthaltung checked. */
    @FXML
    private TextField tfFarbeHintergrundWeisungEnthaltungChecked;

    /** The tf farbe schrift weisung enthaltung checked. */
    @FXML
    private TextField tfFarbeSchriftWeisungEnthaltungChecked;

    /** The tf farbe rahmen weisung enthaltung checked. */
    @FXML
    private TextField tfFarbeRahmenWeisungEnthaltungChecked;

    /** The tf breite rahmen unten buttons. */
    @FXML
    private TextField tfBreiteRahmenUntenButtons;

    /** The tf radius rahmen unten buttons. */
    @FXML
    private TextField tfRadiusRahmenUntenButtons;

    /** The tf stil rahmen unten buttons. */
    @FXML
    private TextField tfStilRahmenUntenButtons;

    /** The tf farbe hintergrund unten buttons hover. */
    @FXML
    private TextField tfFarbeHintergrundUntenButtonsHover;

    /** The tf farbe schrift unten buttons hover. */
    @FXML
    private TextField tfFarbeSchriftUntenButtonsHover;

    /** The tf farbe rahmen unten buttons hover. */
    @FXML
    private TextField tfFarbeRahmenUntenButtonsHover;

    /** The tf breite rahmen unten buttons hover. */
    @FXML
    private TextField tfBreiteRahmenUntenButtonsHover;

    /** The tf radius rahmen unten buttons hover. */
    @FXML
    private TextField tfRadiusRahmenUntenButtonsHover;

    /** The tf stil rahmen unten buttons hover. */
    @FXML
    private TextField tfStilRahmenUntenButtonsHover;

    /** The tf farbe hintergrund modal. */
    @FXML
    private TextField tfFarbeHintergrundModal;

    /** The tf farbe schrift modal. */
    @FXML
    private TextField tfFarbeSchriftModal;

    /** The tf farbe hintergrund modal header. */
    @FXML
    private TextField tfFarbeHintergrundModalHeader;

    /** The tf farbe schrift modal header. */
    @FXML
    private TextField tfFarbeSchriftModalHeader;

    /** The tf farbe trennlinie modal. */
    @FXML
    private TextField tfFarbeTrennlinieModal;

    /** The tf farbe hintergrund unten buttons. */
    @FXML
    private TextField tfFarbeHintergrundUntenButtons;

    /** The tf farbe schrift unten buttons. */
    @FXML
    private TextField tfFarbeSchriftUntenButtons;

    /** The tf farbe rahmen unten buttons. */
    @FXML
    private TextField tfFarbeRahmenUntenButtons;

    /** The tf farbe cookie hint button schrift. */
    @FXML
    private TextField tfFarbeCookieHintButtonSchrift;

    /** The tf farbe cookie hint button. */
    @FXML
    private TextField tfFarbeCookieHintButton;

    /** The tf farbe cookie hint schrift. */
    @FXML
    private TextField tfFarbeCookieHintSchrift;

    /** The tf farbe cookie hint hintergrund. */
    @FXML
    private TextField tfFarbeCookieHintHintergrund;

    /** The tf farbe ladebalken upload leer. */
    @FXML
    private TextField tfFarbeLadebalkenUploadLeer;

    /** The tf farbe ladebalken upload full. */
    @FXML
    private TextField tfFarbeLadebalkenUploadFull;

    /** The tp phasen. */
    @FXML
    private Tab tpPhasen;

    /** The gp phasen. */
    @FXML
    private GridPane gpPhasen;

    /** The tp portal funktionen. */
    @FXML
    private Tab tpPortalFunktionen;

    /** The gp portal funktionen. */
    @FXML
    private GridPane gpPortalFunktionen;

    /** The tp portal unterlagen. */
    @FXML
    private Tab tpPortalUnterlagen;

    /** The gp portal unterlagen. */
    @FXML
    private GridPane gpPortalUnterlagen;

    /** The tp portal mitteilungen. */
    @FXML
    private Tab tpPortalMitteilungen;

    /** The tb pane mitteilungen. */
    @FXML
    private TabPane tbPaneMitteilungen;

    /** The tp fragen. */
    @FXML
    private Tab tpFragen;

    /** The gp portal mitteilungen fragen. */
    @FXML
    private GridPane gpPortalMitteilungenFragen;

    /** The tf fragen steller abfragen. */
    @FXML
    private TextField tfFragenStellerAbfragen;

    /** The tf fragen name abfragen. */
    @FXML
    private TextField tfFragenNameAbfragen;

    /** The tf fragen kontaktdaten abfragen. */
    @FXML
    private TextField tfFragenKontaktdatenAbfragen;

    /** The tf fragen kontaktdaten E mail vorschlagen. */
    @FXML
    private TextField tfFragenKontaktdatenEMailVorschlagen;

    /** The tf fragen kontaktdaten telefon abfragen. */
    @FXML
    private TextField tfFragenKontaktdatenTelefonAbfragen;

    /** The tf fragen kurztext abfragen. */
    @FXML
    private TextField tfFragenKurztextAbfragen;

    /** The tf fragen top liste anbieten. */
    @FXML
    private TextField tfFragenTopListeAnbieten;

    /** The tf fragen langtext abfragen. */
    @FXML
    private TextField tfFragenLangtextAbfragen;

    /** The tf fragen zurueckziehen moeglich. */
    @FXML
    private TextField tfFragenZurueckziehenMoeglich;

    /** The tf fragen laenge. */
    @FXML
    private TextField tfFragenLaenge;

    /** The tf fragen anzahl je aktionaer. */
    @FXML
    private TextField tfFragenAnzahlJeAktionaer;

    /** The tf fragen steller zulaessig. */
    @FXML
    private TextField tfFragenStellerZulaessig;

    /** The tf fragen hinweis gelesen. */
    @FXML
    private TextField tfFragenHinweisGelesen;

    /** The tf fragen rueckfragen ermoeglichen. */
    @FXML
    private TextField tfFragenRueckfragenErmoeglichen;

    /** The tf fragen mail bei eingang. */
    @FXML
    private TextField tfFragenMailBeiEingang;

    /** The tf fragen externer zugriff. */
    @FXML
    private TextField tfFragenExternerZugriff;

    /** The tf fragen mail verteiler 1. */
    @FXML
    private TextField tfFragenMailVerteiler1;

    /** The tf fragen mail verteiler 2. */
    @FXML
    private TextField tfFragenMailVerteiler2;

    /** The tf fragen mail verteiler 3. */
    @FXML
    private TextField tfFragenMailVerteiler3;

    /** The tf fragen hinweis vorbelegen mit. */
    @FXML
    private TextField tfFragenHinweisVorbelegenMit;

    /** The tp wortmeldungen. */
    @FXML
    private Tab tpWortmeldungen;

    /** The gp portal mitteilungen wortmeldungen. */
    @FXML
    private GridPane gpPortalMitteilungenWortmeldungen;

    /** The tf wortmeldung art. */
    @FXML
    private TextField tfWortmeldungArt;

    /** The tf wortmeldung steller abfragen. */
    @FXML
    private TextField tfWortmeldungStellerAbfragen;

    /** The tf wortmeldung name abfragen. */
    @FXML
    private TextField tfWortmeldungNameAbfragen;

    /** The tf wortmeldung kontaktdaten abfragen. */
    @FXML
    private TextField tfWortmeldungKontaktdatenAbfragen;

    /** The tf wortmeldung kontaktdaten E mail vorschlagen. */
    @FXML
    private TextField tfWortmeldungKontaktdatenEMailVorschlagen;

    /** The tf wortmeldung kontaktdaten telefon abfragen. */
    @FXML
    private TextField tfWortmeldungKontaktdatenTelefonAbfragen;

    /** The tf wortmeldung kurztext abfragen. */
    @FXML
    private TextField tfWortmeldungKurztextAbfragen;

    /** The tf wortmeldung top liste anbieten. */
    @FXML
    private TextField tfWortmeldungTopListeAnbieten;

    /** The tf wortmeldung langtext abfragen. */
    @FXML
    private TextField tfWortmeldungLangtextAbfragen;

    /** The tf wortmeldung zurueckziehen moeglich. */
    @FXML
    private TextField tfWortmeldungZurueckziehenMoeglich;

    /** The tf wortmeldung laenge. */
    @FXML
    private TextField tfWortmeldungLaenge;

    /** The tf wortmeldung anzahl je aktionaer. */
    @FXML
    private TextField tfWortmeldungAnzahlJeAktionaer;

    /** The tf wortmeldung steller zulaessig. */
    @FXML
    private TextField tfWortmeldungStellerZulaessig;

    /** The tf wortmeldung hinweis gelesen. */
    @FXML
    private TextField tfWortmeldungHinweisGelesen;

    /** The tf wortmeldung liste anzeigen. */
    @FXML
    private TextField tfWortmeldungListeAnzeigen;

    /** The tf wortmeldung mail bei eingang. */
    @FXML
    private TextField tfWortmeldungMailBeiEingang;

    /** The tf wortmeldung VL liste anzeigen. */
    @FXML
    private TextField tfWortmeldungVLListeAnzeigen;

    /** The tf wortmeldung mail verteiler 1. */
    @FXML
    private TextField tfWortmeldungMailVerteiler1;

    /** The tf wortmeldung mail verteiler 2. */
    @FXML
    private TextField tfWortmeldungMailVerteiler2;

    /** The tf wortmeldung mail verteiler 3. */
    @FXML
    private TextField tfWortmeldungMailVerteiler3;

    /** The tf wortmeldung hinweis vorbelegen mit. */
    @FXML
    private TextField tfWortmeldungHinweisVorbelegenMit;

    /** The tf wortmeldung test durchfuehren. */
    @FXML
    private TextField tfWortmeldungTestDurchfuehren;

    /** The tf wortmeldung rede aufruf zweiten versuch durchfuehren. */
    @FXML
    private TextField tfWortmeldungRedeAufrufZweitenVersuchDurchfuehren;

    /** The tf wortmeldung nach test manuell in rednerliste aufnehmen. */
    @FXML
    private TextField tfWortmeldungNachTestManuellInRednerlisteAufnehmen;

    /** The tf wortmeldung inhalts hinweise aktiv. */
    @FXML
    private TextField tfWortmeldungInhaltsHinweiseAktiv;

    /** The tf wortmeldetisch set nr. */
    @FXML
    private TextField tfWortmeldetischSetNr;

    /** The tf schriftgroesse versammlunsleiter view. */
    @FXML
    private TextField tfSchriftgroesseVersammlunsleiterView;

    /** The tp wiederprueche. */
    @FXML
    private Tab tpWiederprueche;

    /** The gp portal mitteilungen widersprueche. */
    @FXML
    private GridPane gpPortalMitteilungenWidersprueche;

    /** The tf widersprueche steller abfragen. */
    @FXML
    private TextField tfWiderspruecheStellerAbfragen;

    /** The tf widersprueche name abfragen. */
    @FXML
    private TextField tfWiderspruecheNameAbfragen;

    /** The tf widersprueche kontaktdaten abfragen. */
    @FXML
    private TextField tfWiderspruecheKontaktdatenAbfragen;

    /** The tf widersprueche kontaktdaten E mail vorschlagen. */
    @FXML
    private TextField tfWiderspruecheKontaktdatenEMailVorschlagen;

    /** The tf widersprueche kontaktdaten telefon abfragen. */
    @FXML
    private TextField tfWiderspruecheKontaktdatenTelefonAbfragen;

    /** The tf widersprueche kurztext abfragen. */
    @FXML
    private TextField tfWiderspruecheKurztextAbfragen;

    /** The tf widersprueche top liste anbieten. */
    @FXML
    private TextField tfWiderspruecheTopListeAnbieten;

    /** The tf widersprueche langtext abfragen. */
    @FXML
    private TextField tfWiderspruecheLangtextAbfragen;

    /** The tf widersprueche zurueckziehen moeglich. */
    @FXML
    private TextField tfWiderspruecheZurueckziehenMoeglich;

    /** The tf widersprueche laenge. */
    @FXML
    private TextField tfWiderspruecheLaenge;

    /** The tf widersprueche anzahl je aktionaer. */
    @FXML
    private TextField tfWiderspruecheAnzahlJeAktionaer;

    /** The tf widersprueche steller zulaessig. */
    @FXML
    private TextField tfWiderspruecheStellerZulaessig;

    /** The tf widersprueche hinweis gelesen. */
    @FXML
    private TextField tfWiderspruecheHinweisGelesen;

    /** The tf widersprueche mail bei eingang. */
    @FXML
    private TextField tfWiderspruecheMailBeiEingang;

    /** The tf widersprueche mail verteiler 1. */
    @FXML
    private TextField tfWiderspruecheMailVerteiler1;

    /** The tf widersprueche mail verteiler 2. */
    @FXML
    private TextField tfWiderspruecheMailVerteiler2;

    /** The tf widersprueche mail verteiler 3. */
    @FXML
    private TextField tfWiderspruecheMailVerteiler3;

    /** The tf widersprueche hinweis vorbelegen mit. */
    @FXML
    private TextField tfWiderspruecheHinweisVorbelegenMit;

    /** The tp antraege. */
    @FXML
    private Tab tpAntraege;

    /** The gp portal mitteilungen antraege. */
    @FXML
    private GridPane gpPortalMitteilungenAntraege;

    /** The tf antraege steller abfragen. */
    @FXML
    private TextField tfAntraegeStellerAbfragen;

    /** The tf antraege name abfragen. */
    @FXML
    private TextField tfAntraegeNameAbfragen;

    /** The tf antraege kontaktdaten abfragen. */
    @FXML
    private TextField tfAntraegeKontaktdatenAbfragen;

    /** The tf antraege kontaktdaten E mail vorschlagen. */
    @FXML
    private TextField tfAntraegeKontaktdatenEMailVorschlagen;

    /** The tf antraege kontaktdaten telefon abfragen. */
    @FXML
    private TextField tfAntraegeKontaktdatenTelefonAbfragen;

    /** The tf antraege kurztext abfragen. */
    @FXML
    private TextField tfAntraegeKurztextAbfragen;

    /** The tf antraege top liste anbieten. */
    @FXML
    private TextField tfAntraegeTopListeAnbieten;

    /** The tf antraege langtext abfragen. */
    @FXML
    private TextField tfAntraegeLangtextAbfragen;

    /** The tf antraege zurueckziehen moeglich. */
    @FXML
    private TextField tfAntraegeZurueckziehenMoeglich;

    /** The tf antraege laenge. */
    @FXML
    private TextField tfAntraegeLaenge;

    /** The tf antraege anzahl je aktionaer. */
    @FXML
    private TextField tfAntraegeAnzahlJeAktionaer;

    /** The tf antraege steller zulaessig. */
    @FXML
    private TextField tfAntraegeStellerZulaessig;

    /** The tf antraege hinweis gelesen. */
    @FXML
    private TextField tfAntraegeHinweisGelesen;

    /** The tf antraege mail bei eingang. */
    @FXML
    private TextField tfAntraegeMailBeiEingang;

    /** The tf antraege mail verteiler 1. */
    @FXML
    private TextField tfAntraegeMailVerteiler1;

    /** The tf antraege mail verteiler 2. */
    @FXML
    private TextField tfAntraegeMailVerteiler2;

    /** The tf antraege mail verteiler 3. */
    @FXML
    private TextField tfAntraegeMailVerteiler3;

    /** The tf antraege hinweis vorbelegen mit. */
    @FXML
    private TextField tfAntraegeHinweisVorbelegenMit;

    /** The tp sonst mitteilungen. */
    @FXML
    private Tab tpSonstMitteilungen;

    /** The gp portal mitteilungen sonst mitteilungen. */
    @FXML
    private GridPane gpPortalMitteilungenSonstMitteilungen;

    /** The tf sonst mitteilungen steller abfragen. */
    @FXML
    private TextField tfSonstMitteilungenStellerAbfragen;

    /** The tf sonst mitteilungen name abfragen. */
    @FXML
    private TextField tfSonstMitteilungenNameAbfragen;

    /** The tf sonst mitteilungen kontaktdaten abfragen. */
    @FXML
    private TextField tfSonstMitteilungenKontaktdatenAbfragen;

    /** The tf sonst mitteilungen kontaktdaten E mail vorschlagen. */
    @FXML
    private TextField tfSonstMitteilungenKontaktdatenEMailVorschlagen;

    /** The tf sonst mitteilungen kontaktdaten telefon abfragen. */
    @FXML
    private TextField tfSonstMitteilungenKontaktdatenTelefonAbfragen;

    /** The tf sonst mitteilungen kurztext abfragen. */
    @FXML
    private TextField tfSonstMitteilungenKurztextAbfragen;

    /** The tf sonst mitteilungen top liste anbieten. */
    @FXML
    private TextField tfSonstMitteilungenTopListeAnbieten;

    /** The tf sonst mitteilungen langtext abfragen. */
    @FXML
    private TextField tfSonstMitteilungenLangtextAbfragen;

    /** The tf sonst mitteilungen zurueckziehen moeglich. */
    @FXML
    private TextField tfSonstMitteilungenZurueckziehenMoeglich;

    /** The tf sonst mitteilungen laenge. */
    @FXML
    private TextField tfSonstMitteilungenLaenge;

    /** The tf sonst mitteilungen anzahl je aktionaer. */
    @FXML
    private TextField tfSonstMitteilungenAnzahlJeAktionaer;

    /** The tf sonst mitteilungen steller zulaessig. */
    @FXML
    private TextField tfSonstMitteilungenStellerZulaessig;

    /** The tf sonst mitteilungen hinweis gelesen. */
    @FXML
    private TextField tfSonstMitteilungenHinweisGelesen;

    /** The tf sonst mitteilungen mail bei eingang. */
    @FXML
    private TextField tfSonstMitteilungenMailBeiEingang;

    /** The tf sonst mitteilungen mail verteiler 1. */
    @FXML
    private TextField tfSonstMitteilungenMailVerteiler1;

    /** The tf sonst mitteilungen mail verteiler 2. */
    @FXML
    private TextField tfSonstMitteilungenMailVerteiler2;

    /** The tf sonst mitteilungen mail verteiler 3. */
    @FXML
    private TextField tfSonstMitteilungenMailVerteiler3;

    /** The tf sonst mitteilungen hinweis vorbelegen mit. */
    @FXML
    private TextField tfSonstMitteilungenHinweisVorbelegenMit;

    /** The tp botschaften. */
    @FXML
    private Tab tpBotschaften;

    /** The gp portal botschaften. */
    @FXML
    private GridPane gpPortalBotschaften;

    /** The tf botschaften steller abfragen. */
    @FXML
    private TextField tfBotschaftenStellerAbfragen;

    /** The tf botschaften name abfragen. */
    @FXML
    private TextField tfBotschaftenNameAbfragen;

    /** The tf botschaften kontaktdaten abfragen. */
    @FXML
    private TextField tfBotschaftenKontaktdatenAbfragen;

    /** The tf botschaften kontaktdaten E mail vorschlagen. */
    @FXML
    private TextField tfBotschaftenKontaktdatenEMailVorschlagen;

    /** The tf botschaften kontaktdaten telefon abfragen. */
    @FXML
    private TextField tfBotschaftenKontaktdatenTelefonAbfragen;

    /** The tf botschaften kurztext abfragen. */
    @FXML
    private TextField tfBotschaftenKurztextAbfragen;

    /** The tf botschaften top liste anbieten. */
    @FXML
    private TextField tfBotschaftenTopListeAnbieten;

    /** The tf botschaften langtext abfragen. */
    @FXML
    private TextField tfBotschaftenLangtextAbfragen;

    /** The tf botschaften zurueckziehen moeglich. */
    @FXML
    private TextField tfBotschaftenZurueckziehenMoeglich;

    /** The tf botschaften laenge. */
    @FXML
    private TextField tfBotschaftenLaenge;

    /** The tf botschaften anzahl je aktionaer. */
    @FXML
    private TextField tfBotschaftenAnzahlJeAktionaer;

    /** The tf botschaften langtext und datei nur alternativ. */
    @FXML
    private TextField tfBotschaftenLangtextUndDateiNurAlternativ;

    /** The tf botschaften hinweis gelesen. */
    @FXML
    private TextField tfBotschaftenHinweisGelesen;

    /** The tf botschaften steller zulaessig. */
    @FXML
    private TextField tfBotschaftenStellerZulaessig;

    /** The tf botschaften hinweis vorbelegen mit. */
    @FXML
    private TextField tfBotschaftenHinweisVorbelegenMit;

    /** The tf botschaften voranmeldung erforderlich. */
    @FXML
    private TextField tfBotschaftenVoranmeldungErforderlich;

    /** The tf botschaften mail bei eingang. */
    @FXML
    private TextField tfBotschaftenMailBeiEingang;

    /** The tf botschaften mail verteiler 1. */
    @FXML
    private TextField tfBotschaftenMailVerteiler1;

    /** The tf botschaften mail verteiler 2. */
    @FXML
    private TextField tfBotschaftenMailVerteiler2;

    /** The tf botschaften mail verteiler 3. */
    @FXML
    private TextField tfBotschaftenMailVerteiler3;

    /** The tf botschaften video. */
    @FXML
    private TextField tfBotschaftenVideo;

    /** The tf botschaften text datei. */
    @FXML
    private TextField tfBotschaftenTextDatei;

    /** The tf botschaften video laenge. */
    @FXML
    private TextField tfBotschaftenVideoLaenge;

    /** The tf botschaften text datei laenge. */
    @FXML
    private TextField tfBotschaftenTextDateiLaenge;

    /** The tf video botschaft project id. */
    @FXML
    private TextField tfVideoBotschaftProjectId;

    /** The tf video botschaft api key. */
    @FXML
    private TextField tfVideoBotschaftApiKey;

    /** The tp inhalts hinweise. */
    @FXML
    private Tab tpInhaltsHinweise;

    /** The gp portal mitteilungen inhalts hinweise. */
    @FXML
    private GridPane gpPortalMitteilungenInhaltsHinweise;

    /** The tp kontakt. */
    @FXML
    private Tab tpKontakt;

    /** The tf kontakt sonstige moeglichkeiten anbieten. */
    @FXML
    private TextField tfKontaktSonstigeMoeglichkeitenAnbieten;

    /** The tf kontaktformular aktiv. */
    @FXML
    private TextField tfKontaktformularAktiv;

    /** The tf kontaktformular bei eingang mail. */
    @FXML
    private TextField tfKontaktformularBeiEingangMail;

    /** The tf kontaktformular bei eingang mail an. */
    @FXML
    private TextField tfKontaktformularBeiEingangMailAn;

    /** The tf kontaktformular bei eingang mail inhalt aufnehmen. */
    @FXML
    private TextField tfKontaktformularBeiEingangMailInhaltAufnehmen;

    /** The tf kontaktformular bei eingang aufgabe. */
    @FXML
    private TextField tfKontaktformularBeiEingangAufgabe;

    /** The tf kontaktformular anzahl kontaktfelder. */
    @FXML
    private TextField tfKontaktformularAnzahlKontaktfelder;

    /** The tf kontaktformular telefon kontakt abfragen. */
    @FXML
    private TextField tfKontaktformularTelefonKontaktAbfragen;

    /** The tf kontaktformular mail kontakt abfragen. */
    @FXML
    private TextField tfKontaktformularMailKontaktAbfragen;

    /** The tf kontakt sonstige moeglichkeiten oben oder unten. */
    @FXML
    private TextField tfKontaktSonstigeMoeglichkeitenObenOderUnten;

    /** The tf kontaktformular thema anbieten. */
    @FXML
    private TextField tfKontaktformularThemaAnbieten;

    /** The tf kontaktformular themen liste global lokal. */
    @FXML
    private TextField tfKontaktformularThemenListeGlobalLokal;

    /** The tp virt. */
    @FXML
    private Tab tpVirt;

    /** The tf stream mit einmal key. */
    @FXML
    private TextField tfStreamMitEinmalKey;

    /** The tf stream link. */
    @FXML
    private TextField tfStreamLink;

    /** The tf stream ID. */
    @FXML
    private TextField tfStreamID;

    /** The tf timeout auf lang. */
    @FXML
    private TextField tfTimeoutAufLang;

    /** The tf stream mit einmal key 2. */
    @FXML
    private TextField tfStreamMitEinmalKey2;

    /** The tf stream link 2. */
    @FXML
    private TextField tfStreamLink2;

    /** The tf stream ID 2. */
    @FXML
    private TextField tfStreamID2;

    /** The tf websockets moeglich. */
    @FXML
    private TextField tfWebsocketsMoeglich;

    /** The tf doppel login gesperrt. */
    @FXML
    private TextField tfDoppelLoginGesperrt;

    /** The tf shrink format. */
    @FXML
    private TextField tfShrinkFormat;

    /** The tf shrink aufloesung. */
    @FXML
    private TextField tfShrinkAufloesung;

    /** The tp virt 2. */
    @FXML
    private Tab tpVirt2;

    /** The tf teilnehmerverz beginnend bei. */
    @FXML
    private TextField tfTeilnehmerverzBeginnendBei;

    /** The tf teilnehmerverz zusammenstellung. */
    @FXML
    private TextField tfTeilnehmerverzZusammenstellung;

    /** The tf teilnehmerverz letzte nr. */
    @FXML
    private TextField tfTeilnehmerverzLetzteNr;

    /** The tf abstimmungserg letzte nr. */
    @FXML
    private TextField tfAbstimmungsergLetzteNr;

    /** The tf zuschaltung HV automatisch nach login. */
    @FXML
    private TextField tfZuschaltungHVAutomatischNachLogin;

    @FXML
    private TextField tfZuschaltungHVStreamAutomatischStarten;

    /** The tpkonferenz. */
    @FXML
    private Tab tpkonferenz;

    /** The btn konf erstelle event. */
    @FXML
    private Button btnKonfErstelleEvent;

    /** The btn konf erstelle session 1. */
    @FXML
    private Button btnKonfErstelleSession1;

    /** The btn konf erstelle session 2. */
    @FXML
    private Button btnKonfErstelleSession2;

    /** The btn konf erstelle session 3. */
    @FXML
    private Button btnKonfErstelleSession3;

    /** The btn konf erstelle session 4. */
    @FXML
    private Button btnKonfErstelleSession4;

    /** The btn konf erstelle session 5. */
    @FXML
    private Button btnKonfErstelleSession5;

    /** The btn konf erstelle session 6. */
    @FXML
    private Button btnKonfErstelleSession6;

    @FXML
    private Button btnKonfErstelleSession7;

    @FXML
    private Button btnKonfErstelleSession8;

    @FXML
    private Button btnKonfErstelleSession9;

    @FXML
    private Button btnKonfErstelleSession10;

    @FXML
    private Button btnKonfErstelleSession11;

    @FXML
    private Button btnKonfErstelleSession12;

    /** The btn konf loesche session 1. */
    @FXML
    private Button btnKonfLoescheSession1;

    /** The btn konf loesche session 2. */
    @FXML
    private Button btnKonfLoescheSession2;

    /** The btn konf loesche session 3. */
    @FXML
    private Button btnKonfLoescheSession3;

    /** The btn konf loesche session 4. */
    @FXML
    private Button btnKonfLoescheSession4;

    /** The btn konf loesche session 5. */
    @FXML
    private Button btnKonfLoescheSession5;

    /** The btn konf loesche session 6. */
    @FXML
    private Button btnKonfLoescheSession6;

    @FXML
    private Button btnKonfLoescheSession7;

    @FXML
    private Button btnKonfLoescheSession8;

    @FXML
    private Button btnKonfLoescheSession9;

    @FXML
    private Button btnKonfLoescheSession10;

    @FXML
    private Button btnKonfLoescheSession11;

    @FXML
    private Button btnKonfLoescheSession12;

    /** The tf konf event id. */
    @FXML
    private TextField tfKonfEventId;

    /** The tf konf raum id T 1. */
    @FXML
    private TextField tfKonfRaumIdT1;

    /** The tf konf raum id T 2. */
    @FXML
    private TextField tfKonfRaumIdT2;

    /** The tf konf raum id T 3. */
    @FXML
    private TextField tfKonfRaumIdT3;

    @FXML
    private TextField tfKonfRaumIdT4;

    @FXML
    private TextField tfKonfRaumIdT5;

    @FXML
    private TextField tfKonfRaumIdT6;

    /** The tf konf raum id R 1. */
    @FXML
    private TextField tfKonfRaumIdR1;

    /** The tf konf raum id R 2. */
    @FXML
    private TextField tfKonfRaumIdR2;

    /** The tf konf raum id R 3. */
    @FXML
    private TextField tfKonfRaumIdR3;

    @FXML
    private TextField tfKonfRaumIdR4;

    @FXML
    private TextField tfKonfRaumIdR5;

    @FXML
    private TextField tfKonfRaumIdR6;

    /** The tf konf raum anzahl test. */
    @FXML
    private TextField tfKonfRaumAnzahlTest;

    /** The tf konf raum anzahl reden. */
    @FXML
    private TextField tfKonfRaumAnzahlReden;

    /** The tf konf globale testraeume nutzen. */
    @FXML
    private TextField tfKonfGlobaleTestraeumeNutzen;

    /** The cb konf payed event. */
    @FXML
    private CheckBox cbKonfPayedEvent;

    /** The tf konf backup server. */
    @FXML
    private TextField tfKonfBackupServer;

    /** The tf konf raum id BT 1. */
    @FXML
    private TextField tfKonfRaumIdBT1;

    /** The tf konf raum id BT 2. */
    @FXML
    private TextField tfKonfRaumIdBT2;

    /** The tf konf raum id BT 3. */
    @FXML
    private TextField tfKonfRaumIdBT3;

    @FXML
    private TextField tfKonfRaumIdBT4;

    @FXML
    private TextField tfKonfRaumIdBT5;

    @FXML
    private TextField tfKonfRaumIdBT6;

    /** The tf konf raum id BR 1. */
    @FXML
    private TextField tfKonfRaumIdBR1;

    /** The tf konf raum id BR 2. */
    @FXML
    private TextField tfKonfRaumIdBR2;

    /** The tf konf raum id BR 3. */
    @FXML
    private TextField tfKonfRaumIdBR3;

    @FXML
    private TextField tfKonfRaumIdBR4;

    @FXML
    private TextField tfKonfRaumIdBR5;

    @FXML
    private TextField tfKonfRaumIdBR6;

    /** The tf konf raum id BT 1 PW. */
    @FXML
    private TextField tfKonfRaumIdBT1PW;

    /** The tf konf raum id BT 2 PW. */
    @FXML
    private TextField tfKonfRaumIdBT2PW;

    /** The tf konf raum id BT 3 PW. */
    @FXML
    private TextField tfKonfRaumIdBT3PW;

    @FXML
    private TextField tfKonfRaumIdBT4PW;

    @FXML
    private TextField tfKonfRaumIdBT5PW;

    @FXML
    private TextField tfKonfRaumIdBT6PW;

    /** The tf konf raum id BR 1 PW. */
    @FXML
    private TextField tfKonfRaumIdBR1PW;

    /** The tf konf raum id BR 2 PW. */
    @FXML
    private TextField tfKonfRaumIdBR2PW;

    /** The tf konf raum id BR 3 PW. */
    @FXML
    private TextField tfKonfRaumIdBR3PW;

    @FXML
    private TextField tfKonfRaumIdBR4PW;

    @FXML
    private TextField tfKonfRaumIdBR5PW;

    @FXML
    private TextField tfKonfRaumIdBR6PW;

    /** The cb konf backup aktiv. */
    @FXML
    private CheckBox cbKonfBackupAktiv;

    /** The tp online HV. */
    @FXML
    private Tab tpOnlineHV;

    /** The tf online teilnehmer abfragen. */
    @FXML
    private TextField tfOnlineTeilnehmerAbfragen;

    /** The tf online teilnahme separate nutzungsbedingungen. */
    @FXML
    private TextField tfOnlineTeilnahmeSeparateNutzungsbedingungen;

    /** The tf online teilnahme aktionaer als gast. */
    @FXML
    private TextField tfOnlineTeilnahmeAktionaerAlsGast;

    /** The tp app. */
    @FXML
    private Tab tpApp;

    /** The tf impressum emittent. */
    @FXML
    private TextField tfImpressumEmittent;

    /** The tf anzeige startseite. */
    @FXML
    private TextField tfAnzeigeStartseite;

    /** The tf app aktiv. */
    @FXML
    private TextField tfAppAktiv;

    /** The tf app sprache. */
    @FXML
    private TextField tfAppSprache;

    /** The btn speichern. */
    @FXML
    private Button btnSpeichern;

    /** The btn beenden ohne speichern. */
    @FXML
    private Button btnBeendenOhneSpeichern;

    /** The btn konferenz only. */
    @FXML
    private Button btnKonferenzOnly;

    /** *************Ab hier individuelle Deklarationen******************. */

    /** The tf phasen namen. */
    private TextField[] tfPhasenNamen = null;

    /** The cb manuell aktiv. */
    private CheckBox[] cbManuellAktiv = null;

    /** The cb manuell deaktiv. */
    private CheckBox[] cbManuellDeaktiv = null;

    /** The cb gewinnspiel aktiv. */
    private CheckBox[] cbGewinnspielAktiv = null;

    /** The cb lfd HV portal in betrieb. */
    private CheckBox[] cbLfdHVPortalInBetrieb = null;

    /** The tf lfd vor der HV nach der HV. */
    private TextField[] tfLfdVorDerHVNachDerHV = null;

    /** The cb lfd HV portal erstanmeldung ist moeglich. */
    private CheckBox[] cbLfdHVPortalErstanmeldungIstMoeglich = null;

    /** The cb lfd HV portal EK ist moeglich. */
    private CheckBox[] cbLfdHVPortalEKIstMoeglich = null;

    /** The cb lfd HV portal SRV ist moeglich. */
    private CheckBox[] cbLfdHVPortalSRVIstMoeglich = null;

    /** The cb lfd HV portal briefwahl ist moeglich. */
    private CheckBox[] cbLfdHVPortalBriefwahlIstMoeglich = null;

    /** The cb lfd HV portal KIAV ist moeglich. */
    private CheckBox[] cbLfdHVPortalKIAVIstMoeglich = null;

    /** The cb lfd HV portal vollmacht dritte ist moeglich. */
    private CheckBox[] cbLfdHVPortalVollmachtDritteIstMoeglich = null;

    /** The cb lfd HV stream ist moeglich. */
    private CheckBox[] cbLfdHVStreamIstMoeglich = null;

    /** The cb lfd HV fragen ist moeglich. */
    private CheckBox[] cbLfdHVFragenIstMoeglich = null;

    /** The cb lfd HV rueckfragen ist moeglich. */
    private CheckBox[] cbLfdHVRueckfragenIstMoeglich = null;

    /** The cb lfd HV wortmeldungenen ist moeglich. */
    private CheckBox[] cbLfdHVWortmeldungenenIstMoeglich = null;

    /** The cb lfd HV wiedersprueche ist moeglich. */
    private CheckBox[] cbLfdHVWiederspruecheIstMoeglich = null;

    /** The cb lfd HV antraege ist moeglich. */
    private CheckBox[] cbLfdHVAntraegeIstMoeglich = null;

    /** The cb lfd HV sonstige mitteilungen ist moeglich. */
    private CheckBox[] cbLfdHVSonstigeMitteilungenIstMoeglich = null;

    /** The cb lfd HV botschaften einreichen ist moeglich. */
    private CheckBox[] cbLfdHVBotschaftenEinreichenIstMoeglich = null;

    /** The cb lfd HV botschaften ist moeglich. */
    private CheckBox[] cbLfdHVBotschaftenIstMoeglich = null;

    /** The cb lfd HV chat ist moeglich. */
    private CheckBox[] cbLfdHVChatIstMoeglich = null;

    /** The cb lfd HV unterlagen 1 ist moeglich. */
    private CheckBox[] cbLfdHVUnterlagen1IstMoeglich = null;

    /** The cb lfd HV unterlagen 2 ist moeglich. */
    private CheckBox[] cbLfdHVUnterlagen2IstMoeglich = null;

    /** The cb lfd HV unterlagen 3 ist moeglich. */
    private CheckBox[] cbLfdHVUnterlagen3IstMoeglich = null;

    /** The cb lfd HV unterlagen 4 ist moeglich. */
    private CheckBox[] cbLfdHVUnterlagen4IstMoeglich = null;

    /** The cb lfd HV unterlagen 5 ist moeglich. */
    private CheckBox[] cbLfdHVUnterlagen5IstMoeglich = null;

    /** The cb lfd HV teilnehmerverz ist moeglich. */
    private CheckBox[] cbLfdHVTeilnehmerverzIstMoeglich = null;

    /** The cb lfd HV abstimmungserg ist moeglich. */
    private CheckBox[] cbLfdHVAbstimmungsergIstMoeglich = null;

    /*Portalfunktionen*/

    /** The lb funkt namen. */
    private Label[] lbFunktNamen = null;

    /** The cb funkt wird angeboten. */
    private CheckBox[] cbFunktWirdAngeboten = null;

    /** The tf funkt aktiv. */
    private TextField[] tfFunktAktiv = null;

    /** The cb funkt gast 1. */
    private CheckBox[] cbFunktGast1 = null;

    /** The cb funkt gast 2. */
    private CheckBox[] cbFunktGast2 = null;

    /** The cb funkt gast 3. */
    private CheckBox[] cbFunktGast3 = null;

    /** The cb funkt gast 4. */
    private CheckBox[] cbFunktGast4 = null;

    /** The cb funkt gast 5. */
    private CheckBox[] cbFunktGast5 = null;

    /** The cb funkt gast 6. */
    private CheckBox[] cbFunktGast6 = null;

    /** The cb funkt gast 7. */
    private CheckBox[] cbFunktGast7 = null;

    /** The cb funkt gast 8. */
    private CheckBox[] cbFunktGast8 = null;

    /** The cb funkt gast 9. */
    private CheckBox[] cbFunktGast9 = null;

    /** The cb funkt gast 10. */
    private CheckBox[] cbFunktGast10 = null;

    /** The cb funkt gast 1 OT. */
    private CheckBox[] cbFunktGast1OT = null;

    /** The cb funkt gast 2 OT. */
    private CheckBox[] cbFunktGast2OT = null;

    /** The cb funkt gast 3 OT. */
    private CheckBox[] cbFunktGast3OT = null;

    /** The cb funkt gast 4 OT. */
    private CheckBox[] cbFunktGast4OT = null;

    /** The cb funkt gast 5 OT. */
    private CheckBox[] cbFunktGast5OT = null;

    /** The cb funkt gast 6 OT. */
    private CheckBox[] cbFunktGast6OT = null;

    /** The cb funkt gast 7 OT. */
    private CheckBox[] cbFunktGast7OT = null;

    /** The cb funkt gast 8 OT. */
    private CheckBox[] cbFunktGast8OT = null;

    /** The cb funkt gast 9 OT. */
    private CheckBox[] cbFunktGast9OT = null;

    /** The cb funkt gast 10 OT. */
    private CheckBox[] cbFunktGast10OT = null;

    /** The cb funkt akt. */
    private CheckBox[] cbFunktAkt = null;

    /** The cb funkt akt ang. */
    private CheckBox[] cbFunktAktAng = null;

    /** The cb funkt akt OT. */
    private CheckBox[] cbFunktAktOT = null;

    /** The tf unter login oben. */
    /*PortalUnterlagen*/
    private List<TextField> tfUnterLoginOben = null;

    /** The tf unter login unten. */
    private List<TextField> tfUnterLoginUnten = null;

    /** The tf unter extern. */
    private List<TextField> tfUnterExtern = null;

    /** The tf unter unterlagen. */
    private List<TextField> tfUnterUnterlagen = null;

    /** The tf unter botschaften. */
    private List<TextField> tfUnterBotschaften = null;

    /** The tf unter aktiv. */
    private List<TextField> tfUnterAktiv = null;

    /** The tf unter art. */
    private List<TextField> tfUnterArt = null;

    /** The tf unter art style. */
    private List<TextField> tfUnterArtStyle = null;

    /** The tf unter preview login. */
    private List<TextField> tfUnterPreviewLogin = null;

    /** The tf unter preview extern. */
    private List<TextField> tfUnterPreviewExtern = null;

    /** The tf unter preview intern. */
    private List<TextField> tfUnterPreviewIntern = null;

    /** The tf unter dateiname. */
    private List<TextArea> tfUnterDateiname = null;

    /** The tf unter datei auf englisch. */
    private List<TextField> tfUnterDateiAufEnglisch = null;

    /** The cb unter gast 1. */
    private List<CheckBox> cbUnterGast1 = null;

    /** The cb unter gast 2. */
    private List<CheckBox> cbUnterGast2 = null;

    /** The cb unter gast 3. */
    private List<CheckBox> cbUnterGast3 = null;

    /** The cb unter gast 4. */
    private List<CheckBox> cbUnterGast4 = null;

    /** The cb unter gast 5. */
    private List<CheckBox> cbUnterGast5 = null;

    /** The cb unter gast 6. */
    private List<CheckBox> cbUnterGast6 = null;

    /** The cb unter gast 7. */
    private List<CheckBox> cbUnterGast7 = null;

    /** The cb unter gast 8. */
    private List<CheckBox> cbUnterGast8 = null;

    /** The cb unter gast 9. */
    private List<CheckBox> cbUnterGast9 = null;

    /** The cb unter gast 10. */
    private List<CheckBox> cbUnterGast10 = null;

    /** The cb unter gast 1 OT. */
    private List<CheckBox> cbUnterGast1OT = null;

    /** The cb unter gast 2 OT. */
    private List<CheckBox> cbUnterGast2OT = null;

    /** The cb unter gast 3 OT. */
    private List<CheckBox> cbUnterGast3OT = null;

    /** The cb unter gast 4 OT. */
    private List<CheckBox> cbUnterGast4OT = null;

    /** The cb unter gast 5 OT. */
    private List<CheckBox> cbUnterGast5OT = null;

    /** The cb unter gast 6 OT. */
    private List<CheckBox> cbUnterGast6OT = null;

    /** The cb unter gast 7 OT. */
    private List<CheckBox> cbUnterGast7OT = null;

    /** The cb unter gast 8 OT. */
    private List<CheckBox> cbUnterGast8OT = null;

    /** The cb unter gast 9 OT. */
    private List<CheckBox> cbUnterGast9OT = null;

    /** The cb unter gast 10 OT. */
    private List<CheckBox> cbUnterGast10OT = null;

    /** The cb unter akt. */
    private List<CheckBox> cbUnterAkt = null;

    /** The cb unter akt ang. */
    private List<CheckBox> cbUnterAktAng = null;

    /** The cb unter akt OT. */
    private List<CheckBox> cbUnterAktOT = null;

    /** The lb unter text DE. */
    private List<TextArea> lbUnterTextDE = null;

    /** The lb unter text EN. */
    private List<TextArea> lbUnterTextEN = null;

    /** The btn unterlagen neu. */
    private Button btnUnterlagenNeu = null;

    /** Botschaften. */
    private Label[] lbBotschaftenVideoFormate = new Label[16];

    /** The lb botschaften text formate. */
    private Label[] lbBotschaftenTextFormate = new Label[16];

    /** The tf botschaften video formate. */
    private TextField[] tfBotschaftenVideoFormate = new TextField[16];

    /** The tf botschaften text formate. */
    private TextField[] tfBotschaftenTextFormate = new TextField[16];

    /** Inhalts-Hinweise. */
    private Label[] lbInhaltsHinweiseNr = null;

    /** The tf inhalts hinweise text DE. */
    private TextField[] tfInhaltsHinweiseTextDE = null;

    /** The tf inhalts hinweise text EN. */
    private TextField[] tfInhaltsHinweiseTextEN = null;

    /** The cb inhalts hinweise aktiv. */
    private CheckBox[][] cbInhaltsHinweiseAktiv = null;

    /**
     * Initialize.
     */
    @FXML
    void initialize() {

        assert tbPaneAnnzeige != null : "fx:id=\"tbPaneAnnzeige\" was not injected: check your FXML file 'ParameterPortalApp.fxml'.";
        assert tpAllgemein != null : "fx:id=\"tpAllgemein\" was not injected: check your FXML file 'ParameterPortalApp.fxml'.";
        assert tfPortalAuchInEnglischVerfuegbar != null : "fx:id=\"tfPortalAuchInEnglischVerfuegbar\" was not injected: check your FXML file 'ParameterPortalApp.fxml'.";
        assert tfDatumsformatDE != null : "fx:id=\"tfDatumsformatDE\" was not injected: check your FXML file 'ParameterPortalApp.fxml'.";
        assert tfDatumsformatEN != null : "fx:id=\"tfDatumsformatEN\" was not injected: check your FXML file 'ParameterPortalApp.fxml'.";
        assert tfLetzterAktienregisterUpdate != null : "fx:id=\"tfLetzterAktienregisterUpdate\" was not injected: check your FXML file 'ParameterPortalApp.fxml'.";
        assert tfArtSprachumschaltung != null : "fx:id=\"tfArtSprachumschaltung\" was not injected: check your FXML file 'ParameterPortalApp.fxml'.";
        assert tfBestaetigenDialog != null : "fx:id=\"tfBestaetigenDialog\" was not injected: check your FXML file 'ParameterPortalApp.fxml'.";
        assert tfQuittungDialog != null : "fx:id=\"tfQuittungDialog\" was not injected: check your FXML file 'ParameterPortalApp.fxml'.";
        assert tfKontaktFenster != null : "fx:id=\"tfKontaktFenster\" was not injected: check your FXML file 'ParameterPortalApp.fxml'.";
        assert tfPersonenAnzeigeAnredeMitAufnahmen != null : "fx:id=\"tfPersonenAnzeigeAnredeMitAufnahmen\" was not injected: check your FXML file 'ParameterPortalApp.fxml'.";
        assert tfTextPostfachMitAufnahmen != null : "fx:id=\"tfTextPostfachMitAufnahmen\" was not injected: check your FXML file 'ParameterPortalApp.fxml'.";
        assert tfPortalAktuellAktiv != null : "fx:id=\"tfPortalAktuellAktiv\" was not injected: check your FXML file 'ParameterPortalApp.fxml'.";
        assert tfPortalStandard != null : "fx:id=\"tfPortalStandard\" was not injected: check your FXML file 'ParameterPortalApp.fxml'.";
        assert tfTestModus != null : "fx:id=\"tfTestModus\" was not injected: check your FXML file 'ParameterPortalApp.fxml'.";
        assert tpLogin != null : "fx:id=\"tpLogin\" was not injected: check your FXML file 'ParameterPortalApp.fxml'.";
        assert tfAppInstallButtonsAnzeigen != null : "fx:id=\"tfAppInstallButtonsAnzeigen\" was not injected: check your FXML file 'ParameterPortalApp.fxml'.";
        assert tfPasswortCaseSensitiv != null : "fx:id=\"tfPasswortCaseSensitiv\" was not injected: check your FXML file 'ParameterPortalApp.fxml'.";
        assert tfPasswortMindestLaenge != null : "fx:id=\"tfPasswortMindestLaenge\" was not injected: check your FXML file 'ParameterPortalApp.fxml'.";
        assert tfVerfahrenPasswortVergessen != null : "fx:id=\"tfVerfahrenPasswortVergessen\" was not injected: check your FXML file 'ParameterPortalApp.fxml'.";
        assert cbGattung1Moeglich != null : "fx:id=\"cbGattung1Moeglich\" was not injected: check your FXML file 'ParameterPortalApp.fxml'.";
        assert cbGattung2Moeglich != null : "fx:id=\"cbGattung2Moeglich\" was not injected: check your FXML file 'ParameterPortalApp.fxml'.";
        assert cbGattung3Moeglich != null : "fx:id=\"cbGattung3Moeglich\" was not injected: check your FXML file 'ParameterPortalApp.fxml'.";
        assert cbGattung4Moeglich != null : "fx:id=\"cbGattung4Moeglich\" was not injected: check your FXML file 'ParameterPortalApp.fxml'.";
        assert cbGattung5Moeglich != null : "fx:id=\"cbGattung5Moeglich\" was not injected: check your FXML file 'ParameterPortalApp.fxml'.";
        assert tfLoginGesperrt != null : "fx:id=\"tfLoginGesperrt\" was not injected: check your FXML file 'ParameterPortalApp.fxml'.";
        assert tfLoginGesperrtTextDeutsch != null : "fx:id=\"tfLoginGesperrtTextDeutsch\" was not injected: check your FXML file 'ParameterPortalApp.fxml'.";
        assert tfLoginGesperrtTextEnglisch != null : "fx:id=\"tfLoginGesperrtTextEnglisch\" was not injected: check your FXML file 'ParameterPortalApp.fxml'.";
        assert tfLoginIPTracking != null : "fx:id=\"tfLoginIPTracking\" was not injected: check your FXML file 'ParameterPortalApp.fxml'.";
        assert tfPasswortPerPostPruefen != null : "fx:id=\"tfPasswortPerPostPruefen\" was not injected: check your FXML file 'ParameterPortalApp.fxml'.";
        assert tfLogoutZiel != null : "fx:id=\"tfLogoutZiel\" was not injected: check your FXML file 'ParameterPortalApp.fxml'.";
        assert tfLoginVerfahren != null : "fx:id=\"tfLoginVerfahren\" was not injected: check your FXML file 'ParameterPortalApp.fxml'.";
        assert tpRegistrierung != null : "fx:id=\"tpRegistrierung\" was not injected: check your FXML file 'ParameterPortalApp.fxml'.";
        assert tfDauerhaftesPasswortMoeglich != null : "fx:id=\"tfDauerhaftesPasswortMoeglich\" was not injected: check your FXML file 'ParameterPortalApp.fxml'.";
        assert tfRegistrierungFuerEmailVersandMoeglich != null : "fx:id=\"tfRegistrierungFuerEmailVersandMoeglich\" was not injected: check your FXML file 'ParameterPortalApp.fxml'.";
        assert tfAdressaenderungMoeglich != null : "fx:id=\"tfAdressaenderungMoeglich\" was not injected: check your FXML file 'ParameterPortalApp.fxml'.";
        assert tfKommunikationsspracheAuswahl != null : "fx:id=\"tfKommunikationsspracheAuswahl\" was not injected: check your FXML file 'ParameterPortalApp.fxml'.";
        assert tfPublikationenAnbieten != null : "fx:id=\"tfPublikationenAnbieten\" was not injected: check your FXML file 'ParameterPortalApp.fxml'.";
        assert tfKontaktDetailsAnbieten != null : "fx:id=\"tfKontaktDetailsAnbieten\" was not injected: check your FXML file 'ParameterPortalApp.fxml'.";
        assert tfSeparateTeilnahmebedingungenFuerGewinnspiel != null : "fx:id=\"tfSeparateTeilnahmebedingungenFuerGewinnspiel\" was not injected: check your FXML file 'ParameterPortalApp.fxml'.";
        assert tfSeparateDatenschutzerklaerung != null : "fx:id=\"tfSeparateDatenschutzerklaerung\" was not injected: check your FXML file 'ParameterPortalApp.fxml'.";
        assert tfBestaetigenHinweisAktionaersportal != null : "fx:id=\"tfBestaetigenHinweisAktionaersportal\" was not injected: check your FXML file 'ParameterPortalApp.fxml'.";
        assert tfBestaetigenHinweisHVportal != null : "fx:id=\"tfBestaetigenHinweisHVportal\" was not injected: check your FXML file 'ParameterPortalApp.fxml'.";
        assert tfEmailNurBeiEVersandOderPasswort != null : "fx:id=\"tfEmailNurBeiEVersandOderPasswort\" was not injected: check your FXML file 'ParameterPortalApp.fxml'.";
        assert tfBestaetigungsseiteEinstellungen != null : "fx:id=\"tfBestaetigungsseiteEinstellungen\" was not injected: check your FXML file 'ParameterPortalApp.fxml'.";
        assert tfAbsendeMailAdresse != null : "fx:id=\"tfAbsendeMailAdresse\" was not injected: check your FXML file 'ParameterPortalApp.fxml'.";
        assert tfReihenfolgeRegistrierung != null : "fx:id=\"tfReihenfolgeRegistrierung\" was not injected: check your FXML file 'ParameterPortalApp.fxml'.";
        assert tpErklaerungen != null : "fx:id=\"tpErklaerungen\" was not injected: check your FXML file 'ParameterPortalApp.fxml'.";
        assert tfBriefwahlAngeboten != null : "fx:id=\"tfBriefwahlAngeboten\" was not injected: check your FXML file 'ParameterPortalApp.fxml'.";
        assert tfVollmachtDritteAngeboten != null : "fx:id=\"tfVollmachtDritteAngeboten\" was not injected: check your FXML file 'ParameterPortalApp.fxml'.";
        assert tfVollmachtKIAVAngeboten != null : "fx:id=\"tfVollmachtKIAVAngeboten\" was not injected: check your FXML file 'ParameterPortalApp.fxml'.";
        assert tfGastkartenAnforderungMoeglich != null : "fx:id=\"tfGastkartenAnforderungMoeglich\" was not injected: check your FXML file 'ParameterPortalApp.fxml'.";
        assert tfOeffentlicheIDMoeglich != null : "fx:id=\"tfOeffentlicheIDMoeglich\" was not injected: check your FXML file 'ParameterPortalApp.fxml'.";
        assert tfZusaetzlicheEKDritteMoeglich != null : "fx:id=\"tfZusaetzlicheEKDritteMoeglich\" was not injected: check your FXML file 'ParameterPortalApp.fxml'.";
        assert tfEkUndWeisungGleichzeitigMoeglich != null : "fx:id=\"tfEkUndWeisungGleichzeitigMoeglich\" was not injected: check your FXML file 'ParameterPortalApp.fxml'.";
        assert tfEkSelbstMoeglich != null : "fx:id=\"tfEkSelbstMoeglich\" was not injected: check your FXML file 'ParameterPortalApp.fxml'.";
        assert tfEkVollmachtMoeglich != null : "fx:id=\"tfEkVollmachtMoeglich\" was not injected: check your FXML file 'ParameterPortalApp.fxml'.";
        assert tfEk2PersonengemeinschaftMoeglich != null : "fx:id=\"tfEk2PersonengemeinschaftMoeglich\" was not injected: check your FXML file 'ParameterPortalApp.fxml'.";
        assert tfEk2MitOderOhneVollmachtMoeglich != null : "fx:id=\"tfEk2MitOderOhneVollmachtMoeglich\" was not injected: check your FXML file 'ParameterPortalApp.fxml'.";
        assert tfEk2SelbstMoeglich != null : "fx:id=\"tfEk2SelbstMoeglich\" was not injected: check your FXML file 'ParameterPortalApp.fxml'.";
        assert tfSrvAngeboten != null : "fx:id=\"tfSrvAngeboten\" was not injected: check your FXML file 'ParameterPortalApp.fxml'.";
        assert tfBriefwahlZusaetzlichZuSRVMoeglich != null : "fx:id=\"tfBriefwahlZusaetzlichZuSRVMoeglich\" was not injected: check your FXML file 'ParameterPortalApp.fxml'.";
        assert tfAnmeldenOhneWeitereWK != null : "fx:id=\"tfAnmeldenOhneWeitereWK\" was not injected: check your FXML file 'ParameterPortalApp.fxml'.";
        assert tpWeisung != null : "fx:id=\"tpWeisung\" was not injected: check your FXML file 'ParameterPortalApp.fxml'.";
        assert tfPNichtmarkiertSpeichernAls != null : "fx:id=\"tfPNichtmarkiertSpeichernAls\" was not injected: check your FXML file 'ParameterPortalApp.fxml'.";
        assert tfGegenantraegeWeisungenMoeglich != null : "fx:id=\"tfGegenantraegeWeisungenMoeglich\" was not injected: check your FXML file 'ParameterPortalApp.fxml'.";
        assert tfGegenantragsText != null : "fx:id=\"tfGegenantragsText\" was not injected: check your FXML file 'ParameterPortalApp.fxml'.";
        assert cbGesamtMarkierungJa != null : "fx:id=\"cbGesamtMarkierungJa\" was not injected: check your FXML file 'ParameterPortalApp.fxml'.";
        assert cbGesamtMarkierungNein != null : "fx:id=\"cbGesamtMarkierungNein\" was not injected: check your FXML file 'ParameterPortalApp.fxml'.";
        assert cbGesamtMarkierungEnthaltung != null : "fx:id=\"cbGesamtMarkierungEnthaltung\" was not injected: check your FXML file 'ParameterPortalApp.fxml'.";
        assert cbGesamtMarkierungImSinne != null : "fx:id=\"cbGesamtMarkierungImSinne\" was not injected: check your FXML file 'ParameterPortalApp.fxml'.";
        assert cbGesamtMarkierungGegenSinne != null : "fx:id=\"cbGesamtMarkierungGegenSinne\" was not injected: check your FXML file 'ParameterPortalApp.fxml'.";
        assert cbGesamtMarkierungAllesLoeschen != null : "fx:id=\"cbGesamtMarkierungAllesLoeschen\" was not injected: check your FXML file 'ParameterPortalApp.fxml'.";
        assert cbMarkierungJa != null : "fx:id=\"cbMarkierungJa\" was not injected: check your FXML file 'ParameterPortalApp.fxml'.";
        assert cbMarkierungNein != null : "fx:id=\"cbMarkierungNein\" was not injected: check your FXML file 'ParameterPortalApp.fxml'.";
        assert cbMarkierungEnthaltung != null : "fx:id=\"cbMarkierungEnthaltung\" was not injected: check your FXML file 'ParameterPortalApp.fxml'.";
        assert cbMarkierungLoeschen != null : "fx:id=\"cbMarkierungLoeschen\" was not injected: check your FXML file 'ParameterPortalApp.fxml'.";
        assert cbCheckboxBeiSRV != null : "fx:id=\"cbCheckboxBeiSRV\" was not injected: check your FXML file 'ParameterPortalApp.fxml'.";
        assert cbCheckboxBeiBriefwahl != null : "fx:id=\"cbCheckboxBeiBriefwahl\" was not injected: check your FXML file 'ParameterPortalApp.fxml'.";
        assert cbCheckboxBeiKIAV != null : "fx:id=\"cbCheckboxBeiKIAV\" was not injected: check your FXML file 'ParameterPortalApp.fxml'.";
        assert tfBestaetigungBeiWeisung != null : "fx:id=\"tfBestaetigungBeiWeisung\" was not injected: check your FXML file 'ParameterPortalApp.fxml'.";
        assert tpTextInhalte != null : "fx:id=\"tpTextInhalte\" was not injected: check your FXML file 'ParameterPortalApp.fxml'.";
        assert tfStandardTexteBeruecksichtigen != null : "fx:id=\"tfStandardTexteBeruecksichtigen\" was not injected: check your FXML file 'ParameterPortalApp.fxml'.";
        assert tfMehrereStimmrechtsvertreter != null : "fx:id=\"tfMehrereStimmrechtsvertreter\" was not injected: check your FXML file 'ParameterPortalApp.fxml'.";
        assert tfStimmrechtsvertreterNameDE != null : "fx:id=\"tfStimmrechtsvertreterNameDE\" was not injected: check your FXML file 'ParameterPortalApp.fxml'.";
        assert tfStimmrechtsvertreterNameEN != null : "fx:id=\"tfStimmrechtsvertreterNameEN\" was not injected: check your FXML file 'ParameterPortalApp.fxml'.";
        assert tfLinkTagesordnung != null : "fx:id=\"tfLinkTagesordnung\" was not injected: check your FXML file 'ParameterPortalApp.fxml'.";
        assert tfLinkGegenantraege != null : "fx:id=\"tfLinkGegenantraege\" was not injected: check your FXML file 'ParameterPortalApp.fxml'.";
        assert tfLinkEinladungsPDF != null : "fx:id=\"tfLinkEinladungsPDF\" was not injected: check your FXML file 'ParameterPortalApp.fxml'.";
        assert tfEmailAdresseLink != null : "fx:id=\"tfEmailAdresseLink\" was not injected: check your FXML file 'ParameterPortalApp.fxml'.";
        assert tfEmailAdresseText != null : "fx:id=\"tfEmailAdresseText\" was not injected: check your FXML file 'ParameterPortalApp.fxml'.";
        assert tfBasisSetStandardTexteVerwenden != null : "fx:id=\"tfBasisSetStandardTexteVerwenden\" was not injected: check your FXML file 'ParameterPortalApp.fxml'.";
        assert tfLinkNutzungsbedingungenAktionaersPortal != null : "fx:id=\"tfLinkNutzungsbedingungenAktionaersPortal\" was not injected: check your FXML file 'ParameterPortalApp.fxml'.";
        assert tfLinkNutzungsbedingungenHVPortal != null : "fx:id=\"tfLinkNutzungsbedingungenHVPortal\" was not injected: check your FXML file 'ParameterPortalApp.fxml'.";
        assert tfLinkDatenschutzhinweise != null : "fx:id=\"tfLinkDatenschutzhinweise\" was not injected: check your FXML file 'ParameterPortalApp.fxml'.";
        assert tfLinkImpressum != null : "fx:id=\"tfLinkImpressum\" was not injected: check your FXML file 'ParameterPortalApp.fxml'.";
        assert tfLogoBreite != null : "fx:id=\"tfLogoBreite\" was not injected: check your FXML file 'ParameterPortalApp.fxml'.";
        assert tfLogoName != null : "fx:id=\"tfLogoName\" was not injected: check your FXML file 'ParameterPortalApp.fxml'.";
        assert tfCSSName != null : "fx:id=\"tfCSSName\" was not injected: check your FXML file 'ParameterPortalApp.fxml'.";
        assert tfLinkDatenschutzhinweiseKunde != null : "fx:id=\"tfLinkDatenschutzhinweiseKunde\" was not injected: check your FXML file 'ParameterPortalApp.fxml'.";
        assert tfEkText != null : "fx:id=\"tfEkText\" was not injected: check your FXML file 'ParameterPortalApp.fxml'.";
        assert tfEkTextMitArtikel != null : "fx:id=\"tfEkTextMitArtikel\" was not injected: check your FXML file 'ParameterPortalApp.fxml'.";
        assert tfEkTextEN != null : "fx:id=\"tfEkTextEN\" was not injected: check your FXML file 'ParameterPortalApp.fxml'.";
        assert ekTextENMitArtikel != null : "fx:id=\"ekTextENMitArtikel\" was not injected: check your FXML file 'ParameterPortalApp.fxml'.";
        assert tfDesignKuerzel != null : "fx:id=\"tfDesignKuerzel\" was not injected: check your FXML file 'ParameterPortalApp.fxml'.";
        assert tfLogoutObenOderUnten != null : "fx:id=\"tfLogoutObenOderUnten\" was not injected: check your FXML file 'ParameterPortalApp.fxml'.";
        assert tpPhasen != null : "fx:id=\"tpPhasen\" was not injected: check your FXML file 'ParameterPortalApp.fxml'.";
        assert gpPhasen != null : "fx:id=\"gpPhasen\" was not injected: check your FXML file 'ParameterPortalApp.fxml'.";
        assert tpPortalFunktionen != null : "fx:id=\"tpPortalFunktionen\" was not injected: check your FXML file 'ParameterPortalApp.fxml'.";
        assert gpPortalFunktionen != null : "fx:id=\"gpPortalFunktionen\" was not injected: check your FXML file 'ParameterPortalApp.fxml'.";
        assert tpPortalUnterlagen != null : "fx:id=\"tpPortalUnterlagen\" was not injected: check your FXML file 'ParameterPortalApp.fxml'.";
        assert gpPortalUnterlagen != null : "fx:id=\"gpPortalUnterlagen\" was not injected: check your FXML file 'ParameterPortalApp.fxml'.";
        assert tpPortalMitteilungen != null : "fx:id=\"tpPortalMitteilungen\" was not injected: check your FXML file 'ParameterPortalApp.fxml'.";
        assert tfFragenStellerAbfragen != null : "fx:id=\"tfFragenStellerAbfragen\" was not injected: check your FXML file 'ParameterPortalApp.fxml'.";
        assert tfFragenNameAbfragen != null : "fx:id=\"tfFragenNameAbfragen\" was not injected: check your FXML file 'ParameterPortalApp.fxml'.";
        assert tfFragenKontaktdatenAbfragen != null : "fx:id=\"tfFragenKontaktdatenAbfragen\" was not injected: check your FXML file 'ParameterPortalApp.fxml'.";
        assert tfFragenKontaktdatenEMailVorschlagen != null : "fx:id=\"tfFragenKontaktdatenEMailVorschlagen\" was not injected: check your FXML file 'ParameterPortalApp.fxml'.";
        assert tfFragenKurztextAbfragen != null : "fx:id=\"tfFragenKurztextAbfragen\" was not injected: check your FXML file 'ParameterPortalApp.fxml'.";
        assert tfFragenTopListeAnbieten != null : "fx:id=\"tfFragenTopListeAnbieten\" was not injected: check your FXML file 'ParameterPortalApp.fxml'.";
        assert tfFragenLangtextAbfragen != null : "fx:id=\"tfFragenLangtextAbfragen\" was not injected: check your FXML file 'ParameterPortalApp.fxml'.";
        assert tfFragenZurueckziehenMoeglich != null : "fx:id=\"tfFragenZurueckziehenMoeglich\" was not injected: check your FXML file 'ParameterPortalApp.fxml'.";
        assert tfFragenLaenge != null : "fx:id=\"tfFragenLaenge\" was not injected: check your FXML file 'ParameterPortalApp.fxml'.";
        assert tfFragenAnzahlJeAktionaer != null : "fx:id=\"tfFragenAnzahlJeAktionaer\" was not injected: check your FXML file 'ParameterPortalApp.fxml'.";
        assert tfFragenStellerZulaessig != null : "fx:id=\"tfFragenStellerZulaessig\" was not injected: check your FXML file 'ParameterPortalApp.fxml'.";
        assert tfFragenMailBeiEingang != null : "fx:id=\"tfFragenMailBeiEingang\" was not injected: check your FXML file 'ParameterPortalApp.fxml'.";
        assert tfFragenExternerZugriff != null : "fx:id=\"tfFragenExternerZugriff\" was not injected: check your FXML file 'ParameterPortalApp.fxml'.";
        assert tfFragenMailVerteiler1 != null : "fx:id=\"tfFragenMailVerteiler1\" was not injected: check your FXML file 'ParameterPortalApp.fxml'.";
        assert tfFragenMailVerteiler2 != null : "fx:id=\"tfFragenMailVerteiler2\" was not injected: check your FXML file 'ParameterPortalApp.fxml'.";
        assert tfFragenMailVerteiler3 != null : "fx:id=\"tfFragenMailVerteiler3\" was not injected: check your FXML file 'ParameterPortalApp.fxml'.";
        assert tfWortmeldungStellerAbfragen != null : "fx:id=\"tfWortmeldungStellerAbfragen\" was not injected: check your FXML file 'ParameterPortalApp.fxml'.";
        assert tfWortmeldungNameAbfragen != null : "fx:id=\"tfWortmeldungNameAbfragen\" was not injected: check your FXML file 'ParameterPortalApp.fxml'.";
        assert tfWortmeldungKontaktdatenAbfragen != null : "fx:id=\"tfWortmeldungKontaktdatenAbfragen\" was not injected: check your FXML file 'ParameterPortalApp.fxml'.";
        assert tfWortmeldungKontaktdatenEMailVorschlagen != null : "fx:id=\"tfWortmeldungKontaktdatenEMailVorschlagen\" was not injected: check your FXML file 'ParameterPortalApp.fxml'.";
        assert tfWortmeldungKurztextAbfragen != null : "fx:id=\"tfWortmeldungKurztextAbfragen\" was not injected: check your FXML file 'ParameterPortalApp.fxml'.";
        assert tfWortmeldungTopListeAnbieten != null : "fx:id=\"tfWortmeldungTopListeAnbieten\" was not injected: check your FXML file 'ParameterPortalApp.fxml'.";
        assert tfWortmeldungLangtextAbfragen != null : "fx:id=\"tfWortmeldungLangtextAbfragen\" was not injected: check your FXML file 'ParameterPortalApp.fxml'.";
        assert tfWortmeldungZurueckziehenMoeglich != null : "fx:id=\"tfWortmeldungZurueckziehenMoeglich\" was not injected: check your FXML file 'ParameterPortalApp.fxml'.";
        assert tfWortmeldungLaenge != null : "fx:id=\"tfWortmeldungLaenge\" was not injected: check your FXML file 'ParameterPortalApp.fxml'.";
        assert tfWortmeldungAnzahlJeAktionaer != null : "fx:id=\"tfWortmeldungAnzahlJeAktionaer\" was not injected: check your FXML file 'ParameterPortalApp.fxml'.";
        assert tfWortmeldungStellerZulaessig != null : "fx:id=\"tfWortmeldungStellerZulaessig\" was not injected: check your FXML file 'ParameterPortalApp.fxml'.";
        assert tfWortmeldungListeAnzeigen != null : "fx:id=\"tfWortmeldungListeAnzeigen\" was not injected: check your FXML file 'ParameterPortalApp.fxml'.";
        assert tfWortmeldungMailBeiEingang != null : "fx:id=\"tfWortmeldungMailBeiEingang\" was not injected: check your FXML file 'ParameterPortalApp.fxml'.";
        assert tfWortmeldungMailVerteiler1 != null : "fx:id=\"tfWortmeldungMailVerteiler1\" was not injected: check your FXML file 'ParameterPortalApp.fxml'.";
        assert tfWortmeldungMailVerteiler2 != null : "fx:id=\"tfWortmeldungMailVerteiler2\" was not injected: check your FXML file 'ParameterPortalApp.fxml'.";
        assert tfWortmeldungMailVerteiler3 != null : "fx:id=\"tfWortmeldungMailVerteiler3\" was not injected: check your FXML file 'ParameterPortalApp.fxml'.";
        assert tfWiderspruecheStellerAbfragen != null : "fx:id=\"tfWiderspruecheStellerAbfragen\" was not injected: check your FXML file 'ParameterPortalApp.fxml'.";
        assert tfWiderspruecheNameAbfragen != null : "fx:id=\"tfWiderspruecheNameAbfragen\" was not injected: check your FXML file 'ParameterPortalApp.fxml'.";
        assert tfWiderspruecheKontaktdatenAbfragen != null : "fx:id=\"tfWiderspruecheKontaktdatenAbfragen\" was not injected: check your FXML file 'ParameterPortalApp.fxml'.";
        assert tfWiderspruecheKontaktdatenEMailVorschlagen != null : "fx:id=\"tfWiderspruecheKontaktdatenEMailVorschlagen\" was not injected: check your FXML file 'ParameterPortalApp.fxml'.";
        assert tfWiderspruecheKurztextAbfragen != null : "fx:id=\"tfWiderspruecheKurztextAbfragen\" was not injected: check your FXML file 'ParameterPortalApp.fxml'.";
        assert tfWiderspruecheTopListeAnbieten != null : "fx:id=\"tfWiderspruecheTopListeAnbieten\" was not injected: check your FXML file 'ParameterPortalApp.fxml'.";
        assert tfWiderspruecheLangtextAbfragen != null : "fx:id=\"tfWiderspruecheLangtextAbfragen\" was not injected: check your FXML file 'ParameterPortalApp.fxml'.";
        assert tfWiderspruecheZurueckziehenMoeglich != null : "fx:id=\"tfWiderspruecheZurueckziehenMoeglich\" was not injected: check your FXML file 'ParameterPortalApp.fxml'.";
        assert tfWiderspruecheLaenge != null : "fx:id=\"tfWiderspruecheLaenge\" was not injected: check your FXML file 'ParameterPortalApp.fxml'.";
        assert tfWiderspruecheAnzahlJeAktionaer != null : "fx:id=\"tfWiderspruecheAnzahlJeAktionaer\" was not injected: check your FXML file 'ParameterPortalApp.fxml'.";
        assert tfWiderspruecheStellerZulaessig != null : "fx:id=\"tfWiderspruecheStellerZulaessig\" was not injected: check your FXML file 'ParameterPortalApp.fxml'.";
        assert tfWiderspruecheMailBeiEingang != null : "fx:id=\"tfWiderspruecheMailBeiEingang\" was not injected: check your FXML file 'ParameterPortalApp.fxml'.";
        assert tfWiderspruecheMailVerteiler1 != null : "fx:id=\"tfWiderspruecheMailVerteiler1\" was not injected: check your FXML file 'ParameterPortalApp.fxml'.";
        assert tfWiderspruecheMailVerteiler2 != null : "fx:id=\"tfWiderspruecheMailVerteiler2\" was not injected: check your FXML file 'ParameterPortalApp.fxml'.";
        assert tfWiderspruecheMailVerteiler3 != null : "fx:id=\"tfWiderspruecheMailVerteiler3\" was not injected: check your FXML file 'ParameterPortalApp.fxml'.";
        assert tfAntraegeStellerAbfragen != null : "fx:id=\"tfAntraegeStellerAbfragen\" was not injected: check your FXML file 'ParameterPortalApp.fxml'.";
        assert tfAntraegeNameAbfragen != null : "fx:id=\"tfAntraegeNameAbfragen\" was not injected: check your FXML file 'ParameterPortalApp.fxml'.";
        assert tfAntraegeKontaktdatenAbfragen != null : "fx:id=\"tfAntraegeKontaktdatenAbfragen\" was not injected: check your FXML file 'ParameterPortalApp.fxml'.";
        assert tfAntraegeKontaktdatenEMailVorschlagen != null : "fx:id=\"tfAntraegeKontaktdatenEMailVorschlagen\" was not injected: check your FXML file 'ParameterPortalApp.fxml'.";
        assert tfAntraegeKurztextAbfragen != null : "fx:id=\"tfAntraegeKurztextAbfragen\" was not injected: check your FXML file 'ParameterPortalApp.fxml'.";
        assert tfAntraegeTopListeAnbieten != null : "fx:id=\"tfAntraegeTopListeAnbieten\" was not injected: check your FXML file 'ParameterPortalApp.fxml'.";
        assert tfAntraegeLangtextAbfragen != null : "fx:id=\"tfAntraegeLangtextAbfragen\" was not injected: check your FXML file 'ParameterPortalApp.fxml'.";
        assert tfAntraegeZurueckziehenMoeglich != null : "fx:id=\"tfAntraegeZurueckziehenMoeglich\" was not injected: check your FXML file 'ParameterPortalApp.fxml'.";
        assert tfAntraegeLaenge != null : "fx:id=\"tfAntraegeLaenge\" was not injected: check your FXML file 'ParameterPortalApp.fxml'.";
        assert tfAntraegeAnzahlJeAktionaer != null : "fx:id=\"tfAntraegeAnzahlJeAktionaer\" was not injected: check your FXML file 'ParameterPortalApp.fxml'.";
        assert tfAntraegeStellerZulaessig != null : "fx:id=\"tfAntraegeStellerZulaessig\" was not injected: check your FXML file 'ParameterPortalApp.fxml'.";
        assert tfAntraegeMailBeiEingang != null : "fx:id=\"tfAntraegeMailBeiEingang\" was not injected: check your FXML file 'ParameterPortalApp.fxml'.";
        assert tfAntraegeMailVerteiler1 != null : "fx:id=\"tfAntraegeMailVerteiler1\" was not injected: check your FXML file 'ParameterPortalApp.fxml'.";
        assert tfAntraegeMailVerteiler2 != null : "fx:id=\"tfAntraegeMailVerteiler2\" was not injected: check your FXML file 'ParameterPortalApp.fxml'.";
        assert tfAntraegeMailVerteiler3 != null : "fx:id=\"tfAntraegeMailVerteiler3\" was not injected: check your FXML file 'ParameterPortalApp.fxml'.";
        assert tfSonstMitteilungenStellerAbfragen != null : "fx:id=\"tfSonstMitteilungenStellerAbfragen\" was not injected: check your FXML file 'ParameterPortalApp.fxml'.";
        assert tfSonstMitteilungenNameAbfragen != null : "fx:id=\"tfSonstMitteilungenNameAbfragen\" was not injected: check your FXML file 'ParameterPortalApp.fxml'.";
        assert tfSonstMitteilungenKontaktdatenAbfragen != null : "fx:id=\"tfSonstMitteilungenKontaktdatenAbfragen\" was not injected: check your FXML file 'ParameterPortalApp.fxml'.";
        assert tfSonstMitteilungenKontaktdatenEMailVorschlagen != null : "fx:id=\"tfSonstMitteilungenKontaktdatenEMailVorschlagen\" was not injected: check your FXML file 'ParameterPortalApp.fxml'.";
        assert tfSonstMitteilungenKurztextAbfragen != null : "fx:id=\"tfSonstMitteilungenKurztextAbfragen\" was not injected: check your FXML file 'ParameterPortalApp.fxml'.";
        assert tfSonstMitteilungenTopListeAnbieten != null : "fx:id=\"tfSonstMitteilungenTopListeAnbieten\" was not injected: check your FXML file 'ParameterPortalApp.fxml'.";
        assert tfSonstMitteilungenLangtextAbfragen != null : "fx:id=\"tfSonstMitteilungenLangtextAbfragen\" was not injected: check your FXML file 'ParameterPortalApp.fxml'.";
        assert tfSonstMitteilungenZurueckziehenMoeglich != null : "fx:id=\"tfSonstMitteilungenZurueckziehenMoeglich\" was not injected: check your FXML file 'ParameterPortalApp.fxml'.";
        assert tfSonstMitteilungenLaenge != null : "fx:id=\"tfSonstMitteilungenLaenge\" was not injected: check your FXML file 'ParameterPortalApp.fxml'.";
        assert tfSonstMitteilungenAnzahlJeAktionaer != null : "fx:id=\"tfSonstMitteilungenAnzahlJeAktionaer\" was not injected: check your FXML file 'ParameterPortalApp.fxml'.";
        assert tfSonstMitteilungenStellerZulaessig != null : "fx:id=\"tfSonstMitteilungenStellerZulaessig\" was not injected: check your FXML file 'ParameterPortalApp.fxml'.";
        assert tfSonstMitteilungenMailBeiEingang != null : "fx:id=\"tfSonstMitteilungenMailBeiEingang\" was not injected: check your FXML file 'ParameterPortalApp.fxml'.";
        assert tfSonstMitteilungenMailVerteiler1 != null : "fx:id=\"tfSonstMitteilungenMailVerteiler1\" was not injected: check your FXML file 'ParameterPortalApp.fxml'.";
        assert tfSonstMitteilungenMailVerteiler2 != null : "fx:id=\"tfSonstMitteilungenMailVerteiler2\" was not injected: check your FXML file 'ParameterPortalApp.fxml'.";
        assert tfSonstMitteilungenMailVerteiler3 != null : "fx:id=\"tfSonstMitteilungenMailVerteiler3\" was not injected: check your FXML file 'ParameterPortalApp.fxml'.";
        assert tpVirt != null : "fx:id=\"tpVirt\" was not injected: check your FXML file 'ParameterPortalApp.fxml'.";
        assert tfStreamMitEinmalKey != null : "fx:id=\"tfStreamMitEinmalKey\" was not injected: check your FXML file 'ParameterPortalApp.fxml'.";
        assert tfStreamLink != null : "fx:id=\"tfStreamLink\" was not injected: check your FXML file 'ParameterPortalApp.fxml'.";
        assert tfStreamID != null : "fx:id=\"tfStreamID\" was not injected: check your FXML file 'ParameterPortalApp.fxml'.";
        assert tfTimeoutAufLang != null : "fx:id=\"tfTimeoutAufLang\" was not injected: check your FXML file 'ParameterPortalApp.fxml'.";
        assert tfStreamMitEinmalKey2 != null : "fx:id=\"tfStreamMitEinmalKey2\" was not injected: check your FXML file 'ParameterPortalApp.fxml'.";
        assert tfStreamLink2 != null : "fx:id=\"tfStreamLink2\" was not injected: check your FXML file 'ParameterPortalApp.fxml'.";
        assert tfStreamID2 != null : "fx:id=\"tfStreamID2\" was not injected: check your FXML file 'ParameterPortalApp.fxml'.";
        assert tfWebsocketsMoeglich != null : "fx:id=\"tfWebsocketsMoeglich\" was not injected: check your FXML file 'ParameterPortalApp.fxml'.";
        assert tfDoppelLoginGesperrt != null : "fx:id=\"tfDoppelLoginGesperrt\" was not injected: check your FXML file 'ParameterPortalApp.fxml'.";
        assert tpVirt2 != null : "fx:id=\"tpVirt2\" was not injected: check your FXML file 'ParameterPortalApp.fxml'.";
        assert tfTeilnehmerverzBeginnendBei != null : "fx:id=\"tfTeilnehmerverzBeginnendBei\" was not injected: check your FXML file 'ParameterPortalApp.fxml'.";
        assert tfTeilnehmerverzZusammenstellung != null : "fx:id=\"tfTeilnehmerverzZusammenstellung\" was not injected: check your FXML file 'ParameterPortalApp.fxml'.";
        assert tfTeilnehmerverzLetzteNr != null : "fx:id=\"tfTeilnehmerverzLetzteNr\" was not injected: check your FXML file 'ParameterPortalApp.fxml'.";
        assert tfAbstimmungsergLetzteNr != null : "fx:id=\"tfAbstimmungsergLetzteNr\" was not injected: check your FXML file 'ParameterPortalApp.fxml'.";
        assert tpOnlineHV != null : "fx:id=\"tpOnlineHV\" was not injected: check your FXML file 'ParameterPortalApp.fxml'.";
        assert tfOnlineTeilnehmerAbfragen != null : "fx:id=\"tfOnlineTeilnehmerAbfragen\" was not injected: check your FXML file 'ParameterPortalApp.fxml'.";
        assert tfOnlineTeilnahmeSeparateNutzungsbedingungen != null : "fx:id=\"tfOnlineTeilnahmeSeparateNutzungsbedingungen\" was not injected: check your FXML file 'ParameterPortalApp.fxml'.";
        assert tfOnlineTeilnahmeAktionaerAlsGast != null : "fx:id=\"tfOnlineTeilnahmeAktionaerAlsGast\" was not injected: check your FXML file 'ParameterPortalApp.fxml'.";
        assert tpApp != null : "fx:id=\"tpApp\" was not injected: check your FXML file 'ParameterPortalApp.fxml'.";
        assert tfImpressumEmittent != null : "fx:id=\"tfImpressumEmittent\" was not injected: check your FXML file 'ParameterPortalApp.fxml'.";
        assert tfAnzeigeStartseite != null : "fx:id=\"tfAnzeigeStartseite\" was not injected: check your FXML file 'ParameterPortalApp.fxml'.";
        assert tfAppAktiv != null : "fx:id=\"tfAppAktiv\" was not injected: check your FXML file 'ParameterPortalApp.fxml'.";
        assert tfAppSprache != null : "fx:id=\"tfAppSprache\" was not injected: check your FXML file 'ParameterPortalApp.fxml'.";
        assert btnSpeichern != null : "fx:id=\"btnSpeichern\" was not injected: check your FXML file 'ParameterPortalApp.fxml'.";

        /*** Ab hier individuell **/

        eigeneStage.setOnCloseRequest(new EventHandler<WindowEvent>() {

            @Override
            public void handle(WindowEvent event) {
                CaZeigeHinweis caZeigeHinweis = new CaZeigeHinweis();
                caZeigeHinweis.zeige(eigeneStage, "Achtung - beim Schließen erfolgt keine erneute Speicherung! Bitte nach Schließen Parameter aktuelle Einstellungen nochmals überprüfen!");
                //                speichernParameter();
            }
        });

        /******************** Kapital **************************************/
        tfPortalAktuellAktiv.setText(Integer.toString(ParamS.eclEmittent.portalAktuellAktiv));
        tfPortalStandard.setText(Integer.toString(ParamS.eclEmittent.portalStandard));
        tfPortalIstDauerhaft.setText(Integer.toString(ParamS.eclEmittent.portalIstDauerhaft));
        tfTestModus.setText(Integer.toString(ParamS.param.paramPortal.testModus));

        tfPortalAuchInEnglischVerfuegbar.setText(Integer.toString(ParamS.eclEmittent.portalSprache));
        tfArtSprachumschaltung.setText(Integer.toString(ParamS.param.paramPortal.artSprachumschaltung));
        tfDatumsformatDE.setText(Integer.toString(ParamS.param.paramPortal.datumsformatDE));
        tfDatumsformatEN.setText(Integer.toString(ParamS.param.paramPortal.datumsformatEN));
        tfLetzterAktienregisterUpdate.setText(ParamS.param.paramPortal.letzterAktienregisterUpdate);
        tfBestaetigenDialog.setText(Integer.toString(ParamS.param.paramPortal.bestaetigenDialog));
        tfQuittungDialog.setText(Integer.toString(ParamS.param.paramPortal.quittungDialog));
        tfKontaktFenster.setText(Integer.toString(ParamS.param.paramPortal.kontaktFenster));
        tfPersonenAnzeigeAnredeMitAufnahmen.setText(Integer.toString(ParamS.param.paramPortal.personenAnzeigeAnredeMitAufnahmen));
        tfTextPostfachMitAufnahmen.setText(Integer.toString(ParamS.param.paramPortal.textPostfachMitAufnahmen));
        tfAnzeigeStimmenKennung.setText(Integer.toString(ParamS.param.paramPortal.anzeigeStimmenKennung));

        /********************* Login ****************************************/
        tfAppInstallButtonsAnzeigen.setText(Integer.toString(ParamS.param.paramPortalServer.appInstallButtonsAnzeigen));
        tfPasswortCaseSensitiv.setText(Integer.toString(ParamS.param.paramPortal.passwortCaseSensitiv));
        tfPasswortMindestLaenge.setText(Integer.toString(ParamS.param.paramPortal.passwortMindestLaenge));
        tfVerfahrenPasswortVergessen.setText(Integer.toString(ParamS.param.paramPortal.verfahrenPasswortVergessen));
        tfPasswortPerPostPruefen.setText(Integer.toString(ParamS.param.paramPortal.passwortPerPostPruefen));

        cbGattung1Moeglich.setText(ParamS.param.paramBasis.getGattungBezeichnungKurz(1));
        if (ParamS.param.paramPortal.portalFuerGattungMoeglich[0] == 1) {
            cbGattung1Moeglich.setSelected(true);
        }
        if (!ParamS.param.paramBasis.getGattungAktiv(1)) {
            cbGattung1Moeglich.setVisible(false);
        }

        cbGattung2Moeglich.setText(ParamS.param.paramBasis.getGattungBezeichnungKurz(2));
        if (ParamS.param.paramPortal.portalFuerGattungMoeglich[1] == 1) {
            cbGattung2Moeglich.setSelected(true);
        }
        if (!ParamS.param.paramBasis.getGattungAktiv(2)) {
            cbGattung2Moeglich.setVisible(false);
        }

        cbGattung3Moeglich.setText(ParamS.param.paramBasis.getGattungBezeichnungKurz(3));
        if (ParamS.param.paramPortal.portalFuerGattungMoeglich[2] == 1) {
            cbGattung3Moeglich.setSelected(true);
        }
        if (!ParamS.param.paramBasis.getGattungAktiv(3)) {
            cbGattung3Moeglich.setVisible(false);
        }

        cbGattung4Moeglich.setText(ParamS.param.paramBasis.getGattungBezeichnungKurz(4));
        if (ParamS.param.paramPortal.portalFuerGattungMoeglich[3] == 1) {
            cbGattung4Moeglich.setSelected(true);
        }
        if (!ParamS.param.paramBasis.getGattungAktiv(4)) {
            cbGattung4Moeglich.setVisible(false);
        }

        cbGattung5Moeglich.setText(ParamS.param.paramBasis.getGattungBezeichnungKurz(5));
        if (ParamS.param.paramPortal.portalFuerGattungMoeglich[4] == 1) {
            cbGattung5Moeglich.setSelected(true);
        }
        if (!ParamS.param.paramBasis.getGattungAktiv(5)) {
            cbGattung5Moeglich.setVisible(false);
        }

        tfLoginGesperrt.setText(Integer.toString(ParamS.param.paramPortal.loginGesperrt));
        tfLoginGesperrtTextDeutsch.setText(ParamS.param.paramPortal.loginGesperrtTextDeutsch);
        tfLoginGesperrtTextEnglisch.setText(ParamS.param.paramPortal.loginGesperrtTextEnglisch);

        tfLogoutZiel.setText(ParamS.param.paramPortal.logoutZiel);
        tfLoginVerfahren.setText(Integer.toString(ParamS.param.paramPortal.loginVerfahren));
        tfVerfahrenPasswortVergessenBeiEmailHinterlegtAuchPost.setText(Integer.toString(ParamS.param.paramPortal.verfahrenPasswortVergessenBeiEmailHinterlegtAuchPost));
        tfLinkEmailEnthaeltLinkOderCode.setText(Integer.toString(ParamS.param.paramPortal.linkEmailEnthaeltLinkOderCode));
        tfEmailBestaetigenIstZwingend.setText(Integer.toString(ParamS.param.paramPortal.emailBestaetigenIstZwingend));

        tfLoginIPTracking.setText(Integer.toString(ParamS.param.paramPortal.loginIPTrackingAktiv));

        tfCaptchaVerwenden.setText(Integer.toString(ParamS.param.paramPortal.captchaVerwenden));
        tfLoginVerzoegerungAbVersuch.setText(Integer.toString(ParamS.param.paramPortal.loginVerzoegerungAbVersuch));
        tfLoginVerzoegerungSekunden.setText(Integer.toString(ParamS.param.paramPortal.loginVerzoegerungSekunden));
        tfAlternativeLoginKennung.setText(Integer.toString(ParamS.param.paramPortal.alternativeLoginKennung));
        tfAnzahlEindeutigeKennungenVorhanden.setText(Integer.toString(ParamS.param.paramPortal.anzahlEindeutigeKennungenVorhanden));
        lblAnzahlEindeutigeKennungenVerbraucht.setText("Derzeit verbraucht: " + Integer.toString(ParamS.param.paramPortal.anzahlEindeutigeKennungenVerbraucht));

        tfTeilnehmerKannSichWeitereKennungenZuordnen.setText(Integer.toString(ParamS.param.paramPortal.teilnehmerKannSichWeitereKennungenZuordnen));

        tfKennungAufbereiten.setText(Integer.toString(ParamS.param.paramPortal.kennungAufbereiten));
        tfKennungAufbereitenFuerAnzeige.setText(Integer.toString(ParamS.param.paramPortal.kennungAufbereitenFuerAnzeige));

        /********************************Registrierung/Einstellungen****************************************/
        tfDauerhaftesPasswortMoeglich.setText(Integer.toString(ParamS.param.paramPortal.dauerhaftesPasswortMoeglich));
        tfRegistrierungFuerEmailVersandMoeglich.setText(Integer.toString(ParamS.param.paramPortal.registrierungFuerEmailVersandMoeglich));
        tfEmailVersandRegistrierungOderWiderspruch.setText(Integer.toString(ParamS.param.paramPortal.emailVersandRegistrierungOderWiderspruch));
        tfAdressaenderungMoeglich.setText(Integer.toString(ParamS.param.paramPortal.adressaenderungMoeglich));
        tfKommunikationsspracheAuswahl.setText(Integer.toString(ParamS.param.paramPortal.kommunikationsspracheAuswahl));
        tfPublikationenAnbieten.setText(Integer.toString(ParamS.param.paramPortal.publikationenAnbieten));
        tfKontaktDetailsAnbieten.setText(Integer.toString(ParamS.param.paramPortal.kontaktDetailsAnbieten));
        tfEmailNurBeiEVersandOderPasswort.setText(Integer.toString(ParamS.param.paramPortal.emailNurBeiEVersandOderPasswort));
        tfCookieHinweis.setText(Integer.toString(ParamS.param.paramPortal.cookieHinweis));
        tfSeparateTeilnahmebedingungenFuerGewinnspiel.setText(Integer.toString(ParamS.param.paramPortal.separateTeilnahmebedingungenFuerGewinnspiel));
        tfSeparateDatenschutzerklaerung.setText(Integer.toString(ParamS.param.paramPortal.separateDatenschutzerklaerung));
        tfBestaetigenHinweisAktionaersportal.setText(Integer.toString(ParamS.param.paramPortal.bestaetigenHinweisAktionaersportal));
        tfBestaetigenHinweisHVportal.setText(Integer.toString(ParamS.param.paramPortal.bestaetigenHinweisHVportal));
        tfBestaetigungsseiteEinstellungen.setText(Integer.toString(ParamS.param.paramPortal.bestaetigungsseiteEinstellungen));
        tfAbsendeMailAdresse.setText(Integer.toString(ParamS.param.paramPortal.absendeMailAdresse));
        tfReihenfolgeRegistrierung.setText(Integer.toString(ParamS.param.paramPortal.reihenfolgeRegistrierung));
        tfMailEingabeServiceline.setText(Integer.toString(ParamS.param.paramPortal.mailEingabeServiceline));

        /**********************Erklärungen***********************************************/
        tfSrvAngeboten.setText(Integer.toString(ParamS.param.paramPortal.srvAngeboten));
        tfBriefwahlAngeboten.setText(Integer.toString(ParamS.param.paramPortal.briefwahlAngeboten));
        tfVollmachtDritteAngeboten.setText(Integer.toString(ParamS.param.paramPortal.vollmachtDritteAngeboten));
        tfVollmachtsnachweisAufStartseiteAktiv.setText(Integer.toString(ParamS.param.paramPortal.vollmachtsnachweisAufStartseiteAktiv));
        tfBestaetigungStimmabgabeNachHV.setText(Integer.toString(ParamS.param.paramPortal.bestaetigungStimmabgabeNachHV));
        tfBestaetigungPerEmailUeberallZulassen.setText(Integer.toString(ParamS.param.paramPortal.bestaetigungPerEmailUeberallZulassen));
        tfVollmachtKIAVAngeboten.setText(Integer.toString(ParamS.param.paramPortal.vollmachtKIAVAngeboten));
        tfWeisungAktuellNichtMoeglich.setText(Integer.toString(ParamS.param.paramPortal.weisungenAktuellNichtMoeglich));
        tfWeisungAktuellNichtMoeglichAberBriefwahlSchon.setText(Integer.toString(ParamS.param.paramPortal.weisungenAktuellNichtMoeglichAberBriefwahlSchon));

        tfGastkartenAnforderungMoeglich.setText(Integer.toString(ParamS.param.paramPortal.gastkartenAnforderungMoeglich));
        tfOeffentlicheIDMoeglich.setText(Integer.toString(ParamS.param.paramPortal.oeffentlicheIDMoeglich));

        tfZusaetzlicheEKDritteMoeglich.setText(Integer.toString(ParamS.param.paramPortal.zusaetzlicheEKDritteMoeglich));
        tfEkUndWeisungGleichzeitigMoeglich.setText(Integer.toString(ParamS.param.paramPortal.ekUndWeisungGleichzeitigMoeglich));
        tfBriefwahlZusaetzlichZuSRVMoeglich.setText(Integer.toString(ParamS.param.paramPortal.briefwahlZusaetzlichZuSRVMoeglich));
        tfHandhabungWeisungDurchVerschiedene.setText(Integer.toString(ParamS.param.paramPortal.handhabungWeisungDurchVerschiedene));

        tfErklAnPos1.setText(Integer.toString(ParamS.param.paramPortal.erklAnPos1));
        tfErklAnPos2.setText(Integer.toString(ParamS.param.paramPortal.erklAnPos2));
        tfErklAnPos3.setText(Integer.toString(ParamS.param.paramPortal.erklAnPos3));
        tfErklAnPos4.setText(Integer.toString(ParamS.param.paramPortal.erklAnPos4));
        tfErklAnPos5.setText(Integer.toString(ParamS.param.paramPortal.erklAnPos5));

        tfVollmachtDritteUndAndereWKMoeglich.setText(Integer.toString(ParamS.param.paramPortal.vollmachtDritteUndAndereWKMoeglich));

        tfEkSelbstMoeglich.setText(Integer.toString(ParamS.param.paramPortal.ekSelbstMoeglich));
        tfEkVollmachtMoeglich.setText(Integer.toString(ParamS.param.paramPortal.ekVollmachtMoeglich));
        tfEk2PersonengemeinschaftMoeglich.setText(Integer.toString(ParamS.param.paramPortal.ek2PersonengemeinschaftMoeglich));
        tfEk2MitOderOhneVollmachtMoeglich.setText(Integer.toString(ParamS.param.paramPortal.ek2MitOderOhneVollmachtMoeglich));
        tfEk2SelbstMoeglich.setText(Integer.toString(ParamS.param.paramPortal.ek2SelbstMoeglich));
        tfAnmeldenOhneWeitereWK.setText(Integer.toString(ParamS.param.paramPortal.anmeldenOhneWeitereWK));

        /***************************** Weisung **************************************/

        tfPNichtmarkiertSpeichernAls.setText(Integer.toString(ParamS.param.paramPortal.pNichtmarkiertSpeichernAls));

        if (ParamS.param.paramPortal.gesamtMarkierungJa == 1) {
            cbGesamtMarkierungJa.setSelected(true);
        }
        if (ParamS.param.paramPortal.gesamtMarkierungNein == 1) {
            cbGesamtMarkierungNein.setSelected(true);
        }
        if (ParamS.param.paramPortal.gesamtMarkierungEnthaltung == 1) {
            cbGesamtMarkierungEnthaltung.setSelected(true);
        }
        if (ParamS.param.paramPortal.gesamtMarkierungImSinne == 1) {
            cbGesamtMarkierungImSinne.setSelected(true);
        }
        if (ParamS.param.paramPortal.gesamtMarkierungGegenSinne == 1) {
            cbGesamtMarkierungGegenSinne.setSelected(true);
        }
        if (ParamS.param.paramPortal.gesamtMarkierungAllesLoeschen == 1) {
            cbGesamtMarkierungAllesLoeschen.setSelected(true);
        }

        if (ParamS.param.paramPortal.markierungJa == 1) {
            cbMarkierungJa.setSelected(true);
        }
        if (ParamS.param.paramPortal.markierungNein == 1) {
            cbMarkierungNein.setSelected(true);
        }
        if (ParamS.param.paramPortal.markierungEnthaltung == 1) {
            cbMarkierungEnthaltung.setSelected(true);
        }
        if (ParamS.param.paramPortal.markierungLoeschen == 1) {
            cbMarkierungLoeschen.setSelected(true);
        }

        tfGegenantraegeWeisungenMoeglich.setText(Integer.toString(ParamS.param.paramPortal.gegenantraegeWeisungenMoeglich));
        tfGegenantragsText.setText(Integer.toString(ParamS.param.paramPortal.gegenantragsText));

        if (ParamS.param.paramPortal.checkboxBeiSRV == 1) {
            cbCheckboxBeiSRV.setSelected(true);
        }
        if (ParamS.param.paramPortal.checkboxBeiBriefwahl == 1) {
            cbCheckboxBeiBriefwahl.setSelected(true);
        }
        if (ParamS.param.paramPortal.checkboxBeiKIAV == 1) {
            cbCheckboxBeiKIAV.setSelected(true);
        }
        if (ParamS.param.paramPortal.checkboxBeiVollmacht == 1) {
            cbCheckboxBeiVollmacht.setSelected(true);
        }

        if (ParamS.param.paramPortal.jnAbfrageBeiWeisungQuittung == 1) {
            cbJnAbfrageBeiWeisungQuittung.setSelected(true);
        }

        tfBestaetigungBeiWeisung.setText(Integer.toString(ParamS.param.paramPortal.bestaetigungBeiWeisung));
        tfBestaetigungBeiWeisungMitTOP.setText(Integer.toString(ParamS.param.paramPortal.bestaetigungBeiWeisungMitTOP));
        tfSammelkartenFuerAenderungSperren.setText(Integer.toString(ParamS.param.paramPortal.sammelkartenFuerAenderungSperren));

        /******************************Text-Inhalte*************************************/
        tfStandardTexteBeruecksichtigen.setText(Integer.toString(ParamS.param.paramPortal.standardTexteBeruecksichtigen));
        tfBasisSetStandardTexteVerwenden.setText(Integer.toString(ParamS.param.paramPortal.basisSetStandardTexteVerwenden));
        tfFragezeichenHinweiseVerwenden.setText(Integer.toString(ParamS.param.paramPortal.fragezeichenHinweiseVerwenden));
        tfMehrereStimmrechtsvertreter.setText(Integer.toString(ParamS.param.paramPortal.mehrereStimmrechtsvertreter));

        tfStimmrechtsvertreterNameDE.setText(ParamS.param.paramPortal.stimmrechtsvertreterNameDE);
        tfStimmrechtsvertreterNameEN.setText(ParamS.param.paramPortal.stimmrechtsvertreterNameEN);

        tfKurzLinkPortal.setText(ParamS.param.paramPortal.kurzLinkPortal);
        tfSubdomainPortal.setText(ParamS.param.paramPortal.subdomainPortal);
        tfLinkTagesordnung.setText(ParamS.param.paramPortal.linkTagesordnung);
        tfLinkGegenantraege.setText(ParamS.param.paramPortal.linkGegenantraege);
        tfLinkEinladungsPDF.setText(ParamS.param.paramPortal.linkEinladungsPDF);

        tfLinkNutzungsbedingungenAktionaersPortal.setText(ParamS.param.paramPortal.linkNutzungsbedingungenAktionaersPortal);
        tfLinkNutzungsbedingungenHVPortal.setText(ParamS.param.paramPortal.linkNutzungsbedingungenHVPortal);
        tfLinkDatenschutzhinweise.setText(ParamS.param.paramPortal.linkDatenschutzhinweise);
        tfLinkDatenschutzhinweiseKunde.setText(ParamS.param.paramPortal.linkDatenschutzhinweiseKunde);
        tfLinkImpressum.setText(ParamS.param.paramPortal.linkImpressum);

        tfEkText.setText(ParamS.param.paramPortal.ekText);
        tfEkTextMitArtikel.setText(ParamS.param.paramPortal.ekTextMitArtikel);
        tfEkTextEN.setText(ParamS.param.paramPortal.ekTextEN);
        ekTextENMitArtikel.setText(ParamS.param.paramPortal.ekTextENMitArtikel);

        tfEmailAdresseLink.setText(ParamS.param.paramPortal.emailAdresseLink);
        tfEmailAdresseText.setText(ParamS.param.paramPortal.emailAdresseText);

        tfVollmachtEmailAdresseLink.setText(ParamS.param.paramPortal.vollmachtEmailAdresseLink);
        tfVollmachtEmailAdresseText.setText(ParamS.param.paramPortal.vollmachtEmailAdresseText);

        tfLogoutObenOderUnten.setText(Integer.toString(ParamS.param.paramPortal.logoutObenOderUnten));
        tfLogoPosition.setText(Integer.toString(ParamS.param.paramPortal.logoPosition));
        tfLogoName.setText(ParamS.param.paramPortal.logoName);
        tfLogoBreite.setText(Integer.toString(ParamS.param.paramPortal.logoBreite));
        tfCSSName.setText(ParamS.param.paramPortal.cssName);
        tfDesignKuerzel.setText(ParamS.param.paramPortal.designKuerzel);

        tfFarbeHeader.setText(ParamS.param.paramPortal.farbeHeader);
        tfFarbeLink.setText(ParamS.param.paramPortal.farbeLink);
        tfFarbeLinkHover.setText(ParamS.param.paramPortal.farbeLinkHover);
        tfFarbeListeUngerade.setText(ParamS.param.paramPortal.farbeListeUngerade);
        tfFarbeListeGerade.setText(ParamS.param.paramPortal.farbeListeGerade);
        tfFarbeHintergrund.setText(ParamS.param.paramPortal.farbeHintergrund);
        tfFarbeText.setText(ParamS.param.paramPortal.farbeText);
        tfFarbeUeberschriftHintergrund.setText(ParamS.param.paramPortal.farbeUeberschriftHintergrund);
        tfFarbeUeberschrift.setText(ParamS.param.paramPortal.farbeUeberschrift);

        /* +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++ */

        tfKachelFarbe.setText(ParamS.param.paramPortal.kachelFarbe);

        tfThemeFarbe.setText(ParamS.param.paramPortal.themeFarbe);

        tfSchriftgroesseGlobal.setText(ParamS.param.paramPortal.schriftgroesseGlobal);

        tfLogoMindestbreite.setText(ParamS.param.paramPortal.logoMindestbreite);

        tfFarbeHintergrundBtn00.setText(ParamS.param.paramPortal.farbeHintergrundBtn00);

        tfFarbeSchriftBtn00.setText(ParamS.param.paramPortal.farbeSchriftBtn00);

        tfFarbeRahmenBtn00.setText(ParamS.param.paramPortal.farbeRahmenBtn00);

        tfBreiteRahmenBtn00.setText(ParamS.param.paramPortal.breiteRahmenBtn00);

        tfRadiusRahmenBtn00.setText(ParamS.param.paramPortal.radiusRahmenBtn00);

        tfStilRahmenBtn00.setText(ParamS.param.paramPortal.stilRahmenBtn00);

        tfFarbeHintergrundBtn00Hover.setText(ParamS.param.paramPortal.farbeHintergrundBtn00Hover);

        tfFarbeSchriftBtn00Hover.setText(ParamS.param.paramPortal.farbeSchriftBtn00Hover);

        tfFarbeRahmenBtn00Hover.setText(ParamS.param.paramPortal.farbeRahmenBtn00Hover);

        tfBreiteRahmenBtn00Hover.setText(ParamS.param.paramPortal.breiteRahmenBtn00Hover);

        tfRadiusRahmenBtn00Hover.setText(ParamS.param.paramPortal.radiusRahmenBtn00Hover);

        tfStilRahmenBtn00Hover.setText(ParamS.param.paramPortal.stilRahmenBtn00Hover);

        tfFarbeFocus.setText(ParamS.param.paramPortal.farbeFocus);

        tfFarbeError.setText(ParamS.param.paramPortal.farbeError);

        tfFarbeErrorSchrift.setText(ParamS.param.paramPortal.farbeErrorSchrift);

        tfFarbeWarning.setText(ParamS.param.paramPortal.farbeWarning);

        tfFarbeWarningSchrift.setText(ParamS.param.paramPortal.farbeWarningSchrift);

        tfFarbeSuccess.setText(ParamS.param.paramPortal.farbeSuccess);

        tfFarbeSuccessSchrift.setText(ParamS.param.paramPortal.farbeSuccessSchrift);

        tfFarbeRahmenEingabefelder.setText(ParamS.param.paramPortal.farbeRahmenEingabefelder);

        tfBreiteRahmenEingabefelder.setText(ParamS.param.paramPortal.breiteRahmenEingabefelder);

        tfRadiusRahmenEingabefelder.setText(ParamS.param.paramPortal.radiusRahmenEingabefelder);

        tfStilRahmenEingabefelder.setText(ParamS.param.paramPortal.stilRahmenEingabefelder);

        tfFarbeHintergrundLogoutBtn.setText(ParamS.param.paramPortal.farbeHintergrundLogoutBtn);

        tfFarbeSchriftLogoutBtn.setText(ParamS.param.paramPortal.farbeSchriftLogoutBtn);

        tfFarbeRahmenLogoutBtn.setText(ParamS.param.paramPortal.farbeRahmenLogoutBtn);

        tfBreiteRahmenLogoutBtn.setText(ParamS.param.paramPortal.breiteRahmenLogoutBtn);

        tfRadiusRahmenLogoutBtn.setText(ParamS.param.paramPortal.radiusRahmenLogoutBtn);

        tfStilRahmenLogoutBtn.setText(ParamS.param.paramPortal.stilRahmenLogoutBtn);

        tfFarbeHintergrundLogoutBtnHover.setText(ParamS.param.paramPortal.farbeHintergrundLogoutBtnHover);

        tfFarbeSchriftLogoutBtnHover.setText(ParamS.param.paramPortal.farbeSchriftLogoutBtnHover);

        tfFarbeRahmenLogoutBtnHover.setText(ParamS.param.paramPortal.farbeRahmenLogoutBtnHover);

        tfBreiteRahmenLogoutBtnHover.setText(ParamS.param.paramPortal.breiteRahmenLogoutBtnHover);

        tfRadiusRahmenLogoutBtnHover.setText(ParamS.param.paramPortal.radiusRahmenLogoutBtnHover);

        tfStilRahmenLogoutBtnHover.setText(ParamS.param.paramPortal.stilRahmenLogoutBtnHover);

        tfFarbeHintergrundLoginBtn.setText(ParamS.param.paramPortal.farbeHintergrundLoginBtn);

        tfFarbeSchriftLoginBtn.setText(ParamS.param.paramPortal.farbeSchriftLoginBtn);

        tfFarbeRahmenLoginBtn.setText(ParamS.param.paramPortal.farbeRahmenLoginBtn);

        tfBreiteRahmenLoginBtn.setText(ParamS.param.paramPortal.breiteRahmenLoginBtn);

        tfRadiusRahmenLoginBtn.setText(ParamS.param.paramPortal.radiusRahmenLoginBtn);

        tfStilRahmenLoginBtn.setText(ParamS.param.paramPortal.stilRahmenLoginBtn);

        tfFarbeHintergrundLoginBtnHover.setText(ParamS.param.paramPortal.farbeHintergrundLoginBtnHover);

        tfFarbeSchriftLoginBtnHover.setText(ParamS.param.paramPortal.farbeSchriftLoginBtnHover);

        tfFarbeRahmenLoginBtnHover.setText(ParamS.param.paramPortal.farbeRahmenLoginBtnHover);

        tfBreiteRahmenLoginBtnHover.setText(ParamS.param.paramPortal.breiteRahmenLoginBtnHover);

        tfRadiusRahmenLoginBtnHover.setText(ParamS.param.paramPortal.radiusRahmenLoginBtnHover);

        tfStilRahmenLoginBtnHover.setText(ParamS.param.paramPortal.stilRahmenLoginBtnHover);

        tfFarbeRahmenLoginBereich.setText(ParamS.param.paramPortal.farbeRahmenLoginBereich);

        tfBreiteRahmenLoginBereich.setText(ParamS.param.paramPortal.breiteRahmenLoginBereich);

        tfRadiusRahmenLoginBereich.setText(ParamS.param.paramPortal.radiusRahmenLoginBereich);

        tfStilRahmenLoginBereich.setText(ParamS.param.paramPortal.stilRahmenLoginBereich);

        tfFarbeHintergrundLoginBereich.setText(ParamS.param.paramPortal.farbeHintergrundLoginBereich);

        tfFarbeLinkLoginBereich.setText(ParamS.param.paramPortal.farbeLinkLoginBereich);

        tfFarbeLinkHoverLoginBereich.setText(ParamS.param.paramPortal.farbeLinkHoverLoginBereich);

        tfFarbeRahmenEingabefelderLoginBereich.setText(ParamS.param.paramPortal.farbeRahmenEingabefelderLoginBereich);

        tfBreiteRahmenEingabefelderLoginBereich.setText(ParamS.param.paramPortal.breiteRahmenEingabefelderLoginBereich);

        tfRadiusRahmenEingabefelderLoginBereich.setText(ParamS.param.paramPortal.radiusRahmenEingabefelderLoginBereich);

        tfStilRahmenEingabefelderLoginBereich.setText(ParamS.param.paramPortal.stilRahmenEingabefelderLoginBereich);

        tfFarbeBestandsbereichUngeradeReihe.setText(ParamS.param.paramPortal.farbeBestandsbereichUngeradeReihe);

        tfFarbeBestandsbereichGeradeReihe.setText(ParamS.param.paramPortal.farbeBestandsbereichGeradeReihe);

        tfFarbeLineUntenBestandsbereich.setText(ParamS.param.paramPortal.farbeLineUntenBestandsbereich);

        tfBreiteLineUntenBestandsbereich.setText(ParamS.param.paramPortal.breiteLineUntenBestandsbereich);

        tfStilLineUntenBestandsbereich.setText(ParamS.param.paramPortal.stilLineUntenBestandsbereich);

        tfFarbeRahmenAnmeldeuebersicht.setText(ParamS.param.paramPortal.farbeRahmenAnmeldeuebersicht);

        tfBreiteRahmenAnmeldeuebersicht.setText(ParamS.param.paramPortal.breiteRahmenAnmeldeuebersicht);

        tfRadiusRahmenAnmeldeuebersicht.setText(ParamS.param.paramPortal.radiusRahmenAnmeldeuebersicht);

        tfStilRahmenAnmeldeuebersicht.setText(ParamS.param.paramPortal.stilRahmenAnmeldeuebersicht);

        tfFarbeTrennlinieAnmeldeuebersicht.setText(ParamS.param.paramPortal.farbeTrennlinieAnmeldeuebersicht);

        tfBreiteTrennlinieAnmeldeuebersicht.setText(ParamS.param.paramPortal.breiteTrennlinieAnmeldeuebersicht);

        tfStilTrennlinieAnmeldeuebersicht.setText(ParamS.param.paramPortal.stilTrennlinieAnmeldeuebersicht);

        tfFarbeRahmenErteilteWillenserklärungen.setText(ParamS.param.paramPortal.farbeRahmenErteilteWillenserklärungen);

        tfBreiteRahmenErteilteWillenserklärungen.setText(ParamS.param.paramPortal.breiteRahmenErteilteWillenserklärungen);

        tfRadiusRahmenErteilteWillenserklärungen.setText(ParamS.param.paramPortal.radiusRahmenErteilteWillenserklärungen);

        tfStilRahmenErteilteWillenserklärungen.setText(ParamS.param.paramPortal.stilRahmenErteilteWillenserklärungen);

        tfFarbeHintergrundErteilteWillenserklärungen.setText(ParamS.param.paramPortal.farbeHintergrundErteilteWillenserklärungen);

        tfFarbeSchriftErteilteWillenserklärungen.setText(ParamS.param.paramPortal.farbeSchriftErteilteWillenserklärungen);

        tfFarbeRahmenAbstimmungstabelle.setText(ParamS.param.paramPortal.farbeRahmenAbstimmungstabelle);

        tfBreiteRahmenAbstimmungstabelle.setText(ParamS.param.paramPortal.breiteRahmenAbstimmungstabelle);

        tfRadiusRahmenAbstimmungstabelle.setText(ParamS.param.paramPortal.radiusRahmenAbstimmungstabelle);

        tfStilRahmenAbstimmungstabelle.setText(ParamS.param.paramPortal.stilRahmenAbstimmungstabelle);

        tfFarbeHintergrundAbstimmungstabelleUngeradeReihen.setText(ParamS.param.paramPortal.farbeHintergrundAbstimmungstabelleUngeradeReihen);

        tfFarbeSchriftAbstimmungstabelleUngeradeReihen.setText(ParamS.param.paramPortal.farbeSchriftAbstimmungstabelleUngeradeReihen);

        tfFarbeHintergrundAbstimmungstabelleGeradeReihen.setText(ParamS.param.paramPortal.farbeHintergrundAbstimmungstabelleGeradeReihen);

        tfFarbeSchriftAbstimmungstabelleGeradeReihen.setText(ParamS.param.paramPortal.farbeSchriftAbstimmungstabelleGeradeReihen);

        tfFarbeHintergrundWeisungJa.setText(ParamS.param.paramPortal.farbeHintergrundWeisungJa);

        tfFarbeSchriftWeisungJa.setText(ParamS.param.paramPortal.farbeSchriftWeisungJa);

        tfFarbeRahmenWeisungJa.setText(ParamS.param.paramPortal.farbeRahmenWeisungJa);

        tfFarbeHintergrundWeisungJaChecked.setText(ParamS.param.paramPortal.farbeHintergrundWeisungJaChecked);

        tfFarbeSchriftWeisungJaChecked.setText(ParamS.param.paramPortal.farbeSchriftWeisungJaChecked);

        tfFarbeRahmenWeisungJaChecked.setText(ParamS.param.paramPortal.farbeRahmenWeisungJaChecked);

        tfFarbeHintergrundWeisungNein.setText(ParamS.param.paramPortal.farbeHintergrundWeisungNein);

        tfFarbeSchriftWeisungNein.setText(ParamS.param.paramPortal.farbeSchriftWeisungNein);

        tfFarbeRahmenWeisungNein.setText(ParamS.param.paramPortal.farbeRahmenWeisungNein);

        tfFarbeHintergrundWeisungNeinChecked.setText(ParamS.param.paramPortal.farbeHintergrundWeisungNeinChecked);

        tfFarbeSchriftWeisungNeinChecked.setText(ParamS.param.paramPortal.farbeSchriftWeisungNeinChecked);

        tfFarbeRahmenWeisungNeinChecked.setText(ParamS.param.paramPortal.farbeRahmenWeisungNeinChecked);

        tfFarbeHintergrundWeisungEnthaltung.setText(ParamS.param.paramPortal.farbeHintergrundWeisungEnthaltung);

        tfFarbeSchriftWeisungEnthaltung.setText(ParamS.param.paramPortal.farbeSchriftWeisungEnthaltung);

        tfFarbeRahmenWeisungEnthaltung.setText(ParamS.param.paramPortal.farbeRahmenWeisungEnthaltung);

        tfFarbeHintergrundWeisungEnthaltungChecked.setText(ParamS.param.paramPortal.farbeHintergrundWeisungEnthaltungChecked);

        tfFarbeSchriftWeisungEnthaltungChecked.setText(ParamS.param.paramPortal.farbeSchriftWeisungEnthaltungChecked);

        tfFarbeRahmenWeisungEnthaltungChecked.setText(ParamS.param.paramPortal.farbeRahmenWeisungEnthaltungChecked);

        tfFarbeHintergrundFooterTop.setText(ParamS.param.paramPortal.farbeHintergrundFooterTop);

        tfFarbeSchriftFooterTop.setText(ParamS.param.paramPortal.farbeSchriftFooterTop);

        tfFarbeLinkFooterTop.setText(ParamS.param.paramPortal.farbeLinkFooterTop);

        tfFarbeLinkFooterTopHover.setText(ParamS.param.paramPortal.farbeLinkFooterTopHover);

        tfFarbeHintergrundFooterBottom.setText(ParamS.param.paramPortal.farbeHintergrundFooterBottom);

        tfFarbeSchriftFooterBottom.setText(ParamS.param.paramPortal.farbeSchriftFooterBottom);

        tfFarbeLinkFooterBottom.setText(ParamS.param.paramPortal.farbeLinkFooterBottom);

        tfFarbeLinkFooterBottomHover.setText(ParamS.param.paramPortal.farbeLinkFooterBottomHover);

        tfFarbeHintergrundModal.setText(ParamS.param.paramPortal.farbeHintergrundModal);

        tfFarbeSchriftModal.setText(ParamS.param.paramPortal.farbeSchriftModal);

        tfFarbeHintergrundModalHeader.setText(ParamS.param.paramPortal.farbeHintergrundModalHeader);

        tfFarbeSchriftModalHeader.setText(ParamS.param.paramPortal.farbeSchriftModalHeader);

        tfFarbeTrennlinieModal.setText(ParamS.param.paramPortal.farbeTrennlinieModal);

        tfFarbeHintergrundUntenButtons.setText(ParamS.param.paramPortal.farbeHintergrundUntenButtons);

        tfFarbeSchriftUntenButtons.setText(ParamS.param.paramPortal.farbeSchriftUntenButtons);

        tfFarbeRahmenUntenButtons.setText(ParamS.param.paramPortal.farbeRahmenUntenButtons);

        tfBreiteRahmenUntenButtons.setText(ParamS.param.paramPortal.breiteRahmenUntenButtons);

        tfRadiusRahmenUntenButtons.setText(ParamS.param.paramPortal.radiusRahmenUntenButtons);

        tfStilRahmenUntenButtons.setText(ParamS.param.paramPortal.stilRahmenUntenButtons);

        tfFarbeHintergrundUntenButtonsHover.setText(ParamS.param.paramPortal.farbeHintergrundUntenButtonsHover);

        tfFarbeSchriftUntenButtonsHover.setText(ParamS.param.paramPortal.farbeSchriftUntenButtonsHover);

        tfFarbeRahmenUntenButtonsHover.setText(ParamS.param.paramPortal.farbeRahmenUntenButtonsHover);

        tfBreiteRahmenUntenButtonsHover.setText(ParamS.param.paramPortal.breiteRahmenUntenButtonsHover);

        tfRadiusRahmenUntenButtonsHover.setText(ParamS.param.paramPortal.radiusRahmenUntenButtonsHover);

        tfStilRahmenUntenButtonsHover.setText(ParamS.param.paramPortal.stilRahmenUntenButtonsHover);

        /* +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++ */

        tfFarbeCookieHintHintergrund.setText(ParamS.param.paramPortal.farbeCookieHintHintergrund);
        tfFarbeCookieHintSchrift.setText(ParamS.param.paramPortal.farbeCookieHintSchrift);
        tfFarbeCookieHintButton.setText(ParamS.param.paramPortal.farbeCookieHintButton);
        tfFarbeCookieHintButtonSchrift.setText(ParamS.param.paramPortal.farbeCookieHintButtonSchrift);

        tfFarbeLadebalkenUploadLeer.setText(ParamS.param.paramPortal.farbeLadebalkenUploadLeer);
        tfFarbeLadebalkenUploadFull.setText(ParamS.param.paramPortal.farbeLadebalkenUploadFull);

        /************************ Phasen *******************************/
        tfPhasenNamen = new TextField[21];
        cbManuellAktiv = new CheckBox[21];
        cbManuellDeaktiv = new CheckBox[21];
        cbGewinnspielAktiv = new CheckBox[21];
        cbLfdHVPortalInBetrieb = new CheckBox[21];
        tfLfdVorDerHVNachDerHV = new TextField[21];
        cbLfdHVPortalErstanmeldungIstMoeglich = new CheckBox[21];
        cbLfdHVPortalEKIstMoeglich = new CheckBox[21];
        cbLfdHVPortalSRVIstMoeglich = new CheckBox[21];
        cbLfdHVPortalBriefwahlIstMoeglich = new CheckBox[21];
        cbLfdHVPortalKIAVIstMoeglich = new CheckBox[21];
        cbLfdHVPortalVollmachtDritteIstMoeglich = new CheckBox[21];

        cbLfdHVStreamIstMoeglich = new CheckBox[21];
        cbLfdHVFragenIstMoeglich = new CheckBox[21];
        cbLfdHVRueckfragenIstMoeglich = new CheckBox[21];
        cbLfdHVWortmeldungenenIstMoeglich = new CheckBox[21];
        cbLfdHVWiederspruecheIstMoeglich = new CheckBox[21];
        cbLfdHVAntraegeIstMoeglich = new CheckBox[21];
        cbLfdHVSonstigeMitteilungenIstMoeglich = new CheckBox[21];
        cbLfdHVBotschaftenEinreichenIstMoeglich = new CheckBox[21];
        cbLfdHVBotschaftenIstMoeglich = new CheckBox[21];
        cbLfdHVChatIstMoeglich = new CheckBox[21];
        cbLfdHVUnterlagen1IstMoeglich = new CheckBox[21];
        cbLfdHVUnterlagen2IstMoeglich = new CheckBox[21];
        cbLfdHVUnterlagen3IstMoeglich = new CheckBox[21];
        cbLfdHVUnterlagen4IstMoeglich = new CheckBox[21];
        cbLfdHVUnterlagen5IstMoeglich = new CheckBox[21];
        cbLfdHVTeilnehmerverzIstMoeglich = new CheckBox[21];
        cbLfdHVAbstimmungsergIstMoeglich = new CheckBox[21];

        for (int i = 1; i <= 20; i++) {
            gpPhasen.add(new Label(Integer.toString(i)), 0, i);

            tfPhasenNamen[i] = new TextField();
            tfPhasenNamen[i].setText(ParamS.param.paramPortalServer.phasenNamen[i]);
            gpPhasen.add(tfPhasenNamen[i], 1, i);
            GridPane.setMargin(tfPhasenNamen[i], new Insets(2));

            cbManuellAktiv[i] = new CheckBox();
            cbManuellDeaktiv[i] = new CheckBox();
            cbGewinnspielAktiv[i] = new CheckBox();
            cbLfdHVPortalInBetrieb[i] = new CheckBox();
            tfLfdVorDerHVNachDerHV[i] = new TextField();
            cbLfdHVPortalErstanmeldungIstMoeglich[i] = new CheckBox();
            cbLfdHVPortalEKIstMoeglich[i] = new CheckBox();
            cbLfdHVPortalSRVIstMoeglich[i] = new CheckBox();
            cbLfdHVPortalBriefwahlIstMoeglich[i] = new CheckBox();
            cbLfdHVPortalKIAVIstMoeglich[i] = new CheckBox();
            cbLfdHVPortalVollmachtDritteIstMoeglich[i] = new CheckBox();

            cbLfdHVStreamIstMoeglich[i] = new CheckBox();
            cbLfdHVFragenIstMoeglich[i] = new CheckBox();
            cbLfdHVRueckfragenIstMoeglich[i] = new CheckBox();
            cbLfdHVWortmeldungenenIstMoeglich[i] = new CheckBox();
            cbLfdHVWiederspruecheIstMoeglich[i] = new CheckBox();
            cbLfdHVAntraegeIstMoeglich[i] = new CheckBox();
            cbLfdHVSonstigeMitteilungenIstMoeglich[i] = new CheckBox();
            cbLfdHVBotschaftenEinreichenIstMoeglich[i] = new CheckBox();
            cbLfdHVBotschaftenIstMoeglich[i] = new CheckBox();
            cbLfdHVChatIstMoeglich[i] = new CheckBox();
            cbLfdHVUnterlagen1IstMoeglich[i] = new CheckBox();
            cbLfdHVUnterlagen2IstMoeglich[i] = new CheckBox();
            cbLfdHVUnterlagen3IstMoeglich[i] = new CheckBox();
            cbLfdHVUnterlagen4IstMoeglich[i] = new CheckBox();
            cbLfdHVUnterlagen5IstMoeglich[i] = new CheckBox();
            cbLfdHVTeilnehmerverzIstMoeglich[i] = new CheckBox();
            cbLfdHVAbstimmungsergIstMoeglich[i] = new CheckBox();

            cbManuellAktiv[i].setSelected(ParamS.param.paramPortal.eclPortalPhase[i].manuellAktiv);
            cbManuellDeaktiv[i].setSelected(ParamS.param.paramPortal.eclPortalPhase[i].manuellDeaktiv);
            cbGewinnspielAktiv[i].setSelected(ParamS.param.paramPortal.eclPortalPhase[i].gewinnspielAktiv);
            cbLfdHVPortalInBetrieb[i].setSelected(ParamS.param.paramPortal.eclPortalPhase[i].lfdHVPortalInBetrieb);
            tfLfdVorDerHVNachDerHV[i].setText(Integer.toString(ParamS.param.paramPortal.eclPortalPhase[i].lfdVorDerHVNachDerHV));
            cbLfdHVPortalErstanmeldungIstMoeglich[i].setSelected(ParamS.param.paramPortal.eclPortalPhase[i].lfdHVPortalErstanmeldungIstMoeglich);
            cbLfdHVPortalEKIstMoeglich[i].setSelected(ParamS.param.paramPortal.eclPortalPhase[i].lfdHVPortalEKIstMoeglich);
            cbLfdHVPortalSRVIstMoeglich[i].setSelected(ParamS.param.paramPortal.eclPortalPhase[i].lfdHVPortalSRVIstMoeglich);
            cbLfdHVPortalBriefwahlIstMoeglich[i].setSelected(ParamS.param.paramPortal.eclPortalPhase[i].lfdHVPortalBriefwahlIstMoeglich);
            cbLfdHVPortalKIAVIstMoeglich[i].setSelected(ParamS.param.paramPortal.eclPortalPhase[i].lfdHVPortalKIAVIstMoeglich);
            cbLfdHVPortalVollmachtDritteIstMoeglich[i].setSelected(ParamS.param.paramPortal.eclPortalPhase[i].lfdHVPortalVollmachtDritteIstMoeglich);

            cbLfdHVStreamIstMoeglich[i].setSelected(ParamS.param.paramPortal.eclPortalPhase[i].lfdHVStreamIstMoeglich);
            cbLfdHVFragenIstMoeglich[i].setSelected(ParamS.param.paramPortal.eclPortalPhase[i].lfdHVFragenIstMoeglich);
            cbLfdHVRueckfragenIstMoeglich[i].setSelected(ParamS.param.paramPortal.eclPortalPhase[i].lfdHVRueckfragenIstMoeglich);
            cbLfdHVWortmeldungenenIstMoeglich[i].setSelected(ParamS.param.paramPortal.eclPortalPhase[i].lfdHVWortmeldungenIstMoeglich);
            cbLfdHVWiederspruecheIstMoeglich[i].setSelected(ParamS.param.paramPortal.eclPortalPhase[i].lfdHVWiderspruecheIstMoeglich);
            cbLfdHVAntraegeIstMoeglich[i].setSelected(ParamS.param.paramPortal.eclPortalPhase[i].lfdHVAntraegeIstMoeglich);
            cbLfdHVSonstigeMitteilungenIstMoeglich[i].setSelected(ParamS.param.paramPortal.eclPortalPhase[i].lfdHVSonstigeMitteilungenIstMoeglich);
            cbLfdHVBotschaftenEinreichenIstMoeglich[i].setSelected(ParamS.param.paramPortal.eclPortalPhase[i].lfdHVBotschaftenEinreichenIstMoeglich);
            cbLfdHVBotschaftenIstMoeglich[i].setSelected(ParamS.param.paramPortal.eclPortalPhase[i].lfdHVBotschaftenIstMoeglich);
            cbLfdHVChatIstMoeglich[i].setSelected(ParamS.param.paramPortal.eclPortalPhase[i].lfdHVChatIstMoeglich);
            cbLfdHVUnterlagen1IstMoeglich[i].setSelected(ParamS.param.paramPortal.eclPortalPhase[i].lfdHVUnterlagenGruppe1IstMoeglich);
            cbLfdHVUnterlagen2IstMoeglich[i].setSelected(ParamS.param.paramPortal.eclPortalPhase[i].lfdHVUnterlagenGruppe2IstMoeglich);
            cbLfdHVUnterlagen3IstMoeglich[i].setSelected(ParamS.param.paramPortal.eclPortalPhase[i].lfdHVUnterlagenGruppe3IstMoeglich);
            cbLfdHVUnterlagen4IstMoeglich[i].setSelected(ParamS.param.paramPortal.eclPortalPhase[i].lfdHVUnterlagenGruppe4IstMoeglich);
            cbLfdHVUnterlagen5IstMoeglich[i].setSelected(ParamS.param.paramPortal.eclPortalPhase[i].lfdHVUnterlagenGruppe5IstMoeglich);
            cbLfdHVTeilnehmerverzIstMoeglich[i].setSelected(ParamS.param.paramPortal.eclPortalPhase[i].lfdHVTeilnehmerverzIstMoeglich);
            cbLfdHVAbstimmungsergIstMoeglich[i].setSelected(ParamS.param.paramPortal.eclPortalPhase[i].lfdHVAbstimmungsergIstMoeglich);

            gpPhasen.add(cbManuellAktiv[i], 2, i);
            gpPhasen.add(cbManuellDeaktiv[i], 3, i);
            gpPhasen.add(cbGewinnspielAktiv[i], 4, i);
            gpPhasen.add(cbLfdHVPortalInBetrieb[i], 5, i);
            gpPhasen.add(tfLfdVorDerHVNachDerHV[i], 6, i);
            GridPane.setMargin(tfLfdVorDerHVNachDerHV[i], new Insets(2));
            gpPhasen.add(cbLfdHVPortalErstanmeldungIstMoeglich[i], 7, i);
            gpPhasen.add(cbLfdHVPortalEKIstMoeglich[i], 8, i);
            gpPhasen.add(cbLfdHVPortalSRVIstMoeglich[i], 9, i);
            gpPhasen.add(cbLfdHVPortalBriefwahlIstMoeglich[i], 10, i);
            gpPhasen.add(cbLfdHVPortalKIAVIstMoeglich[i], 11, i);
            gpPhasen.add(cbLfdHVPortalVollmachtDritteIstMoeglich[i], 12, i);

            gpPhasen.add(cbLfdHVStreamIstMoeglich[i], 13, i);
            gpPhasen.add(cbLfdHVFragenIstMoeglich[i], 14, i);
            gpPhasen.add(cbLfdHVRueckfragenIstMoeglich[i], 15, i);
            gpPhasen.add(cbLfdHVWortmeldungenenIstMoeglich[i], 16, i);
            gpPhasen.add(cbLfdHVWiederspruecheIstMoeglich[i], 17, i);
            gpPhasen.add(cbLfdHVAntraegeIstMoeglich[i], 18, i);
            gpPhasen.add(cbLfdHVSonstigeMitteilungenIstMoeglich[i], 19, i);
            gpPhasen.add(cbLfdHVBotschaftenEinreichenIstMoeglich[i], 20, i);
            gpPhasen.add(cbLfdHVBotschaftenIstMoeglich[i], 21, i);
            gpPhasen.add(cbLfdHVChatIstMoeglich[i], 22, i);
            gpPhasen.add(cbLfdHVUnterlagen1IstMoeglich[i], 23, i);
            gpPhasen.add(cbLfdHVUnterlagen2IstMoeglich[i], 24, i);
            gpPhasen.add(cbLfdHVUnterlagen3IstMoeglich[i], 25, i);
            gpPhasen.add(cbLfdHVUnterlagen4IstMoeglich[i], 26, i);
            gpPhasen.add(cbLfdHVUnterlagen5IstMoeglich[i], 27, i);
            gpPhasen.add(cbLfdHVTeilnehmerverzIstMoeglich[i], 28, i);
            gpPhasen.add(cbLfdHVAbstimmungsergIstMoeglich[i], 29, i);
            
            gpPhasen.add(new Label(Integer.toString(i)), 30, i);
        }

        /************************ Portalfunktionen *******************************/
        lbFunktNamen = new Label[41];
        cbFunktWirdAngeboten = new CheckBox[41];
        tfFunktAktiv = new TextField[41];

        cbFunktGast1 = new CheckBox[41];
        cbFunktGast2 = new CheckBox[41];
        cbFunktGast3 = new CheckBox[41];
        cbFunktGast4 = new CheckBox[41];
        cbFunktGast5 = new CheckBox[41];
        cbFunktGast6 = new CheckBox[41];
        cbFunktGast7 = new CheckBox[41];
        cbFunktGast8 = new CheckBox[41];
        cbFunktGast9 = new CheckBox[41];
        cbFunktGast10 = new CheckBox[41];

        cbFunktGast1OT = new CheckBox[41];
        cbFunktGast2OT = new CheckBox[41];
        cbFunktGast3OT = new CheckBox[41];
        cbFunktGast4OT = new CheckBox[41];
        cbFunktGast5OT = new CheckBox[41];
        cbFunktGast6OT = new CheckBox[41];
        cbFunktGast7OT = new CheckBox[41];
        cbFunktGast8OT = new CheckBox[41];
        cbFunktGast9OT = new CheckBox[41];
        cbFunktGast10OT = new CheckBox[41];

        cbFunktAkt = new CheckBox[41];
        cbFunktAktAng = new CheckBox[41];
        cbFunktAktOT = new CheckBox[41];

        for (int i = 1; i <= 40; i++) {
            gpPortalFunktionen.add(new Label(Integer.toString(i)), 0, i);

            lbFunktNamen[i] = new Label();
            lbFunktNamen[i].setText(KonstPortalFunktionen.getText(i));
            gpPortalFunktionen.add(lbFunktNamen[i], 1, i);

            cbFunktWirdAngeboten[i] = new CheckBox();
            tfFunktAktiv[i] = new TextField();
            GridPane.setMargin(tfFunktAktiv[i], new Insets(2));

            cbFunktGast1[i] = new CheckBox();
            cbFunktGast2[i] = new CheckBox();
            cbFunktGast3[i] = new CheckBox();
            cbFunktGast4[i] = new CheckBox();
            cbFunktGast5[i] = new CheckBox();
            cbFunktGast6[i] = new CheckBox();
            cbFunktGast7[i] = new CheckBox();
            cbFunktGast8[i] = new CheckBox();
            cbFunktGast9[i] = new CheckBox();
            cbFunktGast10[i] = new CheckBox();

            cbFunktGast1OT[i] = new CheckBox();
            cbFunktGast2OT[i] = new CheckBox();
            cbFunktGast3OT[i] = new CheckBox();
            cbFunktGast4OT[i] = new CheckBox();
            cbFunktGast5OT[i] = new CheckBox();
            cbFunktGast6OT[i] = new CheckBox();
            cbFunktGast7OT[i] = new CheckBox();
            cbFunktGast8OT[i] = new CheckBox();
            cbFunktGast9OT[i] = new CheckBox();
            cbFunktGast10OT[i] = new CheckBox();

            cbFunktAkt[i] = new CheckBox();
            cbFunktAktAng[i] = new CheckBox();
            cbFunktAktOT[i] = new CheckBox();

            cbFunktWirdAngeboten[i].setSelected(ParamS.param.paramPortal.eclPortalFunktion[i].wirdAngeboten);
            tfFunktAktiv[i].setText(Integer.toString(ParamS.param.paramPortal.eclPortalFunktion[i].aktiv));

            cbFunktGast1[i].setSelected(ParamS.param.paramPortal.eclPortalFunktion[i].berechtigtGast1);
            cbFunktGast2[i].setSelected(ParamS.param.paramPortal.eclPortalFunktion[i].berechtigtGast2);
            cbFunktGast3[i].setSelected(ParamS.param.paramPortal.eclPortalFunktion[i].berechtigtGast3);
            cbFunktGast4[i].setSelected(ParamS.param.paramPortal.eclPortalFunktion[i].berechtigtGast4);
            cbFunktGast5[i].setSelected(ParamS.param.paramPortal.eclPortalFunktion[i].berechtigtGast5);
            cbFunktGast6[i].setSelected(ParamS.param.paramPortal.eclPortalFunktion[i].berechtigtGast6);
            cbFunktGast7[i].setSelected(ParamS.param.paramPortal.eclPortalFunktion[i].berechtigtGast7);
            cbFunktGast8[i].setSelected(ParamS.param.paramPortal.eclPortalFunktion[i].berechtigtGast8);
            cbFunktGast9[i].setSelected(ParamS.param.paramPortal.eclPortalFunktion[i].berechtigtGast9);
            cbFunktGast10[i].setSelected(ParamS.param.paramPortal.eclPortalFunktion[i].berechtigtGast10);

            cbFunktGast1OT[i].setSelected(ParamS.param.paramPortal.eclPortalFunktion[i].berechtigtGastOnlineTeilnahmer1);
            cbFunktGast2OT[i].setSelected(ParamS.param.paramPortal.eclPortalFunktion[i].berechtigtGastOnlineTeilnahmer2);
            cbFunktGast3OT[i].setSelected(ParamS.param.paramPortal.eclPortalFunktion[i].berechtigtGastOnlineTeilnahmer3);
            cbFunktGast4OT[i].setSelected(ParamS.param.paramPortal.eclPortalFunktion[i].berechtigtGastOnlineTeilnahmer4);
            cbFunktGast5OT[i].setSelected(ParamS.param.paramPortal.eclPortalFunktion[i].berechtigtGastOnlineTeilnahmer5);
            cbFunktGast6OT[i].setSelected(ParamS.param.paramPortal.eclPortalFunktion[i].berechtigtGastOnlineTeilnahmer6);
            cbFunktGast7OT[i].setSelected(ParamS.param.paramPortal.eclPortalFunktion[i].berechtigtGastOnlineTeilnahmer7);
            cbFunktGast8OT[i].setSelected(ParamS.param.paramPortal.eclPortalFunktion[i].berechtigtGastOnlineTeilnahmer8);
            cbFunktGast9OT[i].setSelected(ParamS.param.paramPortal.eclPortalFunktion[i].berechtigtGastOnlineTeilnahmer9);
            cbFunktGast10OT[i].setSelected(ParamS.param.paramPortal.eclPortalFunktion[i].berechtigtGastOnlineTeilnahmer10);

            cbFunktAkt[i].setSelected(ParamS.param.paramPortal.eclPortalFunktion[i].berechtigtAktionaer);
            cbFunktAktAng[i].setSelected(ParamS.param.paramPortal.eclPortalFunktion[i].berechtigtAngemeldeterAktionaer);
            cbFunktAktOT[i].setSelected(ParamS.param.paramPortal.eclPortalFunktion[i].berechtigtOnlineTeilnahmeAktionaer);

            gpPortalFunktionen.add(cbFunktWirdAngeboten[i], 2, i);
            gpPortalFunktionen.add(tfFunktAktiv[i], 3, i);
            
            gpPortalFunktionen.add(cbFunktAkt[i], 4, i);
            gpPortalFunktionen.add(cbFunktAktAng[i], 5, i);
            gpPortalFunktionen.add(cbFunktAktOT[i], 6, i);
            
            gpPortalFunktionen.add(cbFunktGast1[i], 7, i);
            gpPortalFunktionen.add(cbFunktGast2[i], 8, i);
            gpPortalFunktionen.add(cbFunktGast3[i], 9, i);
            gpPortalFunktionen.add(cbFunktGast4[i], 10, i);
            gpPortalFunktionen.add(cbFunktGast5[i], 11, i);
            gpPortalFunktionen.add(cbFunktGast6[i], 12, i);
            gpPortalFunktionen.add(cbFunktGast7[i], 13, i);
            gpPortalFunktionen.add(cbFunktGast8[i], 14, i);
            gpPortalFunktionen.add(cbFunktGast9[i], 15, i);
            gpPortalFunktionen.add(cbFunktGast10[i], 16, i);

            gpPortalFunktionen.add(cbFunktGast1OT[i], 17, i);
            gpPortalFunktionen.add(cbFunktGast2OT[i], 18, i);
            gpPortalFunktionen.add(cbFunktGast3OT[i], 19, i);
            gpPortalFunktionen.add(cbFunktGast4OT[i], 20, i);
            gpPortalFunktionen.add(cbFunktGast5OT[i], 21, i);
            gpPortalFunktionen.add(cbFunktGast6OT[i], 22, i);
            gpPortalFunktionen.add(cbFunktGast7OT[i], 23, i);
            gpPortalFunktionen.add(cbFunktGast8OT[i], 24, i);
            gpPortalFunktionen.add(cbFunktGast9OT[i], 25, i);
            gpPortalFunktionen.add(cbFunktGast10OT[i], 26, i);
            
            gpPortalFunktionen.add(new Label(Integer.toString(i)), 27, i);
        }

        /************************ PortalUnterlagen *******************************/
        tfUnterLoginOben = new LinkedList<TextField>();
        tfUnterLoginUnten = new LinkedList<TextField>();
        tfUnterExtern = new LinkedList<TextField>();
        tfUnterUnterlagen = new LinkedList<TextField>();
        tfUnterBotschaften = new LinkedList<TextField>();

        tfUnterAktiv = new LinkedList<TextField>();
        tfUnterArt = new LinkedList<TextField>();
        tfUnterArtStyle = new LinkedList<TextField>();

        tfUnterPreviewLogin = new LinkedList<TextField>();
        tfUnterPreviewExtern = new LinkedList<TextField>();
        tfUnterPreviewIntern = new LinkedList<TextField>();

        tfUnterDateiname = new LinkedList<TextArea>();
        tfUnterDateiAufEnglisch = new LinkedList<TextField>();

        cbUnterGast1 = new LinkedList<CheckBox>();
        cbUnterGast2 = new LinkedList<CheckBox>();
        cbUnterGast3 = new LinkedList<CheckBox>();
        cbUnterGast4 = new LinkedList<CheckBox>();
        cbUnterGast5 = new LinkedList<CheckBox>();
        cbUnterGast6 = new LinkedList<CheckBox>();
        cbUnterGast7 = new LinkedList<CheckBox>();
        cbUnterGast8 = new LinkedList<CheckBox>();
        cbUnterGast9 = new LinkedList<CheckBox>();
        cbUnterGast10 = new LinkedList<CheckBox>();

        cbUnterGast1OT = new LinkedList<CheckBox>();
        cbUnterGast2OT = new LinkedList<CheckBox>();
        cbUnterGast3OT = new LinkedList<CheckBox>();
        cbUnterGast4OT = new LinkedList<CheckBox>();
        cbUnterGast5OT = new LinkedList<CheckBox>();
        cbUnterGast6OT = new LinkedList<CheckBox>();
        cbUnterGast7OT = new LinkedList<CheckBox>();
        cbUnterGast8OT = new LinkedList<CheckBox>();
        cbUnterGast9OT = new LinkedList<CheckBox>();
        cbUnterGast10OT = new LinkedList<CheckBox>();

        cbUnterAkt = new LinkedList<CheckBox>();
        cbUnterAktAng = new LinkedList<CheckBox>();
        cbUnterAktOT = new LinkedList<CheckBox>();

        lbUnterTextDE = new LinkedList<TextArea>();
        lbUnterTextEN = new LinkedList<TextArea>();

        for (int i = 0; i < ParamS.param.paramPortal.eclPortalUnterlagen.size(); i++) {
            EclPortalUnterlagen lPortalUnterlage = ParamS.param.paramPortal.eclPortalUnterlagen.get(i);

            bereiteFelderFuerUnterlageAuf(lPortalUnterlage, i);
        }

        bereiteNeuenButtonAuf();

        /*Fragen*/
        tfFragenStellerAbfragen.setText(Integer.toString(ParamS.param.paramPortal.fragenStellerAbfragen));
        tfFragenNameAbfragen.setText(Integer.toString(ParamS.param.paramPortal.fragenNameAbfragen));
        tfFragenKontaktdatenAbfragen.setText(Integer.toString(ParamS.param.paramPortal.fragenKontaktdatenAbfragen));
        tfFragenKontaktdatenEMailVorschlagen.setText(Integer.toString(ParamS.param.paramPortal.fragenKontaktdatenEMailVorschlagen));
        tfFragenKontaktdatenTelefonAbfragen.setText(Integer.toString(ParamS.param.paramPortal.fragenKontaktdatenTelefonAbfragen));
        tfFragenKurztextAbfragen.setText(Integer.toString(ParamS.param.paramPortal.fragenKurztextAbfragen));
        tfFragenTopListeAnbieten.setText(Integer.toString(ParamS.param.paramPortal.fragenTopListeAnbieten));
        tfFragenLangtextAbfragen.setText(Integer.toString(ParamS.param.paramPortal.fragenLangtextAbfragen));
        tfFragenZurueckziehenMoeglich.setText(Integer.toString(ParamS.param.paramPortal.fragenZurueckziehenMoeglich));
        tfFragenLaenge.setText(Integer.toString(ParamS.param.paramPortal.fragenLaenge));
        tfFragenAnzahlJeAktionaer.setText(Integer.toString(ParamS.param.paramPortal.fragenAnzahlJeAktionaer));
        tfFragenStellerZulaessig.setText(Integer.toString(ParamS.param.paramPortal.fragenStellerZulaessig));
        tfFragenHinweisGelesen.setText(Integer.toString(ParamS.param.paramPortal.fragenHinweisGelesen));
        tfFragenHinweisVorbelegenMit.setText(Integer.toString(ParamS.param.paramPortal.fragenHinweisVorbelegenMit));
        tfFragenRueckfragenErmoeglichen.setText(Integer.toString(ParamS.param.paramPortal.fragenRueckfragenErmoeglichen));
        tfFragenMailBeiEingang.setText(Integer.toString(ParamS.param.paramPortal.fragenMailBeiEingang));
        tfFragenMailVerteiler1.setText(ParamS.param.paramPortal.fragenMailVerteiler1);
        tfFragenMailVerteiler2.setText(ParamS.param.paramPortal.fragenMailVerteiler2);
        tfFragenMailVerteiler3.setText(ParamS.param.paramPortal.fragenMailVerteiler3);
        tfFragenExternerZugriff.setText(Integer.toString(ParamS.param.paramPortal.fragenExternerZugriff));

        /*Wortmeldungen*/
        tfWortmeldungArt.setText(Integer.toString(ParamS.param.paramPortal.wortmeldungArt));
        tfWortmeldungStellerAbfragen.setText(Integer.toString(ParamS.param.paramPortal.wortmeldungStellerAbfragen));
        tfWortmeldungNameAbfragen.setText(Integer.toString(ParamS.param.paramPortal.wortmeldungNameAbfragen));
        tfWortmeldungKontaktdatenAbfragen.setText(Integer.toString(ParamS.param.paramPortal.wortmeldungKontaktdatenAbfragen));
        tfWortmeldungKontaktdatenEMailVorschlagen.setText(Integer.toString(ParamS.param.paramPortal.wortmeldungKontaktdatenEMailVorschlagen));
        tfWortmeldungKontaktdatenTelefonAbfragen.setText(Integer.toString(ParamS.param.paramPortal.wortmeldungKontaktdatenTelefonAbfragen));
        tfWortmeldungKurztextAbfragen.setText(Integer.toString(ParamS.param.paramPortal.wortmeldungKurztextAbfragen));
        tfWortmeldungTopListeAnbieten.setText(Integer.toString(ParamS.param.paramPortal.wortmeldungTopListeAnbieten));
        tfWortmeldungLangtextAbfragen.setText(Integer.toString(ParamS.param.paramPortal.wortmeldungLangtextAbfragen));
        tfWortmeldungZurueckziehenMoeglich.setText(Integer.toString(ParamS.param.paramPortal.wortmeldungZurueckziehenMoeglich));
        tfWortmeldungLaenge.setText(Integer.toString(ParamS.param.paramPortal.wortmeldungLaenge));
        tfWortmeldungAnzahlJeAktionaer.setText(Integer.toString(ParamS.param.paramPortal.wortmeldungAnzahlJeAktionaer));
        tfWortmeldungStellerZulaessig.setText(Integer.toString(ParamS.param.paramPortal.wortmeldungStellerZulaessig));
        tfWortmeldungHinweisGelesen.setText(Integer.toString(ParamS.param.paramPortal.wortmeldungHinweisGelesen));
        tfWortmeldungHinweisVorbelegenMit.setText(Integer.toString(ParamS.param.paramPortal.wortmeldungHinweisVorbelegenMit));
        tfWortmeldungListeAnzeigen.setText(Integer.toString(ParamS.param.paramPortal.wortmeldungListeAnzeigen));
        tfWortmeldungVLListeAnzeigen.setText(Integer.toString(ParamS.param.paramPortal.wortmeldungVLListeAnzeigen));
        tfWortmeldungMailBeiEingang.setText(Integer.toString(ParamS.param.paramPortal.wortmeldungMailBeiEingang));
        tfWortmeldungMailVerteiler1.setText(ParamS.param.paramPortal.wortmeldungMailVerteiler1);
        tfWortmeldungMailVerteiler2.setText(ParamS.param.paramPortal.wortmeldungMailVerteiler2);
        tfWortmeldungMailVerteiler3.setText(ParamS.param.paramPortal.wortmeldungMailVerteiler3);

        tfWortmeldungTestDurchfuehren.setText(Integer.toString(ParamS.param.paramPortal.wortmeldungTestDurchfuehren));
        tfWortmeldungRedeAufrufZweitenVersuchDurchfuehren.setText(Integer.toString(ParamS.param.paramPortal.wortmeldungRedeAufrufZweitenVersuchDurchfuehren));
        tfWortmeldungNachTestManuellInRednerlisteAufnehmen.setText(Integer.toString(ParamS.param.paramPortal.wortmeldungNachTestManuellInRednerlisteAufnehmen));
        tfWortmeldungInhaltsHinweiseAktiv.setText(Integer.toString(ParamS.param.paramPortal.wortmeldungInhaltsHinweiseAktiv));
        tfWortmeldetischSetNr.setText(Integer.toString(ParamS.param.paramPortal.wortmeldetischSetNr));
        tfSchriftgroesseVersammlunsleiterView.setText(ParamS.param.paramPortal.schriftgroesseVersammlunsleiterView);

        /*Widersprüche*/
        tfWiderspruecheStellerAbfragen.setText(Integer.toString(ParamS.param.paramPortal.widerspruecheStellerAbfragen));
        tfWiderspruecheNameAbfragen.setText(Integer.toString(ParamS.param.paramPortal.widerspruecheNameAbfragen));
        tfWiderspruecheKontaktdatenAbfragen.setText(Integer.toString(ParamS.param.paramPortal.widerspruecheKontaktdatenAbfragen));
        tfWiderspruecheKontaktdatenEMailVorschlagen.setText(Integer.toString(ParamS.param.paramPortal.widerspruecheKontaktdatenEMailVorschlagen));
        tfWiderspruecheKontaktdatenTelefonAbfragen.setText(Integer.toString(ParamS.param.paramPortal.widerspruecheKontaktdatenTelefonAbfragen));
        tfWiderspruecheKurztextAbfragen.setText(Integer.toString(ParamS.param.paramPortal.widerspruecheKurztextAbfragen));
        tfWiderspruecheTopListeAnbieten.setText(Integer.toString(ParamS.param.paramPortal.widerspruecheTopListeAnbieten));
        tfWiderspruecheLangtextAbfragen.setText(Integer.toString(ParamS.param.paramPortal.widerspruecheLangtextAbfragen));
        tfWiderspruecheZurueckziehenMoeglich.setText(Integer.toString(ParamS.param.paramPortal.widerspruecheZurueckziehenMoeglich));
        tfWiderspruecheLaenge.setText(Integer.toString(ParamS.param.paramPortal.widerspruecheLaenge));
        tfWiderspruecheAnzahlJeAktionaer.setText(Integer.toString(ParamS.param.paramPortal.widerspruecheAnzahlJeAktionaer));
        tfWiderspruecheStellerZulaessig.setText(Integer.toString(ParamS.param.paramPortal.widerspruecheStellerZulaessig));
        tfWiderspruecheHinweisGelesen.setText(Integer.toString(ParamS.param.paramPortal.widerspruecheHinweisGelesen));
        tfWiderspruecheHinweisVorbelegenMit.setText(Integer.toString(ParamS.param.paramPortal.widerspruecheHinweisVorbelegenMit));
        tfWiderspruecheMailBeiEingang.setText(Integer.toString(ParamS.param.paramPortal.widerspruecheMailBeiEingang));
        tfWiderspruecheMailVerteiler1.setText(ParamS.param.paramPortal.widerspruecheMailVerteiler1);
        tfWiderspruecheMailVerteiler2.setText(ParamS.param.paramPortal.widerspruecheMailVerteiler2);
        tfWiderspruecheMailVerteiler3.setText(ParamS.param.paramPortal.widerspruecheMailVerteiler3);

        /*Anträge*/
        tfAntraegeStellerAbfragen.setText(Integer.toString(ParamS.param.paramPortal.antraegeStellerAbfragen));
        tfAntraegeNameAbfragen.setText(Integer.toString(ParamS.param.paramPortal.antraegeNameAbfragen));
        tfAntraegeKontaktdatenAbfragen.setText(Integer.toString(ParamS.param.paramPortal.antraegeKontaktdatenAbfragen));
        tfAntraegeKontaktdatenEMailVorschlagen.setText(Integer.toString(ParamS.param.paramPortal.antraegeKontaktdatenEMailVorschlagen));
        tfAntraegeKontaktdatenTelefonAbfragen.setText(Integer.toString(ParamS.param.paramPortal.antraegeKontaktdatenTelefonAbfragen));
        tfAntraegeKurztextAbfragen.setText(Integer.toString(ParamS.param.paramPortal.antraegeKurztextAbfragen));
        tfAntraegeTopListeAnbieten.setText(Integer.toString(ParamS.param.paramPortal.antraegeTopListeAnbieten));
        tfAntraegeLangtextAbfragen.setText(Integer.toString(ParamS.param.paramPortal.antraegeLangtextAbfragen));
        tfAntraegeZurueckziehenMoeglich.setText(Integer.toString(ParamS.param.paramPortal.antraegeZurueckziehenMoeglich));
        tfAntraegeLaenge.setText(Integer.toString(ParamS.param.paramPortal.antraegeLaenge));
        tfAntraegeAnzahlJeAktionaer.setText(Integer.toString(ParamS.param.paramPortal.antraegeAnzahlJeAktionaer));
        tfAntraegeStellerZulaessig.setText(Integer.toString(ParamS.param.paramPortal.antraegeStellerZulaessig));
        tfAntraegeHinweisGelesen.setText(Integer.toString(ParamS.param.paramPortal.antraegeHinweisGelesen));
        tfAntraegeHinweisVorbelegenMit.setText(Integer.toString(ParamS.param.paramPortal.antraegeHinweisVorbelegenMit));
        tfAntraegeMailBeiEingang.setText(Integer.toString(ParamS.param.paramPortal.antraegeMailBeiEingang));
        tfAntraegeMailVerteiler1.setText(ParamS.param.paramPortal.antraegeMailVerteiler1);
        tfAntraegeMailVerteiler2.setText(ParamS.param.paramPortal.antraegeMailVerteiler2);
        tfAntraegeMailVerteiler3.setText(ParamS.param.paramPortal.antraegeMailVerteiler3);

        /*Sonstige Mitteilungen*/
        tfSonstMitteilungenStellerAbfragen.setText(Integer.toString(ParamS.param.paramPortal.sonstMitteilungenStellerAbfragen));
        tfSonstMitteilungenNameAbfragen.setText(Integer.toString(ParamS.param.paramPortal.sonstMitteilungenNameAbfragen));
        tfSonstMitteilungenKontaktdatenAbfragen.setText(Integer.toString(ParamS.param.paramPortal.sonstMitteilungenKontaktdatenAbfragen));
        tfSonstMitteilungenKontaktdatenEMailVorschlagen.setText(Integer.toString(ParamS.param.paramPortal.sonstMitteilungenKontaktdatenEMailVorschlagen));
        tfSonstMitteilungenKontaktdatenTelefonAbfragen.setText(Integer.toString(ParamS.param.paramPortal.sonstMitteilungenKontaktdatenTelefonAbfragen));
        tfSonstMitteilungenKurztextAbfragen.setText(Integer.toString(ParamS.param.paramPortal.sonstMitteilungenKurztextAbfragen));
        tfSonstMitteilungenTopListeAnbieten.setText(Integer.toString(ParamS.param.paramPortal.sonstMitteilungenTopListeAnbieten));
        tfSonstMitteilungenLangtextAbfragen.setText(Integer.toString(ParamS.param.paramPortal.sonstMitteilungenLangtextAbfragen));
        tfSonstMitteilungenZurueckziehenMoeglich.setText(Integer.toString(ParamS.param.paramPortal.sonstMitteilungenZurueckziehenMoeglich));
        tfSonstMitteilungenLaenge.setText(Integer.toString(ParamS.param.paramPortal.sonstMitteilungenLaenge));
        tfSonstMitteilungenAnzahlJeAktionaer.setText(Integer.toString(ParamS.param.paramPortal.sonstMitteilungenAnzahlJeAktionaer));
        tfSonstMitteilungenStellerZulaessig.setText(Integer.toString(ParamS.param.paramPortal.sonstMitteilungenStellerZulaessig));
        tfSonstMitteilungenHinweisGelesen.setText(Integer.toString(ParamS.param.paramPortal.sonstMitteilungenHinweisGelesen));
        tfSonstMitteilungenHinweisVorbelegenMit.setText(Integer.toString(ParamS.param.paramPortal.sonstMitteilungenHinweisVorbelegenMit));
        tfSonstMitteilungenMailBeiEingang.setText(Integer.toString(ParamS.param.paramPortal.sonstMitteilungenMailBeiEingang));
        tfSonstMitteilungenMailVerteiler1.setText(ParamS.param.paramPortal.sonstMitteilungenMailVerteiler1);
        tfSonstMitteilungenMailVerteiler2.setText(ParamS.param.paramPortal.sonstMitteilungenMailVerteiler2);
        tfSonstMitteilungenMailVerteiler3.setText(ParamS.param.paramPortal.sonstMitteilungenMailVerteiler3);

        /*Botschaften*/
        tfBotschaftenStellerAbfragen.setText(Integer.toString(ParamS.param.paramPortal.botschaftenStellerAbfragen));
        tfBotschaftenNameAbfragen.setText(Integer.toString(ParamS.param.paramPortal.botschaftenNameAbfragen));
        tfBotschaftenKontaktdatenAbfragen.setText(Integer.toString(ParamS.param.paramPortal.botschaftenKontaktdatenAbfragen));
        tfBotschaftenKontaktdatenEMailVorschlagen.setText(Integer.toString(ParamS.param.paramPortal.botschaftenKontaktdatenEMailVorschlagen));
        tfBotschaftenKontaktdatenTelefonAbfragen.setText(Integer.toString(ParamS.param.paramPortal.botschaftenKontaktdatenTelefonAbfragen));
        tfBotschaftenKurztextAbfragen.setText(Integer.toString(ParamS.param.paramPortal.botschaftenKurztextAbfragen));
        tfBotschaftenTopListeAnbieten.setText(Integer.toString(ParamS.param.paramPortal.botschaftenTopListeAnbieten));
        tfBotschaftenLangtextAbfragen.setText(Integer.toString(ParamS.param.paramPortal.botschaftenLangtextAbfragen));
        tfBotschaftenLangtextUndDateiNurAlternativ.setText(Integer.toString(ParamS.param.paramPortal.botschaftenLangtextUndDateiNurAlternativ));
        tfBotschaftenZurueckziehenMoeglich.setText(Integer.toString(ParamS.param.paramPortal.botschaftenZurueckziehenMoeglich));
        tfBotschaftenAnzahlJeAktionaer.setText(Integer.toString(ParamS.param.paramPortal.botschaftenAnzahlJeAktionaer));
        tfBotschaftenStellerZulaessig.setText(Integer.toString(ParamS.param.paramPortal.botschaftenStellerZulaessig));
        tfBotschaftenHinweisGelesen.setText(Integer.toString(ParamS.param.paramPortal.botschaftenHinweisGelesen));
        tfBotschaftenHinweisVorbelegenMit.setText(Integer.toString(ParamS.param.paramPortal.botschaftenHinweisVorbelegenMit));
        tfBotschaftenVoranmeldungErforderlich.setText(Integer.toString(ParamS.param.paramPortal.botschaftenVoranmeldungErforderlich));
        tfBotschaftenVideoLaenge.setText(Integer.toString(ParamS.param.paramPortal.botschaftenVideoLaenge));
        tfBotschaftenLaenge.setText(Integer.toString(ParamS.param.paramPortal.botschaftenLaenge));
        tfBotschaftenTextDateiLaenge.setText(Integer.toString(ParamS.param.paramPortal.botschaftenTextDateiLaenge));
        tfBotschaftenMailBeiEingang.setText(Integer.toString(ParamS.param.paramPortal.botschaftenMailBeiEingang));
        tfBotschaftenMailVerteiler1.setText(ParamS.param.paramPortal.botschaftenMailVerteiler1);
        tfBotschaftenMailVerteiler2.setText(ParamS.param.paramPortal.botschaftenMailVerteiler2);
        tfBotschaftenMailVerteiler3.setText(ParamS.param.paramPortal.botschaftenMailVerteiler3);
        tfBotschaftenVideo.setText(Integer.toString(ParamS.param.paramPortal.botschaftenVideo));
        tfBotschaftenTextDatei.setText(Integer.toString(ParamS.param.paramPortal.botschaftenTextDatei));
        tfVideoBotschaftProjectId.setText("EMPTY");
        tfVideoBotschaftApiKey.setText("EMPTY");

        for (int i = 1; i <= 15; i++) {
            lbBotschaftenVideoFormate[i] = new Label(ParamS.param.paramPortal.botschaftenVideoZusatz[i]);
            tfBotschaftenVideoFormate[i] = new TextField();
            tfBotschaftenVideoFormate[i].setText(Integer.toString(ParamS.param.paramPortal.botschaftenVideoFormate[i]));
            gpPortalBotschaften.add(lbBotschaftenVideoFormate[i], 1, 14 + i);
            gpPortalBotschaften.add(tfBotschaftenVideoFormate[i], 2, 14 + i);

            lbBotschaftenTextFormate[i] = new Label(ParamS.param.paramPortal.botschaftenTextZusatz[i]);
            tfBotschaftenTextFormate[i] = new TextField();
            tfBotschaftenTextFormate[i].setText(Integer.toString(ParamS.param.paramPortal.botschaftenTextFormate[i]));
            gpPortalBotschaften.add(lbBotschaftenTextFormate[i], 4, 14 + i);
            gpPortalBotschaften.add(tfBotschaftenTextFormate[i], 5, 14 + i);

        }

        /*Inhalts-Hinweise*/
        lbInhaltsHinweiseNr = new Label[10];
        tfInhaltsHinweiseTextDE = new TextField[10];
        tfInhaltsHinweiseTextEN = new TextField[10];
        cbInhaltsHinweiseAktiv = new CheckBox[10][];

        for (int i = 0; i < 10; i++) {
            lbInhaltsHinweiseNr[i] = new Label(Integer.toString(i + 1));
            gpPortalMitteilungenInhaltsHinweise.add(lbInhaltsHinweiseNr[i], 0, i + 1);

            tfInhaltsHinweiseTextDE[i] = new TextField();
            tfInhaltsHinweiseTextDE[i].setText(ParamS.param.paramPortal.inhaltsHinweiseTextDE[i]);
            gpPortalMitteilungenInhaltsHinweise.add(tfInhaltsHinweiseTextDE[i], 1, i + 1);

            tfInhaltsHinweiseTextEN[i] = new TextField();
            tfInhaltsHinweiseTextEN[i].setText(ParamS.param.paramPortal.inhaltsHinweiseTextEN[i]);
            gpPortalMitteilungenInhaltsHinweise.add(tfInhaltsHinweiseTextEN[i], 2, i + 1);

            cbInhaltsHinweiseAktiv[i] = new CheckBox[6];
            for (int i1 = 0; i1 < 6; i1++) {
                cbInhaltsHinweiseAktiv[i][i1] = new CheckBox();
                cbInhaltsHinweiseAktiv[i][i1].setSelected(ParamS.param.paramPortal.inhaltsHinweiseAktiv[i][i1]);
                gpPortalMitteilungenInhaltsHinweise.add(cbInhaltsHinweiseAktiv[i][i1], 3 + i1, i + 1);

            }
        }

        /************* Kontakt/Hilfe *******************/
        tfKontaktformularAktiv.setText(Integer.toString(ParamS.param.paramPortal.kontaktformularAktiv));
        tfKontaktformularBeiEingangMail.setText(Integer.toString(ParamS.param.paramPortal.kontaktformularBeiEingangMail));
        tfKontaktformularBeiEingangMailAn.setText(ParamS.param.paramPortal.kontaktformularBeiEingangMailAn);
        tfKontaktformularBeiEingangMailInhaltAufnehmen.setText(Integer.toString(ParamS.param.paramPortal.kontaktformularBeiEingangMailInhaltAufnehmen));
        tfKontaktformularBeiEingangAufgabe.setText(Integer.toString(ParamS.param.paramPortal.kontaktformularBeiEingangAufgabe));
        tfKontaktformularAnzahlKontaktfelder.setText(Integer.toString(ParamS.param.paramPortal.kontaktformularAnzahlKontaktfelder));
        tfKontaktformularTelefonKontaktAbfragen.setText(Integer.toString(ParamS.param.paramPortal.kontaktformularTelefonKontaktAbfragen));
        tfKontaktformularMailKontaktAbfragen.setText(Integer.toString(ParamS.param.paramPortal.kontaktformularMailKontaktAbfragen));
        tfKontaktformularThemaAnbieten.setText(Integer.toString(ParamS.param.paramPortal.kontaktformularThemaAnbieten));
        tfKontaktformularThemenListeGlobalLokal.setText(Integer.toString(ParamS.param.paramPortal.kontaktformularThemenListeGlobalLokal));
        tfKontaktSonstigeMoeglichkeitenAnbieten.setText(Integer.toString(ParamS.param.paramPortal.kontaktSonstigeMoeglichkeitenAnbieten));
        tfKontaktSonstigeMoeglichkeitenObenOderUnten.setText(Integer.toString(ParamS.param.paramPortal.kontaktSonstigeMoeglichkeitenObenOderUnten));

        /*************** Virtuelle HV *******************/
        tfTimeoutAufLang.setText(Integer.toString(ParamS.param.paramPortal.timeoutAufLang));
        tfWebsocketsMoeglich.setText(Integer.toString(ParamS.param.paramPortal.websocketsMoeglich));
        tfDoppelLoginGesperrt.setText(Integer.toString(ParamS.param.paramPortal.doppelLoginGesperrt));

        tfStreamMitEinmalKey.setText(Integer.toString(ParamS.param.paramPortal.streamAnbieter));
        tfStreamLink.setText(ParamS.param.paramPortal.streamLink);
        tfStreamID.setText(ParamS.param.paramPortal.streamID);

        tfStreamMitEinmalKey2.setText(Integer.toString(ParamS.param.paramPortal.streamAnbieter2));
        tfStreamLink2.setText(ParamS.param.paramPortal.streamLink2);
        tfStreamID2.setText(ParamS.param.paramPortal.streamID2);

        tfShrinkFormat.setText(ParamS.param.paramPortal.shrinkFormat);
        tfShrinkAufloesung.setText(ParamS.param.paramPortal.shrinkAufloesung);

        tfTeilnehmerverzBeginnendBei.setText(Integer.toString(ParamS.param.paramPortal.teilnehmerverzBeginnendBei));
        tfTeilnehmerverzZusammenstellung.setText(Integer.toString(ParamS.param.paramPortal.teilnehmerverzZusammenstellung));
        tfTeilnehmerverzLetzteNr.setText(Integer.toString(ParamS.param.paramPortal.teilnehmerverzLetzteNr));

        tfAbstimmungsergLetzteNr.setText(Integer.toString(ParamS.param.paramPortal.abstimmungsergLetzteNr));
        tfZuschaltungHVAutomatischNachLogin.setText(Integer.toString(ParamS.param.paramPortal.zuschaltungHVAutomatischNachLogin));
        tfZuschaltungHVStreamAutomatischStarten.setText(Integer.toString(ParamS.param.paramPortal.zuschaltungHVStreamAutomatischStarten));

        /************ Konferenz *************************/

        tfKonfEventId.setText(ParamS.param.paramPortal.konfEventId);

        tfKonfRaumIdT1.setText(ParamS.param.paramPortal.konfRaumId[0][0]);
        tfKonfRaumIdT2.setText(ParamS.param.paramPortal.konfRaumId[0][1]);
        tfKonfRaumIdT3.setText(ParamS.param.paramPortal.konfRaumId[0][2]);
        tfKonfRaumIdT4.setText(ParamS.param.paramPortal.konfRaumId[0][3]);
        tfKonfRaumIdT5.setText(ParamS.param.paramPortal.konfRaumId[0][4]);
        tfKonfRaumIdT6.setText(ParamS.param.paramPortal.konfRaumId[0][5]);

        tfKonfRaumIdR1.setText(ParamS.param.paramPortal.konfRaumId[1][0]);
        tfKonfRaumIdR2.setText(ParamS.param.paramPortal.konfRaumId[1][1]);
        tfKonfRaumIdR3.setText(ParamS.param.paramPortal.konfRaumId[1][2]);
        tfKonfRaumIdR4.setText(ParamS.param.paramPortal.konfRaumId[1][3]);
        tfKonfRaumIdR5.setText(ParamS.param.paramPortal.konfRaumId[1][4]);
        tfKonfRaumIdR6.setText(ParamS.param.paramPortal.konfRaumId[1][5]);

        tfKonfRaumAnzahlTest.setText(Integer.toString(ParamS.param.paramPortal.konfRaumAnzahl[0]));
        tfKonfRaumAnzahlReden.setText(Integer.toString(ParamS.param.paramPortal.konfRaumAnzahl[1]));
        tfKonfGlobaleTestraeumeNutzen.setText(Integer.toString(ParamS.param.paramPortal.konfGlobaleTestraeumeNutzen));

        /************ Konferenz Backup *************************/
        tfKonfBackupServer.setText(ParamS.param.paramPortal.konfBackupServer);

        tfKonfRaumIdBT1.setText(ParamS.param.paramPortal.konfRaumBId[0][0]);
        tfKonfRaumIdBT2.setText(ParamS.param.paramPortal.konfRaumBId[0][1]);
        tfKonfRaumIdBT3.setText(ParamS.param.paramPortal.konfRaumBId[0][2]);
        tfKonfRaumIdBT4.setText(ParamS.param.paramPortal.konfRaumBId[0][3]);
        tfKonfRaumIdBT5.setText(ParamS.param.paramPortal.konfRaumBId[0][4]);
        tfKonfRaumIdBT6.setText(ParamS.param.paramPortal.konfRaumBId[0][5]);

        tfKonfRaumIdBR1.setText(ParamS.param.paramPortal.konfRaumBId[1][0]);
        tfKonfRaumIdBR2.setText(ParamS.param.paramPortal.konfRaumBId[1][1]);
        tfKonfRaumIdBR3.setText(ParamS.param.paramPortal.konfRaumBId[1][2]);
        tfKonfRaumIdBR4.setText(ParamS.param.paramPortal.konfRaumBId[1][3]);
        tfKonfRaumIdBR5.setText(ParamS.param.paramPortal.konfRaumBId[1][4]);
        tfKonfRaumIdBR6.setText(ParamS.param.paramPortal.konfRaumBId[1][5]);

        tfKonfRaumIdBT1PW.setText(ParamS.param.paramPortal.konfRaumBIdPW[0][0]);
        tfKonfRaumIdBT2PW.setText(ParamS.param.paramPortal.konfRaumBIdPW[0][1]);
        tfKonfRaumIdBT3PW.setText(ParamS.param.paramPortal.konfRaumBIdPW[0][2]);
        tfKonfRaumIdBT4PW.setText(ParamS.param.paramPortal.konfRaumBIdPW[0][3]);
        tfKonfRaumIdBT5PW.setText(ParamS.param.paramPortal.konfRaumBIdPW[0][4]);
        tfKonfRaumIdBT6PW.setText(ParamS.param.paramPortal.konfRaumBIdPW[0][5]);

        tfKonfRaumIdBR1PW.setText(ParamS.param.paramPortal.konfRaumBIdPW[1][0]);
        tfKonfRaumIdBR2PW.setText(ParamS.param.paramPortal.konfRaumBIdPW[1][1]);
        tfKonfRaumIdBR3PW.setText(ParamS.param.paramPortal.konfRaumBIdPW[1][2]);
        tfKonfRaumIdBR4PW.setText(ParamS.param.paramPortal.konfRaumBIdPW[1][3]);
        tfKonfRaumIdBR5PW.setText(ParamS.param.paramPortal.konfRaumBIdPW[1][4]);
        tfKonfRaumIdBR6PW.setText(ParamS.param.paramPortal.konfRaumBIdPW[1][5]);

        if (ParamS.param.paramPortal.konfBackupAktiv == 1) {
            cbKonfBackupAktiv.setSelected(true);
        } else {
            cbKonfBackupAktiv.setSelected(false);
        }

        /************* Online-HV ***********************/
        //        tfOnlineTeilnahmeAngeboten.setText(Integer.toString(ParamS.param.paramPortal.onlineTeilnahmeAngeboten));
        //        tfOnlineTeilnahmeAktiv.setText(Integer.toString(ParamS.param.paramPortal.onlineTeilnahmeAktiv));
        tfOnlineTeilnehmerAbfragen.setText(Integer.toString(ParamS.param.paramPortal.onlineTeilnehmerAbfragen));
        tfOnlineTeilnahmeSeparateNutzungsbedingungen.setText(Integer.toString(ParamS.param.paramPortal.onlineTeilnahmeSeparateNutzungsbedingungen));
        tfOnlineTeilnahmeAktionaerAlsGast.setText(Integer.toString(ParamS.param.paramPortal.onlineTeilnahmeAktionaerAlsGast));

        /**************** App ************************************/

        tfAppAktiv.setText(Integer.toString(ParamS.eclEmittent.appAktiv));
        tfAppSprache.setText(Integer.toString(ParamS.eclEmittent.appSprache));
        tfImpressumEmittent.setText(Integer.toString(ParamS.param.paramPortal.impressumEmittent));
        tfAnzeigeStartseite.setText(Integer.toString(ParamS.param.paramPortal.anzeigeStartseite));

    }

    /**
     * **********************Logik**************************************************.
     *
     * @return true, if successful
     */

    private boolean speichernParameter() {
        lFehlertext = "";
        String hString = "";

        /*================================Prüfen=============================================*/
        /*************** Allgemein ***********************************/
        pruefe01(tfPortalAktuellAktiv, "Portal aktuell aktiv");
        pruefe01(tfPortalStandard, "Portal ist Standard für diesen Mandant");
        pruefe01(tfPortalIstDauerhaft, "Portal ist Dauerhaft");
        pruefeZahlNichtLeerLaenge(tfTestModus, "Test-/Entwicklermodus", 2);

        pruefeZahlNichtLeerLaenge(tfPortalAuchInEnglischVerfuegbar, "Portal auch in Englisch verfügbar", 3);
        pruefe12(tfArtSprachumschaltung, "Sprachumschaltung");
        pruefe12(tfDatumsformatDE, "Datumsformat Deutsch");
        pruefe12345(tfDatumsformatEN, "Datumsformat Englisch");

        hString = tfLetzterAktienregisterUpdate.getText();
        if (!hString.isEmpty() && !CaString.isDatum(hString)) {
            lFehlertext = "Letzter Aktienregisterupdate (TT.MM.JJJJ): Eingabe ist unzulässig!";
        }

        pruefe01(tfBestaetigenDialog, "Bestätigungs-Maske in Dialogen");
        pruefe01(tfQuittungDialog, "Quittungs-Maske in Dialogen ");
        pruefe01(tfKontaktFenster, "Hilfe/Kontakt-Fenster anzeigen");
        pruefe01(tfPersonenAnzeigeAnredeMitAufnahmen, "Aktionärsanzeige: mit Anrede");
        pruefe01(tfTextPostfachMitAufnahmen, "Aktionärsanzeige: 'Postfach' ergänzen");
        pruefe01(tfAnzeigeStimmenKennung, "Kennungsanzeige: 'Stimmen' anzeigen");

        /**************** Login ***************************/
        pruefe01(tfAppInstallButtonsAnzeigen, "Auf Login-Seite: App-Install-Buttons (S)");
        pruefe01(tfPasswortCaseSensitiv, "Passwort ist Case-Sensitive");

        if (!CaString.isNummern(tfPasswortMindestLaenge.getText())) {
            lFehlertext = "Mindestlänge Passwort: Nur Nummer zulässig!";
        }
        if (!CaString.isNummern(tfVerfahrenPasswortVergessen.getText())) {
            lFehlertext = "Verfahren Passwort-Vergessen: Nur Nummer zulässig!";
        }
        pruefe01(tfPasswortPerPostPruefen, "Passwort per Post prüfen");

        pruefe01(tfLoginGesperrt, "Login Gesperrt");
        pruefeLaenge(tfLoginGesperrtTextDeutsch, "Login gesperrt - Meldung Deutsch", 200);
        pruefeLaenge(tfLoginGesperrtTextEnglisch, "Login gesperrt - Meldung Englisch", 200);

        pruefeLaenge(tfLogoutZiel, "Logout Ziel-Seite", 200);
        pruefeZahlNichtLeerLaenge(tfLoginVerfahren, "Login-Verfahren", 2);
        pruefe01(tfLoginIPTracking, "IP Tracking aktiv");
        pruefe01(tfVerfahrenPasswortVergessenBeiEmailHinterlegtAuchPost, "Passwort Vergessen bei E-Mail hinterlegt");
        pruefe12(tfLinkEmailEnthaeltLinkOderCode, "Link enthält E-Mail oder Code");
        pruefe01(tfEmailBestaetigenIstZwingend, "E-Mail-Bestätigen zwingend");

        pruefeZahlNichtLeerLaenge(tfCaptchaVerwenden, "Captcha Verwenden", 2);
        pruefeZahlNichtLeerLaenge(tfLoginVerzoegerungAbVersuch, "Login Verzögerung ab Versuch", 2);
        pruefeZahlNichtLeerLaenge(tfLoginVerzoegerungSekunden, "Login Verzögerung Sekunden", 2);
        pruefe012(tfAlternativeLoginKennung, "Alternative Login-Kennung");
        pruefeZahlNichtLeerLaenge(tfAnzahlEindeutigeKennungenVorhanden, "Anzahl eindeutige Kennungen vorhanden", 6);
        pruefe01(tfTeilnehmerKannSichWeitereKennungenZuordnen, "Weitere Kennung zuordnen");

        pruefe012(tfKennungAufbereiten, "Kennung aufbereiten");
        pruefe01(tfKennungAufbereitenFuerAnzeige, "Kennung aufbereiten für Anzeige");

        /**************** Registrierung/Einstellungen ******************/
        pruefe012(tfDauerhaftesPasswortMoeglich, "Dauerhaftes Passwort");
        pruefe01(tfRegistrierungFuerEmailVersandMoeglich, "Einladungsversand per Email");
        pruefe12(tfEmailVersandRegistrierungOderWiderspruch, "Einladungsversand Email - Verfahren");
        pruefe01(tfAdressaenderungMoeglich, "Adressänderung möglich");
        pruefe01(tfKommunikationsspracheAuswahl, "Auswahl Kommunikationssprache");
        pruefe01(tfPublikationenAnbieten, "Publikationen anbieten");
        pruefe01(tfKontaktDetailsAnbieten, "Eingabe Kontaktdetails");
        pruefe01(tfEmailNurBeiEVersandOderPasswort, "Email-Eingabe möglich");
        pruefe01(tfCookieHinweis, "Cookie Hinweis");

        pruefe01(tfSeparateTeilnahmebedingungenFuerGewinnspiel, "Teilnahmebedingungen für Gewinnspiel");
        pruefe01(tfSeparateDatenschutzerklaerung, "Datenschutzerklärung");
        pruefe012(tfBestaetigenHinweisAktionaersportal, "Hinweise Aktionärsportal bestätigen");
        pruefe012(tfBestaetigenHinweisHVportal, "Hinweise HV-Portal bestätigen");
        pruefe01(tfBestaetigungsseiteEinstellungen, "Bestätigungsseite Einstellungen");
        pruefeZahlNichtLeerLaenge(tfAbsendeMailAdresse, "Absende-Mail-Adresse", 1);
        pruefe01(tfReihenfolgeRegistrierung, "Reihenfolge");
        pruefe01(tfMailEingabeServiceline, "Mail-Eingabe Serviceline");

        /********************* Erklärungen *********************************/
        pruefe01(tfSrvAngeboten, "Stimmrechtsvertreter möglich");
        pruefe01(tfBriefwahlAngeboten, "Briefwahl möglich");
        pruefe01(tfVollmachtDritteAngeboten, "Vollmacht Dritte möglich");
        pruefe01(tfVollmachtsnachweisAufStartseiteAktiv, "Vollmachtsnachweis auf Startseite");
        pruefe012(tfBestaetigungStimmabgabeNachHV, "Bestätigung Stimmabgabe nach HV");
        pruefe012(tfBestaetigungPerEmailUeberallZulassen, "Bestätigung Erklärungen per Mail");
        pruefe01(tfVollmachtKIAVAngeboten, "Vollmacht KIAV möglich");
        pruefe01(tfWeisungAktuellNichtMoeglich, "Weisung aktuell nicht möglich");
        pruefe01(tfWeisungAktuellNichtMoeglichAberBriefwahlSchon, "Weisung aktuell nicht möglich aber Briefwahl schon");

        pruefeZahlAuchNegativNichtLeerLaenge(tfGastkartenAnforderungMoeglich, "Gastkartenanforderung möglich",4);
        pruefe01(tfOeffentlicheIDMoeglich, "Öffentliche ID möglich");

        pruefe01(tfZusaetzlicheEKDritteMoeglich, "EK mit Vollmacht zusätzlich möglich");
        pruefe01(tfEkUndWeisungGleichzeitigMoeglich, "EK und Weisung gleichzeitig möglich");
        pruefe01(tfBriefwahlZusaetzlichZuSRVMoeglich, "Briefwahl zusätzlich zu SRV möglich");

        pruefe01(tfVollmachtDritteUndAndereWKMoeglich, "Vollmacht Dritte und andere WK möglich");
        pruefe01(tfHandhabungWeisungDurchVerschiedene, "Handhabung Weisung");

        pruefeZahlNichtLeerLaenge(tfErklAnPos1, "Erklärung an Pos 1", 1);
        pruefeZahlNichtLeerLaenge(tfErklAnPos2, "Erklärung an Pos 2", 1);
        pruefeZahlNichtLeerLaenge(tfErklAnPos3, "Erklärung an Pos 3", 1);
        pruefeZahlNichtLeerLaenge(tfErklAnPos4, "Erklärung an Pos 4", 1);
        pruefeZahlNichtLeerLaenge(tfErklAnPos5, "Erklärung an Pos 5", 1);

        pruefe01(tfEkSelbstMoeglich, "EK Selbst möglich");
        pruefe01(tfEkVollmachtMoeglich, "EK mit Vollmacht möglich");
        pruefe01(tfEk2PersonengemeinschaftMoeglich, "2 EK für Personengemeinschaft möglich");
        pruefe01(tfEk2MitOderOhneVollmachtMoeglich, "2 EK mit oder ohne Vollmacht möglich");
        pruefe01(tfEk2SelbstMoeglich, "2 EK selbst möglich");
        pruefe01(tfAnmeldenOhneWeitereWK, "Anmelden ohne weitere Willenserklärung");

        /******************** Weisung *********************************/
        pruefe0123(tfPNichtmarkiertSpeichernAls, "Nicht-markierte speichern als");

        pruefe01(tfGegenantraegeWeisungenMoeglich, "Markierungen für Gegenanträge");
        pruefe0123(tfGegenantragsText, "Text bei Gegenanträgen");

        pruefeZahlNichtLeerLaenge(tfBestaetigungBeiWeisung, "Bestätigung bei Weisung", 2);
        pruefeZahlNichtLeerLaenge(tfBestaetigungBeiWeisungMitTOP, "Bestätigung bei Weisung mit TOP", 2);
        pruefe01(tfSammelkartenFuerAenderungSperren, "Sperre für Sammelkarten");

        /********************** Text-Inhalte *******************************/
        pruefe01(tfStandardTexteBeruecksichtigen, "Standard-Texte berücksichtigen");
        pruefeZahlNichtLeerLaenge(tfBasisSetStandardTexteVerwenden, "Basis-Set Standard-Texte", 4);
        pruefeZahlNichtLeerLaenge(tfFragezeichenHinweiseVerwenden, "Hint verwenden", 4);
        pruefe012(tfMehrereStimmrechtsvertreter, "Mehrere Stimmrechrechtsvertreter");

        if (tfStimmrechtsvertreterNameDE.getText().length() > 200) {
            lFehlertext = "Name Stimmrechtsvertreter (DE): Text zu lang (maximal 200 Zeichen)";
        }
        if (tfStimmrechtsvertreterNameEN.getText().length() > 200) {
            lFehlertext = "Name Stimmrechtsvertreter (DE): Text zu lang (maximal 200 Zeichen)";
        }
        if (tfKurzLinkPortal.getText().length() > 200) {
            lFehlertext = "Kurz Link Portal: Text zu lang (maximal 200 Zeichen)";
        }
        if (tfSubdomainPortal.getText().length() > 200) {
            lFehlertext = "Subdomain: Text zu lang (maximal 200 Zeichen)";
        }
        if (tfLinkTagesordnung.getText().length() > 200) {
            lFehlertext = "Link Tagesordnung: Text zu lang (maximal 200 Zeichen)";
        }
        if (tfLinkGegenantraege.getText().length() > 200) {
            lFehlertext = "Link Gegenanträge: Text zu lang (maximal 200 Zeichen)";
        }
        if (tfLinkEinladungsPDF.getText().length() > 200) {
            lFehlertext = "Link Einladungs-PDF: Text zu lang (maximal 200 Zeichen)";
        }

        if (tfLinkNutzungsbedingungenAktionaersPortal.getText().length() > 200) {
            lFehlertext = "Link Nutzungsbedingungen Aktionärsportal: Text zu lang (maximal 200 Zeichen)";
        }
        if (tfLinkNutzungsbedingungenHVPortal.getText().length() > 200) {
            lFehlertext = "Link Nutzungsbedingungen HV-Portal: Text zu lang (maximal 200 Zeichen)";
        }
        if (tfLinkDatenschutzhinweise.getText().length() > 200) {
            lFehlertext = "Link Datenschutzhinweise: Text zu lang (maximal 200 Zeichen)";
        }
        if (tfLinkDatenschutzhinweiseKunde.getText().length() > 200) {
            lFehlertext = "Link Datenschutzhinweise Kunde: Text zu lang (maximal 200 Zeichen)";
        }
        if (tfLinkImpressum.getText().length() > 200) {
            lFehlertext = "Link Impressum: Text zu lang (maximal 200 Zeichen)";
        }

        if (tfEkText.getText().length() > 40) {
            lFehlertext = "Ek Text: Text zu lang (maximal 40 Zeichen)";
        }
        if (tfEkTextMitArtikel.getText().length() > 40) {
            lFehlertext = "Ek Text EN mit Artikel: Text zu lang (maximal 40 Zeichen)";
        }
        if (tfEkTextEN.getText().length() > 40) {
            lFehlertext = "Ek Text: Text zu lang (maximal 40 Zeichen)";
        }
        if (ekTextENMitArtikel.getText().length() > 40) {
            lFehlertext = "Ek Text EN mit Artikel: Text zu lang (maximal 40 Zeichen)";
        }

        if (tfEmailAdresseLink.getText().length() > 200) {
            lFehlertext = "*Hotline-Email Link: Text zu lang (maximal 200 Zeichen)";
        }
        if (tfEmailAdresseText.getText().length() > 200) {
            lFehlertext = "Hotline-Email Text: Text zu lang (maximal 200 Zeichen)";
        }

        if (tfVollmachtEmailAdresseLink.getText().length() > 200) {
            lFehlertext = "*Vollmacht-Email Link: Text zu lang (maximal 200 Zeichen)";
        }
        if (tfVollmachtEmailAdresseText.getText().length() > 200) {
            lFehlertext = "Vollmacht-Email Text: Text zu lang (maximal 200 Zeichen)";
        }

        this.pruefe12(tfLogoutObenOderUnten, "Logout Oben oder Unten");
        pruefeZahlNichtLeerLaenge(tfLogoPosition, "Logo Position", 1);
        pruefeZahlNichtLeerLaenge(tfLogoBreite, "Logo-Breite", 4);

        /********* Portal-Funktionen ************/
        for (int i = 1; i <= 20; i++) {
            pruefeZahlNichtLeerLaenge(tfFunktAktiv[i], "Portal-Funktionen, aktiv", 1);
        }

        /**************** PortalUnterlagen ****************/
        for (int i = 0; i < ParamS.param.paramPortal.eclPortalUnterlagen.size(); i++) {
            pruefeZahlAuchNegativOderLeer(tfUnterLoginOben.get(i), "Portal-Unterlagen, Reihenfolge Login Oben");
            pruefeZahlAuchNegativOderLeer(tfUnterLoginUnten.get(i), "Portal-Unterlagen, Reihenfolge Login Unten");
            pruefeZahlAuchNegativOderLeer(tfUnterExtern.get(i), "Portal-Unterlagen, Reihenfolge Extern");
            pruefeZahlAuchNegativOderLeer(tfUnterUnterlagen.get(i), "Portal-Unterlagen, Reihenfolge Unterlagen");
            pruefeZahlAuchNegativOderLeer(tfUnterBotschaften.get(i), "Portal-Unterlagen, Reihenfolge Botschaften");

            pruefeZahlNichtLeerLaenge(tfUnterAktiv.get(i), "Portal-Unterlagen, aktiv", 1);

            pruefeZahlNichtLeerLaenge(tfUnterArt.get(i), "Portal-Unterlagen, Art", 1);
            pruefeZahlNichtLeerLaenge(tfUnterArtStyle.get(i), "Portal-Unterlagen, Art - Style", 1);

            pruefeZahlNichtLeerLaenge(tfUnterPreviewLogin.get(i), "Portal-Unterlagen, Preview Login", 1);
            pruefeZahlNichtLeerLaenge(tfUnterPreviewExtern.get(i), "Portal-Unterlagen, Preview Extern", 1);
            pruefeZahlNichtLeerLaenge(tfUnterPreviewIntern.get(i), "Portal-Unterlagen, Preview Intern", 1);

            if (tfUnterDateiname.get(i).getText().length() > 100) {
                lFehlertext = "Unterlagen: Dateiname lang (maximal 100 Zeichen)";
            }
            pruefeZahlAuchNegativOderLeer(tfUnterDateiAufEnglisch.get(i), "Portal-Unterlagen, Text auch auf Englisch");

            if (lbUnterTextDE.get(i).getText().length() > 5000) {
                lFehlertext = "Unterlagen: Text Deutsch zu lang (maximal 5000 Zeichen)";
            }
            if (lbUnterTextEN.get(i).getText().length() > 5000) {
                lFehlertext = "Unterlagen: Text Englisch zu lang (maximal 5000 Zeichen)";
            }
        }

        /***************** Fragen *********************************/
        pruefe01(tfFragenStellerAbfragen, "Fragen - Steller Abfragen");
        pruefeZahlNichtLeerLaenge(tfFragenNameAbfragen, "Fragen - Steller Name Abfragen", 2);
        pruefe012(tfFragenKontaktdatenAbfragen, "Fragen - Kontaktdaten Abfragen");
        pruefe01(tfFragenKontaktdatenEMailVorschlagen, "Fragen - Kontaktdaten Mail vorschlagen");
        pruefe012(tfFragenKontaktdatenTelefonAbfragen, "Fragen - Kontaktdaten Abfragen (Telefon)");
        pruefe012(tfFragenKurztextAbfragen, "Fragen - Kurztext Abfragen");
        pruefe012(tfFragenTopListeAnbieten, "Fragen - TOP-Liste anbieten");
        pruefe012(tfFragenLangtextAbfragen, "Fragen - Langtext Abfragen");
        pruefe01(tfFragenZurueckziehenMoeglich, "Fragen - Zurückziehen möglich");
        pruefeZahlNichtLeerLaenge(tfFragenLaenge, "Fragen - Länge", 5);
        pruefeZahlNichtLeerLaenge(tfFragenAnzahlJeAktionaer, "Fragen - Anzahl", 5);
        pruefeZahlNichtLeerLaenge(tfFragenStellerZulaessig, "Fragen - als Steller zulässig", 1);
        pruefeZahlNichtLeerLaenge(tfFragenHinweisGelesen, "Fragen - Bestätigungshinweis", 1);
        pruefe01(tfFragenHinweisVorbelegenMit, "Fragen - Bestätigungshinweis vorbelegen");
        pruefe01(tfFragenRueckfragenErmoeglichen, "Fragen - Rückfragen auf HV");
        pruefe01(tfFragenMailBeiEingang, "Fragen - Mail bei Eingang");
        if (tfFragenMailVerteiler1.getText().length() > 200) {
            lFehlertext = "Fragen Verteiler 1: Text zu lang (maximal 200 Zeichen)";
        }
        if (tfFragenMailVerteiler2.getText().length() > 200) {
            lFehlertext = "Fragen Verteiler 2: Text zu lang (maximal 200 Zeichen)";
        }
        if (tfFragenMailVerteiler3.getText().length() > 200) {
            lFehlertext = "Fragen Verteiler 3: Text zu lang (maximal 200 Zeichen)";
        }

        pruefe01(tfFragenExternerZugriff, "Fragen - Externer Zugriff");

        /***************** Wortmeldung *********************************/
        pruefeZahlNichtLeerLaenge(tfWortmeldungArt, "Wortmeldung - Art", 1);
        pruefe01(tfWortmeldungStellerAbfragen, "Wortmeldung - Steller Abfragen");
        pruefeZahlNichtLeerLaenge(tfWortmeldungNameAbfragen, "Wortmeldung - Steller Name Abfragen", 2);
        pruefe012(tfWortmeldungKontaktdatenAbfragen, "Wortmeldung - Kontaktdaten Abfragen");
        pruefe01(tfWortmeldungKontaktdatenEMailVorschlagen, "Wortmeldung - Kontaktdaten Mail vorschlagen");
        pruefe012(tfWortmeldungKontaktdatenTelefonAbfragen, "Wortmeldung - Kontaktdaten Abfragen (Telefon)");
        pruefe012(tfWortmeldungKurztextAbfragen, "Wortmeldung - Kurztext Abfragen");
        pruefe012(tfWortmeldungTopListeAnbieten, "Wortmeldung - TOP-Liste anbieten");
        pruefe012(tfWortmeldungLangtextAbfragen, "Wortmeldung - Langtext Abfragen");
        pruefe01(tfWortmeldungZurueckziehenMoeglich, "Wortmeldung - Zurückziehen möglich");
        pruefeZahlNichtLeerLaenge(tfWortmeldungLaenge, "Wortmeldung - Länge", 5);
        pruefeZahlNichtLeerLaenge(tfWortmeldungAnzahlJeAktionaer, "Wortmeldung - Anzahl", 5);
        pruefeZahlNichtLeerLaenge(tfWortmeldungStellerZulaessig, "Wortmeldung - als Steller zulässig", 1);
        pruefeZahlNichtLeerLaenge(tfWortmeldungHinweisGelesen, "Wortmeldung - Bestätigungshinweis", 1);
        pruefe01(tfWortmeldungHinweisVorbelegenMit, "Wortmeldung - Bestätigungshinweis vorbelegen");
        pruefe01(tfWortmeldungMailBeiEingang, "Wortmeldung - Mail bei Eingang");
        pruefeZahlNichtLeerLaenge(tfWortmeldungListeAnzeigen, "Wortmeldung - Liste anzeigen", 4);
        pruefeZahlNichtLeerLaenge(tfWortmeldungVLListeAnzeigen, "Wortmeldung Versammlungsleitung - Liste anzeigen", 4);
        if (tfWortmeldungMailVerteiler1.getText().length() > 200) {
            lFehlertext = "Wortmeldung Verteiler 1: Text zu lang (maximal 200 Zeichen)";
        }
        if (tfWortmeldungMailVerteiler2.getText().length() > 200) {
            lFehlertext = "Wortmeldung Verteiler 2: Text zu lang (maximal 200 Zeichen)";
        }
        if (tfWortmeldungMailVerteiler3.getText().length() > 200) {
            lFehlertext = "Wortmeldung Verteiler 3: Text zu lang (maximal 200 Zeichen)";
        }

        pruefe012(tfWortmeldungTestDurchfuehren, "Wortmeldung - Test durchführen");
        pruefe01(tfWortmeldungRedeAufrufZweitenVersuchDurchfuehren, "Wortmeldung - Rede Aufruf zweiten Versuch durchführen");
        pruefe01(tfWortmeldungNachTestManuellInRednerlisteAufnehmen, "Wortmeldung - nach Test manuell in Rednerliste");
        pruefe012(tfWortmeldungInhaltsHinweiseAktiv, "Wortmeldung - Inhaltshinweise aktiv");
        pruefeZahlNichtLeerLaenge(tfWortmeldetischSetNr, "Wortmeldung - Workflow SetNr", 3);
        pruefeLaenge(tfSchriftgroesseVersammlunsleiterView, "Schriftgröße Versammlungsleiter-View", 4);

        /***************** Widersprueche *********************************/
        pruefe01(tfWiderspruecheStellerAbfragen, "Widersprueche - Steller Abfragen");
        pruefeZahlNichtLeerLaenge(tfWiderspruecheNameAbfragen, "Widersprueche - Steller Name Abfragen", 2);
        pruefe012(tfWiderspruecheKontaktdatenAbfragen, "Widersprueche - Kontaktdaten Abfragen");
        pruefe01(tfWiderspruecheKontaktdatenEMailVorschlagen, "Widersprueche - Kontaktdaten Mail vorschlagen");
        pruefe012(tfWiderspruecheKontaktdatenTelefonAbfragen, "Widersprueche - Kontaktdaten Abfragen (Telefon)");
        pruefe012(tfWiderspruecheKurztextAbfragen, "Widersprueche - Kurztext Abfragen");
        pruefe012(tfWiderspruecheTopListeAnbieten, "Widersprueche - TOP-Liste anbieten");
        pruefe012(tfWiderspruecheLangtextAbfragen, "Widersprueche - Langtext Abfragen");
        pruefe01(tfWiderspruecheZurueckziehenMoeglich, "Widersprueche - Zurückziehen möglich");
        pruefeZahlNichtLeerLaenge(tfWiderspruecheLaenge, "Widersprueche - Länge", 5);
        pruefeZahlNichtLeerLaenge(tfWiderspruecheAnzahlJeAktionaer, "Widersprueche - Anzahl", 5);
        pruefeZahlNichtLeerLaenge(tfWiderspruecheStellerZulaessig, "Widersprueche - als Steller zulässig", 1);
        pruefeZahlNichtLeerLaenge(tfWiderspruecheHinweisGelesen, "Widersprueche - Bestätigungshinweis", 1);
        pruefe01(tfWiderspruecheHinweisVorbelegenMit, "Widersprüche - Bestätigungshinweis vorbelegen");
        pruefe01(tfWiderspruecheMailBeiEingang, "Widersprueche - Mail bei Eingang");
        if (tfWiderspruecheMailVerteiler1.getText().length() > 200) {
            lFehlertext = "Widersprueche Verteiler 1: Text zu lang (maximal 200 Zeichen)";
        }
        if (tfWiderspruecheMailVerteiler2.getText().length() > 200) {
            lFehlertext = "Widersprueche Verteiler 2: Text zu lang (maximal 200 Zeichen)";
        }
        if (tfWiderspruecheMailVerteiler3.getText().length() > 200) {
            lFehlertext = "Widersprueche Verteiler 3: Text zu lang (maximal 200 Zeichen)";
        }

        /***************** Antraege *********************************/
        pruefe01(tfAntraegeStellerAbfragen, "Antraege - Steller Abfragen");
        pruefeZahlNichtLeerLaenge(tfAntraegeNameAbfragen, "Antraege - Steller Name Abfragen", 2);
        pruefe012(tfAntraegeKontaktdatenAbfragen, "Antraege - Kontaktdaten Abfragen");
        pruefe01(tfAntraegeKontaktdatenEMailVorschlagen, "Antraege - Kontaktdaten Mail vorschlagen");
        pruefe012(tfAntraegeKontaktdatenTelefonAbfragen, "Antraege - Kontaktdaten Abfragen (Telefon)");
        pruefe012(tfAntraegeKurztextAbfragen, "Antraege - Kurztext Abfragen");
        pruefe012(tfAntraegeTopListeAnbieten, "Antraege - TOP-Liste anbieten");
        pruefe012(tfAntraegeLangtextAbfragen, "Antraege - Langtext Abfragen");
        pruefe01(tfAntraegeZurueckziehenMoeglich, "Antraege - Zurückziehen möglich");
        pruefeZahlNichtLeerLaenge(tfAntraegeLaenge, "Antraege - Länge", 5);
        pruefeZahlNichtLeerLaenge(tfAntraegeAnzahlJeAktionaer, "Antraege - Anzahl", 5);
        pruefeZahlNichtLeerLaenge(tfAntraegeStellerZulaessig, "Antraege - als Steller zulässig", 1);
        pruefeZahlNichtLeerLaenge(tfAntraegeHinweisGelesen, "Antraege - Bestätigungshinweis", 1);
        pruefe01(tfAntraegeHinweisVorbelegenMit, "Anträge - Bestätigungshinweis vorbelegen");
        pruefe01(tfAntraegeMailBeiEingang, "Antraege - Mail bei Eingang");
        if (tfAntraegeMailVerteiler1.getText().length() > 200) {
            lFehlertext = "Antraege Verteiler 1: Text zu lang (maximal 200 Zeichen)";
        }
        if (tfAntraegeMailVerteiler2.getText().length() > 200) {
            lFehlertext = "Antraege Verteiler 2: Text zu lang (maximal 200 Zeichen)";
        }
        if (tfAntraegeMailVerteiler3.getText().length() > 200) {
            lFehlertext = "Antraege Verteiler 3: Text zu lang (maximal 200 Zeichen)";
        }

        /***************** SonstMitteilungen *********************************/
        pruefe01(tfSonstMitteilungenStellerAbfragen, "SonstMitteilungen - Steller Abfragen");
        pruefeZahlNichtLeerLaenge(tfSonstMitteilungenNameAbfragen, "SonstMitteilungen - Steller Name Abfragen", 2);
        pruefe012(tfSonstMitteilungenKontaktdatenAbfragen, "SonstMitteilungen - Kontaktdaten Abfragen");
        pruefe01(tfSonstMitteilungenKontaktdatenEMailVorschlagen, "SonstMitteilungen - Kontaktdaten Mail vorschlagen");
        pruefe012(tfSonstMitteilungenKontaktdatenTelefonAbfragen, "SonstMitteilungen - Kontaktdaten Abfragen (Telefon)");
        pruefe012(tfSonstMitteilungenKurztextAbfragen, "SonstMitteilungen - Kurztext Abfragen");
        pruefe012(tfSonstMitteilungenTopListeAnbieten, "SonstMitteilungen - TOP-Liste anbieten");
        pruefe012(tfSonstMitteilungenLangtextAbfragen, "SonstMitteilungen - Langtext Abfragen");
        pruefe01(tfSonstMitteilungenZurueckziehenMoeglich, "SonstMitteilungen - Zurückziehen möglich");
        pruefeZahlNichtLeerLaenge(tfSonstMitteilungenLaenge, "SonstMitteilungen - Länge", 5);
        pruefeZahlNichtLeerLaenge(tfSonstMitteilungenAnzahlJeAktionaer, "SonstMitteilungen - Anzahl", 5);
        pruefeZahlNichtLeerLaenge(tfSonstMitteilungenStellerZulaessig, "SonstMitteilungen - als Steller zulässig", 1);
        pruefeZahlNichtLeerLaenge(tfSonstMitteilungenHinweisGelesen, "SonstMitteilungen - Bestätigungshinweis", 1);
        pruefe01(tfSonstMitteilungenHinweisVorbelegenMit, "SonstMitteilungen - Bestätigungshinweis vorbelegen");
        pruefe01(tfSonstMitteilungenMailBeiEingang, "SonstMitteilungen - Mail bei Eingang");
        if (tfSonstMitteilungenMailVerteiler1.getText().length() > 200) {
            lFehlertext = "SonstMitteilungen Verteiler 1: Text zu lang (maximal 200 Zeichen)";
        }
        if (tfSonstMitteilungenMailVerteiler2.getText().length() > 200) {
            lFehlertext = "SonstMitteilungen Verteiler 2: Text zu lang (maximal 200 Zeichen)";
        }
        if (tfSonstMitteilungenMailVerteiler3.getText().length() > 200) {
            lFehlertext = "SonstMitteilungen Verteiler 3: Text zu lang (maximal 200 Zeichen)";
        }

        /***************** Botschaften *********************************/
        pruefe01(tfBotschaftenStellerAbfragen, "Botschaften - Steller Abfragen");
        pruefeZahlNichtLeerLaenge(tfBotschaftenNameAbfragen, "Botschaften - Steller Name Abfragen", 2);
        pruefe012(tfBotschaftenKontaktdatenAbfragen, "Botschaften - Kontaktdaten Abfragen");
        pruefe01(tfBotschaftenKontaktdatenEMailVorschlagen, "Botschaften - Kontaktdaten Mail vorschlagen");
        pruefe012(tfBotschaftenKontaktdatenTelefonAbfragen, "Botschaften - Kontaktdaten Abfragen (Telefon)");
        pruefe012(tfBotschaftenKurztextAbfragen, "Botschaften - Kurztext Abfragen");
        pruefe012(tfBotschaftenTopListeAnbieten, "Botschaften - TOP-Liste anbieten");
        pruefe01(tfBotschaftenLangtextAbfragen, "Botschaften - Langtext Abfragen");
        pruefe01(tfBotschaftenLangtextUndDateiNurAlternativ, "Botschaften - Langtext und Datei alternativ");
        pruefe01(tfBotschaftenZurueckziehenMoeglich, "Botschaften - Zurückziehen möglich");
        pruefeZahlNichtLeerLaenge(tfBotschaftenAnzahlJeAktionaer, "Botschaften - Anzahl", 5);
        pruefeZahlNichtLeerLaenge(tfBotschaftenStellerZulaessig, "Botschaften - als Steller zulässig", 1);
        pruefeZahlNichtLeerLaenge(tfBotschaftenHinweisGelesen, "Botschaften - Bestätigungshinweis", 1);
        pruefe01(tfBotschaftenHinweisVorbelegenMit, "Botschaften - Bestätigungshinweis vorbelegen");
        pruefeZahlNichtLeerLaenge(tfBotschaftenVoranmeldungErforderlich, "Botschaften - Voranmeldung erforderlich", 1);
        pruefeZahlNichtLeerLaenge(tfBotschaftenVideoLaenge, "Botschaften - Video-Länge", 5);
        pruefeZahlNichtLeerLaenge(tfBotschaftenLaenge, "Botschaften - Länge", 9);
        pruefeZahlNichtLeerLaenge(tfBotschaftenTextDateiLaenge, "Botschaften - Text-DateiLänge", 9);
        pruefe01(tfBotschaftenMailBeiEingang, "Botschaften - Mail bei Eingang");
        if (tfBotschaftenMailVerteiler1.getText().length() > 200) {
            lFehlertext = "Botschaften Verteiler 1: Text zu lang (maximal 200 Zeichen)";
        }
        if (tfBotschaftenMailVerteiler2.getText().length() > 200) {
            lFehlertext = "Botschaften Verteiler 2: Text zu lang (maximal 200 Zeichen)";
        }
        if (tfBotschaftenMailVerteiler3.getText().length() > 200) {
            lFehlertext = "Botschaften Verteiler 3: Text zu lang (maximal 200 Zeichen)";
        }
        pruefe01(tfBotschaftenVideo, "Botschaften - Videobotschaft möglich");
        pruefe01(tfBotschaftenTextDatei, "Botschaften - Text-Dateibotschaft möglich");

        for (int i = 1; i <= 15; i++) {
            pruefe01(tfBotschaftenVideoFormate[i], "Botschaften - VideoFormat " + Integer.toString(i));
            pruefe01(tfBotschaftenTextFormate[i], "Botschaften - TextFormat " + Integer.toString(i));
        }

        if (tfVideoBotschaftProjectId.getText().length() > 200) {
            lFehlertext = "Botschaften Projekt-ID: Text zu lang (maximal 200 Zeichen)";
        }
        if (tfVideoBotschaftApiKey.getText().length() > 200) {
            lFehlertext = "Botschaften Api-Key: Text zu lang (maximal 200 Zeichen)";
        }

        /********************* Inhalts-Hinweise ********************/
        for (int i = 0; i < 9; i++) {
            this.pruefeLaenge(tfInhaltsHinweiseTextDE[i], "Inhalts-Hinweis Text Deutsch " + Integer.toString(i + 1), 200);
            this.pruefeLaenge(tfInhaltsHinweiseTextEN[i], "Inhalts-Hinweis Text Englisch " + Integer.toString(i + 1), 200);
        }

        /************* Kontakt/Hilfe *******************/
        pruefe01(tfKontaktformularAktiv, "Kontakt - Formular aktiv");
        pruefe01(tfKontaktformularBeiEingangMail, "Kontakt - Bei Eingang Mail");
        if (tfKontaktformularBeiEingangMailAn.getText().length() > 200) {
            lFehlertext = "Kontakt Mail an: Text zu lang (maximal 200 Zeichen)";
        }
        pruefe01(tfKontaktformularBeiEingangMailInhaltAufnehmen, "Kontakt - Bei Eingang Mail - Inhalt mit aufnehmen");
        pruefe01(tfKontaktformularBeiEingangAufgabe, "Kontakt - Bei Eingang Aufgabe");
        pruefeZahlNichtLeerLaenge(tfKontaktformularAnzahlKontaktfelder, "Kontakt - Anzahl Kontaktfelder, 0 bis 5", 1);
        pruefe012(tfKontaktformularTelefonKontaktAbfragen, "Kontakt - Telefonnummer abfragen");
        pruefe012(tfKontaktformularMailKontaktAbfragen, "Kontakt - Mail abfragen");
        pruefe012(tfKontaktformularThemaAnbieten, "Kontakt - Thema anbieten");
        pruefe123(tfKontaktformularThemenListeGlobalLokal, "Kontakt - Themen global oder lokal");
        pruefeZahlNichtLeerLaenge(tfKontaktSonstigeMoeglichkeitenAnbieten, "Kontakt - sonstigen Kontakt anbieten", 1);
        pruefe12(tfKontaktSonstigeMoeglichkeitenObenOderUnten, "Kontakt - sonstigen Kontakt oben oder unten");

        /*************** Virtuelle HV ************************/
        pruefe01(tfTimeoutAufLang, "Timeout auf Lang");
        pruefe01(tfWebsocketsMoeglich, "Websockets möglich");
        pruefe01(tfDoppelLoginGesperrt, "Doppelt-Login gesperrt");

        pruefeZahlNichtLeerLaenge(tfStreamMitEinmalKey, "Streamanbieter", 2);
        if (tfStreamLink.getText().length() > 600) {
            lFehlertext = "Stream-Link: Text zu lang (maximal 600 Zeichen)";
        }
        if (tfStreamID.getText().length() > 600) {
            lFehlertext = "Stream-ID: Text zu lang (maximal 600 Zeichen)";
        }
        pruefeZahlNichtLeerLaenge(tfStreamMitEinmalKey2, "Streamanbieter Bakup", 2);
        if (tfStreamLink2.getText().length() > 600) {
            lFehlertext = "Stream-Link Backup: Text zu lang (maximal 600 Zeichen)";
        }
        if (tfStreamID2.getText().length() > 600) {
            lFehlertext = "Stream-ID Backup: Text zu lang (maximal 600 Zeichen)";
        }

        if (tfShrinkFormat.getText().length() > 40) {
            lFehlertext = "ShrinkFormat Backup: Text zu lang (maximal 40 Zeichen)";
        }
        if (tfShrinkAufloesung.getText().length() > 40) {
            lFehlertext = "ShrinkAufloesung: Text zu lang (maximal 40 Zeichen)";
        }

        pruefe01(tfTeilnehmerverzBeginnendBei, "Teilnehmerverzeichnis - beginnend bei");
        pruefe01(tfTeilnehmerverzZusammenstellung, "Teilnehmerverzeichnis - Zusammenstellung");
        //    	pruefe01(tfTeilnehmerverzLetzteNr, "Teilnehmerverzeichnis - letzte Nummer");
        pruefe012(tfZuschaltungHVAutomatischNachLogin, "Zuschaltung nach HV");
        pruefe01(tfZuschaltungHVStreamAutomatischStarten, "Stream starten bei Zuschaltung");

        /*************************** Konferenz *********************************/

        if (tfKonfRaumIdT1.getText().length() > 200) {
            lFehlertext = "konfRaumId Test 1: Text zu lang (maximal 200 Zeichen)";
        }
        if (tfKonfRaumIdT2.getText().length() > 200) {
            lFehlertext = "konfRaumId Test 2: Text zu lang (maximal 200 Zeichen)";
        }
        if (tfKonfRaumIdT3.getText().length() > 200) {
            lFehlertext = "konfRaumId Test 3: Text zu lang (maximal 200 Zeichen)";
        }

        if (tfKonfRaumIdR1.getText().length() > 200) {
            lFehlertext = "konfRaumId Rede 1: Text zu lang (maximal 200 Zeichen)";
        }
        if (tfKonfRaumIdR2.getText().length() > 200) {
            lFehlertext = "konfRaumId Rede 2: Text zu lang (maximal 200 Zeichen)";
        }
        if (tfKonfRaumIdR3.getText().length() > 200) {
            lFehlertext = "konfRaumId Rede 3: Text zu lang (maximal 200 Zeichen)";
        }

        if (!CaString.isNummern(tfKonfRaumAnzahlTest.getText())) {
            lFehlertext = "Anzahl Konferenz-Räume Test: Nur Nummer zulässig!";
        }
        else {
            if (Integer.parseInt(tfKonfRaumAnzahlTest.getText())>6) {
                lFehlertext = "Anzahl Konferenz-Räume Test: Maximal 6 zulöässug!";
            }
        }
        if (!CaString.isNummern(tfKonfRaumAnzahlReden.getText())) {
            lFehlertext = "Anzahl Konferenz-Räume Test: Nur Nummer zulässig!";
        }
        else {
            if (Integer.parseInt(tfKonfRaumAnzahlReden.getText())>6) {
                lFehlertext = "Anzahl Konferenz-Räume Rede: Maximal 6 zulöässug!";
            }
        }

        pruefe01(tfKonfGlobaleTestraeumeNutzen, "Globale Testräume nutzen");

        /************* Online-HV ***********************/
        //    	pruefe01(tfOnlineTeilnahmeAngeboten, "Online-Teilnahme angeboten");
        //    	pruefe01(tfOnlineTeilnahmeAktiv, "Online-Teilnahme aktiv");
        pruefe01(tfOnlineTeilnehmerAbfragen, "Online-Teilnehmer abfragen");
        pruefe01(tfOnlineTeilnahmeSeparateNutzungsbedingungen, "Separate Nutzungsbedingungen Online-Teilnahme");
        pruefe0123(tfOnlineTeilnahmeAktionaerAlsGast, "Online-Teilnahme für Aktionär als Gast");

        /******************** App *********************/
        pruefe01(tfAppAktiv, "App aktiv");
        if (!CaString.isNummern(tfAppSprache.getText())) {
            lFehlertext = "App Sprache: Nur Nummer zulässig!";
        }
        pruefe01(tfImpressumEmittent, "Kundenspezifisches Impressum");
        if (!CaString.isNummern(tfAnzeigeStartseite.getText())) {
            lFehlertext = "Startseite Mandant anzeigen: Nur Nummer zulässig!";
        }

        if (!lFehlertext.isEmpty()) {
            CaZeigeHinweis caZeigeHinweis = new CaZeigeHinweis();
            caZeigeHinweis.zeige(eigeneStage, lFehlertext);
            return false;
        }

        /*============================Speichern===============================*/

        /******************** Allgemein ******************************/
        ParamS.eclEmittent.portalAktuellAktiv = Integer.parseInt(tfPortalAktuellAktiv.getText());
        ParamS.eclEmittent.portalStandard = Integer.parseInt(tfPortalStandard.getText());
        ParamS.eclEmittent.portalIstDauerhaft = Integer.parseInt(tfPortalIstDauerhaft.getText());
        ParamS.param.paramPortal.testModus = Integer.parseInt(tfTestModus.getText());

        ParamS.eclEmittent.portalSprache = Integer.parseInt(tfPortalAuchInEnglischVerfuegbar.getText());
        ParamS.param.paramPortal.artSprachumschaltung = Integer.parseInt(tfArtSprachumschaltung.getText());
        ParamS.param.paramPortal.datumsformatDE = Integer.parseInt(tfDatumsformatDE.getText());
        ParamS.param.paramPortal.datumsformatEN = Integer.parseInt(tfDatumsformatEN.getText());
        ParamS.param.paramPortal.letzterAktienregisterUpdate = tfLetzterAktienregisterUpdate.getText();
        ParamS.param.paramPortal.bestaetigenDialog = Integer.parseInt(tfBestaetigenDialog.getText());
        ParamS.param.paramPortal.quittungDialog = Integer.parseInt(tfQuittungDialog.getText());
        ParamS.param.paramPortal.kontaktFenster = Integer.parseInt(tfKontaktFenster.getText());
        ParamS.param.paramPortal.personenAnzeigeAnredeMitAufnahmen = Integer.parseInt(tfPersonenAnzeigeAnredeMitAufnahmen.getText());
        ParamS.param.paramPortal.textPostfachMitAufnahmen = Integer.parseInt(tfTextPostfachMitAufnahmen.getText());
        ParamS.param.paramPortal.anzeigeStimmenKennung = Integer.parseInt(tfAnzeigeStimmenKennung.getText());

        /********************* Login ****************************************/
        ParamS.param.paramPortalServer.appInstallButtonsAnzeigen = Integer.parseInt(tfAppInstallButtonsAnzeigen.getText());
        ParamS.param.paramPortal.passwortCaseSensitiv = Integer.parseInt(tfPasswortCaseSensitiv.getText());
        ParamS.param.paramPortal.passwortMindestLaenge = Integer.parseInt(tfPasswortMindestLaenge.getText());
        ParamS.param.paramPortal.verfahrenPasswortVergessen = Integer.parseInt(tfVerfahrenPasswortVergessen.getText());
        ParamS.param.paramPortal.passwortPerPostPruefen = Integer.parseInt(tfPasswortPerPostPruefen.getText());

        if (cbGattung1Moeglich.isSelected()) {
            ParamS.param.paramPortal.portalFuerGattungMoeglich[0] = 1;
        } else {
            ParamS.param.paramPortal.portalFuerGattungMoeglich[0] = 0;
        }
        if (cbGattung2Moeglich.isSelected()) {
            ParamS.param.paramPortal.portalFuerGattungMoeglich[1] = 1;
        } else {
            ParamS.param.paramPortal.portalFuerGattungMoeglich[1] = 0;
        }
        if (cbGattung3Moeglich.isSelected()) {
            ParamS.param.paramPortal.portalFuerGattungMoeglich[2] = 1;
        } else {
            ParamS.param.paramPortal.portalFuerGattungMoeglich[2] = 0;
        }
        if (cbGattung4Moeglich.isSelected()) {
            ParamS.param.paramPortal.portalFuerGattungMoeglich[3] = 1;
        } else {
            ParamS.param.paramPortal.portalFuerGattungMoeglich[3] = 0;
        }
        if (cbGattung5Moeglich.isSelected()) {
            ParamS.param.paramPortal.portalFuerGattungMoeglich[4] = 1;
        } else {
            ParamS.param.paramPortal.portalFuerGattungMoeglich[4] = 0;
        }

        ParamS.param.paramPortal.loginGesperrt = Integer.parseInt(tfLoginGesperrt.getText());
        ParamS.param.paramPortal.loginGesperrtTextDeutsch = tfLoginGesperrtTextDeutsch.getText();
        ParamS.param.paramPortal.loginGesperrtTextEnglisch = tfLoginGesperrtTextEnglisch.getText();
        ParamS.param.paramPortal.logoutZiel = tfLogoutZiel.getText();
        ParamS.param.paramPortal.loginVerfahren = Integer.parseInt(tfLoginVerfahren.getText());

        ParamS.param.paramPortal.verfahrenPasswortVergessenBeiEmailHinterlegtAuchPost = Integer.parseInt(tfVerfahrenPasswortVergessenBeiEmailHinterlegtAuchPost.getText());
        ParamS.param.paramPortal.linkEmailEnthaeltLinkOderCode = Integer.parseInt(tfLinkEmailEnthaeltLinkOderCode.getText());
        ParamS.param.paramPortal.emailBestaetigenIstZwingend = Integer.parseInt(tfEmailBestaetigenIstZwingend.getText());

        ParamS.param.paramPortal.loginIPTrackingAktiv = Integer.parseInt(tfLoginIPTracking.getText());

        ParamS.param.paramPortal.captchaVerwenden = Integer.parseInt(tfCaptchaVerwenden.getText());
        ParamS.param.paramPortal.loginVerzoegerungAbVersuch = Integer.parseInt(tfLoginVerzoegerungAbVersuch.getText());
        ParamS.param.paramPortal.loginVerzoegerungSekunden = Integer.parseInt(tfLoginVerzoegerungSekunden.getText());
        ParamS.param.paramPortal.alternativeLoginKennung = Integer.parseInt(tfAlternativeLoginKennung.getText());
        ParamS.param.paramPortal.anzahlEindeutigeKennungenVorhanden = Integer.parseInt(tfAnzahlEindeutigeKennungenVorhanden.getText());
        ParamS.param.paramPortal.teilnehmerKannSichWeitereKennungenZuordnen = Integer.parseInt(tfTeilnehmerKannSichWeitereKennungenZuordnen.getText());
        ParamS.param.paramPortal.kennungAufbereiten = Integer.parseInt(tfKennungAufbereiten.getText());
        ParamS.param.paramPortal.kennungAufbereitenFuerAnzeige = Integer.parseInt(tfKennungAufbereitenFuerAnzeige.getText());

        /****************Registrierung/Einstellungen******************/
        ParamS.param.paramPortal.dauerhaftesPasswortMoeglich = Integer.parseInt(tfDauerhaftesPasswortMoeglich.getText());
        ParamS.param.paramPortal.registrierungFuerEmailVersandMoeglich = Integer.parseInt(tfRegistrierungFuerEmailVersandMoeglich.getText());
        ParamS.param.paramPortal.emailVersandRegistrierungOderWiderspruch = Integer.parseInt(tfEmailVersandRegistrierungOderWiderspruch.getText());
        ParamS.param.paramPortal.adressaenderungMoeglich = Integer.parseInt(tfAdressaenderungMoeglich.getText());
        ParamS.param.paramPortal.kommunikationsspracheAuswahl = Integer.parseInt(tfKommunikationsspracheAuswahl.getText());
        ParamS.param.paramPortal.publikationenAnbieten = Integer.parseInt(tfPublikationenAnbieten.getText());
        ParamS.param.paramPortal.kontaktDetailsAnbieten = Integer.parseInt(tfKontaktDetailsAnbieten.getText());
        ParamS.param.paramPortal.emailNurBeiEVersandOderPasswort = Integer.parseInt(tfEmailNurBeiEVersandOderPasswort.getText());
        ParamS.param.paramPortal.cookieHinweis = Integer.parseInt(tfCookieHinweis.getText());
        ParamS.param.paramPortal.separateTeilnahmebedingungenFuerGewinnspiel = Integer.parseInt(tfSeparateTeilnahmebedingungenFuerGewinnspiel.getText());
        ParamS.param.paramPortal.separateDatenschutzerklaerung = Integer.parseInt(tfSeparateDatenschutzerklaerung.getText());
        ParamS.param.paramPortal.bestaetigenHinweisAktionaersportal = Integer.parseInt(tfBestaetigenHinweisAktionaersportal.getText());
        ParamS.param.paramPortal.bestaetigenHinweisHVportal = Integer.parseInt(tfBestaetigenHinweisHVportal.getText());
        ParamS.param.paramPortal.bestaetigungsseiteEinstellungen = Integer.parseInt(tfBestaetigungsseiteEinstellungen.getText());
        ParamS.param.paramPortal.absendeMailAdresse = Integer.parseInt(tfAbsendeMailAdresse.getText());
        ParamS.param.paramPortal.reihenfolgeRegistrierung = Integer.parseInt(tfReihenfolgeRegistrierung.getText());
        ParamS.param.paramPortal.mailEingabeServiceline = Integer.parseInt(tfMailEingabeServiceline.getText());

        /**********************
         * Erklärungen
         ***********************************************/
        ParamS.param.paramPortal.srvAngeboten = Integer.parseInt(tfSrvAngeboten.getText());
        ParamS.param.paramPortal.briefwahlAngeboten = Integer.parseInt(tfBriefwahlAngeboten.getText());
        ParamS.param.paramPortal.vollmachtDritteAngeboten = Integer.parseInt(tfVollmachtDritteAngeboten.getText());
        ParamS.param.paramPortal.vollmachtsnachweisAufStartseiteAktiv = Integer.parseInt(tfVollmachtsnachweisAufStartseiteAktiv.getText());
        ParamS.param.paramPortal.bestaetigungStimmabgabeNachHV = Integer.parseInt(tfBestaetigungStimmabgabeNachHV.getText());
        ParamS.param.paramPortal.bestaetigungPerEmailUeberallZulassen = Integer.parseInt(tfBestaetigungPerEmailUeberallZulassen.getText());
        ParamS.param.paramPortal.vollmachtKIAVAngeboten = Integer.parseInt(tfVollmachtKIAVAngeboten.getText());
        ParamS.param.paramPortal.weisungenAktuellNichtMoeglich = Integer.parseInt(tfWeisungAktuellNichtMoeglich.getText());
        ParamS.param.paramPortal.weisungenAktuellNichtMoeglichAberBriefwahlSchon = Integer.parseInt(tfWeisungAktuellNichtMoeglichAberBriefwahlSchon.getText());

        ParamS.param.paramPortal.gastkartenAnforderungMoeglich = Integer.parseInt(tfGastkartenAnforderungMoeglich.getText());
        ParamS.param.paramPortal.oeffentlicheIDMoeglich = Integer.parseInt(tfOeffentlicheIDMoeglich.getText());

        ParamS.param.paramPortal.zusaetzlicheEKDritteMoeglich = Integer.parseInt(tfZusaetzlicheEKDritteMoeglich.getText());
        ParamS.param.paramPortal.ekUndWeisungGleichzeitigMoeglich = Integer.parseInt(tfEkUndWeisungGleichzeitigMoeglich.getText());
        ParamS.param.paramPortal.briefwahlZusaetzlichZuSRVMoeglich = Integer.parseInt(tfBriefwahlZusaetzlichZuSRVMoeglich.getText());

        ParamS.param.paramPortal.vollmachtDritteUndAndereWKMoeglich = Integer.parseInt(tfVollmachtDritteUndAndereWKMoeglich.getText());
        ParamS.param.paramPortal.handhabungWeisungDurchVerschiedene = Integer.parseInt(tfHandhabungWeisungDurchVerschiedene.getText());

        ParamS.param.paramPortal.erklAnPos1 = Integer.parseInt(tfErklAnPos1.getText());
        ParamS.param.paramPortal.erklAnPos2 = Integer.parseInt(tfErklAnPos2.getText());
        ParamS.param.paramPortal.erklAnPos3 = Integer.parseInt(tfErklAnPos3.getText());
        ParamS.param.paramPortal.erklAnPos4 = Integer.parseInt(tfErklAnPos4.getText());
        ParamS.param.paramPortal.erklAnPos5 = Integer.parseInt(tfErklAnPos5.getText());

        ParamS.param.paramPortal.ekSelbstMoeglich = Integer.parseInt(tfEkSelbstMoeglich.getText());
        ParamS.param.paramPortal.ekVollmachtMoeglich = Integer.parseInt(tfEkVollmachtMoeglich.getText());
        ParamS.param.paramPortal.ek2PersonengemeinschaftMoeglich = Integer.parseInt(tfEk2PersonengemeinschaftMoeglich.getText());
        ParamS.param.paramPortal.ek2MitOderOhneVollmachtMoeglich = Integer.parseInt(tfEk2MitOderOhneVollmachtMoeglich.getText());
        ParamS.param.paramPortal.ek2SelbstMoeglich = Integer.parseInt(tfEk2SelbstMoeglich.getText());
        ParamS.param.paramPortal.anmeldenOhneWeitereWK = Integer.parseInt(tfAnmeldenOhneWeitereWK.getText());

        /***************************** Weisung **************************************/

        ParamS.param.paramPortal.pNichtmarkiertSpeichernAls = Integer.parseInt(tfPNichtmarkiertSpeichernAls.getText());

        if (cbGesamtMarkierungJa.isSelected()) {
            ParamS.param.paramPortal.gesamtMarkierungJa = 1;
        } else {
            ParamS.param.paramPortal.gesamtMarkierungJa = 0;
        }
        if (cbGesamtMarkierungNein.isSelected()) {
            ParamS.param.paramPortal.gesamtMarkierungNein = 1;
        } else {
            ParamS.param.paramPortal.gesamtMarkierungNein = 0;
        }
        if (cbGesamtMarkierungEnthaltung.isSelected()) {
            ParamS.param.paramPortal.gesamtMarkierungEnthaltung = 1;
        } else {
            ParamS.param.paramPortal.gesamtMarkierungEnthaltung = 0;
        }
        if (cbGesamtMarkierungImSinne.isSelected()) {
            ParamS.param.paramPortal.gesamtMarkierungImSinne = 1;
        } else {
            ParamS.param.paramPortal.gesamtMarkierungImSinne = 0;
        }
        if (cbGesamtMarkierungGegenSinne.isSelected()) {
            ParamS.param.paramPortal.gesamtMarkierungGegenSinne = 1;
        } else {
            ParamS.param.paramPortal.gesamtMarkierungGegenSinne = 0;
        }
        if (cbGesamtMarkierungAllesLoeschen.isSelected()) {
            ParamS.param.paramPortal.gesamtMarkierungAllesLoeschen = 1;
        } else {
            ParamS.param.paramPortal.gesamtMarkierungAllesLoeschen = 0;
        }

        if (cbMarkierungJa.isSelected()) {
            ParamS.param.paramPortal.markierungJa = 1;
        } else {
            ParamS.param.paramPortal.markierungJa = 0;
        }
        if (cbMarkierungNein.isSelected()) {
            ParamS.param.paramPortal.markierungNein = 1;
        } else {
            ParamS.param.paramPortal.markierungNein = 0;
        }
        if (cbMarkierungEnthaltung.isSelected()) {
            ParamS.param.paramPortal.markierungEnthaltung = 1;
        } else {
            ParamS.param.paramPortal.markierungEnthaltung = 0;
        }
        if (cbMarkierungLoeschen.isSelected()) {
            ParamS.param.paramPortal.markierungLoeschen = 1;
        } else {
            ParamS.param.paramPortal.markierungLoeschen = 0;
        }

        ParamS.param.paramPortal.gegenantraegeWeisungenMoeglich = Integer.parseInt(tfGegenantraegeWeisungenMoeglich.getText());
        ParamS.param.paramPortal.gegenantragsText = Integer.parseInt(tfGegenantragsText.getText());

        if (cbCheckboxBeiSRV.isSelected()) {
            ParamS.param.paramPortal.checkboxBeiSRV = 1;
        } else {
            ParamS.param.paramPortal.checkboxBeiSRV = 0;
        }
        if (cbCheckboxBeiBriefwahl.isSelected()) {
            ParamS.param.paramPortal.checkboxBeiBriefwahl = 1;
        } else {
            ParamS.param.paramPortal.checkboxBeiBriefwahl = 0;
        }
        if (cbCheckboxBeiKIAV.isSelected()) {
            ParamS.param.paramPortal.checkboxBeiKIAV = 1;
        } else {
            ParamS.param.paramPortal.checkboxBeiKIAV = 0;
        }
        if (cbCheckboxBeiVollmacht.isSelected()) {
            ParamS.param.paramPortal.checkboxBeiVollmacht = 1;
        } else {
            ParamS.param.paramPortal.checkboxBeiVollmacht = 0;
        }

        if (cbJnAbfrageBeiWeisungQuittung.isSelected()) {
            ParamS.param.paramPortal.jnAbfrageBeiWeisungQuittung = 1;
        } else {
            ParamS.param.paramPortal.jnAbfrageBeiWeisungQuittung = 0;
        }

        ParamS.param.paramPortal.bestaetigungBeiWeisung = Integer.parseInt(tfBestaetigungBeiWeisung.getText());
        ParamS.param.paramPortal.bestaetigungBeiWeisungMitTOP = Integer.parseInt(tfBestaetigungBeiWeisungMitTOP.getText());
        ParamS.param.paramPortal.sammelkartenFuerAenderungSperren = Integer.parseInt(tfSammelkartenFuerAenderungSperren.getText());

        /**********************Text-Inhalte*******************************/
        ParamS.param.paramPortal.standardTexteBeruecksichtigen = Integer.parseInt(tfStandardTexteBeruecksichtigen.getText());
        ParamS.param.paramPortal.basisSetStandardTexteVerwenden = Integer.parseInt(tfBasisSetStandardTexteVerwenden.getText());
        ParamS.param.paramPortal.fragezeichenHinweiseVerwenden = Integer.parseInt(tfFragezeichenHinweiseVerwenden.getText());
        ParamS.param.paramPortal.mehrereStimmrechtsvertreter = Integer.parseInt(tfMehrereStimmrechtsvertreter.getText());

        ParamS.param.paramPortal.stimmrechtsvertreterNameDE = tfStimmrechtsvertreterNameDE.getText();
        ParamS.param.paramPortal.stimmrechtsvertreterNameEN = tfStimmrechtsvertreterNameEN.getText();

        ParamS.param.paramPortal.kurzLinkPortal = tfKurzLinkPortal.getText();
        ParamS.param.paramPortal.subdomainPortal = tfSubdomainPortal.getText();
        ParamS.param.paramPortal.linkTagesordnung = tfLinkTagesordnung.getText();
        ParamS.param.paramPortal.linkGegenantraege = tfLinkGegenantraege.getText();
        ParamS.param.paramPortal.linkEinladungsPDF = tfLinkEinladungsPDF.getText();

        ParamS.param.paramPortal.linkNutzungsbedingungenAktionaersPortal = tfLinkNutzungsbedingungenAktionaersPortal.getText();
        ParamS.param.paramPortal.linkNutzungsbedingungenHVPortal = tfLinkNutzungsbedingungenHVPortal.getText();
        ParamS.param.paramPortal.linkDatenschutzhinweise = tfLinkDatenschutzhinweise.getText();
        ParamS.param.paramPortal.linkDatenschutzhinweiseKunde = tfLinkDatenschutzhinweiseKunde.getText();
        ParamS.param.paramPortal.linkImpressum = tfLinkImpressum.getText();

        ParamS.param.paramPortal.ekText = tfEkText.getText();
        ParamS.param.paramPortal.ekTextMitArtikel = tfEkTextMitArtikel.getText();
        ParamS.param.paramPortal.ekTextEN = tfEkTextEN.getText();
        ParamS.param.paramPortal.ekTextENMitArtikel = ekTextENMitArtikel.getText();

        ParamS.param.paramPortal.emailAdresseLink = tfEmailAdresseLink.getText();
        ParamS.param.paramPortal.emailAdresseText = tfEmailAdresseText.getText();

        ParamS.param.paramPortal.vollmachtEmailAdresseLink = tfVollmachtEmailAdresseLink.getText();
        ParamS.param.paramPortal.vollmachtEmailAdresseText = tfVollmachtEmailAdresseText.getText();

        ParamS.param.paramPortal.logoutObenOderUnten = Integer.parseInt(tfLogoutObenOderUnten.getText());
        ParamS.param.paramPortal.logoPosition = Integer.parseInt(tfLogoPosition.getText());
        ParamS.param.paramPortal.logoName = tfLogoName.getText();
        ParamS.param.paramPortal.logoBreite = Integer.parseInt(tfLogoBreite.getText());
        ParamS.param.paramPortal.cssName = tfCSSName.getText();
        ParamS.param.paramPortal.designKuerzel = tfDesignKuerzel.getText();

        ParamS.param.paramPortal.farbeHeader = tfFarbeHeader.getText();
        ParamS.param.paramPortal.farbeLink = tfFarbeLink.getText();
        ParamS.param.paramPortal.farbeLinkHover = tfFarbeLinkHover.getText();
        ParamS.param.paramPortal.farbeListeUngerade = tfFarbeListeUngerade.getText();
        ParamS.param.paramPortal.farbeListeGerade = tfFarbeListeGerade.getText();
        ParamS.param.paramPortal.farbeHintergrund = tfFarbeHintergrund.getText();
        ParamS.param.paramPortal.farbeText = tfFarbeText.getText();
        ParamS.param.paramPortal.farbeUeberschriftHintergrund = tfFarbeUeberschriftHintergrund.getText();
        ParamS.param.paramPortal.farbeUeberschrift = tfFarbeUeberschrift.getText();

        /* +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++ */

        ParamS.param.paramPortal.kachelFarbe = tfKachelFarbe.getText();
        ParamS.param.paramPortal.themeFarbe = tfThemeFarbe.getText();

        ParamS.param.paramPortal.schriftgroesseGlobal = tfSchriftgroesseGlobal.getText();
        ParamS.param.paramPortal.logoMindestbreite = tfLogoMindestbreite.getText();
        ParamS.param.paramPortal.farbeHintergrundBtn00 = tfFarbeHintergrundBtn00.getText();
        ParamS.param.paramPortal.farbeSchriftBtn00 = tfFarbeSchriftBtn00.getText();
        ParamS.param.paramPortal.farbeRahmenBtn00 = tfFarbeRahmenBtn00.getText();
        ParamS.param.paramPortal.breiteRahmenBtn00 = tfBreiteRahmenBtn00.getText();
        ParamS.param.paramPortal.radiusRahmenBtn00 = tfRadiusRahmenBtn00.getText();
        ParamS.param.paramPortal.stilRahmenBtn00 = tfStilRahmenBtn00.getText();
        ParamS.param.paramPortal.farbeHintergrundBtn00Hover = tfFarbeHintergrundBtn00Hover.getText();
        ParamS.param.paramPortal.farbeSchriftBtn00Hover = tfFarbeSchriftBtn00Hover.getText();

        ParamS.param.paramPortal.farbeRahmenBtn00Hover = tfFarbeRahmenBtn00Hover.getText();
        ParamS.param.paramPortal.breiteRahmenBtn00Hover = tfBreiteRahmenBtn00Hover.getText();
        ParamS.param.paramPortal.radiusRahmenBtn00Hover = tfRadiusRahmenBtn00Hover.getText();
        ParamS.param.paramPortal.stilRahmenBtn00Hover = tfStilRahmenBtn00Hover.getText();
        ParamS.param.paramPortal.farbeFocus = tfFarbeFocus.getText();
        ParamS.param.paramPortal.farbeError = tfFarbeError.getText();
        ParamS.param.paramPortal.farbeErrorSchrift = tfFarbeErrorSchrift.getText();
        ParamS.param.paramPortal.farbeWarning = tfFarbeWarning.getText();
        ParamS.param.paramPortal.farbeWarningSchrift = tfFarbeWarningSchrift.getText();
        ParamS.param.paramPortal.farbeSuccess = tfFarbeSuccess.getText();

        ParamS.param.paramPortal.farbeSuccessSchrift = tfFarbeSuccessSchrift.getText();
        ParamS.param.paramPortal.farbeRahmenEingabefelder = tfFarbeRahmenEingabefelder.getText();
        ParamS.param.paramPortal.breiteRahmenEingabefelder = tfBreiteRahmenEingabefelder.getText();
        ParamS.param.paramPortal.radiusRahmenEingabefelder = tfRadiusRahmenEingabefelder.getText();
        ParamS.param.paramPortal.stilRahmenEingabefelder = tfStilRahmenEingabefelder.getText();
        ParamS.param.paramPortal.farbeHintergrundLogoutBtn = tfFarbeHintergrundLogoutBtn.getText();
        ParamS.param.paramPortal.farbeSchriftLogoutBtn = tfFarbeSchriftLogoutBtn.getText();
        ParamS.param.paramPortal.farbeRahmenLogoutBtn = tfFarbeRahmenLogoutBtn.getText();
        ParamS.param.paramPortal.breiteRahmenLogoutBtn = tfBreiteRahmenLogoutBtn.getText();
        ParamS.param.paramPortal.radiusRahmenLogoutBtn = tfRadiusRahmenLogoutBtn.getText();

        ParamS.param.paramPortal.stilRahmenLogoutBtn = tfStilRahmenLogoutBtn.getText();
        ParamS.param.paramPortal.farbeHintergrundLogoutBtnHover = tfFarbeHintergrundLogoutBtnHover.getText();
        ParamS.param.paramPortal.farbeSchriftLogoutBtnHover = tfFarbeSchriftLogoutBtnHover.getText();
        ParamS.param.paramPortal.farbeRahmenLogoutBtnHover = tfFarbeRahmenLogoutBtnHover.getText();
        ParamS.param.paramPortal.breiteRahmenLogoutBtnHover = tfBreiteRahmenLogoutBtnHover.getText();
        ParamS.param.paramPortal.radiusRahmenLogoutBtnHover = tfRadiusRahmenLogoutBtnHover.getText();
        ParamS.param.paramPortal.stilRahmenLogoutBtnHover = tfStilRahmenLogoutBtnHover.getText();
        ParamS.param.paramPortal.farbeHintergrundLoginBtn = tfFarbeHintergrundLoginBtn.getText();
        ParamS.param.paramPortal.farbeSchriftLoginBtn = tfFarbeSchriftLoginBtn.getText();
        ParamS.param.paramPortal.farbeRahmenLoginBtn = tfFarbeRahmenLoginBtn.getText();

        ParamS.param.paramPortal.breiteRahmenLoginBtn = tfBreiteRahmenLoginBtn.getText();
        ParamS.param.paramPortal.radiusRahmenLoginBtn = tfRadiusRahmenLoginBtn.getText();
        ParamS.param.paramPortal.stilRahmenLoginBtn = tfStilRahmenLoginBtn.getText();
        ParamS.param.paramPortal.farbeHintergrundLoginBtnHover = tfFarbeHintergrundLoginBtnHover.getText();
        ParamS.param.paramPortal.farbeSchriftLoginBtnHover = tfFarbeSchriftLoginBtnHover.getText();
        ParamS.param.paramPortal.farbeRahmenLoginBtnHover = tfFarbeRahmenLoginBtnHover.getText();
        ParamS.param.paramPortal.breiteRahmenLoginBtnHover = tfBreiteRahmenLoginBtnHover.getText();
        ParamS.param.paramPortal.radiusRahmenLoginBtnHover = tfRadiusRahmenLoginBtnHover.getText();
        ParamS.param.paramPortal.stilRahmenLoginBtnHover = tfStilRahmenLoginBtnHover.getText();
        ParamS.param.paramPortal.farbeRahmenLoginBereich = tfFarbeRahmenLoginBereich.getText();

        ParamS.param.paramPortal.breiteRahmenLoginBereich = tfBreiteRahmenLoginBereich.getText();
        ParamS.param.paramPortal.radiusRahmenLoginBereich = tfRadiusRahmenLoginBereich.getText();
        ParamS.param.paramPortal.stilRahmenLoginBereich = tfStilRahmenLoginBereich.getText();
        ParamS.param.paramPortal.farbeHintergrundLoginBereich = tfFarbeHintergrundLoginBereich.getText();
        ParamS.param.paramPortal.farbeLinkLoginBereich = tfFarbeLinkLoginBereich.getText();
        ParamS.param.paramPortal.farbeLinkHoverLoginBereich = tfFarbeLinkHoverLoginBereich.getText();
        ParamS.param.paramPortal.farbeRahmenEingabefelderLoginBereich = tfFarbeRahmenEingabefelderLoginBereich.getText();
        ParamS.param.paramPortal.breiteRahmenEingabefelderLoginBereich = tfBreiteRahmenEingabefelderLoginBereich.getText();
        ParamS.param.paramPortal.radiusRahmenEingabefelderLoginBereich = tfRadiusRahmenEingabefelderLoginBereich.getText();
        ParamS.param.paramPortal.stilRahmenEingabefelderLoginBereich = tfStilRahmenEingabefelderLoginBereich.getText();

        ParamS.param.paramPortal.farbeBestandsbereichUngeradeReihe = tfFarbeBestandsbereichUngeradeReihe.getText();
        ParamS.param.paramPortal.farbeBestandsbereichGeradeReihe = tfFarbeBestandsbereichGeradeReihe.getText();
        ParamS.param.paramPortal.farbeLineUntenBestandsbereich = tfFarbeLineUntenBestandsbereich.getText();
        ParamS.param.paramPortal.breiteLineUntenBestandsbereich = tfBreiteLineUntenBestandsbereich.getText();
        ParamS.param.paramPortal.stilLineUntenBestandsbereich = tfStilLineUntenBestandsbereich.getText();
        ParamS.param.paramPortal.farbeRahmenAnmeldeuebersicht = tfFarbeRahmenAnmeldeuebersicht.getText();
        ParamS.param.paramPortal.breiteRahmenAnmeldeuebersicht = tfBreiteRahmenAnmeldeuebersicht.getText();
        ParamS.param.paramPortal.radiusRahmenAnmeldeuebersicht = tfRadiusRahmenAnmeldeuebersicht.getText();
        ParamS.param.paramPortal.stilRahmenAnmeldeuebersicht = tfStilRahmenAnmeldeuebersicht.getText();

        ParamS.param.paramPortal.farbeTrennlinieAnmeldeuebersicht = tfFarbeTrennlinieAnmeldeuebersicht.getText();
        ParamS.param.paramPortal.breiteTrennlinieAnmeldeuebersicht = tfBreiteTrennlinieAnmeldeuebersicht.getText();
        ParamS.param.paramPortal.stilTrennlinieAnmeldeuebersicht = tfStilTrennlinieAnmeldeuebersicht.getText();
        ParamS.param.paramPortal.farbeRahmenErteilteWillenserklärungen = tfFarbeRahmenErteilteWillenserklärungen.getText();
        ParamS.param.paramPortal.breiteRahmenErteilteWillenserklärungen = tfBreiteRahmenErteilteWillenserklärungen.getText();
        ParamS.param.paramPortal.radiusRahmenErteilteWillenserklärungen = tfRadiusRahmenErteilteWillenserklärungen.getText();
        ParamS.param.paramPortal.stilRahmenErteilteWillenserklärungen = tfStilRahmenErteilteWillenserklärungen.getText();
        ParamS.param.paramPortal.farbeHintergrundErteilteWillenserklärungen = tfFarbeHintergrundErteilteWillenserklärungen.getText();
        ParamS.param.paramPortal.farbeSchriftErteilteWillenserklärungen = tfFarbeSchriftErteilteWillenserklärungen.getText();
        ParamS.param.paramPortal.farbeRahmenAbstimmungstabelle = tfFarbeRahmenAbstimmungstabelle.getText();
        ParamS.param.paramPortal.breiteRahmenAbstimmungstabelle = tfBreiteRahmenAbstimmungstabelle.getText();

        ParamS.param.paramPortal.radiusRahmenAbstimmungstabelle = tfRadiusRahmenAbstimmungstabelle.getText();
        ParamS.param.paramPortal.stilRahmenAbstimmungstabelle = tfStilRahmenAbstimmungstabelle.getText();
        ParamS.param.paramPortal.farbeHintergrundAbstimmungstabelleUngeradeReihen = tfFarbeHintergrundAbstimmungstabelleUngeradeReihen.getText();
        ParamS.param.paramPortal.farbeSchriftAbstimmungstabelleUngeradeReihen = tfFarbeSchriftAbstimmungstabelleUngeradeReihen.getText();
        ParamS.param.paramPortal.farbeHintergrundAbstimmungstabelleGeradeReihen = tfFarbeHintergrundAbstimmungstabelleGeradeReihen.getText();
        ParamS.param.paramPortal.farbeSchriftAbstimmungstabelleGeradeReihen = tfFarbeSchriftAbstimmungstabelleGeradeReihen.getText();
        ParamS.param.paramPortal.farbeHintergrundWeisungJa = tfFarbeHintergrundWeisungJa.getText();
        ParamS.param.paramPortal.farbeSchriftWeisungJa = tfFarbeSchriftWeisungJa.getText();
        ParamS.param.paramPortal.farbeRahmenWeisungJa = tfFarbeRahmenWeisungJa.getText();
        ParamS.param.paramPortal.farbeHintergrundWeisungJaChecked = tfFarbeHintergrundWeisungJaChecked.getText();

        ParamS.param.paramPortal.farbeSchriftWeisungJaChecked = tfFarbeSchriftWeisungJaChecked.getText();
        ParamS.param.paramPortal.farbeRahmenWeisungJaChecked = tfFarbeRahmenWeisungJaChecked.getText();
        ParamS.param.paramPortal.farbeHintergrundWeisungNein = tfFarbeHintergrundWeisungNein.getText();
        ParamS.param.paramPortal.farbeSchriftWeisungNein = tfFarbeSchriftWeisungNein.getText();
        ParamS.param.paramPortal.farbeRahmenWeisungNein = tfFarbeRahmenWeisungNein.getText();
        ParamS.param.paramPortal.farbeHintergrundWeisungNeinChecked = tfFarbeHintergrundWeisungNeinChecked.getText();
        ParamS.param.paramPortal.farbeSchriftWeisungNeinChecked = tfFarbeSchriftWeisungNeinChecked.getText();
        ParamS.param.paramPortal.farbeRahmenWeisungNeinChecked = tfFarbeRahmenWeisungNeinChecked.getText();
        ParamS.param.paramPortal.farbeHintergrundWeisungEnthaltung = tfFarbeHintergrundWeisungEnthaltung.getText();
        ParamS.param.paramPortal.farbeSchriftWeisungEnthaltung = tfFarbeSchriftWeisungEnthaltung.getText();

        ParamS.param.paramPortal.farbeRahmenWeisungEnthaltung = tfFarbeRahmenWeisungEnthaltung.getText();
        ParamS.param.paramPortal.farbeHintergrundWeisungEnthaltungChecked = tfFarbeHintergrundWeisungEnthaltungChecked.getText();
        ParamS.param.paramPortal.farbeSchriftWeisungEnthaltungChecked = tfFarbeSchriftWeisungEnthaltungChecked.getText();
        ParamS.param.paramPortal.farbeRahmenWeisungEnthaltungChecked = tfFarbeRahmenWeisungEnthaltungChecked.getText();
        ParamS.param.paramPortal.farbeHintergrundFooterTop = tfFarbeHintergrundFooterTop.getText();
        ParamS.param.paramPortal.farbeSchriftFooterTop = tfFarbeSchriftFooterTop.getText();
        ParamS.param.paramPortal.farbeLinkFooterTop = tfFarbeLinkFooterTop.getText();
        ParamS.param.paramPortal.farbeLinkFooterTopHover = tfFarbeLinkFooterTopHover.getText();
        ParamS.param.paramPortal.farbeHintergrundFooterBottom = tfFarbeHintergrundFooterBottom.getText();
        ParamS.param.paramPortal.farbeSchriftFooterBottom = tfFarbeSchriftFooterBottom.getText();

        ParamS.param.paramPortal.farbeLinkFooterBottom = tfFarbeLinkFooterBottom.getText();
        ParamS.param.paramPortal.farbeLinkFooterBottomHover = tfFarbeLinkFooterBottomHover.getText();
        ParamS.param.paramPortal.farbeHintergrundModal = tfFarbeHintergrundModal.getText();
        ParamS.param.paramPortal.farbeSchriftModal = tfFarbeSchriftModal.getText();
        ParamS.param.paramPortal.farbeHintergrundModalHeader = tfFarbeHintergrundModalHeader.getText();
        ParamS.param.paramPortal.farbeSchriftModalHeader = tfFarbeSchriftModalHeader.getText();
        ParamS.param.paramPortal.farbeTrennlinieModal = tfFarbeTrennlinieModal.getText();
        ParamS.param.paramPortal.farbeHintergrundUntenButtons = tfFarbeHintergrundUntenButtons.getText();
        ParamS.param.paramPortal.farbeSchriftUntenButtons = tfFarbeSchriftUntenButtons.getText();
        ParamS.param.paramPortal.farbeRahmenUntenButtons = tfFarbeRahmenUntenButtons.getText();

        ParamS.param.paramPortal.breiteRahmenUntenButtons = tfBreiteRahmenUntenButtons.getText();
        ParamS.param.paramPortal.radiusRahmenUntenButtons = tfRadiusRahmenUntenButtons.getText();
        ParamS.param.paramPortal.stilRahmenUntenButtons = tfStilRahmenUntenButtons.getText();
        ParamS.param.paramPortal.farbeHintergrundUntenButtonsHover = tfFarbeHintergrundUntenButtonsHover.getText();
        ParamS.param.paramPortal.farbeSchriftUntenButtonsHover = tfFarbeSchriftUntenButtonsHover.getText();
        ParamS.param.paramPortal.farbeRahmenUntenButtonsHover = tfFarbeRahmenUntenButtonsHover.getText();
        ParamS.param.paramPortal.breiteRahmenUntenButtonsHover = tfBreiteRahmenUntenButtonsHover.getText();
        ParamS.param.paramPortal.radiusRahmenUntenButtonsHover = tfRadiusRahmenUntenButtonsHover.getText();
        ParamS.param.paramPortal.stilRahmenUntenButtonsHover = tfStilRahmenUntenButtonsHover.getText();

        /* +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++ */

        ParamS.param.paramPortal.farbeCookieHintHintergrund = tfFarbeCookieHintHintergrund.getText();
        ParamS.param.paramPortal.farbeCookieHintSchrift = tfFarbeCookieHintSchrift.getText();
        ParamS.param.paramPortal.farbeCookieHintButton = tfFarbeCookieHintButton.getText();
        ParamS.param.paramPortal.farbeCookieHintButtonSchrift = tfFarbeCookieHintButtonSchrift.getText();

        ParamS.param.paramPortal.farbeLadebalkenUploadLeer = tfFarbeLadebalkenUploadLeer.getText();
        ParamS.param.paramPortal.farbeLadebalkenUploadFull = tfFarbeLadebalkenUploadFull.getText();

        /************************ Phasen *******************************/

        for (int i = 1; i <= 20; i++) {
            ParamS.param.paramPortalServer.phasenNamen[i] = tfPhasenNamen[i].getText();

            ParamS.param.paramPortal.eclPortalPhase[i].manuellAktiv = cbManuellAktiv[i].isSelected();
            ParamS.param.paramPortal.eclPortalPhase[i].manuellDeaktiv = cbManuellDeaktiv[i].isSelected();
            ParamS.param.paramPortal.eclPortalPhase[i].gewinnspielAktiv = cbGewinnspielAktiv[i].isSelected();
            ParamS.param.paramPortal.eclPortalPhase[i].lfdHVPortalInBetrieb = cbLfdHVPortalInBetrieb[i].isSelected();
            ParamS.param.paramPortal.eclPortalPhase[i].lfdVorDerHVNachDerHV = Integer.parseInt(tfLfdVorDerHVNachDerHV[i].getText());
            ParamS.param.paramPortal.eclPortalPhase[i].lfdHVPortalErstanmeldungIstMoeglich = cbLfdHVPortalErstanmeldungIstMoeglich[i].isSelected();
            ParamS.param.paramPortal.eclPortalPhase[i].lfdHVPortalEKIstMoeglich = cbLfdHVPortalEKIstMoeglich[i].isSelected();
            ParamS.param.paramPortal.eclPortalPhase[i].lfdHVPortalSRVIstMoeglich = cbLfdHVPortalSRVIstMoeglich[i].isSelected();
            ParamS.param.paramPortal.eclPortalPhase[i].lfdHVPortalBriefwahlIstMoeglich = cbLfdHVPortalBriefwahlIstMoeglich[i].isSelected();
            ParamS.param.paramPortal.eclPortalPhase[i].lfdHVPortalKIAVIstMoeglich = cbLfdHVPortalKIAVIstMoeglich[i].isSelected();
            ParamS.param.paramPortal.eclPortalPhase[i].lfdHVPortalVollmachtDritteIstMoeglich = cbLfdHVPortalVollmachtDritteIstMoeglich[i].isSelected();

            ParamS.param.paramPortal.eclPortalPhase[i].lfdHVStreamIstMoeglich = cbLfdHVStreamIstMoeglich[i].isSelected();
            ParamS.param.paramPortal.eclPortalPhase[i].lfdHVFragenIstMoeglich = cbLfdHVFragenIstMoeglich[i].isSelected();
            ParamS.param.paramPortal.eclPortalPhase[i].lfdHVRueckfragenIstMoeglich = cbLfdHVRueckfragenIstMoeglich[i].isSelected();
            ParamS.param.paramPortal.eclPortalPhase[i].lfdHVWortmeldungenIstMoeglich = cbLfdHVWortmeldungenenIstMoeglich[i].isSelected();
            ParamS.param.paramPortal.eclPortalPhase[i].lfdHVWiderspruecheIstMoeglich = cbLfdHVWiederspruecheIstMoeglich[i].isSelected();
            ParamS.param.paramPortal.eclPortalPhase[i].lfdHVAntraegeIstMoeglich = cbLfdHVAntraegeIstMoeglich[i].isSelected();
            ParamS.param.paramPortal.eclPortalPhase[i].lfdHVSonstigeMitteilungenIstMoeglich = cbLfdHVSonstigeMitteilungenIstMoeglich[i].isSelected();
            ParamS.param.paramPortal.eclPortalPhase[i].lfdHVBotschaftenEinreichenIstMoeglich = cbLfdHVBotschaftenEinreichenIstMoeglich[i].isSelected();
            ParamS.param.paramPortal.eclPortalPhase[i].lfdHVBotschaftenIstMoeglich = cbLfdHVBotschaftenIstMoeglich[i].isSelected();
            ParamS.param.paramPortal.eclPortalPhase[i].lfdHVChatIstMoeglich = cbLfdHVChatIstMoeglich[i].isSelected();
            ParamS.param.paramPortal.eclPortalPhase[i].lfdHVUnterlagenGruppe1IstMoeglich = cbLfdHVUnterlagen1IstMoeglich[i].isSelected();
            ParamS.param.paramPortal.eclPortalPhase[i].lfdHVUnterlagenGruppe2IstMoeglich = cbLfdHVUnterlagen2IstMoeglich[i].isSelected();
            ParamS.param.paramPortal.eclPortalPhase[i].lfdHVUnterlagenGruppe3IstMoeglich = cbLfdHVUnterlagen3IstMoeglich[i].isSelected();
            ParamS.param.paramPortal.eclPortalPhase[i].lfdHVUnterlagenGruppe4IstMoeglich = cbLfdHVUnterlagen4IstMoeglich[i].isSelected();
            ParamS.param.paramPortal.eclPortalPhase[i].lfdHVUnterlagenGruppe5IstMoeglich = cbLfdHVUnterlagen5IstMoeglich[i].isSelected();
            ParamS.param.paramPortal.eclPortalPhase[i].lfdHVTeilnehmerverzIstMoeglich = cbLfdHVTeilnehmerverzIstMoeglich[i].isSelected();
            ParamS.param.paramPortal.eclPortalPhase[i].lfdHVAbstimmungsergIstMoeglich = cbLfdHVAbstimmungsergIstMoeglich[i].isSelected();

        }

        /************************ Portalfunktionen *******************************/

        for (int i = 1; i <= 40; i++) {

            ParamS.param.paramPortal.eclPortalFunktion[i].wirdAngeboten = cbFunktWirdAngeboten[i].isSelected();
            ParamS.param.paramPortal.eclPortalFunktion[i].aktiv = Integer.parseInt(tfFunktAktiv[i].getText());

            ParamS.param.paramPortal.eclPortalFunktion[i].berechtigtGast1 = cbFunktGast1[i].isSelected();
            ParamS.param.paramPortal.eclPortalFunktion[i].berechtigtGast2 = cbFunktGast2[i].isSelected();
            ParamS.param.paramPortal.eclPortalFunktion[i].berechtigtGast3 = cbFunktGast3[i].isSelected();
            ParamS.param.paramPortal.eclPortalFunktion[i].berechtigtGast4 = cbFunktGast4[i].isSelected();
            ParamS.param.paramPortal.eclPortalFunktion[i].berechtigtGast5 = cbFunktGast5[i].isSelected();
            ParamS.param.paramPortal.eclPortalFunktion[i].berechtigtGast6 = cbFunktGast6[i].isSelected();
            ParamS.param.paramPortal.eclPortalFunktion[i].berechtigtGast7 = cbFunktGast7[i].isSelected();
            ParamS.param.paramPortal.eclPortalFunktion[i].berechtigtGast8 = cbFunktGast8[i].isSelected();
            ParamS.param.paramPortal.eclPortalFunktion[i].berechtigtGast9 = cbFunktGast9[i].isSelected();
            ParamS.param.paramPortal.eclPortalFunktion[i].berechtigtGast10 = cbFunktGast10[i].isSelected();

            ParamS.param.paramPortal.eclPortalFunktion[i].berechtigtGastOnlineTeilnahmer1 = cbFunktGast1OT[i].isSelected();
            ParamS.param.paramPortal.eclPortalFunktion[i].berechtigtGastOnlineTeilnahmer2 = cbFunktGast2OT[i].isSelected();
            ParamS.param.paramPortal.eclPortalFunktion[i].berechtigtGastOnlineTeilnahmer3 = cbFunktGast3OT[i].isSelected();
            ParamS.param.paramPortal.eclPortalFunktion[i].berechtigtGastOnlineTeilnahmer4 = cbFunktGast4OT[i].isSelected();
            ParamS.param.paramPortal.eclPortalFunktion[i].berechtigtGastOnlineTeilnahmer5 = cbFunktGast5OT[i].isSelected();
            ParamS.param.paramPortal.eclPortalFunktion[i].berechtigtGastOnlineTeilnahmer6 = cbFunktGast6OT[i].isSelected();
            ParamS.param.paramPortal.eclPortalFunktion[i].berechtigtGastOnlineTeilnahmer7 = cbFunktGast7OT[i].isSelected();
            ParamS.param.paramPortal.eclPortalFunktion[i].berechtigtGastOnlineTeilnahmer8 = cbFunktGast8OT[i].isSelected();
            ParamS.param.paramPortal.eclPortalFunktion[i].berechtigtGastOnlineTeilnahmer9 = cbFunktGast9OT[i].isSelected();
            ParamS.param.paramPortal.eclPortalFunktion[i].berechtigtGastOnlineTeilnahmer10 = cbFunktGast10OT[i].isSelected();

            ParamS.param.paramPortal.eclPortalFunktion[i].berechtigtAktionaer = cbFunktAkt[i].isSelected();
            ParamS.param.paramPortal.eclPortalFunktion[i].berechtigtAngemeldeterAktionaer = cbFunktAktAng[i].isSelected();
            ParamS.param.paramPortal.eclPortalFunktion[i].berechtigtOnlineTeilnahmeAktionaer = cbFunktAktOT[i].isSelected();

        }

        /************************ PortalUnterlagen *******************************/

        for (int i = 0; i < ParamS.param.paramPortal.eclPortalUnterlagen.size(); i++) {

            ParamS.param.paramPortal.eclPortalUnterlagen.get(i).reihenfolgeLoginOben = CaString.integerParseInt(tfUnterLoginOben.get(i).getText());
            ParamS.param.paramPortal.eclPortalUnterlagen.get(i).reihenfolgeLoginUnten = CaString.integerParseInt(tfUnterLoginUnten.get(i).getText());
            ParamS.param.paramPortal.eclPortalUnterlagen.get(i).reihenfolgeExterneSeite = CaString.integerParseInt(tfUnterExtern.get(i).getText());
            ParamS.param.paramPortal.eclPortalUnterlagen.get(i).reihenfolgeUnterlagen = CaString.integerParseInt(tfUnterUnterlagen.get(i).getText());
            ParamS.param.paramPortal.eclPortalUnterlagen.get(i).reihenfolgeBotschaften = CaString.integerParseInt(tfUnterBotschaften.get(i).getText());

            ParamS.param.paramPortal.eclPortalUnterlagen.get(i).aktiv = CaString.integerParseInt(tfUnterAktiv.get(i).getText());

            ParamS.param.paramPortal.eclPortalUnterlagen.get(i).art = CaString.integerParseInt(tfUnterArt.get(i).getText());
            ParamS.param.paramPortal.eclPortalUnterlagen.get(i).artStyle = CaString.integerParseInt(tfUnterArtStyle.get(i).getText());

            ParamS.param.paramPortal.eclPortalUnterlagen.get(i).previewLogin = CaString.integerParseInt(tfUnterPreviewLogin.get(i).getText());
            ParamS.param.paramPortal.eclPortalUnterlagen.get(i).previewExterneSeite = CaString.integerParseInt(tfUnterPreviewExtern.get(i).getText());
            ParamS.param.paramPortal.eclPortalUnterlagen.get(i).previewIntern = CaString.integerParseInt(tfUnterPreviewIntern.get(i).getText());

            ParamS.param.paramPortal.eclPortalUnterlagen.get(i).dateiname = tfUnterDateiname.get(i).getText();
            ParamS.param.paramPortal.eclPortalUnterlagen.get(i).dateiMehrsprachigVorhanden = CaString.integerParseInt(tfUnterDateiAufEnglisch.get(i).getText());

            ParamS.param.paramPortal.eclPortalUnterlagen.get(i).berechtigtGast1 = cbUnterGast1.get(i).isSelected();
            ParamS.param.paramPortal.eclPortalUnterlagen.get(i).berechtigtGast2 = cbUnterGast2.get(i).isSelected();
            ParamS.param.paramPortal.eclPortalUnterlagen.get(i).berechtigtGast3 = cbUnterGast3.get(i).isSelected();
            ParamS.param.paramPortal.eclPortalUnterlagen.get(i).berechtigtGast4 = cbUnterGast4.get(i).isSelected();
            ParamS.param.paramPortal.eclPortalUnterlagen.get(i).berechtigtGast5 = cbUnterGast5.get(i).isSelected();
            ParamS.param.paramPortal.eclPortalUnterlagen.get(i).berechtigtGast6 = cbUnterGast6.get(i).isSelected();
            ParamS.param.paramPortal.eclPortalUnterlagen.get(i).berechtigtGast7 = cbUnterGast7.get(i).isSelected();
            ParamS.param.paramPortal.eclPortalUnterlagen.get(i).berechtigtGast8 = cbUnterGast8.get(i).isSelected();
            ParamS.param.paramPortal.eclPortalUnterlagen.get(i).berechtigtGast9 = cbUnterGast9.get(i).isSelected();
            ParamS.param.paramPortal.eclPortalUnterlagen.get(i).berechtigtGast10 = cbUnterGast10.get(i).isSelected();

            ParamS.param.paramPortal.eclPortalUnterlagen.get(i).berechtigtGastOnlineTeilnahmer1 = cbUnterGast1OT.get(i).isSelected();
            ParamS.param.paramPortal.eclPortalUnterlagen.get(i).berechtigtGastOnlineTeilnahmer2 = cbUnterGast2OT.get(i).isSelected();
            ParamS.param.paramPortal.eclPortalUnterlagen.get(i).berechtigtGastOnlineTeilnahmer3 = cbUnterGast3OT.get(i).isSelected();
            ParamS.param.paramPortal.eclPortalUnterlagen.get(i).berechtigtGastOnlineTeilnahmer4 = cbUnterGast4OT.get(i).isSelected();
            ParamS.param.paramPortal.eclPortalUnterlagen.get(i).berechtigtGastOnlineTeilnahmer5 = cbUnterGast5OT.get(i).isSelected();
            ParamS.param.paramPortal.eclPortalUnterlagen.get(i).berechtigtGastOnlineTeilnahmer6 = cbUnterGast6OT.get(i).isSelected();
            ParamS.param.paramPortal.eclPortalUnterlagen.get(i).berechtigtGastOnlineTeilnahmer7 = cbUnterGast7OT.get(i).isSelected();
            ParamS.param.paramPortal.eclPortalUnterlagen.get(i).berechtigtGastOnlineTeilnahmer8 = cbUnterGast8OT.get(i).isSelected();
            ParamS.param.paramPortal.eclPortalUnterlagen.get(i).berechtigtGastOnlineTeilnahmer9 = cbUnterGast9OT.get(i).isSelected();
            ParamS.param.paramPortal.eclPortalUnterlagen.get(i).berechtigtGastOnlineTeilnahmer10 = cbUnterGast10OT.get(i).isSelected();

            ParamS.param.paramPortal.eclPortalUnterlagen.get(i).berechtigtGastOnlineTeilnahmer10 = cbUnterGast10OT.get(i).isSelected();
            ParamS.param.paramPortal.eclPortalUnterlagen.get(i).berechtigtGastOnlineTeilnahmer10 = cbUnterGast10OT.get(i).isSelected();
            ParamS.param.paramPortal.eclPortalUnterlagen.get(i).berechtigtGastOnlineTeilnahmer10 = cbUnterGast10OT.get(i).isSelected();

            ParamS.param.paramPortal.eclPortalUnterlagen.get(i).berechtigtAktionaer = cbUnterAkt.get(i).isSelected();
            ParamS.param.paramPortal.eclPortalUnterlagen.get(i).berechtigtAngemeldeterAktionaer = cbUnterAktAng.get(i).isSelected();
            ParamS.param.paramPortal.eclPortalUnterlagen.get(i).berechtigtOnlineTeilnahmeAktionaer = cbUnterAktOT.get(i).isSelected();

            ParamS.param.paramPortal.eclPortalUnterlagen.get(i).bezeichnungDE = lbUnterTextDE.get(i).getText();
            ParamS.param.paramPortal.eclPortalUnterlagen.get(i).bezeichnungEN = lbUnterTextEN.get(i).getText();

        }

        //        /*Nach dem Speichern: neu sortieren, und gelöschte raus.
        //         * D.h.: durcharbeiten, alle ignorieren die -1 haben; und die durchgearbeite sind auf -1 setzen
        //         */
        //        List<EclPortalUnterlagen> lPortalUnterlagenNeu = new LinkedList<EclPortalUnterlagen>();
        //
        //        int gef = 1;
        //        while (gef != 99999) {
        //            gef = 99999;
        //            for (int i = 0; i < ParamS.param.paramPortal.eclPortalUnterlagen.size(); i++) {
        //                EclPortalUnterlagen lPortalUnterlage = ParamS.param.paramPortal.eclPortalUnterlagen.get(i);
        //                if (lPortalUnterlage.reihenfolgeLoginOben != -1 && lPortalUnterlage.reihenfolgeLoginOben < gef) {
        //                    gef = i;
        //                }
        //            }
        //            if (gef != 99999) {
        //                EclPortalUnterlagen lPortalUnterlage = ParamS.param.paramPortal.eclPortalUnterlagen.get(gef);
        //                lPortalUnterlage.reihenfolgeLoginOben = -1;
        //                lPortalUnterlagenNeu.add(lPortalUnterlage);
        //            }
        //        }
        //        for (int i = 0; i < lPortalUnterlagenNeu.size(); i++) {
        //            lPortalUnterlagenNeu.get(i).reihenfolgeLoginOben = i + 1;
        //        }

        /*Gelöschte raus*/
        List<EclPortalUnterlagen> lPortalUnterlagenNeu = new LinkedList<EclPortalUnterlagen>();
        for (int i = 0; i < ParamS.param.paramPortal.eclPortalUnterlagen.size(); i++) {
            EclPortalUnterlagen lPortalUnterlage = ParamS.param.paramPortal.eclPortalUnterlagen.get(i);
            if (lPortalUnterlage.reihenfolgeLoginOben != -1) {
                lPortalUnterlagenNeu.add(lPortalUnterlage);
            }
        }

        ParamS.param.paramPortal.eclPortalUnterlagen = lPortalUnterlagenNeu;

        /*Fragen*/
        ParamS.param.paramPortal.fragenStellerAbfragen = Integer.parseInt(tfFragenStellerAbfragen.getText());
        ParamS.param.paramPortal.fragenNameAbfragen = Integer.parseInt(tfFragenNameAbfragen.getText());
        ParamS.param.paramPortal.fragenKontaktdatenAbfragen = Integer.parseInt(tfFragenKontaktdatenAbfragen.getText());
        ParamS.param.paramPortal.fragenKontaktdatenEMailVorschlagen = Integer.parseInt(tfFragenKontaktdatenEMailVorschlagen.getText());
        ParamS.param.paramPortal.fragenKontaktdatenTelefonAbfragen = Integer.parseInt(tfFragenKontaktdatenTelefonAbfragen.getText());
        ParamS.param.paramPortal.fragenKurztextAbfragen = Integer.parseInt(tfFragenKurztextAbfragen.getText());
        ParamS.param.paramPortal.fragenTopListeAnbieten = Integer.parseInt(tfFragenTopListeAnbieten.getText());
        ParamS.param.paramPortal.fragenLangtextAbfragen = Integer.parseInt(tfFragenLangtextAbfragen.getText());
        ParamS.param.paramPortal.fragenZurueckziehenMoeglich = Integer.parseInt(tfFragenZurueckziehenMoeglich.getText());
        ParamS.param.paramPortal.fragenLaenge = Integer.parseInt(tfFragenLaenge.getText());
        ParamS.param.paramPortal.fragenAnzahlJeAktionaer = Integer.parseInt(tfFragenAnzahlJeAktionaer.getText());
        ParamS.param.paramPortal.fragenStellerZulaessig = Integer.parseInt(tfFragenStellerZulaessig.getText());
        ParamS.param.paramPortal.fragenHinweisGelesen = Integer.parseInt(tfFragenHinweisGelesen.getText());
        ParamS.param.paramPortal.fragenHinweisVorbelegenMit = Integer.parseInt(tfFragenHinweisVorbelegenMit.getText());
        ParamS.param.paramPortal.fragenRueckfragenErmoeglichen = Integer.parseInt(tfFragenRueckfragenErmoeglichen.getText());
        ParamS.param.paramPortal.fragenMailBeiEingang = Integer.parseInt(tfFragenMailBeiEingang.getText());
        ParamS.param.paramPortal.fragenMailVerteiler1 = tfFragenMailVerteiler1.getText();
        ParamS.param.paramPortal.fragenMailVerteiler2 = tfFragenMailVerteiler2.getText();
        ParamS.param.paramPortal.fragenMailVerteiler3 = tfFragenMailVerteiler3.getText();
        ParamS.param.paramPortal.fragenExternerZugriff = Integer.parseInt(tfFragenExternerZugriff.getText());

        /*Wortmeldung*/
        ParamS.param.paramPortal.wortmeldungArt = Integer.parseInt(tfWortmeldungArt.getText());
        ParamS.param.paramPortal.wortmeldungStellerAbfragen = Integer.parseInt(tfWortmeldungStellerAbfragen.getText());
        ParamS.param.paramPortal.wortmeldungNameAbfragen = Integer.parseInt(tfWortmeldungNameAbfragen.getText());
        ParamS.param.paramPortal.wortmeldungKontaktdatenAbfragen = Integer.parseInt(tfWortmeldungKontaktdatenAbfragen.getText());
        ParamS.param.paramPortal.wortmeldungKontaktdatenEMailVorschlagen = Integer.parseInt(tfWortmeldungKontaktdatenEMailVorschlagen.getText());
        ParamS.param.paramPortal.wortmeldungKontaktdatenTelefonAbfragen = Integer.parseInt(tfWortmeldungKontaktdatenTelefonAbfragen.getText());
        ParamS.param.paramPortal.wortmeldungKurztextAbfragen = Integer.parseInt(tfWortmeldungKurztextAbfragen.getText());
        ParamS.param.paramPortal.wortmeldungTopListeAnbieten = Integer.parseInt(tfWortmeldungTopListeAnbieten.getText());
        ParamS.param.paramPortal.wortmeldungLangtextAbfragen = Integer.parseInt(tfWortmeldungLangtextAbfragen.getText());
        ParamS.param.paramPortal.wortmeldungZurueckziehenMoeglich = Integer.parseInt(tfWortmeldungZurueckziehenMoeglich.getText());
        ParamS.param.paramPortal.wortmeldungLaenge = Integer.parseInt(tfWortmeldungLaenge.getText());
        ParamS.param.paramPortal.wortmeldungAnzahlJeAktionaer = Integer.parseInt(tfWortmeldungAnzahlJeAktionaer.getText());
        ParamS.param.paramPortal.wortmeldungStellerZulaessig = Integer.parseInt(tfWortmeldungStellerZulaessig.getText());
        ParamS.param.paramPortal.wortmeldungHinweisGelesen = Integer.parseInt(tfWortmeldungHinweisGelesen.getText());
        ParamS.param.paramPortal.wortmeldungHinweisVorbelegenMit = Integer.parseInt(tfWortmeldungHinweisVorbelegenMit.getText());
        ParamS.param.paramPortal.wortmeldungMailBeiEingang = Integer.parseInt(tfWortmeldungMailBeiEingang.getText());
        ParamS.param.paramPortal.wortmeldungMailVerteiler1 = tfWortmeldungMailVerteiler1.getText();
        ParamS.param.paramPortal.wortmeldungMailVerteiler2 = tfWortmeldungMailVerteiler2.getText();
        ParamS.param.paramPortal.wortmeldungMailVerteiler3 = tfWortmeldungMailVerteiler3.getText();
        ParamS.param.paramPortal.wortmeldungListeAnzeigen = Integer.parseInt(tfWortmeldungListeAnzeigen.getText());
        ParamS.param.paramPortal.wortmeldungVLListeAnzeigen = Integer.parseInt(tfWortmeldungVLListeAnzeigen.getText());

        ParamS.param.paramPortal.wortmeldungTestDurchfuehren = Integer.parseInt(tfWortmeldungTestDurchfuehren.getText());
        ParamS.param.paramPortal.wortmeldungRedeAufrufZweitenVersuchDurchfuehren = Integer.parseInt(tfWortmeldungRedeAufrufZweitenVersuchDurchfuehren.getText());
        ParamS.param.paramPortal.wortmeldungNachTestManuellInRednerlisteAufnehmen = Integer.parseInt(tfWortmeldungNachTestManuellInRednerlisteAufnehmen.getText());
        ParamS.param.paramPortal.wortmeldungInhaltsHinweiseAktiv = Integer.parseInt(tfWortmeldungInhaltsHinweiseAktiv.getText());
        ParamS.param.paramPortal.wortmeldetischSetNr = Integer.parseInt(tfWortmeldetischSetNr.getText());
        ParamS.param.paramPortal.schriftgroesseVersammlunsleiterView = tfSchriftgroesseVersammlunsleiterView.getText();

        /*Widersprueche*/
        ParamS.param.paramPortal.widerspruecheStellerAbfragen = Integer.parseInt(tfWiderspruecheStellerAbfragen.getText());
        ParamS.param.paramPortal.widerspruecheNameAbfragen = Integer.parseInt(tfWiderspruecheNameAbfragen.getText());
        ParamS.param.paramPortal.widerspruecheKontaktdatenAbfragen = Integer.parseInt(tfWiderspruecheKontaktdatenAbfragen.getText());
        ParamS.param.paramPortal.widerspruecheKontaktdatenEMailVorschlagen = Integer.parseInt(tfWiderspruecheKontaktdatenEMailVorschlagen.getText());
        ParamS.param.paramPortal.widerspruecheKontaktdatenTelefonAbfragen = Integer.parseInt(tfWiderspruecheKontaktdatenTelefonAbfragen.getText());
        ParamS.param.paramPortal.widerspruecheKurztextAbfragen = Integer.parseInt(tfWiderspruecheKurztextAbfragen.getText());
        ParamS.param.paramPortal.widerspruecheTopListeAnbieten = Integer.parseInt(tfWiderspruecheTopListeAnbieten.getText());
        ParamS.param.paramPortal.widerspruecheLangtextAbfragen = Integer.parseInt(tfWiderspruecheLangtextAbfragen.getText());
        ParamS.param.paramPortal.widerspruecheZurueckziehenMoeglich = Integer.parseInt(tfWiderspruecheZurueckziehenMoeglich.getText());
        ParamS.param.paramPortal.widerspruecheLaenge = Integer.parseInt(tfWiderspruecheLaenge.getText());
        ParamS.param.paramPortal.widerspruecheAnzahlJeAktionaer = Integer.parseInt(tfWiderspruecheAnzahlJeAktionaer.getText());
        ParamS.param.paramPortal.widerspruecheStellerZulaessig = Integer.parseInt(tfWiderspruecheStellerZulaessig.getText());
        ParamS.param.paramPortal.widerspruecheHinweisGelesen = Integer.parseInt(tfWiderspruecheHinweisGelesen.getText());
        ParamS.param.paramPortal.widerspruecheHinweisVorbelegenMit = Integer.parseInt(tfWiderspruecheHinweisVorbelegenMit.getText());
        ParamS.param.paramPortal.widerspruecheMailBeiEingang = Integer.parseInt(tfWiderspruecheMailBeiEingang.getText());
        ParamS.param.paramPortal.widerspruecheMailVerteiler1 = tfWiderspruecheMailVerteiler1.getText();
        ParamS.param.paramPortal.widerspruecheMailVerteiler2 = tfWiderspruecheMailVerteiler2.getText();
        ParamS.param.paramPortal.widerspruecheMailVerteiler3 = tfWiderspruecheMailVerteiler3.getText();

        /*Antraege*/
        ParamS.param.paramPortal.antraegeStellerAbfragen = Integer.parseInt(tfAntraegeStellerAbfragen.getText());
        ParamS.param.paramPortal.antraegeNameAbfragen = Integer.parseInt(tfAntraegeNameAbfragen.getText());
        ParamS.param.paramPortal.antraegeKontaktdatenAbfragen = Integer.parseInt(tfAntraegeKontaktdatenAbfragen.getText());
        ParamS.param.paramPortal.antraegeKontaktdatenEMailVorschlagen = Integer.parseInt(tfAntraegeKontaktdatenEMailVorschlagen.getText());
        ParamS.param.paramPortal.antraegeKontaktdatenTelefonAbfragen = Integer.parseInt(tfAntraegeKontaktdatenTelefonAbfragen.getText());
        ParamS.param.paramPortal.antraegeKurztextAbfragen = Integer.parseInt(tfAntraegeKurztextAbfragen.getText());
        ParamS.param.paramPortal.antraegeTopListeAnbieten = Integer.parseInt(tfAntraegeTopListeAnbieten.getText());
        ParamS.param.paramPortal.antraegeLangtextAbfragen = Integer.parseInt(tfAntraegeLangtextAbfragen.getText());
        ParamS.param.paramPortal.antraegeZurueckziehenMoeglich = Integer.parseInt(tfAntraegeZurueckziehenMoeglich.getText());
        ParamS.param.paramPortal.antraegeLaenge = Integer.parseInt(tfAntraegeLaenge.getText());
        ParamS.param.paramPortal.antraegeAnzahlJeAktionaer = Integer.parseInt(tfAntraegeAnzahlJeAktionaer.getText());
        ParamS.param.paramPortal.antraegeStellerZulaessig = Integer.parseInt(tfAntraegeStellerZulaessig.getText());
        ParamS.param.paramPortal.antraegeHinweisGelesen = Integer.parseInt(tfAntraegeHinweisGelesen.getText());
        ParamS.param.paramPortal.antraegeHinweisVorbelegenMit = Integer.parseInt(tfAntraegeHinweisVorbelegenMit.getText());
        ParamS.param.paramPortal.antraegeMailBeiEingang = Integer.parseInt(tfAntraegeMailBeiEingang.getText());
        ParamS.param.paramPortal.antraegeMailVerteiler1 = tfAntraegeMailVerteiler1.getText();
        ParamS.param.paramPortal.antraegeMailVerteiler2 = tfAntraegeMailVerteiler2.getText();
        ParamS.param.paramPortal.antraegeMailVerteiler3 = tfAntraegeMailVerteiler3.getText();

        /*SonstMitteilungen*/
        ParamS.param.paramPortal.sonstMitteilungenStellerAbfragen = Integer.parseInt(tfSonstMitteilungenStellerAbfragen.getText());
        ParamS.param.paramPortal.sonstMitteilungenNameAbfragen = Integer.parseInt(tfSonstMitteilungenNameAbfragen.getText());
        ParamS.param.paramPortal.sonstMitteilungenKontaktdatenAbfragen = Integer.parseInt(tfSonstMitteilungenKontaktdatenAbfragen.getText());
        ParamS.param.paramPortal.sonstMitteilungenKontaktdatenEMailVorschlagen = Integer.parseInt(tfSonstMitteilungenKontaktdatenEMailVorschlagen.getText());
        ParamS.param.paramPortal.sonstMitteilungenKontaktdatenTelefonAbfragen = Integer.parseInt(tfSonstMitteilungenKontaktdatenTelefonAbfragen.getText());
        ParamS.param.paramPortal.sonstMitteilungenKurztextAbfragen = Integer.parseInt(tfSonstMitteilungenKurztextAbfragen.getText());
        ParamS.param.paramPortal.sonstMitteilungenTopListeAnbieten = Integer.parseInt(tfSonstMitteilungenTopListeAnbieten.getText());
        ParamS.param.paramPortal.sonstMitteilungenLangtextAbfragen = Integer.parseInt(tfSonstMitteilungenLangtextAbfragen.getText());
        ParamS.param.paramPortal.sonstMitteilungenZurueckziehenMoeglich = Integer.parseInt(tfSonstMitteilungenZurueckziehenMoeglich.getText());
        ParamS.param.paramPortal.sonstMitteilungenLaenge = Integer.parseInt(tfSonstMitteilungenLaenge.getText());
        ParamS.param.paramPortal.sonstMitteilungenAnzahlJeAktionaer = Integer.parseInt(tfSonstMitteilungenAnzahlJeAktionaer.getText());
        ParamS.param.paramPortal.sonstMitteilungenStellerZulaessig = Integer.parseInt(tfSonstMitteilungenStellerZulaessig.getText());
        ParamS.param.paramPortal.sonstMitteilungenHinweisGelesen = Integer.parseInt(tfSonstMitteilungenHinweisGelesen.getText());
        ParamS.param.paramPortal.sonstMitteilungenHinweisVorbelegenMit = Integer.parseInt(tfSonstMitteilungenHinweisVorbelegenMit.getText());
        ParamS.param.paramPortal.sonstMitteilungenMailBeiEingang = Integer.parseInt(tfSonstMitteilungenMailBeiEingang.getText());
        ParamS.param.paramPortal.sonstMitteilungenMailVerteiler1 = tfSonstMitteilungenMailVerteiler1.getText();
        ParamS.param.paramPortal.sonstMitteilungenMailVerteiler2 = tfSonstMitteilungenMailVerteiler2.getText();
        ParamS.param.paramPortal.sonstMitteilungenMailVerteiler3 = tfSonstMitteilungenMailVerteiler3.getText();

        /*Botschaften*/
        ParamS.param.paramPortal.botschaftenStellerAbfragen = Integer.parseInt(tfBotschaftenStellerAbfragen.getText());
        ParamS.param.paramPortal.botschaftenNameAbfragen = Integer.parseInt(tfBotschaftenNameAbfragen.getText());
        ParamS.param.paramPortal.botschaftenKontaktdatenAbfragen = Integer.parseInt(tfBotschaftenKontaktdatenAbfragen.getText());
        ParamS.param.paramPortal.botschaftenKontaktdatenEMailVorschlagen = Integer.parseInt(tfBotschaftenKontaktdatenEMailVorschlagen.getText());
        ParamS.param.paramPortal.botschaftenKontaktdatenTelefonAbfragen = Integer.parseInt(tfBotschaftenKontaktdatenTelefonAbfragen.getText());
        ParamS.param.paramPortal.botschaftenKurztextAbfragen = Integer.parseInt(tfBotschaftenKurztextAbfragen.getText());
        ParamS.param.paramPortal.botschaftenTopListeAnbieten = Integer.parseInt(tfBotschaftenTopListeAnbieten.getText());
        ParamS.param.paramPortal.botschaftenLangtextAbfragen = Integer.parseInt(tfBotschaftenLangtextAbfragen.getText());
        ParamS.param.paramPortal.botschaftenLangtextUndDateiNurAlternativ = Integer.parseInt(tfBotschaftenLangtextUndDateiNurAlternativ.getText());
        ParamS.param.paramPortal.botschaftenZurueckziehenMoeglich = Integer.parseInt(tfBotschaftenZurueckziehenMoeglich.getText());
        ParamS.param.paramPortal.botschaftenAnzahlJeAktionaer = Integer.parseInt(tfBotschaftenAnzahlJeAktionaer.getText());
        ParamS.param.paramPortal.botschaftenStellerZulaessig = Integer.parseInt(tfBotschaftenStellerZulaessig.getText());
        ParamS.param.paramPortal.botschaftenHinweisGelesen = Integer.parseInt(tfBotschaftenHinweisGelesen.getText());
        ParamS.param.paramPortal.botschaftenHinweisVorbelegenMit = Integer.parseInt(tfBotschaftenHinweisVorbelegenMit.getText());
        ParamS.param.paramPortal.botschaftenVoranmeldungErforderlich = Integer.parseInt(tfBotschaftenVoranmeldungErforderlich.getText());
        ParamS.param.paramPortal.botschaftenVideoLaenge = Integer.parseInt(tfBotschaftenVideoLaenge.getText());
        ParamS.param.paramPortal.botschaftenLaenge = Integer.parseInt(tfBotschaftenLaenge.getText());
        ParamS.param.paramPortal.botschaftenTextDateiLaenge = Integer.parseInt(tfBotschaftenTextDateiLaenge.getText());
        ParamS.param.paramPortal.botschaftenMailBeiEingang = Integer.parseInt(tfBotschaftenMailBeiEingang.getText());
        ParamS.param.paramPortal.botschaftenMailVerteiler1 = tfBotschaftenMailVerteiler1.getText();
        ParamS.param.paramPortal.botschaftenMailVerteiler2 = tfBotschaftenMailVerteiler2.getText();
        ParamS.param.paramPortal.botschaftenMailVerteiler3 = tfBotschaftenMailVerteiler3.getText();
        ParamS.param.paramPortal.botschaftenVideo = Integer.parseInt(tfBotschaftenVideo.getText());
        ParamS.param.paramPortal.botschaftenTextDatei = Integer.parseInt(tfBotschaftenTextDatei.getText());

        for (int i = 1; i <= 15; i++) {
            ParamS.param.paramPortal.botschaftenVideoFormate[i] = Integer.parseInt(tfBotschaftenVideoFormate[i].getText());
            ParamS.param.paramPortal.botschaftenTextFormate[i] = Integer.parseInt(tfBotschaftenTextFormate[i].getText());
        }

        /************** Inhalts-Hinweise *************************/
        for (int i = 0; i < 10; i++) {
            ParamS.param.paramPortal.inhaltsHinweiseTextDE[i] = tfInhaltsHinweiseTextDE[i].getText();
            ParamS.param.paramPortal.inhaltsHinweiseTextEN[i] = tfInhaltsHinweiseTextEN[i].getText();

            for (int i1 = 0; i1 < 6; i1++) {
                ParamS.param.paramPortal.inhaltsHinweiseAktiv[i][i1] = cbInhaltsHinweiseAktiv[i][i1].isSelected();
            }
        }

        /*********** Kontakt/Hilfe *******************/
        ParamS.param.paramPortal.kontaktformularAktiv = Integer.parseInt(tfKontaktformularAktiv.getText());
        ParamS.param.paramPortal.kontaktformularBeiEingangMail = Integer.parseInt(tfKontaktformularBeiEingangMail.getText());
        ParamS.param.paramPortal.kontaktformularBeiEingangMailAn = tfKontaktformularBeiEingangMailAn.getText();
        ParamS.param.paramPortal.kontaktformularBeiEingangMailInhaltAufnehmen = Integer.parseInt(tfKontaktformularBeiEingangMailInhaltAufnehmen.getText());
        ParamS.param.paramPortal.kontaktformularBeiEingangAufgabe = Integer.parseInt(tfKontaktformularBeiEingangAufgabe.getText());
        ParamS.param.paramPortal.kontaktformularAnzahlKontaktfelder = Integer.parseInt(tfKontaktformularAnzahlKontaktfelder.getText());
        ParamS.param.paramPortal.kontaktformularTelefonKontaktAbfragen = Integer.parseInt(tfKontaktformularTelefonKontaktAbfragen.getText());
        ParamS.param.paramPortal.kontaktformularMailKontaktAbfragen = Integer.parseInt(tfKontaktformularMailKontaktAbfragen.getText());
        ParamS.param.paramPortal.kontaktformularThemaAnbieten = Integer.parseInt(tfKontaktformularThemaAnbieten.getText());
        ParamS.param.paramPortal.kontaktformularThemenListeGlobalLokal = Integer.parseInt(tfKontaktformularThemenListeGlobalLokal.getText());
        ParamS.param.paramPortal.kontaktSonstigeMoeglichkeitenAnbieten = Integer.parseInt(tfKontaktSonstigeMoeglichkeitenAnbieten.getText());
        ParamS.param.paramPortal.kontaktSonstigeMoeglichkeitenObenOderUnten = Integer.parseInt(tfKontaktSonstigeMoeglichkeitenObenOderUnten.getText());

        /******** Virtuelle HV **************/
        ParamS.param.paramPortal.timeoutAufLang = Integer.parseInt(tfTimeoutAufLang.getText());
        ParamS.param.paramPortal.websocketsMoeglich = Integer.parseInt(tfWebsocketsMoeglich.getText());
        ParamS.param.paramPortal.doppelLoginGesperrt = Integer.parseInt(tfDoppelLoginGesperrt.getText());

        ParamS.param.paramPortal.streamAnbieter = Integer.parseInt(tfStreamMitEinmalKey.getText());
        ParamS.param.paramPortal.streamLink = tfStreamLink.getText();
        ParamS.param.paramPortal.streamID = tfStreamID.getText();

        ParamS.param.paramPortal.streamAnbieter2 = Integer.parseInt(tfStreamMitEinmalKey2.getText());
        ParamS.param.paramPortal.streamLink2 = tfStreamLink2.getText();
        ParamS.param.paramPortal.streamID2 = tfStreamID2.getText();

        ParamS.param.paramPortal.shrinkFormat = tfShrinkFormat.getText();
        ParamS.param.paramPortal.shrinkAufloesung = tfShrinkAufloesung.getText();

        ParamS.param.paramPortal.teilnehmerverzBeginnendBei = Integer.parseInt(tfTeilnehmerverzBeginnendBei.getText());
        ParamS.param.paramPortal.teilnehmerverzZusammenstellung = Integer.parseInt(tfTeilnehmerverzZusammenstellung.getText());
        ParamS.param.paramPortal.teilnehmerverzLetzteNr = Integer.parseInt(tfTeilnehmerverzLetzteNr.getText());

        ParamS.param.paramPortal.zuschaltungHVStreamAutomatischStarten = Integer.parseInt(tfZuschaltungHVStreamAutomatischStarten.getText());
        ParamS.param.paramPortal.zuschaltungHVAutomatischNachLogin = Integer.parseInt(tfZuschaltungHVAutomatischNachLogin.getText());

        /*TODO später: da werden einige parameter noch nicht auf gültigkeit überprüft ...*/
        ParamS.param.paramPortal.abstimmungsergLetzteNr = Integer.parseInt(tfAbstimmungsergLetzteNr.getText());

        /******************************Konferenzsystem**************************************/

        ParamS.param.paramPortal.konfEventId = tfKonfEventId.getText();

        ParamS.param.paramPortal.konfRaumId[0][0] = tfKonfRaumIdT1.getText();
        ParamS.param.paramPortal.konfRaumId[0][1] = tfKonfRaumIdT2.getText();
        ParamS.param.paramPortal.konfRaumId[0][2] = tfKonfRaumIdT3.getText();
        ParamS.param.paramPortal.konfRaumId[0][3] = tfKonfRaumIdT4.getText();
        ParamS.param.paramPortal.konfRaumId[0][4] = tfKonfRaumIdT5.getText();
        ParamS.param.paramPortal.konfRaumId[0][5] = tfKonfRaumIdT6.getText();

        ParamS.param.paramPortal.konfRaumId[1][0] = tfKonfRaumIdR1.getText();
        ParamS.param.paramPortal.konfRaumId[1][1] = tfKonfRaumIdR2.getText();
        ParamS.param.paramPortal.konfRaumId[1][2] = tfKonfRaumIdR3.getText();
        ParamS.param.paramPortal.konfRaumId[1][3] = tfKonfRaumIdR4.getText();
        ParamS.param.paramPortal.konfRaumId[1][4] = tfKonfRaumIdR5.getText();
        ParamS.param.paramPortal.konfRaumId[1][5] = tfKonfRaumIdR6.getText();

        ParamS.param.paramPortal.konfRaumAnzahl[0] = Integer.parseInt(tfKonfRaumAnzahlTest.getText());
        ParamS.param.paramPortal.konfRaumAnzahl[1] = Integer.parseInt(tfKonfRaumAnzahlReden.getText());

        ParamS.param.paramPortal.konfGlobaleTestraeumeNutzen = Integer.parseInt(tfKonfGlobaleTestraeumeNutzen.getText());

        /******************************Konferenzsystem Backup**************************************/
        ParamS.param.paramPortal.konfBackupServer = tfKonfBackupServer.getText();

        ParamS.param.paramPortal.konfRaumBId[0][0] = tfKonfRaumIdBT1.getText();
        ParamS.param.paramPortal.konfRaumBId[0][1] = tfKonfRaumIdBT2.getText();
        ParamS.param.paramPortal.konfRaumBId[0][2] = tfKonfRaumIdBT3.getText();
        ParamS.param.paramPortal.konfRaumBId[0][3] = tfKonfRaumIdBT4.getText();
        ParamS.param.paramPortal.konfRaumBId[0][4] = tfKonfRaumIdBT5.getText();
        ParamS.param.paramPortal.konfRaumBId[0][5] = tfKonfRaumIdBT6.getText();

        ParamS.param.paramPortal.konfRaumBId[1][0] = tfKonfRaumIdBR1.getText();
        ParamS.param.paramPortal.konfRaumBId[1][1] = tfKonfRaumIdBR2.getText();
        ParamS.param.paramPortal.konfRaumBId[1][2] = tfKonfRaumIdBR3.getText();
        ParamS.param.paramPortal.konfRaumBId[1][3] = tfKonfRaumIdBR4.getText();
        ParamS.param.paramPortal.konfRaumBId[1][4] = tfKonfRaumIdBR5.getText();
        ParamS.param.paramPortal.konfRaumBId[1][5] = tfKonfRaumIdBR6.getText();

        ParamS.param.paramPortal.konfRaumBIdPW[0][0] = tfKonfRaumIdBT1PW.getText();
        ParamS.param.paramPortal.konfRaumBIdPW[0][1] = tfKonfRaumIdBT2PW.getText();
        ParamS.param.paramPortal.konfRaumBIdPW[0][2] = tfKonfRaumIdBT3PW.getText();
        ParamS.param.paramPortal.konfRaumBIdPW[0][3] = tfKonfRaumIdBT4PW.getText();
        ParamS.param.paramPortal.konfRaumBIdPW[0][4] = tfKonfRaumIdBT5PW.getText();
        ParamS.param.paramPortal.konfRaumBIdPW[0][5] = tfKonfRaumIdBT6PW.getText();

        ParamS.param.paramPortal.konfRaumBIdPW[1][0] = tfKonfRaumIdBR1PW.getText();
        ParamS.param.paramPortal.konfRaumBIdPW[1][1] = tfKonfRaumIdBR2PW.getText();
        ParamS.param.paramPortal.konfRaumBIdPW[1][2] = tfKonfRaumIdBR3PW.getText();
        ParamS.param.paramPortal.konfRaumBIdPW[1][3] = tfKonfRaumIdBR4PW.getText();
        ParamS.param.paramPortal.konfRaumBIdPW[1][4] = tfKonfRaumIdBR5PW.getText();
        ParamS.param.paramPortal.konfRaumBIdPW[1][5] = tfKonfRaumIdBR6PW.getText();

        if (cbKonfBackupAktiv.isSelected()) {
            ParamS.param.paramPortal.konfBackupAktiv = 1;
        } else {
            ParamS.param.paramPortal.konfBackupAktiv = 0;
        }

        /************* Online-HV ***********************/
        //      	ParamS.param.paramPortal.onlineTeilnahmeAngeboten=Integer.parseInt(tfOnlineTeilnahmeAngeboten.getText());
        //      	ParamS.param.paramPortal.onlineTeilnahmeAktiv=Integer.parseInt(tfOnlineTeilnahmeAktiv.getText());

        ParamS.param.paramPortal.onlineTeilnehmerAbfragen = Integer.parseInt(tfOnlineTeilnehmerAbfragen.getText());
        ParamS.param.paramPortal.onlineTeilnahmeSeparateNutzungsbedingungen = Integer.parseInt(tfOnlineTeilnahmeSeparateNutzungsbedingungen.getText());
        ParamS.param.paramPortal.onlineTeilnahmeAktionaerAlsGast = Integer.parseInt(tfOnlineTeilnahmeAktionaerAlsGast.getText());

        /**************** App ************************************/
        ParamS.eclEmittent.appAktiv = Integer.parseInt(tfAppAktiv.getText());
        ParamS.eclEmittent.appSprache = Integer.parseInt(tfAppSprache.getText());
        ParamS.param.paramPortal.impressumEmittent = Integer.parseInt(tfImpressumEmittent.getText());
        ParamS.param.paramPortal.anzeigeStartseite = Integer.parseInt(tfAnzeigeStartseite.getText());

        DbBundle lDbBundle = new DbBundle();
        StubParameter stubParameter = new StubParameter(false, lDbBundle);
        int erg = stubParameter.updateHVParam_all(ParamS.param);
        if (erg == CaFehler.pfXyWurdeVonAnderemBenutzerVeraendert) {
            CaZeigeHinweis caZeigeHinweis = new CaZeigeHinweis();
            caZeigeHinweis.zeige(eigeneStage, "Achtung, anderer User im System - Ihre Änderungen wurden nicht gespeichert!");
        } else {

            CaBug.druckeLog("Speichern dbEmittenten.update", logDrucken, 10);
            CaBug.druckeLog("ParamS.eclEmittent.portalAktuellAktiv=" + ParamS.eclEmittent.portalAktuellAktiv, logDrucken, 10);

            erg = stubParameter.updateEmittent(ParamS.eclEmittent);
            if (erg != 1) {
                CaZeigeHinweis caZeigeHinweis = new CaZeigeHinweis();
                caZeigeHinweis.zeige(eigeneStage, "Achtung, Emittenten-Parameter nicht gespeichert!");
            }

            //            lDbBundle.openAllOhneParameterCheck();
            //            erg = lDbBundle.dbEmittenten.update(ParamS.eclEmittent);
            //            if (erg != 1) {
            //                CaZeigeHinweis caZeigeHinweis = new CaZeigeHinweis();
            //                caZeigeHinweis.zeige(eigeneStage, "Achtung, Emittenten-Parameter nicht gespeichert!");
            //            }
            //            lDbBundle.closeAll();
        }

        return true;

    }

    /**
     * Bereite felder fuer unterlage auf.
     *
     * @param lPortalUnterlage the l portal unterlage
     * @param i                the i
     */
    private void bereiteFelderFuerUnterlageAuf(EclPortalUnterlagen lPortalUnterlage, int i) {

        Label hIdent = new Label(Integer.toString(lPortalUnterlage.ident));
        gpPortalUnterlagen.add(hIdent, 0, i + 1);

        TextField htfUnterLoginOben = new TextField();
        htfUnterLoginOben.setText(Integer.toString(lPortalUnterlage.reihenfolgeLoginOben));
        tfUnterLoginOben.add(htfUnterLoginOben);
        gpPortalUnterlagen.add(tfUnterLoginOben.get(i), 1, i + 1);

        TextField htfUnterLoginUnten = new TextField();
        htfUnterLoginUnten.setText(Integer.toString(lPortalUnterlage.reihenfolgeLoginUnten));
        tfUnterLoginUnten.add(htfUnterLoginUnten);
        gpPortalUnterlagen.add(tfUnterLoginUnten.get(i), 2, i + 1);

        TextField htfUnterExtern = new TextField();
        htfUnterExtern.setText(Integer.toString(lPortalUnterlage.reihenfolgeExterneSeite));
        tfUnterExtern.add(htfUnterExtern);
        gpPortalUnterlagen.add(tfUnterExtern.get(i), 3, i + 1);

        TextField htfUnterUnterlagen = new TextField();
        htfUnterUnterlagen.setText(Integer.toString(lPortalUnterlage.reihenfolgeUnterlagen));
        tfUnterUnterlagen.add(htfUnterUnterlagen);
        gpPortalUnterlagen.add(tfUnterUnterlagen.get(i), 4, i + 1);

        TextField htfUnterBotschaften = new TextField();
        htfUnterBotschaften.setText(Integer.toString(lPortalUnterlage.reihenfolgeBotschaften));
        tfUnterBotschaften.add(htfUnterBotschaften);
        gpPortalUnterlagen.add(tfUnterBotschaften.get(i), 5, i + 1);

        // CheckBox lcbFunktWirdAngeboten = new CheckBox();
        TextField ltfUnterAktiv = new TextField();
        TextField ltfUnterArt = new TextField();
        TextField ltfUnterArtStyle = new TextField();

        TextField ltfUnterPreviewLogin = new TextField();
        TextField ltfUnterPreviewExtern = new TextField();
        TextField ltfUnterPreviewIntern = new TextField();

        TextArea ltfUnterDateiname = new TextArea();
        TextField ltfUnterDateiAufEnglisch = new TextField();

        CheckBox lcbUnterGast1 = new CheckBox();
        CheckBox lcbUnterGast2 = new CheckBox();
        CheckBox lcbUnterGast3 = new CheckBox();
        CheckBox lcbUnterGast4 = new CheckBox();
        CheckBox lcbUnterGast5 = new CheckBox();
        CheckBox lcbUnterGast6 = new CheckBox();
        CheckBox lcbUnterGast7 = new CheckBox();
        CheckBox lcbUnterGast8 = new CheckBox();
        CheckBox lcbUnterGast9 = new CheckBox();
        CheckBox lcbUnterGast10 = new CheckBox();

        CheckBox lcbUnterGast1OT = new CheckBox();
        CheckBox lcbUnterGast2OT = new CheckBox();
        CheckBox lcbUnterGast3OT = new CheckBox();
        CheckBox lcbUnterGast4OT = new CheckBox();
        CheckBox lcbUnterGast5OT = new CheckBox();
        CheckBox lcbUnterGast6OT = new CheckBox();
        CheckBox lcbUnterGast7OT = new CheckBox();
        CheckBox lcbUnterGast8OT = new CheckBox();
        CheckBox lcbUnterGast9OT = new CheckBox();
        CheckBox lcbUnterGast10OT = new CheckBox();

        CheckBox lcbUnterAkt = new CheckBox();
        CheckBox lcbUnterAktAng = new CheckBox();
        CheckBox lcbUnterAktOT = new CheckBox();

        TextArea ltfUnterTextDE = new TextArea();
        TextArea ltfUnterTextEN = new TextArea();

        ltfUnterAktiv.setText(Integer.toString(lPortalUnterlage.aktiv));

        ltfUnterArt.setText(Integer.toString(lPortalUnterlage.art));
        ltfUnterArtStyle.setText(Integer.toString(lPortalUnterlage.artStyle));

        ltfUnterPreviewLogin.setText(Integer.toString(lPortalUnterlage.previewLogin));
        ltfUnterPreviewExtern.setText(Integer.toString(lPortalUnterlage.previewExterneSeite));
        ltfUnterPreviewIntern.setText(Integer.toString(lPortalUnterlage.previewIntern));

        ltfUnterDateiname.setText(lPortalUnterlage.dateiname);
        ltfUnterDateiAufEnglisch.setText(Integer.toString(lPortalUnterlage.dateiMehrsprachigVorhanden));

        lcbUnterGast1.setSelected(lPortalUnterlage.berechtigtGast1);
        lcbUnterGast2.setSelected(lPortalUnterlage.berechtigtGast2);
        lcbUnterGast3.setSelected(lPortalUnterlage.berechtigtGast3);
        lcbUnterGast4.setSelected(lPortalUnterlage.berechtigtGast4);
        lcbUnterGast5.setSelected(lPortalUnterlage.berechtigtGast5);
        lcbUnterGast6.setSelected(lPortalUnterlage.berechtigtGast6);
        lcbUnterGast7.setSelected(lPortalUnterlage.berechtigtGast7);
        lcbUnterGast8.setSelected(lPortalUnterlage.berechtigtGast8);
        lcbUnterGast9.setSelected(lPortalUnterlage.berechtigtGast9);
        lcbUnterGast10.setSelected(lPortalUnterlage.berechtigtGast10);

        lcbUnterGast1OT.setSelected(lPortalUnterlage.berechtigtGastOnlineTeilnahmer1);
        lcbUnterGast2OT.setSelected(lPortalUnterlage.berechtigtGastOnlineTeilnahmer2);
        lcbUnterGast3OT.setSelected(lPortalUnterlage.berechtigtGastOnlineTeilnahmer3);
        lcbUnterGast4OT.setSelected(lPortalUnterlage.berechtigtGastOnlineTeilnahmer4);
        lcbUnterGast5OT.setSelected(lPortalUnterlage.berechtigtGastOnlineTeilnahmer5);
        lcbUnterGast6OT.setSelected(lPortalUnterlage.berechtigtGastOnlineTeilnahmer6);
        lcbUnterGast7OT.setSelected(lPortalUnterlage.berechtigtGastOnlineTeilnahmer7);
        lcbUnterGast8OT.setSelected(lPortalUnterlage.berechtigtGastOnlineTeilnahmer8);
        lcbUnterGast9OT.setSelected(lPortalUnterlage.berechtigtGastOnlineTeilnahmer9);
        lcbUnterGast10OT.setSelected(lPortalUnterlage.berechtigtGastOnlineTeilnahmer10);

        lcbUnterAkt.setSelected(lPortalUnterlage.berechtigtAktionaer);
        lcbUnterAktAng.setSelected(lPortalUnterlage.berechtigtAngemeldeterAktionaer);
        lcbUnterAktOT.setSelected(lPortalUnterlage.berechtigtOnlineTeilnahmeAktionaer);

        ltfUnterTextDE.setText(lPortalUnterlage.bezeichnungDE);
        ltfUnterTextEN.setText(lPortalUnterlage.bezeichnungEN);

        tfUnterArt.add(ltfUnterArt);
        gpPortalUnterlagen.add(tfUnterArt.get(i), 6, i + 1);

        tfUnterArtStyle.add(ltfUnterArtStyle);
        gpPortalUnterlagen.add(tfUnterArtStyle.get(i), 7, i + 1);

        tfUnterPreviewLogin.add(ltfUnterPreviewLogin);
        gpPortalUnterlagen.add(tfUnterPreviewLogin.get(i), 8, i + 1);
        tfUnterPreviewExtern.add(ltfUnterPreviewExtern);
        gpPortalUnterlagen.add(tfUnterPreviewExtern.get(i), 9, i + 1);
        tfUnterPreviewIntern.add(ltfUnterPreviewIntern);
        gpPortalUnterlagen.add(tfUnterPreviewIntern.get(i), 10, i + 1);

        tfUnterDateiname.add(ltfUnterDateiname);
        gpPortalUnterlagen.add(tfUnterDateiname.get(i), 11, i + 1);
        tfUnterDateiAufEnglisch.add(ltfUnterDateiAufEnglisch);
        gpPortalUnterlagen.add(tfUnterDateiAufEnglisch.get(i), 12, i + 1);

        tfUnterAktiv.add(ltfUnterAktiv);
        gpPortalUnterlagen.add(tfUnterAktiv.get(i), 13, i + 1);
        
        cbUnterAkt.add(lcbUnterAkt);
        gpPortalUnterlagen.add(cbUnterAkt.get(i), 14, i + 1);
        cbUnterAktAng.add(lcbUnterAktAng);
        gpPortalUnterlagen.add(cbUnterAktAng.get(i), 15, i + 1);
        cbUnterAktOT.add(lcbUnterAktOT);
        gpPortalUnterlagen.add(cbUnterAktOT.get(i), 16, i + 1);

        lbUnterTextDE.add(ltfUnterTextDE);
        gpPortalUnterlagen.add(lbUnterTextDE.get(i), 17, i + 1);
        lbUnterTextEN.add(ltfUnterTextEN);
        gpPortalUnterlagen.add(lbUnterTextEN.get(i), 18, i + 1);

        cbUnterGast1.add(lcbUnterGast1);
        gpPortalUnterlagen.add(cbUnterGast1.get(i), 19, i + 1);
        cbUnterGast2.add(lcbUnterGast2);
        gpPortalUnterlagen.add(cbUnterGast2.get(i), 20, i + 1);
        cbUnterGast3.add(lcbUnterGast3);
        gpPortalUnterlagen.add(cbUnterGast3.get(i), 21, i + 1);
        cbUnterGast4.add(lcbUnterGast4);
        gpPortalUnterlagen.add(cbUnterGast4.get(i), 22, i + 1);
        cbUnterGast5.add(lcbUnterGast5);
        gpPortalUnterlagen.add(cbUnterGast5.get(i), 23, i + 1);
        cbUnterGast6.add(lcbUnterGast6);
        gpPortalUnterlagen.add(cbUnterGast6.get(i), 24, i + 1);
        cbUnterGast7.add(lcbUnterGast7);
        gpPortalUnterlagen.add(cbUnterGast7.get(i), 25, i + 1);
        cbUnterGast8.add(lcbUnterGast8);
        gpPortalUnterlagen.add(cbUnterGast8.get(i), 26, i + 1);
        cbUnterGast9.add(lcbUnterGast9);
        gpPortalUnterlagen.add(cbUnterGast9.get(i), 27, i + 1);
        cbUnterGast10.add(lcbUnterGast10);
        gpPortalUnterlagen.add(cbUnterGast10.get(i), 28, i + 1);

        cbUnterGast1OT.add(lcbUnterGast1OT);
        gpPortalUnterlagen.add(cbUnterGast1OT.get(i), 29, i + 1);
        cbUnterGast2OT.add(lcbUnterGast2OT);
        gpPortalUnterlagen.add(cbUnterGast2OT.get(i), 30, i + 1);
        cbUnterGast3OT.add(lcbUnterGast3OT);
        gpPortalUnterlagen.add(cbUnterGast3OT.get(i), 31, i + 1);
        cbUnterGast4OT.add(lcbUnterGast4OT);
        gpPortalUnterlagen.add(cbUnterGast4OT.get(i), 32, i + 1);
        cbUnterGast5OT.add(lcbUnterGast5OT);
        gpPortalUnterlagen.add(cbUnterGast5OT.get(i), 33, i + 1);
        cbUnterGast6OT.add(lcbUnterGast6OT);
        gpPortalUnterlagen.add(cbUnterGast6OT.get(i), 34, i + 1);
        cbUnterGast7OT.add(lcbUnterGast7OT);
        gpPortalUnterlagen.add(cbUnterGast7OT.get(i), 35, i + 1);
        cbUnterGast8OT.add(lcbUnterGast8OT);
        gpPortalUnterlagen.add(cbUnterGast8OT.get(i), 36, i + 1);
        cbUnterGast9OT.add(lcbUnterGast9OT);
        gpPortalUnterlagen.add(cbUnterGast9OT.get(i), 37, i + 1);
        cbUnterGast10OT.add(lcbUnterGast10OT);
        gpPortalUnterlagen.add(cbUnterGast10OT.get(i), 38, i + 1);
        
        hIdent = new Label(Integer.toString(lPortalUnterlage.ident));
        gpPortalUnterlagen.add(hIdent, 39, i + 1);
        
        for(Node node : gpPortalUnterlagen.getChildren()) {
            if(node instanceof TextInputControl) {
                GridPane.setMargin(node, new Insets(2));
                if(node instanceof TextArea) {
                    ((TextArea) node).setMaxHeight(45);
                    ((TextArea) node).setWrapText(true);
                }
            }
        }
    }

    /**
     * Bereite neuen button auf.
     */
    public void bereiteNeuenButtonAuf() {
        btnUnterlagenNeu = new Button();
        btnUnterlagenNeu.setText("Neu");
        btnUnterlagenNeu.setOnAction(e -> {
            clickedNeueUnterlage(e);
        });

        gpPortalUnterlagen.add(btnUnterlagenNeu, 1, ParamS.param.paramPortal.eclPortalUnterlagen.size() + 1);

    }

    /**************************Anzeigefunktionen***************************************/

    public void init() {
        /** HV-Parameter sicherheitshalber neu holen */
        DbBundle lDbBundle = new DbBundle();
        StubParameter stubParameter = new StubParameter(false, lDbBundle);
        stubParameter.leseHVParam_all(lDbBundle.clGlobalVar.mandant, lDbBundle.clGlobalVar.hvJahr, lDbBundle.clGlobalVar.hvNummer, lDbBundle.clGlobalVar.datenbereich);
        ParamS.param = stubParameter.rcHVParam;

        stubParameter.leseEmittent(lDbBundle.clGlobalVar.mandant, lDbBundle.clGlobalVar.hvJahr, lDbBundle.clGlobalVar.hvNummer, lDbBundle.clGlobalVar.datenbereich);
        ParamS.eclEmittent = stubParameter.rcEmittent;

        //        lDbBundle.openAllOhneParameterCheck();
        //        lDbBundle.dbEmittenten.readEmittentHV(lDbBundle.clGlobalVar.mandant, lDbBundle.clGlobalVar.hvJahr, lDbBundle.clGlobalVar.hvNummer, lDbBundle.clGlobalVar.datenbereich);
        //        ParamS.eclEmittent = lDbBundle.dbEmittenten.ergebnisPosition(0);
        //        lDbBundle.closeAll();

    }

    /*****************Event-Funktionen*************************/

    void clickedNeueUnterlage(ActionEvent event) {
        gpPortalUnterlagen.getChildren().remove(btnUnterlagenNeu);
        EclPortalUnterlagen lPortalUnterlage = new EclPortalUnterlagen();
        lPortalUnterlage.reihenfolgeLoginOben = ParamS.param.paramPortal.eclPortalUnterlagen.size() + 1;
        ParamS.param.paramPortal.eclPortalUnterlagen.add(lPortalUnterlage);
        bereiteFelderFuerUnterlageAuf(lPortalUnterlage, ParamS.param.paramPortal.eclPortalUnterlagen.size() - 1);
        bereiteNeuenButtonAuf();
    }

    /**
     * Clicked speichern.
     *
     * @param event the event
     */
    @FXML
    void clickedSpeichern(ActionEvent event) {

        boolean rc = speichernParameter();
        if (rc == true) {
            eigeneStage.hide();
        }

    }

    /**
     * Clicked beenden ohne speichern.
     *
     * @param event the event
     */
    @FXML
    void clickedBeendenOhneSpeichern(ActionEvent event) {
        CaZeigeHinweis caZeigeHinweis = new CaZeigeHinweis();
        caZeigeHinweis.zeige(eigeneStage, "Achtung, Änderungen werden nicht gespeichert!");

        eigeneStage.hide();

    }

    /**
     * On tp changed.
     *
     * @param event the event
     */
    @FXML
    void onTpChanged(MouseEvent event) {

    }

    /*********************** Konferenz oder Stream erstellen 
     * @throws ParseException *************************/
    @FXML
    void clickedKonfErstelleEvent(ActionEvent event) throws ParseException {
        // add your Convernce Calls here
    }

    /**
     * Clicked konf loesche event.
     *
     * @param event the event
     * @throws ParseException the parse exception
     */
    @FXML
    void clickedKonfLoescheEvent(ActionEvent event) throws ParseException {
        // add your Convernce Calls here
    }

    /**
     * Clicked konf erstelle session.
     *
     * @param event the event
     */
    @FXML
    void clickedKonfErstelleSession(ActionEvent event) {
        // add your Convernce Calls here
    }

    /**
     * Clicked konf loesche session.
     *
     * @param event the event
     * @throws ParseException the parse exception
     * @throws IOException    Signals that an I/O exception has occurred.
     */
    @FXML
    void clickedKonfLoescheSession(ActionEvent event) throws ParseException, IOException {
        // add your Convernce Calls here
    }


    @FXML
    void clickedBtnKonferenzOnly() {
        // add your Convernce Calls here
    }

}
