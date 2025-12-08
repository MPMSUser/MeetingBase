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
import java.util.ResourceBundle;

import de.meetingapps.meetingclient.meetingClientDialoge.CaController;
import de.meetingapps.meetingclient.meetingClientDialoge.CaIcon;
import de.meetingapps.meetingclient.meetingClientDialoge.CaZeigeHinweis;
import de.meetingapps.meetingclient.meetingClientDialoge.CtrlRoot;
import de.meetingapps.meetingportal.meetComAllg.CaFehler;
import de.meetingapps.meetingportal.meetComAllg.CaString;
import de.meetingapps.meetingportal.meetComDb.DbBundle;
import de.meetingapps.meetingportal.meetComHVParam.ParamS;
import de.meetingapps.meetingportal.meetComKonst.KonstAbstimmungsAblauf;
import de.meetingapps.meetingportal.meetComStub.StubParameter;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 * The Class CtrlParameterAbstimmungsParameter.
 */
public class CtrlParameterAbstimmungsParameter extends CtrlRoot {

    /** The resources. */
    @FXML
    private ResourceBundle resources;

    /** The location. */
    @FXML
    private URL location;

    /** The tf bei mehrfacher stimmabgabe letzte abgabe gilt ungleiche ungueltig. */
    @FXML
    private TextField tfBeiMehrfacherStimmabgabe_letzteAbgabeGilt_ungleicheUngueltig;

    /** The tf bei abstimmung eintrittskartennummer zulaessig. */
    @FXML
    private TextField tfBeiAbstimmungEintrittskartennummerZulaessig;

    /** The tf nicht teilnahme moeglich. */
    @FXML
    private TextField tfNichtTeilnahmeMoeglich;

    /** The tf abstimmen duerfen nur praesente praesente und gewesene. */
    @FXML
    private TextField tfAbstimmenDuerfen_nurPraesente_PraesenteUndGewesene;

    /** The tf priorisierung elektronisch vor papier papier vor elektronisch. */
    @FXML
    private TextField tfPriorisierung_ElektronischVorPapier_PapierVorElektronisch;

    /** The cb 100 enthaltungen. */
    @FXML
    private CheckBox cb100Enthaltungen;

    /** The cb 100 ungueltige. */
    @FXML
    private CheckBox cb100Ungueltige;

    /** The cb 100 nicht stimmberechtigte. */
    @FXML
    private CheckBox cb100NichtStimmberechtigte;

    /** The cb 100 nicht teilnahme. */
    @FXML
    private CheckBox cb100NichtTeilnahme;

    /** The cb ungueltige zaehlen als enthaltung. */
    @FXML
    private CheckBox cbUngueltigeZaehlenAlsEnthaltung;

    /** The tf bei tablet abstimmung nur aufgerufene stimm zettel zulaessig. */
    @FXML
    private TextField tfBeiTabletAbstimmungNurAufgerufeneStimmZettelZulaessig;

    /** The tf bei tablet abstimmung blaettern. */
    @FXML
    private TextField tfBeiTabletAbstimmungBlaettern;

    /** The tf ja abstimmungsgruppe 1. */
    @FXML
    private TextField tfJaAbstimmungsgruppe1;

    /** The tf ja abstimmungsgruppe 2. */
    @FXML
    private TextField tfJaAbstimmungsgruppe2;

    /** The tf ja abstimmungsgruppe 3. */
    @FXML
    private TextField tfJaAbstimmungsgruppe3;

    /** The tf ja abstimmungsgruppe 4. */
    @FXML
    private TextField tfJaAbstimmungsgruppe4;

    /** The tf ja abstimmungsgruppe 5. */
    @FXML
    private TextField tfJaAbstimmungsgruppe5;

    /** The tf ja abstimmungsgruppe 6. */
    @FXML
    private TextField tfJaAbstimmungsgruppe6;

    /** The tf ja abstimmungsgruppe 7. */
    @FXML
    private TextField tfJaAbstimmungsgruppe7;

    /** The tf ja abstimmungsgruppe 8. */
    @FXML
    private TextField tfJaAbstimmungsgruppe8;

    /** The tf ja abstimmungsgruppe 9. */
    @FXML
    private TextField tfJaAbstimmungsgruppe9;

    /** The tf ja abstimmungsgruppe 10. */
    @FXML
    private TextField tfJaAbstimmungsgruppe10;

    /** The tf bei tablet gesamtmarkierung text anzeigen. */
    @FXML
    private TextField tfBeiTabletGesamtmarkierungTextAnzeigen;

    /** The tf auswerten PP liste zeilen. */
    @FXML
    private TextField tfAuswertenPPListeZeilen;

    /** The tf bei tablet text kurz oder lang. */
    @FXML
    private TextField tfBeiTabletTextKurzOderLang;

    /** The tf bei tablet gesamtmarkierung fuer alle. */
    @FXML
    private TextField tfBeiTabletGesamtmarkierungFuerAlle;

    /** The cb bei tablet alles ja. */
    @FXML
    private CheckBox cbBeiTabletAllesJa;

    /** The cb bei tablet alles nein. */
    @FXML
    private CheckBox cbBeiTabletAllesNein;

    /** The cb bei tablet alles enthaltung. */
    @FXML
    private CheckBox cbBeiTabletAllesEnthaltung;

    /** The tf bei tablet farbe unmarkiert ja. */
    @FXML
    private TextField tfBeiTabletFarbeUnmarkiertJa;

    /** The tf bei tablet farbe ja. */
    @FXML
    private TextField tfBeiTabletFarbeJa;

    /** The tf bei tablet farbe gesamtmarkierung ja. */
    @FXML
    private TextField tfBeiTabletFarbeGesamtmarkierungJa;

    /** The tf bei tablet farbe unmarkiert nein. */
    @FXML
    private TextField tfBeiTabletFarbeUnmarkiertNein;

    /** The tf bei tablet farbe nein. */
    @FXML
    private TextField tfBeiTabletFarbeNein;

    /** The tf bei tablet farbe gesamtmarkierung nein. */
    @FXML
    private TextField tfBeiTabletFarbeGesamtmarkierungNein;

    /** The tf bei tablet farbe unmarkiert enthaltung. */
    @FXML
    private TextField tfBeiTabletFarbeUnmarkiertEnthaltung;

    /** The tf bei tablet farbe enthaltung. */
    @FXML
    private TextField tfBeiTabletFarbeEnthaltung;

    /** The tf bei tablet farbe gesamtmarkierung enthaltung. */
    @FXML
    private TextField tfBeiTabletFarbeGesamtmarkierungEnthaltung;

    /** The tf veroeffentlicht im bundesanzeiger vom. */
    @FXML
    private TextField tfVeroeffentlichtImBundesanzeigerVom;


    /** The tf text verwenden formulare. */
    @FXML
    private TextField tfTextVerwendenFormulare;

    /** The cb weisungen gegenantraege portal separat. */
    @FXML
    private CheckBox cbWeisungenGegenantraegePortalSeparat;

    /** The cb weisungen gegenantraege intern separat. */
    @FXML
    private CheckBox cbWeisungenGegenantraegeInternSeparat;

    /** The cb weisungen gegenantraege verlassen HV separat. */
    @FXML
    private CheckBox cbWeisungenGegenantraegeVerlassenHVSeparat;

    /** The pn steuerelemente verfuegbar. */
    @FXML
    private ScrollPane pnSteuerelementeVerfuegbar;

    /** The pn steuerelemente aktiv. */
    @FXML
    private ScrollPane pnSteuerelementeAktiv;

    /** The pn voreinstellung. */
    @FXML
    private Pane pnVoreinstellung;

    /** The btn refresh steuerelemente. */
    @FXML
    private Button btnRefreshSteuerelemente;

    @FXML
    private Button btnLadenPresetAblauf;
    
    /** The btn aktive steuerelemente abbruch. */
    @FXML
    private Button btnAktiveSteuerelementeAbbruch;

    /** The btn aktive steuerelemente ausfuehren. */
    @FXML
    private Button btnAktiveSteuerelementeAusfuehren;

    /** The lbl steuerelemente neue position. */
    @FXML
    private Label lblSteuerelementeNeuePosition;

    /** The tf steuerelemente neue position. */
    @FXML
    private TextField tfSteuerelementeNeuePosition;

    /** The tf anzeige bezeichnung kurz aktiv. */
    @FXML
    private TextField tfAnzeigeBezeichnungKurzAktiv;

    /** The cb eingabezwang weisung HV. */
    @FXML
    private CheckBox cbEingabezwangWeisungHV;

    /** The cb eingabezwang SRV. */
    @FXML
    private CheckBox cbEingabezwangSRV;

    /** The cb eingabezwang brierfwahl. */
    @FXML
    private CheckBox cbEingabezwangBrierfwahl;

