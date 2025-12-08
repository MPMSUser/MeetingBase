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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.ResourceBundle;
import java.util.Set;

import org.kordamp.ikonli.fontawesome5.FontAwesomeSolid;
import org.kordamp.ikonli.javafx.FontIcon;

import de.meetingapps.meetingclient.meetingClientDialoge.CaZeigeHinweis;
import de.meetingapps.meetingclient.meetingClientDialoge.CtrlRoot;
import de.meetingapps.meetingclient.meetingClientOberflaechen.CbAllgemein;
import de.meetingapps.meetingclient.meetingClientOberflaechen.CbElement;
import de.meetingapps.meetingportal.meetComAllg.CaBug;
import de.meetingapps.meetingportal.meetComAllg.CaDateiVerwaltung;
import de.meetingapps.meetingportal.meetComAllg.CaDateiWrite;
import de.meetingapps.meetingportal.meetComAllg.CaDatumZeit;
import de.meetingapps.meetingportal.meetComAllg.CaFehler;
import de.meetingapps.meetingportal.meetComAllg.CaString;
import de.meetingapps.meetingportal.meetComBVerwaltung.BvReload;
import de.meetingapps.meetingportal.meetComBl.BlAbstimmung;
import de.meetingapps.meetingportal.meetComBl.BlDelayedAufloesen;
import de.meetingapps.meetingportal.meetComBl.BlNummernformen;
import de.meetingapps.meetingportal.meetComDb.DbBundle;
import de.meetingapps.meetingportal.meetComEntities.EclAbstimmungMeldung;
import de.meetingapps.meetingportal.meetComEntities.EclAbstimmungMeldungRaw;
import de.meetingapps.meetingportal.meetComEntities.EclAbstimmungsblock;
import de.meetingapps.meetingportal.meetComEntities.EclAbstimmungsmonitorEK;
import de.meetingapps.meetingportal.meetComEntities.EclKonfigAuswertung;
import de.meetingapps.meetingportal.meetComEntities.EclMeldung;
import de.meetingapps.meetingportal.meetComEntities.EclParameter;
import de.meetingapps.meetingportal.meetComEntities.EclScan;
import de.meetingapps.meetingportal.meetComEntities.EclStimmkarten;
import de.meetingapps.meetingportal.meetComEntities.EclTablet;
import de.meetingapps.meetingportal.meetComEntities.EclZutrittsIdent;
import de.meetingapps.meetingportal.meetComEntities.EclZutrittskarten;
import de.meetingapps.meetingportal.meetComHVParam.ParamS;
import de.meetingapps.meetingportal.meetComHVParam.ParamSpezial;
import de.meetingapps.meetingportal.meetComKonst.KonstAbstimmungsAblauf;
import de.meetingapps.meetingportal.meetComKonst.KonstKartenklasse;
import de.meetingapps.meetingportal.meetComKonst.KonstKonfigAuswertungArt;
import de.meetingapps.meetingportal.meetComKonst.KonstKonfigAuswertungAusgabeWeg;
import de.meetingapps.meetingportal.meetComKonst.KonstKonfigAuswertungFunktion;
import de.meetingapps.meetingportal.meetComKonst.KonstStimmart;
import de.meetingapps.meetingportal.meetComKonst.KonstWillenserklaerungWeg;
import de.meetingapps.meetingportal.meetingCoreReport.RpDrucken;
import de.meetingapps.meetingportal.meetingCoreReport.RpVariablen;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**Kürzel, die in den Methoden-Namen verwendet werden:
 * AUSW = Auswerten
 * DRUEX = Drucken Exportieren
 */

/**
 * The Class CtrlAbstimmungAblauf.
 */
public class CtrlAbstimmungAblauf extends CtrlRoot {

    /** The log drucken. */
    private int logDrucken = 10;

    /** The resources. */
    @FXML
    private ResourceBundle resources;

    /** The location. */
    @FXML
    private URL location;

    /** The btn beenden. */
    @FXML
    private Button btnBeenden;

    /** The gp aktive abstimmung. */
    @FXML
    private ScrollPane gpAktiveAbstimmung;

    /** The tf aktive elektronische abstimmung. */
    @FXML
    private TextField tfAktiveLokaleAbstimmung;

    /** The tf aktive elektronische abstimmung. */
    @FXML
    private TextField tfAktiveElektronischeAbstimmung;

    /** The tf zu verarbeitende abstimmung. */
    @FXML
    private TextField tfZuVerarbeitendeAbstimmung;

    /** The pn ablauf. */
    @FXML
    private Pane pnAblauf;

    /** The gp ablauf. */
    @FXML
    private ScrollPane gpAblauf;

    /** The pn funktion AB aktiv deaktiv. */
    @FXML
    private Pane pnFunktionABAktivDeaktiv;

    /** The sp funktion AB aktiv deaktiv. */
    @FXML
    private ScrollPane spFunktionABAktivDeaktiv;

    /** The btn funkion AB aktiv deaktiv fertig. */
    @FXML
    private Button btnFunkionABAktivDeaktivFertig;

    /** The pn funktion stimmen ohne markierung manuell. */
    @FXML
    private Pane pnFunktionStimmenOhneMarkierungManuell;

    /** The cb funktion SOMM auswahl stimmart. */
    @FXML
    private ComboBox<String> cbFunktionSOMMAuswahlStimmart;

    /** The tf funktion SOMM eingabe nummer. */
    @FXML
    private TextField tfFunktionSOMMEingabeNummer;

    /** The tf funktion SOMM stimmzettel. */
    @FXML
    private TextField tfFunktionSOMMStimmzettel;

    /** The tf funktion SOMM stimmart. */
    @FXML
    private TextField tfFunktionSOMMStimmart;

    /** The btn funktion SOMM buchen. */
    @FXML
    private Button btnFunktionSOMMBuchen;

    /** The btn funktion SOMM felder leeren. */
    @FXML
    private Button btnFunktionSOMMFelderLeeren;

    /** The tf funktion SOMM bestaetigung. */
    @FXML
    private TextField tfFunktionSOMMBestaetigung;

    /** The tf funktion SOMM anzahl gel karten. */
    @FXML
    private TextField tfFunktionSOMMAnzahlGelKarten;

    /** The btn funktion SOMM zaehler zuruecksetzen. */
    @FXML
    private Button btnFunktionSOMMZaehlerZuruecksetzen;

    /** The sp funktion SOMM. */
    @FXML
    private ScrollPane spFunktionSOMM;

    /** The btn funktion SOMM letzten stornieren. */
    @FXML
    private Button btnFunktionSOMMLetztenStornieren;

    /** The btn funktion SOMM fertig. */
    @FXML
    private Button btnFunktionSOMMFertig;

    /** The pn MBEM. */
    @FXML
    private Pane pnMBEM;

    /** The lbl MBEM ueberschrift. */
    @FXML
    private Label lblMBEMUeberschrift;

    /** The tf MBEM eingabe nummer. */
    @FXML
    private TextField tfMBEMEingabeNummer;

    /** The tf MBEM stimmzettel. */
    @FXML
    private TextField tfMBEMStimmzettel;

    /** The tf MBEM stapel stimmart. */
    @FXML
    private TextField tfMBEMStapelStimmart;

    /** The btn MBEM buchen. */
    @FXML
    private Button btnMBEMBuchen;

    /** The btn MBEM felder leeren. */
    @FXML
    private Button btnMBEMFelderLeeren;

    /** The tf MBEM bestaetigung. */
    @FXML
    private TextField tfMBEMBestaetigung;

    /** The sp MBEM eingabe markierungen. */
    @FXML
    private ScrollPane spMBEMEingabeMarkierungen;

    /** The tf MBEM anzahl gel karten. */
    @FXML
    private TextField tfMBEMAnzahlGelKarten;

    /** The btn MBEM zaehler zuruecksetzen. */
    @FXML
    private Button btnMBEMZaehlerZuruecksetzen;

    /** The sp MBEM protokoll. */
    @FXML
    private ScrollPane spMBEMProtokoll;

    /** The btn MBEM letzten stornieren. */
    @FXML
    private Button btnMBEMLetztenStornieren;

    /** The btn MBEM fertig. */
    @FXML
    private Button btnMBEMFertig;

    /** The btn MEBM abbruch. */
    @FXML
    private Button btnMEBMAbbruch;

    /** The btn MBEM lesen. */
    @FXML
    private Button btnMBEMLesen;

    /** The pn funktion DAN. */
    @FXML
    private Pane pnFunktionDAN;

    /** The cb DAN anzeige. */
    @FXML
    private ComboBox<String> cbDANAnzeige;

    /** The btn DAN refresh. */
    @FXML
    private Button btnDANRefresh;

    /** The btn DAN fehler erneut verarbeiten. */
    @FXML
    private Button btnDANFehlerErneutVerarbeiten;

    /** The sp DAN protokoll. */
    @FXML
    private ScrollPane spDANProtokoll;

    /** The btn DAN fertig. */
    @FXML
    private Button btnDANFertig;

    /** The tf DAN nummer zur anaylse. */
    @FXML
    private TextField tfDANNummerZurAnaylse;

    /** The tab DAN aktionaersdaten. */
    @FXML
    private Tab tabDANAktionaersdaten;

    /** The tf DAN stimmkarten. */
    @FXML
    private ScrollPane tfDANStimmkarten;

    /** The scpn DAN eintrittskarten. */
    @FXML
    private ScrollPane scpnDANEintrittskarten;

    /** The tf DAN interne ident. */
    @FXML
    private TextField tfDANInterneIdent;

    /** The tf DAN sammelkartenstatus. */
    @FXML
    private TextField tfDANSammelkartenstatus;

    /** The tf DAN name. */
    @FXML
    private TextField tfDANName;

    /** The tf DAN vorname. */
    @FXML
    private TextField tfDANVorname;

    /** The tf DAN ort. */
    @FXML
    private TextField tfDANOrt;

    /** The tf DAN vertreter name. */
    @FXML
    private TextField tfDANVertreterName;

    /** The tf DAN vertreter vorname. */
    @FXML
    private TextField tfDANVertreterVorname;

    /** The tf DAN vertreter ort. */
    @FXML
    private TextField tfDANVertreterOrt;

    /** The tf DAN stimmen. */
    @FXML
    private TextField tfDANStimmen;

    /** The tf DAN anwesend delayed. */
    @FXML
    private TextField tfDANAnwesendDelayed;

    /** The tf DAN anwesend aktuell. */
    @FXML
    private TextField tfDANAnwesendAktuell;

    /** The tf DAN gattung. */
    @FXML
    private TextField tfDANGattung;

    /** The tab DAN uebersicht stimmabgaben. */
    @FXML
    private Tab tabDANUebersichtStimmabgaben;

    /** The scrpn DAN uebersicht stimmabgaben. */
    @FXML
    private ScrollPane scrpnDANUebersichtStimmabgaben;

    /** The tab DAN detail stimmabgabe. */
    @FXML
    private Tab tabDANDetailStimmabgabe;

    /** The scpn DAN detail stimmabgabe. */
    @FXML
    private ScrollPane scpnDANDetailStimmabgabe;

    /** The btn DAN vor. */
    @FXML
    private Button btnDANVor;

    /** The btn DAN zurueck. */
    @FXML
    private Button btnDANZurueck;

    /** The btn DAN storno. */
    @FXML
    private Button btnDANStorno;

    /** The btn DAN aktuell pruefen. */
    @FXML
    private Button btnDANAktuellPruefen;

    /** The tf DAN detail ident. */
    @FXML
    private TextField tfDANDetailIdent;

    /** The tf DAN detail verarbeitet. */
    @FXML
    private TextField tfDANDetailVerarbeitet;

    /** The tf DAN detail gelesene nr. */
    @FXML
    private TextField tfDANDetailGeleseneNr;

    /** The tf DAN detail wurde storniert. */
    @FXML
    private TextField tfDANDetailWurdeStorniert;

    /** The tf DAN detail erteilt auf weg. */
    @FXML
    private TextField tfDANDetailErteiltAufWeg;

    /** The tf DAN detail zeit gespeichert. */
    @FXML
    private TextField tfDANDetailZeitGespeichert;

    /** The tf DAN detail zeit abgegeben. */
    @FXML
    private TextField tfDANDetailZeitAbgegeben;

    /** The tab DAN aktuelle stimmwertung. */
    @FXML
    private Tab tabDANAktuelleStimmwertung;

    /** The scpn aktuelle stimmwertung. */
    @FXML
    private ScrollPane scpnAktuelleStimmwertung;

    /** The btn DAN clear. */
    @FXML
    private Button btnDANClear;

    /** The pn MON. */
    @FXML
    private Pane pnMON;

    /** The lbl MON ueberschrift. */
    @FXML
    private Label lblMONUeberschrift;

    /** The btn MON fertig. */
    @FXML
    private Button btnMONFertig;

    /** The gp MON tablet. */
    @FXML
    private GridPane gpMONTablet;

    /** The lbl MON ueberschrift tablet. */
    @FXML
    private Label lblMONUeberschriftTablet;

    /** The btn MON refresh tablets. */
    @FXML
    private Button btnMONRefreshTablets;

    /** The pn TEDAT. */
    @FXML
    private Pane pnTEDAT;

    /** The lbl TEDAT ueberschrift. */
    @FXML
    private Label lblTEDATUeberschrift;

    /** The btn TEDAT fertig. */
    @FXML
    private Button btnTEDATFertig;

    /** The gp TEDAT protokoll. */
    @FXML
    private GridPane gpTEDATProtokoll;

    /** The btn TEDAT datei auswaehlen. */
    @FXML
    private Button btnTEDATDateiAuswaehlen;

    /** The tf TEDAT einzulesende datei. */
    @FXML
    private TextField tfTEDATEinzulesendeDatei;

    /** The btn TEDAT scanner lesen. */
    @FXML
    private Button btnTEDATScannerLesen;

    /** The btn TEDAT tablet datei. */
    @FXML
    private Button btnTEDATTabletDatei;

    /** The btn TEDAT scanner lesen DB erst. */
    @FXML
    private Button btnTEDATScannerLesenDBErst;

    /** The btn TEDAT scanner lesen DB wieder. */
    @FXML
    private Button btnTEDATScannerLesenDBWieder;

    /** The pn AUSW. */
    @FXML
    private Pane pnAUSW;

    /** The lbl AUSW ueberschrift. */
    @FXML
    private Label lblAUSWUeberschrift;

    /** The btn AUSW fertig. */
    @FXML
    private Button btnAUSWFertig;

    /** The gp AUSW protokoll. */
    @FXML
    private GridPane gpAUSWProtokoll;

    /** The btn AUSW auswerten. */
    @FXML
    private Button btnAUSWAuswerten;

    /** The btn AUSW archivieren. */
    @FXML
    private Button btnAUSWArchivieren;

    /** DRUEX - Drucken Export. */
    @FXML
    private Pane pnDRUEX;

    /** The lbl DRUEX ueberschrift. */
    @FXML
    private Label lblDRUEXUeberschrift;

    /** The btn DRUEX fertig. */
    @FXML
    private Button btnDRUEXFertig;

    /** The gp DRUEX protokoll. */
    @FXML
    private GridPane gpDRUEXProtokoll;

    /** The btn DRUEX langfassung. */
    @FXML
    private Button btnDRUEXLangfassung;

    /** The btn DRUEX kurzfassung. */
    @FXML
    private Button btnDRUEXKurzfassung;

    /** The btn DRUEX liste. */
    @FXML
    private Button btnDRUEXListe;

    /** The btn DRUEX praesentation PDF. */
    @FXML
    private Button btnDRUEXPraesentationPDF;

    /** The tf DRUEX langfassung. */
    @FXML
    private TextField tfDRUEXLangfassung;

    /** The tf DRUEX kurzfassung. */
    @FXML
    private TextField tfDRUEXKurzfassung;

    /** The tf DRUEX liste. */
    @FXML
    private TextField tfDRUEXListe;

    /** The btn DRUEX praesentation PP. */
    @FXML
    private Button btnDRUEXPraesentationPP;

    /** The btn DRUEX texte buehne. */
    @FXML
    private Button btnDRUEXTexteBuehne;

    /** The btn DRUEX export. */
    @FXML
    private Button btnDRUEXExport;

    /** The btn DRUEX export fuer PP. */
    @FXML
    private Button btnDRUEXExportFuerPP;

    /** The tf DRUEX praesentation PDF nr. */
    @FXML
    private TextField tfDRUEXPraesentationPDFNr;

    /** The tf DRUEX praesentation PDF datei. */
    @FXML
    private TextField tfDRUEXPraesentationPDFDatei;

    /** The tf DRUEX praesentation PDF pfad. */
    @FXML
    private TextField tfDRUEXPraesentationPDFPfad;

    /** The tf DRUEX praesentation PP nr. */
    @FXML
    private TextField tfDRUEXPraesentationPPNr;

    /** The tf DRUEX praesentation PP datei. */
    @FXML
    private TextField tfDRUEXPraesentationPPDatei;

    /** The tf DRUEX praesentation PP pfad. */
    @FXML
    private TextField tfDRUEXPraesentationPPPfad;

    /** The tf DRUEX texte buehne nr. */
    @FXML
    private TextField tfDRUEXTexteBuehneNr;

    /** The tf DRUEX texte buehne datei. */
    @FXML
    private TextField tfDRUEXTexteBuehneDatei;

    /** The tf DRUEX texte buehne pfad. */
    @FXML
    private TextField tfDRUEXTexteBuehnePfad;

    /** The tf DRUEX export nr. */
    @FXML
    private TextField tfDRUEXExportNr;

    /** The tf DRUEX export datei. */
    @FXML
    private TextField tfDRUEXExportDatei;

    /** The tf DRUEX export pfad. */
    @FXML
    private TextField tfDRUEXExportPfad;

    /** The cb DRUEX job. */
    @FXML
    private ComboBox<CbElement> cbDRUEXJob;

    /** The btn DRUEX praesentation PP liste. */
    @FXML
    private Button btnDRUEXPraesentationPPListe;

    /** The tf DRUEX praesentation PP nr liste. */
    @FXML
    private TextField tfDRUEXPraesentationPPNrListe;

    /** The tf DRUEX praesentation PP datei liste. */
    @FXML
    private TextField tfDRUEXPraesentationPPDateiListe;

    /** The tf DRUEX praesentation PP pfad liste. */
    @FXML
    private TextField tfDRUEXPraesentationPPPfadListe;
    
    /** The tf DRUEX edit. */
    @FXML
    private Button btnDRUEXEdit;
    
    /** The tf DRUEX export ordner. */
    @FXML
    private Button btnDRUEXExportOrdner;

    /*DELB - Delay Abstimmungsbeginn*/

    /** The pn DELB. */
    @FXML
    private Pane pnDELB;

    /** The lbl DELB ueberschrift. */
    @FXML
    private Label lblDELBUeberschrift;

    /** The btn DELB starten. */
    @FXML
    private Button btnDELBStarten;

    /*DELE - Delay Abstimmungsende*/

    /** The pn DELE. */
    @FXML
    private Pane pnDELE;

    /** The lbl DELE ueberschrift. */
    @FXML
    private Label lblDELEUeberschrift;

    /** The btn DELE starten. */
    @FXML
    private Button btnDELEStarten;

    /** The pn DELL. */
    /*DELL Delay auflösen*/
    @FXML
    private Pane pnDELL;

    /** The lbl DELL ueberschrift. */
    @FXML
    private Label lblDELLUeberschrift;

    /** The btn DELL starten. */
    @FXML
    private Button btnDELLStarten;

    /** The pn DELA. */
    /*DELA Delay aus*/
    @FXML
    private Pane pnDELA;

    /** The lbl DELA ueberschrift. */
    @FXML
    private Label lblDELAUeberschrift;

    /** The btn DELA starten. */
    @FXML
    private Button btnDELAStarten;

    /*MONEK Monitor Eintrittskarten*/

    /** The scpn MONEK. */
    @FXML
    private ScrollPane scpnMONEK;

    /** The pn MONEK. */
    @FXML
    private Pane pnMONEK;

    /** The lbl MONEK ueberschrift. */
    @FXML
    private Label lblMONEKUeberschrift;

    /** The btn MONEK fertig. */
    @FXML
    private Button btnMONEKFertig;

    /** The gp MONEK abstimmungsverhalten. */
    @FXML
    private GridPane gpMONEKAbstimmungsverhalten;

    /** The lbl MONEK ueberschrift tablet. */
    @FXML
    private Label lblMONEKUeberschriftTablet;

    /** The btn MONEK refresh alle stimmabgaben. */
    @FXML
    private Button btnMONEKRefreshAlleStimmabgaben;

    /** The btn MONEK refresh abstimmungsverhalten. */
    @FXML
    private Button btnMONEKRefreshAbstimmungsverhalten;

    /** The btn MONEK refresh aktionaersdaten. */
    @FXML
    private Button btnMONEKRefreshAktionaersdaten;

    /*Btn globales Beenden*/

    /**
     * On btn beenden.
     *
     * @param event the event
     */
    @FXML
    void onBtnBeenden(ActionEvent event) {
        eigeneStage.hide();
    }

    /** The grpn anzuzeigende abstimmungen. */
    /*Überschriftsbereich*/
    private GridPane grpnAnzuzeigendeAbstimmungen = null;

    /** The grpn ablauf. */
    /*Ablauf*/
    private GridPane grpnAblauf = null;

    /** The btn ablauf. */
    private Button btnAblauf[] = null;

    /** The grpn funktion AB aktiv deaktiv. */
    /*abstimmungsblockAktivierenDeaktivieren*/
    private GridPane grpnFunktionABAktivDeaktiv = null;

    /** The btn funktion AB aktiv deaktiv deaktivieren. */
    private Button[] btnFunktionABAktivDeaktivDeaktivieren = null;

    /** The btn funktion AB aktiv deaktiv nur anzeigen. */
    private Button[] btnFunktionABAktivDeaktivNurAnzeigen = null;

    /** The btn funktion AB aktiv deaktiv elektronische abstimmung. */
    private Button[] btnFunktionABAktivDeaktivElektronischeAbstimmung = null;

    /** The btn funktion AB aktiv deaktiv verarbeitung abstimmung. */
    private Button[] btnFunktionABAktivDeaktivVerarbeitungAbstimmung = null;

    /** The btn funktion AB aktiv deaktiv verarbeitung abstimmung. */
    private Button[] btnFunktionABAktivDeaktivLokalAktiv = null;

    /**Interne Ident des Abstimmungsblock, der gerade lokal aktiv ist.
     * -1 = kein lokaler Abstimmungspunkt aktiv.
     */
    private int aktivDeaktivLokalAbstimmungsblock=-1;
    
    /** The grpn stimmkarten manuellprotokoll. */
    /*stimmenOhneMarkierungenEinlesenManuell*/
    private GridPane grpnStimmkartenManuellprotokoll = null;

    /*stimmenMarkierungsBelegeEinlesenManuell bzw. stimmenMarkierungenEinlesenManuell*/

    /** The btn alle ja. */
    private Button btnAlleJa = null;

    /** The btn alle nein. */
    private Button btnAlleNein = null;

    /** The btn alle enthaltung. */
    private Button btnAlleEnthaltung = null;

    /** The btn alle ungueltig. */
    private Button btnAlleUngueltig = null;

    /** The btn alle nicht teilnahme. */
    private Button btnAlleNichtTeilnahme = null;

    /** The btn alle nicht markiert. */
    private Button btnAlleNichtMarkiert = null;

    /** The btn alle im sinne. */
    private Button btnAlleImSinne = null;

    /** The btn alle gegen sinne. */
    private Button btnAlleGegenSinne = null;

    /** The grpn MBEM markierungseingabe. */
    private GridPane grpnMBEMMarkierungseingabe = null;

    /** The btn ja. */
    private Button btnJa[] = null;

    /** The btn nein. */
    private Button btnNein[] = null;

    /** The btn enthaltung. */
    private Button btnEnthaltung[] = null;

    /** The btn ungueltig. */
    private Button btnUngueltig[] = null;

    /** The btn nicht teilnahme. */
    private Button btnNichtTeilnahme[] = null;

    /** The btn nicht markiert. */
    private Button btnNichtMarkiert[] = null;

    /** The markierte stimmart. */
    private int markierteStimmart[] = null;

    /** The grpn DAN protokoll. */
    /*stimmenDetailPruefenStorno*/
    private GridPane grpnDANProtokoll = null;

    /** The grpn DAN eintrittskarten. */
    private GridPane grpnDANEintrittskarten = null;

    /** The grpn DAN stimmkarten. */
    private GridPane grpnDANStimmkarten = null;

    /** The grpn DAN uebersicht stimmabgaben. */
    private GridPane grpnDANUebersichtStimmabgaben = null;

    /** The grpn DAN detail stimmabgabe. */
    private GridPane grpnDANDetailStimmabgabe = null;

    /** The grpn DAN aktuelle stimmwertung. */
    private GridPane grpnDANAktuelleStimmwertung = null;

    /** The l db bundle. */
    private DbBundle lDbBundle = null;

    /** The bl abstimmung. */
    private BlAbstimmung blAbstimmung = null;

    /** Ausgewählte Ablauf-Funktion, entspricht Wert aus KonstAbstimmungsAblauf. */
    private int ausgewaehlteAblaufFunktion = 0;

    /**
     * wird bei Schnipsel/Markierungslesen gesetzt. Wenn Wechsel zwischen den
     * Funktionen, dann wird Protokoll etc. zurückgesetzt
     */
    private int letzterErfassungsvorgang = 0;

    /**
     * Je nach Einlesefunktion (Manuell) für SOMM und MBEM: Markierungen oder
     * Stimmschnipsel werden gelesen.
     */
    private boolean markierungenEinlesen = true;

    /** The stimm schnispel einlesen. */
    private boolean stimmSchnispelEinlesen = false;

    /** Für DRUEX: alle Druck-Jobs-Konfig aus Datenbank. */
    EclKonfigAuswertung[] konfigAuswertungAlle = null;

    /** Abstimmungsarchivierung. */
    private boolean archiviertNachAuswertung = true;

