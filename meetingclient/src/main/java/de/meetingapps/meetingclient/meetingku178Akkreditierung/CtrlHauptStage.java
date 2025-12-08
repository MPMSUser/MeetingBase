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
package de.meetingapps.meetingclient.meetingku178Akkreditierung;

/**Testfälle
 * 43363 => Normale selbst
 * 43365 => gesetzlicher Vertreter an 43364
 * 43366 => normaler Vertreter an 43364
 * 
 * Testfall "Zuordenbar"
 * 
 * Zuordnen A. Aktiv
 * =3646
 * A Deaktivieren, wieder aktivieren.
 * 
 * B 11982
 * B Zuordnen über Vollmacht
 * B Zuordnen über Vollmacht -> Geht nicht
 * B Vollmacht deaktivieren
 * B Zuordnen über Vollmacht -> Geht nicht
 * B zuordnen über Gesetzl.
 * B Vollmacht aktiveren => geht nicht
 * B Gesetzl. deaktivieren 
 * B Vollmacht aktivieren
 * B Gesetzl. aktivieren -> geht nicht
 * B Vollmacht deaktivieren
 * 
 * B Zuordnen über Vollmacht => geht nicht
 * B Zuornden über Gesetzlich => geht nicht
 * 
 * C Zuordnen als gesetzlicher Vertreter
 * 1014 => mit U.Vollamcht 1003
 * 
 */

/**Großer Endtest
 * ==============
 * Scannen einer Gast-Nummer
 * 
 * Scannen einer Ticket-Nummer (und Zuordnung durchführen, danach nochmals Ticket Scannen)
 * 
 * Mitglied mit "keiner Zuordnung" (weil gleich wieder deaktiviert) (bei Ersterstellung)
 * 
 * Mitglied mit "keiner Zuordnung" (weil bisherige Zuordnungen alle deaktiviert)
 * 
 * 
 * Mitglied selbst, kommt, gibt dann Vollmacht an anderes Mitglied
 * A=4651, B=7404
 * Mitglied A kommt selbst (Badge, Zugang)
 * Mitglied A hat Badge verloren (neuer Badge, Zugang + Abstimmung mit altem / neuen Badge)
 * Mitglied B kommt
 * Mitglied B erhält Vollmacht von Mitglied A
 * Mitglied B geht zu, stimmt ab (mit A)
 * 
 * Mitglied, das bereits Vertreter eingetragen hat, kommt selbst
 * 
 * Mitglied Selbst, mit 2 Bevollmächtigten und 2 gültigen Begleitpersonen und 2 ungültigen Begleitpersonen
 * A=9157, V1=4663, V2=15112, 
 * Mitglied A wird akkreditiert mit Vollmachten V1 und V2, sowie Begleitpersonen B1 und B2
 * Druckwiederholung für A und V2
 * Zugang mit A, V1, V2
 * "Badge verloren" A. Neuer Badge für A
 * Zugang für A (Alt), A
 * Abgang für A, B1.
 * Badge B2 verloren - neu ausstellen, mit Druckwiederholung
 * Badge A, B1, B2 (Alt), B2 zugehen, 
 * 
 * Vollmachtgeber=>Bevollmächtigter im Vorfeld, dann kommt erst Bevollmächtigter
 * einschließlich Vollmachtgeber
 * 
 * Vollmachtgeber=>Bevollmächtigter im Vorfeld, dann kommt erst Vollmachtgeber selbst,
 * dann kommt Bevollmächtigter
 * 
 * Mitglied mit zu vielen Vollmachten => Drucken darf nicht möglich sein
 * Kind als UNtervollmacht mit zu vielen Vollmachten => Weiter zum Teilnehmer darf nicht möglich sein
 * 
 * Neue Zuordnung, Deaktivieren, Aktivieren, Speichern
 * Neue Zuordnung, Deaktivieren, Speichern, Aktivieren
 * Neue ZUordnung, Speichern, Deaktivieren, Aktivieren
 * Jeweils für Selbst / gesetzlich / Bevollmächtigt
 * 
 * Testideen:
 * ==========
 * 
 * Zugang mit Briefwahl
 * QR-Code mit vielen Inhalten - Druckbar, Scanbar?
 * 
 * 
 */
import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;

import de.meetingapps.meetingclient.meetingClientDialoge.CaController;
import de.meetingapps.meetingclient.meetingClientDialoge.CaIcon;
import de.meetingapps.meetingclient.meetingClientDialoge.CaZeigeHinweis;
import de.meetingapps.meetingclient.meetingClientDialoge.CtrlRoot;
import de.meetingapps.meetingclient.meetingClientDialoge.CtrlSuchen;
import de.meetingapps.meetingclient.meetingClientOberflaechen.MeetingGridPane;
import de.meetingapps.meetingportal.meetComAllg.CaBug;
import de.meetingapps.meetingportal.meetComAllg.CaString;
import de.meetingapps.meetingportal.meetComBl.BlGastkarte;
import de.meetingapps.meetingportal.meetComBl.BlWillenserklaerung;
import de.meetingapps.meetingportal.meetComDb.DbBundle;
import de.meetingapps.meetingportal.meetComEntities.EclAktienregister;
import de.meetingapps.meetingportal.meetComEntities.EclMeldung;
import de.meetingapps.meetingportal.meetComEntities.EclPersonenNatJur;
import de.meetingapps.meetingportal.meetComEntities.EclVeranstaltungenWert;
import de.meetingapps.meetingportal.meetComEntities.EclVorlaeufigeVollmacht;
import de.meetingapps.meetingportal.meetComEntities.EclZugeordneteAktionaerePraesenz;
import de.meetingapps.meetingportal.meetComEntities.EclZutrittsIdent;
import de.meetingapps.meetingportal.meetComKonst.KonstGruppen;
import de.meetingapps.meetingportal.meetComKonst.KonstMeldung;
import de.meetingapps.meetingportal.meetComKonst.KonstPortalFunktionen;
import de.meetingapps.meetingportal.meetingCoreReport.RpDrucken;
import de.meetingapps.meetingportal.meetingCoreReport.RpVariablen;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

/**
 * The Class CtrlHauptStage.
 */
public class CtrlHauptStage extends CtrlRoot {

    /** The log drucken. */
    private int logDrucken = 10;

    /** The resources. */
    @FXML
    private ResourceBundle resources;

    /** The location. */
    @FXML
    private URL location;

    /** The btn abbrechen global. */
    @FXML
    private Button btnAbbrechenGlobal;

    /** The btn naechster vorgang. */
    @FXML
    private Button btnNaechsterVorgang;

    /** The btn weiter global. */
    @FXML
    private Button btnWeiterGlobal;

    /** The btn weiter global. */
    @FXML
    private Button btnBuchenGlobal;

    /** The tp ablauf. */
    @FXML
    private TabPane tpAblauf;

    /** The t start. */
    @FXML
    private Tab tStart;

    /** The btn batch aendern. */
    @FXML
    private Button btnBatchAendern;

    /** The btn neue zuordnung start. */
    @FXML
    private Button btnNeueZuordnungStart;

    /** The tf batch nummer. */
    @FXML
    private TextField tfBatchNummer;

    /** The btn suchen start. */
    @FXML
    private Button btnSuchenStart;

    /** The tf ticket nummer. */
    @FXML
    private TextField tfTicketNummer;

    /** The btn T icket einlesen. */
    @FXML
    private Button btnTIcketEinlesen;

    /** The t vertretung. */
    @FXML
    private Tab tVertretung;

    /** The btn abbrechen selbst. */
    @FXML
    private Button btnAbbrechenSelbst;

    /** The btn selbst selbst. */
    @FXML
    private Button btnSelbstSelbst;

    /** The btn vertreter selbst. */
    @FXML
    private Button btnVertreterSelbst;

    /** The btn neuer vertreter selbst. */
    @FXML
    private Button btnNeuerVertreterSelbst;

    /** The lbl selbst art. */
    @FXML
    private Label lblSelbstArt;

    /** The lbl selbst name. */
    @FXML
    private Label lblSelbstName;

    /** The lbl selbst ort. */
    @FXML
    private Label lblSelbstOrt;

    /** The btn gesetzl vertreter selbst. */
    @FXML
    private Button btnGesetzlVertreterSelbst;

    /** The btn neuer gesetzl vertreter selbst. */
    @FXML
    private Button btnNeuerGesetzlVertreterSelbst;

    /** The t eingabe neuer vertreter. */
    @FXML
    private Tab tEingabeNeuerVertreter;

    /** The lbl neuer vertreter ueberschrift. */
    @FXML
    private Label lblNeuerVertreterUeberschrift;

    /** The tf neuer vertreter name. */
    @FXML
    private TextField tfNeuerVertreterName;

    /** The tf neuer vertreter vorname. */
    @FXML
    private TextField tfNeuerVertreterVorname;

    /** The tf neuer vertreter ort. */
    @FXML
    private TextField tfNeuerVertreterOrt;

    /** The btn abbrechen neuer vertreter. */
    @FXML
    private Button btnAbbrechenNeuerVertreter;

    /** The btn uebernehmen neuer vertreter. */
    @FXML
    private Button btnUebernehmenNeuerVertreter;

    /** The lbl neuer vertreter mitglied art. */
    @FXML
    private Label lblNeuerVertreterMitgliedArt;

    /** The lbl neuer vertreter mitglied name. */
    @FXML
    private Label lblNeuerVertreterMitgliedName;

    /** The lbl neuer vertreter mitglied ort. */
    @FXML
    private Label lblNeuerVertreterMitgliedOrt;

    /** The lbl neuer vertreter auswahl 1. */
    @FXML
    private Label lblNeuerVertreterAuswahl1;

    /** The lbl neuer vertreter auswahl 2. */
    @FXML
    private Label lblNeuerVertreterAuswahl2;

    /** The cb 1. */
    @FXML
    private RadioButton cb1;

    /** The cb 2. */
    @FXML
    private RadioButton cb2;

    /** The tg vertreter ist. */
    @FXML
    private ToggleGroup tgVertreterIst;

    /** The cb 3. */
    @FXML
    private RadioButton cb3;

    /** The cb 9. */
    @FXML
    private RadioButton cb9;

    /** The cb 8. */
    @FXML
    private RadioButton cb8;

    /** The cb 7. */
    @FXML
    private RadioButton cb7;

    /** The cb 6. */
    @FXML
    private RadioButton cb6;

    /** The cb 5. */
    @FXML
    private RadioButton cb5;

    /** The cb 4. */
    @FXML
    private RadioButton cb4;

    /** The t eingabe gb R. */
    @FXML
    private Tab tEingabeGbR;

    /** The lbl gb R ueberschrift teilnehmer. */
    @FXML
    private Label lblGbRUeberschriftTeilnehmer;

    /** The tf gb R name teilnehmer. */
    @FXML
    private TextField tfGbRNameTeilnehmer;

    /** The tf gb R vorname teilnehmer. */
    @FXML
    private TextField tfGbRVornameTeilnehmer;

    /** The tf gb R ort teilnehmer. */
    @FXML
    private TextField tfGbROrtTeilnehmer;

    /** The btn abbrechen gb R. */
    @FXML
    private Button btnAbbrechenGbR;

    /** The btn uebernehmen gb R. */
    @FXML
    private Button btnUebernehmenGbR;

    /** The lbl gb R mitglied art. */
    @FXML
    private Label lblGbRMitgliedArt;

    /** The lbl gb R mitglied name. */
    @FXML
    private Label lblGbRMitgliedName;

    /** The lbl gb R mitglied ort. */
    @FXML
    private Label lblGbRMitgliedOrt;

    /** The lbl gb R ueberschrift begleitperson. */
    @FXML
    private Label lblGbRUeberschriftBegleitperson;

    /** The tf gb R name begleitperson. */
    @FXML
    private TextField tfGbRNameBegleitperson;

    /** The tf gb R vorname begleitperson. */
    @FXML
    private TextField tfGbRVornameBegleitperson;

    /** The tf gb R ort begleitperson. */
    @FXML
    private TextField tfGbROrtBegleitperson;

    /** The btn gb R keine begleitperson. */
    @FXML
    private Button btnGbRKeineBegleitperson;

    /** The btn gb R tauschen. */
    @FXML
    private Button btnGbRTauschen;

    /** The t teilnahme. */
    @FXML
    private Tab tTeilnahme;

    /** The lbl warnhinweis teilnehmer. */
    @FXML
    private Label lblWarnhinweisTeilnehmer;

    /** The tf teilnehmer ident. */
    @FXML
    private TextField tfTeilnehmerIdent;

    /** The tf teilnehmer name. */
    @FXML
    private TextField tfTeilnehmerName;

    /** The tf teilnehmer ort. */
    @FXML
    private TextField tfTeilnehmerOrt;

    /** The btn teilnehmer drucken. */
    @FXML
    private Button btnTeilnehmerDrucken;

    /** The btn teilnehmer weitere. */
    @FXML
    private Button btnTeilnehmerWeitere;

    /** The scpn teilnehmer. */
    @FXML
    private ScrollPane scpnTeilnehmer;

    /** The btn teilnehmer abbrechen. */
    @FXML
    private Button btnTeilnehmerAbbrechen;

    /** The btn teilnehmer weitere begleitperson. */
    @FXML
    private Button btnTeilnehmerWeitereBegleitperson;

    /** The scpn begleitpersonen. */
    @FXML
    private ScrollPane scpnBegleitpersonen;

    /** The t weitere vertretung. */
    @FXML
    private Tab tWeitereVertretung;

    /** The btn weitere vertretung weiter. */
    @FXML
    private Button btnWeitereVertretungWeiter;

    /** The btn weitere vertretung abbrechen. */
    @FXML
    private Button btnWeitereVertretungAbbrechen;

    /** The lbl weitere vertretung mitglied art. */
    @FXML
    private Label lblWeitereVertretungMitgliedArt;

    /** The lbl weitere vertretung mitglied name. */
    @FXML
    private Label lblWeitereVertretungMitgliedName;

    /** The lbl weitere vertretung mitglied ort. */
    @FXML
    private Label lblWeitereVertretungMitgliedOrt;

    /** The rb bevollmaechtigter. */
    @FXML
    private RadioButton rbBevollmaechtigter;

    /** The Vertreter. */
    @FXML
    private ToggleGroup Vertreter;

    /** The rb gesetzlicher vertreter. */
    @FXML
    private RadioButton rbGesetzlicherVertreter;

    /** The rb selbst. */
    @FXML
    private RadioButton rbSelbst;

    /** The t eingabe neue begleitperson. */
    @FXML
    private Tab tEingabeNeueBegleitperson;

    /** The lbl neue begleitperson ueberschrift. */
    @FXML
    private Label lblNeueBegleitpersonUeberschrift;

    /** The tf neue begleitperson name. */
    @FXML
    private TextField tfNeueBegleitpersonName;

    /** The tf neue begleitperson vorname. */
    @FXML
    private TextField tfNeueBegleitpersonVorname;

    /** The tf neue begleitperson ort. */
    @FXML
    private TextField tfNeueBegleitpersonOrt;

    /** The btn abbrechen neue begleitperson. */
    @FXML
    private Button btnAbbrechenNeueBegleitperson;

    /** The btn uebernehmen neue begleitperson. */
    @FXML
    private Button btnUebernehmenNeueBegleitperson;

    /** The lbl neue begleitperson mitglied art. */
    @FXML
    private Label lblNeueBegleitpersonMitgliedArt;

    /** The lbl neue begleitperson mitglied name. */
    @FXML
    private Label lblNeueBegleitpersonMitgliedName;

    /** The lbl neue begleitperson mitglied ort. */
    @FXML
    private Label lblNeueBegleitpersonMitgliedOrt;

    /** The t kind. */
    /*++++++++++++++++++++++++++Kind++++++++++++++++++*/
    @FXML
    private Tab tKind;

    /** The lbl warnhinweis kind. */
    @FXML
    private Label lblWarnhinweisKind;

    /** The tf kind ident. */
    @FXML
    private TextField tfKindIdent;

    /** The tf kind name. */
    @FXML
    private TextField tfKindName;

    /** The tf kind ort. */
    @FXML
    private TextField tfKindOrt;

    /** The btn kind weitere. */
    @FXML
    private Button btnKindWeitere;

    /** The scpn kind. */
    @FXML
    private ScrollPane scpnKind;

    /** The btn kind abbrechen. */
    @FXML
    private Button btnKindAbbrechen;

    /** The grpn vertretene. */
    private MeetingGridPane grpnVertretene = null;

    /** The grpn begleitpersonen. */
    private MeetingGridPane grpnBegleitpersonen = null;

    /** The grpn kind. */
    private MeetingGridPane grpnKind = null;

    /** Wird beim Erstellen einer neuen Zuordnung auf false gesetzt. */
    private boolean zuordnungWarBereitsVorhanden = true;

    /** The mindestens ein badge wurde gedruckt. */
    private boolean mindestensEinBadgeWurdeGedruckt = false;

    /**
     * 0=Start 1=neuen Vertreter eingeben, noch kein Mitglied ausgewählt 2=neuen
     * Vertreter eingeben, Mitglied bereits ausgewählt.
     */
    private int aktuelleFunktion = 0;

    /** Daten für Vertreter, der teilnehmen wird. */
    private boolean neuerVertreter = false;

    /**
     * -1 => neuer Vertreter, ohne Mitglied => undefiniert 0=Bevollmächtigter
     * 1=Gesetzlicher 2=Bevollmächtigter - Geerbter Bevollmächtigter
     * 3=Bevollmächtigter - Geerbter Gesetzlicher Bevollmächtigter 4=Gesetzlicher -
     * Geerbter Bevollmächtigter 5=Gesetzlicher - Geerbter Gesetzlicher
     * Bevollmächtigter.
     */
    private int neuerVertreterIstGesetzlich = 0;

    /** The neuer vertreter art. */
    private int neuerVertreterArt = 0;

    /** Kennung des Vertreters. */
    private String vertreterId = "";

    /**
     * Wird beim Speichern gefüllt und für das Eintragen der Vollmachten verwendet.
     */
    int vertreterPersonNatJurIdent = 0;

    /** The vertreter name. */
    private String vertreterName = "";

    /** The vertreter vorname. */
    private String vertreterVorname = "";

    /** The vertreter ort. */
    private String vertreterOrt = "";

    /** The liste der vertretenen. */
    /*+++++++++++++++Liste der Vertretenen+++++++++++++++++++++++++++++++*/
    private List<EclZugeordneteAktionaerePraesenz> listeDerVertretenen = null;

    /** True => einige aus dem Batch sind präsent => nicht verarbeitbar. */
    private boolean listeDerVertretenenGesperrt = false;

    /** The liste der buttons zu vertretenen. */
    private List<Button> listeDerButtonsZuVertretenen = null;

    /** Nur für minderjährige gefüllt - weitere Vollmachten zuordnen. */
    private List<Button> listeDerWeitereButtonsZuVertretenen = null;
    /**
     * -2 - keine zugeordneten aktiven Mitglieder (aber inaktive), Veränderung
     * erfolgt - d.h. kein Druck, aber ungültig machen des alten Badge -1 - keine
     * zugeordneten aktiven Mitglieder (aber inaktive) - keine Veränderung => kein
     * Druck möglich / zulässig 0 - keine Veränderung, Druck freiwillig 1 - Druck
     * muß erfolgen, da verändert 2 - Druck wird freiwillig durchgeführt
     */
    private int listeDerVertretenenDrucken = 0;

    /** The liste der vertretenen ident. */
    private String[] listeDerVertretenenIdent = null;

    /** Für Badge. */
    private String vertreterEKIdent = "";
    
    /**Wird beim Badge-Druck erzeugt, und anschließend bei der Zugangsbuchung verwendet*/
    private String listeDerVertretenenZugangsstring="";

    /** The liste der begleitpersonen. */
    /*+++++++++++++++++++++++++++++++Liste gesperrt++++++++++++++++++++++++++++++++++*/
    private List<EclZugeordneteAktionaerePraesenz> listeDerBegleitpersonen = null;

    /** The liste der buttons zu begleitpersonen. */
    private List<Button> listeDerButtonsZuBegleitpersonen = null;
    /**
     * Wird erst beim Drucken erzeugt und gefüllt. Werte siehe
     * listeDerVertretenenDrucken
     */
    private int[] listDerBegleitpersonenZuDrucken = null;

    /** The liste der begleitpersonen ident. */
    private String[] listeDerBegleitpersonenIdent = null;

    /**Wird beim Badge-Druck erzeugt, und anschließend bei der Zugangsbuchung verwendet*/
    private String[] listeDerBegleitpersonenZugangsstring=null;
    