    /** The cb eingabezwang KIAV. */
    @FXML
    private CheckBox cbEingabezwangKIAV;

    /** The cb eingabezwang dauer. */
    @FXML
    private CheckBox cbEingabezwangDauer;

    /** The cb eingabezwang org. */
    @FXML
    private CheckBox cbEingabezwangOrg;

    /** The cb eingabezwang abstimmung. */
    @FXML
    private CheckBox cbEingabezwangAbstimmung;

    /** The btn speichern. */
    @FXML
    private Button btnSpeichern;

    /** **Ab hier individuell****. */

    @FXML
    private ComboBox<String> cbWeisungVorbelegungMitSRV;

    /** The cb weisung vorbelegung mit briefwahl. */
    @FXML
    private ComboBox<String> cbWeisungVorbelegungMitBriefwahl;

    /** The cb weisung vorbelegung mit KIAV. */
    @FXML
    private ComboBox<String> cbWeisungVorbelegungMitKIAV;

    /** The cb weisung vorbelegung mit dauer. */
    @FXML
    private ComboBox<String> cbWeisungVorbelegungMitDauer;

    /** The cb weisung vorbelegung mit org. */
    @FXML
    private ComboBox<String> cbWeisungVorbelegungMitOrg;

    /** The cb weisung nicht markierte speichern als SRV. */
    @FXML
    private ComboBox<String> cbWeisungNichtMarkierteSpeichernAlsSRV;

    /** The cb weisung nicht markierte speichern als briefwahl. */
    @FXML
    private ComboBox<String> cbWeisungNichtMarkierteSpeichernAlsBriefwahl;

    /** The cb weisung nicht markierte speichern als KIAV. */
    @FXML
    private ComboBox<String> cbWeisungNichtMarkierteSpeichernAlsKIAV;

    /** The cb weisung nicht markierte speichern als dauer. */
    @FXML
    private ComboBox<String> cbWeisungNichtMarkierteSpeichernAlsDauer;

    /** The cb weisung nicht markierte speichern als org. */
    @FXML
    private ComboBox<String> cbWeisungNichtMarkierteSpeichernAlsOrg;

    /** The cb abstimmung nicht markierte speichern als. */
    @FXML
    private ComboBox<String> cbAbstimmungNichtMarkierteSpeichernAls;

    /** The cb weisung HV vorbelegung mit. */
    @FXML
    private ComboBox<String> cbWeisungHVVorbelegungMit;

    /** The cb weisung HV nicht markierte speichern als. */
    @FXML
    private ComboBox<String> cbWeisungHVNichtMarkierteSpeichernAls;

    /** The cb abstimmung vorbelegung mit. */
    @FXML
    private ComboBox<String> cbAbstimmungVorbelegungMit;

    /** The cb undefinierte weisungen zaehlen als. */
    @FXML
    private ComboBox<String> cbUndefinierteWeisungenZaehlenAls;

    /** *********Abstimmungs-Steuerung************. */
    /*Steuerelemente verfügbar*/
    private GridPane grpnSteuerelementeVerfuegbar = null;

    /** The cb steuerelement aktiv. */
    private CheckBox[] cbSteuerelementAktiv = null;

    /** The steuerelemente verfuegbar id. */
    private int[] steuerelementeVerfuegbarId = null;

    /**
     * -2 => nicht markiert; -1=neu markiert, noch keine Position (wird dann Pos 0)
     * >=0 => Pos.
     */
    private int[] positionInAktiv = null;

    /** The verarbeitet. */
    private int[] verarbeitet = null;

    /** The grpn steuerelemente aktiv. */
    /*Steuerelemente aktiv*/
    private GridPane grpnSteuerelementeAktiv = null;

    /** The lbl steuerelemente aktiv position. */
    private Label[] lblSteuerelementeAktivPosition = null;

    /** The btn steuerelemente aktiv. */
    private Button[] btnSteuerelementeAktiv = null;

    /** The btn steuerelemente aktiv verschieben. */
    private Button[] btnSteuerelementeAktivVerschieben = null;

    /** The steuerelemente aktiv id. */
    private int[] steuerelementeAktivId = null;

