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
package de.meetingapps.meetingclient.meetingFrontOffice;

/*Testfälle
 * EK 36 = Vertreter1 Aktionär 5
 * EK 37 = Vertreter2 Aktionär 5
 */

import java.io.IOException;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;

import de.meetingapps.meetingclient.meetingClient.BlClientPraesenzBuchen;
import de.meetingapps.meetingclient.meetingClient.BlParameterUeberWebService;
import de.meetingapps.meetingclient.meetingClientAllg.CALabelPrint;
import de.meetingapps.meetingclient.meetingClientDialoge.CaController;
import de.meetingapps.meetingclient.meetingClientDialoge.CaIcon;
import de.meetingapps.meetingclient.meetingClientDialoge.CaZeigeHinweis;
import de.meetingapps.meetingclient.meetingClientDialoge.CtrlRoot;
import de.meetingapps.meetingclient.meetingClientDialoge.CtrlSuchen;
import de.meetingapps.meetingclient.meetingClientOberflaechen.CaOpenWindow;
import de.meetingapps.meetingclient.meetingClientOberflaechen.LoadingScreen;
import de.meetingapps.meetingclient.meetingClientOberflaechen.ObjectActions;
import de.meetingapps.meetingportal.meetComAllg.CaBug;
import de.meetingapps.meetingportal.meetComAllg.CaDateiVerwaltung;
import de.meetingapps.meetingportal.meetComAllg.CaFehler;
import de.meetingapps.meetingportal.meetComBl.BlNummernformen;
import de.meetingapps.meetingportal.meetComBl.BlStimmkartendruck;
import de.meetingapps.meetingportal.meetComDb.DbBundle;
import de.meetingapps.meetingportal.meetComEntities.EclKIAVFuerVollmachtDritte;
import de.meetingapps.meetingportal.meetComEntities.EclLabelPrint;
import de.meetingapps.meetingportal.meetComEntities.EclMeldung;
import de.meetingapps.meetingportal.meetComEntities.EclWillensErklVollmachtenAnDritte;
import de.meetingapps.meetingportal.meetComEntities.EclZutrittsIdent;
import de.meetingapps.meetingportal.meetComHVParam.ParamS;
import de.meetingapps.meetingportal.meetComHVParam.ParamSpezial;
import de.meetingapps.meetingportal.meetComKonst.KonstAutoDrucker;
import de.meetingapps.meetingportal.meetComKonst.KonstBesitzart;
import de.meetingapps.meetingportal.meetComKonst.KonstEingabeQuelle;
import de.meetingapps.meetingportal.meetComKonst.KonstKartenart;
import de.meetingapps.meetingportal.meetComKonst.KonstKartenklasse;
import de.meetingapps.meetingportal.meetComKonst.KonstProgrammFunktionHV;
import de.meetingapps.meetingportal.meetComKonst.KonstSkIst;
import de.meetingapps.meetingportal.meetComKonst.KonstWillenserklaerung;
import de.meetingapps.meetingportal.meetComStub.CInjects;
import de.meetingapps.meetingportal.meetComStub.WSClient;
import de.meetingapps.meetingportal.meetComWE.WELoginVerify;
import de.meetingapps.meetingportal.meetComWE.WEPraesenzBuchen;
import de.meetingapps.meetingportal.meetComWE.WEPraesenzBuchenRC;
import de.meetingapps.meetingportal.meetComWE.WEPraesenzStatusabfrage;
import de.meetingapps.meetingportal.meetComWE.WEPraesenzStatusabfrageRC;
import de.meetingapps.meetingportal.meetComWE.WETabletRuecksetzen;
import de.meetingapps.meetingportal.meetComWE.WETabletRuecksetzenRC;
import de.meetingapps.meetingportal.meetingCoreReport.RpDrucken;
import de.meetingapps.meetingportal.meetingCoreReport.RpVariablen;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * The Class CtrlHauptStage.
 */
public class CtrlHauptStage extends CtrlRoot {

    @FXML
    private AnchorPane rootPane;

    /** The log drucken. */
    private int logDrucken = 10;

    /** The log show. */
    private boolean logShow = false; //Für Fehleranzeige

    /** The resources. */
    @FXML
    private ResourceBundle resources;

    /** The location. */
    @FXML
    private URL location;

    /** The scrpn vertreter. */
    @FXML
    private ScrollPane scrpnVertreter;

    /** The btn suchen. */
    @FXML
    private Button btnSuchen;

    /** The btn links. */
    @FXML
    private Button btnLinks;

    /** The btn mitte. */
    @FXML
    private Button btnMitte;

    /** The btn rechts. */
    @FXML
    private Button btnRechts;

    /** The lbl anweisung. */
    @FXML
    private Label lblAnweisung;

    /** The tf scan feld. */
    @FXML
    private TextField tfScanFeld;

    /** The lbl Z 1. */
    @FXML
    private Label lblZ1;

    /** The lbl Z 2. */
    @FXML
    private Label lblZ2;

    /** The lbl Z 3. */
    @FXML
    private Label lblZ3;

    /** The lbl Z 4. */
    @FXML
    private Label lblZ4;

    /** The lbl Z 5. */
    @FXML
    private Label lblZ5;

    /** The lbl Z 6. */
    @FXML
    private Label lblZ6;

    /** The lbl Z 7. */
    @FXML
    private Label lblZ7;

    /** The lbl V 1. */
    @FXML
    private Label lblV1;

    /** The lbl V 2. */
    @FXML
    private Label lblV2;

    /** The lbl V 3. */
    @FXML
    private Label lblV3;

    /** The lbl name. */
    @FXML
    private Label lblName;

    /** The lbl vorname. */
    @FXML
    private Label lblVorname;

    /** The lbl ort. */
    @FXML
    private Label lblOrt;

    /** The tf name. */
    @FXML
    private TextField tfName;

    /** The tf vorname. */
    @FXML
    private TextField tfVorname;

    /** The tf ort. */
    @FXML
    private TextField tfOrt;

    /** The lbl Z 0. */
    @FXML
    private Label lblZ0;

    /** The lbl fehler meldung. */
    @FXML
    private Label lblFehlerMeldung;

    /** The lbl zuletzt gebucht. */
    @FXML
    private Label lblZuletztGebucht;

    /** The tf zuletzt gebucht. */
    @FXML
    private TextField tfZuletztGebucht;

    /** The btn label nachdruck. */
    @FXML
    private Button btnLabelNachdruck;

    /** The tf scan bestaetigen. */
    @FXML
    private TextField tfScanBestaetigen;

    /** The btn abstimmungs modus. */
    @FXML
    private Button btnAbstimmungsModus;

    /** The lbl stimmen 0. */
    @FXML
    private Label lblStimmen0;

    /** The tf scan stimmen 0. */
    @FXML
    private TextField tfScanStimmen0;

    /** The lbl stimmen 1. */
    @FXML
    private Label lblStimmen1;

    /** The tf scan stimmen 1. */
    @FXML
    private TextField tfScanStimmen1;

    /** The lbl stimmen 2. */
    @FXML
    private Label lblStimmen2;

    /** The tf scan stimmen 2. */
    @FXML
    private TextField tfScanStimmen2;

    /** The lbl stimmen 3. */
    @FXML
    private Label lblStimmen3;

    /** The tf scan stimmen 3. */
    @FXML
    private TextField tfScanStimmen3;

    /** The lbl stimmen 4. */
    @FXML
    private Label lblStimmen4;

    /** The tf scan stimmen 4. */
    @FXML
    private TextField tfScanStimmen4;

    /** The pn app. */
    @FXML
    private ScrollPane pnApp;

    private StackPane loading;

    /**
     * Initialize.
     */
    @FXML
    void initialize() {
        /**********************
         * Ab hier individuell
         **************************************************/

        wsClient = new WSClient();

        /** Prüfen, ob Rücksetzen der Abstimmungsdaten erforderlich */
        WETabletRuecksetzen weTabletRuecksetzen = new WETabletRuecksetzen();
        WELoginVerify weLoginVerify = new WELoginVerify();
        weTabletRuecksetzen.setWeLoginVerify(weLoginVerify);
        weTabletRuecksetzen.geraeteNummer = ParamS.clGlobalVar.arbeitsplatz;
        WETabletRuecksetzenRC weTabletRuecksetzenRC = wsClient.tabletRuecksetzenPruefen(weTabletRuecksetzen);
        if (weTabletRuecksetzenRC.ruecksetzen == true) {
            CaZeigeHinweis caZeigeHinweis = new CaZeigeHinweis();
            boolean brc = caZeigeHinweis.zeige2Buttons(eigeneStage, "Abstimmungsdaten zurücksetzen!?", "Ja", "Nein");
            if (brc == true) {
                CaDateiVerwaltung caDateiVerwaltung = new CaDateiVerwaltung();
                for (int i = 1; i <= 2; i++) {
                    String pfad = "";
                    if (i == 1) {
                        pfad = ParamS.clGlobalVar.lwPfadSicherung1;
                    } else {
                        pfad = ParamS.clGlobalVar.lwPfadSicherung2;
                    }
                    if (!pfad.isEmpty()) {
                        String dateiname = pfad + "\\abstimmung" + Integer.toString(ParamS.clGlobalVar.arbeitsplatz)
                                + ".txt";
                        caDateiVerwaltung.deleteDatei(dateiname);
                    }

                }
                weTabletRuecksetzenRC = wsClient.tabletRuecksetzen(weTabletRuecksetzen);
            }
        }

        vertreterNameLabel();
        vertreterVornameLabel();

        statusMaske = konstStatusMaske_EinstiegsCodeEingeben;
        setzeAnzeige();

        if (eingabeNummerBereitsErfolgt == false) {
            Platform.runLater(() -> tfScanFeld.requestFocus());
        }

        eigeneStage.addEventHandler(KeyEvent.KEY_PRESSED, e -> {
            if (e.isControlDown() && e.getCode().equals(KeyCode.M)) {
                CaOpenWindow.openModulauswahl(eigeneStage);
            }
        });

        ObjectActions.createInfoButton(rootPane, new String[] { "Strg-M - Modulauswahl" }, 30.0, 10.0);

        loading = LoadingScreen.createLoadingScreen(rootPane);

        ObjectActions.tfEnterEvent(tfVorname, btnLinks);
        ObjectActions.tfEnterEvent(tfName, btnLinks);
        ObjectActions.tfEnterEvent(tfOrt, btnLinks);

        if (eingabeNummerBereitsErfolgt == true) {
            tfScanFeld.setText(eingabeNummer);
            scanFeldVerarbeiten();
        }
    }

    /** The grpn vertreter. */
    VBox boxVertreter = null;

    /** The btn vertreter selbst. */
    Button btnVertreterSelbst = null;

    /** The btn vertreter neue vollmacht. */
    Button btnVertreterNeueVollmacht = null;

    /** The btn vertreter. */
    Button btnVertreter[] = null;

    /** The btn vollmacht dritte aktionaer uebernehmen. */
    Button btnVollmachtDritteAktionaerUebernehmen = null;

    /** The btn vollmacht dritte letzten vertreter uebernehmen. */
    Button btnVollmachtDritteLetztenVertreterUebernehmen = null;

    /** The btn vollmacht dritte. */
    Button btnVollmachtDritte[] = null;

    /** The grpn app teilnehmer. */
    GridPane grpnAppTeilnehmer = null;

    /** The ck bx vollmacht vorgelegt. */
    CheckBox ckBxVollmachtVorgelegt[] = null;

    /** The ck bx person ueberprueft. */
    CheckBox ckBxPersonUeberprueft[] = null;

    /** The ck bx nicht buchen. */
    CheckBox ckBxNichtBuchen[] = null;

    /** The l tf stimmkarte. */
    TextField lTfStimmkarte[][] = null;

    /** The btn vollmacht eingeben. */
    Button btnVollmachtEingeben[] = null;

    /** The vertreter V box. */
    VBox vertreterVBox[] = null;

    /** The app vertreter name. */
    String[] appVertreterName = null;

    /** The app vertreter vorname. */
    String[] appVertreterVorname = null;

    /** The app vertreter ort. */
    String[] appVertreterOrt = null;

    /** The app vollmacht personen nat jur ident. */
    int[] appVollmachtPersonenNatJurIdent = null;

    /** The app letzter vertreter name. */
    String appLetzterVertreterName = "";

    /** The app letzter vertreter vorname. */
    String appLetzterVertreterVorname = "";

    /** The app letzter vertreter ort. */
    String appLetzterVertreterOrt = "";

    /** The app letzter vertreter ident. */
    int appLetzterVertreterIdent = -1;

    /** The konst status maske einstiegs code eingeben. */
    private final int konstStatusMaske_EinstiegsCodeEingeben = 2;

    /** The konst status maske erstzugang wiederzugang mit frage selbst. */
    private final int konstStatusMaske_ErstzugangWiederzugangMitFrageSelbst = 3;

    /** The konst status maske erstzugang wiederzugang mit frage vertreter. */
    private final int konstStatusMaske_ErstzugangWiederzugangMitFrageVertreter = 4;

    /** The konst status maske erstzugang wiederzugang vertreter eingabe. */
    private final int konstStatusMaske_ErstzugangWiederzugangVertreterEingabe = 5;

    /** The konst status maske stimmkarte neu eingeben. */
    private final int konstStatusMaske_StimmkarteNeuEingeben = 6;

    /** The konst status maske abgang. */
    private final int konstStatusMaske_Abgang = 7;

    /** The konst status maske wiederzugang ohne frage. */
    private final int konstStatusMaske_WiederzugangOhneFrage = 8;

    /** The konst status maske vertreterwechsel eingabe vertreter. */
    private final int konstStatusMaske_VertreterwechselEingabeVertreter = 9;

    /** The konst status maske buendeln. */
    private final int konstStatusMaske_Buendeln = 10;

    /** The konst status maske vollmacht SRV. */
    private final int konstStatusMaske_VollmachtSRV = 11;

    /** The konst status maske fehlersituation. */
    private final int konstStatusMaske_Fehlersituation = 13;

    /** The konst status maske zugang abgang app ident. */
    private final int konstStatusMaske_ZugangAbgangAppIdent = 21;

    /**
     * 2 =BarcodeScannen
     * 3 =Erstzugang/WiederzugangMitFrage - Selbst?
     * 4 =Erstzugang/WiederzugangMitFrage - Sind Sie Vertreter?
     * 5 =Erstzugang/WiederzugangMitFrage - Vertreter eingabe
     * 6 =Stimmkarte einlesen (als Neuzuordnung)
     * 7 =Abgang
     * 8 =Wiederzugang (einer bereits anwesenden EK, gleiche Person/EK)
     * 9 =Vertreterwechsel - Vertretereingabe
     * 10=Bündeln
     * 11=VollmachtSRV 
     * TODO $Akkreditierung VollmachtSRV implementieren (mit / ohne Weisungseingabe)
     * 13=Fehlermeldung - Anzeige des Aktionärs, "falscher Barcode"
     * 
     * 21 = Anzeige Zugang/Abgang AppIdent
     * 
     * 
     * Detailbeschreibung, welche Buttons in welcher Maske unter welcher Situation angezeigt werden, siehe
     * in AktionaersAkkreditierungButtonsUndTexte.xlsx
     */
    private int statusMaske = konstStatusMaske_EinstiegsCodeEingeben;

    /** *****************Für Web-Service-Kommunikation***********************. */
    private WSClient wsClient = null;

    /**
     * ****************Für "normale" Zugangs/Abgangbuchungen*********************.
     */
    private EclMeldung lEclMeldung = null;

    /** The l initial passwort. */
    private String lInitialPasswort = "";

    /** The aktionaer vollmachten. */
    private EclWillensErklVollmachtenAnDritte[] aktionaerVollmachten = null;

    /** The aktionaer vollmachten aktuelle abfrage. */
    private int aktionaerVollmachtenAktuelleAbfrage = 0;
    //	private EclZutrittsIdent lEintrittskartennummer=null;
    //	private String lStimmkartennummer=null;
    /**
     * Für Zugang: vorbestimmte Peron (d.h. nur diese darf zugehen!) (siehe
     * WEPraesenzStatusabfrageRC)
     */
    private int vorbestimmtePersonNatJur = 0;
    /**
     * Für Zugang: vorbestimmte Peron (d.h. nur diese darf zugehen!) (siehe auch
     * WEPraesenzStatusabfrageRC - enthält komplette Personendaten falls
     * vorbestimmtePersonNatJur auf einen Bevollmächtigten zeigt
     */
    private EclWillensErklVollmachtenAnDritte vorbestimmteEclPersonNatJur = null;

    /** Enthält Willenserklärungsaktion, die durchgeführt werden soll. */
    private int aktion = 0;

    /** bei Abgangsbuchung: auch Button Vertreterwechsel anbieten. */
    private int auchVollmachtwechsel = 0;

    /** bei Abgangsbuchung: auch Button VollmachtKIAV anbieten. */
    private int auchVollmachtSRV = 0;

    /**
     * Bei Abgangsbuchung: es wurde Erstzugangs-oder WiederzugangsKarte eingelesen.
     */
    private int zugangskarteGelesen = 0;

    /** Fuer statusMaske==13: Aktionär ist anwesend oder abwesend. */
    private boolean istAnwesend = false;

    /** Für die Anzeige von bereits von zugeordneten EKs. */
    private EclZutrittsIdent zutrittsIdent = null;

    /** The zutritts ident kartenklasse. */
    private int zutrittsIdentKartenklasse = 0;

    /**
     * Für die Eingabe von zuzuordnenden Stimmkarten. Entweder bereits zugeordnete
     * (zum Anzeigen), oder leer (einzugeben), oder - nach der Eingabemaske -
     * gefüllt
     */
    private String[] stimmkarten = null;
    /**
     * wie WEPraesenzStatusAbfrageRC.stimmkartenZuordnungenAktionaer, aber für die
     * tatsächliche Person die Wiederzugeht / Erstzugeht: ==0 => keine Zuordnung
     * erforderlich ==1 => Eingabe erforderlich; ==-1 => bereits zugeordnet
     */
    private int[] stimmkartenEingeben = null;
    /**
     * Generell - Stimmkartenzuordnung muß noch eingegeben werden (true) (bzw. beim
     * Speichern: wurden nei eingegeben
     */
    private boolean stimmkarteEingebenErforderlich = false;

    /**
     * -1 => nicht definiert; ansonsten 0 bis 4 - gibt das höchste zu verwendende
     * Stimmkartenzuordnungsfeld wieder.
     */
    private int letzteZuzuordnendeStimmkarte = -1;

    /**
     * Nach Personenabfrage/Eingabe gefüllt: -1 => Aktionär selbst >1 =>
     * personNatJurIdent aus aktionaerVollmachten =0 => neuer Vertreter, der in
     * vertreterName, vertreterVorname, vertreterOrt steht, bzw. auch
     * personalNatJurVertreterNeueVollmacht>0 Bei >=0 immer auch vertreterName,
     * vertreterVorname, vertreterOrt gefüllt (für Anzeige zu verwenden)
     */
    private int personNatJurVertreter = 0;

    /** The vertreter name. */
    private String vertreterName = "";

    /** The vertreter vorname. */
    private String vertreterVorname = "";

    /** The vertreter ort. */
    private String vertreterOrt = "";

    /** The person nat jur vertreter neue vollmacht. */
    private int personNatJurVertreterNeueVollmacht = 0;

    /**** Aktionen bei Erstzugang erforderlich - z.B. Sammelkarte stornieren ****/
    private int aktionen = 0;

    /**
     * ***************************Letzter
     * Vertreter*********************************************.
     */
    private int letzterVertreterIdent = 0;

    /** The letzter vertreter name. */
    private String letzterVertreterName = "";

    /** The letzter vertreter vorname. */
    private String letzterVertreterVorname = "";

    /** The letzter vertreter ort. */
    private String letzterVertreterOrt = "";

    /** The nummer erst scan. */
    private String nummerErstScan = "";

    /**
     * Für AppIdent. Beim Buchen einer AppIdent können ja einzelne Meldungen aus der
     * Status-Abfrage (bzw. aus den gelesenen Barcodes) ausgeklammert werden. Damit
     * stimmt der [offset] in der StatusmeldungRC nicht mehr mit dem [offset] im
     * buchen bzw. buchenrc überein. Die Zuordnung wird aber für die
     * "Nachverarbeitung" (Labelprint!) benötigt. Sie ist hier eingetragen. D.h.
     * zuordnungStatusZuBuchen[]=-1 => keine Buchen durchgeführt für diese meldung
     * []>=0 => Offset in buchen bzw. buchenRC für diese meldung.
     */
    private int[] zuordnungStatusZuBuchen = null;

    /** The gwe praesenz statusabfrage RC. */
    /*Enthält den Statusabfrage-RC*/
    private WEPraesenzStatusabfrageRC gwePraesenzStatusabfrageRC = null;

    /**
     * **************Löscht alle Daten vor der Eingabe eines neuen
     * Vorgangs***************.
     */
    private void clearDaten() {

        lEclMeldung = null;
        aktionaerVollmachten = null;
        aktionaerVollmachtenAktuelleAbfrage = 0;
        //		lEintrittskartennummer=null;
        //		lStimmkartennummer=null;

        vorbestimmtePersonNatJur = 0;
        vorbestimmteEclPersonNatJur = null;

        aktion = 0;
        auchVollmachtwechsel = 0;
        auchVollmachtSRV = 0;
        zugangskarteGelesen = 0;
        istAnwesend = false;

        zutrittsIdent = new EclZutrittsIdent();
        zutrittsIdentKartenklasse = 0;

        stimmkarten = new String[] { "", "", "", "", "", "" };
        stimmkartenEingeben = new int[] { 0, 0, 0, 0, 0, 0 };

        stimmkarteEingebenErforderlich = false;
        letzteZuzuordnendeStimmkarte = -1;

        personNatJurVertreter = 0;
        vertreterName = "";
        vertreterVorname = "";
        vertreterOrt = "";
        personNatJurVertreterNeueVollmacht = 0;

        vertreterNameSet("");
        vertreterVornameSet("");
        tfOrt.setText("");
        nummerErstScan = "";

        lblFehlerMeldung.setStyle(""); //-fx-background-color: #ff0000; 

    }

    /** *********Label-Druck************************************. */
    /**
     * Labeldaten. Werden beim "Erstlabeldruck" für einen Aktionär gefüllt. Bei
     * Funktion "Label wiederholen" werden diese (dann ja gefüllten) Variablen dann
     * einfach nochmal ausgedruckt - ohne Veränderung/Neufüllung. Für 4 zuzuordnende
     * Stimmmaterialen (StimmkarteSecond erhält keinen Labelprint)
     */
    private EclLabelPrint[] datenLabelPrint = new EclLabelPrint[] { null, null, null, null };

    /**
     * true => beim letzten Vorgang wurde mindestens ein Label gedruckt.
     *
     * @return true, if successful
     */
    private boolean pruefeObLabelDatenVorhanden() {
        for (int i = 0; i < 4; i++) {
            if (datenLabelPrint[i] != null) {
                return true;
            }
        }
        return false;
    }

    /**
     * Prints the einzelnes aktionaers label.
     *
     * @param labelOffset the label offset
     */
    private void printEinzelnesAktionaersLabel(int labelOffset) {
        if (!ParamS.paramGeraet.labelDruckerIPAdresse.equals("0")) {
            CALabelPrint caLabelPrint = new CALabelPrint();
            caLabelPrint.druckenAktionaer(datenLabelPrint[labelOffset]);
        }
    }