    /*++++++++++++++++++++++++++++++++++++++Liste Kind++++++++++++++++++++++++*/
    /**
     * Liste der Zuordnungen für ein Kind, für das gerade noch Vollmachten
     * eingetragen werden.
     */
    private List<EclZugeordneteAktionaerePraesenz> listeDerVertretenenKind = null;

    /** The liste der buttons zu vertretenen kind. */
    private List<Button> listeDerButtonsZuVertretenenKind = null;

    /** The l db bundle. */
    private DbBundle lDbBundle = null;

    /** The drucker wiederverwendet. */
    private int druckerWiederverwendet = 1;

    /**
     * Initialize.
     */
    @FXML
    void initialize() {

        initalisierenFuerNaechstenVorgang();

        /*Buttons auf Startseite den Eingabefeldern zuordnen*/
    }

    /**
     * Initalisieren fuer naechsten vorgang.
     */
    private void initalisierenFuerNaechstenVorgang() {

        aktuelleFunktion = 0;

        neuerVertreter = false;

        vertreterId = "";

        vertreterName = "";
        vertreterVorname = "";
        vertreterOrt = "";

        /*Felder für Vertreter-Tab müssen hier resettet werden, da diese sichtbar sind*/
        tfNeuerVertreterName.setText("");
        tfNeuerVertreterVorname.setText("");
        tfNeuerVertreterOrt.setText("");

        tfBatchNummer.setText("");
        tfTicketNummer.setText("");

        cb1.setSelected(false);
        cb2.setSelected(false);
        cb3.setSelected(false);
        cb4.setSelected(false);
        cb5.setSelected(false);
        cb6.setSelected(false);
        cb7.setSelected(false);
        cb8.setSelected(false);
        cb9.setSelected(false);

        initListeDerVertretenen();
        listeDerVertretenenGesperrt = false;
        initListeDerBegleitpersonen();

        showStartTab();
    }

    /**
     * *********************Globale Buttons**********************.
     *
     * @param event the event
     */
    @FXML
    void onBtnNaechsterVorgang(ActionEvent event) {

        initalisierenFuerNaechstenVorgang();

    }

    /**
     * On btn abbrechen global.
     *
     * @param event the event
     */
    @FXML
    void onBtnAbbrechenGlobal(ActionEvent event) {
        CaZeigeHinweis caZeigeHinweis = new CaZeigeHinweis();
        boolean rc = caZeigeHinweis.zeige2Buttons(eigeneStage, "Wollen Sie das Akkreditierungs-Modul komplett beenden?",
                "Akkreditierung beenden", "Akkreditierung fortsetzen");
        if (rc) {
            eigeneStage.hide();
        }
    }

    /**
     * Verwendet für Gastkarte für Event.
     *
     * @param event the event
     */
    @FXML
    void onBtnWeiterGlobal(ActionEvent event) {
        CaZeigeHinweis caZeigeHinweis = new CaZeigeHinweis();
        boolean brc = caZeigeHinweis.zeige2Buttons(eigeneStage,
                "Neutrale Gastkarte für das Mitgliedertreffen erstellen?", "Nein", "Ja");
        if (brc) {
            return;
        }

        lDbBundleOpen();
        BlGastkarte blGastkarte = new BlGastkarte(lDbBundle);
        EclMeldung meldungNeuerGast = new EclMeldung();

        meldungNeuerGast.name = "Mitgliedertreffen";
        meldungNeuerGast.vorname = "";

        blGastkarte.pGast = meldungNeuerGast;
        blGastkarte.berechtigungsWert = KonstPortalFunktionen.berechtigungsbit(KonstPortalFunktionen.gast9);
        blGastkarte.pVersandart = 1;
        int rc = blGastkarte.ausstellen();
        CaBug.druckeLog("Zutrittsident=" + blGastkarte.rcZutrittsIdent + "rc=" + rc, logDrucken, 10);

        /*Nun drucken*/
        EclZutrittsIdent lZutrittsIdent = new EclZutrittsIdent();
        lZutrittsIdent.zutrittsIdent = blGastkarte.rcZutrittsIdent;
        lZutrittsIdent.zutrittsIdentNeben = "00";
        lDbBundle.dbZutrittskarten.readGast(lZutrittsIdent, 1);
        int meldungsIdent = lDbBundle.dbZutrittskarten.ergebnisPosition(0).meldungsIdentGast;

        lDbBundle.dbMeldungen.leseZuIdent(meldungsIdent);
        int personNatJurIdent = lDbBundle.dbMeldungen.meldungenArray[0].personenNatJurIdent;

        lDbBundle.dbLoginDaten.read_personNatJurIdent(personNatJurIdent);

        blGastkarte = new BlGastkarte(lDbBundle);
        RpDrucken rpDrucken = new RpDrucken();
        rpDrucken.initClientDrucke();
        rpDrucken.druckerWiederverwendet = druckerWiederverwendet;
        rpDrucken.druckerWiederverwendetNummer = 999;
        if (druckerWiederverwendet == 2) {
            rpDrucken.druckerAbfragen = false;
        }
        blGastkarte.initRpDrucken(rpDrucken);
        blGastkarte.pVersandart = 6;
        blGastkarte.berechtigungsWert = lDbBundle.dbLoginDaten.ergebnisPosition(0).berechtigungPortal;

        blGastkarte.rcZutrittsIdent = lZutrittsIdent.zutrittsIdent;
        blGastkarte.pGast = lDbBundle.dbMeldungen.meldungenArray[0];
        blGastkarte.drucken(lDbBundle);
        blGastkarte.druckenEnde();

        if (druckerWiederverwendet == 1) {
            druckerWiederverwendet = 2;
        }

        lDbBundleClose();
        return;

    }

    
    @FXML
    void onBtnBuchenGlobal(ActionEvent event) {
        oeffneFOohneBuchung();
        
    }

    
    
    /**
     * ****************Start*********************************************.
     *
     * @param event the event
     */