    /**
     * Initialize.
     */
    @FXML
    void initialize() {
        assert cbWeisungVorbelegungMitSRV != null : "fx:id=\"cbWeisungVorbelegungMitSRV\" was not injected: check your FXML file 'ParameterAbstimmungsParameter.fxml'.";
        assert cbWeisungVorbelegungMitBriefwahl != null : "fx:id=\"cbWeisungVorbelegungMitBriefwahl\" was not injected: check your FXML file 'ParameterAbstimmungsParameter.fxml'.";
        assert cbWeisungVorbelegungMitKIAV != null : "fx:id=\"cbWeisungVorbelegungMitKIAV\" was not injected: check your FXML file 'ParameterAbstimmungsParameter.fxml'.";
        assert cbWeisungVorbelegungMitDauer != null : "fx:id=\"cbWeisungVorbelegungMitDauer\" was not injected: check your FXML file 'ParameterAbstimmungsParameter.fxml'.";
        assert cbWeisungVorbelegungMitOrg != null : "fx:id=\"cbWeisungVorbelegungMitOrg\" was not injected: check your FXML file 'ParameterAbstimmungsParameter.fxml'.";
        assert cbWeisungNichtMarkierteSpeichernAlsSRV != null : "fx:id=\"cbWeisungNichtMarkierteSpeichernAlsSRV\" was not injected: check your FXML file 'ParameterAbstimmungsParameter.fxml'.";
        assert cbWeisungNichtMarkierteSpeichernAlsBriefwahl != null : "fx:id=\"cbWeisungNichtMarkierteSpeichernAlsBriefwahl\" was not injected: check your FXML file 'ParameterAbstimmungsParameter.fxml'.";
        assert cbWeisungNichtMarkierteSpeichernAlsKIAV != null : "fx:id=\"cbWeisungNichtMarkierteSpeichernAlsKIAV\" was not injected: check your FXML file 'ParameterAbstimmungsParameter.fxml'.";
        assert cbWeisungNichtMarkierteSpeichernAlsDauer != null : "fx:id=\"cbWeisungNichtMarkierteSpeichernAlsDauer\" was not injected: check your FXML file 'ParameterAbstimmungsParameter.fxml'.";
        assert cbWeisungNichtMarkierteSpeichernAlsOrg != null : "fx:id=\"cbWeisungNichtMarkierteSpeichernAlsOrg\" was not injected: check your FXML file 'ParameterAbstimmungsParameter.fxml'.";
        assert cbWeisungHVVorbelegungMit != null : "fx:id=\"cbWeisungHVVorbelegungMit\" was not injected: check your FXML file 'ParameterAbstimmungsParameter.fxml'.";
        assert cbWeisungHVNichtMarkierteSpeichernAls != null : "fx:id=\"cbWeisungHVNichtMarkierteSpeichernAls\" was not injected: check your FXML file 'ParameterAbstimmungsParameter.fxml'.";
        assert cbAbstimmungVorbelegungMit != null : "fx:id=\"cbAbstimmungVorbelegungMit\" was not injected: check your FXML file 'ParameterAbstimmungsParameter.fxml'.";
        assert tfBeiMehrfacherStimmabgabe_letzteAbgabeGilt_ungleicheUngueltig != null : "fx:id=\"tfBeiMehrfacherStimmabgabe_letzteAbgabeGilt_ungleicheUngueltig\" was not injected: check your FXML file 'ParameterAbstimmungsParameter.fxml'.";
        assert tfBeiAbstimmungEintrittskartennummerZulaessig != null : "fx:id=\"tfBeiAbstimmungEintrittskartennummerZulaessig\" was not injected: check your FXML file 'ParameterAbstimmungsParameter.fxml'.";
        assert tfAbstimmenDuerfen_nurPraesente_PraesenteUndGewesene != null : "fx:id=\"tfAbstimmenDuerfen_nurPraesente_PraesenteUndGewesene\" was not injected: check your FXML file 'ParameterAbstimmungsParameter.fxml'.";
        assert tfPriorisierung_ElektronischVorPapier_PapierVorElektronisch != null : "fx:id=\"tfPriorisierung_ElektronischVorPapier_PapierVorElektronisch\" was not injected: check your FXML file 'ParameterAbstimmungsParameter.fxml'.";
        assert cb100Enthaltungen != null : "fx:id=\"cb100Enthaltungen\" was not injected: check your FXML file 'ParameterAbstimmungsParameter.fxml'.";
        assert cb100Ungueltige != null : "fx:id=\"cb100Ungueltige\" was not injected: check your FXML file 'ParameterAbstimmungsParameter.fxml'.";
        assert cb100NichtStimmberechtigte != null : "fx:id=\"cb100NichtStimmberechtigte\" was not injected: check your FXML file 'ParameterAbstimmungsParameter.fxml'.";
        assert cb100NichtTeilnahme != null : "fx:id=\"cb100NichtTeilnahme\" was not injected: check your FXML file 'ParameterAbstimmungsParameter.fxml'.";
        assert cbUndefinierteWeisungenZaehlenAls != null : "fx:id=\"cbUndefinierteWeisungenZaehlenAls\" was not injected: check your FXML file 'ParameterAbstimmungsParameter.fxml'.";
        assert cbUngueltigeZaehlenAlsEnthaltung != null : "fx:id=\"cbUngueltigeZaehlenAlsEnthaltung\" was not injected: check your FXML file 'ParameterAbstimmungsParameter.fxml'.";
        assert tfBeiTabletAbstimmungNurAufgerufeneStimmZettelZulaessig != null : "fx:id=\"tfBeiTabletAbstimmungNurAufgerufeneStimmZettelZulaessig\" was not injected: check your FXML file 'ParameterAbstimmungsParameter.fxml'.";
        assert tfBeiTabletAbstimmungBlaettern != null : "fx:id=\"tfBeiTabletAbstimmungBlaettern\" was not injected: check your FXML file 'ParameterAbstimmungsParameter.fxml'.";
        assert tfJaAbstimmungsgruppe1 != null : "fx:id=\"tfJaAbstimmungsgruppe1\" was not injected: check your FXML file 'ParameterAbstimmungsParameter.fxml'.";
        assert tfJaAbstimmungsgruppe2 != null : "fx:id=\"tfJaAbstimmungsgruppe2\" was not injected: check your FXML file 'ParameterAbstimmungsParameter.fxml'.";
        assert tfJaAbstimmungsgruppe3 != null : "fx:id=\"tfJaAbstimmungsgruppe3\" was not injected: check your FXML file 'ParameterAbstimmungsParameter.fxml'.";
        assert tfJaAbstimmungsgruppe4 != null : "fx:id=\"tfJaAbstimmungsgruppe4\" was not injected: check your FXML file 'ParameterAbstimmungsParameter.fxml'.";
        assert tfJaAbstimmungsgruppe5 != null : "fx:id=\"tfJaAbstimmungsgruppe5\" was not injected: check your FXML file 'ParameterAbstimmungsParameter.fxml'.";
        assert tfJaAbstimmungsgruppe6 != null : "fx:id=\"tfJaAbstimmungsgruppe6\" was not injected: check your FXML file 'ParameterAbstimmungsParameter.fxml'.";
        assert tfJaAbstimmungsgruppe7 != null : "fx:id=\"tfJaAbstimmungsgruppe7\" was not injected: check your FXML file 'ParameterAbstimmungsParameter.fxml'.";
        assert tfJaAbstimmungsgruppe8 != null : "fx:id=\"tfJaAbstimmungsgruppe8\" was not injected: check your FXML file 'ParameterAbstimmungsParameter.fxml'.";
        assert tfJaAbstimmungsgruppe9 != null : "fx:id=\"tfJaAbstimmungsgruppe9\" was not injected: check your FXML file 'ParameterAbstimmungsParameter.fxml'.";
        assert tfJaAbstimmungsgruppe10 != null : "fx:id=\"tfJaAbstimmungsgruppe10\" was not injected: check your FXML file 'ParameterAbstimmungsParameter.fxml'.";
        assert tfBeiTabletGesamtmarkierungTextAnzeigen != null : "fx:id=\"tfBeiTabletGesamtmarkierungTextAnzeigen\" was not injected: check your FXML file 'ParameterAbstimmungsParameter.fxml'.";
        assert tfAuswertenPPListeZeilen != null : "fx:id=\"tfAuswertenPPListeZeilen\" was not injected: check your FXML file 'ParameterAbstimmungsParameter.fxml'.";
        assert tfBeiTabletTextKurzOderLang != null : "fx:id=\"tfBeiTabletTextKurzOderLang\" was not injected: check your FXML file 'ParameterAbstimmungsParameter.fxml'.";
        assert tfBeiTabletGesamtmarkierungFuerAlle != null : "fx:id=\"tfBeiTabletGesamtmarkierungFuerAlle\" was not injected: check your FXML file 'ParameterAbstimmungsParameter.fxml'.";
        assert cbBeiTabletAllesJa != null : "fx:id=\"cbBeiTabletAllesJa\" was not injected: check your FXML file 'ParameterAbstimmungsParameter.fxml'.";
        assert cbBeiTabletAllesNein != null : "fx:id=\"cbBeiTabletAllesNein\" was not injected: check your FXML file 'ParameterAbstimmungsParameter.fxml'.";
        assert cbBeiTabletAllesEnthaltung != null : "fx:id=\"cbBeiTabletAllesEnthaltung\" was not injected: check your FXML file 'ParameterAbstimmungsParameter.fxml'.";
        assert tfVeroeffentlichtImBundesanzeigerVom != null : "fx:id=\"tfVeroeffentlichtImBundesanzeigerVom\" was not injected: check your FXML file 'ParameterAbstimmungsParameter.fxml'.";
        assert tfTextVerwendenFormulare != null : "fx:id=\"tfTextVerwendenTablet\" was not injected: check your FXML file 'ParameterAbstimmungsParameter.fxml'.";
        assert pnSteuerelementeVerfuegbar != null : "fx:id=\"pnSteuerelementeVerfuegbar\" was not injected: check your FXML file 'ParameterAbstimmungsParameter.fxml'.";
        assert pnSteuerelementeAktiv != null : "fx:id=\"pnSteuerelementeAktiv\" was not injected: check your FXML file 'ParameterAbstimmungsParameter.fxml'.";
        assert pnVoreinstellung != null : "fx:id=\"pnVoreinstellung\" was not injected: check your FXML file 'ParameterAbstimmungsParameter.fxml'.";
        assert btnRefreshSteuerelemente != null : "fx:id=\"btnRefreshSteuerelemente\" was not injected: check your FXML file 'ParameterAbstimmungsParameter.fxml'.";
        assert btnAktiveSteuerelementeAbbruch != null : "fx:id=\"btnAktiveSteuerelementeAbbruch\" was not injected: check your FXML file 'ParameterAbstimmungsParameter.fxml'.";
        assert btnAktiveSteuerelementeAusfuehren != null : "fx:id=\"btnAktiveSteuerelementeAusfuehren\" was not injected: check your FXML file 'ParameterAbstimmungsParameter.fxml'.";
        assert lblSteuerelementeNeuePosition != null : "fx:id=\"lblSteuerelementeNeuePosition\" was not injected: check your FXML file 'ParameterAbstimmungsParameter.fxml'.";
        assert tfSteuerelementeNeuePosition != null : "fx:id=\"tfSteuerelementeNeuePosition\" was not injected: check your FXML file 'ParameterAbstimmungsParameter.fxml'.";
        assert btnSpeichern != null : "fx:id=\"btnSpeichern\" was not injected: check your FXML file 'ParameterAbstimmungsParameter.fxml'.";

        /*Abstimmungsparameter*/
        belegeTextfelder();

        /*Abstimmungs-Steuerung*/
        zeigeSteuerelementeVerfuegbar();
        zeigeSteuerelementeAktiv();
        deaktiviereModusVerschieben();

        eigeneStage.setOnCloseRequest(new EventHandler<WindowEvent>() {

            @Override
            public void handle(WindowEvent event) {
                CaZeigeHinweis caZeigeHinweis = new CaZeigeHinweis();
                caZeigeHinweis.zeige(eigeneStage,
                        "Achtung - beim Schließen erfolgt keine erneute Speicherung! Bitte nach Schließen Parameter aktuelle Einstellungen nochmals überprüfen!");
                //                speichernParameter();
            }
        });

    }

    /** The eigene stage. */
    private Stage eigeneStage;

    /**
     * Inits the.
     *
     * @param pEigeneStage the eigene stage
     */
    public void init(Stage pEigeneStage) {
        /** HV-Parameter sicherheitshalber neu holen */
        DbBundle lDbBundle = new DbBundle();
        StubParameter stubParameter = new StubParameter(false, lDbBundle);
        stubParameter.leseHVParam_all(lDbBundle.clGlobalVar.mandant, lDbBundle.clGlobalVar.hvJahr,
                lDbBundle.clGlobalVar.hvNummer, lDbBundle.clGlobalVar.datenbereich);
        ParamS.param = stubParameter.rcHVParam;

        eigeneStage = pEigeneStage;
    }