    /**
     * Initialize.
     */
    @FXML
    void initialize() {
        assert btnBeenden != null : "fx:id=\"btnBeenden\" was not injected: check your FXML file 'AbstimmungAblauf.fxml'.";
        assert gpAktiveAbstimmung != null : "fx:id=\"gpAktiveAbstimmung\" was not injected: check your FXML file 'AbstimmungAblauf.fxml'.";
        assert tfAktiveElektronischeAbstimmung != null : "fx:id=\"tfAktiveElektronischeAbstimmung\" was not injected: check your FXML file 'AbstimmungAblauf.fxml'.";
        assert tfZuVerarbeitendeAbstimmung != null : "fx:id=\"tfZuVerarbeitendeAbstimmung\" was not injected: check your FXML file 'AbstimmungAblauf.fxml'.";
        assert pnAblauf != null : "fx:id=\"pnAblauf\" was not injected: check your FXML file 'AbstimmungAblauf.fxml'.";
        assert gpAblauf != null : "fx:id=\"gpAblauf\" was not injected: check your FXML file 'AbstimmungAblauf.fxml'.";
        assert pnFunktionABAktivDeaktiv != null : "fx:id=\"pnFunktionABAktivDeaktiv\" was not injected: check your FXML file 'AbstimmungAblauf.fxml'.";
        assert spFunktionABAktivDeaktiv != null : "fx:id=\"spFunktionABAktivDeaktiv\" was not injected: check your FXML file 'AbstimmungAblauf.fxml'.";
        assert btnFunkionABAktivDeaktivFertig != null : "fx:id=\"btnFunkionABAktivDeaktivFertig\" was not injected: check your FXML file 'AbstimmungAblauf.fxml'.";
        assert pnFunktionStimmenOhneMarkierungManuell != null : "fx:id=\"pnFunktionStimmenOhneMarkierungManuell\" was not injected: check your FXML file 'AbstimmungAblaufKopie.fxml'.";
        assert cbFunktionSOMMAuswahlStimmart != null : "fx:id=\"cbFunktionSOMMAuswahlStimmart\" was not injected: check your FXML file 'AbstimmungAblaufKopie.fxml'.";
        assert tfFunktionSOMMEingabeNummer != null : "fx:id=\"tfFunktionSOMMEingabeNummer\" was not injected: check your FXML file 'AbstimmungAblaufKopie.fxml'.";
        assert tfFunktionSOMMStimmzettel != null : "fx:id=\"tfFunktionSOMMStimmzettel\" was not injected: check your FXML file 'AbstimmungAblaufKopie.fxml'.";
        assert tfFunktionSOMMStimmart != null : "fx:id=\"tfFunktionSOMMStimmart\" was not injected: check your FXML file 'AbstimmungAblaufKopie.fxml'.";
        assert btnFunktionSOMMBuchen != null : "fx:id=\"btnFunktionSOMMBuchen\" was not injected: check your FXML file 'AbstimmungAblaufKopie.fxml'.";
        assert btnFunktionSOMMFelderLeeren != null : "fx:id=\"btnFunktionSOMMFelderLeeren\" was not injected: check your FXML file 'AbstimmungAblaufKopie.fxml'.";
        assert tfFunktionSOMMBestaetigung != null : "fx:id=\"tfFunktionSOMMBestaetigung\" was not injected: check your FXML file 'AbstimmungAblaufKopie.fxml'.";
        assert tfFunktionSOMMAnzahlGelKarten != null : "fx:id=\"tfFunktionSOMMAnzahlGelKarten\" was not injected: check your FXML file 'AbstimmungAblaufKopie.fxml'.";
        assert btnFunktionSOMMZaehlerZuruecksetzen != null : "fx:id=\"btnFunktionSOMMZaehlerZuruecksetzen\" was not injected: check your FXML file 'AbstimmungAblaufKopie.fxml'.";
        assert spFunktionSOMM != null : "fx:id=\"spFunktionSOMM\" was not injected: check your FXML file 'AbstimmungAblaufKopie.fxml'.";
        assert btnFunktionSOMMLetztenStornieren != null : "fx:id=\"btnFunktionSOMMLetztenStornieren\" was not injected: check your FXML file 'AbstimmungAblaufKopie.fxml'.";
        assert btnFunktionSOMMFertig != null : "fx:id=\"btnFunktionSOMMFertig\" was not injected: check your FXML file 'AbstimmungAblaufKopie.fxml'.";

        assert pnMBEM != null : "fx:id=\"pnMBEM\" was not injected: check your FXML file 'AbstimmungAblaufKopie.fxml'.";
        assert lblMBEMUeberschrift != null : "fx:id=\"lblMBEMUeberschrift\" was not injected: check your FXML file 'AbstimmungAblaufKopie.fxml'.";
        assert tfMBEMEingabeNummer != null : "fx:id=\"tfMBEMEingabeNummer\" was not injected: check your FXML file 'AbstimmungAblaufKopie.fxml'.";
        assert tfMBEMStimmzettel != null : "fx:id=\"tfMBEMStimmzettel\" was not injected: check your FXML file 'AbstimmungAblaufKopie.fxml'.";
        assert tfMBEMStapelStimmart != null : "fx:id=\"tfMBEMStapelStimmart\" was not injected: check your FXML file 'AbstimmungAblaufKopie.fxml'.";
        assert btnMBEMBuchen != null : "fx:id=\"btnMBEMBuchen\" was not injected: check your FXML file 'AbstimmungAblaufKopie.fxml'.";
        assert btnMBEMFelderLeeren != null : "fx:id=\"btnMBEMFelderLeeren\" was not injected: check your FXML file 'AbstimmungAblaufKopie.fxml'.";
        assert tfMBEMBestaetigung != null : "fx:id=\"tfMBEMBestaetigung\" was not injected: check your FXML file 'AbstimmungAblaufKopie.fxml'.";
        assert spMBEMEingabeMarkierungen != null : "fx:id=\"spMBEMEingabeMarkierungen\" was not injected: check your FXML file 'AbstimmungAblaufKopie.fxml'.";
        assert tfMBEMAnzahlGelKarten != null : "fx:id=\"tfMBEMAnzahlGelKarten\" was not injected: check your FXML file 'AbstimmungAblaufKopie.fxml'.";
        assert btnMBEMZaehlerZuruecksetzen != null : "fx:id=\"btnMBEMZaehlerZuruecksetzen\" was not injected: check your FXML file 'AbstimmungAblaufKopie.fxml'.";
        assert spMBEMProtokoll != null : "fx:id=\"spMBEMProtokoll\" was not injected: check your FXML file 'AbstimmungAblaufKopie.fxml'.";
        assert btnMBEMLetztenStornieren != null : "fx:id=\"btnMBEMLetztenStornieren\" was not injected: check your FXML file 'AbstimmungAblaufKopie.fxml'.";
        assert btnMBEMFertig != null : "fx:id=\"btnMBEMFertig\" was not injected: check your FXML file 'AbstimmungAblaufKopie.fxml'.";
        assert btnMEBMAbbruch != null : "fx:id=\"btnMEBMAbbruch\" was not injected: check your FXML file 'AbstimmungAblaufKopie.fxml'.";
        assert btnMBEMLesen != null : "fx:id=\"btnMBEMLesen\" was not injected: check your FXML file 'AbstimmungAblaufKopie.fxml'.";

        assert pnFunktionDAN != null : "fx:id=\"pnFunktionDAN\" was not injected: check your FXML file 'AbstimmungAblauf.fxml'.";
        assert cbDANAnzeige != null : "fx:id=\"cbDANAnzeige\" was not injected: check your FXML file 'AbstimmungAblauf.fxml'.";
        assert btnDANRefresh != null : "fx:id=\"btnDANFehlerErneutVerarbeiten\" was not injected: check your FXML file 'AbstimmungAblaufKopie.fxml'.";
        assert btnDANFehlerErneutVerarbeiten != null : "fx:id=\"btnDANFehlerErneutVerarbeiten\" was not injected: check your FXML file 'AbstimmungAblaufKopie.fxml'.";
        assert spDANProtokoll != null : "fx:id=\"spDANProtokoll\" was not injected: check your FXML file 'AbstimmungAblauf.fxml'.";
        assert btnDANFertig != null : "fx:id=\"btnDANFertig\" was not injected: check your FXML file 'AbstimmungAblauf.fxml'.";
        assert tfDANNummerZurAnaylse != null : "fx:id=\"tfDANNummerZurAnaylse\" was not injected: check your FXML file 'AbstimmungAblauf.fxml'.";
        assert tabDANAktionaersdaten != null : "fx:id=\"tabDANAktionaersdaten\" was not injected: check your FXML file 'AbstimmungAblauf.fxml'.";
        assert tfDANStimmkarten != null : "fx:id=\"tfDANStimmkarten\" was not injected: check your FXML file 'AbstimmungAblauf.fxml'.";
        assert scpnDANEintrittskarten != null : "fx:id=\"scpnDANEintrittskarten\" was not injected: check your FXML file 'AbstimmungAblauf.fxml'.";
        assert tfDANInterneIdent != null : "fx:id=\"tfDANInterneIdent\" was not injected: check your FXML file 'AbstimmungAblauf.fxml'.";
        assert tfDANSammelkartenstatus != null : "fx:id=\"tfDANSammelkartenstatus\" was not injected: check your FXML file 'AbstimmungAblauf.fxml'.";
        assert tfDANName != null : "fx:id=\"tfDANName\" was not injected: check your FXML file 'AbstimmungAblauf.fxml'.";
        assert tfDANVorname != null : "fx:id=\"tfDANVorname\" was not injected: check your FXML file 'AbstimmungAblauf.fxml'.";
        assert tfDANOrt != null : "fx:id=\"tfDANOrt\" was not injected: check your FXML file 'AbstimmungAblauf.fxml'.";
        assert tfDANVertreterName != null : "fx:id=\"tfDANVertreterName\" was not injected: check your FXML file 'AbstimmungAblauf.fxml'.";
        assert tfDANVertreterVorname != null : "fx:id=\"tfDANVertreterVorname\" was not injected: check your FXML file 'AbstimmungAblauf.fxml'.";
        assert tfDANVertreterOrt != null : "fx:id=\"tfDANVertreterOrt\" was not injected: check your FXML file 'AbstimmungAblauf.fxml'.";
        assert tfDANStimmen != null : "fx:id=\"tfDANStimmen\" was not injected: check your FXML file 'AbstimmungAblauf.fxml'.";
        assert tfDANAnwesendDelayed != null : "fx:id=\"tfDANAnwesendDelayed\" was not injected: check your FXML file 'AbstimmungAblauf.fxml'.";
        assert tfDANAnwesendAktuell != null : "fx:id=\"tfDANAnwesendAktuell\" was not injected: check your FXML file 'AbstimmungAblauf.fxml'.";
        assert tfDANGattung != null : "fx:id=\"tfDANGattung\" was not injected: check your FXML file 'AbstimmungAblauf.fxml'.";
        assert tabDANUebersichtStimmabgaben != null : "fx:id=\"tabDANUebersichtStimmabgaben\" was not injected: check your FXML file 'AbstimmungAblauf.fxml'.";
        assert scrpnDANUebersichtStimmabgaben != null : "fx:id=\"scrpnDANUebersichtStimmabgaben\" was not injected: check your FXML file 'AbstimmungAblauf.fxml'.";
        assert tabDANDetailStimmabgabe != null : "fx:id=\"tabDANDetailStimmabgabe\" was not injected: check your FXML file 'AbstimmungAblauf.fxml'.";
        assert scpnDANDetailStimmabgabe != null : "fx:id=\"scpnDANDetailStimmabgabe\" was not injected: check your FXML file 'AbstimmungAblauf.fxml'.";
        assert btnDANVor != null : "fx:id=\"btnDANVor\" was not injected: check your FXML file 'AbstimmungAblauf.fxml'.";
        assert btnDANZurueck != null : "fx:id=\"btnDANZurueck\" was not injected: check your FXML file 'AbstimmungAblauf.fxml'.";
        assert btnDANStorno != null : "fx:id=\"btnDANStorno\" was not injected: check your FXML file 'AbstimmungAblauf.fxml'.";
        assert btnDANAktuellPruefen != null : "fx:id=\"btnDANAktuellPruefen\" was not injected: check your FXML file 'AbstimmungAblauf.fxml'.";
        assert tfDANDetailIdent != null : "fx:id=\"tfDANDetailIdent\" was not injected: check your FXML file 'AbstimmungAblauf.fxml'.";
        assert tfDANDetailVerarbeitet != null : "fx:id=\"tfDANDetailVerarbeitet\" was not injected: check your FXML file 'AbstimmungAblauf.fxml'.";
        assert tfDANDetailGeleseneNr != null : "fx:id=\"tfDANDetailGeleseneNr\" was not injected: check your FXML file 'AbstimmungAblauf.fxml'.";
        assert tfDANDetailWurdeStorniert != null : "fx:id=\"tfDANDetailWurdeStorniert\" was not injected: check your FXML file 'AbstimmungAblauf.fxml'.";
        assert tfDANDetailErteiltAufWeg != null : "fx:id=\"tfDANDetailErteiltAufWeg\" was not injected: check your FXML file 'AbstimmungAblauf.fxml'.";
        assert tfDANDetailZeitGespeichert != null : "fx:id=\"tfDANDetailZeitGespeichert\" was not injected: check your FXML file 'AbstimmungAblauf.fxml'.";
        assert tfDANDetailZeitAbgegeben != null : "fx:id=\"tfDANDetailZeitAbgegeben\" was not injected: check your FXML file 'AbstimmungAblauf.fxml'.";
        assert tabDANAktuelleStimmwertung != null : "fx:id=\"tabDANAktuelleStimmwertung\" was not injected: check your FXML file 'AbstimmungAblauf.fxml'.";
        assert scpnAktuelleStimmwertung != null : "fx:id=\"scpnAktuelleStimmwertung\" was not injected: check your FXML file 'AbstimmungAblauf.fxml'.";
        assert btnDANClear != null : "fx:id=\"tfDANClear\" was not injected: check your FXML file 'AbstimmungAblauf.fxml'.";

        assert pnMON != null : "fx:id=\"pnMON\" was not injected: check your FXML file 'AbstimmungAblaufKopie.fxml'.";
        assert lblMONUeberschrift != null : "fx:id=\"lblMONUeberschrift\" was not injected: check your FXML file 'AbstimmungAblaufKopie.fxml'.";
        assert btnMONFertig != null : "fx:id=\"btnMONFertig\" was not injected: check your FXML file 'AbstimmungAblaufKopie.fxml'.";
        assert gpMONTablet != null : "fx:id=\"gpMONTablet\" was not injected: check your FXML file 'AbstimmungAblaufKopie.fxml'.";
        assert lblMONUeberschriftTablet != null : "fx:id=\"lblMONUeberschriftTablet\" was not injected: check your FXML file 'AbstimmungAblaufKopie.fxml'.";
        assert btnMONRefreshTablets != null : "fx:id=\"btnMONRefreshTablets\" was not injected: check your FXML file 'AbstimmungAblaufKopie.fxml'.";

        assert pnTEDAT != null : "fx:id=\"pnTEDAT\" was not injected: check your FXML file 'AbstimmungAblaufKopie.fxml'.";
        assert lblTEDATUeberschrift != null : "fx:id=\"lblTEDATUeberschrift\" was not injected: check your FXML file 'AbstimmungAblaufKopie.fxml'.";
        assert btnTEDATFertig != null : "fx:id=\"btnTEDATFertig\" was not injected: check your FXML file 'AbstimmungAblaufKopie.fxml'.";
        assert gpTEDATProtokoll != null : "fx:id=\"gpTEDATProtokoll\" was not injected: check your FXML file 'AbstimmungAblaufKopie.fxml'.";
        assert btnTEDATDateiAuswaehlen != null : "fx:id=\"btnTEDATDateiAuswaehlen\" was not injected: check your FXML file 'AbstimmungAblaufKopie.fxml'.";
        assert tfTEDATEinzulesendeDatei != null : "fx:id=\"tfTEDATEinzulesendeDatei\" was not injected: check your FXML file 'AbstimmungAblaufKopie.fxml'.";
        assert btnTEDATScannerLesen != null : "fx:id=\"btnTEDATScannerLesen\" was not injected: check your FXML file 'AbstimmungAblaufKopie.fxml'.";
        assert btnTEDATTabletDatei != null : "fx:id=\"btnTEDATTabletDatei\" was not injected: check your FXML file 'AbstimmungAblaufKopie.fxml'.";
        assert btnTEDATScannerLesenDBErst != null : "fx:id=\"btnTEDATScannerLesenDBErst\" was not injected: check your FXML file 'AbstimmungAblaufKopie.fxml'.";
        assert btnTEDATScannerLesenDBWieder != null : "fx:id=\"btnTEDATScannerLesenDBWieder\" was not injected: check your FXML file 'AbstimmungAblaufKopie.fxml'.";

        assert pnAUSW != null : "fx:id=\"pnAUSW\" was not injected: check your FXML file 'AbstimmungAblaufKopie.fxml'.";
        assert lblAUSWUeberschrift != null : "fx:id=\"lblAUSWUeberschrift\" was not injected: check your FXML file 'AbstimmungAblaufKopie.fxml'.";
        assert btnAUSWFertig != null : "fx:id=\"btnAUSWFertig\" was not injected: check your FXML file 'AbstimmungAblaufKopie.fxml'.";
        assert gpAUSWProtokoll != null : "fx:id=\"gpAUSWProtokoll\" was not injected: check your FXML file 'AbstimmungAblaufKopie.fxml'.";
        assert btnAUSWAuswerten != null : "fx:id=\"btnAUSWAuswerten\" was not injected: check your FXML file 'AbstimmungAblaufKopie.fxml'.";

        assert pnDRUEX != null : "fx:id=\"pnDRUEX\" was not injected: check your FXML file 'AbstimmungAblaufKopie.fxml'.";
        assert lblDRUEXUeberschrift != null : "fx:id=\"lblDRUEXUeberschrift\" was not injected: check your FXML file 'AbstimmungAblaufKopie.fxml'.";
        assert btnDRUEXFertig != null : "fx:id=\"btnDRUEXFertig\" was not injected: check your FXML file 'AbstimmungAblaufKopie.fxml'.";
        assert gpDRUEXProtokoll != null : "fx:id=\"gpDRUEXProtokoll\" was not injected: check your FXML file 'AbstimmungAblaufKopie.fxml'.";
        assert btnDRUEXLangfassung != null : "fx:id=\"btnDRUEXLangfassung\" was not injected: check your FXML file 'AbstimmungAblaufKopie.fxml'.";
        assert btnDRUEXKurzfassung != null : "fx:id=\"btnDRUEXKurzfassung\" was not injected: check your FXML file 'AbstimmungAblaufKopie.fxml'.";
        assert btnDRUEXListe != null : "fx:id=\"btnDRUEXListe\" was not injected: check your FXML file 'AbstimmungAblaufKopie.fxml'.";
        assert btnDRUEXPraesentationPDF != null : "fx:id=\"btnDRUEXPraesentationPDF\" was not injected: check your FXML file 'AbstimmungAblaufKopie.fxml'.";
        assert tfDRUEXLangfassung != null : "fx:id=\"tfDRUEXLangfassung\" was not injected: check your FXML file 'AbstimmungAblaufKopie.fxml'.";
        assert tfDRUEXKurzfassung != null : "fx:id=\"tfDRUEXKurzfassung\" was not injected: check your FXML file 'AbstimmungAblaufKopie.fxml'.";
        assert tfDRUEXListe != null : "fx:id=\"tfDRUEXListe\" was not injected: check your FXML file 'AbstimmungAblaufKopie.fxml'.";
        assert btnDRUEXPraesentationPP != null : "fx:id=\"btnDRUEXPraesentationPP\" was not injected: check your FXML file 'AbstimmungAblaufKopie.fxml'.";
        assert btnDRUEXTexteBuehne != null : "fx:id=\"btnDRUEXTexteBuehne\" was not injected: check your FXML file 'AbstimmungAblaufKopie.fxml'.";
        assert btnDRUEXExport != null : "fx:id=\"btnDRUEXExport\" was not injected: check your FXML file 'AbstimmungAblaufKopie.fxml'.";
        assert tfDRUEXPraesentationPDFNr != null : "fx:id=\"tfDRUEXPraesentationPDFNr\" was not injected: check your FXML file 'AbstimmungAblaufKopie.fxml'.";
        assert tfDRUEXPraesentationPDFDatei != null : "fx:id=\"tfDRUEXPraesentationPDFDatei\" was not injected: check your FXML file 'AbstimmungAblaufKopie.fxml'.";
        assert tfDRUEXPraesentationPDFPfad != null : "fx:id=\"tfDRUEXPraesentationPDFPfad\" was not injected: check your FXML file 'AbstimmungAblaufKopie.fxml'.";
        assert tfDRUEXPraesentationPPNr != null : "fx:id=\"tfDRUEXPraesentationPPNr\" was not injected: check your FXML file 'AbstimmungAblaufKopie.fxml'.";
        assert tfDRUEXPraesentationPPDatei != null : "fx:id=\"tfDRUEXPraesentationPPDatei\" was not injected: check your FXML file 'AbstimmungAblaufKopie.fxml'.";
        assert tfDRUEXPraesentationPPPfad != null : "fx:id=\"tfDRUEXPraesentationPPPfad\" was not injected: check your FXML file 'AbstimmungAblaufKopie.fxml'.";
        assert tfDRUEXTexteBuehneNr != null : "fx:id=\"tfDRUEXTexteBuehneNr\" was not injected: check your FXML file 'AbstimmungAblaufKopie.fxml'.";
        assert tfDRUEXTexteBuehneDatei != null : "fx:id=\"tfDRUEXTexteBuehneDatei\" was not injected: check your FXML file 'AbstimmungAblaufKopie.fxml'.";
        assert tfDRUEXTexteBuehnePfad != null : "fx:id=\"tfDRUEXTexteBuehnePfad\" was not injected: check your FXML file 'AbstimmungAblaufKopie.fxml'.";
        assert tfDRUEXExportNr != null : "fx:id=\"tfDRUEXExportNr\" was not injected: check your FXML file 'AbstimmungAblaufKopie.fxml'.";
        assert tfDRUEXExportDatei != null : "fx:id=\"tfDRUEXExportDatei\" was not injected: check your FXML file 'AbstimmungAblaufKopie.fxml'.";
        assert tfDRUEXExportPfad != null : "fx:id=\"tfDRUEXExportPfad\" was not injected: check your FXML file 'AbstimmungAblaufKopie.fxml'.";

        assert pnDELB != null : "fx:id=\"pnDELB\" was not injected: check your FXML file 'AbstimmungAblaufKopie.fxml'.";
        assert lblDELBUeberschrift != null : "fx:id=\"lblDELBUeberschrift\" was not injected: check your FXML file 'AbstimmungAblaufKopie.fxml'.";
        assert btnDELBStarten != null : "fx:id=\"btnDELBAuswerten\" was not injected: check your FXML file 'AbstimmungAblaufKopie.fxml'.";

        assert pnDELE != null : "fx:id=\"pnDELE\" was not injected: check your FXML file 'AbstimmungAblaufKopie.fxml'.";
        assert lblDELEUeberschrift != null : "fx:id=\"lblDELEUeberschrift\" was not injected: check your FXML file 'AbstimmungAblaufKopie.fxml'.";
        assert btnDELEStarten != null : "fx:id=\"btnDELEAuswerten\" was not injected: check your FXML file 'AbstimmungAblaufKopie.fxml'.";

        assert pnDELL != null : "fx:id=\"pnDELL\" was not injected: check your FXML file 'AbstimmungAblaufKopie.fxml'.";
        assert lblDELLUeberschrift != null : "fx:id=\"lblDELLUeberschrift\" was not injected: check your FXML file 'AbstimmungAblaufKopie.fxml'.";
        assert btnDELLStarten != null : "fx:id=\"btnDELLStarten\" was not injected: check your FXML file 'AbstimmungAblaufKopie.fxml'.";

        assert pnDELA != null : "fx:id=\"pnDELA\" was not injected: check your FXML file 'AbstimmungAblaufKopie.fxml'.";
        assert lblDELAUeberschrift != null : "fx:id=\"lblDELAUeberschrift\" was not injected: check your FXML file 'AbstimmungAblaufKopie.fxml'.";
        assert btnDELAStarten != null : "fx:id=\"btnDELAStarten\" was not injected: check your FXML file 'AbstimmungAblaufKopie.fxml'.";

        assert pnMONEK != null : "fx:id=\"pnMON\" was not injected: check your FXML file 'AbstimmungAblaufKopie.fxml'.";
        assert lblMONEKUeberschrift != null : "fx:id=\"lblMONEKUeberschrift\" was not injected: check your FXML file 'AbstimmungAblaufKopie.fxml'.";
        assert btnMONEKFertig != null : "fx:id=\"btnMONEKFertig\" was not injected: check your FXML file 'AbstimmungAblaufKopie.fxml'.";
        assert gpMONEKAbstimmungsverhalten != null : "fx:id=\"gpMONEKAbstimmungsverhalten\" was not injected: check your FXML file 'AbstimmungAblaufKopie.fxml'.";
        assert lblMONEKUeberschriftTablet != null : "fx:id=\"lblMONEKUeberschriftTablet\" was not injected: check your FXML file 'AbstimmungAblaufKopie.fxml'.";
        assert btnMONEKRefreshAlleStimmabgaben != null : "fx:id=\"btnMONEKRefreshAlleStimmabgaben\" was not injected: check your FXML file 'AbstimmungAblaufKopie.fxml'.";
        assert btnMONEKRefreshAbstimmungsverhalten != null : "fx:id=\"btnMONEKRefreshAbstimmungsverhalten\" was not injected: check your FXML file 'AbstimmungAblaufKopie.fxml'.";
        assert btnMONEKRefreshAktionaersdaten != null : "fx:id=\"btnMONEKRefreshAktionaersdaten\" was not injected: check your FXML file 'AbstimmungAblaufKopie.fxml'.";
        

        eigeneStage.setOnCloseRequest(new EventHandler<WindowEvent>() {

            @Override
            public void handle(WindowEvent event) {
                pruefenObArchiviert();
            }
        });

        lDbBundle = new DbBundle();

        lDbBundle.openAll();

        /** Ablauf-Bereich je nach Aufruf ausblenden oder füllen */
        if (aufrufFunktion > 0) {
            pnAblauf.setVisible(false);
        } else {
            zeigeAblaufAbstimmung();
            resetAblaufAuswahl();
        }

        /*FunktionSOMM initialisieren*/
        cbFunktionSOMMAuswahlStimmart.valueProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(@SuppressWarnings("rawtypes") ObservableValue ov, String alterWert, String neuerWert) {
                if (neuerWert == null || neuerWert.isEmpty()) {
                    return;
                }
                tfFunktionSOMMEingabeNummer.requestFocus();
            }
        });
        initSOMMProtokoll();

        /*Funktion DAN initialisieren*/