    /*ENTER abfangen im ScanFeld*/
    @FXML
    void onKeyPressedTicketNummer(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            CaBug.druckeLog("Ticket einleses", logDrucken, 10);
            ticketVerarbeiten();
        }
    }

    /**
     * On btn T icket einlesen.
     *
     * @param event the event
     */
    @FXML
    void onBtnTIcketEinlesen(ActionEvent event) {
        ticketVerarbeiten();
    }

    /**
     * Ticket verarbeiten.
     */
    private void ticketVerarbeiten() {
        zuordnungWarBereitsVorhanden = false;

        String hTicketNr = tfTicketNummer.getText().trim();
        CaBug.druckeLog("A hTicketNr=" + hTicketNr, logDrucken, 10);
        if (hTicketNr.isEmpty() || hTicketNr.length() > 7
                || CaString.isNummern(hTicketNr) == false) {
            fehlerMeldung("Bitte geben Sie eine gültige Ticket-Nummer ein");
            return;
        }
        if (hTicketNr.length() == 7) {
            hTicketNr = hTicketNr.substring(1, 6);
            CaBug.druckeLog("B hTicketNr=" + hTicketNr, logDrucken, 10);
        }
        hTicketNr = CaString.fuelleLinksNull(hTicketNr, 5);
        CaBug.druckeLog("C hTicketNr=" + hTicketNr, logDrucken, 10);

        if (hTicketNr.startsWith("9")) {
            fehlerMeldung("Ticket " + hTicketNr + " nicht vorhanden oder Mitglied bereits bearbeitet");
            tfTicketNummer.setText("");
            return;
        }
        lDbBundle = new DbBundle();
        lDbBundle.openAll();
        lDbBundle.openWeitere();
        int anz = lDbBundle.dbMeldungen.leseEkNummer(hTicketNr);
        if (anz < 1) {
            lDbBundle.closeAll();
            fehlerMeldung("Ticket nicht gefunden");
            tfTicketNummer.setText("");
            return;
        }

        rcSuchenErstSucheMeldungen = lDbBundle.dbMeldungen.meldungenArray[0];
        String hAktionaersnummer = rcSuchenErstSucheMeldungen.aktionaersnummer;
        lDbBundle.dbAktienregister.leseZuAktienregisternummer(hAktionaersnummer);
        rcSuchenErstSucheAktienregister = lDbBundle.dbAktienregister.ergebnisPosition(0);
        boolean bereitsVorhanden = pruefeObMitgliedSchonZugeordnet(hAktionaersnummer, "");
        lDbBundle.closeAll();
        if (bereitsVorhanden) {
            fehlerMeldung("Mitglied ist bereits dem Teilnehmer " + aufbereiteteNummer(zugeordnetZu) + " zugeordnet!");
            return;
        }
        neueZuordnungStartMitgliedGefunden();

    }

    /**
     * On key pressed batch nummer.
     *
     * @param event the event
     */
    /*ENTER abfangen im ScanFeld*/
    @FXML
    void onKeyPressedBatchNummer(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            CaBug.druckeLog("Batch einleses", logDrucken, 10);
            batchNrEingegeben();
        }
    }

    /**
     * On btn batch aendern.
     *
     * @param event the event
     */
    @FXML
    void onBtnBatchAendern(ActionEvent event) {
        batchNrEingegeben();
    }

    /**
     * Batch nr eingegeben.
     */
    private void batchNrEingegeben() {
        zuordnungWarBereitsVorhanden = true;

        String hEkNr = tfBatchNummer.getText().trim();
        if (hEkNr.isEmpty()) {
            fehlerMeldung("Bitte geben Sie eine gültige Badge-Nummer ein!");
            return;
        }

        int posStrichPunkt = hEkNr.indexOf(";");
        if (posStrichPunkt != -1) {
            hEkNr = hEkNr.substring(posStrichPunkt + 1);
            CaBug.druckeLog("A hEkNr=" + hEkNr, logDrucken, 10);
            posStrichPunkt = hEkNr.indexOf(";");
            if (posStrichPunkt != -1) {
                hEkNr = hEkNr.substring(0, posStrichPunkt);
                CaBug.druckeLog("B hEkNr=" + hEkNr, logDrucken, 10);
            }

        }

        hEkNr = CaString.fuelleLinksNull(hEkNr, 5);

        /*Hier: EK-Nr ist in eEkNr*/
        CaBug.druckeLog("C hEkNr=" + hEkNr, logDrucken, 10);
        lDbBundle = new DbBundle();
        lDbBundle.openAll();
        lDbBundle.openWeitere();

        int rc = lDbBundle.dbMeldungen.leseEkNummer(hEkNr);
        if (rc < 1) {
            lDbBundle.closeAll();
            fehlerMeldung("Badge nicht verfügbar!");
            return;
        }

        EclMeldung lMeldung = lDbBundle.dbMeldungen.meldungenArray[0];

        einlesenZuordnung(lMeldung);

        boolean batchGefunden = false;
        if (listeDerVertretenen != null && listeDerVertretenen.size() > 0) {
            batchGefunden = true;
        } else {
            /*Nun ggf. Begleitperson-Batch prüfen*/
            if (lMeldung.meldungIstEinGast()) {
                lDbBundle.dbZugeordneteAktionaerePraesenz
                        .readAlleAktivenVertreterEinesAktionaers(lMeldung.loginKennung);
                if (lDbBundle.dbZugeordneteAktionaerePraesenz.anzErgebnis() > 0) {
                    lMeldung = leseMeldungZuKennung(
                            lDbBundle.dbZugeordneteAktionaerePraesenz.ergebnisPosition(0).anwesendePersonLoginIdent);
                    einlesenZuordnung(lMeldung);
                    batchGefunden = true;
                }
            }
        }

        if (batchGefunden == false) {
            lDbBundle.closeAll();
            fehlerMeldung("Badge nicht verfügbar!");
            return;
        }
        lDbBundle.closeAll();

        /**
         * Nun prüfen, ob eingelesene Batches vollständig bearbeitenbar - sonst Meldung
         * und Druck anbieten
         */
        hinweiseFuerGesperrteBadges();
        erstAnzeigeTeilnehmer();

    }

    /**
     * Liest die Zuordnung ein zu dem Vertreter, der in pMeldung steht.
     *
     * @param pMeldung the meldung
     */
    private void einlesenZuordnung(EclMeldung pMeldung) {
        boolean praesenteGefunden = false;

        if (pMeldung.meldungIstEinGast()) {
            vertreterId = pMeldung.loginKennung;
        } else {
            vertreterId = pMeldung.aktionaersnummer;
        }
        vertreterName = pMeldung.name;
        vertreterVorname = pMeldung.vorname;
        vertreterOrt = pMeldung.ort;

        CaBug.druckeLog("kennungTeilnehmer=" + vertreterId, logDrucken, 10);

        lDbBundle.dbZugeordneteAktionaerePraesenz.readLoginKennung(vertreterId);
        for (int i = 0; i < lDbBundle.dbZugeordneteAktionaerePraesenz.anzErgebnis(); i++) {
            EclZugeordneteAktionaerePraesenz lZugeordneteAktionaerePraesenz = lDbBundle.dbZugeordneteAktionaerePraesenz
                    .ergebnisPosition(i);
            if (lZugeordneteAktionaerePraesenz.zuordnungsArt == 9999) {
                lDbBundle.dbPersonenNatJur.leseKennung(lZugeordneteAktionaerePraesenz.zugeordneterAktionaer);
                lZugeordneteAktionaerePraesenz.eclPersonenNatJur = lDbBundle.dbPersonenNatJur.personenNatJurArray[0];
                addListeDerBegleitpersonen(lZugeordneteAktionaerePraesenz);

                /*Meldung einlesen, um Präsenzstatus festzustellen*/
                lDbBundle.dbMeldungen.leseZuPersonenNatJurIdent(lZugeordneteAktionaerePraesenz.eclPersonenNatJur.ident);
                if (lDbBundle.dbMeldungen.meldungenArray[0].statusPraesenz == 1) {
                    praesenteGefunden = true;
                    lZugeordneteAktionaerePraesenz.zurBearbeitungGesperrt = true;
                }
            } else {
                lDbBundle.dbAktienregister
                        .leseZuAktienregisternummer(lZugeordneteAktionaerePraesenz.zugeordneterAktionaer);
                lZugeordneteAktionaerePraesenz.eclAktienregister = lDbBundle.dbAktienregister.ergebnisPosition(0);
                addListeDerVertretenen(lZugeordneteAktionaerePraesenz);

                /*Meldung einlesen, um Präsenzstatus festzustellen*/
                lDbBundle.dbMeldungen.leseZuAktionaersnummer(lZugeordneteAktionaerePraesenz.zugeordneterAktionaer);
                if (lDbBundle.dbMeldungen.meldungenArray[0].statusPraesenz == 1) {
                    praesenteGefunden = true;
                    listeDerVertretenenGesperrt = true;
                }
            }
        }
        /*aktuelle BadgeIdents einlesen - nur benötigt, wenn ein Badge präsent
         * und deshalb gesperrt
         */
        listDerBegleitpersonenZuDrucken = new int[listeDerBegleitpersonen.size()];
        listeDerBegleitpersonenIdent = new String[listeDerBegleitpersonen.size()];
        if (praesenteGefunden) {
            ermittleIdentsFuerBadges();
        }

    }

    /**
     * On btn suchen start.
     *
     * @param event the event
     */
    @FXML
    void onBtnSuchenStart(ActionEvent event) {
        zuordnungWarBereitsVorhanden = true;

        suchenAufrufenFolgeSuche();
        if (rcSuchenFolgeSucheFunktion == 0) {
            return;
        }
        String aktionaersNummer = rcSuchenFolgeSucheAktienregister.aktionaersnummer;
        lDbBundle = new DbBundle();
        lDbBundle.openAll();
        lDbBundle.openWeitere();

        lDbBundle.dbZugeordneteAktionaerePraesenz.readAlleVertreterEinesAktionaers(aktionaersNummer);
        String vertreterAktiv = "";
        String vertreterInaktiv = "";
        for (int i = 0; i < lDbBundle.dbZugeordneteAktionaerePraesenz.anzErgebnis(); i++) {
            EclZugeordneteAktionaerePraesenz lZugeordneteAktionaerePraesenz = lDbBundle.dbZugeordneteAktionaerePraesenz
                    .ergebnisPosition(i);
            if (lZugeordneteAktionaerePraesenz.zuordnungIstAktiv == 1) {
                vertreterAktiv = lZugeordneteAktionaerePraesenz.anwesendePersonLoginIdent;
            } else {
                vertreterInaktiv = lZugeordneteAktionaerePraesenz.anwesendePersonLoginIdent;
            }
        }

        if (vertreterAktiv.isEmpty()) {
            vertreterAktiv = vertreterInaktiv;
        }

        if (vertreterAktiv.startsWith("S")) {
            lDbBundle.dbLoginDaten.read_loginKennung(vertreterAktiv);
            lDbBundle.dbMeldungen.leseZuMeldungsIdent(lDbBundle.dbLoginDaten.ergebnisPosition(0).meldeIdent);
        } else {
            lDbBundle.dbMeldungen.leseZuAktionaersnummer(vertreterAktiv);
        }

        EclMeldung lMeldung = lDbBundle.dbMeldungen.meldungenArray[0];
        einlesenZuordnung(lMeldung);

        lDbBundle.closeAll();
        /**
         * Nun prüfen, ob eingelesene Batches vollständig bearbeitenbar - sonst Meldung
         * und Druck anbieten
         */
        hinweiseFuerGesperrteBadges();
        erstAnzeigeTeilnehmer();

    }

    /**
     * Hinweise fuer gesperrte badges.
     */
    private void hinweiseFuerGesperrteBadges() {
        if (listeDerVertretenenGesperrt) {
            CaZeigeHinweis caZeigeHinweis = new CaZeigeHinweis();
            boolean bRc = caZeigeHinweis.zeige2Buttons(eigeneStage,
                    "Badge für Teilnehmer " + vertreterVorname + " " + vertreterName +
                            " ist präsent und deshalb gesperrt. Badge für Abgangsbuchung ausdrucken?",
                    "Ja", "Nein");
            if (bRc == true) {
                /*Badge für Teilnehmer ausdrucken*/
                druckenTeilnehmerBadge();
            }
        }
        for (int i = 0; i < listeDerBegleitpersonen.size(); i++) {
            if (listeDerBegleitpersonen.get(i).zurBearbeitungGesperrt) {
                CaZeigeHinweis caZeigeHinweis = new CaZeigeHinweis();
                boolean bRc = caZeigeHinweis.zeige2Buttons(eigeneStage,
                        "Badge für Begleitperson " +
                                listeDerBegleitpersonen.get(i).eclPersonenNatJur.vorname + " " +
                                listeDerBegleitpersonen.get(i).eclPersonenNatJur.name +
                                " ist präsent und deshalb gesperrt. Badge für Abgangsbuchung ausdrucken?",
                        "Ja", "Nein");
                if (bRc == true) {
                    /*Badge für Begleitperson ausdrucken*/
                    druckenBegleitpersonBadge(i);
                }
            }
        }
    }

    /**
     * On btn neue zuordnung start.
     *
     * @param event the event
     */
    @FXML
    void onBtnNeueZuordnungStart(ActionEvent event) {
        zuordnungWarBereitsVorhanden = false;
        /** Suchmaske */
        suchenAufrufenErstSuche();
        CaBug.druckeLog("rcSuchenErstSucheFunktion=" + rcSuchenErstSucheFunktion, logDrucken, 10);
        switch (rcSuchenErstSucheFunktion) {
        case 0:
            return; /*Bleibt alles beim alten*/
        case 1:
            /*Mitglied gefunden*/
            neueZuordnungStartMitgliedGefunden();
            break;
        case 2:

            neuenVertreterAnlegen();
            aktuelleFunktion = 1;
            break;

        }

    }

    /**
     * Neue zuordnung start mitglied gefunden.
     */
    private void neueZuordnungStartMitgliedGefunden() {
        /*Vorgespeicherte Vollmachten einlesen des Mitglieds, das eingelesen wurde*/
        lDbBundle = new DbBundle();
        lDbBundle.openAll();
        lDbBundle.openWeitere();

        /*Prüfen, ob das gefundene Mitglied bereits anderweitig zugeordnet ist - wenn ja, dann ablehnen.
         * Hinweis: Prüfung auf Präsenz erübrigt sich, da ein Mitglied nur über eine Zuordnung
         * präsent gemacht werden kann
         */
        boolean bRc = pruefeObMitgliedBereitsAnderemTeilnehmerZugeordnet(rcSuchenErstSucheAktienregister);
        if (bRc == true) {
            lDbBundle.closeAll();
            fehlerMeldung(fehlerMeldungAnderemBatchZugeordnet);
            return;
        }

        /*Vorgespeicherte Vollmachten einlesen des Mitglieds, das eingelesen wurde*/
        lDbBundle.dbVorlaeufigeVollmacht
                .readErteiltUndGueltigVonAktionaer(rcSuchenErstSucheAktienregister.aktienregisterIdent);
        abfrageVorlaeufigeVollmacht = null;
        if (lDbBundle.dbVorlaeufigeVollmacht.anzErgebnis() > 0) {
            abfrageVorlaeufigeVollmacht = lDbBundle.dbVorlaeufigeVollmacht.ergebnisPosition(0);
        }
        lDbBundle.closeAll();

        frageVertretungAbBeiErstmitglied();

    }

    /**
     * *****************Neuen Vertreter
     * anlegen*************************************.
     */

    private void neuenVertreterAnlegen() {

        if (neuerVertreterIstGesetzlich != -1) {
            /*Mitgliedsname anzeigen*/
            lblNeuerVertreterMitgliedName
                    .setText(rcSuchenErstSucheAktienregister.nachname + " " + rcSuchenErstSucheAktienregister.vorname);
            lblNeuerVertreterMitgliedOrt.setText(rcSuchenErstSucheAktienregister.ort);

            /*Mitgliedsgruppe anzeigen*/
            String hMitgliedsgruppe = KonstGruppen.getText(rcSuchenErstSucheAktienregister.getGruppe());
            lblNeuerVertreterMitgliedArt.setText(hMitgliedsgruppe);
        } else {
            /*Noch kein Mitglied ausgewählt*/
            lblNeuerVertreterMitgliedName.setText("");
            lblNeuerVertreterMitgliedOrt.setText("");

            /*Mitgliedsgruppe anzeigen*/
            lblNeuerVertreterMitgliedArt.setText("");

        }
        CaBug.druckeLog("neuerVertreterIstGesetzlich=" + neuerVertreterIstGesetzlich, logDrucken, 10);

        lblNeuerVertreterAuswahl1.setVisible(true);
        lblNeuerVertreterAuswahl2.setVisible(true);
        cb1.setVisible(true);
        cb2.setVisible(true);
        cb3.setVisible(true);
        cb4.setVisible(true);
        cb5.setVisible(true);
        cb6.setVisible(true);
        cb7.setVisible(true);
        cb8.setVisible(true);
        cb9.setVisible(true);

        switch (neuerVertreterIstGesetzlich) {
        case -1:
            lblNeuerVertreterUeberschrift.setText("Daten des neuen Vertreters:");
            break;
        case 0:
            lblNeuerVertreterUeberschrift.setText("Daten des (neuen) Bevollmächtigten:");
            break;
        case 1:
            lblNeuerVertreterUeberschrift.setText("Daten des (neuen) gesetzlichen/internen Vertreters:");
            lblNeuerVertreterAuswahl1.setVisible(false);
            lblNeuerVertreterAuswahl2.setVisible(false);
            cb1.setVisible(false);
            cb2.setVisible(false);
            cb3.setVisible(false);
            cb4.setVisible(false);
            cb5.setVisible(false);
            cb6.setVisible(false);
            cb7.setVisible(false);
            cb8.setVisible(false);
            cb9.setVisible(false);
            break;
        }
        showNeuerVertreterTab();
    }

    /**
     * On btn abbrechen neuer vertreter.
     *
     * @param event the event
     */
    @FXML
    void onBtnAbbrechenNeuerVertreter(ActionEvent event) {
        initalisierenFuerNaechstenVorgang();

    }

    /**
     * On btn uebernehmen neuer vertreter.
     *
     * @param event the event
     */
    @FXML
    void onBtnUebernehmenNeuerVertreter(ActionEvent event) {
        vertreterId = "";
        vertreterName = tfNeuerVertreterName.getText();
        vertreterVorname = tfNeuerVertreterVorname.getText();
        vertreterOrt = tfNeuerVertreterOrt.getText();

        if (vertreterName.isEmpty() || vertreterName.length() > 80) {
            fehlerMeldung("Bitte Vertretername mit maximal 80 Zeichen eingeben");
            return;
        }
        if (vertreterVorname.isEmpty() || vertreterVorname.length() > 80) {
            fehlerMeldung("Bitte Vertretervorname mit maximal 80 Zeichen eingeben");
            return;
        }
        if (vertreterOrt.isEmpty() || vertreterOrt.length() > 80) {
            fehlerMeldung("Bitte Vertreterort mit maximal 80 Zeichen eingeben");
            return;
        }

        neuerVertreterArt = 0;

        if (cb1.isSelected()) {
            neuerVertreterArt = 1;
        }
        if (cb2.isSelected()) {
            neuerVertreterArt = 2;
        }
        if (cb3.isSelected()) {
            neuerVertreterArt = 3;
        }
        if (cb4.isSelected()) {
            neuerVertreterArt = 4;
        }
        if (cb5.isSelected()) {
            neuerVertreterArt = 5;
        }
        if (cb6.isSelected()) {
            neuerVertreterArt = 6;
        }
        if (cb7.isSelected()) {
            neuerVertreterArt = 7;
        }
        if (cb8.isSelected()) {
            neuerVertreterArt = 8;
        }
        if (cb9.isSelected()) {
            neuerVertreterArt = 9;
        }
        if (neuerVertreterIstGesetzlich == 1) {
            neuerVertreterArt = 0;
        } else {
            if (neuerVertreterArt == 0) {
                fehlerMeldung("Bitte geben Sie die Art des Vertreters ein");
                return;
            }
        }

        neuerVertreter = true;

        switch (aktuelleFunktion) {
        case 1: /*Neuen Vertreter eingeben, noch kein Mitglied ausgewählt*/
            erstAnzeigeTeilnehmer();
            break;
        case 2: /*Neuen Vertreter (Bevollmächtigter) eingeben, Mitglied bereits ausgewählt*/
            int vollmachtsart = 0;
            if (neuerVertreterIstGesetzlich == 1) {
                vollmachtsart = 1;
            }
            addListeDerVertretenen(rcSuchenErstSucheAktienregister, vollmachtsart);
            holeVollmachtenEinesZugeordnetenMitglieds(rcSuchenErstSucheAktienregister, vollmachtsart);
            if (holeVollmachtenFehler.isEmpty() == false) {
                fehlerMeldung("Achtung: Vollmachten der Mitglieder " + holeVollmachtenFehler + " konnten nicht"
                        + " zugeordnet werden, da bereits anderem Teilnehmer zugeordnet");
            }
            erstAnzeigeTeilnehmer();
            break;
        }
    }

    /**
     * ********************************************Abfrage
     * Erst-Mitglied*************************************************************.
     */
    //    private boolean vertreterAngemeldetBeiEinzelmitglied=false;
    /**
     * !=null => das Mitglied, über den der Einstieg in den neuen Vorgang erfolgte,
     * hat eine Vollmacht im Vorfeld eingereicht. Diese steht dann hier drin
     */
    private EclVorlaeufigeVollmacht abfrageVorlaeufigeVollmacht = null;

    /**
     * Frage vertretung ab bei erstmitglied.
     */
    private void frageVertretungAbBeiErstmitglied() {
        showVertretungTab();

        int selbstMoeglich = 0;
        int gesetzlichMoeglich = 0;

        switch (rcSuchenErstSucheAktienregister.gruppe) {
        case KonstGruppen.einzelmitglied:
            selbstMoeglich = 1;
            gesetzlichMoeglich = 0;
            break;
        case KonstGruppen.erbengemeinschaft:
            selbstMoeglich = 0;
            gesetzlichMoeglich = 1;
            break;
        case KonstGruppen.eheleuteGbR:
            selbstMoeglich = 1;
            gesetzlichMoeglich = 0;
            break;
        default:
            selbstMoeglich = 0;
            gesetzlichMoeglich = 1;
            break;
        }

        /*Mitgliedsname anzeigen*/
        lblSelbstName.setText(rcSuchenErstSucheAktienregister.nachname + " " + rcSuchenErstSucheAktienregister.vorname);
        lblSelbstOrt.setText(rcSuchenErstSucheAktienregister.ort);

        /*Mitgliedsgruppe anzeigen*/
        String hMitgliedsgruppe = KonstGruppen.getText(rcSuchenErstSucheAktienregister.getGruppe());
        lblSelbstArt.setText(hMitgliedsgruppe);

        if (selbstMoeglich == 1) {
            if (rcSuchenErstSucheAktienregister.gruppe == KonstGruppen.eheleuteGbR) {
                btnSelbstSelbst.setText("Eine(r) der beiden Partner, mit ggf. zweitem Partner als Begleitperson: "
                        + rcSuchenErstSucheAktienregister.nachname + " " + rcSuchenErstSucheAktienregister.vorname);
            } else {
                btnSelbstSelbst.setText("Das Mitglied selbst: " + rcSuchenErstSucheAktienregister.nachname + " "
                        + rcSuchenErstSucheAktienregister.vorname);
            }
            btnSelbstSelbst.setVisible(true);
        } else {
            btnSelbstSelbst.setVisible(false);
        }

        if (abfrageVorlaeufigeVollmacht != null) {
            int gesetzlVertreter = abfrageVorlaeufigeVollmacht.bevollmaechtigterAusgefuehrtIstGesetzlich;
            String buttonBeschriftungVertreterName = abfrageVorlaeufigeVollmacht.bevollmaechtigterName + ", " +
                    abfrageVorlaeufigeVollmacht.bevollmaechtigterVorname + " "
                    + abfrageVorlaeufigeVollmacht.bevollmaechtigterOrt;
            if (gesetzlVertreter == 0) {
                btnVertreterSelbst
                        .setText("Bevollmächtigter (bereits Nachgewiesen): " + buttonBeschriftungVertreterName);
                btnVertreterSelbst.setVisible(true);
                btnGesetzlVertreterSelbst.setVisible(false);
            } else {
                btnGesetzlVertreterSelbst.setText(
                        "Gesetzl./interner Vertreter (bereits Nachgewiesen): " + buttonBeschriftungVertreterName);
                btnVertreterSelbst.setVisible(false);
                btnGesetzlVertreterSelbst.setVisible(true);
            }
        } else {
            btnVertreterSelbst.setVisible(false);
            btnGesetzlVertreterSelbst.setVisible(false);
        }

        btnNeuerVertreterSelbst.setVisible(true);
        if (gesetzlichMoeglich == 1) {
            btnNeuerGesetzlVertreterSelbst.setVisible(true);
        } else {
            btnNeuerGesetzlVertreterSelbst.setVisible(false);
        }
    }

    /**
     * On btn selbst selbst.
     *
     * @param event the event
     */
    @FXML
    void onBtnSelbstSelbst(ActionEvent event) {
        if (rcSuchenErstSucheAktienregister.gruppe == KonstGruppen.eheleuteGbR) {
            frageTeilnehmerGbRAb();
            return;
        }
        /*Vertreter = er selbst*/
        vertreterId = rcSuchenErstSucheAktienregister.aktionaersnummer;
        vertreterName = rcSuchenErstSucheAktienregister.nachname;
        vertreterVorname = rcSuchenErstSucheAktienregister.vorname;
        vertreterOrt = rcSuchenErstSucheAktienregister.ort;

        /*Zugeordente Mitglieder = er selbst*/
        addListeDerVertretenen(rcSuchenErstSucheAktienregister, -1);
        holeVollmachtenEinesZugeordnetenMitglieds(rcSuchenErstSucheAktienregister, -1);
        if (holeVollmachtenFehler.isEmpty() == false) {
            fehlerMeldung("Achtung: Vollmachten der Mitglieder " + holeVollmachtenFehler + "konnten nicht"
                    + "zugeordnet werden, da bereits anderem Teilnehmer zugeordnet");
        }

        erstAnzeigeTeilnehmer();

    }

    /**
     * On btn neuer vertreter selbst.
     *
     * @param event the event
     */
    @FXML
    void onBtnNeuerVertreterSelbst(ActionEvent event) {
        CaBug.druckeLog("", logDrucken, 10);
        neuerVertreterIstGesetzlich = 0;
        aktuelleFunktion = 2;
        neuenVertreterAnlegen();

    }

    /**
     * On btn neuer gesetzl vertreter selbst.
     *
     * @param event the event
     */
    @FXML
    void onBtnNeuerGesetzlVertreterSelbst(ActionEvent event) {
        CaBug.druckeLog("", logDrucken, 10);
        neuerVertreterIstGesetzlich = 1;
        aktuelleFunktion = 2;
        neuenVertreterAnlegen();

    }

    /**
     * On btn vertreter selbst.
     *
     * @param event the event
     */
    @FXML
    void onBtnVertreterSelbst(ActionEvent event) {
        neuerVertreterIstGesetzlich = 0;
        fuelleVertreterdaten();
        erstAnzeigeTeilnehmer();
    }

    /**
     * On btn gesetzl vertreter selbst.
     *
     * @param event the event
     */
    @FXML
    void onBtnGesetzlVertreterSelbst(ActionEvent event) {
        neuerVertreterIstGesetzlich = 1;
        fuelleVertreterdaten();
        erstAnzeigeTeilnehmer();

    }

    /**
     * Fuelle vertreterdaten.
     */
    public void fuelleVertreterdaten() {
        int vertreterArt = abfrageVorlaeufigeVollmacht.bevollmaechtigterAusgefuehrtArt;
        lDbBundle = new DbBundle();
        lDbBundle.openAll();
        if (vertreterArt == 1) {
            /*AktienregisterIdent*/
            lDbBundle.dbAktienregister
                    .leseZuAktienregisterIdent(abfrageVorlaeufigeVollmacht.bevollmaechtigterAusgefuehrtIdent);
            vertreterId = lDbBundle.dbAktienregister.ergebnisPosition(0).aktionaersnummer;
        } else {
            /*PersonNatJurIdent*/
            lDbBundle.dbPersonenNatJur.read(abfrageVorlaeufigeVollmacht.bevollmaechtigterAusgefuehrtIdent);
            vertreterId = lDbBundle.dbPersonenNatJur.personenNatJurArray[0].loginKennung;
        }
        lDbBundle.closeAll();

        vertreterName = abfrageVorlaeufigeVollmacht.bevollmaechtigterName;
        vertreterVorname = abfrageVorlaeufigeVollmacht.bevollmaechtigterVorname;
        vertreterOrt = abfrageVorlaeufigeVollmacht.bevollmaechtigterOrt;

        neuerVertreter = false;

        int vollmachtsart = 0;
        if (neuerVertreterIstGesetzlich == 1) {
            vollmachtsart = 1;
        }
        addListeDerVertretenen(rcSuchenErstSucheAktienregister, vollmachtsart);
        holeVollmachtenEinesZugeordnetenMitglieds(rcSuchenErstSucheAktienregister, vollmachtsart);
        if (holeVollmachtenFehler.isEmpty() == false) {
            fehlerMeldung("Achtung: Vollmachten der Mitglieder " + holeVollmachtenFehler + "konnten nicht"
                    + "zugeordnet werden, da bereits anderem Teilnehmer zugeordnet");
        }

    }

    /**
     * On btn abbrechen selbst.
     *
     * @param event the event
     */
    @FXML
    void onBtnAbbrechenSelbst(ActionEvent event) {
        initalisierenFuerNaechstenVorgang();

    }

    /**
     * *****************Anzeige GbrTab**************************************.
     */

    private void frageTeilnehmerGbRAb() {
        showGbRTab();

        /*Mitgliedsname anzeigen*/
        lblGbRMitgliedName
                .setText(rcSuchenErstSucheAktienregister.nachname + " " + rcSuchenErstSucheAktienregister.vorname);
        lblGbRMitgliedOrt.setText(rcSuchenErstSucheAktienregister.ort);

        /*Mitgliedsgruppe anzeigen*/
        String hMitgliedsgruppe = KonstGruppen.getText(rcSuchenErstSucheAktienregister.getGruppe());
        lblGbRMitgliedArt.setText(hMitgliedsgruppe);

        String nameGbR = rcSuchenErstSucheAktienregister.nachname;

        int posGbr = nameGbR.toUpperCase().indexOf(" - IN GBR -");
        nameGbR = nameGbR.substring(0, posGbr);

        int posUnd = nameGbR.toUpperCase().indexOf(" UND ");

        if (posUnd == -1) {
            tfGbRNameTeilnehmer.setText(nameGbR);
            tfGbRVornameTeilnehmer.setText("");
            tfGbROrtTeilnehmer.setText(rcSuchenErstSucheAktienregister.ort);

            tfGbRNameBegleitperson.setText("");
            ;
            tfGbRVornameBegleitperson.setText("");
            ;
            tfGbROrtBegleitperson.setText(rcSuchenErstSucheAktienregister.ort);
            ;
        } else {
            String vorname1 = "", nachname1 = "";
            String name1 = nameGbR.substring(0, posUnd).trim();
            String vorname2 = "", nachname2 = "";
            String name2 = nameGbR.substring(posUnd + 4).trim();
            int pos1 = name1.indexOf(" ");
            int pos2 = name2.indexOf(" ");
            if (pos1 != -1) {
                vorname1 = name1.substring(0, pos1);
                nachname1 = name1.substring(pos1 + 1);
            } else {
                vorname1 = "";
                nachname1 = name1;
            }
            if (pos2 != -1) {
                vorname2 = name2.substring(0, pos2);
                nachname2 = name2.substring(pos2);
            } else {
                vorname2 = "";
                nachname2 = name2;
            }

            tfGbRNameTeilnehmer.setText(nachname1.trim());
            tfGbRVornameTeilnehmer.setText(vorname1.trim());
            tfGbROrtTeilnehmer.setText(rcSuchenErstSucheAktienregister.ort);

            tfGbRNameBegleitperson.setText(nachname2.trim());
            ;
            tfGbRVornameBegleitperson.setText(vorname2.trim());
            ;
            tfGbROrtBegleitperson.setText(rcSuchenErstSucheAktienregister.ort);
            ;

        }

    }

    /**
     * On btn abbrechen gb R.
     *
     * @param event the event
     */
    @FXML
    void onBtnAbbrechenGbR(ActionEvent event) {
        initalisierenFuerNaechstenVorgang();

    }

    /**
     * On btn gb R keine begleitperson.
     *
     * @param event the event
     */
    @FXML
    void onBtnGbRKeineBegleitperson(ActionEvent event) {
        tfGbRNameBegleitperson.setText("");
        tfGbRVornameBegleitperson.setText("");
        tfGbROrtBegleitperson.setText("");

    }

    /**
     * On btn gb R tauschen.
     *
     * @param event the event
     */
    @FXML
    void onBtnGbRTauschen(ActionEvent event) {
        String tauschString = "";
        tauschString = tfGbRNameTeilnehmer.getText();
        tfGbRNameTeilnehmer.setText(tfGbRNameBegleitperson.getText());
        tfGbRNameBegleitperson.setText(tauschString);

        tauschString = tfGbRVornameTeilnehmer.getText();
        tfGbRVornameTeilnehmer.setText(tfGbRVornameBegleitperson.getText());
        tfGbRVornameBegleitperson.setText(tauschString);

        tauschString = tfGbROrtTeilnehmer.getText();
        tfGbROrtTeilnehmer.setText(tfGbROrtBegleitperson.getText());
        tfGbROrtBegleitperson.setText(tauschString);

    }

    /**
     * On btn uebernehmen gb R.
     *
     * @param event the event
     */
    @FXML
    void onBtnUebernehmenGbR(ActionEvent event) {
        String teilnehmerName = tfGbRNameTeilnehmer.getText().trim();
        String teilnehmerVorname = tfGbRVornameTeilnehmer.getText().trim();
        String teilnehmerOrt = tfGbROrtTeilnehmer.getText().trim();

        String begleitpersonName = tfGbRNameBegleitperson.getText().trim();
        String begleitpersonVorname = tfGbRVornameBegleitperson.getText().trim();
        String begleitpersonOrt = tfGbROrtBegleitperson.getText().trim();
        boolean begleitpersonVorhanden = false;

        if (teilnehmerName.isEmpty() || teilnehmerVorname.isEmpty() || teilnehmerOrt.isEmpty()) {
            fehlerMeldung("Bitte Name, Vorname und Ort des Teilnehmers eingeben");
            return;
        }
        if (teilnehmerName.length() > 80) {
            fehlerMeldung("Bitte Name des Teilnehmers mit maximal 80 Zeichen eingeben");
            return;
        }
        if (teilnehmerVorname.length() > 80) {
            fehlerMeldung("Bitte Vorname des Teilnehmers mit maximal 80 Zeichen eingeben");
            return;
        }
        if (teilnehmerOrt.length() > 80) {
            fehlerMeldung("Bitte Ort des Teilnehmers mit maximal 80 Zeichen eingeben");
            return;
        }

        if (begleitpersonName.isEmpty() == false && begleitpersonVorname.isEmpty() == false
                && begleitpersonOrt.isEmpty() == false) {
            begleitpersonVorhanden = true;
            if (begleitpersonName.isEmpty() || begleitpersonVorname.isEmpty() || begleitpersonOrt.isEmpty()) {
                fehlerMeldung("Bitte Name, Vorname und Ort der Begleitperson eingeben");
                return;
            }
            if (begleitpersonName.length() > 80) {
                fehlerMeldung("Bitte Name der Begleitperson mit maximal 80 Zeichen eingeben");
                return;
            }
            if (begleitpersonVorname.length() > 80) {
                fehlerMeldung("Bitte Vorname der Begleitperson mit maximal 80 Zeichen eingeben");
                return;
            }
            if (begleitpersonOrt.length() > 80) {
                fehlerMeldung("Bitte Ort der Begleitperson mit maximal 80 Zeichen eingeben");
                return;
            }

        }

        /*Vertreter = stimmberechtigter Teilnehmer*/
        neuerVertreter = true;
        vertreterId = "";
        vertreterName = teilnehmerName;
        vertreterVorname = teilnehmerVorname;
        vertreterOrt = teilnehmerOrt;

        /*Zugeordente Mitglieder = er selbst*/
        addListeDerVertretenen(rcSuchenErstSucheAktienregister, 1);
        holeVollmachtenEinesZugeordnetenMitglieds(rcSuchenErstSucheAktienregister, 1);
        if (holeVollmachtenFehler.isEmpty() == false) {
            fehlerMeldung("Achtung: Vollmachten der Mitglieder " + holeVollmachtenFehler + "konnten nicht"
                    + "zugeordnet werden, da bereits anderem Teilnehmer zugeordnet");
        }

        EclPersonenNatJur lPersonenNatJur = new EclPersonenNatJur();

        if (begleitpersonVorhanden) {
            lPersonenNatJur.name = begleitpersonName;
            lPersonenNatJur.vorname = begleitpersonVorname;
            lPersonenNatJur.ort = begleitpersonOrt;
            addListeDerBegleitpersonen(lPersonenNatJur);
        }

        erstAnzeigeTeilnehmer();

    }

    /**
     * ******************************Anzeige
     * Teilnehmer-Tab*****************************************.
     */
    private int summeVollmachtenTeilnehmer = 0;

    /**
     * Erst anzeige teilnehmer.
     */
    private void erstAnzeigeTeilnehmer() {

        showTeilnehmerTab();

        lblWarnhinweisTeilnehmer.setText("");
        summeVollmachtenTeilnehmer = 0;

        /** Vertreter anzeigen **/
        if (neuerVertreter) {
            tfTeilnehmerIdent.setText("(Neu)");
        } else {
            tfTeilnehmerIdent.setText(aufbereiteteNummer(vertreterId));
        }
        tfTeilnehmerName.setText(vertreterName + ", " + vertreterVorname);
        tfTeilnehmerOrt.setText(vertreterOrt);

        tfTeilnehmerIdent.setEditable(false);
        tfTeilnehmerName.setEditable(false);
        tfTeilnehmerOrt.setEditable(false);

        /*Zugeordnete anzeigen*/
        grpnVertretene = new MeetingGridPane();
        int anzEmpfang=0;
        for (int i = 0; i < listeDerVertretenen.size(); i++) {
            Label lNr = new Label(aufbereiteteNummer(listeDerVertretenen.get(i).eclAktienregister.aktionaersnummer));
            grpnVertretene.addMeeting(lNr, 0, i + 1);

            String lVollmachtUber = aufbereiteteNummer(listeDerVertretenen.get(i).geerbtVonZugeordnetemAktionaer);

            Label lVollmachtsart = new Label();
            boolean buttonWiderrufenMoeglich = false;
            switch (listeDerVertretenen.get(i).zuordnungsArt) {
            case -1:
                //                if (vertreterId.isEmpty() ||
                //                        (vertreterId.isEmpty()==false && vertreterId.startsWith("S"))
                //                        ){
                //                    buttonWiderrufenMoeglich=true;
                //                }
                buttonWiderrufenMoeglich = true; //immer möglich, da sonst ein einmal zugeordneter Selbster keine Vollmacht mehr geben könnte
                lVollmachtsart.setText("Selbst");
                break;
            case 0:
                buttonWiderrufenMoeglich = true;
                lVollmachtsart.setText("Bevollmächtigter");
                if (listeDerVertretenen.get(i).zuordnungIstAktiv == 1) {
                    summeVollmachtenTeilnehmer++;
                }
                break;
            case 1:
                buttonWiderrufenMoeglich = true;
                lVollmachtsart.setText("Gesetzl./interner Vertreter");
                break;
            case 2:
                lVollmachtsart.setText("U-Bevoll.-Bevollmächtigter " + lVollmachtUber);
                break;
            case 3:
                lVollmachtsart.setText("U-Bevoll.-Gesetzl.Vertreter " + lVollmachtUber);
                break;
            case 4:
                lVollmachtsart.setText("U-Gesetzl.Vertr.-Bevollmächtigter " + lVollmachtUber);
                break;
            case 5:
                lVollmachtsart.setText("U-Gesetzl.Vertr.-Gesetzl.Vertreter " + lVollmachtUber);
                break;

            }
            grpnVertretene.addMeeting(lVollmachtsart, 1, i + 1);

            Label lName = new Label(listeDerVertretenen.get(i).eclAktienregister.nachname + " " +
                    listeDerVertretenen.get(i).eclAktienregister.vorname);
            grpnVertretene.addMeeting(lName, 2, i + 1);

            Label lOrt = new Label(listeDerVertretenen.get(i).eclAktienregister.ort);
            grpnVertretene.addMeeting(lOrt, 3, i + 1);

            Label lMitgliedsart = new Label(KonstGruppen.getText(listeDerVertretenen.get(i).eclAktienregister.gruppe));
            grpnVertretene.addMeeting(lMitgliedsart, 4, i + 1);

            if (listeDerVertretenen.get(i).zuordnungIstAktiv == 1) {
                anzEmpfang+=listeDerVertretenen.get(i).anmeldungenEmpfang;
            }
            Label lAnmeldungenAbendempfang=new Label(Integer.toString(listeDerVertretenen.get(i).anmeldungenEmpfang));
            grpnVertretene.addMeeting(lAnmeldungenAbendempfang, 5, i + 1);
           
            if (listeDerVertretenen.get(i).zuordnungIstAktiv == 1) {
                listeDerButtonsZuVertretenen.get(i).setText("Zuordnung widerrufen");
                Label lWiderrufen = new Label("");
                grpnVertretene.addMeeting(lWiderrufen, 6, i + 1);
            } else {
                listeDerButtonsZuVertretenen.get(i).setText("Zuordnung wiederherstellen");
                Label lWiderrufen = new Label("Widerrufen");
                grpnVertretene.addMeeting(lWiderrufen, 6, i + 1);
            }
            if (buttonWiderrufenMoeglich && listeDerVertretenenGesperrt == false) {

                grpnVertretene.addMeeting(listeDerButtonsZuVertretenen.get(i), 7, i + 1);
            }

            if (listeDerWeitereButtonsZuVertretenen.get(i) != null && listeDerVertretenenGesperrt == false) {
                grpnVertretene.addMeeting(listeDerWeitereButtonsZuVertretenen.get(i), 8, i + 1);
            }

        }

        Label lSummeAnmeldungenAbendempfang=new Label(Integer.toString(anzEmpfang));
        grpnVertretene.addMeeting(lSummeAnmeldungenAbendempfang, 5, listeDerVertretenen.size()+2);

        if (summeVollmachtenTeilnehmer > 2) {
            lblWarnhinweisTeilnehmer.setText("Zu viele Vollmachten!");
        }

        List<String> ueberschriftList = new LinkedList<String>();
        ueberschriftList.add("Nr");
        ueberschriftList.add("Vollmachtsart");
        ueberschriftList.add("Name");
        ueberschriftList.add("Ort");
        ueberschriftList.add("Mitgliedsart");

        String[] uberschriftString = new String[ueberschriftList.size()];
        for (int i = 0; i < ueberschriftList.size(); i++) {
            uberschriftString[i] = ueberschriftList.get(i);
        }
        grpnVertretene.setzeUeberschrift(uberschriftString, scpnTeilnehmer);

        if (listeDerVertretenenGesperrt == false) {
            btnTeilnehmerWeitere.setVisible(true);
        } else {
            btnTeilnehmerWeitere.setVisible(false);
        }

        /*Zugeordnete Begleitpersonen anzeigen*/
        grpnBegleitpersonen = new MeetingGridPane();
        for (int i = 0; i < listeDerBegleitpersonen.size(); i++) {
            Label lNr = new Label(listeDerBegleitpersonen.get(i).eclPersonenNatJur.loginKennung);
            grpnBegleitpersonen.addMeeting(lNr, 0, i + 1);

            Label lVollmachtsart = new Label();
            lVollmachtsart.setText("Begleitperson");
            grpnBegleitpersonen.addMeeting(lVollmachtsart, 1, i + 1);

            Label lName = new Label(listeDerBegleitpersonen.get(i).eclPersonenNatJur.name + " " +
                    listeDerBegleitpersonen.get(i).eclPersonenNatJur.vorname);
            grpnBegleitpersonen.addMeeting(lName, 2, i + 1);

            Label lOrt = new Label(listeDerBegleitpersonen.get(i).eclPersonenNatJur.ort);
            grpnBegleitpersonen.addMeeting(lOrt, 3, i + 1);

            if (listeDerBegleitpersonen.get(i).zuordnungIstAktiv == 1) {
                listeDerButtonsZuBegleitpersonen.get(i).setText("Zuordnung Widerrufen");
                Label lWiderrufen = new Label("");
                grpnBegleitpersonen.addMeeting(lWiderrufen, 4, i + 1);
            } else {
                listeDerButtonsZuBegleitpersonen.get(i).setText("Zuordnung wiederherstellen");
                Label lWiderrufen = new Label("Widerrufen");
                grpnBegleitpersonen.addMeeting(lWiderrufen, 4, i + 1);
            }

            if (listeDerBegleitpersonen.get(i).zurBearbeitungGesperrt == false) {
                grpnBegleitpersonen.addMeeting(listeDerButtonsZuBegleitpersonen.get(i), 5, i + 1);
            }

        }

        List<String> ueberschriftListBegleitpersonen = new LinkedList<String>();
        ueberschriftListBegleitpersonen.add("Nr");
        ueberschriftListBegleitpersonen.add("Art");
        ueberschriftListBegleitpersonen.add("Name");
        ueberschriftListBegleitpersonen.add("Ort");

        String[] uberschriftStringBegleitpersonen = new String[ueberschriftListBegleitpersonen.size()];
        for (int i = 0; i < ueberschriftListBegleitpersonen.size(); i++) {
            uberschriftStringBegleitpersonen[i] = ueberschriftListBegleitpersonen.get(i);
        }
        grpnBegleitpersonen.setzeUeberschrift(uberschriftStringBegleitpersonen, scpnBegleitpersonen);

    }

    /**
     * On btn teilnehmer abbrechen.
     *
     * @param event the event
     */
    @FXML
    void onBtnTeilnehmerAbbrechen(ActionEvent event) {
        initalisierenFuerNaechstenVorgang();

    }

    /**
     * On btn teilnehmer drucken.
     *
     * @param event the event
     */
    @FXML
    void onBtnTeilnehmerDrucken(ActionEvent event) {
        if (summeVollmachtenTeilnehmer > 2) {
            CaZeigeHinweis caZeigeHinweis = new CaZeigeHinweis();
            caZeigeHinweis.zeige(eigeneStage, "Zu viele Vollmachten! Widerrufen Sie eine Zuordnung!");
            return;

        }

        /*Daten initialisieren*/
        beimSpeichernInaktivGesetzteAktionaere = "";

        /*Als erstes Speichern*/

        int rc = speichern();
        if (rc < 0) {
            /*Kritische Fehler behandeln*/
            for (int i = 0; i < 5; i++) {
                switch (rc) {
                case -1:
                    fehlerMeldung("Systemfehler beim Speichern - bitte Support benachrichtigen");
                    break;
                case -2:
                    fehlerMeldung(
                            "Vertreter wurde parallel an anderem Arbeitsplatz bearbeitet - bitte Support benachrichtigen");
                    break;
                }
            }
            /*Unkritische Fehler bearbeiten*/
            switch (rc) {
            case -3:
                fehlerMeldung("Ein neuer Badge ohne aktive Zuordnungen kann nicht erstellt werden");
                break;
            }

            erstAnzeigeTeilnehmer();
            return;
        }

        if (beimSpeichernInaktivGesetzteAktionaere.isEmpty() == false) {
            rc = speichern();
            /*Einige Aktionäre wurden inaktiv gesetzt*/
            fehlerMeldung(
                    "Einige Mitglieder wurden parallel zugeordnet und deshalb in dieser Zuordnung auf inaktiv gesetzt: "
                            + beimSpeichernInaktivGesetzteAktionaere + " - bitte klären und dann erneut Drucken");
            erstAnzeigeTeilnehmer();
            return;
        }

        initalisierenFuerNaechstenVorgang();
    }

    /** The beim speichern inaktiv gesetzte aktionaere. */
    private String beimSpeichernInaktivGesetzteAktionaere = "";

    /**
     * -1 => Fehler, der nicht vorkommen darf -2 => Teilnehmer wurde von anderem
     * soeben bearbeitet -3 => keine Zuordnung von Aktiven bei Vertretenen
     * vorhanden, deshalb kein Druck / Speichern möglich -4 => Es wurden zugeordnete
     * Teilnehmer inaktiv gesetzt (Text in beimSpeichernInaktivGesetzteAktionaere),
     * weil parallel-Bearbeitung
     * 
     * Vorgehen beim Speichern: ======================== Für Vertreter:
     * -------------- > Wenn VertreterID noch nicht vorhanden, dann als Gast
     * anlegen. > Wenn Zuordnung neu erstellt, dann nix tun. > Wenn Zuordnung
     * eingelesen und erneut drucken: dann Ersatzkarte ausstellen.
     * 
     * Für zugeordnete Mitglieder - Vollmachten:
     * ----------------------------------------- > Wenn noch nicht in DB vorhanden,
     * oder Zuordnung vorher deaktiv und jetzt aktiv: Vertreter anlegen, Vollmacht
     * eintragen > Wenn noch nicht in DB vorhanden, und jetzt inaktiv; oder vorher
     * inaktiv und jetzt inaktiv: nichts machen > Wenn noch nicht in DB vorhanden
     * und jetzt aktiv, oder vorher inaktiv und jetzt aktiv Vollmacht eintragen >
     * Wenn vorher aktiv, und jetzt inaktiv: Vollmacht widerrufen > Wenn "Selbst"
     * vertreten, d.h. Vertreter-ID= Mitgliedsnummer, dann alle Vollmachten löschen
     *
     * @return the int
     */
    private int speichern() {

        int rc = pruefenAufDruckbedarf();
        if (rc < 1) {
            return rc;
        }

        abfragenFreiwilligeDrucke();

        /*Ab hier lDbBundle erforderlich*/
        lDbBundle = new DbBundle();
        lDbBundle.openAll();
        lDbBundle.openWeitere();

        rc = vertreterBearbeiten();
        if (rc < 1) {
            lDbBundle.closeAll();
            return rc;
        }

        loescheBisherigeZuordnungen();

        rc = trageAlleZuordnungenEinUndErzeugeBegleitpersonenNeu();
        if (rc < 1) {
            lDbBundle.closeAll();
            return rc;
        }

        /*Schließen und wiedereröffnen, damit alles Commitet ist*/
        lDbBundle.closeAll();
        lDbBundle.openAll();
        lDbBundle.openWeitere();

        rc = pruefeObZuordnungParallelVerarbeitet();
        if (rc < 1) {
            lDbBundle.closeAll();
            return rc;
        }

        ekUndVertreterKorrigierenFuerTeilnehmer();

        ekKorrigierenFuerBegleitpersonen();

        ermittleIdentsFuerBadges();

        lDbBundle.closeAll();

        CaBug.druckeLog("listeDerVertretenenDrucken=" + listeDerVertretenenDrucken, logDrucken, 10);
        if (listeDerVertretenenDrucken == 1 || listeDerVertretenenDrucken == 2) {
            druckenTeilnehmerBadge();
        }

        listeDerBegleitpersonenZugangsstring=new String[listeDerBegleitpersonen.size()];

        for (int i = 0; i < listeDerBegleitpersonen.size(); i++) {
            if (listDerBegleitpersonenZuDrucken[i] == 1 || listDerBegleitpersonenZuDrucken.clone()[i] == 2) {
                druckenBegleitpersonBadge(i);
            }
        }

        meldungenZuBatches();

        if (mindestensEinBadgeWurdeGedruckt == true) {
            
            /**Ablauf für Zugangsbuchungs-Aufruf:
             * Wenn erste Frage mit "Nein - Zugänge buchen" beantwortet wurde, dann werden für alle gedruckten Batches Zugangsbuchungen ausgestellt.
             * Bei Druckwiederholungen wird am Ende nochmal die Frage, ob Zugangsbuchungen durchgeführt werden sollen
             * 
             * 0 = noch nicht entschieden.
             * -1 = keine Zugangsbuchungen durchführen
             * 1 = Zugangsbuchungen durchführen
             */
            int zugangsbuchungenDurchfuehren=0;
            
            
            CaZeigeHinweis caZeigeHinweis = new CaZeigeHinweis();
            int rcB = caZeigeHinweis.zeige3Buttons(eigeneStage, "Druckwiederholung erforderlich?", "Ja",
                    "Nein - Neuer Vorgang", "Nein - Buchen");
            if (rcB == 1) {
                /*Vertreter*/
                if (listeDerVertretenenDrucken == 1 || listeDerVertretenenDrucken == 2) {
                    boolean lWiederholen = true;
                    while (lWiederholen) {
                        boolean bRc = caZeigeHinweis.zeige2Buttons(eigeneStage,
                                "Druckwiederholung für stimmberechtigten Teilnehmer " + vertreterVorname + " "
                                        + vertreterName + "?",
                                "Ja", "Nein");
                        if (bRc == false) {
                            lWiederholen = false;
                        } else {
                            druckenTeilnehmerBadge();
                        }

                    }
                }
                /*Begleitpersonen*/

                for (int i = 0; i < listeDerBegleitpersonen.size(); i++) {
                    if (listDerBegleitpersonenZuDrucken[i] == 1 || listDerBegleitpersonenZuDrucken.clone()[i] == 2) {
                        boolean lWiederholen = true;
                        while (lWiederholen) {
                            boolean bRc = caZeigeHinweis.zeige2Buttons(eigeneStage, "Druckwiederholung für Begleitpersonr "
                                    + listeDerBegleitpersonen.get(i).eclPersonenNatJur.vorname + " "
                                    + listeDerBegleitpersonen.get(i).eclPersonenNatJur.name + "?", "Ja", "Nein");
                            if (bRc == false) {
                                lWiederholen = false;
                            } else {
                                druckenBegleitpersonBadge(i);
                            }

                        }
                    }
                }
                boolean bRc=caZeigeHinweis.zeige2Buttons(eigeneStage, "Buchen durchführen?", "Ja", "Nein");
                if (bRc==true) {
                    zugangsbuchungenDurchfuehren=1;
                }
                else {
                    zugangsbuchungenDurchfuehren=-1;
                }

            }
            if (rcB==2) {
                zugangsbuchungenDurchfuehren=-1;
            }
            if (rcB==3) {
                zugangsbuchungenDurchfuehren=1;
            }
            if (zugangsbuchungenDurchfuehren==1) {
                /*Vertreter*/
                if (listeDerVertretenenDrucken == 1 || listeDerVertretenenDrucken == 2) {
                    oeffneFOmitBuchung(listeDerVertretenenZugangsstring);
                }
                for (int i = 0; i < listeDerBegleitpersonen.size(); i++) {
                    if (listDerBegleitpersonenZuDrucken[i] == 1 || listDerBegleitpersonenZuDrucken.clone()[i] == 2) {
                        oeffneFOmitBuchung(listeDerBegleitpersonenZugangsstring[i]);
                    }
                }

                
            }
        }

        return 1;
    }

    /**
     * ermitteln, ob / wo Druck möglich erforderlich etc. ist. Füllt
     * listeDerVertretenenDrucken und listDerBegleitpersonenZuDrucken. dbBundle
     * nicht erforderlich
     *
     * @return the int
     */
    private int pruefenAufDruckbedarf() {
        mindestensEinBadgeWurdeGedruckt = false;

        /*Für Liste der vertretenen*/

        /*Prüfen, ob überhaupt aktive Zuordnung dabei*/
        if (zuordnungWarBereitsVorhanden == false) {
            /*Neuer Badge - überhaupt was vorhanden?*/
            boolean gefAktiv = false;
            for (int i = 0; i < listeDerVertretenen.size(); i++) {
                if (listeDerVertretenen.get(i).zuordnungIstAktiv == 1) {
                    gefAktiv = true;
                }
            }
            if (gefAktiv == false) {
                return -3;
            }
            listeDerVertretenenDrucken = 1;
            mindestensEinBadgeWurdeGedruckt = true;
        } else {
            boolean gefAktiv = false;
            boolean gefVeraendert = false;
            for (int i = 0; i < listeDerVertretenen.size(); i++) {
                if (listeDerVertretenen.get(i).zuordnungIstAktiv == 1) {
                    gefAktiv = true;
                }
                if (listeDerVertretenen.get(i).zuordnungIstAktiv != listeDerVertretenen.get(i).statusInDatenbank) {
                    gefVeraendert = true;
                }
            }
            if (gefAktiv == true && gefVeraendert == true) {
                /*aktive vorhanden, und veränderung => Zwangsdruck*/
                listeDerVertretenenDrucken = 1;
                mindestensEinBadgeWurdeGedruckt = true;
            }
            if (gefAktiv == true && gefVeraendert == false) {
                /*Aktive vorhanden, aber keine Veränderung => Freiwilliger Druck*/
                listeDerVertretenenDrucken = 0;
            }
            if (gefAktiv == false && gefVeraendert == false) {
                /*keine Aktive vorhanden, keine Veränderung => nichts möglich*/
                listeDerVertretenenDrucken = -1;
            }
            if (gefAktiv == false && gefVeraendert == true) {
                /*keine Aktive vorhanden, aber Veränderung =>  Kein Druck, aber ungültig machen des alten Badge*/
                listeDerVertretenenDrucken = -2;
            }

        }

        /** für Liste der Begleitpersonen */
        int anzBegleitpersonen = listeDerBegleitpersonen.size();
        listDerBegleitpersonenZuDrucken = new int[anzBegleitpersonen];
        for (int i = 0; i < anzBegleitpersonen; i++) {
            boolean gefAktiv = false;
            boolean gefVeraendert = false;
            if (listeDerBegleitpersonen.get(i).zuordnungIstAktiv == 1) {
                gefAktiv = true;
            }
            if (listeDerBegleitpersonen.get(i).zuordnungIstAktiv != listeDerBegleitpersonen.get(i).statusInDatenbank) {
                gefVeraendert = true;
            }

            if (gefAktiv == true && gefVeraendert == true) {
                /*aktive vorhanden, und veränderung => Zwangsdruck*/
                listDerBegleitpersonenZuDrucken[i] = 1;
                mindestensEinBadgeWurdeGedruckt = true;
            }
            if (gefAktiv == true && gefVeraendert == false) {
                /*Aktive vorhanden, aber keine Veränderung => Freiwilliger Druck*/
                listDerBegleitpersonenZuDrucken[i] = 0;
            }
            if (gefAktiv == false && gefVeraendert == false) {
                /*keine Aktive vorhanden, keine Veränderung => nichts möglich*/
                listDerBegleitpersonenZuDrucken[i] = -1;
            }
            if (gefAktiv == false && gefVeraendert == true) {
                /*keine Aktive vorhanden, aber Veränderung =>  Kein Druck, aber ungültig machen des alten Badge*/
                listDerBegleitpersonenZuDrucken[i] = -2;
            }

        }
        return 1;
    }

    /**
     * Abfragen freiwillige drucke.
     */
    private void abfragenFreiwilligeDrucke() {
        if (listeDerVertretenenDrucken == 0 && listeDerVertretenenGesperrt == false) {
            CaZeigeHinweis caZeigeHinweis = new CaZeigeHinweis();
            boolean brc = caZeigeHinweis.zeige2Buttons(eigeneStage, "Badge für stimmberechtigten Teilnehmer "
                    + vertreterVorname + " " + vertreterName
                    + " wurde nicht verändert - trotzdem drucken und damit bisherigen Badge ungültig machen?",
                    "Nicht Drucken", "Drucken");
            if (brc == false) {
                /*Dann Drucken*/
                listeDerVertretenenDrucken = 2;
                mindestensEinBadgeWurdeGedruckt = true;
            }
        }

        for (int i = 0; i < listeDerBegleitpersonen.size(); i++) {
            if (listDerBegleitpersonenZuDrucken[i] == 0
                    && listeDerBegleitpersonen.get(i).zurBearbeitungGesperrt == false) {
                CaZeigeHinweis caZeigeHinweis = new CaZeigeHinweis();
                boolean brc = caZeigeHinweis.zeige2Buttons(eigeneStage, "Badge für Begleitperson "
                        + listeDerBegleitpersonen.get(i).eclPersonenNatJur.vorname + " "
                        + listeDerBegleitpersonen.get(i).eclPersonenNatJur.name
                        + " wurde nicht verändert - trotzdem drucken und damit bisherigen Badge ungültig machen?",
                        "Nicht Drucken", "Drucken");

                if (brc == false) {
                    /*Dann Drucken*/
                    listDerBegleitpersonenZuDrucken[i] = 2;
                    mindestensEinBadgeWurdeGedruckt = true;
                }
            }
        }

    }

    /**
     * Ggf. neuen Vertreter anlegen. vertreterID und vertreterPersonNatJurIdent
     * füllen (für spätere Vollmachtsspeicherungen)
     *
     * @return the int
     */
    private int vertreterBearbeiten() {
        /*Falls neuer Vertreter: neue Person erzeugen und Kennung setzen*/
        if (neuerVertreter) {
            CaBug.druckeLog("Neuen Vertreter anlegen", logDrucken, 10);
            BlGastkarte blGastkarte = new BlGastkarte(lDbBundle);
            EclMeldung lGastMeldung = new EclMeldung();

            lGastMeldung.kommunikationssprache = -1;
            lGastMeldung.name = vertreterName;
            lGastMeldung.vorname = vertreterVorname;
            lGastMeldung.ort = vertreterOrt;

            lGastMeldung.mailadresse = Integer.toString(neuerVertreterArt);
            blGastkarte.pVersandart = 2;

            blGastkarte.pGast = lGastMeldung;
            blGastkarte.pWillenserklaerungGeberIdent = 0; /*undefiniert*/

            int erg = blGastkarte.ausstellen();

            if (erg < 0) {
                return -1;
            }

            vertreterId = blGastkarte.rcKennung;
            vertreterPersonNatJurIdent = blGastkarte.pGast.personenNatJurIdent;

            CaBug.druckeLog("vertreterId=" + vertreterId + " vertreterPersonNatJurIdent=" + vertreterPersonNatJurIdent,
                    logDrucken, 10);

        } else {
            CaBug.druckeLog("Bestehenden Vertreter verwenden " + vertreterId, logDrucken, 10);
            lDbBundle.dbPersonenNatJur.leseKennung(vertreterId);
            vertreterPersonNatJurIdent = lDbBundle.dbPersonenNatJur.PersonNatJurGefunden(0).ident;
        }

        return 1;
    }

    /**
     * Loesche bisherige zuordnungen.
     */
    /*Alle bisherigen Zuordnungen auf die Kennung löschen*/
    private void loescheBisherigeZuordnungen() {
        lDbBundle.dbZugeordneteAktionaerePraesenz.delete(vertreterId);

    }

    /**
     * Alle Zuordnungen eintragen. Für Begleitpersonen Vertreter erzeugen
     * 
     * Dabei überprüfen, wenn ein Insert nicht möglich ist - das dürfte eigentlich
     * nicht sein, denn Key ist immer eindeutig außer die selbe Person wird
     * zeitgleich bearbeitet.
     *
     * @return the int
     */
    private int trageAlleZuordnungenEinUndErzeugeBegleitpersonenNeu() {
        for (int i = 0; i < listeDerVertretenen.size(); i++) {
            EclZugeordneteAktionaerePraesenz lZugeordneteAktionaerePraesenz = listeDerVertretenen.get(i);
            lZugeordneteAktionaerePraesenz.anwesendePersonLoginIdent = vertreterId;
            int anz = lDbBundle.dbZugeordneteAktionaerePraesenz.insert(lZugeordneteAktionaerePraesenz);
            CaBug.druckeLog("anz Insert=" + anz, logDrucken, 10);
            /*-1 => Duplicate Key => nicht einfügbar
             * Darf nicht vorkommen, da dies bedeutet dass der selbe Teilnehmer zweimal
             * bearbeitet wird - wegen Personalausweiskontrolle bei ku178 nicht möglich*/
            if (anz == -1) {
                return -2;
            }
        }

        for (int i = 0; i < listeDerBegleitpersonen.size(); i++) {
            EclZugeordneteAktionaerePraesenz lZugeordneteAktionaerePraesenz = listeDerBegleitpersonen.get(i);
            lZugeordneteAktionaerePraesenz.anwesendePersonLoginIdent = vertreterId;
            lZugeordneteAktionaerePraesenz.zuordnungsArt = 9999;
            EclPersonenNatJur lPersonenNatJur = lZugeordneteAktionaerePraesenz.eclPersonenNatJur;

            if (lZugeordneteAktionaerePraesenz.zugeordneterAktionaer.isEmpty()) {
                CaBug.druckeLog("Neue Gastkarte anlegen", logDrucken, 10);
                BlGastkarte blGastkarte = new BlGastkarte(lDbBundle);
                EclMeldung lGastMeldung = new EclMeldung();

                lGastMeldung.kommunikationssprache = -1;
                lGastMeldung.name = lPersonenNatJur.name;
                lGastMeldung.vorname = lPersonenNatJur.vorname;
                lGastMeldung.ort = lPersonenNatJur.ort;

                blGastkarte.pVersandart = 2;

                blGastkarte.pGast = lGastMeldung;
                blGastkarte.pWillenserklaerungGeberIdent = 0; /*undefiniert*/

                int erg = blGastkarte.ausstellen();

                if (erg < 0) {
                    return -1;
                }

                lZugeordneteAktionaerePraesenz.zugeordneterAktionaer = blGastkarte.rcKennung;
                CaBug.druckeLog("blGastkarte.rcKennung=" + blGastkarte.rcKennung, logDrucken, 10);

            }
            int anz = lDbBundle.dbZugeordneteAktionaerePraesenz.insert(lZugeordneteAktionaerePraesenz);
            CaBug.druckeLog("anz Insert=" + anz, logDrucken, 10);
            /*-1 => Duplicate Key => nicht einfügbar
             * Darf nicht vorkommen, da dies bedeutet dass der selbe Teilnehmer zweimal
             * bearbeitet wird - wegen Personalausweiskontrolle bei ku178 nicht möglich*/
            if (anz == -1) {
                return -2;
            }
        }

        return 1;
    }

    /**
     * Pruefe ob zuordnung parallel verarbeitet.
     *
     * @return the int
     */
    /*Checken, ob mehr als eine aktive Zuordnung gespeichert wurde*/
    private int pruefeObZuordnungParallelVerarbeitet() {
        for (int i = 0; i < listeDerVertretenen.size(); i++) {
            EclZugeordneteAktionaerePraesenz lZugeordneteAktionaerePraesenz = listeDerVertretenen.get(i);
            int rcAktionaer = lDbBundle.dbZugeordneteAktionaerePraesenz
                    .readAlleAktivenVertreterEinesAktionaers(lZugeordneteAktionaerePraesenz.zugeordneterAktionaer);
            if (rcAktionaer > 1) {
                beimSpeichernInaktivGesetzteAktionaere += lZugeordneteAktionaerePraesenz.zugeordneterAktionaer + " ";
                lZugeordneteAktionaerePraesenz.zuordnungIstAktiv = 0;
            }
        }
        if (beimSpeichernInaktivGesetzteAktionaere.isEmpty() == false) {
            return -4;
        }

        return 1;

    }

    /**
     * Ek und vertreter korrigieren fuer teilnehmer.
     */
    private void ekUndVertreterKorrigierenFuerTeilnehmer() {
        /*EK stornieren / Ausstellen*/

        /*Gedanken - wann müssen EK neu ausgestellt und storniert werden?
         * 
         * Es muß nur dann keine neue EK ausgestellt werden, wenn das Mitglied
         * noch keinem anderen Mitglied zugeordnet war. Und diesem Vertreter erstmalig zugeordnet wurde.
         * 
         * Vereinfachung: Immer stornieren.
         * 
         */
        if (listeDerVertretenenDrucken != -1 && listeDerVertretenenDrucken != 0) {
            for (int i = 0; i < listeDerVertretenen.size(); i++) {
                EclZugeordneteAktionaerePraesenz lZugeordneteAktionaerePraesenz = listeDerVertretenen.get(i);
                EclMeldung lMeldung = leseMeldungZuKennung(lZugeordneteAktionaerePraesenz.zugeordneterAktionaer);
                //                if (lZugeordneteAktionaerePraesenz.statusInDatenbank==1) {
                /*Alte EK Stornieren*/
                if (lMeldung.zutrittsIdent.isEmpty() == false) {
                    BlWillenserklaerung willenserklaerung = new BlWillenserklaerung();

                    willenserklaerung.piZutrittsIdent.zutrittsIdent = lMeldung.zutrittsIdent;
                    willenserklaerung.piZutrittsIdent.zutrittsIdentNeben = "00";

                    willenserklaerung.piKlasse = KonstMeldung.MELDUNG_IST_AKTIONAER;
                    willenserklaerung.pWillenserklaerungGeberIdent = -1; /*Aktionär*/

                    willenserklaerung.sperrenZutrittsIdent(lDbBundle);

                    /*Falls nicht möglich: Fehlermeldung, zum Anmelden*/
                    if (willenserklaerung.rcIstZulaessig == false) {
                        CaBug.drucke("001 " + willenserklaerung.rcGrundFuerUnzulaessig);
                    }

                    if (lMeldung.vertreterIdent != 0) {
                        BlWillenserklaerung vmWillenserklaerung = new BlWillenserklaerung();
                        vmWillenserklaerung.piMeldungsIdentAktionaer = lMeldung.meldungsIdent;
                        EclPersonenNatJur lPersonenNatJur = new EclPersonenNatJur();
                        lPersonenNatJur.ident = lMeldung.vertreterIdent;
                        vmWillenserklaerung.pEclPersonenNatJur = lPersonenNatJur;
                        vmWillenserklaerung.widerrufVollmachtAnDritte(lDbBundle);
                        if (vmWillenserklaerung.rcIstZulaessig == false) {
                            CaBug.drucke("002 willenserklaerung.rcGrundFuerUnzulaessig="
                                    + willenserklaerung.rcGrundFuerUnzulaessig);
                        }

                    }
                }
                //                }

                if (lZugeordneteAktionaerePraesenz.zuordnungIstAktiv == 1) {
                    /*Nun neue EK eintragen*/
                    BlWillenserklaerung ekWillenserklaerung = new BlWillenserklaerung();
                    ekWillenserklaerung.piMeldungsIdentAktionaer = lMeldung.meldungsIdent;

                    ekWillenserklaerung.pVersandartEK = 2;

                    ekWillenserklaerung.neueZutrittsIdentZuMeldung(lDbBundle);
                    if (ekWillenserklaerung.rcIstZulaessig == false) {
                        CaBug.drucke("003 " + ekWillenserklaerung.rcGrundFuerUnzulaessig);
                    }

                    CaBug.druckeLog("Neue ZutrittsIdent wurde eingetagen", logDrucken, 10);
                    if (lZugeordneteAktionaerePraesenz.zuordnungsArt != -1) {
                        CaBug.druckeLog("Nun neue Vollmacht eintragen", logDrucken, 10);
                        /*. Vollmachten eintragen*/
                        BlWillenserklaerung vmWillenserklaerung = null;
                        vmWillenserklaerung = new BlWillenserklaerung();
                        vmWillenserklaerung.piMeldungsIdentAktionaer = lMeldung.meldungsIdent;

                        EclPersonenNatJur personNatJur = new EclPersonenNatJur();
                        personNatJur.ident = vertreterPersonNatJurIdent;
                        vmWillenserklaerung.pEclPersonenNatJur = personNatJur;

                        vmWillenserklaerung.vollmachtAnDritte(lDbBundle);

                        if (vmWillenserklaerung.rcIstZulaessig == false) {
                            CaBug.drucke("004 " + vmWillenserklaerung.rcGrundFuerUnzulaessig);
                        }

                        /*Vertreter direkt in meldung eintragen - weil das aus irgendwelchen Gründen nicht durch Willenserklärung erfolgt*/
                        lDbBundle.dbMeldungen.leseZuIdent(lMeldung.meldungsIdent);
                        EclMeldung vMeldung = lDbBundle.dbMeldungen.meldungenArray[0];
                        vMeldung.vertreterIdent = vertreterPersonNatJurIdent;
                        lDbBundle.dbMeldungen.update(vMeldung);
                    }

                }

            }
        }
    }

    /**
     * Ek korrigieren fuer begleitpersonen.
     */
    private void ekKorrigierenFuerBegleitpersonen() {
        for (int i = 0; i < listeDerBegleitpersonen.size(); i++) {
            if (listDerBegleitpersonenZuDrucken[i] != -1 && listDerBegleitpersonenZuDrucken[i] != 0) {
                if (zuordnungWarBereitsVorhanden == true) {
                    /*Alte EK Stornieren*/
                    EclZugeordneteAktionaerePraesenz lZugeordneteAktionaerePraesenz = listeDerBegleitpersonen.get(i);
                    String kennungDerBegleitperson = lZugeordneteAktionaerePraesenz.zugeordneterAktionaer;
                    EclMeldung lMeldung = leseMeldungZuKennung(kennungDerBegleitperson);
                    BlGastkarte lGastkarte = new BlGastkarte(lDbBundle);
                    lGastkarte.pZutrittsIdentNebenStorno = "00";
                    lGastkarte.pZutrittsIdentStorno = lMeldung.zutrittsIdent;
                    int rc = lGastkarte.stornoGKNr();
                    if (rc < 1) {
                        CaBug.drucke("001");
                    }

                    if (listDerBegleitpersonenZuDrucken[i] == 1 || listDerBegleitpersonenZuDrucken[i] == 2) {
                        /*Neue EK ausstellen*/
                        BlGastkarte lGastkarteNeu = new BlGastkarte(lDbBundle);
                        lGastkarteNeu.pVersandart = 2;
                        lGastkarteNeu.pGast = lMeldung;
                        lGastkarteNeu.pWillenserklaerungGeberIdent = 0; /*undefiniert*/

                        lGastkarteNeu.pVersandAnAdresseAusAktienregister = 0;

                        rc = lGastkarteNeu.neueGKNr(lMeldung.meldungsIdent, 0);
                        if (rc < 1) {
                            CaBug.drucke("002");
                        }
                    }
                }
            }
        }
    }

    /**
     * Ermittle idents fuer badges.
     */
    private void ermittleIdentsFuerBadges() {
        EclMeldung lMeldungVertreter = leseMeldungZuKennung(vertreterId);
        vertreterEKIdent = lMeldungVertreter.zutrittsIdent;

        /** Vertretene */
        int anzVertretenen = listeDerVertretenen.size();
        listeDerVertretenenIdent = new String[anzVertretenen];
        if (listeDerVertretenenDrucken == 1 || listeDerVertretenenDrucken == 2) {
            for (int i = 0; i < anzVertretenen; i++) {
                EclZugeordneteAktionaerePraesenz lZugeordneteAktionaerePraesenz = listeDerVertretenen.get(i);
                String aktionaersnummerDesVertretenen = lZugeordneteAktionaerePraesenz.zugeordneterAktionaer;
                lDbBundle.dbMeldungen.leseZuAktionaersnummer(aktionaersnummerDesVertretenen);
                listeDerVertretenenIdent[i] = lDbBundle.dbMeldungen.meldungenArray[0].zutrittsIdent;
            }
        }

        /** Begleitpersonen */
        int anzBegleitpersonen = listeDerBegleitpersonen.size();
        listeDerBegleitpersonenIdent = new String[anzBegleitpersonen];
        for (int i = 0; i < anzBegleitpersonen; i++) {
            //            if (listDerBegleitpersonenZuDrucken[i]==1 || listDerBegleitpersonenZuDrucken[i]==2) {
            EclZugeordneteAktionaerePraesenz lZugeordneteAktionaerePraesenz = listeDerBegleitpersonen.get(i);
            String kennungDerBegleitperson = lZugeordneteAktionaerePraesenz.zugeordneterAktionaer;
            EclMeldung lMeldung = leseMeldungZuKennung(kennungDerBegleitperson);
            listeDerBegleitpersonenIdent[i] = lMeldung.zutrittsIdent;
            //            }
        }
    }

    /**
     * Drucken teilnehmer badge.
     */
    private void druckenTeilnehmerBadge() {
        listeDerVertretenenZugangsstring = "51;" + vertreterEKIdent + ";" + Integer.toString(vertreterPersonNatJurIdent) + ";";
        for (int i = 0; i < listeDerVertretenen.size(); i++) {
            EclZugeordneteAktionaerePraesenz lZugeordneteAktionaerePraesenz = listeDerVertretenen.get(i);
            if (lZugeordneteAktionaerePraesenz.zuordnungIstAktiv == 1) {
                listeDerVertretenenZugangsstring = listeDerVertretenenZugangsstring + listeDerVertretenenIdent[i] + ";"
                        + Integer.toString(vertreterPersonNatJurIdent) + ";";
            }
        }
        listeDerVertretenenZugangsstring = listeDerVertretenenZugangsstring + "EOL";

        RpDrucken rpDrucken = new RpDrucken();
        rpDrucken.initClientDrucke();
        rpDrucken.druckerWiederverwendet = druckerWiederverwendet;
        rpDrucken.druckerWiederverwendetNummer = 999;
        if (druckerWiederverwendet == 2) {
            rpDrucken.druckerAbfragen = false;
        }

        rpDrucken.initFormular(lDbBundle);
        RpVariablen rpVariablen = new RpVariablen(lDbBundle);
        rpVariablen.versammlungsbatch("01", rpDrucken);

        rpDrucken.startFormular();

        rpVariablen.fuelleVariable(rpDrucken, "Zugangsstring", listeDerVertretenenZugangsstring);
        rpVariablen.fuelleVariable(rpDrucken, "BadgeIdent", vertreterEKIdent);
        rpVariablen.fuelleVariable(rpDrucken, "BadgeArt", "1"); //1=Teilnehmer; 2=Begleitperson*/
        rpVariablen.fuelleVariable(rpDrucken, "TeilnehmerName", vertreterVorname + " " + vertreterName);

        rpDrucken.druckenFormular();

        rpDrucken.endeFormular();
        if (druckerWiederverwendet == 1) {
            druckerWiederverwendet = 2;
        }
    }

    /**
     * Drucken begleitperson badge.
     *
     * @param pOffset the offset
     */
    private void druckenBegleitpersonBadge(int pOffset) {
        String badgeIdent = listeDerBegleitpersonenIdent[pOffset];
        String zugangsstring = "9" + CaString.fuelleLinksNull(badgeIdent, 5) + "0";

        RpDrucken rpDrucken = new RpDrucken();
        rpDrucken.initClientDrucke();
        rpDrucken.druckerWiederverwendet = druckerWiederverwendet;
        rpDrucken.druckerWiederverwendetNummer = 999;
        if (druckerWiederverwendet == 2) {
            rpDrucken.druckerAbfragen = false;
        }

        rpDrucken.initFormular(lDbBundle);
        RpVariablen rpVariablen = new RpVariablen(lDbBundle);
        rpVariablen.versammlungsbatch("01", rpDrucken);

        rpDrucken.startFormular();

        rpVariablen.fuelleVariable(rpDrucken, "Zugangsstring", zugangsstring);
        rpVariablen.fuelleVariable(rpDrucken, "BadgeIdent", badgeIdent);
        rpVariablen.fuelleVariable(rpDrucken, "BadgeArt", "2"); //1=Teilnehmer; 2=Begleitperson*/
        rpVariablen.fuelleVariable(rpDrucken, "TeilnehmerName",
                listeDerBegleitpersonen.get(pOffset).eclPersonenNatJur.vorname + " " +
                        listeDerBegleitpersonen.get(pOffset).eclPersonenNatJur.name);

        rpDrucken.druckenFormular();

        rpDrucken.endeFormular();
        if (druckerWiederverwendet == 1) {
            druckerWiederverwendet = 2;
        }
        
        listeDerBegleitpersonenZugangsstring[pOffset]=zugangsstring;
    }

    /**
     * Meldungen zu batches.
     */
    private void meldungenZuBatches() {
        if (zuordnungWarBereitsVorhanden == false) {
            return;
        }

        if (listeDerVertretenenDrucken != 0) {
            String meldung = "Badge für stimmberechtigten Teilnehmer " + vertreterVorname + " " + vertreterName + ": ";
            switch (listeDerVertretenenDrucken) {
            case -2:
                meldung += "keine Vertretung mehr vorhanden - kein neuer Badge, alter Badge wurde ungültig";
                break;
            case -1:
                meldung += "unverändert keine Vertretung vorhanden - kein neuer Badge";
                break;
            case 1:
            case 2:
                meldung += "bisher ausgestellte Badges sind ungültig";
                break;
            }
            CaZeigeHinweis caZeigeHinweis = new CaZeigeHinweis();
            caZeigeHinweis.zeige(eigeneStage, meldung);
        }

        for (int i = 0; i < listeDerBegleitpersonen.size(); i++) {
            if (listDerBegleitpersonenZuDrucken[i] != 0) {
                String meldung = "Badge für Begleitperson " +
                        listeDerBegleitpersonen.get(i).eclPersonenNatJur.vorname
                        + " " + listeDerBegleitpersonen.get(i).eclPersonenNatJur.name + ": ";
                switch (listDerBegleitpersonenZuDrucken[i]) {
                case -2:
                    meldung += "nicht mehr aktiv - kein neuer Badge, alter Badge wurde ungültig";
                    break;
                case -1:
                    meldung += "unverändert - kein neuer Badge";
                    break;
                case 1:
                case 2:
                    meldung += "bisher ausgestellte Badges sind ungültig";
                    break;
                }
                CaZeigeHinweis caZeigeHinweis = new CaZeigeHinweis();
                caZeigeHinweis.zeige(eigeneStage, meldung);
            }

        }
    }

    /**
     * On btn teilnehmer weitere.
     *
     * @param event the event
     */
    @FXML
    void onBtnTeilnehmerWeitere(ActionEvent event) {
        suchenAufrufenFolgeSuche();
        if (rcSuchenFolgeSucheFunktion == 0) {
            return;
        }

        /*Prüfen, ob schon zugeordnet - hier erst mal unabhängig abfragen von Zuordnungsart - wenn aktiv, dann
         * hier schon nicht zulässig*/
        lDbBundleOpen();
        String lAktionaersnummerNeu = rcSuchenFolgeSucheAktienregister.aktionaersnummer;
        int zuordenbar = pruefeObMitgliedZuordenbar(lAktionaersnummerNeu, "", -999);
        lDbBundleClose();
        if (zuordenbar < 1) {
            CaZeigeHinweis caZeigeHinweis = new CaZeigeHinweis();
            switch (zuordenbar) {
            case -1:
                caZeigeHinweis.zeige(eigeneStage, "Mitglied ist bereits diesem Teilnehmer zugeordnet und aktiv!");
                break;
            case -2:
                //bereits anderem Batch zugeordnet - Fehlermeldung wird in Prüfroutine angezeigt
                caZeigeHinweis.zeige(eigeneStage, fehlerMeldungAnderemBatchZugeordnet);
                break;
            }
            return;
        }

        frageVertretungAbBeiFolgeZuordnung();

    }

    /**
     * On btn teilnehmer weitere begleitperson.
     *
     * @param event the event
     */
    @FXML
    void onBtnTeilnehmerWeitereBegleitperson(ActionEvent event) {
        neueBegleitpersonAnlegen();
    }

    /*+++++++++++++++++++++++++++Frage weitere Vertretung ab Tab+++++++++++++++++++*/

    /**
     * Frage vertretung ab bei folge zuordnung.
     */
    public void frageVertretungAbBeiFolgeZuordnung() {
        showWeitereVertretungTab();
        rbBevollmaechtigter.setSelected(false);
        rbGesetzlicherVertreter.setSelected(false);
        rbSelbst.setSelected(false);
        ;

        int selbstMoeglich = 0;
        int gesetzlichMoeglich = 0;

        switch (rcSuchenFolgeSucheAktienregister.gruppe) {
        case KonstGruppen.einzelmitglied:
            gesetzlichMoeglich = 0;
            if (vertreterId.isEmpty() ||
                    (vertreterId.isEmpty() == false && vertreterId.startsWith("S"))) {
                selbstMoeglich = 1;
            }
            break;
        case KonstGruppen.erbengemeinschaft:
            gesetzlichMoeglich = 1;
            break;
        default:
            gesetzlichMoeglich = 1;
            break;
        }

        /*Mitgliedsname anzeigen*/
        lblWeitereVertretungMitgliedName
                .setText(rcSuchenFolgeSucheAktienregister.nachname + " " + rcSuchenFolgeSucheAktienregister.vorname);
        lblWeitereVertretungMitgliedOrt.setText(rcSuchenFolgeSucheAktienregister.ort);

        /*Mitgliedsgruppe anzeigen*/
        String hMitgliedsgruppe = KonstGruppen.getText(rcSuchenFolgeSucheAktienregister.getGruppe());
        lblWeitereVertretungMitgliedArt.setText(hMitgliedsgruppe);

        if (gesetzlichMoeglich == 0) {
            rbGesetzlicherVertreter.setVisible(false);
        } else {
            rbGesetzlicherVertreter.setVisible(true);
        }

        if (selbstMoeglich == 0) {
            rbSelbst.setVisible(false);
        } else {
            rbSelbst.setVisible(true);
        }

    }

    /**
     * On btn weitere vertretung abbrechen.
     *
     * @param event the event
     */
    @FXML
    void onBtnWeitereVertretungAbbrechen(ActionEvent event) {
        showTeilnehmerTab();
    }

    /**
     * On btn weitere vertretung weiter.
     *
     * @param event the event
     */
    @FXML
    void onBtnWeitereVertretungWeiter(ActionEvent event) {
        if (rbBevollmaechtigter.isSelected() == false && rbGesetzlicherVertreter.isSelected() == false
                && rbSelbst.isSelected() == false) {
            CaZeigeHinweis caZeigeHinweis = new CaZeigeHinweis();
            caZeigeHinweis.zeige(eigeneStage, "Bitte Vertretungsart auswählen!");
            return;
        }

        int lVertretungsart = 0;
        if (rbGesetzlicherVertreter.isSelected() == true) {
            lVertretungsart = 1;
        }
        if (rbSelbst.isSelected() == true) {
            lVertretungsart = -1;
        }

        /*Nun nochmal checken, ob mit dieser Vertretungsart schon zugeordnet*/

        lDbBundleOpen();
        int zuordenbar = pruefeObMitgliedZuordenbar(rcSuchenFolgeSucheAktienregister.aktionaersnummer, "",
                lVertretungsart);
        lDbBundleClose();
        if (zuordenbar < 1) {
            CaZeigeHinweis caZeigeHinweis = new CaZeigeHinweis();
            switch (zuordenbar) {
            case -1:
                caZeigeHinweis.zeige(eigeneStage,
                        "Mitglied ist bereits diesem Teilnehmer mit dieser Vertretungsart zugeordnet!");
                break;
            case -2:
                //bereits anderem Batch zugeordnet - Fehlermeldung wird in Prüfroutine angezeigt
                caZeigeHinweis.zeige(eigeneStage, fehlerMeldungAnderemBatchZugeordnet);
                break;
            }
            return;
        }

        addListeDerVertretenen(rcSuchenFolgeSucheAktienregister, lVertretungsart);
        holeVollmachtenEinesZugeordnetenMitglieds(rcSuchenFolgeSucheAktienregister, lVertretungsart);
        erstAnzeigeTeilnehmer();

    }

    /**
     * Neue begleitperson anlegen.
     */
    /*++++++++++++++++++++++++++++++++++Neue Begleitperson++++++++++++++++++++++*/
    private void neueBegleitpersonAnlegen() {

        /*Noch kein Mitglied ausgewählt*/
        lblNeueBegleitpersonMitgliedName.setText(vertreterName + ", " + vertreterVorname);
        lblNeueBegleitpersonMitgliedOrt.setText(vertreterOrt);

        /*Mitgliedsgruppe anzeigen*/
        lblNeueBegleitpersonMitgliedArt.setText("");

        lblNeuerVertreterUeberschrift.setText("Daten der neuen Begleitperson:");

        tfNeueBegleitpersonName.setText("");
        tfNeueBegleitpersonVorname.setText("");
        tfNeueBegleitpersonOrt.setText("");

        showNeueBegleitpersonTab();
    }

    /**
     * On btn abbrechen neue begleitperson.
     *
     * @param event the event
     */
    @FXML
    void onBtnAbbrechenNeueBegleitperson(ActionEvent event) {
        showTeilnehmerTab();
    }

    /**
     * On btn uebernehmen neue begleitperson.
     *
     * @param event the event
     */
    @FXML
    void onBtnUebernehmenNeueBegleitperson(ActionEvent event) {
        EclPersonenNatJur lPersonenNatJur = new EclPersonenNatJur();

        lPersonenNatJur.name = tfNeueBegleitpersonName.getText();
        lPersonenNatJur.vorname = tfNeueBegleitpersonVorname.getText();
        lPersonenNatJur.ort = tfNeueBegleitpersonOrt.getText();

        if (lPersonenNatJur.name.isEmpty() || lPersonenNatJur.name.length() > 80) {
            fehlerMeldung("Bitte Name mit maximal 80 Zeichen eingeben");
            return;
        }
        if (lPersonenNatJur.vorname.isEmpty() || lPersonenNatJur.vorname.length() > 80) {
            fehlerMeldung("Bitte Vorname mit maximal 80 Zeichen eingeben");
            return;
        }
        if (lPersonenNatJur.ort.isEmpty() || lPersonenNatJur.ort.length() > 80) {
            fehlerMeldung("Bitte Ort mit maximal 80 Zeichen eingeben");
            return;
        }

        addListeDerBegleitpersonen(lPersonenNatJur);
        erstAnzeigeTeilnehmer();
    }

    /*++++++++++++++++++++++++++Hinterlegen Vollmacht von Kindern++++++++++++++++++++*/
    /** The l aktienregister kind. */
    //    private int kindZumErgaenzen=0;
    private EclAktienregister lAktienregisterKind = null;

    /** The summe vollmachten kind. */
    private int summeVollmachtenKind = 0;

    /**
     * On btn hinterlegen untervollmacht.
     *
     * @param event the event
     */
    void onBtnHinterlegenUntervollmacht(ActionEvent event) {
        CaBug.druckeLog("", logDrucken, 10);

        int kindZumErgaenzen = -1;
        for (int i = 0; i < listeDerWeitereButtonsZuVertretenen.size(); i++) {
            if (event.getSource() == listeDerWeitereButtonsZuVertretenen.get(i)) {
                kindZumErgaenzen = i;
            }
        }

        CaBug.druckeLog("kindZumErgaenzen=" + kindZumErgaenzen, logDrucken, 10);
        if (kindZumErgaenzen == -1) {
            return;
        }

        lAktienregisterKind = listeDerVertretenen.get(kindZumErgaenzen).eclAktienregister;

        /*Liste für Vollmachten an Kind initialisieren*/
        initListeDerVertretenenKind();

        /*Liste der Vollmachten an Kind aus der vorhandenen Liste füllen*/
        for (int i = 0; i < listeDerVertretenen.size(); i++) {
            if (listeDerVertretenen.get(i).geerbtVonZugeordnetemAktionaer
                    .equals(lAktienregisterKind.aktionaersnummer)) {
                addListeDerVertretenenKind(listeDerVertretenen.get(i));
                listeDerVertretenen.get(i).quellOffset = i;
            }
        }

        zuordnenWeitereVollmachtZuKindAnzeigen();
    }

    /**
     * Zuordnen weitere vollmacht zu kind anzeigen.
     */
    private void zuordnenWeitereVollmachtZuKindAnzeigen() {
        showKindTab();

        lblWarnhinweisKind.setText("");
        summeVollmachtenKind = 0;

        tfKindIdent.setText(lAktienregisterKind.aktionaersnummer);

        /*Mitgliedsname anzeigen*/
        tfKindName.setText(lAktienregisterKind.nachname + " " + lAktienregisterKind.vorname);
        tfKindOrt.setText(lAktienregisterKind.ort);

        tfKindIdent.setEditable(false);
        tfKindName.setEditable(false);
        tfKindOrt.setEditable(false);

        /*Zugeordnete anzeigen*/
        grpnKind = new MeetingGridPane();
        for (int i = 0; i < listeDerVertretenenKind.size(); i++) {

            Label lNr = new Label(listeDerVertretenenKind.get(i).eclAktienregister.aktionaersnummer);
            grpnKind.addMeeting(lNr, 0, i + 1);

            Label lName = new Label(listeDerVertretenenKind.get(i).eclAktienregister.nachname + " " +
                    listeDerVertretenenKind.get(i).eclAktienregister.vorname);
            grpnKind.addMeeting(lName, 1, i + 1);

            Label lOrt = new Label(listeDerVertretenenKind.get(i).eclAktienregister.ort);
            grpnKind.addMeeting(lOrt, 2, i + 1);

            Label lMitgliedsart = new Label(
                    KonstGruppen.getText(listeDerVertretenenKind.get(i).eclAktienregister.gruppe));
            grpnKind.addMeeting(lMitgliedsart, 3, i + 1);

            if (listeDerVertretenenKind.get(i).zuordnungIstAktiv == 1) {
                listeDerButtonsZuVertretenenKind.get(i).setText("Zuordnung widerrufen");
                Label lWiderrufen = new Label("");
                grpnKind.addMeeting(lWiderrufen, 4, i + 1);
                summeVollmachtenKind++;
            } else {
                listeDerButtonsZuVertretenenKind.get(i).setText("Zuordnung wiederherstellen");
                Label lWiderrufen = new Label("Widerrufen");
                grpnKind.addMeeting(lWiderrufen, 4, i + 1);
            }

            grpnKind.addMeeting(listeDerButtonsZuVertretenenKind.get(i), 5, i + 1);

        }

        List<String> ueberschriftList = new LinkedList<String>();
        ueberschriftList.add("Nr");
        ueberschriftList.add("Name");
        ueberschriftList.add("Ort");
        ueberschriftList.add("Mitgliedsart");

        String[] uberschriftString = new String[ueberschriftList.size()];
        for (int i = 0; i < ueberschriftList.size(); i++) {
            uberschriftString[i] = ueberschriftList.get(i);
        }
        grpnKind.setzeUeberschrift(uberschriftString, scpnKind);

        if (summeVollmachtenKind > 2) {
            lblWarnhinweisKind.setText("Zu viele Vollmachten!");
        }

    }

    /**
     * On btn kind uebernehmen.
     *
     * @param event the event
     */
    @FXML
    void onBtnKindUebernehmen(ActionEvent event) {
        if (summeVollmachtenKind > 2) {
            CaZeigeHinweis caZeigeHinweis = new CaZeigeHinweis();
            caZeigeHinweis.zeige(eigeneStage, "Zu viele Vollmachten! Widerrufen Sie eine Zuordnung!");
            return;

        }
        for (int i = 0; i < listeDerVertretenenKind.size(); i++) {
            /**
             * Wenn !=0, dann braucht nix gemacht zu werden, da wegen selbem Objekt alle
             * Änderungen bereits drin sind.
             * 
             * Bei 0 => neue Zuordnung
             */
            if (listeDerVertretenenKind.get(i).quellOffset == 0) {
                addListeDerVertretenen(listeDerVertretenenKind.get(i));
            }
        }
        erstAnzeigeTeilnehmer();
    }

    /**
     * On btn kind weitere.
     *
     * @param event the event
     */
    @FXML
    void onBtnKindWeitere(ActionEvent event) {
        suchenAufrufenFolgeSuche();
        if (rcSuchenFolgeSucheFunktion == 0) {
            return;
        }

        /*Prüfen, ob schon zugeordnet*/
        String lAktionaersnummerNeu = rcSuchenFolgeSucheAktienregister.aktionaersnummer;

        lDbBundleOpen();
        int zuordenbar = pruefeObMitgliedZuordenbar(lAktionaersnummerNeu, lAktienregisterKind.aktionaersnummer, -999);
        if (zuordenbar < 1) {
            lDbBundleClose();
            CaZeigeHinweis caZeigeHinweis = new CaZeigeHinweis();
            switch (zuordenbar) {
            case -1:
                caZeigeHinweis.zeige(eigeneStage, "Mitglied ist bereits diesem Teilnehmer zugeordnet!");
                break;
            case -2:
                //bereits anderem Batch zugeordnet - Fehlermeldung wird in Prüfroutine angezeigt
                caZeigeHinweis.zeige(eigeneStage, fehlerMeldungAnderemBatchZugeordnet);
                break;
            }
            return;
        }

        zuordenbar = pruefeObMitgliedZuordenbarKind(lAktionaersnummerNeu, 4);
        lDbBundleClose();
        if (zuordenbar < 1) {
            CaZeigeHinweis caZeigeHinweis = new CaZeigeHinweis();
            switch (zuordenbar) {
            case -1:
                caZeigeHinweis.zeige(eigeneStage, "Mitglied ist bereits dem Kind zugeordnet!");
                break;
            case -2:
                //bereits anderem Batch zugeordnet - Fehlermeldung wird in Prüfroutine angezeigt
                caZeigeHinweis.zeige(eigeneStage, fehlerMeldungAnderemBatchZugeordnet);
                break;
            }
            return;
        }

        EclZugeordneteAktionaerePraesenz lZugeordneteAktionaerePraesenz = addListeDerVertretenenKind(
                rcSuchenFolgeSucheAktienregister, 0);
        lZugeordneteAktionaerePraesenz.geerbtVonZugeordnetemAktionaer = lAktienregisterKind.aktionaersnummer;
        lZugeordneteAktionaerePraesenz.zuordnungsArt = 4;

        zuordnenWeitereVollmachtZuKindAnzeigen();
    }

    /**
     * On btn zuordnung loeschen kind.
     *
     * @param event the event
     */
    void onBtnZuordnungLoeschenKind(ActionEvent event) {
        CaBug.druckeLog("", logDrucken, 10);
        int zuordnungZumLoeschen = -1;
        for (int i = 0; i < listeDerButtonsZuVertretenenKind.size(); i++) {
            if (event.getSource() == listeDerButtonsZuVertretenenKind.get(i)) {
                zuordnungZumLoeschen = i;
            }
        }
        if (zuordnungZumLoeschen == -1) {
            CaBug.druckeLog("Button nicht gefunden", logDrucken, 10);
            return;
        }

        if (listeDerVertretenenKind.get(zuordnungZumLoeschen).zuordnungIstAktiv == 1) {
            /*Dann einfach löschen*/
            listeDerVertretenenKind.get(zuordnungZumLoeschen).zuordnungIstAktiv = 0;
            zuordnenWeitereVollmachtZuKindAnzeigen();
            return;

        }

        /*Neu aktivieren*/
        lDbBundleOpen();
        int zuordenbar = pruefeObMitgliedZuordenbar(
                listeDerVertretenenKind.get(zuordnungZumLoeschen).zugeordneterAktionaer,
                lAktienregisterKind.aktionaersnummer, -999);
        if (zuordenbar < 1) {
            CaZeigeHinweis caZeigeHinweis = new CaZeigeHinweis();
            switch (zuordenbar) {
            case -1:
                caZeigeHinweis.zeige(eigeneStage,
                        "Mitglied ist bereits diesem Teilnehmer aktiv zugeordnet, deshalb ist dieser Eintrag nicht aktivierbar!");
                break;
            case -2:
                //bereits anderem Batch zugeordnet - Fehlermeldung wird in Prüfroutine angezeigt
                caZeigeHinweis.zeige(eigeneStage, fehlerMeldungAnderemBatchZugeordnet);
                break;
            }
            lDbBundleClose();
            return;
        }

        zuordenbar = pruefeObMitgliedZuordenbarKind(
                listeDerVertretenenKind.get(zuordnungZumLoeschen).zugeordneterAktionaer, -999);
        lDbBundleClose();
        if (zuordenbar < 1) {
            CaZeigeHinweis caZeigeHinweis = new CaZeigeHinweis();
            switch (zuordenbar) {
            case -1:
                caZeigeHinweis.zeige(eigeneStage, "Mitglied ist bereits dem Kind zugeordnet und aktiv!");
                break;
            case -2:
                //bereits anderem Batch zugeordnet - Fehlermeldung wird in Prüfroutine angezeigt
                caZeigeHinweis.zeige(eigeneStage, fehlerMeldungAnderemBatchZugeordnet);
                break;
            }
            return;
        }

        listeDerVertretenenKind.get(zuordnungZumLoeschen).zuordnungIstAktiv = 1;
        zuordnenWeitereVollmachtZuKindAnzeigen();
    }

    /**
     * On btn zuordnung loeschen.
     *
     * @param event the event
     */
    /*+++++++++++++++++Zuordnung löschen+++++++++++++++++++++++++++++++++*/
    void onBtnZuordnungLoeschen(ActionEvent event) {
        CaBug.druckeLog("", logDrucken, 10);
        int zuordnungZumLoeschen = -1;
        for (int i = 0; i < listeDerButtonsZuVertretenen.size(); i++) {
            if (event.getSource() == listeDerButtonsZuVertretenen.get(i)) {
                zuordnungZumLoeschen = i;
            }
        }
        if (zuordnungZumLoeschen == -1) {
            CaBug.druckeLog("Button nicht gefunden", logDrucken, 10);
            return;
        }

        if (listeDerVertretenen.get(zuordnungZumLoeschen).zuordnungIstAktiv == 1) {
            /*Dann einfach löschen*/
            listeDerVertretenen.get(zuordnungZumLoeschen).zuordnungIstAktiv = 0;
            erstAnzeigeTeilnehmer();
            return;

        }

        /*Neu aktivieren*/
        lDbBundleOpen();
        int zuordenbar = pruefeObMitgliedZuordenbar(listeDerVertretenen.get(zuordnungZumLoeschen).zugeordneterAktionaer,
                "", -999);
        lDbBundleClose();
        if (zuordenbar < 1) {
            CaZeigeHinweis caZeigeHinweis = new CaZeigeHinweis();
            switch (zuordenbar) {
            case -1:
                caZeigeHinweis.zeige(eigeneStage,
                        "Mitglied ist bereits diesem Teilnehmer aktiv zugeordnet, deshalb ist dieser Eintrag nicht aktivierbar!");
                break;
            case -2:
                //bereits anderem Batch zugeordnet - Fehlermeldung wird in Prüfroutine angezeigt
                caZeigeHinweis.zeige(eigeneStage, fehlerMeldungAnderemBatchZugeordnet);
                break;
            }
            return;
        }

        listeDerVertretenen.get(zuordnungZumLoeschen).zuordnungIstAktiv = 1;
        erstAnzeigeTeilnehmer();
    }

    /**
     * On btn zuordnung begleitperson loeschen.
     *
     * @param event the event
     */
    void onBtnZuordnungBegleitpersonLoeschen(ActionEvent event) {
        CaBug.druckeLog("", logDrucken, 10);
        int zuordnungZumLoeschen = -1;
        for (int i = 0; i < listeDerButtonsZuBegleitpersonen.size(); i++) {
            if (event.getSource() == listeDerButtonsZuBegleitpersonen.get(i)) {
                zuordnungZumLoeschen = i;
            }
        }
        if (zuordnungZumLoeschen == -1) {
            CaBug.druckeLog("Button nicht gefunden", logDrucken, 10);
            return;
        }

        if (listeDerBegleitpersonen.get(zuordnungZumLoeschen).zuordnungIstAktiv == 1) {
            /*Dann einfach löschen*/
            listeDerBegleitpersonen.get(zuordnungZumLoeschen).zuordnungIstAktiv = 0;
            erstAnzeigeTeilnehmer();
            return;

        }

        listeDerBegleitpersonen.get(zuordnungZumLoeschen).zuordnungIstAktiv = 1;
        erstAnzeigeTeilnehmer();
    }

    /** +++++++Subfunktionen zum Dialogablauf++++++++++++++++++. */
    /** Anzeige des Start-Tabs / Ausgangsbasis des Serivce-Desk-Tools */

    private final int NR_TAB_START = 0;

    /** The nr tab selbst. */
    private final int NR_TAB_SELBST = 1;

    /** The nr tab eingabe neuer vertreter. */
    private final int NR_TAB_EINGABE_NEUER_VERTRETER = 2;

    /** The nr tab eingabe gbr. */
    private final int NR_TAB_EINGABE_GBR = 3;

    /** The nr tab teilnahme. */
    private final int NR_TAB_TEILNAHME = 4;

    /** The nr tab weitere vertretung. */
    private final int NR_TAB_WEITERE_VERTRETUNG = 5;

    /** The nr tab neue begleitperson. */
    private final int NR_TAB_NEUE_BEGLEITPERSON = 6;

    /** The nr tab kind. */
    private final int NR_TAB_KIND = 7;

    /**
     * Disable all tabs.
     */
    private void disableAllTabs() {
        tEingabeNeuerVertreter.setDisable(true);
        tVertretung.setDisable(true);
        tStart.setDisable(true);
        tWeitereVertretung.setDisable(true);
        tTeilnahme.setDisable(true);
        tEingabeNeueBegleitperson.setDisable(true);
        tEingabeGbR.setDisable(true);
        tKind.setDisable(true);
    }

    //    private void enableGlobalButton() {
    //        btnNaechsterVorgang.setDisable(false);
    //        btnNaechsterVorgang.setVisible(true);
    //        
    //        btnAbbrechenGlobal.setDisable(false);
    //        btnAbbrechenGlobal.setVisible(true);
    //        
    //        btnWeiterGlobal.setDisable(true);
    //        btnWeiterGlobal.setVisible(false);
    //    }

    /**
     * Enable global button nur abbruch.
     */
    private void enableGlobalButtonNurAbbruch() {
        btnNaechsterVorgang.setDisable(true);
        btnNaechsterVorgang.setVisible(false);

        btnAbbrechenGlobal.setDisable(false);
        btnAbbrechenGlobal.setVisible(true);

        btnWeiterGlobal.setDisable(true);
        btnWeiterGlobal.setVisible(false);

        btnBuchenGlobal.setDisable(true);
        btnBuchenGlobal.setVisible(false);
    }

    /**
     * Disable global button.
     */
    private void disableGlobalButton() {
        btnNaechsterVorgang.setDisable(true);
        btnNaechsterVorgang.setVisible(false);

        btnAbbrechenGlobal.setDisable(true);
        btnAbbrechenGlobal.setVisible(false);

        btnWeiterGlobal.setDisable(true);
        btnWeiterGlobal.setVisible(false);
        
        btnBuchenGlobal.setDisable(true);
        btnBuchenGlobal.setVisible(false);

    }

    /**
     * Show start tab.
     */
    private void showStartTab() {
        disableAllTabs();
        tStart.setDisable(false);
        tpAblauf.getSelectionModel().select(NR_TAB_START);
        disableGlobalButton();
        /*Verwendet für Neue Gastkarte für Event*/
//        btnWeiterGlobal.setDisable(false);
//        btnWeiterGlobal.setVisible(true);
      btnWeiterGlobal.setDisable(true);
      btnWeiterGlobal.setVisible(false);

        btnBuchenGlobal.setDisable(false);
        btnBuchenGlobal.setVisible(true);

    }

    /**
     * Show gb R tab.
     */
    private void showGbRTab() {
        disableAllTabs();
        tEingabeGbR.setDisable(false);
        tpAblauf.getSelectionModel().select(NR_TAB_EINGABE_GBR);
        enableGlobalButtonNurAbbruch();
        aktuelleFunktion = 0;
    }

    /**
     * Show neuer vertreter tab.
     */
    private void showNeuerVertreterTab() {
        disableAllTabs();
        tEingabeNeuerVertreter.setDisable(false);
        tpAblauf.getSelectionModel().select(NR_TAB_EINGABE_NEUER_VERTRETER);
        disableGlobalButton();
    }

    /**
     * Show vertretung tab.
     */
    private void showVertretungTab() {
        disableAllTabs();
        tVertretung.setDisable(false);
        tpAblauf.getSelectionModel().select(NR_TAB_SELBST);
        disableGlobalButton();
    }

    /**
     * Show teilnehmer tab.
     */
    private void showTeilnehmerTab() {
        disableAllTabs();
        tTeilnahme.setDisable(false);
        tpAblauf.getSelectionModel().select(NR_TAB_TEILNAHME);
        disableGlobalButton();
    }

    /**
     * Show weitere vertretung tab.
     */
    private void showWeitereVertretungTab() {
        disableAllTabs();
        tWeitereVertretung.setDisable(false);
        tpAblauf.getSelectionModel().select(NR_TAB_WEITERE_VERTRETUNG);
        disableGlobalButton();
    }

    /**
     * Show neue begleitperson tab.
     */
    private void showNeueBegleitpersonTab() {
        disableAllTabs();
        tEingabeNeueBegleitperson.setDisable(false);
        tpAblauf.getSelectionModel().select(NR_TAB_NEUE_BEGLEITPERSON);
        disableGlobalButton();
    }

    /**
     * Show kind tab.
     */
    private void showKindTab() {
        disableAllTabs();
        tKind.setDisable(false);
        tpAblauf.getSelectionModel().select(NR_TAB_KIND);
        disableGlobalButton();

    }

    /** The rc suchen erst suche aktienregister. */
    private EclAktienregister rcSuchenErstSucheAktienregister = null;

    /** The rc suchen erst suche meldungen. */
    private EclMeldung rcSuchenErstSucheMeldungen = null;

    /** The rc suchen erst suche sonstige vollmacht. */
    private String rcSuchenErstSucheSonstigeVollmacht = null;

    /** 1=ausgewählten Datensatz weiterverarbeiten 2=neuen Vertreter anlegen. */
    private int rcSuchenErstSucheFunktion = 0;

    /**
     * Suchen aufrufen erst suche.
     */
    private void suchenAufrufenErstSuche() {
        rcSuchenErstSucheFunktion = 0;

        Stage newStage = new Stage();
        CaIcon.serviceDesk(newStage);
        CtrlSuchen controllerFenster = new CtrlSuchen();
        controllerFenster.init(newStage);

        controllerFenster.funktionsButton1 = "Verarbeiten";

        /** Button neuer Vertreter an dieser Stelle stillgelegt, da sinnlos */
        //        controllerFenster.funktionsButton3 = "Neuen Vertreter anlegen";
        //        controllerFenster.funktionsButton3OhneAuswahlZulaessig = true;

        controllerFenster.mehrfachAuswahlZulaessig = false;

        //        controllerFenster.funktionsButton3 = "Neue Gastkarte";

        controllerFenster.durchsuchenSammelkarten = false;
        controllerFenster.durchsuchenInSammelkarten = true;
        controllerFenster.durchsuchenGaeste = false;
        controllerFenster.durchsuchenNurGaeste = false;

        controllerFenster.suchenNachInterneIdent = false;

        controllerFenster.aktienregisterNummerAufbereiten = true;

        CaController caController = new CaController();
        caController.open(newStage, controllerFenster, "/de/meetingapps/meetingclient/meetingClientDialoge/Suchen.fxml",
                1500, 760, "Suchen", true);

        if (controllerFenster.rcFortsetzen) {

            rcSuchenErstSucheFunktion = 0;

            int rcFunktion = controllerFenster.rcFunktion;

            if (rcFunktion == 1) {
                rcSuchenErstSucheFunktion = 1;
                rcSuchenErstSucheAktienregister = controllerFenster.rcAktienregister[0];
                rcSuchenErstSucheMeldungen = controllerFenster.rcMeldungen[0];
                rcSuchenErstSucheSonstigeVollmacht = controllerFenster.rcSonstigeVollmacht[0];
                return;
            }
            if (rcFunktion == 3) {
                neuerVertreterIstGesetzlich = -1;
                rcSuchenErstSucheFunktion = 2;
                return;
            }

            return;

        }

    }

    /** The rc suchen folge suche aktienregister. */
    private EclAktienregister rcSuchenFolgeSucheAktienregister = null;

    /** The rc suchen folge suche meldungen. */
    private EclMeldung rcSuchenFolgeSucheMeldungen = null;

    /** The rc suchen folge suche sonstige vollmacht. */
    private String rcSuchenFolgeSucheSonstigeVollmacht = null;

    /** 1=ausgewählten Datensatz weiterverarbeiten 2=neuen Vertreter anlegen. */
    private int rcSuchenFolgeSucheFunktion = 0;

    /**
     * Suchen aufrufen folge suche.
     */
    private void suchenAufrufenFolgeSuche() {
        rcSuchenFolgeSucheFunktion = 0;

        Stage newStage = new Stage();
        CaIcon.serviceDesk(newStage);
        CtrlSuchen controllerFenster = new CtrlSuchen();
        controllerFenster.init(newStage);

        controllerFenster.funktionsButton1 = "Verarbeiten";

        controllerFenster.mehrfachAuswahlZulaessig = false;

        //        controllerFenster.funktionsButton3 = "Neue Gastkarte";

        controllerFenster.durchsuchenSammelkarten = false;
        controllerFenster.durchsuchenInSammelkarten = true;
        controllerFenster.durchsuchenGaeste = false;
        controllerFenster.durchsuchenNurGaeste = false;

        controllerFenster.suchenNachInterneIdent = false;

        controllerFenster.aktienregisterNummerAufbereiten = true;

        CaController caController = new CaController();
        caController.open(newStage, controllerFenster, "/de/meetingapps/meetingclient/meetingClientDialoge/Suchen.fxml",
                1500, 760, "Suchen", true);

        if (controllerFenster.rcFortsetzen) {

            rcSuchenFolgeSucheFunktion = controllerFenster.rcFunktion;
            rcSuchenFolgeSucheAktienregister = controllerFenster.rcAktienregister[0];
            rcSuchenFolgeSucheMeldungen = controllerFenster.rcMeldungen[0];
            rcSuchenFolgeSucheSonstigeVollmacht = controllerFenster.rcSonstigeVollmacht[0];

            return;
        }

    }

    /**
     * ************Verwaltung der listeDerVertretenen**********************.
     */
    private void initListeDerVertretenen() {
        listeDerVertretenenGesperrt = false;
        listeDerVertretenen = new LinkedList<EclZugeordneteAktionaerePraesenz>();
        listeDerButtonsZuVertretenen = new LinkedList<Button>();
        listeDerWeitereButtonsZuVertretenen = new LinkedList<Button>();
    }

    /**
     * Liefert neu eingefügten Satz zurück, um diesen ggf. noch zu ergänzen
     * pVollmachtsart=0 Bevollmächtigter, 1=Gesetzlicher, etc.
     *
     * @param pAktienregister the aktienregister
     * @param pVollmachtsart  the vollmachtsart
     * @return the ecl zugeordnete aktionaere praesenz
     */
    private EclZugeordneteAktionaerePraesenz addListeDerVertretenen(EclAktienregister pAktienregister,
            int pVollmachtsart) {
        EclZugeordneteAktionaerePraesenz lZugeordneteAktionaerePraesenz = new EclZugeordneteAktionaerePraesenz();
        lZugeordneteAktionaerePraesenz.eclAktienregister = pAktienregister;
        lZugeordneteAktionaerePraesenz.zuordnungsArt = pVollmachtsart;
        lZugeordneteAktionaerePraesenz.zuordnungIstAktiv = 1;
        lZugeordneteAktionaerePraesenz.zugeordneterAktionaer = pAktienregister.aktionaersnummer;
        addListeDerVertretenen(lZugeordneteAktionaerePraesenz);

        DbBundle lDbBundle=new DbBundle();
        lDbBundle.openAll();
        lDbBundle.openWeitere();
        
        lDbBundle.dbVeranstaltungenWert.readLoginKennung(pAktienregister.aktionaersnummer, -1);
        int anz=lDbBundle.dbVeranstaltungenWert.anzErgebnis();
        for (int i=0;i<anz;i++) {
            EclVeranstaltungenWert lVeranstaltungenWert=lDbBundle.dbVeranstaltungenWert.ergebnisPosition(i);
            if (lVeranstaltungenWert.identElement==22 && lVeranstaltungenWert.identAktion==0) {
                int anmeldungen=0;
                if (CaString.isNummern(lVeranstaltungenWert.eingabeWert)) {
                    anmeldungen=Integer.parseInt(lVeranstaltungenWert.eingabeWert); 
                }
                lZugeordneteAktionaerePraesenz.anmeldungenEmpfang=anmeldungen;
            }
        }
        lDbBundle.closeAll();
        
        
        return lZugeordneteAktionaerePraesenz;
    }

    /**
     * Adds the liste der vertretenen.
     *
     * @param pZugeordneteAktionaerePraesenz the zugeordnete aktionaere praesenz
     */
    private void addListeDerVertretenen(EclZugeordneteAktionaerePraesenz pZugeordneteAktionaerePraesenz) {
        listeDerVertretenen.add(pZugeordneteAktionaerePraesenz);

        Button lButton = new Button();
        lButton.setText("Zuordnung löschen");
        lButton.setOnAction(e -> {
            onBtnZuordnungLoeschen(e);
        });
        listeDerButtonsZuVertretenen.add(lButton);

        if (pZugeordneteAktionaerePraesenz.eclAktienregister.getGruppe() == KonstGruppen.minderjaehrigesEinzelmitglied
                && pZugeordneteAktionaerePraesenz.zuordnungsArt == 1) {
            Button lButtonVollmacht = new Button();
            lButtonVollmacht.setText("Hinterlegen Vollmacht");
            lButtonVollmacht.setOnAction(e -> {
                onBtnHinterlegenUntervollmacht(e);
            });
            listeDerWeitereButtonsZuVertretenen.add(lButtonVollmacht);
        } else {
            listeDerWeitereButtonsZuVertretenen.add(null);
        }

    }

    /**
     * ************Verwaltung der listeDerVertretenen Kind**********************.
     */
    private void initListeDerVertretenenKind() {
        listeDerVertretenenKind = new LinkedList<EclZugeordneteAktionaerePraesenz>();
        listeDerButtonsZuVertretenenKind = new LinkedList<Button>();
    }

    /**
     * Liefert neu eingefügten Satz zurück, um diesen ggf. noch zu ergänzen
     * pVollmachtsart=0 Bevollmächtigter, 1=Gesetzlicher, etc.
     *
     * @param pAktienregister the aktienregister
     * @param pVollmachtsart  the vollmachtsart
     * @return the ecl zugeordnete aktionaere praesenz
     */
    private EclZugeordneteAktionaerePraesenz addListeDerVertretenenKind(EclAktienregister pAktienregister,
            int pVollmachtsart) {
        EclZugeordneteAktionaerePraesenz lZugeordneteAktionaerePraesenz = new EclZugeordneteAktionaerePraesenz();
        lZugeordneteAktionaerePraesenz.eclAktienregister = pAktienregister;
        lZugeordneteAktionaerePraesenz.zuordnungsArt = pVollmachtsart;
        lZugeordneteAktionaerePraesenz.zuordnungIstAktiv = 1;
        lZugeordneteAktionaerePraesenz.zugeordneterAktionaer = pAktienregister.aktionaersnummer;

        addListeDerVertretenenKind(lZugeordneteAktionaerePraesenz);

        return lZugeordneteAktionaerePraesenz;
    }

    /**
     * Adds the liste der vertretenen kind.
     *
     * @param pZugeordneteAktionaerePraesenz the zugeordnete aktionaere praesenz
     */
    private void addListeDerVertretenenKind(EclZugeordneteAktionaerePraesenz pZugeordneteAktionaerePraesenz) {
        listeDerVertretenenKind.add(pZugeordneteAktionaerePraesenz);

        Button lButton = new Button();
        lButton.setText("Zuordnung löschen");
        lButton.setOnAction(e -> {
            onBtnZuordnungLoeschenKind(e);
        });
        listeDerButtonsZuVertretenenKind.add(lButton);

        return;
    }

    /**
     * ************Verwaltung der listeDerBegleitpersonen**********************.
     */
    private void initListeDerBegleitpersonen() {
        listeDerBegleitpersonen = new LinkedList<EclZugeordneteAktionaerePraesenz>();
        listeDerButtonsZuBegleitpersonen = new LinkedList<Button>();
    }

    /**
     * Liefert neu eingefügten Satz zurück, um diesen ggf. noch zu ergänzen
     * pVollmachtsart=0 Bevollmächtigter, 1=Gesetzlicher, etc.
     *
     * @param pPersonenNatJur the personen nat jur
     * @return the ecl zugeordnete aktionaere praesenz
     */
    private EclZugeordneteAktionaerePraesenz addListeDerBegleitpersonen(EclPersonenNatJur pPersonenNatJur) {
        EclZugeordneteAktionaerePraesenz lZugeordneteAktionaerePraesenz = new EclZugeordneteAktionaerePraesenz();
        lZugeordneteAktionaerePraesenz.eclPersonenNatJur = pPersonenNatJur;
        lZugeordneteAktionaerePraesenz.zuordnungsArt = 9999;
        lZugeordneteAktionaerePraesenz.zuordnungIstAktiv = 1;

        addListeDerBegleitpersonen(lZugeordneteAktionaerePraesenz);

        return lZugeordneteAktionaerePraesenz;
    }

    /**
     * Adds the liste der begleitpersonen.
     *
     * @param pZugeordneteAktionaerePraesenz the zugeordnete aktionaere praesenz
     */
    private void addListeDerBegleitpersonen(EclZugeordneteAktionaerePraesenz pZugeordneteAktionaerePraesenz) {
        listeDerBegleitpersonen.add(pZugeordneteAktionaerePraesenz);
        Button lButton = new Button();
        lButton.setText("Zuordnung löschen");
        lButton.setOnAction(e -> {
            onBtnZuordnungBegleitpersonLoeschen(e);
        });
        listeDerButtonsZuBegleitpersonen.add(lButton);

    }

    /**
     * *******************Business-FUnktionen***********************.
     *
     * pAktienregisterIdent: es werden alle Vollmachten zugeordnet, die aktiv sind
     * und an diese pAktienregisterIdent Vollmacht gegeben haben
     * 
     * pArtVollmacht=0 (Bevollmächtigter) oder 1 (Gesetzlicher Vertreter) - die
     * Vollmachtsart, die aktuell gegeben wurde. =-1 => Selbst
     * 
     * Rückgabewert: holeVollmachtenFehler nicht leer => Diese Untervollmachten sind
     * bereits jemandem zugeordnet
     * 
     * @param pAktienregister the aktienregister
     * @param pArtVollmacht   the art vollmacht
     */
    private void holeVollmachtenEinesZugeordnetenMitglieds(EclAktienregister pAktienregister, int pArtVollmacht) {
        holeVollmachtenFehler = "";

        if ((pAktienregister.getGruppe() != KonstGruppen.minderjaehrigesEinzelmitglied
                ||
                pArtVollmacht != 1)
                && pArtVollmacht != -1) {
            return;
        }
        lDbBundle = new DbBundle();
        lDbBundle.openAll();
        lDbBundle.openWeitere();

        holeVollmachtenEinesZugeordnetenMitglieds_Rek(pAktienregister, pArtVollmacht);
        lDbBundle.closeAll();
    }

    /** The hole vollmachten fehler. */
    private String holeVollmachtenFehler = "";

    /**
     * Hole vollmachten eines zugeordneten mitglieds rek.
     *
     * @param pAktienregister the aktienregister
     * @param pArtVollmacht   the art vollmacht
     */
    private void holeVollmachtenEinesZugeordnetenMitglieds_Rek(EclAktienregister pAktienregister, int pArtVollmacht) {

        int pAktienregisterIdent = pAktienregister.aktienregisterIdent;
        lDbBundle.dbVorlaeufigeVollmacht.readGegebenAnAktionaer(pAktienregisterIdent);
        EclVorlaeufigeVollmacht[] unterVollmachten = lDbBundle.dbVorlaeufigeVollmacht.ergebnis();
        int anz = lDbBundle.dbVorlaeufigeVollmacht.anzErgebnis();
        if (anz > 0) {
            for (int i = 0; i < anz; i++) {
                EclVorlaeufigeVollmacht lVorlaeufigeVollmacht = unterVollmachten[i];
                if (lVorlaeufigeVollmacht.pruefstatus == 2 && lVorlaeufigeVollmacht.storniert == 0) {
                    int lAktienregisterIdentWeitereVollmacht = lVorlaeufigeVollmacht.erteiltVonIdent;
                    lDbBundle.dbAktienregister.leseZuAktienregisterIdent(lAktienregisterIdentWeitereVollmacht);
                    EclAktienregister lAktienregister = lDbBundle.dbAktienregister.ergebnisPosition(0);

                    int rc = pruefeObMitgliedZuordenbar(lAktienregister.aktionaersnummer, "", 999);
                    if (rc != 1) {
                        if (holeVollmachtenFehler.isEmpty() == false) {
                            holeVollmachtenFehler += "; ";
                        }
                        holeVollmachtenFehler += aufbereiteteNummer(lAktienregister.aktionaersnummer);
                    }

                    else {
                        int lNeueVollmachtsart = 0;
                        switch (pArtVollmacht) {
                        case -1:
                            /*Selbst*/
                            if (lVorlaeufigeVollmacht.bevollmaechtigterAusgefuehrtIstGesetzlich == 0) {
                                lNeueVollmachtsart = 0;
                            } else {
                                lNeueVollmachtsart = 1;
                            }
                            break;
                        case 1:
                        case 3:
                        case 5:
                            /*Gesetzliche Vollmacht*/
                            if (lVorlaeufigeVollmacht.bevollmaechtigterAusgefuehrtIstGesetzlich == 0) {
                                lNeueVollmachtsart = 4;
                            } else {
                                lNeueVollmachtsart = 5;
                            }
                            break;
                        case 0:
                        case 2:
                        case 4:
                            /*Normale Vollmacht*/
                            if (lVorlaeufigeVollmacht.bevollmaechtigterAusgefuehrtIstGesetzlich == 0) {
                                lNeueVollmachtsart = 2;
                            } else {
                                lNeueVollmachtsart = 3;
                            }
                            break;
                        }
                        EclZugeordneteAktionaerePraesenz lZugeordneteAktionaerePraesenz = addListeDerVertretenen(
                                lAktienregister, lNeueVollmachtsart);
                        lZugeordneteAktionaerePraesenz.geerbtVonZugeordnetemAktionaer = pAktienregister.aktionaersnummer;
                        holeVollmachtenEinesZugeordnetenMitglieds_Rek(lAktienregister, lNeueVollmachtsart);
                    }
                }
            }
        }
    }

    /** The fehler meldung anderem batch zugeordnet. */
    private String fehlerMeldungAnderemBatchZugeordnet = "";

    /*+++++++++Prüfen, ob Mitglied zuordenbar+++++++++++++++++++++++++++*/
    /**
     * pAusschliessen!="" => wenn eine Vollmacht über diese Mitgliedsnummer
     * zugeordnet ist, wird sie für die Prüfung nicht berücksichtigt (für Prüfung
     * bei Kinder-Untervollmachten ist hier das Kind eingetragen)
     * 
     * pZuordnungsart==-999 => wird nicht überprüft
     * 
     * 
     * 1 => zuordenbar -1 => bereits diesem Teilnehmer zugeordnet -2 => bereits
     * anderem Teilnehmer zugeordnet - Fehlermeldung
     * fehlerMeldungAnderemBatchZugeordnet ist gefüllt!
     * 
     * lDbBundle muß geöffnet sein.
     *
     * @param pAktionaersnummer the aktionaersnummer
     * @param pAusschliessen    the ausschliessen
     * @param pZuordnungsart    the zuordnungsart
     * @return the int
     */
    private int pruefeObMitgliedZuordenbar(String pAktionaersnummer, String pAusschliessen, int pZuordnungsart) {
        CaBug.druckeLog("pAktionaersnummer=" + pAktionaersnummer, logDrucken, 10);
        CaBug.druckeLog("pAusschliessen=" + pAusschliessen, logDrucken, 10);
        for (int i = 0; i < listeDerVertretenen.size(); i++) {
            if (listeDerVertretenen.get(i).eclAktienregister.aktionaersnummer.equals(pAktionaersnummer)
                    &&
                    /**
                     * Wenn Aktiv, dann nicht zuordenbar. Wenn Deaktiv, dann nicht mit gleicher
                     * Zuordnungsart zuordenbar
                     */
                    (listeDerVertretenen.get(i).zuordnungIstAktiv == 1
                            ||
                            (listeDerVertretenen.get(i).zuordnungsArt == pZuordnungsart && pZuordnungsart != -999))
                    &&
                    (pAusschliessen.isEmpty() || pAusschliessen
                            .equals(listeDerVertretenen.get(i).geerbtVonZugeordnetemAktionaer) == false)) {
                CaBug.druckeLog("Return -1", logDrucken, 10);
                return -1;
            }
        }

        /*Abprüfen, ob einem anderen Batch zugeordnet*/
        boolean bRc = pruefeObMitgliedBereitsAnderemTeilnehmerZugeordnet(pAktionaersnummer);
        if (bRc == true) {
            return -2;
        }

        CaBug.druckeLog("Return 1", logDrucken, 10);
        return 1;
    }

    /**
     * Wird nur die Kinder-Liste überprüft. D.h. vorher pruefeObMitgliedZuordenbar
     * überprüfen pZuordnungsart=-999 => wird ignoriert. Ansonsten nur 4 zulässig
     * 
     * lDbBundle muß geöffnet sein
     *
     * @param pAktionaersnummer the aktionaersnummer
     * @param pZuordnungsart    the zuordnungsart
     * @return the int
     */
    private int pruefeObMitgliedZuordenbarKind(String pAktionaersnummer, int pZuordnungsart) {
        for (int i = 0; i < listeDerVertretenenKind.size(); i++) {
            if (listeDerVertretenenKind.get(i).eclAktienregister.aktionaersnummer.equals(pAktionaersnummer)
                    &&
                    /**
                     * Wenn Aktiv, dann nicht zuordenbar. Wenn Deaktiv, dann nicht mit gleicher
                     * Zuordnungsart zuordenbar
                     */
                    (listeDerVertretenenKind.get(i).zuordnungIstAktiv == 1
                            ||
                            (listeDerVertretenenKind.get(i).zuordnungsArt == pZuordnungsart
                                    && pZuordnungsart != -999))) {
                return -1;
            }
        }

        /*Abprüfen, ob einem anderen Batch zugeordnet*/
        boolean bRc = pruefeObMitgliedBereitsAnderemTeilnehmerZugeordnet(pAktionaersnummer);
        if (bRc == true) {
            return -2;
        }

        return 1;
    }

    /**
     * lDbBundle muß offen sein.
     * 
     * false = noch nicht zugeordnet
     * 
     * Fehlermeldung fehlerMeldungAnderemBatchZugeordnet wird in dieser Routine
     * gefüllt!
     *
     * @param pAktienregister the aktienregister
     * @return true, if successful
     */
    private boolean pruefeObMitgliedBereitsAnderemTeilnehmerZugeordnet(EclAktienregister pAktienregister) {
        String lAktienregisterNummer = pAktienregister.aktionaersnummer;

        int anzZugeordnet = lDbBundle.dbZugeordneteAktionaerePraesenz
                .readAlleAktivenVertreterEinesAktionaers(lAktienregisterNummer);
        if (anzZugeordnet < 0) {
            return false;
        }
        for (int i = 0; i < anzZugeordnet; i++) {
            if (vertreterId.isEmpty() ||
                    lDbBundle.dbZugeordneteAktionaerePraesenz.ergebnisPosition(i).anwesendePersonLoginIdent
                            .equals(vertreterId) == false) {
                EclZugeordneteAktionaerePraesenz lZugeordneteAktionaerePraesenz = lDbBundle.dbZugeordneteAktionaerePraesenz
                        .ergebnisPosition(i);
                fehlerMeldungAnderemBatchZugeordnet = "Mitglied " + aufbereiteteNummer(lAktienregisterNummer) + " "
                        + pAktienregister.vorname + " " +
                        pAktienregister.nachname + " ist bereits dem Teilnehmer "
                        + aufbereiteteNummer(lZugeordneteAktionaerePraesenz.anwesendePersonLoginIdent) + " zugeordnet";
                return true;

            }
        }
        return false;

    }

    /**
     * Pruefe ob mitglied bereits anderem teilnehmer zugeordnet.
     *
     * @param pAktionaersnummer the aktionaersnummer
     * @return true, if successful
     */
    private boolean pruefeObMitgliedBereitsAnderemTeilnehmerZugeordnet(String pAktionaersnummer) {
        lDbBundle.dbAktienregister.leseZuAktienregisternummer(pAktionaersnummer);
        EclAktienregister lAktienregister = lDbBundle.dbAktienregister.ergebnisPosition(0);
        return pruefeObMitgliedBereitsAnderemTeilnehmerZugeordnet(lAktienregister);
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
     * DbBundle muß offen sein. Falls pVertreterId=="", dann prüft ob irgendwem
     * aktiv zugeordnet. Wenn pVertreterId!="", dann prüft ob es irgendjemand
     * anderem zugeordnet ist
     */
    String zugeordnetZu = "";

    /**
     * Pruefe ob mitglied schon zugeordnet.
     *
     * @param pMitgliedsnr the mitgliedsnr
     * @param pVertreterId the vertreter id
     * @return true, if successful
     */
    private boolean pruefeObMitgliedSchonZugeordnet(String pMitgliedsnr, String pVertreterId) {
        zugeordnetZu = "";
        int rc = lDbBundle.dbZugeordneteAktionaerePraesenz.readAlleAktivenVertreterEinesAktionaers(pMitgliedsnr);
        if (rc < 1) {
            return false;
        }
        for (int i = 0; i < rc; i++) {
            if (pVertreterId.isEmpty() ||
                    lDbBundle.dbZugeordneteAktionaerePraesenz.ergebnisPosition(i).anwesendePersonLoginIdent
                            .equals(pVertreterId) == false) {
                zugeordnetZu = lDbBundle.dbZugeordneteAktionaerePraesenz.ergebnisPosition(i).anwesendePersonLoginIdent;
                return true;
            }
        }

        return false;
    }

    /*+++++++++++++++++++++++++++Routinen zum Einlesen einzelner Meldungen++++++++++++++++++++*/

    /**
     * Kennung kann S-Nr. oder Aktionärsnummer sein. Liefert die zugehörige Meldung
     * zurück. lDbBundle muß geöffnet sein
     *
     * @param pKennung the kennung
     * @return the ecl meldung
     */
    private EclMeldung leseMeldungZuKennung(String pKennung) {
        EclMeldung lMeldungRc = null;
        if (pKennung.startsWith("S")) {
            lDbBundle.dbLoginDaten.read_loginKennung(pKennung);
            lDbBundle.dbMeldungen.leseZuMeldungsIdent(lDbBundle.dbLoginDaten.ergebnisPosition(0).meldeIdent);
        } else {
            EclMeldung lMeldungFuerSuche = new EclMeldung();
            lMeldungFuerSuche.aktionaersnummer = pKennung;
            lDbBundle.dbMeldungen.leseZuAktionaersnummer(lMeldungFuerSuche);
        }

        lMeldungRc = lDbBundle.dbMeldungen.meldungenArray[0];
        return lMeldungRc;
    }

    /**
     * L db bundle open.
     */
    /*+++++++++++++++++++++++++++Dateiverwaltung++++++++++++++++++++++++++++++++*/
    private void lDbBundleOpen() {
        lDbBundle = new DbBundle();
        lDbBundle.openAll();
        lDbBundle.openWeitere();
    }

    /**
     * L db bundle close.
     */
    private void lDbBundleClose() {
        lDbBundle.closeAll();
    }

    
    /*++++++++++++++++++++++++++Aufruf Front-Office-Modul+++++++++++++++++++++*/
    private void oeffneFOohneBuchung() {
        Stage neuerDialog = new Stage();
        de.meetingapps.meetingclient.meetingFrontOffice.CtrlHauptStage controllerFenster = new de.meetingapps.meetingclient.meetingFrontOffice.CtrlHauptStage();
        controllerFenster.init(neuerDialog);

        CaController caController = new CaController();
        caController.open(neuerDialog, controllerFenster,
                "/de/meetingapps/meetingclient/meetingFrontOffice/HauptStage.fxml", 1200, 600,
                "MeetingPortal FrontOffice", true);

    }

    private void oeffneFOmitBuchung(String pEingabeNummer) {
        Stage neuerDialog = new Stage();
        de.meetingapps.meetingclient.meetingFrontOffice.CtrlHauptStage controllerFenster = new de.meetingapps.meetingclient.meetingFrontOffice.CtrlHauptStage();
        controllerFenster.init(neuerDialog, pEingabeNummer);

        CaController caController = new CaController();
        caController.open(neuerDialog, controllerFenster,
                "/de/meetingapps/meetingclient/meetingFrontOffice/HauptStage.fxml", 1200, 600,
                "MeetingPortal FrontOffice", true);

    }

    
    /**
     * ********************Sonstiges************************************************.
     *
     * @param pNummer the nummer
     * @return the string
     */
    private String aufbereiteteNummer(String pNummer) {
        String hNummer = pNummer;
        if (pNummer.length() == 11) {
            hNummer = pNummer.substring(0, 10);
        }
        return hNummer;
    }

}