    /*******************Parameter***********************************/
    /**Parameter in Textfelder übernehmen*/
    private void belegeTextfelder() {

        /*Eingabe Weisungen etc.*/
        CtrlParameterAbstimmungAllgemein.fuelleCbStimmenBelegenMit(cbWeisungVorbelegungMitSRV,
                ParamS.param.paramAbstimmungParameter.weisungVorbelegungMitSRV);
        CtrlParameterAbstimmungAllgemein.fuelleCbStimmenBelegenMit(cbWeisungVorbelegungMitBriefwahl,
                ParamS.param.paramAbstimmungParameter.weisungVorbelegungMitBriefwahl);
        CtrlParameterAbstimmungAllgemein.fuelleCbStimmenBelegenMit(cbWeisungVorbelegungMitKIAV,
                ParamS.param.paramAbstimmungParameter.weisungVorbelegungMitKIAV);
        CtrlParameterAbstimmungAllgemein.fuelleCbStimmenBelegenMit(cbWeisungVorbelegungMitDauer,
                ParamS.param.paramAbstimmungParameter.weisungVorbelegungMitDauer);
        CtrlParameterAbstimmungAllgemein.fuelleCbStimmenBelegenMit(cbWeisungVorbelegungMitOrg,
                ParamS.param.paramAbstimmungParameter.weisungVorbelegungMitOrg);

        CtrlParameterAbstimmungAllgemein.fuelleCbStimmenBelegenMit(cbWeisungNichtMarkierteSpeichernAlsSRV,
                ParamS.param.paramAbstimmungParameter.weisungNichtMarkierteSpeichernAlsSRV);
        CtrlParameterAbstimmungAllgemein.fuelleCbStimmenBelegenMit(cbWeisungNichtMarkierteSpeichernAlsBriefwahl,
                ParamS.param.paramAbstimmungParameter.weisungNichtMarkierteSpeichernAlsBriefwahl);
        CtrlParameterAbstimmungAllgemein.fuelleCbStimmenBelegenMit(cbWeisungNichtMarkierteSpeichernAlsKIAV,
                ParamS.param.paramAbstimmungParameter.weisungNichtMarkierteSpeichernAlsKIAV);
        CtrlParameterAbstimmungAllgemein.fuelleCbStimmenBelegenMit(cbWeisungNichtMarkierteSpeichernAlsDauer,
                ParamS.param.paramAbstimmungParameter.weisungNichtMarkierteSpeichernAlsDauer);
        CtrlParameterAbstimmungAllgemein.fuelleCbStimmenBelegenMit(cbWeisungNichtMarkierteSpeichernAlsOrg,
                ParamS.param.paramAbstimmungParameter.weisungNichtMarkierteSpeichernAlsOrg);

        CtrlParameterAbstimmungAllgemein.fuelleCbStimmenBelegenMit(cbWeisungHVVorbelegungMit,
                ParamS.param.paramAbstimmungParameter.weisungHVVorbelegungMit);
        CtrlParameterAbstimmungAllgemein.fuelleCbStimmenBelegenMit(cbWeisungHVNichtMarkierteSpeichernAls,
                ParamS.param.paramAbstimmungParameter.weisungHVNichtMarkierteSpeichernAls);
        CtrlParameterAbstimmungAllgemein.fuelleCbStimmenBelegenMit(cbAbstimmungVorbelegungMit,
                ParamS.param.paramAbstimmungParameter.abstimmungVorbelegungMit);

        cbEingabezwangSRV.setSelected(ParamS.param.paramAbstimmungParameter.eingabezwangSRV);
        cbEingabezwangBrierfwahl.setSelected(ParamS.param.paramAbstimmungParameter.eingabezwangBriefwahl);
        cbEingabezwangKIAV.setSelected(ParamS.param.paramAbstimmungParameter.eingabezwangKIAV);
        cbEingabezwangDauer.setSelected(ParamS.param.paramAbstimmungParameter.eingabezwangDauer);
        cbEingabezwangOrg.setSelected(ParamS.param.paramAbstimmungParameter.eingabezwangOrg);
        cbEingabezwangSRV.setSelected(ParamS.param.paramAbstimmungParameter.eingabezwangWeisungHV);
        cbEingabezwangAbstimmung.setSelected(ParamS.param.paramAbstimmungParameter.eingabezwangAbstimmung);

        /*Stimmabgabe*/
        tfBeiMehrfacherStimmabgabe_letzteAbgabeGilt_ungleicheUngueltig.setText(Integer.toString(
                ParamS.param.paramAbstimmungParameter.beiMehrfacherStimmabgabe_letzteAbgabeGilt_ungleicheUngueltig));
        tfBeiAbstimmungEintrittskartennummerZulaessig.setText(
                Integer.toString(ParamS.param.paramAbstimmungParameter.beiAbstimmungEintrittskartennummerZulaessig));
        tfNichtTeilnahmeMoeglich
                .setText(Integer.toString(ParamS.param.paramAbstimmungParameter.nichtTeilnahmeMoeglich));

        /*Abstimmungs-Auswertung*/
        tfAbstimmenDuerfen_nurPraesente_PraesenteUndGewesene.setText(Integer
                .toString(ParamS.param.paramAbstimmungParameter.abstimmenDuerfen_nurPraesente_PraesenteUndGewesene));
        tfPriorisierung_ElektronischVorPapier_PapierVorElektronisch.setText(Integer.toString(
                ParamS.param.paramAbstimmungParameter.priorisierung_ElektronischVorPapier_PapierVorElektronisch));
        if (ParamS.param.paramAbstimmungParameter.in100PZAuchEnthaltungen == 1) {
            cb100Enthaltungen.setSelected(true);
        }
        if (ParamS.param.paramAbstimmungParameter.in100PZAuchUngueltige == 1) {
            cb100Ungueltige.setSelected(true);
        }
        if (ParamS.param.paramAbstimmungParameter.in100PZAuchNichtStimmberechtigte == 1) {
            cb100NichtStimmberechtigte.setSelected(true);
        }
        if (ParamS.param.paramAbstimmungParameter.in100PZAuchNichtTeilnahme == 1) {
            cb100NichtTeilnahme.setSelected(true);
        }

        CtrlParameterAbstimmungAllgemein.fuelleCbStimmenBelegenMitJNEU(cbUndefinierteWeisungenZaehlenAls,
                ParamS.param.paramAbstimmungParameter.undefinierteWeisungenZaehlenAls);

        if (ParamS.param.paramAbstimmungParameter.ungueltigeZaehlenAlsEnthaltung == 1) {
            cbUngueltigeZaehlenAlsEnthaltung.setSelected(true);
        }

        /*Ergebnis-Ausgabe*/
        tfVeroeffentlichtImBundesanzeigerVom
                .setText(ParamS.param.paramAbstimmungParameter.veroeffentlichtImBundesanzeigerVom);


        tfTextVerwendenFormulare.setText(Integer.toString(ParamS.param.paramAbstimmungParameter.textVerwendenFormular));
        tfAuswertenPPListeZeilen
                .setText(Integer.toString(ParamS.param.paramAbstimmungParameter.auswertenPPListeZeilen));

        /*Tablet-Abstimmung*/
        tfBeiTabletAbstimmungNurAufgerufeneStimmZettelZulaessig.setText(Integer
                .toString(ParamS.param.paramAbstimmungParameter.beiTabletAbstimmungNurAufgerufeneStimmZettelZulaessig));
        tfBeiTabletAbstimmungBlaettern
                .setText(Integer.toString(ParamS.param.paramAbstimmungParameter.beiTabletAbstimmungBlaettern));
        tfBeiTabletTextKurzOderLang
                .setText(Integer.toString(ParamS.param.paramAbstimmungParameter.textVerwendenTablet));
        tfBeiTabletGesamtmarkierungTextAnzeigen
                .setText(Integer.toString(ParamS.param.paramAbstimmungParameter.beiTabletGesamtmarkierungTextAnzeigen));
        tfBeiTabletGesamtmarkierungFuerAlle
                .setText(Integer.toString(ParamS.param.paramAbstimmungParameter.beiTabletGesamtmarkierungTextAnzeigen));
        if (ParamS.param.paramAbstimmungParameter.beiTabletAllesJa == 1) {
            cbBeiTabletAllesJa.setSelected(true);
        }
        if (ParamS.param.paramAbstimmungParameter.beiTabletAllesNein == 1) {
            cbBeiTabletAllesNein.setSelected(true);
        }
        if (ParamS.param.paramAbstimmungParameter.beiTabletAllesEnthaltung == 1) {
            cbBeiTabletAllesEnthaltung.setSelected(true);
        }

        tfBeiTabletFarbeJa.setText(ParamS.param.paramAbstimmungParameter.beiTabletFarbeJa);
        tfBeiTabletFarbeNein.setText(ParamS.param.paramAbstimmungParameter.beiTabletFarbeNein);
        tfBeiTabletFarbeEnthaltung.setText(ParamS.param.paramAbstimmungParameter.beiTabletFarbeEnthaltung);

        tfBeiTabletFarbeUnmarkiertJa.setText(ParamS.param.paramAbstimmungParameter.beiTabletFarbeUnmarkiertJa);
        tfBeiTabletFarbeUnmarkiertNein.setText(ParamS.param.paramAbstimmungParameter.beiTabletFarbeUnmarkiertNein);
        tfBeiTabletFarbeUnmarkiertEnthaltung
                .setText(ParamS.param.paramAbstimmungParameter.beiTabletFarbeUnmarkiertEnthaltung);

        tfBeiTabletFarbeGesamtmarkierungJa
                .setText(ParamS.param.paramAbstimmungParameter.beiTabletFarbeGesamtmarkierungJa);
        tfBeiTabletFarbeGesamtmarkierungNein
                .setText(ParamS.param.paramAbstimmungParameter.beiTabletFarbeGesamtmarkierungNein);
        tfBeiTabletFarbeGesamtmarkierungEnthaltung
                .setText(ParamS.param.paramAbstimmungParameter.beiTabletFarbeGesamtmarkierungEnthaltung);

        /*Anzahl Abstimmungen je Abstimmungsgruppe*/
        tfJaAbstimmungsgruppe1
                .setText(Integer.toString(ParamS.param.paramAbstimmungParameter.anzahlJaJeAbstimmungsgruppe[1]));
        tfJaAbstimmungsgruppe2
                .setText(Integer.toString(ParamS.param.paramAbstimmungParameter.anzahlJaJeAbstimmungsgruppe[2]));
        tfJaAbstimmungsgruppe3
                .setText(Integer.toString(ParamS.param.paramAbstimmungParameter.anzahlJaJeAbstimmungsgruppe[3]));
        tfJaAbstimmungsgruppe4
                .setText(Integer.toString(ParamS.param.paramAbstimmungParameter.anzahlJaJeAbstimmungsgruppe[4]));
        tfJaAbstimmungsgruppe5
                .setText(Integer.toString(ParamS.param.paramAbstimmungParameter.anzahlJaJeAbstimmungsgruppe[5]));
        tfJaAbstimmungsgruppe6
                .setText(Integer.toString(ParamS.param.paramAbstimmungParameter.anzahlJaJeAbstimmungsgruppe[6]));
        tfJaAbstimmungsgruppe7
                .setText(Integer.toString(ParamS.param.paramAbstimmungParameter.anzahlJaJeAbstimmungsgruppe[7]));
        tfJaAbstimmungsgruppe8
                .setText(Integer.toString(ParamS.param.paramAbstimmungParameter.anzahlJaJeAbstimmungsgruppe[8]));
        tfJaAbstimmungsgruppe9
                .setText(Integer.toString(ParamS.param.paramAbstimmungParameter.anzahlJaJeAbstimmungsgruppe[9]));
        tfJaAbstimmungsgruppe10
                .setText(Integer.toString(ParamS.param.paramAbstimmungParameter.anzahlJaJeAbstimmungsgruppe[10]));

        tfAnzeigeBezeichnungKurzAktiv
                .setText(Integer.toString(ParamS.param.paramAbstimmungParameter.anzeigeBezeichnungKurzAktiv));

        cbWeisungenGegenantraegePortalSeparat
                .setSelected(ParamS.param.paramAbstimmungParameter.weisungenGegenantraegePortalSeparat);
        cbWeisungenGegenantraegeInternSeparat
                .setSelected(ParamS.param.paramAbstimmungParameter.weisungenGegenantraegeInternSeparat);
        cbWeisungenGegenantraegeVerlassenHVSeparat
                .setSelected(ParamS.param.paramAbstimmungParameter.weisungenGegenantraegeVerlassenHVSeparat);
    }

