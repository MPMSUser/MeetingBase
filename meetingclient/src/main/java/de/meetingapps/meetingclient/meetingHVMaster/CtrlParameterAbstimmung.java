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

import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;

import de.meetingapps.meetingclient.meetingClientDialoge.CaController;
import de.meetingapps.meetingclient.meetingClientDialoge.CaIcon;
import de.meetingapps.meetingclient.meetingClientDialoge.CtrlRoot;
import de.meetingapps.meetingclient.meetingClientOberflaechen.CbAllgemein;
import de.meetingapps.meetingclient.meetingClientOberflaechen.CbElement;
import de.meetingapps.meetingclient.meetingClientOberflaechen.MaxLengthNumericTextField;
import de.meetingapps.meetingclient.meetingClientOberflaechen.MaxLengthTextField;
import de.meetingapps.meetingclient.meetingClientOberflaechen.MeetingGridPane;
import de.meetingapps.meetingportal.meetComAllg.CaBug;
import de.meetingapps.meetingportal.meetComAllg.CaFehler;
import de.meetingapps.meetingportal.meetComAllg.CaString;
import de.meetingapps.meetingportal.meetComBl.BlAbstimmungsListeBearbeiten;
import de.meetingapps.meetingportal.meetComDb.DbBundle;
import de.meetingapps.meetingportal.meetComEntities.EclAbstimmung;
import de.meetingapps.meetingportal.meetComEntities.EclAbstimmungZuAbstimmungsblock;
import de.meetingapps.meetingportal.meetComEntities.EclAbstimmungenZuStimmkarte;
import de.meetingapps.meetingportal.meetComEntities.EclAbstimmungsblock;
import de.meetingapps.meetingportal.meetComEntities.EclStimmkarteInhalt;
import de.meetingapps.meetingportal.meetComHVParam.ParamS;
import de.meetingapps.meetingportal.meetComKonst.KonstStimmart;
import de.meetingapps.meetingportal.meetComStub.StubAbstimmungen;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 * The Class CtrlParameterAbstimmung.
 */
public class CtrlParameterAbstimmung extends CtrlRoot {

    /** The log drucken. */
    private int logDrucken = 3;

    /**Verarbeitungslogik für Oberfläche
    *
    * Der jeweilige Bildschirminhalt wird (vollständig) gespeichert bei folgenden Aktionen:
    * > Beim Wechseln eines Taps
    * > Vor dem Ausführen (Speichern) von Funktionen (also z.B. Neuaufnahme, Verschieben etc.)
    * > Bei Wechseln von Filter und/oder Sortierung
    * > Bei Verlassen/Speichern
    *
    * je nach filterTyp (siehe dort) wird geladen / gespeichert:
    * 0 Abstimmungen               angezeigteAbstimmungen,
    * 1 abstimmungsblock           angezeigteAbstimmungsblock (+Abstimmungen)
    * 2 elektronische Stimmkarte   angezeigteElekStimmkarte    (+Abstimmungen)
    * Welche gespeichert (sprich verändert) werden, steht jeweils in abstimmungWurdeVeraendert
    */
    @FXML
    private ResourceBundle resources;

    /** The location. */
    @FXML
    private URL location;

    /** The tb pane annzeige. */
    @FXML
    private TabPane tbPaneAnnzeige;

    /** The tp uebersicht. */
    @FXML
    private Tab tpUebersicht;

    /** The scr pn uebersicht. */
    @FXML
    private ScrollPane scrPnUebersicht;

    /** The btn neue abstimmung. */
    @FXML
    private Button btnNeueAbstimmung;

    /** The btn abstimmung loeschen. */
    @FXML
    private Button btnAbstimmungLoeschen;

    /*Nutzung von lblFunktion, tfEingabe1, lblEingabe1, tfEingabe2, lblEingabe2, comboAbstimmung:
     * Beim Verschieben:
     *
     * lblFunktion: ident, auf die die Funktion angewendet wird, plus Hinweis auf die Funktion
     *
     * tfEingabe1 und anschließend lblEingabe1,
     * tfEingabe2 und anschließend lblEingabe2, Jeweils Zielposition
     *
     */

    /** The lbl funktion. */
    @FXML
    private Label lblFunktion;

    /** The tf eingabe 1. */
    @FXML
    private TextField tfEingabe1;

    /** The lbl eingabe 1. */
    @FXML
    private Label lblEingabe1;

    /** The tf eingabe 2. */
    @FXML
    private TextField tfEingabe2;

    /** The lbl eingabe 2. */
    @FXML
    private Label lblEingabe2;

    /** The combo abstimmung. */
    @FXML
    private ComboBox<String> comboAbstimmung;

    /** The tp bezeichnungen. */
    @FXML
    private Tab tpBezeichnungen;

    /** The scr pn bezeichnungen. */
    @FXML
    private ScrollPane scrPnBezeichnungen;

    /** The tp aktivierung. */
    @FXML
    private Tab tpAktivierung;

    /** The scr pn aktivierung. */
    @FXML
    private ScrollPane scrPnAktivierung;

    /** The tp stimmarten. */
    @FXML
    private Tab tpStimmarten;

    /** The scr pn stimmarten. */
    @FXML
    private ScrollPane scrPnStimmarten;

    /** The tp stimmrecht. */
    @FXML
    private Tab tpStimmrecht;

    /** The scr pn stimmrecht. */
    @FXML
    private ScrollPane scrPnStimmrecht;

    /** The tp details. */
    @FXML
    private Tab tpDetails;

    /** The scr pn details. */
    @FXML
    private ScrollPane scrPnDetails;

    /** The tp drucken. */
    @FXML
    private Tab tpDrucken;

    /** The scr pn drucken. */
    @FXML
    private ScrollPane scrPnDrucken;

    /** The tp abstimmungsvorgang. */
    @FXML
    private Tab tpAbstimmungsvorgang;

    /** The scr pn abstimmungsvorgang. */
    @FXML
    private ScrollPane scrPnAbstimmungsvorgang;

    /** The btn neue abstimmung abstimmungsvorgang. */
    @FXML
    private Button btnNeueAbstimmungAbstimmungsvorgang;

    /** The btn abstimmung loeschen abstimmungsvorgang. */
    @FXML
    private Button btnAbstimmungLoeschenAbstimmungsvorgang;

    /** The lbl funktion abstimmungsvorgang. */
    @FXML
    private Label lblFunktionAbstimmungsvorgang;

    /** The tf eingabe 1 abstimmungsvorgang. */
    @FXML
    private TextField tfEingabe1Abstimmungsvorgang;

    /** The lbl eingabe 1 abstimmungsvorgang. */
    @FXML
    private Label lblEingabe1Abstimmungsvorgang;

    /** The combo abstimmungsvorgang. */
    @FXML
    private ComboBox<String> comboAbstimmungsvorgang;

    /** The tp elek stimmkarte. */
    @FXML
    private Tab tpElekStimmkarte;

    /** The scr pn elek stimmkarte. */
    @FXML
    private ScrollPane scrPnElekStimmkarte;

    /** The btn neue stimmkarte elek stimmkarte. */
    @FXML
    private Button btnNeueStimmkarteElekStimmkarte;

    /** The btn stimmkarte loeschen elek stimmkarte. */
    @FXML
    private Button btnStimmkarteLoeschenElekStimmkarte;

    /** The lbl funktion elek stimmkarte. */
    @FXML
    private Label lblFunktionElekStimmkarte;

    /** The tf eingabe 1 elek stimmkarte. */
    @FXML
    private TextField tfEingabe1ElekStimmkarte;

    /** The lbl eingabe 1 elek stimmkarte. */
    @FXML
    private Label lblEingabe1ElekStimmkarte;

    /** The combo elek stimmkarte. */
    @FXML
    private ComboBox<String> comboElekStimmkarte;

    /** The btn speichern. */
    @FXML
    private Button btnSpeichern;

    /** The combo filter fuer anzeige. */
    @FXML
    private ComboBox<CbElement> comboFilterFuerAnzeige;
    /**
     * ident1!=0: früher (A...): ident1 = Sortierreihenfolge Weisungen ident2!=0:
     * früher (B...): ident2 = Nummer des Abstimmungsvorgangsvorgangs ident3!=0:
     * früher (S...): ident3 = Nummer der elektronischen Stimmkarte
     */
    private CbAllgemein comboAllgemeinFuerAnzeige;

    /** The combo sortierung. */
    @FXML
    private ComboBox<CbElement> comboSortierung;

    /** The combo allgemein sortierung. */
    private CbAllgemein comboAllgemeinSortierung;

    /** The grpn abstimmungen. */
    private MeetingGridPane grpnAbstimmungen = null;

    /** The btn veraendern pos weisung intern. */
    private Button[] btnVeraendernPosWeisungIntern = null;

    /** The btn veraendern pos weisung portal. */
    private Button[] btnVeraendernPosWeisungPortal = null;

    /** The btn veraendern pos weisung verlassen HV. */
    private Button[] btnVeraendernPosWeisungVerlassenHV = null;

    /** Position auf elektronischen Geräte, Tablet/App. */
    private Button[] btnVeraendernPosInAbstimmungsblock = null;

    /** The btn veraendern pos in papier stimmkarte. */
    private Button[] btnVeraendernPosInPapierStimmkarte = null;

    /** The btn veraendern pos in elek stimmkarte. */
    private Button[] btnVeraendernPosInElekStimmkarte = null;
    /**
     * Verändern der Abstimmung, die sich tatsächlich hinter dem auf der Elektron.
     * Stimmkarte aufgeführten Abstimmung verbirgt
     */
    private Button[] btnVeraendernAbstInElekStimmkarte = null;

    /** The btn veraendern druck pos. */
    private Button[] btnVeraendernDruckPos = null;

    /** Zuordnung neu oder löschen, je nach Sicht. */
    private CheckBox[] cbZuordnung = null;

    /** The tf nummer key. */
    /*Bezeichnungen*/
    private MaxLengthTextField[] tfNummerKey = null;

    /** The tf nummerindex key. */
    private MaxLengthTextField[] tfNummerindexKey = null;

    /** The tf nummer formular. */
    private MaxLengthTextField[] tfNummerFormular = null;

    /** The tf nummerindex formular. */
    private MaxLengthTextField[] tfNummerindexFormular = null;

    /** The tf nummer. */
    private MaxLengthTextField[] tfNummer = null;

    /** The tf nummer EN. */
    private MaxLengthTextField[] tfNummerEN = null;

    /** The tf nummer index. */
    private MaxLengthTextField[] tfNummerIndex = null;

    /** The tf nummer index EN. */
    private MaxLengthTextField[] tfNummerIndexEN = null;

    /** The cb nummer unterdruecken. */
    private CheckBox[] cbNummerUnterdruecken = null;

    /** The tf kurz bezeichnung. */
    private MaxLengthTextField[] tfKurzBezeichnung = null;

    /** The tf kurz bezeichnung EN. */
    private MaxLengthTextField[] tfKurzBezeichnungEN = null;

    /** The tf anzeige bezeichnung kurz. */
    private MaxLengthTextField[] tfAnzeigeBezeichnungKurz = null;

    /** The tf anzeige bezeichnung kurz EN. */
    private MaxLengthTextField[] tfAnzeigeBezeichnungKurzEN = null;

    /** The tf anzeige bezeichnung lang. */
    private MaxLengthTextField[] tfAnzeigeBezeichnungLang = null;

    /** The tf anzeige bezeichnung lang EN. */
    private MaxLengthTextField[] tfAnzeigeBezeichnungLangEN = null;

    /** The tf kandidat. */
    private MaxLengthTextField[] tfKandidat = null;

    /** The tf kandidat EN. */
    private MaxLengthTextField[] tfKandidatEN = null;

    /** The btn alles aktivieren. */
    /*Aktivierung*/
    private Button[] btnAllesAktivieren = null;

    /** The cb aktiv. */
    private CheckBox[] cbAktiv = null;

    /** The cb aktiv preview. */
    private CheckBox[] cbAktivPreview = null;

    /** The cb aktiv weisungen in portal. */
    private CheckBox[] cbAktivWeisungenInPortal = null;

    /** The cb aktiv weisungen HV. */
    private CheckBox[] cbAktivWeisungenHV = null;

    /** The cb aktiv weisungen schnittstelle. */
    private CheckBox[] cbAktivWeisungenSchnittstelle = null;

    /** The cb aktiv weisungen anzeige. */
    private CheckBox[] cbAktivWeisungenAnzeige = null;

    /** The cb aktiv weisungen interne auswertungen. */
    private CheckBox[] cbAktivWeisungenInterneAuswertungen = null;

    /** The cb aktiv weisungen externe auswertungen. */
    private CheckBox[] cbAktivWeisungenExterneAuswertungen = null;

    /** The cb aktiv weisungen pflege intern. */
    private CheckBox[] cbAktivWeisungenPflegeIntern = null;

    /** The cb aktiv abstimmung in portal. */
    private CheckBox[] cbAktivAbstimmungInPortal = null;

    /** The cb aktiv bei SRV. */
    private CheckBox[] cbAktivBeiSRV = null;

    /** The cb aktiv bei briefwahl. */
    private CheckBox[] cbAktivBeiBriefwahl = null;

    /** The cb aktiv bei KIAV dauer. */
    private CheckBox[] cbAktivBeiKIAVDauer = null;

    /** The cb aktiv fragen. */
    private CheckBox[] cbAktivFragen = null;

    /** The cb aktiv antraege. */
    private CheckBox[] cbAktivAntraege = null;

    /** The cb aktiv widersprueche. */
    private CheckBox[] cbAktivWidersprueche = null;

    /** The cb aktiv wortmeldungen. */
    private CheckBox[] cbAktivWortmeldungen = null;

    /** The cb aktiv sonst mitteilungen. */
    private CheckBox[] cbAktivSonstMitteilungen = null;

    /** The cb aktiv botschaften einreichen. */
    private CheckBox[] cbAktivBotschaftenEinreichen = null;

    /** The cb stimmarten. */
    /*Stimmarten*/
    private CheckBox[][] cbStimmarten = null;

    /** The cb stimmen auswerten. */
    private ComboBox<String>[] cbStimmenAuswerten = null;

    /** The cb weisung nicht markierte speichern als SRV. */
    private ComboBox<String>[] cbWeisungNichtMarkierteSpeichernAlsSRV = null;

    /** The cb weisung nicht markierte speichern als briefwahl. */
    private ComboBox<String>[] cbWeisungNichtMarkierteSpeichernAlsBriefwahl = null;

    /** The cb weisung nicht markierte speichern als KIAV. */
    private ComboBox<String>[] cbWeisungNichtMarkierteSpeichernAlsKIAV = null;

    /** The cb weisung nicht markierte speichern als dauer. */
    private ComboBox<String>[] cbWeisungNichtMarkierteSpeichernAlsDauer = null;

    /** The cb weisung nicht markierte speichern als org. */
    private ComboBox<String>[] cbWeisungNichtMarkierteSpeichernAlsOrg = null;

    /** The cb weisung HV nicht markierte speichern als. */
    private ComboBox<String>[] cbWeisungHVNichtMarkierteSpeichernAls = null;

    /** The cb abstimmung nicht markierte speichern als. */
    private ComboBox<String>[] cbAbstimmungNichtMarkierteSpeichernAls = null;

    /** The cb stimmausschluss. */
    /*Stimmrecht*/
    private CheckBox[][] cbStimmausschluss = null;

    /** The btn pauschalausschluss. */
    private Button[] btnPauschalausschluss = null;

    /** The btn einzelausschluss. */
    private Button[] btnEinzelausschluss = null;

    /** The cb stimmberechtigte gattungen. */
    private CheckBox[][] cbStimmberechtigteGattungen = null;

    /** The cb erforderliche mehrheit. */
    /*Details*/
    private ComboBox<String>[] cbErforderlicheMehrheit = null;

    /** The cb zu abstimmungsgruppe. */
    private ComboBox<String>[] cbZuAbstimmungsgruppe = null;

    /** The cb ist gegenantrag. */
    private CheckBox[] cbIstGegenantrag = null;

    /** The cb ist ergaenzungsantrag. */
    private CheckBox[] cbIstErgaenzungsantrag = null;

    /** The cb gegenantraege gestellt. */
    private ComboBox<String>[] cbGegenantraegeGestellt = null;

    /** The cb von ident gesamtweisung. */
    private ComboBox<String>[] cbVonIdentGesamtweisung = null;

    /** The cb beschlussvorschlag gestellt von. */
    private ComboBox<String>[] cbBeschlussvorschlagGestelltVon = null;

    /** The tf beschlussvorschlag gestellt von sonstige. */
    private MaxLengthTextField[] tfBeschlussvorschlagGestelltVonSonstige = null;

    /** The tf formular kurz. */
    /*Drucken*/
    private MaxLengthNumericTextField[] tfFormularKurz = null;

    /** The tf formular lang. */
    private MaxLengthNumericTextField[] tfFormularLang = null;

    /** The tf formular buehne. */
    private MaxLengthNumericTextField[] tfFormularBuehne = null;

    /** The tf abstimmungsvorgang position. */
    /*Für Abstimmungsvorgang*/
    private MaxLengthNumericTextField[] tfAbstimmungsvorgangPosition = null;

    /** The tf abstimmungsvorgang kurz beschreibung. */
    private MaxLengthTextField[] tfAbstimmungsvorgangKurzBeschreibung = null;

    /** The tf abstimmungsvorgang beschreibung. */
    private MaxLengthTextField[] tfAbstimmungsvorgangBeschreibung = null;

    /** The tf abstimmungsvorgang beschreibung EN. */
    private MaxLengthTextField[] tfAbstimmungsvorgangBeschreibungEN = null;

    /** The cb abstimmungsvorgang aktiv. */
    private ComboBox<String>[] cbAbstimmungsvorgangAktiv = null; /*0, 1, 2, 3 möglich*/

    /** The tf elek stimmkarte position. */
    /*Für elektronische Stimmkarte*/
    private MaxLengthNumericTextField[] tfElekStimmkartePosition = null;

    /** The tf elek stimmkarte kurz beschreibung. */
    private MaxLengthTextField[] tfElekStimmkarteKurzBeschreibung = null;

    /** The tf elek stimmkarte beschreibung. */
    private MaxLengthTextField[] tfElekStimmkarteBeschreibung = null;

    /** The tf elek stimmkarte beschreibung EN. */
    private MaxLengthTextField[] tfElekStimmkarteBeschreibungEN = null;

    /** The cb elek stimmkarte aktiv. */
    private ComboBox<String>[] cbElekStimmkarteAktiv = null; /*0, 1 möglich*/

    /** The db bundle. */
    private DbBundle dbBundle = null;

    /** The stub abstimmungen. */
    private StubAbstimmungen stubAbstimmungen = null;

    /**
    * 0 = alle; 
    * 1 = Abstimmungsblock; 
    * 2 = Karte aus elektronischem Stimmkartenblock
    * 3=Zuordnung einer Abstimmung zu Abstimmungsblock
    * 4=Zuordnung einer Abstimmung zu einer elektronischen Stimmkarte
    * 5=Löschen einer Abstimmung zu Abstimmungsblock
    * 6=Löschen einer Abstimmung zu einer elektronischen Stimmkarte
    * */
    private int filterTyp = 0;

    /**interne ident, je nach ausgewähltem Filtertyp
    *
    * Falls Filtertyp 0:
    * 1 wirklich alle
    * 2 alle die intern aktiv
    * 3 alle die im Portal aktiv
    * 4 alle, die bei Weisungsterminals aktiv
    *
    * Falls Filtertyp sonst: ident des Selektierten Abstimmungsblocks bzw. El.Stimmkarte*/
    private int filterIdent = 1;

    /** The angezeigte abstimmungen. */
    private EclAbstimmung[] angezeigteAbstimmungen = null;

    /** The abstimmung wurde veraendert. */
    private boolean[] abstimmungWurdeVeraendert = null;

    /** The angezeigte abstimmung zu abstimmungsblock. */
    private EclAbstimmungZuAbstimmungsblock[] angezeigteAbstimmungZuAbstimmungsblock = null;

    /** The angezeigte abstimmung zu stimmkarte. */
    private EclAbstimmungenZuStimmkarte[] angezeigteAbstimmungZuStimmkarte = null;

    /** The angezeigte abstimmungsblock. */
    private EclAbstimmungsblock[] angezeigteAbstimmungsblock = null;

    /** The angezeigte elek stimmkarte. */
    private EclStimmkarteInhalt[] angezeigteElekStimmkarte = null;

    /** The ausgewaehlte abstimmung. */
    private int ausgewaehlteAbstimmung = -1;

    /** The angezeigter tab. */
    private int angezeigterTab = 0;

    /** The angezeigter tab alt. */
    private int angezeigterTabAlt = -1;

    /**0 normaler Änderungsmodus der einzelnen Abstimmungen
     * 1 Verschieben interne Position
     * 2 Verschieben externe Position (Weisungen)
     * 3 Verschieben Position Tablet/App
     * 4 Ändern Papier-StimmkartenNr/Position
     * 5 Ändern elek.Stimmkarten-Position
     * 6 Ändern Verw.Abst.Ident (Elektronische Stimmkarte)
     * 7 Änderung DruckPos (Abstimmungsblock)
     * 8 Verschieben externe Position (Weisungen HV)
     * 11 Neuaufnahme Abstimmung (quasi außer Kraft - wird sofort ausgelöst)
     * 12 Loeschen Abstimmung
     * 13 Loeschen Zuordnung Abstimmungsblock
     * 14 Loeschen Zuordnung el. Stimmkarte
     * 15 Zuordnen zu Abstimmungsblock
     * 16 Zuordnen zu ElekStimmkarte
     *
     * 21 neuer Abstimmungsvorgang
     * 22 Abstimmungsvorgang löschen
     *
     * 23 neue Elek. Stimmkarte
     * 24 Elek. Stimmkarte löschen
     */
    private int aktuelleFunktion = 0;

    /** Achtung - Integer-Werte / Reihenfolge identisch zu KostDBAbstimmungen!. */
    String sortierMoeglichkeiten[] = { "interne Ident", //0
            "Position Weisungen Intern", //1
            "Position Weisungen Portal", //2
            "Position elektron.Stimmkarte", //3
            "Position Stimmkarte", //4
            "Position Tablet-Abstimmung", //5
            "Position Ausdruck", //6
            "Position Weisungen Verlassen HV" //7
    };

    /** The sortierung. */
    private int sortierung = 0;

    /** The aktiv abstimmungsblock moeglichkeiten. */
    String aktivAbstimmungsblockMoeglichkeiten[] = { "0 nicht aktiv", "1 Wird angezeigt", "2 Einsammeln / Verarbeiten",
            "3 Verarbeiten" };

    /** The aktiv elek stimmkarte moeglichkeiten. */
    String aktivElekStimmkarteMoeglichkeiten[] = { "0 nicht aktiv", "1 aktiv (angezeigt)" };

    /** The aktiv moeglichkeiten. */
    String aktivMoeglichkeiten[] = { "0 nicht aktiv", "1 aktiv" };