    /**
     * Prints the alle aktionaers label.
     */
    private void printAlleAktionaersLabel() {
        for (int j = 0; j < 4; j++) {
            if (datenLabelPrint[j] != null) {
                if (lLabelGedruckt == true) {
                    CaZeigeHinweis lZeigeHinweis = new CaZeigeHinweis();
                    lZeigeHinweis.zeige(eigeneStage, "Bitte Label entfernen!");
                }
                printEinzelnesAktionaersLabel(j);
                lLabelGedruckt = true;
            }
        }
    }

    /**
     * Prints the buendeln label.
     */
    private void printBuendelnLabel() {
        if (!ParamS.paramGeraet.labelDruckerIPAdresse.equals("0")) {
            CALabelPrint caLabelPrint = new CALabelPrint();
            caLabelPrint.druckenBuendelnLabel(ParamS.clGlobalVar.arbeitsplatz, lWEPraesenzBuchenRC.buendelnProtokollNr);
        }
    }

    /**
     * ***************"Kleine" Hilfsfunktionen************************.
     *
     * Falls pPersonIdent==-1, dann null, sonst dieDaten der bevollmächtigten
     * Person. Gesucht wird in den Vollmachten, die zur Ident[pOffset] enthalten
     * sind
     * 
     * @param pPersonIdent the person ident
     * @param pOffset      the offset
     * @return the ecl willens erkl vollmachten an dritte
     */
    private EclWillensErklVollmachtenAnDritte sucheEclInAktionaerVollmachten(int pPersonIdent, int pOffset) {
        EclWillensErklVollmachtenAnDritte lrcEclWillensErklVollmachtenAnDritte = null;
        if (pPersonIdent <= 0) {
            return null;
        }
        for (int i = 0; i < gwePraesenzStatusabfrageRC.aktionaerVollmachten.get(pOffset).length; i++) {
            if (logDrucken == 10) {
                CaBug.druckeInfo("CtrlHauptStage.sucheEclInAktionaerVollmachten sucheEclInAktionaerVollmachten i=" + i);
                CaBug.druckeInfo(
                        "sucheEclInAktionaerVollmachten gwePraesenzStatusabfrageRC.aktionaerVollmachten.get(pOffset)[i].bevollmaechtigtePerson.ident="
                                + gwePraesenzStatusabfrageRC.aktionaerVollmachten
                                        .get(pOffset)[i].bevollmaechtigtePerson.ident);
            }
            if (pPersonIdent == gwePraesenzStatusabfrageRC.aktionaerVollmachten
                    .get(pOffset)[i].bevollmaechtigtePerson.ident) {
                lrcEclWillensErklVollmachtenAnDritte = gwePraesenzStatusabfrageRC.aktionaerVollmachten.get(pOffset)[i];
            }
        }
        return lrcEclWillensErklVollmachtenAnDritte;
    }

    /**
     * Aktion enthalten.
     *
     * @param aktionsListe the aktions liste
     * @param aktion       the aktion
     * @return true, if successful
     */
    private boolean aktionEnthalten(List<Integer> aktionsListe, int aktion) {
        int i;
        if (aktionsListe == null) {
            return false;
        }
        for (i = 0; i < aktionsListe.size(); i++) {
            if (aktionsListe.get(i) == aktion) {
                return true;
            }
        }

        return false;
    }

    /**
     * Stimkarten erfassen fuer person.
     *
     * @param pStimmkartenZuordnen the stimmkarten zuordnen
     * @return true, if successful
     */
    private boolean stimkartenErfassenFuerPerson(int[] pStimmkartenZuordnen) {
        if (ParamS.param.paramAkkreditierung.eintrittskarteWirdStimmkarte == false && pStimmkartenZuordnen[0] == 1) {
            return true;
        }
        for (int i = 1; i <= 4; i++) {
            if (pStimmkartenZuordnen[i] == 1) {
                return true;
            }
        }
        return false;
    }

    /**
     * Ausgabe test.
     *
     * @param wePraesenzStatusabfrageRC the we praesenz statusabfrage RC
     */
    private void ausgabe_test(WEPraesenzStatusabfrageRC wePraesenzStatusabfrageRC) {
        if (logDrucken == 10) {
            CaBug.druckeInfo("CtrlHauptStage Frontoffice.ausgabe_test wePraesenzStatusabfrageRC rc="
                    + wePraesenzStatusabfrageRC.rc + " " + CaFehler.getFehlertext(wePraesenzStatusabfrageRC.rc, 0));

            if (wePraesenzStatusabfrageRC.rc == CaFehler.pmNummernformUngueltig
                    || wePraesenzStatusabfrageRC.rc == CaFehler.pmNummernformAktionsnummerUngueltig
                    || wePraesenzStatusabfrageRC.rc == CaFehler.pfNichtEindeutig
                    || wePraesenzStatusabfrageRC.rc == CaFehler.pmNummernformMandantUngueltig
                    || wePraesenzStatusabfrageRC.rc == CaFehler.pfXyNichtImZulaessigenNummernkreis
                    || wePraesenzStatusabfrageRC.rc == CaFehler.pmNummernformStimmkartenSubNummernkreisUngueltig
                    || wePraesenzStatusabfrageRC.rc == CaFehler.pmNummernformHVIdentNrUngueltig
                    || wePraesenzStatusabfrageRC.rc == CaFehler.pmNummernformKontrollzahlFalsch
                    || wePraesenzStatusabfrageRC.rc == CaFehler.pmNummernformPruefzifferFalsch
                    || wePraesenzStatusabfrageRC.rc == CaFehler.pmNummernformStimmartFalsch) {
                return;
            }

            CaBug.druckeInfo("Kartenklasse=" + wePraesenzStatusabfrageRC.rcKartenklasse);
            CaBug.druckeInfo("Kartenart=" + wePraesenzStatusabfrageRC.rcKartenart);
            CaBug.druckeInfo("Länge der Identifikationen=" + wePraesenzStatusabfrageRC.identifikationsnummer.size());

            int i;
            for (i = 0; i < wePraesenzStatusabfrageRC.identifikationsnummer.size(); i++) {
                CaBug.druckeInfo(i + " Identifikation =" + wePraesenzStatusabfrageRC.identifikationsnummer.get(i));
                CaBug.druckeInfo(
                        i + " IdentifikationNeben =" + wePraesenzStatusabfrageRC.identifikationsnummerNeben.get(i));
                CaBug.druckeInfo(i + " kartenklasseZuIdentifikationsnummer "
                        + wePraesenzStatusabfrageRC.kartenklasseZuIdentifikationsnummer.get(i));
                CaBug.druckeInfo(
                        i + " rcZuIdentifikationsnummer " + wePraesenzStatusabfrageRC.rcZuIdentifikationsnummer.get(i));
                for (int i1 = 0; i1 < wePraesenzStatusabfrageRC.erforderlicheAktionen.get(i).size(); i1++) {
                    CaBug.druckeInfo(i + " erforderlicheAktionen " + KonstWillenserklaerung
                            .getText(wePraesenzStatusabfrageRC.erforderlicheAktionen.get(i).get(i1)));
                }
                CaBug.druckeInfo("Vollmachten");
                if (wePraesenzStatusabfrageRC.aktionaerVollmachten.get(i) != null) {
                    for (int i1 = 0; i1 < wePraesenzStatusabfrageRC.aktionaerVollmachten.get(i).length; i1++) {
                        CaBug.druckeInfo(
                                wePraesenzStatusabfrageRC.aktionaerVollmachten.get(i)[i1].bevollmaechtigtePerson.name);
                    }
                } else {
                    CaBug.druckeInfo("Sind Null");

                }
                CaBug.druckeInfo("Zulässige Funktionen zu dieser Aktionsnummer:");
                for (int i1 = 0; i1 < wePraesenzStatusabfrageRC.zulaessigeFunktionenMitAktionsnummer.get(i)
                        .size(); i1++) {
                    CaBug.druckeInfo(i + " zulaessigeFunktionen " + KonstWillenserklaerung
                            .getText(wePraesenzStatusabfrageRC.zulaessigeFunktionenMitAktionsnummer.get(i).get(i1)));
                }
                CaBug.druckeInfo("Zulässige Funktionen alle:");
                for (int i1 = 0; i1 < wePraesenzStatusabfrageRC.zulaessigeFunktionenAlle.get(i).size(); i1++) {
                    CaBug.druckeInfo(i + " zulaessigeFunktionen " + KonstWillenserklaerung
                            .getText(wePraesenzStatusabfrageRC.zulaessigeFunktionenAlle.get(i).get(i1)));
                }
            }

            CaBug.druckeInfo("Zulässige Gemeinsame Funktionen Alle:");
            if (wePraesenzStatusabfrageRC.gemeinsameZulaessigeFunktionenAlle != null) {
                for (int i1 = 0; i1 < wePraesenzStatusabfrageRC.gemeinsameZulaessigeFunktionenAlle.size(); i1++) {
                    CaBug.druckeInfo(i + " zulaessigeFunktionen " + KonstWillenserklaerung
                            .getText(wePraesenzStatusabfrageRC.gemeinsameZulaessigeFunktionenAlle.get(i1)));
                }
            } else {
                CaBug.druckeInfo("gemeinsameZulaessigeFunktionenAlle ist null");
            }
        }

    }

    /**Füllen von:
     * stimmkartenEingeben[] ([4]=Second)
     * stimmkarten[] ([4]=Second)
     * zutrittsIdent
     * mit der Vorbelegung für pPersonNatJurIdent.
     * 
     * Vorher muß aktionaerVollmachten bereits belegt sein.
     * 
     * Offset: Funktion wird für ident[pOffset] durchgeführt
     */
    private void findeStimmkartenZutrittsIdentZuPerson(int pPersonNatJurIdent, int pOffset) {
        if (lEclMeldung.meldungIstEinGast()) {
            /*Dann die Aktionärsdaten verwenden*/
            for (int i = 0; i < 6; i++) {
                if (i < 4) {
                    stimmkarten[i] = gwePraesenzStatusabfrageRC.zugeordneteStimmkartenAktionaer
                            .get(pOffset)[i];/*von Aktionär übernehmen*/
                    if (logDrucken == 10) {
                        CaBug.druckeInfo(
                                "CtrlHauptStage.findeStimmkartenZutrittsIdentZuPerson i=" + i + " " + stimmkarten[i]);
                    }
                }
                if (i == 5) {
                    //					stimmkarten[i]=gwePraesenzStatusabfrageRC.zugeordneteStimmkartenSecondAktionaer.get(pOffset);/*von Aktionär übernehmen*/
                }
            }
            if (zutrittsIdent.zutrittsIdent.isEmpty()) {
                zutrittsIdent = gwePraesenzStatusabfrageRC.zugeordneteEintrittskartenAktionaer.get(pOffset);
                zutrittsIdentKartenklasse = KonstKartenklasse.eintrittskartennummer;
            }

            return;
        }

        if (pPersonNatJurIdent == 0) {
            return;
        }

        if (pPersonNatJurIdent == -1) { //Aktionär selbst
            for (int i = 0; i < 6; i++) {
                stimmkartenEingeben[i] = gwePraesenzStatusabfrageRC.stimmkartenZuordnungenAktionaer
                        .get(pOffset)[i];/*von Aktionär übernehmen*/
                if (logDrucken == 10) {
                    CaBug.druckeInfo("CtrlHauptStage.findeStimmkartenZutrittsIdentZuPerson i=" + i
                            + " stimmkartenEingeben=" + stimmkartenEingeben[i]);
                }
                if (i < 4) {
                    stimmkarten[i] = gwePraesenzStatusabfrageRC.zugeordneteStimmkartenAktionaer
                            .get(pOffset)[i];/*von Aktionär übernehmen*/
                }
                if (i == 5) {
                    if (stimmkartenEingeben[i] == 1) {
                        stimmkarten[i] = gwePraesenzStatusabfrageRC.zugeordneteStimmkartenSecondAktionaer
                                .get(pOffset);/*von Aktionär übernehmen*/
                    }
                }
            }
            if (logDrucken == 10) {
                CaBug.druckeInfo(
                        "CtrlHauptStage.findeStimmkartenZutrittsIdentZuPerson Hier überprüfen der zutrittsIdent");
                CaBug.druckeInfo("zutrittsIdent.zutrittsIdent " + zutrittsIdent.zutrittsIdent);
            }

            if (zutrittsIdent.zutrittsIdent.isEmpty()) {
                zutrittsIdent = gwePraesenzStatusabfrageRC.zugeordneteEintrittskartenAktionaer.get(pOffset);
                zutrittsIdentKartenklasse = KonstKartenklasse.eintrittskartennummer;
            }
        }
        if (pPersonNatJurIdent > 0) {// bereits eingetragener Vertreter
            int gef = -1;
            for (int i = 0; i < aktionaerVollmachten.length; i++) {
                if (aktionaerVollmachten[i].bevollmaechtigtePerson.ident == pPersonNatJurIdent) {
                    gef = i;
                }
            }
            for (int i = 0; i < 6; i++) {
                stimmkartenEingeben[i] = gwePraesenzStatusabfrageRC.stimmkartenZuordnungenVollmachten
                        .get(pOffset)[gef][i];/*von Bevollmächtigten übernehmen*/
                if (i < 4) {
                    stimmkarten[i] = gwePraesenzStatusabfrageRC.zugeordneteStimmkartenVollmachten
                            .get(pOffset)[gef][i];/*von Bevollmächtigten übernehmen*/
                }
                if (i == 5) {
                    if (stimmkartenEingeben[i] == 1) {
                        stimmkarten[i] = gwePraesenzStatusabfrageRC.zugeordneteStimmkartenSecondVollmachten
                                .get(pOffset)[gef];/*von Bevollmächtigten übernehmen*/
                    }
                }
            }
            if (zutrittsIdent.zutrittsIdent.isEmpty()) {
                zutrittsIdent = gwePraesenzStatusabfrageRC.zugeordneteEintrittskartenVollmachten.get(pOffset)[gef];
                zutrittsIdentKartenklasse = KonstKartenklasse.eintrittskartennummer;
            }
        }

    }

    /**
     * *************************AnzeigeFunktionen*********************************************.
     */

    private void clearFehlermeldung() {
        lblFehlerMeldung.setText("");
        return;

    }

    /**
     * pSituation: Aufrufsituation innerhalb des Front-Officetools. Kann zu differenzierter Fehlermeldung verwendet werden. 
     * Derzeit: 
     * 1 = nach Eingabe erster Barcode - Scanfeld wird gelöscht, Cursor wieder im Scanfeld positioniert
     *
     * @param pFehler    the fehler
     * @param pSituation the situation
     */
    private void zeigeFehlermeldung(int pFehler, int pSituation) {

        String fehlertext = "";
        if (logShow) {
            fehlertext += Integer.toString(pFehler) + " " + CaFehler.getFehlertext(pFehler, 0) + " ";
        }

        switch (pFehler) {
        /*Fehler aus blNummernformen*/
        case CaFehler.pmNummernformUngueltig: {
            fehlertext += "Eingelesene Nummer hat ungültiges Format. Bitte überprüfen, und neu einlesen. Ggf. Serviceschalter.";
            break;
        }
        case CaFehler.pmNummernformAktionsnummerUngueltig: {
            fehlertext += "Eingelesene Nummer kann hier nicht verwendet werden (ungültige Aktionsnummer). Bitte überprüfen, und neu einlesen. Ggf. Serviceschalter.";
            break;
        }
        case CaFehler.pmNummernformMandantUngueltig: {
            fehlertext += "Eingelesene Nummer gehört nicht zu dieser Veranstaltung! Bitte Formular (Datum, Veranstalter) überprüfen, und neu einlesen. Ggf. Serviceschalter.";
            break;
        }
        case CaFehler.pfXyNichtImZulaessigenNummernkreis: {
            fehlertext += "Eingelesene Nummer kann hier nicht verwendet werden (nicht im Nummernkreis). Bitte überprüfen, und neu einlesen. Ggf. Serviceschalter.";
            break;
        }
        case CaFehler.pmNummernformStimmkartenSubNummernkreisUngueltig: {
            fehlertext += "Eingelesene Nummer kann hier nicht verwendet werden (Subnummernkreis Stimmkarte). Bitte überprüfen, und neu einlesen. Ggf. Serviceschalter.";
            break;
        }
        case CaFehler.pmNummernformHVIdentNrUngueltig: {
            fehlertext += "Eingelesene Nummer hat ungültiges Format (HV-Identifikations-Nummer). Bitte überprüfen, und neu einlesen. Ggf. Serviceschalter.";
            break;
        }
        case CaFehler.pmNummernformKontrollzahlFalsch: {
            fehlertext += "Eingelesene Nummer hat ungültiges Format (falsche Kontrollzahl). Bitte überprüfen, und neu einlesen. Ggf. Serviceschalter.";
            break;
        }
        case CaFehler.pmNummernformPruefzifferFalsch: {
            fehlertext += "Eingelesene Nummer hat ungültiges Format (falsche Prüfziffer). Bitte überprüfen, und neu einlesen. Ggf. Serviceschalter.";
            break;
        }
        case CaFehler.pmNummernformStimmartFalsch: {
            fehlertext += "Eingelesene Nummer hat ungültiges Format (falsche Stimmart). Bitte überprüfen, und neu einlesen. Ggf. Serviceschalter.";
            break;
        }

        case CaFehler.pmZutrittsIdentNichtVorhanden: {
            fehlertext += "Eintrittskarte unbekannt. Serviceschalter.";
            break;
        }
        case CaFehler.pmZutrittsIdentIstStorniert: {
            fehlertext += "Eintrittskarte wurde storniert / gesperrt. Serviceschalter.";
            break;
        }
        case CaFehler.pmStimmkarteNichtVorhanden: {
            fehlertext += "Stimmkarte noch nicht verwendet. Bitte überprüfen, ggf. zuerst Eintrittskartennummer einlesen. Ggf. Serviceschalter.";
            break;
        }
        case CaFehler.pmStimmkarteGesperrt: {
            fehlertext += "Stimmkarte wurde storniert / gesperrt. Serviceschalter.";
            break;
        }
        case CaFehler.pmStimmkarteSecondNichtVorhanden: {
            fehlertext += "StimmkarteSecond noch nicht verwendet. Bitte überprüfen, ggf. zuerst Eintrittskartennummer einlesen. Ggf. Serviceschalter.";
            break;
        }
        case CaFehler.pmStimmkarteSecondGesperrt: {
            fehlertext += "StimmkarteSecond wurde storniert / gesperrt. Serviceschalter.";
            break;
        }

        case CaFehler.pmSammelkartenBuchungAufDiesemGeraetNichtMoeglich: {
            fehlertext += "Akkreditierung von Sammelkarte ist auf diesem Arbeitsplatz nicht möglich. Bankenschalter.";
            break;
        }
        case CaFehler.pmNullBestandBuchenNichtMoeglich: {
            fehlertext += "Eintrittskarte hat Null-Bestand. Serviceschalter.";
            break;
        }
        case CaFehler.pmMeldungNochNichtGeprueft: {
            fehlertext += "Anmeldung ist gesperrt. Serviceschalter.";
            break;
        }

        case CaFehler.pmInSammelkarteEnthalten: {
            fehlertext += "Vollmacht/Weisung/Briefwahl vorhanden. Serviceschalter.";
            break;
        }

        case CaFehler.pmServiceschalter: {
            fehlertext += "Sonderaktion erforderlich. Serviceschalter.";
            break;
        }

        default:
            fehlertext = "Fehler " + CaFehler.getFehlertext(pFehler, 0)
                    + " => Bitte beim Front-Office-Betreuer melden!";
            break;

        }
        lblFehlerMeldung.setText(fehlertext);

        lblFehlerMeldung.setStyle("-fx-background-color: #ff0000; ");

        if (pSituation == 1) {
            tfScanFeld.setText("");
            tfScanFeld.requestFocus();
        }
    }

    /**
     * Zeige vollmachten dritte scroll.
     */
    private void zeigeVollmachtenDritteScroll() {
        boxVertreter = new VBox(15);
        boxVertreter.setAlignment(Pos.TOP_CENTER);

        btnVollmachtDritteAktionaerUebernehmen = new Button("Aktionär übernehmen");
        configVerteterButton(btnVollmachtDritteAktionaerUebernehmen);
        btnVollmachtDritteAktionaerUebernehmen.setOnAction(e -> {
            onBtnAktionaerUebernehmen(e);
        });
        boxVertreter.getChildren().add(btnVollmachtDritteAktionaerUebernehmen);

        if (letzterVertreterIdent != 0) {
            btnVollmachtDritteLetztenVertreterUebernehmen = new Button("Letzten Vertreter übernehmen: "
                    + letzterVertreterName + ", " + letzterVertreterVorname + ", " + letzterVertreterOrt);
            configVerteterButton(btnVollmachtDritteLetztenVertreterUebernehmen);
            btnVollmachtDritteLetztenVertreterUebernehmen.setOnAction(e -> {
                onBtnLetztenVertreterUebernehmen(e);
            });
            boxVertreter.getChildren().add(btnVollmachtDritteLetztenVertreterUebernehmen);
        }

        if (CInjects.weKIAVFuerVollmachtDritteRC.listKIAVFuerVollmachtDritte != null
                && CInjects.weKIAVFuerVollmachtDritteRC.listKIAVFuerVollmachtDritte.size() > 0) {
            int anzVollmachtenDritte = CInjects.weKIAVFuerVollmachtDritteRC.listKIAVFuerVollmachtDritte.size();
            btnVollmachtDritte = new Button[anzVollmachtenDritte];
            for (int j = 0; j < anzVollmachtenDritte; j++) {
                EclKIAVFuerVollmachtDritte kiavFuerVollmachtDritte = CInjects.weKIAVFuerVollmachtDritteRC.listKIAVFuerVollmachtDritte
                        .get(j);
                btnVollmachtDritte[j] = new Button(kiavFuerVollmachtDritte.kiavName + ", "
                        + kiavFuerVollmachtDritte.kiavOrt + "; " + kiavFuerVollmachtDritte.vertreterName + ", "
                        + kiavFuerVollmachtDritte.vertreterVorname + ", " + kiavFuerVollmachtDritte.vertreterOrt);
                configVerteterButton(btnVollmachtDritte[j]);
                btnVollmachtDritte[j].setOnAction(e -> {
                    clickedBtnVollmachtDritte(e);
                });
                boxVertreter.getChildren().add(btnVollmachtDritte[j]);
            }
        }

        scrpnVertreter.setContent(boxVertreter);

        if (ParamS.paramGeraet.akkreditierungShortcutsAktiv) {
            scrpnVertreter.setVisible(true);
        }
    }