    /**
     * Textfelder in Parameter übernehmen.
     *
     * @return true, if successful
     */
    private boolean holeTextfelder() {
        /*++++++++++++++++Prüfen++++++++++++++++*/
        lFehlertext = "";
        pruefe12(tfBeiMehrfacherStimmabgabe_letzteAbgabeGilt_ungleicheUngueltig, "Mehrfache Stimmabgabe");
        pruefe01(tfBeiAbstimmungEintrittskartennummerZulaessig, "Bei Abstimmungseingabe auch EK zulässig");
        pruefe01(tfNichtTeilnahmeMoeglich, "Nicht-Teilnahme möglich");
        pruefe123(tfAbstimmenDuerfen_nurPraesente_PraesenteUndGewesene, "Nicht-präsente werten");
        pruefe12(tfPriorisierung_ElektronischVorPapier_PapierVorElektronisch, "Vorrang Elektronisch/Papier");
        pruefeLaenge(tfVeroeffentlichtImBundesanzeigerVom, "Veröffentlichung Bundesanzeiger", 200);
        pruefe123(tfTextVerwendenFormulare, "Ergebnis-Ausgabe - Text verwenden");
        pruefeZahlOderLeer(tfAuswertenPPListeZeilen, "Powerpoint-Export Liste");
        pruefe012(tfBeiTabletAbstimmungNurAufgerufeneStimmZettelZulaessig,
                "Tablet-Abstimmung: nur aufgerufener Stimmzettel zulässig");
        pruefe01(tfBeiTabletAbstimmungBlaettern, "Tablet-Abstimmung: Blättern/Scrollen");
        pruefe123(tfBeiTabletTextKurzOderLang, "Tablet-Abstimmung, Text verwenden");
        pruefe01(tfBeiTabletGesamtmarkierungTextAnzeigen, "Tablet-Abstimmung: Text neben Gesamtmarkierung");
        pruefe01(tfBeiTabletGesamtmarkierungFuerAlle, "Tablet-Abstimmung: Gesamtmarkierung gilt für");

        pruefeZahlOderLeer(tfJaAbstimmungsgruppe1, "Gruppe 1");
        pruefeZahlOderLeer(tfJaAbstimmungsgruppe2, "Gruppe 2");
        pruefeZahlOderLeer(tfJaAbstimmungsgruppe3, "Gruppe 3");
        pruefeZahlOderLeer(tfJaAbstimmungsgruppe4, "Gruppe 4");
        pruefeZahlOderLeer(tfJaAbstimmungsgruppe5, "Gruppe 5");
        pruefeZahlOderLeer(tfJaAbstimmungsgruppe6, "Gruppe 6");
        pruefeZahlOderLeer(tfJaAbstimmungsgruppe7, "Gruppe 7");
        pruefeZahlOderLeer(tfJaAbstimmungsgruppe8, "Gruppe 8");
        pruefeZahlOderLeer(tfJaAbstimmungsgruppe9, "Gruppe 9");
        pruefeZahlOderLeer(tfJaAbstimmungsgruppe10, "Gruppe 10");

        pruefe01(tfAnzeigeBezeichnungKurzAktiv, "Für Abstimmungspunkte auch Kurzbezeichnung Anzeige verwenden");

        if (!lFehlertext.isEmpty()) {
            this.fehlerMeldung(lFehlertext);
            return false;
        }

        /*+++++++++++++++++Speichern+++++++++++++++*/
        /*Eingabe Weisungen etc.*/
        ParamS.param.paramAbstimmungParameter.weisungVorbelegungMitSRV = CtrlParameterAbstimmungAllgemein
                .liefereAuswahlCbStimmenBelegen(cbWeisungVorbelegungMitSRV);
        ParamS.param.paramAbstimmungParameter.weisungVorbelegungMitBriefwahl = CtrlParameterAbstimmungAllgemein
                .liefereAuswahlCbStimmenBelegen(cbWeisungVorbelegungMitBriefwahl);
        ParamS.param.paramAbstimmungParameter.weisungVorbelegungMitKIAV = CtrlParameterAbstimmungAllgemein
                .liefereAuswahlCbStimmenBelegen(cbWeisungVorbelegungMitKIAV);
        ParamS.param.paramAbstimmungParameter.weisungVorbelegungMitDauer = CtrlParameterAbstimmungAllgemein
                .liefereAuswahlCbStimmenBelegen(cbWeisungVorbelegungMitDauer);
        ParamS.param.paramAbstimmungParameter.weisungVorbelegungMitOrg = CtrlParameterAbstimmungAllgemein
                .liefereAuswahlCbStimmenBelegen(cbWeisungVorbelegungMitOrg);

        ParamS.param.paramAbstimmungParameter.weisungNichtMarkierteSpeichernAlsSRV = CtrlParameterAbstimmungAllgemein
                .liefereAuswahlCbStimmenBelegen(cbWeisungNichtMarkierteSpeichernAlsSRV);
        ParamS.param.paramAbstimmungParameter.weisungNichtMarkierteSpeichernAlsBriefwahl = CtrlParameterAbstimmungAllgemein
                .liefereAuswahlCbStimmenBelegen(cbWeisungNichtMarkierteSpeichernAlsBriefwahl);
        ParamS.param.paramAbstimmungParameter.weisungNichtMarkierteSpeichernAlsKIAV = CtrlParameterAbstimmungAllgemein
                .liefereAuswahlCbStimmenBelegen(cbWeisungNichtMarkierteSpeichernAlsKIAV);
        ParamS.param.paramAbstimmungParameter.weisungNichtMarkierteSpeichernAlsDauer = CtrlParameterAbstimmungAllgemein
                .liefereAuswahlCbStimmenBelegen(cbWeisungNichtMarkierteSpeichernAlsDauer);
        ParamS.param.paramAbstimmungParameter.weisungNichtMarkierteSpeichernAlsOrg = CtrlParameterAbstimmungAllgemein
                .liefereAuswahlCbStimmenBelegen(cbWeisungNichtMarkierteSpeichernAlsOrg);

        ParamS.param.paramAbstimmungParameter.weisungHVVorbelegungMit = CtrlParameterAbstimmungAllgemein
                .liefereAuswahlCbStimmenBelegen(cbWeisungHVVorbelegungMit);
        ParamS.param.paramAbstimmungParameter.weisungHVNichtMarkierteSpeichernAls = CtrlParameterAbstimmungAllgemein
                .liefereAuswahlCbStimmenBelegen(cbWeisungHVVorbelegungMit);
        ParamS.param.paramAbstimmungParameter.abstimmungVorbelegungMit = CtrlParameterAbstimmungAllgemein
                .liefereAuswahlCbStimmenBelegen(cbAbstimmungVorbelegungMit);

        ParamS.param.paramAbstimmungParameter.eingabezwangSRV = cbEingabezwangSRV.isSelected();
        ParamS.param.paramAbstimmungParameter.eingabezwangBriefwahl = cbEingabezwangBrierfwahl.isSelected();
        ParamS.param.paramAbstimmungParameter.eingabezwangKIAV = cbEingabezwangKIAV.isSelected();
        ParamS.param.paramAbstimmungParameter.eingabezwangDauer = cbEingabezwangDauer.isSelected();
        ParamS.param.paramAbstimmungParameter.eingabezwangOrg = cbEingabezwangOrg.isSelected();
        ParamS.param.paramAbstimmungParameter.eingabezwangWeisungHV = cbEingabezwangSRV.isSelected();
        ParamS.param.paramAbstimmungParameter.eingabezwangAbstimmung = cbEingabezwangAbstimmung.isSelected();

        /*Stimmabgabe*/
        ParamS.param.paramAbstimmungParameter.beiMehrfacherStimmabgabe_letzteAbgabeGilt_ungleicheUngueltig = Integer
                .parseInt(tfBeiMehrfacherStimmabgabe_letzteAbgabeGilt_ungleicheUngueltig.getText());
        ParamS.param.paramAbstimmungParameter.beiAbstimmungEintrittskartennummerZulaessig = Integer
                .parseInt(tfBeiAbstimmungEintrittskartennummerZulaessig.getText());
        ParamS.param.paramAbstimmungParameter.nichtTeilnahmeMoeglich = Integer
                .parseInt(tfNichtTeilnahmeMoeglich.getText());

        /*Abstimmungs-Auswertung*/
        ParamS.param.paramAbstimmungParameter.abstimmenDuerfen_nurPraesente_PraesenteUndGewesene = Integer
                .parseInt(tfAbstimmenDuerfen_nurPraesente_PraesenteUndGewesene.getText());
        ParamS.param.paramAbstimmungParameter.priorisierung_ElektronischVorPapier_PapierVorElektronisch = Integer
                .parseInt(tfPriorisierung_ElektronischVorPapier_PapierVorElektronisch.getText());
        if (cb100Enthaltungen.isSelected()) {
            ParamS.param.paramAbstimmungParameter.in100PZAuchEnthaltungen = 1;
        } else {
            ParamS.param.paramAbstimmungParameter.in100PZAuchEnthaltungen = 0;
        }
        if (cb100Ungueltige.isSelected()) {
            ParamS.param.paramAbstimmungParameter.in100PZAuchUngueltige = 1;
        } else {
            ParamS.param.paramAbstimmungParameter.in100PZAuchUngueltige = 0;
        }
        if (cb100NichtStimmberechtigte.isSelected()) {
            ParamS.param.paramAbstimmungParameter.in100PZAuchNichtStimmberechtigte = 1;
        } else {
            ParamS.param.paramAbstimmungParameter.in100PZAuchNichtStimmberechtigte = 0;
        }
        if (cb100NichtTeilnahme.isSelected()) {
            ParamS.param.paramAbstimmungParameter.in100PZAuchNichtTeilnahme = 1;
        } else {
            ParamS.param.paramAbstimmungParameter.in100PZAuchNichtTeilnahme = 0;
        }
        ParamS.param.paramAbstimmungParameter.undefinierteWeisungenZaehlenAls = CtrlParameterAbstimmungAllgemein
                .liefereAuswahlCbStimmenBelegenMitJNEU(cbUndefinierteWeisungenZaehlenAls);
        if (cbUngueltigeZaehlenAlsEnthaltung.isSelected()) {
            ParamS.param.paramAbstimmungParameter.ungueltigeZaehlenAlsEnthaltung = 1;
        } else {
            ParamS.param.paramAbstimmungParameter.ungueltigeZaehlenAlsEnthaltung = 0;
        }

        /*Ergebnis-Ausgabe*/
        ParamS.param.paramAbstimmungParameter.veroeffentlichtImBundesanzeigerVom = tfVeroeffentlichtImBundesanzeigerVom
                .getText();
        String hString = tfAuswertenPPListeZeilen.getText();
        if (hString.isEmpty()) {
            ParamS.param.paramAbstimmungParameter.auswertenPPListeZeilen = 0;
        } else {
            ParamS.param.paramAbstimmungParameter.auswertenPPListeZeilen = Integer.parseInt(hString);
        }

        /*Tablet-Abstimmung*/
        ParamS.param.paramAbstimmungParameter.beiTabletAbstimmungNurAufgerufeneStimmZettelZulaessig = Integer
                .parseInt(tfBeiTabletAbstimmungNurAufgerufeneStimmZettelZulaessig.getText());
        ParamS.param.paramAbstimmungParameter.beiTabletAbstimmungBlaettern = Integer
                .parseInt(tfBeiTabletAbstimmungBlaettern.getText());
        ParamS.param.paramAbstimmungParameter.textVerwendenTablet = Integer
                .parseInt(tfBeiTabletTextKurzOderLang.getText());
        ParamS.param.paramAbstimmungParameter.beiTabletGesamtmarkierungTextAnzeigen = Integer
                .parseInt(tfBeiTabletGesamtmarkierungTextAnzeigen.getText());
        ParamS.param.paramAbstimmungParameter.beiTabletGesamtmarkierungFuerAlle = Integer
                .parseInt(tfBeiTabletGesamtmarkierungFuerAlle.getText());

        if (cbBeiTabletAllesJa.isSelected()) {
            ParamS.param.paramAbstimmungParameter.beiTabletAllesJa = 1;
        } else {
            ParamS.param.paramAbstimmungParameter.beiTabletAllesJa = 0;
        }

        if (cbBeiTabletAllesNein.isSelected()) {
            ParamS.param.paramAbstimmungParameter.beiTabletAllesNein = 1;
        } else {
            ParamS.param.paramAbstimmungParameter.beiTabletAllesNein = 0;
        }

        if (cbBeiTabletAllesEnthaltung.isSelected()) {
            ParamS.param.paramAbstimmungParameter.beiTabletAllesEnthaltung = 1;
        } else {
            ParamS.param.paramAbstimmungParameter.beiTabletAllesEnthaltung = 0;
        }

        ParamS.param.paramAbstimmungParameter.beiTabletFarbeJa = tfBeiTabletFarbeJa.getText();
        ParamS.param.paramAbstimmungParameter.beiTabletFarbeNein = tfBeiTabletFarbeNein.getText();
        ParamS.param.paramAbstimmungParameter.beiTabletFarbeEnthaltung = tfBeiTabletFarbeEnthaltung.getText();

        ParamS.param.paramAbstimmungParameter.beiTabletFarbeUnmarkiertJa = tfBeiTabletFarbeUnmarkiertJa.getText();
        ParamS.param.paramAbstimmungParameter.beiTabletFarbeUnmarkiertNein = tfBeiTabletFarbeUnmarkiertNein.getText();
        ParamS.param.paramAbstimmungParameter.beiTabletFarbeUnmarkiertEnthaltung = tfBeiTabletFarbeUnmarkiertEnthaltung
                .getText();

        ParamS.param.paramAbstimmungParameter.beiTabletFarbeGesamtmarkierungJa = tfBeiTabletFarbeGesamtmarkierungJa
                .getText();
        ParamS.param.paramAbstimmungParameter.beiTabletFarbeGesamtmarkierungNein = tfBeiTabletFarbeGesamtmarkierungNein
                .getText();
        ParamS.param.paramAbstimmungParameter.beiTabletFarbeGesamtmarkierungEnthaltung = tfBeiTabletFarbeGesamtmarkierungEnthaltung
                .getText();

        /*Anzahl Abstimmungen je Abstimmungsgruppe*/
        hString = tfJaAbstimmungsgruppe1.getText();
        if (hString.isEmpty()) {
            ParamS.param.paramAbstimmungParameter.anzahlJaJeAbstimmungsgruppe[1] = 0;
        } else {
            ParamS.param.paramAbstimmungParameter.anzahlJaJeAbstimmungsgruppe[1] = Integer.parseInt(hString);
        }

        hString = tfJaAbstimmungsgruppe2.getText();
        if (hString.isEmpty()) {
            ParamS.param.paramAbstimmungParameter.anzahlJaJeAbstimmungsgruppe[2] = 0;
        } else {
            ParamS.param.paramAbstimmungParameter.anzahlJaJeAbstimmungsgruppe[2] = Integer.parseInt(hString);
        }

        hString = tfJaAbstimmungsgruppe3.getText();
        if (hString.isEmpty()) {
            ParamS.param.paramAbstimmungParameter.anzahlJaJeAbstimmungsgruppe[3] = 0;
        } else {
            ParamS.param.paramAbstimmungParameter.anzahlJaJeAbstimmungsgruppe[3] = Integer.parseInt(hString);
        }

        hString = tfJaAbstimmungsgruppe4.getText();
        if (hString.isEmpty()) {
            ParamS.param.paramAbstimmungParameter.anzahlJaJeAbstimmungsgruppe[4] = 0;
        } else {
            ParamS.param.paramAbstimmungParameter.anzahlJaJeAbstimmungsgruppe[4] = Integer.parseInt(hString);
        }

        hString = tfJaAbstimmungsgruppe5.getText();
        if (hString.isEmpty()) {
            ParamS.param.paramAbstimmungParameter.anzahlJaJeAbstimmungsgruppe[5] = 0;
        } else {
            ParamS.param.paramAbstimmungParameter.anzahlJaJeAbstimmungsgruppe[5] = Integer.parseInt(hString);
        }

        hString = tfJaAbstimmungsgruppe6.getText();
        if (hString.isEmpty()) {
            ParamS.param.paramAbstimmungParameter.anzahlJaJeAbstimmungsgruppe[6] = 0;
        } else {
            ParamS.param.paramAbstimmungParameter.anzahlJaJeAbstimmungsgruppe[6] = Integer.parseInt(hString);
        }

        hString = tfJaAbstimmungsgruppe7.getText();
        if (hString.isEmpty()) {
            ParamS.param.paramAbstimmungParameter.anzahlJaJeAbstimmungsgruppe[7] = 0;
        } else {
            ParamS.param.paramAbstimmungParameter.anzahlJaJeAbstimmungsgruppe[7] = Integer.parseInt(hString);
        }

        hString = tfJaAbstimmungsgruppe8.getText();
        if (hString.isEmpty()) {
            ParamS.param.paramAbstimmungParameter.anzahlJaJeAbstimmungsgruppe[8] = 0;
        } else {
            ParamS.param.paramAbstimmungParameter.anzahlJaJeAbstimmungsgruppe[8] = Integer.parseInt(hString);
        }

        hString = tfJaAbstimmungsgruppe9.getText();
        if (hString.isEmpty()) {
            ParamS.param.paramAbstimmungParameter.anzahlJaJeAbstimmungsgruppe[9] = 0;
        } else {
            ParamS.param.paramAbstimmungParameter.anzahlJaJeAbstimmungsgruppe[9] = Integer.parseInt(hString);
        }

        hString = tfJaAbstimmungsgruppe10.getText();
        if (hString.isEmpty()) {
            ParamS.param.paramAbstimmungParameter.anzahlJaJeAbstimmungsgruppe[10] = 0;
        } else {
            ParamS.param.paramAbstimmungParameter.anzahlJaJeAbstimmungsgruppe[10] = Integer.parseInt(hString);
        }

        /*Sonstige*/
        ParamS.param.paramAbstimmungParameter.anzeigeBezeichnungKurzAktiv = Integer
                .parseInt(tfAnzeigeBezeichnungKurzAktiv.getText());

        ParamS.param.paramAbstimmungParameter.weisungenGegenantraegePortalSeparat = cbWeisungenGegenantraegePortalSeparat
                .isSelected();
        ParamS.param.paramAbstimmungParameter.weisungenGegenantraegeInternSeparat = cbWeisungenGegenantraegeInternSeparat
                .isSelected();
        ParamS.param.paramAbstimmungParameter.weisungenGegenantraegeVerlassenHVSeparat = cbWeisungenGegenantraegeVerlassenHVSeparat
                .isSelected();

        return true;
    }