//        btnDANAktuellPruefen
//                .setVisible(false); /*Erst mal temporär immer ausgeschaltet; klären, ob überhaupt nich nötig*/

        lDbBundle.closeAll();
        zeigeAktiveAbstimmungen();
        
        btnDRUEXEdit.setGraphic(new FontIcon(FontAwesomeSolid.EDIT));
        btnDRUEXExportOrdner.setGraphic(new FontIcon(FontAwesomeSolid.FOLDER_OPEN));
        
    }

    /**
     * Pruefen ob archiviert.
     */
    private void pruefenObArchiviert() {
        if (archiviertNachAuswertung == false) {
            CaZeigeHinweis caZeigeHinweis = new CaZeigeHinweis();
            caZeigeHinweis.zeige(eigeneStage, "Achtung - Archivierung nach Auswertung noch nicht erfolgt!");
        }
    }

    /**
     * ***********************Grund-Dialog*************************************************
     * *************************************************************************************.
     */

    /***************** Logik **********************/

    /**************** Aktionen ******************/

    private int ausgewahelteAblaufFunktionIndex = -1;

    /**
     * Clicked ablauf auswahl.
     *
     * @param event the event
     */
    @FXML
    void clickedAblaufAuswahl(ActionEvent event) {

        resetAblaufAuswahl();

        for (int i = 0; i < btnAblauf.length; i++) {
            if (event.getSource() == btnAblauf[i]) {
                ausgewahelteAblaufFunktionIndex = i;
                ausgewaehlteAblaufFunktion = ParamS.param.paramAbstimmungParameter.ablaufAbstimmung[i];
            }
        }
        switch (ausgewaehlteAblaufFunktion) {
        case KonstAbstimmungsAblauf.abstimmungsblockAktivierenDeaktivieren: {
            ausfuehrenAbstimmungsblockAktivierenDeaktivieren();
            break;
        }

        case KonstAbstimmungsAblauf.abstimmungRefresh: {
            ausfuehrenAbstimmungRefresh();
            break;
        }

        case KonstAbstimmungsAblauf.stimmenOhneMarkierungenEinlesenManuell: {
            markierungenEinlesen = false;
            stimmSchnispelEinlesen = true;
            ausfuehrenStimmenOhneMarkierungenEinlesenManuell();
            break;
        }

        case KonstAbstimmungsAblauf.stimmenMarkierungsBelegeEinlesenManuell: {
            markierungenEinlesen = true;
            stimmSchnispelEinlesen = false;
            ausfuehrenMBEMBelege();
            break;
        }

        case KonstAbstimmungsAblauf.stimmenMarkierungenEinlesenManuell: {
            markierungenEinlesen = true;
            stimmSchnispelEinlesen = false;
            ausfuehrenMBEMBlock();
            break;
        }

        case KonstAbstimmungsAblauf.stimmsammlungMonitor: {
            ausfuehrenMON();
            break;
        }

        case KonstAbstimmungsAblauf.stimmenEinlesenScannerDatei: {
            ausfuehrenTEDAT();
            break;
        }

        case KonstAbstimmungsAblauf.stimmenEinlesenScannerDatenbank: {
            ausfuehrenTEDAT();
            break;
        }

        case KonstAbstimmungsAblauf.stimmenEinlesenTabletKarte: {
            ausfuehrenTEDAT();
            break;
        }

        case KonstAbstimmungsAblauf.stimmenDetailPruefenStorno: {
            ausfuehrenDAN();
            break;
        }

        case KonstAbstimmungsAblauf.ergebnisAuswerten: {
            ausfuehrenAUSW();
            break;
        }

        case KonstAbstimmungsAblauf.ergebnisDruckExport: {
            ausfuehrenDRUEX();
            break;
        }

        case KonstAbstimmungsAblauf.naechsteAbstimmung: {
            ausfuehrenNaechsteAbstimmung();
            break;
        }

        case KonstAbstimmungsAblauf.delaySammelBeginn: {
            ausfuehrenDelayBeginnAbstimmung();
            break;
        }

        case KonstAbstimmungsAblauf.delaySammelEnde: {
            ausfuehrenDelayEndeAbstimmung();
            break;
        }

        case KonstAbstimmungsAblauf.delayAufloesen: {
            ausfuehrenDelayAufloesen();
            break;
        }

        case KonstAbstimmungsAblauf.delayEnde: {
            ausfuehrenDelayAus();
            break;
        }

        case KonstAbstimmungsAblauf.stimmsammlungMonitorEK: {
            ausfuehrenMonitorEK();
            break;
        }

        }
    }

    /**
     * Ab hier: jeweiligen Button des Abstimmungsablaufs verarbeiten.
     */
    private void ausfuehrenAbstimmungRefresh() {
        zeigeAktiveAbstimmungen();
        erledigtAblaufAuswahl();
        resetAblaufAuswahl();
    }

    /**
     * Ausfuehren naechste abstimmung.
     */
    private void ausfuehrenNaechsteAbstimmung() {
        for (int i = 0; i < btnAblauf.length; i++) {
            ParamS.param.paramAbstimmungParameter.ablaufAbstimmungErledigt[i] = false;
            if (ParamS.param.paramAbstimmungParameter.ablaufAbstimmung[i] == KonstAbstimmungsAblauf.abstimmungRefresh) {
                ParamS.param.paramAbstimmungParameter.ablaufAbstimmungErledigt[i] = true;
            }
        }
        zeigeAblaufAbstimmung();
        zeigeAktiveAbstimmungen();
        resetAblaufAuswahl();
        initSOMMProtokoll();
    }

    /**
     * Reset ablauf auswahl.
     */
    private void resetAblaufAuswahl() {
        ausgewaehlteAblaufFunktion = 0;
        ausgewahelteAblaufFunktionIndex = -1;

        pnFunktionABAktivDeaktiv.setVisible(false);
        pnFunktionStimmenOhneMarkierungManuell.setVisible(false);
        pnMBEM.setVisible(false);
        pnFunktionDAN.setVisible(false);
        pnMON.setVisible(false);
        pnTEDAT.setVisible(false);
        pnAUSW.setVisible(false);
        pnDRUEX.setVisible(false);

        pnDELB.setVisible(false);
        pnDELE.setVisible(false);
        pnDELL.setVisible(false);
        pnDELA.setVisible(false);
        pnMONEK.setVisible(false);

    }

    /**
     * Erledigt ablauf auswahl.
     */
    private void erledigtAblaufAuswahl() {
        ParamS.param.paramAbstimmungParameter.ablaufAbstimmungErledigt[ausgewahelteAblaufFunktionIndex] = true;
        btnAblauf[ausgewahelteAblaufFunktionIndex].setStyle("-fx-background-color: #00ff00");
    }

    /**
     * ***************Anzeige-Funktionen*************************.
     */
    /** Ablauf-Steuerungsleiste anzeigen */
    private void zeigeAblaufAbstimmung() {

        grpnAblauf = new GridPane();
        grpnAblauf.setVgap(5);
        grpnAblauf.setHgap(15);

        int laengeAblauf = ParamS.param.paramAbstimmungParameter.ablaufAbstimmung.length;

        btnAblauf = new Button[laengeAblauf];

        for (int i = 0; i < laengeAblauf; i++) {
            btnAblauf[i] = new Button();
            btnAblauf[i]
                    .setText(KonstAbstimmungsAblauf.getText(ParamS.param.paramAbstimmungParameter.ablaufAbstimmung[i]));
            if (ParamS.param.paramAbstimmungParameter.ablaufAbstimmungErledigt[i]) {
                btnAblauf[i].setStyle("-fx-background-color: #00ff00");

            }
            btnAblauf[i].setOnAction(e -> {
                clickedAblaufAuswahl(e);
            });
            grpnAblauf.add(btnAblauf[i], 0, i);

        }

        gpAblauf.setContent(grpnAblauf);

    }

    /**
     * Aktive Abstimmungen (oberer Bildschirmbereich) anzeigen lDbBundle wird in der
     * Funktion eröffnet.
     */
    private int zuVerarbeitenderAbstimmungsblock = 0;

    /**
     * Zeige aktive abstimmungen.
     */
    private void zeigeAktiveAbstimmungen() {
        lDbBundle.openAll();

        blAbstimmung = new BlAbstimmung(lDbBundle);

        grpnAnzuzeigendeAbstimmungen = new GridPane();
        grpnAnzuzeigendeAbstimmungen.setVgap(5);
        grpnAnzuzeigendeAbstimmungen.setHgap(15);

        boolean erg = blAbstimmung.leseAnzuzeigendeAbstimmungsbloecke();
        if (erg == false) {
            Label llabel = new Label("keine aktiv");
            grpnAnzuzeigendeAbstimmungen.add(llabel, 0, 0);
            tfAktiveElektronischeAbstimmung.setText("keine aktiv");
        } else {
            for (int i = 0; i < blAbstimmung.anzuzeigendeAbstimmungsbloecke.length; i++) {
                System.out.println("i=" + i);
                Label llabel = new Label(blAbstimmung.anzuzeigendeAbstimmungsbloecke[i].kurzBeschreibung);
                grpnAnzuzeigendeAbstimmungen.add(llabel, 0, i);
            }
        }
        gpAktiveAbstimmung.setContent(grpnAnzuzeigendeAbstimmungen);

        erg = blAbstimmung.leseEinzusammelndenAbstimmungsblock();
        if (erg == false) {
            tfAktiveElektronischeAbstimmung.setText("keiner oder zu viele aktiv");
        } else {
            tfAktiveElektronischeAbstimmung.setText(blAbstimmung.einzusammelnderAbstimmungsblock.kurzBeschreibung);
        }
        tfAktiveElektronischeAbstimmung.setEditable(false);

        erg = blAbstimmung.leseZuVerarbeitendenAbstimmungsblock();
        if (erg == false) {
            tfZuVerarbeitendeAbstimmung.setText("keiner oder zu viele aktiv");
            zuVerarbeitenderAbstimmungsblock = 0;
        } else {
            auswertenText();
            tfZuVerarbeitendeAbstimmung.setText(blAbstimmung.zuVerarbeitenderAbstimmungsblock.kurzBeschreibung);
            zuVerarbeitenderAbstimmungsblock = blAbstimmung.zuVerarbeitenderAbstimmungsblock.ident;
        }
        tfZuVerarbeitendeAbstimmung.setEditable(false);

        /**Keiner => -1*/
        erg = blAbstimmung.leseLokalenAbstimmungsblock(aktivDeaktivLokalAbstimmungsblock);
        if (erg == false) {
            tfAktiveLokaleAbstimmung.setText("");
        } else {
            auswertenText();
            tfAktiveLokaleAbstimmung.setText(blAbstimmung.lokalerAbstimmungsblock.kurzBeschreibung);
        }
        tfAktiveLokaleAbstimmung.setEditable(false);

        lDbBundle.closeAll();

    }
    
    private void auswertenText() {
        if (blAbstimmung != null) {
            Set<String> set = new HashSet<>();
            if (blAbstimmung.leseAktivenAbstimmungsblock()) {
                for (var tmp : blAbstimmung.abstimmungen) {
                    if (!tmp.liefereIstUeberschift()) {
                        switch (tmp.stimmenAuswerten) {
                        case -1 -> set.add("Addition");
                        case KonstStimmart.ja -> set.add("Subtraktion");
                        case KonstStimmart.nein -> set.add("Nein");
                        case KonstStimmart.enthaltung -> set.add("Enthaltung");
                        }
                    }
                }
                lblAUSWUeberschrift.setText("Auswerten: " + Arrays.toString(set.toArray()));
            }
        }
    }

    /** *********Initialisierung*****************. */

    private Stage eigeneStage;

    /** The aufruf funktion. */
    private int aufrufFunktion = -1;

    /**
     * pFunktion = aufrufFunktion; 0 = gesamter Abstimmungsablauf; >0 ist ein
     * spezieller Ablaufpunkt (Direktsprung).
     *
     * @param pEigeneStage the eigene stage
     * @param pFunktion    the funktion
     */
    public void init(Stage pEigeneStage, int pFunktion) {
        eigeneStage = pEigeneStage;
        aufrufFunktion = pFunktion;
    }

    /**
     * *********************************************************************
     * ******************abstimmungsblockAktivierenDeaktivieren*************.
     */

    /***************** Logik **********************/
    private EclAbstimmungsblock[] eclAbstimmungsbloecke = null;

    /** The anz abstimmungsbloecke. */
    private int anzAbstimmungsbloecke = 0;

    /**
     * Ausfuehren abstimmungsblock aktivieren deaktivieren.
     */
    private void ausfuehrenAbstimmungsblockAktivierenDeaktivieren() {

        lDbBundle.openAll();
        lDbBundle.dbAbstimmungsblock.read_all();
        lDbBundle.closeAll();
        if (lDbBundle.dbAbstimmungsblock.anzErgebnis() == 0) {
            return;
        }
        eclAbstimmungsbloecke = lDbBundle.dbAbstimmungsblock.ergebnisArray;
        anzAbstimmungsbloecke = eclAbstimmungsbloecke.length;

        pnFunktionABAktivDeaktiv.setVisible(true);

        grpnFunktionABAktivDeaktiv = new GridPane();
        grpnFunktionABAktivDeaktiv.setVgap(5);
        grpnFunktionABAktivDeaktiv.setHgap(15);

        btnFunktionABAktivDeaktivDeaktivieren = new Button[anzAbstimmungsbloecke];
        btnFunktionABAktivDeaktivNurAnzeigen = new Button[anzAbstimmungsbloecke];
        btnFunktionABAktivDeaktivElektronischeAbstimmung = new Button[anzAbstimmungsbloecke];
        btnFunktionABAktivDeaktivVerarbeitungAbstimmung = new Button[anzAbstimmungsbloecke];
        btnFunktionABAktivDeaktivLokalAktiv = new Button[anzAbstimmungsbloecke];
        
        for (int i = 0; i < anzAbstimmungsbloecke; i++) {
            Label lLabelIdent = new Label(Integer.toString(eclAbstimmungsbloecke[i].ident));
            grpnFunktionABAktivDeaktiv.add(lLabelIdent, 0, i);

            Label lLabelBezeichnung = new Label(eclAbstimmungsbloecke[i].kurzBeschreibung);
            grpnFunktionABAktivDeaktiv.add(lLabelBezeichnung, 1, i);

            String statusText = "";
            switch (eclAbstimmungsbloecke[i].aktiv) {
            case 0:
                statusText = "nicht aktiv";
                break;
            case 1:
                statusText = "nur Anzeigen";
                break;
            case 2:
                statusText = "Elektronisches Einsammeln/Verarbeitung";
                break;
            case 3:
                statusText = "Verarbeitung";
                break;
            }
            if (aktivDeaktivLokalAbstimmungsblock==eclAbstimmungsbloecke[i].ident) {
                statusText=statusText+"//lokal aktiv";
            }
            Label lLabelStatus = new Label(statusText);
            grpnFunktionABAktivDeaktiv.add(lLabelStatus, 2, i);

            btnFunktionABAktivDeaktivDeaktivieren[i] = new Button("Deaktivieren");
            btnFunktionABAktivDeaktivDeaktivieren[i].setOnAction(e -> {
                clickedAktivDeaktiv(e);
            });

            btnFunktionABAktivDeaktivNurAnzeigen[i] = new Button("Anzeigen");
            btnFunktionABAktivDeaktivNurAnzeigen[i].setOnAction(e -> {
                clickedAktivDeaktiv(e);
            });

            btnFunktionABAktivDeaktivElektronischeAbstimmung[i] = new Button("elekt. Abstimmung");
            btnFunktionABAktivDeaktivElektronischeAbstimmung[i].setOnAction(e -> {
                clickedAktivDeaktiv(e);
            });

            btnFunktionABAktivDeaktivVerarbeitungAbstimmung[i] = new Button("Verarbeitung");
            btnFunktionABAktivDeaktivVerarbeitungAbstimmung[i].setOnAction(e -> {
                clickedAktivDeaktiv(e);
            });

            btnFunktionABAktivDeaktivLokalAktiv[i] = new Button("Lokal aktivieren");
            btnFunktionABAktivDeaktivLokalAktiv[i].setOnAction(e -> {
                clickedAktivDeaktiv(e);
            });

            if (eclAbstimmungsbloecke[i].aktiv != 0) {
                grpnFunktionABAktivDeaktiv.add(btnFunktionABAktivDeaktivDeaktivieren[i], 3, i);
            }
            if (eclAbstimmungsbloecke[i].aktiv != 1) {
                grpnFunktionABAktivDeaktiv.add(btnFunktionABAktivDeaktivNurAnzeigen[i], 4, i);
            }
            if (eclAbstimmungsbloecke[i].aktiv != 2) {
                grpnFunktionABAktivDeaktiv.add(btnFunktionABAktivDeaktivElektronischeAbstimmung[i], 5, i);
            }
            if (eclAbstimmungsbloecke[i].aktiv != 3) {
                grpnFunktionABAktivDeaktiv.add(btnFunktionABAktivDeaktivVerarbeitungAbstimmung[i], 6, i);
            }
            if (aktivDeaktivLokalAbstimmungsblock!=eclAbstimmungsbloecke[i].ident) {
                grpnFunktionABAktivDeaktiv.add(btnFunktionABAktivDeaktivLokalAktiv[i], 7, i);
            }

        }

        spFunktionABAktivDeaktiv.setContent(grpnFunktionABAktivDeaktiv);

    }

    /**
     * Block deaktivieren.
     *
     * @param pOffset the offset
     */
    private void blockDeaktivieren(int pOffset) {
        aktivDeaktivLokalAbstimmungsblock=-1;
        lDbBundle.openAll();
        EclAbstimmungsblock lAbstimmungsblock = eclAbstimmungsbloecke[pOffset];
        lAbstimmungsblock.aktiv = 0;
        lDbBundle.dbAbstimmungsblock.update(lAbstimmungsblock);
        lDbBundle.closeAll();
        blockRefresh();
    }

    /**
     * Block nur anzeigen.
     *
     * @param pOffset the offset
     */
    private void blockNurAnzeigen(int pOffset) {
        aktivDeaktivLokalAbstimmungsblock=-1;
        lDbBundle.openAll();
        EclAbstimmungsblock lAbstimmungsblock = eclAbstimmungsbloecke[pOffset];
        lAbstimmungsblock.aktiv = 1;
        lDbBundle.dbAbstimmungsblock.update(lAbstimmungsblock);
        lDbBundle.closeAll();
        blockRefresh();
    }

    /**
     * Block elektronische abstimmung.
     *
     * @param pOffset the offset
     */
    private void blockElektronischeAbstimmung(int pOffset) {
        aktivDeaktivLokalAbstimmungsblock=-1;
        lDbBundle.openAll();
        lDbBundle.dbAbstimmungsblock.update_RuecksetzenStatusAufAnzeigen();

        EclAbstimmungsblock lAbstimmungsblock = eclAbstimmungsbloecke[pOffset];
        lAbstimmungsblock.aktiv = 2;
        lDbBundle.dbAbstimmungsblock.update(lAbstimmungsblock);
        if (lDbBundle.param.paramAbstimmungParameter.abstimmungsLoadBeiAktivierungImAbstimmungsablauf==1) {
            speichereReload();
        }
        lDbBundle.closeAll();
        blockRefresh();

    }

    /**
     * Block verarbeiten.
     *
     * @param pOffset the offset
     */
    private void blockVerarbeiten(int pOffset) {
        aktivDeaktivLokalAbstimmungsblock=-1;
        lDbBundle.openAll();
        lDbBundle.dbAbstimmungsblock.update_RuecksetzenStatusAufAnzeigen();

        EclAbstimmungsblock lAbstimmungsblock = eclAbstimmungsbloecke[pOffset];
        lAbstimmungsblock.aktiv = 3;
        lDbBundle.dbAbstimmungsblock.update(lAbstimmungsblock);
        
        if (lDbBundle.param.paramAbstimmungParameter.abstimmungsLoadBeiAktivierungImAbstimmungsablauf==1) {
            speichereReload();
        }
        
        lDbBundle.closeAll();
        blockRefresh();

    }

    private void speichereReload() {
        BvReload bvReload = new BvReload(lDbBundle);
        CaBug.druckeLog("Speichere reloadWeisungenAbbruch", logDrucken, 10);
        bvReload.setReloadWeisungen(lDbBundle.clGlobalVar.mandant);
         CaBug.druckeLog("Speichere reloadAbstimmungenAbbruch", logDrucken, 10);
        bvReload.setReloadAbstimmungen(lDbBundle.clGlobalVar.mandant);
         CaBug.druckeLog("Speichere reloadOhneAbbruch", logDrucken, 10);
        bvReload.setReloadAbstimmungenWeisungenOhneAbbruch(lDbBundle.clGlobalVar.mandant);
    }
        
    private void blockLokalAktiv(int pOffset) {
        aktivDeaktivLokalAbstimmungsblock=eclAbstimmungsbloecke[pOffset].ident;
        
//        lDbBundle.openAll();
//        lDbBundle.dbAbstimmungsblock.update_RuecksetzenStatusAufAnzeigen();
//
//        EclAbstimmungsblock lAbstimmungsblock = eclAbstimmungsbloecke[pOffset];
//        lAbstimmungsblock.aktiv = 3;
//        lDbBundle.dbAbstimmungsblock.update(lAbstimmungsblock);
//        lDbBundle.closeAll();
        blockRefresh();

    }

    /**
     * Nach dem Verändern eines Abstimmungsblocks aufzurufen - erneute Anzeige
     * sowohl des Kopfbereichs als auch der Abstimmungsblöcke.
     */
    private void blockRefresh() {
        ausfuehrenAbstimmungsblockAktivierenDeaktivieren();
        zeigeAktiveAbstimmungen();
    }

    /**
     * **************Aktionen*****************.
     *
     * @param event the event
     */
    @FXML
    void clickedAktivDeaktiv(ActionEvent event) {

        for (int i = 0; i < anzAbstimmungsbloecke; i++) {
            if (event.getSource() == btnFunktionABAktivDeaktivDeaktivieren[i]) {
                blockDeaktivieren(i);
            }
            if (event.getSource() == btnFunktionABAktivDeaktivNurAnzeigen[i]) {
                blockNurAnzeigen(i);
            }
            if (event.getSource() == btnFunktionABAktivDeaktivElektronischeAbstimmung[i]) {
                blockElektronischeAbstimmung(i);
            }
            if (event.getSource() == btnFunktionABAktivDeaktivVerarbeitungAbstimmung[i]) {
                blockVerarbeiten(i);
            }
            if (event.getSource() == btnFunktionABAktivDeaktivLokalAktiv[i]) {
                blockLokalAktiv(i);
            }
        }

    }

    /**
     * On btn funkion AB aktiv deaktiv fertig.
     *
     * @param event the event
     */
    @FXML
    void onBtnFunkionABAktivDeaktivFertig(ActionEvent event) {
        zeigeAktiveAbstimmungen();
        erledigtAblaufAuswahl();
        resetAblaufAuswahl();
    }

    /** ***************Anzeige-Funktionen*************************. */

    /***********************************************************************
     * ******************stimmenOhneMarkierungenEinlesenManuell*************
     */

    private int anzGeleseneSchnipsel = 0;

    /**
     * ***************Logik*********************.
     */
    private void ausfuehrenStimmenOhneMarkierungenEinlesenManuell() {

        lDbBundle.openAll();
        blAbstimmung = new BlAbstimmung(lDbBundle);
        blAbstimmung.leseAktivenOderLokalenAbstimmungsblock(aktivDeaktivLokalAbstimmungsblock);
        lDbBundle.closeAll();

        pnFunktionStimmenOhneMarkierungManuell.setVisible(true);

        /*cbFunktionSOMMAuswahlStimmart füllen*/
        cbFunktionSOMMAuswahlStimmart.getItems().removeAll(cbFunktionSOMMAuswahlStimmart.getItems());
        cbFunktionSOMMAuswahlStimmart.getItems().addAll("Ja (" + KonstStimmart.ja + ")");
        cbFunktionSOMMAuswahlStimmart.getItems().addAll("Nein (" + KonstStimmart.nein + ")");
        cbFunktionSOMMAuswahlStimmart.getItems().addAll("Enthaltung (" + KonstStimmart.enthaltung + ")");
        cbFunktionSOMMAuswahlStimmart.getItems().addAll("Ungültig (" + KonstStimmart.ungueltig + ")");
        cbFunktionSOMMAuswahlStimmart.getItems().addAll("Nichtteilnahme (" + KonstStimmart.nichtTeilnahme + ")");
        cbFunktionSOMMAuswahlStimmart.getItems().addAll("wie kodiert (9)");
        cbFunktionSOMMAuswahlStimmart.setValue("");

        clearSOMMEingabeFelder();
        zeigeAnzGeleseneSchnipsel();

        if (letzterErfassungsvorgang != KonstAbstimmungsAblauf.stimmenOhneMarkierungenEinlesenManuell) {
            letzterErfassungsvorgang = KonstAbstimmungsAblauf.stimmenOhneMarkierungenEinlesenManuell;
            anzGeleseneSchnipsel = 0;
            initSOMMProtokoll();

        }
    }

    /** The ausloeser funktion SOMM. */
    private int ausloeserFunktionSOMM = 0;

    /**
     * Die Funktion verarbeitenFunktionSOMM wird gemeinsam verwendet - auch von
     * MBEM. Verarbeitung abhängig von markierungenEinlesen. Wird gesteuert über
     * markierungenEinlesen, stimmSchnispelEinlesen
     */
    private void verarbeitenFunktionSOMM() {

        /**
         * Die Stimmart, die dann tatsächlich abgegeben wurde und zum Speichern an die
         * Abstimmungsroutine übergebene wird
         */
        int abgegebeneStimmart = 0;

        /** Der Stimmzettel, für denn die Stimme abgegeben wird */
        int abgegebenerStimmzettel = 0;

        /** Stimmart, die über Combobox ausgewählt wurde (nicht bei Markierungen) */
        int gewaehlteStimmart = 0;

        /**
         * Gattung der eingelesenen Meldung. Nur bei MBEM erforderlich - um nur die
         * zulässigen Abstimmungen auszufiltern
         */
        int gewaehlteGattung = 0;

        if (stimmSchnispelEinlesen) {
            /*In gewaehlteStimmart die im Auswahlfeld ausgewählte Stimmart (oder 9=wie Kodierung) eintragen*/
            String sGewaehlteStimmart = cbFunktionSOMMAuswahlStimmart.getValue();
            if (sGewaehlteStimmart == null || sGewaehlteStimmart.isEmpty()) {
                zeigeSOMMFehlermeldung("Fehler: Keine Stimmart ausgewählt!");
                return;
            }
            int hIndex = sGewaehlteStimmart.lastIndexOf('(');
            String kennzeichen = sGewaehlteStimmart.substring(hIndex + 1, hIndex + 2);
            gewaehlteStimmart = Integer.parseInt(kennzeichen);
        }

        /*Erste Überprüfung der Eingaben*/
        TextField lTFEingabeNummer = null;
        if (stimmSchnispelEinlesen) {
            lTFEingabeNummer = tfFunktionSOMMEingabeNummer;
        } else {
            lTFEingabeNummer = tfMBEMEingabeNummer;
        }
        String eingebeneNummer = lTFEingabeNummer.getText().trim();
        if (eingebeneNummer.isEmpty()) {
            zeigeSOMMFehlermeldung("Fehler: Keine Nummer zum Verarbeiten eingegeben!");
            lTFEingabeNummer.requestFocus();
            return;
        }

        TextField lTFStimmart = null;
        if (stimmSchnispelEinlesen) {
            lTFStimmart = tfFunktionSOMMStimmart;
        } else {
            lTFStimmart = tfMBEMStapelStimmart;
        }
        String sStimmart = lTFStimmart.getText().trim().toUpperCase();
        int eingegebeneStimmartSeparatesFeld = -1;
        if (!sStimmart.isEmpty()) {
            if (sStimmart.compareTo("J") == 0) {
                eingegebeneStimmartSeparatesFeld = KonstStimmart.ja;
            }
            if (sStimmart.compareTo("N") == 0) {
                eingegebeneStimmartSeparatesFeld = KonstStimmart.nein;
            }
            if (sStimmart.compareTo("E") == 0) {
                eingegebeneStimmartSeparatesFeld = KonstStimmart.enthaltung;
            }
            if (sStimmart.compareTo("U") == 0) {
                eingegebeneStimmartSeparatesFeld = KonstStimmart.ungueltig;
            }
            if (sStimmart.compareTo("NT") == 0) {
                eingegebeneStimmartSeparatesFeld = KonstStimmart.nichtTeilnahme;
            }
            if (eingegebeneStimmartSeparatesFeld == -1) {
                zeigeSOMMFehlermeldung("Fehler: Ungültige Stimmart eingegeben - nur J/N/E/U/NT zulässig!");
                lTFStimmart.requestFocus();
                return;
            }
        }

        TextField lTFStimmzettel = null;
        if (stimmSchnispelEinlesen) {
            lTFStimmzettel = tfFunktionSOMMStimmzettel;
        } else {
            lTFStimmzettel = tfMBEMStimmzettel;
        }
        String sStimmzettel = lTFStimmzettel.getText().trim();
        int eingegebenerStimmzettelSeparatesFeld = -1;
        if (!sStimmzettel.isEmpty()) {
            if (!CaString.isNummern(sStimmzettel)) {
                zeigeSOMMFehlermeldung("Fehler: Ungültiger Stimmzettel eingegeben - nur Zahlen zulässig!");
                lTFStimmzettel.requestFocus();
                return;
            }
            eingegebenerStimmzettelSeparatesFeld = Integer.parseInt(sStimmzettel);
        }

        /*Nummernform dekodieren*/

        boolean rcBool = dekodiereNummernform(eingebeneNummer);
        BlNummernformen blNummernformen = allgBlNummernformen;
        if (rcBool == false) {
            zeigeSOMMFehlermeldung(rcNummernformText);
            einfuegenSOMMProtokoll(grpnStimmkartenManuellprotokoll, spFunktionSOMM, eingebeneNummer, "", "Fehler", "",
                    0, "");
            clearSOMMNurEingabeFelder();
            return;
        }

        if (blNummernformen.rcGattung != 0) {
            gewaehlteGattung = blNummernformen.rcGattung;
        } else {
            gewaehlteGattung = 0;
        }

        if (stimmSchnispelEinlesen) {
            /*Stimmart abprüfen*/
            if (gewaehlteStimmart == 9) {
                /*Stimmart wurde nicht ausgewählt - "wie kodierung"*/
                if (blNummernformen.rcStimmart != 0 && eingegebeneStimmartSeparatesFeld != -1) {
                    /*Beide eingegeben*/
                    if (eingegebeneStimmartSeparatesFeld != blNummernformen.rcStimmart) {
                        zeigeSOMMFehlermeldung(
                                "Fehler: Kodierte Stimmart und eingegebene Stimmart stimmen nicht überein!");
                        einfuegenSOMMProtokoll(grpnStimmkartenManuellprotokoll, spFunktionSOMM, eingebeneNummer, "",
                                "Fehler", "", 0, "");
                        clearSOMMNurEingabeFelder();
                        return;
                    }
                } else {
                    /*Nur Kodierung oder Feldeingabe (oder auch gar nix ...)*/
                    if (blNummernformen.rcStimmart == 0 && eingegebeneStimmartSeparatesFeld == -1) {
                        /*keines eingegeben*/
                        zeigeSOMMFehlermeldung("Fehler: Stimmart weder in Kodierung noch in Eingabe enthalten!");
                        einfuegenSOMMProtokoll(grpnStimmkartenManuellprotokoll, spFunktionSOMM, eingebeneNummer, "",
                                "Fehler", "", 0, "");
                        clearSOMMNurEingabeFelder();
                        return;
                    } else { /*Nur eines enthalten - dieses dann verwenden*/
                        if (blNummernformen.rcStimmart != 0) {
                            abgegebeneStimmart = blNummernformen.rcStimmart;
                        } else {
                            abgegebeneStimmart = eingegebeneStimmartSeparatesFeld;
                        }
                    }
                }
            } else {
                /*Stimmart wurde ausgewählt - nicht "wie kodierung"*/
                if (blNummernformen.rcStimmart != 0 && gewaehlteStimmart != blNummernformen.rcStimmart) {
                    /*Nummernform ist kodiert, und weicht von ausgewählter Stimmart ab*/
                    zeigeSOMMFehlermeldung("Fehler: Stimmart in Kodierung weicht von ausgewählter Stimmart ab!");
                    einfuegenSOMMProtokoll(grpnStimmkartenManuellprotokoll, spFunktionSOMM, eingebeneNummer, "",
                            "Fehler", "", 0, "");
                    clearSOMMNurEingabeFelder();
                    return;
                }
                if (eingegebeneStimmartSeparatesFeld != -1 && gewaehlteStimmart != eingegebeneStimmartSeparatesFeld) {
                    /*Nummernform ist eingegeben, und weicht von ausgewählter Stimmart ab*/
                    zeigeSOMMFehlermeldung("Fehler: Eingegebene Stimmart weicht von ausgewählter Stimmart ab!");
                    einfuegenSOMMProtokoll(grpnStimmkartenManuellprotokoll, spFunktionSOMM, eingebeneNummer, "",
                            "Fehler", "", 0, "");
                    clearSOMMNurEingabeFelder();
                    return;
                }
                abgegebeneStimmart = gewaehlteStimmart;
            }
        } else { /*Markierungslesen*/
            if (eingegebeneStimmartSeparatesFeld != -1) {
                gewaehlteStimmart = eingegebeneStimmartSeparatesFeld;
            } else {
                gewaehlteStimmart = 0;

            }
        }

        int rcStimmzettelnummer = 0;
        if (stimmSchnispelEinlesen
                || ausgewaehlteAblaufFunktion == KonstAbstimmungsAblauf.stimmenMarkierungsBelegeEinlesenManuell) {
            /*Stimmzettel abprüfen*/
            if (eingegebenerStimmzettelSeparatesFeld == -1 && blNummernformen.rcStimmkartennummer == 0) {
                /*Stimmkartennummer weder in Kodierung noch in Eingabefeld vorhanden*/
                zeigeSOMMFehlermeldung("Fehler: Stimmzettelnummer weder kodiert noch eingegeben!");
                einfuegenSOMMProtokoll(grpnStimmkartenManuellprotokoll, spFunktionSOMM, eingebeneNummer, "", "Fehler",
                        "", 0, "");
                clearSOMMNurEingabeFelder();
                return;
            }
            if (eingegebenerStimmzettelSeparatesFeld != -1 && blNummernformen.rcStimmkartennummer != 0) {
                /*Stimmkartennummer in Kodierung und in Eingabefeld vorhanden*/
                if (eingegebenerStimmzettelSeparatesFeld != blNummernformen.rcStimmkartennummer) {
                    zeigeSOMMFehlermeldung(
                            "Fehler: Kodierte Stimmzettelnummer und eingegebenee Stimmzettelnummer stimmen nicht überein!");
                    einfuegenSOMMProtokoll(grpnStimmkartenManuellprotokoll, spFunktionSOMM, eingebeneNummer, "",
                            "Fehler", "", 0, "");
                    clearSOMMNurEingabeFelder();
                    return;
                }
            }
            if (eingegebenerStimmzettelSeparatesFeld != -1) {
                abgegebenerStimmzettel = eingegebenerStimmzettelSeparatesFeld;
            }
            if (blNummernformen.rcStimmkartennummer != 0) {
                abgegebenerStimmzettel = blNummernformen.rcStimmkartennummer;
            }
            rcStimmzettelnummer = blAbstimmung.liefereLfdAbstimmungsNrZuStimmkartenNrUndPosition(abgegebenerStimmzettel,
                    -1);
            if (rcStimmzettelnummer == -1) {
                zeigeSOMMFehlermeldung("Fehler: Stimmzettelnummer nicht aktiv!");
                einfuegenSOMMProtokoll(grpnStimmkartenManuellprotokoll, spFunktionSOMM, eingebeneNummer, "", "Fehler",
                        "", 0, "");
                clearSOMMNurEingabeFelder();
                return;
            }
        }

        /*Meldungs-Satz identifizieren / einlesen*/
        lDbBundle.openAll();
        int meldungsIdent = 0; /*Ident der Meldung, für die die Stimme abgegeben wird*/
        String identifikationsNummer = ""; /*Verwendete Identifikationsnummer zum Speichern*/
        if (blNummernformen.rcKartenklasse == KonstKartenklasse.eintrittskartennummer) {
            EclZutrittsIdent lZutrittsIdent = new EclZutrittsIdent();
            lZutrittsIdent.zutrittsIdent = blNummernformen.rcIdentifikationsnummer.get(0);
            lZutrittsIdent.zutrittsIdentNeben = blNummernformen.rcIdentifikationsnummerNeben.get(0);
            identifikationsNummer = lZutrittsIdent.zutrittsIdent + "-" + lZutrittsIdent.zutrittsIdentNeben;
            int rcEK = blAbstimmung.pruefeZutrittsIdentZulaessigFuerAbstimmung(lZutrittsIdent);
            if (rcEK < 1) {
                switch (rcEK) {
                case CaFehler.pmZutrittsIdentNichtVorhanden: {
                    zeigeSOMMFehlermeldung("Warnung: Eintrittskartennummer nicht vorhanden!");
                    break;
                }
                case CaFehler.pmZutrittsIdentGesperrt: {
                    zeigeSOMMFehlermeldung("Warnung: Eintrittskartennummer ist gesperrt!");
                    break;
                }
                case CaFehler.pmEintrittskarteVerweistAufGast: {
                    zeigeSOMMFehlermeldung("Warnung: Eintrittskartennummer verweist auf Gast!");
                    break;
                }
                }
                if (stimmSchnispelEinlesen) {
                    blAbstimmung.starteSpeichernFuerMeldung(null, 0, false,
                            KonstWillenserklaerungWeg.abstManuelleEingabe, blNummernformen.rcKartenklasse,
                            identifikationsNummer, rcEK);
                    blAbstimmung.setzeMarkierungZuAbstimmungsPosition(abgegebeneStimmart, rcStimmzettelnummer, KonstWillenserklaerungWeg.abstManuelleEingabe);
                    int identRaw = blAbstimmung.beendeSpeichernFuerMeldung();
                    einfuegenSOMMProtokoll(grpnStimmkartenManuellprotokoll, spFunktionSOMM, eingebeneNummer, "",
                            "Warnung", "", identRaw, "");
                    clearSOMMNurEingabeFelder();
                } else {
                    starteMarkierungLesenMBEM(identifikationsNummer, eingebeneNummer, blNummernformen.rcKartenklasse,
                            null, rcEK, abgegebenerStimmzettel, gewaehlteStimmart, gewaehlteGattung);
                }
                lDbBundle.closeAll();
                return;
            }
            meldungsIdent = rcEK;
        }
        if (blNummernformen.rcKartenklasse == KonstKartenklasse.stimmkartennummer) {

            identifikationsNummer = blNummernformen.rcIdentifikationsnummer.get(0);
            int rcEK = blAbstimmung.pruefeStimmkarteZulaessigFuerAbstimmung(identifikationsNummer);
            if (rcEK < 1) {
                switch (rcEK) {
                case CaFehler.pmStimmkarteNichtVorhanden: {
                    zeigeSOMMFehlermeldung("Warnung: Stimmkartennummer nicht vorhanden!");
                    break;
                }
                case CaFehler.pmStimmkarteGesperrt: {
                    zeigeSOMMFehlermeldung("Warnung: Stimmkartennummer ist gesperrt!");
                    break;
                }
                case CaFehler.pmStimmkarteVerweistAufGast: {
                    zeigeSOMMFehlermeldung("Warnung: Stimmkartennummer verweist auf Gast!");
                    break;
                }
                }
                if (stimmSchnispelEinlesen) {
                    blAbstimmung.starteSpeichernFuerMeldung(null, 0, false,
                            KonstWillenserklaerungWeg.abstManuelleEingabe, blNummernformen.rcKartenklasse,
                            identifikationsNummer, rcEK);
                    blAbstimmung.setzeMarkierungZuAbstimmungsPosition(abgegebeneStimmart, rcStimmzettelnummer, KonstWillenserklaerungWeg.abstManuelleEingabe);
                    int identRaw = blAbstimmung.beendeSpeichernFuerMeldung();
                    einfuegenSOMMProtokoll(grpnStimmkartenManuellprotokoll, spFunktionSOMM, eingebeneNummer, "",
                            "Warnung", "", identRaw, "");
                    clearSOMMNurEingabeFelder();
                } else {
                    starteMarkierungLesenMBEM(identifikationsNummer, eingebeneNummer, blNummernformen.rcKartenklasse,
                            null, rcEK, abgegebenerStimmzettel, gewaehlteStimmart, gewaehlteGattung);
                }
                lDbBundle.closeAll();
                return;
            }
            meldungsIdent = rcEK;
        }

        EclMeldung eclMeldung = new EclMeldung();
        eclMeldung.meldungsIdent = meldungsIdent;
        lDbBundle.dbMeldungen.leseZuMeldungsIdent(eclMeldung);
        eclMeldung = lDbBundle.dbMeldungen.meldungenArray[0];
        gewaehlteGattung = eclMeldung.gattung;

        System.out.println("CtrlAbstimmungAblauf A");

        if (stimmSchnispelEinlesen) {

            int abstimmungInitRC = blAbstimmung.starteSpeichernFuerMeldung(eclMeldung, 0, false,
                    KonstWillenserklaerungWeg.abstManuelleEingabe, blNummernformen.rcKartenklasse,
                    identifikationsNummer, 1);
            int abstimmungsMarkierungRC = blAbstimmung.setzeMarkierungZuAbstimmungsPosition(abgegebeneStimmart,
                    rcStimmzettelnummer, KonstWillenserklaerungWeg.abstManuelleEingabe);
            int identRaw = blAbstimmung.beendeSpeichernFuerMeldung();

            String quittungsText = eingebeneNummer + " OK ";
            String protokollQuittung = KonstStimmart.getText(abgegebeneStimmart);
            if (abstimmungInitRC == CaFehler.pmInSammelkarteEnthalten) {
                quittungsText = eingebeneNummer
                        + " Warnung: Meldung ist in Sammelkarte enthalten - voraussichtlich keine Verarbeitung!";
                protokollQuittung = "Warnung";
            }
            if (abstimmungInitRC == CaFehler.pmNichtPraesent) {
                quittungsText = eingebeneNummer
                        + " Warnung: Meldung ist nicht präsent - voraussichtlich keine Verarbeitung!";
                protokollQuittung = "Warnung";
            }
            if (abstimmungsMarkierungRC == CaFehler.pmDoppeltNunUngueltig) {
                quittungsText = eingebeneNummer + " Warnung: unterschiedliche Stimmarten erfaßt - nun ungültig!";
                protokollQuittung = "Warnung";
            }
            if (abstimmungsMarkierungRC == CaFehler.pmGattungNichtStimmberechtigt) {
                quittungsText = eingebeneNummer
                        + " Warnung: Gattung nicht stimmberechtigt - voraussichtlich keine Verarbeitung!";
                protokollQuittung = "Warnung";
            }
            zeigeSOMMBestaetigungOK(quittungsText);

            einfuegenSOMMProtokoll(grpnStimmkartenManuellprotokoll, spFunktionSOMM, eingebeneNummer,
                    Integer.toString(abgegebenerStimmzettel), protokollQuittung, Long.toString(eclMeldung.stimmen),
                    identRaw, "");
            clearSOMMNurEingabeFelder();
        } else {
            int abstimmungInitRC = blAbstimmung.starteSpeichernFuerMeldung(eclMeldung, 0, false,
                    KonstWillenserklaerungWeg.abstManuelleEingabe, blNummernformen.rcKartenklasse,
                    identifikationsNummer, 1);

            String quittungsText = eingebeneNummer + " OK ";
            if (abstimmungInitRC == CaFehler.pmInSammelkarteEnthalten) {
                quittungsText = eingebeneNummer
                        + " Warnung: Meldung ist in Sammelkarte enthalten - voraussichtlich keine Verarbeitung!";
            }
            if (abstimmungInitRC == CaFehler.pmNichtPraesent) {
                quittungsText = eingebeneNummer
                        + " Warnung: Meldung ist nicht präsent - voraussichtlich keine Verarbeitung!";
            }
            zeigeSOMMBestaetigungOK(quittungsText);

            starteMarkierungLesenMBEM(identifikationsNummer, eingebeneNummer, blNummernformen.rcKartenklasse,
                    eclMeldung, abstimmungInitRC, abgegebenerStimmzettel, gewaehlteStimmart, gewaehlteGattung);
        }

        lDbBundle.closeAll();

    }

    /**
     * Stornieren.
     */
    private void stornierenSOMM() {
        if (protokollNummerSOMM == null) {
            return;
        }
        int anzInProtokoll = protokollNummerSOMM.size();
        if (anzInProtokoll == 0) {
            return;
        }
        int zuPruefenderProtokollsatz = anzInProtokoll; /*Zum "Durchrennen" von oben her*/
        int zuStornierenderProtokollsatz = -1; /*Gefundener Protokollsatz, der dann storniert werden kann*/

        while (zuPruefenderProtokollsatz > 0 && zuStornierenderProtokollsatz == -1) {
            if (protokollRawIdentSOMM.get(zuPruefenderProtokollsatz - 1) >= 1) {
                if (protokollStornoSOMM.get(zuPruefenderProtokollsatz - 1).compareTo("J") != 0) {
                    zuStornierenderProtokollsatz = zuPruefenderProtokollsatz;
                }
            }
            zuPruefenderProtokollsatz--;
        }

        if (zuStornierenderProtokollsatz == -1) {
            return;
        } /*Keinen gefunden*/
        protokollStornoSOMM.set(zuStornierenderProtokollsatz - 1, "J");

        Label lUber4 = new Label("J");
        grpnStimmkartenManuellprotokoll.add(lUber4, 4, zuStornierenderProtokollsatz);

        int rawIdent = protokollRawIdentSOMM.get(zuStornierenderProtokollsatz - 1);
        lDbBundle.openAll();
        lDbBundle.dbAbstimmungMeldungRaw.lese_RawIdent(rawIdent);
        EclAbstimmungMeldungRaw lAbstimmungMeldungRaw = lDbBundle.dbAbstimmungMeldungRaw.ergebnisGefunden(0);
        lAbstimmungMeldungRaw.wurdeStorniert = 1;
        lDbBundle.dbAbstimmungMeldungRaw.updateStorno(lAbstimmungMeldungRaw);
        int meldeIdent = lAbstimmungMeldungRaw.meldungsIdent;
        if (meldeIdent != 0) {
            EclMeldung lMeldung = new EclMeldung();
            lMeldung.meldungsIdent = meldeIdent;
            lDbBundle.dbMeldungen.leseZuMeldungsIdent(lMeldung);
            lMeldung = lDbBundle.dbMeldungen.meldungenArray[0];
            blAbstimmung.ermittleAktuelleAbstimmungNeu(lMeldung);
        }

        lDbBundle.closeAll();

    }

    /**
     * **************Aktionen*****************.
     *
     * @param event the event
     */

    @FXML
    void onBtnFunktionSOMMFertig(ActionEvent event) {
        erledigtAblaufAuswahl();
        resetAblaufAuswahl();

    }

    /**
     * On btn funktion SOMM felder leeren.
     *
     * @param event the event
     */
    @FXML
    void onBtnFunktionSOMMFelderLeeren(ActionEvent event) {
        clearSOMMEingabeFelder();
        tfFunktionSOMMEingabeNummer.requestFocus();

    }

    /**
     * On btn funktion SOMM zaehler zuruecksetzen.
     *
     * @param event the event
     */
    @FXML
    void onBtnFunktionSOMMZaehlerZuruecksetzen(ActionEvent event) {
        anzGeleseneSchnipsel = 0;
        zeigeAnzGeleseneSchnipsel();
        initSOMMProtokoll();
        tfFunktionSOMMEingabeNummer.requestFocus();

    }

    /**
     * On key funktion SOMM eingabe nummer.
     *
     * @param event the event
     */
    @FXML
    void onKeyFunktionSOMMEingabeNummer(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            ausloeserFunktionSOMM = 1;
            verarbeitenFunktionSOMM();
        }

    }

    /**
     * On key funktion SOMM stimmart.
     *
     * @param event the event
     */
    @FXML
    void onKeyFunktionSOMMStimmart(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            ausloeserFunktionSOMM = 2;
            verarbeitenFunktionSOMM();
        }

    }

    /**
     * On key funktion SOMM stimmzettel.
     *
     * @param event the event
     */
    @FXML
    void onKeyFunktionSOMMStimmzettel(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            ausloeserFunktionSOMM = 3;
            verarbeitenFunktionSOMM();
        }

    }

    /**
     * On btn funktion SOMM buchen.
     *
     * @param event the event
     */
    @FXML
    void onBtnFunktionSOMMBuchen(ActionEvent event) {
        ausloeserFunktionSOMM = 4;
        verarbeitenFunktionSOMM();

    }

    /**
     * On btn funktion SOMM letzten stornieren.
     *
     * @param event the event
     */
    @FXML
    void onBtnFunktionSOMMLetztenStornieren(ActionEvent event) {
        stornierenSOMM();
    }

    /**
     * ***************Anzeige-Funktionen*************************.
     */

    private void clearSOMMNurEingabeFelder() {
        if (stimmSchnispelEinlesen) {
            tfFunktionSOMMEingabeNummer.setText("");
            tfFunktionSOMMStimmzettel.setText("");
            tfFunktionSOMMStimmart.setText("");
            tfFunktionSOMMEingabeNummer.requestFocus();
        } else {
            tfMBEMEingabeNummer.setText("");
            tfMBEMStimmzettel.setText("");
            tfMBEMStapelStimmart.setText("");
            grpnMBEMMarkierungseingabe = new GridPane();
            spMBEMEingabeMarkierungen.setContent(grpnMBEMMarkierungseingabe);
            tfMBEMEingabeNummer.requestFocus();
        }
    }

    /**
     * Clear SOMM eingabe felder.
     */
    private void clearSOMMEingabeFelder() {
        clearSOMMNurEingabeFelder();
        clearSOMMBestaetigungsFeld();
    }

    /**
     * Zeige anz gelesene schnipsel.
     */
    private void zeigeAnzGeleseneSchnipsel() {
        if (stimmSchnispelEinlesen) {
            tfFunktionSOMMAnzahlGelKarten.setText(Integer.toString(anzGeleseneSchnipsel));
        } else {
            tfMBEMAnzahlGelKarten.setText(Integer.toString(anzGeleseneSchnipsel));
        }
    }

    /** The farbcodes OK. */
    /*+++++++++++Verwaltung Bestätigungsfeld*******************/
    private String farbcodesOK[] = { "#98F5FF", "#66CDAA", "#CAFF70" };

    /** The verwendeter farbcode OK. */
    private int verwendeterFarbcodeOK = 0;

    /** The farbcodes fehler. */
    private String farbcodesFehler[] = { "#FFC1C1", "#F4A460", "#FF4040" };

    /** The verwendeter farbcode fehler. */
    private int verwendeterFarbcodeFehler = 0;

    /**
     * Clear SOMM bestaetigungs feld.
     */
    private void clearSOMMBestaetigungsFeld() {
        TextField lTF = null;
        if (stimmSchnispelEinlesen) {
            lTF = tfFunktionSOMMBestaetigung;
        } else {
            lTF = tfMBEMBestaetigung;
        }
        lTF.setText("");
        lTF.setStyle("-fx-background-color: #A7A7A7");
        verwendeterFarbcodeOK = 0;
        verwendeterFarbcodeFehler = 0;
    }

    /**
     * Zeige SOMM fehlermeldung.
     *
     * @param pFehlermeldung the fehlermeldung
     */
    private void zeigeSOMMFehlermeldung(String pFehlermeldung) {
        TextField lTF = null;
        if (stimmSchnispelEinlesen) {
            lTF = tfFunktionSOMMBestaetigung;
        } else {
            lTF = tfMBEMBestaetigung;
        }
        lTF.setText(pFehlermeldung);
        lTF.setStyle("-fx-background-color: " + farbcodesFehler[verwendeterFarbcodeFehler]);
        verwendeterFarbcodeFehler++;
        if (verwendeterFarbcodeFehler >= farbcodesFehler.length) {
            verwendeterFarbcodeFehler = 0;
        }
    }

    /**
     * Zeige SOMM bestaetigung OK.
     *
     * @param pBestaetigungstext the bestaetigungstext
     */
    private void zeigeSOMMBestaetigungOK(String pBestaetigungstext) {
        TextField lTF = null;
        if (stimmSchnispelEinlesen) {
            lTF = tfFunktionSOMMBestaetigung;
        } else {
            lTF = tfMBEMBestaetigung;
        }
        lTF.setText(pBestaetigungstext);
        lTF.setStyle("-fx-background-color: " + farbcodesOK[verwendeterFarbcodeOK]);
        verwendeterFarbcodeOK++;
        if (verwendeterFarbcodeOK >= farbcodesOK.length) {
            verwendeterFarbcodeOK = 0;
        }
    }

    /** The protokoll nummer SOMM. */
    /*+++++++++++Verwalten Protokollfeld+++++++++++++++++++++++++*/
    private ArrayList<String> protokollNummerSOMM = null;

    /** The protokoll zt nr SOMM. */
    private ArrayList<String> protokollZtNrSOMM = null;

    /** The protokoll stimm art SOMM. */
    private ArrayList<String> protokollStimmArtSOMM = null;

    /** The protokoll anz stimmen SOMM. */
    private ArrayList<String> protokollAnzStimmenSOMM = null;

    /** The protokoll storno SOMM. */
    private ArrayList<String> protokollStornoSOMM = null;

    /** The protokoll raw ident SOMM. */
    private ArrayList<Integer> protokollRawIdentSOMM = null;

    /**
     * Inits the SOMM protokoll.
     */
    private void initSOMMProtokoll() {
        protokollNummerSOMM = new ArrayList<String>();
        protokollZtNrSOMM = new ArrayList<String>();
        protokollStimmArtSOMM = new ArrayList<String>();
        protokollAnzStimmenSOMM = new ArrayList<String>();
        protokollStornoSOMM = new ArrayList<String>();
        protokollRawIdentSOMM = new ArrayList<Integer>();

        grpnStimmkartenManuellprotokoll = new GridPane();
        if (stimmSchnispelEinlesen) {
            zeigeUeberschriftSOMMProtokoll(grpnStimmkartenManuellprotokoll, spFunktionSOMM);
        } else {
            zeigeUeberschriftSOMMProtokoll(grpnStimmkartenManuellprotokoll, spMBEMProtokoll);
        }

    }

    /**
     * pGridPan muß vorher initalisiert sein!.
     *
     * @param pGridPan    the grid pan
     * @param pScrollPane the scroll pane
     */
    private void zeigeUeberschriftSOMMProtokoll(GridPane pGridPan, ScrollPane pScrollPane) {

        pGridPan.setVgap(5);
        pGridPan.setHgap(15);

        Label lUber0 = new Label("Nummer");
        pGridPan.add(lUber0, 0, 0);

        Label lUber1 = new Label("ZtlNr");
        pGridPan.add(lUber1, 1, 0);

        Label lUber2 = new Label("StimmArt");
        pGridPan.add(lUber2, 2, 0);

        Label lUber3 = new Label("AnzStimmen");
        pGridPan.add(lUber3, 3, 0);

        Label lUber4 = new Label("Storno");
        pGridPan.add(lUber4, 4, 0);

        pScrollPane.setContent(pGridPan);

    }

    /**
     * pScrollPane = null, d.h. es erfolgt kein Scroll nach unten, und die Anzahl
     * wird nicht angezeigt, und es wird nicht in Pufferspeicher aufgenommen!
     *
     * @param pGridPane   the grid pane
     * @param pScrollPane the scroll pane
     * @param pNummer     the nummer
     * @param pZtNr       the zt nr
     * @param pStimmArt   the stimm art
     * @param pAnzStimmen the anz stimmen
     * @param pRawIdent   the raw ident
     * @param pStorno     the storno
     */
    private void einfuegenSOMMProtokoll(GridPane pGridPane, ScrollPane pScrollPane, String pNummer, String pZtNr,
            String pStimmArt, String pAnzStimmen, int pRawIdent, String pStorno) {
        anzGeleseneSchnipsel++;
        zeigeAnzGeleseneSchnipsel();

        System.out.println("einfuegenSOMMProtokoll");
        if (pScrollPane != null) {
            protokollNummerSOMM.add(pNummer);
        }
        Label lUber0 = new Label(pNummer);
        pGridPane.add(lUber0, 0, anzGeleseneSchnipsel);

        if (pScrollPane != null) {
            protokollZtNrSOMM.add(pZtNr);
        }
        Label lUber1 = new Label(pZtNr);
        pGridPane.add(lUber1, 1, anzGeleseneSchnipsel);

        if (pScrollPane != null) {
            protokollStimmArtSOMM.add(pStimmArt);
        }
        Label lUber2 = new Label(pStimmArt);
        pGridPane.add(lUber2, 2, anzGeleseneSchnipsel);

        if (pScrollPane != null) {
            protokollAnzStimmenSOMM.add(pAnzStimmen);
        }
        Label lUber3 = new Label(pAnzStimmen);
        pGridPane.add(lUber3, 3, anzGeleseneSchnipsel);

        if (pScrollPane != null) {
            protokollStornoSOMM.add(pStorno);
        }
        Label lUber4 = new Label(pStorno);
        pGridPane.add(lUber4, 4, anzGeleseneSchnipsel);

        if (pScrollPane != null) {
            protokollRawIdentSOMM.add(pRawIdent);
        }

        if (pScrollPane != null) {
            System.out.println("einfuegenSOMMProtokoll pScrollPane!=null");
            ScrollPane lScrollPane = null;
            if (stimmSchnispelEinlesen) {
                System.out.println("spFunktionSOMM");
                lScrollPane = spFunktionSOMM;
            } else {
                lScrollPane = spMBEMProtokoll;
                System.out.println("spMBEMProtokoll");
            }

            Label lUber99 = new Label("");
            pGridPane.add(lUber99, 0, anzGeleseneSchnipsel + 1);

            lScrollPane.setContent(grpnStimmkartenManuellprotokoll);

            Double vmaxProperty;
            vmaxProperty = pScrollPane.getVmax();
            lScrollPane.setVvalue(vmaxProperty);
        }

    }

    /************************************************************************
     * *****************stimmenMarkierungsBelegeEinlesenManuell**************
     * *****************stimmenMarkierungenEinlesenManuell*******************
     */

    /*****************Logik**********************/

    /**Einlesen eines Markierungsstimmbelegs*/
    private void ausfuehrenMBEMBelege() {

        lDbBundle.openAll();
        blAbstimmung = new BlAbstimmung(lDbBundle);
        blAbstimmung.setzeAktivenAbstimmungsblockSortierenNachStimmkarten();
        blAbstimmung.leseAktivenOderLokalenAbstimmungsblock(aktivDeaktivLokalAbstimmungsblock);
        blAbstimmung.leseAbstimmungsVorschlagDerGesellschaft();
        lDbBundle.closeAll();

        pnMBEM.setVisible(true);
        lblMBEMUeberschrift.setText("Markierungsbelege einlesen Manuell");

        clearSOMMEingabeFelder();
        zeigeAnzGeleseneSchnipsel();
        spFunktionSOMM.setContent(null);

        if (letzterErfassungsvorgang != KonstAbstimmungsAblauf.stimmenMarkierungsBelegeEinlesenManuell) {
            letzterErfassungsvorgang = KonstAbstimmungsAblauf.stimmenMarkierungsBelegeEinlesenManuell;
            anzGeleseneSchnipsel = 0;
            initSOMMProtokoll();

        }

        btnMBEMBuchen.setVisible(false);
        btnMEBMAbbruch.setVisible(false);

    }

    /**
     * Einlesen von Markierungen für einen gesamten Abstimmungsblock (also quasi wie
     * Tablet/App.
     */
    private void ausfuehrenMBEMBlock() {
        lDbBundle.openAll();
        blAbstimmung = new BlAbstimmung(lDbBundle);
        blAbstimmung.setzeAktivenAbstimmungsblockSortierenNachAufTable();
        blAbstimmung.leseAktivenOderLokalenAbstimmungsblock(aktivDeaktivLokalAbstimmungsblock);
        blAbstimmung.leseAbstimmungsVorschlagDerGesellschaft();
        lDbBundle.closeAll();

        pnMBEM.setVisible(true);
        lblMBEMUeberschrift.setText("Markierungen für Abstimmungsblock einlesen Manuell");

        clearSOMMEingabeFelder();
        zeigeAnzGeleseneSchnipsel();
        spFunktionSOMM.setContent(null);

        if (letzterErfassungsvorgang != KonstAbstimmungsAblauf.stimmenMarkierungenEinlesenManuell) {
            letzterErfassungsvorgang = KonstAbstimmungsAblauf.stimmenMarkierungenEinlesenManuell;
            anzGeleseneSchnipsel = 0;
            initSOMMProtokoll();

        }

        btnMBEMBuchen.setVisible(false);
        btnMEBMAbbruch.setVisible(false);

    }

    /** The identifikationsnummer MBEM. */
    /*Parameter von starteMarkierungLesenMBEM werden in den folgenden Variablen zwischengespeichert, um sie fürs Buchen
     * weiterverwenden zu können
     */
    private String identifikationsnummerMBEM = "";

    /** The eingebene nummer MBEM. */
    private String eingebeneNummerMBEM = "";

    /** The kartenklasse MBEM. */
    private int kartenklasseMBEM = 0;

    /** The meldung MBEM. */
    private EclMeldung meldungMBEM = null;

    /** The fehler MBEM. */
    private int fehlerMBEM = 0;

    /** The stimmzettel nr MBEM. */
    private int stimmzettelNrMBEM = 0;

    /** The vorbelegte stimmart MBEM. */
    private int vorbelegteStimmartMBEM = 0;

    /** The gewaehlte gattung MBEM. */
    private int gewaehlteGattungMBEM = 0;

    /**Markierungsmaske anzeigen und Eingabe vorbereiten/aktivieren
     * pIdentifikastionsnummer=gelesene Nummer (nur Identifikationsteil)
     * pKartenklasse = gelesene Kartenklassen
     * pMeldung = null => keiner Meldung zuordenbar
     * pFehler=1 => normale Verarbeitung; <1 bisher schon Fehler aufgetreten
     * pStimmzettelNr=>0 ausgewählter Stimmzettel
     * pVorbelegteStimmart = KonstKartenklasse-Wert, falls Stapelverarbeitung vorbelegt*/
    private void starteMarkierungLesenMBEM(String pIdentifikationsnummer, String pEingegebeneNummer, int pKartenklasse,
            EclMeldung pMeldung, int pFehler, int pStimmzettelNr, int pVorbelegteStimmart, int pGewaehlteGattung) {

        /*Parameter "retten" fürs Verbuchen*/
        identifikationsnummerMBEM = pIdentifikationsnummer;
        eingebeneNummerMBEM = pEingegebeneNummer;
        kartenklasseMBEM = pKartenklasse;
        meldungMBEM = pMeldung;
        fehlerMBEM = pFehler;
        stimmzettelNrMBEM = pStimmzettelNr;
        System.out.println("pStimmzettelNr=" + pStimmzettelNr);
        vorbelegteStimmartMBEM = pVorbelegteStimmart;
        gewaehlteGattungMBEM = pGewaehlteGattung;
        System.out.println("gewaehlteGattungMBEM=" + gewaehlteGattungMBEM);
        /*Die einzugebenden Abstimmungspunkte aufbereiten*/
        if (ausgewaehlteAblaufFunktion == KonstAbstimmungsAblauf.stimmenMarkierungsBelegeEinlesenManuell) {
            blAbstimmung.selektiereAbstimmungenFuerErfassung(pStimmzettelNr, pGewaehlteGattung);
        } else {
            System.out.println("pGewaehlteGattung=" + pGewaehlteGattung);
            blAbstimmung.selektiereAbstimmungenFuerErfassung(0, pGewaehlteGattung);
        }

        int anzahlAnzuzeigenderAbstimmungen = blAbstimmung.getAnzAbstimmungenInSelektiertenAbstimmungen();
        System.out.println("anzahlAnzuzeigenderAbstimmungen=" + anzahlAnzuzeigenderAbstimmungen);

        /*0 => kein Vorbelegen (EclMeldung nicht vorhanden, oder noch nichts eingelesen)
         * >0 => alle vorbelegen gemäß pVorbelegteStimmart
         *-1 => Vorbelegen gemäß bereits vorhandener Abstimmung von Eclmeldung
         */
        int vorbelegen = 0;
        if (pVorbelegteStimmart > 0) {
            vorbelegen = pVorbelegteStimmart;
        } else {
            if (pMeldung != null) {
                boolean abstimmungenVorhanden = blAbstimmung
                        .liefereAktuelleAbstimmungZuMeldeIdent(pMeldung.meldungsIdent);
                if (abstimmungenVorhanden) {
                    vorbelegen = -1;
                }
            }
        }
        markierteStimmart = new int[anzahlAnzuzeigenderAbstimmungen];
        for (int i = 0; i < anzahlAnzuzeigenderAbstimmungen; i++) {
            markierteStimmart[i] = 0;
            if (!blAbstimmung.istUeberschriftSelektierteAbstimmung(i)) {
                if (vorbelegen > 0) {
                    markierteStimmart[i] = vorbelegen;
                }
                if (vorbelegen < 0) {
                    markierteStimmart[i] = blAbstimmung.liefereAktuelleMarkierungZuSelektierterAbstimmungsPosition(i,
                            blAbstimmung.bisherigesAbstimmverhalten);
                }
            }
        }

        /*Eingabe-Grid füllen*/
        grpnMBEMMarkierungseingabe = new GridPane();
        grpnMBEMMarkierungseingabe.setVgap(5);
        grpnMBEMMarkierungseingabe.setHgap(15);

        btnAlleJa = new Button("A J");
        btnAlleJa.setOnAction(e -> {
            clickedStimmartMBEM(e);
        });
        btnAlleNein = new Button("A N");
        btnAlleNein.setOnAction(e -> {
            clickedStimmartMBEM(e);
        });
        btnAlleEnthaltung = new Button("A E");
        btnAlleEnthaltung.setOnAction(e -> {
            clickedStimmartMBEM(e);
        });
        btnAlleUngueltig = new Button("A U");
        btnAlleUngueltig.setOnAction(e -> {
            clickedStimmartMBEM(e);
        });
        btnAlleNichtTeilnahme = new Button("A NT");
        btnAlleNichtTeilnahme.setOnAction(e -> {
            clickedStimmartMBEM(e);
        });
        btnAlleNichtMarkiert = new Button("A N.M.");
        btnAlleNichtMarkiert.setOnAction(e -> {
            clickedStimmartMBEM(e);
        });

        grpnMBEMMarkierungseingabe.add(btnAlleJa, 0, 0);
        grpnMBEMMarkierungseingabe.add(btnAlleNein, 1, 0);
        grpnMBEMMarkierungseingabe.add(btnAlleEnthaltung, 2, 0);
        grpnMBEMMarkierungseingabe.add(btnAlleUngueltig, 3, 0);
        grpnMBEMMarkierungseingabe.add(btnAlleNichtTeilnahme, 4, 0);
        grpnMBEMMarkierungseingabe.add(btnAlleNichtMarkiert, 5, 0);

        btnAlleImSinne = new Button("A im S.V.");
        btnAlleImSinne.setOnAction(e -> {
            clickedStimmartMBEM(e);
        });
        btnAlleGegenSinne = new Button("A gegen V.");
        btnAlleGegenSinne.setOnAction(e -> {
            clickedStimmartMBEM(e);
        });

        if (blAbstimmung.abstimmungsVorschlagDerGesellschaft != null) {
            HBox hBox = new HBox();
            hBox.getChildren().add(btnAlleImSinne);
            hBox.getChildren().add(btnAlleGegenSinne);
            grpnMBEMMarkierungseingabe.add(hBox, 8, 0);
        }

        btnJa = new Button[anzahlAnzuzeigenderAbstimmungen];
        btnNein = new Button[anzahlAnzuzeigenderAbstimmungen];
        btnEnthaltung = new Button[anzahlAnzuzeigenderAbstimmungen];
        btnUngueltig = new Button[anzahlAnzuzeigenderAbstimmungen];
        btnNichtTeilnahme = new Button[anzahlAnzuzeigenderAbstimmungen];
        btnNichtMarkiert = new Button[anzahlAnzuzeigenderAbstimmungen];

        for (int i1 = 0; i1 < anzahlAnzuzeigenderAbstimmungen; i1++) {
            btnJa[i1] = new Button("J");
            btnJa[i1].setOnAction(e -> {
                clickedStimmartMBEM(e);
            });
            btnNein[i1] = new Button("N");
            btnNein[i1].setOnAction(e -> {
                clickedStimmartMBEM(e);
            });
            btnEnthaltung[i1] = new Button("E");
            btnEnthaltung[i1].setOnAction(e -> {
                clickedStimmartMBEM(e);
            });
            btnUngueltig[i1] = new Button("U");
            btnUngueltig[i1].setOnAction(e -> {
                clickedStimmartMBEM(e);
            });
            btnNichtTeilnahme[i1] = new Button("NT");
            btnNichtTeilnahme[i1].setOnAction(e -> {
                clickedStimmartMBEM(e);
            });
            btnNichtMarkiert[i1] = new Button("N.M.");
            btnNichtMarkiert[i1].setOnAction(e -> {
                clickedStimmartMBEM(e);
            });

            if (!blAbstimmung.istUeberschriftSelektierteAbstimmung(i1)) {
                /*5 Einträge frei lassen für J/N/E/U/NT*/
                if (blAbstimmung.selektierteAbstimmungen[i1].internJa == 1) {
                    grpnMBEMMarkierungseingabe.add(btnJa[i1], 0, i1 + 1);
                }
                if (blAbstimmung.selektierteAbstimmungen[i1].internNein == 1) {
                    grpnMBEMMarkierungseingabe.add(btnNein[i1], 1, i1 + 1);
                }
                if (blAbstimmung.selektierteAbstimmungen[i1].internEnthaltung == 1) {
                    grpnMBEMMarkierungseingabe.add(btnEnthaltung[i1], 2, i1 + 1);
                }
                if (blAbstimmung.selektierteAbstimmungen[i1].internUngueltig == 1) {
                    grpnMBEMMarkierungseingabe.add(btnUngueltig[i1], 3, i1 + 1);
                }
                if (blAbstimmung.selektierteAbstimmungen[i1].internNichtTeilnahme == 1) {
                    grpnMBEMMarkierungseingabe.add(btnNichtTeilnahme[i1], 4, i1 + 1);
                }
                grpnMBEMMarkierungseingabe.add(btnNichtMarkiert[i1], 5, i1 + 1);
            }

            Label lTop = new Label(blAbstimmung.selektierteAbstimmungen[i1].nummer);
            grpnMBEMMarkierungseingabe.add(lTop, 6, i1 + 1);

            Label lTopIndex = new Label(blAbstimmung.selektierteAbstimmungen[i1].nummerindex);
            grpnMBEMMarkierungseingabe.add(lTopIndex, 7, i1 + 1);

            Label lTopText = new Label(blAbstimmung.selektierteAbstimmungen[i1].kurzBezeichnung);
            grpnMBEMMarkierungseingabe.add(lTopText, 8, i1 + 1);

        }
        aktiviereButtonsMBEM();

        spMBEMEingabeMarkierungen.setContent(grpnMBEMMarkierungseingabe);

    }

    /**
     * Buchen markierungen MBEM.
     */
    /*Buchen ausführen nach Markierungseingabe*/
    private void buchenMarkierungenMBEM() {

        lDbBundle.openAll();
        int anzahlAnzuzeigenderAbstimmungen = blAbstimmung.getAnzAbstimmungenInSelektiertenAbstimmungen();

        /*1. Schritt: Aufbereiten der Markierungen - insbesondere alle auf "unmarkiert" setzen, die z.B. durch Gesamtmarkierung etc.
         * eine Stimmart markiert haben, die für diese Abstimmung nicht zulässig sind
         */

        for (int i = 0; i < anzahlAnzuzeigenderAbstimmungen; i++) {
            if (markierteStimmart[i] != 0) {
                if (blAbstimmung.selektierteAbstimmungen[i].internJa != 1 && markierteStimmart[i] == KonstStimmart.ja) {
                    markierteStimmart[i] = KonstStimmart.nichtMarkiert;
                }
                if (blAbstimmung.selektierteAbstimmungen[i].internNein != 1
                        && markierteStimmart[i] == KonstStimmart.nein) {
                    markierteStimmart[i] = KonstStimmart.nichtMarkiert;
                }
                if (blAbstimmung.selektierteAbstimmungen[i].internEnthaltung != 1
                        && markierteStimmart[i] == KonstStimmart.enthaltung) {
                    markierteStimmart[i] = KonstStimmart.nichtMarkiert;
                }
                if (blAbstimmung.selektierteAbstimmungen[i].internUngueltig != 1
                        && markierteStimmart[i] == KonstStimmart.ungueltig) {
                    markierteStimmart[i] = KonstStimmart.nichtMarkiert;
                }
                if (blAbstimmung.selektierteAbstimmungen[i].internNichtTeilnahme != 1
                        && markierteStimmart[i] == KonstStimmart.nichtTeilnahme) {
                    markierteStimmart[i] = KonstStimmart.nichtMarkiert;
                }
            }
        }

        if (meldungMBEM == null || fehlerMBEM < 1) {
            blAbstimmung.starteSpeichernFuerMeldung(null, 0, false, KonstWillenserklaerungWeg.abstManuelleEingabe,
                    kartenklasseMBEM, identifikationsnummerMBEM, fehlerMBEM);
            for (int i = 0; i < anzahlAnzuzeigenderAbstimmungen; i++) {
                if (!blAbstimmung.istUeberschriftSelektierteAbstimmung(i)) {
                    blAbstimmung.setzeMarkierungZuAbstimmungsIdent(markierteStimmart[i],
                            blAbstimmung.selektierteAbstimmungen[i].ident, KonstWillenserklaerungWeg.abstManuelleEingabe);
                }
            }
            int identRaw = blAbstimmung.beendeSpeichernFuerMeldung();
            einfuegenSOMMProtokoll(grpnStimmkartenManuellprotokoll, spMBEMProtokoll, eingebeneNummerMBEM, "", "Warnung",
                    "", identRaw, "");
            clearSOMMNurEingabeFelder();
        } else {
            int abstimmungInitRC = blAbstimmung.starteSpeichernFuerMeldung(meldungMBEM, 0, false,
                    KonstWillenserklaerungWeg.abstManuelleEingabe, kartenklasseMBEM, identifikationsnummerMBEM, 1);
            for (int i = 0; i < anzahlAnzuzeigenderAbstimmungen; i++) {
                if (!blAbstimmung.istUeberschriftSelektierteAbstimmung(i)) {
                    blAbstimmung.setzeMarkierungZuAbstimmungsIdent(markierteStimmart[i],
                            blAbstimmung.selektierteAbstimmungen[i].ident, KonstWillenserklaerungWeg.abstManuelleEingabe);
                }
            }
            int identRaw = blAbstimmung.beendeSpeichernFuerMeldung();

            String quittungsText = eingebeneNummerMBEM + " OK ";
            String protokollQuittung = "M";
            if (abstimmungInitRC == CaFehler.pmInSammelkarteEnthalten) {
                quittungsText = eingebeneNummerMBEM
                        + " Warnung: Meldung ist in Sammelkarte enthalten - voraussichtlich keine Verarbeitung!";
                protokollQuittung = "Warnung";
            }
            if (abstimmungInitRC == CaFehler.pmNichtPraesent) {
                quittungsText = eingebeneNummerMBEM
                        + " Warnung: Meldung ist nicht präsent - voraussichtlich keine Verarbeitung!";
                protokollQuittung = "Warnung";
            }
            zeigeSOMMBestaetigungOK(quittungsText);

            einfuegenSOMMProtokoll(grpnStimmkartenManuellprotokoll, spMBEMProtokoll, eingebeneNummerMBEM,
                    Integer.toString(stimmzettelNrMBEM), protokollQuittung, Long.toString(meldungMBEM.stimmen),
                    identRaw, "");
            clearSOMMNurEingabeFelder();
        }
        lDbBundle.closeAll();
    }

    /**
     * Markiert die "Abstimmungsbottons" der Markierung gemäß markierteStimmart.
     */
    private void aktiviereButtonsMBEM() {

        btnAlleJa.setStyle("-fx-background-color: #A7A7A7");
        btnAlleNein.setStyle("-fx-background-color: #A7A7A7");
        btnAlleEnthaltung.setStyle("-fx-background-color: #A7A7A7");
        btnAlleUngueltig.setStyle("-fx-background-color: #A7A7A7");
        btnAlleNichtTeilnahme.setStyle("-fx-background-color: #A7A7A7");
        btnAlleNichtMarkiert.setStyle("-fx-background-color: #A7A7A7");
        btnAlleImSinne.setStyle("-fx-background-color: #A7A7A7");
        btnAlleGegenSinne.setStyle("-fx-background-color: #A7A7A7");

        for (int i = 0; i < markierteStimmart.length; i++) {
            btnJa[i].setStyle("-fx-background-color: #A7A7A7");
            btnNein[i].setStyle("-fx-background-color: #A7A7A7");
            btnEnthaltung[i].setStyle("-fx-background-color: #A7A7A7");
            btnUngueltig[i].setStyle("-fx-background-color: #A7A7A7");
            btnNichtTeilnahme[i].setStyle("-fx-background-color: #A7A7A7");
            btnNichtMarkiert[i].setStyle("-fx-background-color: #A7A7A7");
            switch (markierteStimmart[i]) {
            case KonstStimmart.ja:
                btnJa[i].setStyle("-fx-background-color: #00ff00");
                break;
            case KonstStimmart.nein:
                btnNein[i].setStyle("-fx-background-color: #ff0000");
                break;
            case KonstStimmart.enthaltung:
                btnEnthaltung[i].setStyle("-fx-background-color: #fcf800");
                break;
            case KonstStimmart.ungueltig:
                btnUngueltig[i].setStyle("-fx-background-color: #98F5FF");
                break;
            case KonstStimmart.nichtTeilnahme:
                btnNichtTeilnahme[i].setStyle("-fx-background-color: #98F5FF");
                break;
            }
        }

        aktiviereButtonsFuerMarkierung();
    }

    /**
     * **************Aktionen*****************.
     *
     * @param event the event
     */

    @FXML
    void onBtnMBEMFelderLeeren(ActionEvent event) {
        clearSOMMEingabeFelder();
        tfMBEMEingabeNummer.requestFocus();
    }

    /**
     * On btn MBEM fertig.
     *
     * @param event the event
     */
    @FXML
    void onBtnMBEMFertig(ActionEvent event) {
        erledigtAblaufAuswahl();
        resetAblaufAuswahl();
    }

    /**
     * On btn MBEM lesen.
     *
     * @param event the event
     */
    @FXML
    void onBtnMBEMLesen(ActionEvent event) {
        ausloeserFunktionSOMM = 4;
        verarbeitenFunktionSOMM();
    }

    /**
     * On btn MBEM letzten stornieren.
     *
     * @param event the event
     */
    @FXML
    void onBtnMBEMLetztenStornieren(ActionEvent event) {
        stornierenSOMM();
    }

    /**
     * On btn MBEM zaehler zuruecksetzen.
     *
     * @param event the event
     */
    @FXML
    void onBtnMBEMZaehlerZuruecksetzen(ActionEvent event) {
        anzGeleseneSchnipsel = 0;
        zeigeAnzGeleseneSchnipsel();
        initSOMMProtokoll();
        tfMBEMEingabeNummer.requestFocus();
    }

    /**
     * On btn MBEM abbruch.
     *
     * @param event the event
     */
    @FXML
    void onBtnMBEMAbbruch(ActionEvent event) {
        clearSOMMEingabeFelder();
        tfMBEMEingabeNummer.requestFocus();
        deaktiviereButtonsFuerMarkierung();
    }

    /**
     * On key MBEM eingabe nummer.
     *
     * @param event the event
     */
    @FXML
    void onKeyMBEMEingabeNummer(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            ausloeserFunktionSOMM = 1;
            verarbeitenFunktionSOMM();
        }

    }

    /**
     * On key MBEM stapel stimmart.
     *
     * @param event the event
     */
    @FXML
    void onKeyMBEMStapelStimmart(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            ausloeserFunktionSOMM = 2;
            verarbeitenFunktionSOMM();
        }

    }

    /**
     * On key MBEM stimmzettel.
     *
     * @param event the event
     */
    @FXML
    void onKeyMBEMStimmzettel(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            ausloeserFunktionSOMM = 3;
            verarbeitenFunktionSOMM();
        }

    }

    /**
     * On btn MBEM buchen.
     *
     * @param event the event
     */
    @FXML
    void onBtnMBEMBuchen(ActionEvent event) {
        buchenMarkierungenMBEM();
        deaktiviereButtonsFuerMarkierung();
    }

    /**
     * Clicked stimmart MBEM.
     *
     * @param event the event
     */
    @FXML
    void clickedStimmartMBEM(ActionEvent event) {
        int anzahlAnzuzeigenderAbstimmungen = blAbstimmung.getAnzAbstimmungenInSelektiertenAbstimmungen();
        if (event.getSource() == btnAlleJa) {
            for (int i = 0; i < anzahlAnzuzeigenderAbstimmungen; i++) {
                markierteStimmart[i] = KonstStimmart.ja;
            }
        }
        if (event.getSource() == btnAlleNein) {
            for (int i = 0; i < anzahlAnzuzeigenderAbstimmungen; i++) {
                markierteStimmart[i] = KonstStimmart.nein;
            }
        }
        if (event.getSource() == btnAlleEnthaltung) {
            for (int i = 0; i < anzahlAnzuzeigenderAbstimmungen; i++) {
                markierteStimmart[i] = KonstStimmart.enthaltung;
            }
        }
        if (event.getSource() == btnAlleUngueltig) {
            for (int i = 0; i < anzahlAnzuzeigenderAbstimmungen; i++) {
                markierteStimmart[i] = KonstStimmart.ungueltig;
            }
        }
        if (event.getSource() == btnAlleNichtTeilnahme) {
            for (int i = 0; i < anzahlAnzuzeigenderAbstimmungen; i++) {
                markierteStimmart[i] = KonstStimmart.nichtTeilnahme;
            }
        }
        if (event.getSource() == btnAlleNichtMarkiert) {
            for (int i = 0; i < anzahlAnzuzeigenderAbstimmungen; i++) {
                markierteStimmart[i] = KonstStimmart.nichtMarkiert;
            }
        }
        if (event.getSource() == btnAlleImSinne) {
            for (int i = 0; i < anzahlAnzuzeigenderAbstimmungen; i++) {
                int hPos = blAbstimmung.lieferePosInSelektierterWeisung(i);
                if (hPos != -1) {
                    markierteStimmart[i] = blAbstimmung.abstimmungsVorschlagDerGesellschaft.abgabe[hPos];
                }
            }
        }
        if (event.getSource() == btnAlleGegenSinne) {
            for (int i = 0; i < anzahlAnzuzeigenderAbstimmungen; i++) {
                int hPos = blAbstimmung.lieferePosInSelektierterWeisung(i);
                if (hPos != -1) {
                    int hStimmart = blAbstimmung.abstimmungsVorschlagDerGesellschaft.abgabe[hPos];
                    if (hStimmart == KonstStimmart.ja) {
                        hStimmart = KonstStimmart.nein;
                    } else {
                        if (hStimmart == KonstStimmart.nein) {
                            hStimmart = KonstStimmart.ja;
                        }
                    }
                    markierteStimmart[i] = hStimmart;
                }
            }
        }

        for (int i = 0; i < anzahlAnzuzeigenderAbstimmungen; i++) {
            if (event.getSource() == btnJa[i]) {
                markierteStimmart[i] = KonstStimmart.ja;
            }
            if (event.getSource() == btnNein[i]) {
                markierteStimmart[i] = KonstStimmart.nein;
            }
            if (event.getSource() == btnEnthaltung[i]) {
                markierteStimmart[i] = KonstStimmart.enthaltung;
            }
            if (event.getSource() == btnUngueltig[i]) {
                markierteStimmart[i] = KonstStimmart.ungueltig;
            }
            if (event.getSource() == btnNichtTeilnahme[i]) {
                markierteStimmart[i] = KonstStimmart.nichtTeilnahme;
            }
            if (event.getSource() == btnNichtMarkiert[i]) {
                markierteStimmart[i] = KonstStimmart.nichtMarkiert;
            }
        }

        aktiviereButtonsMBEM();

    }

    /**
     * ***************Anzeige-Funktionen*************************.
     */

    private void aktiviereButtonsFuerMarkierung() {
        btnMBEMBuchen.setVisible(true);
        btnMEBMAbbruch.setVisible(true);
        btnMBEMZaehlerZuruecksetzen.setVisible(false);
        btnMBEMLetztenStornieren.setVisible(false);
        btnMBEMLesen.setVisible(false);
        btnMBEMFelderLeeren.setVisible(false);

    }

    /**
     * Deaktiviere buttons fuer markierung.
     */
    private void deaktiviereButtonsFuerMarkierung() {
        btnMBEMBuchen.setVisible(false);
        btnMEBMAbbruch.setVisible(false);
        btnMBEMZaehlerZuruecksetzen.setVisible(true);
        btnMBEMLetztenStornieren.setVisible(true);
        btnMBEMLesen.setVisible(true);
        btnMBEMFelderLeeren.setVisible(true);
    }

    /**
     * *********************************************************************
     * ******************stimmenDetailPruefenStorno*************.
     */

    /***************** Logik **********************/

    private BlAbstimmung blDANAbstimmung = null;

    /** The array abstimmung meldung raw DAN. */
    private EclAbstimmungMeldungRaw[] arrayAbstimmungMeldungRawDAN = null;

    /** The anz array abstimmung meldung raw DAN. */
    private int anzArrayAbstimmungMeldungRawDAN = 0;

    /** The angezeigter detailsatz. */
    private int angezeigterDetailsatz = -1;

    /** The ecl meldung DAN. */
    private EclMeldung eclMeldungDAN = null;

    /** The eingebene nummer DAN. */
    private String eingebeneNummerDAN = "";

    /**
     * Do changed.
     *
     * @param alterWert the alter wert
     * @param neuerWert the neuer wert
     */
    private void doChanged(String alterWert, String neuerWert) {
        if (neuerWert == null || neuerWert.isEmpty()) {
            return;
        }

        if (neuerWert.compareTo("aktuelles Protokoll") == 0) {
            grpnDANProtokoll = new GridPane();
            zeigeUeberschriftSOMMProtokoll(grpnDANProtokoll, spDANProtokoll);
            anzGeleseneSchnipsel = 0; /*Zurücksetzen, da in protokollanzeige hochgezählt wird*/
            if (protokollNummerSOMM != null) {
                for (int i = 0; i < protokollNummerSOMM.size(); i++) {
                    einfuegenSOMMProtokoll(grpnDANProtokoll, null, protokollNummerSOMM.get(i),
                            protokollNummerSOMM.get(i), protokollStimmArtSOMM.get(i), protokollAnzStimmenSOMM.get(i),
                            protokollRawIdentSOMM.get(i), protokollStornoSOMM.get(i));
                }

            }

        } else {
            lDbBundle.openAll();
            lDbBundle.dbAbstimmungMeldungRaw.lese_allNichtVerarbeitet(zuVerarbeitenderAbstimmungsblock);
            grpnDANProtokoll = new GridPane();
            grpnDANProtokoll.setVgap(5);
            grpnDANProtokoll.setHgap(15);

            Label lUber0 = new Label("MeldungsIdent");
            grpnDANProtokoll.add(lUber0, 0, 0);

            Label lUber1 = new Label("Storniert");
            grpnDANProtokoll.add(lUber1, 1, 0);

            Label lUber1a = new Label("Fehler");
            grpnDANProtokoll.add(lUber1a, 2, 0);

            Label lUber2 = new Label("Erteilt auf Weg");
            grpnDANProtokoll.add(lUber2, 3, 0);

            Label lUber3 = new Label("Verwendete Nr");
            grpnDANProtokoll.add(lUber3, 4, 0);

            Label lUber4 = new Label("Arbeitsplatz");
            grpnDANProtokoll.add(lUber4, 5, 0);

            Label lUber5 = new Label("Zeit Stimmabgabe");
            grpnDANProtokoll.add(lUber5, 6, 0);

            Label lUber6 = new Label("Zeit Speichervorgang");
            grpnDANProtokoll.add(lUber6, 7, 0);

            Label lUber7 = new Label("Stimmen");
            grpnDANProtokoll.add(lUber7, 8, 0);

            Label lUber8 = new Label("EK");
            grpnDANProtokoll.add(lUber8, 9, 0);

            Label lUber9 = new Label("SK");
            grpnDANProtokoll.add(lUber9, 10, 0);

            Label lUber10 = new Label("Name");
            grpnDANProtokoll.add(lUber10, 11, 0);

            for (int i = 0; i < lDbBundle.dbAbstimmungMeldungRaw.anzErgebnisGefunden(); i++) {
                EclAbstimmungMeldungRaw lAbstimmungMeldungRaw = lDbBundle.dbAbstimmungMeldungRaw.ergebnisGefunden(i);
                EclMeldung lMeldung = new EclMeldung();
                if (lAbstimmungMeldungRaw.meldungsIdent != 0) {
                    lDbBundle.dbMeldungen.leseZuIdent(lAbstimmungMeldungRaw.meldungsIdent);
                    lMeldung = lDbBundle.dbMeldungen.meldungenArray[0];
                }

                Label lInhalt0 = new Label(Integer.toString(lAbstimmungMeldungRaw.meldungsIdent));
                grpnDANProtokoll.add(lInhalt0, 0, i + 1);

                if (lAbstimmungMeldungRaw.wurdeStorniert == 1) {
                    Label lInhalt1 = new Label("J");
                    grpnDANProtokoll.add(lInhalt1, 1, i + 1);
                }

                Label lInhalt1a = new Label(CaFehler.getFehlertext(lAbstimmungMeldungRaw.fehlercode, 0));
                grpnDANProtokoll.add(lInhalt1a, 2, i + 1);

                Label lInhalt2 = new Label(KonstWillenserklaerungWeg.getText(lAbstimmungMeldungRaw.erteiltAufWeg));
                grpnDANProtokoll.add(lInhalt2, 3, i + 1);

                Label lInhalt3 = new Label(lAbstimmungMeldungRaw.verwendeteNummer);
                grpnDANProtokoll.add(lInhalt3, 4, i + 1);

                Label lInhalt4 = new Label(Integer.toString(lAbstimmungMeldungRaw.arbeitsplatzNr));
                grpnDANProtokoll.add(lInhalt4, 5, i + 1);

                Label lInhalt5 = new Label(CaDatumZeit.DatumZeitStringFromLong(lAbstimmungMeldungRaw.zeitstempelraw));
                grpnDANProtokoll.add(lInhalt5, 6, i + 1);

                Label lInhalt6 = new Label(CaDatumZeit.DatumZeitStringFuerAnzeige(lAbstimmungMeldungRaw.zeitstempel));
                grpnDANProtokoll.add(lInhalt6, 7, i + 1);

                Label lInhalt7 = new Label(Long.toString(lMeldung.stueckAktien));
                grpnDANProtokoll.add(lInhalt7, 8, i + 1);

                Label lInhalt8 = new Label(lMeldung.zutrittsIdent);
                grpnDANProtokoll.add(lInhalt8, 9, i + 1);

                Label lInhalt9 = new Label(lMeldung.stimmkarte);
                grpnDANProtokoll.add(lInhalt9, 10, i + 1);

                Label lInhalt10 = new Label(lMeldung.name);
                grpnDANProtokoll.add(lInhalt10, 11, i + 1);

            }

            spDANProtokoll.setContent(grpnDANProtokoll);

            lDbBundle.closeAll();
        }

    }

    /**
     * Ausfuehren DAN.
     */
    private void ausfuehrenDAN() {

        pnFunktionDAN.setVisible(true);

        /*Combo-Box für Auswahl Anzeige Protokoll oder nicht-gewertete füllen*/
        cbDANAnzeige.valueProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(@SuppressWarnings("rawtypes") ObservableValue ov, String alterWert, String neuerWert) {
                doChanged(alterWert, neuerWert);

            }
        });
        cbDANAnzeige.getItems().removeAll(cbDANAnzeige.getItems());
        cbDANAnzeige.getItems().addAll("aktuelles Protokoll");
        cbDANAnzeige.getItems().addAll("nicht gewertete Stimmabgaben");
        cbDANAnzeige.setValue("aktuelles Protokoll");

        lDbBundle.openAll();
        blDANAbstimmung = new BlAbstimmung(lDbBundle);
        blDANAbstimmung.leseAktivenOderLokalenAbstimmungsblock(aktivDeaktivLokalAbstimmungsblock);
        lDbBundle.closeAll();

        /** Daten löschen/vorbereiten, Cursor positionieren */
        clearDANDaten();
        tfDANNummerZurAnaylse.setText("");
        tfDANNummerZurAnaylse.requestFocus();

    }

    /**
     * neuEingelesen=true => eingegebeNummerDAN wird neu aus Eingabefeld gelesen.
     *
     * @param neuEingelesen the neu eingelesen
     */
    private void nummerEingelesenDAN(boolean neuEingelesen) {
        if (neuEingelesen) {
            eingebeneNummerDAN = tfDANNummerZurAnaylse.getText().trim();
        }
        clearDANDaten();
        if (eingebeneNummerDAN.isEmpty()) {
            tfDANNummerZurAnaylse.setText("");
            tfDANNummerZurAnaylse.requestFocus();
            return;
        }

        /*Nummernform dekodieren*/
        BlNummernformen blNummernformen = new BlNummernformen(lDbBundle);
        int rc = blNummernformen.dekodiere(eingebeneNummerDAN, KonstKartenklasse.unbekannt);
        if (rc == CaFehler.pmNummernformUngueltig || rc == CaFehler.pmNummernformAktionsnummerUngueltig) {
            tfDANSammelkartenstatus.setText("Nummernform ungültig!");
            tfDANNummerZurAnaylse.setText("");
            tfDANNummerZurAnaylse.requestFocus();
            return;
        }
        if (rc == CaFehler.pfNichtEindeutig) {
            tfDANSammelkartenstatus.setText("Nummern nicht eindeutig - bitte vollständige Kodierung eingeben!");
            tfDANNummerZurAnaylse.setText("");
            tfDANNummerZurAnaylse.requestFocus();
            return;
        }
        if (rc == CaFehler.pmNummernformMandantUngueltig) {
            tfDANSammelkartenstatus.setText("Falsche Mandantennummer!");
            tfDANNummerZurAnaylse.setText("");
            tfDANNummerZurAnaylse.requestFocus();
            return;
        }
        if (rc == CaFehler.pfXyNichtImZulaessigenNummernkreis) {
            tfDANSammelkartenstatus.setText("Nummer nicht im zulässigen Nummernkreis!");
            tfDANNummerZurAnaylse.setText("");
            tfDANNummerZurAnaylse.requestFocus();
            return;
        }
        if (blNummernformen.rcKartenklasse == KonstKartenklasse.eintrittskartennummer
                && ParamS.param.paramAbstimmungParameter.beiAbstimmungEintrittskartennummerZulaessig != 1) {
            tfDANSammelkartenstatus.setText("Eintrittskarte nicht für Abstimmung verwendbar!");
            tfDANNummerZurAnaylse.setText("");
            tfDANNummerZurAnaylse.requestFocus();
            return;
        }
        if (blNummernformen.rcKartenklasse == KonstKartenklasse.gastkartennummer) {
            tfDANSammelkartenstatus.setText("Gastkarte nicht für Abstimmung verwendbar!");
            tfDANNummerZurAnaylse.setText("");
            tfDANNummerZurAnaylse.requestFocus();
            return;
        }

        /*Meldungs-Satz identifizieren / einlesen*/
        lDbBundle.openAll();
        int meldungsIdent = 0; /*Ident der Meldung, für die die Stimme abgegeben wird*/
        if (blNummernformen.rcKartenklasse == KonstKartenklasse.eintrittskartennummer) {
            EclZutrittsIdent lZutrittsIdent = new EclZutrittsIdent();
            lZutrittsIdent.zutrittsIdent = blNummernformen.rcIdentifikationsnummer.get(0);
            lZutrittsIdent.zutrittsIdentNeben = blNummernformen.rcIdentifikationsnummerNeben.get(0);
            lDbBundle.dbZutrittskarten.readAktionaer(lZutrittsIdent, 2);
            if (lDbBundle.dbZutrittskarten.anzErgebnis() == 0) {
                tfDANSammelkartenstatus.setText("Eintrittskartennummer nicht vorhanden!");
                tfDANNummerZurAnaylse.setText("");
                tfDANNummerZurAnaylse.requestFocus();
                lDbBundle.closeAll();
                return;
            }
            EclZutrittskarten lZutrittskarten = lDbBundle.dbZutrittskarten.ergebnisPosition(0);
            if (lZutrittskarten.zutrittsIdentIstFuerAbstimmungNichtGueltig_Delayed()) {
                tfDANSammelkartenstatus.setText("Eintrittskartennummer ist gesperrt!");
                tfDANNummerZurAnaylse.setText("");
                tfDANNummerZurAnaylse.requestFocus();
                lDbBundle.closeAll();
                return;
            }
            meldungsIdent = lZutrittskarten.meldungsIdentAktionaer;
        }
        if (blNummernformen.rcKartenklasse == KonstKartenklasse.stimmkartennummer) {
            lDbBundle.dbStimmkarten.read(blNummernformen.rcIdentifikationsnummer.get(0), 2);
            if (lDbBundle.dbStimmkarten.anzErgebnis() == 0) {
                tfDANSammelkartenstatus.setText("Stimmkartennummer nicht vorhanden!");
                tfDANNummerZurAnaylse.setText("");
                tfDANNummerZurAnaylse.requestFocus();
                lDbBundle.closeAll();
                return;
            }
            EclStimmkarten lStimmkarten = lDbBundle.dbStimmkarten.ergebnisPosition(0);
            if (lStimmkarten.stimmkarteIstGesperrt_Delayed == 1) {
                tfDANSammelkartenstatus.setText("Stimmkartennummer ist gesperrt!");
                tfDANNummerZurAnaylse.setText("");
                tfDANNummerZurAnaylse.requestFocus();
                lDbBundle.closeAll();
                return;
            }
            meldungsIdent = lStimmkarten.meldungsIdentAktionaer;
        }

        EclMeldung eclMeldung = new EclMeldung();
        eclMeldung.meldungsIdent = meldungsIdent;
        lDbBundle.dbMeldungen.leseZuMeldungsIdent(eclMeldung);
        eclMeldung = lDbBundle.dbMeldungen.meldungenArray[0];
        eclMeldungDAN = eclMeldung;

        tfDANInterneIdent.setText(Integer.toString(eclMeldung.meldungsIdent));

        switch (eclMeldung.meldungstyp) {
        case 1:
            tfDANSammelkartenstatus.setText("Meldung");
            break;
        case 2:
            tfDANSammelkartenstatus.setText("Sammelkarte");
            break;
        case 3:
            tfDANSammelkartenstatus.setText("in Sammelkarte");
            break;
        }

        tfDANName.setText(eclMeldung.name);
        tfDANVorname.setText(eclMeldung.vorname);
        tfDANOrt.setText(eclMeldung.ort);
        tfDANVertreterName.setText(eclMeldung.vertreterName);
        tfDANVertreterVorname.setText(eclMeldung.vertreterVorname);
        tfDANVertreterOrt.setText(eclMeldung.vertreterOrt);

        tfDANStimmen.setText(Long.toString(eclMeldung.stimmen));

        switch (eclMeldung.statusPraesenz_Delayed) {
        case 0:
            tfDANAnwesendDelayed.setText("nicht anwesend");
            break;
        case 1:
            tfDANAnwesendDelayed.setText("anwesend");
            break;
        case 2:
            tfDANAnwesendDelayed.setText("war anwesend");
            break;
        case 4:
            tfDANAnwesendDelayed.setText("anwesend in Sammelkarte");
            break;
        }

        switch (eclMeldung.statusPraesenz) {
        case 0:
            tfDANAnwesendAktuell.setText("nicht anwesend");
            break;
        case 1:
            tfDANAnwesendAktuell.setText("anwesend");
            break;
        case 2:
            tfDANAnwesendAktuell.setText("war anwesend");
            break;
        case 4:
            tfDANAnwesendAktuell.setText("anwesend in Sammelkarte");
            break;
        }

        if (eclMeldung.gattung > 0) {
            tfDANGattung.setText(lDbBundle.param.paramBasis.gattungBezeichnungKurz[eclMeldung.gattung - 1]);
        }

        lDbBundle.dbZutrittskarten.readZuMeldungsIdent(eclMeldung.meldungsIdent);
        int anzEKarten = lDbBundle.dbZutrittskarten.anzErgebnis();
        if (anzEKarten != 0) {
            grpnDANEintrittskarten = new GridPane();
            grpnDANEintrittskarten.setVgap(5);
            grpnDANEintrittskarten.setHgap(15);

            for (int i = 0; i < anzEKarten; i++) {
                Label lEK = new Label(lDbBundle.dbZutrittskarten.ergebnisPosition(i).zutrittsIdent);
                grpnDANEintrittskarten.add(lEK, 0, i);
                Label lEKNeben = new Label(lDbBundle.dbZutrittskarten.ergebnisPosition(i).zutrittsIdentNeben);
                grpnDANEintrittskarten.add(lEKNeben, 1, i);
            }
            scpnDANEintrittskarten.setContent(grpnDANEintrittskarten);
        }

        lDbBundle.dbStimmkarten.readZuMeldungsIdent(eclMeldung.meldungsIdent);
        int anzSKarten = lDbBundle.dbStimmkarten.anzErgebnis();
        if (anzSKarten != 0) {
            grpnDANStimmkarten = new GridPane();
            grpnDANStimmkarten.setVgap(5);
            grpnDANStimmkarten.setHgap(15);

            for (int i = 0; i < anzSKarten; i++) {
                Label lEK = new Label(lDbBundle.dbStimmkarten.ergebnisPosition(i).stimmkarte);
                grpnDANStimmkarten.add(lEK, 0, i);
            }
            tfDANStimmkarten.setContent(grpnDANStimmkarten);
        }

        /*RAW-Stimmabgaben einlesen*/
        lDbBundle.dbAbstimmungMeldungRaw.lese_MeldungsIdent(eclMeldung.meldungsIdent,
                blDANAbstimmung.aktiverAbstimmungsblock.ident);
        arrayAbstimmungMeldungRawDAN = lDbBundle.dbAbstimmungMeldungRaw.ergebnisArray;
        if (arrayAbstimmungMeldungRawDAN == null) {
            anzArrayAbstimmungMeldungRawDAN = 0;
        } else {
            anzArrayAbstimmungMeldungRawDAN = arrayAbstimmungMeldungRawDAN.length;
        }

        angezeigterDetailsatz = -1;
        if (anzArrayAbstimmungMeldungRawDAN != 0) {
            grpnDANUebersichtStimmabgaben = new GridPane();
            grpnDANUebersichtStimmabgaben.setVgap(5);
            grpnDANUebersichtStimmabgaben.setHgap(15);

            //        	Label lUber0=new Label("LfdNr");
            //        	grpnDANUebersichtStimmabgaben.add(lUber0, 0, 0);

            Label lUber0a = new Label("Ident");
            grpnDANUebersichtStimmabgaben.add(lUber0a, 1, 0);

            Label lUber1 = new Label("Storniert");
            grpnDANUebersichtStimmabgaben.add(lUber1, 2, 0);

            Label lUber2 = new Label("Erteilt auf Weg");
            grpnDANUebersichtStimmabgaben.add(lUber2, 3, 0);

            Label lUber3 = new Label("Verwendete Nr");
            grpnDANUebersichtStimmabgaben.add(lUber3, 4, 0);

            Label lUber3a = new Label("Verarbeitet");
            grpnDANUebersichtStimmabgaben.add(lUber3a, 5, 0);

            Label lUber4 = new Label("Arbeitsplatz");
            grpnDANUebersichtStimmabgaben.add(lUber4, 6, 0);

            Label lUber5 = new Label("Zeit Stimmabgabe");
            grpnDANUebersichtStimmabgaben.add(lUber5, 7, 0);

            Label lUber6 = new Label("Zeit Speichervorgang");
            grpnDANUebersichtStimmabgaben.add(lUber6, 8, 0);

            for (int i = 0; i < anzArrayAbstimmungMeldungRawDAN; i++) {
                EclAbstimmungMeldungRaw lAbstimmungMeldungRaw = arrayAbstimmungMeldungRawDAN[i];

                //            	Label lInhalt0=new Label(Integer.toString(i));
                //            	grpnDANUebersichtStimmabgaben.add(lInhalt0, 0, i+1);

                Label lInhalt0a = new Label(Integer.toString(lAbstimmungMeldungRaw.ident));
                grpnDANUebersichtStimmabgaben.add(lInhalt0a, 1, i + 1);

                if (lAbstimmungMeldungRaw.wurdeStorniert == 1) {
                    Label lInhalt1 = new Label("J");
                    grpnDANUebersichtStimmabgaben.add(lInhalt1, 2, i + 1);
                }

                Label lInhalt2 = new Label(KonstWillenserklaerungWeg.getText(lAbstimmungMeldungRaw.erteiltAufWeg));
                grpnDANUebersichtStimmabgaben.add(lInhalt2, 3, i + 1);

                Label lInhalt3 = new Label(lAbstimmungMeldungRaw.verwendeteNummer);
                grpnDANUebersichtStimmabgaben.add(lInhalt3, 4, i + 1);

                if (lAbstimmungMeldungRaw.wurdeVerarbeitet == 1 && lAbstimmungMeldungRaw.fehlercode >= 0) {
                    Label lInhalt1 = new Label("J");
                    grpnDANUebersichtStimmabgaben.add(lInhalt1, 5, i + 1);
                } else {
                    Label lInhalt1 = new Label(CaFehler.getFehlertext(lAbstimmungMeldungRaw.fehlercode, 0));
                    grpnDANUebersichtStimmabgaben.add(lInhalt1, 5, i + 1);
                }

                Label lInhalt4 = new Label(Integer.toString(lAbstimmungMeldungRaw.arbeitsplatzNr));
                grpnDANUebersichtStimmabgaben.add(lInhalt4, 6, i + 1);

                Label lInhalt5 = new Label(CaDatumZeit.DatumZeitStringFromLong(lAbstimmungMeldungRaw.zeitstempelraw));
                grpnDANUebersichtStimmabgaben.add(lInhalt5, 7, i + 1);

                Label lInhalt6 = new Label(CaDatumZeit.DatumZeitStringFuerAnzeige(lAbstimmungMeldungRaw.zeitstempel));
                grpnDANUebersichtStimmabgaben.add(lInhalt6, 8, i + 1);

                for (int i1 = 0; i1 < blDANAbstimmung.getAnzAbstimmungenInAktivemAbstimmungsblock(); i1++) {
                    Label lTop = new Label(blDANAbstimmung.abstimmungen[i1].nummer + " "
                            + blDANAbstimmung.abstimmungen[i1].nummerindex);
                    grpnDANUebersichtStimmabgaben.add(lTop, 9 + i1 * 2, i + 1);

                    if (!blDANAbstimmung.istUeberschrift(i1)) {
                        int abgegebeneStimmart = lAbstimmungMeldungRaw.abgabe[blDANAbstimmung.lieferePosInWeisung(i1)];
                        Label lStimmart = new Label(KonstStimmart.getTextKurz(abgegebeneStimmart));
                        grpnDANUebersichtStimmabgaben.add(lStimmart, 9 + i1 * 2 + 1, i + 1);
                    }

                }

            }
            scrpnDANUebersichtStimmabgaben.setContent(grpnDANUebersichtStimmabgaben);

            angezeigterDetailsatz = 0;
        }

        zeigeDetailsatz();
        zeigeAktuelleAbstimmungDAN();

        lDbBundle.closeAll();

    }

    /**
     * **************Aktionen*****************.
     *
     * @param event the event
     */

    @FXML
    void onBtnDANFehlerErneutVerarbeiten(ActionEvent event) {
        lDbBundle.openAll();
        blDANAbstimmung.arbeiteFehlerDurchUndUeberpruefe();
        lDbBundle.closeAll();
        ausfuehrenDAN();
    }

    /**
     * On btn DAN refresh.
     *
     * @param event the event
     */
    @FXML
    void onBtnDANRefresh(ActionEvent event) {

        doChanged(cbDANAnzeige.getValue(), cbDANAnzeige.getValue());
    }

    /**
     * On btn DAN aktuell pruefen.
     *
     * @param event the event
     */
    @FXML
    void onBtnDANAktuellPruefen(ActionEvent event) {
        if (angezeigterDetailsatz == -1) {
            return;
        }
        lDbBundle.openAll();
        blDANAbstimmung.ermittleAktuelleAbstimmungNeu(eclMeldungDAN);
        lDbBundle.closeAll();
        nummerEingelesenDAN(false);

    }

    /**
     * On btn DAN clear.
     *
     * @param event the event
     */
    @FXML
    void onBtnDANClear(ActionEvent event) {
        clearDANDaten();
        tfDANNummerZurAnaylse.requestFocus();

    }

    /**
     * On btn DAN fertig.
     *
     * @param event the event
     */
    @FXML
    void onBtnDANFertig(ActionEvent event) {
        erledigtAblaufAuswahl();
        resetAblaufAuswahl();

    }

    /**
     * On btn DAN storno.
     *
     * @param event the event
     */
    @FXML
    void onBtnDANStorno(ActionEvent event) {
        if (angezeigterDetailsatz == -1) {
            return;
        }
        EclAbstimmungMeldungRaw lAbstimmungMeldungRaw = arrayAbstimmungMeldungRawDAN[angezeigterDetailsatz];
        lDbBundle.openAll();
        if (lAbstimmungMeldungRaw.wurdeStorniert == 1) {
            /*Re-Storno ausführen*/
            lAbstimmungMeldungRaw.wurdeStorniert = 0;
        } else {
            /*Storno durchführen*/
            lAbstimmungMeldungRaw.wurdeStorniert = 1;
        }

        lDbBundle.dbAbstimmungMeldungRaw.updateStorno(lAbstimmungMeldungRaw);
        blDANAbstimmung.ermittleAktuelleAbstimmungNeu(eclMeldungDAN);
        lDbBundle.closeAll();
        nummerEingelesenDAN(false);

    }

    /**
     * On btn DAN vor.
     *
     * @param event the event
     */
    @FXML
    void onBtnDANVor(ActionEvent event) {
        angezeigterDetailsatz++;
        zeigeDetailsatz();
    }

    /**
     * On btn DAN zurueck.
     *
     * @param event the event
     */
    @FXML
    void onBtnDANZurueck(ActionEvent event) {
        angezeigterDetailsatz--;
        zeigeDetailsatz();
    }

    /**
     * On key DAN nummer zur anaylse.
     *
     * @param event the event
     */
    @FXML
    void onKeyDANNummerZurAnaylse(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            nummerEingelesenDAN(true);
        }

    }

    /**
     * ***************Anzeige-Funktionen*************************.
     */

    /**
     * Zeige angezeigterDetailsatz (0 bis länge-1) im Raw-Fenster an Falls
     * angezeigterDetailsatz==-1, dann werden alle Buttons Disabled
     */
    private void zeigeDetailsatz() {
        if (angezeigterDetailsatz == -1) {
            btnDANVor.setDisable(true);
            btnDANZurueck.setDisable(true);
            btnDANStorno.setDisable(true);
            btnDANStorno.setText("Storno");
            btnDANAktuellPruefen.setDisable(true);
            return;
        }
        btnDANAktuellPruefen.setDisable(false);
        if (angezeigterDetailsatz == 0) {
            btnDANZurueck.setDisable(true);
        } else {
            btnDANZurueck.setDisable(false);
        }
        if (angezeigterDetailsatz == anzArrayAbstimmungMeldungRawDAN - 1) {
            btnDANVor.setDisable(true);
        } else {
            btnDANVor.setDisable(false);
        }
        EclAbstimmungMeldungRaw lAbstimmungMeldungRaw = arrayAbstimmungMeldungRawDAN[angezeigterDetailsatz];
        tfDANDetailIdent.setText(Integer.toString(lAbstimmungMeldungRaw.ident));
        if (lAbstimmungMeldungRaw.wurdeVerarbeitet == 1 && lAbstimmungMeldungRaw.fehlercode >= 0) {
            tfDANDetailVerarbeitet.setText("Ja");
//            btnDANAktuellPruefen.setDisable(true);
        } else {
            tfDANDetailVerarbeitet.setText(CaFehler.getFehlertext(lAbstimmungMeldungRaw.fehlercode, 0));
//            btnDANAktuellPruefen.setDisable(false);
        }
        tfDANDetailGeleseneNr.setText(lAbstimmungMeldungRaw.verwendeteNummer);
        if (lAbstimmungMeldungRaw.wurdeStorniert == 1) {
            tfDANDetailWurdeStorniert.setText("Ja");
            btnDANStorno.setDisable(false);
            btnDANStorno.setText("Re-Storno");
        } else {
            tfDANDetailWurdeStorniert.setText("Nein");
            btnDANStorno.setDisable(false);
            btnDANStorno.setText("Storno");
        }
        tfDANDetailErteiltAufWeg.setText(KonstWillenserklaerungWeg.getText(lAbstimmungMeldungRaw.erteiltAufWeg));
        tfDANDetailZeitGespeichert.setText(CaDatumZeit.DatumZeitStringFromLong(lAbstimmungMeldungRaw.zeitstempelraw));
        tfDANDetailZeitAbgegeben.setText(CaDatumZeit.DatumZeitStringFuerAnzeige(lAbstimmungMeldungRaw.zeitstempel));

        grpnDANDetailStimmabgabe = new GridPane();
        grpnDANDetailStimmabgabe.setVgap(5);
        grpnDANDetailStimmabgabe.setHgap(15);

        for (int i1 = 0; i1 < blDANAbstimmung.getAnzAbstimmungenInAktivemAbstimmungsblock(); i1++) {
            if (!blDANAbstimmung.istUeberschrift(i1)) {
                int abgegebeneStimmart = lAbstimmungMeldungRaw.abgabe[blDANAbstimmung.lieferePosInWeisung(i1)];
                Label lStimmart = new Label(KonstStimmart.getTextKurz(abgegebeneStimmart));
                grpnDANDetailStimmabgabe.add(lStimmart, 0, i1);
            }

            Label lTop = new Label(
                    blDANAbstimmung.abstimmungen[i1].nummer + " " + blDANAbstimmung.abstimmungen[i1].nummerindex);
            grpnDANDetailStimmabgabe.add(lTop, 1, i1);

            Label lTopKurz = new Label(blDANAbstimmung.abstimmungen[i1].kurzBezeichnung);
            grpnDANDetailStimmabgabe.add(lTopKurz, 3, i1);

        }

        scpnDANDetailStimmabgabe.setContent(grpnDANDetailStimmabgabe);

    }

    /**
     * lDbBundle muß geöffnet sein!.
     */
    private void zeigeAktuelleAbstimmungDAN() {
        boolean rcBool = blDANAbstimmung.liefereAktuelleAbstimmungZuMeldeIdent(eclMeldungDAN.meldungsIdent);
        if (rcBool == false) {
            return;
        }

        grpnDANAktuelleStimmwertung = new GridPane();
        grpnDANAktuelleStimmwertung.setVgap(5);
        grpnDANAktuelleStimmwertung.setHgap(15);

        for (int i1 = 0; i1 < blDANAbstimmung.getAnzAbstimmungenInAktivemAbstimmungsblock(); i1++) {
            if (!blDANAbstimmung.istUeberschrift(i1)) {
                int abgegebeneStimmart = blDANAbstimmung.liefereAktuelleMarkierungZuAbstimmungsPosition(i1);
                Label lStimmart = new Label(KonstStimmart.getTextKurz(abgegebeneStimmart));
                grpnDANAktuelleStimmwertung.add(lStimmart, 0, i1);
            }

            Label lTop = new Label(
                    blDANAbstimmung.abstimmungen[i1].nummer + " " + blDANAbstimmung.abstimmungen[i1].nummerindex);
            grpnDANAktuelleStimmwertung.add(lTop, 1, i1);

            Label lTopKurz = new Label(blDANAbstimmung.abstimmungen[i1].kurzBezeichnung);
            grpnDANAktuelleStimmwertung.add(lTopKurz, 3, i1);

        }

        scpnAktuelleStimmwertung.setContent(grpnDANAktuelleStimmwertung);

    }

    /** Alle Daten des angezeigten Aktionärs löschen bzw. vorbereiten */
    private void clearDANDaten() {
        tfDANNummerZurAnaylse.setText("");
        tfDANInterneIdent.setText("");
        tfDANInterneIdent.setEditable(false);
        tfDANSammelkartenstatus.setText("");
        tfDANSammelkartenstatus.setEditable(false);
        tfDANName.setText("");
        tfDANName.setEditable(false);
        tfDANVorname.setText("");
        tfDANVorname.setEditable(false);
        tfDANOrt.setText("");
        tfDANOrt.setEditable(false);
        tfDANVertreterName.setText("");
        tfDANVertreterName.setEditable(false);
        tfDANVertreterVorname.setText("");
        tfDANVertreterVorname.setEditable(false);
        tfDANVertreterOrt.setText("");
        tfDANVertreterOrt.setEditable(false);
        tfDANStimmen.setText("");
        tfDANStimmen.setEditable(false);
        tfDANAnwesendDelayed.setText("");
        tfDANAnwesendDelayed.setEditable(false);
        tfDANAnwesendAktuell.setText("");
        tfDANAnwesendAktuell.setEditable(false);
        tfDANGattung.setText("");
        tfDANGattung.setEditable(false);
        scpnDANEintrittskarten.setContent(null);
        tfDANStimmkarten.setContent(null);
        scrpnDANUebersichtStimmabgaben.setContent(null);

        btnDANVor.setDisable(true);
        btnDANZurueck.setDisable(true);
        btnDANStorno.setDisable(true);
        btnDANAktuellPruefen.setDisable(true);

        tfDANDetailIdent.setText("");
        tfDANDetailIdent.setEditable(false);
        tfDANDetailVerarbeitet.setText("");
        tfDANDetailVerarbeitet.setEditable(false);
        tfDANDetailGeleseneNr.setText("");
        tfDANDetailGeleseneNr.setEditable(false);
        tfDANDetailWurdeStorniert.setText("");
        tfDANDetailWurdeStorniert.setEditable(false);
        tfDANDetailErteiltAufWeg.setText("");
        tfDANDetailErteiltAufWeg.setEditable(false);
        tfDANDetailZeitGespeichert.setText("");
        tfDANDetailZeitGespeichert.setEditable(false);
        tfDANDetailZeitAbgegeben.setText("");
        tfDANDetailZeitAbgegeben.setEditable(false);
        scpnDANDetailStimmabgabe.setContent(null);

        scpnAktuelleStimmwertung.setContent(null);

    }

    /**
     * *************************************************************************
     * **************monitor*****************************************.
     */

    /******************* Logik ************************************/

    private void ausfuehrenMON() {

        pnMON.setVisible(true);
        zeigeTabletsMON();
    }

    /**
     * Zeige tablets MON.
     */
    private void zeigeTabletsMON() {
        lDbBundle.openAll();
        lDbBundle.dbTablet.lese_all();
        gpMONTablet.getChildren().clear();
        gpMONTablet.setVgap(5);
        gpMONTablet.setHgap(15);

        int anzTablets = lDbBundle.dbTablet.anzErgebnisGefunden();
        if (anzTablets > 0) {
            for (int i = 0; i < anzTablets; i++) {
                EclTablet lTablet = lDbBundle.dbTablet.ergebnisGefunden(i);
                Label lblArbeitsplatz = new Label(Integer.toString(lTablet.arbeitsplatzNr));
                gpMONTablet.add(lblArbeitsplatz, 0, i);

                Label lblStatus = new Label();
                switch (lTablet.status) {
                case 1:
                    lblStatus.setText("ist angemeldet");
                    break;
                case 2:
                    lblStatus.setText("in Abstimmungsmodus");
                    break;
                case 3:
                    lblStatus.setText("Abstimmungsmodus beendet");
                    break;
                }
                gpMONTablet.add(lblStatus, 1, i);

            }
        }
        lDbBundle.closeAll();
    }

    /**
     * On btn MON fertig.
     *
     * @param event the event
     */
    @FXML
    void onBtnMONFertig(ActionEvent event) {
        erledigtAblaufAuswahl();
        resetAblaufAuswahl();
    }

    /**
     * On btn MON refresh tablets.
     *
     * @param event the event
     */
    @FXML
    void onBtnMONRefreshTablets(ActionEvent event) {
        zeigeTabletsMON();

    }

    /**
     * *************************************************************************
     * **************textdatei einlesen*****************************************.
     */

    private String scanDateiPfad = "C:";

    /** The protokoll datei. */
    private CaDateiWrite protokollDatei = null;

    /** The protokoll offset. */
    private int protokollOffset = 0;

    /** The aus scan DB erstlauf. */
    private boolean ausScanDBErstlauf = true;

    /** The aktueller scan lauf. */
    private int aktuellerScanLauf = -1;

    /**
     * *****************Logik***********************************.
     */

    private void ausfuehrenTEDAT() {

        btnTEDATScannerLesen.setVisible(false);
        btnTEDATTabletDatei.setVisible(false);
        btnTEDATScannerLesenDBErst.setVisible(false);
        btnTEDATScannerLesenDBWieder.setVisible(false);

        pnTEDAT.setVisible(true);

        lDbBundle.openAll();
        blAbstimmung = new BlAbstimmung(lDbBundle);
 
        switch (ausgewaehlteAblaufFunktion) {
        case KonstAbstimmungsAblauf.stimmenEinlesenTabletKarte: {
            btnTEDATTabletDatei.setVisible(true);
            lblTEDATUeberschrift.setText("Stimmbelege einlesen Tablet-Karte");
            blAbstimmung.setzeAktivenAbstimmungsblockSortierenNachAufTable();
            break;
        }
        case KonstAbstimmungsAblauf.stimmenEinlesenScannerDatei: {
            btnTEDATScannerLesen.setVisible(true);
            lblTEDATUeberschrift.setText("Stimmbelege einlesen Scanner aus Datei");
            blAbstimmung.setzeAktivenAbstimmungsblockSortierenNachStimmkarten();
            break;
        }
        case KonstAbstimmungsAblauf.stimmenEinlesenScannerDatenbank: {
            btnTEDATScannerLesenDBErst.setVisible(true);
            btnTEDATScannerLesenDBWieder.setVisible(true);
            btnTEDATDateiAuswaehlen.setVisible(false);
            tfTEDATEinzulesendeDatei.setVisible(false);
            lblTEDATUeberschrift.setText("Stimmbelege einlesen Scanner aus Datenbank");
            blAbstimmung.setzeAktivenAbstimmungsblockSortierenNachStimmkarten();
            break;
        }
        }

        blAbstimmung.leseAktivenOderLokalenAbstimmungsblock(aktivDeaktivLokalAbstimmungsblock);
        blAbstimmung.leseAbstimmungsVorschlagDerGesellschaft();
        lDbBundle.closeAll();

    }

    /**
     * On btn TEDAT datei auswaehlen.
     *
     * @param event the event
     */
    @FXML
    void onBtnTEDATDateiAuswaehlen(ActionEvent event) {
        /*Laufwerk für Scanner steht auf (Voreinstellung) */
        scanDateiPfad = "D:";

        try {

            FileChooser fc = new FileChooser();
            fc.setInitialDirectory(new File(scanDateiPfad));
            FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter("TXT files", "*.txt");
            fc.getExtensionFilters().add(filter);
            File f = fc.showOpenDialog(null);

            scanDateiPfad = f.getPath();
            tfTEDATEinzulesendeDatei.setText(f.getPath());

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }

    /**
     * On btn TEDAT scanner lesen.
     *
     * @param event the event
     */
    @FXML
    void onBtnTEDATScannerLesen(ActionEvent event) {
        doTEDATDateiEinlesen();
    }

    /**
     * On btn TEDAT tablet datei.
     *
     * @param event the event
     */
    @FXML
    void onBtnTEDATTabletDatei(ActionEvent event) {
        doTEDATDateiEinlesen();

    }

    /**
     * Tedat init DB erst.
     */
    private void tedatInitDBErst() {
        lDbBundle.openAll();

        /*Aktueller Scan-Lauf-Nummer (neue Nummer) definieren und zurückschreiben*/
        aktuellerScanLauf = lDbBundle.dbScan.holeNeueScanLaufNummer();

        /*Alle noch nicht verarbeiteten Scans mit der aktuellen Scan-Lauf-Nummer updaten*/
        /*int rc=*/lDbBundle.dbScan.updateUngeleseneScanVorgaenge(aktuellerScanLauf);

        /*Ergebnis= aktuellerScanLauf*/

        lDbBundle.closeAll();
    }

    /**
     * Tedat init DB wieder.
     */
    private void tedatInitDBWieder() {
        lDbBundle.openAll();

        /*Aktueller Scan-Lauf-Nummer (neue Nummer) definieren und zurückschreiben*/
        aktuellerScanLauf = lDbBundle.dbScan.holeLetzteScanLaufNummer();

        /*Ergebnis= aktuellerScanLauf*/

        lDbBundle.closeAll();
    }

    /**
     * On btn TEDAT scanner lesen DB erst.
     *
     * @param event the event
     */
    @FXML
    void onBtnTEDATScannerLesenDBErst(ActionEvent event) {
        ausScanDBErstlauf = true;
        tedatInitDBErst();
        doTEDATDateiEinlesen();
    }

    /**
     * On btn TEDAT scanner lesen DB wieder.
     *
     * @param event the event
     */
    @FXML
    void onBtnTEDATScannerLesenDBWieder(ActionEvent event) {
        ausScanDBErstlauf = false;
        tedatInitDBWieder();
        doTEDATDateiEinlesen();
    }

    /**
     * On btn TEDAT fertig.
     *
     * @param event the event
     */
    @FXML
    void onBtnTEDATFertig(ActionEvent event) {
        erledigtAblaufAuswahl();
        resetAblaufAuswahl();
    }

    /**
     * Proto eintrag.
     *
     * @param pEintrag the eintrag
     */
    /*Trägt pEintrag sowohl in die Protokoll-Datei als auch ins ProtokollPane ein*/
    private void protoEintrag(String pEintrag) {
        protokollDatei.ausgabe(pEintrag);
        protokollDatei.newline();

        Label lblProtokoll = new Label(pEintrag);
        gpTEDATProtokoll.add(lblProtokoll, 0, protokollOffset);
        protokollOffset++;

    }

    /** The gesamt markierung vorhanden. */
    private int[] gesamtMarkierungVorhanden = null;;

    /**
     * gesamtMarkierungVorhanden[kartennummer]=1 => Gesamtmarkierung für diese Karte
     * vorhanden.
     */
    private void bereiteGesamtmarkierungVorhandenAuf() {
        /*TODO _Abstimmungsparameter: Gesamtmarkierung je Karte vorsehen*/

        gesamtMarkierungVorhanden = new int[100];
        for (int i = 0; i < 100; i++) {
            gesamtMarkierungVorhanden[i] = 0;
        }

        if (lDbBundle.clGlobalVar.mandant == 116) {
            /*ku1007
             * Gesamtmarkierung bei S1 - 01, S - 07, A - 08
             * */
            gesamtMarkierungVorhanden[1] = 1;
            gesamtMarkierungVorhanden[7] = 1;
            gesamtMarkierungVorhanden[8] = 1;
        }
    }

    /**
     * Do TEDAT datei einlesen.
     */
    private void doTEDATDateiEinlesen() {

        /***************Beschreibung Dateiformat*************************
         * Scan-Datei, z.B.:
         * "Barcode";"Pos.01";"Pos.02";"Pos.03";"Pos.04";"Pos.05";"Pos.06";"Pos.07";"Pos.08";"Pos.09";"Pos.10"
         * "24850613518016";"1";"3";"3";"3";"3";"3";"3";"3";"3";"3"
         * 
         * Erste Zeile = Überschriftszeile.
         * An Pos.01 seht Gesamtmarkierung, wenn für diese Karte eine Gesamtmarkierung definiert wurde.
         * Ansonsten beginnt an Pos.01 die erste Einzelmarkierung der Karte.
         * 
         * 
         * Tablet-Datei, z.B.:
         * 100053000310;1528144322921;1;0;0;0;0;0;0;
         * Dateiname:
         * "abstimmung<Arbeitsplatznummer>.txt"
         * Trennzeichen=";"
         * 
         * Keine Überschriftszeile.
         * Datenzeilen:
         * > Barcode (in Original-Form, wie eingelesen)
         * > Time-Stamp (lon)
         * > Ident des aktiven Abstimmungsblocks, für den dieser Satz gelesen (eingesammelt) wurde
         * > Anschließend die gelesenen "Markierungen" für jeden Agendapunkt: Abstimmungsart (Int) (auch für Überschriften) (Sortiert nach Position)
         * 
         */

        /***************Testablauf Tablet-Datei****************************
         * Fälle:
         * A Online einlesen, Offline einlesen. Anschließend Offline einspielen.
         * B Offline einlesen, A Online einlesen. Anschließend Offline einspielen.
         * C Online einllesen, Online einlesen.
         * D Offline Einlesen (1), Offline EInlesen (2). Anschließend (1) einlesen, dann (2) einlesen
         * E Offline Einlesen (1), Offline Einlesen (2). Anschließend (2) einlesen, dann (1) einlesen.
         * 
         * 
         * Konkrete Ausprägungen:
         * User 101 Online-Einlesen
         *  Aktionär A Online einlesen (JNJN)
         * 
         * User 102 Online-Einlesen
         *  Aktionär C Online einlesen (JNJN)
         * 
         * User 106 Offline-Einlesen
         *  Aktionär A Offline einlesen (NJNJ)
         *  Aktionär B Offline einlesen (NJNJ)
         *  Aktionär D Offline einlesen (NJNJ)
         * 
         * User 102 Online-Einlesen
         *  Aktionär B Online einlesen (JNJN)
         *  Aktionäc C Online einlesen (NJNJ)
         * 
         * User 107 Offline-Einlesen
         *  Aktionär D Offline einlesen (JNJN)
         *  Aktionär E Offline einlesen (JNJN)
         * 
         * User 106 Offline einlesen
         *  Aktionär E Offline einlesen (NJNJ)
         * 
         * 
         * User 106 einlesen
         * User 107 einlesen
         * 
         */

        /**Wenn Barcode nicht verarbeitbar, dann wird rc auf false gesetzt (also keine Weiterverarbeitung),
         * sonst true.
         */
        boolean rc = false;

        /**
         * true => es werden Scan-Daten aus der Datenbank eingelesen. false => es werden
         * Daten aus einer Datei gelesen, je nach tabletDatei entweder vom Scanner oder
         * vom Tablet
         */
        boolean datenbankDatei = false;

        /**
         * Wird nur beim Einlesen einer Tablet-Datei verwendet. Enthält dann die Ident
         * des Abstimmungsblocks, für den der aktuelle Satz auf dem Tablet eingelesen
         * wurde.
         */
        int aktiveAbstimmungAusDatei = 0;

        /**
         * Wird nur verwendet für Einlesen aus Datei (also wenn datenbankDatei=false).
         * Wenn aus DatenbankDatei gelesen wird, dann wird - zwecks Ausgabe in Protokoll
         * - entweder "ScannerDatei Erstlauf" oder "ScannerDatei Wiederholung"
         * eingetragen.
         */
        String importDateiname = tfTEDATEinzulesendeDatei.getText();
        if (ausgewaehlteAblaufFunktion == KonstAbstimmungsAblauf.stimmenEinlesenScannerDatenbank) {
            datenbankDatei = true;
            if (ausScanDBErstlauf) {
                importDateiname = "ScannerDatei Erstlauf";
            } else {
                importDateiname = "ScannerDatei Wiederholung";
            }
        }

        /** Anzahl der Insgesamt eingelesenen Sätze, also ok-Sätze und Fehler-Sätze */
        int gesamtanzahl = 0;

        /** Anzahl der Sätze, die nicht verarbeitet werden konnten */
        int fehleranzahl = 0;

        /*Allgemeines Initialisieren*/
        lDbBundle = new DbBundle();
        lDbBundle.openAll();
        blAbstimmung.initDb(lDbBundle);

        bereiteGesamtmarkierungVorhandenAuf();

        /*Protokolldatei Initialisieren*/
        protokollDatei = null;
        protokollDatei = new CaDateiWrite();
        protokollDatei.oeffne(lDbBundle, "stimmkartenScannen");
        protokollDatei.ausgabe(CaDatumZeit.DatumZeitStringFuerDatenbank());
        protokollDatei.newline();

        /*Protokoll-Pane initialisieren*/
        gpTEDATProtokoll.getChildren().clear();
        gpTEDATProtokoll.setVgap(5);
        gpTEDATProtokoll.setHgap(15);
        protokollOffset = 0;

        /*Dateiname ins Protokoll*/
        protoEintrag("Scan-Datei : " + importDateiname);

        boolean tabletDatei = false;

        /**
         * Falls aus Tablet-Datei gelesen wird: zusätzliche 2 Felder am Anfang! Siehe
         * Beschreibung Datei
         */
        int tabletOffset = 0;

        if (datenbankDatei == false) {
            if (ausgewaehlteAblaufFunktion == KonstAbstimmungsAblauf.stimmenEinlesenScannerDatei) {
                tabletDatei = false;
                tabletOffset = 0;
            } else {
                tabletDatei = true;
                tabletOffset = 2;
            }
        }

        try {

            FileReader fr = null;
            BufferedReader br = null;
            int anzahlInDatenbank = 0;
            int offsetInDatenbank = 0;
            if (datenbankDatei) {
                anzahlInDatenbank = lDbBundle.dbScan.read_scanVorgaenge(aktuellerScanLauf);
                offsetInDatenbank = 0;
            } else {
                fr = new FileReader(importDateiname);
                br = new BufferedReader(fr);
            }

            try {
                String zeile = null;
                while ((datenbankDatei == false && (zeile = br.readLine()) != null)
                        || (datenbankDatei == true && offsetInDatenbank < anzahlInDatenbank)) {
                    if (datenbankDatei == true || (!zeile.isEmpty())) {
                        EclScan lScanSplit = null;
                        String[] zeileSplit = null;
                        if (datenbankDatei) {
                            lScanSplit = lDbBundle.dbScan.ergebnisPosition(offsetInDatenbank);
                            offsetInDatenbank++;

                        } else {
                            zeileSplit = zeile.split(";");
                        }

                        String gelesenerBarcode = "";
                        if (datenbankDatei) {
                            gelesenerBarcode = lScanSplit.barcode;
                        } else {
                            gelesenerBarcode = zeileSplit[0];
                        }
                        gelesenerBarcode = gelesenerBarcode.replace('"', ' ').trim();

                        if (!gelesenerBarcode.equals("Barcode")) {//Abfrage erforderlich, da erste Zeile in Scan-Datei Überschrift

                            gesamtanzahl++;

                            String hStimmzettelnummer = "";
                            int karteHatGesamtMarkierung = 0; //1 => Gesamtmarkierung für die eingelesene Karte vorhanden
                            rc = true;

                            boolean rcBool = dekodiereNummernform(gelesenerBarcode);
                            BlNummernformen blNummernformen = allgBlNummernformen;
                            System.out.println("rcBool" + rcBool);
                            if (rcBool == false) {
                                rc = false;
                            }

                            if (rc == false) {
                                protoEintrag("Aussteuern " + gelesenerBarcode + " : " + rcNummernformText);
                                fehleranzahl++;
                            } else {
                                if (tabletDatei) {
                                    karteHatGesamtMarkierung = 0;
                                } else {
                                    karteHatGesamtMarkierung = gesamtMarkierungVorhanden[blNummernformen.rcStimmkartennummer];
                                }
                                hStimmzettelnummer = Integer.toString(blNummernformen.rcStimmkartennummer);

                                int anzahlZuVerarbeitendeEinzelmarkierungen = 0;

                                if (datenbankDatei) {
                                    anzahlZuVerarbeitendeEinzelmarkierungen = 60;
                                } else {
                                    /**
                                     * Falls Gesamtmarkierung vorhanden, und falls Tablet-Datei gelesen wird,
                                     * Feldanzahl entsprechend korrigieren
                                     */
                                    anzahlZuVerarbeitendeEinzelmarkierungen = zeileSplit.length
                                            - karteHatGesamtMarkierung - tabletOffset - 1;
                                }

                                /**
                                 * topString = die Einzelmarkierungen +1, da [0] nicht belegt!
                                 */
                                String[] topString = new String[anzahlZuVerarbeitendeEinzelmarkierungen + 1];

                                if (datenbankDatei) {
                                    for (int j = 1; j <= anzahlZuVerarbeitendeEinzelmarkierungen; j++) {
                                        topString[j] = lScanSplit.pos[j].replace('"', ' ').trim();
                                    }

                                    //					    			/*>>>>Gesamtmarkierung<<<<*/
                                    //					    			for (int j=1;j<=anzahlZuVerarbeitendeEinzelmarkierungen;j++){
                                    //					    				if (topString[j].equals("3")){
                                    //					    					if (lScanSplit.gesamtmarkierung.equals("1") || 
                                    //					    							lScanSplit.geraeteNummer.equals("2")){
                                    //					    						topString[j]=lScanSplit.gesamtmarkierung;
                                    //					    					}
                                    //					    				}
                                    //					    			}
                                    //					    			/*<<<<<<<*/

                                } else {
                                    for (int j = 1; j <= anzahlZuVerarbeitendeEinzelmarkierungen; j++) {
                                        /*Falls Gesamtmarkierung, bzw. falls Tablet-Datei - erste Felder ignorieren!*/
                                        topString[j] = zeileSplit[j + karteHatGesamtMarkierung + tabletOffset]
                                                .replace('"', ' ').trim();
                                    }
                                }

                                String gesamtMarkierungStimmart = "";
                                if (karteHatGesamtMarkierung == 1) {
                                    if (datenbankDatei) {
                                        gesamtMarkierungStimmart = lScanSplit.gesamtmarkierung;
                                        if (gesamtMarkierungStimmart.equals("0")) {
                                            gesamtMarkierungStimmart = "3";
                                        }
                                    } else {
                                        gesamtMarkierungStimmart = zeileSplit[1];
                                    }
                                }

                                /*Nun Speichern*/

                                /*Meldung einlesen*/
                                rcBool = einlesenMeldung();

                                if (rcBool == false) {
                                    protoEintrag("Fehler bei " + gelesenerBarcode + " :" + rcNummernformText);
                                    fehleranzahl++;
                                } else {
                                    if (!rcNummernformText.isEmpty()) {
                                        protoEintrag("Warnung bei " + gelesenerBarcode + " :" + rcNummernformText);
                                    }

                                    if (tabletDatei == false) {
                                        blAbstimmung.starteSpeichernFuerMeldung(rcMeldung);
                                    } else {
                                        String hZeitString = zeileSplit[1].replace('"', ' ').trim();
                                        long hZeitLong = Long.parseLong(hZeitString);
                                        aktiveAbstimmungAusDatei = Integer.parseInt(zeileSplit[2]);
                                        blAbstimmung.starteSpeichernFuerMeldung(rcMeldung, hZeitLong, false,
                                                KonstWillenserklaerungWeg.abstTablet, 0, "", 1);
                                    }

                                    int positionGefunden = 0; //wird auf 1 gesetzt, wenn eine gültige Abstimmungsmarkierung enthalten ist

                                    if (tabletDatei) {
                                        if (aktiveAbstimmungAusDatei == blAbstimmung.aktiverAbstimmungsblock.ident) {
                                            /*Bei Tablet: 0 = Nummer, 1=Zeitstempel; Danach direkt wie Pos*/
                                            for (int j = 1; j <= anzahlZuVerarbeitendeEinzelmarkierungen; j++) {
                                                int abstimmungsOffset = j - 1;
                                                int weisungsPosition = blAbstimmung
                                                        .lieferePosInWeisung(abstimmungsOffset);
                                                System.out.println("abstimmungsOffset=" + abstimmungsOffset
                                                        + " weisungsPosition=" + weisungsPosition);
                                                if (weisungsPosition != -1) {
                                                    positionGefunden = 1;
                                                    int stimmart = -1;
                                                    String hString = topString[j];
                                                    switch (hString) {
                                                    case "0": {
                                                        stimmart = KonstStimmart.nichtMarkiert;
                                                        break;
                                                    }
                                                    case "1": {
                                                        stimmart = KonstStimmart.ja;
                                                        break;
                                                    }
                                                    case "2": {
                                                        stimmart = KonstStimmart.nein;
                                                        break;
                                                    }
                                                    case "3": {
                                                        stimmart = KonstStimmart.enthaltung;
                                                        break;
                                                    }
                                                    case "???": {
                                                        stimmart = KonstStimmart.ungueltig;
                                                        break;
                                                    }
                                                    }
                                                    if (stimmart != -1) {
                                                        blAbstimmung.setzeMarkierungZuAbstimmungsPosition(stimmart,
                                                                abstimmungsOffset, KonstWillenserklaerungWeg.abstTablet);
                                                    }
                                                }
                                            }
                                        }
                                    } else { 
                                        //Scandatei oder ScanTable
                                        //   CaBug.druckeLog("=================================anzahlZuVerarbeitendeEinzelmarkierungen="+anzahlZuVerarbeitendeEinzelmarkierungen, logDrucken, 10);
                                        for (int j = 1; j <= anzahlZuVerarbeitendeEinzelmarkierungen; j++) {
                                            int weisungsPosition = blAbstimmung
                                                    .liefereLfdAbstimmungsNrZuStimmkartenNrUndPosition(
                                                            Integer.parseInt(hStimmzettelnummer), j);
                                            // CaBug.druckeLog("hStimmzettelnummer="+hStimmzettelnummer+" j="+j+" weisungsPosition="+weisungsPosition, logDrucken, 10);
                                            if (weisungsPosition != -1) {
                                                positionGefunden = 1;
                                                int stimmart = -1;
                                                String hString = topString[j];
                                                //                                                CaBug.druckeLog("hString="+hString, logDrucken, 10);
                                                /*TODO _Abstimmungsparameter: Achtung: Gesamtmarkierung derzeit nur beim Additionsverfahren funktionsfähig!*/
                                                if (hString.compareTo("3") == 0 && karteHatGesamtMarkierung == 1) { //Gesamtmarkierung
                                                    hString = gesamtMarkierungStimmart;
                                                }
                                                switch (hString) {
                                                case "0": {
                                                    stimmart = KonstStimmart.nichtMarkiert;
                                                    break;
                                                }
                                                case "1": {
                                                    stimmart = KonstStimmart.ja;
                                                    break;
                                                }
                                                case "2": {
                                                    stimmart = KonstStimmart.nein;
                                                    break;
                                                }
                                                case "3": {
                                                    stimmart = KonstStimmart.enthaltung;
                                                    break;
                                                }
                                                case "???": {
                                                    stimmart = KonstStimmart.ungueltig;
                                                    break;
                                                }
                                                }
                                                if (stimmart != -1) {
                                                    blAbstimmung.setzeMarkierungZuAbstimmungsPosition(stimmart,
                                                            weisungsPosition, KonstWillenserklaerungWeg.abstStapelScanner);
                                                }

                                            }
                                        }

                                    }

                                    blAbstimmung.beendeSpeichernFuerMeldung();

                                    if (positionGefunden == 0) {
                                        protoEintrag("Aussteuern " + gelesenerBarcode
                                                + " : keine Markierungsposition passend!");
                                        fehleranzahl++;
                                    }
                                    protoEintrag("Ok: " + gelesenerBarcode);
                                }
                            }
                        }
                    }
                }
                if (datenbankDatei) {
                } else {
                    br.close();
                    fr.close();
                }
            } catch (IOException e) {
                System.out.print(e.getMessage());
            }

        } catch (FileNotFoundException ex) {
            protoEintrag("Datei nicht gefunden!");
        }

        protoEintrag(CaDatumZeit.DatumZeitStringFuerDatenbank());
        protoEintrag("Anzahl verarbeiteter Sätze insgesamt=" + Integer.toString(gesamtanzahl));

        protoEintrag("Anzahl Fehler=" + Integer.toString(fehleranzahl));

        protoEintrag(" Protokoll in " + protokollDatei.dateiname);
        protokollDatei.schliessen();
        lDbBundle.closeAll();
    }

    /**
     * *************************************************************************
     * **************Auswerten*****************************************.
     */

    /******************* Logik ************************************/

    private void ausfuehrenAUSW() {

        pnAUSW.setVisible(true);

    }

    /**
     * On btn AUSW auswerten.
     *
     * @param event the event
     */
    @FXML
    void onBtnAUSWAuswerten(ActionEvent event) {
        auswerten();
        archiviertNachAuswertung = false;
        if (blAbstimmung.rcWarnungenFehler!=null) {
            for (int i=0;i<blAbstimmung.rcWarnungenFehler.size();i++) {
                String fehlertext=blAbstimmung.rcWarnungenFehler.get(i);
                CaZeigeHinweis caZeigeHinweis=new CaZeigeHinweis();
                caZeigeHinweis.zeige(eigeneStage, fehlertext);
            }
        }
    }

    /**
     * Auswerten.
     */
    private void auswerten() {
        Long ku310Arbeitnehmer=-1L;
        Long ku310Arbeitgeber=-1L;
        if (ParamSpezial.ku310(lDbBundle.clGlobalVar.mandant)) {
            Stage neuerDialog = new Stage();

            CtrlAbstimmungku310 controllerFenster = new CtrlAbstimmungku310();
            controllerFenster.init(neuerDialog);
            
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Abstimmungku310.fxml"));
            loader.setController(controllerFenster);
            Parent mainPane = null;
            try {
                mainPane = (Parent) loader.load();
            } catch (IOException e) {
                CaBug.drucke("001");
                e.printStackTrace();
            }
            Scene scene = new Scene(mainPane, 800 /*controllerFenster.width*/, 700/*controllerFenster.height*/);
            neuerDialog.setTitle("Präsenz ku310");
            neuerDialog.setScene(scene);
            neuerDialog.initModality(Modality.APPLICATION_MODAL);
            neuerDialog.showAndWait();

            String eingabeArbeitgeber=controllerFenster.eingabeArbeitgeber;
            String eingabeArbeitnehmer=controllerFenster.eingabeArbeitnehmer;
            if (eingabeArbeitgeber.isEmpty()==false) {
                ku310Arbeitgeber=Long.parseLong(eingabeArbeitgeber);
            }
            if (eingabeArbeitnehmer.isEmpty()==false) {
                ku310Arbeitnehmer=Long.parseLong(eingabeArbeitnehmer);
            }
        }
        
        /*Protokoll-Pane initialisieren*/
        gpAUSWProtokoll.getChildren().clear();
        gpAUSWProtokoll.setVgap(5);
        gpAUSWProtokoll.setHgap(15);
        protokollOffset = 0;

        Label lblProtokoll = new Label("Auswertung gestartet");
        gpAUSWProtokoll.add(lblProtokoll, 0, protokollOffset);
        protokollOffset++;

        lDbBundle.openAll();

        blAbstimmung = new BlAbstimmung(lDbBundle);
        if (ParamSpezial.ku310(lDbBundle.clGlobalVar.mandant)) {
            blAbstimmung.anwesendeStimmenFuerSubtraktionsverfahrenArbeitnehmer=ku310Arbeitnehmer;
            blAbstimmung.anwesendeStimmenFuerSubtraktionsverfahrenArbeitgeber=ku310Arbeitgeber;
        }
         boolean brc = blAbstimmung.leseAktivenOderLokalenAbstimmungsblock(aktivDeaktivLokalAbstimmungsblock);
        if (brc == false) {
            lblProtokoll = new Label("Fehler beim Lesen aktiver Abstimmungsblock");
            gpAUSWProtokoll.add(lblProtokoll, 0, protokollOffset);
            protokollOffset++;
        }
        blAbstimmung.auswerten();

        lDbBundle.closeAll();

        lblProtokoll = new Label("Auswertung beendet");
        gpAUSWProtokoll.add(lblProtokoll, 0, protokollOffset);
        protokollOffset++;
    }

    /**
     * On btn AUSW archivieren.
     *
     * @param event the event
     */
    @FXML
    void onBtnAUSWArchivieren(ActionEvent event) {
        archivieren();
        archiviertNachAuswertung = true;
    }

    /**
     * Archivieren.
     */
    private void archivieren() {
        /*Protokoll-Pane initialisieren*/
        gpAUSWProtokoll.getChildren().clear();
        gpAUSWProtokoll.setVgap(5);
        gpAUSWProtokoll.setHgap(15);
        protokollOffset = 0;

        Label lblProtokoll = new Label("Archivierung gestartet");
        gpAUSWProtokoll.add(lblProtokoll, 0, protokollOffset);
        protokollOffset++;

        lDbBundle.openAll();

        blAbstimmung = new BlAbstimmung(lDbBundle);
        boolean brc = blAbstimmung.leseAktivenOderLokalenAbstimmungsblock(aktivDeaktivLokalAbstimmungsblock);
        if (brc == false) {
            lblProtokoll = new Label("Fehler beim Lesen aktiver Abstimmungsblock");
            gpAUSWProtokoll.add(lblProtokoll, 0, protokollOffset);
            protokollOffset++;
        }
        lDbBundle.dbWeisungMeldung.copyTable(blAbstimmung.aktiverAbstimmungsblock.ident);

        lDbBundle.closeAll();

        lblProtokoll = new Label("Archivierung beendet");
        gpAUSWProtokoll.add(lblProtokoll, 0, protokollOffset);
        protokollOffset++;
    }

    /**
     * On btn AUSW fertig.
     *
     * @param event the event
     */
    @FXML
    void onBtnAUSWFertig(ActionEvent event) {
        erledigtAblaufAuswahl();
        resetAblaufAuswahl();
    }

    /**
     * *************************************************************************
     * **************DruckenExportieren*****************************************.
     */

    /******************* Logik ************************************/

    private void ausfuehrenDRUEX() {

        pnDRUEX.setVisible(true);

        tfDRUEXLangfassung.setText("01");
        tfDRUEXKurzfassung.setText("02");
        tfDRUEXListe.setText("01");

        tfDRUEXPraesentationPDFNr.setText("01");//03
        tfDRUEXPraesentationPPNr.setText("01");//04
        tfDRUEXPraesentationPPNrListe.setText("01");
        tfDRUEXTexteBuehneNr.setText("01");//05
        tfDRUEXExportNr.setText("01");

        tfDRUEXPraesentationPDFDatei.setText("PraesPDF");
        tfDRUEXPraesentationPPDatei.setText("PraesPP");
        tfDRUEXPraesentationPPDateiListe.setText("PraesPPListe");
        tfDRUEXTexteBuehneDatei.setText("Buehne");
        tfDRUEXExportDatei.setText("ExportPP");

        tfDRUEXPraesentationPDFPfad.setText(ParamS.paramGeraet.lwPfadExportFuerPraesentation + "\\");
        tfDRUEXPraesentationPPPfad.setText(ParamS.paramGeraet.lwPfadExportFuerPraesentation + "\\");
        tfDRUEXPraesentationPPPfadListe.setText(ParamS.paramGeraet.lwPfadExportFuerPraesentation + "\\");
        tfDRUEXTexteBuehnePfad.setText(ParamS.paramGeraet.lwPfadExportFuerBuehnensystem + "\\");
        tfDRUEXExportPfad.setText(ParamS.paramGeraet.lwPfadExportExcelFuerPowerpoint + "\\");

        /*Protokoll-Pane initialisieren*/
        gpDRUEXProtokoll.getChildren().clear();
        gpDRUEXProtokoll.setVgap(5);
        gpDRUEXProtokoll.setHgap(15);
        protokollOffset = 0;

        lDbBundle.openAll();
        lDbBundle.dbKonfigAuswertung.readAll_Laeufe(KonstKonfigAuswertungArt.abstimmung);
        if (lDbBundle.dbKonfigAuswertung.anzErgebnis() > 0) {
            CbAllgemein cbAllgemein1 = new CbAllgemein(cbDRUEXJob);
            for (int i = 0; i < lDbBundle.dbKonfigAuswertung.anzErgebnis(); i++) {
                EclKonfigAuswertung lKonfiguAuswertung = lDbBundle.dbKonfigAuswertung.ergebnisPosition(i);
                String hString = Integer.toString(lKonfiguAuswertung.nr) + " "
                        + KonstKonfigAuswertungArt.getText(lKonfiguAuswertung.fuerFunktion);
                CbElement hElement = new CbElement();
                hElement.anzeige = hString;
                hElement.ident1 = lKonfiguAuswertung.nr;
                hElement.ident2 = lKonfiguAuswertung.fuerFunktion;

                cbAllgemein1.addElement(hElement);
            }

            lDbBundle.dbKonfigAuswertung.readAll();
            konfigAuswertungAlle = lDbBundle.dbKonfigAuswertung.ergebnisArray;
        }
        lDbBundle.closeAll();

    }

    /**
     * On btn DRUEX fertig.
     *
     * @param event the event
     */
    @FXML
    void onBtnDRUEXFertig(ActionEvent event) {
        erledigtAblaufAuswahl();
        resetAblaufAuswahl();
    }

    /**
     * On btn DRUEX kurzfassung.
     *
     * @param event the event
     */
    @FXML
    void onBtnDRUEXKurzfassung(ActionEvent event) {
        Label lblProtokoll = null;
        String hString = tfDRUEXKurzfassung.getText().trim();
        if (hString.isEmpty() || Integer.parseInt(hString) <= 0) {
            lblProtokoll = new Label("Formularnummer unzulässig!");
            gpDRUEXProtokoll.add(lblProtokoll, 0, protokollOffset);
            protokollOffset++;
            return;
        }
        int formNr = Integer.parseInt(hString);

        lblProtokoll = new Label("Kurzfassung " + hString + " gestartet");
        gpDRUEXProtokoll.add(lblProtokoll, 0, protokollOffset);
        protokollOffset++;

        /*AUsführen*/
        lDbBundle.openAll();

        BlAbstimmung blAbstimmung = new BlAbstimmung(lDbBundle);
        blAbstimmung.setzeAktivenAbstimmungsblockSortierenNachAuswertung();
        boolean brc = blAbstimmung.leseAktivenOderLokalenAbstimmungsblock(aktivDeaktivLokalAbstimmungsblock);
        if (brc == false) {
            lblProtokoll = new Label("Fehler beim Lesen Stimmblock");
            gpDRUEXProtokoll.add(lblProtokoll, 0, protokollOffset);
            protokollOffset++;
        } else {
            RpDrucken rpDrucken = new RpDrucken();
            rpDrucken.initClientDrucke();
            blAbstimmung.drucken(rpDrucken, 2, CaString.fuelleLinksNull(Integer.toString(formNr), 2), false);
        }

        lDbBundle.closeAll();

        lblProtokoll = new Label("Kurzfassung " + hString + " beendet");
        gpDRUEXProtokoll.add(lblProtokoll, 0, protokollOffset);
        protokollOffset++;

    }

    /**
     * On btn DRUEX langfassung.
     *
     * @param event the event
     */
    @FXML
    void onBtnDRUEXLangfassung(ActionEvent event) {
        Label lblProtokoll = null;
        String hString = tfDRUEXLangfassung.getText().trim();
        if (hString.isEmpty() || Integer.parseInt(hString) <= 0) {
            lblProtokoll = new Label("Formularnummer unzulässig!");
            gpDRUEXProtokoll.add(lblProtokoll, 0, protokollOffset);
            protokollOffset++;
            return;
        }
        int formNr = Integer.parseInt(hString);

        lblProtokoll = new Label("Langfassung " + hString + " gestartet");
        gpDRUEXProtokoll.add(lblProtokoll, 0, protokollOffset);
        protokollOffset++;

        /*AUsführen*/
        lDbBundle.openAll();

        BlAbstimmung blAbstimmung = new BlAbstimmung(lDbBundle);
        blAbstimmung.setzeAktivenAbstimmungsblockSortierenNachAuswertung();

        boolean brc = blAbstimmung.leseAktivenOderLokalenAbstimmungsblock(aktivDeaktivLokalAbstimmungsblock);
        if (brc == false) {
            lblProtokoll = new Label("Fehler beim Lesen Stimmblock");
            gpDRUEXProtokoll.add(lblProtokoll, 0, protokollOffset);
            protokollOffset++;
        } else {
            RpDrucken rpDrucken = new RpDrucken();
            rpDrucken.initClientDrucke();
            blAbstimmung.drucken(rpDrucken, 2, CaString.fuelleLinksNull(Integer.toString(formNr), 2), false);
        }

        lDbBundle.closeAll();

        lblProtokoll = new Label("Langfassung " + hString + " beendet");
        gpDRUEXProtokoll.add(lblProtokoll, 0, protokollOffset);
        protokollOffset++;

    }

    /**
     * On btn DRUEX liste.
     *
     * @param event the event
     */
    @FXML
    void onBtnDRUEXListe(ActionEvent event) {
        Label lblProtokoll = null;
        String hString = tfDRUEXListe.getText().trim();
        if (hString.isEmpty() || Integer.parseInt(hString) <= 0) {
            lblProtokoll = new Label("Formularnummer unzulässig!");
            gpDRUEXProtokoll.add(lblProtokoll, 0, protokollOffset);
            protokollOffset++;
            return;
        }
        int formNr = Integer.parseInt(hString);

        lblProtokoll = new Label("Liste " + hString + " gestartet");
        gpDRUEXProtokoll.add(lblProtokoll, 0, protokollOffset);
        protokollOffset++;

        /*AUsführen*/
        lDbBundle.openAll();

        BlAbstimmung blAbstimmung = new BlAbstimmung(lDbBundle);
        blAbstimmung.setzeAktivenAbstimmungsblockSortierenNachAuswertung();
        boolean brc = blAbstimmung.leseAktivenOderLokalenAbstimmungsblock(aktivDeaktivLokalAbstimmungsblock);
        if (brc == false) {
            lblProtokoll = new Label("Fehler beim Lesen Stimmblock");
            gpDRUEXProtokoll.add(lblProtokoll, 0, protokollOffset);
            protokollOffset++;
        } else {
            RpDrucken rpDrucken = new RpDrucken();
            rpDrucken.initClientDrucke();
            blAbstimmung.drucken(rpDrucken, 1, CaString.fuelleLinksNull(Integer.toString(formNr), 2), false);
            //       		blAbstimmung.drucken(1,  CaString.fuelleLinksNull(Integer.toString(formNr),2), 4, "D:\\", "Exp");
            //       		.drucken(1, CaString.fuelleLinksNull(Integer.toString(formNr),2));
        }

        lDbBundle.closeAll();

        lblProtokoll = new Label("Liste " + hString + " beendet");
        gpDRUEXProtokoll.add(lblProtokoll, 0, protokollOffset);
        protokollOffset++;

    }

    /**
     * On btn DRUEX ausfuehren job.
     *
     * @param event the event
     */
    @FXML
    void onBtnDRUEXAusfuehrenJob(ActionEvent event) {
        CbElement gewaehlteKonfig = null;
        Label lblProtokoll = null;
        RpDrucken rpDrucken = null;

        gewaehlteKonfig = cbDRUEXJob.getValue();
        if (gewaehlteKonfig == null) {
            lblProtokoll = new Label("Auswertungslauf nicht ausgewählt!");
            gpDRUEXProtokoll.add(lblProtokoll, 0, protokollOffset);
            protokollOffset++;
            return;
        }

        lDbBundle.openAll();
        for (int i = 0; i < konfigAuswertungAlle.length; i++) {
            EclKonfigAuswertung aktuelleAuswertung = konfigAuswertungAlle[i];
            if (aktuelleAuswertung.nr == gewaehlteKonfig.ident1
                    && aktuelleAuswertung.fuerFunktion == gewaehlteKonfig.ident2) {
                String hMeldeText = "Position " + Integer.toString(aktuelleAuswertung.positionInLauf) + " "
                        + KonstKonfigAuswertungFunktion.getText(aktuelleAuswertung.ausgeloesteFunktion);
                lblProtokoll = new Label(hMeldeText);
                gpDRUEXProtokoll.add(lblProtokoll, 0, protokollOffset);
                protokollOffset++;

                /*Initialisieren*/
                if (aktuelleAuswertung.ausgeloesteFunktion == KonstKonfigAuswertungFunktion.abstimmungVerleseblatt
                        || aktuelleAuswertung.ausgeloesteFunktion == KonstKonfigAuswertungFunktion.abstimmungUebersicht
                        || aktuelleAuswertung.ausgeloesteFunktion == KonstKonfigAuswertungFunktion.abstimmungZwischenblatt) {
                    rpDrucken = new RpDrucken();
                    rpDrucken.initClientDrucke();
                    if (aktuelleAuswertung.ausgabeWeg == KonstKonfigAuswertungAusgabeWeg.druckerAbfragen) {
                        rpDrucken.druckerWiederverwendet = 1;
                        rpDrucken.druckerWiederverwendetNummer = 999;
                    }
                    if (aktuelleAuswertung.ausgabeWeg == KonstKonfigAuswertungAusgabeWeg.wieVorherigerDrucker) {
                        rpDrucken.druckerWiederverwendet = 2;
                        rpDrucken.druckerWiederverwendetNummer = 999;
                        rpDrucken.druckerAbfragen = false;
                    }
                    if (aktuelleAuswertung.ausgabeWeg == KonstKonfigAuswertungAusgabeWeg.pfadMeetingOutput) {
                        rpDrucken.exportFormat = 7;
                        rpDrucken.exportDatei = aktuelleAuswertung.dateinamePdf;
                        rpDrucken.druckerAbfragen = false;
                    }
                }

                /*Funktion auslösen*/
                if (aktuelleAuswertung.ausgeloesteFunktion == KonstKonfigAuswertungFunktion.abstimmungVerleseblatt) {
                    BlAbstimmung blAbstimmung = new BlAbstimmung(lDbBundle);
                    blAbstimmung.setzeAktivenAbstimmungsblockSortierenNachAuswertung();
                    boolean brc = blAbstimmung.leseAktivenOderLokalenAbstimmungsblock(aktivDeaktivLokalAbstimmungsblock);
                    if (brc == false) {
                        lblProtokoll = new Label("Fehler beim Lesen Stimmblock");
                        gpDRUEXProtokoll.add(lblProtokoll, 0, protokollOffset);
                        protokollOffset++;
                        lDbBundle.closeAll();
                        return;
                    } else {
                        blAbstimmung.drucken(rpDrucken, 2,
                                CaString.fuelleLinksNull(Integer.toString(aktuelleAuswertung.ausgeloesteFormNr), 2),
                                false);
                        //		    			if (rc==false){
                        //		    		    	lblMeldung.setText("Druck abgebrochen");
                        //		    				lDbBundle.closeAll();
                        //		    				return;
                        //		    			}
                    }

                }
                if (aktuelleAuswertung.ausgeloesteFunktion == KonstKonfigAuswertungFunktion.abstimmungUebersicht) {
                    BlAbstimmung blAbstimmung = new BlAbstimmung(lDbBundle);
                    blAbstimmung.setzeAktivenAbstimmungsblockSortierenNachAuswertung();
                    boolean brc = blAbstimmung.leseAktivenOderLokalenAbstimmungsblock(aktivDeaktivLokalAbstimmungsblock);
                    if (brc == false) {
                        lblProtokoll = new Label("Fehler beim Lesen Stimmblock");
                        gpDRUEXProtokoll.add(lblProtokoll, 0, protokollOffset);
                        protokollOffset++;
                        lDbBundle.closeAll();
                        return;
                    } else {
                        blAbstimmung.drucken(rpDrucken, 1,
                                CaString.fuelleLinksNull(Integer.toString(aktuelleAuswertung.ausgeloesteFormNr), 2),
                                false);
                        //			       		blAbstimmung.drucken(1,  CaString.fuelleLinksNull(Integer.toString(formNr),2), 4, "D:\\", "Exp");
                        //			       		.drucken(1, CaString.fuelleLinksNull(Integer.toString(formNr),2));
                    }

                    //					String pText1=aktuelleAuswertung.textFuerFormular1;
                    //					String pText2=aktuelleAuswertung.textFuerFormular2;

                }
                if (aktuelleAuswertung.ausgeloesteFunktion == KonstKonfigAuswertungFunktion.abstimmungZwischenblatt) {
                    rpDrucken.initFormular(lDbBundle);
                    RpVariablen rpVariablen = new RpVariablen(lDbBundle);
                    rpVariablen = new RpVariablen(lDbBundle);
                    rpVariablen.abstimmungZwischenblatt(
                            CaString.fuelleLinksNull(Integer.toString(aktuelleAuswertung.ausgeloesteFormNr), 2),
                            rpDrucken);

                    rpDrucken.startFormular(/*true*/);
                    rpVariablen.fuelleVariable(rpDrucken, "Ueberschrift.text1", aktuelleAuswertung.textFuerFormular1);
                    rpVariablen.fuelleVariable(rpDrucken, "Ueberschrift.text2", aktuelleAuswertung.textFuerFormular2);
                    rpDrucken.druckenFormular();
                    rpDrucken.endeFormular();
                }
                if (aktuelleAuswertung.ausgeloesteFunktion == KonstKonfigAuswertungFunktion.abstimmungExport) {
                    BlAbstimmung blAbstimmung = new BlAbstimmung(lDbBundle);
                    blAbstimmung.setzeAktivenAbstimmungsblockSortierenNachAuswertung();

                    boolean brc = blAbstimmung.leseAktivenOderLokalenAbstimmungsblock(aktivDeaktivLokalAbstimmungsblock);
                    if (brc == false) {
                        lblProtokoll = new Label("Fehler beim Lesen Stimmblock");
                        gpDRUEXProtokoll.add(lblProtokoll, 0, protokollOffset);
                        protokollOffset++;
                        lDbBundle.closeAll();
                        return;
                    } else {
                        blAbstimmung.exportVerzeichnis = tfDRUEXExportPfad.getText();
                        blAbstimmung.exportDateiname = tfDRUEXExportDatei.getText();
                        blAbstimmung.drucken(null, 3, "", false);
                    }
                }
            }
        }

        lDbBundle.closeAll();
    }

    /**
     * Exort einer CSV-Datei für Power-Point-Import.
     *
     * @param event the event
     */
    @FXML
    void onBtnDRUEXExportFuerPP(ActionEvent event) {
        Label lblProtokoll = null;

        lblProtokoll = new Label("Export für PP gestartet");
        gpDRUEXProtokoll.add(lblProtokoll, 0, protokollOffset);
        protokollOffset++;

        /*AUsführen*/
        lDbBundle.openAll();

        BlAbstimmung blAbstimmung = new BlAbstimmung(lDbBundle);
        blAbstimmung.setzeAktivenAbstimmungsblockSortierenNachAuswertung();

        boolean brc = blAbstimmung.leseAktivenOderLokalenAbstimmungsblock(aktivDeaktivLokalAbstimmungsblock);
        if (brc == false) {
            lblProtokoll = new Label("Fehler beim Lesen Stimmblock");
            gpDRUEXProtokoll.add(lblProtokoll, 0, protokollOffset);
            protokollOffset++;
        } else {
            blAbstimmung.exportVerzeichnis = tfDRUEXExportPfad.getText();
            blAbstimmung.exportDateiname = tfDRUEXExportDatei.getText();
            blAbstimmung.drucken(null, 3, "", false);
            //       		blAbstimmung.drucken(1,  CaString.fuelleLinksNull(Integer.toString(formNr),2), 4, "D:\\", "Exp");
            //       		.drucken(1, CaString.fuelleLinksNull(Integer.toString(formNr),2));
        }

        lDbBundle.closeAll();

        lblProtokoll = new Label("Export für PP beendet");
        gpDRUEXProtokoll.add(lblProtokoll, 0, protokollOffset);
        protokollOffset++;

    }

    /**
     * Export einer Datei mit den erfolgten Stimmabgaben je Karte / Sammelkarte.
     *
     * @param event the event
     */
    @FXML
    void onBtnDRUEXExport(ActionEvent event) {
        Label lblProtokoll = null;

        lblProtokoll = new Label("Export Stimmabgaben gestartet");
        gpDRUEXProtokoll.add(lblProtokoll, 0, protokollOffset);
        protokollOffset++;

        /*AUsführen*/
        lDbBundle.openAll();

        BlAbstimmung blAbstimmung = new BlAbstimmung(lDbBundle);
        blAbstimmung.setzeAktivenAbstimmungsblockSortierenNachAuswertung();
        
        boolean brc = blAbstimmung.leseAktivenOderLokalenAbstimmungsblock(aktivDeaktivLokalAbstimmungsblock);
        if (brc == false) {
            lblProtokoll = new Label("Fehler beim Lesen Stimmblock");
            gpDRUEXProtokoll.add(lblProtokoll, 0, protokollOffset);
            protokollOffset++;
        } else {
            blAbstimmung.fuelleAbstimmungsverhalten(1, true, true);
            
//            blAbstimmung.exportieren();
        }

        lDbBundle.closeAll();

        lblProtokoll = new Label("Export Stimmabgaben beendet");
        gpDRUEXProtokoll.add(lblProtokoll, 0, protokollOffset);
        protokollOffset++;

    }

    /**
     * On btn DRUEX praesentation PDF.
     *
     * @param event the event
     */
    @FXML
    void onBtnDRUEXPraesentationPDF(ActionEvent event) {
        Label lblProtokoll = null;
        String hString = tfDRUEXPraesentationPDFNr.getText().trim();
        if (hString.isEmpty() || Integer.parseInt(hString) <= 0) {
            lblProtokoll = new Label("Formularnummer unzulässig!");
            gpDRUEXProtokoll.add(lblProtokoll, 0, protokollOffset);
            protokollOffset++;
            return;
        }
        int formNr = Integer.parseInt(hString);

        lblProtokoll = new Label("DruckPräsentation PDF" + hString + " gestartet");
        gpDRUEXProtokoll.add(lblProtokoll, 0, protokollOffset);
        protokollOffset++;

        /*AUsführen*/
        lDbBundle.openAll();

        BlAbstimmung blAbstimmung = new BlAbstimmung(lDbBundle);
        blAbstimmung.setzeAktivenAbstimmungsblockSortierenNachAuswertung();
        boolean brc = blAbstimmung.leseAktivenOderLokalenAbstimmungsblock(aktivDeaktivLokalAbstimmungsblock);
        if (brc == false) {
            lblProtokoll = new Label("Fehler beim Lesen Stimmblock");
            gpDRUEXProtokoll.add(lblProtokoll, 0, protokollOffset);
            protokollOffset++;
        } else {
            String dateiname = tfDRUEXPraesentationPDFDatei.getText();
            String pfad = tfDRUEXPraesentationPDFPfad.getText();

            RpDrucken rpDrucken = new RpDrucken();
            rpDrucken.initClientDrucke();
            rpDrucken.exportFormat = 1;
            rpDrucken.exportAnzeigen = false;
            rpDrucken.exportDateinameAbfragen = false;
            rpDrucken.exportDatei = dateiname;
            rpDrucken.exportVerzeichnis = pfad;
            rpDrucken.druckerAbfragen = false;

            blAbstimmung.drucken(rpDrucken, 2, CaString.fuelleLinksNull(Integer.toString(formNr), 2), false);
        }

        lDbBundle.closeAll();

        lblProtokoll = new Label("DruckPräsentation PDF" + hString + " beendet");
        gpDRUEXProtokoll.add(lblProtokoll, 0, protokollOffset);
        protokollOffset++;

    }

    /**
     * On btn DRUEX praesentation PP.
     *
     * @param event the event
     */
    @FXML
    void onBtnDRUEXPraesentationPP(ActionEvent event) {
        Label lblProtokoll = null;
        String hString = tfDRUEXPraesentationPPNr.getText().trim();
        if (hString.isEmpty() || Integer.parseInt(hString) <= 0) {
            lblProtokoll = new Label("Formularnummer unzulässig!");
            gpDRUEXProtokoll.add(lblProtokoll, 0, protokollOffset);
            protokollOffset++;
            return;
        }
        int formNr = Integer.parseInt(hString);

        lblProtokoll = new Label("DruckPräsentation Power Point" + hString + " gestartet");
        gpDRUEXProtokoll.add(lblProtokoll, 0, protokollOffset);
        protokollOffset++;

        /*AUsführen*/
        lDbBundle.openAll();

        BlAbstimmung blAbstimmung = new BlAbstimmung(lDbBundle);
        blAbstimmung.setzeAktivenAbstimmungsblockSortierenNachAuswertung();
        boolean brc = blAbstimmung.leseAktivenOderLokalenAbstimmungsblock(aktivDeaktivLokalAbstimmungsblock);
        if (brc == false) {
            lblProtokoll = new Label("Fehler beim Lesen Stimmblock");
            gpDRUEXProtokoll.add(lblProtokoll, 0, protokollOffset);
            protokollOffset++;
        } else {
            String dateiname = tfDRUEXPraesentationPPDatei.getText();
            String pfad = tfDRUEXPraesentationPPPfad.getText();

            RpDrucken rpDrucken = new RpDrucken();
            rpDrucken.initClientDrucke();
            rpDrucken.exportFormat = 3;
            rpDrucken.exportAnzeigen = false;
            rpDrucken.exportDateinameAbfragen = false;
            rpDrucken.exportDatei = dateiname;
            rpDrucken.exportVerzeichnis = pfad;
            rpDrucken.druckerAbfragen = false;

            blAbstimmung.drucken(rpDrucken, 2, CaString.fuelleLinksNull(Integer.toString(formNr), 2), true);
        }

        lDbBundle.closeAll();

        lblProtokoll = new Label("DruckPräsentation PowerPoint" + hString + " beendet");
        gpDRUEXProtokoll.add(lblProtokoll, 0, protokollOffset);
        protokollOffset++;

    }

    /**
     * On btn DRUEX praesentation PP liste.
     *
     * @param event the event
     */
    @FXML
    void onBtnDRUEXPraesentationPPListe(ActionEvent event) {
        Label lblProtokoll = null;
        String hString = tfDRUEXPraesentationPPNrListe.getText().trim();
        if (hString.isEmpty() || Integer.parseInt(hString) <= 0) {
            lblProtokoll = new Label("Formularnummer unzulässig!");
            gpDRUEXProtokoll.add(lblProtokoll, 0, protokollOffset);
            protokollOffset++;
            return;
        }
        int formNr = Integer.parseInt(hString);

        lblProtokoll = new Label("DruckPräsentation Power Point Liste " + hString + " gestartet");
        gpDRUEXProtokoll.add(lblProtokoll, 0, protokollOffset);
        protokollOffset++;

        /*AUsführen*/
        lDbBundle.openAll();

        BlAbstimmung blAbstimmung = new BlAbstimmung(lDbBundle);
        blAbstimmung.setzeAktivenAbstimmungsblockSortierenNachAuswertung();
        boolean brc = blAbstimmung.leseAktivenOderLokalenAbstimmungsblock(aktivDeaktivLokalAbstimmungsblock);
        if (brc == false) {
            lblProtokoll = new Label("Fehler beim Lesen Stimmblock");
            gpDRUEXProtokoll.add(lblProtokoll, 0, protokollOffset);
            protokollOffset++;
        } else {
            String dateiname = tfDRUEXPraesentationPPDateiListe.getText();
            String pfad = tfDRUEXPraesentationPPPfadListe.getText();

            RpDrucken rpDrucken = new RpDrucken();
            rpDrucken.initClientDrucke();
            rpDrucken.exportFormat = 3;
            rpDrucken.exportAnzeigen = false;
            rpDrucken.exportDateinameAbfragen = false;
            rpDrucken.exportDatei = dateiname;
            rpDrucken.exportVerzeichnis = pfad;
            rpDrucken.druckerAbfragen = false;

            blAbstimmung.drucken(rpDrucken, 1, CaString.fuelleLinksNull(Integer.toString(formNr), 2), true);
        }

        lDbBundle.closeAll();

        lblProtokoll = new Label("DruckPräsentation PowerPoint Liste " + hString + " beendet");
        gpDRUEXProtokoll.add(lblProtokoll, 0, protokollOffset);
        protokollOffset++;

    }

    /**
     * On btn DRUEX texte buehne.
     *
     * @param event the event
     */
    @FXML
    void onBtnDRUEXTexteBuehne(ActionEvent event) {
        Label lblProtokoll = null;
        String hString = tfDRUEXTexteBuehneNr.getText().trim();
        if (hString.isEmpty() || Integer.parseInt(hString) <= 0) {
            lblProtokoll = new Label("Formularnummer unzulässig!");
            gpDRUEXProtokoll.add(lblProtokoll, 0, protokollOffset);
            protokollOffset++;
            return;
        }
        int formNr = Integer.parseInt(hString);

        lblProtokoll = new Label("Bühnentexte " + hString + " gestartet");
        gpDRUEXProtokoll.add(lblProtokoll, 0, protokollOffset);
        protokollOffset++;

        /*AUsführen*/
        lDbBundle.openAll();

        BlAbstimmung blAbstimmung = new BlAbstimmung(lDbBundle);
        blAbstimmung.setzeAktivenAbstimmungsblockSortierenNachAuswertung();
        boolean brc = blAbstimmung.leseAktivenOderLokalenAbstimmungsblock(aktivDeaktivLokalAbstimmungsblock);
        if (brc == false) {
            lblProtokoll = new Label("Fehler beim Lesen Stimmblock");
            gpDRUEXProtokoll.add(lblProtokoll, 0, protokollOffset);
            protokollOffset++;
        } else {
            String dateiname = tfDRUEXTexteBuehneDatei.getText();
            String pfad = tfDRUEXTexteBuehnePfad.getText();

            RpDrucken rpDrucken = new RpDrucken();
            rpDrucken.initClientDrucke();
            rpDrucken.exportFormat = 1;
            rpDrucken.exportAnzeigen = false;
            rpDrucken.exportDateinameAbfragen = false;
            rpDrucken.exportDatei = dateiname;
            rpDrucken.exportVerzeichnis = pfad;
            rpDrucken.druckerAbfragen = false;

            blAbstimmung.drucken(rpDrucken, 2, CaString.fuelleLinksNull(Integer.toString(formNr), 2), false);
        }

        lDbBundle.closeAll();

        lblProtokoll = new Label("Bühnentexte " + hString + " beendet");
        gpDRUEXProtokoll.add(lblProtokoll, 0, protokollOffset);
        protokollOffset++;

    }

    /**
     * *************************************************************************
     * **************Delay Abstimmung
     * Anfang*****************************************.
     *
     * @param pDelaystufe the delaystufe
     */

    private void delaySpeichern(int pDelaystufe) {
        lDbBundle.openAll();

        EclParameter lEclParameter = new EclParameter();
        lEclParameter.ident = 513;
        lEclParameter.mandant = lDbBundle.clGlobalVar.mandant;
        lDbBundle.dbParameter.read(lEclParameter);
        lEclParameter = lDbBundle.dbParameter.ergebnisPosition(0);
        //    	if (lEclParameter==null){
        //    		lEclParameter=new EclParameter();
        //    		lEclParameter.ident=513;
        //    	}
        lEclParameter.wert = Integer.toString(pDelaystufe);
        lDbBundle.dbParameter.update(lEclParameter);

        lDbBundle.closeAll();
    }

    /**
     * Ausfuehren delay beginn abstimmung.
     */
    private void ausfuehrenDelayBeginnAbstimmung() {

        pnDELB.setVisible(true);

    }

    /**
     * On btn DELB starten.
     *
     * @param event the event
     */
    @FXML
    void onBtnDELBStarten(ActionEvent event) {
        delaySpeichern(1);
        erledigtAblaufAuswahl();
        resetAblaufAuswahl();
    }

    /**
     * *************************************************************************
     * **************Delay Abstimmung Ende*****************************************.
     */

    private void ausfuehrenDelayEndeAbstimmung() {

        pnDELE.setVisible(true);

    }

    /**
     * On btn DELE starten.
     *
     * @param event the event
     */
    @FXML
    void onBtnDELEStarten(ActionEvent event) {
        delaySpeichern(2);
        erledigtAblaufAuswahl();
        resetAblaufAuswahl();
    }

    /**
     * *************************************************************************
     * **************Delay Auflösen*****************************************.
     */

    private void ausfuehrenDelayAufloesen() {

        pnDELL.setVisible(true);

    }

    /**
     * On btn DELL starten.
     *
     * @param event the event
     */
    @FXML
    void onBtnDELLStarten(ActionEvent event) {
        lDbBundle.openAll();
        BlDelayedAufloesen blDelayedAufloesen = new BlDelayedAufloesen(lDbBundle);
        blDelayedAufloesen.aufloesenDurchfueren();

        lDbBundle.closeAll();
        erledigtAblaufAuswahl();
        resetAblaufAuswahl();
    }

    /**
     * *************************************************************************
     * **************Delay Ausschalten*****************************************.
     */

    private void ausfuehrenDelayAus() {

        pnDELA.setVisible(true);

    }

    /**
     * On btn DELA starten.
     *
     * @param event the event
     */
    @FXML
    void onBtnDELAStarten(ActionEvent event) {
        delaySpeichern(0);
        erledigtAblaufAuswahl();
        resetAblaufAuswahl();
    }

    /**
     * ******************************************************************************.
     */
    /**************
     * MONEK Monitor Eintrittskarten************************************
     * 
     */

    BlAbstimmung blMONEKAbstimmung = null;

    /**
     * Ausfuehren monitor EK.
     */
    private void ausfuehrenMonitorEK() {

        lDbBundle.openAll();
        blMONEKAbstimmung = new BlAbstimmung(lDbBundle);
        blMONEKAbstimmung.leseAktivenOderLokalenAbstimmungsblock(aktivDeaktivLokalAbstimmungsblock);
        lDbBundle.closeAll();

        pnMONEK.setVisible(true);

    }

    /**
     * On btn MONEK fertig.
     *
     * @param event the event
     */
    @FXML
    void onBtnMONEKFertig(ActionEvent event) {
        erledigtAblaufAuswahl();
        resetAblaufAuswahl();

    }

    /**
     * On btn MONEK refresh aktionaersdaten.
     *
     * @param event the event
     */
    @FXML
    void onBtnMONEKRefreshAktionaersdaten(ActionEvent event) {
        lDbBundle.openAll();

        lDbBundle.dbAbstimmungsmonitorEK.read_all();
        int anzAktionaere = lDbBundle.dbAbstimmungsmonitorEK.anzErgebnis();
        for (int i = 0; i < anzAktionaere; i++) {
            boolean meldungGefunden = false;
            EclAbstimmungsmonitorEK eclAbstimmungsmonitorEK = lDbBundle.dbAbstimmungsmonitorEK.ergebnisPosition(i);
            EclZutrittsIdent lZutrittsIdent = new EclZutrittsIdent();
            lZutrittsIdent.zutrittsIdent = eclAbstimmungsmonitorEK.zutrittsIdent;
            lZutrittsIdent.zutrittsIdentNeben = eclAbstimmungsmonitorEK.zutrittsIdentNeben;
            lDbBundle.dbZutrittskarten.read(lZutrittsIdent, 2);
            if (lDbBundle.dbZutrittskarten.anzErgebnis() == 0) {
                meldungGefunden = false;
            } else {
                int lMeldungsIdent = lDbBundle.dbZutrittskarten.ergebnisPosition(0).meldungsIdentAktionaer;
                if (lMeldungsIdent == 0) {
                    meldungGefunden = false;
                } else {
                    lDbBundle.dbMeldungen.leseZuIdent(lMeldungsIdent);
                    if (lDbBundle.dbMeldungen.meldungenArray.length == 0) {
                        meldungGefunden = false;
                    } else {
                        meldungGefunden = true;
                        EclMeldung lMeldung = lDbBundle.dbMeldungen.meldungenArray[0];
                        eclAbstimmungsmonitorEK.stimmkarte = lMeldung.stimmkarte;
                        eclAbstimmungsmonitorEK.meldeIdent = lMeldungsIdent;
                        eclAbstimmungsmonitorEK.meldeAktionaer = lMeldung.name;
                        eclAbstimmungsmonitorEK.meldeVertreter = lMeldung.vertreterName;
                        eclAbstimmungsmonitorEK.meldeStimmen = lMeldung.stimmen;
                        eclAbstimmungsmonitorEK.statusPraesenz = lMeldung.statusPraesenz_Delayed;
                        lDbBundle.dbAbstimmungsmonitorEK.update(eclAbstimmungsmonitorEK);
                    }

                }
            }

            if (meldungGefunden == false) {
                /*ZutrittsIdent nicht vorhanden*/
                eclAbstimmungsmonitorEK.stimmkarte = "";
                eclAbstimmungsmonitorEK.meldeIdent = -1;
                eclAbstimmungsmonitorEK.meldeAktionaer = "";
                eclAbstimmungsmonitorEK.meldeVertreter = "";
                eclAbstimmungsmonitorEK.meldeStimmen = 0;
                eclAbstimmungsmonitorEK.statusPraesenz = 0;
                lDbBundle.dbAbstimmungsmonitorEK.update(eclAbstimmungsmonitorEK);
            }

        }

        lDbBundle.closeAll();

        gpMONEKAbstimmungsverhalten.getChildren().clear();
        gpMONEKAbstimmungsverhalten.setVgap(5);
        gpMONEKAbstimmungsverhalten.setHgap(15);

        Label lblProtokoll = null;
        lblProtokoll = new Label(anzAktionaere + " Aktionärsdaten refresh durchgeführt");
        gpMONEKAbstimmungsverhalten.add(lblProtokoll, 0, 0);

    }

    /**
     * On btn MONEK refresh alle stimmabgaben.
     *
     * @param event the event
     */
    @FXML
    void onBtnMONEKRefreshAlleStimmabgaben(ActionEvent event) {

        lDbBundle.openAll();

        gpMONEKAbstimmungsverhalten = new GridPane();
        gpMONEKAbstimmungsverhalten.setVgap(5);
        gpMONEKAbstimmungsverhalten.setHgap(15);

        int anzahlAbstimmungMeldung = lDbBundle.dbAbstimmungMeldung.readinit_all(true);
        int anzahlAbstimmungDieserblock = 0;

        Label lbU0 = new Label("MIdent");
        gpMONEKAbstimmungsverhalten.add(lbU0, 0, 0);
        Label lbU1 = new Label("Abstimmung");
        gpMONEKAbstimmungsverhalten.add(lbU1, 1, 0);
        Label lbU2 = new Label("Stimmen");
        gpMONEKAbstimmungsverhalten.add(lbU2, 2, 0);
        Label lbU3 = new Label("Aktiv");
        gpMONEKAbstimmungsverhalten.add(lbU3, 3, 0);
        int i = 2;
        while (anzahlAbstimmungMeldung > 0 && lDbBundle.dbAbstimmungMeldung.readnext_all()) {

            EclAbstimmungMeldung eclAbstimmungMeldungAktionaer = lDbBundle.dbAbstimmungMeldung.ergebnisGefunden(0);

            Label lbMeldeIdent = new Label(Integer.toString(eclAbstimmungMeldungAktionaer.meldungsIdent));
            gpMONEKAbstimmungsverhalten.add(lbMeldeIdent, 0, i + 1);

            /*Stimmen aus Abstimmungsverhalten anzeigen -3*/
            Label lbAbstimmungStimmen = new Label(Long.toString(eclAbstimmungMeldungAktionaer.stimmen));
            gpMONEKAbstimmungsverhalten.add(lbAbstimmungStimmen, 2, i + 1);

            String anzeigeAbstimmung = "";
            boolean abstimmungAbgegeben = false;
            for (int i1 = 0; i1 < blMONEKAbstimmung.getAnzAbstimmungenInAktivemAbstimmungsblock(); i1++) {
                if (!blMONEKAbstimmung.istUeberschrift(i1)) {
                    int abgegebeneStimmart = blMONEKAbstimmung.liefereAktuelleMarkierungZuAbstimmungsPosition(i1,
                            eclAbstimmungMeldungAktionaer);
                    anzeigeAbstimmung = anzeigeAbstimmung + KonstStimmart.getTextKurz(abgegebeneStimmart);
                    if (abgegebeneStimmart == KonstStimmart.ja ||
                            abgegebeneStimmart == KonstStimmart.nein ||
                            abgegebeneStimmart == KonstStimmart.enthaltung ||
                            abgegebeneStimmart == KonstStimmart.ungueltig) {
                        abstimmungAbgegeben = true;
                    }
                } else {
                    anzeigeAbstimmung = anzeigeAbstimmung + ".";
                }
            }
            if (abstimmungAbgegeben) {
                anzahlAbstimmungDieserblock++;
            }

            Label lbAnzeigeAbstimmung = new Label(anzeigeAbstimmung);
            gpMONEKAbstimmungsverhalten.add(lbAnzeigeAbstimmung, 1, i + 1);

            Label lbMeldeAktiv = new Label(Integer.toString(eclAbstimmungMeldungAktionaer.aktiv));
            gpMONEKAbstimmungsverhalten.add(lbMeldeAktiv, 3, i + 1);

            i++;

        }
        Label lAnzahlInsgesamt = new Label("Anzahl bei Allen Blöcken: " + Integer.toString(anzahlAbstimmungMeldung));
        gpMONEKAbstimmungsverhalten.add(lAnzahlInsgesamt, 1, 2);

        Label lAnzahlDieserBlock = new Label(
                "Anzahl bei diesem Block: " + Integer.toString(anzahlAbstimmungDieserblock));
        gpMONEKAbstimmungsverhalten.add(lAnzahlDieserBlock, 1, 1);

        scpnMONEK.setContent(gpMONEKAbstimmungsverhalten);
        lDbBundle.closeAll();

    }

    /**
     * On btn MONEK refresh abstimmungsverhalten.
     *
     * @param event the event
     */
    @FXML
    void onBtnMONEKRefreshAbstimmungsverhalten(ActionEvent event) {
        lDbBundle.openAll();

        gpMONEKAbstimmungsverhalten = new GridPane();
        gpMONEKAbstimmungsverhalten.setVgap(5);
        gpMONEKAbstimmungsverhalten.setHgap(15);

        lDbBundle.dbAbstimmungsmonitorEK.read_all();
        int anzAktionaere = lDbBundle.dbAbstimmungsmonitorEK.anzErgebnis();

        Label lbU0 = new Label("EKNr");
        gpMONEKAbstimmungsverhalten.add(lbU0, 0, 0);
        Label lbU1 = new Label("MIdent");
        gpMONEKAbstimmungsverhalten.add(lbU1, 1, 0);
        Label lbU2 = new Label("Abstimmung");
        gpMONEKAbstimmungsverhalten.add(lbU2, 2, 0);
        Label lbU3 = new Label("Stimmen1");
        gpMONEKAbstimmungsverhalten.add(lbU3, 3, 0);
        Label lbU4 = new Label("Stimmen2");
        gpMONEKAbstimmungsverhalten.add(lbU4, 4, 0);
        Label lbU5 = new Label("SKNr");
        gpMONEKAbstimmungsverhalten.add(lbU5, 5, 0);
        Label lbU6 = new Label("Anw.KZ");
        gpMONEKAbstimmungsverhalten.add(lbU6, 6, 0);
        Label lbU7 = new Label("Aktionär");
        gpMONEKAbstimmungsverhalten.add(lbU7, 7, 0);
        Label lbU8 = new Label("Vertreter");
        gpMONEKAbstimmungsverhalten.add(lbU8, 8, 0);

        for (int i = 0; i < anzAktionaere; i++) {
            EclAbstimmungsmonitorEK eclAbstimmungsmonitorEK = lDbBundle.dbAbstimmungsmonitorEK.ergebnisPosition(i);

            Label lbZutrittsIdent = new Label(eclAbstimmungsmonitorEK.zutrittsIdent);
            gpMONEKAbstimmungsverhalten.add(lbZutrittsIdent, 0, i + 1);

            if (eclAbstimmungsmonitorEK.meldeIdent == -1) {
                Label lbNichtVorhanden = new Label("EK nicht vorhanden");
                gpMONEKAbstimmungsverhalten.add(lbNichtVorhanden, 2, i + 1);

            } else {
                Label lbMeldeIdent = new Label(Integer.toString(eclAbstimmungsmonitorEK.meldeIdent));
                gpMONEKAbstimmungsverhalten.add(lbMeldeIdent, 1, i + 1);

                lDbBundle.dbAbstimmungMeldung.lese_Ident(eclAbstimmungsmonitorEK.meldeIdent);
                if (lDbBundle.dbAbstimmungMeldung.anzErgebnisGefunden() == 0) {
                    Label lbNichtVorhanden = new Label("keine Abstimmung vorhanden");
                    gpMONEKAbstimmungsverhalten.add(lbNichtVorhanden, 2, i + 1);
                } else {
                    EclAbstimmungMeldung eclAbstimmungMeldung = lDbBundle.dbAbstimmungMeldung.ergebnisGefunden(0);

                    /*Stimmen aus Abstimmungsverhalten anzeigen -3*/
                    Label lbAbstimmungStimmen = new Label(Long.toString(eclAbstimmungMeldung.stimmen));
                    gpMONEKAbstimmungsverhalten.add(lbAbstimmungStimmen, 3, i + 1);

                    /*Abstimmungsverhalten anzeigen -2 */
                    String anzeigeAbstimmung = "";
                    for (int i1 = 0; i1 < blMONEKAbstimmung.getAnzAbstimmungenInAktivemAbstimmungsblock(); i1++) {
                        if (!blMONEKAbstimmung.istUeberschrift(i1)) {
                            int abgegebeneStimmart = blMONEKAbstimmung
                                    .liefereAktuelleMarkierungZuAbstimmungsPosition(i1, eclAbstimmungMeldung);
                            anzeigeAbstimmung = anzeigeAbstimmung + KonstStimmart.getTextKurz(abgegebeneStimmart);
                        } else {
                            anzeigeAbstimmung = anzeigeAbstimmung + ".";
                        }
                    }

                    Label lbAnzeigeAbstimmung = new Label(anzeigeAbstimmung);
                    gpMONEKAbstimmungsverhalten.add(lbAnzeigeAbstimmung, 2, i + 1);

                }

                /*Restliche Daten aus Meldung anzeigen*/

                Label lbMeldeStimmen = new Label(Long.toString(eclAbstimmungsmonitorEK.meldeStimmen));
                gpMONEKAbstimmungsverhalten.add(lbMeldeStimmen, 4, i + 1);

                Label lbStimmkarte = new Label(eclAbstimmungsmonitorEK.stimmkarte);
                gpMONEKAbstimmungsverhalten.add(lbStimmkarte, 5, i + 1);

                Label lbPraesent = new Label();
                switch (eclAbstimmungsmonitorEK.statusPraesenz) {
                case 0: {
                    lbPraesent.setText("nicht anwesend");
                    break;
                }
                case 1: {
                    lbPraesent.setText("anwesend");
                    break;
                }
                case 2: {
                    lbPraesent.setText("war anwesend");
                    break;
                }
                case 4: {
                    lbPraesent.setText("anwesend in Sammel");
                    break;
                }
                }
                gpMONEKAbstimmungsverhalten.add(lbPraesent, 6, i + 1);

                Label lbMeldeAktionaer = new Label(eclAbstimmungsmonitorEK.meldeAktionaer);
                gpMONEKAbstimmungsverhalten.add(lbMeldeAktionaer, 7, i + 1);

                Label lbMeldeVertreter = new Label(eclAbstimmungsmonitorEK.meldeVertreter);
                gpMONEKAbstimmungsverhalten.add(lbMeldeVertreter, 8, i + 1);

            }

        }

        scpnMONEK.setContent(gpMONEKAbstimmungsverhalten);
        lDbBundle.closeAll();

    }

    /**
     * **************Allgemeine übergreifende
     * Funktionen***************************************.
     */

    private BlNummernformen allgBlNummernformen = null;

    /** The rc nummernform. */
    private int rcNummernform = 1;

    /** The rc nummernform text. */
    private String rcNummernformText = "";

    /** The rc meldung. */
    private EclMeldung rcMeldung = null;

    /**
     * Eingegebene Nummer dekodieren true = erfolgreich, false=Fehler.
     *
     * @param pEingabeString the eingabe string
     * @return true, if successful
     */
    private boolean dekodiereNummernform(String pEingabeString) {
        rcNummernform = 1;
        rcNummernformText = "";

        allgBlNummernformen = new BlNummernformen(lDbBundle);
        rcNummernform = allgBlNummernformen.dekodiere(pEingabeString, KonstKartenklasse.unbekannt);
        if (rcNummernform == CaFehler.pmNummernformUngueltig
                || rcNummernform == CaFehler.pmNummernformAktionsnummerUngueltig) {
            rcNummernformText = "Fehler: Nummernform ungültig!";
            return false;
        }
        if (rcNummernform == CaFehler.pfNichtEindeutig) {
            rcNummernformText = "Fehler: Nummern nicht eindeutig - bitte vollständige Kodierung eingeben!";
            return false;
        }
        if (rcNummernform == CaFehler.pmNummernformMandantUngueltig) {
            rcNummernformText = "Fehler: Falsche Mandantennummer!";
            return false;
        }
        if (rcNummernform == CaFehler.pfXyNichtImZulaessigenNummernkreis) {
            rcNummernformText = "Fehler: Nummer nicht im zulässigen Nummernkreis!";
            return false;
        }
        if (allgBlNummernformen.rcKartenklasse == KonstKartenklasse.eintrittskartennummer
                && ParamS.param.paramAbstimmungParameter.beiAbstimmungEintrittskartennummerZulaessig != 1) {
            rcNummernformText = "Fehler: Eintrittskarte nicht für Abstimmung verwendbar!";
            return false;
        }
        if (allgBlNummernformen.rcKartenklasse == KonstKartenklasse.gastkartennummer) {
            rcNummernformText = "Fehler: Gastkarte nicht für Abstimmung verwendbar!";
            return false;
        }

        return true;
    }

    /**
     * In allgBlNummernformen eingelesene Nummer einlesen lDbBundle wird außerhalb
     * dieser Funktion geöffnet/geschlossen!
     * 
     * false = keine Weiterverarbeitung möglich true=Weiterverarbeitung möglich;
     * ggf. in rcNummernformText bzw. rcNummernform Warnung!
     *
     * @return true, if successful
     */
    private boolean einlesenMeldung() {
        rcNummernform = 1;
        rcNummernformText = "";

        int meldungsIdent = 0; /*Ident der Meldung, für die die Stimme abgegeben wird*/
        String identifikationsNummer = ""; /*Verwendete Identifikationsnummer zum Speichern*/
        if (allgBlNummernformen.rcKartenklasse == KonstKartenklasse.eintrittskartennummer) {
            EclZutrittsIdent lZutrittsIdent = new EclZutrittsIdent();
            lZutrittsIdent.zutrittsIdent = allgBlNummernformen.rcIdentifikationsnummer.get(0);
            lZutrittsIdent.zutrittsIdentNeben = allgBlNummernformen.rcIdentifikationsnummerNeben.get(0);
            identifikationsNummer = lZutrittsIdent.zutrittsIdent + "-" + lZutrittsIdent.zutrittsIdentNeben;
            rcNummernform = blAbstimmung.pruefeZutrittsIdentZulaessigFuerAbstimmung(lZutrittsIdent);
            if (rcNummernform < 1) {
                switch (rcNummernform) {
                case CaFehler.pmZutrittsIdentNichtVorhanden: {
                    rcNummernformText = "Fehler: Eintrittskartennummer nicht vorhanden!";
                    break;
                }
                case CaFehler.pmZutrittsIdentGesperrt: {
                    rcNummernformText = "Fehler: Eintrittskartennummer ist gesperrt!";
                    break;
                }
                case CaFehler.pmEintrittskarteVerweistAufGast: {
                    rcNummernformText = "Fehler: Eintrittskartennummer verweist auf Gast!";
                    break;
                }
                }
                return false;
            }
            meldungsIdent = rcNummernform;
        }
        if (allgBlNummernformen.rcKartenklasse == KonstKartenklasse.stimmkartennummer) {

            identifikationsNummer = allgBlNummernformen.rcIdentifikationsnummer.get(0);
            rcNummernform = blAbstimmung.pruefeStimmkarteZulaessigFuerAbstimmung(identifikationsNummer);
            if (rcNummernform < 1) {
                switch (rcNummernform) {
                case CaFehler.pmStimmkarteNichtVorhanden: {
                    rcNummernformText = "Warnung: Stimmkartennummer nicht vorhanden!";
                    break;
                }
                case CaFehler.pmStimmkarteGesperrt: {
                    rcNummernformText = "Warnung: Stimmkartennummer ist gesperrt!";
                    break;
                }
                case CaFehler.pmStimmkarteVerweistAufGast: {
                    rcNummernformText = "Warnung: Stimmkartennummer verweist auf Gast!";
                    break;
                }
                }
                return false;
            }
            meldungsIdent = rcNummernform;
        }

        lDbBundle.dbMeldungen.leseZuIdent(meldungsIdent);
        rcMeldung = lDbBundle.dbMeldungen.meldungenArray[0];

        return true;

    }
    
    @FXML
    void onBtnDRUEXEdit(ActionEvent event) {
        CtrlHauptStage tmp = new CtrlHauptStage();
        tmp.gewDesigner(new ActionEvent());
    }
    
    @FXML
    void onBtnDRUEXExportOrdner(ActionEvent event) {
        CaDateiVerwaltung.openFileExplorer(new DbBundle().lieferePfadMeetingOutput());
    }
}