    /**
     * Zeige vertreter scroll.
     */
    private void zeigeVertreterScroll() {
        boxVertreter = new VBox(15);
        boxVertreter.setAlignment(Pos.TOP_CENTER);

        btnVertreterNeueVollmacht = new Button("Neue Vollmacht prüfen / erfassen");
        configVerteterButton(btnVertreterNeueVollmacht);
        btnVertreterNeueVollmacht.setOnAction(e -> {
            clickedBtnVertreterNeueVollmacht(e);
        });
        boxVertreter.getChildren().add(btnVertreterNeueVollmacht);

        btnVertreterSelbst = new Button(
                "Selbst: " + lEclMeldung.name + ", " + lEclMeldung.vorname + ", " + lEclMeldung.ort);
        configVerteterButton(btnVertreterSelbst);
        btnVertreterSelbst.setOnAction(e -> {
            clickedBtnVertreterSelbst(e);
        });
        boxVertreter.getChildren().add(btnVertreterSelbst);

        if (aktionaerVollmachten != null && aktionaerVollmachten.length > 0) {
            int anzVollmachten = aktionaerVollmachten.length;
            btnVertreter = new Button[anzVollmachten];
            for (int j = 0; j < anzVollmachten; j++) {
                btnVertreter[j] = new Button("Vertreter: " + aktionaerVollmachten[j].bevollmaechtigtePerson.name + ", "
                        + aktionaerVollmachten[j].bevollmaechtigtePerson.vorname + ", "
                        + aktionaerVollmachten[j].bevollmaechtigtePerson.ort);
                configVerteterButton(btnVertreter[j]);
                btnVertreter[j].setOnAction(e -> {
                    clickedBtnVertreter(e);
                });
                boxVertreter.getChildren().add(btnVertreter[j]);
            }
        }

        scrpnVertreter.setContent(boxVertreter);

        if (ParamS.paramGeraet.akkreditierungShortcutsAktiv) {
            scrpnVertreter.setVisible(true);
        }
    }

    private void configVerteterButton(Button btn) {
        btn.setWrapText(true);
        btn.setTextAlignment(TextAlignment.CENTER);
        btn.setMaxWidth(Double.MAX_VALUE);
        btn.setPrefHeight(51);
    }

    /**
     * Disable stimmkarten felder.
     */
    private void disableStimmkartenFelder() {
        lblStimmen0.setVisible(false);
        tfScanStimmen0.setVisible(false);
        lblStimmen1.setVisible(false);
        tfScanStimmen1.setVisible(false);
        lblStimmen2.setVisible(false);
        tfScanStimmen2.setVisible(false);
        lblStimmen3.setVisible(false);
        tfScanStimmen3.setVisible(false);
        lblStimmen4.setVisible(false);
        tfScanStimmen4.setVisible(false);
    }

    /**
     * Enable stimmkarten felder.
     */
    private void enableStimmkartenFelder() {
        letzteZuzuordnendeStimmkarte = -1;

        for (int i = 4; i >= 0; i--) {
            Label lLabel = null;
            TextField lTextField = null;

            switch (i) {
            case 0:
                lLabel = lblStimmen0;
                lTextField = tfScanStimmen0;
                break;
            case 1:
                lLabel = lblStimmen1;
                lTextField = tfScanStimmen1;
                break;
            case 2:
                lLabel = lblStimmen2;
                lTextField = tfScanStimmen2;
                break;
            case 3:
                lLabel = lblStimmen3;
                lTextField = tfScanStimmen3;
                break;
            case 4:
                lLabel = lblStimmen4;
                lTextField = tfScanStimmen4;
                break;
            }

            if (stimmkartenEingeben[i] != 0) {/*Second*/
                lLabel.setVisible(true);
                lTextField.setVisible(true);
                lLabel.setText(gwePraesenzStatusabfrageRC.stimmkartenZuordnungenText.get(0)[i]);
                if (stimmkartenEingeben[i] == 1) {
                    lTextField.setText("");
                    lTextField.requestFocus();
                    lTextField.setEditable(true);
                    if (letzteZuzuordnendeStimmkarte == -1) {
                        letzteZuzuordnendeStimmkarte = i;
                    }
                } else {
                    lTextField.setText(stimmkarten[i]);
                    lTextField.setEditable(true);
                }
            }
        }
    }

    /**
     * Setze anzeige.
     */
    /*Farben:
     * #00ff00 = grün
     * #ff0000 = rot
     * #A7A7A7 = grau
     * #0080ff = blau
     */
    private void setzeAnzeige() {

        CaBug.druckeLog("statusMaske=" + statusMaske, logDrucken, 10);
        switch (statusMaske) {
        case konstStatusMaske_EinstiegsCodeEingeben: {/*Barcode scannen*/
            if (ParamS.paramGeraet.akkreditierungSuchfunktionAktiv) {
                btnSuchen.setVisible(true);
            } else {
                btnSuchen.setVisible(false);
            }
            lblAnweisung.setVisible(true);
            lblAnweisung.setText("Barcode scannen oder Nummer eingeben");
            tfScanFeld.setVisible(true);
            tfScanFeld.setText("");

            tfScanFeld.requestFocus();

            lblV1.setVisible(false);
            lblV2.setVisible(false);
            lblV3.setVisible(false);

            lblName.setVisible(false);
            tfName.setVisible(false);
            lblVorname.setVisible(false);
            tfVorname.setVisible(false);
            lblOrt.setVisible(false);
            tfOrt.setVisible(false);

            lblZ0.setVisible(false);
            lblZ1.setVisible(false);
            lblZ2.setVisible(false);
            lblZ3.setVisible(false);
            lblZ4.setVisible(false);
            lblZ5.setVisible(false);
            lblZ6.setVisible(false);
            lblZ7.setVisible(false);

            btnLinks.setText("Weiter");
            btnLinks.setVisible(true);
            btnLinks.setStyle("-fx-background-color: #0080ff");

            btnMitte.setVisible(false);

            btnRechts.setText("");
            btnRechts.setVisible(false);
            btnRechts.setStyle("-fx-background-color: #A7A7A7");

            tfScanBestaetigen.setVisible(false);
            tfScanBestaetigen.setText("");

            if (ParamS.paramGeraet.moduleTabletAbstimmung) {
                btnAbstimmungsModus.setVisible(true);
            } else {
                btnAbstimmungsModus.setVisible(false);
            }

            scrpnVertreter.setVisible(false);
            pnApp.setVisible(false);
            disableStimmkartenFelder();

            if (!pruefeObLabelDatenVorhanden()) {
                btnLabelNachdruck.setVisible(false);
            } else {
                btnLabelNachdruck.setVisible(true);
            }

            break;
        }
        case konstStatusMaske_ErstzugangWiederzugangMitFrageSelbst: {
            /*Erstzugang/WiederzugangMitFrage - Sind Sie - selbst?*/

            btnSuchen.setVisible(false);

            lblAnweisung.setVisible(false);
            tfScanFeld.setVisible(false);

            lblV1.setVisible(false);
            lblV2.setVisible(false);
            lblV3.setVisible(false);

            lblName.setVisible(false);
            tfName.setVisible(false);
            lblVorname.setVisible(false);
            tfVorname.setVisible(false);
            lblOrt.setVisible(false);
            tfOrt.setVisible(false);

            lblZ0.setVisible(true);
            lblZ1.setVisible(true);
            lblZ2.setVisible(true);
            lblZ3.setVisible(true);
            lblZ4.setVisible(true);
            lblZ5.setVisible(true);
            lblZ6.setVisible(true);
            lblZ7.setVisible(true);
            if (lEclMeldung.klasse == 1) {/*Aktionär*/
                lblZ0.setText("");
                if (ParamS.paramGeraet.akkreditierungVertreterErfassungAktiv) {
                    lblZ1.setText("Sind Sie " + lEclMeldung.vorname + " " + lEclMeldung.name + "?");
                    lblZ2.setText("Aktionär persönlich?");
                } else {
                    lblZ1.setText("Aktionär " + lEclMeldung.vorname + " " + lEclMeldung.name + "");
                    lblZ2.setText("Vollmacht prüfen, Eintrittskarte einbehalten");
                }
                lblZ3.setText("EK-Nummer: " + zutrittsIdent.toString(0));
                String hAktienzahl = "Aktien: " + lEclMeldung.stueckAktien;
                if (ParamS.param.paramBasis.mehrereGattungenAktiv()) {
                    hAktienzahl += " " + ParamS.param.paramBasis.getGattungBezeichnung(lEclMeldung.liefereGattung());
                }
                hAktienzahl += " im " + KonstBesitzart.getText(lEclMeldung.besitzart);
                lblZ4.setText(hAktienzahl);
                lblZ5.setText("Name: " + lEclMeldung.name);
                lblZ6.setText("Vorname: " + lEclMeldung.vorname);
                lblZ7.setText("Ort: " + lEclMeldung.ort);

                boolean stimmkartenErfassenFuerAktionaer = stimkartenErfassenFuerPerson(
                        gwePraesenzStatusabfrageRC.stimmkartenZuordnungenAktionaer.get(0));

                if (stimmkartenErfassenFuerAktionaer) {
                    if (ParamS.paramGeraet.akkreditierungVertreterErfassungAktiv) {
                        btnLinks.setText("Ja / Weiter");
                    } else {
                        btnLinks.setText("Weiter");
                    }
                } else { //es werden keine Stimmkarten zugeordnet
                    if (ParamS.paramGeraet.akkreditierungVertreterErfassungAktiv) {
                        if (aktion == KonstWillenserklaerung.erstzugang) {
                            btnLinks.setText("Ja / Erstzugang buchen");
                        } else {
                            btnLinks.setText("Ja / Wiederzugang buchen");
                        }
                    } else {
                        if (aktion == KonstWillenserklaerung.erstzugang) {
                            btnLinks.setText("Erstzugang buchen");
                        } else {
                            btnLinks.setText("Wiederzugang buchen");
                        }
                    }
                }
                btnLinks.setStyle("-fx-background-color: #00ff00");
                btnLinks.setVisible(true);
                btnLinks.requestFocus();

                if (ParamS.paramGeraet.akkreditierungVertreterErfassungAktiv
                        && ParamS.param.paramAkkreditierung.beiZugangSelbstVertreterIgnorierenZulaessig == 1) {
                    btnMitte.setText("Nein");
                    btnMitte.setStyle("-fx-background-color: #ff0000");
                    btnMitte.setVisible(true);
                } else {
                    btnMitte.setVisible(false);
                }
                zeigeVertreterScroll();
            }

            if (lEclMeldung.klasse == 0) {/*Gast*/
                lblZ0.setText("");
                lblZ1.setText("Sind Sie " + lEclMeldung.vorname + " " + lEclMeldung.name + "?");
                lblZ2.setText("Gast?");
                lblZ3.setText("Gastkarten-Nummer: " + zutrittsIdent.toString(0));
                if (stimmkarten[0].isEmpty()) {
                    lblZ4.setText("");
                } else {
                    lblZ4.setText("Stimmkarten-Nummer: " + stimmkarten[0]);
                }
                lblZ5.setText("Name: " + lEclMeldung.name);
                lblZ6.setText("Vorname: " + lEclMeldung.vorname);
                lblZ7.setText("Ort: " + lEclMeldung.ort);

                btnLinks.setText("Ja / Erstzugang buchen");

                btnLinks.setStyle("-fx-background-color: #00ff00");
                btnLinks.setVisible(true);
                btnLinks.requestFocus();

                btnMitte.setVisible(false);
            }

            btnRechts.setText("Abbruch");
            btnRechts.setStyle("-fx-background-color: #A7A7A7");
            btnRechts.setVisible(true);

            if (ParamS.paramGeraet.akkreditierungScanFeldFuerBestaetigungAktiv) {
                tfScanBestaetigen.setVisible(true);
                tfScanBestaetigen.setText("");
                tfScanBestaetigen.requestFocus();
            }

            btnAbstimmungsModus.setVisible(false);
            btnLabelNachdruck.setVisible(false);

            break;
        }

        case konstStatusMaske_ErstzugangWiederzugangMitFrageVertreter: {/*Erstzugang//WiederzugangMitFrage - Sind Sie - Vertreter?*/
            btnSuchen.setVisible(false);

            lblAnweisung.setVisible(false);
            tfScanFeld.setVisible(false);

            lblV1.setVisible(false);
            lblV2.setVisible(false);
            lblV3.setVisible(false);

            lblName.setVisible(false);
            tfName.setVisible(false);
            lblVorname.setVisible(false);
            tfVorname.setVisible(false);
            lblOrt.setVisible(false);
            tfOrt.setVisible(false);

            lblZ0.setVisible(true);
            lblZ1.setVisible(true);
            lblZ2.setVisible(true);
            lblZ3.setVisible(true);
            lblZ4.setVisible(true);
            lblZ5.setVisible(true);
            lblZ6.setVisible(true);
            lblZ7.setVisible(true);
            lblZ0.setText("");
            lblZ1.setText("Sind Sie "
                    + aktionaerVollmachten[aktionaerVollmachtenAktuelleAbfrage].bevollmaechtigtePerson.vorname + " "
                    + aktionaerVollmachten[aktionaerVollmachtenAktuelleAbfrage].bevollmaechtigtePerson.name + "?");
            lblZ2.setText("Vertreter?");
            lblZ3.setText("EK-Nummer: " + zutrittsIdent.toString(0));
            lblZ4.setText(
                    "Aktien: " + lEclMeldung.stueckAktien + " im " + KonstBesitzart.getText(lEclMeldung.besitzart));
            lblZ5.setText("Name: " + lEclMeldung.name);
            lblZ6.setText("Vorname: " + lEclMeldung.vorname);
            lblZ7.setText("Ort: " + lEclMeldung.ort);

            if (logDrucken == 10) {
                CaBug.druckeInfo("CtrlHauptStage Frontoffice.setzeAnzeige aktionaerVollmachtenAktuelleAbfrage="
                        + aktionaerVollmachtenAktuelleAbfrage);
            }
            boolean stimmkartenErfassenFuerVertreter = stimkartenErfassenFuerPerson(
                    gwePraesenzStatusabfrageRC.stimmkartenZuordnungenVollmachten
                            .get(0)[aktionaerVollmachtenAktuelleAbfrage]);
            if (logDrucken == 10) {
                CaBug.druckeInfo("CtrlHauptStage Frontoffice.setzeAnzeige stimmkartenErfassenFuerVertreter="
                        + stimmkartenErfassenFuerVertreter);
            }
            if (stimmkartenErfassenFuerVertreter) {
                btnLinks.setText("Ja / Weiter");
            } else { //keine Zuordnung erforderlich
                if (aktion == KonstWillenserklaerung.erstzugang) {
                    btnLinks.setText("Ja / Erstzugang buchen");
                } else {
                    btnLinks.setText("Ja / Wiederzugang buchen");
                }
            }
            btnLinks.setStyle("-fx-background-color: #00ff00");
            btnLinks.setVisible(true);
            btnLinks.requestFocus();

            if (ParamS.param.paramAkkreditierung.beiZugangSelbstVertreterIgnorierenZulaessig == 1) {
                btnMitte.setText("Nein");
                btnMitte.setStyle("-fx-background-color: #ff0000");
                btnMitte.setVisible(true);
            }

            btnRechts.setText("Abbruch");
            btnRechts.setStyle("-fx-background-color: #A7A7A7");
            btnRechts.setVisible(true);

            if (ParamS.paramGeraet.akkreditierungScanFeldFuerBestaetigungAktiv) {
                tfScanBestaetigen.setVisible(true);
                tfScanBestaetigen.setText("");
                tfScanBestaetigen.requestFocus();
            }

            btnAbstimmungsModus.setVisible(false);
            zeigeVertreterScroll();
            btnLabelNachdruck.setVisible(false);

            break;
        }

        case konstStatusMaske_ErstzugangWiederzugangVertreterEingabe: {/*Erstzugang/WiederzugangMitFrage - Vertreter-Eingabe*/
            btnSuchen.setVisible(false);

            lblAnweisung.setVisible(false);
            tfScanFeld.setVisible(false);

            lblV1.setVisible(true);
            lblV2.setVisible(true);
            lblV3.setVisible(true);
            lblV1.setText("Vertreter erfassen:");
            lblV2.setText("EK-Nummer: " + zutrittsIdent.toString(0));
            lblV3.setText(lEclMeldung.vorname + " " + lEclMeldung.name + ", Aktien: " + lEclMeldung.stueckAktien
                    + " im " + KonstBesitzart.getText(lEclMeldung.besitzart));

            lblName.setVisible(true);
            tfName.setVisible(true);
            tfName.requestFocus();
            lblVorname.setVisible(true);
            tfVorname.setVisible(true);
            lblOrt.setVisible(true);
            tfOrt.setVisible(true);

            lblZ0.setVisible(false);
            lblZ1.setVisible(false);
            lblZ2.setVisible(false);
            lblZ3.setVisible(false);
            lblZ4.setVisible(false);
            lblZ5.setVisible(false);
            lblZ6.setVisible(false);
            lblZ7.setVisible(false);

            lblZ0.setText("");
            lblZ1.setText("");
            lblZ2.setText("");
            lblZ3.setText("");
            lblZ4.setText("");
            lblZ5.setText("");
            lblZ6.setText("");
            lblZ7.setText("");

            if (aktion == KonstWillenserklaerung.erstzugang) {
                btnLinks.setText("Erstzugang buchen");
            } else {
                btnLinks.setText("Wiederzugang buchen");
            }
            btnLinks.setVisible(true);

            btnMitte.setText("");
            btnMitte.setVisible(false);

            btnRechts.setText("Abbruch");
            btnRechts.setVisible(true);

            tfScanBestaetigen.setVisible(false);
            btnAbstimmungsModus.setVisible(false);

            zeigeVollmachtenDritteScroll();
            btnLabelNachdruck.setVisible(false);

            break;
        }

        case konstStatusMaske_StimmkarteNeuEingeben: { /*Stimmkarte einlesen*/
            btnSuchen.setVisible(false);

            /* Z2 = Aktionärsdaten; Z3=ggf. Vertreterdaten
             * Z4, Z5, Z6, Z7, Z8 = Stimmmaterial
             */

            lblAnweisung.setVisible(true);
            lblAnweisung
                    .setText("Stimmkartennummer für Eintrittskartennummer " + zutrittsIdent.toString(0) + " erfassen:");
            tfScanFeld.setVisible(false);
            tfScanFeld.setText("");

            lblV1.setVisible(false);
            lblV2.setVisible(false);
            lblV3.setVisible(false);

            lblName.setVisible(false);
            tfName.setVisible(false);
            lblVorname.setVisible(false);
            tfVorname.setVisible(false);
            lblOrt.setVisible(false);
            tfOrt.setVisible(false);

            lblZ0.setVisible(false);
            lblZ1.setVisible(false);

            lblZ2.setVisible(true);
            lblZ2.setText("Aktionär: " + lEclMeldung.name + " " + lEclMeldung.vorname + " " + lEclMeldung.ort);
            if (personNatJurVertreter != -1) {
                lblZ3.setVisible(true);
                lblZ3.setText("Vertreter: " + vertreterName + " " + vertreterVorname + " " + vertreterOrt);
            } else {
                lblZ3.setVisible(false);
            }
            lblZ4.setVisible(false);
            lblZ5.setVisible(false);
            lblZ6.setVisible(false);
            lblZ7.setVisible(false);

            if (aktion == KonstWillenserklaerung.erstzugang) {
                btnLinks.setText("Erstzugang buchen");
            } else {
                btnLinks.setText("Wiederzugang buchen");
            }
            btnLinks.setVisible(true);

            btnMitte.setVisible(false);

            btnRechts.setText("Abbruch");
            btnRechts.setVisible(true);

            tfScanBestaetigen.setVisible(false);
            btnAbstimmungsModus.setVisible(false);

            scrpnVertreter.setVisible(false);
            enableStimmkartenFelder();
            btnLabelNachdruck.setVisible(false);

            break;
        }

        case konstStatusMaske_Abgang: { /*Abgang*/
            btnSuchen.setVisible(false);

            lblAnweisung.setVisible(false);
            tfScanFeld.setVisible(false);

            lblV1.setVisible(false);
            lblV2.setVisible(false);
            lblV3.setVisible(false);

            lblName.setVisible(false);
            tfName.setVisible(false);
            lblVorname.setVisible(false);
            tfVorname.setVisible(false);
            lblOrt.setVisible(false);
            tfOrt.setVisible(false);

            lblZ0.setVisible(true);
            lblZ1.setVisible(true);
            lblZ2.setVisible(true);
            lblZ3.setVisible(true);
            lblZ4.setVisible(true);
            lblZ5.setVisible(true);
            lblZ6.setVisible(true);
            lblZ7.setVisible(true);

            if (lEclMeldung.klasse == 1) {/*Aktionär*/
                if (zugangskarteGelesen == 0) {
                    lblZ0.setText("Anwesend");
                } else {
                    lblZ0.setText("Bereits anwesend -> Sonderschalter?");
                }
                int durchVertreter = 0;
                if (lEclMeldung.vertreterName.isEmpty()) {
                    durchVertreter = 0;
                } else {
                    durchVertreter = 1;
                }
                if (durchVertreter == 0) {
                    lblZ1.setText("Selbst");
                } else {
                    lblZ1.setText("Vertreter: ja");
                }
                lblZ2.setText("EK-Nummer: " + zutrittsIdent.toString(0));
                if (stimmkarten[0].isEmpty()) {
                    lblZ3.setText("");
                } else {
                    lblZ3.setText("SK-Nummer: " + stimmkarten[0]);

                }
                lblZ4.setText(
                        "Aktien: " + lEclMeldung.stueckAktien + " im " + KonstBesitzart.getText(lEclMeldung.besitzart));
                if (durchVertreter == 0) {
                    lblZ5.setText("Name: " + lEclMeldung.name);
                    lblZ6.setText("Vorname: " + lEclMeldung.vorname);
                    lblZ7.setText("Ort: " + lEclMeldung.ort);
                } else {
                    lblZ5.setText("Name: " + lEclMeldung.vertreterName);
                    lblZ6.setText("Ort: " + lEclMeldung.vertreterOrt);
                    lblZ7.setText("");
                }
            }
            if (lEclMeldung.klasse == 0) {/*Gast*/
                if (zugangskarteGelesen == 0) {
                    lblZ0.setText("Anwesend");
                } else {
                    if (ParamS.param.paramAkkreditierung.gaesteKartenHabenWiederzugangAbgangsCode) {
                        lblZ0.setText("Bereits anwesend -> Sonderschalter?");
                    } else {
                        lblZ0.setText("");
                    }
                }
                lblZ1.setText("Gast");
                lblZ2.setText("Gastkarten-Nummer:" + zutrittsIdent.toString(0));
                lblZ3.setText(""); /*Hier ggf. Stimmkartennummer*/
                if (stimmkarten[0].isEmpty()) {
                    lblZ4.setText("");
                } else {
                    lblZ4.setText("Stimmkarten-Nummer: " + stimmkarten[0]);
                }
                lblZ5.setText("Name: " + lEclMeldung.name);
                lblZ6.setText("Vorname: " + lEclMeldung.vorname);
                lblZ7.setText("Ort: " + lEclMeldung.ort);
            }

            btnLinks.setText("Abgang buchen");
            btnLinks.setStyle("-fx-background-color: ff0000");
            btnLinks.setVisible(true);

            if (auchVollmachtwechsel == 0 || ParamS.paramGeraet.akkreditierungVertreterErfassungAktiv == false
                    || ParamS.param.paramAkkreditierung.beiZugangSelbstVertreterIgnorierenZulaessig == 0) {
                btnMitte.setVisible(false);
            } else {
                btnMitte.setStyle("-fx-background-color: #00ff00");
                btnMitte.setText("Vertreterwechsel");
                btnMitte.setVisible(true);
            }

            btnRechts.setText("Abbruch");
            btnRechts.setStyle("-fx-background-color: #A7A7A7");
            btnRechts.setVisible(true);
            btnRechts.requestFocus();

            if (ParamS.paramGeraet.akkreditierungScanFeldFuerBestaetigungAktiv) {
                tfScanBestaetigen.setVisible(true);
                tfScanBestaetigen.setText("");
                tfScanBestaetigen.requestFocus();
            }

            btnAbstimmungsModus.setVisible(false);
            btnLabelNachdruck.setVisible(false);

            break;
        }

        case konstStatusMaske_WiederzugangOhneFrage: { /*Wiederzugang*/
            btnSuchen.setVisible(false);

            lblAnweisung.setVisible(false);
            tfScanFeld.setVisible(false);

            lblV1.setVisible(false);
            lblV2.setVisible(false);
            lblV3.setVisible(false);

            lblName.setVisible(false);
            tfName.setVisible(false);
            lblVorname.setVisible(false);
            tfVorname.setVisible(false);
            lblOrt.setVisible(false);
            tfOrt.setVisible(false);

            lblZ0.setVisible(true);
            lblZ1.setVisible(true);
            lblZ2.setVisible(true);
            lblZ3.setVisible(true);
            lblZ4.setVisible(true);
            lblZ5.setVisible(true);
            lblZ6.setVisible(true);
            lblZ7.setVisible(true);

            if (lEclMeldung.klasse == 1) {/*Aktionär*/
                lblZ0.setText("Wiederzugang?");
                int durchVertreter = 0;
                if (personNatJurVertreter == -1) {
                    durchVertreter = 0;
                } else {
                    durchVertreter = 1;
                }
                if (durchVertreter == 0) {
                    lblZ1.setText("Selbst");
                } else {
                    lblZ1.setText("Vertreter: ja");
                }
                lblZ2.setText("EK-Nummer:" + zutrittsIdent.toString(0));
                if (stimmkarten[0].isEmpty()) {
                    lblZ3.setText("");
                } else {
                    lblZ3.setText("SK-Nummer:" + stimmkarten[0]);
                }
                lblZ4.setText(
                        "Aktien: " + lEclMeldung.stueckAktien + " im " + KonstBesitzart.getText(lEclMeldung.besitzart));
                if (durchVertreter == 0) {
                    lblZ5.setText("Name: " + lEclMeldung.name);
                    lblZ6.setText("Vorname: " + lEclMeldung.vorname);
                    lblZ7.setText("Ort: " + lEclMeldung.ort);
                } else {
                    lblZ5.setText("Name: " + vorbestimmteEclPersonNatJur.bevollmaechtigtePerson.name);
                    lblZ6.setText("Vorname: " + vorbestimmteEclPersonNatJur.bevollmaechtigtePerson.vorname);
                    lblZ7.setText("Ort: " + vorbestimmteEclPersonNatJur.bevollmaechtigtePerson.ort);
                }
            }
            if (lEclMeldung.klasse == 0) {/*Gast*/
                lblZ0.setText("Wiederzugang?");
                lblZ1.setText("Gast");
                lblZ2.setText("Gastkarten-Nummer:" + zutrittsIdent.toString(0));
                lblZ3.setText("");
                if (stimmkarten[0].isEmpty()) {
                    lblZ4.setText("");
                } else {
                    lblZ4.setText("Stimmkarten-Nummer: " + stimmkarten[0]);
                }
                lblZ5.setText("Name: " + lEclMeldung.name);
                lblZ6.setText("Vorname: " + lEclMeldung.vorname);
                lblZ7.setText("Ort: " + lEclMeldung.ort);
            }

            btnLinks.setText("Wiederzugang buchen");
            btnLinks.setVisible(true);
            btnLinks.setStyle("-fx-background-color: #00ff00");
            btnLinks.requestFocus();

            btnMitte.setVisible(false);

            btnRechts.setText("Abbrechen");
            btnRechts.setStyle("-fx-background-color: #A7A7A7");
            btnRechts.setVisible(true);

            if (ParamS.paramGeraet.akkreditierungScanFeldFuerBestaetigungAktiv) {
                tfScanBestaetigen.setVisible(true);
                tfScanBestaetigen.setText("");
                tfScanBestaetigen.requestFocus();
            }

            btnAbstimmungsModus.setVisible(false);
            btnLabelNachdruck.setVisible(false);

            break;
        }

        case konstStatusMaske_VertreterwechselEingabeVertreter: {/*Vertreterwechsel - Vertreter-Eingabe*/
            btnSuchen.setVisible(false);

            lblAnweisung.setVisible(false);
            tfScanFeld.setVisible(false);

            lblV1.setVisible(true);
            lblV2.setVisible(true);
            lblV3.setVisible(true);
            lblV1.setText("Vertreter erfassen:");
            lblV2.setText("EK-Nummer:" + zutrittsIdent.toString(0));
            lblV3.setText(lEclMeldung.vorname + " " + lEclMeldung.name + ", Aktien: " + lEclMeldung.stueckAktien
                    + " im " + KonstBesitzart.getText(lEclMeldung.besitzart));

            lblName.setVisible(true);
            tfName.setVisible(true);
            tfName.requestFocus();
            lblVorname.setVisible(true);
            tfVorname.setVisible(true);
            lblOrt.setVisible(true);
            tfOrt.setVisible(true);

            lblZ0.setVisible(false);
            lblZ1.setVisible(false);
            lblZ2.setVisible(false);
            lblZ3.setVisible(false);
            lblZ4.setVisible(false);
            lblZ5.setVisible(false);
            lblZ6.setVisible(false);
            lblZ7.setVisible(false);
            if (lEclMeldung.klasse == 1) {/*Aktionär*/

                lblZ0.setText("");
                lblZ1.setText("");
                lblZ2.setText("");
                lblZ3.setText("");
                lblZ4.setText("");
                lblZ5.setText("");
                lblZ6.setText("");
                lblZ7.setText("");
            }

            btnLinks.setText("Wechsel buchen");
            btnLinks.setStyle("-fx-background-color: #00ff00");
            btnLinks.setVisible(true);

            btnMitte.setVisible(false);

            btnRechts.setText("Abbruch");
            btnRechts.setStyle("-fx-background-color: #ff0000");
            btnRechts.setVisible(true);

            btnAbstimmungsModus.setVisible(false);
            tfScanBestaetigen.setVisible(false);

            zeigeVollmachtenDritteScroll();
            btnLabelNachdruck.setVisible(false);

            break;
        }

        case konstStatusMaske_Buendeln: {/*Bündeln*/
            btnSuchen.setVisible(false);

            lblAnweisung.setVisible(true);
            lblAnweisung.setText("Bitte Bündeln! Protokoll-Nr. " + lWEPraesenzBuchenRC.buendelnProtokollNr);

            tfScanFeld.setVisible(false);

            lblV1.setVisible(false);
            lblV2.setVisible(false);
            lblV3.setVisible(false);

            lblName.setVisible(false);
            tfName.setVisible(false);
            lblVorname.setVisible(false);
            tfVorname.setVisible(false);
            lblOrt.setVisible(false);
            tfOrt.setVisible(false);

            lblZ0.setVisible(false);
            lblZ1.setVisible(false);
            lblZ2.setVisible(false);
            lblZ3.setVisible(false);
            lblZ4.setVisible(false);
            lblZ5.setVisible(false);
            lblZ6.setVisible(false);
            lblZ7.setVisible(false);
            lblZ0.setText("");
            lblZ1.setText("");
            lblZ2.setText("");
            lblZ3.setText("");
            lblZ4.setText("");
            lblZ5.setText("");
            lblZ6.setText("");
            lblZ7.setText("");

            btnLinks.setText("Bündeln durchgeführt");
            btnLinks.setVisible(true);

            btnMitte.setVisible(false);

            btnRechts.setText("");
            btnRechts.setVisible(false);

            tfScanBestaetigen.setVisible(false);
            btnAbstimmungsModus.setVisible(false);
            btnLabelNachdruck.setVisible(false);

            break;
        }

        case konstStatusMaske_Fehlersituation: { /*Fehlersituation*/
            btnSuchen.setVisible(false);

            lblAnweisung.setVisible(false);
            tfScanFeld.setVisible(false);

            lblV1.setVisible(false);
            lblV2.setVisible(false);
            lblV3.setVisible(false);

            lblName.setVisible(false);
            tfName.setVisible(false);
            lblVorname.setVisible(false);
            tfVorname.setVisible(false);
            lblOrt.setVisible(false);
            tfOrt.setVisible(false);

            lblZ0.setVisible(true);
            lblZ1.setVisible(true);
            lblZ2.setVisible(true);
            lblZ3.setVisible(true);
            lblZ4.setVisible(true);
            lblZ5.setVisible(true);
            lblZ6.setVisible(true);
            lblZ7.setVisible(true);

            if (lEclMeldung.klasse == 1) {/*Aktionär*/
                if (istAnwesend) {
                    lblZ0.setText("Fehler: Anwesend");
                } else {
                    lblZ0.setText("Fehler: Abwesend");
                }
                int durchVertreter = 0;
                if (lEclMeldung.vertreterName.isEmpty()) {
                    durchVertreter = 0;
                } else {
                    durchVertreter = 1;
                }
                if (durchVertreter == 0) {
                    lblZ1.setText("Selbst");
                } else {
                    lblZ1.setText("Vertreter: ja");
                }
                lblZ2.setText("EK-Nummer: " + zutrittsIdent.toString(0));
                if (stimmkarten[0].isEmpty()) {
                    lblZ3.setText("");
                } else {
                    lblZ3.setText("SK-Nummer: " + stimmkarten[0]);

                }
                lblZ4.setText(
                        "Aktien: " + lEclMeldung.stueckAktien + " im " + KonstBesitzart.getText(lEclMeldung.besitzart));
                if (durchVertreter == 0) {
                    lblZ5.setText("Name: " + lEclMeldung.name);
                    lblZ6.setText("Vorname: " + lEclMeldung.vorname);
                    lblZ7.setText("Ort: " + lEclMeldung.ort);
                } else {
                    lblZ5.setText("Name: " + lEclMeldung.vertreterName);
                    lblZ6.setText("Ort: " + lEclMeldung.vertreterOrt);
                    lblZ7.setText("");
                }
            }
            if (lEclMeldung.klasse == 0) {/*Gast*/
                if (istAnwesend) {
                    lblZ0.setText("Fehler: Anwesend");
                } else {
                    lblZ0.setText("Fehler: Abwesend");
                }
                lblZ1.setText("Gast");
                lblZ2.setText("Gastkarten-Nummer:" + zutrittsIdent.toString(0));
                lblZ3.setText(""); /*Hier ggf. Stimmkartennummer*/
                lblZ4.setText("");
                lblZ5.setText("Name: " + lEclMeldung.name);
                lblZ6.setText("Vorname: " + lEclMeldung.vorname);
                lblZ7.setText("Ort: " + lEclMeldung.ort);
            }

            btnLinks.setVisible(false);
            btnMitte.setVisible(false);

            btnRechts.setText("Abbruch");
            btnRechts.setStyle("-fx-background-color: #A7A7A7");
            btnRechts.setVisible(true);
            btnRechts.requestFocus();

            tfScanBestaetigen.setVisible(false);
            btnAbstimmungsModus.setVisible(false);
            btnLabelNachdruck.setVisible(false);

            break;
        }

        case konstStatusMaske_ZugangAbgangAppIdent: { /*AppIdent*/
            btnSuchen.setVisible(false);

            setzeAnzeige21AppIdent();
            break;
        }

        }
    }