    /**
     * ******************************Abstimmungs-Steuerung*********************************.
     */
    private int anzSteuerelementeVerfuegbar = 0;;

    /**
     * Zeige steuerelemente verfuegbar.
     */
    private void zeigeSteuerelementeVerfuegbar() {
        anzSteuerelementeVerfuegbar = 0;
        for (int i = 0; i <= KonstAbstimmungsAblauf.maxNummer; i++) {
            if (!KonstAbstimmungsAblauf.getText(i).isEmpty()) {
                anzSteuerelementeVerfuegbar++;
            }
        }

        grpnSteuerelementeVerfuegbar = new GridPane();
        grpnSteuerelementeVerfuegbar.setVgap(5);
        grpnSteuerelementeVerfuegbar.setHgap(15);

        cbSteuerelementAktiv = new CheckBox[anzSteuerelementeVerfuegbar];
        steuerelementeVerfuegbarId = new int[anzSteuerelementeVerfuegbar];
        positionInAktiv = new int[anzSteuerelementeVerfuegbar];
        verarbeitet = new int[anzSteuerelementeVerfuegbar];

        int nrInArray = 0;
        for (int i = 0; i <= KonstAbstimmungsAblauf.maxNummer; i++) {
            String steuerelementText = KonstAbstimmungsAblauf.getText(i);
            if (!steuerelementText.isEmpty()) {
                steuerelementeVerfuegbarId[nrInArray] = i;

                Label lTemp = new Label(steuerelementText);
                grpnSteuerelementeVerfuegbar.add(lTemp, 1, nrInArray);

                cbSteuerelementAktiv[nrInArray] = new CheckBox();
                grpnSteuerelementeVerfuegbar.add(cbSteuerelementAktiv[nrInArray], 0, nrInArray);

                positionInAktiv[nrInArray] = -2;
                /*Möglicherweise bereits aktive eintragen mit Position*/
                for (int i1 = 0; i1 < ParamS.param.paramAbstimmungParameter.ablaufAbstimmung.length; i1++) {
                    if (ParamS.param.paramAbstimmungParameter.ablaufAbstimmung[i1] == i) {
                        positionInAktiv[nrInArray] = i1;
                        cbSteuerelementAktiv[nrInArray].setSelected(true);
                    }
                }

                nrInArray++;
            }
        }
        pnSteuerelementeVerfuegbar.setContent(grpnSteuerelementeVerfuegbar);

    }