    /** The cb zu abstimmungsgruppe moeglichkeiten. */
    String cbZuAbstimmungsgruppeMoeglichkeiten[] = { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10" };

    /** The gegenantraege gestellt moeglichkeiten. */
    String gegenantraegeGestelltMoeglichkeiten[] = { "(0) keiner", "(1) einer", "(2) mehrere" };

    /**
     * Wenn true, dann wird - auch bei einem Filter und Sortierungswechsel - die
     * Anzeige nicht refreshed. Verwenden, wenn Anzeigemaske neu aufgebaut wird, und
     * zum Schluß Gesamtanzeige erfolgt.
     */
    private boolean reloadUnterdruecken = false;

    /**
     * Initialize.
     */
    @FXML
    void initialize() {

        // @formatter:off
        assert tbPaneAnnzeige != null : "fx:id=\"tbPaneAnnzeige\" was not injected: check your FXML file 'ParameterAbstimmung.fxml'.";
        assert tpUebersicht != null : "fx:id=\"tpUebersicht\" was not injected: check your FXML file 'ParameterAbstimmung.fxml'.";
        assert scrPnUebersicht != null : "fx:id=\"scrPnUebersicht\" was not injected: check your FXML file 'ParameterAbstimmung.fxml'.";
        assert btnNeueAbstimmung != null : "fx:id=\"btnNeueAbstimmung\" was not injected: check your FXML file 'ParameterAbstimmung.fxml'.";
        assert btnAbstimmungLoeschen != null : "fx:id=\"btnAbstimmungLoeschen\" was not injected: check your FXML file 'ParameterAbstimmung.fxml'.";
        assert lblFunktion != null : "fx:id=\"lblFunktion\" was not injected: check your FXML file 'ParameterAbstimmung.fxml'.";
        assert tfEingabe1 != null : "fx:id=\"tfEingabe1\" was not injected: check your FXML file 'ParameterAbstimmung.fxml'.";
        assert lblEingabe1 != null : "fx:id=\"lblEingabe1\" was not injected: check your FXML file 'ParameterAbstimmung.fxml'.";
        assert tfEingabe2 != null : "fx:id=\"tfEingabe2\" was not injected: check your FXML file 'ParameterAbstimmung.fxml'.";
        assert lblEingabe2 != null : "fx:id=\"lblEingabe2\" was not injected: check your FXML file 'ParameterAbstimmung.fxml'.";
        assert comboAbstimmung != null : "fx:id=\"comboAbstimmung\" was not injected: check your FXML file 'ParameterAbstimmung.fxml'.";
        assert tpBezeichnungen != null : "fx:id=\"tpBezeichnungen\" was not injected: check your FXML file 'ParameterAbstimmung.fxml'.";
        assert scrPnBezeichnungen != null : "fx:id=\"scrPnBezeichnungen\" was not injected: check your FXML file 'ParameterAbstimmung.fxml'.";
        assert tpAktivierung != null : "fx:id=\"tpAktivierung\" was not injected: check your FXML file 'ParameterAbstimmung.fxml'.";
        assert scrPnAktivierung != null : "fx:id=\"scrPnAktivierung\" was not injected: check your FXML file 'ParameterAbstimmung.fxml'.";
        assert tpStimmarten != null : "fx:id=\"tpStimmarten\" was not injected: check your FXML file 'ParameterAbstimmung.fxml'.";
        assert scrPnStimmarten != null : "fx:id=\"scrPnStimmarten\" was not injected: check your FXML file 'ParameterAbstimmung.fxml'.";
        assert tpStimmrecht != null : "fx:id=\"tpStimmrecht\" was not injected: check your FXML file 'ParameterAbstimmung.fxml'.";
        assert scrPnStimmrecht != null : "fx:id=\"scrPnStimmrecht\" was not injected: check your FXML file 'ParameterAbstimmung.fxml'.";
        assert tpDetails != null : "fx:id=\"tpDetails\" was not injected: check your FXML file 'ParameterAbstimmung.fxml'.";
        assert scrPnDetails != null : "fx:id=\"scrPnDetails\" was not injected: check your FXML file 'ParameterAbstimmung.fxml'.";
        assert tpDrucken != null : "fx:id=\"tpDrucken\" was not injected: check your FXML file 'ParameterAbstimmung.fxml'.";
        assert scrPnDrucken != null : "fx:id=\"scrPnDrucken\" was not injected: check your FXML file 'ParameterAbstimmung.fxml'.";
        assert tpAbstimmungsvorgang != null : "fx:id=\"tpAbstimmungsvorgang\" was not injected: check your FXML file 'ParameterAbstimmung.fxml'.";
        assert scrPnAbstimmungsvorgang != null : "fx:id=\"scrPnAbstimmungsvorgang\" was not injected: check your FXML file 'ParameterAbstimmung.fxml'.";
        assert btnNeueAbstimmungAbstimmungsvorgang != null : "fx:id=\"btnNeueAbstimmungAbstimmungsvorgang\" was not injected: check your FXML file 'ParameterAbstimmung.fxml'.";
        assert btnAbstimmungLoeschenAbstimmungsvorgang != null : "fx:id=\"btnAbstimmungLoeschenAbstimmungsvorgang\" was not injected: check your FXML file 'ParameterAbstimmung.fxml'.";
        assert lblFunktionAbstimmungsvorgang != null : "fx:id=\"lblFunktionAbstimmungsvorgang\" was not injected: check your FXML file 'ParameterAbstimmung.fxml'.";
        assert tfEingabe1Abstimmungsvorgang != null : "fx:id=\"tfEingabe1Abstimmungsvorgang\" was not injected: check your FXML file 'ParameterAbstimmung.fxml'.";
        assert lblEingabe1Abstimmungsvorgang != null : "fx:id=\"lblEingabe1Abstimmungsvorgang\" was not injected: check your FXML file 'ParameterAbstimmung.fxml'.";
        assert comboAbstimmungsvorgang != null : "fx:id=\"comboAbstimmungsvorgang\" was not injected: check your FXML file 'ParameterAbstimmung.fxml'.";
        assert tpElekStimmkarte != null : "fx:id=\"tpElekStimmkarte\" was not injected: check your FXML file 'ParameterAbstimmung.fxml'.";
        assert scrPnElekStimmkarte != null : "fx:id=\"scrPnElekStimmkarte\" was not injected: check your FXML file 'ParameterAbstimmung.fxml'.";
        assert btnNeueStimmkarteElekStimmkarte != null : "fx:id=\"btnNeueStimmkarteElekStimmkarte\" was not injected: check your FXML file 'ParameterAbstimmung.fxml'.";
        assert btnStimmkarteLoeschenElekStimmkarte != null : "fx:id=\"btnStimmkarteLoeschenElekStimmkarte\" was not injected: check your FXML file 'ParameterAbstimmung.fxml'.";
        assert lblFunktionElekStimmkarte != null : "fx:id=\"lblFunktionElekStimmkarte\" was not injected: check your FXML file 'ParameterAbstimmung.fxml'.";
        assert tfEingabe1ElekStimmkarte != null : "fx:id=\"tfEingabe1ElekStimmkarte\" was not injected: check your FXML file 'ParameterAbstimmung.fxml'.";
        assert lblEingabe1ElekStimmkarte != null : "fx:id=\"lblEingabe1ElekStimmkarte\" was not injected: check your FXML file 'ParameterAbstimmung.fxml'.";
        assert comboElekStimmkarte != null : "fx:id=\"comboElekStimmkarte\" was not injected: check your FXML file 'ParameterAbstimmung.fxml'.";
        assert btnSpeichern != null : "fx:id=\"btnSpeichern\" was not injected: check your FXML file 'ParameterAbstimmung.fxml'.";
        assert comboFilterFuerAnzeige != null : "fx:id=\"comboFilterFuerAnzeige\" was not injected: check your FXML file 'ParameterAbstimmung.fxml'.";
        assert comboSortierung != null : "fx:id=\"comboSortierung\" was not injected: check your FXML file 'ParameterAbstimmung.fxml'.";
        // @formatter:on

        /*************** Ab hier individuell **********************************/
        tfEingabe1.setOnKeyPressed(e -> {
            if (e.getCode().equals(KeyCode.ENTER))
                btnNeueAbstimmung.fire();
        });

        tfEingabe2.setOnKeyPressed(e -> {
            if (e.getCode().equals(KeyCode.ENTER))
                btnNeueAbstimmung.fire();
        });

        dbBundle = new DbBundle();
        stubAbstimmungen = new StubAbstimmungen(false, dbBundle);

        eigeneStage.setOnCloseRequest(new EventHandler<WindowEvent>() {

            @Override
            public void handle(WindowEvent event) {
                int rc = abfragenReload();
                if (rc < 1) {
                    event.consume();
                    return;
                }
                rc = speichern();
                if (rc < 1) {
                    event.consume();
                }
            }
        });

        //        tpElekStimmkarte.setDisable(true);

        lblFunktion.setVisible(false);
        tfEingabe1.setVisible(false);
        lblEingabe1.setVisible(false);
        tfEingabe2.setVisible(false);
        lblEingabe2.setVisible(false);
        comboAbstimmung.setVisible(false);

        lblFunktionAbstimmungsvorgang.setVisible(false);
        tfEingabe1Abstimmungsvorgang.setVisible(false);
        lblEingabe1Abstimmungsvorgang.setVisible(false);
        comboAbstimmungsvorgang.setVisible(false);

        lblFunktionElekStimmkarte.setVisible(false);
        tfEingabe1ElekStimmkarte.setVisible(false);
        lblEingabe1ElekStimmkarte.setVisible(false);
        comboElekStimmkarte.setVisible(false);

        filterTyp = 0;
        sortierung = 0;
        reloadUnterdruecken = false;

        comboAllgemeinSortierung = new CbAllgemein(comboSortierung);
        fuelleSortierungFuerAnzeige();
        comboAllgemeinSortierung.setAusgewaehlt1(sortierung); //0

        comboAllgemeinFuerAnzeige = new CbAllgemein(comboFilterFuerAnzeige);
        fuelleFilterFuerAnzeige();
        comboAllgemeinFuerAnzeige.setAusgewaehlt1(1); //Alle

        comboFilterFuerAnzeige.valueProperty().addListener(new ChangeListener<CbElement>() {
            @Override
            public void changed(@SuppressWarnings("rawtypes") ObservableValue ov, CbElement alterWert,
                    CbElement neuerWert) {
                if (neuerWert == null) {
                    return;
                }
                if (neuerWert.ident1 != 0) {
                    filterTyp = 0;
                    filterIdent = neuerWert.ident1;
                    fuelleSortierungFuerAnzeige();
                    switch (filterIdent) {
                    case 1:
                        sortierung = 0;
                        break;
                    case 2:
                        sortierung = 1;
                        break;
                    case 3:
                        sortierung = 2;
                        break;
                    case 4:
                        sortierung = 7;
                        break;
                    }
                    comboAllgemeinSortierung.setAusgewaehlt1(sortierung);
                } else {
                    if (neuerWert.ident2 != 0) {
                        filterTyp = 1;
                        sortierung = 4;
                        filterIdent = neuerWert.ident2;
                    } else {
                        filterTyp = 2;
                        sortierung = 3;
                        filterIdent = neuerWert.ident3;
                    }
                    fuelleSortierungFuerAnzeige();
                    comboAllgemeinSortierung.setAusgewaehlt1(sortierung);
                }
                int rc = stubAbstimmungen.speichernVeraenderteAbstimmungenAbhaengigVonFilterTyp(filterTyp,
                        angezeigteAbstimmungen, angezeigteAbstimmungZuAbstimmungsblock,
                        angezeigteAbstimmungZuStimmkarte, abstimmungWurdeVeraendert);
                if (rc < 1) {
                    zeigeFehlerVeraendert();
                }

                fuelleNeuLoeschenButton();
                zeigeSortierung();
            }
        });

        comboSortierung.valueProperty().addListener(new ChangeListener<CbElement>() {
            @Override
            public void changed(@SuppressWarnings("rawtypes") ObservableValue ov, CbElement alterWert,
                    CbElement neuerWert) {
                if (neuerWert == null) {
                    return;
                } /*Entsteht, wenn COmbobox neu aufgebaut wird*/
                sortierung = neuerWert.ident1;
                int rc = stubAbstimmungen.speichernVeraenderteAbstimmungenAbhaengigVonFilterTyp(filterTyp,
                        angezeigteAbstimmungen, angezeigteAbstimmungZuAbstimmungsblock,
                        angezeigteAbstimmungZuStimmkarte, abstimmungWurdeVeraendert);
                if (rc < 1) {
                    zeigeFehlerVeraendert();
                }
                zeigeSortierung();
            }
        });

        if (!ParamS.param.paramModuleKonfigurierbar.elektronischerStimmblock) {
            tpElekStimmkarte.setDisable(true);
        }

        //    	sortierung=0;
        //    	filterTyp=0;

        tpDrucken.setDisable(true);
        if (!ParamS.param.paramModuleKonfigurierbar.elektronischerStimmblock) {
            tpElekStimmkarte.setDisable(true);
        }

        reloadUnterdruecken = false;
        zeigeSortierung();
        angezeigterTab = 0;
        angezeigterTabAlt = 0;

    }

    /**
     * fuehre Verschieben Aus
     * 
     * Tatsächliches Verschieben durchführen. Voraussetzung: aktuelleFunktion Bei
     * aktuelle Funktion 1, 2, 5, 7, 8 wird *Neben ignoriert Bei 3 (Tablet/App):
     * *Ident=Seite, *IdentNeben=Position
     * 
     * @param pAlteIdent      the alte ident
     * @param pAlteIdentNeben the alte ident neben
     * @param pNeueIdent      the neue ident
     * @param pNeueIdentNeben the neue ident neben
     */
    private void fuehreVerschiebenAus(int pAlteIdent, int pAlteIdentNeben, int pNeueIdent, int pNeueIdentNeben) {
        if (aktuelleFunktion == 1 || aktuelleFunktion == 2 || aktuelleFunktion == 8) {
            BlAbstimmungsListeBearbeiten blAbstimmungsListeBearbeiten = new BlAbstimmungsListeBearbeiten(
                    angezeigteAbstimmungen);
            blAbstimmungsListeBearbeiten.verschiebenPositionAbstimmungen(angezeigteAbstimmungen[ausgewaehlteAbstimmung],
                    pNeueIdent, aktuelleFunktion, abstimmungWurdeVeraendert);
            abstimmungWurdeVeraendert[ausgewaehlteAbstimmung] = true;
        } else {

            switch (aktuelleFunktion) {
            case 3://Seite+Pos Tablet/App
                einfuegenNeueIdent(pNeueIdent, pNeueIdentNeben, aktuelleFunktion);
                angezeigteAbstimmungZuAbstimmungsblock[ausgewaehlteAbstimmung].seite = pNeueIdent;
                angezeigteAbstimmungZuAbstimmungsblock[ausgewaehlteAbstimmung].position = pNeueIdentNeben;
                loeschenAlteIdent(pAlteIdent, pAlteIdentNeben);
                break;
            case 5://Elek. Stimmkarten-Pos
                einfuegenNeueIdent(pNeueIdent, -1, aktuelleFunktion);
                angezeigteAbstimmungZuStimmkarte[ausgewaehlteAbstimmung].positionInStimmkarte = pNeueIdent;
                loeschenAlteIdent(pAlteIdent, -1);
                break;
            case 7://Druck-Pos
                einfuegenNeueIdent(pNeueIdent, -1, aktuelleFunktion);
                angezeigteAbstimmungZuAbstimmungsblock[ausgewaehlteAbstimmung].positionInAusdruck = pNeueIdent;
                loeschenAlteIdent(pAlteIdent, -1);
                break;
            }
            abstimmungWurdeVeraendert[ausgewaehlteAbstimmung] = true;

        }

        speichernNachVerschieben();
    }

    /**
     * Speichern nach verschieben.
     */
    private void speichernNachVerschieben() {
        int rc;
        rc = stubAbstimmungen.speichernVeraenderteAbstimmungenAbhaengigVonFilterTyp(filterTyp, angezeigteAbstimmungen,
                angezeigteAbstimmungZuAbstimmungsblock, angezeigteAbstimmungZuStimmkarte, abstimmungWurdeVeraendert);
        if (rc < 1) {
            zeigeFehlerVeraendert();
        }
        deaktiviereBefehlszeile(false);
        zeigeSortierung();
    }

    /**
     * Einfügen einer neueIdent - PositionIntern oder etc. abhängig von
     * pAktuelleFunktion 3, 5, 7
     *
     * @param neueIdent         the neue ident
     * @param neueIdentNeben    the neue ident neben
     * @param pAktuelleFunktion the aktuelle funktion
     */
    private void einfuegenNeueIdent(int neueIdent, int neueIdentNeben, int pAktuelleFunktion) {
        /*Vorgehen:
         * Für alle Abstimmungen i
         * 		Wenn ident[i]<neueIdent: tue nichts
         * 		wenn ident[i]>=neueIdent: erhöhe ident[i] um 1
         */

        if ((neueIdent == 0 && (aktuelleFunktion == 5 || aktuelleFunktion == 7))
                || (neueIdentNeben == 0 && aktuelleFunktion == 3)) {
            /*bei "0" wird nichts verschoben, da mehrere 0 durchaus sinnvoll!
             */
            return;
        }
        int anz = angezeigteAbstimmungen.length;
        for (int i = 0; i < anz; i++) {
            switch (pAktuelleFunktion) {
            case 3://Seite+Pos Tablet/App
                if (angezeigteAbstimmungZuAbstimmungsblock[i].position >= neueIdentNeben
                        && angezeigteAbstimmungZuAbstimmungsblock[i].seite == neueIdent) {
                    angezeigteAbstimmungZuAbstimmungsblock[i].position++;
                    abstimmungWurdeVeraendert[i] = true;
                }
                break;
            case 5://ElekStimmKarte Pos
                if (angezeigteAbstimmungZuStimmkarte[i].positionInStimmkarte >= neueIdent) {
                    angezeigteAbstimmungZuStimmkarte[i].positionInStimmkarte++;
                    abstimmungWurdeVeraendert[i] = true;
                }
                break;
            case 7://Druck Pos
                if (angezeigteAbstimmungZuAbstimmungsblock[i].positionInAusdruck >= neueIdent) {
                    angezeigteAbstimmungZuAbstimmungsblock[i].positionInAusdruck++;
                    abstimmungWurdeVeraendert[i] = true;
                }
                break;
            }
        }
    }

    /**
     * Loeschen einer alteIdent - PositionIntern oder etc. je nach aktuelleFunktion
     *
     * @param alteIdent      the alte ident
     * @param alteIdentNeben the alte ident neben
     */
    private void loeschenAlteIdent(int alteIdent, int alteIdentNeben) {

        int anz = angezeigteAbstimmungen.length;

        /*Prüfen, ob alte Ident mehrfach vorhanden ist. Falls ja, dann Lücke nicht schließen!*/
        int gef = 0;
        for (int i = 0; i < anz; i++) {
            switch (aktuelleFunktion) {
            case 3://Seite+Pos Tablet/App
                if (angezeigteAbstimmungZuAbstimmungsblock[i].position == alteIdentNeben
                        && angezeigteAbstimmungZuAbstimmungsblock[i].seite == alteIdent) {
                    gef++;
                }
                break;
            case 5:
                if (angezeigteAbstimmungZuStimmkarte[i].positionInStimmkarte == alteIdent) {
                    gef++;
                }
                break;
            case 7:
                if (angezeigteAbstimmungZuAbstimmungsblock[i].positionInAusdruck == alteIdent) {
                    gef++;
                }
                break;
            }
        }
        if (gef > 0) {
            return;
        }

        /*Falls alteIdent 0, dann Lücke nicht schließen (0 möglichst nicht verwenden)*/
        if ((alteIdent == 0 && (aktuelleFunktion == 5 || aktuelleFunktion == 7))
                || (alteIdentNeben == 0 && aktuelleFunktion == 3)) {
            return;
        }
        /*Nun Lücke füllen*/
        for (int i = 0; i < anz; i++) {
            switch (aktuelleFunktion) {
            case 3:
                if (angezeigteAbstimmungZuAbstimmungsblock[i].position > alteIdentNeben
                        && angezeigteAbstimmungZuAbstimmungsblock[i].seite == alteIdent) {
                    angezeigteAbstimmungZuAbstimmungsblock[i].position--;
                    abstimmungWurdeVeraendert[i] = true;
                }
                break;
            case 5:
                if (angezeigteAbstimmungZuStimmkarte[i].positionInStimmkarte > alteIdent) {
                    angezeigteAbstimmungZuStimmkarte[i].positionInStimmkarte--;
                    abstimmungWurdeVeraendert[i] = true;
                }
                break;
            case 7:
                if (angezeigteAbstimmungZuAbstimmungsblock[i].positionInAusdruck > alteIdent) {
                    angezeigteAbstimmungZuAbstimmungsblock[i].positionInAusdruck--;
                    abstimmungWurdeVeraendert[i] = true;
                }
                break;
            }
        }
    }

    /**
     * Überprüfen, welche Tab aktuell angezeigt ist, und abhängig davon Speichern.
     *
     * @return the int
     */
    private int speichern() {
        CaBug.druckeLog("Start angezeigterTab=" + angezeigterTab, logDrucken, 10);
        int rc = 1;
        switch (angezeigterTab) {
        case 1:
            rc = speichernVeraendertePositionenBezeichnungen();
            break;
        case 2:
            rc = speichernVeraendertePositionenAktivierung();
            break;
        case 3:
            rc = speichernVeraendertePositionenStimmarten();
            break;
        case 4:
            rc = speichernVeraendertePositionenStimmrecht();
            break;
        case 5:
            rc = speichernVeraendertePositionenDetails();
            break;
        case 6:
            rc = speichernVeraendertePositionenDrucken();
            break;
        case 7:
            rc = speichernVeraendertePositionenAbstimmungsvorgang();
            break;
        case 8:
            rc = speichernVeraendertePositionenElStimmkarte();
            break;
        }

        stubAbstimmungen.speichernReload(reloadWeisungenAbbruch, reloadAbstimmungenAbbruch, reload);
        return rc;

    }

    /** The reload weisungen abbruch. */
    private boolean reloadWeisungenAbbruch = false;

    /** The reload abstimmungen abbruch. */
    private boolean reloadAbstimmungenAbbruch = false;

    /** The reload. */
    private boolean reload = false;

    /**
     * {@literal <0 =>} Abbruch gedrückt.
     *
     * @return the int
     */
    private int abfragenReload() {

        Stage newStage = new Stage();
        CaIcon.master(newStage);
        CtrlParameterAbstimmungReload controllerFenster = new CtrlParameterAbstimmungReload();
        controllerFenster.init(newStage);

        CaController caController = new CaController();
        caController.open(newStage, controllerFenster,
                "/de/meetingapps/meetingclient/meetingHVMaster/ParameterAbstimmungReload.fxml", 800, 500,
                "Speichern", true);

        if (controllerFenster.ergebnis == false) {
            reload = false;
            reloadWeisungenAbbruch = false;
            reloadAbstimmungenAbbruch = false;
            return -1;
        }

        reload = true;
        reloadWeisungenAbbruch = controllerFenster.weisungenAbbruch;
        reloadAbstimmungenAbbruch = controllerFenster.abstimmungenAbbruch;
        CaBug.druckeLog("vor Return", logDrucken, 10);
        return 1;
    }

    /**
     * Speichern aller veränderten Positionen für den Reiter Bezeichnungen. rc -1
     * Speichern nicht möglich - Daten von anderem Benutzer verändert 0 Falsche
     * Eingabe - Focus ist im entsprechenden Feld 1 Speichern ok
     *
     * @return the int
     */
    private int speichernVeraendertePositionenBezeichnungen() {
        if (angezeigteAbstimmungen == null) {
            return (1);
        }
        int anz = angezeigteAbstimmungen.length;

        /*Schritt 1 - Felder ggf. übertragen, und veraendert setzen*/
        String hString = "";
        for (int i = 0; i < anz; i++) {

            hString = tfNummerKey[i].getText().trim();
            if (hString.compareTo(angezeigteAbstimmungen[i].nummerKey) != 0) {
                angezeigteAbstimmungen[i].nummerKey = hString;
                abstimmungWurdeVeraendert[i] = true;
            }
            hString = tfNummerindexKey[i].getText().trim();
            if (hString.compareTo(angezeigteAbstimmungen[i].nummerindexKey) != 0) {
                angezeigteAbstimmungen[i].nummerindexKey = hString;
                abstimmungWurdeVeraendert[i] = true;
            }

            hString = tfNummerFormular[i].getText().trim();
            if (hString.compareTo(angezeigteAbstimmungen[i].nummerFormular) != 0) {
                angezeigteAbstimmungen[i].nummerFormular = hString;
                abstimmungWurdeVeraendert[i] = true;
            }
            hString = tfNummerindexFormular[i].getText().trim();
            if (hString.compareTo(angezeigteAbstimmungen[i].nummerindexFormular) != 0) {
                angezeigteAbstimmungen[i].nummerindexFormular = hString;
                abstimmungWurdeVeraendert[i] = true;
            }

            hString = tfNummer[i].getText().trim();
            if (hString.compareTo(angezeigteAbstimmungen[i].nummer) != 0) {
                angezeigteAbstimmungen[i].nummer = hString;
                abstimmungWurdeVeraendert[i] = true;
            }

            hString = tfNummerEN[i].getText().trim();
            if (hString.compareTo(angezeigteAbstimmungen[i].nummerEN) != 0) {
                angezeigteAbstimmungen[i].nummerEN = hString;
                abstimmungWurdeVeraendert[i] = true;
            }

            hString = tfNummerIndex[i].getText().trim();
            if (hString.compareTo(angezeigteAbstimmungen[i].nummerindex) != 0) {
                angezeigteAbstimmungen[i].nummerindex = hString;
                abstimmungWurdeVeraendert[i] = true;
            }

            hString = tfNummerIndexEN[i].getText().trim();
            if (hString.compareTo(angezeigteAbstimmungen[i].nummerindexEN) != 0) {
                angezeigteAbstimmungen[i].nummerindexEN = hString;
                abstimmungWurdeVeraendert[i] = true;
            }

            int hb = 0;
            if (cbNummerUnterdruecken[i].isSelected()) {
                hb = 1;
            } else {
                hb = 0;
            }
            if (hb != angezeigteAbstimmungen[i].nummerUnterdruecken) {
                angezeigteAbstimmungen[i].nummerUnterdruecken = hb;
                abstimmungWurdeVeraendert[i] = true;
            }

            hString = tfKurzBezeichnung[i].getText().trim();
            if (hString.compareTo(angezeigteAbstimmungen[i].kurzBezeichnung) != 0) {
                angezeigteAbstimmungen[i].kurzBezeichnung = hString;
                abstimmungWurdeVeraendert[i] = true;
            }

            hString = tfKurzBezeichnungEN[i].getText().trim();
            if (hString.compareTo(angezeigteAbstimmungen[i].kurzBezeichnungEN) != 0) {
                angezeigteAbstimmungen[i].kurzBezeichnungEN = hString;
                abstimmungWurdeVeraendert[i] = true;
            }

            hString = tfAnzeigeBezeichnungKurz[i].getText().trim();
            if (hString.compareTo(angezeigteAbstimmungen[i].anzeigeBezeichnungKurz) != 0) {
                angezeigteAbstimmungen[i].anzeigeBezeichnungKurz = hString;
                abstimmungWurdeVeraendert[i] = true;
            }

            hString = tfAnzeigeBezeichnungKurzEN[i].getText().trim();
            if (hString.compareTo(angezeigteAbstimmungen[i].anzeigeBezeichnungKurzEN) != 0) {
                angezeigteAbstimmungen[i].anzeigeBezeichnungKurzEN = hString;
                abstimmungWurdeVeraendert[i] = true;
            }

            hString = tfAnzeigeBezeichnungLang[i].getText().trim();
            if (hString.compareTo(angezeigteAbstimmungen[i].anzeigeBezeichnungLang) != 0) {
                angezeigteAbstimmungen[i].anzeigeBezeichnungLang = hString;
                abstimmungWurdeVeraendert[i] = true;
            }

            hString = tfAnzeigeBezeichnungLangEN[i].getText().trim();
            if (hString.compareTo(angezeigteAbstimmungen[i].anzeigeBezeichnungLangEN) != 0) {
                angezeigteAbstimmungen[i].anzeigeBezeichnungLangEN = hString;
                abstimmungWurdeVeraendert[i] = true;
            }

            hString = tfKandidat[i].getText().trim();
            if (hString.compareTo(angezeigteAbstimmungen[i].kandidat) != 0) {
                angezeigteAbstimmungen[i].kandidat = hString;
                abstimmungWurdeVeraendert[i] = true;
            }

            hString = tfKandidatEN[i].getText().trim();
            if (hString.compareTo(angezeigteAbstimmungen[i].kandidatEN) != 0) {
                angezeigteAbstimmungen[i].kandidatEN = hString;
                abstimmungWurdeVeraendert[i] = true;
            }

        }

        /*Schritt 2 - veraenderte Sätze in db schreiben*/
        int rc = stubAbstimmungen.speichernVeraenderteAngezeigteAbstimmungen(angezeigteAbstimmungen,
                abstimmungWurdeVeraendert);
        if (rc < 1) {
            zeigeFehlerVeraendert();
            return -1;
        }
        return 1;
    }

    /**
     * Speichern aller veränderten Positionen für den Reiter Aktivierung. lDbBundle
     * muß geöffnet sein rc -1 Speichern nicht möglich - Daten von anderem Benutzer
     * verändert 0 Falsche Eingabe - Focus ist im entsprechenden Feld 1 Speichern ok
     *
     * @return the int
     */
    private int speichernVeraendertePositionenAktivierung() {
        if (angezeigteAbstimmungen == null) {
            return (1);
        }
        int anz = angezeigteAbstimmungen.length;

        /*Schritt 1 - Felder ggf. übertragen, und veraendert setzen*/
        int hInt = 0;
        for (int i = 0; i < anz; i++) {

            if (cbAktiv[i].isSelected()) {
                hInt = 1;
            } else {
                hInt = 0;
            }
            if (hInt != angezeigteAbstimmungen[i].aktiv) {
                angezeigteAbstimmungen[i].aktiv = hInt;
                abstimmungWurdeVeraendert[i] = true;
            }

            if (cbAktivPreview[i].isSelected()) {
                hInt = 1;
            } else {
                hInt = 0;
            }
            if (hInt != angezeigteAbstimmungen[i].aktivPreview) {
                angezeigteAbstimmungen[i].aktivPreview = hInt;
                abstimmungWurdeVeraendert[i] = true;
            }

            if (cbAktivWeisungenInPortal[i].isSelected()) {
                hInt = 1;
            } else {
                hInt = 0;
            }
            if (hInt != angezeigteAbstimmungen[i].aktivWeisungenInPortal) {
                angezeigteAbstimmungen[i].aktivWeisungenInPortal = hInt;
                abstimmungWurdeVeraendert[i] = true;
            }

            if (cbAktivWeisungenHV[i].isSelected()) {
                hInt = 1;
            } else {
                hInt = 0;
            }
            if (hInt != angezeigteAbstimmungen[i].aktivWeisungenAufHV) {
                angezeigteAbstimmungen[i].aktivWeisungenAufHV = hInt;
                abstimmungWurdeVeraendert[i] = true;
            }

            if (cbAktivWeisungenSchnittstelle[i].isSelected()) {
                hInt = 1;
            } else {
                hInt = 0;
            }
            if (hInt != angezeigteAbstimmungen[i].aktivWeisungenSchnittstelle) {
                angezeigteAbstimmungen[i].aktivWeisungenSchnittstelle = hInt;
                abstimmungWurdeVeraendert[i] = true;
            }

            if (cbAktivWeisungenAnzeige[i].isSelected()) {
                hInt = 1;
            } else {
                hInt = 0;
            }
            if (hInt != angezeigteAbstimmungen[i].aktivWeisungenAnzeige) {
                angezeigteAbstimmungen[i].aktivWeisungenAnzeige = hInt;
                abstimmungWurdeVeraendert[i] = true;
            }

            if (cbAktivWeisungenInterneAuswertungen[i].isSelected()) {
                hInt = 1;
            } else {
                hInt = 0;
            }
            if (hInt != angezeigteAbstimmungen[i].aktivWeisungenInterneAuswertungen) {
                angezeigteAbstimmungen[i].aktivWeisungenInterneAuswertungen = hInt;
                abstimmungWurdeVeraendert[i] = true;
            }

            if (cbAktivWeisungenExterneAuswertungen[i].isSelected()) {
                hInt = 1;
            } else {
                hInt = 0;
            }
            if (hInt != angezeigteAbstimmungen[i].aktivWeisungenExterneAuswertungen) {
                angezeigteAbstimmungen[i].aktivWeisungenExterneAuswertungen = hInt;
                abstimmungWurdeVeraendert[i] = true;
            }

            if (cbAktivWeisungenPflegeIntern[i].isSelected()) {
                hInt = 1;
            } else {
                hInt = 0;
            }
            if (hInt != angezeigteAbstimmungen[i].aktivWeisungenPflegeIntern) {
                angezeigteAbstimmungen[i].aktivWeisungenPflegeIntern = hInt;
                abstimmungWurdeVeraendert[i] = true;
            }

            if (cbAktivAbstimmungInPortal[i].isSelected()) {
                hInt = 1;
            } else {
                hInt = 0;
            }
            if (hInt != angezeigteAbstimmungen[i].aktivAbstimmungInPortal) {
                angezeigteAbstimmungen[i].aktivAbstimmungInPortal = hInt;
                abstimmungWurdeVeraendert[i] = true;
            }

            if (cbAktivBeiSRV[i].isSelected()) {
                hInt = 1;
            } else {
                hInt = 0;
            }
            if (hInt != angezeigteAbstimmungen[i].aktivBeiSRV) {
                angezeigteAbstimmungen[i].aktivBeiSRV = hInt;
                abstimmungWurdeVeraendert[i] = true;
            }

            if (cbAktivBeiBriefwahl[i].isSelected()) {
                hInt = 1;
            } else {
                hInt = 0;
            }
            if (hInt != angezeigteAbstimmungen[i].aktivBeiBriefwahl) {
                angezeigteAbstimmungen[i].aktivBeiBriefwahl = hInt;
                abstimmungWurdeVeraendert[i] = true;
            }

            if (cbAktivBeiKIAVDauer[i].isSelected()) {
                hInt = 1;
            } else {
                hInt = 0;
            }
            if (hInt != angezeigteAbstimmungen[i].aktivBeiKIAVDauer) {
                angezeigteAbstimmungen[i].aktivBeiKIAVDauer = hInt;
                abstimmungWurdeVeraendert[i] = true;
            }

            if (cbAktivFragen[i].isSelected()) {
                hInt = 1;
            } else {
                hInt = 0;
            }
            if (hInt != angezeigteAbstimmungen[i].aktivFragen) {
                angezeigteAbstimmungen[i].aktivFragen = hInt;
                abstimmungWurdeVeraendert[i] = true;
            }

            if (cbAktivAntraege[i].isSelected()) {
                hInt = 1;
            } else {
                hInt = 0;
            }
            if (hInt != angezeigteAbstimmungen[i].aktivAntraege) {
                angezeigteAbstimmungen[i].aktivAntraege = hInt;
                abstimmungWurdeVeraendert[i] = true;
            }

            if (cbAktivWidersprueche[i].isSelected()) {
                hInt = 1;
            } else {
                hInt = 0;
            }
            if (hInt != angezeigteAbstimmungen[i].aktivWidersprueche) {
                angezeigteAbstimmungen[i].aktivWidersprueche = hInt;
                abstimmungWurdeVeraendert[i] = true;
            }

            if (cbAktivWortmeldungen[i].isSelected()) {
                hInt = 1;
            } else {
                hInt = 0;
            }
            if (hInt != angezeigteAbstimmungen[i].aktivWortmeldungen) {
                angezeigteAbstimmungen[i].aktivWortmeldungen = hInt;
                abstimmungWurdeVeraendert[i] = true;
            }

            if (cbAktivSonstMitteilungen[i].isSelected()) {
                hInt = 1;
            } else {
                hInt = 0;
            }
            if (hInt != angezeigteAbstimmungen[i].aktivSonstMitteilungen) {
                angezeigteAbstimmungen[i].aktivSonstMitteilungen = hInt;
                abstimmungWurdeVeraendert[i] = true;
            }

            if (cbAktivBotschaftenEinreichen[i].isSelected()) {
                hInt = 1;
            } else {
                hInt = 0;
            }
            if (hInt != angezeigteAbstimmungen[i].aktivBotschaftenEinreichen) {
                angezeigteAbstimmungen[i].aktivBotschaftenEinreichen = hInt;
                abstimmungWurdeVeraendert[i] = true;
            }

        }

        /*Schritt 2 - veraenderte Sätze in db schreiben*/
        int rc = stubAbstimmungen.speichernVeraenderteAngezeigteAbstimmungen(angezeigteAbstimmungen,
                abstimmungWurdeVeraendert);
        if (rc < 1) {
            zeigeFehlerVeraendert();
            return -1;
        }
        return 1;
    }

    /**
     * Speichern nicht markiert.
     *
     * @param pComboBox the combo box
     * @return the int
     */
    private int speichernNichtMarkiert(ComboBox<String> pComboBox) {
        int wert = -1;
        String hString = pComboBox.getValue();
        if (hString != null) {
            switch (hString) {
            case "J":
                wert = KonstStimmart.ja;
                break;
            case "N":
                wert = KonstStimmart.nein;
                break;
            case "E":
                wert = KonstStimmart.enthaltung;
                break;
            }
        }
        return wert;
    }

    /**
     * Speichern aller veränderten Positionen für den Reiter Aktivierung. lDbBundle
     * muß geöffnet sein rc -1 Speichern nicht möglich - Daten von anderem Benutzer
     * verändert 0 Falsche Eingabe - Focus ist im entsprechenden Feld 1 Speichern ok
     *
     * @return the int
     */
    private int speichernVeraendertePositionenStimmarten() {
        if (angezeigteAbstimmungen == null) {
            return (1);
        }
        int anz = angezeigteAbstimmungen.length;

        /*Schritt 1 - Felder ggf. übertragen, und veraendert setzen*/
        String hString = "";
        for (int i = 0; i < anz; i++) {
            for (int i1 = 0; i1 < 23; i1++) {
                int wertSelected = 0;
                if (cbStimmarten[i][i1].isSelected()) {
                    wertSelected = 1;
                } else {
                    wertSelected = 0;
                }
                switch (i1) {
                case 0:
                    if (angezeigteAbstimmungen[i].externJa != wertSelected) {
                        angezeigteAbstimmungen[i].externJa = wertSelected;
                        abstimmungWurdeVeraendert[i] = true;
                    }
                    break;
                case 1:
                    if (angezeigteAbstimmungen[i].externNein != wertSelected) {
                        angezeigteAbstimmungen[i].externNein = wertSelected;
                        abstimmungWurdeVeraendert[i] = true;
                    }
                    break;
                case 2:
                    if (angezeigteAbstimmungen[i].externEnthaltung != wertSelected) {
                        angezeigteAbstimmungen[i].externEnthaltung = wertSelected;
                        abstimmungWurdeVeraendert[i] = true;
                    }
                    break;
                case 3:
                    if (angezeigteAbstimmungen[i].externUngueltig != wertSelected) {
                        angezeigteAbstimmungen[i].externUngueltig = wertSelected;
                        abstimmungWurdeVeraendert[i] = true;
                    }
                    break;
                case 4:
                    if (angezeigteAbstimmungen[i].externNichtTeilnahme != wertSelected) {
                        angezeigteAbstimmungen[i].externNichtTeilnahme = wertSelected;
                        abstimmungWurdeVeraendert[i] = true;
                    }
                    break;
                case 5:
                    if (angezeigteAbstimmungen[i].externSonstiges1 != wertSelected) {
                        angezeigteAbstimmungen[i].externSonstiges1 = wertSelected;
                        abstimmungWurdeVeraendert[i] = true;
                    }
                    break;

                case 6:
                    if (angezeigteAbstimmungen[i].internJa != wertSelected) {
                        angezeigteAbstimmungen[i].internJa = wertSelected;
                        abstimmungWurdeVeraendert[i] = true;
                    }
                    break;
                case 7:
                    if (angezeigteAbstimmungen[i].internNein != wertSelected) {
                        angezeigteAbstimmungen[i].internNein = wertSelected;
                        abstimmungWurdeVeraendert[i] = true;
                    }
                    break;
                case 8:
                    if (angezeigteAbstimmungen[i].internEnthaltung != wertSelected) {
                        angezeigteAbstimmungen[i].internEnthaltung = wertSelected;
                        abstimmungWurdeVeraendert[i] = true;
                    }
                    break;
                case 9:
                    if (angezeigteAbstimmungen[i].internUngueltig != wertSelected) {
                        angezeigteAbstimmungen[i].internUngueltig = wertSelected;
                        abstimmungWurdeVeraendert[i] = true;
                    }
                    break;
                case 10:
                    if (angezeigteAbstimmungen[i].internNichtTeilnahme != wertSelected) {
                        angezeigteAbstimmungen[i].internNichtTeilnahme = wertSelected;
                        abstimmungWurdeVeraendert[i] = true;
                    }
                    break;
                case 11:
                    if (angezeigteAbstimmungen[i].internSonstiges1 != wertSelected) {
                        angezeigteAbstimmungen[i].internSonstiges1 = wertSelected;
                        abstimmungWurdeVeraendert[i] = true;
                    }
                    break;

                case 12:
                    if (angezeigteAbstimmungen[i].elStiBloJa != wertSelected) {
                        angezeigteAbstimmungen[i].elStiBloJa = wertSelected;
                        abstimmungWurdeVeraendert[i] = true;
                    }
                    break;
                case 13:
                    if (angezeigteAbstimmungen[i].elStiBloNein != wertSelected) {
                        angezeigteAbstimmungen[i].elStiBloNein = wertSelected;
                        abstimmungWurdeVeraendert[i] = true;
                    }
                    break;
                case 14:
                    if (angezeigteAbstimmungen[i].elStiBloEnthaltung != wertSelected) {
                        angezeigteAbstimmungen[i].elStiBloEnthaltung = wertSelected;
                        abstimmungWurdeVeraendert[i] = true;
                    }
                    break;
                case 15:
                    if (angezeigteAbstimmungen[i].elStiBloUngueltig != wertSelected) {
                        angezeigteAbstimmungen[i].elStiBloUngueltig = wertSelected;
                        abstimmungWurdeVeraendert[i] = true;
                    }
                    break;
                case 16:
                    if (angezeigteAbstimmungen[i].elStiBloNichtTeilnahme != wertSelected) {
                        angezeigteAbstimmungen[i].elStiBloNichtTeilnahme = wertSelected;
                        abstimmungWurdeVeraendert[i] = true;
                    }
                    break;
                case 17:
                    if (angezeigteAbstimmungen[i].elStiBloSonstiges1 != wertSelected) {
                        angezeigteAbstimmungen[i].elStiBloSonstiges1 = wertSelected;
                        abstimmungWurdeVeraendert[i] = true;
                    }
                    break;

                case 18:
                    if (angezeigteAbstimmungen[i].tabletJa != wertSelected) {
                        angezeigteAbstimmungen[i].tabletJa = wertSelected;
                        abstimmungWurdeVeraendert[i] = true;
                    }
                    break;
                case 19:
                    if (angezeigteAbstimmungen[i].tabletNein != wertSelected) {
                        angezeigteAbstimmungen[i].tabletNein = wertSelected;
                        abstimmungWurdeVeraendert[i] = true;
                    }
                    break;
                case 20:
                    if (angezeigteAbstimmungen[i].tabletEnthaltung != wertSelected) {
                        angezeigteAbstimmungen[i].tabletEnthaltung = wertSelected;
                        abstimmungWurdeVeraendert[i] = true;
                    }
                    break;
                case 21:
                    if (angezeigteAbstimmungen[i].tabletUngueltig != wertSelected) {
                        angezeigteAbstimmungen[i].tabletUngueltig = wertSelected;
                        abstimmungWurdeVeraendert[i] = true;
                    }
                    break;
                case 22:
                    if (angezeigteAbstimmungen[i].tabletNichtTeilnahme != wertSelected) {
                        angezeigteAbstimmungen[i].tabletNichtTeilnahme = wertSelected;
                        abstimmungWurdeVeraendert[i] = true;
                    }
                    break;
                case 23:
                    if (angezeigteAbstimmungen[i].tabletSonstiges1 != wertSelected) {
                        angezeigteAbstimmungen[i].tabletSonstiges1 = wertSelected;
                        abstimmungWurdeVeraendert[i] = true;
                    }
                    break;
                }
            }

            hString = cbStimmenAuswerten[i].getValue();
            int wert = -1;
            if (hString != null) {
                switch (hString) {
                case "A":
                    wert = -1;
                    break;
                case "J":
                    wert = KonstStimmart.ja;
                    break;
                case "N":
                    wert = KonstStimmart.nein;
                    break;
                case "E":
                    wert = KonstStimmart.enthaltung;
                    break;
                }
            }
            if (wert != angezeigteAbstimmungen[i].stimmenAuswerten) {
                angezeigteAbstimmungen[i].stimmenAuswerten = wert;
                abstimmungWurdeVeraendert[i] = true;
            }

            wert = speichernNichtMarkiert(cbWeisungNichtMarkierteSpeichernAlsSRV[i]);
            if (wert != angezeigteAbstimmungen[i].weisungNichtMarkierteSpeichernAlsSRV) {
                angezeigteAbstimmungen[i].weisungNichtMarkierteSpeichernAlsSRV = wert;
                abstimmungWurdeVeraendert[i] = true;
            }

            wert = speichernNichtMarkiert(cbWeisungNichtMarkierteSpeichernAlsBriefwahl[i]);
            if (wert != angezeigteAbstimmungen[i].weisungNichtMarkierteSpeichernAlsBriefwahl) {
                angezeigteAbstimmungen[i].weisungNichtMarkierteSpeichernAlsBriefwahl = wert;
                abstimmungWurdeVeraendert[i] = true;
            }

            wert = speichernNichtMarkiert(cbWeisungNichtMarkierteSpeichernAlsKIAV[i]);
            if (wert != angezeigteAbstimmungen[i].weisungNichtMarkierteSpeichernAlsKIAV) {
                angezeigteAbstimmungen[i].weisungNichtMarkierteSpeichernAlsKIAV = wert;
                abstimmungWurdeVeraendert[i] = true;
            }

            wert = speichernNichtMarkiert(cbWeisungNichtMarkierteSpeichernAlsDauer[i]);
            if (wert != angezeigteAbstimmungen[i].weisungNichtMarkierteSpeichernAlsDauer) {
                angezeigteAbstimmungen[i].weisungNichtMarkierteSpeichernAlsDauer = wert;
                abstimmungWurdeVeraendert[i] = true;
            }

            wert = speichernNichtMarkiert(cbWeisungNichtMarkierteSpeichernAlsOrg[i]);
            if (wert != angezeigteAbstimmungen[i].weisungNichtMarkierteSpeichernAlsOrg) {
                angezeigteAbstimmungen[i].weisungNichtMarkierteSpeichernAlsOrg = wert;
                abstimmungWurdeVeraendert[i] = true;
            }

            wert = speichernNichtMarkiert(cbWeisungHVNichtMarkierteSpeichernAls[i]);
            if (wert != angezeigteAbstimmungen[i].weisungHVNichtMarkierteSpeichernAls) {
                angezeigteAbstimmungen[i].weisungHVNichtMarkierteSpeichernAls = wert;
                abstimmungWurdeVeraendert[i] = true;
            }

            wert = speichernNichtMarkiert(cbAbstimmungNichtMarkierteSpeichernAls[i]);
            if (wert != angezeigteAbstimmungen[i].abstimmungNichtMarkierteSpeichernAls) {
                angezeigteAbstimmungen[i].abstimmungNichtMarkierteSpeichernAls = wert;
                abstimmungWurdeVeraendert[i] = true;
            }
        }

        /*Schritt 2 - veraenderte Sätze in db schreiben*/
        int rc = stubAbstimmungen.speichernVeraenderteAngezeigteAbstimmungen(angezeigteAbstimmungen,
                abstimmungWurdeVeraendert);
        if (rc < 1) {
            zeigeFehlerVeraendert();
            return -1;
        }
        return 1;
    }

    /**
     * Speichern aller veränderten Positionen für den Reiter Stimmrecht. lDbBundle
     * muß geöffnet sein rc -1 Speichern nicht möglich - Daten von anderem Benutzer
     * verändert 0 Falsche Eingabe - Focus ist im entsprechenden Feld 1 Speichern ok
     *
     * @return the int
     */
    private int speichernVeraendertePositionenStimmrecht() {
        if (angezeigteAbstimmungen == null) {
            return (1);
        }
        int anz = angezeigteAbstimmungen.length;

        /*Schritt 1 - Felder ggf. übertragen, und veraendert setzen*/
        for (int i = 0; i < anz; i++) {
            String stimmausschluss = "";
            if (cbStimmausschluss[i][0].isSelected()) {
                stimmausschluss += "V";
            }
            if (cbStimmausschluss[i][1].isSelected()) {
                stimmausschluss += "A";
            }
            if (cbStimmausschluss[i][2].isSelected()) {
                stimmausschluss += "S";
            }
            if (cbStimmausschluss[i][3].isSelected()) {
                stimmausschluss += "1";
            }
            if (cbStimmausschluss[i][4].isSelected()) {
                stimmausschluss += "2";
            }
            if (cbStimmausschluss[i][5].isSelected()) {
                stimmausschluss += "3";
            }
            if (cbStimmausschluss[i][6].isSelected()) {
                stimmausschluss += "4";
            }
            if (cbStimmausschluss[i][7].isSelected()) {
                stimmausschluss += "5";
            }
            if (cbStimmausschluss[i][8].isSelected()) {
                stimmausschluss += "6";
            }
            if (cbStimmausschluss[i][9].isSelected()) {
                stimmausschluss += "7";
            }
            if (cbStimmausschluss[i][10].isSelected()) {
                stimmausschluss += "8";
            }
            if (cbStimmausschluss[i][11].isSelected()) {
                stimmausschluss += "9";
            }
            if (cbStimmausschluss[i][12].isSelected()) {
                stimmausschluss += "E";
            }
            if (stimmausschluss.compareTo(angezeigteAbstimmungen[i].stimmausschluss) != 0) {
                abstimmungWurdeVeraendert[i] = true;
            }
            angezeigteAbstimmungen[i].stimmausschluss = stimmausschluss;

            for (int i1 = 0; i1 < 5; i1++) {
                int neuerWert = 0;
                if (cbStimmberechtigteGattungen[i][i1].isSelected()) {
                    neuerWert = 1;
                }
                if (angezeigteAbstimmungen[i].stimmberechtigteGattungen[i1] != neuerWert) {
                    abstimmungWurdeVeraendert[i] = true;
                    angezeigteAbstimmungen[i].stimmberechtigteGattungen[i1] = neuerWert;
                }
            }
        }

        /*Schritt 2 - veraenderte Sätze in db schreiben*/
        int rc = stubAbstimmungen.speichernVeraenderteAngezeigteAbstimmungen(angezeigteAbstimmungen,
                abstimmungWurdeVeraendert);
        if (rc < 1) {
            zeigeFehlerVeraendert();
            return -1;
        }
        return 1;
    }

    /**
     * Speichern aller veränderten Positionen für den Reiter Details. 
     * lDbBundle muß geöffnet sein 
     * rc -1 Speichern nicht möglich - Daten von anderem Benutzer verändert 
     *     0 Falsche Eingabe - Focus ist im entsprechenden Feld 
     *     1 Speichern ok
     *
     * @return the int
     */
    private int speichernVeraendertePositionenDetails() {
        if (angezeigteAbstimmungen == null) {
            return (1);
        }
        int anz = angezeigteAbstimmungen.length;

        /*Schritt 1 - Felder ggf. übertragen, und veraendert setzen*/
        for (int i = 0; i < anz; i++) {

            int identMehrheit = CtrlParameterAbstimmungAllgemein
                    .liefereAuswahlCbErforderlicheMehrheit(cbErforderlicheMehrheit[i]);
            if (identMehrheit != angezeigteAbstimmungen[i].identErforderlicheMehrheit) {
                abstimmungWurdeVeraendert[i] = true;
                angezeigteAbstimmungen[i].identErforderlicheMehrheit = identMehrheit;
            }

            String zuAbstimmungsgruppe = cbZuAbstimmungsgruppe[i].getValue();
            int identZuAbstimmungsgruppe = 0;
            if (zuAbstimmungsgruppe != null && !zuAbstimmungsgruppe.isEmpty()) {
                identZuAbstimmungsgruppe = Integer.parseInt(zuAbstimmungsgruppe);
            }
            if (identZuAbstimmungsgruppe != angezeigteAbstimmungen[i].zuAbstimmungsgruppe) {
                abstimmungWurdeVeraendert[i] = true;
                angezeigteAbstimmungen[i].zuAbstimmungsgruppe = identZuAbstimmungsgruppe;
            }

            int istGegenantrag = 0;
            if (cbIstGegenantrag[i].isSelected()) {
                istGegenantrag = 1;
            }
            if (istGegenantrag != angezeigteAbstimmungen[i].gegenantrag) {
                abstimmungWurdeVeraendert[i] = true;
                angezeigteAbstimmungen[i].gegenantrag = istGegenantrag;
            }

            int istErgaenzungsantrag = 0;
            if (cbIstErgaenzungsantrag[i].isSelected()) {
                istErgaenzungsantrag = 1;
            }
            if (istErgaenzungsantrag != angezeigteAbstimmungen[i].ergaenzungsantrag) {
                abstimmungWurdeVeraendert[i] = true;
                angezeigteAbstimmungen[i].ergaenzungsantrag = istErgaenzungsantrag;
            }

            String gegenantraegeGestellt = cbGegenantraegeGestellt[i].getValue();
            int identGegenantraegeGestellt = 0;
            if (gegenantraegeGestellt != null && !gegenantraegeGestellt.isEmpty()) {
                int hIndex = gegenantraegeGestellt.indexOf('(');
                int hIndexEnde = gegenantraegeGestellt.indexOf(')');
                String identString = gegenantraegeGestellt.substring(hIndex + 1, hIndexEnde);
                identGegenantraegeGestellt = Integer.parseInt(identString);
            }
            if (identGegenantraegeGestellt != angezeigteAbstimmungen[i].gegenantraegeGestellt) {
                abstimmungWurdeVeraendert[i] = true;
                angezeigteAbstimmungen[i].gegenantraegeGestellt = identGegenantraegeGestellt;
            }

            int identAbstimmung = CtrlParameterAbstimmungAllgemein
                    .liefereAuswahlAusCbAbstimmungArray(cbVonIdentGesamtweisung[i]);
            int identVonGesamtweisung = 0;
            if (identAbstimmung == -1) {
                identVonGesamtweisung = -1;
            } else {
                identVonGesamtweisung = CtrlParameterAbstimmungAllgemein.holeIdentGesamtweisungZuIdent(identAbstimmung);
            }
            if (identVonGesamtweisung != angezeigteAbstimmungen[i].vonIdentGesamtweisung) {
                abstimmungWurdeVeraendert[i] = true;
                angezeigteAbstimmungen[i].vonIdentGesamtweisung = identVonGesamtweisung;
            }

            int beschlussvorschlagGestelltVon = CtrlParameterAbstimmungAllgemein
                    .liefereAuswahlCbBeschlussvorschlagVonMoeglichkeiten(cbBeschlussvorschlagGestelltVon[i]);
            if (beschlussvorschlagGestelltVon != angezeigteAbstimmungen[i].beschlussvorschlagGestelltVon) {
                abstimmungWurdeVeraendert[i] = true;
                angezeigteAbstimmungen[i].beschlussvorschlagGestelltVon = beschlussvorschlagGestelltVon;
            }

            String beschlussvorschlagGestelltVonSonstige = tfBeschlussvorschlagGestelltVonSonstige[i].getText();
            if (!beschlussvorschlagGestelltVonSonstige
                    .equals(angezeigteAbstimmungen[i].beschlussvorschlagGestelltVonSonstige)) {
                abstimmungWurdeVeraendert[i] = true;
                angezeigteAbstimmungen[i].beschlussvorschlagGestelltVonSonstige = beschlussvorschlagGestelltVonSonstige;
            }

        }

        /*Schritt 2 - veraenderte Sätze in db schreiben*/
        int rc = stubAbstimmungen.speichernVeraenderteAngezeigteAbstimmungen(angezeigteAbstimmungen,
                abstimmungWurdeVeraendert);
        if (rc < 1) {
            zeigeFehlerVeraendert();
            return -1;
        }
        return 1;
    }

    /**
     * Speichern aller veränderten Positionen für den Reiter Drucken. lDbBundle muß geöffnet sein 
     * rc -1 Speichern nicht möglich - Daten von anderem Benutzer verändert 
     *     0 Falsche Eingabe - Focus ist im entsprechenden Feld 
     *     1 Speichern ok
     *
     * @return the int
     */
    private int speichernVeraendertePositionenDrucken() {
        if (angezeigteAbstimmungen == null) {
            return (1);
        }
        int anz = angezeigteAbstimmungen.length;

        /*Schritt 1 - Felder ggf. übertragen, und veraendert setzen*/
        for (int i = 0; i < anz; i++) {

            String hString = "";
            int hIdent = 0;

            hString = tfFormularKurz[i].getText();
            hIdent = 0;
            if (hString != null && !hString.isEmpty()) {
                hIdent = Integer.parseInt(hString);
            }
            if (hIdent != angezeigteAbstimmungen[i].formularKurz) {
                abstimmungWurdeVeraendert[i] = true;
                angezeigteAbstimmungen[i].formularKurz = hIdent;
            }

            hString = tfFormularLang[i].getText();
            hIdent = 0;
            if (hString != null && !hString.isEmpty()) {
                hIdent = Integer.parseInt(hString);
            }
            if (hIdent != angezeigteAbstimmungen[i].formularLang) {
                abstimmungWurdeVeraendert[i] = true;
                angezeigteAbstimmungen[i].formularLang = hIdent;
            }

            hString = tfFormularBuehne[i].getText();
            hIdent = 0;
            if (hString != null && !hString.isEmpty()) {
                hIdent = Integer.parseInt(hString);
            }
            if (hIdent != angezeigteAbstimmungen[i].formularBuehnenInfo) {
                abstimmungWurdeVeraendert[i] = true;
                angezeigteAbstimmungen[i].formularBuehnenInfo = hIdent;
            }

        }

        /*Schritt 2 - veraenderte Sätze in db schreiben*/
        int rc = stubAbstimmungen.speichernVeraenderteAngezeigteAbstimmungen(angezeigteAbstimmungen,
                abstimmungWurdeVeraendert);
        if (rc < 1) {
            zeigeFehlerVeraendert();
            return -1;
        }
        return 1;
    }

    /**
     * Speichern aller veränderten Positionen für den Reiter Abstimmungsvorgang. 
     * rc -1 Speichern nicht möglich - Daten von anderem Benutzer verändert 
     *     0 Falsche Eingabe - Focus ist im entsprechenden Feld 
     *     1 Speichern ok
     *
     * @return the int
     */
    private int speichernVeraendertePositionenAbstimmungsvorgang() {
        if (angezeigteAbstimmungsblock == null) {
            return (1);
        }
        int anz = angezeigteAbstimmungsblock.length;

        /*Schritt 1 - Felder ggf. übertragen, und veraendert setzen*/
        String hString = "";
        int hInt = 0;
        for (int i = 0; i < anz; i++) {
            /*Position*/
            hString = tfAbstimmungsvorgangPosition[i].getText().trim();
            if (!CaString.isNummern(hString)) {
                fehlerMeldung("Bitte gültige Position eingeben!");
                tfAbstimmungsvorgangPosition[i].requestFocus();
                return 0;
            }
            hInt = Integer.parseInt(hString);
            if (hInt < 0 || hInt > 9999) {
                fehlerMeldung("Bitte gültige Position eingeben!");
                tfAbstimmungsvorgangPosition[i].requestFocus();
                return 0;
            }
            if (hInt != angezeigteAbstimmungsblock[i].position) {
                angezeigteAbstimmungsblock[i].position = hInt;
                abstimmungWurdeVeraendert[i] = true;
            }

            /*kurzBezeichnung*/
            hString = tfAbstimmungsvorgangKurzBeschreibung[i].getText().trim();
            if (hString.compareTo(angezeigteAbstimmungsblock[i].kurzBeschreibung) != 0) {
                angezeigteAbstimmungsblock[i].kurzBeschreibung = hString;
                abstimmungWurdeVeraendert[i] = true;
            }

            /*Beschreibung*/
            hString = tfAbstimmungsvorgangBeschreibung[i].getText().trim();
            if (hString.compareTo(angezeigteAbstimmungsblock[i].beschreibung) != 0) {
                angezeigteAbstimmungsblock[i].beschreibung = hString;
                abstimmungWurdeVeraendert[i] = true;
            }

            /*BeschreibungEN*/
            hString = tfAbstimmungsvorgangBeschreibungEN[i].getText().trim();
            if (hString.compareTo(angezeigteAbstimmungsblock[i].beschreibungEN) != 0) {
                angezeigteAbstimmungsblock[i].beschreibungEN = hString;
                abstimmungWurdeVeraendert[i] = true;
            }

            /*Abstimmungsvorgang Aktiv*/
            hString = cbAbstimmungsvorgangAktiv[i].getValue();
            hInt = Integer.parseInt(hString.substring(0, 1));
            if (hInt != angezeigteAbstimmungsblock[i].aktiv) {
                angezeigteAbstimmungsblock[i].aktiv = hInt;
                abstimmungWurdeVeraendert[i] = true;
            }
        }

        /*Schritt 2 - veraenderte Sätze in db schreiben*/
        int rc = stubAbstimmungen.speichernVeraenderteAngezeigteAbstimmungsbloecke(angezeigteAbstimmungsblock,
                abstimmungWurdeVeraendert);
        if (rc < 1) {
            zeigeFehlerVeraendert();
            return -1;
        }
        return 1;
    }

    /**
     * Speichern aller veränderten Positionen für den Reiter ElektrStimmkarte. 
     * rc -1 Speichern nicht möglich - Daten von anderem Benutzer verändert 
     *     0 Falsche Eingabe - Focus ist im entsprechenden Feld 
     *     1 Speichern ok
     *
     * @return the int
     */
    private int speichernVeraendertePositionenElStimmkarte() {
        if (angezeigteElekStimmkarte == null) {
            return (1);
        }
        int anz = angezeigteElekStimmkarte.length;

        /*Schritt 1 - Felder ggf. übertragen, und veraendert setzen*/
        String hString = "";
        int hInt = 0;
        for (int i = 0; i < anz; i++) {
            /*Position*/
            hString = tfElekStimmkartePosition[i].getText().trim();
            if (!CaString.isNummern(hString)) {
                fehlerMeldung("Bitte gültige Position eingeben!");
                tfElekStimmkartePosition[i].requestFocus();
                return 0;
            }
            hInt = Integer.parseInt(hString);
            if (hInt < 0 || hInt > 9999) {
                fehlerMeldung("Bitte gültige Position eingeben!");
                tfElekStimmkartePosition[i].requestFocus();
                return 0;
            }
            if (hInt != angezeigteElekStimmkarte[i].posInBlock) {
                angezeigteElekStimmkarte[i].posInBlock = hInt;
                abstimmungWurdeVeraendert[i] = true;
            }

            /*kurzBezeichnung*/
            hString = tfElekStimmkarteKurzBeschreibung[i].getText().trim();
            if (hString.compareTo(angezeigteElekStimmkarte[i].kurzBezeichnung) != 0) {
                angezeigteElekStimmkarte[i].kurzBezeichnung = hString;
                abstimmungWurdeVeraendert[i] = true;
            }

            /*Beschreibung*/
            hString = tfElekStimmkarteBeschreibung[i].getText().trim();
            if (hString.compareTo(angezeigteElekStimmkarte[i].stimmkartenBezeichnung) != 0) {
                angezeigteElekStimmkarte[i].stimmkartenBezeichnung = hString;
                abstimmungWurdeVeraendert[i] = true;
            }

            /*BeschreibungEN*/
            hString = tfElekStimmkarteBeschreibungEN[i].getText().trim();
            if (hString.compareTo(angezeigteElekStimmkarte[i].stimmkartenBezeichnungEN) != 0) {
                angezeigteElekStimmkarte[i].stimmkartenBezeichnungEN = hString;
                abstimmungWurdeVeraendert[i] = true;
            }

            /*Abstimmungsvorgang Aktiv*/
            hString = cbElekStimmkarteAktiv[i].getValue();
            hInt = Integer.parseInt(hString.substring(0, 1));
            if (hInt != angezeigteElekStimmkarte[i].stimmkarteIstAktiv) {
                angezeigteElekStimmkarte[i].stimmkarteIstAktiv = hInt;
                abstimmungWurdeVeraendert[i] = true;
            }
        }

        /*Schritt 2 - veraenderte Sätze in db schreiben*/
        int rc = stubAbstimmungen.speichernVeraenderteAngezeigteElekStimmkarte(angezeigteElekStimmkarte,
                abstimmungWurdeVeraendert);
        if (rc < 1) {
            zeigeFehlerVeraendert();
            return -1;
        }
        return 1;
    }

    /*************************Ab hier Anzeigefunktionen****************************/

    /** Fehlermeldung auf "verändert von anderem Benutzer" setzen */
    private void zeigeFehlerVeraendert() {
        fehlerMeldung("Abstimmungsparameter wurden zwischenzeitlich von anderem Benutzer verändert! "
                + "Bitte Fenster schließen und Änderungen neu durchführen!");
        eigeneStage.hide();
    }

    /**
     * open/close erfolgt in dieser Funktion selbst.
     */
    private void zeigeSortierung() {
        if (reloadUnterdruecken) {
            return;
        }

        switch (angezeigterTab) {
        case 0:
            zeigeSortierungTbUebersicht();
            break;
        case 1:
            zeigeSortierungTbBezeichnungen();
            break;
        case 2:
            zeigeSortierungTbAktivierung();
            break;
        case 3:
            zeigeSortierungTbStimmarten();
            break;
        case 4:
            zeigeSortierungTbStimmrecht();
            break;
        case 5:
            zeigeSortierungTbDetails();
            break;
        case 6:
            zeigeSortierungTbDrucken();
            break;
        case 7:
            zeigeSortierungTpAbstimmungsvorgang();
            break;
        case 8:
            zeigeSortierungTpElekStimmkarte();
            break;
        }
    }

    /**
     * Zeige sortierung abstimmungen init.
     */
    private void zeigeSortierungAbstimmungenInit() {

        stubAbstimmungen.angezeigteAbstimmungen = angezeigteAbstimmungen;
        stubAbstimmungen.abstimmungWurdeVeraendert = abstimmungWurdeVeraendert;
        stubAbstimmungen.angezeigteAbstimmungZuAbstimmungsblock = angezeigteAbstimmungZuAbstimmungsblock;
        stubAbstimmungen.angezeigteAbstimmungZuStimmkarte = angezeigteAbstimmungZuStimmkarte;
        stubAbstimmungen.zeigeSortierungAbstimmungenInit(filterTyp, filterIdent, sortierung);
        angezeigteAbstimmungen = stubAbstimmungen.angezeigteAbstimmungen;
        abstimmungWurdeVeraendert = stubAbstimmungen.abstimmungWurdeVeraendert;
        angezeigteAbstimmungZuAbstimmungsblock = stubAbstimmungen.angezeigteAbstimmungZuAbstimmungsblock;
        angezeigteAbstimmungZuStimmkarte = stubAbstimmungen.angezeigteAbstimmungZuStimmkarte;
        abstimmungWurdeVeraendert = new boolean[angezeigteAbstimmungen.length];

        for (int i = 0; i < abstimmungWurdeVeraendert.length; i++) {
            abstimmungWurdeVeraendert[i] = false;
        }
    }

    /**
     * Zeige sortierung tb uebersicht.
     */
    private void zeigeSortierungTbUebersicht() {
        zeigeSortierungAbstimmungenInit();

        grpnAbstimmungen = new MeetingGridPane();
        //		grpnAbstimmungen.setVgap(5);
        //		grpnAbstimmungen.setHgap(15);

        btnVeraendernPosWeisungIntern = null;
        btnVeraendernPosWeisungPortal = null;
        btnVeraendernPosWeisungVerlassenHV = null;
        btnVeraendernPosInAbstimmungsblock = null;
        btnVeraendernPosInPapierStimmkarte = null;
        btnVeraendernPosInElekStimmkarte = null;
        btnVeraendernAbstInElekStimmkarte = null;
        btnVeraendernDruckPos = null;

        cbZuordnung = null;

        int spalte = 0;

        if (angezeigteAbstimmungen != null && angezeigteAbstimmungen.length > 0) {
            btnVeraendernPosWeisungIntern = new Button[angezeigteAbstimmungen.length];
            btnVeraendernPosWeisungPortal = new Button[angezeigteAbstimmungen.length];
            btnVeraendernPosWeisungVerlassenHV = new Button[angezeigteAbstimmungen.length];
            btnVeraendernPosInAbstimmungsblock = new Button[angezeigteAbstimmungen.length];
            btnVeraendernPosInPapierStimmkarte = new Button[angezeigteAbstimmungen.length];
            btnVeraendernPosInElekStimmkarte = new Button[angezeigteAbstimmungen.length];
            btnVeraendernAbstInElekStimmkarte = new Button[angezeigteAbstimmungen.length];
            btnVeraendernDruckPos = new Button[angezeigteAbstimmungen.length];

            cbZuordnung = new CheckBox[angezeigteAbstimmungen.length];

            for (int i = 0; i < angezeigteAbstimmungen.length; i++) {
                spalte = 0;

                if (filterTyp == 3 || filterTyp == 4) { /*Neue Zuordnung zu Abstimmungsblock / ElekStimmkarte*/
                    String vorhanden = "";

                    /*Prüfen, ob bereits zugeordnet*/
                    int gef = -1;
                    if (filterTyp == 3) {
                        for (int i1 = 0; i1 < angezeigteAbstimmungZuAbstimmungsblock.length; i1++) {
                            if (angezeigteAbstimmungZuAbstimmungsblock[i1].identAbstimmung == angezeigteAbstimmungen[i].ident) {
                                gef = 1;
                            }
                        }
                    } else {
                        for (int i1 = 0; i1 < angezeigteAbstimmungZuStimmkarte.length; i1++) {
                            if (angezeigteAbstimmungZuStimmkarte[i1].identAbstimmung == angezeigteAbstimmungen[i].ident) {
                                gef = 1;
                            }
                        }
                    }
                    if (gef == 1) {
                        vorhanden = "Bereits zugeordnet";
                    }
                    Label hLabelVorhanden = new Label(vorhanden);
                    grpnAbstimmungen.addMeeting(hLabelVorhanden, spalte, i + 1);
                    spalte++;

                    cbZuordnung[i] = new CheckBox();
                    grpnAbstimmungen.addMeeting(cbZuordnung[i], spalte, i + 1);
                    spalte++;
                }

                if (filterTyp == 5 || filterTyp == 6) { /*Analog: Löschen*/
                    cbZuordnung[i] = new CheckBox();
                    grpnAbstimmungen.addMeeting(cbZuordnung[i], spalte, i + 1);
                    spalte++;
                }

                if (filterTyp == 1) { /*Anzeige Abstimmungsblock*/
                    Label hLabel1a = new Label(Integer.toString(angezeigteAbstimmungZuAbstimmungsblock[i].ident));
                    grpnAbstimmungen.addMeeting(hLabel1a, spalte, i + 1);
                    spalte++;
                }

                Label hLabel1 = new Label(Integer.toString(angezeigteAbstimmungen[i].ident));
                grpnAbstimmungen.addMeeting(hLabel1, spalte, i + 1);
                spalte++;

                Label hLabel2 = new Label(angezeigteAbstimmungen[i].nummerKey);
                grpnAbstimmungen.addMeeting(hLabel2, spalte, i + 1);
                spalte++;

                Label hLabel3 = new Label(angezeigteAbstimmungen[i].nummerindexKey);
                grpnAbstimmungen.addMeeting(hLabel3, spalte, i + 1);
                spalte++;

                Label hLabel4 = new Label(angezeigteAbstimmungen[i].kurzBezeichnung);
                grpnAbstimmungen.addMeeting(hLabel4, spalte, i + 1);
                spalte++;

                Label hLabel5 = new Label("");
                if (angezeigteAbstimmungen[i].identWeisungssatz == -1) {
                    hLabel5.setText("Ja");
                }
                grpnAbstimmungen.addMeeting(hLabel5, spalte, i + 1);
                spalte++;

                Label hLabel8 = new Label("");
                if (angezeigteAbstimmungen[i].aktiv == 1) {
                    hLabel8.setText("Ja");
                } else {
                    hLabel8.setText("Nein");
                }
                grpnAbstimmungen.addMeeting(hLabel8, spalte, i + 1);
                spalte++;

                Label hLabel6 = new Label(Integer.toString(angezeigteAbstimmungen[i].anzeigePositionIntern));
                grpnAbstimmungen.addMeeting(hLabel6, spalte, i + 1);
                spalte++;

                btnVeraendernPosWeisungIntern[i] = new Button("Verschieben");
                btnVeraendernPosWeisungIntern[i].setOnAction(e -> {
                    clickedVeraendernPos(e);
                });
                if (filterTyp == 0 && (filterIdent == 2 || filterIdent == 1)) {
                    grpnAbstimmungen.addMeeting(btnVeraendernPosWeisungIntern[i], spalte, i + 1);
                    spalte++;
                }

                Label hLabel7 = new Label(Integer.toString(angezeigteAbstimmungen[i].anzeigePositionExternWeisungen));
                if (ParamS.param.paramModuleKonfigurierbar.aktionaersportal) {
                    grpnAbstimmungen.addMeeting(hLabel7, spalte, i + 1);
                    spalte++;
                }

                btnVeraendernPosWeisungPortal[i] = new Button("Verschieben");
                btnVeraendernPosWeisungPortal[i].setOnAction(e -> {
                    clickedVeraendernPos(e);
                });
                if (filterTyp == 0 && (filterIdent == 3 || filterIdent == 1)) {
                    if (ParamS.param.paramModuleKonfigurierbar.aktionaersportal) {
                        grpnAbstimmungen.addMeeting(btnVeraendernPosWeisungPortal[i], spalte, i + 1);
                        spalte++;
                    }
                }

                Label hLabel7a = new Label(
                        Integer.toString(angezeigteAbstimmungen[i].anzeigePositionExternWeisungenHV));
                grpnAbstimmungen.addMeeting(hLabel7a, spalte, i + 1);
                spalte++;

                btnVeraendernPosWeisungVerlassenHV[i] = new Button("Verschieben");
                btnVeraendernPosWeisungVerlassenHV[i].setOnAction(e -> {
                    clickedVeraendernPos(e);
                });
                if (filterTyp == 0 && (filterIdent == 4 || filterIdent == 1)) {
                    grpnAbstimmungen.addMeeting(btnVeraendernPosWeisungVerlassenHV[i], spalte, i + 1);
                    spalte++;
                }

                btnVeraendernPosInAbstimmungsblock[i] = new Button("Verschieben");
                btnVeraendernPosInAbstimmungsblock[i].setOnAction(e -> {
                    clickedVeraendernPos(e);
                });
                btnVeraendernPosInPapierStimmkarte[i] = new Button("Ändern");
                btnVeraendernPosInPapierStimmkarte[i].setOnAction(e -> {
                    clickedVeraendernPos(e);
                });
                btnVeraendernPosInPapierStimmkarte[i].setDisable(angezeigteAbstimmungen[i].liefereIstUeberschift());

                btnVeraendernDruckPos[i] = new Button("Verschieben");
                btnVeraendernDruckPos[i].setOnAction(e -> {
                    clickedVeraendernPos(e);
                });
                if (filterTyp == 1) { /*Anzeige Abstimmungsblock*/
                    Label hLabel9Seite = new Label(Integer.toString(angezeigteAbstimmungZuAbstimmungsblock[i].seite));
                    grpnAbstimmungen.addMeeting(hLabel9Seite, spalte, i + 1);
                    spalte++;

                    Label hLabel9 = new Label(Integer.toString(angezeigteAbstimmungZuAbstimmungsblock[i].position));
                    grpnAbstimmungen.addMeeting(hLabel9, spalte, i + 1);
                    spalte++;
                    grpnAbstimmungen.addMeeting(btnVeraendernPosInAbstimmungsblock[i], spalte, i + 1);
                    spalte++;

                    Label hLabel10 = new Label(
                            Integer.toString(angezeigteAbstimmungZuAbstimmungsblock[i].nummerDerStimmkarte));
                    grpnAbstimmungen.addMeeting(hLabel10, spalte, i + 1);
                    spalte++;
                    Label hLabel11 = new Label(
                            Integer.toString(angezeigteAbstimmungZuAbstimmungsblock[i].positionAufStimmkarte));
                    grpnAbstimmungen.addMeeting(hLabel11, spalte, i + 1);
                    spalte++;
                    grpnAbstimmungen.addMeeting(btnVeraendernPosInPapierStimmkarte[i], spalte, i + 1);
                    spalte++;
                    Label hLabel15 = new Label(
                            Integer.toString(angezeigteAbstimmungZuAbstimmungsblock[i].positionInAusdruck));
                    grpnAbstimmungen.addMeeting(hLabel15, spalte, i + 1);
                    spalte++;
                    grpnAbstimmungen.addMeeting(btnVeraendernDruckPos[i], spalte, i + 1);
                    spalte++;

                }

                btnVeraendernPosInElekStimmkarte[i] = new Button("Ändern");
                btnVeraendernPosInElekStimmkarte[i].setOnAction(e -> {
                    clickedVeraendernPos(e);
                });
                btnVeraendernAbstInElekStimmkarte[i] = new Button("Ändern");
                btnVeraendernAbstInElekStimmkarte[i].setOnAction(e -> {
                    clickedVeraendernPos(e);
                });
                if (filterTyp == 2) { /*Anzeige elek. Stimmkartenblock*/
                    Label hLabel12 = new Label(Integer.toString(angezeigteAbstimmungZuStimmkarte[i].stimmkartenNr));
                    grpnAbstimmungen.addMeeting(hLabel12, spalte, i + 1);
                    spalte++;
                    Label hLabel13 = new Label(
                            Integer.toString(angezeigteAbstimmungZuStimmkarte[i].positionInStimmkarte));
                    grpnAbstimmungen.addMeeting(hLabel13, spalte, i + 1);
                    spalte++;
                    grpnAbstimmungen.addMeeting(btnVeraendernPosInElekStimmkarte[i], spalte, i + 1);
                    spalte++;

                    Label hLabel14 = new Label(Integer.toString(angezeigteAbstimmungZuStimmkarte[i].identAbstimmung));
                    grpnAbstimmungen.addMeeting(hLabel14, spalte, i + 1);
                    spalte++;
                    grpnAbstimmungen.addMeeting(btnVeraendernAbstInElekStimmkarte[i], spalte, i + 1);
                    spalte++;

                }

            }
        }

        List<String> ueberschriftList = new LinkedList<String>();
        if (filterTyp == 3 || filterTyp == 4) {/*Neue Zuordnung zu Abstimmungsblock / ElekStimmkarte*/
            ueberschriftList.add("");
            ueberschriftList.add("Zuordnen");

        }
        if (filterTyp == 5 || filterTyp == 6) {/*Analog Löschen*/
            ueberschriftList.add("Löschen");

        }
        if (filterTyp == 1) { /*Anzeige Abstimmungsblock*/
            ueberschriftList.add("Zuordn.Ident");
        }
        ueberschriftList.add("Ident");
        ueberschriftList.add("TOP");
        ueberschriftList.add("TOPIndex");
        ueberschriftList.add("Kurzbezeichnung");
        ueberschriftList.add("Überschrift");
        ueberschriftList.add("aktiv");
        ueberschriftList.add("PosWeis. Intern");
        if (filterTyp == 0 && (filterIdent == 2 || filterIdent == 1)) {
            ueberschriftList.add("");
        }
        if (ParamS.param.paramModuleKonfigurierbar.aktionaersportal) {
            ueberschriftList.add("PosWeis.Portal");
            if (filterTyp == 0 && (filterIdent == 3 || filterIdent == 1)) {
                ueberschriftList.add("");
            }
        }
        ueberschriftList.add("PosWeis.Verl.HV");
        if (filterTyp == 0 && (filterIdent == 4 || filterIdent == 1)) {
            ueberschriftList.add("");
        }

        if (filterTyp == 1) { /*Anzeige Abstimmungsblock*/
            ueberschriftList.add("SeiteTabl.App");
            ueberschriftList.add("PosTabl.App");
            ueberschriftList.add("");
            ueberschriftList.add("StiKNr");
            ueberschriftList.add("PosStiK");
            ueberschriftList.add("");
            ueberschriftList.add("PosDruck");
            ueberschriftList.add("");
        }

        if (filterTyp == 2) { /*Anzeige elekt. Stimmungsblock*/
            ueberschriftList.add("El.StiKNr");
            ueberschriftList.add("PosStiK");
            ueberschriftList.add("");
            ueberschriftList.add("Verw.Abst.Ident");
            ueberschriftList.add("");
        }

        String[] uberschriftString = new String[ueberschriftList.size()];
        for (int i = 0; i < ueberschriftList.size(); i++) {
            uberschriftString[i] = ueberschriftList.get(i);
        }
        grpnAbstimmungen.setzeUeberschrift(uberschriftString, scrPnUebersicht);
    }

    /**
     * Zeige sortierung tb bezeichnungen.
     */
    private void zeigeSortierungTbBezeichnungen() {

        int faktor = 2;
        if (!ParamS.param.paramModuleKonfigurierbar.englischeAgenda) {
            faktor = 1;
        }

        zeigeSortierungAbstimmungenInit();

        grpnAbstimmungen = new MeetingGridPane();
        //		grpnAbstimmungen.setVgap(5);
        //		grpnAbstimmungen.setHgap(15);

        int spalte = 0;

        if (angezeigteAbstimmungen != null && angezeigteAbstimmungen.length > 0) {
            tfNummerKey = new MaxLengthTextField[angezeigteAbstimmungen.length];
            tfNummerindexKey = new MaxLengthTextField[angezeigteAbstimmungen.length];
            tfNummerFormular = new MaxLengthTextField[angezeigteAbstimmungen.length];
            tfNummerindexFormular = new MaxLengthTextField[angezeigteAbstimmungen.length];
            tfNummer = new MaxLengthTextField[angezeigteAbstimmungen.length];
            tfNummerEN = new MaxLengthTextField[angezeigteAbstimmungen.length];
            tfNummerIndex = new MaxLengthTextField[angezeigteAbstimmungen.length];
            tfNummerIndexEN = new MaxLengthTextField[angezeigteAbstimmungen.length];
            cbNummerUnterdruecken = new CheckBox[angezeigteAbstimmungen.length];
            tfKurzBezeichnung = new MaxLengthTextField[angezeigteAbstimmungen.length];
            tfKurzBezeichnungEN = new MaxLengthTextField[angezeigteAbstimmungen.length];
            tfAnzeigeBezeichnungKurz = new MaxLengthTextField[angezeigteAbstimmungen.length];
            tfAnzeigeBezeichnungKurzEN = new MaxLengthTextField[angezeigteAbstimmungen.length];
            tfAnzeigeBezeichnungLang = new MaxLengthTextField[angezeigteAbstimmungen.length];
            tfAnzeigeBezeichnungLangEN = new MaxLengthTextField[angezeigteAbstimmungen.length];
            tfKandidat = new MaxLengthTextField[angezeigteAbstimmungen.length];
            tfKandidatEN = new MaxLengthTextField[angezeigteAbstimmungen.length];

            for (int i = 0; i < angezeigteAbstimmungen.length; i++) {
                spalte = 0;
                Label hLabel1 = new Label(Integer.toString(angezeigteAbstimmungen[i].ident));
                grpnAbstimmungen.addMeeting(hLabel1, spalte, i * faktor + 1);
                spalte++;

                tfNummerKey[i] = new MaxLengthTextField(10);
                tfNummerKey[i].setText(angezeigteAbstimmungen[i].nummerKey);
                tfNummerKey[i].setMaxWidth(50);
                grpnAbstimmungen.addMeeting(tfNummerKey[i], spalte, i * faktor + 1);
                spalte++;

                tfNummerindexKey[i] = new MaxLengthTextField(10);
                tfNummerindexKey[i].setText(angezeigteAbstimmungen[i].nummerindexKey);
                tfNummerindexKey[i].setMaxWidth(50);
                grpnAbstimmungen.addMeeting(tfNummerindexKey[i], spalte, i * faktor + 1);
                spalte++;

                tfNummerFormular[i] = new MaxLengthTextField(10);
                tfNummerFormular[i].setText(angezeigteAbstimmungen[i].nummerFormular);
                tfNummerFormular[i].setMaxWidth(50);
                grpnAbstimmungen.addMeeting(tfNummerFormular[i], spalte, i * faktor + 1);
                spalte++;

                tfNummerindexFormular[i] = new MaxLengthTextField(10);
                tfNummerindexFormular[i].setText(angezeigteAbstimmungen[i].nummerindexFormular);
                tfNummerindexFormular[i].setMaxWidth(50);
                grpnAbstimmungen.addMeeting(tfNummerindexFormular[i], spalte, i * faktor + 1);
                spalte++;

                tfNummer[i] = new MaxLengthTextField(10);
                tfNummer[i].setText(angezeigteAbstimmungen[i].nummer);
                tfNummer[i].setMaxWidth(50);
                grpnAbstimmungen.addMeeting(tfNummer[i], spalte, i * faktor + 1);

                tfNummerEN[i] = new MaxLengthTextField(10);
                tfNummerEN[i].setText(angezeigteAbstimmungen[i].nummerEN);
                tfNummerEN[i].setMaxWidth(50);
                if (ParamS.param.paramModuleKonfigurierbar.englischeAgenda) {
                    grpnAbstimmungen.addMeeting(tfNummerEN[i], spalte, i * faktor + 2);
                }
                spalte++;

                tfNummerIndex[i] = new MaxLengthTextField(10);
                tfNummerIndex[i].setText(angezeigteAbstimmungen[i].nummerindex);
                tfNummerIndex[i].setMaxWidth(50);
                grpnAbstimmungen.addMeeting(tfNummerIndex[i], spalte, i * faktor + 1);

                tfNummerIndexEN[i] = new MaxLengthTextField(10);
                tfNummerIndexEN[i].setText(angezeigteAbstimmungen[i].nummerindexEN);
                tfNummerIndexEN[i].setMaxWidth(50);
                if (ParamS.param.paramModuleKonfigurierbar.englischeAgenda) {
                    grpnAbstimmungen.addMeeting(tfNummerIndexEN[i], spalte, i * faktor + 2);
                }
                spalte++;

                cbNummerUnterdruecken[i] = new CheckBox();
                cbNummerUnterdruecken[i].setSelected(angezeigteAbstimmungen[i].nummerUnterdruecken == 1);
                grpnAbstimmungen.addMeeting(cbNummerUnterdruecken[i], spalte, i * faktor + 1);
                spalte++;

                tfKurzBezeichnung[i] = new MaxLengthTextField(160);
                tfKurzBezeichnung[i].setText(angezeigteAbstimmungen[i].kurzBezeichnung);
                tfKurzBezeichnung[i].setMinWidth(200);
                grpnAbstimmungen.addMeeting(tfKurzBezeichnung[i], spalte, i * faktor + 1);

                tfKurzBezeichnungEN[i] = new MaxLengthTextField(160);
                tfKurzBezeichnungEN[i].setText(angezeigteAbstimmungen[i].kurzBezeichnungEN);
                tfKurzBezeichnungEN[i].setMinWidth(200);
                if (ParamS.param.paramModuleKonfigurierbar.englischeAgenda) {
                    grpnAbstimmungen.addMeeting(tfKurzBezeichnungEN[i], spalte, i * faktor + 2);
                }
                spalte++;

                tfAnzeigeBezeichnungKurz[i] = new MaxLengthTextField(800);
                tfAnzeigeBezeichnungKurz[i].setText(angezeigteAbstimmungen[i].anzeigeBezeichnungKurz);
                tfAnzeigeBezeichnungKurz[i].setMinWidth(400);

                tfAnzeigeBezeichnungKurzEN[i] = new MaxLengthTextField(800);
                tfAnzeigeBezeichnungKurzEN[i].setText(angezeigteAbstimmungen[i].anzeigeBezeichnungKurzEN);
                tfAnzeigeBezeichnungKurzEN[i].setMinWidth(400);

                if (ParamS.param.paramAbstimmungParameter.anzeigeBezeichnungKurzAktiv == 1) {
                    grpnAbstimmungen.addMeeting(tfAnzeigeBezeichnungKurz[i], spalte, i * faktor + 1);
                    if (ParamS.param.paramModuleKonfigurierbar.englischeAgenda) {
                        grpnAbstimmungen.addMeeting(tfAnzeigeBezeichnungKurzEN[i], spalte, i * faktor + 2);
                    }
                    spalte++;
                }

                tfAnzeigeBezeichnungLang[i] = new MaxLengthTextField(800);
                tfAnzeigeBezeichnungLang[i].setText(angezeigteAbstimmungen[i].anzeigeBezeichnungLang);
                tfAnzeigeBezeichnungLang[i].setMinWidth(800);
                grpnAbstimmungen.addMeeting(tfAnzeigeBezeichnungLang[i], spalte, i * faktor + 1);

                tfAnzeigeBezeichnungLangEN[i] = new MaxLengthTextField(800);
                tfAnzeigeBezeichnungLangEN[i].setText(angezeigteAbstimmungen[i].anzeigeBezeichnungLangEN);
                tfAnzeigeBezeichnungLangEN[i].setMinWidth(800);
                if (ParamS.param.paramModuleKonfigurierbar.englischeAgenda) {
                    grpnAbstimmungen.addMeeting(tfAnzeigeBezeichnungLangEN[i], spalte, i * faktor + 2);
                }
                spalte++;

                tfKandidat[i] = new MaxLengthTextField(160);
                tfKandidat[i].setText(angezeigteAbstimmungen[i].kandidat);
                tfKandidat[i].setMinWidth(200);
                grpnAbstimmungen.addMeeting(tfKandidat[i], spalte, i * faktor + 1);

                tfKandidatEN[i] = new MaxLengthTextField(160);
                tfKandidatEN[i].setText(angezeigteAbstimmungen[i].kandidatEN);
                tfKandidatEN[i].setMinWidth(200);
                if (ParamS.param.paramModuleKonfigurierbar.englischeAgenda) {
                    grpnAbstimmungen.addMeeting(tfKandidatEN[i], spalte, i * faktor + 2);
                }
                spalte++;

                Label hLabel5 = new Label("");
                if (angezeigteAbstimmungen[i].identWeisungssatz == -1) {
                    hLabel5.setText("Ja");
                }
                grpnAbstimmungen.addMeeting(hLabel5, spalte, i * faktor + 1);
                spalte++;

                Label hLabel8 = new Label("");
                if (angezeigteAbstimmungen[i].aktiv == 1) {
                    hLabel8.setText("Ja");
                } else {
                    hLabel8.setText("Nein");
                }
                grpnAbstimmungen.addMeeting(hLabel8, spalte, i * faktor + 1);
                spalte++;

            }
        }

        List<String> ueberschriftList = new LinkedList<String>();
        ueberschriftList.add("Ident");
        ueberschriftList.add("TOP");
        ueberschriftList.add("TOPIndex");
        ueberschriftList.add("TOP-Formular");
        ueberschriftList.add("TOPIndex-Formular");
        ueberschriftList.add("TOP-Liste");
        ueberschriftList.add("TOPIndex-Liste");
        ueberschriftList.add("TOPUnterdr.Port.");
        ueberschriftList.add("Kurzbezeichnung");
        if (ParamS.param.paramAbstimmungParameter.anzeigeBezeichnungKurzAktiv == 1) {
            ueberschriftList.add("Kurzbezeichnung Anzeige (leer => Langbezeichnung)");
        }
        ueberschriftList.add("Langbezeichnung Anzeige");
        ueberschriftList.add("Kandidat");
        ueberschriftList.add("Überschrift");
        ueberschriftList.add("aktiv");

        String[] uberschriftString = new String[ueberschriftList.size()];
        for (int i = 0; i < ueberschriftList.size(); i++) {
            uberschriftString[i] = ueberschriftList.get(i);
        }

        grpnAbstimmungen.setzeUeberschrift(uberschriftString, scrPnBezeichnungen);
    }

    /**
     * Inits the check box aktivierung.
     *
     * @param wert the wert
     * @return the check box
     */
    private CheckBox initCheckBoxAktivierung(int wert) {
        CheckBox cBNeu = new CheckBox();
        if (wert == 1) {
            cBNeu.setSelected(true);
        } else {
            cBNeu.setSelected(false);
        }
        return cBNeu;
    }

    /**
     * Zeige sortierung tb aktivierung.
     */
    private void zeigeSortierungTbAktivierung() {
        zeigeSortierungAbstimmungenInit();

        grpnAbstimmungen = new MeetingGridPane();
        //		grpnAbstimmungen.setVgap(5);
        //		grpnAbstimmungen.setHgap(15);

        int spalte = 0;

        if (angezeigteAbstimmungen != null && angezeigteAbstimmungen.length > 0) {
            btnAllesAktivieren = new Button[angezeigteAbstimmungen.length];
            cbAktiv = new CheckBox[angezeigteAbstimmungen.length];
            cbAktivPreview = new CheckBox[angezeigteAbstimmungen.length];
            cbAktivWeisungenInPortal = new CheckBox[angezeigteAbstimmungen.length];
            cbAktivWeisungenHV = new CheckBox[angezeigteAbstimmungen.length];
            cbAktivWeisungenSchnittstelle = new CheckBox[angezeigteAbstimmungen.length];
            cbAktivWeisungenAnzeige = new CheckBox[angezeigteAbstimmungen.length];
            cbAktivWeisungenInterneAuswertungen = new CheckBox[angezeigteAbstimmungen.length];
            cbAktivWeisungenExterneAuswertungen = new CheckBox[angezeigteAbstimmungen.length];
            cbAktivWeisungenPflegeIntern = new CheckBox[angezeigteAbstimmungen.length];
            cbAktivAbstimmungInPortal = new CheckBox[angezeigteAbstimmungen.length];
            cbAktivBeiSRV = new CheckBox[angezeigteAbstimmungen.length];
            cbAktivBeiBriefwahl = new CheckBox[angezeigteAbstimmungen.length];
            cbAktivBeiKIAVDauer = new CheckBox[angezeigteAbstimmungen.length];
            cbAktivFragen = new CheckBox[angezeigteAbstimmungen.length];
            cbAktivAntraege = new CheckBox[angezeigteAbstimmungen.length];
            cbAktivWidersprueche = new CheckBox[angezeigteAbstimmungen.length];
            cbAktivWortmeldungen = new CheckBox[angezeigteAbstimmungen.length];
            cbAktivSonstMitteilungen = new CheckBox[angezeigteAbstimmungen.length];
            cbAktivBotschaftenEinreichen = new CheckBox[angezeigteAbstimmungen.length];

            for (int i = 0; i < angezeigteAbstimmungen.length; i++) {
                spalte = 0;
                Label hLabel1 = new Label(Integer.toString(angezeigteAbstimmungen[i].ident));
                grpnAbstimmungen.addMeeting(hLabel1, spalte, i * 2 + 1);
                spalte++;

                Label lblNummer = new Label(angezeigteAbstimmungen[i].nummerKey);
                lblNummer.setMinWidth(50);
                grpnAbstimmungen.addMeeting(lblNummer, spalte, i * 2 + 1);
                spalte++;

                Label lblNummerIndex = new Label(angezeigteAbstimmungen[i].nummerindexKey);
                lblNummerIndex.setMinWidth(50);
                grpnAbstimmungen.addMeeting(lblNummerIndex, spalte, i * 2 + 1);
                spalte++;

                Label lblKurzBezeichnung = new Label(angezeigteAbstimmungen[i].kurzBezeichnung);
                lblKurzBezeichnung.setMinWidth(200);
                grpnAbstimmungen.addMeeting(lblKurzBezeichnung, spalte, i * 2 + 1);
                spalte++;

                Label hLabel5 = new Label("");
                if (angezeigteAbstimmungen[i].identWeisungssatz == -1) {
                    hLabel5.setText("Ja");
                }
                grpnAbstimmungen.addMeeting(hLabel5, spalte, i * 2 + 1);
                spalte++;

                btnAllesAktivieren[i] = new Button("Komplett aktivieren");
                btnAllesAktivieren[i].setOnAction(e -> {
                    clickedAllesAktivieren(e);
                });
                grpnAbstimmungen.addMeeting(btnAllesAktivieren[i], spalte, i * 2 + 1);
                spalte++;

                cbAktiv[i] = initCheckBoxAktivierung(angezeigteAbstimmungen[i].aktiv);
                grpnAbstimmungen.addMeeting(cbAktiv[i], spalte, i * 2 + 1);
                spalte++;

                cbAktivPreview[i] = initCheckBoxAktivierung(angezeigteAbstimmungen[i].aktivPreview);
                grpnAbstimmungen.addMeeting(cbAktivPreview[i], spalte, i * 2 + 1);
                spalte++;

                cbAktivWeisungenInPortal[i] = initCheckBoxAktivierung(angezeigteAbstimmungen[i].aktivWeisungenInPortal);
                if (ParamS.param.paramModuleKonfigurierbar.aktionaersportal) {
                    grpnAbstimmungen.addMeeting(cbAktivWeisungenInPortal[i], spalte, i * 2 + 1);
                    spalte++;
                }

                cbAktivWeisungenSchnittstelle[i] = initCheckBoxAktivierung(
                        angezeigteAbstimmungen[i].aktivWeisungenSchnittstelle);
                if (ParamS.param.paramModuleKonfigurierbar.weisungenSchnittstelleExternesSystem) {
                    grpnAbstimmungen.addMeeting(cbAktivWeisungenSchnittstelle[i], spalte, i * 2 + 1);
                    spalte++;
                }

                cbAktivWeisungenAnzeige[i] = initCheckBoxAktivierung(angezeigteAbstimmungen[i].aktivWeisungenAnzeige);
                grpnAbstimmungen.addMeeting(cbAktivWeisungenAnzeige[i], spalte, i * 2 + 1);
                spalte++;

                cbAktivWeisungenInterneAuswertungen[i] = initCheckBoxAktivierung(
                        angezeigteAbstimmungen[i].aktivWeisungenInterneAuswertungen);
                grpnAbstimmungen.addMeeting(cbAktivWeisungenInterneAuswertungen[i], spalte, i * 2 + 1);
                spalte++;

                cbAktivWeisungenExterneAuswertungen[i] = initCheckBoxAktivierung(
                        angezeigteAbstimmungen[i].aktivWeisungenExterneAuswertungen);
                grpnAbstimmungen.addMeeting(cbAktivWeisungenExterneAuswertungen[i], spalte, i * 2 + 1);
                spalte++;

                cbAktivWeisungenPflegeIntern[i] = initCheckBoxAktivierung(
                        angezeigteAbstimmungen[i].aktivWeisungenPflegeIntern);
                grpnAbstimmungen.addMeeting(cbAktivWeisungenPflegeIntern[i], spalte, i * 2 + 1);
                spalte++;

                cbAktivWeisungenHV[i] = initCheckBoxAktivierung(angezeigteAbstimmungen[i].aktivWeisungenAufHV);
                grpnAbstimmungen.addMeeting(cbAktivWeisungenHV[i], spalte, i * 2 + 1);
                spalte++;

                cbAktivAbstimmungInPortal[i] = initCheckBoxAktivierung(
                        angezeigteAbstimmungen[i].aktivAbstimmungInPortal);
                if (ParamS.param.paramModuleKonfigurierbar.hvAppAbstimmung
                        || ParamS.param.paramModuleKonfigurierbar.tabletAbstimmung) {
                    grpnAbstimmungen.addMeeting(cbAktivAbstimmungInPortal[i], spalte, i * 2 + 1);
                    spalte++;
                }

                cbAktivBeiSRV[i] = initCheckBoxAktivierung(angezeigteAbstimmungen[i].aktivBeiSRV);
                grpnAbstimmungen.addMeeting(cbAktivBeiSRV[i], spalte, i * 2 + 1);
                spalte++;

                cbAktivBeiBriefwahl[i] = initCheckBoxAktivierung(angezeigteAbstimmungen[i].aktivBeiBriefwahl);
                if (ParamS.param.paramModuleKonfigurierbar.briefwahl) {
                    grpnAbstimmungen.addMeeting(cbAktivBeiBriefwahl[i], spalte, i * 2 + 1);
                    spalte++;
                }

                cbAktivBeiKIAVDauer[i] = initCheckBoxAktivierung(angezeigteAbstimmungen[i].aktivBeiKIAVDauer);
                grpnAbstimmungen.addMeeting(cbAktivBeiKIAVDauer[i], spalte, i * 2 + 1);
                spalte++;

                cbAktivFragen[i] = initCheckBoxAktivierung(angezeigteAbstimmungen[i].aktivFragen);
                grpnAbstimmungen.addMeeting(cbAktivFragen[i], spalte, i * 2 + 1);
                spalte++;

                cbAktivAntraege[i] = initCheckBoxAktivierung(angezeigteAbstimmungen[i].aktivAntraege);
                grpnAbstimmungen.addMeeting(cbAktivAntraege[i], spalte, i * 2 + 1);
                spalte++;

                cbAktivWidersprueche[i] = initCheckBoxAktivierung(angezeigteAbstimmungen[i].aktivWidersprueche);
                grpnAbstimmungen.addMeeting(cbAktivWidersprueche[i], spalte, i * 2 + 1);
                spalte++;

                cbAktivWortmeldungen[i] = initCheckBoxAktivierung(angezeigteAbstimmungen[i].aktivWortmeldungen);
                grpnAbstimmungen.addMeeting(cbAktivWortmeldungen[i], spalte, i * 2 + 1);
                spalte++;

                cbAktivSonstMitteilungen[i] = initCheckBoxAktivierung(angezeigteAbstimmungen[i].aktivSonstMitteilungen);
                grpnAbstimmungen.addMeeting(cbAktivSonstMitteilungen[i], spalte, i * 2 + 1);
                spalte++;

                cbAktivBotschaftenEinreichen[i] = initCheckBoxAktivierung(
                        angezeigteAbstimmungen[i].aktivBotschaftenEinreichen);
                grpnAbstimmungen.addMeeting(cbAktivBotschaftenEinreichen[i], spalte, i * 2 + 1);
                spalte++;

            }
        }

        List<String> ueberschriftList = new LinkedList<String>();
        ueberschriftList.add("Ident");
        ueberschriftList.add("TOP");
        ueberschriftList.add("TOPIndex");
        ueberschriftList.add("Kurzbezeichnung");
        ueberschriftList.add("Überschrift");
        ueberschriftList.add("");
        ueberschriftList.add("Insgesamt");
        ueberschriftList.add("Preview");
        if (ParamS.param.paramModuleKonfigurierbar.aktionaersportal) {
            ueberschriftList.add("Weis.Portal");
        }
        if (ParamS.param.paramModuleKonfigurierbar.weisungenSchnittstelleExternesSystem) {
            ueberschriftList.add("Weis.ext.Sys.");
        }
        ueberschriftList.add("Weis.Anzeige int.");
        ueberschriftList.add("Weis.Ausw. int.");
        ueberschriftList.add("Weis.Ausw. ext.");
        ueberschriftList.add("Weis.Pflege int.");
        ueberschriftList.add("Weis.Verl.HV");
        if (ParamS.param.paramModuleKonfigurierbar.hvAppAbstimmung
                || ParamS.param.paramModuleKonfigurierbar.tabletAbstimmung) {
            ueberschriftList.add("Abst.TabApp");
        }
        ueberschriftList.add("Erg:SRV");
        if (ParamS.param.paramModuleKonfigurierbar.briefwahl) {
            ueberschriftList.add("Erg:Briefwahl");
        }
        ueberschriftList.add("Erg:KIAV/Dauer");

        ueberschriftList.add("Fragen");
        ueberschriftList.add("Anträge");
        ueberschriftList.add("Widerspr.");
        ueberschriftList.add("Wortmeld.");
        ueberschriftList.add("Sonst.Mitteil.");
        ueberschriftList.add("Botschaften");

        String[] uberschriftString = new String[ueberschriftList.size()];
        for (int i = 0; i < ueberschriftList.size(); i++) {
            uberschriftString[i] = ueberschriftList.get(i);
        }
        grpnAbstimmungen.setzeUeberschrift(uberschriftString, scrPnAktivierung);
    }

    /**
     * Fuelle stimmart.
     *
     * @param i              the i
     * @param offset         the offset
     * @param spalte         the spalte
     * @param ja             the ja
     * @param nein           the nein
     * @param enthaltung     the enthaltung
     * @param ungueltig      the ungueltig
     * @param nichtTeilnahme the nicht teilnahme
     * @param sonstiges1     the sonstiges 1
     */
    private void fuelleStimmart(int i, int offset, int spalte, int ja, int nein, int enthaltung, int ungueltig,
            int nichtTeilnahme, int sonstiges1) {

        HBox hBox = new HBox();

        Label lJa = new Label("J");
        hBox.getChildren().add(lJa);
        if (ja == 1) {
            cbStimmarten[i][offset + 0].setSelected(true);
        } else {
            cbStimmarten[i][offset + 0].setSelected(false);
        }
        hBox.getChildren().add(cbStimmarten[i][offset + 0]);

        Label lNein = new Label("N");
        hBox.getChildren().add(lNein);
        if (nein == 1) {
            cbStimmarten[i][offset + 1].setSelected(true);
        } else {
            cbStimmarten[i][offset + 1].setSelected(false);
        }
        hBox.getChildren().add(cbStimmarten[i][offset + 1]);

        Label lEnthaltung = new Label("E");
        hBox.getChildren().add(lEnthaltung);
        if (enthaltung == 1) {
            cbStimmarten[i][offset + 2].setSelected(true);
        } else {
            cbStimmarten[i][offset + 2].setSelected(false);
        }
        hBox.getChildren().add(cbStimmarten[i][offset + 2]);

        Label lUngueltig = new Label("U");
        hBox.getChildren().add(lUngueltig);
        if (ungueltig == 1) {
            cbStimmarten[i][offset + 3].setSelected(true);
        } else {
            cbStimmarten[i][offset + 3].setSelected(false);
        }
        hBox.getChildren().add(cbStimmarten[i][offset + 3]);

        Label lNichtTeilnahme = new Label("NT");
        if (nichtTeilnahme == 1) {
            cbStimmarten[i][offset + 4].setSelected(true);
        } else {
            cbStimmarten[i][offset + 4].setSelected(false);
        }
        if (ParamS.param.paramAbstimmungParameter.nichtTeilnahmeMoeglich == 1) {
            hBox.getChildren().add(lNichtTeilnahme);
            hBox.getChildren().add(cbStimmarten[i][offset + 4]);
        }

        //        Gegenanträge "nur markiert" wird nicht mehr unterstützt
        //        Label lSonstiges = new Label("Geg.:M");
        //        if (sonstiges1 == 1) {
        //            cbStimmarten[i][offset + 5].setSelected(true);
        //        } else {
        //            cbStimmarten[i][offset + 5].setSelected(false);
        //        }
        //        if (ParamS.param.paramAbstimmungParameter.gegenantragMarkiertMoeglich == 1) {
        //            hBox.getChildren().add(lSonstiges);
        //            hBox.getChildren().add(cbStimmarten[i][offset + 5]);
        //        }

        grpnAbstimmungen.addMeeting(hBox, spalte, i * 2 + 1);
    }

    /**
     * Fuelle combo box speichern unmarkiert.
     *
     * @param pComboBox the combo box
     * @param pWert     the wert
     */
    private void fuelleComboBoxSpeichernUnmarkiert(ComboBox<String> pComboBox, int pWert) {
        pComboBox.getItems().add("P");
        if (pWert == -1) {
            pComboBox.setValue("P");
        }

        pComboBox.getItems().add(KonstStimmart.getTextKurz(KonstStimmart.nichtMarkiert));
        if (pWert == KonstStimmart.nichtMarkiert) {
            pComboBox.setValue(KonstStimmart.getTextKurz(KonstStimmart.nichtMarkiert));
        }

        pComboBox.getItems().add(KonstStimmart.getTextKurz(KonstStimmart.ja));
        if (pWert == KonstStimmart.ja) {
            pComboBox.setValue(KonstStimmart.getTextKurz(KonstStimmart.ja));
        }

        pComboBox.getItems().add(KonstStimmart.getTextKurz(KonstStimmart.nein));
        if (pWert == KonstStimmart.nein) {
            pComboBox.setValue(KonstStimmart.getTextKurz(KonstStimmart.nein));
        }

        pComboBox.getItems().add(KonstStimmart.getTextKurz(KonstStimmart.enthaltung));
        if (pWert == KonstStimmart.enthaltung) {
            pComboBox.setValue(KonstStimmart.getTextKurz(KonstStimmart.enthaltung));
        }

    }

    /**
     * Zeige sortierung tb stimmarten.
     */
    @SuppressWarnings("unchecked")
    private void zeigeSortierungTbStimmarten() {
        zeigeSortierungAbstimmungenInit();

        grpnAbstimmungen = new MeetingGridPane();
        //		grpnAbstimmungen.setVgap(5);
        //		grpnAbstimmungen.setHgap(15);

        int spalte = 0;

        if (angezeigteAbstimmungen != null && angezeigteAbstimmungen.length > 0) {
            cbStimmarten = new CheckBox[angezeigteAbstimmungen.length][24];
            cbStimmenAuswerten = new ComboBox[angezeigteAbstimmungen.length];
            cbWeisungNichtMarkierteSpeichernAlsSRV = new ComboBox[angezeigteAbstimmungen.length];
            cbWeisungNichtMarkierteSpeichernAlsBriefwahl = new ComboBox[angezeigteAbstimmungen.length];
            cbWeisungNichtMarkierteSpeichernAlsKIAV = new ComboBox[angezeigteAbstimmungen.length];
            cbWeisungNichtMarkierteSpeichernAlsDauer = new ComboBox[angezeigteAbstimmungen.length];
            cbWeisungNichtMarkierteSpeichernAlsOrg = new ComboBox[angezeigteAbstimmungen.length];
            cbWeisungHVNichtMarkierteSpeichernAls = new ComboBox[angezeigteAbstimmungen.length];
            cbAbstimmungNichtMarkierteSpeichernAls = new ComboBox[angezeigteAbstimmungen.length];

            for (int i = 0; i < angezeigteAbstimmungen.length; i++) {
                spalte = 0;
                Label hLabel1 = new Label(Integer.toString(angezeigteAbstimmungen[i].ident));
                grpnAbstimmungen.addMeeting(hLabel1, spalte, i * 2 + 1);
                spalte++;

                Label lblNummer = new Label(angezeigteAbstimmungen[i].nummerKey);
                lblNummer.setMinWidth(50);
                grpnAbstimmungen.addMeeting(lblNummer, spalte, i * 2 + 1);
                spalte++;

                Label lblNummerIndex = new Label(angezeigteAbstimmungen[i].nummerindexKey);
                lblNummerIndex.setMinWidth(50);
                grpnAbstimmungen.addMeeting(lblNummerIndex, spalte, i * 2 + 1);
                spalte++;

                Label lblKurzBezeichnung = new Label(angezeigteAbstimmungen[i].kurzBezeichnung);
                lblKurzBezeichnung.setMinWidth(200);
                grpnAbstimmungen.addMeeting(lblKurzBezeichnung, spalte, i * 2 + 1);
                spalte++;

                Label hLabel5 = new Label("");
                if (angezeigteAbstimmungen[i].identWeisungssatz == -1) {
                    hLabel5.setText("Ja");
                }
                grpnAbstimmungen.addMeeting(hLabel5, spalte, i * 2 + 1);
                spalte++;

                /*Elemente füllen, da sonst bei Überschrift "null"*/
                for (int i1 = 0; i1 < 24; i1++) {
                    cbStimmarten[i][i1] = new CheckBox();
                }
                cbStimmenAuswerten[i] = new ComboBox<String>();

                /*Weisung speichern als*/
                HBox hBox = new HBox();

                Label lBestandSRV = new Label("Bestand: SRV:");
                hBox.getChildren().add(lBestandSRV);
                cbWeisungNichtMarkierteSpeichernAlsSRV[i] = new ComboBox<String>();
                fuelleComboBoxSpeichernUnmarkiert(cbWeisungNichtMarkierteSpeichernAlsSRV[i],
                        angezeigteAbstimmungen[i].weisungNichtMarkierteSpeichernAlsSRV);
                hBox.getChildren().add(cbWeisungNichtMarkierteSpeichernAlsSRV[i]);

                Label lBestandBriefwahl = new Label(" Briefw.:");
                cbWeisungNichtMarkierteSpeichernAlsBriefwahl[i] = new ComboBox<String>();
                fuelleComboBoxSpeichernUnmarkiert(cbWeisungNichtMarkierteSpeichernAlsBriefwahl[i],
                        angezeigteAbstimmungen[i].weisungNichtMarkierteSpeichernAlsBriefwahl);
                if (ParamS.param.paramModuleKonfigurierbar.briefwahl) {
                    hBox.getChildren().add(lBestandBriefwahl);
                    hBox.getChildren().add(cbWeisungNichtMarkierteSpeichernAlsBriefwahl[i]);
                }

                Label lBestandKIAV = new Label(" KIAV:");
                hBox.getChildren().add(lBestandKIAV);
                cbWeisungNichtMarkierteSpeichernAlsKIAV[i] = new ComboBox<String>();
                fuelleComboBoxSpeichernUnmarkiert(cbWeisungNichtMarkierteSpeichernAlsKIAV[i],
                        angezeigteAbstimmungen[i].weisungNichtMarkierteSpeichernAlsKIAV);
                hBox.getChildren().add(cbWeisungNichtMarkierteSpeichernAlsKIAV[i]);

                Label lBestandDauer = new Label(" Dauer:");
                hBox.getChildren().add(lBestandDauer);
                cbWeisungNichtMarkierteSpeichernAlsDauer[i] = new ComboBox<String>();
                fuelleComboBoxSpeichernUnmarkiert(cbWeisungNichtMarkierteSpeichernAlsDauer[i],
                        angezeigteAbstimmungen[i].weisungNichtMarkierteSpeichernAlsDauer);
                hBox.getChildren().add(cbWeisungNichtMarkierteSpeichernAlsDauer[i]);

                Label lBestandOrg = new Label(" Org:");
                hBox.getChildren().add(lBestandOrg);
                cbWeisungNichtMarkierteSpeichernAlsOrg[i] = new ComboBox<String>();
                fuelleComboBoxSpeichernUnmarkiert(cbWeisungNichtMarkierteSpeichernAlsOrg[i],
                        angezeigteAbstimmungen[i].weisungNichtMarkierteSpeichernAlsOrg);
                hBox.getChildren().add(cbWeisungNichtMarkierteSpeichernAlsOrg[i]);

                Label lVerlassenHV = new Label(" Weis.Verl.HV:");
                hBox.getChildren().add(lVerlassenHV);
                cbWeisungHVNichtMarkierteSpeichernAls[i] = new ComboBox<String>();
                fuelleComboBoxSpeichernUnmarkiert(cbWeisungHVNichtMarkierteSpeichernAls[i],
                        angezeigteAbstimmungen[i].weisungHVNichtMarkierteSpeichernAls);
                hBox.getChildren().add(cbWeisungHVNichtMarkierteSpeichernAls[i]);

                Label lAbstimmung = new Label(" Abst.:");
                hBox.getChildren().add(lAbstimmung);
                cbAbstimmungNichtMarkierteSpeichernAls[i] = new ComboBox<String>();
                fuelleComboBoxSpeichernUnmarkiert(cbAbstimmungNichtMarkierteSpeichernAls[i],
                        angezeigteAbstimmungen[i].abstimmungNichtMarkierteSpeichernAls);
                hBox.getChildren().add(cbAbstimmungNichtMarkierteSpeichernAls[i]);

                if (angezeigteAbstimmungen[i].identWeisungssatz != -1) {

                    if (ParamS.param.paramModuleKonfigurierbar.aktionaersportal
                            || ParamS.param.paramModuleKonfigurierbar.elektronischeWeisungserfassungHV) {
                        fuelleStimmart(i, 0, spalte, angezeigteAbstimmungen[i].externJa,
                                angezeigteAbstimmungen[i].externNein, angezeigteAbstimmungen[i].externEnthaltung,
                                angezeigteAbstimmungen[i].externUngueltig,
                                angezeigteAbstimmungen[i].externNichtTeilnahme,
                                angezeigteAbstimmungen[i].externSonstiges1);
                        spalte++;
                    }
                    fuelleStimmart(i, 6, spalte, angezeigteAbstimmungen[i].internJa,
                            angezeigteAbstimmungen[i].internNein, angezeigteAbstimmungen[i].internEnthaltung,
                            angezeigteAbstimmungen[i].internUngueltig, angezeigteAbstimmungen[i].internNichtTeilnahme,
                            angezeigteAbstimmungen[i].internSonstiges1);
                    spalte++;
                    if (ParamS.param.paramModuleKonfigurierbar.elektronischerStimmblock) {
                        fuelleStimmart(i, 12, spalte, angezeigteAbstimmungen[i].elStiBloJa,
                                angezeigteAbstimmungen[i].elStiBloNein, angezeigteAbstimmungen[i].elStiBloEnthaltung,
                                angezeigteAbstimmungen[i].elStiBloUngueltig,
                                angezeigteAbstimmungen[i].elStiBloNichtTeilnahme,
                                angezeigteAbstimmungen[i].elStiBloSonstiges1);
                        spalte++;
                    }
                    if (ParamS.param.paramModuleKonfigurierbar.tabletAbstimmung) {
                        fuelleStimmart(i, 18, spalte, angezeigteAbstimmungen[i].tabletJa,
                                angezeigteAbstimmungen[i].tabletNein, angezeigteAbstimmungen[i].tabletEnthaltung,
                                angezeigteAbstimmungen[i].tabletUngueltig,
                                angezeigteAbstimmungen[i].tabletNichtTeilnahme,
                                angezeigteAbstimmungen[i].tabletSonstiges1);
                        spalte++;
                    }

                    int wert = 0;
                    switch (angezeigteAbstimmungen[i].stimmenAuswerten) {
                    case -1:
                        wert = 0;
                        break;
                    case KonstStimmart.ja:
                        wert = 1;
                        break;
                    case KonstStimmart.nein:
                        wert = 2;
                        break;
                    case KonstStimmart.enthaltung:
                        wert = 3;
                        break;
                    }
                    cbStimmenAuswerten[i].setMinWidth(50);
                    CtrlParameterAbstimmungAllgemein.fuelleCbStimmenAuswerten(cbStimmenAuswerten[i], wert);
                    grpnAbstimmungen.addMeeting(cbStimmenAuswerten[i], spalte, i * 2 + 1);
                    spalte++;

                    grpnAbstimmungen.addMeeting(hBox, spalte, i * 2 + 1);
                    spalte++;
                }

            }
        }

        List<String> ueberschriftList = new LinkedList<String>();
        ueberschriftList.add("Ident");
        ueberschriftList.add("TOP");
        ueberschriftList.add("TOPIndex");
        ueberschriftList.add("Kurzbezeichnung");
        ueberschriftList.add("Überschrift");
        if (ParamS.param.paramModuleKonfigurierbar.aktionaersportal
                || ParamS.param.paramModuleKonfigurierbar.elektronischeWeisungserfassungHV) {
            ueberschriftList.add("Weisungen Portal/Elektronisch");
        }
        ueberschriftList.add("Weisungen intern/Stimmabgabe");
        if (ParamS.param.paramModuleKonfigurierbar.elektronischerStimmblock) {
            ueberschriftList.add("Elek. Stimmblock");
        }
        if (ParamS.param.paramModuleKonfigurierbar.tabletAbstimmung) {
            ueberschriftList.add("Tablet/App");
        }
        ueberschriftList.add("Auswerten");
        ueberschriftList.add("Bei man.Eing./Stapelscan: nicht-markierte speichern als (P wie Param)");
        String[] uberschriftString = new String[ueberschriftList.size()];
        for (int i = 0; i < ueberschriftList.size(); i++) {
            uberschriftString[i] = ueberschriftList.get(i);
        }
        grpnAbstimmungen.setzeUeberschrift(uberschriftString, scrPnStimmarten);
    }

    /**
     * Fuelle stimmausschluss KZ.
     *
     * @param i          the i
     * @param i1         the i 1
     * @param ausschluss the ausschluss
     * @param hBox       the h box
     */
    private void fuelleStimmausschlussKZ(int i, int i1, char ausschluss, HBox hBox) {
        Label lL = new Label(Character.toString(ausschluss));
        hBox.getChildren().add(lL);
        if (angezeigteAbstimmungen[i].stimmausschluss.indexOf(ausschluss) != -1) {
            cbStimmausschluss[i][i1].setSelected(true);
        } else {
            cbStimmausschluss[i][i1].setSelected(false);
        }
        hBox.getChildren().add(cbStimmausschluss[i][i1]);
    }

    /**
     * Zeige sortierung tb stimmrecht.
     */
    private void zeigeSortierungTbStimmrecht() {
        zeigeSortierungAbstimmungenInit();

        grpnAbstimmungen = new MeetingGridPane();
        //		grpnAbstimmungen.setVgap(5);
        //		grpnAbstimmungen.setHgap(15);

        int spalte = 0;

        if (angezeigteAbstimmungen != null && angezeigteAbstimmungen.length > 0) {
            cbStimmausschluss = new CheckBox[angezeigteAbstimmungen.length][13];
            btnPauschalausschluss = new Button[angezeigteAbstimmungen.length];
            btnEinzelausschluss = new Button[angezeigteAbstimmungen.length];
            cbStimmberechtigteGattungen = new CheckBox[angezeigteAbstimmungen.length][5];

            for (int i = 0; i < angezeigteAbstimmungen.length; i++) {
                spalte = 0;
                Label hLabel1 = new Label(Integer.toString(angezeigteAbstimmungen[i].ident));
                grpnAbstimmungen.addMeeting(hLabel1, spalte, i * 2 + 1);
                spalte++;

                Label lblNummer = new Label(angezeigteAbstimmungen[i].nummerKey);
                lblNummer.setMinWidth(50);
                grpnAbstimmungen.addMeeting(lblNummer, spalte, i * 2 + 1);
                spalte++;

                Label lblNummerIndex = new Label(angezeigteAbstimmungen[i].nummerindexKey);
                lblNummerIndex.setMinWidth(50);
                grpnAbstimmungen.addMeeting(lblNummerIndex, spalte, i * 2 + 1);
                spalte++;

                Label lblKurzBezeichnung = new Label(angezeigteAbstimmungen[i].kurzBezeichnung);
                lblKurzBezeichnung.setMinWidth(200);
                grpnAbstimmungen.addMeeting(lblKurzBezeichnung, spalte, i * 2 + 1);
                spalte++;

                Label hLabel5 = new Label(""); /*Überschrift*/
                if (angezeigteAbstimmungen[i].identWeisungssatz == -1) {
                    hLabel5.setText("Ja");
                }
                grpnAbstimmungen.addMeeting(hLabel5, spalte, i * 2 + 1);
                spalte++;

                /*Stimmausschluss*/
                HBox hBox = new HBox();

                /*Elemente füllen, da sonst bei Überschrift "null"*/
                for (int i1 = 0; i1 < 13; i1++) {
                    cbStimmausschluss[i][i1] = new CheckBox();
                }

                fuelleStimmausschlussKZ(i, 0, 'V', hBox);
                fuelleStimmausschlussKZ(i, 1, 'A', hBox);
                fuelleStimmausschlussKZ(i, 2, 'S', hBox);
                fuelleStimmausschlussKZ(i, 3, '1', hBox);
                fuelleStimmausschlussKZ(i, 4, '2', hBox);
                fuelleStimmausschlussKZ(i, 5, '3', hBox);
                fuelleStimmausschlussKZ(i, 6, '4', hBox);
                fuelleStimmausschlussKZ(i, 7, '5', hBox);
                fuelleStimmausschlussKZ(i, 8, '6', hBox);
                fuelleStimmausschlussKZ(i, 9, '7', hBox);
                fuelleStimmausschlussKZ(i, 10, '8', hBox);
                fuelleStimmausschlussKZ(i, 11, '9', hBox);
                fuelleStimmausschlussKZ(i, 12, 'E', hBox);
                if (angezeigteAbstimmungen[i].identWeisungssatz != -1) {
                    grpnAbstimmungen.addMeeting(hBox, spalte, i * 2 + 1);
                }
                spalte++;

                /*Einzelausschluß*/
                btnEinzelausschluss[i] = new Button();
                btnEinzelausschluss[i].setText("Pflegen Einzelausschluss");
                btnEinzelausschluss[i].setOnAction(e -> {
                    clickedDetailPflege(e);
                });
                if (angezeigteAbstimmungen[i].identWeisungssatz != -1) {
                    grpnAbstimmungen.addMeeting(btnEinzelausschluss[i], spalte, i * 2 + 1);
                }
                spalte++;

                /*Pauschalausschluss*/
                int pauschalAusschlussVorhanden = -1;
                for (int i1 = 0; i1 < 5; i1++) {
                    if (angezeigteAbstimmungen[i].pauschalAusschluss[i1] != 0) {
                        pauschalAusschlussVorhanden = 1;
                    }
                }
                btnPauschalausschluss[i] = new Button();
                btnPauschalausschluss[i].setOnAction(e -> {
                    clickedDetailPflege(e);
                });
                if (pauschalAusschlussVorhanden == 1) {
                    btnPauschalausschluss[i].setText("Pauschalausschluß vorhanden - Pflegen");
                } else {
                    btnPauschalausschluss[i].setText("Pauschalausschluß anlegen");
                }
                if (angezeigteAbstimmungen[i].identWeisungssatz != -1) {
                    grpnAbstimmungen.addMeeting(btnPauschalausschluss[i], spalte, i * 2 + 1);
                }
                spalte++;

                /*Stimmberechtigte Gattungen*/
                HBox hBoxGattungen = new HBox();
                for (int i1 = 0; i1 < 5; i1++) {
                    cbStimmberechtigteGattungen[i][i1] = new CheckBox();/*Elemente füllen, da sonst bei Überschrift "null"*/
                    Label hLabel = new Label(ParamS.param.paramBasis.gattungBezeichnungKurz[i1]);
                    if (ParamS.param.paramBasis.gattungAktiv[i1]) {
                        hBoxGattungen.getChildren().add(hLabel);
                        if (angezeigteAbstimmungen[i].stimmberechtigteGattungen[i1] == 1) {
                            cbStimmberechtigteGattungen[i][i1].setSelected(true);
                        }
                        hBoxGattungen.getChildren().add(cbStimmberechtigteGattungen[i][i1]);
                    }
                }
                //    			if (angezeigteAbstimmungen[i].identWeisungssatz!=-1){
                grpnAbstimmungen.addMeeting(hBoxGattungen, spalte, i * 2 + 1);
                spalte++;
                //    			}
            }
        }

        List<String> ueberschriftList = new LinkedList<String>();
        ueberschriftList.add("Ident");
        ueberschriftList.add("TOP");
        ueberschriftList.add("TOPIndex");
        ueberschriftList.add("Kurzbezeichnung");
        ueberschriftList.add("Überschrift");
        ueberschriftList.add("Stimmausschluss");
        ueberschriftList.add("");
        ueberschriftList.add("");
        ueberschriftList.add("Stimmberech.Gattungen");
        String[] uberschriftString = new String[ueberschriftList.size()];
        for (int i = 0; i < ueberschriftList.size(); i++) {
            uberschriftString[i] = ueberschriftList.get(i);
        }
        grpnAbstimmungen.setzeUeberschrift(uberschriftString, scrPnStimmrecht);
    }

    /**
     * Zeige sortierung tb details.
     */
    @SuppressWarnings("unchecked")
    private void zeigeSortierungTbDetails() {
        zeigeSortierungAbstimmungenInit();

        grpnAbstimmungen = new MeetingGridPane();
        //		grpnAbstimmungen.setVgap(5);
        //		grpnAbstimmungen.setHgap(15);

        int spalte = 0;

        if (angezeigteAbstimmungen != null && angezeigteAbstimmungen.length > 0) {
            cbErforderlicheMehrheit = new ComboBox[angezeigteAbstimmungen.length];
            cbZuAbstimmungsgruppe = new ComboBox[angezeigteAbstimmungen.length];
            cbIstGegenantrag = new CheckBox[angezeigteAbstimmungen.length];
            cbIstErgaenzungsantrag = new CheckBox[angezeigteAbstimmungen.length];
            cbGegenantraegeGestellt = new ComboBox[angezeigteAbstimmungen.length];
            cbVonIdentGesamtweisung = new ComboBox[angezeigteAbstimmungen.length];
            cbBeschlussvorschlagGestelltVon = new ComboBox[angezeigteAbstimmungen.length];
            tfBeschlussvorschlagGestelltVonSonstige = new MaxLengthTextField[angezeigteAbstimmungen.length];

            CtrlParameterAbstimmungAllgemein.belegeAbstimmungArray();

            for (int i = 0; i < angezeigteAbstimmungen.length; i++) {
                spalte = 0;
                Label hLabel1 = new Label(Integer.toString(angezeigteAbstimmungen[i].ident));
                grpnAbstimmungen.addMeeting(hLabel1, spalte, i * 2 + 1);
                spalte++;

                Label lblNummer = new Label(angezeigteAbstimmungen[i].nummerKey);
                lblNummer.setMinWidth(50);
                grpnAbstimmungen.addMeeting(lblNummer, spalte, i * 2 + 1);
                spalte++;

                Label lblNummerIndex = new Label(angezeigteAbstimmungen[i].nummerindexKey);
                lblNummerIndex.setMinWidth(50);
                grpnAbstimmungen.addMeeting(lblNummerIndex, spalte, i * 2 + 1);
                spalte++;

                Label lblKurzBezeichnung = new Label(angezeigteAbstimmungen[i].kurzBezeichnung);
                lblKurzBezeichnung.setMinWidth(200);
                grpnAbstimmungen.addMeeting(lblKurzBezeichnung, spalte, i * 2 + 1);
                spalte++;

                Label hLabel5 = new Label(""); /*Überschrift*/
                if (angezeigteAbstimmungen[i].identWeisungssatz == -1) {
                    hLabel5.setText("Ja");
                }
                grpnAbstimmungen.addMeeting(hLabel5, spalte, i * 2 + 1);
                spalte++;

                cbErforderlicheMehrheit[i] = new ComboBox<String>();
                CtrlParameterAbstimmungAllgemein.fuelleCbErforderlicheMehrheit(cbErforderlicheMehrheit[i],
                        angezeigteAbstimmungen[i].identErforderlicheMehrheit);
                if (angezeigteAbstimmungen[i].identWeisungssatz != -1) {
                    grpnAbstimmungen.addMeeting(cbErforderlicheMehrheit[i], spalte, i * 2 + 1);
                }
                spalte++;

                cbZuAbstimmungsgruppe[i] = new ComboBox<String>();
                for (int i1 = 0; i1 < cbZuAbstimmungsgruppeMoeglichkeiten.length; i1++) {
                    cbZuAbstimmungsgruppe[i].getItems().addAll(cbZuAbstimmungsgruppeMoeglichkeiten[i1]);
                    if (i1 == angezeigteAbstimmungen[i].zuAbstimmungsgruppe) {
                        cbZuAbstimmungsgruppe[i].setValue(cbZuAbstimmungsgruppeMoeglichkeiten[i1]);
                    }
                }
                if (angezeigteAbstimmungen[i].identWeisungssatz != -1) {
                    grpnAbstimmungen.addMeeting(cbZuAbstimmungsgruppe[i], spalte, i * 2 + 1);
                }
                spalte++;

                cbIstGegenantrag[i] = new CheckBox();
                if (angezeigteAbstimmungen[i].gegenantrag == 1) {
                    cbIstGegenantrag[i].setSelected(true);
                }
                //                if (angezeigteAbstimmungen[i].identWeisungssatz != -1) {
                grpnAbstimmungen.addMeeting(cbIstGegenantrag[i], spalte, i * 2 + 1);
                //                }
                spalte++;

                cbIstErgaenzungsantrag[i] = new CheckBox();
                if (angezeigteAbstimmungen[i].ergaenzungsantrag == 1) {
                    cbIstErgaenzungsantrag[i].setSelected(true);
                }
                if (angezeigteAbstimmungen[i].identWeisungssatz != -1) {
                    grpnAbstimmungen.addMeeting(cbIstErgaenzungsantrag[i], spalte, i * 2 + 1);
                }
                spalte++;

                cbGegenantraegeGestellt[i] = new ComboBox<String>();
                for (int i1 = 0; i1 < gegenantraegeGestelltMoeglichkeiten.length; i1++) {
                    cbGegenantraegeGestellt[i].getItems().addAll(gegenantraegeGestelltMoeglichkeiten[i1]);
                    if (i1 == angezeigteAbstimmungen[i].gegenantraegeGestellt) {
                        cbGegenantraegeGestellt[i].setValue(gegenantraegeGestelltMoeglichkeiten[i1]);

                    }
                }
                if (angezeigteAbstimmungen[i].identWeisungssatz != -1) {
                    grpnAbstimmungen.addMeeting(cbGegenantraegeGestellt[i], spalte, i * 2 + 1);
                }
                spalte++;

                if (angezeigteAbstimmungen[i].identWeisungssatz != -1) {
                    Label uLabel = new Label(Integer.toString(angezeigteAbstimmungen[i].identWeisungssatz));
                    grpnAbstimmungen.addMeeting(uLabel, spalte, i * 2 + 1);
                }
                spalte++;

                /*Übernahme von Gesamtmarkierung*/
                cbVonIdentGesamtweisung[i] = new ComboBox<String>();
                CtrlParameterAbstimmungAllgemein
                        .belegeMitAbstimmungArray(cbVonIdentGesamtweisung[i],
                                CtrlParameterAbstimmungAllgemein
                                        .holeIdentZuIdentGesamtweisung(angezeigteAbstimmungen[i].vonIdentGesamtweisung),
                                false, "keine Übernahme (-1)");
                if (angezeigteAbstimmungen[i].identWeisungssatz != -1) {
                    grpnAbstimmungen.addMeeting(cbVonIdentGesamtweisung[i], spalte, i * 2 + 1);
                }
                spalte++;

                cbBeschlussvorschlagGestelltVon[i] = new ComboBox<String>();
                CtrlParameterAbstimmungAllgemein.fuelleCbBeschlussvorschlagVon(cbBeschlussvorschlagGestelltVon[i],
                        angezeigteAbstimmungen[i].beschlussvorschlagGestelltVon);
                if (angezeigteAbstimmungen[i].identWeisungssatz != -1) {
                    grpnAbstimmungen.addMeeting(cbBeschlussvorschlagGestelltVon[i], spalte, i * 2 + 1);
                }
                spalte++;

                tfBeschlussvorschlagGestelltVonSonstige[i] = new MaxLengthTextField(200);

                tfBeschlussvorschlagGestelltVonSonstige[i]
                        .setText(angezeigteAbstimmungen[i].beschlussvorschlagGestelltVonSonstige);
                tfBeschlussvorschlagGestelltVonSonstige[i].setMinWidth(200);
                if (angezeigteAbstimmungen[i].identWeisungssatz != -1) {
                    grpnAbstimmungen.addMeeting(tfBeschlussvorschlagGestelltVonSonstige[i], spalte, i * 2 + 1);
                }
                spalte++;

            }
        }

        List<String> ueberschriftList = new LinkedList<String>();
        ueberschriftList.add("Ident");
        ueberschriftList.add("TOP");
        ueberschriftList.add("TOPIndex");
        ueberschriftList.add("Kurzbezeichnung");
        ueberschriftList.add("Überschrift");
        ueberschriftList.add("Erford.Mehrheit");
        ueberschriftList.add("zu Abstimmungsgru.");
        ueberschriftList.add("ist Gegenantr.");
        ueberschriftList.add("ist Ergä.antr.");
        ueberschriftList.add("Gegenantr. gest.");
        ueberschriftList.add("Ident.Weisung");
        ueberschriftList.add("Übern.Von Gesamtmark.");
        ueberschriftList.add("Antr.gest.von");
        ueberschriftList.add("Antr.gest.von-sonstige");

        String[] uberschriftString = new String[ueberschriftList.size()];
        for (int i = 0; i < ueberschriftList.size(); i++) {
            uberschriftString[i] = ueberschriftList.get(i);
        }
        grpnAbstimmungen.setzeUeberschrift(uberschriftString, scrPnDetails);
    }

    /**
     * Zeige sortierung tb drucken.
     */
    private void zeigeSortierungTbDrucken() {
        zeigeSortierungAbstimmungenInit();

        grpnAbstimmungen = new MeetingGridPane();
        grpnAbstimmungen.setVgap(5);
        grpnAbstimmungen.setHgap(15);

        int spalte = 0;

        Label uLabel1 = new Label("Ident");
        grpnAbstimmungen.add(uLabel1, spalte, 0);
        spalte++;

        Label uLabel2 = new Label("Nr");
        grpnAbstimmungen.add(uLabel2, spalte, 0);
        spalte++;

        Label uLabel3 = new Label("NrIndex");
        grpnAbstimmungen.add(uLabel3, spalte, 0);
        spalte++;

        Label uLabel4 = new Label("Kurzbezeichnung");
        grpnAbstimmungen.add(uLabel4, spalte, 0);
        spalte++;

        Label uLabel5 = new Label("Überschrift");
        grpnAbstimmungen.add(uLabel5, spalte, 0);
        spalte++;

        Label uLabel6 = new Label("Formular kurz");
        grpnAbstimmungen.add(uLabel6, spalte, 0);
        spalte++;

        Label uLabel7 = new Label("Formular lang");
        grpnAbstimmungen.add(uLabel7, spalte, 0);
        spalte++;

        Label uLabel8 = new Label("Formular Bühne");
        grpnAbstimmungen.add(uLabel8, spalte, 0);
        spalte++;

        if (angezeigteAbstimmungen != null && angezeigteAbstimmungen.length > 0) {
            tfFormularKurz = new MaxLengthNumericTextField[angezeigteAbstimmungen.length];
            tfFormularLang = new MaxLengthNumericTextField[angezeigteAbstimmungen.length];
            tfFormularBuehne = new MaxLengthNumericTextField[angezeigteAbstimmungen.length];

            for (int i = 0; i < angezeigteAbstimmungen.length; i++) {
                spalte = 0;
                Label hLabel1 = new Label(Integer.toString(angezeigteAbstimmungen[i].ident));
                grpnAbstimmungen.add(hLabel1, spalte, i * 2 + 1);
                spalte++;

                Label lblNummer = new Label(angezeigteAbstimmungen[i].nummer);
                lblNummer.setMinWidth(50);
                grpnAbstimmungen.add(lblNummer, spalte, i * 2 + 1);
                spalte++;

                Label lblNummerIndex = new Label(angezeigteAbstimmungen[i].nummerindex);
                lblNummerIndex.setMinWidth(50);
                grpnAbstimmungen.add(lblNummerIndex, spalte, i * 2 + 1);
                spalte++;

                Label lblKurzBezeichnung = new Label(angezeigteAbstimmungen[i].kurzBezeichnung);
                lblKurzBezeichnung.setMinWidth(200);
                grpnAbstimmungen.add(lblKurzBezeichnung, spalte, i * 2 + 1);
                spalte++;

                Label hLabel5 = new Label(""); /*Überschrift*/
                if (angezeigteAbstimmungen[i].identWeisungssatz == -1) {
                    hLabel5.setText("Ja");
                }
                grpnAbstimmungen.add(hLabel5, spalte, i * 2 + 1);
                spalte++;

                tfFormularKurz[i] = new MaxLengthNumericTextField(4);
                tfFormularKurz[i].setText(Integer.toString(angezeigteAbstimmungen[i].formularKurz));
                grpnAbstimmungen.add(tfFormularKurz[i], spalte, i * 2 + 1);
                spalte++;

                tfFormularLang[i] = new MaxLengthNumericTextField(4);
                tfFormularLang[i].setText(Integer.toString(angezeigteAbstimmungen[i].formularLang));
                grpnAbstimmungen.add(tfFormularLang[i], spalte, i * 2 + 1);
                spalte++;

                tfFormularBuehne[i] = new MaxLengthNumericTextField(4);
                tfFormularBuehne[i].setText(Integer.toString(angezeigteAbstimmungen[i].formularBuehnenInfo));
                grpnAbstimmungen.add(tfFormularBuehne[i], spalte, i * 2 + 1);
                spalte++;

            }
        }
        scrPnDrucken.setContent(grpnAbstimmungen);
    }

    /**
     * Zeige sortierung tp abstimmungsvorgang.
     */
    @SuppressWarnings("unchecked")
    private void zeigeSortierungTpAbstimmungsvorgang() {

        CtrlParameterAbstimmungAllgemein.belegeAbstimmungsblockArray();
        angezeigteAbstimmungsblock = CtrlParameterAbstimmungAllgemein.abstimmungsblockArray;

        if (angezeigteAbstimmungsblock != null) {
            abstimmungWurdeVeraendert = new boolean[angezeigteAbstimmungsblock.length];
            for (int i = 0; i < abstimmungWurdeVeraendert.length; i++) {
                abstimmungWurdeVeraendert[i] = false;
            }
        }

        grpnAbstimmungen = new MeetingGridPane();
        //		grpnAbstimmungen.setVgap(5);
        //		grpnAbstimmungen.setHgap(15);

        tfAbstimmungsvorgangPosition = null;
        tfAbstimmungsvorgangKurzBeschreibung = null;
        tfAbstimmungsvorgangBeschreibung = null;
        tfAbstimmungsvorgangBeschreibungEN = null;
        cbAbstimmungsvorgangAktiv = null;

        int spalte = 0;

        if (angezeigteAbstimmungsblock != null && angezeigteAbstimmungsblock.length > 0) {
            tfAbstimmungsvorgangPosition = new MaxLengthNumericTextField[angezeigteAbstimmungsblock.length];
            tfAbstimmungsvorgangKurzBeschreibung = new MaxLengthTextField[angezeigteAbstimmungsblock.length];
            tfAbstimmungsvorgangBeschreibung = new MaxLengthTextField[angezeigteAbstimmungsblock.length];
            tfAbstimmungsvorgangBeschreibungEN = new MaxLengthTextField[angezeigteAbstimmungsblock.length];
            cbAbstimmungsvorgangAktiv = new ComboBox[angezeigteAbstimmungsblock.length];

            for (int i = 0; i < angezeigteAbstimmungsblock.length; i++) {
                spalte = 0;
                Label hLabel1 = new Label(Integer.toString(angezeigteAbstimmungsblock[i].ident));
                grpnAbstimmungen.addMeeting(hLabel1, spalte, i * 2 + 1);
                spalte++;

                tfAbstimmungsvorgangKurzBeschreibung[i] = new MaxLengthTextField(40);//40
                tfAbstimmungsvorgangKurzBeschreibung[i].setText(angezeigteAbstimmungsblock[i].kurzBeschreibung);
                tfAbstimmungsvorgangKurzBeschreibung[i].setMinWidth(200);
                grpnAbstimmungen.addMeeting(tfAbstimmungsvorgangKurzBeschreibung[i], spalte, i * 2 + 1);
                spalte++;

                tfAbstimmungsvorgangBeschreibung[i] = new MaxLengthTextField(80);
                tfAbstimmungsvorgangBeschreibung[i].setText(angezeigteAbstimmungsblock[i].beschreibung);
                tfAbstimmungsvorgangBeschreibung[i].setMinWidth(400);
                grpnAbstimmungen.addMeeting(tfAbstimmungsvorgangBeschreibung[i], spalte, i * 2 + 1);

                tfAbstimmungsvorgangBeschreibungEN[i] = new MaxLengthTextField(80);
                tfAbstimmungsvorgangBeschreibungEN[i].setText(angezeigteAbstimmungsblock[i].beschreibungEN);
                tfAbstimmungsvorgangBeschreibungEN[i].setMinWidth(400);
                if (ParamS.param.paramModuleKonfigurierbar.englischeAgenda) {
                    grpnAbstimmungen.addMeeting(tfAbstimmungsvorgangBeschreibungEN[i], spalte, i * 2 + 2);
                    spalte++;
                }

                tfAbstimmungsvorgangPosition[i] = new MaxLengthNumericTextField(4);
                tfAbstimmungsvorgangPosition[i].setText(Integer.toString(angezeigteAbstimmungsblock[i].position));
                if (ParamS.param.paramModuleKonfigurierbar.tabletAbstimmung
                        || ParamS.param.paramModuleKonfigurierbar.hvAppAbstimmung) {
                    grpnAbstimmungen.addMeeting(tfAbstimmungsvorgangPosition[i], spalte, i * 2 + 1);
                    spalte++;
                }

                cbAbstimmungsvorgangAktiv[i] = new ComboBox<String>();
                cbAbstimmungsvorgangAktiv[i].setMinWidth(300);
                cbAbstimmungsvorgangAktiv[i].getItems().clear();
                for (int i1 = 0; i1 < aktivAbstimmungsblockMoeglichkeiten.length; i1++) {
                    cbAbstimmungsvorgangAktiv[i].getItems().addAll(aktivAbstimmungsblockMoeglichkeiten[i1]);
                    if (i1 == angezeigteAbstimmungsblock[i].aktiv) {
                        cbAbstimmungsvorgangAktiv[i].setValue(aktivAbstimmungsblockMoeglichkeiten[i1]);
                    }
                }
                grpnAbstimmungen.addMeeting(cbAbstimmungsvorgangAktiv[i], spalte, i * 2 + 1);
                spalte++;

            }

        }
        List<String> ueberschriftList = new LinkedList<String>();
        ueberschriftList.add("Ident");
        ueberschriftList.add("Kurzbeschreibung (intern)");
        String uBeschreibung = "Beschreibung";
        if (ParamS.param.paramModuleKonfigurierbar.englischeAgenda) {
            uBeschreibung = uBeschreibung + " D/E";
        }
        ueberschriftList.add(uBeschreibung);
        if (ParamS.param.paramModuleKonfigurierbar.tabletAbstimmung
                || ParamS.param.paramModuleKonfigurierbar.hvAppAbstimmung) {
            ueberschriftList.add("Pos TablApp");
        }
        ueberschriftList.add("Aktiv");

        String[] uberschriftString = new String[ueberschriftList.size()];
        for (int i = 0; i < ueberschriftList.size(); i++) {
            uberschriftString[i] = ueberschriftList.get(i);
        }
        grpnAbstimmungen.setzeUeberschrift(uberschriftString, scrPnAbstimmungsvorgang);
    }

    /**
     * Zeige sortierung tp elek stimmkarte.
     */
    @SuppressWarnings("unchecked")
    private void zeigeSortierungTpElekStimmkarte() {
        CtrlParameterAbstimmungAllgemein.belegeStimmkarteInhaltArray();
        angezeigteElekStimmkarte = CtrlParameterAbstimmungAllgemein.stimmkarteInhaltArray;

        if (angezeigteElekStimmkarte != null) {
            abstimmungWurdeVeraendert = new boolean[angezeigteElekStimmkarte.length];
            for (int i = 0; i < abstimmungWurdeVeraendert.length; i++) {
                abstimmungWurdeVeraendert[i] = false;
            }
        }

        grpnAbstimmungen = new MeetingGridPane();
        grpnAbstimmungen.setVgap(5);
        grpnAbstimmungen.setHgap(15);

        tfElekStimmkartePosition = null;
        tfElekStimmkarteKurzBeschreibung = null;
        tfElekStimmkarteBeschreibung = null;
        tfElekStimmkarteBeschreibungEN = null;
        cbElekStimmkarteAktiv = null;

        int spalte = 0;

        Label uLabel1 = new Label("Ident");
        grpnAbstimmungen.add(uLabel1, spalte, 0);
        spalte++;

        Label uLabel5 = new Label("Kurzbeschreibung (intern)");
        grpnAbstimmungen.add(uLabel5, spalte, 0);
        spalte++;

        Label uLabel2 = new Label("Beschreibung (D/E)");
        grpnAbstimmungen.add(uLabel2, spalte, 0);
        spalte++;

        Label uLabel3 = new Label("PosInElBlock");
        grpnAbstimmungen.add(uLabel3, spalte, 0);
        spalte++;

        Label uLabel4 = new Label("Aktiv");
        grpnAbstimmungen.add(uLabel4, spalte, 0);
        spalte++;

        if (angezeigteElekStimmkarte != null && angezeigteElekStimmkarte.length > 0) {
            tfElekStimmkartePosition = new MaxLengthNumericTextField[angezeigteElekStimmkarte.length];
            tfElekStimmkarteKurzBeschreibung = new MaxLengthTextField[angezeigteElekStimmkarte.length];
            tfElekStimmkarteBeschreibung = new MaxLengthTextField[angezeigteElekStimmkarte.length];
            tfElekStimmkarteBeschreibungEN = new MaxLengthTextField[angezeigteElekStimmkarte.length];
            cbElekStimmkarteAktiv = new ComboBox[angezeigteElekStimmkarte.length];

            for (int i = 0; i < angezeigteElekStimmkarte.length; i++) {
                spalte = 0;
                Label hLabel1 = new Label(Integer.toString(angezeigteElekStimmkarte[i].stimmkartenNr));
                grpnAbstimmungen.add(hLabel1, spalte, i * 2 + 1);
                spalte++;

                tfElekStimmkarteKurzBeschreibung[i] = new MaxLengthTextField(40);
                tfElekStimmkarteKurzBeschreibung[i].setText(angezeigteElekStimmkarte[i].kurzBezeichnung);
                tfElekStimmkarteKurzBeschreibung[i].setMinWidth(200);
                grpnAbstimmungen.add(tfElekStimmkarteKurzBeschreibung[i], spalte, i * 2 + 1);
                spalte++;

                tfElekStimmkarteBeschreibung[i] = new MaxLengthTextField(80);
                tfElekStimmkarteBeschreibung[i].setText(angezeigteElekStimmkarte[i].stimmkartenBezeichnung);
                tfElekStimmkarteBeschreibung[i].setMinWidth(400);
                grpnAbstimmungen.add(tfElekStimmkarteBeschreibung[i], spalte, i * 2 + 1);

                tfElekStimmkarteBeschreibungEN[i] = new MaxLengthTextField(80);
                tfElekStimmkarteBeschreibungEN[i].setText(angezeigteElekStimmkarte[i].stimmkartenBezeichnungEN);
                tfElekStimmkarteBeschreibungEN[i].setMinWidth(400);
                grpnAbstimmungen.add(tfElekStimmkarteBeschreibungEN[i], spalte, i * 2 + 2);
                spalte++;

                tfElekStimmkartePosition[i] = new MaxLengthNumericTextField(4);
                tfElekStimmkartePosition[i].setText(Integer.toString(angezeigteElekStimmkarte[i].posInBlock));
                grpnAbstimmungen.add(tfElekStimmkartePosition[i], spalte, i * 2 + 1);
                spalte++;

                cbElekStimmkarteAktiv[i] = new ComboBox<String>();
                cbElekStimmkarteAktiv[i].setMinWidth(300);
                cbElekStimmkarteAktiv[i].getItems().clear();
                for (int i1 = 0; i1 < aktivElekStimmkarteMoeglichkeiten.length; i1++) {
                    cbElekStimmkarteAktiv[i].getItems().addAll(aktivElekStimmkarteMoeglichkeiten[i1]);
                    if (i1 == angezeigteElekStimmkarte[i].stimmkarteIstAktiv) {
                        cbElekStimmkarteAktiv[i].setValue(aktivElekStimmkarteMoeglichkeiten[i1]);
                    }
                }
                grpnAbstimmungen.add(cbElekStimmkarteAktiv[i], spalte, i * 2 + 1);
                spalte++;

            }
        }

        scrPnElekStimmkarte.setContent(grpnAbstimmungen);
    }

    /**
     * Voraussetzung: lDbBundle muß geöffnet sein.
     */
    private void fuelleFilterFuerAnzeige() {
        stubAbstimmungen.fuelleFilterFuerAnzeige();
        EclAbstimmungsblock[] abstimmungsblockListe = stubAbstimmungen.abstimmungsblockListe;
        EclStimmkarteInhalt[] stimmkarteInhalt = stubAbstimmungen.stimmkarteInhalt;

        comboFilterFuerAnzeige.getItems().removeAll(comboFilterFuerAnzeige.getItems());

        CbElement lElement1 = new CbElement();
        lElement1.anzeige = "alle";
        lElement1.ident1 = 1;
        comboAllgemeinFuerAnzeige.addElement(lElement1);
        CbElement lElement2 = new CbElement();
        lElement2.anzeige = "alle aktiven Weisungen Intern";
        lElement2.ident1 = 2;
        comboAllgemeinFuerAnzeige.addElement(lElement2);
        if (ParamS.param.paramModuleKonfigurierbar.aktionaersportal) {
            CbElement lElement3 = new CbElement();
            lElement3.anzeige = "alle aktiven Weisungen Portal";
            lElement3.ident1 = 3;
            comboAllgemeinFuerAnzeige.addElement(lElement3);
        }
        CbElement lElement4 = new CbElement();
        lElement4.anzeige = "alle aktiven Weisungen Verlassen HV";
        lElement4.ident1 = 2;
        comboAllgemeinFuerAnzeige.addElement(lElement4);

        int anzAbstimmungsblock = abstimmungsblockListe.length;
        if (anzAbstimmungsblock > 0) {
            for (int i = 0; i < anzAbstimmungsblock; i++) {
                EclAbstimmungsblock lAbstimmungsblock = abstimmungsblockListe[i];
                CbElement lElement = new CbElement();
                lElement.anzeige = "Abstimmungsvorgang (" + Integer.toString(lAbstimmungsblock.ident) + "): "
                        + lAbstimmungsblock.beschreibung;
                lElement.ident2 = lAbstimmungsblock.ident;
                comboAllgemeinFuerAnzeige.addElement(lElement);
            }
        }

        if (ParamS.param.paramModuleKonfigurierbar.elektronischerStimmblock) {
            int anz = stimmkarteInhalt.length;
            if (anz > 0) {
                for (int i = 0; i < anz; i++) {
                    EclStimmkarteInhalt lStimmkarteInhalt = stimmkarteInhalt[i];
                    CbElement lElement = new CbElement();
                    lElement.anzeige = "Elektronische Stimmkarte (" + Integer.toString(lStimmkarteInhalt.stimmkartenNr)
                            + "): " + lStimmkarteInhalt.stimmkartenBezeichnung;
                    lElement.ident3 = lStimmkarteInhalt.stimmkartenNr;
                    comboAllgemeinFuerAnzeige.addElement(lElement);
                }
            }
        }
    }

    /**
     * Fuelle sortierung fuer anzeige.
     */
    private void fuelleSortierungFuerAnzeige() {
        comboSortierung.getItems().clear();

        int anz = 0;
        anz = sortierMoeglichkeiten.length;

        for (int i = 0; i < anz; i++) {
            boolean einfuegen = true;

            switch (filterTyp) {
            case 0: { //alle
                switch (i) {
                case 3:
                case 4:
                case 5:
                case 6:
                    einfuegen = false;
                    break;
                }
                break;
            }
            case 1: { //Abstimmungsblock
                switch (i) {
                case 3:
                    einfuegen = false;
                    break;
                }
                break;
            }
            case 2: { //elektronische Stimmkarte
                switch (i) {
                case 4:
                case 5:
                case 6:
                    einfuegen = false;
                    break;
                }
                break;
            }
            }

            if (!ParamS.param.paramModuleKonfigurierbar.aktionaersportal) {
                if (i == 2) {
                    einfuegen = false;
                }
            }
            if (!ParamS.param.paramModuleKonfigurierbar.elektronischerStimmblock) {
                if (i == 3) {
                    einfuegen = false;
                }
            }
            if (!ParamS.param.paramModuleKonfigurierbar.tabletAbstimmung
                    && !ParamS.param.paramModuleKonfigurierbar.hvAppAbstimmung) {
                if (i == 5) {
                    einfuegen = false;
                }
            }

            if (einfuegen) {
                CbElement cbElement = new CbElement();
                cbElement.ident1 = i;
                cbElement.anzeige = sortierMoeglichkeiten[i];
                comboAllgemeinSortierung.addElement(cbElement);
            }

        }

    }

    /**
     * Füllt comboBox mit allen gespeicherten Abstimmungen. ohneAngezeigte true ->
     * es werden nur die aufgenommen, die nicht in der aktuellen Anzeige drin sind.
     * false -> alle aufnehmen.
     * 
     * Returnwert: false -> es wurde keiner aufgenommen
     * 
     * open/close wird in der Funktion durchgeführt.
     *
     * @param ohneAngezeigte the ohne angezeigte
     * @return true, if successful
     */
    private boolean fuelleComboAbstimmung(boolean ohneAngezeigte) {
        stubAbstimmungen.fuelleComboAbstimmung();
        EclAbstimmung[] abstimmungListe = stubAbstimmungen.abstimmungListe;

        comboAbstimmung.getItems().removeAll(comboAbstimmung.getItems());
        int anz = abstimmungListe.length;
        int anzInComboBox = 0;
        if (anz > 0) {
            for (int i = 0; i < anz; i++) {
                EclAbstimmung lAbstimmung = abstimmungListe[i];

                int angezeigt = 0;
                if (ohneAngezeigte) {
                    for (int i1 = 0; i1 < angezeigteAbstimmungen.length; i1++) {
                        if (angezeigteAbstimmungen[i1].ident == lAbstimmung.ident) {
                            angezeigt = 1;
                        }
                    }
                }

                if (angezeigt == 0) {
                    String h1Text = "(" + lAbstimmung.ident + ") " + lAbstimmung.nummer + " " + lAbstimmung.nummerindex
                            + " " + lAbstimmung.kurzBezeichnung;
                    comboAbstimmung.getItems().add(h1Text);
                    if (aktuelleFunktion == 11) {
                        if (lAbstimmung.ident == angezeigteAbstimmungZuStimmkarte[ausgewaehlteAbstimmung].identAbstimmung) {
                            comboAbstimmung.setValue(h1Text);
                        }
                    }
                    anzInComboBox++;
                }
            }
        }
        if (anzInComboBox == 0) {
            return false;
        }
        return true;
    }

    /**
     * Fuelle neu loeschen button.
     */
    private void fuelleNeuLoeschenButton() {
        switch (filterTyp) {
        case 0:
            btnNeueAbstimmung.setText("Neue Abstimmung");
            btnAbstimmungLoeschen.setText("Abstimmung löschen");
            break;
        case 1:
            btnNeueAbstimmung.setText("Neue Zuordnung");
            btnAbstimmungLoeschen.setText("Zuordnung löschen");
            break;
        case 2:
            btnNeueAbstimmung.setText("Neue Zuordnung");
            btnAbstimmungLoeschen.setText("Zuordnung löschen");
            break;
        }
        btnNeueAbstimmung.setDisable(false);
    }

    /**
     * Fuelle neu loeschen button abstimmungsvorgang.
     */
    private void fuelleNeuLoeschenButtonAbstimmungsvorgang() {
        btnNeueAbstimmungAbstimmungsvorgang.setText("Neuer Vorgang");
        btnAbstimmungLoeschenAbstimmungsvorgang.setText("Vorgang löschen");
        btnNeueAbstimmungAbstimmungsvorgang.setDisable(false);
        btnAbstimmungLoeschenAbstimmungsvorgang.setDisable(false);
    }

    /**
     * Fuelle neu loeschen button elek stimmkarte.
     */
    private void fuelleNeuLoeschenButtonElekStimmkarte() {
        btnNeueStimmkarteElekStimmkarte.setText("Neue El.Stimmkarte");
        btnStimmkarteLoeschenElekStimmkarte.setText("El. Stimmkarte löschen");
        btnNeueStimmkarteElekStimmkarte.setDisable(false);
        btnStimmkarteLoeschenElekStimmkarte.setDisable(false);
    }

    /**
     * Deaktiviere tabs.
     *
     * @param deaktiviere the deaktiviere
     */
    private void deaktiviereTabs(boolean deaktiviere) {
        tpBezeichnungen.setDisable(deaktiviere);
        tpAktivierung.setDisable(deaktiviere);
        tpStimmarten.setDisable(deaktiviere);
        tpStimmrecht.setDisable(deaktiviere);
        tpDetails.setDisable(deaktiviere);
        tpDrucken.setDisable(/*deaktiviere*/true); //derzeit nie aktiv
        tpAbstimmungsvorgang.setDisable(deaktiviere);
        if (ParamS.param.paramModuleKonfigurierbar.elektronischerStimmblock) {
            tpElekStimmkarte.setDisable(deaktiviere);
        }

    }

    /** The befehlszeile. */
    private boolean befehlszeile = false;

    /**
     * Aktivieren / Deaktivieren der tpBezeichnungen für Zuordnungs-Änderung: >
     * Tab-Umschaltung > Filter-Box > Sortier-Box.
     *
     * @param deaktiviere the deaktiviere
     */
    private void aktiviereZuordnungsmodus(boolean deaktiviere) {
        deaktiviereTabs(deaktiviere);
        comboFilterFuerAnzeige.setDisable(deaktiviere);
        comboSortierung.setDisable(deaktiviere);
        if (deaktiviere == false) {
            /*Anzeige in der Befehlszeile verbergen bzw. auf Normalzustand setzen*/
            fuelleNeuLoeschenButton();
        }
        befehlszeile = deaktiviere;

    }

    /**
     * Aktivieren der Befehlszeile unterhalb des Scrollbereichs. Alle anderen
     * Buttons deaktivieren. Ebenso Filter und Tabs
     *
     * @param deaktiviere the deaktiviere
     */
    private void deaktiviereBefehlszeile(boolean deaktiviere) {
        deaktiviereTabs(deaktiviere);

        comboFilterFuerAnzeige.setDisable(deaktiviere);
        comboSortierung.setDisable(deaktiviere);

        btnSpeichern.setDisable(deaktiviere);

        int anz = 0;
        if (btnVeraendernPosWeisungIntern != null) {
            anz = btnVeraendernPosWeisungIntern.length;
        }
        for (int i = 0; i < anz; i++) {
            btnVeraendernPosWeisungIntern[i].setDisable(deaktiviere);
            btnVeraendernPosWeisungPortal[i].setDisable(deaktiviere);
            btnVeraendernPosInAbstimmungsblock[i].setDisable(deaktiviere);
            btnVeraendernPosInPapierStimmkarte[i].setDisable(deaktiviere);
            btnVeraendernPosInElekStimmkarte[i].setDisable(deaktiviere);
            btnVeraendernAbstInElekStimmkarte[i].setDisable(deaktiviere);
        }

        befehlszeile = deaktiviere;

        if (deaktiviere == false) {
            /*Anzeige in der Befehlszeile verbergen bzw. auf Normalzustand setzen*/
            fuelleNeuLoeschenButton();
            lblFunktion.setVisible(false);
            tfEingabe1.setVisible(false);
            lblEingabe1.setVisible(false);
            tfEingabe2.setVisible(false);
            lblEingabe2.setVisible(false);
            comboAbstimmung.setVisible(false);
        }
    }

    /**
     * Deaktiviere befehlszeile abstimmungsvorgang.
     *
     * @param deaktiviere the deaktiviere
     */
    private void deaktiviereBefehlszeileAbstimmungsvorgang(boolean deaktiviere) {
        tpUebersicht.setDisable(deaktiviere);
        tpBezeichnungen.setDisable(deaktiviere);
        tpAktivierung.setDisable(deaktiviere);
        tpStimmarten.setDisable(deaktiviere);
        tpStimmrecht.setDisable(deaktiviere);
        tpDetails.setDisable(deaktiviere);
        tpDrucken.setDisable(/*deaktiviere*/true); //derzeit nie aktiv
        if (ParamS.param.paramModuleKonfigurierbar.elektronischerStimmblock) {
            tpElekStimmkarte.setDisable(deaktiviere);
        }

        btnSpeichern.setDisable(deaktiviere);

        befehlszeile = deaktiviere;

        if (deaktiviere == false) {
            /*Anzeige in der Befehlszeile verbergen bzw. auf Normalzustand setzen*/
            fuelleNeuLoeschenButtonAbstimmungsvorgang();
            lblFunktionAbstimmungsvorgang.setVisible(false);
            tfEingabe1Abstimmungsvorgang.setVisible(false);
            lblEingabe1Abstimmungsvorgang.setVisible(false);
            comboAbstimmungsvorgang.setVisible(false);
        }
    }

    /**
     * Deaktiviere befehlszeile elek stimmkarte.
     *
     * @param deaktiviere the deaktiviere
     */
    private void deaktiviereBefehlszeileElekStimmkarte(boolean deaktiviere) {
        tpUebersicht.setDisable(deaktiviere);
        tpBezeichnungen.setDisable(deaktiviere);
        tpAktivierung.setDisable(deaktiviere);
        tpStimmarten.setDisable(deaktiviere);
        tpStimmrecht.setDisable(deaktiviere);
        tpDetails.setDisable(deaktiviere);
        tpDrucken.setDisable(/*deaktiviere*/true); //derzeit nie aktiv
        tpAbstimmungsvorgang.setDisable(deaktiviere);

        btnSpeichern.setDisable(deaktiviere);

        befehlszeile = deaktiviere;

        if (deaktiviere == false) {
            /*Anzeige in der Befehlszeile verbergen bzw. auf Normalzustand setzen*/
            fuelleNeuLoeschenButtonElekStimmkarte();
            lblFunktionElekStimmkarte.setVisible(false);
            tfEingabe1ElekStimmkarte.setVisible(false);
            lblEingabe1ElekStimmkarte.setVisible(false);
            comboElekStimmkarte.setVisible(false);
        }
    }

    /** The eigene stage. */
    private Stage eigeneStage;

    /**
     * Inits the.
     *
     * @param pEigeneStage the eigene stage
     */
    public void init(Stage pEigeneStage) {
        eigeneStage = pEigeneStage;
    }

    /**
     * **************Ab hier
     * Oberflächen-Aktionen************************************.
     *
     * @param event the event
     */

    @FXML
    void clickedSpeichern(ActionEvent event) {
        int rc = abfragenReload();
        if (rc < 1) {
            return;
        }
        rc = speichern();
        if (rc >= 1) {
            eigeneStage.hide();
        }
    }

    /**
     * lDbBundle wird in der Funktion geöffnet/geschlossen.
     *
     * @param event the event
     */
    @FXML
    void clickedVeraendernPos(ActionEvent event) {

        for (int i = 0; i < btnVeraendernPosWeisungIntern.length; i++) {
            if (event.getSource() == btnVeraendernPosWeisungIntern[i]) {
                ausgewaehlteAbstimmung = i;
                aktuelleFunktion = 1;
            }
            if (event.getSource() == btnVeraendernPosWeisungPortal[i]) {
                ausgewaehlteAbstimmung = i;
                aktuelleFunktion = 2;
            }
            if (event.getSource() == btnVeraendernPosInAbstimmungsblock[i]) {
                ausgewaehlteAbstimmung = i;
                aktuelleFunktion = 3;
            }
            if (event.getSource() == btnVeraendernPosInPapierStimmkarte[i]) {
                ausgewaehlteAbstimmung = i;
                aktuelleFunktion = 4;
            }
            if (event.getSource() == btnVeraendernPosInElekStimmkarte[i]) {
                ausgewaehlteAbstimmung = i;
                aktuelleFunktion = 5;
            }
            if (event.getSource() == btnVeraendernAbstInElekStimmkarte[i]) {
                ausgewaehlteAbstimmung = i;
                aktuelleFunktion = 6;
            }
            if (event.getSource() == btnVeraendernDruckPos[i]) {
                ausgewaehlteAbstimmung = i;
                aktuelleFunktion = 7;
            }
            if (event.getSource() == btnVeraendernPosWeisungVerlassenHV[i]) {
                ausgewaehlteAbstimmung = i;
                aktuelleFunktion = 8;
            }

        }

        String hText = "";
        if (aktuelleFunktion != 6) {
            hText = "Ident " + Integer.toString(angezeigteAbstimmungen[ausgewaehlteAbstimmung].ident) + " verschieben";
        } else {
            hText = "Ident " + Integer.toString(angezeigteAbstimmungen[ausgewaehlteAbstimmung].ident)
                    + " Verwendete Abst.Ident";
        }
        lblFunktion.setText(hText);
        lblFunktion.setVisible(true);

        if (aktuelleFunktion != 6) {
            tfEingabe1.setVisible(true);
            tfEingabe1.setText("");
            Platform.runLater(() -> tfEingabe1.requestFocus());
        }

        boolean fehler = false;
        switch (aktuelleFunktion) {
        case 1:
            lblEingabe1.setText("an PosIntern");
            lblEingabe1.setVisible(true);
            break;
        case 2:
            lblEingabe1.setText("an PosExternWeisung");
            lblEingabe1.setVisible(true);
            break;
        case 3:
            lblEingabe1.setText("an Seite Tabl.App");
            lblEingabe1.setVisible(true);
            lblEingabe2.setText("an Pos Tabl.App");
            lblEingabe2.setVisible(true);
            tfEingabe2.setText("");
            tfEingabe2.setVisible(true);
            break;
        case 4:
            lblEingabe1.setText("StiKNr");
            lblEingabe1.setVisible(true);
            tfEingabe2.setVisible(true);
            tfEingabe2.setText("");
            lblEingabe2.setText("Pos StiK");
            lblEingabe2.setVisible(true);
            break;
        case 5:
            lblEingabe1.setText("Pos ElStiK");
            lblEingabe1.setVisible(true);
            break;
        case 6:
            boolean brc = fuelleComboAbstimmung(false);
            if (!brc) {
                fehler = true;
            } else {
                comboAbstimmung.setVisible(true);
            }
            break;
        case 7:
            lblEingabe1.setText("Pos Druck");
            lblEingabe1.setVisible(true);
            break;
        case 8:
            lblEingabe1.setText("an PosExternWeisungHV");
            lblEingabe1.setVisible(true);
            break;
        }

        if (fehler == true) {
            btnNeueAbstimmung.setDisable(true);
        }
        btnNeueAbstimmung.setText("Ausführen");
        btnAbstimmungLoeschen.setText("Abbrechen");

        deaktiviereBefehlszeile(true);

        System.out
                .println("ausgewaehlteAbstimmung=" + ausgewaehlteAbstimmung + " aktuelleFunktion=" + aktuelleFunktion);

    }

    /**
     * On tp changed.
     *
     * @param event the event
     */
    @FXML
    void onTpChanged(MouseEvent event) {
        if (angezeigterTab >= 0) {
            /*bisherigen Tab abspeichern*/
            int rc = 1;
            switch (angezeigterTab) {
            case 1:
                rc = speichernVeraendertePositionenBezeichnungen();
                break;
            case 2:
                rc = speichernVeraendertePositionenAktivierung();
                break;
            case 3:
                rc = speichernVeraendertePositionenStimmarten();
                break;
            case 4:
                rc = speichernVeraendertePositionenStimmrecht();
                break;
            case 5:
                rc = speichernVeraendertePositionenDetails();
                break;
            case 6:
                rc = speichernVeraendertePositionenDrucken();
                break;
            case 7:
                rc = speichernVeraendertePositionenAbstimmungsvorgang();
                break;
            case 8:
                rc = speichernVeraendertePositionenElStimmkarte();
                break;
            }
            if (rc <= 0) {
                tbPaneAnnzeige.getSelectionModel().select(angezeigterTab);
                return;
            }
        }
        angezeigterTabAlt = angezeigterTab;
        if (tpUebersicht.isSelected() && angezeigterTab != 0) {
            angezeigterTab = 0;
            zeigeTab();
        }
        if (tpBezeichnungen.isSelected() && angezeigterTab != 1) {
            angezeigterTab = 1;
            zeigeTab();
        }
        if (tpAktivierung.isSelected() && angezeigterTab != 2) {
            angezeigterTab = 2;
            zeigeTab();
        }
        if (tpStimmarten.isSelected() && angezeigterTab != 3) {
            angezeigterTab = 3;
            zeigeTab();
        }
        if (tpStimmrecht.isSelected() && angezeigterTab != 4) {
            angezeigterTab = 4;
            zeigeTab();
        }
        if (tpDetails.isSelected() && angezeigterTab != 5) {
            angezeigterTab = 5;
            zeigeTab();
        }
        if (tpDrucken.isSelected() && angezeigterTab != 6) {
            angezeigterTab = 6;
            zeigeTab();
        }
        if (tpAbstimmungsvorgang.isSelected() && angezeigterTab != 7) {
            angezeigterTab = 7;
            zeigeTab();
        }
        if (tpElekStimmkarte.isSelected() && angezeigterTab != 8) {
            angezeigterTab = 8;
            zeigeTab();
        }
    }

    /**
     * Zeige tab.
     */
    private void zeigeTab() {

        if (angezeigterTab == 7 || angezeigterTab == 8) {
            /*Filter und Sortierung deaktivieren*/
            comboFilterFuerAnzeige.setDisable(true);
            comboSortierung.setDisable(true);
        } else {
            comboFilterFuerAnzeige.setDisable(false);
            comboSortierung.setDisable(false);
        }

        if ((angezeigterTabAlt == 7 || angezeigterTabAlt == 8) && angezeigterTab != 7 && angezeigterTab != 8) {
            /*Dann ggf. neue Stimmblöcke etc., d.h. Filter und Anzeige neu einlesen*/
            reloadUnterdruecken = true;
            filterTyp = 0;
            sortierung = 0;

            fuelleSortierungFuerAnzeige();
            comboAllgemeinSortierung.setAusgewaehlt1(sortierung);

            fuelleFilterFuerAnzeige();
            comboAllgemeinFuerAnzeige.setAusgewaehlt1(1); //Alle
            reloadUnterdruecken = false;
        }
        zeigeSortierung();

        System.out.println("angezeigterTab=" + angezeigterTab);
    }

    /**
     * Neu-Button. Falls Menuezeile aktiv, dann "Ausführen"
     *
     * @param event the event
     */
    @FXML
    void clickedNeu(ActionEvent event) {
        if (befehlszeile == true) {
            switch (aktuelleFunktion) {
            case 1: {/*Verschieben interne Position*/
                int alteIdent = 0;
                int neueIdent = 0;
                alteIdent = angezeigteAbstimmungen[ausgewaehlteAbstimmung].anzeigePositionIntern;

                lFehlertext = "";
                pruefeZahlVonBis(tfEingabe1, "Ziel-Pos", 0, 9999);
                if (!lFehlertext.isEmpty()) {
                    fehlerMeldung(lFehlertext);
                    return;
                }
                neueIdent = Integer.parseInt(tfEingabe1.getText());
                if (neueIdent == alteIdent) {
                    fehlerMeldung("Ziel-Pos ist identisch mit bisheriger Pos!");
                    return;
                }

                /*Durchführung*/
                fuehreVerschiebenAus(alteIdent, -1, neueIdent, -1);
                return;
            }
            case 2: {/*Verschieben PosExternWeisung*/
                int alteIdent = 0;
                int neueIdent = 0;
                alteIdent = angezeigteAbstimmungen[ausgewaehlteAbstimmung].anzeigePositionExternWeisungen;

                lFehlertext = "";
                pruefeZahlVonBis(tfEingabe1, "Ziel-Pos", 0, 9999);
                if (!lFehlertext.isEmpty()) {
                    fehlerMeldung(lFehlertext);
                    return;
                }
                neueIdent = Integer.parseInt(tfEingabe1.getText());
                if (neueIdent == alteIdent) {
                    fehlerMeldung("Ziel-Pos ist identisch mit bisheriger Pos!");
                    return;
                }

                /*Durchführung*/
                fuehreVerschiebenAus(alteIdent, -1, neueIdent, -1);
                return;
            }
            case 3: {/*Verschieben Position in Tablet/App*/
                int alteIdent = 0;
                int neueIdent = 0;
                int alteIdentSeite = 0;
                int neueIdentSeite = 0;
                alteIdent = angezeigteAbstimmungZuAbstimmungsblock[ausgewaehlteAbstimmung].position;
                alteIdentSeite = angezeigteAbstimmungZuAbstimmungsblock[ausgewaehlteAbstimmung].seite;

                pruefeZahlVonBis(tfEingabe1, "Ziel-Seite", 0, 9999);
                if (!lFehlertext.isEmpty()) {
                    fehlerMeldung(lFehlertext);
                    return;
                }
                neueIdentSeite = Integer.parseInt(tfEingabe1.getText());

                lFehlertext = "";
                pruefeZahlVonBis(tfEingabe2, "Ziel-Position", 0, 9999);
                if (!lFehlertext.isEmpty()) {
                    fehlerMeldung(lFehlertext);
                    return;
                }
                neueIdent = Integer.parseInt(tfEingabe2.getText());

                if (neueIdent == alteIdent && neueIdentSeite == alteIdentSeite) {
                    fehlerMeldung("Ziel-Position/Seite ist identisch mit bisheriger Position/Seite!");
                    return;
                }

                /*Durchführung*/
                fuehreVerschiebenAus(alteIdentSeite, alteIdent, neueIdentSeite, neueIdent);
                return;
            }
            case 4: {/*Abstimmungsblock: Stimmkartennummer/ Position StimmkartenNr.*/
                int neueStiKNr = 0;
                int neueIdent = 0;

                lFehlertext = "";
                pruefeZahlVonBis(tfEingabe1, "Stimmkartennummer", 0, 9999);
                if (!lFehlertext.isEmpty()) {
                    fehlerMeldung(lFehlertext);
                    return;
                }
                neueStiKNr = Integer.parseInt(tfEingabe1.getText());

                lFehlertext = "";
                pruefeZahlVonBis(tfEingabe2, "Ziel-Position", 0, 9999);
                if (!lFehlertext.isEmpty()) {
                    fehlerMeldung(lFehlertext);
                    return;
                }
                neueIdent = Integer.parseInt(tfEingabe2.getText());

                /*Durchführung - kein Verschieben, nur neu setzen*/
                angezeigteAbstimmungZuAbstimmungsblock[ausgewaehlteAbstimmung].nummerDerStimmkarte = neueStiKNr;
                angezeigteAbstimmungZuAbstimmungsblock[ausgewaehlteAbstimmung].positionAufStimmkarte = neueIdent;
                abstimmungWurdeVeraendert[ausgewaehlteAbstimmung] = true;
                speichernNachVerschieben();
                return;
            }
            case 5: {/*Elektronische Stimmkarte - Position*/
                int alteIdent = 0;
                int neueIdent = 0;
                alteIdent = angezeigteAbstimmungZuStimmkarte[ausgewaehlteAbstimmung].positionInStimmkarte;

                lFehlertext = "";
                pruefeZahlVonBis(tfEingabe1, "Ziel-Position", 0, 9999);
                if (!lFehlertext.isEmpty()) {
                    fehlerMeldung(lFehlertext);
                    return;
                }
                neueIdent = Integer.parseInt(tfEingabe1.getText());

                /*Durchführung*/
                fuehreVerschiebenAus(alteIdent, -1, neueIdent, -1);
                return;
            }
            case 6: {/*Verwendete Abstimmung auf elektronischer Stimmkarte*/
                int neueIdent = 0;
                String neuerWert = comboAbstimmung.getValue();
                int hIndex = neuerWert.indexOf('(');
                int hIndexEnde = neuerWert.indexOf(')');
                String identString = neuerWert.substring(hIndex + 1, hIndexEnde);
                neueIdent = Integer.parseInt(identString);

                /*Durchführung - nur setzen*/
                angezeigteAbstimmungZuStimmkarte[ausgewaehlteAbstimmung].identAbstimmung = neueIdent;
                speichernNachVerschieben();
                return;
            }
            case 7: {/*Abstimmungsblock - DruckPosition*/
                int alteIdent = 0;
                int neueIdent = 0;
                alteIdent = angezeigteAbstimmungZuAbstimmungsblock[ausgewaehlteAbstimmung].positionInAusdruck;

                lFehlertext = "";
                pruefeZahlVonBis(tfEingabe1, "Ziel-Position", 0, 9999);
                if (!lFehlertext.isEmpty()) {
                    fehlerMeldung(lFehlertext);
                    return;
                }
                neueIdent = Integer.parseInt(tfEingabe1.getText());

                /*Durchführung*/
                fuehreVerschiebenAus(alteIdent, -1, neueIdent, -1);
                return;
            }
            case 8: {/*Verschieben PosExternWeisungHV*/
                int alteIdent = 0;
                int neueIdent = 0;
                alteIdent = angezeigteAbstimmungen[ausgewaehlteAbstimmung].anzeigePositionExternWeisungenHV;

                lFehlertext = "";
                pruefeZahlVonBis(tfEingabe1, "Ziel-Position", 0, 9999);
                if (!lFehlertext.isEmpty()) {
                    fehlerMeldung(lFehlertext);
                    return;
                }
                neueIdent = Integer.parseInt(tfEingabe1.getText());
                if (neueIdent == alteIdent) {
                    fehlerMeldung("Ziel-Pos ist identisch mit bisheriger Pos!");
                    return;
                }

                /*Durchführung*/
                fuehreVerschiebenAus(alteIdent, -1, neueIdent, -1);
                return;
            }
            case 12: {/*Abstimmung löschen*/
                lFehlertext = "";
                /*Eingabe formal korrekt?*/
                String feldname = "";
                if (aktuelleFunktion == 13) {
                    feldname = "Interne Ident";
                } else {
                    feldname = "Ident";
                }

                pruefeZahlNichtLeerLaenge(tfEingabe1, feldname, 4);
                if (!lFehlertext.isEmpty()) {
                    fehlerMeldung(lFehlertext);
                    return;
                }
                int ident = Integer.parseInt(tfEingabe1.getText());

                /*Gibts diese Ident?*/
                int gef = -1;
                if (angezeigteAbstimmungen != null) {
                    for (int i = 0; i < angezeigteAbstimmungen.length; i++) {
                        if (angezeigteAbstimmungen[i].ident == ident) {
                            gef = i;
                        }
                    }
                }
                if (gef == -1) {
                    fehlerMeldung("Ident nicht vorhanden!");
                    return;
                }

                /*Durchführen*/
                int rc = stubAbstimmungen.loescheAbstimmung(ident);
                if (rc < 0) {
                    fehlerMeldung(CaFehler.getFehlertext(rc, 0));
                    return;
                }
                deaktiviereBefehlszeile(false);
                zeigeSortierung();
                return;
            }
            case 13: {/*Abstimmungsvorgang Zuordnung löschen*/
                int gef = -1;
                List<EclAbstimmungZuAbstimmungsblock> zuordnungLoeschen = new LinkedList<EclAbstimmungZuAbstimmungsblock>();
                for (int i = 0; i < angezeigteAbstimmungen.length; i++) {
                    if (cbZuordnung[i].isSelected()) {
                        gef = 1;
                        zuordnungLoeschen.add(angezeigteAbstimmungZuAbstimmungsblock[i]);
                    }
                }
                if (gef == -1) {
                    this.fehlerMeldung("Bitte eine Abstimmung zum Löschen der Zuordnung auswählen!");
                    return;
                }
                /*Speichern*/
                stubAbstimmungen.loeschenListeAbstimmungZuAbstimmungsblock(zuordnungLoeschen);

                /*Funktion beenden*/
                filterTyp = 1;
                zeigeSortierungTbUebersicht();
                aktiviereZuordnungsmodus(false);
                return;
            }
            case 14: {/*ElStimmk Zuordnung löschen*/
                int gef = -1;
                List<EclAbstimmungenZuStimmkarte> zuordnungLoeschen = new LinkedList<EclAbstimmungenZuStimmkarte>();
                for (int i = 0; i < angezeigteAbstimmungen.length; i++) {
                    if (cbZuordnung[i].isSelected()) {
                        gef = 1;
                        zuordnungLoeschen.add(angezeigteAbstimmungZuStimmkarte[i]);
                    }
                }
                if (gef == -1) {
                    this.fehlerMeldung("Bitte eine Abstimmung zum Löschen der Zuordnung auswählen!");
                    return;
                }
                /*Speichern*/
                stubAbstimmungen.loeschenListeAbstimmungZuElekStimmkarte(zuordnungLoeschen);

                /*Funktion beenden*/
                filterTyp = 2;
                zeigeSortierungTbUebersicht();
                aktiviereZuordnungsmodus(false);
                return;
            }
            case 15: {/*Neue Zuordnung Abstimmungsblock*/
                int gef = -1;
                List<EclAbstimmungZuAbstimmungsblock> neueZuordnung = new LinkedList<EclAbstimmungZuAbstimmungsblock>();
                for (int i = 0; i < angezeigteAbstimmungen.length; i++) {
                    if (cbZuordnung[i].isSelected()) {
                        gef = 1;
                        EclAbstimmungZuAbstimmungsblock lAbstimmungZuAbsimmungsblock = new EclAbstimmungZuAbstimmungsblock();
                        lAbstimmungZuAbsimmungsblock.identAbstimmungsblock = filterIdent;
                        lAbstimmungZuAbsimmungsblock.identAbstimmung = angezeigteAbstimmungen[i].ident;
                        neueZuordnung.add(lAbstimmungZuAbsimmungsblock);
                    }
                }
                if (gef == -1) {
                    this.fehlerMeldung("Bitte eine Abstimmung zur Zuordnung auswählen!");
                    return;
                }
                /*Speichern*/
                stubAbstimmungen.speichernListeNeueAbstimmungZuAbstimmungsblock(neueZuordnung);

                /*Funktion beenden*/
                filterTyp = 1;
                zeigeSortierungTbUebersicht();
                aktiviereZuordnungsmodus(false);
                return;
            }
            case 16: {/*Neue Zuordnung ElStimmkarte*/
                int gef = -1;
                List<EclAbstimmungenZuStimmkarte> neueZuordnung = new LinkedList<EclAbstimmungenZuStimmkarte>();
                for (int i = 0; i < angezeigteAbstimmungen.length; i++) {
                    if (cbZuordnung[i].isSelected()) {
                        gef = 1;
                        EclAbstimmungenZuStimmkarte lAbstimmungZuStimmkarte = new EclAbstimmungenZuStimmkarte();
                        lAbstimmungZuStimmkarte.stimmkartenNr = filterIdent;
                        lAbstimmungZuStimmkarte.identAbstimmung = angezeigteAbstimmungen[i].ident;
                        lAbstimmungZuStimmkarte.identAbstimmungAufKarte = angezeigteAbstimmungen[i].ident;
                        neueZuordnung.add(lAbstimmungZuStimmkarte);
                    }
                }
                if (gef == -1) {
                    this.fehlerMeldung("Bitte eine Abstimmung zur Zuordnung auswählen!");
                    return;
                }
                /*Speichern*/
                stubAbstimmungen.speichernListeNeueAbstimmungZuElekStimmkarte(neueZuordnung);

                /*Funktion beenden*/
                filterTyp = 2;
                zeigeSortierungTbUebersicht();
                aktiviereZuordnungsmodus(false);
                return;
            }
            }
            return;
        }

        /*Hier: Button in Funktion "Neu" wurde gedrückt*/

        /*Neue Abstimmung einfügen*/
        int rc = stubAbstimmungen.speichernVeraenderteAbstimmungenAbhaengigVonFilterTyp(filterTyp,
                angezeigteAbstimmungen, angezeigteAbstimmungZuAbstimmungsblock, angezeigteAbstimmungZuStimmkarte,
                abstimmungWurdeVeraendert);
        if (rc < 1) {
            zeigeFehlerVeraendert();
        }

        if (filterTyp == 0) {
            /*Komplett-Sicht anzeigen, damit richtig "angehängt" werden kann*/
            stubAbstimmungen.zeigeSortierungAbstimmungenInit(0, 1, 0);
            angezeigteAbstimmungen = stubAbstimmungen.angezeigteAbstimmungen;
            abstimmungWurdeVeraendert = stubAbstimmungen.abstimmungWurdeVeraendert;
            angezeigteAbstimmungZuAbstimmungsblock = stubAbstimmungen.angezeigteAbstimmungZuAbstimmungsblock;
            angezeigteAbstimmungZuStimmkarte = stubAbstimmungen.angezeigteAbstimmungZuStimmkarte;
            abstimmungWurdeVeraendert = new boolean[angezeigteAbstimmungen.length];

            /*Neue Abstimmung einfügen*/

            /*Eingabe-Maske aufrufen*/
            Stage newStage = new Stage();
            CaIcon.master(newStage);
            CtrlParameterAbstimmungNeu controllerFenster = new CtrlParameterAbstimmungNeu();
            controllerFenster.init(newStage, stubAbstimmungen, angezeigteAbstimmungen);

            CaController caController = new CaController();
            caController.open(newStage, controllerFenster,
                    "/de/meetingapps/meetingclient/meetingHVMaster/ParameterAbstimmungNeu.fxml", 1545, 810,
                    "Neue Abstimmung anlegen", true);

            if (controllerFenster.rc) {
                /*Ausführen*/
                zeigeSortierung();

                comboAllgemeinFuerAnzeige = new CbAllgemein(comboFilterFuerAnzeige);
                fuelleFilterFuerAnzeige();
                comboAllgemeinFuerAnzeige.setAusgewaehlt1(1);

            } else {
                /*Abbrechen der Neuaufnahme*/
                zeigeSortierung();

                comboAllgemeinFuerAnzeige = new CbAllgemein(comboFilterFuerAnzeige);
                fuelleFilterFuerAnzeige();
                comboAllgemeinFuerAnzeige.setAusgewaehlt1(1);
            }

            return;
        } else {
            /*Neue Zuordnung*/
            if (filterTyp == 1) {/*Zuordnen zu Abstimmungsblock*/
                aktuelleFunktion = 15;
                filterTyp = 3;
            } else {/*Zuordnung zu ElekStimmkarte*/
                aktuelleFunktion = 16;
                filterTyp = 4;
            }
            aktiviereZuordnungsmodus(true);
            btnNeueAbstimmung.setText("Zuordnen");
            btnAbstimmungLoeschen.setText("Abbrechen");
            zeigeSortierungTbUebersicht();
            return;
        }
    }

    /**
     * Loeschen-Button. Falls Menuezeile aktiv, dann "Abbruch"
     *
     * @param event the event
     */
    @FXML
    void clickedLoeschen(ActionEvent event) {

        if (befehlszeile == true) {/*Dann ist der Löschen-Button Abbruch*/
            if (aktuelleFunktion == 15 || aktuelleFunktion == 16) {
                if (aktuelleFunktion == 15) {
                    filterTyp = 1;
                } else {
                    filterTyp = 2;
                }
                zeigeSortierungTbUebersicht();
                aktiviereZuordnungsmodus(false);
                return;
            }

            deaktiviereBefehlszeile(false);
            return;
        }

        aktuelleFunktion = 12 + filterTyp;

        int rc = stubAbstimmungen.speichernVeraenderteAbstimmungenAbhaengigVonFilterTyp(filterTyp,
                angezeigteAbstimmungen, angezeigteAbstimmungZuAbstimmungsblock, angezeigteAbstimmungZuStimmkarte,
                abstimmungWurdeVeraendert);
        if (rc < 1) {
            zeigeFehlerVeraendert();
        }

        /*Hier: Button als Funktion "Löschen" wurde gedrückt*/
        if (aktuelleFunktion == 13 || aktuelleFunktion == 14) {
            /*Zuordnung aus Abstimmungsblock oder ElekStimmkarte*/
            if (filterTyp == 1) {
                filterTyp = 5;
            } else {
                filterTyp = 6;
            }
            aktiviereZuordnungsmodus(true);
            btnNeueAbstimmung.setText("Zuordnung löschen");
            btnAbstimmungLoeschen.setText("Abbrechen");
            zeigeSortierungTbUebersicht();
            return;
        }

        /*Abstimmung löschen*/

        if (filterTyp == 0) {
            lblFunktion.setText("Abstimmung löschen");
            lblFunktion.setVisible(true);
        }

        tfEingabe1.setVisible(true);
        tfEingabe1.setText("");

        if (aktuelleFunktion == 13) {
            lblEingabe1.setText("InterneIdent");
        } else {
            lblEingabe1.setText("Ident");
        }
        lblEingabe1.setVisible(true);

        btnNeueAbstimmung.setText("Ausführen");
        btnAbstimmungLoeschen.setText("Abbrechen");

        deaktiviereBefehlszeile(true);

        return;
    }

    /**
     * Neu-Button. Falls Menuezeile aktiv, dann "Ausführen"
     *
     * @param event the event
     */
    @FXML
    void clickedNeuAbstimmungsvorgang(ActionEvent event) {
        if (befehlszeile == true) {
            switch (aktuelleFunktion) {
            case 21: {
                /*Neuer Abstimmungsvorgang*/
                /*Kann nicht mehr entstehen!*/
                return;
            }
            case 22: {/*Abstimmungsvorgang löschen*/
                int ident = -1;
                if (tfEingabe1Abstimmungsvorgang.getText().isEmpty()) {
                    fehlerMeldung("Bitte zu löschende Ident eingeben!");
                    return;
                }
                String sIdent = tfEingabe1Abstimmungsvorgang.getText();
                if (!CaString.isNummern(sIdent) || sIdent.length() > 4) {
                    fehlerMeldung("Bitte zu löschende Ident korrekt eingeben!");
                    return;
                }
                ident = Integer.parseInt(sIdent);
                /*Gibts diese Ident?*/
                int gef = -1;
                if (angezeigteAbstimmungsblock != null) {
                    for (int i = 0; i < angezeigteAbstimmungsblock.length; i++) {
                        if (angezeigteAbstimmungsblock[i].ident == ident) {
                            gef = i;
                        }
                    }
                }
                if (gef == -1) {
                    fehlerMeldung("Ident nicht vorhanden!");
                    return;
                }

                speichernVeraendertePositionenAbstimmungsvorgang();

                int rc = stubAbstimmungen.loescheAbstimmVorgang(ident);
                if (rc < 1) {
                    fehlerMeldung(CaFehler.getFehlertext(rc, 0));
                    return;
                }
                deaktiviereBefehlszeileAbstimmungsvorgang(false);
                zeigeSortierungTpAbstimmungsvorgang();
                return;
            }
            }
            return;
        }

        /*Neuer Abstimmungsvorgang wird direkt ausgeführt,
         * wenn nicht "Löschen" schon vorher aktiv war*/
        speichernVeraendertePositionenAbstimmungsvorgang();

        /*Eingabe-Maske aufrufen*/
        Stage newStage = new Stage();
        CaIcon.master(newStage);
        CtrlParameterAbstimmungsvorgangNeu controllerFenster = new CtrlParameterAbstimmungsvorgangNeu();
        controllerFenster.init(newStage, stubAbstimmungen);

        CaController caController = new CaController();
        caController.open(newStage, controllerFenster,
                "/de/meetingapps/meetingclient/meetingHVMaster/ParameterAbstimmungsvorgangNeu.fxml", 1015, 200,
                "Neuer Abstimmungsvorgang", true);

        zeigeSortierungTpAbstimmungsvorgang();
        return;

    }

    /**
     * Loeschen-Button. Falls Menuezeile aktiv, dann "Abbruch"
     *
     * @param event the event
     */
    @FXML
    void clickedLoeschenAbstimmungsvorgang(ActionEvent event) {
        if (befehlszeile == true) {
            deaktiviereBefehlszeileAbstimmungsvorgang(false);
            return;
        }

        /*Hier: Button in Funktion "Löschen" wurde gedrückt*/
        /*Abstimmung löschen*/

        lblFunktionAbstimmungsvorgang.setText("Vorgang löschen");
        lblFunktionAbstimmungsvorgang.setVisible(true);

        tfEingabe1Abstimmungsvorgang.setVisible(true);
        tfEingabe1Abstimmungsvorgang.setText("");

        lblEingabe1Abstimmungsvorgang.setText("Ident");
        lblEingabe1Abstimmungsvorgang.setVisible(true);

        btnNeueAbstimmungAbstimmungsvorgang.setText("Ausführen");
        btnAbstimmungLoeschenAbstimmungsvorgang.setText("Abbrechen");

        aktuelleFunktion = 22;

        deaktiviereBefehlszeileAbstimmungsvorgang(true);

        return;

    }

    /**
     * Neu-Button. Falls Menuezeile aktiv, dann "Ausführen"
     *
     * @param event the event
     */
    @FXML
    void clickedNeuElekStimmkarte(ActionEvent event) {
        if (befehlszeile == true) {
            switch (aktuelleFunktion) {
            case 23: {/*Neue ElekStimmkarte*/

                /*Prüfen, ob stimmkartenNr gültig bzw. noch frei*/
                int ident = -1;
                if (tfEingabe1ElekStimmkarte.getText().isEmpty()) {
                    fehlerMeldung("Bitte neue Stimmk. eingeben!");
                    return;
                }
                String sIdent = tfEingabe1ElekStimmkarte.getText();
                if (!CaString.isNummern(sIdent) || sIdent.length() > 4) {
                    fehlerMeldung("Bitte neue Stimmk. korrekt eingeben!");
                    return;
                }
                ident = Integer.parseInt(sIdent);
                if (ident < 0 || ident > 9999) {
                    fehlerMeldung("Neue Stimmkarte muß Wert von 0 bis 9999 sein!");
                    return;
                }

                /*Gibts diese Ident?*/
                int gef = -1;
                if (angezeigteElekStimmkarte != null) {
                    for (int i = 0; i < angezeigteElekStimmkarte.length; i++) {
                        if (angezeigteElekStimmkarte[i].stimmkartenNr == ident) {
                            gef = i;
                        }
                    }
                }
                if (gef != -1) {
                    fehlerMeldung("Elektronische Stimmkartennummer bereits vorhanden!");
                    return;
                }

                /*Durchführung*/
                speichernVeraendertePositionenElStimmkarte();

                /*Neuen speichern*/
                EclStimmkarteInhalt elekStimmkarte = new EclStimmkarteInhalt();

                elekStimmkarte.stimmkartenNr = ident;
                elekStimmkarte.posInBlock = 0;
                elekStimmkarte.kurzBezeichnung = "neu";
                elekStimmkarte.stimmkartenBezeichnung = "neu";
                elekStimmkarte.stimmkartenBezeichnungEN = "neu";
                elekStimmkarte.stimmkarteIstAktiv = 0;
                stubAbstimmungen.insertElekStimmkarte(elekStimmkarte);

                deaktiviereBefehlszeileElekStimmkarte(false);
                zeigeSortierungTpElekStimmkarte();
                return;
            }
            case 24: {/*ElekStimmkarte löschen*/
                int ident = -1;
                if (tfEingabe1ElekStimmkarte.getText().isEmpty()) {
                    fehlerMeldung("Bitte zu löschende Stimmk. eingeben!");
                    return;
                }
                String sIdent = tfEingabe1ElekStimmkarte.getText();
                if (!CaString.isNummern(sIdent) || sIdent.length() > 4) {
                    fehlerMeldung("Bitte zu löschende Stimmk. korrekt eingeben!");
                    return;
                }
                ident = Integer.parseInt(sIdent);
                /*Gibts diese Ident?*/
                int gef = -1;
                if (angezeigteElekStimmkarte != null) {
                    for (int i = 0; i < angezeigteElekStimmkarte.length; i++) {
                        if (angezeigteElekStimmkarte[i].stimmkartenNr == ident) {
                            gef = i;
                        }
                    }
                }
                if (gef == -1) {
                    fehlerMeldung("Elektronische Stimmkartennummer nicht vorhanden!");
                    return;
                }

                speichernVeraendertePositionenElStimmkarte();

                /*Löschen*/
                int rc = stubAbstimmungen.loescheElekStimmkarte(ident);
                if (rc < 1) {
                    fehlerMeldung(CaFehler.getFehlertext(rc, 0));
                    return;
                }

                deaktiviereBefehlszeileElekStimmkarte(false);
                zeigeSortierungTpElekStimmkarte();
                return;
            }
            }
            return;
        }

        /*Hier: Button in Funktion "Neu" wurde gedrückt*/

        /*Neue El. Stimmkarte einfügen*/

        lblFunktionElekStimmkarte.setText("Neue ElStimmkarte");
        aktuelleFunktion = 23;

        lblFunktionElekStimmkarte.setVisible(true);

        tfEingabe1ElekStimmkarte.setVisible(true);
        tfEingabe1ElekStimmkarte.setText("");

        lblEingabe1ElekStimmkarte.setText("Stimmkarten-Nr");
        lblEingabe1ElekStimmkarte.setVisible(true);

        btnNeueStimmkarteElekStimmkarte.setText("Ausführen");
        btnStimmkarteLoeschenElekStimmkarte.setText("Abbrechen");

        deaktiviereBefehlszeileElekStimmkarte(true);

        return;
    }

    /**
     * Loeschen-Button. Falls Menuezeile aktiv, dann "Abbruch"
     *
     * @param event the event
     */
    @FXML
    void clickedLoeschenElekStimmkarte(ActionEvent event) {
        if (befehlszeile == true) {
            deaktiviereBefehlszeileElekStimmkarte(false);
            return;
        }

        /*Hier: Button in Funktion "Löschen" wurde gedrückt*/
        /*Abstimmung löschen*/

        lblFunktionElekStimmkarte.setText("ElStimm. löschen");
        lblFunktionElekStimmkarte.setVisible(true);

        tfEingabe1ElekStimmkarte.setVisible(true);
        tfEingabe1ElekStimmkarte.setText("");

        lblEingabe1ElekStimmkarte.setText("Stimmk.Nr.");
        lblEingabe1ElekStimmkarte.setVisible(true);

        btnNeueStimmkarteElekStimmkarte.setText("Ausführen");
        btnStimmkarteLoeschenElekStimmkarte.setText("Abbrechen");

        aktuelleFunktion = 24;

        deaktiviereBefehlszeileElekStimmkarte(true);

        return;

    }

    /**
     * Clicked detail pflege.
     *
     * @param event the event
     */
    @FXML
    void clickedDetailPflege(ActionEvent event) {

        String ueberschrift = "";

        for (int i = 0; i < btnPauschalausschluss.length; i++) {
            if (event.getSource() == btnPauschalausschluss[i]) {
                ausgewaehlteAbstimmung = i;
                aktuelleFunktion = 1;
                ueberschrift = "Details Pauschalausschluss";
            }
            if (event.getSource() == btnEinzelausschluss[i]) {
                ausgewaehlteAbstimmung = i;
                aktuelleFunktion = 2;
                ueberschrift = "Details Einzelausschluss";
            }
        }

        Stage newStage = new Stage();
        CaIcon.master(newStage);
        CtrlParameterAbstimmungAusschluss controllerFenster = new CtrlParameterAbstimmungAusschluss();
        controllerFenster.init(newStage, aktuelleFunktion, angezeigteAbstimmungen[ausgewaehlteAbstimmung]);

        CaController caController = new CaController();
        caController.open(newStage, controllerFenster,
                "/de/meetingapps/meetingclient/meetingHVMaster/ParameterAbstimmungAusschluss.fxml", 640, 460,
                ueberschrift, true);

        abstimmungWurdeVeraendert[ausgewaehlteAbstimmung] = true;
        speichernVeraendertePositionenStimmrecht();

        zeigeSortierungTbStimmrecht();
    }

    /**
     * Clicked alles aktivieren.
     *
     * @param event the event
     */
    @FXML
    void clickedAllesAktivieren(ActionEvent event) {
        int gef = -1;
        for (int i = 0; i < btnAllesAktivieren.length; i++) {
            if (event.getSource() == btnAllesAktivieren[i]) {
                gef = i;
            }
        }
        if (gef == -1) {
            return;
        }
        cbAktiv[gef].setSelected(true);
        cbAktivPreview[gef].setSelected(true);
        cbAktivWeisungenInPortal[gef].setSelected(true);
        cbAktivWeisungenHV[gef].setSelected(true);
        cbAktivWeisungenSchnittstelle[gef].setSelected(true);
        cbAktivWeisungenAnzeige[gef].setSelected(true);
        cbAktivWeisungenInterneAuswertungen[gef].setSelected(true);
        cbAktivWeisungenExterneAuswertungen[gef].setSelected(true);
        cbAktivWeisungenPflegeIntern[gef].setSelected(true);
        cbAktivAbstimmungInPortal[gef].setSelected(true);
        cbAktivBeiSRV[gef].setSelected(true);
        cbAktivBeiBriefwahl[gef].setSelected(true);
        cbAktivBeiKIAVDauer[gef].setSelected(true);
        cbAktivFragen[gef].setSelected(true);
        cbAktivAntraege[gef].setSelected(true);
        cbAktivWidersprueche[gef].setSelected(true);
        cbAktivWortmeldungen[gef].setSelected(true);
        cbAktivSonstMitteilungen[gef].setSelected(true);
        cbAktivBotschaftenEinreichen[gef].setSelected(true);
    }

}