    /**
     * Setze anzeige 21 app ident.
     */
    private void setzeAnzeige21AppIdent() {
        /*Anzuzeigen ist:
         * lblZ0:
         * 		Falls Zugang: "Sind Sie?"
         * 		Falls Abgang: "Abgang:"
         * lblZ1:
         * 		Name, Vorname, Ort (der Person)
         * lblZ2:
         * 		Sie vertreten folgende Karten:
         * pnApp
         * Spalte 1:
         * > Zeile 1:
         * 		> Eintrittskartennummer
         * 		> "Gast" / "Aktionär" / "Vertreter"
         * > Zeile 2:
         * 		> Name, Vorname
         * > Zeile 3: Ort
         * > Zeile 4, falls Aktionär: Aktien
         * Spalte 2:
         * >	Anzuhaken "Vollmacht wurde vorgelegt" (mehrzeilig)
         * Spalte 3:
         * > Zuzuordnende Stimmkarten (Untereinander) 1 - 2 - 3
         * Spalte 4:
         * > in der App zugeordneter Teilnehmer (so abweichend von App-teilnehmernummer)
         */

        lblAnweisung.setVisible(false);
        tfScanFeld.setVisible(false);

        lblV1.setVisible(false);
        lblV2.setVisible(false);
        lblV3.setVisible(false);

        lblName.setVisible(false);
        tfName.setVisible(false);
        lblVorname.setVisible(false);
        tfVorname.setVisible(false);
        lblOrt.setVisible(false);
        tfOrt.setVisible(false);

        lblZ0.setVisible(true);
        switch (aktion) {
        case KonstWillenserklaerung.erstzugang:
            lblZ0.setText("Zugang:");
            break;
        case KonstWillenserklaerung.wiederzugang:
            lblZ0.setText("Wiederzugang:");
            break;
        case KonstWillenserklaerung.abgang:
            lblZ0.setText("Abgang:");
            break;
        default:
            lblZ0.setText("Buchung nicht möglich => Sonderschalter");
            break;
        }
        lblZ1.setVisible(true);
        //		String hPerson="";
        //		hPerson=Integer.toString(gwePraesenzStatusabfrageRC.appIdentPersonNatJur.ident)+" ";
        //		hPerson+=gwePraesenzStatusabfrageRC.appIdentPersonNatJur.name+" ";
        //		hPerson+=gwePraesenzStatusabfrageRC.appIdentPersonNatJur.vorname+" ";
        //		hPerson+=gwePraesenzStatusabfrageRC.appIdentPersonNatJur.ort+" ";
        //		lblZ1.setText(hPerson);
        lblZ1.setText("");
        lblZ2.setVisible(true);
        lblZ2.setText("Sie vertreten folgende Karten:");

        grpnAppTeilnehmer = new GridPane();
        grpnAppTeilnehmer.setGridLinesVisible(true);
        grpnAppTeilnehmer.setVgap(10);
        grpnAppTeilnehmer.setHgap(20);
        Label uLabel1 = new Label("Meldung");
        grpnAppTeilnehmer.add(uLabel1, 0, 0);
        Label uLabel2 = new Label("Aktion");
        grpnAppTeilnehmer.add(uLabel2, 1, 0);
        Label uLabel4 = new Label("Stimmaterial");
        grpnAppTeilnehmer.add(uLabel4, 2, 0);
        Label uLabel3 = new Label("Bevollmächtigte");
        grpnAppTeilnehmer.add(uLabel3, 3, 0);

        int anzMeldungen = gwePraesenzStatusabfrageRC.meldungen.size();

        ckBxVollmachtVorgelegt = new CheckBox[anzMeldungen];
        ckBxPersonUeberprueft = new CheckBox[anzMeldungen];
        ckBxNichtBuchen = new CheckBox[anzMeldungen];
        btnVollmachtEingeben = new Button[anzMeldungen];
        lTfStimmkarte = new TextField[anzMeldungen][4];
        vertreterVBox = new VBox[anzMeldungen];

        /*Vertreterdaten initialisieren*/

        appVertreterName = new String[anzMeldungen];
        appVertreterVorname = new String[anzMeldungen];
        appVertreterOrt = new String[anzMeldungen];
        appVollmachtPersonenNatJurIdent = new int[anzMeldungen];
        for (int i = 0; i < anzMeldungen; i++) {
            appVertreterName[i] = "";
            appVertreterVorname[i] = "";
            appVertreterOrt[i] = "";
            appVollmachtPersonenNatJurIdent[i] = -1;
        }
        appLetzterVertreterName = "";
        appLetzterVertreterVorname = "";
        appLetzterVertreterOrt = "";
        appLetzterVertreterIdent = -1;

        for (int i = 0; i < anzMeldungen; i++) {
            EclMeldung lMeldung = gwePraesenzStatusabfrageRC.meldungen.get(i);

            /*Spalte 1: ZutrittsIdent/Art; Name, Vorname; Ort*/
            VBox lVBox = new VBox();
            String hText = "";
            hText = lMeldung.zutrittsIdent + " ";
            if (lMeldung.meldungIstEinGast()) {
                hText += "Gast";
            } else {
                //				if (gwePraesenzStatusabfrageRC.appIdentPersonNatJur.ident!=lMeldung.personenNatJurIdent &&
                //						gwePraesenzStatusabfrageRC.appIdentPersonNatJur.ident!=lMeldung.istSelbePersonWieIdent){
                //					hText+="Vertreter";
                //				}
                //				else{hText+="Aktionär Selbst";
                //				}
            }
            Label lLblZutrittsIdent = new Label();
            lLblZutrittsIdent.setText(hText);
            lVBox.getChildren().add(lLblZutrittsIdent);

            Label lLblNameVorname = new Label();
            lLblNameVorname.setText(lMeldung.name + " " + lMeldung.vorname);
            lVBox.getChildren().add(lLblNameVorname);

            Label lLblOrt = new Label();
            lLblOrt.setText(lMeldung.ort);
            lVBox.getChildren().add(lLblOrt);

            if (lMeldung.meldungIstEinAktionaer()) {
                Label lLblAktien = new Label();
                hText = "Aktien: " + lMeldung.stueckAktien + " im " + KonstBesitzart.getText(lMeldung.besitzart);
                lLblAktien.setText(hText);
                lVBox.getChildren().add(lLblAktien);
            }
            grpnAppTeilnehmer.add(lVBox, 0, i + 1);

            /*Spalte 2: Anzuhaken: Vollmacht wurde vorgelegt; ist selbe Person; nicht berücksichtigen*/
            VBox lVBox2 = new VBox();
            lVBox2.setSpacing(10);

            ckBxVollmachtVorgelegt[i] = new CheckBox("Vollmacht wurde vorgelegt");
            if (gwePraesenzStatusabfrageRC.appVollmachtMussNochVorgelegtWerden.get(i)) {
                /*Person kann nicht - ohne weitere Maßnahmen - vertreten*/
                //				if (lMeldung.meldungIstEinAktionaer()){
                //					lVBox2.getChildren().add(ckBxVollmachtVorgelegt[i]);
                //				}
            }

            ckBxPersonUeberprueft[i] = new CheckBox("Person ist identisch");
            if (gwePraesenzStatusabfrageRC.appPersonMussMitAppPersonUeberprueftWerden.get(i)) {
                //				lVBox2.getChildren().add(ckBxPersonUeberprueft[i]);
            }

            ckBxNichtBuchen[i] = new CheckBox("Bei Buchung nicht berücksichtigten");
            if (ParamSpezial.ku178(ParamS.clGlobalVar.mandant) == false) {
                lVBox2.getChildren().add(ckBxNichtBuchen[i]);
            }

            System.out.println("aktion=" + aktion);
            if (aktion == KonstWillenserklaerung.erstzugang) {

                btnVollmachtEingeben[i] = new Button("Vertreter zuordnen");
                if (ParamSpezial.ku178(ParamS.clGlobalVar.mandant) == false) {
                    lVBox2.getChildren().add(btnVollmachtEingeben[i]);
                }
                btnVollmachtEingeben[i].setOnAction(e -> {
                    clickedBtnAppVollmachtDritte(e);
                });
            }

            grpnAppTeilnehmer.add(lVBox2, 1, i + 1);

            /*Spalte 3: Abfrage der Stimmkartennummern*/
            GridPane grpnAppStimmkarten = new GridPane();
            int zeile = 0;

            for (int i1 = 0; i1 < 4; i1++) {
                lTfStimmkarte[i][i1] = null;
                if (gwePraesenzStatusabfrageRC.stimmkartenZuordnungenAktionaer.get(i)[i1] == 1
                        && aktion == KonstWillenserklaerung.erstzugang) {
                    Label lLblStimmkartenText = new Label(
                            gwePraesenzStatusabfrageRC.stimmkartenZuordnungenText.get(i)[i1]);
                    grpnAppStimmkarten.add(lLblStimmkartenText, 0, zeile);

                    lTfStimmkarte[i][i1] = new TextField();
                    lTfStimmkarte[i][i1].setOnKeyPressed(e -> onKeyPressedTfStimmkarte(e));
                    lTfStimmkarte[i][i1].prefWidth(80);
                    lTfStimmkarte[i][i1].setMinWidth(80);
                    lTfStimmkarte[i][i1].setMaxWidth(80);
                    grpnAppStimmkarten.add(lTfStimmkarte[i][i1], 1, zeile);
                    zeile++;
                }
            }

            grpnAppTeilnehmer.add(grpnAppStimmkarten, 2, i + 1);

            /*Spalte 4: Anzeige des in der App zugeordneten Teilnehmers*/
            //			if (gwePraesenzStatusabfrageRC.appIdentPersonNatJur.ident!=gwePraesenzStatusabfrageRC.appVertretendePerson.get(i).ident){
            //				/*Anzeige der "ausgewählten" Person für diese Meldung, soweit diese von der App-Person abweicht*/
            //				String hPersonMeldung="";
            //				VBox lVBox4=new VBox();
            //				hPersonMeldung=Integer.toString(gwePraesenzStatusabfrageRC.appVertretendePerson.get(i).ident)+" ";
            //				hPersonMeldung+=gwePraesenzStatusabfrageRC.appVertretendePerson.get(i).name;
            //				Label hpLabel1=new Label(hPersonMeldung);
            //				lVBox4.getChildren().add(hpLabel1);
            //				Label hpLabel2=new Label(gwePraesenzStatusabfrageRC.appVertretendePerson.get(i).vorname);
            //				lVBox4.getChildren().add(hpLabel2);
            //				Label hpLabel3=new Label(gwePraesenzStatusabfrageRC.appVertretendePerson.get(i).ort);
            //				lVBox4.getChildren().add(hpLabel3);
            //				grpnAppTeilnehmer.add(lVBox4, 3, i+1);
            //			}

            /*Spalte 4: Anzeige der Vertreter*/
            vertreterVBox[i] = new VBox();
            if (gwePraesenzStatusabfrageRC.aktionaerVollmachten.get(i) != null
                    && gwePraesenzStatusabfrageRC.aktionaerVollmachten.get(i).length > 0) {
                if (aktion == KonstWillenserklaerung.erstzugang) {
                    Label hUberschrift = new Label();
                    hUberschrift.setText("Vorliegende Vollmachten:");
                    vertreterVBox[i].getChildren().add(hUberschrift);

                    for (int i1 = 0; i1 < gwePraesenzStatusabfrageRC.aktionaerVollmachten.get(i).length; i1++) {
                        Label lVertreterName = new Label();
                        lVertreterName.setText(
                                gwePraesenzStatusabfrageRC.aktionaerVollmachten.get(i)[i1].bevollmaechtigtePerson.name
                                        + " " + gwePraesenzStatusabfrageRC.aktionaerVollmachten
                                                .get(i)[i1].bevollmaechtigtePerson.vorname);
                        vertreterVBox[i].getChildren().add(lVertreterName);
                    }
                } else {

                }

            }
            grpnAppTeilnehmer.add(vertreterVBox[i], 3, i + 1);

        }

        pnApp.setStyle("-fx-background-color:transparent;");
        pnApp.setContent(grpnAppTeilnehmer);

        pnApp.setVisible(true);

        btnLinks.setStyle("-fx-background-color: #00ff00");
        switch (aktion) {
        case KonstWillenserklaerung.erstzugang:
            btnLinks.setText("Zugang");
            btnLinks.setStyle("-fx-background-color: #00ff00");
            btnLinks.setVisible(true);
            break;
        case KonstWillenserklaerung.wiederzugang:
            btnLinks.setText("Wiederzugang");
            btnLinks.setStyle("-fx-background-color: #00ff00");
            btnLinks.setVisible(true);
            break;
        case KonstWillenserklaerung.abgang:
            btnLinks.setText("Abgang");
            btnLinks.setStyle("-fx-background-color: #00ff00");
            btnLinks.setVisible(true);
            break;
        default:
            btnLinks.setVisible(false);
            break;
        }

        btnMitte.setVisible(false);

        btnRechts.setText("Abbruch");
        btnRechts.setStyle("-fx-background-color: #A7A7A7");
        btnRechts.setVisible(true);

        btnAbstimmungsModus.setVisible(false);
        btnLabelNachdruck.setVisible(false);

    }