    /** The anz steuerelemente aktiv. */
    private int anzSteuerelementeAktiv = 0;

    /** The fuer verschieben ausgewaehltes offset in aktiven. */
    private int fuerVerschiebenAusgewaehltesOffsetInAktiven = 0;

    /**
     * Zeige steuerelemente aktiv.
     */
    private void zeigeSteuerelementeAktiv() {
        anzSteuerelementeAktiv = 0;
        for (int i = 0; i < anzSteuerelementeVerfuegbar; i++) {
            if (cbSteuerelementAktiv[i].isSelected()) {
                anzSteuerelementeAktiv++;
                if (positionInAktiv[i] == -2) {
                    positionInAktiv[i] = -1;
                } /*Neu selektiert, noch keine Position*/
            } else {
                positionInAktiv[i] = -2;
            }

            /*Auf "nicht verarbeitet" setzen*/
            verarbeitet[i] = -1;
        }

        grpnSteuerelementeAktiv = new GridPane();
        grpnSteuerelementeAktiv.setVgap(5);
        grpnSteuerelementeAktiv.setHgap(15);

        if (anzSteuerelementeAktiv > 0) {
            lblSteuerelementeAktivPosition = new Label[anzSteuerelementeAktiv];
            btnSteuerelementeAktiv = new Button[anzSteuerelementeAktiv];
            btnSteuerelementeAktivVerschieben = new Button[anzSteuerelementeAktiv];
            steuerelementeAktivId = new int[anzSteuerelementeAktiv];

            int nrInArray = 0;
            while (nrInArray < anzSteuerelementeAktiv) {
                /*Niedrigstes selektiertes aber noch nicht verarbeitet Element in "verfuegbar" suchen*/
                int gefId = -1;
                int gefPos = 1000;
                for (int i = 0; i < anzSteuerelementeVerfuegbar; i++) {
                    if (cbSteuerelementAktiv[i].isSelected() && verarbeitet[i] == -1) {
                        if (positionInAktiv[i] < gefPos) {
                            gefId = i;
                            gefPos = positionInAktiv[i];
                        }
                    }
                }
                /*Nun übertragen und auf "verarbeitet" setzen*/
                verarbeitet[gefId] = 1;
                positionInAktiv[gefId] = nrInArray;

                steuerelementeAktivId[nrInArray] = steuerelementeVerfuegbarId[gefId];

                btnSteuerelementeAktiv[nrInArray] = new Button(
                        KonstAbstimmungsAblauf.getText(steuerelementeVerfuegbarId[gefId]));
                btnSteuerelementeAktiv[nrInArray].setMaxWidth(200);
                btnSteuerelementeAktiv[nrInArray].setWrapText(true);
                grpnSteuerelementeAktiv.add(btnSteuerelementeAktiv[nrInArray], 0, nrInArray);

                lblSteuerelementeAktivPosition[nrInArray] = new Label(Integer.toString(nrInArray));
                grpnSteuerelementeAktiv.add(lblSteuerelementeAktivPosition[nrInArray], 1, nrInArray);

                btnSteuerelementeAktivVerschieben[nrInArray] = new Button("Verschieben");
                btnSteuerelementeAktivVerschieben[nrInArray].setOnAction(e -> {
                    clickedVeraendernPos(e);
                });
                grpnSteuerelementeAktiv.add(btnSteuerelementeAktivVerschieben[nrInArray], 2, nrInArray);

                nrInArray++;
            }
        }
        pnSteuerelementeAktiv.setContent(grpnSteuerelementeAktiv);

    }