    /**
     * *********Funktionen mit Reaktionen auf
     * Eingabe*********************************.
     *
     * @param event the event
     */

    void clickedBtnVertreterNeueVollmacht(ActionEvent event) {
        statusMaske = konstStatusMaske_ErstzugangWiederzugangVertreterEingabe;
        setzeAnzeige();
        return;
    }

    /**
     * Clicked btn vertreter selbst.
     *
     * @param event the event
     */
    void clickedBtnVertreterSelbst(ActionEvent event) {
        personNatJurVertreter = -1;
        vertreterName = "";
        vertreterVorname = "";
        vertreterOrt = "";
        linksClickNachPersonenauswahlErstzugangWiederzugangVertreterwechsel();
        return;
    }

    /**
     * Clicked btn vertreter.
     *
     * @param event the event
     */
    void clickedBtnVertreter(ActionEvent event) {
        int lfdVertreter = -1;
        for (int i = 0; i < btnVertreter.length; i++) {
            if (event.getSource() == btnVertreter[i]) {
                lfdVertreter = i;
            }
        }

        if (lfdVertreter != -1) {
            vertreterNameSet(aktionaerVollmachten[lfdVertreter].bevollmaechtigtePerson.name);
            vertreterVornameSet(aktionaerVollmachten[lfdVertreter].bevollmaechtigtePerson.vorname);
            tfOrt.setText(aktionaerVollmachten[lfdVertreter].bevollmaechtigtePerson.ort);
            tfName.requestFocus();
        }

        //		personNatJurVertreter=aktionaerVollmachten[lfdVertreter].bevollmaechtigtePerson.ident;
        //		vertreterName=aktionaerVollmachten[lfdVertreter].bevollmaechtigtePerson.name;
        //		vertreterVorname=aktionaerVollmachten[lfdVertreter].bevollmaechtigtePerson.vorname;
        //		vertreterOrt=aktionaerVollmachten[lfdVertreter].bevollmaechtigtePerson.ort;
        //		personNatJurVertreterNeueVollmacht=0;
        //		linksClickNachPersonenauswahlErstzugangWiederzugangVertreterwechsel();
    }

    /** The vollmacht bearbeiten fuer lfd app eintrag. */
    private int vollmachtBearbeitenFuerLfdAppEintrag = 0;

    /**
     * Clicked btn app vollmacht dritte.
     *
     * @param event the event
     */
    void clickedBtnAppVollmachtDritte(ActionEvent event) {
        vollmachtBearbeitenFuerLfdAppEintrag = -1;
        for (int i = 0; i < btnVollmachtEingeben.length; i++) {
            if (event.getSource() == btnVollmachtEingeben[i]) {
                vollmachtBearbeitenFuerLfdAppEintrag = i;
            }
        }

        Stage neuerDialog = new Stage();

        CtrlVertreterApp controllerFenster = new CtrlVertreterApp();

        controllerFenster.init(neuerDialog);
        controllerFenster.vertreterliste = gwePraesenzStatusabfrageRC.aktionaerVollmachten
                .get(vollmachtBearbeitenFuerLfdAppEintrag);
        if (appLetzterVertreterIdent != -1) {
            controllerFenster.letzterVertreterMoeglich = true;
            controllerFenster.vertreterIdent = appLetzterVertreterIdent;
            controllerFenster.vertreterName = appLetzterVertreterName;
            controllerFenster.vertreterVorname = appLetzterVertreterVorname;
            controllerFenster.vertreterOrt = appLetzterVertreterOrt;
        }

        FXMLLoader loader = null;
        String maske = "VertreterApp.fxml";
        loader = new FXMLLoader(getClass().getResource(maske));
        loader.setController(controllerFenster);
        Parent mainPane = null;
        try {
            mainPane = (Parent) loader.load();
        } catch (IOException e) {
            CaBug.drucke("CtrlHauptStage.clickedBtnAppVollmachtDritte 001");
            e.printStackTrace();
        }
        Scene scene = null;
        scene = new Scene(mainPane, 1300, 800);
        neuerDialog.setTitle("Vertreter");
        neuerDialog.setScene(scene);
        neuerDialog.initStyle(StageStyle.UNDECORATED);
        neuerDialog.initModality(Modality.APPLICATION_MODAL);
        neuerDialog.showAndWait();
        if (controllerFenster.abbruch == false) {
            /*Eingegebenen Vertreter übernehmen*/
            appLetzterVertreterIdent = controllerFenster.vertreterIdent;
            appLetzterVertreterName = controllerFenster.vertreterName;
            appLetzterVertreterVorname = controllerFenster.vertreterVorname;
            appLetzterVertreterOrt = controllerFenster.vertreterOrt;

            appVertreterName[vollmachtBearbeitenFuerLfdAppEintrag] = controllerFenster.vertreterName;
            appVertreterVorname[vollmachtBearbeitenFuerLfdAppEintrag] = controllerFenster.vertreterVorname;
            appVertreterOrt[vollmachtBearbeitenFuerLfdAppEintrag] = controllerFenster.vertreterOrt;
            appVollmachtPersonenNatJurIdent[vollmachtBearbeitenFuerLfdAppEintrag] = controllerFenster.vertreterIdent;
            System.out.println(">>>>>>>>>>ontrollerFenster.vertreterIdent=" + controllerFenster.vertreterIdent);

            vertreterVBox[vollmachtBearbeitenFuerLfdAppEintrag].getChildren().clear();

            Label hUberschrift = new Label();
            hUberschrift.setText("Vertreter:");
            vertreterVBox[vollmachtBearbeitenFuerLfdAppEintrag].getChildren().add(hUberschrift);

            Label lVertreterName = new Label();
            lVertreterName.setText(appVertreterName[vollmachtBearbeitenFuerLfdAppEintrag]);
            vertreterVBox[vollmachtBearbeitenFuerLfdAppEintrag].getChildren().add(lVertreterName);

            Label lVertreterVorname = new Label();
            lVertreterVorname.setText(appVertreterVorname[vollmachtBearbeitenFuerLfdAppEintrag]);
            vertreterVBox[vollmachtBearbeitenFuerLfdAppEintrag].getChildren().add(lVertreterVorname);

            Label lVertreterOrt = new Label();
            lVertreterOrt.setText(appVertreterOrt[vollmachtBearbeitenFuerLfdAppEintrag]);
            vertreterVBox[vollmachtBearbeitenFuerLfdAppEintrag].getChildren().add(lVertreterOrt);
        }
    }

    /**
     * Clicked btn vollmacht dritte.
     *
     * @param event the event
     */
    void clickedBtnVollmachtDritte(ActionEvent event) {
        int lfdVertreter = -1;
        for (int i = 0; i < btnVollmachtDritte.length; i++) {
            if (event.getSource() == btnVollmachtDritte[i]) {
                lfdVertreter = i;
            }
        }

        if (lfdVertreter != -1) {
            vertreterNameSet(
                    CInjects.weKIAVFuerVollmachtDritteRC.listKIAVFuerVollmachtDritte.get(lfdVertreter).vertreterName);
            vertreterVornameSet(CInjects.weKIAVFuerVollmachtDritteRC.listKIAVFuerVollmachtDritte
                    .get(lfdVertreter).vertreterVorname);
            tfOrt.setText(
                    CInjects.weKIAVFuerVollmachtDritteRC.listKIAVFuerVollmachtDritte.get(lfdVertreter).vertreterOrt);
            tfName.requestFocus();
        }

        //		personNatJurVertreter=0;
        //		personNatJurVertreterNeueVollmacht=CInjects.weKIAVFuerVollmachtDritteRC.listKIAVFuerVollmachtDritte.get(lfdVertreter).vertreterIdent;
        //		vertreterName=CInjects.weKIAVFuerVollmachtDritteRC.listKIAVFuerVollmachtDritte.get(lfdVertreter).vertreterName;
        //		vertreterVorname=CInjects.weKIAVFuerVollmachtDritteRC.listKIAVFuerVollmachtDritte.get(lfdVertreter).vertreterVorname;
        //		vertreterOrt=CInjects.weKIAVFuerVollmachtDritteRC.listKIAVFuerVollmachtDritte.get(lfdVertreter).vertreterOrt;
        //		
        //		linksClickNachPersonenauswahlErstzugangWiederzugangVertreterwechsel();
    }

    /**
     * On clicked label nachdruck.
     *
     * @param event the event
     */
    @FXML
    void onClickedLabelNachdruck(ActionEvent event) {
        printAlleAktionaersLabel();
    }