    /**
     * Deaktiviere modus verschieben.
     */
    private void deaktiviereModusVerschieben() {
        btnAktiveSteuerelementeAbbruch.setVisible(false);
        btnAktiveSteuerelementeAusfuehren.setVisible(false);
        lblSteuerelementeNeuePosition.setVisible(false);
        tfSteuerelementeNeuePosition.setVisible(false);
        if (cbSteuerelementAktiv != null) {
            for (int i = 0; i < cbSteuerelementAktiv.length; i++) {
                cbSteuerelementAktiv[i].setDisable(false);
            }
        }
        if (lblSteuerelementeAktivPosition != null) {
            for (int i = 0; i < lblSteuerelementeAktivPosition.length; i++) {
                btnSteuerelementeAktiv[i].setDisable(false);
                btnSteuerelementeAktivVerschieben[i].setDisable(false);
            }
        }
    }

    /**
     * Aktiviere modus verschieben.
     */
    private void aktiviereModusVerschieben() {
        btnAktiveSteuerelementeAbbruch.setVisible(true);
        btnAktiveSteuerelementeAusfuehren.setVisible(true);
        lblSteuerelementeNeuePosition
                .setText(Integer.toString(fuerVerschiebenAusgewaehltesOffsetInAktiven) + " Verschieben an:");
        lblSteuerelementeNeuePosition.setVisible(true);
        tfSteuerelementeNeuePosition.setVisible(true);
        tfSteuerelementeNeuePosition.setText("");
        if (cbSteuerelementAktiv != null) {
            for (int i = 0; i < cbSteuerelementAktiv.length; i++) {
                cbSteuerelementAktiv[i].setDisable(true);
            }
        }
        if (lblSteuerelementeAktivPosition != null) {
            for (int i = 0; i < lblSteuerelementeAktivPosition.length; i++) {
                btnSteuerelementeAktiv[i].setDisable(true);
                btnSteuerelementeAktivVerschieben[i].setDisable(true);
            }
        }

    }

    /**
     * On btn refresh steuerelemente.
     *
     * @param event the event
     */
    @FXML
    void onBtnRefreshSteuerelemente(ActionEvent event) {
        zeigeSteuerelementeAktiv();
        deaktiviereModusVerschieben();
    }

    @FXML
    void onBtnLadenPresetAblauf(ActionEvent event) {
        Stage neuerDialog = new Stage();
        CaIcon.master(neuerDialog);
        CtrlParameterAbstimmungsparameterLadenAblauf controllerFenster = new CtrlParameterAbstimmungsparameterLadenAblauf();
        controllerFenster.init(neuerDialog);

        CaController caController = new CaController();
        caController.open(neuerDialog, controllerFenster,
                "/de/meetingapps/meetingclient/meetingHVMaster/ParameterAbstimmungsparameterAblauf.fxml", 1500, 760,
                "Laden Preset-Ablauf", true);
        if (controllerFenster.umstellungWurdeDurchgefuehrt) {
            int anz=controllerFenster.gewaehlterAblaufListe.size();
            ParamS.param.paramAbstimmungParameter.ablaufAbstimmung=new int[anz];
            for (int i=0;i<anz;i++) {
                ParamS.param.paramAbstimmungParameter.ablaufAbstimmung[i]=controllerFenster.gewaehlterAblaufListe.get(i);
            }
            zeigeSteuerelementeVerfuegbar();
            zeigeSteuerelementeAktiv();
            deaktiviereModusVerschieben();

        }
    }
    
    /**
     * Clicked veraendern pos.
     *
     * @param event the event
     */
    @FXML
    void clickedVeraendernPos(ActionEvent event) {

        for (int i = 0; i < btnSteuerelementeAktivVerschieben.length; i++) {
            if (event.getSource() == btnSteuerelementeAktivVerschieben[i]) {
                fuerVerschiebenAusgewaehltesOffsetInAktiven = i;
            }
        }
        aktiviereModusVerschieben();
    }

    /**
     * On btn aktive steuerelemente abbruch.
     *
     * @param event the event
     */
    @FXML
    void onBtnAktiveSteuerelementeAbbruch(ActionEvent event) {
        deaktiviereModusVerschieben();
    }

    /**
     * On btn aktive steuerelemente ausfuehren.
     *
     * @param event the event
     */
    @FXML
    void onBtnAktiveSteuerelementeAusfuehren(ActionEvent event) {

        String hString = tfSteuerelementeNeuePosition.getText();
        if (!CaString.isNummern(hString)) {
            return;
        }
        if (hString.length() > 5) {
            return;
        }
        int neuePos = Integer.parseInt(hString);

        for (int i = 0; i < cbSteuerelementAktiv.length; i++) {
            if (positionInAktiv[i] >= neuePos) {
                positionInAktiv[i]++;
            }
            if (steuerelementeVerfuegbarId[i] == steuerelementeAktivId[fuerVerschiebenAusgewaehltesOffsetInAktiven]) {
                positionInAktiv[i] = neuePos;
            }
        }

        deaktiviereModusVerschieben();
        zeigeSteuerelementeAktiv();

    }

    /**
     * *****************Übergreifende Elemente*****************************.
     *
     * @param event the event
     */
    @FXML
    void onBtnSpeichern(ActionEvent event) {

        /*Parameter-Textfelder in parameter übertragen*/
        boolean brc = holeTextfelder();
        if (!brc) {
            return;
        }

        /****** Ablauf Abstimmung übertragen ******/
        int anzSteuerElemente = 0;
        if (steuerelementeAktivId == null) {
            anzSteuerElemente = 0;
            steuerelementeAktivId = new int[0];
        } else {
            anzSteuerElemente = steuerelementeAktivId.length;
        }
        ParamS.param.paramAbstimmungParameter.ablaufAbstimmung = steuerelementeAktivId;
        ParamS.param.paramAbstimmungParameter.ablaufAbstimmungErledigt = new boolean[anzSteuerElemente];
        for (int i = 0; i < anzSteuerElemente; i++) {
            ParamS.param.paramAbstimmungParameter.ablaufAbstimmungErledigt[i] = false;
        }

        DbBundle lDbBundle = new DbBundle();
        StubParameter stubParameter = new StubParameter(false, lDbBundle);
        int erg = stubParameter.updateHVParam_all(ParamS.param);
        if (erg == CaFehler.pfXyWurdeVonAnderemBenutzerVeraendert) {
            CaZeigeHinweis caZeigeHinweis = new CaZeigeHinweis();
            caZeigeHinweis.zeige(eigeneStage,
                    "Achtung, anderer User im System - Ihre Änderungen wurden nicht gespeichert!");
        }
        eigeneStage.hide();

    }

}