    /**
     * ****************Ab hier unsortiert**************************************.
     *
     * @param event the event
     */
    /*ENTER abfangen im ScanFeld*/
    @FXML
    void onKeyPressedScanFeld(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            scanFeldVerarbeiten();
        }
    }

    /**
     * Do scan stimmen.
     *
     * @param feldNummer the feld nummer
     */
    private void doScanStimmen(int feldNummer) {
        if (letzteZuzuordnendeStimmkarte == feldNummer) {
            scanFeldStimmenVerarbeiten();
        }
        int i = feldNummer + 1;
        while (i <= 4) {
            if (stimmkartenEingeben[i] == 1) {
                switch (i) {
                case 1: {
                    tfScanStimmen1.requestFocus();
                    break;
                }
                case 2: {
                    tfScanStimmen2.requestFocus();
                    break;
                }
                case 3: {
                    tfScanStimmen3.requestFocus();
                    break;
                }
                case 4: {
                    tfScanStimmen4.requestFocus();
                    break;
                }
                }
                return;
            }
            i++;

        }

    }

    /**
     * On key pressed tf stimmkarte.
     *
     * @param event the event
     */
    @FXML
    void onKeyPressedTfStimmkarte(KeyEvent event) {
        int gef = -1, gef1 = -1;
        if (event.getCode() == KeyCode.ENTER) {
            for (int i = 0; i < gwePraesenzStatusabfrageRC.meldungen.size(); i++) {
                for (int i1 = 0; i1 < 4; i1++) {
                    if (event.getSource() == lTfStimmkarte[i][i1]) {
                        gef = i;
                        gef1 = i1;
                    }
                }

            }
            if (gef == -1) {
                return;
            }
            while (gef < gwePraesenzStatusabfrageRC.meldungen.size()) {
                while (gef1 < 4) {
                    gef1++;
                    if (lTfStimmkarte[gef][gef1] != null) {
                        System.out.println("Request Focus " + gef + " " + gef1);
                        lTfStimmkarte[gef][gef1].requestFocus();
                        return;
                    }
                }
                gef++;
                gef1 = -1;
            }
        }
    }

    /**
     * On key pressed scan stimmen 0.
     *
     * @param event the event
     */
    @FXML
    void onKeyPressedScanStimmen0(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            /*Sonderverarbeitung ku164*/
            //			System.out.println("SkarteChecken "+statusMaske);
            //			if (statusMaske==6){
            //				String hString=tfScanStimmen0.getText();
            //				System.out.println("hString="+hString);
            //				if (hString.length()>6){
            //					hString=hString.substring(1, 6);
            //					tfScanStimmen0.setText(hString);
            //					System.out.println("hStringNeu="+hString);
            //
            //				}
            //			}
            /*Ende Sonderverarbeitung*/

            doScanStimmen(0);
        }
    }

    /**
     * On key pressed scan stimmen 1.
     *
     * @param event the event
     */
    @FXML
    void onKeyPressedScanStimmen1(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            doScanStimmen(1);
        }
    }

    /**
     * On key pressed scan stimmen 2.
     *
     * @param event the event
     */
    @FXML
    void onKeyPressedScanStimmen2(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            doScanStimmen(2);
        }
    }

    /**
     * On key pressed scan stimmen 3.
     *
     * @param event the event
     */
    @FXML
    void onKeyPressedScanStimmen3(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            doScanStimmen(3);
        }
    }

    /**
     * On key pressed scan stimmen 4.
     *
     * @param event the event
     */
    @FXML
    void onKeyPressedScanStimmen4(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            doScanStimmen(4);
        }
    }

    /**
     * On key pressed bestaetigen.
     *
     * @param event the event
     */
    @FXML
    void onKeyPressedBestaetigen(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            if (tfScanBestaetigen.getText().compareTo(nummerErstScan) != 0) {
                lblFehlerMeldung.setText("Bitte mit gleichem Barcode bestätigen!");
                tfScanBestaetigen.setText("");
                tfScanBestaetigen.requestFocus();
                return;
            } else if (!btnLinks.isDisable()) {
                doBtnLinks();
            }
        }
    }

    /**
     * *************Funktionen zum Ausführen der Willenserklärungen
     * (praesenzBuchen)***************.
     */
    private WEPraesenzBuchen lWEPraesenzBuchen = null;

    /** The l WE praesenz buchen RC. */
    private WEPraesenzBuchenRC lWEPraesenzBuchenRC = null;

    /** The bl client praesenz buchen. */
    private BlClientPraesenzBuchen blClientPraesenzBuchen = null;

    /**
     * Buchen bl init.
     */
    private void buchen_blInit() {
        if (wsClient == null) {
            wsClient = new WSClient();
        }

        blClientPraesenzBuchen = new BlClientPraesenzBuchen();
        blClientPraesenzBuchen.initialisieren(wsClient);
        lWEPraesenzBuchen = blClientPraesenzBuchen.lWEPraesenzBuchen;
    }

    /**
     * Buchen init.
     */
    private void buchen_init() {
        blClientPraesenzBuchen.buchen_init(KonstEingabeQuelle.konventionell_aufHV);
    }

    /**
     * Ggf. Ausdruck eines Formulars oder der Stimmkarten. Ob gedruckt wird, wird in
     * dieser Funktion entschieden. Aktuell nur implementiert: bei Erstzugang, nicht
     * bei App.
     * 
     * pIIdentifikation=Offset in statusRC pIIdentifikationBuchenRC=offset in buchen
     * bzw. buchenRC
     * 
     * Hinweis: in lInitialPasswort steht das passende Initialpasswort, falls
     * vorhanden
     *
     * @param pIIdentifikation         the i identifikation
     * @param pIIdentifikationBuchenRC the i identifikation buchen RC
     */
    private void buchen_formularDruck(int pIIdentifikation, int pIIdentifikationBuchenRC) {
        if (lEclMeldung.klasse == 0) {
            /*Bei Gast keine Druckfunktion*/
            return;
        }
        /*Nur bereitstellen wegen Parameter*/
        DbBundle lDbBundle = new DbBundle();
        if (ParamS.param.paramAkkreditierung.formularNachErstzugang != 0) {

            RpDrucken rpDrucken = new RpDrucken();
            rpDrucken.initClientDrucke();
            rpDrucken.setzeAutoDrucker(
                    ParamS.param.paramBasis.autoDruckerVerwendung[KonstAutoDrucker.FORMULAR_BEI_ERSTZUGANG]);

            rpDrucken.initFormular(lDbBundle);
            RpVariablen rpVariablen = new RpVariablen(lDbBundle);
            rpVariablen.formularErstzugang("01", rpDrucken);

            rpDrucken.startFormular();

            rpVariablen.fuelleVariable(rpDrucken, "Nummern.ZutrittsIdentKurz",
                    lWEPraesenzBuchenRC.zutrittsIdent.get(pIIdentifikationBuchenRC).zutrittsIdent);

            rpVariablen.fuelleVariable(rpDrucken, "Zugangsdaten.Kennung", lEclMeldung.aktionaersnummer);
            rpVariablen.fuelleVariable(rpDrucken, "Zugangsdaten.Initialpasswort", lInitialPasswort);

            rpVariablen.fuelleVariable(rpDrucken, "Aktionaer.Name", lEclMeldung.name);
            rpVariablen.fuelleVariable(rpDrucken, "Aktionaer.Vorname", lEclMeldung.vorname);
            rpVariablen.fuelleVariable(rpDrucken, "Aktionaer.Ort", lEclMeldung.ort);
            rpVariablen.fuelleVariable(rpDrucken, "Aktionaer.Gattung", Integer.toString(lEclMeldung.gattung));

            rpVariablen.fuelleVariable(rpDrucken, "Vertreter.Name", vertreterName);
            rpVariablen.fuelleVariable(rpDrucken, "Vertreter.Vorname", vertreterVorname);
            rpVariablen.fuelleVariable(rpDrucken, "Vertreter.Ort", vertreterOrt);

            rpDrucken.druckenFormular();

            rpDrucken.endeFormular();
        }

        /*Stimmkartendruck*/
        BlStimmkartendruck blStimmkartendruck = new BlStimmkartendruck(false, lDbBundle);
        List<EclMeldung> lMeldungsliste = new LinkedList<EclMeldung>();
        lMeldungsliste.add(lEclMeldung);
        blStimmkartendruck.rcMeldungsliste = lMeldungsliste;

        int gattung = lEclMeldung.gattung;

        for (int i = 1; i <= 4; i++) {
            int sNrOffset = i - 1;
            if (ParamS.param.paramAkkreditierung.pPraesenzStimmkarteAktiv[gattung - 1][sNrOffset] == 1) {
                String hDrucker = ParamS.param.paramAkkreditierung.pPraesenzStimmkarteDruckerBeiErstzugang[gattung
                        - 1][sNrOffset];
                if (!hDrucker.isEmpty()) {
                    int druckerNr = Integer.parseInt(hDrucker);

                    RpDrucken rpDrucken = new RpDrucken();
                    rpDrucken.initClientDrucke();
                    rpDrucken.setzeAutoDrucker(druckerNr);

                    rpDrucken.initFormular(lDbBundle);
                    RpVariablen rpVariablen = new RpVariablen(lDbBundle);
                    rpVariablen.stimmkarten(
                            ParamS.param.paramAkkreditierung.pPraesenzStimmkarteFormularnummer[gattung - 1][sNrOffset],
                            rpDrucken);

                    rpDrucken.startFormular();
                    blStimmkartendruck.drucke1Zu1(rpDrucken, rpVariablen, gattung, i);
                    rpDrucken.endeFormular();

                }
            }

        }
    }

    /** The l label gedruckt. */
    private boolean lLabelGedruckt = false;

    /**
     * Initialisierung des Labeldrucks (konkret: Zurücksetzen des Schalters für das
     * Anzeigen einer Meldung zum Entfernen eines Labels).
     */
    private void buchen_labeldruckinit() {
        lLabelGedruckt = false;
    }

    /**
     * Ggf. Ausdruck eines Labels. Ob gedruckt wird, wird in dieser Methode
     * abgefragt. Für stimmkarteSecond wird grundsätzlich kein Label ausgedruckt.
     * pIIdentifikation=Offset in statusRC pIIdentifikationBuchenRC=offset in buchen
     * bzw. buchenRC
     *
     * @param pIIdentifikation         the i identifikation
     * @param pIIdentifikationBuchenRC the i identifikation buchen RC
     */
    private void buchen_labeldruck(int pIIdentifikation, int pIIdentifikationBuchenRC) {
        for (int j = 0; j < 4; j++) {
            datenLabelPrint[j] = null;
        }
        boolean druckWegenStimmkartenZuordnung = true;

        if (ParamS.paramGeraet.labelDruckerIPAdresse.equals("0")) {
            return;
        } /*Kein Labelprinter angeschlossen*/

        if (lWEPraesenzBuchenRC.rc < 0) {
            return;
        } /*Dann Fehler aufgetreten, Buchung wurde nicht durchgeführt*/

        if (ParamS.param.paramAkkreditierung.labeldruckVerfahren == 0 && stimmkarteEingebenErforderlich == false) {
            return;
        } /*Dann keine Stimmkartenzuordnung (beim Verfahren 0) erfolgt - kein Labeldruck*/

        if (ParamS.param.paramAkkreditierung.labeldruckVerfahren == 1) {
            if (vertreterName.isEmpty()) {
                return;
            } /*Dann kein Vertreter (beim Verfahren 1) eingetragen - kein Labeldruck*/
            druckWegenStimmkartenZuordnung = false;
        }

        if (ParamS.paramGeraet.akkreditierungLabeldruckFuerAktionaer == false) {
            return;
        } /*Labeldruck für Aktionäre nicht aktiviert*/

        if (lEclMeldung.meldungIstEinGast()) {
            return;
        }
        /*Sobald ein Label gedruckt wurde, wird das auf true gesetzt. D.h. wenn dann ein zweites Label gedruckt wird,
         * muß vorher eine Abfrage kommen, damit der Benutzer die Chance hat das Label zu entfernen!*/

        int ende = 4;
        if (druckWegenStimmkartenZuordnung == false) {
            ende = 1;
        }
        for (int j = 0; j < ende; j++) {
            if (stimmkartenEingeben[j] == 1 || druckWegenStimmkartenZuordnung == false) {
                /*Label aufbereiten*/
                datenLabelPrint[j] = new EclLabelPrint();
                DbBundle lDbBundle = new DbBundle();
                BlNummernformen blNummernform = new BlNummernformen(lDbBundle);

                datenLabelPrint[j].ekNummer = "Eintrittskarten Nr. "
                        + lWEPraesenzBuchenRC.zutrittsIdent.get(pIIdentifikationBuchenRC);
                datenLabelPrint[j].skNummer = "Stimmkarten Nr. "
                        + lWEPraesenzBuchenRC.zugeordneteStimmkarten.get(pIIdentifikationBuchenRC)[j];

                if (lWEPraesenzBuchenRC.zugeordneteStimmkarten.get(pIIdentifikationBuchenRC)[j].isEmpty() == false) {
                    blNummernform.rcStimmkarteSubNummernkreis = j;
                    datenLabelPrint[j].skNummerBar = blNummernform.formatiereNrKomplett(
                            lWEPraesenzBuchenRC.zugeordneteStimmkarten.get(pIIdentifikationBuchenRC)[j], "",
                            KonstKartenklasse.stimmkartennummer, KonstKartenart.abgang);
                } else {
                    datenLabelPrint[j].skNummerBar = "";
                }

                datenLabelPrint[j].vornameAktionaer = lEclMeldung.vorname;
                datenLabelPrint[j].nachnameAktionaer = lEclMeldung.name;
                datenLabelPrint[j].ortAktionaer = lEclMeldung.ort;

                datenLabelPrint[j].vornameVertreter = vertreterVorname;
                datenLabelPrint[j].nameVertreter = vertreterName;
                datenLabelPrint[j].ortVertreter = vertreterOrt;
                if (personNatJurVertreter > 0) {
                    int gef = -1;
                    for (int i = 0; i < aktionaerVollmachten.length; i++) {
                        System.out.println("i=" + i + " ident=" + aktionaerVollmachten[i].bevollmaechtigtePerson.ident);
                        if (aktionaerVollmachten[i].bevollmaechtigtePerson.ident == personNatJurVertreter) {
                            gef = i;
                        }
                        if (gef != -1) {
                            datenLabelPrint[j].vornameVertreter = aktionaerVollmachten[gef].bevollmaechtigtePerson.vorname;
                            datenLabelPrint[j].nameVertreter = aktionaerVollmachten[gef].bevollmaechtigtePerson.name;
                            datenLabelPrint[j].ortVertreter = aktionaerVollmachten[gef].bevollmaechtigtePerson.ort;
                        }
                    }
                }
                datenLabelPrint[j].aktienzahl = lEclMeldung.stimmen + " Aktie(n) " + lEclMeldung.besitzart;
            } else {
                datenLabelPrint[j] = null;
            }
        }
        printAlleAktionaersLabel();
    }

    /**
     * Abschlussbehandlung einschließlich Fehlerbehandlung.
     */
    private void buchen_abschlussBehandlung() {
        if (lWEPraesenzBuchenRC.rc >= 0) {
            /*Buchung durchgeführt*/
            if (gwePraesenzStatusabfrageRC.rcKartenklasse != KonstKartenklasse.appIdent) {
                String lZuletztGebucht = "EK: " + lWEPraesenzBuchenRC.zutrittsIdent.get(0).toString(0);
                boolean skAngefuegt = false;
                if (lWEPraesenzBuchenRC.zugeordneteStimmkarten.get(0) != null) {
                    for (int i = 0; i < 4; i++) {
                        if (!lWEPraesenzBuchenRC.zugeordneteStimmkarten.get(0)[i].isEmpty()) {
                            if (skAngefuegt == true) {
                                lZuletztGebucht = lZuletztGebucht + ";";
                            } else {
                                lZuletztGebucht = lZuletztGebucht + " SK: ";
                            }
                            lZuletztGebucht = lZuletztGebucht + lWEPraesenzBuchenRC.zugeordneteStimmkarten.get(0)[i];
                            skAngefuegt = true;
                        }
                    }
                }
                lZuletztGebucht = lZuletztGebucht + " " + lEclMeldung.name;
                tfZuletztGebucht.setText(lZuletztGebucht);

                if (lWEPraesenzBuchenRC.gebuchterVertreterIdent != 0) {
                    letzterVertreterIdent = lWEPraesenzBuchenRC.gebuchterVertreterIdent;
                }
            } else {
                //				String lZuletztGebucht="App: "+gwePraesenzStatusabfrageRC.appIdentPersonNatJur.name+" "+
                //						gwePraesenzStatusabfrageRC.appIdentPersonNatJur.vorname;
                String lZuletztGebucht = "App-Code: ";
                tfZuletztGebucht.setText(lZuletztGebucht);
            }

            if (lWEPraesenzBuchenRC.buendeln && ParamS.paramGeraet.akkreditierungAnzeigeBelegeBuendeln) {
                /*Bündeln durchführen*/
                statusMaske = konstStatusMaske_Buendeln;
                setzeAnzeige();
                return;
            }

            if (eingabeNummerBereitsErfolgt) {
                eigeneStage.hide();
                return;
            }
            statusMaske = konstStatusMaske_EinstiegsCodeEingeben;
            setzeAnzeige();
            return;
        } else {

            /*TODO $_ Fehlerbehandlung nach Buchen Texte noch finalisieren*/
            /*Fehler aufgetreten*/
            switch (lWEPraesenzBuchenRC.rc) {
            case CaFehler.pmStimmkarte1BereitsVerwendet: {
                lblFehlerMeldung.setText("Stimmkarte bereits anderweitig verwendet!");
                break;
            }
            case CaFehler.pmStimmkarte2BereitsVerwendet: {
                lblFehlerMeldung.setText("Stimmkarte 2 bereits anderweitig verwendet!");
                break;
            }
            case CaFehler.pmStimmkarte3BereitsVerwendet: {
                lblFehlerMeldung.setText("Stimmkarte 3 bereits anderweitig verwendet!");
                break;
            }
            case CaFehler.pmStimmkarte4BereitsVerwendet: {
                lblFehlerMeldung.setText("Stimmkarte 4 bereits anderweitig verwendet!");
                break;
            }
            case CaFehler.pmStimmkarteSecondBereitsVerwendet: {
                lblFehlerMeldung.setText("Stimmkarte Second bereits anderweitig verwendet!");
                break;
            }
            default: {
                lblFehlerMeldung
                        .setText(lWEPraesenzBuchenRC.rc + " " + CaFehler.getFehlertext(lWEPraesenzBuchenRC.rc, 0));
                break;
            }
            }
        }

    }

    /**
     * ************Buchung auslösen - "oberste
     * Ebene**********************************.
     */
    /** Buchen Erstzugang */
    private void buchen_erstzugang() {

        aktionen = 0;
        if (gwePraesenzStatusabfrageRC.erforderlicheAktionen.get(0) != null) {
            List<Integer> zulaessigeFunktionenAlle = gwePraesenzStatusabfrageRC.erforderlicheAktionen.get(0);
            if (zulaessigeFunktionenAlle.size() > 0) {
                aktionen = 1;
            }
        }

        buchen_blInit();
        blClientPraesenzBuchen.buchen_erstzugang(KonstEingabeQuelle.konventionell_aufHV, zutrittsIdent,
                zutrittsIdentKartenklasse, stimmkarteEingebenErforderlich, stimmkarten, stimmkartenEingeben,
                lEclMeldung, personNatJurVertreter, personNatJurVertreterNeueVollmacht, vertreterName, vertreterVorname,
                vertreterOrt, aktionen);
        lWEPraesenzBuchenRC = blClientPraesenzBuchen.lWEPraesenzBuchenRC;

        letzterVertreterName = lWEPraesenzBuchen.vertreterName;
        letzterVertreterVorname = lWEPraesenzBuchen.vertreterVorname;
        letzterVertreterOrt = lWEPraesenzBuchen.vertreterOrt;
        letzterVertreterIdent = lWEPraesenzBuchenRC.gebuchterVertreterIdent;

        if (lWEPraesenzBuchenRC.rc == CaFehler.teVerbindungsabbruchWebService) {
            zeigeOfflineWarnung();
        }

        buchen_labeldruckinit();
        buchen_labeldruck(0, 0);
        buchen_formularDruck(0, 0);
        buchen_abschlussBehandlung();
    }

    /**
     * Buchen Wiederzugang.
     */
    private void buchen_wiederzugang() {

        buchen_blInit();
        blClientPraesenzBuchen.buchen_wiederzugang(KonstEingabeQuelle.konventionell_aufHV, zutrittsIdent,
                zutrittsIdentKartenklasse, stimmkarteEingebenErforderlich, stimmkarten, stimmkartenEingeben,
                lEclMeldung, personNatJurVertreter, personNatJurVertreterNeueVollmacht, vertreterName, vertreterVorname,
                vertreterOrt, vorbestimmtePersonNatJur);
        lWEPraesenzBuchenRC = blClientPraesenzBuchen.lWEPraesenzBuchenRC;

        letzterVertreterName = lWEPraesenzBuchen.vertreterName;
        letzterVertreterVorname = lWEPraesenzBuchen.vertreterVorname;
        letzterVertreterOrt = lWEPraesenzBuchen.vertreterOrt;
        letzterVertreterIdent = lWEPraesenzBuchenRC.gebuchterVertreterIdent;

        if (lWEPraesenzBuchenRC.rc == CaFehler.teVerbindungsabbruchWebService) {
            zeigeOfflineWarnung();
        }
        buchen_labeldruckinit();
        buchen_labeldruck(0, 0);
        buchen_abschlussBehandlung();
    }

    /**
     * Buchen Vertreterwechsel.
     */
    private void buchen_vertreterwechsel() {

        buchen_blInit();
        blClientPraesenzBuchen.buchen_vertreterwechsel(KonstEingabeQuelle.konventionell_aufHV, zutrittsIdent,
                zutrittsIdentKartenklasse, stimmkarteEingebenErforderlich, stimmkarten, stimmkartenEingeben,
                lEclMeldung, personNatJurVertreter, personNatJurVertreterNeueVollmacht, vertreterName, vertreterVorname,
                vertreterOrt, vorbestimmtePersonNatJur);
        lWEPraesenzBuchenRC = blClientPraesenzBuchen.lWEPraesenzBuchenRC;

        /*Hinweis: Falls Karte in Sammelkarte, und sonstige erforderliche Aktionen => Sonderschalter*/
        if (lWEPraesenzBuchenRC.rc == CaFehler.teVerbindungsabbruchWebService) {
            zeigeOfflineWarnung();
        }

        letzterVertreterName = lWEPraesenzBuchen.vertreterName;
        letzterVertreterVorname = lWEPraesenzBuchen.vertreterVorname;
        letzterVertreterOrt = lWEPraesenzBuchen.vertreterOrt;
        letzterVertreterIdent = lWEPraesenzBuchenRC.gebuchterVertreterIdent;

        buchen_abschlussBehandlung();
    }

    /**
     * Buchen Abgang.
     */
    private void buchen_abgang() {

        buchen_blInit();
        blClientPraesenzBuchen.buchen_abgang(KonstEingabeQuelle.konventionell_aufHV, zutrittsIdent,
                zutrittsIdentKartenklasse, stimmkarteEingebenErforderlich, stimmkarten, stimmkartenEingeben,
                lEclMeldung);
        lWEPraesenzBuchenRC = blClientPraesenzBuchen.lWEPraesenzBuchenRC;

        /*Hinweis: Falls Karte in Sammelkarte, und sonstige erforderliche Aktionen => Sonderschalter*/
        if (lWEPraesenzBuchenRC.rc == CaFehler.teVerbindungsabbruchWebService) {
            zeigeOfflineWarnung();
        }
        buchen_abschlussBehandlung();

    }

    /**
     * **************Ab hier "neuere" Funktionen zum Buchen der AppIdent********.
     *
     * @param pIIdentifikation the i identifikation
     */

    private void buchen_appIdentZugang(int pIIdentifikation) {

        doBtnLinks21AppIdent_stimmkarteBelegen(pIIdentifikation);

        stimmkarten[4] = "";
        stimmkartenEingeben[4] = 0;

        zutrittsIdent = new EclZutrittsIdent();
        zutrittsIdentKartenklasse = gwePraesenzStatusabfrageRC.kartenklasseZuIdentifikationsnummer
                .get(pIIdentifikation);
        zutrittsIdent.zutrittsIdent = gwePraesenzStatusabfrageRC.zutrittskarten.get(pIIdentifikation).zutrittsIdent;
        zutrittsIdent.zutrittsIdentNeben = gwePraesenzStatusabfrageRC.zutrittskarten
                .get(pIIdentifikation).zutrittsIdentNeben;

        lEclMeldung = gwePraesenzStatusabfrageRC.meldungen.get(pIIdentifikation);

        doBtnLinks21Appident_vertreterBelegen(pIIdentifikation);

        int lAktion = 0;
        if (aktionEnthalten(gwePraesenzStatusabfrageRC.zulaessigeFunktionenAlle.get(pIIdentifikation),
                KonstWillenserklaerung.wiederzugang_beliebigePerson)
                || aktionEnthalten(gwePraesenzStatusabfrageRC.zulaessigeFunktionenAlle.get(pIIdentifikation),
                        KonstWillenserklaerung.wiederzugang_nurSelbePerson)
                || aktionEnthalten(gwePraesenzStatusabfrageRC.zulaessigeFunktionenAlle.get(pIIdentifikation),
                        KonstWillenserklaerung.wiederzugang_nurSelbePersonMitNeuerVollmacht)
                || aktionEnthalten(gwePraesenzStatusabfrageRC.zulaessigeFunktionenAlle.get(pIIdentifikation),
                        KonstWillenserklaerung.wiederzugangGast_nurSelbePerson)) {
            lAktion = KonstWillenserklaerung.wiederzugang;
        } else {
            lAktion = KonstWillenserklaerung.erstzugang;
        }

        blClientPraesenzBuchen.buchen_appIdentZugang(KonstEingabeQuelle.konventionell_aufHV, zutrittsIdent,
                zutrittsIdentKartenklasse, stimmkarteEingebenErforderlich, stimmkarten, stimmkartenEingeben,
                lEclMeldung, personNatJurVertreter, personNatJurVertreterNeueVollmacht, vertreterName, vertreterVorname,
                vertreterOrt, vorbestimmtePersonNatJur, lAktion,
                gwePraesenzStatusabfrageRC.appPersonMussMitAppPersonUeberprueftWerden.get(pIIdentifikation),
                gwePraesenzStatusabfrageRC.appVertretendePerson.get(pIIdentifikation).ident,
                gwePraesenzStatusabfrageRC.appIdentPersonNatJurIdent, aktionen);

    }

    /**
     * Buchen app ident abgang.
     *
     * @param pIIdentifikation the i identifikation
     */
    private void buchen_appIdentAbgang(int pIIdentifikation) {

        zutrittsIdent = new EclZutrittsIdent();
        zutrittsIdentKartenklasse = gwePraesenzStatusabfrageRC.kartenklasseZuIdentifikationsnummer
                .get(pIIdentifikation);
        zutrittsIdent.zutrittsIdent = gwePraesenzStatusabfrageRC.zutrittskarten.get(pIIdentifikation).zutrittsIdent;
        zutrittsIdent.zutrittsIdentNeben = gwePraesenzStatusabfrageRC.zutrittskarten
                .get(pIIdentifikation).zutrittsIdentNeben;

        doBtnLinks21AppIdent_stimmkarteBelegen(pIIdentifikation);

        stimmkarten[4] = "";
        stimmkartenEingeben[4] = 0;

        lEclMeldung = gwePraesenzStatusabfrageRC.meldungen.get(pIIdentifikation);

        blClientPraesenzBuchen.buchen_appIdentAbgang(KonstEingabeQuelle.konventionell_aufHV, zutrittsIdent,
                zutrittsIdentKartenklasse, stimmkarteEingebenErforderlich, stimmkarten, stimmkartenEingeben,
                lEclMeldung, gwePraesenzStatusabfrageRC.appIdentPersonNatJurIdent);

    }

    /**
     * Prüfen für AppIdent: ist die Willenserklärung in allen Idents zulässig?.
     *
     * @param willenserklaerungArt the willenserklaerung art
     * @return true, if successful
     */
    boolean willenserklaerungZulaessigFuerAlle(int willenserklaerungArt) {
        boolean rc = true;
        for (int i = 0; i < gwePraesenzStatusabfrageRC.zulaessigeFunktionenAlle.size(); i++) {
            List<Integer> zulaessigeAlle = gwePraesenzStatusabfrageRC.zulaessigeFunktionenAlle.get(i);
            boolean vorhanden = false;
            for (int i1 = 0; i1 < zulaessigeAlle.size(); i1++) {
                if (zulaessigeAlle.get(i1) == willenserklaerungArt) {
                    vorhanden = true;
                }
            }
            if (vorhanden == false) {
                rc = false;
            }
        }
        return rc;
    }

    /**
     * Nach Einscannen Eintrittskartennummer.
     */
    void scanFeldVerarbeiten() {
        lblFehlerMeldung.setText("");
        int i1;
        /*Neuer Vorgang => Initialisieren*/
        clearDaten();

        nummerErstScan = tfScanFeld.getText();

        if (wsClient == null) {
            wsClient = new WSClient();
        }

        WEPraesenzStatusabfrage wePraesenzStatusabfrage = new WEPraesenzStatusabfrage();
        WELoginVerify weLoginVerify = new WELoginVerify();
        ;
        weLoginVerify.setEingabeQuelle(KonstEingabeQuelle.konventionell_aufHV);
        wePraesenzStatusabfrage.setWeLoginVerify(weLoginVerify);
        wePraesenzStatusabfrage.identifikationString = tfScanFeld.getText();
        wePraesenzStatusabfrage.programmFunktionHV = KonstProgrammFunktionHV.akkreditierungsSchalterStandard;
        WEPraesenzStatusabfrageRC wePraesenzStatusabfrageRC = wsClient.praesenzStatusabfrage(wePraesenzStatusabfrage);
        gwePraesenzStatusabfrageRC = wePraesenzStatusabfrageRC;
        if (wePraesenzStatusabfrageRC.rc == CaFehler.teVerbindungsabbruchWebService) {
            zeigeOfflineWarnung();
        }
        ausgabe_test(wePraesenzStatusabfrageRC);

        if (wePraesenzStatusabfrageRC.rc == CaFehler.pfNichtEindeutig) {
            /*TODO $_ Nicht-eindeutige Nummerneingaben implementieren*/
        }

        if (wePraesenzStatusabfrageRC.rc != 1) {
            zeigeFehlermeldung(wePraesenzStatusabfrageRC.rc, 1);
            return;
        }

        if (wePraesenzStatusabfrageRC.rcKartenklasse == KonstKartenklasse.appIdent) {
            aktion = 0;
            /*XXX Appident - müßte hier nicht auch Erstzugang gesetzt werden?*/
            //			if (aktionEnthalten(gwePraesenzStatusabfrageRC.gemeinsameZulaessigeFunktionenAlle, KonstWillenserklaerung.erstzugang)){aktion=KonstWillenserklaerung.erstzugang;}
            //			else{
            //				if (aktionEnthalten(gwePraesenzStatusabfrageRC.gemeinsameZulaessigeFunktionenAlle, KonstWillenserklaerung.wiederzugang)){aktion=KonstWillenserklaerung.wiederzugang;}
            //			}
            //			if (aktionEnthalten(gwePraesenzStatusabfrageRC.gemeinsameZulaessigeFunktionenAlle, KonstWillenserklaerung.abgang)){aktion=KonstWillenserklaerung.abgang;}
            if (willenserklaerungZulaessigFuerAlle(KonstWillenserklaerung.erstzugang)) {
                aktion = KonstWillenserklaerung.erstzugang;
            } else {
                if (willenserklaerungZulaessigFuerAlle(KonstWillenserklaerung.wiederzugang_nurSelbePerson)) {
                    aktion = KonstWillenserklaerung.wiederzugang;
                }
            }
            if (willenserklaerungZulaessigFuerAlle(KonstWillenserklaerung.abgang)) {
                aktion = KonstWillenserklaerung.abgang;
            }
            statusMaske = konstStatusMaske_ZugangAbgangAppIdent;
            setzeAnzeige();
            return;

        } else {/*Normaler Barcode gelesen*/
            CaBug.druckeLog("Normaler Barcode gelesen", logDrucken, 10);
            boolean stimmkarteGelesen = false;
            /*TODO _Praesenz: irgendwas hier mal im Rahmen von ku164 gefixt. Sollte nochmal überprüft werden*/
            if (wePraesenzStatusabfrageRC.rcKartenklasse == KonstKartenklasse.stimmkartennummer) {
                stimmkarteGelesen = true;
            }
            //			if (wePraesenzStatusabfrageRC.rcKartenklasse==KonstKartenklasse.eintrittskartennummer){eintrittskarteGelesen=true;}
            //			if (wePraesenzStatusabfrageRC.rcKartenklasse==KonstKartenklasse.gastkartennummer){gastkarteGelesen=true;}

            if (wePraesenzStatusabfrageRC.identifikationsnummer.size() != 1) {//Mehr als 1 Karte gelesen, ohne dass AppIdent vorliegt => Fehler
                zeigeFehlermeldung(CaFehler.pmNummernformUngueltig, 1);
                return;
            }

            /*Meldung einlesen*/
            lEclMeldung = wePraesenzStatusabfrageRC.meldungen.get(0);
            if (ParamS.param.paramAkkreditierung.formularNachErstzugang == 1) {
                lInitialPasswort = wePraesenzStatusabfrageRC.initialPasswort.get(0);
            }

            /*GGf. vorbestimmte Person übertragen*/
            vorbestimmtePersonNatJur = wePraesenzStatusabfrageRC.vorbestimmtePersonNatJur.get(0);
            CaBug.druckeLog("vorbestimmte PersonNatJur=" + vorbestimmtePersonNatJur, logDrucken, 10);
            vorbestimmteEclPersonNatJur = sucheEclInAktionaerVollmachten(vorbestimmtePersonNatJur, 0);

            /*aktionaerVollmachten belegen*/
            if (lEclMeldung.meldungIstEinGast()) {
                aktionaerVollmachten = null;
            } else {
                aktionaerVollmachten = wePraesenzStatusabfrageRC.aktionaerVollmachten.get(0);
            }

            CaBug.druckeLog("CtrlHauptStage.scanFeldVerarbeiten stimmkarteGelesen=" + stimmkarteGelesen, logDrucken,
                    10);
            /*Eintrittskartennummer der Meldung bestimmen und ggf. gelesene Stimmkartennummer*/
            if (stimmkarteGelesen) {
                /*stimmkarte ist "eigentlich" immer schon was fest zugeordnet*/
                findeStimmkartenZutrittsIdentZuPerson(vorbestimmtePersonNatJur, 0);
                //				lEintrittskartennummer=lEclMeldung.zutrittsIdent;
                //				lEintrittskartennummer.zutrittsIdent=wePraesenzStatusabfrageRC.zutrittskarten.get(0).zutrittsIdent;
                //				lStimmkartennummer=wePraesenzStatusabfrageRC.stimmkarten.get(0).stimmkarte;
            } else {
                zutrittsIdent.zutrittsIdent = wePraesenzStatusabfrageRC.identifikationsnummer.get(0);
                zutrittsIdent.zutrittsIdentNeben = wePraesenzStatusabfrageRC.identifikationsnummerNeben.get(0);
                zutrittsIdentKartenklasse = wePraesenzStatusabfrageRC.kartenklasseZuIdentifikationsnummer.get(0);
                findeStimmkartenZutrittsIdentZuPerson(vorbestimmtePersonNatJur, 0);

                //				lStimmkartennummer=""; //kann erst bestimmt werden, wenn Person bekannt ist!
            }

            /*Aktionen erforderlich, bevor Präsenzbuchung überhaupt möglich?*/
            if (wePraesenzStatusabfrageRC.erforderlicheAktionen.get(0) != null
                    && wePraesenzStatusabfrageRC.erforderlicheAktionen.get(0).size() != 0) {
                CaBug.druckeLog("Aktionen erforderlich vor Präsenzbuchung ermöglichen", logDrucken, 10);
                int gefAblehnung = -1;
                for (i1 = 0; i1 < wePraesenzStatusabfrageRC.erforderlicheAktionen.get(0).size(); i1++) {
                    int erfAktion = wePraesenzStatusabfrageRC.erforderlicheAktionen.get(0).get(i1);
                    if (erfAktion == KonstWillenserklaerung.sammelkartenZuordnungMussWiderrufenWerden
                            || erfAktion == KonstWillenserklaerung.sammelkartenZuordnungMussDeaktiviertWerden
                            || erfAktion == KonstWillenserklaerung.hinweisSammelkartenZuordnungVorhanden) {
                        gefAblehnung = 1;
                    }
                }
                /*TODO $Akkreditierung - erforderliche Aktionen - soweit es geht - auch am Akkreditierungsschalter durchführen*/
                if (gefAblehnung == 1
                        && ParamS.param.paramAkkreditierung.beiZugangStornierenAusSammelAutomatisch == 0) {
                    System.out.println("ParamS.param.paramAkkreditierung.beiZugangStornierenAusSammelAutomatisch="
                            + ParamS.param.paramAkkreditierung.beiZugangStornierenAusSammelAutomatisch);
                    zeigeFehlermeldung(CaFehler.pmInSammelkarteEnthalten, 1);
                    return;
                }
                if (gefAblehnung == 1
                        && ParamS.param.paramAkkreditierung.beiZugangStornierenAusSammelAutomatisch == 1) {
                    /**Ermitteln, in welcher Sammelkartenart die Karte enmthalten ist*/
                    String sammelkartenArt = "";
                    if (wePraesenzStatusabfrageRC.inSammelkarten != null
                            && wePraesenzStatusabfrageRC.inSammelkarten.get(0) != null) {
                        int anzInSammelkarten = wePraesenzStatusabfrageRC.inSammelkarten.get(0).length;
                        for (int i = 0; i < anzInSammelkarten; i++) {
                            int sammelkartenArtId = wePraesenzStatusabfrageRC.inSammelkarten.get(0)[i].skIst;
                            sammelkartenArt = KonstSkIst.getText(sammelkartenArtId);
                            sammelkartenArt += "-" + wePraesenzStatusabfrageRC.inSammelkarten.get(0)[i].name;
                        }
                    }
                    CaZeigeHinweis caZeigeHinweis = new CaZeigeHinweis();
                    boolean brc = caZeigeHinweis.zeige2Buttons(eigeneStage,
                            "In Sammelkarte - " + sammelkartenArt + " -  enthalten.\nAus Sammelkarte löschen?", "Ja",
                            "Nein");
                    if (brc == false) {
                        return;
                    }
                }
            }
            CaBug.druckeLog("vor Abfrage ob Gast", logDrucken, 10);
            if (lEclMeldung.meldungIstEinGast()) {
                CaBug.druckeLog("ist ein Gast", logDrucken, 10);
                if (aktionEnthalten(wePraesenzStatusabfrageRC.zulaessigeFunktionenMitAktionsnummer.get(0),
                        KonstWillenserklaerung.zugangGast) == true) {
                    aktion = KonstWillenserklaerung.zugangGast;
                    statusMaske = konstStatusMaske_ErstzugangWiederzugangMitFrageSelbst;
                    setzeAnzeige();
                    return;
                }
                if (aktionEnthalten(wePraesenzStatusabfrageRC.zulaessigeFunktionenMitAktionsnummer.get(0),
                        KonstWillenserklaerung.abgangGast) == true) {
                    aktion = KonstWillenserklaerung.abgangGast;
                    auchVollmachtwechsel = 0;
                    if (wePraesenzStatusabfrageRC.rcKartenart == KonstKartenart.erstzugang
                            || wePraesenzStatusabfrageRC.rcKartenart == KonstKartenart.wiederzugang) {
                        zugangskarteGelesen = 1;
                    } else {
                        zugangskarteGelesen = 0;
                    }
                    statusMaske = konstStatusMaske_Abgang;
                    setzeAnzeige();
                    return;
                }
                if (aktionEnthalten(wePraesenzStatusabfrageRC.zulaessigeFunktionenMitAktionsnummer.get(0),
                        KonstWillenserklaerung.wiederzugangGast_nurSelbePerson) == true) {
                    aktion = KonstWillenserklaerung.wiederzugangGast_nurSelbePerson;
                    statusMaske = konstStatusMaske_WiederzugangOhneFrage;
                    setzeAnzeige();
                    return;
                }
                /**
                 * An dieser Stelle: zulaessigeFunktionenMitAktionsnummer enthält keine Aktion,
                 * die an diesem Akkreditierungsschalter unterstützt wird
                 */
                if (aktionEnthalten(wePraesenzStatusabfrageRC.zulaessigeFunktionenAlle.get(0),
                        KonstWillenserklaerung.abgangGast) == true) {
                    istAnwesend = true;
                } else {
                    istAnwesend = false;
                }
                statusMaske = konstStatusMaske_Fehlersituation;
                setzeAnzeige();
                return;

            }

            if (aktionEnthalten(wePraesenzStatusabfrageRC.zulaessigeFunktionenMitAktionsnummer.get(0),
                    KonstWillenserklaerung.erstzugang) == true
                    || aktionEnthalten(wePraesenzStatusabfrageRC.zulaessigeFunktionenMitAktionsnummer.get(0),
                            KonstWillenserklaerung.wiederzugang_beliebigePerson) == true) {
                CaBug.druckeLog("Erstzugang Wiederzugang beliebige Person", logDrucken, 10);
                //*************Erstzugang/Wiederzugang beliebige Person
                if (aktionEnthalten(wePraesenzStatusabfrageRC.zulaessigeFunktionenMitAktionsnummer.get(0),
                        KonstWillenserklaerung.erstzugang) == true) {
                    if (logDrucken == 10) {
                        CaBug.druckeInfo("CtrlHauptStage.scanFeldVerarbeiten FrontofficeErstzugang!");
                    }
                    aktion = KonstWillenserklaerung.erstzugang;
                } else {
                    aktion = KonstWillenserklaerung.wiederzugang_beliebigePerson;
                    if (logDrucken == 10) {
                        CaBug.druckeInfo("CtrlHauptStage.scanFeldVerarbeiten WiederzugangbeliebigePerson");
                    }
                }

                if (logDrucken == 10) {
                    CaBug.druckeInfo("CtrlHauptStage.scanFeldVerarbeiten Vordefinierte Person="
                            + wePraesenzStatusabfrageRC.vordefiniertePersonNatJur.get(0));
                }
                if (aktionaerVollmachten != null && aktionaerVollmachten.length > 0) {
                    int jgef = -1; /*Es wurde überhaupt ein Vertreter gefunden - der wird als erster angeboten*/
                    int vordefGef = -1; /*Es wurde ein Vertreter gefunden, auf den die Karte ausgestellt ist - und der ist auch noch gültig. Mit diesem anfangen*/
                    for (int j = 0; j < aktionaerVollmachten.length; j++) {
                        aktionaerVollmachten[j].merker = 0;

                        if (logDrucken == 10) {
                            CaBug.druckeInfo("CtrlHauptStage.scanFeldVerarbeiten Vollmachten j=" + j);
                            CaBug.druckeInfo(aktionaerVollmachten[j].bevollmaechtigtePerson.name);
                            CaBug.druckeInfo(Boolean.toString(aktionaerVollmachten[j].wurdeStorniert));
                        }

                        if (aktionaerVollmachten[j].wurdeStorniert == false) {
                            if (jgef == -1) {
                                jgef = j;
                            }
                            if (aktionaerVollmachten[j].bevollmaechtigtePerson.ident == wePraesenzStatusabfrageRC.vordefiniertePersonNatJur
                                    .get(0)) {
                                if (logDrucken == 10) {
                                    CaBug.druckeInfo("VordefGef=" + j);
                                }
                                vordefGef = j;
                            }
                        }
                    }
                    if (vordefGef != -1) {
                        jgef = vordefGef;
                    }
                    if (jgef != -1) {
                        aktionaerVollmachtenAktuelleAbfrage = jgef;
                        aktionaerVollmachten[jgef].merker = 1;
                        statusMaske = konstStatusMaske_ErstzugangWiederzugangMitFrageVertreter;
                        setzeAnzeige();
                        return; //Bevollmächtigter vorhanden
                    }
                }
                statusMaske = konstStatusMaske_ErstzugangWiederzugangMitFrageSelbst;
                setzeAnzeige();
                return; //Nur "Selbst" voreingetragen, noch keine Vollmacht vorhanden.
            }

            if (aktionEnthalten(wePraesenzStatusabfrageRC.zulaessigeFunktionenMitAktionsnummer.get(0),
                    KonstWillenserklaerung.abgang) == true) {//************Abgang und Vollmachtswechsle/SRV
                CaBug.druckeLog("Abgang und Vollmachtswechsel/SRV", logDrucken, 10);
                aktion = KonstWillenserklaerung.abgang;
                if (aktionEnthalten(wePraesenzStatusabfrageRC.zulaessigeFunktionenMitAktionsnummer.get(0),
                        KonstWillenserklaerung.vertreterwechsel) == true) {
                    auchVollmachtwechsel = 1;
                } else {
                    auchVollmachtwechsel = 0;
                }
                if (aktionEnthalten(wePraesenzStatusabfrageRC.zulaessigeFunktionenMitAktionsnummer.get(0),
                        KonstWillenserklaerung.verlassenUndVollmachtUndWeisungAnSRV) == true) {
                    auchVollmachtSRV = 1;
                } else {
                    auchVollmachtSRV = 0;
                }

                if (wePraesenzStatusabfrageRC.rcKartenart == KonstKartenart.erstzugang
                        || wePraesenzStatusabfrageRC.rcKartenart == KonstKartenart.wiederzugang) {
                    zugangskarteGelesen = 1;
                } else {
                    zugangskarteGelesen = 0;
                }
                statusMaske = konstStatusMaske_Abgang;
                setzeAnzeige();
                return;
            }

            if (aktionEnthalten(wePraesenzStatusabfrageRC.zulaessigeFunktionenMitAktionsnummer.get(0),
                    KonstWillenserklaerung.wiederzugang_nurSelbePerson) == true) {//*****Wiederzugang - mit verwendetem Medium
                CaBug.druckeLog("Wiederzugang mit verwendetem Medium", logDrucken, 10);
                aktion = KonstWillenserklaerung.wiederzugang_nurSelbePerson;
                /*Die mögliche zugehende Person steht in vorbestimmtePersonNatJur, sowie die Daten (soweit bevollmächtigter)
                 * in vorbestimmteEclPersonNatJur*/
                if (vorbestimmtePersonNatJur > 0) {
                    personNatJurVertreter = vorbestimmteEclPersonNatJur.bevollmaechtigtePerson.ident;
                } else {
                    personNatJurVertreter = -1;
                }

                if (logDrucken == 10) {
                    CaBug.druckeInfo(
                            "CtrlHauptStage.scanFeldVerarbeiten personNatJurVertreter=" + personNatJurVertreter);
                }

                statusMaske = konstStatusMaske_WiederzugangOhneFrage;
                setzeAnzeige();
                return;
            }

            if (aktionEnthalten(wePraesenzStatusabfrageRC.zulaessigeFunktionenMitAktionsnummer.get(0),
                    KonstWillenserklaerung.vertreterwechsel) == true) {//********Vollmacht Dritte
                CaBug.druckeLog("Vollmacht Dritte", logDrucken, 10);
                aktion = KonstWillenserklaerung.vertreterwechsel;
                statusMaske = konstStatusMaske_VertreterwechselEingabeVertreter;
                setzeAnzeige();
                return;
            }
            if (aktionEnthalten(wePraesenzStatusabfrageRC.zulaessigeFunktionenMitAktionsnummer.get(0),
                    KonstWillenserklaerung.verlassenUndVollmachtUndWeisungAnSRV) == true) {//***********SRV
                CaBug.druckeLog("SRV", logDrucken, 10);
                aktion = KonstWillenserklaerung.verlassenUndVollmachtUndWeisungAnSRV;
                statusMaske = konstStatusMaske_VollmachtSRV;
                setzeAnzeige();
                return;
            }

            /**
             * An dieser Stelle: zulaessigeFunktionenMitAktionsnummer enthält keine Aktion,
             * die an diesem Akkreditierungsschalter unterstützt wird
             */
            CaBug.druckeLog("keine zulässige Funktion vorhanden", logDrucken, 10);
            if (aktionEnthalten(wePraesenzStatusabfrageRC.zulaessigeFunktionenAlle.get(0),
                    KonstWillenserklaerung.abgang) == true) {
                istAnwesend = true;
            } else {
                istAnwesend = false;
            }
            statusMaske = konstStatusMaske_Fehlersituation;
            setzeAnzeige();
            return;

        }
    }

    /**
     * Scan feld stimmen verarbeiten.
     */
    /*Letztes Stimmkarten-Zuordnungsfeld eingelesen - dann diese Funktion aufrufen*/
    private void scanFeldStimmenVerarbeiten() {
        /*ok*/
        lblFehlerMeldung.setText("");

        for (int i = 0; i < 4; i++) {
            TextField lTextField = null;
            if (stimmkartenEingeben[i] == 1) {
                switch (i) {
                case 0:
                    lTextField = tfScanStimmen0;
                    break;
                case 1:
                    lTextField = tfScanStimmen1;
                    break;
                case 2:
                    lTextField = tfScanStimmen2;
                    break;
                case 3:
                    lTextField = tfScanStimmen3;
                    break;
                case 4:
                    lTextField = tfScanStimmen4;
                    break;
                }
                stimmkarten[i] = lTextField.getText();
                if (stimmkarten[i].isEmpty()) {
                    lblFehlerMeldung.setText(
                            "Bitte " + gwePraesenzStatusabfrageRC.stimmkartenZuordnungenText.get(0)[i] + " einlesen!");
                    lTextField.setText("");
                    lTextField.requestFocus();
                    return;
                }
            }
        }

        if (aktion == KonstWillenserklaerung.erstzugang) {
            buchen_erstzugang();
        } else {
            buchen_wiederzugang();
        }
        return;
    }

    /**
     * Wird bei Erstzugang/Wiederzugang aufgerufen, nachdem Eintrittskarte/Person
     * feststeht (auch wenn für die Erstakkreditierung eine Stimmkarte verwendet
     * wurde) um festzustellen, ob die (weitere / zusätzliche) Eingabe/Zuordnung von
     * Stimmmaterial erforderlich ist (true) oder nicht (false).
     * 
     * Aufzurufen nach Bestätigung der Maske 3, 4, 5, 8, 12
     * 
     * Voraussetzung: personNatJurVertreter
     * 
     * Ergebnis: true => Stimmkarten müssen noch zugeordnet werden. Ansonsten ist
     * gefüllt: > stimmkarten > stimmkartenEingeben
     *
     * @return true, if successful
     */
    private boolean pruefeObStimmkartenEinzugeben() {

        System.out.println("StimmkartePruefen=" + personNatJurVertreter);

        if (aktion == KonstWillenserklaerung.zugangGast
                || aktion == KonstWillenserklaerung.wiederzugangGast_nurSelbePerson) {
            /*Bei Gästen: nie etwas zuzuordnen*/
            return false;
        }

        /********
         * stimmkarten und stimmkartenEingeben füllen - abhängig von der ausgewählten
         * Person
         *********/

        stimmkarten = new String[] { "", "", "", "", "", "" };
        stimmkartenEingeben = new int[] { 0, 0, 0, 0, 0, 0 };

        if (personNatJurVertreter == 0) { //neue Person - immer neu Zuordnen!
            /*TODO $Beachten: wird eine bereits zugeordnete Stimmkarte erneut zugeordnet, so muß diese intern "umgewidmet" werden,
             * aber grundsätzlich akzeptiert werden. Aber nur innerhalb einer Meldung! Hintergrund ist, dass eine andere Person
             * mit einem bereits ausgegebenen Stimmmaterial kommt (Bankenvorbereitung ...)
             */
            for (int i = 0; i < 4; i++) {
                stimmkartenEingeben[i] = gwePraesenzStatusabfrageRC.stimmkartenZuordnungenAktionaer
                        .get(0)[i];/*von Aktionär übernehmen - aber alles auf "neu zuordnen" setzen*/
                stimmkarten[i] = ""; /*von Aktionär übernehmen - aber alles auf "neu zuordnen" setzen*/
                if (stimmkartenEingeben[i] == -1) {
                    stimmkartenEingeben[i] = 1;
                }
            }
        }

        findeStimmkartenZutrittsIdentZuPerson(personNatJurVertreter, 0); //Hier sollte das "0" noch überprüft werden ...

        //		/******Falls Tausch 1:1: dann immer[0] vorbelegen - braucht nicht mehr abgefragt zu werden - war früher so, jetzt nicht mehr!********/
        //		if (ParamS.param.paramAkkreditierung.eintrittskarteWirdStimmkarte==true){
        //			/*Falls Tausch 1:1, dann hier stimmkarten[0] füllen*/
        //			DbBundle lDbBundle=new DbBundle();
        //			BlNummernformen blNummernformen=new BlNummernformen(lDbBundle);
        //			stimmkarten[0]=blNummernformen.formatiereNr(gwePraesenzStatusabfrageRC.zutrittskarten.get(0).zutrittsIdent, KonstKartenklasse.stimmkartennummer);
        //			stimmkartenEingeben[0]=-1;
        //		}

        /*****
         * Nun prüfen, ob noch eine Stimmkarteneingabe erforderlich ist
         ********************/
        stimmkarteEingebenErforderlich = false;
        for (int i = 0; i < 6; i++) {
            if (i != 4) {
                if (stimmkartenEingeben[i] == 1) {
                    stimmkarteEingebenErforderlich = true;
                }
            }
        }

        return stimmkarteEingebenErforderlich;
    }

    /**
     * Links click nach personenauswahl erstzugang wiederzugang vertreterwechsel.
     */
    /*Gemeinschaftliche Routine für alle linken-Button-Gecklicked bei Erstzugang*/
    private void linksClickNachPersonenauswahlErstzugangWiederzugangVertreterwechsel() {

        System.out.println("aktion=" + aktion);
        if (aktion == KonstWillenserklaerung.vertreterwechsel) {//Keine Abfrage bzgl. Stimmkarten!*/
            clearFehlermeldung();
            buchen_vertreterwechsel();
            return;
        }

        /*Präsenzbuchung speichern - Erstzugang, Tausch 1:1, Stimmkarte wie EK zuordnen*/
        if (pruefeObStimmkartenEinzugeben() == false) {
            if (aktion == KonstWillenserklaerung.erstzugang || aktion == KonstWillenserklaerung.zugangGast) {
                buchen_erstzugang();
            } else {
                buchen_wiederzugang();
            }
        } else {
            statusMaske = konstStatusMaske_StimmkarteNeuEingeben;
            setzeAnzeige();
        }

        return;
    }

    /**
     * Auszugebende stimmkartenausgeben.
     */
    /*Aufrufen nach Erstzugang und Wiederzugang - Ausgabe einer Meldung, was an Stimmkarten ausgegeben werden soll*/
    private void auszugebendeStimmkartenausgeben() {
        CaBug.druckeLog("", logDrucken, 10);
        if (ParamS.param.paramAkkreditierung.auszugebendeStimmkartenInMeldungAnzeigen == 0) {
            return;
        }
        if (lEclMeldung.klasse == 0) {
            /*Gast - nie Kartenmaterial*/
            CaZeigeHinweis caZeigeHinweis = new CaZeigeHinweis();
            caZeigeHinweis.zeige(eigeneStage, "Keine Stimmmaterial", 2);
            return;
        }
        if (aktion == KonstWillenserklaerung.erstzugang) {
            CaZeigeHinweis caZeigeHinweis = new CaZeigeHinweis();
            String lText = "Ausgabe von: ";
            for (int i = 0; i <= 4; i++) {
                if (ParamS.param.paramAkkreditierung.pPraesenzStimmkarteAktiv[lEclMeldung.liefereGattung()
                        - 1][i] == 1) {
                    lText = lText + ParamS.param.paramAkkreditierung.pPraesenzStimmkartenZuordnenGattungText[lEclMeldung
                            .liefereGattung() - 1][i] + " ";
                }
            }
            caZeigeHinweis.zeige(eigeneStage, lText, 1);
            return;
        } else {
            //Keine Abfrage bzgl. Stimmkarten!*/
            CaZeigeHinweis caZeigeHinweis = new CaZeigeHinweis();
            caZeigeHinweis.zeige(eigeneStage, "Keine Stimmmaterial", 2);
            return;
        }

    }

    /**
     * On btn links.
     *
     * @param event the event
     */
    @FXML
    void onBtnLinks(ActionEvent event) {
        doBtnLinks();
    }

    /**
     * Do btn links.
     */
    void doBtnLinks() {

        Platform.runLater(() -> loading.setVisible(true));
        btnLinks.setDisable(true);

        switch (statusMaske) {
        case konstStatusMaske_EinstiegsCodeEingeben: {/*Barcode scannen*/
            scanFeldVerarbeiten();
            break;
        }
        case konstStatusMaske_ErstzugangWiederzugangMitFrageSelbst: /*Erstzugang - sind Sie selbst?*/
        {
            personNatJurVertreter = -1;
            vertreterName = "";
            vertreterVorname = "";
            vertreterOrt = "";
            linksClickNachPersonenauswahlErstzugangWiederzugangVertreterwechsel();
            auszugebendeStimmkartenausgeben();
            break;
        }
        case konstStatusMaske_ErstzugangWiederzugangMitFrageVertreter: /*Erstzugang - sind Sie Vertreter?*/
        {
            personNatJurVertreter = aktionaerVollmachten[aktionaerVollmachtenAktuelleAbfrage].bevollmaechtigtePerson.ident;
            //            vertreterName = "";
            //            vertreterVorname = "";
            //            vertreterOrt = "";
            vertreterName = aktionaerVollmachten[aktionaerVollmachtenAktuelleAbfrage].bevollmaechtigtePerson.name;
            vertreterVorname = aktionaerVollmachten[aktionaerVollmachtenAktuelleAbfrage].bevollmaechtigtePerson.vorname;
            vertreterOrt = aktionaerVollmachten[aktionaerVollmachtenAktuelleAbfrage].bevollmaechtigtePerson.ort;
            linksClickNachPersonenauswahlErstzugangWiederzugangVertreterwechsel();
            auszugebendeStimmkartenausgeben();
            break;
        }
        case konstStatusMaske_ErstzugangWiederzugangVertreterEingabe: {/*Vertreter erfassen*/
            if (vertreterNameGet().isEmpty()) {
                lblFehlerMeldung.setText("Bitte Vertretername eingeben!");
            } else if (vertreterVornameNameGet().isEmpty()) {
                lblFehlerMeldung.setText("Bitte Vertretervorname eingeben!");
            } else if (tfOrt.getText().isEmpty()) {
                lblFehlerMeldung.setText("Bitte Vertreterort!");
            } else {
                personNatJurVertreter = 0;
                vertreterName = vertreterNameGet();
                vertreterVorname = vertreterVornameNameGet();
                vertreterOrt = tfOrt.getText();
                clearFehlermeldung();
                linksClickNachPersonenauswahlErstzugangWiederzugangVertreterwechsel();
            }
            break;
        }
        case konstStatusMaske_StimmkarteNeuEingeben: { /*Stimmkarte einlesen*/
            scanFeldStimmenVerarbeiten();
            break;
        }
        case konstStatusMaske_Abgang: { /*Abgang*/
            buchen_abgang();
            clearFehlermeldung();
            break;
        }
        case konstStatusMaske_WiederzugangOhneFrage: { /*WiederzugangOhneFrage => Wiederzugang durchführen*/
            linksClickNachPersonenauswahlErstzugangWiederzugangVertreterwechsel();
            auszugebendeStimmkartenausgeben();
            clearFehlermeldung();
            break;
        }
        case konstStatusMaske_VertreterwechselEingabeVertreter: { /*Vertreterwechsel buchen*/
            if (vertreterNameGet().isEmpty()) {
                lblFehlerMeldung.setText("Bitte Vertretername eingeben!");
            } else if (vertreterVornameNameGet().isEmpty()) {
                lblFehlerMeldung.setText("Bitte Vertretervorname eingeben!");
            } else if (tfOrt.getText().isEmpty()) {
                lblFehlerMeldung.setText("Bitte Vertreterort eingeben!");
            } else {
                personNatJurVertreter = 0;
                vertreterName = vertreterNameGet();
                vertreterVorname = vertreterVornameNameGet();
                vertreterOrt = tfOrt.getText();
                linksClickNachPersonenauswahlErstzugangWiederzugangVertreterwechsel();
            }

            break;
        }
        case konstStatusMaske_Buendeln: {/*Bündeln*/
            printBuendelnLabel();
            statusMaske = konstStatusMaske_EinstiegsCodeEingeben;
            setzeAnzeige();
            break;
        }
        case konstStatusMaske_ZugangAbgangAppIdent: {/*App-Ident*/
            doBtnLinks21AppIdent();
            break;
        }
        }
        Platform.runLater(() -> loading.setVisible(false));
        btnLinks.setDisable(false);
    }

    /**
     * Do btn links 21 app ident stimmkarte belegen.
     *
     * @param pIIdentifikation the i identifikation
     */
    private void doBtnLinks21AppIdent_stimmkarteBelegen(int pIIdentifikation) {
        stimmkarteEingebenErforderlich = false;
        if (aktion == KonstWillenserklaerung.abgang) {
            return;
        }
        stimmkarten = new String[5];
        stimmkartenEingeben = new int[5];
        for (int i1 = 0; i1 < 4; i1++) {
            if (gwePraesenzStatusabfrageRC.stimmkartenZuordnungenAktionaer.get(pIIdentifikation)[i1] == 1) {
                stimmkarteEingebenErforderlich = true;
                stimmkartenEingeben[i1] = 1;
                stimmkarten[i1] = lTfStimmkarte[pIIdentifikation][i1].getText();
            }
        }
    }

    /**
     * Do btn links 21 appident vertreter belegen.
     *
     * @param pIIdentifikation the i identifikation
     */
    private void doBtnLinks21Appident_vertreterBelegen(int pIIdentifikation) {

        vertreterName = appVertreterName[pIIdentifikation];
        vertreterVorname = appVertreterVorname[pIIdentifikation];
        vertreterOrt = appVertreterOrt[pIIdentifikation];
        if (lEclMeldung.meldungIstEinGast()) {/*Gast*/
            personNatJurVertreter = -1;
            personNatJurVertreterNeueVollmacht = 0;
        } else {/*Aktionär*/
            if (appVollmachtPersonenNatJurIdent[pIIdentifikation] == -1) {
                personNatJurVertreter = -1;
                personNatJurVertreterNeueVollmacht = 0;
            } else {
                personNatJurVertreter = appVollmachtPersonenNatJurIdent[pIIdentifikation];
                personNatJurVertreterNeueVollmacht = 0;
            }
            System.out.println(">>>>>>>>>>>>>> personNatJurVertreter=" + personNatJurVertreter);
            System.out.println(">>>>>>>>>>>>>> neue Vollmacht=" + personNatJurVertreterNeueVollmacht);
        }

        //		/*Ab hier: alte Funktionen!*/
        //		
        //		/*Gedanken zum Bevollmächtigten:
        //		 * die "durch die AppIdent gelieferte Vertretende Person" kann sein:
        //		 * > bereits dem Aktionär zugeordnete Person "Selbst"
        //		 * > ein bereits eingetragender Bevollmächtigter
        //		 * > die Person, die die App inne hat, und deshalb eine Vollmacht benötigt
        //		 */
        //		vertreterName=gwePraesenzStatusabfrageRC.appIdentPersonNatJur.name;
        //		vertreterVorname=gwePraesenzStatusabfrageRC.appIdentPersonNatJur.vorname;
        //		vertreterOrt=gwePraesenzStatusabfrageRC.appIdentPersonNatJur.ort;
        //		if (lEclMeldung.meldungIstEinGast()){/*Gast*/
        //			personNatJurVertreter=-1;
        //			personNatJurVertreterNeueVollmacht=0;
        //		}
        //		else{/*Aktionär*/
        //			if (gwePraesenzStatusabfrageRC.appVertretendePerson.get(pIIdentifikation).ident==lEclMeldung.personenNatJurIdent ||
        //					gwePraesenzStatusabfrageRC.appVertretendePerson.get(pIIdentifikation).ident==lEclMeldung.istSelbePersonWieIdent){
        //				personNatJurVertreter=-1;
        //				personNatJurVertreterNeueVollmacht=0;
        //			}
        //			else{
        //				personNatJurVertreter=gwePraesenzStatusabfrageRC.appVertretendePerson.get(pIIdentifikation).ident;
        //				personNatJurVertreterNeueVollmacht=0;
        //			}
        //		}

    }

    /**
     * Do btn links 21 app ident.
     */
    private void doBtnLinks21AppIdent() {
        int anzMeldungen = gwePraesenzStatusabfrageRC.meldungen.size();
        zuordnungStatusZuBuchen = new int[anzMeldungen];
        for (int i = 0; i < anzMeldungen; i++) {
            if (!ckBxNichtBuchen[i].isSelected()) {
                System.out.println("AAAA=" + gwePraesenzStatusabfrageRC.zutrittskarten.get(i).zutrittsIdent);

                //				if (gwePraesenzStatusabfrageRC.appVollmachtMussNochVorgelegtWerden.get(i)){
                //					EclMeldung lMeldung=gwePraesenzStatusabfrageRC.meldungen.get(i);
                //
                //					/*Person kann nicht - ohne weitere Maßnahmen - vertreten*/
                //					if (lMeldung.meldungIstEinAktionaer()){
                //						if (!ckBxVollmachtVorgelegt[i].isSelected()){
                //							ckBxVollmachtVorgelegt[i].setStyle("-fx-background-color: #ff0000");
                //							lblFehlerMeldung.setText("Bitte Vollmacht überprüfen und bestätigen!");return;
                //						}
                //					}
                //				}
                //				if (gwePraesenzStatusabfrageRC.appPersonMussMitAppPersonUeberprueftWerden.get(i)){
                //					if (!ckBxPersonUeberprueft[i].isSelected()){
                //						ckBxPersonUeberprueft[i].setStyle("-fx-background-color: #ff0000");
                //						lblFehlerMeldung.setText("Bitte Person überprüfen!");return;
                //					}
                //				}
                if (aktion == KonstWillenserklaerung.wiederzugang) {
                    for (int i1 = 0; i1 < 4; i1++) { /*Stimmkarten*/
                        if (gwePraesenzStatusabfrageRC.stimmkartenZuordnungenAktionaer.get(i)[i1] == 1) {
                            if (lTfStimmkarte[i][i1].getText().isEmpty()) {
                                lTfStimmkarte[i][i1].setStyle("-fx-background-color: #ff0000");
                                lblFehlerMeldung.setText("Bitte Stimmkarte zuordnen!");
                                return;

                            }
                        }
                    }
                }
            }
        }
        clearFehlermeldung();

        buchen_labeldruckinit();

        for (int i = 0; i < anzMeldungen; i++) {
            buchen_blInit();
            buchen_init();

            if (!ckBxNichtBuchen[i].isSelected()) {
                if (aktion == KonstWillenserklaerung.wiederzugang || aktion == KonstWillenserklaerung.erstzugang) {
                    System.out.println("i=" + i + " Buchen AppIdentZugang");

                    aktionen = 0;
                    if (gwePraesenzStatusabfrageRC.erforderlicheAktionen.get(i) != null) {
                        List<Integer> zulaessigeFunktionenAlle = gwePraesenzStatusabfrageRC.erforderlicheAktionen
                                .get(i);
                        if (zulaessigeFunktionenAlle.size() > 0) {
                            aktionen = 1;
                        }
                    }

                    buchen_appIdentZugang(i);
                } else { /*Abgang*/
                    buchen_appIdentAbgang(i);
                }
                zuordnungStatusZuBuchen[i] = lWEPraesenzBuchen.eclMeldung.size() - 1;

                lWEPraesenzBuchen = blClientPraesenzBuchen.lWEPraesenzBuchen;
                lWEPraesenzBuchenRC = wsClient.praesenzBuchen(lWEPraesenzBuchen);

                if (lWEPraesenzBuchenRC.rc == CaFehler.teVerbindungsabbruchWebService) {
                    zeigeOfflineWarnung();
                }

                if (ParamS.param.paramAkkreditierung.labelAuchFuerAppIdent) {
                    lEclMeldung = gwePraesenzStatusabfrageRC.meldungen.get(i);
                    doBtnLinks21AppIdent_stimmkarteBelegen(i);
                    doBtnLinks21Appident_vertreterBelegen(i);
                    aktionaerVollmachten = gwePraesenzStatusabfrageRC.aktionaerVollmachten.get(i);
                    buchen_labeldruck(i, zuordnungStatusZuBuchen[i]);
                    buchen_labeldruck(i, zuordnungStatusZuBuchen[i]);
                }

            } else {
                zuordnungStatusZuBuchen[i] = -1;
            }

        }

        buchen_abschlussBehandlung();
        System.out.println("Fehlerrc=" + lWEPraesenzBuchenRC.rc);
    }

    /**
     * On btn mitte.
     *
     * @param event the event
     */
    @FXML
    void onBtnMitte(ActionEvent event) {
        switch (statusMaske) {

        case konstStatusMaske_ErstzugangWiederzugangMitFrageSelbst: {/*Erstzugang - sind Sie selbst?*/
            if (lEclMeldung.klasse == 1) { /*Aktionär - weiter zu Vertretereingabe*/
                statusMaske = konstStatusMaske_ErstzugangWiederzugangVertreterEingabe;
                setzeAnzeige();
                return;
            } else {/*Gast => Abbruch*/
                statusMaske = konstStatusMaske_EinstiegsCodeEingeben;
                setzeAnzeige();
                return;
            }
        }

        case konstStatusMaske_ErstzugangWiederzugangMitFrageVertreter: {/*Erstzugang - sind Sie Vertreter?*/
            int jgef = -1;
            for (int j = 0; j < aktionaerVollmachten.length; j++) {
                if (aktionaerVollmachten[j].wurdeStorniert == false && aktionaerVollmachten[j].merker == 0) {
                    if (jgef == -1) {
                        jgef = j;
                    }
                }
            }

            if (jgef == -1) {
                statusMaske = konstStatusMaske_ErstzugangWiederzugangMitFrageSelbst;
                setzeAnzeige();
                return;
            }
            aktionaerVollmachtenAktuelleAbfrage = jgef;
            aktionaerVollmachten[jgef].merker = 1;
            setzeAnzeige();
            return;
        }

        case konstStatusMaske_Abgang:/*Abgang => Vertreterwechsel*/
            aktion = KonstWillenserklaerung.vertreterwechsel;
            vertreterNameSet("");
            vertreterVornameSet("");
            tfOrt.setText("");
            statusMaske = konstStatusMaske_VertreterwechselEingabeVertreter;
            personNatJurVertreter = 0;
            setzeAnzeige();
            break;
        }
    }

    /**
     * On btn rechts.
     *
     * @param event the event
     */
    @FXML
    void onBtnRechts(ActionEvent event) {

        clearFehlermeldung();
        statusMaske = konstStatusMaske_EinstiegsCodeEingeben;
        setzeAnzeige();

        return;

        //    	switch (statusMaske){
        //    	
        //    	case 2:{/*Barcode scannen*/
        //    		doBtnEinlesen();
        //    		if (statusMaske!=7 && statusMaske!=8){
        //       			statusMaske=2;setzeAnzeige();
        //				lblFehlerMeldung.setText("Präsenzänderung nicht möglich!");
        //				tfScanFeld.setText("");tfScanFeld.requestFocus();return;
        //   			
        //    		}
        //    		break;
        //    	}

    }

    /**
     * On btn aktionaer uebernehmen.
     *
     * @param event the event
     */
    @FXML
    void onBtnAktionaerUebernehmen(ActionEvent event) {
        vertreterNameSet(lEclMeldung.name);
        tfOrt.setText(lEclMeldung.ort);
        vertreterVornameRequestFocus();

    }

    /**
     * On btn letzten vertreter uebernehmen.
     *
     * @param event the event
     */
    @FXML
    void onBtnLetztenVertreterUebernehmen(ActionEvent event) {

        vertreterNameSet(letzterVertreterName);
        vertreterVornameSet(letzterVertreterVorname);
        tfOrt.setText(letzterVertreterOrt);
        tfName.requestFocus();
        /*TODO #Konsolidierung: derzeit wird immer neue VertreterIdent vergeben*/
        //		personNatJurVertreter=0;
        //		personNatJurVertreterNeueVollmacht=letzterVertreterIdent;
        //		vertreterName=letzterVertreterName;
        //		vertreterVorname=letzterVertreterVorname;
        //		vertreterOrt=letzterVertreterOrt;

        //		linksClickNachPersonenauswahlErstzugangWiederzugangVertreterwechsel();
    }

    /**
     * On btn abstimmungs modus.
     *
     * @param event the event
     */
    @FXML
    void onBtnAbstimmungsModus(ActionEvent event) {
        Stage neuerDialog = new Stage();

        CtrlWechseln controllerFenster = new CtrlWechseln();

        controllerFenster.init(neuerDialog, "Wechsel in Abstimmungsmodus?");

        FXMLLoader loader = new FXMLLoader(getClass().getResource("Wechseln.fxml"));
        loader.setController(controllerFenster);
        Parent mainPane = null;
        try {
            mainPane = (Parent) loader.load();
        } catch (IOException e) {
            CaBug.drucke("CtrlHautStage.onBtnAbstimmungsModus 001");
            e.printStackTrace();
        }
        Scene scene = new Scene(mainPane, controllerFenster.width, controllerFenster.height);
        neuerDialog.setTitle("Wechseln");
        neuerDialog.setScene(scene);
        neuerDialog.initModality(Modality.APPLICATION_MODAL);
        neuerDialog.showAndWait();

        boolean ausgewaehlt = controllerFenster.ausgewaehlt;
        if (ausgewaehlt == true) {
            wechsleZuAbstimmung();
        }

    }

    /**
     * On btn suchen.
     *
     * @param event the event
     */
    @FXML
    void onBtnSuchen(ActionEvent event) {
        CaBug.druckeLog("", logDrucken, 10);
        Stage newStage = new Stage();
        CaIcon.bestandsverwaltung(newStage);
        CtrlSuchen controllerFenster = new CtrlSuchen();
        controllerFenster.init(newStage);

        controllerFenster.funktionsButton1 = "Verarbeiten";
        controllerFenster.mehrfachAuswahlZulaessig = false;

        controllerFenster.durchsuchenSammelkarten = false;
        controllerFenster.durchsuchenInSammelkarten = true;
        controllerFenster.durchsuchenGaeste = false;

        controllerFenster.selektionNichtAngemeldeteMoeglich = false;

        CaController caController = new CaController();
        caController.open(newStage, controllerFenster, "/de/meetingapps/meetingclient/meetingClientDialoge/Suchen.fxml",
                1500, 760, "Suchen", true);

        if (controllerFenster.rcFortsetzen) {
            int anz = controllerFenster.rcAktienregister.length;
            if (anz == 1) {
                String hString = controllerFenster.rcMeldungen[0].zutrittsIdent;
                tfScanFeld.setText(hString);
                scanFeldVerarbeiten();
                //                tfAktionaersnummer.setText(controllerFenster.rcAktienregister[0].aktionaersnummer);
                //                doEinlesen();
                return;
            }
        }
    }

    /**
     * Wechsle zu abstimmung.
     */
    private void wechsleZuAbstimmung() {
        aufrufFensterTabletAbstimmung();
        CaIcon.frontOffice(eigeneStage);
        tfScanFeld.requestFocus();
    }

    /**
     * Wird auch für den direkten Start der Tablet-Abstimmung verwendet aus
     * MainClientMeeting heraus.
     */
    public void aufrufFensterTabletAbstimmung() {
        BlParameterUeberWebService lBlInitHVWebServices = new BlParameterUeberWebService();
        int rc = lBlInitHVWebServices.holeHVParameter();
        if (rc < 1) { /*Fehlerbehandlung*/
            this.fehlerMeldung("Abstimmungsstart im Offline-Betrieb nicht möglich!");
            return;
        }

        if (CInjects.praesenzAbstimmungsdatenEingelesen == false) {
            this.fehlerMeldung(
                    "Abstimmungsdaten stehen nicht zur Verfügung (möglicherweise kein aktiver Abstimmungsvorgang)!");
            return;

        }

        if (ParamSpezial.ku310(ParamS.clGlobalVar.mandant)) {
            /**ku310 nur quer, ohne Blättern*/
            String maske = "";
            int x = 1300, y = 800;
            int sollX = 0, sollY = 0;
            if (!ParamS.paramGeraet.abstimmungTabletXSize.isEmpty()) {
                sollX = Integer.parseInt(ParamS.paramGeraet.abstimmungTabletXSize);
            }
            if (!ParamS.paramGeraet.abstimmungTabletYSize.isEmpty()) {
                sollY = Integer.parseInt(ParamS.paramGeraet.abstimmungTabletYSize);
            }

            x = sollX;
            y = sollY;
            maske = "AbstimmungsStageku310.fxml";
            CaBug.druckeLog("maske=" + maske, logDrucken, 3);

            CaIcon.abstimmung(eigeneStage);

            Stage neuerDialog = new Stage();
            CtrlAbstimmungsStageku310 controllerFenster = new CtrlAbstimmungsStageku310();
            controllerFenster.init(neuerDialog);

            CaController caController = new CaController();
            caController.openUndecorated(neuerDialog, controllerFenster,
                    "/de/meetingapps/meetingclient/meetingFrontOffice/" + maske, x, y, "Abstimmung", true);

        } else {
            String maske = "";
            int x = 1300, y = 800;
            int sollX = 0, sollY = 0;
            if (!ParamS.paramGeraet.abstimmungTabletXSize.isEmpty()) {
                sollX = Integer.parseInt(ParamS.paramGeraet.abstimmungTabletXSize);
            }
            if (!ParamS.paramGeraet.abstimmungTabletYSize.isEmpty()) {
                sollY = Integer.parseInt(ParamS.paramGeraet.abstimmungTabletYSize);
            }
            if (ParamS.paramGeraet.abstimmungTabletHochformat) {
                x = sollY;
                y = sollX;
                maske = "AbstimmungsStageHoch.fxml";
                if (ParamS.param.paramAbstimmungParameter.beiTabletAbstimmungBlaettern == 1) {
                    maske = "AbstimmungsStageHochBlaettern.fxml";
                }
            } else {
                x = sollX;
                y = sollY;
                maske = "AbstimmungsStage.fxml";
                if (ParamS.param.paramAbstimmungParameter.beiTabletAbstimmungBlaettern == 1) {
                    maske = "AbstimmungsStageBlaettern.fxml";
                }
            }
            CaBug.druckeLog("maske=" + maske, logDrucken, 3);

            CaIcon.abstimmung(eigeneStage);

            Stage neuerDialog = new Stage();
            CtrlAbstimmungsStage controllerFenster = new CtrlAbstimmungsStage();
            controllerFenster.init(neuerDialog);

            CaController caController = new CaController();
            caController.openUndecorated(neuerDialog, controllerFenster,
                    "/de/meetingapps/meetingclient/meetingFrontOffice/" + maske, x, y, "Abstimmung", true);
        }

    }

    /**
     * Zeige offline warnung.
     *
     * @return the int
     */
    private int zeigeOfflineWarnung() {
        /*OK*/
        Stage neuerDialog = new Stage();

        CtrlOfflinePraesenz controllerFenster = new CtrlOfflinePraesenz();

        controllerFenster.init(neuerDialog);

        FXMLLoader loader = new FXMLLoader(getClass().getResource("OfflinePraesenzStage.fxml"));
        loader.setController(controllerFenster);
        Parent mainPane = null;
        try {
            mainPane = (Parent) loader.load();
        } catch (IOException e) {
            CaBug.drucke("CtrlHautStage.zeigeOfflineWarnung 001");
            e.printStackTrace();
        }
        Scene scene = new Scene(mainPane, 1200, 750);
        neuerDialog.setTitle("Wechseln");
        neuerDialog.setScene(scene);
        neuerDialog.initStyle(StageStyle.UNDECORATED);
        neuerDialog.initModality(Modality.APPLICATION_MODAL);
        neuerDialog.showAndWait();
        return 0;

    }

    /**
     * *********Verwaltung der Vertreterfelder**********.
     *
     * @param inhalt the inhalt
     */
    private void vertreterNameSet(String inhalt) {
        if (ParamS.param.paramAkkreditierung.positionVertretername == 1) {
            tfName.setText(inhalt);
        } else {
            tfVorname.setText(inhalt);
        }
    }

    /**
     * Vertreter name get.
     *
     * @return the string
     */
    private String vertreterNameGet() {
        if (ParamS.param.paramAkkreditierung.positionVertretername == 1) {
            return tfName.getText();
        } else {
            return tfVorname.getText();
        }
    }

    /**
     * Vertreter name label.
     */
    private void vertreterNameLabel() {
        if (ParamS.param.paramAkkreditierung.positionVertretername == 1) {
            lblName.setText("Name");
        } else {
            lblVorname.setText("Name");
        }
    }

    /**
     * Vertreter vorname set.
     *
     * @param inhalt the inhalt
     */
    private void vertreterVornameSet(String inhalt) {
        if (ParamS.param.paramAkkreditierung.positionVertretername == 1) {
            tfVorname.setText(inhalt);
        } else {
            tfName.setText(inhalt);
        }

    }

    /**
     * Vertreter vorname name get.
     *
     * @return the string
     */
    private String vertreterVornameNameGet() {
        if (ParamS.param.paramAkkreditierung.positionVertretername == 1) {
            return tfVorname.getText();
        } else {
            return tfName.getText();
        }
    }

    /**
     * Vertreter vorname label.
     */
    private void vertreterVornameLabel() {
        if (ParamS.param.paramAkkreditierung.positionVertretername == 1) {
            lblVorname.setText("Vorname");
        } else {
            lblName.setText("Vorname");
        }
    }

    /**
     * Vertreter vorname request focus.
     */
    private void vertreterVornameRequestFocus() {
        if (ParamS.param.paramAkkreditierung.positionVertretername == 1) {
            tfVorname.requestFocus();
        } else {
            tfName.requestFocus();
        }

    }

    /** The eigene stage. */
    private Stage eigeneStage;

    private boolean eingabeNummerBereitsErfolgt = false;
    private String eingabeNummer = "";

    /**
     * Inits the.
     *
     * @param pEigeneStage the eigene stage
     */
    public void init(Stage pEigeneStage) {
        eigeneStage = pEigeneStage;
        eingabeNummerBereitsErfolgt = false;
        eingabeNummer = "";
        statusMaske = konstStatusMaske_EinstiegsCodeEingeben;

    }

    public void init(Stage pEigeneStage, String pEingabeNummer) {
        eigeneStage = pEigeneStage;
        eingabeNummerBereitsErfolgt = true;
        eingabeNummer = pEingabeNummer;
        statusMaske = konstStatusMaske_EinstiegsCodeEingeben;

    }

}
